package cn.explink.b2c.jumeiyoupin;

public class JuMeiYouPin_json {

	private String cwb;// 订单号
	private String trackdatetime;// 操作时间
	private String trackevent;// 最后一条运转信息
	private String trackstatus;// 当前状态

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTrackdatetime() {
		return trackdatetime;
	}

	public void setTrackdatetime(String trackdatetime) {
		this.trackdatetime = trackdatetime;
	}

	public String getTrackevent() {
		return trackevent;
	}

	public void setTrackevent(String trackevent) {
		this.trackevent = trackevent;
	}

	public String getTrackstatus() {
		return trackstatus;
	}

	public void setTrackstatus(String trackstatus) {
		this.trackstatus = trackstatus;
	}

}
