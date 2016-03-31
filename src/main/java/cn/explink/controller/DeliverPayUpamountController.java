package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.FinanceDeliverPayUpDetailDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Exportmould;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliverPayupArrearageapprovedEnum;
import cn.explink.enumutil.DeliverPayuptypeEnum;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.UserService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@RequestMapping("/deliverpayup")
@Controller
public class DeliverPayUpamountController {

	@Autowired
	BranchDAO branchDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	UserService userService;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	FinanceDeliverPayUpDetailDAO financeDeliverPayUpDetailDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	ExportmouldDAO exportmouldDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Logger logger = LoggerFactory.getLogger(DeliverPayUpamountController.class);

	private enum ExportDetailColEnum {

		Cwb("cwb", "订单号", Cell.CELL_TYPE_STRING), Deliver("deliver", "小件员", Cell.CELL_TYPE_STRING), PayupTime("payupTime", "交款时间", Cell.CELL_TYPE_STRING), PayupType("payupType", "交款类型",
				Cell.CELL_TYPE_STRING), BusinessFee("businessFee", "应收款", Cell.CELL_TYPE_NUMERIC), ReceivedFee("receivedFee", "实收款", Cell.CELL_TYPE_NUMERIC), AuditStatus("auditState", "审核状态",
				Cell.CELL_TYPE_STRING), AuditRemark("auditRemark", "审核备注", Cell.CELL_TYPE_STRING);

		ExportDetailColEnum(String name, String text, int type) {
			this.name = name;
			this.text = text;
			this.type = type;
		}

		String name;
		String text;
		int type;

		private String getName() {
			return this.name;
		}

		private String getText() {
			return this.text;
		}

		private int getType() {
			return this.type;
		}

	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/showdetail")
	public String showDetail(Model model, @RequestParam(value = "id", required = true) Long id) {
		List<Exportmould> exportmouldlist = this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("rowData", this.getDetailRowData(id));
		model.addAttribute("gcaid", id);

		return "/deliverpayup/detail";
	}

	private List<Map<String, Object>> getDetailRowData(Long id) {
		List<Map<String, Object>> rowData = new ArrayList<Map<String, Object>>();
		this.addDetailRowData(rowData, id);

		return rowData;
	}

	@RequestMapping("/exportDetail")
	public void exportDetail(Model model, HttpServletResponse response, @RequestParam(value = "gcaid", required = true) Long gcaid) {
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<Map<String, Object>> rowData = this.getDetailRowData(gcaid);
			ExcelUtils excelUtil = new ExportDetailUtil(rowData);
			excelUtil.excel(response, this.getExportDetailColNames(), sheetName, fileName);

		} catch (Exception e) {
			Log.error("小件员交款审核明细导出失败.", e);
		}
	}

	private String[] getExportDetailColNames() {
		List<String> colNames = new ArrayList<String>();
		for (ExportDetailColEnum colEnum : ExportDetailColEnum.values()) {
			colNames.add(colEnum.getText());
		}
		return colNames.toArray(new String[0]);
	}

	private class ExportDetailUtil extends ExcelUtils {

		public List<Map<String, Object>> rowData = null;

		public ExportDetailUtil(List<Map<String, Object>> rowData) {
			this.rowData = rowData;
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			int rowNumber = 1;
			for (Map<String, Object> row : this.getRowData()) {
				this.writeSignleRow(sheet, row, rowNumber++);
			}
		}

		private void writeSignleRow(Sheet sheet, Map<String, Object> rowData, int rowNumber) {
			Row row = sheet.createRow(rowNumber);
			int colCnt = ExportDetailColEnum.values().length;
			for (int i = 0; i < colCnt; i++) {
				ExportDetailColEnum colEnum = ExportDetailColEnum.values()[i];
				Cell cell = row.createCell(i);
				Object value = rowData.get(colEnum.getName());
				if (Cell.CELL_TYPE_NUMERIC == colEnum.getType()) {
					cell.setCellValue(((BigDecimal) value).doubleValue());
				} else {
					cell.setCellValue(value.toString());
				}
			}
		}

		private List<Map<String, Object>> getRowData() {
			return this.rowData;
		}

	}

