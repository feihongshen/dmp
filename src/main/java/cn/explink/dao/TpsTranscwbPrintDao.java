package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.vo.TPStranscwb;

/**
 * TPS运单号打印
 * @author yurong.liang 2016-06-17
 */
@Repository("tpsTranscwbPrintDao")
public class TpsTranscwbPrintDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class TPStranscwbMapper implements RowMapper<TPStranscwb> {
		@Override
		public TPStranscwb mapRow(ResultSet rs, int rowNum) throws SQLException {

			TPStranscwb model = new TPStranscwb();
			model.setId(rs.getInt("id"));
			model.setTpstranscwb(rs.getString("tpstranscwb"));
			model.setPrintStatus(rs.getInt("print_status"));
			model.setPrintUser(rs.getString("print_user"));
			String printTime=rs.getString("print_time");
			String createTime=rs.getString("create_time");
			String updateTime=rs.getString("update_time");
			model.setPrintTime(printTime==null?"":printTime.substring(0, 19));
			model.setCreateTime(createTime==null?"":createTime.substring(0, 19));
			model.setUpdateTime(updateTime==null?"":updateTime.substring(0, 19));
			String printStatusTitle= model.getPrintStatus()==1?"已打印":"未打印";
			model.setPrintStatusTitle(printStatusTitle);
			return model;
		}
	}

	
	/**
	 * 根据查询条件获得本地tps运单号List
	 */
	public List<TPStranscwb> getList(Integer printStatus,String tpstranscwb, int page, int rows){
		
		StringBuffer sb=new StringBuffer("select * from express_ops_tpstranscwb where id>0");
		String sqlWhere=getWhereParam(printStatus,tpstranscwb);
		sb.append(sqlWhere);
		sb.append(" LIMIT "+(page-1)*rows+ "," +rows);
		String sql = sb.toString();
		return jdbcTemplate.query(sql, new TPStranscwbMapper());
	}
	
	/**
	 * 根据条件获得记录数
	 */
	public int getCount(Integer printStatus,String tpstranscwb){
		StringBuffer sb = new StringBuffer("select count(*) from express_ops_tpstranscwb where id>0");
		String sqlWhere=getWhereParam(printStatus,tpstranscwb);
		sb.append(sqlWhere);
		String sql = sb.toString();
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	/**
	 * 新增一条运单号信息
	 */
	public int createTPStranscwb( String tpstranscwb, String create_time) {
		String sql ="insert into express_ops_tpstranscwb(tpstranscwb,create_time)"
				+ " values (?,?)";
		try{
			return this.jdbcTemplate.update(sql,tpstranscwb,create_time);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 更新打印状态
	 */
	public void updatePrintStatus(String tpstranscwb,String printUser, String printTime,int printStatus) {
		String sql = "update express_ops_tpstranscwb set print_time=?,print_user=?,print_status=? where tpstranscwb=?";
		this.jdbcTemplate.update(sql, printTime, printUser,printStatus,tpstranscwb);
	}
	
	
	
	//拼接查询条件
	private String getWhereParam(Integer printStatus,String tpstranscwb){
		StringBuffer whereParam = new StringBuffer();
		if (printStatus!=null && printStatus!=-1) {
			whereParam.append(" and print_status = "+printStatus);
		}
		if (tpstranscwb!=null && !"".equals(tpstranscwb)) {
			whereParam.append(" and tpstranscwb = '"+tpstranscwb+"'");
		}
		return whereParam.toString();
	}
}
