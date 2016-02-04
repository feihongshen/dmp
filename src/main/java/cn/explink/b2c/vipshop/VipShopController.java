package cn.explink.b2c.vipshop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;

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
	@Autowired
	CwbOrderService  cwbOrderService;
	@Autowired
	UserDAO  userDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;

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
	/**
	 * vipshop请求接口 2012-10-25
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadsigngle")
	public @ResponseBody String downloadsigngle(HttpServletRequest request, HttpServletResponse response) {

		this.vipShopService.excuteVipshopDownLoadTaskSigngle();

		return "手动请求下载成功";

	}
	
	
	@RequestMapping("/download")
	public @ResponseBody String download(HttpServletRequest request, HttpServletResponse response) {

		this.vipShopService.excuteGetOrdersByVipshopSigle();

		return "单次手动请求下载成功";

	}
	
	@RequestMapping("/lanjie/{cwb}")
	public @ResponseBody String lanjie(HttpServletRequest request, @PathVariable("cwb") String  cwb) {

		cwbOrderService.tuihuoHandleVipshop(userDAO.getAllUserByid(1), cwb, cwb,0);

		return "执行订单"+cwb+"拦截成功";

	}
	
	@RequestMapping("/shixiao/{cwb}")
	public @ResponseBody String shixiao(HttpServletRequest request, @PathVariable("cwb") String  cwb) {
		
		
		dataImportDAO_B2c.dataLoseB2ctempByCwb(cwb);
		this.cwbDAO.dataLoseByCwb(cwb);
		orderGoodsDAO.loseOrderGoods(cwb);
		cwbOrderService.datalose_vipshop(cwb);

		return "执行订单"+cwb+"失效成功";

	}
	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {

		this.vipshopInsertCwbDetailTimmer.selectTempAndInsertToCwbDetails();

		return "手动执行定时任务成功";

	}

}
