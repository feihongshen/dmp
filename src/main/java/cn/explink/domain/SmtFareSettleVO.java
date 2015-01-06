package cn.explink.domain;

/**
 * 上门退运费结算VO.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
/**
 * @author zhaoshb
 * @since AR1.0
 */
public class SmtFareSettleVO {

	private Long stationId = null;

	private String stationName = null;

	private Long venderId = null;

	private String venderName = null;

	private Long deliverId = null;

	private String deliverName = null;

	private String stationAcceptCnt = "";

	private String deliverPickingCnt = "";

	private String smtSuccessedCnt = "";

	private String shouldFee = "";

	private String receivedFee = "";

	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	public String getVenderName() {
		return this.venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public Long getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(Long deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public String getStationAcceptCnt() {
		return this.stationAcceptCnt;
	}

	public void setStationAcceptCnt(String stationAcceptCnt) {
		this.stationAcceptCnt = stationAcceptCnt;
	}

	public String getSmtSuccessedCnt() {
		return this.smtSuccessedCnt;
	}

	public void setSmtSuccessedCnt(String smtSuccessedCnt) {
		this.smtSuccessedCnt = smtSuccessedCnt;
	}

	public String getShouldFee() {
		return this.shouldFee;
	}

	public void setShouldFee(String shouldFee) {
		this.shouldFee = shouldFee;
	}

	public String getDeliverPickingCnt() {
		return this.deliverPickingCnt;
	}

	public void setDeliverPickingCnt(String deliverPickingCnt) {
		this.deliverPickingCnt = deliverPickingCnt;
	}

	public String getReceivedFee() {
		return this.receivedFee;
	}

	public void setReceivedFee(String receivedFee) {
		this.receivedFee = receivedFee;
	}

}
