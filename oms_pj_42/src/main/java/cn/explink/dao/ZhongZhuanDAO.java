package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ZhongZhuan;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ZhongZhuanDAO {
	private final class ZhongZhuanMapper implements RowMapper<ZhongZhuan> {
		@Override
		public ZhongZhuan mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZhongZhuan zhongZhuan = new ZhongZhuan();
			zhongZhuan.setId(rs.getLong("id"));
			zhongZhuan.setCwb(rs.getString("cwb"));
			zhongZhuan.setZhongzhuanoutstoreroomtime(StringUtil.nullConvertToEmptyString(rs.getString("zhongzhuanoutstoreroomtime")));
			zhongZhuan.setCustomerid(rs.getLong("customerid"));
			zhongZhuan.setNextbranchid(rs.getLong("nextbranchid"));
			zhongZhuan.setStartbranchid(rs.getLong("startbranchid"));
			zhongZhuan.setReceivablefee(rs.getBigDecimal("receivablefee"));
			zhongZhuan.setPaybackfee(rs.getBigDecimal("paybackfee"));
			zhongZhuan.setInsitebranchid(rs.getLong("insitebranchid"));
			zhongZhuan.setInSitetime(StringUtil.nullConvertToEmptyString(rs.getString("inSitetime")));
			zhongZhuan.setCwbordertypeid(rs.getLong("cwbordertypeid"));

			return zhongZhuan;
		}
	}

	private final class ZhongZhuanSumMapper implements RowMapper<ZhongZhuan> {
		@Override
		public ZhongZhuan mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZhongZhuan zhongZhuan = new ZhongZhuan();
			zhongZhuan.setId(rs.getLong("id"));
			zhongZhuan.setReceivablefee(rs.getBigDecimal("receivablefee"));
			zhongZhuan.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return zhongZhuan;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creZhongZhuan(final ZhongZhuan zhongZhuan) {
		jdbcTemplate.update("INSERT INTO `ops_zhongzhuan`(`cwb`,`zhongzhuanoutstoreroomtime`,"
				+ "`customerid`,`nextbranchid`,`startbranchid`,`receivablefee`,`paybackfee`,`insitebranchid`,`inSitetime`,`cwbordertypeid`) " + " VALUES ( ?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, zhongZhuan.getCwb());
						ps.setString(2, zhongZhuan.getZhongzhuanoutstoreroomtime());
						ps.setLong(3, zhongZhuan.getCustomerid());
						ps.setLong(4, zhongZhuan.getNextbranchid());
						ps.setLong(5, zhongZhuan.getStartbranchid());
						ps.setBigDecimal(6, zhongZhuan.getReceivablefee());
						ps.setBigDecimal(7, zhongZhuan.getPaybackfee());
						ps.setLong(8, zhongZhuan.getInsitebranchid());
						ps.setString(9, zhongZhuan.getInSitetime());
						ps.setLong(10, zhongZhuan.getCwbordertypeid());
					}
				});
	}

	public void saveZhongZhuanByCwb(final ZhongZhuan zhongZhuan) {
		jdbcTemplate.update("UPDATE `ops_zhongzhuan` SET " + "`zhongzhuanoutstoreroomtime`=?,`customerid`=?,`nextbranchid`=?,`startbranchid`=?,"
				+ "`receivablefee`=?,`paybackfee`=?,`insitebranchid`=?,`inSitetime`=?,`cwbordertypeid`=? WHERE `id`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, zhongZhuan.getZhongzhuanoutstoreroomtime());
				ps.setLong(2, zhongZhuan.getCustomerid());
				ps.setLong(3, zhongZhuan.getNextbranchid());
				ps.setLong(4, zhongZhuan.getStartbranchid());
				ps.setBigDecimal(5, zhongZhuan.getReceivablefee());
				ps.setBigDecimal(6, zhongZhuan.getPaybackfee());
				ps.setLong(7, zhongZhuan.getInsitebranchid());
				ps.setString(8, zhongZhuan.getInSitetime());
				ps.setLong(9, zhongZhuan.getCwbordertypeid());
				ps.setLong(10, zhongZhuan.getId());
			}
		});
	}

	public void saveZhongZhuanInsiteByCwb(long insitebranchid, String inSitetime, String cwb) {
		String sql = "update ops_zhongzhuan set insitebranchid=?,inSitetime=? where cwb=?";
		jdbcTemplate.update(sql, insitebranchid, inSitetime, cwb);
	}

	public List<ZhongZhuan> getZhongZhuanList(String begindate, String enddate, String nextbranchids, String startbranchids, long page) {
		String sql = "SELECT * FROM ops_zhongzhuan FORCE INDEX(ZZ_Zhongzhuanoutstoreroomtime_Idx) where zhongzhuanoutstoreroomtime>='" + begindate + "'  and zhongzhuanoutstoreroomtime<='" + enddate
				+ "' ";
		sql = setWhereSql(sql, nextbranchids, startbranchids);
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new ZhongZhuanMapper());
	}

	public ZhongZhuan getZhongZhuanSum(String begindate, String enddate, String nextbranchids, String startbranchids) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_zhongzhuan  FORCE INDEX(ZZ_Zhongzhuanoutstoreroomtime_Idx) where zhongzhuanoutstoreroomtime>='"
				+ begindate + "'  and zhongzhuanoutstoreroomtime<='" + enddate + "' ";
		sql = setWhereSql(sql, nextbranchids, startbranchids);
		return jdbcTemplate.queryForObject(sql, new ZhongZhuanSumMapper());
	}

	public List<ZhongZhuan> checkZhongZhuanAndNextbranchid(String cwb, long nextbranchid) {
		String sql = "SELECT * FROM ops_zhongzhuan  WHERE cwb=? and  nextbranchid=? limit 0,1";
		return jdbcTemplate.query(sql, new ZhongZhuanMapper(), cwb, nextbranchid);
	}

	public List<ZhongZhuan> checkZhongZhuanAndStartbranchid(String cwb, long startbranchid) {
		String sql = "SELECT * FROM ops_zhongzhuan  WHERE cwb=? and startbranchid=?  limit 0,1";
		return jdbcTemplate.query(sql, new ZhongZhuanMapper(), cwb, startbranchid);
	}

	public void deleteDeliveryDaohuo(String cwb) {
		String sql = "DELETE FROM ops_zhongzhuan WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 
	 * @param sql
	 * @param customerid
	 * @param nextbranchids
	 * @param startbranchids
	 * @return
	 */
	public String setWhereSql(String sql, String nextbranchids, String startbranchids) {
		if (nextbranchids.length() > 0) {
			sql += " and nextbranchid in(" + nextbranchids + ") ";
		}
		if (startbranchids.length() > 0) {
			sql += " and startbranchid in(" + startbranchids + ") ";
		}
		return sql;
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_zhongzhuan set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_zhongzhuan set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}
