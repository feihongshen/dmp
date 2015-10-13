package cn.explink.b2c.yonghuics;

/**
 * yihaodian XML的节点
 * 
 * @author Administrator
 *
 */
public class YonghuiJSONnote {

	private String userCode;
	private String requestTime;
	private String sign;
	private String sheetid; // cwb订单号
	private String bagno; // 运单号
	private String flag; // 状态 1=送货中/2=已收货/9=拒收
	private String sender; // 派送员
	private String sendphone;// 配送手机
	private String sdate; // 发生日期
	private String note; // 备注信息

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSheetid() {
		return sheetid;
	}

	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}

	public String getBagno() {
		return bagno;
	}

	public void setBagno(String bagno) {
		this.bagno = bagno;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSendphone() {
		return sendphone;
	}

	public void setSendphone(String sendphone) {
		this.sendphone = sendphone;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
