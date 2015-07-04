/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PenalizeOutBillDAO;
import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PunishinsideBillDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.ExpressOpsPunishinsideBill;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.User;
import cn.explink.domain.penalizeOutBill;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.util.DateTimeUtil;

/**
 * @author wangqiang
 */
@Service
public class PenalizeOutBillService {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private PenalizeOutBillDAO PenalizeOutBilldao;
	@Autowired
	private PenalizeOutDAO penalizeOutDAO;
	@Autowired
	PunishinsideBillDAO punishinsideBillDAO;
	@Autowired
	private CustomerDAO customerDao;

	// 查询对外赔付账单明细
	public List<penalizeOutBill> queryAll(penalizeOutBill bill, String billCreationStartDate, String billCreationEndDate, String billVerificationStrartDate, String billVerificationEndDate,
			String sort, String method, long page) {
		return this.PenalizeOutBilldao.queryAll(bill, billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
	}

	// 根据条件查询指定订单再新增到对外赔付账单表中
	public void addPenalizeOutBill(Integer compensatebig, Integer compensatesmall, String compensateodd, String[] customerid, String CreationStartDate, String CreationEndDate, String compensateexplain) {
		penalizeOutBill bill = new penalizeOutBill();
		String string = StringUtils.join(customerid, ",");
		if (StringUtils.isNotBlank(string)) {
			List<Customer> customers = this.customerDao.getCustomerByIds(PenalizeOutBillService.spiltString(string));
			String customerName = "";
			for (int i = 0; i < customers.size(); i++) {
				customerName += customers.get(i).getCustomername() + ",";
			}
			String customerStr = customerName.substring(0, customerName.length() - 1);
			bill.setCustomername(customerStr);
		}
		String date = DateTimeUtil.getNowTime();
		if (StringUtils.isNotBlank(compensateodd)) {

			String str = PenalizeOutBillService.spilt(compensateodd);
			BigDecimal sum = new BigDecimal(0);
			List<PenalizeOut> list = this.penalizeOutDAO.queryByOdd(compensatebig, compensatesmall, str, CreationStartDate, CreationEndDate);
			for (int i = 0; i < list.size(); i++) {
				PenalizeOut out = list.get(i);
				if (out.getPenalizeOutfee() != null) {
					BigDecimal fee = out.getPenalizeOutfee();
					sum = sum.add(fee);
				}
			}
			bill.setCompensatefee(sum);
		}
		StringBuffer customerids = new StringBuffer("");

		String number = this.generateBillBatch();
		String odd = compensateodd.replace("\r\n", ",");
		bill.setBillstate(PunishBillStateEnum.WeiShenHe.getValue());
		bill.setFounder(this.getSessionUser().getUserid());
		bill.setBillbatches(number);
		bill.setCompensateodd(odd);

		bill.setCompensateexplain(compensateexplain);
		bill.setCompensatebig(compensatebig);
		bill.setCompensatesmall(compensatesmall);
		bill.setCustomerid(string);
		bill.setCreateddate(date);
		this.PenalizeOutBilldao.addPenalizeOutBill(bill);
	}

	// 将字符串中的回车符换成逗号 并且加上单引号
	public static String spilt(String str) {
		StringBuffer sb = new StringBuffer();
		String[] temp = str.split("\r\n");
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

	// 自动生成编号
	public String getNumber() {
		String number = "";
		List<penalizeOutBill> penalizeOutBill = this.PenalizeOutBilldao.getMaxNumber();
		if ((penalizeOutBill != null) && !penalizeOutBill.isEmpty()) {
			for (int i = 0; i < penalizeOutBill.size(); i++) {
				penalizeOutBill bill = penalizeOutBill.get(i);
				String maxNumber = bill.getBillbatches();
				if ((maxNumber.length() == 15) && "P".equals(maxNumber.substring(0, 3))) {
					String partContractNo = maxNumber.substring(0, 11);
					String maxOrderStr = maxNumber.substring(11);
					int maxOrderInt = Integer.valueOf(maxOrderStr);
					maxOrderInt++;
					String orderStr = String.valueOf(maxOrderInt);
					while (orderStr.length() != 4) {
						orderStr = "0" + orderStr;
					}
					number = partContractNo + orderStr;
					break;
				}
			}
		}
		if (StringUtils.isBlank(number)) {
			String rule = "P_F";
			String date = DateTimeUtil.getCurrentDate();
			String orderStr = "0001";
			number = rule + date + orderStr;
		}
		return number;
	}

	public String generateBillBatch() {
		String rule = "P";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMddHHmmssSSS");
		String billBatch = rule + nowTime;
		return billBatch;
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// 通过id查询指定赔付账单明细
	public penalizeOutBill queryById(Integer id, long page) {
		penalizeOutBill outBill = this.PenalizeOutBilldao.queryById(id);
		if (StringUtils.isNotBlank(outBill.getCompensateodd())) {
			String str = PenalizeOutBillService.spiltString(outBill.getCompensateodd());
			List<PenalizeOut> OutList = this.penalizeOutDAO.getPenalizeOutByCwbs(str, page);
			outBill.setPenalizeOutList(OutList);
		}
		return outBill;
	}

	public int queryByIdcount(Integer id) {
		penalizeOutBill outBill = this.PenalizeOutBilldao.queryById(id);
		if (StringUtils.isNotBlank(outBill.getCompensateodd())) {
			String str = PenalizeOutBillService.spiltString(outBill.getCompensateodd());
			int count = this.penalizeOutDAO.getPenalizeOutByCwbscount(str);
			return count;
		}
		return 0;
	}

	// 查询指定条件的赔付单
	public List<PenalizeOut> penalizeOutBillList(Integer id, Integer compensatebig, Integer compensatesmall, String compensateodd, Integer customerid, String creationStartDate,
			String creationEndDate, long page) {
		List<PenalizeOut> out = null;
		String str = "";
		String oddstr = "";
		if (StringUtils.isNotBlank(compensateodd)) {
			str = PenalizeOutBillService.spilt(compensateodd);
		}
		penalizeOutBill outBill = this.PenalizeOutBilldao.queryById(id);
		if (StringUtils.isNotBlank(outBill.getCompensateodd())) {
			oddstr = PenalizeOutBillService.spiltString(outBill.getCompensateodd());
		}
		out = this.penalizeOutDAO.queryByOddDetail(compensatebig, compensatesmall, str, creationStartDate, creationEndDate, oddstr, page);
		return out;

	}

	public int queryByOddDetailsum(Integer id, Integer compensatebig, Integer compensatesmall, String compensateodd, Integer customerid, String creationStartDate, String creationEndDate, long page) {
		String str = "";
		String oddstr = "";
		if (StringUtils.isNotBlank(compensateodd)) {
			str = PenalizeOutBillService.spilt(compensateodd);
		}
		penalizeOutBill outBill = this.PenalizeOutBilldao.queryById(id);
		if (StringUtils.isNotBlank(outBill.getCompensateodd())) {
			oddstr = PenalizeOutBillService.spiltString(outBill.getCompensateodd());
		}
		int out = this.penalizeOutDAO.queryByOddDetailsum(compensatebig, compensatesmall, str, creationStartDate, creationEndDate, oddstr);
		return out;

	}

	// 添加指定订单
	public void addpenalizeOutBillList(Integer id, String compensateodd) {
		penalizeOutBill outBill = this.PenalizeOutBilldao.queryById(id);
		String oddstr = outBill.getCompensateodd();
		if (StringUtils.isNotBlank(compensateodd)) {
			if (StringUtils.isNotBlank(oddstr)) {
				compensateodd = oddstr + "," + compensateodd;
			}
		}
		this.PenalizeOutBilldao.addpenalizeOutDetail(id, compensateodd);
	}

	// 修改指定账单信息
	public void penalizeOutBillUpdate(penalizeOutBill bill) {
		this.PenalizeOutBilldao.penalizeOutBillUpdate(bill);
	}

	/**
	 * 新增的同时添加对内扣罚账单
	 *
	 * @param str
	 * @return
	 */
	public void addpunishinsideBill(String batchstate, String dutypersonid, BigDecimal sumPrice, String punishInsideRemark, String compensateodd) {
		ExpressOpsPunishinsideBill punishinsideBill = new ExpressOpsPunishinsideBill();
		String odd = "";
		BigDecimal sum = new BigDecimal(0);
		if (StringUtils.isNotBlank(compensateodd)) {
			odd = compensateodd.replace("\r\n", ",");
			String str = PenalizeOutBillService.spilt(compensateodd);

			List<PenalizeOut> list = this.penalizeOutDAO.getPenalizeOutByid(str);
			for (int i = 0; i < list.size(); i++) {
				PenalizeOut out = list.get(i);
				if (out.getPenalizeOutfee() != null) {
					BigDecimal fee = out.getPenalizeOutfee();
					sum = sum.add(fee);
				}
			}

		}
		punishinsideBill.setSumPrice(sum);
		punishinsideBill.setBillBatch(this.BillBatch());
		punishinsideBill.setPunishNos(odd);
		this.punishinsideBillDAO.createPunishinsideBill(punishinsideBill);
	}

	public String BillBatch() {
		String rule = "K";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMddHHmmssSSS");
		String billBatch = rule + nowTime;
		return billBatch;
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

	public void deletePenalizeOutBill(Integer id) {
		this.PenalizeOutBilldao.deletePenalizeOutBill(id);
	}
}
