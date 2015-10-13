package cn.explink.b2c.amazon.domain;

/**
 * 亚马逊对接设置entity
 * 
 * @author Administrator
 *
 */
public class Amazon {
	private String full_url; // 获取订单的文件路径
	private String tnt_url; // 上传文件的路径
	private String tnt_url_bak; // 上传文件的备份路径

	private String customerid; // 在系统中的customerid,只能唯一一个
	private int maxCount; // 每次推送最大数量

	private long warehouseid; // 订单入库库房
	private String senderIdentifier; // 发送方id
	private String recipientIdentifier; // 接收方id

	private long isShow; // 是否显示配送员信息
	private String carrierSCAC; // CarrierSCAC

	private String dssFile; // dssFile
	private long isHebingTuotou; // 是否合并妥投,0合并,1不合并 默认合并
	private long delay; // 超期天数
	private long isSystemCommit; // 是否系统自动推送

	public String getTnt_url() {
		return tnt_url;
	}

	public void setTnt_url(String tnt_url) {
		this.tnt_url = tnt_url;
	}

	public String getFull_url() {
		return full_url;
	}

	public void setFull_url(String full_url) {
		this.full_url = full_url;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getSenderIdentifier() {
		return senderIdentifier;
	}

	public void setSenderIdentifier(String senderIdentifier) {
		this.senderIdentifier = senderIdentifier;
	}

	public String getRecipientIdentifier() {
		return recipientIdentifier;
	}

	public void setRecipientIdentifier(String recipientIdentifier) {
		this.recipientIdentifier = recipientIdentifier;
	}

	public long getIsShow() {
		return isShow;
	}

	public void setIsShow(long isShow) {
		this.isShow = isShow;
	}

	public String getCarrierSCAC() {
		return carrierSCAC;
	}

	public void setCarrierSCAC(String carrierSCAC) {
		this.carrierSCAC = carrierSCAC;
	}

	public String getDssFile() {
		return dssFile;
	}

	public void setDssFile(String dssFile) {
		this.dssFile = dssFile;
	}

	public long getIsHebingTuotou() {
		return isHebingTuotou;
	}

	public void setIsHebingTuotou(long isHebingTuotou) {
		this.isHebingTuotou = isHebingTuotou;
	}

	public String getTnt_url_bak() {
		return tnt_url_bak;
	}

	public void setTnt_url_bak(String tnt_url_bak) {
		this.tnt_url_bak = tnt_url_bak;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getIsSystemCommit() {
		return isSystemCommit;
	}

	public void setIsSystemCommit(long isSystemCommit) {
		this.isSystemCommit = isSystemCommit;
	}

}
