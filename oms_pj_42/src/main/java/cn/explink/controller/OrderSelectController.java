package cn.explink.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OperatelogDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderSelectDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Exportmould;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.OrderSelectService;
import cn.explink.service.ProxyService;
import cn.explink.util.JMath;
import cn.explink.util.Page;

@Controller
@RequestMapping("/order")
public class OrderSelectController {

	@Autowired
	OrderSelectDAO cwbDao;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbDAO cwborderDAO;
	@Autowired
	OperatelogDAO operatelogDAO;
	@Autowired
	Excel2007Extractor excel2007Extractor;
	@Autowired
	Excel2003Extractor excel2003Extractor;
	@Autowired
	OrderSelectService orderSelectService;
	@Autowired
	ProxyService proxyService;
	@Autowired
	BranchDAO branchDAO;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = LoggerFactory.getLogger(OrderSelectController.class);

	/**
	 * lansheng
	 * 
	 * @param model
	 * @param page
	 * @param consigneename
	 * @param consigneephone
	 * @param beginemaildate
	 * @param endemaildate
	 * @param ordercwb
	 * @param consigneemobile
	 * @param sendcarname
	 * @param showMess
	 * @return
	 */

	@RequestMapping("/select/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, HttpServletResponse response,
			HttpServletRequest request) {
		model.addAttribute("cwb", cwb);
		return "/selectorder/orderWorkIndex";
	}

