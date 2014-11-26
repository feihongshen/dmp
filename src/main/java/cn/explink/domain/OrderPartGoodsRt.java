package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

public class OrderPartGoodsRt {

	String cwb;// 订单
	String consigneename;// 收件人名称
	String consigneeaddress;// 收件人地址
	BigDecimal receivablefee;// 代收货款应收金额
	String customer;// 取件承运商
	String createtime;// 领货时间
	String collectiontime;// 揽收时间
	long rtwarehouseid;// 退货仓库
	String rtwarehouseaddress;// 退货仓库地址
	List<OrderGoods> ordergoodsList;

	public List<OrderGoods> getOrdergoodsList() {
		return ordergoodsList;
	}

	public void setOrdergoodsList(List<OrderGoods> ordergoodsList) {
		this.ordergoodsList = ordergoodsList;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCollectiontime() {
		return collectiontime;
	}

	public void setCollectiontime(String collectiontime) {
		this.collectiontime = collectiontime;
	}

	public long getRtwarehouseid() {
		return rtwarehouseid;
	}

	public void setRtwarehouseid(long rtwarehouseid) {
		this.rtwarehouseid = rtwarehouseid;
	}

	public String getRtwarehouseaddress() {
		return rtwarehouseaddress;
	}

	public void setRtwarehouseaddress(String rtwarehouseaddress) {
		this.rtwarehouseaddress = rtwarehouseaddress;
	}
}
