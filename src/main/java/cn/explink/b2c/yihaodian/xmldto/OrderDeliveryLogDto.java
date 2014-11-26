/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 配送日志反馈到TMS（配送单、取件单）(1.03)
 *
 * @since 2011-12
 * @author songzhonghua
 * @version $Id: OrderDeliveryLogDto.java, v 0.1 2012-08-07 15:23:21
 *          songzhonghua Exp $
 */
@XmlRootElement(name = "OrderDeliveryLog")
public class OrderDeliveryLogDto implements Serializable {
	private static final long serialVersionUID = -5445134913999461709L;
	// Property
	// --------------------------------------------------
	/** 用户代码 (userCode) */
	private String userCode = null;

	/** 请求时间 (requestTime) */
	private String requestTime = null;

	/** 签名（md5） (sign) */
	private String sign = null;

	/** 运单号 (shipmentCode) */
	private String shipmentCode = null;

	/** 业务操作枚举值 (operationTypeEnumValue) */
	private Integer operationTypeEnumValue = null;

	/** 业务操作类型 (operationType) */
	private String operationType = null;

	/** 执行时间 (operationTime) */
	private String operationTime = null;
	private Date operationDateTime = null;

	/** 操作人 (operator) */
	private String operator = null;

	/** 备注 (remark) */
	private String remark = null;

	// Accessors
	// --------------------------------------------------

	/**
	 * 用户代码 GET
	 *
	 * @return userCode 用户代码
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * 用户代码 SET
	 *
	 * @param userCode
	 *            用户代码
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 请求时间 GET
	 *
	 * @return requestTime 请求时间
	 */
	public String getRequestTime() {
		return requestTime;
	}

	/**
	 * 请求时间 SET
	 *
	 * @param requestTime
	 *            请求时间
	 */
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * 签名（md5） GET
	 *
	 * @return sign 签名（md5）
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 签名（md5） SET
	 *
	 * @param sign
	 *            签名（md5）
	 */
	public void setSign(String sign) {
		this.sign = sign;
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
	 * 业务操作枚举值 GET
	 *
	 * @return operationTypeEnumValue 业务操作枚举值
	 */
	public Integer getOperationTypeEnumValue() {
		return operationTypeEnumValue;
	}

	/**
	 * 业务操作枚举值 SET
	 *
	 * @param operationTypeEnumValue
	 *            业务操作枚举值
	 */
	public void setOperationTypeEnumValue(Integer operationTypeEnumValue) {
		this.operationTypeEnumValue = operationTypeEnumValue;
	}

	/**
	 * 业务操作类型 GET
	 *
	 * @return operationType 业务操作类型
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * 业务操作类型 SET
	 *
	 * @param operationType
	 *            业务操作类型
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * 执行时间 GET
	 *
	 * @return operationTime 执行时间
	 */
	public String getOperationTime() {
		return operationTime;
	}

	/**
	 * 执行时间 SET
	 *
	 * @param operationTime
	 *            执行时间
	 */
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	/**
	 * 操作人 GET
	 *
	 * @return operator 操作人
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 操作人 SET
	 *
	 * @param operator
	 *            操作人
	 */
	public void setOperator(String operator) {
		this.operator = operator;
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

	public Date getOperationDateTime() {
		return operationDateTime;
	}

	public void setOperationDateTime(Date operationDateTime) {
		this.operationDateTime = operationDateTime;
	}

}
