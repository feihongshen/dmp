package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.JMath;

public class CwbOrderDTO {
	String cwb;
	String consigneename;
	String consigneeaddress;
	String consigneepostcode;
	String consigneephone;
	String consigneemobile;
	String sendcargoname;
	String backcargoname;
	BigDecimal receivablefee = BigDecimal.ZERO;
	BigDecimal paybackfee = BigDecimal.ZERO;
	BigDecimal cargorealweight = BigDecimal.ZERO;
	String cwbremark;
	String accountarea;
	long accountareaid;
	long serviceareaid;
	String emaildate;
	long excelbranchid;
	String shipcwb;
	String exceldeliver;
	String consigneeno;
	String excelbranch;
	BigDecimal cargoamount = BigDecimal.ZERO;
	String customercommand;
	String cargotype;
	String cargosize;
	BigDecimal backcargoamount = BigDecimal.ZERO;
	String destination;
	String transway;
	long shipperid;
	int sendcargonum;
	int backcargonum;
	long cwbordertypeid = 0;
	long cwbdelivertypeid;
	long customerwarehouseid;
	String excelimportuserid;
	long multipbranchflag;
	long multipdeliverflag;
	long cstypeid;
	long nextbranchid;
	String commonnumber;
	String commonname;
	long commonid;
	String orderprefix;
	long commonstate;
	BigDecimal primitivemoney = BigDecimal.ZERO;

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getMultipbranchflag() {
		return multipbranchflag;
	}

	public void setMultipbranchflag(long multipbranchflag) {
		this.multipbranchflag = multipbranchflag;
	}

	public long getMultipdeliverflag() {
		return multipdeliverflag;
	}

	public void setMultipdeliverflag(long multipdeliverflag) {
		this.multipdeliverflag = multipdeliverflag;
	}

	public long getCstypeid() {
		return cstypeid;
	}

	public void setCstypeid(long cstypeid) {
		this.cstypeid = cstypeid;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public void setCargorealweight(BigDecimal cargorealweight) {
		this.cargorealweight = cargorealweight;
	}

	public void setCargoamount(BigDecimal cargoamount) {
		this.cargoamount = cargoamount;
	}

	public void setBackcargoamount(BigDecimal backcargoamount) {
		this.backcargoamount = backcargoamount;
	}

	public void setSendcargonum(int sendcargonum) {
		this.sendcargonum = sendcargonum;
	}

	public void setBackcargonum(int backcargonum) {
		this.backcargonum = backcargonum;
	}

	public String getExcelimportuserid() {
		return excelimportuserid;
	}

	public void setExcelimportuserid(String excelimportuserid) {
		this.excelimportuserid = excelimportuserid;
	}

	String cwbprovince;
	String cwbcity;
	String cwbcounty;
	// String ordercwb;
	String transcwb;
	String paisongArea;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = removeZero(cwb);
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
		if (consigneeaddress == null) {
			this.consigneeaddress = "";
			return;
		}
		this.consigneeaddress = consigneeaddress.replaceAll("#", "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{P}", "");
	}

	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = removeZero(consigneepostcode);
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = JMath.getmobileinstr(consigneemobile);
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