	private void addDetailRowData(List<Map<String, Object>> rowData, Long gcaid) {
		List<DeliveryState> stateList = this.deliveryStateDAO.getDeliveryStateByGcaid(gcaid);
		if (stateList.isEmpty()) {
			return;
		}
		GotoClassAuditing gtcState = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(gcaid);
		String deliverName = this.getDeliverName(gtcState.getDeliverealuser());
		String auditStatus = this.getStrAuditStatus(gtcState);
		String auditRemark = this.getAuditRemakr(gtcState);
		for (DeliveryState state : stateList) {
			rowData.add(this.createDetailRowData(gtcState, state, deliverName, auditStatus, auditRemark));
		}
	}

	private String getDeliverName(long deliverId) {
		User user = this.userDAO.getAllUserByid(deliverId);

		return user.getRealname();
	}

	private Map<String, Object> createDetailRowData(GotoClassAuditing gState, DeliveryState dState, String delName, String aState, String aRemark) {
		Map<String, Object> rowData = new HashMap<String, Object>();
		rowData.put("cwb", dState.getCwb());
		rowData.put("deliver", delName);
		rowData.put("payupTime", gState.getAuditingtime());
		rowData.put("payupType", this.getDeliverOPayupType(gState));
		rowData.put("businessFee", dState.getBusinessfee());
		rowData.put("receivedFee", dState.getReceivedfee());
		rowData.put("auditState", aState);
		rowData.put("auditRemark", aRemark);

		return rowData;
	}

	private String getStrAuditStatus(GotoClassAuditing gtcState) {
		if (gtcState.getDeliverpayupapproved() == 0) {
			return "未通过";
		}
		return "通过";
	}

	private String getAuditRemakr(GotoClassAuditing gtcState) {
		String checkRemark = gtcState.getCheckremark();
		if (checkRemark == null) {
			return "";
		}
		return checkRemark;
	}

	private String getDeliverOPayupType(GotoClassAuditing gtcState) {
		return DeliverPayuptypeEnum.values()[gtcState.getDeliverpayuptype()].getText();
	}

