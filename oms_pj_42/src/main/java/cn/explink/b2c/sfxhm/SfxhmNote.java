package cn.explink.b2c.sfxhm;

public class SfxhmNote {

	private String scan_type; // 扫描类型
	private String mailno; // 运单号
	private String scan_time; // 扫描时间
	private String upload_time; // 上传时间
	private String scan_zone; // 扫描网点
	private String operator;// 操作员
	private String shipper; // 派件员
	private String note; // 备注

	public String getUpload_time() {
		return upload_time;
	}

	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}

	public String getScan_type() {
		return scan_type;
	}

	public void setScan_type(String scan_type) {
		this.scan_type = scan_type;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getScan_time() {
		return scan_time;
	}

	public void setScan_time(String scan_time) {
		this.scan_time = scan_time;
	}

	public String getScan_zone() {
		return scan_zone;
	}

	public void setScan_zone(String scan_zone) {
		this.scan_zone = scan_zone;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
