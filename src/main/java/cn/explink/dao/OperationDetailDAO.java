package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OperationDetail;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Component
public class OperationDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OperationDetailRowMapper implements RowMapper<OperationDetail> {
		@Override
		public OperationDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperationDetail operationDetail = new OperationDetail();
			operationDetail.setId(rs.getLong("id"));
			operationDetail.setTimeid(rs.getLong("timeid"));
			operationDetail.setBranchid(rs.getLong("branchid"));
			return operationDetail;
		}
	}

	public void creOperationDetail(long timeid, long branchid) {
		jdbcTemplate.update("insert into express_set_operation_detail (timeid,branchid) values(?,?)", timeid, branchid);
	}

	public void deleteOperationDetailByTimeid(long timeid) {
		String sql = "delete from  express_set_operation_detail where timeid=?";
		jdbcTemplate.update(sql, timeid);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<Map<String, Object>> getDetailBranch(long timeid, String branchids) {
		String sql = "SELECT a.branchid,b.branchname FROM express_set_operation_detail a,express_set_branch b WHERE a.branchid=b.branchid ";
		if (timeid > 0) {
			sql += " and a.timeid=" + timeid;
		}
		if (!"".equals(branchids)) {
			sql += " and a.branchid in (" + branchids + ")";
		}
		// if(nextbranchid==0||nextbranchid==-1){
		// sql += " and nextbranchid<>0 ";
		// }else{
		// sql += " and nextbranchid ="+nextbranchid;
		// }
		// if(cwbstate>-1){
		// sql += " and cwbstate="+cwbstate;
		// }
		return jdbcTemplate.queryForList(sql);
	}
}
