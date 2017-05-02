package cn.explink.b2c.liantongordercenter.xmlDto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Order")
public class OrderDto {
	private String orderId;// 客户订单号
	private String mailNo;// 物流公司运单号
	private int payMethod;// 快递费付款方式：1：寄方付2：收方付
	private int isGenGillNo;// 是否要求返回运单号：1、要求 其它：不返回运单号
	private String jCompany;// 寄件方公司名称
	private String jContact;// 寄件方联系人
	private String jTel;// 寄件方联系电话
	private String jMobile;// 寄件方手机
	private String jProvince;// 寄件方所在省分
								// 字段要求：必须是标准的省名称称谓，如：南京市，直辖市，请直接填写：北京市、上海市、重庆市
	private String jCity;// 寄方所在的城市名称标准称谓
	private String jCounty;// 寄方所在县/区标准称谓，示例：东城区
	private String jAddress;// 寄件方详细地址，如：广东省深圳市福田区新洲十一街万基商务大厦 10 楼
	private String jPostCode;// 邮政编码
	private String dCompany;// 收件方公司
	private String dContact;// 收件方联系人
	private String dTel;// 收件方电话，当d_mobile不为空时，选填。
	private String dMobile;// 收件方手机，当d_tel不为空时，选填。
	private String dProvince;// 收件方所在省分
								// 字段要求：必须是标准的省名称称谓，如：南京市，直辖市，请直接填写：北京市、上海市、重庆市。
	private String dCity;// 收件方所在的城市名称标准称谓。
	private String dCounty;// 收件所在县/区标准称谓，示例：东城区
	private String dAddress;// 收件方详细地址，如：广东省深圳市福田区新洲十一街万基商务大厦 10 楼
	private String dPostCode;// 邮政编码
	private String remark;// 备注

	private List<CargoDto> cargoDtos;

	@XmlAttribute(name = "orderid")
	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlAttribute(name = "mailno")
	public String getMailNo() {
		return this.mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	@XmlAttribute(name = "pay_method")
	public int getPayMethod() {
		return this.payMethod;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	@XmlAttribute(name = "is_gen_bill_no")
	public int getIsGenGillNo() {
		return this.isGenGillNo;
	}

	@XmlElement(name = "Cargo")
	public List<CargoDto> getCargoDtos() {
		return this.cargoDtos;
	}

	public void setCargoDtos(List<CargoDto> cargoDtos) {
		this.cargoDtos = cargoDtos;
	}

	public void setIsGenGillNo(int isGenGillNo) {
		this.isGenGillNo = isGenGillNo;
	}

	@XmlAttribute(name = "j_company")
	public String getjCompany() {
		return this.jCompany;
	}

	public void setjCompany(String jCompany) {
		this.jCompany = jCompany;
	}

	@XmlAttribute(name = "j_contact")
	public String getjContact() {
		return this.jContact;
	}

	public void setjContact(String jContact) {
		this.jContact = jContact;
	}

	@XmlAttribute(name = "j_tel")
	public String getjTel() {
		return this.jTel;
	}

	public void setjTel(String jTel) {
		this.jTel = jTel;
	}

	@XmlAttribute(name = "j_mobile")
	public String getjMobile() {
		return this.jMobile;
	}

	public void setjMobile(String jMobile) {
		this.jMobile = jMobile;
	}

	@XmlAttribute(name = "j_province")
	public String getjProvince() {
		return this.jProvince;
	}

	public void setjProvince(String jProvince) {
		this.jProvince = jProvince;
	}

	@XmlAttribute(name = "j_city")
	public String getjCity() {
		return this.jCity;
	}

	public void setjCity(String jCity) {
		this.jCity = jCity;
	}

	@XmlAttribute(name = "j_county")
	public String getjCounty() {
		return this.jCounty;
	}

	public void setjCounty(String jCounty) {
		this.jCounty = jCounty;
	}

	@XmlAttribute(name = "j_address")
	public String getjAddress() {
		return this.jAddress;
	}

	public void setjAddress(String jAddress) {
		this.jAddress = jAddress;
	}

	@XmlAttribute(name = "j_post_code")
	public String getjPostCode() {
		return this.jPostCode;
	}

	public void setjPostCode(String jPostCode) {
		this.jPostCode = jPostCode;
	}

	@XmlAttribute(name = "d_company")
	public String getdCompany() {
		return this.dCompany;
	}

	public void setdCompany(String dCompany) {
		this.dCompany = dCompany;
	}

	@XmlAttribute(name = "d_contact")
	public String getdContact() {
		return this.dContact;
	}

	public void setdContact(String dContact) {
		this.dContact = dContact;
	}

	@XmlAttribute(name = "d_tel")
	public String getdTel() {
		return this.dTel;
	}

	public void setdTel(String dTel) {
		this.dTel = dTel;
	}

	@XmlAttribute(name = "d_mobile")
	public String getdMobile() {
		return this.dMobile;
	}

	public void setdMobile(String dMobile) {
		this.dMobile = dMobile;
	}

	@XmlAttribute(name = "d_ province")
	public String getdProvince() {
		return this.dProvince;
	}

	public void setdProvince(String dProvince) {
		this.dProvince = dProvince;
	}

	@XmlAttribute(name = "d_city")
	public String getdCity() {
		return this.dCity;
	}

	public void setdCity(String dCity) {
		this.dCity = dCity;
	}

	@XmlAttribute(name = "d_county")
	public String getdCounty() {
		return this.dCounty;
	}

	public void setdCounty(String dCounty) {
		this.dCounty = dCounty;
	}

	@XmlAttribute(name = "d_address")
	public String getdAddress() {
		return this.dAddress;
	}

	public void setdAddress(String dAddress) {
		this.dAddress = dAddress;
	}

	@XmlAttribute(name = "d_post_code")
	public String getdPostCode() {
		return this.dPostCode;
	}

	public void setdPostCode(String dPostCode) {
		this.dPostCode = dPostCode;
	}

	@XmlAttribute(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
