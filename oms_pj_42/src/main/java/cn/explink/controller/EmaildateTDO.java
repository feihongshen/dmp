package cn.explink.controller;

public class EmaildateTDO {
	private String emaildate;

	private long emaildateid;
	private String customername;

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getEmaildate() {
		if (emaildate != null && !"".equals(emaildate) && emaildate.length() > 19) {
			emaildate = emaildate.substring(0, 19);
		}
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}
}
