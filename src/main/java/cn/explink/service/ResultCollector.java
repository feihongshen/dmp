package cn.explink.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultCollector {

	private String id;

	private Date createTime = new Date();

	private boolean isFinished;

	private boolean isStoped = false;

	private int successParsNum;

	private int successSavcNum;

	private int failParsNum;

	private int failSavcNum;

	private long emaildateid;

	private List<String> errors = new ArrayList<String>();

	public List<String> getErrors() {
		return errors;
	}

	public void addError(String identifier, String reason) {
		this.errors.add(identifier + ":" + reason);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSuccessParsNum() {
		return successParsNum;
	}

	public void setSuccessParsNum(int successParsNum) {
		this.successParsNum = successParsNum;
	}

	public int getSuccessSavcNum() {
		return successSavcNum;
	}

	public void setSuccessSavcNum(int successSavcNum) {
		this.successSavcNum = successSavcNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isStoped() {
		return isStoped;
	}

	public void setStoped(boolean isStoped) {
		this.isStoped = isStoped;
	}

	public int getFailParsNum() {
		return failParsNum;
	}

	public void setFailParsNum(int failParsNum) {
		this.failParsNum = failParsNum;
	}

	public int getFailSavcNum() {
		return failSavcNum;
	}

	public void setFailSavcNum(int failSavcNum) {
		this.failSavcNum = failSavcNum;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

}
