package cn.explink.b2c.haoyigou;

public class HaoYiGou {
	private String sendCode;//货运公司code
	private String customercode;//客户代码
	private String companyid;//电商标识 默认====好易购
	private String customerid;//系统设置电商id
	
	private long maxcount;//每次返回的最大单量
	
	public long getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(long maxcount) {
		this.maxcount = maxcount;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getSendCode() {
		return sendCode;
	}
	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}
	public String getCustomercode() {
		return customercode;
	}
	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	
	//===========FTP基础信息配置============
	private String ftp_host; // ftp主机名 IP地址
	private String ftp_username; // 访问的用户名
	private String ftp_password; // 访问的密码
	private int ftp_port; // 端口号
	private String charencode; // 编码方式
	
	//===========服务器以及FTP文件存储位置============
	private String upload_remotePathps; // 远程服务器文件夹 ^上传订单文件夹（上传到FTP）配送文件夹
	private String upload_remotePathth; // 远程服务器文件夹 ^上传订单文件夹（上传到FTP）退货文件夹
	private String uploadPath; // 状态反馈上传地址
	private String uploadPath_bak; // 状态反馈上传地址 备份
	//标识供货商
	private String partener;
	
	
	public String getPartener() {
		return partener;
	}
	public void setPartener(String partener) {
		this.partener = partener;
	}
	
	public String getUpload_remotePathps() {
		return upload_remotePathps;
	}
	public void setUpload_remotePathps(String upload_remotePathps) {
		this.upload_remotePathps = upload_remotePathps;
	}
	public String getUpload_remotePathth() {
		return upload_remotePathth;
	}
	public void setUpload_remotePathth(String upload_remotePathth) {
		this.upload_remotePathth = upload_remotePathth;
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
	public String getFtp_host() {
		return ftp_host;
	}
	public void setFtp_host(String ftp_host) {
		this.ftp_host = ftp_host;
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
	public int getFtp_port() {
		return ftp_port;
	}
	public void setFtp_port(int ftp_port) {
		this.ftp_port = ftp_port;
	}
	public String getCharencode() {
		return charencode;
	}
	public void setCharencode(String charencode) {
		this.charencode = charencode;
	}
	
	
}
