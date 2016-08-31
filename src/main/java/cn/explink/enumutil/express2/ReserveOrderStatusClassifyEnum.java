package cn.explink.enumutil.express2;

/**
 * 预约单状态分类
 * @date 2016年5月19日 下午3:52:10
 */
public enum ReserveOrderStatusClassifyEnum {
	
	QUERY_BY_CUSTOM_SERVICE(new ReserveOrderDmpStatusEnum[] {
			ReserveOrderDmpStatusEnum.HaveProOutZone,
			ReserveOrderDmpStatusEnum.HadAllocationPro,
			ReserveOrderDmpStatusEnum.HadAllocationStation,
			ReserveOrderDmpStatusEnum.HadAllocationCourier,
			ReserveOrderDmpStatusEnum.HadReceiveSuccess,
			ReserveOrderDmpStatusEnum.HaveStationOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveFailure,
			ReserveOrderDmpStatusEnum.HaveFeedbackRetention,
			ReserveOrderDmpStatusEnum.HadClosed
		}),
	
	QUERY_BY_WAREHOUSE_MASTER(new ReserveOrderDmpStatusEnum[] {
			ReserveOrderDmpStatusEnum.HadAllocationStation,
			ReserveOrderDmpStatusEnum.HadAllocationCourier,
			ReserveOrderDmpStatusEnum.HadReceiveSuccess,
			ReserveOrderDmpStatusEnum.HaveStationOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveFailure,
			ReserveOrderDmpStatusEnum.HaveFeedbackRetention,
			ReserveOrderDmpStatusEnum.HadClosed
		}),
	
	HANDLE_BY_CUSTOM_SERVICE(new ReserveOrderDmpStatusEnum[] {
//			ReserveOrderStatusEnum.HaveProOutZone,
            ReserveOrderDmpStatusEnum.HadAllocationPro,
            ReserveOrderDmpStatusEnum.HadAllocationStation,
            ReserveOrderDmpStatusEnum.HadAllocationCourier,
//			ReserveOrderStatusEnum.HadReceiveSuccess,
            ReserveOrderDmpStatusEnum.HaveStationOutZone,
            ReserveOrderDmpStatusEnum.HaveReciveOutZone,
//			ReserveOrderStatusEnum.HaveReciveFailure,
            ReserveOrderDmpStatusEnum.HaveFeedbackRetention,
//			ReserveOrderStatusEnum.HadClosed
    }),
	
	HANDLE_BY_WAREHOUSE_MASTER(new ReserveOrderDmpStatusEnum[] {
			ReserveOrderDmpStatusEnum.HadAllocationStation,
			ReserveOrderDmpStatusEnum.HadAllocationCourier,
			ReserveOrderDmpStatusEnum.HadReceiveSuccess,
			ReserveOrderDmpStatusEnum.HaveStationOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveOutZone,
			ReserveOrderDmpStatusEnum.HaveReciveFailure,
			ReserveOrderDmpStatusEnum.HaveFeedbackRetention,
			ReserveOrderDmpStatusEnum.HadClosed
		}),
	
	WAREHOUSE_HANDLE(new ReserveOrderDmpStatusEnum[] {
			ReserveOrderDmpStatusEnum.HadAllocationStation,
			ReserveOrderDmpStatusEnum.HadAllocationCourier,
			ReserveOrderDmpStatusEnum.HaveReciveOutZone,
			ReserveOrderDmpStatusEnum.HaveFeedbackRetention
		});
	
	private ReserveOrderDmpStatusEnum[] reserveOrderStatusArray;
	
	private ReserveOrderStatusClassifyEnum(ReserveOrderDmpStatusEnum[] reserveOrderStatusArray) {
		this.reserveOrderStatusArray = reserveOrderStatusArray;
	}
	
	public ReserveOrderDmpStatusEnum[] toArray() {
		return this.reserveOrderStatusArray;
	}
	
	public String toString() {
		StringBuilder rosSb = new StringBuilder();
		for(int i = 0; i < reserveOrderStatusArray.length; i++) {
			ReserveOrderDmpStatusEnum e = reserveOrderStatusArray[i];
			if(i > 0) {
				rosSb.append(",");
			}
			rosSb.append(e.getIndex());
		}
		return rosSb.toString();
	}
}
