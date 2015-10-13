package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.GroupDetail;

@Component
public class GroupDetailDao {
	private final class GroupDetailMapper implements RowMapper<GroupDetail> {
		@Override
		public GroupDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupDetail groupDetail = new GroupDetail();
			groupDetail.setCwb(rs.getString("cwb"));
			groupDetail.setGroupid(rs.getLong("groupid"));
			return groupDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creGroupDetail(String cwb, long groupid) {
		String sql = "insert into express_ops_groupdetail(cwb,groupid) values(?,?)";
		jdbcTemplate.update(sql, cwb, groupid);
	}

	public List<GroupDetail> getAllGroupDetail(long groupid) {
		String sql = "select * from express_ops_groupdetail where groupid=" + groupid;
		return jdbcTemplate.query(sql, new GroupDetailMapper());
	}
}
