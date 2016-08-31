package cn.explink.domain;

/**
 * 上门退信心-实体
 * @author chunlei05.li
 * @date 2016年8月22日 下午3:50:30
 */
public class SmtCwb {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 订单号
	 */
	private String cwb;
	
	/**
	 * 退货号
	 */
	private String returnNo;
	
	/**
	 * 退货地址
	 */
	private String returnAddress;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	@Override
	public String toString() {
		return "ReturnCwb [id=" + id + ", cwb=" + cwb + ", returnNo=" + returnNo + ", returnAddress=" + returnAddress
				+ "]";
	}
}
