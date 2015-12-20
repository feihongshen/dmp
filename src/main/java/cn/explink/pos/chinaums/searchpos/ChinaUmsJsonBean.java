package cn.explink.pos.chinaums.searchpos;

/**
 * 返回的Json bean对象--根据单号查询接口
 * 
 * @author Administrator
 *
 */
public class ChinaUmsJsonBean {

	private String order_no; // 运单号
	private String dsorder_no; // 订单号
	private String cod; // 代收款
	private String pay_way; // 支付方式
	private String pos_trace; // POS机的流水号
	private String trace_time; // 交易时间
	private String card_id; // 银行卡号
	private String signflag; // 本人签收标记
	private String signer; // 签收人

	private String dssn; // 电商编号
	private String dsname; // 电商名称
	private String erp_status;// ERP状态
	private String erp_date; // ERP通知时间
	private String ds_status; // DS状态
	private String ds_date; // DS通知时间
	private String tt_status;// 妥投状态
	private String tt_date; // 妥投通知时间
	private String mac; // 签名sign

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getDsorder_no() {
		return dsorder_no;
	}

	public void setDsorder_no(String dsorder_no) {
		this.dsorder_no = dsorder_no;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getPay_way() {
		return pay_way;
	}

	public void setPay_way(String pay_way) {
		this.pay_way = pay_way;
	}

	public String getPos_trace() {
		return pos_trace;
	}

	public void setPos_trace(String pos_trace) {
		this.pos_trace = pos_trace;
	}

	public String getTrace_time() {
		return trace_time;
	}

	public void setTrace_time(String trace_time) {
		this.trace_time = trace_time;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getSignflag() {
		return signflag;
	}

	public void setSignflag(String signflag) {
		this.signflag = signflag;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getDssn() {
		return dssn;
	}

	public void setDssn(String dssn) {
		this.dssn = dssn;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getErp_status() {
		return erp_status;
	}

	public void setErp_status(String erp_status) {
		this.erp_status = erp_status;
	}

	public String getErp_date() {
		return erp_date;
	}

	public void setErp_date(String erp_date) {
		this.erp_date = erp_date;
	}

	public String getDs_status() {
		return ds_status;
	}

	public void setDs_status(String ds_status) {
		this.ds_status = ds_status;
	}

	public String getDs_date() {
		return ds_date;
	}

	public void setDs_date(String ds_date) {
		this.ds_date = ds_date;
	}

	public String getTt_status() {
		return tt_status;
	}

	public void setTt_status(String tt_status) {
		this.tt_status = tt_status;
	}

	public String getTt_date() {
		return tt_date;
	}

	public void setTt_date(String tt_date) {
		this.tt_date = tt_date;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
