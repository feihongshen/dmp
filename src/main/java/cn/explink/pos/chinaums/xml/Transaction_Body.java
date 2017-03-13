package cn.explink.pos.chinaums.xml;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Body {

	// 订单查询、签收使用
	private String orderno;// 订单号
	// 登录使用
	private String passwd;// 登录密码
	private String exceptioncodeversion;// 问题编码最新数据版本
	// 签收使用
	private String cod;// 代收款金额
	private String payway;// 代收款支付方式
	private String banktrace;// 银行系统参考号
	private String postrace;// POS机的流水号
	private String tracetime;// 银行交易时间

	private String cardid;// 银行卡号
	private String signpeople;// 0本人签收1他人签收
	private String dssn;// 电商编号
	private String dsname;// 电商名称
	// 问题件登记
	private String badtype; // 2014-05-09更新为问题件类型
	private String errorcode;// 更改为异常码

	private String memo;// 问题信息备注
	private String urgent;// 紧急标志
	private int signflag;//  城联：signflag 0:本人签收1：他人签收
	private String signer; //城联:签收人
	
	// 撤销签收
	// private String orderno;
	// private String cod;
	// private String banktrace;
	// private String postrace;
	// private String cardid;

	@XmlElement(name = "signflag")
	public int getSignflag() {
		return signflag;
	}

	public void setSignflag(int signflag) {
		this.signflag = signflag;
	}
	@XmlElement(name = "signer")
	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	@XmlElement(name = "badtype")
	public String getBadtype() {
		return badtype;
	}

	public void setBadtype(String badtype) {
		this.badtype = badtype;
	}

	@XmlElement(name = "orderno")
	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	@XmlElement(name = "passwd")
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@XmlElement(name = "cod")
	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	@XmlElement(name = "payway")
	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	@XmlElement(name = "banktrace")
	public String getBanktrace() {
		return banktrace;
	}

	public void setBanktrace(String banktrace) {
		this.banktrace = banktrace;
	}

	@XmlElement(name = "postrace")
	public String getPostrace() {
		return postrace;
	}

	public void setPostrace(String postrace) {
		this.postrace = postrace;
	}

	@XmlElement(name = "passwd")
	public String getTracetime() {
		return tracetime;
	}

	public void setTracetime(String tracetime) {
		this.tracetime = tracetime;
	}

	@XmlElement(name = "cardid")
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	@XmlElement(name = "signpeople")
	public String getSignpeople() {
		return signpeople;
	}

	public void setSignpeople(String signpeople) {
		this.signpeople = signpeople;
	}

	@XmlElement(name = "dssn")
	public String getDssn() {
		return dssn;
	}

	public void setDssn(String dssn) {
		this.dssn = dssn;
	}

	@XmlElement(name = "dsname")
	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	@XmlElement(name = "errorcode")
	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	@XmlElement(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@XmlElement(name = "urgent")
	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}
	@XmlElement(name = "exceptioncodeversion")
	public String getExceptioncodeversion() {
		return exceptioncodeversion;
	}

	public void setExceptioncodeversion(String exceptioncodeversion) {
		this.exceptioncodeversion = exceptioncodeversion;
	}
	
	

}
