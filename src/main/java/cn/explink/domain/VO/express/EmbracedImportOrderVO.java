package cn.explink.domain.VO.express;

import java.io.Serializable;

import cn.explink.util.poi.excel.annotation.Excel;

/**
 * @description 快递运单补录导入按钮需要导入的列
 * @author  刘武强
 * @data   2015年10月10日
 */
public class EmbracedImportOrderVO implements Serializable {
	@Excel(exportName = "运单号", exportFieldWidth = 20)
	private String orderNo;
	@Excel(exportName = "寄件人", exportFieldWidth = 20)
	private String sender_name;
	@Excel(exportName = "寄件人单位名称", exportFieldWidth = 20)
	private String sender_companyName;
	@Excel(exportName = "客户账号", exportFieldWidth = 20)
	private String monthly_account_number;
	@Excel(exportName = "省（寄件人）", exportFieldWidth = 20)
	private String sender_provinceName;
	@Excel(exportName = "市（寄件人）", exportFieldWidth = 20)
	private String sender_cityName;
	@Excel(exportName = "区（寄件人）", exportFieldWidth = 20)
	private String sender_countyName;
	@Excel(exportName = "街道（寄件人）", exportFieldWidth = 20)
	private String sender_townName;
	@Excel(exportName = "地址（寄件人）", exportFieldWidth = 20)
	private String sender_adress;
	@Excel(exportName = "寄件人手机", exportFieldWidth = 20)
	private String sender_cellphone;
	@Excel(exportName = "寄件人固话", exportFieldWidth = 20)
	private String sender_telephone;
	@Excel(exportName = "商品名称", exportFieldWidth = 20)
	private String goods_name;
	@Excel(exportName = "数量", exportFieldWidth = 20)
	private String goods_number;
	@Excel(exportName = "重量", exportFieldWidth = 20)
	private String charge_weight;
	@Excel(exportName = "长", exportFieldWidth = 20)
	private String goods_length;
	@Excel(exportName = "宽", exportFieldWidth = 20)
	private String goods_width;
	@Excel(exportName = "高", exportFieldWidth = 20)
	private String goods_high;
	@Excel(exportName = "其他", exportFieldWidth = 20)
	private String goods_other;
	@Excel(exportName = "实际重量", exportFieldWidth = 20)
	private String actual_weight; //10.28加--刘武强
	@Excel(exportName = "收件人", exportFieldWidth = 20)
	private String consignee_name;
	@Excel(exportName = "省（收件人）", exportFieldWidth = 20)
	private String consignee_provinceName;
	@Excel(exportName = "市（收件人）", exportFieldWidth = 20)
	private String consignee_cityName;
	@Excel(exportName = "区（收件人）", exportFieldWidth = 20)
	private String consignee_countyName;
	@Excel(exportName = "街道（收件人）", exportFieldWidth = 20)
	private String consignee_townName;
	@Excel(exportName = "地址（收件人）", exportFieldWidth = 20)
	private String consignee_adress;
	@Excel(exportName = "收件人手机", exportFieldWidth = 20)
	private String consignee_cellphone;
	@Excel(exportName = "收件人固话", exportFieldWidth = 20)
	private String consignee_telephone;
	@Excel(exportName = "揽件员签字", exportFieldWidth = 20)
	private String delivermanName;
	@Excel(exportName = "现付", exportFieldWidth = 20)
	private String xianfu;
	@Excel(exportName = "到付", exportFieldWidth = 20)
	private String daofu;
	@Excel(exportName = "月结", exportFieldWidth = 20)
	private String yuejie;
	@Excel(exportName = "代收款", exportFieldWidth = 20)
	private String collection_amount;
	@Excel(exportName = "保价声明价值", exportFieldWidth = 20)
	private String Insured_value;
	@Excel(exportName = "保价", exportFieldWidth = 20)
	private String insured_cost;
	@Excel(exportName = "包装费用", exportFieldWidth = 20)
	private String packing_amount; //10.28加--刘武强
	private String errMsg;
	private String payment_method;
	private String freight;


	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSender_name() {
		return this.sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_companyName() {
		return this.sender_companyName;
	}

	public void setSender_companyName(String sender_companyName) {
		this.sender_companyName = sender_companyName;
	}

	public String getMonthly_account_number() {
		return this.monthly_account_number;
	}

	public void setMonthly_account_number(String monthly_account_number) {
		this.monthly_account_number = monthly_account_number;
	}

	public String getSender_provinceName() {
		return this.sender_provinceName;
	}

	public void setSender_provinceName(String sender_provinceName) {
		this.sender_provinceName = sender_provinceName;
	}

	public String getSender_cityName() {
		return this.sender_cityName;
	}

	public void setSender_cityName(String sender_cityName) {
		this.sender_cityName = sender_cityName;
	}

	public String getSender_countyName() {
		return this.sender_countyName;
	}

	public void setSender_countyName(String sender_countyName) {
		this.sender_countyName = sender_countyName;
	}

	public String getSender_townName() {
		return this.sender_townName;
	}

	public void setSender_townName(String sender_townName) {
		this.sender_townName = sender_townName;
	}

	public String getSender_adress() {
		return this.sender_adress;
	}

	public void setSender_adress(String sender_adress) {
		this.sender_adress = sender_adress;
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

	public String getCharge_weight() {
		return this.charge_weight;
	}

	public void setCharge_weight(String charge_weight) {
		this.charge_weight = charge_weight;
	}

	public String getConsignee_name() {
		return this.consignee_name;
	}

	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	public String getConsignee_provinceName() {
		return this.consignee_provinceName;
	}

	public void setConsignee_provinceName(String consignee_provinceName) {
		this.consignee_provinceName = consignee_provinceName;
	}

	public String getConsignee_countyName() {
		return this.consignee_countyName;
	}

	public void setConsignee_countyName(String consignee_countyName) {
		this.consignee_countyName = consignee_countyName;
	}

	public String getConsignee_adress() {
		return this.consignee_adress;
	}

	public void setConsignee_adress(String consignee_adress) {
		this.consignee_adress = consignee_adress;
	}

	public String getConsignee_cellphone() {
		return this.consignee_cellphone;
	}

	public void setConsignee_cellphone(String consignee_cellphone) {
		this.consignee_cellphone = consignee_cellphone;
	}

	public String getConsignee_telephone() {
		return this.consignee_telephone;
	}

	public void setConsignee_telephone(String consignee_telephone) {
		this.consignee_telephone = consignee_telephone;
	}

	public String getDelivermanName() {
		return this.delivermanName;
	}

	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}

	public String getXianfu() {
		return this.xianfu;
	}

	public void setXianfu(String xianfu) {
		this.xianfu = xianfu;
	}

	public String getDaofu() {
		return this.daofu;
	}

	public void setDaofu(String daofu) {
		this.daofu = daofu;
	}

	public String getYuejie() {
		return this.yuejie;
	}

	public void setYuejie(String yuejie) {
		this.yuejie = yuejie;
	}

	public String getCollection_amount() {
		return this.collection_amount;
	}

	public void setCollection_amount(String collection_amount) {
		this.collection_amount = collection_amount;
	}

	public String getInsured_cost() {
		return this.insured_cost;
	}

	public void setInsured_cost(String insured_cost) {
		this.insured_cost = insured_cost;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getConsignee_cityName() {
		return this.consignee_cityName;
	}

	public void setConsignee_cityName(String consignee_cityName) {
		this.consignee_cityName = consignee_cityName;
	}

	public String getPayment_method() {
		return this.payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getGoods_length() {
		return this.goods_length;
	}

	public void setGoods_length(String goods_length) {
		this.goods_length = goods_length;
	}

	public String getGoods_width() {
		return this.goods_width;
	}

	public void setGoods_width(String goods_width) {
		this.goods_width = goods_width;
	}

	public String getGoods_high() {
		return this.goods_high;
	}

	public void setGoods_high(String goods_high) {
		this.goods_high = goods_high;
	}

	public String getGoods_other() {
		return this.goods_other;
	}

	public void setGoods_other(String goods_other) {
		this.goods_other = goods_other;
	}

	public String getConsignee_townName() {
		return this.consignee_townName;
	}

	public void setConsignee_townName(String consignee_townName) {
		this.consignee_townName = consignee_townName;
	}

	public String getInsured_value() {
		return this.Insured_value;
	}

	public void setInsured_value(String insured_value) {
		this.Insured_value = insured_value;
	}

	public String getFreight() {
		return this.freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getPacking_amount() {
		return this.packing_amount;
	}

	public void setPacking_amount(String packing_amount) {
		this.packing_amount = packing_amount;
	}

	public String getActual_weight() {
		return this.actual_weight;
	}

	public void setActual_weight(String actual_weight) {
		this.actual_weight = actual_weight;
	}

}
