package cn.explink.controller.addresscontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressCustomerStationVO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.BranchService;
import cn.explink.service.CustomerService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.addressmatch.AddressCustomerStationService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/addressCustomerStationMap")
public class AddressCustomerStationMappingController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AddressCustomerStationService addressCustomerStationService;
	@Autowired
	CustomerService customerService;
	@Autowired
	BranchService branchService;

	// 获取当前用户
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// 进入客户站点对应关系维护界面
	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page) {
		// List<AddressCustomerStationVO> list =
		// this.addressCustomerStationService.getAllCountByCustomerId();
		HashMap<String, List<AddressCustomerStationVO>> map = this.addressCustomerStationService.getCustomerStations();
		// model.addAttribute("listRalations", list);
		model.addAttribute("mapRalation", map);
		model.addAttribute("page_obj", new Page(this.addressCustomerStationService.getAllCountByCustomerId(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/address/list";
	}

	// 进入添加关系页
	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("listCustomers", this.customerService.getPageCash());
		model.addAttribute("listBranchs", this.getStations());
		return "/address/add";
	}

	// 确认添加客户站点对应关系操作
	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, @RequestParam("stationName") String stationName, @RequestParam("customerName") String customerName) throws Exception {
		User user = this.getSessionUser();
		this.addressCustomerStationService.create(customerName, stationName, user);
		return "{\"errorCode\":0,\"error\":\"创建成功\"}";
	}

	// 进入编辑页
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long customerid) {
		// model.addAttribute("addressCustomerStationVO",
		// this.addressCustomerStationService.getCustomerStationByid(customerid));
		model.addAttribute("listCustomer", this.addressCustomerStationService.getCustomerStationByCustomerid(customerid));
		model.addAttribute("listBranchs", this.getStations());
		return "/address/edit";
	}

	// 确认修改客户站点对应关系操作
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") long id, @RequestParam("customerName") String customerName, @RequestParam("branchName") String branchName) throws Exception {
		// SystemInstall cs = systemInstallDAO.getSystemInstallById(id);
		// if (cs == null) {
		// return "{\"errorCode\":1,\"error\":\"该设置不存在\"}";
		// } else {
		// systemInstallService.saveSystemInstall(chinesename, name, value, id);
		// if ("siteDayLogTime".equals(name) ||
		// "wareHouseDayLogTime".equals(name) ||
		// "tuiHuoDayLogTime".equals(name)) {
		// setQuartzTime(value, name);
		// }
		// logger.info("operatorUser={},系统 设置->save",
		// getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		// }
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long id) throws Exception {
		this.addressCustomerStationService.delById(id);
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	public List<Branch> getStations() {
		List<Branch> list = this.branchService.getPageCash();
		List<Branch> listStation = new ArrayList<Branch>();
		for (Branch branch : list) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				listStation.add(branch);
			}
		}
		return list;
	}

}
