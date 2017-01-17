package cn.explink.b2c.pjwl;

import java.math.BigDecimal;

/**
 * 运单 dto 用于接收tps运单单转换后的容器与临时表对应
 * @author jiangyu 2015年9月9日
 *
 */
public class ExpressCwbOrderDTO {
	
	private String id;
	/**
	 * 运单号,快递单运单号
	 */
	private String transportNo;
	/**
	 * 客户订单号
	 */
	private String custOrderNo;
	/**
	 * 发件客户编码 
	 */
	private String custCode;
	/**
	 * 揽件站点
	 */
	private String acceptDept;
	/**
	 * 揽件员
	 */
	private String acceptOperator;
	/**
	 * 发货省份名称
	 */
	private String cnorProv;
	/**
	 * 发货城市名称
	 */
	private String cnorCity;
	/**
	 * 发货区/县名称
	 */
	private String cnorRegion;
	/**
	 * 发货乡镇名称
	 */
	private String cnorTown;
	/**
	 * 发货详细地址
	 */
	private String cnorAddr;
	/**
	 * 发货联系人
	 */
	private String cnorName;
	/**
	 * 发货人联系手机
	 */
	private String cnorMobile;
	/**
	 * 发货人电话
	 */
	private String cnorTel;
	/**
	 * 发货备注
	 */
	private String cnorRemark;
	/**
	 * 收货省份名称
	 */
	private String cneeProv;
	/**
	 * 收货城市名称
	 */
	private String cneeCity;
	/**
	 * 收货区域名称
	 */
	private String cneeRegion;
	/**
	 * 收货街道名称
	 */
	private String cneeTown;
	/**
	 * 收货地址
	 */
	private String cneeAddr;
	/**
	 * 收货联系人
	 */
	private String cneeName;
	/**
	 * 收货联系人手机
	 */
	private String cneeMobile;
	/**
	 * 收货联系人电话
	 */
	private String cneeTel;
	/**
	 * 0:送货时间不限
	 *	1:只工作日(双休日/节假日不用送)
	 *	2:只双休日/节假日送货(工作日不用送)
	 */
	private Integer cneePeriod;
	/**
	 * 收货人备注
	 */
	private String cneeRemark;
	/**
	 * 寄件人证件号
	 */
	private String cneeCertificate;
	/**
	 * 收货公司编码
	 */
	private String cneeNo;
	/**
	 * 是否货到付款,默认为0 ，     0:否、1:是
	 */
	private Integer isCod;
	/**
	 * 代收货款,当is_cod为1时，必填，大于0
	 */
	private BigDecimal codAmount; 
	/**
	 * 运费合计
	 */
	private BigDecimal carriage;
	/**
	 * 合计件数
	 */
	private Integer totalNum;
	/**
	 * 实际重量
	 */
	private BigDecimal totalWeight;
	/**
	 * 计费重量
	 */
	private BigDecimal calculateWeight;
	/**
	 * 合计体积
	 */
	private BigDecimal totalVolume;
	/**
	 * 合计箱数
	 */
	private Integer totalBox;
	/**
	 * 保价金额
	 */
	private BigDecimal assuranceValue;
	/**
	 * 保费
	 */
	private BigDecimal assuranceFee;
	/**
	 * 
	 * 0:月结
	 * 1:现付
	 * 2:到付
	 */
	private Integer payType;
	/**
	 * 默认-1
	 * 0:现金 
	 * 1:pos 
	 * 2.支付宝 
	 */
	private Integer payment;
	/**
	 * 运单明细对象（运单商品信息）
	 */
	private String details;
	/**
	 * 货名
	 */
	private String cargoName;
	/**
	 * 件数
	 */
	private Integer count;
	/**
	 * 长
	 */
	private BigDecimal cargoLength;
	/**
	 * 宽
	 */
	private BigDecimal cargoWidth;
	/**
	 * 高
	 */
	private BigDecimal cargoHeight;
	/**
	 * 重量
	 */
	private BigDecimal weight;
	/**
	 * 体积
	 */
	private BigDecimal volume;
	/**
	 * 客户箱号
	 */
	private String custPackNo;
	/**
	 * 商品条码
	 */
	private String sizeSn;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * // 省份类型 0 派件省，1揽件省
	 */
	private int isAcceptProv;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransportNo() {
		return transportNo;
	}
	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	public String getCustOrderNo() {
		return custOrderNo;
	}
	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getAcceptDept() {
		return acceptDept;
	}
	public void setAcceptDept(String acceptDept) {
		this.acceptDept = acceptDept;
	}
	public String getAcceptOperator() {
		return acceptOperator;
	}
	public void setAcceptOperator(String acceptOperator) {
		this.acceptOperator = acceptOperator;
	}
	public String getCnorProv() {
		return cnorProv;
	}
	public void setCnorProv(String cnorProv) {
		this.cnorProv = cnorProv;
	}
	public String getCnorCity() {
		return cnorCity;
	}
	public void setCnorCity(String cnorCity) {
		this.cnorCity = cnorCity;
	}
	public String getCnorRegion() {
		return cnorRegion;
	}
	public void setCnorRegion(String cnorRegion) {
		this.cnorRegion = cnorRegion;
	}
	public String getCnorTown() {
		return cnorTown;
	}
	public void setCnorTown(String cnorTown) {
		this.cnorTown = cnorTown;
	}
	public String getCnorAddr() {
		return cnorAddr;
	}
	public void setCnorAddr(String cnorAddr) {
		this.cnorAddr = cnorAddr;
	}
	public String getCnorName() {
		return cnorName;
	}
	public void setCnorName(String cnorName) {
		this.cnorName = cnorName;
	}
	public String getCnorMobile() {
		return cnorMobile;
	}
	public void setCnorMobile(String cnorMobile) {
		this.cnorMobile = cnorMobile;
	}
	public String getCnorTel() {
		return cnorTel;
	}
	public void setCnorTel(String cnorTel) {
		this.cnorTel = cnorTel;
	}
	public String getCnorRemark() {
		return cnorRemark;
	}
	public void setCnorRemark(String cnorRemark) {
		this.cnorRemark = cnorRemark;
	}
	public String getCneeProv() {
		return cneeProv;
	}
	public void setCneeProv(String cneeProv) {
		this.cneeProv = cneeProv;
	}
	public String getCneeCity() {
		return cneeCity;
	}
	public void setCneeCity(String cneeCity) {
		this.cneeCity = cneeCity;
	}
	public String getCneeRegion() {
		return cneeRegion;
	}
	public void setCneeRegion(String cneeRegion) {
		this.cneeRegion = cneeRegion;
	}
	public String getCneeTown() {
		return cneeTown;
	}
	public void setCneeTown(String cneeTown) {
		this.cneeTown = cneeTown;
	}
	public String getCneeAddr() {
		return cneeAddr;
	}
	public void setCneeAddr(String cneeAddr) {
		this.cneeAddr = cneeAddr;
	}
	public String getCneeName() {
		return cneeName;
	}
	public void setCneeName(String cneeName) {
		this.cneeName = cneeName;
	}
	public String getCneeMobile() {
		return cneeMobile;
	}
	public void setCneeMobile(String cneeMobile) {
		this.cneeMobile = cneeMobile;
	}
	public String getCneeTel() {
		return cneeTel;
	}
	public void setCneeTel(String cneeTel) {
		this.cneeTel = cneeTel;
	}
	public Integer getCneePeriod() {
		return cneePeriod;
	}
	public void setCneePeriod(Integer cneePeriod) {
		this.cneePeriod = cneePeriod;
	}
	public String getCneeRemark() {
		return cneeRemark;
	}
	public void setCneeRemark(String cneeRemark) {
		this.cneeRemark = cneeRemark;
	}
	public String getCneeCertificate() {
		return cneeCertificate;
	}
	public void setCneeCertificate(String cneeCertificate) {
		this.cneeCertificate = cneeCertificate;
	}
	public String getCneeNo() {
		return cneeNo;
	}
	public void setCneeNo(String cneeNo) {
		this.cneeNo = cneeNo;
	}
	public Integer getIsCod() {
		return isCod;
	}
	public void setIsCod(Integer isCod) {
		this.isCod = isCod;
	}
	public BigDecimal getCodAmount() {
		return codAmount;
	}
	public void setCodAmount(BigDecimal codAmount) {
		this.codAmount = codAmount;
	}
	public BigDecimal getCarriage() {
		return carriage;
	}
	public void setCarriage(BigDecimal carriage) {
		this.carriage = carriage;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getCalculateWeight() {
		return calculateWeight;
	}
	public void setCalculateWeight(BigDecimal calculateWeight) {
		this.calculateWeight = calculateWeight;
	}
	public BigDecimal getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	public Integer getTotalBox() {
		return totalBox;
	}
	public void setTotalBox(Integer totalBox) {
		this.totalBox = totalBox;
	}
	public BigDecimal getAssuranceValue() {
		return assuranceValue;
	}
	public void setAssuranceValue(BigDecimal assuranceValue) {
		this.assuranceValue = assuranceValue;
	}
	public BigDecimal getAssuranceFee() {
		return assuranceFee;
	}
	public void setAssuranceFee(BigDecimal assuranceFee) {
		this.assuranceFee = assuranceFee;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getPayment() {
		return payment;
	}
	public void setPayment(Integer payment) {
		this.payment = payment;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public BigDecimal getCargoLength() {
		return cargoLength;
	}
	public void setCargoLength(BigDecimal cargoLength) {
		this.cargoLength = cargoLength;
	}
	public BigDecimal getCargoWidth() {
		return cargoWidth;
	}
	public void setCargoWidth(BigDecimal cargoWidth) {
		this.cargoWidth = cargoWidth;
	}
	public BigDecimal getCargoHeight() {
		return cargoHeight;
	}
	public void setCargoHeight(BigDecimal cargoHeight) {
		this.cargoHeight = cargoHeight;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public String getCustPackNo() {
		return custPackNo;
	}
	public void setCustPackNo(String custPackNo) {
		this.custPackNo = custPackNo;
	}
	public String getSizeSn() {
		return sizeSn;
	}
	public void setSizeSn(String sizeSn) {
		this.sizeSn = sizeSn;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getIsAcceptProv() {
		return isAcceptProv;
	}
	public void setIsAcceptProv(int isAcceptProv) {
		this.isAcceptProv = isAcceptProv;
	}
	
}

//PjDeliverOrder4DMPRequest