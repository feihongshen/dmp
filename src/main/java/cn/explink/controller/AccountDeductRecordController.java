package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountDeducDetailDAO;
import cn.explink.dao.AccountDeductRecordDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeducDetailView;
import cn.explink.domain.AccountDeductRecord;
import cn.explink.domain.AccountDeductRecordView;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.AccountDeducDetailService;
import cn.explink.service.AccountDeductRecordService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

/**
 * 结算扣款模式
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping("/accountdeductrecord")
public class AccountDeductRecordController {
	@Autowired
	UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	AccountDeductRecordService accountDeductRecordService;
	@Autowired
	AccountDeducDetailService accountDeducDetailService;
	@Autowired
	AccountDeducDetailDAO accountDeducDetailDAO;
	@Autowired
	AccountDeductRecordDAO accountDeductRecordDAO;
	@Autowired
	ExportService exportService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 账户信息列表
	 * 
	 * @param model
	 * @param page
	 * @param branchname
	 * @param request
	 * @return
	 */
	@RequestMapping("/branchList/{page}")
	public String branchList(Model model, @PathVariable("page") long page, @RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, HttpServletRequest request) {
		model.addAttribute("branchList",
				branchDAO.getBranchByAccountbranch(page, branchname, getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid()));
		model.addAttribute("page_obj",
				new Page(branchDAO.getBranchByAccountbranchCount(branchname, getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid()), page,
						Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/accountdeductrecord/branchList";
	}

	/**
	 * 充值、调账窗口弹出
	 * 
	 * @param model
	 * @param branchid
	 * @param request
	 * @return
	 */
	@RequestMapping("/recharge/{branchid}")
	public @ResponseBody String recharge(Model model, @PathVariable("branchid") long branchid, HttpServletRequest request) {
		Branch branch = new Branch();
		branch = branchDAO.getBranchByBranchid(branchid);
		return "{\"errorCode\":0,\"branchname\":\"" + branch.getBranchname() + "\",\"credit\":\"" + branch.getCredit() + "\",\"balance\":\"" + branch.getBalance() + "\",\"debt\":\""
				+ branch.getDebt() + "\"}";
	}

	/**
	 * 站点充值
	 * 
	 * @param model
	 * @param branchid
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveRecharge")
	public @ResponseBody String saveRecharge(Model model, HttpServletRequest request) {
		try {
			accountDeductRecordService.saveRecharge(request, getSessionUser());
			return "{\"errorCode\":0,\"error\":\"充值成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 站点充值
	 * 
	 * @param model
	 * @param branchid
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveTiaoZhang")
	public @ResponseBody String saveTiaoZhang(Model model, HttpServletRequest request) {
		try {
			accountDeductRecordService.saveTiaoZhang(request, getSessionUser());
			return "{\"errorCode\":0,\"error\":\"调账成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 预扣款冲正页面
	 * 
	 * @param model
	 * @param branchid
	 * @param flowordertype
	 * @param strtime
	 * @param endtime
	 * @param cwb
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailList")
	public String detailList(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "") String flowordertype, @RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			HttpServletRequest request) {
		// 当前站点按权限 扣款类型过滤
		List<Branch> branchList = branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
		List<User> userList = userDAO.getAllUser();
		// String branchids="";
		// //如果为选择站点则匹配用户权限
		// if(branchid==0){
		// if(branchList!=null&&!branchList.isEmpty()){
		// for(Branch listb:branchList){
		// branchids+=listb.getBranchid()+",";
		// }
		// }
		// if(!"".equals(branchids)){
		// branchids=branchids.substring(0,branchids.lastIndexOf(","));
		// }
		// }else{
		// branchids=String.valueOf(branchid);
		// }
		if ("".equals(flowordertype)) {
			flowordertype = AccountFlowOrderTypeEnum.ZhongZhuan.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();
		}

		if (branchid > 0) {
			// 处理多条订单
			StringBuffer cwbBuffer = new StringBuffer();
			if (!"".equals(cwb.trim())) {
				for (String o : cwb.split("\r\n")) {
					if (o.trim().length() == 0) {
						continue;
					}
					cwbBuffer.append("'").append(o).append("',");
				}
				cwb = cwbBuffer.substring(0, cwbBuffer.lastIndexOf(","));
			}
			List<AccountDeducDetail> adList = accountDeducDetailDAO.getDeducDetailByList(branchid, flowordertype, cwb.trim(), strtime.trim(), endtime.trim(), "");
			model.addAttribute("detailList", accountDeducDetailService.getAccountDeducDetailListByBranchAndUser(adList, branchList, userList));
		}
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("branchList", branchList);
		return "/accountdeductrecord/detailList";
	}

	/**
	 * 预扣款冲正审核
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveRecord")
	public @ResponseBody String saveRecord(Model model, @RequestParam(value = "flowordertype", required = false, defaultValue = "") String flowordertype, HttpServletRequest request) {
		try {
			accountDeductRecordService.saveRecord(request, getSessionUser(), flowordertype);
			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 账户基本信息
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param strtime
	 * @param endtime
	 * @param request
	 * @return
	 */
	@RequestMapping("/accountBasic/{page}")
	public String accountBasic(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "starttimehz", required = false, defaultValue = "") String starttimehz, @RequestParam(value = "endtimehz", required = false, defaultValue = "") String endtimehz,
			@RequestParam(value = "recordtype", required = false, defaultValue = "0") long recordtype, @RequestParam(value = "flag", required = false, defaultValue = "1") String flag,// 第几个页卡显示
			HttpServletRequest request) {
		if ("".equals(strtime) && "".equals(endtime)) {
			strtime = DateDayUtil.getDateBefore("", -30) + " 00:00:00";
			endtime = DateDayUtil.getDateBefore("", 0) + " 23:59:59";
		} else {
			strtime += " 00:00:00";
			endtime += " 23:59:59";
		}
		if ("".equals(starttimehz) && "".equals(endtimehz)) {
			starttimehz = DateDayUtil.getDateBefore("", -7) + " 00:00:00";
			endtimehz = DateDayUtil.getDateBefore("", 0) + " 23:59:59";
		} else {
			starttimehz += " 00:00:00";
			endtimehz += " 23:59:59";
		}
		Branch userbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		// 判断当前操作用户类型为站点
		if (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			branchid = getSessionUser().getBranchid();
		} else {// 财务登录
				// 当前站点按权限 扣款类型过滤
			model.addAttribute("branchList", branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid()));
		}

		String sumOfamount = "0.00";
		if (branchid > 0) {
			model.addAttribute("branch", branchDAO.getBranchByBranchid(branchid));
			sumOfamount = accountDeductRecordDAO.sumOfamount(branchid);
		}
		model.addAttribute("sumOfamount", sumOfamount);
		model.addAttribute("branchid", branchid);
		List<AccountDeductRecord> huizongwenzi = accountDeductRecordDAO.getHuiZong(branchid, starttimehz, endtimehz);
		StringBuffer sb = new StringBuffer();
		if (huizongwenzi != null && !huizongwenzi.isEmpty()) {
			sb.append("合计汇总    ");
			for (AccountDeductRecord l : huizongwenzi) {
				// 循环类型枚举
				for (AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()) {
					if (l.getRecordtype() == ft.getValue()) {
						sb.append(ft.getText() + "：" + l.getFee() + "元 ，" + l.getNums() + "单；");
					}
				}
			}
		}
		List<User> userList = userDAO.getAllUser();
		model.addAttribute("recordList", accountDeductRecordService.getAccountDeductRecordListByBranchAndUser(userList, page, branchid, strtime, endtime, recordtype));
		model.addAttribute("page_obj", new Page(accountDeductRecordDAO.getAccountDeductRecordCount(branchid, strtime, endtime, recordtype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("huizongList", accountDeductRecordDAO.getAccountDeductRecordHuiZong(branchid, starttimehz, endtimehz));
		model.addAttribute("page", page);
		model.addAttribute("flag", flag);
		model.addAttribute("strtime", strtime.substring(0, 11));
		model.addAttribute("endtime", endtime.substring(0, 11));
		model.addAttribute("starttimehz", starttimehz.substring(0, 11));
		model.addAttribute("endtimehz", endtimehz.substring(0, 11));
		model.addAttribute("message", sb.toString());
		return "/accountdeductrecord/accountBasic";
	}

	// /**
	// * 账户加款扣款记录明细
	// * @param model
	// * @param page
	// * @param branchid
	// * @param recordtype
	// * @param strtime
	// * @param endtime
	// * @param request
	// * @return
	// */
	// @RequestMapping("/huizong")
	// public String huizong(Model model,
	// @RequestParam(value ="branchid",required = false,defaultValue="0") long
	// branchid,
	// @RequestParam(value ="starttime",required = false,defaultValue="")String
	// starttime,
	// @RequestParam(value ="endtime",required = false,defaultValue="")String
	// endtime,
	// HttpServletRequest request){
	// if("".equals(starttime)&&"".equals(endtime)){
	// starttime=DateDayUtil.getDateBefore("",-1)+" 00:00:00";
	// endtime=DateDayUtil.getDateBefore("",0)+" 23:59:59";
	// }else{
	// starttime+=" 00:00:00";
	// endtime+=" 23:59:59";
	// }
	// // List<User> userList = userDAO.getAllUser();
	//
	// List<Branch> branchList
	// =branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(),3,String.valueOf(BranchEnum.ZhanDian.getValue()),getSessionUser().getUserid());
	// Branch
	// userbranch=branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
	// //判断当前操作用户类型为站点
	// if(userbranch.getSitetype()==BranchEnum.ZhanDian.getValue()){
	// branchid=getSessionUser().getBranchid();
	// }else{//财务登录
	// //当前站点按权限 扣款类型过滤
	// model.addAttribute("branchList",branchList);
	// }
	// if(branchid>0){
	// model.addAttribute("branch",branchDAO.getBranchByBranchid(branchid));
	// }
	// //
	// //
	// model.addAttribute("recordList",accountDeductRecordService.getAccountDeductRecordListByBranchAndUser(userList,page,branchid,strtime,endtime,recordtype));
	// model.addAttribute("starttime",starttime.substring(0,11));
	// model.addAttribute("endtime",endtime.substring(0,11));
	// return "/accountdeductrecord/huizong";
	// }

	/**
	 * 根据recordid查找账务明细
	 * 
	 * @param model
	 * @param page
	 * @param recordid
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailByRecordList/{page}")
	public String detailByRecordList(Model model, @PathVariable("page") long page, @RequestParam(value = "recordid", required = false, defaultValue = "0") long recordid, HttpServletRequest request) {
		// 当前站点按权限 扣款类型过滤
		List<Branch> branchList = branchDAO.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue()));
		// List<Branch> branchList
		// =branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(),3,String.valueOf(BranchEnum.ZhanDian.getValue()),getSessionUser().getUserid());
		List<User> userList = userDAO.getAllUser();

		List<AccountDeducDetail> adList = accountDeducDetailDAO.getDeducDetailRecordPage(page, recordid);

		model.addAttribute("recordList", accountDeducDetailService.getAccountDeducDetailListByBranchAndUser(adList, branchList, userList));
		model.addAttribute("page_obj", new Page(accountDeducDetailDAO.getDeducDetailRecordCount(recordid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/accountdeductrecord/accountDetailList";
	}

	/**
	 * 根据recordid导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param recordid
	 * @param request
	 */
	@RequestMapping("/exportByRecord")
	public void exportByRecord(Model model, HttpServletResponse response, @RequestParam(value = "recordid", required = false, defaultValue = "0") long recordid, HttpServletRequest request) {
		// 当前站点按权限 扣款类型过滤
		List<Branch> branchList = branchDAO.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue()));
		// List<Branch> branchList
		// =branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(),3,String.valueOf(BranchEnum.ZhanDian.getValue()),getSessionUser().getUserid());
		List<User> userList = userDAO.getAllUser();
		String[] cloumnName1 = new String[7]; // 导出的列名
		String[] cloumnName2 = new String[7]; // 导出的英文列名

		exportService.SetAccountDeducDetailFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			List<AccountDeducDetail> adList = accountDeducDetailDAO.getDeducDetailRecordList(recordid);
			List<AccountDeducDetail> clist = accountDeducDetailService.getAccountDeducDetailListByBranchAndUser(adList, branchList, userList);
			final List<AccountDeducDetailView> list = accountDeducDetailService.getViewByAccountDeducDetail(clist);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDeducDetail(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 预扣款冲正导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param branchid
	 * @param flowordertype
	 * @param request
	 */
	@RequestMapping("/exportByRecordByCZ")
	public void exportByRecordByBranch(Model model, HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "stime", required = false, defaultValue = "") String stime, @RequestParam(value = "edtime", required = false, defaultValue = "") String edtime,
			@RequestParam(value = "idsStr", required = false, defaultValue = "") String idsStr, HttpServletRequest request) {
		// 当前站点按权限 扣款类型过滤
		List<Branch> branchList = branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
		List<User> userList = userDAO.getAllUser();
		// String branchids="";
		// //如果为选择站点则匹配用户权限
		// if(branchid==0){
		// if(branchList!=null&&!branchList.isEmpty()){
		// for(Branch listb:branchList){
		// branchids+=listb.getBranchid()+",";
		// }
		// }
		// if(!"".equals(branchids)){
		// branchids=branchids.substring(0,branchids.lastIndexOf(","));
		// }
		// }else{
		// branchids=String.valueOf(branchid);
		// }

		if ("".equals(type)) {
			type = AccountFlowOrderTypeEnum.ZhongZhuan.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();
		}
		if (branchid > 0) {
			// 处理多条订单
			StringBuffer cwbBuffer = new StringBuffer();
			if (!"".equals(cwbs.trim())) {
				for (String o : cwbs.split("\r\n")) {
					if (o.trim().length() == 0) {
						continue;
					}
					cwbBuffer.append("'").append(o).append("',");
				}
				cwbs = cwbBuffer.substring(0, cwbBuffer.lastIndexOf(","));
			}

			String[] cloumnName1 = new String[7]; // 导出的列名
			String[] cloumnName2 = new String[7]; // 导出的英文列名

			exportService.SetAccountDeducDetailFields(cloumnName1, cloumnName2);
			final String[] cloumnName = cloumnName1;
			final String[] cloumnName3 = cloumnName2;
			final HttpServletRequest request1 = request;
			String sheetName = "订单信息"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
			try {
				// 查询出数据
				List<AccountDeducDetail> adList = accountDeducDetailDAO.getDeducDetailByList(branchid, type, cwbs.trim(), stime.trim(), edtime.trim(), idsStr);
				List<AccountDeducDetail> clist = accountDeducDetailService.getAccountDeducDetailListByBranchAndUser(adList, branchList, userList);
				final List<AccountDeducDetailView> list = accountDeducDetailService.getViewByAccountDeducDetail(clist);

				ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
					@Override
					public void fillData(Sheet sheet, CellStyle style) {
						for (int k = 0; k < list.size(); k++) {
							Row row = sheet.createRow(k + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								// 给导出excel赋值
								a = exportService.setAccountDeducDetail(cloumnName3, request1, list, a, i, k);
								cell.setCellValue(a == null ? "" : a.toString());
							}
						}
					}
				};
				excelUtil.excel(response, cloumnName, sheetName, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 帐户信息账务明细导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param recordid
	 * @param request
	 */
	@RequestMapping("/exportAccountList")
	public void exportAccountList(Model model, HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "recordtype", required = false, defaultValue = "") long recordtype, @RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, HttpServletRequest request) {
		strtime += " 00:00:00";
		endtime += " 23:59:59";

		String[] cloumnName1 = new String[10]; // 导出的列名
		String[] cloumnName2 = new String[10]; // 导出的英文列名

		exportService.SetAccountDeductRecordFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "账务明细"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			List<AccountDeductRecord> clist = accountDeductRecordDAO.getAccountDeductRecordAndUserNamePage(0, branchid, strtime, endtime, recordtype);
			final List<AccountDeductRecordView> list = accountDeductRecordService.getViewByAccountDeductRecord(clist);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDeductRecord(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 帐户信息账务汇总导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param recordid
	 * @param request
	 */
	@RequestMapping("/exportSumList")
	public void exportSumList(Model model, HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "recordtype", required = false, defaultValue = "") long recordtype, @RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, HttpServletRequest request) {
		strtime += " 00:00:00";
		endtime += " 23:59:59";

		String[] cloumnName1 = new String[5]; // 导出的列名
		String[] cloumnName2 = new String[5]; // 导出的英文列名

		exportService.SetAccountSumFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "账务汇总"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			List<AccountDeductRecord> clist = accountDeductRecordDAO.getAccountDeductRecordHuiZong(branchid, strtime, endtime);
			final List<AccountDeductRecordView> list = accountDeductRecordService.getViewByHuiZong(clist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDeductRecord(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 账户管理导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param request
	 */
	@RequestMapping("/exportBranchList")
	public void exportBranchList(Model model, HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = new String[7]; // 导出的列名
		String[] cloumnName2 = new String[7]; // 导出的英文列名

		exportService.SetBranchListFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "账户管理"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			List<Branch> clist = branchDAO.getBranchByAccountbranch(0, "", getSessionUser().getBranchid(), 3, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
			final List<Branch> list = accountDeductRecordService.getViewByBranchList(clist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setBranchList(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
