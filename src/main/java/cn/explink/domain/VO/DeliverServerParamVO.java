package cn.explink.domain.VO;

public class DeliverServerParamVO {

	/**
	 * 接入公司code
	 */
	private String code="";
	/**
	 * 接入公司token
	 */
	private String token="";
	
	/**
	 * 配送公司code
	 */
	private String delivery_company_code;
	
	public String getDelivery_company_code() {
		return delivery_company_code;
	}
	public void setDelivery_company_code(String delivery_company_code) {
		this.delivery_company_code = delivery_company_code;
	}
	/**
	 * 派件通知url
	 */
	private String deliverServerPushUrl="";
	/**
	 * 派件员同步url
	 */
	private String deliverSyncPushUrl="";
	/**
	 * POS机刷卡通知App URL
	 */
	private String deliverPosSynUrl="";
	public String getDeliverPosSynUrl() {
		return deliverPosSynUrl;
	}
	public void setDeliverPosSynUrl(String deliverPosSynUrl) {
		this.deliverPosSynUrl = deliverPosSynUrl;
	}
	/**
	 * 唯一流水号
	 */
	private Integer tradeNum = Integer.valueOf(0);
	
	public Integer getTradeNum() {
		return tradeNum;
	}
	public void setTradeNum(Integer tradeNum) {
		this.tradeNum = tradeNum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeliverServerPushUrl() {
		return deliverServerPushUrl;
	}
	public void setDeliverServerPushUrl(String deliverServerPushUrl) {
		this.deliverServerPushUrl = deliverServerPushUrl;
	}
	public String getDeliverSyncPushUrl() {
		return deliverSyncPushUrl;
	}
	public void setDeliverSyncPushUrl(String deliverSyncPushUrl) {
		this.deliverSyncPushUrl = deliverSyncPushUrl;
	}
	
}
