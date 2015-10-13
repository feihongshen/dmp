package cn.explink.b2c.chinamobile;

/**
 *
 * @author Administrator
 *
 */
public class ChinamobileXMLNote {

	private String partnerId; // 快递公司标识
	private String oprnum; // cwb
	private String delieverId; // transcwb

	private String sendTime; // 发送时间
	private String status; // 状态
	private String showMsg; // 状态描述
	private String deliveryfailure; // 配送失败信息

	public void setDeliveryfailure(String deliveryfailure) {
		this.deliveryfailure = deliveryfailure;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getOprnum() {
		return oprnum;
	}

	public void setOprnum(String oprnum) {
		this.oprnum = oprnum;
	}

	public String getDelieverId() {
		return delieverId;
	}

	public void setDelieverId(String delieverId) {
		this.delieverId = delieverId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	public String getDeliveryfailure() {
		return deliveryfailure;
	}

}
