package cn.explink.enumutil.express2;

import java.util.HashMap;
import java.util.Map;

/**
 * dmp预约单状态转换
 * from:com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum
 * 2016年5月26日 下午6:36:08
 */
public enum ReserveOrderDmpStatusEnum {
	
	HadMakeOrder(Integer.valueOf(0), "已下单"), HadChecked(Integer.valueOf(1), "已审核"), HadAllocationPro(Integer
            .valueOf(10), "已分配省公司"), HadAllocationStation(Integer.valueOf(20), "已分配站点"), HadAllocationCourier(Integer
            .valueOf(30), "已揽件分配"), HadReceiveSuccess(Integer.valueOf(40), "揽件成功"), HaveProOutZone(
            Integer.valueOf(50), "省公司超区"), HaveStationOutZone(Integer.valueOf(60), "站点超区"), HaveReciveOutZone(Integer
            .valueOf(70), "揽件超区"), HaveReciveFailure(Integer.valueOf(80), "揽件失败"), HaveFeedbackRetention(Integer
            .valueOf(90), "反馈滞留"), HadClosed(Integer.valueOf(100), "已关闭");

    private static Map<Integer, ReserveOrderDmpStatusEnum> RESERVE_ORDER_STATUS_MAP = null;

    static {
        ReserveOrderDmpStatusEnum.RESERVE_ORDER_STATUS_MAP = new HashMap<Integer, ReserveOrderDmpStatusEnum>();
        for (ReserveOrderDmpStatusEnum reserveOrderStatusEnum : ReserveOrderDmpStatusEnum.values()) {
            ReserveOrderDmpStatusEnum.RESERVE_ORDER_STATUS_MAP.put(reserveOrderStatusEnum.getIndex(),
                    reserveOrderStatusEnum);
        }
    }

    private Integer index;

    private String name;

    ReserveOrderDmpStatusEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public static ReserveOrderDmpStatusEnum getReserveOrderStatusEnum(Integer index) {
        return RESERVE_ORDER_STATUS_MAP.get(index);
    }
    
    public static String getNameByIndex(Integer index) {
    	ReserveOrderDmpStatusEnum e = RESERVE_ORDER_STATUS_MAP.get(index);
    	return e == null ? null : e.getName();
    	
    }

    public static Integer getIndexByName(String name) {
        Integer index = null;
        ReserveOrderDmpStatusEnum[] values = ReserveOrderDmpStatusEnum.values();
        for (ReserveOrderDmpStatusEnum value : values) {
            if (value.getName().equals(name)) {
                index = value.getIndex();
            }
        }
        return index;
    }
}
