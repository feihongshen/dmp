/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFbusiness {
	private long id;
	private BigDecimal subsidyfee;
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
	 * @return the subsidyfee
	 */
	public BigDecimal getSubsidyfee() {
		return this.subsidyfee;
	}
	/**
	 * @param subsidyfee the subsidyfee to set
	 */
	public void setSubsidyfee(BigDecimal subsidyfee) {
		this.subsidyfee = subsidyfee;
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
