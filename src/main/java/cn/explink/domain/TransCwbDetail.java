package cn.explink.domain;

import java.io.Serializable;

public class TransCwbDetail implements Serializable{
	
	private static final long serialVersionUID = 5162533003345792685L;
	
	
	private int id; //id
	private String cwb; //订单号
	private String transcwb; //运单号
	private int transcwbstate; //运单状态（配送、退货等 ）
	private int transcwboptstate; //运单操作状态（枚举值同订单操作状态 flowordertype）
	private int currentbranchid; //当前站点
	private int previousbranchid; //上一站id
	private int nextbranchid;  //下一站id
	private String createtime;  //创建时间（运单流入系统时间）
	private String modifiedtime; //修改时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public int getTranscwbstate() {
		return transcwbstate;
	}
	public void setTranscwbstate(int transcwbstate) {
		this.transcwbstate = transcwbstate;
	}
	public int getTranscwboptstate() {
		return transcwboptstate;
	}
	public void setTranscwboptstate(int transcwboptstate) {
		this.transcwboptstate = transcwboptstate;
	}
	public int getCurrentbranchid() {
		return currentbranchid;
	}
	public void setCurrentbranchid(int currentbranchid) {
		this.currentbranchid = currentbranchid;
	}
	public int getPreviousbranchid() {
		return previousbranchid;
	}
	public void setPreviousbranchid(int previousbranchid) {
		this.previousbranchid = previousbranchid;
	}
	public int getNextbranchid() {
		return nextbranchid;
	}
	public void setNextbranchid(int nextbranchid) {
		this.nextbranchid = nextbranchid;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getModifiedtime() {
		return modifiedtime;
	}
	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}
	
	

}
