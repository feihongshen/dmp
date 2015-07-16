/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFoverarea {
	private long id;
	private BigDecimal overareafee;
	private int typeid;
	private long pfruleid;
	private int tabid;
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
	 * @return the overareafee
	 */
	public BigDecimal getOverareafee() {
		return this.overareafee;
	}
	/**
	 * @param overareafee the overareafee to set
	 */
	public void setOverareafee(BigDecimal overareafee) {
		this.overareafee = overareafee;
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


}
