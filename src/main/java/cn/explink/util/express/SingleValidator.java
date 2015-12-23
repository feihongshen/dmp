package cn.explink.util.express;

/**
 * 数据库中的校验规则
 * 
 * @author jiangyu 2015年6月4日
 *
 */
public class SingleValidator {
	/**
	 * 类型
	 */
	String type;
	/**
	 * 规则
	 */
	String rule;
	/**
	 * 错误提示信息
	 */
	String errorMsg;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}