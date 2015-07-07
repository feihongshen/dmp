package cn.explink.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SmsManageDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SmsSendService;
import cn.explink.util.HttpUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/sms")
public class SmsSendController {

	@Autowired
	SmsSendService smsSendService;
	@Autowired
	SmsManageDao smsManageDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(SmsSendController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/send")
	public String smsSend(Model model, @RequestParam(value = "mobiles", required = false, defaultValue = "") String mobiles,
			@RequestParam(value = "smsRemack", required = false, defaultValue = "") String smsRemack, HttpServletRequest request) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("使用短信群发功能：用户名：{},站点：{},手机号：" + mobiles + ",短信内容:" + smsRemack + "", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("使用短信群发功能：获取用户名，站点异常");
		}
		String msg = "";
		try {
			if (mobiles.trim().length() > 0) {
				logger.info("短信发送，单量：{}", mobiles.split("\r\n").length);
				int i = 0;
				for (String cwb : mobiles.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					List<CwbOrder> list = cwbDAO.getAllCwbOrderByCwb(cwb.trim());
					if (list != null && list.size() > 0) {
						for (CwbOrder cwbOrder : list) {
							if (cwbOrder.getConsigneemobile() != null && !"".equals(cwbOrder.getConsigneemobile())) {
								logger.info("短信发送，单号：{} 手机号：{}", cwb.trim(), cwbOrder.getConsigneemobile());
								try {
									msg = smsSendService.sendSms(cwbOrder.getConsigneemobile(), smsRemack, 1, cwbOrder.getCustomerid(), cwbOrder.getConsigneename(), getSessionUser().getUserid(),
											HttpUtil.getUserIp(request));
									logger.info("短信发送，单号：{}  结果：{}", cwb.trim(), msg);
									if ("发送短信成功".equals(msg)) {
										i++;
									}
								} catch (UnsupportedEncodingException e) {
									logger.error("短信发送，异常", e);
								}
							} else {
								msg = "没有找到相关手机号";
							}
						}
					}
				}
				logger.info("短信发送，成功单数：{}", i);
			}
		} catch (Exception e) {
			logger.error("短信发送，异常", e);
		}
		model.addAttribute("msg", msg);
		return "smsSendto/smsSend";
	}

	@RequestMapping("/view")
	public String view(Model model) {
		model.addAttribute("msg", "");
		return "smsSendto/smsSend";
	}

	@RequestMapping("/viewrole")
	public String viewrole(Model model) {
		model.addAttribute("roles", roleDAO.getRoles());
		return "smsSendto/smsSendRole";
	}

	@RequestMapping("/viewcwb")
	public String viewcwb(Model model) {
		return "smsSendto/smsSendCwb";
	}

	@RequestMapping("/viewmobile")
	public String viewMobile(Model model) {
		return "smsSendto/smsSendMobile";
	}

	@RequestMapping("/viewbranch")
	public String viewbranch(Model model) {
		List<Branch> branchnameList = branchDAO.getBranchEffectAllzhandian(BranchEnum.ZhanDian.getValue() + "");
		model.addAttribute("branchList", branchnameList);
		return "smsSendto/smsSendBranch";
	}

