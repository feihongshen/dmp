package cn.explink.b2c.haoxgou;

/**
 * 东方CJ XML的节点
 * 
 * @author Administrator
 *
 */
public class HaoXiangGouXMLNote {

	private String interfaceMethodname; // 接口方法名

	private String dlver_cd; // 配送公司 标示
	private String ord_id; // 订单号（9位）transcwb
	private String invc_id; // 运单号 cwb

	private String dlv_stat_cd; // 配送状态
	private String dlv_stat_date; // 配送状态时间 格式：yyyymmddhh24miss
	private String transit_info; // 中转信息，如：正送往XX站（XX城市）途中
	private String declined_rsn; // 拒收原因
	private String pass; // 密码

	private String payer_nm; // 支付人姓名
	private String pay_date; // 支付日期
	private String pay_cd; // 支付类型（01：现金、02：刷卡）
	private String pay_amt; // 支付金额
	private String pay_company_cd; // 支付公司代码，使用POS-PDA设备上的支付公司代码
	private String rtn_id; // 退货单
	private String payee; // 收款人姓名

	private long paytypeflag; // 付款标识 1配送， 2退货款垫付

	public long getPaytypeflag() {
		return paytypeflag;
	}

	public void setPaytypeflag(long paytypeflag) {
		this.paytypeflag = paytypeflag;
	}

	public String getInterfaceMethodname() {
		return interfaceMethodname;
	}

	public void setInterfaceMethodname(String interfaceMethodname) {
		this.interfaceMethodname = interfaceMethodname;
	}

	public String getDlver_cd() {
		return dlver_cd;
	}

	public void setDlver_cd(String dlver_cd) {
		this.dlver_cd = dlver_cd;
	}

	public String getOrd_id() {
		return ord_id;
	}

	public void setOrd_id(String ord_id) {
		this.ord_id = ord_id;
	}

	public String getInvc_id() {
		return invc_id;
	}

	public void setInvc_id(String invc_id) {
		this.invc_id = invc_id;
	}

	public String getDlv_stat_cd() {
		return dlv_stat_cd;
	}

	public void setDlv_stat_cd(String dlv_stat_cd) {
		this.dlv_stat_cd = dlv_stat_cd;
	}

	public String getDlv_stat_date() {
		return dlv_stat_date;
	}

	public void setDlv_stat_date(String dlv_stat_date) {
		this.dlv_stat_date = dlv_stat_date;
	}

	public String getTransit_info() {
		return transit_info;
	}

	public void setTransit_info(String transit_info) {
		this.transit_info = transit_info;
	}

	public String getDeclined_rsn() {
		return declined_rsn;
	}

	public void setDeclined_rsn(String declined_rsn) {
		this.declined_rsn = declined_rsn;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPayer_nm() {
		return payer_nm;
	}

	public void setPayer_nm(String payer_nm) {
		this.payer_nm = payer_nm;
	}

	public String getPay_date() {
		return pay_date;
	}

	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}

	public String getPay_cd() {
		return pay_cd;
	}

	public void setPay_cd(String pay_cd) {
		this.pay_cd = pay_cd;
	}

	public String getPay_amt() {
		return pay_amt;
	}

	public void setPay_amt(String pay_amt) {
		this.pay_amt = pay_amt;
	}

	public String getPay_company_cd() {
		return pay_company_cd;
	}

	public void setPay_company_cd(String pay_company_cd) {
		this.pay_company_cd = pay_company_cd;
	}

	public String getRtn_id() {
		return rtn_id;
	}

	public void setRtn_id(String rtn_id) {
		this.rtn_id = rtn_id;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getDisburse_amt() {
		return disburse_amt;
	}

	public void setDisburse_amt(String disburse_amt) {
		this.disburse_amt = disburse_amt;
	}

	public String getDisburse_date() {
		return disburse_date;
	}

	public void setDisburse_date(String disburse_date) {
		this.disburse_date = disburse_date;
	}

	private String disburse_amt; // 退款垫付金额
	private String disburse_date;// 退款垫付日期，格式：yyyymmddhh24miss

}
