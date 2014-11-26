package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.AccountCwbDetailDAO;
import cn.explink.dao.AccountCwbSummaryDAO;
import cn.explink.dao.AccountFeeDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.AccountCwbDetail;
import cn.explink.domain.AccountCwbDetailView;
import cn.explink.domain.AccountCwbSummary;
import cn.explink.domain.AccountFeeDetail;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.AccountTongjiEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.StringUtil;

@Service
public class AccountCwbDetailService {
	@Autowired
	AccountCwbDetailDAO accountCwbDetailDAO;

	@Autowired
	AccountFeeDetailDAO accountFeeDetailDAO;

	@Autowired
	AccountCwbSummaryDAO accountCwbSummaryDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	ExportService exportService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	private Logger logger = LoggerFactory.getLogger(AccountCwbDetailService.class);

	/**
	 * 中转货款、退货退款处理计算
	 * 
	 * @param long branchid
	 * @return Map
	 */
	public Map getAccountCwbByZZTHMap(long branchid) {
		Map typeMap = new HashMap();
		try {
			// 中转货款
			String chukuIds = AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();

			List<AccountCwbDetail> listZZ = accountCwbDetailDAO.getAccountCwbByYJList(branchid, chukuIds);
			typeMap = this.getDetailByZZTH(listZZ, typeMap, AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue());
			// 退货退款
			List<AccountCwbDetail> listTH = accountCwbDetailDAO.getAccountCwbDetailByFlow(branchid, AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue(), 0, 0);
			typeMap = this.getDetailByZZTH(listTH, typeMap, AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue());
			// POS退款
			List<AccountCwbDetail> listPos = accountCwbDetailDAO.getAccountCwbDetailByFlow(branchid, AccountFlowOrderTypeEnum.Pos.getValue(), 0, 0);
			typeMap = this.getDetailByZZTH(listPos, typeMap, AccountFlowOrderTypeEnum.Pos.getValue());
		} catch (Exception e) {
			logger.info("结算中转货款、退货退款、pos退款处理计算异常：" + e);
			e.printStackTrace();
		}
		return typeMap;
	}

