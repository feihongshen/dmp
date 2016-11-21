package cn.explink.dao.fnc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

/**
* @ClassName: SignStationReportDao 
* @Description: 余额报表的dao 
* @author 刘武强
* @date 2016年11月8日 下午2:45:24 
*
 */
@Repository
public class FnStationSignOrderDetailsSnapshotExpressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Logger logger = LoggerFactory.getLogger(FnStationSignOrderDetailsSnapshotExpressDao.class);
	
	/**
	* @Title: getReportIdByCwbAndReportdate 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param customerid
	* @param @param typeid
	* @param @param reportdate
	* @param @return    设定文件 
	* @return long    返回类型 
	* @throws 
	* @date 2016年11月8日 下午2:47:58 
	* @author 刘武强
	 */
	@DataSource(DatabaseType.REPLICA)
	public long getReportIdByCwbAndReportdate(String cwb, int reportdate) {
		String sql = "select count(1) from fn_station_sign_order_details_snapshot_express where cwb = ? and reportdate = ? ";
		return this.jdbcTemplate.queryForLong(sql, cwb, reportdate);
	}
	
	@DataSource(DatabaseType.REPLICA)
	public long getOrderCountByCwb(String cwb) {
		String sql = "select count(1) from fn_station_sign_order_details_snapshot_express where cwb = ?";
		return this.jdbcTemplate.queryForLong(sql, cwb);
	}
	
}
