package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import cn.explink.enumutil.BranchEnum;

public class Branch implements java.io.Serializable {
	
	private static final long serialVersionUID = -7873112119688063347L;
	
	private long branchid;
	
	@Length(max = 50, message = "机构名称最多为50个字符")
	private String branchname;
	
	@Length(max = 32, message = "省份最多为32个字符")
	private String branchprovince;
	
	@Length(max = 32, message = "城市最多为32个字符")
	private String branchcity;
	
	@Length(max = 32, message = "区/县最多为32个字符")
	private String brancharea;
	
	@Length(max = 32, message = "街道最多为32个字符")
	private String branchstreet;
	
	@Length(max = 50, message = "地址最多为50个字符")
	private String branchaddress;
	
	@Length(max = 50, message = "负责人最多为50个字符")
	private String branchcontactman;
	
	@Length(max = 50, message = "固定电话最多为50个字符")
	private String branchphone;
	
	@Length(max = 50, message = "机构手机最多为50个字符")
	private String branchmobile;
	
	@Length(max = 50, message = "传真最多为50个字符")
	private String branchfax;
	
	@Length(max = 50, message = "邮箱最多为50个字符")
	private String branchemail;
	
	private String contractflag;
	private String bankcard;
	private String cwbtobranchid;
	private String payfeeupdateflag;
	private String backtodeliverflag;
	private String branchpaytoheadflag;
	private String branchfinishdayflag;
	private BigDecimal branchinsurefee;
	private String branchwavfile;
	private BigDecimal creditamount;
	private String brancheffectflag;
	private BigDecimal contractrate;
	
	@Length(max = 32, message = "分拣码最多为32个字符")
	private String branchcode;
	
	private String noemailimportflag;
	private String errorcwbdeliverflag;
	private String errorcwbbranchflag;
	private String branchcodewavfile;
	private String importwavtype;
	private String exportwavtype;
	private String noemaildeliverflag;
	private int sendstartbranchid;
	private int sitetype;
	private int checkremandtype;
	private String branchmatter;
	private int accountareaid;

	private BigDecimal arrearagehuo;
	private BigDecimal arrearagepei;
	private BigDecimal arrearagefa;

	private long zhongzhuanid;
	private long tuihuoid;
	private long caiwuid;

	private BigDecimal arrearagepayupaudit;
	private BigDecimal posarrearagepayupaudit;
	private int bindmsksid; // 绑定迈思可站点id

	private int accounttype;// 结算类型
	private int accountexcesstype;// 超额类型 %or元
	private BigDecimal accountexcessfee;// 超额
	private long accountbranch;// 结算对象

	private BigDecimal credit;// 信誉额度
	private BigDecimal balance;// 账户余额
	private BigDecimal debt;// 账户欠款
	private BigDecimal balancevirt;// 账户余额
	private BigDecimal debtvirt;// 账户欠款

	private BigDecimal credituse;// 可用额度 不在数据库字段

	private long prescription24;// 24小时时效
	private long prescription48;// 48小时时效

	private long backtime;// 退货出站超时时效：

	private BigDecimal branchBail;// 站点保证金
	private long pfruleid;// 派费规则id

	private long branchprovinceid;//站点所在省id
	
	private long branchcityid;//站点所在市id
	
	@Length(max = 32, message = "机构编码最多为32个字符")
	private String tpsbranchcode;//上传tps时所用的机构编码
	
	@Pattern(regexp = "^([0-9]{3}[01]{1})?$", message = "对应滑槽口号要么为空，要么为0或1结尾的4位数字")
	private String outputno;	//自动化分拣使用的出货口编号
	
	//自动核销用到的字段
	/*
	 * 通联-银行账号
	 */
	private String bankCardNo;
	/*
	 * 通联-银行编码
	 */
	private String bankCode;
	/*
	 * 通联-持卡人姓名
	 */
	private String ownerName;
	/*
	 * 通联-银行账号类型  0：私人   1：企业
	 */
	private int bankAccountType;

	/*
	 * 财付通-银行账号
	 */
	private String cftAccountNo;
	/*
	 * 通联-银行编码
	 */
	private String cftBankCode;
	/*
	 * 通联-持卡人姓名
	 */
	private String cftAccountName;
	/*
	 * 通联-银行账号类型  0：私人   1：企业
	 */
	private int cftAccountProp;
	/*
	 * 财付通-持卡人证件类型  身份证1；护照2；军官证3；士兵证4；回乡证5；临时身份证6；户口簿7；警官证8；台胞证9；营业执照10；其它证件11
	 */
	private int cftCertType;
	/*
	 * 证件号
	 */
	private String cftCertId;
	/*
	 * 站点缴款方式
	 */
	private int payinType;
	
