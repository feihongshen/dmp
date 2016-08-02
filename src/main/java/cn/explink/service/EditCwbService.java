package cn.explink.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tps.ThirdPartyOrder2DOCfgService;
import cn.explink.dao.AccountCwbDetailDAO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AccountCwbSummaryDAO;
import cn.explink.dao.AccountDeducDetailDAO;
import cn.explink.dao.AccountDeductRecordDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryCashDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EditCwbDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.FinanceAuditDAO;
import cn.explink.dao.FinanceDeliverPayUpDetailDAO;
import cn.explink.dao.FinancePayUpAuditDAO;
import cn.explink.dao.FnOrgOrderAdjustRecordDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.dao.ReturnCwbsDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbDetail;
import cn.explink.domain.AccountCwbSummary;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeductRecord;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.FinanceAudit;
import cn.explink.domain.FinanceDeliverPayupDetail;
import cn.explink.domain.FinancePayUpAudit;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.OrgOrderAdjustmentRecord;
import cn.explink.domain.PayUp;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.BillAdjustTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.EditCwbTypeEnum;
import cn.explink.enumutil.FinanceAuditTypeEnum;
import cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PayMethodSwitchEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;

@Service
public class EditCwbService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private DeliveryStateDAO deliveryStateDAO;
	@Autowired
	private GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	private PayUpDAO payUpDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private FinanceAuditDAO financeAuditDAO;
	@Autowired
	private FinancePayUpAuditDAO financePayUpAuditDAO;

	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	@Autowired
	private DeliveryCashDAO deliveryCashDAO;

	@Autowired
	private FinanceDeliverPayUpDetailDAO financeDeliverPayUpDetailDAO;

	@Autowired
	private EditCwbDAO editCwbDAO;
	@Autowired
	private AccountCwbDetailDAO accountCwbDetailDAO;
	@Autowired
	AccountCwbSummaryDAO accountCwbSummaryDAO;
	@Autowired
	AccountCwbDetailService accountCwbDetailService;
	@Autowired
	AccountDeducDetailDAO accountDeducDetailDAO;
	@Autowired
	AccountDeducDetailService accountDeducDetailService;
	@Autowired
	AccountDeductRecordDAO accountDeductRecordDAO;
	@Autowired
	AccountDeductRecordService accountDeductRecordService;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	ExceptionCwbDAO exceptionDAO;
	@Autowired
	ReturnCwbsDAO returnCwbsDAO;
	@Autowired
	FnOrgOrderAdjustRecordDAO fnOrgOrderAdjustRecordDAO;
	
	@Autowired
	OrderBackCheckDAO orderBackCheckDao;
	
	@Autowired
	CwbDAO cwbDao;

	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	ThirdPartyOrder2DOCfgService thirdPartyOrder2DOCfgService;
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;
	
	/**
	 * 修改订单 之 重置审核状态
	 *
	 * @param editUser
	 *            修改人
	 * @param requestUser
	 *            申请人
	 * @param cwbs
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public EdtiCwb_DeliveryStateDetail analysisAndSaveByChongZhiShenHe(String cwb, Long requestUser, User editUser) {
		this.logger.info("EditCwb_SQL:{}重置审核状态 开始", cwb);
		// 根据cwb 获得 订单表 express_ops_cwb_detail 有效记录 state lock
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		// 根据 反馈表 中的gcaid 获得 归班表 express_ops_goto_class_auditing 记录 lock
		// 根据 归班表中的payupid 获得 交款表 express_ops_pay_up 记录 lock
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		
		if (co.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue()) {
			throw new ExplinkException(co.getFlowordertype() + "_当前订单状态已经不是[已审核]状态！");
		}

		/*
		 * List<AccountCwbFareDetail> accountCwbFareDetailList =
		 * accountCwbFareDetailDAO.getAccountCwbFareDetailByCwb(cwb); if
		 * (co.getInfactfare().compareTo(BigDecimal.ZERO) > 0 &&
		 * accountCwbFareDetailList.size() > 0 &&
		 * accountCwbFareDetailList.get(0).getFareid() > 0) { throw new
		 * ExplinkException("当前订单运费已交款，不可重置审核状态！"); }
		 */
		
		//  Hps_Concerto add 2016年6月13日 10:33:46
		//校验该订单是否存在晚于最后一条归班审核时间的退货出站审核通过记录
		DeliveryState ds1 = this.deliveryStateDAO.getDelivertStateYishenheCountByCwb(cwb);
		if(ds1 != null){
			List<OrderBackCheck> obc = this.orderBackCheckDao.getOrderBackCheckByCheckstateAndYiShenhe(cwb);
			if(obc!=null&&obc.size()>0){
				if (!this.compareDate1(obc, ds1.getAuditingtime())) {
					throw new ExplinkException(cwb+":该订单存在晚于最后一条归班审核时间的退货出站记录[异常标记]");
					//这个异常标记 是为了让那边捕获到从而 弹出的提示 不再是 : "审核未通过 当前订单或已不再是审核状态"
				}
			}
		}
		

		DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwbLock(cwb);
		//立个flag
		boolean flag = false;
		if(ds!=null){
			if(ds.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()){
				flag = true;
			}
		}
		GotoClassAuditing gca = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(ds.getGcaid());
		PayUp payUp = null;
		if (gca.getPayupid() > 0) {
			payUp = this.payUpDAO.getPayUpByIdLock(gca.getPayupid());
		}

		// 根据反馈表中的 deliveryid 获得 express_ops_user表 对应的小件员详细信息 用于更改小件员帐户 lock
		User u = this.userDAO.getUserByUseridLock(ds.getDeliveryid());

		// 根据 反馈表的cwb 获取 finance_audit_temp 结算审核明细表中 按配送结果结算的明细记录 中的审核id
		// 根据 结算审核明细表中的记录 中的 auditid 获取finance_audit 结算审核表 记录 lock
		Long financeAuditId = this.financeAuditDAO.getFinanceAuditTempByCwb(cwb, FinanceAuditTypeEnum.AnPeiSongJieGuo);
		FinanceAudit financeAudit = null;
		if (financeAuditId > 0) {// 如果存在按配送结果结算和按配送结果结算的
			financeAudit = this.financeAuditDAO.getFinanceAuditByIdLock(financeAuditId);
		}

		// 根据 归班表 payupid 获得 finance_pay_up_audit 站点交款审核表 记录 lock
		FinancePayUpAudit financePayUpAudit = null;
		if ((payUp != null) && (payUp.getAuditid() > 0)) {
			financePayUpAudit = this.financePayUpAuditDAO.getFinancePayUpAuditByIdLock(payUp.getAuditid());
		}
		// 整理 更改对象 EdtiCwb_DeliveryStateDetail
		EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
		ec_dsd.setDs(ds);
		ec_dsd.setDsid(ds.getId());
		ec_dsd.setEditcwbtypeid(EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue());
		ec_dsd.setRequestUser(requestUser);
		ec_dsd.setEditUser(editUser.getUserid());

		ec_dsd.getDs().setGcaid(gca.getId());
		ec_dsd.setFinance_audit_id(financeAuditId);
		ec_dsd.getDs().setPayupid(0);
		ec_dsd.setF_payup_audit_id(0);
		ec_dsd.setFd_payup_detail_id(0);
		if (payUp != null) {
			ec_dsd.getDs().setPayupid(payUp.getId());
		}
		if (financePayUpAudit != null) {
			ec_dsd.setF_payup_audit_id(financePayUpAudit.getId());
		}
		// 重置审核状态必要的变更值的字段和值 start
		ec_dsd.setNew_receivedfee(BigDecimal.ZERO);
		ec_dsd.setNew_returnedfee(BigDecimal.ZERO);
		ec_dsd.setNew_pos(BigDecimal.ZERO);
		ec_dsd.setNew_flowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		ec_dsd.setNew_deliverystate(DeliveryStateEnum.WeiFanKui.getValue());
		// 重置审核状态必要的变更值的字段和值 end
		ec_dsd.setNew_businessfee(ds.getBusinessfee());
		ec_dsd.setNew_isout(ds.getIsout());
		ec_dsd.setCwbordertypeid(co.getCwbordertypeid());
		ec_dsd.setNew_cwbordertypeid(co.getCwbordertypeid());
		// 记录原实收运费
		ec_dsd.setOriInfactfare(co.getInfactfare());

		/*
		 * 修改订单表字段 express_ops_cwb_detail｛ 将 nextbranchid 改为对应货物所在站点的中转站id 将
		 * flowordertype 改为9 领货状态 将 currentbranchid 当前站点id 置为0 因为有些上门退 上门换 拒收
		 * 等订单审核后属于库存 此值会变为当前站点id 将 cwbstate 置为1 将订单状态变为配送状态 将 deliverystate 置为0
		 * 配送结果与反馈表一致 会根据反馈的状态而变更，而领货时是0 ｝
		 */
		// Long nextbranchid =
		// branchDAO.getBranchByBranchid(co.getDeliverybranchid()).getZhongzhuanid();
		Long nextbranchid = co.getNextbranchid();
		this.cwbDAO.updateForChongZhiShenHe(co.getOpscwbid(), nextbranchid, FlowOrderTypeEnum.FenZhanLingHuo, 0L, CwbStateEnum.PeiShong, DeliveryStateEnum.WeiFanKui, BigDecimal.ZERO);
		// 重置运单状态
		transCwbDetailDAO.updateDetailTranscwbstate(co.getCwb(), TransCwbStateEnum.PEISONG);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where nextbranchid=" + nextbranchid + " and flowordertype=" + FlowOrderTypeEnum.FenZhanLingHuo.getValue()
				+ " and currentbranchid=0 and cwbstate=" + CwbStateEnum.PeiShong.getValue() + " and deliverystate=" + DeliveryStateEnum.WeiFanKui.getValue() + " and opscwbid=" + co.getOpscwbid());

		this.exceptionDAO.editCwbofException(co, FlowOrderTypeEnum.GaiDan.getValue(), this.getSessionUser(), "重置审核状态");
		// 删除返单表
		this.returnCwbsDAO.deleteReturnCwbByCwb(cwb);


		/**
		 * 云订单重置审核状态
		 */
		try {
			JSONReslutUtil.getResultMessageChangeLog(this.omsUrl() + "/OMSChange/editcwb", "type=1&cwb=" + co.getCwb() + "&nextbranchid=" + nextbranchid + "&flowordertype="
					+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "&currentbranchid=0&deliverystate=" + DeliveryStateEnum.WeiFanKui.getValue()+ "&customerid=" +co.getCustomerid() , "POST");
		} catch (IOException e1) {
			logger.error("", e1);
			this.logger.info("云订单重置审核状态异常:" + co.getCwb());
		}

		/*
		 * 修改反馈表字段 express_ops_delivery_state ｛ 修改 deliverystate 为0 为反馈； 将
		 * cash、pos、otherfee、checkfee、receivedfee、returnedfee 清零； 将deliverytime
		 * 清空； 将gcaid 归班id 变为0 auditingtime 清空； 将receivedfeeuser 收款人 置为0；
		 * 将sign_typeid 变为未签收 0 sign_man 签收人清空 sign_time 清空 将issendcustomer
		 * 是否已推送供货商 状态变更为 0 为推送 将 pushtime 推送成功时间 置为"" ｝
		 */
		this.deliveryStateDAO.updateForChongZhiShenHe(ds.getId(), DeliveryStateEnum.WeiFanKui, BigDecimal.ZERO);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from  express_ops_delivery_state " + "where  cash=0 and pos=0 and otherfee=0 and checkfee=0 and receivedfee=0 and returnedfee=0"
				+ " and deliverytime='' and gcaid=0 and auditingtime='' and receivedfeeuser=0" + " and sign_typeid=0 and sign_man is null and sign_time is null and issendcustomer=0 and pushtime=''"
				+ " and deliverystate=" + DeliveryStateEnum.WeiFanKui.getValue() + " and id=" + ds.getId());
		/*
		 * 修改归班表金额与字段 express_ops_goto_class_auditing｛ 将 payupamount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee-returnedfee)后的值 将payupamount_pos 变更为 减去 反馈表中
		 * pos 后的值 归班表增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */

		SystemInstall usedeliverpay = null;
		try {// 获取 小件员交款 功能使用开关 如果不使用小件员交款，则不调用小件员帐户变动
			usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");

		} catch (Exception e) {
			this.logger.error(cwb + " 获取使用小件员交款功能异常，默认不使用小件员交款功能-重置审核状态");
		}
		FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
		if ((usedeliverpay != null) && usedeliverpay.getValue().equals("yes")) {
			/*
			 * 创建小件员帐户交易记录 （如果开启了小件员交款，并且小件员已经归班审核交款）
			 * finance_deliver_pay_up_detail｛｝
			 */
			BigDecimal payupamount = ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee());

			fdpud.setDeliverealuser(ds.getDeliveryid());
			fdpud.setPayupamount(payupamount.negate());
			fdpud.setDeliverpayupamount(BigDecimal.ZERO);
			fdpud.setDeliverAccount(u.getDeliverAccount());
			fdpud.setDeliverpayuparrearage(u.getDeliverAccount().add(payupamount));
			fdpud.setPayupamount_pos(ds.getPos().negate());
			fdpud.setDeliverpayupamount_pos(BigDecimal.ZERO);
			fdpud.setDeliverPosAccount(u.getDeliverPosAccount());
			fdpud.setDeliverpayuparrearage_pos(u.getDeliverPosAccount().add(ds.getPos()));
			fdpud.setGcaid(gca.getId());
			fdpud.setAudituserid(ds.getUserid()); // 设置操作反馈与归班的人为操作人
			fdpud.setCredate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			fdpud.setType(FinanceDeliverPayUpDetailTypeEnum.ChongZhiShenHe.getValue());
			fdpud.setRemark("申请[" + cwb + "]重置审核为[" + DeliveryStateEnum.getByValue((int) ds.getDeliverystate()).getText() + "]的订单为[领货]状态");
			this.logger.info(cwb + " 小件员交款产生交易：{}", fdpud.toString());
			ec_dsd.setFd_payup_detail_id(this.financeDeliverPayUpDetailDAO.insert(fdpud));
			/*
			 * 修改小件员帐户余额（如果开启了小件员交款，并且小件员已经归班审核交款） finance_pay_up_audit｛ 将
			 * deliverAccount 变更为 deliverAccount 加上 反馈表中
			 * (cash+otherfee+checkfee-returnedfee)后的值 将 deliverPosAccount 变更为
			 * deliverAccount 加上 反馈表中 pos 后的值 ｝
			 */
			this.userDAO.updateUserAmount(u.getUserid(), fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos());
			this.logger.info("EditCwb_SQL:" + cwb + " select * express_set_user where deliverAccount=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverPosAccount="
					+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and userid=" + u.getUserid());
		} else {// 没有使用小件员交款功能 那么要复制小件员交易后的余额为0元
			fdpud.setDeliverpayuparrearage(BigDecimal.ZERO);
			fdpud.setDeliverpayuparrearage_pos(BigDecimal.ZERO);
		}

		BigDecimal gca_payupamount = gca.getPayupamount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
		BigDecimal gca_payupamount_pos = gca.getPayupamount_pos().subtract(ds.getPos());
		this.gotoClassAuditingDAO.updateForChongZhiShenHe(gca.getId(), gca_payupamount, gca_payupamount_pos, fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos(),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_goto_class_auditing where  payupamount=" + gca_payupamount.doubleValue() + " and payupamount_pos="
				+ gca_payupamount_pos.doubleValue() + " and deliverpayuparrearage=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverpayuparrearage_pos="
				+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and id =" + gca.getId());

		/*
		 * 修改交款表 express_ops_pay_up｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee-returnedfee)后的值 将 amount_pos 变更为 减去 反馈表中 pos
		 * 后的值 交款表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (payUp != null) {
			BigDecimal payup_payupamount = payUp.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			BigDecimal payup_payupamount_pos = payUp.getAmountPos().subtract(ds.getPos());
			this.payUpDAO.updateForChongZhiShenHe(payUp.getId(), payup_payupamount, payup_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_pay_up where amount=" + payup_payupamount.doubleValue() + " and amountpos=" + payup_payupamount_pos.doubleValue()
					+ " and id =" + payUp.getId());
		}

		/*
		 * 修改站点交款审核表 finance_pay_up_audit｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee-returnedfee)后的值 将 amountpos 变更为 减去 反馈表中 pos
		 * 后的值 站点交款审核表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (financePayUpAudit != null) {
			BigDecimal fpua_payupamount = financePayUpAudit.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			BigDecimal fpua_payupamount_pos = financePayUpAudit.getAmountpos().subtract(ds.getPos());
			this.financePayUpAuditDAO.updateForChongZhiShenHe(financePayUpAudit.getId(), fpua_payupamount, fpua_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from finance_pay_up_audit where amount=" + fpua_payupamount.doubleValue() + " and amountpos=" + fpua_payupamount_pos.doubleValue()
					+ " and id =" + financePayUpAudit.getId());

			// 如果有站点交款审核记录，则一定有帐户变动，所以同时要修改站点欠款金额 由于站点的欠款字段是记录的欠款正数 也就是钱多少钱
			// 里面的数值就是多少 所以当有款项变动的时候 应该用当前欠款 减去货款
			Branch b = this.branchDAO.getBranchByBranchidLock(payUp.getBranchid());
			BigDecimal arrearagepayupaudit = b.getArrearagepayupaudit().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			BigDecimal posarrearagepayupaudit = b.getPosarrearagepayupaudit().subtract(ds.getPos());
			this.branchDAO.updateForChongZhiShenHe(b.getBranchid(), arrearagepayupaudit, posarrearagepayupaudit);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_set_branch where  arrearagepayupaudit=" + arrearagepayupaudit.doubleValue() + " and posarrearagepayupaudit="
					+ posarrearagepayupaudit.doubleValue() + " and branchid=" + b.getBranchid());
		}

		/*
		 * 修改结算审核表 finance_audit｛ 将 shouldpayamount 应付金额 变更为 减去 反馈表中
		 * (cash+pos+otherfee+checkfee-returnedfee)后的值
		 * 结算审核表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (financeAudit != null) {
			BigDecimal fa_amount = financeAudit.getShouldpayamount().subtract(co.getReceivablefee().subtract(co.getPaybackfee()));
			this.financeAuditDAO.updateForChongZhiShenHe(financeAudit.getId(), fa_amount, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from finance_audit where shouldpayamount=" + fa_amount + " and id =" + financeAudit.getId());
		}

		// 根据 cwb 与归班id 获得唯一的express_ops_deliver_cash 小件员工作量统计表对应的记录 并修改
		this.deliveryCashDAO.updateForChongZhiShenHe(cwb, gca.getId());
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_deliver_cash  where fankuitime='' and paybackfee=0" + " and receivableNoPosfee=0 and receivablePosfee=0 and deliverystate="
				+ DeliveryStateEnum.WeiFanKui.getValue() + " and guibantime=''" + " and gcaid=0 and cwb='" + cwb + "' and gcaid=" + gca.getId());

		/*
		 * 修改新结算业务——买单结算(重置审核状态) POS不会产生欠款 所以不考虑debetstate
		 * 查询ops_account_cwb_detail表 FlowOrderType为POS数据 1.如果POS退款没交款，就直接删
		 * 2.如果POS交款了，将已交款的订单poscash-pos金额，posnums-单数，合计金额+pos金额&&删除订单明细记录；
		 */
		List<AccountCwbDetail> list_md = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.Pos.getValue()));
		if ((list_md != null) && !list_md.isEmpty()) {
			for (AccountCwbDetail list : list_md) {
				if (list.getCheckoutstate() > 0) {// 已交款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
					BigDecimal poscash = o.getPoscash().subtract(list.getPaybackfee());
					o.setPoscash(poscash);
					o.setPosnums(o.getPosnums() - 1);
					o.setHjfee(o.getHjfee().add(list.getPaybackfee()));
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
				this.accountCwbDetailDAO.deleteAccountCwbDetailById(list.getAccountcwbid());
				this.logger.info("EditCwb_SQL:delete from ops_account_cwb_detail where accountcwbid =" + list.getAccountcwbid());
			}
		}

		/*
		 * 修改新结算业务——配送结果结算(重置审核状态) 查询ops_account_cwb_detail表
		 * FlowOrderType为GuiBanShenHe数据
		 */
		List<AccountCwbDetail> list_psjg = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()));
		if ((list_psjg != null) && !list_psjg.isEmpty()) {
			for (AccountCwbDetail list : list_psjg) {
				if (list.getDebetstate() > 0) {// 本次欠款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getDebetstate());
					o.setWjnums(o.getWjnums() - 1);// 本次欠款订单数
					o.setWjcash(o.getWjcash().subtract(list.getCash()));// 本次欠款现金
					o.setWjpos(o.getWjpos().subtract(list.getPos()));// 本次欠款pos
					o.setWjcheck(o.getWjcheck().subtract(list.getCheckfee()));// 本次欠款支票
					o.setWjother(o.getWjother().subtract(list.getOtherfee()));// 本次欠款其他
					o.setWjfee(o.getWjfee().subtract(list.getCash()).subtract(list.getPos()).subtract(list.getCheckfee()).subtract(list.getOtherfee()));// 本次欠款合计
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}

				if (list.getCheckoutstate() > 0) {// 已交款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() == 0)) {// 无欠款
						o.setTonums(o.getTonums() - 1);// 本次应交订单数
						o.setTocash(o.getTocash().subtract(list.getCash()));// 本次应交现金
						o.setTopos(o.getTopos().subtract(list.getPos()));// 本次应交pos
						o.setTocheck(o.getTocheck().subtract(list.getCheckfee()));// 本次应交支票
						o.setToother(o.getToother().subtract(list.getOtherfee()));// 本次应交其他
						o.setTofee(o.getTofee().subtract(list.getCash()).subtract(list.getPos()).subtract(list.getCheckfee()).subtract(list.getOtherfee()));// 本次应交合计
						o.setYjnums(o.getYjnums() - 1);// 本次实交订单数
						o.setYjcash(o.getYjcash().subtract(list.getCash()));// 本次实交现金
						o.setYjpos(o.getYjpos().subtract(list.getPos()));// 本次实交pos
						o.setYjcheck(o.getYjcheck().subtract(list.getCheckfee()));// 本次实交支票
						o.setYjother(o.getYjother().subtract(list.getOtherfee()));// 本次实交其他
						o.setYjfee(o.getYjfee().subtract(list.getCash()).subtract(list.getPos()).subtract(list.getCheckfee()).subtract(list.getOtherfee()));// 本次实交合计
					}

					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() > 0)) {// 有欠款
						BigDecimal qkcash = o.getQkcash().subtract(list.getCash()).subtract(list.getPos()).subtract(list.getCheckfee()).subtract(list.getOtherfee());// 欠款金额
						o.setQknums(o.getQknums() - 1);// 欠款订单数
						o.setQkcash(qkcash);
					}
					o.setHjfee(o.getHjfee().subtract(list.getCash()).subtract(list.getCheckfee()).subtract(list.getOtherfee()));// 合计支付金额
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
				this.accountCwbDetailDAO.deleteAccountCwbDetailById(list.getAccountcwbid());
				this.logger.info("EditCwb_SQL:delete from ops_account_cwb_detail where accountcwbid =" + list.getAccountcwbid());
			}
		}

		/*
		 * 修改新结算业务——扣款结算(重置审核状态) 查询ops_account_deduct_detail表
		 * FlowOrderType为POS数据 1.将已交款的订单汇总fee-pos金额；
		 * 2.更新站点余额、欠款、伪余额、伪欠款字段(进去返还的POS金额)； 3.删除订单明细记录；
		 */
		List<AccountDeducDetail> list_kk = this.accountDeducDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.Pos.getValue()));
		if ((list_kk != null) && !list_kk.isEmpty()) {
			for (AccountDeducDetail list : list_kk) {
				if (list.getRecordid() > 0) {// 已审核
					AccountDeductRecord o = this.accountDeductRecordDAO.getAccountDeductRecordByIdLock(list.getRecordid());
					o.setFee(o.getFee().subtract(list.getFee()));
					this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), o.getFee());
					// 事务锁 锁住当前站点
					Branch branch = this.branchDAO.getBranchByBranchidLock(list.getBranchid());
					BigDecimal credit = branch.getCredit();
					BigDecimal debt = branch.getDebt();// 欠款
					BigDecimal balance = branch.getBalance();// 余额
					BigDecimal debtvirt = branch.getDebtvirt();// 伪欠款
					BigDecimal balancevirt = branch.getBalancevirt();// 伪余额
					Map feeMap = new HashMap();
					Map feeMap1 = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, list.getFee());
					feeMap1 = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, list.getFee());
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

					balancevirt = new BigDecimal("".equals(feeMap1.get("balance").toString()) ? "0" : feeMap1.get("balance").toString());
					debtvirt = new BigDecimal("".equals(feeMap1.get("debt").toString()) ? "0" : feeMap1.get("debt").toString());

					this.branchDAO.updateForFeeAndVirt(list.getBranchid(), balance, debt, balancevirt, debtvirt);
				}
				this.accountDeducDetailDAO.deleteAccountDeducDetailById(list.getId());
				this.logger.info("EditCwb_SQL:delete from ops_account_deduct_detail where id =" + list.getId());
			}
		}

		this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
		this.editCwbDAO.creEditCwb(ec_dsd);
		// 清除运费记录表信息
		this.accountCwbFareDetailDAO.deleteAccountCwbFareDetailByCwb(cwb);
		//当审核为“拒收”的订单重置反馈状态成功时，应将该订单从站点退货出站审核列表删除。意思是不让该订单在退货出站审核列表中出现
		//话说我整了半天都不知道怎么让一个拒收的订单重置反馈状态的同时，还能让退货出站审核列表中有。
		//未审核的订单一次不可能出现两条吧。不过即使出现三条也要干掉。
		//flag 为true
		if(flag){
			int dcount = this.orderBackCheckDao.deleteOrderBackCheckAndWeishenheByCwb(cwb);
			this.logger.info("该订单的状态为拒收,所以从退货出战审核列表上干掉"+(dcount==0?",但是好像并没有记录":",干掉了"+dcount+"条"));
		}
		
		this.logger.info("EditCwb_SQL:{}重置审核状态 结束", cwb);

		return ec_dsd;
	}
	

	//比较订单是否存在晚于最后一条归班审核时间的退货出站审核通过记录
	private boolean compareDate1(List<OrderBackCheck> obcs,String date2){
		if(obcs!=null&&obcs.size()>0){
			for (OrderBackCheck c : obcs) {
				if(!this.compareDate(c.getAudittime(), date2)){
					return false;
				}
			}
		}
		return true;
	}
	//比较两个字符串时间。
	private boolean compareDate(String date1,String date2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);
			boolean flag = d1.before(d2);
			return flag;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public EdtiCwb_DeliveryStateDetail analysisAndSaveByKuaiDiYunFei(String cwb, String isDeliveryState, BigDecimal shouldfare, BigDecimal cash, BigDecimal pos, BigDecimal checkfee,
			BigDecimal otherfee, Long requestUser, Long editUser) {
		this.logger.info("EditCwb_JINE_SQL:{}修改快递运费 开始", cwb);
		// 根据cwb 获得 订单表 express_ops_cwb_detail 有效记录 state lock
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		
		//快递运费金额修改的时候用的是shouldfare，所以校验的时候也用主表的shouldfare，而不是Receivablefee ---刘武强 20160802
		if (co.getShouldfare().compareTo(shouldfare) == 0) {
			throw new ExplinkException(co.getFlowordertype() + "_[失败]快递运费金额没有任何变化，不需要更改数据！");
		}
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwbLock(cwb);
		// 判断是否需要影响到财务分别调用两个方法
		if (isDeliveryState.equals("no")) {
			if ((ds != null) && (ds.getDeliverystate() != DeliveryStateEnum.WeiFanKui.getValue()) && (ds.getDeliverystate() != DeliveryStateEnum.JuShou.getValue())
					&& (ds.getDeliverystate() != DeliveryStateEnum.FenZhanZhiLiu.getValue()) && (ds.getDeliverystate() != DeliveryStateEnum.ShangMenJuTui.getValue())) {
				throw new ExplinkException(co.getFlowordertype() + "_[失败]变更金额过程中操作了反馈为最终结果的操作，无法完成这次金额修改！");
			}
		}
		/*
		 * 根据cwb 修改订单表 express_ops_cwb_detail 的快递运费金额 修改 shouldfare
		 */
		this.cwbDAO.updateShouldfare(co.getOpscwbid(), shouldfare, (co.getTotalfee()).add(shouldfare.subtract(co.getShouldfare())).doubleValue());
		this.exceptionDAO.editCwbofException(co, FlowOrderTypeEnum.GaiDan.getValue(), this.getSessionUser(), "修改快递运费金额:运费金额为：" + shouldfare);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where shouldfare=" + shouldfare.doubleValue() + " and opscwbid=" + co.getOpscwbid());

		EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
		ec_dsd.setEditcwbtypeid(EditCwbTypeEnum.XiuGaiKaiDiYunFeiJinE.getValue());
		ec_dsd.setRequestUser(requestUser);
		ec_dsd.setEditUser(editUser);
		ec_dsd.setFinance_audit_id(0);
		ec_dsd.setF_payup_audit_id(0);
		ec_dsd.setFd_payup_detail_id(0);
		ec_dsd.setNew_shouldfare(shouldfare);
		ec_dsd.setCwbordertypeid(co.getCwbordertypeid());
		ec_dsd.setNew_pos(pos);
		ec_dsd.setNew_flowordertype(co.getFlowordertype());
		ec_dsd.setNew_deliverystate(DeliveryStateEnum.WeiFanKui.getValue());
		ec_dsd.setNew_cwbordertypeid(co.getCwbordertypeid());

		this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
		// this.editCwbDAO.creEditCwb(ec_dsd);
		this.logger.info("EditCwb_SQL:{}修改快递运费金额 结束", cwb);
		return ec_dsd;
	}

	/**
	 * 修改订单 修改金额
	 *
	 * @param cwb
	 *            要修改的订单号
	 * @param isDeliveryState
	 *            是否已反馈为最终结果影响账务
	 * @param receivablefee
	 *            代收金额
	 * @param cash
	 * @param pos
	 * @param checkfee
	 * @param otherfee
	 * @param paybackfee
	 *            代退金额
	 * @param editUser
	 *            修改人
	 * @param requestUser
	 *            申请人
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public EdtiCwb_DeliveryStateDetail analysisAndSaveByXiuGaiJinE(String cwb, String isDeliveryState, BigDecimal receivablefee, BigDecimal cash, BigDecimal pos, BigDecimal checkfee,
			BigDecimal otherfee, BigDecimal paybackfee, Long requestUser, Long editUser) {
		this.logger.info("EditCwb_JINE_SQL:{}修改金额 开始", cwb);
		// 根据cwb 获得 订单表 express_ops_cwb_detail 有效记录 state lock
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwbLock(cwb);
		// 判断是否需要影响到财务分别调用两个方法
		if (isDeliveryState.equals("no")) {
			if ((ds != null) && (ds.getDeliverystate() != DeliveryStateEnum.WeiFanKui.getValue()) && (ds.getDeliverystate() != DeliveryStateEnum.JuShou.getValue())
					&& (ds.getDeliverystate() != DeliveryStateEnum.FenZhanZhiLiu.getValue()) && (ds.getDeliverystate() != DeliveryStateEnum.ShangMenJuTui.getValue())) {
				throw new ExplinkException(co.getFlowordertype() + "_[失败]变更金额过程中操作了反馈为最终结果的操作，无法完成这次金额修改！");
			}
		}
		if ((co.getReceivablefee().compareTo(receivablefee) == 0) && (co.getPaybackfee().compareTo(paybackfee) == 0)) {
			throw new ExplinkException(co.getFlowordertype() + "_[失败]订单金额没有任何变化，不需要更改数据！");
		}

		/*
		 * 根据cwb 修改订单表 express_ops_cwb_detail 的金额 修改 receivablefee 代收金额 修改
		 * paybackfee 代退金额
		 */
		this.cwbDAO.updateXiuGaiJinE(co.getOpscwbid(), receivablefee, paybackfee);
		this.exceptionDAO.editCwbofException(co, FlowOrderTypeEnum.GaiDan.getValue(), this.getSessionUser(), "修改金额:应收金额为：" + receivablefee + "\t应退金额为：" + paybackfee);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where receivablefee=" + receivablefee.doubleValue() + " and paybackfee=" + paybackfee + " and opscwbid="
				+ co.getOpscwbid());

		/**
		 * 云订单修改订单金额
		 */
		try {
			JSONReslutUtil.getResultMessageChangeLog(this.omsUrl() + "/OMSChange/editcwb", "type=2&cwb=" + co.getCwb() + "&receivablefee=" + receivablefee + "&paybackfee=" + paybackfee, "POST");
		} catch (Exception e1) {
			logger.error("", e1);
			this.logger.info("云订单修改订单金额异常:" + co.getCwb());
		}

		/*
		 * 修改新结算业务——买单结算(修改订单金额) 未产生过交款记录的订单修改 receivablefee&&paybackfee
		 * 已产生过交款记录的订单： 1.修改汇总表中相对应的中转金额、退货金额、POS金额&&合计支付金额 2.根据欠款和结算ID
		 * 来更新相对应的本次应交、本次实交、本次欠款、欠款&&合计支付金额
		 */
		String ids = AccountFlowOrderTypeEnum.KuFangChuKu.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue() + ","
				+ AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue() + ","
				+ AccountFlowOrderTypeEnum.Pos.getValue();
		List<AccountCwbDetail> list_md = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, ids);
		if ((list_md != null) && !list_md.isEmpty()) {
			for (AccountCwbDetail list : list_md) {
				if (list.getDebetstate() > 0) {// 本次欠款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getDebetstate());
					o.setWjcash(o.getWjcash().subtract(list.getReceivablefee().subtract(receivablefee)));// 本次欠款
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
				if (list.getCheckoutstate() > 0) {// 已交款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
					// 出库
					if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.KuFangChuKu.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue())
							|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue())) {
						if ((list.getCheckoutstate() > 0) && (list.getDebetstate() == 0)) {// 无欠款
							o.setTocash(o.getTocash().subtract(list.getReceivablefee().subtract(receivablefee)));// 本次应交
							o.setYjcash(o.getYjcash().subtract(list.getReceivablefee().subtract(receivablefee)));// 本次实交
						}
						if ((list.getCheckoutstate() > 0) && (list.getDebetstate() > 0)) {// 有欠款
							o.setQkcash(o.getQkcash().subtract(list.getReceivablefee().subtract(receivablefee)));// 欠款
						}
						o.setHjfee(o.getHjfee().subtract(list.getReceivablefee().subtract(receivablefee)));// 合计支付
					} else {// 中转
							// 不同类型订单取不同的金额字段
						BigDecimal ZZfee = this.accountCwbDetailService.getZZPaybackfee(list.getCwbordertypeid(), list.getDeliverystate(), receivablefee, paybackfee);
						// 中转退款
						if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())) {
							o.setZzcash(o.getZzcash().subtract(list.getPaybackfee().subtract(ZZfee)));// 中转退款=中转退款+原paybackfee-新paybackfee
																										// (新paybackfee根据不同订单类型取值不同)
						}
						// 退货退款
						if (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue()) {
							// 不同类型订单取不同的金额字段
							o.setThcash(o.getThcash().subtract(list.getPaybackfee().subtract(ZZfee)));// 退货退款=退货退款+原paybackfee-新paybackfee
																										// (新paybackfee根据不同订单类型取值不同)
						}
						o.setHjfee(o.getHjfee().add(list.getPaybackfee().subtract(ZZfee)));// 合计支付

						// POS退款
						if (list.getFlowordertype() == AccountFlowOrderTypeEnum.Pos.getValue()) {
							if (pos.compareTo(new BigDecimal("0")) > 0) {
								o.setPoscash(o.getPoscash().subtract(list.getPaybackfee().subtract(pos)));// 退货退款=退货退款+原paybackfee-新paybackfee
																											// (新paybackfee根据不同订单类型取值不同)
								o.setHjfee(o.getHjfee().add(list.getPaybackfee().subtract(pos)));// 合计支付
							}
						}
					}
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}

				// 修改订单明细字段
				if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.KuFangChuKu.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue())
						|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue())) {
					this.accountCwbDetailDAO.updateXiuGaiJinE(list.getAccountcwbid(), receivablefee, paybackfee);
					this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set receivablefee=" + receivablefee + ",paybackfee=" + paybackfee + " where accountcwbid=" + list.getAccountcwbid());
				} else {
					this.accountCwbDetailDAO.updateXiuGaiJinE(list.getAccountcwbid(), receivablefee,
							this.accountCwbDetailService.getZZPaybackfee(list.getCwbordertypeid(), list.getDeliverystate(), receivablefee, paybackfee));
					this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set receivablefee=" + receivablefee + ",paybackfee="
							+ this.accountCwbDetailService.getZZPaybackfee(list.getCwbordertypeid(), list.getDeliverystate(), receivablefee, paybackfee) + " where accountcwbid="
							+ list.getAccountcwbid());
				}
			}

		}

		/*
		 * 修改新结算业务——配送结果结算(修改订单金额)
		 * 未产生过交款记录的订单修改receivablefee,paybackfee,cash,pos,checkfee,otherfee
		 * 已产生过交款记录的订单： 1.修改汇总表中相对应的cash,pos,checkfee,otherfee合计支付金额 2.根据欠款和结算ID
		 * 来更新相对应的本次应交、本次实交、本次欠款、欠款&&合计支付金额
		 */
		List<AccountCwbDetail> list_psjg = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()));
		if ((list_psjg != null) && !list_psjg.isEmpty()) {
			for (AccountCwbDetail list : list_psjg) {
				if (list.getDebetstate() > 0) {// 本次欠款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getDebetstate());
					o.setWjcash(o.getWjcash().subtract(list.getCash().subtract(cash)));// 本次欠款现金
					o.setWjpos(o.getWjpos().subtract(list.getPos().subtract(pos)));// 本次欠款pos
					o.setWjcheck(o.getWjcheck().subtract(list.getCheckfee().subtract(checkfee)));// 本次欠款支票
					o.setWjother(o.getWjother().subtract(list.getOtherfee().subtract(otherfee)));// 本次欠款其他
					o.setWjfee(o.getWjcash().add(o.getWjpos()).add(o.getWjcheck()).add(o.getWjother()));// 本次欠款合计
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
				if (list.getCheckoutstate() > 0) {// 已交款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
					BigDecimal newcash = list.getCash().subtract(cash);
					BigDecimal newpos = list.getPos().subtract(pos);
					BigDecimal newcheck = list.getCheckfee().subtract(checkfee);
					BigDecimal newother = list.getOtherfee().subtract(otherfee);
					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() == 0)) {// 无欠款
						o.setTocash(o.getTocash().subtract(newcash));// 本次应交现金
						o.setTopos(o.getTopos().subtract(newpos));// 本次应交pos
						o.setTocheck(o.getTocheck().subtract(newcheck));// 本次应交支票
						o.setToother(o.getToother().subtract(list.getOtherfee().subtract(otherfee)));// 本次应交其他
						o.setTofee(o.getTocash().add(o.getTopos()).add(o.getTocheck()).add(o.getToother()));// 本次应交合计

						o.setYjcash(o.getYjcash().subtract(newcash));// 本次实交现金
						o.setYjpos(o.getYjpos().subtract(newpos));// 本次实交pos
						o.setYjcheck(o.getYjcheck().subtract(newcheck));// 本次实交支票
						o.setYjother(o.getYjother().subtract(newother));// 本次实交其他
						o.setYjfee(o.getYjcash().add(o.getYjpos()).add(o.getYjcheck()).add(o.getYjother()));
					}

					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() > 0)) {// 有欠款
						o.setQkcash(o.getQkcash().subtract(newcash.add(newcheck).add(newother)));// 欠款
					}
					o.setHjfee(o.getHjfee().subtract(newcash.add(newcheck).add(newother)));// 合计支付
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
			}
			this.accountCwbDetailDAO.updateXiuGaiJinEByCwb(cwb, receivablefee, paybackfee, cash, pos, checkfee, otherfee);
			this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set receivablefee=" + receivablefee + ",paybackfee=" + paybackfee + " cash=" + cash + ",pos=" + pos + ",checkfee=" + checkfee
					+ ",otherfee=" + otherfee + " where cwb=" + cwb);
		}

		/*
		 * 修改新结算业务——扣款结算(修改订单金额) 查询ops_account_deduct_detail表
		 * FlowOrderType为POS数据 1.将已交款的订单汇总fee-pos金额；
		 * 2.更新站点余额、欠款、伪余额、伪欠款字段(进去返还的POS金额)； 3.删除订单明细记录；
		 */
		List<AccountDeducDetail> list_kk = this.accountDeducDetailDAO.getDetailByCwb(cwb);
		if ((list_kk != null) && !list_kk.isEmpty()) {
			// 根据不同订单类型 取得应扣款金额
			// BigDecimal
			// newfee=accountDeductRecordService.getDetailFee(co.getCwbordertypeid(),receivablefee,paybackfee);
			BigDecimal newfee = this.accountDeducDetailService.getTHPaybackfee(co.getCwbordertypeid(), co.getDeliverystate(), receivablefee, paybackfee);

			for (AccountDeducDetail list : list_kk) {
				// 锁住该站点记录
				Branch branchLock = this.branchDAO.getBranchByBranchidLock(list.getBranchid());
				BigDecimal oldfee = list.getFee();// 加减款金额
				BigDecimal credit = branchLock.getCredit();// 信用额度
				BigDecimal balance = branchLock.getBalance();// 余额
				BigDecimal debt = branchLock.getDebt();// 欠款
				BigDecimal debtvirt = branchLock.getDebtvirt();// 伪欠款
				BigDecimal balancevirt = branchLock.getBalancevirt();// 伪余额

				if (list.getRecordid() > 0) {// 已结算
					AccountDeductRecord o = this.accountDeductRecordDAO.getAccountDeductRecordByIdLock(list.getRecordid());

					// 冲正
					if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuan.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())
							|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuo.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.Pos.getValue())) {
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====减款逻辑=====减款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, oldfee.subtract(newfee));
						} else {
							// ====加款逻辑=====加款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.addBranchFee(balance, debt, newfee.subtract(oldfee));
						}
						balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 的余额、欠款
						this.branchDAO.updateForFee(list.getBranchid(), balance, debt);
						this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), o.getFee().subtract(oldfee.subtract(newfee)));
					} else {// 出库
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====加款逻辑=====加款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.addBranchFee(balance, debt, oldfee.subtract(newfee));
						} else {
							// ====减款逻辑=====减款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, newfee.subtract(oldfee));
						}
						balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 的余额、欠款
						this.branchDAO.updateForFee(list.getBranchid(), balance, debt);
						this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), newfee);
					}
				}

				if (list.getRecordidvirt() > 0) {// 已伪结算
					// 冲正
					if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuan.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())
							|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuo.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.Pos.getValue())) {
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====减款逻辑=====减款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, oldfee.subtract(newfee));
						} else {
							// ====加款逻辑=====加款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.addBranchFee(balancevirt, debtvirt, newfee.subtract(oldfee));
						}
						balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 伪余额、伪欠款
						this.branchDAO.updateForVirt(list.getBranchid(), balancevirt, debtvirt);
					} else {// 出库
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====加款逻辑=====加款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.addBranchFee(balance, debt, oldfee.subtract(newfee));
						} else {
							// ====减款逻辑=====减款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, newfee.subtract(oldfee));
						}
						balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 伪余额、伪欠款
						this.branchDAO.updateForVirt(list.getBranchid(), balancevirt, debtvirt);
					}
				}
			}
			this.accountDeducDetailDAO.updateXiuGaiJinE(cwb, newfee);
		}

		/*
		 * 根据cwb修改express_ops_delivery_state 的 实收金额与实退金额 以及修改应处理金额和 收款退款状态
		 * 实收金额receivedfee与实退金额returnedfee只能存在一个
		 * 如果实收金额receivedfee大于0，那么更新receivedfee，并且isout变更为 0 应收
		 * 如果实退金额returnedfee大于0，那么更新returnedfee，并且isout变更为 1 应退 应处理金额businessfee
		 * 等同与 receivedfee + receivedfee 现金 pos 支票 和其他 同时变更，如果是没有反馈为最终配送结果 默认都为0
		 */
		Long isOut = 0L;
		if (receivablefee.compareTo(paybackfee) < 0) {
			isOut = 1L;
		}

		// 整理 更改对象 EdtiCwb_DeliveryStateDetail
		EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
		ec_dsd.setEditcwbtypeid(EditCwbTypeEnum.XiuGaiJinE.getValue());
		ec_dsd.setRequestUser(requestUser);
		ec_dsd.setEditUser(editUser);
		ec_dsd.setFinance_audit_id(0);
		ec_dsd.setF_payup_audit_id(0);
		ec_dsd.setFd_payup_detail_id(0);
		ec_dsd.setNew_receivedfee(receivablefee);
		ec_dsd.setNew_returnedfee(paybackfee);
		ec_dsd.setCwbordertypeid(co.getCwbordertypeid());
		ec_dsd.setNew_pos(pos);
		ec_dsd.setNew_flowordertype(co.getFlowordertype());
		ec_dsd.setNew_deliverystate(DeliveryStateEnum.WeiFanKui.getValue());
		ec_dsd.setNew_cwbordertypeid(co.getCwbordertypeid());

		if (ds != null) {
			// 如果这个订单已经领货 未反馈 或者反馈为 滞留 拒收 上门退拒退，那么 不更改实收金额
			if ((ds.getDeliverystate() == DeliveryStateEnum.WeiFanKui.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.JuShou.getValue())
					|| (ds.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue())) {
				this.deliveryStateDAO.updateXiuGaiJinE(ds.getId(), BigDecimal.ZERO, BigDecimal.ZERO, receivablefee.add(paybackfee), cash, pos, checkfee, otherfee, isOut);
				this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_delivery_state where receivedfee=" + BigDecimal.ZERO.doubleValue() + " and returnedfee="
						+ BigDecimal.ZERO.doubleValue() + " and businessfee=" + receivablefee.add(paybackfee).doubleValue() + " and isout=" + isOut + " and cash=" + cash.doubleValue() + " and pos="
						+ pos.doubleValue() + " and otherfee=" + otherfee.doubleValue() + " and checkfee=" + checkfee.doubleValue() + " and id=" + ds.getId());

			} else {
				this.deliveryStateDAO.updateXiuGaiJinE(ds.getId(), receivablefee, paybackfee, receivablefee.add(paybackfee), cash, pos, checkfee, otherfee, isOut);
				this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_delivery_state where receivedfee=" + receivablefee.doubleValue() + " and returnedfee=" + paybackfee.doubleValue()
						+ " and businessfee=" + receivablefee.add(paybackfee).doubleValue() + " and isout=" + isOut + " and cash=" + cash.doubleValue() + " and pos=" + pos.doubleValue()
						+ " and otherfee=" + otherfee.doubleValue() + " and checkfee=" + checkfee.doubleValue() + " and id=" + ds.getId());
			}
			ec_dsd.setDs(ds);
			ec_dsd.setDsid(ds.getId());
			ec_dsd.setNew_businessfee(receivablefee.add(paybackfee));
			ec_dsd.setNew_isout(isOut);

		} else {
			ec_dsd.setDs(new DeliveryState());
			ec_dsd.getDs().setCwb(cwb);
			ec_dsd.setNew_businessfee(receivablefee.add(paybackfee));
			ec_dsd.setNew_isout(isOut);
		}

		if (isDeliveryState.equals("no")) {// 如果订单没有反馈最终配送结果，那么到这里就结束了，因为这次修改不影响其他账务
			this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
			this.editCwbDAO.creEditCwb(ec_dsd);
			this.logger.info("EditCwb_SQL:{}修改订单金额 结束", cwb);
			return ec_dsd;
		}

		// 根据 反馈表 中的gcaid 获得 归班表 express_ops_goto_class_auditing 记录 lock
		// 根据 归班表中的payupid 获得 交款表 express_ops_pay_up 记录 lock
		GotoClassAuditing gca = null;
		if ((ds != null) && (ds.getGcaid() > 0)) {
			gca = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(ds.getGcaid());
			ec_dsd.getDs().setGcaid(gca.getId());
		}
		PayUp payUp = null;
		if ((gca != null) && (gca.getPayupid() > 0)) {
			payUp = this.payUpDAO.getPayUpByIdLock(gca.getPayupid());
			ec_dsd.getDs().setPayupid(payUp.getId());
		}

		// 根据反馈表中的 deliveryid 获得 express_ops_user表 对应的小件员详细信息 用于更改小件员帐户 lock
		User u = this.userDAO.getUserByUseridLock(ds.getDeliveryid());

		// 根据 反馈表的cwb 获取 finance_audit_temp 结算审核明细表中 按配送结果结算的明细记录 中的审核id
		// 根据 结算审核明细表中的记录 中的 auditid 获取finance_audit 结算审核表 记录 lock
		Long financeAuditId = this.financeAuditDAO.getFinanceAuditTempByCwb(cwb);
		FinanceAudit financeAudit = null;
		if (financeAuditId > 0) {// 如果存在按配送结果结算和按配送结果结算的
			financeAudit = this.financeAuditDAO.getFinanceAuditByIdLock(financeAuditId);
			ec_dsd.setFinance_audit_id(financeAudit.getId());
		}

		// 根据 归班表 payupid 获得 finance_pay_up_audit 站点交款审核表 记录 lock
		FinancePayUpAudit financePayUpAudit = null;
		if ((payUp != null) && (payUp.getAuditid() > 0)) {
			financePayUpAudit = this.financePayUpAuditDAO.getFinancePayUpAuditByIdLock(payUp.getAuditid());
			ec_dsd.setF_payup_audit_id(financePayUpAudit.getId());
		}

		/*
		 * 修改归班表金额与字段 如果订单已经归班 express_ops_goto_class_auditing｛ 将 payupamount
		 * 变更为 减去 反馈表中 (cash+otherfee+checkfee-returnedfee)后的值 再加上传入的
		 * (cash+otherfee+checkfee-paybackfee) 将payupamount_pos 变更为 减去 反馈表中 pos
		 * 后的值 再加上 传入的pos的值 归班表增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (gca != null) {

			SystemInstall usedeliverpay = null;
			try {// 获取 小件员交款 功能使用开关 如果不使用小件员交款，则不调用小件员帐户变动
				usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");

			} catch (Exception e) {
				this.logger.error(cwb + " 获取使用小件员交款功能异常，默认不使用小件员交款功能-修改订单金额");
			}
			FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
			if ((usedeliverpay != null) && usedeliverpay.getValue().equals("yes")) {
				/*
				 * 创建小件员帐户交易记录 （如果开启了小件员交款，并且小件员已经归班审核交款）
				 * finance_deliver_pay_up_detail｛｝
				 */
				fdpud.setDeliverealuser(ds.getDeliveryid());

				BigDecimal payupamount = cash.add(otherfee).add(checkfee).subtract(paybackfee);
				BigDecimal deliverpayupamount = ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee());

				fdpud.setPayupamount(payupamount);
				fdpud.setDeliverpayupamount(deliverpayupamount);
				fdpud.setDeliverAccount(u.getDeliverAccount());
				// 小件员帐户 先减去更改后的金额 因为 货款变更成了更改后的金额 然后加上 之前的订单金额 因为 之前的订单金额已经付过款了
				fdpud.setDeliverpayuparrearage(u.getDeliverAccount().subtract(payupamount).add(deliverpayupamount));

				fdpud.setPayupamount_pos(pos);
				fdpud.setDeliverpayupamount_pos(ds.getPos());
				fdpud.setDeliverPosAccount(u.getDeliverPosAccount());
				// 小件员帐户 先减去更改后的金额 因为 货款变更成了更改后的金额 然后加上 之前的订单金额 因为 之前的订单金额已经付过款了
				fdpud.setDeliverpayuparrearage_pos(u.getDeliverPosAccount().subtract(pos).add(ds.getPos()));

				fdpud.setGcaid(gca.getId());
				fdpud.setAudituserid(ds.getUserid()); // 设置操作反馈与归班的人为操作人
				fdpud.setCredate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				fdpud.setType(FinanceDeliverPayUpDetailTypeEnum.XiuGaiJinE.getValue());
				fdpud.setRemark("申请[" + cwb + "]代收金额从[" + co.getReceivablefee().doubleValue() + "]改为[" + receivablefee.doubleValue() + "],代退金额从[" + co.getPaybackfee().doubleValue() + "]改为["
						+ paybackfee.doubleValue() + "]");
				this.logger.info("EditCwb_fdpud:" + cwb + " {}", fdpud.toString());
				ec_dsd.setFd_payup_detail_id(this.financeDeliverPayUpDetailDAO.insert(fdpud));
				/*
				 * 修改小件员帐户余额（如果开启了小件员交款，并且小件员已经归班审核交款） finance_pay_up_audit｛ 将
				 * deliverAccount 变更为 fdpud.setDeliverpayuparrearage 将
				 * deliverPosAccount 变更为 fdpud.setDeliverpayuparrearage_pos ｝
				 */
				this.userDAO.updateUserAmount(u.getUserid(), fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos());
				this.logger.info("EditCwb_SQL:" + cwb + " select * express_set_user where deliverAccount=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverPosAccount="
						+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and userid=" + u.getUserid());
			} else {// 没有使用小件员交款功能 那么要复值小件员交易后的余额为0元
				fdpud.setDeliverpayuparrearage(BigDecimal.ZERO);
				fdpud.setDeliverpayuparrearage_pos(BigDecimal.ZERO);
			}
			BigDecimal gca_payupamount = gca.getPayupamount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			gca_payupamount = gca_payupamount.add(cash.add(otherfee).add(checkfee).subtract(paybackfee));
			BigDecimal gca_payupamount_pos = gca.getPayupamount_pos().subtract(ds.getPos());
			gca_payupamount_pos = gca_payupamount_pos.add(pos);

			this.gotoClassAuditingDAO.updateForChongZhiShenHe(gca.getId(), gca_payupamount, gca_payupamount_pos, fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos(),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_goto_class_auditing where  payupamount=" + gca_payupamount.doubleValue() + " and payupamount_pos="
					+ gca_payupamount_pos.doubleValue() + " and deliverpayuparrearage=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverpayuparrearage_pos="
					+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and id =" + gca.getId());

		}
		/*
		 * 修改交款表 express_ops_pay_up｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee-returnedfee)后的值 再加上传入的
		 * (cash+otherfee+checkfee-paybackfee) 将 amount_pos 变更为 减去 反馈表中 pos 后的值
		 * 再加上传入的 pos 交款表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (payUp != null) {
			BigDecimal payup_payupamount = payUp.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			payup_payupamount = payup_payupamount.add(cash.add(otherfee).add(checkfee).subtract(paybackfee));
			BigDecimal payup_payupamount_pos = payUp.getAmountPos().subtract(ds.getPos());
			payup_payupamount_pos = payup_payupamount_pos.add(pos);

			this.payUpDAO.updateForChongZhiShenHe(payUp.getId(), payup_payupamount, payup_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_pay_up where amount=" + payup_payupamount.doubleValue() + " and amountpos=" + payup_payupamount_pos.doubleValue()
					+ " and id =" + payUp.getId());
		}

		/*
		 * 修改站点交款审核表 finance_pay_up_audit｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee-returnedfee)后的值 再加上传入的
		 * (cash+otherfee+checkfee-paybackfee) 将 amountpos 变更为 减去 反馈表中 pos 后的值
		 * 再加上传入的 pos 站点交款审核表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (financePayUpAudit != null) {
			BigDecimal fpua_payupamount = financePayUpAudit.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			fpua_payupamount = fpua_payupamount.add(cash.add(otherfee).add(checkfee).subtract(paybackfee));
			BigDecimal fpua_payupamount_pos = financePayUpAudit.getAmountpos().subtract(ds.getPos());
			fpua_payupamount_pos = fpua_payupamount_pos.add(pos);

			this.financePayUpAuditDAO.updateForChongZhiShenHe(financePayUpAudit.getId(), fpua_payupamount, fpua_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from finance_pay_up_audit where amount=" + fpua_payupamount.doubleValue() + " and amountpos=" + fpua_payupamount_pos.doubleValue()
					+ " and id =" + financePayUpAudit.getId());

			// 如果有站点交款审核记录，则一定有帐户变动，所以同时要修改站点欠款金额 由于站点的欠款字段是记录的欠款正数 也就是钱多少钱
			// 里面的数值就是多少 所以当有款项变动的时候 应该用当前欠款 减去货款
			Branch b = this.branchDAO.getBranchByBranchidLock(payUp.getBranchid());
			BigDecimal arrearagepayupaudit = b.getArrearagepayupaudit().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()).subtract(ds.getReturnedfee()));
			arrearagepayupaudit = arrearagepayupaudit.add(cash.add(otherfee).add(checkfee).subtract(paybackfee));
			BigDecimal posarrearagepayupaudit = b.getPosarrearagepayupaudit().subtract(ds.getPos());
			posarrearagepayupaudit = posarrearagepayupaudit.add(pos);

			this.branchDAO.updateForChongZhiShenHe(b.getBranchid(), arrearagepayupaudit, posarrearagepayupaudit);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_set_branch where  arrearagepayupaudit=" + arrearagepayupaudit.doubleValue() + " and posarrearagepayupaudit="
					+ posarrearagepayupaudit.doubleValue() + " and branchid=" + b.getBranchid());
		}

		/*
		 * 修改结算审核表 finance_audit｛ 将 shouldpayamount 应付金额 变更为 减去 反馈表中
		 * (cash+pos+otherfee+checkfee-returnedfee)后的值 再加上传入的
		 * (cash+pos+otherfee+checkfee-paybackfee)
		 * 结算审核表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (financeAudit != null) {
			BigDecimal fa_amount = financeAudit.getShouldpayamount().subtract(co.getReceivablefee().subtract(co.getPaybackfee()));
			fa_amount = fa_amount.add(receivablefee.subtract(paybackfee));
			this.financeAuditDAO.updateForChongZhiShenHe(financeAudit.getId(), fa_amount, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from finance_audit where shouldpayamount=" + fa_amount + " and id =" + financeAudit.getId());
		}

		// 根据 cwb 与归班id 获得唯一的express_ops_deliver_cash 小件员工作量统计表对应的记录 并修改
		this.deliveryCashDAO.updateXiuGaiJinE(cwb, ds.getDeliverystate(), receivablefee, pos, paybackfee);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_deliver_cash where paybackfee=" + paybackfee.doubleValue() + " and receivableNoPosfee="
				+ receivablefee.subtract(pos).doubleValue() + " and receivablePosfee=" + pos.doubleValue() + " and cwb={} and deliverystate={} and deliverystate>0", cwb, ds.getDeliverystate());

		this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
		this.editCwbDAO.creEditCwb(ec_dsd);
		this.logger.info("EditCwb_SQL:{}修改订单金额 结束", cwb);
		return ec_dsd;
	}

	/**
	 * 修改订单支付方式
	 *
	 * @param cwb
	 * @param paywayid
	 *            订单的当前支付方式
	 * @param newpaywayid
	 *            订单要修改为的支付方式
	 * @param editUser
	 *            修改人
	 * @param requestUser
	 *            申请人
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public EdtiCwb_DeliveryStateDetail analysisAndSaveByXiuGaiZhiFuFangShi(String cwb, int paywayid, int newpaywayid, Long requestUser, Long editUser) {
		this.logger.info("EditCwb_SQL:{}修改订单支付方式 开始", cwb);
		// 根据cwb 获得 订单表 express_ops_cwb_detail 有效记录 state lock
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		// 根据 反馈表 中的gcaid 获得 归班表 express_ops_goto_class_auditing 记录 lock
		// 根据 归班表中的payupid 获得 交款表 express_ops_pay_up 记录 lock
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (paywayid == newpaywayid) {
			throw new ExplinkException(PaytypeEnum.getByValue(paywayid).getText() + "_当前订单状态已经是[" + PaytypeEnum.getByValue(newpaywayid).getText() + "]状态！");
		}
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwbLock(cwb);
		if (ds == null) {
			throw new ExplinkException(PaytypeEnum.getByValue(paywayid).getText() + "_当前订单还没有反馈！");
		} else if ((ds.getDeliverystate() != DeliveryStateEnum.PeiSongChengGong.getValue()) && (ds.getDeliverystate() != DeliveryStateEnum.ShangMenHuanChengGong.getValue())
				&& (ds.getDeliverystate() != DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			throw new ExplinkException(PaytypeEnum.getByValue(paywayid).getText() + "_当前订单反馈为[" + DeliveryStateEnum.getByValue((int) ds.getDeliverystate()).getText() + "]无需修改支付方式！");
		} else if ((paywayid == PaytypeEnum.Xianjin.getValue()) && (ds.getCash().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ExplinkException("现金_当前订单支付方式从[现金]修改为[" + PaytypeEnum.getByValue(newpaywayid) + "],但现金金额却为 0 元！");
		} else if ((paywayid == PaytypeEnum.Pos.getValue()) && (ds.getPos().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ExplinkException("POS刷卡_当前订单支付方式从[POS刷卡]修改为[" + PaytypeEnum.getByValue(newpaywayid) + "],但POS刷卡金额却为 0 元！");
		} else if ((paywayid == PaytypeEnum.Zhipiao.getValue()) && (ds.getCheckfee().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ExplinkException("支票_当前订单支付方式从[支票]修改为[" + PaytypeEnum.getByValue(newpaywayid) + "],但支票金额却为 0 元！");
		} else if ((paywayid == PaytypeEnum.Qita.getValue()) && (ds.getOtherfee().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ExplinkException("其他_当前订单支付方式从[其他]修改为[" + PaytypeEnum.getByValue(newpaywayid) + "],但其他金额却为 0 元！");
		}
		/*
		 * 根据cwb 修改订单表 express_ops_cwb_detail 的金额 修改 newpaywayid 现支付方式
		 */
		this.cwbDAO.updateXiuGaiZhiFuFangShi(co.getOpscwbid(), newpaywayid);
		this.exceptionDAO.editCwbofException(co, FlowOrderTypeEnum.GaiDan.getValue(), this.getSessionUser(), "修改支付方式为：" + PaytypeEnum.getByValue(newpaywayid).getText());
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where newpaywayid=" + newpaywayid + " and opscwbid=" + co.getOpscwbid());

		/**
		 * 修改云订单支付方式
		 */
		try {
			JSONReslutUtil.getResultMessageChangeLog(this.omsUrl() + "/OMSChange/editcwb", "type=3&cwb=" + co.getCwb() + "&newpaywayid=" + newpaywayid, "POST");
		} catch (IOException e1) {
			logger.error("", e1);
			this.logger.info("修改云订单支付方式异常:" + co.getCwb());
		}

		/*
		 * 根据cwb修改express_ops_delivery_state 的支付方式 需要判断订单的支付方式从什么改为什么后进行交替赋值
		 */

		// 整理 更改对象 EdtiCwb_DeliveryStateDetail
		EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
		ec_dsd.setDs(ds);
		ec_dsd.setEditcwbtypeid(EditCwbTypeEnum.XiuGaiZhiFuFangShi.getValue());
		ec_dsd.setRequestUser(requestUser);
		ec_dsd.setEditUser(editUser);
		ec_dsd.setFinance_audit_id(0);
		ec_dsd.setF_payup_audit_id(0);
		ec_dsd.setFd_payup_detail_id(0);
		ec_dsd.setNew_receivedfee(co.getReceivablefee());
		ec_dsd.setNew_returnedfee(co.getPaybackfee());
		ec_dsd.setCwbordertypeid(co.getCwbordertypeid());
		ec_dsd.setNew_flowordertype(co.getFlowordertype());
		ec_dsd.setNew_deliverystate(ds.getDeliverystate());
		ec_dsd.setNew_cwbordertypeid(co.getCwbordertypeid());

		// 订单要修改成什么支付方式，就将对应支付方式的金额赋值成实收金额
		BigDecimal cash, pos, checkfee, otherfee;
		cash = pos = checkfee = otherfee = BigDecimal.ZERO;
		if (newpaywayid == PaytypeEnum.Xianjin.getValue()) {
			cash = ds.getReceivedfee();
		} else if (newpaywayid == PaytypeEnum.Pos.getValue()) {
			pos = ds.getReceivedfee();
		} else if (newpaywayid == PaytypeEnum.Zhipiao.getValue()) {
			checkfee = ds.getReceivedfee();
		} else if (newpaywayid == PaytypeEnum.Qita.getValue()) {
			otherfee = ds.getReceivedfee();
		}

		this.deliveryStateDAO.updateXiuGaiZhiFuFangShi(ds.getId(), cash, pos, checkfee, otherfee);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_delivery_state where " + "cash=" + cash.doubleValue() + " and pos=" + pos.doubleValue() + " and otherfee="
				+ otherfee.doubleValue() + " and checkfee=" + checkfee.doubleValue() + " and id=" + ds.getId());
		ec_dsd.setDs(ds);
		ec_dsd.setDsid(ds.getId());
		ec_dsd.setNew_businessfee(ds.getBusinessfee());
		ec_dsd.setNew_isout(0);
		ec_dsd.setNew_pos(pos);

		// 根据 反馈表 中的gcaid 获得 归班表 express_ops_goto_class_auditing 记录 lock
		// 根据 归班表中的payupid 获得 交款表 express_ops_pay_up 记录 lock
		GotoClassAuditing gca = null;
		if ((ds != null) && (ds.getGcaid() > 0)) {
			gca = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(ds.getGcaid());
			ec_dsd.getDs().setGcaid(gca.getId());
		}
		PayUp payUp = null;
		if ((gca != null) && (gca.getPayupid() > 0)) {
			payUp = this.payUpDAO.getPayUpByIdLock(gca.getPayupid());
			ec_dsd.getDs().setPayupid(payUp.getId());
		}

		// 根据反馈表中的 deliveryid 获得 express_ops_user表 对应的小件员详细信息 用于更改小件员帐户 lock
		User u = this.userDAO.getUserByUseridLock(ds.getDeliveryid());

		// 根据 归班表 payupid 获得 finance_pay_up_audit 站点交款审核表 记录 lock
		FinancePayUpAudit financePayUpAudit = null;
		if ((payUp != null) && (payUp.getAuditid() > 0)) {
			financePayUpAudit = this.financePayUpAuditDAO.getFinancePayUpAuditByIdLock(payUp.getAuditid());
			ec_dsd.setF_payup_audit_id(financePayUpAudit.getId());
		}

		/*
		 * 修改归班表金额与字段 如果订单已经归班 express_ops_goto_class_auditing｛ 将 payupamount
		 * 变更为 减去 反馈表中 (cash+otherfee+checkfee-returnedfee)后的值 再加上传入的
		 * (cash+otherfee+checkfee-paybackfee) 将payupamount_pos 变更为 减去 反馈表中 pos
		 * 后的值 再加上 传入的pos的值 归班表增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (gca != null) {

			SystemInstall usedeliverpay = null;
			try {// 获取 小件员交款 功能使用开关 如果不使用小件员交款，则不调用小件员帐户变动
				usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");

			} catch (Exception e) {
				this.logger.error(cwb + " 获取使用小件员交款功能异常，默认不使用小件员交款功能-修改订单金额");
			}
			FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
			if ((usedeliverpay != null) && usedeliverpay.getValue().equals("yes")) {
				/*
				 * 创建小件员帐户交易记录 （如果开启了小件员交款，并且小件员已经归班审核交款）
				 * finance_deliver_pay_up_detail｛｝
				 */
				fdpud.setDeliverealuser(ds.getDeliveryid());
				// 应交金额 = 现在要交的金额 - 历史要交的金额 如果历史金额大 则为负数 那么在后续的计算总 帐户余额减去负数
				// 则相当于加钱
				BigDecimal payupamount = cash.add(otherfee).add(checkfee);
				payupamount = payupamount.subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()));

				fdpud.setPayupamount(payupamount);
				fdpud.setDeliverpayupamount(BigDecimal.ZERO);
				fdpud.setDeliverAccount(u.getDeliverAccount());
				// 小件员帐户 先减去更改后的金额 因为 货款变更成了更改后的金额 然后加上 之前的订单金额 因为 之前的订单金额已经付过款了
				fdpud.setDeliverpayuparrearage(u.getDeliverAccount().subtract(payupamount));

				fdpud.setPayupamount_pos(pos.subtract(ds.getPos()));
				fdpud.setDeliverpayupamount_pos(BigDecimal.ZERO);
				fdpud.setDeliverPosAccount(u.getDeliverPosAccount());
				// 小件员帐户 先减去更改后的金额 因为 货款变更成了更改后的金额 然后加上 之前的订单金额 因为 之前的订单金额已经付过款了
				fdpud.setDeliverpayuparrearage_pos(u.getDeliverPosAccount().subtract(pos.subtract(ds.getPos())));

				fdpud.setGcaid(gca.getId());
				fdpud.setAudituserid(ds.getUserid()); // 设置操作反馈与归班的人为操作人
				fdpud.setCredate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				fdpud.setType(FinanceDeliverPayUpDetailTypeEnum.XiuGaiZhiFuFangShi.getValue());
				fdpud.setRemark("申请[" + cwb + "]支付方式从[" + PaytypeEnum.getByValue(paywayid).getText() + "]改为[" + PaytypeEnum.getByValue(newpaywayid).getText() + "]");
				this.logger.info("EditCwb_fdpud:" + cwb + " {}", fdpud.toString());
				ec_dsd.setFd_payup_detail_id(this.financeDeliverPayUpDetailDAO.insert(fdpud));
				/*
				 * 修改小件员帐户余额（如果开启了小件员交款，并且小件员已经归班审核交款） finance_pay_up_audit｛ 将
				 * deliverAccount 变更为 fdpud.setDeliverpayuparrearage 将
				 * deliverPosAccount 变更为 fdpud.setDeliverpayuparrearage_pos ｝
				 */
				this.userDAO.updateUserAmount(u.getUserid(), fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos());
				this.logger.info("EditCwb_SQL:" + cwb + " select * express_set_user where deliverAccount=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverPosAccount="
						+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and userid=" + u.getUserid());
			} else {// 没有使用小件员交款功能 那么要复值小件员交易后的余额为0元
				fdpud.setDeliverpayuparrearage(BigDecimal.ZERO);
				fdpud.setDeliverpayuparrearage_pos(BigDecimal.ZERO);
			}
			BigDecimal gca_payupamount = gca.getPayupamount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()));
			gca_payupamount = gca_payupamount.add(cash.add(otherfee).add(checkfee));
			BigDecimal gca_payupamount_pos = gca.getPayupamount_pos().subtract(ds.getPos());
			gca_payupamount_pos = gca_payupamount_pos.add(pos);

			this.gotoClassAuditingDAO.updateForChongZhiShenHe(gca.getId(), gca_payupamount, gca_payupamount_pos, fdpud.getDeliverpayuparrearage(), fdpud.getDeliverpayuparrearage_pos(),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_goto_class_auditing where  payupamount=" + gca_payupamount.doubleValue() + " and payupamount_pos="
					+ gca_payupamount_pos.doubleValue() + " and deliverpayuparrearage=" + fdpud.getDeliverpayuparrearage().doubleValue() + " and deliverpayuparrearage_pos="
					+ fdpud.getDeliverpayuparrearage_pos().doubleValue() + " and id =" + gca.getId());

		}
		/*
		 * 修改交款表 express_ops_pay_up｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee)后的值 再加上传入的 (cash+otherfee+checkfee) 将
		 * amount_pos 变更为 减去 反馈表中 pos 后的值 再加上传入的 pos
		 * 交款表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (payUp != null) {
			BigDecimal payup_payupamount = payUp.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()));
			payup_payupamount = payup_payupamount.add(cash.add(otherfee).add(checkfee));
			BigDecimal payup_payupamount_pos = payUp.getAmountPos().subtract(ds.getPos());
			payup_payupamount_pos = payup_payupamount_pos.add(pos);

			this.payUpDAO.updateForChongZhiShenHe(payUp.getId(), payup_payupamount, payup_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_pay_up where amount=" + payup_payupamount.doubleValue() + " and amountpos=" + payup_payupamount_pos.doubleValue()
					+ " and id =" + payUp.getId());
		}

		/*
		 * 修改站点交款审核表 finance_pay_up_audit｛ 将 amount 变更为 减去 反馈表中
		 * (cash+otherfee+checkfee)后的值 再加上传入的 (cash+otherfee+checkfee) 将
		 * amountpos 变更为 减去 反馈表中 pos 后的值 再加上传入的 pos
		 * 站点交款审核表中增加改单时间字段，改单时将最后一次更改时间赋予其上 用于在各种显示地方点开查看改单记录 ｝
		 */
		if (financePayUpAudit != null) {
			BigDecimal fpua_payupamount = financePayUpAudit.getAmount().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()));
			fpua_payupamount = fpua_payupamount.add(cash.add(otherfee).add(checkfee));
			BigDecimal fpua_payupamount_pos = financePayUpAudit.getAmountpos().subtract(ds.getPos());
			fpua_payupamount_pos = fpua_payupamount_pos.add(pos);

			this.financePayUpAuditDAO.updateForChongZhiShenHe(financePayUpAudit.getId(), fpua_payupamount, fpua_payupamount_pos, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.logger.info("EditCwb_SQL:" + cwb + " select * from finance_pay_up_audit where amount=" + fpua_payupamount.doubleValue() + " and amountpos=" + fpua_payupamount_pos.doubleValue()
					+ " and id =" + financePayUpAudit.getId());

			// 如果有站点交款审核记录，则一定有帐户变动，所以同时要修改站点欠款金额 由于站点的欠款字段是记录的欠款正数 也就是钱多少钱
			// 里面的数值就是多少 所以当有款项变动的时候 应该用当前欠款 减去货款
			Branch b = this.branchDAO.getBranchByBranchidLock(payUp.getBranchid());
			BigDecimal arrearagepayupaudit = b.getArrearagepayupaudit().subtract(ds.getCash().add(ds.getOtherfee()).add(ds.getCheckfee()));
			arrearagepayupaudit = arrearagepayupaudit.add(cash.add(otherfee).add(checkfee));
			BigDecimal posarrearagepayupaudit = b.getPosarrearagepayupaudit().subtract(ds.getPos());
			posarrearagepayupaudit = posarrearagepayupaudit.add(pos);

			this.branchDAO.updateForChongZhiShenHe(b.getBranchid(), arrearagepayupaudit, posarrearagepayupaudit);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_set_branch where  arrearagepayupaudit=" + arrearagepayupaudit.doubleValue() + " and posarrearagepayupaudit="
					+ posarrearagepayupaudit.doubleValue() + " and branchid=" + b.getBranchid());
		}

		// 根据 cwb 与归班id 获得唯一的express_ops_deliver_cash 小件员工作量统计表对应的记录 并修改
		this.deliveryCashDAO.updateXiuGaiZhiFuFangShi(cwb, ds.getDeliverystate(), cash.add(otherfee).add(checkfee), pos);
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_deliver_cash where receivableNoPosfee=" + cash.add(otherfee).add(checkfee) + " and receivablePosfee=" + pos
				+ " and  and cwb={} and deliverystate={} and deliverystate>0", cwb, ds.getDeliverystate());

		Branch accountBranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());

		/*
		 * 修改新结算业务——买单结算(修改订单支付方式) 1.修改为POS刷卡的订单&&金额>0：产生一条POS退款数据
		 * 2.修改为其他支付方式的订单：删除反馈为POS的订单数据 对应修改pos金额，pos单数、合计支付金额
		 */
		if (accountBranch.getAccounttype() == 1) {
			// 查询订单是否有买单POS数据
			List<AccountCwbDetail> list_md = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.Pos.getValue()));
			// 修改为POS刷卡&&金额>0:产生一条POS退款数据
			if (newpaywayid == PaytypeEnum.Pos.getValue()) {
				if (pos.compareTo(BigDecimal.ZERO) > 0) {
					if (list_md.isEmpty()) {
						this.logger.info("===订单修改开始创建买单结算POS数据===");
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, ds.getDeliverybranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), editUser,
								ds.getDeliverybranchid());
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						this.logger.info("用户:{},创建买单结算POS记录,站点:{},订单号:{}", new Object[] { editUser, accountBranch.getBranchname(), co.getCwb() });
					}
				}
			} else {// 删除反馈为POS的订单数据
				if ((list_md != null) && !list_md.isEmpty()) {
					for (AccountCwbDetail list : list_md) {
						if (list.getCheckoutstate() > 0) {// 已交款
							AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
							BigDecimal poscash = o.getPoscash().subtract(ds.getReceivedfee());
							o.setPoscash(poscash);
							o.setPosnums(o.getPosnums() - 1);
							o.setHjfee(o.getHjfee().add(ds.getReceivedfee()));
							this.accountCwbSummaryDAO.saveAccountEditCwb(o);
						}
						this.accountCwbDetailDAO.deleteAccountCwbDetailById(list.getAccountcwbid());
						this.logger.info("EditCwb_SQL:delete from ops_account_cwb_detail where accountcwbid =" + list.getAccountcwbid());
					}
				}
			}
		}

		/*
		 * 修改新结算业务——配送结果结算(修改订单支付方式)
		 */
		List<AccountCwbDetail> list_psjg = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()));
		if ((list_psjg != null) && !list_psjg.isEmpty()) {
			for (AccountCwbDetail list : list_psjg) {
				if (list.getDebetstate() > 0) {// 本次欠款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getDebetstate());
					o.setWjcash(o.getWjcash().subtract(list.getCash().subtract(cash)));// 本次欠款现金
					o.setWjpos(o.getWjpos().subtract(list.getPos().subtract(pos)));// 本次欠款pos
					o.setWjcheck(o.getWjcheck().subtract(list.getCheckfee().subtract(checkfee)));// 本次欠款支票
					o.setWjother(o.getWjother().subtract(list.getOtherfee().subtract(otherfee)));// 本次欠款其他
					o.setWjfee(o.getWjcash().add(o.getWjpos()).add(o.getWjcheck()).add(o.getWjother()));// 本次欠款合计
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}

				if (list.getCheckoutstate() > 0) {// 已交款
					AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
					BigDecimal newcash = list.getCash().subtract(cash);
					BigDecimal newpos = list.getPos().subtract(pos);
					BigDecimal newcheck = list.getCheckfee().subtract(checkfee);
					BigDecimal newother = list.getOtherfee().subtract(otherfee);
					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() == 0)) {// 无欠款
						o.setTocash(o.getTocash().subtract(newcash));// 本次应交现金
						o.setTopos(o.getTopos().subtract(newpos));// 本次应交pos
						o.setTocheck(o.getTocheck().subtract(newcheck));// 本次应交支票
						o.setToother(o.getToother().subtract(list.getOtherfee().subtract(otherfee)));// 本次应交其他
						o.setTofee(o.getTocash().add(o.getTopos()).add(o.getTocheck()).add(o.getToother()));// 本次应交合计

						o.setYjcash(o.getYjcash().subtract(newcash));// 本次实交现金
						o.setYjpos(o.getYjpos().subtract(newpos));// 本次实交pos
						o.setYjcheck(o.getYjcheck().subtract(newcheck));// 本次实交支票
						o.setYjother(o.getYjother().subtract(newother));// 本次实交其他
						o.setYjfee(o.getYjcash().add(o.getYjpos()).add(o.getYjcheck()).add(o.getYjother()));
					}

					if ((list.getCheckoutstate() > 0) && (list.getDebetstate() > 0)) {// 有欠款
						o.setQkcash(o.getQkcash().subtract(newcash.add(newcheck).add(newother)));// 欠款
					}
					o.setHjfee(o.getHjfee().subtract(newcash.add(newcheck).add(newother)));// 合计支付
					this.accountCwbSummaryDAO.saveAccountEditCwb(o);
				}
			}
			this.accountCwbDetailDAO.updateXiuGaiZhiFuFangShi(cwb, cash, pos, checkfee, otherfee);
			this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set cash=" + cash + ",pos=" + pos + ",checkfee=" + checkfee + ",otherfee=" + otherfee + " where cwb=" + cwb);
		}

		/*
		 * 修改新结算业务——扣款结算(修改订单支付方式) 1.修改为POS刷卡的订单&&金额>0：产生一条POS退款数据
		 * 2.修改为其他支付方式的订单：删除反馈为POS的订单数据 对应修改fee金额 更新站点余额、欠款、伪余额、伪欠款字段
		 */
		// 查询订单是否有扣款POS数据
		List<AccountDeducDetail> list_kk = this.accountDeducDetailDAO.getEditCwbByFlowordertype(cwb, String.valueOf(AccountFlowOrderTypeEnum.Pos.getValue()));
		// 修改为POS刷卡&&金额>0:产生一条POS退款数据
		if (newpaywayid == PaytypeEnum.Pos.getValue()) {
			if (pos.compareTo(BigDecimal.ZERO) > 0) {
				if (list_kk.isEmpty()) {
					this.logger.info("===订单修改开始创建扣款结算POS数据===");
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, accountBranch.getBranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), pos, editUser, "", 0, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算POS：站点{},代收货款{}元,id：{}", new Object[] { editUser, accountBranch.getBranchname(), pos, id });
				}
			}
		} else {// 删除反馈为POS的订单数据
			if ((list_kk != null) && !list_kk.isEmpty()) {
				for (AccountDeducDetail list : list_kk) {
					if (list.getRecordid() > 0) {// 已审核
						AccountDeductRecord o = this.accountDeductRecordDAO.getAccountDeductRecordByIdLock(list.getRecordid());
						o.setFee(o.getFee().subtract(list.getFee()));
						// 更新审核汇总 的 fee
						this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), o.getFee());
						// 事务锁 锁住当前站点
						Branch branch = this.branchDAO.getBranchByBranchidLock(list.getBranchid());
						BigDecimal credit = branch.getCredit();
						BigDecimal debt = branch.getDebt();// 欠款
						BigDecimal balance = branch.getBalance();// 余额
						BigDecimal debtvirt = branch.getDebtvirt();// 伪欠款
						BigDecimal balancevirt = branch.getBalancevirt();// 伪余额
						Map feeMap = new HashMap();
						Map feeMap1 = new HashMap();
						feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, list.getFee());
						feeMap1 = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, list.getFee());
						balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						balancevirt = new BigDecimal("".equals(feeMap1.get("balance").toString()) ? "0" : feeMap1.get("balance").toString());
						debtvirt = new BigDecimal("".equals(feeMap1.get("debt").toString()) ? "0" : feeMap1.get("debt").toString());
						// 更新站点余额、欠款、伪余额、伪欠款
						this.branchDAO.updateForFeeAndVirt(list.getBranchid(), balance, debt, balancevirt, debtvirt);
					}
					this.accountDeducDetailDAO.deleteAccountDeducDetailById(list.getId());
					this.logger.info("EditCwb_SQL:delete from ops_account_deduct_detail where id =" + list.getId());
				}
			}
		}

		this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
		this.editCwbDAO.creEditCwb(ec_dsd);
		this.logger.info("EditCwb_SQL:{}修改订单支付方式 结束", cwb);
		return ec_dsd;
	}

	/**
	 * 修改订单类型
	 *
	 * @param cwbordertypeid
	 *            原始的订单类型
	 * @param newcwbordertypeid
	 *            新的订单类型
	 * @param editUser
	 *            修改人
	 * @param requestUser
	 *            申请人
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public EdtiCwb_DeliveryStateDetail analysisAndSaveByXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid, int newcwbordertypeid, Long requestUser, long editUser) {
		this.logger.info("EditCwb_SQL:{}修改订单类型 开始", cwb);
		// 根据cwb 获得 订单表 express_ops_cwb_detail 有效记录 state lock
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (cwbordertypeid == newcwbordertypeid) {
			throw new ExplinkException(CwbOrderTypeIdEnum.getByValue(cwbordertypeid).getText() + "_当前订单类型已经是[" + CwbOrderTypeIdEnum.getByValue(newcwbordertypeid).getText() + "]！");
		} else if ((newcwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) && (co.getPaybackfee().compareTo(BigDecimal.ZERO) > 0)) {
			throw new ExplinkException(CwbOrderTypeIdEnum.getByValue(cwbordertypeid).getText() + "_要修改成[配送]类型的订单，应退金额却大于0！");
		} else if ((newcwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) {
			throw new ExplinkException(CwbOrderTypeIdEnum.getByValue(cwbordertypeid).getText() + "_要修改成[上门退]类型的订单，应收金额却大于0！");
		} else if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getInfactfare().compareTo(BigDecimal.ZERO) > 0)) {
			throw new ExplinkException(CwbOrderTypeIdEnum.getByValue(cwbordertypeid).getText() + "[上门退]类型的订单，应收运费大于0不允许修改订单类型！");
		}
		// 根据cwb 获得反馈表 express_ops_delivery_state 记录 （已审核的，state为1的，对应订单号的） lock
		DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwbLock(cwb);
		/*
		 * 根据cwb 修改订单表 express_ops_cwb_detail 的金额 修改 cwbordertypeid 订单类型
		 */

		this.cwbDAO.updateXiuGaiDingDanLeiXing(co.getOpscwbid(), newcwbordertypeid, DeliveryStateEnum.WeiFanKui);
		this.exceptionDAO.editCwbofException(co, FlowOrderTypeEnum.GaiDan.getValue(), this.getSessionUser(), "修改订单类型为：" + DeliveryStateEnum.getByValue(newcwbordertypeid).getText());
		this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where cwbordertypeid=" + newcwbordertypeid + " and deliverystate=" + DeliveryStateEnum.WeiFanKui.getValue()
				+ " and opscwbid=" + co.getOpscwbid());

		/*
		 * 根据cwb修改express_ops_delivery_state 的配送结果 和订单类型
		 * 当前要修改成什么订单类型，并根据对应要修改成的订单类型，替换当前订单反馈的配送结果 例如上门还成功 转换成 配送成功或者上门退成功
		 * 拒收转换长上门拒退等等
		 */

		// 整理 更改对象 EdtiCwb_DeliveryStateDetail
		EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
		ec_dsd.setEditcwbtypeid(EditCwbTypeEnum.XiuGaiDingDanLeiXing.getValue());
		ec_dsd.setRequestUser(requestUser);
		ec_dsd.setEditUser(editUser);
		DeliveryStateEnum nowDeliveryState = DeliveryStateEnum.WeiFanKui;
		if (ds != null) {
			if (newcwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
				if ((ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {

					nowDeliveryState = DeliveryStateEnum.PeiSongChengGong;

				} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {

					nowDeliveryState = DeliveryStateEnum.JuShou;

				} else {
					nowDeliveryState = DeliveryStateEnum.getByValue((int) ds.getDeliverystate());
				}
			} else if (newcwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				if ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
						|| (ds.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue())) {

					nowDeliveryState = DeliveryStateEnum.ShangMenTuiChengGong;

				} else if (ds.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()) {

					nowDeliveryState = DeliveryStateEnum.ShangMenJuTui;

				} else {
					nowDeliveryState = DeliveryStateEnum.getByValue((int) ds.getDeliverystate());
				}
			} else if (newcwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				if ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {

					nowDeliveryState = DeliveryStateEnum.ShangMenHuanChengGong;

				} else if (ds.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {

					nowDeliveryState = DeliveryStateEnum.JuShou;

				} else {
					nowDeliveryState = DeliveryStateEnum.getByValue((int) ds.getDeliverystate());
				}
			}
			this.cwbDAO.updateXiuGaiDingDanLeiXing(co.getOpscwbid(), newcwbordertypeid, nowDeliveryState);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_cwb_detail where cwbordertypeid=" + newcwbordertypeid + " and deliverystate=" + nowDeliveryState.getValue()
					+ " and opscwbid=" + co.getOpscwbid());

			/**
			 * 修改云订单类型
			 */
			try {
				JSONReslutUtil.getResultMessageChangeLog(this.omsUrl() + "/OMSChange/editcwb", "type=4&cwb=" + co.getCwb() + "&cwbordertypeid=" + newcwbordertypeid + "&deliverystate="
						+ nowDeliveryState.getValue(), "POST");
			} catch (IOException e1) {
				logger.error("", e1);
				this.logger.info("修改云订单类型异常:" + co.getCwb());
			}

			this.deliveryStateDAO.updateXiuGaiDingDanLeiXing(ds.getId(), newcwbordertypeid, nowDeliveryState);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_delivery_state where cwbordertypeid=" + newcwbordertypeid + " and deliverystate=" + nowDeliveryState.getValue()
					+ " and id=" + ds.getId());
			ec_dsd.setDs(ds);
			ec_dsd.setDsid(ds.getId());
			ec_dsd.setNew_businessfee(ds.getBusinessfee());
			ec_dsd.setNew_isout(ds.getIsout());
			ec_dsd.setNew_pos(ds.getPos());

			// 根据 cwb 与归班id 获得唯一的express_ops_deliver_cash 小件员工作量统计表对应的记录 并修改
			this.deliveryCashDAO.updateXiuGaiDingDanLeiXing(cwb, ds.getDeliverystate(), nowDeliveryState);
			this.logger.info("EditCwb_SQL:" + cwb + " select * from express_ops_deliver_cash where delivertState=" + nowDeliveryState.getValue()
					+ "  and cwb={} and deliverystate={} and deliverystate>0", cwb, ds.getDeliverystate());
		} else {
			ec_dsd.setDs(new DeliveryState());
			ec_dsd.getDs().setCwb(cwb);
			ec_dsd.setNew_businessfee(co.getReceivablefee().add(co.getPaybackfee()));
			ec_dsd.setNew_isout(co.getReceivablefee().compareTo(co.getPaybackfee()) >= 0 ? 0 : 1);
		}
		ec_dsd.setFinance_audit_id(0);
		ec_dsd.setF_payup_audit_id(0);
		ec_dsd.setFd_payup_detail_id(0);
		ec_dsd.setNew_receivedfee(co.getReceivablefee());
		ec_dsd.setNew_returnedfee(co.getPaybackfee());
		ec_dsd.setCwbordertypeid(cwbordertypeid);
		ec_dsd.setNew_flowordertype(co.getFlowordertype());
		ec_dsd.setNew_deliverystate(nowDeliveryState.getValue());
		ec_dsd.setNew_cwbordertypeid(newcwbordertypeid);

		this.logger.info("EditCwb_ec_dsd:" + cwb + " {}", ec_dsd.toString());
		this.editCwbDAO.creEditCwb(ec_dsd);
		this.logger.info("EditCwb_SQL:{}修改订单类型 结束", cwb);

		/*
		 * 修改新结算业务——买单结算(修改订单类型)
		 * 1.中转退款、退货退款、改站冲款订单(根据不同订单类型)修改相对应的已交款的金额，因为不会产生欠款，所以无需考虑欠款金额
		 * 2.中转退款、退货退款、改站冲款订单修改订单类型、反馈结果、应退金额(根据不同订单类型) 3.出库的订单修改订单类型、反馈结果
		 */
		String ids = AccountFlowOrderTypeEnum.KuFangChuKu.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue() + ","
				+ AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue();
		List<AccountCwbDetail> list_md = this.accountCwbDetailDAO.getEditCwbByFlowordertype(cwb, ids);
		if ((list_md != null) && !list_md.isEmpty()) {
			for (AccountCwbDetail list : list_md) {
				// 中转退款、退货退款
				if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue())
						|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue())) {
					// 不同类型订单取不同的金额字段
					BigDecimal ZZfee = this.accountCwbDetailService.getZZPaybackfee(newcwbordertypeid, nowDeliveryState.getValue(), co.getReceivablefee(), co.getPaybackfee());
					// 已交款
					if (list.getCheckoutstate() > 0) {
						AccountCwbSummary o = this.accountCwbSummaryDAO.getAccountCwbSummaryByIdLock(list.getCheckoutstate());
						// 中转退款
						if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())) {
							o.setZzcash(o.getZzcash().subtract(list.getPaybackfee().subtract(ZZfee)));// 中转退款=中转退款+原paybackfee-新paybackfee
																										// (新paybackfee根据不同订单类型取值不同)
						}
						// 退货退款
						if (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue()) {
							// 不同类型订单取不同的金额字段
							o.setThcash(o.getThcash().subtract(list.getPaybackfee().subtract(ZZfee)));// 退货退款=退货退款+原paybackfee-新paybackfee
																										// (新paybackfee根据不同订单类型取值不同)
						}
						o.setHjfee(o.getHjfee().add(list.getPaybackfee().subtract(ZZfee)));// 合计支付
						this.accountCwbSummaryDAO.saveAccountEditCwb(o);
					}
					// 中转退款、退货退款、改站冲款修改订单类型、反馈结果、应退金额
					this.accountCwbDetailDAO.updateXiuGaiDingDanLeiXing(list.getAccountcwbid(), newcwbordertypeid, nowDeliveryState.getValue(), ZZfee);
					this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set cwbordertypeid=" + newcwbordertypeid + ",deliverystate=" + nowDeliveryState.getValue() + ",paybackfee=" + ZZfee
							+ " where accountcwbid=" + list.getAccountcwbid());
				} else {// 出库的订单
					this.accountCwbDetailDAO.updateXiuGaiDingDanLeiXing(list.getAccountcwbid(), newcwbordertypeid, nowDeliveryState.getValue(), co.getPaybackfee());
					this.logger.info("EditCwb_SQL:update ops_account_cwb_detail set cwbordertypeid=" + newcwbordertypeid + ",deliverystate=" + nowDeliveryState.getValue() + ",paybackfee="
							+ co.getPaybackfee() + " where accountcwbid=" + list.getAccountcwbid());
				}
			}
		}

		/*
		 * 修改新结算业务——扣款结算(修改订单类型) 1.更具不同订单类型更新detail表fee 2.更新站点余额、欠款、伪余额、伪欠款字段
		 */
		String idsKK = AccountFlowOrderTypeEnum.KouKuan.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuan.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuo.getValue() + ","
				+ AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();
		List<AccountDeducDetail> list_kk = this.accountDeducDetailDAO.getEditCwbByFlowordertype(cwb, idsKK);
		if ((list_kk != null) && !list_kk.isEmpty()) {
			// 根据不同订单类型 取得应扣款金额
			// BigDecimal
			// newfee=accountDeductRecordService.getDetailFee(newcwbordertypeid,co.getReceivablefee(),co.getPaybackfee());
			BigDecimal newfee = this.accountDeducDetailService.getTHPaybackfee(co.getCwbordertypeid(), co.getDeliverystate(), co.getReceivablefee(), co.getPaybackfee());

			for (AccountDeducDetail list : list_kk) {
				// 锁住该站点记录
				Branch branchLock = this.branchDAO.getBranchByBranchidLock(list.getBranchid());
				BigDecimal oldfee = list.getFee();// 加减款金额
				BigDecimal credit = branchLock.getCredit();// 信用额度
				BigDecimal balance = branchLock.getBalance();// 余额
				BigDecimal debt = branchLock.getDebt();// 欠款
				BigDecimal debtvirt = branchLock.getDebtvirt();// 伪欠款
				BigDecimal balancevirt = branchLock.getBalancevirt();// 伪余额

				if (list.getRecordid() > 0) {// 已结算
					AccountDeductRecord o = this.accountDeductRecordDAO.getAccountDeductRecordByIdLock(list.getRecordid());

					// 冲正
					if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuan.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())
							|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuo.getValue())) {
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====减款逻辑=====减款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, oldfee.subtract(newfee));
						} else {
							// ====加款逻辑=====加款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.addBranchFee(balance, debt, newfee.subtract(oldfee));
						}
						balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 的余额、欠款
						this.branchDAO.updateForFee(list.getBranchid(), balance, debt);
						this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), o.getFee().subtract(oldfee.subtract(newfee)));
					} else {// 出库
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====加款逻辑=====加款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.addBranchFee(balance, debt, oldfee.subtract(newfee));
						} else {
							// ====减款逻辑=====减款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, newfee.subtract(oldfee));
						}
						balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 的余额、欠款
						this.branchDAO.updateForFee(list.getBranchid(), balance, debt);
						this.accountDeductRecordDAO.updateRecordforFee(list.getRecordid(), newfee);
					}
				}

				if (list.getRecordidvirt() > 0) {// 已伪结算
					// 冲正
					if ((list.getFlowordertype() == AccountFlowOrderTypeEnum.ZhongZhuan.getValue()) || (list.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue())
							|| (list.getFlowordertype() == AccountFlowOrderTypeEnum.TuiHuo.getValue())) {
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====减款逻辑=====减款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, oldfee.subtract(newfee));
						} else {
							// ====加款逻辑=====加款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.addBranchFee(balancevirt, debtvirt, newfee.subtract(oldfee));
						}
						balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 伪余额、伪欠款
						this.branchDAO.updateForVirt(list.getBranchid(), balancevirt, debtvirt);
					} else {// 出库
						Map feeMap = new HashMap();
						if (oldfee.compareTo(newfee) > 0) {
							// ====加款逻辑=====加款金额：oldfee-newfee
							feeMap = this.accountDeductRecordService.addBranchFee(balancevirt, debtvirt, oldfee.subtract(newfee));
						} else {
							// ====减款逻辑=====减款金额：newfee-oldfee
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, newfee.subtract(oldfee));
						}
						balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
						debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
						// 修改branch表 伪余额、伪欠款
						this.branchDAO.updateForVirt(list.getBranchid(), balancevirt, debtvirt);
					}
				}

				// 中转退款、退货退款、改站冲款修改订单类型、反馈结果、应退金额
				this.accountDeducDetailDAO.updateXiuGaiDingDanLeiXing(list.getId(), newfee);
				this.logger.info("EditCwb_SQL:update ops_account_deduct_detail set cwbordertypeid=" + newcwbordertypeid + ",fee=" + newfee + " where id=" + list.getId());
			}
		}

		return ec_dsd;
	}

	public String omsUrl() {
		// SystemInstall omsPathUrl =
		// systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = this.systemInstallDAO.getSystemInstallByName("omsUrl");
		String url1 = "";
		if (omsUrl != null) {
			url1 = omsUrl.getValue();
		} else {
			url1 = "http://127.0.0.1:8080/oms/";
		}
		final String url = url1;
		logger.info(url);
		return url;
	}

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// 获取已经完成了的订单
	public String getCompletedCwbByCwb(CwbOrder order) {
		String cwbErrMsg = "";
		
		Set<Integer> unModifyFlowSet = new HashSet<Integer>();
		unModifyFlowSet.add(FlowOrderTypeEnum.YiShenHe.getValue());//已审核
		unModifyFlowSet.add(FlowOrderTypeEnum.TuiHuoChuZhan.getValue());//退货出站
		unModifyFlowSet.add(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());//退货站入库
		unModifyFlowSet.add(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());//退供货商出库
		unModifyFlowSet.add(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());//供货商拒收返库
		unModifyFlowSet.add(FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue());//审核为退货再投
		unModifyFlowSet.add(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue());//退供货商成功
		
		Set<Integer> unModifyDeliveryStateSet = new HashSet<Integer>();
		unModifyDeliveryStateSet.add(DeliveryStateEnum.PeiSongChengGong.getValue());
		unModifyDeliveryStateSet.add(DeliveryStateEnum.ShangMenTuiChengGong.getValue());
		unModifyDeliveryStateSet.add(DeliveryStateEnum.ShangMenHuanChengGong.getValue());
		unModifyDeliveryStateSet.add(DeliveryStateEnum.JuShou.getValue());
		unModifyDeliveryStateSet.add(DeliveryStateEnum.BuFenTuiHuo.getValue());
		unModifyDeliveryStateSet.add(DeliveryStateEnum.HuoWuDiuShi.getValue());
		
		//String cwb = order.getCwb();
		long flowordertype = order.getFlowordertype();
		long deliverystate = order.getDeliverystate();
		if (unModifyFlowSet.contains((int)flowordertype)) {
			if (FlowOrderTypeEnum.YiShenHe.getValue() == (int)flowordertype) {
				if (unModifyDeliveryStateSet.contains((int)deliverystate)) {
					cwbErrMsg = "已经审核过的订单不允许修改！";
				}
			} else {
				cwbErrMsg = "已经审核过的订单不允许修改！";
			}
		}
		return cwbErrMsg;
	}

	// 新增站点重置反馈调整记录
	public void createFnOrgOrderAdjustRecord(String cwb, EdtiCwb_DeliveryStateDetail ec_dsd) {
		OrgOrderAdjustmentRecord record = new OrgOrderAdjustmentRecord();
		DeliveryState deliveryState = ec_dsd.getDs();
		// 查询出对应订单号的账单详细信息
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		if (order != null) {
			// 根据不同的订单类型
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());
			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(order.getCwbordertypeid());
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());
			if ((order.getPaybackfee() != null) && (order.getPaybackfee().compareTo(BigDecimal.ZERO) > 0)) {
				record.setModifyFee(BigDecimal.ZERO);
				record.setAdjustAmount(order.getPaybackfee());
			} else {
				record.setModifyFee(order.getReceivablefee());
				record.setAdjustAmount(order.getReceivablefee().negate());
			}
			record.setSignTime(DateTimeUtil.parseDate(deliveryState.getSign_time(), DateTimeUtil.DEF_DATETIME_FORMAT));
			record.setPayWayChangeFlag(0);
			// 调整金额为货款调整
			record.setAdjustType(BillAdjustTypeEnum.OrderFee.getValue());
			// 记录运费
			record.setFreightAmount(ec_dsd.getOriInfactfare());
			this.fnOrgOrderAdjustRecordDAO.creOrderAdjustmentRecord(record);

			// 如果是是上门退订单，生成运单调整记录
			if (CwbOrderTypeIdEnum.Shangmentui.getValue() == order.getCwbordertypeid()) {
				record.setModifyFee(order.getInfactfare());
				record.setAdjustAmount(ec_dsd.getOriInfactfare().negate());
				// 调整金额为运费调整
				record.setAdjustType(BillAdjustTypeEnum.ExpressFee.getValue());
				this.fnOrgOrderAdjustRecordDAO.creOrderAdjustmentRecord(record);
			}
		}
	}
}
