package cn.explink.util.exception;

/**
 * @Desc 校验异常信息类
 *
 */
public class ValidationExeception extends Exception {
	private static final long serialVersionUID = 7000209502999913863L;
	private String errCode;// 错误码
	private String message;// 相关描述

	public ValidationExeception() {
	}

	public ValidationExeception(String message) {
		this.message = message;
	}

	public ValidationExeception(String errCode, String message) {
		super(errCode + " : " + message);
		this.message = message;
		this.errCode = errCode;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.errCode + " : " + this.message;
	}
}
