package cn.explink.domain.deliveryFee;

import java.math.BigDecimal;
import java.util.Date;

public class DfAdjustmentRecord {
    private long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 运单号
     */
    private String transcwb;
    /**
     * 订单类型
     */
    private Integer cwbordertypeid;
    /**
     * 发货客户
     */
    private long customerid;
    /**
     * 发货件数
     */
    private int sendcarnum;

    /**
     * 发货地址
     */
    private String senderaddress;

    /**
     * 收件人地址
     */
    private String consigneeaddress;

    /**
     * 实际重量
     */
    private BigDecimal realweight;
    /**
     * 实际体积
     */
    private BigDecimal cargovolume;
    /**
     * 费用类型:0-揽件费; 1-派费
     */
    private int chargeType;
    /**
     * 取货数量
     */
    private long backcarnum;
    /**
     * 揽件费(派费)
     */
    private BigDecimal adjustAmount;
    /**
     * 均价
     */
    private BigDecimal averPrice;
    /**
     * 阶梯均价
     */
    private BigDecimal levelAverprice;
    /**
     * 范围均价
     */
    private BigDecimal rangeAverprice;
    /**
     * 范围续价
     */
    private BigDecimal rangeAddprice;
    /**
     * 续价
     */
    private BigDecimal addprice;
    /**
     * 超区补贴
     */
    private BigDecimal overareaSub;
    /**
     * 一票多件补贴
     */
    private BigDecimal multiorderSub;
    /**
     * 大件补贴
     */
    private BigDecimal hugeorderSub;
    /**
     * 代收货款补贴
     */
    private BigDecimal codSub;
    /**
     * 其他补贴
     */
    private BigDecimal othersSub;
    /**
     * 小件员名称
     */
    private long deliverId;
    /**
     * 小件员登陆名
     */
    private String deliverUsername;
    /**
     * 操作站点
     */
    private int deliverybranchid;
    /**
     * 订单状态
     */
    private int cwbstate;
    /**
     * 订单操作状态
     */
    private int flowordertype;
    /**
     * 导入数据时间
     */
    private Date createTime;
    /**
     * 揽件出站时间
     */
    private Date outstationDate;
    /**
     * 反馈结果
     */
    private int deliverystate;
    /**
     * 发货时间
     */
    private Date emaildate;
    /**
     * 站点到货时间
     */
    private Date credate;
    /**
     * 归班反馈时间
     */
    private Date mobilepodtime;
    /**
     * 归班审核时间
     */
    private Date auditingtime;
    /**
     * 结算状态
     */
    private int isBilled;
    /**
     * 计费协议编号(多个协议用逗号隔开)
     */
    private String agtIds;
    /**
     * 规则编号(多个规则用逗号隔开展示)
     */
    private String ruleIds;
    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 账单对象类型(0加盟站，1小件员)
     */
    private int chargerType;

    /**
     * 应退款
     */
    private BigDecimal paybackfee;
    /**
     * 代收款
     */
    private BigDecimal receivablefee;

    private Date adjustmentCreateTime;
    private String adjustmentCreateUser;
    private Date adjustmentUpdateTime;
    private String adjustmentUpdateUser;

    /**
     * 领货时间
     */
    private Date pickTime;
    /**
     * 重置反馈申请人id
     */
    private long applyuserid;
    /**
     * 重置反馈通过时间
     */
    private String edittime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTranscwb() {
        return transcwb;
    }

    public void setTranscwb(String transcwb) {
        this.transcwb = transcwb;
    }

    public Integer getCwbordertypeid() {
        return cwbordertypeid;
    }

    public void setCwbordertypeid(Integer cwbordertypeid) {
        this.cwbordertypeid = cwbordertypeid;
    }

    public long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(long customerid) {
        this.customerid = customerid;
    }

    public int getSendcarnum() {
        return sendcarnum;
    }

    public void setSendcarnum(int sendcarnum) {
        this.sendcarnum = sendcarnum;
    }

    public String getSenderaddress() {
        return senderaddress;
    }

    public void setSenderaddress(String senderaddress) {
        this.senderaddress = senderaddress;
    }

    public String getConsigneeaddress() {
        return consigneeaddress;
    }

    public void setConsigneeaddress(String consigneeaddress) {
        this.consigneeaddress = consigneeaddress;
    }

    public BigDecimal getRealweight() {
        return realweight;
    }

    public void setRealweight(BigDecimal realweight) {
        this.realweight = realweight;
    }

    public BigDecimal getCargovolume() {
        return cargovolume;
    }

