package cn.explink.domain.express;

import java.math.BigDecimal;

/**
 * 揽件查询VO
 * @author wangzy
 *
 */
public class ExpressCwb4TakeGoodsQuery {

	/*
	 * 状态
	 */
	private Integer status;
	/*
	 * 站点
	 */
	private String station;
	/*
	 * 小件员id
	 */
	private Integer collectorid;
	/*
	 * 付款方式
	 */
	private Integer payWay;
	/*
	 * 入站时间
	 */
	private String startTime;
	/*
	 * 出战时间
	 */
	private String endTime;
	/*
	 * 汇总单量
	 */
	private int countAll;
	/*
	 * 汇总运费
	 */
	private BigDecimal shouldfareAll;
	/*
	 * 现付单量
	 */
	private int countNow;
	/*
	 * 现付汇总运费
	 */
	private BigDecimal shouldfareNow;
	/*
	 * 到付单量
	 */
	private int countArrive;
	/*
	 * 到付汇总运费
	 */
	private BigDecimal shouldfareArrive;
	/*
	 * 月结单量
	 */
	private int countMonth;
	/*
	 * 月结汇总运费
	 */
	private BigDecimal shouldfareMonth;
	/**
	 * 导出标志位
	 */
	private String exportFlag;
	
	
	public String getExportFlag() {
		return exportFlag;
	}
	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Integer getCollectorid() {
		return collectorid;
	}
	public void setCollectorid(Integer collectorid) {
		this.collectorid = collectorid;
	}
	public Integer getPayWay() {
		return payWay;
	}
	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getCountAll() {
		return countAll;
	}
	public void setCountAll(int countAll) {
		this.countAll = countAll;
	}
	public BigDecimal getShouldfareAll() {
		return shouldfareAll;
	}
	public void setShouldfareAll(BigDecimal shouldfareAll) {
		this.shouldfareAll = shouldfareAll;
	}
	public int getCountNow() {
		return countNow;
	}
	public void setCountNow(int countNow) {
		this.countNow = countNow;
	}
	public BigDecimal getShouldfareNow() {
		return shouldfareNow;
	}
	public void setShouldfareNow(BigDecimal shouldfareNow) {
		this.shouldfareNow = shouldfareNow;
	}
	public int getCountArrive() {
		return countArrive;
	}
	public void setCountArrive(int countArrive) {
		this.countArrive = countArrive;
	}
	public BigDecimal getShouldfareArrive() {
		return shouldfareArrive;
	}
	public void setShouldfareArrive(BigDecimal shouldfareArrive) {
		this.shouldfareArrive = shouldfareArrive;
	}
	public int getCountMonth() {
		return countMonth;
	}
	public void setCountMonth(int countMonth) {
		this.countMonth = countMonth;
	}
	public BigDecimal getShouldfareMonth() {
		return shouldfareMonth;
	}
	public void setShouldfareMonth(BigDecimal shouldfareMonth) {
		this.shouldfareMonth = shouldfareMonth;
	}
}
