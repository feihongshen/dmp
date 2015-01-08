package cn.explink.b2c.vipshop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/vipshop")
public class VipShopController {

	@Autowired
	VipShopGetCwbDataService vipshopService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	@Autowired
	VipShopService vipShopService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopObject", this.vipshopService.getVipShop(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/vipshop";

	}

	@RequestMapping("/saveVipShop/{id}")
	public @ResponseBody String tmallSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {

			this.vipshopService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.vipshopService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * vipshop请求接口 2012-10-25
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/test")
	public @ResponseBody String requestByVipShop(HttpServletRequest request, HttpServletResponse response) {

		this.vipShopService.excuteVipshopDownLoadTask();

		return "手动请求下载成功";

	}

}
