package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.CwbOrder;

@Component
public class AbnormalWriteBackDAO {

	private final class AbnormalWriteBackRowMapper implements RowMapper<AbnormalWriteBack> {
		@Override
		public AbnormalWriteBack mapRow(ResultSet rs, int rowNum) throws SQLException {
			AbnormalWriteBack abnormalWriteBack = new AbnormalWriteBack();

			abnormalWriteBack.setId(rs.getLong("id"));
			abnormalWriteBack.setDescribe(rs.getString("describe"));
			abnormalWriteBack.setCreuserid(rs.getLong("creuserid"));
			abnormalWriteBack.setCredatetime(rs.getString("credatetime"));
			abnormalWriteBack.setOpscwbid(rs.getLong("opscwbid"));
			abnormalWriteBack.setType(rs.getLong("type"));
			abnormalWriteBack.setAbnormalorderid(rs.getLong("abnormalorderid"));
			abnormalWriteBack.setAbnormalordertype(rs.getLong("abnormalordertype"));
			abnormalWriteBack.setCwb(rs.getString("cwb"));
			abnormalWriteBack.setFileposition(rs.getString("fileposition"));
			return abnormalWriteBack;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creAbnormalOrder(long opscwbid, String describe, long creuserid, long type, String credatetime, long abnormalorderid, long abnormaltypeid, String cwb,String name) {
		String sql = "insert into express_ops_abnormal_write_back(`opscwbid`,`describe`,`creuserid`,`type`,`credatetime`,`abnormalorderid`,`abnormalordertype`,`cwb`,`fileposition`) values(?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, opscwbid, describe, creuserid, type, credatetime, abnormalorderid, abnormaltypeid, cwb,name);
	}
	
	public void creAbnormalOrderAdd(long opscwbid, String describe, long creuserid, long type, String credatetime, long abnormalorderid, long abnormaltypeid, String cwb,String name) {
		String sql = "insert into express_ops_abnormal_write_back(`opscwbid`,`describe`,`creuserid`,`type`,`credatetime`,`abnormalorderid`,`abnormalordertype`,`cwb`,`fileposition`) values(?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, opscwbid, describe, creuserid, type, credatetime, abnormalorderid, abnormaltypeid, cwb,name);
	}

	public List<AbnormalWriteBack> getAbnormalOrderByWhere(String begindate, String enddate, long ishandle, long abnormaltypeid, long creuserid) {
		String sql = "select * from express_ops_abnormal_write_back where `creuserid`=? ";
		if (begindate.length() > 0) {
			sql += " and `credatetime` >= '" + begindate + "' ";
		}
		if (enddate.length() > 0) {
			sql += " and `credatetime` <= '" + enddate + "' ";
		}
		if (ishandle > -1) {
			sql += " and `ishandle` =" + ishandle;
		}
		if (abnormaltypeid > 0) {
			sql += " and `abnormalordertype` =" + abnormaltypeid;
		}
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(), creuserid);
	}

	public List<AbnormalWriteBack> getAbnormalOrderByOpscwbid(long opscwbid) {
		String sql = "select * from express_ops_abnormal_write_back where `opscwbid`=? order by credatetime ASC";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(), opscwbid);
	}

	public List<AbnormalWriteBack> getAbnormalOrderByParam(long opscwbid, long type) {
		String sql = "select * from express_ops_abnormal_write_back where `opscwbid`=? and `type`=? order by `credatetime` desc";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(), opscwbid, type);
	}

	public List<String> getAbnormalOrderByCredatetime(String begindate, String enddate) {
		String sql = "select opscwbid from express_ops_abnormal_write_back where " + " credatetime >= ?  and credatetime <= ? ";
		return this.jdbcTemplate.queryForList(sql, String.class, begindate, enddate);
	}

	public void saveAbnormalOrder(long opscwbid, String describe, long creuserid, String credatetime, long type) {
		String sql = " UPDATE express_ops_abnormal_write_back SET `describe`=?,credatetime=?,creuserid=? WHERE opscwbid=? AND type=? ";
		this.jdbcTemplate.update(sql, describe, credatetime, creuserid, opscwbid, type);
	}

	/**
	 * 根据opscwbid type
	 *
	 * @param string
	 * @param value
	 * @return
	 */
	public List<AbnormalWriteBack> getAbnormalOrderByOpscwbidAndType(String opscwbids, int value) {
		String sql = "select *  from  express_ops_abnormal_write_back where opscwbid in(" + opscwbids + ") and type=" + value;
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper());
	}

	/**
	 * 根据 异常订单的id 获取相应的记录
	 *
	 * @param id
	 * @return
	 */
	public List<AbnormalWriteBack> getAbnormalOrderByOrderid(long id) {
		String sql = "select * from express_ops_abnormal_write_back where `abnormalorderid`=? order by credatetime ASC";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(), id);
	}

	public List<AbnormalWriteBack> getAbnormalOrderByOrderidAndType(long orderid, int type) {
		String sql = " select *  from  express_ops_abnormal_write_back where abnormalorderid=? and type=?  order by `credatetime` desc";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(), orderid, type);
	}

	/**
	 * 数据迁移 更新
	 *
	 * @param opscwbid
	 * @param abnormalorderid
	 * @param abnormalordertypeid
	 */
	public void updateForQianyi(long opscwbid, long abnormalorderid, long abnormalordertypeid) {

		String sql = "update express_ops_abnormal_write_back set abnormalorderid=?,abnormalordertype=? where opscwbid=?";
		this.jdbcTemplate.update(sql, abnormalorderid, abnormalordertypeid, opscwbid);

	}

	public void deleteAbnormalWritebycwb(String cwb) {
		try {
			this.jdbcTemplate.update("DELETE FROM express_ops_abnormal_write_back where cwb=?", cwb);
		} catch (DataAccessException e) {
		}
	}

	public List<AbnormalWriteBack> getAbnormalOrderByOpscwbids(String opscwbids) {
		String sql = "select *  from  express_ops_abnormal_write_back where opscwbid in(" + opscwbids + ") ORDER BY credatetime";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper());
	}

	public void abnormaldataMoveofcwb(CwbOrder co) {
		try {
			String sql = "update express_ops_abnormal_write_back set cwb='" + co.getCwb() + "' where opscwbid='" + co.getOpscwbid() + "' ";
			this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
		}
	}
	//判断是否已经结案
	public List<AbnormalWriteBack> getAbnormalWriteBackisjiean(String cwb){
		String sql="select * from express_ops_abnormal_write_back where cwb=?  and type=?";
		return this.jdbcTemplate.query(sql, new AbnormalWriteBackRowMapper(),cwb,5);
	}
}
