package cn.explink.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.explink.aspect.OrderFlowOperation;
import cn.explink.b2c.maisike.branchsyn_json.Stores;
import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.controller.CwbOrderView;
import cn.explink.controller.ExplinkResponse;
import cn.explink.controller.OrderFlowExport;
import cn.explink.dao.AccountCwbDetailDAO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AccountCwbSummaryDAO;
import cn.explink.dao.AccountDeducDetailDAO;
import cn.explink.dao.AccountDeductRecordDAO;
import cn.explink.dao.BackIntoprintDAO;
import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbALLStateControlDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbDiuShiDAO;
import cn.explink.dao.CwbKuaiDiDAO;
import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.DeliveryCashDAO;
import cn.explink.dao.DeliveryResultChangeDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.dao.FinanceDeliverPayUpDetailDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GotoClassOldDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.NoPiPeiCwbDetailDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderBackRukuRecordDao;
import cn.explink.dao.OrderDeliveryClientDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderFlowLogDAO;
import cn.explink.dao.OrderbackRecordDao;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.PosPayMoneyDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.ReturnCwbsDAO;
import cn.explink.dao.ShangMenTuiCwbDetailDAO;
import cn.explink.dao.ShiXiaoDAO;
import cn.explink.dao.StockDetailDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.TransferReasonStasticsDao;
import cn.explink.dao.TransferResMatchDao;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToBranchDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.domain.AccountCwbDetail;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeductRecord;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.domain.ChangeGoodsTypeResult;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbApplyZhongZhuan;
import cn.explink.domain.CwbDiuShi;
import cn.explink.domain.CwbKuaiDi;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryResultChange;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExceptionCwb;
import cn.explink.domain.Exportwarhousesummary;
import cn.explink.domain.ExpressSysMonitor;
import cn.explink.domain.FinanceDeliverPayupDetail;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.GotoClassOld;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.NoPiPeiCwbDetail;
import cn.explink.domain.OperationTime;
import cn.explink.domain.OrderArriveTime;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.OrderBackRuku;
import cn.explink.domain.OrderbackRecord;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.ReturnCwbs;
import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.domain.StockDetail;
import cn.explink.domain.StockResult;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransferReasonStastics;
import cn.explink.domain.TransferResMatch;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.YpdjHandleRecord;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.ApplyEditDeliverystateIshandleEnum;
import cn.explink.enumutil.ApplyEnum;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.ReturnCwbsTypeEnum;
import cn.explink.enumutil.StockDetailEnum;
import cn.explink.enumutil.StockDetailStocktypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.schedule.Constants;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TransCwbService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Service
@Transactional
public class CwbOrderService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	OrderFlowLogDAO orderFlowLogDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	StockDetailDAO stockDetailDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GroupDetailDao groupDetailDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	DeliveryCashDAO deliveryCashDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	GotoClassOldDAO gotoClassOldDAO;

	@Autowired
	CwbStateControlDAO cwbStateControlDAO;
	@Autowired
	CwbALLStateControlDAO cwbALLStateControlDAO;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	TranscwbOrderFlowDAO transcwborderFlowDAO;
	@Autowired
	YpdjHandleRecordDAO ypdjHandleRecordDAO;

	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	BaleDao baleDAO;
	@Autowired
	BaleCwbDao baleCwbDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;

	@Autowired
	CwbAutoHandleService cwbAutoHandleService;
	@Autowired
	CwbApplyZhongZhuanDAO cwbApplyZhongZhuanDAO;
	@Autowired
	ShiXiaoDAO shiXiaoDAO;

	@Produce(uri = "jms:topic:orderFlow")
	ProducerTemplate orderFlowProducerTemplate;

	@Produce(uri = "jms:topic:deliveryBranchChange")
	ProducerTemplate deliveryBranchChangeProducerTemplate;
	// oms数据失效
	@Produce(uri = "jms:topic:losecwbbatch")
	ProducerTemplate losecwbbatchProducerTemplate;

	@Produce(uri = "jms:queue:VirtualTopicConsumers.oms1.changeGoodsType")
	ProducerTemplate changeGoodsTypeTemplate;

	@Produce(uri = "jms:queue:VirtualTopicConsumers.oms1.updateFinanceAuditStatus")
	ProducerTemplate updateFinanceAuditStatusTemplate;

	@Produce(uri = "jms:queue:VirtualTopicConsumers.oms1.updateBranchFinanceAuditStatus")
	ProducerTemplate updateBranchFinanceAuditStatusTemplate;

	// account数据失效
	@Produce(uri = "jms:topic:dataLoseByCwb")
	ProducerTemplate dataLoseByCwb;
	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	ExportwarhousesummaryDAO exportwarhousesummaryDAO;

	@Autowired
	DeliveryResultChangeDAO deliveryResultChangeDAO;

	@Autowired
	OperationRuleService operationRuleService;

	@Autowired
	private OperationTimeDAO operationTimeDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	GroupDetailDao groupDetailDao;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayMoneyDAO posPayMoneyDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	TransCwbService transCwbService;
	@Autowired
	WarehouseToBranchDAO warehouseToBranchDAO;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;
	@Autowired
	FinanceDeliverPayUpDetailDAO financeDeliverPayUpDetailDAO;
	@Autowired
	ReturnCwbsDAO returnCwbsDAO;

	@Autowired
	JointService jointService;

	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	StoresDAO storesDAO;
	@Autowired
	ShangMenTuiCwbDetailDAO shangMenTuiCwbDetailDAO;
	@Autowired
	CwbDiuShiDAO cwbDiuShiDAO;
	@Autowired
	NoPiPeiCwbDetailDAO noPiPeiCwbDetailDAO;
	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;
	@Autowired
	BackIntoprintDAO backIntoprintDAO;

	@Autowired
	AccountCwbDetailDAO accountCwbDetailDAO;
	@Autowired
	AccountCwbSummaryDAO accountCwbSummaryDAO;
	@Autowired
	AccountDeducDetailDAO accountDeducDetailDAO;
	@Autowired
	AccountDeductRecordDAO accountDeductRecordDAO;
	@Autowired
	AccountDeductRecordService accountDeductRecordService;
	@Autowired
	OrderBackCheckService orderBackCheckService;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	OrderArriveTimeService orderArriveTimeService;
	@Autowired
	OrderArriveTimeDAO orderArriveTimeDAO;
	@Autowired
	AccountCwbDetailService accountCwbDetailService;
	@Autowired
	AccountDeducDetailService accountDeducDetailService;
	@Autowired
	OrderDeliveryClientDAO orderDeliveryClientDAO;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	private SystemInstallService systemInstallService;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;
	@Autowired
	private AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	private BackDetailService backDetailService;
	@Autowired
	TransferReasonStasticsDao transferReasonStasticsDao;
	@Autowired
	TransferResMatchDao transferResMatchDao;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	OrderbackRecordDao orderbackRecordDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderBackRukuRecordDao orderBackRukuRecordDao;
	@Autowired
	CwbApplyZhongZhuanDAO applyZhongZhuanDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	public void insertCwbOrder(final CwbOrderDTO cwbOrderDTO, final long customerid, final long warhouseid, final User user, final EmailDate ed) {
		this.logger.info("导入一条新的订单，订单号为{}", cwbOrderDTO.getCwb());

		// 保存操作记录并返回对应的操作记录的id 将id保存到express_ops_cwb_detail记录中 用作双向1对1
		if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) && (cwbOrderDTO.getSendcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(1);
			cwbOrderDTO.setBackcargonum(0);
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) && (cwbOrderDTO.getSendcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(1);
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) && (cwbOrderDTO.getBackcargonum() == 0)) {
			cwbOrderDTO.setBackcargonum(1);// 按海外环球的需求，取货件数不处理
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (cwbOrderDTO.getBackcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(0);// 按海外环球的需求，取货件数不处理
			cwbOrderDTO.setBackcargonum(1);
		}

		this.jdbcTemplate
				.update("insert into express_ops_cwb_detail (cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,cwbremark,"
						+ "customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,cartype,carsize,backcaramount,"
						+ "destination,transway,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,cwbdelivertypeid,customerwarehouseid,cwbprovince,"
						+ "cwbcity,cwbcounty,shipcwb,transcwb,serviceareaid,deliverybranchid,orderflowid,flowordertype,emailfinishflag,commonid,modelname,emaildateid,carwarehouse,"
						+ "remark1,remark2,remark3,remark4,remark5,paywayid,newpaywayid,nextbranchid,tuihuoid,cargovolume,consignoraddress,multi_shipcwb,addresscodeedittype,printtime,commoncwb,shouldfare) "
						+ "values(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,? ,?,?)", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {

						ps.setString(1, cwbOrderDTO.getCwb());
						ps.setString(2, cwbOrderDTO.getConsigneename());
						ps.setString(3, cwbOrderDTO.getConsigneeaddress());
						ps.setString(4, cwbOrderDTO.getConsigneepostcode());
						ps.setString(5, cwbOrderDTO.getConsigneephone());
						ps.setString(6, cwbOrderDTO.getSendcargoname());
						ps.setString(7, cwbOrderDTO.getBackcargoname());
						ps.setFloat(8, cwbOrderDTO.getReceivablefee().floatValue());
						ps.setFloat(9, cwbOrderDTO.getPaybackfee().floatValue());
						ps.setFloat(10, cwbOrderDTO.getCargorealweight().floatValue());
						ps.setString(11, cwbOrderDTO.getCwbremark());
						ps.setLong(12, customerid);
						ps.setString(13, cwbOrderDTO.getEmaildate().length() == 0 ? ed.getEmaildatetime() : cwbOrderDTO.getEmaildate());
						ps.setString(14, cwbOrderDTO.getConsigneemobile());
						ps.setLong(15, 0);
						ps.setString(16, cwbOrderDTO.getExceldeliver());
						ps.setString(17, cwbOrderDTO.getConsigneeno());
						ps.setString(18, cwbOrderDTO.getExcelbranch());
						ps.setFloat(19, cwbOrderDTO.getCargoamount().floatValue());
						ps.setString(20, cwbOrderDTO.getCustomercommand());
						ps.setString(21, cwbOrderDTO.getCargotype());
						ps.setString(22, cwbOrderDTO.getCargosize());
						ps.setFloat(23, cwbOrderDTO.getBackcargoamount().floatValue());
						ps.setString(24, cwbOrderDTO.getDestination());
						ps.setString(25, cwbOrderDTO.getTransway());
						ps.setLong(26, cwbOrderDTO.getShipperid());
						ps.setInt(27, cwbOrderDTO.getSendcargonum());
						ps.setInt(28, cwbOrderDTO.getBackcargonum());
						ps.setLong(29, user.getUserid());
						ps.setLong(30, cwbOrderDTO.getCwbordertypeid());
						ps.setLong(31, cwbOrderDTO.getCwbdelivertypeid());
						ps.setLong(32, cwbOrderDTO.getCustomerwarehouseid());
						ps.setString(33, cwbOrderDTO.getCwbprovince());
						ps.setString(34, cwbOrderDTO.getCwbcity());
						ps.setString(35, cwbOrderDTO.getCwbcounty());
						ps.setString(36, cwbOrderDTO.getShipcwb());
						ps.setString(37, cwbOrderDTO.getTranscwb());
						ps.setLong(38, cwbOrderDTO.getServiceareaid());
						ps.setLong(39, cwbOrderDTO.getDeliverybranchid());
						ps.setLong(40, 0);
						ps.setInt(41, FlowOrderTypeEnum.DaoRuShuJu.getValue());
						ps.setInt(42, EmailFinishFlagEnum.WeiDaoHuo.getValue());
						ps.setLong(43, (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()));
						ps.setString(44, cwbOrderDTO.getModelname());
						ps.setLong(45, ed.getEmaildateid());
						ps.setLong(46, ed.getBranchid());
						ps.setString(47, cwbOrderDTO.getRemark1());
						ps.setString(48, cwbOrderDTO.getRemark2());
						ps.setString(49, cwbOrderDTO.getRemark3());
						ps.setString(50, cwbOrderDTO.getRemark4());
						ps.setString(51, cwbOrderDTO.getRemark5());
						ps.setLong(52, cwbOrderDTO.getPaywayid());
						ps.setString(53, cwbOrderDTO.getNewpaywayid());
						ps.setLong(54, cwbOrderDTO.getStartbranchid());
						ps.setLong(55, 0);
						ps.setFloat(56, cwbOrderDTO.getCargovolume().floatValue()); // 货物体积
						ps.setString(57, cwbOrderDTO.getConsignoraddress()); // 取件地址
						ps.setString(58, cwbOrderDTO.getMulti_shipcwb()); // 不可去掉
						ps.setInt(59, cwbOrderDTO.getAddresscodeedittype());
						ps.setString(60, cwbOrderDTO.getPrinttime());
						ps.setString(61, cwbOrderDTO.getCommoncwb());
						ps.setFloat(62, cwbOrderDTO.getShouldfare().floatValue());
					}

				});
		this.createFloworder(user, user.getBranchid(), cwbOrderDTO.getCwb(), FlowOrderTypeEnum.DaoRuShuJu, "", System.currentTimeMillis());
		this.logger.info("结算区域accountareaid:{}", cwbOrderDTO.getAccountareaid());
	}

	public ChangeGoodsTypeResult changeGoodsType(String cwbs, int goodsType) {
		String[] cwbArray = cwbs.split(",");
		List<CwbOrder> orderList = this.cwbDAO.queryForChangeGoodsType(cwbArray);

		Set<String> queryCwbSet = this.getCwbSet(orderList);
		if (!queryCwbSet.isEmpty()) {
			String joinStr = this.join(queryCwbSet.toArray(new String[0]), ",");
			this.cwbDAO.changeCwbGoodsType(joinStr, goodsType);
			this.sendChangeGoodsTypeMessage(joinStr, goodsType);
		}
		Map<String, CwbOrder> orderMap = this.getCwbOrderMap(orderList);

		return this.makeResult(cwbArray, queryCwbSet, orderMap);
	}

	private Map<String, CwbOrder> getCwbOrderMap(List<CwbOrder> orderList) {
		Map<String, CwbOrder> orderMap = new HashMap<String, CwbOrder>();
		for (CwbOrder order : orderList) {
			orderMap.put(order.getCwb(), order);
		}
		return orderMap;
	}

	private Set<String> getCwbSet(List<CwbOrder> orderList) {
		Set<String> orderSet = new HashSet<String>();
		for (CwbOrder order : orderList) {
			String signerName = order.getPodrealname();
			if ((signerName == null) || signerName.isEmpty()) {
				orderSet.add(order.getCwb());
			}
		}
		return orderSet;
	}

	private ChangeGoodsTypeResult makeResult(String[] cwbs, Set<String> queryCwbs, Map<String, CwbOrder> orderMap) {
		ChangeGoodsTypeResult result = new ChangeGoodsTypeResult();
		result.setSuccessedCount(queryCwbs.size());
		result.setTotalCount(cwbs.length);
		result.setErrorMap(this.createErrorMap(cwbs, orderMap));

		return result;
	}

	private Map<String, Set<String>> createErrorMap(String[] cwbs, Map<String, CwbOrder> orderMap) {
		Map<String, Set<String>> errorMap = new HashMap<String, Set<String>>();
		this.fillNotExistErrorMap(cwbs, orderMap, errorMap);
		this.fillRepeatErrorMap(cwbs, errorMap);
		this.fillSignErrorMap(cwbs, errorMap, orderMap);

		return errorMap;
	}

	private void fillSignErrorMap(String[] cwbs, Map<String, Set<String>> errorMap, Map<String, CwbOrder> orderMap) {
		Set<String> errorCwbSet = new HashSet<String>();
		for (String cwb : cwbs) {
			CwbOrder order = orderMap.get(cwb);
			if (order == null) {
				continue;
			}
			String signerName = order.getPodrealname();
			if ((signerName == null) || signerName.isEmpty()) {
				continue;
			}
			errorCwbSet.add(order.getCwb());
		}
		if (!errorCwbSet.isEmpty()) {
			errorMap.put("订单已签收无法更改货物类型", errorCwbSet);
		}
	}

	private void fillNotExistErrorMap(String[] cwbs, Map<String, CwbOrder> orderMap, Map<String, Set<String>> errorMap) {
		Set<String> notExistSet = new HashSet<String>();
		for (String cwb : cwbs) {
			if (orderMap.containsKey(cwb)) {
				continue;
			}
			notExistSet.add(cwb);
		}
		errorMap.put("订单不存在", notExistSet);
	}

	private void fillRepeatErrorMap(String[] cwbs, Map<String, Set<String>> errorMap) {
		Map<String, List<String>> repeatMap = new HashMap<String, List<String>>();
		for (String cwb : cwbs) {
			if (!repeatMap.containsKey(cwb)) {
				repeatMap.put(cwb, new ArrayList<String>());
			}
			repeatMap.get(cwb).add(cwb);
		}
		Set<String> repeatSet = new HashSet<String>();
		for (Entry<String, List<String>> repeatEntry : repeatMap.entrySet()) {
			if (repeatEntry.getValue().size() == 1) {
				continue;
			}
			repeatSet.add(repeatEntry.getKey());
		}
		errorMap.put("订单重复", repeatSet);
	}

	private String join(String[] strs, String sperator) {
		if ((strs == null) || (strs.length == 0)) {
			return new String();
		}
		StringBuilder builder = new StringBuilder();
		for (String str : strs) {
			builder.append("'");
			builder.append(str);
			builder.append("'");
			builder.append(sperator);
		}
		return builder.substring(0, builder.length() - 1);
	}

	private void sendChangeGoodsTypeMessage(String cwbs, int goodsType) {
		JSONObject object = new JSONObject();
		object.put("cwbs", cwbs);
		object.put("goodsType", Integer.valueOf(goodsType));
		this.changeGoodsTypeTemplate.sendBodyAndHeader(null, "changeGoodsType", object.toString());
	}

	/**
	 * 数据更新数据
	 *
	 * @param branchid
	 * @param user
	 */
	public void updateExcelCwb(CwbOrderDTO cwbOrderDTO, long customerid, long branchid, User user, EmailDate ed, boolean isReImport) {
		this.logger.info("更新一条订单的基本信息，订单号为{}", cwbOrderDTO.getCwb());
		// 保存操作记录并返回对应的操作记录的id 将id保存到express_ops_cwb_detail记录中 用作双向1对1
		if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) && (cwbOrderDTO.getSendcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(1);
			cwbOrderDTO.setBackcargonum(0);
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) && (cwbOrderDTO.getSendcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(1);
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) && (cwbOrderDTO.getBackcargonum() == 0)) {
			cwbOrderDTO.setBackcargonum(1);// 按海外环球的需求，取货件数不处理
		} else if ((cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (cwbOrderDTO.getBackcargonum() == 0)) {
			cwbOrderDTO.setSendcargonum(0);// 按海外环球的需求，取货件数不处理
			cwbOrderDTO.setBackcargonum(1);
		}
		String sql = "update express_ops_cwb_detail set consigneename=?,consigneeaddress=?,consigneepostcode=?,consigneephone=?,sendcarname=?,backcarname=?,receivablefee=?,paybackfee=?,carrealweight=?,cwbremark=?,"
				+ "customerid=?,emaildate=?,emaildateid=?,consigneemobile=?,shipcwb=?,exceldeliver=?,consigneeno=?,excelbranch=?,caramount=?,customercommand=?,"
				+ "cartype=?,carsize=?,backcaramount=?,destination=?,transway=?,shipperid=?,sendcarnum=?,backcarnum=?,excelimportuserid=?,cwbordertypeid=?,customerwarehouseid=?,cwbprovince=?,cwbcity=?,cwbcounty=?,"
				+ "transcwb=?,serviceareaid=?,orderflowid=?,emailfinishflag=?,commonid=?,modelname=?,carwarehouse=?,"
				+ "remark1=?,remark2=?,remark3=?,remark4=?,remark5=?,paywayid=?,newpaywayid=?,addresscodeedittype=?,printtime=?,shouldfare=? " + ",cwbstate =1 where cwb=? and state=1";

		this.jdbcTemplate.update(sql, cwbOrderDTO.getConsigneename(), cwbOrderDTO.getConsigneeaddress(), cwbOrderDTO.getConsigneepostcode(), cwbOrderDTO.getConsigneephone(),
				cwbOrderDTO.getSendcargoname(), cwbOrderDTO.getBackcargoname(), cwbOrderDTO.getReceivablefee(), cwbOrderDTO.getPaybackfee(), cwbOrderDTO.getCargorealweight(),
				cwbOrderDTO.getCwbremark(), customerid, cwbOrderDTO.getEmaildate().length() == 0 ? ed.getEmaildatetime() : cwbOrderDTO.getEmaildate(), ed.getEmaildateid(),
				cwbOrderDTO.getConsigneemobile(), cwbOrderDTO.getShipcwb(), cwbOrderDTO.getExceldeliver(), cwbOrderDTO.getConsigneeno(), cwbOrderDTO.getExcelbranch(), cwbOrderDTO.getCargoamount(),
				cwbOrderDTO.getCustomercommand(), cwbOrderDTO.getCargotype(), cwbOrderDTO.getCargosize(), cwbOrderDTO.getBackcargoamount(), cwbOrderDTO.getDestination(), cwbOrderDTO.getTransway(),
				cwbOrderDTO.getShipperid(), cwbOrderDTO.getSendcargonum(), cwbOrderDTO.getBackcargonum(), user.getUserid(), cwbOrderDTO.getCwbordertypeid(), cwbOrderDTO.getCustomerwarehouseid(),
				cwbOrderDTO.getCwbprovince(), cwbOrderDTO.getCwbcity(), cwbOrderDTO.getCwbcounty(), cwbOrderDTO.getTranscwb(), cwbOrderDTO.getServiceareaid(), 0,
				EmailFinishFlagEnum.ZhengChangRuKu.getValue(), (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()), cwbOrderDTO.getModelname(), branchid, cwbOrderDTO.getRemark1(),
				cwbOrderDTO.getRemark2(), cwbOrderDTO.getRemark3(), cwbOrderDTO.getRemark4(), cwbOrderDTO.getRemark5(), cwbOrderDTO.getPaywayid(), cwbOrderDTO.getNewpaywayid(),
				cwbOrderDTO.getAddresscodeedittype(), cwbOrderDTO.getPrinttime(), cwbOrderDTO.getShouldfare(), cwbOrderDTO.getCwb());
		// 保存操作记录并返回对应的操作记录的id 将id保存到express_ops_cwb_detail记录中 用作双向1对1
		// orderFlowDAO.creOrderFlow(new OrderFlow(0,cwbOrderDTO.getCwb(),
		// branchid, new Timestamp( System.currentTimeMillis()),
		// user.getUserid(),
		// JSONObject.fromObject(cwbDAO.getCwbByCwb(cwbOrderDTO.getCwb())).toString(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),0));
		//
		this.logger.info("结算区域accountareaid:{}", cwbOrderDTO.getAccountareaid());

		if (!isReImport) {
			this.emailDateDAO.saveEmailDateToEmailDate(ed.getEmaildateid());
		}
		// updateProducer.sendBodyAndHeader(null, "cwb", cwbOrderDTO.getCwb());
	}

	public void sendBranchFinanceAuditJMS(List<Long> payupIds, int deliverpayupapproved) {
		if (payupIds.isEmpty()) {
			return;
		}
		JSONObject param = new JSONObject();
		param.put("payupIds", JSONArray.fromObject(payupIds));
		param.put("deliverpayupapproved", deliverpayupapproved);

		this.updateBranchFinanceAuditStatusTemplate.sendBodyAndHeader("", "updateBranchFinanceAuditStatus", param.toString());
	}

	public void sendFinanceAuditJMS(List<Long> gcaids, int deliverpayupapproved) {
		if (gcaids.isEmpty()) {
			return;
		}
		JSONObject param = new JSONObject();
		param.put("gcaids", JSONArray.fromObject(gcaids));
		param.put("deliverpayupapproved", deliverpayupapproved);

		this.updateFinanceAuditStatusTemplate.sendBodyAndHeader("", "updateFinanceAuditStatus", param.toString());
	}

	/**
	 * 提货扫描
	 *
	 * @param co
	 * @param customerid
	 * @param driverid
	 * @return
	 */
	public CwbOrder intoWarehousForGetGoods(User user, String cwb, long owgid, long customerid) {
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			if (customerid < 1) {
				// 有货无单，必须选择供货商
				throw new CwbException(cwb, FlowOrderTypeEnum.TiHuo.getValue(), ExceptionCwbErrorTypeEnum.Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG);
			}

			// 订单不存在时插入一条新数据
			String sql = "insert into express_ops_cwb_detail (cwb,startbranchid,customerid,flowordertype,cwbstate) values(?,?,?,?,?)";
			this.jdbcTemplate.update(sql, cwb, user.getBranchid(), customerid, CwbFlowOrderTypeEnum.WeiDaoHuo.getValue(), CwbStateEnum.WUShuju.getValue());
			co = this.cwbDAO.getCwbByCwb(cwb);
		} else {
			if (co.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue()) {
				if (this.tryIncreaseScanNum(co)) {
					return co;
				} else {
					throw new CwbException(cwb, FlowOrderTypeEnum.TiHuo.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
				}
			} else {
				this.cwbDAO.updateScannum(co.getCwb(), 1);
			}
			this.validateStateTransfer(co, FlowOrderTypeEnum.TiHuo);

			this.cwbRouteService.reload();
			long nextbranchid = this.cwbRouteService.getNextBranch(user.getBranchid(), co.getDeliverybranchid());
			this.logger.info("正常入库-保存下一站");
			this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

			String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, user.getBranchid(), CwbFlowOrderTypeEnum.TiHuo.getValue(), co.getCwb());
		}
		this.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.TiHuo, "", System.currentTimeMillis());
		return co;
	}

	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}

	/**
	 * 正常入库扫描
	 *
	 * @param co
	 * @param customerid
	 * @param driverid
	 * @return
	 */

	public CwbOrder intoWarehous(User user, String cwb, String scancwb, long customerid, long driverid, long requestbatchno, String comment, String baleno, boolean anbaochuku) {
		this.logger.info("开始入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.intoWarehousHandle(user, cwb, scancwb, user.getBranchid(), customerid, driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku);
	}

	@Transactional
	public CwbOrder intoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long customerid, long driverid, long requestbatchno, String comment, boolean isauto, String baleno,Long credate, boolean anbaochuku) {
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;

		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if ((customerid > 0) && (co != null)) {
			// TODO 因为客户的货物会混着扫描
			customerid = co.getCustomerid();
			// throw new CwbException(cwb,flowOrderTypeEnum.getValue(),
			// ExceptionCwbErrorTypeEnum.GONG_YING_SHANG_XUAN_ZE_CUO_WU,
			// customerDAO.getCustomerById(co.getCustomerid()).getCustomername());
		}

		/*
		 * if (baleno != null && baleno.length() > 0 &&
		 * !baleno.equals(co.getPackagecode())) { throw new
		 * CwbException(cwb,flowOrderTypeEnum.getValue(),
		 * ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI); }
		 */

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);

		if (co == null) {
			if (customerid < 1) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG);
			}

			if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue())
					&& (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(CwbStateEnum.WUShuju.getValue(), FlowOrderTypeEnum.RuKu.getValue()) != null)) {
				co = this.createCwbDetail(user, customerid, cwb);
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
		}

		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleIntowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, credate);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, false, credate, anbaochuku);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleIntowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, Long credate) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				// 到车时间入库扫描件数
				this.orderArriveTimeDAO.updateScannum(co.getCwb(), co.getScannum() + 1);

				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void validateIsSubCwb(String cwb, CwbOrder co, long flowordertype) {
		String transcwb = co.getTranscwb();
		if (!StringUtils.hasLength(transcwb)) {// 为兼容腾讯达历史数据没有transcwb的问题，下个版本请删除
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
		if (flowordertype != FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			throw new CwbException(cwb, flowordertype, ExceptionCwbErrorTypeEnum.Qing_SAO_MIAO_YUN_DAN_HAO);
		}
	}

	public String getSplitstring(String transcwb) {
		if (transcwb.indexOf(':') != -1) {
			return ":";
		}
		return ",";
	}

	private void handleIntowarehouse(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}

		// 自动补充完环节后重新定位当前操作

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		this.logger.info("入库数据批次处理完成");

		this.cwbRouteService.reload();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		this.logger.info("正常入库-保存下一站");
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowOrderTypeEnum.getValue(), co.getCwb());

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate);
		this.intoWarhouse(user, cwb, flowOrderTypeEnum, credate);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
		}

		this.baleDaoHuo(co);
		EmailDate ed = this.emailDateDAO.getEmailDateById(co.getEmaildateid());
		if ((ed != null) && (ed.getState() == 0)) {// 如果批次为未到货 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
		// ==========库房入库到车时间=========
		if (userbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			String arriveflag = this.systemInstallDAO.getSystemInstall("arriveflag") == null ? "no" : this.systemInstallDAO.getSystemInstall("arriveflag").getValue();
			if ("yes".equals(arriveflag)) {// 开关
				this.logger.info("===创建库房入库到车时间数据开始===");
				this.orderArriveTimeDAO.deleteOrderArriveTimeByCwb(co.getCwb());
				OrderArriveTime orderArriveTime = this.orderArriveTimeService.loadFormForOrderArriveTime(co, currentbranchid, co.getEmaildate(), user.getUserid());
				this.orderArriveTimeDAO.createOrderArriveTime(orderArriveTime);
				this.logger.info("用户:{},创建库房入库到车时间数据:订单号{}", new Object[] { user.getRealname(), co.getCwb() });
			}

		}

		// 如果订单为出库状态 &&同一个库房进行出库入库
		if ((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getStartbranchid() == currentbranchid)) {
			this.logger.info("重复入库");
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}

		// ==========结算中转入库扫描逻辑=======
		// 起始站为站点类型
		if ((startbranch.getBranchid() != 0) && (startbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 当前操作站点为中转站点
			if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				// 买单结算
				if (startbranch.getAccounttype() == 1) {
					// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
					if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue())
							|| ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
							|| (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
						this.logger.info("===开始创建买单结算中转站点入库扫描记录===");
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(),user,currentbranchid);
						accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(), user.getUserid(),
								currentbranchid);
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						this.logger.info("用户:{},创建结算中转站点入库扫描记录,站点{},入库中转站点{},订单号{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
					}
				}

				// 扣款结算
				if (startbranch.getAccounttype() == 3) {
					this.logger.info("===开始创建扣款结算中转货款数据===");
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), fee, user.getUserid(),
							"", 0, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算中转退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
				}
			}
		}

	}

	private void intoWarhouse(User user, String cwb, FlowOrderTypeEnum flowOrderTypeEnum, Long credate) {
		Exportwarhousesummary ex = this.exportwarhousesummaryDAO.getIntowarhouse(cwb);
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date d = new java.util.Date(credate);
		String date = df.format(d);
		if (ex == null) {
			this.exportwarhousesummaryDAO.setIntowarhouse(cwb, user.getBranchid(), date, user.getUserid(), flowOrderTypeEnum.getValue());
		} else {
			this.exportwarhousesummaryDAO.updateIntowarhouse(cwb, user.getBranchid(), date, user.getUserid(), flowOrderTypeEnum.getValue());
		}
	}

	/**
	 * 中转站正常入库扫描
	 *
	 * @param co
	 * @param customerid
	 * @param driverid
	 * @return
	 */

	public CwbOrder changeintoWarehous(User user, String cwb, String scancwb, long customerid, long driverid, long requestbatchno, String comment, String baleno, boolean anbaochuku, int checktype,
			long nextbranchid) {
		this.logger.info("开始中转站入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		long branchid = user.getBranchid();
		if ((nextbranchid > 0) && (checktype == 1)) {
			branchid = nextbranchid;
		}
		return this.changeintoWarehousHandle(user, cwb, scancwb, branchid, customerid, driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku);
	}

	@Transactional
	public CwbOrder changeintoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long customerid, long driverid, long requestbatchno, String comment, boolean isauto,
			String baleno, Long credate, boolean anbaochuku) {
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.ZhongZhuanZhanRuKu;

		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}

		CwbOrder cwbOrdercheck=this.cwbDAO.getCwbByCwb(cwb);
		int changealowflag=this.getChangealowflagByIdAdd(cwbOrdercheck);
		long count = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCount(cwb);
		if ((cwbOrdercheck.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue())&&(cwbOrdercheck.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue())&&(changealowflag==1)&&(count==0)) {
			//return this.responseErrorZhongzhuanrukuLimit();
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuanruku);
		}

		//已申请中转出站并且没有做审核的订单不允许中转出站
		CwbApplyZhongZhuan cazz = this.cwbApplyZhongZhuanDAO.getCwbapplyZZ(cwb);
		if(cazz!=null){
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shen_Qing_Zhong_Zhuan_Wei_Shen_He_Cheng_Gong_Error);
		}

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if ((customerid > 0) && (co != null)) {
			// TODO 因为客户的货物会混着扫描
			if (customerid != co.getCustomerid()) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.GONG_YING_SHANG_XUAN_ZE_CUO_WU, this.customerDAO.getCustomerById(co.getCustomerid())
						.getCustomername());
			}
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);

		if (co == null) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleChangeIntowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, credate);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, false, credate, anbaochuku);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleChangeIntowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, Long credate) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				// 到车时间入库扫描件数
				this.orderArriveTimeDAO.updateScannum(co.getCwb(), co.getScannum() + 1);

				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleChangeIntowarehouse(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}

		// 自动补充完环节后重新定位当前操作

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		this.logger.info("中转站入库数据批次处理完成");

		this.cwbRouteService.reload();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		this.logger.info("中转站正常入库-保存下一站");
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowOrderTypeEnum.getValue(), co.getCwb());

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
		}

		this.baleDaoHuo(co);
		EmailDate ed = this.emailDateDAO.getEmailDateById(co.getEmaildateid());
		if ((ed != null) && (ed.getState() == 0)) {// 如果批次为未到货 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());

		// 如果订单为出库状态 &&同一个库房进行出库入库
		if (((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()))
				&& (co.getStartbranchid() == currentbranchid)) {
			this.logger.info("重复入库");
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}

		// ==========结算中转入库扫描逻辑=======
		// 起始站为站点类型
		if ((startbranch.getBranchid() != 0) && (startbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 当前操作站点为中转站点
			if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				// 买单结算
				if (startbranch.getAccounttype() == 1) {
					// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
					if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue())
							|| ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
							|| (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
						this.logger.info("===开始创建买单结算中转站点入库扫描记录===");
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(),user,currentbranchid);
						accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(), user.getUserid(),
								currentbranchid);
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						this.logger.info("用户:{},创建结算中转站点入库扫描记录,站点{},入库中转站点{},订单号{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
					}
				}

				// 扣款结算
				if (startbranch.getAccounttype() == 3) {
					this.logger.info("===开始创建扣款结算中转货款数据===");
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), fee, user.getUserid(),
							"", 0, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算中转退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
				}
			}
		}

	}

	/**
	 * 在ops_ypdjhandlerecord表产生一票多件订单的记录
	 *
	 * @param co
	 * @param cwb
	 * @param flowordertype
	 * @param isypdjusetranscwb
	 */
	@Transactional
	public void intoAndOutwarehouseYpdjCre(User user, CwbOrder co, String scancwb, long flowordertype, long isypdjusetranscwb, long nextbranchid) {
		// 2013.8.21
		// 临时处理,避免某订单做了入库,但是有缺货,去做出库时,入库里还显示缺货,暂决定删除之前的数据,避免仍然上一操作还有该单缺货的问题
		this.ypdjHandleRecordDAO.delYpdjHandleRecordByCwb(co.getCwb());

		for (int i = 0; i < (co.getSendcarnum() - 1); i++) {
			// 若运单号第一位是逗号则剔除
			String oldtranscwb = "";
			if (!"".equals(co.getTranscwb()) && (co.getTranscwb().length() > 0)) {// 一票多件没有判断非空情况，扫描报错

				oldtranscwb = co.getTranscwb().substring(0, 1).equals(",") ? co.getTranscwb().substring(1, co.getTranscwb().length()) : co.getTranscwb();
			}

			String transcwb = "";
			if ((isypdjusetranscwb == 1) && ((oldtranscwb.split(",").length > 1) || (oldtranscwb.split(":").length > 1)) && (oldtranscwb.indexOf(scancwb) > -1)) {
				if (oldtranscwb.split(",").length > 1) {
					String newtranscwb = oldtranscwb.replace(scancwb + ",", "");
					transcwb = newtranscwb.split(",")[i];
				} else {
					String newtranscwb = oldtranscwb.replace(scancwb + ":", "");
					transcwb = newtranscwb.split(":")[i];
				}
			} else {
				if (oldtranscwb.length() > 0) {
					transcwb = "havetranscwb_" + oldtranscwb + "_" + i;
				} else {
					transcwb = "explink_" + co.getCwb() + "_" + i;
				}
			}
			if ("".equals(transcwb.trim())) {
				continue;
			}
			YpdjHandleRecord ypdjHandleRecord = new YpdjHandleRecord();
			ypdjHandleRecord.setCustomerid(co.getCustomerid());
			ypdjHandleRecord.setCwb(co.getCwb());
			ypdjHandleRecord.setFlowordertype(flowordertype);
			ypdjHandleRecord.setTranscwb(transcwb);
			ypdjHandleRecord.setBranchid(user.getBranchid());
			ypdjHandleRecord.setNextbranchid(nextbranchid);

			this.ypdjHandleRecordDAO.creYpdjHandleRecord(ypdjHandleRecord);
		}
	}

	/**
	 * 从ops_ypdjhandlerecord表删除当前扫描一票多件对应的运单号的记录
	 *
	 * @param co
	 * @param cwb
	 * @param flowordertype
	 * @param isypdjusetranscwb
	 */
	public void intoAndOutwarehouseYpdjDel(User user, CwbOrder co, String scancwb, long flowordertype, long isypdjusetranscwb, long nextbranchid) {
		// 若运单号第一位是逗号则剔除
		String oldtranscwb = co.getTranscwb().substring(0, 1).equals(",") ? co.getTranscwb().substring(1, co.getTranscwb().length()) : co.getTranscwb();
		String transcwb = "";
		if ((isypdjusetranscwb == 1) && ((oldtranscwb.split(",").length > 1) || (oldtranscwb.split(":").length > 1)) && (oldtranscwb.indexOf(scancwb) > -1)) {
			transcwb = scancwb;
		} else {
			if (transcwb.length() == 0) {
				if (oldtranscwb.length() > 0) {
					transcwb = "havetranscwb_" + oldtranscwb + "_" + (co.getScannum() - 2);
				} else {
					transcwb = "explink_" + co.getCwb() + "_" + (co.getScannum() - 2);
				}
			}
		}

		this.ypdjHandleRecordDAO.delYpdjHandleRecord(co.getCwb(), transcwb, flowordertype, co.getCustomerid(), user.getBranchid(), nextbranchid);
	}

	/**
	 * 分站到货扫描
	 *
	 * @param co
	 * @param customerid
	 * @param driverid
	 * @param anbaochuku是否按包出库
	 * @return
	 */

	public CwbOrder substationGoods(User user, String cwb, String scancwb, long driverid, long requestbatchno, String comment, String baleno, boolean anbaochuku) {
		this.logger.info("开始分站到货处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.substationGoodsHandle(user, cwb, scancwb, user.getBranchid(), driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku);
	}

	@Transactional
	public CwbOrder substationGoodsHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long requestbatchno, String comment, boolean isauto, String baleno, Long credate,
			boolean anbaochuku) {
		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		/*
		 * if (baleno != null && baleno.length() > 0 &&
		 * !baleno.equals(co.getPackagecode())) { throw new
		 * CwbException(cwb,FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		 * ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI); }
		 */

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao;
		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {
			flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao;
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleSubstationGoodsYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, credate);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, false, credate, anbaochuku);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleSubstationGoodsYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, Branch userbranch, long isypdjusetranscwb, Long credate) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleSubstationGoods(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, Branch userbranch, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!isauto
				&& !(((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao)) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui
						.getValue()) && (flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao)))) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}

		// 自动补充完环节后重新定位当前操作

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		this.logger.info("分站到货数据批次处理完成");

		long accountFlowOrderType = co.getFlowordertype();

		flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao;
		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
				&& (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {
			flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao;

		}

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowOrderTypeEnum.getValue(), co.getCwb());
		// TODO 历史配送站点
		if (co.getHistorybranchname() == null) {
			String sql1 = "update express_ops_cwb_detail set historybranchname=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql1, userbranch.getBranchname(), co.getCwb());
		}

		if ((co.getNextbranchid() != currentbranchid) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			Branch nextbranch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
			if ((nextbranch != null) && (nextbranch.getSitetype() != BranchEnum.ZhanDian.getValue())) {
				String sqlstr = "update express_ops_cwb_detail set nextbranchid=? where cwb=? and state=1";
				this.jdbcTemplate.update(sqlstr, currentbranchid, co.getCwb());
			}
		}
		// ======按包到货时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
		}

		this.baleDaoHuo(co);
		EmailDate ed = this.emailDateDAO.getEmailDateById(co.getEmaildateid());
		if ((ed != null) && (ed.getState() == 0)) {// 如果批次为未到货 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		// ============结算逻辑站点到货扫描&&站点到错货处理小件员记录=======================
		Branch nextbranch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
		// 判断当前操作类型为站点
		if (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			// ========================到错货=====================================
			if (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				if ((nextbranch.getAccounttype() == 3) || (userbranch.getAccounttype() == 3)) {
					// 流程检查 (到货扫描操作之后不允许做到错货)
					long count = this.cwbStateControlDAO.getCountFromstateTostate(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
					if (count > 0) {
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha);
					}
				}
				// 校验配送状态
				DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
				if (ds != null) {
					// 如果换小件员，则失效上一条记录
					if ((ds.getGcaid() <= 0) && (ds.getDeliverystate() == 0)) {
						// 修改派送反馈表（派送员领货时将产生这样一条记录对应cwb表）为失效
						this.deliveryStateDAO.inactiveDeliveryStateByCwb(cwb);
						// 同时更改deliverycash表中的相关信息为失效
						this.deliveryCashDAO.updateDeliveryCashStateBycwb(cwb);
					}
				}
			}// 到错货END
				// =====================到错货处理(对最后一条出库记录【KouKuan】进行【预扣款】返款
				// 2.产生一条出库记录【KouKuan】进行【预扣款】扣款 )==========================
			if (accountFlowOrderType == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				// 1.对最后一条出库记录【KouKuan】进行【预扣款】返款
				this.accountDeductRecordService.returnLastChuKu(co.getCwb(), user);
				// 2.产生一条出库记录【KouKuan】进行【预扣款】扣款
				if (userbranch.getAccounttype() == 3) {
					this.logger.info("===开始创建扣款结算到错货处理出库记录===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(currentbranchid);
					BigDecimal koukuan = co.getReceivablefee();// 扣款
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款
					// 扣款逻辑
					Map feeMap = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					// 修改branch表 的余额、欠款
					this.branchDAO.updateForFee(currentbranchid, balance, debt);
					// 插入一条扣款记录
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan,
							branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货处理出库", co.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, user.getUserid(),
							"到错货处理出库", recordid, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算到错货处理出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), koukuan });
				}
			}// 到错货处理END
				// =========================正常到货=================================
			if (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				// 当前操作站扣款结算======伪扣款======
				if (userbranch.getAccounttype() == 3) {
					// 流程检查 (到错货不允许做到货扫描)
					long count = this.cwbStateControlDAO.getCountFromstateTostate(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
					if (count > 0) {
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha1);
					}
					this.logger.info("===开始站点到货扣款(伪扣款)===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(currentbranchid);
					// 扣款IDS
					String kouKuanIds = AccountFlowOrderTypeEnum.KouKuan.getValue() + "";
					List<AccountDeducDetail> listd = this.accountDeducDetailDAO.getDeducDetailByRuKuKouKuan(co.getCwb(), currentbranchid, kouKuanIds);
					if ((listd != null) && !listd.isEmpty()) {
						BigDecimal koukuan = listd.get(0).getFee();// 代收货款
						if (koukuan.compareTo(new BigDecimal("0")) > 0) {
							Map feeMap = new HashMap();
							BigDecimal credit = branchLock.getCredit();// 信用额度
							BigDecimal balance = branchLock.getBalancevirt();// 伪余额
							BigDecimal debt = branchLock.getDebtvirt();// 伪欠款
							// 扣款逻辑
							feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
							balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
							debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
							// 修改branch表 的余额、欠款
							this.branchDAO.updateForVirt(currentbranchid, balance, debt);
							// 更新订单记录 recordidvirt=1 已结算
							this.accountDeducDetailDAO.updateDeducDetailRecordVirtIdById(listd.get(0).getId() + "");
							this.logger.info("用户{},对{}站点进行站点到货伪扣款：原伪余额{}元，原伪欠款{}元。扣款{}后，伪余额{}元，伪 欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), branchLock.getBalancevirt(),
									branchLock.getDebtvirt(), koukuan, balance, debt });
						}
						this.logger.info("===伪扣款结束===");
					}
				}
			}
		}

	}

	/**
	 * 退货站入库扫描
	 *
	 * @param co
	 * @param customerid
	 * @param driverid
	 * @return
	 */

	public CwbOrder backIntoWarehous(User user, String cwb, String scancwb, long driverid, long requestbatchno, String comment, boolean anbaochuku, int checktype, long nextbranchid) {
		this.logger.info("开始退货站入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		long branchid = user.getBranchid();
		if ((nextbranchid > 0) && (checktype == 1)) {
			branchid = nextbranchid;
		}
		return this.backIntoWarehousHandle(user, cwb, scancwb, branchid, driverid, requestbatchno, comment, anbaochuku);
	}

	@Transactional
	private CwbOrder backIntoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long requestbatchno, String comment, boolean anbaochuku) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		//有效订单存入退货站入库记录表
		OrderBackRuku obr = new OrderBackRuku();
		obr.setCwb(co.getCwb());
		obr.setCustomerid(co.getCustomerid());//供货商id
		obr.setBranchid(co.getDeliverybranchid());//上一站（配送站） 当前站为退货站(配送到达的站点)
		obr.setCwbordertypeid(co.getCwbordertypeid());//订单类型
		obr.setConsigneename(co.getConsigneename());//收件人名字
		obr.setConsigneeaddress(co.getConsigneeaddress());//收件人地址
		obr.setCreatetime(this.getNowtime());//当前时间
		obr.setCwbstate((int)co.getCwbstate());
		this.orderBackRukuRecordDao.creOrderbackRuku(obr);//导入到退货站入库记录表

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.TuiHuoZhanRuKu;

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleBackIntoWarehousYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, driverid);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleBackIntoWarehous(user, cwb, scancwb, currentbranchid, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, false, anbaochuku, driverid);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	public String getNowtime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	private CwbOrder handleBackIntoWarehousYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long driverid) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleBackIntoWarehous(user, cwb, scancwb, currentbranchid, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false, driverid);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleBackIntoWarehous(user, cwb, scancwb, currentbranchid, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false, driverid);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleBackIntoWarehous(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum,
			long isypdjusetranscwb, boolean isypdj, boolean anbaochuku, long driverid) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), (int) driverid, 0, "");
		}

		this.logger.info("退货站入库数据批次处理完成");

		int flowordertype = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		this.logger.info("退货站正常入库-保存下一站");
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowordertype, co.getCwb());

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, System.currentTimeMillis());
		/**
		 * 退货入库交接单打印
		 */
		this.backIntoprintDAO.creBackIntoprint(co, user, driverid, nextbranchid, "", "", "", "", comment);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
		if ((co.getEmaildateid() > 0) && (this.emailDateDAO.getEmailDateById(co.getEmaildateid()).getState() == 0)) {// 如果批次为未到货
			// 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		// ==========结算退货入库扫描逻辑=======
		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
		// 如果订单为出库状态&&上一个入库站为当前操作站
		if ((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getStartbranchid() == currentbranchid)) {
			this.logger.info("重复入库");
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}

		// 起始站为站点类型
		if ((startbranch.getBranchid() != 0) && (startbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 买单结算
			if (startbranch.getAccounttype() == 1) {
				// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入退货记录
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue())
						|| ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
						|| (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					this.logger.info("===开始创建买单结算退货站点入库扫描记录===");
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue(),user,currentbranchid);
					accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue(), user.getUserid(),
							currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					this.logger.info("用户:{},创建结算退货入库扫描记录,退货站点{},退货站点{},订单号:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
			}

			// 扣款结算
			if (startbranch.getAccounttype() == 3) {
				this.logger.info("===开始创建扣款结算退货站点数据===");
				BigDecimal fee = BigDecimal.ZERO;
				// 不同的订单类型返不同的钱
				fee = this.accountDeducDetailService.getTHPaybackfee(co.getCwbordertypeid(), co.getDeliverystate(), co.getReceivablefee(), co.getPaybackfee());
				// //上门退订单
				// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// fee=co.getPaybackfee();
				// }else{//配送||其他
				// fee=co.getReceivablefee();
				// }
				AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
				accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.TuiHuo.getValue(), fee, user.getUserid(), "", 0,
						0);
				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				this.logger.info("用户:{},创建扣款结算退货站点退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
			}
		}
	}

	private void validateCwbChongFu(CwbOrder co, String scancwb, long flowordertype, long currentbranchid, long startbranchid, long nextbranchid, ExceptionCwbErrorTypeEnum exceptionCwbErrorTypeEnum) {
		// 针对一票多件多个运单号的订单，如果一个运单号同样的机构下同样的环节重复扫描的验证
		List<TranscwbOrderFlow> tcofList = this.transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndFloworderdetail(scancwb, co.getCwb(), flowordertype, currentbranchid, startbranchid, nextbranchid);
		if (tcofList.size() > 0) {
			throw new CwbException(co.getCwb(), flowordertype, exceptionCwbErrorTypeEnum);
		}
	}

	private void validateCwbState(CwbOrder co, FlowOrderTypeEnum flowordertype) {
		// 在数据库增加一个状态和操作的对应表，只有有记录的才允许操作
		CwbStateEnum cwbstate = CwbStateEnum.getByValue((int) co.getCwbstate());
		if (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(cwbstate.getValue(), flowordertype.getValue()) == null) {
			throw new CwbException(co.getCwb(), flowordertype.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, cwbstate.getText(), flowordertype.getText());
		}
		this.validateDeliveryState(co, flowordertype);
	}

	private void validateDeliveryState(CwbOrder co, FlowOrderTypeEnum flowordertype) {
		// 在数据库增加一个状态和操作的对应表，只有有记录的才允许操作
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
		if ((deliveryState != null) && (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue())) {
			if ((flowordertype != FlowOrderTypeEnum.YiFanKui) && (flowordertype != FlowOrderTypeEnum.YiShenHe)) {
				throw new CwbException(co.getCwb(), flowordertype.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, "配送成功", flowordertype.getText());
			}
		}
	}

	private void validateDeliveryStateForZhiLiu(CwbOrder co, FlowOrderTypeEnum flowordertype) {
		// 在数据库增加一个状态和操作的对应表，只有有记录的才允许操作
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
		if ((deliveryState != null) && (deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
			if (flowordertype == FlowOrderTypeEnum.TuiHuoChuZhan) {
				throw new CwbException(co.getCwb(), flowordertype.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, DeliveryStateEnum.FenZhanZhiLiu.getText(), flowordertype.getText());
			}
		}
	}

	/**
	 * 创建操作环节
	 *
	 * @param user
	 *            操作用户
	 * @param branchid
	 *            操作机构
	 * @param co
	 *            订单详情
	 * @param flowordertype
	 *            操作类型
	 * @param comment
	 *            操作备注
	 * @param credate
	 *            操作时间 Long型
	 */
	public void createFloworder(User user, long branchid, CwbOrder co, FlowOrderTypeEnum flowordertype, String comment, Long credate) {
		this.createFloworder(user, branchid, co.getCwb(), flowordertype, comment, credate);
	}

	public void baleDaoHuo(CwbOrder co) {
		if ((co.getPackagecode() != null) && (co.getPackagecode().length() > 0)) {
			Bale isbale = this.baleDAO.getBaleByBaleno(co.getPackagecode(), BaleStateEnum.KeYong.getValue());
			if (isbale != null) {
				this.baleDAO.saveForBalestate(co.getPackagecode(), BaleStateEnum.YiDaoHuo.getValue(), BaleStateEnum.KeYong.getValue());
			}
		}
	}

	private ObjectMapper om = new ObjectMapper();

	private void createFloworder(User user, long branchid, String cwb, FlowOrderTypeEnum flowordertype, String comment, Long credate) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		cwbOrder.setConsigneemobile(cwbOrder.getConsigneemobileOfkf());
		cwbOrder.setConsigneename(cwbOrder.getConsigneenameOfkf());
		cwbOrder.setConsigneephone(cwbOrder.getConsigneephoneOfkf());
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
		cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
		cwbOrderWithDeliveryState.setDeliveryState(deliveryState);
		try {
			OrderFlow of = new OrderFlow(0, cwb, branchid, new Timestamp(credate), user.getUserid(), this.om.writeValueAsString(cwbOrderWithDeliveryState).toString(), flowordertype.getValue(),
					comment);
			long floworderid = this.orderFlowDAO.creAndUpdateOrderFlow(of);
			try {
				this.orderFlowLogDAO.creAndUpdateOrderFlow(of,floworderid);
			} catch (Exception e) {
				this.logger.info("存储flow日志异常，订单号：{}",cwb);
				e.printStackTrace();
			}
			this.updateOrInsertWareHouseToBranch(cwbOrder, of);
			this.updateOutToCommen(cwbOrder, of, 0); // 出库 承运商库房
			this.updateOutToCommen_toTwoLeavelBranch(cwbOrder, of, 1); // 一级站出库二级站
			this.exceptionCwbDAO.createExceptionCwb(cwb, flowordertype.getValue(), "", user.getBranchid(), user.getUserid(), cwbOrder.getCustomerid(), 0, 0, 0, "");
			// TODO
			this.send(of);

			// 创建退货中心出入库跟踪表
			if ((flowordertype.getValue() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (flowordertype.getValue() != FlowOrderTypeEnum.TiHuo.getValue())
					&& (flowordertype.getValue() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) && (flowordertype.getValue() != FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())) {
				this.logger.warn("订单号{}订单当前状态为{}，创建退货中心出入库跟踪表", cwbOrder.getCwb(), flowordertype.getValue());
				this.backDetailService.createBackDetail(user, cwb, flowordertype.getValue(), credate);
			}

		} catch (Exception e) {
			this.logger.error("error while saveing orderflow", e);
			throw new ExplinkException(ExceptionCwbErrorTypeEnum.SYS_ERROR, cwb);
		}
	}

	public void updateOrInsertWareHouseToBranch(CwbOrder cwbOrder, OrderFlow of) {

		try {
			if ((of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {// 出库状态
				this.logger.info("存入出库记录表 订单号:{},存入的环节:{}", of.getCwb(), of.getFlowordertypeText());
				Branch b = this.branchDAO.getBranchByBranchid(of.getBranchid());
				long count = this.warehouseToBranchDAO.getcwb(cwbOrder.getCwb(), of.getBranchid());
				if (count > 0) {
					this.warehouseToBranchDAO.updateWarehouseToBranch(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()), b.getSitetype());
					this.logger.info("存入出库记录表 订单号:{},存入的环节:{},更新成功", of.getCwb(), of.getFlowordertypeText());
				} else {
					this.warehouseToBranchDAO.creWarehouseToBranch(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()), b.getSitetype());
					this.logger.info("存入出库记录表 订单号:{},存入的环节:{},插入成功", of.getCwb(), of.getFlowordertypeText());
				}
			}
		} catch (Exception e) {
			this.logger.error("error while saveing warehouseToBranch", e);
		}

	}

	/**
	 * 出给
	 *
	 * @param cwbOrder
	 * @param outbranchflag
	 */
	public void updateOutToCommen(CwbOrder cwbOrder, OrderFlow of, int outbranchflag) {
		try {
			if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {// 出库状态

				if (cwbOrder.getCwbstate() != CwbStateEnum.PeiShong.getValue()) {
					this.logger.warn("订单号{}订单当前状态为{}，不能参与发送至承运商", cwbOrder.getCwb(), cwbOrder.getCwbstate());
					return;
				}

				long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());// 查询是否存在

				List<Common> commlist = this.commonDAO.getCommonByBranchid(cwbOrder.getNextbranchid());
				if ((commlist == null) || (commlist.size() == 0)) {
					if (count > 0) {
						this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
						this.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
					}
					return;
				}
				this.logger.info("存入出库给承运商表  订单号:{}", of.getCwb());
				String commencode = commlist.get(0).getCommonnumber();

				if (count > 0) {
					this.warehouseToCommenDAO.updateWarehouseToCommen(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode,
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					this.logger.info("存入出库给承运商表 订单号:{},更新成功", of.getCwb());
				} else {
					this.warehouseToCommenDAO.creWarehouseToCommen(cwbOrder.getCwb(), cwbOrder.getCustomerid(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode, new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(of.getCredate()), "", outbranchflag);
					this.logger.info("存入出库给承运商表 订单号:{},插入成功", of.getCwb());
				}
			}
		} catch (Exception e) {
			this.logger.error("error while saveing warehouseToBranch", e);
		}

	}

	/**
	 * 一级站 出库给二级站点
	 *
	 * @param cwbOrder
	 * @param of
	 */
	public void updateOutToCommen_toTwoLeavelBranch(CwbOrder cwbOrder, OrderFlow of, int outbranchflag) {
		try {
			if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {// 出库状态

				if (cwbOrder.getCwbstate() != CwbStateEnum.PeiShong.getValue()) {
					this.logger.warn("订单号{}订单当前状态为{}，不能参与发送至承运商", cwbOrder.getCwb(), cwbOrder.getCwbstate());
					return;
				}

				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				if (!"2".equals(branch.getContractflag())) { // 如果不是二级站，则return
					return;
				}

				if (branch.getBindmsksid() > 0) { // 绑定了二级站，则需要
					Stores stores = this.storesDAO.getMaisiBranchById(branch.getBindmsksid());
					if (stores == null) {
						this.logger.warn("该迈思可站点被删除，Bindmsksid=" + branch.getBindmsksid());
						return;
					}

					Common common = this.commonDAO.getCommonByCommonnumber(stores.getB2cenum());

					this.logger.info("存入出库给承运商表  订单号:{}", of.getCwb());
					String commencode = common.getCommonnumber();
					long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());
					if (count > 0) {
						this.warehouseToCommenDAO.updateWarehouseToCommen(cwbOrder.getCwb(), of.getBranchid(), branch.getBranchid(), commencode,
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
						this.logger.info("存入出库给承运商表 订单号:{},更新成功", of.getCwb());
					} else {
						this.warehouseToCommenDAO.creWarehouseToCommen(cwbOrder.getCwb(), cwbOrder.getCustomerid(), of.getBranchid(), branch.getBranchid(), commencode, new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(of.getCredate()), "", outbranchflag);
						this.logger.info("存入出库给承运商表 订单号:{},插入成功", of.getCwb());
					}
				} else {
					long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());
					if (count > 0) {
						this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
						this.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
					}
				}

			}
		} catch (Exception e) {
			this.logger.error("error while saveing warehouseToBranch", e);
		}

	}

	public void updateOrInsertWareHouseToBranch(long branchid, String cwb, long nextBranchid, String credate) {

		try {
			Branch b = this.branchDAO.getBranchByBranchid(branchid);
			long count = this.warehouseToBranchDAO.getcwb(cwb, branchid);
			if (count > 0) {
				this.warehouseToBranchDAO.updateWarehouseToBranch(cwb, branchid, nextBranchid, credate, b.getSitetype());
			} else {
				this.warehouseToBranchDAO.creWarehouseToBranch(cwb, branchid, nextBranchid, credate, b.getSitetype());
			}
		} catch (Exception e) {
			this.logger.error("error while saveing warehouseToBranch", e);
		}
	}

	private void createTranscwbOrderFlow(User user, long branchid, String cwb, String scancwb, FlowOrderTypeEnum flowOrdertype, String comment) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
		cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
		cwbOrderWithDeliveryState.setDeliveryState(deliveryState);
		try {
			TranscwbOrderFlow tof = new TranscwbOrderFlow(0, cwb, scancwb, branchid, new Timestamp(System.currentTimeMillis()), user.getUserid(), this.om.writeValueAsString(cwbOrderWithDeliveryState)
					.toString(), flowOrdertype.getValue(), comment);
			this.transcwborderFlowDAO.creAndUpdateTranscwbOrderFlow(tof);
			// sendTranscwbOrderFlow(tof);
		} catch (Exception e) {
			this.logger.error("error while saveing orderflow", e);
			throw new ExplinkException(ExceptionCwbErrorTypeEnum.SYS_ERROR, cwb);
		}
	}

	private CwbOrder createCwbDetail(User user, long customerid, String cwb) {
		try {
			String sql = "insert into express_ops_cwb_detail (cwb,currentbranchid,customerid,emailfinishflag,cwbordertypeid,cwbstate) values(?,?,?,?,?,?)";
			this.jdbcTemplate.update(sql, cwb, user.getBranchid(), customerid, EmailFinishFlagEnum.YouHuoWuDan.getValue(), CwbOrderTypeIdEnum.Peisong.getValue(), CwbStateEnum.WUShuju.getValue());

			return this.cwbDAO.getCwbByCwb(cwb);
		} catch (Exception e) {
			this.logger.error("error while saveing cwbdetail", e);
			throw new ExplinkException(ExceptionCwbErrorTypeEnum.SYS_ERROR, cwb);
		}
	}

	public void send(OrderFlow of) {
		String enableOrderFlowTask = this.systemInstallService.getParameter("enableOrderFlowTask");
		if ("yes".equals(enableOrderFlowTask)) {
			// 任务调度方式处理里orderFlow
			this.createOrderFlowTask(of);
		} else {
			// JMS消息模式处理orderFlow
			try {
				this.orderFlowProducerTemplate.sendBodyAndHeader(null, "orderFlow", this.om.writeValueAsString(of));
			} catch (Exception ee) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {// 导入数据的话，手工调用保存订单号和运单号的表
					this.logger.info("调接口执行运单号保存 单号：{}", of.getCwb());
					this.transCwbService.saveTransCwbByFloworderdetail(of.getFloworderdetail());
				}
				// TODO jms异常写入监控表
				String optime = DateTimeUtil.formatDateHour(new Date());
				ExpressSysMonitor monitor = this.expressSysMonitorDAO.getMaxOpt("JMSDmpFlow");
				// 系统上线第一次加载
				if (monitor == null) {
					ExpressSysMonitor newmonitor = new ExpressSysMonitor();
					newmonitor.setOptime(optime);
					newmonitor.setType("JMSDmpFlow");
					this.expressSysMonitorDAO.save(newmonitor);
				} else {
					// 后续加载
					String preoptime = monitor.getOptime();
					if (!optime.equals(preoptime)) {
						ExpressSysMonitor newmonitor = new ExpressSysMonitor();
						newmonitor.setOptime(optime);
						newmonitor.setType("JMSDmpFlow");
						this.expressSysMonitorDAO.save(newmonitor);
					}
				}

				this.logger.error("send flow message error", ee);
			}
		}
	}

	/**
	 * 创建5个任务对应原分发给5个queue
	 *
	 * @param of
	 */
	private void createOrderFlowTask(OrderFlow of) {
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_OMS1, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_OMSB2C, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_EDIT_SHOW_INFO, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_RECIEVE_GOODS, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_TRANSCWB, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_SMS, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);

		String enableAccountProject = this.systemInstallService.getParameter("enableAccountProject");
		if ("yes".equals(enableAccountProject)) {
			this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_ORDER_FLOW_ACCOUNT, Constants.REFERENCE_TYPE_ORDER_FLOW_ID, String.valueOf(of.getFloworderid()), true);
		}
	}

	// public void sendTranscwbOrderFlow(TranscwbOrderFlow tof) {
	//
	// try {
	// orderFlowProducerTemplate.sendBodyAndHeader(null, "transcwborderFlow",
	// om.writeValueAsString(tof));
	// } catch (Exception ee) {
	// logger.error("send transcwborderflow message error", ee);
	// }
	// }

	/**
	 * 中转站出库扫描
	 *
	 * @param cwb
	 * @param owgid
	 * @param driverid
	 * @param truckid
	 * @param anbaochuku
	 *            是否按包出库
	 */
	public CwbOrder changeoutWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			long reasonid, boolean iszhongzhuanout, boolean anbaochuku) {

		this.logger.info("开始中转站出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.changeOutWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, iszhongzhuanout,
				System.currentTimeMillis(), anbaochuku);
	}

	@Transactional
	public CwbOrder changeOutWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut,
			String comment, String packagecode, boolean isauto, long reasonid, boolean iszhongzhuanout, Long credate, boolean anbaochuku) {
		Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {// 是否拥有
			// 请指出库权限
			// 1是
			// 0
			// 否
			// 默认1
			forceOut = false;
		}

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		boolean aflag = false;
		if ((ifBranch != null) && (ifBranch.getSitetype() == 2)) {
			List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentbranchid, branchid, 2);
			for (BranchRoute r : routelist) {
				if (branchid == r.getToBranchId()) {
					aflag = true;
				}
			}
			if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getNextbranchid() != 0) && !aflag && (branchid > 0) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO
						.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			}
		} else if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
					.getBranchname());
		}

		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())))
				&& (co.getCurrentbranchid() != currentbranchid)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		// =====加入按包出库标识 zs=====
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleChangeOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co,
					FlowOrderTypeEnum.ZhongZhuanZhanChuKu, isypdjusetranscwb, iszhongzhuanout, aflag, credate, userbranch);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid))
					&& (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co,
						FlowOrderTypeEnum.ZhongZhuanZhanChuKu, isypdjusetranscwb, false, iszhongzhuanout, aflag, credate, anbaochuku, userbranch);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// 原包号处理
		// disposePackageCode(packagecode, scancwb, user, co);

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleChangeOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment,
			String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean iszhongzhuanout, boolean aflag, Long credate,
			Branch userbranch) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum,
						isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, userbranch);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				long realscannum = co.getScannum() + 1;
				if (isypdjusetranscwb == 1) {
					// 一票多件使用运单号时，扫描次数需要计算
					realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, co.getNextbranchid()) + 1;
				}
				this.cwbDAO.updateScannum(co.getCwb(), realscannum);

				// 结算更新扫描件数
				this.accountCwbDetailDAO.updateAccountCwbDetailScannum(co.getCwb(), realscannum);

				co.setScannum(realscannum);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, co.getNextbranchid());
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, forceOut);
			this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb,
					true, iszhongzhuanout, aflag, credate, false, userbranch);
		}
		// //包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleChangeOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean iszhongzhuanout, boolean aflag, Long credate,
			boolean anbaochuku, Branch userbranch) {
		this.validateCwbState(co, flowOrderTypeEnum);
		if ((co.getFlowordertype() != flowOrderTypeEnum.getValue()) || (co.getStartbranchid() != currentbranchid)) {
			this.validateStateTransfer(co, flowOrderTypeEnum);
		}
		if (iszhongzhuanout) {
			// 中转出站操作根据系统设置，是否只有审核的订单才可以中转出站
			String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
			if (isUseAuditZhongZhuan.equals("yes") && (this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCount(co.getCwb()) == 0)) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shen_Qing_Zhong_Zhuan_Wei_Shen_He_Cheng_Gong_Error);
			}

			// 非本操作站点的订单不允许出库（中转出站）
			if (co.getCurrentbranchid() != currentbranchid) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.FeiBenZhanDianDingDanBuYunXuZhongZhuanChuZhan);
			}
		}

		if ((userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) && (co.getCwbstate() == CwbStateEnum.ZhongZhuan.getValue())) { // 如果当前站是中转站，并且cwbstate=中转
			this.cwbDAO.updateCwbState(scancwb, CwbStateEnum.PeiShong);
		}

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, iszhongzhuanout);
		}

		// =====加入按包出库标识 zs==========
		if (!anbaochuku) {
			branchid = this.getNextBranchid(branchid, forceOut, co, user, aflag);
		}

		// 已出库 向打印列表 插入数据
		this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), branchid, co.getDeliverid(), co.getCustomerid(), 0, 0, packagecode);

		if (reasonid != 0) {
			Reason reason = this.reasonDAO.getReasonByReasonid(reasonid);
			if (reason != null) {
				comment = reason.getReasoncontent();
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
				} catch (Exception e) {
					this.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, cwb);

		if (forceOut && iszhongzhuanout) {
			Branch nextbranch = this.branchDAO.getBranchByBranchid(branchid);
			if (nextbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.cwbDAO.updateDeliveryBranchidByCwb(nextbranch.getBranchname(), branchid, cwb);
			}
		}

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			long realscannum = 1;
			if (isypdjusetranscwb == 1) {
				// 一票多件使用运单号时，扫描次数需要计算
				realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
			}
			this.cwbDAO.updateScannum(co.getCwb(), realscannum);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}
		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid);
		}

		// ============结算逻辑出库扫描=======================
		// Branch userbranch=branchDAO.getBranchByBranchid(currentbranchid);
		Branch nextbranch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
		Branch tobranch = this.branchDAO.getBranchByBranchid(branchid);

		// 强制出库&&当前订单状态为出库状态 插入一条中转站
		if ((forceOut == true) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			// 买单结算
			if (nextbranch.getAccounttype() == 1) {
				// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue())
						|| ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
						|| (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), user.getUserid(),
							currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					this.logger.info("用户:{},创建买单结算该站冲款扫描记录,站点id{},出库站{},订单号{}", new Object[] { user.getRealname(), co.getNextbranchid(), userbranch.getBranchname(), co.getCwb() });
				}
			}
			// 扣款结算
			if (nextbranch.getAccounttype() == 3) {
				this.logger.info("===开始创建扣款结算强制出库中转货款数据===");
				BigDecimal fee = BigDecimal.ZERO;
				// //上门退订单
				// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// fee=co.getPaybackfee();
				// }else{//配送||其他
				fee = co.getReceivablefee();
				// }
				AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
				accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), fee,
						user.getUserid(), "强制出库", 0, 1);

				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				this.logger.info("用户:{},创建扣款结算强制出库中转退货：站点id{},代收货款{}元,id：{}", new Object[] { user.getRealname(), co.getNextbranchid(), fee, id });
			}
		}

		// =====================中转出站==========================
		if (tobranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			// ==============到错货 ==============
			if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				// 1.对最后一条出库记录【KouKuan】进行【预扣款】返款
				this.accountDeductRecordService.returnLastChuKu(co.getCwb(), user);
				// 2.当前操作站点产生一条出库记录
				if (userbranch.getAccounttype() == 3) {
					// 产生一条出库记录
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					this.logger.info("===开始创建扣款结算中转出站出库记录===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(currentbranchid);
					BigDecimal koukuan = co.getReceivablefee();// 扣款
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款
					BigDecimal balancevirt = branchLock.getBalancevirt();// 伪余额
					BigDecimal debtvirt = branchLock.getDebtvirt();// 伪欠款
					// 扣款逻辑
					Map feeMap = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, koukuan);
					balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					// 修改branch表 的余额、欠款
					this.branchDAO.updateForFeeAndVirt(currentbranchid, balance, debt, balancevirt, debtvirt);
					// 插入一条扣款记录
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan,
							branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货出库", co.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), fee, user.getUserid(), "到错货出库",
							recordid, 1);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算到错货出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), fee });
					this.logger.info("用户:对{}站点进行到错货出库扣款：原余额{}元，原欠款{}元。出库{}后，余额{}元，欠款{}元", new Object[] { branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(), koukuan, balance,
							debt });
				}

			}
		}

		// =========所选择的出库下一站为站点类型==========
		if ((userbranch.getBranchid() != 0) && (tobranch.getBranchid() != 0) && (tobranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 买单结算
			if (tobranch.getAccounttype() == 1) {
				// 当前操作站点为库房or中转站or退货站
				long flowordertype = 0;
				if (userbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
					// 出库是否根据结算类型和未结算的账单限制出库开关
					String jiesuanchuku = this.systemInstallDAO.getSystemInstall("jiesuanchuku") == null ? "no" : this.systemInstallDAO.getSystemInstall("jiesuanchuku").getValue();
					if ("yes".equals(jiesuanchuku)) {
						long jiesuan = this.accountCwbSummaryDAO.getJiesuanchukuCount(0, 1, tobranch.getBranchid());
						if (jiesuan > 0) {
							throw new CwbException(co.getCwb(), AccountFlowOrderTypeEnum.KuFangChuKu.getValue(), ExceptionCwbErrorTypeEnum.ZhangDanWeiJieSuan);
						}
					}
					flowordertype = AccountFlowOrderTypeEnum.KuFangChuKu.getValue();// 库房出库扫描
					this.logger.info("用户:{},创建结算库房出库扫描记录,站点{},出库库房{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue();// 中转出库扫描
					this.logger.info("用户:{},创建结算中转站点出库扫描记录,站点{},出库中转站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();// 退货出库扫描
					this.logger.info("用户:{},创建结算退货站点出库扫描记录,站点{},出库退货站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
				accountCwbDetail = this.loadFormForAccountCwbDetail(co, branchid, flowordertype, user, currentbranchid);
				this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
			}// 买单结算End

			// 扣款结算
			if (tobranch.getAccounttype() == 3) {
				// 当前操作站点为库房or中转站or退货站
				if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue()) || (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())
						|| (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue())) {
					this.logger.info("===开始扣款===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(branchid);
					BigDecimal koukuan = co.getReceivablefee();// 代收货款
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款

					// 扣款逻辑
					Map feeMap = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

					this.logger.info("===开始修改账户===");
					this.branchDAO.updateForFee(branchid, balance, debt);

					this.logger.info("===插入一条扣款记录===");
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(),
							balance, user, branchLock.getDebt(), debt, "", co.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					this.logger.info("===插入一条订单记录===");
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), co.getReceivablefee(),
							user.getUserid(), "", recordid, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户{},对{}站点进行出库扣款：扣款记录id{}，原余额{}元，原欠款{}元。扣款{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), recordid, branchLock.getBalance(),
							branchLock.getDebt(), koukuan, balance, debt });
					this.logger.info("===扣款结束===");
				}
			}

		}
	}

	/**
	 * 出库扫描
	 *
	 * @param cwb
	 * @param owgid
	 * @param driverid
	 * @param truckid
	 * @param anbaochuku
	 *            是否按包出库
	 */
	public CwbOrder outWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			long reasonid, boolean iszhongzhuanout, boolean anbaochuku) {

		this.logger.info("开始出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.outWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, iszhongzhuanout,
				System.currentTimeMillis(), anbaochuku);
	}

	@Transactional
	public CwbOrder outWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment,
			String packagecode, boolean isauto, long reasonid, boolean iszhongzhuanout, Long credate, boolean anbaochuku) {
		Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {// 是否拥有
			// 请指出库权限
			// 1是
			// 0
			// 否
			// 默认1
			forceOut = false;
		}

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		boolean aflag = false;
		if ((ifBranch != null) && (ifBranch.getSitetype() == 2)) {
			List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentbranchid, branchid, 2);
			for (BranchRoute r : routelist) {
				if (branchid == r.getToBranchId()) {
					aflag = true;
				}
			}
			if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getNextbranchid() != 0) && !aflag && (branchid > 0) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
						.getBranchname());
			}
		} else if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
					.getBranchname());
		}

		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())))
				&& (co.getCurrentbranchid() != currentbranchid)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		// =====加入按包出库标识 zs=====
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co,
					FlowOrderTypeEnum.ChuKuSaoMiao, isypdjusetranscwb, iszhongzhuanout, aflag, credate, driverid, truckid);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid))
					&& (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.ChuKuSaoMiao,
						isypdjusetranscwb, false, iszhongzhuanout, aflag, credate, anbaochuku, driverid, truckid);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// 原包号处理
		// disposePackageCode(packagecode, scancwb, user, co);

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	// private void disposePackageCode(String packagecode,String cwb,User
	// user,CwbOrder co){
	// //包号处理开始
	// if (!packagecode.equals("") && packagecode.length() > 0) {
	// Bale isbale = baleDAO.getBaleByBaleno(packagecode,
	// BaleStateEnum.KeYong.getValue());
	// if (packagecode.equals("0")) {
	// throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
	// ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI);
	// } else {
	// if (!packagecode.equals("") && packagecode.length() > 0 && isbale ==
	// null) {
	// Bale bale = new Bale();
	// bale.setBaleno(packagecode);
	// bale.setBranchid(user.getBranchid());
	// bale.setBalestate(BaleStateEnum.KeYong.getValue());
	// bale.setNextbranchid(co.getNextbranchid());
	// bale.setCwbcount(1);
	// long baleid = baleDAO.createBale(bale);
	// //添加包号和订单的关系表
	// String sql =
	// "update express_ops_cwb_detail set packagecode=? where cwb=? and state=1";
	// jdbcTemplate.update(sql, packagecode, cwb);
	// baleCwbDAO.createBale(baleid, packagecode, cwb);
	// }else if (!packagecode.equals("") && packagecode.length() > 0 && isbale
	// != null) {
	// if(isbale.getNextbranchid() != co.getNextbranchid()){
	// Branch branch = branchDAO.getBranchByBranchid(isbale.getNextbranchid());
	// Branch nextbranch = branchDAO.getBranchByBranchid(co.getNextbranchid());
	// throw new CwbException(cwb,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
	// ExceptionCwbErrorTypeEnum.Bao_Hao_Bu_Keyi_Duozhan_Shiyong,
	// branch==null?"":branch.getBranchname(),nextbranch==null?"":nextbranch.getBranchname());
	// }
	// if(co.getScannum()<2){//当扫描的订单是第二件的时候，不记录在此包中的订单件数
	// baleDAO.saveForBaleCount(isbale.getId(), isbale.getCwbcount()+1);
	// }
	// String sql =
	// "update express_ops_cwb_detail set packagecode=? where cwb=? and state=1";
	// jdbcTemplate.update(sql, packagecode, cwb);
	// //添加包号和订单的关系表
	// baleCwbDAO.createBale(isbale.getId(), packagecode, cwb);
	// }
	// }
	// }
	// //包号结束
	// }

	private CwbOrder handleOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment,
			String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean iszhongzhuanout, boolean aflag, Long credate,
			long driverid, long truckid) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb,
						true, iszhongzhuanout, aflag, credate, false, driverid, truckid);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				long realscannum = co.getScannum() + 1;
				if (isypdjusetranscwb == 1) {
					// 一票多件使用运单号时，扫描次数需要计算
					realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, co.getNextbranchid()) + 1;
				}
				this.cwbDAO.updateScannum(co.getCwb(), realscannum);

				// 结算更新扫描件数
				this.accountCwbDetailDAO.updateAccountCwbDetailScannum(co.getCwb(), realscannum);

				co.setScannum(realscannum);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, co.getNextbranchid());
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, forceOut);
			this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true,
					iszhongzhuanout, aflag, credate, false, driverid, truckid);
		}
		// //包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void validateYipiaoduojianState(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean forceOut) {
		if (isypdjusetranscwb != 1) {
			return;
		}
		if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			return;
		}
		if (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
			return;
		}
		if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			return;
		}

		int alength = 0;
		if (co.getTranscwb().length() > 0) {
			String trans = co.getTranscwb().replaceAll(":", ",");
			String[] a = trans.split(",");
			for (String m : a) {
				if (m.trim().length() > 0) {
					alength++;
				}
			}
		}
		SystemInstall switchInstall = this.systemInstallDAO.getSystemInstall("ypdjpathtong");
		if (switchInstall.getValue().equals("0")) {
			// 针对一票多件多个订单号的订单扫描其中运单号,未匹配站点,出库给不同下一站的时候会更改扫描次数,并且重复扫描同一运单号,再扫其他单号的时候会直接报重复出库的问题
			if (!forceOut && (co.getSendcarnum() > co.getScannum()) && (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co.getSendcarnum())) {
				throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(),
						flowOrderTypeEnum.getText());
			}
		} else if (switchInstall.getValue().equals("1")) {
			// 一票多件时在领货前的操作是不阻挡的，但在领货的时候会拦截一票多件前一环节件数不对而阻拦
			if (!forceOut && (co.getSendcarnum() > co.getScannum())
					&& ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()))
					&& (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co.getSendcarnum())) {
				throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(),
						flowOrderTypeEnum.getText());

			}
		}

	}

	private void handleOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean iszhongzhuanout, boolean aflag, Long credate,
			boolean anbaochuku, long driverid, long truckid) {
		this.validateCwbState(co, flowOrderTypeEnum);
		if ((co.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getStartbranchid() != currentbranchid)) {
			this.validateStateTransfer(co, FlowOrderTypeEnum.ChuKuSaoMiao);
		}
		if (iszhongzhuanout) {
			// 中转出站操作根据系统设置，是否只有审核的订单才可以中转出站

			int changealowflag = this.getChangealowflagById(co);
			if((changealowflag ==1) && (co.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue())){
				if (this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCount(co.getCwb()) == 0){
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Shen_Qing_Zhong_Zhuan_Wei_Shen_He_Cheng_Gong_Error);
				}
			}


			// 非本操作站点的订单不允许出库（中转出站）
			if (co.getCurrentbranchid() != currentbranchid) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FeiBenZhanDianDingDanBuYunXuZhongZhuanChuZhan);
			}

