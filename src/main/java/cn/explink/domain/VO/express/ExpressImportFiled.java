package cn.explink.domain.VO.express;

/**
 * 导入字段的帮助类
 * @author jiangyu 2015年8月21日
 *
 */
public class ExpressImportFiled {
	/**
	 * 字段名称
	 */
	private String filedName;

	/**
	 * 字段编码
	 */
	private String filedCode;

	/**
	 * 字段类型
	 */
	private String filedType;

	/**
	 * 校验规则
	 */
	private String validateRule;

	/**
	 * 额外的规则
	 */
	private String extraRule;
	
	public ExpressImportFiled() {
		// TODO Auto-generated constructor stub
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getFiledCode() {
		return filedCode;
	}

	public void setFiledCode(String filedCode) {
		this.filedCode = filedCode;
	}

	public String getFiledType() {
		return filedType;
	}

	public void setFiledType(String filedType) {
		this.filedType = filedType;
	}

	public String getValidateRule() {
		return validateRule;
	}

	public void setValidateRule(String validateRule) {
		this.validateRule = validateRule;
	}

	public String getExtraRule() {
		return extraRule;
	}

	public void setExtraRule(String extraRule) {
		this.extraRule = extraRule;
	}

	public ExpressImportFiled(String filedName, String filedCode, String filedType, String validateRule, String extraRule) {
		super();
		this.filedName = filedName;
		this.filedCode = filedCode;
		this.filedType = filedType;
		this.validateRule = validateRule;
		this.extraRule = extraRule;
	}
	
}
