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
 * 订单信息获取
 *
 * 
 */
@XmlRootElement(name = "orderList")
public class OrderListDto implements Serializable {
	private static final long serialVersionUID = -9071269456162320375L;
	// Property
	// --------------------------------------------------
	private List<OrderDto> orderDtoList;

	@XmlElement(name = "Order")
	public List<OrderDto> getOrderDtoList() {
		return orderDtoList;
	}

	public void setOrderDtoList(List<OrderDto> orderDtoList) {
		this.orderDtoList = orderDtoList;
	}

}
