package cn.explink.pos.tonglianpos.delivery;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
public class Delivery {
	
	private String delivery_man;//物流员工号，登录用户名
	private String delivery_dept_no;//员工所在站点编辑
	private String order_no;//扫描请求单号
	
	@XmlElement(name="delivery_man")
	public String getDelivery_man() {
		return delivery_man;
	}
	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}
	
	@XmlElement(name="delivery_dept_no")
	public String getDelivery_dept_no() {
		return delivery_dept_no;
	}
	public void setDelivery_dept_no(String delivery_dept_no) {
		this.delivery_dept_no = delivery_dept_no;
	}
	@XmlElement(name="order_no")
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
}
