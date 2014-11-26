package cn.explink.b2c.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ExptCodeJointDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class ExptRowMapper implements RowMapper<ExptCodeJoint> {
		@Override
		public ExptCodeJoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExptCodeJoint exptcode = new ExptCodeJoint();
			exptcode.setExptid(rs.getLong("exptid"));
			exptcode.setReasonid(rs.getLong("reasonid"));
			exptcode.setExptcode_remark(rs.getString("exptcode_remark"));
			exptcode.setExpt_type(rs.getLong("expt_type"));
			exptcode.setSupport_key(rs.getLong("support_key"));
			exptcode.setExpt_code(rs.getString("expt_code"));
			exptcode.setExpt_msg(rs.getString("expt_msg"));
			exptcode.setExpt_remark(rs.getString("expt_remark"));
			exptcode.setReasoncontent(rs.getString("reasoncontent"));
			exptcode.setExptcodeid(rs.getLong("exptcodeid"));
			exptcode.setCustomerid(rs.getLong("customerid"));
			exptcode.setCustomercode(rs.getString("customercode"));
			return exptcode;
		}
	}

	private final class ExptB2cRowMapper implements RowMapper<ExptCodeJoint> {
		@Override
		public ExptCodeJoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExptCodeJoint exptcode = new ExptCodeJoint();
			exptcode.setExpt_type(rs.getLong("expt_type"));
			exptcode.setExpt_code(rs.getString("expt_code"));
			exptcode.setExpt_msg(rs.getString("expt_msg"));
			return exptcode;
		}
	}

	// b2c专用
	public ExptCodeJoint getExpMatchByKey(long reasonid, String support_key, int expt_type) {
		ExptCodeJoint exptCodeJoint = null;
		List<ExptCodeJoint> list = null;
		try {
			String sql = "SELECT b.customercode,b.customerid,j.exptcodeid,r.reasonid,b.exptid,b.expt_type,r.reasoncontent,b.support_key,b.expt_code,b.expt_msg,b.expt_remark,j.exptcode_remark "
					+ " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN " + " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` "
					+ " WHERE 1=1 and r.reasonid=" + reasonid + " and b.customerid in (" + support_key + ") and b.expt_type=" + expt_type;

			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			exptCodeJoint = list.get(0);
		}
		return exptCodeJoint == null ? new ExptCodeJoint() : exptCodeJoint;

	}

	public List<ExptCodeJoint> getExpMatchListByKey(long expt_type, long supportkey, int b2c_flag) {
		List<ExptCodeJoint> list = null;
		try {
			String sql = "SELECT  b.customercode,b.customerid,j.exptcodeid,r.reasonid,b.exptid,b.expt_type,r.reasoncontent,b.support_key,b.expt_code,b.expt_msg,b.expt_remark,j.exptcode_remark "
					+ " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN " + " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` "
					+ " WHERE 1=1  ";
			if (expt_type != -1) {
				sql += " and b.expt_type=" + expt_type;
			}

			if (b2c_flag == 1) {
				sql += " and customerid='" + supportkey + "' ";
			}
			if (b2c_flag == 0) {
				sql += "  and  support_key='" + supportkey + "' ";
			}
			sql += " ORDER BY reasoncontent";

			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public ExptCodeJoint getExpMatchListByKeyEdit(int exptcodeid) {
		ExptCodeJoint list = null;
		try {
			String sql = "SELECT  b.customercode,b.customerid,j.exptcodeid,r.reasonid,b.exptid,b.expt_type,r.reasoncontent,b.support_key,b.expt_code,b.expt_msg,b.expt_remark,j.exptcode_remark "
					+ " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN " + " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` "
					+ " WHERE 1=1 and   exptcodeid=" + exptcodeid;
			list = jdbcTemplate.queryForObject(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public int getExpMatchListCount(long support_key) {
		int counts = 0;
		try {
			String sql = "select count(1) from express_set_b2c_exptreason where 1=1 ";
			if (support_key > -1) {
				sql += " and support_key=" + support_key;
			}
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}

	public boolean IsExistsExptMatchFlag(long reasonid, long exptid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_exptcode_joint where reasonid=" + reasonid + " and  exptid=" + exptid;
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public boolean IsExistsExptMatchFlag_update(long reasonid, long exptid, long exptcodeid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_exptcode_joint where reasonid=" + reasonid + " and  exptid=" + exptid + " and exptcodeid<>" + exptcodeid;
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public void createExptCodeReason(ExptCodeJoint expt) {
		try {
			String sql = "insert into express_set_exptcode_joint(reasonid,exptid,exptcode_remark)" + " values(?,?,?) ";
			jdbcTemplate.update(sql, expt.getReasonid(), expt.getExptid(), expt.getExptcode_remark());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateExptCodeReason(ExptCodeJoint expt, long exptcodeid) {
		try {
			String sql = "update express_set_exptcode_joint set reasonid=?,exptid=?,exptcode_remark=? " + " where exptcodeid=? ";
			jdbcTemplate.update(sql, expt.getReasonid(), expt.getExptid(), expt.getExptcode_remark(), exptcodeid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void exptcodeJoint_delete(long exptid) {
		try {
			String sql = "delete from express_set_exptcode_joint where exptcodeid=" + exptid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public ExptReason getExptReasonEntityByKey(long exptid){
	// ExptReason expt=null;
	// try {
	// String sql =
	// "select * from express_set_b2c_exptreason where exptid="+exptid;
	// expt= jdbcTemplate.queryForObject(sql, new ExptRowMapper());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return expt;
	//
	// }

	public void SaveExptReason(ExptReason expt, long exptid) {
		try {
			String sql = "update express_set_b2c_exptreason set customerid=" + expt.getCustomerid() + ",expt_code='" + expt.getExpt_code() + "'," + " expt_msg='" + expt.getExpt_msg() + "',expt_type="
					+ expt.getExpt_type() + ",expt_remark='" + expt.getExpt_remark() + "' " + " where exptid=" + exptid;
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 用于OMS存储反馈时的异常原因及编码 根据 customerid
	 * 
	 * @param reasonid
	 *            异常原因id
	 * @param customerid
	 *            供货商id
	 * @param expt_type
	 *            异常类型
	 * @return
	 */
	public ExptCodeJoint getExpMatchByKeyByOMS(long reasonid, long customerid, int expt_type) {
		ExptCodeJoint exptCodeJoint = null;
		List<ExptCodeJoint> list = null;
		try {
			String sql = "SELECT  b.customercode,b.expt_type,b.expt_code,b.expt_msg " + " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN "
					+ " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` " + " WHERE 1=1 and r.reasonid=" + reasonid + " and b.support_key=0 and b.customerid in (" + customerid
					+ ") and b.expt_type=" + expt_type;
			list = jdbcTemplate.query(sql, new ExptB2cRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			exptCodeJoint = list.get(0);
		}
		return exptCodeJoint == null ? new ExptCodeJoint() : exptCodeJoint;

	}

	/**
	 * POS查询 异常码
	 */
	public ExptCodeJoint getExpMatchListByPosCode(String extpt_code, int support_key) {
		List<ExptCodeJoint> list = null;
		try {
			String sql = "SELECT  b.customercode,b.customerid,j.exptcodeid,r.reasonid,b.exptid,b.expt_type,r.reasoncontent,b.support_key,b.expt_code,b.expt_msg,b.expt_remark,j.exptcode_remark "
					+ " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN " + " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` "
					+ " WHERE 1=1 and b.expt_code=? and b.support_key=? ";

			list = jdbcTemplate.query(sql, new ExptRowMapper(), extpt_code, support_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list != null && list.size() > 0 ? list.get(0) : new ExptCodeJoint();

	}

	/**
	 * 根据异常码查询滞留和拒收的原因
	 */
	public ExptCodeJoint getExpListByCodeandid(long extpt_code, long customerid) {
		List<ExptCodeJoint> list = null;
		try {
			String sql = "SELECT  b.customercode,j.exptcodeid,r.reasonid,b.exptid,b.expt_type,r.reasoncontent,b.support_key,b.expt_code,b.expt_msg,b.expt_remark,j.exptcode_remark "
					+ " FROM  express_set_reason r INNER JOIN  express_set_exptcode_joint j ON r.`reasonid`=j.`reasonid` INNER JOIN " + " express_set_b2c_exptreason b ON j.`exptid`=b.`exptid` "
					+ " WHERE 1=1 and b.expt_code=? and b.customerid=? ";

			list = jdbcTemplate.query(sql, new ExptRowMapper(), extpt_code, customerid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list != null && list.size() > 0 ? list.get(0) : null;

	}
}
