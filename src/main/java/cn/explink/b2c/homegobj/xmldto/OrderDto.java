package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class OrderDto {

	private String invc_id; // 运单号 =cwb
	private String ord_id; // 订单号= transcwb
	private String hb_ord_id; // 合包号
	private String ord_type;// 订单状态00: 销售出库订单 10: 同品换货 20: 退货退款
	private String cod_amt; // 代收金额单位为元，精确到小数点后2位，上限为运单的原交易金额。
	private String remark;
	private String send_start_tim;
	private String send_end_time;

	private Send send; // 发件信息
	private Receiver receiver; // 收件信息

	private Page page; // 分页内容

	private Goods goods;

	private String length;
	private String width;
	private String height;
	private String weight;

	@XmlElement(name = "invc_id")
	public String getInvc_id() {
		return invc_id;
	}

	public void setInvc_id(String invc_id) {
		this.invc_id = invc_id;
	}

	@XmlElement(name = "ord_id")
	public String getOrd_id() {
		return ord_id;
	}

	public void setOrd_id(String ord_id) {
		this.ord_id = ord_id;
	}

	@XmlElement(name = "hb_ord_id")
	public String getHb_ord_id() {
		return hb_ord_id;
	}

	public void setHb_ord_id(String hb_ord_id) {
		this.hb_ord_id = hb_ord_id;
	}

	@XmlElement(name = "ord_type")
	public String getOrd_type() {
		return ord_type;
	}

	public void setOrd_type(String ord_type) {
		this.ord_type = ord_type;
	}

	@XmlElement(name = "cod_amt")
	public String getCod_amt() {
		return cod_amt;
	}

	public void setCod_amt(String cod_amt) {
		this.cod_amt = cod_amt;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "send_start_tim")
	public String getSend_start_tim() {
		return send_start_tim;
	}

	public void setSend_start_tim(String send_start_tim) {
		this.send_start_tim = send_start_tim;
	}

	@XmlElement(name = "send_end_time")
	public String getSend_end_time() {
		return send_end_time;
	}

	public void setSend_end_time(String send_end_time) {
		this.send_end_time = send_end_time;
	}

	@XmlElement(name = "sender")
	public Send getSend() {
		return send;
	}

	public void setSend(Send send) {
		this.send = send;
	}

	@XmlElement(name = "receiver")
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@XmlElement(name = "page")
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@XmlElement(name = "goods")
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@XmlElement(name = "length")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@XmlElement(name = "width")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@XmlElement(name = "height")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@XmlElement(name = "weight")
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

}
