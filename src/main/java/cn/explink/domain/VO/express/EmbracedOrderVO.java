package cn.explink.domain.VO.express;

import java.io.Serializable;

import cn.explink.util.SecurityUtil;
public class EmbracedOrderVO implements Serializable {
	/*
	 * 运单号
	 */
	private String orderNo;
	/*
	 * 小件员id(揽件)
	 */
	private String delivermanId;
	/*
	 * 小件员(揽件)
	 */
	private String delivermanName;
	/*
	 * 小件员id(派件)
	 */
	private String deliverid;
	/*
	 * 小件员(派件)
	 */
	private String delivername;
	/*
	 * 寄件人-省id
	 */
	private String sender_provinceid;
	/*
	 *寄件人-省
	 */
	private String sender_provinceName;
	/*
	 *寄件人-市id
	 */
	private String sender_cityid;
	/*
	 *寄件人-市
	 */
	private String sender_cityName;
	/*
	 *寄件人手机号码
	 */
	private String sender_cellphone;
	/*
	 *寄件人固话
	 */
	private String sender_telephone;
	/*
	 *寄件人地址
	 */
	private String sender_adress;
	/*
	 *收件人-省id
	 */
	private String consignee_provinceid;
	/*
	 *收件人-省
	 */
	private String consignee_provinceName;
	/*
	 *收件人-市id
	 */
	private String consignee_cityid;
	/*
	 *收件人-市
	 */
	private String consignee_cityName;
	/*
	 *收件人手机号码
	 */
	private String consignee_cellphone;
	/*
	 *收件人固话
	 */
	private String consignee_telephone;
	/*
	 *收件人地址
	 */
	private String consignee_adress;
	/*
	 *件数
	 */
	private String number;
	/*
	 *运费
	 */
	private String freight;
	/*
	 * 是否补录（0：否，1：是）
	 */
	private String isadditionflag;
	/*
	 * 录入操作人id
	 */
	private String inputhandlerid;
	/*
	 * 录入操作人姓名
	 */
	private String inputhandlername;
	/*
	 * 录入时间
	 */
	private String inputdatetime;
	/*
	 * 寄件人客户编码
	 */
	private String sender_No;
	/*
	 * 寄件人公司
	 */
	private String sender_companyName;
	/*
	 * 寄件人客户id
	 */
	private Long sender_customerid;
	/*
	 * 寄件人
	 */
	private String sender_name;
	/*
	 * 寄件人区id
	 */
	private String sender_countyid;
	/*
	 * 寄件人区
	 */
	private String sender_countyName;
	/*
	 * 寄件人街道id
	 */
	private String sender_townid;
	/*
	 * 寄件人街道
	 */
	private String sender_townName;
	/*
	 * 寄件人证件号
	 */
	private String sender_certificateNo;
	/*
	 * 收件人客户编码
	 */
	private String consignee_No;
	/*
	 * 收件人公司
	 */
	private String consignee_companyName;
	/*
	 * 收件人客户id
	 */
	private Long consignee_customerid;
	/*
	 * 收件人
	 */
	private String consignee_name;
	/*
	 * 收件人区id
	 */
	private String consignee_countyid;
	/*
	 * 收件人区
	 */
	private String consignee_countyName;
	/*
	 * 收件人街道id
	 */
	private String consignee_townid;
	/*
	 * 收件人街道
	 */
	private String consignee_townName;
	/*
	 * 收件人证件号
	 */
	private String consignee_certificateNo;
	/*
	 * 委托内容/名称
	 */
	private String goods_name;
	/*
	 * 数量
	 */
	private String goods_number;
	/*
	 * 重量
	 */
	private String goods_weight;
	/*
	 * 长
	 */
	private String goods_longth;
	/*
	 * 宽
	 */
	private String goods_width;
	/*
	 * 高
	 */
	private String goods_height;
	/*
	 * KGS
	 */
	private String goods_kgs;
	/*
	 * 其他
	 */
	private String goods_other;
	/*
	 * 是否代收货款（0：否，1：是）
	 */
	private String collection;
	/*
	 * 代收货款金额
	 */
	private String collection_amount;
	/*
	 * 包装费用
	 */
	private String packing_amount;
	/*
	 * 是否有保价（0：否，1：是）
	 */
	private String insured;
	/*
	 * 保价声明价值
	 */
	private String insured_amount;
	/*
	 * 保价费用
	 */
	private String insured_cost;
	/*
	 * 计费重量
	 */
	private String charge_weight;
	/*
	 * 实际重量/重量
	 */
	private String actual_weight;
	/*
	 * 费用合计（运费+包装+保价）
	 */
	private String freight_total;
	/*
	 * 始发点
	 */
	private String origin_adress;
	/*
	 * 目的地
	 */
	private String destination;
	/*
	 * 付款方式（1：现付，2：到付，0：月结）
	 */
	private String payment_method;
	/*
	 * 月结账号
	 */
	private String monthly_account_number;
	/*
	 * 备注
	 */
	private String remarks;
	/*
	 * 订单类型
	 */
	private String cwbordertypeid;
	/*
	 * 揽件入站操作人id
	 */
	private String instationhandlerid;
	/*
	 * 揽件入站操作人
	 */
	private String instationhandlername;
	/*
	 * 揽件入站站点id
	 */
	private Integer instationid;
	/*
	 * 揽件入站站点名称
	 */
	private String instationname;
	/*
	 * 揽件入站时间
	 */
	private String instationdatetime;
	/*
	 * 补录人id
	 */
	private String completehandlerid;
	/*
	 * 补录人姓名
	 */
	private String completehandlername;
	/*
	 * 补录时间
	 */
	private String completedatetime;
	/*
	 * 运单所处状态
	 */
	private String flowordertype;
	/*
	 * 货物尺寸
	 */
	private String carsize;
	
