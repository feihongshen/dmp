/**
 * 
 */
package cn.explink.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kyle
 */
public class AjaxResult<T> {
	/**
	 * 是否成功
	 */
	private boolean result;
	
	/**
	 * 返回消息
	 */
	private String message;
	
	/**
	 * 返回数据
	 */
	private T data;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 *字段
	 * */
	private String field;
	
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 验证失败字段和错误信息
	 */
	private Map<String, List<String>> fieldErrors=new HashMap<String, List<String>>();

	public AjaxResult(boolean result, String message) {
		this.result = result;
		this.message = message;
	}

	/**
	 * @param result
	 * @param message
	 * @param fieldErrors
	 */
	public AjaxResult(boolean result, String message, Map<String, List<String>> fieldErrors) {
		super();
		this.result = result;
		this.message = message;
		this.fieldErrors = fieldErrors;
	}

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return this.result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param fieldErrors
	 *            the fieldErrors to set
	 */
	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	/**
	 * @return the fieldErrors
	 */
	public Map<String, List<String>> getFieldErrors() {
		return this.fieldErrors;
	}

}
