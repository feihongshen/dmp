package cn.explink.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.constant.Constants;
import cn.explink.dao.CwbOrderTailDao;
import cn.explink.dao.DeliveryRateConditionDAO;
import cn.explink.dao.DownloadManagerDAO;
import cn.explink.dao.DownloadResultDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryRateCondition;
import cn.explink.domain.DownloadResult;
import cn.explink.enumutil.DownloadState;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JsonUtil;
import cn.explink.vo.delivery.CustomizedDeliveryDateType;
import cn.explink.vo.delivery.DeliveryRate;
import cn.explink.vo.delivery.DeliveryRateAggregation;
import cn.explink.vo.delivery.DeliveryRateBranchOrCustomerAggregation;
import cn.explink.vo.delivery.DeliveryRateDateAggregation;
import cn.explink.vo.delivery.DeliveryRateQueryType;
import cn.explink.vo.delivery.DeliveryRateRequest;
import cn.explink.vo.delivery.DeliveryRateResult;
import cn.explink.vo.delivery.DeliveryRateTimeType;

@Service
public class DeliveryRateService {

	private static Logger logger = LoggerFactory.getLogger(DeliveryRateService.class);

	@Autowired
	private DownloadManagerDAO downloadManagerDAO;

	@Autowired
	private GetDmpDAO getDmpDAO;

	@Autowired
	private DownloadResultDAO downloadResultDAO;

	@Autowired
	private DeliveryRateConditionDAO deliveryRateConditionDAO;

	@Autowired
	private CwbOrderTailDao orderTailDao;

	@Autowired
	private ScheduledTaskService scheduledTaskService;

