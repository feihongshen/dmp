package cn.explink.exception;

import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;

/**
 * 快递导入应收账单的时候用到的异常
 * @author jiangyu 2015年8月21日
 *
 */
public class ExpressException extends ExplinkException{
	
	private String orderNo;
	
	public ExpressException(ExceptionCwbErrorTypeEnum error) {
		super(error);
	}
	
	public ExpressException(String orderNo,ExceptionCwbErrorTypeEnum error, Object... argarray) {
		super(error, argarray);
		this.orderNo = orderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}
	
	public ExceptionCwbErrorTypeEnum getError() {
		return error;
	}
	
}
