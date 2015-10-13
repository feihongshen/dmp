package cn.explink.b2c.benlaishenghuo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class BenlaishenghuoService {
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	GetDmpDAO getdmpDAO;

	private Logger logger = LoggerFactory.getLogger(BenlaishenghuoService.class);

	/**
	 * 订单查询 接口
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public long feedback_status(int key) {
		long calcCount = 0;
		try {
			Benlaishenghuo benlai = getBenlaishenghuo(key);

			if (!isBenlaiOpen(key)) {
				logger.error("未开[本来生活]日志反馈的对接!");
				return -1;
			}
			if (benlai.getTrack_url().equals("") || benlai.getTrack_url() == null) {
				logger.error("不存在【本来生活】日志反馈链接");
				return 0;
			}

			int reload = 0;
			while (true) {
				reload++;
				if (reload > 100) {
					logger.error("循环次数超过100次，跳出死循环");
					return 0;
				}
				try {
					List<B2CData> datalist = b2cDataDAO.getDataForBenlai(benlai.getCustomerid(), "200");
					if (datalist == null || datalist.size() == 0) {
						logger.info("当前没有要推送[本来生活]的数据.......");
						return 0;
					}
					calcCount += datalist.size();
					String cwb = "";
					String orderxml = "";
					String b2cid = "";
					StringBuffer b2cids = new StringBuffer();
					for (B2CData b : datalist) {
						if (b.getCwb().equals(cwb)) {
							continue;
						}

						List<B2CData> cwblist = b2cDataDAO.getCwB(b.getCwb());
						String callback = "";
						if (cwblist.size() > 0 && cwblist != null) {
							callback = PushXmlForReturn(b, orderxml, cwblist, b2cids);
						}
						orderxml += callback;
						cwb = b.getCwb();

					}
					b2cid = b2cids.toString().substring(0, b2cids.length() - 1);
					String requestxml = "<BatchOrdersUpdate>" + "<orders>" + orderxml + "</orders>" + "</BatchOrdersUpdate>";
					String password = MD5Util.getMD5String32Bytes(requestxml, benlai.getUserkey());
					logger.info("发送给【本来网】的xml={}", requestxml);
					String returnxml = getPostMethodResult(benlai.getTrack_url(), password, requestxml, benlai.getsShippedCode());
					Map<String, Object> map = XmlDocByTmallMap(returnxml);
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("orderlist");
					for (Map<String, Object> l : list) {
						if (l.get("Success").toString().equalsIgnoreCase("True")) {
							b2cDataDAO.UpdateBenlaiSql(l.get("orderNo").toString(), l.get("Remark").toString(), "1", b2cid);// 成功推送
						} else if (l.get("Success").toString().equalsIgnoreCase("False")) {
							b2cDataDAO.UpdateBenlaiSql(l.get("orderNo").toString(), l.get("Remark").toString(), "2", b2cid);// 失败推送
						} else {
							b2cDataDAO.UpdateBenlaiSql(l.get("orderNo").toString(), l.get("Remark").toString(), "3", b2cid);// 不给推送
						}
					}

					logger.info("【本来网】订单日志查询返回的xml={}", returnxml);

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("【本来网】订单日志查询接口出现异常", e);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calcCount;
	}

	private String PushXmlForReturn(B2CData b, String callback, List<B2CData> cwblist, StringBuffer b2cids) {
		String Status = "";
		StringBuffer sb = new StringBuffer();
		String statusTime = "";
		for (B2CData l : cwblist) {
			b2cids.append(l.getB2cid() + ",");
			String json_content = l.getJsoncontent();
			JSONObject job = JSONObject.fromObject(json_content);
			long orderSatus = l.getFlowordertype();
			Status = getBenlaishenghuoStatus(orderSatus);
			String operatetype = job.getString("type");
			statusTime = job.getString("acceptTime");
			if (operatetype.equals("3")) {
				sb.append("<step>" + "<acceptTime>" + statusTime + "</acceptTime>" + "<acceptAddress>" + job.getString("status") + "</acceptAddress>" + "<podresultname>" + Status + "</podresultname>"
						+ "<operatetype>" + operatetype + "</operatetype>" + "</step>");
			} else if (operatetype.equals("4")) {// 派件
				sb.append("<step>" + "<acceptTime>" + statusTime + "</acceptTime>" + "<acceptAddress>" + job.getString("status") + "</acceptAddress>" + "<podresultname>派件</podresultname>"
						+ "<dispatchname>" + job.getString("courier") + "</dispatchname>" + "<dispatchtel>" + job.getString("contact_phone") + "</dispatchtel>" + "<operatetype>" + operatetype
						+ "</operatetype>" + "</step>");
			} else if (operatetype.equals("2")) {
				sb.append("<step>" + "<acceptTime>" + statusTime + "</acceptTime>" + "<acceptAddress>" + job.getString("status") + "</acceptAddress>" + "<podresultname>拒收</podresultname>"
						+ "<operatetype>" + operatetype + "</operatetype>" + "</step>");
			} else {
				sb.append("<step>" + "<acceptTime>" + statusTime + "</acceptTime>" + "<acceptAddress>" + job.getString("status") + "</acceptAddress>" + "<podresultname>签收</podresultname>"
						+ "<signer>" + job.getString("signman") + "</signer>" + "<operatetype>" + operatetype + "</operatetype>" + "</step>");
			}

		}
		sb.toString();
		callback = "<order>" + "<orderNo>" + b.getCwb() + "</orderNo>" + "<orderStatus>" + Status + "</orderStatus>" + "<statusTime>" + statusTime + "</statusTime>" + "<steps>" + sb
				+ "</steps></order>";
		return callback;
	}

	private boolean isBenlaiOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	private String getBenlaishenghuoStatus(long orderSatus) {
		String Status = "";
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			Status = "分站到货";

		}
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			Status = "分站领货";
		}
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.RuKu.getValue()) {
			Status = "入库";
		}
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			Status = "出库";
		}
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.YiShenHe.getValue()) {
			Status = "已审核";
		}
		return Status;
	}

	/**
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param saohuobang
	 *            在express_b2cdata_search_saohuobang
	 */
	public Benlaishenghuo getBenlaishenghuo(int key) {
		Benlaishenghuo benlai = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			benlai = (Benlaishenghuo) JSONObject.toBean(jsonObj, Benlaishenghuo.class);
		} else {
			benlai = new Benlaishenghuo();
		}
		return benlai;
	}

	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * 发送方式
	 */
	public String getPostMethodResult(String url, String token, String post_xml, String sShippedCode) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("logicdata", post_xml), new NameValuePair("checkdata", token), new NameValuePair("sShippedCode", sShippedCode), }; // 原始信息
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	/*
	 * 解析xml
	 */

	public static Map<String, Object> XmlDocByTmallMap(String fileName) throws Exception {
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		Map<String, Object> jsontotal = new HashMap<String, Object>();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());
				Map<String, Object> jsondetail = new HashMap<String, Object>();
				for (Iterator<Element> k = node.elementIterator(); k.hasNext();) {
					Element node_child = (Element) k.next();
					jsondetail.put(node_child.getName(), node_child.getText());
				}
				jarry.add(jsondetail);
			}

			if (jarry != null && jarry.size() > 0) {
				returnMap.put("orderlist", jarry);
			}

		}
		return returnMap;
	}
	/*
	 * public static void main(String[] args) { String xml=
	 * "<ResponseOrders><ResponseOrder><waybillCode>355688974</waybillCode><Success>True</Success><Remark>成功</Remark></ResponseOrder><ResponseOrder><waybillCode>100000000</waybillCode><Success>False</Success><Remark>运单信息不详</Remark></ResponseOrder></ResponseOrders>"
	 * ; String eexml=
	 * "<LogisticsResponse><Result>2</Result><Remark>成功</Remark><orders><order><orderNo>S661000206983</orderNo><CompanyId>BenLai</CompanyId><Success>False</Success><Remark>已经签收或拒收，不接收其他操作</Remark></order><order><orderNo>S661000206984</orderNo><CompanyId>BenLai</CompanyId><Success>True</Success><Remark></Remark></order><order><orderNo>S661000206986</orderNo><CompanyId>BenLai</CompanyId><Success>True</Success><Remark></Remark></order></orders></LogisticsResponse>"
	 * ; BenlaishenghuoService s=new BenlaishenghuoService(); try { Map<String,
	 * Object> map=s.XmlDocByTmallMap(eexml); List<Map<String, Object>> list=
	 * (List<Map<String, Object>>) map.get("orderlist"); for(Map<String, Object>
	 * l:list){ if(l.get("Success").toString().equalsIgnoreCase("True")){
	 * System.out.println(l.get("orderNo").toString());//成功推送 }else
	 * if(l.get("Success").toString().equalsIgnoreCase("False")){
	 * System.out.println(l.get("Remark").toString());//成功推送 }else{
	 * System.out.println(3);//成功推送 } } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

}
