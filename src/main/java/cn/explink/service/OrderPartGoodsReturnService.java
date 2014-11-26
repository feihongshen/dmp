package cn.explink.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.controller.DeliveryController;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.OrderPartGoodsRtDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.OrderPartGoodsRt;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class OrderPartGoodsReturnService {

	private Logger logger = LoggerFactory.getLogger(OrderPartGoodsReturnService.class);
	@Autowired
	OrderPartGoodsRtDAO orderPartGoodsRtDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	/**
	 * 获取登陆用户
	 * 
	 * @return
	 */
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 获取上门退待反馈明细列表
	 * 
	 * @param page
	 * @param userid
	 * @param customerid
	 * @param userList
	 * @param customerList
	 * @return
	 */
	public List<OrderPartGoodsRt> getOrderPartGoodsRtList(long page, long userid, long customerid, List<User> userList, List<Customer> customerList) {

		String userids = "";
		String customerids = "";
		if (userid == -1) {
			for (User user : userList) {
				userids += "" + user.getUserid() + ",";
			}
			userids = userids.length() > 0 ? userids.substring(0, userids.length() - 1) : "";
		}
		if (customerid == -1) {
			for (Customer cus : customerList) {
				customerids += "" + cus.getCustomerid() + ",";
			}
			customerids = customerids.length() > 0 ? customerids.substring(0, customerids.length() - 1) : "";
		}
		List<OrderPartGoodsRt> orderPartGoodsRtList = orderPartGoodsRtDAO.getOrderPartGoodsRtList(page, userid, customerid, userids, customerids);
		return orderPartGoodsRtList;
	}

	/**
	 * 获取上门退待反馈订单数量
	 * 
	 * @param page
	 * @param userid
	 * @param customerid
	 * @param userList
	 * @param customerList
	 * @return
	 */
	public long getOrderPartGoodsRtCount(long page, long userid, long customerid, List<User> userList, List<Customer> customerList) {

		String userids = "";
		String customerids = "";
		if (userid == -1) {
			for (User user : userList) {
				userids += "" + user.getUserid() + ",";
			}
			userids = userids.length() > 0 ? userids.substring(0, userids.length() - 1) : "";
		}
		if (customerid == -1) {
			for (Customer cus : customerList) {
				customerids += "" + cus.getCustomerid() + ",";
			}
			customerids = customerids.length() > 0 ? customerids.substring(0, customerids.length() - 1) : "";
		}
		long count = orderPartGoodsRtDAO.getOrderPartGoodsRtCount(userid, customerid, userids, customerids);
		return count;
	}

	/**
	 * 部分退更新ordergoods表
	 * 
	 * @param goodsList
	 */
	public void updateOrderGoods(List<OrderGoods> goodsList) {
		if (goodsList != null && goodsList.size() > 0) {
			for (OrderGoods orderGoods : goodsList) {
				orderGoodsDao.updateOrderGoodsById(orderGoods);
			}
		}
	}

	/**
	 * 获取上门退待反馈明细列表
	 * 
	 * @param page
	 * @param userid
	 * @param customerid
	 * @param userList
	 * @param customerList
	 * @return
	 */
	public List<OrderPartGoodsRt> getOrderPartGoodsRtBycwbs(String cwbs, List<User> userList, List<Customer> customerList) {

		String userids = "";
		String customerids = "";
		for (User user : userList) {
			userids += "" + user.getUserid() + ",";
		}
		userids = userids.length() > 0 ? userids.substring(0, userids.length() - 1) : "";

		for (Customer cus : customerList) {
			customerids += "" + cus.getCustomerid() + ",";
		}
		customerids = customerids.length() > 0 ? customerids.substring(0, customerids.length() - 1) : "";
		List<OrderPartGoodsRt> orderPartGoodsRtList = orderPartGoodsRtDAO.getOrderPartGoodsRtBycwb(cwbs, userids, customerids);
		return orderPartGoodsRtList;
	}

	/**
	 * 调用上门退业务方法
	 * 
	 * @param cwb
	 * @return
	 */
	@Transactional
	public String partgoodsreturn(String cwb, String shangmenlanshoutime, int returngoodscount) {
		try {
			// 成功订单
			logger.info("反馈(批量)-上门退订单,cwb:{}", cwb);
			Map<String, Object> parameters = new HashMap<String, Object>();

			DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
			if (returngoodscount > 0) {
				parameters.put("podresultid", (long) DeliveryStateEnum.ShangMenTuiChengGong.getValue());
			} else {
				parameters.put("podresultid", (long) DeliveryStateEnum.ShangMenJuTui.getValue());
			}
			parameters.put("backreasonid", 0l);
			parameters.put("leavedreasonid", 0l);
			parameters.put("podremarkid", 0l);
			parameters.put("posremark", "");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", getSessionUser().getBranchid());
			parameters.put("sessionuserid", getSessionUser().getUserid());
			parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("isbatch", true);
			parameters.put("paywayid", 0l);
			parameters.put("resendtime", "");
			parameters.put("zhiliuremark", "");
			parameters.put("deliverstateremark", "");
			if (deliveryState != null) {
				parameters.put("infactfare", deliveryState.getShouldfare());
			}
			deliveryStateDAO.updateShangmenlanshoutime(cwb, shangmenlanshoutime);
			cwbOrderService.deliverStatePod(getSessionUser(), cwb, cwb, parameters);

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"errorCode\":0,\"error\":\"订单部分退失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"订单部分退成功\"}";
	}
}
