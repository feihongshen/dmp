package cn.explink.pos.unionpay.json;

public class Reqdata {

	private String ordernum; // 订单号
	private int companyid; // 商户id 默认为0，
	private int logisticsid;// 物流公司id

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public int getCompanyid() {
		return companyid;
	}

	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}

	public int getLogisticsid() {
		return logisticsid;
	}

	public void setLogisticsid(int logisticsid) {
		this.logisticsid = logisticsid;
	}

}
