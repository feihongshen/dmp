package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderDeliveryClientDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderDeliveryClient;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.OrderDeliveryClientService;
import cn.explink.util.Page;

/**
 * 领货委托派送
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping("/orderdeliveryclient")
public class OrderDeliveryClientController {
	@Autowired
	UserDAO userDAO;
	@Autowired
	OrderDeliveryClientService orderDeliveryClientService;
	@Autowired
	OrderDeliveryClientDAO orderDeliveryClientDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbDAO cwbDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 领货委托派送(明细)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/clientCwb")
	public String clientCwb(Model model) {
		// 已领货列表
		List<CwbOrderView> yilinghuoList = orderDeliveryClientService.getYiLingHuo(getSessionUser());

		// 已委派列表
		List<OrderDeliveryClient> clientList = orderDeliveryClientDAO.getOrderDeliveryClientList(getSessionUser().getBranchid(), 1, 1);
		StringBuffer sb = new StringBuffer();
		if (clientList != null && !clientList.isEmpty()) {
			for (OrderDeliveryClient dc : clientList) {
				sb.append("'").append(dc.getCwb()).append("',");
			}
		}
		String weipaicwbs = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
		List<CwbOrderView> weipaiList = orderDeliveryClientService.getOrderDeliveryList(clientList, weipaicwbs);

		List<User> uList = userDAO.getDeliveryUserByRolesAndBranchid("2", getSessionUser().getBranchid());
		model.addAttribute("uList", uList);
		model.addAttribute("yilinghuoList", yilinghuoList);
		model.addAttribute("weipaiList", weipaiList);
		return "/orderdeliveryclient/clientCwb";
	}

	/**
	 * 保存领货委托派送(明细)
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param clientid
	 * @return
	 */
	@RequestMapping("/saveCwb/{cwb}")
	public @ResponseBody ExplinkResponse saveCwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "clientid", required = true, defaultValue = "-1") long clientid) {
		JSONObject obj = new JSONObject();
		try {
			CwbOrder cwbOrder = orderDeliveryClientService.saveLingHuoWeiTuo(getSessionUser(), cwb, clientid);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			// 委托派送员
			User user = userDAO.getUserByUserid(clientid);
			obj.put("clientname", user.getRealname());
			// 原小件员
			if (cwbOrder.getDeliverid() != 0) {
				User user1 = userDAO.getUserByUserid(cwbOrder.getDeliverid());
				obj.put("delivername", user1.getRealname());

			}
			ExplinkResponse explinkResponse = new ExplinkResponse("0", "", obj);
			return explinkResponse;
		} catch (CwbException e) {
			obj.put("error", e.getMessage());
			ExplinkResponse explinkResponse = new ExplinkResponse("1", "", obj);
			return explinkResponse;
		}
	}

	/**
	 * 领货委托派送(批量)
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwbs
	 * @param clientid
	 * @return
	 */
	@RequestMapping("/clientCwbs")
	public String clientCwbs(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "clientid", required = true, defaultValue = "0") long clientid) {
		long weipaiSuccessCount = 0;
		long weipaiErrorCount = 0;
		String msg = "";
		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			JSONObject obj = new JSONObject();
			try {// 成功订单
				CwbOrder cwbOrder = orderDeliveryClientService.saveLingHuoWeiTuo(getSessionUser(), cwb, clientid);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				weipaiSuccessCount++;
			} catch (CwbException e) {// 出现验证错误
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					obj.put("errorcode", "000000");
					obj.put("cwbOrder", cwbOrder);
				} else {// 无此订单
					obj.put("errorcode", "111111");
					obj.put("cwb", cwb);
				}
				obj.put("error", e.getMessage());
				objList.add(obj);
				weipaiErrorCount++;
			}
			msg = "成功扫描" + weipaiSuccessCount + "单，异常" + weipaiErrorCount + "单";
		}
		model.addAttribute("objList", objList);
		model.addAttribute("msg", msg);
		// 已委派列表
		// 查询当前站点所有已委派的订单
		List<OrderDeliveryClient> clientList = orderDeliveryClientDAO.getOrderDeliveryClientList(getSessionUser().getBranchid(), 1, 1);
		StringBuffer sb = new StringBuffer("");
		if (clientList != null && !clientList.isEmpty()) {
			for (OrderDeliveryClient dc : clientList) {
				sb.append("'").append(dc.getCwb()).append("',");
			}
		}
		String weipaicwbs = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();

		List<CwbOrderView> weipaiList = orderDeliveryClientService.getOrderDeliveryList(clientList, weipaicwbs);
		/*
		 * if(weipaiList!=null&&!weipaiList.isEmpty()){
		 * model.addAttribute("weipaiNum",weipaiList.size()); }
		 */

		// 已领货列表
		List<CwbOrderView> yilinghuoList = orderDeliveryClientService.getYiLingHuo(getSessionUser());
		/*
		 * if(yilinghuoList!=null&&!yilinghuoList.isEmpty()){
		 * model.addAttribute("yilinghuoNum",yilinghuoList.size()); }
		 */

		List<User> uList = userDAO.getDeliveryUserByRolesAndBranchid("2", getSessionUser().getBranchid());
		model.addAttribute("uList", uList);
		model.addAttribute("yilinghuoList", yilinghuoList);
		model.addAttribute("weipaiList", weipaiList);
		return "/orderdeliveryclient/clientCwbs";
	}

	/**
	 * 委派撤销
	 * 
	 * @param model
	 * @param request
	 * @param page
	 * @param cwbs
	 * @param chexiaocwbs
	 * @param starttime
	 * @param endtime
	 * @param flag
	 *            1：撤销页面 2：撤销List页面
	 * @return
	 */
	@RequestMapping("/delete/{page}")
	public String delete(Model model, HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "chexiaocwbs", required = false, defaultValue = "") String chexiaocwbs, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "flag", required = false, defaultValue = "1") String flag) {
		// 撤销操作
		long chexiaoSuccessCount = 0;
		long chexiaoErrorCount = 0;
		String msg = "";
		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			JSONObject obj = new JSONObject();
			try {// 成功订单
				CwbOrder cwbOrder = orderDeliveryClientService.deleteLingHuoWeiTuo(getSessionUser(), cwb);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				chexiaoSuccessCount++;
			} catch (CwbException e) {// 出现验证错误
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					obj.put("errorcode", "000000");
					obj.put("cwbOrder", cwbOrder);
				} else {// 无此订单
					obj.put("errorcode", "111111");
					obj.put("cwb", cwb);
				}
				obj.put("error", e.getMessage());
				objList.add(obj);
				chexiaoErrorCount++;
			}
			msg = "成功扫描" + chexiaoSuccessCount + "单，异常" + chexiaoErrorCount + "单";
		}
		model.addAttribute("objList", objList);
		model.addAttribute("msg", msg);

		// 委派撤销查询
		// 处理多条订单
		StringBuffer cwbBuffer = new StringBuffer();
		if (!"".equals(chexiaocwbs.trim())) {
			for (String o : chexiaocwbs.split("\r\n")) {
				if (o.trim().length() == 0) {
					continue;
				}
				cwbBuffer.append("'").append(o).append("',");
			}
			chexiaocwbs = cwbBuffer.substring(0, cwbBuffer.lastIndexOf(","));
		}
		if (!"".equals(chexiaocwbs.trim()) || !"".equals(starttime) || !"".equals(endtime)) {
			List<OrderDeliveryClient> clientList = orderDeliveryClientDAO.getOrderDeliveryClientPage(page, getSessionUser().getBranchid(), 0, chexiaocwbs.trim(), starttime, endtime);
			StringBuffer sb = new StringBuffer("");
			if (clientList != null && !clientList.isEmpty()) {
				for (OrderDeliveryClient dc : clientList) {
					sb.append("'").append(dc.getCwb()).append("',");
				}
			}
			String weipaicwbs = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();

			model.addAttribute("weipaiList", orderDeliveryClientService.getOrderDeliveryList(clientList, weipaicwbs));
			model.addAttribute("page_obj", new Page(orderDeliveryClientDAO.getOrderDeliveryClientCount(getSessionUser().getBranchid(), 0, chexiaocwbs.trim(), starttime, endtime), page,
					Page.ONE_PAGE_NUMBER));
		} else {
			model.addAttribute("page_obj", new Page(0, page, Page.ONE_PAGE_NUMBER));
		}

		model.addAttribute("page", page);
		model.addAttribute("flag", flag);
		return "/orderdeliveryclient/delete";
	}

	/**
	 * 导出Excel
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param flag
	 *            1：已领货 2:已委派
	 */
	@RequestMapping("/export")
	public void export(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "flag", defaultValue = "0", required = false) long flag) {
		if (flag == 1) {// 已领货
			orderDeliveryClientService.exportYiLingHuo(request, response, getSessionUser());
		} else {// 已委派
			orderDeliveryClientService.exportYiWeiPai(request, response, getSessionUser());
		}
	}

	@RequestMapping("/getSum")
	public @ResponseBody JSONObject getSum(@RequestParam(value = "clientid", required = false, defaultValue = "0") long clientid) {
		JSONObject obj = new JSONObject();
		// 已委派
		obj.put("weipaiNum", orderDeliveryClientDAO.getCountYiWeiPai(getSessionUser().getBranchid(), 1, clientid));
		// 已领货列表(当前站点 已领货&&未反馈&&未委派 的订单)
		obj.put("yilinghuoNum", deliveryStateDAO.getCountYiLingHuo(getSessionUser().getBranchid(), DeliveryStateEnum.WeiFanKui.getValue(), clientid));
		// deliveryStateDAO.getDeliveryByDeliver(user.getBranchid(),DeliveryStateEnum.WeiFanKui.getValue(),weipaicwbs,1);
		// if(yilinghuoList!=null&&!yilinghuoList.isEmpty()){
		// model.addAttribute("yilinghuoNum",yilinghuoList.size());
		// }
		// List<Map<String, Object>> weichukudata =
		// cwbDAO.getLBChukubyBranchid(branchid,nextbranchid,cwbstate,customerid);
		// obj.put("weichukucount", weichukudata.get(0).get("count"));
		// obj.put("weichukusum", weichukudata.get(0).get("sum"));
		// obj.put("yichukucount",
		// operationTimeDAO.getLBOperationTimeByFlowordertypeAndBranchidAndNextCount(branchid,nextbranchid,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),customerid));

		return obj;
	}
}
