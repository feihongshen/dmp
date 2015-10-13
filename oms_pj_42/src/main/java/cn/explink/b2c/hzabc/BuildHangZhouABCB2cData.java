package cn.explink.b2c.hzabc;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildHangZhouABCB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildHangZhouABCB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	HangZhouABCService hangZhouABCService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildHangZhouMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		String receivedStatus = hangZhouABCService.getHangZhouABCFlowEnum(flowOrdertype, delivery_state);

		if (receivedStatus == null) {
			logger.info("订单号：{} 不属于 0杭州ABC0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {
			logger.info("订单号：{}封装成0杭州ABC0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);

			HangZhouABC gzabc = hangZhouABCService.getHangZhouABCSettingMethod(B2cEnum.HangZhouABC.getKey());

			updateNewDeliveryState(orderFlow, cwbOrder);

			HangZhouXMLNote gzxmlNote = new HangZhouXMLNote();
			gzxmlNote.setLogisticProviderID(gzabc.getLogisticProviderID());
			gzxmlNote.setWaybillNo(cwbOrder.getCwb());
			gzxmlNote.setStatus(receivedStatus);
			gzxmlNote.setStateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			User user = getDmpdao.getUserById(orderFlow.getUserid());
			Branch branch = getDmpdao.getNowBranch(orderFlow.getBranchid());
			gzxmlNote.setOperName(user.getRealname());
			gzxmlNote.setOperateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			gzxmlNote.setOperatorUnit(branch.getBranchname());
			gzxmlNote.setScanId(String.valueOf(orderFlow.getFloworderid()));

			if (receivedStatus == HangZhouFlowEnum.SC01.getAbc_state()) { // 入库
				gzxmlNote.setPreSiteName("杭州ABC");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setActualMny("");
			} else if (receivedStatus == HangZhouFlowEnum.SC06_2.getAbc_state() || receivedStatus == HangZhouFlowEnum.SC06_1.getAbc_state() || receivedStatus == HangZhouFlowEnum.SC06_3.getAbc_state()
					|| receivedStatus == HangZhouFlowEnum.SC06_4.getAbc_state()) {// 库房出库,
																					// 中转中
																					// 、分站退货出站
				gzxmlNote.setPreSiteName(getDmpdao.getNowBranch(cwbOrder.getStartbranchid()).getBranchname());
				gzxmlNote.setNextSiteName(getDmpdao.getNowBranch(cwbOrder.getNextbranchid()).getBranchname());
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setActualMny("");
			} else if (receivedStatus == HangZhouFlowEnum.SC02.getAbc_state()) {// 配送中
				User deliverUser = getDmpdao.getUserById(cwbOrder.getDeliverid());
				gzxmlNote.setPreSiteName(getDmpdao.getNowBranch(cwbOrder.getStartbranchid()).getBranchname());
				gzxmlNote.setNextSiteName(getDmpdao.getNowBranch(cwbOrder.getNextbranchid()).getBranchname());
				gzxmlNote.setDeliveryMan(deliverUser.getRealname());
				gzxmlNote.setDeliveryMobile(deliverUser.getUsermobile());
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setActualMny("");
			}

			else if (receivedStatus == HangZhouFlowEnum.SC03.getAbc_state()) {// 妥投
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark(cwbOrder.getConsigneename());
				gzxmlNote.setActualMny("");
				gzxmlNote.setPayMethod(getPayMehtod(cwbOrder));

			}

			else if (receivedStatus == HangZhouFlowEnum.SC04.getAbc_state()) {// 滞留
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setActualMny("");
				gzxmlNote.setPayMethod("");

				ExptReason exptReason = b2cTools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), cwbOrderWothDeliverystate
						.getDeliveryState().getDeliverystate());
				if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					String otherReason = cwbOrder.getLeavedreason() == null || "".equals(cwbOrder.getLeavedreason()) ? "其他原因" : cwbOrder.getLeavedreason();

					String expt_msg = (exptReason.getExpt_msg() == null || exptReason.getExpt_msg().equals("")) ? otherReason : exptReason.getExpt_msg();
					String expt_code = (exptReason.getExpt_code() == null || exptReason.getExpt_code().equals("")) ? "OR" : exptReason.getExpt_code();
					gzxmlNote.setReason(expt_code);
					gzxmlNote.setRemark(expt_msg);
				}

			}

			else if (receivedStatus == HangZhouFlowEnum.SC05.getAbc_state()) {// 拒收
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setActualMny("");
				gzxmlNote.setPayMethod("");

				ExptReason exptReason = b2cTools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), cwbOrderWothDeliverystate
						.getDeliveryState().getDeliverystate());
				if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
					String otherReason = cwbOrder.getBackreason() == null || "".equals(cwbOrder.getBackreason()) ? "其他原因" : cwbOrder.getBackreason();

					String expt_msg = (exptReason.getExpt_msg() == null || exptReason.getExpt_msg().equals("")) ? otherReason : exptReason.getExpt_msg();
					String expt_code = (exptReason.getExpt_code() == null || exptReason.getExpt_code().equals("")) ? "OR" : exptReason.getExpt_code();
					gzxmlNote.setReason(expt_code);
					gzxmlNote.setRemark(expt_msg);
				}

			}

			else if (receivedStatus == HangZhouFlowEnum.SC08.getAbc_state() || receivedStatus == HangZhouFlowEnum.SC07.getAbc_state()) {// 已退货
																																		// 或丢失
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setActualMny("");
				gzxmlNote.setPayMethod("");

			} else if (receivedStatus == HangZhouFlowEnum.SC13.getAbc_state()) {// 换货成功
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setActualMny(String.valueOf(cwbOrderWothDeliverystate.getDeliveryState().getReceivedfee())); // 实收款
				gzxmlNote.setPayMethod("");
			} else if (receivedStatus == HangZhouFlowEnum.SC14.getAbc_state()) {// 半签半退->部分退货
				gzxmlNote.setPreSiteName("");
				gzxmlNote.setNextSiteName("");
				gzxmlNote.setDeliveryMan("");
				gzxmlNote.setDeliveryMobile("");
				gzxmlNote.setReason("");
				gzxmlNote.setRemark("");
				gzxmlNote.setActualMny(String.valueOf(cwbOrderWothDeliverystate.getDeliveryState().getReceivedfee())); // 实收款
				gzxmlNote.setPayMethod("");
			}

			logger.info("订单号：{}封装成0杭州ABC0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
			return objectMapper.writeValueAsString(gzxmlNote);
		}
	}

	private String getPayMehtod(DmpCwbOrder cwbOrder) {
		for (PaytypeEnum p : PaytypeEnum.values()) {
			if (String.valueOf(p.getValue()).contains(cwbOrder.getNewpaywayid())) {
				return p.getText();
			}
		}
		return PaytypeEnum.Xianjin.getText();

	}

	/**
	 * 更新短时间内出现多次归班的问题，以最新的一次为准.
	 * 
	 * @param orderFlow
	 * @param cwbOrder
	 */
	private void updateNewDeliveryState(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder) {
		try {
			long flowordertype = orderFlow.getFlowordertype();
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				return;
			}
			B2CData b2CData = b2CDataDAO.getB2cDataByKeys(orderFlow.getCwb(), flowordertype, 0);
			if (b2CData != null) {
				logger.warn("系统自动屏蔽状态={},订单号={}", FlowOrderTypeEnum.YiShenHe.getValue(), cwbOrder.getCwb());
				b2CDataDAO.updateB2cIdSQLResponseStatus(b2CData.getB2cid(), 3, "系统自动屏蔽此状态");
			}
		} catch (Exception e) {
			logger.error("ABC修改短时间内未推送36状态发生未知异常", e);
		}
	}

}
