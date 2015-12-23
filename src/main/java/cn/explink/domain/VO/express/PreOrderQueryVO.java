package cn.explink.domain.VO.express;


/**
 *
 * @description 快递业务预订单查询VO
 * @author  刘武强
 * @data   2015年8月3日
 */
public class PreOrderQueryVO {
	/*
	 * 生成时间-起始
	 */
	private String start;
	/*
	 * 生成时间-结束
	 */
	private String end;
	/*
	 * 预订单编号
	 */
	private String preordercode;
	/*
	 * 执行状态
	 */
	private String excuteState;
	/*
	 * 寄件人
	 */
	private String sender;
	/*
	 * 手机号
	 */
	private String mobile;
	/*
	 * 站点
	 */
	private String station;

	public String getStart() {
		return this.start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return this.end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getPreordercode() {
		return this.preordercode;
	}

	public void setPreordercode(String preordercode) {
		this.preordercode = preordercode;
	}

	public String getExcuteState() {
		return this.excuteState;
	}

	public void setExcuteState(String excuteState) {
		this.excuteState = excuteState;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStation() {
		return this.station;
	}

	public void setStation(String station) {
		this.station = station;
	}

}
