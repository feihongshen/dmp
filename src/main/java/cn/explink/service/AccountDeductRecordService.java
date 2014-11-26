package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.AccountDeducDetailDAO;
import cn.explink.dao.AccountDeductRecordDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeductRecord;
import cn.explink.domain.AccountDeductRecordView;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.StringUtil;

@Service
public class AccountDeductRecordService {
	private Logger logger = LoggerFactory.getLogger(AccountDeductRecordService.class);
	@Autowired
	AccountDeductRecordDAO accountDeductRecordDAO;
	@Autowired
	AccountDeducDetailDAO accountDeducDetailDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;

	/**
	 * 加款逻辑
	 * 
	 * @param balance
	 *            余额
	 * @param debt
	 *            欠款
	 * @param fee
	 *            加款金额
	 */
	public Map addBranchFee(BigDecimal balance, BigDecimal debt, BigDecimal fee) {
		// new [余额]=[充值金额]-[当前欠款]<0?[当前余额]:[当前余额]+[充值金额]-[当前欠款]
		// new [欠款]= [充值金额]-[当前欠款]>0?0:[当前欠款]-[充值金额]
		Map feeMap = new HashMap();
		balance = (fee.subtract(debt).compareTo(new BigDecimal("0"))) < 0 ? balance : balance.add(fee).subtract(debt);
		debt = (fee.subtract(debt).compareTo(new BigDecimal("0"))) > 0 ? new BigDecimal("0") : debt.subtract(fee);
		feeMap.put("balance", balance);
		feeMap.put("debt", debt);
		return feeMap;
	}

	/**
	 * 扣款逻辑
	 * 
	 * @param credit
	 * @param balance
	 * @param debt
	 * @param koukuan
	 * @return
	 */
	public Map subBranchFee(BigDecimal credit, BigDecimal balance, BigDecimal debt, BigDecimal koukuan) {
		Map feeMap = new HashMap();
		// [可用额度] = [当前信誉额度]+[当前余额]-[当前欠款]
		BigDecimal credituse = credit.add(balance).subtract(debt);
		// [扣款金额]>[可用额度]
		if (koukuan.compareTo(credituse) > 0) {
			logger.info("站点余额不足");
			throw new CwbException("", AccountFlowOrderTypeEnum.KouKuan.getValue(), ExceptionCwbErrorTypeEnum.ZhanDianYuEBuZu);
		} else {
			// new [欠款] =([扣款金额]-[当前余额]+[当前欠款])>0?([扣款金额]-[当前余额]+[当前欠款]):0;
			// new [余额] = ([当前余额]-[扣款金额])<0?0:([当前余额]-[扣款金额]);
			debt = (koukuan.subtract(balance).add(debt)).compareTo(new BigDecimal("0")) > 0 ? koukuan.subtract(balance).add(debt) : new BigDecimal("0");
			balance = (balance.subtract(koukuan)).compareTo(new BigDecimal("0")) < 0 ? new BigDecimal("0") : balance.subtract(koukuan);
			feeMap.put("balance", balance);
			feeMap.put("debt", debt);
		}
		return feeMap;
	}

	/**
	 * 充值
	 * 
	 * @param request
	 * @param user
	 */
	@Transactional
	public void saveRecharge(HttpServletRequest request, User user) {
		logger.info("===站点充值开始===");
		long branchid = Long.parseLong("".equals(request.getParameter("branchid")) ? "0" : request.getParameter("branchid"));
		BigDecimal fee = new BigDecimal("".equals(request.getParameter("fee")) ? "0" : request.getParameter("fee"));
		// 事务锁 锁住当前站点
		Branch branch = branchDAO.getBranchByBranchidLock(branchid);

		BigDecimal debt = branch.getDebt();// 欠款
		BigDecimal balance = branch.getBalance();// 余额
		BigDecimal debtvirt = branch.getDebtvirt();// 伪欠款
		BigDecimal balancevirt = branch.getBalancevirt();// 伪余额

		// ====加款逻辑=====
		Map feeMap = new HashMap();
		feeMap = this.addBranchFee(balance, debt, fee);
		balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
		debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

		feeMap = this.addBranchFee(balancevirt, debtvirt, fee);
		balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
		debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

		branchDAO.updateForFeeAndVirt(branchid, balance, debt, balancevirt, debtvirt);

		// 插入一条冲值记录
		AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
		accountDeductRecord = this.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.ChongZhi.getValue(), fee, branch.getBalance(), balance, user, branch.getDebt(), debt, "", "");
		long recordid = accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

