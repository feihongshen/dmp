package cn.explink.b2c.letv.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 广州思迈签收，拒收，异常反馈的存储的对象，转为json——str
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "request")
public class LetvContent {

	private String biz_no; // 业务编码，用于防重
	private String waybill_no; // 运单号
	private String order_no; // 订单号
	private String service_no; // 接口服务编号，由乐视致新提供
	private String operator; // 操作人姓名
	private String operator_time; // 操作时间格式YYYY-MM-DD HH:mm:ss
	private String node_no; // 节点编码
	private String content; // 描述
	private String signee; // 签收人 当node_no=300 必填
	private String consignee_sign_flag; // Y本人签收 N他人代签
	private String exception_no; // 异常码 node_no=400 必填
	private String exception_description; // 异常码描述 node_no=400必填

	public String getBiz_no() {
		return biz_no;
	}

	public void setBiz_no(String biz_no) {
		this.biz_no = biz_no;
	}

	public String getWaybill_no() {
		return waybill_no;
	}

	public void setWaybill_no(String waybill_no) {
		this.waybill_no = waybill_no;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getService_no() {
		return service_no;
	}

	public void setService_no(String service_no) {
		this.service_no = service_no;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator_time() {
		return operator_time;
	}

	public void setOperator_time(String operator_time) {
		this.operator_time = operator_time;
	}

	public String getNode_no() {
		return node_no;
	}

	public void setNode_no(String node_no) {
		this.node_no = node_no;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSignee() {
		return signee;
	}

	public void setSignee(String signee) {
		this.signee = signee;
	}

	public String getConsignee_sign_flag() {
		return consignee_sign_flag;
	}

	public void setConsignee_sign_flag(String consignee_sign_flag) {
		this.consignee_sign_flag = consignee_sign_flag;
	}

	public String getException_no() {
		return exception_no;
	}

	public void setException_no(String exception_no) {
		this.exception_no = exception_no;
	}

	public String getException_description() {
		return exception_description;
	}

	public void setException_description(String exception_description) {
		this.exception_description = exception_description;
	}

}
