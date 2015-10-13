/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.explink.xmldto;

import java.io.Serializable;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单信息获取（配送单、取件单）(1.01)
 *
 * @since 2011-12
 * @author
 * @version
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
	private OrderListDto orderListDto;

	@XmlElement(name = "orderList")
	public OrderListDto getOrderListDto() {
		return orderListDto;
	}

	public void setOrderListDto(OrderListDto orderListDto) {
		this.orderListDto = orderListDto;
	}

	// @XmlElement(name="orderList")
	// public OrderListDto getOrderList() {
	// return orderList;
	// }
	// public void setOrderList(OrderListDto orderList) {
	// this.orderList = orderList;
	// }
	/**
	 * 返回状态 GET
	 *
	 * @return errCode 返回状态
	 */
	public String getErrCode() {
		return errCode;
	}

	// @XmlElement(name="orderList")
	// public OrderListDto getOrderList() {
	// return orderList;
	// }
	// public void setOrderList(List<OrderListDto> orderList) {
	// this.orderList = orderList;
	// }
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
