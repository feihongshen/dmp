package cn.explink.b2c.lechong;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.lechong.xml.UpdateInfo;
import cn.explink.b2c.lechong.xml.UpdateInfoResponse;
import cn.explink.b2c.lechong.xml.UpdateInfoResponseUnMarchal;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class LechongService {
	private Logger logger = LoggerFactory.getLogger(LechongService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	// 获取配置信息
	public Lechong getLechong(int key) {
		Lechong le = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			le = (Lechong) JSONObject.toBean(jsonObj, Lechong.class);
		} else {
			le = new Lechong();
		}
		return le;
	}

	public String getFlowEnum(long flowordertype, long deliverystate) {
		for (LechongTrackEnum em : LechongTrackEnum.values()) {
			if (em.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (flowordertype == em.getFlowordertype()) {
					return em.getState();
				}
			}
		}

		// 拒收
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			return LechongTrackEnum.JuShou.getState();
		}
		// 签收
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return LechongTrackEnum.QianShou.getState();
		}
		return null;
	}

	/**
	 * 反馈[乐宠]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.LeChong.getKey())) {
			logger.info("未开乐宠的对接!");
			return;
		}
		Lechong wx = getLechong(B2cEnum.LeChong.getKey());
		sendCwbStatus_To_wx(wx);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_wx(Lechong le) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(le.getCustomerids(), le.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询乐宠状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送乐宠的数据");
					return;
				}
				DealWithBuildXMLAndSending(le, datalist);

			}

		} catch (Exception e) {
			logger.error("发送乐宠状态反馈遇到不可预知的异常", e);
		}

	}

	public LechongXmlNote getXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, LechongXmlNote.class);
	}

	private void DealWithBuildXMLAndSending(Lechong wx, List<B2CData> datalist) throws Exception {
		String cwb = null;
		for (B2CData data : datalist) {
			cwb = data.getCwb();
			try {
				String jsonconten = data.getJsoncontent();
				UpdateInfo up = JacksonMapper.getInstance().readValue(jsonconten, UpdateInfo.class);
				String Remark = up.getRemark() == null ? "" : up.getRemark();
				String STATUS_REASON = up.getSTATUS_REASON() == null ? "" : up.getSTATUS_REASON();
				String LogisticProvider = up.getLogisticProvider() == null ? "" : up.getLogisticProvider();
				String Xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<UpdateInfo>" + "<LogisticProvider>" + LogisticProvider + "</LogisticProvider>" + "<LogisticProviderID>"
						+ up.getLogisticProviderID() + "</LogisticProviderID>" + "<DoID>" + up.getDoID() + "</DoID>" + "<MailNO>" + up.getMailNO() + "</MailNO>" + "<Status>" + up.getStatus()
						+ "</Status>" + "<AcceptTime>" + up.getAcceptTime() + "</AcceptTime>" + "<ReceiveEmployee>" + up.getReceiveEmployee() + "</ReceiveEmployee>" + "<City>" + up.getCity()
						+ "</City>" + "<Remark>" + Remark + "</Remark>" + "<STATUS_REASON>" + STATUS_REASON + "</STATUS_REASON>" + "<MD5Key>" + up.getMD5Key() + "</MD5Key>" + "</UpdateInfo>";
				logger.info("乐宠状态回传请求XML{}", Xml);
				String responseXML = RestHttpServiceHanlder.sendHttptoServer(Xml, wx.getFeedbackUrl() + "?cwb=" + cwb);
				// String responseXML = WebServiceHandler.invokeWs(wsdlUrl,
				// opName, opArgs);
				logger.info("乐宠状态回传响应XML{}", responseXML);

				UpdateInfoResponse updateInfoResponse = UpdateInfoResponseUnMarchal.XmltoPOJO(responseXML);
				int send_b2c_flag = updateInfoResponse.getFlag().equals("0") ? 1 : 2;
				b2cDataDAO.updateB2cIdSQLResponseStatus(Long.valueOf(data.getB2cid()), send_b2c_flag, updateInfoResponse.getDesc());
			} catch (Exception e) {
				logger.error("乐宠状态回传异常" + cwb, e);
			}

		}
	}

}
