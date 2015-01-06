package cn.explink.domain;

import java.sql.Timestamp;

/**
 * 超期异常监控VO.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class OverdueExMoVO {

	public static final String ID = "id";

	public static final String CWB = "id";

	public static final String VENDER_ID = "vender_id";

	public static final String WAREHOUSE_ID = "warehouse_id";

	public static final String DELIVER_STATION_ID = "deliver_station_id";

	public static final String CREATE_TIME = "create_time";

	public static final String SYSTEM_ACCEPT_TIME = "system_accept_time";

	public static final String STATION_ACCEPT_TIME = "station_accept_time";

	public static final String DISPATCH_TIME = "dispatch_time";

	public static final String PRINT_TIME = "print_time";

	public static final String REPORT_OUTAREA_TIME = "report_outarea_time";

	public static final String EXCEPTION_MATCH_TIME = "exception_match_time";

	public static final String FEEDBACK_TIME = "feedback_time";

	private long id = -1;

	private String cwb = null;

	private long venderId = -1;

	private long warehouseId = -1;

	private long deliverStationId = -1;

	private Timestamp createTime = null;

	private Timestamp systemAcceptTime = null;

	private Timestamp stationAcceptTime = null;

	private Timestamp dispatchTime = null;

	private Timestamp printTime = null;

	private Timestamp reportOutareaTime = null;

	private Timestamp exceptionMatchTime = null;

	private Timestamp feedbackTime = null;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(long venderId) {
		this.venderId = venderId;
	}

	public long getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getDeliverStationId() {
		return this.deliverStationId;
	}

	public void setDeliverStationId(long deliverStationId) {
		this.deliverStationId = deliverStationId;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getSystemAcceptTime() {
		return this.systemAcceptTime;
	}

	public void setSystemAcceptTime(Timestamp systemAcceptTime) {
		this.systemAcceptTime = systemAcceptTime;
	}

	public Timestamp getStationAcceptTime() {
		return this.stationAcceptTime;
	}

	public void setStationAcceptTime(Timestamp stationAcceptTime) {
		this.stationAcceptTime = stationAcceptTime;
	}

	public Timestamp getDispatchTime() {
		return this.dispatchTime;
	}

	public void setDispatchTime(Timestamp dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public Timestamp getPrintTime() {
		return this.printTime;
	}

	public void setPrintTime(Timestamp printTime) {
		this.printTime = printTime;
	}

	public Timestamp getReportOutareaTime() {
		return this.reportOutareaTime;
	}

	public void setReportOutareaTime(Timestamp reportOutareaTime) {
		this.reportOutareaTime = reportOutareaTime;
	}

	public Timestamp getExceptionMatchTime() {
		return this.exceptionMatchTime;
	}

	public void setExceptionMatchTime(Timestamp exceptionMatchTime) {
		this.exceptionMatchTime = exceptionMatchTime;
	}

	public Timestamp getFeedbackTime() {
		return this.feedbackTime;
	}

	public void setFeedbackTime(Timestamp feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

}
