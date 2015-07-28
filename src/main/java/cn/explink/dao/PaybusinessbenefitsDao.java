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
			paybusinessbenefits.setKpifee(rs.getBigDecimal("kpifee"));
			paybusinessbenefits.setLower(rs.getString("lower"));
			paybusinessbenefits.setUpper(rs.getString("upper"));
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
		sql+=" GROUP BY customerid";
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
		String sql="insert into express_ops_paybusiness_benefits (`customerid`,`paybusinessbenefits`,`othersubsidies`,`lower`,`upper`,`kpifee`) values(?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, paybusinessbenefits.getCustomerid());
				ps.setString(2, paybusinessbenefits.getPaybusinessbenefits());
				ps.setBigDecimal(3, paybusinessbenefits.getOthersubsidies());
				ps.setString(4, paybusinessbenefits.getLower());
				ps.setString(5, paybusinessbenefits.getUpper());
				ps.setBigDecimal(6, paybusinessbenefits.getKpifee());
			}
		});
	}
	/**
	 * deletebatch(id)
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
	/**
	 * 通过主键id查询
	 * @param id
	 * @return
	 */
	public Paybusinessbenefits getPaybusinessbenefitsByid(long id){
		String sql="select * from express_ops_paybusiness_benefits where id=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PaybusinessbenefitsRowMapper(),id);
		} catch (DataAccessException e) {
			return new Paybusinessbenefits();
		}
	}
	public int updatePaybusinessbenfits(Paybusinessbenefits paybusinessbenefits){
		String sql="update express_ops_paybusiness_benefits set paybusinessbenefits=?,othersubsidies=? where customerid=?";
		try {
			return this.jdbcTemplate.update(sql, paybusinessbenefits.getPaybusinessbenefits(),paybusinessbenefits.getOthersubsidies(),paybusinessbenefits.getCustomerid());
		} catch (DataAccessException e) {
			return 0;
		}
	}
	public Paybusinessbenefits getpbbfBycustomerid(long customerid) {
		String sql = "select * from express_ops_paybusiness_benefits where customerid=?";
		try{
			return this.jdbcTemplate.queryForObject(sql,new PaybusinessbenefitsRowMapper(),customerid);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Paybusinessbenefits> getAllpbbf(){
		String sql = "select * from express_ops_paybusiness_benefits"; 
		return this.jdbcTemplate.query(sql, new PaybusinessbenefitsRowMapper());
	}
	
	
	public int cutDatabyCustomerid(long customerid){
		String sql="delete from express_ops_paybusiness_benefits where customerid=?";
		try {
			return this.jdbcTemplate.update(sql, customerid);
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	public int cutDatabyCustomerids(String customerids){
		String sql="delete from express_ops_paybusiness_benefits where customerid IN("+customerids+")";
		try {
			return this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}
}
