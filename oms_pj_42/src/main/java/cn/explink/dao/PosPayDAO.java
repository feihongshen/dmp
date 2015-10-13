package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PosPayEntity;
import cn.explink.domain.Reason;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * POS刷卡支付
 * 
 * @author Administrator
 *
 */
@Component
public class PosPayDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 插入，针对付款
	 * 
	 * @param posPay
	 */
	public boolean PosPayRecord_insert(PosPayEntity posPay) {
		boolean flag = false;
		String sql = "insert into express_set_pos (pos_code,cwb,pos_money,pos_document,pos_payname,pos_paydate,pos_remark,pos_delivery,pos_deliveryname,"
				+ "pos_branchid,pos_branchname,pos_upbranchid,customerid,customername,shiptime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(sql, posPay.getPos_code(), posPay.getCwb(), posPay.getPos_money(), posPay.getPos_document(), posPay.getPos_payname(), posPay.getPos_paydate(), posPay.getPos_remark(),
					posPay.getPos_delivery(), posPay.getPos_deliveryname(), posPay.getBranchid(), posPay.getBranchname(), posPay.getUpbranchid(), posPay.getCustomerid(), posPay.getCustomername(),
					posPay.getShiptime());
			flag = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * 如果修改了pos反馈就修改
	 */
	public void UpdatePosMoney(double pos_money, String cwb) {
		String sql = "update express_set_pos set pos_money=? where cwb=? ";
		try {
			jdbcTemplate.update(sql, pos_money, cwb);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public long checkPos(String cwb) {
		String sql = "select count(1) from express_set_pos where cwb='" + cwb + "'";
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 修改,针对签收
	 */
	public boolean PosPayRecord_update(PosPayEntity posPay) {
		boolean flag = false;
		String sql = "update express_set_pos set pos_signname=?,pos_signtime=?,pos_signremark=?,pos_signtype=? where cwb=? ";
		try {
			jdbcTemplate.update(sql, posPay.getPos_signname(), posPay.getPos_signtime(), posPay.getPos_signremark(), posPay.getPos_signtype(), posPay.getCwb());
			flag = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * 查询POS刷卡记录
	 * 
	 * @param posPay
	 * @return
	 */
	public List<PosPayEntity> PosPayRecord_selectByList(String pos_code, long customerid, long deliverid, long branchid, String payname, String cwb, String starttime, String endtime, int signtype,
			long branchidSession, int branchtype, String starttime_sp, String endtime_sp, int pos_backoutflag, long page) {
		List<PosPayEntity> datalist = null;
		String sql = "select * from  express_set_pos where 1=1 ";
		if (branchtype == BranchEnum.CaiWu.getValue()) {
			sql += " and pos_upbranchid=" + branchidSession;
		} else {
			sql += " and pos_branchid=" + branchidSession;
		}
		sql = getWhereSql(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, signtype, branchidSession, branchtype, starttime_sp, endtime_sp, pos_backoutflag, sql);
		if (page != -1) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}

		try {
			datalist = jdbcTemplate.query(sql, new PosPayRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return datalist;

	}

	private String getWhereSql(String pos_code, long customerid, long deliverid, long branchid, String payname, String cwb, String starttime, String endtime, int signtype, long branchidSession,
			int branchtype, String starttime_sp, String endtime_sp, int pos_backoutflag, String sql) {

		if (pos_code != null && !"".equals(pos_code)) {
			sql += " and pos_code='" + pos_code + "'";
		}
		if (pos_backoutflag > -1) {
			sql += " and pos_backoutflag=" + pos_backoutflag;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (cwb != null && !"".equals(cwb)) {
			sql += " and cwb='" + cwb + "'";
		}
		if (starttime != null && !"".equals(starttime) && endtime != null && !"".equals(endtime)) {
			sql += " and pos_paydate between '" + starttime + "' and '" + endtime + "' ";
		} else {
			sql += " and pos_paydate between '" + (DateTimeUtil.getNowDate() + " 00:00:00") + "' and '" + DateTimeUtil.getNowTime() + "' ";
		}

		if (starttime_sp != null && !"".equals(starttime_sp) && endtime_sp != null && !"".equals(endtime_sp)) {
			sql += " and shiptime between '" + starttime_sp + "' and '" + endtime_sp + "' ";
		} else {
			sql += " and shiptime between '" + (DateTimeUtil.getNowDate() + " 00:00:00") + "' and '" + DateTimeUtil.getNowTime() + "' ";
		}
		if (signtype > -1) {
			sql += " and pos_signtype =" + signtype;
		}
		if (deliverid > 0) {
			sql += " and pos_delivery=" + deliverid;
		}
		if (branchid > 0) {
			sql += " and pos_branchid=" + branchid;
		}
		if (!"".equals(payname)) {
			sql += " and pos_payname  like '%" + payname + "%' ";
		}
		return sql;
	}

	/**
	 * 查询POS刷卡记录
	 * 
	 * @param posPay
	 * @return
	 */
	public int PosPayRecord_selectByListCount(String pos_code, long customerid, long deliverid, long branchid, String payname, String cwb, String starttime, String endtime, int signtype,
			long branchidSession, int branchtype, String starttime_sp, String endtime_sp, int pos_backoutflag) {
		int returnCount = 0;
		String sql = "select count(1) from  express_set_pos where 1=1 ";
		if (branchtype == BranchEnum.CaiWu.getValue()) {
			sql += " and pos_upbranchid=" + branchidSession;
		} else {
			sql += " and pos_branchid=" + branchidSession;
		}
		sql = getWhereSql(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, signtype, branchidSession, branchtype, starttime_sp, endtime_sp, pos_backoutflag, sql);
		try {
			returnCount = jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return returnCount;

	}

	private final class PosPayRowMapper implements RowMapper<PosPayEntity> {
		@Override
		public PosPayEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			PosPayEntity pospay = new PosPayEntity();
			pospay.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			pospay.setPos_document(StringUtil.nullConvertToEmptyString(rs.getString("pos_document")));
			pospay.setPos_code(StringUtil.nullConvertToEmptyString(rs.getString("pos_code")));
			pospay.setPos_payname(StringUtil.nullConvertToEmptyString(rs.getString("pos_payname")));
			pospay.setPos_money(rs.getDouble("pos_money"));
			pospay.setPos_paydate(rs.getString("pos_paydate"));
			pospay.setPos_signtype(rs.getInt("pos_signtype"));
			pospay.setPos_signname(StringUtil.nullConvertToEmptyString(rs.getString("pos_signname")));
			pospay.setPos_signtime(StringUtil.nullConvertToEmptyString(rs.getString("pos_signtime")));
			pospay.setPos_signremark(StringUtil.nullConvertToEmptyString(rs.getString("pos_signremark")));
			pospay.setPos_remark(StringUtil.nullConvertToEmptyString(rs.getString("pos_remark")));
			pospay.setPos_delivery(rs.getInt("pos_delivery"));
			pospay.setPos_deliveryname(StringUtil.nullConvertToEmptyString(rs.getString("pos_deliveryname")));
			pospay.setUpbranchid(rs.getInt("pos_upbranchid"));
			pospay.setBranchid(rs.getInt("pos_branchid"));
			pospay.setBranchname(StringUtil.nullConvertToEmptyString(rs.getString("pos_branchname")));
			pospay.setId(rs.getInt("id"));
			pospay.setCustomerid(rs.getLong("customerid"));
			pospay.setCustomername(rs.getString("customername"));
			pospay.setShiptime(rs.getString("shiptime"));
			pospay.setPos_backoutflag(rs.getInt("pos_backoutflag"));

			return pospay;
		}
	}
}
