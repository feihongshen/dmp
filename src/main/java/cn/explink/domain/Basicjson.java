/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class Basicjson {
	private String showflag;
	private BigDecimal PFfee;
	private List<PFbasic> PFfees;
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
	public List<PFbasic> getPFfees() {
		return this.PFfees;
	}
	/**
	 * @param pFfees the pFfees to set
	 */
	public void setPFfees(List<PFbasic> pFfees) {
		this.PFfees = pFfees;
	}

}
