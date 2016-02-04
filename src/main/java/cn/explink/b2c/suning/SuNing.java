package cn.explink.b2c.suning;

public class SuNing {
	private int everythreaddonum;
	private String private_key;//秘钥
	private String customerid;//苏宁customerid
	private String spCode;//苏宁合作商编码
	private long maxcount;//每次推送货态的数量
	private String importUrl;//苏宁请求过来的地址
	private String feedbackUrl;//反馈给苏宁的地址
	private long warehouseid;//导入库房的id
	
	public int getEverythreaddonum() {
		return everythreaddonum;
	}
	public void setEverythreaddonum(int everythreaddonum) {
		this.everythreaddonum = everythreaddonum;
	}
	public long getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getImportUrl() {
		return importUrl;
	}
	public void setImportUrl(String importUrl) {
		this.importUrl = importUrl;
	}
	public String getFeedbackUrl() {
		return feedbackUrl;
	}
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getSpCode() {
		return spCode;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public long getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(long maxcount) {
		this.maxcount = maxcount;
	}
	
}
