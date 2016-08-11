package cn.explink.b2c.ems;

/**
 * 发送给ems的订单信息
 * @author huan.zhou
 */
public class SendToEMSOrder {
	private String cwb;//订单号
	private String transcwb;//运单号
	private String email_num;//邮政运单号
	private String credate;//记录生产时间
	private String getMailnumFlag;//获取ems运单号标志
	private String addTranscwbFlag;//生成运单号标志
	private String data;//发送给ems的报文
	private Long orderDirection;//订单去向 add by zhouhuan 2016-07-21
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public String getCredate() {
		return credate;
	}
	public void setCredate(String credate) {
		this.credate = credate;
	}
	public String getGetMailnumFlag() {
		return getMailnumFlag;
	}
	public void setGetMailnumFlag(String getMailnumFlag) {
		this.getMailnumFlag = getMailnumFlag;
	}
	public String getAddTranscwbFlag() {
		return addTranscwbFlag;
	}
	public void setAddTranscwbFlag(String addTranscwbFlag) {
		this.addTranscwbFlag = addTranscwbFlag;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Long getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(Long orderDirection) {
		this.orderDirection = orderDirection;
	}
	public String getEmail_num() {
		return email_num;
	}
	public void setEmail_num(String email_num) {
		this.email_num = email_num;
	}
}
