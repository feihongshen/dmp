package cn.explink.controller;

import java.math.BigDecimal;

public class WarehouseGroupPrintDto {
	private String customername;//供货商
	private String consigneepostcode;//邮编
	private String carsize;//商品尺寸
	private String cwb;//订单号
	private String consigneemobile;//手机
	private BigDecimal receivablefee;//应收金额
	private String cwbordertypeid;//订单类型
	private String consigneephone;//电话
	private BigDecimal paybackfee;//应退金额
	private long sendcarnum;//订单数量
	private BigDecimal carrealweight;//货物重量
	private String paywayid;//支付方式
	private String credate;//
	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}
	private String consigneename;//收件人
	private long backcarnum;//取货数量
	private String cwbremark;// 订单备注
	private String reasoncontent;//退货原因
	private String consigneeaddress;//地址
	private BigDecimal caramount;// 货物金额
	private String  carwarehouse;//发货仓库
	private String transcwb;//运单号
	private String startbranch;//上一站
	private String tuihuozhanrukutime;//退货站入库时间
	private String emaildate;// 发货时间
	private String sendcarname;//发货商品
	private String backcarname;//取货商品'
	private long jianshu;//件数
	private BigDecimal chengzhong;//称重重量
	private String baleno;//包号
	private long danshu;//单数
	
	public long getDanshu() {
		return danshu;
	}

	public void setDanshu(long danshu) {
		this.danshu = danshu;
	}

	public long getJianshu() {
		return jianshu;
	}

	public void setJianshu(long jianshu) {
		this.jianshu = jianshu;
	}

	public BigDecimal getChengzhong() {
		return chengzhong;
	}

	public void setChengzhong(BigDecimal chengzhong) {
		this.chengzhong = chengzhong;
	}

	public String getBaleno() {
		return baleno;
	}

	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public String getBackcarname() {
		return backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public WarehouseGroupPrintDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	
	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getCarsize() {
		return carsize;
	}
	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getConsigneemobile() {
		return consigneemobile;
	}
	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}
	public BigDecimal getReceivablefee() {
		return receivablefee;
	}
	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}
	
	

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getConsigneephone() {
		return consigneephone;
	}
	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}
	
	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	
	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public BigDecimal getCarrealweight() {
		return carrealweight;
	}
	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}
	
	public String getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(String paywayid) {
		this.paywayid = paywayid;
	}

	public String getConsigneename() {
		return consigneename;
	}
	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}
	public long getBackcarnum() {
		return backcarnum;
	}
	public void setBackcarnum(long backcarnum) {
		this.backcarnum = backcarnum;
	}
	public String getCwbremark() {
		return cwbremark;
	}
	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}
	public String getReasoncontent() {
		return reasoncontent;
	}
	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
	}
	public String getConsigneeaddress() {
		return consigneeaddress;
	}
	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}
	public BigDecimal getCaramount() {
		return caramount;
	}
	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	

	public String getCarwarehouse() {
		return carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public String getStartbranch() {
		return startbranch;
	}
	public void setStartbranch(String startbranch) {
		this.startbranch = startbranch;
	}
	public String getTuihuozhanrukutime() {
		return tuihuozhanrukutime;
	}
	public void setTuihuozhanrukutime(String tuihuozhanrukutime) {
		this.tuihuozhanrukutime = tuihuozhanrukutime;
	}
	public String getEmaildate() {
		return emaildate;
	}
	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}
	public String getSendcarname() {
		return sendcarname;
	}
	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}
	
}
