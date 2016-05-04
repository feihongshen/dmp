package cn.explink.b2c.ems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EMSDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class EMSFlowMapper implements RowMapper<EMSFlowEntity> {
		@Override
		public EMSFlowEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			EMSFlowEntity en = new EMSFlowEntity();
			en.setTranscwb(rs.getString("transcwb"));
			en.setEmailnum(rs.getString("email_num"));
			en.setFlowContent(rs.getString("flow_content"));
			en.setState(rs.getInt("state"));
			en.setEmsFlowordertype(rs.getLong("emsFlowordertype"));
			en.setAction(rs.getString("action"));
			en.setProperdelivery(rs.getString("properdelivery"));
			en.setNotproperdelivery(rs.getString("notproperdelivery"));
			return en; 
		}
	}
	
    //保存ems运单轨迹报文信息
	public void saveEMSFlowInfo(final String transcwb, final String mailnum,
			final String listexpressmail,final String action,final long emsFlowordertype,
			final String properdelivery,final String notproperdelivery) {
		this.jdbcTemplate
		.update("insert into express_ems_flow_info(transcwb,email_num,flow_content,action,action,emsFlowordertype,"
				+ "properdelivery,notproperdelivery) values(?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, transcwb);
				ps.setString(2, mailnum);
				ps.setString(3, listexpressmail);
				ps.setString(4, action);
				ps.setLong(5, emsFlowordertype);
				ps.setString(6, properdelivery);
				ps.setString(7, notproperdelivery);
			}
		});
	}

	//根据ems运单号获取dmp运单号
	public String getTranscwbByEmailNo(String mailnum) {
		String sql = "select transcwb from express_ems_dmp_transcwb where email_num=? limit 0,1";
		List<Map<String, Object>> mailnumObj = this.jdbcTemplate.queryForList(sql,mailnum);
		String transcwb = "";
		if ((mailnumObj != null) && (mailnumObj.size() > 0)) {
			Map<String, Object> map = mailnumObj.get(0);
			transcwb =  map.get("transcwb")+"";
		}
		return transcwb;
	}

	//根据ems运单号获取dmp运单号
	public List<EMSFlowEntity> getEMSFlowEntityList() {
		String sql = "select * from express_ems_flow_info where state<>1 order by emsFlowordertype asc";
		List<EMSFlowEntity> emsFlowEntityList = this.jdbcTemplate.query(sql, new EMSFlowMapper());
		return emsFlowEntityList;
	}

	//根据ems轨迹模拟dmp操作的处理结果，修改处理状态
	public void changeEmsTraceDataState(long id,int state,String remark) {
		String sql = "update express_ems_flow_info set state="+state+",remark="+remark+" where id=?";
		this.jdbcTemplate.update(sql,id);
	}

}
