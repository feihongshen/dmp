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
import org.springframework.stereotype.Service;

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
import cn.explink.dao.LogTodayByTuihuoDAO;
import cn.explink.dao.LogTodayByWarehouseDAO;
import cn.explink.dao.LogTodayLogDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
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
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

@Service
public class LogToDayExportService {
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
	LogTodayByWarehouseDAO logTodayByWarehouseDAO;
	@Autowired
	LogTodayByTuihuoDAO logTodayByTuihuoDAO;
	@Autowired
	LogTodayLogDAO logTodayDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ComplaintDAO complaintDAO;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void excelPublicMethodByTuihuo(HttpServletResponse response, String branchids, String type, String startTime, String exportmould2) {
		String sql = "";
		if ("zhandianyingtuihuo".equals(type)) {
			sql = cwbDAO.getZhanDianYingtuiSql(FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
					+ DeliveryStateEnum.ShangMenTuiChengGong.getValue());
		}
		if ("zhandiantuihuochukuzaitu".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("tuihuozhanruku".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("tuihuozhantuihuochukuzaitu".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbsByTuihuozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchids, startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("tuigonghuoshangchuku".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("gonghuoshangshouhuo".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("gonghuoshangjushoufanku".equals(type)) {
			List<String> cwborderList = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (String cwb : cwborderList) {
					cwbs += "'" + cwb + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		excelMethod(response, sql, exportmould2);
	}

	public void excelPublicMethod(HttpServletResponse response, long branchid, long customerid, String type, String startTime, String endTime, String exportmould2) {
		String sql = "";

		if ("weiruku".equals(type)) {
			sql = cwbDAO.getWeirukuListSql(branchid, customerid, FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "," + FlowOrderTypeEnum.DaoRuShuJu.getValue() + ","
					+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		}
		if ("yiruku".equals(type)) {
			List<String> cwbList = logTodayByWarehouseDAO.getYirukuCwbAll(customerid, branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("daocuohuo".equals(type)) {
			sql = cwbDAO.getDaocuohuoListSql(branchid, customerid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		}
		if ("zuorichukuzaitu".equals(type)) {
			sql = cwbDAO.getZuorichukuzaituListSql(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), customerid, startTime);
		}
		if ("jinrichukuzaitu".equals(type)) {
			sql = cwbDAO.getJinrichukuzaituListSql(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), customerid, startTime);
		}
		if ("chukuyidaozhan".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, customerid,
					startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getYichukudaozhanSql(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), customerid, startTime, cwbs);
		}
		if ("weichukuyidaozhan".equals(type)) {
			sql = cwbDAO.getLousaodaozhanSql(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, customerid, startTime);
		}
		if ("kucun".equals(type)) {
			sql = cwbDAO.getJinriKucunSql(branchid, customerid);
		}
		excelMethod(response, sql, exportmould2);
	}

	/**
	 * 库房今日日志全部供货商汇总导出
	 * 
	 * @param response
	 * @param branchid
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @param exportmould2
	 */
	public void excelPublicByAllCustomeridMethod(HttpServletResponse response, long branchid, String type, String startTime, String endTime, String exportmould2) {
		String sql = "";

		if ("weiruku".equals(type)) {
			sql = cwbDAO.getWeirukuListSql(branchid,
					FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "," + FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		}
		if ("yiruku".equals(type)) {
			List<String> cwbList = logTodayByWarehouseDAO.getYirukuCwbAll(branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("daocuohuo".equals(type)) {
			sql = cwbDAO.getDaocuohuoListSql(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		}
		if ("zuorichukuzaitu".equals(type)) {
			sql = cwbDAO.getZuorichukuzaituListSql(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), startTime);
		}
		if ("jinrichukuzaitu".equals(type)) {
			sql = cwbDAO.getJinrichukuzaituListSql(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), startTime);
		}
		if ("chukuyidaozhan".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getYichukudaozhanSql(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, cwbs);
		}
		if ("weichukuyidaozhan".equals(type)) {
			sql = cwbDAO.getLousaodaozhanSql(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, startTime);
		}
		if ("kucun".equals(type)) {
			sql = cwbDAO.getJinriKucunSql(branchid);
		}
		excelMethod(response, sql, exportmould2);
	}

	public void excelPublicByZhandianMethod(HttpServletResponse response, long branchid, String type, String exportmould2, String startTime, String endTime) {
		String sql = "";

		if ("daohuo_jinriyichuku".equals(type)) {
			List<String> cwbList = logTodayDAO.getTodayWeidaohuoCwbbyChukuNextbranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);

		}
		if ("daohuo_weidaohuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getTodayWeidaohuoCwbbyChukuNextbranchidCwb(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			;
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("daohuo_yidaohuo".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanByZhandianCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			sql = cwbDAO.getYichukudaozhanByZhandianSql(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "", startTime, cwbs, branchid);
		}
		if ("daohuo_lousaoyidaohuo".equals(type)) {
			sql = cwbDAO.getLousaodaozhanByZhandianSql(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
		}
		if ("daohuo_tazhandaohuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getCwbTodaybyOrtherBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("daohuo_shaohuo".equals(type)) {

			sql = cwbDAO.getSqlByCwb("'--'");
		}
		if ("daohuo_daocuohuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_daohuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_zuorizhiliu".equals(type)) {// 页面却掉显示

			sql = cwbDAO.getSqlByCwb("--");
		}
		if ("toudi_daozhanweiling".equals(type)) {
			List<String> cwbList = logTodayDAO.getCountbyDedailtCwb(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_linghuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_zuoriweiguiban".equals(type)) {

			sql = cwbDAO.getSqlByCwb("--");
		}
		if ("toudi_peisongcheng".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_jushou".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.JuShou.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_jinrizhiliu".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCwbByBranchidAndDeliveryStateAndCredateByJinri(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(),
					startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_bufenjushou".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.BuFenTuiHuo.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_zhongzhuan".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_shangmentuichenggong".equals(type)) {

			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}

		if ("toudi_shangmentuijutui".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.ShangMenJuTui.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_shangmenhuanchenggong".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}

		if ("toudi_shangmenhuanjuhuan".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderTypeCwb(branchid, DeliveryStateEnum.JuShou.getValue(),
					CwbOrderTypeIdEnum.Shangmenhuan.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_diushi".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.HuoWuDiuShi.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_fankuiheji".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);

		}
		if ("toudi_weifankuiheji".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}

		if ("kuanxiang_peisongchenggong".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kuanxiang_shangmentui".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kuanxiang_shangmenhuan".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kuanxiang_shijiaokuan".equals(type)) {
			List<String> cwbList = logTodayDAO.getPayUpByDeliverybranchidAndDeliverystateAndCredateTo(branchid, startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kucun_zuorikucun".equals(type)) {

			sql = cwbDAO.getSqlByCwb("'--'");
		}
		if ("kucun_daohuo".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kucun_tuotou".equals(type)) {
			List<String> cwbList = new ArrayList<String>();
			String deliverystate = DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			String[] deliverystateList = deliverystate.split(",");
			for (String deliveryst : deliverystateList) {
				List<String> daocuohuoList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, Long.parseLong(deliveryst), startTime, endTime);

				cwbList.addAll(daocuohuoList);
			}
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kucun_tuihuochuku".equals(type)) {
			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("kucun_zhongzhuanchuku".equals(type)) {

			List<String> cwbList = logTodayDAO.getOrderBybranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}

		if ("jushou_kuicun".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateCwb(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.JuShou.getValue());
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("zhiliu_kuicun".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateCwb(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue());
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("weiguiban_kuicun".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("toudi_fankuiweiguiban_heji".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime);
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		if ("zuori_fankui_leijizhiliu".equals(type)) {
			List<String> cwbList = deliveryStateDAO.getCwbByBranchidAndDeliveryStateAndCredateByZuori(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(),
					(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + " 00:00:00");
			String cwbs = getCwbs(cwbList);
			sql = cwbDAO.getSqlByCwb(cwbs);
		}
		excelMethod(response, sql, exportmould2);
	}

	private String getCwbs(List<String> cwbList) {
		String cwbs = "'--'";
		if (cwbList != null && cwbList.size() > 0) {
			cwbs = "";
			for (String cwb : cwbList) {
				cwbs += "'" + cwb + "',";
			}
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		return cwbs;
	}

	private void excelMethod(HttpServletResponse response, String sql1, String mouldfieldids2) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
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
		try {
			// 查询出数据
			final String sql = sql1;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDao.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
							recordbatch.add(mapRow);
							count++;
							if (count % 100 == 0) {
								writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
										complaintMap);
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
							writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = recordbatch.get(i).get("cwb").toString();
									writeSingle(recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), count - size + i, cwbspayupMsp, complaintMap);
								}
								recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
					 * (rs.getString("cwb")); for (int i = 0; i <
					 * cloumnName4.length; i++) { Cell cell =
					 * row.createCell((short) i); cell.setCellStyle(style);
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
			e.printStackTrace();
		}
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
	public String getOrderFlowCwbs(List<OrderFlow> orderFlowList) {
		StringBuffer str = new StringBuffer();
		String orderflowid;
		if (orderFlowList.size() > 0) {
			for (OrderFlow of : orderFlowList) {
				str.append("'").append(of.getCwb()).append("',");
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
	public List<CwbOrderView> getCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList,
			List<Reason> reasonList, String begindate, String enddate, List<Remark> remarkList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
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

				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(c.getCustomerwarehouseid()));
				cwbOrderView.setCustomerwarehousename(customwarehouse);
				cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse())));// 入库仓库
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
				cwbOrderView.setPaytype(this.getPayWayType(c.getCwb(), deliverystate));// 支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setReturngoodsremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), begindate, enddate).getComment());
				String currentBranch = this.getQueryBranchName(branchList, c.getCurrentbranchid());
				cwbOrderView.setCurrentbranchname(currentBranch);
				// cwbOrderView.setAuditstate();//审核状态
				// cwbOrderView.setAuditor();//审核人
				// cwbOrderView.setAudittime();//审核时间
				// cwbOrderView.setMarksflagmen();//标记人
				// cwbOrderView.setMarksflag();//标记状态
				// cwbOrderView.setMarksflagtime();//标记时间
				Date ruku = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue(), begindate, enddate).getCredate();
				Date chukusaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), begindate, enddate).getCredate();
				Date daohuosaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), begindate, enddate).getCredate();
				daohuosaomiao = daohuosaomiao == null ? this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), begindate, enddate).getCredate()
						: daohuosaomiao;
				Date fenzhanlinghuo = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), begindate, enddate).getCredate();
				Date yifankui = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), begindate, enddate).getCredate();
				Date zuixinxiugai = this.getOrderFlowByCwb(c.getCwb()).getCredate();
				Date yishenhe = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), begindate, enddate).getCredate();
				cwbOrderView.setInstoreroomtime(ruku != null ? sdf.format(ruku) : "");// 入库时间
				cwbOrderView.setOutstoreroomtime(chukusaomiao != null ? sdf.format(chukusaomiao) : "");// 出库时间
				cwbOrderView.setInSitetime(daohuosaomiao != null ? sdf.format(daohuosaomiao) : "");// 到站时间
				cwbOrderView.setPickGoodstime(fenzhanlinghuo != null ? sdf.format(fenzhanlinghuo) : "");// 小件员领货时间
				cwbOrderView.setGobacktime(yifankui != null ? sdf.format(yifankui) : "");// 反馈时间
				cwbOrderView.setGoclasstime(yishenhe == null ? "" : sdf.format(yishenhe));// 归班时间
				cwbOrderView.setNowtime(zuixinxiugai != null ? sdf.format(zuixinxiugai) : "");// 最新修改时间
				cwbOrderView.setBackreason(c.getBackreason());
				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
				// cwbOrderView.setExpt_code(); //异常编码
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setInwarhouseremark(exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
						.get(ReasonTypeEnum.RuKuBeiZhu.getText()));

				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (yifankui != null ? sdf.format(yifankui) : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), begindate, enddate).getComment());
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if (deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && yifankui != null) {
						cwbOrderView.setSendSuccesstime(sdf.format(yifankui));// 配送成功时间
					}
				}
				cwbOrderViewList.add(cwbOrderView);

			}
		}
		return cwbOrderViewList;
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
		orderflowList = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public OrderFlow getOrderFlowByCwb(String cwb) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		deliveryStateList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
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

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public TuihuoRecord getTuihuoRecordByCwb(String cwb) {
		List<TuihuoRecord> tuihuoRecords = tuihuoRecordDAO.getTuihuoRecordByCwb(cwb);
		return tuihuoRecords.size() > 0 ? tuihuoRecords.get(tuihuoRecords.size() - 1) : new TuihuoRecord();
	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	public void batchSelectExport(HttpServletResponse response, HttpServletRequest request, List<String> cwbStrList) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
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
		try {
			// 查询出数据
			final List<CwbOrder> coList = new ArrayList<CwbOrder>();
			for (int i = 0; i < cwbStrList.size(); i++) {
				CwbOrder cwborder = cwbDAO.getCwbByCwb(cwbStrList.get(i));
				if (cwborder != null) {
					coList.add(cwborder);
				}

			}
			final Map<String, String> cwbspayupMap = getcwbspayupidMap(cwbStrList);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDao.getAllReason();
					int count = 0;
					for (CwbOrder co : coList) {
						Row row = sheet.createRow(count + 1);
						row.setHeightInPoints((float) 15);
						if (co != null) {
							DeliveryState ds = getDeliveryByCwb(co.getCwb());
							TuihuoRecord tuihuoRecord = getTuihuoRecordByCwb(co.getCwb());
							Map<String, String> allTime = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(co.getCwb());
							String cwbspayup = cwbspayupMap.get(co.getCwb()) == null ? "否" : cwbspayupMap.get(co.getCwb());
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = exportService.setObjectB(cloumnName5, co, tuihuoRecord, i, uList, cMap, bList, commonList, ds, allTime, cWList, remarkMap, reasonList, cwbspayup);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}
						count++;
					}
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
		Map<String, String> cwbspayupidMap = new HashMap<String, String>();
		/*
		 * for(DeliveryState
		 * deliveryState:deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)){
		 * String ispayup = "否"; GotoClassAuditing goclass =
		 * gotoClassAuditingDAO
		 * .getGotoClassAuditingByGcaid(deliveryState.getGcaid());
		 * 
		 * if(goclass!=null&&goclass.getPayupid()!=0){ ispayup = "是"; }
		 * cwbspayupidMap.put(deliveryState.getCwb(), ispayup); }
		 */
		return cwbspayupidMap;
	}

}
