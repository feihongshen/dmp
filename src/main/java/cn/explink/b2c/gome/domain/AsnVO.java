package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "asnVo")
public class AsnVO {
	private String businessCode;// 业务代码
	private String buid;// 渠道代码
	private String asnNumber;// ASN号
	private String asnType;// 支线物流公司编码
	private String lspCode;// 干线物流公司编码
	private String lspAbbr;// 物流公司代码
	private String shipDate;// ASN日期
	private String fromMasLoc;// 发货仓库
	private String comments;// 注释
	private AddressVO shippingAddress;// AddressVO
	private TruckInfoVO truckInfos;// List<TruckInfoVO> trucks
	private ItemVO items;// List<ItemVO> items

	@XmlElement(name = "businessCode")
	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	@XmlElement(name = "buid")
	public String getBuid() {
		return buid;
	}

	public void setBuid(String buid) {
		this.buid = buid;
	}

	@XmlElement(name = "asnNumber")
	public String getAsnNumber() {
		return asnNumber;
	}

	public void setAsnNumber(String asnNumber) {
		this.asnNumber = asnNumber;
	}

	@XmlElement(name = "asnType")
	public String getAsnType() {
		return asnType;
	}

	public void setAsnType(String asnType) {
		this.asnType = asnType;
	}

	@XmlElement(name = "lspCode")
	public String getLspCode() {
		return lspCode;
	}

	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}

	@XmlElement(name = "lspAbbr")
	public String getLspAbbr() {
		return lspAbbr;
	}

	public void setLspAbbr(String lspAbbr) {
		this.lspAbbr = lspAbbr;
	}

	@XmlElement(name = "shipDate")
	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	@XmlElement(name = "fromMasLoc")
	public String getFromMasLoc() {
		return fromMasLoc;
	}

	public void setFromMasLoc(String fromMasLoc) {
		this.fromMasLoc = fromMasLoc;
	}

	@XmlElement(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@XmlElement(name = "shippingAddress")
	public AddressVO getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressVO shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@XmlElement(name = "truckInfos")
	public TruckInfoVO getTruckInfos() {
		return truckInfos;
	}

	public void setTruckInfos(TruckInfoVO truckInfos) {
		this.truckInfos = truckInfos;
	}

	@XmlElement(name = "items")
	public ItemVO getItems() {
		return items;
	}

	public void setItems(ItemVO items) {
		this.items = items;
	}

}
