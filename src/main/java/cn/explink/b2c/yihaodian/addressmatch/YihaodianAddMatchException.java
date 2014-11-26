package cn.explink.b2c.yihaodian.addressmatch;

/**
 * 自定义一号店异常类
 * 
 * @author Administrator
 *
 */
public class YihaodianAddMatchException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private YihaodianAddEmum error;
	private String content;

	public YihaodianAddMatchException(YihaodianAddEmum error, String content) {
		super(error.getErrMsg());
		this.error = error;
		this.content = content;
	}

	public YihaodianAddEmum getError() {
		return error;
	}

	public String getContent() {
		return content;
	}
}
