package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.Page;

@Component
public class WarehouseCommenDAO {

	private final class WarehouseToCommenRowMapper implements RowMapper<WarehouseToCommen> {
		@Override
		public WarehouseToCommen mapRow(ResultSet rs, int rowNum) throws SQLException {
			WarehouseToCommen warehtoCommen = new WarehouseToCommen();
			warehtoCommen.setId(rs.getLong("id"));
			warehtoCommen.setCwb(rs.getString("cwb"));
			warehtoCommen.setStartbranchid(rs.getLong("startbranchid"));
			warehtoCommen.setCommencode(rs.getString("commencode"));
			warehtoCommen.setCredate(rs.getString("credate"));
			warehtoCommen.setStatetime(rs.getString("statetime"));
			warehtoCommen.setEmaildateid(rs.getLong("emaildateid"));
			warehtoCommen.setNextbranchid(rs.getLong("nextbranchid"));
			warehtoCommen.setRemark(rs.getString("remark"));
			warehtoCommen.setCustomerid(rs.getLong("customerid"));
			return warehtoCommen;
		}
	}

	private final class WarehouseToCommenGroupByRowMapper implements RowMapper<WarehouseToCommen> {
		@Override
		public WarehouseToCommen mapRow(ResultSet rs, int rowNum) throws SQLException {
			WarehouseToCommen warehtoCommen = new WarehouseToCommen();
			warehtoCommen.setId(rs.getLong("cwbcount"));
			warehtoCommen.setCommencode(rs.getString("commencode"));
			return warehtoCommen;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void creWarehouseToCommen(long id, String cwb, long customerid, long startbranchid, long nextbranchid, String commencode, String credate, String statetime, long emaildateid) {
		jdbcTemplate.update("insert into commen_cwb_order(id,cwb,customerid,startbranchid,nextbranchid,commencode,credate,statetime,emaildateid) " + " values(?,?,?,?,?,?,?,?,?)", id, cwb, customerid,
				startbranchid, nextbranchid, commencode, credate, statetime, emaildateid);
	}

	public void updateWarehouseToCommen(long id, String cwb, long startbranchid, long nextbranchid, String commencode, String credate, long emaildateid) {
		jdbcTemplate.update("update commen_cwb_order set cwb=?,startbranchid=?,nextbranchid=?,commencode=?,credate=?,emaildateid=?,statetime='' " + " where id=?  ", cwb, startbranchid, nextbranchid,
				commencode, credate, emaildateid, id);
	}

	public WarehouseToCommen getCountByid(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from  commen_cwb_order  " + " where id=? ", new WarehouseToCommenRowMapper(), id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencode(String commencode) {
		return jdbcTemplate.query("select * from  commen_cwb_order " + " where commencode=? and stateTime='' ", new WarehouseToCommenRowMapper(), commencode);
	}

	public long getCommenCwbListByCommencodeCount(String commencode) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order " + " where commencode=? and stateTime='' ", commencode);
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencode(String commencode, long page) {
		String sql = "select * from  commen_cwb_order  " + " where commencode=? and stateTime='' limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper(), commencode);
	}

	public List<WarehouseToCommen> getCommenCountGroupByCommencode() {
		return jdbcTemplate.query("SELECT commencode,COUNT(1) as cwbcount FROM  commen_cwb_order GROUP BY " + " commencode,stateTime HAVING stateTime=''", new WarehouseToCommenGroupByRowMapper());
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencodes(long countFirst, String commencodes, long page) {
		String sql = "select * from  commen_cwb_order  " + " where commencode in(" + commencodes + ") and stateTime='' limit " + countFirst + "," + page;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	public void updateCommenCwbListById(long id, String stateTime) {
		jdbcTemplate.update("update commen_cwb_order set stateTime=? " + " where id=?  ", stateTime, id);
	}

	public List<WarehouseToCommen> getCommenCwbListByCommonAndCount(String commencodes, long maxCount) {
		String sql = "select * from  commen_cwb_order  " + " where commencode in(" + commencodes + ") and stateTime in ('',2)  order by stateTime limit 0," + maxCount;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	public List<WarehouseToCommen> getCommenCwbListByCommon(String commencode, long maxCount, long loopcount) {
		String sql = "select * from  commen_cwb_order  " + " where commencode ='" + commencode + "' and stateTime in ('',2) and loopcount<" + loopcount + " order by stateTime limit 0," + maxCount;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	public void updateCommenCwbListBycwbs(String cwbs, String stateTime) {
		jdbcTemplate.update("update commen_cwb_order set stateTime=?  where cwb in (" + cwbs + ") ", stateTime);
	}

	public void updateCommenCwbListBycwb(String cwb, String stateTime, String remark) {
		jdbcTemplate.update("update commen_cwb_order set stateTime=?,remark=? ,loopcount=loopcount+1 where cwb=? ", stateTime, remark, cwb);
	}

	public WarehouseToCommen getCommenCwbBycwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("select * from  commen_cwb_order  where cwb =? limit 0,1", new WarehouseToCommenRowMapper(), cwb);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * 根据 emaildateid 得到 下游 已经获取的数量
	 * 
	 * @param emaildateid
	 * @return
	 */
	public long getCountByEmaildateIdAndStatetime(long emaildateid) {
		String sql = " SELECT COUNT(1) FROM commen_cwb_order  WHERE emaildateid='" + emaildateid + "'";
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 根据emaildateid 查
	 * 
	 * @param emaildateid
	 * @return
	 */
	public List<WarehouseToCommen> getCountByEmaildateId(long emaildateid) {
		String sql = " SELECT * FROM commen_cwb_order  WHERE emaildateid='" + emaildateid + "'";
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	/**
	 * 根据 cwbs 查询
	 * 
	 * @param string
	 * @return
	 */
	public List<WarehouseToCommen> getCountByCwbs(String cwbs) {
		String sql = " SELECT * FROM commen_cwb_order  WHERE cwb in(" + cwbs + ")";
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	public long getCountByCwb(String cwb) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order where cwb=?", cwb);
	}
}
