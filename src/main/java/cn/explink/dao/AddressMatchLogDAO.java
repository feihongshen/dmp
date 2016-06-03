package cn.explink.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.domain.AddressMatchLog;

/**
 * 地址匹配日志表DAO
 * @author neo01.huang
 * 2016-4-12
 */
@Component
public class AddressMatchLogDAO {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	/**
	 * 批量insert
	 * @param logList
	 * @author neo01.huang
	 */
	public void insertBatch(final List<AddressMatchLog> logList) {
		if (logList == null || logList.size() == 0) {
			logger.info("insertBatch->logList is null or empty");
			return;
		}
		StringBuffer sql = new StringBuffer("INSERT INTO `express_ops_address_match_log` ( `itemno`, `address`, `match_status`, `match_msg`, `create_time`) VALUES ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		for (int i = 0, len = logList.size(); i < len; i++) {
			
			String paramSample = String.format("(:itemno%1$s, :address%1$s, :matchStatus%1$s, :matchMsg%1$s, :createTime%1$s)", i);
			
			if (i == 0) {
				sql.append(paramSample);
				
			} else {
				sql.append(",").append(paramSample);
				
			}
			
			AddressMatchLog log = logList.get(i);
			paramMap.put("itemno" + i, log.getItemno());
			paramMap.put("address" + i, log.getAddress());
			paramMap.put("matchStatus" + i, log.getMatchStatus());
			paramMap.put("matchMsg" + i, log.getMatchMsg());
			paramMap.put("createTime" + i, log.getCreateTime());
		}
		//logger.info("sql==>" + sql);
		jdbcTemplate.update(sql.toString(), paramMap);
	}
	
}
