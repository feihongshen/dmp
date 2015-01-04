package cn.explink.domain;

import java.util.Map;

public class OverdueConstantVO {

	private Map<Integer, String> timeTypeMap = null;

	private Integer timeType = -1;

	private Map<Long, String> venderMap = null;

	private Map<Long, String> orgMap = null;

	private Map<Integer, String> showColMap = null;

	public Map<Integer, String> getTimeTypeMap() {
		return this.timeTypeMap;
	}

	public void setTimeTypeMap(Map<Integer, String> timeTypeMap) {
		this.timeTypeMap = timeTypeMap;
	}

	public Integer getTimeType() {
		return this.timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Map<Long, String> getVenderMap() {
		return this.venderMap;
	}

	public void setVenderMap(Map<Long, String> venderMap) {
		this.venderMap = venderMap;
	}

	public Map<Long, String> getOrgMap() {
		return this.orgMap;
	}

	public void setOrgMap(Map<Long, String> orgMap) {
		this.orgMap = orgMap;
	}

	public Map<Integer, String> getShowColMap() {
		return this.showColMap;
	}

	public void setShowColMap(Map<Integer, String> showColMap) {
		this.showColMap = showColMap;
	}

}
