package cn.explink.domain;

public class ProxyConf {
	private int id;
	private int port;// 端口号
	private int type;// 类型
	private int state;// 状态
	private String ip;// 代理ip
	private int with;// 速度 0慢 1快
	private int isnoDefault;// 是否是默认代理0否1是

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getWith() {
		return with;
	}

	public void setWith(int with) {
		this.with = with;
	}

	public int getIsnoDefault() {
		return isnoDefault;
	}

	public void setIsnoDefault(int isnoDefault) {
		this.isnoDefault = isnoDefault;
	}

}
