/**
 *
 */
package cn.explink.service.express.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.CombineQueryView;
import cn.explink.domain.VO.express.DeliverSummary;
import cn.explink.domain.VO.express.DeliverSummaryItem;
import cn.explink.domain.VO.express.DeliverSummaryView;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.express.CwbOrderForCombine;
import cn.explink.domain.express.CwbOrderForCombineQuery;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressCombineTypeEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.service.express.StationOperationService;
import cn.explink.service.express.tps.adaptor.ExpressDeliveryOrderService4TPS;
import cn.explink.util.BrowserUtils;
import cn.explink.util.ExcelExportUtil;
import cn.explink.util.poi.excel.ExcelPublicUtil;
import cn.explink.util.poi.excel.annotation.ExcelTarget;
import cn.explink.util.poi.excel.entity.ExcelExportEntity;
import cn.explink.util.poi.excel.entity.ExcelTitle;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

/**
 * @author songkaojun 2015年8月6日
 */
@Service
public class StationOperationServiceImpl implements StationOperationService {

	private static final Logger logger = LoggerFactory.getLogger(StationOperationServiceImpl.class);

	@Autowired
	private ExpressOrderDao expressOrderDao;

	@Autowired
	private BaleDao baleDao;

	@Autowired
	private BaleCwbDao baleCwbDao;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private PreOrderDao preOrderDao;

	@Autowired
	private ExpressDeliveryOrderService4TPS expressDeliveryOrderService4TPS;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private ProvinceDAO provinceDAO;

	@Autowired
	private CityDAO cityDAO;

	@Autowired
	private CwbDAO CwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Override
	public boolean assignDeliver(List<Integer> preOrderIdList, int delivermanId, long branchid, User distributeUser) {
		this.passBackAssignToTPS(preOrderIdList, branchid, distributeUser);

		User deliverman = this.userDAO.getUserByUserid(delivermanId);
		return this.preOrderDao.updateDeliverByIdList(preOrderIdList, ExcuteStateEnum.AllocatedDeliveryman.getValue(), delivermanId, deliverman.getRealname(), distributeUser.getUserid(),
				distributeUser.getRealname());
	}

	@Override
	public boolean superzone(List<Integer> preOrderIdList, String note, long branchid, User distributeUser) {
		this.passBackSuperzoneToTPS(preOrderIdList, branchid, distributeUser);
		return this.preOrderDao.updateExcuteStateByIdList(preOrderIdList, ExcuteStateEnum.StationSuperzone.getValue(), note);
	}

	/**
	 *
	 * @param preOrderIdList
	 */
	private void passBackAssignToTPS(List<Integer> preOrderIdList, long branchid, User distributeUser) {
		List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByPreOrderId(preOrderIdList);
		List<String> preOrderNoList = new ArrayList<String>();
		for (ExpressPreOrder preOrder : preOrderList) {
			preOrderNoList.add(preOrder.getPreOrderNo());
		}

		String tpsbranchCode = this.branchDAO.getBranchByBranchid(branchid).getTpsbranchcode();
		this.expressDeliveryOrderService4TPS.assignDeliver(preOrderNoList, tpsbranchCode, distributeUser.getUsername());
	}

	private void passBackSuperzoneToTPS(List<Integer> preOrderIdList, long branchid, User distributeUser) {
		List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByPreOrderId(preOrderIdList);
		List<String> preOrderNoList = new ArrayList<String>();
		for (ExpressPreOrder preOrder : preOrderList) {
			preOrderNoList.add(preOrder.getPreOrderNo());
		}

		String tpsbranchCode = this.branchDAO.getBranchByBranchid(branchid).getTpsbranchcode();
		this.expressDeliveryOrderService4TPS.superzone(preOrderNoList, tpsbranchCode, distributeUser.getUsername());
	}

	@Override
	public CwbOrder getExpressOrderByWaybillNo(String waybillNo, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		return this.expressOrderDao.getExpressOrderByWaybillNo(waybillNo, currentBranchid, combineTypeList);
	}

