package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountCwbFareDAO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbFare;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.ExplinkUserDetail;

/**
 * 上门退运费结算管理
 *
 */

@Controller
@RequestMapping("/accountcwbfare")
public class AccountCwbFareController {
	@Autowired
	UserDAO userDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	AccountCwbFareDAO accountCwbFareDAO;

	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	
	@RequestMapping("/payfare")
	public @ResponseBody String payfare(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "girofee", required = false, defaultValue = "") BigDecimal girofee, @RequestParam(value = "girouser", required = false, defaultValue = "") String girouser,
			@RequestParam(value = "girocardno", required = false, defaultValue = "") String girocardno, @RequestParam(value = "cashfee", required = false, defaultValue = "") BigDecimal cashfee,
			@RequestParam(value = "cashuser", required = false, defaultValue = "") String cashuser) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String payuptime = df.format(new Date());
			AccountCwbFare accountCwbFare = new AccountCwbFare();
			accountCwbFare.setPayuptime(payuptime);
			accountCwbFare.setUserid(getSessionUser().getUserid());
			accountCwbFare.setCashfee(cashfee);
			accountCwbFare.setCashuser(cashuser);
			accountCwbFare.setGirocardno(girocardno);
			accountCwbFare.setGirofee(girofee);
			accountCwbFare.setGirouser(girouser);
			long fareid = accountCwbFareDAO.createAccountCwbFare(accountCwbFare);
			accountCwbFareDetailDAO.saveAccountCwbFareDetailByCwb(payuptime, fareid, cwbs);

			return "{\"errorCode\":0,\"error\":\"提交成功\",\"fareid\":\"" + fareid + "\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}
	
	
	@RequestMapping("/add")
	public String add(Model model) {
		return "accountfare/submitaAlert";
	}
}
