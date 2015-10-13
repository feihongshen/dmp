package cn.explink.b2c.sfexpress.xmldtoSearch;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下单至顺丰返回的bean
 * 
 * @author Administrator
 *
 */

public class Route {

	private String accept_time;// 路由发生时间
	private Body accept_address; // 路由发生地址
	private String opCode; // 操作码
	private String remark; // 详细说明

	@XmlElement(name = "accept_time")
	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	@XmlElement(name = "accept_address")
	public Body getAccept_address() {
		return accept_address;
	}

	public void setAccept_address(Body accept_address) {
		this.accept_address = accept_address;
	}

	@XmlElement(name = "opCode")
	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
