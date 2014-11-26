/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.yonghuics.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 订单信息获取
 *
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {
	private static long serialVersionUID = -9071269456162320375L;
	// Property
	// --------------------------------------------------
	private String sheetid; // 订单号
	private String bagno; // 包裹号 订单号--1对多---包裹号
	private String ordertype; // 订单类型 ？该字段如何定义
	private String recName; // 收件人
	private String order_address; // 详细地址
	private String recphone;// 收件人手机号
	private String weight; // 实际重量
	private String allamt; // 金额 ？是代收金额么？
	private String shopname; // 主配门店;
	private String paytype; // 支付方式
	private String logistics_name; // 配送要求(目前为空)
	private String sendtime; // 送货时段(目前为空)
	private String opname; // 承运人(目前为空)
	private String stationid;// 配送站(目前为空)
	private String prephone;// 电话预约(目前为空);
	private String note; // 备注

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public String getSheetid() {
		return sheetid;
	}

	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}

	public String getBagno() {
		return bagno;
	}

	public void setBagno(String bagno) {
		this.bagno = bagno;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getOrder_address() {
		return order_address;
	}

	public void setOrder_address(String order_address) {
		this.order_address = order_address;
	}

	public String getRecphone() {
		return recphone;
	}

	public void setRecphone(String recphone) {
		this.recphone = recphone;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAllamt() {
		return allamt;
	}

	public void setAllamt(String allamt) {
		this.allamt = allamt;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getLogistics_name() {
		return logistics_name;
	}

	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getOpname() {
		return opname;
	}

	public void setOpname(String opname) {
		this.opname = opname;
	}

	public String getStationid() {
		return stationid;
	}

	public void setStationid(String stationid) {
		this.stationid = stationid;
	}

	public String getPrephone() {
		return prephone;
	}

	public void setPrephone(String prephone) {
		this.prephone = prephone;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
