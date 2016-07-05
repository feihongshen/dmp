package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.util.SecurityUtil;

public class ShangMenTuiCwbDetail {

	private long id; // 主键id
	private long carwarehouseid;// 发货仓库
	private long backcarnum;// 取货数量
	private long customerid;// 供货商id
	private String consigneename;// 收件人名称
	private String consigneeaddress;// 收件人地址
	private String consigneephone;// 收件人电话
	private String consigneemobile;// 收件人手机
	private BigDecimal paybackfee;// 上门退货应退金额
	private String cwb;// 订单号
	private String printtime;// 打印时间（针对上门退订单）
	private String remark3;// 备注三
	private String remark4;// 备注四
	private String remark5;// 备注五
	private String backcarname; // 取回商品名称
	private String consigneepostcode;// 收件人邮编
	private long emaildateid;// 发货时间Id
	private String emaildate;// 发货时间
	private long deliverybranchid;// 配送站点Id

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCarwarehouseid() {
		return carwarehouseid;
	}

	public void setCarwarehouseid(long carwarehouseid) {
		this.carwarehouseid = carwarehouseid;
	}

	public long getBackcarnum() {
		return backcarnum;
	}

	public void setBackcarnum(long backcarnum) {
		this.backcarnum = backcarnum;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
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

	public String getConsigneephone() {
		return SecurityUtil.getInstance().decrypt(consigneephone);
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneemobile() {
		return SecurityUtil.getInstance().decrypt(consigneemobile);
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getPrinttime() {
		return printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
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

	public String getBackcarname() {
		return backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

}
