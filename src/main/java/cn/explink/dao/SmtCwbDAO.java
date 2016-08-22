package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SmtCwb;

/**
 * 上门退信息-DAO
 * @author chunlei05.li
 * @date 2016年8月22日 下午3:55:07
 */
@Component
public class SmtCwbDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class SmtCwbMapper implements RowMapper<SmtCwb> {
		@Override
		public SmtCwb mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmtCwb SmtCwb = new SmtCwb();
			SmtCwb.setId(rs.getLong("id"));
			SmtCwb.setCwb(rs.getString("cwb"));
			SmtCwb.setReturnNo(rs.getString("return_no"));
			SmtCwb.setReturnAddress(rs.getString("return_address"));
			return SmtCwb;
		}
	}
	
	/**
	 * 保存
	 * @author chunlei05.li
	 * @date 2016年8月22日 下午4:14:37
	 * @param SmtCwb
	 * @return
	 */
	public int saveSmtCwb(final SmtCwb SmtCwb) {
		String sql = "INSERT INTO express_ops_smt_cwb (cwb, return_no, return_address) VALUES (?, ?, ?)";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, SmtCwb.getCwb());
				ps.setString(2, SmtCwb.getReturnNo());
				ps.setString(3, SmtCwb.getReturnAddress());
			}
		});
	}
	
	/**
	 * 根据订单号查询
	 * @author chunlei05.li
	 * @date 2016年8月22日 下午4:18:37
	 * @param cwb
	 * @return
	 */
	public SmtCwb getSmtCwbByCwb(String cwb) {
		String sql = "select * from express_ops_smt_cwb where cwb = ?";
		List<SmtCwb> list = this.jdbcTemplate.query(sql, new SmtCwbMapper(), cwb);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
