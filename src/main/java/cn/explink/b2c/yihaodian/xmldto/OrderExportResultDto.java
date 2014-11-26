/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单信息获取（配送单、取件单）(1.01)
 *
 * @since 2011-12
 * @author songzhonghua
 * @version $Id: OrderExportResultDto.java, v 0.1 2012-08-07 15:23:21
 *          songzhonghua Exp $
 */
@XmlRootElement(name = "OrderExportResult")
public class OrderExportResultDto implements Serializable {
	private static final long serialVersionUID = 9047001465999428215L;
	// Property
	// --------------------------------------------------
	/** 返回状态 (errCode) */
	private String errCode = null;

	/** 错误信息 (errMsg) */
	private String errMsg = null;

	/** 订单信息集合 (orderHeaderList) */
	private List<OrderExportHeaderDto> orderHeaderList = null;

	// Accessors
	// --------------------------------------------------

	/**
	 * 返回状态 GET
	 *
	 * @return errCode 返回状态
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * 返回状态 SET
	 *
	 * @param errCode
	 *            返回状态
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	/**
	 * 错误信息 GET
	 *
	 * @return errMsg 错误信息
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 错误信息 SET
	 *
	 * @param errMsg
	 *            错误信息
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 订单信息集合 GET
	 *
	 * @return orderHeaderList 订单信息集合
	 */
	@XmlElement(name = "orderHeader")
	public List<OrderExportHeaderDto> getOrderHeaderList() {
		return orderHeaderList;
	}

	/**
	 * 订单信息集合 SET
	 *
	 * @param orderHeaderList
	 *            订单信息集合
	 */
	public void setOrderHeaderList(List<OrderExportHeaderDto> orderHeaderList) {
		this.orderHeaderList = orderHeaderList;
	}

}
