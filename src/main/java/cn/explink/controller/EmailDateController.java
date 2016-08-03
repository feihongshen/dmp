package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

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

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.service.EmailDateService;
import cn.explink.service.ExplinkUserDetail;

@RequestMapping("/emaildate")
@Controller
public class EmailDateController {

	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	AccountAreaDAO accountAreaDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	EmailDateService emailDateService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private static Logger logger = LoggerFactory.getLogger(EmailDateController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 获取对应供货商的发货时间
	 * 
	 * @param model
	 * @param customerids
	 *            供货商id 用逗号分割 1,2,3
	 * @return
	 */
	@RequestMapping("/getEmailDateList")
	public @ResponseBody List<EmailDate> getEmailDateList(Model model, @RequestParam(value = "customerids" , defaultValue = "-1") String customerids, @RequestParam(value = "state",  defaultValue = "-1") String state) {
		List<EmailDate> eList = new ArrayList<EmailDate>();
		if (null == state) {
			for (String idStr : customerids.split(",")) {
				if (idStr.trim().length() == 0) {
					continue;
				}
				eList.addAll(emailDateDAO.getEmailDateByCustomerid(Long.parseLong(idStr)));
			}
		} else if ("-1".equals(state)) {
			long branchid = getSessionUser().getBranchid();
			eList.addAll(emailDateDAO.getEmailDateByCustomeridAndWarehouseId(Long.parseLong(customerids), branchid));
		}else if("2".equals(state)){//匹配管理里面只查询十天以内的批次信息----刘武强 20160803
			eList.addAll(emailDateDAO.getEmailDateByCustomeridInTenDays(Long.parseLong(customerids), state));
		} else {
			eList.addAll(emailDateDAO.getEmailDateByCustomerid(Long.parseLong(customerids), state));
		}
		emailDateService.makeEmFullName(eList);
		return eList;
	}

	/**
	 * 得到供货商两个月内的批次
	 */
	@RequestMapping("/getTowMonthEmailDateList")
	public @ResponseBody List<EmailDate> getTowMonthEmailDateList(Model model, @RequestParam(value = "customerids") String customerids) {
		List<EmailDate> eList = new ArrayList<EmailDate>();
		for (String idStr : customerids.split(",")) {
			if (idStr.trim().length() == 0) {
				continue;
			}
			eList.addAll(emailDateDAO.getEmailDateByCustomerid(Long.parseLong(idStr)));
		}
		emailDateService.makeEmFullName(eList);
		return eList;
	}
	
	@RequestMapping("/view/{cwb}")
	public String view(@PathVariable("cwb") String cwb, Model model) {
		logger.info(cwb);
		return "orderflow/view";
	}
}
