package cn.explink.pos.tools;

import java.math.BigDecimal;

/**
 * pos交易详情记录表
 * 
 * @author Administrator
 *
 */
public class PosTradeDetail {
	private long payid;
	private String pos_code; // pos支付方编码（alipay，yeepay，unionpay）
	private String cwb; // 交易单号
	private String tradeTime; // 交易时间
	private long tradeDeliverId; // 小件员
	private int tradeTypeId; // 收、出账类型（1收账；2出账）
	private int payTypeId; // 交易类型（1现金、2.pos、3支票、4.账户）枚举
	private BigDecimal payAmount; // 交易金额
	private String payDetail; // 交易详情
	private String payRemark; // 交易备注信息
	private String signName; // 签收人
	private String signTime; // 签收时间
	private String signRemark; // 签收备注
	private int signtypeid; // 签收类型（1本人签收，2他人签收）
	private int isSuccessFlag; // 交易结果，是否撤销（1.交易成功、2撤销）

	public int getIsonlineFlag() {
		return isonlineFlag;
	}

	public void setIsonlineFlag(int isonlineFlag) {
		this.isonlineFlag = isonlineFlag;
	}

	private String acq_type; // 收单模式(single单笔，merge合单，split分单)
	private long customerid; // 供货商
	private int isonlineFlag; // 是否线上 0 线上 ，1手工反馈

	private String terminal_no; // 终端号
	private String emaildate; // 发货时间

	public String getAcq_type() {
		return acq_type;
	}

	public void setAcq_type(String acq_type) {
		this.acq_type = acq_type;
	}

	public int getIsSuccessFlag() {
		return isSuccessFlag;
	}

	public void setIsSuccessFlag(int isSuccessFlag) {
		this.isSuccessFlag = isSuccessFlag;
	}

	public long getPayid() {
		return payid;
	}

	public void setPayid(long payid) {
		this.payid = payid;
	}

	public String getPos_code() {
		return pos_code;
	}

	public void setPos_code(String pos_code) {
		this.pos_code = pos_code;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public long getTradeDeliverId() {
		return tradeDeliverId;
	}

	public void setTradeDeliverId(long tradeDeliverId) {
		this.tradeDeliverId = tradeDeliverId;
	}

	public int getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(int tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayDetail() {
		return payDetail;
	}

	public void setPayDetail(String payDetail) {
		this.payDetail = payDetail;
	}

	public String getPayRemark() {
		return payRemark;
	}

	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getSignRemark() {
		return signRemark;
	}

	public void setSignRemark(String signRemark) {
		this.signRemark = signRemark;
	}

	public int getSigntypeid() {
		return signtypeid;
	}

	public void setSigntypeid(int signtypeid) {
		this.signtypeid = signtypeid;
	}

	public String getPos_codeText() {
		String pos_codeText = "";
		for (PosEnum pe : PosEnum.values()) {
			if (pos_code.equals(pe.getMethod())) {
				return pe.getText();
			}
		}
		return pos_codeText;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getTerminal_no() {
		return terminal_no;
	}

	public void setTerminal_no(String terminal_no) {
		this.terminal_no = terminal_no;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

}
