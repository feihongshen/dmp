package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Reason;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.Page;

@Component
public class ReasonDao {

	private final class ReasonRowMapper implements RowMapper<Reason> {

		@Override
		public Reason mapRow(ResultSet rs, int rowNum) throws SQLException {
			Reason reason = new Reason();
			reason.setReasonid(rs.getLong("reasonid"));
			reason.setReasoncontent(rs.getString("reasoncontent"));
			reason.setReasontype(rs.getLong("reasontype"));
			reason.setWhichreason(rs.getInt("whichreason"));
			reason.setParentid(rs.getInt("parentid"));
			reason.setChangealowflag(rs.getInt("changealowflag"));
			return reason;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getReasonByPageWhereSql(String sql, long reasontype) {
		if (reasontype > 0) {
			sql += " where reasontype=" + reasontype;
		}
		return sql;
	}

	public List<Reason> getReasonByPage(long page, long reasontype) {
		String sql = "select * from express_set_reason";
		sql = this.getReasonByPageWhereSql(sql, reasontype);
		sql += " order by reasonid desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<Reason> reasonList = jdbcTemplate.query(sql, new ReasonRowMapper());
		return reasonList;
	}

	public long getReasonCount(long reasontype) {
		String sql = "select count(1) from express_set_reason";
		sql = this.getReasonByPageWhereSql(sql, reasontype);
		return jdbcTemplate.queryForInt(sql);
	}

	@Cacheable(value = "reasonCache", key = "#reasonid")
	public Reason getReasonByReasonid(long reasonid) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_reason where reasonid=?", new ReasonRowMapper(), reasonid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Reason> getReasonByReasoncontent(String reasoncontent) {
		try {
			return jdbcTemplate.query("select * from express_set_reason where reasoncontent=?", new ReasonRowMapper(), reasoncontent);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Reason> getReasonByReasoncontentAndParentid(String reasoncontent,int parentid) {
		try {
			return jdbcTemplate.query("select * from express_set_reason where reasoncontent=? and parentid=?", new ReasonRowMapper(), reasoncontent,parentid);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Reason> getReasonByReasontype(long reasontype) {
		if(reasontype==2){
			try {
				return jdbcTemplate.query("select * from express_set_reason where reasontype=? and whichreason=1", new ReasonRowMapper(), reasontype);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@CacheEvict(value = "reasonCache", key = "#reason.reasonid")
	public void saveReason(final Reason reason) {
			
			jdbcTemplate.update("update express_set_reason set reasoncontent=?,changealowflag=? where reasonid=?", new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, reason.getReasoncontent());
					ps.setInt(2, reason.getChangealowflag());
					ps.setLong(3, reason.getReasonid());
					
				}
			});
		
	}

	public void creReason(final Reason reason) {

		jdbcTemplate.update("insert into express_set_reason(reasoncontent,reasontype,whichreason,parentid,changealowflag) values(?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, reason.getReasoncontent());
				ps.setLong(2, reason.getReasontype());
				ps.setInt(3, reason.getWhichreason());
				ps.setInt(4, reason.getParentid());
				ps.setInt(5, reason.getChangealowflag());
			}
		});
	}

	public List<Reason> getAllReasonByReasonType(long reasontype) {
		try {
			String sql = "select * from express_set_reason where reasontype=" + reasontype;
			return jdbcTemplate.query(sql, new ReasonRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Reason> getAllReason() {
		return jdbcTemplate.query("select * from express_set_reason ", new ReasonRowMapper());
	}

	/**
	 * 根据 异常编码id查询 异常类型
	 * 
	 * @return
	 */
	public long getReasonTypeByExptCode(String code) {
		String sql = "SELECT * FROM express_set_reason where reasonid=" + code;
		long reasontype = 0;
		Reason reason = null;
		try {
			reason = jdbcTemplate.queryForObject(sql, new ReasonRowMapper());
			if (reason != null) {
				reasontype = reason.getReasontype();
			}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reasontype;
	}
	//查出所有的一级原因
	public List<Reason> add(){
		String sql = "SELECT * FROM express_set_reason where whichreason=1";
		List<Reason> list = jdbcTemplate.query(sql,  new ReasonRowMapper());
		return list;
	}
	
	//查出所有的一级原因
	public List<Reason> getFirstReasonByType(long reasontype){
		String sql = "SELECT * FROM express_set_reason where whichreason=1 and reasontype=?";
		List<Reason> list = jdbcTemplate.query(sql,  new ReasonRowMapper(),reasontype);
		return list;
	}
		
	//查出所有的二级原因
	public List<Reason> getAllSecondLevelReason(long firstreasonid){
		String sql = "SELECT * FROM express_set_reason where parentid="+firstreasonid;
		List<Reason> list = jdbcTemplate.query(sql,  new ReasonRowMapper());
		return list;
	}
	public List<Reason> getSecondLevelReason(long rid){
		String sql ="";
		List<Reason> lr = null;		
		try {			
				sql = "SELECT * FROM express_set_reason where whichreason=2 and parentid=?";
				lr=jdbcTemplate.query(sql,new ReasonRowMapper(),rid);	
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return lr;
	}
	
	//查出所有的一级原因
	public List<Reason> addWO(){
		int reasonGongdantousu=ReasonTypeEnum.GongDanTouSuYuanYin.getValue();
		String sql = "SELECT * FROM express_set_reason where whichreason=1 and reasontype="+reasonGongdantousu;
		List<Reason> list = jdbcTemplate.query(sql,new ReasonRowMapper());
		return list;
	}
	
	public List<Reason> getAllTwoLevelReason(){
		String sql = "SELECT * FROM express_set_reason where whichreason=2";
		List<Reason> list = jdbcTemplate.query(sql,new ReasonRowMapper());
		return list;
	}
	
}
