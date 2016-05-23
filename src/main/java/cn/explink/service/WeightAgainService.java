package cn.explink.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;

@Service
public class WeightAgainService {
	
	@Autowired
	private CwbDAO cwbDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private TransCwbDetailDAO transCwbDetailDao ;
	
	/**
	 * 根据订单编号，获取订单基本信息
	 * @param orderNumber
	 * @return
	 */
	public CwbOrder findCwbOrder(String orderNumber){
		return this.cwbDAO.getCwbByCwb(orderNumber) ;
	}
	
	/**
	 * 获取客户基本信息
	 * @param customerid
	 * @return
	 */
	public Customer findCustomer(long customerid){
		return this.customerDAO.getCustomerById(customerid) ;
	}
	
	/**
	 * 更新订单重量
	 * @param orderNumber
	 * @param carrealweight
	 */
	public void updateOrderWeight(String orderNumber,BigDecimal carrealweight){
		this.cwbDAO.saveCwbWeight(carrealweight, orderNumber);
	}
	
	/**
	 * 更新运单重量
	 * @param cwbOrderNumber
	 * @param deliveryNumber
	 * @param carrealweight
	 */
	public void updateDeliveryNumberWeight(String cwbOrderNumber ,String deliveryNumber,BigDecimal carrealweight){
		this.transCwbDetailDao.updateTransCwbDetailWeight(cwbOrderNumber, deliveryNumber, carrealweight) ;
	}

}
