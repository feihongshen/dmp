package cn.explink.b2c.efast.orders_json;

/**
 * 每个订单的基本列表
 * 
 * @author Administrator
 *
 */
public class OrderInfo {
	private String order_sn; // 订单编号,唯一标识
	private String deal_code; // 交易号
	private int order_status; // 订单状态
	private int shipping_status; // 发货状态
	private int pay_status; // 发货装填
	private String shipping_name; // 配送方式
	private String pay_name; // 支付方式
	private String invoice_no; // 快递单号
	private String add_time; // 下单时间
	private String delivery_time; // 发货时间
	private String process_status;
	private String is_send;
	private String is_locked;
	private String is_separate;
	private String sd_id;

	public String getProcess_status() {
		return process_status;
	}

	public void setProcess_status(String process_status) {
		this.process_status = process_status;
	}

	public String getIs_send() {
		return is_send;
	}

	public void setIs_send(String is_send) {
		this.is_send = is_send;
	}

	public String getIs_locked() {
		return is_locked;
	}

	public void setIs_locked(String is_locked) {
		this.is_locked = is_locked;
	}

	public String getIs_separate() {
		return is_separate;
	}

	public void setIs_separate(String is_separate) {
		this.is_separate = is_separate;
	}

	public String getSd_id() {
		return sd_id;
	}

	public void setSd_id(String sd_id) {
		this.sd_id = sd_id;
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

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

}
