package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.DeliveryCash;
import cn.explink.enumutil.DeliveryStateEnum;

@Component
public class DeliveryCashDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class DeliveryCashRowMapper implements RowMapper<DeliveryCash> {
		@Override
		public DeliveryCash mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryCash deliveryCash = new DeliveryCash();
			deliveryCash.setDeliverystateid(rs.getLong("deliverystateid"));
			deliveryCash.setFankuitime(rs.getString("fankuitime"));
			deliveryCash.setGuibantime(rs.getString("guibantime"));
			deliveryCash.setLinghuotime(rs.getString("linghuotime"));
			deliveryCash.setPaybackfee(rs.getBigDecimal("paybackfee"));
			deliveryCash.setReceivableNoPosfee(rs.getBigDecimal("receivableNoPosfee"));
			deliveryCash.setReceivablePosfee(rs.getBigDecimal("receivablePosfee"));
			deliveryCash.setId(rs.getLong("id"));
			deliveryCash.setCwb(rs.getString("cwb"));
			deliveryCash.setDeliverid(rs.getLong("deliverid"));
			deliveryCash.setDeliverystate(rs.getLong("deliverystate"));
			deliveryCash.setGcaid(rs.getLong("gcaid"));
			deliveryCash.setDeliverybranchid(rs.getLong("deliverybranchid"));
			deliveryCash.setCustomerid(rs.getLong("customerid"));
			return deliveryCash;
		}

	}

	/**
	 * 产生一条记录
	 * 
	 * @param cwb
	 * @param deliveryid
	 * @param deliverybranchid
	 * @param customerid
	 * @param linghuotime
	 */
	public void creDeliveryCash(String cwb, long deliverid, long deliverybranchid, long customerid, String linghuotime, long deliverystateid, BigDecimal receivablefee) {
		String sql = "insert into express_ops_deliver_cash(cwb,deliverid,deliverybranchid,customerid,linghuotime,deliverystateid,receivableNoPosfee) values(?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, cwb, deliverid, deliverybranchid, customerid, linghuotime, deliverystateid, receivablefee);
	}

	// 领货时变更了领货小件员要同时更改deliverycash表中的关于小件员的信息
	public void saveDeliveryCashByDeliverystateid(long deliverid, long deliverybranchid, String linghuotime, long deliverystateid) {
		String sql = "update express_ops_deliver_cash set deliverid=?,deliverybranchid=?,linghuotime=? where deliverystateid=?";
		jdbcTemplate.update(sql, deliverid, deliverybranchid, linghuotime, deliverystateid);
	}

	/**
	 * 失效express_ops_deliver_cash订单
	 * 
	 * @param cwb
	 */
	public void updateDeliveryCashStateBycwb(String cwb) {
		String sql = "update express_ops_deliver_cash set state=0 where cwb=?";
		jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 反馈的时候更新数据
	 * 
	 * @param deliverystateid
	 * @param fankuitime
	 * @param paybackfee
	 * @param receivableNoPosfee
	 * @param receivablePosfee
	 * @param deliverystate
	 * @param id
	 */
	public void saveDeliveryCashForDSById(String fankuitime, BigDecimal paybackfee, BigDecimal receivableNoPosfee, BigDecimal receivablePosfee, long deliverystate, long deliverystateid) {
		String sql = "update express_ops_deliver_cash set fankuitime=?,paybackfee=?,receivableNoPosfee=?,receivablePosfee=?,deliverystate=? where deliverystateid=?";
		jdbcTemplate.update(sql, fankuitime, paybackfee, receivableNoPosfee, receivablePosfee, deliverystate, deliverystateid);
	}

	/**
	 * 归班的时候更新数据
	 * 
	 * @param guibantime
	 * @param gcaid
	 * @param id
	 */
	public void saveDeliveryCashForGBById(String guibantime, long gcaid, long deliverystateid) {
		String sql = "update express_ops_deliver_cash set guibantime=?,gcaid=? where deliverystateid=?";
		;
		jdbcTemplate.update(sql, guibantime, gcaid, deliverystateid);
	}

	// ==============================修改订单使用的方法 start
	// ==================================
	/**
	 * 重置审核状态修改小件员工作量统计表字段
	 * 
	 * @param cwb
	 *            站点交款记录id
	 * @param amount
	 *            站点交款审核POS金额 减去重置审核状态订单的非POS金额 的值
	 * @param updateTime
	 *            修改订单时间
	 */
	public void updateForChongZhiShenHe(String cwb, Long gcaid) {
		String sql = "update express_ops_deliver_cash set fankuitime='',paybackfee=0" + ",receivableNoPosfee=0,receivablePosfee=0,deliverystate=?,guibantime=''" + ",gcaid=0 where cwb=? and gcaid=?";
		jdbcTemplate.update(sql, DeliveryStateEnum.WeiFanKui.getValue(), cwb, gcaid);
	}

	/**
	 * 修改订单金额 修改小件员工作量统计表表字段
	 * 
	 * @param cwb
	 *            订单好 更改条件
	 * @param delivertState
	 *            当前反馈状态 必须大于0才会更新这条记录
	 * @param receivablefee
	 *            代收金额 对应实收
	 * @param pos
	 *            pos实收
	 * @param paybackfee
	 *            代退金额 对应实退
	 */
	public void updateXiuGaiJinE(String cwb, long delivertState, BigDecimal receivablefee, BigDecimal pos, BigDecimal paybackfee) {
		String sql = "update express_ops_deliver_cash set paybackfee=?" + ",receivableNoPosfee=?,receivablePosfee=? where cwb=? and deliverystate=? and deliverystate>0";
		jdbcTemplate.update(sql, paybackfee, receivablefee.subtract(pos), pos, cwb, delivertState);
	}

	/**
	 * 修改订单支付方式 修改小件员工作量统计表字段
	 * 
	 * @param cwb
	 *            订单好 更改条件
	 * @param delivertState
	 *            当前反馈状态 必须大于0才会更新这条记录
	 * @param receivableNoPosfee
	 *            非POS金额
	 * @param pos
	 *            pos实收
	 */
	public void updateXiuGaiZhiFuFangShi(String cwb, long delivertState, BigDecimal receivableNoPosfee, BigDecimal pos) {
		String sql = "update express_ops_deliver_cash set receivableNoPosfee=?,receivablePosfee=? where cwb=? and deliverystate=? and deliverystate>0";
		jdbcTemplate.update(sql, receivableNoPosfee, pos, cwb, delivertState);
	}

	/**
	 * 修改订单类型 修改小件员工作量统计表表字段
	 * 
	 * @param cwb
	 * @param deliverystate
	 *            当前反馈状态 必须大于0才会更新这条记录
	 * @param nowDeliveryState
	 *            新的配送结果
	 */
	public void updateXiuGaiDingDanLeiXing(String cwb, long delivertState, DeliveryStateEnum nowDeliveryState) {
		String sql = "update express_ops_deliver_cash set deliverystate=? where cwb=? and deliverystate=? and deliverystate>0";
		jdbcTemplate.update(sql, nowDeliveryState.getValue(), cwb, delivertState);

	}

	// ==============================修改订单使用的方法 end
	// ==================================
	public long selectCountCashbyCwb(String cwb) {
		String sql = "select count(1) from  express_ops_deliver_cash  where cwb=?  ";
		return jdbcTemplate.queryForInt(sql, cwb);

	}

}