	public DeliveryRateAggregation getDeliveryRateResult(Integer downloadRequestId) {
		DeliveryRateAggregation drAgg = null;
		DownloadResult downloadResult = downloadResultDAO.getDownloadResultByDownloadRequestId(downloadRequestId);
		try {
			drAgg = JsonUtil.readValue(downloadResult.getResult(), DeliveryRateAggregation.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drAgg;
	}

	public SXSSFWorkbook download(long userId, Integer downloadRequestId, Integer tabId) {
		DeliveryRateAggregation drAgg = getDeliveryRateResult(downloadRequestId);
		Map<Integer, String> bocNameMap = new HashMap<Integer, String>();
		if (DeliveryRateQueryType.byBranch == drAgg.getQueryType() || DeliveryRateQueryType.byUser == drAgg.getQueryType()) {
			// 所有站点
			List<Branch> branchList = getDmpDAO.getBranchByAllZhanDian();
			for (Branch branch : branchList) {
				bocNameMap.put((int) branch.getBranchid(), branch.getBranchname());
			}
		} else {
			// 所有供应商
			List<Customer> customerList = getDmpDAO.getAllCustomers();
			for (Customer customer : customerList) {
				bocNameMap.put((int) customer.getCustomerid(), customer.getCustomername());
			}
		}

		downloadManagerDAO.updateState(downloadRequestId, DownloadState.completed.getValue());

		SXSSFWorkbook wb = createWorkbook(drAgg, bocNameMap, tabId);
		return wb;
	}

	public SXSSFWorkbook createWorkbook(DeliveryRateAggregation drAgg, Map<Integer, String> bocNameMap, Integer tabId) {
		DeliveryRateQueryType queryType = drAgg.getQueryType();
		List<String> allDate = drAgg.getAllDate();
		List<DeliveryRateTimeType> timeTypes = drAgg.getTimeTypes();

		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet();
		wb.setSheetName(0, queryType.getDesc());

		sheet.setColumnWidth(0, 6 * 2 * 256);
		sheet.setColumnWidth(1, 8 * 2 * 256);
		for (int i = 0; i <= (allDate.size() + 1) * 3; i++) {
			sheet.setColumnWidth(i + 2, 6 * 2 * 256);
			sheet.setColumnWidth(i + 3, 8 * 2 * 256);
			sheet.setColumnWidth(i + 4, 7 * 2 * 256);
		}

		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 判断是否是站点与供货商双维度
		Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> bocAggMapMapFlag = drAgg.getBranchOrCustomerAggMap();
		Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMapFlag = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();
		List<Integer> cusidstmp = new ArrayList<Integer>();
		for (Integer firstInt : bocAggMapMapFlag.keySet()) {
			bocAggMapFlag = bocAggMapMapFlag.get(firstInt);
			break;
		}

		for (Integer customer : bocAggMapFlag.keySet()) {
			cusidstmp.add(customer);
		}
		int rowNum = 2;
		if (cusidstmp.size() > 1) {
			rowNum = 3;
		} else {
			rowNum = 2;
		}
		// 填充标题行
		fillTitle(drAgg, queryType, allDate, sheet, style, rowNum);

		Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> bocAggMapMap = drAgg.getBranchOrCustomerAggMap();
		for (Integer bocMap : bocAggMapMap.keySet()) {
			Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap = drAgg.getBranchOrCustomerAggMap().get(bocMap);
			fillBoc(bocAggMap, allDate, timeTypes, sheet, rowNum, bocNameMap.get(bocMap), style, tabId, cusidstmp);
			rowNum += timeTypes.size();
		}

		// total行
		Map<Integer, DeliveryRateBranchOrCustomerAggregation> totalMap = drAgg.getTotal();
		fillBoc(totalMap, allDate, timeTypes, sheet, rowNum, "整体", style, tabId, cusidstmp);
		return wb;
	}

	private void fillBoc(Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap, List<String> allDate, List<DeliveryRateTimeType> timeTypes, Sheet sheet, int rowNum, String bocName,
			CellStyle style, Integer tabId, List<Integer> totalcustomerid) {
		Cell cell = null;
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + timeTypes.size() - 1, 0, 0));
		Row[] rows = new Row[timeTypes.size()];
		// 前2列信息
		for (int i = 0; i < timeTypes.size(); i++) {
			DeliveryRateTimeType timeType = timeTypes.get(i);
			if (rows[i] == null) {
				rows[i] = sheet.createRow(rowNum + i);
			}

			if (i == 0) {
				cell = createCell(rows[i], 0, style);
				cell.setCellValue(bocName);
			}

			cell = createCell(rows[i], 1, style);
			cell.setCellValue(timeType.getDesc());
		}
		// 第3列
		// List<Integer> cusidstmp = new ArrayList<Integer>();
		// for(Integer customer:bocAggMap.keySet()){
		// cusidstmp.add(customer);
		// }
		int cusidstmpIndex = 0;
		int columnStart = 0;
		int totalColumnStart = 0;
		for (Integer customerId : totalcustomerid) {
			DeliveryRateBranchOrCustomerAggregation bocAgg = bocAggMap.get(customerId);
			columnStart = cusidstmpIndex * ((allDate.size() + 1) * 3);
			for (int i = 0; i < timeTypes.size(); i++) {
				for (int j = 0; j < allDate.size(); j++) {
					String date = allDate.get(j);
					if (bocAgg != null) {
						Map<String, DeliveryRateDateAggregation> dateAggMap = bocAgg.getDateAggMap();
						DeliveryRateDateAggregation dateAgg = dateAggMap.get(date);

						// 每个日期只创建一次
						if (i == 0) {
							sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + timeTypes.size() - 1, columnStart + 3 * j + 2, columnStart + 3 * j + 2));
							Cell cell1 = createCell(rows[i], columnStart + 3 * j + 2, style);
							if (dateAgg == null) {
								cell1.setCellValue("0");
							} else {
								cell1.setCellValue(String.valueOf(dateAgg.getTotal()));
							}
						}

						Cell cell2 = createCell(rows[i], columnStart + 3 * j + 3, style);
						Cell cell3 = createCell(rows[i], columnStart + 3 * j + 4, style);
						Map<DeliveryRateTimeType, DeliveryRate> timeTypeAggMap = dateAgg == null ? null : dateAgg.getTimeTypeMap();
						DeliveryRate timeTypeAgg = timeTypeAggMap == null ? null : timeTypeAggMap.get(timeTypes.get(i));
						if (timeTypeAgg == null) {
							cell2.setCellValue("0");
							cell3.setCellValue("0.0%");
						} else {
							cell2.setCellValue(String.valueOf(timeTypeAgg.getCount()));
							cell3.setCellValue(String.valueOf(timeTypeAgg.getRate()));
						}
					} else {
						if (i == 0) {
							sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + timeTypes.size() - 1, columnStart + 3 * j + 2, columnStart + 3 * j + 2));
							Cell cell1 = createCell(rows[i], columnStart + 3 * j + 2, style);

							cell1.setCellValue("0");

						}

						Cell cell2 = createCell(rows[i], columnStart + 3 * j + 3, style);
						Cell cell3 = createCell(rows[i], columnStart + 3 * j + 4, style);

						cell2.setCellValue("0");
						cell3.setCellValue("0.0%");

					}
				}
			}
			if (bocAgg != null) {
				DeliveryRateDateAggregation dateAgg = bocAgg.getTotal();
				totalColumnStart = (cusidstmpIndex + 1) * allDate.size() * 3 + cusidstmpIndex * 3;
				for (int i = 0; i < timeTypes.size(); i++) {
					// 每个日期只创建一次
					if (i == 0) {
						sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + timeTypes.size() - 1, totalColumnStart + 2, totalColumnStart + 2));
						Cell cell1 = createCell(rows[i], columnStart + 3 * allDate.size() + 2, style);
						if (dateAgg == null) {
							cell1.setCellValue("0");
						} else {
							cell1.setCellValue(String.valueOf(dateAgg.getTotal()));
						}
					}

					Cell cell2 = createCell(rows[i], totalColumnStart + 3, style);
					Cell cell3 = createCell(rows[i], totalColumnStart + 4, style);
					Map<DeliveryRateTimeType, DeliveryRate> timeTypeAggMap = dateAgg == null ? null : dateAgg.getTimeTypeMap();
					DeliveryRate timeTypeAgg = timeTypeAggMap == null ? null : timeTypeAggMap.get(timeTypes.get(i));
					if (timeTypeAgg == null) {
						cell2.setCellValue("0");
						cell3.setCellValue("0.0%");
					} else {
						cell2.setCellValue(String.valueOf(timeTypeAgg.getCount()));
						cell3.setCellValue(String.valueOf(timeTypeAgg.getRate()));
					}
				}
				cusidstmpIndex = cusidstmpIndex + 1;
			} else {
				totalColumnStart = (cusidstmpIndex + 1) * allDate.size() * 3 + cusidstmpIndex * 3;
				for (int i = 0; i < timeTypes.size(); i++) {
					// 每个日期只创建一次
					if (i == 0) {
						sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + timeTypes.size() - 1, totalColumnStart + 2, totalColumnStart + 2));
						Cell cell1 = createCell(rows[i], columnStart + 3 * allDate.size() + 2, style);

						cell1.setCellValue("0");

					}

					Cell cell2 = createCell(rows[i], totalColumnStart + 3, style);
					Cell cell3 = createCell(rows[i], totalColumnStart + 4, style);

					cell2.setCellValue("0");
					cell3.setCellValue("0.0%");
				}
				cusidstmpIndex = cusidstmpIndex + 1;
			}
		}

	}

	private Cell createCell(Row row, int column, CellStyle style) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(style);
		return cell;
	}

	private void fillTitle(DeliveryRateAggregation drAgg, DeliveryRateQueryType queryType, List<String> allDate, Sheet sheet, CellStyle style, int tabId) {
		if (tabId == 3) {
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
		} else {
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		}

		Boolean customization = drAgg.getCustomization();
		Row row = sheet.createRow(0);
		Cell cell = createCell(row, 0, style);
		/* modify by wangych 多维度妥投率 */
		Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> bocAggMapMap = drAgg.getBranchOrCustomerAggMap();
		Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();
		List<Integer> cusidstmp = new ArrayList<Integer>();
		Map<Integer, String> bocNameMap = new HashMap<Integer, String>();
		List<Customer> customerList = getDmpDAO.getAllCustomers();
		for (Customer customer : customerList) {
			bocNameMap.put((int) customer.getCustomerid(), customer.getCustomername());
		}
		for (Integer firstInt : bocAggMapMap.keySet()) {
			bocAggMap = bocAggMapMap.get(firstInt);
			break;
		}

		for (Integer customer : bocAggMap.keySet()) {
			cusidstmp.add(customer);
		}

		if (tabId == 3) {
			int index = 0;
			int columnStartTitle = 0;
			int columnendTitle = 0;
			for (Integer customerId : cusidstmp) {
				columnStartTitle = index * (allDate.size() + 1) * 3;
				columnendTitle = (index + 1) * (allDate.size() + 1) * 3;
				cell = createCell(row, columnStartTitle + 2, style);
				cell.setCellValue(bocNameMap.get(customerId));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnStartTitle + 2, columnendTitle + 1));
				index = index + 1;
			}
		}
		if (tabId == 3) {
			row = sheet.createRow(1);
		} else {
			row = sheet.createRow(0);
		}

		cell = createCell(row, 0, style);
		cell.setCellValue(queryType.getDesc());

		cell = createCell(row, 1, style);

		if (customization != null && customization) {
			cell.setCellValue("统计日期");
		} else {
			cell.setCellValue(queryType.getTitle());
		}

		int cusidstmpIndex = 0;
		int columnStart = 0;
		for (Integer customerId : cusidstmp) {
			columnStart = cusidstmpIndex * ((allDate.size() + 1) * 3);
			for (int i = 0; i < allDate.size(); i++) {
				String date = allDate.get(i);
				cell = createCell(row, i * 3 + 2 + columnStart, style);
				cell.setCellValue(date);
				if (tabId == 3) {
					sheet.addMergedRegion(new CellRangeAddress(1, 1, i * 3 + 2 + columnStart, i * 3 + 4 + columnStart));
				} else {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, i * 3 + 2 + columnStart, i * 3 + 4 + columnStart));
				}
			}
			cell = createCell(row, allDate.size() * 3 + 2 + columnStart, style);
			cell.setCellValue("整体");
			if (tabId == 3) {
				sheet.addMergedRegion(new CellRangeAddress(1, 1, allDate.size() * 3 + 2 + columnStart, allDate.size() * 3 + 4 + columnStart));
			} else {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, allDate.size() * 3 + 2 + columnStart, allDate.size() * 3 + 4 + columnStart));
			}
			cusidstmpIndex = cusidstmpIndex + 1;
		}
		// 第2行
		if (tabId == 3) {
			row = sheet.createRow(2);
		} else {
			row = sheet.createRow(1);
		}
		cell = createCell(row, 1, style);
		cell.setCellValue("时效");
		cusidstmpIndex = 0;
		columnStart = 0;
		for (Integer customerId : cusidstmp) {
			columnStart = cusidstmpIndex * ((allDate.size() + 1) * 3);
			for (int i = 0; i < allDate.size(); i++) {
				cell = createCell(row, i * 3 + 2 + columnStart, style);
				if (customization != null && customization) {
					cell.setCellValue("统计单量");
				} else {
					cell.setCellValue(queryType.getTitle2());
				}

				cell = createCell(row, i * 3 + 3 + columnStart, style);
				if (customization != null && customization) {
					cell.setCellValue("妥投单量");
				} else {
					cell.setCellValue("时效内妥投量");
				}

				cell = createCell(row, i * 3 + 4 + columnStart, style);
				if (customization != null && customization) {
					cell.setCellValue("妥投率");
				} else {
					cell.setCellValue("时效内妥投率");
				}
			}
			cell = createCell(row, allDate.size() * 3 + 2 + columnStart, style);
			if (customization != null && customization) {
				cell.setCellValue("合计单量");
			} else {
				cell.setCellValue(queryType.getTitle2());
			}

			cell = createCell(row, allDate.size() * 3 + 3 + columnStart, style);
			if (customization != null && customization) {
				cell.setCellValue("合计妥投单量");
			} else {
				cell.setCellValue("时效内妥投量合计");
			}

			cell = createCell(row, allDate.size() * 3 + 4 + columnStart, style);
			cell.setCellValue("整体妥投率");
			cusidstmpIndex = cusidstmpIndex + 1;
		}
	}

	public List<DeliveryRateCondition> listMobileDeliveryRate(Long userId, String name, Integer selectType) {
		return deliveryRateConditionDAO.listConditionByUser(userId, name, selectType);
	}

	public DeliveryRateCondition getDeliveryRateCondition(Long id) {
		return deliveryRateConditionDAO.get(id);
	}

	public void saveDeliveryRateCondition(DeliveryRateCondition deliveryRateCondition) {
		deliveryRateConditionDAO.save(deliveryRateCondition);
	}

	public String getMobileDeliveryRate(Long ruleId) throws JsonGenerationException, JsonMappingException, ParseException, IOException {
		DeliveryRateCondition deliveryRateCondition = deliveryRateConditionDAO.get(ruleId);
		DeliveryRateRequest deliveryRateRequest = JsonUtil.readValue(deliveryRateCondition.getDeliveryRateRequest(), DeliveryRateRequest.class);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		cal.add(Calendar.DATE, -3);
		Date startDate = cal.getTime();

		deliveryRateRequest.setStartDate(startDate);
		deliveryRateRequest.setEndDate(endDate);
		String json = queryDeliveryRate(deliveryRateRequest);
		return json;
	}

	public String queryDeliveryRate(DeliveryRateRequest deliveryRateRequest) throws ParseException, IOException, JsonGenerationException, JsonMappingException {
		List<DeliveryRateResult> resultList = null;
		if (deliveryRateRequest.getCustomization() != null && deliveryRateRequest.getCustomization()) {
			DeliveryRateTimeType timeType = deliveryRateRequest.getTimeTypes().get(0);
			CustomizedDeliveryDateType cdDateType = timeType.getCdDateType();
			resultList = new ArrayList<DeliveryRateResult>();
			boolean isNextDay = deliveryRateRequest.getStartTime().compareTo(deliveryRateRequest.getEndTime()) >= 0;
			Date currentDate = deliveryRateRequest.getStartDate();
			Date endDate = deliveryRateRequest.getEndDate();
			while (DateTimeUtil.isBefore(currentDate, endDate)) {
				currentDate = DateTimeUtil.parse(DateTimeUtil.formatDate(currentDate, "yyyy-MM-dd " + deliveryRateRequest.getStartTime()), "yyyy-MM-dd HH:mm:ss");
				Date currentEndDate = currentDate;
				if (isNextDay) {
					currentEndDate = DateTimeUtil.nextDate(currentDate);
				}
				currentEndDate = DateTimeUtil.parse(DateTimeUtil.formatDate(currentEndDate, "yyyy-MM-dd " + deliveryRateRequest.getEndTime()), "yyyy-MM-dd HH:mm:ss");
				Date thresholdDate = DateTimeUtil.parse(DateTimeUtil.formatDate(DateTimeUtil.after(currentDate, cdDateType.getDay()), "yyyy-MM-dd " + timeType.getCdTime()), "yyyy-MM-dd HH:mm:ss");
				resultList.addAll(orderTailDao.queryDeliveryRate(deliveryRateRequest, currentDate, currentEndDate, thresholdDate));
				currentDate = DateTimeUtil.nextDate(currentDate);
			}
		} else {
			resultList = orderTailDao.queryDeliveryRate(deliveryRateRequest);
		}
		DeliveryRateAggregation agg = aggregate(deliveryRateRequest, resultList);
		String json = JsonUtil.translateToJson(agg);
		return json;
	}

	private DeliveryRateAggregation aggregate(DeliveryRateRequest deliveryRateRequest, List<DeliveryRateResult> resultList) {
		DeliveryRateAggregation agg = new DeliveryRateAggregation();
		agg.setQueryType(deliveryRateRequest.getQueryType());
		agg.setComputeType(deliveryRateRequest.getComputeType());
		agg.setTimeTypes(deliveryRateRequest.getTimeTypes());
		agg.setCustomization(deliveryRateRequest.getCustomization());

		// 所有查询日期
		List<String> allDate = new ArrayList<String>();
		Date currentDate = deliveryRateRequest.getStartDate();
		Date endDate = deliveryRateRequest.getEndDate();
		while (DateTimeUtil.isBefore(currentDate, endDate)) {
			allDate.add(DateTimeUtil.formatDate(currentDate, "yyyy/M/d"));
			currentDate = DateTimeUtil.nextDate(currentDate);
		}
		agg.setAllDate(allDate);

		// 所有查询的站点或供货商
		List<Long> bocIdList = null;
		DeliveryRateQueryType queryType = deliveryRateRequest.getQueryType();
		if (DeliveryRateQueryType.byBranch == queryType || DeliveryRateQueryType.byUser == queryType) {
			bocIdList = deliveryRateRequest.getBranchIds();
		} else {
			bocIdList = deliveryRateRequest.getCustomerIds();
		}
		for (Long bocIdLong : bocIdList) {
			Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();
			DeliveryRateBranchOrCustomerAggregation bocAgg = null;
			Integer bocId = bocIdLong.intValue();
			if (deliveryRateRequest.getBranchIds() != null && deliveryRateRequest.getCustomerIds() != null) {
				for (int i = 0; i < deliveryRateRequest.getCustomerIds().size(); i++) {
					bocAgg = new DeliveryRateBranchOrCustomerAggregation();
					bocAgg.setBranchOrCustomerId(bocId);
					bocAgg.setCustomerId(deliveryRateRequest.getCustomerIds().get(i).intValue());
					bocAggMap.put(deliveryRateRequest.getCustomerIds().get(i).intValue(), bocAgg);
				}
			} else {
				bocAgg = new DeliveryRateBranchOrCustomerAggregation();
				bocAgg.setBranchOrCustomerId(bocId);
				bocAgg.setCustomerId(0);
				bocAggMap.put(0, bocAgg);
			}

			agg.getBranchOrCustomerAggMap().put(bocId, bocAggMap);
		}

		List<DeliveryRateTimeType> timeTypes = deliveryRateRequest.getTimeTypes();

		Map<DeliveryRateTimeType, Boolean> timeTypeHightlightMap = new HashMap<DeliveryRateTimeType, Boolean>();
		Map<String, Boolean> dateHightlightMap = new HashMap<String, Boolean>();
		for (DeliveryRateResult result : resultList) {
			int branchOrCustomerId = result.getBranchOrCustomer();
			int customerId = result.getCustomer();
			String date = DateTimeUtil.formatDate(result.getDate(), "yyyy/M/d");
			Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap = agg.getBranchOrCustomerAggMap().get(branchOrCustomerId);
			if (bocAggMap == null) {
				bocAggMap = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();
				DeliveryRateBranchOrCustomerAggregation bocAgg = null;
				if (deliveryRateRequest.getBranchIds() != null && deliveryRateRequest.getCustomerIds() != null) {
					for (int i = 0; i < deliveryRateRequest.getCustomerIds().size(); i++) {
						bocAgg = new DeliveryRateBranchOrCustomerAggregation();
						bocAgg.setBranchOrCustomerId(branchOrCustomerId);
						bocAgg.setCustomerId(customerId);
						bocAggMap.put(customerId, bocAgg);
					}
				} else {
					bocAgg = new DeliveryRateBranchOrCustomerAggregation();
					bocAgg.setBranchOrCustomerId(branchOrCustomerId);
					bocAggMap.put(0, bocAgg);
				}
				agg.getBranchOrCustomerAggMap().put(branchOrCustomerId, bocAggMap);
			}
			DeliveryRateDateAggregation dateAgg = null;
			for (Integer bocAggKey : bocAggMap.keySet()) {
				DeliveryRateBranchOrCustomerAggregation bocAgg = bocAggMap.get(bocAggKey);
				if (bocAggKey == result.getCustomer()) {
					dateAgg = bocAggMap.get(bocAggKey).getDateAggMap().get(result.getDate());
					if (dateAgg == null) {
						dateAgg = new DeliveryRateDateAggregation();
						dateAgg.setDate(result.getDate());
						dateAgg.setCustomerid(result.getCustomer());
						bocAgg.getDateAggMap().put(date, dateAgg);
					}

					Integer total = result.getTotal();
					dateAgg.setTotal(total);
					for (int i = 0; i < timeTypes.size(); i++) {
						DeliveryRateTimeType timeType = timeTypes.get(i);
						Integer count = result.getDeliveryCount() == null ? null : result.getDeliveryCount().get(i);

						DeliveryRate deliveryRate = new DeliveryRate();
						deliveryRate.setCount(count);
						deliveryRate.setRate(divide(count, total));
						boolean hightlight = calculateHighlight(result.getDate(), timeType);
						deliveryRate.setHighlight(hightlight);
						timeTypeHightlightMap.put(timeType, hightlight);
						dateHightlightMap.put(date, hightlight);

						dateAgg.getTimeTypeMap().put(timeType, deliveryRate);
					}
				}
			}
		}

		// 站点或供应商级别汇总
		Map<Integer, DeliveryRateBranchOrCustomerAggregation> totalBocAggMap = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();
		agg.setTotal(totalBocAggMap);
		List<Integer> cusidstmp = new ArrayList<Integer>();
		for (DeliveryRateResult customer : resultList) {
			cusidstmp.add(customer.getCustomer());
		}
		List<Integer> cusidst = getNewList(cusidstmp);
		// 站点或供应商级别Map
		Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> bocAggMapFirst = agg.getBranchOrCustomerAggMap();

		for (Integer cusid : cusidst) {
			DeliveryRateBranchOrCustomerAggregation totalBocAgg = new DeliveryRateBranchOrCustomerAggregation();
			// 右下角汇总
			DeliveryRateDateAggregation totalTotalDateAgg = totalBocAgg.getTotal();
			Map<DeliveryRateTimeType, DeliveryRate> totalTotalTimeTypeAggMap = totalTotalDateAgg.getTimeTypeMap();
			totalBocAggMap.put(cusid.intValue(), totalBocAgg);

			for (Integer boc : bocAggMapFirst.keySet()) {
				// 站点或供应商级别
				Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMapSecond = bocAggMapFirst.get(boc);
				// 日期级别Map
				for (Integer cus : bocAggMapSecond.keySet()) {

					Map<String, DeliveryRateDateAggregation> dateAggMap = bocAggMapSecond.get(cus).getDateAggMap();
					// 日期级别横向汇总
					DeliveryRateDateAggregation totalDateLineAgg = new DeliveryRateDateAggregation();
					bocAggMapSecond.get(cus).setTotal(totalDateLineAgg);
					// 日期级别纵向汇总Map

					Map<String, DeliveryRateDateAggregation> totalDateColumnAggMap = totalBocAgg.getDateAggMap();
					for (String date : dateAggMap.keySet()) {

						// 日期级别
						DeliveryRateDateAggregation dateAgg = dateAggMap.get(date);
						// 日期级别横向汇总
						totalDateLineAgg.setTotal(add(totalDateLineAgg.getTotal(), dateAgg.getTotal()));
						// 日期级别纵向汇总
						DeliveryRateDateAggregation totalDateColumnAgg = totalDateColumnAggMap.get(date);

						if (totalDateColumnAgg == null) {
							totalDateColumnAgg = new DeliveryRateDateAggregation();
							totalDateColumnAgg.setCustomerid(cusid.intValue());
							totalDateColumnAggMap.put(date, totalDateColumnAgg);
						}
						if (cus == cusid.intValue()) {

							totalDateColumnAgg.setTotal(add(totalDateColumnAgg.getTotal(), dateAgg.getTotal()));
							// 右下角汇总
							totalTotalDateAgg.setTotal(add(totalTotalDateAgg.getTotal(), dateAgg.getTotal()));
						}

						// 时效级别Map
						Map<DeliveryRateTimeType, DeliveryRate> timeTypeMap = dateAgg.getTimeTypeMap();
						// 横向时效级别Map
						Map<DeliveryRateTimeType, DeliveryRate> totalTimeTypeLineMap = totalDateLineAgg.getTimeTypeMap();
						// 纵向时效级别Map
						Map<DeliveryRateTimeType, DeliveryRate> totalTimeTypeColumnMap = totalDateColumnAgg.getTimeTypeMap();
						for (DeliveryRateTimeType timeType : timeTypeMap.keySet()) {
							// 时效级别
							DeliveryRate deliveryRate = timeTypeMap.get(timeType);
							// 时效级别横向汇总
							DeliveryRate totalDeliveryRateLine = totalTimeTypeLineMap.get(timeType);
							if (totalDeliveryRateLine == null) {
								totalDeliveryRateLine = new DeliveryRate();
								totalTimeTypeLineMap.put(timeType, totalDeliveryRateLine);
							}
							totalDeliveryRateLine.setCount(add(totalDeliveryRateLine.getCount(), deliveryRate.getCount()));

							// 时效级别纵向汇总
							if (cus == cusid.intValue()) {
								DeliveryRate totalDeliveryRateColumn = totalTimeTypeColumnMap.get(timeType);
								if (totalDeliveryRateColumn == null) {
									totalDeliveryRateColumn = new DeliveryRate();
									totalTimeTypeColumnMap.put(timeType, totalDeliveryRateColumn);
								}
								totalDeliveryRateColumn.setCount(add(totalDeliveryRateColumn.getCount(), deliveryRate.getCount()));

								DeliveryRate totalTotalDeliveryRate = totalTotalTimeTypeAggMap.get(timeType);
								if (totalTotalDeliveryRate == null) {
									totalTotalDeliveryRate = new DeliveryRate();
									totalTotalTimeTypeAggMap.put(timeType, totalTotalDeliveryRate);
								}
								totalTotalDeliveryRate.setCount(add(totalTotalDeliveryRate.getCount(), deliveryRate.getCount()));
							}
						}

						// 横向汇总完毕，计算妥投率
						for (DeliveryRateTimeType timeType : totalTimeTypeLineMap.keySet()) {
							DeliveryRate totalDeliveryRate = totalTimeTypeLineMap.get(timeType);
							totalDeliveryRate.setRate(divide(totalDeliveryRate.getCount(), totalDateLineAgg.getTotal()));

							Boolean highlight = timeTypeHightlightMap.get(timeType);
							if (highlight != null && highlight) {
								totalDeliveryRate.setHighlight(highlight);
							}
						}
					}
				}
			}

			// 日期级别纵向汇总Map
			Map<String, DeliveryRateDateAggregation> totalDateColumnAggMap = totalBocAgg.getDateAggMap();
			for (String date : totalDateColumnAggMap.keySet()) {
				// 日期级别纵向汇总
				DeliveryRateDateAggregation dateAgg = totalDateColumnAggMap.get(date);
				Map<DeliveryRateTimeType, DeliveryRate> timeTypeMap = dateAgg.getTimeTypeMap();
				for (DeliveryRateTimeType timeType : timeTypeMap.keySet()) {
					DeliveryRate deliveryRate = timeTypeMap.get(timeType);
					deliveryRate.setRate(divide(deliveryRate.getCount(), dateAgg.getTotal()));

					// 暂不高亮汇总
					// Boolean highlight = timeTypeHightlightMap.get(timeType);
					// if (highlight != null && highlight) {
					// deliveryRate.setHighlight(highlight);
					// }
				}
			}

			for (DeliveryRateTimeType timeType : totalTotalTimeTypeAggMap.keySet()) {
				DeliveryRate deliveryRate = totalTotalTimeTypeAggMap.get(timeType);
				deliveryRate.setRate(divide(deliveryRate.getCount(), totalTotalDateAgg.getTotal()));

				// 暂不高亮汇总
				// Boolean highlight = timeTypeHightlightMap.get(timeType);
				// if (highlight != null && highlight) {
				// deliveryRate.setHighlight(highlight);
				// }
			}
		}
		return agg;
	}

	private boolean calculateHighlight(Date date, DeliveryRateTimeType timeType) {
		Date thresholdDate = null;
		if (timeType.isCustomization() != null && timeType.isCustomization()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, timeType.getCdDateType().getDay());
			try {
				String thresholdDateString = DateTimeUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
				thresholdDate = DateTimeUtil.parse(thresholdDateString + " " + timeType.getCdTime(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			if (timeType == DeliveryRateTimeType.all) {
				return true;
			}
			Date nextDate = DateTimeUtil.nextDate(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(nextDate);
			cal.add(Calendar.HOUR, timeType.getValue());
			thresholdDate = cal.getTime();
		}
		Date now = new Date();
		return DateTimeUtil.isBefore(now, thresholdDate);
	}

	/**
	 * 返回保留1位小数的百分比
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	private String divide(Integer one, Integer two) {
		double oneValue = one == null ? 0 : one.doubleValue();
		int twoValue = two == null ? 0 : two.intValue();
		double result = twoValue == 0 ? 0 : oneValue / twoValue;
		return String.format("%.1f", result * 100) + "%";
	}

	private int add(Integer one, Integer two) {
		int oneValue = one == null ? 0 : one.intValue();
		int twoValue = two == null ? 0 : two.intValue();
		return oneValue + twoValue;
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		cal.add(Calendar.DATE, -3);
		Date startDate = cal.getTime();

		System.out.println(DateTimeUtil.formatDate(startDate, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateTimeUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss"));
	}

	public void deactiveDeliveryRateCondition(Long id) {
		deliveryRateConditionDAO.updateStatus(id, Constants.DELIVERY_RATE_CONDITION_STATUS_INVALID);
	}

	public void activeDeliveryRateCondition(Long id) {
		deliveryRateConditionDAO.updateStatus(id, Constants.DELIVERY_RATE_CONDITION_STATUS_VALID);
	}

	// 去掉LIST中相同值
	public List<Integer> getNewList(List<Integer> li) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < li.size(); i++) {
			Integer str = li.get(i); // 获取传入集合对象的每一个元素
			if (!list.contains(str)) { // 查看新集合中是否有指定的元素，如果没有则加入
				list.add(str);
			}
		}
		return list; // 返回集合
	}
}
