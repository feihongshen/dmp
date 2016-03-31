package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbTranslator;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/batchselectcwb")
@Controller
public class BatchSelectCwbController {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, HttpServletRequest request, @PathVariable(value = "page") long page, @RequestParam(value = "batchcwb", required = false, defaultValue = "") String batchcwb, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		Page pageparm = new Page();
		List<CwbOrder> orderList = new ArrayList<CwbOrder>();
		CwbOrder cosum = new CwbOrder();
		long count = 0;
		if (isshow != 0) {
			if (batchcwb.length() == 0) {
				model.addAttribute("page_obj", pageparm);
				return "bacthselectcwb/list";
			}
			String[] cwbs = batchcwb.trim().split("\r\n");
			List<String> cwbStrList = new ArrayList<String>();
			for (int i = 0; i < cwbs.length; i++) {
				if (cwbs[i].trim().length() == 0) {
					continue;
				}
				if (cwbs[i].trim().length() > 0) {
					if (!cwbStrList.contains(cwbs[i].trim())) {
						cwbStrList.add(cwbs[i].trim());
					}
				}
			}

			String temporders = this.dataStatisticsService.getStrings(cwbStrList);
			List<TranscwbView> tempList = this.transCwbDao.getTransCwbByTranscwb(temporders);
			Map<String, String> transcwbAndCwbMap = new HashMap<String, String>();
			for (TranscwbView transcwbView : tempList) {
				transcwbAndCwbMap.put(transcwbView.getTranscwb(), transcwbView.getCwb());
			}
			String lastcwbs = "";
			for (String cwb : cwbStrList) {
				if (transcwbAndCwbMap.containsKey(cwb)) {
					cwb = transcwbAndCwbMap.get(cwb);
				}
				lastcwbs += "'" + cwb + "',";
			}
			if (lastcwbs.length() > 0) {
				lastcwbs = lastcwbs.substring(0, lastcwbs.length() - 1);
			}

			orderList = this.cwbDAO.getCwbByCwbsPage(page, lastcwbs);

			cosum = this.cwbDAO.getcwborderSumBycwbs(lastcwbs);
			count = this.cwbDAO.getCwbOrderCwbsCount(lastcwbs);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUserByuserDeleteFlag();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			cwbOrderView = this.getCwbOrderView(orderList, customerList, customerWareHouseList, branchList, userList, reasonList, "", "", remarkList);
		}
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("cosum", cosum);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", pageparm);
		this.logger.info("订单批量查询，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return "bacthselectcwb/list";
	}

