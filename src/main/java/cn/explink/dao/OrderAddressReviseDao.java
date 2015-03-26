package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderAddressRevise;
import cn.explink.util.DateTimeUtil;

@Component
public class OrderAddressReviseDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final class OrderAddressReviseMapper implements RowMapper<OrderAddressRevise> {
		@Override
		public OrderAddressRevise mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderAddressRevise info = new OrderAddressRevise();
			info.setId(rs.getLong("id"));
			info.setAddress(rs.getString("address"));
			info.setCwb(rs.getString("cwb"));
			info.setModifiername(rs.getString("modifiername"));
			info.setRevisetime(rs.getString("revisetime"));
			return info;
		}
	}
	
	//2015年3.25添加
		/**
		 * 查询表里是否有原始地址的值（通过个数可以知道）
		 * 
		 * @param cwb
		 * @return
		 */
		public long countReviseAddress(String cwb) {
			return jdbcTemplate.queryForLong("SELECT count(1) from express_service_revise_address where cwb=?  ", cwb);
		}
		/**
		 * 根据订单号查询订单地址修改详情
		 * @param cwb
		 * @return
		 */
		public List<OrderAddressRevise> getAddressReviseDetails(String cwb){
			try {
				String sql = "SELECT * from express_service_revise_address where cwb=?  ";
				return jdbcTemplate.query(sql, new OrderAddressReviseMapper(), cwb);
			} catch (Exception ee) {
				return null;
			}
		}
		
		
		/**
		 * 插入订单信息修改表
		 * 
		 * @param info
		 * @param editname
		 * @param editmobile
		 * @param editcommand
		 * @param editaddress
		 * @param resendtime
		 * @param userid
		 */
		public void createReviseAddressInfo(String cwb,String address,String revisetime,String modifiername) {
			jdbcTemplate
					.update("insert into express_service_revise_address(cwb,address,revisetime,modifiername) "
							+ "values(?,?,?,?)", cwb,address,revisetime,modifiername);
		}
		
		
		/**
		 * 删除订单修改信息
		 * 
		 * @param cwb
		 * @return
		 */
		public long deleteEditInfo(String cwb) {
			return jdbcTemplate.update("delete from  express_service_revise_address where cwb=?  ", cwb);
		}
		
}