	private Map getDetailByZZTH(List<AccountCwbDetail> list, Map typeMap, long rukuType) {
		// Map map = typeMap;
		int nums = 0;// 数量
		String cwbs = "";// 订单号
		BigDecimal fees = BigDecimal.ZERO;// 金额
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				nums += 1;
				// 订单号,号拼接处理
				if (i == (list.size() - 1)) {
					cwbs += list.get(i).getAccountcwbid();
				} else {
					cwbs += list.get(i).getAccountcwbid() + ",";
				}
				fees = fees.add(list.get(i).getPaybackfee());
				// if(rukuType==AccountFlowOrderTypeEnum.Pos.getValue()){
				// fees=fees.add(list.get(i).getPos());
				// }else{//中转、退货
				// //上门退成功 fee=paybackfee
				// if(list.get(i).getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// if(list.get(i).getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
				// fees=fees.add(list.get(i).getPaybackfee());
				// }else{
				// fees=fees.add(list.get(i).getReceivablefee());
				// }
				// }//上门换成功 fee=paybackfee+receivablefee 否则 fee=receivablefee
				// else
				// if(list.get(i).getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
				// if(list.get(i).getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
				// fees=fees.add(list.get(i).getPaybackfee().add(list.get(i).getReceivablefee()));
				// }else{
				// fees=fees.add(list.get(i).getReceivablefee());
				// }
				// }else{//配送||其他
				// fees=fees.add(list.get(i).getReceivablefee());
				// }
				// }
			}
		}
		// 中转
		if (rukuType == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()) {
			typeMap.put(AccountTongjiEnum.ZhongZhuanFees.getValue(), fees);
			typeMap.put(AccountTongjiEnum.ZhongZhuanNums.getValue(), nums);
			typeMap.put(AccountTongjiEnum.ZhongZhuanCwbs.getValue(), cwbs);
		}
		// 退货
		if (rukuType == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue()) {
			typeMap.put(AccountTongjiEnum.TuiHuoFees.getValue(), fees);
			typeMap.put(AccountTongjiEnum.TuiHuoNums.getValue(), nums);
			typeMap.put(AccountTongjiEnum.TuiHuoCwbs.getValue(), cwbs);
		}
		// POS
		if (rukuType == AccountFlowOrderTypeEnum.Pos.getValue()) {
			typeMap.put(AccountTongjiEnum.PosFees.getValue(), fees);
			typeMap.put(AccountTongjiEnum.PosNums.getValue(), nums);
			typeMap.put(AccountTongjiEnum.PosCwbs.getValue(), cwbs);
		}
		return typeMap;
	}

	/**
	 * 加减款处理
	 * 
	 * @param typeMap
	 * @param list
	 * @return
	 */
	public Map getAccountCustomFee(Map typeMap, List<AccountFeeDetail> list) {
		BigDecimal addFees = BigDecimal.ZERO;// 加款
		BigDecimal subFees = BigDecimal.ZERO;// 加款
		String jiakuanIds = "";
		String jiankuanIds = "";
		int addNums = 0;
		int subNums = 0;
		try {
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getFeetype() == 1) {// 加款
						addNums++;
						addFees = addFees.add(list.get(i).getCustomfee());
						jiakuanIds += list.get(i).getFeedetailid() + ",";
					} else {// 减款
						subNums++;
						subFees = subFees.add(list.get(i).getCustomfee());
						jiankuanIds += list.get(i).getFeedetailid() + ",";
					}
				}
			}
			typeMap.put(AccountTongjiEnum.JiaKuan.getValue(), addFees);
			typeMap.put(AccountTongjiEnum.JianKuan.getValue(), subFees);
			typeMap.put(AccountTongjiEnum.JiaKuanNums.getValue(), addNums);
			typeMap.put(AccountTongjiEnum.JianKuanNums.getValue(), subNums);
			if (!"".equals(jiakuanIds)) {
				typeMap.put(AccountTongjiEnum.JiaKuanCwbs.getValue(), jiakuanIds.substring(0, (jiakuanIds.length() - 1)));
			} else {
				typeMap.put(AccountTongjiEnum.JiaKuanCwbs.getValue(), jiakuanIds);
			}
			if (!"".equals(jiankuanIds)) {
				typeMap.put(AccountTongjiEnum.JianKuanCwbs.getValue(), jiankuanIds.substring(0, (jiankuanIds.length() - 1)));
			} else {
				typeMap.put(AccountTongjiEnum.JianKuanCwbs.getValue(), jiankuanIds);
			}
			return typeMap;
		} catch (Exception e) {
			logger.info("结算加减款处理异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 应交货款合计金额处理
	 * 
	 * @param list
	 * @param typeMap
	 * @param checkoutstate
	 * @return Map
	 */
	public Map getAccountCwbByYJQKMap(List<AccountCwbDetail> list, Map typeMap, long checkoutstate) {
		BigDecimal fee = BigDecimal.ZERO;// 加款
		int nums = 0;// 数量
		String ids = "";
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				nums += 1;
				AccountCwbDetail acd = list.get(i);
				fee = fee.add(acd.getReceivablefee());// 代收货款
				if (i == list.size() - 1) {
					ids += acd.getAccountcwbid();
				} else {
					ids += acd.getAccountcwbid() + ",";
				}
			}
		}
		if (checkoutstate == 0) {// 应交款
			typeMap.put(AccountTongjiEnum.YingJiao.getValue(), fee);
			typeMap.put(AccountTongjiEnum.YingJiaoNums.getValue(), nums);
		}
		if (checkoutstate == 1) {// 欠款
			typeMap.put(AccountTongjiEnum.QianKuan.getValue(), fee);
			typeMap.put(AccountTongjiEnum.QianKuanNums.getValue(), nums);
			typeMap.put(AccountTongjiEnum.QianKuanCwbs.getValue(), ids);
		}
		if (checkoutstate == 2) {// 二级妥投交款
			typeMap.put(AccountTongjiEnum.YingJiaoEjCash.getValue(), fee);
			typeMap.put(AccountTongjiEnum.YingJiaoEjNums.getValue(), nums);
		}
		return typeMap;
	}

	/**
	 * 库房先付保存
	 * 
	 * @param request
	 * @param user
	 * @param branchid
	 */
	@Transactional
	public long createoutwarehouse(HttpServletRequest request, User user, long branchid) {
		logger.info("创建结算库房先付业务类型开始");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
		String jiakuanStr = StringUtil.nullConvertToEmptyString(request.getParameter("jiakuanStr"));
		String jiankuanStr = StringUtil.nullConvertToEmptyString(request.getParameter("jiankuanStr"));
		String zzStr = StringUtil.nullConvertToEmptyString(request.getParameter("zzStr"));
		String thStr = StringUtil.nullConvertToEmptyString(request.getParameter("thStr"));
		String posStr = StringUtil.nullConvertToEmptyString(request.getParameter("posStr"));
		BigDecimal qkfee = new BigDecimal("".equals(request.getParameter("qkfee")) ? "0" : request.getParameter("qkfee"));// 欠款金额
		BigDecimal otheraddfee = new BigDecimal("".equals(request.getParameter("otheraddfee")) ? "0" : request.getParameter("otheraddfee"));// 加款金额
		BigDecimal othersubtractfee = new BigDecimal("".equals(request.getParameter("othersubtractfee")) ? "0" : request.getParameter("othersubtractfee"));// 减款金额
		BigDecimal zzcash = new BigDecimal("".equals(request.getParameter("zzcash")) ? "0" : request.getParameter("zzcash"));
		BigDecimal thcash = new BigDecimal("".equals(request.getParameter("thcash")) ? "0" : request.getParameter("thcash"));
		BigDecimal poscash = new BigDecimal("".equals(request.getParameter("poscash")) ? "0" : request.getParameter("poscash"));

		accountCwbSummary.setBranchid(branchid);
		accountCwbSummary.setCheckoutstate(0);
		accountCwbSummary.setAccounttype(Long.parseLong("".equals(request.getParameter("accounttype")) ? "0" : request.getParameter("accounttype")));
		accountCwbSummary.setSavecreatetime(df.format(new Date()));
		accountCwbSummary.setSaveuserid(user.getUserid());
		accountCwbSummary.setQknums(Long.parseLong("".equals(request.getParameter("qknums")) ? "0" : request.getParameter("qknums")));
		accountCwbSummary.setQkcash(qkfee);
		accountCwbSummary.setOtheraddfee(otheraddfee);
		accountCwbSummary.setOthersubtractfee(othersubtractfee);
		accountCwbSummary.setZznums(Long.parseLong("".equals(request.getParameter("zznums")) ? "0" : request.getParameter("zznums")));
		accountCwbSummary.setThnums(Long.parseLong("".equals(request.getParameter("thnums")) ? "0" : request.getParameter("thnums")));
		accountCwbSummary.setPosnums(Long.parseLong("".equals(request.getParameter("posnums")) ? "0" : request.getParameter("posnums")));
		accountCwbSummary.setZzcash(zzcash);
		accountCwbSummary.setThcash(thcash);
		accountCwbSummary.setPoscash(poscash);
		accountCwbSummary.setTocash(new BigDecimal("".equals(request.getParameter("tocash")) ? "0" : request.getParameter("tocash")));
		accountCwbSummary.setTonums(Long.parseLong("".equals(request.getParameter("tonums")) ? "0" : request.getParameter("tonums")));
		accountCwbSummary.setHjfee(new BigDecimal("".equals(request.getParameter("hjFee")) ? "0" : request.getParameter("hjFee")));
		accountCwbSummary.setOtheraddnums(Long.parseLong("".equals(request.getParameter("otheraddnums")) ? "0" : request.getParameter("otheraddnums")));
		accountCwbSummary.setOthersubnums(Long.parseLong("".equals(request.getParameter("othersubnums")) ? "0" : request.getParameter("othersubnums")));

		boolean checkInsert = false;// 检查是否重复插入

		// 已交货款、已交订单数
		String yjOnStr = StringUtil.nullConvertToEmptyString(request.getParameter("yjOnStr"));
		if (!"".equals(yjOnStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(yjOnStr);
			String[] yjOnLen = yjOnStr.split(",");
			accountCwbSummary.setYjnums(yjOnLen.length);
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(yjOnStr);
			accountCwbSummary.setYjcash(o.getReceivablefee());
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
			logger.info("用户：{},创建结算库房先付业务类型：已交货款金额：{},订单ids：{}", new Object[] { user.getRealname(), o.getReceivablefee(), yjOnStr });
		} else {
			accountCwbSummary.setYjnums(0);
			accountCwbSummary.setYjcash(new BigDecimal("0"));
		}

		// 未交货款、未交订单数
		String yjOffStr = StringUtil.nullConvertToEmptyString(request.getParameter("yjOffStr"));
		if (!"".equals(yjOffStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(yjOffStr);
			String[] yjOffLen = yjOffStr.split(",");
			accountCwbSummary.setWjnums(yjOffLen.length);
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(yjOffStr);
			accountCwbSummary.setWjcash(o.getReceivablefee());
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
			logger.info("用户：{},创建结算库房先付业务类型：未交货款金额：{},订单ids：{}", new Object[] { user.getRealname(), o.getReceivablefee(), yjOffLen });
		} else {
			accountCwbSummary.setWjnums(0);
			accountCwbSummary.setWjcash(new BigDecimal("0"));
		}

		// 欠款 欠款订单数
		String qkStr = StringUtil.nullConvertToEmptyString(request.getParameter("qkStr"));
		if (!"".equals(qkStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(qkStr);
			logger.info("验证欠款");
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(qkStr);
			// 和前台验证欠款金额
			if (o.getReceivablefee().compareTo(qkfee) != 0) {
				logger.info("用户：{},创建结算库房先付业务类型异常：欠款订单ids:{},欠款金额：{},实际欠款金额为：{}", new Object[] { user.getRealname(), qkStr, qkfee, o.getReceivablefee() });
				throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanQianKuan, user.getRealname());
			}
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() > 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
			logger.info("验证欠款结束");
		}

		long summaryid = accountCwbSummaryDAO.createAccountCwbSummary(accountCwbSummary);
		logger.info("用户:{},创建结算库房先付业务类型：创建审核信息，id：{}", new Object[] { user.getRealname(), summaryid });

		if (!"".equals(jiakuanStr) || !"".equals(jiankuanStr)) {
			logger.info("结算加款：" + jiakuanStr);
			logger.info("结算减款：" + jiankuanStr);
			String s1 = this.returnAccountCwbs(jiakuanStr, jiankuanStr);

			// 锁住
			List<AccountFeeDetail> acdList = accountFeeDetailDAO.getAccountFeeDetailLock(s1);

			// 验证重复提交
			if (accountFeeDetailDAO.getAccountFeeBySummaryidSum(s1) == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}

			if (!"".equals(jiakuanStr)) {
				AccountFeeDetail o = new AccountFeeDetail();
				o = accountFeeDetailDAO.getAccountFeeByCustomfeeSum(jiakuanStr);
				// 和前台验证加款
				if (o.getCustomfee().compareTo(otheraddfee) != 0) {
					logger.info("用户：{},创建结算库房先付业务类型异常：加款订单ids:{},加款金额：{},实际加款金额为：{}", new Object[] { user.getRealname(), jiakuanStr, otheraddfee, o.getCustomfee() });
					throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanJiaKuan, user.getRealname());
				}
			}

			if (!"".equals(jiankuanStr)) {
				AccountFeeDetail o1 = new AccountFeeDetail();
				o1 = accountFeeDetailDAO.getAccountFeeByCustomfeeSum(jiankuanStr);
				// 和前台验证减款
				if (o1.getCustomfee().compareTo(othersubtractfee) != 0) {
					logger.info("用户：{},创建结算库房先付业务类型异常：减款订单ids:{},减款金额：{},实际减款金额为：{}", new Object[] { user.getRealname(), jiankuanStr, othersubtractfee, o1.getCustomfee() });
					throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanJianKuan, user.getRealname());
				}
			}
			accountFeeDetailDAO.updateAccountFeeDetailByCheckout(summaryid, s1);
			logger.info("用户:{},创建结算库房先付业务类型：结算加款、减款id：{}，审核信息id{}", new Object[] { user.getRealname(), s1, summaryid });
		}

		if (!"".equals(zzStr) || !"".equals(thStr) || !"".equals(yjOnStr) || !"".equals(qkStr) || !"".equals(posStr)) {
			logger.info("结算中转退款：" + zzStr);
			logger.info("结算退货退款：" + thStr);
			logger.info("结算已交：" + yjOnStr);
			logger.info("结算欠款：" + qkStr);
			String s1 = this.returnAccountCwbs(zzStr, thStr);
			String s2 = this.returnAccountCwbs(yjOnStr, qkStr);

			String s4 = this.returnAccountCwbs(s1, posStr);

			String s3 = this.returnAccountCwbs(s4, s2);
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(s4);
			if (!"".equals(s1)) {
				AccountCwbDetail o = new AccountCwbDetail();
				// 验证重复提交
				o = accountCwbDetailDAO.getReceivablefeeSum(s4);
				if (o.getCheckoutstate() == 0 && o.getDebetstate() == 0) {
					checkInsert = true;
				} else {
					checkInsert = false;
				}
			}
			accountCwbDetailDAO.updateAccountCwbDetailByCheckout(summaryid, s3);
			logger.info("用户:{},创建结算库房先付业务类型：中转退款、退货退款、pos退款id:{}；已交货款、欠款id：{}，审核信息id{}", new Object[] { user.getRealname(), s4, s2, summaryid });
		}

		if (!"".equals(yjOffStr)) {
			logger.info("结算未交：" + yjOffStr);
			accountCwbDetailDAO.updateAccountCwbDetailByDebetstate(summaryid, yjOffStr);
			logger.info("用户:{},创建结算库房先付业务类型：未交货款结算id:{}，审核信息id{}", new Object[] { user.getRealname(), yjOffStr, summaryid });
		}

		if (checkInsert == false) {
			logger.info("创建结算库房先付业务类型异常：重复提交,用户：" + user.getRealname());
			throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanChongFuTiJiao, user.getRealname());
		}

		logger.info("创建结算库房先付业务类型结束,summaryid{}", new Object[] { summaryid });
		return summaryid;
	}

	@Transactional
	public void outwarehouseDelete(long summaryid, User user) {
		accountCwbSummaryDAO.deleteAccountCwbSummarById(summaryid);
		accountCwbDetailDAO.updateAccountCwbDetailDelete(summaryid);
		accountFeeDetailDAO.updateAccountFeeBySummaryid(summaryid);
		logger.info("用户:{},结算库房先付业务类型：撤销信息id{}", new Object[] { user.getRealname(), summaryid });
	}

	public void updateoutwarehouse(HttpServletRequest request, User user, long checkoutstate, long summaryid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
		accountCwbSummary = accountCwbSummaryDAO.getAccountCwbSummaryById(summaryid);

		if (checkoutstate == 0) {// 未审核只保存
			accountCwbSummary.setSavecreatetime(df.format(new Date()));
			accountCwbSummary.setSaveuserid(user.getUserid());
		} else {// 审核
			accountCwbSummary.setCreatetime(df.format(new Date()));
			accountCwbSummary.setUserid(user.getUserid());
		}
		BigDecimal feetransfer = BigDecimal.valueOf(Float.parseFloat("".equals(request.getParameter("feetransfer")) ? "0" : request.getParameter("feetransfer")));
		BigDecimal feecash = BigDecimal.valueOf(Float.parseFloat("".equals(request.getParameter("feecash")) ? "0" : request.getParameter("feecash")));
		BigDecimal feepos = BigDecimal.valueOf(Float.parseFloat("".equals(request.getParameter("feepos")) ? "0" : request.getParameter("feepos")));
		BigDecimal feecheck = BigDecimal.valueOf(Float.parseFloat("".equals(request.getParameter("feecheck")) ? "0" : request.getParameter("feecheck")));
		accountCwbSummary.setSummaryid(summaryid);
		accountCwbSummary.setCheckoutstate(checkoutstate);
		accountCwbSummary.setMemo(StringUtil.nullConvertToEmptyString(request.getParameter("memo")));
		accountCwbSummary.setFeetransfer(feetransfer);
		accountCwbSummary.setFeecash(feecash);
		accountCwbSummary.setFeepos(feepos);
		accountCwbSummary.setFeecheck(feecheck);
		accountCwbSummary.setUsertransfer(StringUtil.nullConvertToEmptyString(request.getParameter("usertransfer")));
		accountCwbSummary.setUsercash(StringUtil.nullConvertToEmptyString(request.getParameter("usercash")));
		accountCwbSummary.setUserpos(StringUtil.nullConvertToEmptyString(request.getParameter("userpos")));
		accountCwbSummary.setUsercheck(StringUtil.nullConvertToEmptyString(request.getParameter("usercheck")));
		accountCwbSummary.setCardtransfer(StringUtil.nullConvertToEmptyString(request.getParameter("cardtransfer")));
		accountCwbSummaryDAO.saveAccountCwbSummaryByCheck(accountCwbSummary);
		logger.info("用户:{},结算业务更新审核信息id:{},转账:{}；现金:{}；POS:{}；支票:{}；", new Object[] { user.getRealname(), summaryid, feetransfer, feecash, feepos, feecheck });

	}

	/**
	 * 已妥投交款金额处理
	 * 
	 * @param list
	 * @param typeMap
	 * @param checkoutstate
	 * @return Map
	 */
	public Map getAccountCwbByYTTMap(List<AccountCwbDetail> list, Map typeMap, long checkoutstate) {
		BigDecimal cash = BigDecimal.ZERO;
		BigDecimal pos = BigDecimal.ZERO;
		BigDecimal checkfee = BigDecimal.ZERO;
		BigDecimal otherfee = BigDecimal.ZERO;

		int nums = 0;// 数量
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				nums += 1;
				AccountCwbDetail acd = list.get(i);
				cash = cash.add(acd.getCash());
				pos = pos.add(acd.getPos());
				checkfee = checkfee.add(acd.getCheckfee());
				otherfee = otherfee.add(acd.getOtherfee());
			}
		}
		if (checkoutstate == 0) {// 应交款
			typeMap.put(AccountTongjiEnum.YingJiaoCash.getValue(), cash);
			typeMap.put(AccountTongjiEnum.YingJiaoPos.getValue(), pos);
			typeMap.put(AccountTongjiEnum.YingJiaoCheck.getValue(), checkfee);
			typeMap.put(AccountTongjiEnum.YingJiaoOther.getValue(), otherfee);
			typeMap.put(AccountTongjiEnum.YingJiaoNums.getValue(), nums);
		}
		if (checkoutstate == 1) {// 欠款
			BigDecimal qiankuanFee = BigDecimal.ZERO;
			qiankuanFee = qiankuanFee.add(cash);
			// qiankuanFee=qiankuanFee.add(pos);
			qiankuanFee = qiankuanFee.add(checkfee);
			qiankuanFee = qiankuanFee.add(otherfee);
			typeMap.put(AccountTongjiEnum.QianKuan.getValue(), qiankuanFee);
			typeMap.put(AccountTongjiEnum.QianKuanNums.getValue(), nums);
		}
		if (checkoutstate == 2) {// 二级妥投交款
			typeMap.put(AccountTongjiEnum.YingJiaoEjCash.getValue(), cash);
			typeMap.put(AccountTongjiEnum.YingJiaoEjPos.getValue(), pos);
			typeMap.put(AccountTongjiEnum.YingJiaoEjCheck.getValue(), checkfee);
			typeMap.put(AccountTongjiEnum.YingJiaoEjOther.getValue(), otherfee);
			typeMap.put(AccountTongjiEnum.YingJiaoEjNums.getValue(), nums);
		}
		return typeMap;
	}

	private String returnAccountCwbs(String valA, String valB) {
		String str = "''";
		if (!"".equals(valA) && !"".equals(valB)) {
			str = valA + "," + valB;
		}
		if ("".equals(valA) && !"".equals(valB)) {
			str = valB;
		}
		if (!"".equals(valA) && "".equals(valB)) {
			str = valA;
		}
		return str;
	}

	/**
	 * 结算后付保存
	 * 
	 * @param request
	 * @param user
	 * @param branchid
	 */
	@Transactional
	public long createDelivery(HttpServletRequest request, User user, long branchid) {
		logger.info("创建结算库房后付业务类型开始");
		long summaryid = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
		String jiakuanStr = StringUtil.nullConvertToEmptyString(request.getParameter("jiakuanStr"));
		String jiankuanStr = StringUtil.nullConvertToEmptyString(request.getParameter("jiankuanStr"));
		BigDecimal otheraddfee = new BigDecimal("".equals(request.getParameter("otheraddfee")) ? "0" : request.getParameter("otheraddfee"));// 加款金额
		BigDecimal othersubtractfee = new BigDecimal("".equals(request.getParameter("othersubtractfee")) ? "0" : request.getParameter("othersubtractfee"));// 减款金额

		accountCwbSummary.setBranchid(branchid);
		accountCwbSummary.setCheckoutstate(Long.parseLong("".equals(request.getParameter("checkoutstate")) ? "0" : request.getParameter("checkoutstate")));
		accountCwbSummary.setAccounttype(Long.parseLong("".equals(request.getParameter("accounttype")) ? "0" : request.getParameter("accounttype")));
		accountCwbSummary.setSavecreatetime(df.format(new Date()));
		accountCwbSummary.setSaveuserid(user.getUserid());
		accountCwbSummary.setFeetransfer(new BigDecimal("".equals(request.getParameter("feetransfer")) ? "0" : request.getParameter("feetransfer")));
		accountCwbSummary.setFeecash(new BigDecimal("".equals(request.getParameter("feecash")) ? "0" : request.getParameter("feecash")));
		accountCwbSummary.setFeepos(new BigDecimal("".equals(request.getParameter("feepos")) ? "0" : request.getParameter("feepos")));
		accountCwbSummary.setFeecheck(new BigDecimal("".equals(request.getParameter("feecheck")) ? "0" : request.getParameter("feecheck")));
		accountCwbSummary.setUsertransfer(StringUtil.nullConvertToEmptyString(request.getParameter("usertransfer")));
		accountCwbSummary.setUsercash(StringUtil.nullConvertToEmptyString(request.getParameter("usercash")));
		accountCwbSummary.setUserpos(StringUtil.nullConvertToEmptyString(request.getParameter("userpos")));
		accountCwbSummary.setUsercheck(StringUtil.nullConvertToEmptyString(request.getParameter("usercheck")));
		accountCwbSummary.setCardtransfer(StringUtil.nullConvertToEmptyString(request.getParameter("cardtransfer")));
		accountCwbSummary.setOtheraddfee(otheraddfee);
		accountCwbSummary.setOthersubtractfee(othersubtractfee);
		BigDecimal tocash = new BigDecimal("".equals(request.getParameter("tocash")) ? "0" : request.getParameter("tocash"));
		BigDecimal topos = new BigDecimal("".equals(request.getParameter("topos")) ? "0" : request.getParameter("topos"));
		BigDecimal tocheck = new BigDecimal("".equals(request.getParameter("tocheck")) ? "0" : request.getParameter("tocheck"));
		BigDecimal toother = new BigDecimal("".equals(request.getParameter("toother")) ? "0" : request.getParameter("toother"));
		accountCwbSummary.setTocash(tocash);
		accountCwbSummary.setTopos(topos);
		accountCwbSummary.setTocheck(tocheck);
		accountCwbSummary.setToother(toother);
		// 共交合计计算
		BigDecimal tofee = tocash.add(topos.add(tocheck.add(toother)));
		accountCwbSummary.setTofee(tofee);// 共交合计
		accountCwbSummary.setTonums(Long.parseLong("".equals(request.getParameter("tonums")) ? "0" : request.getParameter("tonums")));

		accountCwbSummary.setOtheraddnums(Long.parseLong("".equals(request.getParameter("otheraddnums")) ? "0" : request.getParameter("otheraddnums")));
		accountCwbSummary.setOthersubnums(Long.parseLong("".equals(request.getParameter("othersubnums")) ? "0" : request.getParameter("othersubnums")));
		accountCwbSummary.setHjfee(new BigDecimal("".equals(request.getParameter("hjFee")) ? "0" : request.getParameter("hjFee")));// 合计应支付

		boolean checkInsert = false;// 检查是否重复插入

		// 已妥投货款、已妥投订单数
		String yjOnStr = StringUtil.nullConvertToEmptyString(request.getParameter("yjOnStr"));
		if (!"".equals(yjOnStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(yjOnStr);
			String[] yjOnLen = yjOnStr.split(",");
			accountCwbSummary.setYjnums(yjOnLen.length);
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(yjOnStr);
			accountCwbSummary.setYjcash(o.getCash());
			accountCwbSummary.setYjpos(o.getPos());
			accountCwbSummary.setYjcheck(o.getCheckfee());
			accountCwbSummary.setYjother(o.getOtherfee());
			BigDecimal yjfee = o.getCash().add(o.getPos().add(o.getCheckfee().add(o.getOtherfee())));
			accountCwbSummary.setYjfee(yjfee);// 应交合计
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
		} else {
			accountCwbSummary.setYjnums(0);
			accountCwbSummary.setYjcash(new BigDecimal("0"));
			accountCwbSummary.setYjpos(new BigDecimal("0"));
			accountCwbSummary.setYjcheck(new BigDecimal("0"));
			accountCwbSummary.setYjother(new BigDecimal("0"));
			accountCwbSummary.setYjfee(new BigDecimal("0"));
		}

		// 未交货款、未交订单数
		String yjOffStr = StringUtil.nullConvertToEmptyString(request.getParameter("yjOffStr"));
		if (!"".equals(yjOffStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(yjOffStr);
			String[] yjOffLen = yjOffStr.split(",");
			accountCwbSummary.setWjnums(yjOffLen.length);
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(yjOffStr);
			accountCwbSummary.setWjcash(o.getCash());
			accountCwbSummary.setWjpos(o.getPos());
			accountCwbSummary.setWjcheck(o.getCheckfee());
			accountCwbSummary.setWjother(o.getOtherfee());
			BigDecimal wjfee = o.getCash().add(o.getPos().add(o.getCheckfee().add(o.getOtherfee())));
			accountCwbSummary.setWjfee(wjfee);// 未交合计
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
		} else {
			accountCwbSummary.setWjnums(0);
			accountCwbSummary.setWjcash(new BigDecimal("0"));
			accountCwbSummary.setWjpos(new BigDecimal("0"));
			accountCwbSummary.setWjcheck(new BigDecimal("0"));
			accountCwbSummary.setWjother(new BigDecimal("0"));
			accountCwbSummary.setWjfee(new BigDecimal("0"));
		}

		// 欠款
		BigDecimal qkfee = new BigDecimal("".equals(request.getParameter("qkfee")) ? "0" : request.getParameter("qkfee"));
		String qkStr = StringUtil.nullConvertToEmptyString(request.getParameter("qkStr"));
		accountCwbSummary.setQkcash(qkfee);
		accountCwbSummary.setQknums(Long.parseLong("".equals(request.getParameter("qknums")) ? "0" : request.getParameter("qknums")));

		if (!"".equals(qkStr)) {
			// 锁住
			List<AccountCwbDetail> acdList = accountCwbDetailDAO.getAccountCwbDetailLock(qkStr);
			logger.info("验证欠款");
			AccountCwbDetail o = new AccountCwbDetail();
			o = accountCwbDetailDAO.getReceivablefeeSum(qkStr);
			// 和前台验证欠款金额
			if (qkfee.compareTo(o.getCash().add(o.getCheckfee().add(o.getOtherfee()))) != 0) {
				logger.info("用户：{},创建结算后付业务类型异常：欠款订单ids:{},欠款金额：{},实际欠款金额为：{}",
						new Object[] { user.getRealname(), qkStr, qkfee, o.getCash().add(o.getPos().add(o.getCheckfee().add(o.getOtherfee()))) });
				throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanQianKuan, user.getRealname());
			}
			// 验证重复提交
			if (o.getCheckoutstate() == 0 && o.getDebetstate() > 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
			logger.info("验证欠款结束");
		}

		summaryid = accountCwbSummaryDAO.createAccountCwbSummary(accountCwbSummary);
		logger.info("用户:{},创建结算后付业务类型审核信息id{}", new Object[] { user.getRealname(), summaryid });

		if (!"".equals(jiakuanStr) || !"".equals(jiankuanStr)) {
			logger.info("结算加款" + jiakuanStr);
			logger.info("结算减款" + jiankuanStr);
			String s1 = this.returnAccountCwbs(jiakuanStr, jiankuanStr);
			// 锁住
			List<AccountFeeDetail> acdList = accountFeeDetailDAO.getAccountFeeDetailLock(s1);

			// 验证重复提交
			if (accountFeeDetailDAO.getAccountFeeBySummaryidSum(s1) == 0) {
				checkInsert = true;
			} else {
				checkInsert = false;
			}
			if (!"".equals(jiakuanStr)) {
				AccountFeeDetail o = new AccountFeeDetail();
				o = accountFeeDetailDAO.getAccountFeeByCustomfeeSum(jiakuanStr);
				// 和前台验证加款
				if (o.getCustomfee().compareTo(otheraddfee) != 0) {
					logger.info("用户：{},创建结算后付业务类型异常：加款订单ids:{},加款金额：{},实际加款金额为：{}", new Object[] { user.getRealname(), jiakuanStr, otheraddfee, o.getCustomfee() });
					throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanJiaKuan, user.getRealname());
				}
			}

			if (!"".equals(jiankuanStr)) {
				AccountFeeDetail o1 = new AccountFeeDetail();
				o1 = accountFeeDetailDAO.getAccountFeeByCustomfeeSum(jiankuanStr);
				// 和前台验证减款
				if (o1.getCustomfee().compareTo(othersubtractfee) != 0) {
					logger.info("用户：{},创建结算后付业务类型异常：减款订单ids:{},减款金额：{},实际减款金额为：{}", new Object[] { user.getRealname(), jiankuanStr, othersubtractfee, o1.getCustomfee() });
					throw new CwbException(qkStr, 0, ExceptionCwbErrorTypeEnum.JieSuanJianKuan, user.getRealname());
				}
			}

			accountFeeDetailDAO.updateAccountFeeDetailByCheckout(summaryid, s1);
			logger.info("用户:{},结算后付业务类型：结算加款、减款id：{}，审核信息id{}", new Object[] { user.getRealname(), s1, summaryid });
		}

		if (checkInsert == false) {
			logger.info("创建结算后付业务类型异常：重复提交,用户：" + user.getRealname());
			throw new CwbException("", 0, ExceptionCwbErrorTypeEnum.JieSuanChongFuTiJiao, user.getRealname());
		}

		if (!"".equals(yjOnStr) || !"".equals(qkStr)) {
			logger.info("结算应交" + yjOnStr);
			logger.info("结算欠款" + qkStr);
			accountCwbDetailDAO.updateAccountCwbDetailByCheckout(summaryid, this.returnAccountCwbs(yjOnStr, qkStr));
			logger.info("用户:{},结算后付业务类型：已交货款、欠款id：{}，审核信息id{}", new Object[] { user.getRealname(), this.returnAccountCwbs(yjOnStr, qkStr), summaryid });
		}

		if (!"".equals(yjOffStr)) {
			logger.info("结算未交" + yjOffStr);
			accountCwbDetailDAO.updateAccountCwbDetailByDebetstate(summaryid, yjOffStr);
			logger.info("用户:{},结算后付业务类型：未妥投货款id：{}，审核信息id{}", new Object[] { user.getRealname(), yjOffStr, summaryid });
		}
		logger.info("创建结算后付业务类型结束");
		return summaryid;
	}

	/**
	 * 后付审核
	 * 
	 * @param request
	 * @param user
	 */
	public void updateDelivery(HttpServletRequest request, User user) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long branchid = Long.parseLong(request.getParameter("branchid") == null ? "0" : request.getParameter("branchid"));
		long summaryid = Long.parseLong(request.getParameter("summaryid") == null ? "0" : request.getParameter("summaryid"));
		Branch branch = new Branch();
		branch = branchDAO.getBranchByBranchid(branchid);
		long accountBranch = branch.getAccountbranch();

		// 判断是否是财务部 若不是财务部 则将审核的订单明细信息复制到当前站点
		Branch acountBranch = new Branch();
		acountBranch = branchDAO.getBranchByBranchid(accountBranch);
		if (acountBranch.getSitetype() != BranchEnum.CaiWu.getValue()) {
			List<AccountCwbDetail> list = accountCwbDetailDAO.getAccountCwbByBranch(branchid, summaryid);
			if (list != null && !list.isEmpty()) {
				for (AccountCwbDetail o : list) {
					o.setBranchid(accountBranch);
					o.setAccountbranch(branchid);
					o.setCreatetime(df.format(new Date()));
					o.setUserid(user.getUserid());
					o.setCheckoutstate(0);
					o.setDebetstate(0);
					accountCwbDetailDAO.createAccountCwbDetail(o);
					logger.info("用户:{},结算业务类型：将站点id:{}的订单id:{},提交至结算对象站点id:{}", new Object[] { user.getRealname(), branchid, o.getCwb(), accountBranch });
				}
			}
		}
	}

	/**
	 * 先付导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountCwbDetailView>
	 */
	public List<AccountCwbDetailView> getViewByDelivery(List<AccountCwbDetail> clist) {
		List<AccountCwbDetailView> viewList = new ArrayList<AccountCwbDetailView>();
		if (clist != null && !clist.isEmpty()) {
			for (AccountCwbDetail acd : clist) {
				AccountCwbDetailView o = new AccountCwbDetailView();
				// if(acd.getCheckoutstate()==0&&acd.getDebetstate()==0&&acd.getAccountbranch()==0){
				// o.setTypename("一级站点已妥投");
				// }
				// if(acd.getCheckoutstate()==0&&acd.getDebetstate()==0&&acd.getAccountbranch()>0){
				// o.setTypename("二级站点已妥投");
				// }
				// if(acd.getCheckoutstate()==0&&acd.getDebetstate()>0){
				// o.setTypename("欠款");
				// }
				o.setCwb(acd.getCwb());
				o.setCwbtype(CwbOrderTypeIdEnum.getByValue((int) acd.getCwbordertypeid()).getText());
				o.setSendcarnum(acd.getSendcarnum());
				o.setScannum(acd.getScannum());
				o.setCaramount(acd.getCaramount());
				o.setReceivablefee(acd.getReceivablefee());
				o.setPaybackfee(acd.getPaybackfee());
				o.setPos(acd.getPos());
				o.setCash(acd.getCash());
				o.setCheckfee(acd.getCheckfee());
				o.setOtherfee(acd.getOtherfee());
				viewList.add(o);
			}
		}
		return viewList;
	}

	/**
	 * 后付导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountCwbDetailView>
	 */
	public List<AccountCwbDetailView> getViewByOutwarehouse(List<AccountCwbDetail> clist) {
		List<AccountCwbDetailView> viewList = new ArrayList<AccountCwbDetailView>();
		if (clist != null && !clist.isEmpty()) {
			for (AccountCwbDetail acd : clist) {
				AccountCwbDetailView o = new AccountCwbDetailView();
				// if(acd.getCheckoutstate()==0&&acd.getDebetstate()==0){
				// o.setTypename("应交货款");
				// }
				// if(acd.getCheckoutstate()==0&&acd.getDebetstate()>0){
				// o.setTypename("欠款");
				// }
				o.setCwb(acd.getCwb());
				o.setCwbtype(CwbOrderTypeIdEnum.getByValue((int) acd.getCwbordertypeid()).getText());
				o.setSendcarnum(acd.getSendcarnum());
				o.setScannum(acd.getScannum());
				o.setCaramount(acd.getCaramount());
				o.setReceivablefee(acd.getReceivablefee());
				o.setPaybackfee(acd.getPaybackfee());
				o.setPos(acd.getPos());
				o.setCash(acd.getCash());
				o.setCheckfee(acd.getCheckfee());
				o.setOtherfee(acd.getOtherfee());
				viewList.add(o);
			}
		}
		return viewList;
	}

	public AccountCwbDetail formForAccountCwbDetail(CwbOrder co, long branchid, long flowordertype, long userid, long currentbranchid) {
		BigDecimal paybackfee = BigDecimal.ZERO;// 应退金额
		if (flowordertype == AccountFlowOrderTypeEnum.Pos.getValue()) {
			DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			paybackfee = deliverystate.getPos();// Pos
		} else {
			// 配送
			if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				paybackfee = co.getReceivablefee();
			}
			// 上门退成功 fee=paybackfee
			if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				if (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					paybackfee = co.getPaybackfee();
				}
			}
			// 上门换成功 fee=paybackfee+receivablefee 否则 fee=receivablefee
			if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				if (co.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
					paybackfee = paybackfee.add(co.getPaybackfee().add(co.getReceivablefee()));
				} else {
					paybackfee = co.getReceivablefee();
				}
			}
		}
		AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
		accountCwbDetail.setPaybackfee(paybackfee);// 应退款
		accountCwbDetail.setBranchid(branchid);// 出库站点ID
		accountCwbDetail.setCustomerid(co.getCustomerid());// 供货商id
		accountCwbDetail.setFlowordertype(flowordertype);// 操作类型
		accountCwbDetail.setDeliverystate(co.getDeliverystate());// 配送结果
		accountCwbDetail.setCheckoutstate(0l);// 结算id
		accountCwbDetail.setDebetstate(0l);// 欠款id
		accountCwbDetail.setCwb(StringUtil.nullConvertToEmptyString(co.getCwb()));// 订单号
		accountCwbDetail.setCwbordertypeid(co.getCwbordertypeid());// 订单类型
		accountCwbDetail.setSendcarnum(co.getSendcarnum());// 发货件数
		accountCwbDetail.setScannum(co.getScannum());// 出库件数
		accountCwbDetail.setCaramount(StringUtil.nullConvertToBigDecimal(co.getCaramount()));// 货物价值
		accountCwbDetail.setReceivablefee(StringUtil.nullConvertToBigDecimal(co.getReceivablefee()));// 代收货款
		accountCwbDetail.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		accountCwbDetail.setUserid(userid);
		accountCwbDetail.setCurrentbranchid(currentbranchid);// 出库站ID中转站ID退货站ID
		accountCwbDetail.setPos(new BigDecimal("0"));
		accountCwbDetail.setCash(new BigDecimal("0"));
		accountCwbDetail.setCheckfee(new BigDecimal("0"));
		accountCwbDetail.setOtherfee(new BigDecimal("0"));
		return accountCwbDetail;
	}

	/**
	 * 退款不同类型订单取不同的金额
	 * 
	 * @param cwbordertypeid
	 *            订单类型
	 * @param deliverystate
	 *            反馈结果
	 * @param receivablefee
	 * @param paybackfee
	 * @return 应退金额
	 */
	public BigDecimal getZZPaybackfee(long cwbordertypeid, long deliverystate, BigDecimal receivablefee, BigDecimal paybackfee) {
		BigDecimal fee = BigDecimal.ZERO;// 应退金额
		// 配送
		if (cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
			fee = receivablefee;
		}
		// 上门退成功 fee=paybackfee
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			if (deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				fee = paybackfee;
			}
		}
		// 上门换成功 fee=paybackfee+receivablefee 否则 fee=receivablefee
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			if (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				fee = fee.add(paybackfee.add(receivablefee));
			} else {
				fee = receivablefee;
			}
		}
		return fee;
	}
}