	private String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}

	@RequestMapping("/batchSelectExpore")
	public void batchSelectExpore(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "batchcwb2", required = false, defaultValue = "") String batchcwb2, @RequestParam(value = "exportmould2", required = false, defaultValue = "") final String mouldfieldids2) {
		String[] cwbs = batchcwb2.trim().split("\r\n");

		List<String> cwbStrList = new ArrayList<String>();
		for (int i = 0; i < cwbs.length; i++) {
			if (cwbs[i].trim().length() == 0) {
				continue;
			}
			if (cwbs[i].trim().length() > 0) {
				if (!cwbStrList.contains(cwbs[i].trim())) {
					cwbStrList.add(cwbs[i].trim());
				}
			}
		}

		String temporders = this.dataStatisticsService.getStrings(cwbStrList);
		List<TranscwbView> tempList = this.transCwbDao.getTransCwbByTranscwb(temporders);
		Map<String, String> transcwbAndCwbMap = new HashMap<String, String>();
		for (TranscwbView transcwbView : tempList) {
			transcwbAndCwbMap.put(transcwbView.getTranscwb(), transcwbView.getCwb());
		}
		String lastcwbs = "";
		for (String str : cwbStrList) {
			if (transcwbAndCwbMap.containsKey(str)) {
				str = transcwbAndCwbMap.get(str);
			}
			lastcwbs += "'" + str + "',";
		}
		if (lastcwbs.length() > 0) {
			lastcwbs = lastcwbs.substring(0, lastcwbs.length() - 1);
		}

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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀

		fileName = fileName + otherName + lastStr;
		try {
			final String sql = this.cwbDAO.getSqlByCwb(lastcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = BatchSelectCwbController.this.userDAO.getUserForALL();
					final Map<Long, Customer> cMap = BatchSelectCwbController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = BatchSelectCwbController.this.branchDAO.getAllBranches();
					final List<Common> commonList = BatchSelectCwbController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = BatchSelectCwbController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = BatchSelectCwbController.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = BatchSelectCwbController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = BatchSelectCwbController.this.reasonDAO.getAllReason();
					BatchSelectCwbController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							logger.info("行数据：" + rs);
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
								Object a = BatchSelectCwbController.this.exportService
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
								Map<String, Map<String, String>> orderflowList = BatchSelectCwbController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : BatchSelectCwbController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : BatchSelectCwbController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : BatchSelectCwbController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
			logger.error("", e);
		}
	}

	@RequestMapping("/batchRemark")
	public @ResponseBody String batchRemark(@RequestParam("remarkcwbs") String remarkcwbs, @RequestParam("cwbremark") String cwbremark) {
		try {
			this.cwbOrderService.cwbremark(remarkcwbs, cwbremark, this.getSessionUser());
			return "{\"errorCode\":0,\"error\":\"备注成功\"}";
		} catch (Exception e) {
			this.logger.error("批量备注异常：", e);
			return "{\"errorCode\":1,\"error\":\"备注失败\"}";
		}
	}

	public List<CwbOrderView> getCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList, List<Reason> reasonList, String begindate, String enddate, List<Remark> remarkList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				List<String> cwbs = new ArrayList<String>();
				cwbs.add(c.getCwb());
				Map<String, Map<String, String>> orderflowList = this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, branchList);

				CwbOrderView cwbOrderView = new CwbOrderView();

				cwbOrderView.setCwb(c.getCwb());
				cwbOrderView.setEmaildate(c.getEmaildate());
				cwbOrderView.setCarrealweight(c.getCarrealweight());
				cwbOrderView.setCarsize(c.getCarsize());
				cwbOrderView.setSendcarnum(c.getSendcarnum());
				cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
				cwbOrderView.setConsigneename(c.getConsigneename());
				cwbOrderView.setConsigneemobile(c.getConsigneemobile());
				cwbOrderView.setConsigneephone(c.getConsigneephone());
				cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());

				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(c.getCustomerwarehouseid() == "" ? "0" : c.getCustomerwarehouseid()));
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
				cwbOrderView.setPaytype(this.getPayWayType(c.getCwb(), c.getNewpaywayid()));// 新支付方式
				cwbOrderView.setPaytype_old(this.getOldPayWayType(c.getPaywayid()));// 原支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setReturngoodsremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), begindate, enddate).getComment());
				String ruku = "";
				String chukusaomiao = "";
				String daohuosaomiao = "";
				String fenzhanlinghuo = "";
				String yifankui = "";
				String zuixinxiugai = "";
				String yishenhe = "";
				if (null != orderflowList.get(c.getCwb())) {
					ruku = orderflowList.get(c.getCwb()).get("Instoreroomtime");
					chukusaomiao = orderflowList.get(c.getCwb()).get("Outstoreroomtime");
					daohuosaomiao = orderflowList.get(c.getCwb()).get("InSitetime");
					fenzhanlinghuo = orderflowList.get(c.getCwb()).get("PickGoodstime");
					yifankui = orderflowList.get(c.getCwb()).get("Gobacktime");
					zuixinxiugai = orderflowList.get(c.getCwb()).get("Newchangetime");
					yishenhe = orderflowList.get(c.getCwb()).get("Goclasstime");
				}

				cwbOrderView.setInstoreroomtime(ruku != null ? ruku : "");// 入库时间
				cwbOrderView.setOutstoreroomtime(chukusaomiao != null ? chukusaomiao : "");// 出库时间
				cwbOrderView.setInSitetime(daohuosaomiao != null ? daohuosaomiao : "");// 到站时间
				cwbOrderView.setPickGoodstime(fenzhanlinghuo != null ? fenzhanlinghuo : "");// 小件员领货时间
				cwbOrderView.setGobacktime(yifankui != null ? yifankui : "");// 反馈时间
				cwbOrderView.setGoclasstime(yishenhe == null ? "" : yishenhe);// 归班时间
				cwbOrderView.setNowtime(zuixinxiugai != null ? zuixinxiugai : "");// 最新修改时间
				cwbOrderView.setBackreason(c.getBackreason());
				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setInwarhouseremark(this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
						.get(ReasonTypeEnum.RuKuBeiZhu.getText()));
				cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (yifankui != null ? yifankui : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), begindate, enddate).getComment());
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) && (yifankui != null)) {
						cwbOrderView.setSendSuccesstime(yifankui);// 配送成功时间
					}
				}
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype, String begindate, String enddate) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
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

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		deliveryStateList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
		return deliverState;
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public String getOldPayWayType(long payupid) {
		String str = "";
		for (PaytypeEnum pe : PaytypeEnum.values()) {
			if (payupid == pe.getValue()) {
				str = pe.getText();
			}
		}

		return str;
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

	public String getPayWayType(String cwb, String newpaywayid) {
		StringBuffer str = new StringBuffer();
		String paywaytype = "";
		for (String newpayway : newpaywayid.split(",")) {
			for (PaytypeEnum pe : PaytypeEnum.values()) {
				if (Long.parseLong(newpayway) == pe.getValue()) {
					str.append(pe.getText()).append(",");
				}
			}
		}

		if (str.length() > 0) {
			paywaytype = str.substring(0, str.length() - 1);
		} else {
			paywaytype = "现金";
		}
		return paywaytype;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
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
}
