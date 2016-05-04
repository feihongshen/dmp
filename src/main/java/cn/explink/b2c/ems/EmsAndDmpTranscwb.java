package cn.explink.b2c.ems;

/**
 * dmp与ems运单对照关系
 * @author huan.zhou
 */
public class EmsAndDmpTranscwb {
	private String bigAccountDataId;
	private String billno;
	private String backBillno;
	public String getBigAccountDataId() {
		return bigAccountDataId;
	}
	public void setBigAccountDataId(String bigAccountDataId) {
		this.bigAccountDataId = bigAccountDataId;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getBackBillno() {
		return backBillno;
	}
	public void setBackBillno(String backBillno) {
		this.backBillno = backBillno;
	}
	
}
