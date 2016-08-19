package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 库房上门退打印VO
 * @author chunlei05.li
 * @date 2016年8月17日 下午6:21:24
 */
public class KfSmtPrintVo {
	
	/**
	 * 订单主表
	 */
	private CwbOrder cwbOrder;
	
	/**
	 * 客户、供货商
	 */
	private Customer customer;
	
	/**
	 * 联系电话
	 */
	private String consigneemobile;
	
	/**
	 * 费用合计
	 */
	private BigDecimal totalfee;
	
	/**
	 *  合计费用-大写金额
	 */
	private String cnTotalfee;

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public String getCnTotalfee() {
		return cnTotalfee;
	}

	public void setCnTotalfee(String cnTotalfee) {
		this.cnTotalfee = cnTotalfee;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}
}
