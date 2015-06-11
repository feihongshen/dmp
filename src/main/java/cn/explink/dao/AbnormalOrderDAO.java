package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.b2c.lefeng.result;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Page;

@Component
public class AbnormalOrderDAO {

	private final class AbnormalOrderRowMapper implements RowMapper<AbnormalOrder> {
		@Override
		public AbnormalOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			AbnormalOrder abnormalOrder = new AbnormalOrder();

			abnormalOrder.setId(rs.getLong("id"));
			abnormalOrder.setDescribe(rs.getString("describe"));
			abnormalOrder.setCreuserid(rs.getLong("creuserid"));
			abnormalOrder.setCredatetime(rs.getString("credatetime"));
			abnormalOrder.setAbnormaltypeid(rs.getLong("abnormaltypeid"));
			abnormalOrder.setOpscwbid(rs.getLong("opscwbid"));
			abnormalOrder.setIshandle(rs.getLong("ishandle"));
			abnormalOrder.setBranchid(rs.getLong("branchid"));
			abnormalOrder.setIsnow(rs.getLong("isnow"));
			abnormalOrder.setCwb(rs.getString("cwb"));
			abnormalOrder.setCustomerid(rs.getLong("customerid"));
			abnormalOrder.setEmaildate(rs.getString("emaildate"));
			abnormalOrder.setFlowordertype(rs.getLong("flowordertype"));
			abnormalOrder.setDeliverybranchid(rs.getLong("deliverybranchid"));
			abnormalOrder.setDutypersonid(rs.getLong("dutypersonid"));
			abnormalOrder.setLosebackid(rs.getLong("losebackid"));
			abnormalOrder.setDealresult(rs.getString("dealresult"));
			abnormalOrder.setCwbordertypeid(rs.getString("cwbordertypeid"));
			abnormalOrder.setResultdealcontent(rs.getString("resultdealcontent"));
			abnormalOrder.setFileposition(rs.getString("fileposition"));
			abnormalOrder.setIsfind(rs.getLong("isfind"));
			abnormalOrder.setDutybrachid(rs.getLong("dutybrachid"));
			abnormalOrder.setHandleBranch(rs.getLong("handleBranch"));
			abnormalOrder.setQuestionno(rs.getString("questionno"));
			abnormalOrder.setIsfine(rs.getLong("isfine"));
			abnormalOrder.setLastdutybranchid(rs.getLong("lastdutybranchid"));
			abnormalOrder.setLastdutyuserid(rs.getLong("lastdutyuserid"));
			
