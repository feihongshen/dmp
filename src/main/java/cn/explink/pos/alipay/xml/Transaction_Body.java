package cn.explink.pos.alipay.xml;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Body {

	private String delivery_man;
	private String delivery_name;
	private String delivery_zone;
	private String order_no;
	private double order_payable_amt;
	private int pay_type;
	private String terminal_id;
	private String trace_no;
	private String alipay_trace_no;
	private String trade_no;
	private double order_amt;
	private String acq_type;
	private String password;

	@XmlElement(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement(name = "delivery_man")
	public String getDelivery_man() {
		return delivery_man;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}

	@XmlElement(name = "delivery_name")
	public String getDelivery_name() {
		return delivery_name;
	}

	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}

	@XmlElement(name = "delivery_zone")
	public String getDelivery_zone() {
		return delivery_zone;
	}

	public void setDelivery_zone(String delivery_zone) {
		this.delivery_zone = delivery_zone;
	}

	@XmlElement(name = "order_no")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@XmlElement(name = "order_payable_amt")
	public double getOrder_payable_amt() {
		return order_payable_amt;
	}

	public void setOrder_payable_amt(double order_payable_amt) {
		this.order_payable_amt = order_payable_amt;
	}

	@XmlElement(name = "pay_type")
	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	@XmlElement(name = "terminal_id")
	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	@XmlElement(name = "trace_no")
	public String getTrace_no() {
		return trace_no;
	}

	public void setTrace_no(String trace_no) {
		this.trace_no = trace_no;
	}

	@XmlElement(name = "alipay_trace_no")
	public String getAlipay_trace_no() {
		return alipay_trace_no;
	}

	public void setAlipay_trace_no(String alipay_trace_no) {
		this.alipay_trace_no = alipay_trace_no;
	}

	@XmlElement(name = "trade_no")
	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	@XmlElement(name = "order_amt")
	public double getOrder_amt() {
		return order_amt;
	}

	public void setOrder_amt(double order_amt) {
		this.order_amt = order_amt;
	}

	@XmlElement(name = "acq_type")
	public String getAcq_type() {
		return acq_type;
	}

	public void setAcq_type(String acq_type) {
		this.acq_type = acq_type;
	}

	@XmlElement(name = "signee")
	public String getSignee() {
		return signee;
	}

	public void setSignee(String signee) {
		this.signee = signee;
	}

	@XmlElement(name = "consignee_sign_flag")
	public String getConsignee_sign_flag() {
		return consignee_sign_flag;
	}

	public void setConsignee_sign_flag(String consignee_sign_flag) {
		this.consignee_sign_flag = consignee_sign_flag;
	}

	@XmlElement(name = "void_amt")
	public double getVoid_amt() {
		return void_amt;
	}

	public void setVoid_amt(double void_amt) {
		this.void_amt = void_amt;
	}

	@XmlElement(name = "void_trace_no")
	public String getVoid_trace_no() {
		return void_trace_no;
	}

	public void setVoid_trace_no(String void_trace_no) {
		this.void_trace_no = void_trace_no;
	}

	@XmlElement(name = "ori_alipay_trace_no")
	public String getOri_alipay_trace_no() {
		return ori_alipay_trace_no;
	}

	public void setOri_alipay_trace_no(String ori_alipay_trace_no) {
		this.ori_alipay_trace_no = ori_alipay_trace_no;
	}

	@XmlElement(name = "ex_code")
	public String getEx_code() {
		return ex_code;
	}

	public void setEx_code(String ex_code) {
		this.ex_code = ex_code;
	}

	@XmlElement(name = "ex_desc")
	public String getEx_desc() {
		return ex_desc;
	}

	public void setEx_desc(String ex_desc) {
		this.ex_desc = ex_desc;
	}

	@XmlElement(name = "amt")
	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	@XmlElement(name = "merchant_code")
	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	@XmlElement(name = "account_keyword")
	public String getAccount_keyword() {
		return account_keyword;
	}

	public void setAccount_keyword(String account_keyword) {
		this.account_keyword = account_keyword;
	}

	@XmlElement(name = "merchant_biz_no")
	public String getMerchant_biz_no() {
		return merchant_biz_no;
	}

	public void setMerchant_biz_no(String merchant_biz_no) {
		this.merchant_biz_no = merchant_biz_no;
	}

	@XmlElement(name = "merchant_biz_type")
	public String getMerchant_biz_type() {
		return merchant_biz_type;
	}

	public void setMerchant_biz_type(String merchant_biz_type) {
		this.merchant_biz_type = merchant_biz_type;
	}

	@XmlElement(name = "desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlElement(name = "item")
	public item getItem() {
		return item;
	}

	public void setItem(item item) {
		this.item = item;
	}

	private String signee;
	private String consignee_sign_flag;
	private double void_amt;
	private String void_trace_no;
	private String ori_alipay_trace_no;
	private String ex_code;
	private String ex_desc;
	private double amt;
	private String merchant_code;
	private String account_keyword;
	private String merchant_biz_no;
	private String merchant_biz_type;
	private String desc;

	private item item;

}
