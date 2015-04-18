package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReturnCwbsDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ReturnCwbs;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReturnCwbsTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;

@RequestMapping("/returnCwbs")
@Controller
public class ReturnCwbsController {

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	ReturnCwbsDAO returnCwbsDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 返单出站统计
	 * 
	 * @return
	 */
	@RequestMapping("/getReturnCwbsbackexportSum/{customerandid}")
	public @ResponseBody JSONObject getReturnCwbsbackexportSum(@PathVariable("customerandid") String customerandid) {
		JSONObject obj = new JSONObject();
		long customereid=Long.parseLong(customerandid);
		// 显示每天截止到”当前时间”的数据。
		String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		long count = returnCwbsDAO.getReturnCwbsByTypeAndBranchidAndIsnowCount(ReturnCwbsTypeEnum.DaiFanDanChuZhan.getValue(), getSessionUser().getBranchid(), 0, nowtime,customereid);
		obj.put("size", count);
		return obj;
	}

	/**
	 * 进入返单出站页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/returnCwbsbackexport")
	public String returnCwbsbackexport(Model model, @RequestParam(value = "flag", required = false, defaultValue = "1") long flag,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "timetype", required = false, defaultValue = "3") long timetype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		List<Customer> customerList = customerDAO.getAllCustomers();
		List<Branch> bList = cwborderService.getNextPossibleBranches(getSessionUser());
		List<Branch> removeList = new ArrayList<Branch>();
		for (Branch b : bList) {// 去掉中转站
			if (b.getSitetype() == BranchEnum.KuFang.getValue() || b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				removeList.add(b);
			}
		}

		// 显示每天截止到”当前时间”的数据。
		String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";

		List<CwbOrder> weichuzhanlist = cwbDAO.getCwbOrderByReturncwbsforTypeAndBranchidAndIsnow(ReturnCwbsTypeEnum.DaiFanDanChuZhan.getValue(), getSessionUser().getBranchid(), 0, nowtime, timetype,
				starttime, endtime, customerid);
		List<CwbOrder> yichuzhanlist = cwbDAO.getCwbOrderByReturncwbsforTypeAndBranchidAndIsnow(ReturnCwbsTypeEnum.FanDanChuZhan.getValue(), getSessionUser().getBranchid(), 0, nowtime, timetype,
				starttime, endtime, customerid);

		model.addAttribute("weichuzhanlist", weichuzhanlist);
		model.addAttribute("yichuzhanlist", yichuzhanlist);
		model.addAttribute("yichuzhannums", (yichuzhanlist != null && !yichuzhanlist.isEmpty()) ? yichuzhanlist.size() : 0);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("branchlist", removeList);
		model.addAttribute("customerid", customerid);
		model.addAttribute("customerList", customerDAO.getAllCustomersByExistRules());// 经销商
		return "returnCwbs/returnCwbsbackexport";
	}

	/**
	 * 返单出站功能
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/cwbreturnCwbsbackexport/{cwb}")
	public @ResponseBody JSONObject cwbreturnCwbsbackexport(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		try {
			cwb = cwborderService.translateCwb(cwb);
			CwbOrder co = cwbDAO.getCwbByCwbLock(cwb);

			if (co == null) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			long isFeedbackcwb = customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : customerDAO.getCustomerById(co.getCustomerid()).getIsFeedbackcwb();
			// 验证是否是配送成功的订单，并且所在供货商允许返单操作
			if (!(co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && co.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && isFeedbackcwb == 1)) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Fei_Dai_Fan_Dan);
			}

			List<ReturnCwbs> returncwbsList = returnCwbsDAO.getReturnCwbsByCwbAndType(cwb, ReturnCwbsTypeEnum.FanDanChuZhan.getValue());
			if (returncwbsList.size() > 0) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
			}
			ReturnCwbs returnCwbs = new ReturnCwbs();
			returnCwbs.setBranchid(getSessionUser().getBranchid());
			returnCwbs.setCustomerid(co.getCustomerid());
			returnCwbs.setCwb(co.getCwb());
			returnCwbs.setOpscwbid(co.getOpscwbid());
			returnCwbs.setType(ReturnCwbsTypeEnum.FanDanChuZhan.getValue());
			returnCwbs.setUserid(getSessionUser().getUserid());
			returnCwbs.setTobranchid(branchid);
			returnCwbs.setIsnow("0");

			returnCwbsDAO.creAndUpdateReturnCwbs(returnCwbs);

			obj.put("statuscode", CwbOrderPDAEnum.OK.getCode());
			obj.put("errorinfo", "成功返单");
		} catch (CwbException ce) {
			obj.put("statuscode", ce.getError().getValue());
			obj.put("errorinfo", ce.getMessage());
		}

		return obj;
	}

	/**
	 * 返单入库统计
	 * 
	 * @return
	 */
	@RequestMapping("/getReturnCwbsintowarhouseSum")
	public @ResponseBody JSONObject getReturnCwbsintowarhouseSum() {
		JSONObject obj = new JSONObject();

		long returncwbsFandanweirukuCount = returnCwbsDAO.getReturnCwbsByTypeAndTobranchidCount(ReturnCwbsTypeEnum.FanDanChuZhan.getValue(), getSessionUser().getBranchid());
		obj.put("size", returncwbsFandanweirukuCount);
		return obj;
	}

