package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CwbTrackBody {
	String cwb;
	String trackdatetime;
	String trackevent;
	String branchname;

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

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
}
