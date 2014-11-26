package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.BranchTodayLog;

@Component
public class LogTodayDAO {

	private final class BranchTodayLogRowMapper implements RowMapper<BranchTodayLog> {
		@Override
		public BranchTodayLog mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchTodayLog branchTodayLog = new BranchTodayLog();
			branchTodayLog.setId(rs.getLong("id"));
			branchTodayLog.setBranchid(rs.getLong("branchid"));
			branchTodayLog.setCreatedate(rs.getString("createdate"));
			branchTodayLog.setUserid(rs.getLong("userid"));
			branchTodayLog.setYesterday_stortage(rs.getLong("yesterday_stortage"));
			branchTodayLog.setYesterday_not_arrive(rs.getLong("yesterday_not_arrive"));
			branchTodayLog.setYesterday_other_site_arrive(rs.getLong("yesterday_other_site_arrive"));
			branchTodayLog.setToday_out_storehouse(rs.getLong("today_out_storehouse"));
			branchTodayLog.setToday_not_arrive(rs.getLong("today_not_arrive"));
			branchTodayLog.setToday_arrive(rs.getLong("today_arrive"));
			branchTodayLog.setToday_other_site_arrive(rs.getLong("today_other_site_arrive"));
			branchTodayLog.setToday_stortage(rs.getLong("today_stortage"));
			branchTodayLog.setToday_wrong_arrive(rs.getLong("today_wrong_arrive"));
			branchTodayLog.setToday_fankui_daohuo(rs.getLong("today_fankui_daohuo"));
			branchTodayLog.setToday_fankui_zhiliu(rs.getLong("today_fankui_zhiliu"));
			branchTodayLog.setToday_fankui_linghuo(rs.getLong("today_fankui_linghuo"));
			branchTodayLog.setToday_fankui_zuoriweiguiban(rs.getLong("today_fankui_zuoriweiguiban"));
			branchTodayLog.setToday_fankui_peisongchenggong_count(rs.getLong("today_fankui_peisongchenggong_count"));
			branchTodayLog.setToday_fankui_peisongchenggong_money(rs.getBigDecimal("today_fankui_peisongchenggong_money"));
			branchTodayLog.setToday_fankui_jushou(rs.getLong("today_fankui_jushou"));
			branchTodayLog.setToday_fankui_leijizhiliu(rs.getLong("today_fankui_leijizhiliu"));
			branchTodayLog.setToday_fankui_bufenjushou(rs.getLong("today_fankui_bufenjushou"));
			branchTodayLog.setToday_fankui_zhobgzhuan(rs.getLong("today_fankui_zhobgzhuan"));
			branchTodayLog.setToday_fankui_shangmentuichenggong_count(rs.getLong("today_fankui_shangmentuichenggong_count"));
			branchTodayLog.setToday_fankui_shangmentuichenggong_money(rs.getBigDecimal("today_fankui_shangmentuichenggong_money"));
			branchTodayLog.setToday_fankui_shangmentuijutui(rs.getLong("today_fankui_shangmentuijutui"));
			branchTodayLog.setToday_fankui_shangmenhuanchenggong_count(rs.getLong("today_fankui_shangmenhuanchenggong_count"));
			branchTodayLog.setToday_fankui_shangmenhuanchenggong_yingshou_money(rs.getBigDecimal("today_fankui_shangmenhuanchenggong_yingshou_money"));
			branchTodayLog.setToday_fankui_shangmenhuanchenggong_yingtui_money(rs.getBigDecimal("today_fankui_shangmenhuanchenggong_yingtui_money"));
			branchTodayLog.setToday_fankui_shangmenhuanjuhuan(rs.getLong("today_fankui_shangmenhuanjuhuan"));
			branchTodayLog.setToday_fankui_diushi(rs.getLong("today_fankui_diushi"));
			branchTodayLog.setToday_fankui_heji_count(rs.getLong("today_fankui_heji_count"));
			branchTodayLog.setToday_fankui_heji_yingshou_money(rs.getBigDecimal("today_fankui_heji_yingshou_money"));
			branchTodayLog.setToday_fankui_heji_yingtui_money(rs.getBigDecimal("today_fankui_heji_yingtui_money"));
			branchTodayLog.setToday_weifankui_heji_count(rs.getLong("today_weifankui_heji_count"));
			branchTodayLog.setToday_weifankui_heji_yingshou_money(rs.getBigDecimal("today_weifankui_heji_yingshou_money"));
			branchTodayLog.setToday_weifankui_heji_yingtui_money(rs.getBigDecimal("today_weifankui_heji_yingtui_money"));
			branchTodayLog.setToday_funds_peisongchenggong_count(rs.getLong("today_funds_peisongchenggong_count"));
			branchTodayLog.setToday_funds_peisongchenggong_cash(rs.getBigDecimal("today_funds_peisongchenggong_cash"));
			branchTodayLog.setToday_funds_peisongchenggong_pos(rs.getBigDecimal("today_funds_peisongchenggong_pos"));
			branchTodayLog.setToday_funds_peisongchenggong_checkfee(rs.getBigDecimal("today_funds_peisongchenggong_checkfee"));
			branchTodayLog.setToday_funds_shangmentuichenggong_count(rs.getLong("today_funds_shangmentuichenggong_count"));
			branchTodayLog.setToday_funds_shangmentuichenggong_money(rs.getBigDecimal("today_funds_shangmentuichenggong_money"));
			branchTodayLog.setToday_funds_shangmentuichenggong_cash(rs.getBigDecimal("today_funds_shangmentuichenggong_cash"));
			branchTodayLog.setToday_funds_shangmentuichenggong_pos(rs.getBigDecimal("today_funds_shangmentuichenggong_pos"));
			branchTodayLog.setToday_funds_shangmentuichenggong_checkfee(rs.getBigDecimal("today_funds_shangmentuichenggong_checkfee"));
			branchTodayLog.setYesterday_kucun_count(rs.getLong("yesterday_kucun_count"));
			branchTodayLog.setToday_kucun_arrive(rs.getLong("today_kucun_arrive"));
			branchTodayLog.setToday_kucun_sucess(rs.getLong("today_kucun_sucess"));
			branchTodayLog.setToday_kucun_tuihuochuku(rs.getLong("today_kucun_tuihuochuku"));
			branchTodayLog.setToday_kucun_zhongzhuanchuku(rs.getLong("today_kucun_zhongzhuanchuku"));
			branchTodayLog.setToday_kucun_count(rs.getLong("today_kucun_count"));
			branchTodayLog.setJobRemark(rs.getString("jobRemark"));
			branchTodayLog.setShangmenhuanchenggong(rs.getLong("shangmenhuanchenggong"));
			branchTodayLog.setShangmenhuanchenggong_yingshou_amount(rs.getBigDecimal("shangmenhuanchenggong_yingshou_amount"));
			branchTodayLog.setShangmenhuanchenggong_yingtui_amount(rs.getBigDecimal("shangmenhuanchenggong_yingtui_amount"));
			branchTodayLog.setShishou_sum_amount(rs.getBigDecimal("shishou_sum_amount"));

