package cn.explink.b2c.dongfangcj;

/**
 * 东方购物对接的配置信息
 * 
 * @author Administrator
 *
 */
public class DongFangCJ {

	private int dongfangcj_isopenflag; // 是否开启对接
	private String ftp_host; // ftp主机名 IP地址
	private String ftp_username; // 访问的用户名
	private String ftp_password; // 访问的密码
	private int ftp_port; // 端口号
	private String charencode = "UTF-8"; // 编码方式

	private String put_remotePath; // 远程服务器文件夹 ^下载订单文件夹
	private String get_remotePath; // 远程服务器文件夹 ^上传订单文件夹

	public String getPut_remotePath() {
		return put_remotePath;
	}

	public void setPut_remotePath(String putRemotePath) {
		put_remotePath = putRemotePath;
	}

	public String getGet_remotePath() {
		return get_remotePath;
	}

	public void setGet_remotePath(String getRemotePath) {
		get_remotePath = getRemotePath;
	}

	private String downloadPath; // 本地所在文件夹 位置 下载
	private String downloadPath_bak; // 本地所在文件夹备份 位置 下载
	private String uploadPath; // 状态反馈上传地址
	private String uploadPath_bak; // 状态反馈上传地址 备份
	private String partener; // 配送结果反馈标示

	public String getPartener() {
		return partener;
	}

	public void setPartener(String partener) {
		this.partener = partener;
	}

	public String getUploadPath_bak() {
		return uploadPath_bak;
	}

	public void setUploadPath_bak(String uploadPathBak) {
		uploadPath_bak = uploadPathBak;
	}

	private String maxcount; // 查询最大数量
	private String customerids; // 所在的customerid

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(String maxcount) {
		this.maxcount = maxcount;
	}

	public String getFeedbacktime() {
		return feedbacktime;
	}

	public void setFeedbacktime(String feedbacktime) {
		this.feedbacktime = feedbacktime;
	}

	private String feedbacktime; // 从这个时间段之后的反馈

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	private boolean isdelDirFlag = true; // 是否删除远程文件夹 true删除 flase不删除
	private int keepDays; // 显示几天内 的数据
	private int isExportSystemFlag = 0; // 是否直接导入系统中0 不导入系统 1导入系统 默认为不导入系统
	private int isaddressmatchflag; // 是否开启地址库 0 不开启，1开启
	private long warehouseid; // 库房入库id

	private String search_url; // 提供CJ的查询地址
	private String company_num; // 在CJ的唯一标识
	private String private_key; // 密钥

	public String getSearch_url() {
		return search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public String getCompany_num() {
		return company_num;
	}

	public void setCompany_num(String company_num) {
		this.company_num = company_num;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public int getIsaddressmatchflag() {
		return isaddressmatchflag;
	}

	public void setIsaddressmatchflag(int isaddressmatchflag) {
		this.isaddressmatchflag = isaddressmatchflag;
	}

	public int getDongfangcj_isopenflag() {
		return dongfangcj_isopenflag;
	}

	public void setDongfangcj_isopenflag(int dongfangcjIsopenflag) {
		dongfangcj_isopenflag = dongfangcjIsopenflag;
	}

	public int getIsExportSystemFlag() {
		return isExportSystemFlag;
	}

	public void setIsExportSystemFlag(int isExportSystemFlag) {
		this.isExportSystemFlag = isExportSystemFlag;
	}

	/**
	 * @return the downloadPath
	 */
	public String getDownloadPath() {
		return downloadPath;
	}

	/**
	 * @param downloadPath
	 *            the downloadPath to set
	 */
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	/**
	 * @return the downloadPath_bak
	 */
	public String getDownloadPath_bak() {
		return downloadPath_bak;
	}

	/**
	 * @param downloadPathBak
	 *            the downloadPath_bak to set
	 */
	public void setDownloadPath_bak(String downloadPathBak) {
		downloadPath_bak = downloadPathBak;
	}

	/**
	 * @return the isdelDirFlag
	 */
	public boolean isIsdelDirFlag() {
		return isdelDirFlag;
	}

	/**
	 * @param isdelDirFlag
	 *            the isdelDirFlag to set
	 */
	public void setIsdelDirFlag(boolean isdelDirFlag) {
		this.isdelDirFlag = isdelDirFlag;
	}

	/**
	 * @return the keepDays
	 */
	public int getKeepDays() {
		return keepDays;
	}

	/**
	 * @param keepDays
	 *            the keepDays to set
	 */
	public void setKeepDays(int keepDays) {
		this.keepDays = keepDays;
	}

	public String getCharencode() {
		return charencode;
	}

	public void setCharencode(String charencode) {
		this.charencode = charencode;
	}

	public String getFtp_host() {
		return ftp_host;
	}

	public void setFtp_host(String ftpHost) {
		ftp_host = ftpHost;
	}

	public String getFtp_username() {
		return ftp_username;
	}

	public void setFtp_username(String ftpUsername) {
		ftp_username = ftpUsername;
	}

	public String getFtp_password() {
		return ftp_password;
	}

	public void setFtp_password(String ftpPassword) {
		ftp_password = ftpPassword;
	}

	public int getFtp_port() {
		return ftp_port;
	}

	public void setFtp_port(int ftpPort) {
		ftp_port = ftpPort;
	}

}
