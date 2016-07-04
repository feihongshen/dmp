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
			vo.setState(rs.getInt("status"));
			vo.setTrytime(rs.getInt("trytime"));
			vo.setCreatetime(rs.getTimestamp("createtime"));
			vo.setSendemaildate(rs.getInt("sendemaildate"));
			vo.setSendweight(rs.getInt("sendweight"));
			vo.setWeight(rs.getBigDecimal("weight"));
			vo.setVolume(rs.getBigDecimal("volume"));
			return vo;
		}
	}
	
	private final static String TPS_FLOW_SQL_SAVE=
			"insert into express_ops_tps_flow_tmp (cwb,scancwb,flowordertype,errinfo,createtime,status,trytime,sendemaildate,sendweight,weight,volume)"
			+" values(?,?,?,?,CURRENT_TIMESTAMP,?,0,?,?,?,?)";
	
	private final static String TPS_FLOW_SQL_SAVE_SENT=
			"insert into express_ops_tps_flow_tmp_sent (cwb,scancwb,flowordertype,errinfo,createtime,status,trytime,sendemaildate,sendweight,weight,volume)"
			+" values(?,?,?,?,?,?,?,?,?,?,?)";
	
	
	private final static String TPS_FLOW_SQL_LIST=
			"select * from express_ops_tps_flow_tmp where trytime<? order by createtime limit ?";
	
	private final static String TPS_FLOW_SQL_UPDATE=
			"update express_ops_tps_flow_tmp set status=?,errinfo=?,trytime=trytime+1 where cwb=? and scancwb=? and flowordertype=?";
	
	private final static String TPS_FLOW_SQL_HOUSEKEEP=
			"delete from express_ops_tps_flow_tmp where createtime<DATE_SUB(NOW(),INTERVAL ? DAY)";
	
	private final static String TPS_FLOW_SQL_HOUSEKEEP_SENT=
			"delete from express_ops_tps_flow_tmp_sent where createtime<DATE_SUB(NOW(),INTERVAL ? DAY)";
	
	private final static String TPS_FLOW_SQL_DELETE=
			"delete from express_ops_tps_flow_tmp where cwb=? and scancwb=? and flowordertype=?";
	
	private final static String TPS_FLOW_SQL_EXIST=
			"select cwb from express_ops_tps_flow_tmp where cwb=? and scancwb=? limit 1";
	
	private final static String TPS_FLOW_SQL_EXIST_SENT=
			"select cwb from express_ops_tps_flow_tmp_sent where cwb=? and scancwb=? limit 1";
	
	private final static String TPS_FLOW_SQL_CWB_EXIST=
			"select cwb from express_ops_tps_flow_tmp where cwb=? limit 1";
	
	private final static String TPS_FLOW_SQL_CWB_EXIST_SENT=
			"select cwb from express_ops_tps_flow_tmp_sent where cwb=? limit 1";
	
	private final static String TPS_FLOW_SQL_SENDEMAILDATE_EXIST=
			"select cwb from express_ops_tps_flow_tmp where cwb=? and sendemaildate=1 limit 1";
	
	private final static String TPS_FLOW_SQL_SENDEMAILDATE_EXIST_SENT=
			"select cwb from express_ops_tps_flow_tmp_sent where cwb=? and sendemaildate=1 limit 1";
	
	public void save(TpsCwbFlowVo vo){
		this.jdbcTemplate.update(TPS_FLOW_SQL_SAVE, vo.getCwb(),vo.getScancwb(),vo.getFlowordertype(),vo.getErrinfo(),vo.getState(),vo.getSendemaildate(),vo.getSendweight(),vo.getWeight(),vo.getVolume());
	}
	
	public void saveSent(TpsCwbFlowVo vo){
		this.jdbcTemplate.update(TPS_FLOW_SQL_SAVE_SENT, vo.getCwb(),vo.getScancwb(),vo.getFlowordertype(),vo.getErrinfo(),vo.getCreatetime(),vo.getState(),vo.getTrytime(),vo.getSendemaildate(),vo.getSendweight(),vo.getWeight(),vo.getVolume());
	}

	public List<TpsCwbFlowVo> list(int size,int trytime){
		return this.jdbcTemplate.query(TPS_FLOW_SQL_LIST,new TpsCwbFlowVoMapper(),trytime,size);
	}

	public void update(TpsCwbFlowVo vo){
		this.jdbcTemplate.update(TPS_FLOW_SQL_UPDATE,vo.getState(),vo.getErrinfo(),vo.getCwb(),vo.getScancwb(),vo.getFlowordertype());
	}

	public int delete(int day){
		return jdbcTemplate.update(TPS_FLOW_SQL_HOUSEKEEP,day);
	}
	
	public int deleteSent(int day){
		return jdbcTemplate.update(TPS_FLOW_SQL_HOUSEKEEP_SENT,day);
	}
	
	public int delete(TpsCwbFlowVo vo){
		return jdbcTemplate.update(TPS_FLOW_SQL_DELETE,vo.getCwb(),vo.getScancwb(),vo.getFlowordertype());
	}
	
	public boolean  checkScancwbExist(String cwb,String scancwb){
		List<String> list= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_EXIST,String.class,cwb,scancwb);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			List<String> sentlist= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_EXIST_SENT,String.class,cwb,scancwb);
			if(sentlist!=null&&sentlist.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean  checkCwbExist(String cwb){
		List<String> list= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_CWB_EXIST,String.class,cwb);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			List<String> sentlist= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_CWB_EXIST_SENT,String.class,cwb);
			if(sentlist!=null&&sentlist.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean  checkSendemildateExist(String cwb){
		List<String> list= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_SENDEMAILDATE_EXIST,String.class,cwb);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			List<String> sentlist= this.jdbcTemplate.queryForList(TPS_FLOW_SQL_SENDEMAILDATE_EXIST_SENT,String.class,cwb);
			if(sentlist!=null&&sentlist.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
}
