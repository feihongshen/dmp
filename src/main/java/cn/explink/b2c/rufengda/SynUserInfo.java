package cn.explink.b2c.rufengda;

/**
 * 订单信息同步实体类
 * 
 * @author Administrator
 *
 */
public class SynUserInfo {

	private String rps_Name; // 用户名
	private String rps_Code; // 编码
	private String rps_IdentifyCode; // 身份证
	private String rps_EmailAddress; // 邮箱地址
	private String rps_Sex;
	private String rps_Phone; // 电话

	public String getRps_Phone() {
		return rps_Phone;
	}

	public void setRps_Phone(String rpsPhone) {
		rps_Phone = rpsPhone;
	}

	public String getRps_Name() {
		return rps_Name;
	}

	public void setRps_Name(String rps_Name) {
		this.rps_Name = rps_Name;
	}

	public String getRps_Code() {
		return rps_Code;
	}

	public void setRps_Code(String rps_Code) {
		this.rps_Code = rps_Code;
	}

	public String getRps_IdentifyCode() {
		return rps_IdentifyCode;
	}

	public void setRps_IdentifyCode(String rps_IdentifyCode) {
		this.rps_IdentifyCode = rps_IdentifyCode;
	}

	public String getRps_EmailAddress() {
		return rps_EmailAddress;
	}

	public void setRps_EmailAddress(String rps_EmailAddress) {
		this.rps_EmailAddress = rps_EmailAddress;
	}

	public String getRps_Sex() {
		return rps_Sex;
	}

	public void setRps_Sex(String rps_Sex) {
		this.rps_Sex = rps_Sex;
	}

}
