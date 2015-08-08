/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 配送员派费订单
 *
 * @author wangqiang
 */
public class DiliverymanPaifeiOrder {
	private Integer id;
	private String ordernumber;// 订单号
	private String billbatch;//账单编号
	private Integer orderstatus;// 订单状态
	private Integer ordertype;// 订单类型
	private String timeofdelivery;// 到货时间
	private String deliverytime;// 发货时间
	private Integer paymentmode;// 付款方式
	private String dateoflodgment;// 签收日期
	private BigDecimal paifeicombined;// 派费合计
	private BigDecimal basicpaifei;// 基本派费
	private BigDecimal subsidiesfee;// 代收补助费
	private BigDecimal areasubsidies;// 区域属性补助费
	private BigDecimal beyondareasubsidies;// 超出区域补助费
	private BigDecimal businesssubsidies;// 业务补助
	private BigDecimal delaysubsidies;// 脱单补助

	/**
	 *
	 */
	public DiliverymanPaifeiOrder() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrdernumber() {
		return this.ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	
	public String getBillbatch() {
		return billbatch;
	}

	public void setBillbatch(String billbatch) {
		this.billbatch = billbatch;
	}

	public Integer getOrderstatus() {
		return this.orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public Integer getOrdertype() {
		return this.ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public String getTimeofdelivery() {
		return this.timeofdelivery;
	}

	public void setTimeofdelivery(String timeofdelivery) {
		this.timeofdelivery = timeofdelivery;
	}

	public String getDeliverytime() {
		return this.deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public Integer getPaymentmode() {
		return this.paymentmode;
	}

	public void setPaymentmode(Integer paymentmode) {
		this.paymentmode = paymentmode;
	}

	public String getDateoflodgment() {
		return this.dateoflodgment;
	}

	public void setDateoflodgment(String dateoflodgment) {
		this.dateoflodgment = dateoflodgment;
	}

	public BigDecimal getPaifeicombined() {
		return this.paifeicombined;
	}

	public void setPaifeicombined(BigDecimal paifeicombined) {
		this.paifeicombined = paifeicombined;
	}

	public BigDecimal getBasicpaifei() {
		return this.basicpaifei;
	}

	public void setBasicpaifei(BigDecimal basicpaifei) {
		this.basicpaifei = basicpaifei;
	}

	public BigDecimal getSubsidiesfee() {
		return this.subsidiesfee;
	}

	public void setSubsidiesfee(BigDecimal subsidiesfee) {
		this.subsidiesfee = subsidiesfee;
	}

	public BigDecimal getAreasubsidies() {
		return this.areasubsidies;
	}

	public void setAreasubsidies(BigDecimal areasubsidies) {
		this.areasubsidies = areasubsidies;
	}

	public BigDecimal getBeyondareasubsidies() {
		return this.beyondareasubsidies;
	}

	public void setBeyondareasubsidies(BigDecimal beyondareasubsidies) {
		this.beyondareasubsidies = beyondareasubsidies;
	}

	public BigDecimal getBusinesssubsidies() {
		return this.businesssubsidies;
	}

	public void setBusinesssubsidies(BigDecimal businesssubsidies) {
		this.businesssubsidies = businesssubsidies;
	}

	public BigDecimal getDelaysubsidies() {
		return this.delaysubsidies;
	}

	public void setDelaysubsidies(BigDecimal delaysubsidies) {
		this.delaysubsidies = delaysubsidies;
	}

}
