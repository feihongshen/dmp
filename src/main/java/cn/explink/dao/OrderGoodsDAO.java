package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderGoods;

@Component
public class OrderGoodsDAO {

	private final class OrderGoodsRowMapper implements RowMapper<OrderGoods> {
		@Override
		public OrderGoods mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderGoods good = new OrderGoods();
			good.setId(rs.getLong("id"));
			good.setCwb(rs.getString("cwb"));
			good.setCretime(rs.getString("cretime"));
			good.setGoods_code(rs.getString("goods_code"));
			good.setGoods_brand(rs.getString("goods_brand"));
			good.setGoods_name(rs.getString("goods_name"));
			good.setGoods_spec(rs.getString("goods_spec"));
			good.setGoods_num(rs.getString("goods_num"));
			good.setReturn_reason(rs.getString("return_reason"));
			good.setGoods_pic_url(rs.getString("goods_pic_url"));
			good.setShituicount(rs.getInt("shituicount"));
			good.setWeituicount(rs.getInt("weituicount"));
			good.setTepituicount(rs.getInt("tepituicount"));
			good.setWeituireason(rs.getString("weituireason"));
			good.setRemark1(rs.getString("remark1"));
			good.setRemark2(rs.getString("remark2"));
			good.setRemark3(rs.getString("remark3"));
			good.setRemark4(rs.getString("remark4"));
			good.setRemark5(rs.getString("remark5"));
			good.setRemark6(rs.getString("remark6"));
			good.setRemark7(rs.getString("remark7"));
			good.setRemark8(rs.getString("remark8"));
			good.setRemark9(rs.getString("remark9"));
			good.setRemark10(rs.getString("remark10"));
			good.setThzrkcount(rs.getInt("thzrkcount"));

			return good;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<OrderGoods> getOrderGoodsList(String cwb) {
		try {
			return this.jdbcTemplate.query("select * from orders_goods where cwb=? order by goods_num desc", new OrderGoodsRowMapper(), cwb);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public void CreateOrderGoods(final OrderGoods goods) {

		this.jdbcTemplate.update("insert into orders_goods(cwb,cretime,goods_code,goods_brand,goods_name,goods_spec,goods_num,return_reason,goods_pic_url) " + "values(?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, goods.getCwb());
						ps.setString(2, goods.getCretime());
						ps.setString(3, goods.getGoods_code());
						ps.setString(4, goods.getGoods_brand());
						ps.setString(5, goods.getGoods_name());
						ps.setString(6, goods.getGoods_spec());
						ps.setString(7, goods.getGoods_num());
						ps.setString(8, goods.getReturn_reason());
						ps.setString(9, goods.getGoods_pic_url());

					}
				});
	}

	public void updateOrderGoodsById(final OrderGoods goods) {

		this.jdbcTemplate.update("update orders_goods set shituicount = ?, weituicount = ?, tepituicount = ?, weituireason = ?, remark1 = ? where id = ? ", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, goods.getShituicount());
				ps.setLong(2, goods.getWeituicount());
				ps.setLong(3, goods.getTepituicount());
				ps.setString(4, goods.getWeituireason());
				ps.setString(5, goods.getRemark1());
				ps.setLong(6, goods.getId());
			}
		});
	}

	public int updateThzrkcount(long id, long thzrkcount) {
		return this.jdbcTemplate.update("update  orders_goods set thzrkcount=" + thzrkcount + " where id=" + id);
	}
	
	public int loseOrderGoods(String cwb) {
		return this.jdbcTemplate.update("delete from orders_goods  where cwb='"+cwb+"'");
	}
	
}
