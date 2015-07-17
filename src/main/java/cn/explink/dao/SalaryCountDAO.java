/**
 *
 */
package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SalaryCount;
import cn.explink.enumutil.BatchSateEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class SalaryCountDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class SalaryCountRowMapper implements RowMapper<SalaryCount> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public SalaryCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryCount salary = new SalaryCount();
			salary.setBatchid(StringUtil.nullConvertToEmptyString(rs.getString("batchid")));
			salary.setBranchid(rs.getLong("branchid"));
			salary.setStarttime(StringUtil.nullConvertToEmptyString(rs.getString("starttime")));
			salary.setEndtime(StringUtil.nullConvertToEmptyString(rs.getString("endtime")));
			salary.setUsercount(rs.getLong("usercount"));
			salary.setUserid(rs.getLong("userid"));
			salary.setOperationTime(StringUtil.nullConvertToEmptyString(rs.getString("operationTime")));
			salary.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			salary.setBatchstate(rs.getInt("batchstate"));
			return salary;
		}
	}
	private final class StringRowMapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getString(1);
		}
		
	}
	/**
	 * @param page
	 * @param batchid
	 * @param batchstate
	 * @param branchid
	 * @param starttime
	 * @param endtime
	 * @param userid
	 * @param operationTime
	 * @param orderbyname
	 * @param orderbyway
	 * @return
	 */
	public List<SalaryCount> getSalaryCountByPage(long page, String batchid, int batchstate, String branchids, String starttime, String endtime, String userid, String operationTime, String orderbyname,
			String orderbyway) {
		String sql="select * from express_ops_salaryCount_detail where 1=1 ";
		sql +=this.creConditions(batchid, batchstate, branchids, starttime, endtime, userid, operationTime, orderbyname, orderbyway);
		sql += "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new SalaryCountRowMapper());
	}
	public int getSalaryCountByCount(String batchid, int batchstate, String branchids, String starttime, String endtime, String userid, String operationTime, String orderbyname,
			String orderbyway) {
		String sql="select count(1) from express_ops_salaryCount_detail where 1=1 ";
		sql +=this.creConditions(batchid, batchstate, branchids, starttime, endtime, userid, operationTime, orderbyname, orderbyway);
		return this.jdbcTemplate.queryForInt(sql);
	}

	private String creConditions(String batchid, int batchstate, String branchids, String starttime, String endtime, String userid, String operationTime, String orderbyname, String orderbyway) {
		String sql = "";
		if((null!=batchid)&&(batchid.length()>0))
		{
			sql+=" and batchid like '%"+batchid+"%' ";
		}
		if(batchstate>=0)
		{
			sql+=" and batchstate ="+batchstate;
		}
		if((null!=branchids)&&(branchids.length()>0))
		{
			sql+=" and branchid in ("+branchids+")";
		}
		if(((null!=starttime)&&(starttime.length()>0))&&((null!=endtime)&&(endtime.length()>0)))
		{
			sql+=" and starttime >='"+starttime +"' and endtime<='"+endtime+"'";
		}
		if((null!=userid)&&(userid.length()>0))
		{
			sql+=" and userid in ("+userid +")";
		}
		if((null!=operationTime)&&(operationTime.length()>0))
		{
			sql+=" and operationTime='"+operationTime+"'";
		}
		if((null!=orderbyname)&&(null!=orderbyway)&&(orderbyname.length()>0)&&(orderbyway.length()>0))
		{
			sql+=" order by "+orderbyname +" "+orderbyway;
		}
			return sql;
	}
	/**
	 * @param salaryCount
	 */
	public int cresalaryCount(SalaryCount salaryCount) {
		String sql="insert into express_ops_salaryCount_detail(batchid,branchid,starttime,endtime,usercount,userid,operationTime,remark,batchstate) values(?,?,?,?,?,?,?,?,?);";
		return this.jdbcTemplate.update(sql,salaryCount.getBatchid(),salaryCount.getBranchid(),salaryCount.getStarttime(),salaryCount.getEndtime(),salaryCount.getUsercount(),salaryCount.getUserid(),salaryCount.getOperationTime(),salaryCount.getRemark(),salaryCount.getBatchstate());

	}
	/**
	 * @param batchid
	 * @return
	 */
	public SalaryCount getSalaryCountBybatchid(String batchid) {
		String sql="select * from express_ops_salaryCount_detail where batchid= "+batchid;
		return this.jdbcTemplate.queryForObject(sql, new SalaryCountRowMapper());
	}
	/**
	 * @param branchid
	 * @return
	 */
	public List<String> getSalaryCountBybranchid(long branchid,String batchnum) {
		String sql="select * from express_ops_salaryCount_detail where ";
		if (branchid>0) {
			sql+=" and branchid= "+branchid;
		}
		if (!"".equals(batchnum)&&!"模糊匹配".equals(batchnum)) {
			sql+=" and batchid like '%"+batchnum+"%'";
		}
		try {
			if (sql.indexOf("and")<0) {
				return null;
			}
			return this.jdbcTemplate.query(sql, new StringRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}
	/**
	 * @param ids
	 * @return
	 */
	public long deleteSalarCountyByids(String ids) {
		String sql="delete from express_ops_salaryCount_detail where batchid in(?) and batchstate=? ";
		return this.jdbcTemplate.update(sql,ids,BatchSateEnum.Weihexiao.getValue());
	}

}
