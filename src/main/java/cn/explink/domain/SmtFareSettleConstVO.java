package cn.explink.domain;

import java.util.Map;

/**
 *
 * 上门退运费结算报表前台传递VO.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class SmtFareSettleConstVO {
	private Map<Integer, String> timeTypeMap = null;

	private Integer timeType = -1;

	private Map<Long, String> venderMap = null;

	private Map<Long, String> orgMap = null;

	private Map<Long, String> deliverMap = null;

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

	public Map<Long, String> getDeliverMap() {
		return this.deliverMap;
	}

	public void setDeliverMap(Map<Long, String> deliverMap) {
		this.deliverMap = deliverMap;
	}

}
