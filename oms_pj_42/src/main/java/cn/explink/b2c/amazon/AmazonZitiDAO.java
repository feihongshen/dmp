package cn.explink.b2c.amazon;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

@Component
public class AmazonZitiDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creAmazonZiti(final String cwb, final long createtime, final String daohuotime, final long customerid, long branchid) {

		jdbcTemplate.update("insert into express_ops_b2c_amazon_ziti(cwb,createtime,daohuotime,customerid,state,branchid) values(?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, cwb);
				ps.setLong(2, createtime);
				ps.setString(3, daohuotime);
				ps.setLong(4, customerid);
				ps.setLong(5, 0);
				ps.setLong(6, 0);
			}
		});

	}

	public void delAmazonZiti(String cwb) {
		jdbcTemplate.update("delete from express_ops_b2c_amazon_ziti where cwb='" + cwb + "'");
	}

	public void updateAmazonZiti(String cwb) {
		jdbcTemplate.update("update express_ops_b2c_amazon_ziti set state=id where cwb='" + cwb + "'");
	}

	public long checkAmazonZitiCount(String cwb) {
		String sql = "select count(1) from express_ops_b2c_amazon_ziti where cwb='" + cwb + "'";
		return jdbcTemplate.queryForLong(sql);

	}

}
