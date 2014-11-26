package cn.explink.domain;

public class WavObject {

	private int time = -1;

	private String url = null;

	public WavObject(int time, String url) {
		this.time = time;
		this.url = url;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
