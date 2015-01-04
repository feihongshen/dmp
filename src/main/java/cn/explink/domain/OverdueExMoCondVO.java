package cn.explink.domain;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 超期异常监控条件VO.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class OverdueExMoCondVO {
	private int optTimeType = -1;

	private String startTime = "";

	private String endTime = "";

	private ArrayList<Long> orgs = new ArrayList<Long>();

	private Long venderId = Long.valueOf(0);

	private ArrayList<Integer> showCols = new ArrayList<Integer>();

	private boolean enableTEQuery = false;

	public OverdueExMoCondVO() {
	}

	public OverdueExMoCondVO(JSONObject jsonObject) {
		this.optTimeType = jsonObject.getInt("optTimeType");
		this.startTime = jsonObject.getString("startTime");
		this.endTime = jsonObject.getString("endTime");
		this.orgs = this.getLongArrayList(jsonObject, "orgs");
		this.venderId = jsonObject.getLong("venderId");
		this.showCols = this.getIntegerArrayList(jsonObject, "showCols");
		this.enableTEQuery = jsonObject.getBoolean("enableTEQuery");
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

	public Long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	public ArrayList<Integer> getShowCols() {
		return this.showCols;
	}

	public void setShowCols(ArrayList<Integer> showCols) {
		this.showCols = showCols;
	}

	public boolean isEnableTEQuery() {
		return this.enableTEQuery;
	}

	public void setEnableTEQuery(boolean enableTEQuery) {
		this.enableTEQuery = enableTEQuery;
	}

	private ArrayList<Long> getLongArrayList(JSONObject jsonObject, String name) {
		JSONArray array = jsonObject.getJSONArray(name);
		ArrayList<Long> arrayList = new ArrayList<Long>();
		for (int i = 0; i < array.size(); i++) {
			arrayList.add(array.getLong(i));
		}
		return arrayList;
	}

	private ArrayList<Integer> getIntegerArrayList(JSONObject jsonObject, String name) {
		JSONArray array = jsonObject.getJSONArray(name);
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for (int i = 0; i < array.size(); i++) {
			arrayList.add(array.getInt(i));
		}
		return arrayList;
	}

}
