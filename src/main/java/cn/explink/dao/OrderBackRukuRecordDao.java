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

import cn.explink.domain.OrderBackRuku;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class OrderBackRukuRecordDao {

	private final class OrderBackRukuRowMapper implements RowMapper<OrderBackRuku> {
		@Override
		public OrderBackRuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderBackRuku backRuku = new OrderBackRuku();
			backRuku.setCwb(rs.getString("cwb"));
			backRuku.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			backRuku.setCustomerid(rs.getLong("customerid"));
			backRuku.setBranchid(rs.getLong("branchid"));
			backRuku.setCreatetime(rs.getString("createtime"));
			backRuku.setConsigneename(rs.getString("consigneename"));
			backRuku.setConsigneeaddress(rs.getString("consigneeaddress"));
			backRuku.setAuditstate(rs.getInt("auditstate"));
			backRuku.setCwbstate(rs.getInt("cwbstate"));
			backRuku.setRemarkstr(StringUtil.nullConvertToEmptyString(rs.getString("remarkstr")));
			backRuku.setAuditname(StringUtil.nullConvertToEmptyString(rs.getString("auditname")));
			backRuku.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));
			return backRuku;
		}
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creOrderbackRuku(final OrderBackRuku obr) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_orderbackruku_record (cwb,customerid,cwbordertypeid,"
						+ "branchid,consigneename,consigneeaddress,createtime,cwbstate) " + "values(?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, obr.getCwb());
				ps.setLong(2, obr.getCustomerid());
				ps.setInt(3, obr.getCwbordertypeid());
				ps.setLong(4, obr.getBranchid());
				ps.setString(5, obr.getConsigneename());
				ps.setString(6, obr.getConsigneeaddress());
				ps.setString(7, obr.getCreatetime());
				ps.setInt(8, obr.getCwbstate());//订单状态
				return ps;
			}
		}, key);
		//obr.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}
	
	
	public List<OrderBackRuku> getOrderbackRukus(long page,String cwb,int cwbordertype,long customerid,long branchid,String begintime,String endtime,int auditstate){
		String sql = "";
		if(auditstate==0){
//			sql = "select re.* from express_orderbackruku_record as re left join express_ops_cwb_detail as de on re.cwb = de.cwb"
//					+" where de.state=1 and re.auditstate =0 and de.flowordertype=15";
			//modify by Alice.yu 唯品会上门换业务员不允许 审核为退货再投，故此处加上过滤条件de.exchange_flag!=1
			sql = "select re.* from express_orderbackruku_record as re left join express_ops_cwb_detail as de on re.cwb = de.cwb"
					+" where de.state=1 and re.auditstate =0 and de.flowordertype=15 and de.exchange_flag!=1 ";
			StringBuffer sb = new StringBuffer();
			if(!"".equals(cwb)){
				sb.append(" and re.cwb in("+cwb+")");
			}
			if(cwbordertype>0){
				sb.append(" and re.cwbordertypeid="+cwbordertype);
			}else{ //如果没有选择订单类型，那么就排除掉上门退订单
				sb.append(" and re.cwbordertypeid !="+CwbOrderTypeIdEnum.Shangmentui.getValue());
			}
			if(customerid>0){
				sb.append(" and re.customerid="+customerid);
			}
			if(branchid>0){
				sb.append(" and re.branchid="+branchid);
			}
			if(!"".equals(begintime)){
				sb.append(" and re.createtime>= '"+begintime+"' ");
			}
			if(!"".equals(endtime)){
				sb.append(" and re.createtime<= '"+endtime+"' ");
			}
			sql += sb;
			sql += " order by re.createtime desc";
		}else{
			sql = "select * from express_orderbackruku_record where auditstate=1";
			StringBuffer sb = new StringBuffer();
			if(!"".equals(cwb)){
				sb.append(" and cwb in("+cwb+")");
			}
			if(cwbordertype>0){
				sb.append(" and cwbordertypeid="+cwbordertype);
			}
			if(customerid>0){
				sb.append(" and customerid="+customerid);
			}
			if(branchid>0){
				sb.append(" and branchid="+branchid);
			}
			if(!"".equals(begintime)){
				sb.append(" and createtime>= '"+begintime+"' ");
			}
			if(!"".equals(endtime)){
				sb.append(" and createtime<= '"+endtime+"' ");
			}
			sql += sb;
			sql += " order by createtime desc";
		}

			if(page != -1){
				sql += "  limit "+ (page-1)*Page.ONE_PAGE_NUMBER+","+Page.ONE_PAGE_NUMBER;
			}
		return jdbcTemplate.query(sql, new OrderBackRukuRowMapper());
	}
	
	public long getOrderbackRukusCount(long page,String cwb,int cwbordertype,long customerid,long branchid,String begintime,String endtime,int auditstate){
		String sql = "";
		if(auditstate==0){
			sql = "select count(1) from express_orderbackruku_record as re left join express_ops_cwb_detail as de on re.cwb = de.cwb"
					+" where de.state=1 and re.auditstate =0 and de.flowordertype =15";
			StringBuffer sb = new StringBuffer();
			if(!"".equals(cwb)){
				sb.append(" and re.cwb in("+cwb+")");
			}
			if(cwbordertype>0){
				sb.append(" and re.cwbordertypeid="+cwbordertype);
			}
			if(customerid>0){
				sb.append(" and re.customerid="+customerid);
			}
			if(branchid>0){
				sb.append(" and re.branchid="+branchid);
			}
			if(!"".equals(begintime)){
				sb.append(" and re.createtime>= '"+begintime+"' ");
			}
			if(!"".equals(endtime)){
				sb.append(" and re.createtime<= '"+endtime+"' ");
			}
			sql += sb;
			
		}else{
			sql = "select count(1) from express_orderbackruku_record where auditstate=1";
			StringBuffer sb = new StringBuffer();
			if(!"".equals(cwb)){
				sb.append(" and cwb in("+cwb+")");
			}
			if(cwbordertype>0){
				sb.append(" and cwbordertypeid="+cwbordertype);
			}
			if(customerid>0){
				sb.append(" and customerid="+customerid);
			}
			if(branchid>0){
				sb.append(" and branchid="+branchid);
			}
			if(!"".equals(begintime)){
				sb.append(" and createtime>= '"+begintime+"' ");
			}
			if(!"".equals(endtime)){
				sb.append(" and createtime<= '"+endtime+"' ");
			}
			sql += sb;
		}
		
		return jdbcTemplate.queryForLong(sql);
	}


	public void updateOrderBackRukuRecort(String cwb, String reasoncontent,
			String realname, String createtime) {
		String sql = "update express_orderbackruku_record set auditstate=1,remarkstr=?,auditname=?,audittime=? where cwb=? order by id desc limit 1";
		this.jdbcTemplate.update(sql,reasoncontent,realname,createtime,cwb);
	}


	public OrderBackRuku getBackrukuRecord(String cwb) {
		try{
			String sql = "select * from express_orderbackruku_record where cwb=? and auditstate=0";
			return this.jdbcTemplate.queryForObject(sql, new OrderBackRukuRowMapper(),cwb);
		}catch(Exception e){
			return null;
		}
		
	}
	
	
}
