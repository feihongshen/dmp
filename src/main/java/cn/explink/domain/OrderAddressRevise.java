package cn.explink.domain;
/**
 * 订单地址修改信息表中对应的类
 * @author acer
 *
 */
public class OrderAddressRevise {
	private long id;//主键
	private String cwb;//订单号
	private String address;//修改的地址
	private String revisetime;//修改的时间
	private String modifiername;//修改人
	private String receivemen;//收件人
	private String phone;//电话
	private String peisongtime;//配送时间
	private String destination;//目的站
	private String customerrequest;//客户请求
	private String exceldeliver;//匹配小件员
	
	public String getReceivemen() {
		return receivemen;
	}
	public void setReceivemen(String receivemen) {
		this.receivemen = receivemen;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPeisongtime() {
		return peisongtime;
	}
	public void setPeisongtime(String peisongtime) {
		this.peisongtime = peisongtime;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getCustomerrequest() {
		return customerrequest;
	}
	public void setCustomerrequest(String customerrequest) {
		this.customerrequest = customerrequest;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRevisetime() {
		return revisetime;
	}
	public void setRevisetime(String revisetime) {
		this.revisetime = revisetime;
	}
	public String getModifiername() {
		return modifiername;
	}
	public void setModifiername(String modifiername) {
		this.modifiername = modifiername;
	}
	public String getExceldeliver() {
		return exceldeliver;
	}
	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}
	
}
