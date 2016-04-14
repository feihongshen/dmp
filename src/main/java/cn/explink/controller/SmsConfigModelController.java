package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.sdk.test.SingletonClient;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SmsConfigDAO;
import cn.explink.dao.SmsConfigModelDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.SmsConfig;
import cn.explink.domain.SmsConfigModel;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SmsConfigService;
import cn.explink.service.SmsSendService;

@RequestMapping("/smsconfigmodel")
@Controller
public class SmsConfigModelController {

	@Autowired
	SmsConfigModelDAO smsconfigModelDAO;
	@Autowired
	SmsConfigService smsConfigService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SmsConfigDAO smsconfigDAO;
	@Autowired
	SmsSendService smsSendService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private static Logger logger = LoggerFactory.getLogger(SmsConfigModelController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/showAll")
	public String show(Model model) {
		List<SmsConfigModel> smsList = smsconfigModelDAO.getAllSmsConfig();
		Map<Long, List<String>> customerMap = new HashMap<Long, List<String>>();
		for (SmsConfigModel smsConfigModel : smsList) {
			String customerids = smsConfigModel.getCustomerids();
			List<String> list = new ArrayList<String>();
			String[] customeridsStr = customerids.split(",");
			for (String str : customeridsStr) {
				list.add(str);
			}
			customerMap.put(smsConfigModel.getFlowordertype(), list);

		}
		Map<Long, List<String>> branchMap = new HashMap<Long, List<String>>();
		for (SmsConfigModel smsConfigModel : smsList) {
			String branchids = smsConfigModel.getBranchids();
			List<String> list = new ArrayList<String>();
			String[] branchidstr = branchids.split(",");
			for (String str : branchidstr) {
				list.add(str);
			}
			branchMap.put(smsConfigModel.getFlowordertype(), list);
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerMap", customerMap);
		model.addAttribute("smsList", smsList);
		model.addAttribute("branchList", branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue()));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "/sMSSetModel/smsModelview";
	}

	@RequestMapping("/setsmsview")
	public String list(Model model, @RequestParam(value = "channel", required = false, defaultValue = "0") int channel) {
		SmsConfig smsConfig = smsconfigDAO.getAllSmsConfig(channel);
		int checksmsnull = (smsConfig == null ? 0 : 1);
		model.addAttribute("checksmsnull", checksmsnull);
		model.addAttribute("smsconfig", smsConfig);
		// 获取短信余额 start
		if (smsConfig != null) {
			if (channel == 1) {// 当当短信渠道亿美获取余额
				try {
					String balance = SingletonClient.getClient(smsConfig.getName(), smsConfig.getPassword()).getBalance();
					if (balance.indexOf(".") > -1) {
						model.addAttribute("smsCount", Double.parseDouble(balance) * 10);
					} else {
						model.addAttribute("smsCount", "获取失败");
					}
				} catch (Exception e) {
					model.addAttribute("smsCount", "获取失败");
				}
			} else {
				model.addAttribute("smsCount", smsSendService.getResidualCount(smsConfig.getName(), smsConfig.getPassword()));
			}
		} else {
			model.addAttribute("smsCount", "获取失败");
		}
		// 获取短信余额 end
		model.addAttribute("channel", channel);
		return "/sMSSetModel/smsSetModel";
	}

	@RequestMapping("/createORsave")
	public String create(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "channel", required = false, defaultValue = "0") int channel) {
		response.setCharacterEncoding("utf-8");
		SmsConfig s = new SmsConfig();
		s.setChannel(channel);
		model.addAttribute("channel", channel);
		String isCreate = request.getParameter("isCreate");
		String checksmsnull = request.getParameter("checksmsnull");

		if ("1".equals(isCreate)) {// 创建账号
			if ("1".equals(checksmsnull)) {
				s = smsconfigDAO.getAllSmsConfig(channel) == null ? new SmsConfig() : smsconfigDAO.getAllSmsConfig(channel);
				s.setName(request.getParameter("name"));
				s.setPassword(request.getParameter("pass"));
				s.setIsOpen(1);
			} else {
				s.setName(request.getParameter("name"));
				s.setPassword(request.getParameter("pass"));
				s.setIsOpen(1);
			}
		} else {
			s = smsconfigDAO.getAllSmsConfig(channel) == null ? new SmsConfig() : smsconfigDAO.getAllSmsConfig(channel);
			s.setWarningcount(Long.parseLong((request.getParameter("warningcount") == null || "".equals(request.getParameter("warningcount"))) ? "0" : request.getParameter("warningcount")));
			s.setPhone(request.getParameter("phone"));
			s.setTemplet(request.getParameter("templet"));
			s.setWarningcontent(request.getParameter("warningcontent"));
			s.setTemplatecontent(request.getParameter("templatecontent"));
			s.setMonitor(Long.parseLong((request.getParameter("monitor") == null || "".equals(request.getParameter("monitor"))) ? "0" : request.getParameter("monitor")));
		}
		if ("0".equals(checksmsnull)) {
			smsconfigDAO.creSmsConfig(s.getName(), s.getPassword(), s.getWarningcount(), s.getPhone(), s.getTemplet(), s.getMonitor(), s.getWarningcontent(), s.getIsOpen(), s.getTemplatecontent(),
					channel);
		} else if ("1".equals(checksmsnull)) {
			smsconfigDAO.saveSmsConfig(s.getName(), s.getPassword(), s.getWarningcount(), s.getPhone(), s.getTemplet(), s.getMonitor(), s.getWarningcontent(), s.getIsOpen(), s.getTemplatecontent(),
					channel, s.getId());
		}
		return list(model, channel);
	}

