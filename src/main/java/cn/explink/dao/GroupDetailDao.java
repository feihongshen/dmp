package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.GroupDetail;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.util.Page;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Component
public class GroupDetailDao {
	private final class GroupDetailMapper implements RowMapper<GroupDetail> {
		@Override
		public GroupDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupDetail groupDetail = new GroupDetail();
			groupDetail.setCwb(rs.getString("cwb"));
			groupDetail.setGroupid(rs.getLong("groupid"));
			groupDetail.setBaleid(rs.getLong("baleid"));
			groupDetail.setUserid(rs.getLong("userid"));
			groupDetail.setIssignprint(rs.getLong("issignprint"));
			groupDetail.setCreatetime(rs.getString("createtime"));
			groupDetail.setFlowordertype(rs.getLong("flowordertype"));
			groupDetail.setBranchid(rs.getLong("branchid"));
			groupDetail.setNextbranchid(rs.getLong("nextbranchid"));
			groupDetail.setDeliverid(rs.getLong("deliverid"));
			groupDetail.setCustomerid(rs.getLong("customerid"));
			groupDetail.setBaleno(rs.getString("baleno"));
			groupDetail.setTruckid(rs.getLong("truckid"));
			groupDetail.setDriverid(rs.getLong("driverid"));
			return groupDetail;
		}
	}

	private final class GroupDetailNotDetailMapper implements RowMapper<GroupDetail> {
		@Override
		public GroupDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupDetail groupDetail = new GroupDetail();
			groupDetail.setCwb(rs.getString("cwb"));
			groupDetail.setGroupid(rs.getLong("groupid"));
			return groupDetail;
		}
	}

	private final class GroupDetailByBaleMapper implements RowMapper<GroupDetail> {
		@Override
		public GroupDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupDetail groupDetail = new GroupDetail();
			groupDetail.setBaleid(rs.getLong("baleid"));
			groupDetail.setBaleno(rs.getString("baleno"));
			groupDetail.setNextbranchid(rs.getLong("nextbranchid"));
			groupDetail.setIssignprint(rs.getLong("issignprint"));
			groupDetail.setFlowordertype(rs.getLong("flowordertype"));
			return groupDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creGroupDetail(String cwb, long groupid, long userid, long flowordertype, long branchid, long nextbranchid, long deliverid, long customerid,long driverid,long truckid,String packagecode) {
		String sql = "insert into express_ops_groupdetail(cwb,groupid,userid,createtime,flowordertype,branchid,nextbranchid,deliverid,customerid,driverid,truckid,baleno) values(?,?,?,NOW(),?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, cwb, groupid, userid, flowordertype, branchid, nextbranchid, deliverid, customerid,driverid,truckid,packagecode);
	}

	public void saveGroupDetailByBranchidAndCwb(long userid, long nextbranchid, String cwb, long branchid, long deliverid, long customerid) {
		String sql = "update express_ops_groupdetail set userid=?,createtime=NOW(),nextbranchid=?,deliverid=?,customerid=? where issignprint=0 and cwb=? and branchid=? and groupid=0";
		jdbcTemplate.update(sql, userid, nextbranchid, deliverid, customerid, cwb, branchid);
	}

	public List<GroupDetail> getAllGroupDetailByCwb(long groupid, String cwb) {
		String sql = "select * from express_ops_groupdetail where groupid=? and cwb=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), groupid, cwb);
	}

	public void updateAndSelectByCwb(long baleid, String cwb, long groupid) {
		String sql = "update express_ops_groupdetail set baleid=? where cwb=? and groupid=?";
		jdbcTemplate.update(sql, baleid, cwb, groupid);
	}

	public List<GroupDetail> getBroupDetailForBale(long baleid, long driverid, long balestate, long branchid) {
		try {
			String sql = "select gd.* from express_ops_groupdetail gd left outer join express_ops_outwarehousegroup og on gd.groupid=og.id where gd.baleid=(select id from express_ops_bale where id=? and branchid=? and balestate=?) and og.driverid=?";
			return jdbcTemplate.query(sql, new GroupDetailMapper(), baleid, branchid, balestate, driverid);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<GroupDetail> getBroupDetailForBale(String baleno, long driverid, long balestate, long branchid) {
		try {
			String sql = "select gd.* from express_ops_groupdetail gd left outer join express_ops_outwarehousegroup og on gd.groupid=og.id where gd.baleid=(select id from express_ops_bale where baleno=? and branchid=? and balestate=?) and og.driverid=?";
			return jdbcTemplate.query(sql, new GroupDetailMapper(), baleno, branchid, balestate, driverid);
		} catch (Exception e) {
			return null;
		}
	}
	
	public long getGroupDetailCount(long userid, long emailfinishflag, long reacherrorflag) {
		String sql = "SELECT COUNT(1) FROM express_ops_groupdetail gd LEFT OUTER JOIN express_ops_cwb_detail cd ON gd.cwb=cd.cwb WHERE gd.userid=?";
		if (emailfinishflag > 0) {
			sql += " and cd.emailfinishflag=" + emailfinishflag;
		}
		if (reacherrorflag > 0) {
			sql += " and cd.reacherrorflag=" + reacherrorflag;
		}
		return jdbcTemplate.queryForLong(sql, userid);
	}

	public void saveGroupDetailUserid(long userid) {
		String sql = "update express_ops_groupdetail set userid=0 where userid=?";
		jdbcTemplate.update(sql, userid);
	}

	public List<GroupDetail> getAllGroupDetailByGroupid(long groupid) {
		String sql = "select * from express_ops_groupdetail where groupid=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), groupid);
	}

	public List<GroupDetail> getCheckGroupDetailIsExist(String cwb, long flowordertype, long branchid) {
		String sql = "select * from express_ops_groupdetail where issignprint=0 and cwb=? and groupid=0 and flowordertype=? and branchid=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), cwb, flowordertype, branchid);
	}

	public void updateGroupDetailByCwb(String cwb, long groupid) {
		String sql = "update express_ops_groupdetail set groupid=?,issignprint=? where cwb=? and issignprint=0 and branchid=0";
		jdbcTemplate.update(sql, groupid, System.currentTimeMillis(), cwb);
	}
	public void updateGroupDetailByCwb2(String cwb, long groupid) {
		String sql="update express_ops_groupdetail set groupid="+groupid+",issignprint=1 where cwb='"+cwb+"' and issignprint=0 ";
		jdbcTemplate.execute(sql);
	}

	/**
	 * 按包号查询订单list
	 * 
	 * @param baleId
	 * @param page
	 * @return
	 */
	public List<GroupDetail> getCwbListByBaleId(long baleId, long page) {
		if(baleId<1){
			return null;
		}
		String sql = "select * from express_ops_groupdetail where baleid=? ";
		sql += "limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new GroupDetailMapper(), baleId);
	}

	/**
	 * 按包号查询订单list 导出
	 * 
	 * @param baleId
	 * @return
	 */
	public List<GroupDetail> getCwbListByBaleIdExport(long baleId) {
		if(baleId<1){
			return null;
		}
		String sql = "select * from express_ops_groupdetail where baleid=? ";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), baleId);
	}
	/**
	 * 
	 * @param baleno
	 * @return
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbListByBalenoExport(long baleId) {
		if(baleId<1){
			return null;
		}
		String sql = "select * from express_ops_groupdetail where baleid=? ";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), baleId);
	}
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbListByBalenoExportByTruckid(long truckid) {
		String sql = "select * from express_ops_groupdetail where truckid=? ";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), truckid);
	}
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbListByBalenoExportBydriverid(long driverid) {
		String sql = "select * from express_ops_groupdetail where driverid=? ";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), driverid);
	}
	
	
	/**
	 * 按单号查询
	 * 
	 * @param cwb
	 * @return
	 */
	public List<String> getAllGroupDetailByCwb(String cwb) {
		String sql = "SELECT b.baleno FROM express_ops_groupdetail AS a LEFT JOIN express_ops_bale AS b ON a.baleid=b.id WHERE  a.cwb = ?";
		return jdbcTemplate.queryForList(sql, String.class, cwb);
	}

	public void delGroupDetailByCwbsAndBranchidAndFlowordertype(String cwbs, long branchid, long flowordertype) {
		String sql = "DELETE FROM express_ops_groupdetail where cwb in(" + cwbs + ")  and flowordertype=? and issignprint=0 and groupid=0 ";
		jdbcTemplate.update(sql, flowordertype);
	}
	
	/**
	 * 
	 * @param cwbs
	 * @param branchid
	 * @param flowordertype
	 */
	public void delGroupDetailByCwbsAndBranchidAndFlowordertypeForBale(String cwbs, long branchid, long flowordertype,long baleid) {
		String sql = "DELETE FROM express_ops_groupdetail where cwb in(" + cwbs + ") and branchid=? and flowordertype=? and issignprint=0 and groupid=0 and baleid=?";
		jdbcTemplate.update(sql, branchid, flowordertype,baleid);
	}
	
	/**
	 * 
	 * @param cwbs
	 * @param branchid
	 * @param flowordertypes
	 * @baleno baleno
	 */
	public void delGroupDetailByCwbsAndBranchidAndFlowordertypesForBale(String cwbs, long branchid, String flowordertypes,long baleid) {
		String sql = "DELETE FROM express_ops_groupdetail where cwb in(" + cwbs + ") and branchid=? and flowordertype in(" + flowordertypes + ") and issignprint=0 and groupid=0 and baleid=?";
		jdbcTemplate.update(sql, branchid,baleid);
	}

	public List<GroupDetail> getAllGroupDetailByGroupids(String groupids) {
		String sql = "select cwb,groupid from express_ops_groupdetail where groupid in(" + groupids + ")";
		return jdbcTemplate.query(sql, new GroupDetailNotDetailMapper());
	}

	public List<String> getAllGroupDetailByCreatetime(String begindate, String enddate) {
		String sql = "select cwb from express_ops_groupdetail where createtime >= ? and createtime <= ? and issignprint=0 and groupid=0 and branchid=0 ";

		return jdbcTemplate.queryForList(sql, String.class, begindate, enddate);
	}

	public void saveGroupDetailByCwb(String cwb, long flowordertype, long branchid, long nextbranchid, long deliverid, long customerid) {
		String sql = "update express_ops_groupdetail set flowordertype=?,branchid=?,nextbranchid=?,deliverid=?,customerid=? where cwb=? and issignprint=0 and groupid=0 and branchid=0";
		jdbcTemplate.update(sql, flowordertype, branchid, nextbranchid, deliverid, customerid, cwb);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForChuKuPrintTime(long startbranchid, long nextbranchid, int flowordertype, String strtime, String endtime) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid=? AND flowordertype=? AND issignprint=0 ";
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, nextbranchid, flowordertype);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForTuiGongYingShangPrint(long customerid, long flowordertype) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE customerid=? AND flowordertype=? AND issignprint=0";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), customerid, flowordertype);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForLingHuoPrint(long deliverid, long flowordertype, String begintime, String endtime) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE deliverid=? AND flowordertype=? AND issignprint=0 and createtime>=? and createtime<=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), deliverid, flowordertype, begintime, endtime);
	}

	/**
	 * 出库 打印 （祥） 支持下一站多选
	 * 
	 * @param startbranchid
	 * @param branchids
	 * @param flowordertype
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForChuKuPrintTimeNew(long startbranchid, String branchids, int flowordertype, String strtime, String endtime, String baleno) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid in(" + branchids + ") AND flowordertype=? AND issignprint=0 ";
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		if (!"".equals(baleno)) {
			sql += " and baleno='" + baleno + "'";
		}

		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, flowordertype);
	}
	
	public List<GroupDetail> getCwbForChuKuPrintTimeNewForBale(long startbranchid, String branchids, int flowordertype, String strtime, String endtime, String baleno) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid in(" + branchids + ") AND flowordertype=? AND issignprint=0 and baleid>0 ";
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		if (!"".equals(baleno)) {
			sql += " and baleno='" + baleno + "'";
		}

		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, flowordertype);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForChuKuPrintTimeNew2(long startbranchid, String branchids, int flowordertype, String strtime, String endtime, String baleno,long driverid,long truckid) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid in(" + branchids + ") AND flowordertype=? AND issignprint=0 AND driverid="+driverid+" AND truckid="+truckid;
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		if (!"".equals(baleno)) {
			sql += " and baleno='" + baleno + "'";
		}

		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, flowordertype);
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForChuKuPrintTimeNewByDriverid(long startbranchid, String branchids, int flowordertype, String strtime, String endtime, String baleno,long driverid) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid in(" + branchids + ") AND flowordertype=? AND issignprint=0 AND driverid="+driverid;
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		if (!"".equals(baleno)) {
			sql += " and baleno='" + baleno + "'";
		}

		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, flowordertype);
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getCwbForChuKuPrintTimeNewByTruckid(long startbranchid, String branchids, int flowordertype, String strtime, String endtime, String baleno,long truckid) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE branchid=? AND nextbranchid in(" + branchids + ") AND flowordertype=? AND issignprint=0 AND truckid="+truckid;
		if (strtime.length() > 0) {
			sql += " and createtime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and createtime<'" + endtime + "'";
		}
		if (!"".equals(baleno)) {
			sql += " and baleno='" + baleno + "'";
		}

		sql += " order by createtime desc";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), startbranchid, flowordertype);
	}
	
	
	
	
	public void updateGroupDetailByBale(long baleid, String baleno, String cwb, long branchid) {
		String sql = "update express_ops_groupdetail set baleid=? where cwb=? and branchid=? and baleno=?";
		jdbcTemplate.update(sql, baleid, cwb, branchid,baleno);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<GroupDetail> getGroupDetailListByBale(String baleno) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE baleno=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), baleno);
	}
	
	public List<GroupDetail> getGroupDetailListByBale(long baleid) {
		if(baleid<1){
			return null;
		}
		String sql = "SELECT * FROM express_ops_groupdetail WHERE baleid=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), baleid);
	}
	
	public List<Long> getBranchIdsGroupBYbranchid(String baleids) {
		String sql = "SELECT nextbranchid FROM express_ops_groupdetail WHERE baleid in("+baleids+") group by nextbranchid";
		return jdbcTemplate.queryForList(sql, Long.class);
	}
	public List<Long> getBalesBybranchid(String baleids,long branchid) {
		String sql = "SELECT DISTINCT baleid FROM express_ops_groupdetail WHERE baleid in("+baleids+") and nextbranchid="+branchid+"";
		return jdbcTemplate.queryForList(sql, Long.class);
	}

	public void updateGroupDetailListByBale(long baleid) {
		String sql = "update express_ops_groupdetail set issignprint=? where baleid=? and issignprint=0 ";
		jdbcTemplate.update(sql, System.currentTimeMillis(), baleid);
	}

	public List<GroupDetail> getGroupDetailhistoryByBale(long page, long branchid, long starttime, long endtime, long nextbranchid) {
		String sql = "SELECT DISTINCT(baleid) baleid,baleno,nextbranchid,issignprint,flowordertype FROM express_ops_groupdetail a WHERE branchid=? and baleid>0";
		if (starttime > 0) {
			sql += " and issignprint>=" + starttime;
		}
		if (endtime > 0) {
			sql += " and issignprint<=" + endtime;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		sql += " AND (issignprint>0 OR not exists(select 1 from express_ops_bale b where b.id=a.baleid and b.balestate in (?,?,?)))";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new GroupDetailByBaleMapper(), branchid
				,BaleStateEnum.YiFengBao.getValue(),BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
				);
	}

	public long getGroupDetailhistoryByBaleCount(long branchid, long starttime, long endtime, long nextbranchid) {
		String sql = "SELECT COUNT(DISTINCT(baleid)) FROM express_ops_groupdetail a WHERE branchid=? and baleid>0";
		if (starttime > 0) {
			sql += " and issignprint>=" + starttime;
		}
		if (endtime > 0) {
			sql += " and issignprint<=" + endtime;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		sql += " AND (issignprint>0 OR not exists(select 1 from express_ops_bale b where b.id=a.baleid and b.balestate in (?,?,?)))";

		return jdbcTemplate.queryForLong(sql, branchid
				,BaleStateEnum.YiFengBao.getValue(),BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue()
				);
	}
	
	public List<GroupDetail> getGroupDetailListByCwb(String cwb) {
		String sql = "SELECT * FROM express_ops_groupdetail WHERE cwb=?";
		return jdbcTemplate.query(sql, new GroupDetailMapper(), cwb);
	}
	
	/**
	 * 获取订单号中出库时间最大最小值  add by vic.liang@pjbest.com 2016-08-11
	 * @param cwbs
	 * @return
	 */
	public Map<String,Object> getGroupDetailDateTime (String cwbs) {
		String sql = "select max(createtime) as endtime, min(createtime) as starttime from express_ops_groupdetail where cwb in("+cwbs+")";
		try {
			return this.jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException e) {
			return null;
		}
	}
	
}
