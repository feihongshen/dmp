package cn.explink.b2c.huitongtx;

public class Huitongtx {

	private String app_key; // 物流公司的唯一标识

	private String service_code; // 服务编码
	private String private_key; // 私钥信息
	private String post_url; // 推送订单状态URL;

	private String getInfo_url; // 自己公司的接口（固定，用来被动接收WLB推送的数据信息）
	private String customerids; // Tmall在系统中的id，如果是多个按逗号隔开
	private long warehouseid; // 订单导入的库房Id
	private String serviceTel; // 客服电话，tmall规定如果发生异常则需要联系物流公司客服

	// //////////////站点地址库匹配部分管理////////////////////////
	private long isopenaddress; // 是否开启 ：1启动，0关闭
	private String addressReceiver_url; // httx->易派 地址匹配url
	private String addressSender_url; // 异步通知匹配结果url
	private long addressMaxCount; // 每次请求最大数量

	public long getIsopenaddress() {
		return isopenaddress;
	}

	public void setIsopenaddress(long isopenaddress) {
		this.isopenaddress = isopenaddress;
	}

	public String getAddressReceiver_url() {
		return addressReceiver_url;
	}

	public void setAddressReceiver_url(String addressReceiver_url) {
		this.addressReceiver_url = addressReceiver_url;
	}

	public String getAddressSender_url() {
		return addressSender_url;
	}

	public void setAddressSender_url(String addressSender_url) {
		this.addressSender_url = addressSender_url;
	}

	public long getAddressMaxCount() {
		return addressMaxCount;
	}

	public void setAddressMaxCount(long addressMaxCount) {
		this.addressMaxCount = addressMaxCount;
	}

	private int selectMaxCount;

	public int getSelectMaxCount() {
		return selectMaxCount;
	}

	public void setSelectMaxCount(int selectMaxCount) {
		this.selectMaxCount = selectMaxCount;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	private int b2c_enum; // 对应的enmu

	public int getB2c_enum() {
		return b2c_enum;
	}

	public void setB2c_enum(int b2c_enum) {
		this.b2c_enum = b2c_enum;
	}

	public String getCustomerids() {
		return customerids;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getGetInfo_url() {
		return getInfo_url;
	}

	public void setGetInfo_url(String getInfo_url) {
		this.getInfo_url = getInfo_url;
	}

}
