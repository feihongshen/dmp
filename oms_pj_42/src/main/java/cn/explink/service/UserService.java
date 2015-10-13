package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class UserService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDAO userDAO;

	public void addUser(User user) {
		this.userDAO.creUser(user);
		this.logger.info("创建一个用户,username:{},roleid:{}", new Object[] { user.getUsername(), user.getRoleid() });
	}

	public void editUser(User user) {
		this.userDAO.saveUser(user);
	}

	public User loadFormForUser(HttpServletRequest request, long roleid, long branchid, MultipartFile file) {
		User user = this.loadFormForUser(request, roleid, branchid);
		if ((file != null) && !file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, ServiceUtil.wavPath + name);
			user.setUserwavfile(name);
		}
		return user;
	}

	public User loadFormForUserToEdit(HttpServletRequest request, long roleid, long branchid, MultipartFile file) {
		User user = this.loadFormForUser(request, roleid, branchid);
		if ((file != null) && !file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, ServiceUtil.wavPath + name);
			user.setUserwavfile(name);
		} else if (request.getParameter("wavh") != null) {
			user.setUserwavfile(request.getParameter("wavh"));
		}
		return user;
	}

	public User loadFormForUser(HttpServletRequest request, long roleid, long branchid) {

		User user = new User();

		user.setUserid((request.getParameter("userid") == null) || request.getParameter("userid").equals("") ? 0L : (Long.parseLong(request.getParameter("userid"))));
		user.setUsername(StringUtil.nullConvertToEmptyString(request.getParameter("username")));
		user.setRealname(StringUtil.nullConvertToEmptyString(request.getParameter("realname")));
		user.setPassword(StringUtil.nullConvertToEmptyString(request.getParameter("password")));
		user.setBranchid(branchid);
		user.setUsercustomerid(Long.parseLong(request.getParameter("usercustomerid")));
		user.setIdcardno(StringUtil.nullConvertToEmptyString(request.getParameter("idcardno")));
		user.setEmployeestatus(Integer.parseInt(request.getParameter("employeestatus")));
		// user.setUserphone(StringUtil.nullConvertToEmptyString(request.getParameter("userphone")));
		user.setUsermobile(StringUtil.nullConvertToEmptyString(request.getParameter("usermobile")));
		// user.setUseraddress(StringUtil.nullConvertToEmptyString(request.getParameter("useraddress")));
		// user.setUserremark(StringUtil.nullConvertToEmptyString(request.getParameter("userremark")));
		// user.setUsersalary(BigDecimal.valueOf(Double.parseDouble(request.getParameter("usersalary"))));
		user.setShowphoneflag(Long.parseLong(request.getParameter("showphoneflag")));
		user.setUseremail(StringUtil.nullConvertToEmptyString(request.getParameter("useremail")));
		user.setUserwavfile(StringUtil.nullConvertToEmptyString(request.getParameter("userwavfile")));
		user.setRoleid(roleid);

		return user;
	}

}
