package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeInsideFilepath;
import cn.explink.domain.PenalizeInsideShenhe;
import cn.explink.domain.PunishInsideReviseAndReply;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.service.PunishInsideService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Component
public class PunishInsideDao {
	
	@Autowired
	PunishInsideService punishInsideService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
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
			penalizeInside.setLastgoodpunishprice(rs.getBigDecimal("lastgoodpunishprice"));
			penalizeInside.setLastqitapunishprice(rs.getBigDecimal("lastqitapunishprice"));
			penalizeInside.setCreategoodpunishprice(rs.getBigDecimal("creategoodpunishprice"));
			penalizeInside.setCreateqitapunishprice(rs.getBigDecimal("createqitapunishprice"));
			return penalizeInside;
		}

	}
	
	private final class PenalizeInsideAutoShenheRowMapper implements RowMapper<PenalizeInside>{
		@Override
		public PenalizeInside mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PenalizeInside penalizeInside=new PenalizeInside();
			penalizeInside.setDutybranchid(rs.getLong("dutybranchid"));
			penalizeInside.setDutypersonid(rs.getLong("dutypersonid"));
			penalizeInside.setCreDate(rs.getString("creDate"));
			penalizeInside.setId(rs.getLong("id"));
			penalizeInside.setPunishInsideprice(rs.getBigDecimal("punishInsideprice"));
			penalizeInside.setCreategoodpunishprice(rs.getBigDecimal("creategoodpunishprice"));
			penalizeInside.setCreateqitapunishprice(rs.getBigDecimal("createqitapunishprice"));
			penalizeInside.setPunishInsideprice(rs.getBigDecimal("punishInsideprice"));
			penalizeInside.setPunishNo(rs.getString("punishNo"));
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
	private final class SumPriceROwMapper implements RowMapper<Double>{

		@Override
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
/*			sumPrice.setPrice();
*///			System.out.println(rs.getString("price"));
//			System.out.println(rs.getDouble(1));
//			System.out.println(rs.getLong("price"));
//			System.out.println(rs.getString(1));
			 return rs.getDouble("price");
		}
		
	}
	private final class FilePathRowMapper implements RowMapper<PenalizeInsideFilepath>{

		@Override
		public PenalizeInsideFilepath mapRow(ResultSet rs, int rowNum) throws SQLException {
			PenalizeInsideFilepath filepath=new PenalizeInsideFilepath();
			filepath.setId(rs.getLong("id"));
			filepath.setFilepath(rs.getString("shensufileposition"));
			return filepath;
		}
		
	}
	//查询所有的对内扣罚单
	public List<PenalizeInside> getAllPunishInside(){
		String sql="select * from express_ops_punishInside_detail";
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());
	}
	//根据查询条件查询对内扣罚的扣罚单
	public List<PenalizeInside> findByCondition(long page,String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate,long currentbranchid,long roleid){
		String sql="select * from express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}else {
			if (roleid!=1) {
				sql+=" And dutybranchid="+currentbranchid;
			}
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
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"+begindate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (!enddate.equals("")) {
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"+enddate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (page!=-9) {
			sql += " ORDER BY creDate DESC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());

	}
	//根据查询条件查询对内扣罚的扣罚单总单数
	public int findByConditionSum(String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate,long currentbranchid,long roleid){
		String sql="select count(1) from express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}else {
			if (roleid!=1) {
				sql+=" And dutybranchid="+currentbranchid;

			}
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
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"+begindate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (!enddate.equals("")) {
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"+enddate+"','%Y-%m-%d %H:%i:%s')";
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
	
	public PenalizeInside getInsideByPunishNo(String punishNo){
		try {
			String sql="select * from express_ops_punishInside_detail where punishNo=?";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeInsideRowMapper(), punishNo);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public void updateShensuPunishInside(final long id,final long shensutype,final String describe,final String fileposition,final long userid,final long punishcwbstate,final String shensuTime){
		this.jdbcTemplate.update("update express_ops_punishInside_detail set shensutype=?,shensudescribe=?,shensufileposition=?,shensuuserid=?,punishcwbstate=?,shensudate=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, shensutype);
					ps.setString(2, describe);
					ps.setString(3, fileposition);
					ps.setLong(4, userid);
					ps.setLong(5, punishcwbstate);
					ps.setString(6, shensuTime);
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
	public String calculateSumPrice(String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate,long currentbranchid,long roleid){
		String sql="SELECT SUM(punishInsideprice)as sumprice FROM express_ops_punishInside_detail where 1=1";
		if (cwb.length()>0) {
			sql+=" And cwb IN("+cwb+")";
		}
		if (cwbpunishtype>0) {
			sql+="  And punishcwbstate="+cwbpunishtype;
		}
		if (dutybranchid>0) {
			sql+=" And dutybranchid="+dutybranchid;
		}else {
			if (roleid!=1) {
				sql+=" And dutybranchid="+currentbranchid;
			}
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
	/**
	 * 扣款撤销,在对内扣罚申诉后重判为不成立（扣罚撤销状态）的总金额
	 * 货损赔偿 查询成立状态且货损赔偿扣罚类别的单据数据，将扣罚金额汇总即可
	 * 违纪违规扣罚 只查询成立状态且违纪违规扣罚类别的单据数据，将扣罚金额汇总即可
	 * @param cwbs
	 * @param userid
	 */
	public BigDecimal getKouFaPrice(String starttime,String endtime,long userid,long flag){
		starttime=starttime+"00:00:00";
		endtime=endtime+"23:59:59";
		Double sumprices=null;
		try {
		StringBuffer buffer=new StringBuffer();
		if (flag==1) {//扣款撤销
			buffer.append("select SUM(a.shenhepunishprice) as price ");
		}else if (flag==2) {//货损赔偿
			buffer.append("select SUM(a.lastgoodpunishprice) as price ");
		}else if (flag==3) {//违纪违规扣罚
			buffer.append("select SUM(a.lastqitapunishprice) as price ");
		}
		buffer.append("from express_ops_punishInside_detail as a where   a.dutypersonid=? and shenhedate>=? and shenhedate<=?");
		if (flag==1) {
			buffer.append(" and a.punishcwbstate="+PunishInsideStateEnum.koufachexiao.getValue());
		}else {
			buffer.append(" and a.punishcwbstate="+PunishInsideStateEnum.koufachengli.getValue());
		}
			
			sumprices = this.jdbcTemplate.queryForObject(buffer.toString(), new SumPriceROwMapper(),userid,starttime,endtime);
/*			sumprices=this.jdbcTemplate.query(buffer.toString(), new SumPriceROwMapper(),userid);
*/		} catch (DataAccessException e) {
			this.logger.error("在对内扣罚表中查询扣款撤销或货损赔偿或违纪违规扣罚时出现异常",e);
		}
		return sumprices==null?new BigDecimal("0.00"):new  BigDecimal(sumprices);

	}
	
	public List<PenalizeInsideFilepath> findPenalizeById(String ids){
		String sql="select id,shensufileposition from express_ops_punishInside_detail where id IN("+ids+")";
		try {
			return this.jdbcTemplate.query(sql,new  FilePathRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return new ArrayList<PenalizeInsideFilepath>();
		}
	}
	public void updateShensuPunishInsideAdd(final long id,final long shensutype,final String describe,final long userid,final long punishcwbstate,final String shensuTime){
		this.jdbcTemplate.update("update express_ops_punishInside_detail set shensutype=?,shensudescribe=?,shensuuserid=?,punishcwbstate=?,shensudate=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, shensutype);
					ps.setString(2, describe);
					ps.setLong(3, userid);
					ps.setLong(4, punishcwbstate);
					ps.setString(5, shensuTime);
					ps.setLong(6, id);
			}
		});
	}
	
	
	public List<PenalizeInside> getWeiShenheData(){
		String sql="select * from express_ops_punishInside_detail where punishcwbstate IN(1,2)";
		try {
			return this.jdbcTemplate.query(sql,new PenalizeInsideAutoShenheRowMapper() );
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<PenalizeInside>();
		}
	}
	/**
	 * 系统在超过设置的期限内对对内扣罚单进行自动审核为扣罚成立
	 * @param penalizeInsides
	 */
	public void autoShenheWeiKouFaChengLi(final List<PenalizeInside> penalizeInsides){
		String sql="update express_ops_punishInside_detail set shenhepunishprice=?,shenhetype=?,shenhedescribe=?,shenheuserid=?,shenhedate=?,lastgoodpunishprice=?,lastqitapunishprice=?,punishcwbstate=? where id=?";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setBigDecimal(1, penalizeInsides.get(i).getPunishInsideprice());
				ps.setLong(2, 1);
				ps.setString(3, "系统自动审核为扣罚成立");
				ps.setLong(4, -2);
				ps.setString(5, DateTimeUtil.getNowTime());
				ps.setBigDecimal(6, penalizeInsides.get(i).getCreategoodpunishprice());
				ps.setBigDecimal(7, penalizeInsides.get(i).getCreateqitapunishprice());
				ps.setLong(8, 3);
				ps.setLong(9, penalizeInsides.get(i).getId());
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return penalizeInsides.size();
			}
		});
	}
	
	/**
	 * 修改扣罚金额与扣罚责任信息（包括责任站点与责任人）
	 * @param punishInsideReviseAndReply
	 */
	public void updatekoufaPriceAndDutyInfo(final PunishInsideReviseAndReply punishInsideReviseAndReply){
		String sql="update express_ops_punishInside_detail set creategoodpunishprice=?,createqitapunishprice=?,punishInsideprice=?,dutybranchid=?,dutypersonid=? where id=?";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBigDecimal(1, punishInsideReviseAndReply.getRevisegoodprice());
				ps.setBigDecimal(2, punishInsideReviseAndReply.getReviseqitaprice());
				ps.setBigDecimal(3, punishInsideReviseAndReply.getKoufajine());
				ps.setLong(4, punishInsideReviseAndReply.getDutybranchid());
				ps.setLong(5, punishInsideReviseAndReply.getDutynameAdd());
				ps.setLong(6, punishInsideReviseAndReply.getId());
			}
		});
		
	}
	
}
