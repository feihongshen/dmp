package cn.explink.b2c.cwbsearch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class B2cDatasearchDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class powerMapper implements RowMapper<B2cDatasearch> {
		@Override
		public B2cDatasearch mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2cDatasearch en = new B2cDatasearch();
			en.setB2cid(rs.getLong("b2cid"));
			en.setCretime(rs.getString("cretime"));
			en.setCwb(rs.getString("cwb"));
			en.setFlowordertype(rs.getInt("flowordertype"));
			en.setOperatorname(rs.getString("operatorname"));
			en.setRemark(rs.getString("remark"));
			en.setContent(rs.getString("content"));
			en.setCustomerid(rs.getInt("customerid"));
			en.setDeliverystate(rs.getLong("deliverystate"));
			en.setSignname(rs.getString("signname"));
			en.setMobilephone(rs.getString("mobilephone"));
			en.setB2cid(rs.getInt("b2cid"));
			en.setNowtime(rs.getString("nowtime"));
			return en;
		}
	}

	public long getDataCountByKey(long customerid, String starttime, String endtime) {
		String sql = "select count(1) from express_b2cdata_search where state=1 and  customerid=? and nowtime between ? and ? ";
		return jdbcTemplate.queryForLong(sql, customerid, starttime, endtime);
	}

	public List<B2cDatasearch> getDataListByKey(long customerid, String starttime, String endtime) {
		String sql = "select * from express_b2cdata_search where  state=1 and  customerid=? and nowtime between ? and ? order by cwb,nowtime ";
		return jdbcTemplate.query(sql, new powerMapper(), customerid, starttime, endtime);
	}

	public B2cDatasearch getDataByKeys(String cwb, long flowordertype) {
		String sql = "select * from express_b2cdata_search where cwb=? and flowordertype=? and state=1 limit 1 ";

		try {
			return jdbcTemplate.queryForObject(sql, new powerMapper(), cwb, flowordertype);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public void save(String cwb, long customerid, String cretime, long flowordertype, long deliverystate, String desc, String operator, String signname, String mobilephone, String remark,
			String nowtime) {
		String sql = "insert into express_b2cdata_search (cwb,customerid,cretime,flowordertype,deliverystate,content,operatorname,signname,mobilephone,remark,nowtime) values(?,?,?,?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, cwb, customerid, cretime, flowordertype, deliverystate, desc, operator, signname, mobilephone, remark, nowtime);
	}

	public void updateState(long b2cid, int state) {
		String sql = "update express_b2cdata_search set state=? where b2cid=? ";
		jdbcTemplate.update(sql, state, b2cid);
	}

}
