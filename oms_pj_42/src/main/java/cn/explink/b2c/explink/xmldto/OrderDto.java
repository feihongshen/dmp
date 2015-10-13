/*
 * NewHeight.com Inc.
 * Copyright (c) 2010-2012 All Rights Reserved.
 */
package cn.explink.b2c.explink.xmldto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单信息获取
 *
 * 
 */

public class OrderDto implements Serializable {
	private static final long serialVersionUID = -9071269456162320375L;
	// Property
	// --------------------------------------------------
	private String cwb;

	private String transcwb;
	private String consigneename;
	private String consigneeaddress;
	private String consigneepostcode;
	private String consigneephone;
	private String consigneemobile;
	private String sendcargoname;
	private String backcargoname;
	private BigDecimal receivablefee;
	private BigDecimal paybackfee;
	private BigDecimal cargorealweight;
	private BigDecimal cargoamount;
	private String cargotype;
	private String cargosize;
	private int sendcargonum;
	private int backcargonum;
	private int cwbordertypeid;
	private int cwbdelivertypeid;// 1普通 2加急
	private String warehousename; // 发货库房
	private String sendtime; // 发货时间
	private String outtobranch; // 上游出库至下游站点，承运商站点
	private String customercommand;// 客户要求
	private String customerwarehouseid; // 供货商发货仓库
	private long paywayid; // 支付方式

	private String cwbprovince;// 省
	private String cwbcity; // 市
	private String cwbcounty; // 区
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;
	private long customerid;
	private String consigneenameOfkf;
	private String consigneemobileOfkf;
	private String consigneephoneOfkf;
	

	public String getConsigneenameOfkf() {
		return consigneenameOfkf;
	}

	public void setConsigneenameOfkf(String consigneenameOfkf) {
		this.consigneenameOfkf = consigneenameOfkf;
	}

	public String getConsigneemobileOfkf() {
		return consigneemobileOfkf;
	}

	public void setConsigneemobileOfkf(String consigneemobileOfkf) {
		this.consigneemobileOfkf = consigneemobileOfkf;
	}

	public String getConsigneephoneOfkf() {
		return consigneephoneOfkf;
	}

	public void setConsigneephoneOfkf(String consigneephoneOfkf) {
		this.consigneephoneOfkf = consigneephoneOfkf;
	}

	public String getRemark1() {
		return remark1;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getCwbprovince() {
		return cwbprovince;
	}

	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}

	public String getCwbcity() {
		return cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getCwbcounty() {
		return cwbcounty;
	}

	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public String getCustomerwarehouseid() {
		return customerwarehouseid;
	}

	public void setCustomerwarehouseid(String customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
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

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
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

	public String getCargotype() {
		return cargotype;
	}

	public void setCargotype(String cargotype) {
		this.cargotype = cargotype;
	}

	public String getCargosize() {
		return cargosize;
	}

	public void setCargosize(String cargosize) {
		this.cargosize = cargosize;
	}

	public int getSendcargonum() {
		return sendcargonum;
	}

	public void setSendcargonum(int sendcargonum) {
		this.sendcargonum = sendcargonum;
	}

	public int getBackcargonum() {
		return backcargonum;
	}

	public void setBackcargonum(int backcargonum) {
		this.backcargonum = backcargonum;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public int getCwbdelivertypeid() {
		return cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(int cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getWarehousename() {
		return warehousename;
	}

	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getOuttobranch() {
		return outtobranch;
	}

	public void setOuttobranch(String outtobranch) {
		this.outtobranch = outtobranch;
	}

	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

}
