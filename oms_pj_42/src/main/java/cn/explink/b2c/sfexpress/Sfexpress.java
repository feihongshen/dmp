package cn.explink.b2c.sfexpress;

public class Sfexpress {

	private String commoncode; // 承运商编码 这里设置要和dmp承运商设置的编码一致
	private String send_url; // 下单、查询顺丰快递URL
	private long maxCount; // 每次批量查询数量大小
	private long loopcount;// 失败重发次数
	private String expressCode; // 接入编码

	// 在推送顺丰的时候用到
	private String companyName; // 公司名称
	private String checkword; // 校验码,写在XMLhead里面

	public String getCheckword() {
		return checkword;
	}

	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}

	private String servicePhone; // 客服电话
	private String serviceContact; // 客服联系人
	private String j_address; // 寄件地址,这里统一写迅祥的公司地址
	private String custid; // 月结卡号 迅祥和顺丰月结的时候对应的卡号，上线前需要写在配置里

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getJ_address() {
		return j_address;
	}

	public void setJ_address(String j_address) {
		this.j_address = j_address;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getServiceContact() {
		return serviceContact;
	}

	public void setServiceContact(String serviceContact) {
		this.serviceContact = serviceContact;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public long getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(long loopcount) {
		this.loopcount = loopcount;
	}

	public String getCommoncode() {
		return commoncode;
	}

	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}

	public String getSend_url() {
		return send_url;
	}

	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

}
