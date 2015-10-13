/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单信息获取（配送单、取件单）carton(1.01-c)
 *
 * @since 2011-12
 * @author songzhonghua
 * @version $Id: OrderExportCartonDto.java, v 0.1 2012-08-07 15:29:7
 *          songzhonghua Exp $
 */
@XmlRootElement
public class OrderExportCartonDto implements Serializable {
	private static final long serialVersionUID = -6544864410289749705L;
	// Property
	// --------------------------------------------------

	/** 箱号 (code) */
	private String code = null;

	/** 长(cm) (length) */
	private Long length = null;

	/** 宽(cm) (wiidth) */
	private Long wiidth = null;

	/** 高(cm) (height) */
	private Long height = null;

	/** 数量 (quantity) */
	private Long quantity = null;

	/** 重量(kg) (weight) */
	private BigDecimal weight = null;

	/** 体积(立方厘米) (volume) */
	private BigDecimal volume = null;

	/** 3PL运单号 (carrierShipmentCode) */
	private String carrierShipmentCode = null;

	// Accessors
	// --------------------------------------------------

	/**
	 * 箱号 GET
	 *
	 * @return code 箱号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 箱号 SET
	 *
	 * @param code
	 *            箱号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 长(cm) GET
	 *
	 * @return length 长(cm)
	 */
	public Long getLength() {
		return length;
	}

	/**
	 * 长(cm) SET
	 *
	 * @param length
	 *            长(cm)
	 */
	public void setLength(Long length) {
		this.length = length;
	}

	/**
	 * 宽(cm) GET
	 *
	 * @return wiidth 宽(cm)
	 */
	public Long getWiidth() {
		return wiidth;
	}

	/**
	 * 宽(cm) SET
	 *
	 * @param wiidth
	 *            宽(cm)
	 */
	public void setWiidth(Long wiidth) {
		this.wiidth = wiidth;
	}

	/**
	 * 高(cm) GET
	 *
	 * @return height 高(cm)
	 */
	public Long getHeight() {
		return height;
	}

	/**
	 * 高(cm) SET
	 *
	 * @param height
	 *            高(cm)
	 */
	public void setHeight(Long height) {
		this.height = height;
	}

	/**
	 * 数量 GET
	 *
	 * @return quantity 数量
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * 数量 SET
	 *
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
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
	 * 体积(立方厘米) GET
	 *
	 * @return volume 体积(立方厘米)
	 */
	public BigDecimal getVolume() {
		return volume;
	}

	/**
	 * 体积(立方厘米) SET
	 *
	 * @param volume
	 *            体积(立方厘米)
	 */
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	/**
	 * 3PL运单号 GET
	 *
	 * @return carrierShipmentCode 3PL运单号
	 */
	public String getCarrierShipmentCode() {
		return carrierShipmentCode;
	}

	/**
	 * 3PL运单号 SET
	 *
	 * @param carrierShipmentCode
	 *            3PL运单号
	 */
	public void setCarrierShipmentCode(String carrierShipmentCode) {
		this.carrierShipmentCode = carrierShipmentCode;
	}

}
