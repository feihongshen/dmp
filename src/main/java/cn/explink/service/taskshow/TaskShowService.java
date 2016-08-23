package cn.explink.service.taskshow;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.controller.TaskShowController;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.ResourceBundleUtil;

import com.pjbest.osp.cfg.system.service.SysVersionService;
import com.pjbest.osp.cfg.system.service.SysVersionServiceHelper;
import com.pjbest.osp.cfg.system.service.SysVersioninfoViewrecordService;
import com.pjbest.osp.cfg.system.service.SysVersioninfoViewrecordServiceHelper;
import com.pjbest.osp.cfg.system.service.VersionInfoRequest;
import com.pjbest.osp.cfg.system.service.VersionInfoRespone;
import com.pjbest.osp.cfg.system.service.ViewRecordRequest;
import com.pjbest.osp.cfg.system.service.ViewRecordRespone;
import com.vip.osp.core.exception.OspException;

@Service
public class TaskShowService {
	private static Logger logger = LoggerFactory.getLogger(TaskShowController.class);
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;

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

	//用户浏览记录上报
	public ViewRecordRespone getAddVersionViewRecord(String versionNo,
			long showTime) throws Exception {
		Branch branch = branchDAO.getBranchByIdAdd(this.getSessionUser().getBranchid());
		ViewRecordRequest viewRecordRequest = new ViewRecordRequest();
		viewRecordRequest.setSystemCode("DMP");
		viewRecordRequest.setVersionNo(versionNo);
		viewRecordRequest.setUserName(this.getSessionUser().getUsername());//登陆名
		viewRecordRequest.setRealName(this.getSessionUser().getRealname());
		viewRecordRequest.setOrgInfo(branch.getBranchcode());
		viewRecordRequest.setProvinceCode(ResourceBundleUtil.provinceCode);
		viewRecordRequest.setShowTime(showTime);
		viewRecordRequest.setCloseTime(System.currentTimeMillis());
		SysVersioninfoViewrecordService sysVersionService = new SysVersioninfoViewrecordServiceHelper.SysVersioninfoViewrecordServiceClient();
		try {
			ViewRecordRespone viewRecordRespone = sysVersionService.addVersionViewRecord(viewRecordRequest);
			return viewRecordRespone;
		} catch (Exception e) {
			logger.info("调用ops服务：用户浏览记录上报异常{}",e.getMessage());
			throw e;
		}
	}
}
