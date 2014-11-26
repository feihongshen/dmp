package cn.explink.pos.bill99;

/**
 * 快钱对接
 * 
 * @author Administrator
 *
 */
public class Bill99 {

	private String version; // 版本号
	private String requester; // 请求方
	private String targeter; // 应答方
	private String ipodalias; // 验证用户名
	private String ipodpassword; // 密码
	private String ipodrequestFileName; // 请求验证签名文件名称
	private String ipodresponseFileName; // 返回 加密 文件名称。
	private int isotheroperator = 0; // 是否限制他人刷卡
	private int isupdateDeliverid; // 是否在刷卡或签收的时候更改派送员
	private String request_url; // 请求URL
	private int isopensignflag; // 是否开启签名限制

	public int getIsopensignflag() {
		return isopensignflag;
	}

	public void setIsopensignflag(int isopensignflag) {
		this.isopensignflag = isopensignflag;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public int getIsupdateDeliverid() {
		return isupdateDeliverid;
	}

	public void setIsupdateDeliverid(int isupdateDeliverid) {
		this.isupdateDeliverid = isupdateDeliverid;
	}

	public int getIsotheroperator() {
		return isotheroperator;
	}

	public void setIsotheroperator(int isotheroperator) {
		this.isotheroperator = isotheroperator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIpodalias() {
		return ipodalias;
	}

	public void setIpodalias(String ipodalias) {
		this.ipodalias = ipodalias;
	}

	public String getIpodpassword() {
		return ipodpassword;
	}

	public void setIpodpassword(String ipodpassword) {
		this.ipodpassword = ipodpassword;
	}

	public String getIpodrequestFileName() {
		return ipodrequestFileName;
	}

	public void setIpodrequestFileName(String ipodrequestFileName) {
		this.ipodrequestFileName = ipodrequestFileName;
	}

	public String getIpodresponseFileName() {
		return ipodresponseFileName;
	}

	public void setIpodresponseFileName(String ipodresponseFileName) {
		this.ipodresponseFileName = ipodresponseFileName;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getTargeter() {
		return targeter;
	}

	public void setTargeter(String targeter) {
		this.targeter = targeter;
	}

}
