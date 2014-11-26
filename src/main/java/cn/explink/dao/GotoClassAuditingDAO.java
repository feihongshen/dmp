package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.controller.GotoClassAndDeliveryDTO;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.util.Page;

@Component
public class GotoClassAuditingDAO {
	private final class GotoClassAuditingRowMapper implements RowMapper<GotoClassAuditing> {
		@Override
		public GotoClassAuditing mapRow(ResultSet rs, int rowNum) throws SQLException {
			GotoClassAuditing gca = new GotoClassAuditing();
			gca.setId(rs.getInt("id"));
			gca.setAuditingtime(rs.getString("auditingtime"));
			gca.setPayupamount(rs.getBigDecimal("payupamount"));
			gca.setPayupamount_pos(rs.getBigDecimal("payupamount_pos"));
			gca.setReceivedfeeuser(rs.getLong("receivedfeeuser"));
			gca.setBranchid(rs.getLong("branchid"));
			gca.setPayupid(rs.getLong("payupid"));
			gca.setDeliverealuser(rs.getLong("deliverealuser"));

			gca.setDeliverpayuptype(rs.getInt("deliverpayuptype"));
			gca.setDeliverpayupamount(rs.getBigDecimal("deliverpayupamount"));
			gca.setDeliverpayupamount_pos(rs.getBigDecimal("deliverpayupamount_pos"));
			gca.setDeliverpayupbanknum(rs.getString("deliverpayupbanknum"));
			gca.setDeliverpayupapproved(rs.getInt("deliverpayupapproved"));
			gca.setDeliverpayuparrearage(rs.getBigDecimal("deliverpayuparrearage"));
			gca.setDeliverpayuparrearage_pos(rs.getBigDecimal("deliverpayuparrearage_pos"));
			gca.setDeliverAccount(rs.getBigDecimal("deliverAccount"));
			gca.setDeliverPosAccount(rs.getBigDecimal("deliverPosAccount"));
			gca.setCheckremark(rs.getString("checkremark"));
			gca.setPayupaddress(rs.getString("payupaddress"));
			gca.setUpdatetime(rs.getString("updateTime"));
			return gca;
		}
	}

	private final class GotoClassAuditingAndDeliveryRowMapper implements RowMapper<GotoClassAndDeliveryDTO> {
		@Override
		public GotoClassAndDeliveryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			GotoClassAndDeliveryDTO ga = new GotoClassAndDeliveryDTO();

			ga.setAuditingtime(rs.getString("auditingtime"));
			ga.setPayupamount(rs.getBigDecimal("payupamount"));
			ga.setDeliverealuser(rs.getLong("deliverealuser"));
			ga.setCwb(rs.getString("cwb"));
			ga.setReceivedfee(rs.getBigDecimal("receivedfee"));
			ga.setReturnedfee(rs.getBigDecimal("returnedfee"));
			ga.setBusinessfee(rs.getBigDecimal("businessfee"));
			ga.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			ga.setDeliverystate(rs.getLong("deliverystate"));
			ga.setCash(rs.getBigDecimal("cash"));
			ga.setPos(rs.getBigDecimal("pos"));
			ga.setCodpos(rs.getBigDecimal("codpos"));
			ga.setPosremark(rs.getString("posremark"));
			ga.setMobilepodtime(rs.getString("mobilepodtime"));
			ga.setCheckfee(rs.getBigDecimal("checkfee"));
			ga.setCheckremark(rs.getString("checkremark"));
			ga.setOtherfee(rs.getBigDecimal("otherfee"));
			ga.setPodremarkid(rs.getLong("podremarkid"));
			ga.setDeliverstateremark(rs.getString("deliverstateremark"));
			/*
			 * ga.setConsigneename(rs.getString("consigneename"));
			 * ga.setConsigneephone(rs.getString("consigneephone"));
			 */
			ga.setUpdateTime(rs.getString("updateTime"));

