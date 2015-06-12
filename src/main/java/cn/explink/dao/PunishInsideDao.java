package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeInside;
import cn.explink.util.Page;

@Component
public class PunishInsideDao {
	private final class PenalizeInsideRowMapper implements RowMapper<PenalizeInside>{
		@Override
		public PenalizeInside mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PenalizeInside penalizeInside=new PenalizeInside();
			penalizeInside.setCreateBySource(rs.getLong("createBySource"));
			penalizeInside.setCreateuserid(rs.getLong("createuserid"));
			penalizeInside.setCreDate(rs.getString("creDate"));
			penalizeInside.setCwb(rs.getString("cwb"));
			penalizeInside.setCwbPrice(rs.getBigDecimal("cwbPrice"));
			penalizeInside.setCwbstate(rs.getLong("cwbstate"));
			penalizeInside.setDutybranchid(rs.getLong("dutybranchid"));
			penalizeInside.setDutypersonid(rs.getLong("dutypersonid"));
			penalizeInside.setFileposition(rs.getString("fileposition"));
			penalizeInside.setId(rs.getLong("id"));
			penalizeInside.setPunishbigsort(rs.getLong("punishbigsort"));
			penalizeInside.setPunishcwbstate(rs.getInt("punishcwbstate"));
			penalizeInside.setPunishdescribe(rs.getString("punishdescribe"));
			penalizeInside.setPunishInsideprice(rs.getBigDecimal("punishInsideprice"));
			penalizeInside.setPunishNo(rs.getString("punishNo"));
			penalizeInside.setPunishsmallsort(rs.getLong("punishsmallsort"));
			penalizeInside.setSourceNo(rs.getString("sourceNo"));;
			return penalizeInside;
		}
		
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//根据订单号创建对内扣罚单
	public void createPunishInside(final PenalizeInside penalizeInside){
		this.jdbcTemplate.update("insert into express_ops_punishInside_detail (cwb,punishNo,createBySource,sourceNo,dutybranchid,dutypersonid,cwbstate,cwbPrice,punishInsideprice,punishbigsort,punishsmallsort,createuserid,creDate,punishcwbstate,punishdescribe,fileposition) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
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
			sql+=" And cwb IN('"+cwb+"')";
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
			sql+=" And creDate>='"+enddate+"'";
		}
		if (page!=-9) {
			sql += " ORDER BY creDate DESC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());
		
	}
}
