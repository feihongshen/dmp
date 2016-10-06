package cn.explink.service;

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

import net.sf.json.JSONObject;

import org.apache.camel.Header;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExcelImportEditDao;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.NoPiPeiCwbDetailDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

@Service
public class DataImportService {

	private Logger logger = LoggerFactory.getLogger(DataImportService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	EmailDateDAO emailDateDAO;

	@Autowired
	ExportmouldDAO exportmouldDAO;

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	ImportValidationManager importValidationManager;

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	RemarkDAO remarkDao;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;

	@Autowired
	ResultCollectorManager resultCollectorManager;

	@Autowired
	CwbRouteService cwbRouteService;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	NoPiPeiCwbDetailDAO noPiPeiCwbDetailDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Produce(uri = "jms:queue:cwborderinsert")
	ProducerTemplate importCwbErrorProducer;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;

	@Autowired
	ExcelImportEditDao excelImportEditDao;
	@Autowired
	TransCwbDetailDAO transCwbDetailDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	@Autowired
	private DataImportSingleService dataImportSingleService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public void importData(final List<CwbOrderDTO> cwbOrderDTOs, // 供货商
			User user, // 操作员
			ResultCollector errorCollector, EmailDate ed, boolean isReImport) {
		this.logger.info("开始导入,emaildate:{}", ed.getEmaildateid());
		for (CwbOrderDTO cwbOrderDTO : cwbOrderDTOs) {
			this.logger.info("importing order. cwb = {}", cwbOrderDTO.getCwb());
			if (errorCollector.isStoped()) {
				this.logger.info("导入中止,emaildate:{}", ed.getEmaildateid());
				break;
			}
			try {
				this.importSingleData(user, ed, isReImport, cwbOrderDTO);
				// 成功订单数+1 前台显示
				errorCollector.setSuccessSavcNum(errorCollector.getSuccessSavcNum() + 1);
				this.logger.info("importing order success. cwb = {}", cwbOrderDTO.getCwb());
			} catch (Exception e) {
				this.logger.info("importing order failed. cwb = {}", cwbOrderDTO.getCwb());
				logger.error("", e);
				errorCollector.addError(cwbOrderDTO.getCwb(), e.getMessage());

				// 失败订单数+1 前台显示
				errorCollector.setFailSavcNum(errorCollector.getFailSavcNum() + 1);
				// 存储报错订单，以便统计错误记录和处理错误订单
				JSONObject errorOrder = new JSONObject();
				errorOrder.put("cwbOrderDTO", cwbOrderDTO);
				errorOrder.put("emaildateid", ed.getEmaildateid());
				errorOrder.put("customerid", ed.getCustomerid());
				errorOrder.put("message", e.getMessage());

				try {
					logger.info("消息发送端：importCwbErrorProducer, errorOrder={}", errorOrder.toString());
					this.importCwbErrorProducer.sendBodyAndHeader(null, "errorOrder", errorOrder.toString());
				} catch (Exception ee) {
					logger.error("", ee);
					//写MQ异常表
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".importData")
							.buildExceptionInfo(e.toString()).buildTopic(this.importCwbErrorProducer.getDefaultEndpoint().getEndpointUri())
							.buildMessageHeader("errorOrder", errorOrder.toString()).getMqException());
				}
			}
		}
		this.emailDateDAO.editEditEmaildateForCwbcount(errorCollector.getSuccessSavcNum(), ed.getEmaildateid());
		this.logger.info("导入结束,emaildate:{}", ed.getEmaildateid());
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void importSingleData(User user, EmailDate ed, boolean isReImport, CwbOrderDTO cwbOrderDTO) {
		/**
		 * 本方法存在内部调用，而方法内部调用无法产生新的事务，抽象出来，使数据库操作部分支持事务
		 * add by chunlei05.li 2016/8/23
		 */
		this.dataImportSingleService.importSingleData(user, ed, isReImport, cwbOrderDTO);
		
		// 如果手动分配了地址，就不会自动调用地址库
		if ((cwbOrderDTO.getExcelbranch() == null) || (cwbOrderDTO.getExcelbranch().length() == 0) || (cwbOrderDTO.getDeliverybranchid() == 0)) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("cwb", cwbOrderDTO.getCwb());
			map.put("userid", user.getUserid());
			try{
				logger.info("消息发送端：addressmatch, header={}", map.toString());
				addressmatch.sendBodyAndHeaders(null, map);
			}catch(Exception e){
				logger.error("", e);
				//写MQ异常表
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".importSingleData")
						.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeaderObject(map).getMqException());
			}
		}
	}

	/**
	 * 发送地址库消息
	 *
	 * @param user
	 * @param cwborderList
	 */
	@Transactional
	public void resendAddressmatch(User user, List<CwbOrder> cwborderList) {
		for (CwbOrder cwbOrder : cwborderList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", user.getUserid());
				logger.info("消息发送端：addressmatch, header={}", map.toString());
				this.addressmatch.sendBodyAndHeaders(null, map);
			} catch (Exception e) {
				logger.error("", e);
				this.logger.error(cwbOrder.getCwb() + "匹配站点失败");
				//写MQ异常表
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".resendAddressmatch")
						.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeaderObject(map).getMqException());
		
			}
		}
	}

	/**
	 * 正常导入
	 *
	 * @param cwbOrderDTO
	 * @param customerid
	 *            供货商
	 * @param branchid
	 * @param operatoruserid操作员
	 */
	private void insertCwb(final CwbOrderDTO cwbOrderDTO, final long customerid, long warhouseid, User user, EmailDate ed, boolean isReImport) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwbOrderDTO.getCwb());
		if (cwbOrder == null) {
			this.cwbOrderService.insertCwbOrder(cwbOrderDTO, customerid, warhouseid, user, ed);
			return;
		}
		
		// 只有flowordertype 为1才允许再导入 add by jian_xie 2016-08-24
		if(cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()){
			throw new RuntimeException("flowordertype不为1不允许导入");
		}
		
		if (cwbOrder.getEmaildateid() > 0) {
			if (!isReImport) {
				throw new RuntimeException("重复单号");
			} else if (ed.getEmaildateid() != cwbOrder.getEmaildateid()) {
				throw new RuntimeException("重复单号");
			}
		}
		this.cwbOrderService.updateExcelCwb(cwbOrderDTO, customerid, warhouseid, user, ed, isReImport);
	}

	@Produce(uri = "jms:topic:loseCwb")
	ProducerTemplate loseCwb;

	public void datalose(long emaildateid) {
		try {
			String header = "{\"emaildateid\":" + emaildateid + "}";
			logger.info("消息发送端：loseCwb, loseCwbByEmaildateid={}", header);
			this.loseCwb.sendBodyAndHeader(null, "loseCwbByEmaildateid", header);
		} catch (Exception e) {
		}
	}

	// @Consume(uri="jms:queue:VirtualTopicConsumers.dmp.updateOrderMoney")
	public void updateOrderMoney(@Header("cwbAndMoney") String cwbAndMoney) {
		JSONObject cam = JSONObject.fromObject(cwbAndMoney);
		if ((cam != null) && !cam.isNullObject() && (cam.get("cwb") != null) && (cam.get("money") != null)) {

			String sql = "update express_ops_cwb_detail set receivablefee=? where cwb=? and state=1";

			this.jdbcTemplate.update(sql, cam.getString("money"), cam.getString("cwb"));
		}
	}

	@Produce(uri = "jms:topic:batchedit")
	ProducerTemplate batchedit;

	public void batchedit(long customerwarehouseid, long serviceareaid, String editemaildate, long emaildateid) {
		String header = "{\"emaildateid\":" + emaildateid + ",\"editemaildate\":\"" + editemaildate + "\",\"warehouseid\":" + customerwarehouseid + ",\"areaid\":" + serviceareaid + "}";
		try {
			logger.info("消息发送端：batchedit, emaildate={}", header);
			this.batchedit.sendBodyAndHeader(null, "emaildate", header);
		} catch (Exception e) {
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".batchedit")
					.buildExceptionInfo(e.toString()).buildTopic(this.batchedit.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("emaildate", header).getMqException());
	
		}

	}

	public void ExportExcelMethod(HttpServletResponse response, HttpServletRequest request, String cwb, long emaildate, long customerid, int addressCodeEditType, long branchid) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2) && (mouldfieldids2.length() > 0)) { // 选择模板
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
		String sheetName = "订单地址信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		if (emaildate != 0) {
			otherName = this.emaildateDAO.getEmailDateById(emaildate).getEmaildatetime();
		}
		String lastStr = ".xlsx";// 文件名后缀

		fileName = fileName + otherName + lastStr;
		try {
			if (cwb != "") {
				if (cwb.contains("\r\n")) {
					StringBuffer sb = new StringBuffer();
					for (String cwbString : cwb.split("\r\n")) {
						sb.append("'" + cwbString + "',");
					}
					cwb = sb.toString().substring(0, sb.length() - 1);
				}
			}

			final String sql = this.cwbDAO.getcwbOrderByPageIsMyWarehouseSql(customerid, cwb, emaildate, CwbOrderAddressCodeEditTypeEnum.getText(addressCodeEditType), branchid);

			ExcelUtils excelUtil = new ExcelUtils() {
				// 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataImportService.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataImportService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataImportService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataImportService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataImportService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataImportService.this.remarkDao.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataImportService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataImportService.this.reasonDAO.getAllReason();
					DataImportService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								Object a = DataImportService.this.exportService
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
								Map<String, Map<String, String>> orderflowList = DataImportService.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : DataImportService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataImportService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataImportService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void ExportNoPiPeiExcelMethod(HttpServletResponse response, HttpServletRequest request, long branchid) {
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
		String sheetName = "订单地址信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()); // 文件名
		String lastStr = ".xlsx";// 文件名后缀

		fileName = fileName + lastStr;
		try {

			List<String> cwblist = this.noPiPeiCwbDetailDAO.getNoPiPeiCwbDetailByCarwarehouseid(branchid);
			String cwbs = "";
			if (cwblist.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwblist);
			} else {
				cwbs = "'--'";
			}
			final String sql = this.cwbDAO.getSqlByCwb(cwbs);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataImportService.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataImportService.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataImportService.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataImportService.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataImportService.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataImportService.this.remarkDao.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataImportService.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataImportService.this.reasonDAO.getAllReason();
					DataImportService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								Object a = DataImportService.this.exportService
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
								Map<String, Map<String, String>> orderflowList = DataImportService.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : DataImportService.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataImportService.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataImportService.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public EmailDate getOrCreateEmailDate(final long customerid, long customerwarehouseid, final long warehouseid) {
		EmailDate ed = null;
		try {
			String nowdate = DateTimeUtil.getNowDate();
			String nowtime = DateTimeUtil.getNowTimeMin() + ":00";
			ed = this.emaildateDAO.getEmailDateByKeys(customerid, warehouseid, nowdate);
			if (ed == null) {
				ed = new EmailDate();
				ed.setEmaildatetime(nowtime);
				ed.setEmaildateid(this.emaildateDAO.getCreateEmailDate(nowtime, 1, 0, warehouseid, customerid, warehouseid, 0));
				ed.setCustomerid(customerid);
				ed.setWarehouseid(customerwarehouseid); // 发货仓库Id
				ed.setBranchid(warehouseid); // 入库站点Id.
			}
		} catch (Exception e) {
			this.logger.error("生成批次出错", e);
		}
		return ed;
	}

	/**
	 * B2C对接批次生成时间的限制 ，等同于以上的方法 区别是:新增了按照可设置的时间点来卡状态.
	 *
	 * @param customerid
	 * @param customerwarehouseid
	 * @param warehouseid
	 * @param ruleEmaildateHours
	 *            当前规定的邮件时间，单位小时,假如为3
	 * @return
	 */
	public EmailDate getOrCreateEmailDate_B2C(final long customerid, long customerwarehouseid, final long warehouseid, long ruleEmaildateHours) {
		EmailDate ed = null;
		try {
			String nowdate = DateTimeUtil.getNowDate();
			String nowtime = DateTimeUtil.getNowTimeMin() + ":00";

			long nowHours = DateTimeUtil.getNowHours();
			if ((ruleEmaildateHours > 0) && (nowHours < ruleEmaildateHours)) {
				nowdate = DateTimeUtil.formatDateDay(DateTimeUtil.getPreviousDate(new Date()));
				nowtime = DateTimeUtil.formatDateDay(DateTimeUtil.getPreviousDate(new Date())) + " 22:00:00"; // 如果数据跨0点,则默认前一天的批次问题。
			}

			ed = this.emaildateDAO.getEmailDateByKeys_effect(customerid, warehouseid, nowdate);
			if (ed == null) {
				ed = new EmailDate();
				ed.setEmaildatetime(nowtime);
				ed.setEmaildateid(this.emaildateDAO.getCreateEmailDate(nowtime, 1, 0, warehouseid, customerid, warehouseid, 0));
				ed.setCustomerid(customerid);
				ed.setWarehouseid(customerwarehouseid); // 发货仓库Id
				ed.setBranchid(warehouseid); // 入库站点Id.
			}
		} catch (Exception e) {
			this.logger.error("生成批次出错", e);
		}
		return ed;
	}

	public EmailDate getEmailDate_B2C(final long customerid, long customerwarehouseid, final long warehouseid, String emaildate) {
		EmailDate ed = null;
		try {
			ed = this.emaildateDAO.getEmailDateByKeys_effect(customerid, customerwarehouseid, emaildate);
			if (ed == null) {
				ed = new EmailDate();
				ed.setEmaildatetime(emaildate);
				ed.setEmaildateid(this.emaildateDAO.getCreateEmailDate(emaildate, 1, 0, customerwarehouseid, customerid, warehouseid, 0));
				ed.setCustomerid(customerid);
				ed.setWarehouseid(customerwarehouseid); // 发货仓库Id
				ed.setBranchid(warehouseid); // 入库站点Id.
			}
		} catch (Exception e) {
			this.logger.error("生成批次出错", e);
		}
		return ed;
	}

	public EmailDate getEmailDate_B2CByEmaildate(final long customerid, long customerwarehouseid, final long warehouseid, String emaildate) {
		EmailDate ed = null;
		try {
			ed = this.emaildateDAO.getEmailDateByNowdate(customerid, customerwarehouseid, emaildate);
			if (ed == null) {
				ed = new EmailDate();
				ed.setEmaildatetime(emaildate);
				ed.setEmaildateid(this.emaildateDAO.getCreateEmailDate(emaildate, 1, 0, customerwarehouseid, customerid, warehouseid, 0));
				ed.setCustomerid(customerid);
				ed.setWarehouseid(customerwarehouseid); // 发货仓库Id
				ed.setBranchid(warehouseid); // 入库站点Id.
			}
		} catch (Exception e) {
			this.logger.error("生成批次出错", e);
		}
		return ed;
	}

	public Customer getQueryCustomer(List<Customer> customerList, long customerid) {
		Customer customer = new Customer();
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customer = c;
				break;
			}
		}
		return customer;
	}

	public void reProduceTranscwb(List<CwbOrder> colist, List<Customer> customerlist) {
		if ((colist != null) && (colist.size() > 0)) {
			for (CwbOrder co : colist) {
				Customer customer = this.getQueryCustomer(customerlist, co.getCustomerid());
				if ((customer.getIsAutoProductcwb() == 1) && (customer.getIsUsetranscwb() == 0) && (customer.getIsypdjusetranscwb() == 1)) {
					// 运单号
					if (co.getTranscwb().length() == 0) {
						String transcwb = "";
						if (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
							for (int i = 1; i < (co.getSendcarnum() + 1); i++) {
								String newtranscwb = "";
								if (customer.getAutoProductcwbpre().indexOf("[index]") > 1) {
									// 01，10
									// [0][index](如果小于10，那么就是01，02，之后是10）；1,10
									// [index]；01,010 0[index]
									String index = i < 10 ? (customer.getAutoProductcwbpre().indexOf("[0]") > -1 ? "0" + i : i + "") : i + "";
									String produceTransCwb = customer.getAutoProductcwbpre().replace("[0]", "").replace("[sendcarnum]", co.getSendcarnum() + "").replace("[cwb]", co.getCwb())
											.replace("[index]", index);
									newtranscwb = produceTransCwb;
								} else {
									newtranscwb = co.getCwb() + "-" + i;
								}
								transcwb += newtranscwb + ",";
								this.transCwbDao.saveTranscwb(newtranscwb, co.getCwb());
							}
						}
						if (transcwb.length() > 0) {
							transcwb = transcwb.substring(0, transcwb.length() - 1);
						}
						this.cwbDAO.saveTranscwbByCwb(transcwb, co.getCwb());

					}
				}
			}
		}
	}
	
	/**
	* @Title: getStringsByList 
	* @Description: 根据list获得sql中in里的内容 
	* @param @param strArr
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年10月4日 上午10:49:02 
	* @author 刘武强
	 */
	public String getStringsByList(List<String> strArr) {
		String strs = "'',";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'"+str + "',";
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
				strs += str + ",";
			}
		}
		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getInStrings(List<String> strArr) {
		String prefixAndsuffix = "'";
		String strs = "";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += prefixAndsuffix + str + prefixAndsuffix + ",";
			}
		}
		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public List<CwbOrder> getListByCwbs(String cwbs, long emaildateid) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if (cwbs.length() > 0) {
			String[] cwbstr = cwbs.split("\r\n");
			for (int i = 0; i < cwbstr.length; i++) {
				if (i == cwbstr.length) {
					break;
				}
				if (cwbstr[i].trim().length() == 0) {
					continue;
				}
				String cwb = this.cwbOrderService.translateCwb(cwbstr[i].trim());
				List<CwbOrder> oList = this.cwbDAO.getListByCwb(cwb);
				list.addAll(oList);
			}
		} else {
			list = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
		}
		return list;
	}

	public long getWeipipeiCount() {
		long count = 0;
		List<String> notsuccesscwbList = this.cwbDAO.getEditInfoCountIsNotAddressAdd("", String.valueOf(CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));// 未匹配数量
		String nosuccesscwbs = this.getInStrings(notsuccesscwbList);
		List<CwbOrder> orders = this.cwbDAO.getCwbOrderByDelivery(nosuccesscwbs, "WEIPIPEI");
		if (orders != null) {
			count = orders.size();
		}
		return count;
	}
	
	public void insertTransCwbDetail(CwbOrderDTO cwbOrder,String  emaildate) {
		
		for (String transcwb : cwbOrder.getTranscwb().split(cwbOrderService.getSplitstring(cwbOrder.getTranscwb()))) {
			TransCwbDetail td =	transCwbDetailDAO.findTransCwbDetailByTransCwb(transcwb);
			if(td!=null){
				continue;
			}
			
			TransCwbDetail transcwbdetail = new TransCwbDetail();
			transcwbdetail.setCwb(cwbOrder.getCwb());
			transcwbdetail.setTranscwb(transcwb);
			transcwbdetail.setCreatetime(DateTimeUtil.getNowTime());
			transcwbdetail.setCurrentbranchid(0);
			transcwbdetail.setNextbranchid(cwbOrder.getStartbranchid());
			transcwbdetail.setPreviousbranchid(0);
			transcwbdetail.setTranscwboptstate(FlowOrderTypeEnum.DaoRuShuJu.getValue());
			transcwbdetail.setTranscwbstate(TransCwbStateEnum.PEISONG.getValue());
			transcwbdetail.setEmaildate(emaildate);
			
			transCwbDetailDAO.addTransCwbDetail(transcwbdetail);
		}		
	}
	
	/**
	 * 更新发货时间
	 * @author leo01.liao
	 * @param listTranscwb
	 * @param emaildate
	 */
	public void updateEmaildate(List<String> listTranscwb, String emaildate) {
		transCwbDetailDAO.updateEmaildate(listTranscwb, emaildate);
	}
}
