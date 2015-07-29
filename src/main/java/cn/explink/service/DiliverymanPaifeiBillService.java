/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.DiliverymanPaifeiBillDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.DiliverymanPaifeiBill;
import cn.explink.domain.DiliverymanPaifeiOrder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DateTypeEnum;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.util.DateTimeUtil;

/**
 * @author wangqiang
 */
@Service
public class DiliverymanPaifeiBillService {
	@Autowired
	private DiliverymanPaifeiBillDAO diliverymanPaifeiBillDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	PaiFeiRuleService paiFeiRuleService;
	private User user;

	/**
	 * 查询账单
	 */
	public List<DiliverymanPaifeiBill> queryDiliverymanPaifeiBill(DiliverymanPaifeiBill bill, Long page) {
		return this.diliverymanPaifeiBillDAO.queryDiliverymanPaifeiBill(bill, page);
	}

	/**
	 * 查询出账单的总记录数
	 */
	public int queryDiliverymanPaifeiBillCount(DiliverymanPaifeiBill bill) {
		return this.diliverymanPaifeiBillDAO.queryDiliverymanPaifeiBillCount(bill);
	}


	/**
	 * 查询指定的账单信息
	 */
	public DiliverymanPaifeiBill queryById(Integer id, Long page) {
		DiliverymanPaifeiBill diliverymanPaifeiBill = this.diliverymanPaifeiBillDAO.queryById(id);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(diliverymanPaifeiBill.getOrderids())) {
			String cwbs = DiliverymanPaifeiBillService.spiltString(diliverymanPaifeiBill.getOrderids());
			List<DiliverymanPaifeiOrder> diliverymanPaifeiOrderList = this.diliverymanPaifeiBillDAO.queryorderdedail(cwbs, page);
			diliverymanPaifeiBill.setDiliverymanPaifeiOrderList(diliverymanPaifeiOrderList);
		}
		return diliverymanPaifeiBill;
	}

	public int queryByIdCount(Integer id) {
		int ordercount = 0;
		DiliverymanPaifeiBill diliverymanPaifeiBill = this.diliverymanPaifeiBillDAO.queryById(id);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(diliverymanPaifeiBill.getOrderids())) {
			String cwbs = DiliverymanPaifeiBillService.spiltString(diliverymanPaifeiBill.getOrderids());
			ordercount = this.diliverymanPaifeiBillDAO.queryorderdedailcount(cwbs);
		}
		return ordercount;
	}

	/**
	 * 根据用户选择的条件生成账单
	 */
	public int createBill(Integer site, Integer orderType, String[] diliveryman, Integer dateType, String startDate, String endDate, String explain) {
		DiliverymanPaifeiBill bill = null;
		Integer billcount = 0;
		// 当用户选择站点，并且选择 了小件员的时候，执行此方法
		if ((diliveryman != null) && (diliveryman.length != 0)) {
			/* 该站点下有多少个小件员就创建多少个账单 */
			for (int i = 0; i < diliveryman.length; i++) {
				bill = new DiliverymanPaifeiBill();
				Integer diliverymanid = Integer.parseInt(diliveryman[i]);
				User deliveryUser=userDAO.getUserByUserid(diliverymanid);
				/* 根据小件员id 查询出该小件员所有的订单 */
				if (dateType == DateTypeEnum.FanKuiRiQi.getValue()) {
					/* 日期类型为反馈时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByDeliveryid(site, orderType, diliverymanid, startDate, endDate);

					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				} else if (dateType == DateTypeEnum.FaHuoRiQi.getValue()) {
					/* 日期类型为发货时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByFaHuo(site, orderType, diliverymanid, startDate, endDate);
					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				} else if (dateType == DateTypeEnum.RuKuRiQi.getValue()) {
					/* 日期类型为入库时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByRuKu(site, orderType, diliverymanid, startDate, endDate);
					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				}
				bill.setBillbatch(this.generateBillBatch());
				bill.setBillstate(PunishBillStateEnum.WeiShenHe.getValue());
				bill.setDiliveryman(diliverymanid);
				bill.setTheirsite(site);
				bill.setBillestablishdate(DateTimeUtil.getNowDate());
				bill.setDaterange(startDate + " 至 " + endDate);
				bill.setOrdertype(orderType);
				bill.setRemarks(explain);
				this.diliverymanPaifeiBillDAO.addDiliverymanBill(bill);
				billcount++;
			}
		} else {
			/* 当用户选择站点，但是没有选择站点所属小件员的时候，执行此方法 */
			List<User> list = this.userDAO.queryAllUserByBranchId(site);
			User user = null;
			for (int i = 0; i < list.size(); i++) {
				bill = new DiliverymanPaifeiBill();
				user = list.get(i);
				Integer userid = (int) user.getUserid();
				User deliveryUser=userDAO.getUserByUserid(userid);
				/* 根据小件员id 查询出该小件员所有的订单 */
				if (dateType == DateTypeEnum.FanKuiRiQi.getValue()) {
					/* 日期类型为反馈时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByDeliveryid(site, orderType, userid, startDate, endDate);
					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				} else if (dateType == DateTypeEnum.FaHuoRiQi.getValue()) {
					/* 日期类型为发货时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByFaHuo(site, orderType, userid, startDate, endDate);
					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				} else if (dateType == DateTypeEnum.RuKuRiQi.getValue()) {
					/* 日期类型为入库时间时执行这个方法 */
					List<DeliveryState> deliveryStateList = this.deliveryStateDao.queryOrderByRuKu(site, orderType, userid, startDate, endDate);
					bill = this.calculatePaFei(bill, deliveryStateList,deliveryUser);
				}
				bill.setBillbatch(this.generateBillBatch());
				bill.setBillstate(PunishBillStateEnum.WeiShenHe.getValue());
				bill.setDiliveryman(userid);
				bill.setTheirsite(site);
				bill.setBillestablishdate(DateTimeUtil.getNowTime());
				bill.setDaterange(startDate + " 至 " + endDate);
				bill.setOrdertype(orderType);
				bill.setRemarks(explain);
				this.diliverymanPaifeiBillDAO.addDiliverymanBill(bill);
				billcount++;
			}

		}
		if(bill == null){
			bill = new DiliverymanPaifeiBill();
		}
		bill.setBillbatch(this.generateBillBatch());
		bill.setBillstate(PunishBillStateEnum.WeiShenHe.getValue());
		return billcount;
	}

	/**
	 * 修改账单
	 */
	public void updateDilivermanBill(DiliverymanPaifeiBill bill) {
		this.diliverymanPaifeiBillDAO.updateDilivermanBill(bill);
	}

	/**
	 * 生成账单编号
	 *
	 * @return
	 */
	public String generateBillBatch() {
		String rule = "B";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMddHHmmssSSS");
		String billBatch = rule + nowTime;
		return billBatch;
	}

	/**
	 * 删除账单
	 */
	public int deletePaiFeiBill(String ids) {
		String[] id1 = ids.split(",");
		int count = 0;
		for (int i = 0; i < id1.length; i++) {
			Integer id = Integer.parseInt(id1[i]);
			DiliverymanPaifeiBill bills = this.diliverymanPaifeiBillDAO.queryById(id);
			String ordernumber = "";
			ordernumber = bills.getOrderids();
			// 删除该账单下所有订单
			if (org.apache.commons.lang3.StringUtils.isNotBlank(ordernumber)) {
				ordernumber = DiliverymanPaifeiBillService.spiltString(ordernumber);
				this.diliverymanPaifeiBillDAO.deleteorder(ordernumber);
				this.deliveryStateDao.setWhetherGenerateDeliveryBill(ordernumber);
			}
			this.diliverymanPaifeiBillDAO.deleteBill(id);
			count ++ ;
		}
		return count;
	}

	/**
	 * 根据查出来的订单信息 再调用相关规则计算派费
	 */
	public DiliverymanPaifeiBill calculatePaFei(DiliverymanPaifeiBill bill, List<DeliveryState> deliveryStateList,User user) {
		StringBuffer ordernumber = new StringBuffer();
		Integer ordersum = 0;
		BigDecimal orderfee = new BigDecimal(0);
		DiliverymanPaifeiOrder order = null;
		
		// 通过从配送结果表中查询到的订单号去查询主订单表中对应的数据
		for (int i = 0; i < deliveryStateList.size(); i++) {
			DeliveryState deliveryState = deliveryStateList.get(i);
			ordernumber.append(deliveryState.getCwb() + ",");
			ordersum++;
		}
		
		String orderString = "";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(ordernumber.toString())) {

			orderString = ordernumber.substring(0, ordernumber.length() - 1);
			/* 生成查询条件的字符串 */
			String ordernumberString = DiliverymanPaifeiBillService.spiltString(orderString);
			/* 根据订单号查出对应的数据 */
			List<CwbOrder> cwborderList = this.cwbDAO.getCwbByCwbs(ordernumberString);
			Map<String,BigDecimal> basicMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Basic, cwborderList);
			Map<String,BigDecimal> collectionMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Collection, cwborderList);
			Map<String,BigDecimal> areaMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Area, cwborderList);
			Map<String,BigDecimal> overareaMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Overarea, cwborderList);
			Map<String,BigDecimal> businessMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Business, cwborderList);
			Map<String,BigDecimal> insertionMap = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(user.getPfruleid(), PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Insertion, cwborderList);
			Map<String, String> orderFlowMap = this.orderFlowDAO.getOrderFlowByCwbs(ordernumberString);
			for (int i = 0; i < cwborderList.size(); i++) {
				BigDecimal sum = new BigDecimal(0);
				order = new DiliverymanPaifeiOrder();
				CwbOrder cwbOrder = cwborderList.get(i);
				/* 通过订单号查出到货时间 */
				
				order.setOrdernumber(cwbOrder.getCwb());
				order.setOrdertype(cwbOrder.getCwbordertypeid());
				order.setOrderstatus((int) cwbOrder.getFlowordertype());
				order.setDeliverytime(cwbOrder.getEmaildate());
				order.setDateoflodgment(cwbOrder.getPodtime());
				order.setPaymentmode(Integer.parseInt(cwbOrder.getNewpaywayid()));
				order.setTimeofdelivery(orderFlowMap.get(cwbOrder.getCwb()));
				BigDecimal basic = basicMap.get(cwbOrder.getCwb()); /* 基本派费 */
				BigDecimal collection = collectionMap.get(cwbOrder.getCwb());/* 代收补助费 */
				BigDecimal area = areaMap.get(cwbOrder.getCwb());/* 区域属性补助费 */
				BigDecimal overarea = overareaMap.get(cwbOrder.getCwb());/* 超出区域补助费 */
				BigDecimal business = businessMap.get(cwbOrder.getCwb());/* 业务补助 */
				BigDecimal insertion = insertionMap.get(cwbOrder.getCwb());/* 脱单补助 */
				if(basic != null){
					order.setBasicpaifei(basic); /* 基本派费 */
					sum = sum.add(basic);
				}
				if(collection != null){
					order.setSubsidiesfee(collection); /* 代收补助费 */
					sum = sum.add(collection);
				}
				if(area != null){
					order.setAreasubsidies(area); /* 区域属性补助费 */
					sum = sum.add(area);
				}
				if(overarea != null){
					order.setBeyondareasubsidies(overarea); /* 超出区域补助费 */
					sum = sum.add(overarea);
				}
				if(business != null){
					order.setBusinesssubsidies(business);/* 业务补助 */
					sum = sum.add(business);
				}
				if(insertion != null){
					order.setDelaysubsidies(insertion); /* 脱单补助 */
					sum = sum.add(insertion);
				}
				order.setPaifeicombined(sum);
				orderfee = orderfee.add(sum);
				this.diliverymanPaifeiBillDAO.addOrder(order);
			}
			this.deliveryStateDao.setWhetherGenerateDeliveryManBill(ordernumberString);
		}
		
		bill.setPaifeimoney(orderfee);
		bill.setOrderids(orderString);
		bill.setOrdersum(ordersum);
		return bill;
	}

	/**
	 * 移除订单
	 *
	 * @param str
	 * @return
	 */
	public void deleteorder(String ordernumber, Integer id) {
		String orderString = "";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(ordernumber)) {
			String cwb = this.spiltString(ordernumber);
			List<DiliverymanPaifeiOrder> orderlist = this.diliverymanPaifeiBillDAO.queryByOrderList(cwb);
			this.deliveryStateDao.setWhetherGenerateDeliveryBill(cwb);
			BigDecimal fee = new BigDecimal(0);
			BigDecimal sum = new BigDecimal(0);
			for(int k=0;k<orderlist.size();k++){
				DiliverymanPaifeiOrder order = orderlist.get(k);
				fee = fee.add(order.getPaifeicombined());
			}
			DiliverymanPaifeiBill bill = this.diliverymanPaifeiBillDAO.queryById(id);
			sum = bill.getPaifeimoney().subtract(fee);
			/* 将选中的订单号与账单表中的订单号做对比，去掉选中的订单号 */
			String[] arr1 = ordernumber.split(",");
			String[] arr2 = bill.getOrderids().split(",");
			for (int i = 0; i < arr1.length; i++) {
				for (int j = 0; j < arr2.length; j++) {
					if (arr2[j].equals(arr1[i])) {
						arr2[j] = "";
					}
				}
			}
			int i = 0;
			StringBuffer sBuffer = new StringBuffer();
			for (int j = 0; j < arr2.length; j++) {
				if (!"".equals(arr2[j])) {
					sBuffer.append(arr2[j] + ",");
					i++;
				}
			}
			String order = sBuffer.toString();
			order = order.substring(0, order.length() - 1);
			this.diliverymanPaifeiBillDAO.updateBillForOrder(i, order, id,sum);
			orderString = DiliverymanPaifeiBillService.spiltString(ordernumber);
			this.diliverymanPaifeiBillDAO.deleteorder(orderString);
		}
	}

	public static String spiltString(String str) {
		StringBuffer sb = new StringBuffer();
		String[] temp = str.split(",");
		for (int i = 0; i < temp.length; i++) {
			if (!"".equals(temp[i]) && (temp[i] != null)) {
				sb.append("'" + temp[i] + "',");
			}
		}
		String result = sb.toString();
		String tp = result.substring(result.length() - 1, result.length());
		if (",".equals(tp)) {
			return result.substring(0, result.length() - 1);
		} else {
			return result;
		}
	}

}
