package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.explink.domain.Complaint;
import cn.explink.domain.Customer;
import cn.explink.util.StringUtil;

@Service
public class ComplaintService {

	public Complaint loadFormForComplaint(HttpServletRequest request, long complaintid, String userName) {
		Complaint complaint = this.loadFormForComplaint(request, userName);
		complaint.setComplaintid(complaintid);
		return complaint;
	}

	public Complaint loadFormForComplaint(HttpServletRequest request, String userName) {
		Complaint complaint = new Complaint();
		complaint.setCheckflag(new Long((request.getParameter("checkflag") == null || "".equals(request.getParameter("checkflag"))) ? "0" : request.getParameter("checkflag")));
		complaint.setComplaintcontactman(StringUtil.nullConvertToEmptyString(request.getParameter("complaintcontactman")));
		complaint.setComplaintcontent(StringUtil.nullConvertToEmptyString(request.getParameter("complaintcontent")));
		complaint.setComplaintcreateuser(userName);
		complaint.setComplaintcustomerid(new Long((request.getParameter("complaintcustomerid") == null || "".equals(request.getParameter("complaintcustomerid"))) ? "0" : request
				.getParameter("complaintcustomerid")));
		complaint.setComplaintcwb(StringUtil.nullConvertToEmptyString(request.getParameter("complaintcwb")));
		complaint.setComplaintflag(new Long((request.getParameter("complaintflag") == null || "".equals(request.getParameter("complaintflag"))) ? "0" : request.getParameter("complaintflag")));
		complaint.setComplaintdelflag(new Long((request.getParameter("complaintdelflag") == null || "".equals(request.getParameter("complaintdelflag"))) ? "0" : request
				.getParameter("complaintdelflag")));
		complaint.setComplaintid(new Long((request.getParameter("complaintid") == null || "".equals(request.getParameter("complaintid"))) ? "0" : request.getParameter("complaintid")));
		complaint.setComplaintphone(StringUtil.nullConvertToEmptyString(request.getParameter("complaintphone")));
		complaint.setComplaintresult(StringUtil.nullConvertToEmptyString(request.getParameter("complaintresult")));
		complaint.setComplaintuserdesc(StringUtil.nullConvertToEmptyString(request.getParameter("complaintuserdesc")));
		complaint.setComplainttypeid(new Long((request.getParameter("complainttypeid") == null || "".equals(request.getParameter("complainttypeid"))) ? "0" : request.getParameter("complainttypeid")));
		complaint.setComplaintuserid(new Long((request.getParameter("complaintuserid") == null || "".equals(request.getParameter("complaintuserid"))) ? "0" : request.getParameter("complaintuserid")));
		return complaint;
	}

}
