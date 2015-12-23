/**
 *
 */
package cn.explink.domain.express;

import cn.explink.domain.CwbOrder;

/**
 *
 * @author songkaojun 2015年10月10日
 */
public class CwbOrderForCombineQuery extends CwbOrder {

	/**
	 * 付款方式显示名称
	 *
	 */
	private String payMethodName;

	private String receiverCompany;

	/**
	 * 收件省份名称
	 */
	private String receiveProvinceName;

	/**
	 * 收件市名称
	 */
	private String receiveCityName;

	/**
	 * 发件人公司
	 */
	private String senderCompanyName;

	public String getPayMethodName() {
		return this.payMethodName;
	}

	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
	}

	public String getReceiverCompany() {
		return this.receiverCompany;
	}

	public void setReceiverCompany(String receiverCompany) {
		this.receiverCompany = receiverCompany;
	}

	public String getReceiveProvinceName() {
		return this.receiveProvinceName;
	}

	public void setReceiveProvinceName(String receiveProvinceName) {
		this.receiveProvinceName = receiveProvinceName;
	}

	public String getReceiveCityName() {
		return this.receiveCityName;
	}

	public void setReceiveCityName(String receiveCityName) {
		this.receiveCityName = receiveCityName;
	}

	public String getSenderCompanyName() {
		return this.senderCompanyName;
	}

	public void setSenderCompanyName(String senderCompanyName) {
		this.senderCompanyName = senderCompanyName;
	}

}
