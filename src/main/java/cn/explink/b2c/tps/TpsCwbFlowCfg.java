package cn.explink.b2c.tps;

public class TpsCwbFlowCfg {
	private int openFlag; //是否开启对接: 0不开启;1开启
	private String customerids;//唯品会客户ids 多个以英文逗号隔开
	private int maxTryTime; //最大尝试推送次数
	private int housekeepDay;//临时表数据保留天数
	private int maxDataSize;//每次取多少条数据
	
	public String getCustomerids() {
		return customerids;
	}
	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}
	public int getMaxTryTime() {
		return maxTryTime;
	}
	public void setMaxTryTime(int maxTryTime) {
		this.maxTryTime = maxTryTime;
	}
	public int getHousekeepDay() {
		return housekeepDay;
	}
	public void setHousekeepDay(int housekeepDay) {
		this.housekeepDay = housekeepDay;
	}
	public int getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}
	public int getMaxDataSize() {
		return maxDataSize;
	}
	public void setMaxDataSize(int maxDataSize) {
		this.maxDataSize = maxDataSize;
	}
	
	
}
