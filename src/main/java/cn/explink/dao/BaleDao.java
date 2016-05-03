package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Bale;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.Page;

@Component
public class BaleDao {
	private final class BaleMapper implements RowMapper<Bale> {
		@Override
		public Bale mapRow(ResultSet rs, int rowNum) throws SQLException {
			Bale bale = new Bale();
			bale.setId(rs.getLong("id"));
			bale.setBaleno(rs.getString("baleno"));
			bale.setBalestate(rs.getLong("balestate"));
			bale.setBranchid(rs.getLong("branchid"));
			bale.setGroupid(rs.getLong("groupid"));
			bale.setCretime(rs.getTimestamp("cretime"));
			bale.setCwbcount(rs.getLong("cwbcount"));
			bale.setNextbranchid(rs.getLong("nextbranchid"));
			bale.setScannum(rs.getLong("scannum"));
			return bale;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createBale(final Bale bale) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale(baleno,balestate,branchid,nextbranchid,cwbcount,handlerid,handlername) values(?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, bale.getBaleno());
				ps.setLong(2, bale.getBalestate());
				ps.setLong(3, bale.getBranchid());
				ps.setLong(4, bale.getNextbranchid());
				ps.setLong(5, bale.getCwbcount());
				ps.setInt(6, bale.getHandlerid());
				ps.setString(7, bale.getHandlername());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long create(final String baleno) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale(baleno) values(?)", new String[] { "id" });
				ps.setString(1, baleno);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	@Deprecated
	public Bale getBaleByBalestateAndBranchid(String baleno, long balestate, long branchid) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=? and branchid=?";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate, branchid);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Deprecated
	public List<Bale> getBaleByBalenoAndBranchid(String baleno, long branchid) {
		String sql = "select * from express_ops_bale where baleno=? and branchid=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno, branchid);
	}

	@Deprecated
	public List<Bale> getBaleByBalestate(String baleno, long balestate) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno, balestate);
	}

