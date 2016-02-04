package cn.explink.support.transcwb;

public class TranscwbView {
	String cwb;// 订单号
	String transcwb;// 运单号
	String flowordername;// 一票多件当前状态,原来的运单号当前状态
	String transcwbstate; //运单状态
	
	



	public String getTranscwbstate() {
		return transcwbstate;
	}

	public void setTranscwbstate(String transcwbstate) {
		this.transcwbstate = transcwbstate;
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
