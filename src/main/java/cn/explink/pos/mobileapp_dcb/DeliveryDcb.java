package cn.explink.pos.mobileapp_dcb;

import java.math.BigDecimal;

/**
 * 物流E通的派送信息
 * 
 * @author Administrator
 *
 */
public class DeliveryDcb {

	private String cwb;
	private String username;
	private String delivermobile;
	private String podresultid;
	private String backreasonid;
	private String leavedreasonid;

	private BigDecimal receivedfeecash;
	private BigDecimal receivedfeepos;
	private BigDecimal receivedfeecheque;

	private String remark;
	private String photofile;
	private String operatedate;

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDelivermobile() {
		return delivermobile;
	}

	public void setDelivermobile(String delivermobile) {
		this.delivermobile = delivermobile;
	}

	public String getPodresultid() {
		return podresultid;
	}

	public void setPodresultid(String podresultid) {
		this.podresultid = podresultid;
	}

	public String getBackreasonid() {
		return backreasonid;
	}

	public void setBackreasonid(String backreasonid) {
		this.backreasonid = backreasonid;
	}

	public String getLeavedreasonid() {
		return leavedreasonid;
	}

	public void setLeavedreasonid(String leavedreasonid) {
		this.leavedreasonid = leavedreasonid;
	}

	public BigDecimal getReceivedfeecash() {
		return receivedfeecash;
	}

	public void setReceivedfeecash(BigDecimal receivedfeecash) {
		this.receivedfeecash = receivedfeecash;
	}

	public BigDecimal getReceivedfeepos() {
		return receivedfeepos;
	}

	public void setReceivedfeepos(BigDecimal receivedfeepos) {
		this.receivedfeepos = receivedfeepos;
	}

	public BigDecimal getReceivedfeecheque() {
		return receivedfeecheque;
	}

	public void setReceivedfeecheque(BigDecimal receivedfeecheque) {
		this.receivedfeecheque = receivedfeecheque;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhotofile() {
		return photofile;
	}

	public void setPhotofile(String photofile) {
		this.photofile = photofile;
	}

	public String getOperatedate() {
		return operatedate;
	}

	public void setOperatedate(String operatedate) {
		this.operatedate = operatedate;
	}
}