	/**
	 * 进入返单入库页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/returnCwbsintowarhouse")
	public String returnCwbsintowarhouse(Model model, @RequestParam(value = "flag", required = false, defaultValue = "1") long flag,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "timetype", required = false, defaultValue = "4") long timetype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		List<Customer> customerList = customerDAO.getAllCustomers();

		// 显示每天截止到”当前时间”的数据。
		String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";

		List<CwbOrder> weifandanrukulist = cwbDAO.getCwbOrderByReturncwbsforTypeAndToBranchidAndIsnow(ReturnCwbsTypeEnum.FanDanChuZhan.getValue(), getSessionUser().getBranchid(), nowtime, timetype,
				starttime, endtime, customerid);
		List<CwbOrder> yifandanrukulist = cwbDAO.getCwbOrderByReturncwbsforTypeAndToBranchidAndIsnow(ReturnCwbsTypeEnum.FanDanRuKu.getValue(), 0, nowtime, timetype, starttime, endtime, customerid);

		List<CwbOrder> yifandanrukulistView = new ArrayList<CwbOrder>();
		if (yifandanrukulist != null && !yifandanrukulist.isEmpty()) {
			for (CwbOrder cwbOrder : yifandanrukulist) {
				ReturnCwbs returnCwbs = returnCwbsDAO.getReturnCwbsByTypeAndTobranchid(ReturnCwbsTypeEnum.FanDanChuZhan.getValue(), getSessionUser().getBranchid(), cwbOrder.getCwb());
				cwbOrder.setShenhetime(returnCwbs.getCreatetime());
				yifandanrukulistView.add(cwbOrder);
			}
		}

		model.addAttribute("weifandanrukulist", weifandanrukulist);
		model.addAttribute("yifandanrukulist", yifandanrukulistView);
		model.addAttribute("yifandannums", (yifandanrukulist != null && !yifandanrukulist.isEmpty()) ? yifandanrukulist.size() : 0);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("customerList", customerDAO.getAllCustomersByExistRules());// 供货商
		return "returnCwbs/returnCwbsintowarhouse";
	}

	/**
	 * 返单入库功能
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/cwbreturnCwbsintowarhouse/{cwb}")
	public @ResponseBody JSONObject cwbreturnCwbsintowarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb) {
		JSONObject obj = new JSONObject();
		try {
			cwb = cwborderService.translateCwb(cwb);
			CwbOrder co = cwbDAO.getCwbByCwbLock(cwb);

			if (co == null) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			List<ReturnCwbs> returncwbsList = returnCwbsDAO.getReturnCwbsByCwbAndType(cwb, ReturnCwbsTypeEnum.FanDanChuZhan.getValue());

			List<ReturnCwbs> returncwbsRukuList = returnCwbsDAO.getReturnCwbsByCwbAndType(cwb, ReturnCwbsTypeEnum.FanDanRuKu.getValue());
			// 验证是否是非返单货还是重复扫描
			if (returncwbsList.size() == 0) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Fei_Dai_Fan_Dan);
			}
			if (returncwbsRukuList.size() > 0) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
			}

			ReturnCwbs returnCwbs = new ReturnCwbs();
			returnCwbs.setBranchid(getSessionUser().getBranchid());
			returnCwbs.setCustomerid(co.getCustomerid());
			returnCwbs.setCwb(co.getCwb());
			returnCwbs.setOpscwbid(co.getOpscwbid());
			returnCwbs.setType(ReturnCwbsTypeEnum.FanDanRuKu.getValue());
			returnCwbs.setUserid(getSessionUser().getUserid());
			returnCwbs.setTobranchid(0);
			returnCwbs.setIsnow(System.currentTimeMillis() + "");

			returnCwbsDAO.creAndUpdateReturnCwbs(returnCwbs);

			obj.put("statuscode", CwbOrderPDAEnum.OK.getCode());
			obj.put("errorinfo", "成功返单");
		} catch (CwbException ce) {
			obj.put("statuscode", ce.getError().getValue());
			obj.put("errorinfo", ce.getMessage());
		}

		return obj;
	}

}
