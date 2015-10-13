package cn.explink.b2c.maisike.senddata_json;

/**
 * 订单状态列表集合
 * 
 * @author Administrator
 *
 */
public class OrderStatus {

	private String ostatus = "";// 订单状态名称，如：在途、已发货、转发等
	private String otime = "";// 订单状态产生时间
	private String oremark = ""; // 订单状态生成描述

	public String getOstatus() {
		return ostatus;
	}

	public void setOstatus(String ostatus) {
		this.ostatus = ostatus;
	}

	public String getOtime() {
		return otime;
	}

	public void setOtime(String otime) {
		this.otime = otime;
	}

	public String getOremark() {
		return oremark;
	}

	public void setOremark(String oremark) {
		this.oremark = oremark;
	}

}
