package cn.explink.b2c.ems;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * dmp与ems运单对照关系
 * @author huan.zhou
 */
@XmlRootElement
public class EmsAndDmpTranscwb {
	private String bigAccountDataId;//邮件数据的主键
	private String billno;//快递单号
	private String backBillno;//反向单号
	
	@XmlElement(name = "bigAccountDataId")
	public String getBigAccountDataId() {
		return bigAccountDataId;
	}
	public void setBigAccountDataId(String bigAccountDataId) {
		this.bigAccountDataId = bigAccountDataId;
	}
	@XmlElement(name = "billno")
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	@XmlElement(name = "backBillno")
	public String getBackBillno() {
		return backBillno;
	}
	public void setBackBillno(String backBillno) {
		this.backBillno = backBillno;
	}
	
}
