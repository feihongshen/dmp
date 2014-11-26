package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.enumutil.DeliverPayupArrearageapprovedEnum;
import cn.explink.enumutil.DeliverPayuptypeEnum;

public class GotoClassAuditing {

	private long id;
	private String auditingtime;
	private BigDecimal payupamount;
	private BigDecimal payupamount_pos;
	private long receivedfeeuser;
	private long branchid;
	private long payupid;
	private long deliverealuser;

	private int state;
	private int deliverpayuptype; // 小件员交款类型 0非小件员交款 1网银（网银需要小票号） 2POS 3现金
	private BigDecimal deliverpayupamount; // 小件员交款金额 小数点两位
	private BigDecimal deliverpayupamount_pos; // 小件员交用户POS刷卡交款金额 小数点两位
	private String deliverpayupbanknum; // 小件员交款网银的小票号
	private BigDecimal deliverAccount; // 小件员在交款时的 现金账户余额
	private BigDecimal deliverPosAccount; // 小件员在交款时的 POS帐户余额
	private int deliverpayupapproved; // 小件员交款审核状态 0 未审核 1 已审核

	private BigDecimal deliverpayuparrearage; // 小件员欠款 小数点两位
	private BigDecimal deliverpayuparrearage_pos; // 小件员的用户POS刷卡欠款 小数点两位

	// 加字段
	private String checkremark; // 财务审核备注
	private String payupaddress; // 交款地址

	private String updatetime; // 修改订单的时间，代表本次归班中的订单最后一次修改时间，任何本次归班中的订单更新都将更新这个时间

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public int getDeliverpayupapproved() {
		return deliverpayupapproved;
	}

	public String getDeliverpayupapprovedStr() {
		// 审核状态 0 未审核 1 已通过 2未通过

		for (DeliverPayupArrearageapprovedEnum de : DeliverPayupArrearageapprovedEnum.values()) {
			if (de.getValue() == deliverpayupapproved) {
				return de.getText();
			}
		}
		return "未审核";

	}

	public void setDeliverpayupapproved(int deliverpayupapproved) {
		this.deliverpayupapproved = deliverpayupapproved;
	}

	public int getDeliverpayuptype() {
		return deliverpayuptype;
	}

	public String getDeliverpayuptypeStr() {
		// 小件员交款类型 0非小件员交款 1网银（网银需要小票号） 2POS 3现金
		for (DeliverPayuptypeEnum dp : DeliverPayuptypeEnum.values()) {
			if (deliverpayuptype == dp.getValue()) {
				return dp.getText();
			}
		}
		return "现金";
	}

	public void setDeliverpayuptype(int deliverpayuptype) {
		this.deliverpayuptype = deliverpayuptype;
	}

	public BigDecimal getDeliverpayupamount() {
		return deliverpayupamount;
	}

	public void setDeliverpayupamount(BigDecimal deliverpayupamount) {
		this.deliverpayupamount = deliverpayupamount;
	}

	public String getDeliverpayupbanknum() {
		return deliverpayupbanknum;
	}

	public void setDeliverpayupbanknum(String deliverpayupbanknum) {
		this.deliverpayupbanknum = deliverpayupbanknum;
	}

	public BigDecimal getDeliverpayuparrearage() {
		return deliverpayuparrearage;
	}

	public void setDeliverpayuparrearage(BigDecimal deliverpayuparrearage) {
		this.deliverpayuparrearage = deliverpayuparrearage;
	}

	public BigDecimal getPayupamount_pos() {
		return payupamount_pos;
	}

	public void setPayupamount_pos(BigDecimal payupamount_pos) {
		this.payupamount_pos = payupamount_pos;
	}

	public long getDeliverealuser() {
		return deliverealuser;
	}

	public void setDeliverealuser(long deliverealuser) {
		this.deliverealuser = deliverealuser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public BigDecimal getPayupamount() {
		return payupamount;
	}

	public void setPayupamount(BigDecimal payupamount) {
		this.payupamount = payupamount;
	}

	public long getReceivedfeeuser() {
		return receivedfeeuser;
	}

	public void setReceivedfeeuser(long receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getPayupid() {
		return payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCheckremark() {
		return checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
	}

	public String getPayupaddress() {
		return payupaddress;
	}

	public void setPayupaddress(String payupaddress) {
		this.payupaddress = payupaddress;
	}

	public BigDecimal getDeliverpayupamount_pos() {
		return deliverpayupamount_pos;
	}

	public void setDeliverpayupamount_pos(BigDecimal deliverpayupamount_pos) {
		this.deliverpayupamount_pos = deliverpayupamount_pos;
	}

	public BigDecimal getDeliverpayuparrearage_pos() {
		return deliverpayuparrearage_pos;
	}

	public void setDeliverpayuparrearage_pos(BigDecimal deliverpayuparrearage_pos) {
		this.deliverpayuparrearage_pos = deliverpayuparrearage_pos;
	}

	public BigDecimal getDeliverAccount() {
		return deliverAccount;
	}

	public void setDeliverAccount(BigDecimal deliverAccount) {
		this.deliverAccount = deliverAccount;
	}

	public BigDecimal getDeliverPosAccount() {
		return deliverPosAccount;
	}

	public void setDeliverPosAccount(BigDecimal deliverPosAccount) {
		this.deliverPosAccount = deliverPosAccount;
	}

}
