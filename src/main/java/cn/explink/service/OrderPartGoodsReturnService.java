package cn.explink.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
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
	public List<OrderPartGoodsRt> getOrderPartGoodsRtList(long page, long userid, long customerid, List<User> userList, List<Customer> customerList, long userBranchId) {

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

		List<OrderPartGoodsRt> orderPartGoodsRtList = this.orderPartGoodsRtDAO.getOrderPartGoodsRtList(page, userid, customerid, userids, customerids, userBranchId);
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
		long count = this.orderPartGoodsRtDAO.getOrderPartGoodsRtCount(userid, customerid, userids, customerids);
		return count;
	}

	/**
	 * 部分退更新ordergoods表
	 * 
	 * @param goodsList
	 */
	public void updateOrderGoods(List<OrderGoods> goodsList) {
		if ((goodsList != null) && (goodsList.size() > 0)) {
			for (OrderGoods orderGoods : goodsList) {
				this.orderGoodsDao.updateOrderGoodsById(orderGoods);
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
		List<OrderPartGoodsRt> orderPartGoodsRtList = this.orderPartGoodsRtDAO.getOrderPartGoodsRtBycwb(cwbs, userids, customerids);
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
			this.logger.info("反馈(批量)-上门退订单,cwb:{}", cwb);
			Map<String, Object> parameters = new HashMap<String, Object>();

			DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
			if (returngoodscount > 0) {
				parameters.put("podresultid", (long) DeliveryStateEnum.ShangMenTuiChengGong.getValue());

				if (deliveryState != null) {
					parameters.put("infactfare", deliveryState.getShouldfare());
				}
			} else {
				parameters.put("podresultid", (long) DeliveryStateEnum.ShangMenJuTui.getValue());
				if (deliveryState != null) {
					parameters.put("infactfare", BigDecimal.ZERO);
				}
			}
			parameters.put("backreasonid", 0l);
			parameters.put("leavedreasonid", 0l);
			parameters.put("podremarkid", 0l);
			parameters.put("posremark", "");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
			parameters.put("sessionuserid", this.getSessionUser().getUserid());
			parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("isbatch", true);
			parameters.put("paywayid", 0l);
			parameters.put("resendtime", "");
			parameters.put("zhiliuremark", "");
			parameters.put("deliverstateremark", "");

			this.deliveryStateDAO.updateShangmenlanshoutime(cwb, shangmenlanshoutime);
			this.cwbOrderService.deliverStatePod(this.getSessionUser(), cwb, cwb, parameters);

		} catch (Exception e) {
			logger.error("", e);
			return "{\"errorCode\":0,\"error\":\"订单部分退失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"订单部分退成功\"}";
	}
	
	/**
	 * 上门退订单拒退或上门退成功时,修改货物实际退货数量等字段
	 * @author leo01.liao
	 * @param cwb
	 * @param deliveryState
	 */
	public void processOrderGoods(String cwb, long deliveryState){
		try{
			logger.error("归班反馈拒退或上门退成功时修改货物实际退货数量等字段：cwb={},deliveryState={}", cwb, deliveryState);
			
			List<OrderGoods> listOrderGood = orderGoodsDao.getOrderGoodsList(cwb);
			if(listOrderGood == null || listOrderGood.isEmpty()){
				return;
			}
			
			if(deliveryState == DeliveryStateEnum.ShangMenTuiChengGong.getValue()){
				//上门退成功
				for(OrderGoods orderGood: listOrderGood){
					String goodsNum = orderGood.getGoods_num();
					if(goodsNum == null || goodsNum.trim().equals("")){
						continue;
					}
					
					int weiTuiCount = 0;
					int shiTuiCount = Integer.parseInt(goodsNum);
					
					orderGood.setWeituicount(weiTuiCount);
					orderGood.setShituicount(shiTuiCount);
				}
			}else if(deliveryState == DeliveryStateEnum.ShangMenJuTui.getValue()){
				//上门拒退
				for(OrderGoods orderGood: listOrderGood){
					String goodsNum = orderGood.getGoods_num();
					if(goodsNum == null || goodsNum.trim().equals("")){
						continue;
					}
					
					int weiTuiCount = Integer.parseInt(goodsNum);
					int shiTuiCount = 0;
					
					orderGood.setWeituicount(weiTuiCount);
					orderGood.setShituicount(shiTuiCount);
				}
			}
			
			this.updateOrderGoods(listOrderGood);
		}catch(Exception ex){
			logger.error("拒退或上门退成功时修改商品表异常：cwb="+cwb, ex);
		}
	}
}
