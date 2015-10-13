package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Complaint;
import cn.explink.service.ComplaintService;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtilsOld;
import cn.explink.util.Page;

@RequestMapping("/complaint")
@Controller
public class ComplaintController {

	@Autowired
	ComplaintDAO complaintDao;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	ComplaintService complaintService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportService exportService;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "complainttypeid", required = false, defaultValue = "-1") long complainttypeid,
			@RequestParam(value = "checkflag", required = false, defaultValue = "-1") long checkflag, @RequestParam(value = "complaintflag", required = false, defaultValue = "-1") long complaintflag,
			@RequestParam(value = "complaintcustomerid", required = false, defaultValue = "") String complaintcustomerid,
			@RequestParam(value = "complaintcontactman", required = false, defaultValue = "") String complaintcontactman,
			@RequestParam(value = "complaintphone", required = false, defaultValue = "") String complaintphone,
			@RequestParam(value = "complaintuserid", required = false, defaultValue = "") String complaintuserid,
			@RequestParam(value = "complaintuserdesc", required = false, defaultValue = "") String complaintuserdesc,
			@RequestParam(value = "complaintcwb", required = false, defaultValue = "") String complaintcwb,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "issure", required = false, defaultValue = "-1") long issure,
			HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("complaintList", complaintDao.getComplaintAll(page, complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid,
				complaintuserdesc, complaintcwb, beginemaildate, endemaildate, issure));
		model.addAttribute(
				"page_obj",
				new Page(complaintDao.getComplaintCount(page, complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid, complaintuserdesc,
						complaintcwb, beginemaildate, endemaildate, issure), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		setSesstion(complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid, complaintuserdesc, complaintcwb, beginemaildate,
				endemaildate, issure, request);
		return "complaint/list";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		String userName = "";
		try {
			userName = getDmpDAO.getLogUser(dmpid).getRealname();
		} catch (Exception e) {
		}

		Complaint complaint = complaintService.loadFormForComplaint(request, userName);
		complaintDao.saveComplaint(complaint);
		/*
		 * model.addAttribute("complaintList",complaintDao.getComplaintAll(1,-1,
		 * -1,-1,"", "","", "","","","","")); model.addAttribute("page_obj", new
		 * Page(complaintDao.getComplaintCount(1,-1, -1,-1,"", "","",
		 * "","","","",""), 1, Page.ONE_PAGE_NUMBER));
		 * model.addAttribute("page", 1); return "complaint/list";
		 */
		return "{\"errorCode\":0,\"error\":\"操作成功!\"}";
	}

	@RequestMapping("/add")
	public String add() {
		return "/complaint/add";
	}

	@RequestMapping("/del")
	public String edit(@RequestParam("id") long id, Model model, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("complaintList", new ArrayList<Complaint>());
		model.addAttribute("page_obj", new Page(1, 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		long msg = complaintDao.updateDelFlag(id);
		model.addAttribute("msg", msg);
		return "complaint/list";
	}

	@RequestMapping("/updatetype")
	public String save(Model model, HttpServletRequest request, @RequestParam("id") long id) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("complaintList", new ArrayList<Complaint>());
		model.addAttribute("page_obj", new Page(1, 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		long msg = complaintDao.updateCheckflag(id);
		model.addAttribute("msg", msg);
		return "complaint/list";
	}

	@RequestMapping("/deal")
	public String deal(Model model, HttpServletRequest request, @RequestParam("id") long id, @RequestParam(value = "p", required = false, defaultValue = "") String p) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("complaintList", new ArrayList<Complaint>());
		model.addAttribute("page_obj", new Page(1, 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		long msg = complaintDao.updateFlag(id, p);
		model.addAttribute("msg", msg);
		return "complaint/list";
	}

	@RequestMapping("/issure")
	public String issure(Model model, HttpServletRequest request, @RequestParam("id") long id) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("complaintList", new ArrayList<Complaint>());
		model.addAttribute("page_obj", new Page(1, 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		long msg = complaintDao.isSure(id);
		model.addAttribute("msg", msg);
		return "complaint/list";
	}

	@RequestMapping("/view/{id}")
	public String view(Model model, @PathVariable("id") long id) {
		model.addAttribute("Complaint", complaintDao.getComplaintById(id));
		return "complaint/viewByContactman";
	}

	@RequestMapping("/exportExcle")
	public String exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[14]; // 导出的列名
		String[] cloumnName2 = new String[14]; // 导出的英文列名
		// 新字段数组赋值
		exportService.SetCompitFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "投诉信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = "Complaint_" + df.format(new Date()) + ".xls"; // 文件名
		try {
			// 查询出数据
			long complainttypeid = Long.parseLong(request.getSession().getAttribute("complainttypeid").toString());
			long checkflag = Long.parseLong(request.getSession().getAttribute("checkflag").toString());
			long complaintflag = Long.parseLong(request.getSession().getAttribute("complaintflag").toString());
			String complaintcustomerid = request.getSession().getAttribute("complaintcustomerid").toString();
			String complaintuserid = request.getSession().getAttribute("complaintuserid").toString();
			String complaintcontactman = request.getSession().getAttribute("complaintcontactman").toString();
			String complaintphone = request.getSession().getAttribute("complaintphone").toString();
			String complaintuserdesc = request.getSession().getAttribute("complaintuserdesc").toString();
			String complaintcwb = request.getSession().getAttribute("complaintcwb").toString();
			String beginemaildate = request.getSession().getAttribute("beginemaildate").toString();
			String endemaildate = request.getSession().getAttribute("endemaildate").toString();
			long issure = Long.parseLong(request.getSession().getAttribute("issure").toString());
			final List<Complaint> list = complaintDao.getComplaintAll(-1, complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid,
					complaintuserdesc, complaintcwb, beginemaildate, endemaildate, issure);
			// final List<ExportDTO> list =
			// exportDAO.getExportHmj(cwbandtranscwb, datetype, begindate,
			// enddate, customerid, commonnumber, flowordertype, orderName);
			ExcelUtilsOld excelUtil = new ExcelUtilsOld() { // 生成工具类实例，并实现填充数据的抽象方法
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
							a = exportService.setObjectB(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/advancedquery/exportView";
	}

	private void setSesstion(long complainttypeid, long checkflag, long complaintflag, String complaintcustomerid, String complaintcontactman, String complaintphone, String complaintuserid,
			String complaintuserdesc, String complaintcwb, String beginemaildate, String endemaildate, long issure, HttpServletRequest request) {
		request.getSession().setAttribute("complainttypeid", complainttypeid);
		request.getSession().setAttribute("checkflag", checkflag);
		request.getSession().setAttribute("complaintflag", complaintflag);
		request.getSession().setAttribute("complaintcustomerid", complaintcustomerid);
		request.getSession().setAttribute("complaintcontactman", complaintcontactman);
		request.getSession().setAttribute("complaintphone", complaintphone);
		request.getSession().setAttribute("complaintuserid", complaintuserid);
		request.getSession().setAttribute("complaintuserdesc", complaintuserdesc);
		request.getSession().setAttribute("complaintcwb", complaintcwb);
		request.getSession().setAttribute("beginemaildate", beginemaildate);
		request.getSession().setAttribute("endemaildate", endemaildate);
		request.getSession().setAttribute("issure", issure);

	}
}
