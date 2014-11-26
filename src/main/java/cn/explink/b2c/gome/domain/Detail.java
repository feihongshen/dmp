package cn.explink.b2c.gome.domain;

import javax.xml.bind.annotation.XmlElement;

public class Detail {
	private String tieNumber;// 订单系号
	private String lineNumber;// 订单行编号
	private String partNumber;// 商品编码
	private String partDesc;// 商品名称
	private String qty;// 数量
	private String qcReportFlag;// 是否需要质检报告
	private String invoiceCollectFlag;// 是否需要原始发票
	private String packingList;// 包装清单
	private String serialNumber;// 序列号
	private String comments;// 备注

	@XmlElement(name = "tieNumber")
	public String getTieNumber() {
		return tieNumber;
	}

	public void setTieNumber(String tieNumber) {
		this.tieNumber = tieNumber;
	}

	@XmlElement(name = "lineNumber")
	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	@XmlElement(name = "partNumber")
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@XmlElement(name = "partDesc")
	public String getPartDesc() {
		return partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	@XmlElement(name = "qty")
	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	@XmlElement(name = "qcReportFlag")
	public String getQcReportFlag() {
		return qcReportFlag;
	}

	public void setQcReportFlag(String qcReportFlag) {
		this.qcReportFlag = qcReportFlag;
	}

	@XmlElement(name = "invoiceCollectFlag")
	public String getInvoiceCollectFlag() {
		return invoiceCollectFlag;
	}

	public void setInvoiceCollectFlag(String invoiceCollectFlag) {
		this.invoiceCollectFlag = invoiceCollectFlag;
	}

	@XmlElement(name = "packingList")
	public String getPackingList() {
		return packingList;
	}

	public void setPackingList(String packingList) {
		this.packingList = packingList;
	}

	@XmlElement(name = "serialNumber")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@XmlElement(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
