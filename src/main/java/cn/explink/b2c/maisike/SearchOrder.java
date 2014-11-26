package cn.explink.b2c.maisike;

/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单信息获取
 *
 * 
 */

public class SearchOrder implements Serializable {
	private static final long serialVersionUID = -9071269456162320375L;
	// Property
	// --------------------------------------------------
	private String consigneemobile;
	private String cwb;
	private String transcwb;
	private String consigneephone;
	private String consigneepostcode;
	private String consigneeaddress;
	private String consigneename;
	private String sendcargoname;
	private String backcargoname;
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;
	private BigDecimal cargorealweight;
	private BigDecimal cargoamount;
	private long sendcargonum;
	private int cwbordertypeid;
	private String Sendtime;
	private String customercommand;// 客户要求
	private long paywayid;

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getSendcargoname() {
		return sendcargoname;
	}

	public void setSendcargoname(String sendcargoname) {
		this.sendcargoname = sendcargoname;
	}

	public String getBackcargoname() {
		return backcargoname;
	}

	public void setBackcargoname(String backcargoname) {
		this.backcargoname = backcargoname;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public BigDecimal getCargorealweight() {
		return cargorealweight;
	}

	public void setCargorealweight(BigDecimal cargorealweight) {
		this.cargorealweight = cargorealweight;
	}

	public BigDecimal getCargoamount() {
		return cargoamount;
	}

	public void setCargoamount(BigDecimal cargoamount) {
		this.cargoamount = cargoamount;
	}

	public long getSendcargonum() {
		return sendcargonum;
	}

	public void setSendcargonum(long sendcargonum) {
		this.sendcargonum = sendcargonum;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getSendtime() {
		return Sendtime;
	}

	public void setSendtime(String sendtime) {
		Sendtime = sendtime;
	}

	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
