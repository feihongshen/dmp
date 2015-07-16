/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFinsertion {
	private long id;
	private long mincount;
	private long maxcount;
	private BigDecimal insertionfee;
	private int typeid;
	private long pfruleid;
	private String remark;
	private int tabid;
	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
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
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the mincount
	 */
	public long getMincount() {
		return this.mincount;
	}
	/**
	 * @param mincount the mincount to set
	 */
	public void setMincount(long mincount) {
		this.mincount = mincount;
	}
	/**
	 * @return the maxcount
	 */
	public long getMaxcount() {
		return this.maxcount;
	}
	/**
	 * @param maxcount the maxcount to set
	 */
	public void setMaxcount(long maxcount) {
		this.maxcount = maxcount;
	}
	/**
	 * @return the insertionfee
	 */
	public BigDecimal getInsertionfee() {
		return this.insertionfee;
	}
	/**
	 * @param insertionfee the insertionfee to set
	 */
	public void setInsertionfee(BigDecimal insertionfee) {
		this.insertionfee = insertionfee;
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

}
