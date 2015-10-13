package cn.explink.b2c.chinamobile;

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

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.WebServiceHandler;

@Service
public class ChinamobileService {

	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;

	private Logger logger = LoggerFactory.getLogger(ChinamobileService.class);

	public Chinamobile getChinamobile(int key) {
		Chinamobile chinamobile = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			chinamobile = (Chinamobile) JSONObject.toBean(jsonObj, Chinamobile.class);
		} else {
			chinamobile = new Chinamobile();
		}
		return chinamobile;
	}

	/**
	 * 获取中国移动配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取yemaijiu的反馈状态
	 * 
	 * @return
	 */
	public ChinamobileFlowEnum getFlowStateByChinaMobile(long flowordertype, long deliverystate) {
		for (ChinamobileFlowEnum e : ChinamobileFlowEnum.values()) {
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue() && e.getFlowtype() == flowordertype) {
				return e;
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {

			if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return ChinamobileFlowEnum.QianShou;
			}
			if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				return ChinamobileFlowEnum.YiChangQianShou;
			}
			if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				return ChinamobileFlowEnum.FenZhanZhiLiu;
			}
		}

		return null;
	}

	/**
	 * 反馈[中国移动]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.ChinaMobile.getKey())) {
			logger.info("未开0中国移动0的对接!");
			return;
		}
		Chinamobile chinamobile = getChinamobile(B2cEnum.ChinaMobile.getKey());

		sendCwbStatus_To_chinamobile(chinamobile);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_chinamobile(Chinamobile chinamobile) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(chinamobile.getCustomerid(), chinamobile.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0中国移动0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0中国移动0的数据");
					return;
				}
				DealWithBuildXMLAndSending(chinamobile, datalist);

			}

		} catch (Exception e) {
			logger.error("发送0中国移动0状态反馈遇到不可预知的异常", e);
		}

	}

	public ChinamobileXMLNote getChinaMobileXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, ChinamobileXMLNote.class);
	}

	private void DealWithBuildXMLAndSending(Chinamobile cm, List<B2CData> datalist) throws Exception {

		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			ChinamobileXMLNote note = getChinaMobileXMLNoteMethod(jsoncontent);

			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<InterEB>" + "<PartnerId>" + cm.getExpressid() + "</PartnerId>" + "<Delievers>" + "<DelieverId>" + note.getDelieverId()
					+ "</DelieverId>" + "<Oprnum>" + note.getOprnum() + "</Oprnum>" + "<Message>" + "<SendTime>" + note.getSendTime() + "</SendTime>" + "<Status>" + note.getStatus() + "</Status>"
					+ "<ShowMsg>" + note.getShowMsg() + "</ShowMsg>" + "<Deliveryfailure>" + note.getDeliveryfailure() + "</Deliveryfailure>" + "</Message>" + "</Delievers>" + "</InterEB>";

			logger.info("状态反馈-中国移动0请求信息0XML={}", xml);
			String parms[] = new String[] { xml, "EBPSZT" };

			String responseXML = (String) WebServiceHandler.invokeWs(cm.getFeedback_url(), "service", parms);

			logger.info("状态反馈-中国移动0返回信息0-XML={},cwb={}", responseXML, data.getCwb());

			ChinaMobileResponse res = ChinaUnmarchal.Unmarchal(responseXML);

			int state = "0".equals(res.getReturnCode()) ? 1 : 2;
			String remark = res.getReturnMsg();

			b2cDataDAO.updateFlagAndRemarkByCwb(data.getB2cid(), state, remark);

		}

	}

}
