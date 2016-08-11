package cn.explink.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import org.apache.commons.collections4.CollectionUtils;
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

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

import cn.explink.aspect.OrderFlowOperation;
import cn.explink.b2c.auto.order.service.AutoUserService;
import cn.explink.b2c.maisike.branchsyn_json.Stores;
import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tps.TpsCwbFlowService;
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
import cn.explink.dao.FlowExpDao;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GotoClassOldDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.MqExceptionDAO;
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
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.TransferReasonStasticsDao;
import cn.explink.dao.TransferResMatchDao;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToBranchDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.dao.ZhiFuApplyDao;
import cn.explink.dao.searchEditCwbInfoDao;
import cn.explink.domain.AccountCwbDetail;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeductRecord;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Bale;
import cn.explink.domain.BaleCwb;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.domain.ChangeGoodsTypeResult;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbApplyZhongZhuan;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrderBranchMatchVo;
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
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.NoPiPeiCwbDetail;
import cn.explink.domain.OperationTime;
import cn.explink.domain.OrderArriveTime;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.OrderBackRuku;
import cn.explink.domain.OrderbackRecord;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.ReturnCwbs;
import cn.explink.domain.SearcheditInfo;
import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.domain.StockDetail;
import cn.explink.domain.StockResult;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.TransferReasonStastics;
import cn.explink.domain.TransferResMatch;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.YpdjHandleRecord;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.domain.VO.DeliverServerParamVO;
import cn.explink.domain.VO.DeliverServerPushVO;
import cn.explink.domain.VO.GoodInfoVO;
import cn.explink.domain.VO.express.BatchCount;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.ApplyEditDeliverystateIshandleEnum;
import cn.explink.enumutil.ApplyEnum;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.ReturnCwbsTypeEnum;
import cn.explink.enumutil.StockDetailEnum;
import cn.explink.enumutil.StockDetailStocktypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.schedule.Constants;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.service.mps.CwbOrderBranchInfoModificationService;
import cn.explink.service.mps.MPSCommonService;
import cn.explink.service.mps.MPSOptStateService;
import cn.explink.service.mps.OrderInterceptService;
import cn.explink.service.mps.release.DeliverTakeGoodsMPSReleaseService;
import cn.explink.service.mps.release.OutWarehouseMPSReleaseService;
import cn.explink.service.mps.release.ReturnToCustomerReleaseService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TransCwbService;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.DigestsEncoder;
import cn.explink.util.ExcelUtils;
import cn.explink.util.JsonUtil;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

@Service
@Transactional
public class CwbOrderService extends BaseOrderService {
	private static Logger logger = LoggerFactory.getLogger(CwbOrderService.class);
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
	@Autowired
	DeliverService deliverService;

	@Produce(uri = "jms:topic:orderFlow")
	ProducerTemplate orderFlowProducerTemplate;

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

	@Produce(uri = "jms:topic:transCwbOrderFlow")
	ProducerTemplate transCwbOrderFlowProducerTemplate;

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
	@Autowired
	CustomerDAO customerdao;
	@Autowired
	ZhiFuApplyDao zhiFuApplyDao;
	@Autowired
	searchEditCwbInfoDao editCwbInfoDao;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	@Autowired
	private EmailDateDAO emaildateDAO;
	@Autowired
	OrgBillAdjustmentRecordService orgBillAdjustmentRecordService;
	@Autowired
	AdjustmentRecordService adjustmentRecordService;
	@Autowired
	FnDfAdjustmentRecordService fnDfAdjustmentRecordService;
	@Autowired
	FlowExpDao flowExpDao;

	@Autowired
	DeliverTakeGoodsMPSReleaseService deliverTakeGoodsMPSReleaseService;
	@Autowired
	private OrderInterceptService orderInterceptService;
	@Autowired
	private MPSOptStateService mpsOptStateService;
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;

	@Autowired
	private OutWarehouseMPSReleaseService outWarehouseMPSReleaseService;
	@Autowired
	DataImportService dataImportService;

	@Autowired
	ReturnToCustomerReleaseService customerReleaseService;

	@Autowired
	private CwbOrderBranchInfoModificationService cwbOrderBranchInfoModificationService;
	
	@Autowired
	private AutoUserService autoUserService;

	@Autowired
	private MPSCommonService mpsCommonService;

	@Autowired
	private MPSOptStateService mPSOptStateService;
	@Autowired
	private BaleService baleService;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO; 
	
	@Autowired
	private UserDAO userDao;
	@Autowired
	private TpsCwbFlowService tpsCwbFlowService;
	
	private static final String MQ_FROM_URI_RECEIVE_GOODS_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.receivegoods.orderFlow";
	private static final String MQ_FROM_URI_DELIVERY_APP_JMS_ORDER_FLOW = "jms:queue:VirtualTopicConsumers.deliverAppJms.orderFlow";
	public void insertCwbOrder(final CwbOrderDTO cwbOrderDTO, final long customerid, final long warhouseid, final User user, final EmailDate ed) {
		CwbOrderService.logger.info("导入一条新的订单，订单号为{}", cwbOrderDTO.getCwb());

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
				.update("insert into express_ops_cwb_detail ("
						+ "cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,"
						+ "cwbremark,customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,"
						+ "cartype,carsize,backcaramount,destination,transway,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,"
						+ "cwbdelivertypeid,customerwarehouseid,cwbprovince,cwbcity,cwbcounty,shipcwb,transcwb,serviceareaid,deliverybranchid,orderflowid,"
						+ "flowordertype,emailfinishflag,commonid,modelname,emaildateid,carwarehouse,remark1,remark2,remark3,remark4,"
						+ "remark5,paywayid,newpaywayid,nextbranchid,tuihuoid,cargovolume,consignoraddress,multi_shipcwb,addresscodeedittype,printtime,"
						+ "commoncwb,shouldfare,cwbstate,ismpsflag,mpsallarrivedflag,mpsoptstate,vipclub,tpstranscwb,credate,do_type,"
						+ "paymethod,order_source,announcedvalue) " 
						+ "values("
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?," 
						+ "?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?)", new PreparedStatementSetter() {
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

						// 如果是OXO_JIT类型的订单，则订单状态设为CwbOrderTypeIdEnum.OXO_JIT
						// add by zhouguoting 2015-08-05
						if (cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue()) {
							ps.setLong(63, CwbStateEnum.OXO_JIT.getValue());
						} else {// 其他类型的订单默认为配送状态
							ps.setLong(63, CwbStateEnum.PeiShong.getValue());
						}
						ps.setInt(64, cwbOrderDTO.getIsmpsflag());
						ps.setInt(65, cwbOrderDTO.getMpsallarrivedflag());
						ps.setInt(66, FlowOrderTypeEnum.DaoRuShuJu.getValue());
						ps.setInt(67, cwbOrderDTO.getVipclub());
						ps.setString(68, cwbOrderDTO.getTpsTranscwb());
						ps.setTimestamp(69, Timestamp.valueOf(DateTimeUtil.getNowTime()));
						ps.setInt(70, cwbOrderDTO.getDoType());
						ps.setInt(71, cwbOrderDTO.getPaymethod());
						ps.setInt(72, cwbOrderDTO.getOrderSource());
						ps.setFloat(73, cwbOrderDTO.getAnnouncedvalue()==null ? 0 : cwbOrderDTO.getAnnouncedvalue().floatValue());
					}

				});

		if (cwbOrderDTO.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
			this.dataImportService.insertTransCwbDetail(cwbOrderDTO, (cwbOrderDTO.getEmaildate().length() == 0 ? ed.getEmaildatetime() : cwbOrderDTO.getEmaildate()));
		}
		//Added by leoliao at 2016-03-01
		String allTranscwb = cwbOrderDTO.getTranscwb() == null ? "" : cwbOrderDTO.getTranscwb();
		String strSplit = this.getSplitstring(allTranscwb);
		String[] arrTranscwb = allTranscwb.split(strSplit);
		for (String transcwb : arrTranscwb) {
			if ((transcwb == null) || transcwb.trim().equals("")) {
				continue;
			}
			String selectCwb = this.transCwbDao.getCwbByTransCwb(transcwb);
			if ((selectCwb != null) && !selectCwb.equals("")) {
				continue;
			}
			this.transCwbDao.saveTranscwb(transcwb, cwbOrderDTO.getCwb());
		}
		//Added end

		this.createFloworder(user, user.getBranchid(), cwbOrderDTO.getCwb(), FlowOrderTypeEnum.DaoRuShuJu, "", System.currentTimeMillis(), cwbOrderDTO.getCwb());
		CwbOrderService.logger.info("结算区域accountareaid:{}", cwbOrderDTO.getAccountareaid());
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
		try {
			CwbOrderService.logger.info("消息发送端：changeGoodsTypeTemplate, changeGoodsType={}", object.toString());
			this.changeGoodsTypeTemplate.sendBodyAndHeader(null, "changeGoodsType", object.toString());
		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendChangeGoodsTypeMessage").buildExceptionInfo(e.toString())
					.buildTopic(this.changeGoodsTypeTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("changeGoodsType", object.toString()).getMqException());
		}
	}

	/**
	 * 数据更新数据
	 *
	 * @param branchid
	 * @param user
	 */
	public void updateExcelCwb(CwbOrderDTO cwbOrderDTO, long customerid, long branchid, User user, EmailDate ed, boolean isReImport) {
		CwbOrderService.logger.info("更新一条订单的基本信息，订单号为{}", cwbOrderDTO.getCwb());
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
		String sql = "update express_ops_cwb_detail set "
				+ "consigneename=?,consigneeaddress=?,consigneepostcode=?,consigneephone=?,sendcarname=?,"
				+ "backcarname=?,receivablefee=?,paybackfee=?,carrealweight=?,cwbremark=?," 
				+ "customerid=?,emaildate=?,emaildateid=?,consigneemobile=?,shipcwb=?,"
				+ "exceldeliver=?,consigneeno=?,excelbranch=?,caramount=?,customercommand=?," 
				
				+ "cartype=?,carsize=?,backcaramount=?,destination=?,transway=?,"
				+ "shipperid=?,sendcarnum=?,backcarnum=?,excelimportuserid=?,cwbordertypeid=?,"
				+ "customerwarehouseid=?,cwbprovince=?,cwbcity=?,cwbcounty=?,transcwb=?,"
				+ "serviceareaid=?,orderflowid=?,emailfinishflag=?,commonid=?,modelname=?,"
				
				+ "carwarehouse=?,remark1=?,remark2=?,remark3=?,remark4=?,"
				+ "remark5=?,paywayid=?,newpaywayid=?,addresscodeedittype=?,printtime=?,"
				+ "shouldfare=?,cwbstate =1 where cwb=? and state=1";

		this.jdbcTemplate
				.update(sql, cwbOrderDTO.getConsigneename(), cwbOrderDTO.getConsigneeaddress(), cwbOrderDTO.getConsigneepostcode(), cwbOrderDTO.getConsigneephone(), cwbOrderDTO.getSendcargoname(), 
						cwbOrderDTO.getBackcargoname(), cwbOrderDTO.getReceivablefee(), cwbOrderDTO.getPaybackfee(), cwbOrderDTO.getCargorealweight(), cwbOrderDTO.getCwbremark(), 
						customerid, cwbOrderDTO.getEmaildate().length() == 0 ? ed.getEmaildatetime() : cwbOrderDTO.getEmaildate(), ed.getEmaildateid(), cwbOrderDTO.getConsigneemobile(), cwbOrderDTO.getShipcwb(),
						cwbOrderDTO.getExceldeliver(), cwbOrderDTO.getConsigneeno(), cwbOrderDTO.getExcelbranch(), cwbOrderDTO.getCargoamount(), cwbOrderDTO.getCustomercommand(), 
						
						cwbOrderDTO.getCargotype(), cwbOrderDTO.getCargosize(), cwbOrderDTO.getBackcargoamount(), cwbOrderDTO.getDestination(), cwbOrderDTO.getTransway(), 
						cwbOrderDTO.getShipperid(), cwbOrderDTO.getSendcargonum(), cwbOrderDTO.getBackcargonum(), user.getUserid(), cwbOrderDTO.getCwbordertypeid(), 
						cwbOrderDTO.getCustomerwarehouseid(), cwbOrderDTO.getCwbprovince(), cwbOrderDTO.getCwbcity(), cwbOrderDTO.getCwbcounty(), cwbOrderDTO.getTranscwb(),
						cwbOrderDTO.getServiceareaid(), 0, EmailFinishFlagEnum.ZhengChangRuKu.getValue(), (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()), cwbOrderDTO.getModelname(),
						
						branchid, cwbOrderDTO.getRemark1(), cwbOrderDTO.getRemark2(), cwbOrderDTO.getRemark3(), cwbOrderDTO.getRemark4(), 
						cwbOrderDTO.getRemark5(), cwbOrderDTO.getPaywayid(), cwbOrderDTO.getNewpaywayid(), cwbOrderDTO.getAddresscodeedittype(), cwbOrderDTO.getPrinttime(), 
						cwbOrderDTO.getShouldfare(), cwbOrderDTO.getCwb());
		// 保存操作记录并返回对应的操作记录的id 将id保存到express_ops_cwb_detail记录中 用作双向1对1
		// orderFlowDAO.creOrderFlow(new OrderFlow(0,cwbOrderDTO.getCwb(),
		// branchid, new Timestamp( System.currentTimeMillis()),
		// user.getUserid(),
		// JSONObject.fromObject(cwbDAO.getCwbByCwb(cwbOrderDTO.getCwb())).toString(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),0));
		//
		CwbOrderService.logger.info("结算区域accountareaid:{}", cwbOrderDTO.getAccountareaid());

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

		try {
			CwbOrderService.logger.info("消息发送端：updateBranchFinanceAuditStatusTemplate, updateBranchFinanceAuditStatus={}", param.toString());
			this.updateBranchFinanceAuditStatusTemplate.sendBodyAndHeader("", "updateBranchFinanceAuditStatus", param.toString());
		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendBranchFinanceAuditJMS").buildExceptionInfo(e.toString())
					.buildTopic(this.updateBranchFinanceAuditStatusTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("updateBranchFinanceAuditStatus", param.toString())
					.getMqException());
		}
	}

	public void sendFinanceAuditJMS(List<Long> gcaids, int deliverpayupapproved) {
		if (gcaids.isEmpty()) {
			return;
		}
		JSONObject param = new JSONObject();
		param.put("gcaids", JSONArray.fromObject(gcaids));
		param.put("deliverpayupapproved", deliverpayupapproved);

		try {
			CwbOrderService.logger.info("消息发送端：updateFinanceAuditStatusTemplate, updateFinanceAuditStatus={}", param.toString());
			this.updateFinanceAuditStatusTemplate.sendBodyAndHeader("", "updateFinanceAuditStatus", param.toString());
		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendFinanceAuditJMS").buildExceptionInfo(e.toString())
					.buildTopic(this.updateFinanceAuditStatusTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("updateFinanceAuditStatus", param.toString()).getMqException());
		}
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
			String sql = "insert into express_ops_cwb_detail (cwb,startbranchid,customerid,flowordertype,cwbstate,credate) values(?,?,?,?,?,?)";
			this.jdbcTemplate
					.update(sql, cwb, user.getBranchid(), customerid, CwbFlowOrderTypeEnum.WeiDaoHuo.getValue(), CwbStateEnum.WUShuju.getValue(), Timestamp.valueOf(DateTimeUtil.getNowTime()));
			co = this.cwbDAO.getCwbByCwb(cwb);
		} else {
			// //选择了供货商，但是非本供货商
			// if( customerid < 1 && co.getCustomerid() != customerid ){
			// throw new CwbException(cwb, FlowOrderTypeEnum.TiHuo.getValue(),
			// ExceptionCwbErrorTypeEnum.GONG_YING_SHANG_XUAN_ZE_CUO_WU,this.customerDAO.getCustomerById(co.getCustomerid()).getCustomername());
			// }
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
			CwbOrderService.logger.info("正常入库-保存下一站");
			this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

			String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, user.getBranchid(), CwbFlowOrderTypeEnum.TiHuo.getValue(), co.getCwb());
		}
		this.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.TiHuo, "", System.currentTimeMillis(), cwb, false);
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
		CwbOrderService.logger.info("开始入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.intoWarehousHandle(user, cwb, scancwb, user.getBranchid(), customerid, driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku, true);
	}

	@Transactional
	public CwbOrder intoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long customerid, long driverid, long requestbatchno, String comment, boolean isauto, String baleno, Long credate, boolean anbaochuku, boolean isAutoSupplyLink) {
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;

		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}
		// added by songkaojun 2016-01-18 更新订单上一机构，当前机构，下一机构信息
		this.cwbOrderBranchInfoModificationService.modifyBranchInfo(scancwb);
		this.resetScannumForIntowarehouse(user, cwb, scancwb, currentbranchid, flowOrderTypeEnum, isauto);
		// added by songkaojun 2016-01-12
		// 如果被拦截，则给出提示
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, flowOrderTypeEnum);

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

			if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue()) && (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(CwbStateEnum.WUShuju.getValue(), FlowOrderTypeEnum.RuKu
					.getValue()) != null)) {
				co = this.createCwbDetail(user, customerid, cwb);
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
		}

		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}
		if ((co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())) {
			if (!this.hasAdditionalRecord(co)) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Do_Not_Have_Addition_Record);
			}
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) && !anbaochuku) {
			return this.handleIntowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, credate, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, false, credate, anbaochuku, false, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void resetScannumForIntowarehouse(User user, String cwb, String scancwb, long currentbranchid, FlowOrderTypeEnum flowOrderTypeEnum, boolean isauto) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		long deliverybranchid = 0L;
		if (cwbOrder != null) {
			deliverybranchid = cwbOrder.getDeliverybranchid();
		}
		long branchid = user.getBranchid();
		if (isauto) {
			branchid = currentbranchid;
		}
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, deliverybranchid);
		this.mpsCommonService.resetScannumByTranscwb(scancwb, flowOrderTypeEnum.getValue(), branchid, nextbranchid);
	}

	/**
	 * 是否进行了补录
	 *
	 * @param order
	 * @return
	 */
	private boolean hasAdditionalRecord(CwbOrder order) {
		if ((order == null) || (order.getIsadditionflag() == 0)) {
			return false;
		} else if (order.getIsadditionflag() == 1) {
			return true;
		}
		return false;
	}

	private CwbOrder handleIntowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, Long credate, boolean isAutoSupplyLink) {

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		boolean newMPSOrder = this.mpsCommonService.isNewMPSOrder(scancwb);
		// 先更新运单状态，要不然运单流程里面的当前站为0
		this.mpsOptStateService.updateTransCwbDetailInfo(scancwb, flowOrderTypeEnum, -1L, currentbranchid, 0L);

		if (((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) || (newMPSOrder && (co.getScannum() > 0))) {
			if (co.getScannum() < 1) {
				this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false, false, isAutoSupplyLink);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				// 到车时间入库扫描件数
				this.orderArriveTimeDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
				if (isypdjusetranscwb == 1) {
					if (!newMPSOrder) {
						this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					}
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				}
				if (newMPSOrder) {
					this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false, true, isAutoSupplyLink);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false, false, isAutoSupplyLink);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void updateMPSInfo(CwbOrder co, String scancwb, FlowOrderTypeEnum flowOrderTypeEnum, long currentbranchid, long nextbranchid) {
		if (this.isIntercepted(co)) {
			nextbranchid = this.getNextBranchid(co.getCwb(), currentbranchid);
			currentbranchid = 0L;
		}
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, -1L, currentbranchid, nextbranchid);
	}

	private void validateIsSubCwb(String cwb, CwbOrder co, long flowordertype) {
		String transcwb = co.getTranscwb();
		if (!StringUtils.hasLength(transcwb)) {// 为兼容腾讯达历史数据没有transcwb的问题，下个版本请删除
			return;
		}
		// 排除一票一件，也存在运单号情况，
		if (!((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()))) {
			return;
		}
		String splitString = this.getSplitstring(transcwb);
		String[] split = transcwb.split(splitString);
		for (String string : split) {
			if (string.equals(cwb)) {
				return;
			}
		}
		// 线上问题：退供货商成功确认审核 问题修复
		if (FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue() == flowordertype) {
			if (cwb.equals(co.getCwb())) {
				return;
			}
		}
		// 线上问题：退供应商拒收退货确认审核 问题修复
		if (FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue() == flowordertype) {
			if (cwb.equals(co.getCwb())) {
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

	private void handleIntowarehouse(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku, boolean newMPSOrder, boolean isAutoSupplyLink) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}

		// 自动补充完环节后重新定位当前操作

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		CwbOrderService.logger.info("入库数据批次处理完成");

		// PJ快递单反馈操作流程状态-------LX
		CwbOrderService.logger.info("快递流程状态反馈接口（入库反馈）======开始");
		// 如果不是快递单，则不需要此操作，刘武强 10.19
		if ((flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.RuKu.getValue()) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())) {
			ExpressOperationInfo paramObj = this.contructParam(user, cwb, currentbranchid);
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}
		CwbOrderService.logger.info("快递流程状态反馈接口======结束");

		this.cwbRouteService.reload();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		CwbOrderService.logger.info("正常入库-保存下一站");
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowOrderTypeEnum.getValue(), co.getCwb());

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!newMPSOrder) {
			if (!anbaochuku) {
				this.cwbDAO.updateScannum(co.getCwb(), 1);
			} else {
				this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
			}
		}

		this.intoWarhouse(user, cwb, flowOrderTypeEnum, credate);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			if (!newMPSOrder) {
				this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0, isAutoSupplyLink);
			}
		}

		this.baleDaoHuo(scancwb);
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
				CwbOrderService.logger.info("===创建库房入库到车时间数据开始===");
				this.orderArriveTimeDAO.deleteOrderArriveTimeByCwb(co.getCwb());
				OrderArriveTime orderArriveTime = this.orderArriveTimeService.loadFormForOrderArriveTime(co, currentbranchid, co.getEmaildate(), user.getUserid());
				this.orderArriveTimeDAO.createOrderArriveTime(orderArriveTime);
				CwbOrderService.logger.info("用户:{},创建库房入库到车时间数据:订单号{}", new Object[] { user.getRealname(), co.getCwb() });
			}

		}

		// 如果订单为出库状态 &&同一个库房进行出库入库
		if ((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getStartbranchid() == currentbranchid)) {
			CwbOrderService.logger.info("重复入库");
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		// added by songkaojun 2016-01-11
		this.updateMPSInfo(co, scancwb, flowOrderTypeEnum, currentbranchid, nextbranchid);
		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate, scancwb, newMPSOrder);
		// ==========结算中转入库扫描逻辑=======
		// 起始站为站点类型
		if ((startbranch.getBranchid() != 0) && (startbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 当前操作站点为中转站点
			if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				// 买单结算
				if (startbranch.getAccounttype() == 1) {
					// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
					if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
							.getValue())) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
						CwbOrderService.logger.info("===开始创建买单结算中转站点入库扫描记录===");
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(),user,currentbranchid);
						accountCwbDetail = this.accountCwbDetailService
								.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(), user.getUserid(), currentbranchid);
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						CwbOrderService.logger
								.info("用户:{},创建结算中转站点入库扫描记录,站点{},入库中转站点{},订单号{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
					}
				}

				// 扣款结算
				if (startbranch.getAccounttype() == 3) {
					CwbOrderService.logger.info("===开始创建扣款结算中转货款数据===");
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), fee, user.getUserid(), "", 0, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户:{},创建扣款结算中转退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
				}
			}
		}

	}

	private ExpressOperationInfo contructParam(User user, String cwb, long currentbranchid) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest ptf = new PjTransportFeedbackRequest();
		// 操作人
		ptf.setOperater(user.getRealname());
		// 操作时间
		ptf.setOperateTime(System.currentTimeMillis());
		// 当前操作站点名
		ptf.setOperateOrg(this.branchDAO.getBranchByBranchid(currentbranchid).getTpsbranchcode());// 操作机构--tps机构编码
		// 操作类型
		// 刘武强修改10.19，应该是对应的进展扫描FeedbackOperateTypeEnum.InboundScan
		ptf.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
		// 运单号/预约单号
		ptf.setTransportNo(cwb);
		paramObj.setTransNoFeedBack(ptf);
		paramObj.setTransNo(cwb);// 运单号
		return paramObj;
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

	public CwbOrder changeintoWarehous(User user, String cwb, String scancwb, long customerid, long driverid, long requestbatchno, String comment, String baleno, boolean anbaochuku, int checktype, long nextbranchid) {
		CwbOrderService.logger.info("开始中转站入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		long branchid = user.getBranchid();
		if ((nextbranchid > 0) && (checktype == 1)) {
			branchid = nextbranchid;
		}
		return this.changeintoWarehousHandle(user, cwb, scancwb, branchid, customerid, driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku, true);
	}

	@Transactional
	public CwbOrder changeintoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long customerid, long driverid, long requestbatchno, String comment, boolean isauto, String baleno, Long credate, boolean anbaochuku, boolean isAutoSupplyLink) {
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.ZhongZhuanZhanRuKu;
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, flowOrderTypeEnum);
		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
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

		if ((((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) && !anbaochuku)) {
			return this
					.handleChangeIntowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, credate, driverid, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, false, credate, anbaochuku, driverid, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleChangeIntowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, Long credate, long driverid, boolean isAutoSupplyLink) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		/*
		 * if ((co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) &&
		 * (co.getNextbranchid() != currentbranchid)) {//如果下一站不指向该中转站，那么不能中转入库
		 * --刘武强 throw new CwbException(cwb, flowOrderTypeEnum.getValue(),
		 * ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO); }
		 */

		if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
			if (co.getScannum() < 1) {
				this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false, driverid, isAutoSupplyLink);
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
			
			/* ***************modify begin*********************/
            // modify by neo01.huang，2016-8-3，改用用中转库专用的校验
            //this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.validateYipiaoduojianStateForZhongZhuanKu(co, flowOrderTypeEnum, isypdjusetranscwb, false);
            /* ***************modify end*********************/
			
			this.handleChangeIntowarehouse(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, isypdjusetranscwb, true, credate, false, driverid, isAutoSupplyLink);
		}
		// 中转站入库一票多件操作===LX
		// =======开始=======
		/*
		 * long nextBranchid = 0; long flowOrderType = co.getFlowordertype(); if
		 * ((co.getCurrentbranchid() != 0)) {//
		 * 如果下一站为0，那么说明它处于数据导入、入库、入站之前，那么这个时候下一站不变
		 * ，从而使得数据导入状态直接入退货入库，未入库、未入站允许进入入库/入站 List<Branch> branchidList =
		 * this.cwbRouteService
		 * .getNextInterceptBranch(co.getCurrentbranchid());//
		 * 根据站点的流向配置，找到他对应的退货组 if ((branchidList.size() > 1) && (flowOrderType
		 * != FlowOrderTypeEnum.DaoRuShuJu.getValue())) {// 如果不等于导入数据，那么说明配置是错的
		 * // throw new Exception("配置的下一站不唯一"); nextBranchid = 0; } else if
		 * ((branchidList.size() == 0) && (flowOrderType !=
		 * FlowOrderTypeEnum.DaoRuShuJu.getValue())) {// 如果不等于导入数据，那么说明配置是错的 //
		 * throw new Exception("没有配置下一站"); nextBranchid = 0; } else {
		 * nextBranchid = branchidList.get(0).getBranchid(); } }
		 */
		Long nextbranchid = co.getNextbranchid();
		if (co.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
			nextbranchid = this.getNextBranchid(cwb, currentbranchid);
		}
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, co.getCurrentbranchid(), nextbranchid);// 更新订单一票多件状态和运单状态
		// =======结束=======
		return this.cwbDAO.getCwbByCwb(cwb);
		// 一票多件中主单的属性根据最后操作的这个运单变化 -----刘武强16.01.23
	}

	private void handleChangeIntowarehouse(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku, long driverid, boolean isAutoSupplyLink) {
		/* ***************modify begin*********************/
		//(原来的代码)commented by neo01.huang，2016-7-27，对于一票多件，需要用运单的状态来做校验
//		this.validateCwbState(co, flowOrderTypeEnum);
//
//		this.validateStateTransfer(co, flowOrderTypeEnum);
		/* ***************modify end*********************/
		
		/* ***************add begin*********************/
		//是否为真正一票多件
		boolean isRealOneVoteMultiPiece = mpsCommonService.isRealOneVoteMultiPiece(co, isypdjusetranscwb);
		if (isRealOneVoteMultiPiece) {
			//通过运单的flowordertype校验订单的操作流程
			mpsOptStateService.validateStateTransferForTranscwb(cwb, scancwb, flowOrderTypeEnum);
			
		} else {
			//走原来的逻辑
			this.validateCwbState(co, flowOrderTypeEnum);
			
			this.validateStateTransfer(co, flowOrderTypeEnum);
			
		}
		/* ***************add end*********************/

		// 对oxo和oxo_jit的订单不用中转审核 by jinghui.pan on 20150806
		if (this.isOxoAndJitType(co) == false) {
			this.validateAppZhongZhuan(cwb, co, flowOrderTypeEnum);// 中转校验
		}
		if (!isauto) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}

		// 自动补充完环节后重新定位当前操作

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		CwbOrderService.logger.info("中转站入库数据批次处理完成");

		this.cwbRouteService.reload();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		CwbOrderService.logger.info("中转站正常入库-保存下一站");
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowOrderTypeEnum.getValue(), co.getCwb());
		co.setNextbranchid(nextbranchid);// 将下一站更新到订单实体上去
		co.setCurrentbranchid(currentbranchid);
		// 将当前站更新到订单实体上去
		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate, scancwb, false);
		/**
		 * 中转入库交接单打印
		 */
		this.backIntoprintDAO.creChangeIntoprint(co, user, driverid, nextbranchid, "", "", "", "", comment);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0, isAutoSupplyLink);
		}

		this.baleDaoHuo(scancwb);

		EmailDate ed = this.emailDateDAO.getEmailDateById(co.getEmaildateid());
		if ((ed != null) && (ed.getState() == 0)) {// 如果批次为未到货 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());

		// 如果订单为出库状态 &&同一个库房进行出库入库
		if (((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue())) && (co.getStartbranchid() == currentbranchid)) {
			CwbOrderService.logger.info("重复入库");
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
					if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
							.getValue())) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
						CwbOrderService.logger.info("===开始创建买单结算中转站点入库扫描记录===");
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(),user,currentbranchid);
						accountCwbDetail = this.accountCwbDetailService
								.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue(), user.getUserid(), currentbranchid);
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						CwbOrderService.logger
								.info("用户:{},创建结算中转站点入库扫描记录,站点{},入库中转站点{},订单号{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
					}
				}

				// 扣款结算
				if (startbranch.getAccounttype() == 3) {
					CwbOrderService.logger.info("===开始创建扣款结算中转货款数据===");
					BigDecimal fee = BigDecimal.ZERO;
					// //上门退订单
					// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
					// fee=co.getPaybackfee();
					// }else{//配送||其他
					fee = co.getReceivablefee();
					// }
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), fee, user.getUserid(), "", 0, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户:{},创建扣款结算中转退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
				}
			}
		}
		// 如果是快递单，则需要发送tps运单轨迹请求
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			this.executeTpsTransInterface(co, user);
		}
	}

	/**
	 * 调用tps运单反馈接口 -- 中转入库
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(CwbOrder order, User user) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo(order.getCwb());
		transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());// tps机构编码
		transNoFeedBack.setOperater(user.getRealname());
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
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
	 * 中转审核相关流程限制，条件：待审核和审核不通过 不允许 进行中转相关操作
	 *
	 * @param cwb
	 * @param co
	 * @param flowOrderTypeEnum
	 */
	private void validateAppZhongZhuan(String cwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {
		int changealowflag = this.getChangealowflagByIdAdd(co);
		if (changealowflag == 1) {
			CwbApplyZhongZhuan cwbApplyZhongZhuan = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanByCwb(cwb);
			//加上非空判断，防止异常产生 --- 刘武强20160612
			if(cwbApplyZhongZhuan != null){
				if (cwbApplyZhongZhuan.getIshandle() == 0) {
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Weishenhebuxuzhongzhuan, flowOrderTypeEnum.getText());
				}
				if (cwbApplyZhongZhuan.getIshandle() == 2) {
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuan, flowOrderTypeEnum.getText());
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
	public void intoAndOutwarehouseYpdjCre(User user, CwbOrder co, String scancwb, long flowordertype, long isypdjusetranscwb, long nextbranchid, boolean isAutoSupplyLink) {
		if (isAutoSupplyLink) {//2016.02.16 一票多件自动补录不写一票多件缺件记录
			// 2013.8.21
			// 临时处理,避免某订单做了入库,但是有缺货,去做出库时,入库里还显示缺货,暂决定删除之前的数据,避免仍然上一操作还有该单缺货的问题
			// commentted by songkaojun 2016-01-28 新一票多件模式支持显示
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
		// 打印日志
		CwbOrderService.logger.info("开始分站到货处理,cwb:{}", cwb);
		// 运单获取订单
		cwb = this.translateCwb(cwb);

		return this.substationGoodsHandle(user, cwb, scancwb, user.getBranchid(), driverid, requestbatchno, comment, false, baleno, System.currentTimeMillis(), anbaochuku, true);
	}

	@Transactional
	public CwbOrder substationGoodsHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long requestbatchno, String comment, boolean isauto, String baleno, Long credate, boolean anbaochuku, boolean isAutoSupplyLink) {

		//
		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}
		// added by songkaojun 2016-01-18 更新订单上一机构，当前机构，下一机构信息
		this.cwbOrderBranchInfoModificationService.modifyBranchInfo(scancwb);
		// 根据当前站点查询list
		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		// 查询有没有该订单
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
//		if(isypdjusetranscwb == 1 && co.getFlowordertype() == ){
			//集包一票多件重设扫描件数
		this.resetScannumForSubStationGoods(user, userbranch, cwb, scancwb);
//		}
		co = this.cwbDAO.getCwbByCwbLock(cwb);
		// added shenhongfei 分站到货验证 2016-1-21
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao);
		/*
		 * if (baleno != null && baleno.length() > 0 &&
		 * !baleno.equals(co.getPackagecode())) { throw new
		 * CwbException(cwb,FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		 * ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI); }
		 */

		

		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());

		if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao;
		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu
				.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan
				.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) && (co.getIsmpsflag() == IsmpsflagEnum.no.getValue())) {
			flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao;
		}

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		}
		if (!anbaochuku) {
			this.checkBaleOfOrder(cwb, scancwb, isypdjusetranscwb, flowOrderTypeEnum);
		}
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()))) {
			return this
					.handleSubstationGoodsYipiaoduojian(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, credate, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
			} else {
				this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, false, credate, anbaochuku, false, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		/*
		 * }else{ throw new
		 * CwbException(cwb,FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao
		 * .getValue(),ExceptionCwbErrorTypeEnum.Invalid_Cwb_State); }
		 */
		// PJ快递单反馈操作流程状态-------LX
		CwbOrderService.logger.info("快递流程状态反馈接口（分站到货反馈）======开始");
		if ((flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())) {
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
			PjTransportFeedbackRequest ptf = new PjTransportFeedbackRequest();
			// 操作人
			ptf.setOperater(user.getRealname());
			// 操作时间
			long dat = System.currentTimeMillis();// 获取当前时间的毫秒数
			ptf.setOperateTime(dat);
			// 获取当前操作站点名
			Branch branch = this.branchDAO.getBranchByBranchid(currentbranchid);
			String tpsbranchcode = branch.getTpsbranchcode();
			ptf.setOperateOrg(tpsbranchcode);// 操作机构
			// 操作类型
			// ptf.setOperateType((int) flowOrderType);
			// 应该是对应出站扫描--刘武强10.19
			ptf.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
			// 运单号/预约单号
			ptf.setTransportNo(cwb);
			paramObj.setTransNoFeedBack(ptf);
			paramObj.setTransNo(cwb);// 运单号
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}
		CwbOrderService.logger.info("快递流程状态反馈接口======结束");

		//add by huangzh 2016-6-27 在站点的扫描、批量、合包到货操作，都需要添加数据到临时表express_ops_tps_flow_tmp
		this.tpsCwbFlowService.save(co, scancwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao,user.getBranchid(),null,true,null,null);
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void resetScannumForSubStationGoods(User user, Branch userbranch, String cwb, String scancwb) {
//		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		int flowOrderType = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
//		long nextbranchid = 0L;
//		if (cwbOrder != null) {
//			nextbranchid = cwbOrder.getNextbranchid();
			//Added by leoliao at 2016-03-30 到货扫描在到错货的情况计算扫描次数需要把flowordertype改为8(到错货)
			//			if ((nextbranchid != 0) && (nextbranchid != userbranch.getBranchid()) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())
			//				&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue())
			//				&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue())
			//				&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())
			//				&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
			//				&& (cwbOrder.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {
			//				flowOrderType = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue();
			//			}
			//Added end
//		}

		//		this.mpsCommonService.resetScannumByTranscwb(scancwb, flowOrderType, user.getBranchid(), nextbranchid);
		//update neo01.huang,2016-4-21
		//重设扫描次数（分站到货扫描）
		this.mpsCommonService.resetScannumByTranscwbForArrive(cwb, scancwb, flowOrderType, user.getBranchid());
	}

	private CwbOrder handleSubstationGoodsYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, Branch userbranch, long isypdjusetranscwb, Long credate, boolean isAutoSupplyLink) {

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}

		//一票多件是否存在运单号已经为站点到货，但不全部为到货的标志
		boolean FenZhanDaoHuoFlag = this.getFenZhanDaoHuoFlag(co,isypdjusetranscwb,flowOrderTypeEnum);
		
		boolean newMPSOrder = this.mpsCommonService.isNewMPSOrder(scancwb);
		// 先更新运单状态，要不然运单流程里面的当前站为0
		this.mpsOptStateService.updateTransCwbDetailInfo(scancwb, flowOrderTypeEnum, -1L, currentbranchid, 0L);

		if (((co.getCurrentbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) || (newMPSOrder && (co.getScannum() > 0))) {
			if (co.getScannum() < 1) {
				this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false, false, isAutoSupplyLink);
			}
			// modify by jian_xie 上边有重复扫描的判断
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
			}
			if (isypdjusetranscwb == 1) {
				if (!newMPSOrder ) {
					this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
				}
				//Commented by leoliao at 2016-03-30
				//this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				//Added by leoliao at 2016-03-30 到货扫描在到错货的情况需要把flowordertype改为8(到错货)，然后删除缺件表记录。
				//					long flowOrderTypeTemp = flowOrderTypeEnum.getValue();
				//					if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid)
				//						&& (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())
				//						&& (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue())
				//						&& (co.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue())
				//						&& (co.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())
				//						&& (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
				//						&& (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {
				//						flowOrderTypeTemp = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue();
				//					}
				//
				//					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeTemp, isypdjusetranscwb, 0);
				//Added end
				//update by neo01.huang，2016-4-21
				//清除缺件记录
				this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				//同时，也要把当前站点的到错货的缺件记录，也一并删除
				this.intoAndOutwarehouseYpdjDel(user, co, scancwb, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), isypdjusetranscwb, 0);

				//清除非本站的缺件记录，add by neo01.huang，2016-6-14
				ypdjHandleRecordDAO.delYpdjHandleRecord(cwb, user.getBranchid());
				
			}
			//集包模式
			if (newMPSOrder) {
				this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false, true, isAutoSupplyLink);
			}
