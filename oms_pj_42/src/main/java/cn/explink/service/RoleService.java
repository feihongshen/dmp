package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.explink.domain.Role;
import cn.explink.util.StringUtil;

@Service
public class RoleService {

	public Role loadFormForRole(HttpServletRequest request) {
		Role r = new Role();
		r.setRoleid(Long.parseLong(request.getParameter("roleid") == null ? "0" : request.getParameter("roleid")));
		r.setRolename(StringUtil.nullConvertToEmptyString(request.getParameter("rolename")));
		r.setType(Integer.parseInt(request.getParameter("type") == null ? "0" : request.getParameter("type")));
		return r;

	}

}
