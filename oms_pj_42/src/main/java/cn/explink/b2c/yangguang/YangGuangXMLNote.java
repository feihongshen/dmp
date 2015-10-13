package cn.explink.b2c.yangguang;

/**
 * 央广 XML的节点
 * 
 * @author Administrator
 *
 */
public class YangGuangXMLNote {

	private String serialNo; // 序列
	private String orderNo; // 订购编号
	private String shippNo; // 运单编号
	private String deliveryDate; // 送货日
	private String deliveryResult; // 送货结果 1结束 2拒收 3等待二次指令
	private String exptReason; // 未送货原因
	private String express_id; // 快递公司标识
	private String Wb_I_No; // 运单识别编号

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShippNo() {
		return shippNo;
	}

	public void setShippNo(String shippNo) {
		this.shippNo = shippNo;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryResult() {
		return deliveryResult;
	}

	public void setDeliveryResult(String deliveryResult) {
		this.deliveryResult = deliveryResult;
	}

	public String getExptReason() {
		return exptReason;
	}

	public void setExptReason(String exptReason) {
		this.exptReason = exptReason;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getWb_I_No() {
		return Wb_I_No;
	}

	public void setWb_I_No(String wb_I_No) {
		Wb_I_No = wb_I_No;
	}

}
