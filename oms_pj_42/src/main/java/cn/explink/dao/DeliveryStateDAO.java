package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.BranchPayamountSUMDTO;
import cn.explink.controller.DeliveryStateDTO;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;

@Component
public class DeliveryStateDAO {
	private final class DeliveryStateRowMapper implements RowMapper<DeliveryState> {
		@Override
		public DeliveryState mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryState deliveryState = new DeliveryState();
			deliveryState.setCwb(rs.getString("cwb"));
			deliveryState.setDeliveryid(rs.getLong("deliveryid"));
			deliveryState.setReceivedfee(rs.getBigDecimal("receivedfee"));
			deliveryState.setReturnedfee(rs.getBigDecimal("returnedfee"));
			deliveryState.setBusinessfee(rs.getBigDecimal("businessfee"));
			deliveryState.setCwbordertypeid(rs.getString("cwbordertypeid"));
			deliveryState.setDeliverystate(rs.getLong("deliverystate"));
			deliveryState.setCash(rs.getBigDecimal("cash"));
			deliveryState.setPos(rs.getBigDecimal("pos"));
			deliveryState.setPosremark(rs.getString("posremark"));
			deliveryState.setMobilepodtime(rs.getTimestamp("mobilepodtime"));
			deliveryState.setCheckfee(rs.getBigDecimal("checkfee"));
			deliveryState.setCheckremark(rs.getString("checkremark"));
			deliveryState.setReceivedfeeuser(rs.getLong("receivedfeeuser"));
			deliveryState.setGobackid(rs.getLong("gobackid"));
			deliveryState.setGoodsname(rs.getString("goodsname"));
			deliveryState.setSendcarname(rs.getString("sendcarname"));
			deliveryState.setCaramount(rs.getBigDecimal("caramount"));
			deliveryState.setConsigneename(rs.getString("consigneename"));
			deliveryState.setConsigneephone(rs.getString("consigneephone"));
			deliveryState.setConsigneeaddress(rs.getString("consigneeaddress"));
			deliveryState.setEmaildate(rs.getString("emaildate"));
			deliveryState.setBranchid(rs.getLong("branchid"));
			deliveryState.setOtherfee(rs.getBigDecimal("otherfee"));
			deliveryState.setDeliveryName(rs.getString("deliveryName"));

