package cn.explink.b2c.gztl;

public class GztlXmlNote {
	/**
	 * 此为广州通路返回订单信息的订单信息字段
	 */
	private String id;// 序列号，用于接收成功后返回标识
	private String myNo;// 运单编号
	private String logisticid;// 订单号
	private String custorderno;// 客户订单号
	private String opType;// 反馈类型(由飞远提供)
	private String state;// 订单状态(由飞远提供)
	private String returnState;// 网点反馈状态(由飞远提供)
	private String returnCause;// 网点反馈原因(由飞远提供)
	private String returnRemark;// 网点反馈备注(由飞远提供)
	private String signname;// 签收人
	private String opDt;// 网点反馈时间/签收时间/导入时间/出入库时间
	private String emp;// 网点反馈用户
	private String unit;// 反馈网点名称
	private String empSend;// 派件员/反馈人/导入信息人员/出入库人
	private String returnStatedesc;// 订单状态详情
	private String cuscode;// 供货商代码(由飞远提供)
	private String receiverName;// 收件人
	private String receiverMobile;// 收件人电话
	private String customername;// 供货商(由飞远提供)
	private String senderName;// 寄件人
	private String senderMobile;// 寄件手机
	private String payinamount;// 代收货款
	private String arrivedate;// 最初扫描时间
	private String lspabbr;// 配送区域
	private String pcs;// 件数
	private String business;// 订单类型（正常配送，委托取件，换货
	//private String iszhuangzhuan;//判断是从站点到中转站与到库房（0）还是出库（1）
	
	

	public String getPcs() {
		return this.pcs;
	}

	public void setPcs(String pcs) {
		this.pcs = pcs;
	}

	public String getBusiness() {
		return this.business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMyNo() {
		return this.myNo;
	}

	public void setMyNo(String myNo) {
		this.myNo = myNo;
	}

	public String getLogisticid() {
		return this.logisticid;
	}

	public void setLogisticid(String logisticid) {
		this.logisticid = logisticid;
	}

	public String getCustorderno() {
		return this.custorderno;
	}

	public void setCustorderno(String custorderno) {
		this.custorderno = custorderno;
	}

	public String getOpType() {
		return this.opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReturnState() {
		return this.returnState;
	}

	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}

	public String getReturnCause() {
		return this.returnCause;
	}

	public void setReturnCause(String returnCause) {
		this.returnCause = returnCause;
	}

	public String getReturnRemark() {
		return this.returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}

	public String getSignname() {
		return this.signname;
	}

	public void setSignname(String signname) {
		this.signname = signname;
	}

	public String getOpDt() {
		return this.opDt;
	}

	public void setOpDt(String opDt) {
		this.opDt = opDt;
	}

	public String getEmp() {
		return this.emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getEmpSend() {
		return this.empSend;
	}

	public void setEmpSend(String empSend) {
		this.empSend = empSend;
	}

	public String getReturnStatedesc() {
		return this.returnStatedesc;
	}

	public void setReturnStatedesc(String returnStatedesc) {
		this.returnStatedesc = returnStatedesc;
	}

	public String getCuscode() {
		return this.cuscode;
	}

	public void setCuscode(String cuscode) {
		this.cuscode = cuscode;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return this.receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getSenderName() {
		return this.senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderMobile() {
		return this.senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getPayinamount() {
		return this.payinamount;
	}

	public void setPayinamount(String payinamount) {
		this.payinamount = payinamount;
	}

	public String getArrivedate() {
		return this.arrivedate;
	}

	public void setArrivedate(String arrivedate) {
		this.arrivedate = arrivedate;
	}

	public String getLspabbr() {
		return this.lspabbr;
	}

	public void setLspabbr(String lspabbr) {
		this.lspabbr = lspabbr;
	}

}
