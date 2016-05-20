package cn.explink.enumutil.express2;

import com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum;

/**
 * 预约单状态分类
 * @date 2016年5月19日 下午3:52:10
 */
public enum ReserveOrderStatusClassifyEnum {
	
	QUERY_BY_CUSTOM_SERVICE(new ReserveOrderStatusEnum[] {
			ReserveOrderStatusEnum.HaveProOutZone,
			ReserveOrderStatusEnum.HadAllocationPro,
			ReserveOrderStatusEnum.HadAllocationStation,
			ReserveOrderStatusEnum.HadAllocationCourier,
			ReserveOrderStatusEnum.HadReceiveSuccess,
			ReserveOrderStatusEnum.HaveStationOutZone,
			ReserveOrderStatusEnum.HaveReciveOutZone,
			ReserveOrderStatusEnum.HaveReciveFailure,
			ReserveOrderStatusEnum.HaveFeedbackRetention,
			ReserveOrderStatusEnum.HadClosed
		}),
	
	QUERY_BY_WAREHOUSE_MASTER(new ReserveOrderStatusEnum[] {
			ReserveOrderStatusEnum.HadAllocationStation,
			ReserveOrderStatusEnum.HadAllocationCourier,
			ReserveOrderStatusEnum.HadReceiveSuccess,
			ReserveOrderStatusEnum.HaveStationOutZone,
			ReserveOrderStatusEnum.HaveReciveOutZone,
			ReserveOrderStatusEnum.HaveReciveFailure,
			ReserveOrderStatusEnum.HaveFeedbackRetention,
			ReserveOrderStatusEnum.HadClosed
		}),
	
	HANDLE_BY_CUSTOM_SERVICE(new ReserveOrderStatusEnum[] {
			ReserveOrderStatusEnum.HaveProOutZone,
			ReserveOrderStatusEnum.HadAllocationPro,
			ReserveOrderStatusEnum.HadAllocationStation,
			ReserveOrderStatusEnum.HadAllocationCourier,
			ReserveOrderStatusEnum.HadReceiveSuccess,
			ReserveOrderStatusEnum.HaveStationOutZone,
			ReserveOrderStatusEnum.HaveReciveOutZone,
			ReserveOrderStatusEnum.HaveReciveFailure,
			ReserveOrderStatusEnum.HaveFeedbackRetention,
			ReserveOrderStatusEnum.HadClosed
		}),
	
	HANDLE_BY_WAREHOUSE_MASTER(new ReserveOrderStatusEnum[] {
			ReserveOrderStatusEnum.HadAllocationStation,
			ReserveOrderStatusEnum.HadAllocationCourier,
			ReserveOrderStatusEnum.HadReceiveSuccess,
			ReserveOrderStatusEnum.HaveStationOutZone,
			ReserveOrderStatusEnum.HaveReciveOutZone,
			ReserveOrderStatusEnum.HaveReciveFailure,
			ReserveOrderStatusEnum.HaveFeedbackRetention,
			ReserveOrderStatusEnum.HadClosed
		}),
	
	WAREHOUSE_HANDLE(new ReserveOrderStatusEnum[] {
			ReserveOrderStatusEnum.HadAllocationStation,
			ReserveOrderStatusEnum.HadAllocationCourier,
			ReserveOrderStatusEnum.HaveReciveOutZone,
			ReserveOrderStatusEnum.HaveFeedbackRetention
		});
	
	private ReserveOrderStatusEnum[] reserveOrderStatusArray;
	
	private ReserveOrderStatusClassifyEnum(ReserveOrderStatusEnum[] reserveOrderStatusArray) {
		this.reserveOrderStatusArray = reserveOrderStatusArray;
	}
	
	public ReserveOrderStatusEnum[] toArray() {
		return this.reserveOrderStatusArray;
	}
	
	public String toString() {
		StringBuilder rosSb = new StringBuilder();
		for(int i = 0; i < reserveOrderStatusArray.length; i++) {
			ReserveOrderStatusEnum e = reserveOrderStatusArray[i];
			if(i > 0) {
				rosSb.append(",");
			}
			rosSb.append(e.getIndex());
		}
		return rosSb.toString();
	}
}
