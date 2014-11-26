/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yonghuics.json;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 订单信息获取
 *
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderListDto implements Serializable {

	// Property
	// --------------------------------------------------
	private List<OrderDto> orderDtoList;

	public List<OrderDto> getOrderDtoList() {
		return orderDtoList;
	}

	public void setOrderDtoList(List<OrderDto> orderDtoList) {
		this.orderDtoList = orderDtoList;
	}
}
