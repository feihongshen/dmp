package cn.explink.b2c.huitongtx;

import java.math.BigDecimal;

/**
 * tmall XML的节点
 * 
 * @author Administrator
 *
 */
public class HuitongXMLNote {

	private String taskcode; // 订单唯一识别ID ，在dmp获取数据的时候存入的值

	private String servercode; // 服务编码(订单下发时的servercode)
	private String operator_name; // 操作人
	private String operator_contact; // 操作联系人电话
	private String operator_date; // 操作时间 格式 yyyy-mm-dd hh:mm:ss
	private String status; // Tmall推送 状态 含枚举
	private String content; // 状态更改描述,流程状态描述
	private BigDecimal receivablefee; // 应收款

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getServercode() {
		return servercode;
	}

	public void setServercode(String servercode) {
		this.servercode = servercode;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
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

	private String remark; // 备注信息

}
