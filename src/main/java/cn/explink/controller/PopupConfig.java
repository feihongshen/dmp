package cn.explink.controller;

;

public class PopupConfig {

	private String text = "";
	private String okUrl = "";
	private String cancelUrl = "";
	private String okText = "确定";
	private String cancelText = "取消";

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOkUrl() {
		return okUrl;
	}

	public void setOkUrl(String okUrl) {
		this.okUrl = okUrl;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getOkText() {
		return okText;
	}

	public void setOkText(String okText) {
		this.okText = okText;
	}

	public String getCancelText() {
		return cancelText;
	}

	public void setCancelText(String cancelText) {
		this.cancelText = cancelText;
	}
}
