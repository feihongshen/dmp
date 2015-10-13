package cn.explink.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.DeliveryRateCondition;

@Repository("deliveryRateConditionDAO")
public class DeliveryRateConditionDAO extends BasicJdbcTemplateDaoSupport<DeliveryRateCondition, Long> {

	public DeliveryRateConditionDAO() {
		super(DeliveryRateCondition.class);
	}

	public List<DeliveryRateCondition> listConditionByUser(Long userId, String name, Integer selectType) {
		StringBuilder sql = new StringBuilder("select * from DELIVERY_RATE_CONDITIONS WHERE 1 = 1");
		List<Object> params = new ArrayList<Object>();
		if (userId != null) {
			sql.append(" and USER_ID = ?");
			params.add(userId);
		}
		if (name != null && !name.trim().isEmpty()) {
			sql.append(" and NAME like ?");
			params.add("%" + name + "%");
		}
		if (selectType != null) {
			sql.append(" and SELECT_TYPE = ?");
			params.add(selectType);
		}
		return getJdbcTemplate().query(sql.toString(), params.toArray(), createRowMapper());
	}

	public void updateStatus(Long id, Integer status) {
		String sql = "update DELIVERY_RATE_CONDITIONS set status = ? where id = ?";
		getJdbcTemplate().update(sql, status, id);
	}

}
