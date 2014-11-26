package cn.explink.b2c.tools;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.util.StringUtil;

@Service
public class ExptCodeJointService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public ExptCodeJoint LoadingExptEntity(HttpServletRequest request) {
		ExptCodeJoint expt = new ExptCodeJoint();
		long reasonid = Long.parseLong(request.getParameter("reasonid"));
		long exptid = Long.parseLong(request.getParameter("exptsupportreason"));
		expt.setReasonid(reasonid);
		expt.setExptid(exptid);

		expt.setExptcode_remark(StringUtil.nullConvertToEmptyString(request.getParameter("exptcode_remark")));

		return expt;
	}

}
