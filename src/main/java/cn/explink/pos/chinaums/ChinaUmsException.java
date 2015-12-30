package cn.explink.pos.chinaums;

/**
 * 判断alipay的业务 逻辑并返回相应的异常
 * 
 * @author Administrator
 *
 */
public class ChinaUmsException extends RuntimeException {
	private ChinaUmsExptMessageEnum error;

	public ChinaUmsException(ChinaUmsExptMessageEnum error) {
		super(error.getResp_msg());
		this.error = error;
	}

	public ChinaUmsExptMessageEnum getError() {
		return error;
	}

}
