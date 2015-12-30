
package cn.explink.b2c.auto.order.vo;

import java.math.BigDecimal;

public class TPSOrderDetails {

    /**
     *
     */
    private static final long serialVersionUID = 9199623499399893277L;

    private Integer infCarrierPickTaskSendDetailsId;

    private Integer infCarrierPickTaskSendId;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 品牌名称
     */
    private String goodsBrand;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSize;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 退货原因
     */
    private String remark;

    /**
     * 商品图片URL
     */
    private String goodsPicUrl;

    private BigDecimal price;

    public Integer getInfCarrierPickTaskSendDetailsId() {
        return this.infCarrierPickTaskSendDetailsId;
    }

    public void setInfCarrierPickTaskSendDetailsId(Integer infCarrierPickTaskSendDetailsId) {
        this.infCarrierPickTaskSendDetailsId = infCarrierPickTaskSendDetailsId;
    }

    public Integer getInfCarrierPickTaskSendId() {
        return this.infCarrierPickTaskSendId;
    }

    public void setInfCarrierPickTaskSendId(Integer infCarrierPickTaskSendId) {
        this.infCarrierPickTaskSendId = infCarrierPickTaskSendId;
    }

    public String getGoodsCode() {
        return this.goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsBrand() {
        return this.goodsBrand;
    }

    public void setGoodsBrand(String goodsBrand) {
        this.goodsBrand = goodsBrand;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSize() {
        return this.goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public Integer getGoodsNum() {
        return this.goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGoodsPicUrl() {
        return this.goodsPicUrl;
    }

    public void setGoodsPicUrl(String goodsPicUrl) {
        this.goodsPicUrl = goodsPicUrl;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