	public void setReceivablefee(String receivablefee) {
		try {
			this.receivablefee = new BigDecimal(receivablefee);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("費用不是有效的數字格式:" + receivablefee);
		}
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(String paybackfee) {
		try {
			this.paybackfee = new BigDecimal(paybackfee);
		} catch (Exception e) {
			throw new IllegalArgumentException("退費不是有效的數字格式:" + paybackfee);
		}
	}

	public BigDecimal getCargorealweight() {
		return cargorealweight;
	}

	public void setCargorealweight(String cargorealweight) {
		try {
			this.cargorealweight = new BigDecimal(cargorealweight);
		} catch (Exception e) {
			throw new IllegalArgumentException("真实重量不是有效的數字格式:" + paybackfee);
		}
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getAccountarea() {
		return accountarea;
	}

	public void setAccountarea(String accountarea) {
		this.accountarea = accountarea;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getExcelbranchid() {
		return excelbranchid;
	}

	public void setExcelbranchid(long excelbranchid) {
		this.excelbranchid = excelbranchid;
	}

	public String getShipcwb() {
		return shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		shipcwb = removeZero(shipcwb);
		this.shipcwb = shipcwb;
	}

	public String getExceldeliver() {
		return exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getConsigneeno() {
		return consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = removeZero(consigneeno);
	}

	public String getExcelbranch() {
		return excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public BigDecimal getCargoamount() {
		return cargoamount;
	}

	public void setCargoamount(String cargoamount) {
		try {
			this.cargoamount = new BigDecimal(cargoamount);
		} catch (Exception e) {
			throw new IllegalArgumentException("货物金额不是有效的数字格式:" + cargoamount);
		}
	}

	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
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

	public BigDecimal getBackcargoamount() {
		return backcargoamount;
	}

	public void setBackcargoamount(String backcargoamount) {
		try {
			this.backcargoamount = new BigDecimal(backcargoamount);
		} catch (Exception e) {
			throw new IllegalArgumentException("退货重量不是有效的數字格式:" + backcargoamount);
		}
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransway() {
		return transway;
	}

	public void setTransway(String transway) {
		this.transway = transway;
	}

	public int getSendcargonum() {
		return sendcargonum;
	}

	public void setSendcargonum(String sendcargonum) {
		try {
			this.sendcargonum = Integer.parseInt(removeZero(sendcargonum));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("货物数量不是有效的數字格式:" + sendcargonum);
		}
	}

	public int getBackcargonum() {
		return backcargonum;
	}

	public void setBackcargonum(String backcargonum) {
		try {
			this.backcargonum = Integer.parseInt(removeZero(backcargonum));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("退货数量不是有效的數字格式:" + sendcargonum);
		}
	}

	public long getCwbdelivertypeid() {
		return cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(long cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
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

	/*
	 * public String getOrdercwb() { return ordercwb; } public void
	 * setOrdercwb(String ordercwb) { this.ordercwb = ordercwb; }
	 */
	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getPaisongArea() {
		return paisongArea;
	}

	public void setPaisongArea(String paisongArea) {
		this.paisongArea = paisongArea;
	}

	private String removeZero(String cwb) {
		int end = cwb.indexOf(".0");
		if (end > -1) {
			cwb = cwb.substring(0, end);
		}
		return cwb;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public void guessCwbordertypeid() {
		if (this.getReceivablefee().compareTo(BigDecimal.ZERO) > 0 && this.getPaybackfee().compareTo(BigDecimal.ZERO) > 0) {
			this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmenhuan.getValue()); // 上门退
		} else if (this.getReceivablefee().compareTo(BigDecimal.ZERO) > 0) {
			this.setCwbordertypeid(CwbOrderTypeIdEnum.Peisong.getValue());
		} else {
			this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmentui.getValue());
		}
	}

	public void setDefaultCargoName() {
		if (this.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue() && !StringUtils.hasLength(this.getSendcargoname())) {
			this.setSendcargoname("[发出商品]");
		}
		if (this.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue() && !StringUtils.hasLength(this.getBackcargoname())) {
			this.setBackcargoname("[取回商品]");
		}
	}

	public long getAccountareaid() {
		return accountareaid;
	}

	public void setAccountareaid(long accountareaid) {
		this.accountareaid = accountareaid;
	}

	public long getServiceareaid() {
		return serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public long getShipperid() {
		return shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public long getCustomerwarehouseid() {
		return customerwarehouseid;
	}

	public void setCustomerwarehouseid(long customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
	}

	public String getCommonnumber() {
		return commonnumber;
	}

	public void setCommonnumber(String commonnumber) {
		this.commonnumber = commonnumber;
	}

	public String getCommonname() {
		return commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public long getCommonid() {
		return commonid;
	}

	public void setCommonid(long commonid) {
		this.commonid = commonid;
	}

	public String getOrderprefix() {
		return orderprefix;
	}

	public void setOrderprefix(String orderprefix) {
		this.orderprefix = orderprefix;
	}

	public long getCommonstate() {
		return commonstate;
	}

	public void setCommonstate(long commonstate) {
		this.commonstate = commonstate;
	}

	public BigDecimal getPrimitivemoney() {
		return primitivemoney;
	}

	public void setPrimitivemoney(BigDecimal primitivemoney) {
		this.primitivemoney = primitivemoney;
	}

	public void setPrimitivemoney(String primitivemoney) {
		try {
			this.primitivemoney = new BigDecimal(primitivemoney);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("費用不是有效的數字格式:" + primitivemoney);
		}
	}

}
