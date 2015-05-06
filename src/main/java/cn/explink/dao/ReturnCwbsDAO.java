package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.ReturnCwbs;

@Component
public class ReturnCwbsDAO {
	private final class ReturnCwbsMapper implements RowMapper<ReturnCwbs> {
		@Override
		public ReturnCwbs mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReturnCwbs returnCwbs = new ReturnCwbs();
			returnCwbs.setId(rs.getLong("id"));
			returnCwbs.setBranchid(rs.getLong("branchid"));
			returnCwbs.setCreatetime(rs.getString("createtime"));
			returnCwbs.setCustomerid(rs.getLong("customerid"));
			returnCwbs.setCwb(rs.getString("cwb"));
			returnCwbs.setOpscwbid(rs.getLong("opscwbid"));
			returnCwbs.setType(rs.getLong("type"));
			returnCwbs.setUserid(rs.getLong("userid"));
			returnCwbs.setTobranchid(rs.getLong("tobranchid"));
			returnCwbs.setIsnow(rs.getString("isnow"));

			return returnCwbs;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createReturnCwbs(final ReturnCwbs returnCwbs) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_returncwbs(opscwbid,cwb,customerid,userid,branchid,createtime,type,tobranchid,isnow) values(?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, returnCwbs.getOpscwbid());
				ps.setString(2, returnCwbs.getCwb());
				ps.setLong(3, returnCwbs.getCustomerid());
				ps.setLong(4, returnCwbs.getUserid());
				ps.setLong(5, returnCwbs.getBranchid());
				ps.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				ps.setLong(7, returnCwbs.getType());
				ps.setLong(8, returnCwbs.getTobranchid());
				ps.setString(9, returnCwbs.getIsnow());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long creAndUpdateReturnCwbs(ReturnCwbs returnCwbs) {
		jdbcTemplate.update("update ops_returncwbs set isnow=? where cwb=? and isnow='0'", System.currentTimeMillis(), returnCwbs.getCwb());

		return createReturnCwbs(returnCwbs);
	}

	public List<ReturnCwbs> getReturnCwbsByCwb(String cwb) {
		String sql = "select * from ops_returncwbs where cwb=? and `type` <> 0";
		return jdbcTemplate.query(sql, new ReturnCwbsMapper(), cwb);
	}

	public List<ReturnCwbs> getReturnCwbsByCwbAndType(String cwb, long type) {
		String sql = "select * from ops_returncwbs where `cwb`=? and `type`=?";
		return jdbcTemplate.query(sql, new ReturnCwbsMapper(), cwb, type);
	}

	public long getReturnCwbsByTypeAndTobranchidCount(long type, long tobranchid, String createtime) {
		String sql = "select count(1) from `ops_returncwbs` where `type`=? and `tobranchid`=? and `createtime`>=? and `isnow`='0'";
		return jdbcTemplate.queryForLong(sql, type, tobranchid,createtime);
	}
	//new method
	public long getReturnCwbsByTypeAndTobranchidOther(long type, long tobranchid, int customerid) {
		String sql = "select count(1) from `ops_returncwbs` where `type`=? and `tobranchid`=? and `customerid`=? and `isnow`='0'";
		return jdbcTemplate.queryForLong(sql, type, tobranchid, customerid);
	}

	public long getReturnCwbsByTypeAndBranchidAndIsnowCount(long type, long branchid, long isnow, String nowtime,long customerid,long timetypewei,
			String starttimewei,String endtimewei) {
		String sql = "select count(1) from `ops_returncwbs` where `type`=? and `branchid`=? and `isnow`=? and createtime>=?";
		if (customerid>0) {
			sql+="  and customerid=?";
			return jdbcTemplate.queryForLong(sql, type, branchid, isnow, nowtime,customerid);
		}
		return jdbcTemplate.queryForLong(sql, type, branchid, isnow, nowtime);
	}

	public List<ReturnCwbs> getReturnCwbsByCwbAll(String cwb) {
		String sql = "select * from ops_returncwbs where cwb=? ";
		return jdbcTemplate.query(sql, new ReturnCwbsMapper(), cwb);
	}

	public void deleteReturnCwbByCwb(String cwb) {
		String sql = "delete from ops_returncwbs where cwb=?";
		jdbcTemplate.update(sql, cwb);
	}

	public ReturnCwbs getReturnCwbsByTypeAndTobranchid(long type, long tobranchid, String cwb) {
		String sql = "select * from ops_returncwbs where type=? and tobranchid=? AND cwb=? and isnow>'0' limit 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new ReturnCwbsMapper(), type, tobranchid, cwb);
		} catch (DataAccessException e) {
			return new ReturnCwbs();
		}
	}
	public ReturnCwbs getReturnCwbsChukuByTypeAndTobranchid(long type, long branchid, String cwb) {
		StringBuffer stringBuffer=new StringBuffer();
		//String sql = "select * from ops_returncwbs where type=? and branchid=? AND cwb=?  limit 0,1";
		stringBuffer.append("select * from ops_returncwbs where type=? and branchid=? AND cwb=?  ");
		if (type==1) {
			stringBuffer.append(" and isnow='0'");
		}else {
			stringBuffer.append(" and isnow>'0'");

		}
		stringBuffer.append(" limit 0,1");
		String sql=stringBuffer.toString();
		try {
			return jdbcTemplate.queryForObject(sql, new ReturnCwbsMapper(), type, branchid, cwb);
		} catch (DataAccessException e) {
			return new ReturnCwbs();
		}
	}

}
