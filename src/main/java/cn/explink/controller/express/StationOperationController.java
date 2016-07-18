/**
 *
 */
package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.CombineQueryView;
import cn.explink.domain.VO.express.DeliverSummaryItem;
import cn.explink.domain.VO.express.DeliverSummaryView;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressOrderQueryResult;
import cn.explink.domain.express.CwbOrderForCombine;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.domain.express.ExpressWeigh;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.express.DistributeConditionEnum;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressCombineTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.express.ExpressOutStationService;
import cn.explink.service.express.ExpressWeighService;
import cn.explink.service.express.StationOperationService;
import cn.explink.util.ExportUtil4Express;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * 站点操作
 *
 * @author songkaojun 2015年7月30日
 */
@RequestMapping("/stationOperation")
@Controller
public class StationOperationController extends ExpressCommonController {

	private static final String COMMA_SEPARATOR = ",";

	/**
	 * 库房合包菜单ID
	 */
	private static final Long WAREHOUSE_COMBINE_MENU_ID = Long.valueOf(501002);

	/**
	 * 快递揽件合包菜单ID
	 */
	private static final Long STATION_COMBINE_MENU_ID = Long.valueOf(504091);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private PreOrderDao preOrderDao;

	@Autowired
	private ExportmouldDAO exportmouldDAO;

	@Autowired
	private CwbOrderService cwborderService;

	@Autowired
	private ExpressOrderDao expressOrderDao;

	@Autowired
	private StationOperationService stationOperationService;

	@Autowired
	private ProvinceDAO provinceDAO;

	@Autowired
	private CityDAO cityDAO;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private ExpressOutStationService expressOutStationService;

	@Autowired
	private ExpressWeighService expressWeighService;
	
	@Autowired
	private SystemInstallDAO systemInstallDAO;

	/**
	 * 进入揽件分配/调整的功能页面
	 *
	 * @param model
	 * @param deliverid
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/takeExpressAssign/{page}")
	public String branchdeliverdetail(@PathVariable("page") long page, Model model, @RequestParam(value = "distributeCondition", required = false) Integer distributeCondition,
			@RequestParam(value = "deliverid", required = false) Long deliverid, @RequestParam(value = "returnResult", required = false) Integer returnResult) {
		this.initAssignViewByPage(page, model, distributeCondition, deliverid, returnResult);
		return "express/stationOperation/takeExpressAssign";
	}

	private void initAssignViewByPage(long page, Model model, Integer distributeCondition, Long deliverid, Integer returnResult) {
		List<User> deliverList = this.getDeliverList();
		List<DistributeConditionEnum> distributeConditionList = DistributeConditionEnum.getAllStatus();
		List<ExcuteStateEnum> returnResultList = ExcuteStateEnum.getReturnResultList();

		List<Integer> executeStateList = this.getExecuteStateByDistributeCondition(distributeCondition, returnResult);

		List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByConditions(page, executeStateList, deliverid, this.isAdmin(), this.getCurrentBranchid());
		if (distributeCondition == null) {
			distributeCondition = 1;
		}
		// add by wangzhiyu 添加：分配情况为已分配揽件员时，查询条件增加一个反馈结果下拉框
		model.addAttribute("returnResult", returnResult);
		model.addAttribute("returnResultList", returnResultList);

		model.addAttribute("deliverList", deliverList);
		model.addAttribute("selectedDistributeCondition", distributeCondition);
		model.addAttribute("selectedDeliverid", deliverid);
		model.addAttribute("distributeConditionList", distributeConditionList);
		model.addAttribute("preOrderList", preOrderList);
		model.addAttribute("page_obj",
				new Page(this.preOrderDao.getPreOrderCountByConditions(page, executeStateList, deliverid, this.isAdmin(), this.getCurrentBranchid()), page, Page.ONE_PAGE_NUMBER));
	}

	/**
	 * 打开分配小件员对话框
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/openAssignDlg")
	public String openAssignDlg(Model model, HttpServletRequest request, @RequestParam(value = "selectedPreOrders", required = false) String selectedPreOrders) {
		List<User> deliverList = this.getDeliverList();
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("selectedPreOrders", selectedPreOrders);
		return "express/stationOperation/assignDlg";
	}

	/**
	 * 分配
	 *
	 * @param model
	 * @param request
	 * @param selectedPreOrders
	 * @return
	 */
	@RequestMapping("/doAssign")
	@ResponseBody
	public Boolean doAssign(Model model, HttpServletRequest request, @RequestParam(value = "selectedPreOrders", required = false) String selectedPreOrders,
			@RequestParam(value = "deliverid", required = false) Integer deliverid) {
		List<Integer> preOrderIdList = this.convertToIdList(selectedPreOrders);
		User operateUser = this.getSessionUser();

		this.initAssignViewByPage(1, model, null, null, null);

		return this.stationOperationService.assignDeliver(preOrderIdList, deliverid, this.getCurrentBranchid(), operateUser);
	}