	private String updateUser;
	
	private String updateTime;
	
	/**
	 * @return the pfruleid
	 */
	public long getPfruleid() {
		return this.pfruleid;
	}

	/**
	 * @param pfruleid
	 *            the pfruleid to set
	 */
	public void setPfruleid(long pfruleid) {
		this.pfruleid = pfruleid;
	}

	public long getBacktime() {
		return this.backtime;
	}

	public void setBacktime(long backtime) {
		this.backtime = backtime;
	}

	public BigDecimal getBalancevirt() {
		return this.balancevirt;
	}

	public void setBalancevirt(BigDecimal balancevirt) {
		this.balancevirt = balancevirt;
	}

	public BigDecimal getDebtvirt() {
		return this.debtvirt;
	}

	public void setDebtvirt(BigDecimal debtvirt) {
		this.debtvirt = debtvirt;
	}

	public BigDecimal getCredituse() {
		return this.credituse;
	}

	public void setCredituse(BigDecimal credituse) {
		this.credituse = credituse;
	}

	public BigDecimal getDebt() {
		return this.debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	public BigDecimal getCredit() {
		return this.credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public long getAccountbranch() {
		return this.accountbranch;
	}

	public void setAccountbranch(long accountbranch) {
		this.accountbranch = accountbranch;
	}

	public int getAccounttype() {
		return this.accounttype;
	}

	public void setAccounttype(int accounttype) {
		this.accounttype = accounttype;
	}

	public int getAccountexcesstype() {
		return this.accountexcesstype;
	}

	public void setAccountexcesstype(int accountexcesstype) {
		this.accountexcesstype = accountexcesstype;
	}

	public BigDecimal getAccountexcessfee() {
		return this.accountexcessfee;
	}

	public void setAccountexcessfee(BigDecimal accountexcessfee) {
		this.accountexcessfee = accountexcessfee;
	}

	public int getBindmsksid() {
		return this.bindmsksid;
	}

	public void setBindmsksid(int bindmsksid) {
		this.bindmsksid = bindmsksid;
	}

	public BigDecimal getArrearagepayupaudit() {
		return this.arrearagepayupaudit;
	}

	public void setArrearagepayupaudit(BigDecimal arrearagepayupaudit) {
		this.arrearagepayupaudit = arrearagepayupaudit;
	}

	public BigDecimal getPosarrearagepayupaudit() {
		return this.posarrearagepayupaudit;
	}

	public void setPosarrearagepayupaudit(BigDecimal posarrearagepayupaudit) {
		this.posarrearagepayupaudit = posarrearagepayupaudit;
	}

	public long getZhongzhuanid() {
		return this.zhongzhuanid;
	}

	public void setZhongzhuanid(long zhongzhuanid) {
		this.zhongzhuanid = zhongzhuanid;
	}

	public long getTuihuoid() {
		return this.tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	public long getCaiwuid() {
		return this.caiwuid;
	}

	public void setCaiwuid(long caiwuid) {
		this.caiwuid = caiwuid;
	}

	public BigDecimal getArrearagehuo() {
		return this.arrearagehuo;
	}

	public void setArrearagehuo(BigDecimal arrearagehuo) {
		this.arrearagehuo = arrearagehuo;
	}

	public BigDecimal getArrearagepei() {
		return this.arrearagepei;
	}

	public void setArrearagepei(BigDecimal arrearagepei) {
		this.arrearagepei = arrearagepei;
	}

	public BigDecimal getArrearagefa() {
		return this.arrearagefa;
	}

	public void setArrearagefa(BigDecimal arrearagefa) {
		this.arrearagefa = arrearagefa;
	}

	public String getBranchmatter() {
		return this.branchmatter;
	}

	public void setBranchmatter(String branchmatter) {
		this.branchmatter = branchmatter;
	}

	public int getAccountareaid() {
		return this.accountareaid;
	}

	public void setAccountareaid(int accountareaid) {
		this.accountareaid = accountareaid;
	}

	public int getCheckremandtype() {
		return this.checkremandtype;
	}

	public void setCheckremandtype(int checkremandtype) {
		this.checkremandtype = checkremandtype;
	}

	public int getSendstartbranchid() {
		return this.sendstartbranchid;
	}

	public void setSendstartbranchid(int sendstartbranchid) {
		this.sendstartbranchid = sendstartbranchid;
	}

	public String getBranchprovince() {
		return this.branchprovince;
	}

	public void setBranchprovince(String branchprovince) {
		this.branchprovince = branchprovince;
	}

	public String getBranchcity() {
		return this.branchcity;
	}

	public void setBranchcity(String branchcity) {
		this.branchcity = branchcity;
	}

	public String getBranchaddress() {
		return this.branchaddress;
	}

	public void setBranchaddress(String branchaddress) {
		this.branchaddress = branchaddress;
	}

	public String getBranchcontactman() {
		return this.branchcontactman;
	}

	public void setBranchcontactman(String branchcontactman) {
		this.branchcontactman = branchcontactman;
	}

	public String getBranchphone() {
		return this.branchphone;
	}

	public void setBranchphone(String branchphone) {
		this.branchphone = branchphone;
	}

	public String getBranchmobile() {
		return this.branchmobile;
	}

	public void setBranchmobile(String branchmobile) {
		this.branchmobile = branchmobile;
	}

	public String getBranchfax() {
		return this.branchfax;
	}

	public void setBranchfax(String branchfax) {
		this.branchfax = branchfax;
	}

	public String getBranchemail() {
		return this.branchemail;
	}

	public void setBranchemail(String branchemail) {
		this.branchemail = branchemail;
	}

	public String getContractflag() {
		return this.contractflag;
	}

	public void setContractflag(String contractflag) {
		this.contractflag = contractflag;
	}

	public String getCwbtobranchid() {
		return this.cwbtobranchid;
	}

	public void setCwbtobranchid(String cwbtobranchid) {
		this.cwbtobranchid = cwbtobranchid;
	}

	public String getPayfeeupdateflag() {
		return this.payfeeupdateflag;
	}

	public void setPayfeeupdateflag(String payfeeupdateflag) {
		this.payfeeupdateflag = payfeeupdateflag;
	}

	public String getBacktodeliverflag() {
		return this.backtodeliverflag;
	}

	public void setBacktodeliverflag(String backtodeliverflag) {
		this.backtodeliverflag = backtodeliverflag;
	}

	public String getBranchpaytoheadflag() {
		return this.branchpaytoheadflag;
	}

	public void setBranchpaytoheadflag(String branchpaytoheadflag) {
		this.branchpaytoheadflag = branchpaytoheadflag;
	}

	public String getBranchfinishdayflag() {
		return this.branchfinishdayflag;
	}

	public void setBranchfinishdayflag(String branchfinishdayflag) {
		this.branchfinishdayflag = branchfinishdayflag;
	}

	public BigDecimal getBranchinsurefee() {
		return this.branchinsurefee;
	}

	public void setBranchinsurefee(BigDecimal branchinsurefee) {
		this.branchinsurefee = branchinsurefee;
	}

	public String getBranchwavfile() {
		return this.branchwavfile;
	}

	public void setBranchwavfile(String branchwavfile) {
		this.branchwavfile = branchwavfile;
	}

	public BigDecimal getCreditamount() {
		return this.creditamount;
	}

	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}

	public String getBrancheffectflag() {
		return this.brancheffectflag;
	}

	public void setBrancheffectflag(String brancheffectflag) {
		this.brancheffectflag = brancheffectflag;
	}

	public BigDecimal getContractrate() {
		return this.contractrate;
	}

	public void setContractrate(BigDecimal contractrate) {
		this.contractrate = contractrate;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getNoemailimportflag() {
		return this.noemailimportflag;
	}

	public void setNoemailimportflag(String noemailimportflag) {
		this.noemailimportflag = noemailimportflag;
	}

	public String getErrorcwbdeliverflag() {
		return this.errorcwbdeliverflag;
	}

	public void setErrorcwbdeliverflag(String errorcwbdeliverflag) {
		this.errorcwbdeliverflag = errorcwbdeliverflag;
	}

	public String getErrorcwbbranchflag() {
		return this.errorcwbbranchflag;
	}

	public void setErrorcwbbranchflag(String errorcwbbranchflag) {
		this.errorcwbbranchflag = errorcwbbranchflag;
	}

	public String getBranchcodewavfile() {
		return this.branchcodewavfile;
	}

	public void setBranchcodewavfile(String branchcodewavfile) {
		this.branchcodewavfile = branchcodewavfile;
	}

	public String getImportwavtype() {
		return this.importwavtype;
	}

	public void setImportwavtype(String importwavtype) {
		this.importwavtype = importwavtype;
	}

	public String getExportwavtype() {
		return this.exportwavtype;
	}

	public void setExportwavtype(String exportwavtype) {
		this.exportwavtype = exportwavtype;
	}

	public String getNoemaildeliverflag() {
		return this.noemaildeliverflag;
	}

	public void setNoemaildeliverflag(String noemaildeliverflag) {
		this.noemaildeliverflag = noemaildeliverflag;
	}

	String functionids;

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return this.branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getFunctionids() {
		return this.functionids;
	}

	public void setFunctionids(String functionids) {
		this.functionids = functionids;
	}

	public int getSitetype() {
		return this.sitetype;
	}

	public void setSitetype(int sitetype) {
		this.sitetype = sitetype;
	}

	public String getBankcard() {
		return this.bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getSitetypeName() {
		for (BranchEnum be : BranchEnum.values()) {
			if (be.getValue() == this.sitetype) {
				return be.getText();
			}
		}
		return "";
	}

	public long getPrescription24() {
		return this.prescription24;
	}

	public void setPrescription24(long prescription24) {
		this.prescription24 = prescription24;
	}

	public long getPrescription48() {
		return this.prescription48;
	}

	public void setPrescription48(long prescription48) {
		this.prescription48 = prescription48;
	}

	public String getBrancharea() {
		if (null == this.brancharea) {
			this.brancharea = "";
		}
		return this.brancharea;
	}

	public void setBrancharea(String brancharea) {
		this.brancharea = brancharea;
	}

	public String getBranchstreet() {
		if (null == this.branchstreet) {
			this.branchstreet = "";
		}
		return this.branchstreet;
	}

	public void setBranchstreet(String branchstreet) {
		this.branchstreet = branchstreet;
	}

	public BigDecimal getBranchBail() {
		return this.branchBail;
	}

	public void setBranchBail(BigDecimal branchBail) {
		this.branchBail = branchBail;
	}

	public long getBranchprovinceid() {
		return branchprovinceid;
	}

	public void setBranchprovinceid(long branchprovinceid) {
		this.branchprovinceid = branchprovinceid;
	}

	public long getBranchcityid() {
		return branchcityid;
	}

	public void setBranchcityid(long branchcityid) {
		this.branchcityid = branchcityid;
	}

	/**
	 * @return the tpsbranchcode
	 */
	public String getTpsbranchcode() {
		return tpsbranchcode;
	}

	/**
	 * @param tpsbranchcode the tpsbranchcode to set
	 */
	public void setTpsbranchcode(String tpsbranchcode) {
		this.tpsbranchcode = tpsbranchcode;
	}
	
	public String getBankCardNo() {
		return this.bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getBankAccountType() {
		return this.bankAccountType;
	}

	public void setBankAccountType(int bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getCftAccountNo() {
		return this.cftAccountNo;
	}

	public void setCftAccountNo(String cftAccountNo) {
		this.cftAccountNo = cftAccountNo;
	}

	public String getCftBankCode() {
		return this.cftBankCode;
	}

	public void setCftBankCode(String cftBankCode) {
		this.cftBankCode = cftBankCode;
	}

	public String getCftAccountName() {
		return this.cftAccountName;
	}

	public void setCftAccountName(String cftAccountName) {
		this.cftAccountName = cftAccountName;
	}

	public int getCftAccountProp() {
		return this.cftAccountProp;
	}

	public void setCftAccountProp(int cftAccountProp) {
		this.cftAccountProp = cftAccountProp;
	}

	public int getCftCertType() {
		return this.cftCertType;
	}

	public void setCftCertType(int cftCertType) {
		this.cftCertType = cftCertType;
	}

	public String getCftCertId() {
		return this.cftCertId;
	}

	public void setCftCertId(String cftCertId) {
		this.cftCertId = cftCertId;
	}

	public String getOutputno() {
		return outputno;
	}

	public void setOutputno(String outputno) {
		this.outputno = outputno;
	}

	public int getPayinType() {
		return payinType;
	}

	public void setPayinType(int payinType) {
		this.payinType = payinType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
