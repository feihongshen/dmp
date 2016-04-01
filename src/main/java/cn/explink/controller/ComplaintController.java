package cn.explink.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Complaint;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.service.ComplaintService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.SmsSendService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.HttpUtil;
import cn.explink.util.Page;

@RequestMapping("/complaint")
@Controller
public class ComplaintController {

	@Autowired
	ComplaintDAO complaintDao;
	@Autowired
	ComplaintService complaintService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	SmsSendService smsSendService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ReasonDao reasonDao;

	@Autowired
	ExportService exportService;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = LoggerFactory.getLogger(ComplaintController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "type", required = false, defaultValue = "-1") long type, @RequestParam(value = "auditType", required = false, defaultValue = "-1") long auditType,
			@RequestParam(value = "starteTime", required = false, defaultValue = "") String starteTime, @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {

		List<Complaint> clist = new ArrayList<Complaint>();
		List<ComplaintView> cViewlist = new ArrayList<ComplaintView>();
		Page pageparm = new Page();
		if (isshow != 0) {
			clist = this.complaintService.getListByCwbsAndWhere(cwbs, type, auditType, starteTime, endTime, page);
			pageparm = new Page(this.complaintService.getCountByCwbsAndWhere(cwbs, type, auditType, starteTime, endTime), page, Page.ONE_PAGE_NUMBER);
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<User> userList = this.userDAO.getAllUser();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			cViewlist = this.complaintService.getComplaintView(clist, customerList, userList, branchList);
		}
		model.addAttribute("complaintlist", cViewlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "complaint/wherelist";
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		long id = request.getParameter("id") == null ? 0l : Long.parseLong(request.getParameter("id"));
		long branchid = request.getParameter("branchid") == null ? 0l : Long.parseLong(request.getParameter("branchid"));
		long deliveryid = request.getParameter("deliveryid") == null ? 0l : Long.parseLong(request.getParameter("deliveryid"));
		long servertreasonid = request.getParameter("servertreasonid") == null ? 0l : Long.parseLong(request.getParameter("servertreasonid"));

		long smschack = request.getParameter("smschack") == null ? 0l : Long.parseLong(request.getParameter("smschack"));
		long type = request.getParameter("type") == null ? 0l : Long.parseLong(request.getParameter("type"));
		String content = request.getParameter("content");
		String cwb = request.getParameter("cwb");
		String sms = "";// 短信发送结果
		if (type == ComplaintTypeEnum.CuijianTousu.getValue()) {// 催件投诉
			String rukuTime = request.getParameter("rukuTime");
			String emaildate = request.getParameter("emaildate");
			if ((rukuTime != null) && !rukuTime.equals("")) {
				Date ruku;
				try {
					ruku = this.df.parse(rukuTime);
					if (ruku != null) {
						Date nowdate = new Date();
						long time = nowdate.getTime() - ruku.getTime();
						int chaoshiTime = 12;
						SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("rukuChaoshiTime");
						if (siteDayLogTime != null) {
							try {
								chaoshiTime = Integer.parseInt(siteDayLogTime.getValue());
							} catch (Exception e) {
								return "{\"errorCode\":0,\"error\":\"保存失败，系统设置的入库超时时间不是数字!\"}";
							}
						}
						if ((time / 1000 / 60 / 60) <= chaoshiTime) {// 不超过12小时
							return "{\"errorCode\":0,\"error\":\"保存失败，" + chaoshiTime + "小时内的订单不予受理投诉!\"}";
						}
					}
				} catch (ParseException e) {
					logger.error("", e);
				}

			} else {
				Date ruku;
				try {
					ruku = this.df.parse(emaildate);
					if (ruku != null) {
						Date nowdate = new Date();
						long time = nowdate.getTime() - ruku.getTime();
						int chaoshiTime = 12;
						SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("rukuChaoshiTime");
						if (siteDayLogTime != null) {
							try {
								chaoshiTime = Integer.parseInt(siteDayLogTime.getValue());
							} catch (Exception e) {
								return "{\"errorCode\":0,\"error\":\"保存失败，系统设置的入库超时时间不是数字!\"}";
							}
						}
						if ((time / 1000 / 60 / 60) <= chaoshiTime) {// 不超过12小时
							return "{\"errorCode\":0,\"error\":\"保存失败，" + chaoshiTime + "小时内的订单不予受理投诉!\"}";
						}
					}
				} catch (ParseException e) {
					logger.error("", e);
				}
			}
		}
		Complaint complaint = null;
		if (id > 0) {
			complaint = this.complaintDao.getComplaintById(id);
		} else {
			List<Complaint> complaintList = this.complaintDao.getComplaintByCwbAndType(cwb, type);
			if ((complaintList != null) && (complaintList.size() > 0)) {
				complaint = complaintList.get(0);
			}
		}
		if (smschack == 1) {// 发短信
			User delivery = this.userDAO.getUserByUserid(deliveryid);
			this.logger.info("投诉发送短信，小件员:{},手机号：{}", delivery.getRealname(), delivery.getUsermobile());
			if ((delivery.getUsermobile() == null) || delivery.getUsermobile().equals("")) {
				this.logger.info("投诉发送短信，小件员:{},手机号为空不能发短信");
				sms = "小件员:" + delivery.getRealname() + ",手机号为空不能发短信";
			} else {
				try {
					String smsMsg = "订单号：[" + cwb + "]" + content;
					String smsResult = this.smsSendService.sendSms(delivery.getUsermobile(), smsMsg, 1, 0, delivery.getRealname(), this.getSessionUser().getUserid(), HttpUtil.getUserIp(request));
					this.logger.info("投诉发送短信，小件员:{},短信发送结果：{}", delivery.getRealname(), smsResult);
					sms = "催件投诉短信发送结果：小件员[" + delivery.getRealname() + "]:" + smsResult + "";
				} catch (UnsupportedEncodingException e) {
					sms = "催件投诉短信发送 短信平台异常！";
					this.logger.info("投诉发送短信，小件员:{},手机号：{} ，短信发送异常！", delivery.getRealname(), delivery.getUsermobile());
				}
			}
		}
		complaint = new Complaint();
		complaint.setType(type);
		complaint.setCwb(cwb);
		complaint.setBranchid(branchid);
		complaint.setDeliveryid(deliveryid);
		complaint.setContent(content);
		complaint.setCreateTime(this.df.format(new Date()));
		complaint.setCreateUser(this.getSessionUser().getUserid());
		complaint.setServertreasonid(servertreasonid);
		try {
			this.complaintDao.saveComplaint(complaint);
			return "{\"errorCode\":0,\"error\":\"保存成功!" + sms + "\"}";
		} catch (Exception e) {
			logger.error("", e);
			return "{\"errorCode\":1,\"error\":\"保存失败!" + sms + "\"}";
		}
	}

	@RequestMapping("/update")
	public @ResponseBody
	String update(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		long id = request.getParameter("id") == null ? 0l : Long.parseLong(request.getParameter("id"));
		long auditType = request.getParameter("auditType") == null ? 0l : Long.parseLong(request.getParameter("auditType"));
		String auditRemark = request.getParameter("auditRemark");

		Complaint complaint = this.complaintDao.getComplaintById(id);
		if (complaint == null) {
			return "{\"errorCode\":1,\"error\":\"保存失败，没有查到该投诉!\"}";
		}
		complaint.setAuditRemark(auditRemark);
		complaint.setAuditTime(this.df.format(new Date()));
		complaint.setAuditUser(this.getSessionUser().getUserid());
		complaint.setAuditType(auditType);
		long result = this.complaintService.updateComplaint(complaint);
		if (result == 1) {
			return "{\"errorCode\":0,\"error\":\"保存成功!\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"保存失败!\"}";
		}
	}

	@RequestMapping("/show/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		Complaint complaint = this.complaintDao.getComplaintById(id);
		ComplaintView complaintView = new ComplaintView();
		complaintView.setCwb(complaint.getCwb());
		CwbOrder order = this.cwbDAO.getCwbByCwb(complaint.getCwb());
		if (order != null) {
			Customer customer = this.customerDAO.getCustomerById(order.getCustomerid());
			if (order.getDeliverid() > 0) {
				User dUser = this.userDAO.getUserByUserid(order.getDeliverid());
				complaintView.setCwbdelivername(dUser.getRealname());// 订单本身的小件员
			}
			complaintView.setCustomername(customer.getCustomername());// 供货商
			Date ruku = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.RuKu.getValue()).getCredate();
			Date daohuosaomiao = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()).getCredate();
			daohuosaomiao = daohuosaomiao == null ? this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()).getCredate()
					: daohuosaomiao;
			complaintView.setInstoreroomtime(ruku != null ? this.df.format(ruku) : "");// 入库时间
			complaintView.setInSitetime(daohuosaomiao != null ? this.df.format(daohuosaomiao) : "");// 到站时间
			DeliveryState deliverystate = this.complaintService.getDeliveryByCwb(order.getCwb());
			if (deliverystate != null) {
				complaintView.setDeliverystate(deliverystate.getDeliverystate());
			}
			complaintView.setEmaildate(order.getEmaildate());
			complaintView.setConsigneename(order.getConsigneename());
			complaintView.setOrderflowtype(order.getFlowordertype());
		}
		User cUser = this.userDAO.getUserByUserid(complaint.getDeliveryid());
		complaintView.setDelivername(cUser.getRealname());// 投诉小件员
		Branch branch = this.branchDAO.getBranchByBranchid(complaint.getBranchid());
		complaintView.setBranchname(branch.getBranchname());// 投诉站点
		complaintView.setId(complaint.getId());
		complaintView.setType(complaint.getType());
		complaintView.setAuditType(complaint.getAuditType());
		complaintView.setContent(complaint.getContent());
		complaintView.setAuditRemark(complaint.getAuditRemark());
		complaintView.setCreateTime(complaint.getCreateTime());
		complaintView.setBranchid(complaint.getBranchid());
		complaintView.setDeliveryid(complaint.getDeliveryid());
		complaintView.setReplyDetail(complaint.getReplyDetail());
		model.addAttribute("complaintView", complaintView);
		return "/complaint/whereshow";
	}

	@RequestMapping("/showbyType/{cwb}/{type}")
	public String showbyType(Model model, @PathVariable("cwb") String cwb, @PathVariable("type") long type) {
		CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
		CwbOrderView cwbOrderView = new CwbOrderView();
		User dUser = null;
		if (order != null) {
			cwbOrderView.setCwb(order.getCwb());
			cwbOrderView.setDeliverid(order.getDeliverid());
			cwbOrderView.setCurrentbranchid(order.getCurrentbranchid());
			cwbOrderView.setFlowordertype(order.getFlowordertype());
			cwbOrderView.setEmaildate(order.getEmaildate());
			Date ruku = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.RuKu.getValue()).getCredate();
			Date daohuosaomiao = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()).getCredate();
			daohuosaomiao = daohuosaomiao == null ? this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()).getCredate()
					: daohuosaomiao;
			cwbOrderView.setInstoreroomtime(ruku != null ? this.df.format(ruku) : "");// 入库时间
			cwbOrderView.setInSitetime(daohuosaomiao != null ? this.df.format(daohuosaomiao) : "");// 到站时间
			Branch branch = this.branchDAO.getBranchByBranchid(order.getCurrentbranchid());
			Customer customer = this.customerDAO.getCustomerById(order.getCustomerid());
			cwbOrderView.setCurrentbranchname(branch.getBranchname());// 当前所在机构名称
			cwbOrderView.setCustomername(customer.getCustomername());// 供货商的名称
			cwbOrderView.setConsigneemobile(order.getConsigneemobile());
			cwbOrderView.setConsigneephone(order.getConsigneephone());
			if (order.getDeliverid() > 0) {
				dUser = this.userDAO.getUserByUserid(order.getDeliverid());
				if (branch.getBranchid() == 0) {
					branch = this.branchDAO.getBranchByBranchid(dUser.getBranchid());
					cwbOrderView.setCurrentbranchname(branch.getBranchname());// 当前所在机构名称
					cwbOrderView.setCurrentbranchid(branch.getBranchid());
				}

				cwbOrderView.setDelivername(dUser.getRealname());// 小件员
				List<User> nowUserList = this.userDAO.getAllUserbybranchid(dUser.getBranchid());
				model.addAttribute("userList", nowUserList);
			}
		}
		Complaint complaint = null;
		if (type == ComplaintTypeEnum.CuijianTousu.getValue()) {// 催件投诉
			List<Complaint> list = this.complaintDao.getComplaintByCwbAndType(cwb, ComplaintTypeEnum.CuijianTousu.getValue());
			List<User> nowUserList = new ArrayList<User>();
			if (dUser != null) {
				nowUserList = this.userDAO.getAllUserbybranchid(dUser.getBranchid());
			} else {
				nowUserList = this.userDAO.getAllUserbybranchid(order.getCurrentbranchid());
			}
			if ((list != null) && (list.size() > 0)) {
				complaint = list.get(0);
			}
			model.addAttribute("userList", nowUserList);
			model.addAttribute("branchList", this.branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue()));
			model.addAttribute("complaint", complaint);
			model.addAttribute("cwbOrderView", cwbOrderView);
			return "/complaint/show/cuijiantousu";
		}
		if (type == ComplaintTypeEnum.FuwuTousu.getValue()) {// 服务投诉
			List<Complaint> list = this.complaintDao.getComplaintByCwbAndType(cwb, ComplaintTypeEnum.FuwuTousu.getValue());
			List<User> nowUserList = new ArrayList<User>();
			if (dUser != null) {
				nowUserList = this.userDAO.getAllUserbybranchid(dUser.getBranchid());
			} else {
				nowUserList = this.userDAO.getAllUserbybranchid(order.getCurrentbranchid());
			}
			if ((list != null) && (list.size() > 0)) {
				complaint = list.get(0);
			}
			model.addAttribute("userList", nowUserList);
			model.addAttribute("branchList", this.branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue()));
			model.addAttribute("complaint", complaint);
			model.addAttribute("cwbOrderView", cwbOrderView);
			model.addAttribute("reasonList", this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.FuWuTouSuLeiXing.getValue()));
			return "/complaint/show/fuwutousu";
		}

		if (type == ComplaintTypeEnum.KefuBeizhu.getValue()) {// 客服备注
			List<Complaint> list = this.complaintDao.getComplaintByCwbAndType(cwb, ComplaintTypeEnum.KefuBeizhu.getValue());
			List<User> nowUserList = new ArrayList<User>();
			if (dUser != null) {
				nowUserList = this.userDAO.getAllUserbybranchid(dUser.getBranchid());
			} else {
				nowUserList = this.userDAO.getAllUserbybranchid(order.getCurrentbranchid());
			}
			if ((list != null) && (list.size() > 0)) {
				complaint = list.get(0);
			}
			model.addAttribute("userList", nowUserList);
			model.addAttribute("branchList", this.branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue()));
			model.addAttribute("complaint", complaint);
			model.addAttribute("cwbOrderView", cwbOrderView);
			return "/complaint/show/kefubeizhu";
		}
		return "/complaint/show/cuijiantousu";
	}

	@RequestMapping("/get/{cwb}/{type}")
	public @ResponseBody
	JSONObject getObject(@PathVariable("cwb") String cwb, @PathVariable("type") long type) {
		JSONObject obj = new JSONObject();
		CwbOrderView cwbOrderView = new CwbOrderView();

		obj = JSONObject.fromObject(cwbOrderView);
		return obj;
	}

	@RequestMapping("/showByType/{id}")
	public @ResponseBody
	JSONObject showByType(@PathVariable("id") long id) {
		JSONObject obj = new JSONObject();
		Complaint complaint = this.complaintDao.getComplaintById(id);
		ComplaintView complaintView = new ComplaintView();
		complaintView.setCwb(complaint.getCwb());
		CwbOrder order = this.cwbDAO.getCwbByCwb(complaint.getCwb());
		if (order != null) {
			Customer customer = this.customerDAO.getCustomerById(order.getCustomerid());
			if (order.getDeliverid() > 0) {
				User dUser = this.userDAO.getUserByUserid(order.getDeliverid());
				complaintView.setCwbdelivername(dUser.getRealname());// 订单本身的小件员
			}
			complaintView.setCustomername(customer.getCustomername());// 供货商
			Date ruku = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.RuKu.getValue()).getCredate();
			Date daohuosaomiao = this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()).getCredate();
			daohuosaomiao = daohuosaomiao == null ? this.complaintService.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()).getCredate()
					: daohuosaomiao;
			complaintView.setInstoreroomtime(ruku != null ? this.df.format(ruku) : "");// 入库时间
			complaintView.setInSitetime(daohuosaomiao != null ? this.df.format(daohuosaomiao) : "");// 到站时间
			DeliveryState deliverystate = this.complaintService.getDeliveryByCwb(order.getCwb());
			if (deliverystate != null) {
				complaintView.setDeliverystate(deliverystate.getDeliverystate());
			}
			complaintView.setEmaildate(order.getEmaildate());
			complaintView.setConsigneename(order.getConsigneename());
			complaintView.setOrderflowtype(order.getFlowordertype());
		}
		User cUser = this.userDAO.getUserByUserid(complaint.getDeliveryid());
		complaintView.setDelivername(cUser.getRealname());// 投诉小件员
		Branch branch = this.branchDAO.getBranchByBranchid(complaint.getBranchid());
		complaintView.setBranchname(branch.getBranchname());// 投诉站点
		complaintView.setId(complaint.getId());
		complaintView.setType(complaint.getType());
		complaintView.setAuditType(complaint.getAuditType());
		complaintView.setContent(complaint.getContent());
		complaintView.setAuditRemark(complaint.getAuditRemark());
		complaintView.setCreateTime(complaint.getCreateTime());
		complaintView.setBranchid(complaint.getBranchid());
		complaintView.setDeliveryid(complaint.getDeliveryid());
		obj = JSONObject.fromObject(complaintView);
		return obj;
	}

	@RequestMapping("/delete/{id}")
	public @ResponseBody
	String delete(@PathVariable("id") long id) {
		this.complaintDao.deleteComplaint(id);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[20]; // 导出的列名
		String[] cloumnName2 = new String[20]; // 导出的英文列名

		this.exportService.SetCompitFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "投诉信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String cwbs = request.getParameter("cwbs1") == null ? "" : request.getParameter("cwbs1").toString();
			long type = request.getParameter("type1") == null ? -1 : Long.parseLong(request.getParameter("type1").toString());
			long auditType = request.getParameter("auditType1") == null ? -1 : Long.parseLong(request.getParameter("auditType1").toString());
			String starteTime = request.getParameter("starteTime1") == null ? "" : request.getParameter("starteTime1").toString();
			String endTime = request.getParameter("endTime1") == null ? "" : request.getParameter("endTime1").toString();

			List<Complaint> clist = new ArrayList<Complaint>();
			clist = this.complaintService.getListByCwbsAndWhereNoPage(cwbs, type, auditType, starteTime, endTime);
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			final List<User> userList = this.userDAO.getAllUser();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			final List<ComplaintView> cViewlist = this.complaintService.getComplaintView(clist, customerList, userList, branchList);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < cViewlist.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = ComplaintController.this.exportService.setObjectB(cloumnName3, userList, request1, cViewlist, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@RequestMapping("/cuijiantousufozhanzhang/{page}")
	public String cuijiantousuzhanzhang(@PathVariable("page") long page, Model model, @RequestParam(value = "auditType", required = false, defaultValue = "-1") long auditType,
			@RequestParam(value = "startid", required = false, defaultValue = "") String starteTime, @RequestParam(value = "endid", required = false, defaultValue = "") String endTime,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, @RequestParam(value = "complaintType", required = false, defaultValue = "0") long complaintType) {
		List<User> nowUserList = new ArrayList<User>();
		List<Complaint> list = new ArrayList<Complaint>();
		if ((starteTime.length() > 0) && (endTime.length() > 0)) {
			long branchid = this.getSessionUser().getBranchid();
			list = this.complaintDao.getComplaintForzhandian(cwb, complaintType, auditType, branchid, starteTime, endTime, page);
			nowUserList = this.userDAO.getAllUserbybranchid(branchid);
			model.addAttribute("page_obj", new Page(this.complaintDao.getComplaintCountForzhandian(cwb, complaintType, auditType, branchid, starteTime, endTime), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("startid", starteTime);
			model.addAttribute("endid", endTime);
		}

		model.addAttribute("auditType", auditType);
		model.addAttribute("complaintType", complaintType);
		model.addAttribute("page", page);
		model.addAttribute("nowUserList", nowUserList);
		model.addAttribute("List", list);
		model.addAttribute("branchList", this.branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue()));
		return "/complaint/show/cuijiantousuzhandian";
	}

	@RequestMapping("/updateZD/{id}")
	public @ResponseBody
	String updateZhandain(Model model, @PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "replyDetail", required = false, defaultValue = "") String replyDetail) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		Complaint complaint = this.complaintDao.getComplaintById(id);
		if (complaint == null) {
			return "{\"errorCode\":1,\"error\":\"保存失败，没有查到该投诉!\"}";
		}
		String time = DateTimeUtil.getNowTime();

		long result = this.complaintDao.updateComplaintCountForzhandian(0, time, this.getSessionUser().getUserid(), id, replyDetail);
		if (result == 1) {
			return "{\"errorCode\":0,\"error\":\"保存成功!\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"保存失败!\"}";
		}
	}

	@RequestMapping("/detail/{id}")
	public String edit(Model model, @PathVariable("id") int id) throws Exception {
		List<Branch> branchlist = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		Complaint complaint = this.complaintDao.getComplaintById(id);
		List<Customer> customers = this.customerDAO.getAllCustomers();
		model.addAttribute("customers", customers);
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("userList", userList);
		model.addAttribute("complaint", complaint);
		return "/complaint/detail";
	}
}
