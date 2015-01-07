package cn.explink.domain;

import java.util.ArrayList;

import net.sf.json.JSONObject;

/**
 * 上门退用结算报表条件VO(站点，小件元两个维度)
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class SmtFareSettleCondVO {

	private int optTimeType = -1;

	private String startTime = "";

	private String endTime = "";

	private ArrayList<Long> orgs = new ArrayList<Long>();

	private ArrayList<Long> venders = new ArrayList<Long>();

	private long deliverId = 0;

	public SmtFareSettleCondVO() {

	}

	public SmtFareSettleCondVO(JSONObject jsonObject) {
		this.optTimeType = jsonObject.getInt("optTimeType");
		this.startTime = jsonObject.getString("startTime");
		this.endTime = jsonObject.getString("endTime");
	}

	public int getOptTimeType() {
		return this.optTimeType;
	}

	public void setOptTimeType(int optTimeType) {
		this.optTimeType = optTimeType;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public ArrayList<Long> getOrgs() {
		return this.orgs;
	}

	public void setOrgs(ArrayList<Long> orgs) {
		this.orgs = orgs;
	}

	public ArrayList<Long> getVenders() {
		return this.venders;
	}

	public void setVenders(ArrayList<Long> venders) {
		this.venders = venders;
	}

	public long getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(long deliverId) {
		this.deliverId = deliverId;
	}

}
