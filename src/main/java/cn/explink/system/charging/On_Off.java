package cn.explink.system.charging;

import cn.explink.util.MD5.MD5Util;

public class On_Off {
	private long id;
	private long number;
	private String on_or_off;
	private String type;
	private String mac;

	protected static final String mac_key = "jmsexplink2012";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getOn_or_off() {
		return on_or_off;
	}

	public void setOn_or_off(String on_or_off) {
		this.on_or_off = on_or_off;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	protected boolean compareMac(String mac, String value) {
		if (mac.equals(MD5Util.md5(value + mac_key))) {
			return true;
		}
		return false;
	}
}
