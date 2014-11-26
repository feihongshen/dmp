package cn.explink.pos.alipay.searchpos;

/**
 * 返回交易结果列表
 * 
 * @author Administrator
 *
 */
public class TenPayBean {

	private String sign_type; // MD5
	private String input_charset; // utf-8
	private String sign; // 签名结果
	private String retcode; // 返回状态码，0表示成功，其他未定义
	private String retmsg; // 返回信息，如非空，为错误原因
	private String partner; // 商户号
	private String transaction_id; // 财付通交易号，28位长的数值，其中前10位为商户号，之后8位为订单产生的日期，如20090415，最后10位是流水号。
	private String out_trade_no; // 商户系统内部的订单号
	private String terminal_id; // Pos终端号

	private String account_no;// 刷卡银行卡号，非必填
	private String trade_date;// 订单支付时间，格式为yyyymmdd，如2009年12月25日表示为20091225。时区为GMT+8
								// beijing。
	private String total_fee;// 刷卡订单总金额，单位为分
	private String issue_bank;// 刷卡银行卡发卡行

	private String card_type; // 银行卡类型：0 借记卡；1贷记卡；2 准贷记卡；3 预付卡
	private String retri_ref_num;// 系统参考号。32个字符内、可包含字母,确保在商户系统唯一。
	private String is_cancel; // 是否撤销：0 否；1 是
	private String is_reverse;// 是否冲正：0 否；1 是

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getIssue_bank() {
		return issue_bank;
	}

	public void setIssue_bank(String issue_bank) {
		this.issue_bank = issue_bank;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getRetri_ref_num() {
		return retri_ref_num;
	}

	public void setRetri_ref_num(String retri_ref_num) {
		this.retri_ref_num = retri_ref_num;
	}

	public String getIs_cancel() {
		return is_cancel;
	}

	public void setIs_cancel(String is_cancel) {
		this.is_cancel = is_cancel;
	}

	public String getIs_reverse() {
		return is_reverse;
	}

	public void setIs_reverse(String is_reverse) {
		this.is_reverse = is_reverse;
	}

}
