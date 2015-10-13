package cn.explink.b2c.wangjiu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WangjiuDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class powerMapper implements RowMapper<WangjiuEntity> {
		@Override
		public WangjiuEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			WangjiuEntity en = new WangjiuEntity();
			en.setId(rs.getLong("id"));
			en.setMailNo(rs.getString("mailNo"));
			en.setOrderStatus(rs.getString("orderStatus"));
			en.setAcceptTime(rs.getTimestamp("acceptTime"));
			en.setAcceptAddress(rs.getString("acceptAddress"));
			en.setStatus(rs.getString("status"));
			en.setName(rs.getString("name"));
			en.setRemark(rs.getString("remark"));
			en.setFlowordertype(rs.getInt("flowordertype"));

			return en;
		}
	}

	public List<WangjiuEntity> getDataByCwb(String cwb) {
		String sql = "select * from express_b2cdata_wangjiu where mailNo=?  order by flowordertype ";

		return jdbcTemplate.query(sql, new powerMapper(), cwb);

	}

	public void save(WangjiuEntity lt) {
		String sql = "insert into express_b2cdata_wangjiu (mailNo,orderStatus,acceptTime,acceptAddress,status,name,remark,flowordertype) values(?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, lt.getMailNo(), lt.getOrderStatus(), lt.getAcceptTime(), lt.getAcceptAddress(), lt.getStatus(), lt.getName(), lt.getRemark(), lt.getFlowordertype());
	}

	// public void update(long flowordertype,long deliverystate,String
	// cretime,String desc,String operator,String cwb){
	// String
	// sql="update express_b2cdata_wangjiu set flowordertype=?,deliverystate=?,cretime=?,content=?,operatorname=? where cwb=? ";
	// jdbcTemplate.update(sql,flowordertype,deliverystate,cretime,desc,operator,cwb);
	// }

}
