package cn.explink.b2c.ems;

/**
 * EMS轨迹对象
 * @author huan.zhou
 */
public class ExpressMail {

	private String serialnumber;//状态顺序号，无实际意义
	private String mailnum;//运单号
	private String procdate;//日期 YYYYMMDD
	private String proctime;//时间 HH24MISS
	private String orgfullname;//操作机构
	private String action;//业务动作
	private String properdelivery;//签收情况
	private String notproperdelivery;//投递失败原因
	private String description;//状态描述
	private int effect;//记录有效标识,0：表示无效(表示该邮件当前这个状态标识为无效的状态，判断依据运单号、日期、时间、动作)，1：表示有效
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getMailnum() {
		return mailnum;
	}
	public void setMailnum(String mailnum) {
		this.mailnum = mailnum;
	}
	public String getProcdate() {
		return procdate;
	}
	public void setProcdate(String procdate) {
		this.procdate = procdate;
	}
	public String getProctime() {
		return proctime;
	}
	public void setProctime(String proctime) {
		this.proctime = proctime;
	}
	public String getOrgfullname() {
		return orgfullname;
	}
	public void setOrgfullname(String orgfullname) {
		this.orgfullname = orgfullname;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getProperdelivery() {
		return properdelivery;
	}
	public void setProperdelivery(String properdelivery) {
		this.properdelivery = properdelivery;
	}
	public String getNotproperdelivery() {
		return notproperdelivery;
	}
	public void setNotproperdelivery(String notproperdelivery) {
		this.notproperdelivery = notproperdelivery;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}


}
