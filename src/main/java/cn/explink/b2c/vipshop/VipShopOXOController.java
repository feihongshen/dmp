package cn.explink.b2c.vipshop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.vipshop.oxo.VipShopOXOGetCwbDataService;
import cn.explink.b2c.vipshop.oxo.VipShopOXOInsertCwbDetailTimmer;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.CwbOrderService;

@Controller
@RequestMapping("/vipshopOXO")
public class VipShopOXOController {

	@Autowired
	VipShopOXOGetCwbDataService vipShopOXOGetCwbDataService;
	@Autowired
	BranchDAO branchDAO;
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
	@Autowired
	VipShopOXOInsertCwbDetailTimmer vipShopOXOInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopOXOObject", this.vipShopOXOGetCwbDataService.getVipShop(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/vipshopOXO";

	}

	@RequestMapping("/saveVipShop/{id}")
	public @ResponseBody String oxoSettingSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter("shipper_no"))){
			return "{\"errorCode\":1,\"error\":\"承运商编码必填\"}";
		}
		if(StringUtils.isBlank(request.getParameter("getMaxCount"))){
			return "{\"errorCode\":1,\"error\":\"每次获取订单数量必填\"}";
		}
		if(StringUtils.isBlank(request.getParameter("getCwb_URL"))){
			return "{\"errorCode\":1,\"error\":\"获取订单的URL必填\"}";
		}
		if(StringUtils.isBlank(request.getParameter("sendCwb_URL"))){
			return "{\"errorCode\":1,\"error\":\"反馈URL必填\"}";
		}
		if(StringUtils.isBlank(request.getParameter("customerids"))){
			return "{\"errorCode\":1,\"error\":\"系统中客户id必填\"}";
		}		
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.vipShopOXOGetCwbDataService.edit(request, key);

		return "{\"errorCode\":0,\"error\":\"修改成功\"}";

		

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.vipShopOXOGetCwbDataService.update(key, state);
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
	

	/**
	 * vipshop请求接口 2012-10-25
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {

		vipShopOXOInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.VipShop_OXO.getKey());

		return "定时任务执行成功";

	}

}
