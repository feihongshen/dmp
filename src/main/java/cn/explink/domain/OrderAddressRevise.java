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
	
}
