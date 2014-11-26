package cn.explink.b2c.gome.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderVo")
public class OrderVO {
	private String businessCode;// 业务代码
	private String buid;// 渠道代码
	private String orderNumber;// 订单号
	private String lspCode;// 物流公司编码
	private String lspAbbr;// 物流公司代码
	private String transactionDate;// 讯息生成时间
	private String transactionId;// 讯息交易id
	private String orderDate;// 订单日期
	private String originalOrderNum;// 原始订单号
	private String shippingType;// 发货类型
	private String category;// 货品类别
	private String fromMasLoc;// 发货仓库
	private String exDate;// 出库日期
	private String shipperCode;// 委托客户编号
	private String shipCity;// 目的城市
	private String receiptReturnFlag;// 签收是否反馈
	private String receiptReturnType;// 签单返回要求
	private String insuranceFlag;// 是否需要保险
	private String insuranceFee;// 保险费
	private String sellingPrice;// 商品价值
	private String orderType;// 订单类型
	private String inspectionProcedure;// 交付查验处理方式
	private String bestDelievryTime;// 送达时限
	private String paymentTerm;// 付款类别
	private String paymentType;// 付款方式
	private String totalDualAmount;// COD金额
	private String freightCollectFlag;// 运费到付标识
	private String identifyingCode;// 电子签收码
	private String telephoneFlag;// 送货前是否打电话
	private String attachment1;// 附件1
	private String attachment2;// 附件2
	private String comments;// 备注
	private AddressVO shippingAddress;// AddressVO
	private DetailVO details;// List<DetailVO> details
	private FddVO fdd;// FddVO

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

	@XmlElement(name = "orderNumber")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	@XmlElement(name = "transactionDate")
	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	@XmlElement(name = "transactionId")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@XmlElement(name = "orderDate")
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	@XmlElement(name = "originalOrderNum")
	public String getOriginalOrderNum() {
		return originalOrderNum;
	}

	public void setOriginalOrderNum(String originalOrderNum) {
		this.originalOrderNum = originalOrderNum;
	}

	@XmlElement(name = "shippingType")
	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	@XmlElement(name = "category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement(name = "fromMasLoc")
	public String getFromMasLoc() {
		return fromMasLoc;
	}

	public void setFromMasLoc(String fromMasLoc) {
		this.fromMasLoc = fromMasLoc;
	}

	@XmlElement(name = "exDate")
	public String getExDate() {
		return exDate;
	}

	public void setExDate(String exDate) {
		this.exDate = exDate;
	}

	@XmlElement(name = "shipperCode")
	public String getShipperCode() {
		return shipperCode;
	}

	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}

	@XmlElement(name = "shipCity")
	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	@XmlElement(name = "receiptReturnFlag")
	public String getReceiptReturnFlag() {
		return receiptReturnFlag;
	}

	public void setReceiptReturnFlag(String receiptReturnFlag) {
		this.receiptReturnFlag = receiptReturnFlag;
	}

	@XmlElement(name = "receiptReturnType")
	public String getReceiptReturnType() {
		return receiptReturnType;
	}

	public void setReceiptReturnType(String receiptReturnType) {
		this.receiptReturnType = receiptReturnType;
	}

	@XmlElement(name = "insuranceFlag")
	public String getInsuranceFlag() {
		return insuranceFlag;
	}

	public void setInsuranceFlag(String insuranceFlag) {
		this.insuranceFlag = insuranceFlag;
	}

	@XmlElement(name = "insuranceFee")
	public String getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	@XmlElement(name = "sellingPrice")
	public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	@XmlElement(name = "orderType")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@XmlElement(name = "inspectionProcedure")
	public String getInspectionProcedure() {
		return inspectionProcedure;
	}

	public void setInspectionProcedure(String inspectionProcedure) {
		this.inspectionProcedure = inspectionProcedure;
	}

	@XmlElement(name = "bestDelievryTime")
	public String getBestDelievryTime() {
		return bestDelievryTime;
	}

	public void setBestDelievryTime(String bestDelievryTime) {
		this.bestDelievryTime = bestDelievryTime;
	}

	@XmlElement(name = "paymentTerm")
	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	@XmlElement(name = "paymentType")
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	@XmlElement(name = "totalDualAmount")
	public String getTotalDualAmount() {
		return totalDualAmount;
	}

	public void setTotalDualAmount(String totalDualAmount) {
		this.totalDualAmount = totalDualAmount;
	}

	@XmlElement(name = "freightCollectFlag")
	public String getFreightCollectFlag() {
		return freightCollectFlag;
	}

	public void setFreightCollectFlag(String freightCollectFlag) {
		this.freightCollectFlag = freightCollectFlag;
	}

	@XmlElement(name = "identifyingCode")
	public String getIdentifyingCode() {
		return identifyingCode;
	}

	public void setIdentifyingCode(String identifyingCode) {
		this.identifyingCode = identifyingCode;
	}

	@XmlElement(name = "telephoneFlag")
	public String getTelephoneFlag() {
		return telephoneFlag;
	}

	public void setTelephoneFlag(String telephoneFlag) {
		this.telephoneFlag = telephoneFlag;
	}

	@XmlElement(name = "attachment1")
	public String getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}

	@XmlElement(name = "attachment2")
	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
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

	@XmlElement(name = "details")
	public DetailVO getDetails() {
		return details;
	}

	public void setDetails(DetailVO details) {
		this.details = details;
	}

	@XmlElement(name = "fdd")
	public FddVO getFdd() {
		return fdd;
	}

	public void setFdd(FddVO fdd) {
		this.fdd = fdd;
	}

}
