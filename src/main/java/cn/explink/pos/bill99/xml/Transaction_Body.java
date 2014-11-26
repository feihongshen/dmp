package cn.explink.pos.bill99.xml;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Body {

	private String delivery_man;
	private String password;
	private String order_no;
	private double order_payable_amt;
	private int pay_type;
	private String idTxn;
	private String terminal_id;
	private String trace_no;
	private double void_amt;
	private String void_trace_no;
	private String signee;
	private String consignee_sign_flag;
	private int ex_code;
	private String ex_desc;
	private String orderId;
	private double reverse_amt;
	private int reverse_tran_type;
	private String reverse_trace_no;

	@XmlElement(name = "delivery_man")
	public String getDelivery_man() {
		return delivery_man;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}

	@XmlElement(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@XmlElement(name = "idTxn")
	public String getIdTxn() {
		return idTxn;
	}

	public void setIdTxn(String idTxn) {
		this.idTxn = idTxn;
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

	@XmlElement(name = "ex_code")
	public int getEx_code() {
		return ex_code;
	}

	public void setEx_code(int ex_code) {
		this.ex_code = ex_code;
	}

	@XmlElement(name = "ex_desc")
	public String getEx_desc() {
		return ex_desc;
	}

	public void setEx_desc(String ex_desc) {
		this.ex_desc = ex_desc;
	}

	@XmlElement(name = "orderId")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlElement(name = "reverse_amt")
	public double getReverse_amt() {
		return reverse_amt;
	}

	public void setReverse_amt(double reverse_amt) {
		this.reverse_amt = reverse_amt;
	}

	@XmlElement(name = "reverse_tran_type")
	public int getReverse_tran_type() {
		return reverse_tran_type;
	}

	public void setReverse_tran_type(int reverse_tran_type) {
		this.reverse_tran_type = reverse_tran_type;
	}

	@XmlElement(name = "reverse_trace_no")
	public String getReverse_trace_no() {
		return reverse_trace_no;
	}

	public void setReverse_trace_no(String reverse_trace_no) {
		this.reverse_trace_no = reverse_trace_no;
	}

}
