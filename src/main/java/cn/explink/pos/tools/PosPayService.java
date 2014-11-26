package cn.explink.pos.tools;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;

@Service
public class PosPayService {
	private Logger logger = LoggerFactory.getLogger(PosPayService.class);
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;

	/**
	 * 查询派送反馈表是否为空，如果为空则为跳节点 需补充
	 * 
	 * @param cwbOrder
	 * @return
	 */
	public double getReceivedAmountByCwb(String cwb, BigDecimal businessfee, long deliverid) {
		if (cwb == null || "".equals(cwb)) {
			return 0;
		}
		DeliveryState deliverstate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliverstate != null) {
			return deliverstate.getReceivedfee().doubleValue();
		} else {
			logger.info("POS支付跳流程，需自动创建数据到deliver_state表，订单号：{}" + cwb);
			// cwbOrderService.receiveGoods(userDAO.getUserByUserid(deliverid),userDAO.getUserByUserid(deliverid),
			// cwb);
			return 0;
		}
	}

	public String getCustomerNameByCustomerId(long customerid) {
		Customer customer = customerDAO.getCustomerById(customerid);
		return customer.getCustomername();
	}

	public String getBranchNameById(long branchid) {
		Branch branch = branchDAO.getBranchByBranchid(branchid);
		return branch.getBranchname();
	}
}
