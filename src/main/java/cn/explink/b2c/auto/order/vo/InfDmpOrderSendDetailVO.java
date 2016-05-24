
package cn.explink.b2c.auto.order.vo;

/**
 * 运单下发DMP--明细vo
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
public class InfDmpOrderSendDetailVO {

    private String goodsCode;// 商品编码、条码

    private String goodsBrand;// 商品品牌，供应商编码

    private String goodsName;// 商品名称

    private Integer goodsNum;// 商品数量

    private String goodsPicUrl;// 商品图片url

    private String goodsSize;// 商品规格

    private Double price;// 单价（保留两位小数）

    private String remark;// 备注，退货原因

    private String custPackNo;// 快递一期，客户箱号

    public String getCustPackNo() {
        return this.custPackNo;
    }

    public void setCustPackNo(String custPackNo) {
        this.custPackNo = custPackNo;
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

    public Integer getGoodsNum() {
        return this.goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsPicUrl() {
        return this.goodsPicUrl;
    }

    public void setGoodsPicUrl(String goodsPicUrl) {
        this.goodsPicUrl = goodsPicUrl;
    }

    public String getGoodsSize() {
        return this.goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
