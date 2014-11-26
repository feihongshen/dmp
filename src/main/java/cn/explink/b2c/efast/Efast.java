package cn.explink.b2c.efast;

public class Efast {

	private String app_key;
	private String app_secret; // 密钥
	private String app_nick; // 标识
	private String app_url; // 请求ERP url
	private long warehouseid; // 库房
	private String customerid; // 供货商 id
	private int beforhours; // 提前的小时数
	private int loopcount; // 循环次数
	private String shipping_code; // 配送方式代码 默认01

	private String erpEnum; // 对应的enum,用于区分

	public String getErpEnum() {
		return erpEnum;
	}

	public void setErpEnum(String erpEnum) {
		this.erpEnum = erpEnum;
	}

	public String getShipping_code() {
		return shipping_code;
	}

	public void setShipping_code(String shipping_code) {
		this.shipping_code = shipping_code;
	}

	public int getBeforhours() {
		return beforhours;
	}

	public void setBeforhours(int beforhours) {
		this.beforhours = beforhours;
	}

	public int getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(int loopcount) {
		this.loopcount = loopcount;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getApp_secret() {
		return app_secret;
	}

	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}

	public String getApp_nick() {
		return app_nick;
	}

	public void setApp_nick(String app_nick) {
		this.app_nick = app_nick;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

}
