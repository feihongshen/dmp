package cn.explink.b2c.mss;

/**
 * 电商管理美食送
 *
 * @author zhaoshb
 * @since AR1.0
 */
public class Mss {

	private String access_key;// 公钥(可在o3系统开发者信息中生成)
	private String secret_key;// 私钥(可在o3系统开发者信息中生成)
	private String importUrl; // 请求URL
	private String feedbackUrl;// 状态回传url
	private long warehouseid; // 订单入库库房
	private String customerid; // 在系统中的customerid
	private String dmsCode;// 承运商编码
	private int maxCount; // 每次查询的大小
	private String cmd;// 命令
	private String version;// API版本号，当前为v2.0
	private String partner_id;// 商户ID
	private String partner_order_id;// 商户订单ID
	private String ticket;// 请求唯一标识(UUID)

	public String getAccess_key() {
		return this.access_key;
	}

	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}

	public String getImportUrl() {
		return this.importUrl;
	}

	public void setImportUrl(String importUrl) {
		this.importUrl = importUrl;
	}

	public String getFeedbackUrl() {
		return this.feedbackUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public long getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getDmsCode() {
		return this.dmsCode;
	}

	public void setDmsCode(String dmsCode) {
		this.dmsCode = dmsCode;
	}

	public int getMaxCount() {
		return this.maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getSecret_key() {
		return this.secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getCmd() {
		return this.cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPartner_id() {
		return this.partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getPartner_order_id() {
		return this.partner_order_id;
	}

	public void setPartner_order_id(String partner_order_id) {
		this.partner_order_id = partner_order_id;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	
}
