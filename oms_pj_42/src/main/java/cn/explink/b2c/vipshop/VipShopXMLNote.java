package cn.explink.b2c.vipshop;

/**
 * vipshop XML的节点
 * 
 * @author Administrator
 *
 */
public class VipShopXMLNote {

	private String version = "1.0";
	private String request_time;
	private String cust_code;
	private String cust_data_id;
	private String order_sn;
	private String order_status;
	private String order_status_info;
	private String current_city_name;
	private String order_status_time;
	private String deliverUser; // 派送员姓名
	private String deliverMobile; // 派送员电话
	private String deliverBranch; // 分站名称
	private String order_status_info_temp; // 临时存放跟踪记录
	private long cwbordertypeid; // 订单类型
	private String details; // json 的明细
	private String deliver_name;
	private String deliver_mobile;
	private String station;
	private String is_unpacked=""; //是否开箱验货 1_客户拒收
	private String goods_reason; //未退原因
	private long is_allograph_sign;  //是否代 签收 1：他人签收.0:本人签收
	private String sign_man;
	private String sign_man_phone=""; //代签收人电话



	public long getIs_allograph_sign() {
		return is_allograph_sign;
	}

	public void setIs_allograph_sign(long is_allograph_sign) {
		this.is_allograph_sign = is_allograph_sign;
	}


	public String getGoods_reason() {
		return goods_reason;
	}

	public void setGoods_reason(String goods_reason) {
		this.goods_reason = goods_reason;
	}

	public String getIs_unpacked() {
		return is_unpacked;
	}

	public void setIs_unpacked(String is_unpacked) {
		this.is_unpacked = is_unpacked;
	}

	public String getDeliver_name() {
		return deliver_name;
	}

	public void setDeliver_name(String deliver_name) {
		this.deliver_name = deliver_name;
	}

	public String getDeliver_mobile() {
		return deliver_mobile;
	}

	public void setDeliver_mobile(String deliver_mobile) {
		this.deliver_mobile = deliver_mobile;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	private String shangmenlanshoutime;// 上门揽收时间

	public String getShangmenlanshoutime() {
		return shangmenlanshoutime;
	}

	public void setShangmenlanshoutime(String shangmenlanshoutime) {
		this.shangmenlanshoutime = shangmenlanshoutime;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getOrder_status_info_temp() {
		return order_status_info_temp;
	}

	public void setOrder_status_info_temp(String order_status_info_temp) {
		this.order_status_info_temp = order_status_info_temp;
	}

	public String getDeliverBranch() {
		return deliverBranch;
	}

	public void setDeliverBranch(String deliverBranch) {
		this.deliverBranch = deliverBranch;
	}

	public String getDeliverUser() {
		return deliverUser;
	}

	public void setDeliverUser(String deliverUser) {
		this.deliverUser = deliverUser;
	}

	public String getDeliverMobile() {
		return deliverMobile;
	}

	public void setDeliverMobile(String deliverMobile) {
		this.deliverMobile = deliverMobile;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public String getCust_code() {
		return cust_code;
	}

	public void setCust_code(String cust_code) {
		this.cust_code = cust_code;
	}

	public String getCust_data_id() {
		return cust_data_id;
	}

	public void setCust_data_id(String cust_data_id) {
		this.cust_data_id = cust_data_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getOrder_status_info() {
		return order_status_info;
	}

	public void setOrder_status_info(String order_status_info) {
		this.order_status_info = order_status_info;
	}

	public String getCurrent_city_name() {
		return current_city_name;
	}

	public void setCurrent_city_name(String current_city_name) {
		this.current_city_name = current_city_name;
	}

	public String getOrder_status_time() {
		return order_status_time;
	}

	public void setOrder_status_time(String order_status_time) {
		this.order_status_time = order_status_time;
	}

	public String getSign_man() {
		return sign_man;
	}

	public void setSign_man(String sign_man) {
		this.sign_man = sign_man;
	}

	public String getSign_man_phone() {
		return sign_man_phone;
	}

	public void setSign_man_phone(String sign_man_phone) {
		this.sign_man_phone = sign_man_phone;
	}
	
	

}
