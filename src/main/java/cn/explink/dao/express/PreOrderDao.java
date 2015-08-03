package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.express.ExpressPreOrder;

/**
 * 预订单处理Dao
 * @author wangzy
 */


@Component
public class PreOrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class ExpressPreOrderRowMapper implements RowMapper<ExpressPreOrder> {
		@Override
		public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrder expressPreOrder = new ExpressPreOrder();
			//TODO
			return expressPreOrder;
		}

	}
	
	
	/**
	 * 根据预订单号查询记录
	 * @param preOrderNo
	 * @return
	 */
	public ExpressPreOrder getActivePreOrderByOrderNo(String preOrderNo) {
		try {
			//后期用枚举
			String sql = "select * from express_ops_preorder where pre_order_no=? and excute_state =2 and status=0 order by id desc limit 0,1 ";
			return this.jdbcTemplate.queryForObject(sql, new ExpressPreOrderRowMapper(), preOrderNo);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}
}
