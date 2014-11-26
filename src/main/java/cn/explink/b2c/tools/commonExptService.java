package cn.explink.b2c.tools;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class commonExptService {
	@Autowired
	CommonExptDao commonExptDao;

	public CommonReason LoadingExptEntity(HttpServletRequest request) {
		CommonReason expt = new CommonReason();
		expt.setCustomercode(request.getParameter("customercode"));// 承运商编码
		expt.setCommonid(Long.valueOf(request.getParameter("support_key")));// 承运商id
		expt.setExpt_code(request.getParameter("expt_code"));
		expt.setExpt_msg(request.getParameter("expt_msg"));
		expt.setExpt_type(Long.valueOf(request.getParameter("expt_type")));
		return expt;
	}

	public CommonJoint LoadingJointEntity(HttpServletRequest request) {
		CommonJoint expt = new CommonJoint();
		expt.setReasonid(Long.valueOf(request.getParameter("reasonid")));
		expt.setExptid(Long.valueOf(request.getParameter("exptsupportreason")));
		return expt;
	}

}
