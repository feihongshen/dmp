package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.camel.Header;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.PayUp;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.MD5.PaySign;

@Service
public class PayUpService {
	@Autowired
	UserDAO userDAO;

	@Autowired
	PayUpDAO payUpDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	CwbOrderService cwborderService;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	CwbRouteService cwbRouteService;

	@Autowired
	private OperationTimeDAO operationTimeDAO;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Produce(uri = "jms:topic:PayUp")
	ProducerTemplate sendJMSPayUp;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void subPayUpService(User user, Model model, PayUp payup, String gcaids, BigDecimal hk_amount, BigDecimal fa_amount, String gcaidsMAC) throws Exception {
		JSONObject sendJson = new JSONObject();
		sendJson.put("should_amount", hk_amount.add(fa_amount).doubleValue());// 应交款
		sendJson.put("payup_amount", payup.getAmount().doubleValue());// 实交款
		sendJson.put("payup_amount_pos", payup.getAmountPos().doubleValue());// POS刷卡金额
		sendJson.put("arrearage_huo_amount", hk_amount.doubleValue());// 默认交款欠款
		sendJson.put("arrearage_fa_amount", fa_amount.doubleValue());// 默认罚款欠款
		sendJson.put("payup_type", payup.getType());// 交款类型1货款 2罚款
		sendJson.put("gcaids", gcaids);// 交款对应的当前的归班记录的id
		sendJson.put("remark", payup.getRemark());
		sendJson.put("credatetime", this.df.format(new Date()));
		sendJson.put("branchid", payup.getBranchid());
		sendJson.put("payup_realname", user.getRealname());
		sendJson.put("upbranchid", payup.getUpbranchid());// 上交至哪个财务
		sendJson.put("way", payup.getWay());// 缴款方式

		if ((payup.getType() == 1) && (gcaids.length() == 0) && (payup.getAmount().compareTo(BigDecimal.ZERO) > 0)) {// 只交欠款

			sendJson.put("payupid", this.payUpDAO.crePayUp(payup));
			// 更改欠款字段
			this.branchDAO.saveBranchArrearageHuo(hk_amount.subtract(payup.getAmount()), user.getBranchid());
			sendJson.put("arrearage_huo_amount", hk_amount.subtract(payup.getAmount()).doubleValue());
			model.addAttribute("error", "交货款成功");
		} else if (payup.getType() == 1) {// 交货款

			if ((gcaids.length() > 0) && gcaidsMAC.equals(PaySign.Md5(gcaids))) {
				String[] gcaidArray = gcaids.split("','");// 当前处理的归班记录的id集合 格式为
															// '213','214'
				// 根据当前要处理的归班id集合，获取当前为被结算的归班记录
				List<GotoClassAuditing> gcaList = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaids(gcaids);
				// 如果当前未结算过的归班记录跟将要处理的归班记录id集合数量一致
				if (gcaidArray.length != gcaList.size()) {
					model.addAttribute("error", "系统中款项明细有差异，请重新操作。");
				} else {

					// 存储交款记录

					long payupid = this.payUpDAO.crePayUp(payup);

					// 拿到结算表新的结算记录id 更新对应的归班记录
					this.gotoClassAuditingDAO.updateGotoClassAuditingForPayupidByGcaids(gcaids, payupid);
					// 更改欠款字段
					this.branchDAO.saveBranchArrearageHuo(hk_amount.subtract(payup.getAmount()), user.getBranchid());
					sendJson.put("arrearage_huo_amount", hk_amount.subtract(payup.getAmount()).doubleValue());
					model.addAttribute("error", "交货款成功");
					sendJson.put("payupid", payupid);
				}
			} else {
				model.addAttribute("error", "交款参数不正确");
			}

		} else {// 2交罚款

			sendJson.put("payupid", this.payUpDAO.crePayUp(payup));

			this.branchDAO.saveBranchArrearageFa(fa_amount.subtract(payup.getAmount()), user.getBranchid());
			sendJson.put("arrearage_fa_amount", fa_amount.subtract(payup.getAmount()).doubleValue());
			model.addAttribute("error", "交罚款成功");
		}

		try {
			this.sendPayUp(sendJson.toString());
		} catch (Exception ee) {

		}
	}

