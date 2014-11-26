package cn.explink.b2c.huitongtx;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.dao.OrderFlowDAO;
import cn.explink.util.DateTimeUtil;

@Component
public class HuitongtxDAO {
	private Logger logger = LoggerFactory.getLogger(HuitongtxService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	// 查询是否有相同的notify_id（tmall判断重复是跟踪notify_id判断的）
	public boolean isRepeatNotify_id(String customer_id, String notify_id) {
		int counts = 0;
		if (notify_id != null && !"".equals(notify_id)) {
			String sql = "insert into express_ops_cwb_detail_b2ctemp  where  tmall_notify_id=? ";
			counts = jdbcTemplate.queryForInt(sql, notify_id);
		}
		return counts > 0;
	}

	/**
	 * 
	 * 运单号相同，订单号不同,未入库，并且未超过24小时。
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsOver24HCwbInfo(String cwb, String customer_id) {
		String nowtime = DateTimeUtil.getNowTime();
		String sql = "select credate  from express_ops_cwb_detail_b2ctemp where state=1 and cwb='" + cwb + "' and flowordertype=1  ";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException e) {
			logger.info("订单号为：{}在数据库中不存在，不需要合单！{}", cwb);
			return false;
		}
		String cretime = "";
		if (map != null && map.size() > 0) {
			cretime = map.get("credate").toString();
		}
		double diffHours = DateTimeUtil.getHourFromToTime(cretime, nowtime);

		return diffHours > 24;
	}

	/**
	 * 查询是否含有合单的情况 运单号相同，订单号不同
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsTogetherCwbInfo(String cwb, String customer_id) {
		boolean flag = false;

		String nowtime = DateTimeUtil.getNowTime();
		String sql = "select count(1)  from express_ops_cwb_detail where state=1 and cwb='" + cwb + "'   ";
		// +" and  (TIMEDIFF('"+nowtime+"',credate)<24)>0 ";
		int counts = jdbcTemplate.queryForInt(sql);
		if (counts > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 合单修改multi_shipcwb
	 * 
	 */
	public boolean update_CwbTogetherInfo(String cwb, String shipcwb, String transcwb) {
		boolean flag = false;
		// 追加
		if (!"".equals(shipcwb)) {
			int update = 0;
			try {
				String sql = "update express_ops_cwb_detail set  multi_shipcwb=CONCAT(IF(multi_shipcwb IS NULL,'',multi_shipcwb),'," + shipcwb
						+ "'), transcwb=CONCAT(IF(transcwb IS NULL,'',transcwb),'," + transcwb + "') where cwb='" + cwb + "' ";
				update = jdbcTemplate.update(sql);
				logger.info("tmall订单[一件多票]追加成功，存储系统中为普通单子，sql={}", sql);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				logger.info("tmall订单[一件多票]追加失败");
				e.printStackTrace();
			}
			if (update > 0) {
				flag = true;
			}
		}
		return flag;
	}

}
