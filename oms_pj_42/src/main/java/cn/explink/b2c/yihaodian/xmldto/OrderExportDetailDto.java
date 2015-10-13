/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单信息获取（配送单、取件单）detail(1.01-d)
 *
 * @since 2011-12
 * @author songzhonghua
 * @version $Id: OrderExportDetailDto.java, v 0.1 2012-08-07 15:23:21
 *          songzhonghua Exp $
 */
@XmlRootElement
public class OrderExportDetailDto implements Serializable {
	private static final long serialVersionUID = 4596593069836616714L;
	// Property
	// --------------------------------------------------
	/** 商品代码 (goodsCode) */
	private String goodsCode = null;

	/** 商品名称 (goodsName) */
	private String goodsName = null;

	/** 商品描述 (goodsDesc) */
	private String goodsDesc = null;

	/** 数量 (quantity) */
	private Long quantity = null;

	/** 单位 (uom) */
	private String uom = null;

	/** 货值 (amount) */
	private BigDecimal amount = null;

	/** 退货原因分类 (returnReason) */
	private Integer returnReason = null;

	/** 退货原因描述 (returnReasonDesc) */
	private String returnReasonDesc = null;

	/** 备注 (remark) */
	private String remark = null;

	// Accessors
	// --------------------------------------------------

	/**
	 * 商品代码 GET
	 *
	 * @return goodsCode 商品代码
	 */
	public String getGoodsCode() {
		return goodsCode;
	}

	/**
	 * 商品代码 SET
	 *
	 * @param goodsCode
	 *            商品代码
	 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	/**
	 * 商品名称 GET
	 *
	 * @return goodsName 商品名称
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * 商品名称 SET
	 *
	 * @param goodsName
	 *            商品名称
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * 商品描述 GET
	 *
	 * @return goodsDesc 商品描述
	 */
	public String getGoodsDesc() {
		return goodsDesc;
	}

	/**
	 * 商品描述 SET
	 *
	 * @param goodsDesc
	 *            商品描述
	 */
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
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
	 * 单位 GET
	 *
	 * @return uom 单位
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * 单位 SET
	 *
	 * @param uom
	 *            单位
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}

	/**
	 * 货值 GET
	 *
	 * @return amount 货值
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 货值 SET
	 *
	 * @param amount
	 *            货值
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 退货原因分类 GET
	 *
	 * @return returnReason 退货原因分类
	 */
	public Integer getReturnReason() {
		return returnReason;
	}

	/**
	 * 退货原因分类 SET
	 *
	 * @param returnReason
	 *            退货原因分类
	 */
	public void setReturnReason(Integer returnReason) {
		this.returnReason = returnReason;
	}

	/**
	 * 退货原因描述 GET
	 *
	 * @return returnReasonDesc 退货原因描述
	 */
	public String getReturnReasonDesc() {
		return returnReasonDesc;
	}

	/**
	 * 退货原因描述 SET
	 *
	 * @param returnReasonDesc
	 *            退货原因描述
	 */
	public void setReturnReasonDesc(String returnReasonDesc) {
		this.returnReasonDesc = returnReasonDesc;
	}

	/**
	 * 备注 GET
	 *
	 * @return remark 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 备注 SET
	 *
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
