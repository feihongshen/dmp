package cn.explink.support.transcwb;

public class TranscwbViewJiDanType {
	String cwb;// 订单号
	String transcwb;// 运单号
	String flowordername;// 运单当前状态
	String ypdjcurrentstate; //一票多件当前状态
	
	

	public String getYpdjcurrentstate() {
		return ypdjcurrentstate;
	}

	public void setYpdjcurrentstate(String ypdjcurrentstate) {
		this.ypdjcurrentstate = ypdjcurrentstate;
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

	public String getFlowordername() {
		return flowordername;
	}

	public void setFlowordername(String flowordername) {
		this.flowordername = flowordername;
	}

}
