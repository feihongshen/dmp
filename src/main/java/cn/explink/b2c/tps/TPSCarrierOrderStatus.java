package cn.explink.b2c.tps;
/**
 * TPS运单状态对接配置属性
 * @author yurong.liang 2015/12/19
 */
public class TPSCarrierOrderStatus {
	
	private String shipper_no; // 承运商编码
	private int getMaxCount; // 每次获取订单最大数量
	private int seq; // 每次获取的seq，之后不断累加
	
	
	public String getShipper_no() {
		return shipper_no;
	}
	public void setShipper_no(String shipper_no) {
		this.shipper_no = shipper_no;
	}
	public int getGetMaxCount() {
		return getMaxCount;
	}
	public void setGetMaxCount(int getMaxCount) {
		this.getMaxCount = getMaxCount;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
}
