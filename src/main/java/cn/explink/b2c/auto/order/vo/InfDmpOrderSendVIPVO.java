
package cn.explink.b2c.auto.order.vo;

/**
 * 运单下发DMP--VIP相关字段(包括oxo，揽退，正常单)
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
public class InfDmpOrderSendVIPVO {

    private String boxNo;// 箱号，多个英文逗号分隔

    private Long addTime;// 出仓时间

    private String vipClub;// 团购标识

    private String orderDeliveryBatch;// 订单配送批次

    private Integer serviceType;// 服务类型 1 b2c,2 仓储,3 配送

    private Long createTime;// 创建时间

    private String orderBatchNo;// 交接单号

    private String attemperNo;// 托运单号

    private Long attemperNoCreateTime;// 托运单生成时间

    private Integer isGatherPack;// 是否站点集包

    private Integer isGatherComp;// 是否集包完成

    private String goGetReturnTime;// 预约上门揽收时间

    private String lastFetchTime;// 预约最晚揽收时间

    private String storeName;// oxo-门店名称

    private String brandName;// oxo-供应商名称

    private Integer doType;// 1-乐蜂订单 2-海淘订单 3-OXO订单 4-普通订单 5-海淘直发 6-普通直发单

    public Integer getDoType() {
        return this.doType;
    }

    public void setDoType(Integer doType) {
        this.doType = doType;
    }

    public String getOrderBatchNo() {
        return this.orderBatchNo;
    }

    public void setOrderBatchNo(String orderBatchNo) {
        this.orderBatchNo = orderBatchNo;
    }

    public String getAttemperNo() {
        return this.attemperNo;
    }

    public void setAttemperNo(String attemperNo) {
        this.attemperNo = attemperNo;
    }

    public Long getAttemperNoCreateTime() {
        return this.attemperNoCreateTime;
    }

    public void setAttemperNoCreateTime(Long attemperNoCreateTime) {
        this.attemperNoCreateTime = attemperNoCreateTime;
    }

    public String getBoxNo() {
        return this.boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public Long getAddTime() {
        return this.addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getVipClub() {
        return this.vipClub;
    }

    public void setVipClub(String vipClub) {
        this.vipClub = vipClub;
    }

    public String getOrderDeliveryBatch() {
        return this.orderDeliveryBatch;
    }

    public void setOrderDeliveryBatch(String orderDeliveryBatch) {
        this.orderDeliveryBatch = orderDeliveryBatch;
    }

    public Integer getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getIsGatherPack() {
        return this.isGatherPack;
    }

    public void setIsGatherPack(Integer isGatherPack) {
        this.isGatherPack = isGatherPack;
    }

    public Integer getIsGatherComp() {
        return this.isGatherComp;
    }

    public void setIsGatherComp(Integer isGatherComp) {
        this.isGatherComp = isGatherComp;
    }

    public String getGoGetReturnTime() {
        return this.goGetReturnTime;
    }

    public void setGoGetReturnTime(String goGetReturnTime) {
        this.goGetReturnTime = goGetReturnTime;
    }

    public String getLastFetchTime() {
        return this.lastFetchTime;
    }

    public void setLastFetchTime(String lastFetchTime) {
        this.lastFetchTime = lastFetchTime;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

}