			return ga;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 产生一条归班记录
	 * 
	 * @param branchid
	 * @param receivedfeeuser
	 * @param subAmount
	 * @param okTime
	 * @param subAmountPos
	 */
	public long creGotoClassAuditing(final String okTime, final String subAmount, final String subAmountPos, final long receivedfeeuser, final long branchid, final long deliverealuser) {
		return creGotoClassAuditingAndDeliverPayUp(okTime, subAmount, subAmountPos, receivedfeeuser, branchid, deliverealuser, 0, BigDecimal.ZERO, "", "", BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
	}

	/**
	 * 产生一条归班记录 并包含小件员交款的相关款项信息
	 * 
	 * @param okTime
	 *            审核时间 如果使用小件员交款功能，则也同时代表了小件员交款时间
	 * @param subAmount
	 *            需上交款的金额（站点交给公司）
	 * @param subAmountPos
	 *            本次交款的POS总金额
	 * @param receivedfeeuser
	 *            收款人
	 * @param branchid
	 *            站点编号
	 * @param deliverealuser
	 *            归班人 小件员和站长
	 * @param deliverpayuptype
	 *            小件员交款类型 0非小件员交款 1网银（网银需要小票号） 2POS 3现金
	 * @param deliverpayupamount
	 *            小件员交款金额 小数点两位
	 * @param deliverpayupbanknum
	 *            小件员交款网银的小票号
	 * @param deliverpayuparrearage
	 *            小件员欠款 小数点两位 公式： 应交款金额-交款金额-小件员帐户现金余额
	 * @param deliverpayupamount_pos
	 *            小件员交用户POS刷卡款 小数点两位
	 * @param deliverpayuparrearage_pos
	 *            小件员交用户POS刷卡欠款 小数点两位 公式： 应交POS款金额-交POS款金额-小件员帐户POS款余额
	 * @param payupid
	 *            上交款id 如果为-1 代表是小件员上交款模式
	 * @return
	 */
	public long creGotoClassAuditingAndDeliverPayUp(final String okTime, final String subAmount, final String subAmountPos, final long receivedfeeuser, final long branchid, final long deliverealuser,
			final int deliverpayuptype, final BigDecimal deliverpayupamount, final String deliverpayupbanknum, final String deliverpayupaddress, final BigDecimal deliverpayuparrearage,
			final BigDecimal deliverpayupamount_pos, final BigDecimal deliverpayuparrearage_pos, final BigDecimal deliverAccount, final BigDecimal deliverPosAccount, final long payupid) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_goto_class_auditing(" + "auditingtime,payupamount,payupamount_pos,receivedfeeuser,branchid,payupid,deliverealuser"
						+ ",deliverpayuptype,deliverpayupamount,deliverpayupbanknum,payupaddress,deliverpayuparrearage,deliverpayupamount_pos,deliverpayuparrearage_pos"
						+ ",deliverpayupapproved,deliverAccount,deliverPosAccount)" + " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,0, ?,?) ", new String[] { "id" });
				ps.setString(1, okTime);
				ps.setString(2, subAmount);
				ps.setString(3, subAmountPos);
				ps.setLong(4, receivedfeeuser);
				ps.setLong(5, branchid);
				ps.setLong(6, payupid);
				ps.setLong(7, deliverealuser);
				ps.setInt(8, deliverpayuptype);
				ps.setBigDecimal(9, deliverpayupamount);
				ps.setString(10, deliverpayupbanknum);
				ps.setString(11, deliverpayupaddress);
				ps.setBigDecimal(12, deliverpayuparrearage);
				ps.setBigDecimal(13, deliverpayupamount_pos);
				ps.setBigDecimal(14, deliverpayuparrearage_pos);
				ps.setBigDecimal(15, deliverAccount);
				ps.setBigDecimal(16, deliverPosAccount);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 返回当前应上缴的归班记录列表
	 * 
	 * @param branchid
	 * @param receivedfeeuser
	 * @param subAmount
	 * @param okTime
	 */
	public List<GotoClassAuditing> getCountPayToUp(long branchid) {
		String sql = "select * from express_ops_goto_class_auditing where branchid=? and payupid=0";
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper(), branchid);
	}

	/**
	 * 返回当前id对应的归班记录
	 */
	public GotoClassAuditing getGotoClassAuditingByGcaid(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_goto_class_auditing where id=?", new GotoClassAuditingRowMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 返回当前id对应的归班记录 并锁行
	 */
	public GotoClassAuditing getGotoClassAuditingByGcaidLock(long id) {
		return jdbcTemplate.queryForObject("select * from express_ops_goto_class_auditing where id=? for update", new GotoClassAuditingRowMapper(), id);
	}

	public List<GotoClassAuditing> getGotoClassAuditingByGcaids(String gcaids) {
		String sql = "select * from express_ops_goto_class_auditing where id in (" + gcaids + ") and payupid=0";
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper());
	}

