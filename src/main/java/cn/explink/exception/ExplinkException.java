package cn.explink.exception;

import java.text.MessageFormat;

import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;

public class ExplinkException extends RuntimeException {
	ExceptionCwbErrorTypeEnum error;

	public ExplinkException(String msg) {
		super(msg);
	}

	public ExplinkException(ExceptionCwbErrorTypeEnum error, Object... argarray) {
		super(MessageFormat.format(error.getText(), argarray));
		this.error = error;
	}

	public ExplinkException(ExceptionCwbErrorTypeEnum error) {
		super(error.getText());
		this.error = error;
	}

	public ExceptionCwbErrorTypeEnum getError() {
		return error;
	}

}
