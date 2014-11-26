package cn.explink.exception;

import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;

public class CwbException extends ExplinkException {
	private String cwb;
	private long flowordertye;

	public CwbException(String cwb, long flowordertye, ExceptionCwbErrorTypeEnum error, Object... argarray) {
		super(error, argarray);
		this.cwb = cwb;
		this.flowordertye = flowordertye;
	}

	public CwbException(String cwb, ExceptionCwbErrorTypeEnum error) {
		super(error);
		this.cwb = cwb;
	}

	public CwbException(String cwb, long flowordertye, ExceptionCwbErrorTypeEnum error) {
		super(error);
		this.cwb = cwb;
		this.flowordertye = flowordertye;
	}

	public CwbException(String cwb, long flowordertye, String error) {
		super(error);
		this.cwb = cwb;
		this.flowordertye = flowordertye;
	}

	public ExceptionCwbErrorTypeEnum getError() {
		return error;
	}

	public String getCwb() {
		return cwb;
	}

	public long getFlowordertye() {
		return flowordertye;
	}

}