	private List<Integer> convertToIdList(String selectedPreOrders) {
		List<Integer> preOrderIdList = new ArrayList<Integer>();
		String[] selectedPreOrderArr = StringUtil.splitString(selectedPreOrders, StationOperationController.COMMA_SEPARATOR);
		for (String selectedPreOrder : selectedPreOrderArr) {
			preOrderIdList.add(Integer.parseInt(selectedPreOrder));
		}
		return preOrderIdList;
	}

	/**
	 * 打开站点超区对话框
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/openSuperzoneDlg")
	public String openSuperzoneDlg(Model model, HttpServletRequest request, @RequestParam(value = "selectedPreOrders", required = false) String selectedPreOrders) {
		model.addAttribute("selectedPreOrders", selectedPreOrders);
		return "express/stationOperation/superzoneDlg";
	}

	/**
	 * 站点超区
	 *
	 * @param model
	 * @param request
	 * @param selectedPreOrders
	 * @param deliverid
	 * @return
	 */
	@RequestMapping("/doSuperzone")
	@ResponseBody
	public Boolean doSuperzone(Model model, HttpServletRequest request, @RequestParam(value = "selectedPreOrders", required = false) String selectedPreOrders,
			@RequestParam(value = "note", required = false) String note) {
		List<Integer> preOrderIdList = this.convertToIdList(selectedPreOrders);

		User operateUser = this.getSessionUser();
		this.initAssignViewByPage(1, model, null, null, null);
		return this.stationOperationService.superzone(preOrderIdList, note, this.getCurrentBranchid(), operateUser);
	}

	private List<Integer> getExecuteStateByDistributeCondition(Integer distributeCondition, Integer returnResult) {
		List<Integer> executeStateList = new ArrayList<Integer>();
		if (distributeCondition == null) {// 默认为“未分配”
			executeStateList.add(ExcuteStateEnum.AllocatedStation.getValue());
		} else if (DistributeConditionEnum.NotDistribute.getValue() == distributeCondition) {
			executeStateList.add(ExcuteStateEnum.AllocatedStation.getValue());
		} else if (DistributeConditionEnum.AleryDistributed.getValue() == distributeCondition) {
			if (returnResult == null) {
				executeStateList.add(ExcuteStateEnum.AllocatedDeliveryman.getValue());
			} else if (ExcuteStateEnum.DelayedEmbrace.getValue() == returnResult) {
				executeStateList.add(ExcuteStateEnum.DelayedEmbrace.getValue());
			} else if (ExcuteStateEnum.EmbraceSuperzone.getValue() == returnResult) {
				executeStateList.add(ExcuteStateEnum.EmbraceSuperzone.getValue());
			} else {
				executeStateList.add(ExcuteStateEnum.AllocatedDeliveryman.getValue());
			}
		}
		return executeStateList;
	}

