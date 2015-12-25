package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.enumutil.BranchEnum;

public class Branch {
	private long branchid;
	private String branchname;
	private String branchprovince;
	private String branchcity;

	private String brancharea;
	private String branchstreet;

	private String branchaddress;
	private String branchcontactman;
	private String branchphone;
	private String branchmobile;
	private String branchfax;
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
	
	private String tpsbranchcode;//上传tps时所用的机构编码
	
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
	
}
