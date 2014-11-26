package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.OrderArriveTime;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrderArriveTimeService;
import cn.explink.util.Page;

/**
 * 到货时间
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping("/orderarrivetime")
public class OrderArriveTimeController {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderArriveTimeService orderArriveTimeService;
	@Autowired
	OrderArriveTimeDAO orderArriveTimeDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	BranchDAO branchDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 到车时间确认列表页
	 * 
	 * @param model
	 * @param request
	 * @param page
	 * @param cwb
	 * @param flag
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public String list(Model model, HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "flag", defaultValue = "0", required = false) long flag, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		if (flag > 0) {
			StringBuffer cwbs = new StringBuffer();
			// 处理订单号
			if (!"".equals(cwb.trim())) {
				for (String cwbStr : cwb.split("\r\n")) {
					if ("".equals(cwbStr.trim())) {
						continue;
					}
					cwbs.append("'").append(cwbStr).append("',");
				}
				cwb = cwbs.substring(0, cwbs.lastIndexOf(","));
			}

			// 查询出数据
			List<OrderArriveTime> oatList = orderArriveTimeDAO.getOrderArriveTimePage(page, getSessionUser().getBranchid(), cwb.trim(), starttime, endtime, flag);
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();

			model.addAttribute("oatList", orderArriveTimeService.getOrderArriveTimeList(oatList, customerList, branchList, customerWareHouseList));
			model.addAttribute("page_obj", new Page(orderArriveTimeDAO.getOrderArriveTimePageCount(getSessionUser().getBranchid(), cwb.trim(), starttime, endtime, flag), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}
		return "orderarrivetime/list";
	}

	/**
	 * 提交到车时间
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @param flag
	 * @param starttime
	 * @param endtime
	 * @param arrivetime
	 * @return
	 */
	@RequestMapping("/save")
	public @ResponseBody String save(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "flag", defaultValue = "0", required = false) long flag, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "arrivetime", required = false, defaultValue = "") String arrivetime) {
		try {
			StringBuffer cwbs = new StringBuffer();
			// 处理订单号
			if (!"".equals(cwb.trim())) {
				for (String cwbStr : cwb.split("\r\n")) {
					if ("".equals(cwbStr.trim())) {
						continue;
					}
					cwbs.append("'").append(cwbStr).append("',");
				}
				cwb = cwbs.substring(0, cwbs.lastIndexOf(","));
			}
			// 查询出数据
			List<OrderArriveTime> oatList = orderArriveTimeDAO.getOrderArriveTimeList(getSessionUser().getBranchid(), cwb.trim(), starttime, endtime, flag);
			// 更新到货时间
			orderArriveTimeService.save(oatList, arrivetime);
			return "{\"errorCode\":0,\"error\":\"提交成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 导出Excel
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param flag
	 * @param starttime
	 * @param endtime
	 * @param arrivetime
	 */
	@RequestMapping("/export")
	public void export(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "flag", defaultValue = "0", required = false) long flag, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "arrivetime", required = false, defaultValue = "") String arrivetime) {
		StringBuffer cwbs = new StringBuffer();
		// 处理订单号
		if (!"".equals(cwb.trim())) {
			for (String cwbStr : cwb.split("\r\n")) {
				if ("".equals(cwbStr.trim())) {
					continue;
				}
				cwbs.append("'").append(cwbStr).append("',");
			}
			cwb = cwbs.substring(0, cwbs.lastIndexOf(","));
		}
		orderArriveTimeService.export(request, response, getSessionUser(), cwb.trim(), flag, starttime, endtime);
	}
}
