package cn.explink.pos.bill99;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;

/**
 * 存储Bill99返回信息的实体
 * 
 * @author Administrator
 *
 */
public class Bill99RespNote {

	private CwbOrder cwbOrder;
	private DeliveryState deliverstate;

	private String resp_code;
	private String resp_msg;
	private String order_no; // 订单号
	private String orderId; // 快钱运单查询的orderId;

	private long deliverid; // 小件员id
	private String delivery_man; // 登录名
	private String trackinfo; // 支付交易备注信息
	private double receivablefee; // 应收金额
	private double receivedfee; // 实收金额
	private double order_payable_amt; // bill99 交易金额
	private String podremark; // pos支付备注
	private int system_pay_type; // 系统中的支付类型，对应paytype枚举
	private int pay_type; // bill99支付类型

	private String terminal_id; // 终端号
	private String trace_no; // 交易凭证号
	private String idTxn; // 系统参考编号

	private String transaction_sn; // 作业流水号
	private String transaction_id; // 请求业务类型编号

	private String signee; // 签收人

	private String consigneename; // 收件人
	private String consignee_contract; // 收件人联系方式
	private String ex_packages; // 异常件数
	private int ex_code; // 异常码
	private String ex_desc; // 异常信息

	private long deliverystate; // 反馈结果 默认0
	private long branchid; // 站点Id

	private double void_amt; // 撤销金额
	private String void_trace_no; // 原凭证号

	// /冲正交易专用
	private double reverse_amt; // 实收金额
	private double price_amt; // 应收金额

	private String reverse_tran_type;// 冲正原交易类型
	private String reverse_trace_no;
	private String payee_id; // 20130607 新增 标识电商，保证快钱刷卡归属到电商账号

	public String getPayee_id() {
		return payee_id;
	}

	public void setPayee_id(String payee_id) {
		this.payee_id = payee_id;
	}

	public String getReverse_trace_no() {
		return reverse_trace_no;
	}

	public void setReverse_trace_no(String reverse_trace_no) {
		this.reverse_trace_no = reverse_trace_no;
	}

	public String getReverse_tran_type() {
		return reverse_tran_type;
	}

	public void setReverse_tran_type(String reverse_tran_type) {
		this.reverse_tran_type = reverse_tran_type;
	}

	public double getReverse_amt() {
		return reverse_amt;
	}

	public void setReverse_amt(double reverse_amt) {
		this.reverse_amt = reverse_amt;
	}

	public double getPrice_amt() {
		return price_amt;
	}

	public void setPrice_amt(double price_amt) {
		this.price_amt = price_amt;
	}

	public String getVoid_trace_no() {
		return void_trace_no;
	}

	public void setVoid_trace_no(String void_trace_no) {
		this.void_trace_no = void_trace_no;
	}

	public double getVoid_amt() {
		return void_amt;
	}

	public void setVoid_amt(double void_amt) {
		this.void_amt = void_amt;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getEx_packages() {
		return ex_packages;
	}

	public void setEx_packages(String ex_packages) {
		this.ex_packages = ex_packages;
	}

	public int getEx_code() {
		return ex_code;
	}

	public void setEx_code(int ex_code) {
		this.ex_code = ex_code;
	}

	public String getEx_desc() {
		return ex_desc;
	}

	public void setEx_desc(String ex_desc) {
		this.ex_desc = ex_desc;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsignee_contract() {
		return consignee_contract;
	}

	public void setConsignee_contract(String consignee_contract) {
		this.consignee_contract = consignee_contract;
	}

	public DeliveryState getDeliverstate() {
		return deliverstate;
	}

	public void setDeliverstate(DeliveryState deliverstate) {
		this.deliverstate = deliverstate;
	}

	public String getSignee() {
		return signee;
	}

	public void setSignee(String signee) {
		this.signee = signee;
	}

	public String getTransaction_sn() {
		return transaction_sn;
	}

	public void setTransaction_sn(String transaction_sn) {
		this.transaction_sn = transaction_sn;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getIdTxn() {
		return idTxn;
	}

	public void setIdTxn(String idTxn) {
		this.idTxn = idTxn;
	}

	public int getSystem_pay_type() {
		return system_pay_type;
	}

	public void setSystem_pay_type(int system_pay_type) {
		this.system_pay_type = system_pay_type;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getTrace_no() {
		return trace_no;
	}

	public void setTrace_no(String trace_no) {
		this.trace_no = trace_no;
	}

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public String getDelivery_man() {
		return delivery_man;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}

	public String getTrackinfo() {
		return trackinfo;
	}

	public void setTrackinfo(String trackinfo) {
		this.trackinfo = trackinfo;
	}

	public double getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(double receivablefee) {
		this.receivablefee = receivablefee;
	}

	public double getReceivedfee() {
		return receivedfee;
	}

	public void setReceivedfee(double receivedfee) {
		this.receivedfee = receivedfee;
	}

	public double getOrder_payable_amt() {
		return order_payable_amt;
	}

	public void setOrder_payable_amt(double order_payable_amt) {
		this.order_payable_amt = order_payable_amt;
	}

	public String getPodremark() {
		return podremark;
	}

	public void setPodremark(String podremark) {
		this.podremark = podremark;
	}

}
