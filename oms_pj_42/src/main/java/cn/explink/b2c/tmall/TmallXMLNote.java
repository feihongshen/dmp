package cn.explink.b2c.tmall;

/**
 * tmall XML的节点
 * 
 * @author Administrator
 *
 */
public class TmallXMLNote {

	private String out_biz_code; // 外部业务编码,用来去重同一个合作伙伴唯一
	private String order_code; // 物流宝订单号 shipcwb
	private String tms_order_code; // 运单号 cwb
	private String operator; // 操作人
	private String operator_contact; // 操作联系人电话
	private String operator_date; // 操作时间 格式 yyyy-mm-dd hh:mm:ss
	private String status; // Tmall推送 状态 含枚举
	private String content; // 状态更改描述,流程状态描述
	private String remark; // 备注信息
	private double receivablefee; // 应收款
	private String service_code; // 服务编码code

	// /////////////2014-06-09新异常接口新增字段/////////////////////////////////////////
	private String cpCode; // 编码

	private String logisticsId; // 物流订单号
	private String mailNo;// 运单号
	private String exceptionCode; // 异常编码
	private String actionCode; // 指令编码

	private String exceptionTime; // 异常发生时间
	private String nextDispatchTime; // 下一次派送时间

	private String operatorMobile; // 异常操作人手机
	private String operatorPhone; // 异常操作人电话
	private String exceptionHappenedPlace; // 异常发生地点
	private String extendHandlingContent; // 其他异常处理信息(格式：key1:value1;key2:value2)
											// 扩展字段
	private String actionPushCode; // 推送指令 old 老接口 ， new 新接口

	public String getActionPushCode() {
		return actionPushCode;
	}

	public void setActionPushCode(String actionPushCode) {
		this.actionPushCode = actionPushCode;
	}

	public String getNextDispatchTime() {
		return nextDispatchTime;
	}

	public void setNextDispatchTime(String nextDispatchTime) {
		this.nextDispatchTime = nextDispatchTime;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getLogisticsId() {
		return logisticsId;
	}

	public void setLogisticsId(String logisticsId) {
		this.logisticsId = logisticsId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(String exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public String getOperatorMobile() {
		return operatorMobile;
	}

	public void setOperatorMobile(String operatorMobile) {
		this.operatorMobile = operatorMobile;
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public String getExceptionHappenedPlace() {
		return exceptionHappenedPlace;
	}

	public void setExceptionHappenedPlace(String exceptionHappenedPlace) {
		this.exceptionHappenedPlace = exceptionHappenedPlace;
	}

	public String getExtendHandlingContent() {
		return extendHandlingContent;
	}

	public void setExtendHandlingContent(String extendHandlingContent) {
		this.extendHandlingContent = extendHandlingContent;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public double getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(double receivablefee) {
		this.receivablefee = receivablefee;
	}

	private String multi_shipcwb; // 存储tmall物流宝订单号，逗号隔开的订单号

	public String getMulti_shipcwb() {
		return multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public String getOut_biz_code() {
		return out_biz_code;
	}

	public void setOut_biz_code(String out_biz_code) {
		this.out_biz_code = out_biz_code;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getTms_order_code() {
		return tms_order_code;
	}

	public void setTms_order_code(String tms_order_code) {
		this.tms_order_code = tms_order_code;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator_contact() {
		return operator_contact;
	}

	public void setOperator_contact(String operator_contact) {
		this.operator_contact = operator_contact;
	}

	public String getOperator_date() {
		return operator_date;
	}

	public void setOperator_date(String operator_date) {
		this.operator_date = operator_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
