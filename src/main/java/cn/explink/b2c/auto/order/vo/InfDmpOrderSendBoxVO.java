
package cn.explink.b2c.auto.order.vo;

/**
 * 推送DMP箱明细vo
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
public class InfDmpOrderSendBoxVO {

    private String boxNo;// 箱号

    private String batchNo;// 交接单号

    private Double weight;// 重量

    private Double volume;// 体积

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return this.volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getBoxNo() {
        return this.boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

}
