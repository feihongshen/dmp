package cn.explink.domain;

public class SmsConfig {

	private long id; // ID
	private String name; // 账户
	private String password; // 密码
	private long warningcount; // 预警信息数量
	private String phone; // 电话
	private String templet; // 短信抬头
	private long monitor; // 是否监控
	private String warningcontent; // 预警信息内容
	private long isOpen = 0; // 是否监控
	private String templatecontent; // 短信模板

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getWarningcount() {
		return warningcount;
	}

	public void setWarningcount(long warningcount) {
		this.warningcount = warningcount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTemplet() {
		return templet;
	}

	public void setTemplet(String templet) {
		this.templet = templet;
	}

	public long getMonitor() {
		return monitor;
	}

	public void setMonitor(long monitor) {
		this.monitor = monitor;
	}

	public String getWarningcontent() {
		return warningcontent;
	}

	public void setWarningcontent(String warningcontent) {
		this.warningcontent = warningcontent;
	}

	public long getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(long isOpen) {
		this.isOpen = isOpen;
	}

	public String getTemplatecontent() {
		return templatecontent;
	}

	public void setTemplatecontent(String templatecontent) {
		this.templatecontent = templatecontent;
	}

}