    public void setCargovolume(BigDecimal cargovolume) {
        this.cargovolume = cargovolume;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public long getBackcarnum() {
        return backcarnum;
    }

    public void setBackcarnum(long backcarnum) {
        this.backcarnum = backcarnum;
    }


    public BigDecimal getAverPrice() {
        return averPrice;
    }

    public void setAverPrice(BigDecimal averPrice) {
        this.averPrice = averPrice;
    }

    public BigDecimal getLevelAverprice() {
        return levelAverprice;
    }

    public void setLevelAverprice(BigDecimal levelAverprice) {
        this.levelAverprice = levelAverprice;
    }

    public BigDecimal getRangeAverprice() {
        return rangeAverprice;
    }

    public void setRangeAverprice(BigDecimal rangeAverprice) {
        this.rangeAverprice = rangeAverprice;
    }

    public BigDecimal getRangeAddprice() {
        return rangeAddprice;
    }

    public void setRangeAddprice(BigDecimal rangeAddprice) {
        this.rangeAddprice = rangeAddprice;
    }

    public BigDecimal getAddprice() {
        return addprice;
    }

    public void setAddprice(BigDecimal addprice) {
        this.addprice = addprice;
    }

    public BigDecimal getOverareaSub() {
        return overareaSub;
    }

    public void setOverareaSub(BigDecimal overareaSub) {
        this.overareaSub = overareaSub;
    }

    public BigDecimal getMultiorderSub() {
        return multiorderSub;
    }

    public void setMultiorderSub(BigDecimal multiorderSub) {
        this.multiorderSub = multiorderSub;
    }

    public BigDecimal getHugeorderSub() {
        return hugeorderSub;
    }

    public void setHugeorderSub(BigDecimal hugeorderSub) {
        this.hugeorderSub = hugeorderSub;
    }

    public BigDecimal getCodSub() {
        return codSub;
    }

    public void setCodSub(BigDecimal codSub) {
        this.codSub = codSub;
    }

    public BigDecimal getOthersSub() {
        return othersSub;
    }

    public void setOthersSub(BigDecimal othersSub) {
        this.othersSub = othersSub;
    }

    public long getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(long deliverId) {
        this.deliverId = deliverId;
    }

    public String getDeliverUsername() {
        return deliverUsername;
    }

    public void setDeliverUsername(String deliverUsername) {
        this.deliverUsername = deliverUsername;
    }

    public int getDeliverybranchid() {
        return deliverybranchid;
    }

    public void setDeliverybranchid(int deliverybranchid) {
        this.deliverybranchid = deliverybranchid;
    }

    public int getCwbstate() {
        return cwbstate;
    }

    public void setCwbstate(int cwbstate) {
        this.cwbstate = cwbstate;
    }

    public int getFlowordertype() {
        return flowordertype;
    }

    public void setFlowordertype(int flowordertype) {
        this.flowordertype = flowordertype;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOutstationDate() {
        return outstationDate;
    }

    public void setOutstationDate(Date outstationDate) {
        this.outstationDate = outstationDate;
    }

    public int getDeliverystate() {
        return deliverystate;
    }

    public void setDeliverystate(int deliverystate) {
        this.deliverystate = deliverystate;
    }

    public Date getEmaildate() {
        return emaildate;
    }

    public void setEmaildate(Date emaildate) {
        this.emaildate = emaildate;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }

    public Date getMobilepodtime() {
        return mobilepodtime;
    }

    public void setMobilepodtime(Date mobilepodtime) {
        this.mobilepodtime = mobilepodtime;
    }

    public Date getAuditingtime() {
        return auditingtime;
    }

    public void setAuditingtime(Date auditingtime) {
        this.auditingtime = auditingtime;
    }

    public int getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(int isBilled) {
        this.isBilled = isBilled;
    }

    public String getAgtIds() {
        return agtIds;
    }

    public void setAgtIds(String agtIds) {
        this.agtIds = agtIds;
    }

    public String getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getChargerType() {
        return chargerType;
    }

    public void setChargerType(int chargerType) {
        this.chargerType = chargerType;
    }

    public BigDecimal getPaybackfee() {
        return paybackfee;
    }

    public void setPaybackfee(BigDecimal paybackfee) {
        this.paybackfee = paybackfee;
    }

    public BigDecimal getReceivablefee() {
        return receivablefee;
    }

    public void setReceivablefee(BigDecimal receivablefee) {
        this.receivablefee = receivablefee;
    }

    public Date getAdjustmentCreateTime() {
        return adjustmentCreateTime;
    }

    public void setAdjustmentCreateTime(Date adjustmentCreateTime) {
        this.adjustmentCreateTime = adjustmentCreateTime;
    }

    public String getAdjustmentCreateUser() {
        return adjustmentCreateUser;
    }

    public void setAdjustmentCreateUser(String adjustmentCreateUser) {
        this.adjustmentCreateUser = adjustmentCreateUser;
    }

    public Date getAdjustmentUpdateTime() {
        return adjustmentUpdateTime;
    }

    public void setAdjustmentUpdateTime(Date adjustmentUpdateTime) {
        this.adjustmentUpdateTime = adjustmentUpdateTime;
    }

    public String getAdjustmentUpdateUser() {
        return adjustmentUpdateUser;
    }

    public void setAdjustmentUpdateUser(String adjustmentUpdateUser) {
        this.adjustmentUpdateUser = adjustmentUpdateUser;
    }

    public BigDecimal getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(BigDecimal adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public long getApplyuserid() {
        return applyuserid;
    }

    public void setApplyuserid(long applyuserid) {
        this.applyuserid = applyuserid;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }
}