	/**
	 * 分配后导出
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param selectedPreOrders
	 */
	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "selectedPreOrders", required = false) String selectedPreOrders) {
		List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByPreOrderId(this.convertToIdList(selectedPreOrders));
		ExportUtil4Express.exportXls(request, response, preOrderList, ExpressPreOrder.class, "预订单信息");

	}

	/**
	 * 获取小件员
	 *
	 * @return
	 */
	private List<User> getDeliverList() {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getCurrentBranchid());
		return uList;
	}

	/**
	 * 进入揽件合包的功能页面
	 *
	 * @param model
	 * @param deliverid
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/takeExpressCombine/{page}")
	public String takeExpressCombine(@PathVariable("page") long page, Model model, @RequestParam(value = "packageNo", required = false) String packageNo,
			@RequestParam(value = "nextBranch", required = false) Long nextBranch, @RequestParam(value = "province", required = false) Integer provinceId,
			@RequestParam(value = "cities", required = false) String citiesStr, @RequestParam(value = "waybillNo", required = false) String waybillNo,
			@RequestParam(value = "waybillNos", required = false) String waybillNos, @RequestParam(value = "waybillTotalCount", required = false) Integer waybillTotalCount,
			@RequestParam(value = "itemTotalCount", required = false) Long itemTotalCount, @RequestParam(value = "addWaybill", required = false) String addWaybill) {
		this.initCombineView(page, model, provinceId, citiesStr, waybillNos);
		CwbOrder expressOrder = this.stationOperationService.getExpressOrderByWaybillNo(waybillNo, this.getCurrentBranchid(), this.getCombineType());
		model.addAttribute("nextBranch", nextBranch);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("selectedCities", citiesStr);
		model.addAttribute("waybillNos", waybillNos);
		model.addAttribute("packageNo", packageNo);
		model.addAttribute("page", page);
		if ((addWaybill != null) && addWaybill.equals("true")) {
			if (waybillTotalCount == null) {
				model.addAttribute("waybillTotalCount", 0);
			} else {
				model.addAttribute("waybillTotalCount", ++waybillTotalCount);
			}
			if (itemTotalCount == null) {
				model.addAttribute("itemTotalCount", 0L);
			} else {
				model.addAttribute("itemTotalCount", itemTotalCount + (expressOrder == null ? 0 : expressOrder.getSendcarnum()));
			}
		} else {
			model.addAttribute("waybillTotalCount", waybillTotalCount);
			model.addAttribute("itemTotalCount", itemTotalCount);
		}

		return "express/stationOperation/takeExpressCombine";
	}

	private List<ExpressCombineTypeEnum> getCombineType() {
		List<ExpressCombineTypeEnum> combineTypeEnumList = new ArrayList<ExpressCombineTypeEnum>();
		Set<Long> menuIdSet = new HashSet<Long>(this.roleDAO.getRoleAndMenuByRoleid(this.getSessionUser().getRoleid()));
		if (menuIdSet.contains(StationOperationController.WAREHOUSE_COMBINE_MENU_ID)) {
			combineTypeEnumList.add(ExpressCombineTypeEnum.WAREHOUSE_COMBINE);
		}
		if (menuIdSet.contains(StationOperationController.STATION_COMBINE_MENU_ID)) {
			combineTypeEnumList.add(ExpressCombineTypeEnum.STATION_COMBINE);
		}
		return combineTypeEnumList;
	}

	private void initCombineView(long page, Model model, Integer provinceId, String citiesStr, String excludeWaybillNos) {
		// 下一站（下一站至可以选择机构类型为“库房”，并且为正向流程的）
		// modified by songkaojun 2015-10-21 考虑二级站点
		// modified by songkaojun 2015-10-29 去掉“库房”限制条件
		List<Branch> nextBranchList = this.expressOutStationService.getNextBranchList4Combine(this.getCurrentBranchid(), this.getSessionUser());

		List<String> excludeWaybillNoList = this.splitStrByComma(excludeWaybillNos);

		List<Integer> cityIdList = this.convertToCityIdList(citiesStr);

		List<CwbOrderForCombine> expressOrderList = this.stationOperationService.getExpressOrderByPage(page, provinceId, cityIdList, excludeWaybillNoList, this.getCurrentBranchid(),
				this.getCombineType());

		List<AdressVO> provinceList = this.provinceDAO.getProvince();
		if (provinceId != null) {
			List<AdressVO> cityOfProvinceList = this.cityDAO.getCityOfProvince(provinceId);
			model.addAttribute("cityList", cityOfProvinceList);
		} else {
			model.addAttribute("cityList", new ArrayList<AdressVO>());
		}
		model.addAttribute("provinceList", provinceList);
		model.addAttribute("nextBranchList", nextBranchList);
		model.addAttribute("expressOrderList", expressOrderList);
		model.addAttribute("page_obj", new Page(this.expressOrderDao.getExpressOrderCount(page, provinceId, cityIdList, excludeWaybillNoList, this.getCurrentBranchid(), this.getCombineType()), page,
				Page.ONE_PAGE_NUMBER));
	}

	private List<Integer> convertToCityIdList(String citiesStr) {
		List<Integer> cityIdList = new ArrayList<Integer>();
		if ((citiesStr == null) || citiesStr.equals("null")) {
			citiesStr = "";
		}
		List<String> cityIdStrList = this.splitStrByComma(citiesStr);
		for (String cityIdStr : cityIdStrList) {
			cityIdList.add(Integer.parseInt(cityIdStr));
		}
		return cityIdList;
	}

	@RequestMapping("/getExpressOrderByWaybillNo")
	@ResponseBody
	public ExpressOrderQueryResult getExpressOrderByWaybillNo(Model model, HttpServletRequest request, @RequestParam(value = "waybillNo", required = false) String waybillNo) {
		ExpressOrderQueryResult result = new ExpressOrderQueryResult();
		boolean expressOrderExist = this.expressOrderDao.isExpressOrderExist(waybillNo, this.getCurrentBranchid());
		result.setExpressOrderExist(expressOrderExist);
		if (expressOrderExist) {
			CwbOrder order = this.stationOperationService.getExpressOrderByWaybillNo(waybillNo, this.getCurrentBranchid(), this.getCombineType());
			if (order == null) {
				result.setInPackage(true);
			} else {
				result.setInPackage(false);
				result.setCwbOrder(order);
			}
		}
		return result;
	}

	/**
	 * 打开移除对话框
	 *
	 * @param model
	 * @param request
	 * @param waybillNos
	 * @return
	 */
	@RequestMapping("/openRemoveDlg")
	public String openRemoveDlg(Model model, HttpServletRequest request, @RequestParam(value = "packageNo", required = false) String packageNo,
			@RequestParam(value = "nextBranch", required = false) Long nextBranch, @RequestParam(value = "provinceId", required = false) Integer provinceId,
			@RequestParam(value = "cities", required = false) String selectedCities, @RequestParam(value = "waybillNos", required = false) String waybillNos,
			@RequestParam(value = "waybillTotalCount", required = false) Integer waybillTotalCount, @RequestParam(value = "itemTotalCount", required = false) Long itemTotalCount) {
		List<String> waybillNoList = new ArrayList<String>(new HashSet<String>(this.splitStrByComma(waybillNos)));
		List<CwbOrderForCombine> selectedCwbOrderList = this.stationOperationService.getExpressOrderListByWaybillNoList(waybillNoList, this.getCurrentBranchid(), this.getCombineType());
		model.addAttribute("packageNo", packageNo);
		model.addAttribute("nextBranch", nextBranch);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("selectedCities", selectedCities);
		model.addAttribute("waybillNos", waybillNos);
		model.addAttribute("waybillTotalCount", waybillTotalCount);
		model.addAttribute("itemTotalCount", itemTotalCount);
		model.addAttribute("selectedCwbOrderList", selectedCwbOrderList);
		return "express/stationOperation/removeWaybillNoDlg";
	}

	@RequestMapping("/doRemove")
	public String doRemove(Model model, HttpServletRequest request, @RequestParam(value = "packageNo", required = false) String packageNo,
			@RequestParam(value = "nextBranch", required = false) Long nextBranch, @RequestParam(value = "provinceId", required = false) Integer provinceId,
			@RequestParam(value = "selectedCities", required = false) String selectedCities, @RequestParam(value = "leftWaybillNos", required = false) String leftWaybillNos,
			@RequestParam(value = "waybillTotalCount", required = false) Integer waybillTotalCount, @RequestParam(value = "itemTotalCount", required = false) Long itemTotalCount) {
		this.initCombineView(1, model, provinceId, "", leftWaybillNos);
		model.addAttribute("packageNo", packageNo);
		model.addAttribute("nextBranch", nextBranch);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("selectedCities", selectedCities);
		model.addAttribute("waybillNos", leftWaybillNos);
		model.addAttribute("waybillTotalCount", waybillTotalCount);
		model.addAttribute("itemTotalCount", itemTotalCount);
		return "express/stationOperation/takeExpressCombine";
	}

	/**
	 * 合包
	 *
	 * @param model
	 * @param request
	 * @param packageNo
	 * @param waybillNos
	 * @return
	 */
	@RequestMapping("/combine")
	public String combine(Model model, HttpServletRequest request, @RequestParam(value = "packageNo", required = false) String packageNo,
			@RequestParam(value = "hiddenWaybillNos", required = false) String waybillNos, @RequestParam(value = "nextBranch", required = false) Long nextBranch,
			@RequestParam(value = "province", required = false) Integer provinceId, @RequestParam(value = "cities", required = false) String selectedCities,
			@RequestParam(value = "page", required = false) Long page) {
		List<String> waybillNoList = new ArrayList<String>(new HashSet<String>(this.splitStrByComma(waybillNos)));
		this.initCombineView(page, model, provinceId, "", waybillNos);

		Bale bale = new Bale();
		bale.setBaleno(packageNo);
		// TODO
		bale.setBalestate(BaleStateEnum.WeiFengBao.getValue());
		bale.setBranchid(this.getCurrentBranchid());
		bale.setNextbranchid(nextBranch);
		bale.setCwbcount(waybillNoList.size());
		bale.setHandlerid((int) this.getSessionUser().getUserid());
		bale.setHandlername(this.getSessionUser().getRealname());

		this.stationOperationService.combinePackage(waybillNoList, this.getCurrentBranchCode(), bale);

		model.addAttribute("packageNo", "");
		model.addAttribute("nextBranch", nextBranch);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("selectedCities", selectedCities);
		model.addAttribute("waybillNos", null);
		model.addAttribute("page", page);

		return "express/stationOperation/takeExpressCombine";
	}

	/**
	 * 获取当前机构编码
	 */
	private String getCurrentBranchCode() {
		long branchid = this.getCurrentBranchid();
		return this.branchDAO.getBranchByBranchid(branchid).getBranchcode();
	}

	/**
	 * 获取当前机构名称
	 *
	 * @return
	 */
	private String getCurrentBranchName() {
		long branchid = this.getCurrentBranchid();
		return this.branchDAO.getBranchByBranchid(branchid).getBranchname();
	}

	private long getCurrentBranchid() {
		return this.getSessionUser().getBranchid();
	}

	private List<String> splitStrByComma(String toSplitStr) {
		List<String> splittedResultList = new ArrayList<String>();
		if (StringUtils.isEmpty(toSplitStr)) {
			return splittedResultList;
		}
		String[] strArray = StringUtil.splitString(toSplitStr, StationOperationController.COMMA_SEPARATOR);
		for (String str : strArray) {
			if (!StringUtils.isEmpty(str)) {
				splittedResultList.add(str);
			}
		}
		return splittedResultList;
	}

	/**
	 * 进入小件员交件汇总单页面
	 *
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/deliverSummary/{page}")
	public String deliverSummary(@PathVariable("page") long page, Model model, @RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		DeliverSummaryView deliverSummaryView = null;
		if (StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate)) {
			model.addAttribute("deliverSummaryView", deliverSummaryView);
			return "express/stationOperation/deliverSummary";
		}
		deliverSummaryView = this.stationOperationService.getDeliverSummary(beginDate, endDate, this.getCurrentBranchid());
		model.addAttribute("deliverSummaryView", deliverSummaryView);

		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);

		return "express/stationOperation/deliverSummary";
	}

	@RequestMapping("/getSummary")
	public String getSummary(Model model, HttpServletRequest request, @RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		DeliverSummaryView deliverSummaryView = this.stationOperationService.getDeliverSummary(beginDate, endDate, this.getCurrentBranchid());
		model.addAttribute("deliverSummaryView", deliverSummaryView);

		return "express/stationOperation/deliverSummary";
	}

	@RequestMapping("/exportSummary")
	@ResponseBody
	public void exportSummary(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		DeliverSummaryView deliverSummaryView = this.stationOperationService.getDeliverSummary(beginDate, endDate, this.getCurrentBranchid());

		List<DeliverSummaryItem> headList = new ArrayList<DeliverSummaryItem>();
		headList.add(deliverSummaryView.getHeadSummary());

		this.stationOperationService.exportSummaryXls(request, response, headList, deliverSummaryView.getBodySummaryList(), DeliverSummaryItem.class, "小件员交件汇总单");
	}

	/**
	 * 校验包号是否已经被使用
	 *
	 * @param response
	 * @param request
	 * @param packageNo
	 * @return
	 */
	@RequestMapping("/validatePackageCodeUsed")
	@ResponseBody
	public ExpressOpeAjaxResult checkPackageCodeIsUsed(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "packageNo", required = false) String packageNo) {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		result = this.stationOperationService.checkPackageNoIsUsed(packageNo);
		return result;
	}

	@RequestMapping("/checkPackageNoUnique")
	@ResponseBody
	public Boolean checkPackageNoUnique(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "packageNo", required = false) String packageNo) {
		return this.stationOperationService.checkPackageNoUnique(packageNo);
	}

	/**
	 * 进入合包查询页面
	 *
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/combineQuery/{page}")
	public String combineQuery(@PathVariable("page") long page, Model model, @RequestParam(value = "packageNo", required = false) String packageNo) {
		CombineQueryView combineQueryView = null;
		if (StringUtils.isEmpty(packageNo)) {
			model.addAttribute("combineQueryView", combineQueryView);
			return "express/stationOperation/combineQuery";
		}
		combineQueryView = this.stationOperationService.queryCombineInfo(packageNo);
		model.addAttribute("combineQueryView", combineQueryView);

		return "express/stationOperation/combineQuery";
	}

	/**
	 * 进入电子称称重页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/weighByScale")
	public String weighByScale(Model model) {
		//*************add*****************
		// add by bruce shangguan 20160712 获取电子秤称重时长
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("weightTime") ;
		String weightTime = "10" ; // 电子秤称重时长默认为10秒
		if(systemInstall != null && !StringUtils.isEmpty(systemInstall.getValue()) && systemInstall.getValue().trim().matches("^[1-9][0-9]*$")){
			weightTime = systemInstall.getValue() ;
		}
		model.addAttribute("weightTime", weightTime);
		// end 20160712
		//************end******************
		return "express/stationOperation/weighByScale";
	}

	@RequestMapping("/isExpressOrderExist")
	@ResponseBody
	public boolean isExpressOrderExist(Model model, HttpServletRequest request, @RequestParam(value = "waybillNo", required = false) String waybillNo) {
		return this.expressOrderDao.isExpressOrderExist(waybillNo, this.getCurrentBranchid());
	}

	@RequestMapping("/submitWeight")
	@ResponseBody
	public  boolean submitWeight(Model model, @RequestParam(value = "waybillNo", required = false) String waybillNo, @RequestParam(value = "weight", required = false) Double weight) {
		boolean successFlag = false ;
		if (!this.expressWeighService.isWeightExist(waybillNo)) {
			this.expressWeighService.saveWeight(this.constructExpressWeigh(waybillNo, weight));
		} else {
			this.expressWeighService.updateWeight(this.constructExpressWeigh(waybillNo, weight));
		}
		successFlag = true ;
		return successFlag ;
	}

	private ExpressWeigh constructExpressWeigh(String waybillNo, Double weight) {
		ExpressWeigh expressWeigh = new ExpressWeigh();
		expressWeigh.setCwb(waybillNo);
		expressWeigh.setWeight(weight);
		expressWeigh.setBranchid(this.getCurrentBranchid());
		expressWeigh.setBranchname(this.getCurrentBranchName());
		expressWeigh.setHandlerid(this.getSessionUser().getUserid());
		expressWeigh.setHandlername(this.getSessionUser().getRealname());
		expressWeigh.setHandletime(new Date());
		return expressWeigh;
	}

}