	/**
	 * 不需要归班的上交款
	 *
	 * @param user
	 * @param model
	 * @param payup
	 * @param delids
	 * @param hk_amount
	 * @param fa_amount
	 * @param delidsMAC
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void subPayUpByNotAuditService(User user, Model model, PayUp payup, String delids, BigDecimal hk_amount, BigDecimal fa_amount, String delidsMAC) throws Exception {
		JSONObject sendJson = new JSONObject();
		sendJson.put("should_amount", hk_amount.add(fa_amount).doubleValue());// 应交款
		sendJson.put("payup_amount", payup.getAmount().doubleValue());// 实交款
		sendJson.put("payup_amount_pos", payup.getAmountPos().doubleValue());// POS刷卡金额
		sendJson.put("arrearage_huo_amount", hk_amount.doubleValue());// 默认交款欠款
		sendJson.put("arrearage_fa_amount", fa_amount.doubleValue());// 默认罚款欠款
		sendJson.put("payup_type", payup.getType());// 交款类型1货款 2罚款
		// sendJson.put("gcaids", gcaIds);//交款对应的当前的归班记录的id
		sendJson.put("remark", payup.getRemark());
		sendJson.put("credatetime", this.df.format(new Date()));
		sendJson.put("branchid", payup.getBranchid());
		sendJson.put("payup_realname", user.getRealname());
		sendJson.put("upbranchid", payup.getUpbranchid());// 上交至哪个财务
		sendJson.put("way", payup.getWay());// 缴款方式
		String gcaIds = "";// 归班ids
		if ((payup.getType() == 1) && (delids.length() == 0) && (payup.getAmount().compareTo(BigDecimal.ZERO) > 0)) {// 只交欠款
			sendJson.put("payupid", this.payUpDAO.crePayUp(payup));
			// 更改欠款字段
			this.branchDAO.saveBranchArrearageHuo(hk_amount.subtract(payup.getAmount()), user.getBranchid());
			sendJson.put("arrearage_huo_amount", hk_amount.subtract(payup.getAmount()).doubleValue());
			model.addAttribute("error", "交货款成功");
		} else if (payup.getType() == 1) {// 交货款
			if ((delids.length() > 0) && delidsMAC.equals(PaySign.Md5(delids))) {
				String[] gcaidArray = delids.split("','");// 当前处理的反馈记录的id集合 格式为
															// '213','214'
				// 根据当前要处理的反馈id集合，获取当前为被结算的反馈记录
				List<DeliveryState> delList = this.deliveryStateDAO.getDeliveryByIds(delids);

				// 如果当前未结算过的反馈记录跟将要处理的反馈记录id集合数量一致
				if (gcaidArray.length != delList.size()) {
					model.addAttribute("error", "系统中款项明细有差异，请重新操作。");
				} else {
					// 存储交款记录
					long payupid = this.payUpDAO.crePayUp(payup);
					List<JSONObject> jsonList = this.deliveryStateDAO.getDeliveryByIdsAndDeliveryId(delids);
					String okTime = this.df.format(new Date());
					for (JSONObject delJson : jsonList) {
						// 创建归班表
						this.logger.info("用户:{},开始创建归班记录,金额为{},pos为{},包含{}", new Object[] { user.getUserid(), payup.getAmount().doubleValue(), payup.getAmountPos().doubleValue(), delids });
						long gcaId = this.gotoClassAuditingDAO.creGotoClassAuditing(okTime, delJson.getString("subAmount"), delJson.getString("subAmountPos"), user.getUserid(), user.getBranchid(),
								delJson.getLong("deliveryid"));
						// 更改反馈记录 中的归班gcaid和交款payupid
						this.deliveryStateDAO.updateDeliveryByIds(delids, delJson.getLong("deliveryid"), payupid, gcaId, okTime);
						// 创建跟踪记录
						List<DeliveryState> delLists = this.deliveryStateDAO.getDeliveryByGcaid(gcaId);
						this.creFlow(user, delLists, okTime, gcaId);
						gcaIds += gcaId + ",";
					}
					gcaIds = gcaIds.length() > 0 ? gcaIds.substring(0, gcaIds.length() - 1) : "";
					this.logger.info("开始更新归班记录订单,id:{}", gcaIds);
					// 拿到结算表新的结算记录id 更新对应的归班记录
					this.gotoClassAuditingDAO.updateGotoClassAuditingForPayupidByGcaids(gcaIds, payupid);
					// 更改欠款字段
					this.branchDAO.saveBranchArrearageHuo(hk_amount.subtract(payup.getAmount()), user.getBranchid());
					sendJson.put("arrearage_huo_amount", hk_amount.subtract(payup.getAmount()).doubleValue());
					model.addAttribute("error", "交货款成功");
					sendJson.put("payupid", payupid);
				}
			} else {
				model.addAttribute("error", "交款参数不正确");
			}

		} else {// 2交罚款
			sendJson.put("payupid", this.payUpDAO.crePayUp(payup));
			this.branchDAO.saveBranchArrearageFa(fa_amount.subtract(payup.getAmount()), user.getBranchid());
			sendJson.put("arrearage_fa_amount", fa_amount.subtract(payup.getAmount()).doubleValue());
			model.addAttribute("error", "交罚款成功");
		}
		try {
			// 归班发送JMS
			List<GotoClassAuditing> gcaList = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaids(gcaIds);
			for (GotoClassAuditing gotoClassAuditing : gcaList) {
				this.cwborderService.okJMS(gotoClassAuditing);
			}
			// 发送交款JMS
			sendJson.put("gcaids", gcaIds);// 交款对应的当前的归班记录的id
			this.sendPayUp(sendJson.toString());
		} catch (Exception ee) {

		}
	}

	// =====系统自动归班 begin=====
	private void creFlow(User user, List<DeliveryState> delLists, String okTime, long gcaId) {
		for (DeliveryState del : delLists) {
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(del.getCwb());
			DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			if (deliverystate == null) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
			}
			FlowOrderTypeEnum auditFlowOrderTypeEnum = FlowOrderTypeEnum.YiShenHe;
			// 更改订单的订单状态为退货的流向
			this.deliverPodForCwbstate(co.getCwb(), deliverystate.getDeliverystate(), auditFlowOrderTypeEnum);
			// 更改反馈表中的归班时间
			this.cwborderService.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.YiShenHe, "", System.currentTimeMillis(), co.getCwb(), false);
			// 当订单归班审核配送成功和上门退拒退 和 货物丢失状态时，删除操作时间记录
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue())
					|| (deliverystate.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue())) {
				this.operationTimeDAO.delOperationTime(co.getCwb());
			} else {// 如果不是最终，则更新跟踪记录
				this.operationTimeDAO.updateOperationTime(co.getCwb(), user.getBranchid(), FlowOrderTypeEnum.YiShenHe.getValue(), deliverystate.getDeliverystate(), co.getNextbranchid());
			}
		}

	}

	// 更改订单的订单状态为退货的流向
	private void deliverPodForCwbstate(String cwb, long podresultid, FlowOrderTypeEnum auditFlowOrderTypeEnum) {
		if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
				|| (podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
		} else if (podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			this.updateCwbState(cwb, CwbStateEnum.DiuShi);
		}
		// 处理站点
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?, currentbranchid=0 where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
		} else {
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
		}
	}

	@Transactional
	public void updateCwbState(String cwb, CwbStateEnum state) {
		this.cwbDAO.updateCwbState(cwb, state);
		this.updateNextBranchId(cwb);
	}

	private void updateNextBranchId(String cwb) {

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
			this.logger.info("配送订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getDeliverybranchid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getDeliverybranchid());
			if (nextbranchid != 0) {
				this.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
				this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
			}
			return;
		}
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.TuiHuo.getValue())) {
			this.logger.info("退货订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getTuihuoid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getTuihuoid());
			if (nextbranchid != 0) {
				this.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
				this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
			}
			return;
		}
	}

	// =====系统自动归班 end=====

	public void sendPayUp(String sendJson) {
		try{
			logger.info("消息发送端：sendJMSPayUp, PayUp={}", sendJson);
			this.sendJMSPayUp.sendBodyAndHeader(null, "PayUp", sendJson);
		}catch(Exception e){
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendPayUp")
					.buildExceptionInfo(e.toString()).buildTopic(this.sendJMSPayUp.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("PayUp", sendJson).getMqException());
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void payUpBack(@Header("payUpBack") String payUpBack) {
		logger.info(payUpBack);
		JSONObject o = JSONObject.fromObject(payUpBack);
		long id = o.get("payUpId") == null ? 0L : o.getLong("payUpId");
		if (id > 0) {
			String backRemark = o.get("checkremark") == null ? "" : o.getString("checkremark");
			this.payUpDAO.savePayUpByBack(id, backRemark);
		}
	}

}
