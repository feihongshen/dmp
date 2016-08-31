package cn.explink.domain;

/**
 * 武汉飞远运输交接单打印VO
 * @date 2016年8月11日 下午4:07:07
 */
public class WuhanTransportDetailVo {
	
	/**
	 * 本站
	 */
	private String branchname;
	
	/**
	 * 下一张站
	 */
	private String nextBranchname;
	
	/**
	 * 出库开始时间
	 */
	private String outstockStartTime;
	
	/**
	 * 出库结束时间
	 */
	private String outstockEndTime;
	
	/**
	 * 出库单数
	 */
	private int outstockOrderNum;
	
	/**
	 * 出库件数
	 */
	private int outstockSendNum;

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getNextBranchname() {
		return nextBranchname;
	}

	public void setNextBranchname(String nextBranchname) {
		this.nextBranchname = nextBranchname;
	}

	public String getOutstockStartTime() {
		return outstockStartTime;
	}

	public void setOutstockStartTime(String outstockStartTime) {
		this.outstockStartTime = outstockStartTime;
	}

	public String getOutstockEndTime() {
		return outstockEndTime;
	}

	public void setOutstockEndTime(String outstockEndTime) {
		this.outstockEndTime = outstockEndTime;
	}

	public int getOutstockOrderNum() {
		return outstockOrderNum;
	}

	public void setOutstockOrderNum(int outstockOrderNum) {
		this.outstockOrderNum = outstockOrderNum;
	}

	public int getOutstockSendNum() {
		return outstockSendNum;
	}

	public void setOutstockSendNum(int outstockSendNum) {
		this.outstockSendNum = outstockSendNum;
	}

	@Override
	public String toString() {
		return "WuhanTransportDetailVo [branchname=" + branchname + ", nextBranchname=" + nextBranchname
				+ ", outstockStartTime=" + outstockStartTime + ", outstockEndTime=" + outstockEndTime
				+ ", outstockOrderNum=" + outstockOrderNum + ", outstockSendNum=" + outstockSendNum + "]";
	}
}
