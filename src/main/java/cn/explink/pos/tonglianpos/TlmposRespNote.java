package cn.explink.pos.tonglianpos;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;

/**
 * 存储alipay返回信息的实体
 * 
 * @author Administrator
 *
 */
public class TlmposRespNote {

	private CwbOrder cwbOrder;
	private DeliveryState deliverstate;

	private String resp_code;
	private String resp_msg;
	private String order_no; // 订单号

	private long deliverid;
	private String trackinfo; // 支付交互信息的详情

	private String podremark; // 支付备注
	private int pay_type; // 支付类型，这里一定要统一 ,对应dmp中的PaytypeEnum。包括POS交易记录表也是。
	private String acq_type; // 收单模式 single单笔，merge合单，split分单。
	private int alipay_Pay_type; // 支付宝的支付类型 01现金，02刷卡，04无货款 ,05支票

	// 几个金额要区分清楚
	private double receivablefee; // 系统中detail表中的应收金额
	private double receivedfee; // 系统中的实收金额 deliverystate表中查询
	private double order_payable_amt; // 支付宝的本次应付款.
	private double order_amt; // 支付宝的总共应付款。

	private String terminal_id; // 终端号
	private String trace_no; // 交易凭证号
	private String delivery_man; // 小件员
	private String signee; // 签收人
	private char consignee_sign_flag; // 签收类型 Y 本人签收，N他人代签收 alipay签收类型标识
	private int sign_type; // 系统中签收类型的标识 1.本人签收，2他人代签收

	private String sign_remark; // 签收备注信息
	private String transaction_id; //
	private long branchid; // 站点id
	private String merchant_code; // 供货商编码
	private String consignee; // 收件人 有特殊限制

	private String consignee_contact; // 联系方式
	private String merchant_biz_no; // b2c 商户编号

	private String merchant_biz_type = ""; // b2c 商务订单类型
	private String ex_code; // 异常编码
	private String ex_desc; // 异常说明

	public double getAmount_after() {
		return amount_after;
	}

	public void setAmount_after(double amount_after) {
		this.amount_after = amount_after;
	}

	// 运单撤销专用
	private double void_amt; // 撤销金额
	private double amount_after;// 撤销后金额

	public double getVoid_amt() {
		return void_amt;
	}

	public void setVoid_amt(double void_amt) {
		this.void_amt = void_amt;
	}

	public String getConsignee() {
		return consignee;
	}

	public String getEx_code() {
		return ex_code;
	}

	public void setEx_code(String ex_code) {
		this.ex_code = ex_code;
	}

	public String getEx_desc() {
		return ex_desc;
	}

	public void setEx_desc(String ex_desc) {
		this.ex_desc = ex_desc;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMerchant_biz_no() {
		return merchant_biz_no;
	}

	public void setMerchant_biz_no(String merchant_biz_no) {
		this.merchant_biz_no = merchant_biz_no;
	}

	public String getMerchant_biz_type() {
		return merchant_biz_type;
	}

	public void setMerchant_biz_type(String merchant_biz_type) {
		this.merchant_biz_type = merchant_biz_type;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getConsignee_contact() {
		return consignee_contact;
	}

	public void setConsignee_contact(String consignee_contact) {
		this.consignee_contact = consignee_contact;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public int getSign_type() {
		return sign_type;
	}

	public void setSign_type(int sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign_remark() {
		return sign_remark;
	}

	public void setSign_remark(String sign_remark) {
		this.sign_remark = sign_remark;
	}

	public char getConsignee_sign_flag() {
		return consignee_sign_flag;
	}

	public void setConsignee_sign_flag(char consignee_sign_flag) {
		this.consignee_sign_flag = consignee_sign_flag;
	}

	public String getSignee() {
		return signee;
	}

	public void setSignee(String signee) {
		this.signee = signee;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public int getAlipay_Pay_type() {
		return alipay_Pay_type;
	}

	public void setAlipay_Pay_type(int alipay_Pay_type) {
		this.alipay_Pay_type = alipay_Pay_type;
	}

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public DeliveryState getDeliverstate() {
		return deliverstate;
	}

	public void setDeliverstate(DeliveryState deliverstate) {
		this.deliverstate = deliverstate;
	}

	public String getDelivery_man() {
		return delivery_man;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
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

	public double getOrder_amt() {
		return order_amt;
	}

	public void setOrder_amt(double order_amt) {
		this.order_amt = order_amt;
	}

	public String getAcq_type() {
		return acq_type;
	}

	public void setAcq_type(String acq_type) {
		this.acq_type = acq_type;
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

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
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

	public String getPodremark() {
		return podremark;
	}

	public void setPodremark(String podremark) {
		this.podremark = podremark;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

}
