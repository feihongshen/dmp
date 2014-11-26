package cn.explink.b2c.huitongtx;

/**
 * tmall订单详细的字段 用于json格式的解析
 * 
 * @author Administrator
 *
 */
public class HuitongtxXMLNote {

	private String tms_service_code;
	private String order_code;
	private String order_flag;
	private long total_amount;
	private Integer schedule_type;
	private String schedule_start;
	private String schedule_end;
	private String receiver_name;
	private String receiver_zip;
	private String receiver_province;
	private String receiver_city;
	private String receiver_district;
	private String servercode; // 服务编码

	public String getServercode() {
		return servercode;
	}

	public void setServercode(String servercode) {
		this.servercode = servercode;
	}

	private String receiver_address;
	private String receiver_mobile;
	private String receiver_phone;
	private String package_weight;
	private String package_volume;
	private String tms_order_code;
	private String taskcode; // 唯一标识

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getTms_order_code() {
		return tms_order_code;
	}

	public void setTms_order_code(String tms_order_code) {
		this.tms_order_code = tms_order_code;
	}

	private String wms_code;
	private String wms_address;

	public String getTms_service_code() {
		return tms_service_code;
	}

	public void setTms_service_code(String tms_service_code) {
		this.tms_service_code = tms_service_code;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getOrder_flag() {
		return order_flag;
	}

	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}

	public long getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(long total_amount) {
		this.total_amount = total_amount;
	}

	public Integer getSchedule_type() {
		return schedule_type;
	}

	public void setSchedule_type(Integer schedule_type) {
		this.schedule_type = schedule_type;
	}

	public String getSchedule_start() {
		return schedule_start;
	}

	public void setSchedule_start(String schedule_start) {
		this.schedule_start = schedule_start;
	}

	public String getSchedule_end() {
		return schedule_end;
	}

	public void setSchedule_end(String schedule_end) {
		this.schedule_end = schedule_end;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_zip() {
		return receiver_zip;
	}

	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}

	public String getReceiver_province() {
		return receiver_province;
	}

	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}

	public String getReceiver_city() {
		return receiver_city;
	}

	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}

	public String getReceiver_district() {
		return receiver_district;
	}

	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getPackage_weight() {
		return package_weight;
	}

	public void setPackage_weight(String package_weight) {
		this.package_weight = package_weight;
	}

	public String getPackage_volume() {
		return package_volume;
	}

	public void setPackage_volume(String package_volume) {
		this.package_volume = package_volume;
	}

	public String getWms_code() {
		return wms_code;
	}

	public void setWms_code(String wms_code) {
		this.wms_code = wms_code;
	}

	public String getWms_address() {
		return wms_address;
	}

	public void setWms_address(String wms_address) {
		this.wms_address = wms_address;
	}

}
