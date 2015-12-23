package cn.explink.domain.VO;

import cn.explink.util.poi.excel.annotation.Excel;

public class TransProvincialAuditReconciliationExportVO {
	/*
	 * 运单号
	 */
	@Excel(exportName = "运单号", exportFieldWidth = 20)
	private String orderNo;
	/*
	 *件数
	 */
	@Excel(exportName = "件数", exportFieldWidth = 20)
	private String number;
	/*
	 * 小件员(揽件)
	 */
	@Excel(exportName = "揽件员", exportFieldWidth = 20)
	private String delivermanName;
	/*
	 * 小件员(派件)
	 */
	@Excel(exportName = "派件员", exportFieldWidth = 20)
	private String delivername;
	/*
	 * 代收货款金额
	 */
	@Excel(exportName = "代收货款", exportFieldWidth = 20)
	private String collection_amount;
	/*
	 * 揽件入站站点名称
	 */
	@Excel(exportName = "揽件站点", exportFieldWidth = 20)
	private String instationname;

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDelivermanName() {
		return this.delivermanName;
	}

	public void setDelivermanName(String delivermanName) {
		this.delivermanName = delivermanName;
	}

	public String getDelivername() {
		return this.delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getCollection_amount() {
		return this.collection_amount;
	}

	public void setCollection_amount(String collection_amount) {
		this.collection_amount = collection_amount;
	}

	public String getInstationname() {
		return this.instationname;
	}

	public void setInstationname(String instationname) {
		this.instationname = instationname;
	}

}