			branchTodayLog.setZuorikucun_count(rs.getLong("zuorikucun_count"));
			branchTodayLog.setZuorikucun_money(rs.getBigDecimal("zuorikucun_money"));
			branchTodayLog.setJinridaohuo_count(rs.getLong("jinridaohuo_count"));
			branchTodayLog.setJinridaohuo_money(rs.getBigDecimal("jinridaohuo_money"));
			branchTodayLog.setJinriyingtou_count(rs.getLong("jinriyingtou_count"));
			branchTodayLog.setJinrishoukuan_money(rs.getBigDecimal("jinrishoukuan_money"));
			branchTodayLog.setJinrishoukuan_count(rs.getLong("jinrishoukuan_count"));
			branchTodayLog.setJinriyingtou_money(rs.getBigDecimal("jinriyingtou_money"));
			branchTodayLog.setJinrichuku_count(rs.getLong("jinrichuku_count"));
			branchTodayLog.setJinrichuku_money(rs.getBigDecimal("jinrichuku_money"));
			branchTodayLog.setJinrikucun_count(rs.getLong("jinrikucun_count"));
			branchTodayLog.setJinrikucun_money(rs.getBigDecimal("jinrikucun_money"));

			branchTodayLog.setDeliveryRate(rs.getBigDecimal("deliveryRate"));
			branchTodayLog.setJushou_kuicun(rs.getLong("jushou_kuicun"));
			branchTodayLog.setZhiliu_kuicun(rs.getLong("zhiliu_kuicun"));
			branchTodayLog.setWeiguiban_kuicun(rs.getLong("weiguiban_kuicun"));

			branchTodayLog.setZuori_jushou_kuicun(rs.getLong("zuori_jushou_kuicun"));
			branchTodayLog.setZuori_zhiliu_kuicun(rs.getLong("zuori_zhiliu_kuicun"));
			branchTodayLog.setZuori_weiguiban_kuicun(rs.getLong("zuori_weiguiban_kuicun"));
			branchTodayLog.setToudi_daozhanweiling(rs.getLong("toudi_daozhanweiling"));

			branchTodayLog.setShijiaokuan_cash_amount(rs.getBigDecimal("shijiaokuan_cash_amount"));
			branchTodayLog.setShijiaokuan_pos_amount(rs.getBigDecimal("shijiaokuan_pos_amount"));
			branchTodayLog.setShijiaokuan_checkfee_amount(rs.getBigDecimal("shijiaokuan_checkfee_amount"));

			branchTodayLog.setToday_fankuiweiguiban_heji_count(rs.getLong("today_fankuiweiguiban_heji_count"));
			branchTodayLog.setToday_fankuiweiguiban_heji_yingshou_money(rs.getBigDecimal("today_fankuiweiguiban_heji_yingshou_money"));
			branchTodayLog.setToday_fankuiweiguiban_heji_yingtui_money(rs.getBigDecimal("today_fankuiweiguiban_heji_yingtui_money"));
			branchTodayLog.setZuori_fankui_leijizhiliu(rs.getLong("zuori_fankui_leijizhiliu"));

			branchTodayLog.setJinri_lousaodaozhan(rs.getLong("jinri_lousaodaozhan"));

			branchTodayLog.setZuori_toudi_daozhanweiling(rs.getLong("zuori_toudi_daozhanweiling"));

