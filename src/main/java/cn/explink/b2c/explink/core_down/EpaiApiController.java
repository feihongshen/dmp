package cn.explink.b2c.explink.core_down;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/epaiApi")
public class EpaiApiController {
	private Logger logger = LoggerFactory.getLogger(EpaiApiController.class);
	@Autowired
	EpaiApiService epaiApiService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	EpaiApiService_Download epaiApiService_Download;
	@Autowired
	EpaiApiService_ExportCallBack epaiApiService_ExportCallBack;
	@Autowired
	EpaiInsertCwbDetailTimmer epaiInsertCwbDetailTimmer;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {

		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		return "jointmanage/epaiApi_down/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "") long customerid,
			@RequestParam(value = "userCode", required = false, defaultValue = "") String usercode) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			boolean isExistsFlag = epaiApiDAO.IsExistsPosCodeFlag(usercode, customerid);

			if (isExistsFlag) {
				return "{\"errorCode\":1,\"error\":\"userCode或者供货商已存在！\"}";
			} else {
				EpaiApi pc = epaiApiService.loadingEpaiApiEntity(request, usercode);
				epaiApiDAO.createEpaiApi(pc);
				return "{\"errorCode\":0,\"error\":\"新建成功\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}

	}

	@RequestMapping("/list")
	public String poscodeMapplist(HttpServletRequest request, Model model) {

		model.addAttribute("epailist", epaiApiDAO.getEpaiApiList());
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/epaiApi_down/epaiapi";
	}

	@RequestMapping("/del/{id}/{pwd}")
	public @ResponseBody String del(Model model, @PathVariable("id") long b2cid, @PathVariable("pwd") String pwd) {
		if (pwd != null && pwd.equals("explink")) {
			epaiApiDAO.exptReasonDel(b2cid);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} else {

			return "{\"errorCode\":0,\"error\":\"操作失败，密码错误\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long b2cid, Model model, HttpServletRequest request) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("epaiapi", epaiApiDAO.getEpaiApi(b2cid));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));

		return "jointmanage/epaiApi_down/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long b2cid, Model model, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			String usercode = request.getParameter("userCode");
			String customerid = request.getParameter("customerid");
			boolean isExistsFlag = epaiApiDAO.IsExistsPosCodeFlag(usercode, customerid, b2cid);
			if (!isExistsFlag) {
				EpaiApi pc = epaiApiService.loadingEpaiApiEntity(request, "");
				epaiApiDAO.update(pc, b2cid);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			} else {
				return "{\"errorCode\":0,\"error\":\"重复设置\"}";
			}

		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}

	}

	/**
	 * 手动执行推送测试类
	 * 
	 * @return
	 */
	@RequestMapping("/test")
	public @ResponseBody String epai_test() {
		epaiApiService_Download.downLoadOrders_controllers();
		epaiApiService_ExportCallBack.exportCallBack_controllers();
		epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "系统之间对接-下游电商手动下载数据完成";

	}

}
