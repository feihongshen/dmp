/**
 *
 */
package cn.explink.domain.express;

import cn.explink.domain.CwbOrder;

/**
 * @author songkaojun 2015年8月21日
 */
public class CwbOrderForCombine extends CwbOrder {

	/**
	 * 付款方式显示名称
	 *
	 */
	private String payMethodName;

	/**
	 * 省名称
	 */
	private String provinceName;

	/**
	 * 市名称
	 */
	private String cityName;

	public String getPayMethodName() {
		return this.payMethodName;
	}

	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