	@Deprecated
	public List<Bale> getBaleByBalenoAndBalestate(String baleno, String balestates) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in(" + balestates + ")";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno);
	}

	@Deprecated
	public List<Bale> getBaleByBalestate(long balestate) {
		String sql = "select * from express_ops_bale where balestate=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), balestate);
	}

	@Deprecated
	public void saveForBranchid(long id, long branchid, long groupid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,groupid=?,balestate=? where id=?";
		this.jdbcTemplate.update(sql, branchid, groupid, balestate, id);
	}

	@Deprecated
	public Bale getBaleByBaleno(String baleno, long balestate) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/*
	 * public void saveForBaleState(long balestate,long groupid,long branchid){
	 * String sql = "update express_ops_bale set balestate="+balestate+
	 * " where id in(select baleid from express_ops_groupdetail where groupid="
	 * +groupid
	 * +") and branchid="+branchid+" and balestate="+BaleStateEnum.SaoMiaoZhong
	 * .getValue(); jdbcTemplate.update(sql); }
	 */
	
	@Deprecated
	public void saveForState(String baleno, long branchid, long balestate) {
		String sql = "update express_ops_bale set balestate=? where branchid=? and baleno=? and balestate=? ";
		this.jdbcTemplate.update(sql, balestate, branchid, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}

	@Deprecated
	public void saveForBalestate(String baleno, long balestate, long oldbalestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? and balestate=? ";
		this.jdbcTemplate.update(sql, balestate, baleno, oldbalestate);
	}

	@Deprecated
	public void saveForBranchidAndState(String baleno, long branchid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,balestate=? where baleno=? and balestate=? ";
		this.jdbcTemplate.update(sql, branchid, balestate, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}


	@Deprecated
	public void saveById(long balestate, long id) {
		String sql = "update express_ops_bale set balestate=? where id=?";
		this.jdbcTemplate.update(sql, balestate, id);
	}

	@Deprecated
	public List<Bale> getBaleByBaleno(String baleno) {
		String sql = "select * from express_ops_bale where baleno=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno);

	}

	@Deprecated
	public void saveForBaleCount(long id, long cwbcount) {
		String sql = "update express_ops_bale set cwbcount=?  where id=? ";
		this.jdbcTemplate.update(sql, cwbcount, id);
	}


	@Deprecated
	public Bale getBaleByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_bale where cwb=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Deprecated
	public Bale getBaleOneByBaleno(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Deprecated
	public Bale getBaleOneByBalenoLock(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Deprecated
	public void updateSubBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount-1  where baleno=? ";
		this.jdbcTemplate.update(sql, baleno);
	}

	@Deprecated
	public void updateAddBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount+1 where baleno=? ";
		this.jdbcTemplate.update(sql, baleno);
	}

	@Deprecated
	public void updateAddBaleScannum(String baleno) {
		String sql = "update express_ops_bale set scannum=scannum+1 where baleno=? and balestate=?";
		this.jdbcTemplate.update(sql, baleno,BaleStateEnum.WeiFengBao.getValue());
	}

	@Deprecated
	public void updateBalesate(String baleno, long balestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? ";
		this.jdbcTemplate.update(sql, balestate, baleno);
	}

	public List<Bale> getBaleByBalePrint(long branchid, String baleno, String strtime, String endtime) {
		String sql = "select * from express_ops_bale where branchid=? and cwbcount>0 and balestate in (?,?,?)";
		if (baleno.length() > 0) {
			sql += " and baleno='" + baleno + "'";
		}
		if (strtime.length() > 0) {
			sql += " and cretime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and cretime<'" + endtime + "'";
		}
		return this.jdbcTemplate.query(sql, new BaleMapper(), branchid
				,BaleStateEnum.YiFengBao.getValue(),BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
				);
	}

	public Bale getBaleById(long baleid) {
		try {
			String sql = "select * from express_ops_bale where id=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	// added by jiangyu
	public void updateBalesateAndNextBranchId(long baleid, long balestate, long nextBranchId, long currentBranchId) {
		String sql = "update express_ops_bale set balestate=?,nextbranchid=?,branchid=? where id=? ";
		this.jdbcTemplate.update(sql, balestate, nextBranchId, currentBranchId, baleid);
	}

	@Deprecated
	public void updateBranchIdAndNextBranchId(String baleno, long nextBranchId, long currentBranchId) {
		String sql = "update express_ops_bale set nextbranchid=?,branchid=? where baleno=? ";
		this.jdbcTemplate.update(sql, nextBranchId, currentBranchId, baleno);
	}
	
	public void saveForBranchidAndGroupid(long branchid, long balestate, long groupid) {
		String sql = "update express_ops_bale set balestate=? where branchid=? and groupid=? and balestate=?";
		this.jdbcTemplate.update(sql, balestate, branchid, groupid, BaleStateEnum.WeiDaoZhan.getValue());
	}
	
	public List<Bale> getBaleByChukuDate(String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new BaleMapper(), begindate, enddate);
	}

	public long getBaleByChukuDateCount(String begindate, String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
	}
	
	public int getBaleUseState(String baleno){
		String sqlCount = "select count(id) from express_ops_bale where baleno=?";
		String sqlOpen = "select count(id) from express_ops_bale where baleno=? and balestate in (8)";
		String sqlOnway = "select count(id) from express_ops_bale where baleno=? and balestate in (9,10)";

		long cnt=this.jdbcTemplate.queryForLong(sqlCount, baleno);
		if(cnt==0){
			return 0;
		}
		
		cnt=this.jdbcTemplate.queryForLong(sqlOpen, baleno);
		if(cnt!=0){
			return 1;
		}
		
		cnt=this.jdbcTemplate.queryForLong(sqlOnway, baleno);
		if(cnt!=0){
			return 2;
		}
		
		return 3;
	}
	
	public List<Bale> getBaleYifengbao(String baleno) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in (?,?)";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno,BaleStateEnum.YiFengBao.getValue(),BaleStateEnum.YiFengBaoChuKu.getValue());
	}
	
	private Bale getBaleUnique(String baleno,String sql,Object... args) {
		Bale bale=null;
		List<Bale> baleList=this.jdbcTemplate.query(sql, new BaleMapper(),args);
			
		if(baleList!=null){
			if(baleList.size()==1){
				bale=baleList.get(0);
			}else if(baleList.size()>1){
				throw new CwbException(baleno,"发现此包号处于在途状态的记录多于一个.");
			}
		}

		return bale;
	}
	
	public Bale getBaleWeifengbaoByLock(String baleno) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=? for update";
		return getBaleUnique(baleno,sql,baleno,BaleStateEnum.WeiFengBao.getValue());

	}
	
	public Bale getBaleWeifengbao(String baleno) {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return getBaleUnique(baleno,sql, baleno,BaleStateEnum.WeiFengBao.getValue());
	}
	
	public void updateBalesate(long baleid, long balestate) {
		String sql = "update express_ops_bale set balestate=? where id=? ";
		this.jdbcTemplate.update(sql, balestate, baleid);
	}
	
	//这三种状态在表里都是唯一的
	public Bale getBaleOnway(String baleno) {
			String sql = "select * from express_ops_bale where baleno=? and balestate in (?,?,?)";
			return getBaleUnique(baleno,sql, baleno,BaleStateEnum.YiFengBao.getValue(),
					BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
					);//有可能忘记了封包
	}
	
	public Bale getBaleOnwayByLock(String baleno) {
			String sql = "select * from express_ops_bale where baleno=? and balestate in (?,?,?) for update";
			return getBaleUnique(baleno,sql,baleno,BaleStateEnum.YiFengBao.getValue(),
					BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
					);
	}
	
	public Bale getBaleOnwayBycwb(String scancwb) {
			String sql = "select a.* from express_ops_bale a join "
					+ "(select baleid from express_ops_bale_cwb where cwb=?) b on b.baleid=a.id and a.balestate in (?,?,?)";
			return getBaleUnique(scancwb,sql, scancwb,BaleStateEnum.YiFengBao.getValue(),
					BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
					);//有可能忘记了封包
	}
	
	public List<Bale> getBaleOnwayListBycwb(String scancwb) {
		
			String sql = "select a.* from express_ops_bale a join "
					+ "(select baleid from express_ops_bale_cwb where cwb=?) b on b.baleid=a.id and a.balestate in (?,?,?)";
			return this.jdbcTemplate.query(sql, new BaleMapper(), scancwb,BaleStateEnum.YiFengBao.getValue(),
					BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
					);//有可能忘记了封包
		
	}
	
	public void updateBranchIdAndNextBranchId(long baleid, long nextBranchId, long currentBranchId) {
		String sql = "update express_ops_bale set nextbranchid=?,branchid=? where id=? ";
		this.jdbcTemplate.update(sql, nextBranchId, currentBranchId, baleid);
	}
	
	public void updateAddBaleScannum(long baleid) {
		String sql = "update express_ops_bale set scannum=scannum+1 where id=?";
		this.jdbcTemplate.update(sql, baleid);
	}
	
	public Bale getLastBaleByBaleno(String baleno) {
		String sql = "select * from express_ops_bale where baleno=? order by id desc limit 1";
		List<Bale>  list= this.jdbcTemplate.query(sql, new BaleMapper(), baleno);
		if(list==null||list.size()<1){
			return null;
		}else{
			return list.get(0);
		}

	}
	
	public void saveForBranchidAndState(long baleid, long branchid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,balestate=? where id=?";
		this.jdbcTemplate.update(sql, branchid, balestate, baleid);
	}
	
	public void updateAddBaleCount(long baleid) {
		String sql = "update express_ops_bale set cwbcount=cwbcount+1 where id=? ";
		this.jdbcTemplate.update(sql, baleid);
	}
}