//			} else {
//				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
//			}
		
		}else if(!newMPSOrder && FenZhanDaoHuoFlag){//非集单一票多件中，一件先出库、到货，下一件才出库、到货的情况下，需要对下一件进行到货,并且将这种情况分离出来（不在防止它上面的if中），防止做一些多余的操作 ，譬如多写入一条运单轨迹 ---刘武强 20160802
			this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false, false, isAutoSupplyLink);
			this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
			this.intoAndOutwarehouseYpdjDel(user, co, scancwb, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), isypdjusetranscwb, 0);
			ypdjHandleRecordDAO.delYpdjHandleRecord(cwb, user.getBranchid());
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleSubstationGoods(user, cwb, scancwb, currentbranchid, requestbatchno, comment, isauto, co, flowOrderTypeEnum, userbranch, isypdjusetranscwb, true, credate, false, false, isAutoSupplyLink);
		}
		//add by huangzh 2016-6-27 在站点的扫描、批量、合包到货操作，都需要添加数据到临时表express_ops_tps_flow_tmp
		this.tpsCwbFlowService.save(co, scancwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao,user.getBranchid(),null,true,null,null);
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private boolean isIntercepted(CwbOrder co) {
		return (co.getCwbstate() == CwbStateEnum.BUFENDIUSHI.getValue()) || (CwbStateEnum.BUFENPOSUN.getValue() == co.getCwbstate()) || (CwbStateEnum.WANQUANPOSUN.getValue() == co.getCwbstate()) || (CwbStateEnum.DiuShi
				.getValue() == co.getCwbstate()) || (CwbStateEnum.TuiHuo.getValue() == co.getCwbstate());
	}

	/**
	 * 判断，订单类型是否为‘上门退’，且FlowOrderTypeEnum为"分站到货扫描"或"到错货"
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private boolean isShangMenTuiTypeAndBranchScan(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {
		int cwbOrderTypeId = co.getCwbordertypeid();
		return ((cwbOrderTypeId == CwbOrderTypeIdEnum.Shangmentui.getValue()) && ((flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao) || (flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao)));
	}

	/**
	 * 判断，订单类型是否为‘OXO’ 或 'OXO_JIT'，且FlowOrderTypeEnum为"分站到货扫描"或"到错货"
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private boolean isOxoAndJitTypeAndBranchScan(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {
		return (this.isOxoAndJitType(co) && ((flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao) || (flowOrderTypeEnum == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao)));
	}

	/**
	 * 判断，订单类型是否为‘OXO’ 或 'OXO_JIT'
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private boolean isOxoAndJitType(CwbOrder co) {
		return (this.isOxoType(co) || this.isJitType(co));
	}

	/**
	 * 判断，订单类型是否为 'OXO_JIT'
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private boolean isJitType(CwbOrder co) {
		int cwbOrderTypeId = co.getCwbordertypeid();
		return (CwbOrderTypeIdEnum.OXO_JIT.getValue() == cwbOrderTypeId);
	}

	/**
	 * 判断，订单类型是否为 'OXO'
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private boolean isOxoType(CwbOrder co) {
		int cwbOrderTypeId = co.getCwbordertypeid();
		return (CwbOrderTypeIdEnum.OXO.getValue() == cwbOrderTypeId);
	}

	private void handleSubstationGoods(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, Branch userbranch, long isypdjusetranscwb, boolean isypdj, Long credate, boolean anbaochuku, boolean newMPSOrder, boolean isAutoSupplyLink) {

		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		if (!isauto && !((this.isShangMenTuiTypeAndBranchScan(co, flowOrderTypeEnum)) || this.isOxoAndJitTypeAndBranchScan(co, flowOrderTypeEnum))) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, flowOrderTypeEnum.getValue(), co, requestbatchno, scancwb, false);
		}
		// end

		// 自动补充完环节后重新定位当前操作
		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}

		CwbOrderService.logger.info("分站到货数据批次处理完成");

		long accountFlowOrderType = co.getFlowordertype();

		flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao;

		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu
				.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuo.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoChuZhan
				.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) {

			flowOrderTypeEnum = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao;

		}

		/**
		 * add by 王志宇
		 *
		 * 判断是否为二级站出给一级站的的快递订单 如果是则将订单操作类型改为揽件入站，而非站点到货，并且将此订单的下一站改为0；
		 */
		// ========================================add
		// begin============================================================
		// 判断是够为快递单
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			Branch branchStart = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
			// 判断是否为二级站或者为空（为空证明上一站刚刚揽件）      |||| 并且当前状态为运单录入，才能说明是揽件入站（刘武强 2016.06.08）
			if (((branchStart.getContractflag() == null) || (Integer.parseInt(branchStart.getContractflag()) == BranchTypeEnum.ErJiZhan.getValue())) && (co.getFlowordertype() == FlowOrderTypeEnum.YunDanLuRu.getValue())) {
				flowOrderTypeEnum = FlowOrderTypeEnum.LanJianRuZhan;
				// 将此订单的下一站改为0
				String sqlstr = "update express_ops_cwb_detail set nextbranchid=? where cwb=? and state=1";
				this.jdbcTemplate.update(sqlstr, 0, co.getCwb());

			}
		}
		// ========================================add
		// end==========================================================

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

		// 修复oxo订单，一票多件到错货的缺陷.
		// TODO:但是这里有个问题，就是不同站点扫描不同运单，导致nextbranchid反复变化，
		if (this.isOxoType(co)) {
			String sqlstr = "update express_ops_cwb_detail set nextbranchid=? where cwb=? and state=1";
			this.jdbcTemplate.update(sqlstr, currentbranchid, co.getCwb());
		}

		// ======按包到货时更新扫描件数为发货件数zs=====
		if (!newMPSOrder) {
			if (!anbaochuku) {
				this.cwbDAO.updateScannum(co.getCwb(), 1);
			} else {
				this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
			}
			this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate, scancwb, newMPSOrder);
		}

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			if (!newMPSOrder) {
				this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0, isAutoSupplyLink);
			} else {
				//update by neo01.huang，2016-4-29
				//1票3件或以上，扫描次数大于或等1，需要执行缺件检测
				//原因是：由于其他步骤执行了缺件全量删除，导致本站之前创建的缺件记录都丢失了
				//在这里需要补回缺件记录
				if ((co.getSendcarnum() >= 3) && (co.getScannum() >= 1)) {
					//补充一票多件记录
					this.completeYpdjRecord(user, co, scancwb, flowOrderTypeEnum, isypdjusetranscwb, isAutoSupplyLink);
				}
			}
		}

		// added shenhongfei 分站到货状态修改 2016.1.12
		Long NextBranchid = co.getNextbranchid();
		if (this.isIntercepted(co)) {
			NextBranchid = this.getNextBranchid(cwb, currentbranchid);
		}

		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, -1L, currentbranchid, NextBranchid);

		this.baleDaoHuo_fzdh(scancwb);
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
					CwbOrderService.logger.info("===开始创建扣款结算到错货处理出库记录===");
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
					accountDeductRecord = this.accountDeductRecordService
							.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货处理出库", co
									.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, user.getUserid(), "到错货处理出库", recordid, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户:{},创建扣款结算到错货处理出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), koukuan });
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
					CwbOrderService.logger.info("===开始站点到货扣款(伪扣款)===");
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
							CwbOrderService.logger.info("用户{},对{}站点进行站点到货伪扣款：原伪余额{}元，原伪欠款{}元。扣款{}后，伪余额{}元，伪 欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), branchLock
									.getBalancevirt(), branchLock.getDebtvirt(), koukuan, balance, debt });
						}
						CwbOrderService.logger.info("===伪扣款结束===");
					}
				}
			}
		}

		// ==== 扫描成功后，更新配送状态为‘处理中=====
		this.updateOXODeliveryState(co, CwbOXOStateEnum.Processing);
	}
	/**
	 * 补充一票多件记录
	 * @param user 当前user
	 * @param co 订单
	 * @param scancwb 当前扫描的运单号
	 * @param flowOrderTypeEnum 当前运单操作类型
	 * @param isypdjusetranscwb 是否为1票多件订单
	 * @param isAutoSupplyLink 是否自动补流程
	 */
	@Transactional
	public void completeYpdjRecord(User user, CwbOrder co, String scancwb, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isAutoSupplyLink) {
		this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0, isAutoSupplyLink);
		//通过订单号获取所有运单
		List<TransCwbDetail> transCwbDetailList = this.transCwbDetailDAO.getTransCwbDetailListByCwb(co.getCwb());
		if ((transCwbDetailList != null) && (transCwbDetailList.size() != 0)) {
			//检测哪些运单在当前站，如果该运单在当前站，则需要把该运单的缺件记录去掉
			for (TransCwbDetail transCwbDetail : transCwbDetailList) {
				if (transCwbDetail == null) {
					continue;
				}
				//如果是当前运单，则skip
				if (scancwb.equals(transCwbDetail.getTranscwb())) {
					continue;
				}
				//如果该运单在当前站，则去掉该运单的缺件记录
				if (user.getBranchid() == transCwbDetail.getCurrentbranchid()) {
					this.intoAndOutwarehouseYpdjDel(user, co, transCwbDetail.getTranscwb(), flowOrderTypeEnum.getValue(), isypdjusetranscwb, 0);
				}
			}
		}
	}

	/**
	 * 更新OXO订单的配送状态
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private void updateOXODeliveryState(CwbOrder co, CwbOXOStateEnum oxoStateEnum) {
		if (this.isOxoAndJitType(co)) {
			this.cwbDAO.updateOXODeliveryState(oxoStateEnum.getValue(), co.getCwb());
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
		CwbOrderService.logger.info("开始退货站入库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		long branchid = user.getBranchid();
		if ((nextbranchid > 0) && (checktype == 1)) {
			branchid = nextbranchid;
		}
		return this.backIntoWarehousHandle(user, cwb, scancwb, branchid, driverid, requestbatchno, comment, anbaochuku);
	}

	@Transactional
	private CwbOrder backIntoWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long requestbatchno, String comment, boolean anbaochuku) {
		// added shenhongfei 退货站入库 2016-1-12
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.TuiHuoZhanRuKu);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		// added by songkaojun 2016-01-25 开启新一票多件模式，不校验中转货
		if (!this.mpsCommonService.isNewMPSOrder(scancwb)) {
			if ((cwbBranch.getBranchid() != currentbranchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
			}
		}

		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.TuiHuoZhanRuKu;

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) && !anbaochuku) {
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

	/**
	 * 创建退货再投申请记录
	 *
	 * @param cwb
	 * @param co
	 */
	private void createTuihuoZaiTouRecord(String cwb, CwbOrder co) {
		// 查询是否在退货入库表中有记录
		OrderBackRuku obrobj = this.orderBackRukuRecordDao.getBackrukuRecord(cwb);
		if (obrobj == null) {
			// 有效订单存入退货站入库记录表
			OrderBackRuku obr = new OrderBackRuku();
			obr.setCwb(co.getCwb());
			obr.setCustomerid(co.getCustomerid());// 供货商id
			obr.setBranchid(co.getDeliverybranchid());// 上一站（配送站）
														// 当前站为退货站(配送到达的站点)
			obr.setCwbordertypeid(co.getCwbordertypeid());// 订单类型
			obr.setConsigneename(co.getConsigneename());// 收件人名字
			obr.setConsigneeaddress(co.getConsigneeaddress());// 收件人地址
			obr.setCreatetime(this.getNowtime());// 当前时间
			obr.setCwbstate(2);
			this.orderBackRukuRecordDao.creOrderbackRuku(obr);// 导入到退货站入库记录表
		}
	}

	public String getNowtime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	private CwbOrder handleBackIntoWarehousYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long driverid) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), currentbranchid, 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}
		if (co.getFlowordertype() == flowOrderTypeEnum.getValue()) {
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
			//针对一票多件，如果所有的运单号退货出站已经全部完成，但订单主表发货件数与扫描件数不一致时(异常)，进行补全扫描件数，避免退货入库时报错
			long realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowCount(co.getCwb(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), currentbranchid);
			if ((co.getSendcarnum() == realscannum) && (co.getSendcarnum() > co.getScannum())) {
				co.setScannum(realscannum);
			}
			//针对一票多件，如果订单被拦截了，那么补齐扫描件数（拦截只需要扫一个运单即可完成，那么拦截完成是扫描件书很可能就小于发货件数，所以补齐）---lwq
			if (co.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
				co.setScannum(co.getSendcarnum());
			}
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleBackIntoWarehous(user, cwb, scancwb, currentbranchid, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false, driverid);
		}

		// // added shenhongfei 退货站入库 2016-1-12
		// long nextbranchid =
		// this.cwbRouteService.getNextBranch(currentbranchid,
		// co.getDeliverybranchid());
		// if (co.getFlowordertype() ==
		// FlowOrderTypeEnum.DingDanLanJie.getValue()) {
		// nextbranchid = co.getDeliverybranchid();
		// }

		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.TuiHuoZhanRuKu, co.getStartbranchid(), currentbranchid, co.getNextbranchid());
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleBackIntoWarehous(User user, String cwb, String scancwb, long currentbranchid, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean anbaochuku, long driverid) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		this.validateTuihuoCheck(cwb, flowOrderTypeEnum);// 退货申请校验

		if (requestbatchno > 0) {
			this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), (int) driverid, 0, "");
		}

		CwbOrderService.logger.info("退货站入库数据批次处理完成");

		int flowordertype = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();
		long nextbranchid = this.cwbRouteService.getNextBranch(currentbranchid, co.getDeliverybranchid());
		CwbOrderService.logger.info("退货站正常入库-保存下一站");
		if (co.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
			nextbranchid = co.getDeliverybranchid();
			currentbranchid = user.getBranchid();
		}
		this.cwbDAO.updateNextBranchid(cwb, nextbranchid);

		String sql = "update express_ops_cwb_detail set currentbranchid=?,flowordertype=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, currentbranchid, flowordertype, co.getCwb());
		if ((co.getCwbstate() == CwbStateEnum.TuiGongYingShang.getValue()) && (co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue())) {
			this.cwbDAO.updateCwbState(cwb, CwbStateEnum.TuiHuo);
		}
		co.setCurrentbranchid(currentbranchid);
		co.setNextbranchid(nextbranchid);
		// added shenhongfei 退货站入库 2016-1-12z
		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.TuiHuoZhanRuKu, co.getStartbranchid(), currentbranchid, nextbranchid);
		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			//原逻辑扫描数写死为1，需要改成动态计算
			//this.cwbDAO.updateScannum(co.getCwb(), 1);
			
			// update by neo01.huang，2016-7-6，动态计算扫描数
			int realscannum = transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(null, cwb, flowordertype, user.getBranchid());
			logger.info("退货库入库->原扫描数:{}", realscannum);
			this.cwbDAO.updateScannum(co.getCwb(), realscannum + 1);
			
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}
		// 在退货库入库的时候将express_ops_orderback_record表（退供货商确认表）中的记录删掉，防止两次退供应商出库造成退供应商确认界面有两条待确认记录--刘武强2016.06.08
		this.orderbackRecordDao.deleteByCwb(co.getCwb());
		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, System.currentTimeMillis(), scancwb, false);

		this.createTuihuoZaiTouRecord(cwb, co); // 创建退货再投申请记录
		/**
		 * 退货入库交接单打印
		 */
		this.backIntoprintDAO.creBackIntoprint(co, user, driverid, nextbranchid, "", "", "", "", comment);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
		//将批次信息修改的方法加上判空（和其他功能一样），防止出现批次表记录被归档了，然后退货库入库就报错---刘武强20160627
		EmailDate ed = this.emailDateDAO.getEmailDateById(co.getEmaildateid());
		if ((ed != null) && (ed.getState() == 0)) {// 如果批次为未到货 变更为已到货
			this.emailDateDAO.saveEmailDateToEmailDate(co.getEmaildateid());
		}

		// ==========结算退货入库扫描逻辑=======
		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch startbranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
		// 如果订单为出库状态&&上一个入库站为当前操作站
		if ((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getStartbranchid() == currentbranchid)) {
			CwbOrderService.logger.info("重复入库");
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU);
		}

		// 起始站为站点类型
		if ((startbranch.getBranchid() != 0) && (startbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
			// 买单结算
			if (startbranch.getAccounttype() == 1) {
				// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入退货记录
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					CwbOrderService.logger.info("===开始创建买单结算退货站点入库扫描记录===");
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue(),user,currentbranchid);
					accountCwbDetail = this.accountCwbDetailService
							.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue(), user.getUserid(), currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					CwbOrderService.logger.info("用户:{},创建结算退货入库扫描记录,退货站点{},退货站点{},订单号:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
			}

			// 扣款结算
			if (startbranch.getAccounttype() == 3) {
				CwbOrderService.logger.info("===开始创建扣款结算退货站点数据===");
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
				accountDeducDetail = this.accountDeducDetailService
						.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.TuiHuo.getValue(), fee, user.getUserid(), "", 0, 0);
				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				CwbOrderService.logger.info("用户:{},创建扣款结算退货站点退货：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
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

	public void validateCwbState(CwbOrder co, FlowOrderTypeEnum flowordertype) {
		// 在数据库增加一个状态和操作的对应表，只有有记录的才允许操作
		CwbStateEnum cwbstate = CwbStateEnum.getByValue((int) co.getCwbstate());

		if (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(cwbstate.getValue(), flowordertype.getValue()) == null) {
			throw new CwbException(co.getCwb(), flowordertype.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, cwbstate.getText(), flowordertype.getText());
		}
		this.validateDeliveryState(co, flowordertype);
	}

	public void validateDeliveryState(CwbOrder co, FlowOrderTypeEnum flowordertype) {
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
	public void createFloworder(User user, long branchid, CwbOrder co, FlowOrderTypeEnum flowordertype, String comment, Long credate, String scancwb, boolean isNewMPSOrder) {

		if (!isNewMPSOrder) {
			this.createFloworder(user, branchid, co.getCwb(), flowordertype, comment, credate, scancwb);
		} else {
			this.appendCreateFlowOrderJMS(user, branchid, co.getCwb(), flowordertype, comment, credate, scancwb);
		}

	}

	public void createFloworder(User user, long branchid, CwbOrder co, FlowOrderTypeEnum flowordertype, String comment, Long credate) {
		this.createFloworder(user, branchid, co.getCwb(), flowordertype, comment, credate, null);
	}

	public void baleDaoHuo(String scancwb) {
		baleService.disableBale(scancwb);
	}

	/**
	 * modify by jian_xie
	 * 修改成通过id更新包状态
	 * @param scancwb
	 */
	public void baleDaoHuo_fzdh(String scancwb) {
		baleService.disableBale(scancwb);
	}

	private ObjectMapper om = JacksonMapper.getInstance();

	private void appendCreateFlowOrderJMS(User user, long branchid, String cwb, FlowOrderTypeEnum flowordertype, String comment, Long credate, String scancwb) {

		try {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			cwbOrder.setConsigneemobile(cwbOrder.getConsigneemobileOfkf());
			cwbOrder.setConsigneename(cwbOrder.getConsigneenameOfkf());
			cwbOrder.setConsigneephone(cwbOrder.getConsigneephoneOfkf());
			DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
			cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
			cwbOrderWithDeliveryState.setDeliveryState(deliveryState);

			OrderFlow of = new OrderFlow(0, cwb, branchid, new Timestamp(credate), user.getUserid(), JacksonMapper.getInstance().writeValueAsString(cwbOrderWithDeliveryState), flowordertype.getValue(), comment);

			if (cwbOrder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
				TransCwbDetail transCwbDetail = this.transCwbDetailDAO.findTransCwbDetailByTransCwb(scancwb);
				if ((transCwbDetail != null) && (transCwbDetail.getTranscwboptstate() == cwbOrder.getMpsoptstate())) {
					this.send(of);
				}
			}

		} catch (Exception e1) {
			CwbOrderService.logger.error("createFlowOrderAndResendJMS error", e1);
		}

	}

	private void createFloworder(User user, long branchid, String cwb, FlowOrderTypeEnum flowordertype, String comment, Long credate, String scancwb) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		cwbOrder.setConsigneemobile(cwbOrder.getConsigneemobileOfkf());
		cwbOrder.setConsigneename(cwbOrder.getConsigneenameOfkf());
		cwbOrder.setConsigneephone(cwbOrder.getConsigneephoneOfkf());
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
		cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
		cwbOrderWithDeliveryState.setDeliveryState(deliveryState);
		Customer customer = this.customerDao.getCustomerById(cwbOrder.getCustomerid());
		try {
			if ((customer.getIsypdjusetranscwb() == 1) && ((flowordertype.getValue() == FlowOrderTypeEnum.YiFanKui.getValue()) || (flowordertype.getValue() == FlowOrderTypeEnum.YiShenHe.getValue()))) {
				this.fankuiAddTranscwbFlow(cwb, cwbOrder, user, flowordertype, deliveryState.getDeliverystate());
				cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
			}

			OrderFlow of = new OrderFlow(0, cwb, branchid, new Timestamp(credate), user.getUserid(), this.om.writeValueAsString(cwbOrderWithDeliveryState).toString(), flowordertype.getValue(), comment);
			long floworderid = this.orderFlowDAO.creAndUpdateOrderFlow(of);
			if (!((customer.getIsypdjusetranscwb() == 1) && (cwbOrder.getSendcarnum() > 1))) {
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, flowordertype.getValue(), "", user.getBranchid(), user.getUserid(), cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			}

			try {
				this.orderFlowLogDAO.creAndUpdateOrderFlow(of, floworderid);
			} catch (Exception e) {
				CwbOrderService.logger.info("存储flow日志异常，订单号：{}", cwb);
				CwbOrderService.logger.error("", e);
			}
			this.updateOrInsertWareHouseToBranch(cwbOrder, of);
			this.updateOutToCommen(cwbOrder, of, 0); // 出库 承运商库房
			this.updateOutToCommen_toTwoLeavelBranch(cwbOrder, of, 1); // 一级站出库二级站

			// 一票多件集包模式，并且扫描是最后一件，才会调用JMS发送给oms
			this.send(of);

			// 创建退货中心出入库跟踪表
			if ((flowordertype.getValue() != FlowOrderTypeEnum.YunDanLuRu.getValue()) && (flowordertype.getValue() != FlowOrderTypeEnum.TiHuo.getValue()) && (flowordertype.getValue() != FlowOrderTypeEnum.TiHuoYouHuoWuDan
					.getValue()) && (flowordertype.getValue() != FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())) {
				CwbOrderService.logger.warn("订单号{}订单当前状态为{}，创建退货中心出入库跟踪表", cwbOrder.getCwb(), flowordertype.getValue());
				this.backDetailService.createBackDetail(user, cwb, flowordertype.getValue(), credate);
			}

		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing orderflow", e);
			throw new ExplinkException(ExceptionCwbErrorTypeEnum.SYS_ERROR, cwb);
		}
	}

	public void updateOrInsertWareHouseToBranch(CwbOrder cwbOrder, OrderFlow of) {

		try {
			if ((of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {// 出库状态
				CwbOrderService.logger.info("存入出库记录表 订单号:{},存入的环节:{}", of.getCwb(), of.getFlowordertypeText());
				Branch b = this.branchDAO.getBranchByBranchid(of.getBranchid());
				long count = this.warehouseToBranchDAO.getcwb(cwbOrder.getCwb(), of.getBranchid());
				if (count > 0) {
					this.warehouseToBranchDAO.updateWarehouseToBranch(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of
							.getCredate()), b.getSitetype());
					CwbOrderService.logger.info("存入出库记录表 订单号:{},存入的环节:{},更新成功", of.getCwb(), of.getFlowordertypeText());
				} else {
					this.warehouseToBranchDAO
							.creWarehouseToBranch(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()), b.getSitetype());
					CwbOrderService.logger.info("存入出库记录表 订单号:{},存入的环节:{},插入成功", of.getCwb(), of.getFlowordertypeText());
				}
			}
		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing warehouseToBranch", e);
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
			if ((of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao
					.getValue())) {// 出库状态或中转站出库或上门退订单自动领货
				if (cwbOrder.getCwbstate() != CwbStateEnum.PeiShong.getValue()) {
					CwbOrderService.logger.warn("订单号{}订单当前状态为{}，不能参与发送至承运商", cwbOrder.getCwb(), cwbOrder.getCwbstate());
					return;
				}

				long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());// 查询是否存在

				List<Common> commlist = this.commonDAO.getCommonByBranchid(cwbOrder.getNextbranchid());
				if ((commlist == null) || (commlist.size() == 0)) {
					if (count > 0) {
						this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
						CwbOrderService.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
					}
					return;
				}
				CwbOrderService.logger.info("存入出库给承运商表  订单号:{}", of.getCwb());
				String commencode = commlist.get(0).getCommonnumber();

				if (count > 0) {
					this.warehouseToCommenDAO.updateWarehouseToCommen(cwbOrder.getCwb(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(of.getCredate()));
					CwbOrderService.logger.info("存入出库给承运商表 订单号:{},更新成功", of.getCwb());
				} else {
					this.warehouseToCommenDAO
							.creWarehouseToCommen(cwbOrder.getCwb(), cwbOrder.getCustomerid(), of.getBranchid(), cwbOrder.getNextbranchid(), commencode, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.format(of.getCredate()), "", outbranchflag);
					CwbOrderService.logger.info("存入出库给承运商表 订单号:{},插入成功", of.getCwb());
				}
			}
		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing warehouseToBranch", e);
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
					CwbOrderService.logger.warn("订单号{}订单当前状态为{}，不能参与发送至承运商", cwbOrder.getCwb(), cwbOrder.getCwbstate());
					return;
				}

				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				if (!"2".equals(branch.getContractflag())) { // 如果不是二级站，则return
					return;
				}

				if (branch.getBindmsksid() > 0) { // 绑定了二级站，则需要
					Stores stores = this.storesDAO.getMaisiBranchById(branch.getBindmsksid());
					if (stores == null) {
						CwbOrderService.logger.warn("该迈思可站点被删除，Bindmsksid=" + branch.getBindmsksid());
						return;
					}

					Common common = this.commonDAO.getCommonByCommonnumber(stores.getB2cenum());

					CwbOrderService.logger.info("存入出库给承运商表  订单号:{}", of.getCwb());
					String commencode = common.getCommonnumber();
					long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());
					if (count > 0) {
						this.warehouseToCommenDAO.updateWarehouseToCommen(cwbOrder.getCwb(), of.getBranchid(), branch.getBranchid(), commencode, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of
								.getCredate()));
						CwbOrderService.logger.info("存入出库给承运商表 订单号:{},更新成功", of.getCwb());
					} else {
						this.warehouseToCommenDAO
								.creWarehouseToCommen(cwbOrder.getCwb(), cwbOrder.getCustomerid(), of.getBranchid(), branch.getBranchid(), commencode, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(of.getCredate()), "", outbranchflag);
						CwbOrderService.logger.info("存入出库给承运商表 订单号:{},插入成功", of.getCwb());
					}
				} else {
					long count = this.warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());
					if (count > 0) {
						this.warehouseToCommenDAO.deleteCommonBycwb(cwbOrder.getCwb());
						CwbOrderService.logger.info("承运商出库表已存在该订单={},删除记录", cwbOrder.getCwb());
					}
				}

			}
		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing warehouseToBranch", e);
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
			CwbOrderService.logger.error("error while saveing warehouseToBranch", e);
		}
	}

	private void createTranscwbOrderFlow(User user, long branchid, String cwb, String scancwb, FlowOrderTypeEnum flowOrdertype, String comment) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
		cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
		cwbOrderWithDeliveryState.setDeliveryState(deliveryState);
		this.additionalTransCwbOrderFlow(scancwb, cwbOrderWithDeliveryState); // 追加运单号明细流程
		try {
			TranscwbOrderFlow tof = new TranscwbOrderFlow(0, cwb, scancwb, branchid, new Timestamp(System.currentTimeMillis()), user.getUserid(), this.om.writeValueAsString(cwbOrderWithDeliveryState)
					.toString(), flowOrdertype.getValue(), comment);
			this.transcwborderFlowDAO.creAndUpdateTranscwbOrderFlow(tof);
			if ((FlowOrderTypeEnum.YiFanKui.getValue() == flowOrdertype.getValue()) || (FlowOrderTypeEnum.YiShenHe.getValue() == flowOrdertype.getValue())) {
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, flowOrdertype.getValue(), "", user.getBranchid(), user.getUserid(), 0, 0, 0, 0, "", cwb);
			} else {

				this.exceptionCwbDAO.createExceptionCwbScan(cwb, flowOrdertype.getValue(), "", user.getBranchid(), user.getUserid(), 0, 0, 0, 0, "", scancwb);
			}
			if (cwbOrder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
				this.sendTranscwbOrderFlow(tof);
				// if(cwbOrder.getScannum()>1){ //一票多件，扫描第二件主动调用订单表jms
				// ，判断为了跟第一次不冲突。
				// // OrderFlow of = new OrderFlow(0, cwb, branchid, new
				// Timestamp(credate), user.getUserid(),
				// this.om.writeValueAsString(cwbOrderWithDeliveryState).toString(),
				// flowOrdertype.getValue(), comment);
				// // this.send(null);
				// }
			}

		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing orderflow", e);
			throw new ExplinkException(ExceptionCwbErrorTypeEnum.SYS_ERROR, cwb);
		}
	}

	/**
	 * 创建运单号detail 流程
	 *
	 * @param scancwb
	 * @param cwbOrderWithDeliveryState
	 */
	private void additionalTransCwbOrderFlow(String scancwb, CwbOrderWithDeliveryState cwbOrderWithDeliveryState) {
		TransCwbDetail transCwbDetail = this.transCwbDetailDAO.findTransCwbDetailByTransCwb(scancwb);
		if (transCwbDetail != null) {
			cwbOrderWithDeliveryState.setTransCwbDetail(transCwbDetail); // 运单号主表
		}
	}

	private CwbOrder createCwbDetail(User user, long customerid, String cwb) {
		try {
			String sql = "insert into express_ops_cwb_detail (cwb,currentbranchid,customerid,emailfinishflag,cwbordertypeid,cwbstate,credate) values(?,?,?,?,?,?,?)";
			this.jdbcTemplate
					.update(sql, cwb, user.getBranchid(), customerid, EmailFinishFlagEnum.YouHuoWuDan.getValue(), CwbOrderTypeIdEnum.Peisong.getValue(), CwbStateEnum.WUShuju.getValue(), Timestamp
							.valueOf(DateTimeUtil.getNowTime()));

			return this.cwbDAO.getCwbByCwb(cwb);
		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing cwbdetail", e);
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
				String header = this.om.writeValueAsString(of);
				CwbOrderService.logger.info("消息发送端：orderFlowProducerTemplate, orderFlow={}", header);
				this.orderFlowProducerTemplate.sendBodyAndHeader(null, "orderFlow", header);
			} catch (Exception ee) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {// 导入数据的话，手工调用保存订单号和运单号的表
					CwbOrderService.logger.info("调接口执行运单号保存 单号：{}", of.getCwb());
					this.transCwbService.saveTransCwbByFloworderdetail(of.getFloworderdetail());
				}
				// jms异常写入监控表
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

				//写MQ异常表
				try {
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".send").buildExceptionInfo(ee.toString())
							.buildTopic(this.orderFlowProducerTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("orderFlow", this.om.writeValueAsString(of)).getMqException());
				} catch (IOException e) {
					CwbOrderService.logger.error("转换异常", e);
				}
				CwbOrderService.logger.error("send flow message error", ee);
			}
		}
	}

	public int resend(OrderFlow of) {
		try {
			String header = this.om.writeValueAsString(of);
			CwbOrderService.logger.info("消息发送端：orderFlowProducerTemplate, orderFlow={}", header);
			this.orderFlowProducerTemplate.sendBodyAndHeader(null, "orderFlow", header);
			return 1;
		} catch (Exception ee) {
			if (of.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {// 导入数据的话，手工调用保存订单号和运单号的表
				CwbOrderService.logger.info("调接口执行运单号保存 单号：{}", of.getCwb());
				this.transCwbService.saveTransCwbByFloworderdetail(of.getFloworderdetail());
			}
			CwbOrderService.logger.info("重发JMS异常：{}", of.getCwb());
			this.flowExpDao.createFlowExp(of.getCwb(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			CwbOrderService.logger.error("resend flow message error", ee);

			//写MQ异常表
			try {
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".appendCreateFlowOrderJMS").buildExceptionInfo(ee.toString())
						.buildTopic(this.orderFlowProducerTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("orderFlow", this.om.writeValueAsString(of)).getMqException());
			} catch (IOException e) {
				CwbOrderService.logger.error("转化异常", e);
			}
			return 0;
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

	/**
	 * 发送运单推送JMS
	 *
	 * @param tof
	 */
	public void sendTranscwbOrderFlow(TranscwbOrderFlow tof) {
		try {
			String header = JacksonMapper.getInstance().writeValueAsString(tof);
			CwbOrderService.logger.info("消息发送端：transCwbOrderFlowProducerTemplate, transCwbOrderFlow={}", header);
			this.transCwbOrderFlowProducerTemplate.sendBodyAndHeader(null, "transCwbOrderFlow", header);
		} catch (Exception ee) {
			CwbOrderService.logger.error("send transCwbOrderFlow message error,scancwb=" + tof.getScancwb(), ee);
			//写MQ异常表
			try {
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".sendTranscwbOrderFlow").buildExceptionInfo(ee.toString())
						.buildTopic(this.transCwbOrderFlowProducerTemplate.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeader("transCwbOrderFlow", JacksonMapper.getInstance().writeValueAsString(tof)).getMqException());
			} catch (IOException e) {
				CwbOrderService.logger.error("转换异常", e);
			}
		}
	}

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
	public CwbOrder changeoutWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, long reasonid, boolean iszhongzhuanout, boolean anbaochuku) {

		CwbOrderService.logger.info("开始中转站出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);
		return this
				.changeOutWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, iszhongzhuanout, System
						.currentTimeMillis(), anbaochuku, true);
	}

	@Transactional
	public CwbOrder changeOutWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, boolean iszhongzhuanout, Long credate, boolean anbaochuku, boolean checkUserBranchZhongZhuang, boolean isAutoSupplyLink) {
		// 如果被拦截，则给出提示
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu);

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

		logger.info("changeOutWarehousHandle->订单信息：cwb:{}, scancwb:{}, sendcarnum:{}, flowordertype:{}, deliverystate:{}, currentbranchid:{}, userBranchId:{}", 
				cwb, scancwb, co.getSendcarnum(), co.getFlowordertype(), co.getDeliverystate(), co.getCurrentbranchid(), currentbranchid);
		if (co.getSendcarnum() <= 1 || 
				org.apache.commons.lang3.StringUtils.isEmpty(org.apache.commons.lang3.StringUtils.trimToEmpty(co.getTranscwb()))) { //非一票多件
			if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
					.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid)) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
			}
		} else { //一票多件
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cwb", co.getCwb());
			paramMap.put("scancwb", scancwb);
			paramMap.put("isnow", 1);
			List<TranscwbOrderFlow> list = transcwborderFlowDAO.queryTranscwbOrderFlow(paramMap, null);
			if (CollectionUtils.isNotEmpty(list)) {
				TranscwbOrderFlow transcwbOrderFlow = list.get(0);
				logger.info("changeOutWarehousHandle->运单信息：scancwb:{}, flowordertype:{}, branchid:{}",
						transcwbOrderFlow.getScancwb(), transcwbOrderFlow.getFlowordertype(), transcwbOrderFlow.getBranchid());
				if ( (transcwbOrderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || 
						transcwbOrderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() ||
						(co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) ) && 
						transcwbOrderFlow.getBranchid() != currentbranchid) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
				}
			}
			
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());

		// 添加是否验证当前用户站点为【中转站】验证的控制：by jinghui.pan on 20151025
		if (checkUserBranchZhongZhuang && (cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan
				.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		// =====加入按包出库标识 zs=====
		if ((((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) && !anbaochuku)) {
			return this
					.handleChangeOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.ZhongZhuanZhanChuKu, isypdjusetranscwb, iszhongzhuanout, aflag, credate, userbranch, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid)) && (co
					.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.ZhongZhuanZhanChuKu, isypdjusetranscwb, false, iszhongzhuanout, aflag, credate, anbaochuku, userbranch, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// 原包号处理
		// disposePackageCode(packagecode, scancwb, user, co);

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleChangeOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean iszhongzhuanout, boolean aflag, Long credate, Branch userbranch, boolean isAutoSupplyLink) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, userbranch, isAutoSupplyLink);
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

				if ((userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) { // 如果当前站是中转站
					// added by songkaojun 2016-01-28 一票多件模式，要更新运单状态为中转
					if (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
						this.transCwbDao.updateTransCwbState(scancwb, TransCwbStateEnum.PEISONG);
					}
				}

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
			this.handleChangeOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, userbranch, isAutoSupplyLink);
		}
		// 一票多件主单的部分属性（状态、操作状态、下一站）根据最后操作的运单变化
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, 0, co.getNextbranchid());// 更新订单一票多件状态和运单状态

		// //包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束
		// 一票多件（新增业务处理）更新==LX==

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleChangeOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean iszhongzhuanout, boolean aflag, Long credate, boolean anbaochuku, Branch userbranch, boolean isAutoSupplyLink) {
		/* ***************modify begin*********************/
		//modify by neo01.huang，2016-8-1
		//是否为真正一票多件
		boolean isRealOneVoteMultiPiece = mpsCommonService.isRealOneVoteMultiPiece(co, isypdjusetranscwb);
		if (!isRealOneVoteMultiPiece) { //不是真正一票多件，跳过订单状态流程校验
			this.validateCwbState(co, flowOrderTypeEnum);
		}
		/* ***************modify end*********************/
		
		// 对oxo和oxo_jit的订单不用中转审核 by jinghui.pan on 20150806
		if (this.isOxoAndJitType(co) == false) {
			this.validateAppZhongZhuan(cwb, co, flowOrderTypeEnum);// 中转校验
		}

		// 对oxo和oxo_jit的订单不用中转审核 by jinghui.pan on 20150806
		if (this.isOxoAndJitType(co) == false) {
			this.validateAppZhongZhuan(cwb, co, flowOrderTypeEnum);// 中转校验
		}

		/* ***************modify begin*********************/
		if (!isRealOneVoteMultiPiece) { //不是真正一票多件，走原来的逻辑
			if ((co.getFlowordertype() != flowOrderTypeEnum.getValue()) || (co.getStartbranchid() != currentbranchid)) {
				this.validateStateTransfer(co, flowOrderTypeEnum);
			}
			
		} else {
			
			//通过运单的flowordertype校验订单的操作流程
			mpsOptStateService.validateStateTransferForTranscwb(cwb, scancwb, flowOrderTypeEnum);
			
		}
		
		/* ***************modify begin*********************/
		
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
			this.cwbDAO.updateCwbState(cwb, CwbStateEnum.PeiShong);
			// added by songkaojun 2016-01-28 一票多件模式，要更新运单状态为中转
			if (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
				this.transCwbDao.updateTransCwbState(scancwb, TransCwbStateEnum.PEISONG);
			}
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
					CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, cwb);
		co.setCurrentbranchid(0); // 将订单的currentbrnachid更新上去
		co.setNextbranchid(branchid);// 将订单的nextbrnachid更新上去
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, 0, co.getNextbranchid());// 更新订单一票多件状态和运单状态
		// 中转站出库，将运单的状态置为配送
		this.mpsOptStateService.updateTranscwbstate(scancwb, TransCwbStateEnum.PEISONG);
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
		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, credate, scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid, isAutoSupplyLink);
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
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					accountCwbDetail = this.accountCwbDetailService
							.formForAccountCwbDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), user.getUserid(), currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					CwbOrderService.logger.info("用户:{},创建买单结算该站冲款扫描记录,站点id{},出库站{},订单号{}", new Object[] { user.getRealname(), co.getNextbranchid(), userbranch.getBranchname(), co.getCwb() });
				}
			}
			// 扣款结算
			if (nextbranch.getAccounttype() == 3) {
				CwbOrderService.logger.info("===开始创建扣款结算强制出库中转货款数据===");
				BigDecimal fee = BigDecimal.ZERO;
				// //上门退订单
				// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// fee=co.getPaybackfee();
				// }else{//配送||其他
				fee = co.getReceivablefee();
				// }
				AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
				accountDeducDetail = this.accountDeducDetailService
						.loadFormForAccountDeducDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), fee, user.getUserid(), "强制出库", 0, 1);

				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				CwbOrderService.logger.info("用户:{},创建扣款结算强制出库中转退货：站点id{},代收货款{}元,id：{}", new Object[] { user.getRealname(), co.getNextbranchid(), fee, id });
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
					CwbOrderService.logger.info("===开始创建扣款结算中转出站出库记录===");
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
					accountDeductRecord = this.accountDeductRecordService
							.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货出库", co
									.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), fee, user.getUserid(), "到错货出库", recordid, 1);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户:{},创建扣款结算到错货出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), fee });
					CwbOrderService.logger
							.info("用户:对{}站点进行到错货出库扣款：原余额{}元，原欠款{}元。出库{}后，余额{}元，欠款{}元", new Object[] { branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(), koukuan, balance, debt });
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
					CwbOrderService.logger.info("用户:{},创建结算库房出库扫描记录,站点{},出库库房{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue();// 中转出库扫描
					CwbOrderService.logger.info("用户:{},创建结算中转站点出库扫描记录,站点{},出库中转站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();// 退货出库扫描
					CwbOrderService.logger.info("用户:{},创建结算退货站点出库扫描记录,站点{},出库退货站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
				accountCwbDetail = this.loadFormForAccountCwbDetail(co, branchid, flowordertype, user, currentbranchid);
				this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
			}// 买单结算End

			// 扣款结算
			if (tobranch.getAccounttype() == 3) {
				// 当前操作站点为库房or中转站or退货站
				if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue()) || (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) || (userbranch.getSitetype() == BranchEnum.TuiHuo
						.getValue())) {
					CwbOrderService.logger.info("===开始扣款===");
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

					CwbOrderService.logger.info("===开始修改账户===");
					this.branchDAO.updateForFee(branchid, balance, debt);

					CwbOrderService.logger.info("===插入一条扣款记录===");
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService
							.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "", co
									.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					CwbOrderService.logger.info("===插入一条订单记录===");
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), co.getReceivablefee(), user
							.getUserid(), "", recordid, 0);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户{},对{}站点进行出库扣款：扣款记录id{}，原余额{}元，原欠款{}元。扣款{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), recordid, branchLock
							.getBalance(), branchLock.getDebt(), koukuan, balance, debt });
					CwbOrderService.logger.info("===扣款结束===");
				}
			}

		}
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			Branch nextorg = this.branchDAO.getBranchByBranchid(branchid);
			nextorg = nextorg == null ? new Branch() : nextorg;
			this.executeTpsTransInterface(co, user, nextorg);
		}
	}

	/**
	 * 调用tps运单反馈接口 -- 中转出库
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(CwbOrder order, User user, Branch nextBranch) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo(order.getCwb());
		transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());// tps机构编码
		transNoFeedBack.setNextOrg(nextBranch.getTpsbranchcode());
		transNoFeedBack.setOperater(user.getRealname());
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
	 * 出库扫描 (站点出站)
	 *
	 * @param cwb
	 * @param owgid
	 * @param driverid
	 * @param truckid
	 * @param anbaochuku
	 *            是否按包出库
	 */
	public CwbOrder outWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, long reasonid, boolean iszhongzhuanout, boolean anbaochuku) {

		CwbOrderService.logger.info("开始出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.outWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, iszhongzhuanout, System
				.currentTimeMillis(), anbaochuku, true);
	}

	@Transactional
	public CwbOrder outWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, boolean iszhongzhuanout, Long credate, boolean anbaochuku, boolean isAutoSupplyLink) {
		// added by songkaojun 2016-01-18 更新订单上一机构，当前机构，下一机构信息
		this.cwbOrderBranchInfoModificationService.modifyBranchInfo(scancwb);

		this.resetScannumForOutwarehouse(user, cwb, scancwb, currentbranchid, branchid, anbaochuku, isauto);

		// added by songkaojun 2016-01-09
		Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentbranchid);
		if (ifBranch.getSitetype() == BranchEnum.KuFang.getValue()) {// 如果是库房，那么需要进去集包校验
			// 对于一票多件的校验是否可以放行
			this.outWarehouseMPSReleaseService.validateReleaseCondition(scancwb);
		}
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if ((co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) && ((co.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getStartbranchid() != currentbranchid))) {
			this.validateStateTransfer(co, FlowOrderTypeEnum.ChuKuSaoMiao);
		}
		// 如果被拦截，则给出提示
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.ChuKuSaoMiao);

		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {// 是否拥有
			// 请指出库权限
			// 1是
			// 0
			// 否
			// 默认1
			forceOut = false;
		}

		// OXO项目：OXO_JIT订单不允许做领货。 by jinghui.pan@pjbest.com on 20150730
		if (this.isJitType(co)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.OXO_JIT_DISALLOW_TRANSFER_STATION_OUTBOUND);
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

		/*Modified by leoliao at 2016-07-22 修改非本站货物判断逻辑
		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((co
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid) && !anbaochuku && (co
				.getNextbranchid() == user.getBranchid())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}
		*/
		long     isypdjusetranscwb = 0;
		Customer customer          = this.customerDAO.getCustomerById(co.getCustomerid());
		if(customer != null){
			isypdjusetranscwb = customer.getIsypdjusetranscwb();
		}
		
		logger.info("outWarehousHandle->订单信息：cwb:{}, scancwb:{}, sendcarnum:{}, flowordertype:{}, deliverystate:{}, currentbranchid:{}, userBranchId:{}, isypdjusetranscwb={}", 
					cwb, scancwb, co.getSendcarnum(), co.getFlowordertype(), co.getDeliverystate(), co.getCurrentbranchid(), currentbranchid, isypdjusetranscwb);
		if(isypdjusetranscwb == 0 || co.getSendcarnum() <= 1 || org.apache.commons.lang3.StringUtils.isEmpty(org.apache.commons.lang3.StringUtils.trimToEmpty(co.getTranscwb()))) {
			//非一票多件
			if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || 
				 (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || 
				 ((co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && 
				 (co.getCurrentbranchid() != currentbranchid) && !anbaochuku) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
			}
		}else{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cwb", co.getCwb());
			paramMap.put("scancwb", scancwb);
			paramMap.put("isnow", 1);
			List<TranscwbOrderFlow> list = transcwborderFlowDAO.queryTranscwbOrderFlow(paramMap, null);
			if (CollectionUtils.isNotEmpty(list)) {
				TranscwbOrderFlow transcwbOrderFlow = list.get(0);
				
				logger.info("outWarehousHandle->运单信息：scancwb:{}, flowordertype:{}, branchid:{}", transcwbOrderFlow.getScancwb(), transcwbOrderFlow.getFlowordertype(), transcwbOrderFlow.getBranchid());
				if ((transcwbOrderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || 
					 transcwbOrderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() ||
					 (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) 
					) && transcwbOrderFlow.getBranchid() != currentbranchid && !anbaochuku) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
				}
			}
		}
		//Modified end by leoliao

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		//long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.ChuKuSaoMiao;
		// 验证是否已经封包(按包出校验订单是否在包中)
		if (!anbaochuku) {
			this.checkBaleOfOrder(cwb, scancwb, isypdjusetranscwb, flowOrderTypeEnum);
		}
		// 若当前 归班反馈 反馈为待中转，失效该记录
		DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if ((ds != null) && (DeliveryStateEnum.DaiZhongZhuan.getValue() == ds.getDeliverystate()) && (ds.getDeliverybranchid() == currentbranchid)) {
			this.inactiveDeliveryStateByCwb(user, ds);
		}

		// =====加入按包出库标识 zs=====
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()))) {
			return this
					.handleOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.ChuKuSaoMiao, isypdjusetranscwb, iszhongzhuanout, aflag, credate, driverid, truckid, anbaochuku, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			// 出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0) || (co.getNextbranchid() == currentbranchid)) && (co
					.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {// 重复
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.ChuKuSaoMiao, isypdjusetranscwb, false, iszhongzhuanout, aflag, credate, anbaochuku, driverid, truckid, false, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// 原包号处理
		// disposePackageCode(packagecode, scancwb, user, co);

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void resetScannumForOutwarehouse(User user, String cwb, String scancwb, long currentbranchid, long nextbranchid, boolean anbaochuku, boolean isauto) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if (!anbaochuku) {
			if (cwbOrder == null) {
				return;
			}
			long branchid = user.getBranchid();
			if (isauto) {
				branchid = currentbranchid;
			}
			nextbranchid = this.getNextBranchidForMatchedOrder(nextbranchid, cwbOrder, user);
			this.mpsCommonService.resetScannumByTranscwb(scancwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, nextbranchid);
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
	private void checkBaleOfOrder(String cwb, String scancwb, long isypdjusetranscwb, FlowOrderTypeEnum flowOrderTypeEnum) {

		List<BaleCwb> baleCwbs = new ArrayList<BaleCwb>();
		if (isypdjusetranscwb == 1) {
			baleCwbs = this.baleCwbDAO.getBaleCwbByCwb(scancwb);
		} else {
			baleCwbs = this.baleCwbDAO.getBaleCwbByCwb(cwb);
		}
		if ((baleCwbs != null) && (baleCwbs.size() > 0)) {
			for (BaleCwb baleCwb : baleCwbs) {
				Bale bale = this.baleDAO.getBaleById(baleCwb.getBaleid());
				if ((null != bale) && (bale.getBalestate() == BaleStateEnum.YiFengBao.getValue())) {
					throw new CwbException(scancwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YIJINGFENGBAO, bale.getBaleno(), flowOrderTypeEnum.getText());
				}
			}
		}
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

	private CwbOrder handleOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean iszhongzhuanout, boolean aflag, Long credate, long driverid, long truckid, boolean anbaochuku, boolean isAutoSupplyLink) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		boolean newMPSOrder = this.mpsCommonService.isNewMPSOrder(scancwb);
		// 先更新运单状态，要不然运单流程里面的下一站为0
		this.mpsOptStateService.updateTransCwbDetailInfo(scancwb, flowOrderTypeEnum, -1L, -1L, branchid);
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if (((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) || (newMPSOrder && (co
				.getScannum() > 0))) {
			if (co.getScannum() < 1) {
				this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, driverid, truckid, false, isAutoSupplyLink);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum()) || anbaochuku) {
				long realscannum = co.getScannum() + 1;
				if (isypdjusetranscwb == 1) {
					// 一票多件使用运单号时，扫描次数需要计算
					realscannum = this.transcwborderFlowDAO.getTranscwbOrderFlowByScanCwbCount(scancwb, co.getCwb(), flowOrderTypeEnum.getValue(), currentbranchid, branchid) + 1;
				}
				if (!anbaochuku) {
					this.cwbDAO.updateScannum(co.getCwb(), realscannum);
				}
				// 结算更新扫描件数
				this.accountCwbDetailDAO.updateAccountCwbDetailScannum(co.getCwb(), realscannum);

				// 已出库 向打印列表 插入数据
				this.produceGroupDetail(user, cwb, requestbatchno, isauto, flowOrderTypeEnum.getValue(), branchid, co.getDeliverid(), co.getCustomerid(), driverid, truckid, packagecode);
				co.setScannum(realscannum);
				if (isypdjusetranscwb == 1) {
					if (!newMPSOrder) {
						this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
					}
					// 订单如果匹配过，branchid取匹配的站点
					if (!anbaochuku) {
						branchid = this.getNextBranchidForMatchedOrder(branchid, co, user);
					}
					// 删除一票多件缺件数据
					this.intoAndOutwarehouseYpdjDel(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid);
				}
				if (newMPSOrder) {
					this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, driverid, truckid, true, isAutoSupplyLink);
				}
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			}
		} else {
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, forceOut);
			this.handleOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, iszhongzhuanout, aflag, credate, false, driverid, truckid, false, isAutoSupplyLink);
		}
		// //包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	/**
	 * 校验一票多件状态
	 * @param co 订单
	 * @param flowOrderTypeEnum 操作类型
	 * @param isypdjusetranscwb 一票多件是否使用运单号
	 * @param forceOut 是否强制出库
	 * @see CwbOrderService.validateYipiaoduojianStateForZhongZhuanKu
	 */
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
		if (!this.mpsCommonService.isNewMPSOrder(co.getCwb())) {
			SystemInstall switchInstall = this.systemInstallDAO.getSystemInstall("ypdjpathtong");
			if ("0".equals(switchInstall.getValue())) {
				// 针对一票多件多个订单号的订单扫描其中运单号,未匹配站点,出库给不同下一站的时候会更改扫描次数,并且重复扫描同一运单号,再扫其他单号的时候会直接报重复出库的问题
				if (!forceOut && (co.getSendcarnum() > co.getScannum()) && (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co.getSendcarnum())) {
					boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
					if (!allarrive) {
						throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
								.getText());
					}
				}
			} else if ("1".equals(switchInstall.getValue())) {
				// 一票多件时在领货前的操作是不阻挡的，但在领货的时候会拦截一票多件前一环节件数不对而阻拦
				if (!forceOut && ((co.getSendcarnum() > co.getScannum()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
						.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) && (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co
						.getSendcarnum()))) {
					boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
					if (!allarrive) {
						throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
								.getText());
					}
				}
				// 一票多件时在领货前的操作是不阻挡的，但在领货的时候会拦截一票多件前一环节件数不对而阻拦
				else if (!forceOut && ((co.getSendcarnum() > co.getScannum()) && (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()))) {
					boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
					if (!allarrive) {
						throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
								.getText());
					}
				}
			}
		}
	}
	
	/**
	 * 校验一票多件状态（For中转）
	 * copy 自validateYipiaoduojianState
	 * @param co 订单
	 * @param flowOrderTypeEnum 操作类型
	 * @param isypdjusetranscwb 一票多件是否使用运单号
	 * @param forceOut 是否强制出库
	 * @author neo01.huang，2016-8-3
	 */
	private void validateYipiaoduojianStateForZhongZhuanKu(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean forceOut) {
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
		if (!this.mpsCommonService.isNewMPSOrder(co.getCwb())) {
			SystemInstall switchInstall = this.systemInstallDAO.getSystemInstall("ypdjpathtong");
			if ("0".equals(switchInstall.getValue())) {
				// 针对一票多件多个订单号的订单扫描其中运单号,未匹配站点,出库给不同下一站的时候会更改扫描次数,并且重复扫描同一运单号,再扫其他单号的时候会直接报重复出库的问题
				if (!forceOut && (co.getSendcarnum() > co.getScannum()) && (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co.getSendcarnum())) {
					boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
					if (!allarrive) {
						throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
								.getText());
					}
				}
			} else if ("1".equals(switchInstall.getValue())) {
				// 一票多件时在领货前的操作是不阻挡的，但在领货的时候会拦截一票多件前一环节件数不对而阻拦
				if (!forceOut && ((co.getSendcarnum() > co.getScannum()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
						.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())) && (co.getFlowordertype() != flowOrderTypeEnum.getValue()) && (alength == co
						.getSendcarnum()))) {
					
					if (flowOrderTypeEnum.getValue() != FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
						boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
						if (!allarrive) {
							throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
									.getText());
						}
					}
					
					
				}
				// 一票多件时在领货前的操作是不阻挡的，但在领货的时候会拦截一票多件前一环节件数不对而阻拦
				else if (!forceOut && ((co.getSendcarnum() > co.getScannum()) && (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()))) {
					boolean allarrive = this.automateCheck(co, flowOrderTypeEnum);
					if (!allarrive) {
						throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), flowOrderTypeEnum
								.getText());
					}
				}
			}
		}
	}
	
	private boolean automateCheck(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {
		boolean allarrive = false;
		try {
			int isOpenFlag = this.autoUserService.getAutoFlag();
			if (isOpenFlag == 1) {
				if (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.RuKu.getValue()) {
					allarrive = true;
				} else if (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					allarrive = true;
				} else if (flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					allarrive = this.checkAllArriveSite(co);
				}
			}
		} catch (Exception e) {
			CwbOrderService.logger.info("automateCheck error.", e);
		}
		return allarrive;
	}
	private boolean checkAllArriveSite(CwbOrder co) {
		boolean arrive = true;
		List<String> transcwbList = this.getTranscwbList(co.getTranscwb());
		if (transcwbList != null) {
			for (String transcwb : transcwbList) {
				TranscwbOrderFlow tcof = this.transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, co.getCwb());
				if ((tcof == null) || (tcof.getFlowordertype() != FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())) {
					arrive = false;
					break;
				}
			}
		}
		return arrive;
	}
	public List<String> getTranscwbList(String transcwbs) {
		if ((transcwbs == null) || (transcwbs.length() < 1)) {
			return null;
		}
		List<String> transcwbList = null;
		String transcwbArr[] = null;
		if (transcwbs.indexOf(",") > -1) {
			transcwbArr = transcwbs.split(",");
		} else {
			transcwbArr = transcwbs.split(":");
		}
		if ((transcwbArr != null) && (transcwbArr.length > 0)) {
			for (String transcwbStr : transcwbArr) {
				String transcwb = transcwbStr == null ? null : transcwbStr.trim();
				if ((transcwb != null) && (transcwb.length() > 0)) {
					if (transcwbList == null) {
						transcwbList = new ArrayList<String>();
					}
					transcwbList.add(transcwb);
				}
			}
		}
		return transcwbList;
	}

	private void handleOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean iszhongzhuanout, boolean aflag, Long credate, boolean anbaochuku, long driverid, long truckid, boolean newMPSOrder, boolean isAutoSupplyLink) {
		this.validateCwbState(co, flowOrderTypeEnum);
		if ((co.getFlowordertype() != FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getStartbranchid() != currentbranchid)) {
			this.validateStateTransfer(co, FlowOrderTypeEnum.ChuKuSaoMiao);
		}
		if (iszhongzhuanout) {
			// 中转出站操作根据系统设置，是否只有审核的订单才可以中转出站

			// 对oxo和oxo_jit的订单不用中转审核 by jinghui.pan@pjbest.com
			if (this.isOxoAndJitType(co) == false) {
				int changealowflag = this.getChangealowflagById(co);

				if ((changealowflag == 1) && (co.getDeliverystate() == DeliveryStateEnum.DaiZhongZhuan.getValue())) {
					CwbApplyZhongZhuan cwbApplyZhongZhuan = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanByCwb(cwb);
					if (cwbApplyZhongZhuan != null) {
						if (cwbApplyZhongZhuan.getIshandle() == 0) {
							throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Weishenhebuxuzhongzhuanchuku);
						}
						if (cwbApplyZhongZhuan.getIshandle() == 2) {
							throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuanchuku);
						}
					}
				}
			}

			// 非本操作站点的订单不允许出库（中转出站）
			if (co.getCurrentbranchid() != currentbranchid) {
				throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FeiBenZhanDianDingDanBuYunXuZhongZhuanChuZhan);
			}

			// // 配送单是否允许做中转 yes 允许做中转， no不允许 2015.05.27此控制要取消。
			// String isPeisongAllowtoZhongZhuan =
			// this.systemInstallDAO.getSystemInstall("isPeisongAllowtoZhongZhuan")
			// == null ? "yes" : this.systemInstallDAO.getSystemInstall(
			// "isPeisongAllowtoZhongZhuan").getValue();
			// if ("no".equalsIgnoreCase(isPeisongAllowtoZhongZhuan) &&
			// (co.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
			// throw new CwbException(cwb,
			// FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
			// ExceptionCwbErrorTypeEnum.Peisong_Bu_YunXu_ZhongZhuan);
			// }

			Branch sesionBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			if ((sesionBranch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (co.getCwbstate() == CwbStateEnum.PeiShong.getValue())) { // 如果当前站是中转站，并且cwbstate=中转
				this.cwbDAO.updateCwbState(cwb, CwbStateEnum.ZhongZhuan);
				// added by songkaojun 2016-01-28 一票多件模式，要更新运单状态为中转
				if (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
					this.transCwbDao.updateTransCwbState(scancwb, TransCwbStateEnum.ZHONGZHUAN);
				}
			}

			if (reasonid != 0) {
				Reason reason1 = this.reasonDAO.getReasonByReasonid(reasonid);
				if (reason1 != null) {
					this.cwbDAO.updateZhongzhuanReason(cwb, reason1.getReasonid(), reason1.getReasoncontent());

				}
			}
		}
		//退货再投审核
		OrderBackRuku orderBackRuku = this.orderBackRukuRecordDao.getBackrukuRecord(cwb);
		if (orderBackRuku != null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Weishenhebuxutuihuozaitou);
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
		// if (!"".equals(co.getPackagecode())) {
		// Bale bale = this.baleDAO.getBaleOneByBaleno(co.getPackagecode());
		// if (bale != null) {
		// this.groupDetailDAO.updateGroupDetailByBale(bale.getId(),
		// co.getPackagecode(), cwb, user.getBranchid());
		// }
		// }

		if (reasonid != 0) {
			Reason reason = this.reasonDAO.getReasonByReasonid(reasonid);
			if (reason != null) {
				comment = reason.getReasoncontent();
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
				} catch (Exception e) {
					CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
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
		if (!newMPSOrder) {
			this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.ChuKuSaoMiao, comment, credate, scancwb, newMPSOrder);
		}
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			if (!newMPSOrder) {
				this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid, isAutoSupplyLink);
			}
		}
		// added by songkaojun 2016-01-11
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, currentbranchid, 0L, branchid);// 更新订单一票多件状态和运单状态
		// ============结算逻辑出库扫描=======================
		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		Branch nextbranch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
		Branch tobranch = this.branchDAO.getBranchByBranchid(branchid);

		// 强制出库&&当前订单状态为出库状态 插入一条中转站
		if ((forceOut == true) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {
			// 买单结算
			if (nextbranch.getAccounttype() == 1) {
				// 如果订单类型:配送||(上门退&&上门退成功)||(上门换) 插入中转记录
				if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
					AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
					accountCwbDetail = this.accountCwbDetailService
							.formForAccountCwbDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), user.getUserid(), currentbranchid);
					this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
					CwbOrderService.logger.info("用户:{},创建买单结算该站冲款扫描记录,站点id{},出库站{},订单号{}", new Object[] { user.getRealname(), co.getNextbranchid(), userbranch.getBranchname(), co.getCwb() });
				}
			}
			// 扣款结算
			if (nextbranch.getAccounttype() == 3) {
				CwbOrderService.logger.info("===开始创建扣款结算强制出库中转货款数据===");
				BigDecimal fee = BigDecimal.ZERO;
				// //上门退订单
				// if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
				// fee=co.getPaybackfee();
				// }else{//配送||其他
				fee = co.getReceivablefee();
				// }
				AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
				accountDeducDetail = this.accountDeducDetailService
						.loadFormForAccountDeducDetail(co, co.getNextbranchid(), AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue(), fee, user.getUserid(), "强制出库", 0, 1);

				long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
				CwbOrderService.logger.info("用户:{},创建扣款结算强制出库中转退货：站点id{},代收货款{}元,id：{}", new Object[] { user.getRealname(), co.getNextbranchid(), fee, id });
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
					CwbOrderService.logger.info("===开始创建扣款结算中转出站出库记录===");
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
					accountDeductRecord = this.accountDeductRecordService
							.loadFormForAccountDeductRecord(currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "到错货出库", co
									.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService
							.loadFormForAccountDeducDetail(co, currentbranchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), fee, user.getUserid(), "到错货出库", recordid, 1);
					long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户:{},创建扣款结算到错货出库id{}：站点{},代收货款{}元", new Object[] { user.getRealname(), id, userbranch.getBranchname(), fee });
					CwbOrderService.logger
							.info("用户:对{}站点进行到错货出库扣款：原余额{}元，原欠款{}元。出库{}后，余额{}元，欠款{}元", new Object[] { branchLock.getBranchname(), branchLock.getBalance(), branchLock.getDebt(), koukuan, balance, debt });
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
					CwbOrderService.logger.info("用户:{},创建结算库房出库扫描记录,站点{},出库库房{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue();// 中转出库扫描
					CwbOrderService.logger.info("用户:{},创建结算中转站点出库扫描记录,站点{},出库中转站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				if (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					flowordertype = AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();// 退货出库扫描
					CwbOrderService.logger.info("用户:{},创建结算退货站点出库扫描记录,站点{},出库退货站{},订单号:{}", new Object[] { user.getRealname(), tobranch.getBranchname(), userbranch.getBranchname(), co.getCwb() });
				}
				AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
				accountCwbDetail = this.loadFormForAccountCwbDetail(co, branchid, flowordertype, user, currentbranchid);
				this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
			}// 买单结算End

			// 扣款结算
			if (tobranch.getAccounttype() == 3) {
				// 当前操作站点为库房or中转站or退货站
				if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue()) || (userbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) || (userbranch.getSitetype() == BranchEnum.TuiHuo
						.getValue())) {
					CwbOrderService.logger.info("===开始扣款===");
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

					CwbOrderService.logger.info("===开始修改账户===");
					this.branchDAO.updateForFee(branchid, balance, debt);

					CwbOrderService.logger.info("===插入一条扣款记录===");
					AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
					accountDeductRecord = this.accountDeductRecordService
							.loadFormForAccountDeductRecord(branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), koukuan, branchLock.getBalance(), balance, user, branchLock.getDebt(), debt, "", co
									.getCwb());
					long recordid = this.accountDeductRecordDAO.createAccountDeductRecord(accountDeductRecord);

					CwbOrderService.logger.info("===插入一条订单记录===");
					AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
					accountDeducDetail = this.accountDeducDetailService.loadFormForAccountDeducDetail(co, branchid, AccountFlowOrderTypeEnum.KouKuan.getValue(), co.getReceivablefee(), user
							.getUserid(), "", recordid, 0);
					this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
					CwbOrderService.logger.info("用户{},对{}站点进行出库扣款：扣款记录id{}，原余额{}元，原欠款{}元。扣款{}后，余额{}元，欠款{}元", new Object[] { user.getRealname(), branchLock.getBranchname(), recordid, branchLock
							.getBalance(), branchLock.getDebt(), koukuan, balance, debt });
					CwbOrderService.logger.info("===扣款结束===");
				}
			}

		}

		// PJ快递单反馈操作流程状态-------LX
		CwbOrderService.logger.info("快递流程状态反馈接口（出库反馈）======开始");
		// 现在快递类型的判断，只有快递单才需要此操作--刘武强10.19
		if ((flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())) {
			// 获取下一站操作站点
			Branch branchNext = this.branchDAO.getBranchByBranchid(branchid);
			String nextBranchcode = branchNext.getTpsbranchcode();
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
			PjTransportFeedbackRequest ptf = new PjTransportFeedbackRequest();
			// 操作人
			ptf.setOperater(user.getRealname());
			// 操作时间
			long dat = System.currentTimeMillis();// 获取当前时间的毫秒数
			ptf.setOperateTime(dat);
			// 获取当前操作站点名
			Branch branch = this.branchDAO.getBranchByBranchid(currentbranchid);
			String tpsbranchcode = branch.getTpsbranchcode();
			ptf.setOperateOrg(tpsbranchcode);// 操作机构
			ptf.setNextOrg(nextBranchcode);// 下一站操作机构
			// 操作类型
			// ptf.setOperateType((int) flowOrderType);
			// 应该是对应出站扫描--刘武强10.19
			if (branch.getSitetype() == 1) {
				ptf.setOperateType(FeedbackOperateTypeEnum.OutboundScan.getValue());
			} else if (branch.getSitetype() == 2) {
				ptf.setOperateType(FeedbackOperateTypeEnum.TransferScan.getValue());
			} else {
				ptf.setOperateType(FeedbackOperateTypeEnum.PackingScan.getValue());
			}
			// 运单号/预约单号
			ptf.setTransportNo(cwb);
			paramObj.setTransNoFeedBack(ptf);
			paramObj.setTransNo(cwb);// 运单号
			/*
			 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
			 * contextVar.
			 * setOperationType(TpsOperationEnum.ArrivalScan.getValue(
			 * ));//揽件入站对应入站扫描
			 * contextVar.setStation(branch.getBranchname());//站点名称
			 * contextVar.setOperator(user.getRealname());
			 * contextVar.setNextStation
			 * (this.branchDAO.getBranchByBranchid(nextBranchId
			 * ).getBranchname()); contextVar.connectMessage();
			 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
			 */

			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}
		CwbOrderService.logger.info("快递流程状态反馈接口======结束");

	}

	/**
	 * 通过中转一级原因查询是否需中转
	 *
	 * @param co
	 * @return
	 */
	private int getChangealowflagById(CwbOrder co) {
		long firstchangereasonid = co.getFirstchangereasonid(); // 一级滞留原因
		Reason reason = this.reasonDAO.getReasonByReasonid(firstchangereasonid);

		int changealowflag = reason == null ? 0 : reason.getChangealowflag();
		return changealowflag;
	}

	/**
	 * 通过中转一级原因查询是否需中转
	 *
	 * @param co
	 * @return
	 */
	public int getChangealowflagByIdAdd(CwbOrder co) {
		long firstchangereasonid = co.getFirstchangereasonid(); // 一级滞留原因
		Reason reason = this.reasonDAO.getReasonByReasonid(firstchangereasonid);

		int changealowflag = reason == null ? 0 : reason.getChangealowflag();
		return changealowflag;
	}

	public CwbOrder kdkoutWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, long reasonid) {

		CwbOrderService.logger.info("开始库对库出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.kdkoutWarehousHandle(user, cwb, scancwb, user.getBranchid(), driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, true);
	}

	@Transactional
	public CwbOrder kdkoutWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, boolean isAutoSupplyLink) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		// 如果被拦截，则给出提示
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao);

		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {
			forceOut = false;
		}

		if (!packagecode.equals("") && (packagecode.length() > 0)) {
			Bale isbale = this.baleDAO.getBaleOnwayBycwb(packagecode);
			if (packagecode.equals("0")) {
				throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BAO_HAO_BU_CUN_ZAI);
			} else {
				if (!packagecode.equals("") && (packagecode.length() > 0) && (isbale == null)) {
					Bale bale = new Bale();
					bale.setBaleno(packagecode);
					bale.setBranchid(user.getBranchid());
					bale.setBalestate(BaleStateEnum.WeiFengBao.getValue());
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
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()))) && (co.getCurrentbranchid() != currentbranchid)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid() == 0 ? co.getNextbranchid() : co.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != branchid) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) {
			return this
					.handleKDKOutowarehouseYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, isypdjusetranscwb, isAutoSupplyLink);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			// 库对库出库扫描时, 如果上一站是当前操作人所在的机构，那么出库需要验证是否重复扫描的逻辑
			if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) && ((co.getNextbranchid() == branchid) || (branchid == -1) || (branchid == 0)) && !forceOut) {
				throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, isypdjusetranscwb, false, isAutoSupplyLink);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleKDKOutowarehouseYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isAutoSupplyLink) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, branchid, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if ((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && !(forceOut && (co.getNextbranchid() != branchid) && (branchid > 0))) {
			if (co.getScannum() < 1) {
				this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, isAutoSupplyLink);
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
			this.handleKDKOutowarehouse(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, co, flowOrderTypeEnum, isypdjusetranscwb, true, isAutoSupplyLink);
		}
		// 刘武强 16.01.27
		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, 0L, 0L, co.getNextbranchid());// 更新订单一票多件状态和运单状态
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleKDKOutowarehouse(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean isAutoSupplyLink) {
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
			CwbOrderService.logger.info("cwb package code: {} to {}", co.getPackagecode(), packagecode);
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
					CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=?,packagecode=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), 0L, currentbranchid, branchid, packagecode, cwb);
		co.setCurrentbranchid(0);// 将修改会写到实体中 -- 刘武强
		co.setNextbranchid(branchid);// 将修改会写到实体中 --刘武强
		// 刘武强 16.01.27
		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, 0L, 0L, branchid);// 更新订单一票多件状态和运单状态

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
		this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao, comment, System.currentTimeMillis(), scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
			this.intoAndOutwarehouseYpdjCre(user, co, scancwb, flowOrderTypeEnum.getValue(), isypdjusetranscwb, branchid, isAutoSupplyLink);
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
	public CwbOrder outUntreadWarehous(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagename, boolean anbaochuku) {
		CwbOrderService.logger.info("开始退货出站处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		return this.outUntreadWarehousHandle(user, user.getBranchid(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, forceOut, comment, packagename, anbaochuku);
	}

	@Transactional
	public CwbOrder outUntreadWarehousHandle(User user, long currentbranchid, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean anbaochuku) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		/*
		 * TransCwbDetail transCwbDetail =
		 * this.transCwbDetailDAO.findTransCwbDetailByTransCwb(scancwb);
		 *
		 * // 特殊处理2： 对于退货出站操作，当前站不为0的订单（站点拦截的订单） 或者已审核或者退货出站或者订单拦截状态的运单不做拦截提示 if
		 * ( co.getCurrentbranchid() != 0 &&
		 * ((transCwbDetail.getTranscwboptstate() ==
		 * FlowOrderTypeEnum.YiShenHe.getValue()) ||
		 * (transCwbDetail.getTranscwboptstate() ==
		 * FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) ||
		 * (transCwbDetail.getTranscwboptstate() ==
		 * FlowOrderTypeEnum.DingDanLanJie.getValue()))) {
		 */

		// added shenhongfei 退货出站 2016-1-12
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.TuiHuoChuZhan);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		
		// 非本站货 add by jian_xie 2016-07-27， 逻辑有问题，一票多件出了第一单后，订单当前站点为空。
