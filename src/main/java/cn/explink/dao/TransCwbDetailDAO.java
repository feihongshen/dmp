package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Tools;

@Repository
public class TransCwbDetailDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class TransCwbRowMapper implements RowMapper<TransCwbDetail> {

		@Override
		public TransCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransCwbDetail tcd = new TransCwbDetail();
			tcd.setId(rs.getInt("id"));
			tcd.setCwb(rs.getString("cwb"));
			tcd.setTranscwb(rs.getString("transcwb"));
			tcd.setTranscwbstate(rs.getInt("transcwbstate"));
			tcd.setTranscwboptstate(rs.getInt("transcwboptstate"));
			tcd.setCurrentbranchid(rs.getInt("currentbranchid"));
			tcd.setPreviousbranchid(rs.getInt("previousbranchid"));
			tcd.setNextbranchid(rs.getLong("nextbranchid"));
			tcd.setCreatetime(rs.getString("createtime"));
			tcd.setModifiedtime(rs.getString("modifiedtime"));
			tcd.setEmaildate(rs.getString("emaildate"));
			tcd.setCommonphraseid(rs.getInt("commonphraseid"));
			tcd.setCommonphrase(rs.getString("commonphrase"));
			tcd.setVolume(rs.getBigDecimal("cargovolume"));
			tcd.setWeight(rs.getBigDecimal("carrealweight"));
			return tcd;
		}
	}

	/**
	 * 添加TransCwbDetail
	 *
	 * @return
	 */
	public void addTransCwbDetail(final TransCwbDetail tc) {
		String sql = "insert into express_ops_transcwb_detail(cwb,transcwb,transcwbstate,transcwboptstate,currentbranchid,previousbranchid,nextbranchid,createtime,emaildate,commonphraseid,commonphrase) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, tc.getCwb());
				ps.setString(2, tc.getTranscwb());
				ps.setInt(3, tc.getTranscwbstate());
				ps.setInt(4, tc.getTranscwboptstate());
				ps.setLong(5, tc.getCurrentbranchid());
				ps.setLong(6, tc.getPreviousbranchid());
				ps.setLong(7, tc.getNextbranchid());
				ps.setString(8, tc.getCreatetime());
				ps.setString(9, tc.getEmaildate());
				ps.setLong(10, tc.getCommonphraseid());
				ps.setString(11, tc.getCommonphrase());

			}
		});
	}

	/**
	 * 修改TransCwbDetail
	 *
	 * @return
	 */
	public void updateTransCwbDetail(final TransCwbDetail tc) {
		String sql = "update express_ops_transcwb_detail set cwb=?,transcwb=?,transcwbstate=?,transcwboptstate=?,currentbranchid=?,previousbranchid=?,nextbranchid=?,createtime=?,modifiedtime=?,emaildate=?,commonphraseid=?,commonphrase=? where id=?";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, tc.getCwb());
				ps.setString(2, tc.getTranscwb());
				ps.setInt(3, tc.getTranscwbstate());
				ps.setInt(4, tc.getTranscwboptstate());
				ps.setLong(5, tc.getCurrentbranchid());
				ps.setLong(6, tc.getPreviousbranchid());
				ps.setLong(7, tc.getNextbranchid());
				ps.setString(8, tc.getCreatetime());
				ps.setString(9, tc.getModifiedtime());
				ps.setString(10, tc.getEmaildate());
				ps.setLong(11, tc.getCommonphraseid());
				ps.setString(12, tc.getCommonphrase());
				ps.setInt(13, tc.getId());

			}
		});
	}

	/**
	 * 查找全部
	 *
	 * @return
	 */
	public List<TransCwbDetail> findTransCwbDetail() {
		String sql = "select * from express_ops_transcwb_detail";
		return this.jdbcTemplate.query(sql, new TransCwbRowMapper());
	}

	/**
	 * 查找通过运单号
	 *
	 * @return
	 */
	public TransCwbDetail findTransCwbDetailByTransCwb(String transcwb) {
		String sql = "select * from express_ops_transcwb_detail where transcwb=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new TransCwbRowMapper(), transcwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 *
	 * 通过运单号列表查询运单
	 *
	 * @param transCwbList
	 * @return
	 */
	public List<TransCwbDetail> getTransCwbDetailListByTransCwbList(List<String> transCwbList) {
		List<TransCwbDetail> transCwbDetailList = new ArrayList<TransCwbDetail>();
		//Added by leoliao at 2016-03-01
		if(transCwbList == null || transCwbList.isEmpty()){
			return transCwbDetailList; 
		}
		//Added end
		
		String sql = "select * from express_ops_transcwb_detail where transcwb " + Tools.assembleInByList(transCwbList);
		try {
			transCwbDetailList = this.jdbcTemplate.query(sql, new TransCwbRowMapper());
		} catch (DataAccessException e) {
		}
		return transCwbDetailList;
	}

	public String getCwbByTransCwb(String transcwb) {
		String sql = "select cwb from express_ops_transcwb_detail where transcwb=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, String.class, transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TransCwbDetail> getTransCwbDetailListByCwb(String cwb) {
		List<TransCwbDetail> transCwbDetailList = new ArrayList<TransCwbDetail>();
		String sql = "select * from express_ops_transcwb_detail where cwb=?";
		try {
			transCwbDetailList = this.jdbcTemplate.query(sql, new TransCwbRowMapper(), cwb);
		} catch (DataAccessException e) {
		}
		return transCwbDetailList;
	}

	public List<String> getTransCwbListByCwb(String cwb) {
		List<String> transCwbList = new ArrayList<String>();
		String sql = "select transcwb from express_ops_transcwb_detail where cwb=?";
		try {
			transCwbList = this.jdbcTemplate.queryForList(sql, String.class, cwb);
		} catch (DataAccessException e) {
		}
		return transCwbList;
	}

	public List<String> getTransCwbListByCwb(String cwb, FlowOrderTypeEnum flowordertype) {
		List<String> transCwbList = new ArrayList<String>();
		String sql = "select transcwb from express_ops_transcwb_detail where cwb=? and flowordertype!=?";
		try {
			transCwbList = this.jdbcTemplate.queryForList(sql, String.class, cwb, flowordertype.getValue());
		} catch (DataAccessException e) {
		}
		return transCwbList;
	}

	/**
	 * 通过id删除实体
	 *
	 * @return
	 */
	public void removeTransCwbDetailById(int id) {
		String sql = "delete from express_ops_transcwb_detail where id=" + id;
		this.jdbcTemplate.update(sql);
	}

	/**
	 *
	 * @Title: getTransCwbDetailListByTransCwbs
	 * @description 根据输入的子单集合，获取所有的子单详情
	 * @author 刘武强
	 * @date 2016年1月8日下午5:17:19
	 * @param @param transcwbs
	 * @param @return
	 * @return List<TransCwbDetail>
	 * @throws
	 */
	public List<TransCwbDetail> getTransCwbDetailListByTransCwbs(String transcwbs) {
		String sql = "select * from express_ops_transcwb_detail where transcwb in " + transcwbs;
		try {
			return this.jdbcTemplate.query(sql, new TransCwbRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 *
	 * @Title: importEmbracedData
	 * @description TODO
	 * @author 刘武强
	 * @date 2016年1月11日下午8:56:29
	 * @param @param list
	 * @param @param userparam
	 * @param @param branch
	 * @return void
	 * @throws
	 */
	public void saveWithMount(List<TransCwbDetail> list) {
		final String sql = "update express_ops_transcwb_detail set cwb=?,transcwbstate=?,transcwboptstate=?,nextbranchid=?,commonphraseid=?,commonphrase=?,currentbranchid=? where transcwb=?";
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;

		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<TransCwbDetail> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						TransCwbDetail tc = tempList.get(i);
						ps.setString(1, tc.getCwb());
						ps.setInt(2, tc.getTranscwbstate());
						ps.setInt(3, tc.getTranscwboptstate());
						ps.setLong(4, tc.getNextbranchid());
						ps.setLong(5, tc.getCommonphraseid());
						ps.setString(6, tc.getCommonphrase());
						ps.setLong(7, tc.getCurrentbranchid());
						ps.setString(8, tc.getTranscwb());
					}

					@Override
					public int getBatchSize() {

						return tempList.size();
					}
				});
			}
		}
	}

	/**
	 * 修改TransCwbstateByTranscwb
	 *
	 * @return
	 */
	public void updateTransCwbDetailBytranscwb(final String transcwb, final int transcwbstate) {
		String sql = "update express_ops_transcwb_detail set transcwbstate=?,commonphraseid=0,commonphrase=? where transcwb=?";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, transcwbstate);
				ps.setString(2, "");
				ps.setString(3, transcwb);
			}
		});
	}

	public void updateTransCwbStateByTranscwb(final String transcwb, final int transcwbstate) {
		String sql = "update express_ops_transcwb_detail set transcwbstate=? where transcwb=?";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, transcwbstate);
				ps.setString(2, transcwb);
			}
		});
	}

	/**
	 *
	 * @Title: queryTransCwbDetailBytranscwb
	 * @description 找出运单的所有兄弟运单的运单详情（包括自己）
	 * @author 刘武强
	 * @date 2016年1月14日上午9:38:52
	 * @param @param transcwb
	 * @param @return
	 * @return List<TransCwbDetail>
	 * @throws
	 */
	public List<TransCwbDetail> queryTransCwbDetailBytranscwb(String transcwb) {
		String sql = "select * from express_ops_transcwb_detail where cwb =(select cwb from express_ops_transcwb_detail where transcwb=?)";
		return this.jdbcTemplate.query(sql, new TransCwbRowMapper(), transcwb);
	}

	/**
	 * 更新发货时间
	 * @author leo01.liao
	 * @param listTranscwb
	 * @param emaildate
	 */
	public void updateEmaildate(List<String> listTranscwb, String emaildate) {
		if(listTranscwb == null || listTranscwb.isEmpty() || emaildate == null || emaildate.trim().equals("")){
			return;
		}
		
		String strIn = "";
		for(String transcwb : listTranscwb){
			if(transcwb == null || transcwb.trim().equals("")){
				continue;
			}
			
			strIn += "'" + transcwb.trim() + "',";
		}
		if(strIn.equals("")){
			return;
		}		
		strIn = strIn.substring(0, strIn.length()-1);		
		Date dtEmaildate = DateTimeUtil.parseDate(emaildate, DateTimeUtil.DEF_DATETIME_FORMAT);
		
		this.jdbcTemplate.update("update express_ops_transcwb_detail set emaildate=? where transcwb in(" + strIn + ") ", dtEmaildate);
	}
	
	/**
	 * 更新运单的下一站
	 * @param cwb
	 * @param nextbranchId
	 */
	public void updateNextbranch(String cwb, long nextbranchId) {
		String sql = "update express_ops_transcwb_detail set nextbranchid=? where cwb=?";
		this.jdbcTemplate.update(sql, nextbranchId, cwb);
	}
	
	/**
	 * 更新运单重量
	 * @param cwb
	 * @param transCwb
	 * @param carrealweight
	 */
	public void updateTransCwbDetailWeight(String cwb , String transCwb , BigDecimal carrealweight){
		String  sql = "update express_ops_transcwb_detail set carrealweight=?  where transcwb = ?  and cwb = ? ";
		this.jdbcTemplate.update(sql,carrealweight,transCwb,cwb) ;
	}
	
	public void updateNextbranchByTranscwb(String transcwb, long nextbranchId) {
		String sql = "update express_ops_transcwb_detail set nextbranchid=? where transcwb=?";
		this.jdbcTemplate.update(sql, nextbranchId, transcwb);
	}
	
	/**
	 * 查找查看每一个运单号的状态
	 *
	 * @return
	 */
	public List<Long> getTransCwbStateListByCwb(String cwb) {
		List<Long> transCwbList = null;
		String sql = "select transcwboptstate from express_ops_transcwb_detail where cwb=?";
		try {
			transCwbList = this.jdbcTemplate.queryForList(sql, Long.class, cwb);
		} catch (DataAccessException e) {
		}
		return transCwbList;
	}
	
	public void updateEmaildate(String cwb,String transcwb, String emaildate) {
		if( emaildate == null || emaildate.trim().length()<1){
			return;
		}
		
		Date emaildateObj = DateTimeUtil.parseDate(emaildate, DateTimeUtil.DEF_DATETIME_FORMAT);
		
		String sql="update express_ops_transcwb_detail set emaildate=? where transcwb=? and cwb=?";
		this.jdbcTemplate.update(sql, emaildateObj,transcwb,cwb);
	}
  
  /**
	 * 根据订单号，获取所有的运单
	 *
	 * @return
	 */
	public List<TransCwbDetail> queryTransCwbDetail(String cwb) {
		StringBuffer sql = new StringBuffer("select * from express_ops_transcwb_detail") ;
		 sql.append(" where cwb = '").append(cwb).append("'");
		return this.jdbcTemplate.query(sql.toString(), new TransCwbRowMapper());
	}
	
	public void updatePreviousbranchidByCwb(final String cwb, long previousbranchid) {
		String sql = "update express_ops_transcwb_detail set previousbranchid=? where cwb=?";
		this.jdbcTemplate.update(sql, previousbranchid, cwb);
	}
	
	/**
	 * 更新运单状态
	 */
	public void updateDetailTranscwbstate(String cwb, TransCwbStateEnum transcwbstate){
		String sql = "update express_ops_transcwb_detail set transcwbstate = ? where cwb=?";
		jdbcTemplate.update(sql, transcwbstate.getValue(), cwb);
	}
	
	/**
	 * 根据订单号删除运单明细
	 * @author leo01.liao
	 * @param cwb
	 */
	public void deleteByCwb(String cwb) {
		try {
			if(cwb == null || cwb.trim().equals("")){
				return;
			}
			
			String sql = "delete from express_ops_transcwb_detail where cwb=?";
			this.jdbcTemplate.update(sql, cwb.trim());
		}catch(Exception ex){}
	}
}
