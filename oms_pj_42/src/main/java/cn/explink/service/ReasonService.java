package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.ReasonDao;
import cn.explink.domain.Reason;
import cn.explink.enumutil.ReasonTypeEnum;

@Service
public class ReasonService {

	@Autowired
	ReasonDao reasonDAO;

	public Reason loadFormForReason(HttpServletRequest request, long reasonid) {
		Reason reason = new Reason();
		reason.setReasoncontent(request.getParameter("reasoncontent"));
		reason.setReasontype(Integer.parseInt(request.getParameter("reasontype")));
		reason.setReasonid(reasonid);
		return reason;
	}

	public Reason loadFormForReason(HttpServletRequest request) {
		Reason reason = new Reason();
		reason.setReasoncontent(request.getParameter("reasoncontent"));
		reason.setReasontype(Long.parseLong(request.getParameter("reasontype")));
		return reason;
	}

	public Reason remark(Reason reason, String csremarkid) {
		String reasoncontent = "";
		int reasontype = Integer.parseInt(csremarkid);
		for (ReasonTypeEnum re : ReasonTypeEnum.values()) {
			if (reasontype == re.getValue()) {
				reasoncontent = re.getText();
			}
		}
		if (reasontype != 0 && reasontype != 5 && reasontype != 6 && reasontype != 7) {
			reasoncontent = "入库无特殊情况备注成功";
		}
		reason.setReasoncontent(reasoncontent);
		reason.setReasontype(reasontype);
		reasonDAO.creReason(reason);
		return reason;
	}

}
