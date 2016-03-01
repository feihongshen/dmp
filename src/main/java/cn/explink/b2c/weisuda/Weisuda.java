package cn.explink.b2c.weisuda;

public class Weisuda {
	private String code;
	private String v;
	private String secret;
	private String pushOrders_URL;
	private String UnVerifyOrders_URL;
	private String updateUnVerifyOrders_URL;
	private String updateOrders_URL;
	private String siteUpdate_URL;
	private String siteDel_URL;
	private String courierUpdate_URL;
	private String carrierDel_URL;
	private String unboundOrders_URL;
	private String getback_boundOrders_URL;
	private String getback_getAppOrders_URL;
	private String getback_confirmAppOrders_URL;
	private String getback_updateOrders_URL;
	private String nums;
	private String count;
	private int openbatchflag; //0 默认 是单条推送   1是批量推送
	private int maxBoundCount; //绑定关系每次最大推送数量
	private String customers;
	private int changeBranchcode;//是否修改机构编码  0 关闭   1开启
	private int isSend;//是否发送订单（唯速达）  0 关闭   1开启
	
	
	
	
	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}

	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
	}

	public int getMaxBoundCount() {
		return maxBoundCount;
	}

	public void setMaxBoundCount(int maxBoundCount) {
		this.maxBoundCount = maxBoundCount;
	}

	public int getOpenbatchflag() {
		return openbatchflag;
	}

	public void setOpenbatchflag(int openbatchflag) {
		this.openbatchflag = openbatchflag;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getV() {
		return this.v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getPushOrders_URL() {
		return this.pushOrders_URL;
	}

	public String getUnVerifyOrders_URL() {
		return this.UnVerifyOrders_URL;
	}

	public String getUpdateUnVerifyOrders_URL() {
		return this.updateUnVerifyOrders_URL;
	}

	public void setPushOrders_URL(String pushOrders_URL) {
		this.pushOrders_URL = pushOrders_URL;
	}

	public void setUnVerifyOrders_URL(String unVerifyOrders_URL) {
		this.UnVerifyOrders_URL = unVerifyOrders_URL;
	}

	public void setUpdateUnVerifyOrders_URL(String updateUnVerifyOrders_URL) {
		this.updateUnVerifyOrders_URL = updateUnVerifyOrders_URL;
	}

	public String getUpdateOrders_URL() {
		return this.updateOrders_URL;
	}

	public void setUpdateOrders_URL(String updateOrders_URL) {
		this.updateOrders_URL = updateOrders_URL;
	}

	public String getSiteUpdate_URL() {
		return this.siteUpdate_URL;
	}

	public void setSiteUpdate_URL(String siteUpdate_URL) {
		this.siteUpdate_URL = siteUpdate_URL;
	}

	public String getSiteDel_URL() {
		return this.siteDel_URL;
	}

	public void setSiteDel_URL(String siteDel_URL) {
		this.siteDel_URL = siteDel_URL;
	}

	public String getCourierUpdate_URL() {
		return this.courierUpdate_URL;
	}

	public void setCourierUpdate_URL(String courierUpdate_URL) {
		this.courierUpdate_URL = courierUpdate_URL;
	}

	public String getCarrierDel_URL() {
		return this.carrierDel_URL;
	}

	public void setCarrierDel_URL(String carrierDel_URL) {
		this.carrierDel_URL = carrierDel_URL;
	}

	/**
	 * @return the unboundOrders_URL
	 */
	public String getUnboundOrders_URL() {
		return this.unboundOrders_URL;
	}

	/**
	 * @param unboundOrders_URL
	 *            the unboundOrders_URL to set
	 */
	public void setUnboundOrders_URL(String unboundOrders_URL) {
		this.unboundOrders_URL = unboundOrders_URL;
	}

	/**
	 * @return the getback_boundOrders_URL
	 */
	public String getGetback_boundOrders_URL() {
		return this.getback_boundOrders_URL;
	}

	/**
	 * @param getback_boundOrders_URL the getback_boundOrders_URL to set
	 */
	public void setGetback_boundOrders_URL(String getback_boundOrders_URL) {
		this.getback_boundOrders_URL = getback_boundOrders_URL;
	}

	/**
	 * @return the getback_getAppOrders_URL
	 */
	public String getGetback_getAppOrders_URL() {
		return this.getback_getAppOrders_URL;
	}

	/**
	 * @param getback_getAppOrders_URL the getback_getAppOrders_URL to set
	 */
	public void setGetback_getAppOrders_URL(String getback_getAppOrders_URL) {
		this.getback_getAppOrders_URL = getback_getAppOrders_URL;
	}

	/**
	 * @return the getback_confirmAppOrders_URL
	 */
	public String getGetback_confirmAppOrders_URL() {
		return this.getback_confirmAppOrders_URL;
	}

	/**
	 * @param getback_confirmAppOrders_URL the getback_confirmAppOrders_URL to set
	 */
	public void setGetback_confirmAppOrders_URL(String getback_confirmAppOrders_URL) {
		this.getback_confirmAppOrders_URL = getback_confirmAppOrders_URL;
	}

	/**
	 * @return the getback_updateOrders_URL
	 */
	public String getGetback_updateOrders_URL() {
		return this.getback_updateOrders_URL;
	}

	/**
	 * @param getback_updateOrders_URL the getback_updateOrders_URL to set
	 */
	public void setGetback_updateOrders_URL(String getback_updateOrders_URL) {
		this.getback_updateOrders_URL = getback_updateOrders_URL;
	}

	public String getNums() {
		return this.nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}



	public String getCount() {
		return this.count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getChangeBranchcode() {
		return changeBranchcode;
	}

	public void setChangeBranchcode(int changeBranchcode) {
		this.changeBranchcode = changeBranchcode;
	}

}
