package cn.explink.b2c.amazon.domain;

/**
 * 亚马逊对接设置entity
 * 
 * @author Administrator
 *
 */
public class AmazonZiti {
	private long id; //
	private String cwb; // 订单号
	private long createtime; // 到站时间毫秒数
	private String daozhantime; // 到站时间
	private long customerid; // 在系统中的customerid,只能唯一一个

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

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getDaozhantime() {
		return daozhantime;
	}

	public void setDaozhantime(String daozhantime) {
		this.daozhantime = daozhantime;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

}
