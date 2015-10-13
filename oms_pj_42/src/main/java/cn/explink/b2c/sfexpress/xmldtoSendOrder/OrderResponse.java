package cn.explink.b2c.sfexpress.xmldtoSendOrder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下单至顺丰返回的bean
 * 
 * @author Administrator
 *
 */

public class OrderResponse {
	private String orderid;// 订单号
	private String mailno; // 运单号
	private String origincode; // 原寄，码地址
	private String destcode; // 目的地代码
	private String filter_result; // 筛选结果
	private String remark; // 备注
	private String return_tracking_no; //

	@XmlElement(name = "orderid")
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getOrigincode() {
		return origincode;
	}

	public void setOrigincode(String origincode) {
		this.origincode = origincode;
	}

	public String getDestcode() {
		return destcode;
	}

	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}

	public String getFilter_result() {
		return filter_result;
	}

	public void setFilter_result(String filter_result) {
		this.filter_result = filter_result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReturn_tracking_no() {
		return return_tracking_no;
	}

	public void setReturn_tracking_no(String return_tracking_no) {
		this.return_tracking_no = return_tracking_no;
	}

}
