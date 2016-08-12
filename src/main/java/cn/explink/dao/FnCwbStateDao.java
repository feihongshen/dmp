package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.explink.domain.FnCwbState;
import cn.explink.enumutil.FnCwbStatusEnum;


@Repository
public class FnCwbStateDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 批量更新上门退运费收款状态
	 * @param list
	 */
	public void batchUpdateSmtfreightflag(final List<FnCwbState> list){
		String sql = "update fn_cwb_state set smtfreightflag=?, smtfreight_time=? where cwb=?";
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				FnCwbState cwbState = list.get(i);
				
				ps.setInt(1, cwbState.getSmtfreightflag());
				if(cwbState.getSmtfreightflag() == FnCwbStatusEnum.Unreceive.getIndex()){
					ps.setNull(2, Types.DATE);
				}
				else{
					ps.setString(2, sdf.format(new Date()));
				}
				ps.setString(3, cwbState.getCwb());
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 批量更新代收货款收款状态
	 * @param list
	 */
	public void batchUpdateReceivablefeeflag(final List<FnCwbState> list){
		String sql = "update fn_cwb_state set receivablefeeflag=?, receivablefee_time=? where cwb=?";
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				FnCwbState cwbState = list.get(i);
				
				ps.setInt(1, cwbState.getReceivablefeeflag());
				if(cwbState.getReceivablefeeflag() == FnCwbStatusEnum.Unreceive.getIndex()){
					ps.setNull(2, Types.DATE);
				}
				else{
					ps.setString(2, sdf.format(new Date()));
				}
				ps.setString(3, cwbState.getCwb());
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 批量更新快递运费收款状态
	 * @param list
	 */
	public void batchUpdateExpressfreightflag(final List<FnCwbState> list){
		String sql = "update fn_cwb_state set expressfreightflag=?, expressfreight_time=? where cwb=?";
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				FnCwbState cwbState = list.get(i);
				
				ps.setInt(1, cwbState.getExpressfreightflag());
				if(cwbState.getExpressfreightflag() == FnCwbStatusEnum.Unreceive.getIndex()){
					ps.setNull(2, Types.DATE);
				}
				else{
					ps.setString(2, sdf.format(new Date()));
				}
				ps.setString(3, cwbState.getCwb());
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 批量插入
	 * @param list
	 */
	public void batchInsert(final List<FnCwbState> list){
		String sql = "INSERT INTO fn_cwb_state(cwb,cwbordertypeid,customerid,smtfreightflag,smtfreight_time,receivablefeeflag,receivablefee_time,expressfreightflag,expressfreight_time,created_time)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				FnCwbState cwbState = list.get(i);
				
				ps.setString(1, cwbState .getCwb());
				ps.setInt(2, cwbState.getCwbordertypeid());
				ps.setLong(3, cwbState.getCustomerid());
				ps.setInt(4, cwbState.getSmtfreightflag());
				ps.setString(5, cwbState.getSmtfreightTime() == null ? null : sdf.format(cwbState.getSmtfreightTime()));
				ps.setInt(6, cwbState.getReceivablefeeflag());
				ps.setString(7, cwbState.getReceivablefeeTime() == null ? null : sdf.format(cwbState.getReceivablefeeTime()));
				ps.setInt(8, cwbState.getExpressfreightflag());
				ps.setString(9, cwbState.getExpressfreightTime() == null ? null : sdf.format(cwbState.getExpressfreightTime()));
				ps.setString(10, cwbState.getCreatedTime() == null ? null : sdf.format(cwbState.getCreatedTime()));
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

}
