package cn.explink.b2c.telecomsc;

/**
 * 存储电信商城 订单导入接口的参数信息
 * 
 * @author Administrator
 *
 */
public class TelecomParms {

	private String outtransNo; // 外部交易号
	private String distribution_Id; // 配送单编号 对应cwb
	private String order_Id; // 订单号 transcwb
	private String receiver;// 联系人
	private String link_phone;// 联系电话
	private String phone; // 手机号
	private String address; //
	private String productinfo;// 商品名称
	private String send_conten;// 配送内容
	private String should_collection;// 应收金额
	private String pay_channel_id; // 支付方式
	private String sign; // 签名
	private String signtype; // 签名方式

	private String receiver_idcard_type; // 收件人证件类型
	private String receiver_idcard_code; // 收件人证件号码
	private String network_name; // 开户人姓名
	private String network_phone; // 开户人电话
	private String network_idcard_type;// 开户人证件类型
	private String network_idcard_code;// 开户人证件号码
	private String olId;// Crm购物车ID

	public String getReceiver_idcard_type() {
		return receiver_idcard_type;
	}

	public void setReceiver_idcard_type(String receiver_idcard_type) {
		this.receiver_idcard_type = receiver_idcard_type;
	}

	public String getReceiver_idcard_code() {
		return receiver_idcard_code;
	}

	public void setReceiver_idcard_code(String receiver_idcard_code) {
		this.receiver_idcard_code = receiver_idcard_code;
	}

	public String getNetwork_name() {
		return network_name;
	}

	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}

	public String getNetwork_phone() {
		return network_phone;
	}

	public void setNetwork_phone(String network_phone) {
		this.network_phone = network_phone;
	}

	public String getNetwork_idcard_type() {
		return network_idcard_type;
	}

	public void setNetwork_idcard_type(String network_idcard_type) {
		this.network_idcard_type = network_idcard_type;
	}

	public String getNetwork_idcard_code() {
		return network_idcard_code;
	}

	public void setNetwork_idcard_code(String network_idcard_code) {
		this.network_idcard_code = network_idcard_code;
	}

	public String getOlId() {
		return olId;
	}

	public void setOlId(String olId) {
		this.olId = olId;
	}

	public String getOuttransNo() {
		return outtransNo;
	}

	public void setOuttransNo(String outtransNo) {
		this.outtransNo = outtransNo;
	}

	public String getDistribution_Id() {
		return distribution_Id;
	}

	public void setDistribution_Id(String distribution_Id) {
		this.distribution_Id = distribution_Id;
	}

	public String getOrder_Id() {
		return order_Id;
	}

	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getLink_phone() {
		return link_phone;
	}

	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProductinfo() {
		return productinfo;
	}

	public void setProductinfo(String productinfo) {
		this.productinfo = productinfo;
	}

	public String getSend_conten() {
		return send_conten;
	}

	public void setSend_conten(String send_conten) {
		this.send_conten = send_conten;
	}

	public String getShould_collection() {
		return should_collection;
	}

	public void setShould_collection(String should_collection) {
		this.should_collection = should_collection;
	}

	public String getPay_channel_id() {
		return pay_channel_id;
	}

	public void setPay_channel_id(String pay_channel_id) {
		this.pay_channel_id = pay_channel_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSigntype() {
		return signtype;
	}

	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}

}
