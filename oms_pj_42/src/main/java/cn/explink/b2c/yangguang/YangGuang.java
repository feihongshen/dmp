package cn.explink.b2c.yangguang;

/**
 * 央广 属性 设置
 * 
 * @author Administrator
 * 
 */
public class YangGuang {

	private String multiInfos; // 兼容多个配置 jsonarray集合

	public String getMultiInfos() {
		return multiInfos;
	}

	public void setMultiInfos(String multiInfos) {
		this.multiInfos = multiInfos;
	}

	private String host;// ftp主机名 IP地址
	private int port; // 端口号
	private String charencode = "UTF-8"; // 编码方式
	private String download_remotePath; // 远程服务器文件夹 ^下载订单文件夹
	private String upload_remotePath; // 远程服务器文件夹 ^上传订单文件夹

	private String downloadPath; // 本地所在文件夹 位置 下载
	private String downloadPath_bak; // 本地所在文件夹备份 位置 下载
	private String uploadPath; // 状态反馈上传地址
	private String uploadPath_bak; // 状态反馈上传地址 备份

	private boolean isdelDirFlag = true; // 是否删除远程文件夹 true删除 flase不删除
	private int keepDays; // 显示几天内 的数据

	private int timeout = 20000; // 超时限制r
	private String server_sys = "UNIX"; // FTP服务器类型 默认linux
	private int feedbackHours; // 状态回传时间 06 设置为6

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCharencode() {
		return charencode;
	}

	public void setCharencode(String charencode) {
		this.charencode = charencode;
	}

	public String getDownload_remotePath() {
		return download_remotePath;
	}

	public void setDownload_remotePath(String download_remotePath) {
		this.download_remotePath = download_remotePath;
	}

	public String getUpload_remotePath() {
		return upload_remotePath;
	}

	public void setUpload_remotePath(String upload_remotePath) {
		this.upload_remotePath = upload_remotePath;
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

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUploadPath_bak() {
		return uploadPath_bak;
	}

	public void setUploadPath_bak(String uploadPath_bak) {
		this.uploadPath_bak = uploadPath_bak;
	}

	public boolean isIsdelDirFlag() {
		return isdelDirFlag;
	}

	public void setIsdelDirFlag(boolean isdelDirFlag) {
		this.isdelDirFlag = isdelDirFlag;
	}

	public int getKeepDays() {
		return keepDays;
	}

	public void setKeepDays(int keepDays) {
		this.keepDays = keepDays;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getServer_sys() {
		return server_sys;
	}

	public void setServer_sys(String server_sys) {
		this.server_sys = server_sys;
	}

	public int getFeedbackHours() {
		return feedbackHours;
	}

	public void setFeedbackHours(int feedbackHours) {
		this.feedbackHours = feedbackHours;
	}

}
