package cn.explink.controller;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.AbnormalWriteBackEnum;
import cn.explink.service.AbnormalService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.UserService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@Controller
@RequestMapping("/abnormalOrder")
public class AbnormalOrderController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AbnormalService abnormalService;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RoleDAO roleDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 创建问题件
	 *
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/toCreateabnormal")
	public String toCreateabnormal(Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid) {
		String quot = "'", quotAndComma = "',";
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		Map<Long, JSONObject> mapForAbnormalorder = new HashMap<Long, JSONObject>();
		if (cwb.length() > 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);

			List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
				CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwbStr);
				if (co != null) {
					AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOpscwbidForObject(co.getOpscwbid());
					long action = AbnormalWriteBackEnum.ChuangJian.getValue();
					if (ab != null) {
						action = AbnormalWriteBackEnum.XiuGai.getValue();
					}
					this.abnormalService.creAbnormalOrder(co, this.getSessionUser(), abnormaltypeid, nowtime, mapForAbnormalorder, action);
				}
			}
			cwbList.addAll(this.cwbDAO.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1)));
			model.addAttribute("mapForAbnormal", mapForAbnormalorder);
			model.addAttribute("cwbList", cwbList);
			model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
			model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		}

		return "/abnormalorder/createabnormal";
	}

	/**
	 * 创建问题件的form提交
	 *
	 * @param model
	 * @param cwbdetails
	 * @return
	 */
	@RequestMapping("/submitCreateabnormal")
	public @ResponseBody long submitCreateabnormal(Model model, @RequestParam(value = "cwbdetails", defaultValue = "", required = false) String cwbdetails) {

		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		long successCount = 0;

		if (cwbdetails == null) {
			return successCount;
		}

		JSONArray rJson = JSONArray.fromObject(cwbdetails);

		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_id = reason.split("_s_");
			if (cwb_id.length == 4) {
				try {
					if (!cwb_id[3].equals("") && !cwb_id[0].equals("0") && (cwb_id[2].split("_").length == 2) && !cwb_id[2].split("_")[0].equals("0") && !cwb_id[2].split("_")[1].equals("0")) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = new Date();
						String nowtime = df.format(date);

						// AbnormalOrder
						// ab=abnormalOrderDAO.getAbnormalOrderById(Long.parseLong(cwb_id[2].split("_")[1]));

						this.abnormalOrderDAO.saveAbnormalOrderByid(Long.parseLong(cwb_id[2].split("_")[1]), Long.parseLong(cwb_id[2].split("_")[0]), cwb_id[1]);

						// 改成修改 abnormalenum 添加新的类型

						this.abnormalWriteBackDAO.creAbnormalOrder(Long.parseLong(cwb_id[0]), cwb_id[1], this.getSessionUser().getUserid(), AbnormalWriteBackEnum.XiuGai.getValue(), nowtime,
								Long.parseLong(cwb_id[2].split("_")[1]), Long.parseLong(cwb_id[2].split("_")[0]), cwb_id[3]);
						AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderByOId(Long.parseLong(cwb_id[2].split("_")[1]));
						CwbOrder co = this.cwbDAO.getCwbOrderByOpscwbid(Long.parseLong(cwb_id[0]));
						List<User> kefurole = this.userDAO.getUserByRole(1);
						if (abnormalOrder != null) {
							for (User user : kefurole) {
								String json = "订单：" + co.getCwb() + "待处理";
								this.appearWindowDao.creWindowTime(json, "3", user.getUserid(), "1");
							}
						}
						successCount++;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount;
	}

	/**
	 * 问题件处理查询功能
	 *
	 * @param page
	 * @param model
	 * @param cwb
	 * @param branchid
	 * @param abnormaltypeid
	 * @param ishandle
	 * @return
	 */
	@RequestMapping("/toHandleabnormal/{page}")
	public String toHandleabnormal(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid,
			@RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "chuangjianbegindate", required = false, defaultValue = "") String chuangjianbegindate,
			@RequestParam(value = "chuangjianenddate", required = false, defaultValue = "") String chuangjianenddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		String quot = "'", quotAndComma = "',";
		StringBuffer cwbs1 = new StringBuffer();
		if (cwb.length() > 0) {
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbs1 = cwbs1.append(quot).append(cwbStr.trim()).append(quotAndComma);
			}
		}
		String cwbs = "";
		if (cwbs1.length() > 0) {
			cwbs = cwbs1.substring(0, cwbs1.length() - 1);
		}

		// 根据时间去abnormalWriteBack表查询符合条件的opscwbid

		List<JSONObject> abnormalOrderList = new ArrayList<JSONObject>();
		int count = 0;

		if (isshow == 1) {
			if (ishandle == 1) {
				if (begindate.length() == 0) {
					begindate = DateTimeUtil.getDateBefore(1);
				}
				if (enddate.length() == 0) {
					enddate = DateTimeUtil.getNowTime();
				}

				count = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(cwbs, branchid, abnormaltypeid, ishandle, begindate, enddate);
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(page, cwbs, branchid, abnormaltypeid, ishandle, begindate, enddate);

			} else {
				if (chuangjianbegindate.length() == 0) {
					chuangjianbegindate = DateTimeUtil.getDateBefore(1);
				}
				if (chuangjianenddate.length() == 0) {
					chuangjianenddate = DateTimeUtil.getNowTime();
				}
				// count=abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(cwbs,branchid,
				// abnormaltypeid, ishandle,begindate,enddate);
				// abnormalOrderList=abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(page,cwbs,branchid,
				// abnormaltypeid, ishandle,begindate,enddate);

				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByCredatetimeofpage(page, chuangjianbegindate, chuangjianenddate, cwbs, branchid, abnormaltypeid, ishandle);
				count = this.abnormalOrderDAO.getAbnormalOrderByCredatetimeCount(chuangjianbegindate, chuangjianenddate, cwbs, branchid, abnormaltypeid, ishandle);

			}
		}
		// 根据条件查询和上一步中查出的opscwbid来查询
		List<Branch> branchs = this.branchDAO.getAllEffectBranches();
		List<User> users = this.userDAO.getAllUser();
		List<Customer> customers = this.customerDAO.getAllCustomers();
		List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
		List<AbnormalView> views = this.abnormalService.setViews(abnormalOrderList, branchs, users, customers, atlist);
		/*
		 * List<AbnormalView> viewForShow=new ArrayList<AbnormalView>(); for
		 * (int i = (int) ((page-1)*Page.ONE_PAGE_NUMBER); i <
		 * Page.ONE_PAGE_NUMBER*page&&i<views.size(); i++) { viewFo
		 * rShow.add(views.get(i)); }
		 */

		model.addAttribute("chuangjianbegindate", chuangjianbegindate);
		model.addAttribute("chuangjianenddate", chuangjianenddate);

		model.addAttribute("branchList", branchs);
		model.addAttribute("abnormalTypeList", atlist);
		model.addAttribute("views", views);
		model.addAttribute("cwb", cwb);
		model.addAttribute("ishandle", ishandle);
		/*
		 * model.addAttribute("page_obj", new
		 * Page(views.size(),page,Page.ONE_PAGE_NUMBER));
		 */

		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		return "/abnormalorder/handleabnormallist";
	}

	@RequestMapping("getabnormalOrder/{id}")
	public String getabnormalOrder(@PathVariable("id") long id, Model model, @RequestParam(value = "type", required = false, defaultValue = "0") long type) {
		AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderById(id);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("abnormalOrder", abnormalOrder);
		model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("cwborder", this.cwbDAO.getCwbOrderByOpscwbid(abnormalOrder.getOpscwbid()));
		model.addAttribute("abnormalWriteBackList", this.abnormalWriteBackDAO.getAbnormalOrderByOrderid(id));
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		Role role = this.roleDao.getRolesByRoleid(this.getSessionUser().getRoleid());
		model.addAttribute("role", role);
		if (type == 1) {
			return "/abnormalorder/nowhandleabnormal";
		} else if (type == 2) {
			return "/abnormalorder/nowreplyabnormal";
		}
		return null;
	}

	@RequestMapping("/SubmitHandleabnormal/{id}")
	public @ResponseBody String SubmitHandleabnormal(@PathVariable("id") long id, @RequestParam(value = "describe", defaultValue = "", required = false) String describe,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
			this.abnormalOrderDAO.saveAbnormalOrderForIshandle(id, AbnormalOrderHandleEnum.yichuli.getValue(), nowtime);
			this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
					ab.getAbnormaltypeid(), cwb);
			/*
			 * String json = "订单：" + cwb + "已处理";
			 * this.appearWindowDao.creWindowTime(json, "4", ab.getCreuserid(),
			 * "1");
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	/**
	 * 问题件回复
	 *
	 * @param page
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/toReplyabnormal/{page}")
	public String toReplyabnormal(@PathVariable("page") long page, Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid) {
		if (begindate.length() == 0) {
			begindate = DateTimeUtil.getDateBefore(1);
		}
		if (enddate.length() == 0) {
			enddate = DateTimeUtil.getNowTime();
		}
		List<AbnormalOrder> abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByWhere(page, begindate, enddate, ishandle, abnormaltypeid, this.getSessionUser().getUserid());
		model.addAttribute("abnormalOrderList", abnormalOrderList);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("page_obj", new Page(this.abnormalOrderDAO.getAbnormalOrderCount(begindate, enddate, ishandle, abnormaltypeid, this.getSessionUser().getUserid()), page,
				Page.ONE_PAGE_NUMBER));
		return "/abnormalorder/replyabnormallist";
	}

	@RequestMapping("/SubmitReplyabnormal/{id}")
	public @ResponseBody String SubmitReplyabnormal(@PathVariable("id") long id, @RequestParam(value = "describe", defaultValue = "", required = false) String describe,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderById(id);
			this.abnormalOrderDAO.saveAbnormalOrderForNohandle(id, AbnormalOrderHandleEnum.WeiChuLi.getValue());
			this.abnormalWriteBackDAO.creAbnormalOrder(abnormalOrder.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.HuiFu.getValue(), nowtime,
					abnormalOrder.getId(), abnormalOrder.getAbnormaltypeid(), cwb);
			/*
			 * String json = "订单：" + cwb + "待处理"; List<User> kefurole =
			 * this.userDAO.getUserByRole(1); if (abnormalOrder != null) { for
			 * (User user : kefurole) { this.appearWindowDao.creWindowTime(json,
			 * "3", user.getUserid(), "1"); } }
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	@RequestMapping("/abnormaldataMove")
	public String abnormaldataMove(@RequestParam(value = "opscwbids", defaultValue = "", required = false) String opscwbids,
			@RequestParam(value = "ishandle", defaultValue = "0", required = false) long ishandle) {
		try {
			StringBuffer w = new StringBuffer();
			w.append("");
			if (!opscwbids.equals("")) {
				for (String opscwbid : opscwbids.split("\r\n")) {
					w.append("'" + opscwbid.trim() + "',");
				}
			}
			w.append("''");
			if (ishandle > 0) {
				List<AbnormalWriteBack> abnormalWriteBack = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbids(w.toString());

				List<String> cwbList = this.cwbDAO.getCwbByOpscwbids(w.toString());
				for (String cwb : cwbList) {
					CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
					this.abnormalOrderDAO.abnormaldataMove(co);
					this.abnormalWriteBackDAO.abnormaldataMoveofcwb(co);
				}
				for (AbnormalWriteBack awb : abnormalWriteBack) {
					this.abnormalOrderDAO.abnormaldataMoveofhandletime(awb);
				}
			}
			return "/abnormalorder/abnormaldataMove";
		} catch (Exception e) {
			return "/abnormalorder/abnormaldataMove";
		}
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[8]; // 导出的列名
		String[] cloumnName2 = new String[8]; // 导出的英文列名

		this.exportService.SetAbnormalOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "问题件信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "AbnormalOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String cwb = request.getParameter("cwbStrs") == null ? "" : request.getParameter("cwbStrs").toString();
			long branchid = request.getParameter("branchid1") == null ? 0 : Long.parseLong(request.getParameter("branchid1").toString());
			long abnormaltypeid = request.getParameter("abnormaltypeid1") == null ? 0 : Long.parseLong(request.getParameter("abnormaltypeid1").toString());
			long ishandle = request.getParameter("ishandle1") == null ? 0 : Long.parseLong(request.getParameter("ishandle1").toString());

			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1");
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1");
			String chuangjianbegindate = request.getParameter("chuangjianbegindate1") == null ? "" : request.getParameter("chuangjianbegindate1");
			String chuangjianenddate = request.getParameter("chuangjianenddate1") == null ? "" : request.getParameter("chuangjianenddate1");

			String quot = "'", quotAndComma = "',";
			StringBuffer cwbs1 = new StringBuffer();
			if (cwb.length() > 0) {
				for (String cwbStr : cwb.split("\r\n")) {
					if (cwbStr.trim().length() == 0) {
						continue;
					}
					cwbs1 = cwbs1.append(quot).append(cwbStr).append(quotAndComma);
				}
			}
			String cwbs = new String();
			if (cwbs1.length() > 0) {
				cwbs = cwbs1.substring(0, cwbs1.length() - 1);
			}
			// 根据时间去abnormalWriteBack表查询符合条件的opscwbid
			// List<String> abnormalWriteBackOpscwbidList = new
			// ArrayList<String>();
			List<JSONObject> abnormalOrderList = new ArrayList<JSONObject>();
			if (ishandle == 2) {

				if (chuangjianbegindate.length() == 0) {
					chuangjianbegindate = DateTimeUtil.getDateBefore(1);
				}
				if (chuangjianenddate.length() == 0) {
					chuangjianenddate = DateTimeUtil.getNowTime();
				}
				// abnormalWriteBackOpscwbidList =
				// this.abnormalOrderDAO.getAbnormalOrderByCredatetime(chuangjianbegindate,
				// chuangjianenddate);
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndNohandles(chuangjianbegindate, chuangjianenddate, cwbs, branchid,
						abnormaltypeid, ishandle);

			} else {
				if (begindate.length() == 0) {
					begindate = DateTimeUtil.getDateBefore(1);
				}
				if (enddate.length() == 0) {
					enddate = DateTimeUtil.getNowTime();
				}
				// abnormalWriteBackOpscwbidList =
				// this.abnormalWriteBackDAO.getAbnormalOrderByCredatetime(begindate,
				// enddate);
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandles(begindate, enddate, cwbs, branchid, abnormaltypeid, ishandle);
			}

			// 根据条件查询和上一步中查出的opscwbid来查询
			// abnormalOrderList =
			// this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(this.getStrings(abnormalWriteBackOpscwbidList),
			// cwbs, branchid,
			// abnormaltypeid, ishandle);

			List<Branch> branchs = this.branchDAO.getAllBranches();
			List<User> users = this.userDAO.getAllUser();
			List<Customer> customers = this.customerDAO.getAllCustomers();
			List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
			final List<AbnormalView> views = this.abnormalService.setViews(abnormalOrderList, branchs, users, customers, atlist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = AbnormalOrderController.this.exportService.setAbnormalOrderObject(cloumnName3, views, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/SubmitOverabnormal/{id}")
	public @ResponseBody String SubmitOverabnormal(@PathVariable("id") long id, @RequestParam(value = "describe2", defaultValue = "", required = false) String describe2,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
			this.abnormalOrderDAO.saveAbnormalOrderForIshandle(id, AbnormalOrderHandleEnum.yichuli.getValue(), nowtime);
			this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe2, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
					ab.getAbnormaltypeid(), cwb);
			/*
			 * String json = "订单：" + cwb + "已处理";
			 * this.appearWindowDao.creWindowTime(json, "4", ab.getCreuserid(),
			 * "1");
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	public String getStrings(List<String> strArr) {
		String strs = "0,";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getString(List<Long> list) {
		String str = "0,";
		if (list.size() > 0) {
			for (Long num : list) {
				str += num + ",";
			}

		}
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	@RequestMapping("/SubmitHandleabnormalBatch")
	public @ResponseBody String SubmitHandleabnormalBatch(@RequestParam(value = "ids", defaultValue = "", required = false) String ids,
			@RequestParam(value = "describe", defaultValue = "", required = false) String describe) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			String[] arrayIds = ids.split(",");

			for (int i = 0; i < arrayIds.length; i++) {
				int id = Integer.parseInt(arrayIds[i]);
				AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
				this.abnormalOrderDAO.saveAbnormalOrderForIshandle(id, AbnormalOrderHandleEnum.yichuli.getValue(), nowtime);
				this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
						ab.getAbnormaltypeid(), ab.getCwb());
				/*
				 * String json = "订单：" + ab.getCwb() + "已处理";
				 * this.appearWindowDao.creWindowTime(json, "4",
				 * ab.getCreuserid(), "1");
				 */
			}
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	@RequestMapping("gotoBatch")
	public String gotoBatch(Model model, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		model.addAttribute("ids", ids);
		return "/abnormalorder/nowhandleabnormalBatch";
	}
}