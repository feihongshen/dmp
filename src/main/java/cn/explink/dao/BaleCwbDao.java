package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import cn.explink.core.utils.StringUtils;
import cn.explink.domain.BaleCwb;
import cn.explink.enumutil.BaleUseStateEnum;

@Component
public class BaleCwbDao {
	private final class BaleMapper implements RowMapper<BaleCwb> {
		@Override
		public BaleCwb mapRow(ResultSet rs, int rowNum) throws SQLException {
			BaleCwb baleCwb = new BaleCwb();
			baleCwb.setId(rs.getLong("id"));
			baleCwb.setBaleid(rs.getLong("baleid"));
			baleCwb.setBaleno(rs.getString("baleno"));
			baleCwb.setCwb(rs.getString("cwb"));
			return baleCwb;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public long createBale(final BaleCwb bale) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale_cwb(baleid,baleno,cwb) values(?,?,?)", new String[] { "id" });
				ps.setLong(1, bale.getBaleid());
				ps.setString(2, bale.getBaleno());
				ps.setString(3, bale.getCwb());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void createBale(long baleid, String baleno, String cwb) {
		if (this.getBaleCount(baleid, cwb) == 0) {
			BaleCwb bale = new BaleCwb();
			bale.setBaleid(baleid);
			bale.setBaleno(baleno);
			bale.setCwb(cwb);
			this.createBale(bale);
		}

	}

	public long getBaleCount(long baleid, String cwb) {
		String sql = "select count(1) from express_ops_bale_cwb where baleid=? and cwb=?";
		return this.jdbcTemplate.queryForLong(sql, baleid, cwb);
	}
	public long getBaleAndCwbCount(String baleno, String cwb) {
		String sql = "select count(1) from express_ops_bale_cwb where baleno=? and cwb=?";
		return this.jdbcTemplate.queryForLong(sql, baleno, cwb);
	}
	/**
	 * 根据包号获取当前包扫描所有件数
	 * @param baleno
	 * @return
	 */
	public long getBaleScanCount(String baleno) {
		String sql = "select scannum from express_ops_bale where baleno=? and state=?";
		return this.jdbcTemplate.queryForLong(sql, baleno,BaleUseStateEnum.ZaiShiYong.getValue());
	}
	/**
	 * 根据订单号获取对应包号
	 * @param orderNo
	 * @return
	 */
	public List<String> getBaleNoList(String orderNo){
		StringBuilder queryCondition = new StringBuilder();
		queryCondition.append("'").append(orderNo).append("',");
		
		String transcwbSql = " SELECT transcwb FROM express_ops_cwb_detail WHERE cwb =?";
		List<String> transcwbStrList = this.jdbcTemplate.queryForList(transcwbSql, String.class, orderNo);
		String transcwbStr = "";
		if( null != transcwbStrList && transcwbStrList.size() > 0){
			transcwbStr = transcwbStrList.get(0);
		}
		if( !StringUtils.isEmpty(transcwbStr)){
			for (String tempTranscwb : transcwbStr.split(",")) {
				queryCondition.append("'").append(tempTranscwb).append("',");
			}
		}
		
		String queryStr = queryCondition.substring(0,queryCondition.length()-1);
		String sql = " SELECT baleno FROM express_ops_bale_cwb WHERE cwb IN ( " + queryStr + " ) ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}
	
	public List<String> getCwbsByBale(String baleid) {
		String sql = "select cwb from express_ops_bale_cwb where baleid=";
		return this.jdbcTemplate.queryForList(sql+baleid, String.class);
	}
	public List<String> getCwbsByBaleNO(String baleno) {
		String sql = "select cwb from express_ops_bale_cwb where baleid in (select id from express_ops_bale where baleno=? and state=?)";
		return this.jdbcTemplate.queryForList(sql, String.class,baleno,BaleUseStateEnum.ZaiShiYong.getValue());
		
	}

	public void deleteByBaleidAndCwb(long baleid, String cwb) {
		this.jdbcTemplate.update("delete from express_ops_bale_cwb where baleid = ? and cwb=?", baleid, cwb);
	}
	public void deleteByCwbs(String cwbs) {
		this.jdbcTemplate.update("delete from express_ops_bale_cwb where cwb in("+cwbs+") ");
	}
	public List<BaleCwb> getBaleCwbByCwb(String cwb) {
		String sql = "select * from express_ops_bale_cwb where cwb=?";
		try{
		return this.jdbcTemplate.query(sql, new BaleMapper(),cwb);
		}catch(Exception e){
			return null;
		}
	}

}
