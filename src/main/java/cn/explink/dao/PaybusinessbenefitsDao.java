package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Paybusinessbenefits;
import cn.explink.util.Page;

@Component
public class PaybusinessbenefitsDao {
	private final class PaybusinessbenefitsRowMapper implements RowMapper<Paybusinessbenefits>{

		@Override
		public Paybusinessbenefits mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Paybusinessbenefits paybusinessbenefits=new Paybusinessbenefits();
			paybusinessbenefits.setId(rs.getLong("id"));
			paybusinessbenefits.setCustomerid(rs.getLong("customerid"));
			paybusinessbenefits.setPaybusinessbenefits(rs.getString("paybusinessbenefits"));
			paybusinessbenefits.setOthersubsidies(rs.getBigDecimal("othersubsidies"));
			paybusinessbenefits.setRemark(rs.getString("remark"));
			return paybusinessbenefits;
		}
	}
	@Autowired 
	JdbcTemplate jdbcTemplate;
	/**
	 * 通过客户查询相关工资业务补助设置信息
	 * @param customerids
	 * @return
	 */
	public List<Paybusinessbenefits> getPaybusinessbenefitsbyCustomerName(long page,String customerids,long rows){
		String sql="select * from express_ops_paybusiness_benefits where 1=1";
		if (customerids.length()>0) {
			sql+=" and customerid IN("+customerids+")";
		}
		if (page!=-9) {
			sql+=" limit  "+(page-1)*rows+","+rows;
		}
		try {
			return this.jdbcTemplate.query(sql, new PaybusinessbenefitsRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return  new ArrayList<Paybusinessbenefits>();
		}
	}
	/**
	 * add
	 * @param paybusinessbenefits
	 */
	public void insertIntoPaybusinessbenefits(final Paybusinessbenefits paybusinessbenefits){
		String sql="insert into express_ops_paybusiness_benefits (`customerid`,`paybusinessbenefits`,`othersubsidies`) values(?,?,?)";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, paybusinessbenefits.getCustomerid());
				ps.setString(2, paybusinessbenefits.getPaybusinessbenefits());
				ps.setBigDecimal(3, paybusinessbenefits.getOthersubsidies());
			}
		});
	}
	/**
	 * deletebatch
	 * @param ids
	 * @return
	 */
	public int cutData(String ids){
		String sql="delete from express_ops_paybusiness_benefits where id IN("+ids+")";
		try {
			return this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}
	/**
	 * 根据customerid查询是否供货商已经创建
	 * @param customerid
	 * @return
	 */
	public int getPaybusinessbenefitsByCustomerid(long customerid){
		String sql="select count(1) from express_ops_paybusiness_benefits where customerid=?";
		return this.jdbcTemplate.queryForInt(sql, customerid);
		
	}
}
