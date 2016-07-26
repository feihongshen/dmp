package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

import cn.explink.domain.Common;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.JMath;

public class CwbOrderDTO {
	long opscwbid;

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	String cwb;
	String commoncwb;
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
	long emaildateid;
	String emaildate;
	long excelbranchid;
	long exceldeliverid;
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
	long startbranchid; // 导入数据时入库库房id
	long deliverybranchid;
	// long commonid;
	Common common;
	String orderprefix;
	long commonstate;
	String modelname;// 模版名称
	long isaudit;
	String remark1;
	String remark2;
	String remark3;
	String remark4;
	String remark5;
	long paywayid;// 支付方式（导入数据的时候会导入）
	String newpaywayid;// 新的支付方式（反馈的时候可能会更改的支付方式）
	long customerid;
	String resendtime;// 滞留订单再次配送时间
	BigDecimal shouldfare = BigDecimal.ZERO;
	BigDecimal infactfare = BigDecimal.ZERO;
	long getDataFlag; //是否已同步到主表	
	int mpsallarrivedflag; // '最后一箱标识:1表示最后一箱；0默认';
	int ismpsflag; // 是否一票多件：0默认；1是一票多件'; 
	int vipclub;
	String tpsTranscwb;//tps运单号
    int paymethod;//付款方式
	int doType;//订单类型
	int orderSource;//订单类型：是否外单
	BigDecimal announcedvalue;//保存申明
	
	public int getIsmpsflag() {
		return ismpsflag;
	}

	public void setIsmpsflag(int ismpsflag) {
		this.ismpsflag = ismpsflag;
	}

	public int getMpsallarrivedflag() {
		return mpsallarrivedflag;
	}

	public void setMpsallarrivedflag(int mpsallarrivedflag) {
		this.mpsallarrivedflag = mpsallarrivedflag;
	}

	public long getGetDataFlag() {
		return getDataFlag;
	}

	public void setGetDataFlag(long getDataFlag) {
		this.getDataFlag = getDataFlag;
	}

	public String getResendtime() {
		return resendtime;
	}

	public void setResendtime(String resendtime) {
		this.resendtime = resendtime;
	}

