package cn.explink.b2c.zhemeng.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class RequestOrder {
	private String tms_service_code;
	private String order_code;
	
	private String ordertype;
	private long total_amount;
	private String receiver_name;
	private String receiver_zip;
	private String receiver_province;
	private String receiver_city;
	private String receiver_district;
	private String receiver_address;
	private String receiver_mobile;
	private String receiver_phone;
	private String package_weight;
	private String package_volume;
	private String sd_name;
	private String sd_mobile;
	private String sd_phone;
	private String remark;
	private long cccharge;
	private String orderstr;
	private int pcs;
	private String orderno;
	private String descname;
	
	@XmlElement(name = "tms_service_code")
	public String getTms_service_code() {
		return tms_service_code;
	}
	public void setTms_service_code(String tms_service_code) {
		this.tms_service_code = tms_service_code;
	}
	@XmlElement(name = "order_code")
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	@XmlElement(name = "ordertype")
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	@XmlElement(name = "total_amount")
	public long getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(long total_amount) {
		this.total_amount = total_amount;
	}
	@XmlElement(name = "receiver_name")
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	@XmlElement(name = "receiver_zip")
	public String getReceiver_zip() {
		return receiver_zip;
	}
	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}
	@XmlElement(name = "receiver_province")
	public String getReceiver_province() {
		return receiver_province;
	}
	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}
	@XmlElement(name = "receiver_city")
	public String getReceiver_city() {
		return receiver_city;
	}
	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}
	@XmlElement(name = "receiver_district")
	public String getReceiver_district() {
		return receiver_district;
	}
	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}
	@XmlElement(name = "receiver_address")
	public String getReceiver_address() {
		return receiver_address;
	}
	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}
	@XmlElement(name = "receiver_mobile")
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	@XmlElement(name = "receiver_phone")
	public String getReceiver_phone() {
		return receiver_phone;
	}
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}
	@XmlElement(name = "package_weight")
	public String getPackage_weight() {
		return package_weight;
	}
	public void setPackage_weight(String package_weight) {
		this.package_weight = package_weight;
	}
	@XmlElement(name = "package_volume")
	public String getPackage_volume() {
		return package_volume;
	}
	public void setPackage_volume(String package_volume) {
		this.package_volume = package_volume;
	}
	@XmlElement(name = "sd_name")
	public String getSd_name() {
		return sd_name;
	}
	public void setSd_name(String sd_name) {
		this.sd_name = sd_name;
	}
	@XmlElement(name = "sd_mobile")
	public String getSd_mobile() {
		return sd_mobile;
	}
	public void setSd_mobile(String sd_mobile) {
		this.sd_mobile = sd_mobile;
	}
	@XmlElement(name = "sd_phone")
	public String getSd_phone() {
		return sd_phone;
	}
	public void setSd_phone(String sd_phone) {
		this.sd_phone = sd_phone;
	}
	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@XmlElement(name = "cccharge")
	public long getCccharge() {
		return cccharge;
	}
	public void setCccharge(long cccharge) {
		this.cccharge = cccharge;
	}
	@XmlElement(name = "orderstr")
	public String getOrderstr() {
		return orderstr;
	}
	public void setOrderstr(String orderstr) {
		this.orderstr = orderstr;
	}
	@XmlElement(name = "pcs")
	public int getPcs() {
		return pcs;
	}
	public void setPcs(int pcs) {
		this.pcs = pcs;
	}
	@XmlElement(name = "orderno")
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	@XmlElement(name = "descname")
	public String getDescname() {
		return descname;
	}
	public void setDescname(String descname) {
		this.descname = descname;
	}
	

	

	
}
