package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.service.CommonService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.WhareHouseToCommonService;
import cn.explink.util.Page;

@RequestMapping("/wharehourecommon")
@Controller
public class WhareHouseToCommonController {

	private Logger logger = LoggerFactory.getLogger(WhareHouseToCommonController.class);
	
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CommonService commonService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	WhareHouseToCommonService whareHouseToCommonService;
	@Autowired
	DataStatisticsService dataStatisticsService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/showlist")
	public String showList(Model model, @RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber,
			@RequestParam(value = "ok", required = false, defaultValue = "") String ok) {
		int outbranchflag = 0; // 库房出库类型

		List<WarehouseToCommen> wcommenList = warehouseToCommenDAO.getCommenCountGroupByCommencode(commonnumber, outbranchflag);
		model.addAttribute("wcommenList", wcommenList);
		List<Common> commonlist = commonDAO.getAllCommons();
		model.addAttribute("commList", commonlist);
		Map<String, String> commenMap = new HashMap<String, String>();
		if (commonlist != null && commonlist.size() > 0) {
			for (Common common : commonlist) {
				commenMap.put(common.getCommonnumber(), common.getCommonname());
			}
		}
		if (ok != null && ok.length() > 0) {
			model.addAttribute("ok", ok);
		}
		model.addAttribute("commenMap", commenMap);
		return "/common/showlist";
	}

	@RequestMapping("/show/{commencode}/{page}")
	public String show(Model model, @PathVariable("commencode") String commencode, @PathVariable("page") long page) {
		int outbranchflag = 0; // 库房出库类型
		List<WarehouseToCommen> wcommenList = warehouseToCommenDAO.getCommenCwbListByCommencode(commencode, page, outbranchflag);
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = branchDAO.getAllBranches();
		model.addAttribute("page_obj", new Page(warehouseToCommenDAO.getCommenCwbListByCommencodeCount(commencode, outbranchflag), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("commencode", commencode);
		String cwbs = "";
		for (WarehouseToCommen warehouseToCommen : wcommenList) {
			cwbs += "'" + warehouseToCommen.getCwb() + "',";
		}
		cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
		List<CwbOrder> cList = cwbDAO.getCwbByCwbs(cwbs);

		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchList) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		Common common = commonDAO.getCommonByCommonnumber(commencode);
		if (common != null) {
			model.addAttribute("commonname", common.getCommonname());
		} else {
			model.addAttribute("commonname", "");
		}
		// 赋值显示对象
		cwbOrderView = whareHouseToCommonService.getCwbOrderView(cList, wcommenList, branchList);
		model.addAttribute("cwborderList", cwbOrderView);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "/common/showDetail";
	}

	// 确认出库
	@RequestMapping("/audit")
	public String audit(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "0") String controlStr,
			@RequestParam(value = "isbranchidflag", required = false, defaultValue = "0") long isbranchidflag) {
		String ok = "";
		long startbranchid = 0;
		int outbranchflag = 0;

		try {
			String commencodes = "";
			JSONArray jsonList = JSONArray.fromObject(controlStr);
			if (jsonList != null && jsonList.size() > 0) {
				for (int i = 0; i < jsonList.size(); i++) {
					commencodes += "'" + jsonList.getJSONObject(i).getString("common") + "',";
				}
				commencodes = commencodes.length() > 0 ? commencodes.substring(0, commencodes.length() - 1) : "";
			}
			ok = whareHouseToCommonService.auditCommen(commencodes, startbranchid, outbranchflag, 0);
		} catch (Exception e) {
			logger.error("", e);
			ok = "系统内部错误，请检查一下omsPathUrl";
		}

		return showList(model, "", ok);

	}

