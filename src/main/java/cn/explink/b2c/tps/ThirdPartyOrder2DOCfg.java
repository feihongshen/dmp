package cn.explink.b2c.tps;

public class ThirdPartyOrder2DOCfg {
	private int  openFlag; // 是否开启对接 0.不开启;1.开启
	private String customerids;//客户ids 多个以英文逗号隔开
	private int maxTryTime; //最大尝试推送次数
	private String carrierCode; //承运商编码
	
	private int trackOpenFlag; // 是否开启轨迹对接 0.不开启;1.开启
	private int trackMaxTryTime; //轨迹最大尝试推送次数
	private int trackHousekeepDay;//外单轨迹临时表数据保留天数
	private int housekeepDay;//外单临时表数据保留天数
	
	public int getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}
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
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	public int getTrackOpenFlag() {
		return trackOpenFlag;
	}
	public void setTrackOpenFlag(int trackOpenFlag) {
		this.trackOpenFlag = trackOpenFlag;
	}
	public int getTrackMaxTryTime() {
		return trackMaxTryTime;
	}
	public void setTrackMaxTryTime(int trackMaxTryTime) {
		this.trackMaxTryTime = trackMaxTryTime;
	}
	public int getHousekeepDay() {
		return housekeepDay;
	}
	public void setHousekeepDay(int housekeepDay) {
		this.housekeepDay = housekeepDay;
	}
	public int getTrackHousekeepDay() {
		return trackHousekeepDay;
	}
	public void setTrackHousekeepDay(int trackHousekeepDay) {
		this.trackHousekeepDay = trackHousekeepDay;
	}
	
}
