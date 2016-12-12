package cn.explink.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ShiXiaoDAO;
import cn.explink.dao.SysConfigDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountArea;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderBranchMatchVo;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelImportEdit;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.VipExchangeFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.BranchAutoWarhouseService;
import cn.explink.service.BranchService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbTranslator;
import cn.explink.service.DataImportService;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ResultCollector;
import cn.explink.service.ResultCollectorManager;
import cn.explink.service.UserService;
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
	
	@Autowired
	ShiXiaoDAO shixiaoDao;

	private static Logger logger = LoggerFactory.getLogger(DataImportController.class);

	@Autowired
	ExcelImportEditDao excelImportEditDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	TransCwbDetailDAO transCwbDetailDAO;
	
	@Autowired
	TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	
	@Autowired
	OrderFlowDAO orderFlowDAO;
	
	@Autowired
	BranchAutoWarhouseService branchAutoWarhouseService;

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
		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 7);
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
	@Transactional
	public String datalose(Model model, @RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildateid,
			@RequestParam(value = "isEdit", defaultValue = "no", required = false) String isEdit) {
		if ("yes".equals(isEdit) && !ServiceUtil.isNowImport(emaildateid)) {
			List<CwbOrder>  cwblist  = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
			
			boolean flag = true;//判断是否可以按批次失效,只要批次中含有订单不处于数据导入状态，则不允许在按批次失效 ----刘武强20161003
			for(CwbOrder cwbOrder:cwblist){
				if(cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()){
					flag = false;
					model.addAttribute("ReturnMessage", "本批次中订单已经不处于数据导入状态，不能再按批次失效！");
				}
			}
			if(flag){
				List<String> cwbs = new ArrayList<String>();
				for(CwbOrder cwbOrder:cwblist){
					cwbs.add(cwbOrder.getCwb());
					this.shixiaoDao.creAbnormalOrdernew(cwbOrder.getOpscwbid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), cwbOrder.getCurrentbranchid(), cwbOrder.getCustomerid(), cwbOrder.getCwb(), cwbOrder
							.getDeliverybranchid(), cwbOrder.getFlowordertype(), cwbOrder.getNextbranchid(), cwbOrder.getStartbranchid(), this.getSessionUser().getUserid(), 129, cwbOrder.getCwbstate(), cwbOrder.getEmaildate());
				}
				String cwbStr = this.dataImportService.getInStrings(cwbs);
				
				
				this.cwbDAO.dataLose(emaildateid);//按批次失效主表的数据
				this.emaildateDAO.loseEmailDateById(emaildateid);//失效掉批次的数据
				//刘武强20161004
				this.transCwbDao.deleteTranscwbByCwbs(cwbStr);//批量删除订单-运单对照表数据 
				this.transCwbDetailDAO.deleteByCwbs(cwbStr);//批量删除运单详情表数据
				this.transcwbOrderFlowDAO.deleteByCwbs(cwbStr);//批量删除运单轨迹表数据
				this.orderFlowDAO.deleteByCwbs(cwbStr);//批量 删除订单轨迹表数据   
				this.logger.info("批次id：{} 失效成功，操作人{}",emaildateid, this.getSessionUser().getUserid());
				
				model.addAttribute("ReturnMessage", "操作成功");
				try {
					this.dataImportService.datalose(emaildateid);
					logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid :成功");
				} catch (Exception e) {
					logger.error("", e);
					logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid : " + emaildateid);
				}
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
		CwbOrderDTO dto = JacksonMapper.getInstance().reader(CwbOrderDTO.class).readValue(cwborderjson);
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
		logger.info("excelimport : errorCollectorid : " + errorCollector.getId());
		logger.info("excelimport : ParamEmaildate : " + ParamEmaildate);
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
						logger.error("", e);
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
					logger.error("", e);
					logger.error("JMS : Send : jms:topic:loseCwb : loseCwbByEmaildateid : " + emaildateid);
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
	public void addressBatchMatch(@RequestParam("emaildateid") long emaildateid) throws Exception {
		List<CwbOrder> cwbOrders = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
		for (CwbOrder cwbOrder : cwbOrders) {
			this.addressMatchService.doMatchAddress(this.getSessionUser().getUserid(), cwbOrder.getCwb());
		}
	}

	/*
	 * @RequestMapping("/stop") public @ResponseBody String
	 * stopImport(HttpServletRequest request){
	 * request.getSession().setAttribute("importStop", "stop"); return null; }
	 */

	@RequestMapping("/editBranch")
	public String editBranch(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildate, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType", defaultValue = "-1", required = false) Integer addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow, // 是否显示,
			@RequestParam(value = "zlpop", defaultValue = "2", required = false) long zlpop, // 是否显示
			@RequestParam(value="cwbstr1",required=false) String cwbstr1
	) {
		/*
		 * //如果系统设置 String
		 * showCustomer=systemInstallDAO.getSystemInstall("ifUseeditbranch"
		 * ).getValue(); if(showCustomer.equals("no")){ return
		 * editBrancheach(model,request,response); }
		 */
		if(addressCodeEditType != -1){
			if(StringUtils.isNotEmpty(cwbstr1)){
				if(StringUtils.isBlank(cwbs)){
					cwbs = cwbstr1;
				}
			}
		}
		model.addAttribute("cwbs",cwbs);
		model.addAttribute("branchs", branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
		model.addAttribute("customers", customerDAO.getAllCustomers());
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrderBranchMatchVo> cwbOrderBranchMatchVoList = new ArrayList<CwbOrderBranchMatchVo>();
		Page pageobj = new Page();
		long NotSuccess = 0;
		long SuccessAddress = 0;
		long SuccessEdit = 0;
		long SuccessNew = 0;
		
		String[] cwbstr=cwbs.trim().split("\r\n");
		StringBuffer cwbBuffer = new StringBuffer();
		String cwbstrs = "";
		for(String cwbss:cwbstr){
			if(cwbss.length()>0){
				cwbBuffer = cwbBuffer.append("'" + cwbss + "',");
			}
		}
		
	if (cwbBuffer.length() > 0) {
			cwbstrs = cwbBuffer.toString().substring(0, cwbBuffer.length() - 1);
		}
		
		if (isshow != 0) {
			cwbOrderBranchMatchVoList = this.cwbOrderService.getCwbBranchMatchVoByPageMyWarehouse(page, customerid, cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), onePageNumber, branchid);
			pageobj = new Page(cwbDAO.getcwborderCountIsMyWarehouse(customerid, cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), branchid), page, onePageNumber);
			NotSuccess = cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.WeiPiPei);
			SuccessAddress = cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
			SuccessEdit = cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.XiuGai);
			SuccessNew = cwbDAO.getcwborderCountIsNotAddress(customerid, "", "", cwbstrs, emaildate, CwbOrderAddressCodeEditTypeEnum.RenGong);
			
		}
		
		model.addAttribute("cwbOrderBranchMatchVoList", cwbOrderBranchMatchVoList);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("NotSuccess", NotSuccess);
		model.addAttribute("SuccessAddress", SuccessAddress);
		model.addAttribute("SuccessEdit", SuccessEdit);
		model.addAttribute("SuccessNew", SuccessNew);
		model.addAttribute("AllAddress", SuccessAddress + NotSuccess + SuccessEdit + SuccessNew);
			
		//获取系统参数
		List<SystemInstall> systemInstalls = systemInstallDAO.getAllProperties();
		for (SystemInstall systemInstall : systemInstalls) {
			if(systemInstall.getName().equals("addrDeliveryStationUrl")){
				model.addAttribute("addrDeliveryStationUrl", systemInstall.getValue());
				continue;
			}
			if(systemInstall.getName().equals("addrUser")){
				model.addAttribute("addrUser", systemInstall.getValue());
				continue;
			}
			if(systemInstall.getName().equals("addrPsw")){
				model.addAttribute("addrPsw", systemInstall.getValue());
				continue;
			}
		}
		
		// 处理批次
		List<EmailDate> eList = emaildateDAO.getEmailDateByCustomerid(customerid);
		List<AccountArea> aList = accountAreaDAO.getAllAccountArea();
		Map<Long, AccountArea> aMap = new HashMap<Long, AccountArea>();
		for (AccountArea a : aList) {
			aMap.put(a.getAreaid(), a);
		}
		Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
		List<CustomWareHouse> cwhList = customWareHouseDAO.getAllCustomWareHouse();
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
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		
		//获取站点
		List<Branch> branchList = this.branchDAO.getBanchByBranchidForStock(String.valueOf(BranchEnum.ZhanDian.getValue()));
		model.addAttribute("branchList", branchList);
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
	public @ResponseBody String resendAddressmatch(HttpServletRequest request, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "emaildate", defaultValue = "0", required = false) long emaildate, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, // 供货商编号
			@RequestParam(value = "addressCodeEditType", defaultValue = "-1", required = false) int addressCodeEditType, // 是否是以匹配的站点
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "onePageNumber", defaultValue = "50000", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow) {
		try {
			if (emaildate == 0) {
				return "{\"errorCode\":1,\"error\":\"请选择批次!\"}";
			}
			String[] cwbstr = cwbs.trim().split("\r\n");
			
			StringBuffer sb=new StringBuffer();
			String cwb="";
			
			for(String cwbss:cwbstr){
				if(cwbss.length()>0)
				sb.append("'"+cwbss+"',");
			}
			if(sb.length()>0){
			cwb=sb.toString().substring(0, sb.length() - 1);
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
	public String editBatchBranch(Model model, HttpServletRequest request,
			@RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "courierName", required = false, defaultValue = "") String courierName,
			@RequestParam(value = "branchcode", required = false, defaultValue = "") String branchcode,
			@RequestParam(value = "excelbranch", required = false, defaultValue = "") String excelbranch,
			@RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示
	) throws Exception {
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrderBranchMatchVo> cwbOrderBranchMatchVoList = new ArrayList<CwbOrderBranchMatchVo>();
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
			Branch branch = lb.size() > 0 ? lb.get(0) : null;
			if (branch != null) {
				User deliver = null;
				if(StringUtils.isNotBlank(courierName)) { //小件员非必须
					deliver = this.userService.getBranchDeliverByDeliverName(branch.getBranchid(), courierName);
				}
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
				
				/**把vip上门换里的揽退单也同时做匹配地址操作--start**/
				List<CwbOrder> tuiCoList=null;
				Set<String> tuiCwbSetFinal=null;
				if ((oList != null) && (oList.size() > 0)) {
					Set<String> tuiCwbSet=new HashSet<String>();
					for (CwbOrder cwbOrder : oList) {
						if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()&&
								cwbOrder.getExchangeflag()==VipExchangeFlagEnum.YES.getValue()){
							if(!cwbStrList.contains(cwbOrder.getExchangecwb())){
								tuiCwbSet.add(cwbOrder.getExchangecwb());
							}
						}
					}
					if(tuiCwbSet.size()>0){
						StringBuffer tuiCwbBuffer = new StringBuffer("");
						for (String tuicwb:tuiCwbSet) {
							tuiCwbBuffer = tuiCwbBuffer.append("'" + tuicwb + "',");
						}
						String tuiCwbstrs = tuiCwbBuffer.toString();
						if (tuiCwbstrs.length() > 0) {
							tuiCwbstrs = tuiCwbstrs.substring(0, tuiCwbstrs.length() - 1);
							tuiCoList = this.cwbDAO.getCwbByCwbs(tuiCwbstrs);
							if(tuiCoList!=null&&tuiCoList.size()>0){
								tuiCwbSetFinal=new HashSet<String>();
								for(CwbOrder tuiCo:tuiCoList){
									tuiCwbSetFinal.add(tuiCo.getCwb());
								}
								
							}
						}
					}
				}
				/**把vip上门换里的揽退单也同时做匹配地址操作--end**/
				
				Set<String> ignoreTuiCwbSet=new HashSet<String>();
				if ((oList != null) && (oList.size() > 0)) {
					if(tuiCoList!=null&&tuiCoList.size()>0){
						oList.addAll(tuiCoList);
						if(tuiCwbSetFinal!=null&&tuiCwbSetFinal.size()>0){
							cwbStrList.addAll(tuiCwbSetFinal);
						}
					}
					for (CwbOrder cwbOrder : oList) {
						if(ignoreTuiCwbSet.contains(cwbOrder.getCwb())){
							//配送单失败，关联的揽退单就不做匹配了
							continue;
						}
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
							this.cwbOrderService.updateDeliveryBranchAndCourier(this.getSessionUser(), cwbOrder, branch, addressCodeEditType, deliver);
							// 触发vip上门换业务的揽退单自动分站到货
							vipSmhAutoReachSite(cwbOrder,branch);
							count += 1;
						} catch (CwbException ce) {
							model.addAttribute("error", "匹配失败，" + ce.getMessage() + "!");
							if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()&&
									cwbOrder.getExchangeflag()==VipExchangeFlagEnum.YES.getValue()){
								ignoreTuiCwbSet.add(cwbOrder.getExchangecwb());
								logger.error("vip上门换业务配送单匹配失败,cwb="+cwbOrder.getCwb(),ce);
							}
						}
					}
					cwbStrList.remove(ignoreTuiCwbSet);
				}
				cwbOrderBranchMatchVoList = this.cwbOrderService.getCwbBranchMatchByCwbs(cwbStrList);
				pageobj = new Page(count, page, onePageNumber);
			} else {
				model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
				model.addAttribute("cwbOrderBranchMatchVoList", cwbOrderBranchMatchVoList);
				model.addAttribute("page_obj", pageobj);
				model.addAttribute("page", page);
				model.addAttribute("msg", "1");
				model.addAttribute("count", count);
				return "dataimport/editBatchBranch";
			}

		}
		model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
		model.addAttribute("cwbOrderBranchMatchVoList", cwbOrderBranchMatchVoList);
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
			return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + cwb + "\",\"branchcode\":\"" + lb.get(0).getBranchcode() + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
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
		StringBuffer notfoundCwb=new StringBuffer();
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
			if (cwbs.length() > 0) {
				String[] cwbstr = cwbs.split("\r\n");
				for (int i = 0; i < cwbstr.length; i++) {
					if (i == cwbstr.length) {
						break;
					}
					if (cwbstr[i].trim().length() == 0) {
						continue;
					}
					CwbOrder cwbOrder=cwbDAO.getCwbByCwb(cwbstr[i].trim());
					if (cwbOrder==null) {
						cwbs=cwbs.replace(cwbstr[i].trim(), "");
						notfoundCwb.append(cwbstr[i]).append(",");
					}
				}
			}
			List<CwbOrder> list = this.dataImportService.getListByCwbs(cwbs, emaildateid);
			try {
				this.dataImportService.reProduceTranscwb(list, customerlist);
				msg = "操作成功";
				if (notfoundCwb.toString().indexOf(",")>=0) {
					msg="操作成功，其中订单号为:"+notfoundCwb.toString().substring(0,notfoundCwb.length()-1)+"的订单不存在";
				}
			} catch (Exception e) {
				// logger.error("", e);
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
		
		//Added by leoliao at 2016-06-29 漏了条件
		if(!cwbs.equals("'--'")){
			List<CwbOrder> listCwbOrder = this.cwbDAO.getCwbOrderByDelivery(cwbs, "WEIPIPEI");
			if(listCwbOrder == null || listCwbOrder.isEmpty()){
				// 如果没有数据，则返回--保证查询结果中不会跳过这一条件
				cwbs =  "'--'";
			}else{
				StringBuffer sbCwb = new StringBuffer();
				 for(CwbOrder cwbOrder : listCwbOrder){
					 sbCwb.append("'");
					 sbCwb.append(cwbOrder.getCwb());
					 sbCwb.append("',");
				 }
				 cwbs = sbCwb.substring(0, sbCwb.length() - 1).toString();
			}
		}
		//Added end
		
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
		//long NotSuccess1 = 0;// 未匹配数量
		long NotSuccess = 0;// 未匹配数量
		// 合并已匹配的状态1,2,3 为了进页面即显示
		String AddressCodeEditType = this.gatherAddressEditType();

		long SuccessCount = this.cwbDAO.getEditInfoCountIsNotAddress("", AddressCodeEditType);// 地址库匹配的数量
		//NotSuccess1 = this.cwbDAO.getEditInfoCountIsNotAddress("", String.valueOf(CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));// 未匹配数量
		NotSuccess=dataImportService.getWeipipeiCount();
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
	/**
	 * 通过地图匹配站点
	 * @param matchcwbs
	 * @param matchbranchcode
	 * @param matchbranchname
	 * @return
	 */
	@RequestMapping("/bdmatchbranch")
	public @ResponseBody String bdmatchbranch(@RequestParam(value = "matchcwbs", defaultValue = "", required = false) String matchcwbs,
			@RequestParam(value = "matchbranchcode", defaultValue = "", required = false) String matchbranchcode,	
			@RequestParam(value = "matchbranchname", defaultValue = "", required = false) String matchbranchname	
			){
		String successCwb="";
		String failCwb="";
		//String errorMsg="";
		//Map<String, ExceptionMsg> failcwbInfo=new HashMap<String, ExceptionMsg>();
		//Map<String, String> infoMap=new HashMap<String, String>();
		//List<ExceptionMsg> exceptionMsgs=new ArrayList<ExceptionMsg>();
		List<Branch> lb = new ArrayList<Branch>();
		List<Branch> branchnamelist = branchDAO.getBranchByBranchnameCheck(matchbranchname);
		if (branchnamelist.size() > 0) {
			lb = branchnamelist;
		} else {
			lb = branchDAO.getBranchByBranchcode(matchbranchname);
		}
		if (lb.size() == 0) {
			return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}";
		}
		try {
			for (String cwb : matchcwbs.split(",")) {
				CwbOrder co = cwbDAO.getCwbByCwb(cwb);
				CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
				if (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue() || co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue()) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
																																															// 都将匹配状态变更为修改
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
				} else if (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue() || co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()) {// 如果修改的数据原来是为匹配的
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
				}
					try {
						cwbOrderService.updateDeliveryBranch(getSessionUser(), co, lb.get(0), addressCodeEditType);
						successCwb+=cwb+",";
					} catch (CwbException ce) {
						failCwb+=cwb+",";
						//infoMap.put(cwb, ce.getMessage());
						//ExceptionMsg exceptionMsg=new ExceptionMsg();
						//exceptionMsg.setCwb(cwb);
						//exceptionMsg.setErrorMessage(ce.getMessage());
						//exceptionMsg.setErrorType(ce.getError().getValue());
						//exceptionMsg.setMatchbranchName(lb.get(0).getBranchname());
						//failcwbInfo.put(cwb, exceptionMsg);
						//infoMap.put(cwb,ce.getError().getValue());
				} 
		} 
			}catch (Exception e) {
			return "{\"errorCode\":111,\"error\":\"系统内部异常\",\"cwb\":\"" + matchcwbs + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
		}
		if (successCwb!="") {
			successCwb=successCwb.substring(0, successCwb.length()-1);
		}
		if (failCwb.equals("")) {
			return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + matchcwbs + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\",\"successcwb\":\"" +successCwb+ "\"}";
		}
		failCwb=failCwb.substring(0, failCwb.length()-1);
		if (!failCwb.equals("")&&(failCwb.split(",").length<matchcwbs.split(",").length)) {
			/*StringBuffer errorMsgInfo=new StringBuffer();
			 for (Map.Entry<String, String> entry : infoMap.entrySet()) {
				 errorMsgInfo.append(entry.getKey()+":"+entry.getValue()+",");
			  }*/
			//String errorString=errorMsgInfo.subSequence(0, errorMsgInfo.length()-1).toString();
			return "{\"errorCode\":111,\"error\":\""+failCwb+"部分匹配失败(可能为状态不正确)"+"\",\"cwb\":\"" + matchcwbs + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\",\"successcwb\":\"" +successCwb+ "\"}";
		}
		if (failCwb.split(",").length==matchcwbs.split(",").length) {
			return "{\"errorCode\":222,\"error\":\""+failCwb+"全部匹配失败"+"\",\"cwb\":\"" + matchcwbs + "\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\",\"successcwb\":\"" +successCwb+ "\"}";

		}
	
		return "";
		
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
		
		//Added by leoliao at 2016-06-30 未匹配的需要过滤已审核和已有配送站点的订单
		if((cwb == null || cwb.trim().equals("")) && (cwblist != null) && String.valueOf(CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()).equals(type)){
			List<CwbOrder> listCwbOrderRemove = new ArrayList<CwbOrder>();
			
			for(CwbOrder tmpCwbOrder : cwblist){
				if(tmpCwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.YiShenHe.getValue() || tmpCwbOrder.getDeliverybranchid() != 0){
					listCwbOrderRemove.add(tmpCwbOrder);
				}
			}
			
			if(listCwbOrderRemove.size() > 0){
				cwblist.removeAll(listCwbOrderRemove);
			}
		}
		//Added end
		
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
		Set<String> matchedSmhSet=new HashSet<String>();
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
				Branch branch = this.branchDAO.getBranchByBranchnameMatch(realnamebranches[i]);
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
				if (co != null&&branch != null) {
					if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
							&&co.getExchangeflag()==VipExchangeFlagEnum.YES.getValue()){
						if(matchedSmhSet.contains(co.getCwb())){
							continue;
						}
					}
					
					CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
					if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
						addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
					} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
							|| (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是未匹配或者曾经人工修改匹配的
																														// 都将匹配状态变更为人工修改
						addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
					}
					this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), co, branch, addressCodeEditType);
					// 触发vip上门换业务的揽退单自动分站到货
					boolean success=vipSmhAutoReachSite(co,branch);
					if (success){
						matchedSmhSet.add(co.getCwb());
					}
					
					//vip上门换业务的揽退单也同时匹配地址
					if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()
							&&co.getExchangeflag()==VipExchangeFlagEnum.YES.getValue()){
						if(!matchedSmhSet.contains(co.getExchangecwb())){
							CwbOrder tuiCo = this.cwbDAO.getCwbByCwb(co.getExchangecwb());
							if(tuiCo!=null){
								CwbOrderAddressCodeEditTypeEnum addressCodeEditTypeTui = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
								if ((tuiCo.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (tuiCo.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
									addressCodeEditTypeTui = CwbOrderAddressCodeEditTypeEnum.XiuGai;
								} else if ((tuiCo.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
										|| (tuiCo.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是未匹配或者曾经人工修改匹配的
																																	// 都将匹配状态变更为人工修改
									addressCodeEditTypeTui = CwbOrderAddressCodeEditTypeEnum.RenGong;
								}
								this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), tuiCo, branch, addressCodeEditTypeTui);
								// 触发vip上门换业务的揽退单自动分站到货
								boolean successTui=vipSmhAutoReachSite(tuiCo,branch);
								if(successTui){
									matchedSmhSet.add(tuiCo.getCwb());
								}
							}
						}
					}
					
					
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
	
	/**
	 * 根据站点获取小件员
	 * 2016年6月16日 下午5:04:35
	 * @param branchId
	 * @return
	 */
	@RequestMapping("/getCourierByBranch")
	@ResponseBody
	public List<User> getCourierByBranch(String branchcode, String branchname) {
		Branch branch = null;
		if (StringUtils.isNotBlank(branchcode)) { // 优先编码
			branch = this.branchService.getBranchByBranchcode(branchcode);
			// 兼容名称
			if (branch == null) {
				branch = this.branchService.getBranchByBranchname(branchcode);
			}
		} else if (StringUtils.isNotBlank(branchname)) {
			branch = this.branchService.getBranchByBranchname(branchname);
		}
		List<User> courierList;
		if (branch != null) {
			courierList = this.userService.getDeliverList(branch.getBranchid());
		} else {
			courierList = new ArrayList<User>();
		}
		return courierList;
	}
	
	/**
	 * 保存站点跟小件员
	 * 2016年6月17日 上午10:50:16
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveBranchAndCourier")
	@ResponseBody
	public String saveBranchAndCourier(String cwb, String branchcode, String courierName) throws Exception {
		Branch branch = this.branchService.getBranchByBranchcode(branchcode);
		if (branch == null) {
			return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}";
		}
		User deliver = null;
		if(StringUtils.isNotBlank(courierName)) { //小件员非必须
			deliver = this.userService.getBranchDeliverByDeliverName(branch.getBranchid(), courierName);
			if(deliver == null) {
				return "{\"errorCode\":1,\"error\":\"站点“" + branch.getBranchname() + "”不存在该小件员\"}";
			}
		}
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
		if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue())
				|| (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
			// 都将匹配状态变更为修改
			addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
		} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
				|| (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																											// 或者是人工匹配的
																											// 都将匹配状态变更为人工修改
			addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
		}
		try {
			this.cwbOrderService.updateDeliveryBranchAndCourier(this.getSessionUser(), co, branch, addressCodeEditType, deliver);
			return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + cwb + "\",\"excelbranch\":\""
					+ branch.getBranchname() + "\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":" + ce.getError().getValue() + ",\"error\":\"" + ce.getMessage() + "\",\"cwb\":\""
					+ cwb + "\",\"excelbranch\":\"" + branch.getBranchname() + "\"}";
		}
	}
	
	/**
	 * 保存小件员
	 * @date 2016年7月29日 下午12:01:01
	 * @param cwb
	 * @param courierName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveCourier")
	@ResponseBody
	public String saveCourier(String cwb, String courierName) throws Exception {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co.getDeliverybranchid() == 0) {
			return "{\"errorCode\":1,\"error\":\"未匹配站点\"}";
		}
		Branch branch = this.branchService.getBranchByBranchid(co.getDeliverybranchid());
		if (branch == null) {
			return "{\"errorCode\":1,\"error\":\"匹配站点不存在\"}";
		}
		User deliver = null;
		if (StringUtils.isNotBlank(courierName)) {
			deliver = this.userService.getBranchDeliverByDeliverName(branch.getBranchid(), courierName);
			if(deliver == null) {
				return "{\"errorCode\":1,\"error\":\"匹配站点“" + branch.getBranchname() + "”不存在该小件员\"}";
			}
		}
		this.cwbOrderService.updateDeliveryCourier(cwb, deliver);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	// 触发vip上门换业务里的揽退单自动分站到货
	private boolean vipSmhAutoReachSite(CwbOrder co, Branch branch){
		boolean success=false;
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
				&&co.getExchangeflag()==VipExchangeFlagEnum.YES.getValue()){
			success=true;
			try {
				this.branchAutoWarhouseService.branchAutointoWarhouse(co, branch);
			} catch (Exception e) {
				logger.error("上门退单人手匹配地址库自动分站到货报错,cwb="+co.getCwb(), e);
			}
		}
		return success;
	}
}
