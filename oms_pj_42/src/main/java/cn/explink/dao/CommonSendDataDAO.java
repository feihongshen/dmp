package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CommenSendData;

@Component
public class CommonSendDataDAO {

	private final class CommenSendDataRowMapper implements RowMapper<CommenSendData> {
		@Override
		public CommenSendData mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommenSendData common = new CommenSendData();
			common.setId(rs.getLong("id"));
			common.setCwb(rs.getString("cwb"));
			common.setStartbranchid(rs.getLong("startbranchid"));
			common.setCommencode(rs.getString("commoncode"));
			common.setDatajson(rs.getString("datajson"));
			common.setDeliverystate(rs.getInt("deliverystate"));
			common.setFlowordertype(rs.getInt("flowordertype"));
			common.setPosttime(rs.getString("posttime"));
			common.setState(rs.getString("state"));
			common.setStatetime(rs.getString("statetime"));
			common.setReason(rs.getString("reason"));
			common.setCustid(rs.getString("custid"));
			return common;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void creCommenSendData(String cwb, long startbranchid, String commoncode, String posttime, String statetime, String datajson, long deliverystate, long flowordertype, String custid) {
		jdbcTemplate.update("insert into commen_send_data(cwb,startbranchid,commoncode,posttime,statetime,datajson,deliverystate,flowordertype,custid) " + " values(?,?,?,?,?,?,?,?,?)", cwb,
				startbranchid, commoncode, posttime, statetime, datajson, deliverystate, flowordertype, custid);
	}

	public void updateCommenSendData(String cwb, String state_time) {
		jdbcTemplate.update("update commen_send_data set state=? where cwb=? ", state_time, cwb);
	}

	public void updateCommenSendDataById(long id, String state_time, String reason) {
		jdbcTemplate.update("update commen_send_data set state=?,reason=?,loopcount=loopcount+1 where id=? ", state_time, reason, id);
	}

	public List<CommenSendData> getCommenCwbListBycommoncode(String commoncode, long pagesize, long loopcount) {
		return jdbcTemplate.query("select * from  commen_send_data  where commoncode=?  and state in ('',2) and loopcount<" + loopcount + "   order by cwb,posttime,flowordertype limit 0," + pagesize,
				new CommenSendDataRowMapper(), commoncode);
	}

	public CommenSendData getCommenCwb(String cwb, int flowordertype) {
		try {
			return jdbcTemplate.queryForObject("select * from  commen_send_data  where cwb=? and flowordertype=? limit 1", new CommenSendDataRowMapper(), cwb, flowordertype);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public long isExistsCwbFlag(String cwb, String commoncode, String statetime, String flowordertype) {
		return jdbcTemplate.queryForLong("select count(1) from commen_send_data where cwb=? and commoncode=? and statetime=? and flowordertype=? ", cwb, commoncode, statetime, flowordertype);
	}
	public long isExistsCwbFlag1(String cwb, String commoncode, String statetime, String flowordertype,long deliverystate) {
		return jdbcTemplate.queryForLong("select count(1) from commen_send_data where cwb=? and commoncode=? and statetime=? and flowordertype=?  and deliverystate=?", cwb, commoncode, statetime, flowordertype,deliverystate);
	}

}
