package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Punish;
import cn.explink.util.Page;

@Component
public class PunishDAO {

	private final class PunishRowMapper implements RowMapper<Punish> {
		@Override
		public Punish mapRow(ResultSet rs, int rowNum) throws SQLException {
			Punish punish = new Punish();
			punish.setId(rs.getLong("id"));
			punish.setCwb(rs.getString("cwb"));
			punish.setPunishid(rs.getLong("punishid"));
			punish.setBranchid(rs.getLong("branchid"));
			punish.setUserid(rs.getLong("userid"));
			punish.setPunishtime(rs.getLong("punishtime"));
			punish.setPunishlevel(rs.getLong("punishlevel"));
			punish.setPunishfee(rs.getBigDecimal("punishfee"));
			punish.setRealfee(rs.getBigDecimal("realfee"));
			punish.setPunishcontent(rs.getString("punishcontent"));
			punish.setCreateuser(rs.getLong("createuser"));
			punish.setCreatetime(rs.getString("createtime").length() > 0 ? rs.getString("createtime").substring(0, rs.getString("createtime").length() - 2) : "");
			punish.setState(rs.getInt("state"));
			return punish;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Punish> getAllPunish() {
		String sql = "select * from express_ops_punish_detail where 1=1 ";
		return this.jdbcTemplate.query(sql, new PunishRowMapper());
	}

	public List<Punish> getPunishList(String cwb, long punishid, long userid, long branchid, long punishlevel, int state, long page) {

		String sql = "select * from express_ops_punish_detail where 1=1 ";
		sql += this.creConditions(cwb, punishid, userid, branchid, punishlevel, state);
		sql += " order by createtime desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new PunishRowMapper());
	}

	public int getPunishCount(String cwb, long punishid, long userid, long branchid, long punishlevel, int state, long page) {

		String sql = "select count(*) from express_ops_punish_detail where 1=1 ";
		sql += this.creConditions(cwb, punishid, userid, branchid, punishlevel, state);
		return this.jdbcTemplate.queryForInt(sql);
	}

	private String creConditions(String cwb, long punishid, long userid, long branchid, long punishlevel, int state) {

		String sql = "";
		if ((cwb != null) && (cwb.length() > 0)) {
			sql += " and cwb='" + cwb + "'";
		}
		if (userid > 0) {
			sql += " and userid=" + userid;
		}
		if (branchid > 0) {
			sql += " and branchid=" + branchid;
		}
		if (punishid > 0) {
			sql += " and punishid=" + punishid;
		}
		if (punishlevel > 0) {
			sql += " and punishlevel=" + punishlevel;
		}
		if (state > -1) {
			sql += " and state=" + state;
		}
		return sql;
	}

	public int deletePunish(long id) {
		String sql = "delete from express_ops_punish_detail where id=" + id;
		return this.jdbcTemplate.update(sql);
	}

	public int createPunish(Punish pu) throws Exception {
		String sql = " insert into express_ops_punish_detail(cwb,punishid,branchid,userid,punishtime,punishlevel,punishfee,punishcontent,realfee,createuser,createtime) values(?,?,?,?,?,?,?,?,?,?,NOW())";
		return this.jdbcTemplate.update(sql, pu.getCwb(), pu.getPunishid(), pu.getBranchid(), pu.getUserid(), pu.getPunishtime(), pu.getPunishlevel(),
				pu.getPunishfee() == null ? 0 : pu.getPunishfee(), pu.getPunishcontent(), pu.getRealfee() == null ? 0 : pu.getRealfee(), pu.getCreateuser());
	}

	public int importPunish(Punish pu) throws Exception {
		String sql = " insert into express_ops_punish_detail(cwb,punishid,branchid,userid,punishtime,punishlevel,punishfee,punishcontent,realfee,createuser,createtime,state) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql, pu.getCwb(), pu.getPunishid(), pu.getBranchid(), pu.getUserid(), pu.getPunishtime(), pu.getPunishlevel(),
				pu.getPunishfee() == null ? 0 : pu.getPunishfee(), pu.getPunishcontent(), pu.getRealfee() == null ? 0 : pu.getRealfee(), pu.getCreateuser(), pu.getCreatetime(), pu.getState());
	}

	public int updatePunish(Punish pu) throws Exception {
		String sql = " update  express_ops_punish_detail set punishid=?,branchid=?,userid=?,punishtime=?,punishlevel=?,punishfee=?,punishcontent=?,realfee=? where id=?";
		return this.jdbcTemplate.update(sql, pu.getPunishid(), pu.getBranchid(), pu.getUserid(), pu.getPunishtime(), pu.getPunishlevel(), pu.getPunishfee() == null ? 0 : pu.getPunishfee(),
				pu.getPunishcontent(), pu.getRealfee() == null ? 0 : pu.getRealfee(), pu.getId());
	}

	public Punish getPunishById(int id) {
		try {
			String sql = "select * from express_ops_punish_detail where id= " + id;
			return this.jdbcTemplate.queryForObject(sql, new PunishRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public List<Punish> getPunishByCwb(String cwb) {
		String sql = "select * from express_ops_punish_detail where cwb= ?";
		return this.jdbcTemplate.query(sql, new PunishRowMapper(), cwb);
	}

	public List<Punish> getPunishforExcel(String cwb, long punishid, long userid, long branchid, long punishlevel, int state) {
		String sql = "select * from express_ops_punish_detail where 1=1 ";
		sql += this.creConditions(cwb, punishid, userid, branchid, punishlevel, state);
		return this.jdbcTemplate.query(sql, new PunishRowMapper());
	}

	public int updateStatePunish(int id, int state) {
		String sql = " update  express_ops_punish_detail set state=? where id=?";
		return this.jdbcTemplate.update(sql, state, id);
	}

	public int updateStateBatchPunish(String id, int state) {
		String sql = " update  express_ops_punish_detail set state=? where id in (" + id + ")";
		return this.jdbcTemplate.update(sql, state);
	}

	public List<Punish> getPunishByPunishid(int punishid) {
		String sql = "select * from express_ops_punish_detail where punishid= " + punishid;
		return this.jdbcTemplate.query(sql, new PunishRowMapper());
	}
}
