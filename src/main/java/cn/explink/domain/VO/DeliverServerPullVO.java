package cn.explink.domain.VO;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import cn.explink.util.StringUtil;

/**
 * 派件服务APP派件结果VO
 * 
 * @author gaoll
 * 
 */
public class DeliverServerPullVO {

		
	private String sign = "";
	private String code = "";
	private String outer_trade_no = "";
	private String mail_num = "";
	private Integer payment_means;
	private Integer delivery_result;
	private String message = "";
	private String unid = "";
	private String time_stamp = "";
	private Integer cod = Integer.valueOf(0);
	private Integer postrace = Integer.valueOf(1);
	private Integer sign_code = Integer.valueOf(1);
	private String fail_code = "";
	
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
	public String getOuter_trade_no() {
		return outer_trade_no;
	}
	public void setOuter_trade_no(String outer_trade_no) {
		this.outer_trade_no = outer_trade_no;
	}
	public String getMail_num() {
		return mail_num;
	}
	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}
	public Integer getPayment_means() {
		return payment_means;
	}
	public void setPayment_means(Integer payment_means) {
		this.payment_means = payment_means;
	}
	public Integer getDelivery_result() {
		return delivery_result;
	}
	public void setDelivery_result(Integer delivery_result) {
		this.delivery_result = delivery_result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
	public Integer getCod() {
		return cod;
	}
	public void setCod(Integer cod) {
		this.cod = cod;
	}
	public Integer getPostrace() {
		return postrace;
	}
	public void setPostrace(Integer postrace) {
		this.postrace = postrace;
	}
	public Integer getSign_code() {
		return sign_code;
	}
	public void setSign_code(Integer sign_code) {
		this.sign_code = sign_code;
	}
	public String getFail_code() {
		return fail_code;
	}
	public void setFail_code(String fail_code) {
		this.fail_code = fail_code;
	}
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	
	/**
	 * 获取待签名字符串
	 * @return
	 * @throws Exception
	 */
	public String[] buildSignStr(){
		String[] resultStrs = new String[2]; 
		//待签名字段（可调整）
		String[] simpleFieldArr = new String[]{
				"outer_trade_no"
				,"mail_num"
				,"payment_means"
				,"delivery_result" 
				,"sign_code"
				,"fail_code"
				,"message" 
				,"postrace"
				,"cod"
				,"time_stamp"
				,"unid"
			};
		Arrays.sort(simpleFieldArr);
		//拼装待签名字符串
		StringBuilder sb = new StringBuilder();
		StringBuilder sbConvertToNull = new StringBuilder();
		for (String fieldName : simpleFieldArr) {
			String getMethos = "get" + this.buildFieldMethod(fieldName);
			String fieldValue = "";
			String fieldNameNull = "";
			try {
				fieldValue = null == DeliverServerPullVO.class.getDeclaredMethod(getMethos).invoke(this)?"":DeliverServerPullVO.class.getDeclaredMethod(getMethos).invoke(this).toString();
				fieldNameNull = null == DeliverServerPullVO.class.getDeclaredMethod(getMethos).invoke(this)?"null":DeliverServerPullVO.class.getDeclaredMethod(getMethos).invoke(this).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append(fieldName).append("=").append(fieldValue).append("&");
			sbConvertToNull.append(fieldName).append("=").append(fieldNameNull).append("&");
		}
		resultStrs[0] = sb.substring(0, sb.length()-1);
		resultStrs[1] = sbConvertToNull.substring(0, sbConvertToNull.length()-1);
		return resultStrs;
	}

	/**
	 * 根据字段名，获取对应get方法
	 * @return
	 */
	private String buildFieldMethod(String fieldName){
		 if (StringUtils.isEmpty(fieldName)) {
	            return null;
        }
        String firstChar = StringUtils.substring(fieldName, 0, 1).toUpperCase();
        return firstChar + StringUtils.substring(fieldName, 1);
	}
	@Override
	public String toString() {
		return "DeliverServerPullVO [sign=" + sign + ", code=" + code
				+ ", outer_trade_no=" + outer_trade_no + ", mail_num="
				+ mail_num + ", payment_means=" + payment_means
				+ ", delivery_result=" + delivery_result + ", message="
				+ message + ", unid=" + unid + ", time_stamp=" + time_stamp
				+ ", cod=" + cod + ", postrace=" + postrace + ", sign_code="
				+ sign_code + ", fail_code=" + fail_code + "]";
	}
	
}
