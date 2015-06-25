package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeInsideShenhe;
import cn.explink.domain.PenalizeOut;
import cn.explink.service.PunishInsideService;
import cn.explink.util.Page;

@Component
public class PunishInsideDao {
	
	@Autowired
	PunishInsideService punishInsideService;
	private final class  Cwbrowmapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwbString=rs.getString("cwb");
			return cwbString;
		}
		
	}
	private final class PenalizeInsideRowMapper implements RowMapper<PenalizeInside>{
		@Override
		public PenalizeInside mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PenalizeInside penalizeInside=new PenalizeInside();
			penalizeInside.setCreateBySource(rs.getLong("createBySource"));
			penalizeInside.setCreateBysourcename(punishInsideService.getCreateSource(rs.getLong("createBySource")));
			penalizeInside.setCreateuserid(rs.getLong("createuserid"));
			penalizeInside.setCreUserName(punishInsideService.getCreUser(rs.getLong("createuserid")));
			penalizeInside.setCreDate(rs.getString("creDate"));
			penalizeInside.setCwb(rs.getString("cwb"));
			penalizeInside.setCwbPrice(rs.getBigDecimal("cwbPrice"));
			penalizeInside.setCwbstate(rs.getLong("cwbstate"));
			penalizeInside.setCwbstatename(punishInsideService.getFlowOrdertype(rs.getLong("cwbstate")));
			penalizeInside.setDutybranchid(rs.getLong("dutybranchid"));
			penalizeInside.setDutybranchname(punishInsideService.getBranchName(rs.getLong("dutybranchid")));
			penalizeInside.setDutypersonid(rs.getLong("dutypersonid"));
			penalizeInside.setDutypersonname(punishInsideService.getCreUser(rs.getLong("dutypersonid")));
			penalizeInside.setFileposition(rs.getString("fileposition"));
			penalizeInside.setId(rs.getLong("id"));
			penalizeInside.setPunishbigsort(rs.getLong("punishbigsort"));
			penalizeInside.setPunishcwbstate(rs.getInt("punishcwbstate"));
			penalizeInside.setPunishcwbstatename(punishInsideService.getPunishState(rs.getInt("punishcwbstate")));
			penalizeInside.setPunishdescribe(rs.getString("punishdescribe"));
			penalizeInside.setPunishInsideprice(rs.getBigDecimal("punishInsideprice"));
			penalizeInside.setPunishNo(rs.getString("punishNo"));
			penalizeInside.setPunishsmallsort(rs.getLong("punishsmallsort"));
			penalizeInside.setSourceNo(rs.getString("sourceNo"));
			penalizeInside.setShensutype(rs.getLong("shensutype"));
			penalizeInside.setShensudescribe(rs.getString("shensudescribe"));
			penalizeInside.setShensufileposition(rs.getString("shensufileposition"));
			penalizeInside.setShensuuserid(rs.getLong("shensuuserid"));
			penalizeInside.setShenhedescribe(rs.getString("shenhedescribe"));
			penalizeInside.setShenhefileposition(rs.getString("shenhefileposition"));
			penalizeInside.setShenhepunishprice(rs.getBigDecimal("shenhepunishprice"));
			penalizeInside.setShenhetype(rs.getLong("shenhetype"));
			penalizeInside.setShenheuserid(rs.getLong("shenheuserid"));
			penalizeInside.setShensudate(rs.getString("shensudate"));
			penalizeInside.setShenhedate(rs.getString("shenhedate"));
			penalizeInside.setPunishbigsortname(punishInsideService.getSortname(Integer.parseInt(rs.getLong("punishbigsort")+"")));
			penalizeInside.setPunishsmallsortname(punishInsideService.getSortname(Integer.parseInt(rs.getLong("punishsmallsort")+"")));
			return penalizeInside;
		}

	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//根据订单号创建对内扣罚单
	public int createPunishInside(final PenalizeInside penalizeInside,final long importFlag){
		return this.jdbcTemplate.update("insert into express_ops_punishInside_detail (cwb,punishNo,createBySource,sourceNo,dutybranchid,dutypersonid,cwbstate,cwbPrice,punishInsideprice,punishbigsort,punishsmallsort,createuserid,creDate,punishcwbstate,punishdescribe,fileposition,creategoodpunishprice,createqitapunishprice,importFlag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, penalizeInside.getCwb());
				ps.setString(2, penalizeInside.getPunishNo());
				ps.setLong(3, penalizeInside.getCreateBySource());
				ps.setString(4, penalizeInside.getSourceNo());
				ps.setLong(5, penalizeInside.getDutybranchid());
				ps.setLong(6, penalizeInside.getDutypersonid());
				ps.setLong(7, penalizeInside.getCwbstate());
				ps.setBigDecimal(8, penalizeInside.getCwbPrice());
				ps.setBigDecimal(9, penalizeInside.getPunishInsideprice());
				ps.setLong(10, penalizeInside.getPunishbigsort());
				ps.setLong(11, penalizeInside.getPunishsmallsort());
				ps.setLong(12, penalizeInside.getCreateuserid());
				ps.setString(13, penalizeInside.getCreDate());
				ps.setLong(14, penalizeInside.getPunishcwbstate());
				ps.setString(15, penalizeInside.getPunishdescribe());
				ps.setString(16, penalizeInside.getFileposition());
				ps.setBigDecimal(17, penalizeInside.getCreategoodpunishprice());
				ps.setBigDecimal(18, penalizeInside.getCreateqitapunishprice());
				ps.setLong(19, importFlag);

			}
		});
	}
	
	//查询所有的对内扣罚单
	public List<PenalizeInside> getAllPunishInside(){
		String sql="select * from express_ops_punishInside_detail";
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());
	}
	//根据查询条件查询对内扣罚的扣罚单
	public List<PenalizeInside> findByCondition(long page,String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate){
		String sql="select * from express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}
		if (dutynameid>0) {
			sql+=" And dutypersonid="+dutynameid;
		}
		if (cwbstate>0) {
			sql+=" And cwbstate="+cwbstate;
		}
		if (punishbigsort>0) {
			sql+=" And punishbigsort="+punishbigsort;
		}
		if (punishsmallsort>0) {
			sql+=" And punishsmallsort="+punishsmallsort;
		}
		if (!begindate.equals("")) {
			sql+=" And creDate>='"+begindate+"'";
		}
		if (!enddate.equals("")) {
			sql+=" And creDate<='"+enddate+"'";
		}
		if (page!=-9) {
			sql += " ORDER BY creDate DESC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());

	}
	//根据查询条件查询对内扣罚的扣罚单总单数
	public int findByConditionSum(String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate){
		String sql="select count(1) from express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}
		if (dutynameid>0) {
			sql+=" And dutypersonid="+dutynameid;
		}
		if (cwbstate>0) {
			sql+=" And cwbstate="+cwbstate;
		}
		if (punishbigsort>0) {
			sql+=" And punishbigsort="+punishbigsort;
		}
		if (punishsmallsort>0) {
			sql+=" And punishsmallsort="+punishsmallsort;
		}
		if (!begindate.equals("")) {
			sql+=" And creDate>='"+begindate+"'";
		}
		if (!enddate.equals("")) {
			sql+=" And creDate<='"+enddate+"'";
		}
		return this.jdbcTemplate.queryForInt(sql);
		
	}
	public List<PenalizeInside> getPenalizeInsideIsNull(String cwb,long dutybranchid,long punishsmallsort){
		String sql="select * from express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb ='"+cwb+"'";
		}

		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}

		if (punishsmallsort>0) {
			sql+=" And punishsmallsort="+punishsmallsort;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());

	}
	public PenalizeInside getPenalizeInsideIsNullCheck(String cwb,long dutybranchid,long personid,long punishsmallsort,BigDecimal goodsprice,BigDecimal otherPrice){
		try {
			String sql="select * from express_ops_punishInside_detail where cwb=? and dutybranchid=? and dutypersonid=? and punishsmallsort=? and creategoodpunishprice=? and createqitapunishprice=? ";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeInsideRowMapper(), cwb,dutybranchid,personid,punishsmallsort,goodsprice,otherPrice);
		} catch (DataAccessException e) {
			return null;
		}
		
	}
	public List<PenalizeInside> getInsidebycwb(String cwb){
		try {
			String sql="select * from express_ops_punishInside_detail where cwb=?";
			return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper(), cwb);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public PenalizeInside getInsidebyid(long id){
		try {
			String sql="select * from express_ops_punishInside_detail where id=?";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeInsideRowMapper(), id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public void updateShensuPunishInside(final long id,final long shensutype,final String describe,final String fileposition,final long userid,final long punishcwbstate){
		this.jdbcTemplate.update("update express_ops_punishInside_detail set shensutype=?,shensudescribe=?,shensufileposition=?,shensuuserid=?,punishcwbstate=?,shensudate=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, shensutype);
					ps.setString(2, describe);
					ps.setString(3, fileposition);
					ps.setLong(4, userid);
					ps.setLong(5, punishcwbstate);
					ps.setString(6, punishInsideService.getNowtime());
					ps.setLong(7, id);
			}
		});
	}
	public void updatePunishShenhe(final PenalizeInsideShenhe penalizeInsideShenhe){
		this.jdbcTemplate.update("update express_ops_punishInside_detail set shenhepunishprice=?,shenhetype=?,shenhedescribe=?,shenhefileposition=?,shenheuserid=?,punishcwbstate=?,shenhedate=?,lastgoodpunishprice=?,lastqitapunishprice=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
					ps.setBigDecimal(1, penalizeInsideShenhe.getShenhepunishprice());
					ps.setLong(2, penalizeInsideShenhe.getShenheresult());
					ps.setString(3, penalizeInsideShenhe.getShenhedescribe());
					ps.setString(4, penalizeInsideShenhe.getShenheposition());
					ps.setLong(5, penalizeInsideShenhe.getShenheuserid());
					ps.setLong(6, penalizeInsideShenhe.getPunishcwbstate());
					ps.setString(7, punishInsideService.getNowtime());
					ps.setBigDecimal(8, penalizeInsideShenhe.getResultgoodprice());
					ps.setBigDecimal(9, penalizeInsideShenhe.getResultqitaprice());
					ps.setLong(10, penalizeInsideShenhe.getId());

			}
		});
	}
	public String calculateSumPrice(String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate){
		String sql="SELECT SUM(punishInsideprice)as sumprice FROM express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}
		if (dutynameid>0) {
			sql+=" And dutypersonid="+dutynameid;
		}
		if (cwbstate>0) {
			sql+=" And cwbstate="+cwbstate;
		}
		if (punishbigsort>0) {
			sql+=" And punishbigsort="+punishbigsort;
		}
		if (punishsmallsort>0) {
			sql+=" And punishsmallsort="+punishsmallsort;
		}
		if (!begindate.equals("")) {
			sql+=" And creDate>='"+begindate+"'";
		}
		if (!enddate.equals("")) {
			sql+=" And creDate<='"+enddate+"'";
		}
		String sumprice=this.jdbcTemplate.queryForObject(sql, String.class);
		return sumprice;
	}
	public PenalizeInside getInsidebyidwithstate(long id,long state1,long state2){
		try {
			String sql="select * from express_ops_punishInside_detail where id=? And punishcwbstate IN(?,?)";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeInsideRowMapper(), id,state1,state2);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public List<String> findImportExcelSuccess(long importflag){
		String sql="select cwb from express_ops_punishInside_detail where importFlag=?";
		return this.jdbcTemplate.query(sql, new Cwbrowmapper(),importflag);
	}
	public void updateIsfine(String cwbstr,long isfine){
		String sql="update express_ops_punishInside_detail set isfine=? where cwb=?";
		this.jdbcTemplate.update(sql, isfine,cwbstr);
	}
}
