package cn.explink.b2c.tmall;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.Mail;
import cn.explink.b2c.yihaodian.Yihaodian;
import cn.explink.b2c.yihaodian.YihaodianExpEmum;
import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryResultDto;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.domain.B2CData;
import cn.explink.domain.Customer;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class TmallService {
	private Logger logger = LoggerFactory.getLogger(TmallService.class);
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;
	@Autowired
	TmallErrorService tmallErrorService;

	// 获取tmall配置信息
	public Tmall getTmallSettingMethod(int key) {
		Tmall tmall = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			tmall = (Tmall) JSONObject.toBean(jsonObj, Tmall.class);
		} else {
			tmall = new Tmall();
		}
		return tmall;
	}

	// 获取tmall XML Note
	public TmallXMLNote getTmallXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (TmallXMLNote) JSONObject.toBean(jsonObj, TmallXMLNote.class);
		} catch (Exception e) {
			logger.error("获取TmallXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	private int getTmallStateByCode(String tmllcode) {
		for (TmallFlowEnum tmall : TmallFlowEnum.values()) {
			if (tmall.getTmall_state().equals(tmllcode)) {
				return tmall.getFlowordertype();
			}
		}
		return 0;
	}

	// 状态反馈
	public long feedback_status(int tmall_key) {
		long calcCount = 0;
		Tmall tmall = getTmallSettingMethod(tmall_key); // 获取配置信息
		if (!b2ctools.isB2cOpen(tmall_key)) {
			logger.info("未开tmall的对接!tmall_key={}", tmall_key);
			return -1;
		}
		logger.info("=========tmall状态反馈任务调度开启==========");
		if (tmall.getIsCallBackAccept() == 1) { // 回传
			calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_ACCEPT", tmall.getCustomerids()); // 入库信息反馈
		}
		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_STATION_OUT", tmall.getCustomerids()); // 分站出-（包括库房出库和分站出库派送中）
		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_STATION_IN", tmall.getCustomerids()); // 分站进

		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_DELIVERING", tmall.getCustomerids()); // 派送中
		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_ERROR", tmall.getCustomerids()); // 滞留异常
		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_SIGN", tmall.getCustomerids()); // 配送成功
																						// 包括支付成功TMS_PAY
		calcCount += sendCwbStatus_To_Tmall(tmall, "TMS_FAILED", tmall.getCustomerids()); // 拒收

		// 查询指定失败的数据重发
		feedbackFailedAgain(tmall, "TMS_ACCEPT");
		feedbackFailedAgain(tmall, "TMS_DELIVERING");
		feedbackFailedAgain(tmall, "TMS_SIGN");

		logger.info("=========tmall状态反馈任务调度结束==========");
		return calcCount;
	}

	public long sendCwbStatus_To_Tmall(Tmall tmall, String TmallStateCode, String customerid) {
		long calcCount = 0;

		try {
			int flowordertype = getTmallStateByCode(TmallStateCode);
			if (tmall.getAcceptflag() == 4) { // 以入库作为揽收标准 //acceptflag=4
												// //默认是导入作为揽收标准
				if (flowordertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
					flowordertype = FlowOrderTypeEnum.RuKu.getValue();
				}
			}

			TmallConfig tmallConfig = new TmallConfig();

			while (true) {

				List<B2CData> tmallDataList = b2cDataDAO.getDataListByFlowStatus(flowordertype, customerid, tmall.getSelectMaxCount());
				if (tmallDataList == null || tmallDataList.size() == 0) {
					logger.info("当前没有要推送[tmall]的数据,状态{}", flowordertype + "," + TmallStateCode);
					return 0;
				}
				// 循环推送数据，每次一条
				String b2cids = "";
				for (B2CData b2cData : tmallDataList) {
					try {

						String jsoncontent = b2cData.getJsoncontent();
						TmallXMLNote note = getTmallXMLNoteMethod(jsoncontent);
						if (tmall.getIsCallBackError() == 1 && note.getActionPushCode().equals("new")) { // 是否回传TMS_ERROR异常信息
																											// 0不回传，1
																											// 回传。
							// 执行新的接口异常推送
							tmallErrorService.excuteTmallExceptionMethod(tmall, b2cData, note);
							continue;
						}

						excuteSendTmallServer(tmall, flowordertype, tmallConfig, b2cData, note);

						b2cids += b2cData.getB2cid() + ",";

					} catch (Exception e) {
						logger.error("处理[tmall]推送业务逻辑发生不可预估的异常" + b2cData.getCwb(), e);
					}
				}
				b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";

				flowFromJMSB2cService.sendTodmp(b2cids); // 发送给dmp
				calcCount += tmallDataList.size();
			}

		} catch (Exception e) {

		}

		return calcCount;

	}

	private void excuteSendTmallServer(Tmall tmall, int flowordertype, TmallConfig tmallConfig, B2CData b2cData, TmallXMLNote note) throws Exception {

		// 当前为派送中，判断前一个{览收}是否推送成功
		if (note.getStatus().equals(TmallFlowEnum.TMS_DELIVERING.getTmall_state())) {
			if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue()) == 0) { // 入库没推，需补充
				AppendXMLSendAndFeedBack(tmall, flowordertype, tmallConfig, b2cData, note, TmallFlowEnum.TMS_ACCEPT.getTmall_state(), false);
			}
		}
		// 判断如果到了签收节点，并且COD类型,先推送支付成功
		if (note.getStatus().equals(TmallFlowEnum.TMS_SIGN.getTmall_state()) && note.getReceivablefee() > 0) {
			AppendXMLSendAndFeedBack(tmall, flowordertype, tmallConfig, b2cData, note, "TMS_PAY", false);
		}
		// 当前为投递结果，判断前一个{派送中}是否推送成功
		if (note.getStatus().equals(TmallFlowEnum.TMS_ERROR.getTmall_state()) || note.getStatus().equals(TmallFlowEnum.TMS_SIGN.getTmall_state())
				|| note.getStatus().equals(TmallFlowEnum.TMS_FAILED.getTmall_state())) {
			if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "") == 0) { // 投递中没推，需补充
				AppendXMLSendAndFeedBack(tmall, flowordertype, tmallConfig, b2cData, note, TmallFlowEnum.TMS_DELIVERING.getTmall_state(), false);
			}
		}
		// 正常的推送
		AppendXMLSendAndFeedBack(tmall, flowordertype, tmallConfig, b2cData, note, null, true);

		/**
		 * 新增库房进 TMS_STATION_IN ,库房出 TMS_STATION_OUT
		 */
		// if(note.getStatus().equals(TmallFlowEnum.TMS_ACCEPT.getTmall_state())){
		// //如果是揽收,则 新增 分站进
		// AppendXMLSendAndFeedBack(tmall, flowordertype,tmallConfig, b2cData,
		// note,TmallFlowEnum.TMS_STATION_IN.getTmall_state(),false);
		// }
		if (note.getStatus().equals(TmallFlowEnum.TMS_DELIVERING.getTmall_state())) { // 如果是派送中,则
																						// 新增
																						// 分站出
			AppendXMLSendAndFeedBack(tmall, flowordertype, tmallConfig, b2cData, note, TmallFlowEnum.TMS_STATION_OUT.getTmall_state(), false);
		}
	}

	public int sendByCwbs(String cwbs, long send_b2c_flag, String b2cEnumkey) {
		Tmall tmall = getTmallSettingMethod(Integer.parseInt(b2cEnumkey)); // 获取配置信息
		if (!b2ctools.isB2cOpen(Integer.parseInt(b2cEnumkey))) {
			logger.info("未开tmall的对接!tmall_key={}", b2cEnumkey);
			return 0;
		}
		TmallConfig tmallConfig = new TmallConfig();
		List<B2CData> tmallDataList = b2cDataDAO.getDataListByCwbs(cwbs, send_b2c_flag);
		if (tmallDataList == null || tmallDataList.size() == 0) {
			logger.info("当前没有要推送[tmall]的数据,状态");
			return 0;
		}
		// 循环推送数据，每次一条
		String b2cids = "";
		for (B2CData b2cData : tmallDataList) {
			try {
				String jsoncontent = b2cData.getJsoncontent();
				TmallXMLNote note = getTmallXMLNoteMethod(jsoncontent);

				// 当前为派送中，判断前一个{览收}是否推送成功
				if (note.getStatus().equals(TmallFlowEnum.TMS_DELIVERING.getTmall_state())) {
					if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue()) == 0) { // 入库没推，需补充
						AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, TmallFlowEnum.TMS_ACCEPT.getTmall_state(), false);
					}
				}
				// 判断如果到了签收节点，并且COD类型,先推送支付成功
				if (note.getStatus().equals(TmallFlowEnum.TMS_SIGN.getTmall_state()) && note.getReceivablefee() > 0) {
					AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, "TMS_PAY", false);
				}
				// 当前为投递结果，判断前一个{派送中}是否推送成功
				if (note.getStatus().equals(TmallFlowEnum.TMS_ERROR.getTmall_state()) || note.getStatus().equals(TmallFlowEnum.TMS_SIGN.getTmall_state())
						|| note.getStatus().equals(TmallFlowEnum.TMS_FAILED.getTmall_state())) {
					if (b2cDataDAO.checkPreStatusSendFlag(b2cData.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "") == 0) { // 投递中没推，需补充
						AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, TmallFlowEnum.TMS_DELIVERING.getTmall_state(), false);
					}
				}
				// 正常的推送
				AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, null, true);

				/**
				 * 新增库房进 TMS_STATION_IN ,库房出 TMS_STATION_OUT
				 */
				if (note.getStatus().equals(TmallFlowEnum.TMS_ACCEPT.getTmall_state())) { // 如果是揽收,则
																							// 新增
																							// 分站进
					AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, TmallFlowEnum.TMS_STATION_IN.getTmall_state(), false);
				} else if (note.getStatus().equals(TmallFlowEnum.TMS_DELIVERING.getTmall_state())) { // 如果是派送中,则
																										// 新增
																										// 分站出
					AppendXMLSendAndFeedBack(tmall, (int) b2cData.getFlowordertype(), tmallConfig, b2cData, note, TmallFlowEnum.TMS_STATION_OUT.getTmall_state(), false);
				}

				b2cids += b2cData.getB2cid() + ",";

			} catch (Exception e) {
				String expt = "处理[tmall]推送业务逻辑发生不可预估的异常,订单号:" + b2cData.getCwb() + "," + e;
				expt = ExceptionTrace.getExceptionTrace(e, expt);
				logger.error(expt);
				Mail.LoadingAndSendMessage(expt);
				return 0;
			}
		}
		b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";
		// 发送给dmp
		flowFromJMSB2cService.sendTodmp(b2cids);
		return 1;

	}

	/**
	 * 
	 * @param tmall
	 * @param flowordertype
	 * @param tmallConfig
	 * @param b2cData
	 * @param note
	 * @param TMS_Status
	 * @param isfactExit
	 *            判断是否真实存在，true表示存在 ，false表示不存在
	 * @throws Exception
	 */
	private void AppendXMLSendAndFeedBack(Tmall tmall, int flowordertype, TmallConfig tmallConfig, B2CData b2cData, TmallXMLNote note, String TMS_Status, boolean isfactExists) throws Exception {
		String requestXML;
		requestXML = AppendXMLString(tmall, b2cData, note, flowordertype, TMS_Status, isfactExists);
		SendHttpXmlAndFeedBack(tmall, TMS_Status != null && !"".equals(TMS_Status) ? TMS_Status : note.getStatus(), flowordertype, tmallConfig, b2cData, requestXML, note, isfactExists);
	}

	// 发送XML和返回XMl的处理
	private void SendHttpXmlAndFeedBack(Tmall tmall, String TmallStateCode, int flowordertype, TmallConfig tmallConfig, B2CData b2cData, String requestXML, TmallXMLNote note, boolean isfactExists) {
		Map<String, Object> respMap = null;
		String responsexml = "";
		int send_b2c_flag = 0;
		String is_success = "";
		try {

			responsexml = TmallConfig.posthttpdata_tmall(tmall.getPost_url(), tmall.getPrivate_key(), tmall.getPartner(), requestXML, TmallStateCode, isfactExists);
			logger.info("订单号：{},tmall返回:状态[" + flowordertype + "," + TmallStateCode + "],tmall-XML：{}", b2cData.getCwb(), responsexml);
			respMap = tmallConfig.Analyz_XmlDocByTmall(responsexml);

		} catch (Exception e) {
			// throw new
			// RuntimeException("推送tmall返回异常！,状态：["+flowordertype+","+TmallStateCode+"],当前订单号:"+b2cData.getCwb()+"异常原因:"+ExceptionTrace.getExceptionTrace(e,"推送tmall方法异常"));
			logger.error("解析tmall-xml异常，cwb=" + b2cData.getCwb() + ",tmall返回" + responsexml, e);
			try {
				b2cDataDAO.updateB2cIdSQLResponseStatus(b2cData.getB2cid(), 2, responsexml.length() > 480 ? responsexml.substring(0, 480) : responsexml);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}

		try {
			is_success = respMap.get("is_success").toString();

			send_b2c_flag = is_success.equals("T") ? 1 : 2;
			String is_remark = send_b2c_flag == 2 ? respMap.get("error").toString() : "";

			b2cDataDAO.updateB2cIdSQLResponseStatus(b2cData.getB2cid(), send_b2c_flag, is_remark);
			logger.info("更新tmall状态反馈[" + is_success + "],当前状态{},订单号：{},b2cdata=" + b2cData.getB2cid(), (flowordertype + "," + TmallStateCode), b2cData.getCwb());
		} catch (Exception e) {
			throw new RuntimeException("更新tmall状态反馈faild,当前状态[" + flowordertype + "," + TmallStateCode + "],订单号：" + b2cData.getCwb() + ExceptionTrace.getExceptionTrace(e, "异常原因="));
		}
	}

	// 拼接XML
	private String AppendXMLString(Tmall tmall, B2CData b2cData, TmallXMLNote note, int flowordertype, String TMS_STATUS, boolean isfactExit) {// 签收前支付的标识
		StringBuffer sub = new StringBuffer();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sub.append("<request>");
		sub.append("<out_biz_code>" + (b2cData.getB2cid() + (isfactExit == true ? "_1" : "_2")) + "</out_biz_code>");
		sub.append("<service_code>" + note.getService_code() + "</service_code>");
		sub.append("<order_code>" + (b2cData.getShipcwb() != null && !"".equals(b2cData.getShipcwb()) ? b2cData.getShipcwb() : b2cData.getCwb()) + "</order_code>");
		sub.append("<tms_order_code>" + b2cData.getCwb() + "</tms_order_code>");
		sub.append("<operator>" + note.getOperator() + "</operator>");
		sub.append("<operator_contact>" + note.getOperator_contact() + "</operator_contact>");
		sub.append("<operator_date>" + note.getOperator_date() + "</operator_date>");
		sub.append("<status>" + (TMS_STATUS != null ? TMS_STATUS : note.getStatus()) + "</status>");
		sub.append("<content>" + note.getContent() + "</content>");
		sub.append("<remark>" + note.getRemark() + "</remark>");
		sub.append("</request>");
		// logger.info("请求tmall:状态["+flowordertype+","+(TMS_STATUS!=null?TMS_STATUS:note.getStatus())+"],推送tmall-XML：{}",sub.toString());
		return sub.toString();
	}

	public String getTmallFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (TmallFlowEnum TEnum : TmallFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getTmall_state();
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return TmallFlowEnum.TMS_SIGN.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return TmallFlowEnum.TMS_ERROR.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return TmallFlowEnum.TMS_ERROR.getTmall_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			return TmallFlowEnum.TMS_FAILED.getTmall_state();
		}
		return null;

	}

	/**
	 * 获取tmall的配置
	 * 
	 * @param customer
	 * @return
	 */
	public String getTmallB2cEnumKeys(Customer customer) {
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("tmall")) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					return String.valueOf(enums.getKey());
				}
			}
		}
		return null;
	}

	public void test_insert() {
		String xml = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" + "<html>" + "<head><title>302 Found</title></head>" + "<body bgcolor=\"white\">" + "<h1>302 Found</h1>"
				+ "<p>The requested resource resides temporarily under a different URI.</p>" + "<hr/>Powered by Tengine" + "</body>" + "</html>";

		try {
			b2cDataDAO.updateB2cIdSQLResponseStatus(3862, 2, xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 补发推送失败的信息 只推送结果为 2 send_b2c_flag=2 每 1 个小时 推送一次 最多能推送 20次
	 */
	public void feedbackFailedAgain(Tmall tmall, String tmallStateCode) {

		try {
			int flowordertype = getTmallStateByCode(tmallStateCode);

			List<B2CData> tmallDataList = b2cDataDAO.getDataListFailedByFlowStatus(flowordertype, tmall.getCustomerids(), tmall.getSelectMaxCount(), 5, null);
			if (tmallDataList == null || tmallDataList.size() == 0) {
				logger.info("当前没有待重发的失败的数据tmall");
				return;
			}

			TmallConfig tmallConfig = new TmallConfig();

			for (B2CData b2cData : tmallDataList) {
				try {

					String jsoncontent = b2cData.getJsoncontent();
					TmallXMLNote note = getTmallXMLNoteMethod(jsoncontent);
					if (tmall.getIsCallBackError() == 1 && note.getActionPushCode().equals("new")) { // 是否回传TMS_ERROR异常信息
																										// 0不回传，1
																										// 回传。
						// 执行新的接口异常推送
						tmallErrorService.excuteTmallExceptionMethod(tmall, b2cData, note);
						continue;
					}

					excuteSendTmallServer(tmall, flowordertype, tmallConfig, b2cData, note);

				} catch (Exception e) {
					logger.error("处理tmall推送业务逻辑发生不可预估的异常-again" + b2cData.getCwb(), e);
				}
			}

		} catch (Exception e) {
			logger.error("error info by request tmall interface Again!", e);
		}
	}

}
