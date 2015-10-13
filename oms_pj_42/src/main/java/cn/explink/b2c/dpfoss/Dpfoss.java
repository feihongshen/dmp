package cn.explink.b2c.dpfoss;

/**
 * 对接 属性 设置 德邦物流
 * 
 * @author Administrator
 *
 */
public class Dpfoss {

	private String customerids; // 在系统中的ids
	private long warehouseid; // 订单导入库房
	private String agentCompanyCode; // 代理公司编码
	private String agentCompanyName; // 代理公司名称

	private String agent; // 用户名
	private String pwd; // 访问密码
	private String private_key; // 密码
	private int maxCount; // 每次查询大小

	private String serviceCode_queryWaybill; // 外发单编码
	private String queryWaybill_url; // 请求德邦的URL 外发单

	private String serviceCode_uploadSign; // 上传签收单编码
	private String uploadSign_url; // 请求德邦的URL 上传签收

	private String serviceCode_uploadTrack; // 上传跟踪编码
	private String uploadTrack_url; // 请求德邦的URL 上传跟踪

	public String getServiceCode_queryWaybill() {
		return serviceCode_queryWaybill;
	}

	public void setServiceCode_queryWaybill(String serviceCode_queryWaybill) {
		this.serviceCode_queryWaybill = serviceCode_queryWaybill;
	}

	public String getQueryWaybill_url() {
		return queryWaybill_url;
	}

	public void setQueryWaybill_url(String queryWaybill_url) {
		this.queryWaybill_url = queryWaybill_url;
	}

	public String getServiceCode_uploadSign() {
		return serviceCode_uploadSign;
	}

	public void setServiceCode_uploadSign(String serviceCode_uploadSign) {
		this.serviceCode_uploadSign = serviceCode_uploadSign;
	}

	public String getUploadSign_url() {
		return uploadSign_url;
	}

	public void setUploadSign_url(String uploadSign_url) {
		this.uploadSign_url = uploadSign_url;
	}

	public String getServiceCode_uploadTrack() {
		return serviceCode_uploadTrack;
	}

	public void setServiceCode_uploadTrack(String serviceCode_uploadTrack) {
		this.serviceCode_uploadTrack = serviceCode_uploadTrack;
	}

	public String getUploadTrack_url() {
		return uploadTrack_url;
	}

	public void setUploadTrack_url(String uploadTrack_url) {
		this.uploadTrack_url = uploadTrack_url;
	}

	private String agentOrgName; // 网点名称
	private String agentOrgCode; // 网点编码

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public String getAgentOrgCode() {
		return agentOrgCode;
	}

	public void setAgentOrgCode(String agentOrgCode) {
		this.agentOrgCode = agentOrgCode;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getAgentCompanyCode() {
		return agentCompanyCode;
	}

	public void setAgentCompanyCode(String agentCompanyCode) {
		this.agentCompanyCode = agentCompanyCode;
	}

	public String getAgentCompanyName() {
		return agentCompanyName;
	}

	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

}
