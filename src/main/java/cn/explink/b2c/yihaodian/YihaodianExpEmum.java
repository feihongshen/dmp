package cn.explink.b2c.yihaodian;

public enum YihaodianExpEmum {
	Success("01", "成功，处理结束，正常返回"), YongHuBuCunZai("02", "失败，用户不存在"), QianMingCuoWu("03", "失败，签名错误"), MultiInvoke("04", "失败，当日调用次数超限"), CanShuCuoWu("05", "失败，参数错误"), YeWuYiChang("06", "失败，业务异常"), XiTongYiChang(
			"07", "失败，系统异常"), ;

	private String errCode;
	private String errMsg;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private YihaodianExpEmum(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
}
