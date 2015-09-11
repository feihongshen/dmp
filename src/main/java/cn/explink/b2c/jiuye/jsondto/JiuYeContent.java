package cn.explink.b2c.jiuye.jsondto;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiuYeContent {
	
	@JsonProperty(value = "ClientCode") 
	private String clientCode;   //remark5
	@JsonProperty(value = "WorkCode") 
	private String workCode;    //cwb
	@JsonProperty(value = "SubCodeList") 
	private List<SubCode> subCodeList;   //一票多件
	@JsonProperty(value = "HYWorkCode") 
	private String hyWorkCode;     //原运单号 
	@JsonProperty(value = "RWorkCode") 
	private String rWorkCode;    //退货号 
	@JsonProperty(value = "WorkType") 
	private int workType; //运单类型
	@JsonProperty(value = "OrderCount") 
	private String orderCount;      //运单数量
	@JsonProperty(value = "ReplCost")
	private BigDecimal replCost; //应收金额
	@JsonProperty(value = "IsRecInvoices")
	private int isRecInvoices; //订单重量
	@JsonProperty(value = "GetPerson")
	private String getPerson; //收货人名称
	@JsonProperty(value = "GetZip")
	private String getZip; //收货人邮编
	@JsonProperty(value = "GetProvince")
	private String getProvince;//收货人省份
	@JsonProperty(value = "GetCity")
	private String getCity; //收货人城市
	@JsonProperty(value = "GetCounty")
	private String getCounty; //收货人（地区/县）
	@JsonProperty(value = "GetAddress")
	private String getAddress ; //收货人详细地址
	@JsonProperty(value = "GetPhone")
	private String getPhone; //收货人手机 
	@JsonProperty(value = "GetTel")
	private String getTel; //收货人座机
	@JsonProperty(value = "PackageHav")
	private BigDecimal packageHav;//包裹重量（kg）
	@JsonProperty(value = "PackageSize")
	private BigDecimal packageSize;//包裹体积（立方厘米）
	@JsonProperty(value = "WMSCode")
	private String wmsCode;//仓库编码（唯一标示仓库）
	@JsonProperty(value = "TrustPerson")
	private String trustPerson;//寄件人名称
	@JsonProperty(value = "TrustZip")
	private String trustZip; //寄件人邮编
	@JsonProperty(value = "TrustProvince")
	private String trustProvince;//寄件人省份
	@JsonProperty(value = "TrustCity")
	private String trustCity;//寄件人城市
	@JsonProperty(value = "TrustCounty")
	private String trustCounty;//寄件人(地区/县)
	@JsonProperty(value = "TrustAddress")
	private String trustAddress;//寄件人详细地址
	@JsonProperty(value = "TrustPhone")
	private String trustPhone;//寄件人手机
	@JsonProperty(value = "TrustTel")
	private String trustTel;//寄件人座机
	@JsonProperty(value = "IsIimitSchedule")
	private int isIimitSchedule;//是否限时配送
	@JsonProperty(value = "ScheduleType")
	private int scheduleType;//投递时延要求
	@JsonProperty(value = "ScheduleStart")
	private String scheduleStart;//送达开始时间
	@JsonProperty(value = "ScheduleEnd")
	private String ScheduleEnd;//送达结束时间
	@JsonProperty(value = "Property1")
	private String property1;//自定义字段1
	@JsonProperty(value = "Property2")
	private String property2;//自定义字段2
	@JsonProperty(value = "Property3")
	private String property3;//自定义字段3
	@JsonProperty(value = "Property4")
	private String property4;//自定义字段4
	@JsonProperty(value = "Property5")
	private String property5;//自定义字段5
	
	public String getProperty1() {
		return property1;
	}
	public void setProperty1(String property1) {
		this.property1 = property1;
	}
	public String getProperty2() {
		return property2;
	}
	public void setProperty2(String property2) {
		this.property2 = property2;
	}
	public String getProperty3() {
		return property3;
	}
	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	public String getProperty4() {
		return property4;
	}
	public void setProperty4(String property4) {
		this.property4 = property4;
	}
	public String getProperty5() {
		return property5;
	}
	public void setProperty5(String property5) {
		this.property5 = property5;
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
	public String getHyWorkCode() {
		return hyWorkCode;
	}
	public void setHyWorkCode(String hyWorkCode) {
		this.hyWorkCode = hyWorkCode;
	}
	public String getrWorkCode() {
		return rWorkCode;
	}
	public void setrWorkCode(String rWorkCode) {
		this.rWorkCode = rWorkCode;
	}
	public int getWorkType() {
		return workType;
	}
	public void setWorkType(int workType) {
		this.workType = workType;
	}
	
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public BigDecimal getReplCost() {
		return replCost;
	}
	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
	}
	
	public int getIsRecInvoices() {
		return isRecInvoices;
	}
	public void setIsRecInvoices(int isRecInvoices) {
		this.isRecInvoices = isRecInvoices;
	}
	public String getGetPerson() {
		return getPerson;
	}
	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}
	public String getGetZip() {
		return getZip;
	}
	public void setGetZip(String getZip) {
		this.getZip = getZip;
	}
	public String getGetProvince() {
		return getProvince;
	}
	public void setGetProvince(String getProvince) {
		this.getProvince = getProvince;
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
	
	public String getGetAddress() {
		return getAddress;
	}
	public void setGetAddress(String getAddress) {
		this.getAddress = getAddress;
	}
	public String getGetPhone() {
		return getPhone;
	}
	public void setGetPhone(String getPhone) {
		this.getPhone = getPhone;
	}
	public String getGetTel() {
		return getTel;
	}
	public void setGetTel(String getTel) {
		this.getTel = getTel;
	}
	public BigDecimal getPackageHav() {
		return packageHav;
	}
	public void setPackageHav(BigDecimal packageHav) {
		this.packageHav = packageHav;
	}
	public BigDecimal getPackageSize() {
		return packageSize;
	}
	public void setPackageSize(BigDecimal packageSize) {
		this.packageSize = packageSize;
	}
	public String getWmsCode() {
		return wmsCode;
	}
	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}
	public String getTrustPerson() {
		return trustPerson;
	}
	public void setTrustPerson(String trustPerson) {
		this.trustPerson = trustPerson;
	}
	public String getTrustZip() {
		return trustZip;
	}
	public void setTrustZip(String trustZip) {
		this.trustZip = trustZip;
	}
	public String getTrustProvince() {
		return trustProvince;
	}
	public void setTrustProvince(String trustProvince) {
		this.trustProvince = trustProvince;
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
	public String getTrustAddress() {
		return trustAddress;
	}
	public void setTrustAddress(String trustAddress) {
		this.trustAddress = trustAddress;
	}
	public String getTrustPhone() {
		return trustPhone;
	}
	public void setTrustPhone(String trustPhone) {
		this.trustPhone = trustPhone;
	}
	public String getTrustTel() {
		return trustTel;
	}
	public void setTrustTel(String trustTel) {
		this.trustTel = trustTel;
	}
	
	public int getIsIimitSchedule() {
		return isIimitSchedule;
	}
	public void setIsIimitSchedule(int isIimitSchedule) {
		this.isIimitSchedule = isIimitSchedule;
	}
	public int getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}
	public String getScheduleStart() {
		return scheduleStart;
	}
	public void setScheduleStart(String scheduleStart) {
		this.scheduleStart = scheduleStart;
	}
	public String getScheduleEnd() {
		return ScheduleEnd;
	}
	public void setScheduleEnd(String scheduleEnd) {
		ScheduleEnd = scheduleEnd;
	}
	public List<SubCode> getSubCodeList() {
		return subCodeList;
	}
	public void setSubCodeList(List<SubCode> subCodeList) {
		this.subCodeList = subCodeList;
	}
}
