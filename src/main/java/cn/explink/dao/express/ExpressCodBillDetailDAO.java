package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.express.ExpressCodBillDetail;
import cn.explink.util.StringUtil;

@Repository
public class ExpressCodBillDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressCodBillDetailRowMapper implements RowMapper<ExpressCodBillDetail> {
		@Override
		public ExpressCodBillDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressCodBillDetail expressCodBillDetail = new ExpressCodBillDetail();
			expressCodBillDetail.setId(Long.valueOf(rs.getInt("id")));
			expressCodBillDetail.setOrderNo(StringUtil.nullConvertToEmptyString(rs.getString("order_no")));
			expressCodBillDetail.setGoodNum(rs.getLong("good_num"));
			expressCodBillDetail.setCollectPerson(StringUtil.nullConvertToEmptyString(rs.getString("collect_person")));
			expressCodBillDetail.setDeliveryPerson(StringUtil.nullConvertToEmptyString(rs.getString("delivery_person")));
			expressCodBillDetail.setCod(rs.getBigDecimal("cod"));
			expressCodBillDetail.setDeliveryBranchId(rs.getLong("delivery_branch_id"));
			expressCodBillDetail.setDeliveryBranch(StringUtil.nullConvertToEmptyString(rs.getString("delivery_branch")));
			expressCodBillDetail.setBillId(rs.getLong("bill_id"));
			expressCodBillDetail.setBillNo(StringUtil.nullConvertToEmptyString(rs.getString("bill_no")));
			expressCodBillDetail.setEffectFlag(rs.getInt("effect_flag"));
			expressCodBillDetail.setDismatchReason(StringUtil.nullConvertToEmptyString(rs.getString("dismatch_reason")));
			expressCodBillDetail.setImportPersonId(rs.getLong("import_person_id"));
			expressCodBillDetail.setImportPerson(StringUtil.nullConvertToEmptyString(rs.getString("import_person")));
			expressCodBillDetail.setImportTime(rs.getDate("import_time"));
			return expressCodBillDetail;
		}
	}

	private final class ExpressCodBillDetailIdRowMapper implements RowMapper<Integer> {
		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer integer = new Integer(rs.getInt("id"));
			return integer;
		}

	}

	public Map<String, Object> getOrderByBillId(Long billId, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpressCodBillDetail> list = new ArrayList<ExpressCodBillDetail>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer where = new StringBuffer();

		sql.append("SELECT * FROM express_ops_cod_bill_detail_import");
		countsql.append("SELECT count(id) FROM express_ops_cod_bill_detail_import");
		where.append(" ").append("where effect_flag=1").append(" ").append("and bill_id=?");
		int count = 0;

		// 查询页面数据
		sql.append(where).append(" limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		list = this.jdbcTemplate.query(sql.toString(), new ExpressCodBillDetailRowMapper(), billId);
		count = this.jdbcTemplate.queryForInt(countsql.toString(), billId);
		// 查询数据总量--前面参数已经绑定，所以不需要再次绑定

		map.put("list", list);
		map.put("count", count);
		return map;
	}
}
