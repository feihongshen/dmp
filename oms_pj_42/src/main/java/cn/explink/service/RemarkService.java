package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.domain.Remark;

@Service
public class RemarkService {
	@Autowired
	CwbDAO cwbDAO;

	public Remark loadFormForRemark(HttpServletRequest request, long remarkid) {
		Remark remark = loadFormForRemark(request);
		remark.setRemarkid(remarkid);
		return remark;
	}

	public Remark loadFormForRemark(HttpServletRequest request) {
		Remark remark = new Remark();
		remark.setRemarktype(request.getParameter("remarktype"));
		remark.setRemark(request.getParameter("remark"));
		return remark;
	}

}
