package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Exportmould;
import cn.explink.domain.SetExportField;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.DeliveryService;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@Controller
@RequestMapping("/monitordelivery")
public class MonitorDeliveryController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorService monitorService;
	@Autowired
	DeliveryService deliveryService;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	MonitorDAO monitorDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;

	// 第一页
	@RequestMapping("/date/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "controlStrType", required = false, defaultValue = "") String controlStrType, @RequestParam(value = "timeToid", required = false, defaultValue = "6") int timeToid,
			HttpServletResponse response, HttpServletRequest request) {

		setDeliverylist(model, request, controlStrType, branchid, timeToid);
		return "/monitor/delivery/deliveryMonitor";
	}

	// 第一页导出
	@RequestMapping("/dateExport/{page}")
	public String dateExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String controlStrType = request.getSession().getAttribute("controlStrType").toString();
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		setDeliverylist(model, request, controlStrType, branchid, timeToid);
		return "/monitor/delivery/deliveryExpo";

	}

	// 查看详细页
	@RequestMapping("/dateshow/{page}")
	public String dateShow(Model model, @PathVariable("page") long page, @RequestParam(value = "flowType", required = false, defaultValue = "") String flowType, HttpServletResponse response,
			HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		request.getSession().setAttribute("flowType", flowType);
		User user = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = exportmouldDAO.getAllExportmouldByUser(user.getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("flowType", flowType);
		if ("6".equals(flowType)) {
			model.addAttribute("flowTypeStr", "此处统计的是：库房已经做“出库扫描”，距离当前时间超过" + timeToid + "小时 ,状态还没变化的订单");
		} else if ("7".equals(flowType)) {
			model.addAttribute("flowTypeStr", "此处统计的是：站点已经做“到站扫描”，距离当前时间超过" + timeToid + "小时 ,状态还没变化的订单");
		} else if ("9".equals(flowType)) {
			model.addAttribute("flowTypeStr", "此处统计的是：小件员已经领货，距离当前时间超过" + timeToid + "小时 ,状态还没变化的订单");
		}
		showDate(model, page, branchid, Long.parseLong(flowType), timeToid);
		return "/monitor/delivery/deliveryShow";

	}

	// 详细页导出
	@RequestMapping("/dateShowExport")
	public void dateShowExp(Model model, HttpServletResponse response, HttpServletRequest request) {
		/*
		 * String dmpid =
		 * request.getSession().getAttribute("dmpid")==null?"":request
		 * .getSession().getAttribute("dmpid").toString(); int showphoneflag
		 * =getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		 * model.addAttribute("usershowphoneflag", showphoneflag); long branchid
		 * =
		 * Long.parseLong(request.getSession().getAttribute("branchid").toString
		 * ()); int timeToid =
		 * Integer.parseInt(request.getSession().getAttribute
		 * ("timeToid").toString()); long flowType =
		 * Long.parseLong(request.getSession
		 * ().getAttribute("flowType").toString());
		 * 
		 * String[] cloumnName1={}; String[] cloumnName2={}; String
		 * exportmould2=request.getParameter("exportmould2");
		 * if(exportmould2!=null&&!"0".equals(exportmould2)){ //选择模板
		 * List<SetExportField> listSetExportField =
		 * exportmouldDAO.getSetExportFieldByStrs(exportmould2); cloumnName1 =
		 * new String[listSetExportField.size()]; cloumnName2 = new
		 * String[listSetExportField.size()]; for (int k=0,j = 0; j <
		 * listSetExportField.size(); j++,k++) { cloumnName1[k]=
		 * listSetExportField.get(j).getFieldname(); cloumnName2[k]=
		 * listSetExportField.get(j).getFieldenglishname(); }
		 * 
		 * } else { List<SetExportField> listSetExportField =
		 * exportmouldDAO.getSetExportFieldByStrs("0"); cloumnName1 = new
		 * String[listSetExportField.size()]; cloumnName2 = new
		 * String[listSetExportField.size()]; for (int k = 0, j = 0; j <
		 * listSetExportField.size(); j++, k++) { cloumnName1[k] =
		 * listSetExportField.get(j).getFieldname(); cloumnName2[k] =
		 * listSetExportField.get(j).getFieldenglishname(); } }
		 * 
		 * 
		 * final String[] cloumnName = cloumnName1; final String[] cloumnName3 =
		 * cloumnName2; String sheetName = "订单信息"; //sheet的名称 SimpleDateFormat
		 * df = new SimpleDateFormat("yyyy-MM-dd_HH-MM-ss"); String fileName =
		 * "Delivery_"+df.format(new Date())+".xlsx"; //文件名 try { //查询出数据 final
		 * SqlRowSet r = monitorService.getDeliveryRusulset(-1,branchid,
		 * flowType, timeToid); ExcelUtils excelUtil = new ExcelUtils(){
		 * //生成工具类实例，并实现填充数据的抽象方法
		 * 
		 * @Override public void fillData(Sheet sheet, CellStyle style) { int
		 * k=0; while(r.next()){ Row row = sheet.createRow(k+1);
		 * row.setHeightInPoints((float) 15); for (int i = 0; i <
		 * cloumnName.length; i++) { Cell cell = row.createCell((short)i);
		 * cell.setCellStyle(style); Object a = null; //给导出excel赋值 a=
		 * exportService.setObjectA(cloumnName3, r, i);
		 * cell.setCellValue(a==null?"":a.toString()); } k++; } } };
		 * excelUtil.excel(response, cloumnName, sheetName,fileName);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	// 返回第一页面
	@RequestMapping("/back")
	public String goback(Model model, HttpServletResponse response, HttpServletRequest request) {
		String controlStrType = request.getSession().getAttribute("controlStrType").toString();
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		setDeliverylist(model, request, controlStrType, branchid, timeToid);
		return "/monitor/delivery/deliveryMonitor";
	}

	// ======以下都是封装方法=============/////

	private void showDate(Model model, long page, long branchid, long flowType, int timeToid) {
		model.addAttribute("showDateList", monitorService.getDeliveryShowList(page, branchid, flowType, timeToid));
		model.addAttribute("page_obj", new Page(monitorService.getCountDeliveryShowList(page, branchid, flowType, timeToid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
	}

	private void showDateExp(Model model, long branchid, long flowType, int timeToid) {
		model.addAttribute("showDateList", monitorService.getDeliveryShowListExp(branchid, flowType, timeToid));

	}

	// 保存基本查询条件的session
	private void setSetions(HttpServletRequest request, String controlStrType, long branchid, int timeToid) {
		request.getSession().setAttribute("controlStrType", controlStrType);
		request.getSession().setAttribute("branchid", branchid);
		request.getSession().setAttribute("timeToid", timeToid);
	}

	private void setDeliverylist(Model model, HttpServletRequest request, String controlStrType, long branchid, int timeToid) {
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidPram = branchid;
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		Branch nowbranch = getDmpDAO.getNowBranch(branchidSession);
		if (nowbranch != null) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", nowbranch);
				branchidPram = branchidSession;
			}
		}
		if (branchid != -1) {
			nowbranch = getDmpDAO.getNowBranch(branchidPram);
		} else {
			nowbranch.setBranchname("全部");
		}
		model.addAttribute("branchidSession", branchidPram);

		String typeStr = controlStrType.equals("") ? "" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "" : controlStrType.substring(0, controlStrType.length() - 1);
		Map typeNameMap = new HashMap();
		// typeNameMap.put(FlowOrderTypeEnum.DaoRuShuJu.getValue(),
		// FlowOrderTypeEnum.DaoRuShuJu.getText());
		// typeNameMap.put(FlowOrderTypeEnum.TiHuo.getValue(),
		// FlowOrderTypeEnum.TiHuo.getText());
		// typeNameMap.put(FlowOrderTypeEnum.RuKu.getValue(),
		// FlowOrderTypeEnum.RuKu.getText());
		typeNameMap.put(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出库未到站");
		typeNameMap.put(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "到站未领货");
		typeNameMap.put(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "领货未反馈");

		Map typeNameMapPram = new HashMap();
		String typenameList = "";
		String[] typeStrs = typeStr.split(",");
		for (int i = 0; i < typeStrs.length; i++) {
			typeNameMapPram.put(typeStrs[i], typeNameMap.get(Integer.parseInt(typeStrs[i])));
			typenameList += "'" + typeNameMap.get(Integer.parseInt(typeStrs[i])) + "',";
		}
		typenameList = typenameList.length() > 0 ? typenameList.substring(0, typenameList.length() - 1) : "";
		List deliveryList = monitorService.getDeliveryList(branchidPram, typeStr, timeToid);

		model.addAttribute("timeNow", timeToid);
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("branchNow", nowbranch);
		model.addAttribute("typeList", typeStr);
		model.addAttribute("typeNameMapPram", typeNameMapPram);
		model.addAttribute("typenameList", typenameList);
		model.addAttribute("deliveryList", deliveryList);

		setSetions(request, controlStrType, branchidPram, timeToid);

	}

}