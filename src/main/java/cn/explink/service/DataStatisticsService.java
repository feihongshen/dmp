package cn.explink.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.controller.CwbOrderView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.PayWay;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;


@Service
public class DataStatisticsService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PayWayDao payWaydao;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private static Logger logger = LoggerFactory.getLogger(DataStatisticsService.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@DataSource(DatabaseType.REPLICA)
	public void DataStatisticsExportExcelMethod(HttpServletResponse response, HttpServletRequest request, long page, long sign) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀
		long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count"));

		if (count > 0) {
			if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
				otherName = "1-" + count;
			} else {
				for (int j = 0; j < ((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)); j++) {
					if (j == 0) {
						otherName = "1-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if ((j != 0) && (j != (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1))) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + "-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if (j == (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1)) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + 1 + "-" + count;
					}
				}
			}
		}
		fileName = fileName + otherName + lastStr;
		try {
			// 查询出数据
			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] startbranchid = request.getParameterValues("startbranchid1") == null ? new String[] {} : request.getParameterValues("startbranchid1");
			String[] currentBranchid = request.getParameterValues("currentBranchid1") == null ? new String[] {} : request.getParameterValues("currentBranchid1");
			String[] dispatchbranchid = request.getParameterValues("dispatchbranchid1") == null ? new String[] {} : request.getParameterValues("dispatchbranchid1");
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			long branchid1 = request.getParameter("branchid1") == null ? -1 : Long.parseLong(request.getParameter("branchid1").toString());
			String type1 = request.getParameter("type1") == null ? "startbranchid" : request.getParameter("type1").toString();
			long flowordertype = request.getParameter("flowordertype1") == null ? -1 : Long.parseLong(request.getParameter("flowordertype1").toString());
			long paywayid = request.getParameter("paytype1") == null ? -1 : Long.parseLong(request.getParameter("paytype1").toString());
			long isaudit = request.getParameter("isaudit1") == null ? -1 : Long.parseLong(request.getParameter("isaudit1").toString());
			long isauditTime = request.getParameter("isauditTime1") == null ? -1 : Long.parseLong(request.getParameter("isauditTime1").toString());
			long deliverid = request.getParameter("deliverid1") == null ? -1 : Long.parseLong(request.getParameter("deliverid1").toString());
			String[] operationOrderResultTypes = request.getParameterValues("operationOrderResultTypes1") == null ? new String[] {} : request.getParameterValues("operationOrderResultTypes1");
			String[] branchid2s = request.getParameterValues("branchid2s") == null ? new String[] {} : request.getParameterValues("branchid2s");
			long isnowdata = request.getParameter("isnowdata") == null ? 0 : Long.parseLong(request.getParameter("isnowdata").toString());
			Integer paybackfeeIsZero = request.getParameter("paybackfeeIsZero1") == null ? -1 : Integer.parseInt(request.getParameter("paybackfeeIsZero1").toString());
			String servicetype = request.getParameter("servicetype1") == null ? "全部" : request.getParameter("servicetype1").toString();
			int firstlevelid = request.getParameter("firstzhiliureasonid") == null ? 0 : Integer.parseInt(request.getParameter("firstzhiliureasonid"));
			
			//Modified by leoliao at 2016-07-29 优化分站到货导出性能
			logger.debug("数据导出：cwbordertypeids={}, nextbranchid={}, begindate={}, enddate={}, customerids={}, startbranchid={}, currentBranchid={}, dispatchbranchid={}, " +
					     "kufangid={}, branchid1={}, type1={}, flowordertype={}, paywayid={}, isaudit={}, isauditTime={}, deliverid={}, operationOrderResultTypes={}, branchid2s={}, " +
					     "isnowdata={}, paybackfeeIsZero={}, servicetype={}, firstlevelid={}", 
					     this.getStrings(cwbordertypeids), this.getStrings(nextbranchid), begindate, enddate, this.getStrings(customerids), this.getStrings(startbranchid),
					     this.getStrings(currentBranchid), this.getStrings(dispatchbranchid), this.getStrings(kufangid), branchid1, type1, flowordertype, paywayid, isaudit,
					     isauditTime, deliverid, this.getStrings(operationOrderResultTypes), this.getStrings(branchid2s), isnowdata, paybackfeeIsZero, servicetype, firstlevelid);

			/*String orderflowcwbs = this.getCwbs(sign, begindate, enddate, isauditTime, nextbranchid, startbranchid, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, flowordertype,
					kufangid, currentBranchid, branchid1, type1, branchid2s, customerids, isnowdata, firstlevelid);*/
			
			String sqlOrderFlowBySome = "";
			String orderflowcwbs      = "";
			if(sign == 8){
				//分站到货
				//sqlOrderFlowBySome = orderFlowDAO.genSqlOrderFlowBySome(begindate, enddate, 
				//												FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), 
				//												this.getStrings(currentBranchid), isnowdata, false);
			}else{
				orderflowcwbs = this.getCwbs(sign, begindate, enddate, isauditTime, nextbranchid, startbranchid, isaudit, 
											 operationOrderResultTypes, dispatchbranchid, deliverid, flowordertype,
											 kufangid, currentBranchid, branchid1, type1, branchid2s, customerids, isnowdata, firstlevelid);
			}
			//Modified end

			if ((sign == 1) || (sign == 2)) {// 滞留、拒收
				begindate = enddate = "";
				dispatchbranchid = new String[] {};
			} else if (sign == 3) {// 退供货商统计
				begindate = enddate = "";
			} else if (sign == 5) {// 妥投
				begindate = enddate = "";
				dispatchbranchid = new String[] {};
				flowordertype = 0l;

			} else if (sign == 6) {// 库房出库
				begindate = enddate = "";
				kufangid = nextbranchid = new String[] {};
			} else if (sign == 8) {// 分站到货
				//begindate = enddate = "";
				//currentBranchid = new String[] {};
				String cid = ((request.getParameter("customerids") == null) || "0".equals(request.getParameter("customerids"))) ? "" : request.getParameter("customerids");
				String[] cs = new String[] { cid };
				customerids = cs;
			} else if (sign == 9) {// 中转订单统计
				begindate = enddate = "";
				startbranchid = nextbranchid = new String[] {};
			} else if (sign == 10) {// 站点出站统计
				begindate = enddate = "";
				startbranchid = nextbranchid = new String[] {};
				flowordertype = 0l;
			} else if (sign == 7) {
				List<Branch> kufangList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
						+ BranchEnum.ZhongZhuan.getValue());
				Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
				if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
					if (kufangList.size() == 0) {
						kufangList.add(branch);
					} else {
						if (!this.checkBranchRepeat(kufangList, branch)) {
							kufangList.add(branch);
						}
					}
				}
				if ((kufangid.length == 0)) {
					kufangid = new String[kufangList.size()];
					int i = 0;
					for (Branch kf : kufangList) {
						kufangid[i] = kf.getBranchid() + "";
						i++;
					}

				}
			}
			
			//Added by leoliao at 2016-05-06  以前的揽收订单(非快递单)，走的是这个库房(0)。[与客户发货统计的条件一致，否则统计与导出数据不一致]
			String strKufangid = this.getStrings(kufangid);
			if(strKufangid != null && strKufangid.length() > 0){
				strKufangid += ",0";
			}
			//Added end
			
			//Modified by leoliao at 2016-07-29 优化分站到货查询
			/*final String sql = this.cwbDAO.getSQLExportHuiZong(page, begindate, enddate, this.getStrings(customerids), this.getStrings(startbranchid), this.getStrings(nextbranchid),
					this.getStrings(cwbordertypeids), orderflowcwbs, this.getStrings(currentBranchid), this.getStrings(dispatchbranchid), strKufangid, flowordertype, paywayid, sign,
					paybackfeeIsZero, servicetype);*/
			
			if(sign == 8){
				//分站到货
				String flowordertypes   = "";
				if (isnowdata > 0) {
					flowordertypes   = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue();
				}
				String sql =this.cwbDAO.getDaoHuoSql(this.getStrings(customerids), this.getStrings(cwbordertypeids), strKufangid, flowordertypes,begindate, enddate, this.getStrings(currentBranchid));
				sql=sql+OrderFlowDAO.LIMIT_FLAG;//page is begin
				
				DataStatisticsExcelUtils excelUtil = new DataStatisticsExcelUtils(DataStatisticsService.this);
				excelUtil.setCloumnName4(cloumnName4);
				excelUtil.setCloumnName5(cloumnName5);
				excelUtil.setCloumnName6(cloumnName6);
				excelUtil.setSql(sql);
				excelUtil.setPage(page);
				excelUtil.setTotal(count);
				
				excelUtil.excel(response, cloumnName4, sheetName, fileName);
			}else{
				final String sql = this.cwbDAO.getSQLExportHuiZong(page, begindate, enddate, this.getStrings(customerids), this.getStrings(startbranchid), this.getStrings(nextbranchid),
														this.getStrings(cwbordertypeids), orderflowcwbs, this.getStrings(currentBranchid), this.getStrings(dispatchbranchid), 
														strKufangid, flowordertype, paywayid, sign, paybackfeeIsZero, servicetype);
				
				
				ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
					@Override
					public void fillData(final Sheet sheet, final CellStyle style) {
						final List<User> uList = DataStatisticsService.this.userDAO.getAllUserByuserDeleteFlag();
						final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
						final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
						final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
						final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
						List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
						final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
						final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();

						DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
							private int count = 0;
							ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
							private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

							public void processRow(ResultSet rs) throws SQLException {
								Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
								this.recordbatch.add(mapRow);
								this.count++;
								if ((this.count % 100) == 0) {
									this.writeBatch();
								}
							}

							private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
									Map<String, String> complaintMap) throws SQLException {
								Row row = sheet.createRow(rownum + 1);
								row.setHeightInPoints(15);
								for (int i = 0; i < cloumnName4.length; i++) {
									Cell cell = row.createCell((short) i);
									cell.setCellStyle(style);
									// sheet.setColumnWidth(i, (short) (5000));
									// //设置列宽
									Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
											reasonList, cwbspayupidMap, complaintMap);
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
								this.writeBatch();
								return null;
							}

							public void writeBatch() throws SQLException {
								if (this.recordbatch.size() > 0) {
									List<String> cwbs = new ArrayList<String>();
									for (Map<String, Object> mapRow : this.recordbatch) {
										cwbs.add(mapRow.get("cwb").toString());
									}
									Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
									Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
									Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
									Map<String, String> complaintMap = this.getComplaintMap(cwbs);
									Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
									int size = this.recordbatch.size();
									for (int i = 0; i < size; i++) {
										String cwb = this.recordbatch.get(i).get("cwb").toString();
										this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
												complaintMap);
									}
									this.recordbatch.clear();
								}
							}

							private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
								Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
								for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
									map.put(tuihuoRecord.getCwb(), tuihuoRecord);
								}
								return map;
							}

							
