package cn.explink.b2c.haoxgou;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/hxg")
public class HaoXiangGouController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	HaoXiangGouService haoXiangGouService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	HaoXiangGouService_Test haoXiangGouService_Test;
	@Autowired
	HaoXiangGouService_PeiSong haoXiangGouService_PeiSong;
	@Autowired
	HaoXiangGouService_TuiHuo haoXiangGouService_TuiHuo;
	@Autowired
	HXGInsertCwbDetailTimmer hXGInsertCwbDetailTimmer;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("hxgObject", haoXiangGouService.getHaoXiangGou(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/hxg";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			haoXiangGouService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		haoXiangGouService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/test")
	public String testpage(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "b2cdj/hxg_test";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/test/1")
	public String testrequest(HttpServletRequest request, HttpServletResponse response, Model model) {

		String request_data = request.getParameter("order_list");
		String requesttype = request.getParameter("action");

		String values = haoXiangGouService_Test.GetOrdWayBillInfoForD2D_Test(request_data, requesttype);
		model.addAttribute("values", values);

		return "b2cdj/hxg_test";

	}

	/**
	 * 订单导入接口 配送单
	 */
	@RequestMapping("/excute/1")
	public @ResponseBody String excute1(HttpServletRequest request, HttpServletResponse response, Model model) {
		haoXiangGouService_PeiSong.GetOrdWayBillInfoForD2D();
		hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "excute download Orders Success!";
	}

	/**
	 * 订单导入接口 上门退
	 */
	@RequestMapping("/excute/2")
	public @ResponseBody String excute2(HttpServletRequest request, HttpServletResponse response, Model model) {
		haoXiangGouService_TuiHuo.GetRtnWayBillInfoForD2D();
		hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "excute download Orders Success!";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response, Model model) {
		hXGInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "excute Insert  Orders Success!";
	}

}
