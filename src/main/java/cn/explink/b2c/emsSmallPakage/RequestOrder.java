package cn.explink.b2c.emsSmallPakage;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/*
 * 邮政小包订单下发信息
 * add by zhouhuan 
 * add time:2016-07-20
 */
@XmlRootElement(name = "RequestOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestOrder {
	@XmlElement(name = "ecCompanyId")
	private String ecCompanyId;//电商标识
	@XmlElement(name = "logisticProviderID")
	private String logisticProviderID;//物流公司ID
	@XmlElement(name = "customerId")
	private String customerId;//客户标识
	@XmlElement(name = "txLogisticID")
	private String txLogisticID;//物流订单号
	@XmlElement(name = "tradeNo")
	private String tradeNo;//业务交易号
	@XmlElement(name = "mailNo")
	private String mailNo;//物流运单号
	@XmlElement(name = "totalServiceFee")
	private long totalServiceFee;//总服务费[COD]
	@XmlElement(name = "codSplitFee")
	private long codSplitFee;//物流公司分润[COD] ：（单位：分）
	@XmlElement(name = "buyServiceFee")
	private long buyServiceFee;//买家服务费[COD]
	@XmlElement(name = "orderType")
	private int orderType;//订单类型
	@XmlElement(name = "serviceType")
	private long serviceType;//服务类型
	@XmlElement(name = "sender")
	private UserInfo sender;//寄件人信息
	@XmlElement(name = "receiver")
	private UserInfo receiver;//收件人信息
	@XmlElement(name = "sendStartTime")
	private String sendStartTime;//物流公司上门取货时间段
	@XmlElement(name = "sendEndTime")
	private String sendEndTime;//物流公司上门取货时间段
	@XmlElement(name = "goodsValue")
	private long goodsValue;//商品金额，包括优惠和运费，但无服务费
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<Item> items;//商品信息
	@XmlElement(name = "special")
	private int special;//商品类型
	@XmlElement(name = "remark")
	private String remark;//备注
	
	public String getEcCompanyId() {
		return ecCompanyId;
	}
	public void setEcCompanyId(String ecCompanyId) {
		this.ecCompanyId = ecCompanyId;
	}
	public String getLogisticProviderID() {
		return logisticProviderID;
	}
	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTxLogisticID() {
		return txLogisticID;
	}
	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public long getTotalServiceFee() {
		return totalServiceFee;
	}
	public void setTotalServiceFee(long totalServiceFee) {
		this.totalServiceFee = totalServiceFee;
	}
	public long getCodSplitFee() {
		return codSplitFee;
	}
	public void setCodSplitFee(long codSplitFee) {
		this.codSplitFee = codSplitFee;
	}
	public long getBuyServiceFee() {
		return buyServiceFee;
	}
	public void setBuyServiceFee(long buyServiceFee) {
		this.buyServiceFee = buyServiceFee;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public long getServiceType() {
		return serviceType;
	}
	public void setServiceType(long serviceType) {
		this.serviceType = serviceType;
	}
	public UserInfo getSender() {
		return sender;
	}
	public void setSender(UserInfo sender) {
		this.sender = sender;
	}
	public UserInfo getReceiver() {
		return receiver;
	}
	public void setReceiver(UserInfo receiver) {
		this.receiver = receiver;
	}
	public String getSendStartTime() {
		return sendStartTime;
	}
	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}
	public String getSendEndTime() {
		return sendEndTime;
	}
	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	public long getGoodsValue() {
		return goodsValue;
	}
	public void setGoodsValue(long goodsValue) {
		this.goodsValue = goodsValue;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
