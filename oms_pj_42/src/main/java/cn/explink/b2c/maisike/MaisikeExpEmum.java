package cn.explink.b2c.maisike;

public enum MaisikeExpEmum {
	Success("100", "成功"), WeiShouQuan("101", "未授权的请求"), CanShuCuowu("102", "参数错误"), DataValidateFailed("103", "数据验证失败"), RequestTimeOut("104", "请求超时"), CommitFailed("105", "提交失败"), ;

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

	private MaisikeExpEmum(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
}
