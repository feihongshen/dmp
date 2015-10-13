package cn.explink.b2c.yemaijiu;

/**
 * 广州思迈签收，拒收，异常反馈的存储的对象，转为json——str
 * 
 * @author Administrator
 *
 */
public class YeMaiJiuXMLNote {

	private String boxNo;
	private String sendNo;
	private String meno;
	private String signtime;
	private String signUser;
	private String status;

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getSendNo() {
		return sendNo;
	}

	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}

	public String getSigntime() {
		return signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public String getSignUser() {
		return signUser;
	}

	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
