package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderPartGoodsRt;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

@Component
public class OrderPartGoodsRtDAO {

	private final class OrderPartGoodsRtRowMapper implements RowMapper<OrderPartGoodsRt> {
		@Override
		public OrderPartGoodsRt mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderPartGoodsRt ordergoodsrt = new OrderPartGoodsRt();
			ordergoodsrt.setCwb(rs.getString("cwb"));
			ordergoodsrt.setConsigneename(rs.getString("consigneename"));
			ordergoodsrt.setConsigneeaddress(rs.getString("consigneeaddress"));
			ordergoodsrt.setReceivablefee(rs.getBigDecimal("receivablefee"));
			ordergoodsrt.setCustomer(rs.getString("customer"));
			ordergoodsrt.setCreatetime(rs.getString("createtime"));
			ordergoodsrt.setCollectiontime(rs.getString("collectiontime"));
			ordergoodsrt.setRtwarehouseid(rs.getLong("rtwarehouseid"));
			ordergoodsrt.setRtwarehouseaddress(rs.getString("rtwarehouseaddress"));
			return ordergoodsrt;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取上门退未返馈订单，如果小件员或供货商为空，则取该站所有小件员和供货商
	 *
	 * @param page
	 * @param userid
	 * @param customerid
	 * @param userids
	 * @param customerids
	 * @return
	 */
	public List<OrderPartGoodsRt> getOrderPartGoodsRtList(long page, long userid, long customerid, String userids, String customerids, long deliverybranchid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cd.cwb, cd.consigneename, cd.consigneeaddress, cd.receivablefee, ds.createtime, '' customer, '' collectiontime, 0 rtwarehouseid, '' rtwarehouseaddress ");
		sql.append(" FROM express_ops_cwb_detail cd, express_ops_delivery_state ds ");
		sql.append(" WHERE cd.cwb = ds.cwb ");
		sql.append(" AND cd.cwbordertypeid =  " + CwbOrderTypeIdEnum.Shangmentui.getValue());
		// sql.append(" AND ds.deliverystate = ? ");
		sql.append(" AND ds.gcaid=0 ");
		sql.append(" AND ds.deliverybranchid =" + deliverybranchid);
		sql.append(" AND cd.state=1 AND ds.state=1 ");
		if (userid != -1) {
			sql.append(" AND ds.deliveryid = " + userid);
		} else {
			if ((userids != null) && (userids.trim().length() > 0)) {
				sql.append(" AND ds.deliveryid IN (" + userids).append(")");
			} else {
				return null;
			}
		}
		if (customerid != -1) {
			sql.append(" AND cd.customerid = " + customerid);
		} else {
			if ((customerids != null) && (customerids.trim().length() > 0)) {
				sql.append(" AND cd.customerid IN (" + customerids).append(")");
			} else {
				return null;
			}
		}
		// sql.append(" ORDER BY ds.createtime ASC LIMIT "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER);
		sql.append(" ORDER BY ds.createtime ASC ");
		// return this.jdbcTemplate.query(sql.toString(), new
		// OrderPartGoodsRtRowMapper(),
		// CwbOrderTypeIdEnum.Shangmentui.getValue(),
		// DeliveryStateEnum.WeiFanKui.getValue());
		return this.jdbcTemplate.query(sql.toString(), new OrderPartGoodsRtRowMapper());
	}

	/**
	 * 按订单号获取上门退未返馈订单，取该站所有小件员和规定供货商
	 *
	 * @param page
	 * @param userid
	 * @param customerid
	 * @param userids
	 * @param customerids
	 * @return
	 */
	public List<OrderPartGoodsRt> getOrderPartGoodsRtBycwb(String cwbs, String userids, String customerids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cd.cwb, cd.consigneename, cd.consigneeaddress, cd.receivablefee, ds.createtime, '' customer, '' collectiontime, 0 rtwarehouseid, cd.remark5 rtwarehouseaddress ");
		sql.append(" FROM `express_ops_cwb_detail` cd, express_ops_delivery_state ds ");
		sql.append(" WHERE cd.cwb = ds.cwb ");
		sql.append(" AND cd.cwbordertypeid =  " + CwbOrderTypeIdEnum.Shangmentui.getValue());
		// sql.append(" AND ds.deliverystate = ? ");
		sql.append(" AND ds.cwb IN(" + cwbs).append(")");
		sql.append(" AND ds.gcaid=0 ");
		if ((userids != null) && (userids.trim().length() > 0)) {
			sql.append(" AND ds.deliveryid IN (" + userids).append(")");
		} else {
			return null;
		}
		if ((customerids != null) && (customerids.trim().length() > 0)) {
			sql.append(" AND cd.customerid IN (" + customerids).append(")");
		} else {
			return null;
		}
		// return this.jdbcTemplate.query(sql.toString(), new
		// OrderPartGoodsRtRowMapper(),
		// CwbOrderTypeIdEnum.Shangmentui.getValue(),
		// DeliveryStateEnum.WeiFanKui.getValue());
		return this.jdbcTemplate.query(sql.toString(), new OrderPartGoodsRtRowMapper());
	}

	/**
	 * 获取查询订单总量，用于分页
	 *
	 * @param userid
	 * @param customerid
	 * @param userids
	 * @param customerids
	 * @return
	 */
	public long getOrderPartGoodsRtCount(long userid, long customerid, String userids, String customerids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(1) ");
		sql.append(" FROM express_ops_cwb_detail cd, express_ops_delivery_state ds ");
		sql.append(" WHERE cd.cwb = ds.cwb ");
		sql.append(" AND cd.cwbordertypeid =  " + CwbOrderTypeIdEnum.Shangmentui.getValue());
		// sql.append(" AND ds.deliverystate = ? ");
		sql.append(" AND ds.gcaid=0 ");
		if (userid != -1) {
			sql.append(" AND ds.deliveryid = " + userid);
		} else {
			if ((userids != null) && (userids.trim().length() > 0)) {
				sql.append(" AND ds.deliveryid IN (" + userids).append(")");
			} else {
				return 0;
			}
		}
		if (customerid != -1) {
			sql.append(" AND cd.customerid = " + customerid);
		} else {
			if ((customerids != null) && (customerids.trim().length() > 0)) {
				sql.append(" AND cd.customerid IN (" + customerids).append(")");
			} else {
				return 0;
			}
		}
		// return this.jdbcTemplate.queryForLong(sql.toString(),
		// CwbOrderTypeIdEnum.Shangmentui.getValue(),
		// DeliveryStateEnum.WeiFanKui.getValue());
		return this.jdbcTemplate.queryForLong(sql.toString());
	}
}
