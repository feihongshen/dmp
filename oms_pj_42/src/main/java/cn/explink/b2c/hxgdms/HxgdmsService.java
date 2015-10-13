package cn.explink.b2c.hxgdms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RestHttpServiceHanlder;
import cn.explink.util.MD5.MD5Util;

@Service
public class HxgdmsService {
	private Logger logger = LoggerFactory.getLogger(HxgdmsService.class);
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	public String getHxgdmsFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (HxgdmsFlowEnum TEnum : HxgdmsFlowEnum.values()) {
				if (flowordertype == TEnum.getState()) {
					return TEnum.getDms_code();
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return HxgdmsFlowEnum.QianShou.getDms_code();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()
						|| deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())

		) {
			return HxgdmsFlowEnum.YiChang.getDms_code();
		}

		return null;

	}

	public Hxgdms getDMSSettingMethod(int key) {
		Hxgdms smile = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			smile = (Hxgdms) JSONObject.toBean(jsonObj, Hxgdms.class);
		} else {
			smile = new Hxgdms();
		}
		return smile;
	}

	/**
	 * 获取好享购DMS配置信息的接口
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
	 * 反馈[好享购DMS]订单信息
	 */
	public void feedback_status() {

		Hxgdms dms = getDMSSettingMethod(B2cEnum.Hxgdms.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.Hxgdms.getKey())) {
			logger.info("未开[好享购DMS]的对接!");
			return;
		}

		sendCwbStatus_To_dms(dms);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param dms
	 */
	private void sendCwbStatus_To_dms(Hxgdms dms) {
		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(dms.getCustomerids(), dms.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("好享购DMS状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[好享购DMS]的数据");
					return;
				}
				DealWithBuildXMLAndSending(dms, datalist);

			}

		} catch (Exception e) {
			logger.error("推送好享购DMS系统发生未知异常", e);
		}

	}

	private void DealWithBuildXMLAndSending(Hxgdms dms, List<B2CData> datalist) throws Exception {
		String b2cids = "";
		for (B2CData data : datalist) {

			String jsoncontent = data.getJsoncontent();
			String signed = MD5Util.md5(jsoncontent + dms.getSecretKey());

			Map<String, String> params = new HashMap<String, String>();
			params.put("Request", jsoncontent);
			params.put("Signed", signed);
			params.put("Action", "RequestSendStateToDMS");
			params.put("Dcode", dms.getDcode());

			String jsonresponse = RestHttpServiceHanlder.sendHttptoServer(params, dms.getFeedbackUrl());

			logger.info("当前好享购DMS返回data={},jsonresponse={}", data.getCwb(), jsonresponse);

			DmsResponse dmsResponse = JacksonMapper.getInstance().readValue(jsonresponse, DmsResponse.class);

			int send_b2c_flag = dmsResponse.getRtn_Code().equals("0") ? 1 : 2;
			String remark = dmsResponse.getRtn_Msg();

			b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), send_b2c_flag, remark);
			b2cids += data.getB2cid() + ",";

		}
		b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";
		// 发送给dmp
		flowFromJMSB2cService.sendTodmp(b2cids);
	}

	// 获取tmall XML Note
	public HxgdmsJsonNote getSmileXMLNoteMethod(String jsoncontent) {
		try {
			return new ObjectMapper().readValue(jsoncontent, HxgdmsJsonNote.class);
		} catch (Exception e) {
			logger.error("获取SmileXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	/**
	 * 跟踪日志反馈
	 * 
	 * @param orderFlow
	 * @param cwbOrder
	 */
	public void sendTrackInfoByHxgdms(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, String dmsReceiveStatus, Hxgdms dms) {

		try {
			if (!b2ctools.isB2cOpen(B2cEnum.Hxgdms.getKey())) {
				logger.info("未开[好享购DMS]的对接!");
				return;
			}

			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				logger.info("好享购订单流转信息取消领货，反馈，异常环节推送,cwb={}", orderFlow.getCwb());
				return;
			}

			User operatorUser = getdmpDAO.getUserById(orderFlow.getUserid());
			// //执行跟踪日志反馈的方法

			HxgdmsJsonTrack hxgdmsJsonTrack = new HxgdmsJsonTrack();
			hxgdmsJsonTrack.setWorkCode(cwbOrder.getCwb());
			hxgdmsJsonTrack.setFlowType(dmsReceiveStatus);
			hxgdmsJsonTrack.setOperationDesc(orderFlowDetail.getDetail(orderFlow));
			hxgdmsJsonTrack.setOperationName(operatorUser.getRealname());
			hxgdmsJsonTrack.setOperationTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			hxgdmsJsonTrack.setDelveryCode(dms.getDcode());

			String jsoncontent = null;
			try {
				jsoncontent = JacksonMapper.getInstance().writeValueAsString(hxgdmsJsonTrack);
			} catch (Exception e) {
				logger.error("jackson转化异常,cwb=" + cwbOrder.getCwb(), e);
			}
			String signed = MD5Util.md5(jsoncontent + dms.getSecretKey());

			Map<String, String> params = new HashMap<String, String>();
			params.put("Request", jsoncontent);
			params.put("Signed", signed);
			params.put("Action", "RequestSendTrackToDMS");
			params.put("Dcode", dms.getDcode());

			String jsonresponse = RestHttpServiceHanlder.sendHttptoServer(params, dms.getFeedbackUrl());
			logger.info("好享购DMS跟踪信息-返回,cwb={},jsonresponse={}", orderFlow.getCwb(), jsonresponse);
		} catch (Exception e) {
			logger.error("推送好享购DMS跟踪日志异常cwb=" + orderFlow.getCwb(), e);
		}

	}

	/**
	 * 处理推送失败问题
	 * 
	 * @param cwbOrder
	 * @param delivery_state
	 * @param dms
	 */
	public void sendErrorByHxgdms(DmpCwbOrder cwbOrder, long delivery_state, Hxgdms dms) {
		try {
			HxgdmsJsonError hxgdmsJsonError = new HxgdmsJsonError();
			hxgdmsJsonError.setWorkCode(cwbOrder.getCwb());
			hxgdmsJsonError.setDelveryCode(dms.getDcode());
			String errorCode = "";
			String errorMsg = "";

			if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				ExptReason exptReasonLose = b2ctools.getDiushiReasonByB2c(cwbOrder.getLosereasonid(), cwbOrder.getCustomerid() + "");
				errorCode = exptReasonLose.getExpt_code();
				errorMsg = exptReasonLose.getExpt_msg();
				if (errorCode == null || errorCode.isEmpty()) {
					errorCode = "1";
					errorMsg = "派送失败_丢失";
				}

			} else if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
				ExptReason exptReason = b2ctools.getExptReasonByB2c(0, cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", delivery_state);
				errorCode = exptReason.getExpt_code();
				errorMsg = exptReason.getExpt_msg();
				if (errorCode == null || errorCode.isEmpty()) {
					errorCode = "12";
					errorMsg = "拒收报备_其它";
				}
			} else if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), 0, cwbOrder.getCustomerid() + "", delivery_state);
				errorCode = exptReason.getExpt_code();
				errorMsg = exptReason.getExpt_msg();
				if (errorCode == null || errorCode.isEmpty()) {
					errorCode = "5";
					errorMsg = "派送未果_其它";
				}
			}
			hxgdmsJsonError.setErrorCode(errorCode);
			hxgdmsJsonError.setErrorNote(errorMsg);

			String jsoncontent = null;
			try {
				jsoncontent = JacksonMapper.getInstance().writeValueAsString(hxgdmsJsonError);
			} catch (Exception e) {
				logger.error("jackson转化异常,cwb=" + cwbOrder.getCwb(), e);
			}
			String signed = MD5Util.md5(jsoncontent + dms.getSecretKey());

			Map<String, String> params = new HashMap<String, String>();
			params.put("Request", jsoncontent);
			params.put("Signed", signed);
			params.put("Action", "RequestSendErrorToDMS");
			params.put("Dcode", dms.getDcode());
			String jsonresponse = RestHttpServiceHanlder.sendHttptoServer(params, dms.getFeedbackUrl());
			logger.info("好享购DMS异常信息-返回,jsonresponse={}", jsonresponse);
		} catch (Exception e) {
			logger.error("推送好享购DMS异常反馈异常cwb=" + cwbOrder.getCwb(), e);
		}

	}

}
