
package cn.explink.b2c.auto.order.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 运单下发DMP--VO
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfDmpOrderSendVO {

    private String messageId;// 唯一id，uuid

    private Integer businessType;// 业务类型；20：oxo-jit；30：唯品会正常单；40：oxo直送；60：揽退；80：快递

    private String cmdType;// 操作类型：003新增 ：023取消；090编辑

    private String transportNo;// 运单号

    private String custOrderNo;// 客户订单号

    private String custCode;// 承运商编码，落地配省公司编码

    private String customerCode;// 寄件单位编码,客户编码

    private String customerName;// 客户名称

    private String cnorProv;// 寄件省

    private String cnorCity;// 寄件城市

    private String cnorRegion;// 寄件区

    private String cnorTown;// 寄件镇

    private String cnorAddr;// 寄件人详细地址

    private String cnorContacts;// 寄件联系人

    private String cnorTel;// 寄件人联系方式

    private String cnorMobile;// 寄件人手机

    private String cneeProv;// 收货省

    private String cneeCity;// 收货城市

    private String cneeRegion;// 收货区域

    private String cneeTown;// 收货镇

    private String cneeAddr;// 收货详细地址

    private String cneeContacts;// 收件联系人

    private String cneeTel;// 收件联系方式

    private String cneeMobile;// 收件人手机

    private String postCode;// 收件地址邮编

    private String orgCode;// 机构编码 (DMP7位唯一编码)

    private String orgName;// 机构名称

    private String warehouse;// 唯品会仓库编码

    private String warehouseAddr;// 仓库地址

    private Double originalWeight; // 重量（保存两位小数）

    private Double originalVolume; // 体积（保存三位小数）

    private Integer totalPack; // 总箱数，快递目前0

    private String salesPlatform;// 销售平台

    private String transportType;// 运输类型

    private Double orderSum;// 订单金额 保留三位小数

    private boolean isCod;// 是否货到付款

    private Double codAmount;// 代收金额，保留两位小数

    private Double freight;// 运费，保留两位小数

    private String requiredTime;// 要求提货(揽退)时间

    private Double valuationValue; // 保价价值,保留两位小数

    private Integer payType;// 付款方式/结算方式

    private Integer payment;// 支付方式

    private Double returnCredit;// 应退金额

    private Integer doType;// 1-乐蜂订单 2-海淘订单 3-OXO订单 4-普通订单 5-海淘直发 6-普通直发单

    private Integer orderSource;// 运单表orderSource，揽退单需要转成外单4

    private Long createTime;// 创建时间

    private List<InfDmpOrderSendDetailVO> details;// 商品明细

    private InfDmpOrderSendExpressVO express;// 快递专用vo

    private InfDmpOrderSendVIPVO vip;// vip业务相关vo，包括oxo，正常单，揽退

    private List<InfDmpOrderSendBoxVO> boxs;// 箱信息vo

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public List<InfDmpOrderSendBoxVO> getBoxs() {
        return this.boxs;
    }

    public void setBoxs(List<InfDmpOrderSendBoxVO> boxs) {
        this.boxs = boxs;
    }

    public Integer getOrderSource() {
        return this.orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getDoType() {
        return this.doType;
    }

    public void setDoType(Integer doType) {
        this.doType = doType;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Double getReturnCredit() {
        return this.returnCredit;
    }

    public void setReturnCredit(Double returnCredit) {
        this.returnCredit = returnCredit;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getBusinessType() {
        return this.businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getCmdType() {
        return this.cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getTransportNo() {
        return this.transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustOrderNo() {
        return this.custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public String getCustCode() {
        return this.custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCnorProv() {
        return this.cnorProv;
    }

    public void setCnorProv(String cnorProv) {
        this.cnorProv = cnorProv;
    }

    public String getCnorCity() {
        return this.cnorCity;
    }

    public void setCnorCity(String cnorCity) {
        this.cnorCity = cnorCity;
    }

    public String getCnorRegion() {
        return this.cnorRegion;
    }

    public void setCnorRegion(String cnorRegion) {
        this.cnorRegion = cnorRegion;
    }

    public String getCnorTown() {
        return this.cnorTown;
    }

    public void setCnorTown(String cnorTown) {
        this.cnorTown = cnorTown;
    }

    public String getCnorAddr() {
        return this.cnorAddr;
    }

    public void setCnorAddr(String cnorAddr) {
        this.cnorAddr = cnorAddr;
    }

    public String getCnorContacts() {
        return this.cnorContacts;
    }

    public void setCnorContacts(String cnorContacts) {
        this.cnorContacts = cnorContacts;
    }

    public String getCnorTel() {
        return this.cnorTel;
    }

    public void setCnorTel(String cnorTel) {
        this.cnorTel = cnorTel;
    }

    public String getCnorMobile() {
        return this.cnorMobile;
    }

    public void setCnorMobile(String cnorMobile) {
        this.cnorMobile = cnorMobile;
    }

    public String getCneeProv() {
        return this.cneeProv;
    }

    public void setCneeProv(String cneeProv) {
        this.cneeProv = cneeProv;
    }

    public String getCneeCity() {
        return this.cneeCity;
    }

    public void setCneeCity(String cneeCity) {
        this.cneeCity = cneeCity;
    }

    public String getCneeRegion() {
        return this.cneeRegion;
    }

    public void setCneeRegion(String cneeRegion) {
        this.cneeRegion = cneeRegion;
    }

    public String getCneeTown() {
        return this.cneeTown;
    }

    public void setCneeTown(String cneeTown) {
        this.cneeTown = cneeTown;
    }

    public String getCneeAddr() {
        return this.cneeAddr;
    }

    public void setCneeAddr(String cneeAddr) {
        this.cneeAddr = cneeAddr;
    }

    public String getCneeContacts() {
        return this.cneeContacts;
    }

    public void setCneeContacts(String cneeContacts) {
        this.cneeContacts = cneeContacts;
    }

    public String getCneeTel() {
        return this.cneeTel;
    }

    public void setCneeTel(String cneeTel) {
        this.cneeTel = cneeTel;
    }

    public String getCneeMobile() {
        return this.cneeMobile;
    }

    public void setCneeMobile(String cneeMobile) {
        this.cneeMobile = cneeMobile;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouseAddr() {
        return this.warehouseAddr;
    }

    public void setWarehouseAddr(String warehouseAddr) {
        this.warehouseAddr = warehouseAddr;
    }

    public Double getOriginalWeight() {
        return this.originalWeight;
    }

    public void setOriginalWeight(Double originalWeight) {
        this.originalWeight = originalWeight;
    }

    public Double getOriginalVolume() {
        return this.originalVolume;
    }

    public void setOriginalVolume(Double originalVolume) {
        this.originalVolume = originalVolume;
    }

    public Integer getTotalPack() {
        return this.totalPack;
    }

    public void setTotalPack(Integer totalPack) {
        this.totalPack = totalPack;
    }

    public String getSalesPlatform() {
        return this.salesPlatform;
    }

    public void setSalesPlatform(String salesPlatform) {
        this.salesPlatform = salesPlatform;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Double getOrderSum() {
        return this.orderSum;
    }

    public void setOrderSum(Double orderSum) {
        this.orderSum = orderSum;
    }

    public boolean getIsCod() {
        return this.isCod;
    }

    public void setIsCod(boolean isCod) {
        this.isCod = isCod;
    }

    public Double getCodAmount() {
        return this.codAmount;
    }

    public void setCodAmount(Double codAmount) {
        this.codAmount = codAmount;
    }

    public Double getFreight() {
        return this.freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getRequiredTime() {
        return this.requiredTime;
    }

    public void setRequiredTime(String requiredTime) {
        this.requiredTime = requiredTime;
    }

    public Double getValuationValue() {
        return this.valuationValue;
    }

    public void setValuationValue(Double valuationValue) {
        this.valuationValue = valuationValue;
    }

    public Integer getPayType() {
        return this.payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayment() {
        return this.payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public List<InfDmpOrderSendDetailVO> getDetails() {
        return this.details;
    }

    public void setDetails(List<InfDmpOrderSendDetailVO> details) {
        this.details = details;
    }

    public InfDmpOrderSendExpressVO getExpress() {
        return this.express;
    }

    public void setExpress(InfDmpOrderSendExpressVO express) {
        this.express = express;
    }

    public InfDmpOrderSendVIPVO getVip() {
        return this.vip;
    }

    public void setVip(InfDmpOrderSendVIPVO vip) {
        this.vip = vip;
    }

}
