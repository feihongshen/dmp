package cn.explink.dao.express;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.CwbOrderPartInfo;
import cn.explink.domain.express.ExpressFreightBillImportDetail;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressEffectiveEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

@Repository
public class ExpressFreightBillImportDetailDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	private final class ExpressFreightBillDetailRowMapper implements RowMapper<ExpressFreightBillImportDetail> {
		@Override
		public ExpressFreightBillImportDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressFreightBillImportDetail freightBill = new ExpressFreightBillImportDetail();
			freightBill.setId(Long.valueOf(rs.getInt("id")));
			freightBill.setOrderNo(rs.getString("order_no"));
			freightBill.setGoodNum(rs.getInt("good_num"));
			freightBill.setCollectPerson(rs.getString("collect_person"));
			freightBill.setDeliveryPerson(rs.getString("delivery_person"));
			freightBill.setSumFee(rs.getBigDecimal("sum_fee"));
			freightBill.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
			freightBill.setPackageFee(rs.getBigDecimal("package_fee"));
			freightBill.setInsuredFee(rs.getBigDecimal("insured_fee"));
			freightBill.setCod(rs.getBigDecimal("cod"));
			freightBill.setDeliveryBranchId(rs.getLong("delivery_branch_id"));
			freightBill.setDeliveryBranch(rs.getString("delivery_branch"));
			freightBill.setBillId(rs.getLong("bill_id"));
			freightBill.setBillNo(rs.getString("bill_no"));
			freightBill.setEffectFlag(rs.getInt("effect_flag"));
			freightBill.setDismatchReason(rs.getString("dismatch_reason"));
			freightBill.setImportPersonId(rs.getLong("import_person_id"));
			freightBill.setImportPerson(rs.getString("import_person"));
			freightBill.setImportTime(rs.getDate("import_time"));
			return freightBill;
		}
	}
	
	private final class BillDetailInfo implements RowMapper<ExpressFreightBillImportDetail> {
		@Override
		public ExpressFreightBillImportDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressFreightBillImportDetail freightBill = new ExpressFreightBillImportDetail();
			freightBill.setOrderNo(rs.getString("order_no"));
			freightBill.setBillNo(rs.getString("bill_no"));
			return freightBill;
		}
	}
	
	
	private final class CwbInfoForBillMapper implements RowMapper<CwbOrderBillInfo> {

		@Override
		public CwbOrderBillInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderBillInfo billInfo = new CwbOrderBillInfo();
			billInfo.setId(Long.valueOf(0));
			billInfo.setOrderNo(rs.getString("order_no"));// 订单号
			billInfo.setOrderCount(rs.getLong("good_num"));// 件数
			billInfo.setSendMan(StringUtil.nullConvertToEmptyString(rs.getString("collect_person")));// 派件员
			billInfo.setDeliveryMan(StringUtil.nullConvertToEmptyString(rs.getString("delivery_person")));// 小件员
			billInfo.setTransportFeeTotal(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("sum_fee")));// 总的费用
			billInfo.setTransportFee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("delivery_fee")));// 运费
			billInfo.setPackFee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("package_fee")));//包装费用
			billInfo.setSaveFee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("insured_fee")));// 保价费
			billInfo.setReceivablefee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("cod")));// 代收货款
			billInfo.setBranchName(StringUtil.nullConvertToEmptyString(rs.getString("delivery_branch")));// 站点
			billInfo.setBranchId(StringUtil.nullConvertToLong(rs.getLong("delivery_branch_id")));// 站点id
			billInfo.setNotMatchReason(StringUtil.nullConvertToEmptyString(rs.getString("dismatch_reason")));//未匹配的原因
			return billInfo;
		}
	}

	private final class CwbPartInfoMapper implements RowMapper<CwbOrderPartInfo> {
		@Override
		public CwbOrderPartInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderPartInfo partInfoOrder = new CwbOrderPartInfo();
			partInfoOrder.setOrderNo(rs.getString(""));
			partInfoOrder.setDeliveryFee(rs.getBigDecimal(""));
			partInfoOrder.setBranchId(rs.getLong(""));
			return partInfoOrder;
		}
	}
	
	
	/**
	 * 批量插入记录
	 * @param records
	 */
	public void batchInsertOrderRecord(List<ExpressFreightBillImportDetail> records) {
		 final List<ExpressFreightBillImportDetail> tempOrderList = records;   
	     String sql="insert into express_ops_freight_bill_detail_import"
	       		+ "(order_no,good_num,collect_person,delivery_person,"
	       		+ "sum_fee,delivery_fee,package_fee,insured_fee,"
	       		+ "cod,delivery_branch_id,delivery_branch,bill_id,"
	       		+ "bill_no,effect_flag,dismatch_reason,import_person_id,"
	       		+ "import_person,import_time"
	       		+ ")"
	       		+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";   
	       jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {  
	            @Override
	            public int getBatchSize() {  
	                 return tempOrderList.size();   
	            }
	            
	            @Override  
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	            	ps.setString(1, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getOrderNo()));
	            	ps.setInt(2, tempOrderList.get(i).getGoodNum());
	            	ps.setString(3, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getCollectPerson()));
	            	ps.setString(4, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getDeliveryPerson()));
	            	ps.setBigDecimal(5, tempOrderList.get(i).getSumFee());
	            	ps.setBigDecimal(6, tempOrderList.get(i).getDeliveryFee());
	            	ps.setBigDecimal(7, tempOrderList.get(i).getPackageFee());
	            	ps.setBigDecimal(8, tempOrderList.get(i).getInsuredFee());
	            	ps.setBigDecimal(9, tempOrderList.get(i).getCod());
	            	ps.setLong(10, StringUtil.nullConvertToLong(tempOrderList.get(i).getDeliveryBranchId()));
	            	ps.setString(11, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getDeliveryBranch()));
	            	ps.setLong(12, tempOrderList.get(i).getBillId());
	            	ps.setString(13, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getBillNo()));
	            	ps.setInt(14, tempOrderList.get(i).getEffectFlag());
	            	ps.setString(15, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getDismatchReason()));
	            	ps.setLong(16, StringUtil.nullConvertToLong(tempOrderList.get(i).getImportPersonId()));
	            	ps.setString(17, StringUtil.nullConvertToEmptyString(tempOrderList.get(i).getImportPerson()));
	            	ps.setDate(18, new java.sql.Date(tempOrderList.get(i).getImportTime().getTime()));
	            }   
	      });   
		
	}
	
	/**
	 * 批量更新
	 * @param billId
	 * @param status 
	 */
	public Integer batchUpdateImportDetail(Long billId, Integer status) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_freight_bill_detail_import ");
		sql.append(" set effect_flag="+status);
		sql.append(" where bill_id="+billId);
		sql.append(" and effect_flag="+ExpressEffectiveEnum.UnEffective.getValue());
		return jdbcTemplate.update(sql.toString());
	}
	
	/**
	 * 查询校验通过的订单号的记录是否存在于账单中
	 * @param orderNos
	 * @return
	 */
	public Map<String, Object> queryRecordIsExist(List<String> orderNos) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (orderNos==null||orderNos.size()==0) {
			return map;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select order_no,bill_no from express_ops_freight_bill_detail_import ");
		sql.append(" where order_no "+Tools.assembleInByList(orderNos));
		sql.append(" and effect_flag="+ExpressEffectiveEnum.Effective.getValue());
		List<ExpressFreightBillImportDetail> list =  jdbcTemplate.query(sql.toString(),new BillDetailInfo());
		if (list!=null&&list.size()>0) {
			for (ExpressFreightBillImportDetail record : list) {
				map.put(record.getOrderNo(), record.getBillNo());
			}
		}
		return map;
	}
	
	/**
	 * 获取和账单绑定的订单
	 * @param billId
	 * @param page
	 * @param opeFlag
	 * @return
	 */
	public List<CwbOrderBillInfo> getBillRelateCwb(Long billId, Long page, Integer effectState,Boolean isPaging) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.order_no,c.good_num,c.collect_person,c.delivery_person,");
		sql.append(" c.sum_fee,c.delivery_fee,c.package_fee,c.insured_fee,c.cod,");
		sql.append(" c.delivery_branch,c.dismatch_reason,c.delivery_branch_id");
		sql.append(" from express_ops_freight_bill_detail_import c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.bill_id="+billId);
		sql.append(" and c.effect_flag="+effectState);
		if (isPaging) {
			sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		}
		return this.jdbcTemplate.query(sql.toString(), new CwbInfoForBillMapper());
	}
	
	/**
	 * 获取记录数量
	 * @param billId
	 * @param opeFlag
	 * @return
	 */
	public Long getExpressRecordCount(Long billId, Integer effectState) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1)");
		sql.append(" from express_ops_freight_bill_detail_import c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.bill_id="+billId);
		sql.append(" and c.effect_flag="+effectState);
		return this.jdbcTemplate.queryForLong(sql.toString());
	}
	/**
	 * 查询订单主表记录
	 * @param orderNos
	 * @return
	 */
	public Map<String, CwbOrderPartInfo> getCwbOrderPartInfo(List<String> orderNos) {
		Map<String,CwbOrderPartInfo> resMap = new HashMap<String, CwbOrderPartInfo>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.cwb,c.totalfee,c.instationid");
		sql.append(" from express_ops_cwb_detail c ");
		//连表查询【反馈表】
		sql.append(" LEFT JOIN express_ops_delivery_state AS d ON c.cwb=d.cwb  WHERE d.state=1 ");
		// 到付的订单
		sql.append(" and c.paymethod=" +ExpressSettleWayEnum.ArrivePay.getValue());
		// 配送成功的订单
		sql.append(" and d.deliverystate=" + DeliveryStateEnum.PeiSongChengGong.getValue());
		
		sql.append(" and c.flowordertype=" + FlowOrderTypeEnum.YiShenHe.getValue());
		// 快递类型
		sql.append(" and c.cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.state=1 ");
		List<CwbOrderPartInfo> list = jdbcTemplate.query(sql.toString(), new CwbPartInfoMapper());
		if (list!=null&&list.size()>0) {
			for (CwbOrderPartInfo entity : list) {
				resMap.put(entity.getOrderNo(), entity);
			}
		}
		return resMap;
	}
	/**
	 * 更新没有匹配的原因
	 * @param redList
	 */
	public void updateNotMatchReason(CwbOrderBillInfo orderTemp,Long billId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_freight_bill_detail_import c set dismatch_reason='"+orderTemp.getNotMatchReason()+"'");
		sql.append(" where c.order_no='"+orderTemp.getOrderNo()+"'");
		sql.append(" and c.bill_id="+billId);
		sql.append(" and c.effect_flag="+ExpressEffectiveEnum.UnEffective.getValue());
		jdbcTemplate.update(sql.toString());
	}
	/**
	 * 更新账单的信息
	 * @param billId
	 * @param remark
	 * @param orderCount
	 */
	public void updateBillByBillId(Long billId, String remark, Integer orderCount) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_freight_bill ");
		Boolean flag = false;
		if (!Tools.isEmpty(remark)) {
			sql.append(" set remark='"+remark+"'");
			flag = true;
		}
		if (flag) {
			sql.append(",order_count="+orderCount);
		}else {
			sql.append(" set order_count="+orderCount);
		}
		sql.append(" where id="+billId);
		this.jdbcTemplate.update(sql.toString());
	}
	
}