	/**
	 * 小件员交款记录
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param deliverealuser
	 * @param deliverpayupapproved
	 * @return
	 */
	@RequestMapping("/branch/{page}")
	public String paymentfordeliverystate(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "deliverealuser", required = false, defaultValue = "-1") long deliverealuser,
			@RequestParam(value = "deliverpayupapproved", required = false, defaultValue = "-1") long deliverpayupapproved) {
		this.logger.info("小件员交款记录,begindate:" + begindate + ",enddate:" + enddate + ",deliverealuser:" + deliverealuser + ",deliverpayupapproved:" + deliverpayupapproved + "当前操作人："
				+ this.getSessionUser().getRealname());
		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());
		model.addAttribute("deliverList", deliverList);
		long count = 0;
		Map<Long, String> deliverMap = new HashMap<Long, String>();
		if (!begindate.equals("")) {
			List<GotoClassAuditing> gcaList = this.gotoClassAuditingDAO.getGotoClassAuditingByAuditingtime(page, begindate, enddate, this.getSessionUser().getBranchid(), deliverealuser,
					deliverpayupapproved);
			count = this.gotoClassAuditingDAO.getGotoClassAuditingByAuditingtimeCount(begindate, enddate, this.getSessionUser().getBranchid(), deliverealuser, deliverpayupapproved);

			if ((deliverList != null) && (deliverList.size() > 0)) {
				for (User user : deliverList) {
					deliverMap.put(user.getUserid(), user.getRealname());
				}
			}

			model.addAttribute("gcaList", gcaList);
		}
		model.addAttribute("deliverMap", deliverMap);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		return "deliverpayup/branch";
	}

	/**
	 * 小件员交款审核(重庆华宇需求).
	 *
	 * 
	 * @param model
	 * @param branchid
	 * @param begindate
	 * @param enddate
	 * @param deliverpayupapproved
	 * @return
	 */
	@RequestMapping("/deliverpayuplist")
	public String deliverpayuplist(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverpayupapproved", required = false, defaultValue = "-1") long deliverpayupapproved,
			@RequestParam(value = "deliver", required = false, defaultValue = "0") long deliver) {
		List<Branch> bList = this.branchDao.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		model.addAttribute("bList", bList);
		if ((branchid == 0) || begindate.equals("")) {
			return "deliverpayup/deliverpayuplist";
		}
		List<GotoClassAuditing> gcaList = this.gotoClassAuditingDAO.getGotoClassAuditingByAuditingInfo(branchid, begindate, enddate, deliverpayupapproved, deliver);
		List<JSONObject> viewList = new ArrayList<JSONObject>();

		String shenheState = DeliverPayupArrearageapprovedEnum.WeiShenHe.getText();
		List<User> uList = this.userDAO.getAllUser();
		String deliverName = "";
		String branchName = "";
		String deliverpayuptype = "";
		Map<Long, BigDecimal> gcaidToCodPosSumMap = this.getCodPosAmount(gcaList);

		for (GotoClassAuditing gca : gcaList) {
			for (User u : uList) {
				if (u.getUserid() == gca.getDeliverealuser()) {
					deliverName = u.getRealname();
					break;
				}
			}
			for (Branch b : bList) {
				if (b.getBranchid() == gca.getBranchid()) {
					branchName = b.getBranchname();
					break;
				}
			}
			for (DeliverPayuptypeEnum dp : DeliverPayuptypeEnum.values()) {
				if (gca.getDeliverpayuptype() == dp.getValue()) {
					deliverpayuptype = dp.getText();
					break;
				}
			}
			for (DeliverPayupArrearageapprovedEnum de : DeliverPayupArrearageapprovedEnum.values()) {
				if (de.getValue() == gca.getDeliverpayupapproved()) {
					shenheState = de.getText();
					break;
				}
			}

			JSONObject view = new JSONObject();
			view.put("id", gca.getId());
			view.put("deliverName", deliverName);// 小件员名称
			view.put("branchName", branchName);// 所属站点名称
			view.put("deliverpayuptype", deliverpayuptype);// 交款类型
			// 小票号
			view.put("payupNo", ((gca.getDeliverpayupbanknum() == null) || "".equals(gca.getDeliverpayupbanknum())) ? "无" : gca.getDeliverpayupbanknum());
			view.put("payupamount", gca.getPayupamount().doubleValue());// 应交款现金
			view.put("deliverPayupamount", gca.getDeliverpayupamount().doubleValue());// 交款金额
																						// 现金等
			view.put("deliverAccount", gca.getDeliverAccount().doubleValue());// 小件员交款时的金额
			view.put("deliverPayuparrearage", gca.getDeliverpayuparrearage().negate().doubleValue());// 现金交款产生的欠款
																										// 由于前台显示标题是欠款
																										// 所以将欠款金额是负数需要转换成正数显示
			view.put("payupamount_pos", gca.getPayupamount_pos().doubleValue());// 应交款POS
			view.put("deliverPayupamount_pos", gca.getDeliverpayupamount_pos().doubleValue());// 交款金额
																								// POS
			view.put("deliverPosAccount", gca.getDeliverPosAccount().doubleValue());// 小件员交款时的POS
			view.put("deliverPayuparrearage_pos", gca.getDeliverpayuparrearage_pos().negate().doubleValue());// POS交款产生的欠款
																												// 由于前台显示标题是欠款
																												// 所以将欠款金额是负数需要转换成正数显示
			view.put("shenheState", shenheState);// 审核状态
			view.put("auditingtime", gca.getAuditingtime());// 交款时间
			view.put("payupaddress", gca.getPayupaddress());// 交款地址
			view.put("checkremark", gca.getCheckremark());// 交款审核备注
			view.put("gcaid", gca.getId());// 归班审核编号
			view.put("updateTime", gca.getUpdatetime());// 归班审核编号
			this.fillCodPosSum(view, gcaidToCodPosSumMap, gca);// 添加codpos汇总.

			viewList.add(view);
		}
		model.addAttribute("delivers", this.getDelivers(branchid));
		model.addAttribute("deliver", deliver);
		model.addAttribute("viewList", viewList);

		return "deliverpayup/deliverpayuplist";
	}

	private void fillCodPosSum(JSONObject view, Map<Long, BigDecimal> gcaidToCodPosSumMap, GotoClassAuditing gca) {
		BigDecimal codPosSum = gcaidToCodPosSumMap.get(Long.valueOf(gca.getId()));
		view.put("codpossum", codPosSum == null ? Double.valueOf(0) : Double.valueOf(codPosSum.doubleValue()));
	}

	private Map<Long, BigDecimal> getCodPosAmount(List<GotoClassAuditing> gcaList) {
		if ((gcaList == null) || gcaList.isEmpty()) {
			return new HashMap<Long, BigDecimal>();
		}
		return this.getDeliveryStateDAO().queryCodPosAmountSum(this.getGcaidList(gcaList));
	}

	private List<Long> getGcaidList(List<GotoClassAuditing> gcaList) {
		List<Long> gcaidList = new ArrayList<Long>();
		for (GotoClassAuditing gca : gcaList) {
			gcaidList.add(Long.valueOf(gca.getId()));
		}
		return gcaidList;
	}

	private List<User> getDelivers(long branchId) {
		String roleids = "2,4";
		return this.userDAO.getUserByRolesAndBranchid(roleids, branchId);
	}

	/**
	 * 审核功能（已通过、未通过）
	 *
	 * @param model
	 * @param controlStr
	 * @param mackStr
	 * @param deliverpaystate
	 * @param branchid
	 * @param begindate
	 * @param enddate
	 * @param deliverpayupapproved
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	public String update(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr,
			@RequestParam(value = "mackStr", required = false, defaultValue = "") String mackStr, @RequestParam(value = "deliverpaystate", required = false, defaultValue = "0") long deliverpaystate,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverpayupapproved", required = false, defaultValue = "-1") long deliverpayupapproved,
			@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, @RequestParam(value = "selectedDeliver", required = false, defaultValue = "-1") long deliver,
			HttpServletRequest request) {
		String[] mStr = null;
		List<Long> gcaids = new ArrayList<Long>();
		if (controlStr.length() > 0) {

			if ((mackStr != null) && (mackStr.length() > 0) && (mackStr.indexOf("P:P") > -1)) {
				mStr = mackStr.split("P:P");
			}
			if ((mStr != null) && (mStr.length > 0)) {
				for (String mst : mStr) {
					String[] payM = mst.split("T:T");
					long gcaid = Long.parseLong(payM[0]);
					gcaids.add(gcaid);
					try {
						if (payM.length == 2) {
							this.gotoClassAuditingDAO.updateStateAndRemark(gcaid, payM[1], deliverpaystate);
						} else {
							this.gotoClassAuditingDAO.updateStateAndRemark(gcaid, "", deliverpaystate);
						}
					} catch (NumberFormatException e) {
						this.logger.error("/deliverpayup/update", e);
					}
				}
			}
		}
		// 审核状态数据中为int.
		this.cwbOrderService.sendFinanceAuditJMS(gcaids, (int) deliverpaystate);
		return this.deliverpayuplist(model, branchid, begindate, enddate, deliverpayupapproved, deliver);
	}

	@RequestMapping("/updateMoney")
	public String updateMoney(Model model, @RequestParam(value = "page", required = false, defaultValue = "0") long page, @RequestParam(value = "gcaid", required = false, defaultValue = "0") long id,
			@RequestParam(value = "money", required = false, defaultValue = "0") String deliverpayupamount, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "deliverealuser", required = false, defaultValue = "-1") long deliverealuser,
			@RequestParam(value = "deliverpayupapproved", required = false, defaultValue = "-1") long deliverpayupapproved, HttpServletRequest request) {
		if (id > 0) {
			this.userService.updateStateAndMoney(id, deliverpayupamount);
		}
		this.logger.info("修改金额提交--金额：" + deliverpayupamount + ",当前操作人：" + this.getSessionUser().getRealname() + ",express_ops_goto_class_auditing对应id为" + id);
		return this.paymentfordeliverystate(model, page, begindate, enddate, deliverealuser, deliverpayupapproved);
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[16]; // 导出的列名
		String[] cloumnName2 = new String[16]; // 导出的英文列名

		this.exportService.SetDeliverPayupFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "投诉信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			long branchid = request.getParameter("branchid1") == null ? 0 : Long.parseLong(request.getParameter("branchid1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			long deliverpayupapproved = request.getParameter("deliverpayupapproved1") == null ? 0 : Long.parseLong(request.getParameter("deliverpayupapproved1").toString());
			String branchname = request.getParameter("branchname1") == null ? "" : request.getParameter("branchname1").toString();
			String branchids = "0";
			if (branchname.length() > 0) {
				List<Branch> branchList = this.branchDao.getBranchByBranchnameMoHu(branchname);
				for (Branch b : branchList) {
					branchids += "," + b.getBranchid();
				}
			}

			final List<GotoClassAuditing> gcaList = this.gotoClassAuditingDAO.getGotoClassAuditingByAuditingtimeAndBranchid(branchid, begindate, enddate, deliverpayupapproved, branchids);

			final List<User> userList = this.userDAO.getAllUser();
			final List<Branch> branchList = this.branchDao.getAllBranches();

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < gcaList.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = DeliverPayUpamountController.this.exportService.setObjectC(cloumnName3, request1, gcaList, a, i, k, branchList, userList);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 小件员帐户列表跳转
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 *            所属机构
	 * @param realname
	 *            昵称 LIKE
	 * @param uee
	 *            人员工作状态
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliveraccountList/{page}")
	public String deliveraccountList(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname, @RequestParam(value = "uee", required = false, defaultValue = "-1") int uee,
			HttpServletRequest request) {
		String deliverAccountStartStr = request.getParameter("deliverAccountStart");
		String deliverAccountEndStr = request.getParameter("deliverAccountEnd");
		String deliverPosAccountStartStr = request.getParameter("deliverPosAccountStart");
		String deliverPosAccountEndStr = request.getParameter("deliverPosAccountEnd");
		model.addAttribute("userList",
				this.userDAO.getDeliverAccountList(page, branchid, realname, deliverAccountStartStr, deliverAccountEndStr, deliverPosAccountStartStr, deliverPosAccountEndStr, uee));
		Long count = this.userDAO.getDeliverAccountListCount(branchid, realname, deliverAccountStartStr, deliverAccountEndStr, deliverPosAccountStartStr, deliverPosAccountEndStr, uee);
		List<Branch> bList = this.branchDao.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<Branch> branchAllZhanDianList = this.branchDao.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());
		model.addAttribute("branchAllZhanDianList", branchAllZhanDianList);
		model.addAttribute("bList", bList);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		return "deliverpayup/deliveraccountList";
	}

	/**
	 * 小件员交易明细列表
	 *
	 * @param model
	 * @param deliverid
	 *            小件员id
	 * @param page
	 * @param startTime
	 *            交易开始时间
	 * @param endTime
	 *            交易结束时间
	 * @param type
	 *            交易类型
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliveraccountDetailList/{deliverid}/{page}")
	public String deliveraccountDetailList(Model model, @PathVariable(value = "deliverid") long deliverid, @PathVariable(value = "page") long page,
			@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type, HttpServletRequest request) {

		model.addAttribute("detailList", this.financeDeliverPayUpDetailDAO.getDetailListByDeliveridAndType(page, deliverid, startTime, endTime, type));
		Long count = this.financeDeliverPayUpDetailDAO.getDetailListByDeliveridAndTypeCount(deliverid, startTime, endTime, type);
		model.addAttribute("user", this.userDAO.getAllUserByid(deliverid));
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("bList", this.branchDao.getBranchBySiteType(BranchEnum.ZhanDian.getValue()));
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		return "deliverpayup/deliveraccountDetailList";
	}

	/**
	 * 小件员调账页面显示
	 *
	 * @param model
	 * @param deliverid
	 *            小件员id
	 * @return
	 */
	@RequestMapping("/deliveraccountEdit/{deliverid}")
	public String deliveraccountEdit(Model model, @PathVariable(value = "deliverid") long deliverid) {
		model.addAttribute("user", this.userDAO.getAllUserByid(deliverid));
		model.addAttribute("bList", this.branchDao.getBranchBySiteType(BranchEnum.ZhanDian.getValue()));
		return "deliverpayup/deliveraccountEdit";
	}

	/**
	 * 提交小件员调账内容
	 *
	 * @param model
	 * @param deliverid
	 * @return
	 */
	@RequestMapping("/saveDeliveraccountEdit/{deliverid}")
	public @ResponseBody String saveDeliveraccountEdit(Model model, @PathVariable(value = "deliverid") long deliverid,
			@RequestParam(value = "deliverpayupamount", required = false, defaultValue = "0") BigDecimal deliverpayupamount,
			@RequestParam(value = "deliverpayupamount_pos", required = false, defaultValue = "0") BigDecimal deliverpayupamount_pos,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark) {
		if ((deliverpayupamount.compareTo(BigDecimal.ZERO) == 0) && (deliverpayupamount_pos.compareTo(BigDecimal.ZERO) == 0)) {
			return "{\"errorCode\":1,\"error\":\"账户没有变动\"}";
		}
		try {
			this.userService.editDeliverAccount(deliverid, deliverpayupamount, deliverpayupamount_pos, remark, this.getSessionUser());
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败，数据处理过程中出现异常，请重试！\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	private DeliveryStateDAO getDeliveryStateDAO() {
		return this.deliveryStateDAO;
	}

}
