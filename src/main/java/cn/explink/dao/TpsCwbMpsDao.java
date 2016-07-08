package cn.explink.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TpsCwbMpsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String CWB_MPS_SQL_SAVE=
			"insert into express_ops_tps_flow_tmp_mps (cwb,transcwb) values(?,?)";
	
	
	private final static String CWB_MPS_SQL_LIST=
			"select transcwb from express_ops_tps_flow_tmp_mps where cwb=?";
	
	private final static String CWB_MPS_SQL_DEL=
			"delete from express_ops_tps_flow_tmp_mps where cwb=?";
	
	
	public void save(String cwb,String transcwb){
		this.jdbcTemplate.update(CWB_MPS_SQL_SAVE,cwb,transcwb);
	}
	
	public List<String> findTranscwbList(String cwb){
		return this.jdbcTemplate.queryForList(CWB_MPS_SQL_LIST, String.class, cwb);
	}
	
	public void delete(String cwb){
		this.jdbcTemplate.update(CWB_MPS_SQL_DEL,cwb);
	}
}