			return branchTodayLog;
		}
	}

	private final class OrderCwbMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String obj = rs.getString("cwb");
			return obj;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ===========================站点日报使用===============begin============================
	// 他站到货查询
	public long getTodaybyOrtherBranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.queryForLong("SELECT COUNT(DISTINCT cwb) FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE  " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%'  " + "and branchid <> ? ", flowordertype, startTime, endTime, branchid);
	}

	public List<String> getCwbTodaybyOrtherBranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE  " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%'  " + "and branchid <> ? ", new OrderCwbMapper(), flowordertype, startTime, endTime, branchid);
	}

	// 今日到货日报 按当前站点查询
	public long getTodaybyBranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.queryForLong("SELECT COUNT(DISTINCT cwb) FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE  " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND branchid=? ", flowordertype, startTime, endTime, branchid);
	}

	/**
	 * 获取漏扫已到站数据
	 * 
	 * @param flowordertype
	 *            =6
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public long getLousaoyidaozhanCount(long flowordertype, long branchid, String startTime) {
		String sql = "SELECT COUNT(DISTINCT cd.cwb) as num  " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ "WHERE  cd.state=1 AND of.floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' " + "AND of.flowordertype=?  AND of.`credate`>? " + "AND of.`comment` = '系统自动处理' ";
		return jdbcTemplate.queryForLong(sql, flowordertype, startTime);
	}

	// 今日未到货日报 按当前站点查询
	public long getTodaybyBranchidAndIsnow(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.queryForLong("SELECT COUNT(1) FROM express_ops_order_flow WHERE  " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? " + " AND branchid=? and isnow =1 ",
				flowordertype, startTime, endTime, branchid);
	}

	// 库房出库 未到货 按下一站查询 订单号
	public List<String> getTodayWeidaohuoCwbbyChukuNextbranchidIsNow(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query(" SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND isnow=1 and floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' ", new OrderCwbMapper(), flowordertype, startTime, endTime);
	}

	// 库房出库 按下一站查询 订单号
	public List<String> getTodayWeidaohuoCwbbyChukuNextbranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query(" SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " and floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' ", new OrderCwbMapper(), flowordertype, startTime, endTime);
	}

	// 库房出库 站点未到货 按下一站查询 订单号
	public List<String> getTodayWeidaohuoCwbbyChukuNextbranchidCwb(long branchid, long flowordertype) {
		return jdbcTemplate.query("  SELECT DISTINCT cd.cwb as cwb FROM express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.cwb=of.cwb  "
				+ " WHERE cd.flowordertype=? AND cd.state=1 AND cd.nextbranchid=? " + "  AND of.flowordertype=?  AND of.isnow=1 ", new OrderCwbMapper(), flowordertype, branchid, flowordertype);
	}

	// 库房出库 按下一站查询
	public long getTodayWeidaohuobyNextbranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.queryForLong(" SELECT COUNT(DISTINCT cwb) FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' ", flowordertype, startTime, endTime);
	}

	// 库房出库 按下一站查询
	public long getTodayWeidaohuobyNextbranchidIsnow(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.queryForLong(" SELECT COUNT(DISTINCT cwb) FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND isnow=1 AND floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' ", flowordertype, startTime, endTime);
	}

	// 库房出库 站点未到货 按下一站查询
	public long getTodayWeidaohuobyNextbranchidIsnowCount(long branchid, long flowordertype) {
		return jdbcTemplate.queryForLong(" SELECT COUNT(DISTINCT cd.cwb) FROM express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.cwb=of.cwb  "
				+ " WHERE cd.flowordertype=? AND cd.state=1 AND cd.nextbranchid=? " + "  AND of.flowordertype=?  AND of.isnow=1 ", flowordertype, branchid, flowordertype);
	}

	// 未反馈
	public List<String> getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToCwb(long branchid, String startCredate, String endCredate) {
		return jdbcTemplate.query("select DISTINCT cwb as cwb from express_ops_delivery_state where  "
				+ " deliverystate <= 0 and deliverybranchid=? and createtime>=?  and createtime<=?  and state=1 ", new OrderCwbMapper(), branchid, startCredate, endCredate);
	}

	// 实缴款
	public List<String> getPayUpByDeliverybranchidAndDeliverystateAndCredateTo(long branchid, String startCredate, String endCredate) {
		return jdbcTemplate.query("select  DISTINCT cwb as cwb " + " from express_ops_delivery_state as ds LEFT JOIN  express_ops_goto_class_auditing as gc ON ds.gcaid=gc.id where  "
				+ " gc.payupid<>0 and ds.deliverybranchid=?  and ds.gcaid > 0 and gc.auditingtime >= ?  " + "and gc.auditingtime <= ? and ds.deliverystate IN(1,2,3,5,8) and ds.state=1 ",
				new OrderCwbMapper(), branchid, startCredate, endCredate);
	}

	public long getCountbyDedailt(long branchid, long flowordertype, String flowordertypeStr) {
		return jdbcTemplate.queryForLong(
				" SELECT COUNT(1) FROM express_ops_cwb_detail as cd " + " WHERE  cd.state=1 AND cd.currentbranchid=? " + " AND cd.flowordertype in(" + flowordertypeStr + ") ", branchid);
	}

	public List<String> getCountbyDedailtCwb(long branchid, long flowordertype, String flowordertypeStr) {
		return jdbcTemplate.query("  SELECT cwb FROM express_ops_cwb_detail as cd " + " WHERE  cd.state=1 AND cd.currentbranchid=? " + " AND cd.flowordertype in(" + flowordertypeStr + ")  ",
				new OrderCwbMapper(), branchid);
	}

	// 库房出库 按当前站查询
	public List<String> getOrderBybranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND branchid = " + branchid, new OrderCwbMapper(), flowordertype, startTime, endTime);
	}

	// 库房出库 按下一站查询
	public List<String> getOrderByNextbranchid(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " flowordertype=? " + " AND credate >=? " + " AND credate <=? "
				+ " AND floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%' ", new OrderCwbMapper(), flowordertype, startTime, endTime);
	}

	// ===========================站点日报使用===============end============================

	// ====按flow查询反馈订单的款项统计 begin==============

	// {"cwbOrder":{"opscwbid":10452,"startbranchid":192,"currentbranchid":0,"nextbranchid":192,"deliverybranchid":192,
	// "backtocustomer_awb":"","cwbflowflag":"1","carrealweight":0.000,"cartype":"","carwarehouse":"187","carsize":"",
	// "backcaramount":0.00,"sendcarnum":1,"backcarnum":1,"caramount":59.30,"backcarname":"","sendcarname":"",
	// "deliverid":1000,"emailfinishflag":0,"reacherrorflag":0,"orderflowid":0,"flowordertype":36,"cwbreachbranchid":0,
	// "cwbreachdeliverbranchid":0,"podfeetoheadflag":"0","podfeetoheadtime":null,"podfeetoheadchecktime":null,
	// "podfeetoheadcheckflag":"0","leavedreasonid":0,"deliversubscribeday":null,"customerwarehouseid":"0",
	// "emaildateid":90,"emaildate":"2013-01-10 10:16:05","serviceareaid":0,"customerid":124,"shipcwb":"",
	// "consigneeno":"","consigneename":"杨利萍","consigneeaddress":"四川省成都市金牛区杨高南路428号由由世纪广场3号楼7楼",
	// "consigneepostcode":"","consigneephone":"85847563","cwbremark":"","customercommand":"","transway":"",
	// "cwbprovince":"","cwbcity":"","cwbcounty":"","receivablefee":25.00,"paybackfee":0.00,"cwb":"PS30001",
	// "shipperid":0,"cwbordertypeid":1,"consigneemobile":"13621714795","transcwb":"","destination":"",
	// "cwbdelivertypeid":"0","exceldeliver":"","excelbranch":"朝阳站","excelimportuserid":999,"state":1,
	// "printtime":"","commonid":0,"commoncwb":"","signtypeid":0,"podrealname":"","podtime":"","podsignremark":"",
	// "modelname":null,"scannum":1,"isaudit":0,"backreason":"","leavedreason":"","paywayid":1,"newpaywayid":"1","tuihuoid":187,
	// "cwbstate":1,"remark1":"","remark2":"","remark3":"","remark4":"","remark5":"","backreasonid":0,"multi_shipcwb":null,"packagecode":""},
	// "deliveryState":{"id":217,"cwb":"PS30001","deliveryid":1000,"receivedfee":25.00,"returnedfee":0.00,"businessfee":25.00,"deliverystate":1,
	// "cash":25.00,"pos":0.00,"posremark":"","mobilepodtime":1357786570000,"checkfee":0.00,"checkremark":"",
	// "receivedfeeuser":1000,"createtime":"2013-01-10 10:42:45","otherfee":0.00,"podremarkid":0,
	// "deliverstateremark":"","isout":0,"pos_feedback_flag":0,"userid":1000,"gcaid":11,"sign_typeid":1,
	// "sign_man":"杨利萍","sign_time":"2013-01-10 10:54:59","backreason":null,"leavedreason":null,
	// "deliverybranchid":192,"deliverystateStr":null}}
	public List<String> getOrderByFlowtype(long branchid, long flowordertype, long deliverystate, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " branchid = ? " + " AND flowordertype=? " + " AND credate >=? "
				+ " AND credate <=? " + " AND floworderdetail LIKE '%\"deliverystate\":" + deliverystate + ",%'", new OrderCwbMapper(), branchid, flowordertype, startTime, endTime);
	}

	public List<String> getOrderByFlowtypeJuhuan(long branchid, long flowordertype, long deliverystate, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " branchid = ? " + " AND flowordertype=? " + " AND credate >=? "
				+ " AND credate <=? " + " AND floworderdetail LIKE '%\"cwbordertypeid\":3,%' AND floworderdetail LIKE '%\"deliverystate\":" + deliverystate + ",%'", new OrderCwbMapper(), branchid,
				flowordertype, startTime, endTime);
	}

	public long getOrderByFlowtypeCount(long branchid, long flowordertype, long deliverystate, String startTime, String endTime) {
		return jdbcTemplate.queryForLong("SELECT count(DISTINCT cwb) FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " branchid = ? " + " AND flowordertype=? " + " AND credate >=? "
				+ " AND credate <=? " + " AND floworderdetail LIKE '%\"deliverystate\":" + deliverystate + ",%'", branchid, flowordertype, startTime, endTime);
	}

	public List<String> getOrderByDeliverystate(long branchid, long flowordertype, String startTime, String endTime) {
		return jdbcTemplate.query("SELECT DISTINCT cwb as cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE " + " branchid = ? " + " AND flowordertype=? " + " AND credate >=? "
				+ " AND credate <=? AND isnow=1 ", new OrderCwbMapper(), branchid, flowordertype, startTime, endTime);
	}

	// ====按flow查询反馈订单的款项统计 end==============

	// ======日志表的增加和查看======begin============
	/**
	 * 按站点id和日期查看该站点当天的日志
	 * 
	 * @param brenchid
	 * @param credate
	 * @return
	 */
	public List<BranchTodayLog> getBranchTodayLogByBranchidAndDateList(long brenchid, String startTime, String endTime) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE  branchid=? " + " AND createdate >= ?" + " AND createdate <= ?";
		try {
			return jdbcTemplate.query(sql, new BranchTodayLogRowMapper(), brenchid, startTime, endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<BranchTodayLog> getBranchTodayLogByBranchidAndDateAllList(String startTime, String endTime) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE " + " createdate >= ?" + " AND createdate <= ?";
		try {
			return jdbcTemplate.query(sql, new BranchTodayLogRowMapper(), startTime, endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按站点id查看该站点最近的日志
	 * 
	 * @param brenchid
	 * @return
	 */
	public BranchTodayLog getLastBranchTodayLogByBranchid(long brenchid) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE  branchid=? ORDER BY createdate DESC LIMIT 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new BranchTodayLogRowMapper(), brenchid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 按站点id查看该站点最近的日志
	 * 
	 * @param brenchid
	 * @return
	 */
	public BranchTodayLog getLastBranchTodayLogByBranchid() {
		String sql = "SELECT * FROM express_ops_branch_todaylog ORDER BY createdate DESC LIMIT 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new BranchTodayLogRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 按日期查看所有站当天的日志
	 * 
	 * @param brenchid
	 * @param credate
	 * @return
	 */
	public List<BranchTodayLog> getBranchTodayLogByDateList(String startTime, String endTime, long page, int limitCount, String branchids) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE " + " createdate >= ?" + " AND createdate <= ? and branchid in(" + branchids + ") GROUP BY branchid " + " limit " + (page - 1)
				* limitCount + " ," + limitCount;
		try {
			return jdbcTemplate.query(sql, new BranchTodayLogRowMapper(), startTime, endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<BranchTodayLog> getBranchTodayLogByDateList(String startTime, String endTime) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE " + " createdate >= ?" + " AND createdate <= ?  ";
		try {
			return jdbcTemplate.query(sql, new BranchTodayLogRowMapper(), startTime, endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 站点历史日志 导出
	 * 
	 * @param startTime
	 * @param endTime
	 * @param branchids
	 * @return
	 */
	public List<BranchTodayLog> getBranchTodayLogByDateListExport(String startTime, String endTime, String branchids) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE " + " createdate >= ?" + " AND createdate <= ? and branchid in(" + branchids + ") GROUP BY branchid ";
		return jdbcTemplate.query(sql, new BranchTodayLogRowMapper(), startTime, endTime);
	}

	/**
	 * 按日期查看所有站当天的数量
	 * 
	 * @param brenchid
	 * @param credate
	 * @return
	 */
	public long getBranchTodayLogByDateCount(String startTime, String endTime, long page) {
		String sql = "SELECT count(DISTINCT branchid) FROM express_ops_branch_todaylog WHERE " + " createdate >= ?" + " AND createdate <= ? ";
		try {
			return jdbcTemplate.queryForLong(sql, startTime, endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 按站点id和日期查看该站点当天的日志
	 * 
	 * @param brenchid
	 * @param credate
	 * @return
	 */
	public BranchTodayLog getBranchTodayLogByBranchidAndDate(long brenchid, Date date) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE  branchid=? and createdate < '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
				+ "'  order by createdate desc limit 1 ";
		try {
			return jdbcTemplate.queryForObject(sql, new BranchTodayLogRowMapper(), brenchid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 按id当天的日志
	 * 
	 * @param brenchid
	 * @param credate
	 * @return
	 */
	public BranchTodayLog getBranchTodayLogById(long id) {
		String sql = "SELECT * FROM express_ops_branch_todaylog WHERE  id=? ";
		try {
			return jdbcTemplate.queryForObject(sql, new BranchTodayLogRowMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 * 创建一条日志记录
	 * 
	 * @param bTodaylog
	 * @return
	 */
	public long creBranchTodayLog(final BranchTodayLog bTodaylog) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO express_ops_branch_todaylog" + "(branchid,createdate,userid,yesterday_stortage,yesterday_not_arrive,yesterday_other_site_arrive,"
						+ "today_out_storehouse,today_not_arrive,today_arrive,today_other_site_arrive,today_stortage,"
						+ "today_wrong_arrive,today_fankui_daohuo,today_fankui_zhiliu,today_fankui_linghuo,"
						+ "today_fankui_zuoriweiguiban,today_fankui_peisongchenggong_count,today_fankui_peisongchenggong_money,"
						+ "today_fankui_jushou,today_fankui_leijizhiliu,today_fankui_bufenjushou,today_fankui_zhobgzhuan,"
						+ "today_fankui_shangmentuichenggong_count,today_fankui_shangmentuichenggong_money,today_fankui_shangmentuijutui,"
						+ "today_fankui_shangmenhuanchenggong_count,today_fankui_shangmenhuanchenggong_yingshou_money,"
						+ "today_fankui_shangmenhuanchenggong_yingtui_money,today_fankui_shangmenhuanjuhuan," + "today_fankui_diushi,today_fankui_heji_count,today_fankui_heji_yingshou_money,"
						+ "today_fankui_heji_yingtui_money,today_weifankui_heji_count," + "today_weifankui_heji_yingshou_money,today_weifankui_heji_yingtui_money,today_funds_peisongchenggong_count,"
						+ "today_funds_peisongchenggong_cash,today_funds_peisongchenggong_pos,today_funds_peisongchenggong_checkfee,"
						+ "today_funds_shangmentuichenggong_count,today_funds_shangmentuichenggong_money,"
						+ "today_funds_shangmentuichenggong_cash,today_funds_shangmentuichenggong_pos,today_funds_shangmentuichenggong_checkfee,"
						+ "yesterday_kucun_count,today_kucun_arrive,today_kucun_sucess,today_kucun_tuihuochuku,today_kucun_zhongzhuanchuku,"
						+ "today_kucun_count,jobRemark,shangmenhuanchenggong,shangmenhuanchenggong_yingshou_amount,shangmenhuanchenggong_yingtui_amount,"
						+ "shishou_sum_amount,zuorikucun_count,zuorikucun_money,jinridaohuo_count,jinridaohuo_money,jinriyingtou_count,"
						+ "jinrishoukuan_money,jinrishoukuan_count,jinriyingtou_money,jinrichuku_count,jinrichuku_money,jinrikucun_count,jinrikucun_money,"
						+ "deliveryRate,jushou_kuicun,zhiliu_kuicun,weiguiban_kuicun,toudi_daozhanweiling,zuori_jushou_kuicun,zuori_zhiliu_kuicun,zuori_weiguiban_kuicun,"
						+ "shijiaokuan_cash_amount,shijiaokuan_pos_amount,shijiaokuan_checkfee_amount"
						+ ",today_fankuiweiguiban_heji_count,today_fankuiweiguiban_heji_yingshou_money,today_fankuiweiguiban_heji_yingtui_money,"
						+ "zuori_fankui_leijizhiliu,jinri_lousaodaozhan,zuori_toudi_daozhanweiling) " + "VALUES " + "(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, bTodaylog.getBranchid());
				ps.setString(2, bTodaylog.getCreatedate());
				ps.setLong(3, bTodaylog.getUserid());
				ps.setLong(4, bTodaylog.getYesterday_stortage());
				ps.setLong(5, bTodaylog.getYesterday_not_arrive());
				ps.setLong(6, bTodaylog.getYesterday_other_site_arrive());
				ps.setLong(7, bTodaylog.getToday_out_storehouse());
				ps.setLong(8, bTodaylog.getToday_not_arrive());
				ps.setLong(9, bTodaylog.getToday_arrive());
				ps.setLong(10, bTodaylog.getToday_other_site_arrive());
				ps.setLong(11, bTodaylog.getToday_stortage());
				ps.setLong(12, bTodaylog.getToday_wrong_arrive());
				ps.setLong(13, bTodaylog.getToday_fankui_daohuo());
				ps.setLong(14, bTodaylog.getToday_fankui_zhiliu());
				ps.setLong(15, bTodaylog.getToday_fankui_linghuo());
				ps.setLong(16, bTodaylog.getToday_fankui_zuoriweiguiban());
				ps.setLong(17, bTodaylog.getToday_fankui_peisongchenggong_count());
				ps.setBigDecimal(18, bTodaylog.getToday_fankui_peisongchenggong_money());
				ps.setLong(19, bTodaylog.getToday_fankui_jushou());
				ps.setLong(20, bTodaylog.getToday_fankui_leijizhiliu());
				ps.setLong(21, bTodaylog.getToday_fankui_bufenjushou());
				ps.setLong(22, bTodaylog.getToday_fankui_zhobgzhuan());
				ps.setLong(23, bTodaylog.getToday_fankui_shangmentuichenggong_count());
				ps.setBigDecimal(24, bTodaylog.getToday_fankui_shangmentuichenggong_money());
				ps.setLong(25, bTodaylog.getToday_fankui_shangmentuijutui());
				ps.setLong(26, bTodaylog.getToday_fankui_shangmenhuanchenggong_count());
				ps.setBigDecimal(27, bTodaylog.getToday_fankui_shangmenhuanchenggong_yingshou_money());
				ps.setBigDecimal(28, bTodaylog.getToday_fankui_shangmenhuanchenggong_yingtui_money());
				ps.setLong(29, bTodaylog.getToday_fankui_shangmenhuanjuhuan());
				ps.setLong(30, bTodaylog.getToday_fankui_diushi());
				ps.setLong(31, bTodaylog.getToday_fankui_heji_count());
				ps.setBigDecimal(32, bTodaylog.getToday_fankui_heji_yingshou_money());
				ps.setBigDecimal(33, bTodaylog.getToday_fankui_heji_yingtui_money());
				ps.setLong(34, bTodaylog.getToday_weifankui_heji_count());
				ps.setBigDecimal(35, bTodaylog.getToday_weifankui_heji_yingshou_money());
				ps.setBigDecimal(36, bTodaylog.getToday_weifankui_heji_yingtui_money());
				ps.setLong(37, bTodaylog.getToday_funds_peisongchenggong_count());
				ps.setBigDecimal(38, bTodaylog.getToday_funds_peisongchenggong_cash());
				ps.setBigDecimal(39, bTodaylog.getToday_funds_peisongchenggong_pos());
				ps.setBigDecimal(40, bTodaylog.getToday_funds_peisongchenggong_checkfee());
				ps.setLong(41, bTodaylog.getToday_funds_shangmentuichenggong_count());
				ps.setBigDecimal(42, bTodaylog.getToday_funds_shangmentuichenggong_money());
				ps.setBigDecimal(43, bTodaylog.getToday_funds_shangmentuichenggong_cash());
				ps.setBigDecimal(44, bTodaylog.getToday_funds_shangmentuichenggong_pos());
				ps.setBigDecimal(45, bTodaylog.getToday_funds_shangmentuichenggong_checkfee());
				ps.setLong(46, bTodaylog.getYesterday_kucun_count());
				ps.setLong(47, bTodaylog.getToday_kucun_arrive());
				ps.setLong(48, bTodaylog.getToday_kucun_sucess());
				ps.setLong(49, bTodaylog.getToday_kucun_tuihuochuku());
				ps.setLong(50, bTodaylog.getToday_kucun_zhongzhuanchuku());
				ps.setLong(51, bTodaylog.getToday_kucun_count());
				ps.setString(52, bTodaylog.getJobRemark());
				ps.setLong(53, bTodaylog.getShangmenhuanchenggong());
				ps.setBigDecimal(54, bTodaylog.getShangmenhuanchenggong_yingshou_amount());
				ps.setBigDecimal(55, bTodaylog.getShangmenhuanchenggong_yingtui_amount());
				ps.setBigDecimal(56, bTodaylog.getShishou_sum_amount());

				ps.setLong(57, bTodaylog.getZuorikucun_count());
				ps.setBigDecimal(58, bTodaylog.getZuorikucun_money());
				ps.setLong(59, bTodaylog.getJinridaohuo_count());
				ps.setBigDecimal(60, bTodaylog.getJinridaohuo_money());
				ps.setLong(61, bTodaylog.getJinriyingtou_count());
				ps.setBigDecimal(62, bTodaylog.getJinrishoukuan_money());
				ps.setLong(63, bTodaylog.getJinrishoukuan_count());
				ps.setBigDecimal(64, bTodaylog.getJinriyingtou_money());
				ps.setLong(65, bTodaylog.getJinrichuku_count());
				ps.setBigDecimal(66, bTodaylog.getJinrichuku_money());
				ps.setLong(67, bTodaylog.getJinrikucun_count());
				ps.setBigDecimal(68, bTodaylog.getJinrikucun_money());

				ps.setBigDecimal(69, bTodaylog.getDeliveryRate());
				ps.setLong(70, bTodaylog.getJushou_kuicun());
				ps.setLong(71, bTodaylog.getZhiliu_kuicun());
				ps.setLong(72, bTodaylog.getWeiguiban_kuicun());

				ps.setLong(73, bTodaylog.getToudi_daozhanweiling());
				ps.setLong(74, bTodaylog.getZuori_jushou_kuicun());
				ps.setLong(75, bTodaylog.getZuori_zhiliu_kuicun());
				ps.setLong(76, bTodaylog.getZuori_weiguiban_kuicun());

				ps.setBigDecimal(77, bTodaylog.getShijiaokuan_cash_amount());
				ps.setBigDecimal(78, bTodaylog.getShijiaokuan_pos_amount());
				ps.setBigDecimal(79, bTodaylog.getShijiaokuan_checkfee_amount());

				ps.setLong(80, bTodaylog.getToday_fankuiweiguiban_heji_count());
				ps.setBigDecimal(81, bTodaylog.getToday_fankuiweiguiban_heji_yingshou_money());
				ps.setBigDecimal(82, bTodaylog.getToday_fankuiweiguiban_heji_yingtui_money());
				ps.setLong(83, bTodaylog.getZuori_weiguiban_kuicun());
				ps.setLong(84, bTodaylog.getJinri_lousaodaozhan());
				ps.setLong(85, bTodaylog.getZuori_toudi_daozhanweiling());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 更新一条日志
	 * 
	 * @param bTodaylog
	 */
	public void updateBranchTodayLog(final BranchTodayLog bTodaylog) {
		jdbcTemplate.update("update express_ops_branch_todaylog set " + "branchid=?,createdate=?,userid=?,yesterday_stortage=?,yesterday_not_arrive=?,yesterday_other_site_arrive=?,"
				+ "today_out_storehouse=?,today_not_arrive=?,today_arrive=?,today_other_site_arrive=?,today_stortage=?,"
				+ "today_wrong_arrive=?,today_fankui_daohuo=?,today_fankui_zhiliu=?,today_fankui_linghuo=?,"
				+ "today_fankui_zuoriweiguiban=?,today_fankui_peisongchenggong_count=?,today_fankui_peisongchenggong_money=?,"
				+ "today_fankui_jushou=?,today_fankui_leijizhiliu=?,today_fankui_bufenjushou=?,today_fankui_zhobgzhuan=?,"
				+ "today_fankui_shangmentuichenggong_count=?,today_fankui_shangmentuichenggong_money=?,today_fankui_shangmentuijutui=?,"
				+ "today_fankui_shangmenhuanchenggong_count=?,today_fankui_shangmenhuanchenggong_yingshou_money=?,"
				+ "today_fankui_shangmenhuanchenggong_yingtui_money=?,today_fankui_shangmenhuanjuhuan=?," + "today_fankui_diushi=?,today_fankui_heji_count=?,today_fankui_heji_yingshou_money=?,"
				+ "today_fankui_heji_yingtui_money=?,today_weifankui_heji_count=?,"
				+ "today_weifankui_heji_yingshou_money=?,today_weifankui_heji_yingtui_money=?,today_funds_peisongchenggong_count=?,"
				+ "today_funds_peisongchenggong_cash=?,today_funds_peisongchenggong_pos=?,today_funds_peisongchenggong_checkfee=?,"
				+ "today_funds_shangmentuichenggong_count=?,today_funds_shangmentuichenggong_money=?,"
				+ "today_funds_shangmentuichenggong_cash=?,today_funds_shangmentuichenggong_pos=?,today_funds_shangmentuichenggong_checkfee=?,"
				+ "yesterday_kucun_count=?,today_kucun_arrive=?,today_kucun_sucess=?,today_kucun_tuihuochuku=?,today_kucun_zhongzhuanchuku=?, "
				+ "today_kucun_count=?,jobRemark=?, shangmenhuanchenggong=?,shangmenhuanchenggong_yingshou_amount=?,shangmenhuanchenggong_yingtui_amount=?,"
				+ "shishou_sum_amount=?,zuorikucun_count=?,zuorikucun_money=?,jinridaohuo_count=?,jinridaohuo_money=?,jinriyingtou_count=?,"
				+ "jinrishoukuan_money=?,jinrishoukuan_count=?,jinriyingtou_money=?,jinrichuku_count=?,jinrichuku_money=?,jinrikucun_count=?,jinrikucun_money=?,"
				+ "deliveryRate=?,jushou_kuicun=?,zhiliu_kuicun=?,weiguiban_kuicun=?,toudi_daozhanweiling=?" + "  where  id =?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, bTodaylog.getBranchid());
				ps.setString(2, bTodaylog.getCreatedate());
				ps.setLong(3, bTodaylog.getUserid());
				ps.setLong(4, bTodaylog.getYesterday_stortage());
				ps.setLong(5, bTodaylog.getYesterday_not_arrive());
				ps.setLong(6, bTodaylog.getYesterday_other_site_arrive());
				ps.setLong(7, bTodaylog.getToday_out_storehouse());
				ps.setLong(8, bTodaylog.getToday_not_arrive());
				ps.setLong(9, bTodaylog.getToday_arrive());
				ps.setLong(10, bTodaylog.getToday_other_site_arrive());
				ps.setLong(11, bTodaylog.getToday_stortage());
				ps.setLong(12, bTodaylog.getToday_wrong_arrive());
				ps.setLong(13, bTodaylog.getToday_fankui_daohuo());
				ps.setLong(14, bTodaylog.getToday_fankui_zhiliu());
				ps.setLong(15, bTodaylog.getToday_fankui_linghuo());
				ps.setLong(16, bTodaylog.getToday_fankui_zuoriweiguiban());
				ps.setLong(17, bTodaylog.getToday_fankui_peisongchenggong_count());
				ps.setBigDecimal(18, bTodaylog.getToday_fankui_peisongchenggong_money());
				ps.setLong(19, bTodaylog.getToday_fankui_jushou());
				ps.setLong(20, bTodaylog.getToday_fankui_leijizhiliu());
				ps.setLong(21, bTodaylog.getToday_fankui_bufenjushou());
				ps.setLong(22, bTodaylog.getToday_fankui_zhobgzhuan());
				ps.setLong(23, bTodaylog.getToday_fankui_shangmentuichenggong_count());
				ps.setBigDecimal(24, bTodaylog.getToday_fankui_shangmentuichenggong_money());
				ps.setLong(25, bTodaylog.getToday_fankui_shangmentuijutui());
				ps.setLong(26, bTodaylog.getToday_fankui_shangmenhuanchenggong_count());
				ps.setBigDecimal(27, bTodaylog.getToday_fankui_shangmenhuanchenggong_yingshou_money());
				ps.setBigDecimal(28, bTodaylog.getToday_fankui_shangmenhuanchenggong_yingtui_money());
				ps.setLong(29, bTodaylog.getToday_fankui_shangmenhuanjuhuan());
				ps.setLong(30, bTodaylog.getToday_fankui_diushi());
				ps.setLong(31, bTodaylog.getToday_fankui_heji_count());
				ps.setBigDecimal(32, bTodaylog.getToday_fankui_heji_yingshou_money());
				ps.setBigDecimal(33, bTodaylog.getToday_fankui_heji_yingtui_money());
				ps.setLong(34, bTodaylog.getToday_weifankui_heji_count());
				ps.setBigDecimal(35, bTodaylog.getToday_weifankui_heji_yingshou_money());
				ps.setBigDecimal(36, bTodaylog.getToday_weifankui_heji_yingtui_money());
				ps.setLong(37, bTodaylog.getToday_funds_peisongchenggong_count());
				ps.setBigDecimal(38, bTodaylog.getToday_funds_peisongchenggong_cash());
				ps.setBigDecimal(39, bTodaylog.getToday_funds_peisongchenggong_pos());
				ps.setBigDecimal(40, bTodaylog.getToday_funds_peisongchenggong_checkfee());
				ps.setLong(41, bTodaylog.getToday_funds_shangmentuichenggong_count());
				ps.setBigDecimal(42, bTodaylog.getToday_funds_shangmentuichenggong_money());
				ps.setBigDecimal(43, bTodaylog.getToday_funds_shangmentuichenggong_cash());
				ps.setBigDecimal(44, bTodaylog.getToday_funds_shangmentuichenggong_pos());
				ps.setBigDecimal(45, bTodaylog.getToday_funds_shangmentuichenggong_checkfee());
				ps.setLong(46, bTodaylog.getYesterday_kucun_count());
				ps.setLong(47, bTodaylog.getToday_kucun_arrive());
				ps.setLong(48, bTodaylog.getToday_kucun_sucess());
				ps.setLong(49, bTodaylog.getToday_kucun_tuihuochuku());
				ps.setLong(50, bTodaylog.getToday_kucun_zhongzhuanchuku());
				ps.setLong(51, bTodaylog.getToday_kucun_count());
				ps.setString(52, bTodaylog.getJobRemark());
				ps.setLong(53, bTodaylog.getShangmenhuanchenggong());
				ps.setBigDecimal(54, bTodaylog.getShangmenhuanchenggong_yingshou_amount());
				ps.setBigDecimal(55, bTodaylog.getShangmenhuanchenggong_yingtui_amount());
				ps.setBigDecimal(56, bTodaylog.getShishou_sum_amount());

				ps.setLong(57, bTodaylog.getZuorikucun_count());
				ps.setBigDecimal(58, bTodaylog.getZuorikucun_money());
				ps.setLong(59, bTodaylog.getJinridaohuo_count());
				ps.setBigDecimal(60, bTodaylog.getJinridaohuo_money());
				ps.setLong(61, bTodaylog.getJinriyingtou_count());
				ps.setBigDecimal(62, bTodaylog.getJinrishoukuan_money());
				ps.setLong(63, bTodaylog.getJinrishoukuan_count());
				ps.setBigDecimal(64, bTodaylog.getJinriyingtou_money());
				ps.setLong(65, bTodaylog.getJinrichuku_count());
				ps.setBigDecimal(66, bTodaylog.getJinrichuku_money());
				ps.setLong(67, bTodaylog.getJinrikucun_count());
				ps.setBigDecimal(68, bTodaylog.getJinrikucun_money());

				ps.setBigDecimal(69, bTodaylog.getDeliveryRate());
				ps.setLong(70, bTodaylog.getJushou_kuicun());
				ps.setLong(71, bTodaylog.getZhiliu_kuicun());
				ps.setLong(72, bTodaylog.getWeiguiban_kuicun());
				ps.setLong(73, bTodaylog.getToudi_daozhanweiling());

				ps.setLong(74, bTodaylog.getId());
			}
		});

	}
	// ======日志表的增加和查看======end============
}
