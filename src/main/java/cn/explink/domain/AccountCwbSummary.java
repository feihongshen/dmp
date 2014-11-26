package cn.explink.domain;

import java.math.BigDecimal;

public class AccountCwbSummary {
	private long summaryid;
	private long checkoutstate;
	private long accounttype;
	private String memo;
	private String createtime;
	private long userid;
	private String savecreatetime;
	private long saveuserid;
	private BigDecimal otheraddfee;
	private BigDecimal othersubtractfee;
	private long zznums;
	private BigDecimal zzcash;
	private long thnums;
	private BigDecimal thcash;
	private long tonums;
	private BigDecimal tocash;
	private BigDecimal topos;
	private long wjnums;
	private BigDecimal wjcash;
	private long yjnums;
	private BigDecimal yjcash;
	private BigDecimal yjpos;
	private long hjnums;
	private BigDecimal hjfee;
	private BigDecimal feetransfer;
	private BigDecimal feecash;
	private BigDecimal feepos;
	private BigDecimal feecheck;
	private String usertransfer;
	private String usercash;
	private String userpos;
	private String usercheck;
	private String cardtransfer;
	private long branchid;
	private BigDecimal yjcheck;
	private BigDecimal yjother;
	private BigDecimal wjpos;
	private BigDecimal wjcheck;
	private BigDecimal wjother;
	private BigDecimal tocheck;
	private BigDecimal toother;
	private long qknums;
	private BigDecimal qkcash;
	// private BigDecimal qkpos;
	// private BigDecimal qkcheck;
	// private BigDecimal qkother;

	private BigDecimal tofee;// 共交合计金额
	private BigDecimal yjfee;// 应交合计
	private BigDecimal wjfee;// 未交合计
	private long otheraddnums;// 加款数
	private long othersubnums;// 减款数
	// private String zzids;
	// private String thids;
	// private String wjids;
	// private String yjids;
	// private String qkids;
	private BigDecimal poscash;
	private long posnums;

	// 不在数据库字段
	private String branchname;
	private String username;

	public long getSummaryid() {
		return summaryid;
	}

	public void setSummaryid(long summaryid) {
		this.summaryid = summaryid;
	}

	public long getCheckoutstate() {
		return checkoutstate;
	}

	public void setCheckoutstate(long checkoutstate) {
		this.checkoutstate = checkoutstate;
	}

