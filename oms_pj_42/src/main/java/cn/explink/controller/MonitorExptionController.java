package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.DeliveryService;
import cn.explink.service.MonitorService;

@Controller
@RequestMapping("/monitorEx")
public class MonitorExptionController {

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
	SecurityContextHolderStrategy securityContextHolderStrategy;

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
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		request.getSession().setAttribute("flowType", flowType);

		showDate(model, branchid, Long.parseLong(flowType), timeToid);

		return "/monitor/delivery/deliveryShow";

	}

	// 详细页导出
	@RequestMapping("/dateShowExport/{page}")
	public String dateShowExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		String flowType = request.getSession().getAttribute("flowType").toString();
		showDate(model, branchid, Long.parseLong(flowType), timeToid);
		return "/monitor/delivery/deliveryShowExpo";

	}

	// 返回第一页面
	@RequestMapping("/back/{page}")
	public String goback(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String controlStrType = request.getSession().getAttribute("controlStrType").toString();
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		int timeToid = Integer.parseInt(request.getSession().getAttribute("timeToid").toString());
		setDeliverylist(model, request, controlStrType, branchid, timeToid);
		return "/monitor/delivery/deliveryMonitor";
	}

	// ======以下都是封装方法=============/////

	private void showDate(Model model, long branchid, long flowType, int timeToid) {
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
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		long branchidPram = branchid == -1 ? branchidSession : branchid;

		String typeStr = controlStrType.equals("") ? "" + FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.TiHuo.getValue() + "," + "" + FlowOrderTypeEnum.RuKu.getValue() + ","
				+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "" : controlStrType
				.substring(0, controlStrType.length() - 1);
		Map typeNameMap = new HashMap();
		typeNameMap.put(FlowOrderTypeEnum.DaoRuShuJu.getValue(), FlowOrderTypeEnum.DaoRuShuJu.getText());
		typeNameMap.put(FlowOrderTypeEnum.TiHuo.getValue(), FlowOrderTypeEnum.TiHuo.getText());
		typeNameMap.put(FlowOrderTypeEnum.RuKu.getValue(), FlowOrderTypeEnum.RuKu.getText());
		typeNameMap.put(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), FlowOrderTypeEnum.ChuKuSaoMiao.getText());
		typeNameMap.put(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText());
		typeNameMap.put(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), FlowOrderTypeEnum.FenZhanLingHuo.getText());

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
		model.addAttribute("branchNow", branchDao.getBranchById(branchidPram));
		model.addAttribute("typeList", typeStr);
		model.addAttribute("typeNameMapPram", typeNameMapPram);
		model.addAttribute("typenameList", typenameList);
		model.addAttribute("deliveryList", deliveryList);

		setSetions(request, controlStrType, branchidPram, timeToid);

	}

}