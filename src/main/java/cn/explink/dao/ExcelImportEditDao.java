package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExcelImportEdit;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.StringUtil;

@Component
public class ExcelImportEditDao {
	private Logger logger = LoggerFactory.getLogger(ExcelImportEditDao.class);

	private final class EditMapper implements RowMapper<ExcelImportEdit> {

		@Override
		public ExcelImportEdit mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExcelImportEdit cwbOrder = new ExcelImportEdit();
			cwbOrder.setId(rs.getLong("id"));
			cwbOrder.setAddresscodeedittype(rs.getInt("addresscodeedittype"));
			cwbOrder.setCwb(rs.getString("cwb"));
			cwbOrder.setCwbordertypeid(rs.getString("cwbordertypeid"));
			cwbOrder.setEmaildate((StringUtil.nullConvertToEmptyString(rs.getString("emaildate"))));
			cwbOrder.setEmaildateid(rs.getInt("emaildateid"));
			cwbOrder.setExcelbranchid(rs.getLong("excelbranchid"));
			cwbOrder.setCustomerid(rs.getInt("customerid"));
			cwbOrder.setTranscwb(rs.getString("transcwb"));
			cwbOrder.setFlowordertype(rs.getInt("flowordertype"));
			return cwbOrder;
		}
	}

	private final class EditIdMapper implements RowMapper<ExcelImportEdit> {

		@Override
		public ExcelImportEdit mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExcelImportEdit cwbOrder = new ExcelImportEdit();
			cwbOrder.setId(rs.getLong("id"));
			cwbOrder.setExcelbranchid(rs.getLong("excelbranchid"));
			return cwbOrder;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ExcelImportEdit getEditInfoByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_ops_importedit where cwb=?  limit 0,1", new EditMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}

	public void insertEditInfo(final CwbOrder cwbOrder) {
		logger.info("导入Edit表一条新的订单，订单号为{}配送站点{}", cwbOrder.getCwb(), cwbOrder.getDeliverybranchid());
		jdbcTemplate.update("insert into express_ops_importedit (cwb,transcwb,emaildate,addresscodeedittype,cwbordertypeid,flowordertype,excelbranchid,customerid,emaildateid) "
				+ "values(?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, cwbOrder.getCwb());
				ps.setString(2, cwbOrder.getTranscwb());
				ps.setString(3, cwbOrder.getEmaildate());
				ps.setInt(4, cwbOrder.getAddresscodeedittype());
				ps.setLong(5, cwbOrder.getCwbordertypeid());
				ps.setInt(6, FlowOrderTypeEnum.DaoRuShuJu.getValue());
				ps.setLong(7, cwbOrder.getDeliverybranchid());
				ps.setLong(8, cwbOrder.getCustomerid());
				ps.setLong(9, cwbOrder.getEmaildateid());
			}

		});

	}

	public void updateEditInfo(final CwbOrder cwbOrder) {
		logger.info("更新Edit表一条新的订单，订单号为{}配送站点{}", cwbOrder.getCwb(), cwbOrder.getDeliverybranchid());
		jdbcTemplate.update("update  express_ops_importedit set transcwb=?,emaildate=?,addresscodeedittype=?,cwbordertypeid=?,flowordertype=?,excelbranchid=?,customerid=?,emaildateid=? where cwb=?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {

						ps.setString(1, cwbOrder.getTranscwb());
						ps.setString(2, cwbOrder.getEmaildate());
						ps.setInt(3, cwbOrder.getAddresscodeedittype());
						ps.setLong(4, cwbOrder.getCwbordertypeid());
						ps.setLong(5, cwbOrder.getFlowordertype());
						ps.setLong(6, cwbOrder.getDeliverybranchid());
						ps.setLong(7, cwbOrder.getCustomerid());
						ps.setLong(8, cwbOrder.getEmaildateid());
						ps.setString(9, cwbOrder.getCwb());
					}

				});

	}

	public List<ExcelImportEdit> getEditInfoListIsNotAddress(long page, long onePageNumber, String ordercwb, String addressCodeEditType) {
		String sql = "select * from express_ops_importedit where 1=1";
		if (ordercwb.trim().length() > 0) {
			sql += " and cwb='" + ordercwb + "'";
		} else if (addressCodeEditType.length() > 0) {
			sql += " and addresscodeedittype in(" + addressCodeEditType + ")";
		}
		sql += " order by excelbranchid desc ";
		if (page > 0) {
			sql += " limit " + (page - 1) * onePageNumber + " ," + onePageNumber;
		}

		logger.info("sql:" + sql);
		List<ExcelImportEdit> cwborderList = jdbcTemplate.query(sql, new EditMapper());
		return cwborderList;
	}

	public String getEditInfoByBranch(String type, long branchid) {

		String sql = "select * from express_ops_importedit where 1=1";
		if (branchid > 0) {
			sql += " and excelbranchid='" + branchid + "'";
		}
		if (type.length() > 0) {
			sql += " and addresscodeedittype in(" + type + ")";
		}
		List<ExcelImportEdit> cwborderList = jdbcTemplate.query(sql, new EditMapper());
		StringBuffer sb = new StringBuffer();
		for (ExcelImportEdit e : cwborderList) {
			sb.append("'");
			sb.append(e.getCwb());
			sb.append("',");
		}
		// 如果没有数据，则返回--保证查询结果中不会跳过这一条件
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1).toString() : "'--'";
	}

	public void deleteEdit(String cwb) {
		String sql2 = "delete from  express_ops_importedit  where cwb=? ";
		logger.info("失效edit表" + sql2 + cwb);
		jdbcTemplate.update(sql2, cwb);
	}

	public void deleteByEmaildate(long emaildateid) {
		String sql2 = "delete from  express_ops_importedit  where emaildateid=? ";
		logger.info("失效edit表" + sql2 + emaildateid);
		jdbcTemplate.update(sql2, emaildateid);
	}

	public List<ExcelImportEdit> getEditInfoCountByBranch(String ordercwb, String type) {
		String sql = "select excelbranchid,COUNT(1) AS ID  from express_ops_importedit where 1=1 ";
		StringBuffer w = new StringBuffer();
		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb='" + ordercwb + "'");
		} else if (type.length() > 0) {
			sql += " and addresscodeedittype in(" + type + ")";
		}
		sql += " GROUP BY  excelbranchid";
		logger.info("sql:" + sql);
		List<ExcelImportEdit> list = jdbcTemplate.query(sql, new EditIdMapper());
		return list;
	}
}
