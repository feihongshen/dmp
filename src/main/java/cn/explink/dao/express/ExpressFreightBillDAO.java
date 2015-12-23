package cn.explink.dao.express;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.ExpressBillBasePageView;
import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressBillParamsVO4Query;
import cn.explink.domain.VO.express.ExpressBillSummary;
import cn.explink.domain.express.ExpressFreightBill;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressBillOperFlag;
import cn.explink.enumutil.express.ExpressBillStateEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.enumutil.express.OrderFiledEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;
/**
 * 运费对账
 * @author jiangyu 2015年8月13日
 *
 */
@Repository
public class ExpressFreightBillDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressFreightBillRowMapper implements RowMapper<ExpressFreightBill> {
		@Override
		public ExpressFreightBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressFreightBill freightBill = new ExpressFreightBill();
			freightBill.setId(Long.valueOf(rs.getInt("id")));
			freightBill.setAuditorId(Long.valueOf(rs.getInt("auditor_id")));
			freightBill.setAuditorName(StringUtil.nullConvertToEmptyString(rs.getString("auditor_name")));
			freightBill.setAuditTime(rs.getDate("audit_time"));
			freightBill.setBillNo(StringUtil.nullConvertToEmptyString(rs.getString("bill_no")));
			freightBill.setBillState(rs.getInt("bill_state"));
			freightBill.setBillType(rs.getInt("bill_type"));
			freightBill.setBranchId(Long.valueOf(rs.getInt("branch_id")));
			freightBill.setBranchName(StringUtil.nullConvertToEmptyString(rs.getString("branch_name")));
			freightBill.setCavId(Long.valueOf(rs.getInt("cav_id")));
			freightBill.setCavName(StringUtil.nullConvertToEmptyString(rs.getString("cav_name")));
			freightBill.setCavTime(rs.getDate("cav_time"));
			freightBill.setClosingDate(rs.getDate("closing_date"));
			freightBill.setCod(rs.getBigDecimal("cod"));
			freightBill.setCreateTime(rs.getDate("create_time"));
			freightBill.setCreatorId(Long.valueOf(rs.getInt("creator_id")));
			freightBill.setCreatorName(StringUtil.nullConvertToEmptyString(rs.getString("creator_name")));
			freightBill.setCustomerName(StringUtil.nullConvertToEmptyString(rs.getString("customer_name")));
			freightBill.setCustomreId(Long.valueOf(rs.getInt("customer_id")));
			freightBill.setFreight(rs.getBigDecimal("freight"));
			freightBill.setOrderCount(rs.getInt("order_count"));
			freightBill.setPayableProvinceId(Long.valueOf(rs.getInt("payable_province_id")));
			freightBill.setPayableProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("payable_province_name")));
			freightBill.setPayMethod(rs.getInt("pay_method"));
			freightBill.setReceivableProvinceId(Long.valueOf(rs.getInt("receivable_province_id")));
			freightBill.setReceivableProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("receivable_province_name")));
			freightBill.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));

			return freightBill;
		}

	}

	private final class CwbInfoForBillMapper implements RowMapper<CwbOrderBillInfo> {
		// 小件员信息
		private Map<Long, String> deliverManMap;

		public CwbInfoForBillMapper(Map<Long, String> deliverManMap) {
			super();
			this.deliverManMap = deliverManMap;
		}

		@Override
		public CwbOrderBillInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderBillInfo billInfo = new CwbOrderBillInfo();
			billInfo.setId(Long.valueOf(rs.getInt("opscwbid")));
			billInfo.setOrderNo(rs.getString("cwb"));// 订单号
			billInfo.setOrderCount(rs.getLong("sendcarnum"));// 件数
			billInfo.setDeliveryMan(rs.getString("collectorname"));// 小件员
			billInfo.setSendMan(StringUtil.nullConvertToEmptyString(StringUtil.nullConvertToEmptyString(deliverManMap.get(rs.getLong("deliverid")))));// 派件员
			billInfo.setSaveFee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("insuredfee")));// 保价费
			billInfo.setTransportFee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("shouldfare")));// 应收运费
			billInfo.setTransportFeeTotal(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("totalFee")));// 总的费用
			billInfo.setReceivablefee(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("receivablefee")));// 代收货款
			billInfo.setPayMethod(ExpressSettleWayEnum.getByValue(rs.getInt("paymethod")).getText());// 付款方式
			billInfo.setBranchName(rs.getLong("deliverybranchid")+"");// 站点  //TODO[暂时取值为目的站]
			return billInfo;
		}
	}

	private final class ExpressBillBasePageViewRowMapper implements RowMapper<ExpressBillBasePageView> {

		@Override
		public ExpressBillBasePageView mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressBillBasePageView pageView = new ExpressBillBasePageView();

			pageView.setId(Long.valueOf(rs.getInt("id")));
			pageView.setBillNo(rs.getString("bill_no"));// 运单号
			pageView.setBillState(rs.getInt("bill_state"));
			pageView.setBillStateStr(ExpressBillStateEnum.getByValue(rs.getInt("bill_state")).getText());
			pageView.setCustomerId(Long.valueOf(rs.getInt("customer_id")));
			pageView.setCustomerName(StringUtil.nullConvertToEmptyString(rs.getString("customer_name")));
			pageView.setBranchId(Long.valueOf(rs.getInt("branch_id")));
			pageView.setBranchName(StringUtil.nullConvertToEmptyString(rs.getString("branch_name")));
			
			pageView.setAuditorName(StringUtil.nullConvertToEmptyString(rs.getString("auditor_name")));
			pageView.setAuditTime(StringUtil.nullDateConverToEmptyString(rs.getDate("audit_time")));
			
			pageView.setCod(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("cod")));
			pageView.setFreight(StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("freight")));
			pageView.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			
			pageView.setCreateTime(StringUtil.nullDateConverToEmptyString(rs.getDate("create_time")));
			pageView.setCreatorName(StringUtil.nullConvertToEmptyString(rs.getString("creator_name")));
			
			pageView.setCavTime(StringUtil.nullDateConverToEmptyString(rs.getDate("cav_time")));
			pageView.setCavName(StringUtil.nullConvertToEmptyString(rs.getString("cav_name")));
			
			pageView.setPayMethod(StringUtil.nullConvertToEmptyString(ExpressSettleWayEnum.getByValue(rs.getInt("pay_method")).getText()));
			
			pageView.setDealLineTime(StringUtil.nullDateConverToEmptyString(rs.getDate("closing_date")));
			
			pageView.setReceProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("receivable_province_name")));
			pageView.setPayProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("payable_province_name")));
			return pageView;
		}
	}

	/**
	 * 查询对应的记录
	 * 
	 * @return
	 */
	public List<ExpressBillBasePageView> getRecordByParams(ExpressBillParamsVO4Query params) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,bill_no,bill_state,customer_id,customer_name,branch_id,branch_name,");
		sql.append("auditor_name,audit_time,cod,freight,remark,create_time,creator_name,cav_name,cav_time,pay_method,closing_date,receivable_province_name,payable_province_name");
		sql.append(" from express_ops_freight_bill where 1=1  ");
		if (!Tools.isEmpty(params.getBillNo())) {
			// 模糊查询
			sql.append(" and bill_no like '%" + params.getBillNo() + "%'");
		}
		if (!Tools.isEmpty(params.getBillState())) {// 账单状态
			sql.append(" and bill_state=" + params.getBillState());
		}
		// 创建时间
		if (!Tools.isEmpty(params.getCreateBeginTime())) {
			sql.append(" and create_time >= '" + params.getCreateBeginTime() + "'");
		}
		if (!Tools.isEmpty(params.getCreateEndTime())) {
			sql.append(" and create_time <= '" + params.getCreateEndTime() + "'");
		}
		// 核销时间
		if (!Tools.isEmpty(params.getVerifyBeginTime())) {
			sql.append(" and cav_time >= '" + params.getVerifyBeginTime() + "'");
		}
		if (!Tools.isEmpty(params.getVerifyEndTime())) {
			sql.append(" and cav_time <= '" + params.getVerifyEndTime() + "'");
		}
		// 根据标识来
		if (ExpressBillOperFlag.Customer.getValue().equals(params.getOperFlag())) {
			// 客户
			if (!Tools.isEmpty(params.getCustomerId())) {
				sql.append(" and customer_id =" + params.getCustomerId());
			}
		} else if (ExpressBillOperFlag.Station.getValue().equals(params.getOperFlag())) {
			// 站点
			if (!Tools.isEmpty(params.getBranchId())) {
				sql.append(" and branch_id =" + params.getBranchId());
			}
		} else if (ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(params.getOperFlag())) {
			// 跨省应收
			if (!Tools.isEmpty(params.getPayableProvinceId())) {
				sql.append(" and payable_province_id =" + params.getPayableProvinceId());
			}
		}else if (ExpressBillOperFlag.AcrossProvincePay.getValue().equals(params.getOperFlag())) {
			// 跨省应付
			if (!Tools.isEmpty(params.getPayableProvinceId())) {
				sql.append(" and receivable_province_id =" + params.getPayableProvinceId());
			}
		}
		if(!Tools.isEmpty(params.getBillType())){
			sql.append(" and bill_type="+params.getBillType());
		}
		// 排序规则
		if (!Tools.isEmpty(params.getOrderField()) && !Tools.isEmpty(params.getOrderRule())) {
			String orderFiledStr = "";
			if(OrderFiledEnum.BillNo.getValue().equals(params.getOrderField())){//账单编号
				orderFiledStr = "bill_no";
			}else if(OrderFiledEnum.BillState.getValue().equals(params.getOrderField())) {
				orderFiledStr = "bill_state";
			}else if(OrderFiledEnum.CreateTime.getValue().equals(params.getOrderField())) {
				orderFiledStr = "create_time";
			}else if(OrderFiledEnum.VerifyTime.getValue().equals(params.getOrderField())) {
				orderFiledStr = "cav_time";
			}
			sql.append(" order by " + orderFiledStr + " " + params.getOrderRule());
		}
		sql.append(" limit " + ((params.getPage() - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return jdbcTemplate.query(sql.toString(), new ExpressBillBasePageViewRowMapper());
	}
	
	public Long getRecordTotalCount(ExpressBillParamsVO4Query params) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) ");
		sql.append(" from express_ops_freight_bill where 1=1  ");
		if (!Tools.isEmpty(params.getBillNo())) {
			// 模糊查询
			sql.append(" and bill_no like '%" + params.getBillNo() + "%'");
		}
		if (!Tools.isEmpty(params.getBillState())) {// 账单状态
			sql.append(" and bill_state=" + params.getBillState());
		}
		// 创建时间
		if (!Tools.isEmpty(params.getCreateBeginTime())) {
			sql.append(" and create_time >= '" + params.getCreateBeginTime() + "'");
		}
		if (!Tools.isEmpty(params.getCreateEndTime())) {
			sql.append(" and create_time <= '" + params.getCreateEndTime() + "'");
		}
		// 核销时间
		if (!Tools.isEmpty(params.getVerifyBeginTime())) {
			sql.append(" and cav_time >= '" + params.getVerifyBeginTime() + "'");
		}
		if (!Tools.isEmpty(params.getVerifyEndTime())) {
			sql.append(" and cav_time <= '" + params.getVerifyEndTime() + "'");
		}
		// 根据标识来
		if (ExpressBillOperFlag.Customer.getValue().equals(params.getOperFlag())) {
			// 客户
			if (!Tools.isEmpty(params.getCustomerId())) {
				sql.append(" and customer_id =" + params.getCustomerId());
			}
		} else if (ExpressBillOperFlag.Station.getValue().equals(params.getOperFlag())) {
			// 站点
			if (!Tools.isEmpty(params.getBranchId())) {
				sql.append(" and branch_id =" + params.getBranchId());
			}
		} else if (ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(params.getOperFlag())) {
			// 跨省应收
			if (!Tools.isEmpty(params.getPayableProvinceId())) {
				sql.append(" and payable_province_id =" + params.getPayableProvinceId());
			}
		}else if (ExpressBillOperFlag.AcrossProvincePay.getValue().equals(params.getOperFlag())) {
			// 跨省应付
			if (!Tools.isEmpty(params.getPayableProvinceId())) {
				sql.append(" and receivable_province_id =" + params.getPayableProvinceId());
			}
		}
		if(!Tools.isEmpty(params.getBillType())){
			sql.append(" and bill_type="+params.getBillType());
		}
		
		return jdbcTemplate.queryForLong(sql.toString());
	}

	/**
	 * 通过id去删除记录
	 * 
	 * @param id
	 */
	public void deleteBillByBillId(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from express_ops_freight_bill where 1=1 ");
		sql.append(" and id=" + id);
		jdbcTemplate.update(sql.toString());

	}

	/**
	 * 获取记录
	 * 
	 * @param deliverManMap
	 * @param params
	 * @param isPreScan 
	 * @param b
	 * @return
	 */
	public List<CwbOrderBillInfo> getCwbRecordListByPage(Map<Long, String> deliverManMap, ExpressBillParams4Create params, boolean isPaging) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.opscwbid,c.cwb,c.sendcarnum,c.deliverid,c.instationdatetime,c.shouldfare,c.packagefee,c.insuredfee,c.paymethod");
		sql.append(",(ifnull(c.shouldfare,0)+ifnull(c.packagefee,0)+ifnull(c.insuredfee,0)) as totalFee,c.receivablefee,c.collectorid,c.collectorname,c.deliverybranchid");
		sql.append(" from express_ops_cwb_detail c ");
		
		
		//公共的查询条件
		sql.append(preOrEditedScanCwbList(params));
		
		if (isPaging) {
			sql.append(" limit " + ((params.getPage() - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		}
		return this.jdbcTemplate.query(sql.toString(), new CwbInfoForBillMapper(deliverManMap));
	}
	

	// 查询记录数
	public Long getExpressRecordCount(ExpressBillParams4Create params) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) count FROM express_ops_cwb_detail c ");
		//公共的查询条件
		sql.append(preOrEditedScanCwbList(params));
		return this.jdbcTemplate.queryForLong(sql.toString());
	}

	/**
	 * 创建账单记录
	 * @param params
	 * @return
	 */
	public Long createBillRecord(final ExpressBillParams4Create params) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_freight_bill ");
		sql.append(" (bill_no,bill_state,closing_date,pay_method,");
		sql.append(" order_count,bill_type,freight,cod,");
		sql.append(" creator_id,creator_name,create_time,");
		sql.append(" customer_id,customer_name,branch_id,branch_name,");
		sql.append(" receivable_province_id,receivable_province_name,");
		sql.append(" payable_province_id,payable_province_name,");
		sql.append(" remark )");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, params.getBillNo4Create());
				ps.setInt(++i, ExpressBillStateEnum.UnAudit.getValue());
				ps.setDate(++i, new Date(params.getDateSeconds()));
				ps.setInt(++i, params.getPayMethod4Create());

				ps.setInt(++i, params.getOrderCount());
				ps.setInt(++i, params.getBillType());
				ps.setBigDecimal(++i, params.getTransFee());
				ps.setBigDecimal(++i, params.getReceiveableFee());

				ps.setLong(++i, params.getUser().getUserid());
				ps.setString(++i, params.getUser().getRealname());
				ps.setDate(++i, new Date(new java.util.Date().getTime()));

				ps.setLong(++i, StringUtil.nullConvertToLong(params.getCustomerId4Create()));
				ps.setString(++i, StringUtil.nullConvertToEmptyString(params.getCustomerName()));
				ps.setLong(++i, StringUtil.nullConvertToLong(params.getBranchId4Create()));
				ps.setString(++i, StringUtil.nullConvertToEmptyString(params.getBranchName()));

				ps.setLong(++i, StringUtil.nullConvertToLong(params.getReceProvince4Create()));
				ps.setString(++i, StringUtil.nullConvertToEmptyString(params.getReceivableProvinceName()));
				ps.setLong(++i, StringUtil.nullConvertToLong(params.getPayProvince4Create()));
				ps.setString(++i, StringUtil.nullConvertToEmptyString(params.getPayableProvinceName()));

				ps.setString(++i, StringUtil.nullConvertToEmptyString(params.getRemark4Create()));

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 
	 * @param billId
	 * @return
	 */
	public ExpressFreightBill getRecordById(Long billId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from express_ops_freight_bill where id=" + billId);
		return jdbcTemplate.queryForObject(sql.toString(), new ExpressFreightBillRowMapper());
	}

	/**
	 * 
	 * @param billNo
	 * @return
	 */
	public ExpressFreightBill getRecoredByBillNo(String billNo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from express_ops_freight_bill where bill_no='" + billNo + "'");
		return jdbcTemplate.queryForObject(sql.toString(), new ExpressFreightBillRowMapper());
	}

	/**
	 * 
	 * @param billId
	 * @param remark
	 * @return
	 */
	public Integer updateRecordById(Long billId, String remark) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_freight_bill ");
		sql.append(" set remark='" + remark + "'");
		sql.append(" where id=" + billId);
		return jdbcTemplate.update(sql.toString());
	}
	/**
	 * 
	 * @param params
	 * @return
	 */
	public ExpressBillSummary getSummaryRecord(ExpressBillParams4Create params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select COUNT(1) as totalCount,SUM(IFNULL(c.packagefee,0)+IFNULL(c.insuredfee,0)+IFNULL(c.shouldfare,0)) as sumFee");
		sql.append(" from express_ops_cwb_detail c ");
		//连表查询【反馈表】
		sql.append(" LEFT JOIN express_ops_delivery_state AS d ON c.cwb=d.cwb  WHERE d.state=1 ");
		//区分不同类型的账单
		sql.append(init4FreightTypeBillCommSql(params));
		// 是否生成过账单的
		sql.append(" and c.customerfreightbillid = 0 ");
		sql.append(" and c.branchfreightbillid = 0 ");
		sql.append(" and c.provincereceivablefreightbillid = 0 ");
		sql.append(" and c.provincepayblefreightbillid = 0 ");
		sql.append(" and c.provincereceivablecodbillid = 0 ");
		sql.append(" and c.provincepayablecodbillid = 0 ");
		
		return this.jdbcTemplate.queryForObject(sql.toString(), new CountInfoForSummary(params.getPayMethod4Create()));
	}
	
	/**
	 * 统计信息
	 * @author jiangyu 2015年8月15日
	 *
	 */
	private final class CountInfoForSummary implements RowMapper<ExpressBillSummary> {
		private Integer payMethod;
		public CountInfoForSummary(Integer payMethod) {
			this.payMethod = payMethod;
		}
		@Override
		public ExpressBillSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressBillSummary record = new ExpressBillSummary();
			if (ExpressSettleWayEnum.MonthPay.getValue().equals(payMethod)) {
				record.setCountName(ExpressSettleWayEnum.MonthPay.getText());
				record.setFeeName(ExpressSettleWayEnum.MonthPay.getText());
			}else if(ExpressSettleWayEnum.ArrivePay.getValue().equals(payMethod)){
				record.setCountName(ExpressSettleWayEnum.ArrivePay.getText());
				record.setFeeName(ExpressSettleWayEnum.ArrivePay.getText());
			}else if(ExpressSettleWayEnum.NowPay.getValue().equals(payMethod)){
				record.setCountName(ExpressSettleWayEnum.NowPay.getText());
				record.setFeeName(ExpressSettleWayEnum.NowPay.getText());
			}
			record.setCount(rs.getLong("totalCount"));
			record.setFee(rs.getBigDecimal("sumFee")==null?BigDecimal.ZERO:rs.getBigDecimal("sumFee"));
			return record;
		}
	}
	/**
	 * 查询特定属性的账单内容
	 * @param valueOf
	 * @return
	 */
	public ExpressBillBasePageView getEditPageViewByBillId(Long billId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,bill_no,bill_state,customer_id,customer_name,branch_id,branch_name,");
		sql.append("auditor_name,audit_time,cod,freight,remark,create_time,creator_name,cav_name,cav_time,pay_method,closing_date,receivable_province_name,payable_province_name");
		sql.append(" from express_ops_freight_bill where 1=1  ");
		sql.append(" and id="+billId);
		return jdbcTemplate.queryForObject(sql.toString(), new ExpressBillBasePageViewRowMapper());
	}
	/**
	 * 更新订单的信息
	 * @param billId
	 * @param cwbs
	 * @param opeFlag 
	 */
	public Integer updateCwbRecord(Long billId, List<String> cwbs, Integer opeFlag) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_cwb_detail set ");
		if(ExpressBillOperFlag.Customer.getValue().equals(opeFlag)){
			//针对客户
			sql.append(" customerfreightbillid="+billId);
		}else if(ExpressBillOperFlag.Station.getValue().equals(opeFlag)){
			//站点
			sql.append(" branchfreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("provincereceivablefreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvincePay.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("provincepayblefreightbillid="+billId);
		}
		sql.append(" where 1=1 ");
		sql.append(" and state=1 ");
		sql.append(" and cwb"+Tools.assembleInByList(cwbs));
		return jdbcTemplate.update(sql.toString());
		
	}
	/**
	 * 获取和账单相关的订单
	 * @param billId
	 * @param deliverManMap
	 * @param page 
	 * @param opeFlag 
	 * @return
	 */
	public List<CwbOrderBillInfo> getBillRelateCwb(Long billId, Map<Long, String> deliverManMap, Long page, Integer opeFlag) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.opscwbid,c.cwb,c.sendcarnum,c.deliverid,c.instationdatetime,c.shouldfare,c.packagefee,c.insuredfee,c.paymethod");
		sql.append(",(ifnull(c.shouldfare,0)+ifnull(c.packagefee,0)+ifnull(c.insuredfee,0)) as totalFee,c.receivablefee,c.collectorid,c.collectorname,c.deliverybranchid");
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		if(ExpressBillOperFlag.Customer.getValue().equals(opeFlag)){
			//针对客户
			sql.append("and c.customerfreightbillid="+billId);
		}else if(ExpressBillOperFlag.Station.getValue().equals(opeFlag)){
			//站点
			sql.append("and c.branchfreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("and c.provincereceivablefreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvincePay.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("and c.provincepayblefreightbillid="+billId);
		}
		sql.append(" and c.state=1 ");
		sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new CwbInfoForBillMapper(deliverManMap));
	}
	/**
	 * 查询记录总数
	 * @param billId
	 * @param opeFlag
	 * @return
	 */
	public Long getExpressRecordCount(Long billId,Integer opeFlag) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) count FROM express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		
		if(ExpressBillOperFlag.Customer.getValue().equals(opeFlag)){
			//针对客户
			sql.append("and c.customerfreightbillid="+billId);
		}else if(ExpressBillOperFlag.Station.getValue().equals(opeFlag)){
			//站点
			sql.append("and c.branchfreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("and c.provincereceivablefreightbillid="+billId);
		}else if(ExpressBillOperFlag.AcrossProvincePay.getValue().equals(opeFlag)){
			//跨省应收
			sql.append("and c.provincepayblefreightbillid="+billId);
		}
		sql.append(" and c.state=1 ");
		
		return this.jdbcTemplate.queryForLong(sql.toString());
	}
	/**
	 * 更新账单的信息
	 * @param billId
	 * @param remark
	 */
	public void updateBillByBillId(Long billId, String remark) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_freight_bill set remark='"+remark+"'");
		sql.append(" where id="+billId);
		this.jdbcTemplate.update(sql.toString());
	}
	
	/**
	 * 组织公共的查询语句
	 * @param params
	 * @return
	 */
	private String preOrEditedScanCwbList(ExpressBillParams4Create params) {
		StringBuffer sql = new StringBuffer();
		//连表查询【反馈表】
		sql.append(" LEFT JOIN express_ops_delivery_state AS d ON c.cwb=d.cwb  WHERE d.state=1 ");
		//区分不同类型的账单
		sql.append(init4FreightTypeBillCommSql(params));
		
		if(params.getIsPreScan()){//预览
			// 没有生成过账单的订单记录
			sql.append(" and c.customerfreightbillid = 0 ");
			sql.append(" and c.branchfreightbillid = 0 ");
			sql.append(" and c.provincereceivablefreightbillid = 0 ");
			sql.append(" and c.provincepayblefreightbillid = 0 ");
			sql.append(" and c.provincereceivablecodbillid = 0 ");
			sql.append(" and c.provincepayablecodbillid = 0 ");
		}else {//编辑后要查询的记录
			if (ExpressBillOperFlag.Customer.getValue().equals(params.getOpeFlag())) {
				//客户
				sql.append(" and c.customerfreightbillid="+params.getBillId());
			}else if(ExpressBillOperFlag.Station.getValue().equals(params.getOpeFlag())){
				//站点
				sql.append(" and c.branchfreightbillid="+params.getBillId());
			}else if (ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(params.getOpeFlag())) {
				//跨省应收
				sql.append(" and c.provincereceivablefreightbillid="+params.getBillId());
			}else if (ExpressBillOperFlag.AcrossProvincePay.getValue().equals(params.getOpeFlag())) {
				//跨省应付
				sql.append(" and c.provincepayblefreightbillid="+params.getBillId());
			}
		}
		
		return sql.toString();
	}

	
	/**
	 * 针对不同的运费账单类型组织的sql
	 * @param params
	 * @return
	 */
	private String init4FreightTypeBillCommSql(ExpressBillParams4Create params) {
		StringBuffer sql = new StringBuffer();
		if (ExpressBillOperFlag.Customer.getValue().equals(params.getOpeFlag())) {//客户
//			sql.append(" where 1=1 ");
			sql.append(" and c.customerid="+params.getCustomerId4Create());
			sql.append(" and c.senderprovinceid=c.recprovinceid ");
			sql.append(" and c.instationdatetime<'"+params.getDeadLineTime4Create()+"'");//< 揽件入站截止日期
			
		}else if(ExpressBillOperFlag.Station.getValue().equals(params.getOpeFlag())){
			//区分现付和到付
			if (ExpressSettleWayEnum.NowPay.getValue().equals(params.getPayMethod4Create())) {//现付
//				sql.append(" where 1=1 ");
				sql.append(" and c.instationid="+params.getBranchId4Create());//站点是指揽件入站的站点
				sql.append(" and c.instationdatetime > '"+params.getDeadLineTime4Create()+"'");// < 揽件入站截止日期
				
			}else if (ExpressSettleWayEnum.ArrivePay.getValue().equals(params.getPayMethod4Create())) {//到付
				//连表查询【反馈表】
//				sql.append(" LEFT JOIN express_ops_delivery_state AS d ON c.cwb=d.cwb  WHERE d.state=1 ");
				sql.append(" and c.deliverybranchid="+params.getBranchId4Create());//站点是指派件站点    也就是目的站                           
				sql.append(" and d.sign_time > '"+params.getDeadLineTime4Create()+"'");// < 签收截止日期 sign_time
			}
			sql.append(" and c.senderprovinceid=c.recprovinceid ");
		}else if (ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(params.getOpeFlag())) {
//			sql.append(" LEFT JOIN express_ops_delivery_state AS d ON c.cwb=d.cwb  WHERE d.state=1 ");
			//跨省应收         收件人省和 对应的是应付的省份
			//sql.append(" and senderprovinceid = "+params.getReceProvince4Create());
			sql.append(" and c.senderprovinceid="+params.getPayProvince4Create());
			sql.append(" and d.auditingtime > '"+params.getDeadLineTime4Create()+"'");//< 妥投审核截止日期  auditingtime
			sql.append(" and c.senderprovinceid<>c.recprovinceid "); 
			
		}else if (ExpressBillOperFlag.AcrossProvincePay.getValue().equals(params.getOpeFlag())) {
			//跨省应付 TODO   -----》 对应的是应收的省份
//			sql.append(" where 1=1 ");
			sql.append(" and c.senderprovinceid<>c.recprovinceid ");
		}
		
		sql.append(" and c.paymethod=" + params.getPayMethod4Create());
		// 配送成功的订单
		sql.append(" and d.deliverystate=" + DeliveryStateEnum.PeiSongChengGong.getValue());
		// 订单的操作状态是已审核
		sql.append(" and c.flowordertype=" + FlowOrderTypeEnum.YiShenHe.getValue());
		// 快递类型
		sql.append(" and c.cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		
		sql.append(" and c.state=1 ");
		
		return sql.toString();
	}
	/**
	 * 删除账单时更新订单记录
	 * @param billId
	 * @param billType
	 */
	public void updateCwbDetailBillId(Long billId,Integer billType){
		StringBuffer sql = new StringBuffer();
		sql.append("update express_ops_cwb_detail c set ");
		if (ExpressBillOperFlag.Customer.getValue().equals(billType)) {//客户
			sql.append(" c.customerfreightbillid=0 ");
			sql.append(" where c.customerfreightbillid="+billId);
		}else if(ExpressBillOperFlag.Station.getValue().equals(billType)){
			sql.append(" c.branchfreightbillid=0 ");
			sql.append(" where c.branchfreightbillid="+billId);
		}else if (ExpressBillOperFlag.AcrossProvinceRece.getValue().equals(billType)) {
			sql.append(" c.provincereceivablefreightbillid=0 ");
			sql.append(" where c.provincereceivablefreightbillid="+billId);
		}else if (ExpressBillOperFlag.AcrossProvincePay.getValue().equals(billType)) {
			sql.append(" c.provincepayblefreightbillid=0 ");
			sql.append(" where c.provincepayblefreightbillid="+billId);
		}
	}
}
