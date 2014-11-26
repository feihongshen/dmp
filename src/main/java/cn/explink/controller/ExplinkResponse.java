package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import cn.explink.controller.pda.LoginResponseBody;
import cn.explink.domain.WavObject;

@XmlRootElement
@XmlSeeAlso(LoginResponseBody.class)
public class ExplinkResponse {

	private String statuscode = "000000";
	private String yuyuedaService;
	private String errorinfo = "";
	private Object body = null;
	private String wavPath = "";
	private boolean shouldShock;
	private long requestbatchno;
	String serverversion = "3.0";
	private List<WavObject> wavList = null;

	public ExplinkResponse() {
	}

	public ExplinkResponse(String statuscode, String errorinfo, Object body) {
		this.statuscode = statuscode;
		this.errorinfo = errorinfo;
		this.body = body;
	}

	public ExplinkResponse(String statuscode, String errorinfo, long requestbatchno, String wavPath, Object body) {
		this.statuscode = statuscode;
		this.errorinfo = errorinfo;
		this.requestbatchno = requestbatchno;
		this.wavPath = wavPath;
		this.body = body;
	}

	public String getStatuscode() {
		return this.statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public String getErrorinfo() {
		return this.errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

	public Object getBody() {
		return this.body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getWavPath() {
		return this.wavPath;
	}

	public void setWavPath(String wavPath) {
		this.wavPath = wavPath;
	}

	public String getServerversion() {
		return this.serverversion;
	}

	public void setServerversion(String serverversion) {
		this.serverversion = serverversion;
	}

	public boolean isShouldShock() {
		return this.shouldShock;
	}

	public void setShouldShock(boolean shouldShock) {
		this.shouldShock = shouldShock;
	}

	public long getRequestbatchno() {
		return this.requestbatchno;
	}

	public void setRequestbatchno(long requestbatchno) {
		this.requestbatchno = requestbatchno;
	}

	public String getYuyuedaService() {
		return this.yuyuedaService;
	}

	public void setYuyuedaService(String yuyuedaService) {
		this.yuyuedaService = yuyuedaService;
	}

	public void addWav(int time, String url) {
		this.getWavList().add(new WavObject(time, url));
	}

	public void addShortWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(500, fullPath));
	}

	public void addLongWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(800, fullPath));
	}

	public void addLastWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(0, fullPath));
	}

	public List<WavObject> getWavList() {
		if (this.wavList == null) {
			this.wavList = new ArrayList<WavObject>();
		}
		return this.wavList;
	}

}
