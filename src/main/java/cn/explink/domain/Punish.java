package cn.explink.domain;

import java.math.BigDecimal;

public class Punish {
	private long id;
	private String cwb;
	private long punishid;
	private long branchid;
	private long userid;
	private long punishtime;
	private long punishlevel;
	private BigDecimal punishfee;
	private BigDecimal realfee;
	private String punishcontent;
	private long createuser;
	private String createtime;
	private int state;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getPunishid() {
		return this.punishid;
	}

	public void setPunishid(long punishid) {
		this.punishid = punishid;
	}

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getPunishtime() {
		return this.punishtime;
	}

	public void setPunishtime(long punishtime) {
		this.punishtime = punishtime;
	}

	public long getPunishlevel() {
		return this.punishlevel;
	}

	public void setPunishlevel(long punishlevel) {
		this.punishlevel = punishlevel;
	}

	public BigDecimal getPunishfee() {
		return this.punishfee;
	}

	public void setPunishfee(BigDecimal punishfee) {
		this.punishfee = punishfee;
	}

	public BigDecimal getRealfee() {
		return this.realfee;
	}

	public void setRealfee(BigDecimal realfee) {
		this.realfee = realfee;
	}

	public String getPunishcontent() {
		return this.punishcontent;
	}

	public void setPunishcontent(String punishcontent) {
		this.punishcontent = punishcontent;
	}

	public long getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(long createuser) {
		this.createuser = createuser;
	}

	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
