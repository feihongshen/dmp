package cn.explink.b2c.tmall;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 天猫配送时间
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmallTimeRole {

	private int stime; // 开始时间
	private int etime; // 结束时间
	private String role; // 描述

	public int getStime() {
		return stime;
	}

	public void setStime(int stime) {
		this.stime = stime;
	}

	public int getEtime() {
		return etime;
	}

	public void setEtime(int etime) {
		this.etime = etime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
