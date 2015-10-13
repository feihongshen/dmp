package cn.explink.b2c.liantong.json;

/**
 * 同一个订单号的详细信息
 * 
 * @author Administrator
 *
 */
public class Step {

	private String acceptTime; // 处理时间
	private String acceptAddress; // 处理地点
	private String acceptAction; // 处理动作
	private String AcceptName; // 处理人员/签收人

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(String acceptAction) {
		this.acceptAction = acceptAction;
	}

	public String getAcceptName() {
		return AcceptName;
	}

	public void setAcceptName(String acceptName) {
		AcceptName = acceptName;
	}

}
