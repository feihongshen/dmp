package cn.explink.b2c.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class CommonExptDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class CommonReasonRowMapper implements RowMapper<CommonReason> {
		@Override
		public CommonReason mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonReason expt = new CommonReason();
			expt.setId(rs.getLong("id"));
			expt.setCommonid(rs.getLong("commonid"));
			expt.setExpt_code(rs.getString("expt_code"));
			expt.setExpt_msg(rs.getString("expt_msg"));
			expt.setCustomercode(rs.getString("customercode"));
			expt.setExpt_type(rs.getLong("expt_type"));
			return expt;
		}
	}

	public void createCommonReason(CommonReason expt) {
		try {
			String sql = "insert into express_set_common_exptreason(commonid,customercode,expt_code,expt_msg,expt_type)" + " values(?,?,?,?,?) ";
			jdbcTemplate.update(sql, expt.getCommonid(), expt.getCustomercode(), expt.getExpt_code(), expt.getExpt_msg(), expt.getExpt_type());
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public List<CommonReason> getExptReasonListByKey(String commoncode, long expt_code, long page) {
		List<CommonReason> list = null;
		try {
			String sql = "select * from express_set_common_exptreason where 1=1 and customercode=" + commoncode + " and expt_code=" + expt_code;

			sql += " order by id  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new CommonReasonRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<CommonReason> getAllcommonListByKey(long page) {
		List<CommonReason> list = null;
		try {
			String sql = "select * from express_set_common_exptreason where 1=1";

			sql += " order by id  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new CommonReasonRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<CommonReason> getcommonListByCommonid(long page, long commonid) {
		List<CommonReason> list = null;
		try {
			String sql = "select * from express_set_common_exptreason where 1=1 ";
			if (commonid > -1) {
				sql += "and commonid=" + commonid;
			}
			sql += " order by id  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new CommonReasonRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public boolean IsExistsExptReasonFlag(String commoncode, String expt_code, String expt_msg) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_common_exptreason ";
			sql += "where  expt_code=" + expt_code + " and customercode='" + commoncode + "' and expt_msg ='" + expt_msg + "'";

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public boolean IsExistsExptJointReasonFlag(String commoncode, String expt_code) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_common_exptreason ";
			sql += "where  expt_code='" + expt_code + "' and customercode='" + commoncode + "'";

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public int getExptReasonListCount(long commonid) {
		int counts = 0;
		try {
			String sql = "select count(1) from express_set_common_exptreason where 1=1 ";
			if (commonid > -1) {
				sql += " and commonid=" + commonid;
			}
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}

	public void exptReasonDel(long exptid) {
		try {
			String sql = "delete from express_set_common_exptreason where id=" + exptid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CommonReason getExptReasonEntityByKey(long exptid) {
		CommonReason expt = null;
		try {
			String sql = "select * from express_set_common_exptreason where id=" + exptid;
			expt = jdbcTemplate.queryForObject(sql, new CommonReasonRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expt;
	}

	public void SaveExptReason(CommonReason expt, long id) {
		try {
			String sql = "update express_set_common_exptreason set customercode='" + expt.getCustomercode() + "'," + " expt_msg='" + expt.getExpt_msg() + "',expt_type=" + expt.getExpt_type()
					+ ",expt_code='" + expt.getExpt_code() + "' where id=" + id;
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public List<CommonReason> getExptReasonListByMoreKey(long support_key, long expt_type) {
		List<CommonReason> list = null;
		try {
			String sql = "select * from express_set_common_exptreason where 1=1 ";
			if (expt_type > -1) {
				sql += " and expt_type=" + expt_type;
			}
			sql += " and commonid='" + support_key + "' ";

			list = jdbcTemplate.query(sql, new CommonReasonRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	private final class CommonJointRowMapper implements RowMapper<CommonJoint> {
		@Override
		public CommonJoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonJoint expt = new CommonJoint();
			expt.setId(rs.getLong("id"));
			expt.setCommonid(rs.getLong("commonid"));
			expt.setExpt_code(rs.getString("expt_code"));
			expt.setExpt_msg(rs.getString("expt_msg"));
			expt.setCustomercode(rs.getString("customercode"));
			expt.setExpt_type(rs.getLong("expt_type"));
			expt.setCommonname(rs.getString("commonname"));
			expt.setReasoncontent(rs.getString("reasoncontent"));
			expt.setReasonid(rs.getLong("reasonid"));
			expt.setExptcode_remark(rs.getString("exptcode_remark"));
			expt.setExptcodeid(rs.getLong("exptcodeid"));
			return expt;
		}
	}
	
	public List<CommonJoint> getExpMatchListByKeyandcode(long type, long key, long page) {
		List<CommonJoint> list = null;
		try {
			String sql = "SELECT c.commonname, b.customercode,b.commonid,j.exptcodeid,r.reasonid,b.id,b.expt_type,r.reasoncontent,b.expt_msg,b.expt_code,j.exptcode_remark FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN  express_set_common_exptreason b ON j.`exptid`=b.`id`  INNER JOIN express_set_common c ON c.id=b.commonid WHERE 1=1";
			if (type >= 0) {
				sql += " and b.expt_type=" + type;
			}
			if (key > 0) {
				sql += " and b.commonid=" + key;
			}
			sql += " ORDER BY exptcodeid limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

			list = jdbcTemplate.query(sql, new CommonJointRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public void exptcodeJoint_delete(long exptid) {
		try {
			String sql = "delete from express_set_exptcode_joint where exptcodeid=" + exptid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CommonJoint getExpMatchListByKeyEdit(int exptcodeid) {
		CommonJoint list = null;
		try {
			String sql = "SELECT c.commonname, b.customercode,b.commonid,j.exptcodeid,r.reasonid,b.id,b.expt_type,r.reasoncontent,b.expt_msg,b.expt_code,j.exptcode_remark FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN  express_set_common_exptreason b ON j.`exptid`=b.`id`  INNER JOIN express_set_common c ON c.id=b.commonid WHERE 1=1"
					+ " AND exptcodeid=" + exptcodeid;
			list = jdbcTemplate.queryForObject(sql, new CommonJointRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public void updateCommonJointCodeReason(CommonJoint expt, long exptcodeid) {
		try {
			String sql = "update express_set_exptcode_joint set reasonid=?,exptid=?,exptcode_remark=? " + " where exptcodeid=? ";
			jdbcTemplate.update(sql, expt.getReasonid(), expt.getExptid(), expt.getExptcode_remark(), exptcodeid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean IsExistsExptMatchFlag(long reasonid, long exptid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "SELECT count(1) FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN  express_set_common_exptreason b ON j.`exptid`=b.`id`  INNER JOIN express_set_common c ON c.id=b.commonid WHERE 1=1 and j.reasonid="
					+ reasonid + " and  j.exptid=" + exptid;
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public void createExptCodeReason(CommonJoint expt) {
		try {
			String sql = "insert into express_set_exptcode_joint(reasonid,exptid)" + " values(?,?) ";
			jdbcTemplate.update(sql, expt.getReasonid(), expt.getExptid());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CommonJoint getExpMatchListByKeyEditAndName(int exptcodeid, String codename) {
		CommonJoint list = null;
		try {
			String sql = "SELECT c.commonname, b.customercode,b.commonid,j.exptcodeid,r.reasonid,b.id,b.expt_type,r.reasoncontent,b.expt_msg,b.expt_code,j.exptcode_remark FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN  express_set_common_exptreason b ON j.`exptid`=b.`id`  INNER JOIN express_set_common c ON c.id=b.commonid WHERE 1=1 "
					+ " AND expt_code=" + exptcodeid + " AND customercode='" + codename + "' LIMIT 1";
			list = jdbcTemplate.queryForObject(sql, new CommonJointRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public int getExptReasonJointListCount() {
		int counts = 0;
		try {
			String sql = "SELECT count(1) FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN  express_set_common_exptreason b ON j.`exptid`=b.`id`  INNER JOIN express_set_common c ON c.id=b.commonid WHERE 1=1 ";
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}
	/**
	 * 暂时广州通路添加
	 * @param exptcodeid
	 * @param codename
	 * @return
	 */
	public CommonJoint getExceptReason(String exptcodeid, String codename) {
		CommonJoint commonJoint = null;
		try {
			String sql = "select v.reasoncontent as reasoncontent,v.reasontype as reasontype,v.reasonid as reasonid from express_set_b2c_exptreason r,express_set_exptcode_joint j,express_set_reason v where r.exptid=j.exptid  and j.reasonid=v.reasonid and r.customerid="+codename +"  and r.expt_code='" +exptcodeid + "'  LIMIT 0,1";
			commonJoint = jdbcTemplate.queryForObject(sql, new CommonRowMapper());
			
		} catch (Exception e) {
		}
		return commonJoint;

	}
	/**
	 * 广州通路需要添加
	 * @author wukong
	 *
	 */
	private final class CommonRowMapper implements RowMapper<CommonJoint> {
		@Override
		public CommonJoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonJoint expt = new CommonJoint();
			expt.setExpt_code(rs.getString("reasoncontent"));
			expt.setExpt_type(rs.getLong("reasontype"));
			expt.setReasonid(rs.getLong("reasonid"));
			return expt;
		}
	}
	
}
