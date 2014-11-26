package cn.explink.b2c.tools;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.util.StringUtil;

@Service
public class ExptReasonService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public ExptReason LoadingExptEntity(HttpServletRequest request, int support_key, int b2c_flag) {
		ExptReason expt = new ExptReason();
		if (b2c_flag == 1) { // b2c
			expt.setCustomerid(support_key);
			expt.setSupport_key("0");
		} else {
			expt.setCustomerid(0);
			expt.setSupport_key(StringUtil.nullConvertToEmptyString(request.getParameter("support_key")));
		}

		expt.setCustomername(customerDAO.getCustomerById(Long.parseLong(request.getParameter("customerid"))).getCustomername());
		expt.setCustomercode(request.getParameter("customercode"));
		expt.setExpt_code(StringUtil.nullConvertToEmptyString(request.getParameter("expt_code")));
		expt.setExpt_msg(StringUtil.nullConvertToEmptyString(request.getParameter("expt_msg")));
		expt.setExpt_remark(StringUtil.nullConvertToEmptyString(request.getParameter("expt_remark")));
		expt.setExpt_type(Integer.parseInt(request.getParameter("expt_type")));

		return expt;
	}

}
