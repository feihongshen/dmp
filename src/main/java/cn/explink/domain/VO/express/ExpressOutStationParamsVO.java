package cn.explink.domain.VO.express;
/**
 * 揽件出站的参数
 * @author jiangyu 2015年8月4日
 *
 */
public class ExpressOutStationParamsVO {
	/**
	 * 扫描的单号
	 */
	private String scanNo;
	/**
	 * 下一站
	 */
	private Long nextBranch=0L;
	/**
	 * 驾驶员
	 */
	private Long driverId=0L;
	/**
	 * 车牌号
	 */
	private Long vehicleId=0L;
	/**
	 * 车类型
	 */
	private String vehicleType="";
	/**
	 * 路径
	 */
	public String contextPath;
	
	public ExpressOutStationParamsVO() {
	}

	public String getScanNo() {
		return scanNo;
	}

	public void setScanNo(String scanNo) {
		this.scanNo = scanNo;
	}

	public Long getNextBranch() {
		return nextBranch;
	}

	public void setNextBranch(Long nextBranch) {
		this.nextBranch = nextBranch;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
