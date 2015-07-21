/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFbasic {
private long id;
private long customerid;
private BigDecimal basicPFfee;
private String remark;
private int typeid;
private int tabid;
private long pfruleid;
private int showflag;
/**
 * @return the id
 */
public long getId() {
	return this.id;
}
/**
 * @param id the id to set
 */
public void setId(long id) {
	this.id = id;
}
/**
 * @return the customerid
 */
public long getCustomerid() {
	return this.customerid;
}
/**
 * @param customerid the customerid to set
 */
public void setCustomerid(long customerid) {
	this.customerid = customerid;
}
/**
 * @return the basicPFfee
 */
public BigDecimal getBasicPFfee() {
	return this.basicPFfee;
}
/**
 * @param basicPFfee the basicPFfee to set
 */
public void setBasicPFfee(BigDecimal basicPFfee) {
	this.basicPFfee = basicPFfee;
}
/**
 * @return the remark
 */
public String getRemark() {
	return this.remark;
}
/**
 * @param remark the remark to set
 */
public void setRemark(String remark) {
	this.remark = remark;
}
/**
 * @return the typeid
 */
public int getTypeid() {
	return this.typeid;
}
/**
 * @param typeid the typeid to set
 */
public void setTypeid(int typeid) {
	this.typeid = typeid;
}
/**
 * @return the pfruleid
 */
public long getPfruleid() {
	return this.pfruleid;
}
/**
 * @param pfruleid the pfruleid to set
 */
public void setPfruleid(long pfruleid) {
	this.pfruleid = pfruleid;
}
/**
 * @return the tabid
 */
public int getTabid() {
	return this.tabid;
}
/**
 * @param tabid the tabid to set
 */
public void setTabid(int tabid) {
	this.tabid = tabid;
}
/**
 * @return the showflag
 */
public int getShowflag() {
	return this.showflag;
}
/**
 * @param showflag the showflag to set
 */
public void setShowflag(int showflag) {
	this.showflag = showflag;
}

}
