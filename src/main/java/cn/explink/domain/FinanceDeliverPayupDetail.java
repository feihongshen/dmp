package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum;

public class FinanceDeliverPayupDetail {
	private long id;
	private long deliverealuser;
	private BigDecimal payupamount;
	private BigDecimal deliverpayupamount;
	private BigDecimal deliverAccount;
	private BigDecimal deliverpayuparrearage;
	private BigDecimal payupamount_pos;
	private BigDecimal deliverpayupamount_pos;
	private BigDecimal deliverPosAccount;
	private BigDecimal deliverpayuparrearage_pos;
	private long gcaid;
	private long audituserid;
	private String credate;
	private int type;
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverealuser() {
		return deliverealuser;
	}

	public void setDeliverealuser(long deliverealuser) {
		this.deliverealuser = deliverealuser;
	}

	public BigDecimal getPayupamount() {
		return payupamount;
	}

	public void setPayupamount(BigDecimal payupamount) {
		this.payupamount = payupamount;
	}

	public BigDecimal getDeliverpayupamount() {
		return deliverpayupamount;
	}

	public void setDeliverpayupamount(BigDecimal deliverpayupamount) {
		this.deliverpayupamount = deliverpayupamount;
	}

	public BigDecimal getDeliverAccount() {
		return deliverAccount;
	}

	public void setDeliverAccount(BigDecimal deliverAccount) {
		this.deliverAccount = deliverAccount;
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

	public BigDecimal getDeliverpayupamount_pos() {
		return deliverpayupamount_pos;
	}

	public void setDeliverpayupamount_pos(BigDecimal deliverpayupamount_pos) {
		this.deliverpayupamount_pos = deliverpayupamount_pos;
	}

	public BigDecimal getDeliverPosAccount() {
		return deliverPosAccount;
	}

	public void setDeliverPosAccount(BigDecimal deliverPosAccount) {
		this.deliverPosAccount = deliverPosAccount;
	}

	public BigDecimal getDeliverpayuparrearage_pos() {
		return deliverpayuparrearage_pos;
	}

	public void setDeliverpayuparrearage_pos(BigDecimal deliverpayuparrearage_pos) {
		this.deliverpayuparrearage_pos = deliverpayuparrearage_pos;
	}

	public long getGcaid() {
		return gcaid;
	}

	public void setGcaid(long gcaid) {
		this.gcaid = gcaid;
	}

	public long getAudituserid() {
		return audituserid;
	}

	public void setAudituserid(long audituserid) {
		this.audituserid = audituserid;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

	public int getType() {
		return type;
	}

	public String getTypeName() {
		for (FinanceDeliverPayUpDetailTypeEnum typeEnum : FinanceDeliverPayUpDetailTypeEnum.values()) {
			if (typeEnum.getValue() == type) {
				return typeEnum.getText();
			}
		}
		return "-";

	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		return "FinanceDeliverPayupDetail [id=" + id + ", deliverealuser=" + deliverealuser + ", payupamount=" + payupamount + ", deliverpayupamount=" + deliverpayupamount + ", deliverAccount="
				+ deliverAccount + ", deliverpayuparrearage=" + deliverpayuparrearage + ", payupamount_pos=" + payupamount_pos + ", deliverpayupamount_pos=" + deliverpayupamount_pos
				+ ", deliverPosAccount=" + deliverPosAccount + ", deliverpayuparrearage_pos=" + deliverpayuparrearage_pos + ", gcaid=" + gcaid + ", audituserid=" + audituserid + ", credate="
				+ credate + ", type=" + type + ", remark=" + remark + "]";
	}

}