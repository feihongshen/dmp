package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.TransCwbStateControl;

@Component
public class TransCwbStateControlDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static class TransStateControlRowMapper implements
			RowMapper<TransCwbStateControl> {

		@Override
		public TransCwbStateControl mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TransCwbStateControl  tsc=new TransCwbStateControl();
			tsc.setTranscwbstate(rs.getInt("transcwbstate"));
			tsc.setToflowtype(rs.getInt("toflowtype"));
			return tsc;
		}

	}

	// list集合
	public List<TransCwbStateControl> getTransStateControlsList(
			int transcwbstate, int toflowtype) {

		return this.jdbcTemplate
				.query("select * from  express_set_transcwb_allstate_control where transcwbstate=? and toflowtype in("
						+ toflowtype + ")", new TransStateControlRowMapper(),
						transcwbstate);

	}

	// 查看一条
	public TransCwbStateControl getStateControlByParam(int transcwbstate,
			int toflowtype) {
		try {
			return this.jdbcTemplate
					.queryForObject(
							"select * from  express_set_transcwb_allstate_control where transcwbstate=? and toflowtype=?",
							new TransStateControlRowMapper(), transcwbstate,
							toflowtype);
		} catch (DataAccessException e) {
			return null;
		}

	}
	
	//新增
	public int createTransStateControl(int transcwbstate,int toflowtype){
		return this.jdbcTemplate.update("insert into express_set_transcwb_allstate_control(transcwbstate,toflowtype)  values(?,?)",transcwbstate,toflowtype);
		
	}
	//运单全部集合
	public List<TransCwbStateControl> getTransStateControlByWhere(int transcwbstate){
		String sql="select * from express_set_transcwb_allstate_control where 1=1 ";
		sql=getTransStateControlSql(transcwbstate,0, sql);
		return this.jdbcTemplate.query(sql, new TransStateControlRowMapper());
		
	}

	private String getTransStateControlSql(int transcwbstate, int toflowtype,
			String sql) {
		StringBuffer sb=new StringBuffer(sql);
		if(transcwbstate>0){
			sb.append(" and transcwbstate="+transcwbstate);
		}
		if(toflowtype>0){
			sb.append(" and toflowtype="+toflowtype);		
		}
		return sb.toString();
	}
	//运单总数
	public int getTransStateControlCount(int transcwbstate,int toflowtype){
		String sql="select count(*) from express_set_transcwb_allstate_control where 1=1 ";
		sql=this.getTransStateControlSql(transcwbstate, toflowtype, sql);
		return this.jdbcTemplate.queryForInt(sql); 
		
	}
	
	//删除
	public int deleteTranStateControl(int transcwbstate){
		String sql="delete from express_set_transcwb_allstate_control where transcwbstate=?";
		return this.jdbcTemplate.update(sql, transcwbstate);
		
	}

}
