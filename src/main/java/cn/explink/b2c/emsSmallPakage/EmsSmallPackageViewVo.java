package cn.explink.b2c.emsSmallPakage;

import cn.explink.domain.CwbOrder;

public class EmsSmallPackageViewVo extends CwbOrder{
	private String cwb;//订单号
	private String transcwb;//运单号
	private String email_num;//邮政运单号
	private String bingTime;//打印时间
	private String deliveryBranchName;//配送站点名称
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public String getEmail_num() {
		return email_num;
	}
	public void setEmail_num(String email_num) {
		this.email_num = email_num;
	}
	public String getBingTime() {
		return bingTime;
	}
	public void setBingTime(String bingTime) {
		this.bingTime = bingTime;
	}
	public String getDeliveryBranchName() {
		return deliveryBranchName;
	}
	public void setDeliveryBranchName(String deliveryBranchName) {
		this.deliveryBranchName = deliveryBranchName;
	}
}