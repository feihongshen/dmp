package cn.explink.b2c.explink.code_down;

/**
 * 
 * @author Administrator 易派 接口设置 （下游）
 */
public class EpaiApi {
	private int b2cid;

	private String userCode; // 用户代码 请求唯一标识
	private long customerid; // 在系统中的customerid
	private String getOrder_url; // 订单下载请求URL
	private String callBack_url; // 订单下载完成回调url

	private String private_key; // 密钥
	private int pageSize; // 每次获取数量

	private String feedback_url; // 状态回传url
	private long warehouseid; // 订单导入站点
	private int state; // 状态 1，有效，2失效 目前暂不用 采用删除的方式
	private int ispostflag; // 传输方式 0 数据流 1 POST参数

	public int getIspostflag() {
		return ispostflag;
	}

	public void setIspostflag(int ispostflag) {
		this.ispostflag = ispostflag;
	}

	private int isopenflag; // 是否开启对接 下载
	private int isfeedbackflag; // 是否开启反馈 //是否开启反馈 0关闭， 1开启

	public int getIsfeedbackflag() {
		return isfeedbackflag;
	}

	public void setIsfeedbackflag(int isfeedbackflag) {
		this.isfeedbackflag = isfeedbackflag;
	}

	public int getIsopenflag() {
		return isopenflag;
	}

	public void setIsopenflag(int isopenflag) {
		this.isopenflag = isopenflag;
	}

	public String getCallBack_url() {
		return callBack_url;
	}

	public void setCallBack_url(String callBack_url) {
		this.callBack_url = callBack_url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getB2cid() {
		return b2cid;
	}

	public void setB2cid(int b2cid) {
		this.b2cid = b2cid;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getGetOrder_url() {
		return getOrder_url;
	}

	public void setGetOrder_url(String getOrder_url) {
		this.getOrder_url = getOrder_url;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

}
