package cn.explink.service.taskshow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.controller.TaskShowController;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.ResourceBundleUtil;

import com.pjbest.osp.cfg.system.service.SysVersionService;
import com.pjbest.osp.cfg.system.service.SysVersionServiceHelper;
import com.pjbest.osp.cfg.system.service.VersionInfoRequest;
import com.pjbest.osp.cfg.system.service.VersionInfoRespone;
import com.vip.osp.core.exception.OspException;

@Service
public class TaskShowService {
	private static Logger logger = LoggerFactory.getLogger(TaskShowController.class);
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserDAO userDAO;

	public VersionInfoRespone getLatestVersion() throws Exception {
		VersionInfoRequest versionInfo = new VersionInfoRequest();
		versionInfo.setSystemCode("DMP");
		versionInfo.setUserName(this.getSessionUser().getUsername());
		versionInfo.setProvinceCode(ResourceBundleUtil.provinceCode);
		SysVersionService sysVersionService = new SysVersionServiceHelper.SysVersionServiceClient();	
		try {
			VersionInfoRespone latestVersion = sysVersionService.getLatestVersion(versionInfo);
			return latestVersion;
		} catch (Exception e) {
			logger.info("调用ops服务：获取最新版本说明异常{}",e.getMessage());
			throw e;
		}
	}
	
	//获取当前登录用户
	protected User getSessionUser() {
		Authentication authen = this.securityContextHolderStrategy.getContext().getAuthentication();
		if(authen==null){
			User user = userDAO.getUserByUsername("admin");
			return user;
		}else{
			ExplinkUserDetail userDetail = (ExplinkUserDetail) authen.getPrincipal();
			return userDetail.getUser();
		}
	}
}