//		if(co.getCurrentbranchid() != user.getBranchid()){
//			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
//		}
		
		// added by jiangyu begin 快递订单不允许做退货出站操作
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.ExpressNotAllowTUIHUOCHUZHAN);
		}
		// added by jiangyu end

		// 审核未上门拒退的订单不允许做退货出站操作
		if (co.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.SHANGMENJUTUI_BUYUNXU_TUIHUOCHUZHAN);
		}

		if (this.userDAO.getAllUserByid(user.getUserid()).getIsImposedOutWarehouse() == 0) {
			forceOut = false;
		}
		/*
		 * if(co.getCurrentbranchid()!=currentbranchid) { throw new
		 * CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
		 * ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO); }
		 * 不是这个目的地的货,除非强制出库
		 */
		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != branchid) && (branchid > 0) && !forceOut) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid())
					.getBranchname());
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		// =====加入按包出库标识zs====
		if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && !anbaochuku) {
			return this
					.handleOutUntreadWarehousYipiaoduojian(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, FlowOrderTypeEnum.TuiHuoChuZhan, isypdjusetranscwb, packagecode);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1) || anbaochuku) {
			if ((co.getStartbranchid() == currentbranchid) && (co.getNextbranchid() == branchid) && (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			} else {
				this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, FlowOrderTypeEnum.TuiHuoChuZhan, isypdjusetranscwb, false, anbaochuku);
			}
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// //原包号处理开始
		// disposePackageCode(packagecode, scancwb, user, co);
		// //包号结束
		/*
		 * }else{ throw new CwbException(cwb,
		 * FlowOrderTypeEnum.TuiHuoChuZhan.getValue
		 * (),ExceptionCwbErrorTypeEnum.Invalid_Cwb_State); }
		 */
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleOutUntreadWarehousYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, String packagecode) {
		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, currentbranchid, co.getNextbranchid(), ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
		}
		boolean newMPSOrder = this.mpsCommonService.isNewMPSOrder(scancwb);

		// 出库时statbranchid是当前站，操作是出库，下一站是选择的下一站，非强制(选择了强制，并且下一站和选择的下一站不一样时)
		if (((co.getStartbranchid() == currentbranchid) && (co.getFlowordertype() == flowOrderTypeEnum.getValue())) || (newMPSOrder && (co.getScannum() > 0))) {
			if (co.getScannum() < 1) {
				this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
			}
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum()) || (newMPSOrder && (co.getScannum() > 0))) {
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
				if (newMPSOrder) {
					this.handleOutUntreadWarehous(user, cwb, scancwb, currentbranchid, branchid, requestbatchno, forceOut, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true, false);
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

	private void handleOutUntreadWarehous(User user, String cwb, String scancwb, long currentbranchid, long branchid, long requestbatchno, boolean forceOut, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean anbaochuku) {

		this.validateCwbState(co, flowOrderTypeEnum);
		this.validateDeliveryStateForZhiLiu(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		// 退货出站前做订单校验（判断是否在退货申请表中，以及处于什么状态）

		/**
		 * 退货出站 审核表的相关校验，条件：需要审核订单、待审核、审核不通过
		 *
		 */
		Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
		if (chechFlag) {
			OrderBackCheck obc = this.orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
			if (obc != null) {
				if (obc.getCheckstate() == 1) { // 待审核
					throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.Tui_huo_chu_zhan_dai_shen_he);
				}
				if (obc.getCheckresult() == 2) { // 审核不通过 ,站点配送
					throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), ExceptionCwbErrorTypeEnum.Shenheweizhandianpeisong);
				}
			}
		}
		if (forceOut) {
			this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), branchid, co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		} else {
			this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");
		}
		// =====加入按包出库标识 zs==========

		if (!anbaochuku) {
			branchid = this.getNextBranchid(branchid, forceOut, co, user, false);// xiugai
		}

		// 更新订单打印的包号信息
		if (!"".equals(co.getPackagecode())) {
			Bale bale = this.baleDAO.getBaleOnway(co.getPackagecode());
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
		// added shenhongfei 退货出站 2016-1-12
		CwbOrder conum = this.cwbDAO.getCwbByCwbLock(cwb);

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, user.getBranchid(), branchid, cwb);

		this.createFloworder(user, currentbranchid, co, flowOrderTypeEnum, comment, System.currentTimeMillis(), scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {

			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);

		}
		this.mpsOptStateService.updateMPSInfo(scancwb, FlowOrderTypeEnum.TuiHuoChuZhan, this.getSessionUser().getBranchid(), 0L, branchid);

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
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, comment, System.currentTimeMillis(), scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
	}

	public long getNextBranchid(long branchid, boolean forceOut, CwbOrder co, User user, boolean aflag) {
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
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid()) && (currentbranchtype == BranchEnum.ZhanDian.getValue()) && ((type == BranchEnum.ZhongZhuan
					.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				CwbOrderService.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			}
		} else if ((branchid > 0) && (branchid != nextBranchid) && !forceOut) {
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid()) && (currentbranchtype == BranchEnum.ZhanDian
					.getValue()) && ((type == BranchEnum.ZhongZhuan.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				CwbOrderService.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			} else {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			}
		}
		return branchid;
	}

	private long getNextBranchidForMatchedOrder(long branchid, CwbOrder co, User user) {
		long nextBranchid = this.getNextBranchId(co, user);
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

		return branchid;
	}

	private long getNextBranchId(CwbOrder co, User user) {
		long nextbranchid = co.getNextbranchid();

		if (nextbranchid > 0) {
			long deliverystate = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).size() == 0 ? 0 : this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0).getDeliverystate();
			if ((nextbranchid == user.getBranchid()) && ((deliverystate != DeliveryStateEnum.JuShou.getValue()) && (deliverystate != DeliveryStateEnum.BuFenTuiHuo.getValue()) && (deliverystate != DeliveryStateEnum.ShangMenHuanChengGong
					.getValue()) && (deliverystate != DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && (deliverystate != DeliveryStateEnum.FenZhanZhiLiu.getValue())) && ((co.getFlowordertype() == CwbFlowOrderTypeEnum.FenZhanLingHuo
					.getValue()) || (co.getFlowordertype() == CwbFlowOrderTypeEnum.YiFanKui.getValue()))) {
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
		long id = this.deliveryStateDAO.creDeliveryState(co.getCwb(), co.getBusinessFee(), co.getCwbordertypeid(), deliverUser, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), co
				.isOut() ? 0 : 1, userid, deliverybranchid, co.getCustomerid(), co.getShouldfare());
		this.deliveryCashDAO.updateDeliveryCashStateBycwb(cwb);
		this.deliveryCashDAO.creDeliveryCash(cwb, deliverid, deliverybranchid, co.getCustomerid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), id, co.getReceivablefee());
	}

	/**
	 * 小件员领货
	 * 2016年6月23日 下午5:19:09
	 * @param user
	 * @param deliveryUser
	 * @param cwb
	 * @param scancwb
	 * @return
	 */
	public CwbOrder receiveGoodsByDeliver(User user, User deliveryUser, String cwb, String scancwb, boolean isChaoqu) {
		cwb = this.translateCwb(cwb);
		if (isChaoqu == false) { //未选择超区
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			if (co == null) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
						ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
			// 小件员地址库匹配领货优化
			if (co.getExceldeliverid() > 0) { // 必须该小件员才能领货
				if (co.getExceldeliverid() != deliveryUser.getUserid()) {
					User coDeliver = this.userDao.getUserByUserid(co.getExceldeliverid());
					// 如果地址库匹配的小件员不存在或离职，则按未匹配处理
					if (coDeliver != null && coDeliver.getUserid() != 0 && coDeliver.getEmployeestatus() != 3) {
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
								ExceptionCwbErrorTypeEnum.PEI_SONG_YUAN_BU_PI_PEI, coDeliver.getRealname(),
								deliveryUser.getRealname());
					} /*else { // 未匹配小件员
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
								ExceptionCwbErrorTypeEnum.PEI_SONG_YUAN_WEI_PI_PEI);
					}*/
				}
			} /*else { // 未匹配小件员
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
						ExceptionCwbErrorTypeEnum.PEI_SONG_YUAN_WEI_PI_PEI);
			}*/
		}
		return this.receiveGoodsHandle(user, user.getBranchid(), deliveryUser, cwb, scancwb, false);
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
		
		//add by neo01.huang，2016-6-29
		//领货校验到齐、同站领货
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder != null) {
			deliverTakeGoodsMPSReleaseService.validateReveiveGoodsAllArrivedAndSameBranch(cwbOrder, scancwb, user.getBranchid());
			
			ypdjHandleRecordDAO.delYpdjHandleRecordByCwb(cwbOrder.getCwb());
			logger.info("归班反馈->清除缺件记录,订单号:{}", cwbOrder.getCwb());
		}
		
		// 对于扫描订单号的，将运单号查询处理分别处理
		List<String> transCwbList = new ArrayList<String>();
		if ((cwb != null) && cwb.equalsIgnoreCase(scancwb)) {
			transCwbList = this.transCwbDetailDAO.getTransCwbListByCwb(cwb);
		} else {
			transCwbList.add(scancwb);
		}
		for (String transCwb : transCwbList) {
			// added by songkaojun 2016-01-18 更新订单上一机构，当前机构，下一机构信息
			this.cwbOrderBranchInfoModificationService.modifyBranchInfo(transCwb);
			this.resetScannumForReceiveGoods(user, cwb, transCwb);
			// added shenhongfei 小件员领货扫描 2016-1-12
			this.orderInterceptService.checkTransCwbIsIntercept(transCwb, FlowOrderTypeEnum.FenZhanLingHuo);
		}
		// 是否放行订单号运单号都可以处理		
		this.deliverTakeGoodsMPSReleaseService.validateReleaseCondition(scancwb);
		

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// OXO项目：OXO_JIT订单不允许做领货。 by jinghui.pan@pjbest.com on 20150730
		if (this.isJitType(co)) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.OXO_JIT_DISALLOW_RECEIVEGOODS);
		}

		// 订单如果存在未审批或者审批通过未确认的支付信息修改申请，不允许做领货。 by vic.liang@pjbest.com on
		// 20151013
		String searchCwb = "'" + cwb + "'";
		List<ZhiFuApplyView> zhiFuApplyViewList = this.zhiFuApplyDao.getZhiFuApplyViewListByCwbs(searchCwb);
		if ((zhiFuApplyViewList != null) && !zhiFuApplyViewList.isEmpty()) {
			for (ZhiFuApplyView zhifuApply : zhiFuApplyViewList) {
				if (zhifuApply != null) {
					int applystate = zhifuApply.getApplystate();// 审核所处的状态
					int applyresult = zhifuApply.getApplyresult(); // 审核结果
					int confirmstate = zhifuApply.getConfirmstate();// 确认状态
					int confirmresult = zhifuApply.getConfirmresult();// 确认结果
					if ((applystate == 1) && (applyresult == 0)) {// 客服未审核
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.LingHuo_ZhiFuXinxiWeiQueRen);
					} else if (((applystate == 2) && (applyresult == 2)) && ((confirmstate == 1) && (confirmresult == 0))) {// 客服审核通过财务未确认
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.LingHuo_ZhiFuXinxiWeiQueRen);
					}
				}
			}
		}
		
		if (1 == co.getDeliverypermit()) {//上门退订单关联的配送订单反馈拒收不能领货  2016-06-16
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.SHANG_MEN_TUI_PEI_SONG_JU_SHOU);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1) || (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) {
			return this.handleReceiveGoodsYipiaoduojian(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, FlowOrderTypeEnum.FenZhanLingHuo, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleReceiveGoods(user, cwb, scancwb, currentbranchid, deliveryUser, isauto, co, FlowOrderTypeEnum.FenZhanLingHuo, isypdjusetranscwb, false, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void resetScannumForReceiveGoods(User user, String cwb, String scancwb) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		long nextbranchid = 0L;
		if (cwbOrder != null) {
			nextbranchid = cwbOrder.getNextbranchid();
		}
		this.mpsCommonService.resetScannumByTranscwb(scancwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), user.getBranchid(), nextbranchid);
	}

	private CwbOrder handleReceiveGoodsYipiaoduojian(User user, String cwb, String scancwb, long currentbranchid, User deliveryUser, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb) {

		List<String> transCwbList = new ArrayList<String>();
		if ((cwb != null) && cwb.equalsIgnoreCase(scancwb) && (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) {
			transCwbList = this.transCwbDetailDAO.getTransCwbListByCwb(cwb);
		} else if ((cwb != null) && cwb.equalsIgnoreCase(scancwb) && (co.getIsmpsflag() == IsmpsflagEnum.no.getValue())) {
			if (null != co.getTranscwb()) {
				if (StringUtil.isEmpty(co.getTranscwb())) {
					transCwbList.add(co.getCwb());
				}
				transCwbList = Arrays.asList(co.getTranscwb().split(this.getSplitstring(co.getTranscwb())));
			}
		} else {
			transCwbList.add(scancwb);
		}
		for (String transCwb : transCwbList) {
			CwbOrder cwborder = this.cwbDAO.getCwbByCwbLock(cwb);
			if (isypdjusetranscwb == 1) {
				this.validateIsSubCwb(transCwb, cwborder, flowOrderTypeEnum.getValue());
				this.validateCwbChongFu(cwborder, transCwb, flowOrderTypeEnum.getValue(), cwborder.getCurrentbranchid(), 0, 0, ExceptionCwbErrorTypeEnum.Operation_Repeat);

			}
			// 是否为新一票多件模式
			boolean newMPSOrder = this.mpsCommonService.isNewMPSOrder(transCwb);
			if (((cwborder.getFlowordertype() == flowOrderTypeEnum.getValue())) || (newMPSOrder && (cwborder.getScannum() > 0))) {
				if ((cwborder.getScannum() < 1) || (cwborder.getDeliverid() != deliveryUser.getUserid())) {
					this.handleReceiveGoods(user, cwb, transCwb, currentbranchid, deliveryUser, isauto, cwborder, flowOrderTypeEnum, isypdjusetranscwb, true, false);
				}

				if ((cwborder.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (cwborder.getDeliverid() == deliveryUser.getUserid())) {// 重复领货
					if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
						throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao, cwborder.getCurrentbranchid(), deliveryUser.getBranchid());
					}
					if ((cwborder.getSendcarnum() > 0) && (cwborder.getSendcarnum() == cwborder.getScannum())) {
						throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Sao_Miao, cwborder.getCurrentbranchid(), deliveryUser.getBranchid());
					}
				}
				if ((cwborder.getDeliverid() == deliveryUser.getUserid()) && ((cwborder.getSendcarnum() > cwborder.getScannum()) || (cwborder.getBackcarnum() > cwborder.getScannum()))) {
					this.cwbDAO.updateScannum(cwborder.getCwb(), cwborder.getScannum() + 1);
					cwborder.setScannum(cwborder.getScannum() + 1);

					if (isypdjusetranscwb == 1) {
						this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, transCwb, flowOrderTypeEnum, "");
					}
					if (newMPSOrder) {
						this.handleReceiveGoods(user, cwb, transCwb, currentbranchid, deliveryUser, isauto, cwborder, flowOrderTypeEnum, isypdjusetranscwb, true, true);
						//Added by leoliao at 2016-03-09 解决领货后数据没到一体机的问题
						try {
							this.createFloworder(user, currentbranchid, cwborder, FlowOrderTypeEnum.FenZhanLingHuo, "", System.currentTimeMillis(), transCwb, newMPSOrder);
						} catch (Exception ex) {
							CwbOrderService.logger.error("一票多件发送给OMS系统出错：error send to jms, cwb=" + cwborder.getCwb(), ex);
						}
						//Added end
					}
				}
			} else {
				this.validateYipiaoduojianState(cwborder, flowOrderTypeEnum, isypdjusetranscwb, false);
				this.handleReceiveGoods(user, cwb, transCwb, currentbranchid, deliveryUser, isauto, cwborder, flowOrderTypeEnum, isypdjusetranscwb, true, false);
			}
		}
		return this.cwbDAO.getCwbByCwb(cwb);

	}

	private void handleReceiveGoods(User user, String cwb, String scancwb, long currentbranchid, User deliveryUser, boolean isauto, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean newMPSOrder) {
		/*
		 * if(isypdjusetranscwb==1&&co.getScannum()<co.getSendcarnum()){ throw
		 * new CwbException(cwb,
		 * flowOrderTypeEnum.getValue(),ExceptionCwbErrorTypeEnum
		 * .YIPIAODUOJIAN_DAOHUOBUQUAN); }
		 */
		/*
		 * if(isypdjusetranscwb==1&&co.getScannum()<co.getSendcarnum()){ throw
		 * new CwbException(cwb,
		 * flowOrderTypeEnum.getValue(),ExceptionCwbErrorTypeEnum
		 * .YIPIAODUOJIAN_DAOHUOBUQUAN); }
		 */
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		/**
		 * 中转订单流程校验 中转申请中 、中转申请通过 订单不允许做领货 isstastics=0
		 */
		this.validateAppZhongZhuanLinghuo(cwb, co, flowOrderTypeEnum);// 中转领货校验

		/**
		 * 退货订单流程校验 退货待审核状态不允许做 领货
		 */
		this.validateAppTuihuoCheckStatus(cwb, flowOrderTypeEnum);// 退货领货校验

		//快递单揽件入站   揽件站点和匹配站点一致，该站点可以领该快递单 2016-07-05 vic.liang@pjbest.com
		validateExpress(co, deliveryUser);	
		//快递单揽件入站   揽件站点和匹配站点一致，该站点可以领该快递单 end
				
		Branch userbranch = this.branchDAO.getBranchByBranchid(currentbranchid);
		// 扣款结算 流程检查 (到错货不允许做领货扫描)

		if ((co.getNextbranchid() != 0) && (co.getNextbranchid() != currentbranchid) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao
				.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha2);

		}

		if (userbranch.getAccounttype() == 3) {
			long count = this.cwbStateControlDAO.getCountFromstateTostate(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
			// 到错货订单：已做处理，可以领货；未作处理，不可领货；
			if ((count > 0) && (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.KouKuanLiuChengJianCha2);
			}
		}

		/* String usedeliverpay = "no";
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

		// 单件领货 重复判断（同一个小件员）(一票多件已经处理)
		if ((ds != null) && (deliveryUser.getUserid() == ds.getDeliveryid()) && (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getScannum() == 1) && ((co
				.getSendcarnum() == 1) || (co.getBackcarnum() == 1))) {
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.Chong_Fu_Ling_Huo);
		}

		if (!isauto && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			co = this.cwbAutoHandleService.autoSupplyLink(user, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), co, 0, scancwb, false);
		}

		// 2013-8-14 鞠牧 !(当前站点 是 是领货站点 并（订单流程是 分站到货
		// 、到错货、到错货处理、已审核的）||当前环节是领货/反馈的 并 配送站点是领货站点的)
		if (!( ((co.getCurrentbranchid() == deliveryUser.getBranchid())
				&& ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())
				    || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())
				    || (co.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue())
				    || (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) 
				    || (co.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue())
				    || (co.getFlowordertype() == FlowOrderTypeEnum.LanJianRuZhan.getValue()))) 
			|| ((co.getDeliverybranchid() == deliveryUser.getBranchid()) 
			    && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) 
					|| (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()))))) {
			
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.QING_ZUO_DAO_HUO_SAO_MIAO, co.getCurrentbranchid(), deliveryUser.getBranchid());
		}

		if (ds != null) {
			// 如果换小件员，则失效上一条记录
			if ((ds.getGcaid() <= 0) && (ds.getDeliverystate() == 0)) {
				// this.tryIncreaseScanNum(co);
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
		this.jdbcTemplate.update(sql, this.branchDAO.getBranchByBranchid(deliveryUser.getBranchid()).getBranchname(), deliveryUser.getBranchid(), currentbranchid, 0, FlowOrderTypeEnum.FenZhanLingHuo
				.getValue(), deliveryUser.getUserid(), DeliveryStateEnum.WeiFanKui.getValue(), cwb);
		if (!newMPSOrder) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			this.createFloworder(user, currentbranchid, co, FlowOrderTypeEnum.FenZhanLingHuo, "", System.currentTimeMillis(), scancwb, newMPSOrder);
		}

		if ((isypdjusetranscwb == 1) && isypdj) {
			if (!newMPSOrder) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
			}
		}

		// added shenhongfei modified by songkaojun 领货扫描 状态修改 2016-1-12
		this.updateMPSInfoForTakeGoods(cwb, scancwb, currentbranchid);
		// 如果是快递单，那么需要调用tps状态反馈接口----刘武强10.19
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			this.executeTpsTransInterface4TransFeedBack(co, user, userbranch.getTpsbranchcode(), FeedbackOperateTypeEnum.DeliveryScan.getValue(), deliveryUser);
		}
	}

	/**
	 * 调用tps运单反馈接口 ---小件员领货
	 *
	 * @param orders
	 */
	public void executeTpsTransInterface4TransFeedBack(CwbOrder transNo, User user, String branchcode, int Cwbordertypeid, User deliveryUser) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		transNoFeedBack.setTransportNo(transNo.getCwb());
		transNoFeedBack.setOperateOrg(branchcode);
		// User courier = this.userDAO.getUserByUserid(deliveryUser.g);
		// courier = courier == null ? new User() : courier;
		// transNoFeedBack.setCourier(courier.getRealname());
		transNoFeedBack.setCourier(deliveryUser.getRealname());
		transNoFeedBack.setCourierTel(("".equals(user.getUserphone()) || (user.getUserphone() == null)) ? user.getUsermobile() : user.getUserphone());// 如果手机号没有，则放电话号
		transNoFeedBack.setOperater(user.getRealname());
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(Cwbordertypeid);// 对应接口文档【枚举对应】
		transNoFeedBack.setReason("");
		/*
		 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
		 * contextVar.setOperationType
		 * (TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
		 * contextVar.setStation
		 * (this.branchDAO.getBranchByBranchid(this.getSessionUser
		 * ().getBranchid()).getBranchname());//站点名称
		 * contextVar.setOperator(packageMan); contextVar.connectMessage();
		 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
		 */
		paramObj.setTransNoFeedBack(transNoFeedBack);
		// 发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);
	}

	private void updateMPSInfoForTakeGoods(String cwb, String scancwb, long currentbranchid) {
		List<String> transCwbList = new ArrayList<String>();
		if ((cwb != null) && cwb.equalsIgnoreCase(scancwb)) {
			transCwbList = this.transCwbDetailDAO.getTransCwbListByCwb(cwb);
		} else {
			transCwbList.add(scancwb);
		}
		for (String transCwb : transCwbList) {
			this.mpsOptStateService.updateMPSInfo(transCwb, FlowOrderTypeEnum.FenZhanLingHuo, -1L, 0L, currentbranchid);
		}
	}

	private void validateAppTuihuoCheckStatus(String cwb, FlowOrderTypeEnum flowOrderTypeEnum) {
		Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
		if (chechFlag) {
			OrderBackCheck obc = this.orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
			if (obc != null) {
				//需要审核的订单的状态为1配送
				if (obc.getCheckstate() == 1) {// 待审核
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shenheweiquerentuihuosuccess);
				}
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				if (co.getCwbstate() != 1) {
					if (obc.getCheckresult() == 1) {// 审核为确认退货
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.Tuihuoquerensuccess);
					}
				}
			}
		}
	}

	private void validateAppZhongZhuanLinghuo(String cwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {

		if ((co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.DaiZhongZhuan.getValue())) {

			int changealowflag = this.getChangealowflagById(co); // 中转是否要申请
			if (changealowflag == 0) { // 不需要审核,领货提示不能操作
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.DaizhongzhuanCannotlinghuo);
			}

			CwbApplyZhongZhuan cwbApplyZhongZhuan = this.applyZhongZhuanDAO.getCwbApplyZhongZhuanByCwb(cwb);
			if (cwbApplyZhongZhuan != null) {
				long ishandle = cwbApplyZhongZhuan.getIshandle();
				if (ishandle == 0) {
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.DaizhongzhuanshenheCannotlinghuo);
				}
				if (ishandle == 3) {
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ShenhetongguoCannotlinghuo);
				}
			}
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

	public void produceGroupDetail(User user, String cwb, long outWarehouseGroupId, boolean isauto, long flowordertype, long nextbranchid, long deliverid, long customerid, long driverid, long truckid, String packagecode) {
		CwbOrderService.logger.info("============driverid={},truckid={}", driverid, truckid);
		List<GroupDetail> gdlist = this.groupDetailDAO.getCheckGroupDetailIsExist(cwb, flowordertype, user.getBranchid());
		if (!isauto) {
			if (gdlist.size() == 0) {
				this.groupDetailDAO.creGroupDetail(cwb, outWarehouseGroupId, user.getUserid(), flowordertype, user.getBranchid(), nextbranchid, deliverid, customerid, driverid, truckid, packagecode);
			} else {
				/**
				 * 广州通路按包操作 逻辑 临时方案： 按包（一票多件分扫描在在不同包中） SELECT * FROM
				 * express_ops_bale_cwb WHERE cwb IN (SELECT transcwb FROM
				 * express_ops_transcwb WHERE cwb = 'D0816026') OR cwb =
				 * 'D0816026'
				 */
				List<String> baleNoList = this.baleCwbDAO.getBaleNoList(cwb);
				List<String> existBaleNoList = this.buildExistBaleNoList(gdlist);
				// 更新订单打印的包号信息
				if ((null != baleNoList) && (baleNoList.size() > 1)) {
					if (baleNoList.contains(packagecode) && !existBaleNoList.contains(packagecode)) {
						this.groupDetailDAO
								.creGroupDetail(cwb, outWarehouseGroupId, user.getUserid(), flowordertype, user.getBranchid(), nextbranchid, deliverid, customerid, driverid, truckid, packagecode);
					} else {
						this.groupDetailDAO.saveGroupDetailByBranchidAndCwb(user.getUserid(), nextbranchid, cwb, user.getBranchid(), deliverid, customerid);
					}
				} else {
					this.groupDetailDAO.saveGroupDetailByBranchidAndCwb(user.getUserid(), nextbranchid, cwb, user.getBranchid(), deliverid, customerid);
				}
			}
			if (!"".equals(packagecode)) {
				Bale bale = this.baleDAO.getBaleOnway(packagecode);
				if (bale != null) {
					this.groupDetailDAO.updateGroupDetailByBale(bale.getId(), bale.getBaleno(), cwb, user.getBranchid());
				}
			}

		}
	}

	private List<String> buildExistBaleNoList(List<GroupDetail> gdlist) {
		List<String> resultList = new ArrayList<String>();
		for (GroupDetail groupDetail : gdlist) {
			/**
			 * 如果上次扫描已添加该环节该包记录
			 */
			if (!resultList.contains(groupDetail.getBaleno())) {
				resultList.add(groupDetail.getBaleno());
			}
		}
		return resultList;
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
	@Transactional(isolation = Isolation.REPEATABLE_READ)
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
		BigDecimal receivedfeecheque = parameters.get("receivedfeecheque") == null ? BigDecimal.ZERO : new BigDecimal(parameters.get("receivedfeecheque") + "");
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
		String sign_man_phone = parameters.get("sign_man_phone") == null ? "" : (String) parameters.get("sign_man_phone");
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

		String transcwb = parameters.get("transcwb") == null ? "" : (String) parameters.get("transcwb");

		transcwb = transcwb.replaceAll(" ", "");
		transcwb = transcwb.replaceAll("，", ",");// 全角逗号
		transcwb = transcwb.replaceAll("\r\n", "");

		CwbOrderService.logger.info("棒棒糖请求参数cwb={},leavedreasonid={},resendtime={},zhiliuremark={},transcwb={}", new Object[] { cwb, leavedreasonid, resendtime, zhiliuremark, transcwb});

		// 再次判定时间格式是否正确 如果正确 应该去掉空白符共18个字
		deliverytime = deliverytime.length() != 19 ? DateTimeUtil.getNowTime() : deliverytime;

		CwbOrderService.logger.info("修改反馈时间用户：" + user.getRealname() + " cwb" + cwb + "：当前{}改为{}", DateTimeUtil.getNowTime(), deliverytime);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if ((co != null) && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Express.getValue())) {
			this.validatorVipshopSMT(cwb, co);// 验证唯品会揽退单如果上一个状态未完成，则抛异常
		}

		// added by jiangyu begin
		// 缓存一下原先的支付方式
		map.put("preObj", co);
		if (co != null) {
			map.put("oldpaywayid", co.getNewpaywayid());
		}
		// added by jiangyu end

		// 委托派送变更状态为已反馈
		this.orderDeliveryClientDAO.updateFanKun(cwb);

		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Express.getValue()) {
			this.validatorVipshopSMT(cwb, co);// 验证唯品会揽退单如果上一个状态未完成，则抛异常
		}
		// 上门退、上门换订单，不能反馈为配送成功
		if (((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) || (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) && (podresultid == DeliveryStateEnum.PeiSongChengGong
				.getValue())) {
			throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.SMHorSMTBXYTHCZ, CwbOrderTypeIdEnum.getTextByValue(co.getCwbordertypeid()), DeliveryStateEnum.PeiSongChengGong
					.getText());
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
			if (fankuileixing.equals("PEISONG") && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue()) && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Express.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_PEI_SONG_DING_DAN);
			}
			// 上门换订单批量反馈,验证是否为上门换订单
			if (fankuileixing.equals("SHANGMENHUAN") && (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_SHANG_MEN_HUAN_DING_DAN);
			}
			// 上门退订单批量反馈,验证是否为上门退订单
			if (fankuileixing.equals("SHANGMENTUI") /*
													 * &&((podresultid ==
													 * DeliveryStateEnum
													 * .ShangMenTuiChengGong
													 * .getValue
													 * ())||(podresultid ==
													 * DeliveryStateEnum
													 * .ShangMenJuTui
													 * .getValue()))
													 */
					&& (co.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue())) {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_SHANG_MEN_TUI_DING_DAN);
			}
			// 已反馈订单不允许批量反馈
			if (((co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) && (deliveryState.getDeliverystate() != DeliveryStateEnum.WeiFanKui
					.getValue())) {
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
			if (!((((co.getCurrentbranchid() == deliveryUser.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
					.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()))) || (co
					.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue())) || ((co.getDeliverybranchid() == deliveryUser.getBranchid()) && ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo
					.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()))))) {
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.QING_ZUO_DAO_HUO_SAO_MIAO, co.getCurrentbranchid(), deliveryUser.getBranchid());
			}

			if ((deliveryState != null) && (deliverid != 0) && (deliveryState.getDeliveryid() != deliverid)) {
				if (deliverid == 0) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan);
				}
				this.receiveGoods(user, this.userDAO.getUserByUserid(deliverid), cwb, scancwb);
				deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
			}

			if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || ((podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) && (co.getPaybackfee()
					.compareTo(co.getReceivablefee()) == -1))) {

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
			if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) || (podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.DaiZhongZhuan
					.getValue())) {
				paybackedfee = new BigDecimal("0");
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
					CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}

		}else{
			//Hps_Concerto   如果 不是 批量反馈 如果 反馈为分站滞留。 那么 也需要加上时间前缀。
			if(podresultid==DeliveryStateEnum.FenZhanZhiLiu.getValue()&&deliverstateremark.length()>0){
				String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
				String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + zhiliuremark;
				try {
					this.cwbDAO.updateCwbRemark(co.getCwb(), newcwbremark);
					co.setCwbremark(newcwbremark);
				} catch (Exception e) {
					CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
					throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
				}
			}
		}

		if (((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenTuiChengGong
				.getValue()))) {
			sign_man = sign_man.length() == 0 ? co.getConsigneenameOfkf() : sign_man;
			sign_time = DateTimeUtil.getNowTime();
			sign_man_phone = sign_man_phone.length() == 0 ? "" : sign_man_phone;
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
		if ((isOpenFlag != 0) && ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong
				.getValue())) && (co.getPaywayid() == PaytypeEnum.Pos.getValue()) && ((receivedfeecash.compareTo(BigDecimal.ZERO) > 0) || (receivedfeecheque.compareTo(BigDecimal.ZERO) > 0) || (receivedfeeother
				.compareTo(BigDecimal.ZERO) > 0)) && (weishuakareasonid < 1) && (nosysyemflag == null)) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin);
		}

		String weishuakareasoncontent = "";

		// 根据生哥说的，只验证配送结果为配送成功和部分拒收、上门换成功的，只针对开启了亚马逊对接的订单
		if ((isOpenFlag != 0) && ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong
				.getValue())) && (co.getPaywayid() == PaytypeEnum.Pos.getValue()) && ((receivedfeecash.compareTo(BigDecimal.ZERO) > 0) || (receivedfeecheque.compareTo(BigDecimal.ZERO) > 0) || (receivedfeeother
				.compareTo(BigDecimal.ZERO) > 0))) {
			Reason weishuakareason = this.reasonDAO.getReasonByReasonid(weishuakareasonid);
			weishuakareasoncontent = weishuakareason != null ? weishuakareason.getReasoncontent() : "";
		} else {
			CwbOrderService.logger.info("此时订单{}的未刷卡原因id是{}", co.getCwb(), weishuakareasonid);
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
		String cwbremark = "";

		if ((backreasonid != 0) && ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui
				.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(backreasonid);
			cwbremark = this.creCwbremark(co.getCwbremark(), reason.getReasoncontent(), deliverstateremark,false);
			this.cwbDAO.saveCwbForBackreason(co.getCwb(), reason.getReasoncontent(), backreasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
		}

		if ((firstlevelreasonid != 0) && ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(leavedreasonid);
			cwbremark = this.creCwbremark(co.getCwbremark(), reason.getReasoncontent(), deliverstateremark,true);
			this.cwbDAO.saveCwbForLeavereason(co.getCwb(), reason.getReasoncontent(), leavedreasonid, firstlevelreasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
		}else if((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue())&&(deliverstateremark.length()>0)){
			cwbremark = this.creCwbremark(co.getCwbremark(), null, deliverstateremark,true);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
		}
		if ((changereasonid != 0) && (podresultid == DeliveryStateEnum.DaiZhongZhuan.getValue())) {
			reason = this.reasonDAO.getReasonByReasonid(changereasonid);
			cwbremark = this.creCwbremark(co.getCwbremark(), reason.getReasoncontent(), deliverstateremark,false);
			this.cwbDAO.saveCwbForChangereason(co.getCwb(), reason.getReasoncontent(), changereasonid, firstchangereasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
		}

		// 配送成功添加到历史备注中======LX====ADD====
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			cwbremark = this.creCwbremark(co.getCwbremark(), reason.getReasoncontent(), deliverstateremark,false);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
		}

		// 为货物丢失添加的
		if ((losereasonid != 0) && ((podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()))) {
			reason = this.reasonDAO.getReasonByReasonid(losereasonid);
			cwbremark = this.creCwbremark(co.getCwbremark(), reason.getReasoncontent(), deliverstateremark,false);
			this.cwbDAO.saveCwbForDiushireason(co.getCwb(), reason.getReasoncontent(), losereasonid);
			this.cwbDAO.updateCwbRemark(co.getCwb(), cwbremark);
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
		if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) || (podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) || (podresultid == DeliveryStateEnum.ZhiLiuZiDongLingHuo
				.getValue())) {
			receivedfeecash = receivedfeepos = receivedfeecheque = receivedfeeother = BigDecimal.ZERO;
			infactfare = BigDecimal.ZERO;// DMP4.2.3
											// 修复页面下拉先上门退成功=》再选分站滞留，实收运费被设置成应收运费的缺陷
											// by jinghui.pan 20151021
		}

		CwbOrderService.logger
				.info("进入单票反馈cwborderservice处理完后开始保存信息cwb:" + co.getCwb() + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--receivedfeecash:" + receivedfeecash + "--receivedfeepos:" + receivedfeepos + "--receivedfeecodpos:" + receivedfeecodpos + "--receivedfeecheque:" + receivedfeecheque + "--receivedfeeother:" + receivedfeeother + "--paybackedfee=" + paybackedfee + "--isbatch=" + isbatch + "--infactfare=" + infactfare);

		if ((podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && (infactfare.doubleValue() == 0)) {
			infactfare = co.getShouldfare();
		}

		this.deliveryStateDAO
				.saveForReFanKui(co.getCwb(), deliverid, receivedfee, paybackedfee, businessfee, podresultid, receivedfeecash, receivedfeepos, posremark, receivedfeecheque, checkremark, receivedfeeother, podremarkid, deliverstateremark, "", sign_typeid, sign_man, sign_time, sign_man_phone, receivedfeecodpos, infactfare);
		// 修改订单表的实收运费
		this.cwbDAO.updateCwbInfactFare(co.getCwb(), infactfare);

		// 上门退成功反馈时录入快递单号
		if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			//Modified by leoliao at 2016-06-25 完善运单号(快递单号)处理逻辑，解决运单号被清空问题。
			String paramTranscwb = (String) parameters.get("transcwb");
			if (paramTranscwb != null && !paramTranscwb.trim().equals("")) {
				paramTranscwb = paramTranscwb.trim();
				String[] transcwbArr = paramTranscwb.split(",");
				List<String> transcwbList = new ArrayList<String>();
				for (String transcwbTmp : transcwbArr) {
					if (transcwbTmp.length() > 0) {
						transcwbList.add(transcwbTmp);
					}
				}

				if (transcwbList.size() > 0) {
					this.transCwbDao.deleteTranscwb(cwb);
					for (String transcwbTmp : transcwbList) {
						List<TranscwbView> transcwbViewList = this.transCwbDao.getcwbBytranscwb(transcwbTmp);
						if ((transcwbViewList != null) && (transcwbViewList.size() > 0)) {
							if (transcwbTmp.equals(co.getTranscwb())) {//亚马逊及类似的情况，运单号是对方的主键，因此可能再次出现在退货时
								continue;
							}
							throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FANKUI_KUAIDIDANHAO_YIGUANLIAN);
						}
						this.transCwbDao.saveTranscwb(transcwbTmp, cwb);// it seem
																		// it is
																		// already
																		// done at
																		// OrderFlowNestedTransactionWrapper.saveTransCwb(),it
																		// also
																		// support
																		// split ,
					}
					this.transCwbDao.saveTranscwb(cwb, cwb);
					this.cwbDAO.saveTranscwbByCwb(paramTranscwb, cwb);
					this.cwbDAO.saveBackcarnum(transcwbList.size(), cwb);
					//this.cwbDAO.saveSendcarnum(transcwbList.size(), cwb);
				}
			}else if(paramTranscwb != null && paramTranscwb.trim().equals("")){
				//当录入或修改快递单号为空时
				this.transCwbDao.deleteTranscwb(cwb);
				this.transCwbDao.saveTranscwb(cwb, cwb);
				this.cwbDAO.saveTranscwbByCwb("", cwb);
				this.cwbDAO.saveBackcarnum(1, cwb);
				//this.cwbDAO.saveSendcarnum(1, cwb);
			}
			//Modified end
		}

		// 更新反馈时间
		this.deliveryStateDAO.updateDeliverytime(cwb, deliverytime);

		this.deliveryCashDAO
				.saveDeliveryCashForDSById(deliverytime, paybackedfee, receivedfeecash.add(receivedfeecheque).add(receivedfeeother), receivedfeepos.add(receivedfeecodpos), podresultid, deliveryState
						.getId());
		deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

		long newpaywayid = co.getPaywayid();

		String batchEditDeliveryStateisUseCash = "";

		if ((receivedfeepos.compareTo(BigDecimal.ZERO) == 0) && isbatch) {
			batchEditDeliveryStateisUseCash = this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash") == null ? "no" : this.systemInstallDAO
					.getSystemInstall("batchEditDeliveryStateisUseCash").getValue();
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
			// added by jiangyu begin
			map.put("newpaywayid", newpaywayid);
			// added by jiangyu end
			if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {// 上门退订单，支付方式都要变成现金
				newpaywayid = PaytypeEnum.Xianjin.getValue();
			}
		}
		// 更新当前反馈状态需要指定订单的下一站
		this.saveFanKuiNextBranchId(user, deliveryState.getDeliverystate(), cwb);

		String sql2 = "update express_ops_cwb_detail set flowordertype=?,deliverystate=?,newpaywayid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql2, FlowOrderTypeEnum.YiFanKui.getValue(), deliveryState.getDeliverystate(), newpaywayid, co.getCwb());
		if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) && (zhiliuremark.length() > 0)) {
			this.createFloworder(user, sessionbranchid, co, FlowOrderTypeEnum.YiFanKui, (reason.getReasoncontent() == null ? "" : reason.getReasoncontent()) + " " + zhiliuremark, System
					.currentTimeMillis(), scancwb, false);
		} else {
			this.createFloworder(user, sessionbranchid, co, FlowOrderTypeEnum.YiFanKui, (reason.getReasoncontent() == null ? "" : reason.getReasoncontent()) + " " + deliverstateremark, System
					.currentTimeMillis(), scancwb, false);
		}

		// 反馈时更新订单的反馈的操作时间
		this.operationTimeDAO
				.creAndUpdateOperationTime(co.getCwb(), sessionbranchid, FlowOrderTypeEnum.YiFanKui.getValue(), deliveryState.getDeliverystate(), sessionbranchid, co.getCustomerid(), "", co
						.getEmaildate(), co.getCwbordertypeid(), co.getReceivablefee(), co.getPaybackfee());

		// 如果订单为快递单，那么需要调用tps轨迹上传接口
		BigDecimal freight = new BigDecimal(0);
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			int feedbakOperateType;
			if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue())) { // 配送成功
				feedbakOperateType = FeedbackOperateTypeEnum.SignInScan.getValue();
				freight = co.getShouldfare();
			} else if ((podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {// 分站滞留
				feedbakOperateType = FeedbackOperateTypeEnum.RetensionScan.getValue();
			} else if ((podresultid == DeliveryStateEnum.DaiZhongZhuan.getValue())) {// 待中转
				feedbakOperateType = FeedbackOperateTypeEnum.ExceptionScan.getValue();
			} else if ((podresultid == DeliveryStateEnum.JuShou.getValue())) {// 拒收
				feedbakOperateType = FeedbackOperateTypeEnum.JuShou.getValue();
			} else if ((podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue())) {// 丢失
				feedbakOperateType = FeedbackOperateTypeEnum.ExceptionScan.getValue();
			} else {// 除了上面这些之外，都是不应该出现的状态
				feedbakOperateType = FeedbackOperateTypeEnum.ExceptionScan.getValue();
			}
			co.setNewpaywayid(newpaywayid + "");
			
			//Modified by leoliao at 2016-07-25 完善快递单备注信息
			String expressReason = "";
			if(reason != null && reason.getReasoncontent() != null && !reason.getReasoncontent().trim().equals("")){
				expressReason = reason.getReasoncontent().trim() + " " + deliverstateremark;
			}else{
				expressReason = (deliverstateremark==null?"":deliverstateremark.trim());
			}
			if(zhiliuremark != null && !zhiliuremark.trim().equals("")){
				expressReason += " " + zhiliuremark;
			}
			this.executeTpsTransInterface(co, deliveryUser, feedbakOperateType, expressReason.trim(), sign_man, freight.doubleValue());
			//Modified end
		}
		//Added by leoliao at 2016-03-22 归班审核后清除集包一票多件对应的处理记录
		try {
			if (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
				List<String> listYpdjCwb = new ArrayList<String>();
				listYpdjCwb.add(cwb);
				this.ypdjHandleRecordDAO.delYpdjHandleRecordByCwbs(listYpdjCwb);
			}
		} catch (Exception ex) {
			CwbOrderService.logger.error("归班反馈后清除集包一票多件对应的处理记录出错!", ex);
		}
		//Added end
		//add by neo01.huang，2016-5-26，小件员归班反馈后，清除一票多件缺件记录
		this.ypdjHandleRecordDAO.delYpdjHandleRecordByCwb(co.getCwb());
		CwbOrderService.logger.info("归班反馈->清除缺件记录,订单号:{}", co.getCwb());

		//配送拒收处理关联上门退订单    上门退未领货更新领货标识为不能领货，上门退已领货通知tps取消绑定关系   2016-6-17 ---> 2016-07-08 修改为审核才处理
		//if (CwbOrderTypeIdEnum.Peisong.getValue() == co.getCwbordertypeid() && podresultid == DeliveryStateEnum.JuShou.getValue()) {
		//	handlePeiSongCwbDeliveryPermit(co, podresultid);
		//}
		
		CwbOrderService.logger
				.info("进入单票反馈cwborderservice处理结束跳出cwborderservice！cwb:" + co.getCwb() + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--receivedfeecash:" + receivedfeecash + "--receivedfeepos:" + receivedfeepos + "--receivedfeecheque:" + receivedfeecheque + "--receivedfeeother:" + receivedfeeother);
		return map;
	}

	public void validatorVipshopSMT(String cwb, CwbOrder co) {
		CwbOrder allcwbOrder = this.cwbDAO.getCwbByCwbIgnoreCaseLose(cwb);
		if (allcwbOrder == null) {
			return;
		}
		String enumKey = this.getB2cEnumKeys(this.customerDAO.getCustomerById(allcwbOrder.getCustomerid()), "vipshop");
		if ((enumKey != null) && (allcwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			if (allcwbOrder.getCwb().contains("-T")) {

				// 如果当前订单被失效，那么自动检查其他关联单号
				if (allcwbOrder.getState() == 0) {
					this.notifyOtherCwbsSMT(cwb, allcwbOrder);
				}
				if (co == null) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
				}
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(co.getCwb().substring(0, co.getCwb().indexOf("-T"))); // 去掉-T拿到之前的配送单
				if (cwbOrder == null) {
					return;
				}
				if (!((cwbOrder.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (cwbOrder.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()) || (cwbOrder
						.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()))) {

					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.PeisongDan_vipshop_weiwanjie, co.getCwb());
				}
			}
		}
	}

	/**
	 * 智能提醒其他单号
	 *
	 * @param cwb
	 * @param allcwbOrder
	 */
	private void notifyOtherCwbsSMT(String cwb, CwbOrder allcwbOrder) {
		String newCwb = ""; // 这里需要遍历
		String subCwb = allcwbOrder.getCwb().substring(0, allcwbOrder.getCwb().indexOf("-T"));
		for (int i = 1; i < 10; i++) { // 模拟最多到T9
			newCwb = subCwb + "-T" + i;
			if (allcwbOrder.getCwb().equals(newCwb)) {
				continue;
			}
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(newCwb);
			if (cwbOrder != null) {
				newCwb = cwbOrder.getCwb();
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Shangmentui_vipshop_shixiao, allcwbOrder.getCwb(), newCwb);
			}
		}

	}

	public String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum() != null && customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					return String.valueOf(enums.getKey());
				}
			}
		}
		return null;
	}

	/**
	 * 更新当前反馈状态需要指定订单的下一站
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(CwbOrder order, User user, int feedbakOperateType, String reason, String signMan, double freight) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo(order.getCwb());
		transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());// tps机构编码
		transNoFeedBack.setOperater(user.getRealname());
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(feedbakOperateType);
		transNoFeedBack.setExceptionReason(reason);
		transNoFeedBack.setSignMan(signMan);
		if (feedbakOperateType == FeedbackOperateTypeEnum.SignInScan.getValue()) {
			transNoFeedBack.setActualFee(freight);
			transNoFeedBack.setActualPayType(order.getNewpaywayid() + "");
			//logger.info("==========="+order.getNewpaywayid()+"==============");
		}

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
		String isZhiLiuZhongZhuanDaoKuFand = this.systemInstallDAO.getSystemInstall("isZhiLiuZhongZhuanDaoKuFand") == null ? "no" : this.systemInstallDAO
				.getSystemInstall("isZhiLiuZhongZhuanDaoKuFand").getValue();

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
				CwbOrderService.logger.info("站点{0}没有指定退货站", user.getBranchid());
				return;
			}
			if (dse == DeliveryStateEnum.JuShou) {// 品信退货是否需要审核开关
				// String tuihuocheck =
				// this.systemInstallDAO.getSystemInstallByName("tuihuocheck")
				// == null ? "no" :
				// this.systemInstallDAO.getSystemInstallByName("tuihuocheck").getValue();
				if (/* "no".equals(tuihuocheck) && */!chechFlag) {// 更改下一站为退货站
					this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
				}
			} else {
				this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
			}
		} else if (dse == DeliveryStateEnum.FenZhanZhiLiu) {// 需要中转
			if (zhongzhuanNextBranch == null) {
				CwbOrderService.logger.info("站点{0}没有指定退货站", user.getBranchid());
				return;
			}
			// this.cwbDAO.updateNextBranchid(cwb,
			// zhongzhuanNextBranch.getBranchid());
			this.cwbDAO.updateNextBranchid(cwb, co.getStartbranchid());

		} else if (dse == DeliveryStateEnum.DaiZhongZhuan) {// 待中转
			if (zhongzhuanNextBranch == null) {
				CwbOrderService.logger.info("站点{0}没有指定中转站", user.getBranchid());
				return;
			}
			this.cwbDAO.updateNextBranchid(cwb, zhongzhuanNextBranch.getBranchid());

		} else {// 其他的反馈结果，都将下一站置为0
			this.cwbDAO.updateNextBranchid(cwb, 0L);
		}
	}

	// 更改订单的订单状态为退货的流向
	private void deliverPodForCwbstate(String cwb, long podresultid, FlowOrderTypeEnum auditFlowOrderTypeEnum, User user) {
		// ====退货审核===
		/*
		 * OrderBackCheck
		 * orderBackCheck=orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
		 * if(orderBackCheck!=null){//如果存在先删除
		 * this.orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb); }
		 */
		Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;

		Branch tuihuoNextBranch = null;
		if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenTuiChengGong
				.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue())) {
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
				// chechFlag (退货是否需要审核的标识 0：否 ,1：是)
				if (chechFlag) {
					// this.updateCwbState(cwb, CwbStateEnum.PeiShong);
					// 获取订单信息
					CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
					// 退货审核表插入一条订单数据
					OrderBackCheck orderBackCheck = this.orderBackCheckDAO.getOrderBackCheckByCheckstate(cwb);
					if ((orderBackCheck == null) && ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()))) {
						OrderBackCheck o = new OrderBackCheck();
						if (podresultid == DeliveryStateEnum.JuShou.getValue()) {
							o = this.orderBackCheckService.loadFormForOrderBackCheck(co, co.getDeliverybranchid(), user.getUserid(), 1, DeliveryStateEnum.JuShou.getValue());
						} else if (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
							o = this.orderBackCheckService.loadFormForOrderBackCheck(co, co.getDeliverybranchid(), user.getUserid(), 1, DeliveryStateEnum.BuFenTuiHuo.getValue());
						}
						this.orderBackCheckDAO.createOrderBackCheck(o);
					}

					/*
					 * for (long i :
					 * this.cwbRouteService.getNextPossibleBranch(user
					 * .getBranchid())) {
					 * bList.add(this.branchDAO.getBranchByBranchid(i)); }
					 */

					/*
					 * //Branch tuihuoNextBranch = null; for (Branch b : bList)
					 * {// 获得当前站点的退货站 if (b.getSitetype() ==
					 * BranchEnum.TuiHuo.getValue()) { tuihuoNextBranch = b; } }
					 */
					if (tuihuoNextBranch == null) {
						tuihuoNextBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getTuihuoid());
					} else {
						// 更改下一站为退货站
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
					}
					//Commented by leoliao at 2016-03-24
					//this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
					//Added by leoliao at 2016-03-24 退货需要审核则订单状态要保留为配送(已向蓝生确认)
					//this.updateCwbState(cwb, CwbStateEnum.PeiShong);
					this.cwbDAO.updateCwbState(cwb, CwbStateEnum.PeiShong);//modify by vic.liang 2016/05/03 审核后偶尔出现订单状态没有修改成功，this.updateCwbState -->this.cwbDAO.updateCwbState 尝试去掉updateCwbState的事务
					CwbOrderService.logger.info("退货审核：订单{}，修改为配送状态", new Object[] { cwb });

				} else {
					/*for (long i : this.cwbRouteService.getNextPossibleBranch(user.getBranchid())) {
						bList.add(this.branchDAO.getBranchByBranchid(i));
					}

					for (Branch b : bList) {// 获得当前站点的退货站
						if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
							tuihuoNextBranch = b;
						}
					}*///重复代码
					if (tuihuoNextBranch == null) {
						tuihuoNextBranch = this.branchDAO.getBranchByBranchid(user.getBranchid());
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getTuihuoid());
					} else {
						// 更改下一站为退货站
						this.cwbDAO.updateNextBranchid(cwb, tuihuoNextBranch.getBranchid());
					}
					//this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
					this.cwbDAO.updateCwbState(cwb, CwbStateEnum.TuiHuo);//modify by vic.liang 2016/05/03 审核后偶尔出现订单状态没有修改成功，this.updateCwbState -->this.cwbDAO.updateCwbState 尝试去掉updateCwbState的事务
					CwbOrderService.logger.info("退货审核：订单{}，修改为退货状态", new Object[] { cwb });
				}

			} else {
				this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			}
		} else if (podresultid == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			this.updateCwbState(cwb, CwbStateEnum.DiuShi);
			// added by songkaojun 2016-02-02 对于反馈为"货物丢失"的新一票多件模式订单,将运单也设为“丢失”
			if (this.mpsCommonService.isNewMPSOrder(cwb)) {
				List<String> transCwbList = this.transCwbDetailDAO.getTransCwbListByCwb(cwb);
				for (String transCwb : transCwbList) {
					this.transCwbDetailDAO.updateTransCwbStateByTranscwb(transCwb, TransCwbStateEnum.DIUSHI.getValue());
				}
			}
		}
		// 处理站点
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?, currentbranchid=0 where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
		} else if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			this.jdbcTemplate
					.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid,nextbranchid=? where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), tuihuoNextBranch
							.getBranchid(), cwb);
		} else {
			//this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
			//update by neo01.huang，2016-5-30
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			CwbOrderService.logger.info("归班审核->更新站点信息->站点id信息：startbranchid:{}, currentbranchid:{}, nextbranchid:{}, deliverybranchid:{}, podresultid:{}", cwbOrder.getStartbranchid(), cwbOrder
					.getCurrentbranchid(), cwbOrder.getNextbranchid(), cwbOrder.getDeliverybranchid(), podresultid);
			//==>如果startbranchid不为0，则走原来正常逻辑
			if (cwbOrder.getStartbranchid() != 0) {
				CwbOrderService.logger.info("归班审核->更新站点信息->如果startbranchid不为0，则走原来正常逻辑");
				this.jdbcTemplate.update("update express_ops_cwb_detail set flowordertype=?,currentbranchid=startbranchid where cwb=? and state=1", auditFlowOrderTypeEnum.getValue(), cwb);
			} else { //==>startbranchid为0
				CwbOrderService.logger.info("归班审核->更新站点信息->startbranchid为0");
				long deliverybranchid = cwbOrder.getDeliverybranchid(); // 配送站点
				//==>deliverybranchid不为0，则用deliverybranchid赋值给startbranchid、currentbranchid
				if (deliverybranchid != 0) {
					CwbOrderService.logger.info("归班审核->更新站点信息->deliverybranchid不为0，则用deliverybranchid赋值给startbranchid、currentbranchid");
					this.jdbcTemplate
							.update("update express_ops_cwb_detail set flowordertype=?, startbranchid=deliverybranchid, currentbranchid=deliverybranchid where cwb=? and state=1", auditFlowOrderTypeEnum
									.getValue(), cwb);
				} else { //==>deliverybranchid为0，则用当前用户的branchid赋值给startbranchid、currentbranchid、deliverybranchid
					CwbOrderService.logger.info("归班审核->更新站点信息->deliverybranchid为0，则用当前用户的branchid赋值给startbranchid、currentbranchid、deliverybranchid，当前用户的branchid为：{}", user.getBranchid());
					deliverybranchid = user.getBranchid();
					long startbranchid = deliverybranchid; // 上一个机构id
					long currentbranchid = deliverybranchid; // 当前机构
					this.jdbcTemplate
							.update("update express_ops_cwb_detail set flowordertype=?, startbranchid=?, currentbranchid=?, deliverybranchid=? where cwb=? and state=1", auditFlowOrderTypeEnum
									.getValue(), startbranchid, currentbranchid, deliverybranchid, cwb);
				}
			}
		}
	}

	@Transactional
	public void updateCwbState(String cwb, CwbStateEnum state) {
		this.cwbDAO.updateCwbState(cwb, state);
		this.updateNextBranchId(cwb);
		CwbOrderService.logger.info("订单cwbstate修改为：{}", new Object[] { state.getText() });
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
		
		logger.info("CwbOderService.posPay pay_type={},posremark={},cash={},pos={},check={}", pay_type, posremark, cash, pos, check);

		// 执行支付的方法
		if ((co.getFlowordertype() != CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()) && (co.getFlowordertype() != CwbFlowOrderTypeEnum.YiFanKui.getValue()) && (co.getFlowordertype() != CwbFlowOrderTypeEnum.YiZhiFu
				.getValue()) && (co.getFlowordertype() != CwbFlowOrderTypeEnum.CheXiaoFanKui.getValue())) {
			this.receiveGoods(this.userDAO.getUserByUserid(deliveryid), this.userDAO.getUserByUserid(deliveryid), cwb, cwb);
		}
		User user = this.userDAO.getUserByUserid(deliveryid);

		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		this.deliveryStateDAO
				.saveForReFanKui(cwb, deliveryid, feebackAmount, new BigDecimal(0), Receivablefee, deliverystate, cash, pos, posremark, check, checkremark, new BigDecimal(0), 0, deliverstateremark, DateTimeUtil
						.getNowTime(), 0, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO);

		this.deliveryCashDAO.saveDeliveryCashForDSById(DateTimeUtil.getNowTime(), new BigDecimal(0), cash.add(check), pos, deliverystate == 0 ? ds.getDeliverystate() : deliverystate, ds.getId());

		String sql2 = "update express_ops_cwb_detail set flowordertype=?,deliverid=?,deliverybranchid=?,excelbranch=?,newpaywayid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql2, CwbFlowOrderTypeEnum.YiZhiFu.getValue(), deliveryid, user.getBranchid(), branch.getBranchname(), newpaywayid, co.getCwb());

		String sql3 = "update express_ops_delivery_state set deliveryid=?,deliverybranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql3, deliveryid, user.getBranchid(), co.getCwb());

		this.createFloworder(user, user.getBranchid(), cwb, FlowOrderTypeEnum.PosZhiFu, "POS支付-" + posremark, System.currentTimeMillis(), cwb);

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
			CwbOrderService.logger.error("运单支付,没有检索到数据" + cwb + ",小件员：" + deliverid);
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		if (cwbOrder.getReceivablefee().compareTo(ds.getReceivedfee()) == 0) {
			CwbOrderService.logger.error("[运单支付]该订单已支付,当前订单号：" + cwb + ",小件员：" + deliverid);
			throw new CwbException(cwb, FlowOrderTypeEnum.PosZhiFu.getValue(), ExceptionCwbErrorTypeEnum.DingDanYiZhiFu);
		}
		if ((ds.getBusinessfee().doubleValue() != payamount) && !"split".equals(acq_type)) {
			CwbOrderService.logger.error("[运单支付]支付金额有误,当前订单号：" + cwb + ",小件员：" + deliverid);
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
		this.createFloworder(user, branchid, cwb, FlowOrderTypeEnum.CheXiaoFanKui, "POS撤销", System.currentTimeMillis(), cwb);

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
	public String deliverAuditok(User user, String subTrStr, String okTime, String subAmount, String subAmountPos, long deliverealuser, GotoClassOld gotoClassOld, int deliverpayuptype, BigDecimal deliverpayupamount, String deliverpayupbanknum, String deliverpayupaddress, BigDecimal deliverpayupamount_pos, BigDecimal deliverAccount, BigDecimal deliverPosAccount) {
		/*
		 * if(0<deliveryStateDAO.getIsRepeat(subTrStr)){ return
		 * "{\"errorCode\":1,\"error\":\"禁止审核已审核的订单，请刷新后重试\"}"; }
		 */
		CwbOrderService.logger.info("ok:subTrStr {} | subAmount {} | subAmountPos {}", new Object[] { subTrStr, subAmount, subAmountPos });

		CwbOrderService.logger.info("用户:{},所属站点:{},开始创建归班记录,金额为{},pos为{},包含{}", new Object[] { user.getUserid(), user.getBranchid(), subAmount, subAmountPos, subTrStr });
		//Added by leoliao at 2016-06-01 解决从session中获取User的branchid为0的情况
		if (user.getBranchid() <= 0) {
			CwbOrderService.logger.info("(CwbOrderService.deliverAuditok) branch <= 0,重新从数据库取!");
			try {
				List<User> listUser = this.userDAO.getUserByid(user.getUserid());
				if ((listUser != null) && (listUser.size() > 0) && (listUser.get(0) != null)) {
					long branchidTmp = listUser.get(0).getBranchid();
					user.setBranchid(branchidTmp);
					CwbOrderService.logger.info("重新从数据库取的branchid={}!", branchidTmp);
				}
			} catch (Exception ex) {
				CwbOrderService.logger.error("重新从数据库取branchid出错", ex);
				ex.printStackTrace();
			}
		}
		//Added end

		Boolean isUseDeliverPayUp = false;// 用于判断是否使用小件员交款功能
		try {
			SystemInstall usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");
			if (usedeliverpay.getValue().equals("yes")) {
				isUseDeliverPayUp = true;
			}
		} catch (Exception e) {
			CwbOrderService.logger
					.error("获取使用小件员交款功能异常, deliverpayuptype:{} deliverpayupamount:{} deliverpayupbanknum:{} deliverpayupaddress:{}", new Object[] { deliverpayuptype, deliverpayupamount, deliverpayupbanknum, deliverpayupaddress });
			CwbOrderService.logger.error("", e);
		}
		long gcaId = 0L;
		if (isUseDeliverPayUp) {// 使用小件员交款功能
			// 锁住事物，获得小件员帐户信息
			User uPayupDeliver = this.userDAO.getUserByUseridLock(deliverealuser);
			// 计算欠款
			BigDecimal deliverpayuparrearage = deliverpayupamount.add(deliverAccount).subtract(new BigDecimal(subAmount));
			BigDecimal deliverpayuparrearage_pos = deliverpayupamount_pos.add(deliverPosAccount).subtract(new BigDecimal(subAmountPos));
			// 处理归班信息
			gcaId = this.gotoClassAuditingDAO
					.creGotoClassAuditingAndDeliverPayUp(okTime, subAmount, subAmountPos, user.getUserid(), user.getBranchid(), deliverealuser, deliverpayuptype, deliverpayupamount, deliverpayupbanknum, deliverpayupaddress, deliverpayuparrearage, deliverpayupamount_pos, deliverpayuparrearage_pos, deliverAccount, deliverPosAccount, -1);
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
			CwbOrderService.logger.info("小件员交款产生交易：{}", fdpud.toString());
			this.financeDeliverPayUpDetailDAO.insert(fdpud);
			// 处理小件员帐户
			uPayupDeliver.setDeliverAccount(deliverpayuparrearage);
			uPayupDeliver.setDeliverPosAccount(deliverpayuparrearage_pos);
			this.userDAO.updateUserAmount(uPayupDeliver.getUserid(), uPayupDeliver.getDeliverAccount(), uPayupDeliver.getDeliverPosAccount());

		} else {// 不使用小件员交款功能
			gcaId = this.gotoClassAuditingDAO.creGotoClassAuditing(okTime, subAmount, subAmountPos, user.getUserid(), user.getBranchid(), deliverealuser);
		}

		CwbOrderService.logger.info("开始更新归班记录订单,id:{},cwbs:{}", gcaId, subTrStr);
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
			this.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.YiShenHe, "", System.currentTimeMillis(), cwb, false);
			// 当订单归班审核配送成功和上门退拒退 和 货物丢失状态时，删除操作时间记录
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) || (deliverystate
					.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue())) {
				this.operationTimeDAO.delOperationTime(co.getCwb());
			} else {// 如果不是最终，则更新跟踪记录
				this.operationTimeDAO.updateOperationTime(cwb, user.getBranchid(), FlowOrderTypeEnum.YiShenHe.getValue(), deliverystate.getDeliverystate(), co.getNextbranchid());
			}
			
			//配送拒收处理关联上门退订单    上门退未领货更新领货标识为不能领货，上门退已领货通知tps取消绑定关系   2016-6-17 ---> 2016-07-08 修改为审核才处理 vic.liang@pjbest.com
			this.handlePeiSongCwbDeliveryPermit(co, deliverystate.getDeliverystate());
			//2016-07-08 修改为审核才处理 end 

			amount = amount.add(deliverystate.getCash()).add(deliverystate.getCheckfee()).add(deliverystate.getOtherfee()).subtract(deliverystate.getReturnedfee()).add(deliverystate.getInfactfare());
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
					this.posPayDAO
							.save_PosTradeDetailRecord(cwb, "POS手工反馈", deliverystate.getPos().doubleValue(), deliverystate.getDeliveryid(), PaytypeEnum.Pos.getValue(), "系统中反馈为POS刷卡", deliverystate
									.getSign_man(), 1, "", 1, 1, "single", PosEnum.XitongFanKui.getMethod(), 1, "");
				}

			}
			// =======OXO配送状态逻辑: by jinghui.pan@pjbest.com
			// ==== 如果订单的配送状态为'配送成功'，更新配送状态为'已处理'=====
			if (deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) {
				this.updateOXODeliveryState(co, CwbOXOStateEnum.Processed);
			}

			// =======结算逻辑==========
			// 如果订单为配送成功、上门退、上门换则记录订单号
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) || (deliverystate
					.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
				accountCwbs = accountCwbs.append("'").append(cwb).append("',");
			}

			if (((co.getShouldfare().compareTo(BigDecimal.ZERO) >= 0) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) || ((co.getInfactfare()
					.compareTo(BigDecimal.ZERO) > 0) && (co.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()))) {
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
				CwbOrderService.logger.info("列入上门退运费表记录中,cwb={},userid={},shouldfee={},infactfare={}", new Object[] { co.getCwb(), deliverystate.getDeliveryid(), co.getShouldfare(), deliverystate
						.getInfactfare() });
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
			if ((changealowflag == 1) && (co.getDeliverystate() == DeliveryStateEnum.DaiZhongZhuan.getValue())) {// 要中转申请，就自动插入一条数据
				OrderFlow of = this.orderFlowDAO.getOrderFlowCwb(cwb);
				CwbApplyZhongZhuan cwbApplyZhongZhuan = new CwbApplyZhongZhuan();
				cwbApplyZhongZhuan.setApplybranchid(user.getBranchid());
				cwbApplyZhongZhuan.setApplytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				cwbApplyZhongZhuan.setApplyzhongzhuanbranchid(co.getDeliverybranchid());// 配送站点
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

			/**
			 * added by zhouguoting 2015/11/08 如果审核状态为“配送成功”，“上门退成功”，“上门换成功”。
			 * 如果之前有生成过站内账单（即之前有做过反馈审核，但被重置反馈了），再次做反馈审核后，需要站内调整记录
			 */
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (deliverystate
					.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				this.orgBillAdjustmentRecordService.createAdjustment4GoToClassConfirm(co, deliverystate);
//				this.fnDfAdjustmentRecordService.createAdjustment4GoToClassConfirm(co);//派费调整记录
			}

			/**
			 * added by zhouguoting 2015/11/25
			 * 如果审核状态为“配送成功”，如果结算类型是“返款结算”的客户的订单，
			 * 且之前已经生成过“应付配送货款账单”,需要生成配送货款调整记录，配送货款调整金额= 应收金额。
			 */
			if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue())) {

				this.adjustmentRecordService.createAdjustment4GoToClassConfirm(co, deliverystate);
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
							CwbOrderService.logger.info("===开始创建买单结算POS数据===");
							AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
							accountCwbDetail = this.accountCwbDetailService.formForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), user.getUserid(), co
									.getStartbranchid());
							// accountCwbDetail=this.loadFormForAccountCwbDetail(co,co.getStartbranchid(),AccountFlowOrderTypeEnum.Pos.getValue(),user,co.getStartbranchid());
							this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
							CwbOrderService.logger.info("用户:{},创建买单记录,站点:{},订单号:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), co.getCwb() });
						}
					}

					// 配送结果结算
					if (startbranch.getAccounttype() == 2) {
						AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
						accountCwbDetail = this.loadFormForAccountCwbDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.GuiBanShenHe.getValue(), user, co.getStartbranchid());
						this.accountCwbDetailDAO.createAccountCwbDetail(accountCwbDetail);
						CwbOrderService.logger.info("用户:{},创建结算归班审核记录,站点:{},订单id:{}", new Object[] { user.getRealname(), startbranch.getBranchname(), co.getCwb() });
					}
					// 扣款结算
					if (startbranch.getAccounttype() == 3) {
						DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
						BigDecimal fee = new BigDecimal("0");
						fee = deliverystate.getPos();
						if (fee.compareTo(new BigDecimal("0")) > 0) {
							CwbOrderService.logger.info("===开始创建扣款结算POS数据===");
							AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
							accountDeducDetail = this.accountDeducDetailService
									.loadFormForAccountDeducDetail(co, co.getStartbranchid(), AccountFlowOrderTypeEnum.Pos.getValue(), fee, user.getUserid(), "", 0, 0);
							long id = this.accountDeducDetailDAO.createAccountDeducDetail(accountDeducDetail);
							CwbOrderService.logger.info("用户:{},创建扣款结算POS：站点{},代收货款{}元,id：{}", new Object[] { user.getRealname(), startbranch.getBranchname(), fee, id });
						}
					}
				}
			}
		}

		CwbOrderService.logger.info("比对归班总金额,amount:{} amount_pos:{} subAmount:{} subAmountPos:{}", new Object[] { amount, amount_pos, subAmount, subAmountPos });
		if ((amount.compareTo(new BigDecimal(subAmount)) != 0) || (amount_pos.compareTo(new BigDecimal(subAmountPos)) != 0)) {
			throw new ExplinkException("您审核的订单中存在被改动订单，请刷新后重新提交审核");
		}
		CwbOrderService.logger.info("开始创建旧记录,id:{}", gcaId);
		gotoClassOld.setGotoclassauditingid(gcaId);
		this.gotoClassOldDAO.creGotoClassOld(gotoClassOld);
		GotoClassAuditing gotoClassAuditingByGcaid = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(gcaId);
		this.okJMS(gotoClassAuditingByGcaid);
		return "{\"errorCode\":0,\"error\":\"处理成功\"}";
	}

	/*
	 * public static void main(String[] args) { Date date = new Date();
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * String str = sdf.format(date); logger.info(str); }
	 */

	/**
	 * 退供货商出库
	 *
	 * @param co
	 * @param customerid
	 * @return
	 */
	public CwbOrder backtocustom(User user, String cwb, String scancwb, long requestbatchno, String baleno, boolean anbaochuku) {
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.TuiGongYingShangChuKu);
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
	@Transactional
	public CwbOrder backtocustom(User user, String cwb, String scancwb, long requestbatchno, String baleno, boolean anbaochuku, long customerid) {
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.TuiGongYingShangChuKu);

		this.customerReleaseService.validateReleaseCondition(scancwb);
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		if (co.getCustomerid() != customerid) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.GONG_YING_SHANG_XUAN_ZE_CUO_WU, this.customerDAO
					.getCustomerById(co.getCustomerid()).getCustomername());

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
			//针对一票多件多个运单号的订单，如果一个运单号同样的机构下同样的环节重复扫描的验证
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), 0, user.getBranchid(), 0, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
			
			/* ***************add begin*********************/
			//add by neo01.huang，2016-7-12
			//校验退供货商出库是否到齐
			customerReleaseService.validateBackToCustomerAllArrived(cwb, user.getBranchid(), scancwb);
			/* ***************add end***********************/
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

		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, co.getCurrentbranchid(), 0L);
		
		//add by neo01.huang，2016-7-11，清除缺件记录
		ypdjHandleRecordDAO.delYpdjHandleRecordByCwb(cwb);
		logger.info("退供货商出库->清除缺件记录,订单号:{}", cwb);
		
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleBacktocustom(User user, String cwb, String scancwb, long requestbatchno, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, boolean anbaochuku) {
		this.validateCwbState(co, flowOrderTypeEnum);

		this.validateStateTransfer(co, flowOrderTypeEnum);

		this.validateTuihuoCheck(cwb, flowOrderTypeEnum); // 退货申请校验

		this.produceGroupDetail(user, cwb, requestbatchno, false, flowOrderTypeEnum.getValue(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(), 0, 0, "");

		String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,startbranchid=?,nextbranchid=?,cwbstate=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), 0, user.getBranchid(), 0, CwbStateEnum.TuiGongYingShang.getValue(), cwb);

		// ======按包出库时更新扫描件数为发货件数zs=====
		if (!anbaochuku) {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), co.getSendcarnum());
		}

		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis(), scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
		this.creOrderbackRecord(cwb, co.getCwbordertypeid(), co.getCustomerid(), co.getReceivablefee(), co.getEmaildate(), DateTimeUtil.getNowTime());
	}

	/**
	 * 退货申请相关流程校验 不通过或者待审核都不允许做操作
	 *
	 * @param cwb
	 * @param flowOrderTypeEnum
	 */
	private void validateTuihuoCheck(String cwb, FlowOrderTypeEnum flowOrderTypeEnum) {
		Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
		boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
		if (chechFlag) {
			OrderBackCheck obc = this.orderBackCheckDAO.getOrderBackCheckByCwb(cwb);
			if (obc != null) {
				if (obc.getCheckstate() == 1) { // 待审核
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Weishenhebuxuzhongzhuan, flowOrderTypeEnum.getText());
				}
				if (obc.getCheckresult() == 2) { // 审核不通过 ,站点配送
					throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuan, flowOrderTypeEnum.getText());
				}
			}
		}
	}

	// 新加---在退供应商出库操作时存入退供应商数据lx
	public void creOrderbackRecord(String cwb, int cwbordertypeid, long customerid, BigDecimal recievefee, String emaildate, String createtime) {
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
	public CwbOrder customrefusebackToStorehouse(User user, String cwb, String scancwb, long requestbatchno, String comment) {
		CwbOrderService.logger.info("{} 将订单 {} 审核为供货商拒收返库", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), cwb);
		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.GongYingShangJuShouFanKu);
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

	/**
	 * 供货商拒收退货
	 *
	 * @param co
	 * @return
	 */
	public CwbOrder customrefuseback(User user, String cwb, String scancwb, long requestbatchno, String comment) {
		//		logger.info("{} 将订单 {} 审核为供货商拒收返库", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), cwb);
		//		退供应商拒收退货
		CwbOrderService.logger.info("{} 将订单 {} 审核为退供应商拒收退货", FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue(), cwb);
		cwb = this.translateCwb(cwb);

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleCustomrefusebackYipiaoduojian(user, cwb, scancwb, requestbatchno, comment, co, FlowOrderTypeEnum.GongYingShangJuShouTuiHuo, isypdjusetranscwb);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleCustomrefuseback(user, cwb, scancwb, requestbatchno, comment, co, FlowOrderTypeEnum.GongYingShangJuShouTuiHuo, isypdjusetranscwb, false);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		return co;
	}

	private CwbOrder handleCustomrefusebackYipiaoduojian(User user, String cwb, String scancwb, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb) {

		if (isypdjusetranscwb == 1) {
			this.validateIsSubCwb(scancwb, co, flowOrderTypeEnum.getValue());
			//针对一票多件多个运单号的订单，如果一个运单号同样的机构下同样的环节重复扫描的验证
			this.validateCwbChongFu(co, scancwb, flowOrderTypeEnum.getValue(), user.getBranchid(), 0, 0, ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU);
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
			} else {
				throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_DAO_HUO);
			}
		} else {
			//针对一票多件 ，在"客户收退货确认"那里做了"拒收退货"操作后，订单主表的扫描件数被修改成1了，导致"退客户拒收返库"扫描操作时报异常
			if (co.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue()) {
				co.setScannum(co.getSendcarnum());
			}
			this.validateYipiaoduojianState(co, flowOrderTypeEnum, isypdjusetranscwb, false);
			this.handleCustomrefuseback(user, cwb, scancwb, requestbatchno, comment, co, flowOrderTypeEnum, isypdjusetranscwb, true);
		}
		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, co.getCurrentbranchid(), 0);
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private void handleCustomrefuseback(User user, String cwb, String scancwb, long requestbatchno, String comment, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj) {
		OrderbackRecord obre = null;
		if (flowOrderTypeEnum == FlowOrderTypeEnum.GongYingShangJuShouTuiHuo) {
			obre = this.orderbackRecordDao.getOBRecord(cwb);
		}
		if (flowOrderTypeEnum == FlowOrderTypeEnum.GongYingShangJuShouFanKu) {
			obre = this.orderbackRecordDao.getOBRecordForGongYingShangJuShouFanKu(cwb);
		}
		if (obre != null) {
			if (obre.getShenhestate() == 0) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.TUIKEHU_DAIQUEREN);
			} else if (obre.getShenhestate() == 1) {
				throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.TUIKEHU_SUCCESS);
			}
		}

		this.validateCwbState(co, flowOrderTypeEnum);
		this.validateStateTransfer(co, flowOrderTypeEnum);
		this.validateRepeatScan(co, user, flowOrderTypeEnum);
		String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
		String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[" + user.getRealname() + "]" + comment;
		try {
			String sql = "update express_ops_cwb_detail set flowordertype=?,currentbranchid=?,nextbranchid=?,cwbremark=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), user.getBranchid(), 0, newcwbremark, cwb);
		} catch (Exception e) {
			CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + co.getCwb() + "cwbremark:" + newcwbremark, e);
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		this.cwbDAO.updateScannum(co.getCwb(), 1);

		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, comment, System.currentTimeMillis(), scancwb, false);

		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, comment);
		}
	}

	/**
	 * 退供货商拒收反馈重复扫描判断
	 *
	 * @param co
	 * @param user
	 * @param flowOrderTypeEnum
	 */
	private void validateRepeatScan(CwbOrder co, User user, FlowOrderTypeEnum flowOrderTypeEnum) {
		if ((co.getCurrentbranchid() == user.getBranchid()) && (co.getFlowordertype() == flowOrderTypeEnum.getValue()) && (co.getScannum() >= 1)) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.CHONG_FU_DAO_HUO);
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
			CwbOrderService.logger.error("归班审核:sendJson:" + gotoClassAuditingByGcaid.toString(), ee);
		}
	}

	/**
	 *
	 * @param cwb
	 * @param operator
	 */
	public CwbOrder supplierBackSuccess(User user, String cwb, String scancwb, long flowordertype) {
		CwbOrderService.logger.info("{} 将订单 {} 改为供货商退货成功", flowordertype, cwb);

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

		this.mpsOptStateService.updateMPSInfo(scancwb, flowOrderTypeEnum, 0L, co.getCurrentbranchid(), 0);
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
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis(), scancwb, false);
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
		CwbOrderService.logger.info("{} 将 {} 订单拦截", flowOrderType, cwb);

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
		return this.handleTuihuoChack(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, reasonid);
	}

	@Transactional
	public CwbOrder tuihuoHandleVipshop(User user, String cwb, String scancwb, long reasonid) {

		CwbOrderService.logger.info("vipshop拦截订单{}", cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}
		long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			return this.handleTuihuoChack(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, reasonid);
		} else if ((co.getSendcarnum() == 1) || (co.getBackcarnum() == 1)) {
			this.handleTuihuoVipshop(user, cwb, scancwb, co, FlowOrderTypeEnum.DingDanLanJie, isypdjusetranscwb, false, reasonid);
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		// 删除打印表记录
		this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);

		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private CwbOrder handleTuihuoChack(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long reasonid) {
		if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
			this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
			co.setScannum(co.getScannum() + 1);
		} else {
			this.cwbDAO.updateScannum(co.getCwb(), 1);
		}
		if ((co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || ((co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui
				.getValue()) && (co.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())) || (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) ||
			(co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())) {
			//Modified by leoliao at 2016-06-24 修改下一站为退货站(增加出库扫描时也可以拦截并修改下一站)
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			
			boolean blnNeedUpdateCurrentBranch = true; //是否需要修改当前站为退货组
			long    nextInterceptBranchId      = 0; //订单拦截的下一站(根据站点的流向配置，找到对应的退货组)
			
			Branch currentBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid());
			if(co.getCurrentbranchid() == 0 || currentBranch == null){
				Branch startBranch = this.branchDAO.getBranchByBranchid(co.getStartbranchid());
				if(co.getStartbranchid() == 0 || startBranch == null){
					//如果上一站、当前站都为0，则表明订单处于导入状态，需要根据下一站找到对应的退货组
					CwbOrder cwbOrderNew = this.cwbDAO.getCwbByCwb(co.getCwb());
					long   nextBranchId = (cwbOrderNew==null?0 : cwbOrderNew.getNextbranchid());
					Branch nextBranch   = this.branchDAO.getBranchByBranchid(nextBranchId);
					if(nextBranchId == 0 || nextBranch == null){
						//下一站也为0
						blnNeedUpdateCurrentBranch = false;
						logger.error("订单(订单号={},下一站={})下一站为空", co.getCwb(), nextBranchId);
					}else{
						//根据站点的流向配置，找到对应的退货组
						nextInterceptBranchId = this.getNextInterceptBranchId(nextBranchId);
						if(nextInterceptBranchId <= 0){
							logger.error("订单(订单号={},下一站={})没有配置逆向退货组", co.getCwb(), nextBranch.getBranchname());
							//throw new Exception("订单[订单号="+co.getCwb()+",下一站="+nextBranch.getBranchname()+"]没有配置逆向退货组");
						}
					}
				}else if(startBranch.getSitetype() != BranchEnum.TuiHuo.getValue()){
					//根据站点的流向配置，找到对应的退货组
					nextInterceptBranchId = this.getNextInterceptBranchId(startBranch.getBranchid());
					if(nextInterceptBranchId <= 0){
						logger.error("订单(订单号={},上一站={})没有配置逆向退货组", co.getCwb(), startBranch.getBranchname());
						//throw new Exception("订单[订单号="+co.getCwb()+",上一站="+startBranch.getBranchname()+"]没有配置逆向退货组");
					}
				}else if(startBranch.getSitetype() == BranchEnum.TuiHuo.getValue()){
					nextInterceptBranchId = startBranch.getBranchid();
				}
			}else if(currentBranch.getSitetype() != BranchEnum.TuiHuo.getValue()){
				//根据站点的流向配置，找到对应的退货组
				nextInterceptBranchId = this.getNextInterceptBranchId(currentBranch.getBranchid());
				if(nextInterceptBranchId <= 0){
					logger.error("订单(订单号={},当前站={})没有配置逆向退货组", co.getCwb(), currentBranch.getBranchname());
					//throw new Exception("订单[订单号="+co.getCwb()+",当前站="+currentBranch.getBranchname()+"]没有配置逆向退货组");
				}
			}else if(currentBranch.getSitetype() == BranchEnum.TuiHuo.getValue()){
				//当前branch是退货组不用变下一站
				blnNeedUpdateCurrentBranch = false;
				logger.info("订单(订单号={},当前站={})当前站是退货组不用修改下一站", co.getCwb(), currentBranch.getBranchname());
			}
			
			logger.info("订单拦截：订单(订单号={}),blnNeedUpdateCurrentBranch={},nextInterceptBranchId={})", co.getCwb(), blnNeedUpdateCurrentBranch, nextInterceptBranchId);
			
			if(blnNeedUpdateCurrentBranch && nextInterceptBranchId != 0){
				//修改订单下一站为退货组
				String sql = "update express_ops_cwb_detail set nextbranchid=? where cwb=? and state=1";
				this.jdbcTemplate.update(sql, nextInterceptBranchId, cwb);
				
				//修改订单反馈状态为拒收
				this.deliveryStateDAO.updateDeliveryStateValue(cwb, DeliveryStateEnum.JuShou.getValue());
				
				logger.info("订单拦截：修复订单(订单号={})反馈状态为{}", co.getCwb(), DeliveryStateEnum.JuShou.getText());
			}
			//Modified end
		} else if (!((co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.RuKu
				.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()))) {
			throw new CwbException(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, FlowOrderTypeEnum.getText(co.getFlowordertype()).getText(), FlowOrderTypeEnum.DingDanLanJie
					.getText());
		}
		Reason r = this.reasonDAO.getReasonByReasonid(reasonid);
		String sql = "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r == null ? "" : r.getReasoncontent(), r == null ? 0 : r.getReasonid(), cwb);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r == null ? "" : r.getReasoncontent(), System.currentTimeMillis(), scancwb, false);
		this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);
		if ((co.getTranscwb() != null) && (co.getTranscwb().length() > 0)) {
			String[] cwbList = co.getTranscwb().split(",");
			String cwbs = "'" + cwb + "',";
			if ((cwbList == null) || (cwbList.length == 0)) {
				this.baleCwbDAO.deleteByCwbs(cwb);
			} else {
				for (String tr : cwbList) {
					cwbs += "'" + tr + "',";
					if (isypdjusetranscwb == 1) {
						this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, tr, flowOrderTypeEnum, "");
					}
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
				this.baleCwbDAO.deleteByCwbs(cwbs);
			}
			// 删除包关联表
		}
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	/**
	 *
	 * @Title: handleInterceptChack
	 * @description 一票多件拦截后，主单的变化
	 * @author 刘武强
	 * @date 2016年1月11日下午8:19:22
	 * @param @param user
	 * @param @param cwb
	 * @param @param scancwb
	 * @param @param co
	 * @param @param flowOrderTypeEnum
	 * @param @param isypdjusetranscwb
	 * @param @param reasonid
	 * @param @return
	 * @return CwbOrder
	 * @throws
	 */
	private CwbOrder handleInterceptChack(User user, String cwb, String scancwb, CwbOrder co, long isypdjusetranscwb, Reason r) {
		if (!this.mpsCommonService.isNewMPSOrder(cwb)) {
			if ((co.getSendcarnum() > co.getScannum()) || (co.getBackcarnum() > co.getScannum())) {
				this.cwbDAO.updateScannum(co.getCwb(), co.getScannum() + 1);
				co.setScannum(co.getScannum() + 1);
			} else {
				this.cwbDAO.updateScannum(co.getCwb(), 1);
			}
		}
		this.cwbDAO.updateCwbOnIntercept(cwb, co.getCwbstate(), co.getFlowordertype(), co.getNextbranchid(), r.getReasonid(), r.getReasoncontent(), co.getMpsoptstate(), co.getCurrentbranchid());
		this.createFloworder(user, user.getBranchid(), co, FlowOrderTypeEnum.getByValue((int) co.getFlowordertype()), r.getReasoncontent(), System.currentTimeMillis(), scancwb, false);
		this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);
		List<TransCwbDetail> transCwbList = this.transCwbDetailDAO.getTransCwbDetailListByCwb(scancwb);

		if ((co.getTranscwb() != null) && (co.getTranscwb().length() > 0)) {
			String[] cwbList = co.getTranscwb().split(",");
			String cwbs = "'" + cwb + "',";
			if ((cwbList == null) || (cwbList.length == 0)) {
				this.baleCwbDAO.deleteByCwbs(cwb);
			} else {
				for (TransCwbDetail temp : transCwbList) {
					String tr = temp.getTranscwb();
					cwbs += "'" + tr + "',";
					if (isypdjusetranscwb == 1) {
						this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, tr, FlowOrderTypeEnum.getByValue((int) co.getFlowordertype()), temp.getCommonphrase() == null ? "" : temp
								.getCommonphrase());
					}
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
				this.baleCwbDAO.deleteByCwbs(cwbs);
			}
			// 删除包关联表
		}
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
		if (((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe
				.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) || (co.getCwbstate() == CwbStateEnum.TuiHuo
				.getValue())) && !(((co.getSendcarnum() > 0) || (co.getBackcarnum() > 0)) && (co.getTranscwb().length() > 0) && !co.getCwb().equals(co.getTranscwb()) && (co.getFlowordertype() != FlowOrderTypeEnum.DingDanLanJie
				.getValue()))) {

		} else {
			Reason r = this.reasonDAO.getReasonByReasonid(reasonid);

			String sql = "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r == null ? "" : r.getReasoncontent(), r == null ? 0 : r.getReasonid(), cwb);
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r == null ? "" : r.getReasoncontent(), System.currentTimeMillis(), scancwb, false);
			if ((isypdjusetranscwb == 1) && isypdj) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
			}
		}
	}

	/**
	 * 唯品会上门揽退订单拦截
	 *
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
		if (((co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) || ((co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu
				.getValue()) || (co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()))) && !(((co.getSendcarnum() > 0) || (co.getBackcarnum() > 0)) && (co.getTranscwb()
				.length() > 0) && !co.getCwb().equals(co.getTranscwb()) && (co.getFlowordertype() != FlowOrderTypeEnum.DingDanLanJie.getValue()))) {
		} else {
			Reason r = this.reasonDAO.getReasonByReasonid(reasonid);

			String sql = "update express_ops_cwb_detail set flowordertype=?,backreason=?,backreasonid=? where cwb=? and state=1";
			this.jdbcTemplate.update(sql, flowOrderTypeEnum.getValue(), r == null ? "" : r.getReasoncontent(), r == null ? 0 : r.getReasonid(), cwb);
			this.cwbDAO.updateScannum(co.getCwb(), 1);
			this.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r == null ? "" : r.getReasoncontent(), System.currentTimeMillis(), scancwb, false);
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
		CwbOrderService.logger.info("{} 将订单 {} 审为退货再投", flowOrderType, cwb);

		cwb = this.translateCwb(cwb);
		return this.zaiTouHandle(user, cwb, scancwb, reasonid);
	}

	@Transactional
	public CwbOrder zaiTouHandle(User user, String cwb, String scancwb, long reasonid) {

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
		}

		this.orderInterceptService.checkTransCwbIsIntercept(scancwb, FlowOrderTypeEnum.ChuKuSaoMiao);

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
		String multiTransCwb = co.getTranscwb();
		if (!StringUtil.isEmpty(multiTransCwb)) {
			for (String transCwb : multiTransCwb.split(this.getSplitstring(multiTransCwb))) {
				this.mpsOptStateService.updateMPSInfo(transCwb, FlowOrderTypeEnum.ShenHeWeiZaiTou, 0L, co.getCurrentbranchid(), co.getNextbranchid());
				// added by songkaojun 2016-01-22 将运单状态改为配送
				this.mpsOptStateService.updateTranscwbstate(transCwb, TransCwbStateEnum.PEISONG);
			}
		}
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
		// this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.updateCwbState(cwb, CwbStateEnum.PeiShong);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, r.getReasoncontent(), System.currentTimeMillis(), scancwb, false);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
		// added by songkaojun 2016-01-25 新的一票多件模式，需要记录运单流程信息，用于更新一票多件状态
		if (this.mpsCommonService.isNewMPSOrder(cwb)) {
			List<String> transCwbList = this.mpsCommonService.getTransCwbListByCwb(cwb);
			for (String transCwb : transCwbList) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, transCwb, flowOrderTypeEnum, "");
			}
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
		CwbOrderService.logger.info("{} 将订单 {} 异常单处理", flowOrderType, cwb);

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

	private CwbOrder handleSpecialCwbYipiaoduojian(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, long handleresult, long handleperson, String handlereason) {
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

	private void handleSpecialCwb(User user, String cwb, String scancwb, CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum, long isypdjusetranscwb, boolean isypdj, long handleresult, long handleperson, String handlereason) {
		String sql = "update express_ops_cwb_detail set currentbranchid=" + user.getBranchid() + ",flowordertype=" + FlowOrderTypeEnum.YiChangDingDanChuLi.getValue() + ",handleresult=" + handleresult + ",handleperson=" + handleperson + ",handlereason='" + handlereason + "',cwbstate=" + CwbStateEnum.DiuShi
				.getValue() + " where cwb='" + cwb + "' and state =1 ";
		this.jdbcTemplate.update(sql);
		this.cwbDAO.updateScannum(co.getCwb(), 1);
		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, handlereason, System.currentTimeMillis(), scancwb, false);
		if ((isypdjusetranscwb == 1) && isypdj) {
			this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, scancwb, flowOrderTypeEnum, "");
		}
	}

	public void updateTuihuoBranch(CwbOrder cwbOrder, Branch branch) {
		CwbOrderService.logger.info("更新退货站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());
		this.cwbDAO.updateTuihuoBranchid(branch.getBranchid(), cwbOrder.getCwb());
		this.updateNextBranchId(cwbOrder.getCwb());
	}
	
	/**
	 * 更新站点和小件员
	 * 2016年6月17日 下午12:00:37
	 * @param user
	 * @param cwbOrder
	 * @param branch
	 * @param addresscodeedittype
	 * @throws Exception
	 */
	@Transactional
	public void updateDeliveryBranchAndCourier(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype, User deliver) throws Exception {
		this.updateDeliveryBranch(user, cwbOrder, branch, addresscodeedittype);
		long exceldeliverid = 0;
		String exceldeliver = null;
		if(deliver != null) {
			exceldeliverid = deliver.getUserid();
			exceldeliver = deliver.getRealname();
		}
		this.cwbDAO.updateAddressDeliverByCwb(cwbOrder.getCwb(), exceldeliverid, exceldeliver);
	}
	
	/**
	 * 更新小件员
	 * @date 2016年7月29日 下午12:16:00
	 * @param cwb
	 * @param deliver
	 * @throws Exception
	 */
	@Transactional
	public void updateDeliveryCourier(String cwb, User deliver) throws Exception {
		if (deliver == null) {
			this.cwbDAO.updateAddressDeliverByCwb(cwb,  0, null);
		} else {
			this.cwbDAO.updateAddressDeliverByCwb(cwb,  deliver.getUserid(), deliver.getRealname());
		}
	}
	
	@Transactional
	public void updateDeliveryBranch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype) throws Exception {
		CwbOrderService.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan
				.getValue())) {

			this.cwbDAO.updateDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		} else {
			this.cwbDAO.updateDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		}

		this.updateNextBranchId(cwbOrder.getCwb());
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis(), cwbOrder.getCwb(), false);

		//Added by leoliao at 2016-04-11 订单匹配站点后需要更新运单的下一站
		try {
			CwbOrder cwbOrderTmp = this.cwbDAO.getCwbByCwb(cwbOrder.getCwb());
			if ((cwbOrderTmp != null) && (cwbOrderTmp.getIsmpsflag() == IsmpsflagEnum.yes.getValue())) {
				this.transCwbDetailDAO.updateNextbranch(cwbOrderTmp.getCwb().trim(), cwbOrderTmp.getNextbranchid());
			}
		} catch (Exception ex) {
			CwbOrderService.logger.error("[DataInmportcontroller.editBatchBranch]更新一票多件对应的运单下一站出错", ex);
		}
		//Added end
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
	public void updateAddressMatch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype, List<DelivererVo> delivererList, List<Integer> timeLimitList) throws Exception {
		CwbOrderService.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan
				.getValue())) {

			this.cwbDAO.updateAddressDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype, delivererList, timeLimitList);
		} else {
			this.cwbDAO.updateAddressDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype, delivererList, timeLimitList);
		}

		this.updateNextBranchId(cwbOrder.getCwb());
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis(), cwbOrder.getCwb(), false);

	}

	@Transactional
	public void updateDeliveryOutBranch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype, long branchid) throws Exception {
		CwbOrderService.logger.info("更新配送站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch);

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan
				.getValue())) {

			this.cwbDAO.updateDeliveryBranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		} else {
			this.cwbDAO.updateDeliveryBranchidAndNextbranchid(branch.getBranchname(), branch.getBranchid(), cwbOrder.getCwb(), addresscodeedittype);
		}

		if (branchid != 0) {
			CwbOrderService.logger.info("中转出库强制出库下一站{},单号：{}", branchid, cwbOrder.getCwb());
			this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), branchid);
		}
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdateDeliveryBranch, "", System.currentTimeMillis(), cwbOrder.getCwb(), false);

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
			CwbOrderService.logger.error("error while saveing cwbremark,cwb:" + cwb + "cwbremark:" + csremark, e);
			throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.BeiZhu, csremark, System.currentTimeMillis(), cwb, false);
	}

	public void updateSendCarNum(String cwb, int sendcarnum) {
		this.cwbDAO.updateSendCarNum(sendcarnum, cwb);
	}

	private void updateNextBranchId(String cwb) {

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.PeiShong.getValue())) {
			CwbOrderService.logger.info("配送订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getDeliverybranchid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getDeliverybranchid());
			if (nextbranchid != 0) {
				CwbOrderService.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
				this.cwbDAO.updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
			}
			return;
		}
		if ((cwbOrder != null) && (cwbOrder.getCwbstate() == CwbStateEnum.TuiHuo.getValue())) {
			CwbOrderService.logger.info("退货订单更新目标站点,cwb:{},站点:{}", cwbOrder.getCwb(), cwbOrder.getTuihuoid());
			long nextbranchid = this.cwbRouteService.getNextBranch(cwbOrder.getCurrentbranchid(), cwbOrder.getTuihuoid());
			if (nextbranchid != 0) {
				CwbOrderService.logger.info("路由计算下一站点为{},cwb:{}", nextbranchid, cwbOrder.getCwb());
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
	public long checkResponseBatchnoForBale(User user, long requestbatchno, long branchid, long driverid, long truckid, long state, long operatetype, String cwbs, long customerid, long baleid) {
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

		//		this.groupDetailDao.delGroupDetailByCwbsAndBranchidAndFlowordertypeForBale(cwbsStrSql.toString(), user.getBranchid(), flowordertype, baleno);
		//DMP 4.2.8 由于分拣中转合包打印，所有当出库扫描打印的时候包含包含
		if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
			this.groupDetailDao.delGroupDetailByCwbsAndBranchidAndFlowordertypesForBale(cwbsStrSql.toString(), user.getBranchid(), flowordertypes, baleid);

		} else {
			this.groupDetailDao.delGroupDetailByCwbsAndBranchidAndFlowordertypeForBale(cwbsStrSql.toString(), user.getBranchid(), flowordertype, baleid);
		}

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
	public List<ExceptionCwb> controlForBranchImport(User user, long page, String cwb, long scantype, String errortype, String userid, String beginemaildate, String endemaildate, String branchid, long scope) {
		try {

			List<ExceptionCwb> eclist = null;

			eclist = this.exceptionCwbDAO.getAllECByPage(page, cwb, scantype, errortype, branchid, userid, 0, beginemaildate, endemaildate, scope);

			return eclist;
		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
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
	public List<CwbOrder> getListByCwbs(String cwbs, String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, String baleno, String transcwb, long page) {
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
			//此包号最近一次的使用
			Bale bale = this.baleDAO.getLastBaleByBaleno(baleno);
			if (bale != null) {
				list = this.getListByBale(bale.getId(), page);
			}
		} else if (transcwb.length() > 0) {
			list = this.cwbDAO.getListByTransCwb(transcwb, page);
		} else {
			list = this.cwbDAO.getListByCwb(begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, page);
		}
		return list;
	}

	public List<CwbOrder> getOrderList(String cwbs, String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, String baleno, String transcwb) {
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
									CwbOrderService.logger.error("", e);
								}
							}
						}
						count++;
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);

		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
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
		List<Long> idList = this.cwbRouteService.getNextPossibleBranch(user.getBranchid());
		List<Branch> listBranch = new ArrayList<Branch>();
		if (idList.size() != 0) {
			String ids = idList.toString();
			listBranch = this.branchDAO.getBranchByBranchIdsNotNull(ids.substring(1, ids.length() - 1));
		}
		return listBranch;
	}
	
	/**
	 * 返回当前站点指定类型的下一站点
	 * @param user
	 * @param branchEnum
	 * @return
	 */
	public List<Branch> getNextPossibleBranches(User user, BranchEnum branchEnum){
		List<Branch> listBranch = getNextPossibleBranches(user);
		List<Branch> branchList = new ArrayList<Branch>();
		for(Branch branch : listBranch){
			if(branch.getSitetype() == branchEnum.getValue()){
				branchList.add(branch);
			}
		}
		return branchList;
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
	@Consume(uri = CwbOrderService.MQ_FROM_URI_RECEIVE_GOODS_ORDER_FLOW + "?concurrentConsumers=5")
	public void autoReceiveGoods(@Header("orderFlow") String orderFlow, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			CwbOrderService.logger.info("开始对orderflow的监听");
			OrderFlow orderflow = this.om.readValue(orderFlow, OrderFlow.class);
			this.autoReceiveGoods(orderflow);
		} catch (CwbException cwbe) {
			CwbOrderService.logger.error("对orderflow的监听  处理异常,orderFlow:{}", orderFlow);
			CwbOrderService.logger.error("对orderflow的监听  处理异常, 异常类型 :CwbException ", cwbe);
		} catch (Exception e) {
			CwbOrderService.logger.error("对orderflow的监听  处理异常,orderFlow:{}", orderFlow);
			CwbOrderService.logger.error("", e);

			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".autoReceiveGoods").buildExceptionInfo(e.toString())
					.buildTopic(CwbOrderService.MQ_FROM_URI_RECEIVE_GOODS_ORDER_FLOW).buildMessageHeader("orderFlow", orderFlow).buildMessageHeaderUUID(messageHeaderUUID)
					.buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
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
			CwbOrderService.logger.info("滞留自动领货,cwb:{}", co.getCwb());
		}

		// 2.监听归班的jms，产生待返单记录
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate() == DeliveryStateEnum.PeiSongChengGong
				.getValue())) {
			long isFeedbackcwb = this.customerDAO.getCustomerById(cwbOrderWithDeliveryState.getCwbOrder().getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO
					.getCustomerById(cwbOrderWithDeliveryState.getCwbOrder().getCustomerid()).getIsFeedbackcwb();
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
				CwbOrderService.logger.info("产生返单,cwb:{}", co.getCwb());
			}
		} else {
			this.returnCwbsDAO.deleteReturnCwbByCwb(orderflow.getCwb());
			CwbOrderService.logger.info("删除返单,cwb:{}", co.getCwb());
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
				CwbOrderService.logger.info("更新退货,cwb:{}", co.getCwb());
			} else {
				this.tuihuoRecordDAO.creTuihuoRecord(tuihuoRecord);
				CwbOrderService.logger.info("产生退货,cwb:{}", co.getCwb());
			}
		}
		// 4.监听退货站入库操作，往退货记录表更新记录
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			List<TuihuoRecord> tuihuolist = this.tuihuoRecordDAO.getTuihuoRecordByCwb(orderflow.getCwb());

			if(tuihuolist.size() == 0) {
				TuihuoRecord tuihuoRecord = new TuihuoRecord();
				tuihuoRecord.setCwb(orderflow.getCwb());
				tuihuoRecord.setBranchid(co.getStartbranchid());
				tuihuoRecord.setTuihuobranchid(co.getNextbranchid());
				tuihuoRecord.setTuihuozhanrukutime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()));
				tuihuoRecord.setCustomerid(co.getCustomerid());
				tuihuoRecord.setCwbordertypeid(co.getCwbordertypeid());
				tuihuoRecord.setUserid(orderflow.getUserid());
				this.tuihuoRecordDAO.creTuihuoRecord(tuihuoRecord);
			} else {
				this.tuihuoRecordDAO.saveTuihuoRecordById(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderflow.getCredate()), tuihuolist.get(0).getId());
			}
			logger.info("退货站入库,cwb:{}", co.getCwb());
		}

		// 5.导入数据、出库、分站领货、pos支付、修改匹配站，往上门退订单表更新记录
		if (((orderflow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo
				.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) || (orderflow
				.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())) && (co
				.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) {

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
					CwbOrderService.logger.info("上门退订单更新,cwb:{}", co.getCwb());
				} else {
					this.shangMenTuiCwbDetailDAO.insertShangMenTuiCwbDetail(shangMenTuiCwbDetail);
					CwbOrderService.logger.info("上门退订单插入,cwb:{}", co.getCwb());
				}
			} catch (Exception e) {
				CwbOrderService.logger.error("上门退订单迁移", e);
			}
		}

		// 6.监听归班配送结果为货物丢失、客服标记为货物丢失（亚马逊）、异常单处理的jms，产生货物丢失记录
		if (((orderflow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue())) || (orderflow
				.getFlowordertype() == FlowOrderTypeEnum.ShouGongdiushi.getValue()) || (orderflow.getFlowordertype() == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue())) {
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
				CwbOrderService.logger.info("货物丢失，异常单处理,cwb:{}", co.getCwb());
			} else {
				this.cwbDiuShiDAO.creCwbDiuShi(cwbDiuShi);
				CwbOrderService.logger.info("货物丢失，异常单处理   插入,cwb:{}", co.getCwb());
			}
		} else {
			this.cwbDiuShiDAO.saveCwbDiuShiByCwb(0, co.getCwb());
			CwbOrderService.logger.info("货物丢失，异常单处理,cwb:{}", co.getCwb());
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
			CwbOrderService.logger.info("揽收,cwb:{}", co.getCwb());
		}

		// 9.揽收订单中转出站时更新揽收表中订单信息
		List<Branch> branchList = this.branchDAO.getAllBranches();
		long currentbranchsite = this.getQueryBranchSitetype(branchList, co.getStartbranchid());
		long nextbranchsite = this.getQueryBranchSitetype(branchList, co.getNextbranchid());
		if ((orderflow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getCustomerid() == -2) && (currentbranchsite == BranchEnum.ZhanDian.getValue()) && (nextbranchsite == BranchEnum.ZhongZhuan
				.getValue())) {
			this.cwbKuaiDiDAO.saveCwbKuaiDiZhongZhuanByCwb(co.getNextbranchid(), co.getCwb());
			CwbOrderService.logger.info("揽收订单中转出站时更新揽收表中订单信息,cwb:{}", co.getCwb());
		}

		// 10.分站领货更新中转申请表状态为1/退货申请审核失败，这样统计待领货则不需要查询统计了
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			CwbOrderService.logger.info("分站领货更新中转申请失败、退货申请审核失败表isstastics为1,cwb={}", orderflow.getCwb());
			this.cwbApplyZhongZhuanDAO.updateStastics(orderflow.getCwb()); // 修改中转审核状态修改
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
			List<String> todaydaohuocwbs = this.orderFlowDAO
					.getOrderFlowLingHuoList(user.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil
							.getCurrentDayZeroTime(), "");
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
		CwbOrderService.logger.info(System.currentTimeMillis() + "--creStock111");
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
		CwbOrderService.logger.info(System.currentTimeMillis() + "--creStock333");
	}

	/**
	 * 处理库存list,在库存详情表产生记录
	 *
	 * @param list
	 * @param user
	 */
	public void handleStockData(List<CwbOrder> list, User user) {
		CwbOrderService.logger.info(System.currentTimeMillis() + "--handleStockData1");
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
						this.stockDetailDAO.creStockDetailByParm(c.getCwb(), transcwb, user.getBranchid(), c.getOrderflowid(), StockDetailStocktypeEnum.LingHuoKuCun.getValue(), StockDetailEnum.Kui
								.getValue());
					}
				} else {
					this.stockDetailDAO.creStockDetailByParm(c.getCwb(), c.getTranscwb(), user.getBranchid(), c.getOrderflowid(), StockDetailStocktypeEnum.YiBanKuCun.getValue(), StockDetailEnum.Kui
							.getValue());
				}
			}
		}
		CwbOrderService.logger.info(System.currentTimeMillis() + "--handleStockData2");
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

		List<JSONObject> winlist = this.stockDetailDAO
				.getAllStockDetailByWhere(user.getBranchid(), StockDetailEnum.Ying.getValue(), "1", StockDetailStocktypeEnum.YiBanKuCun.getValue() + "," + StockDetailStocktypeEnum.LingHuoKuCun
						.getValue());
		List<JSONObject> kuilist = this.stockDetailDAO
				.getAllStockDetailByWhere(user.getBranchid(), StockDetailEnum.Kui.getValue(), "1", StockDetailStocktypeEnum.YiBanKuCun.getValue() + "," + StockDetailStocktypeEnum.LingHuoKuCun
						.getValue());
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
			CwbOrderService.logger.info("消息发送端：losecwbbatchProducerTemplate, cwbbatchDelete={}", cwb);
			this.losecwbbatchProducerTemplate.sendBodyAndHeader(null, "cwbbatchDelete", cwb);
			CwbOrderService.logger.info("消息发送端：dataLoseByCwb, cwb={}", cwb);
			this.dataLoseByCwb.sendBodyAndHeader(null, "cwb", cwb);
			CwbOrderService.logger.info("订单失效准备发送jms--");
		} catch (Exception e) {
			CwbOrderService.logger.error("ERRO-订单失效准备发送jms-异常");
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".deletecwb").buildExceptionInfo(e.toString())
					.buildTopic(this.losecwbbatchProducerTemplate.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("cwbbatchDelete", cwb).getMqException());
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".deletecwb").buildExceptionInfo(e.toString())
					.buildTopic(this.dataLoseByCwb.getDefaultEndpoint().getEndpointUri()).buildMessageHeader("cwb", cwb).getMqException());
		}
	}

	public CwbOrder lanShouDaoHuo(User user, String cwb, String scancwb, long deliverid) {
		CwbOrderService.logger.info("开始揽收到货处理,cwb:{}", cwb);

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
		String sql = "insert into express_ops_cwb_detail (cwb,currentbranchid,customerid,flowordertype,cwbstate,cwbordertypeid,credate) values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, cwb, user.getBranchid(), -2, flowOrderTypeEnum.getValue(), CwbStateEnum.PeiShong.getValue(), CwbOrderTypeIdEnum.Peisong.getValue(), Timestamp
				.valueOf(DateTimeUtil.getNowTime()));

		// 入站时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);

		// 揽收表产生记录
		String sql2 = "insert into ops_cwbkuaidi_detail (cwb,lanshoubranchid,lanshouuserid,lanshoutime) values(?,?,?,?)";
		this.jdbcTemplate.update(sql2, cwb, user.getBranchid(), deliverid, datetime);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		this.createFloworder(user, user.getBranchid(), co, flowOrderTypeEnum, "", System.currentTimeMillis(), scancwb, false);
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
			CwbOrderService.logger.info("订单号中转出库匹配中转原因{},cwb={}", transferReasonid, cwbOrder.getCwb());
			transRes.setTransferreasonid(transferReasonid);
			this.transferReasonStasticsDao.updateTransferReasonStastics(transRes);
		}

	}

	public void datalose_vipshop(String cwb) {

		try {
			//CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb); 因为此时在外层已被删除（state=0），所以要改查询方法，以达到最小范围修改！
			List<CwbOrder> coList = this.cwbDAO.getLabelPrintCwbsByCwbs("'"+cwb+"'");
			CwbOrder co = (coList!=null && coList.size()>0)?coList.get(0):null;

			this.cwbDAO.dataLoseByCwb(cwb);
			this.deliveryStateDAO.inactiveDeliveryStateByCwb(cwb);
			this.deliveryCashDAO.updateDeliveryCashStateBycwb(cwb); // 小件员工作量表失效
			this.exportwarhousesummaryDAO.dataLoseByCwb(cwb);
			this.exportwarhousesummaryDAO.LoseintowarhouseByCwb(cwb);
			this.transCwbDao.deleteTranscwb(cwb);
			// 失效订单删除
			this.deletecwb(cwb);
			// 删除倒车时间表的订单
			this.orderArriveTimeDAO.deleteOrderArriveTimeByCwb(cwb);
			// 删除审核为退货再投的订单
			this.orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb);

			if (co != null) {
				if (this.emailDateDAO.getEmailDateById(co.getEmaildateid()) != null) {
					long cwbcount = this.emailDateDAO.getEmailDateById(co.getEmaildateid()).getCwbcount() - 1;
					this.emailDateDAO.editEditEmaildateForCwbcount(cwbcount, co.getEmaildateid());
				}
				if (co != null) {
					this.shiXiaoDAO.creAbnormalOrdernew(co.getOpscwbid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), co.getCurrentbranchid(), co.getCustomerid(), cwb, co
							.getDeliverybranchid(), co.getFlowordertype(), co.getNextbranchid(), co.getStartbranchid(), 1, 129, co.getCwbstate(), co.getEmaildate());
				}
				this.orderFlowDAO.deleteOrderFlowByCwb(cwb);
			}

			// 删除打印表记录
			this.shangMenTuiCwbDetailDAO.deletePrintRecord(cwb);

		} catch (Exception e) {
			CwbOrderService.logger.error("唯品会失效异常cwb=" + cwb, e);
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
		long newpaywayid = (Long) (newpaywayParams.get("newpaywayid") == null ? Long.valueOf(1L) : (newpaywayParams.get("newpaywayid")));
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

	// 新增退货再投view
	public List<CwbOrderView> getTuiZaiCwbOrderView(List<CwbOrder> orderlist, List<OperationTime> optList, List<Customer> customerList, List<Branch> branchList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((optList.size() > 0) && (orderlist.size() > 0)) {
			for (OperationTime ot : optList) {
				for (CwbOrder c : orderlist) {
					if (ot.getCwb().trim().equals(c.getCwb().trim())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setCwb(c.getCwb());
						cwbOrderView.setCwbstate(c.getCwbstate());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(c.getCwbordertypeid()));// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
						cwbOrderView.setConsigneename(c.getConsigneename());// 收件人
						cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());// 收件人地址
						cwbOrderView.setTuihuozhaninstoreroomtime(this.getStringDate(ot.getCredate()));// 退货库入库时间
						cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getStartbranchid()));// 退货站的上一站(配送站)
						User user = this.getSessionUser();
						cwbOrderView.setAuditor(user.getRealname());// 审核人
						cwbOrderView.setAudittime("");// 审核时间
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	// 新增退货再投view
	public List<CwbOrderView> getTuigongSuccessCwbOrderView(List<CwbOrder> orderlist, List<OrderbackRecord> orList, List<Customer> customerList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orderlist.size() > 0)) {
			for (CwbOrder c : orderlist) {
				for (OrderbackRecord ot : orList) {
					if (ot.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setCwb(ot.getCwb());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
						cwbOrderView.setReceivablefee(ot.getReceivablefee());
						cwbOrderView.setEmaildate(ot.getEmaildate());
						cwbOrderView.setCreatetime(ot.getCreatetime());// 退货出库时间
						cwbOrderView.setAuditstatename(this.getAuditname(ot.getShenhestate()));// 当前确认状态
						cwbOrderView.setAuditor(ot.getAuditname());// 确认人
						cwbOrderView.setAudittime(ot.getAudittime());// 确认时间
						cwbOrderView.setNowapplystate(ot.getShenhestate());
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public String getAuditname(int auditstatename) {
		if (auditstatename == 0) {
			return "待确认";
		} else if (auditstatename == 1) {
			return "退客户成功";
		} else if (auditstatename == 2) {
			return "拒收退货";
		}
		return "";
	}

	// 新增支付信息申请view
	public List<CwbOrderView> getZhifuApplyCwbOrderView(List<ZhiFuApplyView> orList, List<Customer> customerList, List<Branch> branchList, List<User> uslist) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orList.size() > 0)) {
			for (ZhiFuApplyView ot : orList) {
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setOpscwbid(ot.getApplyid());// 存放支付申请的主键id
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
				cwbOrderView.setApplytype(ApplyEnum.getTextByValue(ot.getApplyway()));
				String oldnewCwbordertypename = CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText() + "/" + (ot.getApplycwbordertypeid() == 0 ? "" : CwbOrderTypeIdEnum
						.getByValue(ot.getApplycwbordertypeid()).getText());
				cwbOrderView.setOldnewCwbordertypename(oldnewCwbordertypename);// 订单类型
				String oldnewReceivablefee = ot.getReceivablefee() + "/" + ot.getApplyreceivablefee();
				cwbOrderView.setOldnewReceivablefee(oldnewReceivablefee);// 订单金额
				String oldnewPaytype = PaytypeEnum.getTextByValue(ot.getPaywayid()) + "/" + (ot.getApplypaywayid() == 0 ? "" : PaytypeEnum.getTextByValue(ot.getApplypaywayid()));
				cwbOrderView.setOldnewPaytype(oldnewPaytype);// 支付方式
				cwbOrderView.setNowState(this.getNowApplyState(ot.getApplystate()));// 订单当前状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, ot.getBranchid()));// 当前站点
				cwbOrderView.setApplyuser(this.dataStatisticsService.getQueryUserName(uslist, ot.getUserid()));// 申请人
				cwbOrderView.setApplytime(ot.getApplytime());// 申请时间
				cwbOrderView.setAuditor(ot.getAuditname());// 审核人
				cwbOrderView.setAudittime(ot.getAudittime());// 审核时间
				cwbOrderView.setNowapplystate(ot.getApplystate());
				cwbOrderView.setShouldfare(ot.getShouldfare() == null ? new BigDecimal(0) : ot.getShouldfare());
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public String getNowApplyState(int statevalue) {
		if ((statevalue == 1) || (statevalue == 2)) {
			String str = statevalue == 1 ? "待审核" : "已审核";
			return str;
		}
		return null;
	}

	// 新增支付信息确认view
	public List<CwbOrderView> getZhifuConfirmCwbOrderView(List<ZhiFuApplyView> orList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((orList.size() > 0) && (orList.size() > 0)) {
			for (ZhiFuApplyView ot : orList) {
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setOpscwbid(ot.getApplyid());// 存放支付申请的主键id
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
				cwbOrderView.setApplytype(ApplyEnum.getTextByValue(ot.getApplyway()));
				String oldnewCwbordertypename = CwbOrderTypeIdEnum.getByValue(ot.getCwbordertypeid()).getText() + "/" + (ot.getApplycwbordertypeid() == 0 ? "" : CwbOrderTypeIdEnum
						.getByValue(ot.getApplycwbordertypeid()).getText());
				cwbOrderView.setOldnewCwbordertypename(oldnewCwbordertypename);// 订单类型
				String oldnewReceivablefee = ot.getReceivablefee() + "/" + ot.getApplyreceivablefee();
				cwbOrderView.setOldnewReceivablefee(oldnewReceivablefee);// 订单金额
				cwbOrderView.setShouldfare(ot.getShouldfare() == null ? new BigDecimal(0) : ot.getShouldfare());
				String oldnewPaytype = PaytypeEnum.getTextByValue(ot.getPaywayid()) + "/" + (ot.getApplypaywayid() == 0 ? "" : PaytypeEnum.getTextByValue(ot.getApplypaywayid()));
				cwbOrderView.setOldnewPaytype(oldnewPaytype);// 支付方式
				cwbOrderView.setNowState(this.getNowConfirmState(ot.getConfirmstate()));// 订单当前状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, ot.getBranchid()));// 当前站点
				cwbOrderView.setApplyuser(this.dataStatisticsService.getQueryUserName(userList, ot.getUserid()));// 申请人
				cwbOrderView.setAuditor(ot.getAuditname());// 审核人
				cwbOrderView.setAudittime(ot.getAudittime());// 审核时间
				cwbOrderView.setConfirmname(ot.getConfirmname());// 确认人
				cwbOrderView.setConfirmtime(ot.getConfirmtime());// 确认时间
				cwbOrderView.setNowapplystate(ot.getConfirmstate());
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public String getNowConfirmState(int statevalue) {
		if ((statevalue == 1) || (statevalue == 2)) {
			String str = statevalue == 1 ? "待确认" : "已确认";
			return str;
		}
		return null;
	}

	public String getStringDate(long datetime) {
		Date date = new Date(datetime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public String getCwbs(String cwbs) {
		StringBuffer sb = new StringBuffer();
		for (String str : cwbs.split("\r\n")) {
			sb.append("'").append(str.trim()).append("',");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public List<CwbOrderView> getZhongZhuanCwbOrderView(List<CwbOrder> coList, List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist, List<Customer> customerList, List<Branch> branchList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((cwbApplyZhongZhuanlist.size() > 0) && (cwbApplyZhongZhuanlist.size() > 0)) {
			for (CwbOrder c : coList) {
				for (CwbApplyZhongZhuan ot : cwbApplyZhongZhuanlist) {
					if (ot.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();
						cwbOrderView.setOpscwbid(ot.getId());// 中转id
						cwbOrderView.setCwb(ot.getCwb());
						cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getByValue((int) (ot.getCwbordertypeid())).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, ot.getCustomerid()));// 供货商的名称
						cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前站点
						cwbOrderView.setMatchbranchname(this.dataStatisticsService.getQueryBranchName(branchList, ot.getApplyzhongzhuanbranchid()));// 配送站点名称
						cwbOrderView.setInSitetime(ot.getApplytime());// 到站时间(审核为待中转时的当前时间)
						cwbOrderView.setAuditor(ot.getAuditname());// 审核人
						cwbOrderView.setAudittime(ot.getAudittime());// 审核时间
						cwbOrderView.setAuditstatename(this.getAuditstatename(ot.getIshandle()));// 审核状态
						cwbOrderView.setAuditstate(ot.getIshandle());
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public String getAuditstatename(long ishandle) {
		if (ishandle == 2) {
			return "审核不通过";
		} else if (ishandle == 3) {
			return "审核通过";
		} else {
			return "待审核";
		}
	}

	// 修改状态重置反馈(页面view)
	public List<CwbOrderView> getResetCwbOrderView(List<ApplyEditDeliverystate> applyeditlist, List<User> uslist, List<Branch> branchList, Map<String, String> reasonMap) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (applyeditlist.size() > 0) {
			for (ApplyEditDeliverystate ot : applyeditlist) {
				DeliveryState deliveryState = this.deliveryStateDAO.getDeliveryByCwb(ot.getCwb());
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setCwb(ot.getCwb());
				cwbOrderView.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(ot.getCwbordertypeid()));// 订单类型
				cwbOrderView.setDeliveryname(DeliveryStateEnum.getByValue((int) (ot.getNowdeliverystate())).getText());// 配送结果
				cwbOrderView.setCwbstatename(CwbStateEnum.getByValue(ot.getCwbstate()).getText());// 订单状态
				cwbOrderView.setRemark1("");// 结算状态
				cwbOrderView.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, deliveryState.getDeliverybranchid()));// 反馈站点
				cwbOrderView.setResetfeedusername(this.dataStatisticsService.getQueryUserName(uslist, deliveryState.getDeliveryid()));// 反馈人
				cwbOrderView.setResetfeedtime(ot.getDeliverpodtime());// 反馈时间
				cwbOrderView.setDonepeople(this.dataStatisticsService.getQueryUserName(uslist, ot.getEdituserid()));// 操作人
				cwbOrderView.setDonetime(ot.getEdittime());// 操作时间
				cwbOrderView.setNowState(this.getNowstate(ot.getShenhestate()));// 订单当前状态
				cwbOrderView.setRemark2(DeliveryStateEnum.getByValue((int) ot.getEditnowdeliverystate()).getText());// 修改配送结果
				cwbOrderView.setRemark3(reasonMap.get(ot.getReasonid() + ""));// 原因备注
				cwbOrderView.setRemark4(ot.getEditreason());// 备注
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public String getNowstate(int shenhestate) {
		String shenhestateString = "";
		if (shenhestate == 1) {
			shenhestateString = "待审核";
		}
		if (shenhestate == 2) {
			shenhestateString = "审核不通过";
		}
		if (shenhestate == 3) {
			shenhestateString = "审核通过";
		}
		return shenhestateString;
	}

	public List<CwbOrderView> getCwborderviewList(List<CwbOrder> coList, List<ApplyEditDeliverystate> aedsList, List<User> userList, List<Branch> branchList) {
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if ((coList != null) && (aedsList != null)) {
			for (CwbOrder co : coList) {
				CwbOrderView cov = new CwbOrderView();
				int index = coList.indexOf(co);
				ApplyEditDeliverystate aeds = aedsList.get(index);
				cov.setOpscwbid(aeds.getId());
				cov.setCwb(aeds.getCwb());// 订单号
				cov.setCwbordertypeid(String.valueOf(co.getCwbordertypeid()));// 订单类型id
				cov.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(co.getCwbordertypeid()));// 订单类型
				cov.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, co.getStartbranchid()));// 当前站点
				cov.setDeliveryname(DeliveryStateEnum.getByValue(co.getDeliverystate()).getText());
				cov.setDelivername(this.dataStatisticsService.getQueryUserName(userList, co.getDeliverid()));// 小件员
				cov.setRemark1(ApplyEditDeliverystateIshandleEnum.getByValue((int) aeds.getIshandle()).getText());// 审核状态
				cov.setRemark2(this.dataStatisticsService.getQueryUserName(userList, aeds.getEdituserid()));// 处理人
				cov.setRemark3(aeds.getEditreason());// 原因备注
				cov.setDeliverystate(aeds.getEditnowdeliverystate());// 申请修改配送结果
				cov.setReasonid(aeds.getReasonid());
				cov.setState(aeds.getState());
				covList.add(cov);
			}
		}
		return covList;
	}

	// 退货再投页面显示
	public List<OrderBackRuku> getOrderBackRukuRecord(List<OrderBackRuku> obrList, List<Branch> branchList, List<Customer> customerList) {
		if ((obrList != null) && (obrList.size() > 0)) {
			for (OrderBackRuku obr : obrList) {
				obr.setCwbordertypename(CwbOrderTypeIdEnum.getTextByValue(obr.getCwbordertypeid()));// 订单类型
				obr.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, obr.getCustomerid()));// 客户名称
				obr.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, obr.getBranchid()));// 站点名
				obr.setAuditstatename(this.getAuditstatename(obr.getAuditstate()));
			}
		}
		return obrList;
	}

	public String getAuditstatename(int auditstate) {
		if (auditstate == 0) {
			return "待审核";
		} else {
			return "已审核";
		}
	}

	public List<ApplyEditDeliverystate> getapplyeditdeliverysates(List<ApplyEditDeliverystate> applyEditDeliverystateLists, Map<String, String> reasonMap, List<Branch> branchlist, List<User> userList, List<String> strList) {
		if ((applyEditDeliverystateLists != null) && (applyEditDeliverystateLists.size() > 0) && (reasonMap.size() > 0)) {
			for (ApplyEditDeliverystate aeds : applyEditDeliverystateLists) {
				int ad = applyEditDeliverystateLists.indexOf(aeds);
				aeds.setReasoncontent(reasonMap.get(aeds.getReasonid() + "") == null ? "" : reasonMap.get(aeds.getReasonid() + "").toString());// 原因备注
				aeds.setEditusername(this.dataStatisticsService.getQueryUserName(userList, aeds.getEdituserid()));// 处理人
				aeds.setHandlename(this.gethandlename(aeds.getIshandle()));// 处理状态
				CwbOrder co = this.cwbDAO.getCwbByCwb(strList.get(ad));
				aeds.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchlist, co.getStartbranchid()));// 当前站点
				aeds.setApplybranchname(this.dataStatisticsService.getQueryBranchName(branchlist, aeds.getApplybranchid()));// 申请站点
				aeds.setNowdeliveryname(DeliveryStateEnum.getByValue((int) aeds.getNowdeliverystate()).getText());// 修改前的配送结果
				aeds.setEditnowdeliveryname(DeliveryStateEnum.getByValue((int) aeds.getEditnowdeliverystate()).getText());// 修改后的配送结果
				aeds.setDelivername(this.dataStatisticsService.getQueryUserName(userList, aeds.getDeliverid()));// 小件员名字
			}
		}
		return applyEditDeliverystateLists;
	}

	public String gethandlename(long ishandle) {
		String str = ishandle == 1 ? "已处理" : "未处理";
		return str;
	}

	/*
	 * public String getCwbsBydate(long flowordertypeid, String begindate,
	 * String enddate) { List<OrderFlow> orderList =
	 * orderFlowDAO.getOrderByCredates(flowordertypeid,begindate,enddate);
	 * StringBuffer sb = new StringBuffer(""); for(OrderFlow of:orderList){
	 * sb.append("'"+of.getCwb()+"',"); } if(sb.length()>0){ return
	 * sb.toString().substring(0, sb.length()-1); } return null; }
	 */
	public ExplinkResponse responseErrorZhongzhuanrukuLimit() {
		ExplinkResponse explinkResponse = new ExplinkResponse();
		explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.Shenhebutongguobuyunxuzhongzhuan.getText());
		explinkResponse.setStatuscode("11111");
		return explinkResponse;
	}

	public void updatePickBranch(User user, CwbOrder cwbOrder, Branch branch, CwbOrderAddressCodeEditTypeEnum addresscodeedittype) {
		CwbOrderService.logger.info("更新提货站点,cwb:{},站点:{}", cwbOrder.getCwb(), branch.getBranchid());

		this.validateStateTransfer(cwbOrder, FlowOrderTypeEnum.UpdatePickBranch);

		this.cwbDAO.updatePickBranchid(branch.getBranchid(), cwbOrder.getCwb());

		this.createFloworder(user, user.getBranchid(), cwbOrder, FlowOrderTypeEnum.UpdatePickBranch, "", System.currentTimeMillis(), cwbOrder.getCwb(), false);

	}

	public void fankuiAddTranscwbFlow(String cwb, CwbOrder co, User user, FlowOrderTypeEnum flowOrderTypeEnum, long deliverystate) {
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			CwbOrder cwborder = this.cwbDAO.getCwbByCwb(cwb);
			for (String transcwb : cwborder.getTranscwb().split(",")) {
				this.createTranscwbOrderFlow(user, user.getBranchid(), cwb, transcwb, flowOrderTypeEnum, "");

				if (cwborder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
					this.mpsOptStateService.updateMPSInfo(transcwb, flowOrderTypeEnum, 0L, co.getCurrentbranchid(), co.getNextbranchid());
					//拒收审核依据是否需要客服审核来改变订单，运单的状态，需要审核不修改订单，运单状态，不审核修改订单，运单状态为退货。
					if ((flowOrderTypeEnum.getValue() == FlowOrderTypeEnum.YiShenHe.getValue()) && ((deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (deliverystate == DeliveryStateEnum.JuShou
							.getValue()))) {
						Customer customer = this.customerDao.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
						boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
						if (!chechFlag) {
							//Added by vic.liang at 2016-04-13 退货需要审核则运单状态要保留为配送
							this.transCwbDetailDAO.updateTransCwbDetailBytranscwb(transcwb, TransCwbStateEnum.TUIHUO.getValue());
						}
					}
				}
			}
		}
	}

	/**
	 * @param cwbremark
	 * @param reasoncontent
	 * @param deliverstateremark
	 * @return
	 */
	private String creCwbremark(String cwbremark, String reasoncontent, String deliverstateremark,boolean flag) {
		StringBuilder strBuilder = new StringBuilder();
		if ((null != cwbremark) && (cwbremark.length() > 0)) {
			strBuilder.append(cwbremark + ",");
		}
		/*if ((null != deliverstateremark) && (deliverstateremark.length() > 0)) {
			strBuilder.append(deliverstateremark + ",");
		}
		if ((null != reasoncontent) && (reasoncontent.length() > 0)) {
			strBuilder.append(reasoncontent + ",");
		}*/
		//Hps_Concerto  这么做的原因是如果是分站滞留的 那就让其的remark 样式和 批量的一致 就是deliverstateremark在前 reasoncontent在后
		// 不想复制一个新的 因为改动又不大， 所以这么写了。
		int i = 0;
		while(i<2){
			if(flag){
				if ((null != deliverstateremark) && (deliverstateremark.length() > 0)) {
					strBuilder.append(deliverstateremark + ",");
				}
			}else{
				if ((null != reasoncontent) && (reasoncontent.length() > 0)) {
					strBuilder.append(reasoncontent + ",");
				}
				
			}
			flag=!flag;
			i++;	
			
		}
		if (strBuilder.length() > 0) {
			if (strBuilder.lastIndexOf(",") == (strBuilder.length() - 1)) {
				return strBuilder.substring(0, strBuilder.length() - 1);
			}
		}
		return strBuilder.toString();
	}

	public String getToString(String args[]) {
		String strs = "";
		if ((args != null) && (args.length > 0)) {
			for (String str : args) {
				if (!str.equals("0") && !str.equals('0')) {
					strs += "'" + str + "',";
				}
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public List<String> getBranchList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if ((strArr != null) && (strArr.length > 0)) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
	}

	// 获取存在订单修改申请or确认完成的支付信息修改申请的订单号
	public Map<String, List<String>> getEditCwb(String cwbs) {
		List<String> editCwbInfoList = new ArrayList<String>();
		List<String> editCwbPayList = new ArrayList<String>();
		Map<String, List<String>> editCwbMap = new HashMap<String, List<String>>();

		List<ZhiFuApplyView> zhifuApplyList = this.zhiFuApplyDao.getCheckConfirmZFAVByCwbs(cwbs);
		List<SearcheditInfo> editInfoList = this.editCwbInfoDao.getEditInfoByCwbs(cwbs);

		if ((zhifuApplyList != null) && (zhifuApplyList.size() > 0)) {
			for (ZhiFuApplyView zhifuApply : zhifuApplyList) {
				String cwb = zhifuApply.getCwb();
				if (!editCwbPayList.contains(cwb)) {
					editCwbPayList.add(cwb);
				}
			}
		}

		if ((editInfoList != null) && (editInfoList.size() > 0)) {
			for (SearcheditInfo editInfo : editInfoList) {
				String cwb = editInfo.getCwb();
				if (!editCwbInfoList.contains(cwb)) {
					editCwbInfoList.add(cwb);
				}
			}
		}

		editCwbMap.put("cwbInfo", editCwbInfoList);
		editCwbMap.put("cwbPay", editCwbPayList);
		return editCwbMap;
	}

	@Transactional
	public void delYpdjFlowordertypeMethod(String cwb) {

		this.ypdjHandleRecordDAO.delypdjflowordertype(cwb);

	}

	/**
	 *
	 * 分拣和中转 出库扫描业务逻辑处理
	 *
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	@Transactional
	public CwbOrder sortAndChangeOutWarehouse(User user, String cwb, String scancwb, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, long reasonid, boolean iszhongzhuanout, boolean anbaochuku) {

		CwbOrderService.logger.info("开始分拣和中转 出库处理,cwb:{}", cwb);

		cwb = this.translateCwb(cwb);

		// 【区域权限设置】的【中转站】作为扫码的当前站点
		long currentbranchid = super.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);

		if (currentbranchid == 0) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}

		return this.changeOutWarehousHandle(user, cwb, scancwb, currentbranchid, driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, false, reasonid, iszhongzhuanout, System
				.currentTimeMillis(), anbaochuku, false, true);

	}

	public void intohouseForExpressPackage(User user, String cwbs, long customerid, long driverid, BatchCount batchCount, boolean useEaimDate, List<Customer> cList, List<JSONObject> objList, List<String> allEmaildate, JSONArray promt) {
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			if (useEaimDate && !allEmaildate.contains(cwb)) {
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				JSONObject obj = new JSONObject();
				obj.put("cwb", cwb);
				if (null == co) {
					obj.put("emaildatename", "查无此单");
				} else {
					EmailDate e = this.emaildateDAO.getEmailDateById(co.getEmaildateid());
					if (null == e) {
						obj.put("emaildatename", "查无此单");
					} else {
						obj.put("emaildatename", "订单号不在本批次中，请选择" + e.getEmaildatetime() + "的批次");
					}
				}
				promt.add(obj);
				continue;
			}
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.translateCwb(cwb);
			obj.put("cwb", cwb);

			CwbOrder cwbOrder = this.intoWarehous(user, cwb, scancwb, customerid, driverid, 0, "", "", false);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("errorcode", "000000");
			for (Customer c : cList) {
				if (c.getCustomerid() == cwbOrder.getCustomerid()) {
					obj.put("customername", c.getCustomername());
					break;
				}
			}
			if (cwbOrder.getEmaildateid() == 0) {
				batchCount.setYouhuowudanCount(batchCount.getYouhuowudanCount() + 1);
			}
			batchCount.setThissuccess(batchCount.getThissuccess() + 1);

			objList.add(obj);
		}
	}

	public void exportHouseForExpressPackage(User user, String cwbs, long branchid, long driverid, long truckid, long confirmflag, BatchCount batchCount, List<Customer> cList, List<JSONObject> objList) {
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.translateCwb(cwb);
			obj.put("cwb", cwb);

			CwbOrder cwbOrder = this.outWarehous(user, cwb, scancwb, driverid, truckid, branchid, 0, confirmflag == 1, "", "", 0, false, false);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("errorcode", "000000");
			for (Customer c : cList) {
				if (c.getCustomerid() == cwbOrder.getCustomerid()) {
					obj.put("customername", c.getCustomername());
					break;
				}
			}
			batchCount.setThissuccess(batchCount.getThissuccess() + 1);
			objList.add(obj);
		}
	}

	public CwbOrder getCwbByCwb(String cwb) {
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	public List<String> getCwbsByBale(long baleid) {
		return this.baleCwbDAO.getCwbsByBale("" + baleid);
	}

	/**
	 * 出库扫描业务逻辑处理
	 *
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	@Transactional
	public CwbOrder changeOutWarehousHandle(User user, String cwb, String scancwb, long currentbranchid, long driverid, long truckid, long branchid, long requestbatchno, boolean forceOut, String comment, String packagecode, boolean isauto, long reasonid, boolean iszhongzhuanout, Long credate, boolean anbaochuku, boolean isAutoSupplyLink) {
		return this
				.changeOutWarehousHandle(user, cwb, scancwb, currentbranchid, driverid, truckid, branchid, requestbatchno, forceOut, comment, packagecode, isauto, reasonid, iszhongzhuanout, credate, anbaochuku, true, isAutoSupplyLink);
	}

	/**
	 * 获取当前站点id列表中， 货物流向设置的正向站点
	 *
	 * @param currentBranchids
	 *            当前站点branchid或数组
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public List<Branch> getNextPossibleBranches(long... currentBranchids) {

		List<Branch> bList = new ArrayList<Branch>();
		Set<Long> branchidSet = new LinkedHashSet<Long>();

		for (long currentBranchid : currentBranchids) {
			if (currentBranchid != 0) {
				for (long i : this.cwbRouteService.getNextPossibleBranch(currentBranchid)) {
					if (!branchidSet.contains(i)) {
						branchidSet.add(i);
					}
				}
			}
		}
		for (Long branchid : branchidSet) {
			Branch branch = this.branchDAO.getBranchByBranchid(branchid);
			if ((branch != null) && (branch.getBranchid() != 0)) {
				bList.add(branch);
			}
		}
		return bList;
	}

	private void deliveryPosAppJmsNotify(OrderFlow orderFlowObj) throws IOException, JsonParseException, JsonMappingException {

		if (orderFlowObj.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
			DeliverServerParamVO paramVO = this.deliverService.getDeliverServerParamVO(PosEnum.DeliverServerAPP.getKey());
			if (paramVO != null) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.om.readValue(orderFlowObj.getFloworderdetail(), CwbOrderWithDeliveryState.class);
				DeliveryState ds = cwbOrderWithDeliveryState.getDeliveryState();
				if ((ds.getPos().compareTo(BigDecimal.ZERO) > 0) && (ds.getPosremark().contains("POS反馈") || ds.getPosremark().contains("POS刷卡"))) {
					this.deliverService.posFeedbackNotifyApp(ds.getCwb());
				}
			}

		}

	}

	/**
	 * 派件服务监听接口 (分站领货)
	 *
	 * @param orderFlow
	 */
	@Consume(uri = CwbOrderService.MQ_FROM_URI_DELIVERY_APP_JMS_ORDER_FLOW + "?concurrentConsumers=5")
	public void deliverAppJms(@Header("orderFlow") String orderFlow, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			// logger.info("棒棒糖派件服务JMS监听：START");
			OrderFlow orderFlowObj = this.om.readValue(orderFlow, OrderFlow.class);
			this.deliverAppJmsHandel(orderFlowObj);

			this.deliveryPosAppJmsNotify(orderFlowObj); // POS刷卡通知

		} catch (Exception e) {
			CwbOrderService.logger.info("棒棒糖派件服务JMS监听异常,orderFlow:{}", orderFlow);
			CwbOrderService.logger.error("", e);

			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".deliverAppJms").buildExceptionInfo(e.toString())
					.buildTopic(CwbOrderService.MQ_FROM_URI_DELIVERY_APP_JMS_ORDER_FLOW).buildMessageHeaderUUID(messageHeaderUUID).buildMessageHeader("orderFlow", orderFlow)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	/**
	 * 派件服务-派件通知
	 *
	 * @param orderFlowObj
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Transactional
	private void deliverAppJmsHandel(OrderFlow orderflow) throws JsonParseException, JsonMappingException, IOException {
		if (orderflow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {

			DeliverServerParamVO paramVO = this.deliverService.getDeliverServerParamVO(PosEnum.DeliverServerAPP.getKey());
			if (paramVO == null) {
				// logger.info("棒棒糖派件服务-派件通知失败！未开启派件通知服务！");
			} else {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.om.readValue(orderflow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
				CwbOrder co = cwbOrderWithDeliveryState.getCwbOrder();
				Customer customer = this.customerDAO.getCustomerById(co.getCustomerid());
				// 组装请求VO对象
				DeliverServerPushVO dspVO = new DeliverServerPushVO();
				DeliveryState ds = cwbOrderWithDeliveryState.getDeliveryState();
				dspVO.setOuter_trade_no(paramVO.getCode() + (paramVO.getTradeNum() + 1));
				dspVO.setUnid(this.userDAO.getUserByid(ds.getDeliveryid()).get(0).getUsername());
				dspVO.setMerchant_code(paramVO.getCode());
				dspVO.setDelivery_company_code(paramVO.getDelivery_company_code());
				dspVO.setMail_num(co.getCwb());
				dspVO.setDelivery_type(co.getCwbordertypeid() == 1 ? "4" : co.getCwbordertypeid() + "");
				dspVO.setS_company(customer.getCustomername());
				dspVO.setS_contact(customer.getCustomercontactman());
				dspVO.setS_address(customer.getCustomeraddress());
				dspVO.setS_tel(customer.getCustomerphone());
				dspVO.setS_mobile(customer.getCustomerphone());
				dspVO.setS_address("");
				dspVO.setD_company("");
				dspVO.setGoods_fee(this.buildGoodsFee(co));
				dspVO.setD_contact(co.getConsigneename());
				dspVO.setD_tel(StringUtil.isEmpty(co.getConsigneephone()) ? "***********" : co.getConsigneephone());
				dspVO.setD_mobile(StringUtil.isEmpty(co.getConsigneemobile()) ? "***********" : co.getConsigneemobile());
				dspVO.setD_address(co.getConsigneeaddress());
				dspVO.setGoods_info(new ArrayList<GoodInfoVO>());
				// 组装秘钥信息
				String signStr = DigestsEncoder.encode("SHA1", ((dspVO.buildSignStr() + "&" + paramVO.getToken())));
				dspVO.setCode(paramVO.getCode());
				dspVO.setSign(signStr);
				String requestJson = JsonUtil.translateToJson(dspVO);
				try {
					String result = RestHttpServiceHanlder.sendHttptoServer_Json(requestJson, paramVO.getDeliverServerPushUrl());
					CwbOrderService.logger.info("棒棒糖派件服务-派件通知推送完成，订单号：" + dspVO.getMail_num() + "请求报文：" + requestJson + ";响应报文：" + result);
					if (this.isSuccessPush(result)) {
						this.deliverService.updateTradeNum(paramVO, PosEnum.DeliverServerAPP.getKey());
					}
				} catch (Exception e) {
					CwbOrderService.logger.info("棒棒糖派件服务-派件通知推送异常，订单号：" + dspVO.getMail_num() + ",异常：" + e);
				}
			}
		}

	}

	/**
	 * 棒棒糖-派送通知-构建收款金额
	 *
	 * @param co
	 * @return
	 */
	private Integer buildGoodsFee(CwbOrder co) {
		BigDecimal paybackfee = co.getPaybackfee();// 应退金额
		BigDecimal receivablefee = co.getReceivablefee();// 应收金额
		BigDecimal targetGoodsfee = BigDecimal.ZERO;
		CwbOrderTypeIdEnum orderType = CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid());// 订单类型

		if (CwbOrderTypeIdEnum.Peisong.equals(orderType)) {
			targetGoodsfee = null == receivablefee ? BigDecimal.ZERO : receivablefee;
		} else if (CwbOrderTypeIdEnum.Shangmenhuan.equals(orderType) || CwbOrderTypeIdEnum.Shangmentui.equals(orderType)) {
			targetGoodsfee = ((null == paybackfee) || (paybackfee.compareTo(BigDecimal.ZERO) == 0)) ? (((null == receivablefee) || (receivablefee.compareTo(BigDecimal.ZERO) == 0)) ? BigDecimal.ZERO : receivablefee) : paybackfee
					.negate();
		}
		return Integer.valueOf((int) (targetGoodsfee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100));
	}

	/**
	 *
	 * 接收结果处理 单独表格存储，用于监控
	 *
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings("rawtypes")
	private boolean isSuccessPush(String result) throws JsonParseException, JsonMappingException, IOException {
		boolean flag = false;
		Map resultMap = JsonUtil.readValue(result, Map.class);
		if ("ok".equals(resultMap.get("result"))) {
			flag = true;
		}
		return flag;
	}
	public void updatePrinttimeState(CwbOrder smtcd, String printtime) {
		CwbOrderService.logger.info("上门退订单打印记录cwb={}", smtcd.getCwb());
		this.cwbDAO.saveCwbForPrinttime(smtcd.getCwb(), printtime);
		this.shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(smtcd.getCwb(), printtime);
	}

	/**
	 *
	 * @Title: getCwbOrderListByCwb
	 * @description 根据传入的运单号，查询所有运单的信息
	 * @author 刘武强
	 * @date 2016年1月8日下午3:19:50
	 * @param @return
	 * @return List<CwbOrder>
	 * @throws
	 */
	public List<TransCwbDetail> getCwbOrderListByCwb(List<String> transCwbList) {
		List<TransCwbDetail> list = new ArrayList<TransCwbDetail>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (String temp : transCwbList) {
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.transCwbDetailDAO.getTransCwbDetailListByTransCwbs(inStr.substring(0, inStr.length() - 1) + ")");
		return list;
	}

	/**
	 * @Title: getCwbOrderListByCwbs
	 * @description 根据订单号集合，查询订单信息
	 * @author 刘武强
	 * @date 2016年1月11日下午2:14:35
	 * @param @param cwbList
	 * @param @return
	 * @return List<CwbOrder>
	 * @throws
	 */
	public List<CwbOrder> getCwbOrderListByCwbs(List<String> cwbList) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (String temp : cwbList) {
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.cwbDAO.getCwbsBycwbs(inStr.substring(0, inStr.length() - 1) + ")");
		return list;
	}
	
	public List<CwbOrder> getLabelPrintCwbsByCwbs(List<String> cwbList) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (String temp : cwbList) {
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.cwbDAO.getLabelPrintCwbsByCwbs(inStr.substring(0, inStr.length() - 1) + ")");
		return list;
	}

	/**
	 * @Title: getTransCwbsListByCwbs
	 * @description 根据单号集合，从订单-运单对应表找出所有的信息
	 * @author 刘武强
	 * @date 2016年1月8日下午4:46:45
	 * @param @param cwbList
	 * @param @return
	 * @return List<TranscwbView>
	 * @throws
	 */
	public List<TranscwbView> getTransCwbsListByOrderNos(List<String> cwbList) {
		List<TranscwbView> list = new ArrayList<TranscwbView>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (String temp : cwbList) {
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.transCwbDao.getTransCwbByOrderNoList(inStr.substring(0, inStr.length() - 1) + ")");
		return list;
	}

	/**
	 * @Title: getTransCwbsListByCwbNos
	 * @description 根据主单号集合，从订单-运单对应表找出所有的信息
	 * @author 刘武强
	 * @date 2016年1月11日上午9:58:15
	 * @param @param cwbList
	 * @param @return
	 * @return List<TranscwbView>
	 * @throws
	 */
	public List<TranscwbView> getTransCwbsListByCwbNos(List<String> cwbList) {
		List<TranscwbView> list = new ArrayList<TranscwbView>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (String temp : cwbList) {
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.transCwbDao.getTransCwbByOrderNoList(inStr.substring(0, inStr.length() - 1) + ")");
		return list;
	}

	// 拦截功能给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderViewOfIntercept(List<String> scancwblist, List<CwbOrder> clist, List<Customer> customerList, List<Reason> reasonList, List<TransCwbDetail> transOrderList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				if (c.getIsmpsflag() == IsmpsflagEnum.no.getValue()) {// 如果是普通件，直接把需要显示的信息给找出来
					int index = clist.indexOf(c);
					CwbOrderView cwbOrderView = this.setCwbOrderViewValue(scancwblist, clist, customerList, c);
					cwbOrderView.setScancwb(scancwblist.get(index));
					cwbOrderView.setTranscwb(c.getTranscwb());
					cwbOrderView.setEmaildate(c.getEmaildate());
					cwbOrderView.setFlowordertype(c.getFlowordertype());
					cwbOrderView.setCwbstate(c.getCwbstate());
					if (c.getBackreasonid() != 0L) {
						cwbOrderView.setReasonid((int) c.getBackreasonid());
					}
					cwbOrderView.setBackreason(c.getBackreason());
					if (!cwbOrderViewList.contains(cwbOrderView)) {
						cwbOrderViewList.add(cwbOrderView);
					}
				} else {// 如果是一票多件，那么找出他的所有子单，挨个把子单的显示信息带上
					for (TransCwbDetail temp : transOrderList) {
						if (c.getCwb().equals(temp.getCwb())) {
							CwbOrderView cwbOrderView = this.setCwbOrderViewValue(scancwblist, clist, customerList, c);
							cwbOrderView.setScancwb(temp.getTranscwb());
							cwbOrderView.setTranscwb(temp.getTranscwb());
							cwbOrderView.setEmaildate(temp.getEmaildate());
							cwbOrderView.setFlowordertype(temp.getTranscwboptstate());
							cwbOrderView.setCwbstate(temp.getTranscwbstate());
							cwbOrderView.setBackreason(temp.getCommonphrase());
							if (temp.getCommonphraseid() != 0) {
								cwbOrderView.setReasonid(temp.getCommonphraseid());
							}
							if (!cwbOrderViewList.contains(cwbOrderView)) {
								cwbOrderViewList.add(cwbOrderView);
							}
						}
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	private CwbOrderView setCwbOrderViewValue(List<String> scancwblist, List<CwbOrder> clist, List<Customer> customerList, CwbOrder c) {
		CwbOrderView cwbOrderView = new CwbOrderView();
		cwbOrderView.setCwb(c.getCwb());
		cwbOrderView.setSendcarnum(c.getSendcarnum());
		cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
		cwbOrderView.setConsigneename(c.getConsigneename());
		cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
		cwbOrderView.setBackreason(c.getBackreason());
		if (c.getCurrentbranchid() == 0) {
			cwbOrderView.setCurrentbranchid(c.getStartbranchid());
		} else {
			cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
		}
		cwbOrderView.setDeliverystate(c.getDeliverystate());
		cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
		cwbOrderView.setIsmpsflag(c.getIsmpsflag());
		// 针对每个订单的供应商，查看他的集单开关是否打开，用于前台显示拦截原因是否过滤破损和丢失---刘武强16.01.07
		for (Customer customer : customerList) {
			if (customer.getCustomerid() == c.getCustomerid()) {
				cwbOrderView.setMpsswitch(customer.getMpsswitch());
			}
		}

		return cwbOrderView;
	}

	/**
	 *
	 * @Title: dealInterceptForegroundDate
	 * @description 处理前台数据
	 * @author 刘武强
	 * @date 2016年1月11日下午7:27:26
	 * @param @param rJson
	 * @param @param cwbsList
	 * @param @param transList
	 * @param @param cwbNoList
	 * @return void
	 * @throws
	 */
	public void dealInterceptForegroundDate(JSONArray rJson, List<Map<String, String>> cwbsList, List<Map<String, Object>> transList, List<String> cwbNoList) {

		List<Reason> reasonList = this.reasonDAO.getAllReason();// 获取拦截原因的集合
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_intercept_") == -1)) {// 如果数据传过来有错，那不处理
				continue;
			}
			String[] cwb_reasonid = reason.split("_intercept_");

			if (cwb_reasonid.length == 4) {// 只能是有"_intercept_"划分为3段的数据才是前台传过来的
				if (!cwb_reasonid[1].equals("0")) {// 如果中间的为0，那么是不能拦截的数据
					if (cwb_reasonid[2].equals(IsmpsflagEnum.no.getValue() + "")) {// 如果是普通件,把前台传过来的数据放到普通件集合中

						Map<String, String> map = new HashMap<String, String>();
						map.put("cwb", cwb_reasonid[0].trim());
						map.put("reasonid", cwb_reasonid[1]);
						cwbsList.add(map);
					} else {// 如果是一票多件,把前台传过来的数据放到一票多件集合中（运单号、拦截对象）
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("transCwb", cwb_reasonid[0].trim());
						for (Reason temp : reasonList) {// 把拦截原因的类型和原因id绑定
							if (cwb_reasonid[1].equals(temp.getReasonid() + "")) {
								map.put("contentmeaning", temp);
								break;
							}
						}
						transList.add(map);
						if (!cwbNoList.contains(cwb_reasonid[3])) {// 把主单号加到主单集合中，并防止重复
							cwbNoList.add(cwb_reasonid[3].trim());
						}
					}
				}
			}
		}
	}

	/**
	 * @throws Exception
	 * @Title: dealMpsTrans
	 * @description 拦截功能对一票多件的处理：遍历所有子单，修改运单状态、操作状态、下一站和拦截原因的确定
	 *              修改主单的订单状态、操作状态、下一站
	 * @author 刘武强
	 * @date 2016年1月11日上午10:18:07
	 * @param @param cwbNo
	 * @param @param transOrderList
	 * @return void
	 * @throws
	 */
	public void dealMpsTrans(CwbOrder cwbTemp, List<TransCwbDetail> transOrderList, List<Map<String, Object>> transList, List<CwbOrder> cwbOrderList) throws Exception {
		if (cwbTemp == null) {
			throw new Exception("输入的订单信息为空！");
		}
		long flowOrderType = FlowOrderTypeEnum.DaoRuShuJu.getValue();
		List<TransCwbDetail> cwbTransOrderList = new ArrayList<TransCwbDetail>();// 该主单对应的子单，用来保存
		Reason reason = new Reason();// 用来保存主单中的backreason和backreasonid(目前只能去所有子单中的最后一个)
		long nextBranchid = 0;
		int transNum = 0;// 子单总个数
		int posunNum = 0;// 子单破损个数
		int diushiNum = 0;// 子单丢失个数
		flowOrderType = cwbTemp.getFlowordertype();
		for (TransCwbDetail temp : transOrderList) {// 遍历子单,修改子单的运单状态、操作状态、下一站、拦截原因
			if ((temp.getTranscwb() != null) && temp.getCwb().equals(cwbTemp.getCwb())) {// 找出主单对应的子单，由此判断子单的状态和下一站

				cwbTransOrderList.add(temp);
				transNum++;
				Branch transCurrentBranch = this.branchDAO.getBranchByBranchid(temp.getCurrentbranchid());
				// 如果下一站为0，那么说明它处于数据导入、入库、入站之前，那么这个时候下一站不变，未入库、未入站允许进入入库/入站；如果是当前branch是退货组，也不用变下一站（已经在退货组的就不用再去匹配退货组了，在站点需要给他匹配退货组是因为原先的逻辑：如果反馈为待中转，那么就取流程里配置的第一个中转站）
				if ((temp.getCurrentbranchid() != 0) && (transCurrentBranch != null) && (transCurrentBranch.getSitetype() != BranchEnum.TuiHuo.getValue())) {
					List<Branch> branchidList = this.cwbRouteService.getNextInterceptBranch(temp.getCurrentbranchid());// 根据站点的流向配置，找到他对应的退货组
					if ((branchidList.size() == 0)) {// 如果没有配置的退货组，则异常
						throw new Exception("没有配置逆向退货组");
					} else { // 如果配置的退货组大于1，那么默认取第一个
						nextBranchid = branchidList.get(0).getBranchid();
					}
					temp.setNextbranchid(nextBranchid);
				}else if(temp.getCurrentbranchid() == 0 && temp.getPreviousbranchid() != 0) {
					//Added by leoliao at 2016-06-24 当前站为0则，取上一站，然后获取其对应的退货组。订单拦截即使是出库未到站，也需要进行拦截，此时可以在站点直接进行退货出站操作！
					Branch transPreviousBranch = this.branchDAO.getBranchByBranchid(temp.getPreviousbranchid());
					if(temp.getPreviousbranchid() != 0 && transPreviousBranch != null){
						List<Branch> branchidList = this.cwbRouteService.getNextInterceptBranch(temp.getPreviousbranchid());// 根据站点的流向配置，找到他对应的退货组
						if ((branchidList.size() == 0)) {
							// 如果没有配置的退货组，则异常
							logger.error("运单(运单号={},上一站={})没有配置逆向退货组", temp.getTranscwb(), transPreviousBranch.getBranchname());
							throw new Exception("运单[运单号="+temp.getTranscwb()+",上一站="+transPreviousBranch.getBranchname()+"]没有配置逆向退货组");
						}
						
						// 如果配置的退货组大于1，那么默认取第一个
						nextBranchid = branchidList.get(0).getBranchid();
						
						temp.setNextbranchid(nextBranchid);
					}
					//Added end
				}else if(temp.getCurrentbranchid() == 0 && temp.getPreviousbranchid() == 0 && temp.getNextbranchid() != 0){
					//Added by leoliao at 2016-07-20 当前站、上一站都为0则，取下一站，然后获取其对应的退货组。订单拦截即使是出库未到站，也需要进行拦截，此时可以在站点直接进行退货出站操作！
					Branch transNextBranch = this.branchDAO.getBranchByBranchid(temp.getNextbranchid());
					if(transNextBranch != null){
						List<Branch> branchidList = this.cwbRouteService.getNextInterceptBranch(temp.getNextbranchid());// 根据站点的流向配置，找到他对应的退货组
						if ((branchidList.size() == 0)) {
							// 如果没有配置的退货组，则异常
							logger.error("运单(运单号={},下一站={})没有配置逆向退货组", temp.getTranscwb(), transNextBranch.getBranchname());
							throw new Exception("运单[运单号="+temp.getTranscwb()+",下一站="+transNextBranch.getBranchname()+"]没有配置逆向退货组");
						}
						
						// 如果配置的退货组大于1，那么默认取第一个
						nextBranchid = branchidList.get(0).getBranchid();
						
						temp.setNextbranchid(nextBranchid);
					}					
					//Added end
				}
				// 如果运单状态为入库、中转入库，那么当前站改为0----应鹏凯要求
				if ((temp.getTranscwboptstate() == FlowOrderTypeEnum.RuKu.getValue()) || (temp.getTranscwboptstate() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue())) {
					temp.setCurrentbranchid(0);
				}
				// 先默认该运单为非拦截的，把属性set上（运单状态、操作状态、下一站），如果是拦截，可以在下面的for循环里面重新赋值
				temp.setTranscwboptstate(FlowOrderTypeEnum.DingDanLanJie.getValue());
				if ((temp.getTranscwbstate() == TransCwbStateEnum.PEISONG.getValue()) || (temp.getTranscwbstate() == TransCwbStateEnum.ZHONGZHUAN.getValue())) {// 当兄弟子单是正常配送的话，需要变为退货，否则保持原状态
					temp.setTranscwbstate(TransCwbStateEnum.TUIHUO.getValue());
				}

				for (Map<String, Object> map : transList) {// 修改子单的数据，从而接下来可以直接保存
					if ((map.get("transCwb") != null) && map.get("transCwb").equals(temp.getTranscwb())) {
						reason = (Reason) map.get("contentmeaning");
						temp.setCommonphraseid((int) reason.getReasonid());
						temp.setCommonphrase(reason.getReasoncontent());
						temp.setTranscwbstate(reason.getInterceptType());
						break;
					}
				}
				if (temp.getTranscwbstate() == TransCwbStateEnum.POSUN.getValue()) {
					posunNum++;
				} else if (temp.getTranscwbstate() == TransCwbStateEnum.DIUSHI.getValue()) {
					diushiNum++;
				}
			}
		}

		if (transNum == posunNum) {// 修改主单的订单状态
			cwbTemp.setCwbstate(CwbStateEnum.WANQUANPOSUN.getValue());
		} else if (transNum == diushiNum) {
			cwbTemp.setCwbstate(CwbStateEnum.DiuShi.getValue());
		} else if (diushiNum > 0) {
			cwbTemp.setCwbstate(CwbStateEnum.BUFENDIUSHI.getValue());
		} else if (posunNum > 0) {
			cwbTemp.setCwbstate(CwbStateEnum.BUFENPOSUN.getValue());
		} else {
			cwbTemp.setCwbstate(CwbStateEnum.TuiHuo.getValue());
		}

		// 如果下一站为0，那么说明它处于数据导入、入库、入站之前，那么这个时候下一站不变，未入库、未入站允许进入入库/入站；如果是当前branch是退货组，不用变下一站（已经在退货组的就不用再去匹配退货组了）
		/*
		Branch cwbCurrentBranch = this.branchDAO.getBranchByBranchid(cwbTemp.getCurrentbranchid());
		if ((cwbTemp.getCurrentbranchid() != 0) && (cwbCurrentBranch != null) && (cwbCurrentBranch.getSitetype() != BranchEnum.TuiHuo.getValue())) {
			cwbTemp.setNextbranchid(nextBranchid);// 修改主单的下一站
		}
		*/
		//Modified by leoliao at 2016-06-24  当前站为0（说明包裹在途中）或者当前站不是退货组,订单拦截时也是需要把下一站改为退货站。
		Branch cwbCurrentBranch = this.branchDAO.getBranchByBranchid(cwbTemp.getCurrentbranchid());
		if(cwbTemp.getCurrentbranchid() == 0 || (cwbCurrentBranch != null && cwbCurrentBranch.getSitetype() != BranchEnum.TuiHuo.getValue())) {
			cwbTemp.setNextbranchid(nextBranchid);// 修改主单的下一站(直接取最后一个运单所对应的退货组，存在到错货的情况下，可能会错误)
		}
		//Modified end

		// 如果运单状态为入库、中转入库，那么当前站改为0----应鹏凯要求
		if ((cwbTemp.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || (cwbTemp.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue())) {
			cwbTemp.setCurrentbranchid(0);
		}
		cwbTemp.setFlowordertype(FlowOrderTypeEnum.DingDanLanJie.getValue());// 修改主单的操作流程
		cwbTemp.setMpsoptstate(FlowOrderTypeEnum.DingDanLanJie.getValue());// 修改主单的操作流程
		long isypdjusetranscwb = this.customerDAO.getCustomerById(cwbTemp.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(cwbTemp.getCustomerid()).getIsypdjusetranscwb();

		// 保存子单
		this.transCwbDetailDAO.saveWithMount(cwbTransOrderList);
		// 保存主单
		CwbOrderService.logger.info("拦截前数据  cwb:" + cwbTemp.getCwb() + ";扫描件数:" + cwbTemp.getScannum() + ";发货件数：" + cwbTemp.getSendnum() + ";取货件数：" + cwbTemp.getBackcarnum());
		this.handleInterceptChack(this.getSessionUser(), cwbTemp.getCwb(), cwbTemp.getCwb(), cwbTemp, isypdjusetranscwb, reason);
		CwbOrderService.logger.info("拦截后数据  cwb:" + cwbTemp.getCwb() + ";扫描件数:" + cwbTemp.getScannum() + ";发货件数：" + cwbTemp.getSendnum() + ";取货件数：" + cwbTemp.getBackcarnum());
	}

	/**
	 * @throws Exception
	 *
	 * @Title: getNextBranchid
	 * @description 入库前、站点到货前、中转入库前被拦截，那么入的时候下一站的获取方法
	 * @author 刘武强
	 * @date 2016年1月12日下午3:36:46
	 * @param @param currentbranchid
	 * @param @return
	 * @param @throws Exception
	 * @return long
	 * @throws
	 */
	public long getNextBranchid(String cwb, Long currentbranchid) throws CwbException {
		long nextBranchid = 0;
		List<Branch> branchidList = this.cwbRouteService.getNextInterceptBranch(currentbranchid);// 根据站点的流向配置，找到他对应的退货组
		if ((branchidList.size() > 1)) {
			throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.NEXT_STATION_NOT_UNIQUE_ERROR);
		} else if ((branchidList.size() == 0)) {
			throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.NONE_NEXT_STATION_ERROR);
		} else {
			nextBranchid = branchidList.get(0).getBranchid();
		}
		return nextBranchid;
	}

	/**
	 *
	 * @Title: dealCancelIntercept
	 * @description
	 *              撤销拦截的处理方法：被撤销的运单一定变为退货；找出所有兄弟运单，如果兄弟中有丢失，那主单变部分丢失；如果兄弟中含有破损，但没有丢失
	 *              ，主单为部分破损；否则主单变退货；
	 * @author 刘武强
	 * @date 2016年1月14日上午9:42:21
	 * @param @param transCwb
	 * @param @return
	 * @param @throws CwbException
	 * @return boolean
	 * @throws
	 */
	public boolean dealCancelIntercept(String transCwb) throws CwbException {
		try {
			List<TransCwbDetail> transCwbDetailList = this.transCwbDetailDAO.queryTransCwbDetailBytranscwb(transCwb);
			TransCwbDetail interceptTransCwbDetail = new TransCwbDetail();
			int posunNum = 0;
			int diushiNum = 0;
			int cwbState = CwbStateEnum.TuiHuo.getValue();
			String cwb = "";
			for (TransCwbDetail temp : transCwbDetailList) {// 根据运单的状态，判断主单的状态
				if (transCwb.equals(temp.getTranscwb())) {
					interceptTransCwbDetail = temp;
				}
				if (temp.getTranscwbstate() == TransCwbStateEnum.DIUSHI.getValue()) {
					diushiNum++;
				} else if (temp.getTranscwbstate() == TransCwbStateEnum.POSUN.getValue()) {
					posunNum++;
				}
				cwb = temp.getCwb();
			}
			if (diushiNum > 1) { // 如果兄弟运单中有丢失（被撤销的这个运单目前状态依然为丢失，所以丢失的个数至少为1），那么主单变为部分丢失
				cwbState = CwbStateEnum.BUFENDIUSHI.getValue();
			} else if ((diushiNum == 1) && (posunNum > 0)) {// 如果兄弟运单中没有丢失，但是含有破损，那么主单变为部分破损
				cwbState = CwbStateEnum.BUFENPOSUN.getValue();
			} else {// 其余的情况都是退货
				cwbState = CwbStateEnum.TuiHuo.getValue();
			}
			// 更新运单的状态
			this.transCwbDetailDAO.updateTransCwbDetailBytranscwb(transCwb, TransCwbStateEnum.TUIHUO.getValue());

			// 更新主单状态
			this.cwbDAO.updateCwbStateByCwb(cwb, cwbState);
			// 根据撤销的运单，更新主单的相关属性
			if ((interceptTransCwbDetail != null) && (interceptTransCwbDetail.getTranscwb() != null)) {

				this.mPSOptStateService
						.updateMPSInfo(transCwb, FlowOrderTypeEnum.getByValue(interceptTransCwbDetail.getTranscwboptstate()), 0L, interceptTransCwbDetail.getCurrentbranchid(), interceptTransCwbDetail
								.getNextbranchid());

			}
			return true;
		} catch (Exception e) {
			CwbOrderService.logger.error("", e);
			return false;
		}
	}

	@Transactional
	public void updateCwbFlowOrderType(String cwb) {
		this.cwbDAO.updateCwbFlowOrdertype(cwb, FlowOrderTypeEnum.DingDanLanJie.getValue());
	}

	/**
	 *
	 * @Title: nonSupportMpsValid
	 * @description 退货中转入库和分拣中转出库都不支持一票多件的校验方法
	 * @author 刘武强
	 * @date 2016年1月27日下午5:16:42
	 * @param @param co
	 * @return void
	 * @throws
	 */
	public void nonSupportMpsValid(CwbOrder co) {
		if (co == null) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if (co.getIsmpsflag() == IsmpsflagEnum.yes.getValue()) {
			// 只有flowordertype是分拣库入库或中转站入库的订单能够进入分拣中转出库，如果是分拣库入库，那么flowordertype变为分拣库出库，如果是中转站入库则中转站出库
			// 只有下一站指向退货站或中转站的订单会进入退货中转入库，如果指向退货站那么flowordertype变为退货站入库，如果指向中转站则中转站入库
			long flowordertype = 0;
			boolean flag = true;
			// 首先判断分拣中转出库的情况
			if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				flowordertype = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
				flag = false;
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				flowordertype = FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
				flag = false;
			}
			if (!flag) {// 如果分拣中转出库，抛出相应的异常
				throw new CwbException(co.getCwb(), flowordertype, ExceptionCwbErrorTypeEnum.TUIHUOZHONGZHUANRUKU_NONSUPPORT_MPS);
			}

			Branch cwbCurrentBranch = this.branchDAO.getBranchByBranchid(co.getCurrentbranchid());
			if ((cwbCurrentBranch != null) && (cwbCurrentBranch.getSitetype() != BranchEnum.TuiHuo.getValue())) {
				flowordertype = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();
				flag = false;
			} else if ((cwbCurrentBranch != null) && (cwbCurrentBranch.getSitetype() != BranchEnum.ZhongZhuan.getValue())) {
				flowordertype = FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
				flag = false;
			}
			if (!flag) {// 如果退货中转入库，抛出相应的异常
				throw new CwbException(co.getCwb(), flowordertype, ExceptionCwbErrorTypeEnum.FENJIANZHONGZHUANCHUKU_NONSUPPORT_MPS);
			} else {// 如果都不是，那么抛出非本站货这个异常
				throw new CwbException(co.getCwb(), flowordertype, ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
			}
		}
	}
	/**
	 * 更新正向订单的deliverystate
	 * @param cwbordertypeid 订单类型
	 * @param cwb 订单号
	 * @param deliverystate 配送状态
	 * @author neo01.huang
	 */
	public void updateForwardOrderDeliveryState(Integer cwbordertypeid, String cwb, int deliverystate) {
		CwbOrderService.logger.info("updateForwardOrderDeliveryState->cwbordertypeid:{},cwb:{},deliverystate:{}", cwbordertypeid, cwb, deliverystate);
		if (cwbordertypeid == null) {
			CwbOrderService.logger.info("updateForwardOrderDeliveryState->cwbordertypeid is null");
			return;
		}
		if (!((cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) || (cwbordertypeid == CwbOrderTypeIdEnum.OXO.getValue()) || (cwbordertypeid == CwbOrderTypeIdEnum.OXO_JIT.getValue()) || (cwbordertypeid == CwbOrderTypeIdEnum.Express
				.getValue()))) {
			CwbOrderService.logger.info("updateForwardOrderDeliveryState->cwb:{}不是正向订单");
			return;
		}
		if ((cwb == null) || (cwb.length() == 0)) {
			CwbOrderService.logger.info("updateForwardOrderDeliveryState->cwb is null or empty");
			return;
		}
		//更新deliverystate的值
		this.cwbDAO.updateDeliveryStateBycwb(cwb, deliverystate);
		//更新deliverystate的值
		this.deliveryStateDAO.updateDeliveryStateValue(cwb, deliverystate);
		CwbOrderService.logger.info("updateForwardOrderDeliveryState->update ok!");
	}
	public List<CwbOrder> getListByBale(long baleid, long page) {
		List<String> orderNoStrs = this.baleCwbDAO.getCwbsByBale("" + baleid);
		//所有真实订单号 Strs
		StringBuilder cwbsSB = new StringBuilder();
		String cwbs = "";
		if ((null != orderNoStrs) && !orderNoStrs.isEmpty()) {
			for (String tempNo : orderNoStrs) {
				String targetCwb = this.translateCwb(tempNo);
				cwbsSB.append("'").append(targetCwb).append("',");
			}
		}
		if (cwbsSB.length() > 0) {
			cwbs = cwbsSB.substring(0, cwbsSB.length() - 1);
		}
		if ((cwbs != null) && (cwbs.length() > 0)) {
			if (page > 0) {
				return this.cwbDAO.getCwbByCwbsPage(page, cwbs);
			} else {
				return this.cwbDAO.getcwborderList(cwbs);
			}
		} else {
			return null;
		}
	}
	
	public void handleShangMenTuiCwbDeliveryPermit(CwbOrderDTO cwbOrder) {
		if (cwbOrder != null) {
			String enumKey = this.getB2cEnumKeys(this.customerDAO.getCustomerById(cwbOrder.getCustomerid()), "vipshop");
			if ((enumKey != null) && (CwbOrderTypeIdEnum.Shangmentui.getValue() == (int)cwbOrder.getCwbordertypeid())) {
				if (cwbOrder.getCwb().contains("-T")) {
					CwbOrder order = this.cwbDAO.getCwbByCwb(cwbOrder.getCwb().substring(0, cwbOrder.getCwb().indexOf("-T"))); // 去掉-T拿到之前的配送单
					if (order == null) { //没有关联的配送单
						return;
					}
					if (order.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
						if (order.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()
								|| order.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
							cwbDAO.updateCwbDeliveryPermit(cwbOrder.getCwb());//上门退订单有关联的拒收配送单更新领货标识为不可领货
						}
					}
				}
			}
		}
	}
	
	/**
	 * 唯品会配送单拒收/部分拒收关联上门退订单不能领货
	 * 配送成功更新订单可以领揽退单 modify by jian_xie 2016-07-27
	 */
	public void handlePeiSongCwbDeliveryPermit(CwbOrder cwbOrder, long delivery_state) {
		if (cwbOrder != null) {
			String enumKey = this.getB2cEnumKeys(this.customerDAO.getCustomerById(cwbOrder.getCustomerid()), "vipshop");
			// 唯品会的配送单
			if(enumKey != null && CwbOrderTypeIdEnum.Peisong.getValue() == cwbOrder.getCwbordertypeid()){
				if(delivery_state == DeliveryStateEnum.JuShou.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()){
					cwbDAO.updateCwbDeliveryPermitByPeiSong(cwbOrder.getCwb(), 1);//上门退订单有关联的拒收配送单更新领货标识为不可领货
				}else if(delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()){
					cwbDAO.updateCwbDeliveryPermitByPeiSong(cwbOrder.getCwb(), 0);//上门退订单有关联的拒收配送单更新领货标识为可领货
				}
			}
		}
	}
	
	public String haveRelatedShangMenTuiCwb (CwbOrder cwbOrder) {
		if (cwbOrder != null && (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) ){
			String shangMenTuicwb = cwbDAO.queryRelatedShangMenTuiCwb(cwbOrder.getCwb());
			if (shangMenTuicwb != null && !shangMenTuicwb.isEmpty()) {
				return cwbOrder.getCwb();
			}
		}
		return "";
	}
	
	/**
	 * 
	 * 根据传入的订单，判断该订单是否是一票多件非集单并且存在一个运单为到货状态且不全为到货
	 * @author 刘武强
	 * @param order
	 * @return
	 */
	private boolean getFenZhanDaoHuoFlag(CwbOrder order,long isypdjusetranscwb,FlowOrderTypeEnum flowOrderTypeEnum){
		if(order == null){//如果order为空，那么直接返回false
			return false;
		}
		boolean mpsSwitch=order.getIsmpsflag()>0;
		if(mpsSwitch){ //如果为集单模式，直接返回false
			return false;
		}
		String cwb = order.getCwb();
		if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
			if (order.getTranscwb().indexOf(",") > -1) {
				String[] transCwbs = order.getTranscwb().split(",");
				int i = transCwbs.length;
				for (String transcwb : transCwbs) {//遍历所有的运单，查看是否有运单的状态为到货
					if ("".equals(transcwb.trim())) {
						continue;
					}
					//从运单轨迹表中得到运单当前的状态记录
					TranscwbOrderFlow tcof = this.transcwborderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwb);
					
					if(tcof != null && tcof.getFlowordertype() == flowOrderTypeEnum.getValue()){
						i -- ;
					}
				}
				if(i != 0 && i < transCwbs.length){//如果订单不全为到货，只是有部分为到货，那么返回true
					return true;
				}
			}
		}		
		return false;
	}
	
	/**
	 * 订单站点匹配到小件员-分页
	 * 2016年6月15日 下午6:21:31
	 * @param page
	 * @param customerid
	 * @param ordercwb
	 * @param emaildateid
	 * @param addressCodeEditType
	 * @param onePageNumber
	 * @param branchid
	 * @return
	 */
	public List<CwbOrderBranchMatchVo> getCwbBranchMatchVoByPageMyWarehouse(long page, long customerid, String ordercwb,
			long emaildateid, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long onePageNumber, long branchid) {
		List<CwbOrder> cwbOrderList = this.cwbDAO.getcwbOrderByPageIsMyWarehouse(page, customerid, ordercwb, emaildateid,
				addressCodeEditType, onePageNumber, branchid);
		return this.cwbOrderToBranchMatchVo(cwbOrderList);
	}
	
	/**
	 * 订单站点匹配到小件员
	 * 2016年6月17日 下午4:17:33
	 * @param cwbList
	 * @return
	 */
	public List<CwbOrderBranchMatchVo> getCwbBranchMatchByCwbs(List<String> cwbList) {
		StringBuffer cwbBuffer = new StringBuffer();
		String cwbstrs = "";
		for (int i = 0; i < cwbList.size(); i++) {
			cwbBuffer = cwbBuffer.append("'" + cwbList.get(i) + "',");
		}
		if (cwbBuffer.length() > 0) {
			cwbstrs = cwbBuffer.toString().substring(0, cwbBuffer.length() - 1);
		}
		List<CwbOrder> cwbOrderList = this.cwbDAO.getCwbByCwbs(cwbstrs);
		return this.cwbOrderToBranchMatchVo(cwbOrderList);
	}
	
	/**
	 * 订单转换
	 * 2016年6月17日 下午4:16:51
	 * @param cwbOrderList
	 * @return
	 */
	private List<CwbOrderBranchMatchVo> cwbOrderToBranchMatchVo(List<CwbOrder> cwbOrderList) {
		List<CwbOrderBranchMatchVo> voList = new ArrayList<CwbOrderBranchMatchVo>(cwbOrderList.size());
		for(CwbOrder cwbOrder : cwbOrderList) {
			voList.add(this.cwbOrderToBranchMatchVo(cwbOrder));
		}
		return voList;
	}
	
	/**
	 * 订单转换
	 * 2016年6月17日 下午4:17:02
	 * @param cwbOrder
	 * @return
	 */
	private CwbOrderBranchMatchVo cwbOrderToBranchMatchVo(CwbOrder cwbOrder) {
		CwbOrderBranchMatchVo vo = new CwbOrderBranchMatchVo();
		vo.setCwbOrder(cwbOrder);
		CwbFlowOrderTypeEnum cwbFlowOrderType =  CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype());
		vo.setFlowordertypeVal(cwbFlowOrderType == null ? null : cwbFlowOrderType.getText());
		CwbOrderTypeIdEnum cwbOrderType = CwbOrderTypeIdEnum.getByValue(cwbOrder.getCwbordertypeid());
		vo.setOrderTypeVal(cwbOrderType == null ? null : cwbOrderType.getText());
		if(cwbOrder.getDeliverybranchid() != 0) {
			List<User> courierList = this.userDao.getUserByRoleAndBranchid(2, cwbOrder.getDeliverybranchid());
			vo.setCourierList(courierList);
		}
		return vo;
	}
	
	/**
	 * 根据小件员ID过滤
	 * 2016年6月23日 下午3:36:53
	 * @return
	 */
	public List<CwbOrder> filterCwbOrderByDeliver(List<CwbOrder> cwbOrderList, long deliverid) {
		if (deliverid <= 0 || cwbOrderList == null) {
			return cwbOrderList;
		}
		List<CwbOrder> cwbOrdersDeliver = new ArrayList<CwbOrder>();
		for (CwbOrder cwb : cwbOrderList) {
			// 显示未匹配的小件员和匹配相同的小件员
			if (cwb.getExceldeliverid() == 0 || cwb.getExceldeliverid() == deliverid) {
				cwbOrdersDeliver.add(cwb);
			}
		}
		return cwbOrdersDeliver;
	}
	/**
	 * 根据站点的流向配置，找到对应的退货组
	 * @author leo01.liao
	 * @param branchId
	 * @return
	 */
	private long getNextInterceptBranchId(long branchId){
		long nextInterceptBranchId = 0;
		
		// 根据站点的流向配置，找到对应的退货组
		List<Branch> listNextInterceptBranch = this.cwbRouteService.getNextInterceptBranch(branchId); 
		if(listNextInterceptBranch != null && !listNextInterceptBranch.isEmpty()){
			nextInterceptBranchId = listNextInterceptBranch.get(0).getBranchid(); //默认取第一个
		}
		
		return nextInterceptBranchId;
	}

	/**
	 * 判断快递单是否是本站揽收/配送
	 * @param co
	 * @param deliveryUser
	 */
	private void validateExpress(CwbOrder co, User deliveryUser) {
		String cwb  = co.getCwb();
		OrderFlow orderFlow = orderFlowDAO.queryFlow(cwb,FlowOrderTypeEnum.LanJianRuZhan);
		if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()
				&& orderFlow != null ) { // 揽件入站的快递单
				long lanjianBranchid = orderFlow.getBranchid(); //揽件站点
				long deliveryBranchid = co.getDeliverybranchid();//匹配站点
				long operateBranchid = deliveryUser.getBranchid();//当前操作站点
				if (!(deliveryBranchid == operateBranchid && deliveryBranchid == lanjianBranchid)) {
					throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), 
							ExceptionCwbErrorTypeEnum.Ling_huo_EXPRESS_LIMIT);
				}
		} 
	}
}
