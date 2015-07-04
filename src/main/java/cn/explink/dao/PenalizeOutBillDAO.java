/**
 *
 */
package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.penalizeOutBill;
import cn.explink.util.Page;

/**
 * @author wangqiang
 */
@Component
public class PenalizeOutBillDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PenalizeOutBillRowMapper implements RowMapper<penalizeOutBill> {
		@Override
		public penalizeOutBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			penalizeOutBill bill = new penalizeOutBill();
			bill.setId(rs.getInt("id"));
			bill.setBillbatches(rs.getString("billbatches"));
			bill.setBillstate(rs.getInt("billstate"));
			bill.setCustomerid(rs.getString("customerid"));
			bill.setCustomername(rs.getString("customername"));
			bill.setCompensatebig(rs.getInt("compensatebig"));
			bill.setCompensatesmall(rs.getInt("compensatesmall"));
			bill.setCompensatefee(rs.getBigDecimal("compensatefee"));
			bill.setCompensateexplain(rs.getString("compensateexplain"));
			bill.setCompensateodd(rs.getString("compensateodd"));
			bill.setFounder(rs.getLong("founder"));
			bill.setCreateddate(rs.getString("createddate"));
			bill.setVerifier(rs.getInt("verifier"));
			bill.setChecktime(rs.getString("checktime"));
			bill.setVerificationperson(rs.getInt("verificationperson"));
			bill.setVerificationdate(rs.getString("verificationdate"));
			return bill;
		}

	}

	/**
	 * 查询所有账单
	 */
	public List<penalizeOutBill> queryAll(penalizeOutBill bill, String billCreationStartDate, String billCreationEndDate, String billVerificationStrartDate, String billVerificationEndDate,
			String sort, String method, long page) {
		StringBuffer querySql = new StringBuffer();
		querySql.append("select * from express_ops_penalize_out_bill where 1=1");
		if (bill != null) {
			if ((bill.getBillbatches() != null) && (bill.getBillbatches() != "")) {
				querySql.append(" and billbatches like '%" + bill.getBillbatches() + "%'");
			}
			if (bill.getBillstate() != null) {
				querySql.append(" and billstate ='" + bill.getBillstate() + "'");
			}
			if ((billCreationStartDate != null) && (billCreationStartDate != "") && (billCreationEndDate != null) && (billCreationEndDate != "")) {
				querySql.append(" and '" + billCreationStartDate + "'< createddate");
				querySql.append(" and createddate < '" + billCreationEndDate + "'");
			}
			if ((billVerificationStrartDate != null) && (billVerificationStrartDate != "") && (billVerificationEndDate != null) && (billVerificationEndDate != "")) {
				querySql.append(" and '" + billVerificationStrartDate + "' < verificationdate");
				querySql.append(" and verificationdate < '" + billVerificationEndDate + "'");
			}
			if ((bill.getCustomerid() != null) && (bill.getCustomerid() != "")) {
				querySql.append(" and customerid = '" + bill.getCustomerid() + "'");
			}
			if (bill.getCompensatebig() != null) {
				querySql.append(" and compensatebig = '" + bill.getCompensatebig() + "'");
			}
			if ((sort != null) && (sort != "") && (method != null) && (method != "")) {

				querySql.append(" order by " + sort + "  " + method);
			}
			querySql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		}
		return this.jdbcTemplate.query(querySql.toString(), new PenalizeOutBillRowMapper());
	}

	public int queryAllCount(penalizeOutBill bill, String billCreationStartDate, String billCreationEndDate, String billVerificationStrartDate, String billVerificationEndDate, String sort,
			String method, long page) {
		StringBuffer querySql = new StringBuffer();
		querySql.append("select count(id) from express_ops_penalize_out_bill where 1=1");
		if (bill != null) {
			if ((bill.getBillbatches() != null) && (bill.getBillbatches() != "")) {
				querySql.append(" and billbatches like '%" + bill.getBillbatches() + "%'");
			}
			if (bill.getBillstate() != null) {
				querySql.append(" and billstate ='" + bill.getBillstate() + "'");
			}
			if ((billCreationStartDate != null) && (billCreationStartDate != "") && (billCreationEndDate != null) && (billCreationEndDate != "")) {
				querySql.append(" and '" + billCreationStartDate + "'< createddate");
				querySql.append(" and createddate < '" + billCreationEndDate + "'");
			}
			if ((billVerificationStrartDate != null) && (billVerificationStrartDate != "") && (billVerificationEndDate != null) && (billVerificationEndDate != "")) {
				querySql.append(" and '" + billVerificationStrartDate + "' < verificationdate");
				querySql.append(" and verificationdate < '" + billVerificationEndDate + "'");
			}
			if ((bill.getCustomerid() != null) && (bill.getCustomerid() != "")) {
				querySql.append(" and customerid = '" + bill.getCustomerid() + "'");
			}
			if (bill.getCompensatebig() != null) {
				querySql.append(" and compensatebig = '" + bill.getCompensatebig() + "'");
			}
			if ((sort != null) && (sort != "") && (method != null) && (method != "")) {

				querySql.append(" order by " + sort + "  " + method);
			}

		}
		return this.jdbcTemplate.queryForInt(querySql.toString());
	}

	/**
	 * 新增账单信息
	 */
	public void addPenalizeOutBill(penalizeOutBill bill) {
		String sql = "insert into express_ops_penalize_out_bill(billstate,billbatches,customerid,customername,compensateodd,compensatebig,compensatesmall,compensatefee,compensateexplain,founder,createddate) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, bill.getBillstate(), bill.getBillbatches(), bill.getCustomerid(), bill.getCustomername(), bill.getCompensateodd(), bill.getCompensatebig(),
				bill.getCompensatesmall(), bill.getCompensatefee(), bill.getCompensateexplain(), bill.getFounder(), bill.getCreateddate());
	}

	// 获取最大编号
	public List<penalizeOutBill> getMaxNumber() {
		String sql = "select * from express_ops_penalize_out_bill order by billbatches desc ";
		return this.jdbcTemplate.query(sql, new PenalizeOutBillRowMapper());
	}

	// 通过id查询指定赔付账单明细
	public penalizeOutBill queryById(Integer id) {
		String sql = "select * from express_ops_penalize_out_bill where id = '" + id + "'";
		return this.jdbcTemplate.queryForObject(sql, new PenalizeOutBillRowMapper());
	}

	/**
	 * 修改指定账单信息
	 */
	public void penalizeOutBillUpdate(penalizeOutBill bill) {
		StringBuffer updatesql = new StringBuffer("update express_ops_penalize_out_bill set");
		final List<Object> param = new ArrayList<Object>();
		if (bill.getBillstate() != null) {
			updatesql.append(" billstate = ?");
			param.add(bill.getBillstate());
		}
		if ((bill.getCompensateexplain() != null) && (bill.getCompensateexplain() != "")) {
			updatesql.append(", compensateexplain = ?");
			param.add(bill.getCompensateexplain());
		}
		if ((bill.getCompensateodd() != null) && (bill.getCompensateodd() != "")) {
			updatesql.append(", compensateodd = ?");
			param.add(bill.getCompensateodd());
		}
		if ((bill.getChecktime() != null) && (bill.getChecktime() != "")) {
			updatesql.append(", checktime = ?");
			param.add(bill.getChecktime());
		} else {
			updatesql.append(", checktime= null");
		}
		if ((bill.getVerificationdate() != null) && (bill.getVerificationdate() != "")) {
			updatesql.append(", verificationdate= ?");
			param.add(bill.getVerificationdate());
		} else {
			updatesql.append(", verificationdate= null");
		}
		updatesql.append(" where id=" + bill.getId());
		this.jdbcTemplate.update(updatesql.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < param.size(); i++) {
					ps.setObject(i + 1, param.get(i));
				}
			}
		});
	}

	/**
	 * 添加指定账单的单号
	 */
	public void addpenalizeOutDetail(Integer id, String compensateodd) {
		String sql = "update express_ops_penalize_out_bill set compensateodd='" + compensateodd + "' where id='" + id + "'";
		this.jdbcTemplate.update(sql);
	}

	/**
	 * 删除指定账单
	 *
	 */
	public void deletePenalizeOutBill(Integer id) {
		String sql = "delete from express_ops_penalize_out_bill where id='" + id + "'";
		this.jdbcTemplate.update(sql);
	}
}
