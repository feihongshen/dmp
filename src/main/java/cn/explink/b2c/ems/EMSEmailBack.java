package cn.explink.b2c.ems;

/**
 * EMS轨迹返回对象
 * @author huan.zhou
 */
public class EMSEmailBack {

	private String success;//T-成功，F-失败
	private String failmailnums;//失败邮件号
	private String remark;//备注信息
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getFailmailnums() {
		return failmailnums;
	}
	public void setFailmailnums(String failmailnums) {
		this.failmailnums = failmailnums;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