//			// 配送单是否允许做中转 yes 允许做中转， no不允许   2015.05.27此控制要取消。
//			String isPeisongAllowtoZhongZhuan = this.systemInstallDAO.getSystemInstall("isPeisongAllowtoZhongZhuan") == null ? "yes" : this.systemInstallDAO.getSystemInstall(
//					"isPeisongAllowtoZhongZhuan").getValue();
//			if ("no".equalsIgnoreCase(isPeisongAllowtoZhongZhuan) && (co.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
//				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Peisong_Bu_YunXu_ZhongZhuan);
//			}

			Branch sesionBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			if ((sesionBranch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (co.getCwbstate() == CwbStateEnum.PeiShong.getValue())) { // 如果当前站是中转站，并且cwbstate=中转
				this.cwbDAO.updateCwbState(scancwb, CwbStateEnum.ZhongZhuan);
			}

			if (reasonid != 0) {
				Reason reason1 = this.reasonDAO.getReasonByReasonid(reasonid);
				if (reason1 != null) {
					this.cwbDAO.updateZhongzhuanReason(cwb, reason1.getReasonid(), reason1.getReasoncontent());

				}
			}

		}

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), co, requestbatchno, scancwb, iszhongzhuanout);
		}

		// =====加入按包出库标识 zs==========
		if (!anbaochuku) {
			branchid = this.getNextBranchid(branchid, forceOut, co, user, aflag);
		}

		// 已出库 向打印列表 插入数据
		this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), branchid, co.getDeliverid(), co.getCustomerid(), driverid, truckid, packagecode);

		// 中转出库，插入数据到中转统计表中
		this.produceTransferResStastics(co, DateTimeUtil.getNowTime(), user, reasonid, 1);

		// 更新订单打印的包号信息
		if (!"".equals(co.getPackagecode())) {
			Bale bale = this.baleDAO.getBaleOneByBaleno(co.getPackagecode());
			if (bale != null) {
				this.groupDetailDAO.updateGroupDetailByBale(bale.getId(), co.getPackagecode(), cwb, user.getBranchid());
			}
		}

		if (reasonid != 0) {
			Reason reason = this.reasonDAO.getReasonByReasonid(reasonid);
			if (reason != null) {
				comment = reason.getReasoncontent();
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
				} catch (Exception e) {
					this.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), 0, currentbranchid, branchid, cwb);

		if (forceOut && iszhongzhuanout) {
			Branch nextbranch = this.branchDAO.getBranchByBranchid(branchid);
			if (nextbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.cwbDAO.updateDeliveryBranchidByCwb(nextbranch.getBranchname(), branchid, cwb);
			}
		}

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			long realscannum = 1;
			if (isypdjusetranscwb == 1) {
				// 一票多件使用运单号时，扫描次数需要计算
				realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
			}
			this.cwbDAO.updateScannum(co.getCwb(), realscannum);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}
		this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.ChuKuSaoMiao, comment, credate);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid);
		}

		// ============结算逻辑出库扫描=======================
		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch nextbranch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
		Branch tobranch = this.branchDAO.getBranchByBranchid(branchid);

		// 强制出库&&当前订单状态为出库状态 插入一条中转站
		if ((forceOut == true) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {
			// 买单结算
			if (nextbranch.getAccounttype() == 1) {
				// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue())
						|| ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
						|| (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), user.getUserid(),
							currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					this.logger.info("用户:{},创建买单结算该站冲款扫描记录,站点id{},出库站{},订单号{}", new Object[] { user.getRealname(), co.getNextbranchid(), userbranch.getBranchname(), co.getCwb() });
				}
			}
			// 扣款结算
			if (nextbranch.getAccounttype() == 3) {
				this.logger.info("===开始创建扣款结算强制出库中转货款数据===");
				BigDecimal fee = BigDecimal.ZERO;
				// //上门退订单
				// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// fee=co.getPaybackfee();
				// }else{//配送||其他
				fee = co.getReceivablefee();
				// }
				AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
				accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), fee,
						user.getUserid(), "强制出库", 0, 1);

				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				this.logger.info("用户:{},创建扣款结算强制出库中转退货：站点id{},代收货款{}元,id：{}", new Object[] { user.getRealname(), co.getNextbranchid(), fee, id });
			}
		}

		// =====================中转出站==========================
		if (tobranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			// ==============到错货 ==============
			if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				// 1.对最后一条出库记录【KouKuan】进行【预扣款】返款
				this.accountDeductRecordService.returnLastChuKu(co.getCwb(), user);
				// 2.当前操作站点产生一条出库记录
				if (userbranch.getAccounttype() == 3) {
					// 产生一条出库记录
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					this.logger.info("===开始创建扣款结算中转出站出库记录===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(currentbranchid);
					BigDecimal koukuan = co.getReceivablefee();// 扣款
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款
					BigDecimal balancevirt = branchLock.getBalancevirt();// 伪余额
					BigDecimal debtvirt = branchLock.getDebtvirt();// 伪欠款
					// 扣款逻辑
					Map feeMap = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balancevirt, debtvirt, koukuan);
					balancevirt = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debtvirt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());
					// 修改branch表 的余额、欠款
					this.branchDAO.updateForFeeAndVirt(currentbranchid, balance, debt, balancevirt, debtvirt);
					// 插入一条扣款记录
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan,
							branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货出库", co.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), fee, user.getUserid(), "到错货出库",
							recordid, 1);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户:{},创建扣款结算到错货出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), fee });
					this.logger.info("用户:对{}站点进行到错货出库扣款：原余额{}元，原欠款{}元。出库{}后，余额{}元，欠款{}元", new Object[] { branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(), koukuan, balance,
							debt });
				}

			}
		}

		// =========所选择的出库下一站为站点类型==========
		if ((userbranch.getBranchid() != 0) && (tobranch.getBranchid() != 0) && (tobranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 买单结算
			if (tobranch.getAccounttype() == 1) {
				// 当前操作站点为库房or中转站or退货站
				long flowordertype = 0;
				if (userbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
					// 出库是否根据结算类型和未结算的账单限制出库开关
					String jiesuanchuku = this.systemInstallDAO.getSystemInstall("jiesuanchuku") == null ? "no" : this.systemInstallDAO.getSystemInstall("jiesuanchuku").getValue();
					if ("yes".equals(jiesuanchuku)) {
						long jiesuan = this.accountCwbSummaryDAO.getJiesuanchukuCount(0, 1, tobranch.getBranchid());
						if (jiesuan > 0) {
							throw new CwbException(co.getCwb(), AccountFlowOrderTypeEnum.KuFangChuKu.getValue(), ExceptionCwbErrorTypeEnum.ZhangDanWeiJieSuan);
						}
					}
					flowordertype = AccountFlowOrderTypeEnum.KuFangChuKu.getValue();// 库房出库扫描
					this.logger.info("用户:{},创建结算库房出库扫描记录,站点{},出库库房{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue();// 中转出库扫描
					this.logger.info("用户:{},创建结算中转站点出库扫描记录,站点{},出库中转站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();// 退货出库扫描
					this.logger.info("用户:{},创建结算退货站点出库扫描记录,站点{},出库退货站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
				accountCwbDetail = this.loadFormForAccountCwbDetail(co, branchid, flowordertype, user, currentbranchid);
				this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
			}// 买单结算End

			// 扣款结算
			if (tobranch.getAccounttype() == 3) {
				// 当前操作站点为库房or中转站or退货站
				if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue()) || (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())
						|| (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue())) {
					this.logger.info("===开始扣款===");
					// 锁住该站点记录
					Branch branchLock = this.branchDAO.getBranchByBranchidLock(branchid);
					BigDecimal koukuan = co.getReceivablefee();// 代收货款
					BigDecimal credit = branchLock.getCredit();// 信用额度
					BigDecimal balance = branchLock.getBalance();// 余额
					BigDecimal debt = branchLock.getDebt();// 欠款

					// 扣款逻辑
					Map feeMap = new HashMap();
					feeMap = this.accountDeductRecordService.subBranchFee(credit, balance, debt, koukuan);
					balance = new BigDecimal("".equals(feeMap.get("balance").toString()) ? "0" : feeMap.get("balance").toString());
					debt = new BigDecimal("".equals(feeMap.get("debt").toString()) ? "0" : feeMap.get("debt").toString());

					this.logger.info("===开始修改账户===");
					this.branchDAO.updateForFee(branchid, balance, debt);

					this.logger.info("===插入一条扣款记录===");
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(),
							balance, user, branchLock.getDebt(), debt, "", co.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					this.logger.info("===插入一条订单记录===");
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), co.getReceivablefee(),
							user.getUserid(), "", recordid, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					this.logger.info("用户{},对{}站点进行出库扣款：扣款记录id{}，原余额{}元，原欠款{}元。扣款{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), recordid, branchLock.getBalance(),
							branchLock.getDebt(), koukuan, balance, debt });
					this.logger.info("===扣款结束===");
				}
			}

		}
	}

	/**
	 * 通过中转一级原因查询是否需中转
	 * @param co
	 * @return
	 */
	private int getChangealowflagById(CwbOrder co) {
		long firstchangereasonid =  co.getFirstchangereasonid(); //一级滞留原因
		Reason reason = this.reasonDAO.getReasonByReasonid(firstchangereasonid);

		int changealowflag =reason==null?0:reason.getChangealowflag();
		return changealowflag;
	}
	/**
	 * 通过中转一级原因查询是否需中转
	 * @param co
	 * @return
	 */
	public int getChangealowflagByIdAdd(CwbOrder co) {
		long firstchangereasonid =  co.getFirstchangereasonid(); //一级滞留原因
		Reason reason = this.reasonDAO.getReasonByReasonid(firstchangereasonid);

		int changealowflag =reason==null?0:reason.getChangealowflag();
		return changealowflag;
	}

	public CwbOrder kdkoutWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			long reasonid) {

		this.logger.info("开始库对库出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.kdkoutWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid);
	}

	@Transactional
	public CwbOrder kdkoutWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut,
			String comment, String packagecode, boolean isauto, long reasonid) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {
			forceOut = false;
		}

		if (!packagecode.equals("") && (packagecode.length() > 0)) {
			Bale isbale = this.baleDAO.getBaleByBaleno(packagecode, BaleStateEnum.KeYong.getValue());
			if (packagecode.equals("0")) {
				throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI);
			} else {
				if (!packagecode.equals("") && (packagecode.length() > 0) && (isbale == null)) {
					Bale bale = new Bale();
					bale.setBaleno(packagecode);
					bale.setBranchid(user.getBranchid());
					bale.setBalestate(BaleStateEnum.KeYong.getValue());
					this.baleDAO.createBale(bale);
				}
			}
		}

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
					.getBranchname());
		}

		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())))
				&& (co.getCurrentbranchid() != currentbranchid)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleKDKOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co,
					FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			// 库对库出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue())
					&& ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0)) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao,
						isypdjusetranscwb, false);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleKDKOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment,
			String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb,
						true);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				long realscannum = co.getScannum() + 1;
				if (isypdjusetranscwb == 1) {
					// 一票多件使用运单号时，扫描次数需要计算
					realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
				}
				this.cwbDAO.updateScannum(co.getCwb(), realscannum);
				co.setScannum(realscannum);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, co.getNextbranchid());
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, forceOut);
			this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleKDKOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode,
			boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj) {
		this.validateCwbState(co, flowOrderTypeEnum);

		if ((co.getFlowordertype() != FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) || (co.getStartbranchid() != currentbranchid)) {
			this.validateStateTransfer(co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao);
		}

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), co, requestbatchno, scancwb, false);
		}

		branchid = this.getNextBranchid(branchid, forceOut, co, user, false);
		this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), branchid, co.getDeliverid(), co.getCustomerid(), 0, 0, "");

		if (!co.getPackagecode().equals(packagecode)) {
			this.logger.info("cwb package code: {} to {}", co.getPackagecode(), packagecode);
		}

		if (reasonid != 0) {
			Reason reason = this.reasonDAO.getReasonByReasonid(reasonid);
			if (reason != null) {
				comment = reason.getReasoncontent();
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
				} catch (Exception e) {
					this.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=?,packagecode=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), 0, currentbranchid, branchid, packagecode, cwb);

		if (forceOut) {
			Branch nextbranch = this.branchDAO.getBranchByBranchid(branchid);
			if (nextbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.cwbDAO.updateDeliveryBranchidByCwb(nextbranch.getBranchname(), branchid, cwb);
			}
		}

		long realscannum = 1;
		if (isypdjusetranscwb == 1) {
			// 一票多件使用运单号时，扫描次数需要计算
			realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
		}
		this.cwbDAO.updateScannum(co.getCwb(), realscannum);
		this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, comment, System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid);
		}
	}

	/**
	 * 退货出站扫描
	 *
	 * @param cwb
	 * @param owgid
	 * @param driverid
	 * @param truckid
	 */
	public CwbOrder outUntreadWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagename,
			boolean anbaochuku) {
		this.logger.info("开始退货出站处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		OrderBackCheck obc = this.orderBackCheckDAO.getOrderBackCheckByCheckstate(cwb);
		if(obc!=null){
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.Tui_huo_chu_zhan_dai_shen_he);
		}
		return this.outUntreadWarehousHandle(user, user.getBranchid(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, forceOut, comment, packagename, anbaochuku);
	}

	@Transactional
	public CwbOrder outUntreadWarehousHandle(User user, long currentbranchid, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut,
			String comment, String packagecode, boolean anbaochuku) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {
			forceOut = false;
		}
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if(co.getCurrentbranchid()!=currentbranchid)
		{
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}
		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
					.getBranchname());
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		// =====加入按包出库标识zs====
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this.handleOutUntreadWarehousYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, FlowOrderTypeEnum.TuiHuoChuZhan, isypdjusetranscwb,
					packagecode);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getStartbranchid() == currentbranchid) && (co.getNextbranchid() == branchid) && (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, FlowOrderTypeEnum.TuiHuoChuZhan, isypdjusetranscwb, false,
						anbaochuku);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// //原包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleOutUntreadWarehousYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment,
			CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, String packagecode) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				long realscannum = co.getScannum() + 1;
				if (isypdjusetranscwb == 1) {
					// 一票多件使用运单号时，扫描次数需要计算
					realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
				}
				this.cwbDAO.updateScannum(co.getCwb(), realscannum);
				co.setScannum(realscannum);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, forceOut);
			this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
		}
		// disposePackageCode(packagecode, scancwb, user, co);
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleOutUntreadWarehous(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, CwbOrder co,
			FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);
		this.validateDeliveryStateForZhiLiu(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		//退货出站前做订单校验（判断是否在退货申请表中，以及处于什么状态）
		OrderBackCheck obc = this.orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
		if(obc!=null){
			if(obc.getCheckstate()==1){
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shen_Qing_Tui_Huo_Wei_Shen_He_Cheng_Gong_Error);
			}else if(obc.getCheckresult()==2){
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shen_Qing_Tui_Huo_Zhi_Liu_Wu_Fa_Tui_Huo_Error);
			}
		}

		this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");

		// =====加入按包出库标识 zs==========
		if (!anbaochuku) {
			branchid = this.getNextBranchid(branchid, forceOut, co, user, false);// xiugai
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, user.getBranchid(), branchid, cwb);

		// 更新订单打印的包号信息
		if (!"".equals(co.getPackagecode())) {
			Bale bale = this.baleDAO.getBaleOneByBaleno(co.getPackagecode());
			if (bale != null) {
				this.groupDetailDAO.updateGroupDetailByBale(bale.getId(), co.getPackagecode(), cwb, user.getBranchid());
			}
		}

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			long realscannum = 1;
			if (isypdjusetranscwb == 1) {
				// 一票多件使用运单号时，扫描次数需要计算
				realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
			}
			this.cwbDAO.updateScannum(co.getCwb(), realscannum);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}

	}

	public CwbOrder handleDaocuohuo(User user, String cwb, String scancwb, String comment) {
		cwb = this.translateCwb(cwb);

		return this.DaocuohuoHandle(user, cwb, scancwb, comment);
	}

	@Transactional
	public CwbOrder DaocuohuoHandle(User user, String cwb, String scancwb, String comment) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleDaocuohuoYipiaoduojian(user, cwb, scancwb, comment, co, FlowOrderTypeEnum.DaoCuoHuoChuLi, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleDaocuohuoHandle(user, cwb, scancwb, comment, co, FlowOrderTypeEnum.DaoCuoHuoChuLi, isypdjusetranscwb, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleDaocuohuoYipiaoduojian(User user, String cwb, String scancwb, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb) {

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleDaocuohuoHandle(user, cwb, scancwb, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleDaocuohuoHandle(user, cwb, scancwb, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleDaocuohuoHandle(User user, String cwb, String scancwb, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!StringUtils.hasLength(comment)) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "到错货原因");
		}
		// updateNextBranchId(cwb);

		String sql = "update express_ops_cwb_detail set nextbranchid=?,currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, co.getCurrentbranchid(), user.getBranchid(), flowOrderTypeEnum.getValue(), cwb);

		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, comment, System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
	}

	private long getNextBranchid(long branchid, boolean forceOut, CwbOrder co, User user, boolean aflag) {
		long nextBranchid = this.getNextBranchId(co, user);
		long currentbranchtype = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();
		long type = this.branchDAO.getBranchByBranchid(branchid).getSitetype();

		if (branchid <= 0) {
			if (nextBranchid <= 0) {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.MU_DI_ZHAN_WEI_ZHI);
			} else {
				branchid = nextBranchid;
			}
		}

		if ((branchid > 0) && (nextBranchid == 0)) {
			return branchid;
		}
		if ((currentbranchtype == 2) && aflag) {
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid())
					&& (currentbranchtype == BranchEnum.ZhanDian.getValue()) && ((type == BranchEnum.ZhongZhuan.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				this.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			}
		} else if ((branchid > 0) && (branchid != nextBranchid) && !forceOut) {
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid())
					&& (currentbranchtype == BranchEnum.ZhanDian.getValue()) && ((type == BranchEnum.ZhongZhuan.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				this.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			} else {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			}
		}
		return branchid;
	}

	private long getNextBranchId(CwbOrder co, User user) {
		long nextbranchid = co.getNextbranchid();

		if (nextbranchid > 0) {
			long deliverystate = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).size() == 0 ? 0 : this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0).getDeliverystate();
			if ((nextbranchid == user.getBranchid())
					&& ((deliverystate != DeliveryStateEnum.JuShou.getValue()) && (deliverystate != DeliveryStateEnum.BuFenTuiHuo.getValue())
							&& (deliverystate != DeliveryStateEnum.ShangMenHuanChengGong.getValue()) && (deliverystate != DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && (deliverystate != DeliveryStateEnum.FenZhanZhiLiu
							.getValue())) && ((co.getFlowordertype() == CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == CwbFlowOrderTypeEnum.YiFanKui.getValue()))) {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			} else {
				return nextbranchid;
			}

		}
		if (co.getCwbstate() == CwbStateEnum.PeiShong.getValue()) {
			return this.cwbRouteService.getNextBranch(co.getCurrentbranchid(), co.getDeliverybranchid());
		} else if (co.getCwbstate() == CwbStateEnum.TuiHuo.getValue()) {
			return this.cwbRouteService.getPreviousBranch(co.getCurrentbranchid(), co.getTuihuoid());
		}
		return 0;
	}

	private boolean tryIncreaseScanNum(CwbOrder co) {
		if (((co.getSendcarnum() > 1) && (co.getSendcarnum() > co.getScannum()))) {
			this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
			co.setScannum(co.getScannum() + 1);
			return true;
		} else if (co.getScannum() < 1) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			co.setScannum(1);
			return true;
		} else {
			return false;
		}
	}

	// 产生反馈记录
	private void creDeliveryDetail(CwbOrder co, String cwb, long deliverid, long userid, long deliverybranchid) {
		User deliverUser = this.userDAO.getUserByUserid(deliverid);
		long id = this.deliveryStateDAO.creDeliveryState(co.getCwb(), co.getBusinessFee(), co.getCwbordertypeid(), deliverUser, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
				co.isOut() ? 0 : 1, userid, deliverybranchid, co.getCustomerid(), co.getShouldfare());
		this.deliveryCashDAO.updateDeliveryCashStateBycwb(cwb);
		this.deliveryCashDAO.creDeliveryCash(cwb, deliverid, deliverybranchid, co.getCustomerid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), id, co.getReceivablefee());
	}

	/**
	 * 领货扫描提交
	 */
	public CwbOrder receiveGoods(User user, User deliveryUser, String cwb, String scancwb) {
		cwb = this.translateCwb(cwb);

		return this.receiveGoodsHandle(user, user.getBranchid(), deliveryUser, cwb, scancwb, false);
	}

	@Transactional
	public CwbOrder receiveGoodsHandle(User user, long currentbranchid, User deliveryUser, String cwb, String scancwb, boolean isauto) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		if (this.applyZhongZhuanDAO.getCwbApplyZhongZhuanByCwb(cwb)!=null) {
			long count = this.applyZhongZhuanDAO.getCwbApplyZhongZhuanYiChuLiByCwbCounts(cwb,2);
			if ((co.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue())&&(co.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue())&&(count==0)) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DaizhongzhuanshenheCannotLinghuo);
			}

		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleReceiveGoodsYipiaoduojian(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, FlowOrderTypeEnum.FenZhanLingHuo, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleReceiveGoods(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, FlowOrderTypeEnum.FenZhanLingHuo, isypdjusetranscwb, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleReceiveGoodsYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, User deliveryUser, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum,
			long isypdjusetranscwb) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if ((co.getScannum() < 1) || (co.getDeliverid() != deliveryUser.getUserid())) {
				this.handleReceiveGoods(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true);
			}
			if((co.getFlowordertype() ==FlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getDeliverid() == deliveryUser.getUserid())  ){//重复领货
				if(co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()){
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao, co.getCurrentbranchid(), deliveryUser.getBranchid());
				}
				if((co.getSendcarnum() >0) && (co.getSendcarnum() == co.getScannum())){
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao, co.getCurrentbranchid(), deliveryUser.getBranchid());
				}
			}
			if ((co.getDeliverid() == deliveryUser.getUserid()) && ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum()))) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleReceiveGoods(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleReceiveGoods(User user, String cwb, String scancwb, long currentbranchid, User deliveryUser, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum,
			long isypdjusetranscwb, boolean isypdj) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		// 扣款结算 流程检查 (到错货不允许做领货扫描)

		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())
				&& (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha2);

		}

		if (userbranch.getAccounttype() == 3) {
			long count = this.cwbStateControlDAO.getCountFromstateTostate(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
			// 到错货订单：已做处理，可以领货；未作处理，不可领货；
			if (count > 0 && co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() ) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha2);
			}
		}

		String usedeliverpay = "no";
		/*
		 * 小件员领货质控使用的代码，目前先不实现 try { usedeliverpay =
		 * systemInstallDAO.getSystemInstallByName
		 * ("usedeliverpayup").getValue(); } catch (Exception e) {
		 * logger.error("领货时，小件员交款系统配置获取失败"); } if (usedeliverpay.equals("yes")
		 * &&
		 * gotoClassAuditingDAO.getDeliverPayUpAndArrearageByDeliverid(deliveryUser
		 * .getUserid()) > 0) { throw new
		 * CwbException(cwb,FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
		 * ExceptionCwbErrorTypeEnum.XiaoJianYuanYouQianKuan); }
		 */

		// 校验配送状态
		DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

		// 限制几种反馈归班状态不允许做领货操作 - 鞠牧
		if ((ds != null) && (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) && (ds.getBusinessfee().compareTo(BigDecimal.ZERO) > 0)) {
			throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, "上门换成功且应处理金额不为0", FlowOrderTypeEnum.FenZhanLingHuo.getText());
		} else if ((ds != null) && (ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, "上门退成功且应处理金额不为0", FlowOrderTypeEnum.FenZhanLingHuo.getText());
		}

		//单件领货 重复判断（同一个小件员）(一票多件已经处理)
		if( (ds != null) && (deliveryUser.getUserid() == ds.getDeliveryid())&&(co.getFlowordertype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getScannum() == 1) && ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1))){
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Ling_Huo);
		}

		if (!isauto && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), co, 0, scancwb, false);
		}

		// 2013-8-14 鞠牧 !(当前站点 是 是领货站点 并（订单流程是 分站到货
		// 、到错货、到错货处理、已审核的）||当前环节是领货/反馈的 并 配送站点是领货站点的)
		if (!(((co.getCurrentbranchid() == deliveryUser.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue()))) || ((co.getDeliverybranchid() == deliveryUser
				.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()))))) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.QING_ZUO_DAO_HUO_SAO_MIAO, co.getCurrentbranchid(), deliveryUser.getBranchid());
		}

		if (ds != null) {
			// 如果换小件员，则失效上一条记录
			if ((ds.getGcaid() <= 0) && (ds.getDeliverystate() == 0)) {
				this.tryIncreaseScanNum(co);
				this.deliveryStateDAO.reObtain(cwb, deliveryUser.getUserid(), user.getUserid(), co.getDeliverybranchid(), DateTimeUtil.getNowTime());
				// 同时更改deliverycash表中的相关信息
				this.deliveryCashDAO
						.saveDeliveryCashByDeliverystateid(deliveryUser.getUserid(), deliveryUser.getBranchid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), ds.getId());
			} else {
				this.inactiveDeliveryStateByCwb(user, ds);
				this.creDeliveryDetail(co, cwb, deliveryUser.getUserid(), user.getUserid(), deliveryUser.getBranchid());
			}
		} else {
			// 生成配送记录
			this.creDeliveryDetail(co, cwb, deliveryUser.getUserid(), user.getUserid(), deliveryUser.getBranchid());
		}
		this.produceGroupDetail(user, cwb, 0, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), deliveryUser.getUserid(), co.getCustomerid(), 0, 0, "");

		// 更新订单状态
		String sql = "update express_ops_cwb_detail set excelbranch=?,deliverybranchid=?, startbranchid=?,currentbranchid=?, flowordertype=?,deliverid=?,deliverystate=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, this.branchDAO.getBranchByBranchid(deliveryUser.getBranchid()).getBranchname(), deliveryUser.getBranchid(), currentbranchid, 0,
				FlowOrderTypeEnum.FenZhanLingHuo.getValue(), deliveryUser.getUserid(), DeliveryStateEnum.WeiFanKui.getValue(), cwb);
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.FenZhanLingHuo, "", System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
	}

	/**
	 *
	 * @param user
	 * @param deliveryState
	 */
	public void inactiveDeliveryStateByCwb(User user, DeliveryState deliveryState) {
		if (deliveryState.getGcaid() > 0) {
			DeliveryResultChange deliveryResultChange = new DeliveryResultChange();
			deliveryResultChange.setId(deliveryState.getId());
			deliveryResultChange.setUserid(user.getUserid());
			deliveryResultChange.setCash(deliveryState.getCash().negate());
			deliveryResultChange.setCheck(deliveryState.getCheckfee().negate());
			deliveryResultChange.setPos(deliveryState.getPos().negate());
			deliveryResultChange.setOther(deliveryState.getOtherfee().negate());
			switch (DeliveryStateEnum.getByValue((int) deliveryState.getDeliverystate())) {
			case PeiSongChengGong:
				deliveryResultChange.setNum(1);
				break;
			case ShangMenTuiChengGong:
				deliveryResultChange.setNum(-1);
				break;
			default:
				break;
			}
			// deliveryResultChangeDAO.create(deliveryResultChange);
		}
		this.deliveryStateDAO.inactiveDeliveryStateByCwb(deliveryState.getCwb());
	}

	public void produceGroupDetail(User user, String cwb, long outWarehouseGroupId, boolean isauto, long flowordertype, long nextbranchid, long deliverid, long customerid, long driverid,
			long truckid, String packagecode) {
		this.logger.info("============driverid={},truckid={}", driverid, truckid);
		List<GroupDetail> gdlist = this.groupDetailDAO.getCheckGroupDetailIsExist(cwb, flowordertype, user.getBranchid());
		if (!isauto) {
			if (gdlist.size() == 0) {
				this.groupDetailDAO.creGroupDetail(cwb, outWarehouseGroupId, user.getUserid(), flowordertype, user.getBranchid(), nextbranchid, deliverid, customerid, driverid, truckid, packagecode);
			} else {
				this.groupDetailDAO.saveGroupDetailByBranchidAndCwb(user.getUserid(), nextbranchid, cwb, user.getBranchid(), deliverid, customerid);
			}
		}
	}

	private void validateStateTransfer(CwbOrder co, FlowOrderTypeEnum nextState) {
		CwbFlowOrderTypeEnum fromstate = CwbFlowOrderTypeEnum.getText((int) co.getFlowordertype());
		if ((fromstate != null) && (this.cwbStateControlDAO.getCwbStateControlByParam((int) co.getFlowordertype(), nextState.getValue()) == null)) {
			throw new CwbException(co.getCwb(), nextState.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromstate.getText(), nextState.getText());
		}
	}

	/**
	 * 单票结果反馈
	 *
	 * @param co
	 * @param deliverid
	 * @param podresultid
	 * @param backreasonid
	 * @param leavedreasonid
	 * @param podremarkid
	 * @param receivedfeecash
	 * @param receivedfeepos
	 * @param receivedfeecheque
	 * @param receivedfeeother
	 * @param paybackedfee
	 * @return
	 */
	@OrderFlowOperation(FlowOrderTypeEnum.YiFanKui)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Map<String, Object> deliverStatePod(User user, String cwb, String scancwb, Map<String, Object> parameters) {
		Map<String, Object> map = new HashMap<String, Object>();
		String fankuileixing = parameters.get("fankuileixing") == null ? "" : parameters.get("fankuileixing").toString();
		long deliverid = parameters.get("deliverid") == null ? 0l : Long.parseLong(parameters.get("deliverid").toString());
		long podresultid = parameters.get("podresultid") == null ? 0l : (Long) parameters.get("podresultid");
		long backreasonid = parameters.get("backreasonid") == null ? 0l : (Long) parameters.get("backreasonid");
		long leavedreasonid = parameters.get("leavedreasonid") == null ? 0l : (Long) parameters.get("leavedreasonid");
		BigDecimal receivedfeecash = parameters.get("receivedfeecash") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("receivedfeecash");
		BigDecimal receivedfeepos = parameters.get("receivedfeepos") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("receivedfeepos");
		BigDecimal receivedfeecodpos = parameters.get("receivedfeecodpos") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("receivedfeecodpos");
		BigDecimal receivedfeecheque = parameters.get("receivedfeecheque") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("receivedfeecheque");
		BigDecimal receivedfeeother = parameters.get("receivedfeeother") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("receivedfeeother");
		BigDecimal infactfare = parameters.get("infactfare") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("infactfare");
		BigDecimal paybackedfee = parameters.get("paybackedfee") == null ? BigDecimal.ZERO : (BigDecimal) parameters.get("paybackedfee");
		long podremarkid = parameters.get("podremarkid") == null ? 0l : (Long) parameters.get("podremarkid");
		String posremark = parameters.get("posremark") == null ? "" : (String) parameters.get("posremark");
		String checkremark = parameters.get("checkremark") == null ? "" : (String) parameters.get("checkremark");
		String deliverstateremark = parameters.get("deliverstateremark") == null ? "" : (String) parameters.get("deliverstateremark");
		// long owgid = parameters.get("owgid")==null? 0l :
		// (Long)parameters.get("owgid");//包号
		long sessionbranchid = parameters.get("sessionbranchid") == null ? 0l : (Long) parameters.get("sessionbranchid");
		int sign_typeid = parameters.get("sign_typeid") == null ? SignTypeEnum.BenRenQianShou.getValue() : (Integer) parameters.get("sign_typeid");
		String sign_man = parameters.get("sign_man") == null ? "" : (String) parameters.get("sign_man");
		String sign_time = parameters.get("sign_time") == null ? "" : (String) parameters.get("sign_time");
		long paywayid = (parameters.get("paywayid") == null ? 0L : (Long) parameters.get("paywayid"));
		boolean isbatch = parameters.get("isbatch") != null;
		boolean isjutui = parameters.get("isjutui") != null;
		String resendtime = parameters.get("resendtime") == null ? "" : (String) parameters.get("resendtime");
		long weishuakareasonid = parameters.get("weishuakareasonid") == null ? 0l : (Long) parameters.get("weishuakareasonid");
		long losereasonid = parameters.get("losereasonid") == null ? 0l : (Long) parameters.get("losereasonid");
		int firstlevelreasonid = parameters.get("firstlevelreasonid") == null ? 0 : Integer.parseInt(parameters.get("firstlevelreasonid").toString());
		String zhiliuremark = parameters.get("zhiliuremark") == null ? "" : (String) parameters.get("zhiliuremark");
		// 名字为deliverytime_now 是因为时间紧急，为了避免与其他调这个方法的地方冲突。
		String deliverytime = parameters.get("deliverytime_now") == null ? DateTimeUtil.getNowTime() : (String) parameters.get("deliverytime_now");

		// 标识是否系统反馈,如果 为1 则不是系统反馈,不判断未刷卡原因
		String nosysyemflag = parameters.get("nosysyemflag") == null ? null : parameters.get("nosysyemflag").toString();

		long changereasonid = parameters.get("changereasonid") == null ? 0l : (Long) parameters.get("changereasonid");
		long firstchangereasonid = parameters.get("firstchangereasonid") == null ? 0l : (Long) parameters.get("firstchangereasonid");


		// 再次判定时间格式是否正确 如果正确 应该去掉空白符共18个字
		deliverytime = deliverytime.length() != 19 ? DateTimeUtil.getNowTime() : deliverytime;

		this.logger.info("修改反馈时间用户：" + user.getRealname() + " cwb" + cwb + "：当前{}改为{}", DateTimeUtil.getNowTime(), deliverytime);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		//added by jiangyu begin
		//缓存一下原先的支付方式
		map.put("preObj", co);
		if (co!=null) {
			map.put("oldpaywayid", co.getNewpaywayid());
		}
		//added by jiangyu end

		// 委托派送变更状态为已反馈
		this.orderDeliveryClientDAO.updateFanKun(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		this.validateCwbState(co, FlowOrderTypeEnum.YiFanKui);

		this.validateStateTransfer(co, FlowOrderTypeEnum.YiFanKui);

		this.operationRuleService.validate(user, co, parameters, FlowOrderTypeEnum.YiFanKui);

		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
		if (deliveryState == null) {
			if (deliverid == 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan);
			}
			this.receiveGoods(user, this.userDAO.getUserByUserid(deliverid), cwb, scancwb);
			deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
		}

		if (isbatch) {
			// 配送订单批量反馈,验证是否为配送订单
			if (fankuileixing.equals("PEISONG") && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_PEI_SONG_DING_DAN);
			}
			// 上门换订单批量反馈,验证是否为上门换订单
			if (fankuileixing.equals("SHANGMENHUAN") && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_SHANG_MEN_HUAN_DING_DAN);
			}
			// 上门退订单批量反馈,验证是否为上门退订单
			if (fankuileixing.equals("SHANGMENTUI") /*&&((podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue())||(podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()))*/ && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_SHANG_MEN_TUI_DING_DAN);
			}
			// 已反馈订单不允许批量反馈
			if (((co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()))
					&& (deliveryState.getDeliverystate() != DeliveryStateEnum.WeiFanKui.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.YI_FAN_KUI_BU_NENG_PI_LIANG_ZAI_FAN_KUI);
			}
			// 小件员与反馈人不一致
			/*
			 * if(deliverid==0&&deliveryState.getDeliveryid()!=user.getUserid()){
			 * throw new CwbException(cwb,
			 * FlowOrderTypeEnum.YiFanKui.getValue(),
			 * ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO) ;
			 * }
			 */
			if ((deliverid != 0) && (deliverid != deliveryState.getDeliveryid())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO);
			}

			if ((deliverid == 0) && (user.getBranchid() == deliveryState.getDeliverybranchid())) {
				deliverid = deliveryState.getDeliveryid();
			}

			// 请选择小件员
			if (deliverid == 0) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan);
			}

			User deliveryUser = this.userDAO.getUserByUserid(deliverid);
			if (!((((co.getCurrentbranchid() == deliveryUser.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())
					|| (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) || (co
						.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()))) || (co.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue())) || ((co.getDeliverybranchid() == deliveryUser
					.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()))))) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.QING_ZUO_DAO_HUO_SAO_MIAO, co.getCurrentbranchid(), deliveryUser.getBranchid());
			}

			if ((deliveryState != null) && (deliverid != 0) && (deliveryState.getDeliveryid() != deliverid)) {
				if (deliverid == 0) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan);
				}
				this.receiveGoods(user, this.userDAO.getUserByUserid(deliverid), cwb, scancwb);
				deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			}

			if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue())
					|| ((podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) && (co.getPaybackfee().compareTo(co.getReceivablefee()) == -1))) {

				if (paywayid == 5) {
					paywayid = co.getPaywayid();
				}

				if (paywayid == 1) {
					// 现金支付
					receivedfeecash = co.getBusinessFee();
				} else if (paywayid == 2) {
					// pos支付
					receivedfeepos = co.getBusinessFee();
				} else if (paywayid == 3) {
					// 支票支付
					receivedfeecheque = co.getBusinessFee();
				} else if (paywayid == 4) {
					// 其他支付方式
					receivedfeeother = co.getBusinessFee();
				} else if (paywayid == 5) {
					// cod扫码支付方式
					receivedfeecodpos = co.getBusinessFee();
				}
			}
			if (!isjutui) {
				paybackedfee = co.getPaybackfee();
			}
			sign_man = co.getConsigneenameOfkf();
			if((podresultid==DeliveryStateEnum.FenZhanZhiLiu.getValue())
					||(podresultid==DeliveryStateEnum.ShangMenJuTui.getValue())
					||(podresultid==DeliveryStateEnum.JuShou.getValue())
					||(podresultid==DeliveryStateEnum.DaiZhongZhuan.getValue())
					) {
				paybackedfee=new BigDecimal("0");
			}
			if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) && (resendtime.length() > 0)) {
				this.cwbDAO.saveResendtimeByCwb(resendtime, co.getCwb());
			}

			if (zhiliuremark.length() > 0) {
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + zhiliuremark;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
					co.setCwbremark(newcwbremark);
				} catch (Exception e) {
					this.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		if (((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenTuiChengGong
				.getValue()))) {
			sign_man = sign_man.length() == 0 ? co.getConsigneenameOfkf() : sign_man;
			sign_time = DateTimeUtil.getNowTime();
			sign_typeid = 1;

		} else {
			sign_man = "";
			sign_time = "";
			sign_typeid = 0; // 0 未签收 1已签收 如果反馈结果不是成功，sign_typeid=0.
		}

		User deliveryUser = this.userDAO.getUserByUserid(deliveryState.getDeliveryid());
		if (deliveryState.getGcaid() > 0) {
			this.inactiveDeliveryStateByCwb(user, deliveryState);
			this.creDeliveryDetail(co, cwb, deliverid, user.getUserid(), deliveryUser.getBranchid());
			deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
		}

		if (deliveryState.getDeliveryid() != deliverid) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO);
		}

		// 是否开启了亚马逊对接
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());

		// 原支付方式为pos，现金、支票、其他金额有值，没有选择未刷卡原因的时候报异常，只针对开启了亚马逊对接的订单
		if ((isOpenFlag != 0)
				&& ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) && (co.getPaywayid() == PaytypeEnum.Pos.getValue())
				&& ((receivedfeecash.compareTo(BigDecimal.ZERO) > 0) || (receivedfeecheque.compareTo(BigDecimal.ZERO) > 0) || (receivedfeeother.compareTo(BigDecimal.ZERO) > 0))
				&& (weishuakareasonid < 1) && (nosysyemflag == null)) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin);
		}

		String weishuakareasoncontent = "";

		// 根据生哥说的，只验证配送结果为配送成功和部分拒收、上门换成功的，只针对开启了亚马逊对接的订单
		if ((isOpenFlag != 0)
				&& ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) && (co.getPaywayid() == PaytypeEnum.Pos.getValue())
				&& ((receivedfeecash.compareTo(BigDecimal.ZERO) > 0) || (receivedfeecheque.compareTo(BigDecimal.ZERO) > 0) || (receivedfeeother.compareTo(BigDecimal.ZERO) > 0))) {
			Reason weishuakareason = this.reasonDAO.getReasonByReasonid(weishuakareasonid);
			weishuakareasoncontent = weishuakareason != null ? weishuakareason.getReasoncontent() : "";
		} else {
			this.logger.info("此时订单{}的未刷卡原因id是{}", co.getCwb(), weishuakareasonid);
			weishuakareasonid = 0;
		}

		this.cwbDAO.saveCwbForWeishuakareason(co.getCwb(), weishuakareasoncontent, weishuakareasonid);

		// 保存货物丢失原因，只针对开启了亚马逊对接的订单
		if ((isOpenFlag != 0) && (podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()) && (losereasonid != 0)) {
			Reason diushireason = this.reasonDAO.getReasonByReasonid(losereasonid);
			String diushireasoncontent = diushireason != null ? diushireason.getReasoncontent() : "";
			this.cwbDAO.saveCwbForDiushireason(co.getCwb(), diushireasoncontent, losereasonid);
		}

		BigDecimal receivedfee = receivedfeecash.add(receivedfeepos).add(receivedfeecheque).add(receivedfeeother).add(receivedfeecodpos);
		BigDecimal businessfee = new BigDecimal(0);
		if ((co.getReceivablefee() != null) && (co.getPaybackfee() != null) && (co.getReceivablefee().compareTo(co.getPaybackfee()) > -1)) {
			businessfee = co.getReceivablefee();
		} else {
			businessfee = co.getPaybackfee();
		}
		Reason reason = new Reason();
		if ((backreasonid != 0)
				&& ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(backreasonid);
			this.cwbDAO.saveCwbForBackreason(co.getCwb(), reason.getReasoncontent(), backreasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), co.getCwbremark() + "," + reason.getReasoncontent() + "," + deliverstateremark);
		}

		if ((firstlevelreasonid != 0) && ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(leavedreasonid);
			this.cwbDAO.saveCwbForLeavereason(co.getCwb(), reason.getReasoncontent(), leavedreasonid, firstlevelreasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), co.getCwbremark() + "," + reason.getReasoncontent() + "," + deliverstateremark);
		}
		if ((changereasonid != 0) && (podresultid == DeliveryStateEnum.DaiZhongZhuan.getValue() )) {
			reason = this.reasonDAO.getReasonByReasonid(changereasonid);
			this.cwbDAO.saveCwbForChangereason(co.getCwb(), reason.getReasoncontent(), changereasonid,firstchangereasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), co.getCwbremark() + "," + reason.getReasoncontent() + "," + deliverstateremark);

		}
		// 为货物丢失添加的
		if ((losereasonid != 0) && ((podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(losereasonid);
			this.cwbDAO.saveCwbForDiushireason(co.getCwb(), reason.getReasoncontent(), losereasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), co.getCwbremark() + "," + reason.getReasoncontent() + "," + deliverstateremark);
		}
		if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			this.deliveryStateDAO.saveDeliveyStateIsautolinghuoByCwb2(0, co.getCwb(), firstlevelreasonid);
		}
		if (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()) {
			podresultid = DeliveryStateEnum.FenZhanZhiLiu.getValue();
			this.deliveryStateDAO.saveDeliveyStateIsautolinghuoByCwb2(1, co.getCwb(), firstlevelreasonid);
		}
		// podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()||

		/*
		 * if (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue())
		 * { this.deliveryStateDAO.saveDeliveyStateIsautolinghuoByCwb2(1,
		 * co.getCwb() ,firstlevelreasonid); }
		 */

		// 反馈为分站滞留、拒收、上门拒退、滞留自动领货的时候，现金、pos、支票、其他金额处理为0
		if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue())
				|| (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue())) {
			receivedfeecash = receivedfeepos = receivedfeecheque = receivedfeeother = BigDecimal.ZERO;
		}

		this.logger.info("进入单票反馈cwborderservice处理完后开始保存信息cwb:" + co.getCwb() + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--receivedfeecash:" + receivedfeecash
				+ "--receivedfeepos:" + receivedfeepos + "--receivedfeecodpos:" + receivedfeecodpos + "--receivedfeecheque:" + receivedfeecheque + "--receivedfeeother:" + receivedfeeother
				+ "--paybackedfee=" + paybackedfee + "--isbatch=" + isbatch+"--infactfare="+infactfare);

		if((podresultid==DeliveryStateEnum.ShangMenTuiChengGong.getValue())&&(infactfare.doubleValue()==0)){
			infactfare=co.getShouldfare();
		}

		this.deliveryStateDAO.saveForReFanKui(co.getCwb(), deliverid, receivedfee, paybackedfee, businessfee, podresultid, receivedfeecash, receivedfeepos, posremark, receivedfeecheque, checkremark,
				receivedfeeother, podremarkid, deliverstateremark, "", sign_typeid, sign_man, sign_time, receivedfeecodpos, infactfare);
		// 修改订单表的实收运费
		this.cwbDAO.updateCwbInfactFare(co.getCwb(), infactfare);

		// 更新反馈时间
		this.deliveryStateDAO.updateDeliverytime(cwb, deliverytime);

		this.deliveryCashDAO.saveDeliveryCashForDSById(deliverytime, paybackedfee, receivedfeecash.add(receivedfeecheque).add(receivedfeeother), receivedfeepos.add(receivedfeecodpos), podresultid,
				deliveryState.getId());
		deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

		long newpaywayid = co.getPaywayid();

		String batchEditDeliveryStateisUseCash = "";

		if (receivedfeepos.compareTo(BigDecimal.ZERO) == 0) {
			batchEditDeliveryStateisUseCash = this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash") == null ? "no" : this.systemInstallDAO.getSystemInstall(
					"batchEditDeliveryStateisUseCash").getValue();
		}

		if ("yes".equals(batchEditDeliveryStateisUseCash)) {
			newpaywayid = PaytypeEnum.Xianjin.getValue();
		} else {
			if (deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
				newpaywayid = PaytypeEnum.Xianjin.getValue();
			} else if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
				newpaywayid = PaytypeEnum.Pos.getValue();
			} else if (deliveryState.getCheckfee().compareTo(BigDecimal.ZERO) > 0) {
				newpaywayid = PaytypeEnum.Zhipiao.getValue();
			} else if (deliveryState.getOtherfee().compareTo(BigDecimal.ZERO) > 0) {
				newpaywayid = PaytypeEnum.Qita.getValue();
			} else if (deliveryState.getCodpos().compareTo(BigDecimal.ZERO) > 0) {
				newpaywayid = PaytypeEnum.CodPos.getValue();
			}
			//added by jiangyu begin
			map.put("newpaywayid", newpaywayid);
			//added by jiangyu end
			if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {// 上门退订单，支付方式都要变成现金
				newpaywayid = PaytypeEnum.Xianjin.getValue();
			}
		}
		// 更新当前反馈状态需要指定订单的下一站
		this.saveFanKuiNextBranchId(user, deliveryState.getDeliverystate(), cwb);

		String sql2 = "update express_ops_cwb_detail set flowordertype=?,deliverystate=?,newpaywayid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql2, FlowOrderTypeEnum.YiFanKui.getValue(), deliveryState.getDeliverystate(), newpaywayid, co.getCwb());
		this.createFloworder(user, sessionbranchid, co, FlowOrderTypeEnum.YiFanKui, (reason.getReasoncontent() == null ? "" : reason.getReasoncontent()) + " " + deliverstateremark,
				System.currentTimeMillis());

		// 反馈时更新订单的反馈的操作时间
		this.operationTimeDAO.creAndUpdateOperationTime(co.getCwb(), sessionbranchid, FlowOrderTypeEnum.YiFanKui.getValue(), deliveryState.getDeliverystate(), sessionbranchid, co.getCustomerid(), "",
				co.getEmaildate(), co.getCwbordertypeid(), co.getReceivablefee(), co.getPaybackfee());

		this.logger.info("进入单票反馈cwborderservice处理结束跳出cwborderservice！cwb:" + co.getCwb() + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--receivedfeecash:" + receivedfeecash
				+ "--receivedfeepos:" + receivedfeepos + "--receivedfeecheque:" + receivedfeecheque + "--receivedfeeother:" + receivedfeeother);
		return map;
	}

	/**
	 * 更新当前反馈状态需要指定订单的下一站
	 *
	 * @param user
	 *            当前操作的用户所在机构
	 * @param deliverystate
	 *            当前反馈的结果
	 * @param cwb
	 *            要处理的订单号
	 */
	private void saveFanKuiNextBranchId(User user, long deliverystate, String cwb) {
		DeliveryStateEnum dse = DeliveryStateEnum.getByValue((int) deliverystate);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		Customer customer = this.customerDao.getCustomerById(co.getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
		String isZhiLiuZhongZhuanDaoKuFand = this.systemInstallDAO.getSystemInstall("isZhiLiuZhongZhuanDaoKuFand") == null ? "no" : this.systemInstallDAO.getSystemInstall(
				"isZhiLiuZhongZhuanDaoKuFand").getValue();

		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
			bList.add(this.branchDAO.getBranchByBranchid(i));
		}
		// 获取站点设定的中转站ID
		Long zhongZhuanZhanId = this.branchDAO.getBranchByBranchid(user.getBranchid()).getZhongzhuanid();
		Branch zhongzhuanNextBranch = null;
		Branch tuihuoNextBranch = null;
		for (Branch b : bList) {// 获得当前站点的中转站和退货站
			if ((b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) && (b.getBranchid() == zhongZhuanZhanId)) {
				zhongzhuanNextBranch = b;
			} else if (isZhiLiuZhongZhuanDaoKuFand.equals("yes") && (b.getSitetype() == BranchEnum.KuFang.getValue())) {// 腾迅达希望将中转货物中转至库房
				zhongzhuanNextBranch = b;
			} else if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				tuihuoNextBranch = b;
			}
		}

		if ((dse == DeliveryStateEnum.ShangMenTuiChengGong) || (dse == DeliveryStateEnum.ShangMenHuanChengGong) || (dse == DeliveryStateEnum.JuShou) || (dse == DeliveryStateEnum.BuFenTuiHuo)) { // 需要退货
			if (tuihuoNextBranch == null) {
				this.logger.info("站点{0}没有指定退货站", user.getBranchid());
				return;
			}
			if (dse == DeliveryStateEnum.JuShou) {// 品信退货是否需要审核开关
				//String tuihuocheck = this.systemInstallDAO.getSystemInstallByName("tuihuocheck") == null ? "no" : this.systemInstallDAO.getSystemInstallByName("tuihuocheck").getValue();
				if (/*"no".equals(tuihuocheck) && */!chechFlag) {// 更改下一站为退货站
					this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
				}
			} else {
				this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
			}
		} else if (dse == DeliveryStateEnum.FenZhanZhiLiu) {// 需要中转
			if (zhongzhuanNextBranch == null) {
				this.logger.info("站点{0}没有指定退货站", user.getBranchid());
				return;
			}
			//this.cwbDAO.updateNextBranchid(cwb, zhongzhuanNextBranch.getBranchid());
			this.cwbDAO.updateNextBranchid(cwb, co.getStartbranchid());

		} else if (dse == DeliveryStateEnum.DaiZhongZhuan) {// 待中转
			if (zhongzhuanNextBranch == null) {
				this.logger.info("站点{0}没有指定中转站", user.getBranchid());
				return;
			}
			this.cwbDAO.updateNextBranchid(cwb, zhongzhuanNextBranch.getBranchid());

		}else {// 其他的反馈结果，都将下一站置为0
			this.cwbDAO.updateNextBranchid(cwb, 0L);
		}
	}

	// 更改订单的订单状态为退货的流向
	private void deliverPodForCwbstate(String cwb, long podresultid, FlowOrderTypeEnum auditFlowOrderTypeEnum, User user) {
		// ====退货审核===
		/*OrderBackCheck orderBackCheck=orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
		if(orderBackCheck!=null){//如果存在先删除
			this.orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb);
		}*/
		Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;

		Branch tuihuoNextBranch = null;
		if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
				|| (podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			// 拒收修改订单为配送状态
			if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
				List<Branch> bList = new ArrayList<Branch>();
				for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
					bList.add(this.branchDAO.getBranchByBranchid(i));
				}
				for (Branch b : bList) {// 获得当前站点的退货站
					if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
						tuihuoNextBranch = b;
					}
				}
				//chechFlag (退货是否需要审核的标识 0：否 ,1：是)
				if (chechFlag) {
				//	this.updateCwbState(cwb, CwbStateEnum.PeiShong);
					// 获取订单信息
					CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
					// 退货审核表插入一条订单数据
					OrderBackCheck orderBackCheck=this.orderBackCheckDAO.getOrderBackCheckByCheckstate(cwb);
					if((orderBackCheck == null)&&((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())) ){
						OrderBackCheck o = new OrderBackCheck();
						if(podresultid == DeliveryStateEnum.JuShou.getValue()){
							o = this.orderBackCheckService.loadFormForOrderBackCheck(co, co.getDeliverybranchid(), user.getUserid(), 1, DeliveryStateEnum.JuShou.getValue());
						}else if(podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()){
							o = this.orderBackCheckService.loadFormForOrderBackCheck(co, co.getDeliverybranchid(), user.getUserid(), 1, DeliveryStateEnum.BuFenTuiHuo.getValue());
						}
						this.orderBackCheckDAO.createOrderBackCheck(o);
						this.logger.info("退货审核：订单{}，修改为配送状态", new Object[] { cwb });
					}


			/*		for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
						bList.add(this.branchDAO.getBranchByBranchid(i));
					}*/

				/*	//Branch tuihuoNextBranch = null;
					for (Branch b : bList) {// 获得当前站点的退货站
						if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
							tuihuoNextBranch = b;
						}
					}*/
					if (tuihuoNextBranch == null) {
						tuihuoNextBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getTuihuoid());
					} else {
						// 更改下一站为退货站
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
					}
					//this.updateCwbState(cwb, CwbStateEnum.TuiHuo);

				}/* else {
					for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
						bList.add(this.branchDAO.getBranchByBranchid(i));
					}
					Branch tuihuoNextBranch = null;
					for (Branch b : bList) {// 获得当前站点的退货站
						if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
							tuihuoNextBranch = b;
						}
					}
					if (tuihuoNextBranch == null) {
						tuihuoNextBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getTuihuoid());
					} else {
						// 更改下一站为退货站
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
					}
					this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
					this.logger.info("退货审核：订单{}，修改为退货状态", new Object[] { cwb });
				}*/

			} else {
				this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			}
		} else if (podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			this.updateCwbState(cwb, CwbStateEnum.DiuShi);
		}
		// 处理站点
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?, currentbranchid=0 where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
		}else if((podresultid == DeliveryStateEnum.JuShou.getValue())||(podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())){
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid,nextbranchid=? where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(),tuihuoNextBranch.getBranchid(),cwb);
		}else {
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(),cwb);
		}
	}

	@Transactional
	public void updateCwbState(String cwb, CwbStateEnum state) {
		this.cwbDAO.updateCwbState(cwb, state);
		this.updateNextBranchId(cwb);
	}

	/**
	 * 审核为中转
	 *
	 * @param user
	 * @param cwb
	 * @param deliverybranchid
	 * @return
	 * @throws Exception
	 */
	/*
	 * public void auditToZhongZhuan(User user, String cwb,long
	 * deliverybranchid) throws Exception { logger.info("将 {} 审核为中转", cwb);
	 * CwbOrder co = cwbDAO.getCwbByCwbLock(cwb); if (co == null) { throw new
	 * CwbException(cwb,
	 * ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI); }
	 * cwbDAO.updateCwbState(cwb, CwbStateEnum.ZhongZhuan);
	 * CwbOrderAddressCodeEditTypeEnum addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
	 * if(co.getAddresscodeedittype()==
	 * CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()
	 * ||co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue
	 * ()){//如果修改的数据原来是地址库匹配的或者是后来修改的 都将匹配状态变更为修改 addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.XiuGai; }else
	 * if(co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()
	 * ||co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()){//如果修改的数据原来是为匹配的
	 * 或者是人工匹配的 都将匹配状态变更为人工修改 addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.RenGong; } Branch deliverybranch =
	 * branchDAO.getBranchByBranchid(deliverybranchid);
	 * updateDeliveryBranch(user, co, deliverybranch, addressCodeEditType);
	 * logger.info("审核为中转，操作人是{}，配送站点是{}",userDAO.getUserByid(user.getUserid()),
	 * deliverybranch.getBranchname()+"--"+deliverybranchid);
	 *
	 * }
	 */

	/**
	 * POS付款
	 *
	 * @param cwb
	 * @param feebackAmount
	 * @param type
	 * @param podremark
	 * @param deliverstateremark
	 * @return
	 */

	@Transactional
	public CwbOrder posPay(String cwb, BigDecimal feebackAmount, // 实际支付金额
			BigDecimal Receivablefee, // 应收款
			int pay_type, String podremark, String deliverstateremark, long deliveryid, DeliveryState ds, String acq_type, long deliverystate) throws Exception {
		// 验证业务逻辑

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		if (co.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) {
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.DingDanYiZhiFu);
		}
		if ((co.getReceivablefee().compareTo(ds.getReceivedfee()) == 0) && (co.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.DingDanYiZhiFu);
		}

		this.validateCwbState(co, FlowOrderTypeEnum.PosZhiFu);
		this.validateStateTransfer(co, FlowOrderTypeEnum.PosZhiFu);

		if ((ds.getBusinessfee().compareTo(feebackAmount) != 0) && !"split".equals(acq_type) && (co.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton);
		}

		BigDecimal pos = BigDecimal.ZERO;
		String posremark = "";
		BigDecimal check = BigDecimal.ZERO;
		String checkremark = "";
		BigDecimal cash = BigDecimal.ZERO;
		int newpaywayid = 0;
		if (pay_type == PaytypeEnum.Pos.getValue()) {
			// pos刷卡
			pos = feebackAmount;
			posremark = podremark;
			newpaywayid = PaytypeEnum.Pos.getValue();
		} else if (pay_type == PaytypeEnum.Zhipiao.getValue()) {
			// 支票
			check = feebackAmount;
			checkremark = (podremark);
			newpaywayid = PaytypeEnum.Zhipiao.getValue();
		} else if (pay_type == PaytypeEnum.Xianjin.getValue()) {
			// 现金
			cash = feebackAmount;
			posremark = "现金支付";
			newpaywayid = PaytypeEnum.Xianjin.getValue();
		}

		// 执行支付的方法
		if ((co.getFlowordertype() != CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getFlowordertype() != CwbFlowOrderTypeEnum.YiFanKui.getValue())
				&& (co.getFlowordertype() != CwbFlowOrderTypeEnum.YiZhiFu.getValue()) && (co.getFlowordertype() != CwbFlowOrderTypeEnum.CheXiaoFanKui.getValue())) {
			this.receiveGoods(this.userDAO.getUserByUserid(deliveryid), this.userDAO.getUserByUserid(deliveryid), cwb, cwb);
		}
		User user = this.userDAO.getUserByUserid(deliveryid);

		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		this.deliveryStateDAO.saveForReFanKui(cwb, deliveryid, feebackAmount, new BigDecimal(0), Receivablefee, deliverystate, cash, pos, posremark, check, checkremark, new BigDecimal(0), 0,
				deliverstateremark, DateTimeUtil.getNowTime(), 0, "", "", BigDecimal.ZERO, BigDecimal.ZERO);

		this.deliveryCashDAO.saveDeliveryCashForDSById(DateTimeUtil.getNowTime(), new BigDecimal(0), cash.add(check), pos, deliverystate == 0 ? ds.getDeliverystate() : deliverystate, ds.getId());

		String sql2 = "update express_ops_cwb_detail set flowordertype=?,deliverid=?,deliverybranchid=?,excelbranch=?,newpaywayid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql2, CwbFlowOrderTypeEnum.YiZhiFu.getValue(), deliveryid, user.getBranchid(), branch.getBranchname(), newpaywayid, co.getCwb());

		String sql3 = "update express_ops_delivery_state set deliveryid=?,deliverybranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql3, deliveryid, user.getBranchid(), co.getCwb());

		this.createFloworder(user, user.getBranchid(), cwb, FlowOrderTypeEnum.PosZhiFu, "POS支付-" + posremark, System.currentTimeMillis());

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	/**
	 * 验证支付前基本逻辑并返回参数
	 *
	 * @param billRespNote
	 * @return
	 */
	public void ValidateRequestBusiness_PosPay(CwbOrder cwbOrder, DeliveryState ds, String cwb, long deliverid, double payamount, String acq_type) // 支付类型，分单split，合单single
																																					// 或""
	{
		if (cwbOrder == null) {
			this.logger.error("运单支付,没有检索到数据" + cwb + ",小件员：" + deliverid);
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		if (cwbOrder.getReceivablefee().compareTo(ds.getReceivedfee()) == 0) {
			this.logger.error("[运单支付]该订单已支付,当前订单号：" + cwb + ",小件员：" + deliverid);
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.DingDanYiZhiFu);
		}
		if ((ds.getBusinessfee().doubleValue() != payamount) && !"split".equals(acq_type)) {
			this.logger.error("[运单支付]支付金额有误,当前订单号：" + cwb + ",小件员：" + deliverid);
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton);
		}

	}

	/**
	 * 撤销交易
	 *
	 * @param cwb
	 * @param branchid
	 * @param userid
	 */

	public void deliverStatePodCancel(String cwb, long branchid, long userid, String deliverstateremark, double amount_after) {
		this.deliveryStateDAO.saveDeliveyStateForCancel(cwb, deliverstateremark, amount_after);
		this.cwbDAO.saveCwbDetailForCancel(cwb);
		User user = new User();
		user.setBranchid(branchid);
		user.setUserid(userid);
		this.createFloworder(user, branchid, cwb, FlowOrderTypeEnum.CheXiaoFanKui, "POS撤销", System.currentTimeMillis());

	}

	/**
	 * 审核
	 *
	 * @param user
	 *            审核人
	 * @param subTrStr
	 *            交款订单号
	 * @param okTime
	 *            审核时间
	 * @param subAmount
	 *            审核金额
	 * @param subAmountPos
	 *            审核POS金额
	 * @param deliverealuser
	 *            小件员
	 * @param gotoClassOld
	 *            存储历史明细对象
	 * @param deliverpayuptype
	 *            小件员交款类型
	 * @param deliverpayupamount
	 *            小件员交款金额
	 * @param deliverpayupbanknum
	 *            小件员交款小票号 POS类型才有
	 * @param deliverpayupaddress
	 *            小件员交款地址 现金类型才有
	 * @param deliverpayupamount_pos
	 *            小件员交用户刷pos金额
	 * @param deliverAccount
	 *            小件员当前帐户金额
	 * @param deliverPosAccount
	 *            小件员当前pos帐户金额
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public String deliverAuditok(User user, String subTrStr, String okTime, String subAmount, String subAmountPos, long deliverealuser, GotoClassOld gotoClassOld, int deliverpayuptype,
			BigDecimal deliverpayupamount, String deliverpayupbanknum, String deliverpayupaddress, BigDecimal deliverpayupamount_pos, BigDecimal deliverAccount, BigDecimal deliverPosAccount) {
		/*
		 * if(0<deliveryStateDAO.getIsRepeat(subTrStr)){ return
		 * "{\"errorCode\":1,\"error\":\"禁止审核已审核的订单，请刷新后重试\"}"; }
		 */
		this.logger.info("ok:subTrStr {} | subAmount {} | subAmountPos {}", new Object[] { subTrStr, subAmount, subAmountPos });

		this.logger.info("用户:{},开始创建归班记录,金额为{},pos为{},包含{}", new Object[] { user.getUserid(), subAmount, subAmountPos, subTrStr });

		Boolean isUseDeliverPayUp = false;// 用于判断是否使用小件员交款功能
		try {
			SystemInstall usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");
			if (usedeliverpay.getValue().equals("yes")) {
				isUseDeliverPayUp = true;
			}
		} catch (Exception e) {
			this.logger.error("获取使用小件员交款功能异常, deliverpayuptype:{} deliverpayupamount:{} deliverpayupbanknum:{} deliverpayupaddress:{}", new Object[] { deliverpayuptype, deliverpayupamount,
					deliverpayupbanknum, deliverpayupaddress });
			e.printStackTrace();
		}
		long gcaId = 0L;
		if (isUseDeliverPayUp) {// 使用小件员交款功能
			// 锁住事物，获得小件员帐户信息
			User uPayupDeliver = this.userDAO.getUserByUseridLock(deliverealuser);
			// 计算欠款
			BigDecimal deliverpayuparrearage = deliverpayupamount.add(deliverAccount).subtract(new BigDecimal(subAmount));
			BigDecimal deliverpayuparrearage_pos = deliverpayupamount_pos.add(deliverPosAccount).subtract(new BigDecimal(subAmountPos));
			// 处理归班信息
			gcaId = this.gotoClassAuditingDAO.creGotoClassAuditingAndDeliverPayUp(okTime, subAmount, subAmountPos, user.getUserid(), user.getBranchid(), deliverealuser, deliverpayuptype,
					deliverpayupamount, deliverpayupbanknum, deliverpayupaddress, deliverpayuparrearage, deliverpayupamount_pos, deliverpayuparrearage_pos, deliverAccount, deliverPosAccount, -1);
			// 处理小件员交易明细
			FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
			fdpud.setDeliverealuser(uPayupDeliver.getUserid());
			fdpud.setPayupamount(new BigDecimal(subAmount));
			fdpud.setDeliverpayupamount(deliverpayupamount);
			fdpud.setDeliverAccount(deliverAccount);
			fdpud.setDeliverpayuparrearage(deliverpayuparrearage);
			fdpud.setPayupamount_pos(new BigDecimal(subAmountPos));
			fdpud.setDeliverpayupamount_pos(deliverpayupamount_pos);
			fdpud.setDeliverPosAccount(deliverPosAccount);
			fdpud.setDeliverpayuparrearage_pos(deliverpayuparrearage_pos);
			fdpud.setGcaid(gcaId);
			fdpud.setAudituserid(user.getUserid());
			fdpud.setCredate(okTime);
			fdpud.setType(FinanceDeliverPayUpDetailTypeEnum.JiaoKuan.getValue());
			fdpud.setRemark("");
			this.logger.info("小件员交款产生交易：{}", fdpud.toString());
			this.financeDeliverPayUpDetailDAO.insert(fdpud);
			// 处理小件员帐户
			uPayupDeliver.setDeliverAccount(deliverpayuparrearage);
			uPayupDeliver.setDeliverPosAccount(deliverpayuparrearage_pos);
			this.userDAO.updateUserAmount(uPayupDeliver.getUserid(), uPayupDeliver.getDeliverAccount(), uPayupDeliver.getDeliverPosAccount());

		} else {// 不使用小件员交款功能
			gcaId = this.gotoClassAuditingDAO.creGotoClassAuditing(okTime, subAmount, subAmountPos, user.getUserid(), user.getBranchid(), deliverealuser);
		}

		this.logger.info("开始更新归班记录订单,id:{},cwbs:{}", gcaId, subTrStr);
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal amount_pos = BigDecimal.ZERO;
		String[] cwbs = subTrStr.split(",");
		// String accountCwbs="";
		StringBuffer accountCwbs = new StringBuffer();

		List<Branch> zhongzhuanbranchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhongZhuan.getValue() + "");

		for (String cwb : cwbs) {
			cwb = cwb.replaceAll("'", "");
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
			DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			if (deliverystate == null) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
			}
			if (deliverystate.getGcaid() > 0) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), ExceptionCwbErrorTypeEnum.ChongFuShenHe, co.getCwb());
			}
			FlowOrderTypeEnum auditFlowOrderTypeEnum = FlowOrderTypeEnum.YiShenHe;
			this.deliveryStateDAO.auditDelivery(deliverystate.getId(), user.getUserid(), okTime, gcaId);
			this.deliveryCashDAO.saveDeliveryCashForGBById(okTime, gcaId, deliverystate.getId());
			// 更改订单的订单状态为退货的流向
			this.deliverPodForCwbstate(co.getCwb(), deliverystate.getDeliverystate(), auditFlowOrderTypeEnum, user);
			// 更改反馈表中的归班时间
			this.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.YiShenHe, "", System.currentTimeMillis());
			// 当订单归班审核配送成功和上门退拒退 和 货物丢失状态时，删除操作时间记录
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue())
					|| (deliverystate.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue())) {
				this.operationTimeDAO.delOperationTime(co.getCwb());
			} else {// 如果不是最终，则更新跟踪记录
				this.operationTimeDAO.updateOperationTime(cwb, user.getBranchid(), FlowOrderTypeEnum.YiShenHe.getValue(), deliverystate.getDeliverystate(), co.getNextbranchid());
			}
			amount = amount.add(deliverystate.getCash()).add(deliverystate.getCheckfee()).add(deliverystate.getOtherfee()).subtract(deliverystate.getReturnedfee());
			amount_pos = amount_pos.add(deliverystate.getPos()).add(deliverystate.getCodpos());

			// // 如果包号不为空清空包号zs
			// if (!"".equals(co.getPackagecode())) {
			// String sql =
			// "update express_ops_cwb_detail set packagecode='' where cwb=? and state=1";
			// this.jdbcTemplate.update(sql, co.getCwb());
			// }

			// 如果是在系统中POS反馈，则记录到 POS款项记录表
			if (deliverystate.getPos().compareTo(BigDecimal.ZERO) > 0) {
				long posPayCount = this.posPayMoneyDAO.getPosPayCount(cwb, "", "", "", "", 0);
				if (posPayCount == 0) { // 如果不存在，则插入
					this.posPayDAO.save_PosTradeDetailRecord(cwb, "POS手工反馈", deliverystate.getPos().doubleValue(), deliverystate.getDeliveryid(), PaytypeEnum.Pos.getValue(), "系统中反馈为POS刷卡",
							deliverystate.getSign_man(), 1, "", 1, 1, "single", PosEnum.XitongFanKui.getMethod(), 1, "");
				}

			}

			// =======结算逻辑==========
			// 如果订单为配送成功、上门退、上门换则记录订单号
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())
					|| (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
				accountCwbs = accountCwbs.append("'").append(cwb).append("',");
			}

			if (((co.getShouldfare().compareTo(BigDecimal.ZERO) >= 0) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
					|| ((co.getInfactfare().compareTo(BigDecimal.ZERO) > 0) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()))) {
				// 产生运费结算表记录
				AccountCwbFareDetail accountCwbFareDetail = new AccountCwbFareDetail();
				accountCwbFareDetail.setCwb(co.getCwb());
				accountCwbFareDetail.setCustomerid(co.getCustomerid());
				accountCwbFareDetail.setCwbordertypeid(co.getCwbordertypeid());
				accountCwbFareDetail.setDeliverybranchid(co.getDeliverybranchid());
				accountCwbFareDetail.setAudittime(okTime);
				accountCwbFareDetail.setDeliverystate(deliverystate.getDeliverystate());
				accountCwbFareDetail.setShouldfare(co.getShouldfare());
				accountCwbFareDetail.setInfactfare(deliverystate.getInfactfare());
				accountCwbFareDetail.setUserid(deliverystate.getDeliveryid());
				this.logger.info("列入上门退运费表记录中,cwb={},userid={},shouldfee={},infactfare={}",new Object[]{co.getCwb(),deliverystate.getDeliveryid(),co.getShouldfare(),deliverystate.getInfactfare()});
				List<AccountCwbFareDetail> accountCwbFareDetailList = this.accountCwbFareDetailDAO.getAccountCwbFareDetailByCwb(co.getCwb());
				if (accountCwbFareDetailList.size() == 0) {
					this.accountCwbFareDetailDAO.createAccountCwbFareDetail(accountCwbFareDetail);
				} else {
					this.accountCwbFareDetailDAO.saveAccountCwbFareDetailByCwb(accountCwbFareDetail);
				}
			}

			/**
			 * 归班审核自动插入申请表记录
			 */
			int changealowflag = this.getChangealowflagById(co);
			if((changealowflag==1)&&(co.getDeliverystate()==DeliveryStateEnum.DaiZhongZhuan.getValue())){// 要中转申请，就自动插入一条数据
				OrderFlow of = this.orderFlowDAO.getOrderFlowCwb(cwb);
				CwbApplyZhongZhuan cwbApplyZhongZhuan = new CwbApplyZhongZhuan();
				cwbApplyZhongZhuan.setApplybranchid(user.getBranchid());
				cwbApplyZhongZhuan.setApplytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				cwbApplyZhongZhuan.setApplyzhongzhuanbranchid(co.getDeliverybranchid());//配送站点
				cwbApplyZhongZhuan.setApplyuserid(user.getUserid());
				cwbApplyZhongZhuan.setCustomerid(co.getCustomerid());
				cwbApplyZhongZhuan.setCwbordertypeid(co.getCwbordertypeid());
				cwbApplyZhongZhuan.setPaybackfee(co.getPaybackfee());
				cwbApplyZhongZhuan.setReceivablefee(co.getReceivablefee());
				cwbApplyZhongZhuan.setCwb(co.getCwb());
				cwbApplyZhongZhuan.setApplyzhongzhuanremark("申请中转");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cwbApplyZhongZhuan.setArrivebranchtime(sdf.format(of.getCredate()));

				this.cwbApplyZhongZhuanDAO.creAndUpdateCwbApplyZhongZhuan(cwbApplyZhongZhuan);
			}


		}

		// =======结算逻辑 配送结果结算归班审核产生记录==========
		if (!"".equals(accountCwbs.toString())) {
			List<CwbOrder> acountCwbList = this.cwbDAO.getCwbByCwbs(accountCwbs.substring(0, accountCwbs.length() - 1));
			if ((acountCwbList != null) && !acountCwbList.isEmpty()) {
				for (CwbOrder list : acountCwbList) {
					CwbOrder co = list;
					// 事务锁 锁住当前站点
					Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
					// 买单结算
					if (startbranch.getAccounttype() == 1) {
						DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
						BigDecimal fee = new BigDecimal("0");
						fee = deliverystate.getPos();
						if (fee.compareTo(new BigDecimal("0")) > 0) {
							this.logger.info("===开始创建买单结算POS数据===");
							AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
							accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), user.getUserid(),
									co.getStartbranchid());
							// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.Pos.getValue(),user,co.getStartbranchid());
							this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
							this.logger.info("用户:{},创建买单记录,站点:{},订单号:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), co.getCwb() });
						}
					}

					// 配送结果结算
					if (startbranch.getAccounttype() == 2) {
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						accountCwbDetail = this.loadFormForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.GuiBanShenHe.getValue(), user, co.getStartbranchid());
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						this.logger.info("用户:{},创建结算归班审核记录,站点:{},订单id:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), co.getCwb() });
					}
					// 扣款结算
					if (startbranch.getAccounttype() == 3) {
						DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
						BigDecimal fee = new BigDecimal("0");
						fee = deliverystate.getPos();
						if (fee.compareTo(new BigDecimal("0")) > 0) {
							this.logger.info("===开始创建扣款结算POS数据===");
							AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
							accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), fee,
									user.getUserid(), "", 0, 0);
							long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
							this.logger.info("用户:{},创建扣款结算POS：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
						}
					}
				}
			}
		}

		this.logger.info("比对归班总金额,amount:{} amount_pos:{} subAmount:{} subAmountPos:{}", new Object[] { amount, amount_pos, subAmount, subAmountPos });
		if ((amount.compareTo(new BigDecimal(subAmount)) != 0) || (amount_pos.compareTo(new BigDecimal(subAmountPos)) != 0)) {
			throw new ExplinkException("您审核的订单中存在被改动订单，请刷新后重新提交审核");
		}
		this.logger.info("开始创建旧记录,id:{}", gcaId);
		gotoClassOld.setGotoclassauditingid(gcaId);
		this.gotoClassOldDAO.creGotoClassOld(gotoClassOld);
		GotoClassAuditing gotoClassAuditingByGcaid = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(gcaId);
		this.okJMS(gotoClassAuditingByGcaid);
		return "{\"errorCode\":0,\"error\":\"处理成功\"}";
	}

	/*public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		System.out.println(str);
	}*/


	/**
	 * 退供货商出库
	 *
	 * @param co
	 * @param customerid
	 * @return
	 */
	public CwbOrder backtocustom(User user, String cwb, String scancwb, long requestbatchno, String baleno, boolean anbaochuku) {
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		if ((co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) && (((co.getSendcarnum() >= 1) && (co.getSendcarnum() == co.getScannum())) || (co.getSendcarnum() == 0))) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) && !anbaochuku) {
			return this.handleBacktocustomYipiaoduojian(user, cwb, scancwb, requestbatchno, co, FlowOrderTypeEnum.TuiGongYingShangChuKu, isypdjusetranscwb, baleno);
		} else if ((co.getSendcarnum() <= 1) || anbaochuku) {
			this.handleBacktocustom(user, cwb, scancwb, requestbatchno, co, FlowOrderTypeEnum.TuiGongYingShangChuKu, isypdjusetranscwb, false, anbaochuku);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		// //包号处理开始
		// disposePackageCode(baleno, scancwb, user, co);
		// //包号处理结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	/**
	 * 退供货商出库
	 *
	 * @param co
	 * @param customerid
	 * @return
	 */
	public CwbOrder backtocustom(User user, String cwb, String scancwb, long requestbatchno, String baleno, boolean anbaochuku, long customerid) {
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if (co.getCustomerid() != customerid) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.GONG_YING_SHANG_XUAN_ZE_CUO_WU, this.customerDAO.getCustomerById(
					co.getCustomerid()).getCustomername());

		}
		if ((co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) && (((co.getSendcarnum() >= 1) && (co.getSendcarnum() == co.getScannum())) || (co.getSendcarnum() == 0))) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) && !anbaochuku) {
			return this.handleBacktocustomYipiaoduojian(user, cwb, scancwb, requestbatchno, co, FlowOrderTypeEnum.TuiGongYingShangChuKu, isypdjusetranscwb, baleno);
		} else if ((co.getSendcarnum() <= 1) || anbaochuku) {
			this.handleBacktocustom(user, cwb, scancwb, requestbatchno, co, FlowOrderTypeEnum.TuiGongYingShangChuKu, isypdjusetranscwb, false, anbaochuku);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		// //包号处理开始
		// disposePackageCode(baleno, scancwb, user, co);
		// //包号处理结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleBacktocustomYipiaoduojian(User user, String cwb, String scancwb, long requestbatchno, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, String baleno) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getStartbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleBacktocustom(user, cwb, scancwb, requestbatchno, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleBacktocustom(user, cwb, scancwb, requestbatchno, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
		}
		// //包号处理开始
		// disposePackageCode(baleno, scancwb, user, co);
		// //包号处理结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleBacktocustom(User user, String cwb, String scancwb, long requestbatchno, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj,
			boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=?,cwbstate=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, user.getBranchid(), 0, CwbStateEnum.TuiGongYingShang.getValue(), cwb);

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
		this.creOrderbackRecord(cwb,co.getCwbordertypeid(),co.getCustomerid(),co.getReceivablefee(),co.getEmaildate(),DateTimeUtil.getNowTime());
	}

	//新加---在退供应商出库操作时存入退供应商数据lx
	public void creOrderbackRecord(String cwb,int cwbordertypeid,long customerid,BigDecimal recievefee,String emaildate,String createtime){
		OrderbackRecord or = new OrderbackRecord();
		or.setCwb(cwb);
		or.setCwbordertypeid(cwbordertypeid);
		or.setCustomerid(customerid);
		or.setReceivablefee(recievefee);
		or.setEmaildate(emaildate);
		or.setCreatetime(createtime);
		this.orderbackRecordDao.creOrderbackRecord(or);
	}

	/**
	 * 供货商拒收返库
	 *
	 * @param co
	 * @return
	 */
	public CwbOrder customrefuseback(User user, String cwb, String scancwb, long requestbatchno, String comment) {
		this.logger.info("{} 将订单 {} 审核为供货商拒收返库", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), cwb);

		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleCustomrefusebackYipiaoduojian(user, cwb, scancwb, requestbatchno, comment, co, FlowOrderTypeEnum.GongYingShangJuShouFanKu, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleCustomrefuseback(user, cwb, scancwb, requestbatchno, comment, co, FlowOrderTypeEnum.GongYingShangJuShouFanKu, isypdjusetranscwb, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return co;
	}

	private CwbOrder handleCustomrefusebackYipiaoduojian(User user, String cwb, String scancwb, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum,
			long isypdjusetranscwb) {

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleCustomrefuseback(user, cwb, scancwb, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleCustomrefuseback(user, cwb, scancwb, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleCustomrefuseback(User user, String cwb, String scancwb, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb,
			boolean isypdj) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);
		String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
		String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
		try {
			String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=?,cwbremark=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), user.getBranchid(), 0, 0, newcwbremark, cwb);
		} catch (Exception e) {
			this.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, comment, System.currentTimeMillis());

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
	}

	/*
	 * private void validateParameter(Map<String, Object> parameterMap,
	 * FlowOrderTypeEnum orderTypeEnum) { if (orderTypeEnum.getValue() ==
	 * FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) { String comment =
	 * (String) parameterMap.get("comment"); long branchid =
	 * parameterMap.get("branchid") == null ? 0 : (Long)
	 * parameterMap.get("branchid"); long customerid =
	 * parameterMap.get("customerid") == null ? 0 : (Long)
	 * parameterMap.get("customerid"); long driverid =
	 * parameterMap.get("driverid") == null ? 0 : (Long)
	 * parameterMap.get("driverid"); long truckid = parameterMap.get("truckid")
	 * == null ? 0 : (Long) parameterMap.get("truckid"); long baleid =
	 * parameterMap.get("baleid") == null ? 0 : (Long)
	 * parameterMap.get("baleid");
	 *
	 * if (!StringUtils.hasLength(comment)) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "备注"); }
	 * if (branchid != 0) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "下一站"); }
	 * if (customerid != 0) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "供货商"); }
	 * if (driverid != 0) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "司机"); }
	 * if (truckid != 0) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "车辆"); }
	 * if (baleid != 0) { throw new
	 * ExplinkException(ExceptionCwbErrorTypeEnum.Field_IS_Mandatory, "包号"); }
	 *
	 * } }
	 */

	@Produce(uri = "jms:topic:gotoClass")
	ProducerTemplate sendJMSGotoClass;

	public void okJMS(GotoClassAuditing gotoClassAuditingByGcaid) {
		try {
			JSONObject sendJson = JSONObject.fromObject(gotoClassAuditingByGcaid);
			List<DeliveryState> deliverByGcaid = this.deliveryStateDAO.getDeliverByGcaid(gotoClassAuditingByGcaid.getId());
			StringBuilder builder = new StringBuilder();
			for (DeliveryState state : deliverByGcaid) {
				builder.append("'").append(state.getCwb()).append("',");
			}
			if (builder.length() > 1) {
				builder.deleteCharAt(builder.length() - 1);
			}

			sendJson.put("cwbs", builder.toString());
			SystemInstall install = this.systemInstallDAO.getSystemInstall("usedeliverpayup");
			sendJson.put("usedeliverpayup", install.getValue());
			this.sendJMSGotoClass.sendBodyAndHeader(null, "GotoClassAuditing", sendJson.toString());
		} catch (Exception ee) {
			this.logger.error("归班审核:sendJson:" + gotoClassAuditingByGcaid.toString(), ee);
		}
	}

	/**
	 *
	 * @param cwb
	 * @param operator
	 */
	public CwbOrder supplierBackSuccess(User user, String cwb, String scancwb, long flowordertype) {
		this.logger.info("{} 将订单 {} 改为供货商退货成功", flowordertype, cwb);

		cwb = this.translateCwb(cwb);
		return this.supplierBackSuccessHandle(user, cwb, scancwb);
	}

	@Transactional
	public CwbOrder supplierBackSuccessHandle(User user, String cwb, String scancwb) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleSupplierBackSuccessYipiaoduojian(user, cwb, scancwb, co, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleSupplierBackSuccess(user, cwb, scancwb, co, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong, isypdjusetranscwb, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleSupplierBackSuccessYipiaoduojian(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleSupplierBackSuccess(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleSupplierBackSuccess(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleSupplierBackSuccess(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj) {
		/*
		 * validateCwbState(co, flowOrderTypeEnum);
		 *
		 * validateStateTransfer(co, flowOrderTypeEnum);
		 */

		String sql = "update express_ops_cwb_detail set startbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, 0, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), cwb);
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis());
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
	}

	/**
	 * 订单拦截
	 *
	 * @param user
	 * @param cwb
	 * @param operator
	 * @param reasonid
	 * @return
	 */
	public CwbOrder auditToTuihuo(User user, String cwb, String scancwb, long flowOrderType, long reasonid) {
		this.logger.info("{} 将 {} 订单拦截", flowOrderType, cwb);

		cwb = this.translateCwb(cwb);
		return this.tuihuoHandle(user, cwb, scancwb, reasonid);
	}

	/*
	 * @Transactional public CwbOrder auditToTuihuoHandle(User user, String
	 * cwb,long flowOrderType, long reasonid) {
	 *
	 * CwbOrder cwbOrder = cwbDAO.getCwbByCwbLock(cwb); if (cwbOrder == null) {
	 * throw new CwbException(cwb,FlowOrderTypeEnum.DingDanLanJie.getValue(),
	 * ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO); }
	 *
	 * Reason r = reasonDAO.getReasonByReasonid(reasonid); // 更新订单状态 String sql
	 * =
	 * "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1"
	 * ; jdbcTemplate.update(sql, FlowOrderTypeEnum.DingDanLanJie.getValue(),
	 * r.getReasoncontent(), r.getReasonid(), cwb); updateCwbState(cwb,
	 * CwbStateEnum.TuiHuo); createFloworder(user, user.getBranchid(), cwbOrder,
	 * FlowOrderTypeEnum.DingDanLanJie, r.getReasoncontent()); return
	 * cwbDAO.getCwbByCwb(cwb); }
	 */

	@Transactional
	public CwbOrder tuihuoHandle(User user, String cwb, String scancwb, long reasonid) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleTuihuoYipiaoduojian(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, reasonid);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleTuihuo(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, false, reasonid);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	@Transactional
	public CwbOrder tuihuoHandleVipshop(User user, String cwb, String scancwb, long reasonid) {

		this.logger.info("vipshop拦截订单{}",cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleTuihuoYipiaoduojian(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, reasonid);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleTuihuoVipshop(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, false, reasonid);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		//删除打印表记录
		this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);

		return this.cwbDAO.getCwbByCwb(cwb);
	}


	private CwbOrder handleTuihuoYipiaoduojian(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long reasonid) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleTuihuo(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, reasonid);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleTuihuo(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, reasonid);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleTuihuo(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, long reasonid) {
		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) || (co.getCwbstate() == CwbStateEnum.TuiHuo.getValue()))
				&& !(((co.getSendcarnum() > 0) || (co.getBackcarnum() > 0)) && (co.getTranscwb().length() > 0) && !co.getCwb().equals(co.getTranscwb()) && (co.getFlowordertype() != FlowOrderTypeEnum.DingDanLanJie
						.getValue()))) {

		} else {
			Reason r = this.reasonDAO.getReasonByReasonid(reasonid);

			String sql = "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r == null ? "" : r.getReasoncontent(), r == null ? 0 : r.getReasonid(), cwb);
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r == null ? "" : r.getReasoncontent(), System.currentTimeMillis());
			if ((isypdjusetranscwb == 1) && isypdj) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
			}
		}
	}


	/**
	 * 唯品会上门揽退订单拦截
	 * @param user
	 * @param cwb
	 * @param scancwb
	 * @param co
	 * @param flowOrderTypeEnum
	 * @param isypdjusetranscwb
	 * @param isypdj
	 * @param reasonid
	 */
	private void handleTuihuoVipshop(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, long reasonid) {
		if (( (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) || ((co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())
				|| (co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue())
				))
				&& !(((co.getSendcarnum() > 0) || (co.getBackcarnum() > 0)) && (co.getTranscwb().length() > 0) && !co.getCwb().equals(co.getTranscwb()) && (co.getFlowordertype() != FlowOrderTypeEnum.DingDanLanJie
						.getValue()))) {

		} else {
			Reason r = this.reasonDAO.getReasonByReasonid(reasonid);

			String sql = "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r == null ? "" : r.getReasoncontent(), r == null ? 0 : r.getReasonid(), cwb);
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r == null ? "" : r.getReasoncontent(), System.currentTimeMillis());
			if ((isypdjusetranscwb == 1) && isypdj) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
			}
		}
	}

	/**
	 * 审核为退货再投
	 *
	 * @param user
	 * @param cwb
	 * @param operator
	 * @return
	 */
	public CwbOrder auditToZaiTou(User user, String cwb, String scancwb, long flowOrderType, long reasonid) {
		this.logger.info("{} 将订单 {} 审为退货再投", flowOrderType, cwb);

		cwb = this.translateCwb(cwb);
		return this.zaiTouHandle(user, cwb, scancwb, reasonid);
	}

	@Transactional
	public CwbOrder zaiTouHandle(User user, String cwb, String scancwb, long reasonid) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		// if (co.getSendcarnum() > 1 || co.getBackcarnum() > 1) {
		// return handleZaiTouYipiaoduojian(user, cwb, scancwb, co,
		// FlowOrderTypeEnum.ShenHeWeiZaiTou,isypdjusetranscwb,reasonid);
		// } else if (co.getSendcarnum() == 1 || co.getBackcarnum() == 1) {
		this.handleZaiTou(user, cwb, scancwb, co, FlowOrderTypeEnum.ShenHeWeiZaiTou, isypdjusetranscwb, false, reasonid);
		// } else {
		// throw new
		// CwbException(cwb,FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(),
		// ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		// }

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleZaiTouYipiaoduojian(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long reasonid) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleZaiTou(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, reasonid);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleZaiTou(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, reasonid);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleZaiTou(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, long reasonid) {
		Reason r = this.reasonDAO.getReasonByReasonid(reasonid);

		String sql = "update express_ops_cwb_detail set flowordertype=?,backreturnreason=?,backreturnreasonid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r.getReasoncontent(), r.getReasonid(), cwb);
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.updateCwbState(cwb, CwbStateEnum.PeiShong);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r.getReasoncontent(), System.currentTimeMillis());
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
	}

	/**
	 *
	 * @param cwb
	 * @param operator
	 */

	/*
	 * @Transactional public CwbOrder SpecialCwbHandle(User user, String cwb,
	 * long handleresult, long handleperson, String handlereason) {
	 *
	 * CwbOrder cwbOrder = cwbDAO.getCwbByCwbLock(cwb); if (cwbOrder == null) {
	 * throw new
	 * CwbException(cwb,FlowOrderTypeEnum.YiChangDingDanChuLi.getValue(),
	 * ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO); }
	 *
	 * validateDeliveryState(cwbOrder, FlowOrderTypeEnum.YiChangDingDanChuLi);
	 * // 更新订单状态 String sql =
	 * "update express_ops_cwb_detail set currentbranchid=" + user.getBranchid()
	 * + ",flowordertype=" + FlowOrderTypeEnum.YiChangDingDanChuLi.getValue() +
	 * ",handleresult=" + handleresult + ",handleperson=" + handleperson +
	 * ",handlereason='" + handlereason + "',cwbstate=" +
	 * CwbStateEnum.DiuShi.getValue() + " where cwb='" + cwb +
	 * "' and state =1 "; jdbcTemplate.update(sql); createFloworder(user,
	 * user.getBranchid(), cwbOrder, FlowOrderTypeEnum.YiChangDingDanChuLi,
	 * handlereason); return cwbDAO.getCwbByCwb(cwb); }
	 */

	/**
	 * 异常单处理
	 *
	 * @param user
	 * @param cwb
	 * @param scancwb
	 * @param handleresult
	 * @param handleperson
	 * @param handlereason
	 * @param flowOrderType
	 * @return
	 */
	public CwbOrder SpecialCwbHandle(User user, String cwb, String scancwb, long handleresult, long handleperson, String handlereason, long flowOrderType) {
		this.logger.info("{} 将订单 {} 异常单处理", flowOrderType, cwb);

		cwb = this.translateCwb(cwb);
		return this.specialCwbHandle(user, cwb, scancwb, handleresult, handleperson, handlereason);
	}

	@Transactional
	public CwbOrder specialCwbHandle(User user, String cwb, String scancwb, long handleresult, long handleperson, String handlereason) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.YiChangDingDanChuLi.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}

		this.validateDeliveryState(co, FlowOrderTypeEnum.YiChangDingDanChuLi);

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleSpecialCwbYipiaoduojian(user, cwb, scancwb, co, FlowOrderTypeEnum.YiChangDingDanChuLi, isypdjusetranscwb, handleresult, handleperson, handlereason);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleSpecialCwb(user, cwb, scancwb, co, FlowOrderTypeEnum.YiChangDingDanChuLi, isypdjusetranscwb, false, handleresult, handleperson, handlereason);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.YiChangDingDanChuLi.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleSpecialCwbYipiaoduojian(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long handleresult,
			long handleperson, String handlereason) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
		}
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleSpecialCwb(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, handleresult, handleperson, handlereason);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
				}
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleSpecialCwb(user, cwb, scancwb, co, flowOrderTypeEnum, isypdjusetranscwb, true, handleresult, handleperson, handlereason);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleSpecialCwb(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, long handleresult,
			long handleperson, String handlereason) {
		String sql = "update express_ops_cwb_detail set currentbranchid=" + user.getBranchid() + ",flowordertype=" + FlowOrderTypeEnum.YiChangDingDanChuLi.getValue() + ",handleresult=" + handleresult
				+ ",handleperson=" + handleperson + ",handlereason='" + handlereason + "',cwbstate=" + CwbStateEnum.DiuShi.getValue() + " where cwb='" + cwb + "' and state =1 ";
		this.jdbcTemplate.update(sql);
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, handlereason, System.currentTimeMillis());
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
	}

	public void updateTuihuoBranch(CwbOrder cwbOrder, Branch branch) {
		this.logger.info("更新退货站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());
		this.cwbDAO.updateTuihuoBranchid(branch.getBranchid(), cwbOrder.getCwb());
		this.updateNextBranchId(cwbOrder.getCwb());
	}

	@Transactional
	public void updateDeliveryBranch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype) throws Exception {
		this.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue())
				|| (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {

			this.cwbDAO.updateDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		} else {
			this.cwbDAO.updateDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		}

		this.updateNextBranchId(cwbOrder.getCwb());
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis());

	}

	/**
	 * 匹配新地址库成功返回后处理，更新订单表匹配后的信息
	 *
	 * @param user
	 * @param cwbOrder
	 * @param branch
	 * @param addresscodeedittype
	 * @throws Exception
	 */
	@Transactional
	public void updateAddressMatch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype, List<DeliveryStationVo> deliveryStationList,
			List<DelivererVo> delivererList, List<Integer> timeLimitList) throws Exception {
		this.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue())
				|| (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {

			this.cwbDAO.updateAddressDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype, delivererList, timeLimitList);
		} else {
			this.cwbDAO.updateAddressDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype, delivererList, timeLimitList);
		}

		this.updateNextBranchId(cwbOrder.getCwb());
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis());

	}

	@Transactional
	public void updateDeliveryOutBranch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype, long branchid) throws Exception {
		this.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue())
				|| (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {

			this.cwbDAO.updateDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		} else {
			this.cwbDAO.updateDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		}

		if (branchid != 0) {
			this.logger.info("中转出库强制出库下一站{},单号：{}", branchid, cwbOrder.getCwb());
			this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), branchid);
		}
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis());

	}

	public void updateCwbRemark(String cwb, String remark) {

		cwb = this.translateCwb(cwb);

		this.cwbDAO.updateCwbRemark(cwb, remark);
	}

	/**
	 * 入库备注提交
	 *
	 * @param csremarkid
	 * @param multicwbnum
	 * @param cwb
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param responesXML
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	public void forremark(User user, String csremark, long multicwbnum, String cwb) {

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwbLock(cwb);
		if (cwbOrder == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.BeiZhu.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		this.remarkDAO.saveRemark(new Remark(ReasonTypeEnum.RuKuBeiZhu.getText(), csremark, cwb, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), user.getRealname()));
		String oldcwbremark = cwbOrder.getCwbremark().length() > 0 ? cwbOrder.getCwbremark() + "\n" : "";
		csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + csremark;
		if (multicwbnum == 0) {
			multicwbnum = cwbOrder.getSendcarnum();
		}
		try {
			this.cwbDAO.saveCwbForRemark(csremark, multicwbnum, cwb);
		} catch (Exception e) {
			this.logger.error("error while saveing cwbremark,cwb:" + cwb + "cwbremark:" + csremark, e);
			throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.BeiZhu, csremark, System.currentTimeMillis());
	}

	public void updateSendCarNum(String cwb, int sendcarnum) {
		this.cwbDAO.updateSendCarNum(sendcarnum, cwb);
	}

	private void updateNextBranchId(String cwb) {

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
			this.logger.info("配送订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getDeliverybranchid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getDeliverybranchid());
			if (nextbranchid != 0) {
				this.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
				this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
			}
			return;
		}
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.TuiHuo.getValue())) {
			this.logger.info("退货订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getTuihuoid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getTuihuoid());
			if (nextbranchid != 0) {
				this.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
				this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
			}
			return;
		}
	}

	/**
	 * 产生批次号
	 *
	 * @param user
	 * @param requestbatchno
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param state
	 * @param operatetype
	 * @param cwb
	 * @return
	 */
	@Transactional
	public long checkResponseBatchno(User user, long requestbatchno, long branchid, long driverid, long truckid, long state, long operatetype, String cwbs, long customerid) {
		// 不传批次号也不存在符合条件的批次号的时候创建一条新的批次信息

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);

		StringBuffer cwbsStrSql = new StringBuffer();
		for (String cwb : cwbs.split("-H-")) {
			cwb = cwb.replaceAll("'", "");
			cwbsStrSql.append("'").append(cwb).append("',");
		}
		if (cwbsStrSql.length() > 0) {
			cwbsStrSql = cwbsStrSql.deleteCharAt(cwbsStrSql.length() - 1);
		}
		long flowordertype = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		if (operatetype == OutwarehousegroupOperateEnum.ChuKu.getValue()) {
			flowordertype = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		} else if (operatetype == OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue()) {
			flowordertype = FlowOrderTypeEnum.TuiHuoChuZhan.getValue();
		} else if (operatetype == OutwarehousegroupOperateEnum.TuiGongYingShangChuKu.getValue()) {
			flowordertype = FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue();
		} else if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
			flowordertype = FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		} else if (operatetype == OutwarehousegroupOperateEnum.KuDuiKuChuKu.getValue()) {
			flowordertype = FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue();
		}

		requestbatchno = this.outWarehouseGroupDAO.creOutWarehouseGroup(driverid, truckid, branchid, datetime, operatetype, customerid, user.getBranchid(), cwbsStrSql.toString());

		this.groupDetailDao.delGroupDetailByCwbsAndBranchidAndFlowordertype(cwbsStrSql.toString(), user.getBranchid(), flowordertype);

		// 更改批次中间表中该订单的打印状态为1（已打印），0为未打印(若交接单机制更改的功能上线后历史数据中不存在未打印的了，该段代码可删除）
		for (String cwb : cwbs.split("-H-")) {
			cwb = cwb.replaceAll("'", "");
			this.groupDetailDAO.updateGroupDetailByCwb2(cwb, requestbatchno);
		}

		return requestbatchno;
	}

	/**
	 * 分站到货异常单监控返回
	 *
	 * @param parm
	 *            (cwb,errortype,branchid,userid)
	 * @return
	 */
	public List<ExceptionCwb> controlForBranchImport(User user, long page, String cwb, long scantype, String errortype, long userid, String beginemaildate, String endemaildate, long branchid,
			long scope) {
		try {

			List<ExceptionCwb> eclist = null;

			eclist = this.exceptionCwbDAO.getAllECByPage(page, cwb, scantype, errortype, branchid, userid, 0, beginemaildate, endemaildate, scope);

			return eclist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对应流程每一个环节进行筛选，获得对应的环节对象
	 *
	 * @param ofList
	 *            对应订单的流程列表
	 * @param flowOrderTypes
	 * @return
	 */
	public OrderFlow getOrderFlowByOrderFlowListCwbAndTypes(List<OrderFlow> ofList, FlowOrderTypeEnum flowOrderTypes, FlowOrderTypeEnum flowOrderTypes2) {
		for (int i = ofList.size() - 1; i >= 0; i--) {
			OrderFlow of = ofList.get(i);
			if ((of.getFlowordertype() == flowOrderTypes.getValue()) || (of.getFlowordertype() == flowOrderTypes2.getValue())) {
				return of;
			}
		}
		return null;
	}

	/**
	 * 按订单串和其他条件一块查询
	 *
	 * @param cwbs
	 * @param begindate
	 * @param enddate
	 * @param customerid
	 * @param consigneename
	 * @param consigneemobile
	 * @param consigneeaddress
	 * @return
	 */
	public List<CwbOrder> getListByCwbs(String cwbs, String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, String baleno,
			String transcwb, long page) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if (cwbs.length() > 0) {
			String[] cwbstr = cwbs.split("\r\n");
			for (int i = ((int) page - 1) * Page.ONE_PAGE_NUMBER; i < (((int) page) * Page.ONE_PAGE_NUMBER); i++) {
				if (i == cwbstr.length) {
					break;
				}
				String cwb = this.translateCwb(cwbstr[i].trim());
				List<CwbOrder> oList = this.cwbDAO.getListByCwb(cwb);
				list.addAll(oList);
			}
		} else if (baleno.length() > 0) {
			list = this.cwbDAO.getListByPackagecode(baleno, page);
		} else if (transcwb.length() > 0) {
			list = this.cwbDAO.getListByTransCwb(transcwb, page);
		} else {
			list = this.cwbDAO.getListByCwb(begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, page);
		}
		return list;
	}

	public List<CwbOrder> getOrderList(String cwbs, String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, String baleno,
			String transcwb) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if (cwbs.length() > 0) {
			for (String cwb : cwbs.split("\r\n")) {
				String cwb1 = this.translateCwb(cwb.trim());
				List<CwbOrder> oList = this.cwbDAO.getListByCwb(cwb1);
				list.addAll(oList);
			}

		} else if (baleno.length() > 0) {
			list = this.cwbDAO.getListByPackagecodeExcel(baleno);

		} else if (transcwb.length() > 0) {
			list = this.cwbDAO.getListByTransCwbExcel(transcwb);
		} else {
			list = this.cwbDAO.getListByCwbExcel(begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress);
		}
		return list;
	}

	public long getCountByCwbs(String cwbs, String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, String baleno, String transcwb) {
		long count = 0;
		if (cwbs.length() > 0) {
			List<CwbOrder> list = new ArrayList<CwbOrder>();
			for (String cwb : cwbs.split("\r\n")) {
				String cwb1 = this.translateCwb(cwb.trim());
				List<CwbOrder> oList = this.cwbDAO.getListByCwb(cwb1);
				list.addAll(oList);
			}
			count = list.size();
		} else if (baleno.length() > 0) {
			count = this.cwbDAO.getListByPackagecodeCount(baleno);
		} else if (transcwb.length() > 0) {
			count = this.cwbDAO.getListByTransCwbCount(transcwb);
		} else {
			count = this.cwbDAO.getCountByCwb(begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress);
		}
		return count;
	}

	public void exportExcelMethod(HttpServletResponse response, List<OrderFlowExport> orderFlowViewList) {

		String[] cloumnName1 = new String[4]; // 导出的列名
		String[] cloumnName2 = new String[4]; // 导出的列名
		cloumnName1[0] = "订单号";
		cloumnName1[1] = "操作时间";
		cloumnName1[2] = "操作人";
		cloumnName1[3] = "操作详情";
		cloumnName2[0] = "Cwb";
		cloumnName2[1] = "CreateDate";
		cloumnName2[2] = "OperatorName";
		cloumnName2[3] = "Detail";

		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final List<OrderFlowExport> orderFlowList = orderFlowViewList;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					int count = 0;
					for (OrderFlowExport co : orderFlowList) {
						Row row = sheet.createRow(count + 1);
						row.setHeightInPoints(15);
						if (co != null) {
							for (int i = 0; i < cloumnName.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								try {
									Object a = co.getClass().getMethod("get" + cloumnName3[i]).invoke(co);
									cell.setCellValue(a == null ? "" : a.toString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						count++;
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Branch> getNextPossibleKuFangBranches(User user) {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
			Branch branch = this.branchDAO.getBranchByBranchid(i);
			if (branch.getSitetype() == BranchEnum.KuFang.getValue()) {
				bList.add(this.branchDAO.getBranchByBranchid(i));
			}
		}
		return bList;
	}

	public List<Branch> getNextPossibleBranches(User user) {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
			Branch branch = this.branchDAO.getBranchByBranchid(i);
			if (branch.getBranchid() != 0) {
				bList.add(this.branchDAO.getBranchByBranchid(i));
			}
		}
		return bList;
	}

	@Transactional
	public void cwbremark(String batchcwb, String cwbremark, User user) {
		String[] cwbs = batchcwb.trim().split("\n");
		for (int i = 0; i < cwbs.length; i++) {
			if (cwbs[i].trim().length() == 0) {
				continue;
			}
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(this.translateCwb(cwbs[i].trim()));
			if (co != null) {
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String allremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + cwbremark;
				this.cwbDAO.updateCwbRemark(co.getCwb(), allremark);
			}
		}
	}

	/**
	 * 对orderflow的监听 1.滞留自动领货 2.监听归班的jms，产生待返单记录 3.监听退货出站操作，往退货记录表插入记录
	 * 4.监听退货站入库操作，往退货记录表更新记录
	 *
	 * @param orderFlow
	 */
	@Consume(uri = "jms:queue:VirtualTopicConsumers.receivegoods.orderFlow?concurrentConsumers=5")
	public void autoReceiveGoods(@Header("orderFlow") String orderFlow) {
		try {
			this.logger.info("开始对orderflow的监听");
			OrderFlow orderflow = this.om.readValue(orderFlow, OrderFlow.class);
			this.autoReceiveGoods(orderflow);
		} catch (Exception e) {
			this.logger.info("对orderflow的监听  处理异常,orderFlow:{}", orderFlow);
			e.printStackTrace();
		}
	}

	public void autoReceiveGoods(OrderFlow orderflow) throws IOException, JsonParseException, JsonMappingException {
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.om.readValue(orderflow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
		CwbOrder co = cwbOrderWithDeliveryState.getCwbOrder();

		// 1.滞留自动领货
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrderWithDeliveryState.getDeliveryState().getIsautolinghuo() == 1)) {
			User user = this.userDAO.getUserByUserid(orderflow.getUserid());
			User deliveryUser = this.userDAO.getUserByUserid(JSONObject.fromObject(JSONObject.fromObject(orderflow.getFloworderdetail()).getString("cwbOrder")).getLong("deliverid"));
			this.receiveGoods(user, deliveryUser, orderflow.getCwb(), orderflow.getCwb());
			this.logger.info("滞留自动领货,cwb:{}", co.getCwb());
		}

		// 2.监听归班的jms，产生待返单记录
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())
				&& (cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue())) {
			long isFeedbackcwb = this.customerDAO.getCustomerById(cwbOrderWithDeliveryState.getCwbOrder().getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(
					cwbOrderWithDeliveryState.getCwbOrder().getCustomerid()).getIsFeedbackcwb();
			if (isFeedbackcwb == 1) {
				User user = this.userDAO.getUserByUserid(orderflow.getUserid());

				ReturnCwbs returnCwbs = new ReturnCwbs();
				returnCwbs.setBranchid(user.getBranchid());
				returnCwbs.setCustomerid(co.getCustomerid());
				returnCwbs.setCwb(co.getCwb());
				returnCwbs.setOpscwbid(co.getOpscwbid());
				returnCwbs.setType(ReturnCwbsTypeEnum.DaiFanDanChuZhan.getValue());
				returnCwbs.setUserid(user.getUserid());
				returnCwbs.setTobranchid(0);
				returnCwbs.setIsnow("0");
				this.returnCwbsDAO.creAndUpdateReturnCwbs(returnCwbs);
				this.logger.info("产生返单,cwb:{}", co.getCwb());
			}
		} else {
			this.returnCwbsDAO.deleteReturnCwbByCwb(orderflow.getCwb());
			this.logger.info("删除返单,cwb:{}", co.getCwb());
		}

		// 3.监听退货出站操作，往退货记录表插入记录
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {

			TuihuoRecord tuihuoRecord = new TuihuoRecord();
			tuihuoRecord.setCwb(orderflow.getCwb());
			tuihuoRecord.setBranchid(co.getStartbranchid());
			tuihuoRecord.setTuihuobranchid(co.getNextbranchid());
			tuihuoRecord.setTuihuochuzhantime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()));
			tuihuoRecord.setCustomerid(co.getCustomerid());
			tuihuoRecord.setCwbordertypeid(co.getCwbordertypeid());
			tuihuoRecord.setUserid(orderflow.getUserid());
			// 验证重复
			List<TuihuoRecord> tuihuolist = this.tuihuoRecordDAO.getTuihuoRecordByCwb(orderflow.getCwb());
			if ((tuihuolist != null) && (tuihuolist.size() > 0)) {
				this.tuihuoRecordDAO.updateTuihuoRecord(tuihuoRecord);
				this.logger.info("更新退货,cwb:{}", co.getCwb());
			} else {
				this.tuihuoRecordDAO.creTuihuoRecord(tuihuoRecord);
				this.logger.info("产生退货,cwb:{}", co.getCwb());
			}
		}
		// 4.监听退货站入库操作，往退货记录表更新记录
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			List<TuihuoRecord> tuihuolist = this.tuihuoRecordDAO.getTuihuoRecordByCwb(orderflow.getCwb());

			this.tuihuoRecordDAO.saveTuihuoRecordById(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()), tuihuolist.size() == 0 ? 0 : tuihuolist.get(0).getId());
			this.logger.info("退货站入库,cwb:{}", co.getCwb());
		}

		// 5.导入数据、出库、分站领货、pos支付、修改匹配站，往上门退订单表更新记录
		if (((orderflow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())
				|| (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue())
				|| (orderflow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) || (orderflow
				.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) {

			ShangMenTuiCwbDetail shangMenTuiCwbDetail = new ShangMenTuiCwbDetail();
			shangMenTuiCwbDetail.setCwb(co.getCwb());
			shangMenTuiCwbDetail.setBackcarnum(co.getBackcarnum());
			shangMenTuiCwbDetail.setCarwarehouseid(Long.parseLong(co.getCarwarehouse()));
			shangMenTuiCwbDetail.setConsigneeaddress(co.getConsigneeaddress());
			shangMenTuiCwbDetail.setConsigneemobile(co.getConsigneemobile());
			shangMenTuiCwbDetail.setConsigneename(co.getConsigneename());
			shangMenTuiCwbDetail.setConsigneephone(co.getConsigneephone());
			shangMenTuiCwbDetail.setCustomerid(co.getCustomerid());
			shangMenTuiCwbDetail.setPaybackfee(co.getPaybackfee());
			shangMenTuiCwbDetail.setPrinttime(co.getPrinttime());
			shangMenTuiCwbDetail.setRemark3(co.getRemark3());
			shangMenTuiCwbDetail.setRemark4(co.getRemark4());
			shangMenTuiCwbDetail.setRemark5(co.getRemark5());
			shangMenTuiCwbDetail.setBackcarname(co.getBackcarname());
			shangMenTuiCwbDetail.setConsigneepostcode(co.getConsigneepostcode());
			shangMenTuiCwbDetail.setEmaildate(co.getEmaildate());
			shangMenTuiCwbDetail.setEmaildateid(co.getEmaildateid());
			shangMenTuiCwbDetail.setDeliverybranchid(co.getDeliverybranchid());

			try {
				if (this.shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailCountByCwb(co.getCwb()) > 0) {
					this.shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailByCwb(shangMenTuiCwbDetail);
					this.logger.info("上门退订单更新,cwb:{}", co.getCwb());
				} else {
					this.shangMenTuiCwbDetailDAO.insertShangMenTuiCwbDetail(shangMenTuiCwbDetail);
					this.logger.info("上门退订单插入,cwb:{}", co.getCwb());
				}
			} catch (Exception e) {
				this.logger.error("上门退订单迁移", e);
			}
		}

		// 6.监听归班配送结果为货物丢失、客服标记为货物丢失（亚马逊）、异常单处理的jms，产生货物丢失记录
		if (((orderflow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()))
				|| (orderflow.getFlowordertype() == FlowOrderTypeEnum.ShouGongdiushi.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue())) {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();
			cwbDiuShi.setCwb(co.getCwb());
			cwbDiuShi.setShenhetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()));
			cwbDiuShi.setCustomerid(co.getCustomerid());
			cwbDiuShi.setCwbordertypeid(co.getCwbordertypeid());
			cwbDiuShi.setUserid(orderflow.getUserid());
			cwbDiuShi.setReceivablefee(co.getReceivablefee());
			cwbDiuShi.setPaybackfee(co.getPaybackfee());
			cwbDiuShi.setCaramount(co.getCaramount());
			cwbDiuShi.setBranchid(orderflow.getBranchid());
			cwbDiuShi.setIsendstate(1);

			if (this.cwbDiuShiDAO.getCwbDiuShiByCwbCount(co.getCwb()) > 0) {
				this.cwbDiuShiDAO.saveCwbDiuShi(cwbDiuShi);
				this.logger.info("货物丢失，异常单处理,cwb:{}", co.getCwb());
			} else {
				this.cwbDiuShiDAO.creCwbDiuShi(cwbDiuShi);
				this.logger.info("货物丢失，异常单处理   插入,cwb:{}", co.getCwb());
			}
		} else {
			this.cwbDiuShiDAO.saveCwbDiuShiByCwb(0, co.getCwb());
			this.logger.info("货物丢失，异常单处理,cwb:{}", co.getCwb());
		}
		// 7.监听导入数据、提货、修改匹配站的jms，产生未匹配站点订单记录表记录
		if (co.getDeliverybranchid() == 0) {
			NoPiPeiCwbDetail noPiPeiCwbDetail = new NoPiPeiCwbDetail();
			noPiPeiCwbDetail.setCwb(co.getCwb());
			noPiPeiCwbDetail.setCarwarehouseid(Long.parseLong(((co.getCarwarehouse() == null) || "".equals(co.getCarwarehouse())) ? "0" : co.getCarwarehouse()));
			noPiPeiCwbDetail.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()));

			if (this.noPiPeiCwbDetailDAO.getNoPiPeiCwbDetailByCwbCount(co.getCwb()) == 0) {
				this.noPiPeiCwbDetailDAO.creNoPiPeiCwbDetail(noPiPeiCwbDetail);
			} else {
				this.noPiPeiCwbDetailDAO.saveNoPiPeiCwbDetail(noPiPeiCwbDetail);
			}
		} else {
			this.noPiPeiCwbDetailDAO.deleteNoPiPeiCwbDetailByCwb(co.getCwb());
		}

		// 8.揽收订单领货时更新揽收表中订单信息
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getCustomerid() == -2)) {
			CwbKuaiDi cwbKuaiDi = new CwbKuaiDi();
			cwbKuaiDi.setCwb(co.getCwb());
			cwbKuaiDi.setPaisongtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()));
			cwbKuaiDi.setPaisonguserid(co.getDeliverid());
			cwbKuaiDi.setPaisongbranchid(orderflow.getBranchid());

			this.cwbKuaiDiDAO.saveCwbKuaiDiPaiSongByCwb(cwbKuaiDi);
			this.logger.info("揽收,cwb:{}", co.getCwb());
		}

		// 9.揽收订单中转出站时更新揽收表中订单信息
		List<Branch> branchList = this.branchDAO.getAllBranches();
		long currentbranchsite = this.getQueryBranchSitetype(branchList, co.getStartbranchid());
		long nextbranchsite = this.getQueryBranchSitetype(branchList, co.getNextbranchid());
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getCustomerid() == -2) && (currentbranchsite == BranchEnum.ZhanDian.getValue())
				&& (nextbranchsite == BranchEnum.ZhongZhuan.getValue())) {
			this.cwbKuaiDiDAO.saveCwbKuaiDiZhongZhuanByCwb(co.getNextbranchid(), co.getCwb());
			this.logger.info("揽收订单中转出站时更新揽收表中订单信息,cwb:{}", co.getCwb());
		}

		//10.分站领货更新中转申请表状态为1/退货申请审核失败，这样统计待领货则不需要查询统计了
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			this.logger.info("分站领货更新中转申请失败、退货申请审核失败表isstastics为1,cwb={}",orderflow.getCwb());
			this.cwbApplyZhongZhuanDAO.updateStastics(orderflow.getCwb()); //修改中转审核状态修改
			this.orderBackCheckDAO.updateStastics(orderflow.getCwb());
		}


	}

	// 获取对应机构的机构类型
	public long getQueryBranchSitetype(List<Branch> branchList, long branchid) {
		long branchsitetype = 0;
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchsitetype = b.getSitetype();
				break;
			}
		}
		return branchsitetype;
	}

	/**
	 * 一般库存list
	 *
	 * @param user
	 * @return
	 */
	public List<CwbOrder> getkucunList(User user) {
		long sitetype = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();

		List<CwbOrder> cwblist = new ArrayList<CwbOrder>();
		if (sitetype == BranchEnum.KuFang.getValue()) {
			// 库房库存：库房已入库未出库的订单总数
			cwblist = this.cwbDAO.getChukuForCwbOrder(user.getBranchid(), 1);
		} else if (sitetype == BranchEnum.ZhanDian.getValue()) {
			// 站点库存：到站未领+退货未出站+滞留+小件员领货无结果
			List<CwbOrder> todayweilinghuolist = new ArrayList<CwbOrder>();// 今日待领货list
			List<CwbOrder> historyweilinghuolist = new ArrayList<CwbOrder>();// 历史待领货list

			// 今日到货订单数
			List<String> todaydaohuocwbs = this.orderFlowDAO.getOrderFlowLingHuoList(user.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime(), "");
			// 今日滞留订单数
			List<String> todayzhiliucwbs = this.orderFlowDAO.getOrderFlowLingHuoList(user.getBranchid(), FlowOrderTypeEnum.YiShenHe.getValue() + "", DateTimeUtil.getCurrentDayZeroTime(), "");

			// 今日到货订单
			List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
			if (todaydaohuocwbs.size() > 0) {
				todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuobyBranchid(user.getBranchid(), this.getStrings(todaydaohuocwbs));
			}
			// 历史到货订单
			List<CwbOrder> historydaohuolist = this.cwbDAO.getHistoryyWeiLingDaohuobyBranchid(user.getBranchid(), this.getStrings(todaydaohuocwbs));

			// 今日滞留订单
			List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();
			if (todayzhiliucwbs.size() > 0) {
				todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), user.getBranchid(), this.getStrings(todayzhiliucwbs));
			}
			// 历史滞留订单
			List<CwbOrder> historyzhiliulist = this.cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), user.getBranchid(), this.getStrings(todayzhiliucwbs));

			todayweilinghuolist.addAll(todaydaohuolist);
			todayweilinghuolist.addAll(todayzhiliulist);

			historyweilinghuolist.addAll(historydaohuolist);
			historyweilinghuolist.addAll(historyzhiliulist);

			cwblist.addAll(todayweilinghuolist);
			cwblist.addAll(historyweilinghuolist);

			// 退货未出站
			cwblist.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndCurrentbranchid(FlowOrderTypeEnum.DingDanLanJie.getValue(), user.getBranchid()));
			/*
			 * cwblist.addAll(cwbDAO.
			 * getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid
			 * (FlowOrderTypeEnum.YiShenHe,
			 * DeliveryStateEnum.JuShou,user.getBranchid()));
			 * cwblist.addAll(cwbDAO
			 * .getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid
			 * (FlowOrderTypeEnum.YiShenHe,
			 * DeliveryStateEnum.BuFenTuiHuo,user.getBranchid()));
			 * cwblist.addAll
			 * (cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid
			 * (FlowOrderTypeEnum.YiShenHe,
			 * DeliveryStateEnum.ShangMenHuanChengGong,user.getBranchid()));
			 * cwblist.addAll(cwbDAO.
			 * getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid
			 * (FlowOrderTypeEnum.YiShenHe,
			 * DeliveryStateEnum.ShangMenTuiChengGong,user.getBranchid()));
			 */

		} else if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			// 中转站库存：中转站已入库未出库的订单总数
			cwblist = this.cwbDAO.getChukuForCwbOrder(user.getBranchid(), 1);
		} else if (sitetype == BranchEnum.TuiHuo.getValue()) {
			// 退货站库存：退货站已入库未再投、未退供货商的订单总数
			List<CwbOrder> weickList = this.cwbDAO.getChukuForCwbOrder(user.getBranchid(), CwbStateEnum.TuiHuo.getValue());
			List<CwbOrder> weitghsckList = this.cwbDAO.getTGYSCKListbyBranchid(user.getBranchid());

			cwblist.addAll(weickList);
			cwblist.addAll(weitghsckList);
		}
		return cwblist;
	}

	/**
	 * 领货库存list
	 *
	 * @param user
	 * @return
	 */
	public List<CwbOrder> getlinghuokucunlist(User user) {
		long sitetype = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();
		List<CwbOrder> yilinghuolist = new ArrayList<CwbOrder>();
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			// 小件员领货
			yilinghuolist = this.cwbDAO.getYiLingHuoListbyBranchidForService(user.getBranchid(), 0);
		}
		return yilinghuolist;
	}

	/**
	 * 产生盘点记录信息
	 *
	 * @param user
	 * @return
	 */
	public void creStock(User user) {
		System.out.println(System.currentTimeMillis() + "--creStock111");
		long sitetype = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();

		this.stockDetailDAO.saveStockDetailStateByBranchid(System.currentTimeMillis() + "", user.getBranchid());

		List<CwbOrder> kucunlist = this.getkucunList(user);

		this.handleStockData(kucunlist, user);

		long kucunnum = kucunlist.size();

		long id = 0;
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			List<CwbOrder> linghuokucunlist = this.getlinghuokucunlist(user);
			this.handleStockData(linghuokucunlist, user);
			long linghuokucunnum = linghuokucunlist.size();
			id = this.stockResultDAO.creStockResult(new StockResult(user.getBranchid(), 0, kucunnum + linghuokucunnum));
		} else {
			// 如果结果表中不存在当前站点的结果信息，插入一条当前 站的结果信息
			id = this.stockResultDAO.creStockResult(new StockResult(user.getBranchid(), 0, kucunnum));
		}

		this.stockDetailDAO.saveStockDetailForResultId(id, user.getBranchid());
		System.out.println(System.currentTimeMillis() + "--creStock333");
	}

	/**
	 * 处理库存list,在库存详情表产生记录
	 *
	 * @param list
	 * @param user
	 */
	public void handleStockData(List<CwbOrder> list, User user) {
		System.out.println(System.currentTimeMillis() + "--handleStockData1");
		if ((list != null) && (list.size() > 0)) {
			for (CwbOrder c : list) {
				Customer customer = this.customerDAO.getCustomerById(c.getCustomerid());
				long isypdjusetranscwb = customer.getCustomerid() == 0 ? 0 : customer.getIsypdjusetranscwb();
				if (c.getSendcarnum() > 1) {
					for (int i = 0; i < c.getSendcarnum(); i++) {
						String transcwb = "explink_" + c.getCwb() + "_" + i;
						if ((isypdjusetranscwb == 1) && ((c.getTranscwb().split(",").length > 1) || (c.getTranscwb().split(":").length > 1))) {
							if (c.getTranscwb().split(",").length > 1) {
								transcwb = c.getTranscwb().split(",")[i];
							} else {
								transcwb = c.getTranscwb().split(":")[i];
							}
						}
						this.stockDetailDAO.creStockDetailByParm(c.getCwb(), transcwb, user.getBranchid(), c.getOrderflowid(), StockDetailStocktypeEnum.LingHuoKuCun.getValue(),
								StockDetailEnum.Kui.getValue());
					}
				} else {
					this.stockDetailDAO.creStockDetailByParm(c.getCwb(), c.getTranscwb(), user.getBranchid(), c.getOrderflowid(), StockDetailStocktypeEnum.YiBanKuCun.getValue(),
							StockDetailEnum.Kui.getValue());
				}
			}
		}
		System.out.println(System.currentTimeMillis() + "--handleStockData2");
	}

	/**
	 * 盘点功能
	 *
	 * @param user
	 * @param cwb
	 * @return
	 */
	public StockResult stock(User user, String cwb) {
		String realscancwb = cwb;
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		long normal = 0, win = 0;

		if (co == null) {
			co = this.createCwbDetail(user, 0, cwb);
			this.stockDetailDAO.creStockDetailByParm(cwb, realscancwb, user.getBranchid(), 0, StockDetailStocktypeEnum.YiBanKuCun.getValue(), StockDetailEnum.Ying.getValue());
			win += 1;
		}

		StockResult stockresult = this.stockResultDAO.getAllStockResultByBranchidAndState(user.getBranchid(), 0);
		if (stockresult != null) {
			String scancwb = realscancwb;
			if (co.getSendcarnum() < 2) {
				if ((co.getTranscwb().length() > 0) && realscancwb.equals(cwb)) {
					scancwb = co.getTranscwb();
				}
			} else {
				if (realscancwb.equals(cwb)) {
					scancwb = "";
				}
			}
			StockDetail stockdetail = this.stockDetailDAO.getStockDetailByCwbAndBranchid(cwb, scancwb, user.getBranchid(), StockDetailEnum.Kui.getValue());
			long type = StockDetailEnum.Ying.getValue();
			if ((stockdetail != null) && (stockdetail.getType() == StockDetailEnum.Kui.getValue())) {
				normal += 1;
				type = StockDetailEnum.ZhengChang.getValue();
				this.stockDetailDAO.saveStockDetailById(type, user.getUserid(), stockdetail.getId());
			} else {
				this.stockDetailDAO.creStockDetailByParm(cwb, scancwb, user.getBranchid(), 0, StockDetailStocktypeEnum.YiBanKuCun.getValue(), StockDetailEnum.Ying.getValue());
				win += 1;
			}

			this.stockResultDAO.saveStockResultById((stockresult.getCheckcount() + normal + win), stockresult.getId());
			stockresult.setCheckcount((stockresult.getCheckcount() + normal + win));
		}
		return stockresult;
	}

	/**
	 * 盘点完成
	 *
	 * @param user
	 * @return
	 */
	public JSONObject StockFinish(User user) {
		JSONObject obj = new JSONObject();
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		// 将盘点记录的盘库状态改为盘库结束
		this.stockResultDAO.saveStockResultByBranchidAndState(user.getBranchid(), 0);

		List<JSONObject> winlist = this.stockDetailDAO.getAllStockDetailByWhere(user.getBranchid(), StockDetailEnum.Ying.getValue(), "1", StockDetailStocktypeEnum.YiBanKuCun.getValue() + ","
				+ StockDetailStocktypeEnum.LingHuoKuCun.getValue());
		List<JSONObject> kuilist = this.stockDetailDAO.getAllStockDetailByWhere(user.getBranchid(), StockDetailEnum.Kui.getValue(), "1", StockDetailStocktypeEnum.YiBanKuCun.getValue() + ","
				+ StockDetailStocktypeEnum.LingHuoKuCun.getValue());
		List<JSONObject> winviewlist = this.getStockAndCwbDetailView(winlist, customerList);
		List<JSONObject> kuiviewlist = this.getStockAndCwbDetailView(kuilist, customerList);
		obj.put("winlist", winviewlist);
		obj.put("kuilist", kuiviewlist);

		obj.put("winnum", winlist.size());
		obj.put("kuinum", kuilist.size());

		return obj;
	}

	public List<JSONObject> getStockAndCwbDetailView(List<JSONObject> list, List<Customer> customerList) {
		List<JSONObject> stockAndCwbDetaiViewList = new ArrayList<JSONObject>();
		if (list.size() > 0) {
			for (JSONObject obj : list) {
				JSONObject objView = new JSONObject();
				objView.put("branchid", obj.getLong("branchid"));
				objView.put("userid", obj.getLong("userid"));
				objView.put("cwb", obj.getString("cwb"));
				objView.put("transcwb", obj.getString("transcwb").indexOf("explink") > -1 ? "" : obj.getString("transcwb"));
				objView.put("id", obj.getLong("id"));
				objView.put("orderflowid", obj.getLong("orderflowid"));
				objView.put("resultid", obj.getLong("resultid"));
				objView.put("type", obj.getLong("type"));
				objView.put("stocktype", obj.getLong("stocktype"));
				objView.put("state", obj.getString("state"));
				objView.put("customername", this.getQueryCustomerName(customerList, obj.getLong("customerid")));
				objView.put("emaildate", obj.getString("emaildate"));
				objView.put("consigneename", obj.getString("consigneename"));
				objView.put("receivablefee", obj.getString("receivablefee"));
				objView.put("consigneeaddress", obj.getString("consigneeaddress"));

				stockAndCwbDetaiViewList.add(objView);
			}
		}
		return stockAndCwbDetaiViewList;
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

	public void deletecwb(String cwb) {
		try {
			this.losecwbbatchProducerTemplate.sendBodyAndHeader(null, "cwbbatchDelete", cwb);
			this.dataLoseByCwb.sendBodyAndHeader(null, "cwb", cwb);
			this.logger.info("订单失效准备发送jms--");
		} catch (Exception e) {
			this.logger.info("ERRO-订单失效准备发送jms-异常");
		}
	}

	public CwbOrder lanShouDaoHuo(User user, String cwb, String scancwb, long deliverid) {
		this.logger.info("开始揽收到货处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.lanShouDaoHuoHandle(user, cwb, scancwb, deliverid);
	}

	@Transactional
	public CwbOrder lanShouDaoHuoHandle(User user, String cwb, String scancwb, long deliverid) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		// 若订单存在
		if (co != null) {
			if (co.getCustomerid() != -2) {
				// 供货商不是快递单
				throw new CwbException(cwb, FlowOrderTypeEnum.LanShouDaoHuo.getValue(), ExceptionCwbErrorTypeEnum.Fei_Kuai_Di_Dan_Bu_Yun_Xu_Lan_Shou_Dao_Huo);
			} else {
				// 重复到货
				throw new CwbException(cwb, FlowOrderTypeEnum.LanShouDaoHuo.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_DAO_HUO);
			}
		}

		this.handleLanShouDaoHuo(user, cwb, scancwb, FlowOrderTypeEnum.LanShouDaoHuo, deliverid);
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleLanShouDaoHuo(User user, String cwb, String scancwb, FlowOrderTypeEnum flowOrderTypeEnum, long deliverid) {
		/*
		 * validateCwbState(co, flowOrderTypeEnum); validateStateTransfer(co,
		 * flowOrderTypeEnum);
		 */

		// 订单不存在时插入一条新数据
		String sql = "insert into express_ops_cwb_detail (cwb,currentbranchid,customerid,flowordertype,cwbstate,cwbordertypeid) values(?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, cwb, user.getBranchid(), -2, flowOrderTypeEnum.getValue(), CwbStateEnum.PeiShong.getValue(), CwbOrderTypeIdEnum.Peisong.getValue());

		// 入站时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);

		// 揽收表产生记录
		String sql2 = "insert into ops_cwbkuaidi_detail (cwb,lanshoubranchid,lanshouuserid,lanshoutime) values(?,?,?,?)";
		this.jdbcTemplate.update(sql2, cwb, user.getBranchid(), deliverid, datetime);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis());
	}

	public AccountCwbDetail loadFormForAccountCwbDetail(CwbOrder co, long branchid, long flowordertype, User user, long currentbranchid) {
		AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
		accountCwbDetail.setBranchid(branchid);// 出库站点ID
		accountCwbDetail.setCustomerid(co.getCustomerid());// 供货商id
		accountCwbDetail.setFlowordertype(flowordertype);// 操作类型
		accountCwbDetail.setDeliverystate(co.getDeliverystate());// 配送结果
		accountCwbDetail.setCheckoutstate(0l);// 结算id
		accountCwbDetail.setDebetstate(0l);// 欠款id
		accountCwbDetail.setCwb(StringUtil.nullConvertToEmptyString(co.getCwb()));// 订单号
		accountCwbDetail.setCwbordertypeid(co.getCwbordertypeid());// 订单类型
		accountCwbDetail.setSendcarnum(co.getSendcarnum());// 发货件数
		accountCwbDetail.setScannum(co.getScannum());// 出库件数
		accountCwbDetail.setCaramount(StringUtil.nullConvertToBigDecimal(co.getCaramount()));// 货物价值
		accountCwbDetail.setReceivablefee(StringUtil.nullConvertToBigDecimal(co.getReceivablefee()));// 代收货款
		accountCwbDetail.setPaybackfee(StringUtil.nullConvertToBigDecimal(co.getPaybackfee()));// 应退款
		accountCwbDetail.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		accountCwbDetail.setUserid(user.getUserid());
		accountCwbDetail.setCurrentbranchid(currentbranchid);// 出库站ID中转站ID退货站ID
		accountCwbDetail.setPos(new BigDecimal("0"));
		accountCwbDetail.setCash(new BigDecimal("0"));
		accountCwbDetail.setCheckfee(new BigDecimal("0"));
		accountCwbDetail.setOtherfee(new BigDecimal("0"));
		if ((flowordertype == AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()) || (flowordertype == AccountFlowOrderTypeEnum.Pos.getValue())) {
			DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			accountCwbDetail.setPos(deliverystate.getPos());// POS
			accountCwbDetail.setCash(deliverystate.getCash());// 现金
			accountCwbDetail.setCheckfee(deliverystate.getCheckfee());// 支票
			accountCwbDetail.setOtherfee(deliverystate.getOtherfee());// 其他
		}
		return accountCwbDetail;
	}

	/**
	 * 执行中转出库，插入中转出库统计表中
	 *
	 * @param user
	 * @param reasonid
	 * @param inOrOutFlag
	 *            0入库 1出库
	 */
	private void produceTransferResStastics(CwbOrder cwbOrder, String dealtime, User user, long reasonid, int inOrOutFlag) {
		Branch b = this.branchDAO.getBranchById(user.getBranchid());

		int isshowzhongzhuan = 0;
		if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			isshowzhongzhuan = 1;
		}

		if (isshowzhongzhuan == 0) {
			return;
		}

		TransferReasonStastics transRes = new TransferReasonStastics();
		transRes.setCwb(cwbOrder.getCwb());
		transRes.setNowtime(DateTimeUtil.getNowTime());

		if (inOrOutFlag == 0) { // 中转入库
			transRes.setInwarehousetime(dealtime);
			this.transferReasonStasticsDao.createTransferReasonStastics(transRes);
		} else { // 中转出库 修改出库时间，原因等等
			transRes.setOutwarehousetime(dealtime);
			int transferReasonid = 0;
			TransferResMatch transferResMatch = this.transferResMatchDao.getTransferResMatchByReasonid(reasonid);
			if (transferResMatch != null) {
				transferReasonid = transferResMatch.getTransferReasonid();
			}
			this.logger.info("订单号中转出库匹配中转原因{},cwb={}", transferReasonid, cwbOrder.getCwb());
			transRes.setTransferreasonid(transferReasonid);
			this.transferReasonStasticsDao.updateTransferReasonStastics(transRes);
		}

	}

	public void datalose_vipshop(String cwb) {

		try {
			CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

			this.cwbDAO.dataLoseByCwb(cwb);
			this.deliveryStateDAO.inactiveDeliveryStateByCwb(cwb);
			this.exportwarhousesummaryDAO.dataLoseByCwb(cwb);
			this.exportwarhousesummaryDAO.LoseintowarhouseByCwb(cwb);
			this.transCwbDao.deleteTranscwb(cwb);
			// 失效订单删除
			this.deletecwb(cwb);
			// 删除倒车时间表的订单
			this.orderArriveTimeDAO.deleteOrderArriveTimeByCwb(cwb);
			// 删除审核为退货再投的订单
			this.orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb);

			if(co!=null){
				if (this.emailDateDAO.getEmailDateById(co.getEmaildateid()) != null) {
					long cwbcount = this.emailDateDAO.getEmailDateById(co.getEmaildateid()).getCwbcount() - 1;
					this.emailDateDAO.editEditEmaildateForCwbcount(cwbcount, co.getEmaildateid());
				}
				if(co!=null){
					this.shiXiaoDAO.creAbnormalOrder(co.getOpscwbid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), co.getCurrentbranchid(), co.getCustomerid(), cwb,
							co.getDeliverybranchid(), co.getFlowordertype(), co.getNextbranchid(), co.getStartbranchid(), 1);
				}
				this.orderFlowDAO.deleteOrderFlowByCwb(cwb);
			}


			//删除打印表记录
			this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);

		} catch (Exception e) {
			this.logger.error("唯品会失效异常cwb="+cwb,e);
		}
	}

	/***
	 * 判断是否修改了支付方式
	 *
	 * @param parameters
	 * @param newpaywayParams
	 * @return
	 */
	public Map<String, Object> checkIsModifyPayMethod(Map<String, Object> parameters, Map<String, Object> newpaywayParams) {
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		long newpaywayid = (Long) (newpaywayParams.get("newpaywayid")==null?Long.valueOf(1L):(newpaywayParams.get("newpaywayid")));
		// 根据订单号查询订单实体
		CwbOrder co = new CwbOrder();
		co = (CwbOrder) newpaywayParams.get("preObj");
		long payWayId = Long.valueOf(co.getNewpaywayid() == null ? "1" : co.getNewpaywayid());
		if (payWayId != newpaywayid) {
			flag = true;
		}
		map.put("flag", flag);
		map.put("oldPayWayId", Long.valueOf(co.getNewpaywayid() == null ? "1" : co.getNewpaywayid()));
		map.put("newPayWayId", newpaywayid);
		return map;
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


	//新增退货再投view
	public List<CwbOrderView> getTuiZaiCwbOrderView(List<CwbOrder> orderlist, List<OperationTime> optList, List<Customer> customerList, List<Branch> branchList
				) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((optList.size() > 0) && (orderlist.size() > 0)) {
			for (OperationTime ot :optList ) {
				for (CwbOrder c : orderlist) {
					if (ot.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setCwb(c.getCwb());
						cwbOrderView.setCwbstate(c.getCwbstate());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(c.getCwbordertypeid()));// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
						cwbOrderView.setConsigneename(c.getConsigneename());//收件人
						cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());//收件人地址
						cwbOrderView.setTuihuozhaninstoreroomtime(this.getStringDate(ot.getCredate()));//退货库入库时间
						cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getStartbranchid()));//退货站的上一站(配送站)
						User user = this.getSessionUser();
						cwbOrderView.setAuditor(user.getRealname());//审核人
						cwbOrderView.setAudittime("");//审核时间
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	//新增退货再投view
	public List<CwbOrderView> getTuigongSuccessCwbOrderView(List<CwbOrder> orderlist, List<OrderbackRecord> orList, List<Customer> customerList
				) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orderlist.size() > 0)) {
			for (CwbOrder c : orderlist){
				for (OrderbackRecord ot :orList ){
					if (ot.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setCwb(ot.getCwb());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
						cwbOrderView.setReceivablefee(ot.getReceivablefee());
						cwbOrderView.setEmaildate(ot.getEmaildate());
						cwbOrderView.setCreatetime(ot.getCreatetime());//退货出库时间
						cwbOrderView.setAuditstatename(this.getAuditname(ot.getShenhestate()));//当前确认状态
						cwbOrderView.setAuditor(ot.getAuditname());//确认人
						cwbOrderView.setAudittime(ot.getAudittime());//确认时间
						cwbOrderView.setNowapplystate(ot.getShenhestate());
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public String getAuditname(int auditstatename){
		if(auditstatename==0){
			return "待确认";
		}else if(auditstatename==1){
			return "退客户成功";
		}else if(auditstatename==2){
			return "拒收退货";
		}
		return "";
	}



	//新增支付信息申请view
	public List<CwbOrderView> getZhifuApplyCwbOrderView( List<ZhiFuApplyView> orList, List<Customer> customerList,
			List<Branch> branchList,List<User> uslist) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orList.size() > 0)) {
			for (ZhiFuApplyView ot :orList){
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setOpscwbid(ot.getApplyid());//存放支付申请的主键id
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
				cwbOrderView.setApplytype(ApplyEnum.getTextByValue(ot.getApplyway()));
				String oldnewCwbordertypename = CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText()+"/"+(ot.getApplycwbordertypeid()==0?"":CwbOrderTypeIdEnum.getByValue(ot.getApplycwbordertypeid()).getText());
				cwbOrderView.setOldnewCwbordertypename(oldnewCwbordertypename);// 订单类型
				String oldnewReceivablefee = ot.getReceivablefee()+"/"+ot.getApplyreceivablefee();
				cwbOrderView.setOldnewReceivablefee(oldnewReceivablefee);//订单金额
				String oldnewPaytype = PaytypeEnum.getTextByValue(ot.getPaywayid())+"/"+(ot.getApplypaywayid()==0?"":PaytypeEnum.getTextByValue(ot.getApplypaywayid()));
				cwbOrderView.setOldnewPaytype(oldnewPaytype);//支付方式
				cwbOrderView.setNowState(this.getNowApplyState(ot.getApplystate()));//订单当前状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, ot.getBranchid()));//当前站点
				cwbOrderView.setApplyuser(this.dataStatisticsService.getQueryUserName(uslist, ot.getUserid()));//申请人
				cwbOrderView.setApplytime(ot.getApplytime());//申请时间
				cwbOrderView.setAuditor(ot.getAuditname());//审核人
				cwbOrderView.setAudittime(ot.getAudittime());//审核时间
				cwbOrderView.setNowapplystate(ot.getApplystate());
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public String getNowApplyState(int statevalue){
		if((statevalue==1)||(statevalue==2)){
			String str = statevalue==1?"待审核":"已审核";
			return str;
		}
		return null;
	}

	//新增支付信息确认view
	public List<CwbOrderView> getZhifuConfirmCwbOrderView(List<ZhiFuApplyView> orList, List<Customer> customerList,
			List<Branch> branchList,List<User> userList	) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orList.size() > 0)) {
			for (ZhiFuApplyView ot :orList){
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setOpscwbid(ot.getApplyid());//存放支付申请的主键id
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
				cwbOrderView.setApplytype(ApplyEnum.getTextByValue(ot.getApplyway()));
				String oldnewCwbordertypename = CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText()+"/"+(ot.getApplycwbordertypeid()==0?"":CwbOrderTypeIdEnum.getByValue(ot.getApplycwbordertypeid()).getText());
				cwbOrderView.setOldnewCwbordertypename(oldnewCwbordertypename);// 订单类型
				String oldnewReceivablefee = ot.getReceivablefee()+"/"+ot.getApplyreceivablefee();
				cwbOrderView.setOldnewReceivablefee(oldnewReceivablefee);//订单金额
				String oldnewPaytype = PaytypeEnum.getTextByValue(ot.getPaywayid())+"/"+(ot.getApplypaywayid()==0?"":PaytypeEnum.getTextByValue(ot.getApplypaywayid()));
				cwbOrderView.setOldnewPaytype(oldnewPaytype);//支付方式
				cwbOrderView.setNowState(this.getNowConfirmState(ot.getConfirmstate()));//订单当前状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, ot.getBranchid()));//当前站点
				cwbOrderView.setApplyuser(this.dataStatisticsService.getQueryUserName(userList, ot.getUserid()));//申请人
				cwbOrderView.setAuditor(ot.getAuditname());//审核人
				cwbOrderView.setAudittime(ot.getAudittime());//审核时间
				cwbOrderView.setConfirmname(ot.getConfirmname());//确认人
				cwbOrderView.setConfirmtime(ot.getConfirmtime());//确认时间
				cwbOrderView.setNowapplystate(ot.getConfirmstate());
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}
	public String getNowConfirmState(int statevalue){
		if((statevalue==1)||(statevalue==2)){
			String str = statevalue==1?"待确认":"已确认";
			return str;
		}
		return null;
	}

	public String getStringDate(long datetime){
		Date date = new Date(datetime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public String getCwbs(String cwbs){
		StringBuffer sb = new StringBuffer();
		for(String str:cwbs.split("\r\n")){
			sb.append("'").append(str).append("',");
		}
		return sb.substring(0, sb.length()-1);
	}

	public List<CwbOrderView> getZhongZhuanCwbOrderView(List<CwbOrder> coList,List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist,
			List<Customer> customerList,List<Branch> branchList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((cwbApplyZhongZhuanlist.size() > 0) && (cwbApplyZhongZhuanlist.size() > 0)) {
			for (CwbOrder c : coList){
				for (CwbApplyZhongZhuan ot :cwbApplyZhongZhuanlist ){
					if (ot.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setOpscwbid(ot.getId());//中转id
						cwbOrderView.setCwb(ot.getCwb());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getByValue((int)(ot.getCwbordertypeid())).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
						cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getCurrentbranchid()));//当前站点
						cwbOrderView.setMatchbranchname(this.dataStatisticsService.getQueryBranchName(branchList,ot.getApplyzhongzhuanbranchid()));//配送站点名称
						cwbOrderView.setInSitetime(ot.getApplytime());//到站时间(审核为待中转时的当前时间)
						cwbOrderView.setAuditor(ot.getAuditname());//审核人
						cwbOrderView.setAudittime(ot.getAudittime());//审核时间
						cwbOrderView.setAuditstatename(this.getAuditstatename(ot.getIshandle()));//审核状态
						cwbOrderView.setAuditstate(ot.getIshandle());
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public String getAuditstatename(long ishandle){
		if(ishandle==2){
			return "审核不通过";
		}else if(ishandle==3){
			return "审核通过";
		}else{
			return "待审核";
		}
	}
	//修改状态重置反馈(页面view)
	public List<CwbOrderView> getResetCwbOrderView(List<ApplyEditDeliverystate> applyeditlist,
			List<User> uslist,List<Branch> branchList,Map<String, String> reasonMap) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (applyeditlist.size() > 0) {
			for (ApplyEditDeliverystate ot :applyeditlist ){
				DeliveryState deliveryState=this.deliveryStateDAO.getDeliveryByCwb(ot.getCwb());
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(ot.getCwbordertypeid()));//订单类型
				cwbOrderView.setDeliveryname(DeliveryStateEnum.getByValue((int)(ot.getNowdeliverystate())).getText());//配送结果
				cwbOrderView.setCwbstatename(CwbStateEnum.getByValue(ot.getCwbstate()).getText());//订单状态
				cwbOrderView.setRemark1("");//结算状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, deliveryState.getDeliverybranchid()));//反馈站点
				cwbOrderView.setResetfeedusername(this.dataStatisticsService.getQueryUserName(uslist,deliveryState.getDeliveryid()));//反馈人
				cwbOrderView.setResetfeedtime(ot.getDeliverpodtime());//反馈时间
				cwbOrderView.setDonepeople(this.dataStatisticsService.getQueryUserName(uslist,ot.getEdituserid()));//操作人
				cwbOrderView.setDonetime(ot.getEdittime());//操作时间
				cwbOrderView.setNowState(this.getNowstate(ot.getShenhestate()));//订单当前状态
				cwbOrderView.setRemark2(DeliveryStateEnum.getByValue((int)ot.getEditnowdeliverystate()).getText());//修改配送结果
				cwbOrderView.setRemark3(reasonMap.get(ot.getReasonid()+""));//原因备注
				cwbOrderView.setRemark4(ot.getEditreason());//备注
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}
	public String getNowstate(int shenhestate){
		String shenhestateString="";
		if (shenhestate==1) {
			shenhestateString="待审核";
		}
		if (shenhestate==2) {
			shenhestateString="审核不通过";
		}
		if (shenhestate==3) {
			shenhestateString="审核通过";
		}
		return shenhestateString;
	}
	public List<CwbOrderView> getCwborderviewList(List<CwbOrder> coList,
			List<ApplyEditDeliverystate> aedsList, List<User> userList,
			List<Branch> branchList) {
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if((coList!=null)&&(aedsList!=null)){
			for(CwbOrder co : coList){
				CwbOrderView cov = new CwbOrderView();
				int index = coList.indexOf(co);
				ApplyEditDeliverystate aeds = aedsList.get(index);
				cov.setOpscwbid(aeds.getId());
				cov.setCwb(aeds.getCwb());//订单号
				cov.setCwbordertypeid(String.valueOf(co.getCwbordertypeid()));//订单类型id
				cov.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(co.getCwbordertypeid()));//订单类型
				cov.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, co.getStartbranchid()));//当前站点
				cov.setDeliveryname(DeliveryStateEnum.getByValue(co.getDeliverystate()).getText());
				cov.setDelivername(this.dataStatisticsService.getQueryUserName(userList, co.getDeliverid()));//小件员
				cov.setRemark1(ApplyEditDeliverystateIshandleEnum.getByValue((int)aeds.getIshandle()).getText());//审核状态
				cov.setRemark2(this.dataStatisticsService.getQueryUserName(userList, aeds.getEdituserid()));//处理人
				cov.setRemark3(aeds.getEditreason());//原因备注
				cov.setDeliverystate(aeds.getEditnowdeliverystate());//申请修改配送结果
				cov.setReasonid(aeds.getReasonid());
				cov.setState(aeds.getState());
				covList.add(cov);
			}
		}
		return covList;
	}

	//退货再投页面显示
	public List<OrderBackRuku> getOrderBackRukuRecord(
			List<OrderBackRuku> obrList, List<Branch> branchList,
			List<Customer> customerList) {
		if(obrList!=null){
			for(OrderBackRuku obr : obrList){
				obr.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(obr.getCwbordertypeid()));//订单类型
				obr.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, obr.getCustomerid()));//客户名称
				obr.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, obr.getBranchid()));//站点名
				obr.setAuditstatename(this.getAuditstatename(obr.getAuditstate()));
			}
		}
		return obrList;
	}
	public String getAuditstatename(int auditstate){
		if(auditstate==0){
			return "待审核";
		}else{
			return "已审核";
		}
	}



	public List<ApplyEditDeliverystate> getapplyeditdeliverysates(List<ApplyEditDeliverystate> applyEditDeliverystateLists,Map<String, String> reasonMap,
			List<Branch> branchlist,List<User> userList,List<String> strList
			){
		if((applyEditDeliverystateLists!=null)&&(applyEditDeliverystateLists.size()>0)&&(reasonMap.size()>0)){
			for(ApplyEditDeliverystate aeds : applyEditDeliverystateLists){
				int ad = applyEditDeliverystateLists.indexOf(aeds);
				aeds.setReasoncontent(reasonMap.get(aeds.getReasonid()+"")==null?"":reasonMap.get(aeds.getReasonid()+"").toString());//原因备注
				aeds.setEditusername(this.dataStatisticsService.getQueryUserName(userList, aeds.getEdituserid()));//处理人
				aeds.setHandlename(this.gethandlename(aeds.getIshandle()));//处理状态
				CwbOrder co = this.cwbDAO.getCwbByCwb(strList.get(ad));
				aeds.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchlist, co.getStartbranchid()));//当前站点
				aeds.setApplybranchname(this.dataStatisticsService.getQueryBranchName(branchlist,aeds.getApplybranchid()));//申请站点
				aeds.setNowdeliveryname(DeliveryStateEnum.getByValue((int)aeds.getNowdeliverystate()).getText());//修改前的配送结果
				aeds.setEditnowdeliveryname(DeliveryStateEnum.getByValue((int)aeds.getEditnowdeliverystate()).getText());//修改后的配送结果
				aeds.setDelivername(this.dataStatisticsService.getQueryUserName(userList, aeds.getDeliverid()));//小件员名字
			}
		}
		return applyEditDeliverystateLists;
	}

	public String gethandlename(long ishandle){
		String str = ishandle==1?"已处理":"未处理";
		return str;
	}

	/*public String getCwbsBydate(long flowordertypeid, String begindate,
			String enddate) {
		List<OrderFlow> orderList = orderFlowDAO.getOrderByCredates(flowordertypeid,begindate,enddate);
		StringBuffer sb = new StringBuffer("");
		for(OrderFlow of:orderList){
			sb.append("'"+of.getCwb()+"',");
		}
		if(sb.length()>0){
			return sb.toString().substring(0, sb.length()-1);
		}
		return null;
	}*/
	public ExplinkResponse responseErrorZhongzhuanrukuLimit(){
		ExplinkResponse explinkResponse=new ExplinkResponse();
		explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuanruku.getText());
		explinkResponse.setStatuscode("11111");
		return explinkResponse;
	}
}
