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
public class ExptReasonDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class ExptRowMapper implements RowMapper<ExptReason> {
		@Override
		public ExptReason mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExptReason expt = new ExptReason();
			expt.setExptid(rs.getLong("exptid"));
			expt.setCustomerid(rs.getLong("customerid"));
			// expt.setCustomername(rs.getString("customername"));
			expt.setExpt_code(rs.getString("expt_code"));
			expt.setExpt_msg(rs.getString("expt_msg"));
			expt.setExpt_remark(rs.getString("expt_remark"));
			expt.setExpt_type(rs.getInt("expt_type"));
			expt.setSupport_key(rs.getString("support_key"));
			expt.setCustomercode(rs.getString("customercode"));
			return expt;
		}
	}

	public List<ExptReason> getExptReasonListByPosExpt(long support_key) // pos
	{
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where 1=1 ";

			if (support_key > -1) {
				sql += " and support_key=" + support_key;
			}

			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<ExptReason> getExptReasonListByKey(long support_key, long page, int b2cflag) // 是否b2c
																								// 标示
																								// 1:是，0:不是(pos)。
	{
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where 1=1 ";
			if (support_key > -1 && b2cflag == 1) {
				sql += " and customerid=" + support_key;
			}
			if (support_key > -1 && b2cflag == 0) {
				sql += " and support_key=" + support_key;
			}
			sql += " order by support_key  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public int getExptReasonListCount(long support_key, int b2c_flag) {
		int counts = 0;
		try {
			String sql = "select count(1) from express_set_b2c_exptreason where 1=1 ";
			if (support_key > -1 && b2c_flag == 1) {
				sql += " and customerid=" + support_key;
			}
			if (support_key > -1 && b2c_flag == 0) {
				sql += " and support_key=" + support_key;
			}
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}

	public boolean IsExistsExptReasonFlag(long support_key, String expt_code, int b2c_flag, String expt_type) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_b2c_exptreason ";
			sql += "where  expt_code='" + expt_code + "' and expt_type='" + expt_type + "' ";
			if (b2c_flag == 1) {
				sql += " and customerid='" + support_key + "' ";
			}
			if (b2c_flag == 0) {
				sql += " and support_key='" + support_key + "' ";
			}

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public boolean IsExistsExptReasonExceptOwnFlag(String support_key, String expt_code, long exptid, int b2c_flag) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from express_set_b2c_exptreason where  expt_code='" + expt_code + "' and exptid<>" + exptid;
			if (b2c_flag == 1) {
				sql += " and customerid='" + support_key + "' ";
			}
			if (b2c_flag == 0) {
				sql += " and support_key='" + support_key + "' ";
			}
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public void createExptReason(ExptReason expt) {
		try {
			String sql = "insert into express_set_b2c_exptreason(support_key,customerid,expt_code,expt_msg,expt_type,expt_remark,customercode)" + " values(?,?,?,?,?,?,?) ";
			jdbcTemplate.update(sql, expt.getSupport_key(), expt.getCustomerid(), expt.getExpt_code(), expt.getExpt_msg(), expt.getExpt_type(), expt.getExpt_remark(), expt.getCustomercode());
		} catch (Exception e) {
		}

	}

	public void exptReasonDel(long exptid) {
		try {
			String sql = "delete from express_set_b2c_exptreason where exptid=" + exptid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ExptReason getExptReasonEntityByKey(long exptid) {
		ExptReason expt = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where exptid=" + exptid;
			expt = jdbcTemplate.queryForObject(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expt;

	}

	public void SaveExptReason(ExptReason expt, long exptid) {
		try {
			String sql = "update express_set_b2c_exptreason set customerid=" + expt.getCustomerid() + ",expt_code='" + expt.getExpt_code() + "'," + " expt_msg='" + expt.getExpt_msg() + "',expt_type="
					+ expt.getExpt_type() + ",expt_remark='" + expt.getExpt_remark() + "',customercode='" + expt.getCustomercode() + "' " + " where exptid=" + exptid;
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public List<ExptReason> getExptReasonListByMoreKey(long support_key, long expt_type, int b2c_flag) {
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where 1=1 ";
			if (expt_type > -1) {
				sql += " and expt_type=" + expt_type;
			}
			if (b2c_flag == 1) {
				sql += " and customerid='" + support_key + "' ";
			}
			if (b2c_flag == 0) {
				sql += "  and  support_key='" + support_key + "' ";
			}
			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	// 支付宝POS对接用到
	public List<ExptReason> getExptReasonListByPos(String customerids) {
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where customerid in (" + customerids + ") ";
			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	// 支付宝POS对接用到
	public List<ExptReason> getExptReasonListByTypeAndCustomerid(String customerids, String type) {
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where customerid in (" + customerids + ") and expt_type=" + type;
			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	// 支付宝POS对接用到
	public ExptReason getExptReasonCodeByPos(String exptcode, int pos_name) {
		List<ExptReason> list = null;
		try {
			String sql = "select * from express_set_b2c_exptreason where expt_code='" + exptcode + "' and  support_key='" + pos_name + "' ";
			list = jdbcTemplate.query(sql, new ExptRowMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list != null & list.size() > 0 ? list.get(0) : null;

	}

}
