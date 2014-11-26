package cn.explink.b2c.tools.power;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JointPowerDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class powerMapper implements RowMapper<JointPower> {
		@Override
		public JointPower mapRow(ResultSet rs, int rowNum) throws SQLException {
			JointPower en = new JointPower();
			en.setId(rs.getInt("id"));
			en.setJoint_num(rs.getLong("joint_num"));
			en.setRemark(rs.getString("remark"));
			return en;
		}
	}

	public List<JointPower> getJointPowerList(int type) {
		String sql = "select * from express_set_jointpower where type=" + type + " ";
		return jdbcTemplate.query(sql, new powerMapper());
	}

	public void delete(int type) {
		String sql = "delete from  express_set_jointpower where type=" + type + " ";
		jdbcTemplate.update(sql);
	}

	public void update(int type, String joint_nums) {
		String sql = "insert into express_set_jointpower(joint_num,type) values (?,?) ";
		jdbcTemplate.update(sql, joint_nums, type);
	}

}
