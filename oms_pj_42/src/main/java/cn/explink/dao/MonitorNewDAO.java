package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.dto.MonitorNewDTO;

@Component
public class MonitorNewDAO {
	private final class MonnitorMapper implements RowMapper<MonitorNewDTO> {
		@Override
		public MonitorNewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorNewDTO monDto = new MonitorNewDTO();
			monDto.setBranchid(rs.getLong("branchid"));
			monDto.setBranchname(rs.getString("branchname"));
			monDto.setCustomername(rs.getString("customername"));
			monDto.setCustomerid(rs.getLong("customerid"));

			monDto.setDaoruCountsum(rs.getLong("daoruCountsum"));
			monDto.setDaoruCaramountsum(rs.getBigDecimal("daoruCaramountsum"));
			monDto.setKufangCountsum(rs.getLong("weirukuCountsum"));
			monDto.setKufangCaramountsum(rs.getBigDecimal("weirukuCaramountsum"));
			monDto.setKufangCountsum(rs.getLong("kufangCountsum"));
			monDto.setKufangCaramountsum(rs.getBigDecimal("kufangCaramountsum"));
			monDto.setZaituCountsum(rs.getLong("zaituCountsum"));
			monDto.setZaituCaramountsum(rs.getBigDecimal("zaituCaramountsum"));
			monDto.setZhandianCountsum(rs.getLong("zhandianCountsum"));
			monDto.setZhandianCaramountsum(rs.getBigDecimal("zhandianCaramountsum"));
			monDto.setXiaojianyuanCountsum(rs.getLong("xiaojianyuanCountsum"));
			monDto.setXiaojianyuanCaramountsum(rs.getBigDecimal("xiaojianyuanCaramountsum"));
			monDto.setTuihuozhanCountsum(rs.getLong("tuihuozhanCountsum"));
			monDto.setTuihuozhanCaramountsum(rs.getBigDecimal("tuihuozhanCaramountsum"));
			monDto.setZhongzhuanzhanCountsum(rs.getLong("zhongzhuanzhanCountsum"));
			monDto.setZhongzhuanzhanCaramountsum(rs.getBigDecimal("zhongzhuanzhanCaramountsum"));
			monDto.setChenggongCountsum(rs.getLong("chenggongCountsum"));
			monDto.setChenggongCaramountsum(rs.getBigDecimal("chenggongCaramountsum"));
			monDto.setDiushiCountsum(rs.getLong("diushiCountsum"));
			monDto.setDiushiCaramountsum(rs.getBigDecimal("diushiCaramountsum"));
			monDto.setYichangCountsum(rs.getLong("yichangCountsum"));
			monDto.setYichangCaramountsum(rs.getBigDecimal("yichangCaramountsum"));
			monDto.setChaCountsum(rs.getLong("chaCountsum"));
			monDto.setChaCaramountsum(rs.getBigDecimal("chaCaramountsum"));
			return monDto;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getDeliverWhereSql(String sql, String startdate, String enddate, String emaildateid) {
		if (startdate.length() > 0 || enddate.length() > 0 || emaildateid.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (startdate.length() > 0) {
				w.append(" AND emaildate>='" + startdate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" AND emaildate<='" + enddate + "' ");
			}
			if (emaildateid.length() > 0) {
				w.append(" AND emaildateid IN(" + emaildateid + ")");
			}
			sql += w;
		}
		return sql;
	}

	public List<MonitorNewDTO> getShujujiankong(String startdate, String enddate, String emaildateid) {
		String sql = "SELECT " + "branchid," + "branchname," + "customername, " + "customerid, " + "SUM(CASE WHEN (flowordertype >=1 and flowordertype <>5) THEN 1 ELSE 0 END) AS daoruCountsum,"
				+ "SUM(CASE WHEN (flowordertype >=1 and flowordertype <>5) THEN receivablefee ELSE 0 END) AS daoruCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN(1) THEN 1 ELSE 0 END) AS weirukuCountsum, " + "SUM(CASE WHEN flowordertype IN(1) THEN receivablefee ELSE 0 END) AS weirukuCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS kufangCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS kufangCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zaituCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zaituCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhandianCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhandianCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS xiaojianyuanCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS xiaojianyuanCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS tuihuozhanCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS tuihuozhanCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhongzhuanzhanCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhongzhuanzhanCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS chenggongCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS chenggongCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS diushiCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS diushiCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS yichangCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS yichangCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS chaCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS chaCaramountsum, "
				+ " FROM express_ops_cwb_detail " + "WHERE state=1 ";
		sql = getDeliverWhereSql(sql, startdate, enddate, emaildateid);
		return jdbcTemplate.query(sql, new MonnitorMapper());
	}

	public List<MonitorNewDTO> getShujuZujiankong(String startdate, String enddate, String emaildateid) {
		String sql = "SELECT " + "branchid," + "branchname," + "customername, " + "customerid, " + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS daoruCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS daoruCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS weirukuCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS weirukuCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS kufangCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS kufangCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zaituCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zaituCaramountsum, " + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhandianCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhandianCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS xiaojianyuanCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS xiaojianyuanCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS tuihuozhanCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS tuihuozhanCaramountsum, " + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhongzhuanzhanCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhongzhuanzhanCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS chenggongCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS chenggongCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS diushiCountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS diushiCaramountsum," + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS yichangCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS yichangCaramountsum, " + "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS chaCountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS chaCaramountsum, " + " FROM express_ops_cwb_detail " + "WHERE state=1 ";
		sql = getDeliverWhereSql(sql, startdate, enddate, emaildateid);
		return jdbcTemplate.query(sql, new MonnitorMapper());
	}

}
