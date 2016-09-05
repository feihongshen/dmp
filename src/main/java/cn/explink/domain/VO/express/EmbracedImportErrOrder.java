package cn.explink.domain.VO.express;

import java.io.Serializable;

public class EmbracedImportErrOrder implements Serializable {
	private String orderNo;
	private String errMsg;

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
