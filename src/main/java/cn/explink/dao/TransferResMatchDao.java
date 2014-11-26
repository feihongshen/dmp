package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.TransferReason;
import cn.explink.domain.TransferResMatch;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.Page;

@Component
public class TransferResMatchDao {

	private final class ReasonRowMapper implements RowMapper<TransferResMatch> {

		@Override
		public TransferResMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransferResMatch reason = new TransferResMatch();
			reason.setId(rs.getInt("id"));
			reason.setReasonid(rs.getInt("reasonid"));
			reason.setReasonname(rs.getString("reasonname"));
			reason.setTransferreasonname(rs.getString("transferreasonname"));
			reason.setTransferReasonid(rs.getInt("transferReasonid"));
			reason.setRemark(rs.getString("remark"));

			return reason;
		}
	}

	private final class transferResason implements RowMapper<TransferReason> {

		@Override
		public TransferReason mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransferReason reason = new TransferReason();
			reason.setId(rs.getLong("id"));
			reason.setReasoncode(rs.getString("reasoncode"));
			reason.setReasonname(rs.getString("reasonname"));
			reason.setRemark(rs.getString("remark"));

			return reason;
		}
	}

	private final class TransferResMatchs implements RowMapper<TransferResMatch> {

		@Override
		public TransferResMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransferResMatch transfer = new TransferResMatch();
			transfer.setId(rs.getInt("id"));
			transfer.setReasonid(rs.getInt("reasonid"));
			transfer.setTransferReasonid(rs.getInt("transferReasonid"));
			transfer.setRemark(rs.getString("remark"));
			return transfer;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<TransferReason> getSystemTransferResList() {
		try {
			String sql = "select * from system_transfer_reason ";
			return jdbcTemplate.query(sql, new transferResason());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<TransferResMatch> getReasonMatchByPage(long page, String matchreaonsname) {
		String sql = "select m.id,m.reasonid,r.reasoncontent as reasonname,s.reasonname as transferreasonname,transferReasonid,m.remark   "
				+ " from match_transfer_reason m,system_transfer_reason s,express_set_reason r where r.reasontype=" + ReasonTypeEnum.ChangeTrains.getValue() + ""
				+ " and r.reasonid=m.reasonid and s.id=m.transferReasonid ";
		if (matchreaonsname.length() > 0) {
			sql += " and s.reasonname like '%" + matchreaonsname + "%'";
		}
		sql += " order by id desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<TransferResMatch> reasonList = jdbcTemplate.query(sql, new ReasonRowMapper());
		return reasonList;
	}

	public long getReasonMatchCountByPage(String matchreaonsname) {
		String sql = "select count(1) " + " from match_transfer_reason m,system_transfer_reason s,express_set_reason r where r.reasontype=" + ReasonTypeEnum.ChangeTrains.getValue() + ""
				+ " and r.reasonid=m.reasonid and s.id=m.transferReasonid ";
		if (matchreaonsname.length() > 0) {
			sql += " and  s.reasonname like '%" + matchreaonsname + "%'";
		}

		return jdbcTemplate.queryForInt(sql);
	}

	public TransferResMatch getTransferResMatch(long id) {
		try {
			String sql = "select m.id,m.reasonid,r.reasoncontent as reasonname,s.reasonname as transferreasonname,transferReasonid,m.remark  "
					+ " from match_transfer_reason m,system_transfer_reason s,express_set_reason r where r.reasontype=" + ReasonTypeEnum.ChangeTrains.getValue() + ""
					+ " and r.reasonid=m.reasonid and s.id=m.transferReasonid and m.id=" + id;

			return jdbcTemplate.queryForObject(sql, new ReasonRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public TransferResMatch getTransferResMatchByReasonid(long zhongzhuanid) {
		try {
			String sql = "select * from match_transfer_reason where reasonid=? limit 1";

			return jdbcTemplate.queryForObject(sql, new TransferResMatchs(), zhongzhuanid);
		} catch (Exception e) {
			return null;
		}
	}

	public void saveReason(long reasonid, long transferReasonid, long id) {

		jdbcTemplate.update("update match_transfer_reason set reasonid=?,transferReasonid =? where id=?", reasonid, transferReasonid, id);
	}

	public void creReason(long reasonid, long transferReasonid) {

		jdbcTemplate.update("insert into match_transfer_reason(reasonid,transferReasonid) values(?,?)", reasonid, transferReasonid);
	}

	public void deleteReason(long id) {

		jdbcTemplate.update("delete from match_transfer_reason where id =?", id);
	}

	public long IsExistsExptMatchFlag(long reasonid, long transferReasonid) {
		int counts = 0;
		try {
			String sql = "select count(1)  from match_transfer_reason where reasonid=" + reasonid + " and  transferReasonid=" + transferReasonid;
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return counts;

	}

	public long IsExistsReasonFlag(long reasonid) {
		int counts = 0;
		try {
			String sql = "select count(1)  from match_transfer_reason where reasonid=" + reasonid;
			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return counts;

	}
}
