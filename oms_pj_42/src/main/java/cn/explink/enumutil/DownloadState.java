package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载请求处理状态，不包含旧处理机制的-1,0,1,2,3
 */
public enum DownloadState {

	pending(10, "待处理"), ready(11, "已完成"), completed(12, "已下载");

	private String description;

	private int value;

	DownloadState(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
