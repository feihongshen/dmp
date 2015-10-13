package cn.explink.b2c.haoxgou;

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
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;

@Service
public class HaoXiangGouService {
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2CDataDAO;
	private Logger logger = LoggerFactory.getLogger(HaoXiangGouService.class);

	// 获取配置信息
	public HaoXiangGou getHaoXiangGouSettingMethod(int key) {
		HaoXiangGou hxg = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			hxg = (HaoXiangGou) JSONObject.toBean(jsonObj, HaoXiangGou.class);
		} else {
			hxg = new HaoXiangGou();
		}
		return hxg;
	}

	/**
	 * 订单状态反馈的公用类
	 */
	public long feedback_status() {
		long calcCount = 0;
		// 订单配送信息提交接口
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.RuKu.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.YiShenHe.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue());
		calcCount += SubmitDeliveryInfo(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());

		// Cod支付信息提交接口
		SubmitCodPayInfo();

		return calcCount;
	}

	/**
	 * 订单配送流程反馈
	 */
	public long SubmitDeliveryInfo(long flowordertype) {
		long calcCount = 0;
		HaoXiangGou hxg = getHaoXiangGouSettingMethod(B2cEnum.HaoXiangGou.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.HaoXiangGou.getKey())) {
			logger.info("未开启0好享购0状态反馈接口");
			return -1;
		}

		try {

			int i = 0;
			while (true) {
				List<B2CData> hxgdatalist = b2CDataDAO.getDataListByFlowStatus(flowordertype, hxg.getCustomerids(), hxg.getMaxCount());
				i++;
				if (i > 30) {
					String warning = "查询0好享购0状态反馈已经超过30次循环，每次50条，可能存在程序未知异常,请及时查询并处理!";
					logger.warn(warning);
					return 0;
				}

				if (hxgdatalist == null || hxgdatalist.size() == 0) {
					logger.info("当前没有要推送0好享购0的数据");
					return 0;
				}

				buildParmsSending_SubmitDeliveryInfo(hxg, hxgdatalist);

				calcCount += hxgdatalist.size();
			}

		} catch (Exception e) {
			logger.error("调用0好享购0webservice服务器异常" + e.getMessage(), e);
		}

		return calcCount;

	}

	private void buildParmsSending_SubmitDeliveryInfo(HaoXiangGou hxg, List<B2CData> dataList) throws Exception {

		for (B2CData data : dataList) {
			long b2cid = data.getB2cid();
			String jsoncontent = data.getJsoncontent();
			HaoXiangGouXMLNote note = getHaoXiangGouXMLNoteMethod(jsoncontent);

			String dlver_cd = DESUtil.encrypt(hxg.getDlver_cd(), hxg.getDes_key());
			String ord_id = DESUtil.encrypt(note.getOrd_id(), hxg.getDes_key());
			String invc_id = DESUtil.encrypt(note.getInvc_id(), hxg.getDes_key());
			String dlv_stat_cd = DESUtil.encrypt(note.getDlv_stat_cd(), hxg.getDes_key());
			String dlv_stat_date = DESUtil.encrypt(filtertime(note.getDlv_stat_date()), hxg.getDes_key());
			String transit_info = DESUtil.encrypt(note.getTransit_info(), hxg.getDes_key());
			String declined_rsn = DESUtil.encrypt(note.getDeclined_rsn(), hxg.getDes_key());

			String parmslogs = "dlver_cd=" + dlver_cd + ",ord_id=" + note.getOrd_id() + ",invc_id=" + note.getInvc_id() + ",dlv_stat_cd=" + note.getDlv_stat_cd() + ",dlv_stat_date="
					+ note.getDlv_stat_date() + ",transit_info=" + note.getTransit_info() + ",declined_rsn=" + note.getDeclined_rsn();

			Object parms[] = { dlver_cd, ord_id, invc_id, dlv_stat_cd, dlv_stat_date, transit_info, declined_rsn, hxg.getPassword() };
			logger.info("好享购-配送流程反馈(请求)parms={}", parmslogs);

			String rtn_data = (String) WebServiceHandler.invokeWs(hxg.getRequst_url(), "SubmitDeliveryInfo", parms);
			logger.info("好享购-配送流程反馈(返回)rtn_data={}", rtn_data);

			dealWithFeedBackResponse(b2cid, rtn_data);
		}

	}

	public static void main(String[] args) throws Exception {
		// parms=dlver_cd=QOOvZqtKS5Y=,ord_id=14581319,invc_id=626080120493,dlv_stat_cd=04,dlv_stat_date=2014-03-05
		// 17:28:20,transit_info=货物已由[靖江站94]的小件员[赵栋]反馈为[配送成功],电话[18952626967\052382832802],declined_rsn=
		String str = "货物已由[靖江站94]的小件员[赵栋]反馈为[配送成功],电话[18952626967\052382832802]";
		String transit_info = DESUtil.encrypt(str, "31262903");

		Object parms[] = { "QOOvZqtKS5Y=", "14581319", "626080120493", "04", "2014-03-05 17:28:20", transit_info, "", "qsKr3MR5QW3cPyd6nirRIZWiWIrNR2r7" };

		Object parms1[] = { "QOOvZqtKS5Y=", "14581319", "626080120493", "04", "2014-03-05 17:28:20", "1111111", "qsKr3MR5QW3cPyd6nirRIZWiWIrNR2r7" };

		// http://221.226.72.18:8009/default/POS_PDA_DLVERService?wsdl
		// http://221.226.72.18:8009/default/POS_PDA_DLVERService?wsdl
		String rtn_data = (String) WebServiceHandler.invokeWs("http://221.226.72.18:8009/default/POS_PDA_DLVERService?wsdl", "SubmitDeliveryInfo", parms1);
		System.out.println(rtn_data);
	}

	private static String filtertime(String time) {
		if (time == null || time.isEmpty()) {
			return DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		}
		try {
			return time.replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "");
		} catch (Exception e) {
			return DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		}
	}

	/**
	 * 0好享购COD付款0状态反馈
	 */
	public void SubmitCodPayInfo() {
		HaoXiangGou hxg = getHaoXiangGouSettingMethod(B2cEnum.HaoXiangGou.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.HaoXiangGou.getKey())) {
			logger.info("未开启0好享购0状态反馈接口");
			return;
		}

		try {

			int i = 0;
			while (true) {
				List<B2CData> hxgdatalist = b2CDataDAO.getDataListByCodPayAmount(hxg.getCustomerids(), hxg.getMaxCount());

				i++;
				if (i > 30) {
					String warning = "查询0好享购COD付款0信息反馈已经超过30次循环，可能存在程序未知异常,请及时查询并处理!";
					Mail.LoadingAndSendMessage(warning);
					return;
				}

				if (hxgdatalist == null || hxgdatalist.size() == 0) {
					logger.info("当前没有要推送0好享购0COD付款的数据");
					return;
				}

				buildParmsSending_SubmitCodPayInfo(hxg, hxgdatalist);
			}

		} catch (Exception e) {
			logger.error("调用0好享购0webservice服务器异常" + e.getMessage(), e);
		}

	}

	private void buildParmsSending_SubmitCodPayInfo(HaoXiangGou hxg, List<B2CData> dataList) throws Exception {

		for (B2CData data : dataList) {
			long b2cid = data.getB2cid();
			String jsoncontent = data.getJsoncontent();
			HaoXiangGouXMLNote note = getHaoXiangGouXMLNoteMethod(jsoncontent);
			String rtn_data = "";
			if (note.getPaytypeflag() == 1) { // Cod付款通知
				String dlver_cd = DESUtil.encrypt(hxg.getDlver_cd(), hxg.getDes_key());
				String ord_id = DESUtil.encrypt(note.getOrd_id(), hxg.getDes_key());
				String invc_id = DESUtil.encrypt(note.getInvc_id(), hxg.getDes_key());
				String payer_nm = DESUtil.encrypt(note.getPayer_nm(), hxg.getDes_key());
				String pay_date = DESUtil.encrypt(filtertime(note.getPay_date()), hxg.getDes_key());
				String pay_cd = DESUtil.encrypt(note.getPay_cd(), hxg.getDes_key());
				String pay_amt = DESUtil.encrypt(note.getPay_amt(), hxg.getDes_key());
				String pay_company_cd = DESUtil.encrypt(note.getPay_company_cd(), hxg.getDes_key());

				String parmslogs = "dlver_cd=" + hxg.getDlver_cd() + ",ord_id=" + note.getOrd_id() + ",invc_id=" + note.getInvc_id() + ",payer_nm=" + note.getPayer_nm() + ",pay_date="
						+ note.getPay_date() + ",pay_cd=" + note.getPay_cd() + ",pay_amt=" + note.getPay_amt() + ",pay_company_cd=" + pay_company_cd;

				Object parms[] = { dlver_cd, ord_id, invc_id, payer_nm, pay_date, pay_cd, pay_amt, pay_company_cd, hxg.getPassword() };
				logger.info("好享购-Cod付款反馈(请求)parms={}", parmslogs);

				rtn_data = (String) WebServiceHandler.invokeWs(hxg.getRequst_url(), "SubmitCodPayInfo", parms);
				logger.info("好享购-Cod付款反馈(返回)rtn_data={}", rtn_data);
				dealWithCodPayResponse(b2cid, rtn_data);
			} else if (note.getPaytypeflag() == 2) { // 退款垫付信息提交接口
				String rtn_id = DESUtil.encrypt(note.getRtn_id(), hxg.getDes_key());
				String ord_id = DESUtil.encrypt(note.getOrd_id(), hxg.getDes_key());
				String dlver_cd = DESUtil.encrypt(hxg.getDlver_cd(), hxg.getDes_key());
				String payee = DESUtil.encrypt(note.getPayee(), hxg.getDes_key());
				String disburse_amt = DESUtil.encrypt(note.getDisburse_amt(), hxg.getDes_key());
				String disburse_date = DESUtil.encrypt(filtertime(note.getDisburse_date()), hxg.getDes_key());

				Object parms[] = { rtn_id, ord_id, dlver_cd, payee, disburse_amt, disburse_date, hxg.getPassword() };
				logger.info("好享购-退款垫付(请求)parms={}", parms);

				rtn_data = (String) WebServiceHandler.invokeWs(hxg.getRequst_url(), "SubmitDisburseInfo", parms);
				logger.info("好享购-退款垫付(返回)rtn_data={}", rtn_data);

				dealWithCodPayResponse(b2cid, rtn_data);
			} else {

				dealWithCodPayResponse(b2cid, "系统屏蔽无效数据");
				logger.info("好享购-当前没有推送cod支付信息的请求,cwb={}", data.getCwb());
			}

		}

	}

	private void dealWithFeedBackResponse(long b2cid, String response) throws Exception {
		int send_b2c_flag = 1;
		if (!response.contains("0")) {
			send_b2c_flag = 2;
		}
		b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, send_b2c_flag, response);
	}

	private void dealWithCodPayResponse(long b2cid, String response) throws Exception {
		int pay_flag = 3;
		if (response.indexOf("|") > 0) {
			if (response.substring(0, response.indexOf("|")).length() == 1 && response.contains("0")) {
				pay_flag = 1;
			} else {
				pay_flag = 2;
			}
		}

		b2CDataDAO.updatePayAmountFlagByMultiB2cId(String.valueOf(b2cid), pay_flag, response);
	}

	public HaoXiangGouXMLNote getHaoXiangGouXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, HaoXiangGouXMLNote.class);
	}

	public String getHXGFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (HXGFlowEnum TEnum : HXGFlowEnum.values()) {
				if (flowordertype == TEnum.getDelivery_state() && TEnum.getState() == 0) {
					return TEnum.getRequest_code();
				}
			}
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return HXGFlowEnum.PeiSongChengGong.getRequest_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return HXGFlowEnum.ShangMenHuanChengGong.getRequest_code();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return HXGFlowEnum.FenZhanZhiLiu.getRequest_code();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return HXGFlowEnum.JuShou.getRequest_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return HXGFlowEnum.BufenJuShou.getRequest_code();
		}

		return null;

	}

}
