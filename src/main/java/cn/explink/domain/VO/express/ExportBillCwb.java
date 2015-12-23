package cn.explink.domain.VO.express;

import java.math.BigDecimal;

import cn.explink.util.poi.excel.annotation.Excel;
public class ExportBillCwb extends ExpressCwbOrderForTakeGoodsQueryVO{

	/**
	 * 订单号
	 */
	@Excel(exportName = "运单号", exportFieldWidth = 25)
	private String cwb;
	/**
	 * 件数
	 */
	@Excel(exportName = "件数", exportFieldWidth = 20)
	private Integer sendnum;
	/**
	 * 揽件员
	 */
	@Excel(exportName = "揽件员", exportFieldWidth = 20)
	private String collectorname;
	/**
	 * 派件员
	 */
	@Excel(exportName = "派件员", exportFieldWidth = 20)
	private Integer deliverid;
	/**
	 * 费用合计
	 */
	@Excel(exportName = "费用合计", exportFieldWidth = 20)
	private BigDecimal totalfee;

	/**
	 * 运费
	 */
	@Excel(exportName = "运费", exportFieldWidth = 20)
	private BigDecimal shouldfare;
	/**
	 * 包装费用
	 */
	@Excel(exportName = "包装费用", exportFieldWidth = 20)
	private BigDecimal packagefee;
	/**
	 * 保价费用
	 */
	@Excel(exportName = "保价费用", exportFieldWidth = 20)
	private BigDecimal insuredfee;


	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public int getSendnum() {
		if(sendnum == null){
			sendnum = 0;
		}
		return sendnum;
	}
	public void setSendnum(Integer sendnum) {
		this.sendnum = sendnum;
	}
	public String getCollectorname() {
		return collectorname;
	}
	public void setCollectorname(String collectorname) {
		this.collectorname = collectorname;
	}
	public long getDeliverid() {
		if(deliverid == null){
			deliverid = 0;
		}
		return deliverid;
	}
	public void setDeliverid(Integer deliverid) {
		this.deliverid = deliverid;
	}
	public BigDecimal getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}
	public BigDecimal getShouldfare() {
		return shouldfare;
	}
	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}
	public BigDecimal getPackagefee() {
		return packagefee;
	}
	public void setPackagefee(BigDecimal packagefee) {
		this.packagefee = packagefee;
	}
	public BigDecimal getInsuredfee() {
		return insuredfee;
	}
	public void setInsuredfee(BigDecimal insuredfee) {
		this.insuredfee = insuredfee;
	}


}
