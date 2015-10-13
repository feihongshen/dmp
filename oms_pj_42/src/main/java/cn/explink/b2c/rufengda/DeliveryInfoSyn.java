package cn.explink.b2c.rufengda;

/**
 * 订单
 * 
 * @author Administrator
 *
 */
public class DeliveryInfoSyn {

	private String rps_OrderNo; // 订单号
	private String rps_OperatorType; // 操作类型(1=入站，2=分配配送员，3=妥投，4=拒收，5=滞留)
	private int rps_OrderType; // 订单类型（普通=0、换货=1、退货=2）
	private String rps_OperateTime; // 操作时间。
	private String rps_DeliveryMan = ""; // 配送员
	private String rps_DeliverManCode = ""; // 配送员编号
	private int rps_SendMsg; // 是否发短信息 0 不发，1发
	private String rps_Reason = ""; // 滞留或拒收原因
	private String rps_PaymentType; // 支付方式
	private String rps_Comments; // 具体描述 s

	public String getRps_OrderNo() {
		return rps_OrderNo;
	}

	public void setRps_OrderNo(String rps_OrderNo) {
		this.rps_OrderNo = rps_OrderNo;
	}

	public String getRps_OperatorType() {
		return rps_OperatorType;
	}

	public void setRps_OperatorType(String rps_OperatorType) {
		this.rps_OperatorType = rps_OperatorType;
	}

	public int getRps_OrderType() {
		return rps_OrderType;
	}

	public void setRps_OrderType(int rps_OrderType) {
		this.rps_OrderType = rps_OrderType;
	}

	public String getRps_OperateTime() {
		return rps_OperateTime;
	}

	public void setRps_OperateTime(String rps_OperateTime) {
		this.rps_OperateTime = rps_OperateTime;
	}

	public String getRps_DeliveryMan() {
		return rps_DeliveryMan;
	}

	public void setRps_DeliveryMan(String rps_DeliveryMan) {
		this.rps_DeliveryMan = rps_DeliveryMan;
	}

	public String getRps_DeliverManCode() {
		return rps_DeliverManCode;
	}

	public void setRps_DeliverManCode(String rps_DeliverManCode) {
		this.rps_DeliverManCode = rps_DeliverManCode;
	}

	public int getRps_SendMsg() {
		return rps_SendMsg;
	}

	public void setRps_SendMsg(int rpsSendMsg) {
		rps_SendMsg = rpsSendMsg;
	}

	public String getRps_Reason() {
		return rps_Reason;
	}

	public void setRps_Reason(String rps_Reason) {
		this.rps_Reason = rps_Reason;
	}

	public String getRps_PaymentType() {
		return rps_PaymentType;
	}

	public void setRps_PaymentType(String rps_PaymentType) {
		this.rps_PaymentType = rps_PaymentType;
	}

	public String getRps_Comments() {
		return rps_Comments;
	}

	public void setRps_Comments(String rps_Comments) {
		this.rps_Comments = rps_Comments;
	}

	public static void main(String[] args) {
		System.out.println(new DeliveryInfoSyn().getRps_DeliveryMan());
	}
}
