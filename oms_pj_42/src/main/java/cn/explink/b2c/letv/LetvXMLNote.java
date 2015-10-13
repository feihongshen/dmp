package cn.explink.b2c.letv;

import cn.explink.b2c.letv.bean.LetvContent;

/**
 * 广州思迈签收，拒收，异常反馈的存储的对象，转为json——str
 * 
 * @author Administrator
 *
 */
public class LetvXMLNote {

	private String _3pl_no; // 快递公司标识

	private String service_no;
	private String waybill_no;
	private String order_no;
	private String sign_type;
	private String sign;
	private LetvContent content;

	public String get_3pl_no() {
		return _3pl_no;
	}

	public void set_3pl_no(String _3pl_no) {
		this._3pl_no = _3pl_no;
	}

	public String getService_no() {
		return service_no;
	}

	public void setService_no(String service_no) {
		this.service_no = service_no;
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

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public LetvContent getContent() {
		return content;
	}

	public void setContent(LetvContent content) {
		this.content = content;
	}

}
