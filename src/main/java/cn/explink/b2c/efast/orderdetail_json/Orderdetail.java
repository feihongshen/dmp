package cn.explink.b2c.efast.orderdetail_json;

import java.math.BigDecimal;
import java.util.List;

/**
 * 每个订单的基本列表
 * 
 * @author Administrator
 *
 */
public class Orderdetail {
	private String order_id; // 订单id
	private String order_sn; // 订单编号
	private String deal_code; // 交易号
	private int order_status; // 订单状态0（未确认）1确认 2挂起 3作废
	private int shipping_status; // 发货状态(0未发货，1已发货 3备货)
	private int pay_status; // 付款状态 （0未付款 2已付款）
	private String consignee; // 收货人
	private String address;
	private String zipcode;
	private String tel;
	private String mobile;
	private String shipping_name;
	private String pay_name;
	private String invoice_no;// 快递单号
	private List<OrderShipping> orders; // 商品明细列表
	private BigDecimal goods_price; // 商品价格
	private String shipping_fee;
	private String order_amount;

	public String getShipping_fee() {
		return shipping_fee;
	}

	public void setShipping_fee(String shipping_fee) {
		this.shipping_fee = shipping_fee;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getDeal_code() {
		return deal_code;
	}

	public void setDeal_code(String deal_code) {
		this.deal_code = deal_code;
	}

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public int getShipping_status() {
		return shipping_status;
	}

	public void setShipping_status(int shipping_status) {
		this.shipping_status = shipping_status;
	}

	public int getPay_status() {
		return pay_status;
	}

	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getShipping_name() {
		return shipping_name;
	}

	public void setShipping_name(String shipping_name) {
		this.shipping_name = shipping_name;
	}

	public String getPay_name() {
		return pay_name;
	}

	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public List<OrderShipping> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderShipping> orders) {
		this.orders = orders;
	}

	public BigDecimal getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(BigDecimal goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_barcode() {
		return goods_barcode;
	}

	public void setGoods_barcode(String goods_barcode) {
		this.goods_barcode = goods_barcode;
	}

	private String goods_barcode; // 商品sku

}
