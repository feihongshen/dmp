package cn.explink.b2c.explink.code_down;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.Customer;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 易派接口处理类（下游存储状态待反馈）
 */
@Service
public class EPaiInterfaceService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	UserDAO userDAO;
	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	EpaiApiService epaiApiService;

	private ObjectMapper objectMapper = JacksonMapper.getInstance(); // 统一只实例化一次

	/**
	 * 查询供货商
	 * 
	 * @return
	 */
	public List<Customer> getCustomerList() {
		List<Customer> customerlist = GetDmpDAO.getStaticAllCustomers();
		return customerlist;
	}

	/**
	 * 构建派送信息发送上游易派Api
	 */
	public void buildFlowOrdersToEPaiAPI(CwbOrderWithDeliveryState cwbOrderWithDeliveryState, DmpOrderFlow orderFlow) {

		String cwb = "";
		try {

			DmpCwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			cwb = cwbOrder.getCwb();

			String receivedStatus = epaiApiService.getEpaiFlowEnum(orderFlow.getFlowordertype());

			EpaiApi epai = filterCustomerflag(cwbOrder.getCustomerid()); // 是否属于已配置的接口
			if (epai == null) {
				return;
			}
			if (epai.getIsfeedbackflag() == 0) {
				logger.info("未开启环形对接下游反馈");
				return;
			}

			if (receivedStatus == null) { // 发送至上游的对接状态
				logger.info("订单号：{} 不属于0下游系统对接0所需要的json---,状态{}，return", cwbOrder.getCwb(), orderFlow.getFlowordertype());
				return;
			}

			EPaiXMLNote xmlnote = new EPaiXMLNote();
			xmlnote.setUserCode(epai.getUserCode());
			xmlnote.setRequestTime(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
			xmlnote.setSign(MD5Util.md5(epai.getUserCode() + xmlnote.getRequestTime() + epai.getPrivate_key()));
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setOperatortime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setFlowordertype(receivedStatus);

			String deliverystate = "0";
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				DmpDeliveryState dmpDeliveryState = cwbOrderWithDeliveryState.getDeliveryState();
				deliverystate = getEpaiDeliveryState(deliverystate, dmpDeliveryState);

				xmlnote.setPaytype(Integer.valueOf(cwbOrder.getNewpaywayid()));
				xmlnote.setPayremark(dmpDeliveryState.getPosremark());

				buildExptReason(epai, xmlnote, dmpDeliveryState, cwbOrder); // 构建异常原因字段

			}
			xmlnote.setDeliverystate(deliverystate);
			xmlnote.setFloworderdetail(orderFlowDetail.getDetail(orderFlow));

			String Jsoncontent = objectMapper.writeValueAsString(xmlnote);

			// 插入b2cdata表
			saveB2cData(cwbOrderWithDeliveryState, orderFlow, cwbOrder, Jsoncontent);

			logger.info("订单号：{}封装为0系统对接0所需要的json---,状态{}", cwbOrder.getCwb(), orderFlow.getFlowordertype());

		} catch (Exception e) {
			logger.error("下游处理0系统对接0发生未知异常,cwb=" + cwb, e);

		}

	}

	private void buildExptReason(EpaiApi epai, EPaiXMLNote xmlnote, DmpDeliveryState dmpDeliveryState, DmpCwbOrder cwbOrder) {

		ExptReason reason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", dmpDeliveryState.getDeliverystate());
		String exptCode = "";
		String exptMsg = "";
		if (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()
				|| dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			List<ExptReason> exptlist = getDmpdao.getDMPExptReason(String.valueOf(epai.getCustomerid()), ReasonTypeEnum.ReturnGoods.getValue() + "");
			if (exptlist == null || exptlist.size() == 0) {
				logger.warn("下游电商异常码-拒收没有设置！请尽快设置cwb=" + cwbOrder.getCwb());
			} else {
				exptCode = reason.getExpt_code() == null ? exptlist.get(0).getExpt_code() : reason.getExpt_code();
				exptMsg = reason.getExpt_msg() == null ? exptlist.get(0).getExpt_msg() : reason.getExpt_msg();
			}

		} else if (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			List<ExptReason> exptlist = getDmpdao.getDMPExptReason(String.valueOf(epai.getCustomerid()), ReasonTypeEnum.BeHelpUp.getValue() + "");
			if (exptlist == null || exptlist.size() == 0) {
				logger.warn("下游电商异常码-滞留没有设置！请尽快设置cwb=" + cwbOrder.getCwb());
			} else {
				exptCode = reason.getExpt_code() == null ? exptlist.get(0).getExpt_code() : reason.getExpt_code();
				exptMsg = reason.getExpt_msg() == null ? exptlist.get(0).getExpt_msg() : reason.getExpt_msg();
			}
		}

		xmlnote.setExptcode(exptCode);
		xmlnote.setExptmsg(exptMsg);
	}

	/**
	 * 根据下游系统状态转义为文档规范状态
	 * 
	 * @param deliverystate
	 * @param dmpDeliveryState
	 * @return
	 */
	private String getEpaiDeliveryState(String deliverystate, DmpDeliveryState dmpDeliveryState) {
		if (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			deliverystate = EPaiDeliverStateEnum.ChengGong.getRequest_code();
		} else if (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()
				|| dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			deliverystate = EPaiDeliverStateEnum.JuShou.getRequest_code();
		} else if (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			deliverystate = EPaiDeliverStateEnum.FenZhanZhiLiu.getRequest_code();
		}
		return deliverystate;
	}

	/**
	 * 保存B2Cdata
	 * 
	 * @param cwbOrderWithDeliveryState
	 * @param orderFlow
	 * @param cwbOrder
	 * @param Jsoncontent
	 */
	private void saveB2cData(CwbOrderWithDeliveryState cwbOrderWithDeliveryState, DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, String Jsoncontent) {
		B2CData b2cData = new B2CData();
		b2cData.setCwb(orderFlow.getCwb());
		b2cData.setCustomerid(cwbOrder.getCustomerid());
		b2cData.setFlowordertype(orderFlow.getFlowordertype());
		b2cData.setPosttime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		b2cData.setSend_b2c_flag(0);
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			try {
				DmpDeliveryState del = cwbOrderWithDeliveryState.getDeliveryState();
				b2cData.setDelId(del.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		b2cData.setJsoncontent(Jsoncontent); // 封装的JSON格式.
		String multi_shipcwb = StringUtils.hasLength(cwbOrder.getMulti_shipcwb()) ? cwbOrder.getMulti_shipcwb() : null;
		b2CDataDAO.saveB2CData(b2cData, multi_shipcwb);
	}

	private EpaiApi filterCustomerflag(long customerid) {

		for (EpaiApi epai : EpaiAPILis()) {
			if (epai.getIsfeedbackflag() == 0) {
				logger.info("环形未开始回传customerid={},Isfeedbackflag={}", customerid, 0);
				continue;
			}
			if (customerid == epai.getCustomerid()) {
				return epai;
			}
		}
		return null;
	}

	public List<EpaiApi> EpaiAPILis() {
		return epaiApiService.getEpaiAPIList();
	}

}
