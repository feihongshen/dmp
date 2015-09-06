/**
 *
 */
package cn.explink.b2c.weisuda.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Administrator
 *
 */
public class Good {
	private String code;
	private int fetch_num;
	private int special_num;
	private String remark;
	/**
	 * @return the code
	 */
	@XmlElement(name = "code")
	public String getCode() {
		return this.code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the fetch_num
	 */
	@XmlElement(name = "fetch_num")
	public int getFetch_num() {
		return this.fetch_num;
	}
	/**
	 * @param fetch_num the fetch_num to set
	 */
	public void setFetch_num(int fetch_num) {
		this.fetch_num = fetch_num;
	}
	/**
	 * @return the special_num
	 */
	@XmlElement(name = "special_num")
	public int getSpecial_num() {
		return this.special_num;
	}
	/**
	 * @param special_num the special_num to set
	 */
	public void setSpecial_num(int special_num) {
		this.special_num = special_num;
	}
	/**
	 * @return the remark
	 */
	@XmlElement(name = "remark")
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
