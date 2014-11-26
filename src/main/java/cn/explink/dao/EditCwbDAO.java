package cn.explink.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.util.StringUtil;

@Component
public class EditCwbDAO {

	private final class EdtiCwbMapper implements RowMapper<EdtiCwb_DeliveryStateDetail> {
		@Override
		public EdtiCwb_DeliveryStateDetail mapRow(ResultSet rs, int rowNum) throws SQLException {

			DeliveryState deliveryState = new DeliveryState();
			deliveryState.setId(rs.getLong("id"));
			deliveryState.setCwb(rs.getString("cwb"));
			deliveryState.setDeliveryid(rs.getLong("deliveryid"));
			deliveryState.setReceivedfee(rs.getBigDecimal("receivedfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("receivedfee"));
			deliveryState.setReturnedfee(rs.getBigDecimal("returnedfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("returnedfee"));
			deliveryState.setBusinessfee(rs.getBigDecimal("businessfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("businessfee"));
			deliveryState.setDeliverystate(rs.getLong("deliverystate"));
			deliveryState.setCash(rs.getBigDecimal("cash") == null ? BigDecimal.ZERO : rs.getBigDecimal("cash"));
			deliveryState.setPos(rs.getBigDecimal("pos") == null ? BigDecimal.ZERO : rs.getBigDecimal("pos"));
			deliveryState.setPosremark(StringUtil.nullConvertToEmptyString(rs.getString("posremark")));
			deliveryState.setMobilepodtime(rs.getTimestamp("mobilepodtime"));
			deliveryState.setCheckfee(rs.getBigDecimal("checkfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("checkfee"));
			deliveryState.setCheckremark(StringUtil.nullConvertToEmptyString(rs.getString("checkremark")));
			deliveryState.setReceivedfeeuser(rs.getLong("receivedfeeuser"));
			deliveryState.setCreatetime(StringUtil.nullConvertToEmptyString(rs.getString("createtime")));
			deliveryState.setOtherfee(rs.getBigDecimal("otherfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("otherfee"));
			deliveryState.setPodremarkid(rs.getObject("podremarkid") == null ? 0L : rs.getLong("podremarkid"));
			deliveryState.setDeliverstateremark(StringUtil.nullConvertToEmptyString(rs.getString("deliverstateremark")));
			deliveryState.setIsout(rs.getLong("isout"));
			deliveryState.setPos_feedback_flag(rs.getLong("pos_feedback_flag"));
			deliveryState.setUserid(rs.getLong("userid"));
			deliveryState.setGcaid(rs.getLong("gcaid"));
			deliveryState.setPayupid(rs.getLong("payupid"));

			deliveryState.setSign_typeid(rs.getInt("sign_typeid"));
			deliveryState.setSign_man(rs.getString("sign_man"));
			deliveryState.setSign_time(rs.getString("sign_time"));
			deliveryState.setDeliverybranchid(rs.getLong("deliverybranchid"));
			deliveryState.setCustomerid(rs.getLong("customerid"));
			deliveryState.setIssendcustomer(rs.getLong("issendcustomer"));
			deliveryState.setIsautolinghuo(rs.getLong("isautolinghuo"));
			deliveryState.setPushtime(rs.getString("pushtime"));
			deliveryState.setPushstate(rs.getLong("pushstate"));
			deliveryState.setPushremarks(rs.getString("pushremarks"));

			deliveryState.setDeliverytime(rs.getString("deliverytime"));
			deliveryState.setAuditingtime(rs.getString("auditingtime"));

			EdtiCwb_DeliveryStateDetail ec_dsd = new EdtiCwb_DeliveryStateDetail();
			ec_dsd.setDs(deliveryState);
			ec_dsd.setDsid(deliveryState.getId());

			ec_dsd.setId(rs.getLong("id"));// 自增长id
			ec_dsd.setFd_payup_detail_id(rs.getLong("fd_payup_detail_id"));// 小件员交款审核表finance_deliver_pay_up_detail的
																			// id
			ec_dsd.setFinance_audit_id(rs.getLong("finance_audit_id"));// 与供货商结算审核表finance_audit
																		// 的id
			ec_dsd.setF_payup_audit_id(rs.getLong("f_payup_audit_id"));// 站点交款审核表finance_pay_up_audit
																		// 的id
			ec_dsd.setNew_receivedfee(rs.getBigDecimal("new_receivedfee"));// 新的收到总金额
			ec_dsd.setNew_returnedfee(rs.getBigDecimal("new_returnedfee"));// 新的退还金额
			ec_dsd.setNew_businessfee(rs.getBigDecimal("new_businessfee"));// 新的应处理金额
			ec_dsd.setNew_isout(rs.getLong("new_isout"));// 新的应处理金额类型0应收1应退
			ec_dsd.setNew_deliverystate(rs.getLong("new_deliverystate"));// 新的订单配送状态
																			// 0未反馈
			ec_dsd.setNew_pos(rs.getBigDecimal("new_pos"));// 新的POS实收
			ec_dsd.setCwbordertypeid(rs.getInt("cwbordertypeid"));// 订单类型
			ec_dsd.setNew_cwbordertypeid(rs.getInt("new_cwbordertypeid"));// 新的订单类型
			ec_dsd.setNew_flowordertype(rs.getLong("new_flowordertype"));// 新的环节
			ec_dsd.setEditcwbtypeid(rs.getLong("editcwbtypeid"));// 修改订单的修改类型
			ec_dsd.setRequestUser(rs.getLong("requestUser"));// 修改订单的修改类型
			ec_dsd.setEditUser(rs.getLong("editUser"));// 修改订单的修改类型

			return ec_dsd;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 创建一条操作记录
	 * 
	 * @param of
	 * @return key
	 */
	public void creEditCwb(final EdtiCwb_DeliveryStateDetail ec) {
		jdbcTemplate.update("INSERT INTO `edit_delivery_state_detail` " + "(`cwb`,`deliveryid`,`receivedfee`,`returnedfee`,`businessfee`"
				+ ",`cwbordertypeid`,`deliverystate`,`cash`,`pos`,`posremark`" + ",`checkfee`,`checkremark`,`receivedfeeuser`,`statisticstate`,`createtime`"
				+ ",`otherfee`,`podremarkid`,`deliverstateremark`,`isout`,`pos_feedback_flag`" + ",`userid`,`deliverybranchid`,`state`,`sign_typeid`,`sign_man`,`sign_time`"
				+ ",`deliverytime`,`auditingtime`,`customerid`,`issendcustomer`,`isautolinghuo`" + ",`pushtime`,`pushstate`,`pushremarks`,`gcaid`,`payupid`,`fd_payup_detail_id`"
				+ ",`finance_audit_id`,`f_payup_audit_id`,`dsid`,`new_receivedfee`,`new_returnedfee`" + ",`new_businessfee`,`new_isout`,`new_deliverystate`,`new_pos`,`new_cwbordertypeid`"
				+ ",`new_flowordertype`,editcwbtypeid,requestUser,editUser)VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,? ,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, ec.getDs().getCwb());
						ps.setLong(2, ec.getDs().getDeliveryid());
						ps.setBigDecimal(3, ec.getDs().getReceivedfee());
						ps.setBigDecimal(4, ec.getDs().getReturnedfee());
						ps.setBigDecimal(5, ec.getDs().getBusinessfee());

						ps.setInt(6, ec.getCwbordertypeid());
						ps.setLong(7, ec.getDs().getDeliverystate());
						ps.setBigDecimal(8, ec.getDs().getCash());
						ps.setBigDecimal(9, ec.getDs().getPos());
						ps.setString(10, ec.getDs().getPosremark());

						ps.setBigDecimal(11, ec.getDs().getCheckfee());
						ps.setString(12, ec.getDs().getCheckremark());
						ps.setLong(13, ec.getDs().getReceivedfeeuser());
						ps.setInt(14, 1);
						ps.setString(15, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

						ps.setBigDecimal(16, ec.getDs().getOtherfee());
						ps.setLong(17, ec.getDs().getPodremarkid());
						ps.setString(18, ec.getDs().getDeliverstateremark());
						ps.setLong(19, ec.getDs().getIsout());
						ps.setLong(20, ec.getDs().getPos_feedback_flag());

						ps.setLong(21, ec.getDs().getUserid());
						ps.setLong(22, ec.getDs().getDeliverybranchid());
						ps.setInt(23, 1);
						ps.setLong(24, ec.getDs().getSign_typeid());
						ps.setString(25, ec.getDs().getSign_man());

						ps.setString(26, ec.getDs().getSign_time());
						ps.setString(27, ec.getDs().getDeliverytime());
						ps.setString(28, ec.getDs().getAuditingtime());
						ps.setLong(29, ec.getDs().getCustomerid());
						ps.setLong(30, ec.getDs().getIssendcustomer());

						ps.setLong(31, ec.getDs().getIsautolinghuo());
						ps.setString(32, ec.getDs().getPushtime());
						ps.setLong(33, ec.getDs().getPushstate());
						ps.setString(34, ec.getDs().getPushremarks());
						ps.setLong(35, ec.getDs().getGcaid());

						ps.setLong(36, ec.getDs().getPayupid());
						ps.setLong(37, ec.getFd_payup_detail_id());
						ps.setLong(38, ec.getFinance_audit_id());
						ps.setLong(39, ec.getF_payup_audit_id());
						ps.setLong(40, ec.getDsid());

						ps.setBigDecimal(41, ec.getNew_receivedfee());
						ps.setBigDecimal(42, ec.getNew_returnedfee());
						ps.setBigDecimal(43, ec.getNew_businessfee());
						ps.setLong(44, ec.getNew_isout());
						ps.setLong(45, ec.getNew_deliverystate());

						ps.setBigDecimal(46, ec.getNew_pos());
						ps.setInt(47, ec.getNew_cwbordertypeid());
						ps.setLong(48, ec.getNew_flowordertype());
						ps.setLong(49, ec.getEditcwbtypeid());
						ps.setLong(50, ec.getRequestUser());

						ps.setLong(51, ec.getEditUser());

					}

				});
	}

	/**
	 * 获取订单修改记录列表
	 * 
	 * @param fd_payup_detail_id
	 *            小件员交款审核表id
	 * @return
	 */
	public List<EdtiCwb_DeliveryStateDetail> getEditCwbListByFdPayupDetailId(Long fd_payup_detail_id) {
		return jdbcTemplate.query("SELECT * FROM edit_delivery_state_detail WHERE fd_payup_detail_id=?", new EdtiCwbMapper(), fd_payup_detail_id);
	}

	/**
	 * 获取订单修改记录列表
	 * 
	 * @param finance_audit_id
	 *            与供货商结算审核表id
	 * @return
	 */
	public List<EdtiCwb_DeliveryStateDetail> getEditCwbListByFinanceAuditId(Long finance_audit_id) {
		return jdbcTemplate.query("SELECT * FROM edit_delivery_state_detail WHERE finance_audit_id=?", new EdtiCwbMapper(), finance_audit_id);
	}

	/**
	 * 获取订单修改记录列表
	 * 
	 * @param f_payup_audit_id
	 *            站点交款审核表id
	 * @return
	 */
	public List<EdtiCwb_DeliveryStateDetail> getEditCwbListByFPayupAuditId(Long f_payup_audit_id) {
		return jdbcTemplate.query("SELECT * FROM edit_delivery_state_detail WHERE f_payup_audit_id=?", new EdtiCwbMapper(), f_payup_audit_id);
	}

	/**
	 * 获取订单修改记录列表
	 * 
	 * @param gcaid
	 *            归班表id
	 * @return
	 */
	public List<EdtiCwb_DeliveryStateDetail> getEditCwbListByGcaid(Long gcaid) {
		return jdbcTemplate.query("SELECT * FROM edit_delivery_state_detail WHERE gcaid=?", new EdtiCwbMapper(), gcaid);
	}

	/**
	 * 获取订单修改记录列表
	 * 
	 * @param payupid
	 *            站点交款表id
	 * @return
	 */
	public List<EdtiCwb_DeliveryStateDetail> getEditCwbListByPayupid(String payupid) {
		return jdbcTemplate.query("SELECT * FROM edit_delivery_state_detail WHERE payupid=?", new EdtiCwbMapper(), payupid);
	}

}
