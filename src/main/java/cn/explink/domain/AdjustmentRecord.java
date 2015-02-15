package cn.explink.domain;

import java.math.BigDecimal;

public class AdjustmentRecord {
	private long id;//调整单id
	private String order_no;//订单号
	private String bill_no;//账单编号
	private long bill_id;//账单ID
	private String adjust_bill_no;//调整账单编号
	private long customer_id;//客户ID
	private BigDecimal receive_fee;//原始应收金额
	private BigDecimal refund_fee;//原始应退金额
	private BigDecimal modify_fee;//更新金额
	private BigDecimal adjust_amount;//调整差额
	private String remark;//备注
	private String creator;//创建人
	private String create_time;//创建时间
	private int status;//核销标识
	private String check_user;//核对人
	private String check_time;//核对时间
	private int order_type;//订单类型
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}
	public BigDecimal getReceive_fee() {
		return receive_fee;
	}
	public void setReceive_fee(BigDecimal receive_fee) {
		this.receive_fee = receive_fee;
	}
	public BigDecimal getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(BigDecimal refund_fee) {
		this.refund_fee = refund_fee;
	}
	public BigDecimal getModify_fee() {
		return modify_fee;
	}
	public void setModify_fee(BigDecimal modify_fee) {
		this.modify_fee = modify_fee;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCheck_user() {
		return check_user;
	}
	public void setCheck_user(String check_user) {
		this.check_user = check_user;
	}
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public String getAdjust_bill_no() {
		return adjust_bill_no;
	}
	public void setAdjust_bill_no(String adjust_bill_no) {
		this.adjust_bill_no = adjust_bill_no;
	}
	public long getBill_id() {
		return bill_id;
	}
	public void setBill_id(long bill_id) {
		this.bill_id = bill_id;
	}
		
}
