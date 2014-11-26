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

	public long getBacktime() {
		return backtime;
	}

	public void setBacktime(long backtime) {
		this.backtime = backtime;
	}

	public BigDecimal getBalancevirt() {
		return balancevirt;
	}

	public void setBalancevirt(BigDecimal balancevirt) {
		this.balancevirt = balancevirt;
	}

	public BigDecimal getDebtvirt() {
		return debtvirt;
	}

	public void setDebtvirt(BigDecimal debtvirt) {
		this.debtvirt = debtvirt;
	}

	public BigDecimal getCredituse() {
		return credituse;
	}

	public void setCredituse(BigDecimal credituse) {
		this.credituse = credituse;
	}

	public BigDecimal getDebt() {
		return debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public long getAccountbranch() {
		return accountbranch;
	}

	public void setAccountbranch(long accountbranch) {
		this.accountbranch = accountbranch;
	}

	public int getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(int accounttype) {
		this.accounttype = accounttype;
	}

	public int getAccountexcesstype() {
		return accountexcesstype;
	}

	public void setAccountexcesstype(int accountexcesstype) {
		this.accountexcesstype = accountexcesstype;
	}

	public BigDecimal getAccountexcessfee() {
		return accountexcessfee;
	}

	public void setAccountexcessfee(BigDecimal accountexcessfee) {
		this.accountexcessfee = accountexcessfee;
	}

	public int getBindmsksid() {
		return bindmsksid;
	}

	public void setBindmsksid(int bindmsksid) {
		this.bindmsksid = bindmsksid;
	}

	public BigDecimal getArrearagepayupaudit() {
		return arrearagepayupaudit;
	}

	public void setArrearagepayupaudit(BigDecimal arrearagepayupaudit) {
		this.arrearagepayupaudit = arrearagepayupaudit;
	}

	public BigDecimal getPosarrearagepayupaudit() {
		return posarrearagepayupaudit;
	}

	public void setPosarrearagepayupaudit(BigDecimal posarrearagepayupaudit) {
		this.posarrearagepayupaudit = posarrearagepayupaudit;
	}

	public long getZhongzhuanid() {
		return zhongzhuanid;
	}

	public void setZhongzhuanid(long zhongzhuanid) {
		this.zhongzhuanid = zhongzhuanid;
	}

	public long getTuihuoid() {
		return tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	public long getCaiwuid() {
		return caiwuid;
	}

	public void setCaiwuid(long caiwuid) {
		this.caiwuid = caiwuid;
	}

	public BigDecimal getArrearagehuo() {
		return arrearagehuo;
	}

	public void setArrearagehuo(BigDecimal arrearagehuo) {
		this.arrearagehuo = arrearagehuo;
	}

	public BigDecimal getArrearagepei() {
		return arrearagepei;
	}

	public void setArrearagepei(BigDecimal arrearagepei) {
		this.arrearagepei = arrearagepei;
	}

	public BigDecimal getArrearagefa() {
		return arrearagefa;
	}

	public void setArrearagefa(BigDecimal arrearagefa) {
		this.arrearagefa = arrearagefa;
	}

	public String getBranchmatter() {
		return branchmatter;
	}

	public void setBranchmatter(String branchmatter) {
		this.branchmatter = branchmatter;
	}

	public int getAccountareaid() {
		return accountareaid;
	}

	public void setAccountareaid(int accountareaid) {
		this.accountareaid = accountareaid;
	}

	public int getCheckremandtype() {
		return checkremandtype;
	}

	public void setCheckremandtype(int checkremandtype) {
		this.checkremandtype = checkremandtype;
	}

	public int getSendstartbranchid() {
		return sendstartbranchid;
	}

	public void setSendstartbranchid(int sendstartbranchid) {
		this.sendstartbranchid = sendstartbranchid;
	}

	public String getBranchprovince() {
		return branchprovince;
	}

	public void setBranchprovince(String branchprovince) {
		this.branchprovince = branchprovince;
	}

	public String getBranchcity() {
		return branchcity;
	}

	public void setBranchcity(String branchcity) {
		this.branchcity = branchcity;
	}

	public String getBranchaddress() {
		return branchaddress;
	}

	public void setBranchaddress(String branchaddress) {
		this.branchaddress = branchaddress;
	}

	public String getBranchcontactman() {
		return branchcontactman;
	}

	public void setBranchcontactman(String branchcontactman) {
		this.branchcontactman = branchcontactman;
	}

	public String getBranchphone() {
		return branchphone;
	}

	public void setBranchphone(String branchphone) {
		this.branchphone = branchphone;
	}

	public String getBranchmobile() {
		return branchmobile;
	}

	public void setBranchmobile(String branchmobile) {
		this.branchmobile = branchmobile;
	}

	public String getBranchfax() {
		return branchfax;
	}

	public void setBranchfax(String branchfax) {
		this.branchfax = branchfax;
	}

	public String getBranchemail() {
		return branchemail;
	}

	public void setBranchemail(String branchemail) {
		this.branchemail = branchemail;
	}

	public String getContractflag() {
		return contractflag;
	}

	public void setContractflag(String contractflag) {
		this.contractflag = contractflag;
	}

	public String getCwbtobranchid() {
		return cwbtobranchid;
	}

	public void setCwbtobranchid(String cwbtobranchid) {
		this.cwbtobranchid = cwbtobranchid;
	}

	public String getPayfeeupdateflag() {
		return payfeeupdateflag;
	}

	public void setPayfeeupdateflag(String payfeeupdateflag) {
		this.payfeeupdateflag = payfeeupdateflag;
	}

	public String getBacktodeliverflag() {
		return backtodeliverflag;
	}

	public void setBacktodeliverflag(String backtodeliverflag) {
		this.backtodeliverflag = backtodeliverflag;
	}

	public String getBranchpaytoheadflag() {
		return branchpaytoheadflag;
	}

	public void setBranchpaytoheadflag(String branchpaytoheadflag) {
		this.branchpaytoheadflag = branchpaytoheadflag;
	}

	public String getBranchfinishdayflag() {
		return branchfinishdayflag;
	}

	public void setBranchfinishdayflag(String branchfinishdayflag) {
		this.branchfinishdayflag = branchfinishdayflag;
	}

	public BigDecimal getBranchinsurefee() {
		return branchinsurefee;
	}

	public void setBranchinsurefee(BigDecimal branchinsurefee) {
		this.branchinsurefee = branchinsurefee;
	}

	public String getBranchwavfile() {
		return branchwavfile;
	}

	public void setBranchwavfile(String branchwavfile) {
		this.branchwavfile = branchwavfile;
	}

	public BigDecimal getCreditamount() {
		return creditamount;
	}

	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}

	public String getBrancheffectflag() {
		return brancheffectflag;
	}

	public void setBrancheffectflag(String brancheffectflag) {
		this.brancheffectflag = brancheffectflag;
	}

	public BigDecimal getContractrate() {
		return contractrate;
	}

	public void setContractrate(BigDecimal contractrate) {
		this.contractrate = contractrate;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getNoemailimportflag() {
		return noemailimportflag;
	}

	public void setNoemailimportflag(String noemailimportflag) {
		this.noemailimportflag = noemailimportflag;
	}

	public String getErrorcwbdeliverflag() {
		return errorcwbdeliverflag;
	}

	public void setErrorcwbdeliverflag(String errorcwbdeliverflag) {
		this.errorcwbdeliverflag = errorcwbdeliverflag;
	}

	public String getErrorcwbbranchflag() {
		return errorcwbbranchflag;
	}

	public void setErrorcwbbranchflag(String errorcwbbranchflag) {
		this.errorcwbbranchflag = errorcwbbranchflag;
	}

	public String getBranchcodewavfile() {
		return branchcodewavfile;
	}

	public void setBranchcodewavfile(String branchcodewavfile) {
		this.branchcodewavfile = branchcodewavfile;
	}

	public String getImportwavtype() {
		return importwavtype;
	}

	public void setImportwavtype(String importwavtype) {
		this.importwavtype = importwavtype;
	}

	public String getExportwavtype() {
		return exportwavtype;
	}

	public void setExportwavtype(String exportwavtype) {
		this.exportwavtype = exportwavtype;
	}

	public String getNoemaildeliverflag() {
		return noemaildeliverflag;
	}

	public void setNoemaildeliverflag(String noemaildeliverflag) {
		this.noemaildeliverflag = noemaildeliverflag;
	}

	String functionids;

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getFunctionids() {
		return functionids;
	}

	public void setFunctionids(String functionids) {
		this.functionids = functionids;
	}

	public int getSitetype() {
		return sitetype;
	}

	public void setSitetype(int sitetype) {
		this.sitetype = sitetype;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getSitetypeName() {
		for (BranchEnum be : BranchEnum.values()) {
			if (be.getValue() == this.sitetype)
				return be.getText();
		}
		return "";
	}

	public long getPrescription24() {
		return prescription24;
	}

	public void setPrescription24(long prescription24) {
		this.prescription24 = prescription24;
	}

	public long getPrescription48() {
		return prescription48;
	}

	public void setPrescription48(long prescription48) {
		this.prescription48 = prescription48;
	}

	public String getBrancharea() {
		if (null == brancharea) {
			brancharea = "";
		}
		return brancharea;
	}

	public void setBrancharea(String brancharea) {
		this.brancharea = brancharea;
	}

	public String getBranchstreet() {
		if (null == branchstreet) {
			branchstreet = "";
		}
		return branchstreet;
	}

	public void setBranchstreet(String branchstreet) {
		this.branchstreet = branchstreet;
	}

}
