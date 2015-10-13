package cn.explink.domain;

/**
 * POS刷卡记录实体类
 * 
 * @author Administrator
 *
 */
public class PosPayEntity {

	private String cwb; // 订单号
	private String pos_document; // 付款凭证号
	private String pos_payname; // 付款人
	private double pos_money; // 付款金额
	private int pos_signtype; // 签收类型 1.本人接受，2他人签收
	private String pos_signname; // 签收人
	private String pos_signtime; // 签收时间
	private String pos_signremark; // 签收备注

	private String pos_paydate; // 支付日期
	private String pos_remark; // POS备注
	private int pos_delivery; // 小件员id
	private String pos_deliveryname; // 小件员
	private int upbranchid; // 付款财务id。
	private long branchid; // 所属站点id
	private String branchname; // 所属站点名称
	private String pos_code; // 支付方
	private int id;
	private long customerid;
	private String customername;
	private String shiptime;
	private int pos_backoutflag;

	public int getPos_backoutflag() {
		return pos_backoutflag;
	}

	public void setPos_backoutflag(int pos_backoutflag) {
		this.pos_backoutflag = pos_backoutflag;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getShiptime() {
		return shiptime;
	}

	public void setShiptime(String shiptime) {
		this.shiptime = shiptime;
	}

	public String getPos_code() {
		return pos_code;
	}

	public void setPos_code(String pos_code) {
		this.pos_code = pos_code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getPos_document() {
		return pos_document;
	}

	public void setPos_document(String pos_document) {
		this.pos_document = pos_document;
	}

	public String getPos_payname() {
		return pos_payname;
	}

	public void setPos_payname(String pos_payname) {
		this.pos_payname = pos_payname;
	}

	public double getPos_money() {
		return pos_money;
	}

	public void setPos_money(double pos_money) {
		this.pos_money = pos_money;
	}

	public int getPos_signtype() {
		return pos_signtype;
	}

	public void setPos_signtype(int pos_signtype) {
		this.pos_signtype = pos_signtype;
	}

	public String getPos_signname() {
		return pos_signname;
	}

	public void setPos_signname(String pos_signname) {
		this.pos_signname = pos_signname;
	}

	public String getPos_signtime() {
		return pos_signtime;
	}

	public void setPos_signtime(String pos_signtime) {
		this.pos_signtime = pos_signtime;
	}

	public String getPos_paydate() {
		return pos_paydate;
	}

	public void setPos_paydate(String pos_paydate) {
		this.pos_paydate = pos_paydate;
	}

	public String getPos_remark() {
		return pos_remark;
	}

	public void setPos_remark(String pos_remark) {
		this.pos_remark = pos_remark;
	}

	public int getPos_delivery() {
		return pos_delivery;
	}

	public void setPos_delivery(int pos_delivery) {
		this.pos_delivery = pos_delivery;
	}

	public String getPos_deliveryname() {
		return pos_deliveryname;
	}

	public void setPos_deliveryname(String pos_deliveryname) {
		this.pos_deliveryname = pos_deliveryname;
	}

	public int getUpbranchid() {
		return upbranchid;
	}

	public void setUpbranchid(int upbranchid) {
		this.upbranchid = upbranchid;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getPos_signremark() {
		return pos_signremark;
	}

	public void setPos_signremark(String pos_signremark) {
		this.pos_signremark = pos_signremark;
	}

}
