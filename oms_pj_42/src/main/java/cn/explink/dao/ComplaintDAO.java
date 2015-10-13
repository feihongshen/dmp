package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.domain.Complaint;
import cn.explink.domain.CwbOrder;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ComplaintDAO {

	private final class ComMapper implements RowMapper<Complaint> {
		@Override
		public Complaint mapRow(ResultSet rs, int rowNum) throws SQLException {
			Complaint complaint = new Complaint();
			complaint.setComplaintuserid(rs.getLong("complaintuserid"));
			complaint.setCheckflag(rs.getLong("checkflag"));
			complaint.setComplaintcontactman(StringUtil.nullConvertToEmptyString(rs.getString("complaintcontactman")));
			complaint.setComplaintcontent(StringUtil.nullConvertToEmptyString(rs.getString("complaintcontent")));
			complaint.setComplaintcreateuser(StringUtil.nullConvertToEmptyString(rs.getString("complaintcreateuser")));
			complaint.setComplaintcustomerid(rs.getLong("complaintcustomerid"));
			complaint.setComplaintcwb(StringUtil.nullConvertToEmptyString(rs.getString("complaintcwb")));
			complaint.setComplaintflag(rs.getLong("complaintflag"));
			complaint.setComplaintdelflag(rs.getLong("complaintdelflag"));
			complaint.setComplaintid(rs.getLong("complaintid"));
			complaint.setComplaintphone(StringUtil.nullConvertToEmptyString(rs.getString("complaintphone")));
			complaint.setComplaintresult(StringUtil.nullConvertToEmptyString(rs.getString("complaintresult")));
			complaint.setComplaintuserdesc(StringUtil.nullConvertToEmptyString(rs.getString("complaintuserdesc")));
			complaint.setComplainttime(StringUtil.nullConvertToEmptyString(rs.getString("complainttime")));
			complaint.setComplainttypeid(rs.getLong("complainttypeid"));
			complaint.setIssure(rs.getLong("issure"));
			return complaint;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Complaint getComplaintById(long id) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_cs_complaint where complaintid=? ", new ComMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * lansheng 审核投诉
	 * 
	 * @param of
	 * @return
	 */
	public long updateCheckflag(long id) {
		return jdbcTemplate.update("update express_cs_complaint set checkflag =1 where complaintid=" + id + " ");
	}

	/**
	 * lansheng 是否属实
	 * 
	 * @param of
	 * @return
	 */
	public long isSure(long id) {
		return jdbcTemplate.update("update express_cs_complaint set issure=(issure+1)%2 where complaintid=" + id + " ");
	}

	/**
	 * lansheng 处理投诉结果
	 * 
	 * @param id
	 * @param pram
	 * @return
	 */
	public long updateFlag(long id, String pram) {
		return jdbcTemplate.update("update express_cs_complaint set checkflag =1 , complaintflag =1 , complaintresult='" + pram + "' where complaintid=" + id + " ");
	}

	/**
	 * lasnheng 删除投诉
	 * 
	 * @param id
	 * @return
	 */
	public long updateDelFlag(long id) {
		return jdbcTemplate.update("update express_cs_complaint set complaintdelflag=1 where complaintid=" + id + " ");
	}

	public long getComplaintCount(long page, long complainttypeid, long checkflag, long complaintflag, String complaintcustomerid, String complaintcontactman, String complaintphone,
			String complaintuserid, String complaintuserdesc, String complaintcwb, String beginemaildate, String endemaildate, long issure) {
		String sql = "select count(1) from express_cs_complaint   ";
		sql = this.getComplaintPageWhereSql(sql, complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid, complaintuserdesc, complaintcwb,
				beginemaildate, endemaildate, issure);
		sql += " ORDER BY complainttime DESC ";
		return jdbcTemplate.queryForInt(sql);
	}

	public List<Complaint> getComplaintAll(long page, long complainttypeid, long checkflag, long complaintflag, String complaintcustomerid, String complaintcontactman, String complaintphone,
			String complaintuserid, String complaintuserdesc, String complaintcwb, String beginemaildate, String endemaildate, long issure) {
		String sql = "select * from express_cs_complaint ";
		sql = this.getComplaintPageWhereSql(sql, complainttypeid, checkflag, complaintflag, complaintcustomerid, complaintcontactman, complaintphone, complaintuserid, complaintuserdesc, complaintcwb,
				beginemaildate, endemaildate, issure);

		sql += " ORDER BY complainttime DESC ";
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new ComMapper());
	}

	private String getComplaintPageWhereSql(String sql, long complainttypeid, long checkflag, long complaintflag, String complaintcustomerid, String complaintcontactman, String complaintphone,
			String complaintuserid, String complaintuserdesc, String complaintcwb, String beginemaildate, String endemaildate, long issure) {

		if (complainttypeid > -1 || checkflag > -1 || complaintflag > -1 || complaintcustomerid.length() > 0 || complaintcontactman.length() > 0 || complaintphone.length() > 0
				|| complaintuserid.length() > 0 || complaintuserdesc.length() > 0 || complaintcwb.length() > 0 || beginemaildate.length() > 0 || endemaildate.length() > 0 || issure > -1) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (complainttypeid > -1) {
				w.append(" and complainttypeid =" + complainttypeid);
			}
			if (checkflag > -1) {
				w.append(" and checkflag =" + checkflag);
			}
			if (complaintflag > -1) {
				w.append(" and complaintflag=" + complaintflag);
			}
			if (complaintcustomerid.length() > 0) {
				try {
					long complaintcustomeridl = new Long(complaintcustomerid);
				} catch (Exception e) {
					complaintcustomerid = "-1";
				}
				w.append(" and complaintcustomerid=" + complaintcustomerid);
			}
			if (complaintcontactman.length() > 0) {
				w.append(" and complaintcontactman='" + complaintcontactman + "'");
			}
			if (complaintphone.length() > 0) {
				w.append(" and complaintphone='" + complaintphone + "'");
			}
			if (complaintuserid.length() > 0) {
				try {
					long complaintuseridl = new Long(complaintuserid);
				} catch (Exception e) {
					complaintuserid = "-1";
				}
				w.append(" and complaintuserid=" + complaintuserid);
			}
			if (complaintuserdesc.length() > 0) {
				w.append(" and complaintuserdesc  like '%" + complaintuserdesc + "%'");
			}
			if (complaintcwb.length() > 0) {
				w.append(" and complaintcwb='" + complaintcwb + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and complainttime >= '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and complainttime <= '" + endemaildate + "'");
			}
			if (issure > -1) {
				w.append(" and issure = " + issure + "");
			}
			w.append(" and complaintdelflag=0 ");
			sql += w.substring(4, w.length());
		} else {
			sql += " where complaintdelflag=0 ";
		}
		return sql;
	}

	public void saveComplaint(final Complaint complaint) {

		jdbcTemplate.update("insert into express_cs_complaint(complainttypeid,complaintcwb,complaintcustomerid,"
				+ "complaintcontent,complaintuserid,complaintresult,complaintcreateuser,complainttime,checkflag,complaintcontactman,complaintphone,complaintflag,complaintdelflag,complaintuserdesc) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, complaint.getComplainttypeid());
				ps.setString(2, complaint.getComplaintcwb());
				ps.setLong(3, complaint.getComplaintcustomerid());
				ps.setString(4, complaint.getComplaintcontent());
				ps.setLong(5, complaint.getComplaintuserid());
				ps.setString(6, complaint.getComplaintresult());
				ps.setString(7, complaint.getComplaintcreateuser());
				ps.setString(8, complaint.getComplainttime());
				ps.setLong(9, complaint.getCheckflag());
				ps.setString(10, complaint.getComplaintcontactman());
				ps.setString(11, complaint.getComplaintphone());
				ps.setLong(12, complaint.getComplaintflag());
				ps.setLong(13, complaint.getComplaintdelflag());
				ps.setString(14, complaint.getComplaintuserdesc());
			}
		});

	}

}
