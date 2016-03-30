package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.PayUp;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PayUpService;
import cn.explink.util.MD5.PaySign;

@Controller
@RequestMapping("/payup")
public class PayUpController {

	private static Logger logger = LoggerFactory.getLogger(PayUpController.class);
	
	@Autowired
	UserDAO userDAO;

	@Autowired
	PayUpDAO payUpDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	PayUpService payUpService;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/viewCount")
	public String viewCount(Model model) {

		SystemInstall useAudit = systemInstallDAO.getSystemInstallByName("useAudit");
		if (useAudit != null && "no".equals(useAudit.getValue())) {// 不需要归班的上交款查询需要上缴的反馈
			return viewCountByNotAudit(model);
		}
		List<GotoClassAuditing> gcaList = gotoClassAuditingDAO.getCountPayToUp(getSessionUser().getBranchid());
		BigDecimal hk_amount = BigDecimal.ZERO;
		BigDecimal hk_amount_pos = BigDecimal.ZERO;
		StringBuffer gcaids = new StringBuffer();// 存储当前要处理的货物金额
		for (GotoClassAuditing gca : gcaList) {
			hk_amount = gca.getPayupamount().add(hk_amount);
			hk_amount_pos = gca.getPayupamount_pos().add(hk_amount_pos);
			gcaids.append(",'").append(gca.getId()).append("'");
		}
		model.addAttribute("hk_amount", hk_amount);
		model.addAttribute("hk_amount_pos", hk_amount_pos);
		if (gcaids.length() > 0) {
			try {
				model.addAttribute("gcaids", gcaids.toString().substring(1));
				model.addAttribute("gcaidsMAC", PaySign.Md5(gcaids.toString().substring(1)));
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		model.addAttribute("user_branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		model.addAttribute("caiwu_branch", branchDAO.getBranchByMyBranchIsCaiwu(getSessionUser().getBranchid()));
		return "payup/viewCount";
	}

	/**
	 * 不用归班的上交款
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/viewCountByNotAudit")
	public String viewCountByNotAudit(Model model) {
		List<DeliveryState> deliveList = deliveryStateDAO.getCountPayToUpByNotAudit(getSessionUser().getBranchid());
		BigDecimal hk_amount = BigDecimal.ZERO;
		BigDecimal hk_amount_pos = BigDecimal.ZERO;
		StringBuffer delids = new StringBuffer();// 存储当前要处理的货物金额
		for (DeliveryState del : deliveList) {
			hk_amount = del.getCash().add(hk_amount);
			hk_amount_pos = del.getPos().add(hk_amount_pos);
			delids.append(",'").append(del.getId()).append("'");
		}
		model.addAttribute("hk_amount", hk_amount);
		model.addAttribute("hk_amount_pos", hk_amount_pos);
		if (delids.length() > 0) {
			try {
				model.addAttribute("gcaids", delids.toString().substring(1));
				model.addAttribute("gcaidsMAC", PaySign.Md5(delids.toString().substring(1)));
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		model.addAttribute("user_branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		model.addAttribute("caiwu_branch", branchDAO.getBranchByMyBranchIsCaiwu(getSessionUser().getBranchid()));
		return "payup/viewCount";
	}

	@RequestMapping("/subPayUp")
	public String subPayUp(Model model, @RequestParam("gcaids") String gcaids, @RequestParam("gcaidsMAC") String gcaidsMAC, @RequestParam("upbranchid") long upbranchid,
			@RequestParam("type") int type, @RequestParam("amount") BigDecimal amount, @RequestParam("way") int way, @RequestParam("remark") String remark,
			@RequestParam("hk_amount") BigDecimal hk_amount, @RequestParam("hk_amount_pos") BigDecimal hk_amount_pos, @RequestParam("fa_amount") BigDecimal fa_amount) {

		SystemInstall useAudit = systemInstallDAO.getSystemInstallByName("useAudit");
		if (useAudit != null && "no".equals(useAudit.getValue())) {// 不需要归班的上交款
			return subPayUpByNotAudit(model, gcaids, gcaidsMAC, upbranchid, type, amount, way, remark, hk_amount, hk_amount_pos, fa_amount);
		}
		try {
			PayUp payup = new PayUp();
			payup.setUpbranchid(upbranchid);
			payup.setType(type);
			payup.setAmount(amount);
			payup.setAmountPos(hk_amount_pos);
			payup.setWay(way);
			payup.setRemark(remark);
			payup.setBranchid(getSessionUser().getBranchid());
			payup.setUserid(getSessionUser().getUserid());
			payup.setUpuserrealname(getSessionUser().getRealname());

			payUpService.subPayUpService(getSessionUser(), model, payup, gcaids, hk_amount, fa_amount, gcaidsMAC);

		} catch (Exception e) {
			logger.error("", e);
		}
		return "payup/error";
	}

	@RequestMapping("/subPayUpByNotAudit")
	public String subPayUpByNotAudit(Model model, @RequestParam("gcaids") String gcaids, @RequestParam("gcaidsMAC") String gcaidsMAC, @RequestParam("upbranchid") long upbranchid,
			@RequestParam("type") int type, @RequestParam("amount") BigDecimal amount, @RequestParam("way") int way, @RequestParam("remark") String remark,
			@RequestParam("hk_amount") BigDecimal hk_amount, @RequestParam("hk_amount_pos") BigDecimal hk_amount_pos, @RequestParam("fa_amount") BigDecimal fa_amount) {
		try {
			PayUp payup = new PayUp();
			payup.setUpbranchid(upbranchid);
			payup.setType(type);
			payup.setAmount(amount);
			payup.setAmountPos(hk_amount_pos);
			payup.setWay(way);
			payup.setRemark(remark);
			payup.setBranchid(getSessionUser().getBranchid());
			payup.setUserid(getSessionUser().getUserid());
			payup.setUpuserrealname(getSessionUser().getRealname());

			payUpService.subPayUpByNotAuditService(getSessionUser(), model, payup, gcaids, hk_amount, fa_amount, gcaidsMAC);

		} catch (Exception e) {
			logger.error("", e);
		}
		return "payup/error";
	}

	@RequestMapping("/viewPayUp")
	public String viewPayUp(Model model, @RequestParam(value = "upstate", defaultValue = "-1", required = false) int upstate,
			@RequestParam(value = "type", defaultValue = "0", required = false) int type, @RequestParam(value = "way", defaultValue = "0", required = false) int way,
			@RequestParam(value = "credatetime", defaultValue = "", required = false) String credatetime, @RequestParam(value = "credatetime1", defaultValue = "", required = false) String credatetime1) {
		List<GotoClassAuditing> gcaList = gotoClassAuditingDAO.getCountPayToUp(getSessionUser().getBranchid());
		BigDecimal hk_amount = new BigDecimal(0);
		for (GotoClassAuditing gca : gcaList) {
			hk_amount = gca.getPayupamount().add(hk_amount);
		}
		model.addAttribute("hk_amount", hk_amount);
		model.addAttribute("user_branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date c0 = sdf.parse(credatetime);
			try {
				Date c1 = sdf.parse(credatetime1);
				if (c0.getTime() > c1.getTime()) {
					Date c2 = c0;
					c0 = c1;
					c1 = c2;
					credatetime = sdf.format(c0);
					credatetime1 = sdf.format(c1);
				}
			} catch (Exception e) {
				credatetime1 = sdf.format(new Date());
			}
		} catch (Exception e) {
			credatetime = sdf.format(new Date());
			credatetime1 = sdf.format(new Date());
		}

		model.addAttribute("payupList", payUpDAO.getPayUpByBranchid(upstate, type, way, credatetime, credatetime1, getSessionUser().getBranchid()));
		model.addAttribute("credatetime", credatetime);
		model.addAttribute("credatetime1", credatetime1);
		return "payup/viewPayUp";
	}

	@RequestMapping("/noPayUpDetail")
	public String getNoPayUpDetail(Model model) {
		long branchid = getSessionUser().getBranchid();
		model.addAttribute("nopayupdetial", gotoClassAuditingDAO.getNoPayUpDetail(branchid));
		model.addAttribute("userList", userDAO.getAllUser());
		return "payup/noPayUpDetail";
	}
}