			return deliveryState;
		}
	}

	private final class BranchPayamountSUMDTORowMapper implements RowMapper<BranchPayamountSUMDTO> {
		@Override
		public BranchPayamountSUMDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchPayamountSUMDTO branchpayamountSum = new BranchPayamountSUMDTO();
			branchpayamountSum.setCash(rs.getBigDecimal("cash"));
			branchpayamountSum.setOtherfee(rs.getBigDecimal("otherfee"));
			branchpayamountSum.setPos(rs.getBigDecimal("pos"));
			branchpayamountSum.setCheckfee(rs.getBigDecimal("checkfee"));
			return branchpayamountSum;
		}
	}

	private final class DeliveryStateDTOMapper implements RowMapper<DeliveryStateDTO> {
		@Override
		public DeliveryStateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryStateDTO deliveryStateDTO = new DeliveryStateDTO();
			deliveryStateDTO.setBranchid(rs.getLong("branchid"));
			deliveryStateDTO.setReceivedfee(rs.getBigDecimal("receivedfee"));
			return deliveryStateDTO;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creDeliveryState(String cwb, long deliveryid, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, String cwbordertypeid, long deliverystate, BigDecimal cash,
			BigDecimal pos, BigDecimal checkfee, long gobackid, String goodsname, String sendcarname, BigDecimal caramount, String consigneename, String consigneephone, String consigneeaddress,
			String emaildate, BigDecimal otherfee, long branchid, String deliveryName, String posremark, String createtime, String shangmenlanshoutime) {
		boolean isRepeatFlag = this.isRepeatFlagDeliverState(cwb);
		if (isRepeatFlag) {
			String sql = "update express_ops_delivery_state set receivedfee=?,returnedfee=?,businessfee=?,cwbordertypeid=?,deliverystate=?,cash=?,pos=?,"
					+ "checkfee=?,deliveryid=?,gobackid=?,goodsname=?,sendcarname=?,caramount=?,consigneename=?,consigneephone=?,"
					+ "consigneeaddress=?,emaildate=?,otherfee=?,branchid=?,deliveryName=?,posremark=?,createtime=?,shangmenlanshoutime=? " + " where cwb=? ";
			this.jdbcTemplate.update(sql, receivedfee, returnedfee, businessfee, cwbordertypeid, deliverystate, cash, pos, checkfee, deliveryid, gobackid, goodsname, sendcarname, caramount,
					consigneename, consigneephone, consigneeaddress, emaildate, otherfee, branchid, deliveryName, posremark, createtime, shangmenlanshoutime, cwb);
		} else {
			String sql = "insert into express_ops_delivery_state(cwb,receivedfee,returnedfee,businessfee,cwbordertypeid,deliverystate,cash,pos,"
					+ "checkfee,deliveryid,gobackid,goodsname,sendcarname,caramount,consigneename,consigneephone,consigneeaddress,emaildate,otherfee,branchid,deliveryName,posremark,createtime,shangmenlanshoutime) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.jdbcTemplate.update(sql, cwb, receivedfee, returnedfee, businessfee, cwbordertypeid, deliverystate, cash, pos, checkfee, deliveryid, gobackid, goodsname, sendcarname, caramount,
					consigneename, consigneephone, consigneeaddress, emaildate, otherfee, branchid, deliveryName, posremark, createtime, shangmenlanshoutime);
		}

	}

	private boolean isRepeatFlagDeliverState(String cwb) {
		boolean flag = false;
		String sql = "select count(1) from express_ops_delivery_state where cwb='" + cwb + "'";
		int counts = this.jdbcTemplate.queryForInt(sql);
		if (counts > 0) {
			flag = true;
		}
		return flag;
	}

	// 修改POS签收后配送结果.
	public void UpdateDeliveryState(String cwb, long deliverystate) {
		String sql = "update express_ops_delivery_state  set deliverystate=? where cwb=? ";
		this.jdbcTemplate.update(sql, deliverystate, cwb);
	}

	// jms 归班时把归班的id存储到反馈表中
	public void UpdateGoclassState(String cwbStr, long gobackid) {
		String sql = "update express_ops_delivery_state  set gobackid=? where cwb in(" + cwbStr + ") ";
		this.jdbcTemplate.update(sql, gobackid);
	}

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb=" + cwb;
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public void saveDeliverStateByParam(long deliveryid, String cwb, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, String cwbordertypeid, long deliverystate,
			BigDecimal cash, BigDecimal pos, BigDecimal checkfee) {
		String sql = "update express_ops_delivery_state set receivedfee=" + receivedfee + ",returnedfee=" + returnedfee + ",businessfee=" + businessfee + ",cwbordertypeid=" + cwbordertypeid
				+ ",deliverystate=" + deliverystate + ",cash=" + cash + ",pos=" + pos + ",checkfee=" + checkfee + ",deliveryid=" + deliveryid + " where cwb='" + cwb + "'";
		this.jdbcTemplate.update(sql);
	}

	public List<DeliveryState> getDeliveryStateByDeliverAndDeliverystate(long deliveryid, long deliverystate1, long deliverystate2, long deliverystate3) {
		String sql = "select * from express_ops_delivery_state where deliveryid=" + deliveryid + " and deliverystate in(" + deliverystate1 + "," + deliverystate2 + "," + deliverystate3 + ")";
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}

	public List<DeliveryState> getDeliveryStateBygobackid(String gobackidStr, long page) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb in(" + gobackidStr + ")";
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 各项款项查询分页
	 * 
	 * @param gobackidStr
	 * @param type
	 * @param page
	 * @return
	 */
	public List<DeliveryState> getDeliveryStateBygobackidBytype(String gobackidStr, long type, long page) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb in(" + gobackidStr + ")";
			if (type > 0) {
				if (type == 1) {// 现金
					sql += " and cash>0";
				} else if (type == 2) { // pos
					sql += " and pos>0";
				} else if (type == 3) {// 其他
					sql += " and otherfee>0";
				} else if (type == 4) { // 支票
					sql += " and checkfee>0";
				}
			}
			if (page > 0) {
				sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public long getDeliveryCountByconStr(String conStr) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_delivery_state where cwb in(" + conStr + ")");
	}

	/**
	 * 各项款项查询数量
	 * 
	 * @param conStr
	 * @param type
	 * @return
	 */
	public long getDeliveryCountByconStrType(String conStr, long type) {
		String sql = "select count(1) from express_ops_delivery_state where cwb in(" + conStr + ")";
		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and cash>0";
			} else if (type == 2) { // pos
				sql += " and pos>0";
			} else if (type == 3) {// 其他
				sql += " and otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and checkfee>0";
			}
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<DeliveryState> getOrderByconStr(String conStr) {
		try {
			return this.jdbcTemplate.query("select * from express_ops_delivery_state where cwb in(" + conStr + ")", new DeliveryStateRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public BranchPayamountSUMDTO getSumByGobaciid(String gobackidStr) {
		BranchPayamountSUMDTO bSUM = this.jdbcTemplate.queryForObject(
				"select sum(cash) as cash, sum(otherfee) as otherfee,sum(pos) as pos,sum(checkfee) as checkfee from express_ops_delivery_state where cwb in (" + gobackidStr + ")",
				new BranchPayamountSUMDTORowMapper());
		return bSUM;
	}

	public int getGobaciidSum(String gobackidStr) {
		int bSUM = this.jdbcTemplate.queryForInt("select count(1)  from express_ops_delivery_state where cwb in (" + gobackidStr + ")");
		return bSUM;
	}

	public String getOrderFlowWhereSql(String orderFlowSql, String beginemaildate, String endemaildate, int isnow, int flowordertype) {

		if ((beginemaildate.length() > 0) || (endemaildate.length() > 0) || (isnow > 0) || (flowordertype > 0)) {
			orderFlowSql += " where ";
			StringBuffer sb = new StringBuffer();
			if (beginemaildate.length() > 0) {
				sb.append(" and emaildate >= '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				sb.append(" and emaildate <= '" + endemaildate + "'");
			}
			if (isnow > 0) {
				sb.append(" and isnow =" + isnow);
			}
			if (flowordertype > 0) {
				sb.append(" and flowordertype =" + flowordertype);
			}
			orderFlowSql += sb.substring(4, sb.length());
		}
		return orderFlowSql;
	}

	public List<DeliveryStateDTO> getDeliveryStateByCwb(String beginemaildate, String endemaildate, int isnow, int flowordertype) {
		String orderFlowSql = "SELECT branchid,cwb FROM express_ops_order_flow";
		orderFlowSql = this.getOrderFlowWhereSql(orderFlowSql, beginemaildate, endemaildate, isnow, flowordertype);
		String sql = "SELECT p.branchid ,COUNT(1),SUM(receivedfee)as receivedfee FROM express_ops_delivery_state AS e LEFT JOIN (" + orderFlowSql + ") p ON e.cwb = p.cwb  GROUP BY p.branchid";
		return this.jdbcTemplate.query(sql, new DeliveryStateDTOMapper());
	}

	public void saveDeliveyStateForCancel(String cwb, String deliverstateremark, double amount_after) {
		String sql = "update express_ops_delivery_state set pos=" + amount_after + ",receivedfee=" + amount_after + ",deliverystate=0,posremark='" + deliverstateremark + "' where cwb='" + cwb + "' ";
		this.jdbcTemplate.update(sql);
	}

	public void updateCwbDetailByPos_backout(String cwb) {
		String cwbsql = "update express_ops_cwb_detail set flowordertype=" + FlowOrderTypeEnum.CheXiaoFanKui.getValue()
				+ ",signinman='',signintime='',backreason='',backreasonid=0,leavedreasonStr='',leavedreasonid=0 " + " where cwb='" + cwb + "' and state=1";
		this.jdbcTemplate.update(cwbsql);
	}

	// 更新POS机刷卡记录表为撤销
	public void updateSetPosByPos_backout(String cwb) {
		String cwbsql = "update express_set_pos set  pos_signtype=0,pos_signname='',pos_signtime='',pos_backoutflag=1 where cwb='" + cwb + "'";
		this.jdbcTemplate.update(cwbsql);
	}

}
