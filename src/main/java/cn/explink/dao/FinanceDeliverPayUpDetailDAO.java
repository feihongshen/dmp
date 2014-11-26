package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.FinanceDeliverPayupDetail;
import cn.explink.util.Page;

@Component
public class FinanceDeliverPayUpDetailDAO {

	private Logger logger = LoggerFactory.getLogger(FinanceDeliverPayUpDetailDAO.class);

	private final class FinanceDeliverPayUpDetailRowMapper implements RowMapper<FinanceDeliverPayupDetail> {
		@Override
		public FinanceDeliverPayupDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			FinanceDeliverPayupDetail fdpud = new FinanceDeliverPayupDetail();
			fdpud.setId(rs.getLong("id"));
			fdpud.setDeliverealuser(rs.getLong("deliverealuser"));
			fdpud.setPayupamount(rs.getBigDecimal("payupamount"));
			fdpud.setDeliverpayupamount(rs.getBigDecimal("deliverpayupamount"));
			fdpud.setDeliverAccount(rs.getBigDecimal("deliverAccount"));
			fdpud.setDeliverpayuparrearage(rs.getBigDecimal("deliverpayuparrearage"));
			fdpud.setPayupamount_pos(rs.getBigDecimal("payupamount_pos"));
			fdpud.setDeliverpayupamount_pos(rs.getBigDecimal("deliverpayupamount_pos"));
			fdpud.setDeliverPosAccount(rs.getBigDecimal("deliverPosAccount"));
			fdpud.setDeliverpayuparrearage_pos(rs.getBigDecimal("deliverpayuparrearage_pos"));
			fdpud.setGcaid(rs.getLong("gcaid"));
			fdpud.setAudituserid(rs.getLong("audituserid"));
			fdpud.setCredate(rs.getString("credate"));
			fdpud.setType(rs.getInt("type"));
			fdpud.setRemark(rs.getString("remark"));
			return fdpud;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long insert(final FinanceDeliverPayupDetail fdpud) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into `finance_deliver_pay_up_detail`" + "(`deliverealuser`,`payupamount`,`deliverpayupamount`,`deliverAccount`,`deliverpayuparrearage`"
						+ ",`payupamount_pos`,`deliverpayupamount_pos`,`deliverPosAccount`,`deliverpayuparrearage_pos`,`gcaid`" + ",`audituserid`,`credate`,`type`,`remark`)"
						+ "values (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?) ", new String[] { "id" });
				ps.setLong(1, fdpud.getDeliverealuser());
				ps.setBigDecimal(2, fdpud.getPayupamount());
				ps.setBigDecimal(3, fdpud.getDeliverpayupamount());
				ps.setBigDecimal(4, fdpud.getDeliverAccount());
				ps.setBigDecimal(5, fdpud.getDeliverpayuparrearage());

				ps.setBigDecimal(6, fdpud.getPayupamount_pos());
				ps.setBigDecimal(7, fdpud.getDeliverpayupamount_pos());
				ps.setBigDecimal(8, fdpud.getDeliverPosAccount());
				ps.setBigDecimal(9, fdpud.getDeliverpayuparrearage_pos());
				ps.setLong(10, fdpud.getGcaid());

				ps.setLong(11, fdpud.getAudituserid());
				ps.setString(12, fdpud.getCredate());
				ps.setInt(13, fdpud.getType());
				ps.setString(14, fdpud.getRemark());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateDeliverpayupamount(BigDecimal deliverpayupamount, BigDecimal deliverpayuparrearage, Long gcaid) {

		jdbcTemplate.update("update finance_deliver_pay_up_detail set deliverpayupamount=? , deliverpayuparrearage=? where gcaid=?", deliverpayupamount, deliverpayuparrearage, gcaid);
	}

	/**
	 * 获取小件员帐户变动详情列表
	 * 
	 * @param page
	 * @param deliverid
	 * @param startTime
	 * @param endTime
	 * @param type
	 */
	public List<FinanceDeliverPayupDetail> getDetailListByDeliveridAndType(long page, long deliverid, String startTime, String endTime, int type) {
		String sql = "select * from finance_deliver_pay_up_detail where deliverealuser=?";
		sql += getDetailListByDeliveridAndType(startTime, endTime, type);
		sql += " order by id desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new FinanceDeliverPayUpDetailRowMapper(), deliverid);
	}

	/**
	 * 获取小件员帐户变动详情列表
	 * 
	 * @param deliverid
	 * @param startTime
	 * @param endTime
	 * @param type
	 */
	public Long getDetailListByDeliveridAndTypeCount(long deliverid, String startTime, String endTime, int type) {
		String sql = "select count(1) from finance_deliver_pay_up_detail where deliverealuser=?";
		sql += getDetailListByDeliveridAndType(startTime, endTime, type);
		return jdbcTemplate.queryForLong(sql, deliverid);
	}

	private String getDetailListByDeliveridAndType(String startTime, String endTime, int type) {
		StringBuffer sql = new StringBuffer();
		if (startTime.trim().length() > 0) {
			sql = sql.append(" AND credate>='").append(startTime).append("'");
		}
		if (endTime.trim().length() > 0) {
			sql = sql.append(" AND credate<='").append(endTime).append("'");
		}
		if (type > -1) {
			sql = sql.append(" AND type=").append(type);
		}
		return sql.toString();
	}
}
