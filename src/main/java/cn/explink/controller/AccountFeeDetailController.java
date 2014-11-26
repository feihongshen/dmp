package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountFeeDetailDAO;
import cn.explink.dao.AccountFeeTypeDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountFeeDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.AccountCwbDetailService;
import cn.explink.service.AccountFeeDetailService;
import cn.explink.service.AccountFeeTypeService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * 款项明细管理
 * 
 * @author zs
 *
 */

@Controller
@RequestMapping("/accountfeedetail")
public class AccountFeeDetailController {
	@Autowired
	UserDAO userDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	AccountFeeDetailService accountFeeDetailService;

	@Autowired
	AccountFeeTypeService accountFeeTypeService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	AccountFeeDetailDAO accountFeeDetailDAO;

	@Autowired
	AccountFeeTypeDAO accountFeeTypeDAO;

	private Logger logger = LoggerFactory.getLogger(AccountFeeDetailController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 分页查找款项明细列表
	 * 
	 * @param page
	 * @param branchid
	 * @param detailname
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "feetypeid", required = false, defaultValue = "0") long feetypeid,
			@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, @RequestParam(value = "detailname", required = false, defaultValue = "") String detailname)
			throws Exception {
		model.addAttribute("feeDetailList", accountFeeDetailService.getAccountFeeDetailList(page, feetypeid, branchname, detailname));
		model.addAttribute("page_obj", new Page(accountFeeDetailService.getAccountFeeDetailCount(feetypeid, branchname, detailname), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/accountfeedetail/list";
	}

	/**
	 * 弹出创建款项明细窗口跳转
	 * 
	 * @param feetypeid
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("feeTypeList", accountFeeTypeDAO.getAccountFeeTypeList());
		// 站点
		model.addAttribute("branchList", branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue()));

		return "/accountfeedetail/add";
	}

	/**
	 * 创建款项明细
	 * 
	 * @param model
	 * @param branchid
	 * @param feetypeid
	 * @param checkoutstate
	 * @param detailname
	 * @param customfee
	 * @return
	 */
	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("branchid") long branchid, @RequestParam("feetypeid") long feetypeid, @RequestParam("checkoutstate") long checkoutstate,
			@RequestParam("detailname") String detailname, @RequestParam("customfee") BigDecimal customfee) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nowtime = df.format(date);
		AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
		accountFeeDetail.setFeetypeid(feetypeid);
		accountFeeDetail.setBranchid(branchid);
		accountFeeDetail.setCheckoutstate(checkoutstate);
		accountFeeDetail.setDetailname(detailname);
		accountFeeDetail.setCustomfee(customfee);
		accountFeeDetail.setUserid(getSessionUser().getUserid());
		accountFeeDetail.setCreatetime(nowtime);
		logger.info("用户:{},为站点id{},创建自定义款金额{},操作时间{}", new Object[] { getSessionUser().getUserid(), branchid, customfee, nowtime });

		boolean f = accountFeeDetailService.createAccountFeeDetail(accountFeeDetail);
		if (f) {
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"创建失败\"}";
		}
	}

	/**
	 * 修改款项明细跳转
	 * 
	 * @param model
	 * @param feedetailid
	 * @return
	 */
	@RequestMapping("/edit/{feedetailid}")
	public String edit(Model model, @PathVariable("feedetailid") long feedetailid) {
		AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
		accountFeeDetail = accountFeeDetailService.getAccountFeeDetailById(feedetailid);
		model.addAttribute("accountFeeDetail", accountFeeDetail);
		model.addAttribute("accountFeeType", accountFeeTypeService.getAccountFeeTypeById(accountFeeDetail.getFeetypeid()));

		model.addAttribute("feeTypeList", accountFeeTypeDAO.getAccountFeeTypeList());
		// 站点
		model.addAttribute("branchList", branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue()));
		return "/accountfeedetail/edit";
	}

	/**
	 * 款项明细更新
	 * 
	 * @param model
	 * @param feedetailid
	 * @param branchid
	 * @param feetypeid
	 * @param checkoutstate
	 * @param detailname
	 * @param customfee
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save/{feedetailid}")
	public @ResponseBody String save(Model model, @PathVariable("feedetailid") long feedetailid, HttpServletRequest request) throws Exception {
		long branchid = Long.parseLong(request.getParameter("branchid") == null ? "0" : request.getParameter("branchid"));
		BigDecimal customfee = new BigDecimal(request.getParameter("customfee") == null ? "0" : request.getParameter("customfee"));
		AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
		accountFeeDetail.setFeedetailid(Long.parseLong(request.getParameter("feedetailid") == null ? "0" : request.getParameter("feedetailid")));
		accountFeeDetail.setFeetypeid(Long.parseLong(request.getParameter("feetypeid") == null ? "0" : request.getParameter("feetypeid")));
		accountFeeDetail.setBranchid(branchid);
		accountFeeDetail.setDetailname(StringUtil.nullConvertToEmptyString(request.getParameter("detailname")));
		accountFeeDetail.setCustomfee(customfee);

		boolean f = accountFeeDetailService.saveAccountFeeType(accountFeeDetail);
		logger.info("用户:{},为站点id{},创建自定义款金额{}", new Object[] { getSessionUser().getUserid(), branchid, customfee });
		if (f) {
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"修改失败\"}";
		}

	}

	/**
	 * 根据加减款ids查找
	 * 
	 * @param model
	 * @param feedetailid
	 * @return
	 */
	@RequestMapping("/detailList")
	public String detailList(Model model, @RequestParam(value = "feetype", required = false, defaultValue = "0") long feetype,
			@RequestParam(value = "feedetailid", required = false, defaultValue = "") String feedetailid) {
		model.addAttribute("feeDetailList", accountFeeDetailDAO.getAccountFeeDetailByIds(feedetailid, feetype));
		return "/accountfeedetail/detailList";
	}

	/**
	 * 根据加减款Summaryid查找
	 * 
	 * @param model
	 * @param summaryid
	 * @return
	 */
	@RequestMapping("/detailSummaryidList")
	public String detailSummaryidList(Model model, @RequestParam(value = "feetype", required = false, defaultValue = "0") long feetype,
			@RequestParam(value = "summaryid", required = false, defaultValue = "0") long summaryid) {
		model.addAttribute("feeDetailList", accountFeeDetailDAO.getDetailBySummaryidList(summaryid, feetype));
		return "/accountfeedetail/detailList";
	}
}
