package cn.explink.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.AddressStartDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbErrorDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExcelImportEditDao;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ImportFieldDAO;
import cn.explink.dao.SysConfigDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountArea;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelImportEdit;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbTranslator;
import cn.explink.service.DataImportService;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ResultCollector;
import cn.explink.service.ResultCollectorManager;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.Page;
import cn.explink.util.ServiceUtil;
import cn.explink.vo.Information;

@Controller
@RequestMapping("/dataimport")
public class DataImportController {

	@Autowired
	Excel2007Extractor excel2007Extractor;

	@Autowired
	Excel2003Extractor excel2003Extractor;

	@Autowired
	DataImportService dataImportService;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	AddressStartDAO addressStartDAO;

	@Autowired
	ResultCollectorManager resultCollectorManager;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	CwbErrorDAO cwbErrorDAO;

	@Autowired
	SysConfigDAO sysConfigDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	AddressMatchService addressMatchService;

	@Autowired
	EmailDateDAO emaildateDAO;

	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	AccountAreaDAO accountAreaDAO;

	@Autowired
	ImportFieldDAO importFieldDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	ExportmouldDAO exportmouldDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	TransCwbDao transCwbDao;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;

	private Logger logger = LoggerFactory.getLogger(DataImportController.class);

	@Autowired
	ExcelImportEditDao excelImportEditDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/excelimportPage")
	public String importExcelPage(Model model, HttpServletRequest request) {
		List<Branch> branches = new ArrayList<Branch>();
		if (this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype() == BranchEnum.KuFang.getValue()) {
			branches = this.branchDAO.getBranchByMyKuFang(this.getSessionUser().getBranchid());
		} else {
			branches = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		}

		model.addAttribute("branches", branches);
		model.addAttribute("customers", this.customerDAO.getAllCustomersByExistRules());
		model.addAttribute("addressStart", this.addressStartDAO.isAddressStart());
		return "dataimport/excelimport";
	}

	@RequestMapping("/reexcelimportPage")
	public String reImportExcelPage(Model model, HttpServletRequest request) {

		String emaildateid = (request.getParameter("emaildateid") == null) || request.getParameter("emaildateid").equals("null") ? "0" : request.getParameter("emaildateid");

		List<Branch> branches = new ArrayList<Branch>();
		if (this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype() == BranchEnum.KuFang.getValue()) {
			branches = this.branchDAO.getBranchByMyKuFang(this.getSessionUser().getBranchid());
		} else {
			branches = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		}

		model.addAttribute("branches", branches);
		model.addAttribute("customers", this.customerDAO.getAllCustomersByExistRules());
		model.addAttribute("addressStart", this.addressStartDAO.isAddressStart());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 5);
		// model.addAttribute("emaildateList",
		// emaildateDAO.getEmailDateByDateByUserid(df.format(new
		// Date(day7*1000)), df.format(new
		// Date()),getSessionUser().getUserid()));
		model.addAttribute("emaildateList", this.emaildateDAO.getEmailDateByDate(df.format(new Date(day7 * 1000)), this.getSessionUser().getBranchid()));

