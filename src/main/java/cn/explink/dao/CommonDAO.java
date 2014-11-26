package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Common;
import cn.explink.util.Page;

@Component
public class CommonDAO {

	private final class CommonRowMapper implements RowMapper<Common> {
		@Override
		public Common mapRow(ResultSet rs, int rowNum) throws SQLException {
			Common common = new Common();
			common.setId(rs.getLong("id"));
			common.setCommonname(rs.getString("commonname"));
			common.setCommonnumber(rs.getString("commonnumber"));
			common.setOrderprefix(rs.getString("orderprefix"));
			common.setCommonstate(rs.getLong("commonstate"));
			common.setBranchid(rs.getLong("branchid"));
			common.setUserid(rs.getLong("userid"));
			common.setPageSize(rs.getLong("pagesize"));
			common.setPrivate_key(rs.getString("private_key"));
			common.setIsopenflag(rs.getLong("isopenflag"));
			common.setFeedback_url(rs.getString("feedback_url"));
			common.setPhone(rs.getString("phone"));
			common.setLoopcount(rs.getLong("loopcount"));
			common.setIsasynchronous(rs.getLong("isasynchronous"));
			return common;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Common> getAllCommons() {
		try {
			return jdbcTemplate.query("select * from express_set_common", new CommonRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public List<Common> getCommonsByModelNames() {
		try {
			return jdbcTemplate.query("SELECT c.* FROM express_set_common c RIGHT JOIN express_set_commonmodel cm ON c.commonname=cm.modelname WHERE c.id>0", new CommonRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public String getCommonByPageWhereSql(String sql, String commonname, String commonnumber) {

		if (commonname.length() > 0 || commonnumber.length() > 0) {
			sql += " where ";
			StringBuffer str = new StringBuffer();
			if (commonname.length() > 0) {
				str.append(" and commonname like '%" + commonname + "%'");
			}
			if (commonnumber.length() > 0) {
				str.append(" and commonnumber = '" + commonnumber + "'");
			}
			sql += str.substring(4, str.length());
		}
		return sql;
	}

	public long getCommonCount(String commonname, String commonnumber) {
		String sql = "select count(1) from express_set_common";
		sql = this.getCommonByPageWhereSql(sql, commonname, commonnumber);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<Common> getCommonByPage(long page, String commonname, String commonnumber) {
		String sql = "select * from express_set_common ";
		sql = this.getCommonByPageWhereSql(sql, commonname, commonnumber);
		sql += " order by commonstate desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CommonRowMapper());
	}

	public void CreateCommon(String commonname, String commonnumber, String orderprefix, long branchid, long userid, String pageSise, String private_key, long isopenflag, String feedback_url,
			String phone, long loopcount, long isasynchronous) {
		jdbcTemplate.update("insert into express_set_common(commonname,commonnumber,orderprefix,branchid,userid,pagesize,private_key,isopenflag,feedback_url,phone,loopcount,isasynchronous) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?)", commonname, commonnumber, orderprefix, branchid, userid, pageSise, private_key, isopenflag, feedback_url, phone, loopcount, isasynchronous);
	}

	public List<Common> getCommonByCommonname(String commonname, String commonnumber) {
		return jdbcTemplate.query("select * from express_set_common where commonname = ? or commonnumber=?", new CommonRowMapper(), commonname, commonnumber.toUpperCase());
	}

	public void saveCommon(String commonname, String commonnumber, String orderprefix, long branchid, long userid, long id, String pageSize, String private_key, long isopenflag, String feedback_url,
			String phone, long loopcount, long isasynchronous) {
		jdbcTemplate.update("update express_set_common set commonname=?,commonnumber=?,orderprefix=?,"
				+ "branchid=?,userid=?,pagesize=?,private_key=? ,isopenflag=?,feedback_url=?, phone=?,loopcount=?,isasynchronous=? where id =?", commonname, commonnumber, orderprefix, branchid,
				userid, pageSize, private_key, isopenflag, feedback_url, phone, loopcount, isasynchronous, id);
	}

	public Common getCommonById(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_common where id = ?", new CommonRowMapper(), id);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public void delCommon(long id) {
		jdbcTemplate.update("update express_set_common set commonstate=(commonstate+1)%2 where id=? ", id);
	}

	public List<Common> getCommonByBranchid(long branchid) {
		return jdbcTemplate.query("select * from express_set_common where branchid = ? ", new CommonRowMapper(), branchid);
	}

	public List<Common> getCommonByBranchidAndNotID(long branchid, long id) {
		return jdbcTemplate.query("select * from express_set_common where branchid = ? and id<>?", new CommonRowMapper(), branchid, id);
	}

	public Common getCommonByCommonnumber(String commonnumber) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_common where  commonnumber=?  limit 0,1", new CommonRowMapper(), commonnumber);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public Common getCommonByCommonName(String commonname) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_common where commonname =?", new CommonRowMapper(), commonname);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}
}
