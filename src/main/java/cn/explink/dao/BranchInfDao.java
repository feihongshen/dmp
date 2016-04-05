package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.BranchInf;

/**
 * 站点机构接口
 * @author jian.xie
 *
 */
@Component
public class BranchInfDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class BranchInfRowMapper implements RowMapper<BranchInf>{

		@Override
		public BranchInf mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchInf branchInf = new BranchInf();
			branchInf.setInfId(rs.getLong("inf_id"));
			branchInf.setBranchid(rs.getLong("branchid"));
			branchInf.setBranchname(rs.getString("branchname"));
			branchInf.setTpsbranchcode(rs.getString("tpsbranchcode"));
			branchInf.setBranchprovince(rs.getString("branchprovince"));
			branchInf.setBranchcity(rs.getString("branchcity"));
			branchInf.setBrancharea(rs.getString("brancharea"));
			branchInf.setPassword(rs.getString("password"));
			branchInf.setRecBranchid(rs.getLong("rec_branchid"));
			branchInf.setCreateDate(rs.getDate("create_date"));
			branchInf.setCreateUser(rs.getString("create_user"));
			branchInf.setIsSync(rs.getBoolean("is_sync"));
			branchInf.setOperType(rs.getString("oper_type"));
			return branchInf;
		}		
	}	
	
	public List<BranchInf> getBranchInfByIsSync(boolean isSync){
		String sql = " select * from express_set_branch_inf where is_sync = ? order by inf_id";
		List<BranchInf> list = jdbcTemplate.query(sql, new BranchInfRowMapper(), isSync);
		return list;
	}
	
	public long saveBranchInf(final BranchInf branchInf){
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String sql = " insert into express_set_branch_inf (inf_id, branchid,branchname, tpsbranchcode, branchprovince, branchcity,brancharea,password,rec_branchid, create_date,create_user,is_sync,oper_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "inf_id"});
				ps.setLong(1,  branchInf.getInfId());
				ps.setLong(2, branchInf.getBranchid());
				ps.setString(3, branchInf.getBranchname() == null ? "" : branchInf.getBranchname());
				ps.setString(4, branchInf.getTpsbranchcode() == null? "" : branchInf.getTpsbranchcode());
				ps.setString(5, branchInf.getBranchprovince() == null ? "" : branchInf.getBranchprovince());
				ps.setString(6, branchInf.getBranchcity() == null ? "" : branchInf.getBranchcity());
				ps.setString(7, branchInf.getBrancharea() == null ? "" : branchInf.getBrancharea());
				ps.setString(8, branchInf.getPassword() == null ? "" : branchInf.getPassword());
				ps.setLong(9, branchInf.getRecBranchid());
				ps.setTimestamp(10, new Timestamp(branchInf.getCreateDate().getTime()));
				ps.setString(11, branchInf.getCreateUser() == null ? "" : branchInf.getCreateUser());
				ps.setBoolean(12, branchInf.getIsSync());
				ps.setString(13, branchInf.getOperType());
				return ps;
			}
		}, key);
		branchInf.setInfId(key.getKey().longValue());
		return branchInf.getInfId();
	}
	
	/**
	 * 根据主键更新记录为已同步
	 * @param infId
	 * @return
	 */
	public int updateBranchInfForIssync(long infId){
		String sql = " update express_set_branch_inf set is_sync = 1 where inf_id=? ";
		return jdbcTemplate.update(sql, infId);
	}
	
	
}
