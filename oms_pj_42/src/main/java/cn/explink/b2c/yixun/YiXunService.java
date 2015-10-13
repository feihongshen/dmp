package cn.explink.b2c.yixun;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.util.DateTimeUtil;

@Service
public class YiXunService {
	private static Logger logger = LoggerFactory.getLogger(YiXunService.class);
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	/**
	 * 获取易迅配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String toYiXunServicePost(String url) throws IOException {
		String[] uri_params = url.split("\\?");
		return toYiXunService(uri_params[0], uri_params[1], "POST");
	}

	public static String toYiXunService(String url, String params, String method) throws IOException {

		String responseXml = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);

		String[] paramStr = params.split("&");// 按“&”拆分
		if (paramStr != null && paramStr.length > 0) {
			for (int i = 0; i < paramStr.length; i++) {
				postMethod.addParameter(paramStr[i].split("=")[0], paramStr[i].split("=")[1]);// 按“=”拆分，第一个做为参数名，第二个作为参数值
			}
		}
		try {
			httpClient.executeMethod(postMethod); // post数据
			responseXml = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		logger.info("RE : " + method + " : " + url + "?" + params + " : " + responseXml);
		return responseXml;

	}

	public YiXunFlowEnum getYiXunFlowEnum(long deliveryState, long flowordertype) {
		for (YiXunFlowEnum YEnum : YiXunFlowEnum.values()) {
			if (deliveryState == YEnum.getDeliveryState() && flowordertype == YEnum.getFlowordertype()) {
				return YEnum;
			}
		}
		return null;

	}

	/**
	 * 问题件反馈，配送结果反馈 接口
	 */
	public long feedback_status() {
		long calcCount = 0;
		YiXun yixun = getYiXunSettingMethod(B2cEnum.YiXun.getKey()); // 获取配置信息
		if (!isYiXunOpen(B2cEnum.YiXun.getKey())) {
			logger.error("未开易迅的对接!");
			return -1;
		}

		// /状态反馈
		calcCount += FeedbackXMLByFlowOrderStatus(yixun, 1); // 签收拒收
		calcCount += FeedbackXMLByFlowOrderStatus(yixun, 2); // 问题件

		return calcCount;

	}

	/**
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param dangdang
	 */
	private long FeedbackXMLByFlowOrderStatus(YiXun yixun, int code) {
		long calcCount = 0;
		try {
			List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(0, yixun.getCustomerids(), yixun.getCount());

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有要推送[易迅][二次推送]数据,当前时间:[" + DateTimeUtil.getNowTime() + "]");
				return 0;
			} else {
				String b2cids = "";
				for (B2CData data : datalist) {
					JSONObject jsonObj = JSONObject.fromObject(data.getJsoncontent());
					YiXunDTO yixunDTO = (YiXunDTO) JSONObject.toBean(jsonObj, YiXunDTO.class);
					int send_b2c_flag = 1;
					if (code == 1) { // 签收
						String signresult = toYiXunServicePost(yixunDTO.toReportSignstatusUrl());

						if (!signresult.contains("true")) {
							send_b2c_flag = 2;
						}
						logger.info("推送[易迅][二次推送][签收反馈]数据=={}", yixunDTO.toReportSignstatusUrl());
					} else { // 问题件
						String problemresult = toYiXunServicePost(yixunDTO.toReportAbnormalUrl());
						if (!problemresult.contains("true")) {
							send_b2c_flag = 2;
						}
						logger.info("推送[易迅][二次推送][问题件]数据=={}", yixunDTO.toReportAbnormalUrl());
					}

					b2cDataDAO.updateSQLResponseStatus(data.getB2cid(), send_b2c_flag);
					b2cids += data.getB2cid() + ",";
				}
				b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";
				// 发送给dmp
				flowFromJMSB2cService.sendTodmp(b2cids);
				calcCount += datalist.size();
			}

		} catch (Exception e) {
			logger.error("推送[易迅][二次推送]订单异常!", e);
		}
		return calcCount;
	}

	public YiXun getYiXunSettingMethod(int key) {
		YiXun yixun = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			yixun = (YiXun) JSONObject.toBean(jsonObj, YiXun.class);
		} else {
			yixun = new YiXun();
		}

		return yixun;
	}

	private boolean isYiXunOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

}
