package cn.explink.b2c.sfexpress;

public class SfexpressBean {

	private long id;
	private String cwb;
	private String transcwb;
	private String cretime;
	private String updatetime;
	private int status; // 状态 默认为0
	private int isok; // 0 是还需要查询，1是已结束

	public int getIsok() {
		return isok;
	}

	public void setIsok(int isok) {
		this.isok = isok;
	}

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

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String remark; //

}
