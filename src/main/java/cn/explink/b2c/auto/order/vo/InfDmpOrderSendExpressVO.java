
package cn.explink.b2c.auto.order.vo;

/**
 * 运单下发DMP---快递专用vo
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
public class InfDmpOrderSendExpressVO {

    private String accountId;// 月结账号

    private String expressImage;// 快递单快照url

    private Double length;// 长 1位小数

    private Double width;// 宽 1位小数

    private Double height;// 高 1位小数

    private Double valuationFee;// 保费 两位小数

    private Double packingFee;// 包装费 两位小数

    private Double actualFee;// 实收运费 两位小数

    private Double calculateWeight;// 计费重量，两位小数

    private String cneeCertificate;// 收件人身份证号码

    private String cneeCorpName;// 收货公司名称

    private String cneeCorpNo;// 收货公司编码

    private Integer cneePeriod;// 收货时间类型

    private String acceptOperator;// 揽件人员

    private Integer productType;// 服务产品类型 1-标准 2-次日达 3-当日达

    private boolean isAcceptProv;// 是否揽件省份

    public Integer getProductType() {
        return this.productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public boolean getIsAcceptProv() {
        return this.isAcceptProv;
    }

    public void setIsAcceptProv(boolean isAcceptProv) {
        this.isAcceptProv = isAcceptProv;
    }

    public String getCneeCorpNo() {
        return this.cneeCorpNo;
    }

    public void setCneeCorpNo(String cneeCorpNo) {
        this.cneeCorpNo = cneeCorpNo;
    }

    public Integer getCneePeriod() {
        return this.cneePeriod;
    }

    public void setCneePeriod(Integer cneePeriod) {
        this.cneePeriod = cneePeriod;
    }

    public String getAcceptOperator() {
        return this.acceptOperator;
    }

    public void setAcceptOperator(String acceptOperator) {
        this.acceptOperator = acceptOperator;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getExpressImage() {
        return this.expressImage;
    }

    public void setExpressImage(String expressImage) {
        this.expressImage = expressImage;
    }

    public Double getLength() {
        return this.length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return this.width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return this.height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getValuationFee() {
        return this.valuationFee;
    }

    public void setValuationFee(Double valuationFee) {
        this.valuationFee = valuationFee;
    }

    public Double getPackingFee() {
        return this.packingFee;
    }

    public void setPackingFee(Double packingFee) {
        this.packingFee = packingFee;
    }

    public Double getActualFee() {
        return this.actualFee;
    }

    public void setActualFee(Double actualFee) {
        this.actualFee = actualFee;
    }

    public Double getCalculateWeight() {
        return this.calculateWeight;
    }

    public void setCalculateWeight(Double calculateWeight) {
        this.calculateWeight = calculateWeight;
    }

    public String getCneeCertificate() {
        return this.cneeCertificate;
    }

    public void setCneeCertificate(String cneeCertificate) {
        this.cneeCertificate = cneeCertificate;
    }

    public String getCneeCorpName() {
        return this.cneeCorpName;
    }

    public void setCneeCorpName(String cneeCorpName) {
        this.cneeCorpName = cneeCorpName;
    }

}
