package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.PrintcwbDetailDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.domain.PrintView;
import cn.explink.domain.PrintcwbDetail;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.print.template.PrintTemplate;
import cn.explink.print.template.PrintTemplateDAO;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.WarehouseGroupDetailService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Controller
@RequestMapping("/warehousegroupdetail")
public class WarehouseGroup_detailController {

	@Autowired
	OutWarehouseGroupDAO outwarehousegroupDao;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	EmailDateDAO emailDateDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	PrintcwbDetailDAO printcwbDetailDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	PrintTemplateDAO printTemplateDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	GroupDetailDao groupDetailDao;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	WarehouseGroupDetailService warehouseGroupDetailService;

	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	BaleDao baleDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询入库信息
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/inlist/{page}")
	public String inlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate,
			@RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber // 每页记录数
	) {
		List<User> userList = userDAO.getAllUserbybranchid(getSessionUser().getBranchid());

		model.addAttribute("userList", userList);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 5);
		if (beginemaildate.length() == 0 && endemaildate.length() == 0) {
			beginemaildate = df.format(new Date(day7 * 1000));
			endemaildate = df.format(new Date());
		}

		List<OrderFlow> oflist = orderFlowDAO.getOrderFlowByWhere(page, getSessionUser().getBranchid(), userid, FlowOrderTypeEnum.RuKu.getValue(), beginemaildate, endemaildate, onePageNumber);

		model.addAttribute("oflist", oflist);
		model.addAttribute("page_obj", new Page(orderFlowDAO.getOrderFlowCount(getSessionUser().getBranchid(), userid, FlowOrderTypeEnum.RuKu.getValue(), beginemaildate, endemaildate), page,
				onePageNumber));
		model.addAttribute("page", page);

		return "warehousegroup/inlist";
	}

	/**
	 * 入库交接单信息打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/inlistprint")
	public String inlistprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {

		String cwbs = "", cwbstr = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
		}

		List<CwbOrder> cwbList = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));

		printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.RuKu.getValue()));

		SystemInstall si = systemInstallDAO.getSystemInstallByName("rukujiaojiedan");
		model.addAttribute("template", printTemplateDAO.getPrintTemplate(Long.parseLong(si.getValue())));
		model.addAttribute("cwbList", cwbList);
		// model.addAttribute("localbranchname",branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", customerDAO.getAllCustomers());

		// model.addAttribute("branchname",branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/inbillprinting_template";
	}

	private List<Branch> getNextPossibleBranches() {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : cwbRouteService.getNextPossibleBranch(getSessionUser().getBranchid())) {
			Branch branch = branchDAO.getBranchByBranchid(i);
			if (branch.getBranchid() != 0) {
				bList.add(branchDAO.getBranchByBranchid(i));
			}
		}
		return bList;
	}

	/**
	 * 
	 * @param model
	 * @param request
	 * @param isprint
	 * @param isback
	 * @param iscustomer
	 * @param islinghuo
	 * @param deliverid
	 * @param nextbranchid
	 * @param printtemplateid
	 * @return
	 */
	@RequestMapping("/outbillprinting_default")
	public String outbillprinting_default(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "currentdeliverid", defaultValue = "0", required = true) long deliverid,
			@RequestParam(value = "nextbranchid", defaultValue = "", required = false) String nextbranchid,
			@RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid) {
		String cwbs = "", cwbstr = "", cwbhuizongstr = "";
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
			cwbArr[i] = isprint[i];
			cwbhuizongstr += isprint[i] + "-H-";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = cwbDao.getCwbByCwbs(cwbs);
		model.addAttribute("cwbs", cwbhuizongstr);

		if (isback.equals("1")) {
			printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));
		} else {
			printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()));
		}

		model.addAttribute("isback", isback);
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("islinghuo", islinghuo);

		model.addAttribute("template", printTemplateDAO.getPrintTemplate(printtemplateid));
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("branchlist", branchDAO.getAllBranches());
		model.addAttribute("userlist", userDAO.getAllUser());
		model.addAttribute("nextbranchid", Long.parseLong(nextbranchid));
		model.addAttribute("deliverid", deliverid);

		if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 1) {

			model.addAttribute("cwbList", cwbList);
			return "warehousegroup/outbillprinting_template";
		} else if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 2) {

			List<JSONObject> cwbJson = cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbList", cwbJson);
			model.addAttribute("cwbArr", cwbArr);
			return "warehousegroup/outbillhuizongprinting_template";
		}
		return null;
	}

	/**
	 * 出库交接单信息打印和查询 新 按照站点分开
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_defaultnew")
	public String outbillprinting_defaultnew(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "currentdeliverid", defaultValue = "0", required = true) long deliverid,
			@RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) String nextbranchid,
			@RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid) {
		model.addAttribute("flowtype", request.getParameter("type") == null ? -1 : request.getParameter("type"));
		String cwbs = "", cwbstr = "", cwbhuizongstr = "";
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
			cwbArr[i] = isprint[i];
			cwbhuizongstr += isprint[i] + "-H-";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}

		long flowordertype = Long.parseLong(request.getParameter("flowordertype"));

		List<CwbOrder> cwbList = cwbDao.getCwbByCwbsForPrint(cwbs, nextbranchid, getSessionUser().getBranchid(), flowordertype);

		StringBuffer sbf = new StringBuffer();

		// 拼成cwb-H-nextbranchid-HH-

		for (CwbOrder cwbOrder : cwbList) {

			sbf.append(cwbOrder.getCwb()).append("-H-").append(cwbOrder.getNextbranchid()).append("-HH-");

		}
		sbf.toString();
		String aimcwbs = "";
		if (sbf.length() > 0) {
			aimcwbs = sbf.substring(0, sbf.length() - 4);
		}

		model.addAttribute("cwbs", aimcwbs);

		if (isback.equals("1")) {
			printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));
		} else {
			printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()));
		}

		model.addAttribute("isback", isback);
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("islinghuo", islinghuo);

		model.addAttribute("template", printTemplateDAO.getPrintTemplate(printtemplateid));
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("userlist", userDAO.getAllUser());
		// model.addAttribute("nextbranchid", Long.parseLong(nextbranchid));
		model.addAttribute("deliverid", deliverid);

		String[] branchids = nextbranchid.split(",");
		Map<Long, List<CwbOrder>> map = new HashMap<Long, List<CwbOrder>>();
		Map<Long, CwbOrder> mapForCustomer = new HashMap<Long, CwbOrder>();
		/*
		 * for (String s : branchids) { if(Long.parseLong(s)>0){ List<CwbOrder>
		 * orderlist=new ArrayList<CwbOrder>(); for (CwbOrder cwb : cwbList) {
		 * mapForCustomer.put(cwb.getCustomerid(), cwb); if
		 * (cwb.getNextbranchid()==Long.parseLong(s)) { orderlist.add(cwb); } }
		 * map.put(Long.parseLong(s), orderlist); }
		 * 
		 * }
		 */

		for (CwbOrder cwb : cwbList) {
			mapForCustomer.put(cwb.getCustomerid(), cwb);
			if (!map.containsKey(cwb.getNextbranchid())) {
				List<CwbOrder> orders = new ArrayList<CwbOrder>();
				map.put(cwb.getNextbranchid(), orders);
			}
			map.get(cwb.getNextbranchid()).add(cwb);
		}

		model.addAttribute("mapBybranchid", map);
		if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 1) {
			return "warehousegroup/outbillprinting_templatenew";
		} else if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 2) {
			Map<Long, List<JSONObject>> hmap = new HashMap<Long, List<JSONObject>>();
			// 按照custome
			List<Customer> customerList = customerDAO.getAllCustomers();
			Set<Long> customers = mapForCustomer.keySet();
			for (Long branchid : map.keySet()) {
				List<CwbOrder> listForBranchid = map.get(branchid);
				// 得到每个站点的 订单详情
				if (listForBranchid.size() > 0) {
					List<JSONObject> customerJson = new ArrayList<JSONObject>();// 封装
					Map<Long, List<CwbOrder>> mapForCustomerTemp = new HashMap<Long, List<CwbOrder>>();
					// 按照供货商 - 订单 存入map
					for (Long customerid : customers) {
						List<CwbOrder> cwborderBycustomer = new ArrayList<CwbOrder>();
						for (CwbOrder co : listForBranchid) {
							if (customerid == co.getCustomerid()) {
								cwborderBycustomer.add(co);
							}
						}
						mapForCustomerTemp.put(customerid, cwborderBycustomer);
					}
					// 根据 供货商循环 得到每个供货商的汇总信息
					for (Long customerid : customers) {
						List<CwbOrder> tempList = mapForCustomerTemp.get(customerid);
						if (tempList.size() > 0) {
							JSONObject obj = new JSONObject();
							obj.put("customername", getCustomerName(customerid, customerList));
							obj.put("deliverid", deliverid);
							long cwbcount = 0;
							long sendcarnum = 0;
							long backcarnum = 0;
							BigDecimal caramount = BigDecimal.ZERO;
							BigDecimal receivablefee = BigDecimal.ZERO;
							BigDecimal paybackfee = BigDecimal.ZERO;
							for (CwbOrder cwbOrder : tempList) {
								cwbcount++;
								sendcarnum += cwbOrder.getSendcarnum();
								backcarnum += cwbOrder.getBackcarnum();
								caramount = caramount.add(cwbOrder.getCaramount());
								receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
								paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
							}
							obj.put("cwbcount", cwbcount);
							obj.put("sendcarnum", sendcarnum);
							obj.put("backcarnum", backcarnum);
							obj.put("caramount", caramount);
							obj.put("receivablefee", receivablefee);
							obj.put("paybackfee", paybackfee);
							customerJson.add(obj);// 添加到对应站点的 供货商汇总list
						}

					}
					hmap.put(branchid, customerJson);
				}
			}
			model.addAttribute("huizongmap", hmap);
			return "warehousegroup/outbillhuizongprinting_templatenew";
		}
		return null;
	}

	// 得到供货商的名字
	private String getCustomerName(Long customerid, List<Customer> customerList) {
		String name = "";
		if (customerList != null && customerList.size() > 0) {
			for (Customer customer : customerList) {
				if (customerid == customer.getCustomerid()) {
					name = customer.getCustomername();
					break;
				}
			}
		}
		return name;
	}

	/**
	 * 出库信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return jm's ideas 直接用cwbdetail
	 *         按照nextbranchid和当前操作flowordertype来查询到对应的订单信息 这是未打印
	 */
	@RequestMapping("/outlist/{page}")
	public String outlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<PrintView> printList = new ArrayList<PrintView>();
		List<Branch> bList = getNextPossibleBranches();
		model.addAttribute("branchlist", bList);
		String branchids = getStrings(branchid);
		if (isshow > 0) {
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTimeNew(getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime, "");
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}
		// LKN 这段代码没用
		// cwbs = cwbs.length()>0?cwbs.substring(0, cwbs.length()-1):"";
		// if (cwbs.length()>0) {
		// orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
		// }
		// List<Customer> customerList = customerDAO.getAllCustomers();
		// List<Branch> branchList = branchDAO.getAllEffectBranches();
		// List<PrintView> printList =
		// warehouseGroupDetailService.getChuKuView(orderlist, gdList,
		// customerList, branchList);
		model.addAttribute("printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue()));
		model.addAttribute("printList", printList);
		model.addAttribute("type", 1);
		model.addAttribute("time", "出库时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("branchids", branchids);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出库时间");
		return "warehousegroup/outdetaillist";
	}

	/**
	 * 库对库出库打印
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	@RequestMapping("/kdkoutlist/{page}")
	public String kdkoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> bList = getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();
		String branchids = getStrings(branchid);
		for (Branch b : bList) {// 去掉退货站
			if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchlist", lastList);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTimeNew(getSessionUser().getBranchid(), getStrings(branchid), FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), strtime,
					endtime, "");
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}
		model.addAttribute("printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue()));
		model.addAttribute("printList", printList);
		model.addAttribute("type", FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		model.addAttribute("time", "出库时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		model.addAttribute("branchids", branchids);
		return "warehousegroup/outdetaillist";
	}

	private String getStrings(String[] branchid) {
		String str = "-1,";
		for (String s : branchid) {
			str += s + ",";
		}

		return str.substring(0, str.length() - 1);
	}

	/**
	 * 中转出站交接单
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/branchzhongzhuanoutlist/{page}")
	public String branchzhongzhuanoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> bList = getNextPossibleBranches();
		List<Branch> removeList = new ArrayList<Branch>();
		for (Branch b : bList) {// 去掉退货站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				removeList.add(b);
			}
		}
		bList.removeAll(removeList);
		model.addAttribute("branchlist", bList);
		String branchids = getStrings(branchid);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTimeNew(getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime, "");
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);

		}
		model.addAttribute("printList", printList);
		model.addAttribute(
				"printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue()));
		model.addAttribute("type", 2);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出站时间");
		model.addAttribute("branchids", branchids);
		model.addAttribute("time", "出站时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		return "warehousegroup/outdetaillist";
	}

	/**
	 * 站点出站交接单
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/zhandianoutlist/{page}")
	public String zhandianoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime

	) {
		List<Branch> bList = getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();

		for (Branch b : bList) {
			if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchlist", lastList);
		String branchids = getStrings(branchid);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTimeNew(getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "", "");
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}

		model.addAttribute("printList", printList);

		model.addAttribute("printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()));
		model.addAttribute("type", 3);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出站时间");
		model.addAttribute("branchids", branchids);
		model.addAttribute("time", "出站时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		return "warehousegroup/outdetaillist";
	}

	/**
	 * 历史出库信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historyoutlist/{page}/{type}")
	public String historyoutlist(Model model, @PathVariable("page") long page, @PathVariable("type") long type, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<Branch> blist = getNextPossibleBranches();
		List<PrintTemplate> printtemplateList = printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + ","
				+ PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue());
		if (type == 2) {
			// 中转出站
			printtemplateList = printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue() + ","
					+ PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue());
			List<Branch> removeList = new ArrayList<Branch>();
			for (Branch b : blist) {// 去掉退货站
				if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					removeList.add(b);
				}
			}
			blist.removeAll(removeList);
		} else if (type == 3) {
			// 站点出站
			printtemplateList = printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue() + ","
					+ PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue());
			List<Branch> lastList = new ArrayList<Branch>();
			for (Branch b : blist) {
				if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					lastList.add(b);
				}
			}
			blist = lastList;
		} else if (type == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
			List<Branch> lastList = new ArrayList<Branch>();
			for (Branch b : blist) {
				if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
					lastList.add(b);
				}
			}
			blist = lastList;
		}
		model.addAttribute("outwarehousegroupList",
				outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, getSessionUser().getBranchid()));
		model.addAttribute("page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, getSessionUser().getBranchid()),
						page, Page.ONE_PAGE_NUMBER));
		List<Branch> branchs = branchDAO.getAllBranches();
		model.addAttribute("allbranch", branchs);
		model.addAttribute("branchList", blist);
		model.addAttribute("printtemplateList", printtemplateList);
		model.addAttribute("page", page);
		model.addAttribute("type", type);
		return "warehousegroup/historyoutlist";
	}

	/**
	 * 历史出库交接单信息打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_history/{outwarehousegroupid}")
	public String outbillprinting_history(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid) {

		List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		String cwbs = "";

		if (owg.getSign() == 1) {
			cwbs = owg.getCwbs();
			cwbList.addAll(cwbDao.getCwbByCwbs(cwbs));
		} else {
			List<GroupDetail> gdList = groupDetailDao.getAllGroupDetailByGroupid(outwarehousegroupid);

			for (GroupDetail gd : gdList) {
				if (cwbs.indexOf(gd.getCwb()) == -1) {
					cwbList.add(cwbDao.getCwbByCwb(gd.getCwb()));
					cwbs += "'" + gd.getCwb() + "',";
				}
			}
			if (cwbs.length() > 0) {
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
		}

		List<OrderFlow> flowList = orderFlowDAO.getCwbByFlowordertypeAndCwbs(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), cwbs);
		Map<String, String> mapForOperatorName = new HashMap<String, String>();
		for (OrderFlow of : flowList) {
			mapForOperatorName.put(of.getCwb(), userDAO.getUserByUserid(of.getUserid()).getRealname());
		}
		model.addAttribute("map", mapForOperatorName);

		model.addAttribute("template", printTemplateDAO.getPrintTemplate(printtemplateid));
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("userlist", userDAO.getAllUser());
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("isback", isback);
		model.addAttribute("islinghuo", islinghuo);
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("operatetype", outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getOperatetype());

		if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 1) {

			model.addAttribute("cwbList", cwbList);
			return "warehousegroup/outbillprinting_history";
		} else if (printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 2) {

			List<JSONObject> cwbJson = cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbList", cwbJson);
			return "warehousegroup/outbillhuizongprinting_history";
		}
		return null;
	}

	/**
	 * 燕赵出库信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/yzoutlist/{page}")
	public String yzoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<Branch> blist = branchDAO.getBanchByBranchidForCwbtobranchid(getSessionUser().getBranchid());
		if (branchid == 0) {
			String branchids = branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getCwbtobranchid().length() == 0 ? "0" : branchDAO.getBranchByBranchid(getSessionUser().getBranchid())
					.getCwbtobranchid();
			model.addAttribute("outwarehousegroupList",
					outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchids, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0));
			model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchids, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0), page,
					Page.ONE_PAGE_NUMBER));
		} else {
			model.addAttribute("outwarehousegroupList",
					outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, getSessionUser().getBranchid()));
			model.addAttribute(
					"page_obj",
					new Page(
							outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, getSessionUser().getBranchid()),
							page, Page.ONE_PAGE_NUMBER));
		}

		model.addAttribute("bList", branchDAO.getAllEffectBranches());

		model.addAttribute("branchList", blist);
		model.addAttribute("page", page);
		return "warehousegroup/yzoutlist";
	}

	/**
	 * 燕赵出库交接单信息打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_yz/{outwarehousegroupid}")
	public String outbillprinting_yz(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		/*
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * Date date = new Date(); String datetime = df.format(date);
		 */
		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		/*
		 * if(owg.getPrinttime().equals("")){
		 * outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		 * outwarehousegroupDao
		 * .saveOutWarehouseById(OutWarehouseGroupEnum.FengBao
		 * .getValue(),outwarehousegroupid); }
		 */

		List<JSONObject> cwbJson = cwbDao.getSomeDetailForYZPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/outbillprinting_yz";
	}

	/**
	 * 分站到货信息查询
	 * 
	 * @param model
	 * @param page
	 * @param userid
	 * @return
	 */
	@RequestMapping("/inboxlist/{page}")
	public String inboxPrint(Model model, @PathVariable("page") long page, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		List<OutWarehouseGroup> owgAllList = null;
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, getSessionUser()
				.getBranchid());
		// }

		model.addAttribute("owgAllList", owgAllList);
		model.addAttribute(
				"page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/inboxlist";
	}

	/**
	 * 分站到货交接单信息打印和查询
	 * 
	 * @param outwarehousegroupid
	 * @param model
	 * @return
	 */
	@RequestMapping("/inboxsearchAndPrint/{outwarehousegroupid}")
	public String searchDetail(@PathVariable("outwarehousegroupid") long outwarehousegroupid, Model model) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}

		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("cwbAllDetail", cwbDao.getCwbByGroupid(outwarehousegroupid));
		return "warehousegroup/inboxbillprinting_default";
	}

	/**
	 * 中转出站查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changelist/{page}")
	public String changelist(Model model, @PathVariable("page") long page, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		long branchid = getSessionUser().getBranchid();
		model.addAttribute("outwarehousegroupList", outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue(), 0, getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue(), 0, getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changelist";
	}

	/**
	 * 中转出站交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changebillprinting_default/{outwarehousegroupid}")
	public String changebillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		/*
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * Date date = new Date(); String datetime = df.format(date);
		 */

		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		/*
		 * if(owg.getPrinttime().equals("")){
		 * outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		 * outwarehousegroupDao
		 * .saveOutWarehouseById(OutWarehouseGroupEnum.FengBao
		 * .getValue(),outwarehousegroupid); }
		 */

		List<JSONObject> cwbJson = cwbDao.getSomeDetailForPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());

		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/changebillprinting_txd";
	}

	/**
	 * 退货出站信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/returnlist/{page}")
	public String returnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> bList = getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();
		List<PrintView> printList = new ArrayList<PrintView>();
		for (Branch b : bList) {
			// 退货站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchlist", lastList);

		if (isshow > 0) {
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTime(getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), begindate, enddate);
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}

		model.addAttribute("printList", printList);

		model.addAttribute("printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiHuoChuZhanAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()));
		return "warehousegroup/returnlist";
	}

	/**
	 * 退货出站交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/returnbillprinting_default")
	public String returnbillprinting_default(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "", cwbstr = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
		}

		List<CwbOrder> cwbList = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));

		printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));

		SystemInstall si = systemInstallDAO.getSystemInstallByName("chukujiaojiedan");
		model.addAttribute("template", printTemplateDAO.getPrintTemplate(Long.parseLong(si.getValue())));
		model.addAttribute("cwbList", cwbList);
		model.addAttribute("isback", "1");
		model.addAttribute("localbranchname", branchDAO.getBranchByBranchid(cwbList.get(0).getNextbranchid()).getBranchname());
		model.addAttribute("customerlist", customerDAO.getAllCustomers());

		return "warehousegroup/outbillprinting_template";
	}

	/**
	 * 历史退货出站信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historyreturnlist/{page}")
	public String historyreturnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<Branch> blist = getNextPossibleBranches();
		model.addAttribute("outwarehousegroupList", outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));

		model.addAttribute("branchList", blist);
		model.addAttribute("printtemplateList",
				printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiHuoChuZhanAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()));
		model.addAttribute("page", page);
		return "warehousegroup/historyreturnlist";
	}

	/**
	 * 中转站入库查询
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changeinlist/{page}")
	public String changeinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
				OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue(), 0, getSessionUser().getBranchid());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		model.addAttribute(
				"page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
						OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue(), 0, getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changeinlist";
	}

	/**
	 * 中转站入库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changeinlistprint/{outwarehousegroupid}")
	public String changeinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/changeinbillprinting_default";
	}

	/**
	 * 退货站入库查询
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/backinlist/{page}")
	public String backinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
				OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue(), 0, getSessionUser().getBranchid());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		model.addAttribute(
				"page_obj",
				new Page(outwarehousegroupDao.getOutWarehouseGroupCount(getSessionUser().getBranchid(), beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue(),
						0, getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/backinlist";
	}

	/**
	 * 退货站入库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/backinlistprint/{outwarehousegroupid}")
	public String backinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/backinbillprinting_default";
	}

	@RequestMapping("/exportforoutwarehouse")
	public void exportforoutwarehouse(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "isprint", required = false, defaultValue = "") String[] isprint) {
		String mouldfieldids2 = request.getParameter("exportmould2");
		String cwbs = "";
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbArr[i] = isprint[i];
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		final String exportcwbs = cwbs;
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final String sql = cwbDao.getSQLExportKeFu(exportcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getRemarkByCwbs(exportcwbs);
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDao.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
							recordbatch.add(mapRow);
							count++;
							if (count % 100 == 0) {
								writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
										complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = recordbatch.get(i).get("cwb").toString();
									writeSingle(recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), count - size + i, cwbspayupMsp, complaintMap);
								}
								recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							return cwbspayupidMap;
						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/*
	 * public Map<String,Map<String,String>>
	 * getOrderFlowByCredateForDetailAndExportAllTime(List<String>
	 * cwbs,List<Branch> branchlist){
	 * 
	 * Map<String,Map<String,String>> map=new HashMap<String,
	 * Map<String,String>>(); try{ List<OrderFlow> ofList =
	 * orderFlowDAO.getOrderFlowByCwbs(cwbs); for(OrderFlow of : ofList){
	 * if(of.getFlowordertype()==
	 * FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ getCwbRow(map,
	 * of).put("customerbrackhouseremark", of.getComment()); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.RuKu.getValue()){
	 * getCwbRow(map, of).put("Instoreroomtime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
	 * if(getsitetype
	 * (branchlist,of.getBranchid())==BranchEnum.ZhongZhuan.getValue()){
	 * getCwbRow(map, of).put("zhongzhuanzhanIntime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }else
	 * if(of.getFlowordertype()==
	 * FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue
	 * ()||of.getFlowordertype()==
	 * FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){
	 * getCwbRow(map, of).put("InSitetime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
	 * getCwbRow(map, of).put("PickGoodstime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){
	 * getCwbRow(map, of).put("Outstoreroomtime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
	 * if(getsitetype
	 * (branchlist,of.getBranchid())==BranchEnum.ZhongZhuan.getValue()){
	 * getCwbRow(map, of).put("zhongzhuanzhanOuttime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.YiShenHe.getValue()){
	 * getCwbRow(map, of).put("Goclasstime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.YiFanKui.getValue()){
	 * getCwbRow(map, of).put("Gobacktime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if
	 * (of.getFlowordertype()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue
	 * ()) { getCwbRow(map, of).put("tuigonghuoshangchukutime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }
	 * if(getCwbRow(map,
	 * of).get("Newchangetime")==null||of.getCredate().getTime()> new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getCwbRow(map,
	 * of).get("Newchangetime")).getTime()){ getCwbRow(map,
	 * of).put("Newchangetime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }
	 * }catch (Exception e) {} return map; }
	 */
	private Map<String, String> getCwbRow(Map<String, Map<String, String>> map, OrderFlow of) {
		if (map.get(of.getCwb()) == null) {
			map.put(of.getCwb(), new HashMap<String, String>());
		}
		Map<String, String> map2 = map.get(of.getCwb());
		return map2;
	}

	private long getsitetype(List<Branch> branchlist, long branchid) {
		long sitetype = 0;
		if (branchlist.size() > 0) {
			for (Branch b : branchlist) {
				if (branchid == b.getBranchid()) {
					sitetype = b.getSitetype();
					break;
				}
			}
		}
		return sitetype;
	}

	/**
	 * 出库信息查询(按包)
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return jm's ideas 直接用cwbdetail
	 *         按照nextbranchid和当前操作flowordertype来查询到对应的订单信息 这是未打印
	 */
	@RequestMapping("/outbalelist/{page}")
	public String outbalelist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno) {
		List<PrintView> printList = new ArrayList<PrintView>();
		List<Branch> bList = getNextPossibleBranches();
		model.addAttribute("branchlist", bList);
		String branchids = getStrings(branchid);
		if (isshow > 0) {
			// 过滤订单打印表
			List<GroupDetail> gdList = groupDetailDao.getCwbForChuKuPrintTimeNew(getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "", baleno);
			// 过滤包号表
			List<Bale> baleList = baleDAO.getBaleByBalePrint(getSessionUser().getBranchid(), baleno, strtime, endtime);

			List<Branch> branchList = branchDAO.getAllBranches();
			printList = warehouseGroupDetailService.getChuKuBaleView(gdList, baleList, branchList);
		}
		model.addAttribute("printList", printList);
		model.addAttribute("type", 1);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("branchids", branchids);
		return "warehousegroup/outbalelist";
	}

	/**
	 * 出库交接单打印(按包号)
	 * 
	 * @param model
	 * @param request
	 * @param isprint
	 * @param nextbranchid
	 * @return
	 */
	@RequestMapping("/outbalelist_print")
	public String outbalelist_print(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		List<Branch> branchList = branchDAO.getAllBranches();
		List<Customer> cusomerList = customerDAO.getAllCustomers();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String balenos = "";
		// 循环包号
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			balenos += "" + isprint[i] + ",";
			Map<String, Object> map = new HashMap<String, Object>();
			// 根据包号查找订单打印表数据
			List<GroupDetail> groupDetailList = groupDetailDao.getGroupDetailListByBale(isprint[i]);
			if (groupDetailList != null && !groupDetailList.isEmpty()) {
				// 获得包号、发货地、目的地
				map.put("baleid", groupDetailList.get(0).getBaleid());
				map.put("baleno", groupDetailList.get(0).getBaleno());
				map.put("branchname", warehouseGroupDetailService.getQueryBranchName(branchList, groupDetailList.get(0).getBranchid()));
				map.put("nextbranchname", warehouseGroupDetailService.getQueryBranchName(branchList, groupDetailList.get(0).getNextbranchid()));
				map.put("username", this.getSessionUser().getRealname());

				String cwbs = "";
				for (int j = 0; j < groupDetailList.size(); j++) {
					cwbs += "'" + groupDetailList.get(j).getCwb() + "',";
				}
				// 获取订单数量、代收总金额
				List<Map<String, Object>> cwbList = cwbDao.getCwbByPrintCwbs(cwbs.substring(0, cwbs.lastIndexOf(",")));
				List<Map<String, Object>> cwbListView = warehouseGroupDetailService.getChuKuBaleCwbView(cwbList, cusomerList);
				map.put("cwbListView", cwbListView);
			}
			list.add(map);
		}

		// 获得名称：XXX出仓交接单
		SystemInstall sy = systemInstallDAO.getSystemInstallByName("chukujiaojiedananbao");
		model.addAttribute("templateName", sy.getValue() == null ? "" : sy.getValue());

		model.addAttribute("list", list);
		model.addAttribute("balenos", balenos.substring(0, balenos.lastIndexOf(",")));
		return "warehousegroup/outbalelist_print";
	}

	/**
	 * 更新包号状态为已打印
	 * 
	 * @param model
	 * @param request
	 * @param balenos
	 * @return
	 */
	@RequestMapping("/outbalelist_update")
	public @ResponseBody String outbalelist_update(Model model, HttpServletRequest request, @RequestParam(value = "balenos", defaultValue = "", required = true) String balenos) {
		String[] balenosStr = balenos.split(",");
		try {
			for (int i = 0; i < balenosStr.length; i++) {
				if (balenosStr[i].trim().length() == 0) {
					continue;
				}
				groupDetailDao.updateGroupDetailListByBale(balenosStr[i]);
			}
			return "{\"errorCode\":0,\"error\":\"成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 出库交接单打印(按包) 历史打印记录
	 * 
	 * @param model
	 * @param page
	 * @param type
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/outbalelist_history/{page}")
	public String outbalelist_history(Model model, @PathVariable("page") long page, @RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		try {
			List<Branch> branchList = branchDAO.getAllBranches();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long startTime = 0, endTime = 0;
			if (!"".equals(starttime)) {
				startTime = ft.parse(starttime).getTime();
			}
			if (!"".equals(endtime)) {
				endTime = ft.parse(endtime).getTime();
			}
			model.addAttribute("list", groupDetailDao.getGroupDetailhistoryByBale(page, this.getSessionUser().getBranchid(), startTime, endTime, nextbranchid));
			model.addAttribute("page_obj", new Page(groupDetailDao.getGroupDetailhistoryByBaleCount(this.getSessionUser().getBranchid(), startTime, endTime, nextbranchid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("branchList", branchList);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "warehousegroup/outbalelist_history";
	}

}