	/**
	 * 修改本次结算操作对应的归班记录的结算id字段
	 * 
	 * @param gcaids
	 * @param payupid
	 */
	public void updateGotoClassAuditingForPayupidByGcaids(String gcaids, long payupid) {
		String sql = "update  express_ops_goto_class_auditing set payupid=? where id in (" + gcaids + ") and payupid=0";
		jdbcTemplate.update(sql, payupid);
	}

	public List<GotoClassAuditing> getGotoClassAuditingByPage(long page, long deliverealuser, String begindate, String enddate, long branchid) {
		String sql = " select * from express_ops_goto_class_auditing where branchid='" + branchid + "' and auditingtime>='" + begindate + "' and auditingtime<='" + enddate + "' ";
		sql = this.getGotoClassAuditingByPageWhereSql(sql, deliverealuser);
		sql += "order by auditingtime desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<GotoClassAuditing> gcas = jdbcTemplate.query(sql, new GotoClassAuditingRowMapper());
		return gcas;
	}

	private String getGotoClassAuditingByPageWhereSql(String sql, long deliverealuser) {
		if (deliverealuser > 0) {
			sql += " and deliverealuser='" + deliverealuser + "' ";
		}
		return sql;
	}

	public long getGotoClassAuditingCount(long deliverealuser, String begindate, String enddate, long branchid) {
		String sql = "select count(1) from express_ops_goto_class_auditing where branchid='" + branchid + "' and auditingtime>='" + begindate + "' and auditingtime<='" + enddate + "' ";
		sql = this.getGotoClassAuditingByPageWhereSql(sql, deliverealuser);
		return jdbcTemplate.queryForInt(sql);
	}

	/**
	 * 显示未交款的明细
	 */
	public List<GotoClassAndDeliveryDTO> getNoPayUpDetail(long branchid) {
		String sql = "SELECT gca.*,ds.* FROM express_ops_delivery_state ds right join express_ops_goto_class_auditing gca on ds.gcaid=gca.id  WHERE ds.deliverystate IN (?,?,?,?,?) and  ds.deliverybranchid=? and gca.payupid=0";

		return jdbcTemplate.query(sql, new GotoClassAuditingAndDeliveryRowMapper(), DeliveryStateEnum.PeiSongChengGong.getValue(), DeliveryStateEnum.ShangMenTuiChengGong.getValue(),
				DeliveryStateEnum.ShangMenHuanChengGong.getValue(), DeliveryStateEnum.BuFenTuiHuo.getValue(), DeliveryStateEnum.HuoWuDiuShi.getValue(), branchid);
	}

	public List<GotoClassAuditing> getGotoClassAuditingByPayUpId(long payupid) {
		String sql = "select * from express_ops_goto_class_auditing where payupid=?";
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper(), payupid);
	}

	private String getGcaByTimeWhereSql(String sql, long branchid, long deliverealuser, long deliverpayupapproved) {
		if (branchid > 0 || deliverealuser > 0 || deliverpayupapproved > -1) {
			StringBuffer w = new StringBuffer();
			if (branchid > 0) {
				w.append(" and branchid = " + branchid);
			}
			if (deliverealuser > 0) {
				w.append(" and deliverealuser = " + deliverealuser);
			}
			if (deliverpayupapproved > -1) {
				w.append(" and deliverpayupapproved = " + deliverpayupapproved);
			}

			sql += w.toString();
		}
		return sql;
	}

	public List<GotoClassAuditing> getGotoClassAuditingByAuditingtime(long page, String begindate, String enddate, long branchid, long deliverealuser, long deliverpayupapproved) {
		String sql = "select * from express_ops_goto_class_auditing where auditingtime > '" + begindate + "' and auditingtime < '" + enddate + "'";
		sql = this.getGcaByTimeWhereSql(sql, branchid, deliverealuser, deliverpayupapproved);
		sql = sql + " order by deliverealuser,auditingtime ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper());
	}

	public long getGotoClassAuditingByAuditingtimeCount(String begindate, String enddate, long branchid, long deliverealuser, long deliverpayupapproved) {
		String sql = "select count(1) from express_ops_goto_class_auditing  where auditingtime > '" + begindate + "' and auditingtime < '" + enddate + "'";
		sql = this.getGcaByTimeWhereSql(sql, branchid, deliverealuser, deliverpayupapproved);
		sql = sql + " order by deliverealuser ";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<GotoClassAuditing> getGotoClassAuditingByAuditingtimeAndBranchid(long branchid, String begindate, String enddate, long deliverpayupapproved, String branchids) {
		String sql = "select * from express_ops_goto_class_auditing where branchid=" + branchid + " and auditingtime > '" + begindate + "' ";
		if (enddate.length() > 0 || deliverpayupapproved > -1 || branchids.length() > 1) {
			StringBuffer w = new StringBuffer();
			if (enddate.length() > 0) {
				w.append(" and auditingtime < '" + enddate + "'");
			}
			if (deliverpayupapproved > -1) {
				w.append(" and deliverpayupapproved=" + deliverpayupapproved);
			}
			if (branchids.length() > 1) {
				w.append(" and branchid in(" + branchids + ")");

			}
			sql += w.toString();
		}
		sql += " order by deliverealuser,auditingtime";
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper());
	}

