package cn.explink.b2c.gztl.xmldto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
	@XmlElement(name = "typeid")
	private String typeid;// 配送类型
	@XmlElement(name = "orderid")
	private String orderid;// 订单号或运单号
	@XmlElement(name = "sclientcode")
	private String sclientcode;// 客户单号
	@XmlElement(name = "shipperid")
	private String shipperid;// 需要与通路系统基础表对应(由飞远提供)
	@XmlElement(name = "consignorname")
	private String consignorname;// 发货人
	@XmlElement(name = "consignoraddress")
	private String consignoraddress;// 发货人地址
	@XmlElement(name = "consignormobile")
	private String consignormobile;// 发货人手机
	@XmlElement(name = "consignorphone")
	private String consignorphone;// 发货人电话
	@XmlElement(name = "customername")
	private String customername;// 收货人
	@XmlElement(name = "customeraddress")
	private String customeraddress;// 收货人地址
	@XmlElement(name = "customermobile")
	private String customermobile;// 收货人手机
	@XmlElement(name = "customerphone")
	private String customerphone;// 收货人电话
	@XmlElement(name = "deliverygoods")
	private String deliverygoods;// 配送货物
	@XmlElement(name = "returngoods")
	private String returngoods;// 退货货物
	@XmlElement(name = "deliverygoodsprice")
	private String deliverygoodsprice;// 配送货物价格
	@XmlElement(name = "returngoodsprice")
	private String returngoodsprice;// 退货货物价格
	@XmlElement(name = "weight")
	private String weight;// 重量
	@XmlElement(name = "shouldreceive")
	private String shouldreceive;// 应收金额
	@XmlElement(name = "accuallyreceive")
	private String accuallyreceive;// 实收金额
	@XmlElement(name = "remark")
	private String remark;// 备注
	@XmlElement(name = "arrivedate")
	private String arrivedate;// 到货日期
	@XmlElement(name = "pushtime")
	private String pushtime;// 入库时间
	@XmlElement(name = "goodsnum")
	private String goodsnum;// 件数
	@XmlElement(name = "deliverarea")
	private String deliverarea;// 配送区域
	@XmlElement(name = "extPayType")
	private String extPayType;// 货到付款的付款方式:0：现金1：刷卡 2:空值
	@XmlElement(name = "orderBatchNo")
	private String orderBatchNo;// 交接单号，存放至d_order表orderno顺序号字段中
	@XmlElement(name = "otherservicefee")
	private String otherservicefee;// 用于德邦物流货到付款记录
	@XmlElement(name = "orderDate")
	private String orderDate;// 订单生成日期

	public String getTypeid() {
		return this.typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getOrderid() {
		return this.orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSclientcode() {
		return this.sclientcode;
	}

	public void setSclientcode(String sclientcode) {
		this.sclientcode = sclientcode;
	}

	public String getShipperid() {
		return this.shipperid;
	}

	public void setShipperid(String shipperid) {
		this.shipperid = shipperid;
	}

	public String getConsignorname() {
		return this.consignorname;
	}

	public void setConsignorname(String consignorname) {
		this.consignorname = consignorname;
	}

	public String getConsignoraddress() {
		return this.consignoraddress;
	}

	public void setConsignoraddress(String consignoraddress) {
		this.consignoraddress = consignoraddress;
	}

	public String getConsignormobile() {
		return this.consignormobile;
	}

	public void setConsignormobile(String consignormobile) {
		this.consignormobile = consignormobile;
	}

	public String getConsignorphone() {
		return this.consignorphone;
	}

	public void setConsignorphone(String consignorphone) {
		this.consignorphone = consignorphone;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomeraddress() {
		return this.customeraddress;
	}

	public void setCustomeraddress(String customeraddress) {
		this.customeraddress = customeraddress;
	}

	public String getCustomermobile() {
		return this.customermobile;
	}

	public void setCustomermobile(String customermobile) {
		this.customermobile = customermobile;
	}

	public String getCustomerphone() {
		return this.customerphone;
	}

	public void setCustomerphone(String customerphone) {
		this.customerphone = customerphone;
	}

	public String getDeliverygoods() {
		return this.deliverygoods;
	}

	public void setDeliverygoods(String deliverygoods) {
		this.deliverygoods = deliverygoods;
	}

	public String getReturngoods() {
		return this.returngoods;
	}

	public void setReturngoods(String returngoods) {
		this.returngoods = returngoods;
	}

	public String getDeliverygoodsprice() {
		return this.deliverygoodsprice;
	}

	public void setDeliverygoodsprice(String deliverygoodsprice) {
		this.deliverygoodsprice = deliverygoodsprice;
	}

	public String getReturngoodsprice() {
		return this.returngoodsprice;
	}

	public void setReturngoodsprice(String returngoodsprice) {
		this.returngoodsprice = returngoodsprice;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getShouldreceive() {
		return this.shouldreceive;
	}

	public void setShouldreceive(String shouldreceive) {
		this.shouldreceive = shouldreceive;
	}

	public String getAccuallyreceive() {
		return this.accuallyreceive;
	}

	public void setAccuallyreceive(String accuallyreceive) {
		this.accuallyreceive = accuallyreceive;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArrivedate() {
		return this.arrivedate;
	}

	public void setArrivedate(String arrivedate) {
		this.arrivedate = arrivedate;
	}

	public String getPushtime() {
		return this.pushtime;
	}

	public void setPushtime(String pushtime) {
		this.pushtime = pushtime;
	}

	public String getGoodsnum() {
		return this.goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public String getDeliverarea() {
		return this.deliverarea;
	}

	public void setDeliverarea(String deliverarea) {
		this.deliverarea = deliverarea;
	}

	public String getExtPayType() {
		return this.extPayType;
	}

	public void setExtPayType(String extPayType) {
		this.extPayType = extPayType;
	}

	public String getOrderBatchNo() {
		return this.orderBatchNo;
	}

	public void setOrderBatchNo(String orderBatchNo) {
		this.orderBatchNo = orderBatchNo;
	}

	public String getOtherservicefee() {
		return this.otherservicefee;
	}

	public void setOtherservicefee(String otherservicefee) {
		this.otherservicefee = otherservicefee;
	}

	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

}