	// 确认出库 按站点
	@RequestMapping("/audit_branch")
	public String audit_branch(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "0") String controlStr,
			@RequestParam(value = "isbranchidflag", required = false, defaultValue = "0") long isbranchidflag) {
		String ok = "";
		long startbranchid = 0;
		if (isbranchidflag > 0) {
			startbranchid = getSessionUser().getBranchid();
		}
		int outbranchflag = 1;
		try {
			String commencodes = "";
			JSONArray jsonList = JSONArray.fromObject(controlStr);
			if (jsonList != null && jsonList.size() > 0) {
				for (int i = 0; i < jsonList.size(); i++) {
					commencodes += "'" + jsonList.getJSONObject(i).getString("common") + "',";
				}
				commencodes = commencodes.length() > 0 ? commencodes.substring(0, commencodes.length() - 1) : "";
			}
			ok = whareHouseToCommonService.auditCommen(commencodes, startbranchid, outbranchflag, 0);
		} catch (Exception e) {
			logger.error("", e);
			ok = "系统内部错误，请检查一下omsPathUrl";
		}

		return showlist_branch(model, "", ok);

	}

	// 导出excel
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response, @RequestParam(value = "commencode", required = false, defaultValue = "0") String commencode,
			@RequestParam(value = "exportmould2", required = false, defaultValue = "") String exportmould,
			@RequestParam(value = "isbranchidflag", required = false, defaultValue = "0") long isbranchidflag,
			@RequestParam(value = "outbranchflag", required = false, defaultValue = "0") int outbranchflag) {
		long startbranchid = 0;
		if (isbranchidflag > 0) {
			startbranchid = getSessionUser().getBranchid();
		}

		dataStatisticsService.exportExcelOutToComm(response, exportmould, commencode, startbranchid, outbranchflag);

	}

	@RequestMapping("/showlist_branch")
	public String showlist_branch(Model model, @RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber,
			@RequestParam(value = "ok", required = false, defaultValue = "") String ok) {

		int outbranchflag = 1; // 站点出库类型

		long branchid = getSessionUser().getBranchid();
		List<WarehouseToCommen> wcommenList = warehouseToCommenDAO.getCommenCountGroupByCommencode_branch(commonnumber, branchid, outbranchflag);
		model.addAttribute("wcommenList", wcommenList);
		List<Common> commonlist = commonDAO.getAllCommons();
		model.addAttribute("commList", commonlist);
		Map<String, String> commenMap = new HashMap<String, String>();
		if (commonlist != null && commonlist.size() > 0) {
			for (Common common : commonlist) {
				commenMap.put(common.getCommonnumber(), common.getCommonname());
				commenMap.put("startbranch", common.getCommonname());
			}
		}
		if (ok != null && ok.length() > 0) {
			model.addAttribute("ok", ok);
		}
		model.addAttribute("commenMap", commenMap);
		return "/common/showlist_branch";
	}

	@RequestMapping("/show_branch/{commencode}/{page}")
	public String show_branch(Model model, @PathVariable("commencode") String commencode, @PathVariable("page") long page) {
		long branchid = getSessionUser().getBranchid();
		int outbranchflag = 1; // 库房出库类型
		List<WarehouseToCommen> wcommenList = warehouseToCommenDAO.getCommenCwbListByCommencode_branch(commencode, page, branchid, outbranchflag);
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = branchDAO.getAllBranches();
		model.addAttribute("page_obj", new Page(warehouseToCommenDAO.getCommenCwbListByCommencodeCount_branch(commencode, branchid, outbranchflag), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("commencode", commencode);
		String cwbs = "";
		for (WarehouseToCommen warehouseToCommen : wcommenList) {
			cwbs += "'" + warehouseToCommen.getCwb() + "',";
		}
		cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
		List<CwbOrder> cList = cwbDAO.getCwbByCwbs(cwbs);

		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchList) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		Common common = commonDAO.getCommonByCommonnumber(commencode);
		if (common != null) {
			model.addAttribute("commonname", common.getCommonname());
		} else {
			model.addAttribute("commonname", "");
		}
		// 赋值显示对象
		cwbOrderView = whareHouseToCommonService.getCwbOrderView(cList, wcommenList, branchList);
		model.addAttribute("cwborderList", cwbOrderView);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		model.addAttribute("isbranchidflag", "1");
		return "/common/showDetail_branch";
	}

}
