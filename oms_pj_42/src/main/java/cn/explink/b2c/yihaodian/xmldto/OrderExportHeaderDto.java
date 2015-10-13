/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单信息获取（配送单、取件单）header(1.01-h)
 *
 * @since 2011-12
 * @author songzhonghua
 * @version $Id: OrderExportHeaderDto.java, v 0.1 2012-08-07 15:23:21
 *          songzhonghua Exp $
 */
@XmlRootElement
public class OrderExportHeaderDto implements Serializable {
	private static final long serialVersionUID = -9071269456162320375L;
	// Property
	// --------------------------------------------------
	/** 发货地 (fromLocationName) */
	private String fromLocationName = null;

	/** 退货目的地 (toLocationName) */
	private String toLocationName = null;

	/** 订单号 (orderCode) */
	private String orderCode = null;

	/** 运单号 (shipmentCode) */
	private String shipmentCode = null;

	/** 第3方物流运单号 (carrierShipmentCode) */
	private String carrierShipmentCode = null;

	/** 单据类型 (orderType) */
	private String orderType = null;

	/** 应收款 (toCollectAmount) */
	private BigDecimal toCollectAmount = null;

	/** 应退款 (collectedAmount) */
	private BigDecimal collectedAmount = null;

	/** 送货物品金额 (deliveryProductAmount) */
	private BigDecimal deliveryProductAmount = null;

	/** 退货物品金额 (returnProductAmount) */
	private BigDecimal returnProductAmount = null;

	/** 重量(kg) (weight) */
	private BigDecimal weight = null;

	/** 箱数 (cartonQuantity) */
	private Long cartonQuantity = null;

	/** 姓名 (consignee) */
	private String consignee = null;

	/** 电话 (consigneeTelephone) */
	private String consigneeTelephone = null;

	/** 地址 (consigneeAddress) */
	private String consigneeAddress = null;

	/** 邮编 (consigneeZipcode) */
	private String consigneeZipcode = null;

	/** 配送方式 (deliveryMode) */
	private String deliveryMode = null;

	/** 送货要求 (expectedDeliveryInfo) */
	private String expectedDeliveryInfo = null;

	/** 订单明细信息集合 (orderDetailList) */
	private List<OrderExportDetailDto> orderDetailList = null;

	/** 订单包箱信息集合 (cartonList) */
	private List<OrderExportCartonDto> cartonList = null;

	// Accessors
	// --------------------------------------------------

	/**
	 * 发货地 GET
	 *
	 * @return fromLocationName 发货地
	 */
	public String getFromLocationName() {
		return fromLocationName;
	}

	/**
	 * 发货地 SET
	 *
	 * @param fromLocationName
	 *            发货地
	 */
	public void setFromLocationName(String fromLocationName) {
		this.fromLocationName = fromLocationName;
	}

	/**
	 * 退货目的地 GET
	 *
	 * @return toLocationName 退货目的地
	 */
	public String getToLocationName() {
		return toLocationName;
	}

	/**
	 * 退货目的地 SET
	 *
	 * @param toLocationName
	 *            退货目的地
	 */
	public void setToLocationName(String toLocationName) {
		this.toLocationName = toLocationName;
	}

	/**
	 * 订单号 GET
	 *
	 * @return orderCode 订单号
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * 订单号 SET
	 *
	 * @param orderCode
	 *            订单号
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * 运单号 GET
	 *
	 * @return shipmentCode 运单号
	 */
	public String getShipmentCode() {
		return shipmentCode;
	}

	/**
	 * 运单号 SET
	 *
	 * @param shipmentCode
	 *            运单号
	 */
	public void setShipmentCode(String shipmentCode) {
		this.shipmentCode = shipmentCode;
	}

	/**
	 * 第3方物流运单号 GET
	 *
	 * @return carrierShipmentCode 第3方物流运单号
	 */
	public String getCarrierShipmentCode() {
		return carrierShipmentCode;
	}

	/**
	 * 第3方物流运单号 SET
	 *
	 * @param carrierShipmentCode
	 *            第3方物流运单号
	 */
	public void setCarrierShipmentCode(String carrierShipmentCode) {
		this.carrierShipmentCode = carrierShipmentCode;
	}

	/**
	 * 单据类型 GET
	 *
	 * @return orderType 单据类型
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * 单据类型 SET
	 *
	 * @param orderType
	 *            单据类型
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * 应收款 GET
	 *
	 * @return toCollectAmount 应收款
	 */
	public BigDecimal getToCollectAmount() {
		return toCollectAmount;
	}

	/**
	 * 应收款 SET
	 *
	 * @param toCollectAmount
	 *            应收款
	 */
	public void setToCollectAmount(BigDecimal toCollectAmount) {
		this.toCollectAmount = toCollectAmount;
	}