		EmailDate ed = this.emaildateDAO.getEmailDateById(Long.parseLong(emaildateid));
		if (ed != null) {
			int toSuccessPage = request.getParameter("toSuccessPage") == null ? 1 : Integer.parseInt(request.getParameter("toSuccessPage"));
			model.addAttribute("Successpage_obj", new Page(this.cwbDAO.getcwborderCount(ed.getEmaildateid()), toSuccessPage, Page.ONE_PAGE_NUMBER));
			model.addAttribute("SuccessOrder", this.cwbDAO.getcwbOrderByPage(toSuccessPage, ed.getEmaildateid()));
			model.addAttribute("Successpage", toSuccessPage);

			int toFailuresPage = request.getParameter("toFailuresPage") == null ? 1 : Integer.parseInt(request.getParameter("toFailuresPage"));
			model.addAttribute("FailuresOrder", this.cwbErrorDAO.getcwbOrderErrorByPage(toFailuresPage, ed.getEmaildateid()));
			model.addAttribute("Failurespage_obj", new Page(this.cwbErrorDAO.getCwbOrderErrorCount(ed.getEmaildateid()), toFailuresPage, Page.ONE_PAGE_NUMBER));
			model.addAttribute("Failurespage", toFailuresPage);
		}
		return "dataimport/reexcelimport";
	}

	@RequestMapping("/batchedit")
	public String batchedit(Model model, @RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildateid,
			@RequestParam(value = "customerwarehouseid", defaultValue = "0", required = false) long customerwarehouseid,
			@RequestParam(value = "serviceareaid", defaultValue = "0", required = false) long serviceareaid,
			@RequestParam(value = "editemaildate", defaultValue = "", required = false) String editemaildate) {

		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 5);
		// model.addAttribute("emaildateList",
		// emaildateDAO.getEmailDateByDateByUserid(df.format(new
		// Date(day7*1000)), df.format(new
		// Date()),getSessionUser().getUserid()));
		model.addAttribute("emaildateList", this.emaildateDAO.getEmailDateByDate(this.df.format(new Date(day7 * 1000)), this.getSessionUser().getBranchid()));
		return "dataimport/batchedit";
	}

	@RequestMapping("/batcheditselect")
	public @ResponseBody List<CustomWareHouse> batcheditselect(@RequestParam("emaildate") long emaildateid) {
		EmailDate emaildate = this.emaildateDAO.getEmailDateById(emaildateid);
		return this.customWareHouseDAO.getCustomWareHouseByCustomerid(emaildate.getCustomerid());
	}

	@RequestMapping("/batcheditselect1")
	public @ResponseBody List<AccountArea> batcheditselect1(@RequestParam("emaildate") long emaildateid) {
		EmailDate emaildate = this.emaildateDAO.getEmailDateById(emaildateid);
		return this.accountAreaDAO.getAccountAreaByCustomerid(emaildate.getCustomerid());
	}

	@RequestMapping("/datalose")
	public String datalose(Model model, @RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildateid,
			@RequestParam(value = "isEdit", defaultValue = "no", required = false) String isEdit) {
		if ("yes".equals(isEdit) && !ServiceUtil.isNowImport(emaildateid)) {
			List<String> cwblist = this.cwbDAO.getCwbsListByEmailDateId(emaildateid);

			this.transCwbDao.deleteTranscwbByCwbs(this.dataImportService.getStrings(cwblist));
			this.cwbDAO.dataLose(emaildateid);
			this.emaildateDAO.loseEmailDateById(emaildateid);
			model.addAttribute("ReturnMessage", "操作成功");
			try {
				this.dataImportService.datalose(emaildateid);
				this.logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid :成功");
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid : " + emaildateid);
			}
		} else if ("yes".equals(isEdit) && ServiceUtil.isNowImport(emaildateid)) {
			model.addAttribute("ReturnMessage", "对应的批次仍然正在导入，请稍后再试。");
		}
		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 5);
		// model.addAttribute("emaildateList",
		// emaildateDAO.getEmailDateByDateByUserid(df.format(new
		// Date(day7*1000)), df.format(new
		// Date()),getSessionUser().getUserid()));
		model.addAttribute("emaildateList", this.emaildateDAO.getEmailDateByDate(this.df.format(new Date(day7 * 1000)), this.getSessionUser().getBranchid()));
		return "dataimport/datalose";
	}

	@RequestMapping("/import")
	public @ResponseBody CwbOrder importCwborder(Model model, @RequestParam(value = "cwborder") String cwborderjson) throws Exception {
		final ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		CwbOrderDTO dto = new ObjectMapper().reader(CwbOrderDTO.class).readValue(cwborderjson);
		EmailDate ed = this.dataImportService.getOrCreateEmailDate(dto.getCustomerid(), 0, 0);
		this.dataImportService.importSingleData(userDetail.getUser(), ed, false, dto);

		return null;
	}

	@RequestMapping("/excelimport")
	public String importExcel(Model model, @RequestParam("customerid") final long customerid, @RequestParam("Filedata") final MultipartFile file,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") final long branchid,
			@RequestParam(value = "warehouseid", required = false, defaultValue = "0") final long warehouseid, @RequestParam(value = "areaid", required = false, defaultValue = "0") final long areaid,
			@RequestParam(value = "emaildate", required = false) String ParamEmaildate, @RequestParam(value = "emaildateid", required = false, defaultValue = "0") final long emaildateid,
			@RequestParam(value = "isReImport", required = false, defaultValue = "no") final String isReImport, final HttpServletRequest request, HttpServletResponse response) throws Exception {
		final ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		final ResultCollector errorCollector = this.resultCollectorManager.createNewResultCollector();
		response.getWriter().write(errorCollector.getId());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.logger.info("excelimport : errorCollectorid : " + errorCollector.getId());
		this.logger.info("excelimport : ParamEmaildate : " + ParamEmaildate);
		final String emaildate = df.format(ParamEmaildate == null ? new Date() : df.parse(ParamEmaildate));
		response.getWriter().write("," + emaildate);
		response.getWriter().flush();
		response.flushBuffer();
		response.getWriter().close();
		final ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		final InputStream inputStream = file.getInputStream();
		final SecurityContext scontext = this.securityContextHolderStrategy.getContext();
		if (excelExtractor != null) {
			ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
			newSingleThreadExecutor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						DataImportController.this.securityContextHolderStrategy.setContext(scontext);
						DataImportController.this.processFile(excelExtractor, customerid, branchid, warehouseid, areaid, emaildateid, isReImport, userDetail, errorCollector, emaildate, inputStream);
					} catch (Exception e) {
						errorCollector.addError("处理出错", e.getMessage());
						e.printStackTrace();
					} finally {
						errorCollector.setFinished(true);

					}
				}
			});
		} else {
			errorCollector.addError("0", "不可识别的文件");
		}

		return null;
	}

	private void processFile(ExcelExtractor excelExtractor, long customerid, long branchid, long warehouseid, long areaid, long emaildateid, String isReImport, ExplinkUserDetail userDetail,
			ResultCollector errorCollector, String emaildate, InputStream inputStream) {

		// 重新导入需要清空对应的emaildate的错误订单记录
		if (emaildateid > 0) {
			this.jdbcTemplate.update("delete from express_ops_cwb_error where  emaildateid=? ", emaildateid);
		}
		EmailDate ed = this.emaildateDAO.getEmailDateById(emaildateid);
		Customer customer = this.customerDAO.getCustomerById(customerid);

		if (ed == null) {
			ed = new EmailDate();
			ed.setEmaildatetime(emaildate);
		}
		if (emaildateid == 0) {
			emaildateid = this.emaildateDAO.getCreateEmailDate(emaildate, userDetail.getUser().getUserid(), areaid, warehouseid, customerid, branchid, errorCollector.getSuccessSavcNum());
			ed.setEmaildateid(emaildateid);
		} else {
			this.emaildateDAO.editEdit(emaildateid, areaid, warehouseid, customerid);
			if (customer.getIsAutoProductcwb() == 1) {
				// 重复导入数据，供货商如果自动生成订单号的，则先失效该批次对应的订单，更改该批次下的成功导入数据单数，并发送JMS
				this.cwbDAO.dataLose(emaildateid);
				this.emaildateDAO.editEditEmaildateForCwbcount(errorCollector.getSuccessSavcNum(), emaildateid);
				try {
					this.dataImportService.datalose(emaildateid);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid : " + emaildateid);
				}
			}
		}
		ServiceUtil.startNowImport(emaildateid);

		ed.setAreaid(areaid);
		ed.setWarehouseid(warehouseid);
		ed.setCustomerid(customerid);
		ed.setBranchid(branchid);

		errorCollector.setEmaildateid(ed.getEmaildateid());
		excelExtractor.extract(inputStream, customerid, errorCollector, branchid, ed, userDetail, isReImport.equals("yes"));
		ServiceUtil.endNowImport(emaildateid);
	}

	@RequestMapping("/addressmatch")
	public void addressBatchMatch(@RequestParam("emaildateid") long emaildateid) {
		List<CwbOrder> cwbOrders = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
		for (CwbOrder cwbOrder : cwbOrders) {
			this.addressMatchService.matchAddress(this.getSessionUser().getUserid(), cwbOrder.getCwb());
		}
	}

	/*
	 * @RequestMapping("/stop") public @ResponseBody String
	 * stopImport(HttpServletRequest request){
	 * request.getSession().setAttribute("importStop", "stop"); return null; }
	 */

	@RequestMapping("/editBranch")
	public String editBranch(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildate, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType", defaultValue = "-1", required = false) Integer addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow, // 是否显示,
			@RequestParam(value = "zlpop", defaultValue = "2", required = false) long zlpop // 是否显示

	) {
		/*
		 * //如果系统设置 String
		 * showCustomer=systemInstallDAO.getSystemInstall("ifUseeditbranch"
		 * ).getValue(); if(showCustomer.equals("no")){ return
		 * editBrancheach(model,request,response); }
		 */
		model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
		model.addAttribute("customers", this.customerDAO.getAllCustomers());
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		Page pageobj = new Page();
		long NotSuccess = 0;
		long SuccessAddress = 0;
		long SuccessEdit = 0;
		long SuccessNew = 0;
		if (isshow != 0) {
			cwborderList = this.cwbDAO.getcwbOrderByPageIsMyWarehouse(page, customerid, cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), onePageNumber, branchid);
			pageobj = new Page(this.cwbDAO.getcwborderCountIsMyWarehouse(customerid, cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), branchid), page, onePageNumber);
			NotSuccess = this.cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.WeiPiPei);
			SuccessAddress = this.cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
			SuccessEdit = this.cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.XiuGai);
			SuccessNew = this.cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.RenGong);
		}
		model.addAttribute("Order", cwborderList);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("NotSuccess", NotSuccess);
		model.addAttribute("SuccessAddress", SuccessAddress);
		model.addAttribute("SuccessEdit", SuccessEdit);
		model.addAttribute("SuccessNew", SuccessNew);
		model.addAttribute("AllAddress", SuccessAddress + NotSuccess + SuccessEdit + SuccessNew);
		// 处理批次
		List<EmailDate> eList = this.emaildateDAO.getEmailDateByCustomerid(customerid);
		List<AccountArea> aList = this.accountAreaDAO.getAllAccountArea();
		Map<Long, AccountArea> aMap = new HashMap<Long, AccountArea>();
		for (AccountArea a : aList) {
			aMap.put(a.getAreaid(), a);
		}
		Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
		List<CustomWareHouse> cwhList = this.customWareHouseDAO.getAllCustomWareHouse();
		Map<Long, CustomWareHouse> cwhMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse cwh : cwhList) {
			cwhMap.put(cwh.getWarehouseid(), cwh);
		}
		for (EmailDate e : eList) {
			e.setAreaname(aMap.get(e.getAreaid()) == null ? "" : aMap.get(e.getAreaid()).getAreaname());
			e.setCustomername(cMap.get(e.getCustomerid()) == null ? "" : cMap.get(e.getCustomerid()).getCustomername());
			e.setWarehousename(cwhMap.get(e.getWarehouseid()) == null ? "" : cwhMap.get(e.getWarehouseid()).getCustomerwarehouse());
		}
		// 处理批次完毕
		model.addAttribute("emaildateList", eList);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		return "dataimport/editBranch";
	}

	/**
	 * 2013-8-5,鞠sir给的需求，点击（数据导入 > 数据导入 > 导入数据-修改匹配站，点击 地址库匹配0 修改匹配0 人工匹配0 未匹配0)
	 * 这些参数时，重新发送匹配地址库的jms
	 *
	 * @param request
	 * @param cwb
	 * @param emaildate
	 * @param customerid
	 * @param addressCodeEditType
	 * @param onePageNumber
	 * @param isshow
	 */
	@RequestMapping("/resendAddressmatch")
	public @ResponseBody String resendAddressmatch(HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildate, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType", defaultValue = "-1", required = false) int addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "onePageNumber", defaultValue = "50000", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow) {
		try {
			if (emaildate == 0) {
				return "{\"errorCode\":1,\"error\":\"请选择批次!\"}";
			}

			if (isshow != 0) {
				List<CwbOrder> cwborderList = this.cwbDAO.getcwbOrderByPageIsMyWarehouse(0, customerid, cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), onePageNumber,
						branchid);
				try {
					this.dataImportService.resendAddressmatch(this.getSessionUser(), cwborderList);
					return "{\"errorCode\":0,\"error\":\"匹配成功!\"}";
				} catch (CwbException ce) {
					return "{\"errorCode\":" + ce.getError().getValue() + ",\"error\":\"匹配失败，" + ce.getMessage() + "!\"}";
				}

			}
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"匹配失败!\"}";
		}
		return null;
	}

	@RequestMapping("/editBatchBranch")
	public String editBatchBranch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "excelbranch", required = false, defaultValue = "") String excelbranch, @RequestParam(value = "branchcode", required = false, defaultValue = "") String branchcode,
			@RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示
	) throws Exception {
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		Page pageobj = new Page();
		long count = 0;
		if (isshow != 0) {
			List<Branch> lb = new ArrayList<Branch>();
			List<Branch> branchnamelist = this.branchDAO.getBranchByBranchnameCheck(branchcode.length() > 0 ? branchcode : excelbranch);
			if (branchnamelist.size() > 0) {
				lb = branchnamelist;
			} else {
				lb = this.branchDAO.getBranchByBranchcode(branchcode.length() > 0 ? branchcode : excelbranch);
			}
			if (lb.size() > 0) {
				String[] cwbstr = cwbs.trim().split("\r\n");
				List<String> cwbStrList = new ArrayList<String>();
				for (int i = 0; i < cwbstr.length; i++) {
					if (cwbstr[i].trim().length() == 0) {
						continue;
					}
					if (!cwbStrList.contains(cwbstr[i])) {
						cwbStrList.add(cwbstr[i]);
					}
				}
				StringBuffer cwbBuffer = new StringBuffer();
				String cwbstrs = "";
				for (int i = 0; i < cwbStrList.size(); i++) {
					cwbBuffer = cwbBuffer.append("'" + cwbStrList.get(i) + "',");
				}
				if (cwbBuffer.length() > 0) {
					cwbstrs = cwbBuffer.toString().substring(0, cwbBuffer.length() - 1);
				}
				List<CwbOrder> oList = this.cwbDAO.getCwbByCwbs(cwbstrs);
				if ((oList != null) && (oList.size() > 0)) {
					for (CwbOrder cwbOrder : oList) {
						CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
						if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue())
								|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
																																// 都将匹配状态变更为修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
						} else if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
								|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																																// 或者是人工匹配的
																																// 都将匹配状态变更为人工修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
						}
						try {
							this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), cwbOrder, lb.get(0), addressCodeEditType);
							count += 1;
						} catch (CwbException ce) {
							model.addAttribute("error", "匹配失败，" + ce.getMessage() + "!");
						}
					}
				}
				cwborderList = this.cwbDAO.getCwbByCwbs(cwbstrs);
				pageobj = new Page(count, page, onePageNumber);
			} else {
				model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
				model.addAttribute("Order", cwborderList);
				model.addAttribute("page_obj", pageobj);
				model.addAttribute("page", page);
				model.addAttribute("msg", "1");
				model.addAttribute("count", count);
				return "dataimport/editBatchBranch";
			}

		}
		model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
		model.addAttribute("Order", cwborderList);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("count", count);
		return "dataimport/editBatchBranch";
	}

	@RequestMapping("/editexcel/{cwb}")
	public @ResponseBody String editexcel(@PathVariable("cwb") String cwb, @RequestParam(value = "excelbranch", required = false) String excelbranch, HttpServletRequest request) throws Exception {
		List<Branch> lb = new ArrayList<Branch>();
		List<Branch> branchnamelist = this.branchDAO.getBranchByBranchnameCheck(excelbranch);
		if (branchnamelist.size() > 0) {
			lb = branchnamelist;
		} else {
			lb = this.branchDAO.getBranchByBranchcode(excelbranch);
		}
		if (lb.size() == 0) {
			return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}";
		}
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
		if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
			// 都将匹配状态变更为修改
			addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
		} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																																																	// 或者是人工匹配的
																																																	// 都将匹配状态变更为人工修改
			addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
		}
		try {
			this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), co, lb.get(0), addressCodeEditType);
			return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + cwb + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":" + ce.getError().getValue() + ",\"error\":\"" + ce.getMessage() + "\",\"cwb\":\"" + cwb + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
		}

	}

	/**
	 * 生成运单号
	 */
	@RequestMapping("/reproducttranscwb")
	public String reproducttranscwb(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildateid, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示
	) {
		String msg = "";
		List<Customer> customerlist = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customerlist);

		// 处理批次
		List<EmailDate> eList = this.emaildateDAO.getEmailDateByCustomerid(customerid);
		List<AccountArea> aList = this.accountAreaDAO.getAllAccountArea();
		Map<Long, AccountArea> aMap = new HashMap<Long, AccountArea>();
		for (AccountArea a : aList) {
			aMap.put(a.getAreaid(), a);
		}
		Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
		List<CustomWareHouse> cwhList = this.customWareHouseDAO.getAllCustomWareHouse();
		Map<Long, CustomWareHouse> cwhMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse cwh : cwhList) {
			cwhMap.put(cwh.getWarehouseid(), cwh);
		}
		for (EmailDate e : eList) {
			e.setAreaname(aMap.get(e.getAreaid()) == null ? "" : aMap.get(e.getAreaid()).getAreaname());
			e.setCustomername(cMap.get(e.getCustomerid()) == null ? "" : cMap.get(e.getCustomerid()).getCustomername());
			e.setWarehousename(cwhMap.get(e.getWarehouseid()) == null ? "" : cwhMap.get(e.getWarehouseid()).getCustomerwarehouse());
		}
		// 处理批次完毕
		model.addAttribute("emaildateList", eList);

		if (isshow != 0) {
			if (emaildateid > 0) {
				EmailDate ed = this.emaildateDAO.getEmailDateById(emaildateid);
				if (ed.getState() == 1) {
					msg = "已到货批次不允许重新生成运单号";
					model.addAttribute("msg", msg);
					return "dataimport/reproducttranscwb";
				}
			}
			List<CwbOrder> list = this.dataImportService.getListByCwbs(cwbs, emaildateid);
			try {
				this.dataImportService.reProduceTranscwb(list, customerlist);
				msg = "操作成功";
			} catch (Exception e) {
				// e.printStackTrace();
				msg = "生成失败" + e.getMessage();
			}
		}
		model.addAttribute("msg", msg);

		return "dataimport/reproducttranscwb";
	}

	@RequestMapping("/getcwbforeditexcel/{cwb}")
	public String getcwbforeditexcel(@PathVariable("cwb") String cwb, Model model) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		model.addAttribute("co", co);
		return "dataimport/edit";
	}

	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwb1", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "emaildate1", defaultValue = "0", required = false) long emaildate, @RequestParam(value = "customerid1", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType1", defaultValue = "0", required = false) int addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid1", defaultValue = "0", required = false) long branchid) {
		this.dataImportService.ExportExcelMethod(response, request, cwb, emaildate, customerid, addressCodeEditType, branchid);
	}

	@RequestMapping("/exportExcleInfo")
	// 新增修改匹配站导出
	public void exportExcleInfo(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "emaildate1", defaultValue = "0", required = false) long emaildate,
			@RequestParam(value = "customerid1", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType1", defaultValue = "0", required = false) int addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid1", defaultValue = "0", required = false) long branchid) {
		String cwbs = this.excelImportEditDao.getEditInfoByBranch("'1','2','3'", branchid);
		this.dataImportService.ExportExcelMethod(response, request, cwbs, 0, 0, -1, branchid);
	}

	@RequestMapping("/exportExcleNoPIPei")
	public void exportExcleNoPIPei(Model model, HttpServletResponse response, HttpServletRequest request) {
		this.dataImportService.ExportNoPiPeiExcelMethod(response, request, this.getSessionUser().getBranchid());
	}

	@RequestMapping("/exportExcleToNoPIPei")
	public void exportExcleToNoPIPei(Model model, HttpServletResponse response, HttpServletRequest request) {
		String cwbs = this.excelImportEditDao.getEditInfoByBranch("0", 0);
		this.dataImportService.ExportExcelMethod(response, request, cwbs, 0, 0, 0, 0);
	}

	@RequestMapping("/checkemaildate")
	public @ResponseBody String checkemaildate(@RequestParam(value = "emaildate", defaultValue = "", required = false) String emaildate,
			@RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid,
			@RequestParam(value = "warehouseid", required = false, defaultValue = "0") final long warehouseid, @RequestParam(value = "areaid", required = false, defaultValue = "0") final long areaid)
			throws Exception {

		List<EmailDate> ed = this.emaildateDAO.getEmailDateForWhere(emaildate, areaid, warehouseid, customerid);
		if (ed.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"重复导入同一时间批次\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		}
	}

	@RequestMapping("/editBranchonBranch")
	public String editBrancheach(Model model, HttpServletRequest request, HttpServletResponse response) {
		List<Branch> bilist = this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue());
		long ifshow = request.getParameter("isshow") == null ? 0 : Long.valueOf(request.getParameter("isshow"));// 初始进入页面为0
		String cwb = request.getParameter("cwb") == null ? "" : request.getParameter("cwb");
		String Number = request.getParameter("onePageNumber") == null ? "10" : request.getParameter("onePageNumber");
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		long onePageNumber = Long.valueOf(Number);
		String zlpop = request.getParameter("zlpop") == null ? "1" : request.getParameter("zlpop");// 是未匹配还是匹配1：未匹配，2：匹配
		List<CwbOrder> cwblist = new ArrayList<CwbOrder>();
		Page pageobj = new Page();
		long NotSuccess = 0;// 未匹配数量
		// 合并已匹配的状态1,2,3 为了进页面即显示
		String AddressCodeEditType = this.gatherAddressEditType();

		long SuccessCount = this.cwbDAO.getEditInfoCountIsNotAddress("", AddressCodeEditType);// 地址库匹配的数量
		NotSuccess = this.cwbDAO.getEditInfoCountIsNotAddress("", String.valueOf(CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));// 未匹配数量
		String type = "0";
		List<ExcelImportEdit> listshow = new ArrayList<ExcelImportEdit>();// 已匹配数量对应站点的map
		if (ifshow > 0) {
			long SearchCount = NotSuccess;
			if ("2".equals(zlpop)) {// 已匹配
				listshow = this.excelImportEditDao.getEditInfoCountByBranch(cwb, AddressCodeEditType);// 地址库匹配的数量
				type = AddressCodeEditType;
				SearchCount = 0;
			}
			if (cwb.length() > 0) {// 查询订单
				SearchCount = 0;
				type = "";
				zlpop = "1";
			}
			pageobj = new Page(SearchCount, page, onePageNumber);
			cwblist = this.SearchEditInCwbOrder(cwb, page, onePageNumber, type);// 把查询的到edit表中的cwb
			// 合并后关联查询主表
		}
		model.addAttribute("branchs", bilist);
		model.addAttribute("customers", this.customerDAO.getAllCustomers());
		model.addAttribute("Order", cwblist);
		model.addAttribute("SuccessCount", SuccessCount);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("type", zlpop);
		model.addAttribute("NotSuccess", NotSuccess);
		model.addAttribute("SuccessAddress", listshow);// map
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "dataimport/editbrancheach";

	}

	private List<CwbOrder> SearchEditInCwbOrder(String cwb, int page, long onePageNumber, String type) {
		List<ExcelImportEdit> cwborderList;
		List<CwbOrder> cwblist = new ArrayList<CwbOrder>();
		cwborderList = this.excelImportEditDao.getEditInfoListIsNotAddress(page, onePageNumber, cwb, type);
		if (cwborderList.isEmpty()) {
			return cwblist;
		}
		StringBuffer sb = new StringBuffer();

		for (ExcelImportEdit ex : cwborderList) {
			sb.append("'");
			sb.append(ex.getCwb());
			sb.append("',");
		}
		String cwbs = sb.substring(0, sb.toString().length() - 1).toString();
		cwblist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
		return cwblist;
	}

	private String gatherAddressEditType() {
		String AddressCodeEditType = "";
		StringBuffer types = new StringBuffer();
		for (CwbOrderAddressCodeEditTypeEnum a : CwbOrderAddressCodeEditTypeEnum.values()) {
			if (CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue() != a.getValue()) {
				types.append("'");
				types.append(a.getValue());
				types.append("',");
			}
		}
		AddressCodeEditType = types.substring(0, types.length() - 1).toString();
		return AddressCodeEditType;
	}

	// 此时正在进行测试！！！。。。。
	// @RequestMapping("/editexcel2/{cwb2}")
	// public @ResponseBody String editexcel2(@PathVariable("cwb2") String cwb2,
	// @RequestParam(value = "excelbranch2", required = false) String
	// excelbranch2, HttpServletRequest request) throws Exception {
	// List<Branch> lb = new ArrayList<Branch>();
	// List<Branch> branchnamelist =
	// branchDAO.getBranchByBranchnameCheck(excelbranch2);
	// if (branchnamelist.size() > 0) {
	// lb = branchnamelist;
	// } else {
	// lb = branchDAO.getBranchByBranchcode(excelbranch2);
	// }
	// if (lb.size() == 0) {
	// return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}";
	// }
	//
	// //
	// CwbOrder co = cwbDAO.getCwbByCwb(cwb2);
	// CwbOrderAddressCodeEditTypeEnum addressCodeEditType =
	// CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
	// if (co.getAddresscodeedittype() ==
	// CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue() ||
	// co.getAddresscodeedittype() ==
	// CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue()) {//
	// 如果修改的数据原来是地址库匹配的或者是后来修改的
	// // 都将匹配状态变更为修改
	// addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
	// } else if (co.getAddresscodeedittype() ==
	// CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue() ||
	// co.getAddresscodeedittype() ==
	// CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()) {// 如果修改的数据原来是为匹配的
	// // 或者是人工匹配的
	// // 都将匹配状态变更为人工修改
	// addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
	// }
	// try {
	// cwbOrderService.updateDeliveryBranch(getSessionUser(), co, lb.get(0),
	// addressCodeEditType);
	// return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb2\":\"" + cwb2 +
	// "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
	// } catch (CwbException ce) {
	// return "{\"errorCode\":" + ce.getError().getValue() + ",\"error\":\"" +
	// ce.getMessage() + "\",\"cwb\":\"" + cwb2 + "\",\"excelbranch\":\"" +
	// lb.get(0).getBranchname() + "\"}";
	// }
	// //
	// }

	@RequestMapping("/addresslibrarymatching")
	public String editBrancheachPage() {
		return "dataimport/addresslibrarymatching";
	}

	// 测试所用方法
	@RequestMapping("/addresslibrarymatchingpage")
	public @ResponseBody List<Information> match(@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branches", required = false, defaultValue = "") String branches, HttpServletRequest request) {

		List<Information> infolist = new ArrayList<Information>();

		cwbs = request.getParameter("cwbs");
		branches = request.getParameter("branches");
		String[] realnamecwbs = cwbs.split("\r\n");
		String[] realnamebranches = branches.split("\r\n");

		// request.setAttribute("cwbs", realnamecwbs);
		// request.setAttribute("branches", realnamebranches);

		String cwberror = "";
		String brancherror = "";
		// String cwbss="";
		String cwbbranches = "";
		for (int i = 0; i < realnamecwbs.length; i++) {
			Information info = new Information();
			try {
				CwbOrder co = this.cwbDAO.getCwbByCwb(realnamecwbs[i]);
				if (co == null) {
					info.setCwb(realnamecwbs[i]);
					cwberror = "订单<font color='red'>[" + realnamecwbs[i] + "]</font>不存在  ";

				} else {
					info.setCwb(realnamecwbs[i]);
				}
				Branch branch = this.branchDAO.getBranchByBranchname(realnamebranches[i]);
				if (branch == null) {
					brancherror = "站点<font color='red'>[" + realnamebranches[i] + "]</font>不存在  ";
					info.setBranch(realnamebranches[i]);
				} else if ((branch != null) && (branch.getBranchid() == 0)) {
					brancherror = "站点<font color='red'>[" + realnamebranches[i] + "]</font>不存在  ";
					info.setBranch(realnamebranches[i]);
				} else {
					info.setBranch(realnamebranches[i]);
				}
				cwbbranches = cwberror + brancherror;
				info.setError(cwbbranches);
				cwberror = "";
				brancherror = "";
				if (co != null) {
					CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
					if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
						addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
					} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
							|| (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																														// 都将匹配状态变更为人工修改
						addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
					}
					this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), co, branch, addressCodeEditType);
				}

			} catch (Exception ce) {
				info.setCwb(realnamecwbs[i]);
				info.setError(ce.getMessage());
			}

			if (!info.getError().isEmpty()) {
				infolist.add(info);
			}

		}
		return infolist;
	}
}