			return abnormalOrder;
		}
	}

	private final class AbnormalOrderJsonRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getLong("id"));
			obj.put("describe", rs.getString("describe"));
			obj.put("creuserid", rs.getLong("creuserid"));
			obj.put("credatetime", rs.getString("credatetime"));
			obj.put("abnormaltypeid", rs.getLong("abnormaltypeid"));
			obj.put("opscwbid", rs.getLong("opscwbid"));
			obj.put("ishandle", rs.getLong("ishandle"));
			obj.put("branchid", rs.getLong("branchid"));
			obj.put("isnow", rs.getLong("isnow"));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("emaildate", rs.getString("emaildate"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("deliverybranchid", rs.getLong("deliverybranchid"));
			obj.put("fileposition", rs.getString("fileposition")==null?"":rs.getString("fileposition"));
			obj.put("dealresult", rs.getString("dealresult"));
			obj.put("losebackid", rs.getString("losebackid"));
			obj.put("cwbordertypeid", rs.getLong("cwbordertypeid"));
			obj.put("dutybrachid", rs.getLong("dutybrachid"));
			obj.put("isfine", rs.getLong("isfine"));
			obj.put("resultdealcontent", rs.getString("resultdealcontent"));
			obj.put("questionno", rs.getString("questionno"));
			obj.put("dutypersonid", rs.getLong("dutypersonid"));
			obj.put("handleBranch", rs.getLong("handleBranch"));
			obj.put("isfind", rs.getLong("isfind"));
			obj.put("lastdutybranchid", rs.getLong("lastdutybranchid"));
			obj.put("lastdutyuserid", rs.getLong("lastdutyuserid"));
			return obj;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AbnormalOrder getAbnormalOrderById(long id) {
		try {
			String sql = "select * from express_ops_abnormal_order where `id`=?  ";
			return this.jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public List<AbnormalOrder> getAbnormalOrderByOpscwbid(long opscwbid) {
		String sql = "select * from express_ops_abnormal_order where `opscwbid`=?  ";
		return this.jdbcTemplate.query(sql, new AbnormalOrderRowMapper(), opscwbid);
	}

	public AbnormalOrder getAbnormalOrderByOpscwbidForObject(long opscwbid) {
		String sql = "select * from express_ops_abnormal_order where `opscwbid`=? ";
		AbnormalOrder ab = new AbnormalOrder();
		try {
			ab = this.jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), opscwbid);
		} catch (Exception e) {
			ab = null;
		}
		return ab;
	}

	/*
	 * public List<String> getAbnormalOrderGetIds(long abnormaltypeid){ String
	 * sql =
	 * "select id from express_ops_abnormal_order where `abnormaltypeid`="+
	 * abnormaltypeid+" and `isnow`=1 "; return jdbcTemplate.queryForList(sql,
	 * String.class); }
	 */
	
	//修改带文件的已创建的问题件
	public void saveAbnormalOrderByid(long id, long abnormaltypeid, String describe,long ishandle,String filepathsum,String nowtime) {
		String sql = "update express_ops_abnormal_order set `abnormaltypeid`=?,`describe`=?,`ishandle`=?,`fileposition`=?,`handletime`=? where `id`=?";
		this.jdbcTemplate.update(sql, abnormaltypeid, describe,ishandle,filepathsum,nowtime,id);
	}
	//修改不带文件的已创建的问题件
	public void saveAbnormalOrderByidAdd(long id, long abnormaltypeid, String describe,long ishandle,String nowtime) {
		String sql = "update express_ops_abnormal_order set `abnormaltypeid`=?,`describe`=?,`ishandle`=?,`handletime`=? where `id`=?";
		this.jdbcTemplate.update(sql, abnormaltypeid, describe,ishandle,nowtime,id);
	}
	//结案修改信息
	public void saveAbnormalOrderResultByid(long id, long dealresult, String filepathsum,long ishandle,String resultdealcontent,String nowtime) {
		try {
			String sql = "update express_ops_abnormal_order set `dealresult`=?,`fileposition`=?,`ishandle`=?,`resultdealcontent`=?,`handletime`=? where `id`=?";
			this.jdbcTemplate.update(sql, dealresult,filepathsum,ishandle,resultdealcontent,nowtime,id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveAbnormalOrderByOpscwbid(long opscwbid) {
		this.jdbcTemplate.update("update express_ops_abnormal_order set `isnow`='0' where `opscwbid`=?", opscwbid);
	}

	public void saveAbnormalOrderByOpscwb(String cwb) {
		this.jdbcTemplate.update("update express_ops_abnormal_order set `isnow`='0' where `cwb`=?", cwb);
	}

	public void creAbnormalOrder(long opscwbid, long customerid, String describe, long creuserid, long branchid, long abnormaltypeid, String credatetime) {
		this.saveAbnormalOrderByOpscwbid(opscwbid);

		String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`) values(?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, opscwbid, customerid, describe, creuserid, branchid, abnormaltypeid, credatetime, 1);
	}

	public long creAbnormalOrderLong(final long opscwbid, final long customerid, final String describe, final long creuserid, final long branchid, final long abnormaltypeid, final String credatetime) {
		this.saveAbnormalOrderByOpscwbid(opscwbid);
		final String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`) values(?,?,?,?,?,?,?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, opscwbid);
				ps.setLong(2, customerid);
				ps.setString(3, describe);
				ps.setLong(4, creuserid);
				ps.setLong(5, branchid);
				ps.setLong(6, abnormaltypeid);
				ps.setString(7, credatetime);
				ps.setLong(8, 1);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long creAbnormalOrderLong(final CwbOrder co, final String describe, final long creuserid, final long branchid, final long abnormaltypeid, final String credatetime) {
		this.saveAbnormalOrderByOpscwb(co.getCwb());
		final String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`,`emaildate`,`flowordertype`,`deliverybranchid`,`cwb`) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, co.getOpscwbid());
				ps.setLong(2, co.getCustomerid());
				ps.setString(3, describe);
				ps.setLong(4, creuserid);
				ps.setLong(5, branchid);
				ps.setLong(6, abnormaltypeid);
				ps.setString(7, credatetime);
				ps.setLong(8, 1);
				ps.setString(9, co.getEmaildate());
				ps.setLong(10, co.getFlowordertype());
				ps.setLong(11, co.getDeliverybranchid());
				ps.setString(12, co.getCwb());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long creAbnormalOrderLong(final CwbOrder co, final String describe, final long creuserid, final long branchid, final long abnormaltypeid, final String credatetime, final long handleBranch,final String name,final String questionNo) {
		this.saveAbnormalOrderByOpscwb(co.getCwb());
		final String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`,`emaildate`,`flowordertype`,`deliverybranchid`,`cwb`,`handleBranch`,`fileposition`,`questionno`,`cwbordertypeid`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, co.getOpscwbid());
				ps.setLong(2, co.getCustomerid());
				ps.setString(3, describe);
				ps.setLong(4, creuserid);
				ps.setLong(5, branchid);
				ps.setLong(6, abnormaltypeid);
				ps.setString(7, credatetime);
				ps.setLong(8, 1);
				ps.setString(9, co.getEmaildate());
				ps.setLong(10, co.getFlowordertype());
				ps.setLong(11, co.getDeliverybranchid());
				ps.setString(12, co.getCwb());
				ps.setLong(13, handleBranch);
				ps.setString(14, name);
				ps.setString(15,questionNo);
				ps.setLong(16, co.getCwbordertypeid());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}
	public long creAbnormalOrderLongAdd(final CwbOrder co, final String describe, final long creuserid, final long branchid, final long abnormaltypeid, final String credatetime, final long handleBranch,final String name,final String questionNo,final long isfind,final long ishandle) {
		this.saveAbnormalOrderByOpscwb(co.getCwb());
		final String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`,`emaildate`,`flowordertype`,`deliverybranchid`,`cwb`,`handleBranch`,`fileposition`,`questionno`,`cwbordertypeid`,`isfind`,`ishandle`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, co.getOpscwbid());
				ps.setLong(2, co.getCustomerid());
				ps.setString(3, describe);
				ps.setLong(4, creuserid);
				ps.setLong(5, branchid);
				ps.setLong(6, abnormaltypeid);
				ps.setString(7, credatetime);
				ps.setLong(8, 1);
				ps.setString(9, co.getEmaildate());
				ps.setLong(10, co.getFlowordertype());
				ps.setLong(11, co.getDeliverybranchid());
				ps.setString(12, co.getCwb());
				ps.setLong(13, handleBranch);
				ps.setString(14, name);
				ps.setString(15,questionNo);
				ps.setLong(16, co.getCwbordertypeid());
				ps.setLong(17, isfind);
				ps.setLong(18, ishandle);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<AbnormalOrder> getAbnormalOrderByWhere(long page, String begindate, String enddate, long ishandle, long abnormaltypeid, long creuserid) {
		String sql = "select * from express_ops_abnormal_order where creuserid=? " + " and credatetime >= '" + begindate + "' and credatetime <= '" + enddate + "' ";
		if (ishandle > -1) {
			sql += " and ishandle =" + ishandle;
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new AbnormalOrderRowMapper(), creuserid);
	}

	public long getAbnormalOrderCount(String begindate, String enddate, long ishandle, long abnormaltypeid, long creuserid) {
		String sql = "select count(1) from express_ops_abnormal_order where creuserid=? " + " and credatetime >= ?  and credatetime <=? ";
		if (ishandle > -1) {
			sql += " and ishandle =" + ishandle;
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}
		return this.jdbcTemplate.queryForLong(sql, creuserid, begindate, enddate);
	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndNohandles(String chuangjianbegindate, String chuangjianenddate, String cwbs, long branchid,
			long abnormaltypeid, long ishandle, long customerid, long handleBranch) {
		String sql = "SELECT *  from `express_ops_abnormal_order`   " + "WHERE   " + "credatetime >= '" + chuangjianbegindate + "' " + "and credatetime <= '" + chuangjianenddate + "' ";
		if (cwbs.length() > 0) {
			sql += " AND `cwb` IN(" + cwbs + ")";
		}
		if (branchid > 0) {
			sql += " AND `branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND `ishandle`=" + ishandle;
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		sql += " AND `handleBranch` =" + handleBranch;
		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());

	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandles(String begindate, String enddate, String cwbs, long branchid, long abnormaltypeid, long ishandle,
			long customerid, long handleBranch) {
		String sql = "SELECT *  from `express_ops_abnormal_order`   " + "WHERE   " + "handletime >= '" + begindate + "' " + "and handletime <= '" + enddate + "' ";
		if (cwbs.length() > 0) {
			sql += " AND `cwb` IN(" + cwbs + ")";
		}
		if (branchid > 0) {
			sql += " AND `branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND `ishandle`=" + ishandle;
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		sql += " AND `handleBranch` =" + handleBranch;
		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());

	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleExport(String opscwbids, String cwb, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT ao.*,cd.`cwb`,cd.`customerid`,cd.`emaildate`,cd.`flowordertype`,cd.`deliverybranchid` FROM `express_ops_abnormal_order` ao "
				+ "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid`  WHERE ao.isnow=1 and cd.state=1" + " and ao.`opscwbid` in(" + opscwbids + ")";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());

	}

	public long getAbnormalOrderAndCwbdetailCount(String cwb, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT count(1) FROM `express_ops_abnormal_order` ao " + "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid` WHERE  1=1 ";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return this.jdbcTemplate.queryForLong(sql);

	}

	public void saveAbnormalOrderForIshandleAdd(long id, long ishandle, String handletime,String fileposition,long dutyname,long dutybranchid) {
		String sql = "update express_ops_abnormal_order set `ishandle`=? ,`handletime`=?,`fileposition`=?,`dutybrachid`=?,`dutypersonid`=? where `id`=? ";
		this.jdbcTemplate.update(sql, ishandle, handletime, fileposition,dutybranchid,dutyname,id);
	}
	public void saveAbnormalOrderForIshandle(long id, long ishandle, String handletime,String fileposition) {
		String sql = "update express_ops_abnormal_order set `ishandle`=? ,`handletime`=?,`fileposition`=? where `id`=? ";
		this.jdbcTemplate.update(sql, ishandle, handletime, fileposition,id);
	}
	public void saveAbnormalOrderForIshandleAdd(long id, long ishandle, String handletime) {
		String sql = "update express_ops_abnormal_order set `ishandle`=? ,`handletime`=? where `id`=? ";
		this.jdbcTemplate.update(sql, ishandle, handletime, id);
	}

	public void saveAbnormalOrderForNohandle(long id, long ishandle) {
		String sql = "update express_ops_abnormal_order set `ishandle`= ? where `id`=?  ";
		this.jdbcTemplate.update(sql, ishandle, id);
	}

	public List<String> getAbnormalOrderByCredatetime(String chuangjianbegindate, String chuangjianenddate) {
		String sql = "select opscwbid from express_ops_abnormal_order where " + "  credatetime >= '" + chuangjianbegindate + "' and credatetime <= '" + chuangjianenddate + "' ";

		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 用于迁移
	 * 
	 * @return
	 */
	public List<AbnormalOrder> getAllAbnormalIsnow() {
		String sql = "SELECT * FROM express_ops_abnormal_order WHERE isnow=1 ORDER BY credatetime ";
		return this.jdbcTemplate.query(sql, new AbnormalOrderRowMapper());

	}

	/**
	 * 根据id 得到
	 * 
	 * @param id
	 * @return
	 */
	public AbnormalOrder getAbnormalOrderByOId(long id) {
		String sql = "select * from express_ops_abnormal_order where id=? ";
		AbnormalOrder ab = new AbnormalOrder();
		try {
			ab = this.jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), id);
		} catch (Exception e) {
			ab = null;
		}
		return ab;
	}
	/**
	 * 根据cwb 得到
	 * 
	 * @param id
	 * @return
	 */
	public AbnormalOrder getAbnormalOrderByOCwb(String cwb) {
		String sql = "select * from express_ops_abnormal_order where cwb=? ";
		AbnormalOrder ab = new AbnormalOrder();
		try {
			ab = this.jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), cwb);
		} catch (Exception e) {
			ab = null;
		}
		return ab;
	}


	public List<JSONObject> getAbnormalOrderByCredatetimeofpage(long page, String chuangjianbegindate, String chuangjianenddate, String cwbs, long createbranchid, long abnormaltypeid, long ishandle,
			long customerid, long handleBranch,long dealresult,long losebackisornot,long dutybranchid,long currentbranchid,long findscope,long userid) {
		String sql = "SELECT *  from `express_ops_abnormal_order`   " + "WHERE   " + "credatetime >= '" + chuangjianbegindate + "' " + "and credatetime <= '" + chuangjianenddate + "' and isnow=1";
		boolean iskefu=false;
		if (handleBranch==BranchEnum.KeFu.getValue()) {
			iskefu=true;
		}
		//判断是不是客服的时候
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			//判断是管理员还是库房与站点的非管理员的人（判断是否查询到本身）
			if (findscope!=0) {
				sql += " AND (`creuserid`="+userid+"  OR `dutypersonid`="+userid+")";
			}
		}
		if (cwbs.length() > 0) {
			sql += " AND `cwb` IN(" + cwbs + ")";
		}
		//判断是不是客服以及是不是当前机构
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			if ((createbranchid!=currentbranchid&&dutybranchid!=currentbranchid)||(createbranchid==0&&dutybranchid!=currentbranchid)||(createbranchid!=currentbranchid&&dutybranchid==0)) {
				if ((createbranchid==0&&dutybranchid==0)||(createbranchid==currentbranchid&&dutybranchid==currentbranchid)) {
					
				}else {
					//创建查询不到数据的条件
					createbranchid=0;
					sql+="  AND `branchid`="+createbranchid;
				}
				
			}
		}
		if (createbranchid > 0) {
			sql += " AND `branchid`=" + createbranchid;
		}
		if (dutybranchid > 0) {
			sql += " AND `dutybrachid`=" + dutybranchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle==1) {
			String ishandledata=AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue();
			sql += " AND `ishandle` IN('"+ishandledata+"')";
		}
		if (ishandle==7) {
			if (iskefu) {
				String ishandledata=AbnormalOrderHandleEnum.kefuchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
			}else  {
				String ishandledata=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata1=AbnormalOrderHandleEnum.zerenfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata2=ishandledata+","+ishandledata1;
				//创建方与责任方待处理查询条件限制
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata2+"')";
				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata1+"')";
				}else {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata+"') OR `ishandle` NOT IN('"+ishandledata1+"')"+")";

				}
				
			} 
		}
		if (ishandle==6) {
			//问题件是否成立的条件的判断
			if (dealresult>0) {
				sql+=" AND `dealresult`="+dealresult;
			}
			if (iskefu) {
				sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else {
				//创建方与责任方处理
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();

				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();
				}else {
					sql+="  AND (`ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" OR `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue()+")";

				}
			
			}
		}
		if (ishandle==AbnormalOrderHandleEnum.jieanchuli.getValue()) {
			sql+="   AND `ishandle`="+AbnormalOrderHandleEnum.jieanchuli.getValue();
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		if (dealresult>0) {
			sql+=" AND `dealresult`="+dealresult;
		}
		if (losebackisornot>-1) {
			sql+=" AND `isfind`="+losebackisornot;
		}
		//sql += " AND `handleBranch` =" + handleBranch;
		if (page!=-6) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());
	}


	public int getAbnormalOrderByCredatetimeCount(String chuangjianbegindate, String chuangjianenddate, String cwbs, long createbranchid, long abnormaltypeid, long ishandle, long customerid,
			long handleBranch,long dealresult,long losebackisornot,long dutybranchid,long currentbranchid,long findscope,long userid) {
		String sql = "SELECT count(1)  from `express_ops_abnormal_order`   " + "WHERE   " + "credatetime >= '" + chuangjianbegindate + "' " + "and credatetime <= '" + chuangjianenddate + "' ";
		//查询到与自身有关的，自己可以是负责人或者是创建人
		boolean iskefu=false;
		if (handleBranch==BranchEnum.KeFu.getValue()) {
			iskefu=true;
		}
		//判断是不是客服的时候
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			//判断是管理员还是库房与站点的非管理员的人（判断是否查询到本身）
			if (findscope!=0) {
					sql += " AND (`creuserid`="+userid+"  OR `dutypersonid`="+userid+")";
			}
		}
		if (cwbs.length() > 0) {
			sql += " AND `cwb` IN(" + cwbs + ")";
		}
		
		//判断是不是客服以及是不是当前机构
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			if ((createbranchid!=currentbranchid&&dutybranchid!=currentbranchid)||(createbranchid==0&&dutybranchid!=currentbranchid)||(createbranchid!=currentbranchid&&dutybranchid==0)) {
				if ((createbranchid==0&&dutybranchid==0)||(createbranchid==currentbranchid&&dutybranchid==currentbranchid)) {
					
				}else {
					//创建查询不到数据的条件
					createbranchid=0;
					sql+="  AND `branchid`="+createbranchid;
				}
				
			}
		}
		if (createbranchid > 0) {
			sql += " AND `branchid`=" + createbranchid;
		}
		if (dutybranchid > 0) {
			sql += " AND `dutybrachid`=" + dutybranchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle==1) {
			String ishandledata=AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue();
			sql += " AND `ishandle` IN('"+ishandledata+"')";
		}
		if (ishandle==7) {
			if (iskefu) {
				String ishandledata=AbnormalOrderHandleEnum.kefuchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
			}else  {
				String ishandledata=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata1=AbnormalOrderHandleEnum.zerenfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata2=ishandledata+","+ishandledata1;
				//创建方与责任方待处理查询条件限制
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata2+"')";
				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata1+"')";
				}else {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata+"') OR `ishandle` NOT IN('"+ishandledata1+"')"+")";

				}
				
			} 
		}
		if (ishandle==6) {
			if (iskefu) {
				sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else {
				//创建方与责任方处理
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();

				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();
				}else {
					sql+="  AND (`ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" OR `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue()+")";

				}
			
			}
		}
		if (ishandle==AbnormalOrderHandleEnum.jieanchuli.getValue()) {
			sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.jieanchuli.getValue();
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		if (dealresult>0) {
			sql+="  AND `dealresult`="+dealresult;
		}
		if (losebackisornot>0) {
			sql+="   AND `isfind`="+losebackisornot;
		}
		//站点类型
		//sql += " AND `handleBranch` =" + handleBranch;
		return this.jdbcTemplate.queryForInt(sql);
	}

	public int getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(String cwb, long createbranchid, long abnormaltypeid, long ishandle, String begindate, String enddate,
			long customerid, long handleBranch,long dealresult,long losebackisornot,long dutybranchid,long currentbranchid,long findscope,long userid) {
		// String opscwbidsql =
		// "select opscwbid from express_ops_abnormal_write_back where " +
		// " credatetime >= '" + begindate + "'  and credatetime <= '" + enddate
		// + "' ";
		String sql = "SELECT count(1) FROM `express_ops_abnormal_order`" + "  WHERE `handletime` >='" + begindate + "' and `handletime` <='" + enddate + "' ";
		boolean iskefu=false;
		if (handleBranch==BranchEnum.KeFu.getValue()) {
			iskefu=true;
		}
		//判断是不是客服的时候
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			//判断是管理员还是库房与站点的非管理员的人（判断是否查询到本身）
			if (findscope!=0) {
				sql += " AND (`creuserid`="+userid+"  OR `dutypersonid`="+userid+")";
			}
		}
		if (cwb.length() > 0) {
			sql += " AND `cwb` IN(" + cwb + ")";
		}
		//判断是不是客服以及是不是当前机构
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			if ((createbranchid!=currentbranchid&&dutybranchid!=currentbranchid)||(createbranchid==0&&dutybranchid!=currentbranchid)||(createbranchid!=currentbranchid&&dutybranchid==0)) {
				if ((createbranchid==0&&dutybranchid==0)||(createbranchid==currentbranchid&&dutybranchid==currentbranchid)) {
					
				}else {
					//创建查询不到数据的条件
					createbranchid=0;
					sql+="  AND `branchid`="+createbranchid;
				}
				
			}
		}
		if (createbranchid > 0) {
			sql += " AND `branchid`=" + createbranchid;
		}
		if (dutybranchid > 0) {
			sql += " AND `dutybrachid`=" + dutybranchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle==1) {
			String ishandledata=AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue();
			sql += " AND `ishandle` IN('"+ishandledata+"')";
		}
		if (ishandle==7) {
			if (iskefu) {
				String ishandledata=AbnormalOrderHandleEnum.kefuchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
			}else  {
				String ishandledata=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata1=AbnormalOrderHandleEnum.zerenfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata2=ishandledata+","+ishandledata1;
				//创建方与责任方待处理查询条件限制
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata2+"')";
				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata1+"')";
				}else {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata+"') OR `ishandle` NOT IN('"+ishandledata1+"')"+")";

				}
				
			} 
		}
		if (ishandle==6) {
			if (iskefu) {
				sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else {
				//创建方与责任方处理
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();

				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();
				}else {
					sql+="  AND (`ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" OR `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue()+")";

				}
			
			}
		}
		if (ishandle==AbnormalOrderHandleEnum.jieanchuli.getValue()) {
			sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.jieanchuli.getValue();
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		if (dealresult>0) {
			sql+=" AND `dealresult`="+dealresult;
		}
		if (losebackisornot>-1) {
			sql+=" AND `isfind`="+losebackisornot;
		}
		//站点类型暂时不做任何限制
		/*sql += " AND `handleBranch` = " + handleBranch;*/
		// sql +=" AND ao.isnow=1 ";
		return this.jdbcTemplate.queryForInt(sql);
	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(long page, String cwb, long createbranchid, long abnormaltypeid, long ishandle, String begindate,
			String enddate, long customerid, long handleBranch,long dealresult,long losebackisornot,long dutybranchid,long currentbranchid,long findscope,long userid) {
		// String opscwbidsql =
		// "select opscwbid from express_ops_abnormal_write_back where " +
		// " credatetime >= '" + begindate + "'  and credatetime <= '" + enddate
		// + "' ";
		String sql = "SELECT * FROM `express_ops_abnormal_order`" + "  WHERE `handletime` >='" + begindate + "' and `handletime` <='" + enddate + "' and isnow=1";
		
		boolean iskefu=false;
		if (handleBranch==BranchEnum.KeFu.getValue()) {
			iskefu=true;
		}
		//判断是不是客服的时候
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			//判断是管理员还是库房与站点的非管理员的人（判断是否查询到本身）
			if (findscope!=0) {
				if (dutybranchid==currentbranchid&&(createbranchid==currentbranchid)) {
					sql += " AND `creuserid`="+userid;
					sql+=" AND `dutypersonid`="+userid;
				}else if (currentbranchid==createbranchid) {
					sql += " AND `creuserid`="+userid;
				}else if(currentbranchid==dutybranchid){
					sql+=" AND `dutypersonid`="+userid;
				}else {
					sql += " AND (`creuserid`="+userid+" OR `dutypersonid`="+userid+")";
				}
			}
		}
		if (cwb.length() > 0) {
			sql += " AND `cwb` IN(" + cwb + ")";
		}
		//判断是不是客服以及是不是当前机构
		if (handleBranch!=BranchEnum.KeFu.getValue()) {
			if ((createbranchid!=currentbranchid&&dutybranchid!=currentbranchid)||(createbranchid==0&&dutybranchid!=currentbranchid)||(createbranchid!=currentbranchid&&dutybranchid==0)) {
				if ((createbranchid==0&&dutybranchid==0)||(createbranchid==currentbranchid&&dutybranchid==currentbranchid)) {
					
				}else {
					//创建查询不到数据的条件
					createbranchid=0;
					sql+="  AND `branchid`="+createbranchid;
				}
				
			}
		}
	
		if (createbranchid > 0) {
			sql += " AND `branchid`=" +createbranchid ;
		}
		if (dutybranchid > 0) {
			sql += " AND `dutybrachid`=" + dutybranchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND `abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle==1) {
			String ishandledata=AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue();
			sql += " AND `ishandle` IN('"+ishandledata+"')";
		}
		if (ishandle==7) {
			if (iskefu) {
				String ishandledata=AbnormalOrderHandleEnum.kefuchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
			}else  {
				String ishandledata=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata1=AbnormalOrderHandleEnum.zerenfangchuli.getValue()+"','"+AbnormalOrderHandleEnum.xiugai.getValue()+"','"+AbnormalOrderHandleEnum.weichuli.getValue()+"','"+AbnormalOrderHandleEnum.jieanchuli.getValue();
				String ishandledata2=ishandledata+","+ishandledata1;
				//创建方与责任方待处理查询条件限制
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata2+"')";
				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata+"')";
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle` NOT IN('"+ishandledata1+"')";
				}else {
					sql+="  AND (`ishandle` NOT IN('"+ishandledata+"') OR `ishandle` NOT IN('"+ishandledata1+"')"+")";

				}
				
			} 
		}
		if (ishandle==6) {
			if (iskefu) {
				sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else {
				//创建方与责任方处理
				if (currentbranchid==createbranchid&&currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();

				}else if (currentbranchid==createbranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
				}else if (currentbranchid==dutybranchid) {
					sql+="  AND `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue();
				}else {
					sql+="  AND (`ishandle`="+AbnormalOrderHandleEnum.chuangjianfangchuli.getValue()+" OR `ishandle`="+AbnormalOrderHandleEnum.zerenfangchuli.getValue()+")";

				}
			
			}
		}
		if (ishandle==AbnormalOrderHandleEnum.jieanchuli.getValue()) {
			sql+=" AND `ishandle`="+AbnormalOrderHandleEnum.jieanchuli.getValue();
		}
		if (customerid > -1) {
			sql += " AND `customerid`=" + customerid;
		}
		if(dealresult>0){
			sql+="  AND `dealresult`="+dealresult;
		}
		if (losebackisornot>0) {
			sql+="  AND `isfind`="+losebackisornot;
		}
		
		//sql += " AND `handleBranch` =" + handleBranch;
		// sql +=" AND ao.isnow=1 ";
		if (page!=-6) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());
	}

	public void deleteAbnormalOrderbycwb(String cwb) {
		try {
			this.jdbcTemplate.update("DELETE FROM express_ops_abnormal_order where cwb=?", cwb);
		} catch (DataAccessException e) {
		}
	}

	public void abnormaldataMove(CwbOrder co) {
		try {
			String sql = "update express_ops_abnormal_order set cwb='" + co.getCwb() + "',deliverybranchid='" + co.getDeliverybranchid() + "',emaildate='" + co.getEmaildate() + "',flowordertype='"
					+ co.getFlowordertype() + "' where opscwbid='" + co.getOpscwbid() + "'";
			this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
		}
	}

	public void abnormaldataMoveofhandletime(AbnormalWriteBack awb) {
		try {
			String sql = "update express_ops_abnormal_order set handletime='" + awb.getCredatetime() + "' where opscwbid='" + awb.getOpscwbid() + "' ";
			this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
		}
	}

	public List<AbnormalOrder> getAbnormalOrderByWherefind(long page, String begindate, String enddate, long ishandle, long abnormaltypeid) {
		String sql = "select * from express_ops_abnormal_order where  credatetime >= '" + begindate + "' and credatetime <= '" + enddate + "' ";
		if (ishandle > 0) {
			sql += " and ishandle in ( " + ishandle + ")";
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new AbnormalOrderRowMapper());
	}

	public long getAbnormalOrderCountfind(String begindate, String enddate, long ishandle, long abnormaltypeid) {
		String sql = "select count(1) from express_ops_abnormal_order where credatetime >= ?  and credatetime <=? ";
		if (ishandle > 0) {
			sql += " and ishandle in ( " + ishandle + ")";
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}
		return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public List<JSONObject> getAbnormalOrderByWherefindExport(String begindate, String enddate, long ishandle, long abnormaltypeid) {
		String sql = "select * from express_ops_abnormal_order where  credatetime >= '" + begindate + "' and credatetime <= '" + enddate + "' ";
		if (ishandle > 0) {
			sql += " and ishandle in ( " + ishandle + ")";
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}
		return this.jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());
	}
	//创建问题件时，如果丢失件里面有该问题件单号，修改isfind（是否找到）字段，1为找到，0为未找到
	public long updateMisspieceState(long isfind,String cwb){
		String sql="update  express_ops_abnormal_order set isfind=? where cwb=?";
		return this.jdbcTemplate.update(sql, isfind,cwb);
	}
	
	
}
