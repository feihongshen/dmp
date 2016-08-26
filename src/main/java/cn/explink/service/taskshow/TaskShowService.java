package cn.explink.service.taskshow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.controller.TaskShowController;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.ResourceBundleUtil;

import com.pjbest.osp.cfg.system.service.HandbookRespone;
import com.pjbest.osp.cfg.system.service.NearestViewRecordRespone;
import com.pjbest.osp.cfg.system.service.NoticeRespone;
import com.pjbest.osp.cfg.system.service.SysHandbookService;
import com.pjbest.osp.cfg.system.service.SysHandbookServiceHelper;
import com.pjbest.osp.cfg.system.service.SysNoticeService;
import com.pjbest.osp.cfg.system.service.SysNoticeServiceHelper;
import com.pjbest.osp.cfg.system.service.SysVersionService;
import com.pjbest.osp.cfg.system.service.SysVersionServiceHelper;
import com.pjbest.osp.cfg.system.service.SysVersioninfoViewrecordService;
import com.pjbest.osp.cfg.system.service.SysVersioninfoViewrecordServiceHelper;
import com.pjbest.osp.cfg.system.service.VersionInfoRequest;
import com.pjbest.osp.cfg.system.service.VersionInfoRespone;
import com.pjbest.osp.cfg.system.service.ViewRecordRequest;
import com.pjbest.osp.cfg.system.service.ViewRecordRespone;

@Service
public class TaskShowService {
	private static Logger logger = LoggerFactory.getLogger(TaskShowController.class);
	final private static String SYSTEM_CODE = "DMP";
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ProvinceDAO provinceDAO;

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
		viewRecordRequest.setOrgInfo(getOrgInfoByBranch(ResourceBundleUtil.provinceCode, branch.getBranchname()));
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
	
	private String getOrgInfoByBranch(String provinceCode, String branchname) {
		AdressVO province = provinceDAO
				.getProvinceByCode(provinceCode);
		String provinceName = (province == null ? "" : province.getName());
		String result = provinceName + "-" + branchname;
		return result;
	}
	
	public NearestViewRecordRespone getNearestViewRecord() throws Exception {
		SysVersionService sysVersionService = new SysVersionServiceHelper.SysVersionServiceClient();	
		try {
			NearestViewRecordRespone nearestViewRecord = sysVersionService.getNearestViewRecord(SYSTEM_CODE);
			return nearestViewRecord;
		} catch (Exception e) {
			logger.info("调用ops服务：获取历史版本说明异常{}",e.getMessage());
			throw e;
		}
	}
	
	public HandbookRespone getLatestHandbook() throws Exception {
		SysHandbookService sysHandbookService = new SysHandbookServiceHelper.SysHandbookServiceClient();	
		try {
			HandbookRespone handbook = sysHandbookService.getLatestHandbook(SYSTEM_CODE);
			return handbook;
		} catch (Exception e) {
			logger.info("调用ops服务：获取最新用户手册异常{}",e.getMessage());
			throw e;
		}
	}
	
	public NoticeRespone getLatestNotice() throws Exception {
		SysNoticeService sysNoticeService = new SysNoticeServiceHelper.SysNoticeServiceClient();	
		try {
			NoticeRespone notice = sysNoticeService.getLatestNotice(SYSTEM_CODE);
			return notice;
		} catch (Exception e) {
			logger.info("调用ops服务：获取最新系统公告异常{}",e.getMessage());
			throw e;
		}
	}
}