	/**
	 * 应退款 GET
	 *
	 * @return collectedAmount 应退款
	 */
	public BigDecimal getCollectedAmount() {
		return collectedAmount;
	}

	/**
	 * 应退款 SET
	 *
	 * @param collectedAmount
	 *            应退款
	 */
	public void setCollectedAmount(BigDecimal collectedAmount) {
		this.collectedAmount = collectedAmount;
	}

	/**
	 * 送货物品金额 GET
	 *
	 * @return deliveryProductAmount 送货物品金额
	 */
	public BigDecimal getDeliveryProductAmount() {
		return deliveryProductAmount;
	}

	/**
	 * 送货物品金额 SET
	 *
	 * @param deliveryProductAmount
	 *            送货物品金额
	 */
	public void setDeliveryProductAmount(BigDecimal deliveryProductAmount) {
		this.deliveryProductAmount = deliveryProductAmount;
	}

	/**
	 * 退货物品金额 GET
	 *
	 * @return returnProductAmount 退货物品金额
	 */
	public BigDecimal getReturnProductAmount() {
		return returnProductAmount;
	}

	/**
	 * 退货物品金额 SET
	 *
	 * @param returnProductAmount
	 *            退货物品金额
	 */
	public void setReturnProductAmount(BigDecimal returnProductAmount) {
		this.returnProductAmount = returnProductAmount;
	}

	/**
	 * 重量(kg) GET
	 *
	 * @return weight 重量(kg)
	 */
	public BigDecimal getWeight() {
		return weight;
	}

	/**
	 * 重量(kg) SET
	 *
	 * @param weight
	 *            重量(kg)
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	/**
	 * 箱数 GET
	 *
	 * @return cartonQuantity 箱数
	 */
	public Long getCartonQuantity() {
		return cartonQuantity;
	}

	/**
	 * 箱数 SET
	 *
	 * @param cartonQuantity
	 *            箱数
	 */
	public void setCartonQuantity(Long cartonQuantity) {
		this.cartonQuantity = cartonQuantity;
	}

	/**
	 * 姓名 GET
	 *
	 * @return consignee 姓名
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 姓名 SET
	 *
	 * @param consignee
	 *            姓名
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 电话 GET
	 *
	 * @return consigneeTelephone 电话
	 */
	public String getConsigneeTelephone() {
		return consigneeTelephone;
	}

	/**
	 * 电话 SET
	 *
	 * @param consigneeTelephone
	 *            电话
	 */
	public void setConsigneeTelephone(String consigneeTelephone) {
		this.consigneeTelephone = consigneeTelephone;
	}

	/**
	 * 地址 GET
	 *
	 * @return consigneeAddress 地址
	 */
	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	/**
	 * 地址 SET
	 *
	 * @param consigneeAddress
	 *            地址
	 */
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	/**
	 * 邮编 GET
	 *
	 * @return consigneeZipcode 邮编
	 */
	public String getConsigneeZipcode() {
		return consigneeZipcode;
	}

	/**
	 * 邮编 SET
	 *
	 * @param consigneeZipcode
	 *            邮编
	 */
	public void setConsigneeZipcode(String consigneeZipcode) {
		this.consigneeZipcode = consigneeZipcode;
	}

	/**
	 * 配送方式 GET
	 *
	 * @return deliveryMode 配送方式
	 */
	public String getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * 配送方式 SET
	 *
	 * @param deliveryMode
	 *            配送方式
	 */
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	/**
	 * 送货要求 GET
	 *
	 * @return expectedDeliveryInfo 送货要求
	 */
	public String getExpectedDeliveryInfo() {
		return expectedDeliveryInfo;
	}

	/**
	 * 送货要求 SET
	 *
	 * @param expectedDeliveryInfo
	 *            送货要求
	 */
	public void setExpectedDeliveryInfo(String expectedDeliveryInfo) {
		this.expectedDeliveryInfo = expectedDeliveryInfo;
	}

	/**
	 * 订单明细信息集合 GET
	 *
	 * @return orderDetailList 订单明细信息集合
	 */
	@XmlElement(name = "orderDetail")
	public List<OrderExportDetailDto> getOrderDetailList() {
		return orderDetailList;
	}

	/**
	 * 订单明细信息集合 SET
	 *
	 * @param orderDetailList
	 *            订单明细信息集合
	 */
	public void setOrderDetailList(List<OrderExportDetailDto> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	/**
	 * 订单包箱信息集合 GET
	 *
	 * @return cartonList 订单包箱信息集合
	 */
	@XmlElement(name = "carton")
	public List<OrderExportCartonDto> getCartonList() {
		return cartonList;
	}

	/**
	 * 订单包箱信息集合 SET
	 *
	 * @param cartonList
	 *            订单包箱信息集合
	 */
	public void setCartonList(List<OrderExportCartonDto> cartonList) {
		this.cartonList = cartonList;
	}

}
