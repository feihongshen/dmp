package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Component
public class WarehouseToCommenDAO {

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
			warehtoCommen.setNextbranchid(rs.getLong("nextbranchid"));
			warehtoCommen.setRemark(rs.getString("remark"));
			warehtoCommen.setOutbranchflag(rs.getInt("outbranchflag"));

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

	public void creWarehouseToCommen(String cwb, long customerid, long startbranchid, long nextbranchid, String commencode, String credate, String statetime, int outbranchflag) {
		jdbcTemplate.update("insert into commen_cwb_order(cwb,customerid,startbranchid,nextbranchid,commencode,credate,statetime,outbranchflag) " + " values(?,?,?,?,?,?,?,?)", cwb, customerid,
				startbranchid, nextbranchid, commencode, credate, statetime, outbranchflag);
	}

	public void updateWarehouseToCommen(String cwb, long startbranchid, long nextbranchid, String commencode, String credate) {
		jdbcTemplate
				.update("update commen_cwb_order set startbranchid=?,nextbranchid=?,commencode=?,credate=? ,statetime=''" + " where cwb=?  ", startbranchid, nextbranchid, commencode, credate, cwb);
	}

	public long getcwb(String cwb, String commencode) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order  " + " where cwb=? and commencode=? ", cwb, commencode);
	}

	public long getCommonCountByCwb(String cwb) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order  where cwb=? ", cwb);
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencode(String commencode) {
		return jdbcTemplate.query("select * from  commen_cwb_order " + " where commencode=? and stateTime='' ", new WarehouseToCommenRowMapper(), commencode);
	}

	public long getCommenCwbListByCommencodeCount(String commencode, int outbranchflag) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order " + " where commencode=? and stateTime='' and outbranchflag=? ", commencode, outbranchflag);
	}

	public long getCommenCwbListByCommencodeCount_branch(String commencode, long startbranchid, int outbranchflag) {
		return jdbcTemplate.queryForLong("select count(1) from  commen_cwb_order " + " where commencode=? and startbranchid=? and outbranchflag=? and stateTime='' ", commencode, startbranchid,
				outbranchflag);
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencode(String commencode, long page, int outbranchflag) {
		String sql = "select * from  commen_cwb_order  " + " where commencode=? and stateTime='' and outbranchflag=? limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper(), commencode, outbranchflag);
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencode_branch(String commencode, long page, long branchid, int outbranchflag) {
		String sql = "select * from  commen_cwb_order  " + " where commencode=? and stateTime='' and startbranchid=? and outbranchflag=? limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper(), commencode, branchid, outbranchflag);
	}

	public List<WarehouseToCommen> getCommenCountGroupByCommencode(String commencode, int outbranchflag) {
		String sql = "SELECT commencode,COUNT(1) as cwbcount FROM  commen_cwb_order where outbranchflag=" + outbranchflag + " GROUP BY  commencode,stateTime HAVING stateTime=''";
		if (commencode != null && commencode.length() > 0) {
			sql += " and commencode='" + commencode + "'";

		}
		return jdbcTemplate.query(sql, new WarehouseToCommenGroupByRowMapper());
	}

	/**
	 * 根据当前站点查询
	 * 
	 * @param commencode
	 * @return
	 */
	public List<WarehouseToCommen> getCommenCountGroupByCommencode_branch(String commencode, long startbranchid, int outbranchflag) {
		String sql = "SELECT commencode,COUNT(1) as cwbcount FROM  commen_cwb_order where startbranchid=" + startbranchid + " and outbranchflag=" + outbranchflag
				+ " GROUP BY  commencode,stateTime HAVING stateTime=''";
		if (commencode != null && commencode.length() > 0) {
			sql += " and commencode='" + commencode + "'";
		}
		return jdbcTemplate.query(sql, new WarehouseToCommenGroupByRowMapper());
	}

	public List<WarehouseToCommen> getCommenCwbListByCommencodes(String commencodes, long startbranchid, int outbranchflag,int maxCount) {
		String sql = "select * from  commen_cwb_order  " + " where commencode in(" + commencodes + ") and stateTime='' and outbranchflag=" + outbranchflag;
		if (startbranchid > 0) {
			sql += " and startbranchid=" + startbranchid;
		}
		if(maxCount>0){
			sql+=" limit 0,"+maxCount;
		}
		return jdbcTemplate.query(sql, new WarehouseToCommenRowMapper());
	}

	public void updateCommenCwbListById(long id, String stateTime) {
		jdbcTemplate.update("update commen_cwb_order set stateTime=? " + " where id=?  ", stateTime, id);
	}

	public void updateCommenCwbListByCwb(String cwb, String stateTime) {
		jdbcTemplate.update("update commen_cwb_order set stateTime=? " + " where cwb=?  ", stateTime, cwb);
	}

	public void deleteCommonBycwb(String cwb) {
		jdbcTemplate.update("delete from  commen_cwb_order  where cwb=? ", cwb);
	}

	public WarehouseToCommen getCommenByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("select * from  commen_cwb_order  where cwb=?  limit 1", new WarehouseToCommenRowMapper(), cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

}
