package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OperationTime;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;

@Component
public class OperationTimeDAO {

	private final class OperationTimeRowMapper implements RowMapper<OperationTime> {
		@Override
		public OperationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperationTime operationTime = new OperationTime();
			operationTime.setId(rs.getLong("id"));
			operationTime.setCwb(rs.getString("cwb"));
			operationTime.setBranchid(rs.getLong("branchid"));
			operationTime.setCredate(rs.getLong("credate"));
			operationTime.setFlowordertype(rs.getInt("flowordertype"));
			operationTime.setNextbranchid(rs.getLong("nextbranchid"));
			operationTime.setIsupdate(rs.getString("isupdate"));
			operationTime.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			operationTime.setCustomerid(rs.getLong("customerid"));
			return operationTime;
		}
	}

	private final class OperationTimeCountForMapRowMapper implements RowMapper<Map<Long, Long>> {
		@Override
		public Map<Long, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<Long, Long> map = new HashMap<Long, Long>();
			map.put(rs.getLong("branchid"), rs.getLong("num"));
			return map;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 创建一条操作记录
	 *
	 * @param of
	 * @return key
	 */
	public void creOperationTime(String cwb, long branchid, int flowordertype, long deliverystate, long nextbranchid, long customerid, String inwarehouseTime, String emaildate,int cwbordertypeid,BigDecimal receivablefee, BigDecimal paybackfee) {
		this.jdbcTemplate.update(
				"insert into express_ops_operation_time(cwb,branchid,credate,flowordertype,deliverystate,nextbranchid,customerid,outwarehouseTime,emaildate,cwbordertypeid,receivablefee,paybackfee) values(?,?,?,?,?,?,?,?,?,?,?,?)", cwb, branchid,
				System.currentTimeMillis(), flowordertype, deliverystate, nextbranchid, customerid, inwarehouseTime, emaildate,cwbordertypeid,receivablefee,paybackfee);
	}

	public void creAndUpdateOperationTime(String cwb, long branchid, int flowordertype, long deliverystate, long nextbranchid,
			long customeid, String inwarehouseTime, String emaildate,int cwbordertypeid,BigDecimal receivablefee, BigDecimal paybackfee) {
		String sql = "select count(1) from express_ops_operation_time where cwb=?";
		long count = this.jdbcTemplate.queryForLong(sql, cwb);

		if (count > 0) {
			if ((inwarehouseTime != null) && (inwarehouseTime.length() > 0)) {
				this.updateOperationTime(cwb, branchid, flowordertype, deliverystate, nextbranchid, inwarehouseTime);
			} else {
				this.updateOperationTime(cwb, branchid, flowordertype, deliverystate, nextbranchid);
			}
		} else {
			this.creOperationTime(cwb, branchid, flowordertype, deliverystate, nextbranchid, customeid, inwarehouseTime, emaildate,cwbordertypeid,receivablefee,paybackfee);
		}
	}

	/**
	 * 不需要更新入库时间
	 *
	 * @param cwb
	 * @param branchid
	 * @param flowordertype
	 * @param deliverystate
	 * @param nextbranchid
	 */
	public void updateOperationTime(String cwb, long branchid, int flowordertype, long deliverystate, long nextbranchid) {
		String sql = "update express_ops_operation_time set isupdate='',branchid=?,credate=?,flowordertype=?,deliverystate=?,nextbranchid=? where cwb=?";
		this.jdbcTemplate.update(sql, branchid, System.currentTimeMillis(), flowordertype, deliverystate, nextbranchid, cwb);

	}

	/**
	 * 需要更新入库时间
	 *
	 * @param cwb
	 * @param branchid
	 * @param flowordertype
	 * @param deliverystate
	 * @param nextbranchid
	 * @param inwarehouseTime
	 */
	public void updateOperationTime(String cwb, long branchid, int flowordertype, long deliverystate, long nextbranchid, String inwarehouseTime) {
		String sql = "update express_ops_operation_time set isupdate='', branchid=?,credate=?,flowordertype=?,deliverystate=?,nextbranchid=?,outwarehouseTime=? where cwb=?";
		this.jdbcTemplate.update(sql, branchid, System.currentTimeMillis(), flowordertype, deliverystate, nextbranchid, inwarehouseTime, cwb);
	}

	public void delOperationTime(String cwb) {
		this.jdbcTemplate.update("delete from express_ops_operation_time where cwb=? ", cwb);

	}

	/**
	 * 不需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeGroupBranch(long outTime, int flowordertype) {
		return this.transformListToMap(this.jdbcTemplate.query("SELECT COUNT(1) AS num, branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb  "
				+ "WHERE ot.credate<? AND ot.flowordertype=? and cd.state=1 GROUP BY ot.branchid", new OperationTimeCountForMapRowMapper(), outTime, flowordertype));
	}

	/**
	 * 需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeGroupBranch(long outTime, int flowordertype, long customerid, String begindate, String enddate) {
		String sql = "SELECT COUNT(1) AS num, branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb  "
				+ "WHERE ot.credate<? AND ot.flowordertype=? and cd.state=1 ";
		sql = this.getOperationSql(sql, customerid, begindate, enddate);
		sql += " GROUP BY ot.branchid";
		return this.transformListToMap(this.jdbcTemplate.query(sql, new OperationTimeCountForMapRowMapper(), outTime, flowordertype));
	}

	/**
	 * 需要按供货商和入库时间查询中转站超期未中转
	 *
	 * @param outTime
	 * @param flowordertype
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public Map<Long, Long> getZhongZhuanOperationTimeByCredateAndFlowordertypeGroupBranch(long outTime, String flowordertypes, long customerid, String begindate, String enddate) {
		String sql = "SELECT COUNT(1) AS num, branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb  " + "WHERE ot.credate<? AND ot.flowordertype in("
				+ flowordertypes + ") and cd.state=1 ";
		sql = this.getOperationSql(sql, customerid, begindate, enddate);
		sql += " GROUP BY ot.branchid";
		return this.transformListToMap(this.jdbcTemplate.query(sql, new OperationTimeCountForMapRowMapper(), outTime));
	}

	private String getOperationSql(String sql, long customerid, String begindate, String enddate) {
		if ((customerid > 0) || (begindate.length() > 0) || (enddate.length() > 0)) {
			if (customerid > 0) {
				sql += " and ot.customerid=" + customerid;
			}
			if (begindate.length() > 0) {
				sql += " and ot.outwarehouseTime>='" + begindate + "' ";
			}
			if (enddate.length() > 0) {
				sql += " and ot.outwarehouseTime<='" + enddate + "' ";
			}

		}

		return sql;
	}

	/**
	 * 不需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeGroupNextbranch(long outTime, int flowordertype) {
		return this.transformListToMap(this.jdbcTemplate.query(
				"SELECT COUNT(1) AS num,ot.nextbranchid AS branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb "
						+ "WHERE ot.credate<? AND ot.flowordertype=? and cd.state=1 GROUP BY ot.nextbranchid", new OperationTimeCountForMapRowMapper(), outTime, flowordertype));
	}

	/**
	 * 需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeGroupNextbranch(long outTime, int flowordertype, long customerid, String begindate, String enddate) {
		String sql = "SELECT COUNT(1) AS num,ot.nextbranchid AS branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb "
				+ "WHERE ot.credate<? AND ot.flowordertype=? and cd.state=1 ";
		sql = this.getOperationSql(sql, customerid, begindate, enddate);
		sql += " GROUP BY ot.nextbranchid";
		return this.transformListToMap(this.jdbcTemplate.query(sql, new OperationTimeCountForMapRowMapper(), outTime, flowordertype));
	}

	/**
	 * 不需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertypes
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypesGroupNextbranch(long outTime, String flowordertypes) {
		return this.transformListToMap(this.jdbcTemplate.query(
				"SELECT COUNT(1) AS num,ot.nextbranchid AS branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb "
						+ "WHERE ot.credate<? AND ot.flowordertype in(" + flowordertypes + ") and cd.state=1 GROUP BY ot.nextbranchid", new OperationTimeCountForMapRowMapper(), outTime));
	}

	/**
	 * 需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertypes
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypesGroupNextbranch(long outTime, String flowordertypes, long customerid, String begindate, String enddate) {
		String sql = "SELECT COUNT(1) AS num,ot.nextbranchid AS branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb "
				+ "WHERE ot.credate<? AND ot.flowordertype in(" + flowordertypes + ") and cd.state=1 ";
		sql = this.getOperationSql(sql, customerid, begindate, enddate);
		sql += " GROUP BY ot.nextbranchid";
		return this.transformListToMap(this.jdbcTemplate.query(sql, new OperationTimeCountForMapRowMapper(), outTime));
	}

	/**
	 * 不需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @param deliverystate
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(long outTime, String flowordertypes, int deliverystate) {
		return this.transformListToMap(this.jdbcTemplate.query("SELECT COUNT(1) AS num, branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb "
				+ "WHERE ot.credate<? AND ot.flowordertype in(" + flowordertypes + ") and ot.deliverystate=?  and cd.state=1 GROUP BY ot.branchid", new OperationTimeCountForMapRowMapper(), outTime,
				deliverystate));
	}

	/**
	 * 需要按供货商和入库时间查询
	 *
	 * @param outTime
	 * @param flowordertype
	 * @param deliverystate
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public Map<Long, Long> getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(long outTime, String flowordertypes, int deliverystate, long customerid, String begindate,
			String enddate) {
		String sql = "SELECT COUNT(1) AS num, branchid FROM express_ops_operation_time ot left join express_ops_cwb_detail cd ON cd.cwb=ot.cwb " + "WHERE ot.credate<? AND ot.flowordertype in("
				+ flowordertypes + ") and ot.deliverystate=?  and cd.state=1 ";
		sql = this.getOperationSql(sql, customerid, begindate, enddate);
		sql += " GROUP BY ot.branchid";
		return this.transformListToMap(this.jdbcTemplate.query(sql, new OperationTimeCountForMapRowMapper(), outTime, deliverystate));
	}

	private Map<Long, Long> transformListToMap(List<Map<Long, Long>> lm) {
		Map<Long, Long> reMap = new HashMap<Long, Long>();
		for (Map<Long, Long> map : lm) {
			reMap.putAll(map);
		}
		return reMap;
	}

	public List<String> getOperationTimeByFlowordertypeAndBranchid(long branchid, int flowordertype) {
		return this.jdbcTemplate.queryForList("SELECT cwb FROM express_ops_operation_time WHERE branchid=? AND flowordertype=?", String.class, branchid, flowordertype);
	}

	public List<String> getOperationTimeTuiHuoChuZhan(int flowordertype, long nextbranchid) {
		String sql = "SELECT cwb FROM express_ops_operation_time WHERE flowordertype=? and nextbranchid=? ";
		return this.jdbcTemplate.queryForList(sql, String.class, flowordertype, nextbranchid);
	}

	public List<String> getOperationTimeTuiHuoZhanRuKu(int flowordertype, long branchid) {
		String sql = "SELECT cwb FROM express_ops_operation_time WHERE flowordertype=? and branchid=? ";
		return this.jdbcTemplate.queryForList(sql, String.class, flowordertype, branchid);
	}

	public long getOptionNoChangeCount() {
		String sql = " SELECT count(1) FROM `express_ops_operation_time` WHERE customerid=0 ";
		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<String> getOperationTimeByFlowordertypeAndBranchidAndNext(long branchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT cwb FROM express_ops_operation_time WHERE branchid=? AND flowordertype=?";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class, branchid, flowordertype);
	}

	public long getOperationTimeByFlowordertypeAndBranchidAndNextCount(long branchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT count(1) FROM express_ops_operation_time WHERE branchid=? AND flowordertype=?";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, flowordertype);
	}

	public void updateOperationTimeBycwb(String cwb, String outwarehouseTime) {
		String sql = "update express_ops_operation_time set outwarehouseTime=?,customerid='99999' where cwb=?";
		this.jdbcTemplate.update(sql, outwarehouseTime, cwb);

	}

	public void updateOperationTimeBycwb(String cwb, String emaildate, long customerid) {
		String sql = "update express_ops_operation_time set emaildate=?,customerid=? where cwb=?";
		this.jdbcTemplate.update(sql, emaildate, customerid, cwb);

	}

	/**
	 * 查询异常单数量2
	 *
	 * @param timer
	 * @param flowordertype
	 * @return
	 */
	// 修改
	/*public List<OperationTime> getUserExceptionAll() {
		String sql = "SELECT cwb,flowordertype,credate,isupdate FROM express_ops_operation_time  where isupdate=''";
		return this.jdbcTemplate.query(sql, new OperationTimeRowMapper());
	}*/

	public void updateUserException(String time, String cwbs) {
		String sql = "update express_ops_operation_time set isupdate=? where cwb in(" + cwbs + ")";
		this.jdbcTemplate.update(sql, time);
	}

	public List<OperationTime> countUserException(long timer, String flowordertype, int i) {
		String sql = "SELECT * FROM express_ops_operation_time  where credate<? ";
		if ((i == 8) || (i == 9)) {
			sql += " and deliverystate=?";
		} else {
			sql += "AND flowordertype in(?) ";
		}
		sql += " and isupdate=''";
		return this.jdbcTemplate.query(sql, new OperationTimeRowMapper(), timer, flowordertype);
	}

	/**
	 * 今日出库(未到货)订单数 1000条
	 *
	 * @param branchid
	 * @param flowordertypes
	 * @param currentDayZeroTime
	 * @param page
	 * @return
	 */
	public List<String> getOrderFlowJinRiChuKuORRuKuList(long branchid, String flowordertypes, String currentDayZeroTime, long page) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where nextbranchid=" + branchid + " " + "and credate>=" + time + " and flowordertype in(" + flowordertypes + ") ";
		sql += " limit " + ((page - 1) * Page.DETAIL_PAGE_NUMBER) + "," + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 今日出库(未到货)订单数
	 *
	 * @param branchid
	 * @param flowordertypes
	 * @param currentDayZeroTime
	 * @param page
	 * @return
	 */
	public List<String> getOrderFlowJinRiChuKuORRuKuListAll(long branchid, String flowordertypes, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where nextbranchid=" + branchid + " " + "and credate>=" + time + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 小件员领货 今日未领
	 *
	 * @param branchid
	 * @param string
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getOrderFlowLingHuoList(long branchid, String flowordertypes, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + "and credate >=" + time + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 小件员领货 今日未领
	 *
	 * @param branchid
	 * @param string
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getYuyuedaList(long branchid, String flowordertypes) {

		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 今日滞留
	 *
	 * @param branchid
	 * @param flowordertype
	 * @param deliverystate
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getjinrizhiliu(long branchid, int deliverystate, long flowordertype, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time where branchid=" + branchid + " " + "and credate >=" + time + " and deliverystate=" + deliverystate + " and flowordertype ="
				+ flowordertype;
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 历史未领货
	 *
	 * @param branchid
	 * @param flowordertypes
	 * @param currentDayZeroTime
	 * @return
	 */

	public List<String> getlishidaohuo(long branchid, String flowordertypes, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + "and credate <" + time + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 预约未领货
	 *
	 * @param branchid
	 * @param flowordertypes
	 * @param currentDayZeroTime
	 * @return
	 */

	public List<String> getYuyueweidaohuoAll(long branchid, String flowordertypes) {

		String sql = "select cwb from express_ops_operation_time  where nextbranchid=" + branchid + " " + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 分站到货历史未到货
	 *
	 * @param branchid
	 * @param flowordertypes
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getlishiweidaohuoAll(long branchid, String flowordertypes, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where nextbranchid=" + branchid + " " + "and credate <" + time + " and flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 已到货
	 *
	 * @param branchid
	 * @param flowordertype
	 * @return
	 */
	public List<String> getyidaohuoByBranchid(long branchid, int flowordertype) {
		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + " and flowordertype in(" + flowordertype+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()+")";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 历史滞留
	 *
	 * @param branchid
	 * @param value
	 * @param value2
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getlishizhiliu(long branchid, int deliverystate, int flowordertype, String currentDayZeroTime) {
		long time = 0l;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDayZeroTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + "and credate <" + time + " and deliverystate=" + deliverystate + " and flowordertype ="
				+ flowordertype;
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 超期
	 *
	 * @param branchid
	 * @param value
	 * @param value2
	 * @param currentDayZeroTime
	 * @return
	 */
	public List<String> getchaoqi(long branchid, long nextbranchid, int flowordertype) {
		String sql = "select cwb from express_ops_operation_time  where branchid=" + branchid + " " + " and nextbranchid=" + nextbranchid + " and flowordertype =" + flowordertype;
		return this.jdbcTemplate.queryForList(sql, String.class);
	}


	/**
	 * 退货、中转已入库订单列表,读取超期异常监控表
	 * @param branchids 退货或者中转站点ids
	 * @param flowordertype 操作环节
	 * @param cwbordertypeid 订单类型
	 * @param page 第几页
	 * @return 订单号list
	 */
	public List<String> getBackandChangeYiRukuListbyBranchid(String branchids,long flowordertype, int cwbordertypeid, long page) {
		String sql = "SELECT cwb FROM express_ops_operation_time WHERE branchid in("+branchids+") and flowordertype="+flowordertype+"  limit ?,?";
		if(cwbordertypeid >0){
			sql = "SELECT cwb FROM express_ops_operation_time WHERE branchid in("+branchids+") and flowordertype="+flowordertype+" and cwbordertypeid="+cwbordertypeid+" limit ?,?";
		}
		return this.jdbcTemplate.queryForList(sql, String.class, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}
	/**
	 * 退货、中转未入库订单列表,读取超期异常监控表
	 * @param branchids 退货或者中转站点ids
	 * @param flowordertype 操作环节
	 * @param cwbordertypeid 订单类型
	 * @param page 第几页
	 * @return 订单号list
	 */
	public List<String> getBackandChangeWeiRukuListbyBranchid(String branchids,long flowordertype, int cwbordertypeid, long page) {
		String sql = "SELECT cwb FROM express_ops_operation_time WHERE nextbranchid in("+branchids+") and flowordertype="+flowordertype+"  limit ?,?";
		if(cwbordertypeid >0){
			sql = "SELECT cwb FROM express_ops_operation_time WHERE nextbranchid in("+branchids+") and flowordertype="+flowordertype+" and cwbordertypeid="+cwbordertypeid+" limit ?,?";
		}
		return this.jdbcTemplate.queryForList(sql, String.class, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}


	public OperationTime getObjectBycwb(String cwb) {
		String sql = "SELECT * FROM express_ops_operation_time  where cwb=? ";

		try {
			return this.jdbcTemplate.queryForObject(sql, new OperationTimeRowMapper(), cwb);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public void updateOperationTimeMoney(String cwb, BigDecimal receivablefee,BigDecimal paybackfee) {
		String sql = "update express_ops_operation_time set receivablefee="+receivablefee+",paybackfee="+paybackfee+" where cwb=?";
		this.jdbcTemplate.update(sql,  cwb);
	}

	public List<OperationTime> getCwbViewList(long page,String cwbs, int cwbordertype, long customerid, long branchid,long begindate, long enddate) {
		String sql = "select * from express_ops_operation_time where flowordertype=15";
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer w = new StringBuffer("");
			if (cwbordertype > 0) {
				w.append(" and cwbordertypeid=" + cwbordertype);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (begindate> 0) {
				w.append(" and credate >=" + begindate);
			}
			if (enddate> 0) {
				w.append(" and credate <=" + enddate);
			}
			sql += w;
		}
		if (page!=-9) {
			sql+=" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + "," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new OperationTimeRowMapper());
	}

	public long getCwbViewListCount(String cwbs, int cwbordertype, long customerid, long branchid,long begindate, long enddate) {
		String sql = "select count(1) from express_ops_operation_time where flowordertype=15";
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer w = new StringBuffer("");
			if (cwbordertype > 0) {
				w.append(" and cwbordertypeid=" + cwbordertype);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (begindate> 0) {
				w.append(" and credate >= " + begindate);
			}
			if (enddate> 0) {
				w.append(" and credate <= " + enddate);
			}
			sql += w;
		}

		return this.jdbcTemplate.queryForLong(sql);
	}

}