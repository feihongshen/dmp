package cn.explink.domain.VO.express;

/**
 * 快递操作的前台参数封装
 * @author jiangyu 2015年7月31日
 *
 */
public class ExpressParamsVO {
	
	private Integer processState=1;
	
	private Long deliveryId;
	
	private Integer payType;
	
	private String ids;
	
	private Long branchId;
	
	private String pickExpressTime;
	
	public ExpressParamsVO() {
	}

	public Integer getProcessState() {
		return processState;
	}

	public void setProcessState(Integer processState) {
		this.processState = processState;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getPickExpressTime() {
		return pickExpressTime;
	}

	public void setPickExpressTime(String pickExpressTime) {
		this.pickExpressTime = pickExpressTime;
	}
}
