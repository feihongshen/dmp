package cn.explink.b2c.smile;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "RequestOrder")
public class SmileOrder {
	private String waybillNo; // 运单号 cwb
	private String clientCode; // 客户单号
	private String holiday; // 是否节假日送货0送，1不送
	private BigDecimal replCost; // 代收款,没有写0
	private int stmtForm; // 结算方式0月结、1现结
	private String trustClientCode; // 委托客户编号
	private String trustPerson; // 发件人
	private String trustUnit; // 委托单位
	private String trustZipCode; // 委托邮编
	private String trustCity; // 发件城市（精确到区，例如：“北京市北京市朝阳区”，“广东省广州市白云区”）
	private String trustAddress; // 发件详细地址
	private String trustMobile; // 发件人手机
	private String trustTel; // 发件人座机
	private String getPerson; // 收货人
	private String getUnit; // 收货单位
	private String getZipCode; // 收货邮编
	private String getCity; // 收货城市（精确到区，例如：“北京市北京市朝阳区”，“广东省广州市白云区”）
	private String getAddress; // 收货地址
	private String getTel; // 收货人座机
	private String getMobile; // 收货人手机
	private String insForm; // 保险类型
	private String insureValue; // 投保价值
	private int workType; // 0:正常，1：换货运单，2：退货单（拒收） 订单类型
	private String goodsNum; // 总件数
	private String goodsHav; // 总重量(KG)
	private String orderType; // 订单类型

	private GoodsInfo goodsInfo;// 商品信息列表

	@XmlElement(name = "GoodsInfo")
	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	@XmlElement(name = "WaybillNo")
	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	@XmlElement(name = "ClientCode")
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	@XmlElement(name = "Holiday")
	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	@XmlElement(name = "ReplCost")
	public BigDecimal getReplCost() {
		return replCost;
	}

	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
	}

	@XmlElement(name = "StmtForm")
	public int getStmtForm() {
		return stmtForm;
	}

	public void setStmtForm(int stmtForm) {
		this.stmtForm = stmtForm;
	}

	@XmlElement(name = "TrustClientCode")
	public String getTrustClientCode() {
		return trustClientCode;
	}

	public void setTrustClientCode(String trustClientCode) {
		this.trustClientCode = trustClientCode;
	}

	@XmlElement(name = "TrustPerson")
	public String getTrustPerson() {
		return trustPerson;
	}

	public void setTrustPerson(String trustPerson) {
		this.trustPerson = trustPerson;
	}

	@XmlElement(name = "TrustUnit")
	public String getTrustUnit() {
		return trustUnit;
	}

	public void setTrustUnit(String trustUnit) {
		this.trustUnit = trustUnit;
	}

	@XmlElement(name = "TrustZipCode")
	public String getTrustZipCode() {
		return trustZipCode;
	}

	public void setTrustZipCode(String trustZipCode) {
		this.trustZipCode = trustZipCode;
	}

	@XmlElement(name = "TrustCity")
	public String getTrustCity() {
		return trustCity;
	}

	public void setTrustCity(String trustCity) {
		this.trustCity = trustCity;
	}

	@XmlElement(name = "TrustAddress")
	public String getTrustAddress() {
		return trustAddress;
	}

	public void setTrustAddress(String trustAddress) {
		this.trustAddress = trustAddress;
	}

	@XmlElement(name = "TrustMobile")
	public String getTrustMobile() {
		return trustMobile;
	}

	public void setTrustMobile(String trustMobile) {
		this.trustMobile = trustMobile;
	}

	@XmlElement(name = "TrustTel")
	public String getTrustTel() {
		return trustTel;
	}

	public void setTrustTel(String trustTel) {
		this.trustTel = trustTel;
	}

	@XmlElement(name = "GetPerson")
	public String getGetPerson() {
		return getPerson;
	}

	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}

	@XmlElement(name = "GetUnit")
	public String getGetUnit() {
		return getUnit;
	}

	public void setGetUnit(String getUnit) {
		this.getUnit = getUnit;
	}

	@XmlElement(name = "GetZipCode")
	public String getGetZipCode() {
		return getZipCode;
	}

	public void setGetZipCode(String getZipCode) {
		this.getZipCode = getZipCode;
	}

	@XmlElement(name = "GetCity")
	public String getGetCity() {
		return getCity;
	}

	public void setGetCity(String getCity) {
		this.getCity = getCity;
	}

	@XmlElement(name = "GetAddress")
	public String getGetAddress() {
		return getAddress;
	}

	public void setGetAddress(String getAddress) {
		this.getAddress = getAddress;
	}

	@XmlElement(name = "GetTel")
	public String getGetTel() {
		return getTel;
	}

	public void setGetTel(String getTel) {
		this.getTel = getTel;
	}

	@XmlElement(name = "GetMobile")
	public String getGetMobile() {
		return getMobile;
	}

	public void setGetMobile(String getMobile) {
		this.getMobile = getMobile;
	}

	@XmlElement(name = "InsForm")
	public String getInsForm() {
		return insForm;
	}

	public void setInsForm(String insForm) {
		this.insForm = insForm;
	}

	@XmlElement(name = "InsureValue")
	public String getInsureValue() {
		return insureValue;
	}

	public void setInsureValue(String insureValue) {
		this.insureValue = insureValue;
	}

	@XmlElement(name = "WorkType")
	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	@XmlElement(name = "GoodsNum")
	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	@XmlElement(name = "GoodsHav")
	public String getGoodsHav() {
		return goodsHav;
	}

	public void setGoodsHav(String goodsHav) {
		this.goodsHav = goodsHav;
	}

	@XmlElement(name = "OrderType")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}