	@Override
	public List<CwbOrderForCombine> getExpressOrderListByWaybillNoList(List<String> waybillNoList, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		List<CwbOrder> cwbOrderList = new ArrayList<CwbOrder>();
		for (String waybillNo : waybillNoList) {
			CwbOrder cwbOrder = this.expressOrderDao.getExpressOrderByWaybillNo(waybillNo, currentBranchid, combineTypeList);
			cwbOrderList.add(cwbOrder);
		}
		return this.convertToCwbOrderForCombine(cwbOrderList);
	}

	@Override
	public boolean combinePackage(List<String> waybillNoList, String branchCode, Bale bale) {
		boolean succeedSaved = this.savePackageInfo(waybillNoList, bale);
		this.expressDeliveryOrderService4TPS.upLoadPackageInfo(bale.getBaleno(), waybillNoList, branchCode, bale.getHandlername());
		return succeedSaved;
	}

	private boolean savePackageInfo(List<String> waybillNoList, Bale bale) {
		try {
			long id = this.baleDao.createBale(bale);
			for (String waybillNo : waybillNoList) {
				this.baleCwbDao.createBale(id, bale.getBaleno(), waybillNo);
			}
			// added by jiangyu 批量更新订单的包号信息
			this.CwbDAO.updatePackagecode(bale.getBaleno(), bale.getNextbranchid(), waybillNoList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public DeliverSummaryView getDeliverSummary(String beginDate, String endDate, long branchid) {
		DeliverSummaryView summaryView = new DeliverSummaryView();
		DeliverSummaryItem headSummary = new DeliverSummaryItem();
		List<DeliverSummaryItem> bodySummaryList = new ArrayList<DeliverSummaryItem>();
		Map<Long, String> collectorMap = this.getCollectorMapByBranchId(branchid);
		List<DeliverSummary> deliverSummaryList = this.expressOrderDao.getDeliverSummary(beginDate, endDate, collectorMap.keySet());
		// 同一个小件员可能出现多次的汇总列表
		List<DeliverSummaryItem> repeatedBodySummaryList = new ArrayList<DeliverSummaryItem>();

		// 汇总
		long summaryNum = 0L;
		BigDecimal summaryFee = new BigDecimal(0);
		// 现付
		long nowPayNumTotal = 0L;
		BigDecimal nowPayTotalFeeTotal = new BigDecimal(0);
		// 月结
		long monthPayNumTotal = 0L;
		BigDecimal monthPayTotalFeeTotal = new BigDecimal(0);
		// 到付
		long arrivePayNumTotal = 0L;
		BigDecimal arrivePayTotalFeeTotal = new BigDecimal(0);

		for (DeliverSummary deliverSummary : deliverSummaryList) {
			DeliverSummaryItem bodySummary = new DeliverSummaryItem();
			bodySummary.setDeliverId(deliverSummary.getDeliverid());
			bodySummary.setDeliverName(collectorMap.get(deliverSummary.getDeliverid()));

			// 表体汇总
			long summaryNumBody = 0L;
			BigDecimal summaryFeeBody = new BigDecimal(0);

			long orderSum = deliverSummary.getOrderSum();
			BigDecimal totalfeeSum = deliverSummary.getTotalfeeSum();
			if (ExpressSettleWayEnum.NowPay.getValue().equals(deliverSummary.getPaymethod())) {
				bodySummary.setNowPayNum(orderSum);
				bodySummary.setNowPayTotalFee(totalfeeSum);

				nowPayNumTotal += orderSum;
				nowPayTotalFeeTotal = nowPayTotalFeeTotal.add(totalfeeSum);
			} else if (ExpressSettleWayEnum.MonthPay.getValue().equals(deliverSummary.getPaymethod())) {
				bodySummary.setMonthPayNum(orderSum);
				bodySummary.setMonthPayTotalFee(totalfeeSum);

				monthPayNumTotal += orderSum;
				monthPayTotalFeeTotal = monthPayTotalFeeTotal.add(totalfeeSum);
			} else if (ExpressSettleWayEnum.ArrivePay.getValue().equals(deliverSummary.getPaymethod())) {
				bodySummary.setArrivePayNum(orderSum);
				bodySummary.setArrivePayTotalFee(totalfeeSum);

				arrivePayNumTotal += orderSum;
				arrivePayTotalFeeTotal = arrivePayTotalFeeTotal.add(totalfeeSum);
			} else if (deliverSummary.getPaymethod() == -1) {
				continue;
			}

			summaryNumBody += orderSum;
			summaryFeeBody = summaryFeeBody.add(totalfeeSum);
			bodySummary.setSummaryNum(summaryNumBody);
			bodySummary.setSummaryFee(summaryFeeBody);
			repeatedBodySummaryList.add(bodySummary);

			summaryNum += orderSum;
			summaryFee = summaryFee.add(totalfeeSum);
		}

		headSummary.setSummaryNum(summaryNum);
		headSummary.setSummaryFee(summaryFee);
		headSummary.setNowPayNum(nowPayNumTotal);
		headSummary.setNowPayTotalFee(nowPayTotalFeeTotal);
		headSummary.setMonthPayNum(monthPayNumTotal);
		headSummary.setMonthPayTotalFee(monthPayTotalFeeTotal);
		headSummary.setArrivePayNum(arrivePayNumTotal);
		headSummary.setArrivePayTotalFee(arrivePayTotalFeeTotal);

		// 小件员去重汇总
		Map<Long, DeliverSummaryItem> bodySummaryMap = new HashMap<Long, DeliverSummaryItem>();
		for (DeliverSummaryItem repeatedBodySummary : repeatedBodySummaryList) {
			Long deliverId = Long.valueOf(repeatedBodySummary.getDeliverId());
			if (bodySummaryMap.containsKey(deliverId)) {
				DeliverSummaryItem deliverSummaryItem = bodySummaryMap.get(deliverId);

				deliverSummaryItem.setSummaryNum(deliverSummaryItem.getSummaryNum() + repeatedBodySummary.getSummaryNum());
				deliverSummaryItem.setSummaryFee(deliverSummaryItem.getSummaryFee().add(repeatedBodySummary.getSummaryFee()));
				deliverSummaryItem.setNowPayNum(deliverSummaryItem.getNowPayNum() + repeatedBodySummary.getNowPayNum());
				deliverSummaryItem.setNowPayTotalFee(deliverSummaryItem.getNowPayTotalFee().add(repeatedBodySummary.getNowPayTotalFee()));
				deliverSummaryItem.setArrivePayNum(deliverSummaryItem.getArrivePayNum() + repeatedBodySummary.getArrivePayNum());
				deliverSummaryItem.setArrivePayTotalFee(deliverSummaryItem.getArrivePayTotalFee().add(repeatedBodySummary.getArrivePayTotalFee()));
				deliverSummaryItem.setMonthPayNum(deliverSummaryItem.getMonthPayNum() + repeatedBodySummary.getMonthPayNum());
				deliverSummaryItem.setMonthPayTotalFee(deliverSummaryItem.getMonthPayTotalFee().add(repeatedBodySummary.getMonthPayTotalFee()));

				bodySummaryMap.put(deliverId, deliverSummaryItem);
			} else {
				bodySummaryMap.put(deliverId, repeatedBodySummary);
			}
		}
		bodySummaryList.addAll(bodySummaryMap.values());
		summaryView.setHeadSummary(headSummary);
		summaryView.setBodySummaryList(bodySummaryList);
		return summaryView;
	}

	private Map<Long, String> getCollectorMapByBranchId(long branchid) {
		Map<Long, String> collectorMap = this.getDeliverMapByBranchid(branchid);
		return collectorMap;
	}

	private void createSheet(HSSFWorkbook workbook, ExcelTitle entity, Class<?> pojoClass, Collection<?> headDataSet, Collection<?> dataSet) {
		try {
			Sheet sheet = workbook.createSheet(entity.getSheetName());
			// 创建表格属性
			Map<String, HSSFCellStyle> styles = ExcelExportUtil.createStyles(workbook);
			Drawing patriarch = sheet.createDrawingPatriarch();
			List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
			// 得到所有字段
			Field fileds[] = ExcelPublicUtil.getClassFields(pojoClass);
			ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
			String targetId = null;
			if (etarget != null) {
				targetId = etarget.id();
			}
			ExcelExportUtil.getAllExcelField(targetId, fileds, excelParams, pojoClass, null);
			ExcelExportUtil.sortAllParams(excelParams);
			int index = 0;
			int feildWidth = ExcelExportUtil.getFieldWidth(excelParams);
			if (entity.getTitle() != null) {
				int i = ExcelExportUtil.createHeaderRow(entity, sheet, workbook, feildWidth);
				sheet.createFreezePane(0, 2 + i, 0, 2 + i);
				index += i;
			} else {
				sheet.createFreezePane(0, 2, 0, 2);
			}
			List<ExcelExportEntity> headExcelParams = new ArrayList<ExcelExportEntity>();
			headExcelParams.addAll(excelParams.subList(1, excelParams.size()));
			int nextIndex = StationOperationServiceImpl.writeBody(workbook, entity, headDataSet, sheet, styles, patriarch, headExcelParams, index);
			nextIndex += 2;
			StationOperationServiceImpl.writeBody(workbook, entity, dataSet, sheet, styles, patriarch, excelParams, nextIndex++);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private static int writeBody(HSSFWorkbook workbook, ExcelTitle entity, Collection<?> dataSet, Sheet sheet, Map<String, HSSFCellStyle> styles, Drawing patriarch,
			List<ExcelExportEntity> excelParams, int index) throws Exception {
		ExcelExportUtil.createTitleRow(entity, sheet, workbook, index, excelParams);
		index += 2;
		ExcelExportUtil.setCellWith(excelParams, sheet);
		Iterator<?> its = dataSet.iterator();
		while (its.hasNext()) {
			Object t = its.next();
			index += ExcelExportUtil.createCells(patriarch, index, t, excelParams, sheet, workbook, styles);
		}
		return index;
	}

	@Override
	public void exportSummaryXls(HttpServletRequest request, HttpServletResponse response, List<?> head, List<?> list, Class<?> clz, String fileName) {
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			if (fileName.indexOf("流水号") > -1) {
				codedFileName = fileName;
			} else {
				codedFileName = fileName + "" + new Date().getTime();
			}
			// 根据浏览器进行转码，使其支持中文文件名
			if (BrowserUtils.isIE(request)) {
				response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = this.exportExcel(new ExcelTitle(fileName, null, fileName), clz, head, list);
			fOut = response.getOutputStream();

			workbook.write(fOut);

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	private HSSFWorkbook exportExcel(ExcelTitle entity, Class<?> pojoClass, Collection<?> headDataSet, Collection<?> dataSet) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		this.createSheet(workbook, entity, pojoClass, headDataSet, dataSet);
		return workbook;
	}

	@Override
	public String getPackageNo() {
		return this.expressDeliveryOrderService4TPS.getPackageNo();
	}

	@Override
	public List<CwbOrderForCombine> getExpressOrderByPage(long page, Integer provinceId, List<Integer> cityIdList, List<String> excludeWaybillNoList, long currentBranchid,
			List<ExpressCombineTypeEnum> combineTypeList) {
		List<CwbOrder> cwbOrderList = this.expressOrderDao.getExpressOrderByPage(page, provinceId, cityIdList, excludeWaybillNoList, currentBranchid, combineTypeList);
		return this.convertToCwbOrderForCombine(cwbOrderList);
	}

	private List<CwbOrderForCombine> convertToCwbOrderForCombine(List<CwbOrder> cwbOrderList) {
		List<CwbOrderForCombine> cwbOrderForCombineList = new ArrayList<CwbOrderForCombine>();

		Map<Integer, String> provinceMap = this.getProvinceMap();
		Map<Integer, String> cityMap = this.getCityMap();
		for (CwbOrder cwbOrder : cwbOrderList) {
			CwbOrderForCombine cwbOrderForCombine = new CwbOrderForCombine();
			cwbOrderForCombine.setCwb(cwbOrder.getCwb());
			cwbOrderForCombine.setSendcarnum(cwbOrder.getSendcarnum());
			cwbOrderForCombine.setCollectorname(cwbOrder.getCollectorname());
			cwbOrderForCombine.setInstationdatetime(cwbOrder.getInstationdatetime());
			cwbOrderForCombine.setPayMethodName(ExpressSettleWayEnum.getByValue(cwbOrder.getPaymethod()).getText());
			cwbOrderForCombine.setTotalfee(cwbOrder.getTotalfee());
			cwbOrderForCombine.setShouldfare(cwbOrder.getShouldfare());
			cwbOrderForCombine.setPackagefee(cwbOrder.getPackagefee());
			cwbOrderForCombine.setInsuredfee(cwbOrder.getInsuredfee());

			cwbOrderForCombine.setProvinceName(provinceMap.get(cwbOrder.getRecprovinceid()));
			cwbOrderForCombine.setCityName(cityMap.get(cwbOrder.getReccityid()));

			cwbOrderForCombineList.add(cwbOrderForCombine);
		}
		return cwbOrderForCombineList;
	}

	private Map<Integer, String> getProvinceMap() {
		List<AdressVO> allProvinceList = this.provinceDAO.getAllProvince();
		Map<Integer, String> provinceMap = new HashMap<Integer, String>();
		for (AdressVO province : allProvinceList) {
			provinceMap.put(province.getId(), province.getName());
		}
		return provinceMap;
	}

	private Map<Integer, String> getCityMap() {
		List<AdressVO> allCityList = this.cityDAO.getAllCity();
		Map<Integer, String> cityMap = new HashMap<Integer, String>();
		for (AdressVO city : allCityList) {
			cityMap.put(city.getId(), city.getName());
		}
		return cityMap;
	}

	private Map<Long, String> getDeliverMapByBranchid(long branchid) {
		List<User> deliverList = this.getDeliverList(branchid);
		Map<Long, String> deliverMap = new HashMap<Long, String>();
		for (User deliver : deliverList) {
			deliverMap.put(deliver.getUserid(), deliver.getRealname());
		}
		return deliverMap;
	}

	/**
	 * 获取小件员
	 *
	 * @return
	 */
	private List<User> getDeliverList(long branchid) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, branchid);
		return uList;
	}

	@Override
	public ExpressOpeAjaxResult checkPackageNoIsUsed(String packageNo) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		Map<String, Object> map = new HashMap<String, Object>();
		PjDeliveryOrderService service = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		try {
			InvocationContext.Factory.getInstance().setTimeout(100000);
			boolean result = service.checkPackageExist(packageNo);
			res.setStatus(true);
			map.put("checkResult", result);
		} catch (OspException e) {
			res.setStatus(false);
			res.setMsg("校验包号唯一性接口异常,请联系管理员");
			logger.error("校验包号唯一性接口异常,异常原因{" + e.getMessage() + "}");
		}
		res.setAttributes(map);
		return res;
	}

	@Override
	public boolean checkPackageNoUnique(String packageNo) {
		Bale bale = this.baleDao.getBaleOnway(packageNo);
		if (bale != null) {
			return false;
		}
		return true;
	}

	@Override
	public CombineQueryView queryCombineInfo(String packageNo) {
		CombineQueryView combineQueryView = new CombineQueryView();
		Bale bale=this.baleDao.getBaleOnway(packageNo);
		List<String> cwbList = null;
		if(bale!=null){
			cwbList=this.baleCwbDao.getCwbsByBale(""+bale.getId());
		}
		if ((cwbList == null) || cwbList.isEmpty()) {
			return null;
		}
		List<CwbOrder> cwbOrderList = this.CwbDAO.getCwbOrderByCwbList(cwbList);
		Map<Integer, String> provinceMap = this.getProvinceMap();
		Map<Integer, String> cityMap = this.getCityMap();
		int waybillTotalCount = 0;
		int itemTotalCount = 0;
		BigDecimal feeTotalNum = new BigDecimal(0);
		List<CwbOrderForCombineQuery> combineQueryCwbOrderList = new ArrayList<CwbOrderForCombineQuery>();
		for (CwbOrder cwbOrder : cwbOrderList) {
			// 统计信息
			waybillTotalCount++;
			itemTotalCount += cwbOrder.getSendcarnum();
			feeTotalNum = feeTotalNum.add(cwbOrder.getTotalfee());
			// 表体信息
			CwbOrderForCombineQuery combineQueryCwbOrder = new CwbOrderForCombineQuery();

			combineQueryCwbOrder.setCwb(cwbOrder.getCwb());
			combineQueryCwbOrder.setInstationdatetime(cwbOrder.getInstationdatetime());
			combineQueryCwbOrder.setSendcarnum(cwbOrder.getSendcarnum());
			combineQueryCwbOrder.setCollectorname(cwbOrder.getCollectorname());
			combineQueryCwbOrder.setPayMethodName(ExpressSettleWayEnum.getByValue(cwbOrder.getPaymethod()).getText());
			combineQueryCwbOrder.setTotalfee(cwbOrder.getTotalfee());
			combineQueryCwbOrder.setShouldfare(cwbOrder.getShouldfare());
			combineQueryCwbOrder.setPackagefee(cwbOrder.getPackagefee());
			combineQueryCwbOrder.setInsuredfee(cwbOrder.getInsuredfee());

			// 寄件
			combineQueryCwbOrder.setSendername(cwbOrder.getSendername());
			this.setSenderCompany(cwbOrder, combineQueryCwbOrder);
			combineQueryCwbOrder.setSenderprovince(cwbOrder.getSenderprovince());
			combineQueryCwbOrder.setSendercity(cwbOrder.getSendercity());
			combineQueryCwbOrder.setSendercellphone(cwbOrder.getSendercellphone());
			combineQueryCwbOrder.setSendertelephone(cwbOrder.getSendertelephone());

			// 收件
			combineQueryCwbOrder.setConsigneename(cwbOrder.getConsigneename());
			this.setReceiverCompany(cwbOrder, combineQueryCwbOrder);
			combineQueryCwbOrder.setReceiveProvinceName(provinceMap.get(cwbOrder.getRecprovinceid()));
			combineQueryCwbOrder.setReceiveCityName(cityMap.get(cwbOrder.getReccityid()));
			combineQueryCwbOrder.setConsigneemobile(cwbOrder.getConsigneemobile());
			combineQueryCwbOrder.setConsigneephone(cwbOrder.getConsigneephone());

			combineQueryCwbOrder.setEntrustname(cwbOrder.getEntrustname());

			combineQueryCwbOrderList.add(combineQueryCwbOrder);

		}
		combineQueryView.setWaybillTotalCount(waybillTotalCount);
		combineQueryView.setItemTotalCount(itemTotalCount);
		combineQueryView.setFeeTotalNum(feeTotalNum);
		combineQueryView.setCwbOrderList(combineQueryCwbOrderList);

		return combineQueryView;
	}

	private void setSenderCompany(CwbOrder cwbOrder, CwbOrderForCombineQuery combineQueryCwbOrder) {
		String senderCompanyName = "";
		long senderCustomerid = cwbOrder.getCustomerid();

		Customer senderCustomer = this.customerDAO.getCustomerById(senderCustomerid);// 一个包里面的运单数量有限，性能影响不大
		if (senderCustomer != null) {
			senderCompanyName = senderCustomer.getCompanyname();
		}
		combineQueryCwbOrder.setSenderCompanyName(senderCompanyName);
	}

	private void setReceiverCompany(CwbOrder cwbOrder, CwbOrderForCombineQuery combineQueryCwbOrder) {
		String receiverCompanyName = "";
		Integer reccustomerid = cwbOrder.getReccustomerid();
		if (reccustomerid != null) {
			Customer receiverCustomer = this.customerDAO.getCustomerById(reccustomerid);// 一个包里面的运单数量有限，性能影响不大
			if (receiverCustomer != null) {
				receiverCompanyName = receiverCustomer.getCompanyname();
			}
		}
		combineQueryCwbOrder.setReceiverCompany(receiverCompanyName);
	}

}
