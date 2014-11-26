package cn.explink.b2c.maikaolin.xml;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Package {
	String express_id;// varchar 8 Y 快递公司ID
	String express_name;// varchar 30 Y 快递公司名称
	String package_id;// varchar 20 Y 包裹单号
	String package_type;// char 1 Y 发货单类型：
	// 1：正常
	// 2：换货
	// 3：退货
	String contact_name;// varchar 20 Y 客户姓名
	String contact_phone;// varchar 255 Y 客户联系电话
	String contact_phone2;// varchar 255 N 客户联系电话2
	String postal_code;// varchar 10 Y 邮编
	String province;// varchar 8 N 省份
	String city;// varchar 40 N 城市
	String zone;// varchar 40 N 区县
	String address;// varchar 255 N 详细地址(把省市区加在前面）
	double cod_amount;// numeric(9, 2) Y 应收金额(元，精确到小数点后2位)
	BigDecimal return_amount;// numeric(9, 2) Y 应退金额(元，精确到小数点后2位)
	BigDecimal ship_amount;// numeric(9, 2) Y 发货金额(元，精确到小数点后2位)
	BigDecimal returnproduct_amount;// numeric(9, 2) Y 退货金额(元，精确到小数点后2位)
	int itemtotal;// smallint Y 包裹包含的商品件数合计
	BigDecimal weight;// numeric(9, 2) N 重量
	String warehousearea;// nvarchar 20 Y 仓库名称
	String remark;// nvarchar 2500 N 送货备注
	// 以下是商品Item的字段
	private List<Items> listitems = null;

	@XmlElement(name = "express_id")
	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	@XmlElement(name = "express_name")
	public String getExpress_name() {
		return express_name;
	}

	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}

	@XmlElement(name = "package_id")
	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	@XmlElement(name = "package_type")
	public String getPackage_type() {
		return package_type;
	}

	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}

	@XmlElement(name = "contact_name")
	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	@XmlElement(name = "contact_phone")
	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	@XmlElement(name = "contact_phone2")
	public String getContact_phone2() {
		return contact_phone2;
	}

	public void setContact_phone2(String contact_phone2) {
		this.contact_phone2 = contact_phone2;
	}

	@XmlElement(name = "postal_code")
	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	@XmlElement(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@XmlElement(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "zone")
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@XmlElement(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlElement(name = "cod_amount")
	public double getCod_amount() {
		return cod_amount;
	}

	public void setCod_amount(double cod_amount) {
		this.cod_amount = cod_amount;
	}

	@XmlElement(name = "return_amount")
	public BigDecimal getReturn_amount() {
		return return_amount;
	}

	public void setReturn_amount(BigDecimal return_amount) {
		this.return_amount = return_amount;
	}

	@XmlElement(name = "ship_amount")
	public BigDecimal getShip_amount() {
		return ship_amount;
	}

	public void setShip_amount(BigDecimal ship_amount) {
		this.ship_amount = ship_amount;
	}

	@XmlElement(name = "returnproduct_amount")
	public BigDecimal getReturnproduct_amount() {
		return returnproduct_amount;
	}

	public void setReturnproduct_amount(BigDecimal returnproduct_amount) {
		this.returnproduct_amount = returnproduct_amount;
	}

	@XmlElement(name = "itemtotal")
	public int getItemtotal() {
		return itemtotal;
	}

	public void setItemtotal(int itemtotal) {
		this.itemtotal = itemtotal;
	}

	@XmlElement(name = "weight")
	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@XmlElement(name = "warehousearea")
	public String getWarehousearea() {
		return warehousearea;
	}

	public void setWarehousearea(String warehousearea) {
		this.warehousearea = warehousearea;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "Items")
	public List<Items> getListitems() {
		return listitems;
	}

	public void setListitems(List<Items> listitems) {
		this.listitems = listitems;
	}

}
