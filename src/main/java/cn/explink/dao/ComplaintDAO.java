package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Complaint;
import cn.explink.util.Page;

@Component
public class ComplaintDAO {

	private final class ComplaintMapper implements RowMapper<Complaint> {
		@Override
		public Complaint mapRow(ResultSet rs, int rowNum) throws SQLException {
			Complaint complaint = new Complaint();
			complaint.setCwb(rs.getString("cwb"));
			complaint.setAuditRemark(rs.getString("auditRemark") == null ? "" : rs.getString("auditRemark"));
			complaint.setAuditTime(rs.getString("auditTime"));
			complaint.setAuditType(rs.getLong("auditType"));
			complaint.setAuditUser(rs.getLong("auditUser"));
			complaint.setBranchid(rs.getLong("branchid"));
			complaint.setContent(rs.getString("content"));
			complaint.setCreateTime(rs.getString("createTime"));
			complaint.setCreateUser(rs.getLong("createUser"));
			complaint.setDeliveryid(rs.getLong("deliveryid"));
			complaint.setId(rs.getLong("id"));
			complaint.setType(rs.getLong("type"));
			complaint.setServertreasonid(rs.getLong("servertreasonid"));
			complaint.setReplyDetail(rs.getString("replyDetail"));
			return complaint;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Complaint getComplaintById(long id) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_complaint where id=? ", new ComplaintMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 按条件获取查询条数
	 * 
	 * @param type
	 * @param auditType
	 * @param starteTime
	 * @param endTime
	 * @return
	 */
	public long getComplaintCount(long type, long auditType, String starteTime, String endTime) {
		String sql = "select count(1) from express_ops_complaint where ";
		if ((type > -1) && (auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and auditType=? and createTime>=? and createTime<=? ";
			return this.jdbcTemplate.queryForLong(sql, type, auditType, starteTime, endTime);
		} else if ((type > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and createTime>=? and createTime<=? ";
			return this.jdbcTemplate.queryForLong(sql, type, starteTime, endTime);
		} else if ((auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " auditType=? and createTime>=? and createTime<=?  ";
			return this.jdbcTemplate.queryForLong(sql, auditType, starteTime, endTime);
		} else {
			sql += " createTime>=? and createTime<=? ";
			return this.jdbcTemplate.queryForLong(sql, starteTime, endTime);
		}
	}

	/**
	 * 按条件查询投诉列表 使用占位符方式
	 * 
	 * @param type
	 * @param auditType
	 * @param starteTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public List<Complaint> getComplaintByWhere(long type, long auditType, String starteTime, String endTime, long page) {
		String sql = "select * from express_ops_complaint where ";
		if ((type > -1) && (auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and auditType=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), type, auditType, starteTime, endTime);
		} else if ((type > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), type, starteTime, endTime);
		} else if ((auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " auditType=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), auditType, starteTime, endTime);
		} else {
			sql += " createTime>=? and createTime<=? ORDER BY createTime DESC ";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), starteTime, endTime);
		}
	}

	public List<Complaint> getComplaintByWhereNoPage(long type, long auditType, String starteTime, String endTime) {
		String sql = "select * from express_ops_complaint where ";
		if ((type > -1) && (auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and auditType=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), type, auditType, starteTime, endTime);
		} else if ((type > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " type=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), type, starteTime, endTime);
		} else if ((auditType > -1) && (starteTime.length() > 0) && (endTime.length() > 0)) {
			sql += " auditType=? and createTime>=? and createTime<=? ORDER BY createTime DESC ";
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), auditType, starteTime, endTime);
		} else {
			sql += " createTime>=? and createTime<=? ORDER BY createTime DESC ";
			return this.jdbcTemplate.query(sql, new ComplaintMapper(), starteTime, endTime);
		}
	}

	/**
	 * 按订单号查询
	 * 
	 * @param cwb
	 * @return
	 */
	public List<Complaint> getComplaintByCwb(String cwb) {
		String sql = "select * from express_ops_complaint where cwb=?";
		return this.jdbcTemplate.query(sql, new ComplaintMapper(), cwb);
	}

	public List<Complaint> getComplaintByCwbAndType(String cwb, long type) {
		String sql = "select * from express_ops_complaint where cwb=? and type=? order by createTime desc";
		return this.jdbcTemplate.query(sql, new ComplaintMapper(), cwb, type);
	}

	public void saveComplaint(final Complaint complaint) {
		this.jdbcTemplate.update("insert into " + "express_ops_complaint(cwb,type,branchid,deliveryid," + "auditType,content,auditRemark,createTime,"
				+ "auditTime,createUser,auditUser,servertreasonid) " + "values(?,?,?,?,?,?,?,?,?,?," + "?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, complaint.getCwb());
				ps.setLong(2, complaint.getType());
				ps.setLong(3, complaint.getBranchid());
				ps.setLong(4, complaint.getDeliveryid());
				ps.setLong(5, complaint.getAuditType());
				ps.setString(6, complaint.getContent());
				ps.setString(7, complaint.getAuditRemark());
				ps.setString(8, complaint.getCreateTime());
				ps.setString(9, complaint.getAuditTime());
				ps.setLong(10, complaint.getCreateUser());
				ps.setLong(11, complaint.getAuditUser());
				ps.setLong(12, complaint.getServertreasonid());
			}
		});

	}

	public void updateComplaint(final Complaint complaint) {
		this.jdbcTemplate.update("update " + "express_ops_complaint set cwb=?,type=?,branchid=?,deliveryid=?," + "auditType=?,content=?,auditRemark=?,createTime=?,"
				+ "auditTime=?,createUser=?,auditUser=?,servertreasonid=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, complaint.getCwb());
				ps.setLong(2, complaint.getType());
				ps.setLong(3, complaint.getBranchid());
				ps.setLong(4, complaint.getDeliveryid());
				ps.setLong(5, complaint.getAuditType());
				ps.setString(6, complaint.getContent());
				ps.setString(7, complaint.getAuditRemark());
				ps.setString(8, complaint.getCreateTime());
				ps.setString(9, complaint.getAuditTime());
				ps.setLong(10, complaint.getCreateUser());
				ps.setLong(11, complaint.getAuditUser());
				ps.setLong(12, complaint.getServertreasonid());
				ps.setLong(13, complaint.getId());

			}
		});

	}

