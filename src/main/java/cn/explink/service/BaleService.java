package cn.explink.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.service.AutoUserService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderView;
import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.BaleCwb;
import cn.explink.domain.BaleView;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
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
import cn.explink.domain.VO.BaleCwbClassifyVo;
import cn.explink.domain.VO.express.BatchCount;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BaleUseStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import net.sf.json.JSONObject;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

@Service
public class BaleService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
	BaleDao baleDAO;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	BaleCwbDao baleCwbDAO;
	@Autowired
	CwbApplyZhongZhuanDAO applyZhongZhuanDAO;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	CwbStateControlDAO cwbStateControlDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	@Autowired
	AutoUserService autoUserService;
	@Autowired
	UserService userService;
	@Autowired
	TransCwbDao transCwbDao;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void baleExcel(HttpServletRequest request, HttpServletResponse response, String chukubegindate, String chukuenddate) {

		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[6];
		cloumnName2 = new String[6];
		cloumnName3 = new String[6];
		this.exportService.setBale(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;

		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Bale_List_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据

			final String sql = "select * from express_ops_bale where cretime>='" + chukubegindate + "' and cretime<='" + chukuenddate + "'";
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<Branch> branchList = BaleService.this.branchDAO.getAllBranches();
					BaleService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						private int count = 0;

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							Row row = sheet.createRow(this.count + 1);
							row.setHeightInPoints(15);

							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								try {
									try {
										if ("balestate".equals(cloumnName5[i])) {
											for (BaleStateEnum bal : BaleStateEnum.values()) {
												if (bal.getValue() == rs.getInt("balestate")) {
													a = bal.getText();
													break;
												}
											}
										} else if ("branchid".equals(cloumnName5[i])) {
											a = BaleService.this.getBranchName(rs.getLong("branchid"), branchList);
										} else if ("nextbranchid".equals(cloumnName5[i])) {
											a = BaleService.this.getBranchName(rs.getLong("nextbranchid"), branchList);
										}

										else {
											a = rs.getObject(cloumnName5[i]);
										}
										if (cloumnName6[i].equals("double")) {
											cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
										} else {
											cell.setCellValue(a == null ? "" : a.toString());
										}
									} catch (Exception e) {
										logger.error("", e);
									}
								} catch (IllegalArgumentException e) {
									logger.error("", e);
								}
							}
							this.count++;

						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}

	}

	public String getBranchName(long branchid, List<Branch> branchList) {
		String branchname = "";

		if ((branchList != null) && (branchList.size() > 0)) {
			for (Branch branch : branchList) {
				if (branchid == branch.getBranchid()) {
					branchname = branch.getBranchname();
					break;
				}
			}

		}
		return branchname;
	}

	public void exportExcelMethod(HttpServletResponse response, HttpServletRequest request, long baleid, long page) {
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
		String fileName = "Bale_Detail_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀
		long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count"));

		if (count > 0) {
			if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
				otherName = "1-" + count;
			} else {
				otherName = (page * Page.EXCEL_PAGE_NUMBER) + 1 + "-" + (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
			}
		}
		fileName = fileName + otherName + lastStr;
		try {

			List<String> cwbsList = this.baleCwbDAO.getCwbsByBale(String.valueOf(baleid));
			String cwbs = "";
			if ((cwbsList != null) && (cwbsList.size() > 0)) {
				for (String cwb : cwbsList) {
					cwb = this.cwbOrderService.translateCwb(cwb);
					cwbs += "'" + cwb + "',";
				}
				if (cwbs.contains(",")) {
					cwbs = cwbs.substring(0, cwbs.lastIndexOf(","));
				}
			}
			// String sql1 =
			// "select de.* from express_ops_bale_cwb as bw left join express_ops_cwb_detail de on bw.cwb=de.cwb"
			// + " where de.state=1 and bw.baleid='" + baleid + "' limit "
			String sql1 = "select * from express_ops_cwb_detail where state=1 and cwb in(" + cwbs + ") limit " + (page * Page.EXCEL_PAGE_NUMBER) + " ," + Page.EXCEL_PAGE_NUMBER;

			final String sql = sql1;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = BaleService.this.userDAO.getAllUserByuserDeleteFlag();
					final Map<Long, Customer> cMap = BaleService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = BaleService.this.branchDAO.getAllBranches();
					final List<Common> commonList = BaleService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = BaleService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = BaleService.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = BaleService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = BaleService.this.reasonDao.getAllReason();

					BaleService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap, Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = BaleService.this.exportService
										.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = BaleService.this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp, complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : BaleService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : BaleService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : BaleService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Map<String, Map<String, String>> getOrderFlowByCredateForDetailAndExportAllTime(List<String> cwbs, List<Branch> branchlist) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
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
				if ((this.getCwbRow(map, of).get("Newchangetime") == null) || (of.getCredate().getTime() > new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getCwbRow(map, of)
						.get("Newchangetime")).getTime())) {
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

	// 给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList, List<Reason> reasonList, String begindate, String enddate, List<Remark> remarkList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();

		List<String> cwbs = new ArrayList<String>();
		for (CwbOrder co : clist) {
			cwbs.add(co.getCwb());
		}
		Map<String, Map<String, String>> orderflowList = this.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, branchList);

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
				cwbOrderView.setRealweight(c.getCarrealweight());
				cwbOrderView.setCwbremark(c.getCwbremark());
				cwbOrderView.setReceivablefee(c.getReceivablefee());
				cwbOrderView.setCaramount(c.getCaramount());
				cwbOrderView.setPaybackfee(c.getPaybackfee());

				DeliveryState deliverystate = this.getDeliveryByCwb(c.getCwb());
				cwbOrderView.setPaytype(this.getOldPayWayType(Long.parseLong(c.getNewpaywayid())));// 新支付方式
				cwbOrderView.setPaytype_old(this.getOldPayWayType(c.getPaywayid()));// 原支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setReturngoodsremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), begindate, enddate).getComment());
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
				OrderFlow daohuosaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "", "");
				if (daohuosaomiao.getCwb() == null) {
					daohuosaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "", "");
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
				cwbOrderView.setAuditstate(orderflowList.get(c.getCwb()).get("Goclasstime") == null ? 0 : 1);// 审核状态
				// cwbOrderView.setInstoreroomtime(ruku!=null?sdf.format(ruku):"");//入库时间
				cwbOrderView.setInstoreroomtime(orderflowList.get(c.getCwb()).get("Instoreroomtime"));// 入库时间
				cwbOrderView.setOutstoreroomtime(orderflowList.get(c.getCwb()).get("Outstoreroomtime"));// 出库时间
				cwbOrderView.setInSitetime(orderflowList.get(c.getCwb()).get("InSitetime"));// 到站时间
				long currentbranchid = daohuosaomiao.getBranchid();
				Branch thisbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
				String branchname = thisbranch != null ? thisbranch.getBranchname() : "";
				cwbOrderView.setInSiteBranchname(branchname);
				cwbOrderView.setPickGoodstime(orderflowList.get(c.getCwb()).get("PickGoodstime"));// 小件员领货时间
				cwbOrderView.setGobacktime(orderflowList.get(c.getCwb()).get("Gobacktime"));// 反馈时间
				cwbOrderView.setGoclasstime(orderflowList.get(c.getCwb()).get("Goclasstime"));// 归班时间
				cwbOrderView.setNowtime(orderflowList.get(c.getCwb()).get("Newchangetime") != null ? orderflowList.get(c.getCwb()).get("Newchangetime") : "");// 最新修改时间
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

				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView
							.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (orderflowList.get(c.getCwb()).get("Gobacktime") != null ? orderflowList
									.get(c.getCwb()).get("Gobacktime") : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), begindate, enddate).getComment());
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) && (orderflowList.get(c.getCwb()).get("Gobacktime") != null)) {
						cwbOrderView.setSendSuccesstime(orderflowList.get(c.getCwb()).get("Gobacktime"));// 配送成功时间
					}
				}
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

	private String getCwbs(long sign, String begindate, String enddate, long isauditTime, String[] nextbranchid, String[] startbranchid, long isaudit, String[] operationOrderResultTypes, String[] dispatchbranchid, long deliverid, long flowordertype, String[] kufangid, String[] currentBranchid, long branchid1, String type, String[] branchid2s, String[] customerid, long isnowdata, int firstlevelid) {
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
			List<String> orderFlowList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, zhiliucheck, customerids, firstlevelid);
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
			List<String> orderFlowList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, jushouCheck, customerids, firstlevelid);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
		} else if (sign == 3) {
			// 退供货商出库统计
			List<String> orderFlowList = new ArrayList<String>();

			if (flowordertype == -1) {
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
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
				operationOrderResultTypes = new String[] { DeliveryStateEnum.PeiSongChengGong.getValue() + "", DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "", DeliveryStateEnum.ShangMenTuiChengGong
						.getValue() + "" };
			}
			List<String> orderFlowLastList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, 1, customerids, firstlevelid);
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
			List<String> orderFlowList = this.orderFlowDAO
					.getOrderFlowBySome(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), this
							.getStrings(currentBranchid), isnowdata);
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

			if (type.equals("startbranchid")) {
				nextbranchids = branchid2s;
				startbranchids = new String[] { branchid1 + "" };
			} else if (type.equals("nextbranchid")) {
				nextbranchids = new String[] { branchid1 + "" };
				startbranchids = branchid2s;
			}
			orderFlowList = this.orderFlowDAO.getOrderFlowForZhongZhuan(begindate, enddate, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), nextbranchids, startbranchids);
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

	/**
	 * 包号验证（库房出库、退货出站）
	 *
	 * @param user
	 */
	public void validateBaleCheck(User user, String baleno, String cwb, long branchid, long flowOrderTypeEnum) {
		if ("0".equals(baleno)) {
			// 包号不能为0
			throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI);
		}
		
		List<Bale> baleList=this.baleDAO.getBaleYifengbao(baleno);
		if(baleList!=null&&baleList.size()>0){
			throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Bale_Error, baleno, BaleStateEnum.YiFengBao.getText());
		}
		
		/* ***************add begin*********************/
		//add by neo01.huang，2016-7-21，转换session用户的branchid
		userService.convertSessionUserBranchId(user);
		/* ***************add end***********************/
		
		// 根据包号查找
		Bale bale = this.baleDAO.getBaleWeifengbaoByLock(baleno);
		if (bale != null) {
			if (bale.getBranchid() != user.getBranchid()) {
				// 非本站包
				throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Bale_Fei_Ben_Zhan);
			}
			if (bale.getNextbranchid() != branchid) {
				// 不是这个目的站的包
				throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Bale_BU_SHI_ZHE_GE_MU_DI, this.branchDAO.getBranchByBranchid(bale.getNextbranchid()).getBranchname());
			}
			// 只有未封包状态的包才可以扫描订单
			if (bale.getBalestate() != BaleStateEnum.WeiFengBao.getValue()) {
				String errorstate = "";
				for (BaleStateEnum ft : BaleStateEnum.values()) {
					if (bale.getBalestate() == ft.getValue()) {
						errorstate = ft.getText();
					}
				}
				throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Bale_Error, bale.getBaleno(), errorstate);
			}
		}
	}

	/**
	 * 订单的包验证（库房出库扫描、中转站出库扫描、退货出站）
	 */
	public void validateCwbBaleCheck(User user, String baleno, String cwb, CwbOrder co, long branchid, long flowOrderTypeEnum, long currentbranchid, FlowOrderTypeEnum flowEnum) {
		// ==================做和订单出站相同的限制====================
		if ((co.getFlowordertype() != flowOrderTypeEnum) || (co.getStartbranchid() != currentbranchid)) {
			String str = "合包";
			this.validateStateTransfer(co, flowEnum, str);
		}
		if (this.baleCwbDAO.getBaleAndCwbCount(baleno, cwb) == 0) {
		} else {
			if (!"".equals(co.getPackagecode()) && (co.getPackagecode() != null)) {
				Bale coBale = this.baleDAO.getBaleOnway(co.getPackagecode());
				Branch userbranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
				if (coBale != null) {
					Branch balebranch = this.branchDAO.getBranchByBranchid(coBale.getBranchid());
					// 非中转站、退货站用户 或者 中转站用户&&建包的站点为中转站 或者 退货站用户&&建包的站点为退货站
					if (((coBale != null) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (userbranch.getSitetype() != BranchEnum.TuiHuo.getValue())) || ((coBale != null) && (userbranch
							.getSitetype() == BranchEnum.ZhongZhuan.getValue()) && (balebranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) || ((coBale != null) && (userbranch.getSitetype() == BranchEnum.TuiHuo
							.getValue()) && (balebranch.getSitetype() == BranchEnum.TuiHuo.getValue()))) {
						long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid())
								.getIsypdjusetranscwb();

						if (baleno.equals(co.getPackagecode()) && (isypdjusetranscwb == 1)) {
							// 重复封包
							throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
						}
						if ((coBale.getBranchid() == user.getBranchid()) && (isypdjusetranscwb != 1) && (co.getScannum() >= co.getSendcarnum())) {
							// 订单{0}已经在{1}包号中，确认重新封包吗?
							throw new CwbException(cwb, flowOrderTypeEnum, ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
						} else {
							String errorstate = "";
							for (BaleStateEnum ft : BaleStateEnum.values()) {
								if (coBale.getBalestate() == ft.getValue()) {
									errorstate = ft.getText();
								}
							}

							/*
							 * if((coBale.getBalestate() ==
							 * BaleStateEnum.YiDaoHuo
							 * .getValue())&&(coBale.getBranchid
							 * ()!=userbranch.getBranchid())){
							 *
							 * }else{ // 操作失败，此订单已经在{0}包号{1}! throw new
							 * CwbException(cwb, flowOrderTypeEnum,
							 * ExceptionCwbErrorTypeEnum.Bale_Error1, cwb,
							 * co.getPackagecode(), errorstate); }
							 */

						}
					}
				}
			}
		}
	}

	/**
	 * 库房出库根据包号扫描订单检查
	 *
	 * @param forceOut
	 *            是否强制出库
	 */
	@Transactional
	public void baleaddcwbChukuCheck(User user, String baleno, String cwb, boolean forceOut, long currentbranchid, long branchid) {
		if (!"".equals(baleno) && !"".equals(cwb)) {
			this.logger.info("===库房出库包号扫描封包检查开始===");
			this.logger.info("开始验证包号" + baleno);

			// ==================验证包号=======================
			this.validateBaleCheck(user, baleno, cwb, branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			// ==================验证包号End=======================

			// ==================验证订单状态=======================
			this.logger.info("开始验证订单" + cwb);
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			if (co == null) {
				// 订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
			long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
			if ((isypdjusetranscwb == 1) && (null != co.getTranscwb()) && (co.getTranscwb().length() > 0)) {
				this.validateIsSubCwb(scancwb, co, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			}
			this.checkBaleOfOrder(baleno, cwb, scancwb, isypdjusetranscwb, FlowOrderTypeEnum.ChuKuSaoMiao, co);

			/*
			 * long count1 =
			 * this.applyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCounts
			 * (cwb,0); if(count1!=0){ throw new CwbException(cwb,
			 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
			 * ExceptionCwbErrorTypeEnum.Weishenhebuxuzhongzhuankuhebaochuku); }
			 * long count2 =
			 * this.applyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCounts
			 * (cwb,2); if(count2!=0){ throw new CwbException(cwb,
			 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
			 * ExceptionCwbErrorTypeEnum
			 * .Shenhebutongguobuyunxuzhongzhuankuhebaochuku); }
			 */
			// start-------------
			/*
			 * if (this.userDAO.getAllUserByid(user.getUserid()).
			 * getIsImposedOutWarehouse() == 0) {// 是否拥有 // 请指出库权限 // 1是 // 0 //
			 * 否 // 默认1 forceOut = false; } if (((co.getFlowordertype() ==
			 * FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) ||
			 * (co.getFlowordertype() ==
			 * FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) ||
			 * ((co .getFlowordertype() ==
			 * FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate()
			 * == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) &&
			 * (co.getCurrentbranchid() != currentbranchid)) { throw new
			 * CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
			 * ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO); } //若当前 归班反馈
			 * 反馈为待中转，失效该记录 DeliveryState ds =
			 * this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb); if((ds !=
			 * null) && (DeliveryStateEnum.DaiZhongZhuan.getValue() ==
			 * ds.getDeliverystate()) && (ds.getDeliverybranchid() ==
			 * currentbranchid)){
			 * this.deliveryStateDAO.inactiveDeliveryStateByCwb(ds.getCwb()); }
			 */
			// end---------------

			Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
			Branch nextBranch = this.branchDAO.getQueryBranchByBranchid(co.getNextbranchid());
			boolean aflag = false;
			if ((ifBranch != null) && (ifBranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
				List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentbranchid, branchid, 2);
				for (BranchRoute r : routelist) {
					if (branchid == r.getToBranchId()) {
						aflag = true;
					}
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&
				if ((co.getNextbranchid() != 0) && !aflag && (branchid > 0) && !forceOut) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
							.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&co.getNextbranchid()!=branchid
			} else if ((co.getNextbranchid() != 0) && (branchid > 0) && !forceOut) {
				// 计算下一站
				long compariBranchid = nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid();
				// 计算的下一站！=所选的下一站 && 计算的下一站!=0
				List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(branchid, co.getDeliverybranchid(), 2);
				if (null == routelist) {
					if ((compariBranchid != branchid) && (compariBranchid != 0)) {
						throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
								.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
					}
				}
			}

			if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
					.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid)) {
				boolean ignore=false;
				if(co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()){
					int isOpenFlag=this.autoUserService.getAutoFlag();
					if(isOpenFlag==1){
						ignore=true;
					}
				}
				
				if(!ignore){
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
				}
			}

			Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
			Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
			if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
			}

			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid)) && ((co
					.getSendcarnum() > 1) && (co.getSendcarnum() == co.getScannum())) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && ((co.getSendcarnum() > 1) && (co.getSendcarnum() == co
					.getScannum())) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
			// ==================验证订单状态End=======================

			// ==================验证订单的包号=======================
			this.logger.info("开始验证订单的包号" + co.getPackagecode());
			this.validateCwbBaleCheck(user, baleno, scancwb, co, branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), currentbranchid, FlowOrderTypeEnum.ChuKuSaoMiao);
		}

	}

	/**
	 * 验证包号是否已经封包
	 *
	 * @param cwb
	 * @param scancwb
	 * @param isypdjusetranscwb
	 * @param flowOrderTypeEnum
	 */
	private void checkBaleOfOrder(String baleno, String cwb, String scancwb, long isypdjusetranscwb, FlowOrderTypeEnum flowOrderTypeEnum, CwbOrder co) {
		List<BaleCwb> baleCwbs = new ArrayList<BaleCwb>();
		boolean falg = true;
		if (isypdjusetranscwb == 1) {
			baleCwbs = this.baleCwbDAO.getBaleCwbByCwb(scancwb);
			falg = false;
		} else {
			baleCwbs = this.baleCwbDAO.getBaleCwbByCwb(cwb);
		}
		if ((baleCwbs != null) && (baleCwbs.size() > 0)) {
			for (BaleCwb baleCwb : baleCwbs) {
				Bale bale = this.baleDAO.getBaleById(baleCwb.getBaleid());
				if (co.getScannum() >= co.getSendcarnum()) {
					throw new CwbException(scancwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YIJINGSHANGXIAN, "扫描封包");
				}
				if (falg) {
					if ((null != bale) && baleno.equals(bale.getBaleno()) && ((bale.getBalestate() == BaleStateEnum.YiFengBao.getValue()) || (bale.getBalestate() == BaleStateEnum.YiFengBaoChuKu
							.getValue()))) {
						throw new CwbException(scancwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YIJINGFENGBAO, bale.getBaleno(), "扫描封包");
					}
				} else if ((null != bale) && ((bale.getBalestate() == BaleStateEnum.YiFengBao.getValue()) || (bale.getBalestate() == BaleStateEnum.YiFengBaoChuKu.getValue()))) {
					throw new CwbException(scancwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YIJINGFENGBAO, bale.getBaleno(), "扫描封包");
				}
			}
		}
	}

	private void validateIsSubCwb(String cwb, CwbOrder co, long flowordertype) {
		String transcwb = co.getTranscwb();
		if (!org.springframework.util.StringUtils.hasLength(transcwb)) {// 为兼容腾讯达历史数据没有transcwb的问题，下个版本请删除
			return;
		}
		// 排除一票一件，也存在运单号情况，
				if (!((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1))) {
					return;
				}
		String splitString = this.getSplitstring(transcwb);
		String[] split = transcwb.split(splitString);
		for (String string : split) {
			if (string.equals(cwb)) {
				return;
			}
		}
		// 2013-8-5腾讯达需求，领货不再限制只能扫描运单号，产品确定需求不是做成开关，而是所有客户统一如此处理
		if ((flowordertype != FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) && (flowordertype != FlowOrderTypeEnum.FenZhanLingHuo.getValue())) {
			throw new CwbException(cwb, flowordertype, ExceptionCwbErrorTypeEnum.Qing_SAO_MIAO_YUN_DAN_HAO);
		}
	}

	public String getSplitstring(String transcwb) {
		if (transcwb.indexOf(':') != -1) {
			return ":";
		}
		return ",";
	}

	public void validateStateTransfer(CwbOrder co, FlowOrderTypeEnum flowEnum, String text) {
		CwbFlowOrderTypeEnum fromstate = CwbFlowOrderTypeEnum.getText((int) co.getFlowordertype());

		// DMP 4.2.8 中转库合包出库缺陷修改
		// if (co.getFlowordertype() ==
		// FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
		// if ((fromstate != null) &&
		// (this.cwbStateControlDAO.getCwbStateControlByParam((int)
		// co.getFlowordertype(), flowEnum.getValue()) != null)) {
		// throw new CwbException(co.getCwb(), flowEnum.getValue(),
		// ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromstate.getText(),
		// text);
		// }
		// } else if ((fromstate != null) &&
		// (this.cwbStateControlDAO.getCwbStateControlByParam((int)
		// co.getFlowordertype(), flowEnum.getValue()) == null)) {
		// throw new CwbException(co.getCwb(), flowEnum.getValue(),
		// ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromstate.getText(),
		// text);
		// }

		if ((fromstate != null) && (this.cwbStateControlDAO.getCwbStateControlByParam((int) co.getFlowordertype(), flowEnum.getValue()) == null)) {
			throw new CwbException(co.getCwb(), flowEnum.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromstate.getText(), text);
		}
	}

	/**
	 * 退货出站根据包号扫描订单检查
	 *
	 * @param forceOut
	 *            是否强制出库
	 */
	@Transactional
	public void baleaddcwbTuiHuoCheck(User user, String baleno, String cwb, boolean forceOut, long currentbranchid, long branchid) {
		if (!"".equals(baleno) && !"".equals(cwb)) {
			this.logger.info("===退货出站封包检查开始===");
			this.logger.info("开始验证包号" + baleno);
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			// 待审核
			long count = this.orderBackCheckDAO.getOrderbackCheck(cwb, 1);
			if (count != 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.Tui_huo_chu_zhan_dai_shen_he);
			}
			// 审核为站点配送
			long count2 = this.orderBackCheckDAO.getOrderbackResult(cwb, 2);
			if (count2 != 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.Tui_huo_chu_zhan_shen_he_shenhe_zhandianpeisong);
			}
			// ==================验证包号=======================
			this.validateBaleCheck(user, baleno, cwb, branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue());
			// ==================验证包号End=======================

			// ==================验证订单状态=======================
			this.logger.info("开始验证订单" + cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {
				forceOut = false;
			}
			if (co == null) {
				// 订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
						.getBranchname());
			}

			//if ((co.getStartbranchid() == currentbranchid) && (co.getNextbranchid() == branchid) && (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {
			//	throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			//}
			// ==================验证订单状态End=======================

			// ==================验证订单的包号=======================
			this.logger.info("开始验证订单的包号" + co.getPackagecode());
			this.validateCwbBaleCheck(user, baleno, scancwb, co, branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), currentbranchid, FlowOrderTypeEnum.TuiHuoChuZhan);
		}
	}

	/**
	 * 中转站根据包号扫描订单检查
	 *
	 * @param forceOut
	 *            是否强制出库
	 */
	@Transactional
	public void baleaddcwbzhongzhuanchuzhanCheck(User user, String baleno, String cwb, boolean forceOut, long currentbranchid, long branchid) {
		if (!"".equals(baleno) && !"".equals(cwb)) {
			this.logger.info("===中转出站封包检查开始===");
			this.logger.info("开始验证包号" + baleno);

			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			
			// 首先验证订单号是否在中转出站审核表中被审核通过
			long count1 = this.applyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCounts(cwb, 0);
			if (count1 != 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Weishenhebuxuhebaochuku);
			}
			long count2 = this.applyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCounts(cwb, 2);
			if (count2 != 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuhebaochuku);
			}

			// ==================验证包号=======================
			this.validateBaleCheck(user, baleno, cwb, branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			// ==================验证包号End=======================

			// ==================验证订单状态=======================
			this.logger.info("开始验证订单" + cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			if (co == null) {
				// 订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
			// 配送单是否允许做中转 yes 允许做中转， no不允许
			String isPeisongAllowtoZhongZhuan = this.systemInstallDAO.getSystemInstall("isPeisongAllowtoZhongZhuan") == null ? "yes" : this.systemInstallDAO
					.getSystemInstall("isPeisongAllowtoZhongZhuan").getValue();
			if ("no".equalsIgnoreCase(isPeisongAllowtoZhongZhuan) && (co.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Peisong_Bu_YunXu_ZhongZhuan);
			}
			Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
			Branch nextBranch = this.branchDAO.getQueryBranchByBranchid(co.getNextbranchid());
			boolean aflag = false;
			if ((ifBranch != null) && (ifBranch.getSitetype() == 2)) {
				List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentbranchid, branchid, 2);
				for (BranchRoute r : routelist) {
					if (branchid == r.getToBranchId()) {
						aflag = true;
					}
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&
				if ((co.getNextbranchid() != 0) && !aflag && (branchid > 0) && !forceOut) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
							.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&co.getNextbranchid()!=branchid
			} else if ((co.getNextbranchid() != 0) && (branchid > 0) && !forceOut) {
				long compariBranchid = nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid();
				if (compariBranchid != branchid) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
							.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
				}
			}

			if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
					.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid)) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
			}

			Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
			Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
			if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
			}

			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑     一票多件不在这里校验
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid)) && (co
					.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())  && 1 == (int)co.getSendcarnum()) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && 1 == (int)co.getSendcarnum() && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
			// ==================验证订单状态End=======================

			// =========中转订单合包时与订单直接中转出库做相同验证==============
			// ==================做和订单出站相同的限制====================
			if ((co.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getStartbranchid() != currentbranchid)) {
				this.validateStateTransfer(co, FlowOrderTypeEnum.ChuKuSaoMiao, "合包");
			}

			// ==================验证订单的包号=======================
			this.logger.info("开始验证订单的包号" + co.getPackagecode());
			if (this.baleCwbDAO.getBaleAndCwbCount(baleno, scancwb) == 0) {
				
			} else {
				if ((co.getPackagecode() != null) && !"".equals(co.getPackagecode())) {
					Bale coBale = this.baleDAO.getBaleOnway(co.getPackagecode());
					Branch nextbranch = this.branchDAO.getBranchByBranchid(coBale.getNextbranchid());
					if ((coBale != null) && (nextbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
						if (baleno.equals(co.getPackagecode())) {
							// 重复封包
							throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
						}
						if ((coBale.getBranchid() == user.getBranchid()) && (coBale.getBalestate() == BaleStateEnum.WeiFengBao.getValue())) {
							// 订单{0}已经在{1}包号中，确认重新封包吗?
							throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_ChongXinFengBao, cwb, co.getPackagecode());
						} else {
							String errorstate = "";
							for (BaleStateEnum ft : BaleStateEnum.values()) {
								if (coBale.getBalestate() == ft.getValue()) {
									errorstate = ft.getText();
								}
							}
							// 操作失败，此订单已经在{0}包号{1}!
							throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_Error1, cwb, co.getPackagecode(), errorstate);
						}
					}
				}
			}

			// 分站滞留的订单不允许操作中转出站
			if ((co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Fenzhanzhiliustatenotzhongzhanchuzhan);
			}

		}
	}

	/**
	 * 退供货商出库根据包号扫描订单检查
	 */
	@Transactional
	public void baleaddcwbToCustomerCheck(User user, String baleno, String cwb, long currentbranchid, long branchid) {
		if (!"".equals(baleno) && !"".equals(cwb)) {
			this.logger.info("===封包检查开始===");
			this.logger.info("开始验证包号" + baleno);
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			/*
			 * long count = this.orderBackCheckDAO.getOrderbackCheckss(cwb);
			 * if(count!=0){ throw new CwbException(cwb,
			 * FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
			 * ExceptionCwbErrorTypeEnum.Wei_Shen_he_huozhe_shen_he_butongguo);
			 * }
			 */
			// ==================验证包号=======================
			this.validateBaleCheck(user, baleno, cwb, branchid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
			// ==================验证包号End=======================

			// ==================验证订单状态=======================
			this.logger.info("开始验证订单" + cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			if (co == null) {
				// 订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			if ((co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) && (((co.getSendcarnum() >= 1) && (co.getSendcarnum() == co.getScannum())) || (co.getSendcarnum() == 0))) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
			}
			// ==================验证订单状态End=======================

			// ==================验证订单的包号=======================
			this.logger.info("开始验证订单的包号" + co.getPackagecode());
			this.validateCwbBaleCheck(user, baleno, scancwb, co, branchid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), currentbranchid, FlowOrderTypeEnum.TuiGongYingShangChuKu);

		}
	}

	/**
	 * 库房出库根据包号扫描订单
	 *
	 * @param baleno
	 * @param cwb
	 */
	@Transactional
	public CwbOrder baleaddcwb(User user, String baleno, String cwb, long branchid) {
		CwbOrder cwbOrder =null;
		if (!StringUtils.isEmpty(baleno) && !StringUtils.isEmpty(cwb)) {
			// 如果订单存在原来的包号 包号表的订单数-1
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);

			/* ***************add begin*********************/
			//add by neo01.huang，2016-7-19，转换session用户的branchid
			userService.convertSessionUserBranchId(user);
			/* ***************add end***********************/
			logger.info("库房出库根据包号扫描订单,包号："+baleno);
			Bale bale=this._baleaddcwb(user, baleno, cwb, scancwb, branchid);

			/**
			 * 广州通路按包操作性能问题 初步解决方案
			 */
			boolean iszhongzhuanout = false;
			Branch currentBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			// 判断是否中转出库
			if (BranchEnum.ZhongZhuan.getValue() == currentBranch.getSitetype()) {
				iszhongzhuanout = true;
			}
			cwbOrder = this.cwbOrderService.outWarehous(user, cwb, scancwb, 0, 0, branchid, 0, false, "", baleno, 0, iszhongzhuanout, false);
		
			//出库时有可能自动补环节令包失效,恢复它的状态
			this.restoreBaleState(bale);
			
			/* ***************add begin*********************/
			//add by neo01.huang，2016-7-19，确保出库后branchid不会被修改成0
			Bale nowBale=this.baleDAO.getBaleById(bale.getId());
			if (nowBale != null && nowBale.getBranchid() <= 0) {
				//转换session用户的branchid
				userService.convertSessionUserBranchId(user);
				logger.info("确保出库后branchid不会被修改成0->nowBale.branchId:{}, userBranchId:{}", 
						nowBale.getBranchid(), user.getBranchid());
				baleDAO.updateBranchid(bale.getId(), user.getBranchid());
			}
			/* ***************add end***********************/
			
		}
		return cwbOrder;
	}
	
	/**
	 * 退货出站根据包号扫描订单
	 *
	 * @param baleno
	 * @param cwb
	 */
	@Transactional
	public void baletuihuochuzhanaddcwb(User user, String baleno, String cwb, long branchid) {
		if (!StringUtils.isEmpty(baleno) && !StringUtils.isEmpty(cwb)) {
			// 如果订单存在原来的包号 包号表的订单数-1
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
            logger.info("退货出站根据包号扫描订单,包号：" + baleno);
			Bale bale=this._baleaddcwb(user, baleno, cwb, scancwb, branchid);

			this.cwbOrderService.outUntreadWarehous(user, cwb, scancwb, 0, 0, branchid, 0, false, "", baleno, false);
		
			this.restoreBaleState(bale);
		}
	}

	/**
	 * 退供货商出库根据包号扫描订单
	 *
	 * @param baleno
	 * @param cwb
	 */
	@Transactional
	public void baletuigonghuoshangchukuaddcwb(User user, String baleno, String cwb, long branchid) {
		if (!StringUtils.isEmpty(baleno) && !StringUtils.isEmpty(cwb)) {
			// 如果订单存在原来的包号 包号表的订单数-1
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
            logger.info("退供货商出库根据包号扫描订单,包号："+baleno);
			Bale bale=this._baleaddcwb(user, baleno, cwb, scancwb, branchid);

			this.cwbOrderService.backtocustom(user, cwb, scancwb, 0, baleno, true);
			
			this.restoreBaleState(bale);
		}
	}
	
	/**
	 * 分拣中转出库根据包号扫描订单
	 *
	 * @param user
	 * @param baleno
	 * @param cwb
	 * @param branchid
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	@Transactional
	public void sortAndChangeBaleAddCwb(User user, String baleno, String cwb, long branchid) {
		if (!StringUtils.isEmpty(baleno) && !StringUtils.isEmpty(cwb)) {

			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
            logger.info("分拣中转出库根据包号扫描订单,包号：" + baleno);
			Bale bale=this._baleaddcwb(user, baleno, cwb, scancwb, branchid);

			this.cwbOrderService.sortAndChangeOutWarehouse(user, cwb, scancwb, 0, 0, branchid, 0, false, "", baleno, 0, false, false);
		
			this.restoreBaleState(bale);
		}
	}

	private Bale _baleaddcwb(User user, String baleno, String cwb, String scancwb, long branchid) {
		// 如果订单存在原来的包号 包号表的订单数-1
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		//此段代码根据没必要执行，因为新方案下每次都会新建一条包记录。之前都是利用同一条包记录。
		/*
		if(false){
			if ((co != null) && !StringUtils.isEmpty(co.getPackagecode())) {
				Bale baleOld = this.baleDAO.getBaleOneByBaleno(co.getPackagecode());
				if(baleOld!=null){
					this.baleDAO.updateSubBaleCount(co.getPackagecode());
					// 删除包号订单关系表数据
					if (baleOld.getBalestate() == BaleStateEnum.WeiFengBao.getValue()) {
						this.baleCwbDAO.deleteByBaleidAndCwb(baleOld.getId(), scancwb);
					}
		
					// 如果该订单之前封包过并且状态已完结，则需要删除之前的包的关系 ，已到货说明完结
					if ((baleno != co.getPackagecode()) && (baleOld.getBalestate() == BaleStateEnum.YiDaoHuo.getValue()) && (baleOld.getBranchid() != branchid)) {
						this.baleCwbDAO.deleteByBaleidAndCwb(baleOld.getId(), scancwb);
					}
				}
			}
		}
		*/

		long baleid=0;
		Bale bale = this.baleDAO.getBaleWeifengbao(baleno);
		if (bale == null) {
			this.logger.info("创建包号" + baleno);
			Bale o = new Bale();
			o.setBaleno(baleno);
			o.setBranchid(user.getBranchid());
			o.setBalestate(BaleStateEnum.WeiFengBao.getValue());
			o.setNextbranchid(branchid);
			o.setCwbcount(1);
			baleid = this.baleDAO.createBale(o);
			// 添加包号和订单的关系表
			this.baleCwbDAO.createBale(baleid, baleno, scancwb);
		} else {
			this.baleDAO.updateAddBaleCount(bale.getId());// 更新订单数
			// 添加包号和订单的关系表
			this.baleCwbDAO.createBale(bale.getId(), baleno, scancwb);
			baleid=bale.getId();
		}
		bale=this.baleDAO.getBaleById(baleid);
		
		this.logger.info("更新订单:" + cwb + "的包号为:" + baleno + "，下一站为:" + branchid);
		// 更新订单表的包号、下一站
		this.cwbDAO.updatePackagecodeAndNextbranchid(baleno, branchid, cwb);

		// 如果为快递单，则向jms发送运单状态反馈 --刘武强10.20
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			if (co.getDeliverystate() == DeliveryStateEnum.DaiZhongZhuan.getValue()) {
				// 马哥说这个应该会不用了，快递所有的打包中，除了揽件合包，其他都废弃
			} else {
				Branch nextBranch = this.branchDAO.getBranchByBranchid(branchid);
				this.executeTpsTransInterface(co, user, nextBranch.getTpsbranchcode());
			}
		}
		
		return bale;
	}

	/**
	 * 调用tps运单反馈接口
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(CwbOrder order, User user, String branchCode) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo(order.getCwb());
		transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());
		transNoFeedBack.setOperater(user.getRealname());
		transNoFeedBack.setNextOrg(branchCode);
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.OutboundScan.getValue());
		transNoFeedBack.setReason("");

		/*
		 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
		 * contextVar.setOperationType
		 * (TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
		 * contextVar.setStation(branch.getBranchname());//站点名称
		 * contextVar.setOperator(user.getRealname());
		 * contextVar.connectMessage();
		 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
		 */
		paramObj.setTransNoFeedBack(transNoFeedBack);
		// 发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);

	}

	/**
	 * 封包操作
	 *
	 * @param user
	 * @param baleno
	 */
	@Transactional
	public Bale fengbao(User user, String baleno, long branchid) {
		Bale bale = null;
		if (!"".equals(baleno)) {
			this.logger.info("====开始封包" + baleno + "====");
			bale = this.baleDAO.getBaleOnwayByLock(baleno);
			if (bale == null) {
				// 包号不存在
				throw new CwbException("", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BaoHaoBuZhengQue);
			}
			
			/* ***************add begin*********************/
			//add by neo01.huang，2016-7-19，转换session用户的branchid
			userService.convertSessionUserBranchId(user);
			/* ***************add end***********************/

			// 非本站包
			if (bale.getBranchid() != user.getBranchid()) {
				throw new CwbException("", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_Fei_Ben_Zhan);
			}

			if ((branchid > 0) && (bale.getNextbranchid() != branchid)) {
				// 不是这个目的站的包
				throw new CwbException("", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_BU_SHI_ZHE_GE_MU_DI, this.branchDAO.getBranchByBranchid(bale.getNextbranchid())
						.getBranchname());
			}

			// 只有未封包||已封包的包号可以封包
			if ((bale.getBalestate() != BaleStateEnum.WeiFengBao.getValue()) && (bale.getBalestate() != BaleStateEnum.YiFengBao.getValue())) {
				String errorstate = "";
				for (BaleStateEnum ft : BaleStateEnum.values()) {
					if (bale.getBalestate() == ft.getValue()) {
						errorstate = ft.getText();
					}
				}
				// 操作失败，{0}包号{1}!
				throw new CwbException("", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_Error, bale.getBaleno(), errorstate);
			}

			// 包号下无订单
			long successCount = bale.getCwbcount();
			if (successCount == 0) {
				throw new CwbException("", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Bale_Error, baleno, "没有订单");
			}
			this.logger.info("用户:" + user.getRealname() + "，封包" + baleno);
			// 封包
			this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.YiFengBao.getValue());
		}
		return bale;
	}

	// 按包出库页面的显示
	public List<BaleView> getCwbOrderCustomerView(List<CwbOrder> clist, List<Customer> customerList) {
		List<BaleView> baleViewList = new ArrayList<BaleView>();
		if ((clist != null) && !clist.isEmpty()) {
			for (CwbOrder c : clist) {
				BaleView o = new BaleView();
				o.setCwb(c.getCwb());
				o.setCustomerid(c.getCustomerid());
				o.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				o.setNextbranchid(c.getNextbranchid());
				o.setEmaildate(c.getEmaildate());
				o.setConsigneename(c.getConsigneename());
				o.setReceivablefee(c.getReceivablefee());
				o.setConsigneeaddress(c.getConsigneeaddress());
				o.setCwbremark(c.getCwbremark());
				o.setErrorreasion(c.getRemark1());
				baleViewList.add(o);
			}
		}
		return baleViewList;
	}

	/**
	 * 中转出库根据包号扫描订单检查
	 *
	 * @param user
	 * @param baleno
	 * @param cwb
	 * @param forceOut
	 * @param currentbranchid
	 * @param branchid
	 *
	 * @author jinghui.pan@pjbest.com
	 * @version DMP2.4.7
	 */
	@Transactional
	public void baleaddcwbZhongzhuanChuKuCheck(User user, String baleno, String cwb, boolean forceOut, long currentbranchid, long branchid) {
		if (!"".equals(baleno) && !"".equals(cwb)) {
			this.logger.info("===封包检查开始===");
			this.logger.info("开始验证包号" + baleno);

			// ==================验证包号=======================
			this.validateBaleCheck(user, baleno, cwb, branchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
			// ==================验证包号End=======================

			// ==================验证订单状态=======================
			this.logger.info("开始验证订单" + cwb);
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			if (co == null) {
				// 订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
			long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
			if ((isypdjusetranscwb == 1) && (null != co.getTranscwb()) && (co.getTranscwb().length() > 0)) {
				this.validateIsSubCwb(scancwb, co, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
			}

			Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
			Branch nextBranch = this.branchDAO.getQueryBranchByBranchid(co.getNextbranchid());
			boolean aflag = false;
			if ((ifBranch != null) && (ifBranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
				List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentbranchid, branchid, 2);
				for (BranchRoute r : routelist) {
					if (branchid == r.getToBranchId()) {
						aflag = true;
					}
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&
				if ((co.getNextbranchid() != 0) && !aflag && (branchid > 0) && !forceOut) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
							.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
				}// co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&co.getNextbranchid()!=branchid
			} else if ((co.getNextbranchid() != 0) && (branchid > 0) && !forceOut) {
				// 计算下一站
				long compariBranchid = nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid();
				// 计算的下一站！=所选的下一站 && 计算的下一站!=0
				List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(branchid, co.getDeliverybranchid(), 2);
				if (null == routelist) {
					if ((compariBranchid != branchid) && (compariBranchid != 0)) {
						throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
								.getBranchByBranchid(nextBranch.getSitetype() == BranchEnum.ZhanDian.getValue() ? co.getNextbranchid() : co.getDeliverybranchid()).getBranchname());
					}
				}
			}

			if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
					.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid)) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
			}

			Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
			Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
			if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
			}

			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid)) && ((co
					.getSendcarnum() > 1) && (co.getSendcarnum() == co.getScannum())) && (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) && ((co.getSendcarnum() > 1) && (co.getSendcarnum() == co
					.getScannum())) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
			// ==================验证订单状态End=======================

			// ==================验证订单的包号=======================
			this.logger.info("开始验证订单的包号" + co.getPackagecode());
			this.validateCwbBaleCheck(user, baleno, scancwb, co, branchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), currentbranchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu);
		}

	}

	/**
	 * 中转库出库：包添加订单
	 *
	 * @param user
	 * @param baleno
	 *            包号
	 * @param cwb
	 *            订单号
	 * @param branchid
	 *            站点
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	@Transactional
	public void baleZhongZhuanChuKuAddCwb(User user, String baleno, String cwb, long branchid) {
		if (!StringUtils.isEmpty(baleno) && !StringUtils.isEmpty(cwb)) {

			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);

			Bale bale=this._baleaddcwb(user, baleno, cwb, scancwb, branchid);

			this.cwbOrderService.changeoutWarehous(user, cwb, scancwb, 0, 0, branchid, 0, false, "", baleno, 0, false, false);
		
			this.restoreBaleState(bale);
		}
	}
	
	@Transactional
	public Bale getBaleForHebaoDaohuo(HttpServletRequest request,String baleno,FlowOrderTypeEnum flowOrderTypeEnum){
		//store the  bale id in session
		Object [] baleidInfo= (Object []) request.getSession().getAttribute("BALEID_"+flowOrderTypeEnum.getValue());
		Long baleid=null;
		Bale bale = null;
		baleno=baleno==null?"":baleno.trim();
		if(baleidInfo!=null&&baleidInfo.length>1&&baleidInfo[0]!=null&&baleidInfo[1]!=null){
			if(((String)baleidInfo[0]).equals(baleno)&&baleno.length()>0){
				baleid=(Long) baleidInfo[1];
			}
		}
		
		if(baleid==null||baleid.longValue()<1){
			bale = this.baleDAO.getBaleOnway(baleno);
			if(bale!=null){
				baleid=bale.getId();
				request.getSession().setAttribute("BALEID_"+flowOrderTypeEnum.getValue(),new Object[]{baleno,baleid});
			}
		}else{
			bale = this.baleDAO.getBaleById(baleid);
		}
		
		return bale;
	}
	
	@Transactional
	public void disableBale(String scancwb){
		if(scancwb==null||scancwb.length()<1){
			return;
		}
		//类似于setExpressPackageUnable()
		//有可能出库时合包，到站却不做合包到货
		Bale bale=baleDAO.getBaleOnwayBycwb(scancwb);
		if(bale==null){
			String cwb=this.cwbOrderService.translateCwb(scancwb);
			if(!scancwb.equals(cwb)){
				bale=baleDAO.getBaleOnwayBycwb(cwb);
			}
			
			//add by jianrong.gao at 2010-08-08
			//合包时扫的是箱号，但到站时扫的是订单号(一票一件时允许扫订单号)
			if(bale==null){
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				if(co!=null){
					if(co.getSendcarnum()==1||co.getBackcarnum()==1){
						List<TranscwbView> transcwbList=transCwbDao.getTransCwbByCwb(cwb);
						if(transcwbList!=null&&transcwbList.size()>0&&transcwbList.size()<3){
							String transcwb=null;
							for(TranscwbView v:transcwbList){
								if(!cwb.equals(v.getTranscwb())){
									transcwb=v.getTranscwb();
									break;
								}
							}
							if(transcwb!=null){
								bale=baleDAO.getBaleOnwayBycwb(transcwb);
							}
						}
					}
				}
			}
		}
		
		if(bale!=null){
			this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.BuKeYong.getValue());
		}
	}

	//检查扫描号是否在包里
	public boolean inBale(long baleid,String scancwb){
		boolean result=false;
		String cwb=this.cwbOrderService.translateCwb(scancwb);
		if(cwb!=null&&cwb.length()>0){
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			if(co!=null){
				if(!cwb.equals(scancwb)){
					//强制扫运单号时必然进入此处
					cwb="'"+cwb+"','"+scancwb+"'";
				}else{
					cwb="'"+cwb+"'";
					List<String> transcwbList=this.cwbOrderService.getTranscwbList(co.getTranscwb());
					if(transcwbList!=null&&transcwbList.size()>0){
						for(String transcwb:transcwbList){
							cwb=cwb+",'"+transcwb+"'";
						}
					}
				}
				String hebaoCwb=this.baleCwbDAO.getScancwbByCwbs(baleid, cwb);
				if(hebaoCwb!=null&&hebaoCwb.trim().length()>0){
					result=true;
				}
			}
			
		}
		return result;
	}

	private void restoreBaleState(Bale bale){
		//出库时有可能自动补环节令包失效,恢复它的状态
		Bale nowBale=this.baleDAO.getBaleById(bale.getId());
		if(bale.getBalestate()!=nowBale.getBalestate()){
			this.baleDAO.updateBalesate(bale.getId(), bale.getBalestate());
		}
	}
	
	/**
	 *  按订单查询运单对应的包号
	 * @date 2016年7月13日 下午5:29:38
	 * @param cwbList
	 * @return
	 */
	public List<BaleCwbClassifyVo> getBaleCwbClassifyVoList(CwbOrder order) {
		List<BaleCwbClassifyVo> voList = new ArrayList<BaleCwbClassifyVo>();
		if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1))
				&& (order.getTranscwb().contains(",") || order.getTranscwb().contains(":"))) { // 一票多件
			// 获取运单号
			String[] transcwbArray = order.getTranscwb().split(",|:");
			List<String> transcwbs= new ArrayList<String>();
			for (String transcwb : transcwbArray) {
				transcwbs.add(transcwb);
			}
			// 查询包
			List<BaleCwb> baleCwbList = this.baleCwbDAO.getLastBaleCwbList(transcwbs);
			if(baleCwbList != null && baleCwbList.size() > 0) {
				// 根据订单号分类
				Map<String, List<String>> baleCwbMap = new LinkedHashMap<String, List<String>>(); //保证排序
				for(BaleCwb baleCwb : baleCwbList) {
					List<String> baleCwbs = baleCwbMap.get(baleCwb.getBaleno());
					if(baleCwbs == null) {
						baleCwbs = new ArrayList<String>();
					}
					baleCwbs.add(baleCwb.getCwb());
					baleCwbMap.put(baleCwb.getBaleno(), baleCwbs);
					transcwbs.remove(baleCwb.getCwb());
				}
				// 转VO
				for(Map.Entry<String, List<String>> entry : baleCwbMap.entrySet()) {
					BaleCwbClassifyVo vo = new BaleCwbClassifyVo();
					vo.setBaleno(entry.getKey());
					vo.setTranscwbList(entry.getValue());
					voList.add(vo);
				}
				//添加未匹配的包号
				if (transcwbs.size() > 0) {
					BaleCwbClassifyVo vo = new BaleCwbClassifyVo();
					vo.setBaleno("");
					vo.setTranscwbList(transcwbs);
					voList.add(vo);
				}
			} else { // 如果没有查到，则使用绑定的包号
				BaleCwbClassifyVo vo = new BaleCwbClassifyVo();
				vo.setBaleno(order.getPackagecode());
				vo.setTranscwbList(new ArrayList<String>(transcwbs));
				voList.add(vo);
			}
			
		}
		if(voList.isEmpty()) { //非一票多件，则直接使用绑定的运单号和包号
			BaleCwbClassifyVo vo = new BaleCwbClassifyVo();
			List<String> transcwbList = new ArrayList<String>();
			transcwbList.add(order.getTranscwb());
			vo.setBaleno(order.getPackagecode());
			vo.setTranscwbList(transcwbList);
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 根据包号查询订单与包的集合
	 * @date 2016年7月13日 下午5:33:52
	 * @param cwbList
	 * @param baleno
	 * @return
	 */
	public Map<String, BaleCwbClassifyVo> getBaleCwbClassifyVoMapByBaleno(List<String> cwbList, String baleno) {
		Set<String> cwbSet = new HashSet<String>(cwbList);
		Map<String,BaleCwbClassifyVo> voMap = new HashMap<String, BaleCwbClassifyVo>();
		if(cwbList == null || cwbList.size() == 0) {
			return voMap;
		}
		// 查询最近一次使用的包
		Bale bale = this.baleDAO.getLastBaleByBaleno(baleno);
		if (bale != null) {
			List<BaleCwb> baleCwbList = this.baleCwbDAO.getBaleCwbListByBaleId(bale.getId());
			for(BaleCwb baleCwb : baleCwbList) { // 根据订单号分类
				String cwb = this.cwbOrderService.translateCwb(baleCwb.getCwb());
				if(cwbSet.contains(cwb)) {
					BaleCwbClassifyVo vo = voMap.get(cwb);
					if(vo == null) {
						vo = new BaleCwbClassifyVo();
						vo.setBaleno(baleno);
					}
					vo.getTranscwbList().add(baleCwb.getCwb());
					voMap.put(cwb, vo);
				}
			}
		}
		for(String cwb : cwbSet) { //处理未匹配的订单
			BaleCwbClassifyVo vo = voMap.get(cwb);
			if(vo == null) {
				vo = new BaleCwbClassifyVo();
				vo.setBaleno(baleno);
				List<String> transcwbList = new ArrayList<String>();
				transcwbList.add(""); // 运单号未空
				vo.setTranscwbList(transcwbList);
				voMap.put(cwb, vo);
			}
		}
		return voMap;
	}

	/**
	 * 出库扫描包号处理
	 * @param user 当前登录的用户
	 * @param isbale 扫描的包
	 * @param cwb 这里是包号
	 * @param branchid 下一站id
	 * @param cwbList 与包号关联的订单号list
	 * @param driverid 驾驶员id
	 * @param truckid 车辆id
	 * @param confirmflag 强制出库
	 * @param cwbs 模拟订单号文本框
	 * @param batchCount 结果统计计数器
	 * @author neo01.huang，2016-7-25
	 */
	@Transactional
	public void cwbexportwarhouseScanBaleNo(User user, Bale isbale, String cwb, long branchid, List<String> cwbList,
			long driverid, long truckid, long confirmflag, StringBuilder cwbs, BatchCount batchCount) {
		// 更新包的下一站为用户选择的下一站，当前站为0
		this.baleDAO.updateBranchIdAndNextBranchId(isbale.getId(), branchid, 0);

		for (String cwbStr : cwbList) {
			cwbs.append(cwbStr).append("\r\n");
		}
		
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		cwbOrderService.exportHouseForExpressPackage(user,cwbs.toString(), branchid, driverid, truckid, confirmflag, batchCount, cList, objList);
		
	}
}
