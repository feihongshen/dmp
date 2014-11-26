package cn.explink.domain;

import java.math.BigDecimal;

public class CwbKuaiDi {

	private long id; // 主键id
	private String cwb;// 订单号
	private long lanshoubranchid; // 揽收机构id
	private long lanshouuserid; // 揽收人id
	private long paisongbranchid; // 派送机构id
	private long paisonguserid; // 派送人id
	private long zhongzhuanbranchid; // 中转站id
	private String paisongtime;// 派送时间
	private String lanshoutime;// 揽收时间
	private String sendconsigneename;// 寄件人
	private String sendconsigneecompany;// 寄件人公司
	private String sendconsigneeareacode;// 寄件人区号
	private String sendconsigneecity;// 寄件人城市
	private String sendconsigneeaddress;// 寄件人地址
	private String sendconsigneemobile;// 寄件人手机
	private String sendconsigneephone;// 寄件人座机
	private String sendconsigneepostcode;// 寄件人邮编
	private long paytype;// 付款方式
	private String consigneeareacode;// 收件人区号
	private BigDecimal packingfee; // 包装费
	private BigDecimal insuredrate; // 保价率
	private BigDecimal safefee; // 保险费
	private BigDecimal transitfee; // 运费
	private BigDecimal weightfee; // 计费重量
	private BigDecimal realweight; // 实际重量
	private BigDecimal nowfee; // 现付款项
	private BigDecimal futurefee; // 到付款项
	private BigDecimal packagefee; // 封装费
	private BigDecimal otherfee; // 其他费用
	private BigDecimal allfee; // 费用总计
	private String remark;// 备注
	private long signstate;// 签收状态
	private String signman;// 签收人
	private String signtime;// 签收时间
	private long edituserid;// 修改人
	private String edittime;// 修改时间
	private String shoujianrencompany;// 收件人公司

	public String getShoujianrencompany() {
		return shoujianrencompany;
	}

	public void setShoujianrencompany(String shoujianrencompany) {
		this.shoujianrencompany = shoujianrencompany;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getLanshoubranchid() {
		return lanshoubranchid;
	}

	public void setLanshoubranchid(long lanshoubranchid) {
		this.lanshoubranchid = lanshoubranchid;
	}

	public long getPaisongbranchid() {
		return paisongbranchid;
	}

	public void setPaisongbranchid(long paisongbranchid) {
		this.paisongbranchid = paisongbranchid;
	}

	public long getLanshouuserid() {
		return lanshouuserid;
	}

	public void setLanshouuserid(long lanshouuserid) {
		this.lanshouuserid = lanshouuserid;
	}

	public long getPaisonguserid() {
		return paisonguserid;
	}

	public void setPaisonguserid(long paisonguserid) {
		this.paisonguserid = paisonguserid;
	}

	public String getPaisongtime() {
		return paisongtime;
	}

	public void setPaisongtime(String paisongtime) {
		this.paisongtime = paisongtime;
	}

	public String getLanshoutime() {
		return lanshoutime;
	}

	public void setLanshoutime(String lanshoutime) {
		this.lanshoutime = lanshoutime;
	}

	public String getSendconsigneename() {
		return sendconsigneename;
	}

	public void setSendconsigneename(String sendconsigneename) {
		this.sendconsigneename = sendconsigneename;
	}

	public String getSendconsigneecompany() {
		return sendconsigneecompany;
	}

	public void setSendconsigneecompany(String sendconsigneecompany) {
		this.sendconsigneecompany = sendconsigneecompany;
	}

	public String getSendconsigneeareacode() {
		return sendconsigneeareacode;
	}

	public void setSendconsigneeareacode(String sendconsigneeareacode) {
		this.sendconsigneeareacode = sendconsigneeareacode;
	}

	public String getSendconsigneecity() {
		return sendconsigneecity;
	}

	public void setSendconsigneecity(String sendconsigneecity) {
		this.sendconsigneecity = sendconsigneecity;
	}

	public String getSendconsigneeaddress() {
		return sendconsigneeaddress;
	}

	public void setSendconsigneeaddress(String sendconsigneeaddress) {
		this.sendconsigneeaddress = sendconsigneeaddress;
	}

	public String getSendconsigneemobile() {
		return sendconsigneemobile;
	}

	public void setSendconsigneemobile(String sendconsigneemobile) {
		this.sendconsigneemobile = sendconsigneemobile;
	}

	public String getSendconsigneephone() {
		return sendconsigneephone;
	}

	public void setSendconsigneephone(String sendconsigneephone) {
		this.sendconsigneephone = sendconsigneephone;
	}

	public String getSendconsigneepostcode() {
		return sendconsigneepostcode;
	}

	public void setSendconsigneepostcode(String sendconsigneepostcode) {
		this.sendconsigneepostcode = sendconsigneepostcode;
	}

	public long getPaytype() {
		return paytype;
	}

	public void setPaytype(long paytype) {
		this.paytype = paytype;
	}

	public String getConsigneeareacode() {
		return consigneeareacode;
	}

	public void setConsigneeareacode(String consigneeareacode) {
		this.consigneeareacode = consigneeareacode;
	}

	public BigDecimal getPackingfee() {
		return packingfee;
	}

	public void setPackingfee(BigDecimal packingfee) {
		this.packingfee = packingfee;
	}

	public BigDecimal getSafefee() {
		return safefee;
	}

	public void setSafefee(BigDecimal safefee) {
		this.safefee = safefee;
	}

	public BigDecimal getNowfee() {
		return nowfee;
	}

	public void setNowfee(BigDecimal nowfee) {
		this.nowfee = nowfee;
	}

	public BigDecimal getPackagefee() {
		return packagefee;
	}

	public void setPackagefee(BigDecimal packagefee) {
		this.packagefee = packagefee;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getAllfee() {
		return allfee;
	}

	public void setAllfee(BigDecimal allfee) {
		this.allfee = allfee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getZhongzhuanbranchid() {
		return zhongzhuanbranchid;
	}

	public void setZhongzhuanbranchid(long zhongzhuanbranchid) {
		this.zhongzhuanbranchid = zhongzhuanbranchid;
	}

	public BigDecimal getInsuredrate() {
		return insuredrate;
	}

	public void setInsuredrate(BigDecimal insuredrate) {
		this.insuredrate = insuredrate;
	}

	public BigDecimal getTransitfee() {
		return transitfee;
	}

	public void setTransitfee(BigDecimal transitfee) {
		this.transitfee = transitfee;
	}

	public BigDecimal getWeightfee() {
		return weightfee;
	}

	public void setWeightfee(BigDecimal weightfee) {
		this.weightfee = weightfee;
	}

	public BigDecimal getRealweight() {
		return realweight;
	}

	public void setRealweight(BigDecimal realweight) {
		this.realweight = realweight;
	}

	public long getSignstate() {
		return signstate;
	}

	public void setSignstate(long signstate) {
		this.signstate = signstate;
	}

	public String getSignman() {
		return signman;
	}

	public void setSignman(String signman) {
		this.signman = signman;
	}

	public String getSigntime() {
		return signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public BigDecimal getFuturefee() {
		return futurefee;
	}

	public void setFuturefee(BigDecimal futurefee) {
		this.futurefee = futurefee;
	}

	public long getEdituserid() {
		return edituserid;
	}

	public void setEdituserid(long edituserid) {
		this.edituserid = edituserid;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

}
