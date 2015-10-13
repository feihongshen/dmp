package cn.explink.domain;

public class B2CCodData {

	private long id;
	private String cwb;// 订单号
	private String posttime;// 状态时间
	private String cretime;// 创建时间
	private String datajson;// 反馈信息
	private long state;// 推送状态，0未推送，1.推送成功，2推送失败 ,3(垃圾数据标识) 系统自动屏蔽
						// （通过系统自动过滤不推送状态产生20130723 add）
	private long customerid;// 供货商
	private String remark; // 备注信息 包括存储推送失败的原因

	private long deliverystate;// 反馈结果

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public String getDatajson() {
		return datajson;
	}

	public void setDatajson(String datajson) {
		this.datajson = datajson;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

}
