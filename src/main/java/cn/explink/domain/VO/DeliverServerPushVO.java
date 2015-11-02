package cn.explink.domain.VO;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 派件服务APP推送接口VO
 * 
 * @author gaoll
 *
 */
public class DeliverServerPushVO {

	private String sign = "";
	private String code = "";
	private String outer_trade_no = "";
	private String unid = "";
	private String merchant_code = "";
	private String delivery_company_code = "";
	private String mail_num = "";
	private String delivery_type = "";
	private String s_company = ""; 
	private String s_contact = ""; 
	private String s_tel = ""; 
	private String s_mobile = ""; 
	private String s_address = "";
	private String s_province = ""; 
	private String s_city = ""; 
	private String s_district = "";
	private String s_code = ""; 
	private String d_company = ""; 
	private String d_contact = ""; 
	private String d_tel = ""; 
	private String d_mobile = ""; 
	private String d_address = "";
	private String d_province = ""; 
	private String d_city = ""; 
	private String d_district = "";
	private String d_code = ""; 
	private String mch_order_num = "";
	private String mch_package_num = "";
	private Integer goods_num = Integer.valueOf(1);
	private Integer goods_fee = Integer.valueOf(0);
	private String remark = ""; 
	private Integer num = Integer.valueOf(1);
	private String name = "";
	private List<GoodInfoVO> goods_info;
	
	public String getOuter_trade_no() {
		return outer_trade_no;
	}
	public void setOuter_trade_no(String outer_trade_no) {
		this.outer_trade_no = outer_trade_no;
	}
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	public String getMerchant_code() {
		return merchant_code;
	}
	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
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
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getS_company() {
		return s_company;
	}
	public void setS_company(String s_company) {
		this.s_company = s_company;
	}
	public String getS_contact() {
		return s_contact;
	}
	public void setS_contact(String s_contact) {
		this.s_contact = s_contact;
	}
	public String getS_tel() {
		return s_tel;
	}
	public void setS_tel(String s_tel) {
		this.s_tel = s_tel;
	}
	public String getS_mobile() {
		return s_mobile;
	}
	public void setS_mobile(String s_mobile) {
		this.s_mobile = s_mobile;
	}
	public String getS_address() {
		return s_address;
	}
	public void setS_address(String s_address) {
		this.s_address = s_address;
	}
	public String getS_province() {
		return s_province;
	}
	public void setS_province(String s_province) {
		this.s_province = s_province;
	}
	public String getS_city() {
		return s_city;
	}
	public void setS_city(String s_city) {
		this.s_city = s_city;
	}
	public String getS_district() {
		return s_district;
	}
	public void setS_district(String s_district) {
		this.s_district = s_district;
	}
	public String getS_code() {
		return s_code;
	}
	public void setS_code(String s_code) {
		this.s_code = s_code;
	}
	public String getD_company() {
		return d_company;
	}
	public void setD_company(String d_company) {
		this.d_company = d_company;
	}
	public String getD_contact() {
		return d_contact;
	}
	public void setD_contact(String d_contact) {
		this.d_contact = d_contact;
	}
	public String getD_tel() {
		return d_tel;
	}
	public void setD_tel(String d_tel) {
		this.d_tel = d_tel;
	}
	public String getD_mobile() {
		return d_mobile;
	}
	public void setD_mobile(String d_mobile) {
		this.d_mobile = d_mobile;
	}
	public String getD_address() {
		return d_address;
	}
	public void setD_address(String d_address) {
		this.d_address = d_address;
	}
	public String getD_province() {
		return d_province;
	}
	public void setD_province(String d_province) {
		this.d_province = d_province;
	}
	public String getD_city() {
		return d_city;
	}
	public void setD_city(String d_city) {
		this.d_city = d_city;
	}
	public String getD_district() {
		return d_district;
	}
	public void setD_district(String d_district) {
		this.d_district = d_district;
	}
	public String getD_code() {
		return d_code;
	}
	public void setD_code(String d_code) {
		this.d_code = d_code;
	}
	public String getMch_order_num() {
		return mch_order_num;
	}
	public void setMch_order_num(String mch_order_num) {
		this.mch_order_num = mch_order_num;
	}
	public String getMch_package_num() {
		return mch_package_num;
	}
	public void setMch_package_num(String mch_package_num) {
		this.mch_package_num = mch_package_num;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public Integer getGoods_fee() {
		return goods_fee;
	}
	public void setGoods_fee(Integer goods_fee) {
		this.goods_fee = goods_fee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public List<GoodInfoVO> getGoods_info() {
		return goods_info;
	}
	public void setGoods_info(List<GoodInfoVO> goods_info) {
		this.goods_info = goods_info;
	}
	
	/**
	 * 获取待签名字符串
	 * @return
	 * @throws Exception
	 */
	public String buildSignStr(){
		//待签名字段（可调整）
		String[] simpleFieldArr = new String[]{
			"outer_trade_no"
			,"unid"
			,"merchant_code"
			,"delivery_company_code"
			,"mail_num"
			,"delivery_type"
			,"s_company" 
			,"s_contact" 
			,"s_tel" 
			,"s_mobile" 
			,"s_address"
			,"s_province" 
			,"s_city" 
			,"s_district"
			,"s_code" 
			,"d_company" 
			,"d_contact" 
			,"d_tel" 
			,"d_mobile" 
			,"d_address"
			,"d_province" 
			,"d_city" 
			,"d_district"
			,"d_code" 
			,"mch_order_num"
			,"mch_package_num"
			,"goods_num"
			,"goods_fee"
			,"remark"
			};
		Arrays.sort(simpleFieldArr);
		//拼装待签名字符串
		StringBuilder sb = new StringBuilder();
		for (String fieldName : simpleFieldArr) {
			String getMethos = "get" + this.buildFieldMethod(fieldName);
			String fieldValue = null;
			try {
				fieldValue = null == DeliverServerPushVO.class.getDeclaredMethod(getMethos).invoke(this)?"":DeliverServerPushVO.class.getDeclaredMethod(getMethos).invoke(this).toString();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("拼装待签名字符串异常:" + this.mail_num + e.getMessage());
			}
			sb.append(fieldName).append("=").append(fieldValue).append("&");
		}
		return sb.substring(0, sb.length()-1);
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
}
