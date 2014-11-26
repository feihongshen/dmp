package cn.explink.b2c.efast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.b2c.efast.orders_json.OrderInfo;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.b2cdj.CwbOrderTemp;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

@Component
public class EfastDAO {
	private Logger logger = LoggerFactory.getLogger(EfastService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	private final class CwbTempMapper implements RowMapper<ErpCwbtemp> {
		@Override
		public ErpCwbtemp mapRow(ResultSet rs, int rowNum) throws SQLException {
			ErpCwbtemp cwbOrder = new ErpCwbtemp();
			cwbOrder.setErpid(rs.getLong("erpid"));
			cwbOrder.setAdd_time(rs.getString("add_time"));
			cwbOrder.setCretime(rs.getString("cretime"));
			cwbOrder.setDeal_code(rs.getString("deal_code"));
			cwbOrder.setDelivery_time(rs.getString("delivery_time"));
			cwbOrder.setGetdatatime(rs.getString("getdatatime"));
			cwbOrder.setInvoice_no(rs.getString("invoice_no"));
			cwbOrder.setOrder_sn(rs.getString("order_sn"));
			cwbOrder.setOrder_status(rs.getInt("order_status"));
			cwbOrder.setPage_no(rs.getLong("page_no"));
			cwbOrder.setPage_size(rs.getLong("page_size"));
			cwbOrder.setPay_name(rs.getString("pay_name"));
			cwbOrder.setPay_status(rs.getInt("pay_status"));
			cwbOrder.setShipping_name(rs.getString("shipping_name"));
			cwbOrder.setShipping_status(rs.getInt("shipping_status"));
			cwbOrder.setTotal_results(rs.getLong("total_results"));

			return cwbOrder;
		}
	}

	/**
	 * 查询是否含有合单的情况 运单号相同，订单号不同
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsOrder_sn(String order_sn) {
		String sql = "select count(1)  from ops_erp_temp where  order_sn='" + order_sn + "' ";
		int counts = jdbcTemplate.queryForInt(sql);
		return counts > 0;
	}

	/**
	 * 查询临时表 YIxun反馈
	 * 
	 * @return
	 */
	public List<ErpCwbtemp> getOrderSnList(String erpEnum) {
		String sql = "select * from ops_erp_temp where  getdatatime in (0,2) and erpEnum='" + erpEnum + "' order by getdatatime,cretime limit 0,2000 ";
		List<ErpCwbtemp> cwborderList = jdbcTemplate.query(sql, new CwbTempMapper());
		return cwborderList;
	}

	public void insertErpCwb(final OrderInfo order, final long total, final long page_no, final long page_size, final String erpEnum) {

		jdbcTemplate.update(
				"insert into ops_erp_temp (order_sn,deal_code,order_status,shipping_status,pay_status,shipping_name,pay_name,invoice_no,add_time,delivery_time,total_results,page_no,page_size,cretime,erpEnum) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, order.getOrder_sn());
						ps.setString(2, order.getDeal_code());
						ps.setInt(3, order.getOrder_status());
						ps.setInt(4, order.getShipping_status());
						ps.setInt(5, order.getPay_status());
						ps.setString(6, order.getShipping_name());
						ps.setString(7, order.getPay_name());
						ps.setString(8, order.getInvoice_no());
						ps.setString(9, order.getAdd_time());
						ps.setString(10, order.getDelivery_time());
						ps.setLong(11, total);
						ps.setLong(12, page_no);
						ps.setLong(13, page_size);
						ps.setString(14, DateTimeUtil.getNowTime());
						ps.setString(15, erpEnum);

					}

				});

	}

	/**
	 * 修改中兴云购ERP 临时表为获取成功
	 * 
	 */
	public void updateErpCwbtempById(long erpid, String successtime) {
		try {
			jdbcTemplate.update("update ops_erp_temp set getdatatime=? where erpid=?", successtime, erpid);
		} catch (DataAccessException e) {
			logger.error("修改ops_erp_temp异常,erpid=" + erpid, e);
		}

	}

}
