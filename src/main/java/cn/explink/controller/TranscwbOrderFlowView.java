package cn.explink.controller;

import java.util.Date;

import cn.explink.domain.User;

public class TranscwbOrderFlowView {
	private long id;
	private String scancwb;
	private Date createDate;
	private User operator;
	private String detail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScancwb() {
		return scancwb;
	}

	public void setScancwb(String scancwb) {
		this.scancwb = scancwb;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
