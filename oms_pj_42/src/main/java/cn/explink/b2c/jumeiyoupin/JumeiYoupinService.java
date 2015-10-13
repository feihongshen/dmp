package cn.explink.b2c.jumeiyoupin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.Mail;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class JumeiYoupinService {
	private Logger logger = LoggerFactory.getLogger(JumeiYoupinService.class);
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	/**
	 * 获取聚美优品配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public String getObjectMethod(int key) {
		JointEntity obj = new JointEntity();
		String propertity = "";
		try {
			obj = getdmpDAO.getJointEntity(key);
			propertity = obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertity;
	}

	public JuMeiYouPin getJuMeiYouPinSettingMethod(int key) {
		JuMeiYouPin juMeiYouPin = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			juMeiYouPin = (JuMeiYouPin) JSONObject.toBean(jsonObj, JuMeiYouPin.class);
		} else {
			juMeiYouPin = new JuMeiYouPin();
		}

		return juMeiYouPin;
	}

	public int getStateForPos(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = getdmpDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	private boolean isJuMeiOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 出库派送，配送结果反馈 接口
	 */
	public long feedback_status() {

		JuMeiYouPin juMeiYouPin = getJuMeiYouPinSettingMethod(B2cEnum.JuMeiYouPin.getKey()); // 获取配置信息
		if (!isJuMeiOpen(B2cEnum.JuMeiYouPin.getKey())) {
			logger.info("未开启[聚美优品]对接！");
			return -1;
		}

		// 推送给聚美优品
		return send(juMeiYouPin);

	}

	private long send(JuMeiYouPin juMeiYouPin) {
		long calcCount = 0;
		calcCount += sendByType(juMeiYouPin, 0, FlowOrderTypeEnum.RuKu.getValue());
		calcCount += sendByType(juMeiYouPin, 0, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		calcCount += sendByType(juMeiYouPin, 0, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		calcCount += sendByType(juMeiYouPin, 0, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		calcCount += sendByType(juMeiYouPin, 0, FlowOrderTypeEnum.YiShenHe.getValue());

		return calcCount;
	}

	public long sendByType(JuMeiYouPin juMeiYouPin, int state, int flowordertype) {
		long calcCount = 0;

		try {
			// 获取聚美优品等待发送的订单
			String url = juMeiYouPin.getTuisong_url();
			String md5data = "";
			String jumeiKey = juMeiYouPin.getPrivate_key();
			do {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
					String unixstamp = df.format(new Date());
					// 推送入库的订单
					List<B2CData> b2CDataList = b2cDataDAO.getDataListByFlowStatus(flowordertype, juMeiYouPin.getCustomerids(), juMeiYouPin.getCount());
					String trackInfo = orderToXml(b2CDataList, juMeiYouPin.getExpress_id());
					if (!"".equals(trackInfo)) {
						md5data = MD5Util.md5(b2CDataList.get(0).getCwb() + unixstamp);
						md5data = MD5Util.md5(md5data + jumeiKey);
						Map params = new HashMap();
						params.put("trackInfo", trackInfo);
						params.put("cwb", b2CDataList.get(0).getCwb());
						params.put("unixstamp", unixstamp);
						params.put("md5data", md5data);
						for (int i = 0; i < 3; i++) {
							int result = getResultMessage(url, params, b2CDataList);
							if (result > 0 && result != 2) { // 参数是空的
								break; // 跳出当前的推送
							} else if (result == 2) {
								try {
									Thread.sleep(2000 * i);// 重复请求的等待时间
								} catch (InterruptedException e) {
									logger.error("[聚美优品]sleep报异常！");
									return 0;
								}
							} else {
								return 0;// 跳出整个方法
							}
						}
					} else {
						logger.info("[聚美优品]推送给聚美的数据，状态为flowordertype：{} 没有可推送数据！", flowordertype);
						return 0;// 整个方法结束
					}
					calcCount += b2CDataList.size();

				} catch (Exception e) {
					String exptMessage = "处理聚美优品状态推送发生异常，异常原因=" + e;
					exptMessage = ExceptionTrace.getExceptionTrace(e, exptMessage);
					Mail.LoadingAndSendMessage(exptMessage);
					logger.error(exptMessage);

					return 0;
				}
			} while (true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calcCount;
	}

	public int sendByCwbs(String cwbs, long send_b2c_flag) {
		JuMeiYouPin juMeiYouPin = getJuMeiYouPinSettingMethod(B2cEnum.JuMeiYouPin.getKey()); // 获取配置信息
		if (!isJuMeiOpen(B2cEnum.JuMeiYouPin.getKey())) {
			logger.info("未开启[聚美优品]对接！");
			return 0;
		}
		// 获取聚美优品等待发送的订单
		String url = juMeiYouPin.getTuisong_url();
		String md5data = "";
		String jumeiKey = juMeiYouPin.getPrivate_key();
		do {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
				String unixstamp = df.format(new Date());
				// 推送入库的订单
				List<B2CData> b2CDataList = b2cDataDAO.getDataListByCwbs(cwbs, send_b2c_flag);
				String trackInfo = orderToXml(b2CDataList, juMeiYouPin.getExpress_id());
				if (!"".equals(trackInfo)) {
					md5data = MD5Util.md5(b2CDataList.get(0).getCwb() + unixstamp);
					md5data = MD5Util.md5(md5data + jumeiKey);
					Map params = new HashMap();
					params.put("trackInfo", trackInfo);
					params.put("cwb", b2CDataList.get(0).getCwb());
					params.put("unixstamp", unixstamp);
					params.put("md5data", md5data);
					for (int i = 0; i < 3; i++) {
						int result = getResultMessage(url, params, b2CDataList);
						if (result > 0 && result != 2) { // 参数是空的
							break; // 跳出当前的推送
						} else if (result == 2) {
							try {
								Thread.sleep(2000 * i);// 重复请求的等待时间
							} catch (InterruptedException e) {
								logger.error("[聚美优品]sleep报异常！");
								return 0;
							}
						} else {
							return 0;// 跳出整个方法
						}
					}
				} else {
					logger.info("[聚美优品]推送给聚美的数据， 没有可推送数据！");
					return 0;// 整个方法结束
				}
			} catch (Exception e) {
				String exptMessage = "处理聚美优品状态推送发生异常，异常原因=" + e;
				exptMessage = ExceptionTrace.getExceptionTrace(e, exptMessage);
				Mail.LoadingAndSendMessage(exptMessage);
				logger.error(exptMessage);

				return 0;
			}
		} while (true);

	}

	/**
	 * 
	 * @param url
	 *            请求聚美接口路径
	 * @param params
	 *            请求的参数
	 * @return -3 DocumentException 异常 -2 IOException 异常 -1 HttpException 异常 0
	 *         传给聚美的参数为空 1 推送返回成功 2 返回全部失败 3 返回部分失败 201 获取map的值有异常 404
	 *         聚美返回非“true”和非“false”
	 */
	private int getResultMessage(String url, Map params, List<B2CData> b2CDataList) {

		if (params == null || params.isEmpty()) {
			return 0; // 参数是空的
		}
		String md5data = params.get("md5data").toString();
		String unixstamp = params.get("unixstamp").toString();
		String trackInfo = params.get("trackInfo").toString();
		String cwb = params.get("cwb").toString();
		// 把加密方式拼接到url后面
		String paraStrs = "?cwb=" + cwb + "&unixstamp=" + unixstamp + "&sign=" + md5data;

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url + paraStrs);

		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("trackInfo", trackInfo)

		};
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		try {
			httpClient.executeMethod(postMethod); // post数据
			logger.info("[聚美优品]聚美返回xml：{}", postMethod.getResponseBodyAsString());
			SAXReader reader = new SAXReader();
			Document document = reader.read(postMethod.getResponseBodyAsStream(), "UTF-8");
			return parXml(document, b2CDataList);
		} catch (HttpException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -2;
		} catch (DocumentException e) {
			e.printStackTrace();
			return -3;
		} finally {
			postMethod.releaseConnection();
		}
	}

	private String orderToXml(List<B2CData> b2CDataList, String company) {
		if (b2CDataList == null || b2CDataList.size() == 0) {
			return "";
		}
		StringBuffer msg = new StringBuffer();
		msg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		msg.append("<result>");
		msg.append("<company>" + company + "</company>");
		for (int i = 0; i < b2CDataList.size(); i++) {

			B2CData b2cData = b2CDataList.get(i);
			JSONObject json = JSONObject.fromObject(b2cData.getJsoncontent());
			msg.append("<row resultcount=\"" + (i + 1) + "\">");
			msg.append("<cwb>" + json.getString("cwb") + "</cwb>");
			msg.append("<trackdatetime>" + json.getString("trackdatetime") + "</trackdatetime>");
			msg.append("<trackevent>" + json.getString("trackevent") + "</trackevent>");
			msg.append("<trackstatus>" + json.getString("trackstatus") + "</trackstatus>");
			msg.append("</row>");

		}
		msg.append("</result>");
		logger.info("[聚美优品]推送给聚美优品的xml:{}", msg.toString());
		return msg.toString();
	}

	private int parXml(Document document, List<B2CData> b2CDataList) {
		Element rootElm = document.getRootElement();
		String successResult = rootElm.elementText("success");
		Element foo;
		// String reason=""; //聚美返回的

		if ("true".equalsIgnoreCase(successResult)) { // 返回全部成功
			try {
				String b2cids = "";
				for (B2CData b2c : b2CDataList) {
					b2cids += b2c.getB2cid() + ",";
				}
				if (b2cids.length() > 0) {
					b2cids = b2cids.substring(0, b2cids.length() - 1);
				}
				b2cDataDAO.updateDataByCwb(1, b2cids, "", 0, "");
				logger.info("[聚美优品]聚美返回全部成功，订单号串:{}", b2cids);
				// 发送给dmp
				flowFromJMSB2cService.sendTodmp(b2cids);
			} catch (Exception e) {
				logger.error("[聚美优品]聚美返回全部成功，但是保存修改数据库失败", e);
			}

			return 1;
		}
		if ("False".equalsIgnoreCase(successResult)) { // 返回失败
			String reason = rootElm.elementText("reason");
			if (reason != null && !"".equals(reason)) { // 返回全部失败
				try {
					String b2cids = "";
					for (B2CData b2c : b2CDataList) {
						b2cids += b2c.getB2cid() + ",";
					}
					if (b2cids.length() > 0) {
						b2cids = b2cids.substring(0, b2cids.length() - 1);
					}
					b2cDataDAO.updateDataByCwb(2, b2cids, "", 0, reason);
				} catch (Exception e) {
					logger.error("[聚美优品]聚美返回全部失败，但是保存修改数据库失败", e);
				}
				return 2;
			}

			String failedReason = "";
			String cwbs = "";// 失败的订单号
			for (Iterator i = rootElm.elementIterator(); i.hasNext();) {
				foo = (Element) i.next();
				try {
					if (foo.elementText("cwb") != null) {
						logger.info("[聚美优品]部分推送失败，失败订单号:{} 原因：{}", foo.elementText("cwb"), foo.elementText("reason"));
						cwbs += "'" + foo.elementText("cwb") + "',";
						failedReason = foo.elementText("reason");
					}
				} catch (Exception e) {
					logger.error("[聚美优品]聚美返回xml解析订单号和原因失败[部分失败]", e);
					return -202;
				}

			}

			if (cwbs.length() > 0) {
				cwbs = cwbs.substring(0, cwbs.length() - 1);
				logger.info("[聚美优品]部分推送失败，失败订单号串:{}", cwbs);
				try {
					String b2cids = "";
					for (B2CData b2c : b2CDataList) {
						b2cids += b2c.getB2cid() + ",";
					}
					if (b2cids.length() > 0) {
						b2cids = b2cids.substring(0, b2cids.length() - 1);
					}
					b2cDataDAO.updateDataByCwb(1, b2cids, cwbs, 1, "成功");// 保存成功的部分
					b2cDataDAO.updateDataByCwb(2, b2cids, cwbs, 2, failedReason);// 保存失败的部分
					// 发送给dmp
					flowFromJMSB2cService.sendTodmp(b2cids);
				} catch (Exception e) {
					logger.error("[聚美优品]聚美返回部分失败，但是保存修改数据库失败");
				}
			} else {
				logger.error("[聚美优品]聚美返回xml解析订单号和原因失败 没有解析出订单号");
				return -202;
			}
			return 3;
		}
		return -404;
	}

	public static void main(String[] args) {
		int i = 0;
		try {
			while (true) {
				String str = null;

				i++;
				if (str.toString().equals("1111")) {
					System.out.println(i);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据B2CId查询对应的 异常信息
	 * 
	 * @param reasontype
	 * @param leavedreasonid
	 * @param backreasonid
	 * @param customerids
	 * @return
	 */
	public ExptReason getExptReasonByJuMei(long leavedreasonid, long backreasonid, String customerids) {
		ExptReason exptreason = null;
		if (leavedreasonid != 0) { // 滞留
			exptreason = getdmpDAO.getExptCodeJointByB2c(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), customerids);
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("17");
				exptreason.setExpt_msg("其他原因");
			}

		} else if (backreasonid != 0) { // 拒收
			exptreason = getdmpDAO.getExptCodeJointByB2c(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), customerids);
			if (exptreason.getExpt_code() == null || exptreason.getExpt_code().equals("")) {
				exptreason.setExpt_code("27");
				exptreason.setExpt_msg("其他原因");
			}
		}

		return exptreason == null ? new ExptReason() : exptreason;
	}

}
