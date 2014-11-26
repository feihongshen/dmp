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
import cn.explink.domain.TransferReasonStastics;
import cn.explink.domain.TransferReasonStastics;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.Page;

@Component
public class TransferReasonStasticsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ReasonRowMapper implements RowMapper<TransferReasonStastics> {

		@Override
		public TransferReasonStastics mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransferReasonStastics reason = new TransferReasonStastics();
			reason.setId(rs.getInt("id"));
			reason.setTransferreasonid(rs.getInt("transferreasonid"));
			reason.setCwb(rs.getString("cwb"));
			reason.setInwarehousetime(rs.getString("inwarehousetime"));
			reason.setOutwarehousetime(rs.getString("outwarehousetime"));
			reason.setNowtime(rs.getString("nowtime"));
			return reason;
		}
	}

	public void createTransferReasonStastics(TransferReasonStastics transferReasonStastics) {

		jdbcTemplate.update("insert into transferreason_stastics(cwb,inwarehousetime,outwarehousetime,transferreasonid,nowtime) values(?,?)", transferReasonStastics.getCwb(),
				transferReasonStastics.getInwarehousetime(), transferReasonStastics.getOutwarehousetime(), transferReasonStastics.getTransferreasonid(), transferReasonStastics.getNowtime());
	}

	public void updateTransferReasonStastics(TransferReasonStastics transfer) {

		jdbcTemplate.update("update transferreason_stastics set outwarehousetime=?,transferReasonid =?,nowtime=? where cwb=?", transfer.getOutwarehousetime(), transfer.getTransferreasonid(),
				transfer.getNowtime(), transfer.getCwb());
	}

}
