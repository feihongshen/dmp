package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.controller.PopupConfig;

@XmlRootElement
public class PDAResponse {

	public PDAResponse() {
	}

	public PDAResponse(String statuscode, String errorinfo) {
		this.statuscode = statuscode;
		this.errorinfo = errorinfo;
	}

	public PDAResponse(String statuscode, String errorinfo, long requestbatchno, String wavPath) {
		this.statuscode = statuscode;
		this.errorinfo = errorinfo;
		this.requestbatchno = requestbatchno;
		this.wavPath = wavPath;
	}

	private String statuscode = "000000";
	private String errorinfo = "";
	private String wavPath = "";
	private boolean shouldShock;
	private long requestbatchno;
	private String printinfo = "";
	private PopupConfig popupConfig;
	private long scannum;
	String serverversion = "3.0";

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public String getErrorinfo() {
		return errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

	public String getWavPath() {
		return wavPath;
	}

	public void setWavPath(String wavPath) {
		this.wavPath = wavPath;
	}

	public String getServerversion() {
		return serverversion;
	}

	public void setServerversion(String serverversion) {
		this.serverversion = serverversion;
	}

	public boolean isShouldShock() {
		return shouldShock;
	}

	public void setShouldShock(boolean shouldShock) {
		this.shouldShock = shouldShock;
	}

	public long getRequestbatchno() {
		return requestbatchno;
	}

	public void setRequestbatchno(long requestbatchno) {
		this.requestbatchno = requestbatchno;
	}

	public String getPrintinfo() {
		return printinfo;
	}

	public void setPrintinfo(String printinfo) {
		this.printinfo = printinfo;
	}

	public PopupConfig getPopupConfig() {
		return popupConfig;
	}

	public void setPopupConfig(PopupConfig popupConfig) {
		this.popupConfig = popupConfig;
	}

	public long getScannum() {
		return scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
	}

}
