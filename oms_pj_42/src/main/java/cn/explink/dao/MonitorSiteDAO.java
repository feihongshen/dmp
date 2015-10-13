package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.dto.MonitorSiteDTO;

@Component
public class MonitorSiteDAO {
	private final class MonnitorSiteMapper implements RowMapper<MonitorSiteDTO> {
		@Override
		public MonitorSiteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorSiteDTO monDto = new MonitorSiteDTO();
			monDto.setBranchid(rs.getLong("branchid"));
			monDto.setBranchname(rs.getString("branchname"));
			monDto.setCustomername(rs.getString("customername"));
			monDto.setCustomerid(rs.getLong("customerid"));
			monDto.setWeidaohuoCountsum(rs.getLong("weidaohuoCountsum"));
			monDto.setWeidaohuoCaramountsum(rs.getBigDecimal("weidaohuoCaramountsum"));
			monDto.setRukuweilingCountsum(rs.getLong("rukuweilingCountsum"));
			monDto.setRukuweilingCaramountsum(rs.getBigDecimal("rukuweilingCaramountsum"));
			monDto.setYouhuowudanCountsum(rs.getLong("youhuowudanCountsum"));
			monDto.setYouhuowudanCaramountsum(rs.getBigDecimal("youhuowudanCaramountsum"));
			monDto.setYichangdanCountsum(rs.getLong("yichangdanCountsum"));
			monDto.setYichangdanCaramountsum(rs.getBigDecimal("yichangdanCaramountsum"));
			monDto.setYoudanwuhuoCountsum(rs.getLong("youdanwuhuoCountsum"));
			monDto.setYoudanwuhuoCaramountsum(rs.getBigDecimal("youdanwuhuoCaramountsum"));
			monDto.setYilinghuoCountsum(rs.getLong("yilinghuoCountsum"));
			monDto.setYilinghuoCaramountsum(rs.getBigDecimal("yilinghuoCaramountsum"));
			monDto.setYiliudanCountsum(rs.getLong("yiliudanCountsum"));
			monDto.setYiliudanCaramountsum(rs.getBigDecimal("yiliudanCaramountsum"));
			monDto.setKucuntuihuoCountsum(rs.getLong("kucuntuihuoCountsum"));
			monDto.setKucuntuihuoCaramountsum(rs.getBigDecimal("kucuntuihuoCaramountsum"));
			monDto.setZhiliuCountsum(rs.getLong("zhiliuCountsum"));
			monDto.setZhiliuCaramountsum(rs.getBigDecimal("zhiliuCaramountsum"));
			monDto.setZhongzhuanCountsum(rs.getLong("zhongzhuanCountsum"));
			monDto.setZhongzhuanCaramountsum(rs.getBigDecimal("zhongzhuanCaramountsum"));
			monDto.setZaitutuihuoCountsum(rs.getLong("zaitutuihuoCountsum"));
			monDto.setZaituchaCaramountsum(rs.getBigDecimal("zaituchaCaramountsum"));
			monDto.setWeijiaokunCountsum(rs.getLong("weijiaokunCountsum"));
			monDto.setWeijiaokuanCaramountsum(rs.getBigDecimal("weijiaokuanCaramountsum"));
			monDto.setQiankuanCountsum(rs.getLong("qiankuanCountsum"));
			monDto.setQiankuanCaramountsum(rs.getBigDecimal("qiankuanCaramountsum"));
			return monDto;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getDeliverWhereSql(String sql, String startdate, String enddate, String emaildateid, String customerid, long branchid) {
		if (startdate.length() > 0 || enddate.length() > 0 || emaildateid.length() > 0 || customerid.length() > 0 || branchid > 0

		) {
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
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND branchid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	public List<MonitorSiteDTO> getSiteMomitor(String startdate, String enddate, String emaildateid, String customerid, long branchid) {
		String sql = "SELECT " + "branchid," + "branchname," + "customername, " + "customerid, " + "SUM(CASE WHEN (flowordertype <=6 ) THEN 1 ELSE 0 END) AS weidaohuoCountsum,"
				+ "SUM(CASE WHEN (flowordertype <=6 ) THEN receivablefee ELSE 0 END) AS weidaohuoCaramountsum," + "SUM(CASE WHEN flowordertype IN(7) THEN 1 ELSE 0 END) AS rukuweilingCountsum, "
				+ "SUM(CASE WHEN flowordertype IN(7) THEN receivablefee ELSE 0 END) AS rukuweilingCaramountsum," + "SUM(CASE WHEN flowordertype IN(8) THEN 1 ELSE 0 END) AS youhuowudanCountsum, "
				+ "SUM(CASE WHEN flowordertype IN(8) THEN receivablefee ELSE 0 END) AS youhuowudanCaramountsum," + "SUM(0) AS yichangdanCountsum," + "SUM(0) AS yichangdanCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS youdanwuhuoCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS youdanwuhuoCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS yilinghuoCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS yilinghuoCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS yiliudanCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS yiliudanCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS kucuntuihuoCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS kucuntuihuoCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhiliuCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhiliuCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zhongzhuanCountsum," + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zhongzhuanCaramountsum,"
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS zaitutuihuoCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS zaituchaCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS weijiaokunCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS weijiaokuanCaramountsum, "
				+ "SUM(CASE WHEN flowordertype IN() THEN 1 ELSE 0 END) AS qiankuanCountsum, " + "SUM(CASE WHEN flowordertype IN() THEN receivablefee ELSE 0 END) AS qiankuanCaramountsum, "
				+ " FROM express_ops_cwb_detail " + "WHERE state=1 ";
		sql = getDeliverWhereSql(sql, startdate, enddate, emaildateid, customerid, branchid);
		return jdbcTemplate.query(sql, new MonnitorSiteMapper());
	}

}
