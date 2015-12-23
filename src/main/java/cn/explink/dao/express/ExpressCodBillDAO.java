package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.express.ExpressCodBill;
import cn.explink.util.StringUtil;

@Repository
public class ExpressCodBillDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressCodBillRowMapper implements RowMapper<ExpressCodBill> {
		@Override
		public ExpressCodBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressCodBill expressCodBill = new ExpressCodBill();
			expressCodBill.setId(Long.valueOf(rs.getInt("id")));
			expressCodBill.setBillNo(StringUtil.nullConvertToEmptyString(rs.getString("bill_no")));
			expressCodBill.setBillState(rs.getInt("bill_state"));
			expressCodBill.setClosingDate(rs.getDate("closing_date"));
			expressCodBill.setOrderCount(rs.getInt("order_count"));
			expressCodBill.setBillType(rs.getInt("bill_type"));
			expressCodBill.setCod(rs.getBigDecimal("cod"));
			expressCodBill.setCreatorId(Long.valueOf(rs.getInt("creator_id")));
			expressCodBill.setCreateTime(rs.getDate("create_time"));
			expressCodBill.setCreatorName(StringUtil.nullConvertToEmptyString(rs.getString("creator_name")));
			expressCodBill.setAuditorId(Long.valueOf(rs.getInt("auditor_id")));
			expressCodBill.setAuditorName(StringUtil.nullConvertToEmptyString(rs.getString("auditor_name")));
			expressCodBill.setAuditTime(rs.getDate("audit_time"));
			expressCodBill.setCavId(Long.valueOf(rs.getInt("cav_id")));
			expressCodBill.setCavName(StringUtil.nullConvertToEmptyString(rs.getString("cav_name")));
			expressCodBill.setCavTime(rs.getDate("cav_time"));
			expressCodBill.setPayableProvinceId(rs.getInt("payable_province_id"));
			expressCodBill.setPayableProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("payable_province_name")));
			expressCodBill.setReceivableProvinceId(rs.getInt("receivable_province_id"));
			expressCodBill.setReceivableProvinceName(StringUtil.nullConvertToEmptyString(rs.getString("receivable_province_name")));
			expressCodBill.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			return expressCodBill;
		}

	}

	// int deleveryState, int
	// flowOrderType,where.append(" ").append("where cod=1 and deliverystate=? and flowordertype=?");
	/**
	 *
	 * @Title: getCodeBillInfo
	 * @description 根据条件查询express_ops_cod_bill表中的数据
	 * @author 刘武强
	 * @date 2015年8月13日上午11:17:37
	 * @param @param billNo
	 * @param @param billState
	 * @param @param creatStart
	 * @param @param creatEnd
	 * @param @param cavStart
	 * @param @param cavEnd
	 * @param @param provinceId
	 * @param @param sequenceField
	 * @param @param ascOrDesc
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getCodeBillInfo(String billNo, int billState, Date creatStart, Date creatEnd, Date cavStart, Date cavEnd, int provinceId, String sequenceField, String ascOrDesc, int billType, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpressCodBill> list = new ArrayList<ExpressCodBill>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();
		int count = 0;

		sql.append("select * from express_ops_cod_bill");
		countsql.append("select count(id) t from express_ops_cod_bill");
		where.append(" ").append("where 1=1 and bill_type=" + billType);
		if (StringUtils.isNotBlank(billNo)) {
			where.append(" ").append("and bill_no like ?");
			params.add("%" + billNo + "%");
		}
		if (billState != -1) {
			where.append(" ").append("and bill_state=?");
			params.add(billState);
		}
		if ((creatStart != null) && StringUtils.isNotBlank(creatStart.toString())) {
			where.append(" ").append("and create_time>=?");
			params.add(creatStart);
		}
		if ((creatEnd != null) && StringUtils.isNotBlank(creatEnd.toString())) {
			where.append(" ").append("and create_time<=?");
			params.add(creatEnd);
		}
		if ((cavStart != null) && StringUtils.isNotBlank(cavStart.toString())) {
			where.append(" ").append("and cav_time>=?");
			params.add(cavStart);
		}
		if ((cavEnd != null) && StringUtils.isNotBlank(cavEnd.toString())) {
			where.append(" ").append("and cav_time<=?");
			params.add(cavEnd);
		}
		if (provinceId != -1) {
			where.append(" ").append("and payable_province_id=?");
			params.add(provinceId);
		}
		where.append(" ").append("order by ? ?");
		if (StringUtils.isNotBlank(sequenceField)) {
			params.add(sequenceField);
		} else {
			params.add("bill_no");
		}
		if (StringUtils.isNotBlank(ascOrDesc)) {
			params.add(ascOrDesc);
		} else {
			params.add("asc");
		}

		countsql.append(where);
		if (page > 0) {
			where.append(" limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		}
		sql.append(where);

		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = params.get(i);
		}
		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), args, new ExpressCodBillRowMapper());
		// 查询数据总量--前面参数已经绑定，所以不需要再次绑定
		count = this.jdbcTemplate.queryForInt(countsql.toString(), args);
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 *
	 * @Title: getInfobyId
	 * @description 根据id获取数据
	 * @author 刘武强
	 * @date 2015年8月14日下午3:35:27
	 * @param @param id
	 * @param @param state
	 * @param @return
	 * @return ExpressCodBill
	 * @throws
	 */
	public ExpressCodBill getInfobyId(Long id) {
		List<ExpressCodBill> infolist = new ArrayList<ExpressCodBill>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cod_bill where id=" + id);
		infolist = this.jdbcTemplate.query(sql.toString(), new ExpressCodBillRowMapper());
		return infolist.size() > 0 ? infolist.get(0) : null;
	}

}
