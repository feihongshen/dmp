package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 揽件入站的界面字段表格
 * 
 * @author jiangyu 2015年7月31日
 *
 */
public class ExpressIntoStationVO {

	private Integer id;
	/**
	 * 运单号
	 */
	private String transNo;
	/**
	 * 订单件数
	 */
	private Long orderCount;
	/**
	 * 小件员
	 */
	private String deliveryMan;
	/**
	 * 揽件入站时间
	 */
	private String pickTime;
	/**
	 * 支付类型
	 */
	private String payType;
	/**
	 * 运费合计
	 */
	private BigDecimal transportFeeTotal;
	/**
	 * 运费
	 */
	private BigDecimal transportFee;
	/**
	 * 包装费
	 */
	private BigDecimal packFee;
	/**
	 * 保价费
	 */
	private BigDecimal saveFee;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 市区
	 */
	private String city;
	/**
	 * 处理状态
	 */
	private String processState;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public String getDeliveryMan() {
		return deliveryMan;
	}

	public void setDeliveryMan(String deliveryMan) {
		this.deliveryMan = deliveryMan;
	}

	public String getPickTime() {
		return pickTime;
	}

	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getTransportFeeTotal() {
		return transportFeeTotal;
	}

	public void setTransportFeeTotal(BigDecimal transportFeeTotal) {
		this.transportFeeTotal = transportFeeTotal;
	}

	public BigDecimal getTransportFee() {
		return transportFee;
	}

	public void setTransportFee(BigDecimal transportFee) {
		this.transportFee = transportFee;
	}

	public BigDecimal getPackFee() {
		return packFee;
	}

	public void setPackFee(BigDecimal packFee) {
		this.packFee = packFee;
	}

	public BigDecimal getSaveFee() {
		return saveFee;
	}

	public void setSaveFee(BigDecimal saveFee) {
		this.saveFee = saveFee;
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

	public ExpressIntoStationVO() {
		// TODO Auto-generated constructor stub
	}

	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}
}