		logger.info("用户{},对{}站点进行充值：原余额{}元，原欠款{}元。充值{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branch.getBranchname(), branch.getBalance(), branch.getDebt(), fee, balance, debt });
		logger.info("===站点充值结束===");
	}

	/**
	 * 调账
	 * 
	 * @param request
	 * @param user
	 */
	@Transactional
	public void saveTiaoZhang(HttpServletRequest request, User user) {
		logger.info("===站点调账开始===");
		long branchid = Long.parseLong("".equals(request.getParameter("branchid")) ? "0" : request.getParameter("branchid"));
		BigDecimal fee = new BigDecimal("".equals(request.getParameter("fee")) ? "0" : request.getParameter("fee"));
		String memo = StringUtil.nullConvertToEmptyString(request.getParameter("memo"));

		// 事务锁 锁住当前站点
		Branch branch = branchDAO.getBranchByBranchidLock(branchid);

		BigDecimal credit = branch.getCredit();
		BigDecimal debt = branch.getDebt();// 欠款
		BigDecimal balance = branch.getBalance();// 余额
		BigDecimal debtvirt = branch.getDebtvirt();// 伪欠款
		BigDecimal balancevirt = branch.getBalancevirt();// 伪余额

		Map feeMap = new HashMap();
		Map feeMap1 = new HashMap();
		if (fee.compareTo(new BigDecimal("0")) > 0) {
			// ====加款逻辑=====
			logger.info("===站点调账加款===");
			feeMap = this.addBranchFee(balance, debt, fee);
			feeMap1 = this.addBranchFee(balancevirt, debtvirt, fee);
		} else {
			// ====减款逻辑=====
			logger.info("===站点调账减款===");
			feeMap = this.subBranchFee(credit, balance, debt, new BigDecimal(Math.abs(fee.doubleValue())));
			feeMap1 = this.subBranchFee(credit, balancevirt, debtvirt, new BigDecimal(Math.abs(fee.doubleValue())));
		}
		balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
		debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

		balancevirt = new BigDecimal("".equals(feeMap1.get("balance").toString()) ? "0" : feeMap1.get("balance").toString());
		debtvirt = new BigDecimal("".equals(feeMap1.get("debt").toString()) ? "0" : feeMap1.get("debt").toString());

		branchDAO.updateForFeeAndVirt(branchid, balance, debt, balancevirt, debtvirt);

		// 插入一条调账记录
		AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
		accountDeductRecord = this.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.TiaoZhang.getValue(), fee, branch.getBalance(), balance, user, branch.getDebt(), debt, memo, "");
		long recordid = accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

		logger.info("用户{},对{}站点进行调账：原余额{}元，原欠款{}元。调账{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branch.getBranchname(), branch.getBalance(), branch.getDebt(), fee, balance, debt });
		logger.info("===站点调账结束===");
	}

