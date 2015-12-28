package cn.explink.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("orgBillDetailDao")
public class OrgBillDetailDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String,Object> getByid(Long id){
		String sql = "SELECT bill_no,STATUS FROM fn_org_bill WHERE id="+id;
		return this.jdbcTemplate.queryForMap(sql);
	}
}
