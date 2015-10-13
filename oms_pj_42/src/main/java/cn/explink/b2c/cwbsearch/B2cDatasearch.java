package cn.explink.b2c.cwbsearch;

/**
 * 存储订单最后一个状态，用于提供电商查询
 * 
 * @author Administrator
 *
 */
public class B2cDatasearch {
	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	private long b2cid;
	private int customerid; // 对应的供货商id

	private String cwb;
	private String cretime;
	private int flowordertype;
	private String content; // 描述 (梦芭莎不能超过50字)
	private String operatorname; // 操作人
	private String remark; // 备注
	private long deliverystate;
	private String signname; // 签收人
	private int state; // 状态1 有效，0失效
	private String nowtime; // 当前存储时间

	public String getNowtime() {
		return nowtime;
	}

	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSignname() {
		return signname;
	}

	public void setSignname(String signname) {
		this.signname = signname;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	private String mobilephone; // 联系电话

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getB2cid() {
		return b2cid;
	}

	public void setB2cid(long b2cid) {
		this.b2cid = b2cid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
