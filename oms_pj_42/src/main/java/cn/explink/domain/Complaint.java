package cn.explink.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.explink.enumutil.ComplaintTypeEnum;

public class Complaint {
	private long complaintid; // 主键id
	private long complainttypeid; // 投诉类型id
	private String complaintcwb; // 投诉单号
	private long complaintcustomerid; // 投诉客户id
	private String complaintcontent; // 投诉内容
	private long complaintuserid; // 被投诉人id
	private String complaintresult; // 投诉处理结果
	private String complaintcreateuser; // 投诉创建人姓名
	private String complainttime; // 投诉记录时间
	private long checkflag; // 审核确认投诉 0未审核 1已审核
	private String complaintcontactman; // 投诉人
	private String complaintphone; // 投诉人联系电话
	private long complaintflag; // 投诉处理状态
	private long complaintdelflag; // 删除状态
	private String complaintuserdesc; // 被投诉人备注
	private long issure; // 是否属实（0：不属实；1：属实）

	public Complaint() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		complainttime = sdf.format(new Date());
	}

	public long getComplaintid() {
		return complaintid;
	}

	public void setComplaintid(long complaintid) {
		this.complaintid = complaintid;
	}

	public long getComplainttypeid() {
		return complainttypeid;
	}

	public void setComplainttypeid(long complainttypeid) {
		this.complainttypeid = complainttypeid;
	}

	public String getComplaintcwb() {
		return complaintcwb;
	}

	public void setComplaintcwb(String complaintcwb) {
		this.complaintcwb = complaintcwb;
	}

	public long getComplaintcustomerid() {
		return complaintcustomerid;
	}

	public void setComplaintcustomerid(long complaintcustomerid) {
		this.complaintcustomerid = complaintcustomerid;
	}

	public String getComplaintcontent() {
		return complaintcontent;
	}

	public void setComplaintcontent(String complaintcontent) {
		this.complaintcontent = complaintcontent;
	}

	public long getComplaintuserid() {
		return complaintuserid;
	}

	public void setComplaintuserid(long complaintuserid) {
		this.complaintuserid = complaintuserid;
	}

	public String getComplaintresult() {
		return complaintresult;
	}

	public void setComplaintresult(String complaintresult) {
		this.complaintresult = complaintresult;
	}

	public String getComplaintcreateuser() {
		return complaintcreateuser;
	}

	public void setComplaintcreateuser(String complaintcreateuser) {
		this.complaintcreateuser = complaintcreateuser;
	}

	public String getComplainttime() {
		return complainttime;
	}

	public void setComplainttime(String complainttime) {
		this.complainttime = complainttime;
	}

	public long getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(long checkflag) {
		this.checkflag = checkflag;
	}

	public String getComplaintcontactman() {
		return complaintcontactman;
	}

	public void setComplaintcontactman(String complaintcontactman) {
		this.complaintcontactman = complaintcontactman;
	}

	public String getComplaintphone() {
		return complaintphone;
	}

	public void setComplaintphone(String complaintphone) {
		this.complaintphone = complaintphone;
	}

	public long getComplaintflag() {
		return complaintflag;
	}

	public void setComplaintflag(long complaintflag) {
		this.complaintflag = complaintflag;
	}

	public long getComplaintdelflag() {
		return complaintdelflag;
	}

	public void setComplaintdelflag(long complaintdelflag) {
		this.complaintdelflag = complaintdelflag;
	}

	public String getComplaintuserdesc() {
		return complaintuserdesc;
	}

	public void setComplaintuserdesc(String complaintuserdesc) {
		this.complaintuserdesc = complaintuserdesc;
	}

	public String getComplainttype() {
		for (ComplaintTypeEnum c : ComplaintTypeEnum.values()) {
			if (c.getValue() == complainttypeid) {
				return c.getText();
			}
		}
		return "";
	}

	public String getCheckflagStr() {

		return checkflag == 1 ? "已审核" : "未审核";
	}

	public String getComplaintflagStr() {

		return complaintflag == 1 ? "已处理" : "未处理";
	}

	public long getIssure() {
		return issure;
	}

	public void setIssure(long issure) {
		this.issure = issure;
	}

	public String getIssureStr() {
		return issure == 1 ? "属实" : "不属实";
	}

}
