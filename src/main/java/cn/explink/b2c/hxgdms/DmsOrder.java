package cn.explink.b2c.hxgdms;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DmsOrder {
	@JsonProperty(value = "ClientCode")
	private String clientCode; // 运单号 cwb
	@JsonProperty(value = "WorkCode")
	private String workCode; // 运单号 transcwb
	@JsonProperty(value = "YWorkCode")
	private String yWorkCode; // 原运单号
	@JsonProperty(value = "RWorkCode")
	private String rWorkCode; // 退货号
	@JsonProperty(value = "OrderCount")
	private int orderCount; // 退货数量

	@JsonProperty(value = "ReplCost")
	private BigDecimal replCost; // 代收货款
	@JsonProperty(value = "OrderHav")
	private String orderHav; // 订单重量
	@JsonProperty(value = "OrderSize")
	private String orderSize; // 订单体积（立方厘米）
	@JsonProperty(value = "TrustPerson")
	private String trustPerson;// 寄件人
	@JsonProperty(value = "TrustProvice")
	private String trustProvice; // 寄件（省）
	@JsonProperty(value = "TrustCity")
	private String trustCity; // 寄件（市）
	@JsonProperty(value = "TrustCounty")
	private String trustCounty; // 寄件（区/县）
	@JsonProperty(value = "TrustStreet")
	private String trustStreet; // 街道
	@JsonProperty(value = "TrustAddress")
	private String trustAddress; // 寄件人详细地址
	@JsonProperty(value = "TrustTel")
	private String trustTel; // 寄件人电话
	@JsonProperty(value = "TrustPhone")
	private String trustPhone; // 寄件人手机
	@JsonProperty(value = "GetPerson")
	private String getPerson; // 收件人
	@JsonProperty(value = "GetProvice")
	private String getProvice; // 收件（省）
	@JsonProperty(value = "GetCity")
	private String getCity; // 收件（市）
	@JsonProperty(value = "GetCounty")
	private String getCounty; // 收件（区/县）
	@JsonProperty(value = "GetStreet")
	private String getStreet; // 街道
	@JsonProperty(value = "GetAddress")
	private String getAddress; // 收件人详细地址
	@JsonProperty(value = "GetTel")
	private String getTel; // 收件人电话
	@JsonProperty(value = "GetPhone")
	private String getPhone; // 收件人手机
	@JsonProperty(value = "WorkLarge")
	private int workLarge; // 大小件 0:大件 1：中件 2：小件
	@JsonProperty(value = "Note")
	private String note; // 备注
	@JsonProperty(value = "IsRecInvoices")
	private int isRecInvoices; // 是否回收发票

	public int getIsRecInvoices() {
		return isRecInvoices;
	}

	public void setIsRecInvoices(int isRecInvoices) {
		this.isRecInvoices = isRecInvoices;
	}

	/**
	 * 订单类型 0:销售单 1：换货单 2:拒收产生的退货单 3：破损产生的退货单 4：取消产生的退货单 5:换货退货对应拿回来退货单
	 * 6:签收后退货(退货且退款) 7:签收后退货(退货不退款)
	 */
	@JsonProperty(value = "WorkType")
	private int workType;
	@JsonProperty(value = "siteNo")
	private String siteNo; // 仓库号
	@JsonProperty(value = "GoodsInfo")
	private List<Good> goods;// 商品信息列表

	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getyWorkCode() {
		return yWorkCode;
	}

	public void setyWorkCode(String yWorkCode) {
		this.yWorkCode = yWorkCode;
	}

	public String getrWorkCode() {
		return rWorkCode;
	}

	public void setrWorkCode(String rWorkCode) {
		this.rWorkCode = rWorkCode;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public BigDecimal getReplCost() {
		return replCost;
	}

	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
	}

	public String getOrderHav() {
		return orderHav;
	}

	public void setOrderHav(String orderHav) {
		this.orderHav = orderHav;
	}

	public String getOrderSize() {
		return orderSize;
	}

	public void setOrderSize(String orderSize) {
		this.orderSize = orderSize;
	}

	public String getTrustPerson() {
		return trustPerson;
	}

	public void setTrustPerson(String trustPerson) {
		this.trustPerson = trustPerson;
	}

	public String getTrustProvice() {
		return trustProvice;
	}

	public void setTrustProvice(String trustProvice) {
		this.trustProvice = trustProvice;
	}

	public String getTrustCity() {
		return trustCity;
	}

	public void setTrustCity(String trustCity) {
		this.trustCity = trustCity;
	}

	public String getTrustCounty() {
		return trustCounty;
	}

	public void setTrustCounty(String trustCounty) {
		this.trustCounty = trustCounty;
	}

	public String getTrustStreet() {
		return trustStreet;
	}

	public void setTrustStreet(String trustStreet) {
		this.trustStreet = trustStreet;
	}

	public String getTrustAddress() {
		return trustAddress;
	}

	public void setTrustAddress(String trustAddress) {
		this.trustAddress = trustAddress;
	}

	public String getTrustTel() {
		return trustTel;
	}

	public void setTrustTel(String trustTel) {
		this.trustTel = trustTel;
	}

	public String getTrustPhone() {
		return trustPhone;
	}

	public void setTrustPhone(String trustPhone) {
		this.trustPhone = trustPhone;
	}

	public String getGetPerson() {
		return getPerson;
	}

	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}

	public String getGetProvice() {
		return getProvice;
	}

	public void setGetProvice(String getProvice) {
		this.getProvice = getProvice;
	}

	public String getGetCity() {
		return getCity;
	}

	public void setGetCity(String getCity) {
		this.getCity = getCity;
	}

	public String getGetCounty() {
		return getCounty;
	}

	public void setGetCounty(String getCounty) {
		this.getCounty = getCounty;
	}

	public String getGetStreet() {
		return getStreet;
	}

	public void setGetStreet(String getStreet) {
		this.getStreet = getStreet;
	}

	public String getGetAddress() {
		return getAddress;
	}

	public void setGetAddress(String getAddress) {
		this.getAddress = getAddress;
	}

	public String getGetTel() {
		return getTel;
	}

	public void setGetTel(String getTel) {
		this.getTel = getTel;
	}

	public String getGetPhone() {
		return getPhone;
	}

	public void setGetPhone(String getPhone) {
		this.getPhone = getPhone;
	}

	public int getWorkLarge() {
		return workLarge;
	}

	public void setWorkLarge(int workLarge) {
		this.workLarge = workLarge;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

}
