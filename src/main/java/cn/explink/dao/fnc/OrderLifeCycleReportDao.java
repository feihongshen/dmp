package cn.explink.dao.fnc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
	
	/**
	 * 
	 * @param selectedCustomers
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public List<OrderLifeCycleReportVO> getListByCustomers(String selectedCustomers) {
		
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select lc.customerid as customer_id ,customername as customer_name ,")
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
		.append(" from fn_rpt_order_lifecycle lc left join express_set_customer_info c on lc.customerid = c.customerid")
		.append(" where 1 = 1");
		
		if(StringUtils.isNotBlank(selectedCustomers)){
			sqlBuilder.append("and lc.customerid in (").append(selectedCustomers).append(")");
		}
		
		sqlBuilder.append(" group by lc.customerid");
		
		List<OrderLifeCycleReportVO> result = jdbcTemplate.query(sqlBuilder.toString(), new OrderLifeCycleReportSummaryRowMapper());
		
		return result;
	}

	
	private final class OrderLifeCycleReportSummaryRowMapper implements RowMapper<OrderLifeCycleReportVO> {
		@Override
		public OrderLifeCycleReportVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderLifeCycleReportVO reportVo = new OrderLifeCycleReportVO();
			String pNameAmount = null;
			String pNameCount = null;
			final int maxColumnIdx = 14;
			reportVo.setCustomerid(rs.getLong("customer_id"));
			reportVo.setCustomername(rs.getString("customer_name"));
			
			for (int i = 1; i <= maxColumnIdx; i++) {
				pNameCount = "count"+ i;
				pNameAmount = "amount"+ i;
				try {
					PropertyUtils.setProperty(reportVo, pNameCount, rs.getInt(pNameCount));
					PropertyUtils.setProperty(reportVo, pNameAmount, rs.getBigDecimal(pNameAmount));
				} catch (Exception e) {
					logger.error("[OrderLifeCycleReportSummaryRowMapper][mapRow][PropertyUtils.setProperty column="+ i +"]");
				}
			}
			
			
			return reportVo;
		}
	}
}
