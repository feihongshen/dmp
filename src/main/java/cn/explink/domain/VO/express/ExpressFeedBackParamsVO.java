package cn.explink.domain.VO.express;
/**
 * 反馈操作的时候前台交互传递的参数
 * @author jiangyu 2015年8月7日
 *
 */
public class ExpressFeedBackParamsVO {
	/**
	 * 预订单号
	 */
	private String preOrderNoEdit;
	/**
	 * 小件员
	 */
	private Integer deliveryEditId=0;
	/**
	 * 反馈结果
	 */
	private Integer pickResultId;
	/**
	 * 运单号
	 */
	private String transNo="";
	/**
	 * 揽件失败
	 */
	private Long pickFailedFirstLevel;
	
	private Long pickFailedSecondLevel;
	/**
	 * 站点超区
	 */
	private Long areaWrongFirstLevel;
	
	private Long areaWrongSecondLevel;
	/**
	 * 揽件超区
	 */
	private Long pickWrongFirstLevel;
	
	private Long pickWrongSecondLevel;
	/**
	 * 揽件延迟
	 */
	private Long pickDelayFirstLevel;
	
	private Long pickDelaySecondLevel;
	/**
	 * 预计下次揽件时间
	 */
	private String nextPickExpressTime="";
	/**
	 * 反馈备注输入内容
	 */
	private String feedBackRemark="";
	/**
	 * 反馈时间
	 */
	private String feedBackTime="";
	
	public ExpressFeedBackParamsVO() {
	}

	public String getPreOrderNoEdit() {
		return preOrderNoEdit.trim();
	}

	public void setPreOrderNoEdit(String preOrderNoEdit) {
		this.preOrderNoEdit = preOrderNoEdit;
	}

	public Integer getDeliveryEditId() {
		return deliveryEditId;
	}

	public void setDeliveryEditId(Integer deliveryEditId) {
		this.deliveryEditId = deliveryEditId;
	}

	public Integer getPickResultId() {
		return pickResultId;
	}

	public void setPickResultId(Integer pickResultId) {
		this.pickResultId = pickResultId;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public Long getPickFailedFirstLevel() {
		return pickFailedFirstLevel;
	}

	public void setPickFailedFirstLevel(Long pickFailedFirstLevel) {
		this.pickFailedFirstLevel = pickFailedFirstLevel;
	}

	public Long getPickFailedSecondLevel() {
		return pickFailedSecondLevel;
	}

	public void setPickFailedSecondLevel(Long pickFailedSecondLevel) {
		this.pickFailedSecondLevel = pickFailedSecondLevel;
	}

	public Long getAreaWrongFirstLevel() {
		return areaWrongFirstLevel;
	}

	public void setAreaWrongFirstLevel(Long areaWrongFirstLevel) {
		this.areaWrongFirstLevel = areaWrongFirstLevel;
	}

	public Long getAreaWrongSecondLevel() {
		return areaWrongSecondLevel;
	}

	public void setAreaWrongSecondLevel(Long areaWrongSecondLevel) {
		this.areaWrongSecondLevel = areaWrongSecondLevel;
	}

	public Long getPickWrongFirstLevel() {
		return pickWrongFirstLevel;
	}

	public void setPickWrongFirstLevel(Long pickWrongFirstLevel) {
		this.pickWrongFirstLevel = pickWrongFirstLevel;
	}

	public Long getPickWrongSecondLevel() {
		return pickWrongSecondLevel;
	}

	public void setPickWrongSecondLevel(Long pickWrongSecondLevel) {
		this.pickWrongSecondLevel = pickWrongSecondLevel;
	}

	public Long getPickDelayFirstLevel() {
		return pickDelayFirstLevel;
	}

	public void setPickDelayFirstLevel(Long pickDelayFirstLevel) {
		this.pickDelayFirstLevel = pickDelayFirstLevel;
	}

	public Long getPickDelaySecondLevel() {
		return pickDelaySecondLevel;
	}

	public void setPickDelaySecondLevel(Long pickDelaySecondLevel) {
		this.pickDelaySecondLevel = pickDelaySecondLevel;
	}

	public String getNextPickExpressTime() {
		return nextPickExpressTime;
	}

	public void setNextPickExpressTime(String nextPickExpressTime) {
		this.nextPickExpressTime = nextPickExpressTime;
	}

	public String getFeedBackRemark() {
		return feedBackRemark;
	}

	public void setFeedBackRemark(String feedBackRemark) {
		this.feedBackRemark = feedBackRemark;
	}

	public String getFeedBackTime() {
		return feedBackTime;
	}

	public void setFeedBackTime(String feedBackTime) {
		this.feedBackTime = feedBackTime;
	}
}
