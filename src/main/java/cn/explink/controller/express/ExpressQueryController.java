package cn.explink.controller.express;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbApplyTuiHuoDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbKuaiDiDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbKuaiDi;
import cn.explink.domain.CwbKuaiDiView;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.service.CwbKuaiDiService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.LogToDayService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/expressQueryController")
@Controller
public class ExpressQueryController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	EmailDateDAO emaildateDao;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	LogToDayService logToDayService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	ExportService exportService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	CwbApplyTuiHuoDAO cwbApplyTuiHuoDAO;
	@Autowired
	CwbApplyZhongZhuanDAO cwbApplyZhongZhuanDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	JointService jointService;
	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;
	@Autowired
	CwbKuaiDiService cwbKuaiDiService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 快递订单查询
	 *
	 * @param model
	 * @param page
	 * @param timeType
	 * @param begindate
	 * @param enddate
	 * @param lanshoubranchids
	 * @param lanshouuserid
	 * @param paisongbranchids
	 * @param paisonguserid
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/kuaidilist/{page}")
	public String kuaidilist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "timeType", required = false, defaultValue = "1") long timeType,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "lanshoubranchid", required = false, defaultValue = "") String[] lanshoubranchids,
			@RequestParam(value = "lanshouuserid", required = false, defaultValue = "0") long lanshouuserid,
			@RequestParam(value = "paisongbranchid", required = false, defaultValue = "") String[] paisongbranchids,
			@RequestParam(value = "paisonguserid", required = false, defaultValue = "0") long paisonguserid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbKuaiDi> cwbKuaiDilist = new ArrayList<CwbKuaiDi>();
		List<CwbKuaiDiView> cwbViewList = new ArrayList<CwbKuaiDiView>();
		// 保存揽收站点的选择
		List<String> lanshoubranchidList = new ArrayList<String>();
		// 保存配送站点的选择
		List<String> paisongbranchidList = new ArrayList<String>();

		// 需要返回页面的前10条订单List

		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
		List<User> lanshouuserlist = new ArrayList<User>();
		List<User> paisonguserlist = new ArrayList<User>();

		if (isshow != 0) {
			lanshoubranchidList = this.dataStatisticsService.getList(lanshoubranchids);
			paisongbranchidList = this.dataStatisticsService.getList(paisongbranchids);

			String lanshoubranchidStr = this.dataStatisticsService.getStrings(lanshoubranchids);
			String paisongbranchidStr = this.dataStatisticsService.getStrings(paisongbranchids);

			cwbKuaiDilist = this.cwbKuaiDiDAO.getExpressOrderListPage(page, timeType, begindate, enddate, lanshoubranchidStr, lanshouuserid, paisongbranchidStr, paisonguserid);
			pageparm = new Page(this.cwbKuaiDiDAO.getExpressOrderListCount(timeType, begindate, enddate, lanshoubranchidStr, lanshouuserid, paisongbranchidStr, paisonguserid), page,
					Page.ONE_PAGE_NUMBER);

			if ((cwbKuaiDilist != null) && (cwbKuaiDilist.size() > 0)) {
				List<Branch> branchAllList = this.branchDAO.getAllBranches();
				List<User> userList = this.userDAO.getAllUser();
				cwbViewList = this.getCwbKuaiDiViewCount10(cwbKuaiDilist, branchAllList, userList);
			}
			String lanshouusers = this.dataStatisticsService.getStrings(lanshoubranchids);
			if (lanshouusers.length() > 0) {
				lanshouuserlist = this.userDAO.getAllUserByBranchIds(lanshouusers);
			}
			String paisongs = this.dataStatisticsService.getStrings(paisongbranchids);
			if (paisongs.length() > 0) {
				paisonguserlist = this.userDAO.getAllUserByBranchIds(paisongs);
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("lanshoubranchidList", lanshoubranchidList);
		model.addAttribute("paisongbranchidList", paisongbranchidList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("check", 1);
		model.addAttribute("lanshouuserlist", lanshouuserlist);
		model.addAttribute("paisonguserlist", paisonguserlist);

		return "express/queryTransOrder/kuaidilist";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = new String[19]; // 导出的列名
		String[] cloumnName2 = new String[19]; // 导出的英文列名

		this.exportService.SetCwbKuaiDiFields1(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "快递单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "CwbKuaiDi_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			long timeType = request.getParameter("timeType1") == null ? -1 : Long.parseLong(request.getParameter("timeType1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] lanshoubranchids = request.getParameterValues("lanshoubranchid1") == null ? new String[] {} : request.getParameterValues("lanshoubranchid1");
			String[] paisongbranchids = request.getParameterValues("paisongbranchid1") == null ? new String[] {} : request.getParameterValues("paisongbranchid1");
			long lanshouuserid = request.getParameter("lanshouuserid1") == null ? -1 : Long.parseLong(request.getParameter("lanshouuserid1").toString());
			long paisonguserid = request.getParameter("timeType1") == null ? -1 : Long.parseLong(request.getParameter("paisonguserid1").toString());

			List<CwbKuaiDi> cwbKuaiDilist = this.cwbKuaiDiDAO.getExpressOrderListNoPage(timeType, begindate, enddate, this.dataStatisticsService.getStrings(lanshoubranchids), lanshouuserid,
					this.dataStatisticsService.getStrings(paisongbranchids), paisonguserid);

			List<CwbKuaiDiView> cwbViewList = new ArrayList<CwbKuaiDiView>();
			if ((cwbKuaiDilist != null) && (cwbKuaiDilist.size() > 0)) {
				List<Branch> branchAllList = this.branchDAO.getAllBranches();
				List<User> userList = this.userDAO.getAllUser();
				cwbViewList = this.getCwbKuaiDiViewCount10(cwbKuaiDilist, branchAllList, userList);
			}

			final List<CwbKuaiDiView> cwbOrderViewList = cwbViewList;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < cwbOrderViewList.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = ExpressQueryController.this.exportService.setCwbKuaiDiObject(cloumnName3, request1, cwbOrderViewList, a, i, k);
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

	public List<CwbKuaiDiView> getCwbKuaiDiViewCount10(List<CwbKuaiDi> cwbKuaiDilist, List<Branch> branchList, List<User> userList) {
		List<CwbKuaiDiView> cwbOrderViewList = new ArrayList<CwbKuaiDiView>();
		Map<Long, Customer> customerMap = this.customerDao.getAllCustomersToMap();
		for (CwbKuaiDi ck : cwbKuaiDilist) {
			CwbKuaiDiView cwbKuaiDiView = new CwbKuaiDiView();
			cwbKuaiDiView.setCwb(ck.getCwb());
			cwbKuaiDiView.setLanshouusername(this.dataStatisticsService.getQueryUserName(userList, ck.getLanshouuserid()));
			cwbKuaiDiView.setLanshoubranchname(this.dataStatisticsService.getQueryBranchName(branchList, ck.getLanshoubranchid()));
			cwbKuaiDiView.setLanshoutime(ck.getLanshoutime());
			cwbKuaiDiView.setConsigneename(ck.getSendconsigneename());
			cwbKuaiDiView.setConsigneemobile(ck.getSendconsigneemobile());
			cwbKuaiDiView.setConsigneeaddress(ck.getSendconsigneeaddress());
			cwbKuaiDiView.setAllfee(ck.getAllfee() == null ? BigDecimal.ZERO : ck.getAllfee());
			cwbKuaiDiView.setFlowordertype(ck.getFlowordertype());
			cwbKuaiDiView.setRemark(ck.getRemark());

			cwbKuaiDiView.setShouldfare(ck.getTransitfee() == null ? BigDecimal.ZERO : ck.getTransitfee());
			cwbKuaiDiView.setPackagefee(ck.getPackagefee() == null ? BigDecimal.ZERO : ck.getPackagefee());
			cwbKuaiDiView.setInsuredrate(ck.getInsuredrate() == null ? BigDecimal.ZERO : ck.getInsuredrate());
			cwbKuaiDiView.setRealweight(ck.getRealweight() == null ? BigDecimal.ZERO : ck.getRealweight());
			cwbKuaiDiView.setPaymethod(ExpressSettleWayEnum.getByValue((int) ck.getPaytype()).getText());
			if ((null != ck.getSendconsigneecompany()) && !"".equals(ck.getSendconsigneecompany()) && (null != customerMap)) {
				Customer customer = customerMap.get(Long.valueOf(ck.getSendconsigneecompany()));
				if (null != customer) {
					cwbKuaiDiView.setCustomername(StringUtil.nullConvertToEmptyString(customer.getCompanyname()));
				} else {
					cwbKuaiDiView.setCustomername("");// 默认为空
				}

			}
			cwbKuaiDiView.setReceivablefee(ck.getReceivablefee() == null ? BigDecimal.ZERO : ck.getReceivablefee());
			cwbKuaiDiView.setPaisongbranchname(this.dataStatisticsService.getQueryBranchName(branchList, ck.getPaisongbranchid()));
			cwbKuaiDiView.setCustomercode(StringUtil.nullConvertToEmptyString(ck.getCustomercode()));
			cwbOrderViewList.add(cwbKuaiDiView);

		}
		return cwbOrderViewList;
	}
}
