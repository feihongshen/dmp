package cn.explink.controller;

import java.math.BigDecimal;

public class MonitorOrderDTO {
	// m.cwb, m.caramount,m.consigneename,m.sendcarname,
	// m.consigneemobile,m.emaildate,m.shiptime,m.branchname,u.realname

	String cwb;
	String consigneename;
	String consigneemobile;
	String sendcarname;
	String emaildate;
	String shiptime;
	BigDecimal caramount = BigDecimal.ZERO;
	String branchname = "";
	String realname = "";

	/**
	 * @return the cwb
	 */
	public String getCwb() {
		return cwb;
	}

	/**
	 * @param cwb
	 *            the cwb to set
	 */
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	/**
	 * @return the consigneename
	 */
	public String getConsigneename() {
		return consigneename;
	}

	/**
	 * @param consigneename
	 *            the consigneename to set
	 */
	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	/**
	 * @return the consigneemobile
	 */
	public String getConsigneemobile() {
		return consigneemobile;
	}

	/**
	 * @param consigneemobile
	 *            the consigneemobile to set
	 */
	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	/**
	 * @return the emaildate
	 */
	public String getEmaildate() {

		if (emaildate != null && !"".equals(emaildate) && emaildate.length() > 19) {
			emaildate = emaildate.substring(0, 19);
		}
		return emaildate == null ? "" : emaildate;
	}

	/**
	 * @return the sendcarname
	 */
	public String getSendcarname() {
		return sendcarname;
	}

	/**
	 * @param sendcarname
	 *            the sendcarname to set
	 */
	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	/**
	 * @param emaildate
	 *            the emaildate to set
	 */
	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	/**
	 * @return the shiptime
	 */
	public String getShiptime() {
		if (shiptime != null && !"".equals(shiptime) && shiptime.length() > 19) {
			shiptime = shiptime.substring(0, 19);
		}
		return shiptime == null ? "" : shiptime;
	}

	/**
	 * @param shiptime
	 *            the shiptime to set
	 */
	public void setShiptime(String shiptime) {
		this.shiptime = shiptime;
	}

	/**
	 * @return the caramount
	 */
	public BigDecimal getCaramount() {
		return caramount;
	}

	/**
	 * @param caramount
	 *            the caramount to set
	 */
	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	/**
	 * @return the branchname
	 */
	public String getBranchname() {
		return branchname == null ? "" : branchname;
	}

	/**
	 * @param branchname
	 *            the branchname to set
	 */
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname == null ? "" : realname;
	}

	/**
	 * @param realname
	 *            the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

}
