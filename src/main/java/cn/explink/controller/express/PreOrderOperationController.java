package cn.explink.controller.express;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.User;
import cn.explink.domain.VO.express.PreOrderQueryVO;
import cn.explink.domain.express.ExpressPreOrderVOForDeal;
import cn.explink.service.BranchService;
import cn.explink.service.addressmatch.AddressMatchExpressService;
import cn.explink.service.express.PreOrderService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/preOrderOperationController")
public class PreOrderOperationController extends ExpressCommonController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	PreOrderService preOrderService;
	@Autowired
	private BranchService branchService;
	@Autowired
	AddressMatchExpressService addressMatchExpressService;

	/**
	 *
	 * @Title: queryInfo
	 * @description 预订单查询功能的查询方法(省公司)
	 * @author 刘武强
	 * @date  2015年7月30日下午4:37:27
	 * @param  @param preOrderQueryVO ：查询条件
	 * @param  @param model
	 * @param  @param page 页数
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/preOrderquery/{page}")
	public String queryInfo(PreOrderQueryVO preOrderQueryVO, Model model, @PathVariable(value = "page") long page) {
		Map<String, Object> map = this.preOrderService.getPreOrderInfo(preOrderQueryVO, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("excuteStatelist", this.preOrderService.getExcuteState("shenggongsi"));
		//根据branchid获取站点下拉框的内容（公司获得的为全部站点）
		model.addAttribute("stationlist", this.preOrderService.getBranchStations(1, true));
		model.addAttribute("preOrderQueryVO", preOrderQueryVO);
		return "express/companyOperation/preOrderQuery";
	}

	/**
	 *
	 * @Title: queryStationInfo
	 * @description 预订单查询功能的查询方法（站点）
	 * @author 刘武强
	 * @date  2015年8月4日上午8:40:58
	 * @param  @param preOrderQueryVO
	 * @param  @param model
	 * @param  @param page
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/stationPreOrderQuery/{page}")
	public String queryStationInfo(PreOrderQueryVO preOrderQueryVO, Model model, @PathVariable(value = "page") long page) {
		preOrderQueryVO.setStation(this.getSessionUser().getBranchid() + "");
		Map<String, Object> map = this.preOrderService.getPreOrderInfo(preOrderQueryVO, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("excuteStatelist", this.preOrderService.getExcuteState(""));
		model.addAttribute("stationlist", this.preOrderService.getBranchStations(this.getSessionUser().getBranchid(), false));
		model.addAttribute("preOrderQueryVO", preOrderQueryVO);
		return "express/stationOperation/preOrderStationQuery";
	}

	/**
	 * 进入预订单处理页面
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/toPreOrderDeal/{page}")
	public String preOrderDeal(@PathVariable("page") long page, Integer status, String preOrderNo, Model model) {
		List<ExpressPreOrderVOForDeal> preOrderList = this.preOrderService.query(preOrderNo, status, page);
		//		long branchid = this.getSessionUser().getBranchid();

		model.addAttribute("preOrderList", preOrderList);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(this.preOrderService.queryCount(preOrderNo, status), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("stationlist", this.preOrderService.getBranchStations(1, true));
		model.addAttribute("excuteTypelist", this.preOrderService.getExcuteState("shenggongsi"));
		model.addAttribute("status", status);
		return "express/preOrderAudit/PreOrderDeal";
	}

	/**
	 * 关闭预订单
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/closeOrder")
	@ResponseBody
	public int closePreOrder(String ids, String preOrderNo, String reason, Model model) {
		User user = this.getSessionUser();
		return this.preOrderService.updatePreOrederClose(ids, preOrderNo, reason, user);
	}

	/**
	 * 手动匹配站点
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/handMatch")
	@ResponseBody
	public int handMatch(String ids, int siteId, String siteName, String preOrderNo, String reason, Model model) {
		User user = this.getSessionUser();
		return this.preOrderService.updatePreOrderHand(ids, siteId, siteName, preOrderNo, reason, user);
	}

	/**
	 * 退回总部
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/getReturnHeadQuarters")
	@ResponseBody
	public int getReturnHeadQuarters(String ids, String preOrderNo, String reason, Model model) {
		User user = this.getSessionUser();
		return this.preOrderService.updatePreOrederReturn(ids, preOrderNo, reason, user);
	}

	/**
	 * 自动匹配站点
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/autoMatch")
	@ResponseBody
	public String autoMatch(String ids, String preOrderNo, String address, String reason, Model model) {
		User user = this.getSessionUser();
		this.preOrderService.autoMatch(user, preOrderNo, address, reason);
		return null;
	}

}