	//快递产品类型，服务产品
	private Integer express_product_type;
	
	//支付类型
	private Integer paywayid;
	
	//预约单号
	private String reserveOrderNo;
	
	//版本号
	private Long recordVersion;
	
	//tps运单号
	private String tpsTranscwb;

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo.trim();
	}

	public String getDelivermanId() {
		return this.delivermanId;
	}

	public void setDelivermanId(String delivermanId) {
		this.delivermanId = delivermanId;
	}

	public String getDelivermanName() {
		return this.delivermanName;
	}

	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}

	public String getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}

	public String getDelivername() {
		return this.delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getSender_provinceid() {
		return this.sender_provinceid;
	}

	public void setSender_provinceid(String sender_provinceid) {
		this.sender_provinceid = sender_provinceid;
	}

	public String getSender_provinceName() {
		return this.sender_provinceName;
	}

	public void setSender_provinceName(String sender_provinceName) {
		this.sender_provinceName = sender_provinceName;
	}

	public String getSender_cityid() {
		return this.sender_cityid;
	}

	public void setSender_cityid(String sender_cityid) {
		this.sender_cityid = sender_cityid;
	}

	public String getSender_cityName() {
		return this.sender_cityName;
	}

	public void setSender_cityName(String sender_cityName) {
		this.sender_cityName = sender_cityName;
	}

	public String getSender_cellphone() {
		return this.sender_cellphone;
	}

	public void setSender_cellphone(String sender_cellphone) {
		this.sender_cellphone = sender_cellphone;
	}

	public String getSender_telephone() {
		return this.sender_telephone;
	}

	public void setSender_telephone(String sender_telephone) {
		this.sender_telephone = sender_telephone;
	}

	public String getSender_adress() {
		return this.sender_adress;
	}

	public void setSender_adress(String sender_adress) {
		this.sender_adress = sender_adress;
	}

	public String getConsignee_provinceid() {
		return this.consignee_provinceid;
	}

	public void setConsignee_provinceid(String consignee_provinceid) {
		this.consignee_provinceid = consignee_provinceid;
	}

	public String getConsignee_provinceName() {
		return this.consignee_provinceName;
	}

	public void setConsignee_provinceName(String consignee_provinceName) {
		this.consignee_provinceName = consignee_provinceName;
	}

	public String getConsignee_cityid() {
		return this.consignee_cityid;
	}

	public void setConsignee_cityid(String consignee_cityid) {
		this.consignee_cityid = consignee_cityid;
	}

	public String getConsignee_cityName() {
		return this.consignee_cityName;
	}

	public void setConsignee_cityName(String consignee_cityName) {
		this.consignee_cityName = consignee_cityName;
	}

	public String getConsignee_cellphone() {
		return this.consignee_cellphone;
	}

	public void setConsignee_cellphone(String consignee_cellphone) {
		this.consignee_cellphone = consignee_cellphone;
	}

	public String getConsignee_telephone() {
		return SecurityUtil.getInstance().decrypt(this.consignee_telephone);
	}

	public void setConsignee_telephone(String consignee_telephone) {
		this.consignee_telephone = consignee_telephone;
	}

	public String getConsignee_adress() {
		return this.consignee_adress;
	}

	public void setConsignee_adress(String consignee_adress) {
		this.consignee_adress = consignee_adress;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFreight() {
		return this.freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getIsadditionflag() {
		return this.isadditionflag;
	}

	public void setIsadditionflag(String isadditionflag) {
		this.isadditionflag = isadditionflag;
	}

	public String getInputhandlerid() {
		return this.inputhandlerid;
	}

	public void setInputhandlerid(String inputhandlerid) {
		this.inputhandlerid = inputhandlerid;
	}

	public String getInputhandlername() {
		return this.inputhandlername;
	}

	public void setInputhandlername(String inputhandlername) {
		this.inputhandlername = inputhandlername;
	}

	public String getInputdatetime() {
		return this.inputdatetime;
	}

	public void setInputdatetime(String inputdatetime) {
		this.inputdatetime = inputdatetime;
	}

	public String getSender_No() {
		return this.sender_No;
	}

	public void setSender_No(String sender_No) {
		this.sender_No = sender_No;
	}

	public String getSender_companyName() {
		return this.sender_companyName;
	}

	public void setSender_companyName(String sender_companyName) {
		this.sender_companyName = sender_companyName;
	}

	public String getSender_name() {
		return this.sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_countyid() {
		return this.sender_countyid;
	}

	public void setSender_countyid(String sender_countyid) {
		this.sender_countyid = sender_countyid;
	}

	public String getSender_countyName() {
		return this.sender_countyName;
	}

	public void setSender_countyName(String sender_countyName) {
		this.sender_countyName = sender_countyName;
	}

	public String getSender_townid() {
		return this.sender_townid;
	}

	public void setSender_townid(String sender_townid) {
		this.sender_townid = sender_townid;
	}

	public String getSender_townName() {
		return this.sender_townName;
	}

	public void setSender_townName(String sender_townName) {
		this.sender_townName = sender_townName;
	}

	public String getSender_certificateNo() {
		return this.sender_certificateNo;
	}

	public void setSender_certificateNo(String sender_certificateNo) {
		this.sender_certificateNo = sender_certificateNo;
	}

	public String getConsignee_No() {
		return this.consignee_No;
	}

	public void setConsignee_No(String consignee_No) {
		this.consignee_No = consignee_No;
	}

	public String getConsignee_companyName() {
		return this.consignee_companyName;
	}

	public void setConsignee_companyName(String consignee_companyName) {
		this.consignee_companyName = consignee_companyName;
	}

	public String getConsignee_name() {
		return this.consignee_name;
	}

	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	public String getConsignee_countyid() {
		return this.consignee_countyid;
	}

	public void setConsignee_countyid(String consignee_countyid) {
		this.consignee_countyid = consignee_countyid;
	}

	public String getConsignee_countyName() {
		return this.consignee_countyName;
	}

	public void setConsignee_countyName(String consignee_countyName) {
		this.consignee_countyName = consignee_countyName;
	}

	public String getConsignee_townid() {
		return this.consignee_townid;
	}

	public void setConsignee_townid(String consignee_townid) {
		this.consignee_townid = consignee_townid;
	}

	public String getConsignee_townName() {
		return this.consignee_townName;
	}

	public void setConsignee_townName(String consignee_townName) {
		this.consignee_townName = consignee_townName;
	}

	public String getConsignee_certificateNo() {
		return this.consignee_certificateNo;
	}

	public void setConsignee_certificateNo(String consignee_certificateNo) {
		this.consignee_certificateNo = consignee_certificateNo;
	}

	public String getGoods_name() {
		return this.goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_number() {
		return this.goods_number;
	}

	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}

	public String getGoods_weight() {
		return this.goods_weight;
	}

	public void setGoods_weight(String goods_weight) {
		this.goods_weight = goods_weight;
	}

	public String getGoods_longth() {
		return this.goods_longth;
	}

	public void setGoods_longth(String goods_longth) {
		this.goods_longth = goods_longth;
	}

	public String getGoods_width() {
		return this.goods_width;
	}

	public void setGoods_width(String goods_width) {
		this.goods_width = goods_width;
	}

	public String getGoods_height() {
		return this.goods_height;
	}

	public void setGoods_height(String goods_height) {
		this.goods_height = goods_height;
	}

	public String getGoods_kgs() {
		return this.goods_kgs;
	}

	public void setGoods_kgs(String goods_kgs) {
		this.goods_kgs = goods_kgs;
	}

	public String getGoods_other() {
		return this.goods_other;
	}

	public void setGoods_other(String goods_other) {
		this.goods_other = goods_other;
	}

	public String getCollection() {
		return this.collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection_amount() {
		return this.collection_amount;
	}

	public void setCollection_amount(String collection_amount) {
		this.collection_amount = collection_amount;
	}

	public String getPacking_amount() {
		return this.packing_amount;
	}

	public void setPacking_amount(String packing_amount) {
		this.packing_amount = packing_amount;
	}

	public String getInsured() {
		return this.insured;
	}

	public void setInsured(String insured) {
		this.insured = insured;
	}

	public String getInsured_amount() {
		return this.insured_amount;
	}

	public void setInsured_amount(String insured_amount) {
		this.insured_amount = insured_amount;
	}

	public String getInsured_cost() {
		return this.insured_cost;
	}

	public void setInsured_cost(String insured_cost) {
		this.insured_cost = insured_cost;
	}

	public String getCharge_weight() {
		return this.charge_weight;
	}

	public void setCharge_weight(String charge_weight) {
		this.charge_weight = charge_weight;
	}

	public String getActual_weight() {
		return this.actual_weight;
	}

	public void setActual_weight(String actual_weight) {
		this.actual_weight = actual_weight;
	}

	public String getFreight_total() {
		return this.freight_total;
	}

	public void setFreight_total(String freight_total) {
		this.freight_total = freight_total;
	}

	public String getOrigin_adress() {
		return this.origin_adress;
	}

	public void setOrigin_adress(String origin_adress) {
		this.origin_adress = origin_adress;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPayment_method() {
		return this.payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getMonthly_account_number() {
		return this.monthly_account_number;
	}

	public void setMonthly_account_number(String monthly_account_number) {
		this.monthly_account_number = monthly_account_number;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getSender_customerid() {
		return this.sender_customerid;
	}

	public void setSender_customerid(Long sender_customerid) {
		this.sender_customerid = sender_customerid;
	}

	public Long getConsignee_customerid() {
		return this.consignee_customerid;
	}

	public void setConsignee_customerid(Long consignee_customerid) {
		this.consignee_customerid = consignee_customerid;
	}

	public String getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public Integer getInstationid() {
		return this.instationid;
	}

	public void setInstationid(Integer instationid) {
		this.instationid = instationid;
	}

	public String getInstationname() {
		return this.instationname;
	}

	public void setInstationname(String instationname) {
		this.instationname = instationname;
	}

	public String getCompletehandlerid() {
		return this.completehandlerid;
	}

	public void setCompletehandlerid(String completehandlerid) {
		this.completehandlerid = completehandlerid;
	}

	public String getCompletehandlername() {
		return this.completehandlername;
	}

	public void setCompletehandlername(String completehandlername) {
		this.completehandlername = completehandlername;
	}

	public String getCompletedatetime() {
		return this.completedatetime;
	}

	public void setCompletedatetime(String completedatetime) {
		this.completedatetime = completedatetime;
	}

	public String getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(String flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getInstationhandlerid() {
		return this.instationhandlerid;
	}

	public void setInstationhandlerid(String instationhandlerid) {
		this.instationhandlerid = instationhandlerid;
	}

	public String getInstationhandlername() {
		return this.instationhandlername;
	}

	public void setInstationhandlername(String instationhandlername) {
		this.instationhandlername = instationhandlername;
	}

	public String getInstationdatetime() {
		return this.instationdatetime;
	}

	public void setInstationdatetime(String instationdatetime) {
		this.instationdatetime = instationdatetime;
	}

	public String getCarsize() {
		return this.carsize;
	}

	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}

	public Integer getExpress_product_type() {
		return express_product_type;
	}

	public void setExpress_product_type(Integer express_product_type) {
		this.express_product_type = express_product_type;
	}

	public Integer getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(Integer paywayid) {
		this.paywayid = paywayid;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public Long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion;
	}

	public String getTpsTranscwb() {
		return tpsTranscwb;
	}

	public void setTpsTranscwb(String tpsTranscwb) {
		this.tpsTranscwb = tpsTranscwb;
	}
	
}
