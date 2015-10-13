package cn.explink.b2c.maikolin;

public class MaikolinXMLNote {
	String express_name;// 快递公司名称
	String contact_name;// 客户姓名
	String contact_phone;// 客户联系电话
	String city;// 城市
	String zone;// 区县
	String address;// 详细地址(把省市区加在前面）
	int itemtotal;// 包裹包含的商品件数合计
	String weight;// 重量
	String warehousearea;// 仓库名称
	String remark;// 送货备注
	String product_id;// 商品号
	String product_name;// 商品名称
	int quantity;// 商品数量
	String lntype;//
	private String cwb; // 运单号
	private String deliverytime;// 操作时间
	private String orderid;// 配送结束编号
	// 入库
	private String courier;// 快递员姓名
	private String couriertophone;// 快递员联系电话
	private String courierdate;// 分配时间(精确到时分)
	private String status;// 操作详情状态
	// 分站到货
	private String express_id;// 快递公司ID
	private String package_id;// 包裹单号
	private String otstatus;// 回单状态
	private String operation;// 操作人
	// 已审核
	String emsid;// 运单号
	String statepprovregion;// 到达省份
	String operatetime;// 操作时间
	String errocode;
	String erromassage;

	public String getErrocode() {
		return errocode;
	}

	public void setErrocode(String errocode) {
		this.errocode = errocode;
	}

	public String getErromassage() {
		return erromassage;
	}

	public void setErromassage(String erromassage) {
		this.erromassage = erromassage;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getEmsid() {
		return emsid;
	}

	public void setEmsid(String emsid) {
		this.emsid = emsid;
	}

	public String getStatepprovregion() {
		return statepprovregion;
	}

	public void setStatepprovregion(String statepprovregion) {
		this.statepprovregion = statepprovregion;
	}

	public String getOperatetime() {
		return operatetime;
	}

	public void setOperatetime(String operatetime) {
		this.operatetime = operatetime;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getOtstatus() {
		return otstatus;
	}

	public void setOtstatus(String otstatus) {
		this.otstatus = otstatus;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCourier() {
		return courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getCouriertophone() {
		return couriertophone;
	}

	public void setCouriertophone(String couriertophone) {
		this.couriertophone = couriertophone;
	}

	public String getCourierdate() {
		return courierdate;
	}

	public void setCourierdate(String courierdate) {
		this.courierdate = courierdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpress_name() {
		return express_name;
	}

	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getItemtotal() {
		return itemtotal;
	}

	public void setItemtotal(int itemtotal) {
		this.itemtotal = itemtotal;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWarehousearea() {
		return warehousearea;
	}

	public void setWarehousearea(String warehousearea) {
		this.warehousearea = warehousearea;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getLntype() {
		return lntype;
	}

	public void setLntype(String lntype) {
		this.lntype = lntype;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

}
