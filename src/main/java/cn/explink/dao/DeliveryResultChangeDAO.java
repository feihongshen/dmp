package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.DeliveryResultChange;

@Component
public class DeliveryResultChangeDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class DeliveryResultChangeMapper implements RowMapper<DeliveryResultChange> {
		@Override
		public DeliveryResultChange mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryResultChange deliveryResultChange = new DeliveryResultChange();
			deliveryResultChange.setId(rs.getLong("id"));
			deliveryResultChange.setCash(rs.getBigDecimal("cash"));
			deliveryResultChange.setCheck(rs.getBigDecimal("check"));
			deliveryResultChange.setPos(rs.getBigDecimal("pos"));
			deliveryResultChange.setOther(rs.getBigDecimal("other"));
			deliveryResultChange.setNum(rs.getInt("num"));
			deliveryResultChange.setUserid(rs.getLong("uesrid"));
			deliveryResultChange.setCreateDate(rs.getDate("createDate"));
			return deliveryResultChange;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void create(DeliveryResultChange deliveryResultChange) {
		String sql = "insert into express_ops_delivery_result_change(`id`,`cash`,`check`,`pos`,`other`,`num`,`uesrid`) values(?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, deliveryResultChange.getId(), deliveryResultChange.getCash(), deliveryResultChange.getCheck(), deliveryResultChange.getPos(), deliveryResultChange.getOther(),
				deliveryResultChange.getNum(), deliveryResultChange.getUserid());
	}
}
