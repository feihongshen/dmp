package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.controller.UserView;
import cn.explink.dao.FinanceDeliverPayUpDetailDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.FinanceDeliverPayupDetail;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class UserService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDAO userDAO;
	@Autowired
	FinanceDeliverPayUpDetailDAO financeDeliverPayUpDetailDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

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
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, name);
			user.setUserwavfile(name);
		}
		return user;
	}

	public User loadFormForUserToEdit(HttpServletRequest request, long roleid, long branchid, MultipartFile file) {
		User user = this.loadFormForUser(request, roleid, branchid);
		if ((file != null) && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = System.currentTimeMillis() + ".wav";
			ServiceUtil.uploadWavFile(file, filePath, name);
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
		user.setUsermobile(StringUtil.nullConvertToEmptyString(request.getParameter("usermobile")));
		user.setShowphoneflag(request.getParameter("consigneephone") == null ? 0 : 1);
		user.setShownameflag(request.getParameter("consigneename") == null ? 0 : 1);
		user.setShowmobileflag(request.getParameter("consigneemobile") == null ? 0 : 1);
		user.setUseremail(StringUtil.nullConvertToEmptyString(request.getParameter("useremail")));
		user.setUserwavfile(StringUtil.nullConvertToEmptyString(request.getParameter("userwavfile")));
		user.setRoleid(roleid);
		if ((request.getParameter("isImposedOutWarehouse") != null) && (request.getParameter("isImposedOutWarehouse").length() > 0)) {
			user.setIsImposedOutWarehouse(Integer.parseInt(StringUtil.nullConvertToEmptyString(request.getParameter("isImposedOutWarehouse"))));
		}
		return user;
	}

	/**
	 * 调账 修改小件员帐户
	 *
	 * @param deliverid
	 *            小件员id
	 * @param deliverpayupamount
	 *            现金调整金额
	 * @param deliverpayupamount_pos
	 *            POS调整金额
	 * @param remark
	 *            备注
	 * @param user
	 *            操作人
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void editDeliverAccount(Long deliverid, BigDecimal deliverpayupamount, BigDecimal deliverpayupamount_pos, String remark, User user) {
		User u = this.userDAO.getUserByUseridLock(deliverid);
		FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
		fdpud.setDeliverealuser(deliverid);
		fdpud.setPayupamount(BigDecimal.ZERO);
		fdpud.setDeliverpayupamount(deliverpayupamount);
		fdpud.setDeliverAccount(u.getDeliverAccount());
		fdpud.setDeliverpayuparrearage(u.getDeliverAccount().add(deliverpayupamount));
		fdpud.setPayupamount_pos(BigDecimal.ZERO);
		fdpud.setDeliverpayupamount_pos(deliverpayupamount_pos);
		fdpud.setDeliverPosAccount(u.getDeliverPosAccount());
		fdpud.setDeliverpayuparrearage_pos(u.getDeliverPosAccount().add(deliverpayupamount_pos));
		fdpud.setGcaid(-1);
		fdpud.setAudituserid(user.getUserid());
		fdpud.setCredate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		fdpud.setType(FinanceDeliverPayUpDetailTypeEnum.TiaoZhang.getValue());
		fdpud.setRemark(remark);
		this.logger.info("财务为小件员调账产生交易：{}", fdpud.toString());
		// 创建交易明细
		this.financeDeliverPayUpDetailDAO.insert(fdpud);
		// 更改用户余额
		this.userDAO.updateUserAmount(deliverid, u.getDeliverAccount().add(deliverpayupamount), u.getDeliverPosAccount().add(deliverpayupamount_pos));
	}

	/**
	 * 调账 修改小件员帐户
	 *
	 * @param deliverid
	 *            小件员id
	 * @param deliverpayupamount
	 *            现金调整金额
	 * @param deliverpayupamount_pos
	 *            POS调整金额
	 * @param remark
	 *            备注
	 * @param user
	 *            操作人
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void updateStateAndMoney(long id, String deliverpayupamount) {
		GotoClassAuditing gca = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(id);
		BigDecimal amount = new BigDecimal(deliverpayupamount);
		User u = this.userDAO.getUserByUseridLock(gca.getDeliverealuser());
		this.logger.info("小件员" + u.getRealname() + "自己修改审核未通过的金额：{}={} 归班记录id：" + gca.getId(), gca.getDeliverpayupamount().doubleValue(), amount.doubleValue());
		BigDecimal deliverpayuparrearage = u.getDeliverAccount().add(amount.subtract(gca.getDeliverpayupamount()));
		// 修改归班交款金额
		this.gotoClassAuditingDAO.updateStateAndMoney(id, deliverpayupamount, deliverpayuparrearage);
		// 修改交款记录
		this.financeDeliverPayUpDetailDAO.updateDeliverpayupamount(amount, deliverpayuparrearage, gca.getId());
		// 更改用户余额
		this.userDAO.updateUserAmount(u.getUserid(), deliverpayuparrearage, BigDecimal.ZERO);
	}

	public List<UserView> getUserView(List<User> userList, List<Role> roleList, List<Branch> branchList) {
		List<UserView> userViews = new ArrayList<UserView>();
		if ((userList != null) && (userList.size() > 0)) {
			for (User u : userList) {
				UserView view = new UserView();
				view.setUserid(u.getUserid());
				view.setRealname(u.getRealname());
				view.setEmployeestatus(u.getEmployeestatus());
				view.setUsermobile(u.getUsermobile());
				view.setUserphone(u.getUserphone());
				view.setLastLoginIp(u.getLastLoginIp());
				view.setLastLoginTime(u.getLastLoginTime());
				view.setBranchname(this.getBranchName(branchList, u.getBranchid()));
				view.setRolename(this.getRoleName(roleList, u.getRoleid()));
				userViews.add(view);
			}
		}
		return userViews;
	}

	private String getRoleName(List<Role> roleList, long roleid) {
		String rolename = "";
		if ((roleList != null) && (roleList.size() > 0)) {

			for (Role role : roleList) {
				if (role.getRoleid() == roleid) {
					rolename = role.getRolename();
					break;
				}
			}

		}
		return rolename;
	}

	private String getBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		if ((branchList != null) && (branchList.size() > 0)) {
			for (Branch branch : branchList) {
				if (branch.getBranchid() == branchid) {
					branchname = branch.getBranchname();
					break;
				}
			}
		}
		return branchname;
	}

	/**
	 * 导出 user
	 */
	public void exportUser(List<User> userList, List<Branch> branchList, List<Role> roleList) {

		List<UserView> views = new ArrayList<UserView>();
		if ((userList != null) && (userList.size() > 0)) {
			for (User user : userList) {
				UserView userView = new UserView();
				userView.setUserid(user.getUserid());
				userView.setBranchname(this.getBranchName(branchList, user.getBranchid()));
				userView.setRolename(this.getRoleName(roleList, user.getRoleid()));
				userView.setRealname(user.getRealname());
				userView.setUsername(user.getUsername());
				userView.setUsermobile(user.getUsermobile());
				userView.setUseremail(user.getUseremail());
				userView.setLastLoginIp(user.getLastLoginIp());
				userView.setLastLoginTime(user.getLastLoginTime());
				// userView.getEmployeestatusName();
				views.add(userView);
			}
		}
		String[] cloumnName1 = new String[8]; // 导出的列名
		String[] cloumnName2 = new String[8]; // 导出的英文列名

		// exportService.SetAbnormalOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "问题件信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "AbnormalOrder_" + df.format(new Date()) + ".xlsx"; // 文件名

	}

}
