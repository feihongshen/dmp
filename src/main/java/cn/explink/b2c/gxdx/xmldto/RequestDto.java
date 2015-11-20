/**
 * 
 */
package cn.explink.b2c.gxdx.xmldto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: RequestDto
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月12日上午11:08:57
 */
@XmlRootElement(name = "RequestWorkOrder")
public class RequestDto {

	private String waybillNo;// 运单号或者取货指令号
	private String clientCode;// 客户单号
	private String logisticProviderID;// 物流公司编号
	private int holiday;// 是否节假日送货0送，1不送
	private BigDecimal replCost;// 代收款,没有写0，负数表示应退金额，正数表示应收金额
	private String trustPerson;// 发件人
	private String trustUnit;// 委托单位
	private String trustZipCode;// 委托邮编
	private String trustCity;// 发件城市
	private String trustAddress;// 发件详细地址
	private String trustMobile;// 发件人手机
	private String trustTel;// 发件人座机
	private String getPerson;// 收货人
	private String getUnit;// 收货单位
	private String getZipCode;// 收货邮编
	private String getCity;// 收货城市
	private String getAddress;// 收货地址
	private String getTel;// 收货人座机
	private String getMobile;// 收货人手机
	private BigDecimal goodsValue;// 货物金额
	private String workType;// T0:普通T1：换货,T2:取货
	private GoodsInfoList goodsInfo;
	private int goodsNum;// 总件数
	private BigDecimal goodsHav;// 总重量(KG)

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

	@XmlElement(name = "LogisticProviderID")
	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	@XmlElement(name = "Holiday")
	public int getHoliday() {
		return holiday;
	}

	public void setHoliday(int holiday) {
		this.holiday = holiday;
	}

	@XmlElement(name = "ReplCost")
	public BigDecimal getReplCost() {
		return replCost;
	}

	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
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

	@XmlElement(name = "GoodsValue")
	public BigDecimal getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(BigDecimal goodsValue) {
		this.goodsValue = goodsValue;
	}

	@XmlElement(name = "WorkType")
	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	@XmlElement(name = "GoodsInfo")
	public GoodsInfoList getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsInfoList goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	@XmlElement(name = "GoodsNum")
	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	@XmlElement(name = "GoodsHav")
	public BigDecimal getGoodsHav() {
		return goodsHav;
	}

	public void setGoodsHav(BigDecimal goodsHav) {
		this.goodsHav = goodsHav;
	}

}
