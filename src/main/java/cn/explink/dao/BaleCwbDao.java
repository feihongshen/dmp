package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import cn.explink.enumutil.BaleStateEnum;

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
		String sql = "select count(1) from express_ops_bale_cwb a where baleno=? and cwb=? "
				+" and exists(select 1 from express_ops_bale b where b.id=a.baleid and b.balestate in (?,?,?))";
		return this.jdbcTemplate.queryForLong(sql, baleno, cwb, BaleStateEnum.WeiFengBao.getValue(),BaleStateEnum.YiFengBao.getValue(),
				BaleStateEnum.YiFengBaoChuKu.getValue());
	}
	/**
	 * 根据包号获取当前包扫描所有件数
	 * @param baleno
	 * @return
	 */
	public long getBaleScanCount(long baleid) {
		String sql = "select scannum from express_ops_bale where baleid=?";
		return this.jdbcTemplate.queryForLong(sql, baleid);
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
		String sql = " SELECT baleno FROM express_ops_bale_cwb WHERE cwb IN ( " + queryStr + " ) "
				+"and exists(select 1 from express_ops_bale WHERE balestate in (?,?,?))";
		return this.jdbcTemplate.queryForList(sql, String.class,BaleStateEnum.YiFengBao.getValue(),
				BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue());
	}
	
	public List<String> getCwbsByBale(String baleid) {
		String sql = "select cwb from express_ops_bale_cwb where baleid=";
		return this.jdbcTemplate.queryForList(sql+baleid, String.class);
	}
	
	@Deprecated
	public List<String> getCwbsByBaleNO(String baleno) {
		String sql = "select cwb from express_ops_bale_cwb where baleno=?";
		return this.jdbcTemplate.queryForList(sql, String.class,baleno);
	}

	public void deleteByBaleidAndCwb(long baleid, String cwb) {
		this.jdbcTemplate.update("delete from express_ops_bale_cwb where baleid = ? and cwb=?", baleid, cwb);
	}
	public void deleteByCwbs(String cwbs) {
		this.jdbcTemplate.update("delete from express_ops_bale_cwb where cwb in("+cwbs+") ");
	}
	public List<BaleCwb> getBaleCwbByCwb(String cwb) {
		String sql = "select * from express_ops_bale_cwb a where cwb=? "
				+" and exists(select 1 from express_ops_bale b where b.id=a.baleid and b.balestate in (?,?,?))";
		try{
		return this.jdbcTemplate.query(sql, new BaleMapper(),cwb,BaleStateEnum.YiFengBao.getValue(),
				BaleStateEnum.YiFengBaoChuKu.getValue(),BaleStateEnum.WeiFengBao.getValue());
		}catch(Exception e){
			return null;
		}
	}
	
	public String getScancwbByCwbs(long baleid,String cwbs) {
		String sql ="select cwb from express_ops_bale_cwb where baleid=? and cwb in("+cwbs+") limit 1";
		List<String> list=this.jdbcTemplate.queryForList(sql,new Long[]{baleid},String.class);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Object getCwbCountByBaleId(long baleid){
		String sql = "SELECT COUNT(1) FROM express_ops_bale_cwb where baleid=?";
		return this.jdbcTemplate.queryForObject(sql,Object.class,baleid);
	}
	
		/**
	 * 查询订单对应的最后使用的包号
	 * @date 2016年7月13日 下午5:30:18
	 * @param cwbList
	 * @return
	 */
	public List<BaleCwb> getLastBaleCwbList(List<String> cwbList) {
		StringBuilder cwbsSb = new StringBuilder();
		for(int i = 0; i < cwbList.size(); i++) {
			cwbsSb.append("'");
			cwbsSb.append(cwbList.get(i));
			cwbsSb.append("'");
			if(i != cwbList.size() - 1) {
				cwbsSb.append(",");
			}
		}
		String cwbs = cwbsSb.toString();
		String sql = "SELECT bc.* FROM express_ops_bale_cwb bc"
				+ " WHERE bc.cwb IN  (" + cwbs + ")"
				+ " AND bc.baleid = (SELECT max(bc2.baleid) FROM express_ops_bale_cwb bc2 WHERE bc2.cwb = bc.cwb)"
				+ " GROUP BY bc.baleid, bc.cwb"
				+ " ORDER BY bc.baleid ASC, bc.cwb ASC";
		return this.jdbcTemplate.query(sql, new BaleMapper());
	}
	
	/**
	 * 根据包ID查询
	 * @date 2016年7月13日 下午5:30:50
	 * @param baleid
	 * @return
	 */
	public List<BaleCwb> getBaleCwbListByBaleId(long baleid) {
		String sql = "select * from express_ops_bale_cwb where baleid = ?";
		List<BaleCwb> list = this.jdbcTemplate.query(sql, new BaleMapper(), baleid);
		if(list == null) {
			list = new ArrayList<BaleCwb>();
		}
		return list;
	}
	
	
	
}
