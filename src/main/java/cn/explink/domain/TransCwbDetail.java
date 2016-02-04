package cn.explink.domain;

import java.io.Serializable;

import net.sourceforge.jtds.jdbc.DateTime;

public class TransCwbDetail implements Serializable {

	private static final long serialVersionUID = 5162533003345792685L;

	private int id; // id
	private String cwb; // 订单号
	private String transcwb; // 运单号
	private int transcwbstate; // 运单状态（配送、退货等 ）
	private int transcwboptstate; // 运单操作状态（枚举值同订单操作状态 flowordertype）
	private long currentbranchid; // 当前站点
	private long previousbranchid; // 上一站id
	private long nextbranchid; // 下一站id
	private String createtime; // 创建时间（运单流入系统时间）
	private String modifiedtime; // 修改时间
	private String emaildate; // 发货时间
	private int commonphraseid; // 常用语id
	private String commonphrase; // 常用语
	
	public String getEmaildate() {
		return this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public int getCommonphraseid() {
		return this.commonphraseid;
	}

	public void setCommonphraseid(int commonphraseid) {
		this.commonphraseid = commonphraseid;
	}

	public String getCommonphrase() {
		return this.commonphrase;
	}

	public void setCommonphrase(String commonphrase) {
		this.commonphrase = commonphrase;
	}

	public static long getSerialversionuid() {
		return TransCwbDetail.serialVersionUID;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTranscwb() {
		return this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public int getTranscwbstate() {
		return this.transcwbstate;
	}

	public void setTranscwbstate(int transcwbstate) {
		this.transcwbstate = transcwbstate;
	}

	public int getTranscwboptstate() {
		return this.transcwboptstate;
	}

	public void setTranscwboptstate(int transcwboptstate) {
		this.transcwboptstate = transcwboptstate;
	}

	public long getCurrentbranchid() {
		return this.currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getPreviousbranchid() {
		return this.previousbranchid;
	}

	public void setPreviousbranchid(long previousbranchid) {
		this.previousbranchid = previousbranchid;
	}

	public long getNextbranchid() {
		return this.nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getModifiedtime() {
		return this.modifiedtime;
	}

	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

}