	@RequestMapping("/sendCwb")
	public @ResponseBody String smsSendCwb(Model model, @RequestParam(value = "mobiles", required = false, defaultValue = "") String mobiles,
			@RequestParam(value = "smsRemack", required = false, defaultValue = "") String smsRemack, HttpServletRequest request) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("使用短信群发功能：用户名：{},站点：{},订单号：" + mobiles + ",短信内容:" + smsRemack + "", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("使用短信群发功能：获取用户名，站点异常");
		}
		JSONObject o = new JSONObject();
		String msg = "";
		try {
			if (mobiles.trim().length() > 0) {
				logger.info("短信发送，单量：{}", mobiles.split("\r\n").length);
				int j = 0;
				int i = 0;
				String errorMsg = "";
				for (String cwb : mobiles.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					List<CwbOrder> list = cwbDAO.getAllCwbOrderByCwb(cwb.trim());
					if (list != null && list.size() > 0) {
						for (CwbOrder cwbOrder : list) {
							if (cwbOrder.getConsigneemobile() != null && !"".equals(cwbOrder.getConsigneemobile())) {
								logger.info("短信发送，单号：{} 手机号：{}", cwb.trim(), cwbOrder.getConsigneemobile());
								try {
									msg = smsSendService.sendSms(cwbOrder.getConsigneemobileOfkf(), smsRemack, 1, cwbOrder.getCustomerid(), cwbOrder.getConsigneename(), getSessionUser().getUserid(),
											HttpUtil.getUserIp(request));
									logger.info("短信发送，单号：{}  结果：{}", cwb.trim(), msg);
									if ("发送短信成功".equals(msg)) {
										i++;
									} else {
										errorMsg += "<p>订单号：[" + cwb.trim() + "]:<font color='red'>" + msg + "</font></p>";
										j++;
									}
								} catch (UnsupportedEncodingException e) {
									logger.error("短信发送，异常", e);
									j++;
									errorMsg += "<p>订单号：[" + cwb.trim() + "]:<font color='red'>短信发送网络异常</font></p>";
								}
							} else {
								j++;
								errorMsg += "<p>订单号：[" + cwb.trim() + "]:<font color='red'>没有找到相关手机号</font></p>";
							}
						}
					} else {
						j++;
						errorMsg += "<p>订单号：[" + cwb.trim() + "]:<font color='red'>订单号不存在</font></p>";
					}
				}
				logger.info("短信发送，成功单数：{}", i);
				logger.info("短信发送，失败单数：{}", j);
				o.put("sussesCount", i);
				o.put("errorCount", j);
				o.put("errorMsg", errorMsg);
			}
		} catch (Exception e) {
			logger.error("短信发送，异常", e);
		}
		return o.toString();
	}

	@RequestMapping("/sendMobile")
	public @ResponseBody String sendMobile(Model model, @RequestParam(value = "mobiles", required = false, defaultValue = "") String mobiles,
			@RequestParam(value = "smsRemack", required = false, defaultValue = "") String smsRemack, HttpServletRequest request) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("使用短信群发功能：用户名：{},站点：{},手机号：" + mobiles + ",短信内容:" + smsRemack + "", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("使用短信群发功能：获取用户名，站点异常");
		}
		JSONObject o = new JSONObject();
		String msg = "";
		int j = 0;
		int i = 0;
		String errorMsg = "";
		try {

			if (mobiles.trim().length() > 0) {
				logger.info("短信发送，单量：{}", mobiles.split("\r\n").length);
				for (String mobile : mobiles.split("\r\n")) {
					if (mobile.trim().length() == 0) {
						continue;
					}
					if (!isMobileNO(mobile)) {
						logger.info("短信发送，手机号：{}", mobile.trim());
						j++;
						errorMsg += "<p>手机号：[" + mobile.trim() + "]:<font color='red'>手机号格式不正确</font></p>";
						continue;
					}
					if (mobile != null && !"".equals(mobile)) {
						logger.info("短信发送，手机号：{}", mobile.trim());
						try {
							msg = smsSendService.sendSms(mobile, smsRemack, 1, 0, "未知", getSessionUser().getUserid(), HttpUtil.getUserIp(request));
							logger.info("短信发送，手机号：{}  结果：{}", mobile.trim(), msg);
							if ("发送短信成功".equals(msg)) {
								i++;
							} else {
								errorMsg += "<p>手机号：[" + mobile.trim() + "]:<font color='red'>" + msg + "</font></p>";
								j++;
							}
						} catch (UnsupportedEncodingException e) {
							logger.error("短信发送，异常", e);
							j++;
							errorMsg += "<p>手机号：[" + mobile.trim() + "]:<font color='red'>短信发送网络异常</font></p>";
						}
					} else {
						j++;
						errorMsg += "<p>手机号：[" + mobile.trim() + "]:<font color='red'>手机号为空</font></p>";
					}
				}
			}
			logger.info("短信发送，成功单数：{}", i);
			logger.info("短信发送，失败单数：{}", j);

		} catch (Exception e) {
			logger.error("短信发送，异常", e);
			errorMsg += e.getMessage();
		}
		o.put("sussesCount", i);
		o.put("errorCount", j);
		o.put("errorMsg", errorMsg);
		return o.toString();
	}

	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@RequestMapping("/sendRole")
	public @ResponseBody String sendRole(Model model, @RequestParam(value = "roleids", required = false, defaultValue = "") String[] roleids,
			@RequestParam(value = "smsRemack", required = false, defaultValue = "") String smsRemack, HttpServletRequest request) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("使用短信群发功能：用户名：{},站点：{},角色：" + roleids + ",短信内容:" + smsRemack + "", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("使用短信群发功能：获取用户名，站点异常");
		}
		JSONObject o = new JSONObject();
		String msg = "";
		try {
			int j = 0;
			int i = 0;
			String errorMsg = "";
			if (roleids.length > 0) {
				for (String roleid : roleids) {
					List<User> uList = userDAO.getUserByRole(Integer.parseInt(roleid));
					if (uList != null && uList.size() > 0) {
						for (User user : uList) {
							logger.info("短信发送，用户：{} 手机号：{}", user.getRealname(), user.getUsermobile());
							try {
								if (user.getUsermobile() == null || user.getUsermobile().equals("")) {
									errorMsg += "<p>发送[" + user.getRealname() + "]:<font color='red'>手机号不存在</font></p>";
									j++;
								} else {
									msg = smsSendService.sendSms(user.getUsermobile(), smsRemack, 1, 0, user.getRealname(), getSessionUser().getUserid(), HttpUtil.getUserIp(request));
									logger.info("短信发送，user.getRealname()：{}  结果：{}", user.getRealname(), msg);

									if ("发送短信成功".equals(msg)) {
										i++;
									} else {
										errorMsg += "<p>发送[" + user.getRealname() + "]:<font color='red'>" + msg + "</font></p>";
										j++;
									}
								}
							} catch (UnsupportedEncodingException e) {
								errorMsg += "<p>发送[" + user.getRealname() + "]:<font color='red'>短信发送网络异常</font></p>";
								j++;
							}
						}
					} else {
						errorMsg += "<p><font color='red'>没有检索到所选角色的公司职员</font></p>";
					}
				}
			}
			logger.info("短信发送，成功单数：{}", i);
			logger.info("短信发送，失败单数：{}", j);
			o.put("sussesCount", i);
			o.put("errorCount", j);
			o.put("errorMsg", errorMsg);
		} catch (Exception e) {
			logger.error("短信发送，异常", e);
		}
		return o.toString();
	}

	@RequestMapping("/sendBranch")
	public @ResponseBody String sendBranch(Model model, @RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids,
			@RequestParam(value = "smsRemack", required = false, defaultValue = "") String smsRemack, HttpServletRequest request) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("使用短信群发功能：用户名：{},站点：{},站点：" + branchids + ",短信内容:" + smsRemack + "", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("使用短信群发功能：获取用户名，站点异常");
		}
		JSONObject o = new JSONObject();
		try {
			int j = 0;
			int i = 0;
			Map<String, String> mapParm = new HashMap<String, String>();
			mapParm.put("susses", i + "");
			mapParm.put("error", j + "");
			mapParm.put("msg", "");
			if (branchids.length > 0) {
				for (String branchid : branchids) {
					// 获取出库到该站
					List<CwbOrder> chukulist = cwbDAO.getOrderListByBranchidAndFlowtype(Long.parseLong(branchid), FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
					mapParm = sendByOrderList(chukulist, mapParm, smsRemack, request);
					// 获取到站扫描
					List<CwbOrder> daohuolist = cwbDAO.getOrderListByBranchidAndFlowtype(Long.parseLong(branchid), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
					mapParm = sendByOrderList(daohuolist, mapParm, smsRemack, request);
					// 获取领货
					List<CwbOrder> linghuolist = cwbDAO.getOrderListByBranchidAndFlowtype(Long.parseLong(branchid), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
					mapParm = sendByOrderList(linghuolist, mapParm, smsRemack, request);
					// 获取反馈为滞留、审核为滞留的
					List<CwbOrder> zhiliulist = cwbDAO.getOrderListByBranchidAndDeliveryState(Long.parseLong(branchid), DeliveryStateEnum.FenZhanZhiLiu.getValue());
					mapParm = sendByOrderList(zhiliulist, mapParm, smsRemack, request);
				}
			}
			logger.info("短信发送，成功单数：{}", mapParm.get("susses"));
			logger.info("短信发送，失败单数：{}", mapParm.get("error"));
			o.put("sussesCount", mapParm.get("susses"));
			o.put("errorCount", mapParm.get("error"));
			o.put("errorMsg", mapParm.get("msg"));
		} catch (Exception e) {
			logger.error("短信发送，异常", e);
		}
		return o.toString();
	}

	private Map<String, String> sendByOrderList(List<CwbOrder> list, Map<String, String> mapParm, String smsRemack, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String msg = "";
		String errorMsg = mapParm.get("msg");
		int i = Integer.parseInt(mapParm.get("susses"));
		int j = Integer.parseInt(mapParm.get("error"));
		if (list != null && list.size() > 0) {
			for (CwbOrder cwbOrder : list) {
				logger.info("短信发送，订单号：{} 手机号：{}", cwbOrder.getCwb(), cwbOrder.getConsigneemobile());
				try {
					if (cwbOrder.getConsigneemobile() == null || cwbOrder.getConsigneemobile().equals("")) {
						errorMsg += "<p>发送[" + cwbOrder.getCwb() + "]:<font color='red'>手机号不存在</font></p>";
						j++;
					} else {
						msg = smsSendService.sendSms(cwbOrder.getConsigneemobile(), smsRemack, 1, cwbOrder.getCustomerid(), cwbOrder.getConsigneename(), getSessionUser().getUserid(),
								HttpUtil.getUserIp(request));
						logger.info("短信发送，订单号：{}  结果：{}", cwbOrder.getCwb(), msg);
						if ("发送短信成功".equals(msg)) {
							i++;
						} else {
							errorMsg += "<p>发送[" + cwbOrder.getCwb() + "]:<font color='red'>" + msg + "</font></p>";
							j++;
						}
					}
				} catch (UnsupportedEncodingException e) {
					logger.error("短信发送，异常", e);
					j++;
					errorMsg += "<p>发送[" + cwbOrder.getCwb() + "]:<font color='red'>短信发送网络异常</font></p>";
				}
			}
		}
		map.put("susses", i + "");
		map.put("error", j + "");
		map.put("msg", errorMsg);
		return map;
	}

	@RequestMapping("/sendList")
	public String sendList(Model model, @RequestParam(value = "startSenddate", required = false, defaultValue = "") String startSenddate,
			@RequestParam(value = "stopSenddate", required = false, defaultValue = "") String stopSenddate,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "sendstate", required = false, defaultValue = "-1") int sendstate, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "channel", required = false, defaultValue = "-1") int channel, HttpServletRequest request) {
		// 首次进入页面时的初始化 默认为1天查询条件的时间
		if (startSenddate.trim().length() == 0 && stopSenddate.trim().length() == 0 && sendstate == -1) {
			startSenddate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000) + " 00:00:00";
			stopSenddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
		}

		model.addAttribute("SendSmsList", smsManageDao.getSendSmsList(startSenddate, stopSenddate, consigneemobile, sendstate, page, channel));
		model.addAttribute("page_obj", new Page(smsManageDao.getSendSmsListCount(startSenddate, stopSenddate, consigneemobile, sendstate, channel), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("startSenddate", startSenddate);
		model.addAttribute("stopSenddate", stopSenddate);
		model.addAttribute("consigneemobile", consigneemobile);
		model.addAttribute("sendstate", sendstate);
		model.addAttribute("channel", channel);
		Map<Long, User> userMap = new HashMap<Long, User>();
		for (User u : userDAO.getUserForALL()) {
			userMap.put(u.getUserid(), u);
		}
		model.addAttribute("userMap", userMap);
		return "/smsSendto/sendList";
	}

	@RequestMapping("/excelSmsSendList")
	public void excelSmsSendList(Model model, @RequestParam(value = "startSenddate", required = false, defaultValue = "") String startSenddate,
			@RequestParam(value = "stopSenddate", required = false, defaultValue = "") String stopSenddate,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "sendstate", required = false, defaultValue = "-1") int sendstate, @RequestParam(value = "channel", required = false, defaultValue = "-1") int channel,
			HttpServletRequest request, HttpServletResponse response) {
		// 首次进入页面时的初始化 默认为1天查询条件的时间
		if (startSenddate.trim().length() == 0 && stopSenddate.trim().length() == 0 && sendstate == -1) {
			startSenddate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000) + " 00:00:00";
			stopSenddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
		}

		List<User> userList = userDAO.getUserForALL();
		smsSendService.excelSmsSendList(userList, startSenddate, stopSenddate, consigneemobile, sendstate, channel, request, response);
	}
}
