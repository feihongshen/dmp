/**
 *
 */
package cn.explink.domain;

/**
 * @author Administrator
 *
 */
public class Area {
	private long id;
	private String city;
	private String area;
	private int isareafee;
	private int isoverbig;
	private int isoverweight;
	private String remark;

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return this.area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the isareafee
	 */
	public int getIsareafee() {
		return this.isareafee;
	}

	/**
	 * @param isareafee
	 *            the isareafee to set
	 */
	public void setIsareafee(int isareafee) {
		this.isareafee = isareafee;
	}

	/**
	 * @return the isoverbig
	 */
	public int getIsoverbig() {
		return this.isoverbig;
	}

	/**
	 * @param isoverbig
	 *            the isoverbig to set
	 */
	public void setIsoverbig(int isoverbig) {
		this.isoverbig = isoverbig;
	}

	/**
	 * @return the isoverweight
	 */
	public int getIsoverweight() {
		return this.isoverweight;
	}

	/**
	 * @param isoverweight
	 *            the isoverweight to set
	 */
	public void setIsoverweight(int isoverweight) {
		this.isoverweight = isoverweight;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
