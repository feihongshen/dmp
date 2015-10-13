package cn.explink.dao;

import java.math.BigDecimal;
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
import cn.explink.domain.Branch;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class BranchDAO {

	private final class BranchRowMapper implements RowMapper<Branch> {
		@Override
		public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
			Branch branch = new Branch();
			branch.setBranchid(rs.getLong("branchid"));
			branch.setBranchname(StringUtil.nullConvertToEmptyString(rs.getString("branchname")));
			branch.setCwbtobranchid(rs.getString("cwbtobranchid"));
			branch.setFunctionids(rs.getString("functionids"));
			branch.setBranchprovince(rs.getString("branchprovince"));
			branch.setBranchcity(rs.getString("branchcity"));
			branch.setBranchaddress(rs.getString("branchaddress"));
			branch.setBranchcontactman(rs.getString("branchcontactman"));
			branch.setBranchphone(rs.getString("branchphone"));
			branch.setBranchmobile(rs.getString("branchmobile"));
			branch.setBranchfax(rs.getString("branchfax"));
			branch.setBranchemail(rs.getString("branchemail"));
			branch.setContractflag(rs.getString("contractflag"));
			branch.setPayfeeupdateflag(rs.getString("payfeeupdateflag"));
			branch.setBacktodeliverflag(rs.getString("backtodeliverflag"));
			branch.setBranchpaytoheadflag(rs.getString("branchpaytoheadflag"));
			branch.setBranchfinishdayflag(rs.getString("branchfinishdayflag"));
			branch.setBranchinsurefee(rs.getBigDecimal("branchinsurefee"));
			branch.setBranchwavfile(rs.getString("branchwavfile"));
			branch.setCreditamount(rs.getBigDecimal("creditamount"));
			branch.setBrancheffectflag(rs.getString("brancheffectflag"));
			branch.setContractrate(rs.getBigDecimal("contractrate"));
			branch.setBranchcode(rs.getString("branchcode"));
			branch.setNoemailimportflag(rs.getString("noemailimportflag"));
			branch.setErrorcwbdeliverflag(rs.getString("errorcwbdeliverflag"));
			branch.setErrorcwbbranchflag(rs.getString("errorcwbbranchflag"));
			branch.setBranchcodewavfile(StringUtil.nullConvertToEmptyString(rs.getString("branchcodewavfile")));
			branch.setImportwavtype(rs.getString("importwavtype"));
			branch.setExportwavtype(rs.getString("exportwavtype"));
			branch.setNoemaildeliverflag(rs.getString("noemaildeliverflag"));
			branch.setSendstartbranchid(rs.getInt("sendstartbranchid"));
			branch.setSitetype(rs.getInt("sitetype"));
			branch.setCheckremandtype(rs.getInt("checkremandtype"));
			branch.setBranchmatter(rs.getString("branchmatter"));
			branch.setAccountareaid(rs.getInt("accountareaid"));

			branch.setArrearagehuo(rs.getBigDecimal("arrearagehuo"));
			branch.setArrearagepei(rs.getBigDecimal("arrearagepei"));
			branch.setArrearagefa(rs.getBigDecimal("arrearagefa"));

			return branch;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存机构之间的流程关系
	 * 
	 * @param branchId
	 *            保存的对象机构id
	 * @param toBranchId
	 *            被关联的机构ID
	 * @param toBranchId
	 *            被关联的功能ID
	 * @return
	 */
	public int saveBranchCwbtobranchid(long branchId, String toBranchIds, String toFunctionIds) {
		return jdbcTemplate.update("update express_set_branch set cwbtobranchid = '" + toBranchIds + "',functionids = '" + toFunctionIds + "' where `branchid` = '" + branchId + "'");
	}

	public List<Branch> getBranchsByBranchIds(String branchIds) {
		List<Branch> branchs = jdbcTemplate.query("SELECT * from express_set_branch where branchid in (?)", new BranchRowMapper(), branchIds);
		return branchs;
	}

	public Branch getBranchByBranchid(long branchid) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_branch where branchid=?", new BranchRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Branch> getBranchsByBranchname(String branchname) {
		List<Branch> userList = jdbcTemplate.query("SELECT * from express_set_branch where branchname like '%" + branchname + "%'", new BranchRowMapper());
		return userList;
	}

	public List<Branch> getBranchsByBranchaddress(String branchaddress) {
		List<Branch> userList = jdbcTemplate.query("SELECT * from express_set_branch where branchname like '%" + branchaddress + "%'", new BranchRowMapper());
		return userList;

	}

	public List<Branch> getBranchByBranchnameCheck(String branchname) {
		List<Branch> branchList = jdbcTemplate.query("SELECT * from express_set_branch where branchname=?", new BranchRowMapper(), branchname);
		return branchList;
	}

	public List<Branch> getAllBranches() {
		return jdbcTemplate.query("select * from express_set_branch", new BranchRowMapper());
	}

	public List<Branch> getBranchsByBranchnameAndBranchaddress(String branchname, String branchaddress) {
		List<Branch> userList = jdbcTemplate.query("SELECT * from express_set_branch where branchname like '%" + branchname + "%' and branchaddress like '%" + branchaddress + "%'",
				new BranchRowMapper());
		return userList;
	}

	public Branch getBranchById(long branchid) {
		return jdbcTemplate.queryForObject("select * from express_set_branch where branchid = ?", new BranchRowMapper(), branchid);

	}

	public void saveBranch(final Branch brach) {

		jdbcTemplate.update("update express_set_branch set branchname=?,branchaddress=?,branchcontactman=?,branchphone=?,"
				+ "branchmobile=?,branchfax=?,branchemail=?,contractflag=?,contractrate=?,cwbtobranchid=?,branchcode=?,"
				+ "payfeeupdateflag=?,backtodeliverflag=?,branchpaytoheadflag=?,branchfinishdayflag=?,creditamount=?,branchwavfile=?,"
				+ "brancheffectflag=?,noemailimportflag=?,errorcwbdeliverflag=?,errorcwbbranchflag=?,branchcodewavfile=?,importwavtype=?,"
				+ "exportwavtype=?,branchinsurefee=?,branchprovince=?,branchcity=?,noemaildeliverflag=?,sendstartbranchid=?,functionids=?,sitetype=?,checkremandtype=?,"
				+ "branchmatter=?,accountareaid=?" + " where branchid=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, brach.getBranchname());
				ps.setString(2, brach.getBranchaddress());
				ps.setString(3, brach.getBranchcontactman());
				ps.setString(4, brach.getBranchphone());
				ps.setString(5, brach.getBranchmobile());
				ps.setString(6, brach.getBranchfax());
				ps.setString(7, brach.getBranchemail());
				ps.setString(8, brach.getContractflag());
				ps.setBigDecimal(9, brach.getContractrate());
				ps.setString(10, brach.getCwbtobranchid());
				ps.setString(11, brach.getBranchcode());
				ps.setString(12, brach.getPayfeeupdateflag());
				ps.setString(13, brach.getBacktodeliverflag());
				ps.setString(14, brach.getBranchpaytoheadflag());
				ps.setString(15, brach.getBranchfinishdayflag());
				ps.setBigDecimal(16, brach.getCreditamount());
				ps.setString(17, brach.getBranchwavfile());
				ps.setString(18, brach.getBrancheffectflag());
				ps.setString(19, brach.getNoemailimportflag());
				ps.setString(20, brach.getErrorcwbdeliverflag());
				ps.setString(21, brach.getErrorcwbbranchflag());
				ps.setString(22, brach.getBranchcodewavfile());
				ps.setString(23, brach.getImportwavtype());
				ps.setString(24, brach.getExportwavtype());
				ps.setBigDecimal(25, brach.getBranchinsurefee());
				ps.setString(26, brach.getBranchprovince());
				ps.setString(27, brach.getBranchcity());
				ps.setString(28, brach.getNoemaildeliverflag());
				ps.setInt(29, brach.getSendstartbranchid());
				ps.setString(30, brach.getFunctionids());
				ps.setInt(31, brach.getSitetype());
				ps.setLong(32, brach.getCheckremandtype());
				ps.setString(33, brach.getBranchmatter());
				ps.setInt(34, brach.getAccountareaid());
				ps.setLong(35, brach.getBranchid());

			}
		});

	}

	/**
	 * 修改branch的欠货款
	 * 
	 * @param brach
	 */
	public void saveBranchArrearageHuo(BigDecimal arrearagehuo, long branchid) {
		jdbcTemplate.update("update express_set_branch set arrearagehuo=?" + " where branchid=?", arrearagehuo, branchid);
	}

	/**
	 * 修改branch的欠赔款
	 * 
	 * @param brach
	 */
	public void saveBranchArrearagePei(BigDecimal arrearagepei, long branchid) {
		jdbcTemplate.update("update express_set_branch set arrearagepei=?" + " where branchid=?", arrearagepei, branchid);
	}

	/**
	 * 修改branch的欠罚款
	 * 
	 * @param brach
	 */
	public void saveBranchArrearageFa(BigDecimal arrearagefa, long branchid) {
		jdbcTemplate.update("update express_set_branch set arrearagefa=?" + " where branchid=?", arrearagefa, branchid);
	}

	public void creBranch(final Branch branch) {

		jdbcTemplate.update("insert into express_set_branch(branchid,branchname,branchaddress,branchcontactman,branchphone,branchmobile,"
				+ "branchfax,branchemail,contractflag,contractrate,cwbtobranchid,branchcode,payfeeupdateflag,backtodeliverflag,"
				+ "branchpaytoheadflag,branchfinishdayflag,creditamount,branchwavfile,brancheffectflag,noemailimportflag,errorcwbdeliverflag,"
				+ "errorcwbbranchflag,branchcodewavfile,importwavtype,exportwavtype,branchinsurefee,branchprovince,branchcity,noemaildeliverflag,"
				+ "sendstartbranchid,functionids,sitetype,checkremandtype,branchmatter,accountareaid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setLong(1, branch.getBranchid());
						ps.setString(2, branch.getBranchname());
						ps.setString(3, branch.getBranchaddress());
						ps.setString(4, branch.getBranchcontactman());
						ps.setString(5, branch.getBranchphone());
						ps.setString(6, branch.getBranchmobile());
						ps.setString(7, branch.getBranchfax());
						ps.setString(8, branch.getBranchemail());
						ps.setString(9, branch.getContractflag());
						ps.setBigDecimal(10, branch.getContractrate());
						ps.setString(11, branch.getCwbtobranchid());
						ps.setString(12, branch.getBranchcode());
						ps.setString(13, branch.getPayfeeupdateflag());
						ps.setString(14, branch.getBacktodeliverflag());
						ps.setString(15, branch.getBranchpaytoheadflag());
						ps.setString(16, branch.getBranchfinishdayflag());
						ps.setBigDecimal(17, branch.getCreditamount());
						ps.setString(18, branch.getBranchwavfile());
						ps.setString(19, branch.getBrancheffectflag());
						ps.setString(20, branch.getNoemailimportflag());
						ps.setString(21, branch.getErrorcwbdeliverflag());
						ps.setString(22, branch.getErrorcwbbranchflag());
						ps.setString(23, branch.getBranchcodewavfile());
						ps.setString(24, branch.getImportwavtype());
						ps.setString(25, branch.getExportwavtype());
						ps.setBigDecimal(26, branch.getBranchinsurefee());
						ps.setString(27, branch.getBranchprovince());
						ps.setString(28, branch.getBranchcity());
						ps.setString(29, branch.getNoemaildeliverflag());
						ps.setInt(30, branch.getSendstartbranchid());
						ps.setString(31, branch.getFunctionids());
						ps.setInt(32, branch.getSitetype());
						ps.setInt(33, branch.getCheckremandtype());
						ps.setString(34, branch.getBranchmatter());
						ps.setInt(35, branch.getAccountareaid());
					}
				});

	}

	private String getBranchByPageWhereSql(String sql, String branchname, String branchaddress) {

		if (branchname.length() > 0 || branchaddress.length() > 0) {
			sql += " where";
			if (branchname.length() > 0 && branchaddress.length() > 0) {
				sql += " branchname like '%" + branchname + "%' and branchaddress like '%" + branchaddress + "%'";
			} else {
				if (branchname.length() > 0) {
					sql += " branchname like '%" + branchname + "%' ";
				}
				if (branchaddress.length() > 0) {
					sql += " branchaddress like '%" + branchaddress + "%' ";
				}
			}
		}
		return sql;
	}

	public List<Branch> getBranchByPage(long page, String branchname, String branchaddress) {
		String sql = "select * from express_set_branch";
		sql = this.getBranchByPageWhereSql(sql, branchname, branchaddress);
		sql += " order by branchid desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<Branch> branchlist = jdbcTemplate.query(sql, new BranchRowMapper());
		return branchlist;
	}

	public long getBranchCount(String branchname, String branchaddress) {
		String sql = "select count(1) from express_set_branch";
		sql = this.getBranchByPageWhereSql(sql, branchname, branchaddress);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<Branch> getBanchByBranchidForCwbtobranchid(long branchid) {
		String sql1 = "select cwbtobranchid from express_set_branch where branchid=" + branchid;
		String sql2 = "";
		if (jdbcTemplate.queryForObject(sql1, String.class) == null || jdbcTemplate.queryForObject(sql1, String.class).length() == 0) {
			sql2 = "select * from express_set_branch where branchid=" + branchid;
		} else {
			sql2 = "select * from express_set_branch where branchid in(" + jdbcTemplate.queryForObject(sql1, String.class) + ")";

		}

		List<Branch> branchlist = jdbcTemplate.query(sql2, new BranchRowMapper());
		return branchlist;
	}

	public List<Branch> getBranchBySiteType(long sitetype) {
		try {
			String sql = "select * from express_set_branch where sitetype=" + sitetype;
			return jdbcTemplate.query(sql, new BranchRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	// public List<Branch> getBranchByUserId() {
	//
	// }
}