	public List<GotoClassAuditing> getGotoClassAuditingByAuditingInfo(long branchid, String begindate, String enddate, long deliverpayupapproved, long deliver) {
		String sql = "select * from express_ops_goto_class_auditing where branchid=" + branchid + " and auditingtime > '" + begindate + "' ";
		if (enddate.length() > 0 || deliverpayupapproved > -1) {
			StringBuffer w = new StringBuffer();
			if (enddate.length() > 0) {
				w.append(" and auditingtime < '" + enddate + "'");
			}
			if (deliverpayupapproved > -1) {
				w.append(" and deliverpayupapproved=" + deliverpayupapproved);
			}
			if (deliver != 0) {
				w.append(" and deliverealuser=" + Long.toString(deliver));

			}
			sql += w.toString();
		}
		sql += " order by deliverealuser,auditingtime";
		return jdbcTemplate.query(sql, new GotoClassAuditingRowMapper());
	}

	public Integer getDeliverPayUpAndArrearageByDeliverid(long userid) {
		return jdbcTemplate.queryForInt("SELECT COUNT(1) FROM `express_ops_goto_class_auditing` WHERE deliverealuser=? AND deliverpayuparrearage<>deliverpayuparrearageamount", userid);
	}

	public void updateStateAndRemark(long id, String checkremark, long deliverpayupapproved) {
		String sql = "update  express_ops_goto_class_auditing set checkremark=?,deliverpayupapproved=? where id = ?  ";
		jdbcTemplate.update(sql, checkremark, deliverpayupapproved, id);
	}

	public void updateStateAndMoney(long id, String deliverpayupamount, BigDecimal deliverpayuparrearage) {
		String sql = "update  express_ops_goto_class_auditing set deliverpayupamount=?,deliverpayuparrearage=?,deliverpayupapproved=0 where id = ?  ";
		jdbcTemplate.update(sql, deliverpayupamount, deliverpayuparrearage, id);
	}

	private final class AuditingByBranchid implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("branchid", rs.getString("branchid"));
			reJson.put("amount", rs.getString("amount"));
			reJson.put("pos", rs.getString("pos"));
			return reJson;
		}
	}

	public List<JSONObject> getAuditingByDayGroupByBranchid(String day, String secondDay) {
		try {
			String sql = "SELECT branchid,SUM(`payupamount`) as amount,SUM(`payupamount_pos`) as pos FROM `express_ops_goto_class_auditing` WHERE payupid=0 AND auditingtime>? AND auditingtime<=? GROUP BY branchid";
			return jdbcTemplate.query(sql, new AuditingByBranchid(), day, secondDay);
		} catch (DataAccessException e) {
			return null;
		}
	}

	// ==============================修改订单使用的方法 start
	// ==================================
	/**
	 * 重置审核状态 修改归班表字段
	 * 
	 * @param id
	 *            归班记录id
	 * @param gca_payupamount
	 *            归班非POS金额 减去重置审核状态订单的非POS金额 的值
	 * @param gca_payupamount_pos
	 *            归班POS金额 减去重置审核状态订单的POS金额 的值
	 * @param updateTime
	 *            修改订单时间
	 */
	public void updateForChongZhiShenHe(long id, BigDecimal gca_payupamount, BigDecimal gca_payupamount_pos, BigDecimal deliverpayuparrearage, BigDecimal deliverpayuparrearage_pos, String updateTime) {
		jdbcTemplate.update("update  express_ops_goto_class_auditing set " + "payupamount=?,payupamount_pos=?,deliverpayuparrearage=?" + ",deliverpayuparrearage_pos=?,updatetime=? where id = ? ",
				gca_payupamount, gca_payupamount_pos, deliverpayuparrearage, deliverpayuparrearage_pos, updateTime, id);

	}
	// ==============================修改订单使用的方法 End
	// ==================================
}
