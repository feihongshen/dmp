package cn.explink.b2c.letv;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.letv.bean.LetvContent;
import cn.explink.b2c.letv.bean.LetvResponse;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class LetvService {

	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;

	private Logger logger = LoggerFactory.getLogger(LetvService.class);

	public Letv getLetv(int key) {
		Letv letv = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			letv = (Letv) JSONObject.toBean(jsonObj, Letv.class);
		} else {
			letv = new Letv();
		}
		return letv;
	}

	/**
	 * 获取乐视网配置信息的接口
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
	public LetvFlowEnum getFlowStateByLetv(long flowordertype, long deliverystate) {
		for (LetvFlowEnum e : LetvFlowEnum.values()) {
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue() && e.getFlowtype() == flowordertype) {
				return e;
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {

			if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				return LetvFlowEnum.QianShou;
			}
			if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()
					|| deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				return LetvFlowEnum.YiChangQianShou;
			}
			if (deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return LetvFlowEnum.ShangMenTuiChengGong;
			}

		}

		return null;
	}

	/**
	 * 反馈[乐视网]订单信息
	 */
	public void feedback_status() {

		Letv letv = getLetv(B2cEnum.Letv.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.Letv.getKey())) {
			logger.info("未开0乐视网0的对接!");
			return;
		}

		sendCwbStatus_To_letv(letv);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_letv(Letv letv) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(letv.getCustomerid(), letv.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0乐视网0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0乐视网0的数据");
					return;
				}
				DealWithBuildXMLAndSending(letv, datalist);

			}

		} catch (Exception e) {
			logger.error("发送0乐视网0状态反馈遇到不可预知的异常", e);
		}

	}

	// String
	// requestXML="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
	// +"<request>"
	// +"<biz_no>"+letvcontent.getBiz_no()+"</biz_no>"
	// +"<order_no>"+letvcontent.getOrder_no()+"</order_no>"
	// +"<waybill_no>"+letvcontent.getWaybill_no()+"</waybill_no>"
	// +"<service_no>"+letvcontent.getService_no()+"</service_no>"
	// +"<content>"+letvcontent.getContent()+"</content>"
	// +"<node_no>"+letvcontent.getNode_no()+"</node_no>"
	// +"<operator>"+letvcontent.getOperator()+"</operator>"
	// +"<operator_time>"+letvcontent.getOperator_time()+"</operator_time>"
	// +"<consignee_sign_flag>"+letvcontent.getConsignee_sign_flag()+"</consignee_sign_flag>"
	// +"<exception_description>"+letvcontent.getException_description()+"</exception_description>"
	// +"<exception_no>"+letvcontent.getException_no()+"</exception_no>"
	// +"<signee>"+letvcontent.getSignee()+"</signee>"
	// +"</request>";
	// requestXML=requestXML.replace("null", "");
	// parmsMap.put("content", requestXML);

	public LetvXMLNote getYeMaiJiuXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, LetvXMLNote.class);
	}

	private void DealWithBuildXMLAndSending(Letv letv, List<B2CData> datalist) throws Exception {

		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			LetvXMLNote note = getYeMaiJiuXMLNoteMethod(jsoncontent);

			Map<String, String> parmsMap = buildRequestParams(note); // 构建请求参数

			LetvContent letvcontent = note.getContent();

			String content = LetvMarchal.Marchal(letvcontent); // 请求xml报文体
			parmsMap.put("content", content);

			String responseXML = RestHttpServiceHanlder.sendHttptoServer(parmsMap, letv.getSend_url());

			logger.info("状态反馈0乐视网0[返回信息]-XML={}", responseXML);

			LetvResponse letvResponse = LetvUnmarchal.Unmarchal(responseXML);

			int state = letvResponse.getResponse_code().equals("T") ? 1 : 2;
			String remark = letvResponse.getMessage();

			b2cDataDAO.updateFlagAndRemarkByCwb(data.getB2cid(), state, remark);

		}

	}

	private Map<String, String> buildRequestParams(LetvXMLNote note) {
		Map<String, String> parmsMap = new HashMap<String, String>();
		parmsMap.put("3pl_no", note.get_3pl_no());
		parmsMap.put("service_no", note.getService_no());
		parmsMap.put("waybill_no", note.getWaybill_no());
		parmsMap.put("order_no", note.getOrder_no());
		parmsMap.put("sign_type", note.getSign_type());
		parmsMap.put("sign", note.getSign());
		return parmsMap;
	}

}
