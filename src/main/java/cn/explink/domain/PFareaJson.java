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
public class PFareaJson {
	private long id;
	private long areaid;
	private String areaname;
	private BigDecimal areafee;
	private int overbigflag;
	private List<PFoverbig> overbig;
	private List<PFoverweight> overweight;
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
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the areaid
	 */
	public long getAreaid() {
		return this.areaid;
	}
	/**
	 * @param areaid the areaid to set
	 */
	public void setAreaid(long areaid) {
		this.areaid = areaid;
	}
	/**
	 * @return the areaname
	 */
	public String getAreaname() {
		return this.areaname;
	}
	/**
	 * @param areaname the areaname to set
	 */
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	/**
	 * @return the areafee
	 */
	public BigDecimal getAreafee() {
		return this.areafee;
	}
	/**
	 * @param areafee the areafee to set
	 */
	public void setAreafee(BigDecimal areafee) {
		this.areafee = areafee;
	}
	/**
	 * @return the overbigflag
	 */
	public int getOverbigflag() {
		return this.overbigflag;
	}
	/**
	 * @param overbigflag the overbigflag to set
	 */
	public void setOverbigflag(int overbigflag) {
		this.overbigflag = overbigflag;
	}
	/**
	 * @return the overbig
	 */
	public List<PFoverbig> getOverbig() {
		return this.overbig;
	}
	/**
	 * @param overbig the overbig to set
	 */
	public void setOverbig(List<PFoverbig> overbig) {
		this.overbig = overbig;
	}
	/**
	 * @return the overweight
	 */
	public List<PFoverweight> getOverweight() {
		return this.overweight;
	}
	/**
	 * @param overweight the overweight to set
	 */
	public void setOverweight(List<PFoverweight> overweight) {
		this.overweight = overweight;
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

}
