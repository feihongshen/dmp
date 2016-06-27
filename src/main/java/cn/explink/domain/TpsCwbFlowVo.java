package cn.explink.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class TpsCwbFlowVo {
	private String cwb;
	private String scancwb;
	private long flowordertype;
	private String errinfo;
	private int state;
	private int trytime;
	private Timestamp createtime;
	private int sendemaildate;//发送出仓时间,1发送,0不发送
	private int sendweight;//发送重量体积,1发送,0不发送
	private BigDecimal weight;//重量
	private BigDecimal volume;//体积
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getScancwb() {
		return scancwb;
	}
	public void setScancwb(String scancwb) {
		this.scancwb = scancwb;
	}
	public long getFlowordertype() {
		return flowordertype;
	}
	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}
	public String getErrinfo() {
		return errinfo;
	}
	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getTrytime() {
		return trytime;
	}
	public void setTrytime(int trytime) {
		this.trytime = trytime;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public int getSendemaildate() {
		return sendemaildate;
	}
	public void setSendemaildate(int sendemaildate) {
		this.sendemaildate = sendemaildate;
	}
	public int getSendweight() {
		return sendweight;
	}
	public void setSendweight(int sendweight) {
		this.sendweight = sendweight;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	
}