	public void updateComplaintById(final Complaint complaint) {
		this.jdbcTemplate.update("update express_ops_complaint set auditType=?,auditRemark=?,auditTime=?,auditUser=? where id=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, complaint.getAuditType());
				ps.setString(2, complaint.getAuditRemark());
				ps.setString(3, complaint.getAuditTime());
				ps.setLong(4, complaint.getAuditUser());
				ps.setLong(5, complaint.getId());
			}
		});
	}

	public void deleteComplaint(long id) {
		String sql = "delete from express_ops_complaint where id=?";
		this.jdbcTemplate.update(sql, id);
	}

	/**
	 * 按条件获取查询条数
	 * 
	 * @param type
	 * @param auditType
	 * @param starteTime
	 * @param endTime
	 * @return
	 */
	public long getComplaintCountForzhandian(String cwb, long type, long auditType, long branchid, String starteTime, String endTime) {
		String sql = "select count(1) from express_ops_complaint where 1=1";
		if (cwb.length() > 0) {
			sql += " and cwb='" + cwb + "'";
		}
		if (auditType > -1) {
			sql += " and auditType=" + auditType;
		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}
		if ((starteTime.length() > 0) && (endTime.length() > 0)) {

			sql += " and createTime>='" + starteTime + "' and createTime<=  '" + endTime + "'";
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 按条件查询投诉列表 站长下面
	 * 
	 * @param type
	 * @param auditType
	 * @param starteTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public List<Complaint> getComplaintForzhandian(String cwb, long type, long auditType, long branchid, String starteTime, String endTime, long page) {
		String sql = "select * from express_ops_complaint where 1=1";
		if (cwb.length() > 0) {
			sql += " and cwb='" + cwb + "'";
		}
		if (auditType > -1) {
			sql += " and auditType=" + auditType;
		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}
		if ((starteTime.length() > 0) && (endTime.length() > 0)) {

			sql += " and createTime>='" + starteTime + "' and createTime<=  '" + endTime + "'";
		}
		sql += " ORDER BY createTime DESC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new ComplaintMapper());

	}

	public long updateComplaintCountForzhandian(long type, String auditTime, long auditUser, long id, String replyDetail) {
		try {
			String sql = "update express_ops_complaint set audittype=?,replyDetail=?,auditTime='" + auditTime + "' ,auditUser=" + auditUser + " where id=?";
			this.jdbcTemplate.update(sql, type, replyDetail, id);
			return 1;

		} catch (Exception e) {
			return 0;
		}
	}

	public List<Complaint> getActiveComplaintByCwbs(List<String> cwbs) {
		StringBuilder stringBuilder = new StringBuilder("select * from express_ops_complaint where cwb in (");
		for (String cwb : cwbs) {
			stringBuilder.append("'").append(cwb).append("',");
		}
		if (cwbs.size() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		stringBuilder.append(")");
		return this.jdbcTemplate.query(stringBuilder.toString(), new ComplaintMapper());
	}
}
