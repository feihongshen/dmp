package cn.explink.b2c.vipshop.addresspre.response;

public class ResponseItem {

	private String itemno;// 序号
	private String netid;// 配送点id
	private String netpoint;// 配送点
	private String remark;// 错误信息
	private String province;// 省
	private String city;// 市
	private String district;// 区
	private String finaladdress;// 地址

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
	}

	public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getFinaladdress() {
		return finaladdress;
	}

	public void setFinaladdress(String finaladdress) {
		this.finaladdress = finaladdress;
	}

}
