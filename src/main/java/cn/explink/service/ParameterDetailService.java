package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.explink.domain.Branch;
import cn.explink.domain.ParameterDetail;
import cn.explink.util.StringUtil;

@Service
public class ParameterDetailService {

	public ParameterDetail loadFormForBranch(HttpServletRequest request) {
		ParameterDetail parameterDetail = new ParameterDetail();
		parameterDetail.setFlowordertype(Integer.parseInt(request.getParameter("flowordertype") == null ? "0" : request.getParameter("flowordertype")));
		parameterDetail.setFiled(StringUtil.nullConvertToEmptyString(request.getParameter("filed")));
		parameterDetail.setName(parameterDetail.getName());

		return parameterDetail;
	}

}
