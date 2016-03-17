package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sourceforge.jtds.jdbc.DateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExceptionCwb;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Component
public class ExceptionCwbDAO {
	private final class ExceptionCwbMapper implements RowMapper<ExceptionCwb> {
		@Override
		public ExceptionCwb mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExceptionCwb exceptionCwb = new ExceptionCwb();
			exceptionCwb.setId(rs.getLong("id"));
			exceptionCwb.setCwb(rs.getString("cwb"));
			exceptionCwb.setScantype(rs.getLong("scantype"));
			exceptionCwb.setErrortype(rs.getString("errortype"));
			exceptionCwb.setCreatetime(rs.getTimestamp("createtime"));
			exceptionCwb.setBranchid(rs.getLong("branchid"));
			exceptionCwb.setUserid(rs.getLong("userid"));
			exceptionCwb.setIshanlder(rs.getLong("ishanlder"));
			exceptionCwb.setCustomerid(rs.getLong("customerid"));
			exceptionCwb.setDriverid(rs.getLong("driverid"));
			exceptionCwb.setTruckid(rs.getLong("truckid"));
			exceptionCwb.setDeliverid(rs.getLong("deliverid"));
			exceptionCwb.setInterfacetype(rs.getString("interfacetype"));
			exceptionCwb.setRemark(rs.getString("remark"));
			exceptionCwb.setScancwb(rs.getString("scancwb"));
			return exceptionCwb;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ExceptionCwb> getAllECByPage(long page, String cwb, long scantype, String errortype, String branchid, String userid, long ishanlder, String beginemaildate, String endemaildate, long scope) {
		try {
			String sql = "select * from express_ops_exception_cwb where createtime >? and createtime <?";
			sql = this.getECByWhereSqlNew(sql, cwb, scantype, errortype, branchid, userid, ishanlder, scope);
			sql += " order by createtime desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			return jdbcTemplate.query(sql, new ExceptionCwbMapper(), beginemaildate, endemaildate);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<ExceptionCwb> getAllECByExcel(long page, String cwb, long scantype, String errortype, String branchid, long userid, long ishanlder, String beginemaildate, String endemaildate, long scope) {
		try {
			String sql = "select * from express_ops_exception_cwb where createtime >? and createtime <?";
			sql = this.getECByWhereSql(sql, cwb, scantype, errortype, branchid, userid, ishanlder, scope);
			sql += " order by createtime desc limit " + (page - 1) * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
			return jdbcTemplate.query(sql, new ExceptionCwbMapper(), beginemaildate, endemaildate);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public long getAllECCount(String cwb, long scantype, String errortype, String branchid, String userid, long ishanlder, String beginemaildate, String endemaildate, long scope) {
		String sql = "select count(1)  from express_ops_exception_cwb where createtime >? and createtime <?";
		sql = this.getECByWhereSqlNew1(sql, cwb, scantype, errortype, branchid, userid, ishanlder, scope);
		return jdbcTemplate.queryForLong(sql, beginemaildate, endemaildate);
	}

	public void createExceptionCwb(String cwb, long scantype, String errortype, long branchid, long userid, long customerid, long driverid, long truckid, long deliverid, String interfacetype) {
		String sql = "insert into express_ops_exception_cwb (cwb,scantype,errortype,branchid,userid,customerid,driverid,truckid,deliverid,interfacetype,scancwb) " + "values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, cwb, scantype, errortype, branchid, userid, customerid, driverid, truckid, deliverid, interfacetype,cwb);
	}
	
	public void createExceptionCwbScan(String cwb, long scantype, String errortype, long branchid, long userid, long customerid, long driverid, long truckid, long deliverid, String interfacetype,String scancwb) {
		String sql = "insert into express_ops_exception_cwb (cwb,scantype,errortype,branchid,userid,customerid,driverid,truckid,deliverid,interfacetype,scancwb) " + "values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, cwb, scantype, errortype, branchid, userid, customerid, driverid, truckid, deliverid, interfacetype,scancwb);
	}

	public long createAutoExceptionMsg(final String msg,final int interfacetype){
		final String sql = "insert into express_auto_exception (msg,interfacetype,createtime) values(?,?,CURRENT_TIMESTAMP)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {
			
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, msg);
				ps.setInt(2, interfacetype);
		
				return ps;
			}
			
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public long createAutoExceptionDetail(final String cwb, final String transportno, final String errinfo, final int status,final long msgid,final long refid,final String operatetype) {
		final String sql = "insert into express_auto_exception_detail (cwb,transportno,errinfo,status,msgid,refid,createtime,operatetype) " + "values(?,?,?,?,?,?,CURRENT_TIMESTAMP,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {
			
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, cwb);
				ps.setString(2, transportno);
				ps.setString(3, errinfo);
				ps.setInt(4, status);
				ps.setLong(5, msgid);
				ps.setLong(6, refid);
				ps.setString(7, operatetype);
				
				return ps;
			}
			
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	public long updateAutoExceptionDetail(long id,int status,String errorInfo,long msgid,String msg){
		String sql = "update express_auto_exception_detail set status=?,errinfo=?,createtime=CURRENT_TIMESTAMP where id=?";
		jdbcTemplate.update(sql, status,errorInfo,id);
		
		String sql2 = "update express_auto_exception set msg=?,createtime=CURRENT_TIMESTAMP where id=?";
		return jdbcTemplate.update(sql2, msg,msgid);
	}
	
	public long updateAutoExceptionMsg(long id,String msg){
		String sql = "update express_auto_exception_detail set msg=?,createtime=CURRENT_TIMESTAMP where id=?";
		return jdbcTemplate.update(sql, msg,id);
	}
	
	public List<Map<String,Object>> queryAutoExceptionDetail(String cwb,String transportno,String operateType){
		String sql = "select id,msgid from express_auto_exception_detail  where cwb=? and transportno=? and operatetype=?";
		return jdbcTemplate.queryForList(sql, cwb,transportno,operateType);
	}
	
	public void updateExceptionStatus(String cwb,String transportno,int status){
		String sql = "update express_auto_exception_detail set status=? where cwb=? and transportno=?";
		jdbcTemplate.update(sql, status,cwb,transportno);
	}
	
	public String getECByWhereSql(String sql, String cwb, long scantype, String errortype, String branchid, long userid, long ishanlder, long scope) {
		if (cwb.length() > 0 || scantype > 0 || errortype.length() > 0 || branchid.length() > 0 || userid > 0 || ishanlder > 0) {
			StringBuffer w = new StringBuffer();
			if (cwb.length() > 0) {
				w.append(" and cwb='" + cwb + "'");
			}
			if (scantype > 0) {
				w.append(" and scantype=" + scantype);
			}
			if (errortype.length() > 0) {
				w.append(" and errortype='" + errortype + "'");
			}
			if (branchid.length() > 0) {
				w.append(" and branchid in(" + branchid+")");
			}
			if (userid > 0) {
				w.append(" and userid=" + userid);
			}
			if (ishanlder > -1) {
				w.append(" and ishanlder=" + ishanlder);
				sql += w.toString();
			}
		}
		if (scope == 1) {
			sql += " and errortype='' or errortype=null ";
		}
		if (scope == 2) {
			sql += " and errortype!='' or errortype!=null ";
		}
		return sql;
	}
	

	public String getECByWhereSqlNew1(String sql, String cwb, long scantype, String errortype, String branchid, String userid, long ishanlder, long scope) {
		if (cwb.length() > 0 || scantype > 0 || errortype.length() > 0 || !branchid .equals("") ||! userid .equals("") || ishanlder > 0) {
			StringBuffer w = new StringBuffer();
			if (cwb.length() > 0) {
				w.append(" and cwb='" + cwb + "'");
			}
			if (scantype > 0) {
				w.append(" and scantype=" + scantype);
			}
			if (errortype.length() > 0) {
				w.append(" and errortype='" + errortype + "'");
			}
			if (!branchid .equals("")) {
				w.append(" and branchid in ("+branchid+")");
			}
			if (!userid .equals("")) {
				w.append(" and userid in ("+userid+")");
			}
			if (ishanlder > -1) {
				w.append(" and ishanlder=" + ishanlder);
				sql += w.toString();
			}
		}
		if (scope == 1) {
			sql += " and errortype='' or errortype=null ";
		}
		if (scope == 2) {
			sql += " and errortype!='' or errortype!=null ";
		}
		return sql;
	}
	public String getECByWhereSqlNew(String sql, String cwb, long scantype, String errortype, String branchid, String userid, long ishanlder, long scope) {
		if (cwb.length() > 0 || scantype > 0 || errortype.length() > 0 || !branchid.equals("") || !userid.equals("") || ishanlder > 0) {
			StringBuffer w = new StringBuffer();
			if (cwb.length() > 0) {
				w.append(" and cwb='" + cwb + "'");
			}
			if (scantype > 0) {
				w.append(" and scantype=" + scantype);
			}
			if (errortype.length() > 0) {
				w.append(" and errortype='" + errortype + "'");
			}
			if (!branchid.equals("")) {
				w.append(" and branchid in ("+ branchid+")");
			}
			if (!userid .equals("")) {
				w.append(" and userid in ("+ userid+")");
			}
			if (ishanlder > -1) {
				w.append(" and ishanlder=" + ishanlder);
				sql += w.toString();
			}
		}
		if (scope == 1) {
			sql += " and errortype='' or errortype=null ";
		}
		if (scope == 2) {
			sql += " and errortype!='' or errortype!=null ";
		}
		return sql;
	}

	public void saveExceptionCwbByCwb(long id, long ishanlder) {
		String sql = "update express_ops_exception_cwb set ishanlder=? where id=?";
		jdbcTemplate.update(sql, ishanlder, id);
	}

	public void editCwbofException(CwbOrder cwb, int flowOrderTypeEnum, User user, String remark) {

		String sql = "INSERT INTO express_ops_exception_cwb (cwb, scantype, createtime, branchid, userid,remark,scancwb) VALUES('" + cwb.getCwb() + "','" + flowOrderTypeEnum + "','"
				+ DateTimeUtil.getNowTime() + "','" + user.getBranchid() + "','" + user.getUserid() + "','" + remark + "','" + cwb.getCwb() + "')";
		jdbcTemplate.update(sql);
	}
}
