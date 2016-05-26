package cn.explink.enumutil.express2;

/**
 * dmp预约单状态转换
 * from:com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum
 * 2016年5月26日 下午6:36:08
 */
public enum ReserveOrderStatusEnum {
	
	HadMakeOrder(Integer.valueOf(0), "已下单"), HadChecked(Integer.valueOf(1), "已审核"), HadAllocationPro(Integer
            .valueOf(10), "已分配省公司"), HadAllocationStation(Integer.valueOf(20), "已分配站点"), HadAllocationCourier(Integer
            .valueOf(30), "已揽件分配"), HadReceiveSuccess(Integer.valueOf(40), "揽件成功"), HaveProOutZone(
            Integer.valueOf(50), "省公司超区"), HaveStationOutZone(Integer.valueOf(60), "站点超区"), HaveReciveOutZone(Integer
            .valueOf(70), "揽件超区"), HaveReciveFailure(Integer.valueOf(80), "揽件失败"), HaveFeedbackRetention(Integer
            .valueOf(90), "反馈滞留"), HadClosed(Integer.valueOf(100), "已关闭");
	
	private Integer index;

    private String name;

    ReserveOrderStatusEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

	public Integer getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}
}
