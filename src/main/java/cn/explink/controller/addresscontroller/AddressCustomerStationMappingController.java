package cn.explink.controller.addresscontroller;

import java.util.ArrayList;
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
	public String list(Model model, @PathVariable("page") long page, String customerid, String station) {

		List<AddressCustomerStationVO> list = this.addressCustomerStationService.getCustomerStationsArea(page, customerid, station);
		model.addAttribute("customerid", customerid);
		model.addAttribute("station", station);
		model.addAttribute("listRalations", list);
		model.addAttribute("listCustomers", this.customerService.getPageCash());
		model.addAttribute("listBranchs", this.getStations());
		model.addAttribute("page_obj", new Page(this.addressCustomerStationService.getAllCount(), page, Page.ONE_PAGE_NUMBER));
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

	// 将站点对应的区域查询查出来
	@RequestMapping("/getAreaByBranchid")
	public @ResponseBody String getAreaByBranchId(Long branchid) {
		// return
		// this.addressCustomerStationService.getAreaByBranchId(branchid);
		return "{\"errorCode\":0,\"error\":\"" + this.addressCustomerStationService.getAreaByBranchId(branchid) + "\"}";
	}

	// 确认添加客户站点对应关系操作
	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, @RequestParam("stationName") String stationName, @RequestParam("customerName") String customerName, @RequestParam("checkFlag") String checkFlag) throws Exception {
		User user = this.getSessionUser();
		if(checkFlag.endsWith("0")){
			//校验是否添加相同数据
			int checkF = this.addressCustomerStationService.checkSame(customerName,stationName);
			if(checkF==1){
				return "{\"errorCode\":1,\"error\":\"相同的客户、区域已存在该站点，系统将平均分配执行站点，是否继续创建？\"}";
			}else if(checkF==2){
				return "{\"errorCode\":2,\"error\":\"相同客户存在相同站点，创建失败\"}";
			}
		}
		this.addressCustomerStationService.create(customerName, stationName, user);
		return "{\"errorCode\":0,\"error\":\"创建成功\"}";
	}

	// 进入编辑页
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("addressCustomerStationVO", this.addressCustomerStationService.getCustomerStationByid(id));
		model.addAttribute("listCustomers", this.customerService.getPageCash());
		model.addAttribute("listBranchs", this.getStations());
		return "/address/edit";
	}

	// 确认修改客户站点对应关系操作
	@RequestMapping("/save")
	public @ResponseBody String save(Model model, @RequestParam("customerName") String customerName, @RequestParam("branchName") String branchName,@RequestParam("checkFlag") String checkFlag, @RequestParam("customerId") String customerId) throws Exception {
		User user = this.getSessionUser();
		if(checkFlag.endsWith("0")){
			//校验是否添加相同数据
			int checkF = this.addressCustomerStationService.checkSame(customerId,branchName);
			if(checkF==1){
				return "{\"errorCode\":1,\"error\":\"相同的客户、区域已存在该站点，系统将平均分配执行站点，是否继续修改？\"}";
			}else if(checkF==2){
				return "{\"errorCode\":0,\"error\":\"相同客户存在相同站点，保存失败\"}";
			}
		}
		this.addressCustomerStationService.updateById(Long.parseLong(customerName), branchName, user);
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long id) throws Exception {
		this.addressCustomerStationService.delById(id);
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	// 获取所有站点机构
	public List<Branch> getStations() {
		List<Branch> list = this.branchService.getPageCash();
		List<Branch> listStation = new ArrayList<Branch>();
		for (Branch branch : list) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()&&"1".equals(branch.getBrancheffectflag())) {
				listStation.add(branch);
			}
		}
		return listStation;
	}

}
