package cn.explink.b2c.homegou;

/**
 * 东方CJ XML的节点
 * 
 * @author Administrator
 *
 */
public class HomegouXMLNote {

	private String orderid; // 配送结束编号
	private String business_no; // 配送公司代码
	private String cwb; // 运单号
	private String order_no; // 订购编号
	private String deliveryState; // 配送结束与否
	private String deliverytime; // 配送结束日
	private String deliveryReason; // 配送原因代码 使用“00”
	private String peisongqufen; // 配送区分10:不用付款, 20:货到付款，40：交换送货
	private String payAmount; // 配送金额 长度不足前面补0
	private String paytypeid1; // 付款方法1 03:COD 04:MOB 00:其它
	private String payamount1; // 支付金额1 长度不足前面补0
	private String paytypeid2; // 付款方法2 03:COD 04:MOB 00:其它
	private String payamount2; // 支付金额2 长度不足前面补0
	private String terminal_no; // 终端号
	private String card_bank; // 银行代码
	private String card_no; // 卡号
	private String ok_date; // 授权时间
	private String inamt_amt; // 授权金额
	private String batch_no; // batch号
	private String sys_no;// 系统号
	private String serial_no;// 序列号
	private String inamt_date; // 授权日
	private String type; // 02:OK 82:取消(现在不使用)
	private int paytypeflag; // 支付方式,是现金还是POS
	private String transcwb; // 订购编码,用于上门退订单类型

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public int getPaytypeflag() {
		return paytypeflag;
	}

	public void setPaytypeflag(int paytypeflag) {
		this.paytypeflag = paytypeflag;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getBusiness_no() {
		return business_no;
	}

	public void setBusiness_no(String business_no) {
		this.business_no = business_no;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getDeliveryReason() {
		return deliveryReason;
	}

	public void setDeliveryReason(String deliveryReason) {
		this.deliveryReason = deliveryReason;
	}

	public String getPeisongqufen() {
		return peisongqufen;
	}

	public void setPeisongqufen(String peisongqufen) {
		this.peisongqufen = peisongqufen;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPaytypeid1() {
		return paytypeid1;
	}

	public void setPaytypeid1(String paytypeid1) {
		this.paytypeid1 = paytypeid1;
	}

	public String getPayamount1() {
		return payamount1;
	}

	public void setPayamount1(String payamount1) {
		this.payamount1 = payamount1;
	}

	public String getPaytypeid2() {
		return paytypeid2;
	}

	public void setPaytypeid2(String paytypeid2) {
		this.paytypeid2 = paytypeid2;
	}

	public String getPayamount2() {
		return payamount2;
	}

	public void setPayamount2(String payamount2) {
		this.payamount2 = payamount2;
	}

	public String getTerminal_no() {
		return terminal_no;
	}

	public void setTerminal_no(String terminal_no) {
		this.terminal_no = terminal_no;
	}

	public String getCard_bank() {
		return card_bank;
	}

	public void setCard_bank(String card_bank) {
		this.card_bank = card_bank;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getOk_date() {
		return ok_date;
	}

	public void setOk_date(String ok_date) {
		this.ok_date = ok_date;
	}

	public String getInamt_amt() {
		return inamt_amt;
	}

	public void setInamt_amt(String inamt_amt) {
		this.inamt_amt = inamt_amt;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getSys_no() {
		return sys_no;
	}

	public void setSys_no(String sys_no) {
		this.sys_no = sys_no;
	}

	public String getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	public String getInamt_date() {
		return inamt_date;
	}

	public void setInamt_date(String inamt_date) {
		this.inamt_date = inamt_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeno() {
		return consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = consigneeno;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getMessage_content() {
		return message_content;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}

	// ///////////////短信部分//////////////////////////////////////
	private String consigneename; // 顾客姓名
	private String consigneeno; // 客户代码
	private String mobilephone; // 手机号
	private String message_type; // 短信类型 21：订购出库 22：交换出库 23：回收
	private String message_content;// 要求数字，符号和汉字总数控制在60以内

}
