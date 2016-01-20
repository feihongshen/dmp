package cn.explink.domain;

import java.math.BigDecimal;

public class FnDfAdjustmentRecord {
	private long adj_id;//调整记录id
	private String order_no;//订单号
	private String bill_no;//账单编号
	private String adjust_bill_no;//调整账单编号
	private long org_id;//订单配送站点
	private long customer_id;//客户ID
	private BigDecimal adjust_amount;//调整差额
	private String remark;//备注
	private String creator;//创建人
	private String create_time;//创建时间
	private int adj_state;//核销标识
	private int orderType; //订单类型
	
	public long getAdj_id() {
		return adj_id;
	}
	public void setAdj_id(long adj_id) {
		this.adj_id = adj_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getAdjust_bill_no() {
		return adjust_bill_no;
	}
	public void setAdjust_bill_no(String adjust_bill_no) {
		this.adjust_bill_no = adjust_bill_no;
	}
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}
	public BigDecimal getAdjust_amount() {
		return adjust_amount;
	}
	public void setAdjust_amount(BigDecimal adjust_amount) {
		this.adjust_amount = adjust_amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getAdj_state() {
		return adj_state;
	}
	public void setAdj_state(int adj_state) {
		this.adj_state = adj_state;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
