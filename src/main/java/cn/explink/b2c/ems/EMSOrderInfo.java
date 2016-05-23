package cn.explink.b2c.ems;

import java.math.BigDecimal;

/**
 * EMS订单信息
 * @author huan.zhou
 */
public class EMSOrderInfo {
	private String bigAccountDataId;//邮件数据唯一标识
	private String businessType;//业务类型
	private String billno;//邮件号
	private String dateType;//时间类型
	/*private String procdate;//时间*/
	private String scontactor;//寄件人姓名
	private String scustMobile;//寄件人电话1
	private String scustTelplus;//寄件人电话2
	private String scustPost;//寄件人邮编
	private String scustAddr;//寄件人地址
	private String scustComp;//寄件人公司名称
	private String scustProvince;//寄件人所在省
	private String scustCity;//寄件人所在市
	private String scustCounty;//寄件人所在区县
	private String tcontactor;//收件人姓名
	private String tcustMobile;//收件人电话1
	private String tcustTelplus;//收件人电话2
	private String tcustPost;//收件人邮编
	private String tcustAddr;//收件人地址
	private String tcustComp;//收件人公司名称
	private String tcustProvince;//收件人所在省
	private String tcustCity;//收件人所在市
	private String tcustCounty;//收件人所在区县
	private BigDecimal weight;//货物重量
	/*private BigDecimal length;//货物长度
	private BigDecimal insure;//保价金额
	private BigDecimal insurance;//保险金额*/
	private BigDecimal fee;//货款金额
	private String feeUppercase;//大写货款金额
	private String cargoDesc;//内件信息
	private String cargoType;//内件类型
	private String deliveryclaim;//揽投员的投递要求
	private String remark;//备注
	private String productCode;//邮件产品代码
	private String customerDn;//运单号
	/*private Integer subBillCount;//分单数*/
	private String mainBillNo;//主单邮件号
	/*private String mainBillFlag;//主分单标识
	private String mainSubPayMode;//一票多单计费方式
	private String payMode;//付费类型
	private String insureType;//所负责任*/
	private String dshk;//代收货款（附加服务）
	private String blank2;//预留，不实际使用
	private String blank3;//预留，不实际使用
	private String blank4;//预留，不实际使用
	private String blank5;//预留，不实际使用
	public String getBigAccountDataId() {
		return bigAccountDataId;
	}
	public void setBigAccountDataId(String bigAccountDataId) {
		this.bigAccountDataId = bigAccountDataId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getScontactor() {
		return scontactor;
	}
	public void setScontactor(String scontactor) {
		this.scontactor = scontactor;
	}
	public String getScustMobile() {
		return scustMobile;
	}
	public void setScustMobile(String scustMobile) {
		this.scustMobile = scustMobile;
	}
	public String getScustTelplus() {
		return scustTelplus;
	}
	public void setScustTelplus(String scustTelplus) {
		this.scustTelplus = scustTelplus;
	}
	public String getScustPost() {
		return scustPost;
	}
	public void setScustPost(String scustPost) {
		this.scustPost = scustPost;
	}
	public String getScustAddr() {
		return scustAddr;
	}
	public void setScustAddr(String scustAddr) {
		this.scustAddr = scustAddr;
	}
	public String getScustComp() {
		return scustComp;
	}
	public void setScustComp(String scustComp) {
		this.scustComp = scustComp;
	}
	public String getScustProvince() {
		return scustProvince;
	}
	public void setScustProvince(String scustProvince) {
		this.scustProvince = scustProvince;
	}
	public String getScustCity() {
		return scustCity;
	}
	public void setScustCity(String scustCity) {
		this.scustCity = scustCity;
	}
	public String getScustCounty() {
		return scustCounty;
	}
	public void setScustCounty(String scustCounty) {
		this.scustCounty = scustCounty;
	}
	public String getTcontactor() {
		return tcontactor;
	}
	public void setTcontactor(String tcontactor) {
		this.tcontactor = tcontactor;
	}
	public String getTcustMobile() {
		return tcustMobile;
	}
	public void setTcustMobile(String tcustMobile) {
		this.tcustMobile = tcustMobile;
	}
	public String getTcustTelplus() {
		return tcustTelplus;
	}
	public void setTcustTelplus(String tcustTelplus) {
		this.tcustTelplus = tcustTelplus;
	}
	public String getTcustPost() {
		return tcustPost;
	}
	public void setTcustPost(String tcustPost) {
		this.tcustPost = tcustPost;
	}
	public String getTcustAddr() {
		return tcustAddr;
	}
	public void setTcustAddr(String tcustAddr) {
		this.tcustAddr = tcustAddr;
	}
	public String getTcustComp() {
		return tcustComp;
	}
	public void setTcustComp(String tcustComp) {
		this.tcustComp = tcustComp;
	}
	public String getTcustProvince() {
		return tcustProvince;
	}
	public void setTcustProvince(String tcustProvince) {
		this.tcustProvince = tcustProvince;
	}
	public String getTcustCity() {
		return tcustCity;
	}
	public void setTcustCity(String tcustCity) {
		this.tcustCity = tcustCity;
	}
	public String getTcustCounty() {
		return tcustCounty;
	}
	public void setTcustCounty(String tcustCounty) {
		this.tcustCounty = tcustCounty;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getFeeUppercase() {
		return feeUppercase;
	}
	public void setFeeUppercase(String feeUppercase) {
		this.feeUppercase = feeUppercase;
	}
	public String getCargoDesc() {
		return cargoDesc;
	}
	public void setCargoDesc(String cargoDesc) {
		this.cargoDesc = cargoDesc;
	}
	public String getCargoType() {
		return cargoType;
	}
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	public String getDeliveryclaim() {
		return deliveryclaim;
	}
	public void setDeliveryclaim(String deliveryclaim) {
		this.deliveryclaim = deliveryclaim;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getCustomerDn() {
		return customerDn;
	}
	public void setCustomerDn(String customerDn) {
		this.customerDn = customerDn;
	}
	public String getMainBillNo() {
		return mainBillNo;
	}
	public void setMainBillNo(String mainBillNo) {
		this.mainBillNo = mainBillNo;
	}
	public String getDshk() {
		return dshk;
	}
	public void setDshk(String dshk) {
		this.dshk = dshk;
	}
	public String getBlank2() {
		return blank2;
	}
	public void setBlank2(String blank2) {
		this.blank2 = blank2;
	}
	public String getBlank3() {
		return blank3;
	}
	public void setBlank3(String blank3) {
		this.blank3 = blank3;
	}
	public String getBlank4() {
		return blank4;
	}
	public void setBlank4(String blank4) {
		this.blank4 = blank4;
	}
	public String getBlank5() {
		return blank5;
	}
	public void setBlank5(String blank5) {
		this.blank5 = blank5;
	}
	
}
