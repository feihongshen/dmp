package cn.explink.domain.VO.express;
/**
 * 用于组织前台的select
 * @author jiangyu 2015年8月13日
 *
 */
public class SelectReturnVO {
	/**
	 * option -- value
	 */
	private Long hiddenValue;
	/**
	 * option -- text
	 */
	private String displayValue;

	public SelectReturnVO() {
	}

	public Long getHiddenValue() {
		return hiddenValue;
	}

	public void setHiddenValue(Long hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public SelectReturnVO(Long hiddenValue, String displayValue) {
		this.hiddenValue = hiddenValue;
		this.displayValue = displayValue;
	}

}
