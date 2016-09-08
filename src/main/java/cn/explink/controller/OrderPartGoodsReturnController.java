package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.OrderPartGoodsRt;
import cn.explink.domain.User;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.OrderPartGoodsReturnService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/orderpartgoodsreturn")
public class OrderPartGoodsReturnController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderPartGoodsReturnService orderPartGoodsReturnService;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	JointService jointService;
	@Autowired
	VipShopGetCwbDataService vipShopGetCwbDataService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	private Logger logger = LoggerFactory.getLogger(OrderPartGoodsReturnController.class);

	/**
	 * 获取登陆用户
	 * 
	 * @return
	 */
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 根据用户及供货商，获取上门退订单
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/ordergoodslist/{page}")
	public String showOrderByReturnDetail(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", defaultValue = "-1", required = false) long userid,
			@RequestParam(value = "customerid", defaultValue = "-1", required = false) long customerid) {

		// long count;
		List<User> userList = new ArrayList<User>();
		if (this.getSessionUser().getRoleid() == 2) {
			userList.add(this.getSessionUser());
		} else {
			userList = this.userDAO.getUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());
		}
		long userBranchId = this.getSessionUser().getBranchid();

		List<Customer> customerList = this.getCustomerList();
		List<OrderPartGoodsRt> orderPartGoodsRtList = this.orderPartGoodsReturnService.getOrderPartGoodsRtList(page, userid, customerid, userList, customerList, userBranchId);
		String customer = this.systemInstallService.getParameter("pxwl");
		// count =
		// orderPartGoodsReturnService.getOrderPartGoodsRtCount(page,userid,customerid,userList,customerList);
		if ((orderPartGoodsRtList != null) && (orderPartGoodsRtList.size() > 0)) {
			for (int i = 0; i < orderPartGoodsRtList.size(); i++) {
				orderPartGoodsRtList.get(i).setOrdergoodsList(this.orderGoodsDAO.getOrderGoodsList(orderPartGoodsRtList.get(i).getCwb()));
				orderPartGoodsRtList.get(i).setCustomer(customer);
			}
		}
		model.addAttribute("weituiyuanyinList", this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WeiTuiShangPin.getValue()));
		model.addAttribute("userList", userList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("userid", userid);
		model.addAttribute("customerid", customerid);
		model.addAttribute("orderPartGoodsRtList", orderPartGoodsRtList);
		model.addAttribute("nowtime", DateTimeUtil.getNowTime());
		// model.addAttribute("page_obj", new Page(count, page,
		// Page.ONE_PAGE_NUMBER));
		return "orderpartgoodsreturn/goodsReturnList";
	}

	/**
	 * 根据订单号，获取上门退订单
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/ordergoodsbycwbs")
	public String showOrderBycwb(Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";
		StringBuffer cwbs = new StringBuffer();
		String cwbss = "";
		if (cwb.length() > 0) {
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
			}

		} else {
			return "orderpartgoodsreturn/goodsReturnListBycwbs";
		}
		if (cwbs.length() > 0) {
			cwbss = cwbs.substring(0, cwbs.length() - 1);
		} else {
			return "orderpartgoodsreturn/goodsReturnListBycwbs";
		}
		List<User> userList = new ArrayList<User>();
		if (this.getSessionUser().getRoleid() == 2) {
			userList.add(this.getSessionUser());
		} else {
			userList = this.userDAO.getUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());
		}
		List<Customer> customerList = this.getCustomerList();
		List<OrderPartGoodsRt> orderPartGoodsRtList = this.orderPartGoodsReturnService.getOrderPartGoodsRtBycwbs(cwbss, userList, customerList);
		String customer = this.systemInstallService.getParameter("pxwl");
		if ((orderPartGoodsRtList != null) && (orderPartGoodsRtList.size() > 0)) {
			for (int i = 0; i < orderPartGoodsRtList.size(); i++) {
				orderPartGoodsRtList.get(i).setOrdergoodsList(this.orderGoodsDAO.getOrderGoodsList(orderPartGoodsRtList.get(i).getCwb()));
				orderPartGoodsRtList.get(i).setCustomer(customer);
			}
		}
		model.addAttribute("weituiyuanyinList", this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WeiTuiShangPin.getValue()));
		model.addAttribute("customerList", customerList);
		model.addAttribute("orderPartGoodsRtList", orderPartGoodsRtList);
		model.addAttribute("nowtime", DateTimeUtil.getNowTime());
		return "orderpartgoodsreturn/goodsReturnListBycwbs";
	}

	/**
	 * 获取上门退的订单及商品信息，进行退货处理，并对商品表进行回写
	 * 
	 * @param model
	 * @param orderjson
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/ordergoodsreturn")
	public @ResponseBody
	String orderGoodsReturn(Model model, @RequestParam(value = "orderjson", defaultValue = "", required = false) String orderjson) {

		JSONArray json = JSONArray.fromObject(orderjson);
		@SuppressWarnings("rawtypes")
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("ordergoodsList", OrderGoods.class);
		@SuppressWarnings("unchecked")
		List<OrderPartGoodsRt> OrderPartGoodsRtList = JSONArray.toList(json, OrderPartGoodsRt.class, classMap);
		try {
			for (OrderPartGoodsRt ort : OrderPartGoodsRtList) {
				int returngoodscount = 0;
				List<OrderGoods> goodList = ort.getOrdergoodsList();
				for (OrderGoods goods : goodList) {
					returngoodscount += goods.getShituicount();
				}
				this.orderPartGoodsReturnService.partgoodsreturn(ort.getCwb(), ort.getCollectiontime(), returngoodscount);
				this.orderPartGoodsReturnService.updateOrderGoods(goodList);
				//add start 反馈揽收状态/运单对照关系给tps add by zhouhuan 2016-08-30
				CwbOrder co = this.cwbDAO.getCwbByCwbLock(ort.getCwb());
				DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(ort.getCwb());
				cwbOrderService.sendTranscwbRelationToTps(co,co.getTpstranscwb(),co.getTranscwb(),deliveryState.getShouldfare(),2,null,co.getPaybackfee(),false);
			}
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"订单部分退失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"订单部分退成功\"}";
	}

	/**
	 * 获取VIPSHOP的对接客户
	 * 
	 * @return
	 */
	public List<Customer> getCustomerList() {
		List<Customer> customerList = new ArrayList<Customer>();
		StringBuffer customerids = new StringBuffer();
		try {
			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("vipshop")) {
					int isOpenFlag = this.jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 0) {
						this.logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
						continue;
					}
					VipShop vipshop = this.vipShopGetCwbDataService.getVipShop(enums.getKey());
					if(!StringUtils.isEmpty(vipshop.getCustomerids())){
						customerids.append(vipshop.getCustomerids()).append(",");
					}
				}
			}
			if (customerids.length() > 0) {
				customerList = this.customerDAO.getCustomerByIds(customerids.toString().substring(0, customerids.toString().lastIndexOf(",")));
			}
		} catch (Exception e) {// 1111
			this.logger.error("vipshop获取客户失败", e);
			return null;
		}
		return customerList;
	}
}
