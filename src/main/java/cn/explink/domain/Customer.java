package cn.explink.domain;

public class Customer {
	private long customerid;
	private String customername;
	private String companyname;// 公司名称
	private String customeraddress;
	private String customercontactman;
	private String customerphone;
	private long ifeffectflag;
	private int needchecked;// 退货审核0不需要1需要

	private String customercode; // 新增供货商编码（支付宝用到）

	private String b2cEnum;// 对应的枚举id
	private long paytype;
	private long isypdjusetranscwb;// 标识是否一票多件时使用运单号,默认都是0

	private long isUsetranscwb;// 是否使用扫描运单号,默认都是0，0是使用，2是不使用
	private long isAutoProductcwb;// 是否自动生成订单号,默认都是0，0是不使用，1是使用
	private String autoProductcwbpre;// 自动生成订单号前缀
	private int isFeedbackcwb;// 是否进行返单操作
	private int smschannel;// 短信渠道0默认 1亿美
	private long isqufendaxiaoxie;// 单号是否区分大小写0不区分 1区分

	private String wavFilePath = null;// 声音文件路径.

	public int getIsFeedbackcwb() {
		return isFeedbackcwb;
	}

	public void setIsFeedbackcwb(int isFeedbackcwb) {
		this.isFeedbackcwb = isFeedbackcwb;
	}

	public String getB2cEnum() {
		return b2cEnum;
	}

	public void setB2cEnum(String b2cEnum) {
		this.b2cEnum = b2cEnum;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public long getIfeffectflag() {
		return ifeffectflag;
	}

	public void setIfeffectflag(long ifeffectflag) {
		this.ifeffectflag = ifeffectflag;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCustomeraddress() {
		return customeraddress;
	}

	public void setCustomeraddress(String customeraddress) {
		this.customeraddress = customeraddress;
	}

	public String getCustomercontactman() {
		return customercontactman;
	}

	public void setCustomercontactman(String customercontactman) {
		this.customercontactman = customercontactman;
	}

	public String getCustomerphone() {
		return customerphone;
	}

	public void setCustomerphone(String customerphone) {
		this.customerphone = customerphone;
	}

	public long getPaytype() {
		return paytype;
	}

	public void setPaytype(long paytype) {
		this.paytype = paytype;
	}

	public long getIsypdjusetranscwb() {
		return isypdjusetranscwb;
	}

	public void setIsypdjusetranscwb(long isypdjusetranscwb) {
		this.isypdjusetranscwb = isypdjusetranscwb;
	}

	public long getIsUsetranscwb() {
		return isUsetranscwb;
	}

	public void setIsUsetranscwb(long isUsetranscwb) {
		this.isUsetranscwb = isUsetranscwb;
	}

	public long getIsAutoProductcwb() {
		return isAutoProductcwb;
	}

	public void setIsAutoProductcwb(long isAutoProductcwb) {
		this.isAutoProductcwb = isAutoProductcwb;
	}

	public String getAutoProductcwbpre() {
		return autoProductcwbpre;
	}

	public void setAutoProductcwbpre(String autoProductcwbpre) {
		this.autoProductcwbpre = autoProductcwbpre;
	}

	public int getSmschannel() {
		return smschannel;
	}

	public void setSmschannel(int smschannel) {
		this.smschannel = smschannel;
	}

	public long getIsqufendaxiaoxie() {
		return isqufendaxiaoxie;
	}

	public void setIsqufendaxiaoxie(long isqufendaxiaoxie) {
		this.isqufendaxiaoxie = isqufendaxiaoxie;
	}

	public int getNeedchecked() {
		return needchecked;
	}

	public void setNeedchecked(int needchecked) {
		this.needchecked = needchecked;
	}

	public String getWavFilePath() {
		return wavFilePath;
	}

	public void setWavFilePath(String wavFilePath) {
		this.wavFilePath = wavFilePath;
	}

}
