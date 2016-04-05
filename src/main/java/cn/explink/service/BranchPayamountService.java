package cn.explink.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.FinanceAuditDAO;
import cn.explink.dao.FinancePayUpAuditDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FinanceAudit;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FinanceAuditTypeEnum;
import cn.explink.enumutil.FundsEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.util.JMath;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Service
public class BranchPayamountService {

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliverStateDAO;
	@Autowired
	FinanceAuditDAO financeAuditDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	PayUpDAO payUpDAO;
	@Autowired
	FinancePayUpAuditDAO financePayUpAuditDAO;
	@Autowired
	BranchDAO branchDAO;

	private Logger logger = LoggerFactory.getLogger(BranchPayamountService.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<CwbOrder> searchDetailByFlowType(long type, long customerid, long page, String starttime, String endtime) {
		return cwbDAO.getCwbListByCustomeridAndFloworderTypeowAndCredate(customerid, type, starttime, endtime, page);
	}

	public List<String> searchDetailByFlowTypeExp(long type, long customerid, String starttime, String endtime, long page) {
		return deliverStateDAO.getCwbListByCustomeridAndFloworderTypeowAndCredateExp(customerid, starttime, endtime, page);
	}

	public long searchDetailByFlowTypeCount(long type, long customerid, long page, String starttime, String endtime) {
		return cwbDAO.getCountByCustomeridAndFloworderTypeowAndCredate(customerid, type, starttime, endtime, page);
	}

	public List<CwbOrder> searchDetailByFlowTypeback(String type, long customerid, long page, String starttime, String endtime, String startaudittime, String endaudittime) {
		List<CwbOrder> list = null;// cwbDAO.searchCwbDetailByFlowOrdertypeBack(type,starttime,
									// endtime, customerid,page,startaudittime,
									// endaudittime);
		return list;
	}

	public Map<Integer, Map<Integer, List<JSONObject>>> getPayamountMap(List<Customer> cumstrListAll, long type, String startCredate, String endCredate) {
		Map<Integer, Map<Integer, List<JSONObject>>> map = new HashMap<Integer, Map<Integer, List<JSONObject>>>();
		for (Customer customer : cumstrListAll) {

			Map<Integer, List<JSONObject>> jsonMap = new HashMap<Integer, List<JSONObject>>();
			if (type == FundsEnum.PeiSongChengGong.getValue()) {// 配送成功
				for (FundsEnum fun : FundsEnum.values()) {
					List<JSONObject> list = new ArrayList<JSONObject>();
					if (fun.getValue() != FundsEnum.TuiHuo.getValue()) {
						List<JSONObject> jsonobject = getJsonList(customer.getCustomerid(), fun.getValue(), startCredate, endCredate);
						list.addAll(jsonobject);
						jsonMap.put((int) fun.getValue(), list);
					}
				}
			} else {// 退货款
				for (FundsEnum fun : FundsEnum.values()) {
					List<JSONObject> list = new ArrayList<JSONObject>();
					if (fun.getValue() != FundsEnum.PeiSongChengGong.getValue()) {
						List<JSONObject> jsonobject = getJsonList(customer.getCustomerid(), fun.getValue(), startCredate, endCredate);
						list.addAll(jsonobject);
						jsonMap.put((int) fun.getValue(), list);
					}
				}
			}
			map.put((int) customer.getCustomerid(), jsonMap);
		}
		return map;
	}

	private List<JSONObject> getJsonList(long customerid, long flowordertype, String startCredate, String endCredate) {

		List<JSONObject> list = new ArrayList<JSONObject>();
		if (flowordertype == FundsEnum.Fahuo.getValue()) {// 发货总数
		// list.addAll(deliverStateDAO.getListByCustomeridAndFloworderTypeowAndCredate(customerid,
		// FlowOrderTypeEnum.DaoRuShuJu.getValue(), startCredate, endCredate));
		} else if (flowordertype == FundsEnum.PeiSongChengGong.getValue()) {// 配送成功
			JSONObject json = new JSONObject();
			int num = 0;
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			List<JSONObject> peisongchenggongList = deliverStateDAO.getListByCustomeridAndDeliverystateAndCredate(customerid, DeliveryStateEnum.PeiSongChengGong.getValue(), startCredate, endCredate);// 配送成功
			if (peisongchenggongList != null) {
				num += peisongchenggongList.get(0).getInt("num");
				receivablefee = receivablefee.add(new BigDecimal(peisongchenggongList.get(0).getString("receivablefee")));
				paybackfee = paybackfee.add(new BigDecimal(peisongchenggongList.get(0).getString("paybackfee")));
			}
			List<JSONObject> shangmenhuanList = deliverStateDAO.getListByCustomeridAndDeliverystateAndCredate(customerid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate, endCredate);// 上门换成功
			if (shangmenhuanList != null) {
				num += shangmenhuanList.get(0).getInt("num");
				receivablefee = receivablefee.add(new BigDecimal(shangmenhuanList.get(0).getString("receivablefee")));
				paybackfee = paybackfee.add(new BigDecimal(shangmenhuanList.get(0).getString("paybackfee")));
			}

			json.put("num", num);
			json.put("receivablefee", receivablefee);
			json.put("paybackfee", paybackfee);
			list.add(json);

		} else if (flowordertype == FundsEnum.TuiHuo.getValue()) {// 退货
			JSONObject json = new JSONObject();
			int num = 0;
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			List<JSONObject> shangmentuiList = deliverStateDAO.getListByCustomeridAndDeliverystateAndCredate(customerid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startCredate, endCredate);// 上门退成功
			if (shangmentuiList != null) {
				num += shangmentuiList.get(0).getInt("num");
				receivablefee = receivablefee.add(new BigDecimal(shangmentuiList.get(0).getString("receivablefee")));
				paybackfee = paybackfee.add(new BigDecimal(shangmentuiList.get(0).getString("paybackfee")));
			}
			List<JSONObject> jushouList = deliverStateDAO.getListByCustomeridAndDeliverystateAndCredate(customerid, DeliveryStateEnum.JuShou.getValue(), startCredate, endCredate);// 拒收
			if (jushouList != null) {
				num += jushouList.get(0).getInt("num");
				receivablefee = receivablefee.add(new BigDecimal(jushouList.get(0).getString("receivablefee")));
				paybackfee = paybackfee.add(new BigDecimal(jushouList.get(0).getString("paybackfee")));
			}
			json.put("num", num);
			json.put("receivablefee", receivablefee);
			json.put("paybackfee", paybackfee);
			list.add(json);
		}
		return list;
	}

	public Map<Integer, List<String>> getCwbList(long customerid, long flowordertype, String startCredate, String endCredate) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> list = new ArrayList<String>();
		if (flowordertype == FundsEnum.PeiSongChengGong.getValue()) {// 配送成功
			list.addAll(deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
					DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate, endCredate));// 配送成功
			// list.addAll(
			// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
			// DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate,
			// endCredate));//上门换成功
		} else if (flowordertype == FundsEnum.TuiHuo.getValue()) {// 退货
			list.addAll(deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
					DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate, endCredate));// 配送成功
			// list.addAll(
			// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
			// DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate,
			// endCredate));//上门换成功
		}

		for (int i = 0, page = 1; i < list.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > list.size() ? list.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, list.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	public List<String> getCwbListAll(long customerid, long flowordertype, String startCredate, String endCredate) {
		List<String> list = new ArrayList<String>();
		if (flowordertype == FundsEnum.PeiSongChengGong.getValue()) {// 配送成功
			list.addAll(deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
					DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate, endCredate));// 配送成功
			// list.addAll(
			// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
			// DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate,
			// endCredate));//上门换成功
		} else if (flowordertype == FundsEnum.TuiHuo.getValue()) {// 退货
			list.addAll(deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
					DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate, endCredate));// 配送成功
			// list.addAll(
			// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredate(customerid,
			// DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startCredate,
			// endCredate));//上门换成功
		}

		return list;
	}

	/**
	 * 导出站点交款审核数据 结算管理 > 结算与审核 > 货款结算
	 */
	public void getCwbDetailByFlowOrder_payment(HttpServletResponse response, List<JSONObject> datalist, List<User> userlist) {

		String excelbranch = "站点交款审核明细";
		CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("站点交款审核"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "小件员");
		JMath.setCellValue(row, c++, "配送站点");
		JMath.setCellValue(row, c++, "供货商");
		JMath.setCellValue(row, c++, "订单应收金额");
		JMath.setCellValue(row, c++, "退还金额");
		JMath.setCellValue(row, c++, "应处理金额");
		JMath.setCellValue(row, c++, "现金实收");
		JMath.setCellValue(row, c++, "pos实收");
		JMath.setCellValue(row, c++, "支付宝COD扫码");
		JMath.setCellValue(row, c++, "pos备注");
		JMath.setCellValue(row, c++, "反馈时间");
		JMath.setCellValue(row, c++, "其他金额");
		JMath.setCellValue(row, c++, "支票实收");
		JMath.setCellValue(row, c++, "支票号备注");
		JMath.setCellValue(row, c++, "站点上缴时间");
		JMath.setCellValue(row, c++, "归班审核时间");
		JMath.setCellValue(row, c++, "配送结果");
		for (JSONObject obj : datalist) {
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			JMath.setCellValue(row, c++, obj.getString("cwb") + "");
			for (User u : userlist) {
				if (obj.getLong("deliveryid") == u.getUserid()) {
					JMath.setCellValue(row, c++, u.getRealname() + "");
				}
			}
			if (c == 1) {
				JMath.setCellValue(row, c++, "");
			}

			JMath.setCellValue(row, c++, obj.getString("deliverybranch") + "");
			JMath.setCellValue(row, c++, obj.getString("customername") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("receivedfee") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("returnedfee") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("businessfee") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("cash") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("pos") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("codpos") + "");
			JMath.setCellValue(row, c++, StringUtil.nullConvertToEmptyString(obj.getString("posremark")) + "");
			JMath.setCellValue(row, c++, obj.getString("createtime") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("otherfee") + "");
			JMath.setCellNumberValue(row, c++, obj.getDouble("checkfee") + "");
			JMath.setCellValue(row, c++, StringUtil.nullConvertToEmptyString(obj.getString("checkremark") + ""));
			JMath.setCellValue(row, c++, obj.getString("credatetime").substring(0, obj.getString("credatetime").length() - 2) + "");
			JMath.setCellValue(row, c++, obj.getString("auditingtime") + "");
			for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
				if (dse.getValue() == obj.getLong("deliverystate")) {
					JMath.setCellValue(row, c++, dse.getText() + "");
				}
			}
		}

		CreateOutPutExcel(response, workbook);

	}

	private void CreateExcelHeader(HttpServletResponse response, String excelbranch) {
		try {
			excelbranch = new String(excelbranch.getBytes("GBK"), "iso8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.error("export Excel exception!", e1);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		GregorianCalendar thisday = new GregorianCalendar(); // 现在的时间
		String filename = format.format(thisday.getTime()).toString() + excelbranch; // 文件名
		response.setContentType("application/ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
	}

	private void CreateOutPutExcel(HttpServletResponse response, HSSFWorkbook workbook) {
		ServletOutputStream fOut;
		try {
			fOut = response.getOutputStream();// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			fOut.close();// 操作结束，关闭文件
		} catch (IOException e) {
			logger.error("", e);
		}
		logger.info("文件生成...");
	}

	/**
	 * 财务审核结算提交保存
	 * 
	 * @param cwbs
	 * @param auditCustomerid
	 *            审核供货商id
	 * @param auditCustomerwarehouseid
	 *            审核供货商发货仓库id
	 * @param auditCwbOrderType
	 *            审核的订单类型
	 * @param type
	 *            审核类型 对应FinanceAuditTypeEnum
	 * @param auditdatetime
	 *            审核时间
	 * @param paydatetime
	 *            付款时间
	 * @param paytype
	 *            付款方式 1现金 2 POS
	 * @param payamount
	 *            付款金额
	 * @param shouldpayamount
	 *            应付金额
	 * @param paynumber
	 *            付款流水号
	 * @param payremark
	 *            付款备注
	 * @return
	 */
	@Transactional
	public FinanceAudit saveFinanceAuditDetail(String cwbs, long auditCustomerid, long auditCustomerwarehouseid, int auditCwbOrderType, int type, String auditdatetime, String paydatetime,
			int paytype, BigDecimal payamount, BigDecimal shouldpayamount, String paynumber, String payremark) {
		// 验证订单数与金额
		FinanceAudit fa = null;
		cwbs = cwbs.substring(0, cwbs.length() - 1);
		List<CwbOrder> coList = cwbDAO.getCwbOrderByCwbs(cwbs);// 得到要审核的数据
		if (coList.size() > 0) {
			CwbOrder coLock = cwbDAO.getCwbByCwbLock(coList.get(0).getCwb());// 锁住事物开始验证

			if (coLock.getCwbordertypeid() != auditCwbOrderType) {
				throw new ExplinkException("您审核的订单类型与即将审核的数据不一致，请重试！");
			} else if (coLock.getCustomerid() != auditCustomerid) {
				throw new ExplinkException("您审核的供货商与即将审核的数据不一致，请重试！");
			} else if (auditCustomerwarehouseid > -1 && !coLock.getCustomerwarehouseid().equals("" + auditCustomerwarehouseid)) {
				throw new ExplinkException("您审核的供货商发货仓库与即将审核的数据不一致，请重试！");
			}
			BigDecimal countfee = BigDecimal.ZERO;
			for (CwbOrder co : coList) {
				countfee = countfee.add(co.getReceivablefee()).subtract(co.getPaybackfee());
			}
			if (shouldpayamount.compareTo(countfee) != 0) {
				throw new ExplinkException("您审核的货物应收金额与即将审核的数据不一致，请重试！");
			}
			fa = new FinanceAudit(0L, auditCustomerid, auditCustomerwarehouseid, coList.size(), paydatetime, paytype, shouldpayamount, payamount, paynumber, auditdatetime, payremark, type,
					BigDecimal.ZERO, BigDecimal.ZERO, "", auditCwbOrderType);
			fa.setId(financeAuditDAO.creFinanceAudit(fa));
			fa.setQiankuanamount(fa.getShouldpayamount().subtract(fa.getPayamount()));
			long isAudit = financeAuditDAO.isAuditCount(cwbs);
			if (isAudit == 0) {
				Map<Long, Long> map = new HashMap<Long, Long>();
				for (CwbOrder co : coList) {
					financeAuditDAO.insertTemp(co, fa.getId(), auditdatetime, type);
					// 统计每个批次已经被审核的订单数
					map.put(co.getEmaildateid(), map.get(co.getEmaildateid()) == null ? 1 : (map.get(co.getEmaildateid()) + 1));
				}
				for (Map.Entry<Long, Long> emaildate : map.entrySet()) {
					emailDateDAO.editEditEmaildateForFinanceauditcount(emaildate.getValue(), emaildate.getKey());
				}
			} else {
				throw new ExplinkException("您审核的数据中有" + isAudit + "单已经被审核，很可能您的同事在与您审核同一批数据，请重试！");
			}

		} else {
			throw new ExplinkException("系统无法获得您要审核的订单，请重试！");
		}
		return fa;
	}

	/**
	 * 财务审核结算提交保存
	 * 
	 * @param cwbs
	 * @param auditCustomerid
	 *            审核供货商id
	 * @param auditCustomerwarehouseid
	 *            审核供货商发货仓库id
	 * @param auditCwbOrderType
	 *            审核的订单类型
	 * @param type
	 *            审核类型 对应FinanceAuditTypeEnum
	 * @param auditdatetime
	 *            审核时间
	 * @param paydatetime
	 *            付款时间
	 * @param paytype
	 *            付款方式 1现金 2 POS
	 * @param payamount
	 *            付款金额
	 * @param shouldpayamount
	 *            应付金额
	 * @param paynumber
	 *            付款流水号
	 * @param payremark
	 *            付款备注
	 * @return
	 */
	@Transactional
	public FinanceAudit saveFinanceAuditBackDetail(String cwbs, long auditCustomerid, long auditCustomerwarehouseid, int auditCwbOrderType, int type, String auditdatetime, String paydatetime,
			int paytype, BigDecimal payamount, BigDecimal shouldpayamount, String paynumber, String payremark) {
		// 验证订单数与金额
		FinanceAudit fa = null;
		cwbs = cwbs.substring(0, cwbs.length() - 1);
		List<CwbOrder> coList = cwbDAO.getCwbOrderByCwbs(cwbs);// 得到要审核的数据
		if (coList.size() > 0) {
			CwbOrder coLock = cwbDAO.getCwbByCwbLock(coList.get(0).getCwb());// 锁住事物开始验证

			if (coLock.getCustomerid() != auditCustomerid) {
				throw new ExplinkException("您审核的供货商与即将审核的数据不一致，请重试！");
			} else if (auditCustomerwarehouseid > 0 && !coLock.getCustomerwarehouseid().equals("" + auditCustomerwarehouseid)) {
				throw new ExplinkException("您审核的供货商发货仓库与即将审核的数据不一致，请重试！");
			}
			BigDecimal countfee = BigDecimal.ZERO;
			int isout = 0;
			if (type == FinanceAuditTypeEnum.TuiHuoKuanJieSuan_RuZhang.getValue()) {
				isout = 1;
			}
			JSONObject feeJson = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNopageByBack(cwbs, auditCustomerid + "", isout, auditCustomerwarehouseid, 0);
			if (type == FinanceAuditTypeEnum.TuiHuoKuanJieSuan_RuZhang.getValue()) {
				countfee = new BigDecimal(feeJson.get("paybackfees") == null ? "0" : feeJson.getString("paybackfees"));
			} else {
				countfee = new BigDecimal(feeJson.get("receivablefees") == null ? "0" : feeJson.getString("receivablefees"));
			}

			if (shouldpayamount.compareTo(countfee) != 0) {
				throw new ExplinkException("您审核的货物应收金额与即将审核的数据不一致，请重试！");
			}
			fa = new FinanceAudit(0L, auditCustomerid, auditCustomerwarehouseid, coList.size(), paydatetime, paytype, shouldpayamount, payamount, paynumber, auditdatetime, payremark, type,
					BigDecimal.ZERO, BigDecimal.ZERO, "", auditCwbOrderType);
			fa.setId(financeAuditDAO.creFinanceAudit(fa));
			long isAudit = financeAuditDAO.isAuditCount(cwbs);
			if (isAudit == 0) {
				Map<Long, Long> map = new HashMap<Long, Long>();
				for (CwbOrder co : coList) {
					financeAuditDAO.insertTemp(co, fa.getId(), auditdatetime, type);
					// 统计每个批次已经被审核的订单数
					map.put(co.getEmaildateid(), map.get(co.getEmaildateid()) == null ? 1 : (map.get(co.getEmaildateid()) + 1));
				}
				for (Map.Entry<Long, Long> emaildate : map.entrySet()) {
					emailDateDAO.editEditEmaildateForFinanceauditcount(emaildate.getValue(), emaildate.getKey());
				}
			} else {
				throw new ExplinkException("您审核的数据中有" + isAudit + "单已经被审核，很可能您的同事在与您审核同一批数据，请重试！");
			}

		} else {
			throw new ExplinkException("系统无法获得您要审核的订单，请重试！");
		}
		return fa;
	}

	public void updateAudit(JSONArray jsonList, User user) {

		/*
		 * [{branchid:"192",payupids:"14_15",mackStr:"sadsad",cash:"76666.66",pos
		 * :"12222.22",oldcash:"76666.66",oldpos:"12222.22"},
		 * {branchid:"192",payupids
		 * :"16",mackStr:"asdsad",cash:"23.0",pos:"0.00",
		 * oldcash:"23.0",oldpos:"0.00"}]
		 */
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		try {
			for (int i = 0; i < jsonList.size(); i++) {
				String payupIds = doInsertSort2(jsonList.getJSONObject(i).getString("payupids"));// 排序
				String auditingremark = jsonList.getJSONObject(i).getString("mackStr");
				String cash = jsonList.getJSONObject(i).getString("cash");
				String pos = jsonList.getJSONObject(i).getString("pos");
				String oldcash = jsonList.getJSONObject(i).getString("oldcash");
				String oldpos = jsonList.getJSONObject(i).getString("oldpos");
				long branchid = jsonList.getJSONObject(i).getLong("branchid");

				long auditid = financePayUpAuditDAO.insert(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), user.getUserid(), new BigDecimal(oldcash), new BigDecimal(oldpos),
						new BigDecimal(cash), new BigDecimal(pos), payupIds);
				logger.info("站点交款审核 完成插入操作表,操作站点:{},操作人:{}", branchid, user.getRealname());
				payUpDAO.savePayUpForAudtingAudit(payupIds, user.getRealname(), auditingremark, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), auditid);
				BigDecimal qiankuanCash = new BigDecimal(oldcash).subtract(new BigDecimal(cash));
				BigDecimal qiankuanPos = new BigDecimal(oldpos).subtract(new BigDecimal(pos));
				logger.info("站点交款审核 完成更新交款表,操作站点:{},操作人:{}", branchid, user.getRealname());
				if (map.get(branchid + "cash") != null) {
					BigDecimal cashqianKuan = map.get(branchid + "cash");
					cashqianKuan = cashqianKuan.add(qiankuanCash);
					map.put(branchid + "cash", cashqianKuan);
				} else {
					map.put(branchid + "cash", qiankuanCash);
				}
				if (map.get(branchid + "pos") != null) {
					BigDecimal posqianKuan = map.get(branchid + "pos");
					posqianKuan = posqianKuan.add(qiankuanPos);
					map.put(branchid + "pos", posqianKuan);
				} else {
					map.put(branchid + "pos", qiankuanPos);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("站点交款审核", e);
		}

		try {
			Set<String> key = map.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String s = (String) it.next();
				if (s.indexOf("cash") > -1) {
					BigDecimal cashqianKuan = map.get(s);
					long branchid = Long.parseLong(s.substring(0, s.indexOf("cash")));
					branchDAO.updateQiankuan(branchid, cashqianKuan, BigDecimal.ZERO);
					logger.info("站点交款审核 完成现金欠款，站点：{},现金欠款：{}", branchid, cashqianKuan);
				}
				if (s.indexOf("pos") > -1) {
					BigDecimal posqianKuan = map.get(s);
					long branchid = Long.parseLong(s.substring(0, s.indexOf("pos")));
					branchDAO.updateQiankuan(branchid, BigDecimal.ZERO, posqianKuan);
					logger.info("站点交款审核 完成pos欠款，站点：{},pos欠款：{}", branchid, posqianKuan);
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("站点交款审核", e);
		}

	}

	private String doInsertSort2(String payupIds) {
		String[] ids = payupIds.split("_");
		String result = "";
		int[] src = new int[ids.length];
		for (int i = 0; i < ids.length; i++) {
			src[i] = Integer.parseInt(ids[i]);
		}
		int len = src.length;
		for (int i = 1; i < len; i++) {
			int j;
			int temp = src[i];
			for (j = i; j > 0; j--) {
				if (src[j - 1] > temp) {
					src[j] = src[j - 1];
				} else
					// 如果当前的数，不小前面的数，那就说明不小于前面所有的数，
					// 因为前面已经是排好了序的，所以直接通出当前一轮的比较
					break;
			}
			src[j] = temp;
		}
		for (int i : src) {
			result += "" + i + ",";
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "0";
	}

}