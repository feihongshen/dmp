package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.express.ExpressFreightBill;
import cn.explink.enumutil.express.ExpressBillStateEnum;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.util.Page;
import cn.explink.util.Tools;

/**
 * 跨省到付运费审核（应收）
 * @author wangzy 2015年8月11日
 *
 */
@Repository
public class ExpressAcrossProvinceReceFreightAuditDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressFreightBillRowMapper implements RowMapper<ExpressFreightAuditBillVO>{
		
		@Override
		public ExpressFreightAuditBillVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressFreightAuditBillVO expressFreightBill = new ExpressFreightAuditBillVO();
			expressFreightBill.setId(rs.getLong("id"));
			expressFreightBill.setBillNo(rs.getString("bill_no"));
			expressFreightBill.setBillState(ExpressBillStateEnum.getByValue(rs.getInt("bill_state")).getText());
			expressFreightBill.setCustomreId(rs.getLong("customer_id"));
			expressFreightBill.setCustomerName(rs.getString("customer_name"));
			expressFreightBill.setFreight(rs.getBigDecimal("freight"));
			expressFreightBill.setCod(rs.getBigDecimal("cod"));
			expressFreightBill.setCreatorId(rs.getLong("creator_id"));
			expressFreightBill.setCreatorName(rs.getString("creator_name"));
			expressFreightBill.setCreateTime(rs.getDate("create_time"));
			expressFreightBill.setAuditorId(rs.getLong("auditor_id"));
			expressFreightBill.setAuditorName(rs.getString("auditor_name"));
			expressFreightBill.setClosingDate(rs.getDate("closing_date"));
			expressFreightBill.setAuditTime(rs.getDate("audit_time"));
			expressFreightBill.setAuditTime(rs.getDate("audit_time"));
			expressFreightBill.setCavId(rs.getLong("cav_id"));
			expressFreightBill.setCavName(rs.getString("cav_name"));
			expressFreightBill.setCavTime(rs.getDate("cav_time"));
			expressFreightBill.setReceivableProvinceId(rs.getLong("receivable_province_id"));
			expressFreightBill.setReceivableProvinceName(rs.getString("receivable_province_name"));
			expressFreightBill.setPayableProvinceId(rs.getLong("payable_province_id"));
			expressFreightBill.setPayableProvinceName(rs.getString("payable_province_name"));
			expressFreightBill.setRemark(rs.getString("remark"));
			return expressFreightBill;
		}

	}
	
	
	/**
	 * 查询方法
	 * 
	 * @author 王志宇
	 */
	public List<ExpressFreightAuditBillVO> queryBillForFreight(Long page,ExpressFreightAuditBillVO expressFreightAuditBillVO){
		String sql = "SELECT * FROM express_ops_freight_bill where 1=1 and bill_type=" + ExpressBillTypeEnum.AcrossProvinceReceivableBill.getValue();//2表示账单类型为跨省应收运费账单
		sql += getConditionsSql(expressFreightAuditBillVO);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		if(expressFreightAuditBillVO.getSortRule()==null){
			return null;
		}else{
			return this.jdbcTemplate.query(sql,new ExpressFreightBillRowMapper());
		}
	}

	/**
	 * 查询账单数量
	 * @author 王志宇
	 * @param expressFreightAuditBillVO
	 * @return
	 */
	public int getBillCount(ExpressFreightAuditBillVO expressFreightAuditBillVO){
		String sql = "select count(1) from express_ops_freight_bill where 1=1 and bill_type=" + ExpressBillTypeEnum.AcrossProvinceReceivableBill.getValue();//2表示账单类型为跨省应收运费账单
		sql += getConditionsSql(expressFreightAuditBillVO);
		if(expressFreightAuditBillVO.getSortRule()==null){
			return 0;
		}else{
			return this.jdbcTemplate.queryForInt(sql);
		}
	}

	
	//追加查询条件
	public String getConditionsSql(ExpressFreightAuditBillVO expressFreightAuditBillVO){
		StringBuffer sql = new StringBuffer();
		//账单编号
		if(!Tools.isEmpty(expressFreightAuditBillVO.getBillNo())){
			sql.append(" and bill_no like '%"+expressFreightAuditBillVO.getBillNo()+"%'");
		}
		//账单状态
		if(!Tools.isEmpty(expressFreightAuditBillVO.getBillState())){
			sql.append(" and bill_state='"+expressFreightAuditBillVO.getBillState()+"'");
		}
		//账单创建时间
		if(!Tools.isEmpty(expressFreightAuditBillVO.getStartCreatTime())){
			sql.append(" and create_time > '"+expressFreightAuditBillVO.getStartCreatTime()+"'");
		}
		if(!Tools.isEmpty(expressFreightAuditBillVO.getEndCreatTime())){
			sql.append(" and create_time < '"+expressFreightAuditBillVO.getEndCreatTime()+"'");
		}
		//账单核销时间
		if(!Tools.isEmpty(expressFreightAuditBillVO.getStartAuditTime())){
			sql.append(" and audit_time > '"+expressFreightAuditBillVO.getStartAuditTime()+"'");
		}
		if(!Tools.isEmpty(expressFreightAuditBillVO.getEndAuditTime())){
			sql.append(" and audit_time < '"+expressFreightAuditBillVO.getEndAuditTime()+"'");
		}
		//应收省份
		if(!Tools.isEmpty(expressFreightAuditBillVO.getReceivableProvinceId())){
			sql.append(" and customer_id ='"+expressFreightAuditBillVO.getReceivableProvinceId()+"'");
		}
		//排序
		if(!Tools.isEmpty(expressFreightAuditBillVO.getSortOption())&&!Tools.isEmpty(expressFreightAuditBillVO.getSortRule())){
			sql.append(" order by "+expressFreightAuditBillVO.getSortOption()+" "+expressFreightAuditBillVO.getSortRule());
		}

		return sql.toString();
	}
	
	/**
	 * 根据id查询账单
	 * @author 王志宇
	 */
	public ExpressFreightAuditBillVO getBillById(Long id){
		String sql = "select * from express_ops_freight_bill where id='"+id+"'";
		return this.jdbcTemplate.query(sql, new ExpressFreightBillRowMapper()).get(0);
	}

	/**
	 * 审核、取消审核、核销
	 * 根据id修改账单状态
	 * @author 王志宇
	 */
	public int updateState(Long id,Integer state){
		String sql = "UPDATE express_ops_freight_bill SET bill_state='"+state+"' where id='"+id+"'";
		return this.jdbcTemplate.update(sql);
	}


}
