package cn.explink.util.express;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
/**
 * 导入字段的校验
 * @author jiangyu 2015年6月4日
 *
 */
public class FieldValidator {
	/**
	 * 存放校验规则的集合
	 */
	private List<SingleValidator> list = new ArrayList<SingleValidator>();
	
	public FieldValidator() {}
	
	/**
	 * 解析校验规则
	 * @param jsonStr
	 */
	public FieldValidator(String jsonStr) {
		if (StringUtils.isNotBlank(jsonStr)) {
			list = JSONArray.toList(JSONArray.fromObject(jsonStr));
//			list = JSON.parseArray(jsonStr, SingleValidator.class);
		}
	}
	
	/**
	 * 注册校验规则
	 * @param singleValidators
	 */
	public FieldValidator registerValidators(SingleValidator... singleValidators){
		if (singleValidators.length>0) {
			for (SingleValidator singleValidator : singleValidators) {
				this.list.add(singleValidator);
			}
		}
		return this;
	}
	/**
	 * 注册校验规则
	 * @param singleValidator
	 * @return
	 */
	public FieldValidator registerValidator(SingleValidator singleValidator){
		this.list.add(singleValidator);
		return this;
	}
	
	/**
	 * 字段的校验
	 * @param str
	 * @throws Exception
	 */
	public void validator(String str) throws Exception{
		if (list == null || list.isEmpty()) {
			return;
		}
		for (SingleValidator validatorRule : list) {
			//非空
			if (ValidatorTypeEnum.NOTNULL.equals(parseEnum(validatorRule.type))) {
				if (StringUtils.isBlank(str)) {
					throw new RuntimeException(validatorRule.errorMsg);
				}
			}
			//正则
			if (ValidatorTypeEnum.REGEX.equals(parseEnum(validatorRule.type))) {
				if (StringUtils.isNotBlank(str)) {
					if (!Pattern.matches(validatorRule.rule, str)) {
						throw new RuntimeException(validatorRule.errorMsg);
					}
				}

			}
			//业务逻辑
			if (ValidatorTypeEnum.LOGICAL.equals(parseEnum(validatorRule.type))) {
				
			}
			
			//最大值
			if (ValidatorTypeEnum.MAX.equals(parseEnum(validatorRule.type))) {
				if (StringUtils.isNotBlank(str)) {
					try {
						float max = NumberUtils.toFloat(validatorRule.rule);
						float val = NumberUtils.toFloat(str);
						if (max < val) {
							throw new RuntimeException(validatorRule.errorMsg);
						}
					} catch (Exception e) {
						throw new RuntimeException(validatorRule.errorMsg);
					}
				}
			}
			//最小值
			if (ValidatorTypeEnum.MIN.equals(parseEnum(validatorRule.type))) {
				if (StringUtils.isNotBlank(str)) {
					try {
						float min = NumberUtils.toFloat(validatorRule.rule);
						float val = NumberUtils.toFloat(str);
						if (min > val) {
							throw new RuntimeException(validatorRule.errorMsg);
						}
					} catch (Exception e) {
						throw new RuntimeException(validatorRule.errorMsg);
					}
				}
			}
			
		}
	}

	public List<SingleValidator> getList() {
		return list;
	}

	public void setList(List<SingleValidator> list) {
		this.list = list;
	}
	/**
	 * 校验枚举的解析
	 * @param type
	 * @return
	 */
	ValidatorTypeEnum parseEnum(String type) {
		return Enum.valueOf(ValidatorTypeEnum.class, type);
	}
	
}
