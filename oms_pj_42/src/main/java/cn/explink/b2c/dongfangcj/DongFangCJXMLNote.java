package cn.explink.b2c.dongfangcj;

/**
 * 东方CJ XML的节点
 * 
 * @author Administrator
 *
 */
public class DongFangCJXMLNote {

	private String paixubianhao; // 排序编号

	private String cwb; // 订单号
	private String deliverytime; // 配送日
	private String deliveryresult; // 配送结果 1.completed(完成) 2.拒收/退货
	private String exptreason; // 异常原因
	private String paytype1; // 付款方式 1 拒收/退货：00
	private String payamount1; // 付款金额
	private String paytype2; // 付款方式 1 拒收/退货：00
	private String payamount2; // 付款金额2

	private String payamount; // 付款金额, cod支付

	// ///////////回收单需求/////////////
	private String gobacktime; // 上传时间
	private String gobackflag; // 回收与否
	private String faildReason; // 失败原因r

	private int interfaceType; // 接口类型，用于区分新老接口推送标识，由于状态重复只能以此为准 1老接口， 2.新接口

	public int getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(int interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getGobacktime() {
		return gobacktime;
	}

	public void setGobacktime(String gobacktime) {
		this.gobacktime = gobacktime;
	}

	public String getGobackflag() {
		return gobackflag;
	}

	public void setGobackflag(String gobackflag) {
		this.gobackflag = gobackflag;
	}

	public String getFaildReason() {
		return faildReason;
	}

	public void setFaildReason(String faildReason) {
		this.faildReason = faildReason;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getPaixubianhao() {
		return paixubianhao;
	}

	public void setPaixubianhao(String paixubianhao) {
		this.paixubianhao = paixubianhao;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getDeliveryresult() {
		return deliveryresult;
	}

	public void setDeliveryresult(String deliveryresult) {
		this.deliveryresult = deliveryresult;
	}

	public String getExptreason() {
		return exptreason;
	}

	public void setExptreason(String exptreason) {
		this.exptreason = exptreason;
	}

	public String getPaytype1() {
		return paytype1;
	}

	public void setPaytype1(String paytype1) {
		this.paytype1 = paytype1;
	}

	public String getPayamount1() {
		return payamount1;
	}

	public void setPayamount1(String payamount1) {
		this.payamount1 = payamount1;
	}

	public String getPaytype2() {
		return paytype2;
	}

	public void setPaytype2(String paytype2) {
		this.paytype2 = paytype2;
	}

	public String getPayamount2() {
		return payamount2;
	}

	public void setPayamount2(String payamount2) {
		this.payamount2 = payamount2;
	}

}
