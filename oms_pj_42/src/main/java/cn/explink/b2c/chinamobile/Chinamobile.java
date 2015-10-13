package cn.explink.b2c.chinamobile;

public class Chinamobile {

	private String expressid; // 承运商编号
	private String customerid; // 供货商Id
	private long maxCount; // 每次查询反馈数量
	private String feedback_url; // 状态推送移动URL
	private String ftp_host; // 请求FTP IP
	private String ftp_port; // 请求FTP 端口 21
	private String ftp_username; // 用户名
	private String ftp_password; // 密码
	private String remotePath; // 远程服务器文件夹 ^下载订单文件夹
	private String downloadPath; // 本地所在文件夹 位置 下载
	private String downloadPath_bak; // 本地所在文件夹备份 位置 下载

	public String getExpressid() {
		return expressid;
	}

	public void setExpressid(String expressid) {
		this.expressid = expressid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public String getFtp_host() {
		return ftp_host;
	}

	public void setFtp_host(String ftp_host) {
		this.ftp_host = ftp_host;
	}

	public String getFtp_port() {
		return ftp_port;
	}

	public void setFtp_port(String ftp_port) {
		this.ftp_port = ftp_port;
	}

	public String getFtp_username() {
		return ftp_username;
	}

	public void setFtp_username(String ftp_username) {
		this.ftp_username = ftp_username;
	}

	public String getFtp_password() {
		return ftp_password;
	}

	public void setFtp_password(String ftp_password) {
		this.ftp_password = ftp_password;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getDownloadPath_bak() {
		return downloadPath_bak;
	}

	public void setDownloadPath_bak(String downloadPath_bak) {
		this.downloadPath_bak = downloadPath_bak;
	}

}
