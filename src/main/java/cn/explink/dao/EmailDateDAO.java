package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.EmailDate;
import cn.explink.util.StringUtil;

@Component
public class EmailDateDAO {
	private final class EmailDateRowMapper implements RowMapper<EmailDate> {
		@Override
		public EmailDate mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailDate emaildate = new EmailDate();
			emaildate.setEmaildateid(rs.getLong("emaildateid"));
			emaildate.setEmaildatetime(rs.getString("emaildatetime"));
			emaildate.setUserid(rs.getLong("userid"));
			emaildate.setAreaid(rs.getLong("areaid"));
			emaildate.setWarehouseid(rs.getLong("warehouseid"));
			emaildate.setCustomerid(rs.getLong("customerid"));
			emaildate.setBranchid(rs.getLong("branchid"));
			emaildate.setState(rs.getInt("state"));
			emaildate.setCwbcount(rs.getInt("cwbcount"));
			return emaildate;
		}
	}

	private final class EmailDateDetailRowMapper implements RowMapper<EmailDate> {
		@Override
		public EmailDate mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailDate emaildate = new EmailDate();
			emaildate.setEmaildateid(rs.getLong("emaildateid"));
			emaildate.setEmaildatetime(rs.getString("emaildatetime"));
			emaildate.setUserid(rs.getLong("userid"));
			emaildate.setAreaid(rs.getLong("areaid"));
			emaildate.setWarehouseid(rs.getLong("warehouseid"));
			emaildate.setCustomerid(rs.getLong("customerid"));
			emaildate.setAreaname(StringUtil.nullConvertToEmptyString(rs.getString("areaname")));
			emaildate.setWarehousename(StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouse")));
			emaildate.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			emaildate.setBranchid(rs.getLong("branchid"));
			emaildate.setState(rs.getInt("state"));
			return emaildate;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<EmailDate> getAllEmailDate() {
		List<EmailDate> emailDate = jdbcTemplate.query("select * from express_ops_emaildate", new EmailDateRowMapper());
		return emailDate;
	}

	public EmailDate getEmailDateByKeys(long customerid, long warehouseid, String nowdate) {
		List<EmailDate> el = null;
		try {
			el = jdbcTemplate.query("select * from express_ops_emaildate where SUBSTRING(emaildatetime,1,10)=? and  customerid=? and warehouseid=?", new EmailDateRowMapper(), nowdate, customerid,
					warehouseid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (el != null && el.size() > 0) {
			return el.get(0);
		}
		return null;
	}

	/**
	 * 等同于上面的方法
	 * 
	 * @param customerid
	 * @param warehouseid
	 * @param nowdate
	 * @return
	 */
	public EmailDate getEmailDateByKeys_effect(long customerid, long warehouseid, String nowdate) {
		List<EmailDate> el = null;
		try {
			el = jdbcTemplate.query("select * from express_ops_emaildate where SUBSTRING(emaildatetime,1,10)=? and  customerid=? and state<>2 and warehouseid=?", new EmailDateRowMapper(), nowdate,
					customerid, warehouseid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (el != null && el.size() > 0) {
			return el.get(0);
		}
		return null;
	}

	public EmailDate getEmailDateByNowdate(long customerid, long warehouseid, String nowdate) {
		List<EmailDate> el = null;
		try {
			el = jdbcTemplate.query("select * from express_ops_emaildate where emaildatetime='" + nowdate + "' and  customerid=" + customerid + " and state<>2 and warehouseid=" + warehouseid,
					new EmailDateRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (el != null && el.size() > 0) {
			return el.get(0);
		}
		return null;
	}

	public long getCreateEmailDate(final String emaildate, final long userid, final long areaid, final long warehouseid, final long customerid, final long branchid, final long cwbcount) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_emaildate(emaildatetime,userid,areaid,warehouseid,customerid,branchid,cwbcount) values(?,?,?,?,?,?,?)",
						new String[] { "emaildateid" });
				ps.setString(1, emaildate);
				ps.setLong(2, userid);
				ps.setLong(3, areaid);
				ps.setLong(4, warehouseid);
				ps.setLong(5, customerid);
				ps.setLong(6, branchid);
				ps.setLong(7, cwbcount);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public int getEmailDateCount(String emaildate) {
		return jdbcTemplate.queryForInt("select count(1) from express_ops_emaildate where emaildatetime=?", emaildate);
	}

	public List<EmailDate> getEmailDateByDate(String beginemaildate, long myBranchid) {
		String sql = "select ed.*,cw.`customerwarehouse`,a.areaname,ci.customername from " + "express_set_customer_warehouse cw right join (select * from express_ops_emaildate where"
				+ " emaildatetime >'" + beginemaildate + "' and branchid='" + myBranchid + "' and (state=0  or state=1) order by emaildatetime desc) ed"
				+ " on cw.warehouseid=ed.warehouseid left join express_set_account_area a on ed.areaid=a.areaid " + " left join express_set_customer_info ci on ed.customerid=ci.customerid";
		return jdbcTemplate.query(sql, new EmailDateDetailRowMapper());
	}

	public List<EmailDate> getEmailDateByDateByUserid(String beginemaildate, String endemaildate, long userid) {
		return jdbcTemplate.query("select ed.*,cw.`customerwarehouse`,a.areaname,ci.customername from " + "express_set_customer_warehouse cw right join (select * from express_ops_emaildate where"
				+ " emaildatetime >? and emaildatetime <=? and userid=? and (state=0  or state=1) order by emaildatetime desc) ed"
				+ " on cw.warehouseid=ed.warehouseid left join express_set_account_area a on ed.areaid=a.areaid " + " left join express_set_customer_info ci on ed.customerid=ci.customerid",
				new EmailDateDetailRowMapper(), beginemaildate, endemaildate, userid);
	}

	/**
	 * 非到货查询
	 * 
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<EmailDate> getEmailDateByDateAndState(String beginemaildate, String endemaildate) {
		return jdbcTemplate.query("select ed.*,cw.`customerwarehouse`,a.areaname,ci.customername from " + "express_set_customer_warehouse cw right join (select * from express_ops_emaildate where"
				+ " emaildatetime >? and emaildatetime <=? and state=0 order by emaildatetime desc) ed"
				+ " on cw.warehouseid=ed.warehouseid left join express_set_account_area a on ed.areaid=a.areaid " + " left join express_set_customer_info ci on ed.customerid=ci.customerid",
				new EmailDateDetailRowMapper(), beginemaildate, endemaildate);
	}

	/**
	 * 得到供货商指定时间内的批次
	 */
	public List<EmailDate> getEmailDateByEmaildatetimeAndCustomerid(String beginemaildate, long customerid) {
		String sql = "select ed.*,cw.`customerwarehouse`,a.areaname,ci.customername from " + "express_set_customer_warehouse cw right join (select * from express_ops_emaildate where"
				+ " emaildatetime >? and customerid =? and state>-1 and state<2 order by emaildatetime desc) ed"
				+ " on cw.warehouseid=ed.warehouseid left join express_set_account_area a on ed.areaid=a.areaid " + " left join express_set_customer_info ci on ed.customerid=ci.customerid";
		return jdbcTemplate.query(sql, new EmailDateDetailRowMapper(), beginemaildate, customerid);

	}

	public String getEmailDateByIdAndState(long id) {
		try {
			return jdbcTemplate.queryForObject("select emaildatetime from express_ops_emaildate where emaildateid=? and state<>2 ", String.class, id);
		} catch (Exception e) {
			return "";
		}
	}

	public EmailDate getEmailDateById(long id) {
		List<EmailDate> el = jdbcTemplate.query("select * from express_ops_emaildate where emaildateid=?", new EmailDateRowMapper(), id);
		if (el.size() > 0) {
			return el.get(0);
		}
		return null;
	}

	public long saveEmailDateToEmailDate(long emaildateid) {
		return jdbcTemplate.update("update express_ops_emaildate set state=1 where emaildateid=? and state<>1 ", emaildateid);
	}

	public long saveEmailDateStateToEmailDate(long emaildateid) {
		return jdbcTemplate.update("update express_ops_emaildate set state=0 where emaildateid=? and state=2 ", emaildateid);
	}

	public long loseEmailDateById(long emaildateid) {
		return jdbcTemplate.update("update express_ops_emaildate set state=2 where emaildateid=? ", emaildateid);
	}

	public void editEdit(long emaildateid, long areaid, long warehouseid, long customerid) {
		jdbcTemplate.update("update express_ops_emaildate set areaid=?,warehouseid=?,customerid=? where  emaildateid=?", areaid, warehouseid, customerid, emaildateid);
	}

	public List<EmailDate> getEmailDateByBranchidForState(long branchid, int state) {
		String sql = "select * from express_ops_emaildate where branchid=? and state=?";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), branchid, state);
	}

	public List<EmailDate> getEmailDateAllForState(int state) {
		String sql = "select * from express_ops_emaildate where state=?";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), state);
	}

	public void editEditEmaildateForCwbcount(long cwbcount, long emaildateid) {
		String sql = "update express_ops_emaildate set cwbcount=? where emaildateid=?";
		jdbcTemplate.update(sql, cwbcount, emaildateid);
	}

	public void editEditEmaildateForCwbcountAdd(long emaildateid) {
		String sql = "update express_ops_emaildate set cwbcount=cwbcount+1 where emaildateid=?";
		jdbcTemplate.update(sql, emaildateid);
	}

	public void editEditEmaildateForFinanceauditcount(long financeauditcount, long emaildateid) {
		String sql = "update express_ops_emaildate set financeauditcount=? where emaildateid=? ";
		jdbcTemplate.update(sql, financeauditcount, emaildateid);
	}

	public List<EmailDate> getEmailDateForWhere(String emaildate, long areaid, long warehouseid, long customerid) {
		String sql = "select * from express_ops_emaildate where areaid=? and warehouseid=? and customerid=? and emaildatetime=? and state<>2";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), areaid, warehouseid, customerid, emaildate);

	}

	/**
	 * 获得发货批次，条件是这个批次下的订单存在未审核的部分
	 * 
	 * @param customerids
	 * @return
	 */
	public List<EmailDate> getEmailDateByFinanceAuditCount(String customerids) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE state<>2 AND customerid in (" + customerids + ") and cwbcount>financeauditcount ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper());
	}

	public List<EmailDate> getEmailDateByCustomerids(String customerids) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE state<>2 AND customerid in (" + customerids + ") ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper());
	}

	public List<EmailDate> getEmailDateByCustomerid(long customerid) {
		String sql = "select * from express_ops_emaildate where customerid =? and state<>2 ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), customerid);

	}

	public List<EmailDate> getEmailDateByEmaildate(String startTime, String endTime) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE  emaildatetime>=? and  emaildatetime<=?  ORDER BY emaildateid DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), startTime, endTime);
	}

	public List<EmailDate> getEmailDateByEmaildateAndCustomerid(long customerid, String startTime, String endTime) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE state<>2 AND emaildatetime>=? and  emaildatetime<=? and customerid=? ORDER BY emaildateid DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), startTime, endTime, customerid);
	}

	public long getEmailDateMaxid() {
		String sql = "SELECT (CASE WHEN MAX(emaildateid) IS NULL THEN 0 ELSE MAX(emaildateid) END ) FROM express_ops_emaildate WHERE state<>2";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<EmailDate> getEmailDateByEmaildateid(long startTimeid, Long endTimeid) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE emaildateid>=? and  emaildateid<=?  ORDER BY emaildateid DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), startTimeid, endTimeid);
	}

	// ===========================新增修改匹配站新页面=====================
	public List<EmailDate> getEmailDateByEmaildateAndCustomeridOnly(String startTime, long Customerid) {
		String sql = "SELECT * FROM express_ops_emaildate WHERE  emaildatetime>=? and  customerid=?  ORDER BY emaildateid DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), startTime, Customerid);
	}

	public Collection<? extends EmailDate> getEmailDateByCustomerid(long customerid, String state) {
		String sql = "select * from express_ops_emaildate where customerid =? and state in (0,1) ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), customerid);
	}
	
	/**
	 * 查询10天以内的批次信息
	 * @author 刘武强
	 * @date:2016年8月3日 下午3:56:34 
	 * @params:@param customerid
	 * @params:@param branchid
	 * @params:@return
	 */
	public Collection<? extends EmailDate> getEmailDateByCustomeridInTenDays(long customerid, String state) {
		String sql = "select * from express_ops_emaildate where customerid =? and state in (0,1) and emaildatetime > DATE_ADD(NOW(), INTERVAL - 10 DAY) ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), customerid);
	}
	
	public Collection<? extends EmailDate> getEmailDateByCustomeridAndWarehouseId(long customerid, long branchid) {
		String sql = "select * from express_ops_emaildate where customerid =? and state<>2 and branchid=?  ORDER BY emaildatetime DESC ";
		return jdbcTemplate.query(sql, new EmailDateRowMapper(), customerid, branchid);
	}

}
