package cn.explink.domain;

public class NoPiPeiCwbDetail {

	private long id;// 主键id
	private String cwb;// 订单号
	private String createtime;// 创建时间
	private long carwarehouseid;// 发货库房id

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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getCarwarehouseid() {
		return carwarehouseid;
	}

	public void setCarwarehouseid(long carwarehouseid) {
		this.carwarehouseid = carwarehouseid;
	}

}
