package cn.explink.b2c.maisike.feedback_json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;

import cn.explink.b2c.tools.JacksonMapper;

class test1 {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOsn("K000049450");
		orderStatus.setOstatus("IN_STORE");
		orderStatus.setOrealpaytype("CASH");
		orderStatus.setOtime("2013-12-28 15:00:00");
		orderStatus.setOpuname("张三");
		orderStatus.setOremark("货物由张三分站到货扫描");
		String responses = JacksonMapper.getInstance().writeValueAsString(orderStatus);
		System.out.println(responses);
		OrderStatus orderStatus1 = JacksonMapper.getInstance().readValue(responses, OrderStatus.class);

	}

}

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatus {

	private String osn; // 订单号
	private String ostatus; // 状态
	private String orealpaytype; // 支付方式
	private float opaymoney; // 支付金额
	private String otime; // 时间
	private String opuname;// 订单状态变化处理人员
	private String oremark; // 订单状态变化备注
	private float oreturnmoney; // 退货金额

	private String odelivername; // 配送员

	public String getOsn() {
		return osn;
	}

	public void setOsn(String osn) {
		this.osn = osn;
	}

	public float getOreturnmoney() {
		return oreturnmoney;
	}

	public void setOreturnmoney(float oreturnmoney) {
		this.oreturnmoney = oreturnmoney;
	}

	public String getOdelivername() {
		return odelivername;
	}

	public void setOdelivername(String odelivername) {
		this.odelivername = odelivername;
	}

	public String getOstatus() {
		return ostatus;
	}

	public void setOstatus(String ostatus) {
		this.ostatus = ostatus;
	}

	public String getOrealpaytype() {
		return orealpaytype;
	}

	public void setOrealpaytype(String orealpaytype) {
		this.orealpaytype = orealpaytype;
	}

	public float getOpaymoney() {
		return opaymoney;
	}

	public void setOpaymoney(float opaymoney) {
		this.opaymoney = opaymoney;
	}

	public String getOtime() {
		return otime;
	}

	public void setOtime(String otime) {
		this.otime = otime;
	}

	public String getOpuname() {
		return opuname;
	}

	public void setOpuname(String opuname) {
		this.opuname = opuname;
	}

	public String getOremark() {
		return oremark;
	}

	public void setOremark(String oremark) {
		this.oremark = oremark;
	}

}
