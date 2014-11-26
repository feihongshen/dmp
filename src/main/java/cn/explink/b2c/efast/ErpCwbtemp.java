package cn.explink.b2c.efast;

/**
 * 临时表 中兴云购ERP获取订单列表
 * 
 * @author Administrator
 *
 */
public class ErpCwbtemp {

	private long erpid;
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
	private long total_results; // 总数
	private long page_no; // 总页数
	private long page_size; // 每页数量
	private String cretime; // 创建数据时间
	private String getdatatime; // 获取订单详情时间，默认"",成功为时间格式，失败为2 --索引列

	public long getErpid() {
		return erpid;
	}

	public void setErpid(long erpid) {
		this.erpid = erpid;
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

	public long getTotal_results() {
		return total_results;
	}

	public void setTotal_results(long total_results) {
		this.total_results = total_results;
	}

	public long getPage_no() {
		return page_no;
	}

	public void setPage_no(long page_no) {
		this.page_no = page_no;
	}

	public long getPage_size() {
		return page_size;
	}

	public void setPage_size(long page_size) {
		this.page_size = page_size;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public String getGetdatatime() {
		return getdatatime;
	}

	public void setGetdatatime(String getdatatime) {
		this.getdatatime = getdatatime;
	}

}
