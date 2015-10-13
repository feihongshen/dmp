package cn.explink.b2c.saohuobang;

import java.io.IOException;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.saohuobang.xml.Order;
import cn.explink.b2c.saohuobang.xml.BatchQueryRequest;
import cn.explink.b2c.saohuobang.xml.SaohuobangUnmarchal;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.SaohuobangSign;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class SaohuobangomsService {
	@Autowired
	SaohuobangDao saohuobangDao;
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	GetDmpDAO getdmpDAO;
	private Logger logger = LoggerFactory.getLogger(SaohuobangomsService.class);

	/**
	 * 订单查询 接口
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public String feedback_status(String xml, String sign) {
		List<Order> orderlist = null;
		String cwb = "";
		String mainNo = "";
		BatchQueryRequest rootnote = null;
		try {
			rootnote = SaohuobangUnmarchal.Unmarchal(xml);
			orderlist = rootnote.getOrders().getListorder();
			JointEntity entity = getdmpDAO.getJointEntityByClientID(rootnote.getClientID());
			if (entity == null) {
				return "";// 英文格式
			}
			int key = entity.getJoint_num();
			JSONObject jsonObj = JSONObject.fromObject(entity.getJoint_property());
			Saohuobang saohuobang = (Saohuobang) JSONObject.toBean(jsonObj, Saohuobang.class);

			if (saohuobang.getIsopendownload() != 1) {
				logger.error("未开[扫货帮oms" + key + "]的对接!");
				return "";
			}
			// 签名验证
			String password = SaohuobangSign.encryptSign_Method(xml, saohuobang.getKey());
			logger.info("扫货帮" + key + "对接,我的签名是={},得到的签名是={}", password, sign);
			if (!password.equals(sign)) {
				return jiontXmlForRetrunSaohuobang(0, "", saohuobang.getProviderID(), "Search", "签名错误");
			}
			for (Order list : orderlist) {
				cwb = list.getMailNo();
				mainNo += "'" + cwb + "'" + ",";
			}
			mainNo = mainNo.length() > 0 ? mainNo.substring(0, mainNo.length() - 1) : "";

			String retrunxml = BatchQueryRequestXmlForSaohuobang(saohuobang, mainNo);
			logger.info("订单[Search" + key + "]接口返回扫货帮,xml={}", retrunxml);

			return retrunxml;
		} catch (Exception e) {
			logger.info("订单查询接口出现问题{},异常{}", e, e.getMessage());
			return jiontXmlForRetrunSaohuobang(0, "", rootnote.getLogisticProviderID(), "Exception", e.getMessage());
		}

	}

	/**
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param saohuobang
	 *            在express_b2cdata_search_saohuobang
	 */
	private String BatchQueryRequestXmlForSaohuobang(Saohuobang saohuobang, String cwbs) {

		long count = saohuobangDao.getDataByCwbs(cwbs);

		if (count == 0) {
			logger.info("当前没有要推送[扫货帮]的数据,订单号:{}", cwbs);
			return jiontXmlForRetrunSaohuobang(0, "", saohuobang.getProviderID(), "false", "当前没有要推送[扫货帮]的数据");
		} else {
			try {
				String requestXML = StringXMLBody(saohuobang, cwbs);
				requestXML = getXmlForCallback(requestXML, saohuobang);

				logger.info("当前面单号{},当前推送[扫货帮]XML：{}", cwbs, requestXML);
				return requestXML;
			} catch (Exception e) {
				logger.info("[扫货帮]订单查询接口出现【异常】{},原因是={}", e, e.getMessage());
				return jiontXmlForRetrunSaohuobang(0, cwbs, saohuobang.getProviderID(), "Exception", e.getMessage());
			}
		}
	}

	private String StringXMLBody(Saohuobang saohuobang, String cwb) {
		String callback = "";
		String aString[] = cwb.split(",");
		for (String a : aString) {
			StringBuffer sb = new StringBuffer();
			List<SaohuobangXMLNote> sList = saohuobangDao.getCwBForList(a);
			if (sList == null || sList.size() == 0) {
				continue;
			}
			String Status = "";
			for (SaohuobangXMLNote saohuobangXmlNote : sList) {

				sb.append("<step>" + "<acceptTime>" + saohuobangXmlNote.getOptiontime() + "</acceptTime>" + "<acceptAddress>" + saohuobangXmlNote.getAddress() + "</acceptAddress>" + "<remark>"
						+ saohuobangXmlNote.getRemark() + "</remark>" + "</step>");
				String orderSatus = saohuobangXmlNote.getFlowordertype();
				Status = getSaohuobangStatus(saohuobangXmlNote, orderSatus);

			}
			sb.toString();
			logger.info("[扫货帮]的订单查询xml={}", sb);
			callback += "<order>" + "<txLogisticID>" + sList.get(0).getTranscwb() + "</txLogisticID>" + "<mailNo>" + sList.get(0).getCwb() + "</mailNo>" + "<mailType>" + Status + "</mailType>"
					+ "<orderStatus></orderStatus>" + "<steps>" + sb + "</steps></order>";
		}

		return callback;

	}

	private String getSaohuobangStatus(SaohuobangXMLNote list, String orderSatus) {
		String Status;
		if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			Status = "ACCEPT";
		} else if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.RuKu.getValue()) {
			Status = "GOT";
		} else if (Long.valueOf(orderSatus) == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (list.getDelivery_state() == DeliveryStateEnum.FenZhanZhiLiu.getValue() || list.getDelivery_state() == DeliveryStateEnum.QuanBuTuiHuo.getValue()
						|| list.getDelivery_state() == DeliveryStateEnum.HuoWuDiuShi.getValue() || list.getDelivery_state() == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			Status = "FAILED";
		} else {
			Status = "SIGNED";
		}
		return Status;
	}

	public String getXmlForCallback(String callback, Saohuobang saohuobang) {
		String xml = "<BatchQueryResponse>" + "<logisticProviderID>" + saohuobang.getProviderID() + "</logisticProviderID>" + "<orders>" + callback + "</orders>" + "</BatchQueryResponse>";
		return xml;

	}

	public Saohuobang getSaohuobangSettingMethod(int key) {
		Saohuobang saohuobang = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			saohuobang = (Saohuobang) JSONObject.toBean(jsonObj, Saohuobang.class);
		} else {
			saohuobang = new Saohuobang();
		}

		return saohuobang;

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
	public String getPostMethodResult(String url, String token, String post_xml) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("logistics_interface", post_xml), new NameValuePair("data_digest", token), }; // 原始信息
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	// 拼接xml
	private String jiontXmlForRetrunSaohuobang(int flag, String cwb, String s, String state, String reason) {
		String retrunxml = "";

		if (flag == 1) {
			retrunxml = "<Response>" + "<txLogisticID>" + cwb + "</txLogisticID>" + "<logisticProviderID>" + s + "</logisticProviderID>" + "<success>true</success>" + "</Response>";
		}
		if (flag == 0) {
			retrunxml = "<Response>" + "<txLogisticID>" + cwb + "</txLogisticID>" + "<logisticProviderID>" + s + "</logisticProviderID>" + "<success>false</success>" + "<status>" + state
					+ "</status>" + "<reason>" + reason + "</reason>" + "</Response>";

		}
		return retrunxml;
	}
}
