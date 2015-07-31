/**
 *
 */
package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.DiliverymanPaifeiBill;
import cn.explink.domain.DiliverymanPaifeiOrder;
import cn.explink.util.Page;

/**
 * 配送员派费dao
 *
 * @author wangqiang
 */
@Component
public class DiliverymanPaifeiBillDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class DiliverymanPaifeiBillRowMapper implements RowMapper<DiliverymanPaifeiBill> {
		@Override
		public DiliverymanPaifeiBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			DiliverymanPaifeiBill diliverymanPaifeiBill = new DiliverymanPaifeiBill();
			diliverymanPaifeiBill.setId(rs.getInt("id"));
			diliverymanPaifeiBill.setBillbatch(rs.getString("billbatch"));
			diliverymanPaifeiBill.setBillstate(rs.getInt("billstate"));
			diliverymanPaifeiBill.setDiliveryman(rs.getInt("diliveryman"));
			diliverymanPaifeiBill.setTheirsite(rs.getInt("theirsite"));
			diliverymanPaifeiBill.setBillestablishdate(rs.getString("billestablishdate"));
			diliverymanPaifeiBill.setBillverificationdate(rs.getString("billverificationdate"));
			diliverymanPaifeiBill.setOrdertype(rs.getInt("ordertype"));
			diliverymanPaifeiBill.setOrdersum(rs.getInt("ordersum"));
			diliverymanPaifeiBill.setPaifeimoney(rs.getBigDecimal("paifeimoney"));
			diliverymanPaifeiBill.setRemarks(rs.getString("remarks"));
			diliverymanPaifeiBill.setDaterange(rs.getString("daterange"));
			diliverymanPaifeiBill.setOrderids(rs.getString("orderids"));
			return diliverymanPaifeiBill;
		}

	}

	private final class DiliverymanPaiFeiOrderRowMapper implements RowMapper<DiliverymanPaifeiOrder> {
		@Override
		public DiliverymanPaifeiOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			DiliverymanPaifeiOrder order = new DiliverymanPaifeiOrder();
			order.setId(rs.getInt("id"));
			order.setOrdernumber(rs.getString("ordernumber"));
			order.setOrderstatus(rs.getInt("orderstatus"));
			order.setOrdertype(rs.getInt("ordertype"));
			order.setTimeofdelivery(rs.getString("timeofdelivery"));
			order.setDeliverytime(rs.getString("deliverytime"));
			order.setPaymentmode(rs.getInt("paymentmode"));
			order.setDateoflodgment(rs.getString("dateoflodgment"));
			order.setPaifeicombined(rs.getBigDecimal("paifeicombined"));
			order.setBasicpaifei(rs.getBigDecimal("basicpaifei"));
			order.setSubsidiesfee(rs.getBigDecimal("subsidiesfee"));
			order.setAreasubsidies(rs.getBigDecimal("areasubsidies"));
			order.setBeyondareasubsidies(rs.getBigDecimal("beyondareasubsidies"));
			order.setBusinesssubsidies(rs.getBigDecimal("businesssubsidies"));
			order.setDelaysubsidies(rs.getBigDecimal("delaysubsidies"));
			return order;
		}

	}

	/**
	 * 查询所有配送员账单
	 *
	 * @param bill
	 * @param page
	 * @return
	 */
	public List<DiliverymanPaifeiBill> queryDiliverymanPaifeiBill(DiliverymanPaifeiBill bill, long page) {
		String sql = "select * from express_ops_diliveryman_paifei_bill where 1=1 ";
		// 条件sql
		String conditionSql = this.queryCondition(sql, bill);
		conditionSql = conditionSql + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER + "";
		return this.jdbcTemplate.query(sql + conditionSql, new DiliverymanPaifeiBillRowMapper());
	}

	/**
	 * 查询时的条件sql
	 *
	 * @param sql
	 * @param bill
	 * @return
	 */
	public String queryCondition(String sql, DiliverymanPaifeiBill bill) {
		StringBuffer querySql = new StringBuffer();
		if (bill.getBillstate() != null) {
			querySql.append(" and billstate ='" + bill.getBillstate() + "'");
		}
		if (StringUtils.isNotBlank(bill.getBillbatch())) {
			querySql.append(" and billbatch = '" + bill.getBillbatch() + "'");
		}
		if (StringUtils.isNotBlank(bill.getBillCreationStartDate())) {
			querySql.append(" and billestablishdate >='" + bill.getBillCreationStartDate() + "'");
		}
		if (StringUtils.isNotBlank(bill.getBillCreationEndDate())) {
			querySql.append(" and billestablishdate <= '" + bill.getBillCreationEndDate() + "'");
		}
		if (StringUtils.isNotBlank(bill.getBillVerificationStrartDate())) {
			querySql.append(" and billverificationdate >= '" + bill.getBillVerificationStrartDate() + "'");
		}
		if (StringUtils.isNotBlank(bill.getBillVerificationEndDate())) {
			querySql.append(" and billverificationdate <= '" + bill.getBillVerificationEndDate() + "'");
		}
		if ((bill.getTheirsite() != null) && (bill.getTheirsite() != 0)) {
			querySql.append(" and theirsite = '" + bill.getTheirsite() + "'");
		}
		if (bill.getOrdertype() != null) {
			querySql.append(" and ordertype = '" + bill.getOrdertype() + "'");
		}
		if (bill.getDiliveryman() != null) {
			querySql.append(" and diliveryman = '" + bill.getDiliveryman() + "'");
		}
		if (StringUtils.isNotBlank(bill.getSortField())) {
			querySql.append(" ORDER BY " + bill.getSortField() + "");
		}
		if (StringUtils.isNotBlank(bill.getOrderingMethod())) {
			querySql.append(" " + bill.getOrderingMethod() + "");
		}else{
			querySql.append(" ORDER BY billbatch desc");
		}
		return querySql.toString();
	}

	/**
	 * 查询出总记录数
	 */
	public int queryDiliverymanPaifeiBillCount(DiliverymanPaifeiBill bill) {
		String sql = "select count(1) from express_ops_diliveryman_paifei_bill where 1=1 ";
		// 条件sql
		String conditionSql = this.queryCondition(sql, bill);
		return this.jdbcTemplate.queryForInt(sql + conditionSql);
	}

	/**
	 * 根据id 查询指定的账单
	 */
	public DiliverymanPaifeiBill queryById(Integer id) {
		String sql = " select * from express_ops_diliveryman_paifei_bill where id = '" + id + "'";
		return this.jdbcTemplate.queryForObject(sql, new DiliverymanPaifeiBillRowMapper());
	}

	/**
	 * 新增账单
	 */

	public Long addDiliverymanBill(final DiliverymanPaifeiBill bill) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_diliveryman_paifei_bill(billstate,billbatch,diliveryman,"
						+ "theirsite,billestablishdate,billverificationdate,ordertype,ordersum,paifeimoney,remarks,daterange,orderids) values(?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				int i = 1;
				ps.setInt(i++, bill.getBillstate());
				ps.setString(i++, bill.getBillbatch());
				ps.setInt(i++, bill.getDiliveryman());
				ps.setInt(i++, bill.getTheirsite());
				ps.setString(i++, bill.getBillestablishdate());
				ps.setString(i++, bill.getBillverificationdate());
				ps.setInt(i++, bill.getOrdertype());
				ps.setInt(i++, bill.getOrdersum());
				ps.setBigDecimal(i++, bill.getPaifeimoney());
				ps.setString(i++, bill.getRemarks());
				ps.setString(i++, bill.getDaterange());
				ps.setString(i++, bill.getOrderids());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 修改账单
	 */
	public void updateDilivermanBill(DiliverymanPaifeiBill bill) {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("update express_ops_diliveryman_paifei_bill set");
		if (bill.getBillstate() != null) {
			updateSql.append(" billstate = '" + bill.getBillstate() + "'");
		}
		if (StringUtils.isNotBlank(bill.getRemarks())) {
			updateSql.append(", remarks= '" + bill.getRemarks() + "'");
		}
		updateSql.append(" where id = '" + bill.getId() + "'");
		this.jdbcTemplate.update(updateSql.toString());
	}

	/**
	 * 删除账单
	 */
	public void deleteBill(Integer id) {
		String sql = " delete from express_ops_diliveryman_paifei_bill where id='" + id + "'";
		this.jdbcTemplate.update(sql);
	}

	/**
	 * 查询指定账单下的订单
	 */
	public List<DiliverymanPaifeiOrder> queryorderdedail(String cwbs, Long page) {
		String sql = "select * from express_ops_diliveryman_paifei_order where ordernumber in (" + cwbs + ")";
		sql += " order by ordernumber";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER + "";
		return this.jdbcTemplate.query(sql, new DiliverymanPaiFeiOrderRowMapper());
	}

	public int queryorderdedailcount(String cwbs) {
		String sql = "select count(1) from express_ops_diliveryman_paifei_order where ordernumber in (" + cwbs + ")";

		return this.jdbcTemplate.queryForInt(sql);
	}
	/**
	 * 根据订单号查询订单明细
	 */
	public List<DiliverymanPaifeiOrder> queryByOrderList(String cwbs){
		String sql = "select * from express_ops_diliveryman_paifei_order where ordernumber in (" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new DiliverymanPaiFeiOrderRowMapper());
	}

	/**
	 * 新增订单
	 */
	public void addOrder(DiliverymanPaifeiOrder order) {
		String sql = "insert into express_ops_diliveryman_paifei_order(ordernumber,orderstatus,ordertype," + "timeofdelivery,deliverytime,paymentmode,dateoflodgment,paifeicombined,basicpaifei,"
				+ "subsidiesfee,areasubsidies,beyondareasubsidies,businesssubsidies,delaysubsidies)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, order.getOrdernumber(), order.getOrderstatus(), order.getOrdertype(), order.getTimeofdelivery(), order.getDeliverytime(), order.getPaymentmode(),
				order.getDateoflodgment(), order.getPaifeicombined(), order.getBasicpaifei(), order.getSubsidiesfee(), order.getAreasubsidies(), order.getBeyondareasubsidies(),
				order.getBusinesssubsidies(), order.getDelaysubsidies());
	}

	/**
	 * 移除订单
	 */
	public void deleteorder(String orderString) {
		String sql = "delete from  express_ops_diliveryman_paifei_order where ordernumber in (" + orderString + ")";
		this.jdbcTemplate.update(sql);
	}

	/**
	 * 修改账单中的订单号
	 */
	public void updateBillForOrder(Integer i, String order, Integer id,BigDecimal sum) {
		String sql = "update express_ops_diliveryman_paifei_bill set orderids='" + order + "', ordersum = '" + i + "', paifeimoney='" +sum+ "' where id = '" + id + "'";
		this.jdbcTemplate.update(sql);
	}
	/**
	 * 修改账单状态
	 */
	public void updateBillState(Integer id,Integer state) {
		String sql = "update express_ops_diliveryman_paifei_bill set billstate = '"+state+"' where id = '"+id+"'";
		this.jdbcTemplate.update(sql);
	}
	
	// 获取最大编号
	public List<DiliverymanPaifeiBill> getMaxNumber() {
		String sql = "select * from express_ops_diliveryman_paifei_bill order by billbatch desc ";
		return this.jdbcTemplate.query(sql,new DiliverymanPaifeiBillRowMapper());
	}
	
}
