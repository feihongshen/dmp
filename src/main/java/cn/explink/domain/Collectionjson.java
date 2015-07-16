/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Collectionjson {
private String showflag;
private BigDecimal PFfee;
private List<PFcollection> PFfees;
/**
 * @return the showflag
 */
public String getShowflag() {
	return this.showflag;
}
/**
 * @param showflag the showflag to set
 */
public void setShowflag(String showflag) {
	this.showflag = showflag;
}
/**
 * @return the pFfee
 */
public BigDecimal getPFfee() {
	return this.PFfee;
}
/**
 * @param pFfee the pFfee to set
 */
public void setPFfee(BigDecimal pFfee) {
	this.PFfee = pFfee;
}
/**
 * @return the pFfees
 */
public List<PFcollection> getPFfees() {
	return this.PFfees;
}
/**
 * @param pFfees the pFfees to set
 */
public void setPFfees(List<PFcollection> pFfees) {
	this.PFfees = pFfees;
}

}
