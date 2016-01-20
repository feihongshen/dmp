package cn.explink.b2c.tps;

public class ThirdPartyOrder2DOCfg {
	private int  openFlag; // 是否开启对接 0.不开启;1.开启
	private String customerids;//客户ids 多个以英文逗号隔开
	private int maxTryTime; //最大尝试推送次数
	private String carrierCode; //承运商编码
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
	
	
}