private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
								Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
								for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
									map.put(deliveryState.getCwb(), deliveryState);
								}
								return map;
							}

							private Map<String, String> getComplaintMap(List<String> cwbs) {
								Map<String, String> complaintMap = new HashMap<String, String>();
								for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
									complaintMap.put(complaint.getCwb(), complaint.getContent());
								}
								return complaintMap;
							}

							private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
								Map<String, String> cwbspayupidMap = new HashMap<String, String>();
								/*
								 * for(DeliveryState deliveryState:deliveryStateDAO.
								 * getActiveDeliveryStateByCwbs(cwbs)){ String
								 * ispayup = "否"; GotoClassAuditing goclass =
								 * gotoClassAuditingDAO
								 * .getGotoClassAuditingByGcaid(deliveryState
								 * .getGcaid());
								 *
								 * if(goclass!=null&&goclass.getPayupid()!=0){
								 * ispayup = "是"; }
								 * cwbspayupidMap.put(deliveryState.getCwb(),
								 * ispayup); }
								 */
								return cwbspayupidMap;
							}
						});

					}
				};
				
				excelUtil.excel(response, cloumnName4, sheetName, fileName);
			}
			//Moidified by leoliao

			String dataJson = this.setDataStatisticsExportExcelJson(begindate, enddate, isauditTime, nextbranchid,
					startbranchid, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, flowordertype,
					kufangid, currentBranchid, branchid1, type1, branchid2s, customerids, cwbordertypeids, paywayid,
					isnowdata, paybackfeeIsZero, servicetype, firstlevelid);
			
			//记录导出文件日志
			this.auditExportExcel(dataJson, fileName, count, this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Map<String, Map<String, String>> getOrderFlowByCredateForDetailAndExportAllTime(List<String> cwbs, List<Branch> branchlist) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			//订单的集合 
			List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwbs(cwbs);
			for (OrderFlow of : ofList) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
					this.getCwbRow(map, of).put("customerbrackhouseremark", of.getComment());
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
					if (this.getsitetype(branchlist, of.getBranchid()) == BranchEnum.ZhongZhuan.getValue()) {
						this.getCwbRow(map, of).put("zhongzhuanzhanIntime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					} else {
						this.getCwbRow(map, of).put("Instoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					}
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
					this.getCwbRow(map, of).put("zhongzhuanzhanIntime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if ((of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())) {
					this.getCwbRow(map, of).put("InSitetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					this.getCwbRow(map, of).put("PickGoodstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					if (this.getsitetype(branchlist, of.getBranchid()) == BranchEnum.ZhongZhuan.getValue()) {
						this.getCwbRow(map, of).put("zhongzhuanzhanOuttime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					} else if (this.getsitetype(branchlist, of.getBranchid()) == BranchEnum.KuFang.getValue()) {
						this.getCwbRow(map, of).put("Outstoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					} else if (this.getsitetype(branchlist, of.getBranchid()) == BranchEnum.ZhanDian.getValue()) {
						this.getCwbRow(map, of).put("OutSitetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					}
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
					this.getCwbRow(map, of).put("zhongzhuanzhanOuttime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					this.getCwbRow(map, of).put("Goclasstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					this.getCwbRow(map, of).put("Gobacktime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
					this.getCwbRow(map, of).put("tuigonghuoshangchukutime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
				if ((this.getCwbRow(map, of).get("Newchangetime") == null)
						|| (of.getCredate().getTime() > new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getCwbRow(map, of).get("Newchangetime")).getTime())) {
					this.getCwbRow(map, of).put("Newchangetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
			}
		} catch (Exception e) {
		}
		return map;
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

	private Map<String, String> getCwbRow(Map<String, Map<String, String>> map, OrderFlow of) {
		if (map.get(of.getCwb()) == null) {
			map.put(of.getCwb(), new HashMap<String, String>());
		}
		Map<String, String> map2 = map.get(of.getCwb());
		return map2;
	}

	/**
	 * 库房在途订单统计导出
	 *
	 * @param response
	 * @param request
	 * @param page
	 */
	@DataSource(DatabaseType.REPLICA)
	public void DataStatisticsZaituExportExcelMethod(HttpServletResponse response, HttpServletRequest request, long page) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀
		long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count"));

		if (count > 0) {
			if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
				otherName = "1-" + count;
			} else {
				for (int j = 0; j < ((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)); j++) {
					if (j == 0) {
						otherName = "1-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if ((j != 0) && (j != (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1))) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + "-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if (j == (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1)) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + 1 + "-" + count;
					}
				}
			}
		}
		fileName = fileName + otherName + lastStr;
		try {
			// 查询出数据
			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			long datetype = request.getParameter("datetype1") == null ? -1 : Long.parseLong(request.getParameter("datetype1").toString());
			String dataJson = this.setDataStatisticsZaituExportExcelJson(cwbordertypeids, nextbranchid, kufangid, begindate, enddate, datetype);

			String sql = "";
			if (datetype == 1) {
				sql = this.cwbDAO.getzaitucwbOrderSQL(datetype, begindate, enddate, this.getStrings(kufangid), this.getStrings(nextbranchid), this.getStrings(cwbordertypeids),
						CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			} else if (datetype == 2) {
				List<String> orderflowlist = this.orderFlowDAO.getZaituCwbsByDateAndFlowtype(begindate, enddate, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), this.getStrings(kufangid));
				String orderflowcwbs = this.getStrings(orderflowlist);
				List<String> cwbStr = this.cwbDAO.getzaitucwborderCwb(this.getStrings(kufangid), this.getStrings(nextbranchid), this.getStrings(cwbordertypeids),
						CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue(), orderflowcwbs);
				if (!cwbStr.isEmpty()) {
					String cwbs = this.getStrings(cwbStr);
					sql = this.cwbDAO.getSqlByCwb(cwbs);
				}
			}
			final String sql2 = sql;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUserByuserDeleteFlag();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();

					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql2), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							List<String> cwbs = new ArrayList<String>();
							for (Map<String, Object> mapRow : this.recordbatch) {
								cwbs.add(mapRow.get("cwb").toString());
							}
							Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
							Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
							Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
							Map<String, String> complaintMap = this.getComplaintMap(cwbs);
							Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
							int size = this.recordbatch.size();
							for (int i = 0; i < size; i++) {
								String cwb = this.recordbatch.get(i).get("cwb").toString();
								this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
										complaintMap);
							}
							this.recordbatch.clear();
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});

				}
			};

			excelUtil.excel(response, cloumnName4, sheetName, fileName);
            //记录导出文件日志
			this.auditExportExcel(dataJson, fileName, count, this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@DataSource(DatabaseType.REPLICA)
	public void DataStatisticsExportOutWareExcelMethod(HttpServletResponse response, HttpServletRequest request, long page, long sign) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀
		long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count"));

		if (count > 0) {
			if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
				otherName = "1-" + count;
			} else {
				for (int j = 0; j < ((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)); j++) {
					if (j == 0) {
						otherName = "1-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if ((j != 0) && (j != (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1))) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + "-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if (j == (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1)) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + 1 + "-" + count;
					}
				}
			}
		}
		fileName = fileName + otherName + lastStr;
		try {
			// 查询出数据
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerid = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String customerids = this.getStrings(customerid);
			String kufangdis = this.getStrings(kufangid);
			String cwbordertypeids = this.getStrings(cwbordertypeid);
			String nextbranchids = this.getStrings(nextbranchid);

			final String sql = this.cwbDAO.getcwbOrderByOutWarehouseSqlNew(page, begindate, enddate, customerids, kufangdis, nextbranchids, cwbordertypeids, 0);
			String dataJson = this.setDataStatisticsExportOutWareExcelJson(cwbordertypeid, nextbranchid, kufangid,
					customerid, begindate, enddate);
			
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUserByuserDeleteFlag();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();
					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});

					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 *
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 *
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 *
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object
					 * a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList,cMap
					 * ,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 *
					 * }});
					 */

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			//记录文件导出日志
			this.auditExportExcel(dataJson, fileName, count, this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@DataSource(DatabaseType.REPLICA)
	public void DataStatisticsExportIntoWareExcelMethod(HttpServletResponse response, HttpServletRequest request, long page) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀
		long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count"));

		if (count > 0) {
			if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
				otherName = "1-" + count;
			} else {
				for (int j = 0; j < ((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)); j++) {
					if (j == 0) {
						otherName = "1-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if ((j != 0) && (j != (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1))) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + "-" + ((j + 1) * Page.EXCEL_PAGE_NUMBER);
					} else if (j == (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) - 1)) {
						otherName = (j * Page.EXCEL_PAGE_NUMBER) + 1 + "-" + count;
					}
				}
			}
		}
		fileName = fileName + otherName + lastStr;
		try {
			// 查询出数据

			// String[] emaildateids =
			// request.getParameterValues("emaildateid1")==null?new
			// String[]{}:request.getParameterValues("emaildateid1");
			long cwbordertypeid = request.getParameter("cwbordertypeid1") == null ? -2 : Long.parseLong(request.getParameter("cwbordertypeid1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String emaildatebegin = request.getParameter("emaildatebegin1") == null ? "" : request.getParameter("emaildatebegin1").toString();
			String emaildateend = request.getParameter("emaildateend1") == null ? "" : request.getParameter("emaildateend1").toString();
			long kufangid = request.getParameter("kufangid1") == null ? -1 : Long.parseLong(request.getParameter("kufangid1"));
			String isruku = request.getParameter("isruku1") == null ? "false" : request.getParameter("isruku1").toString();
			String[] customerids = request.getParameterValues("customerids1") == null ? new String[] {} : request.getParameterValues("customerids1");
			String customers = "";
			if (customerids.length > 0) {
				customers = this.getStrings(customerids);
			}
			String dataJson = this.setDataStatisticsExportIntoWareExcelJson(cwbordertypeid, begindate, enddate,
					emaildatebegin, emaildateend, kufangid, isruku, customerids);
			String sql = "";
			if (isruku.equals("false")) {
				sql = this.cwbDAO.getCwbDetailByParamAndCwbsSql(page, customers, emaildatebegin, emaildateend, cwbordertypeid, kufangid);
			} else {
				sql = this.orderFlowDAO.getCwbDetailAndOrderFlowByParamSQL(page, customers, emaildatebegin, emaildateend, begindate, enddate, FlowOrderTypeEnum.RuKu.getValue(), kufangid,
						cwbordertypeid);
			}
			final String lastsql = sql;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUserByuserDeleteFlag();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();
					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(lastsql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
					/*
					 * jdbcTemplate.query(new
					 * StreamingStatementCreator(lastsql), new
					 * RowCallbackHandler(){ private int count=0;
					 *
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 *
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 *
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object
					 * a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList,cMap
					 * ,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 *
					 * }});
					 */

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			//记录文件导出日志
			this.auditExportExcel(dataJson, fileName, count, this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@DataSource(DatabaseType.REPLICA)
	public void exportExcelByNoresultMethod(HttpServletResponse response, String sql1, Map<String, Object> paramsMAP, String mouldfieldids) {

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀

		fileName = fileName + otherName + lastStr;
		String dataJson = this.setexportExcelByNoresultJson(paramsMAP);
		try {
			final String sql = sql1;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();
					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 *
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 *
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 *
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object
					 * a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList,cMap
					 * ,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 *
					 * }});
					 */

				}
				
				@Override
				public long getRecordCount(){
					try{
						int size = (Integer) DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>(){ 
							@Override
							public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
								int count = 0;
					            while(rs.next()){
					                count = count + 1;
					            }
								return count;
							}
						});
						return size;
					}catch(Exception e){
						logger.info("Not able to fetch excel row count, so use default 0");
						return 0;
					}
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			//记录文件导出日志
			this.auditExportExcel(dataJson, fileName, excelUtil.getRecordCount(), this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@DataSource(DatabaseType.REPLICA)
	public void cwbExportExcelMethod(HttpServletResponse response, HttpServletRequest request, String begindate1, String enddate1, String branchid1, String customerid1, String types,
			long istuihuozhanruku1, long tuihuotype) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2) && !"".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String dataJson = this.setcwbExportExcelJson(begindate1, enddate1, branchid1, customerid1, types, istuihuozhanruku1, tuihuotype);
		try {
			// 查询出数据
			String sql1 = "";
			if (tuihuotype == 1) {// 退货出站
				sql1 = this.tuihuoRecordDAO.getSqlTuihuoRecordBychuzhan(begindate1, enddate1, branchid1, customerid1, types, istuihuozhanruku1);
			} else {// 退货入库
				sql1 = this.tuihuoRecordDAO.getSqlTuihuoRecordByTuihuo(begindate1, enddate1, branchid1, customerid1, types);
			}
			final String sql = sql1;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					// List<Remark> remarkList =
					// remarkDAO.getRemarkByCwbs(exportcwbs);
					final Map<String, Map<String, String>> remarkMap = new HashMap<String, Map<String, String>>();
					/* exportService.getInwarhouseRemarks(remarkList) */
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();
					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 * 
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 * 
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 * 
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 * 
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object
					 * a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList,cMap
					 * ,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 * 
					 * }});
					 */

				}
				
				@Override
				public long getRecordCount(){
					try{
						int size = (Integer) DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>(){ 
							@Override
							public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
								int count = 0;
					            while(rs.next()){
					                count = count + 1;
					            }
								return count;
							}
						});
						return size;
					}catch(Exception e){
						logger.info("Not able to fetch excel row count, so use default 0");
						return 0;
					}
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			//记录导出excel日志
			this.auditExportExcel(dataJson, fileName, excelUtil.getRecordCount(), this.getSessionUser().getUserid());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Map<String, String> getOrderFlowByCredateForDetailAndExportAllTime(String cwb) {
		Map<String, String> reMap = new HashMap<String, String>();
		try {
			List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb);
			for (OrderFlow of : ofList) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
					reMap.put("customerbrackhouseremark", of.getComment());
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
					reMap.put("Instoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if ((of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())) {
					reMap.put("InSitetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					reMap.put("PickGoodstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					reMap.put("Outstoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					reMap.put("Goclasstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					reMap.put("Gobacktime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
					reMap.put("tuigonghuoshangchukutime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
			}

			if (ofList.size() > 0) {
				reMap.put("Newchangetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ofList.get(ofList.size() - 1).getCredate()));
			}
		} catch (Exception e) {
		}
		return reMap;
	}

	// 获取deliverystateids
	public String getDeliveryStateCwbs(List<DeliveryState> deliverystateList) {
		StringBuffer str = new StringBuffer();
		String deliverystatecwbs;
		if (deliverystateList.size() > 0) {
			for (DeliveryState d : deliverystateList) {
				str.append("'").append(d.getCwb()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		deliverystatecwbs = str.substring(0, str.length() - 1);
		return deliverystatecwbs;
	}

	// 获取paywayid
	public String getPayWayId(List<PayWay> paywayList) {
		StringBuffer str = new StringBuffer();
		String paywayid;
		if (paywayList.size() > 0) {
			for (PayWay p : paywayList) {
				str.append("'").append(p.getId()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		paywayid = str.substring(0, str.length() - 1);
		return paywayid;
	}

	// 获取orderflowid
	public String getOrderFlowCwbs(List<String> orderFlowCwbList) {
		StringBuffer str = new StringBuffer();
		String orderflowid = "";
		if (orderFlowCwbList.size() > 0) {
			for (String cwb : orderFlowCwbList) {
				str.append("'").append(cwb).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		orderflowid = str.substring(0, str.length() - 1);
		return orderflowid;
	}

	// 获取
	public String getDeliveryCwbs(List<DeliveryState> deliveryStateList) {
		StringBuffer str = new StringBuffer();
		String deliveryid;
		if (deliveryStateList.size() > 0) {
			for (DeliveryState ds : deliveryStateList) {
				str.append("'").append(ds.getCwb()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		deliveryid = str.substring(0, str.length() - 1);
		return deliveryid;
	}

	/*
	 * public List<OrderFlow> getOrderFlowList(long datetype,String
	 * begindate,String enddate,String[] operationOrderResultTypes){
	 * List<OrderFlow> orderFlowList = new ArrayList<OrderFlow>(); if(datetype
	 * ==2){ orderFlowList=
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate
	 * ,enddate,FlowOrderTypeEnum.RuKu.getValue(),operationOrderResultTypes);
	 * }else if(datetype==3){ orderFlowList=
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype
	 * (begindate,enddate,FlowOrderTypeEnum
	 * .ChuKuSaoMiao.getValue(),operationOrderResultTypes); }else
	 * if(datetype==4){ orderFlowList=
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype
	 * (begindate,enddate,FlowOrderTypeEnum
	 * .FenZhanDaoHuoSaoMiao.getValue(),operationOrderResultTypes); }else
	 * if(datetype==5){ orderFlowList=
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype
	 * (begindate,enddate,FlowOrderTypeEnum
	 * .FenZhanLingHuo.getValue(),operationOrderResultTypes); }else
	 * if(datetype==7){ orderFlowList=
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype
	 * (begindate,enddate,FlowOrderTypeEnum
	 * .YiFanKui.getValue(),operationOrderResultTypes); }else if(datetype==8){
	 * return
	 * orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate,enddate
	 * ,FlowOrderTypeEnum.YiShenHe.getValue(),operationOrderResultTypes); }
	 * return orderFlowList; }
	 */

	// 给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList,
			List<Reason> reasonList, String begindate, String enddate, List<Remark> remarkList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();

		List<String> cwbs = new ArrayList<String>();
		for (CwbOrder co : clist) {
			cwbs.add(co.getCwb());
		}
		Map<String, Map<String, String>> orderflowList = this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, branchList);
		
		//优化方法，将循环中的sql查询放到循环外部--刘武强20160725
		StringBuffer cwbStr = new StringBuffer();//订单里面cwb的集合
		cwbStr.append("'").append(StringUtils.join(cwbs, "','")).append("'");
		List<DeliveryState> deliveryStateListByCwbs = this.deliveryStateDAO.getLastDeliveryStateByCwbs(cwbStr.toString());
		List<OrderFlow> orderflowListByCwbsAndFlowordertype_tuihuozhaoruku = this.orderFlowDAO.getOrderFlowByCwbsAndFlowordertype(cwbStr.toString(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), begindate, enddate);
		List<OrderFlow> orderflowListByCwbsAndFlowordertype_fenzhandaohuo = this.orderFlowDAO.getOrderFlowByCwbsAndFlowordertype(cwbStr.toString(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "", "");
		List<OrderFlow> orderflowListByCwbsAndFlowordertype_fenzhandaohuoyouhuowudan = this.orderFlowDAO.getOrderFlowByCwbsAndFlowordertype(cwbStr.toString(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "", "");
		List<OrderFlow> orderflowListByCwbsAndFlowordertype_gongyingshangjushoufanku = this.orderFlowDAO.getOrderFlowByCwbsAndFlowordertype(cwbStr.toString(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "", "");
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setCwb(c.getCwb());
				cwbOrderView.setEmaildate(c.getEmaildate());
				cwbOrderView.setCarrealweight(c.getCarrealweight());
				cwbOrderView.setCarsize(c.getCarsize());
				cwbOrderView.setSendcarnum(c.getSendcarnum());
				cwbOrderView.setCwbprovince(c.getCwbprovince());
				cwbOrderView.setCwbcity(c.getCwbcity());
				cwbOrderView.setCwbcounty(c.getCwbcounty());
				cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
				cwbOrderView.setConsigneename(c.getConsigneename());
				cwbOrderView.setConsigneemobile(c.getConsigneemobile());
				cwbOrderView.setConsigneephone(c.getConsigneephone());
				cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());
				cwbOrderView.setResendtime(c.getResendtime() == null ? "" : c.getResendtime());

				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(c.getCustomerwarehouseid()));
				cwbOrderView.setCustomerwarehousename(customwarehouse);
				cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
				cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
				cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
				cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
				cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
				cwbOrderView.setDelivername(this.getQueryUserName(userList, c.getDeliverid()));
				//刘武强优化 -- 20160725
				cwbOrderView.setJobnum(this.getQueryUser(userList, c.getDeliverid()).getJobnum());
				cwbOrderView.setRealweight(c.getCarrealweight());
				cwbOrderView.setCwbremark(c.getCwbremark());
				cwbOrderView.setReceivablefee(c.getReceivablefee());
				cwbOrderView.setCaramount(c.getCaramount());
				cwbOrderView.setPaybackfee(c.getPaybackfee());

				//DeliveryState deliverystate = this.getDeliveryByCwb(c.getCwb());
				cwbOrderView.setPaytype(this.getOldPayWayType(Long.parseLong(c.getNewpaywayid())));// 新支付方式
				cwbOrderView.setPaytype_old(this.getOldPayWayType(c.getPaywayid()));// 原支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setSendcarnum(c.getSendcarnum());
				cwbOrderView.setScannum(c.getScannum());
				//刘武强优化 -- 20160725
				try {
					cwbOrderView.setReturngoodsremark((this.getLastObject(orderflowListByCwbsAndFlowordertype_tuihuozhaoruku, OrderFlow.class)).getComment());
				} catch (Exception e1) {
					e1.printStackTrace();
					this.logger.info(e1.getMessage());
					cwbOrderView.setReturngoodsremark("");
				}
				String currentBranch = this.getQueryBranchName(branchList, c.getCurrentbranchid());
				cwbOrderView.setCurrentbranchname(currentBranch);

				// cwbOrderView.setAuditor();//审核人
				// cwbOrderView.setAudittime();//审核时间
				// cwbOrderView.setMarksflagmen();//标记人
				// cwbOrderView.setMarksflag();//标记状态
				// cwbOrderView.setMarksflagtime();//标记时间
				// Date ruku =this.getOrderFlowByCwbAndType(c.getCwb(),
				// FlowOrderTypeEnum.RuKu.getValue(),"","").getCredate();
				// Date chukusaomiao =this.getOrderFlowByCwbAndType(c.getCwb(),
				// FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"","").getCredate();
				
				// 到货扫描
				//刘武强优化 -- 20160725
				OrderFlow daohuosaomiao = null;
				try {
					//获取到货轨迹集合中，这个订单的所有到货记录 -----刘武强20161020
					List<OrderFlow> tempOrderList = new ArrayList<OrderFlow>();
 					for(OrderFlow temp : orderflowListByCwbsAndFlowordertype_fenzhandaohuo){
						if(c.getCwb() != null && c.getCwb().equals(temp.getCwb())){
							tempOrderList.add(temp);
						}
					}
					daohuosaomiao = this.getLastObject(tempOrderList, OrderFlow.class);
					if (daohuosaomiao.getCwb() == null) {
						//刘武强优化 -- 20160725
						daohuosaomiao = this.getLastObject(orderflowListByCwbsAndFlowordertype_fenzhandaohuoyouhuowudan, OrderFlow.class);
					}
				}catch (Exception e1) {
					e1.printStackTrace();
					this.logger.info(e1.getMessage());
					daohuosaomiao = new OrderFlow();
				}
				// Date fenzhanlinghuo
				// =this.getOrderFlowByCwbAndType(c.getCwb(),
				// FlowOrderTypeEnum.FenZhanLingHuo.getValue(),"","").getCredate();
				// Date yifankui =this.getOrderFlowByCwbAndType(c.getCwb(),
				// FlowOrderTypeEnum.YiFanKui.getValue(),"","").getCredate();
				// Date zuixinxiugai
				// =this.getOrderFlowByCwb(c.getCwb()).getCredate();
				// Date yishenhe=this.getOrderFlowByCwbAndType(c.getCwb(),
				// FlowOrderTypeEnum.YiShenHe.getValue(),"","").getCredate();
				cwbOrderView.setAuditstate(orderflowList.get(c.getCwb()) == null ? 0 : orderflowList.get(c.getCwb()).get("Goclasstime") == null ? 0 : 1);// 审核状态
				// cwbOrderView.setInstoreroomtime(ruku!=null?sdf.format(ruku):"");//入库时间
				cwbOrderView.setInstoreroomtime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Instoreroomtime"));// 入库时间
				cwbOrderView.setOutstoreroomtime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Outstoreroomtime"));// 出库时间
				cwbOrderView.setInSitetime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("InSitetime"));// 到站时间
				long currentbranchid = daohuosaomiao.getBranchid();
				//刘武强优化 -- 20160725
				Branch thisbranch = this.getQueryBranch(branchList,currentbranchid);
				String branchname = thisbranch != null ? thisbranch.getBranchname() : "";
				cwbOrderView.setInSiteBranchname(branchname);
				cwbOrderView.setPickGoodstime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("PickGoodstime"));// 小件员领货时间
				cwbOrderView.setGobacktime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Gobacktime"));// 反馈时间
				cwbOrderView.setGoclasstime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Goclasstime"));// 归班时间
				cwbOrderView.setNowtime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Newchangetime") != null ? orderflowList.get(c.getCwb()).get("Newchangetime") : "");// 最新修改时间
				cwbOrderView.setBackreason(c.getBackreason());
				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
				// cwbOrderView.setExpt_code(); //异常编码
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setInwarhouseremark(this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
						.get(ReasonTypeEnum.RuKuBeiZhu.getText()));
				cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
				
				//刘武强优化 -- 20160725
				DeliveryState deliverystate = null;
				try {
					deliverystate = this.getLastObject(deliveryStateListByCwbs, DeliveryState.class);
				} catch (Exception e1) {
					e1.printStackTrace();
					this.logger.info(e1.getMessage());
					deliverystate = new DeliveryState();
				}
				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView
							.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Gobacktime") != null ? orderflowList
									.get(c.getCwb()).get("Gobacktime") : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					//刘武强优化 -- 20160725
					try{
						cwbOrderView.setCustomerbrackhouseremark(this.getLastObject(orderflowListByCwbsAndFlowordertype_gongyingshangjushoufanku, OrderFlow.class).getComment());
					}catch (Exception e){
						e.printStackTrace();
						this.logger.info(e.getMessage());
						cwbOrderView.setCustomerbrackhouseremark("");
					}
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) && orderflowList.get(c.getCwb()) != null && (orderflowList.get(c.getCwb()).get("Gobacktime") != null)) {
						cwbOrderView.setSendSuccesstime(orderflowList.get(c.getCwb()) == null ? "" : orderflowList.get(c.getCwb()).get("Gobacktime"));// 配送成功时间
					}
				}
				cwbOrderView.setShouldfare(c.getShouldfare());
				cwbOrderView.setExpressPayWay(ExpressSettleWayEnum.getByValue(c.getPaymethod()).getText());
				cwbOrderView.setRealweight(new BigDecimal(c.getRealweight()));

				// 添加揽件省 added by songkaojun 2015-11-17
				cwbOrderView.setSenderprovince(c.getSenderprovince());
				//【修改】vipclu更名为业务类型，上游给什么dmp存什么，同样界面也显示什么 【周欢】 --2016-07-12
				cwbOrderView.setVipclub(c.getVipclub()+"");
				cwbOrderViewList.add(cwbOrderView);
				

			}
		}
		return cwbOrderViewList;
	}

	// 退货出站统计页面的显示
	public List<CwbOrderView> getCwbOrderTuiHuoView(List<CwbOrder> clist, List<TuihuoRecord> tuihuoRecordList, List<Customer> customerList, List<Branch> branchList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				CwbOrderView cwbOrderView = new CwbOrderView();
				TuihuoRecord tr = this.getQueryTuihuo(tuihuoRecordList, c.getCwb());

				cwbOrderView.setCwb(c.getCwb());
				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, tr.getBranchid()));// 退货站点
				cwbOrderView.setFlowordertype(c.getFlowordertype());// 订单的当前最新状态
				cwbOrderView.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(tr.getCwbordertypeid()).getText());
				cwbOrderView.setTuihuozhaninstoreroomtime(tr.getTuihuozhanrukutime());

				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public TuihuoRecord getQueryTuihuo(List<TuihuoRecord> tuihuoRecordList, String cwb) {
		TuihuoRecord tuihuo = new TuihuoRecord();
		for (TuihuoRecord tr : tuihuoRecordList) {
			if (tr.getCwb().equals(cwb)) {
				tuihuo = tr;
				break;
			}
		}
		return tuihuo;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				if (c.getIfeffectflag() != 1) {
					customername += "<strong style=\"color:red;font-size:10\">[已停用]</strong>";
				}
				break;
			}
		}
		return customername;
	}

	public String getQueryCustomWareHouse(List<CustomWareHouse> customerWareHouseList, long customerwarehouseid) {
		String customerwarehouse = "";
		for (CustomWareHouse ch : customerWareHouseList) {
			if (ch.getWarehouseid() == customerwarehouseid) {
				customerwarehouse = ch.getCustomerwarehouse();
				break;
			}
		}
		return customerwarehouse;
	}

	public String getQueryBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchname = b.getBranchname();
				break;
			}
		}
		return branchname;
	}

	public String getQueryUserName(List<User> userList, long userid) {
		String username = "";
		for (User u : userList) {
			if (u.getUserid() == userid) {
				username = u.getRealname();
				if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
					username = username + "(离职)";
				}
				break;
			}
		}
		return username;
	}
	
	/**
	 * 
	 * 根据userid从userlist中找到对应的对象
	 * @author 刘武强
	 * @date:2016年7月25日 上午10:53:15 
	 * @params:@param reasonList
	 * @params:@param reasonid
	 * @params:@return
	 */
	public User getQueryUser(List<User> userList, long userid) {
		for (User u : userList) {
			if (u.getUserid() == userid) {
				return u;
			}
		}
		return new User();
	}
	
	/**
	 * 
	 * 根据branchid从branchlist中找到对应的对象(统一使用同一个方法的话，需要用到反射机制，考虑到性能，所以就不用了)
	 * @author 刘武强
	 * @date:2016年7月25日 下午5:24:42 
	 * @params:@param branchList
	 * @params:@param branchid
	 * @params:@return
	 */
	public Branch getQueryBranch(List<Branch> branchList, long branchid) {
		for (Branch u : branchList) {
			if (u.getBranchid() == branchid) {
				return u;
			}
		}
		return new Branch();
	}

	public String getQueryReason(List<Reason> reasonList, long reasonid) {
		String reasoncontent = "";
		for (Reason r : reasonList) {
			if (r.getReasonid() == reasonid) {
				reasoncontent = r.getReasoncontent();
				break;
			}
		}
		return reasoncontent;
	}

	public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype, String begindate, String enddate) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}
	
	/**
	 *  从集合中获取最后一个对象 
	 * @author 刘武强
	 * @date:2016年7月25日 上午11:27:17 
	 * @params:@param objectList
	 * @params:@param c
	 * @params:@return
	 */
	
	public  <T> T getLastObject(List<T> objectList, Class<T> c)throws Exception {
		T object = objectList.size() > 0 ? objectList.get(objectList.size() - 1) : c.newInstance();
		return object;
	}
	
	public OrderFlow getOrderFlowByCwb(String cwb) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = this.orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		deliveryStateList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
		return deliverState;
	}

	public String getPayWayType(String cwb, DeliveryState ds) {
		StringBuffer str = new StringBuffer();
		String paywaytype = "";
		if (ds.getCash().compareTo(BigDecimal.ZERO) == 1) {
			str.append("现金,");
		}
		if (ds.getPos().compareTo(BigDecimal.ZERO) == 1) {
			str.append("POS,");
		}
		if (ds.getCheckfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("支票,");
		}
		if (ds.getOtherfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("其它,");
		}
		if (str.length() > 0) {
			paywaytype = str.substring(0, str.length() - 1);
		} else {
			paywaytype = "现金";
		}
		return paywaytype;
	}

	public String getOldPayWayType(long payupid) {
		StringBuffer str = new StringBuffer();
		for (PaytypeEnum pe : PaytypeEnum.values()) {
			if (payupid == pe.getValue()) {
				str.append(pe.getText());
			}
		}

		return str.toString();
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	private String getCwbs(long sign, String begindate, String enddate, long isauditTime, String[] nextbranchid, String[] startbranchid, long isaudit, String[] operationOrderResultTypes,
			String[] dispatchbranchid, long deliverid, long flowordertype, String[] kufangid, String[] currentBranchid, long branchid1, String type, String[] branchid2s, String[] customerid,
			long isnowdata, int firstlevelid) {
		String orderflowcwbs = "";
		String customerids = this.getStrings(customerid);
		if (sign == 1) {
			// 滞留订单统计
			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("ZhiLiuTongji");
			int zhiliucheck = 0;
			if (systemInstall != null) {
				try {
					zhiliucheck = Integer.parseInt(systemInstall.getValue());
				} catch (NumberFormatException e) {
					zhiliucheck = 0;
				}
			}
			operationOrderResultTypes[0] = DeliveryStateEnum.FenZhanZhiLiu.getValue() + "";
			List<String> orderFlowList = this.deliveryStateDAO.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid,
					deliverid, zhiliucheck, customerids, firstlevelid);

			if (orderFlowList.size() > 0) {

				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 2) {
			// 拒收订单统计

			// operationOrderResultTypes[0] =
			// DeliveryStateEnum.JuShou.getValue()+"";
			if (operationOrderResultTypes.length == 0) {
				operationOrderResultTypes = new String[4];
				operationOrderResultTypes[0] = DeliveryStateEnum.JuShou.getValue() + "";
				operationOrderResultTypes[1] = DeliveryStateEnum.ShangMenJuTui.getValue() + "";
				operationOrderResultTypes[2] = DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "";
				operationOrderResultTypes[3] = DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "";
			}
			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("JuShouTongji");
			int jushouCheck = 0;
			if (systemInstall != null) {
				try {
					jushouCheck = Integer.parseInt(systemInstall.getValue());
				} catch (NumberFormatException e) {
					jushouCheck = 0;
				}
			}
			List<String> orderFlowList = this.deliveryStateDAO.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid,
					deliverid, jushouCheck, customerids, firstlevelid);

			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 3) {
			// 退供货商出库统计
			List<String> orderFlowList = new ArrayList<String>();

			if (flowordertype == -1) {
				orderFlowList.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), operationOrderResultTypes,
						new String[] {}, 0, 0));
				orderFlowList.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), operationOrderResultTypes,
						new String[] {}, 0, 0));
				orderFlowList.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), operationOrderResultTypes,
						new String[] {}, 0, 0));
			} else {
				orderFlowList = this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, flowordertype, operationOrderResultTypes, new String[] {}, 0, 0);
			}

			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 4) {
			// 在途统计

		} else if (sign == 5) {
			// 妥投
			if (operationOrderResultTypes.length == 0) {
				operationOrderResultTypes = new String[] { DeliveryStateEnum.PeiSongChengGong.getValue() + "", DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "",
						DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "" };
			}
			List<String> orderFlowLastList = this.deliveryStateDAO.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid,
					deliverid, 1, customerids, firstlevelid);
			if (orderFlowLastList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowLastList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 6) {
			// 库房出库
			List<String> orderFlowList = this.orderFlowDAO.getOrderFlowForOutwarehouse(begindate, enddate, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), nextbranchid, kufangid);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 7) {
			// 库房发货统计

		} else if (sign == 8) {
			// 到货统计
			List<String> orderFlowList = this.orderFlowDAO.getOrderFlowBySome(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), this.getStrings(currentBranchid), isnowdata);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 9) {
			// 中转订单统计
			List<String> orderFlowList = new ArrayList<String>();
			String[] nextbranchids = new String[] {};
			String[] startbranchids = new String[] {};
			long flowtype = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
			if (type.equals("startbranchid")) {
				nextbranchids = branchid2s;
				startbranchids = new String[] { branchid1 + "" };
				flowtype = FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
			} else if (type.equals("nextbranchid")) {
				nextbranchids = new String[] { branchid1 + "" };
				startbranchids = branchid2s;
			}
			orderFlowList = this.orderFlowDAO.getOrderFlowForZhongZhuan(begindate, enddate, flowtype, nextbranchids, startbranchids);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}

		} else if (sign == 10) {
			// 站点出站统计
			List<OrderFlow> orderLastList = new ArrayList<OrderFlow>();
			String sig = "'";
			List<OrderFlow> orderFlowList = this.orderFlowDAO.getOrderFlowForZhanDianChuZhan(begindate, enddate, startbranchid, nextbranchid, flowordertype);
			if (orderFlowList.size() > 0) {
				StringBuffer cwbTemp = new StringBuffer();
				for (OrderFlow of : orderFlowList) {// 第一次循环，过滤获取入库时间符合条件的数据
					if (cwbTemp.indexOf(sig + of.getCwb() + sig) == -1) {
						cwbTemp = cwbTemp.append(sig).append(of.getCwb()).append(sig);
						orderLastList.add(of);
					}
				}
			}

			StringBuffer str = new StringBuffer();
			if (orderLastList.size() > 0) {
				for (OrderFlow of : orderLastList) {
					str.append("'").append(of.getCwb()).append("',");
				}
				orderflowcwbs = str.substring(0, str.length() - 1);
			} else {
				orderflowcwbs = "'--'";
			}
		}

		return orderflowcwbs;
	}

	public String getCwbs(String flowordertypes, String begindate, String enddate, String[] kufangids, String[] branchids) {
		String kufangidStr = "";
		String branchidStr = "";
		if ((kufangids != null) && (kufangids.length > 0)) {
			for (String kufangid : kufangids) {
				kufangidStr += kufangid + ",";
			}
			kufangidStr = (kufangidStr.length() > 0 ? kufangidStr.substring(0, kufangidStr.length() - 1) : "");
		}
		if ((branchids != null) && (branchids.length > 0)) {
			for (String branchid : branchids) {
				branchidStr += branchid + ",";
			}
			branchidStr = (branchidStr.length() > 0 ? branchidStr.substring(0, branchidStr.length() - 1) : "");
		}
		List<String> cwblist = this.orderFlowDAO.getOneCwbs(flowordertypes, begindate, enddate, kufangidStr, branchidStr);
		return this.getCwbs(cwblist);
	}

	public String getCwbs(List<String> cwblist) {
		StringBuffer str = new StringBuffer();
		String cwbs = "";
		if ((cwblist != null) && (cwblist.size() > 0)) {
			for (String cwb : cwblist) {
				str.append("'").append(cwb).append("',");
			}
			cwbs = str.substring(0, str.length() - 1);
		}
		return cwbs;
	}

	public String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getStrings(List<String> strArr) {
		String strs = "";
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

	public List<String> getList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if ((strArr != null) && (strArr.length > 0)) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
	}

	public Map<Long, Long> getCustomerMap(List<Customer> customerList, long kufangid, String begindate, String enddate) {
		final Map<Long, Long> reMap = new HashMap<Long, Long>();

		if (customerList.size() > 0) {
			for (Customer c : customerList) {
				String sql = "select count(1) from express_ops_cwb_detail where state=1 and customerid=" + c.getCustomerid();

				if (begindate.length() > 0) {
					sql += " and emaildate >='" + begindate + "'";
				}
				if (enddate.length() > 0) {
					sql += " and emaildate <= '" + enddate + "'";
				}
				if (kufangid > 0) {
					sql += " and carwarehouse = " + kufangid;
				}

				reMap.put(c.getCustomerid(), this.jdbcTemplate.queryForLong(sql));
			}
		}

		return reMap;
	}

	@DataSource(DatabaseType.REPLICA)
	public void exportExcelOutToComm(HttpServletResponse response, String mouldfieldids2, String commoncode, long startbranchid, int outbranchflag) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String dataJson = setexportExcelOutToCommJson(mouldfieldids2, commoncode, startbranchid, outbranchflag);
		try {
			// 查询出数据

			final String sql = "SELECT de.* FROM `commen_cwb_order` AS co LEFT JOIN `express_ops_cwb_detail` AS de " + " ON co.`cwb`=de.`cwb` WHERE co.`commencode`='" + commoncode
					+ "' AND co.outbranchflag=" + outbranchflag + "  AND co.`stateTime`='' AND de.`state`=1  " + (startbranchid > 0 ? " And co.startbranchid=" + startbranchid : "");

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsService.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataStatisticsService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsService.this.remarkDAO.getAllRemark();
					;
					final Map<String, Map<String, String>> remarkMap = DataStatisticsService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsService.this.reasonDao.getAllReason();
					DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}
						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsService.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});

				}
				
				@Override
				public long getRecordCount(){
					try{
						int size = (Integer) DataStatisticsService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>(){ 
							@Override
							public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
								int count = 0;
					            while(rs.next()){
					                count = count + 1;
					            }
								return count;
							}
						});
						return size;
					}catch(Exception e){
						logger.info("Not able to fetch excel row count, so use default 0");
						return 0;
					}
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			//记录导出excel日志
			this.auditExportExcel(dataJson, fileName, excelUtil.getRecordCount(), this.getSessionUser().getUserid());

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// 获取orderflowid
	public String getConnectCwb(List<CwbOrder> orderFlowCwbList) {
		StringBuffer str = new StringBuffer();
		String orderflowid = "";
		if (orderFlowCwbList.size() > 0) {
			for (CwbOrder cwb : orderFlowCwbList) {
				str.append("'").append(cwb.getCwb()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		orderflowid = str.substring(0, str.length() - 1);
		return orderflowid;
	}

	// 电商单量查询与导出
	/**
	 * 获得指定日期的前一天
	 *
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			logger.error("", e);
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	
	/**
	 * 记录所有导出文件的操作
	 * 
	 * @param request
	 * @param dataJson
	 * @param userid
	 */
	private void auditExportExcel(String dataJson, String fileName, long count, long userid) {
		try{
			String logStr = String.format(
					"UserId [%s] was exported the excel file:[name: %s, line-count: %d] by using conditions [%s]", userid, fileName, count, dataJson);
			logger.info(logStr);
		}catch(Exception e){
			logger.error("Fail to log exported file info");
		}
	}
	
	private String setDataStatisticsExportExcelJson(String begindate, String enddate, long isauditTime,
			String[] nextbranchid, String[] startbranchids, long isaudit, String[] operationOrderResultTypes,
			String[] dispatchbranchid, long deliverid, long flowordertype, String[] kufangids, String[] currentBranchid,
			long branchid1, String type, String[] branchid2s, String[] customerids, String[] cwbordertypeids,
			long paywayid, long isnowdata, int paybackfeeIsZero, String servicetype, int firstlevelid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("isaudit", isaudit);
		json.put("isauditTime", isauditTime);
		json.put("type", type);
		json.put("startbranchids", startbranchids);
		json.put("nextbranchid", nextbranchid);
		json.put("dispatchbranchid", dispatchbranchid);
		json.put("currentBranchid", currentBranchid);
		json.put("customerids", customerids);
		json.put("deliverid", deliverid);
		json.put("flowordertype", flowordertype);
		json.put("paytype", paywayid);
		json.put("kufangids", kufangids);
		json.put("operationOrderResultTypes", operationOrderResultTypes);
		json.put("branchid1", branchid1);
		json.put("branchid2s", branchid2s);
		json.put("isnowdata", isnowdata);
		json.put("paybackfeeIsZero", paybackfeeIsZero);
		json.put("servicetype", servicetype);
		json.put("firstlevelid", firstlevelid);
		return json.toString();
	}

	private String setDataStatisticsZaituExportExcelJson(String[] cwbordertypeids, String[] nextbranchids,
			String[] kufangids, String begindate, String enddate, long datetype) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("datetype", datetype);
		json.put("enddate", enddate);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("nextbranchids", nextbranchids);
		json.put("kufangids", kufangids);
		return json.toString();

	}

	private String setDataStatisticsExportOutWareExcelJson(String[] cwbordertypeids, String[] nextbranchids,
			String[] kufangids, String[] customerids, String begindate, String enddate) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerids", customerids);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("nextbranchids", nextbranchids);
		json.put("nextbranchids", nextbranchids);
		json.put("kufangids", kufangids);
		return json.toString();
	}

	private String setDataStatisticsExportIntoWareExcelJson(long cwbordertypeid, String begindate, String enddate,
			String emaildatebegin, String emaildateend, long kufangid, String isruku, String[] customerids) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerids", customerids);
		json.put("cwbordertypeid", cwbordertypeid);
		json.put("emaildatebegin", emaildatebegin);
		json.put("emaildateend", emaildateend);
		json.put("kufangid", kufangid);
		json.put("isruku", isruku);
		return json.toString();
	}
	
	private String setexportExcelByNoresultJson(Map<String, Object> paramsMAP){
		JSONObject json = new JSONObject();
		for(String key : paramsMAP.keySet()){
			if(paramsMAP.get(key).getClass().isArray()){
				json.put(key, (String[])(paramsMAP.get(key)));
			}else{
				json.put(key, (String)(paramsMAP.get(key)));
			}			
		}
		return json.toString();
	}
	
	private String setcwbExportExcelJson(String begindate1, String enddate1, String branchid1, String customerid1, String types,
			long istuihuozhanruku1, long tuihuotype){
		JSONObject json = new JSONObject();
		json.put("begindate", begindate1);
		json.put("enddate", enddate1);
		json.put("branchid", branchid1);
		json.put("customerid",customerid1);
		json.put("types",types);
		json.put("istuihuozhanruku", istuihuozhanruku1);
		json.put("tuihuotype", tuihuotype);
		return json.toString();
	}
	
	private String setexportExcelOutToCommJson(String mouldfieldids2, String commoncode, long startbranchid,
			int outbranchflag) {
		JSONObject json = new JSONObject();
		json.put("mouldfieldids", mouldfieldids2);
		json.put("commoncode", commoncode);
		json.put("startbranchid", startbranchid);
		json.put("outbranchflag", outbranchflag);
		return json.toString();
	}

	
	
}
