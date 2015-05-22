package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;
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
			branch.setBankcard(rs.getString("bankcard"));
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

			branch.setZhongzhuanid(rs.getLong("zhongzhuanid"));
			branch.setTuihuoid(rs.getLong("tuihuoid"));
			branch.setCaiwuid(rs.getLong("caiwuid"));

			branch.setArrearagepayupaudit(rs.getBigDecimal("arrearagepayupaudit"));
			branch.setPosarrearagepayupaudit(rs.getBigDecimal("posarrearagepayupaudit"));
			branch.setBindmsksid(rs.getInt("bindmsksid"));

			branch.setAccounttype(rs.getInt("accounttype"));
			branch.setAccountexcesstype(rs.getInt("accountexcesstype"));
			branch.setAccountexcessfee(rs.getBigDecimal("accountexcessfee"));
			branch.setAccountbranch(rs.getLong("accountbranch"));
			branch.setCredit(rs.getBigDecimal("credit"));
			branch.setBalance(rs.getBigDecimal("balance"));
			branch.setDebt(rs.getBigDecimal("debt"));
			branch.setBalancevirt(rs.getBigDecimal("balancevirt"));
			branch.setDebtvirt(rs.getBigDecimal("debtvirt"));
			branch.setPrescription24(rs.getLong("prescription24"));
			branch.setPrescription48(rs.getLong("prescription48"));
			branch.setBrancharea(rs.getString("brancharea"));
			branch.setBranchstreet(rs.getString("branchstreet"));
			branch.setBacktime(rs.getLong("backtime"));
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
	 * @return
	 */
	@CacheEvict(value = "branchCache", key = "#branchId")
	public int saveBranchCwbtobranchid(long branchId, String toBranchIds) {
		return this.jdbcTemplate.update("update express_set_branch set cwbtobranchid = ? where `branchid` = ? order by branchname ", toBranchIds, branchId);
	}

	public Branch getBranchByBranchid(long branchid) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_branch where branchid=? ", new BranchRowMapper(), branchid);
		} catch (Exception ee) {
			return new Branch();
		}
	}

	public Branch getBranchByBranchidLock(long branchid) {
		return this.jdbcTemplate.queryForObject("SELECT * from express_set_branch where branchid=? for update ", new BranchRowMapper(), branchid);
	}

	public List<Branch> getBranchByIdsAndId(String branchids, long branchid) {
		try {
			return this.jdbcTemplate.query("SELECT * from express_set_branch where branchid in(" + branchids + ") and branchid=? ", new BranchRowMapper(), branchid);
		} catch (Exception ee) {
			return null;
		}
	}

	public Branch getBranchByBranchname(String branchname) {
		try {
			Branch branchList = this.jdbcTemplate.queryForObject("SELECT * from express_set_branch where branchname=? and brancheffectflag='1' ", new BranchRowMapper(), branchname);
			return branchList;
		} catch (DataAccessException e) {
			return new Branch();
		}
	}

	public List<Branch> getBranchByBranchnameCheck(String branchname) {
		List<Branch> branchList = this.jdbcTemplate.query("SELECT * from express_set_branch where branchname=?", new BranchRowMapper(), branchname);
		return branchList;
	}

	public List<Branch> getBranchByBranchnameMoHu(String branchname) {
		List<Branch> branchList = this.jdbcTemplate.query("SELECT * from express_set_branch where branchname like '%" + branchname + "%' and brancheffectflag='1' ", new BranchRowMapper());
		return branchList;
	}

	public List<Branch> getBranchByBranchcodeCheck(String branchcode) {
		return this.jdbcTemplate.query("SELECT * from express_set_branch where branchcode=? and branchcode <> '' ", new BranchRowMapper(), branchcode);
	}

	public List<Branch> getBranchByBranchcode(String branchcode) {
		return this.jdbcTemplate.query("SELECT * from express_set_branch where branchcode=? and branchcode <> '' and brancheffectflag='1' ", new BranchRowMapper(), branchcode);
	}

	public List<Branch> getAllBranches() {
		return this.jdbcTemplate.query("SELECT * FROM express_set_branch ORDER BY sitetype ASC, CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC", new BranchRowMapper());
	}

	public List<Branch> getAllEffectBranches() {
		return this.jdbcTemplate.query("SELECT * FROM express_set_branch where brancheffectflag='1' ORDER BY sitetype ASC, CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC",
				new BranchRowMapper());
	}

	public List<Branch> getBranchesByKuFangAndZhanDian() {
		return this.jdbcTemplate.query(
				"select * from express_set_branch where sitetype in (" + BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.ZhongZhuan.getValue() + ","
						+ BranchEnum.TuiHuo.getValue() + ") and brancheffectflag='1' ORDER BY  CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC", new BranchRowMapper());
	}

	@Cacheable(value = "branchCache", key = "#branchid")
	public Branch getBranchById(long branchid) {
		try {
			List<Branch> list = this.jdbcTemplate.query("select * from express_set_branch where branchid =?", new BranchRowMapper(), branchid);
			if ((list != null) && (list.size() > 0)) {
				return list.get(0);
			} else {
				return new Branch();
			}
		} catch (EmptyResultDataAccessException e) {
			return new Branch();
		}
	}

	@Cacheable(value = "branchCache", key = "#branchid")
	public Branch getEffectBranchById(long branchid) {
		try {
			List<Branch> list = this.jdbcTemplate.query("select * from express_set_branch where branchid =? and brancheffectflag='1' ", new BranchRowMapper(), branchid);
			if ((list != null) && (list.size() > 0)) {
				return list.get(0);
			} else {
				return new Branch();
			}
		} catch (EmptyResultDataAccessException e) {
			return new Branch();
		}
	}

	@CacheEvict(value = "branchCache", key = "#branch.branchid")
	public void saveBranch(final Branch branch) {

		this.jdbcTemplate
				.update("update express_set_branch set branchname=?,branchaddress=?,branchcontactman=?,branchphone=?,"
						+ "branchmobile=?,branchfax=?,branchemail=?,contractflag=?,contractrate=?,cwbtobranchid=?,branchcode=?,"
						+ "payfeeupdateflag=?,backtodeliverflag=?,branchpaytoheadflag=?,branchfinishdayflag=?,creditamount=?,branchwavfile=?,"
						+ "brancheffectflag=?,noemailimportflag=?,errorcwbdeliverflag=?,errorcwbbranchflag=?,branchcodewavfile=?,importwavtype=?,"
						+ "exportwavtype=?,branchinsurefee=?,branchprovince=?,branchcity=?,noemaildeliverflag=?,sendstartbranchid=?,sitetype=?,checkremandtype=?,"
						+ "branchmatter=?,accountareaid=?,functionids=?,zhongzhuanid=?,tuihuoid=?,caiwuid=?,bankcard=?,accounttype=?,accountexcesstype=?,accountexcessfee=?,accountbranch=?,credit=?,backtime=? "
						+ " where branchid=?", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, branch.getBranchname());
						ps.setString(2, branch.getBranchaddress());
						ps.setString(3, branch.getBranchcontactman());
						ps.setString(4, branch.getBranchphone());
						ps.setString(5, branch.getBranchmobile());
						ps.setString(6, branch.getBranchfax());
						ps.setString(7, branch.getBranchemail());
						ps.setString(8, branch.getContractflag());
						ps.setBigDecimal(9, branch.getContractrate());
						ps.setString(10, branch.getCwbtobranchid());
						ps.setString(11, branch.getBranchcode());
						ps.setString(12, branch.getPayfeeupdateflag());
						ps.setString(13, branch.getBacktodeliverflag());
						ps.setString(14, branch.getBranchpaytoheadflag());
						ps.setString(15, branch.getBranchfinishdayflag());
						ps.setBigDecimal(16, branch.getCreditamount());
						ps.setString(17, branch.getBranchwavfile());
						ps.setString(18, "1");
						ps.setString(19, branch.getNoemailimportflag());
						ps.setString(20, branch.getErrorcwbdeliverflag());
						ps.setString(21, branch.getErrorcwbbranchflag());
						ps.setString(22, branch.getBranchcodewavfile());
						ps.setString(23, branch.getImportwavtype());
						ps.setString(24, branch.getExportwavtype());
						ps.setBigDecimal(25, branch.getBranchinsurefee());
						ps.setString(26, branch.getBranchprovince());
						ps.setString(27, branch.getBranchcity());
						ps.setString(28, branch.getNoemaildeliverflag());
						ps.setInt(29, branch.getSendstartbranchid());
						ps.setInt(30, branch.getSitetype());
						ps.setLong(31, branch.getCheckremandtype());
						ps.setString(32, branch.getBranchmatter());
						ps.setInt(33, branch.getAccountareaid());
						ps.setString(34, branch.getFunctionids());
						ps.setLong(35, branch.getZhongzhuanid());
						ps.setLong(36, branch.getTuihuoid());
						ps.setLong(37, branch.getCaiwuid());
						ps.setString(38, branch.getBankcard());

						ps.setInt(39, branch.getAccounttype());
						ps.setInt(40, branch.getAccountexcesstype());
						ps.setBigDecimal(41, branch.getAccountexcessfee());
						ps.setLong(42, branch.getAccountbranch());
						ps.setBigDecimal(43, branch.getCredit());

						ps.setLong(44, branch.getBacktime());
						ps.setLong(45, branch.getBranchid());

					}
				});

	}

	@CacheEvict(value = "branchCache", key = "#branch.branchid")
	public void saveBranchNoFile(final Branch branch) {

		this.jdbcTemplate.update("update express_set_branch set branchname=?,branchaddress=?,branchcontactman=?,branchphone=?,"
				+ "branchmobile=?,branchfax=?,branchemail=?,contractflag=?,contractrate=?,cwbtobranchid=?,branchcode=?,"
				+ "payfeeupdateflag=?,backtodeliverflag=?,branchpaytoheadflag=?,branchfinishdayflag=?,creditamount=?,"
				+ "brancheffectflag=?,noemailimportflag=?,errorcwbdeliverflag=?,errorcwbbranchflag=?,branchcodewavfile=?,importwavtype=?,"
				+ "exportwavtype=?,branchinsurefee=?,branchprovince=?,branchcity=?,noemaildeliverflag=?,sendstartbranchid=?,sitetype=?,checkremandtype=?,"
				+ "branchmatter=?,accountareaid=?,functionids=?,zhongzhuanid=?,tuihuoid=?,caiwuid=?,bankcard=?,bindmsksid=?,"
				+ "accounttype=?,accountexcesstype=?,accountexcessfee=?,accountbranch=?,credit=?,prescription24=?,prescription48=?,branchcity=?,brancharea=?,branchstreet=?,backtime=? "
				+ " where branchid=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, branch.getBranchname());
				ps.setString(2, branch.getBranchaddress());
				ps.setString(3, branch.getBranchcontactman());
				ps.setString(4, branch.getBranchphone());
				ps.setString(5, branch.getBranchmobile());
				ps.setString(6, branch.getBranchfax());
				ps.setString(7, branch.getBranchemail());
				ps.setString(8, branch.getContractflag());
				ps.setBigDecimal(9, branch.getContractrate());
				ps.setString(10, branch.getCwbtobranchid());
				ps.setString(11, branch.getBranchcode());
				ps.setString(12, branch.getPayfeeupdateflag());
				ps.setString(13, branch.getBacktodeliverflag());
				ps.setString(14, branch.getBranchpaytoheadflag());
				ps.setString(15, branch.getBranchfinishdayflag());
				ps.setBigDecimal(16, branch.getCreditamount());
				ps.setString(17, "1");
				ps.setString(18, branch.getNoemailimportflag());
				ps.setString(19, branch.getErrorcwbdeliverflag());
				ps.setString(20, branch.getErrorcwbbranchflag());
				ps.setString(21, branch.getBranchcodewavfile());
				ps.setString(22, branch.getImportwavtype());
				ps.setString(23, branch.getExportwavtype());
				ps.setBigDecimal(24, branch.getBranchinsurefee());
				ps.setString(25, branch.getBranchprovince());
				ps.setString(26, branch.getBranchcity());
				ps.setString(27, branch.getNoemaildeliverflag());
				ps.setInt(28, branch.getSendstartbranchid());
				ps.setInt(29, branch.getSitetype());
				ps.setLong(30, branch.getCheckremandtype());
				ps.setString(31, branch.getBranchmatter());
				ps.setInt(32, branch.getAccountareaid());
				ps.setString(33, branch.getFunctionids());
				ps.setLong(34, branch.getZhongzhuanid());
				ps.setLong(35, branch.getTuihuoid());
				ps.setLong(36, branch.getCaiwuid());
				ps.setString(37, branch.getBankcard());
				ps.setInt(38, branch.getBindmsksid());

				ps.setInt(39, branch.getAccounttype());
				ps.setInt(40, branch.getAccountexcesstype());
				ps.setBigDecimal(41, branch.getAccountexcessfee());
				ps.setLong(42, branch.getAccountbranch());
				ps.setBigDecimal(43, branch.getCredit());

				ps.setLong(44, branch.getPrescription24());
				ps.setLong(45, branch.getPrescription48());

				ps.setString(46, branch.getBranchcity());
				ps.setString(47, branch.getBrancharea());
				ps.setString(48, branch.getBranchstreet());
				ps.setLong(49, branch.getBacktime());
				ps.setLong(50, branch.getBranchid());
			}
		});

	}

	/**
	 * 修改branch的欠货款
	 * 
	 * @param brach
	 */
	@CacheEvict(value = "branchCache", key = "#branchid")
	public void saveBranchArrearageHuo(BigDecimal arrearagehuo, long branchid) {
		this.jdbcTemplate.update("update express_set_branch set arrearagehuo=?" + " where branchid=?", arrearagehuo, branchid);
	}

	/**
	 * 修改branch的欠赔款
	 * 
	 * @param brach
	 */
	@CacheEvict(value = "branchCache", key = "#branchid")
	public void saveBranchArrearagePei(BigDecimal arrearagepei, long branchid) {
		this.jdbcTemplate.update("update express_set_branch set arrearagepei=?" + " where branchid=?", arrearagepei, branchid);
	}

	/**
	 * 修改branch的欠罚款
	 * 
	 * @param brach
	 */
	@CacheEvict(value = "branchCache", key = "#branchid")
	public void saveBranchArrearageFa(BigDecimal arrearagefa, long branchid) {
		this.jdbcTemplate.update("update express_set_branch set arrearagefa=?" + " where branchid=?", arrearagefa, branchid);
	}

	public long creBranch(final Branch branch) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_branch(branchid,branchname,branchaddress,branchcontactman,branchphone,branchmobile,"
						+ "branchfax,branchemail,contractflag,contractrate,cwbtobranchid,branchcode,payfeeupdateflag,backtodeliverflag,"
						+ "branchpaytoheadflag,branchfinishdayflag,creditamount,branchwavfile,brancheffectflag,noemailimportflag,errorcwbdeliverflag,"
						+ "errorcwbbranchflag,branchcodewavfile,importwavtype,exportwavtype,branchinsurefee,branchprovince,branchcity,noemaildeliverflag,"
						+ "sendstartbranchid,sitetype,checkremandtype,branchmatter,accountareaid,zhongzhuanid,tuihuoid,caiwuid,functionids,bankcard,bindmsksid,"
						+ "accounttype,accountexcesstype,accountexcessfee,accountbranch,credit,prescription24,prescription48,brancharea,branchstreet,backtime) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "branchid" });
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
				ps.setString(19, "1");
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
				ps.setInt(31, branch.getSitetype());
				ps.setInt(32, branch.getCheckremandtype());
				ps.setString(33, branch.getBranchmatter());
				ps.setInt(34, branch.getAccountareaid());
				ps.setLong(35, branch.getZhongzhuanid());
				ps.setLong(36, branch.getTuihuoid());
				ps.setLong(37, branch.getCaiwuid());
				ps.setString(38, branch.getFunctionids());
				ps.setString(39, branch.getBankcard());
				ps.setInt(40, branch.getBindmsksid());
				ps.setInt(41, branch.getAccounttype());
				ps.setInt(42, branch.getAccountexcesstype());
				ps.setBigDecimal(43, branch.getAccountexcessfee());
				ps.setLong(44, branch.getAccountbranch());
				ps.setBigDecimal(45, branch.getCredit());
				ps.setLong(46, branch.getPrescription24());
				ps.setLong(47, branch.getPrescription48());
				ps.setString(48, branch.getBrancharea());
				ps.setString(49, branch.getBranchstreet());
				ps.setLong(50, branch.getBacktime());
				return ps;
			}
		}, key);
		branch.setBranchid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	private String getBranchByPageWhereSql(String sql, String branchname, String branchaddress) {

		if ((branchname.length() > 0) || (branchaddress.length() > 0)) {
			sql += " where";
			if ((branchname.length() > 0) && (branchaddress.length() > 0)) {
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
		sql += " order by branchid desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<Branch> branchlist = this.jdbcTemplate.query(sql, new BranchRowMapper());
		return branchlist;
	}

	public long getBranchCount(String branchname, String branchaddress) {
		String sql = "select count(1) from express_set_branch";
		sql = this.getBranchByPageWhereSql(sql, branchname, branchaddress);
		return this.jdbcTemplate.queryForInt(sql);
	}

	public List<Branch> getBanchByBranchidForCwbtobranchid(long branchid) {
		String sql1 = "select cwbtobranchid from express_set_branch where branchid=" + branchid;
		String sql1str = this.jdbcTemplate.queryForObject(sql1, String.class);
		if (sql1str.length() > 0) {
			sql1str = "," + sql1str;
		}

		String sql2 = "select * from express_set_branch where branchid in(0" + sql1str + ") ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC";

		List<Branch> branchlist = this.jdbcTemplate.query(sql2, new BranchRowMapper());
		return branchlist;
	}

	public List<Branch> getBanchByBranchidForStock(String sitetypes) {
		String sql1 = "select * from express_set_branch where sitetype in(" + sitetypes + ") and brancheffectflag='1' ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC ";
		List<Branch> branchlist = this.jdbcTemplate.query(sql1, new BranchRowMapper());
		return branchlist;
	}

	public List<Branch> getBranchBySiteType(long sitetype) {
		try {
			String sql = "select * from express_set_branch where sitetype=? and brancheffectflag='1' ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), sitetype);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Branch> getAllBranchBySiteType(long sitetype) {
		try {
			String sql = "select * from express_set_branch where sitetype=? ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), sitetype);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Branch> getBranchAllzhandian(String sitetype) {
		try {
			String sql = "select * from express_set_branch where sitetype in(" + sitetype + ") order by sitetype ASC ,CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
			return this.jdbcTemplate.query(sql, new BranchRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Branch> getBranchEffectAllzhandian(String sitetype) {
		try {
			String sql = "select * from express_set_branch where sitetype in(" + sitetype
					+ ") and brancheffectflag='1' order by sitetype ASC ,CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
			return this.jdbcTemplate.query(sql, new BranchRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<Branch> getBranchByMyKuFang(long brancheid) {
		try {
			String sql = "select * from express_set_branch where sitetype=? and branchid=? and brancheffectflag='1' ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), +BranchEnum.KuFang.getValue(), brancheid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 相应当前站点的财务机构或者中转站或者退货站，根据当前的用户所属机构的相对应这些类型的机构
	 * 
	 * @param sitetype
	 * @return
	 */
	public List<Branch> getBranchByMyBranchIsCaiwu(long branchid) {
		try {
			String sql = "select * from express_set_branch where  branchid in (select caiwuid from express_set_branch where branchid=? ) and brancheffectflag='1' ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 相应当前站点的财中转站，根据当前的用户所属机构的相对应这些类型的机构
	 * 
	 * @param sitetype
	 * @return
	 */
	public List<Branch> getBranchByMyBranchIsZhongzhuan(long branchid) {
		try {
			String sql = "select * from express_set_branch where  branchid in (select zhongzhuanid from express_set_branch where branchid=? ) and brancheffectflag='1' ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 相应当前站点的退货站，根据当前的用户所属机构的相对应这些类型的机构
	 * 
	 * @param sitetype
	 * @return
	 */
	public List<Branch> getBranchByMyBranchIsTuihuo(long branchid) {
		try {
			String sql = "select * from express_set_branch where  branchid in (select tuihuoid from express_set_branch where branchid=? ) and brancheffectflag='1' ";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 根据财务所在的站点id查询 所有的属于财务的站点。
	 * 
	 * @param caiwubranchid
	 * @return
	 */
	public List<Branch> getBranchBelowToCaiwu(long caiwubranchid) {
		try {
			String sql = "select * from express_set_branch where  caiwuid=?";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), caiwubranchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public Branch getQueryBranchByBranchid(long branchid) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_branch where branchid=? ", new BranchRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return new Branch();
		}
	}

	public List<Branch> getQueryBranchByBranchidAndUserid(long userid, long sitetype) {
		String sql = "SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype=? and ub.userid=? and b.brancheffectflag='1' order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new BranchRowMapper(), sitetype, userid);
	}

	public List<Branch> getQueryBranchByBranchsiteAndUserid(long userid, String sitetype) {
		String sql = "SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype in(" + sitetype
				+ ") and ub.userid=? and b.brancheffectflag='1' order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new BranchRowMapper(), userid);
	}

	public List<Branch> getBranchBelowToCaiwuAndUser(long caiwubranchid, long userid) {
		String sql = "SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.caiwuid =? and ub.userid=? order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new BranchRowMapper(), caiwubranchid, userid);
	}

	public List<Branch> getBranchToUser(long userid) {
		String sql = "SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE  ub.userid=? and b.brancheffectflag='1' order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new BranchRowMapper(), userid);
	}

	public List<Branch> getBranchNotInBranchid(String branchids) {
		if (branchids.length() > 0) {
			List<Branch> branchList = this.jdbcTemplate.query("select * from express_set_branch where branchid not in(" + branchids
					+ ") and sitetype=2 order by CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC", new BranchRowMapper());
			return branchList;
		} else {
			return null;
		}

	}

	public List<Branch> getBranchByBranchids(String branchids) {
		if (branchids.length() > 0) {
			List<Branch> branchList = this.jdbcTemplate.query("select * from express_set_branch where branchid in(" + branchids
					+ ") and sitetype=2 order by CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC", new BranchRowMapper());
			return branchList;
		} else {
			return null;
		}
	}
	public List<Branch> getBranchByBranchidsNoType(String branchids) {
		if (branchids.length() > 0) {
			List<Branch> branchList = this.jdbcTemplate.query("select * from express_set_branch where branchid in(" + branchids
					+ ")  order by CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC", new BranchRowMapper());
			return branchList;
		} else {
			return null;
		}
	}

	@CacheEvict(value = "branchCache", key = "#branchid")
	public int updateQiankuan(long branchid, BigDecimal arrearagepayupaudit, BigDecimal posarrearagepayupaudit) {
		String sql = "update express_set_branch set arrearagepayupaudit=arrearagepayupaudit+" + arrearagepayupaudit + ",posarrearagepayupaudit=posarrearagepayupaudit+" + posarrearagepayupaudit
				+ " where branchid=? ";

		return this.jdbcTemplate.update(sql, branchid);

	}

	// ==============================修改订单使用的方法 start
	// ==================================
	/**
	 * 重置审核状态 修改站点帐户欠款金额表字段
	 * 
	 * @param branchid
	 *            站点id
	 * @param arrearagepayupaudit
	 *            现金欠款金额
	 * @param posarrearagepayupaudit
	 *            pos欠款金额
	 */
	@CacheEvict(value = "branchCache", key = "#branchid")
	public void updateForChongZhiShenHe(long branchid, BigDecimal arrearagepayupaudit, BigDecimal posarrearagepayupaudit) {
		this.jdbcTemplate.update("update express_set_branch set arrearagepayupaudit=?,posarrearagepayupaudit=? where branchid=? ", arrearagepayupaudit, posarrearagepayupaudit, branchid);
	}

	// ==============================修改订单使用的方法 start
	// ==================================

	public void delBranch(long branchid) {
		this.jdbcTemplate.update("update express_set_branch set brancheffectflag=(brancheffectflag+1)%2 where branchid=?", branchid);
	}

	// public List<Branch> getBranchByUseridAndAccounttype(long userid,String
	// sitetype,long accounttype){
	// String sql =
	// "SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype in("+sitetype+") and ub.userid=? and b.accounttype=? and b.brancheffectflag='1' order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
	// return jdbcTemplate.query(sql,new BranchRowMapper(),userid,accounttype);
	// }

	// public List<Branch> getBranchByBranchidAccounttype(long branchid,long
	// accounttype){
	// String sql =
	// "SELECT * FROM express_set_branch WHERE accountbranch=? AND accounttype=? ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
	// return jdbcTemplate.query(sql,new
	// BranchRowMapper(),branchid,accounttype);
	// }

	public List<Branch> getBranchByBranchidAccounttype(long branchid, long accounttype, String sitetype, long userid) {
		String sql = "SELECT * FROM express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid = ub.branchid " + "WHERE b.sitetype IN (" + sitetype
				+ ") AND ub.userid =? AND b.accounttype=? AND b.brancheffectflag = '1' AND b.accountbranch=? " + "ORDER BY CONVERT(b.branchname USING gbk) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new BranchRowMapper(), userid, accounttype, branchid);
	}

	// public List<Branch> getBranchByUseridSitetype(long page,long
	// userid,String sitetype,String branchname){
	// String
	// sql="SELECT * from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype in("+sitetype+") and ub.userid=? ";
	// if(!("").equals(branchname)){
	// sql+=" and b.branchname like '%"+branchname+"%' ";
	// }
	// sql += " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER;
	// return jdbcTemplate.query(sql,new BranchRowMapper(),userid);
	// }
	//
	// public long getBranchByUseridSitetypeCount(long userid,String
	// sitetype,String branchname){
	// String
	// sql="SELECT count(1) FROM express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype in("+sitetype+") and ub.userid=? ";
	// if(!("").equals(branchname)){
	// sql+=" and b.branchname like '%"+branchname+"%' ";
	// }
	// return jdbcTemplate.queryForLong(sql,userid);
	// }

	// SELECT * FROM express_set_branch WHERE accountbranch=? AND accounttype=?
	// ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC
	public List<Branch> getBranchByAccountbranch(long page, String branchname, long branchid, long accounttype, String sitetype, long userid) {
		try {
			String sql = "SELECT * FROM express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid = ub.branchid " + "WHERE b.sitetype IN (" + sitetype
					+ ") AND ub.userid =? AND b.accounttype=? AND b.brancheffectflag = '1' AND b.accountbranch=? ";
			if (!("").equals(branchname)) {
				sql += " and branchname like '%" + branchname + "%' ";
			}
			sql += "ORDER BY CONVERT(b.branchname USING gbk) COLLATE gbk_chinese_ci ASC";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), userid, accounttype, branchid);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getBranchByAccountbranchCount(String branchname, long branchid, long accounttype, String sitetype, long userid) {
		try {
			String sql = "SELECT count(1) FROM express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid = ub.branchid " + "WHERE b.sitetype IN (" + sitetype
					+ ") AND ub.userid =? AND b.accounttype=? AND b.brancheffectflag = '1' AND b.accountbranch=? ";
			if (!("").equals(branchname)) {
				sql += " and branchname like '%" + branchname + "%' ";
			}
			return this.jdbcTemplate.queryForLong(sql, userid, accounttype, branchid);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void updateForFee(long branchid, BigDecimal balance, BigDecimal debt) {
		this.jdbcTemplate.update("UPDATE express_set_branch SET balance=?,debt=? WHERE branchid=?", balance, debt, branchid);
	}

	public void updateForVirt(long branchid, BigDecimal balancevirt, BigDecimal debtvirt) {
		this.jdbcTemplate.update("UPDATE express_set_branch SET balancevirt=?,debtvirt=? WHERE branchid=?", balancevirt, debtvirt, branchid);
	}

	public void updateForFeeAndVirt(long branchid, BigDecimal balance, BigDecimal debt, BigDecimal balancevirt, BigDecimal debtvirt) {
		this.jdbcTemplate.update("UPDATE express_set_branch SET balance=?,debt=?,balancevirt=?,debtvirt=? WHERE branchid=?", balance, debt, balancevirt, debtvirt, branchid);
	}

	public List<Branch> getAccessableBranch(long userId, int siteType) {
		try {
			String sql = "SELECT b.* FROM express_set_branch b, express_set_user_branch ub WHERE b.branchid = ub.branchid AND ub.userid = ? AND b.sitetype = ? order by sitetype ASC ,CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
			return this.jdbcTemplate.query(sql, new BranchRowMapper(), userId, siteType);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public Map<Long, String> getBranchNameMap(Set<Long> branchIdSet) {
		String sql = "select branchid,branchname from  express_set_branch where branchid in (" + this.getBranchIdInPara(branchIdSet) + ")";
		Map<Long, String> nameMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new NameMapHandler(nameMap));

		return nameMap;
	}

	public Map<Long, String> getBranchNameMap(Set<Long> branchIdSet, int page) {
		int start = (page - 1) * 10;
		String sql = "select branchid,branchname from  express_set_branch where branchid in (" + this.getBranchIdInPara(branchIdSet) + ") limit " + start + ",10";
		Map<Long, String> nameMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new NameMapHandler(nameMap));

		return nameMap;
	}

	public Map<Long, String> getBranchNameMap(int siteType) {
		String sql = "select branchid,branchname from  express_set_branch where siteType = " + siteType + " and brancheffectflag = 1";
		Map<Long, String> nameMap = new LinkedHashMap<Long, String>();
		this.jdbcTemplate.query(sql, new NameMapHandler(nameMap));

		return nameMap;
	}

	public Map<Long, String> getBranchNameMap(long userid, int siteType) {

		String sql = "SELECT b.branchid as branchid,b.branchname as branchname  from express_set_user_branch ub LEFT OUTER JOIN express_set_branch b ON b.branchid=ub.branchid WHERE b.sitetype in("
				+ siteType + ") and ub.userid=? and b.brancheffectflag='1' order by CONVERT( b.branchname USING gbk ) COLLATE gbk_chinese_ci ASC";
		Map<Long, String> nameMap = new LinkedHashMap<Long, String>();

		this.jdbcTemplate.query(sql, new NameMapHandler(nameMap), userid);

		return nameMap;
	}

	public List<Long> getAllBranchId() {
		String sql = " select branchid from express_set_branch where sitetype = 2";
		return this.jdbcTemplate.queryForList(sql, Long.class);
	}

	public List<Long> getAllBranchAndWarehouseId() {
		String sql = " select branchid from express_set_branch where sitetype in(1,2)";
		return this.jdbcTemplate.queryForList(sql, Long.class);
	}

	public Map<Long, String> getBranchAndWarehouseNameMap() {
		String sql = "select branchid , branchname from express_set_branch where sitetype in(1,2) and brancheffectflag = 1";
		Map<Long, String> nameMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new NameMapHandler(nameMap));

		return nameMap;
	}

	public String getBranchName(long branchId) {
		String sql = "select branchname from express_set_branch where branchid = ?";
		Object[] paras = new Object[] { branchId };

		return this.jdbcTemplate.queryForObject(sql, paras, String.class);
	}

	private String getBranchIdInPara(Set<Long> idSet) {
		StringBuilder inPara = new StringBuilder();
		for (Long id : idSet) {
			inPara.append(id.toString());
			inPara.append(",");
		}
		return inPara.substring(0, inPara.length() - 1);
	}

	private class NameMapHandler implements RowCallbackHandler {

		private Map<Long, String> nameMap = null;

		public NameMapHandler(Map<Long, String> nameMap) {
			this.nameMap = nameMap;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			long id = rs.getLong("branchid");
			String name = rs.getString("branchname");
			this.getNameMap().put(Long.valueOf(id), name);
		}

		private Map<Long, String> getNameMap() {
			return this.nameMap;
		}

	}

	public List<Branch> getBranchByPage(long page, String branchname, String branchaddress, int sitetype, int pagesize) {
		String sql = "select * from express_set_branch";
		sql += " where 1=1 ";
		if (branchname.length() > 0) {
			sql += " and branchname like '%" + branchname + "%' ";
		}
		if (branchaddress.length() > 0) {
			sql += " and branchaddress like '%" + branchaddress + "%' ";
		}
		if (sitetype > 0) {
			sql += " and sitetype=" + sitetype;
		}
		sql += " order by branchid desc limit " + ((page - 1) * pagesize) + " ," + pagesize;
		List<Branch> branchlist = this.jdbcTemplate.query(sql, new BranchRowMapper());
		return branchlist;
	}

	public long getBranchCount(String branchname, String branchaddress, int sitetype, int pagesize) {
		String sql = "select count(1) from express_set_branch";
		sql += " where 1=1 ";
		if (branchname.length() > 0) {
			sql += " and branchname like '%" + branchname + "%' ";
		}
		if (branchaddress.length() > 0) {
			sql += " and branchaddress like '%" + branchaddress + "%' ";
		}
		if (sitetype > 0) {
			sql += " and sitetype=" + sitetype;
		}
		return this.jdbcTemplate.queryForInt(sql);
	}
}
