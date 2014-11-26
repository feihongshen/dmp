package cn.explink.pos.alipay;

/**
 * 判断alipay的业务 逻辑并返回相应的异常
 * 
 * @author Administrator
 *
 */
public class AlipayException extends RuntimeException {
	private AliPayExptMessageEnum error;

	public AlipayException(AliPayExptMessageEnum error) {
		super(error.getResp_msg());
		this.error = error;
	}

	public AliPayExptMessageEnum getError() {
		return error;
	}

}