	public long getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(long accounttype) {
		this.accounttype = accounttype;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getSavecreatetime() {
		return savecreatetime;
	}

	public void setSavecreatetime(String savecreatetime) {
		this.savecreatetime = savecreatetime;
	}

	public long getSaveuserid() {
		return saveuserid;
	}

	public void setSaveuserid(long saveuserid) {
		this.saveuserid = saveuserid;
	}

	public BigDecimal getOtheraddfee() {
		return otheraddfee;
	}

	public void setOtheraddfee(BigDecimal otheraddfee) {
		this.otheraddfee = otheraddfee;
	}

	public BigDecimal getOthersubtractfee() {
		return othersubtractfee;
	}

	public void setOthersubtractfee(BigDecimal othersubtractfee) {
		this.othersubtractfee = othersubtractfee;
	}

	public long getZznums() {
		return zznums;
	}

	public void setZznums(long zznums) {
		this.zznums = zznums;
	}

	public BigDecimal getZzcash() {
		return zzcash;
	}

	public void setZzcash(BigDecimal zzcash) {
		this.zzcash = zzcash;
	}

	public long getThnums() {
		return thnums;
	}

	public void setThnums(long thnums) {
		this.thnums = thnums;
	}

	public BigDecimal getThcash() {
		return thcash;
	}

	public void setThcash(BigDecimal thcash) {
		this.thcash = thcash;
	}

	public long getTonums() {
		return tonums;
	}

	public void setTonums(long tonums) {
		this.tonums = tonums;
	}

	public BigDecimal getTocash() {
		return tocash;
	}

	public void setTocash(BigDecimal tocash) {
		this.tocash = tocash;
	}

	public BigDecimal getTopos() {
		return topos;
	}

	public void setTopos(BigDecimal topos) {
		this.topos = topos;
	}

	public long getWjnums() {
		return wjnums;
	}

	public void setWjnums(long wjnums) {
		this.wjnums = wjnums;
	}

	public BigDecimal getWjcash() {
		return wjcash;
	}

	public void setWjcash(BigDecimal wjcash) {
		this.wjcash = wjcash;
	}

	public long getYjnums() {
		return yjnums;
	}

	public void setYjnums(long yjnums) {
		this.yjnums = yjnums;
	}

	public BigDecimal getYjcash() {
		return yjcash;
	}

	public void setYjcash(BigDecimal yjcash) {
		this.yjcash = yjcash;
	}

	public BigDecimal getYjpos() {
		return yjpos;
	}

	public void setYjpos(BigDecimal yjpos) {
		this.yjpos = yjpos;
	}

	public long getHjnums() {
		return hjnums;
	}

	public void setHjnums(long hjnums) {
		this.hjnums = hjnums;
	}

	public BigDecimal getHjfee() {
		return hjfee;
	}

	public void setHjfee(BigDecimal hjfee) {
		this.hjfee = hjfee;
	}

	public BigDecimal getFeetransfer() {
		return feetransfer;
	}

	public void setFeetransfer(BigDecimal feetransfer) {
		this.feetransfer = feetransfer;
	}

	public BigDecimal getFeecash() {
		return feecash;
	}

	public void setFeecash(BigDecimal feecash) {
		this.feecash = feecash;
	}

	public BigDecimal getFeepos() {
		return feepos;
	}

	public void setFeepos(BigDecimal feepos) {
		this.feepos = feepos;
	}

	public BigDecimal getFeecheck() {
		return feecheck;
	}

	public void setFeecheck(BigDecimal feecheck) {
		this.feecheck = feecheck;
	}

	public String getUsertransfer() {
		return usertransfer;
	}

	public void setUsertransfer(String usertransfer) {
		this.usertransfer = usertransfer;
	}

	public String getUsercash() {
		return usercash;
	}

	public void setUsercash(String usercash) {
		this.usercash = usercash;
	}

	public String getUserpos() {
		return userpos;
	}

	public void setUserpos(String userpos) {
		this.userpos = userpos;
	}

	public String getUsercheck() {
		return usercheck;
	}

	public void setUsercheck(String usercheck) {
		this.usercheck = usercheck;
	}

	public String getCardtransfer() {
		return cardtransfer;
	}

	public void setCardtransfer(String cardtransfer) {
		this.cardtransfer = cardtransfer;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public BigDecimal getYjcheck() {
		return yjcheck;
	}

	public void setYjcheck(BigDecimal yjcheck) {
		this.yjcheck = yjcheck;
	}

	public BigDecimal getYjother() {
		return yjother;
	}

	public void setYjother(BigDecimal yjother) {
		this.yjother = yjother;
	}

	public BigDecimal getWjpos() {
		return wjpos;
	}

	public void setWjpos(BigDecimal wjpos) {
		this.wjpos = wjpos;
	}

	public BigDecimal getWjcheck() {
		return wjcheck;
	}

	public void setWjcheck(BigDecimal wjcheck) {
		this.wjcheck = wjcheck;
	}

	public BigDecimal getWjother() {
		return wjother;
	}

	public void setWjother(BigDecimal wjother) {
		this.wjother = wjother;
	}

	public BigDecimal getTocheck() {
		return tocheck;
	}

	public void setTocheck(BigDecimal tocheck) {
		this.tocheck = tocheck;
	}

	public BigDecimal getToother() {
		return toother;
	}

	public void setToother(BigDecimal toother) {
		this.toother = toother;
	}

	public long getQknums() {
		return qknums;
	}

	public void setQknums(long qknums) {
		this.qknums = qknums;
	}

	public BigDecimal getQkcash() {
		return qkcash;
	}

	public void setQkcash(BigDecimal qkcash) {
		this.qkcash = qkcash;
	}

	public BigDecimal getTofee() {
		return tofee;
	}

	public void setTofee(BigDecimal tofee) {
		this.tofee = tofee;
	}

	public BigDecimal getYjfee() {
		return yjfee;
	}

	public void setYjfee(BigDecimal yjfee) {
		this.yjfee = yjfee;
	}

	public BigDecimal getWjfee() {
		return wjfee;
	}

	public void setWjfee(BigDecimal wjfee) {
		this.wjfee = wjfee;
	}

	public long getOtheraddnums() {
		return otheraddnums;
	}

	public void setOtheraddnums(long otheraddnums) {
		this.otheraddnums = otheraddnums;
	}

	public long getOthersubnums() {
		return othersubnums;
	}

	public void setOthersubnums(long othersubnums) {
		this.othersubnums = othersubnums;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getPoscash() {
		return poscash;
	}

	public void setPoscash(BigDecimal poscash) {
		this.poscash = poscash;
	}

	public long getPosnums() {
		return posnums;
	}

	public void setPosnums(long posnums) {
		this.posnums = posnums;
	}

}
