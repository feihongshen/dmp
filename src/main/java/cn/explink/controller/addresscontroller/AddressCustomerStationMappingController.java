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
	private SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private AddressCustomerStationService addressCustomerStationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BranchService branchService;
	//1代表没有停用的站点 0代表已经停用的站点
	private final String  BRANCHEFFECTFLAG="1";
	// 获取当前用户
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// 进入客户站点对应关系维护界面
	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page,
			String customerid, String station, String execute_branchid) {

		List<AddressCustomerStationVO> list = this.addressCustomerStationService
				.getCustomerStationsArea(page, customerid, station,
						execute_branchid);
		model.addAttribute("customerid", customerid);
		model.addAttribute("station", station);
		model.addAttribute("listRalations", list);
		model.addAttribute("execute_branchid", execute_branchid);
		model.addAttribute("listCustomers", this.customerService.getPageCash());
		model.addAttribute("listBranchs",this.getBranchs());
		model.addAttribute("page_obj", new Page(
				this.addressCustomerStationService.getAllCount(customerid, station,execute_branchid), page,
				Page.ONE_PAGE_NUMBER));
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
	public @ResponseBody String create(HttpServletRequest request,
			@RequestParam("stations") String stationName,
			@RequestParam("customerName") String customerName,
			@RequestParam("executes") String excute_branchid,
			@RequestParam("checkFlag") String checkFlag) throws Exception {
		User user = this.getSessionUser();
		if (checkFlag.endsWith("0")) {
			// 校验是否添加相同数据
			int checkF = this.addressCustomerStationService.checkSame(customerName, stationName);
			if (checkF == 1) {
				return "{\"errorCode\":1,\"error\":\"相同的客户、区域已存在该站点，系统将平均分配执行站点，是否继续创建？\"}";
			} else if (checkF == 2) {
				return "{\"errorCode\":2,\"error\":\"相同客户存在相同站点，创建失败\"}";
			}
		}
		this.addressCustomerStationService.create(customerName, stationName, user, excute_branchid);
		return "{\"errorCode\":0,\"error\":\"创建成功\"}";
	}

	// 进入编辑页
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		AddressCustomerStationVO addressCustomerStationVO = this.addressCustomerStationService
				.getCustomerStationByid(id);
		model.addAttribute("addressCustomerStationVO", addressCustomerStationVO);
		model.addAttribute("listCustomers", this.customerService.getPageCash());
		model.addAttribute("listBranchs", this.getStations());
		return "/address/edit";
	}

	// 确认修改客户站点对应关系操作
	@RequestMapping("/save")
	public @ResponseBody String save(Model model,
			@RequestParam("customerName") String customerName,
			@RequestParam("stations") String branchName,
			@RequestParam("checkFlag") String checkFlag,
			@RequestParam("executes") String excute_branchid,
			@RequestParam("id") String id) throws Exception {
		User user = this.getSessionUser();
		if (checkFlag.endsWith("0")) {
			// 校验是否添加相同数据
			int checkF = this.addressCustomerStationService.checkSame(id, branchName);
			if (checkF == 1) {
				return "{\"errorCode\":1,\"error\":\"相同的客户、区域已存在该站点，系统将平均分配执行站点，是否继续修改？\"}";
			} else if (checkF == 2) {
				return "{\"errorCode\":0,\"error\":\"相同客户存在相同站点，保存失败\"}";
			}
		}
		this.addressCustomerStationService.updateById(Long.parseLong(id),branchName,customerName,  user, excute_branchid);
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long id)
			throws Exception {
		this.addressCustomerStationService.delById(id);
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	// 获取没有停用的站点机构的集合
	public List<Branch> getStations() {
		List<Branch> list = this.branchService.getPageCash();
		List<Branch> listStation = new ArrayList<Branch>();
		for (Branch branch : list) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()
					&& this.BRANCHEFFECTFLAG.equals(branch.getBrancheffectflag())) {
				listStation.add(branch);
			}
		}
		return listStation;
	}
	//获取所有的站点
	public List<Branch> getBranchs() {
		List<Branch> list = this.branchService.getBranchs();
		List<Branch> listStation = new ArrayList<Branch>();
		for (Branch branch : list) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				listStation.add(branch);
			}
		}
		return listStation;
	}


}
