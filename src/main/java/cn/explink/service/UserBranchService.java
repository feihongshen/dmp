package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserBranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.UserBranch;

@Service
public class UserBranchService {
	@Autowired
	UserBranchDAO userBranchDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@Transactional
	public String create(String[] branchids, String[] userids) {
		for (String userid : userids) {
			List<UserBranch> ubList = userBranchDAO.getUserBranchByUserId(Long.parseLong(userid));
			User user = userDAO.getUserByUserid(Long.parseLong(userid));
			if (ubList != null && ubList.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"用户<" + user.getRealname() + ">已经创建了区域权限，请使用修改的功能修改或者添加新权限\"}";
			}
			for (String branchid : branchids) {
				List<UserBranch> ublist = userBranchDAO.getUserBranchByWheresql(Long.parseLong(branchid), Long.parseLong(userid));
				if (ublist.size() > 0) {
					continue;
				} else {
					UserBranch userBranch = new UserBranch();
					userBranch.setBranchid(Long.parseLong(branchid));
					userBranch.setUserid(Long.parseLong(userid));
					userBranchDAO.creUserBranch(userBranch);
					logger.info("operatorUser={},用户区域权限设置->create", getSessionUser().getUsername());
				}
			}
		}

		return "{\"errorCode\":0,\"error\":\"创建成功\"}";

	}

	@Transactional
	public String createbybranch(String[] branchids, String[] userids) {
		for (String branchid : branchids) {
			List<UserBranch> ubList = userBranchDAO.getUserBranchByBranchid(Long.parseLong(branchid));
			Branch branch = branchDAO.getBranchByBranchid(Long.parseLong(branchid));
			if (ubList != null && ubList.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"机构<" + branch.getBranchname() + ">已经创建了用户区域权限，请使用修改的功能修改或者添加新权限\"}";
			}
			for (String userid : userids) {
				List<UserBranch> ublist = userBranchDAO.getUserBranchByWheresql(Long.parseLong(branchid), Long.parseLong(userid));
				if (ublist.size() > 0) {
					continue;
				} else {
					UserBranch userBranch = new UserBranch();
					userBranch.setBranchid(Long.parseLong(branchid));
					userBranch.setUserid(Long.parseLong(userid));
					userBranchDAO.creUserBranch(userBranch);
					logger.info("operatorUser={},用户区域权限设置->create", getSessionUser().getUsername());
				}
			}
		}

		return "{\"errorCode\":0,\"error\":\"创建成功\"}";

	}

}
