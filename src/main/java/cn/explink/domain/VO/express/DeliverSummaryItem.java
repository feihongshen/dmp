/**
 *
 */
package cn.explink.domain.VO.express;

import java.math.BigDecimal;

import cn.explink.util.poi.excel.annotation.Excel;

/**
 * @author songkaojun 2015年8月9日
 */
public class DeliverSummaryItem {

	/**
	 * 小件员ID
	 */
	private long deliverId;

	/**
	 * 小件员姓名
	 */
	@Excel(exportName = "小件员", exportFieldWidth = 20)
	private String deliverName;

	/**
	 * 汇总单量
	 *
	 */
	@Excel(exportName = "汇总单量", exportFieldWidth = 20)
	private long summaryNum = 0L;

	/**
	 * 汇总运费
	 */
	@Excel(exportName = "汇总运费", exportFieldWidth = 20)
	private BigDecimal summaryFee = new BigDecimal(0);

	/**
	 * 现付单量
	 *
	 */
	@Excel(exportName = "现付单量", exportFieldWidth = 20)
	private long nowPayNum = 0L;

	/**
	 * 现付汇总运费
	 */
	@Excel(exportName = "现付汇总运费", exportFieldWidth = 20)
	private BigDecimal nowPayTotalFee = new BigDecimal(0);

	/**
	 * 到付单量
	 *
	 */
	@Excel(exportName = "到付单量", exportFieldWidth = 20)
	private long arrivePayNum = 0L;

	/**
	 * 到付汇总运费
	 */
	@Excel(exportName = "到付汇总运费", exportFieldWidth = 20)
	private BigDecimal arrivePayTotalFee = new BigDecimal(0);

	/**
	 * 月结单量
	 *
	 */
	@Excel(exportName = "月结单量", exportFieldWidth = 20)
	private long monthPayNum = 0L;

	/**
	 * 月结汇总运费
	 */
	@Excel(exportName = "月结汇总运费", exportFieldWidth = 20)
	private BigDecimal monthPayTotalFee = new BigDecimal(0);

	public long getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(long deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public long getSummaryNum() {
		return this.summaryNum;
	}

	public void setSummaryNum(long summaryNum) {
		this.summaryNum = summaryNum;
	}

	public BigDecimal getSummaryFee() {
		return this.summaryFee;
	}

	public void setSummaryFee(BigDecimal summaryFee) {
		this.summaryFee = summaryFee;
	}

	public long getNowPayNum() {
		return this.nowPayNum;
	}

	public void setNowPayNum(long nowPayNum) {
		this.nowPayNum = nowPayNum;
	}

	public BigDecimal getNowPayTotalFee() {
		return this.nowPayTotalFee;
	}

	public void setNowPayTotalFee(BigDecimal nowPayTotalFee) {
		this.nowPayTotalFee = nowPayTotalFee;
	}

	public long getArrivePayNum() {
		return this.arrivePayNum;
	}

	public void setArrivePayNum(long arrivePayNum) {
		this.arrivePayNum = arrivePayNum;
	}

	public BigDecimal getArrivePayTotalFee() {
		return this.arrivePayTotalFee;
	}

	public void setArrivePayTotalFee(BigDecimal arrivePayTotalFee) {
		this.arrivePayTotalFee = arrivePayTotalFee;
	}

	public long getMonthPayNum() {
		return this.monthPayNum;
	}

	public void setMonthPayNum(long monthPayNum) {
		this.monthPayNum = monthPayNum;
	}

	public BigDecimal getMonthPayTotalFee() {
		return this.monthPayTotalFee;
	}

	public void setMonthPayTotalFee(BigDecimal monthPayTotalFee) {
		this.monthPayTotalFee = monthPayTotalFee;
	}

}
