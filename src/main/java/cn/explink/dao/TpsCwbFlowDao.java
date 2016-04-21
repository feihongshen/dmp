package cn.explink.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Component;

import cn.explink.domain.TpsCwbFlowVo;

@Component
public class TpsCwbFlowDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class TpsCwbFlowVoMapper implements RowMapper<TpsCwbFlowVo> {
		@Override
		public TpsCwbFlowVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TpsCwbFlowVo vo = new TpsCwbFlowVo();
			vo.setCwb(rs.getString("cwb"));
			vo.setErrinfo(rs.getString("errinfo"));
			vo.setFlowordertype(rs.getLong("flowordertype"));
			vo.setScancwb(rs.getString("scancwb"));
			
			return vo;
		}
	}
	
	private final static String TPS_FLOW_SQL_SAVE=
			"insert into express_ops_tps_flow_tmp (cwb,scancwb,flowordertype,errinfo,createtime,status,trytime)"
			+" values(?,?,?,?,CURRENT_TIMESTAMP,?,0)";
	
	private final static String TPS_FLOW_SQL_LIST=
			"select * from express_ops_tps_flow_tmp where status in (0,2) and trytime<? and flowordertype=? order by createtime limit ?";
	
	private final static String TPS_FLOW_SQL_UPDATE=
			"update express_ops_tps_flow_tmp set status=?,errinfo=?,trytime=trytime+1 where cwb=? and scancwb=? and flowordertype=?";
	
	private final static String TPS_FLOW_SQL_HOUSEKEEP=
			"delete from express_ops_tps_flow_tmp where createtime<DATE_SUB(NOW(),INTERVAL ? DAY) or status=1";
	
	private final static String TPS_FLOW_SQL_DELETE=
			"delete from express_ops_tps_flow_tmp where cwb=? and scancwb=? and flowordertype=?";
	
	public void save(TpsCwbFlowVo vo){
		this.jdbcTemplate.update(TPS_FLOW_SQL_SAVE, vo.getCwb(),vo.getScancwb(),vo.getFlowordertype(),vo.getErrinfo(),vo.getState());
	}

	public List<TpsCwbFlowVo> list(int size,int trytime,int flowordertype){
		return this.jdbcTemplate.query(TPS_FLOW_SQL_LIST,new TpsCwbFlowVoMapper(),trytime,flowordertype,size);
	}

	public void update(TpsCwbFlowVo vo){
		this.jdbcTemplate.update(TPS_FLOW_SQL_UPDATE,vo.getState(),vo.getErrinfo(),vo.getCwb(),vo.getScancwb(),vo.getFlowordertype());
	}

	public int delete(int day){
		return jdbcTemplate.update(TPS_FLOW_SQL_HOUSEKEEP,day);
	}
	
	public int delete(TpsCwbFlowVo vo){
		return jdbcTemplate.update(TPS_FLOW_SQL_DELETE,vo.getCwb(),vo.getScancwb(),vo.getFlowordertype());
	}
}
