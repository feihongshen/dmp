package cn.explink.domain.VO;

/**
 * 反馈POS通知APP 
 * @author Administrator
 *
 */
public class DeliveryPosNotifyVO {
	
	private String sign;
	private String code;
	private String delivery_company_code; //落地配企业代码
	private String mail_num; //唯一单号标识
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDelivery_company_code() {
		return delivery_company_code;
	}
	public void setDelivery_company_code(String delivery_company_code) {
		this.delivery_company_code = delivery_company_code;
	}
	public String getMail_num() {
		return mail_num;
	}
	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}

	
	
}
