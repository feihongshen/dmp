package cn.explink.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CommonDAO;
import cn.explink.dao.CommonModelDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/changecwb")
public class ChangeCwbController {
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CommonModelDAO commonmodelDAO;

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbOrderService cwborderService;

	@RequestMapping("/tochangecwbforwegiht")
	public String tochangecwbforwegiht(Model model) {
		return "changecwb/changecwbforwegiht";
	}

	@RequestMapping("/tochangecwbforwegihttopaohuo")
	public String tochangecwbforwegihttopaohuo(Model model) {
		return "changecwb/changecwbforwegihttopaohuo";
	}

	@RequestMapping("/toprintcwb")
	public String toprintcwb(Model model) {
		List<Common> commonlist = commonDAO.getAllCommons();
		// List<Common> commonlist = commonDAO.getCommonsByModelNames();
		model.addAttribute("commonlist", commonlist);
		return "changecwb/printcwb";
	}

	@RequestMapping("/toprintandweightcwb")
	public String toprintandweightcwb(Model model) {
		List<Common> commonlist = commonDAO.getAllCommons();
		model.addAttribute("commonlist", commonlist);
		return "changecwb/printandweightcwb";
	}

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 订单称重
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/changecwbforwegiht")
	public @ResponseBody String changecwbforwegiht(Model model, HttpServletRequest request) {
		String carrealweight = request.getParameter("carrealweight");
		String length = request.getParameter("length");
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		long scanways = Long.parseLong(request.getParameter("scanways"));
		String scancwb = request.getParameter("scancwb");
		long syncupdate = request.getParameter("syncupdate") == "" ? 0 : Long.parseLong(request.getParameter("syncupdate"));
		String cwb = cwborderService.translateCwb(scancwb);

		CwbOrder co = null;
		if (scanways == 1) {
			co = cwbDao.getCwbByCwb(cwb);
		} else {
			co = cwbDao.getCwbByCommoncwb(cwb);
		}
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\"该订单不存在\"}";
		} else {
			cwbDao.saveCwbForChangecwb(carrealweight, length + "," + width + "," + height, scanways, cwb);
			if (syncupdate == 1) {
				cwborderService.intoWarehous(getSessionUser(), cwb, scancwb, co.getCustomerid(), 0, 0, "", "", false);
			}

			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}

	}

	/**
	 * 按泡货规格称重
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/changecwbforwegihttopaohuo")
	public @ResponseBody String changecwbforwegihttopaohuo(Model model, HttpServletRequest request) {
		String carrealweight = request.getParameter("carrealweight");
		String carsize = request.getParameter("carsize");
		long scanways = Long.parseLong(request.getParameter("scanways"));
		String scancwb = request.getParameter("scancwb");

		CwbOrder co = null;
		if (scanways == 1) {
			co = cwbDao.getCwbByCwb(scancwb);
		} else {
			co = cwbDao.getCwbByCommoncwb(scancwb);
		}
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\"该订单不存在\"}";
		} else {
			cwbDao.saveCwbForChangecwb(carrealweight, carsize, scanways, scancwb);
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	/**
	 * 打印面单扫描订单号
	 * 
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getCommonByCwb/{cwb}")
	public @ResponseBody JSONObject getCommonByCwb(Model model, @PathVariable("cwb") String cwb) {
		CwbOrder co = cwbDao.getCwbByCwb(cwb);
		JSONObject data = new JSONObject();

		if (co == null) {
			model.addAttribute("common", null);
			data.put("error", "该订单不存在");
		} else {
			List<Customer> customerlist = customerDAO.getAllCustomers();

			Common common = null;
			JSONObject commonmodel = null;
			if (co.getModelname() != null && co.getModelname().length() > 0) {
				commonmodel = commonmodelDAO.getCommonModelJsonByModelname(co.getModelname());
				common = commonDAO.getCommonByCommonName(co.getModelname());

				common = commonDAO.getCommonByCommonName(co.getModelname());

			}

			JSONObject cwborder = new JSONObject();
			cwborder.put("sendcarname", co.getSendcarname());
			cwborder.put("cwb", co.getCwb());
			cwborder.put("customerid", co.getCustomerid());
			cwborder.put("consigneename", co.getConsigneename());
			cwborder.put("consigneemobile", co.getConsigneemobile());
			cwborder.put("consigneeaddress", co.getConsigneeaddress());
			cwborder.put("carrealweight", co.getCarrealweight());
			cwborder.put("carsize", co.getCarsize());
			cwborder.put("consigneepostcode", co.getConsigneepostcode());
			cwborder.put("cwbremark", co.getCwbremark());

			/*
			 * model.addAttribute("common", common);
			 * model.addAttribute("cwborder", co);
			 * model.addAttribute("customerlist", customerlist);
			 */

			data.put("error", "0");
			data.put("common", common);
			data.put("commonmodel", commonmodel);
			data.put("cwborder", cwborder);
			data.put("customerlist", customerlist);
		}
		return data;
	}

	/**
	 * 打印面单
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 */
	/*
	 * @RequestMapping("/printCwb/{cwb}") public @ResponseBody JSONObject
	 * printCwb(Model model,@PathVariable("cwb")String cwb){ JSONObject data =
	 * new JSONObject(); CwbOrder co = cwbDao.getCwbByCwb(cwb); List<Customer>
	 * customerlist = customerDAO.getAllCustomers();
	 * 
	 * CommonModel commonmodel = null; if(co.getModelname().length()>0){
	 * commonmodel =
	 * commonmodelDAO.getCommonModelByModelname(co.getModelname()); }
	 * 
	 * JSONObject cwborder = new JSONObject(); cwborder.put("sendcarname",
	 * co.getSendcarname()); cwborder.put("cwb", co.getCwb());
	 * cwborder.put("customerid", co.getCustomerid());
	 * cwborder.put("consigneename", co.getConsigneename());
	 * cwborder.put("consigneemobile", co.getConsigneemobile());
	 * cwborder.put("consigneeaddress", co.getConsigneeaddress());
	 * cwborder.put("carrealweight", co.getcarrealweight());
	 * cwborder.put("carsize", co.getCarsize());
	 * cwborder.put("consigneepostcode", co.getConsigneepostcode());
	 * cwborder.put("cwbremark", co.getCwbremark());
	 * 
	 * model.addAttribute("common", common); model.addAttribute("cwborder", co);
	 * model.addAttribute("customerlist", customerlist); data.put("common",
	 * commonmodel); data.put("cwborder",cwborder); data.put("customerlist",
	 * customerlist);
	 * 
	 * return data;
	 * 
	 * }
	 */

	/**
	 * 打印面单带称重扫描运单号
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/printCwbAndWeightForScan/{cwb}")
	public @ResponseBody String printCwbAndWeightForScan(Model model, HttpServletRequest request, @PathVariable("cwb") String cwb) {
		try {
			String carrealweight = request.getParameter("carrealweight");
			String length = request.getParameter("length");
			String width = request.getParameter("width");
			String height = request.getParameter("height");
			String commoncwb = request.getParameter("commoncwb");

			cwbDao.saveCwbForPrintChangecwb(carrealweight, length + "," + width + "," + height, commoncwb, cwb);
			return "{\"errorCode\":0,\"error\":\"扫描成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"扫描出现异常\"}";
		}
	}

	/**
	 * 打印面单扫描运单号
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/printCwbForScan/{cwb}")
	public @ResponseBody String printCwbForScan(Model model, HttpServletRequest request, @PathVariable("cwb") String cwb) {
		try {
			String commoncwb = request.getParameter("commoncwb");

			cwbDao.saveCwbForPrintChangecwb(commoncwb, cwb);
			return "{\"errorCode\":0,\"error\":\"扫描成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"扫描出现异常\"}";
		}
	}

	/**
	 * 打印面单（带称重）
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 */
	@RequestMapping("/printAndWeightCwb/{cwb}")
	public void printAndWeightCwb(Model model, HttpServletRequest request, @PathVariable("cwb") String cwb) {
		String carrealweight = request.getParameter("carrealweight");
		String length = request.getParameter("length");
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		CwbOrder co = cwbDao.getCwbByCwb(cwb);
		co.setCarrealweight(new BigDecimal(carrealweight));
		co.setCarsize(length + "," + width + "," + height);

		model.addAttribute("cwborder", co);
	}
}
