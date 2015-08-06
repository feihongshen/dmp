package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PunishInsideOperationinfo;

@Component
public class PunishInsideOperationinfoDao {
	private final class PunishInsideInfoRowMapper implements RowMapper<PunishInsideOperationinfo>{
		@Override
		public PunishInsideOperationinfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PunishInsideOperationinfo punishInsideOperationinfo=new PunishInsideOperationinfo();
			punishInsideOperationinfo.setId(rs.getLong("id"));
			punishInsideOperationinfo.setDetailid(rs.getLong("detailid"));
			punishInsideOperationinfo.setOperationdescribe(rs.getString("operationdescribe"));
			punishInsideOperationinfo.setOperationtype(rs.getLong("operationtype"));
			punishInsideOperationinfo.setOperationuserid(rs.getLong("operationuserid"));
			punishInsideOperationinfo.setShensutype(rs.getLong("shensutype"));
			punishInsideOperationinfo.setShensudate(rs.getString("shensudate"));
			return punishInsideOperationinfo;
		}
	}
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * 根据detailid来查找操作记录(暂时使用在对内扣罚申诉记录上)
	 * @param detailid
	 * @return
	 */
	public List<PunishInsideOperationinfo> findByDetailId(long detailid){
		String sql="select * from express_ops_punishInside_operationinfo where detailid=?";
		try {
			return this.jdbcTemplate.query(sql, new PunishInsideInfoRowMapper(), detailid);
		} catch (DataAccessException e) {
			return new ArrayList<PunishInsideOperationinfo>(); 
		}
	}
	/**
	 * 对内扣罚插入一条数据（暂时为申诉的数据）
	 * @param punishInsideOperationinfo
	 * @return
	 */
	public int insertIntoOperationInfo(final PunishInsideOperationinfo punishInsideOperationinfo){
		String sql="insert into express_ops_punishInside_operationinfo (detailid,operationuserid,operationtype,operationdescribe,shensutype,shensudate) values(?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, punishInsideOperationinfo.getDetailid());
				ps.setLong(2, punishInsideOperationinfo.getOperationuserid());
				ps.setLong(3, punishInsideOperationinfo.getOperationtype());
				ps.setString(4, punishInsideOperationinfo.getOperationdescribe());
				ps.setLong(5, punishInsideOperationinfo.getShensutype());
				ps.setString(6, punishInsideOperationinfo.getShensudate());
			}
		});
	}
}
