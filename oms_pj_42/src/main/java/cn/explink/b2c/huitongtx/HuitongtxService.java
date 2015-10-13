package cn.explink.b2c.huitongtx;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.huitongtx.response.datadetail;
import cn.explink.b2c.huitongtx.response.response;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class HuitongtxService {
	private Logger logger = LoggerFactory.getLogger(HuitongtxService.class);
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	public static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	// 获取httx配置信息
	public Huitongtx getHuitongtxSettingMethod(int key) {
		Huitongtx httx = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			httx = (Huitongtx) JSONObject.toBean(jsonObj, Huitongtx.class);
		} else {
			httx = new Huitongtx();
		}
		return httx;
	}

	// 获取tmall XML Note
	public HuitongXMLNote getXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (HuitongXMLNote) JSONObject.toBean(jsonObj, HuitongXMLNote.class);
		} catch (Exception e) {
			logger.error("获取TmallXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	private int getTmallStateByCode(String tmllcode) {
		for (HuitongtxFlowEnum tmall : HuitongtxFlowEnum.values()) {
			if (tmall.getTmall_state().equals(tmllcode)) {
				return tmall.getFlowordertype();
			}
		}
		return 0;
	}

	// 状态反馈
	public long feedback_status(int httx_key) {
		long calcCount = 0;

		Huitongtx httx = getHuitongtxSettingMethod(httx_key); // 获取配置信息
		if (!b2ctools.isB2cOpen(httx_key)) {
			logger.info("未开汇通天下的对接!key={}", httx_key);
			return -1;
		}
		logger.info("=========汇通天下状态反馈任务调度开启==========");

		calcCount += sendCwbStatus_To_httx(httx, "TMS_ACCEPT", httx.getCustomerids()); // 入库信息反馈
		calcCount += sendCwbStatus_To_httx(httx, "TMS_STATION_IN", httx.getCustomerids()); // 分站进
		// sendCwbStatus_To_httx(httx,"TMS_STATION_OUT",httx.getCustomerids());
		// //分站出-（分站出库派送中）

		calcCount += sendCwbStatus_To_httx(httx, "TMS_DELIVERING", httx.getCustomerids()); // 派送中
		calcCount += sendCwbStatus_To_httx(httx, "TMS_ERROR", httx.getCustomerids()); // 滞留异常
		calcCount += sendCwbStatus_To_httx(httx, "TMS_SIGN", httx.getCustomerids()); // 配送成功
																						// 包括支付成功TMS_PAY
		calcCount += sendCwbStatus_To_httx(httx, "TMS_FAILED", httx.getCustomerids()); // 拒收

		logger.info("=========汇通天下状态反馈任务调度结束==========");
		return calcCount;
	}

	private long sendCwbStatus_To_httx(Huitongtx httx, String TmallStateCode, String customerid) {
		long calcCount = 0;

		try {
			int flowordertype = getTmallStateByCode(TmallStateCode);

			HuitongtxConfig tmallConfig = new HuitongtxConfig();
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(flowordertype, customerid, httx.getSelectMaxCount() == 0 ? 100 : httx.getSelectMaxCount());
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[汇通天下]的数据,状态{}", flowordertype + "," + TmallStateCode);
					return 0;
				}
				// 循环推送数据，每次一条
				String b2cids = "";
				for (B2CData b2cData : datalist) {
					try {
						String jsoncontent = b2cData.getJsoncontent();
						HuitongXMLNote note = getXMLNoteMethod(jsoncontent);

						// 当前为派送中，判断前一个{览收}是否推送成功
						if (note.getStatus().equals(HuitongtxFlowEnum.TMS_DELIVERING.getTmall_state())) {
							if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue()) == 0) { // 入库没推，需补充
								AppendXMLSendAndFeedBack(httx, flowordertype, tmallConfig, b2cData, note, HuitongtxFlowEnum.TMS_ACCEPT.getTmall_state());
							}
						}
						// 判断如果到了签收节点，并且COD类型,先推送支付成功
						if (note.getStatus().equals(HuitongtxFlowEnum.TMS_SIGN.getTmall_state()) && note.getReceivablefee().compareTo(BigDecimal.ZERO) > 0) {
							AppendXMLSendAndFeedBack(httx, flowordertype, tmallConfig, b2cData, note, "TMS_PAY");
						}
						// 当前为投递结果，判断前一个{派送中}是否推送成功
						if (note.getStatus().equals(HuitongtxFlowEnum.TMS_ERROR.getTmall_state()) || note.getStatus().equals(HuitongtxFlowEnum.TMS_SIGN.getTmall_state())
								|| note.getStatus().equals(HuitongtxFlowEnum.TMS_FAILED.getTmall_state())) {
							if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "") == 0) { // 投递中没推，需补充
								AppendXMLSendAndFeedBack(httx, flowordertype, tmallConfig, b2cData, note, HuitongtxFlowEnum.TMS_DELIVERING.getTmall_state());
							}
						}

						if (note.getStatus().equals(HuitongtxFlowEnum.TMS_DELIVERING.getTmall_state())) { // 如果是派送中,则
																											// 新增
																											// 分站出
							AppendXMLSendAndFeedBack(httx, flowordertype, tmallConfig, b2cData, note, "TMS_STATION_OUT");
						}

						// 正常的推送
						AppendXMLSendAndFeedBack(httx, flowordertype, tmallConfig, b2cData, note, null);

						// b2cids += b2cData.getB2cid()+",";

					} catch (Exception e) {
						String expt = "处理[汇通天下]推送业务逻辑发生不可预估的异常,订单号:" + b2cData.getCwb() + ",当前状态：[" + flowordertype + "," + TmallStateCode + "]," + e;
						logger.error(expt, e);

					}

				}
				calcCount += datalist.size();
				// b2cids =b2cids.length()>0?b2cids.substring(0,
				// b2cids.length()-1):"0";
			}
		} catch (Exception e) {

		}
		return calcCount;
	}

	/**
	 * 
	 * @param httx
	 * @param flowordertype
	 * @param tmallConfig
	 * @param b2cData
	 * @param note
	 * @param TMS_Status
	 * @param isfactExit
	 *            判断是否真实存在，true表示存在 ，false表示不存在
	 * @throws Exception
	 */
	private void AppendXMLSendAndFeedBack(Huitongtx httx, int flowordertype, HuitongtxConfig tmallConfig, B2CData b2cData, HuitongXMLNote note, String TMS_Status) throws Exception {

		String TMS_status = TMS_Status != null && !"".equals(TMS_Status) ? TMS_Status : note.getStatus();
		note.setStatus(TMS_status);

		String requestXML = JacksonMapper.getInstance().writeValueAsString(note);

		SendHttpAndFeedBack(httx, TMS_status, flowordertype, tmallConfig, b2cData, requestXML, note);
		if (TMS_Status != null && TMS_Status.equals("TMS_STATION_OUT")) {
			note.setStatus("TMS_DELIVERING");
		}
	}

	// 发送XML和返回XMl的处理
	private void SendHttpAndFeedBack(Huitongtx httx, String Status_code, int flowordertype, HuitongtxConfig tmallConfig, B2CData b2cData, String jsoncontent, HuitongXMLNote note) throws Exception {

		String responseJson = "";

		responseJson = HuitongtxConfig.posthttpdata_Httx(httx.getPost_url(), httx.getApp_key(), jsoncontent, Status_code, httx.getPrivate_key());

		logger.info("订单号：{},汇通天下返回:状态[" + flowordertype + "," + Status_code + "],json：{}", b2cData.getCwb(), responseJson);

		response resp = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<response>() {
		});
		if (resp.getCode() != 0) {
			throw new RuntimeException("汇通返回信息异常:" + responseJson);

		}
		List<datadetail> datas = resp.getData();
		for (datadetail data : datas) {
			int status = data.getStatus() == 1 ? 1 : 2;
			String message = data.getMessage();
			b2cDataDAO.updateB2cIdSQLResponseStatus(b2cData.getB2cid(), status, message);
		}

	}

	public String getTmallFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (HuitongtxFlowEnum TEnum : HuitongtxFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getTmall_state();
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return HuitongtxFlowEnum.TMS_SIGN.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return HuitongtxFlowEnum.TMS_ERROR.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return HuitongtxFlowEnum.TMS_ERROR.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			return HuitongtxFlowEnum.TMS_FAILED.getTmall_state();
		}
		return null;

	}

}
