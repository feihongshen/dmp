package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbOXODetailDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOXODetailBean;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@RequestMapping("/cwboxodetail")
@Controller
public class CwbOXODetailController {

	@Autowired
	CwbOXODetailDAO cwbOXODetailDAO;
	@Autowired
	CustomerDAO customerDao;
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	
	@RequestMapping("/list/{page}")
	public String list(
			@PathVariable("page") long page,
			Model model,
			@RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "executeType", required = false, defaultValue = "") String executeType,
			@RequestParam(value = "executeStatus", required = false, defaultValue = "") String executeStatus,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String customerid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String cwbordertypeid) {
		
		if ("".equals(executeStatus)) {
			executeStatus = CwbOXOStateEnum.UnProcessed.getValue()+""; //默认未处理
		}
		long currentBranchId = getCurrentBranchId(); //只查询登录站点的OXO、OXOJIT
		List<Customer> cumstrListAll = this.customerDao.getAllCustomers();
		List<String> customeridList = this.getList(customerid);
		List<CwbOXODetailBean> cwbOXODetailList = this.cwbOXODetailDAO
				.getCwbOXODetailByPage(page, currentBranchId, startDate, endDate, executeType, executeStatus, customerid, cwbordertypeid);
		long cwbOXODetailCount = this.cwbOXODetailDAO.getCwbOXODetailCount(currentBranchId, startDate, endDate, executeType, executeStatus, customerid, cwbordertypeid);
		model.addAttribute("cwboxodetailList", cwbOXODetailList);
		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("page_obj", new Page(cwbOXODetailCount, page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "cwboxodetail/list";
	}
	
	@RequestMapping("/oxobill/{page}")
	public String printOXOBill(Model model,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		if (cwb != null && !cwb.isEmpty()) {
			model.addAttribute("cwbOrder", this.cwbDao.getCwbByCwb(cwb));
			model.addAttribute("query", "1");
		}
		return "warehousegroup/oxobill";
	}
	
	@RequestMapping("/oxobillprint")
	public String printOXOBillPrint(Model model,
			@RequestParam(value = "printcwb", required = false, defaultValue = "") String printcwb) {
		
		List<Branch> branchList = branchDAO.getAllEffectBranches();
		model.addAttribute("branchList", branchList);
		if (printcwb != null && !printcwb.isEmpty()) {
			model.addAttribute("cwbOrder", this.cwbDao.getCwbByCwb(printcwb));
		}
		return "warehousegroup/oxobillprint";
	}
	
	
	private long getCurrentBranchId() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser().getBranchid();
	}
	
	private List<String> getList(String customer) {
		List<String> strList = new ArrayList<String>();
		if (customer != null && customer.length() > 0) {
			String[] strArr = customer.split(",");
			if (strArr != null && strArr.length > 0) {
				for (String str : strArr) {
					strList.add(str);
				}
			}
		}
		return strList;
	}
}