	/**
	 * 预扣款冲正审核
	 * 
	 * @param request
	 * @param user
	 */
	@Transactional
	public void saveRecord(HttpServletRequest request, User user, String flowordertype) {
		long floworder = 0;
		if ((AccountFlowOrderTypeEnum.ZhongZhuan.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()).equals(flowordertype)) {
			floworder = AccountFlowOrderTypeEnum.ZhongZhuan.getValue();
		} else {
			floworder = Long.parseLong(flowordertype);
		}

		logger.info("===预扣款冲正审核开始===");
		String ids = StringUtil.nullConvertToEmptyString(request.getParameter("ids"));
		String memo = StringUtil.nullConvertToEmptyString(request.getParameter("memo"));
		long branchid = Long.parseLong(request.getParameter("branchid") == null ? "0" : request.getParameter("branchid").toString());
		List<AccountDeducDetail> list = accountDeducDetailDAO.getDeducDetailRecordIdById(ids, branchid);
		if (list != null && !list.isEmpty()) {
			logger.info("===预扣款冲正:真实冲正开始===");
			BigDecimal fee = BigDecimal.ZERO;
			for (AccountDeducDetail detailList : list) {
				if (detailList.getRecordid() > 0) {
					throw new CwbException("", AccountFlowOrderTypeEnum.ChongZheng.getValue(), ExceptionCwbErrorTypeEnum.JieSuanChongFuTiJiao);
				}
				fee = fee.add(detailList.getFee());
			}
			// 锁住该站点记录
			Branch branchLock = branchDAO.getBranchByBranchidLock(branchid);
			BigDecimal debt = branchLock.getDebt();// 欠款
			BigDecimal balance = branchLock.getBalance();// 余额
			// ====加款逻辑=====
			Map feeMap = new HashMap();
			feeMap = this.addBranchFee(balance, debt, fee);
			balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
			debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
			branchDAO.updateForFee(branchid, balance, debt);
			// 插入一条冲正记录
			AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
			accountDeductRecord = this.loadFormForAccountDeductRecord(branchid, floworder, fee, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, memo, "");
			long recordid = accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);
			// 更新订单记录 recordid
			accountDeducDetailDAO.updateJobKouKuan(recordid, ids, branchid);
			logger.info("用户{},对{}站点进行预扣款冲正：原余额{}元，原欠款{}元。冲正{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(), fee,
					balance, debt });
			logger.info("===预扣款冲正:真实冲正结束===");
		}

		List<AccountDeducDetail> listVirt = accountDeducDetailDAO.getDeducDetailRecordIdByIdVirt(ids, branchid);
		if (listVirt != null && !listVirt.isEmpty()) {
			logger.info("===预扣款冲正:伪冲正开始===");
			// 锁住该站点记录
			Branch branchLock = branchDAO.getBranchByBranchidLock(branchid);
			for (AccountDeducDetail detailList : listVirt) {
				BigDecimal fee = detailList.getFee() == null ? BigDecimal.ZERO : detailList.getFee();// 加款
				BigDecimal debtvirt = branchLock.getDebtvirt();// 伪欠款
				BigDecimal balancevirt = branchLock.getBalancevirt();// 伪余额
				// ====加款逻辑=====
				Map feeMap = new HashMap();
				feeMap = this.addBranchFee(balancevirt, debtvirt, fee);
				balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
				debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
				branchDAO.updateForVirt(branchid, balancevirt, debtvirt);
				logger.info("用户{},对{}站点进行预扣款伪冲正：原伪余额{}元，原伪欠款{}元。冲正{}后，余额{}元，欠款{}元",
						new Object[] { user.getRealname(), branchLock.getBranchname(), branchLock.getBalancevirt(), branchLock.getDebtvirt(), fee, balancevirt, debtvirt });
			}
			accountDeducDetailDAO.updateJobKouKuanVirt(ids, branchid);
			logger.info("===预扣款冲正:伪冲正结束===");
		}
	}

	/**
	 * 定时器监控 到货时间超过24小时 自动到货
	 */
	@Transactional
	public void updateJobKouKuan() {
		// //24小时前
		// // long
		// createtimesecond=System.currentTimeMillis();//-(24*60*60*1000);
		// long createtimesecond=System.currentTimeMillis()-(24*60*60*1000);
		// //=======伪扣款=============
		// List<AccountDeducDetail> listSub
		// =accountDeducDetailDAO.selectJobKouKuanVirt(String.valueOf(AccountFlowOrderTypeEnum.KouKuan.getValue()),createtimesecond);
		// if(listSub!=null&&!listSub.isEmpty()){
		// logger.info("===系统扣款结算伪扣款开始===");
		// String deatilIds="";
		// for(AccountDeducDetail detailList:listSub){
		// //锁住该站点记录
		// Branch
		// branchLock=branchDAO.getBranchByBranchidLock(detailList.getBranchid());
		// BigDecimal koukuan=detailList.getFee();//加款
		// deatilIds+=detailList.getMemo()+",";//冲正记录ids
		// BigDecimal debtvirt=branchLock.getDebtvirt();//伪欠款
		// BigDecimal balancevirt=branchLock.getBalancevirt();//伪余额
		// BigDecimal credit=branchLock.getCredit();
		// //====扣款逻辑=====
		// Map feeMap = new HashMap();
		// feeMap=this.subBranchFee(credit,balancevirt,debtvirt,koukuan);
		// balancevirt=new
		// BigDecimal("".equals(feeMap.get("balance").toString())?"0":feeMap.get("balance").toString());
		// debtvirt=new
		// BigDecimal("".equals(feeMap.get("debt").toString())?"0":feeMap.get("debt").toString());
		//
		// //修改branch表 伪余额、伪欠款
		// branchDAO.updateForVirt(detailList.getBranchid(),balancevirt,debtvirt);
		//
		// logger.info("系统对{}站点进行伪扣款：原伪余额{}元，原伪欠款{}元。自动到货{}后，伪余额{}元，伪欠款{}元",
		// new Object[]
		// {branchLock.getBranchname(),branchLock.getBalancevirt(),branchLock.getDebtvirt(),koukuan,balancevirt,debtvirt});
		// }
		// accountDeducDetailDAO.updateJobKouKuanVirt(deatilIds.substring(0,deatilIds.lastIndexOf(",")));
		// logger.info("===系统扣款结算伪扣款结束===");
		// }
		//
		//
		// //=======伪加款=============
		// List<AccountDeducDetail> listAdd
		// =accountDeducDetailDAO.selectJobKouKuanVirt(String.valueOf(AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()),createtimesecond);
		// if(listAdd!=null&&!listAdd.isEmpty()){
		// logger.info("===系统扣款结算伪加款开始===");
		// String deatilIds="";
		// for(AccountDeducDetail detailList:listAdd){
		// //锁住该站点记录
		// Branch
		// branchLock=branchDAO.getBranchByBranchidLock(detailList.getBranchid());
		// BigDecimal jiakuan=detailList.getFee();//加款
		// deatilIds+=detailList.getMemo()+",";//冲正记录ids
		// BigDecimal debtvirt=branchLock.getDebtvirt();//伪欠款
		// BigDecimal balancevirt=branchLock.getBalancevirt();//伪余额
		//
		// //====扣款逻辑=====
		// Map feeMap = new HashMap();
		// feeMap=this.addBranchFee(balancevirt,debtvirt,jiakuan);
		// balancevirt=new
		// BigDecimal("".equals(feeMap.get("balance").toString())?"0":feeMap.get("balance").toString());
		// debtvirt=new
		// BigDecimal("".equals(feeMap.get("debt").toString())?"0":feeMap.get("debt").toString());
		//
		// //修改branch表 伪余额、伪欠款
		// branchDAO.updateForVirt(detailList.getBranchid(),balancevirt,debtvirt);
		//
		// logger.info("系统对{}站点进行伪加款：原伪余额{}元，原伪欠款{}元。自动到货{}后，伪余额{}元，伪欠款{}元",
		// new Object[]
		// {branchLock.getBranchname(),branchLock.getBalancevirt(),branchLock.getDebtvirt(),jiakuan,balancevirt,debtvirt});
		// }
		// accountDeducDetailDAO.updateJobKouKuanVirt(deatilIds.substring(0,deatilIds.lastIndexOf(",")));
		// logger.info("===系统扣款结算伪加款结束===");
		// }

	}

	public List<AccountDeductRecord> getAccountDeductRecordListByBranchAndUser(List<User> userList, long page, long branchid, String starttime, String endtime, long recordtype) {
		try {
			return accountDeductRecordDAO.getAccountDeductRecordAndUserNamePage(page, branchid, starttime, endtime, recordtype);
			/*
			 * List<AccountDeductRecord> detailList =
			 * accountDeductRecordDAO.getAccountDeductRecordAndUserNamePage
			 * (page,branchid,starttime, endtime,recordtype); //处理列表页关联的站点名称
			 * List<AccountDeductRecord> list=new
			 * ArrayList<AccountDeductRecord>(); if(!detailList.isEmpty()){ for
			 * (AccountDeductRecord o : detailList) { //
			 * o.setBranchname(dataStatisticsService
			 * .getQueryBranchName(branchList,o.getBranchid()));
			 * o.setUsername(dataStatisticsService
			 * .getQueryUserName(userList,o.getUserid())); list.add(o); } }
			 * return list;
			 */
		} catch (Exception e) {
			logger.info("分页查找扣款结算列表异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountDeductRecordView>
	 */
	public List<AccountDeductRecordView> getViewByAccountDeductRecord(List<AccountDeductRecord> clist) {
		List<AccountDeductRecordView> viewList = new ArrayList<AccountDeductRecordView>();
		if (clist != null && !clist.isEmpty()) {
			for (AccountDeductRecord acd : clist) {
				AccountDeductRecordView o = new AccountDeductRecordView();
				/* o.setBranchname(acd.getBranchname()); */
				o.setCwb("".equals(acd.getCwb()) ? "--" : acd.getCwb());
				o.setFee(acd.getFee());
				o.setMemo(acd.getMemo());
				o.setCreatetime(acd.getCreatetime());
				o.setUsername(acd.getUsername());
				o.setBeforefee(acd.getBeforefee());
				o.setAfterfee(acd.getAfterfee());
				o.setBeforedebt(acd.getAfterdebt());
				o.setAfterdebt(acd.getAfterdebt());
				// 类型
				for (AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()) {
					if (acd.getRecordtype() == ft.getValue()) {
						o.setRecordtype(ft.getText());
					}
				}
				viewList.add(o);
			}
		}
		return viewList;
	}

	public AccountDeductRecord loadFormForAccountDeductRecord(long branchid, long flowordertype, BigDecimal fee, BigDecimal beforefee, BigDecimal afterfee, User user, BigDecimal beforedebt,
			BigDecimal afterdebt, String memo, String cwb) {
		AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
		accountDeductRecord.setBranchid(branchid);
		accountDeductRecord.setRecordtype(flowordertype);
		accountDeductRecord.setFee(fee);
		accountDeductRecord.setBeforefee(beforefee);
		accountDeductRecord.setAfterfee(afterfee);
		if (null != user) {
			accountDeductRecord.setUserid(user.getUserid());
		}
		accountDeductRecord.setBeforedebt(beforedebt);
		accountDeductRecord.setAfterdebt(afterdebt);
		accountDeductRecord.setMemo(memo);
		accountDeductRecord.setCwb(cwb);
		return accountDeductRecord;
	}

	public BigDecimal getDetailFee(long cwbordertypeid, BigDecimal receivablefee, BigDecimal paybackfee) {
		BigDecimal fee = BigDecimal.ZERO;// 应退金额
		// 上门退 fee=paybackfee
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			fee = paybackfee;
		} else {
			fee = receivablefee;
		}
		return fee;
	}

	/**
	 * 对最后一条出库记录 进行virt返款
	 * 
	 * @param cwb
	 * @param user
	 */
	public void returnLastChuKu(String cwb, User user) {
		// 根据订单号查询最后一条出库记录
		List<AccountDeducDetail> listd = accountDeducDetailDAO.getDeducDetailByZhongZhaunRuKuKouKuan(cwb, AccountFlowOrderTypeEnum.KouKuan.getValue() + "");
		if (listd != null && !listd.isEmpty()) {
			// 锁住该站点记录
			Branch branchLock = branchDAO.getBranchByBranchidLock(listd.get(0).getBranchid());
			if (branchLock.getAccounttype() == 3) {
				BigDecimal koukuan = listd.get(0).getFee();// 代收货款
				// 代收货款>0 进行扣款
				if (koukuan.compareTo(new BigDecimal("0")) > 0) {
					Map feeMap = new HashMap();
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款
					// 加款逻辑
					feeMap = this.addBranchFee(balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					// 修改branch表 的余额、欠款
					branchDAO.updateForFee(listd.get(0).getBranchid(), balance, debt);
					logger.info("用户{},对{}站点进行virt返款：原余额{}元，原欠款{}元。返款{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(),
							koukuan, balance, debt });
				}
				// 更新订单记录 recordidvirt=1 已结算
				accountDeducDetailDAO.updateDeducDetailRecordVirtIdById(listd.get(0).getId() + "");
			}
		}

	}

	/**
	 * 导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountDeductRecordView>
	 */
	public List<AccountDeductRecordView> getViewByHuiZong(List<AccountDeductRecord> clist) {
		List<AccountDeductRecordView> viewList = new ArrayList<AccountDeductRecordView>();
		if (clist != null && !clist.isEmpty()) {
			for (AccountDeductRecord acd : clist) {
				AccountDeductRecordView o = new AccountDeductRecordView();
				// 类型
				for (AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()) {
					if (acd.getRecordtype() == ft.getValue()) {
						o.setRecordtype(ft.getText());
					}
				}

				if (acd.getRecordtype() == AccountFlowOrderTypeEnum.KouKuan.getValue()) {
					o.setAfterfee(acd.getFee());
				} else {
					o.setBeforefee(acd.getFee());
				}

				o.setNums(acd.getNums());
				o.setCreatetime(acd.getCreatetime().substring(0, 10));
				viewList.add(o);
			}
		}
		return viewList;
	}

	/**
	 * 账户管理导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountDeductRecordView>
	 */
	public List<Branch> getViewByBranchList(List<Branch> clist) {
		List<Branch> viewList = new ArrayList<Branch>();
		if (clist != null && !clist.isEmpty()) {
			for (Branch o : clist) {
				BigDecimal credituse = o.getCredit().add(o.getBalance()).subtract(o.getDebt());
				o.setCredituse(credituse);// 可用额度
				viewList.add(o);
			}
		}
		return viewList;
	}
}
