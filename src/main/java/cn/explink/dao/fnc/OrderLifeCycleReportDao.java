package cn.explink.dao.fnc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.domain.OrderLifeCycleReportVO;
import cn.explink.enumutil.CwbOrderLifeCycleTypeIdEnum;

/**
 * 
 * OrderLifeCycleReportDao
 * 
 * @author jinghui.pan
 *
 */
@Repository
public class OrderLifeCycleReportDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Logger logger = LoggerFactory.getLogger(OrderLifeCycleReportDao.class);
	
	public long getReportIdByCustomerAndTypeid(long customerid, int typeid) {
		
		List<Long> list = this.jdbcTemplate.query("select id from fn_rpt_order_lifecycle where customerid = ? and typeid = ? and state = 1 order by reportdate desc",new OrderLifeCycleReportIdRowMapper(),customerid, typeid);
		long reportid = 0;
		if(list!= null && !list.isEmpty()){
			reportid = list.get(0);
		}
		
		return reportid;
	}
	
	public long getReportIdByCustomerAndTypeidAndReportdate(long customerid, int typeid, int reportdate) {
		
		List<Long> list = this.jdbcTemplate.query("select id from fn_rpt_order_lifecycle where customerid = ? and typeid = ? and reportdate = ? and state = 1",new OrderLifeCycleReportIdRowMapper(),customerid, typeid, reportdate);
		long reportid = 0;
		if(list!= null && !list.isEmpty()){
			reportid = list.get(0);
		}
		
		return reportid;
	}
	
	/**
	 * Get the order life cycle list.
	 * 
	 * @param selectedCustomers
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public List<OrderLifeCycleReportVO> getListByCustomers(String selectedCustomers, int reportdate) {
		
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select customerid as customer_id ,customername as customer_name")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.Import.getValue()).append(" then `count` else 0 end) as count1")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.Import.getValue()).append(" then `amount` else 0 end) as amount1")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TiHuo.getValue()).append(" then `count` else 0 end) as count2")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TiHuo.getValue()).append(" then `amount` else 0 end) as amount2") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.FengBoRuku.getValue()).append(" then `count` else 0 end) as count3")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.FengBoRuku.getValue()).append(" then `amount` else 0 end) as amount3") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.FengBoChuku.getValue()).append(" then `count` else 0 end) as count4")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.FengBoChuku.getValue()).append(" then `amount` else 0 end) as amount4") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhanDianZaiZhan.getValue()).append(" then `count` else 0 end) as count5")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhanDianZaiZhan.getValue()).append(" then `amount` else 0 end) as amount5") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhanDianWeiFanKuan.getValue()).append(" then `count` else 0 end) as count6")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhanDianWeiFanKuan.getValue()).append(" then `amount` else 0 end) as amount6") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoChuZhanZaiTu.getValue()).append(" then `count` else 0 end) as count7")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoChuZhanZaiTu.getValue()).append(" then `amount` else 0 end) as amount7") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanChuZhanZaiTu.getValue()).append(" then `count` else 0 end) as count8")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanChuZhanZaiTu.getValue()).append(" then `amount` else 0 end) as amount8") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuRuKu.getValue()).append(" then `count` else 0 end) as count9")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuRuKu.getValue()).append(" then `amount` else 0 end) as amount9") 
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuChuKu.getValue()).append(" then `count` else 0 end) as count10")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuChuKu.getValue()).append(" then `amount` else 0 end) as amount10")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoKuRuKu.getValue()).append(" then `count` else 0 end) as count11")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoKuRuKu.getValue()).append(" then `amount` else 0 end) as amount11")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoZaiTou.getValue()).append(" then `count` else 0 end) as count12")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiHuoZaiTou.getValue()).append(" then `amount` else 0 end) as amount12")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiKeHuZaiTu.getValue()).append(" then `count` else 0 end) as count13")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiKeHuZaiTu.getValue()).append(" then `amount` else 0 end) as amount13")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiKeHuWeiShouKuan.getValue()).append(" then `count` else 0 end) as count14")
		.append(",sum(case when typeid = " ).append( CwbOrderLifeCycleTypeIdEnum.TuiKeHuWeiShouKuan.getValue()).append(" then `amount` else 0 end) as amount14")
//		.append(" from fn_rpt_order_lifecycle lc left join express_set_customer_info c on lc.customerid = c.customerid")
		.append(" from (")
		.append(" select c.customerid, customername ,  CASE WHEN `amount` is not null THEN `amount` ELSE 0 END amount, CASE WHEN `count` is not null THEN `count` ELSE 0 END `count`, typeid")
		.append(" FROM express_set_customer_info c LEFT JOIN fn_rpt_order_lifecycle lc ON lc.customerid = c.customerid and state = 1 and reportdate = ?")
		.append(" where 1 = 1");
		if(StringUtils.isNotBlank(selectedCustomers)){
			sqlBuilder.append(" and c.customerid in (").append(selectedCustomers).append(")");
		}
		sqlBuilder.append(" ) t") 
		.append(" where 1 = 1") 
		.append(" group by customerid");
		
		List<OrderLifeCycleReportVO> result = jdbcTemplate.query(sqlBuilder.toString(), new OrderLifeCycleReportSummaryRowMapper(reportdate), reportdate);
		
		return result;
	}
	
	/**
	 * 更形state从1变成0
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public void updateStateToZero() {
		this.jdbcTemplate
				.update("update fn_rpt_order_lifecycle set state = 0 where state = 1");

	}
	
	/**
	 * 更形state从1变成0
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public void updateStateToZeroByReportDate(int reportDate) {
		this.jdbcTemplate
				.update("update fn_rpt_order_lifecycle set state = 0 where state = 1 and reportdate = ?", reportDate);

	}

	public void insertOrderDetailSnapshot(final OrderLifeCycleReportVO lifeCycleReportVO){
			final String insertsql = "insert into fn_rpt_order_lifecycle"
					+ "( customerid,  `count`,  amount,  typeid, reportdate)"
					+ " values(?,?,?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(insertsql, new String[] { "id" });
					ps.setLong(1, lifeCycleReportVO.getCustomerid());
					ps.setInt(2, lifeCycleReportVO.getCount());
					ps.setBigDecimal(3, lifeCycleReportVO.getAmount());
					ps.setInt(4,  lifeCycleReportVO.getTypeid());
					ps.setInt(5, lifeCycleReportVO.getReportdate());
					return ps;
				}
			}, keyHolder);
			
			lifeCycleReportVO.setId(keyHolder.getKey().longValue());
	}
	
	private final class OrderLifeCycleReportIdRowMapper implements RowMapper<Long>{
		@Override
		public Long mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			return rs.getLong("id");
		}
	}
	
	private final class OrderLifeCycleReportSummaryRowMapper implements RowMapper<OrderLifeCycleReportVO> {
		
		private int reportdate;
		
		public OrderLifeCycleReportSummaryRowMapper() {
			super();
		}
		public OrderLifeCycleReportSummaryRowMapper(int reportdate) {
			super();
			this.reportdate = reportdate;
		}


		@Override
		public OrderLifeCycleReportVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderLifeCycleReportVO reportVo = new OrderLifeCycleReportVO();
			reportVo.setCustomerid(rs.getLong("customer_id"));
			reportVo.setCustomername(rs.getString("customer_name"));
//			reportVo.setCount1(rs.getInt("count1"));
//			reportVo.setAmout1(rs.getBigDecimal("amount1"));
//			for (int i = 1; i <= maxColumnIdx; i++) {
//				pNameCount = "count"+ i;
//				pNameAmount = "amount"+ i;
//				try {
//					PropertyUtils.setProperty(reportVo, pNameCount, rs.getInt(pNameCount));
//					PropertyUtils.setProperty(reportVo, pNameAmount, rs.getBigDecimal(pNameAmount));
//				} catch (Exception e) {
//					logger.error("[OrderLifeCycleReportSummaryRowMapper][mapRow][PropertyUtils.setProperty column="+ i +"]");
//				}
//			}
			
			reportVo.setAmount1(rs.getBigDecimal("amount1"));
			reportVo.setAmount2(rs.getBigDecimal("amount2"));
			reportVo.setAmount3(rs.getBigDecimal("amount3"));
			reportVo.setAmount4(rs.getBigDecimal("amount4"));
			reportVo.setAmount5(rs.getBigDecimal("amount5"));
			reportVo.setAmount6(rs.getBigDecimal("amount6"));
			reportVo.setAmount7(rs.getBigDecimal("amount7"));
			reportVo.setAmount8(rs.getBigDecimal("amount8"));
			reportVo.setAmount9(rs.getBigDecimal("amount9"));
			reportVo.setAmount10(rs.getBigDecimal("amount10"));
			reportVo.setAmount11(rs.getBigDecimal("amount11"));
			reportVo.setAmount12(rs.getBigDecimal("amount12"));
			reportVo.setAmount13(rs.getBigDecimal("amount13"));
			reportVo.setAmount14(rs.getBigDecimal("amount14"));

			reportVo.setCount1(rs.getInt("count1"));
			reportVo.setCount2(rs.getInt("count2"));
			reportVo.setCount3(rs.getInt("count3"));
			reportVo.setCount4(rs.getInt("count4"));
			reportVo.setCount5(rs.getInt("count5"));
			reportVo.setCount6(rs.getInt("count6"));
			reportVo.setCount7(rs.getInt("count7"));
			reportVo.setCount8(rs.getInt("count8"));
			reportVo.setCount9(rs.getInt("count9"));
			reportVo.setCount10(rs.getInt("count10"));
			reportVo.setCount11(rs.getInt("count11"));
			reportVo.setCount12(rs.getInt("count12"));
			reportVo.setCount13(rs.getInt("count13"));
			reportVo.setCount14(rs.getInt("count14"));
			
			reportVo.setReportdate(reportdate);
			
			return reportVo;
		}
	}
}
