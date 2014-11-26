package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.DeliveryPercentForEmaildateDTO;
import cn.explink.domain.DeliveryPercent;
import cn.explink.enumutil.DeliveryPercentTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 日志记录明细DAO 目前用于妥投率
 * 
 * @author jumu
 *
 */
@Component
public class DeliveryPercentDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class DeliveryPercentMapper implements RowMapper<DeliveryPercent> {
		@Override
		public DeliveryPercent mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryPercent dp = new DeliveryPercent();
			dp.setId(rs.getLong("id"));
			dp.setCwb(rs.getString("cwb"));
			dp.setBranchid(rs.getLong("branchid"));
			dp.setCustomerid(rs.getLong("customerid"));
			dp.setType(rs.getInt("type"));
			dp.setCredate(rs.getString("credate"));
			return dp;
		}
	}

	private final class CwbToJsonMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("num", rs.getString("num"));
			obj.put("branchid", rs.getBigDecimal("branchid"));
			obj.put("customerid", rs.getBigDecimal("customerid"));
			return obj;
		}
	}

	/**
	 * 获取某环节的订单的统计数，并用当前站点与供货商进行分组，所以分组只对库存的订单有效
	 * 
	 * @param flowOrderType
	 *            当前环节
	 * @param today
	 *            大于等于这个时间
	 * @return 用当前站点与供货商进行分组的JSONObject的List
	 */
	public List<JSONObject> getCwbByFlowOrderTypeAndTodayForCount(FlowOrderTypeEnum flowOrderType, String toDay) {
		return jdbcTemplate.query("SELECT COUNT(DISTINCT of.cwb) AS num , of.branchid AS branchid,cd.customerid  "
				+ "FROM express_ops_order_flow of LEFT JOIN  express_ops_cwb_detail cd ON of.cwb=cd.cwb " + "WHERE of.flowordertype=? AND of.credate>=? AND cd.state=1 "
				+ "GROUP BY of.branchid,cd.customerid ", new CwbToJsonMapper(), flowOrderType.getValue(), toDay);
	}

	/**
	 * 获得今天的领货记录
	 * 
	 * @param toDay
	 *            大于等于这个时间
	 * @return
	 */
	public List<JSONObject> getCwbByTodayForCountJsonGroupDeliverbranchid(String toDay) {
		return jdbcTemplate.query("SELECT COUNT(1) AS num , cd.deliverybranchid AS branchid,cd.customerid "
				+ "FROM express_ops_delivery_state ds LEFT JOIN  express_ops_cwb_detail cd ON ds.cwb=cd.cwb " + "WHERE ds.createtime>=? AND ds.state=1 AND cd.state=1 "
				+ "GROUP BY cd.deliverybranchid,cd.customerid ", new CwbToJsonMapper(), toDay);
	}

	/**
	 * 获取妥投订单
	 * 
	 * @return
	 */
	public List<JSONObject> getCwbByTodaySuccessForCountJsonGroupDeliverbranchid(String toDay) {
		return jdbcTemplate.query("SELECT COUNT(1) AS num , cd.deliverybranchid AS branchid,cd.customerid "
				+ "FROM express_ops_delivery_state ds LEFT JOIN  express_ops_cwb_detail cd ON ds.cwb=cd.cwb "
				+ "WHERE ds.deliverytime>=? AND ds.deliverystate>0 AND ds.deliverystate<4 AND ds.state=1 AND cd.state=1 " + "GROUP BY cd.deliverybranchid,cd.customerid", new CwbToJsonMapper(), toDay);
	}

	/**
	 * 获得昨日遗留订单明细
	 * 
	 * @param flowOrderType
	 *            当前环节
	 * @return
	 */
	public List<JSONObject> getCwbByFlowOrderTypeForCountJson(DeliveryPercentTypeEnum deliverypercenttype, String credate) {
		return jdbcTemplate.query("SELECT COUNT(1) AS num , branchid,customerid FROM express_ops_delivery_percent " + "WHERE `type`=? AND credate=? GROUP BY branchid,customerid ",
				new CwbToJsonMapper(), deliverypercenttype.getValue(), credate);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void cre(String cwb, long branchid, long customerid, DeliveryPercentTypeEnum daozhanweiling, String toDay) {
		jdbcTemplate.update("INSERT INTO express_ops_delivery_percent (cwb,branchid,customerid,type,credate) VALUES (?,?,?,?,?)", cwb, branchid, customerid, daozhanweiling.getValue(), toDay);
	}

	public List<DeliveryPercent> getDeliveryPercentEnd() {
		return jdbcTemplate.query("select * from express_ops_delivery_percent order by credate desc limit 0,1 ", new DeliveryPercentMapper());
	}

	private final class DeliveryPercentForEmaildateDTOMapper implements RowMapper<DeliveryPercentForEmaildateDTO> {
		@Override
		public DeliveryPercentForEmaildateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryPercentForEmaildateDTO deliveryPercentForEmaildateDTO = new DeliveryPercentForEmaildateDTO();
			deliveryPercentForEmaildateDTO.setCwb(rs.getString("cwb"));
			deliveryPercentForEmaildateDTO.setEmaildateid(rs.getLong("emaildateid"));
			deliveryPercentForEmaildateDTO.setDeliverytime(rs.getDate("deliverytime"));
			deliveryPercentForEmaildateDTO.setStarttime(rs.getDate("credate"));
			return deliveryPercentForEmaildateDTO;
		}
	}

	private final class DeliveryPercentForEmaildateCustomeridDTOMapper implements RowMapper<DeliveryPercentForEmaildateDTO> {
		@Override
		public DeliveryPercentForEmaildateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryPercentForEmaildateDTO deliveryPercentForEmaildateDTO = new DeliveryPercentForEmaildateDTO();
			deliveryPercentForEmaildateDTO.setCwb(rs.getString("cwb"));
			deliveryPercentForEmaildateDTO.setEmaildateid(rs.getLong("emaildateid"));
			deliveryPercentForEmaildateDTO.setDeliverytime(rs.getDate("deliverytime"));
			deliveryPercentForEmaildateDTO.setStarttime(rs.getDate("credate"));
			deliveryPercentForEmaildateDTO.setCustomerid(rs.getLong("customerid"));
			return deliveryPercentForEmaildateDTO;
		}
	}

	/**
	 * 按照发货批次（emaildateid）获取投递率基础数据
	 * 
	 * @param emaildateid
	 *            批次id
	 * @param flowordertype
	 *            按照什么条件计算
	 * @return
	 */
	public List<DeliveryPercentForEmaildateDTO> getDeliveryPercentForEmaildate(String emaildateids, int flowordertype) {
		List<DeliveryPercentForEmaildateDTO> dpfeDTOList = jdbcTemplate.query("SELECT cd.cwb,cd.emaildateid,ds.deliverytime,of.credate FROM `express_ops_cwb_detail` cd "
				+ "LEFT JOIN `express_ops_delivery_state` ds ON cd.`cwb`=ds.`cwb` " + "LEFT JOIN `express_ops_order_flow` of ON cd.`cwb`=of.`cwb` " + "WHERE cd.emaildateid in(" + emaildateids
				+ ") AND cd.state=1 AND ds.`state`=1 " + "AND ds.`deliverystate`>0 AND ds.`deliverystate`<4 AND ds.gcaid>0 " + "AND of.flowordertype=? ORDER BY of.credate ASC ",
				new DeliveryPercentForEmaildateDTOMapper(), flowordertype);
		// 虑重逻辑
		StringBuffer cwbs = new StringBuffer();
		String separtor = "#";
		List<DeliveryPercentForEmaildateDTO> reDpfeDTOList = new ArrayList<DeliveryPercentForEmaildateDTO>();
		for (DeliveryPercentForEmaildateDTO dpfe : dpfeDTOList) {
			if (cwbs.indexOf(dpfe.getCwb() + separtor) == -1) {
				reDpfeDTOList.add(dpfe);
				cwbs.append(dpfe.getCwb() + separtor);
			}
		}

		return reDpfeDTOList;
	}

	public List<DeliveryPercentForEmaildateDTO> getDeliveryPercentForEmaildate(String customerids, String beginemaildate, String endemaildate, int flowordertype) {
		List<DeliveryPercentForEmaildateDTO> dpfeDTOList = jdbcTemplate.query("SELECT cd.customerid,cd.cwb,cd.emaildateid,ds.deliverytime,of.credate FROM `express_ops_cwb_detail` cd "
				+ "LEFT JOIN `express_ops_delivery_state` ds ON cd.`cwb`=ds.`cwb` " + "LEFT JOIN `express_ops_order_flow` of ON cd.`cwb`=of.`cwb` " + "WHERE cd.emaildate>='" + beginemaildate
				+ "' AND cd.emaildate<='" + endemaildate + "'  AND cd.state=1 AND ds.`state`=1 " + "AND ds.`deliverystate`>0 AND ds.`deliverystate`<4 AND ds.gcaid>0 "
				+ "AND of.flowordertype=? ORDER BY of.credate ASC ", new DeliveryPercentForEmaildateCustomeridDTOMapper(), flowordertype);
		// 虑重逻辑
		StringBuffer cwbs = new StringBuffer();
		String separtor = "#";
		List<DeliveryPercentForEmaildateDTO> reDpfeDTOList = new ArrayList<DeliveryPercentForEmaildateDTO>();
		for (DeliveryPercentForEmaildateDTO dpfe : dpfeDTOList) {
			if (cwbs.indexOf(dpfe.getCwb() + separtor) == -1) {
				reDpfeDTOList.add(dpfe);
				cwbs.append(dpfe.getCwb() + separtor);
			}
		}

		return reDpfeDTOList;
	}

}
