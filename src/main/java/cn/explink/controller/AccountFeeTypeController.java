package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountFeeType;
import cn.explink.domain.User;
import cn.explink.service.AccountFeeTypeService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

/**
 * 款项管理
 * 
 * @author zs
 *
 */

@Controller
@RequestMapping("/accountfeetype")
public class AccountFeeTypeController {
	@Autowired
	UserDAO userDAO;

	@Autowired
	AccountFeeTypeService accountFeeTypeService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 分页查找款项列表
	 * 
	 * @param page
	 * @param feetype
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "feetype", required = false, defaultValue = "0") long feetype) throws Exception {
		model.addAttribute("feeTypeList", accountFeeTypeService.getAccountFeeTypeList(page, feetype));
		model.addAttribute("page_obj", new Page(accountFeeTypeService.getAccountFeeTypeCount(feetype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/accountfeetype/list";
	}

	/**
	 * 弹出创建款项窗口跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model) {
		return "/accountfeetype/add";
	}

	/**
	 * 创建款项
	 * 
	 * @param feetype
	 * @param feetypename
	 * @return
	 */
	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("feetype") long feetype, @RequestParam("feetypename") String feetypename) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nowtime = df.format(date);

		AccountFeeType accountFeeType = new AccountFeeType();
		accountFeeType.setFeetype(feetype);
		accountFeeType.setFeetypename(feetypename);
		accountFeeType.setUserid(getSessionUser().getUserid());
		accountFeeType.setCreatetime(nowtime);
		accountFeeType.setEffectflag(1l);// 是否启用

		List<AccountFeeType> list = accountFeeTypeService.getAccountFeeTypeByName(feetypename);
		if (!list.isEmpty()) {
			return "{\"errorCode\":1,\"error\":\"款项名称已存在\"}";
		} else {
			boolean f = accountFeeTypeService.createAccountFeeType(accountFeeType);
			if (!f) {
				return "{\"errorCode\":0,\"error\":\"创建失败\"}";
			}
		}
		return "{\"errorCode\":0,\"error\":\"创建成功\"}";
	}

	/**
	 * 修改款项跳转
	 * 
	 * @param feetypeid
	 * @return
	 */
	@RequestMapping("/edit/{feetypeid}")
	public String edit(Model model, @PathVariable("feetypeid") long feetypeid) {
		model.addAttribute("accountFeeType", accountFeeTypeService.getAccountFeeTypeById(feetypeid));
		return "/accountfeetype/edit";
	}

	/**
	 * 款项更新
	 * 
	 * @param feetypeid
	 * @param feetype
	 * @param feetypename
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save/{feetypeid}")
	public @ResponseBody String save(Model model, @PathVariable("feetypeid") long feetypeid, @RequestParam("feetype") long feetype, @RequestParam("feetypename") String feetypename) throws Exception {
		List<AccountFeeType> list = accountFeeTypeService.getAccountFeeTypeByName(feetypename);
		if (!list.isEmpty()) {
			return "{\"errorCode\":1,\"error\":\"款项名称已存在\"}";
		} else {
			boolean f = accountFeeTypeService.saveAccountFeeType(feetypeid, feetype, feetypename);
			if (!f) {
				return "{\"errorCode\":0,\"error\":\"修改失败\"}";
			}
		}
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	/**
	 * 款项启用停用
	 * 
	 * @param feetypeid
	 * @return
	 */
	@RequestMapping("del/{feetypeid}")
	public @ResponseBody String del(@PathVariable("feetypeid") long feetypeid) {
		boolean f = accountFeeTypeService.getDelAccountFeeType(feetypeid);
		if (f) {
			return "{\"errorCode\":0}";
		} else {
			return "{\"errorCode\":1}";
		}
	}
}