	@RequestMapping("/update")
	public @ResponseBody String update(Model model, @RequestParam(value = "flowordertype", defaultValue = "-1", required = false) long flowordertype,
			@RequestParam(value = "money", defaultValue = "-1", required = false) String money, @RequestParam(value = "templatecontent", defaultValue = "", required = false) String templatecontent) {
		try {
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				sms.setMoney(new BigDecimal(money));
				sms.setTemplatecontent(templatecontent);
				sms.setUsername(getSessionUser().getRealname());
				sms.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				smsconfigModelDAO.updateSmsConfigModel(sms);
				model.addAttribute("ifUpdate", 1);
				return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			}
		} catch (Exception e) {
			model.addAttribute("ifUpdate", 0);
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"更新失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/del")
	public @ResponseBody String del(Model model, @RequestParam(value = "flowordertype", defaultValue = "-1", required = false) long flowordertype) {
		try {
			long result = smsconfigModelDAO.delSmsConfigByFlowtype(flowordertype);
			if (result > 0) {
				model.addAttribute("ifUpdate", 1);
				return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			}
		} catch (Exception e) {
			model.addAttribute("ifUpdate", 0);
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"更新失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/update1")
	public @ResponseBody String update1(Model model, @RequestParam(value = "flowordertype", defaultValue = "-1", required = false) long flowordertype,
			@RequestParam(value = "branchids", defaultValue = "", required = false) String[] branchids) {
		String branchidstr = "";
		for (String branchid : branchids) {
			branchidstr += branchid + ",";
		}
		if (branchidstr.length() > 0) {
			branchidstr = branchidstr.substring(0, branchidstr.length() - 1);
		}
		try {
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				sms.setBranchids(branchidstr);
				sms.setUsername(getSessionUser().getRealname());
				sms.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				smsconfigModelDAO.updateSmsConfigModel(sms);
				model.addAttribute("ifUpdate", 1);
				return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			}
		} catch (Exception e) {
			model.addAttribute("ifUpdate", 0);
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"更新失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/update2")
	public @ResponseBody String update2(Model model, @RequestParam(value = "flowordertype", defaultValue = "-1", required = false) long flowordertype,
			@RequestParam(value = "customerids", defaultValue = "", required = false) String[] customerids) {
		String customeridstr = "";
		for (String customerid : customerids) {
			customeridstr += customerid + ",";
		}
		if (customeridstr.length() > 0) {
			customeridstr = customeridstr.substring(0, customeridstr.length() - 1);
		}
		try {
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				sms.setCustomerids(customeridstr);
				sms.setUsername(getSessionUser().getRealname());
				sms.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				smsconfigModelDAO.updateSmsConfigModel(sms);
				model.addAttribute("ifUpdate", 1);
				return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			}
		} catch (Exception e) {
			model.addAttribute("ifUpdate", 0);
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"更新失败\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/savesmsconfig")
	public @ResponseBody String savesmsconfig(Model model, @RequestParam("pass") String pass) {
		if ("explink".equals(pass)) {
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		}
		return "{\"errorCode\":1,\"error\":\"密码有误\"}";
	}

	@RequestMapping("/updateOpen")
	public @ResponseBody String updateOpen(Model model, @RequestParam("isOpen") long isOpen, @RequestParam(value = "channel", required = false, defaultValue = "0") int channel) {

		long result = smsconfigDAO.updateIsOpen(isOpen, channel);

		if (result > 0) {
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		}
		return "{\"errorCode\":1,\"error\":\"更新失败\"}";
	}

	@RequestMapping("/updateMonitor")
	public @ResponseBody String updateMoner(Model model, @RequestParam("isMonitor") long isMonitor, @RequestParam(value = "channel", required = false, defaultValue = "0") int channel) {

		long result = smsconfigDAO.updateIsMonitor(isMonitor, channel);

		if (result > 0) {
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		}
		return "{\"errorCode\":1,\"error\":\"更新失败\"}";
	}

	@RequestMapping("/getCustomerids")
	public @ResponseBody String getCustomerids(Model model, @RequestParam("flowordertype") long flowordertype) {
		if (flowordertype > 0) {
			List<Customer> customerlist = new ArrayList<Customer>();
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				String customerids = sms.getCustomerids() == null || "".equals(sms.getCustomerids()) ? "-1" : sms.getCustomerids();
				String[] customeridsStr = customerids.split(",");
				for (String str : customeridsStr) {
					Customer c = customerDAO.getCustomerById(Long.parseLong(str));
					if (c != null && c.getCustomerid() > 0) {
						c.setIfeffectflag(10000);
						customerlist.add(c);
					}
				}
				customerlist.addAll(customerDAO.getCustomerByNoInIds(customerids));
				return JSONArray.fromObject(customerlist).toString();
			} else {
				customerlist.addAll(customerDAO.getAllCustomers());
				return JSONArray.fromObject(customerlist).toString();
			}

		} else {
			return "[]";
		}

	}

