package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.ExpressOpsPunishinsideBill;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.VO.ExpressOpsPunishinsideBillVO;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.service.PunishInsideService;
import cn.explink.util.Page;

@Component
public class PunishinsideBillDAO {
	@Autowired
	PunishInsideService punishInsideService;

	private final class PunishinsideBillMapper implements
			RowMapper<ExpressOpsPunishinsideBill> {
		@Override
		public ExpressOpsPunishinsideBill mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressOpsPunishinsideBill punishinsideBill = new ExpressOpsPunishinsideBill();
			punishinsideBill.setId(rs.getInt("id"));
			punishinsideBill.setBillBatch(rs.getString("billBatch"));
			punishinsideBill.setBillState(rs.getInt("billState"));
			punishinsideBill.setDutybranchid(rs.getInt("dutybranchid"));
			punishinsideBill.setDutypersonid(rs.getInt("dutypersonid"));
			punishinsideBill.setSumPrice(rs.getBigDecimal("sumPrice"));
			punishinsideBill.setCreator(rs.getInt("creator"));
			punishinsideBill.setCreateDate(rs.getString("createDate"));
			punishinsideBill.setShenHePerson(rs.getInt("shenHePerson"));
			punishinsideBill.setShenHeDate(rs.getString("shenHeDate"));
			punishinsideBill.setCheXiaoPerson(rs.getInt("cheXiaoPerson"));
			punishinsideBill.setCheXiaoDate(rs.getString("cheXiaoDate"));
			punishinsideBill.setHeXiaoPerson(rs.getInt("heXiaoPerson"));
			punishinsideBill.setHeXiaoDate(rs.getString("heXiaoDate"));
			punishinsideBill.setQuXiaoHeXiaoPerson(rs
					.getInt("quXiaoHeXiaoPerson"));
			punishinsideBill.setQuXiaoHeXiaoDate(rs
					.getString("quXiaoHeXiaoDate"));
			punishinsideBill.setPunishbigsort(rs.getInt("punishbigsort"));
			punishinsideBill.setPunishsmallsort(rs.getInt("punishsmallsort"));
			punishinsideBill.setPunishInsideRemark(rs
					.getString("punishInsideRemark"));
			punishinsideBill
					.setPunishInsideIds(rs.getString("punishInsideIds"));
			punishinsideBill.setPunishNos(rs.getString("punishNos"));
			punishinsideBill.setPunishNoCreateBeginDate(rs
					.getString("punishNoCreateBeginDate"));
			punishinsideBill.setPunishNoCreateEndDate(rs
					.getString("punishNoCreateEndDate"));

			return punishinsideBill;
		}
	}

	private final class PenalizeInsideRowMapper implements
			RowMapper<PenalizeInside> {
		@Override
		public PenalizeInside mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PenalizeInside penalizeInside = new PenalizeInside();
			penalizeInside.setCreateBySource(rs.getLong("createBySource"));
			penalizeInside.setCreateBysourcename(punishInsideService
					.getCreateSource(rs.getLong("createBySource")));
			penalizeInside.setCreateuserid(rs.getLong("createuserid"));
			penalizeInside.setCreUserName(punishInsideService.getCreUser(rs
					.getLong("createuserid")));
			penalizeInside.setCreDate(rs.getString("creDate"));
			penalizeInside.setCwb(rs.getString("cwb"));
			penalizeInside.setCwbPrice(rs.getBigDecimal("cwbPrice"));
			penalizeInside.setCwbstate(rs.getLong("cwbstate"));
			penalizeInside.setCwbstatename(punishInsideService
					.getFlowOrdertype(rs.getLong("cwbstate")));
			penalizeInside.setDutybranchid(rs.getLong("dutybranchid"));
			penalizeInside.setDutybranchname(punishInsideService
					.getBranchName(rs.getLong("dutybranchid")));
			penalizeInside.setDutypersonid(rs.getLong("dutypersonid"));
			penalizeInside.setDutypersonname(punishInsideService.getCreUser(rs
					.getLong("dutypersonid")));
			penalizeInside.setFileposition(rs.getString("fileposition"));
			penalizeInside.setId(rs.getLong("id"));
			penalizeInside.setPunishbigsort(rs.getLong("punishbigsort"));
			penalizeInside.setPunishcwbstate(rs.getInt("punishcwbstate"));
			penalizeInside.setPunishcwbstatename(punishInsideService
					.getPunishState(rs.getInt("punishcwbstate")));
			penalizeInside.setPunishdescribe(rs.getString("punishdescribe"));
			penalizeInside.setPunishInsideprice(rs
					.getBigDecimal("punishInsideprice"));
			penalizeInside.setPunishNo(rs.getString("punishNo"));
			penalizeInside.setPunishsmallsort(rs.getLong("punishsmallsort"));
			penalizeInside.setSourceNo(rs.getString("sourceNo"));
			penalizeInside.setShensutype(rs.getLong("shensutype"));
			penalizeInside.setShensudescribe(rs.getString("shensudescribe"));
			penalizeInside.setShensufileposition(rs
					.getString("shensufileposition"));
			penalizeInside.setShensuuserid(rs.getLong("shensuuserid"));
			penalizeInside.setShenhedescribe(rs.getString("shenhedescribe"));
			penalizeInside.setShenhefileposition(rs
					.getString("shenhefileposition"));
			penalizeInside.setShenhepunishprice(rs
					.getBigDecimal("shenhepunishprice"));
			penalizeInside.setShenhetype(rs.getLong("shenhetype"));
			penalizeInside.setShenheuserid(rs.getLong("shenheuserid"));
			penalizeInside.setShensudate(rs.getString("shensudate"));
			penalizeInside.setShenhedate(rs.getString("shenhedate"));
			penalizeInside.setPunishbigsortname(punishInsideService
					.getSortname(Integer.parseInt(rs.getLong("punishbigsort")
							+ "")));
			penalizeInside.setPunishsmallsortname(punishInsideService
					.getSortname(Integer.parseInt(rs.getLong("punishsmallsort")
							+ "")));
			return penalizeInside;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createPunishinsideBill(
			final ExpressOpsPunishinsideBill punishinsideBill) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_ops_punishinside_bill("
								+ "billBatch,billState,dutybranchid,dutypersonid,sumPrice,"
								+ "creator,createDate,shenHePerson,shenHeDate,cheXiaoPerson,"
								+ "cheXiaoDate,heXiaoPerson,heXiaoDate,quXiaoHeXiaoPerson,"
								+ "quXiaoHeXiaoDate,punishbigsort,punishsmallsort,punishInsideRemark,"
								+ "punishInsideIds,punishNos,punishNoCreateBeginDate,punishNoCreateEndDate"
								+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;

				ps.setString(i++, punishinsideBill.getBillBatch());
				ps.setInt(i++, punishinsideBill.getBillState());
				ps.setInt(i++, punishinsideBill.getDutybranchid());
				ps.setInt(i++, punishinsideBill.getDutypersonid());
				ps.setBigDecimal(i++, punishinsideBill.getSumPrice());
				ps.setInt(i++, punishinsideBill.getCreator());
				ps.setString(i++, punishinsideBill.getCreateDate());
				ps.setInt(i++, punishinsideBill.getShenHePerson());
				ps.setString(i++, punishinsideBill.getShenHeDate());
				ps.setInt(i++, punishinsideBill.getCheXiaoPerson());
				ps.setString(i++, punishinsideBill.getCheXiaoDate());
				ps.setInt(i++, punishinsideBill.getHeXiaoPerson());
				ps.setString(i++, punishinsideBill.getHeXiaoDate());
				ps.setInt(i++, punishinsideBill.getQuXiaoHeXiaoPerson());
				ps.setString(i++, punishinsideBill.getQuXiaoHeXiaoDate());
				ps.setInt(i++, punishinsideBill.getPunishbigsort());
				ps.setInt(i++, punishinsideBill.getPunishsmallsort());
				ps.setString(i++, punishinsideBill.getPunishInsideRemark());
				ps.setString(i++, punishinsideBill.getPunishInsideIds());
				ps.setString(i++, punishinsideBill.getPunishNos());
				ps.setString(i++, punishinsideBill.getPunishNoCreateBeginDate());
				ps.setString(i++, punishinsideBill.getPunishNoCreateEndDate());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updatePunishinsideBill(
			final ExpressOpsPunishinsideBill punishinsideBill) {

		this.jdbcTemplate
				.update("update express_ops_punishinside_bill set "
						+ "billState=?,sumPrice=?,shenHePerson=?,shenHeDate=?,cheXiaoPerson=?,"
						+ "cheXiaoDate=?,heXiaoPerson=?,heXiaoDate=?,quXiaoHeXiaoPerson=?,quXiaoHeXiaoDate=?,"
						+ "punishInsideRemark=?,punishInsideIds=?,punishNos=? where id=?",
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								int i = 1;

								ps.setInt(i++, punishinsideBill.getBillState());
								ps.setBigDecimal(i++,
										punishinsideBill.getSumPrice());
								ps.setInt(i++,
										punishinsideBill.getShenHePerson());
								ps.setString(i++,
										punishinsideBill.getShenHeDate());
								ps.setInt(i++,
										punishinsideBill.getCheXiaoPerson());
								ps.setString(i++,
										punishinsideBill.getCheXiaoDate());
								ps.setInt(i++,
										punishinsideBill.getHeXiaoPerson());
								ps.setString(i++,
										punishinsideBill.getHeXiaoDate());
								ps.setInt(i++, punishinsideBill
										.getQuXiaoHeXiaoPerson());
								ps.setString(i++,
										punishinsideBill.getQuXiaoHeXiaoDate());
								ps.setString(i++, punishinsideBill
										.getPunishInsideRemark());
								ps.setString(i++,
										punishinsideBill.getPunishInsideIds());
								ps.setString(i++,
										punishinsideBill.getPunishNos());
								ps.setInt(i++, punishinsideBill.getId());
							}
						});
	}
	
	public void updatePunishinsideBill( final String punishNos, final BigDecimal sumPrice, final int id) {
		
		this.jdbcTemplate
		.update("update express_ops_punishinside_bill set "
				+ "punishNos=?,sumPrice=? where id=?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, punishNos);
						ps.setBigDecimal(2, sumPrice);
						ps.setInt(3, id);
					}
				});
	}

	public int deletePunishinsideBill(String ids) {
		String sql = "delete from express_ops_punishinside_bill where id in (" + ids + ")";
		return this.jdbcTemplate.update(sql);
	}

	public List<ExpressOpsPunishinsideBill> getPunishinsideBillList() {
		String sql = "select * from express_ops_punishinside_bill";
		return jdbcTemplate.query(sql, new PunishinsideBillMapper());
	}

	public ExpressOpsPunishinsideBill getPunishinsideBillListById(int id) {
		try {
			String sql = "select * from express_ops_punishinside_bill where id=?";
			return jdbcTemplate.queryForObject(sql, new PunishinsideBillMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<ExpressOpsPunishinsideBill> queryPunishinsideBill(
			long page, ExpressOpsPunishinsideBillVO punishinsideBillVO) {

		String sql = "select * from express_ops_punishinside_bill pb "
				+ " left join express_set_branch b on pb.dutybranchid = b.branchid "
				+ " left join express_set_user u on pb.dutypersonid = u.userid "
				+ " where 1=1 ";

		if (punishinsideBillVO != null) {
			if (StringUtils.isNotBlank(punishinsideBillVO.getBillBatch())) {
				sql += " and pb.billBatch like '%"
						+ punishinsideBillVO.getBillBatch() + "%' ";
			}
			if (punishinsideBillVO.getBillState() != 0) {
				sql += " and pb.billState= '"
						+ punishinsideBillVO.getBillState() + "' ";
			}

			if (StringUtils.isNotBlank(punishinsideBillVO.getDutybranchname())) {
				sql += " and b.branchname like '%"
						+ punishinsideBillVO.getDutybranchname() + "%' ";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getDutypersonname())) {
				sql += " and u.realname like '%"
						+ punishinsideBillVO.getDutypersonname() + "%' ";
			}

			if (punishinsideBillVO.getPunishbigsort() != 0) {
				sql += " and pb.punishbigsort= '"
						+ punishinsideBillVO.getPunishbigsort() + "' ";
			}
			if (punishinsideBillVO.getPunishsmallsort() != 0) {
				sql += " and pb.punishsmallsort= '"
						+ punishinsideBillVO.getPunishsmallsort() + "' ";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getCreateDateFrom())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ punishinsideBillVO.getCreateDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getCreateDateTo())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ punishinsideBillVO.getCreateDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getHeXiaoDateFrom())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ punishinsideBillVO.getHeXiaoDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getHeXiaoDateTo())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ punishinsideBillVO.getHeXiaoDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getContractColumn())) {
				if(punishinsideBillVO.getContractColumn().equalsIgnoreCase("dutybranchname")){
					sql += " order by b.branchname";
				} else if(punishinsideBillVO.getContractColumn().equalsIgnoreCase("dutypersonname")){
					sql += " order by u.realname";
				} else {
					sql += " order by pb." + punishinsideBillVO.getContractColumn();
				}
			}
			if (StringUtils.isNotBlank(punishinsideBillVO
					.getContractColumnOrder())) {
				sql += " " + punishinsideBillVO.getContractColumnOrder();
			}
		}
		
		if(sql.indexOf("order by") > -1) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " order by id desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}

		return jdbcTemplate.query(sql, new PunishinsideBillMapper());
	}
	
	public int queryPunishinsideBillCount(
			ExpressOpsPunishinsideBillVO punishinsideBillVO) {
		
		String sql = "select count(1) from express_ops_punishinside_bill pb "
				+ " left join express_set_branch b on pb.dutybranchid = b.branchid "
				+ " left join express_set_user u on pb.dutypersonid = u.userid "
				+ " where 1=1 ";
		
		if (punishinsideBillVO != null) {
			if (StringUtils.isNotBlank(punishinsideBillVO.getBillBatch())) {
				sql += " and pb.billBatch like '%"
						+ punishinsideBillVO.getBillBatch() + "%' ";
			}
			if (punishinsideBillVO.getBillState() != 0) {
				sql += " and pb.billState= '"
						+ punishinsideBillVO.getBillState() + "' ";
			}
			
			if (StringUtils.isNotBlank(punishinsideBillVO.getDutybranchname())) {
				sql += " and b.branchname like '%"
						+ punishinsideBillVO.getDutybranchname() + "%' ";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getDutypersonname())) {
				sql += " and u.realname like '%"
						+ punishinsideBillVO.getDutypersonname() + "%' ";
			}
			
			if (punishinsideBillVO.getPunishbigsort() != 0) {
				sql += " and pb.punishbigsort= '"
						+ punishinsideBillVO.getPunishbigsort() + "' ";
			}
			if (punishinsideBillVO.getPunishsmallsort() != 0) {
				sql += " and pb.punishsmallsort= '"
						+ punishinsideBillVO.getPunishsmallsort() + "' ";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getCreateDateFrom())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ punishinsideBillVO.getCreateDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getCreateDateTo())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ punishinsideBillVO.getCreateDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getHeXiaoDateFrom())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ punishinsideBillVO.getHeXiaoDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getHeXiaoDateTo())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ punishinsideBillVO.getHeXiaoDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(punishinsideBillVO.getContractColumn())) {
				if(punishinsideBillVO.getContractColumn().equalsIgnoreCase("dutybranchname")){
					sql += " order by b.branchname";
				} else if(punishinsideBillVO.getContractColumn().equalsIgnoreCase("dutypersonname")){
					sql += " order by u.realname";
				} else {
					sql += " order by pb." + punishinsideBillVO.getContractColumn();
				}
			}
			if (StringUtils.isNotBlank(punishinsideBillVO
					.getContractColumnOrder())) {
				sql += " " + punishinsideBillVO.getContractColumnOrder();
			}
		}
		
		return jdbcTemplate.queryForInt(sql);
	}

	// 根据查询条件查询对内扣罚的扣罚单
	public List<PenalizeInside> findByCondition(long punishbigsort,
			long punishsmallsort, String punishNos, long dutybranchid,
			long dutypersonid, String punishNoCreateBeginDate,
			String punishNoCreateEndDate, int punishcwbstate, long page) {
		String sql = "select * from express_ops_punishInside_detail where 1=1 ";

		if (punishbigsort > 0) {
			sql += " And punishbigsort=" + punishbigsort;
		}
		if (punishsmallsort > 0) {
			sql += " And punishsmallsort=" + punishsmallsort;
		}
		if (StringUtils.isNotBlank(punishNos)) {
			sql += " And punishNo IN(" + punishNos + ")";
		}
		if (dutybranchid > 0) {
			sql += " And dutybranchid=" + dutybranchid;
		}
		if (dutypersonid > 0) {
			sql += " And dutypersonid=" + dutypersonid;
		}
		if (StringUtils.isNotBlank(punishNoCreateBeginDate)) {
			sql += " And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"+punishNoCreateBeginDate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (StringUtils.isNotBlank(punishNoCreateEndDate)) {
			sql += " And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"+punishNoCreateEndDate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (punishcwbstate != -1) {
			sql += " And punishcwbstate=" + punishcwbstate;
		}
		if (page != -9) {
			sql += " ORDER BY creDate DESC limit "
					+ ((page - 1) * Page.ONE_PAGE_NUMBER) + " ,"
					+ Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());

	}

	//根据查询条件查询对内扣罚的扣罚单
	public List<PenalizeInside> findByCondition(long page,String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate,String existedPunishNos){
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
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"+begindate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (!enddate.equals("")) {
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"+enddate+"','%Y-%m-%d %H:%i:%s')";
		}
		if(StringUtils.isNotBlank(existedPunishNos)){
			sql+=" And punishNo not in ("+existedPunishNos+") ";
		}
		if (page!=-9) {
			sql += " ORDER BY creDate DESC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new PenalizeInsideRowMapper());

	}
	
	//根据查询条件查询对内扣罚的扣罚单总单数
	public int findByConditionSum(String cwb,long dutybranchid,long cwbpunishtype,long dutynameid,long cwbstate,long punishbigsort,long punishsmallsort,String begindate,String enddate,String existedPunishNos){
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
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"+begindate+"','%Y-%m-%d %H:%i:%s')";
		}
		if (!enddate.equals("")) {
			sql+=" And DATE_FORMAT(creDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"+enddate+"','%Y-%m-%d %H:%i:%s')";
		}
		if(StringUtils.isNotBlank(existedPunishNos)){
			sql+=" And punishNo not in ("+existedPunishNos+") ";
		}
		return this.jdbcTemplate.queryForInt(sql);
		
	}
		
	public List<ExpressOpsPunishinsideBill> getAllAuditedBill() {
		String sql = "select * from express_ops_punishinside_bill where billState !=" + PunishBillStateEnum.WeiShenHe.getValue() + " order by billBatch desc";
		return jdbcTemplate.query(sql, new PunishinsideBillMapper());
	}
	
	public ExpressOpsPunishinsideBill getMaxBillBatch(String billBatch) {
		String sql = "select * from express_ops_punishinside_bill where billBatch like '%" + billBatch + "%' order by billBatch desc limit 0,1";
		try{
			return jdbcTemplate.queryForObject(sql, new PunishinsideBillMapper());
		}catch (EmptyResultDataAccessException e) {
            return null;   
        }
	}

	public BigDecimal calculateSumPrice(String punishNos){
		String sql="SELECT SUM(punishInsideprice)as sumprice FROM express_ops_punishInside_detail where 1=1";
		if (StringUtils.isNotBlank(punishNos)) {
			sql+=" And punishNo IN("+punishNos+")";
		}
		return this.jdbcTemplate.queryForObject(sql, BigDecimal.class);
	}
	
	public List<ExpressOpsPunishinsideBill> getPunishinsideBillByChukuDate(
			String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new PunishinsideBillMapper(), begindate,
				enddate);
	}

	public long getPunishinsideBillByChukuDateCount(String begindate,
			String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public ExpressOpsPunishinsideBill getPunishinsideBillOneByPunishinsideBillnoLock(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql,
					new PunishinsideBillMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

}