	@RequestMapping("/selectOrder/{page}")
	public String selectOrder(Model model, @PathVariable("page") long page, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, HttpServletResponse response,
			HttpServletRequest request) {
		List<CwbOrder> list1 = cwbDao.getCwbOrderBycwbid(cwb);
		if (list1 != null && list1.size() > 0) {
			String commNunber = list1.get(0).getCommonnumber() == null ? "" : list1.get(0).getCommonnumber();
			if (commNunber.indexOf("EMS") > -1) {// ems处理
				// 判断 代理是否存在
				if (proxyService.getProxy()) {
					String htmlStr = getDmpDAO.getEMSType(list1.get(0).getTranscwb());
					JSONObject jsonValue = JSONObject.fromObject(htmlStr.length() > 0 ? htmlStr : "{}");
					int status = htmlStr.length() > 0 ? jsonValue.getInt("status") : 0;
					int state = htmlStr.length() > 0 ? (jsonValue.get("state") == null ? 0 : jsonValue.getInt("state")) : 0;
					if (status == 200 && (list1.get(0).getNewfollownotes() == null || list1.get(0).getNewfollownotes().indexOf("签收") < 0)) {
						try {
							String allfollownotes = jsonValue.getString("data");
							JSONArray jsonArray = JSONArray.fromObject(allfollownotes);
							String jsonOneObject = jsonArray.get(0).toString();
							JSONObject jsonOne = JSONObject.fromObject(jsonOneObject);
							String newfollownotes = jsonOne.getString("context");
							String signintime = jsonOne.getString("time");
							if (jsonArray.size() > 10) {// 退货完成
								cwborderDAO.updateDeliveryByTranscwb(DeliveryStateEnum.QuanBuTuiHuo.getValue(), signintime, list1.get(0).getCwb(), false);
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
							} else if (state == 3) {// 做签收处理
								cwborderDAO.updateDeliveryByTranscwb(DeliveryStateEnum.PeiSongChengGong.getValue(), signintime, list1.get(0).getCwb(), true);
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());

							} else {
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				} else {
					logger.info("RE : EMS  No proxy!!");
				}
			}
		}
		List<User> userList = getDmpDAO.getAllUsers();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User us = getDmpDAO.getLogUser(dmpid);
		List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
		model.addAttribute("cwborder", list);

		StringBuffer sub = new StringBuffer();
		List<User> userlist = getDmpDAO.getAllUsers();
		List<OrderFlow> datalist = orderFlowDAO.getOrderFlowByCwb(cwb);
		for (int i = 0; i < datalist.size(); i++) {
			OrderFlow orderFlow = datalist.get(i);
			User user = getUserInfoById(userlist, orderFlow.getDeliverid());//
			if (orderFlow.getIsGo() == 1) {
				sub.append("<tr>" + orderFlow.getFloworderTrackInfoBody(user) + "</tr>");
			}
		}
		model.addAttribute("flowOrder", orderFlowDAO.getOrderFlowByCwb(cwb));
		model.addAttribute("flowOrderStr", sub);
		model.addAttribute("branchList", getDmpDAO.getAllBranchs());
		model.addAttribute("userList", userList);
		model.addAttribute("sw", getDmpDAO.getSwitchBySwitchname("changesing"));
		model.addAttribute("userRoleid", us.getRoleid());
		return "/selectorder/orderWorkRight";
	}

	private User getUserInfoById(List<User> userlist, long deliverid) {
		User u = new User();
		if (userlist != null && userlist.size() > 0) {
			for (User user : userlist) {
				if (user.getUserid() == deliverid) {
					u = user;
					break;
				}
			}
		}
		return u;
	}

	// 快速查询栏
	@RequestMapping("/queckSelectOrder/{page}")
	public String queckSelectOrder(Model model, @PathVariable("page") long page, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, HttpServletResponse response,
			HttpServletRequest request) {
		List<CwbOrder> list1 = cwbDao.getCwbOrderBycwbid(cwb);
		if (list1 != null && list1.size() > 0) {
			String commNunber = list1.get(0).getCommonnumber() == null ? "" : list1.get(0).getCommonnumber();
			if (commNunber.indexOf("EMS") > -1) {// ems处理
				// 判断 代理是否存在
				if (proxyService.getProxy()) {
					String htmlStr = getDmpDAO.getEMSType(list1.get(0).getTranscwb());
					JSONObject jsonValue = JSONObject.fromObject(htmlStr.length() > 0 ? htmlStr : "{}");
					int status = htmlStr.length() > 0 ? jsonValue.getInt("status") : 0;
					int state = htmlStr.length() > 0 ? (jsonValue.get("state") == null ? 0 : jsonValue.getInt("state")) : 0;
					if (status == 200 && (list1.get(0).getNewfollownotes() == null || list1.get(0).getNewfollownotes().indexOf("签收") < 0)) {
						try {
							String allfollownotes = jsonValue.getString("data");
							JSONArray jsonArray = JSONArray.fromObject(allfollownotes);
							String jsonOneObject = jsonArray.get(0).toString();
							JSONObject jsonOne = JSONObject.fromObject(jsonOneObject);
							String newfollownotes = jsonOne.getString("context");
							String signintime = jsonOne.getString("time");
							if (jsonArray.size() > 10) {// 退货完成
								cwborderDAO.updateDeliveryByTranscwb(DeliveryStateEnum.QuanBuTuiHuo.getValue(), signintime, list1.get(0).getCwb(), false);
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
							} else if (state == 3) {// 做签收处理
								cwborderDAO.updateDeliveryByTranscwb(DeliveryStateEnum.PeiSongChengGong.getValue(), signintime, list1.get(0).getCwb(), true);
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());

							} else {
								cwborderDAO.saveCwbByTranscwb(allfollownotes, newfollownotes, signintime, list1.get(0).getTranscwb());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				} else {
					logger.info("RE : EMS  No proxy!!");
				}
			}
		}
		List<User> userList = getDmpDAO.getAllUsers();

		List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
		model.addAttribute("cwborder", list);

		StringBuffer sub = new StringBuffer();
		List<User> userlist = getDmpDAO.getAllUsers();
		List<OrderFlow> datalist = orderFlowDAO.getOrderFlowByCwb(cwb);
		for (int i = 0; i < datalist.size(); i++) {
			OrderFlow orderFlow = datalist.get(i);
			User user = getUserInfoById(userlist, orderFlow.getDeliverid());//
			if (orderFlow.getIsGo() == 1) {
				sub.append("<tr>" + orderFlow.getFloworderTrackInfoBody(user) + "</tr>");
			}
		}
		model.addAttribute("flowOrder", orderFlowDAO.getOrderFlowByCwb(cwb));
		model.addAttribute("flowOrderStr", sub);
		model.addAttribute("branchList", getDmpDAO.getAllBranchs());
		model.addAttribute("userList", userList);
		return "/selectorder/orderWorkRightQueck";
	}

	/**
	 * 签收功能
	 * 
	 * @param cwb
	 * @param flagtime
	 * @return
	 */
	@RequestMapping("/sign/{cwb}")
	public @ResponseBody String selectOrder(@PathVariable(value = "cwb") String cwb, @RequestParam(value = "flagtime") String flagtime, HttpServletRequest request) {

		try {
			if (JMath.checkdate(flagtime)) {

			}
		} catch (ParseException e1) {
			flagtime = sdf.format(new Date());
		}
		try {
			cwbDao.updateDelierySignin(cwb, flagtime, DeliveryStateEnum.PeiSongChengGong.getValue());
			// 写日志
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwb, cwb, "订单号或运单号为：" + cwb + "  修改签收时间为" + flagtime
					+ "！");
			return "{\"errorCode\":0,\"error\":\"签收成功\",\"date\":\"" + flagtime + "\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"签收失败，请及时联系相关人员\"}";
		}
	}

	/**
	 * 修改签收人功能
	 * 
	 * @param cwb
	 * @param flagtime
	 * @return
	 */
	@RequestMapping("/signname/{cwb}")
	public @ResponseBody String editSignname(@PathVariable(value = "cwb") String cwb, @RequestParam(value = "flagname") String flagname, HttpServletRequest request) {

		try {
			cwbDao.updateDeliverySigninman(cwb, flagname, DeliveryStateEnum.PeiSongChengGong.getValue());
			try {
				orderFlowDAO.updateSignmanDelivery(flagname, cwb, DeliveryStateEnum.PeiSongChengGong.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 写日志
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwb, cwb, "订单号或运单号为：" + cwb + "  修改签收人为：" + flagname
					+ "！");
			return "{\"errorCode\":0,\"error\":\"签收成功\",\"date\":\"" + flagname + "\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"签收失败，请及时联系相关人员\"}";
		}
	}

	@RequestMapping("/selectAll/{page}")
	public String selectAll(Model model, @PathVariable("page") long page, @RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "consigneephone", required = false, defaultValue = "") String consigneephone,
			@RequestParam(value = "consigneeaddress", required = false, defaultValue = "") String consigneeaddress,
			@RequestParam(value = "marksflag", required = false, defaultValue = "1") long marksflag,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "marksflagtime") String orderbyName,
			@RequestParam(value = "showflag", required = false, defaultValue = "0") long showflag, @RequestParam(value = "orderbyId", required = false, defaultValue = "DESC") String orderbyId,
			HttpServletResponse response, HttpServletRequest request) {

		if ("收件人姓名".equals(consigneename)) {
			consigneename = "";
		}
		if ("手机号".equals(consigneemobile)) {
			consigneemobile = "";
		}
		if ("地址（模糊）".equals(consigneeaddress)) {
			consigneeaddress = "";
		}
		if ("电话号".equals(consigneephone)) {
			consigneephone = "";
		}
		if (showflag == 1) {
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			String marksflagmen = (user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname();
			String orderName = " " + orderbyName + " " + orderbyId;
			List<CwbOrder> list = cwbDao.getAllCwbOrderByMarksflag(page, consigneename, consigneemobile, consigneephone, consigneeaddress, marksflagmen, orderName, marksflag);
			model.addAttribute("page_obj", new Page(cwbDao.getAllCwbOrderByMarksflagCount(consigneename, consigneemobile, consigneephone, consigneeaddress, marksflagmen, marksflag), page,
					Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("allcwborderList", list);
		} else {
			model.addAttribute("page_obj", new Page(0, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}
		return "/selectorder/orderWorkLeft";
	}

	/**
	 * 修改备注
	 * 
	 * @param cwb
	 * @param cwbremack
	 * @param model
	 * @return
	 */
	@RequestMapping("/update")
	public String update(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "cwbremack", required = false, defaultValue = "") String cwbremack, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		String name = (user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname();

		long msg = cwbDao.updateOrder(name + ":" + cwbremack, sdf.format(new Date()), name, cwb);
		List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
		try {
			// 写日志
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), list.get(0).getCwb(), list.get(0).getTranscwb(),
					"订单号或运单号为：" + cwb + "添加备注" + cwbremack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<User> userList = getDmpDAO.getAllUsers();
		User us = getDmpDAO.getLogUser(dmpid);
		model.addAttribute("cwborder", list);

		StringBuffer sub = new StringBuffer();
		List<User> userlist = getDmpDAO.getAllUsers();
		List<OrderFlow> datalist = orderFlowDAO.getOrderFlowByCwb(cwb);
		for (int i = 0; i < datalist.size(); i++) {
			OrderFlow orderFlow = datalist.get(i);
			User user1 = getUserInfoById(userlist, orderFlow.getDeliverid());//
			if (orderFlow.getIsGo() == 1) {
				sub.append("<tr>" + orderFlow.getFloworderTrackInfoBody(user1) + "</tr>");
			}
		}

		model.addAttribute("flowOrder", orderFlowDAO.getOrderFlowByCwb(cwb));
		model.addAttribute("flowOrderStr", sub);
		model.addAttribute("branchList", getDmpDAO.getAllBranchs());
		model.addAttribute("userList", userList);
		model.addAttribute("sw", getDmpDAO.getSwitchBySwitchname("changesing"));

		model.addAttribute("userRoleid", us.getRoleid());
		// model.addAttribute("msg", msg);
		return "/selectorder/orderWorkRight";
	}

	/**
	 * 修改金额
	 * 
	 * @param cwb
	 * @param receivablefee
	 * @return
	 */
	@RequestMapping("/saveReceivablefee/{cwb}")
	public @ResponseBody String saveReceivablefee(@PathVariable("cwb") String cwb, @RequestParam("receivablefee") String receivablefee, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		String name = (user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname();

		long count = orderFlowDAO.getOrderFlowDeliveryByCwbCount(cwb, "" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.QuanBuTuiHuo.getValue());
		if (count > 0) {
			return "{\"errorCode\":1,\"error\":\"修改代收金额失败！配送成功、退货不可修改\"}";
		}
		// 发送jms给dmp
		int sendjms = orderSelectService.updateOrderMoneySendJms(cwb, receivablefee);
		if (sendjms == 1) {
			cwbDao.updateReceivablefee(receivablefee, sdf.format(new Date()), name, cwb);
			cwbDao.updateReceivablefeeForOrderFlow(receivablefee, cwb);
			List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
			try {
				// 写日志
				operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), list.get(0).getCwb(), list.get(0).getTranscwb(),
						"订单号或运单号为：" + cwb + "的代收金额修改为" + receivablefee + "元！");
				return "{\"errorCode\":0,\"error\":\"修改代收金额成功！\"}";
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"修改代收金额失败！\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"修改代收金额失败！数据未同步到生产线!\"}";
		}
	}

	/**
	 * 标记功能
	 * 
	 * @param cwb
	 * @param marksflag
	 * @return
	 */
	@RequestMapping("/updateMack")
	public @ResponseBody String updateMack(@RequestParam("cwb") String cwb, @RequestParam("mackType") long marksflag, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		String name = (user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname();
		cwbDao.updateMack(cwb, sdf.format(new Date()), name, marksflag);
		List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
		try {
			// 写日志
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), list.get(0).getCwb(), list.get(0).getTranscwb(),
					"订单号或运单号为：" + cwb + (marksflag == 1 ? "被标记！" : "被取消标记！"));
			return "{\"errorCode\":0,\"error\":\"" + (marksflag == 1 ? "被标记！" : "被取消标记！") + "成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"" + (marksflag == 1 ? "被标记！" : "被取消标记！") + "失败，请及时联系相关人员\"}";
		}
	}

	/**
	 * 批量标记功能
	 * 
	 * @param cwb
	 * @param marksflag
	 * @return
	 */
	@RequestMapping("/updateBatchMack")
	public @ResponseBody String updateBatchMack(@RequestParam("cwbs") String cwbs, @RequestParam("mackType") long marksflag, HttpServletRequest request) {
		String re = "{\"errorCode\":1,\"error\":\"" + (marksflag == 1 ? "被标记！" : "被取消标记！") + "失败，请及时联系相关人员\"}";
		for (String cwb : cwbs.split(",")) {
			re = updateMack(cwb, marksflag, request);
		}
		return re;
	}

	@RequestMapping("/quxiaoMack")
	public String quxiaoMack(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "mackType", required = false, defaultValue = "0") long marksflag, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		String name = (user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname();
		long msg = cwbDao.updateMack(cwb, sdf.format(new Date()), name, marksflag);
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		try {
			// 写日志
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), list.get(0).getCwb(), list.get(0).getTranscwb(),
					"订单号或运单号为：" + cwb + "被取消标记！");
		} catch (Exception e) {
			System.out.println("写日志报错！");
		}
		model.addAttribute("page_obj", new Page(1, 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("allcwborderList", list);
		model.addAttribute("msg", msg);
		return "/selectorder/orderWorkLeft";
	}

	/**
	 * lansheng
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/getCwb/{id}")
	public String selectCwbByid(Model model, @PathVariable("id") String id, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("cwborder", cwbDao.getCwbOrderBycwbid(id));
		return "/selectorder/viewByCwbId";
	}

	@RequestMapping("/getByCwb/{cwb}")
	public String selectByCwb(Model model, @PathVariable("cwb") String cwb, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("cwborder", cwbDao.getCwbOrderBycwbid(cwb));
		model.addAttribute("flowOrder", orderFlowDAO.getOrderFlowByCwb(cwb));
		return "/selectorder/viewByCwb";
	}

	/**
	 * lansheng
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/getDeliver/{id}")
	public String selectDeliverByid(Model model, @PathVariable("id") long id, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		User u = getDmpDAO.getUserById(id);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("user", u);
		return "/selectorder/viewDeliverById";
	}

	// 预警
	@RequestMapping("/earlywarning/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber,
			@RequestParam(value = "surpassdate", required = false, defaultValue = "-2") int surpassdate, @RequestParam(value = "marksflag", required = false, defaultValue = "0") int marksflag,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "") String isshow,
			HttpServletResponse response, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		Page pageparm = new Page();
		if (!isshow.isEmpty()) {
			String orderName = " " + orderbyName + " " + orderbyId;
			clist = cwborderDAO.getcwbOrderByPageD(page, customerid, commonnumber, surpassdate, marksflag, orderName);
			pageparm = new Page(cwborderDAO.getcwborderCountD(customerid, commonnumber, surpassdate, marksflag), page, Page.ONE_PAGE_NUMBER);
			setSesstionBack(customerid, commonnumber, surpassdate, marksflag, orderName, request);
		}
		User user = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = getDmpDAO.getExportmoulds(user, dmpid);
		model.addAttribute("orderlist", clist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("customerlist", getDmpDAO.getAllCustomers());
		model.addAttribute("commonlist", getDmpDAO.getAllCommons());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("page", page);

		return "cwborder/list";
	}

	// 预警导出
	@RequestMapping("/exportExcle")
	public String exportExcleWang(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam("exportmould2") String mouldfieldids2) {
		orderSelectService.exportexportExcleWang(mouldfieldids2, response, request);
		return "/cwborder/list";
	}

	// 预警导入修改
	@RequestMapping("/importExcle")
	public String importExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam("txtFileName") MultipartFile file) {
		String isOk = "";
		String msg = "";

		try {
			ExcelExtractor excelExtractor = getExcelExtractor(file);
			if (excelExtractor != null) {
				List<CwbOrderImportDTO> cwbOrderDTOs = excelExtractor.extractImport(file);
				isOk = orderSelectService.updateImportOrder(cwbOrderDTOs);
				if ("ok".equals(isOk)) {
					msg = "导入修改成功！";

				} else {
					msg = "导入修改失败！";
				}
			} else {
				msg = "不可识别的文件";
			}
		} catch (Exception e) {
			msg = "系统内部错误";
		}
		model.addAttribute("importMsg", msg);
		// 返回当前页面
		long customerid = Long.parseLong(request.getSession().getAttribute("customerid").toString());
		String commonnumber = request.getSession().getAttribute("commonnumber").toString();
		int surpassdate = Integer.parseInt(request.getSession().getAttribute("surpassdate").toString()) == 0 ? 2 : Integer.parseInt(request.getSession().getAttribute("surpassdate").toString());
		int marksflag = Integer.parseInt(request.getSession().getAttribute("marksflag").toString());
		String orderName = request.getSession().getAttribute("orderbyName").toString();
		model.addAttribute("orderlist", cwborderDAO.getcwbOrderByPageD(1, customerid, commonnumber, surpassdate, marksflag, orderName));
		model.addAttribute("customerlist", getDmpDAO.getAllCustomers());
		model.addAttribute("commonlist", getDmpDAO.getAllCommons());
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = getDmpDAO.getExportmoulds(user, dmpid);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("page_obj", new Page(cwborderDAO.getcwborderCountD(customerid, commonnumber, surpassdate, marksflag), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		return "/cwborder/list";
	}

	// 批量修改
	@RequestMapping("/batchedit")
	public String batchedit() {
		return "/cwborder/batchedit";
	}

	// 批量修改保存
	@RequestMapping("/savebatchedit")
	public String savebatchedit(Model model, @RequestParam("cwb") String cwb, @RequestParam("cwbremark") String cwbremark, HttpServletRequest request) {

		if (cwb.trim().length() > 0 && cwbremark.trim().length() > 0) {
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			StringBuffer cwbstr = new StringBuffer();
			String remarks[] = cwbremark.split("\r\n");
			String cwbs[] = cwb.split("\r\n");
			for (int i = 0; i < cwbs.length; i++) {
				CwbOrder cwborder = cwborderDAO.getCwbByCwb(cwbs[i]);
				if (cwborder == null) {
					model.addAttribute("SuMessage", "包含不存在的单号");
					return "/cwborder/batchedit";
				}
			}
			List<CwbOrder> listcwborder = cwborderDAO.getCwbByCwbs(cwb);
			if (listcwborder.size() > 0) {
				for (CwbOrder c : listcwborder) {
					cwbstr.append(c.getCwb() + ",");
				}
				String insertcwb[] = cwbstr.toString().split(",");
				for (int i = 0; i < remarks.length; i++) {
					remarks[i] = ((user.getRealname() == null || "".equals(user.getRealname())) ? user.getUsername() : user.getRealname()) + ": " + remarks[i];
					cwborderDAO.batchEdit(remarks[i], insertcwb[i]);
					try {
						// 写日志
						operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), insertcwb[i], insertcwb[i], "订单号或运单号为："
								+ insertcwb[i] + "被修改备注，备注为：" + remarks[i] + "");
					} catch (Exception e) {
						System.out.println("写日志报错！");
					}
				}
				model.addAttribute("SuMessage", "操作成功");
			} else {
				model.addAttribute("SuMessage", "没有查到单号");
			}
		}
		return "/cwborder/batchedit";
	}

	// 批量修改运单号
	@RequestMapping("/transcwbbatchedit")
	public String transcwbbatchedit() {
		return "/cwborder/transcwbbatchedit";
	}

	// 批量修改改运单保存
	@RequestMapping("/transcwbsavebatchedit")
	public String transcwbsavebatchedit(Model model, @RequestParam("cwb") String cwb, @RequestParam("cwbremark") String cwbremark, HttpServletRequest request) {

		if (cwb.trim().length() > 0 && cwbremark.trim().length() > 0) {
			StringBuffer cwbstr = new StringBuffer();
			String transcwbs[] = cwbremark.split("\r\n");
			String cwbs[] = cwb.split("\r\n");
			for (int i = 0; i < cwbs.length; i++) {
				CwbOrder cwborder = cwborderDAO.getCwbByCwb(cwbs[i]);
				if (cwborder == null) {
					model.addAttribute("SuMessage", "包含不存在的单号");
					return "/cwborder/batchedit";
				}
			}
			List<CwbOrder> listcwborder = cwborderDAO.getCwbByCwbs(cwb);
			if (listcwborder.size() > 0) {
				for (CwbOrder c : listcwborder) {
					cwbstr.append(c.getCwb() + ",");
				}
				String insertcwb[] = cwbstr.toString().split(",");
				for (int i = 0; i < transcwbs.length; i++) {
					cwborderDAO.transcwbbatchEdit(transcwbs[i], insertcwb[i]);
					try {
						// 写日志
						String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
						User user = getDmpDAO.getLogUser(dmpid);
						operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), insertcwb[i], transcwbs[i], "订单号为："
								+ insertcwb[i] + "的运单号被修改为：" + transcwbs[i] + "");
					} catch (Exception e) {
						System.out.println("写日志报错！");
					}
				}
				model.addAttribute("SuMessage", "操作成功");
			} else {
				model.addAttribute("SuMessage", "没有查到单号");
			}
		}
		return "/cwborder/transcwbbatchedit";
	}

	// 退货管理
	@RequestMapping("/backgoods/{page}")
	public String backgoods(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber, @RequestParam(value = "auditstate", required = false, defaultValue = "-2") long auditstate,
			@RequestParam(value = "auditEganstate", required = false, defaultValue = "-2") long auditEganstate,
			@RequestParam(value = "beginshiptime", required = false, defaultValue = "") String beginshiptime,
			@RequestParam(value = "endshiptime", required = false, defaultValue = "") String endshiptime,
			@RequestParam(value = "orderByName", required = false, defaultValue = "shiptime") String orderByName,
			@RequestParam(value = "orderByType", required = false, defaultValue = "DESC") String orderByType, HttpServletResponse response, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		String orderbyNameAndType = " " + orderByName + " " + orderByType;
		User u = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = getDmpDAO.getExportmoulds(u, dmpid);
		model.addAttribute("customerlist", getDmpDAO.getAllCustomers());
		model.addAttribute("commonlist", getDmpDAO.getAllCommons());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("backgoodslist", cwborderDAO.getcwbOrderByPageR(page, customerid, commonnumber, auditstate, auditEganstate, beginshiptime, endshiptime, orderbyNameAndType));
		model.addAttribute("page_obj", new Page(cwborderDAO.getcwborderCountR(customerid, commonnumber, auditstate, auditEganstate, beginshiptime, endshiptime), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		setSesstion(beginshiptime, endshiptime, customerid, commonnumber, orderbyNameAndType, auditstate, auditEganstate, request);
		return "/cwborder/backgoods";
	}

	// 退货管理导出
	@RequestMapping("/backGoodsexportExcle")
	public String backGoodsexportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam("exportmould2") String mouldfieldids2) {
		orderSelectService.backGoodsexportExcle(mouldfieldids2, response, request);
		return "/cwborder/backgoods";
	}

	// 退货批量审核
	@RequestMapping("/backgoodsbatch/{page}")
	public String backgoodsbatch(Model model, @PathVariable("page") long page, @RequestParam(value = "cwbandtranscwb", required = false, defaultValue = "") String cwbandtranscwb,
			@RequestParam(value = "typeName", required = false, defaultValue = "0") String typeName,
			@RequestParam(value = "orderByName", required = false, defaultValue = "emaildate") String orderByName,
			@RequestParam(value = "orderByType", required = false, defaultValue = "DESC") String orderByType, HttpServletRequest request) {
		String orderByNameAndType = "" + orderByName + " " + orderByType;
		if (Integer.parseInt(typeName) == 0) {
			model.addAttribute("backgoodslist", cwborderDAO.getcwbOrderByPageRB(page, cwbandtranscwb, orderByNameAndType));
			model.addAttribute("page_obj", new Page(cwborderDAO.getcwborderCountRB(cwbandtranscwb), page, Page.ONE_PAGE_NUMBER));
		} else if (Integer.parseInt(typeName) == 1) { // 批量审核
			if (cwbandtranscwb.trim().length() > 0) {
				String cwbat[] = cwbandtranscwb.trim().split("\r\n");
				if (cwbat.length > 0) {
					for (int ct = 0; ct < cwbat.length; ct++) {
						// 发送jms给dmp
						int sendjms = orderSelectService.auditBackGoodsJms(cwbat[ct], 1);
						if (sendjms == 1) {
							cwborderDAO.backgoodsExameVerifyByCwbOrTranscwb(cwbat[ct]);
							try {
								// 写日志
								String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
								User user = getDmpDAO.getLogUser(dmpid);
								operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwbat[ct], cwbat[ct], "订单号为："
										+ cwbat[ct] + "做了退货审核");
							} catch (Exception e) {
								logger.info("re:" + cwbat[ct] + "退货审核 写日志报错！");
							}
						} else {
							logger.info("re:" + cwbat[ct] + "退货审核失败！数据未同步到生产线!");
						}
					}
				}
			}
			model.addAttribute("backgoodslist", cwborderDAO.getcwbOrderByPageRB(page, "", orderByNameAndType));
			model.addAttribute("page_obj", new Page(cwborderDAO.getcwborderCountRB(""), page, Page.ONE_PAGE_NUMBER));
		} else if (Integer.parseInt(typeName) == 2) {// 批量撤销审核
			if (cwbandtranscwb.trim().length() > 0) {
				String cwbat[] = cwbandtranscwb.trim().split("\r\n");
				if (cwbat.length > 0) {
					for (int ct = 0; ct < cwbat.length; ct++) {
						// 发送jms给dmp
						int sendjms = orderSelectService.auditBackGoodsJms(cwbat[ct], 0);
						if (sendjms == 1) {
							cwborderDAO.rebackgoodsExameVerifyBycat(cwbat[ct]);
							try {
								// 写日志
								String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
								User user = getDmpDAO.getLogUser(dmpid);
								operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwbat[ct], cwbat[ct], "订单号为："
										+ cwbat[ct] + "做了撤销退货审核");
							} catch (Exception e) {
								logger.info("re:" + cwbat[ct] + "撤销退货审核 写日志报错！");
							}
						} else {
							logger.info("re:" + cwbat[ct] + "退货审核失败！数据未同步到生产线!");
						}
					}
				}
			}
			model.addAttribute("backgoodslist", cwborderDAO.getcwbOrderByPageRB(page, "", orderByNameAndType));
			model.addAttribute("page_obj", new Page(cwborderDAO.getcwborderCountRB(""), page, Page.ONE_PAGE_NUMBER));
		}
		model.addAttribute("page", page);
		return "/cwborder/backgoodsbatch";
	}

	// 审核
	@RequestMapping("/audit/{cwb}")
	public @ResponseBody String audit(@PathVariable("cwb") String cwb, @RequestParam("auditstate") int auditstate, HttpServletRequest request) {

		// 发送jms给dmp
		int sendjms = orderSelectService.auditBackGoodsJms(cwb, auditstate);
		if (sendjms == 1) {

			try {
				// 写日志
				String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
				User user = getDmpDAO.getLogUser(dmpid);

				cwborderDAO.backgoodsExameVerifyByCwb(cwb, auditstate, user.getRealname());
				operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwb, cwb, "订单号或运单号为：" + cwb + "做"
						+ (auditstate == 0 ? "撤销审核！" : (auditstate == -1 ? "审核不通过" : (auditstate == 1 ? "审为再投" : "审为退供货商"))));
				return "{\"errorCode\":0,\"error\":\"" + (auditstate == 0 ? "撤销审核！" : (auditstate == -1 ? "审核不通过" : (auditstate == 1 ? "审为再投" : "审为退供货商"))) + "成功\"}";
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"" + (auditstate == 0 ? "撤销审核！" : (auditstate == -1 ? "审核不通过" : (auditstate == 1 ? "审为再投" : "审为退供货商"))) + "失败，请及时联系相关人员\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"审核失败！数据未同步到生产线!\"}";
		}
	}

	// 审核再投
	@RequestMapping("/auditEgan/{cwb}")
	public @ResponseBody String auditBack(@PathVariable("cwb") String cwb, @RequestParam("auditstate") int auditstate, HttpServletRequest request) {

		// 发送jms给dmp
		int sendjms = orderSelectService.auditBackGoodsEganJms(cwb, auditstate);
		if (sendjms == 1) {
			cwborderDAO.backgoodsExameVerifyEganByCwb(cwb, auditstate);
			try {
				// 写日志
				String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
				User user = getDmpDAO.getLogUser(dmpid);
				operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwb, cwb, "订单号或运单号为：" + cwb + "做退货"
						+ (auditstate == 1 ? "再投审核！" : "撤销再投审核！"));
				return "{\"errorCode\":0,\"error\":\"" + (auditstate == 1 ? "再投审核！" : "撤销再投审核！") + "成功\"}";
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"" + (auditstate == 1 ? "再投审核！" : "撤销再投审核！") + "失败，请及时联系相关人员\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"再投审核失败！数据未同步到生产线!\"}";
		}
	}

	// 退货备注
	@RequestMapping("/returngoodsremark/{cwb}")
	public String returngoodsremark(Model model, @PathVariable("cwb") String cwb) {
		model.addAttribute("remarkbyId", cwborderDAO.getCwbOrderByCwb("'" + cwb + "'"));
		return "/cwborder/regoodsremark";
	}

	// 保存退货备注
	@RequestMapping("/saveremark/{cwb}")
	public @ResponseBody String saveremark(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "returngoodsremark", required = false, defaultValue = "") String returngoodsremark,
			HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		String d = sdf.format(new Date());
		String username = user.getRealname() == null ? user.getUsername() : user.getRealname();
		String regoodsremark = d + ":" + username + ":" + returngoodsremark;
		cwborderDAO.saveReturngoodsremark(regoodsremark, cwb);
		// 发送jms给dmp
		orderSelectService.auditBackGoodsRemarkJms(cwb, returngoodsremark);
		try {
			// 写日志
			operatelogDAO.creOperatelog((user.getRealname() == null || user.getRealname().equals("") ? user.getUsername() : user.getRealname()), cwb, cwb, "订单号或运单号为：" + cwb + "做退货备注："
					+ returngoodsremark + "");
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败，请及时联系相关人员\"}";
		}
	}

	// 日志操作
	@RequestMapping("/makelog/{cwb}")
	public String makelog(Model model, @PathVariable("cwb") String cwb) {
		model.addAttribute("operatelogList", operatelogDAO.getOperatelogByCwb(cwb));
		return "/cwborder/makelog";
	}

	/**
	 * 快速查询栏（给供货商客服查询）
	 * 
	 * @param model
	 * @param page
	 * @param cwb
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/orderFlowQuery")
	public String orderFlowQuery() {
		return "/selectorder/orderFlowQuery";
	}

	@RequestMapping("/orderFlowQueryByCwb")
	public String orderFlowQueryByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "isSearchFlag", required = false, defaultValue = "") String isSearchFlag, HttpServletResponse response, HttpServletRequest request) {
		List<CwbOrder> list = cwbDao.getCwbOrderBycwbid(cwb);
		model.addAttribute("cwborder", list);
		model.addAttribute("isSearchFlag", isSearchFlag);
		StringBuffer sub = new StringBuffer();
		List<User> userlist = getDmpDAO.getAllUsers();
		List<OrderFlow> datalist = orderFlowDAO.getOrderFlowByCwb(cwb);
		for (int i = 0; i < datalist.size(); i++) {
			OrderFlow orderFlow = datalist.get(i);
			User user = getUserInfoById(userlist, orderFlow.getDeliverid());//
			if (orderFlow.getIsGo() == 1) {
				sub.append("<tr>" + orderFlow.getFloworderTrackInfoBody(user) + "</tr>");
			}
		}
		model.addAttribute("flowOrder", orderFlowDAO.getOrderFlowByCwb(cwb));
		model.addAttribute("flowOrderStr", sub);
		model.addAttribute("branchList", getDmpDAO.getAllBranchs());
		return "/selectorder/orderFlowQuery";
	}

	private void setSesstion(String beginshiptime, String endshiptime, long customerid, String commonnumber, String orderbyName, long auditstate, long auditEganstate, HttpServletRequest request) {
		request.getSession().setAttribute("beginshiptime", beginshiptime);
		request.getSession().setAttribute("endshiptime", endshiptime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("commonnumber", commonnumber);
		request.getSession().setAttribute("orderbyName", orderbyName);
		request.getSession().setAttribute("auditstate", auditstate);
		request.getSession().setAttribute("auditEganstate", auditEganstate);

	}

	private void setSesstionBack(long customerid, String commonnumber, int surpassdate, int marksflag, String orderbyName, HttpServletRequest request) {
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("commonnumber", commonnumber);
		request.getSession().setAttribute("surpassdate", surpassdate);
		request.getSession().setAttribute("marksflag", marksflag);
		request.getSession().setAttribute("orderbyName", orderbyName);

	}

	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return excel2003Extractor;
		}
		return null;
	}

}