	@RequestMapping("/getBranchids")
	public @ResponseBody String getBranchids(Model model, @RequestParam("flowordertype") long flowordertype) {
		if (flowordertype > 0) {
			JSONObject o = new JSONObject();
			JSONArray oList = new JSONArray();
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				String branchids = sms.getBranchids() == null || "".equals(sms.getBranchids()) ? "-1" : sms.getBranchids();
				String[] customeridsStr = branchids.split(",");
				for (String str : customeridsStr) {
					Branch c = branchDAO.getBranchByBranchid(Long.parseLong(str));
					;
					if (c != null && c.getBranchid() > 0) {
						c.setSitetype(10000);
						o.put("branchid", c.getBranchid());
						o.put("branchname", c.getBranchname());
						o.put("sitetype", c.getSitetype());
						oList.add(o);
					}
				}
				List<Branch> blist = branchDAO.getBranchNotInBranchid(branchids);
				if (blist != null && blist.size() > 0) {
					for (Branch branch : blist) {
						o.put("branchid", branch.getBranchid());
						o.put("branchname", branch.getBranchname());
						o.put("sitetype", branch.getSitetype());
						oList.add(o);
					}
				}
				return oList.toString();
			} else {
				List<Branch> blist = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
				if (blist != null && blist.size() > 0) {
					for (Branch branch : blist) {
						o.put("branchid", branch.getBranchid());
						o.put("branchname", branch.getBranchname());
						o.put("sitetype", branch.getSitetype());
						oList.add(o);
					}
				}
				return oList.toString();
			}

		} else {
			return "[]";
		}

	}

	@RequestMapping("/getSms")
	public @ResponseBody String getSms(Model model, @RequestParam("flowordertype") long flowordertype) {
		if (flowordertype > 0) {
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms != null) {
				return JSONObject.fromObject(sms).toString();
			} else {
				JSONObject o = new JSONObject();
				o.put("money", -1);
				o.put("templatecontent", "");
				return o.toString();
			}

		} else {
			JSONObject o = new JSONObject();
			o.put("money", -1);
			o.put("templatecontent", "");
			return o.toString();
		}

	}

	@RequestMapping("/alertsms")
	public String alertsms() {
		return "/sMSConf/save";
	}

	@RequestMapping("/saveSms")
	public @ResponseBody String savesmsconfig(Model model, @RequestParam(value = "flowordertype", defaultValue = "-1", required = false) long flowordertype,
			@RequestParam(value = "customerids", defaultValue = "", required = false) String[] customerids, @RequestParam(value = "branchids", defaultValue = "", required = false) String[] branchids,
			@RequestParam(value = "money", defaultValue = "-1", required = false) String money, @RequestParam(value = "templatecontent", defaultValue = "", required = false) String templatecontent) {
		String branchidstr = "";
		String customeridstr = "";
		for (String customerid : customerids) {
			customeridstr += customerid + ",";
		}
		for (String branchid : branchids) {
			branchidstr += branchid + ",";
		}
		if (customeridstr.length() > 0) {
			customeridstr = customeridstr.substring(0, customeridstr.length() - 1);
		}
		if (branchidstr.length() > 0) {
			branchidstr = branchidstr.substring(0, branchidstr.length() - 1);
		}
		try {
			SmsConfigModel sms = smsconfigModelDAO.getSmsConfigByFlowtype(flowordertype);
			if (sms == null) {
				sms = new SmsConfigModel();
				sms.setBranchids(branchidstr);
				sms.setCustomerids(customeridstr);
				sms.setFlowordertype(flowordertype);
				sms.setMoney(new BigDecimal(money));
				sms.setTemplatecontent(templatecontent);
				sms.setUsername(getSessionUser().getRealname());
				smsconfigModelDAO.creSmsConfigModel(sms);
			} else {
				sms.setBranchids(branchidstr);
				sms.setCustomerids(customeridstr);
				sms.setFlowordertype(flowordertype);
				sms.setMoney(new BigDecimal(money));
				sms.setTemplatecontent(templatecontent);
				sms.setUsername(getSessionUser().getRealname());
				sms.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				smsconfigModelDAO.updateSmsConfigModel(sms);
			}
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		} catch (Exception e) {
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"保存失败\"}";
		}

	}

	/**
	 * 亿美短信渠道首次使用需要注册
	 * 
	 * @param channel
	 * @return
	 */
	@RequestMapping("/regist")
	public @ResponseBody int regist(@RequestParam(value = "channel", defaultValue = "1", required = false) int channel) {
		SmsConfig smsConfig = smsconfigDAO.getAllSmsConfig(channel);
		return SingletonClient.getClientInit(smsConfig.getName(), smsConfig.getPassword()).registEx(smsConfig.getPassword());
	}

}