	public BigDecimal getShouldfare() {
		return this.shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public void setShouldfare(String shouldfare) {
		try {
			if ((shouldfare == null) || (shouldfare.length() == 0)) {
				shouldfare = "0";
			}
			this.shouldfare = new BigDecimal(shouldfare);
		} catch (Exception e) {
			throw new IllegalArgumentException("退货运费不是有效的數字格式:" + shouldfare);
		}
	}

	public BigDecimal getInfactfare() {
		return this.infactfare;
	}

	public void setInfactfare(BigDecimal infactfare) {
		this.infactfare = infactfare;
	}

	int addresscodeedittype;// 匹配类型
	String timelimited;// 地址库匹配时效

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	BigDecimal primitivemoney = BigDecimal.ZERO;

	// /2012-10-29 tmall新增字段
	String multi_shipcwb; // 运单号逗号隔开存储 tmall用
	BigDecimal cargovolume = BigDecimal.ZERO; // 货物体积
	String consignoraddress; // 取件地址
	String tmall_notifyid; // tmall发送数据唯一标示
	String printtime = "";// 打印时间

	public long getIsaudit() {
		return this.isaudit;
	}

	public void setIsaudit(long isaudit) {
		this.isaudit = isaudit;
	}

	public String getCommoncwb() {
		return this.commoncwb;
	}

	public void setCommoncwb(String commoncwb) {
		this.commoncwb = commoncwb;
	}

	public String getMulti_shipcwb() {
		return this.multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public BigDecimal getCargovolume() {
		return this.cargovolume;
	}

	public void setCargovolume(BigDecimal cargovolume) {
		this.cargovolume = cargovolume;
	}
	
	public void setCargovolume(String cargovolume) {
		try {
			if ((cargovolume == null) || (cargovolume.length() == 0)) {
				cargovolume = "0";
			}
			this.cargovolume = new BigDecimal(cargovolume);
		} catch (Exception e) {
			throw new IllegalArgumentException("真实体积不是有效的數字格式:" + cargovolume);
		}
	}

	public String getConsignoraddress() {
		return this.consignoraddress;
	}

	public void setConsignoraddress(String consignoraddress) {
		this.consignoraddress = consignoraddress;
	}

	public String getTmall_notifyid() {
		return this.tmall_notifyid;
	}

	public void setTmall_notifyid(String tmall_notifyid) {
		this.tmall_notifyid = tmall_notifyid;
	}

	public long getStartbranchid() {
		return this.startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public long getMultipbranchflag() {
		return this.multipbranchflag;
	}

	public void setMultipbranchflag(long multipbranchflag) {
		this.multipbranchflag = multipbranchflag;
	}

	public long getMultipdeliverflag() {
		return this.multipdeliverflag;
	}

	public void setMultipdeliverflag(long multipdeliverflag) {
		this.multipdeliverflag = multipdeliverflag;
	}

	public long getCstypeid() {
		return this.cstypeid;
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
		return this.excelimportuserid;
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
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = this.removeZero(cwb);
	}

	public String getConsigneename() {
		return this.consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return this.consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		if (consigneeaddress == null) {
			this.consigneeaddress = "";
			return;
		}
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneepostcode() {
		return this.consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = this.removeZero(consigneepostcode);
	}

	public String getConsigneephone() {
		return this.consigneephone;
	}

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			this.setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return this.consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public String getSendcargoname() {
		return this.sendcargoname;
	}

	public void setSendcargoname(String sendcargoname) {
		this.sendcargoname = sendcargoname;
	}

	public String getBackcargoname() {
		return this.backcargoname;
	}

	public void setBackcargoname(String backcargoname) {
		this.backcargoname = backcargoname;
	}

	public BigDecimal getReceivablefee() {
		return this.receivablefee;
	}

	public void setReceivablefee(String receivablefee) {
		try {
			if ((receivablefee == null) || (receivablefee.length() == 0)) {
				receivablefee = "0";
			}
			this.receivablefee = new BigDecimal(receivablefee);
			if (this.receivablefee.compareTo(BigDecimal.ZERO) == -1) {
				this.setPaybackfee(receivablefee.replace("-", ""));
				this.receivablefee = BigDecimal.ZERO;
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("費用不是有效的數字格式:" + receivablefee);
		}
	}

	public BigDecimal getPaybackfee() {
		return this.paybackfee;
	}

	public void setPaybackfee(String paybackfee) {
		try {
			if ((paybackfee == null) || (paybackfee.length() == 0)) {
				paybackfee = "0";
			}
			this.paybackfee = new BigDecimal(paybackfee);
		} catch (Exception e) {
			throw new IllegalArgumentException("退費不是有效的數字格式:" + paybackfee);
		}
	}

	public BigDecimal getCargorealweight() {
		return this.cargorealweight;
	}

	public void setCargorealweight(String cargorealweight) {
		try {
			if ((cargorealweight == null) || (cargorealweight.length() == 0)) {
				cargorealweight = "0";
			}
			this.cargorealweight = new BigDecimal(cargorealweight);
		} catch (Exception e) {
			throw new IllegalArgumentException("真实重量不是有效的數字格式:" + cargorealweight);
		}
	}

	public String getCwbremark() {
		return this.cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getAccountarea() {
		return this.accountarea;
	}

	public void setAccountarea(String accountarea) {
		this.accountarea = accountarea;
	}

	public long getEmaildateid() {
		return this.emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getEmaildate() {
		return this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getExcelbranchid() {
		return this.excelbranchid;
	}

	public void setExcelbranchid(long excelbranchid) {
		this.excelbranchid = excelbranchid;
	}

	public String getShipcwb() {
		return this.shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		shipcwb = this.removeZero(shipcwb);
		this.shipcwb = shipcwb;
	}

	public String getExceldeliver() {
		return this.exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getConsigneeno() {
		return this.consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = this.removeZero(consigneeno);
	}

	public String getExcelbranch() {
		return this.excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public BigDecimal getCargoamount() {
		return this.cargoamount;
	}

	public void setCargoamount(String cargoamount) {
		try {
			if ((cargoamount == null) || (cargoamount.length() == 0)) {
				cargoamount = "0";
			}
			this.cargoamount = new BigDecimal(cargoamount);
		} catch (Exception e) {
			throw new IllegalArgumentException("货物金额不是有效的数字格式:" + cargoamount);
		}
	}

	public String getCustomercommand() {
		return this.customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public String getCargotype() {
		return this.cargotype;
	}

	public void setCargotype(String cargotype) {
		this.cargotype = cargotype;
	}

	public String getCargosize() {
		return this.cargosize;
	}

	public void setCargosize(String cargosize) {
		this.cargosize = cargosize;
	}

	public BigDecimal getBackcargoamount() {
		return this.backcargoamount;
	}

	public void setBackcargoamount(String backcargoamount) {
		try {
			if ((backcargoamount == null) || (backcargoamount.length() == 0)) {
				backcargoamount = "0";
			}
			this.backcargoamount = new BigDecimal(backcargoamount);
		} catch (Exception e) {
			throw new IllegalArgumentException("退货重量不是有效的數字格式:" + backcargoamount);
		}
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransway() {
		return this.transway;
	}

	public void setTransway(String transway) {
		this.transway = transway;
	}

	public int getSendcargonum() {
		return this.sendcargonum;
	}

	public void setSendcargonum(String sendcargonum) {
		try {
			if ((sendcargonum == null) || (sendcargonum.length() == 0)) {
				sendcargonum = "1";
			}
			this.sendcargonum = Integer.parseInt(this.removeZero(sendcargonum));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("货物数量不是有效的數字格式:" + sendcargonum);
		}
	}

	public int getBackcargonum() {
		return this.backcargonum;
	}

	public void setBackcargonum(String backcargonum) {
		try {
			if ((backcargonum == null) || (backcargonum.length() == 0)) {
				backcargonum = "1";
			}
			this.backcargonum = Integer.parseInt(this.removeZero(backcargonum));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("退货数量不是有效的數字格式:" + this.sendcargonum);
		}
	}

	public long getCwbdelivertypeid() {
		return this.cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(long cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getCwbprovince() {
		return this.cwbprovince;
	}

	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}

	public String getCwbcity() {
		return this.cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getCwbcounty() {
		return this.cwbcounty;
	}

	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}

	/*
	 * public String getOrdercwb() { return ordercwb; } public void
	 * setOrdercwb(String ordercwb) { this.ordercwb = ordercwb; }
	 */
	public String getTranscwb() {
		return this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = this.removeZero(transcwb);
	}

	public String getPaisongArea() {
		return this.paisongArea;
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
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public void guessCwbordertypeid() {
		// if(this.getReceivablefee().compareTo(BigDecimal.ZERO)>0&&this.getPaybackfee().compareTo(BigDecimal.ZERO)>0){
		// this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		// // 上门退
		// }else if(this.getReceivablefee().compareTo(BigDecimal.ZERO)>0){
		this.setCwbordertypeid(CwbOrderTypeIdEnum.Peisong.getValue());
		// }else{
		// this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmentui.getValue());
		// }
	}

	public void guessPaywayid() {
		// if(this.getReceivablefee().compareTo(BigDecimal.ZERO)>0&&this.getPaybackfee().compareTo(BigDecimal.ZERO)>0){
		// this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		// // 上门退
		// }else if(this.getReceivablefee().compareTo(BigDecimal.ZERO)>0){
		this.setPaywayid(PaytypeEnum.Xianjin.getValue());
		// }else{
		// this.setCwbordertypeid(CwbOrderTypeIdEnum.Shangmentui.getValue());
		// }
	}

	public void setDefaultCargoName() {
		if ((this.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) && !StringUtils.hasLength(this.getSendcargoname())) {
			this.setSendcargoname("");
		}
		if ((this.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && !StringUtils.hasLength(this.getBackcargoname())) {
			this.setBackcargoname("");
		}
	}

	public long getAccountareaid() {
		return this.accountareaid;
	}

	public void setAccountareaid(long accountareaid) {
		this.accountareaid = accountareaid;
	}

	public long getServiceareaid() {
		return this.serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public long getShipperid() {
		return this.shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public long getCustomerwarehouseid() {
		return this.customerwarehouseid;
	}

	public void setCustomerwarehouseid(long customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
	}

	public Common getCommon() {
		return this.common;
	}

	public void setCommon(Common common) {
		this.common = common;
	}

	public BigDecimal getPrimitivemoney() {
		return this.primitivemoney;
	}

	public void setPrimitivemoney(BigDecimal primitivemoney) {
		this.primitivemoney = primitivemoney;
	}

	public String getModelname() {
		return this.modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return this.remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return this.remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return this.remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public long getPaywayid() {
		return this.paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public String getNewpaywayid() {
		return this.newpaywayid;
	}

	public void setNewpaywayid(String newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public int getAddresscodeedittype() {
		return this.addresscodeedittype;
	}

	public void setAddresscodeedittype(int addresscodeedittype) {
		this.addresscodeedittype = addresscodeedittype;
	}

	public String getPrinttime() {
		return this.printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}

	public String getTimelimited() {
		return this.timelimited;
	}

	public void setTimelimited(String timelimited) {
		this.timelimited = timelimited;
	}
	
	public int getVipclub() {
		return vipclub;
	}

	public void setVipclub(int vipclub) {
		this.vipclub = vipclub;
	}

	public String getTpsTranscwb() {
		return tpsTranscwb;
	}

	public void setTpsTranscwb(String tpsTranscwb) {
		this.tpsTranscwb = tpsTranscwb;
	}

	public int getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(int paymethod) {
		this.paymethod = paymethod;
	}

	public int getDoType() {
		return doType;
	}

	public void setDoType(int doType) {
		this.doType = doType;
	}

	public int getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(int orderSource) {
		this.orderSource = orderSource;
	}

	public BigDecimal getAnnouncedvalue() {
		return announcedvalue;
	}

	public void setAnnouncedvalue(BigDecimal announcedvalue) {
		this.announcedvalue = announcedvalue;
	}
		public long getExceldeliverid() {
		return exceldeliverid;
	}

	public void setExceldeliverid(long exceldeliverid) {
		this.exceldeliverid = exceldeliverid;
	}
}
