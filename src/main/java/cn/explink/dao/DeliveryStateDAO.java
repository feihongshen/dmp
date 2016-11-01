package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryPayment;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Smtcount;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryPaymentPatternEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeliveryStateDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;

	private final class DeliveryStateRowMapper implements RowMapper<DeliveryState> {
		@Override
		public DeliveryState mapRow(ResultSet rs, int rowNum) throws SQLException {
			return getDeliverStateRow(rs);
		}

	}
	
	/**
	 * 抽象出来，复用
	 * @author chunlei05.li
	 * @date 2016年8月26日 下午1:55:59
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private DeliveryState getDeliverStateRow(ResultSet rs) throws SQLException {
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
		deliveryState.setCwbordertypeid(rs.getInt("cwbordertypeid"));
		deliveryState.setDeliverytime(rs.getString("deliverytime"));
		deliveryState.setAuditingtime(rs.getString("auditingtime"));
		deliveryState.setCodpos(rs.getBigDecimal("codpos") == null ? BigDecimal.ZERO : rs.getBigDecimal("codpos"));
		deliveryState.setShouldfare(rs.getBigDecimal("shouldfare") == null ? BigDecimal.ZERO : rs.getBigDecimal("shouldfare"));
		deliveryState.setInfactfare(rs.getBigDecimal("infactfare") == null ? BigDecimal.ZERO : rs.getBigDecimal("infactfare"));
		deliveryState.setShangmenlanshoutime(rs.getString("shangmenlanshoutime"));
		deliveryState.setSign_img(rs.getString("sign_img"));
		return deliveryState;
	}
	
	private final class DeliveryPaymentRowMapper implements RowMapper<DeliveryPayment> {
		@Override
		public DeliveryPayment mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryPayment deliveryPayment = new DeliveryPayment();
			DeliveryState deliveryState = getDeliverStateRow(rs);
			deliveryPayment.setDeliveryState(deliveryState);
			// cwbOrder表
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setCwb(deliveryState.getCwb());
			cwbOrder.setReceivablefee(rs.getBigDecimal("cwbreceivablefee") == null ? BigDecimal.ZERO : rs.getBigDecimal("cwbreceivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("cwbpaybackfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("cwbpaybackfee"));
			deliveryPayment.setCwbOrder(cwbOrder);
			return deliveryPayment;
		}

	}

	private final class OrderCwbMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String obj = rs.getString("cwb");
			return obj;
		}
	}
	
	
	private final class OrderCwbByCustomerIdMapper implements RowMapper<Smtcount> {
		@Override
		public Smtcount mapRow(ResultSet rs, int rowNum) throws SQLException {
			Smtcount obj = new Smtcount();
			obj.setCount(rs.getLong("customerid"));
			obj.setPscount(rs.getLong("pscount"));
			return obj;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 产生一条反馈记录
	 *
	 * @param cwb
	 * @param businessfee
	 * @param cwbordertypeid
	 * @param deliveryid
	 * @param createtime
	 */
	public long creDeliveryState(final String cwb, final BigDecimal businessfee, final int cwbordertypeid, final User deliveryuser, final String createtime, final long isout, final long userid,
			final long deliverybranchid, final long customerid, final BigDecimal shouldfare) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_ops_delivery_state(cwb,businessfee,cwbordertypeid,deliveryid,createtime,isout,userid,deliverybranchid,customerid,shouldfare) values(?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				ps.setString(1, cwb);
				ps.setBigDecimal(2, businessfee);
				ps.setLong(3, cwbordertypeid);
				ps.setLong(4, deliveryuser.getUserid());
				ps.setString(5, createtime);
				ps.setLong(6, isout);
				ps.setLong(7, userid);
				ps.setLong(8, deliverybranchid);
				ps.setLong(9, customerid);
				ps.setBigDecimal(10, shouldfare);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 根据gcaid汇总codpos交款.
	 *
	 * @param gcaids
	 * @return gcaid->codpos汇总.
	 */
	public Map<Long, BigDecimal> queryCodPosAmountSum(List<Long> gcaids) {
		Map<Long, BigDecimal> amountMap = new HashMap<Long, BigDecimal>();
		if ((gcaids == null) || gcaids.isEmpty()) {
			return amountMap;
		}
		String sqlInPara = this.getSqlInPara(gcaids);
		if (sqlInPara.length() == 0) {
			return amountMap;
		}
		String sql = "select gcaid ,sum(codpos) as codpossum from express_ops_delivery_state where gcaid in(" + sqlInPara + ") group by gcaid";
		this.jdbcTemplate.query(sql, new CodPosAmountSumRCH(amountMap));

		return amountMap;
	}

	private class CodPosAmountSumRCH implements RowCallbackHandler {

		private Map<Long, BigDecimal> amountMap = null;

		public CodPosAmountSumRCH(Map<Long, BigDecimal> amountMap) {
			this.amountMap = amountMap;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			Long gcaid = Long.valueOf(rs.getLong("gcaid"));
			BigDecimal amountSum = rs.getBigDecimal("codpossum");
			this.amountMap.put(gcaid, amountSum);
		}
	}

	/**
	 * 返回数字in(需要前置判空).
	 *
	 * @param ids
	 * @return
	 */
	private <T extends Number> String getSqlInPara(List<T> ids) {
		StringBuilder sqlIn = new StringBuilder();
		for (T id : ids) {
			if (id == null) {
				continue;
			}
			sqlIn.append(id.toString());
			sqlIn.append(",");
		}
		if (sqlIn.length() == 0) {
			return sqlIn.toString();
		}
		return sqlIn.substring(0, sqlIn.length() - 1);
	}

	/**
	 * 根据订单号查询反馈记录 查询最后一条信息，并且能够确保只查询到一条limit 0,1
	 *
	 * @param cwb
	 * @return
	 */
	public DeliveryState getActiveDeliveryStateByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb=? and state=1 order by id desc limit 0,1 ";
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 根据订单查询最近的一条已经审核的记录 
	 * @author Hps_Concerto
	 * @param cwb
	 * @return
	 */
	public DeliveryState getDelivertStateYishenheCountByCwb(String cwb){
		try{
			String sql = "SELECT * FROM express_ops_delivery_state WHERE cwb='"+cwb+"' AND deliverystate!=0 ORDER BY auditingtime DESC LIMIT 0,1";
			DeliveryState ob = this.jdbcTemplate.queryForObject(sql,new DeliveryStateRowMapper());
			return ob;
		}catch(Exception ee){
			ee.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据订单号查询反馈记录 查询最后一条信息，并且能够确保只查询到一条limit 0,1
	 *
	 * @param cwb
	 * @return
	 */
	public List<DeliveryState> getActiveDeliveryStateByCwbs(List<String> cwbs) {
		StringBuilder stringBuilder = new StringBuilder("select * from express_ops_delivery_state where cwb in (");
		for (String cwb : cwbs) {
			stringBuilder.append("'").append(cwb).append("',");
		}
		if (cwbs.size() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		stringBuilder.append(") and state=1  ");
		return this.jdbcTemplate.query(stringBuilder.toString(), new DeliveryStateRowMapper());
	}

	/**
	 * 根据订单号查询反馈记录 查询最后一条信息，并且能够确保只查询到一条limit 0,1
	 *
	 * @param cwb
	 * @return
	 */
	public DeliveryState getDeliveryStateByCwb_posHelper(String cwb, long deliverid) {
		User user = this.userDAO.getUserByUserid(deliverid);
		DeliveryState deliverstate = this.getActiveDeliveryStateByCwbAndBranchid(cwb, user.getBranchid());

		if (deliverstate != null) {
			return deliverstate;
		} else {
			this.logger.info("POS支付跳流程，需自动创建数据到deliver_state表，订单号={},deliverid={}", cwb, deliverid);
			this.cwbOrderService.receiveGoods(user, user, cwb, cwb);
			deliverstate = this.getActiveDeliveryStateByCwb(cwb);

			return deliverstate;
		}

	}

	public DeliveryState getDeliveryStateByCwb_posAndSign(String cwb, long deliverid) {
		DeliveryState deliverstate = this.getActiveDeliveryStateByCwb(cwb);

		if (deliverstate != null) {
			return deliverstate;
		} else {
			this.logger.info("POS支付跳流程，需自动创建数据到deliver_state表，订单号={},deliverid={}", cwb, deliverid);
			this.cwbOrderService.receiveGoods(this.userDAO.getUserByUserid(deliverid), this.userDAO.getUserByUserid(deliverid), cwb, cwb);
			deliverstate = this.getActiveDeliveryStateByCwb(cwb);
			return deliverstate;
		}
	}

	public void inactiveDeliveryStateByCwb(String cwb) {
		this.jdbcTemplate.execute("update express_ops_delivery_state set state=0 where cwb='" + cwb + "' and state=1");
	}

	public void reObtain(String cwb, long deliveryid, long userid, long deliverybranchid, String createtime) {
		this.jdbcTemplate.execute("update express_ops_delivery_state set gcaid=0,deliverystate=0,deliveryid='" + deliveryid + "',userid='" + userid + "',deliverybranchid='" + deliverybranchid
				+ "',createtime='" + createtime + "' where cwb='" + cwb + "' and state=1");
	}

	/**
	 * 根据派送员获得当前未归班的反馈记录
	 *
	 * @param deliveryId
	 * @return
	 */
	public List<DeliveryState> getDeliveryStateByDeliver(long deliveryId) {
		return this.jdbcTemplate.query("select * from express_ops_delivery_state where deliveryid=? and gcaid<=0 and state=1 order by deliverystate asc", new DeliveryStateRowMapper(), deliveryId);
	}

	/**
	 * 根据订单编号串获取对应的订单反馈记录
	 *
	 * @param cwbs
	 *            格式 '324324','sf3234'
	 * @return
	 */
	public List<DeliveryState> getDeliveryStateByCwbs(String cwbs) {
		if (cwbs.length() > 0) {
			return this.jdbcTemplate.query("select * from express_ops_delivery_state where cwb in (" + cwbs + ") and state=1 order by deliverystate asc", new DeliveryStateRowMapper());
		} else {
			return null;
		}

	}

	public List<DeliveryState> getDeliveryStateByGcaid(Long gcaid) {
		String sql = "select * from express_ops_delivery_state where gcaid=" + gcaid.toString();

		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}

	private final class DeliverByDeliveryStateNoZero implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("deliveryid", rs.getString("deliveryid"));
			reJson.put("delivername", rs.getString("realname"));
			reJson.put("num", rs.getString("num"));

			return reJson;
		}
	}

	private final class DeliverByDeliveryId implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("deliveryid", rs.getString("deliveryid"));
			reJson.put("subAmount", rs.getString("subAmount"));
			reJson.put("subAmountPos", rs.getString("subAmountPos"));
			return reJson;
		}
	}

	private final class DeliverByDeliverybranchid implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("deliverybranchid", rs.getString("deliverybranchid"));
			reJson.put("amount", rs.getString("amount"));
			reJson.put("pos", rs.getString("pos"));
			return reJson;
		}
	}

	/**
	 * 获取所有身上有订单的小件员列表
	 *
	 * @return
	 */
	public List<JSONObject> getDeliverByDeliveryStateNoZero(long branchId) {
		return this.jdbcTemplate.query("SELECT u.realname,ds.deliveryid,COUNT(1) AS num FROM express_ops_delivery_state ds "
				+ "LEFT JOIN express_set_user u ON ds.deliveryid=u.userid WHERE ds.deliverybranchid=? AND ds.gcaid<=0 AND ds.state=1 GROUP BY ds.deliveryid", new DeliverByDeliveryStateNoZero(),
				branchId);
	}

	public List<DeliveryState> getDeliverByBranchid(long branchId) {
		return this.jdbcTemplate.query("select * from express_ops_delivery_state where deliverybranchid=? and gcaid<=0 and state=1 ", new DeliveryStateRowMapper(), branchId);
	}

	/**
	 * 获取所有小件员列表以及小件员身上订单的汇总统计
	 *
	 * @return
	 */
	public List<DeliveryState> getDeliverByBrancheForCountState(long branchId) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_delivery_state ds RIGHT JOIN express_set_user u  ON u.userid=ds.deliveryid WHERE gcaid=0 and ds.state=1 and deliverybranchid=? ",
				new DeliveryStateRowMapper(), branchId);
	}

	/**
	 * 根据订单号更新对应的派送员id和创建时间
	 *
	 * @param cwb
	 * @param deliveryid
	 * @param createtime
	 */

	public void noSubSave(String cwb) {
		String sql = "update express_ops_delivery_state set gcaid=-1 where cwb=? and state=1";
		this.jdbcTemplate.update(sql, cwb);
	}

	public void reSubSave(String cwb) {
		String sql = "update express_ops_delivery_state set gcaid=0 where cwb=? and state=1";
		this.jdbcTemplate.update(sql, cwb);
	}
	
	public void updateStateBycwb(String cwb) {
		String sql = "update express_ops_delivery_state set state=0 where cwb=? and state=1";
		this.jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 通过订单号更新该反馈记录的信息
	 *
	 * @param gcaId
	 */
	public void auditDelivery(long id, long receivedfeeuser, String auditingtime, long gcaId) {
		String sql = "update express_ops_delivery_state set receivedfeeuser=?,gcaid=?,auditingtime=? where id =? ";
		this.jdbcTemplate.update(sql, receivedfeeuser, gcaId, auditingtime, id);
	}

	public void saveForReFanKui(String cwb, long deliverid, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, long deliverystate, BigDecimal cash, BigDecimal pos,
			String posremark, BigDecimal checkfee, String checkremark, BigDecimal otherfee, long podremarkid, String deliverstateremark, String createtime, int sign_typeid, String sign_man,
			String sign_time,String sign_man_phone, BigDecimal codpos, BigDecimal infactfare) {
		String sql = "update express_ops_delivery_state set sign_man=?,sign_time=?,sign_man_phone=? ";
		sql = this.setDeliveyStateParmForSql(sql, cwb, deliverid, receivedfee, returnedfee, businessfee, deliverystate, cash, pos, posremark, checkfee, checkremark, otherfee, podremarkid,
				deliverstateremark, createtime, sign_typeid, codpos, infactfare);
		
		logger.info("DeliveryStateDAO.saveForReFanKui sql:", sql);
		
		this.jdbcTemplate.update(sql, sign_man, sign_time,sign_man_phone);
	}

	public void saveDeliveyStateIsautolinghuoByCwb(long isautolinghuo, String cwb) {
		String sql = "update express_ops_delivery_state set isautolinghuo=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, isautolinghuo, cwb);
	}

	public void saveDeliveyStateIsautolinghuoByCwb2(long isautolinghuo, String cwb, int firstlevelreasonid) {
		String sql = "update express_ops_delivery_state set isautolinghuo=?,firstlevelid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, isautolinghuo, firstlevelreasonid, cwb);
	}

	public void saveDeliveyStateByCwb(String cwb, long deliverystate, String createtime) {
		String sql = "update express_ops_delivery_state set deliverystate=?,createtime=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, deliverystate, createtime, cwb);
	}

	public void saveDeliveyStateForCancel(String cwb, String deliverstateremark, double amount_after) {
		String sql = "update express_ops_delivery_state set pos=?,receivedfee=?,posremark='',deliverstateremark=?,pos_feedback_flag=0,deliverystate=0,sign_typeid=0,sign_man='',sign_time='' where cwb=? and state=1";
		this.jdbcTemplate.update(sql, amount_after, amount_after, deliverstateremark, cwb);
	}

	public String setDeliveyStateParmForSql(String sql, String cwb, long deliverid, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, long deliverystate, BigDecimal cash,
			BigDecimal pos, String posremark, BigDecimal checkfee, String checkremark, BigDecimal otherfee, long podremarkid, String deliverstateremark, String createtime, int sign_typeid,
			BigDecimal codpos, BigDecimal infactfare) {		
		StringBuffer w = new StringBuffer();		
		if ((cwb.length() > 0) || (deliverid > 0) || (receivedfee.compareTo(new BigDecimal(0)) > -1) || (returnedfee.compareTo(new BigDecimal(0)) > -1)
				|| (businessfee.compareTo(new BigDecimal(0)) > -1) || (deliverystate > 0) || (cash.compareTo(new BigDecimal(0)) > -1) || (pos.compareTo(new BigDecimal(0)) > -1)
				|| (posremark.length() > 0) || (checkfee.compareTo(new BigDecimal(0)) > -1) || (checkremark.length() > 0) || (otherfee.compareTo(new BigDecimal(0)) > -1) || (podremarkid > 0)
				|| (deliverstateremark.length() > 0) || (createtime.length() > 0) || (codpos.compareTo(new BigDecimal(0)) > -1) || (infactfare.compareTo(new BigDecimal(0)) > -1)) {
			

			if (receivedfee.compareTo(new BigDecimal(0)) > -1) {
				w.append(",receivedfee=" + receivedfee);
			}
			if (returnedfee.compareTo(new BigDecimal(0)) > -1) {
				w.append(",returnedfee=" + returnedfee);
			}
			if (businessfee.compareTo(new BigDecimal(0)) > -1) {
				w.append(",businessfee=" + businessfee);
			}
			if (deliverystate > 0) {
				w.append(",deliverystate=" + deliverystate);
			}
			if (cash.compareTo(new BigDecimal(0)) > -1) {
				w.append(",cash=" + cash);
			}
			if (pos.compareTo(new BigDecimal(0)) > -1) {
				w.append(",pos=" + pos);
			}
			if (posremark.length() > 0) {
				w.append(",posremark='" + posremark + "'");
			}
			if (checkfee.compareTo(new BigDecimal(0)) > -1) {
				w.append(",checkfee=" + checkfee);
			}
			if (checkremark.length() > 0) {
				w.append(",checkremark='" + checkremark + "'");
			}
			if (otherfee.compareTo(new BigDecimal(0)) > -1) {
				w.append(",otherfee=" + otherfee);
			}
			if (podremarkid > 0) {
				w.append(",podremarkid=" + podremarkid);
			}
			if (deliverstateremark.length() > 0) {
				w.append(",deliverstateremark='" + deliverstateremark + "'");
			}
			if (deliverid > 0) {
				w.append(",deliveryid=" + deliverid);
			}
			if (createtime.length() > 0) {
				w.append(",createtime='" + createtime + "'");
			}
			if (codpos.compareTo(new BigDecimal(0)) > -1) {
				w.append(",codpos=" + codpos);
			}
			if (infactfare.compareTo(new BigDecimal(0)) > -1) {
				w.append(",infactfare=" + infactfare);
			}
			w.append(",sign_typeid=" + sign_typeid);

			
		}
		
		w.append(" where cwb='" + cwb + "' and state=1");
		sql += w.toString();
		
		return sql;
	}

	/**
	 * 验证是否审核了已审核的反馈记录
	 *
	 * @param subTrStr
	 */
	public int getIsRepeat(String subTrStr) {
		String sql = "select count(1) from  express_ops_delivery_state  where cwb in (" + subTrStr + ") and  gcaid>0 and state=1";
		return this.jdbcTemplate.queryForInt(sql);
	}

	/**
	 * 获取所有小件员列表以及小件员身上订单的汇总统计
	 *
	 * @return
	 */
	public List<DeliveryState> getDeliverByGcaid(long gcaid) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_delivery_state WHERE gcaid = ? ", new DeliveryStateRowMapper(), gcaid);
	}

	public List<DeliveryState> getDeliveryStateByGcaid(String gcaids) {
		String sql = "select * from express_ops_delivery_state where gcaid in(" + gcaids + ") and  state=1 ";
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}

	public List<DeliveryState> getDeliveryStateByCwb(String cwb) {
		String sql = "select * from express_ops_delivery_state where cwb =?  and  state=1 ";
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper(), cwb);
	}

	/**
	 * 去掉归班审核后，获取当前应上缴的反馈记录列表
	 *
	 * @param branchid
	 */
	public List<DeliveryState> getCountPayToUpByNotAudit(long branchid) {
		String sql = "select * from express_ops_delivery_state where deliverybranchid=? and deliverystate>0 and payupid=0 and state=1";
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper(), branchid);
	}

	private final class DeliveryStateStatisticsMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("num", rs.getString("num"));
			obj.put("receivablefee", rs.getString("receivablefee") == null ? 0 : rs.getString("receivablefee"));
			obj.put("paybackfee", rs.getString("paybackfee") == null ? 0 : rs.getString("paybackfee"));
			return obj;
		}
	}

	private final class CustomerTongjiMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("num", rs.getString("num"));
			obj.put("receivablefee", rs.getString("receivablefee") == null ? 0 : rs.getString("receivablefee"));
			obj.put("paybackfee", rs.getString("paybackfee") == null ? 0 : rs.getString("paybackfee"));
			return obj;
		}
	}

	private final class CwbStringMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwb = rs.getString("cwb");
			return cwb;
		}
	}

	private final class DeliveryStateStatisticsFunsMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("num", rs.getString("num"));
			obj.put("cash", rs.getString("cash") == null ? 0 : rs.getString("cash"));
			obj.put("pos", rs.getString("pos") == null ? 0 : rs.getString("pos"));
			obj.put("checkfee", rs.getString("checkfee") == null ? 0 : rs.getString("checkfee"));
			return obj;
		}
	}

	public List<JSONObject> getDeliveryStateByDeliverybranchidAndDeliverystateToJson(long branchid, int deliverystate) {
		return this.jdbcTemplate
				.query("select count(1) as num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) as receivablefee,SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) as paybackfee from express_ops_delivery_state where  deliverybranchid=? and deliverystate=? and state=1 ",
						new DeliveryStateStatisticsMapper(), branchid, deliverystate);

	}

	public List<DeliveryState> getDeliverByCwbs(long page, String cwbs) {
		return this.jdbcTemplate.query("select * from express_ops_delivery_state where cwb in (" + cwbs + ") and state=1 limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER,
				new DeliveryStateRowMapper());
	}

	public long getDeliverByCwbsCount(String cwbs) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_delivery_state where cwb in (" + cwbs + ") and state=1 ");
	}

	// ======站点日报统计使用======begin====
	/**
	 * 按站点、反馈状态、反馈日期 查询订单数量 state=1的
	 *
	 * @param branchid
	 * @param deliveryState
	 * @param credate
	 * @return
	 */
	public long getCountByBranchidAndDeliveryStateAndCredateAndState(long branchid, long deliveryState, String startCredate, String endCredate) {
		String sql = "select  count(1)  from  express_ops_delivery_state where  " + "deliverybranchid=? and deliverystate=? and auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0";
		return this.jdbcTemplate.queryForLong(sql, branchid, deliveryState, startCredate, endCredate);
	}

	public List<String> getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(long branchid, long deliveryState, String startCredate, String endCredate) {
		String sql = "select  DISTINCT cwb from  express_ops_delivery_state where  " + "deliverybranchid=? and deliverystate=? and auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0";
		return this.jdbcTemplate.queryForList(sql, String.class, branchid, deliveryState, startCredate, endCredate);
	}

	/**
	 * 按站点、反馈状态、反馈日期、订单类型 查询订单数量 state=1的
	 *
	 * @param branchid
	 * @param deliveryState
	 * @param cwbordertypeid
	 * @param credate
	 * @return
	 */
	public long getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderType(long branchid, long deliveryState, long cwbordertypeid, String startCredate, String endCredate) {
		String sql = "select  count(1)  from express_ops_delivery_state " + " where deliverybranchid=? and  deliverystate =? and cwbordertypeid=? and "
				+ " auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, deliveryState, cwbordertypeid, startCredate, endCredate);
	}

	public List<String> getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderTypeCwb(long branchid, long deliveryState, long cwbordertypeid, String startCredate, String endCredate) {
		String sql = "select  DISTINCT cwb  from express_ops_delivery_state " + " where deliverybranchid=? and  deliverystate =? and cwbordertypeid=? and "
				+ " auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0 ";
		return this.jdbcTemplate.query(sql, new OrderCwbMapper(), branchid, deliveryState, cwbordertypeid, startCredate, endCredate);
	}

	/**
	 * 按站点、反馈状态、时间 查询订单数量
	 *
	 * @param branchid
	 * @param deliveryState
	 * @param credate
	 * @return
	 */
	public long getCountByBranchidAndDeliveryStateAndCredate(long flowordertype, long branchid, long deliveryState) {
		String sql = "select  count(DISTINCT ds.cwb) from express_ops_delivery_state AS ds  LEFT JOIN " + " express_ops_cwb_detail AS de ON ds.cwb = de.cwb "
				+ " WHERE de.flowordertype=? AND ds.deliverybranchid=? and  ds.deliverystate =? " + " and  ds.gcaid > 0 and de.state = 1  and ds.state=1";
		return this.jdbcTemplate.queryForLong(sql, flowordertype, branchid, deliveryState);
	}

	public List<JSONObject> getCountByBranchidAndDeliveryStateAndCredateTojson(long flowordertype, long branchid, long deliveryState) {
		String sql = "select   count(1) as num,SUM(CASE WHEN ds.isout=0 THEN ds.businessfee ELSE 0 END) as receivablefee," + "SUM(CASE WHEN ds.isout=1 THEN ds.businessfee ELSE 0 END) as paybackfee "
				+ " from express_ops_delivery_state AS ds  LEFT JOIN " + " express_ops_cwb_detail AS de ON ds.cwb = de.cwb "
				+ " WHERE de.flowordertype=? AND ds.deliverybranchid=? and  ds.deliverystate =? " + " and  ds.gcaid > 0 and de.state = 1  and ds.state=1";
		return this.jdbcTemplate.query(sql, new DeliveryStateStatisticsMapper(), flowordertype, branchid, deliveryState);
	}

	/**
	 * 按站点、反馈状态、时间 查询订单数量昨日的滞留
	 *
	 * @param branchid
	 * @param deliveryState
	 * @param credate
	 * @return
	 */
	public long getCountByBranchidAndDeliveryStateAndCredateByZuori(long flowordertype, long branchid, long deliveryState, String startCredate) {
		/*
		 * String sql =
		 * "select count(DISTINCT des.cwb) from (select ds.cwb from express_ops_delivery_state AS ds  LEFT JOIN "
		 * + " express_ops_goto_class_auditing AS gc ON ds.gcaid = gc.id " +
		 * " WHERE gc.auditingtime < ? AND ds.deliverybranchid=? and  ds.deliverystate =? "
		 * + " and  ds.gcaid > 0 and ds.state=1 ) as des left join " +
		 * " express_ops_cwb_detail AS de ON  des.cwb=de.cwb where de.flowordertype=?  and de.state = 1  "
		 * ;
		 */

		String sql = "SELECT COUNT(DISTINCT ds.cwb) FROM  express_ops_delivery_state ds FORCE INDEX(ds_deliverybranchid_idx) LEFT JOIN " + "express_ops_cwb_detail cd ON  cd.cwb=ds.cwb "
				+ "WHERE ds.auditingtime < ? AND ds.deliverybranchid=? AND  ds.deliverystate =? " + "AND  ds.gcaid > 0 AND ds.state=1  AND cd.flowordertype=?  AND cd.state = 1  ";
		return this.jdbcTemplate.queryForLong(sql, startCredate, branchid, deliveryState, flowordertype);
	}

	/**
	 * 按站点、反馈状态、时间 查询订单数量昨日的滞留
	 *
	 * @param flowordertype
	 * @param branchid
	 * @param deliveryState
	 * @return
	 */
	public List<String> getCwbByBranchidAndDeliveryStateAndCredateByZuori(long flowordertype, long branchid, long deliveryState, String startCredate) {
		/*
		 * String sql =
		 * "select DISTINCT des.cwb as cwb from (select ds.cwb from express_ops_delivery_state AS ds  LEFT JOIN "
		 * + " express_ops_goto_class_auditing AS gc ON ds.gcaid = gc.id " +
		 * " WHERE gc.auditingtime < ? AND ds.deliverybranchid=? and  ds.deliverystate =? "
		 * + " and  ds.gcaid > 0 and ds.state=1 ) as des left join " +
		 * " express_ops_cwb_detail AS de ON  des.cwb=de.cwb where de.flowordertype=?  and de.state = 1  "
		 * ;
		 */
		String sql = "SELECT DISTINCT ds.cwb AS cwb FROM express_ops_delivery_state ds FORCE INDEX(ds_deliverybranchid_idx) " + "LEFT JOIN express_ops_cwb_detail cd ON  ds.cwb=cd.cwb"
				+ " WHERE ds.auditingtime < ? AND ds.deliverybranchid=? AND  ds.deliverystate =? " + "AND  ds.gcaid > 0 AND ds.state=1 AND cd.flowordertype=?  AND cd.state = 1  ";
		return this.jdbcTemplate.query(sql, new OrderCwbMapper(), startCredate, branchid, deliveryState, flowordertype);
	}

	/**
	 * 按站点、反馈状态、时间 查询订单数量今日的滞留 单数
	 *
	 * @param branchid
	 * @param deliveryState
	 * @param credate
	 * @return
	 */
	public long getCountByBranchidAndDeliveryStateAndCredateByJinri(long flowordertype, long branchid, long deliveryState, String startCredate, String endCredate) {
		/*
		 * String sql =
		 * "select count(DISTINCT des.cwb) from (select ds.cwb from express_ops_delivery_state AS ds  LEFT JOIN "
		 * + " express_ops_goto_class_auditing AS gc ON ds.gcaid = gc.id " +
		 * " WHERE gc.auditingtime >= ? and gc.auditingtime <= ?  AND ds.deliverybranchid=? and  ds.deliverystate =? "
		 * + " and  ds.gcaid > 0 and ds.state=1 ) as des left join " +
		 * " express_ops_cwb_detail AS de ON  des.cwb=de.cwb where de.flowordertype=?  and de.state = 1 "
		 * ;
		 */
		String sql = "SELECT COUNT(DISTINCT ds.cwb) FROM express_ops_delivery_state  ds FORCE INDEX(ds_deliverybranchid_idx) " + "LEFT JOIN express_ops_cwb_detail  cd ON  cd.cwb=ds.cwb"
				+ " WHERE ds.auditingtime >= ? AND ds.auditingtime <= ?  AND ds.deliverybranchid=? AND  ds.deliverystate =? "
				+ "AND  ds.gcaid > 0 AND ds.state=1  AND cd.flowordertype=?  AND cd.state = 1 ";
		return this.jdbcTemplate.queryForLong(sql, startCredate, endCredate, branchid, deliveryState, flowordertype);
	}

	/**
	 * 按站点、反馈状态 查询订单数量今日的滞留
	 *
	 * @param flowordertype
	 * @param branchid
	 * @param deliveryState
	 * @return
	 */
	public List<String> getCwbByBranchidAndDeliveryStateAndCredateByJinri(long flowordertype, long branchid, long deliveryState, String startCredate, String endCredate) {
		/*
		 * String sql =
		 * "select DISTINCT des.cwb as cwb from (select ds.cwb from express_ops_delivery_state AS ds  LEFT JOIN "
		 * + " express_ops_goto_class_auditing AS gc ON ds.gcaid = gc.id " +
		 * " WHERE gc.auditingtime >= ? and gc.auditingtime <= ?  AND ds.deliverybranchid=? and  ds.deliverystate =? "
		 * + " and  ds.gcaid > 0 and ds.state=1 ) as des left join " +
		 * " express_ops_cwb_detail AS de ON  des.cwb=de.cwb where de.flowordertype=?  and de.state = 1 "
		 * ;
		 */
		String sql = "SELECT DISTINCT ds.cwb AS cwb FROM express_ops_delivery_state ds FORCE INDEX(ds_deliverybranchid_idx) LEFT JOIN " + "express_ops_cwb_detail cd ON cd.cwb = ds.cwb "
				+ "WHERE ds.auditingtime >= ? AND ds.auditingtime <= ?  AND ds.deliverybranchid=? AND  ds.deliverystate =? "
				+ "AND  ds.gcaid > 0 AND ds.state=1 AND cd.flowordertype=?  AND cd.state = 1 ";
		return this.jdbcTemplate.query(sql, new OrderCwbMapper(), startCredate, endCredate, branchid, deliveryState, flowordertype);
	}

	/**
	 * 获取单号
	 *
	 * @param flowordertype
	 * @param branchid
	 * @param deliveryState
	 * @return
	 */
	public List<String> getCountByBranchidAndDeliveryStateAndCredateCwb(long flowordertype, long branchid, long deliveryState) {
		String sql = "select DISTINCT ds.cwb as cwb from express_ops_delivery_state AS ds FORCE INDEX(ds_deliverybranchid_idx) LEFT JOIN " + " express_ops_cwb_detail AS de ON ds.cwb = de.cwb "
				+ " WHERE de.flowordertype=? AND ds.deliverybranchid=? and  ds.deliverystate =? " + " and  ds.gcaid > 0 and  de.state = 1 and  ds.state=1";
		return this.jdbcTemplate.query(sql, new OrderCwbMapper(), flowordertype, branchid, deliveryState);
	}

	/**
	 * 按站点、反馈状态、反馈日期 查询订单数量、应收金额、应退金额
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, int deliverystate, String startCredate, String endCredate) {
		String sql = "SELECT COUNT(1) AS num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) AS receivablefee," + "SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) AS paybackfee "
				+ "FROM express_ops_delivery_state WHERE  " + "deliverybranchid=? AND deliverystate=? AND auditingtime>=?  AND auditingtime<=?  AND state=1 AND gcaid>0";
		return this.jdbcTemplate.query(sql, new DeliveryStateStatisticsMapper(), branchid, deliverystate, startCredate, endCredate);
	}

	public List<String> getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, int deliverystate, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select DISTINCT cwb from express_ops_delivery_state where  "
				+ "deliverybranchid=? and deliverystate=? and auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0 ", new OrderCwbMapper(), branchid, deliverystate, startCredate, endCredate);
	}

	/**
	 * 更新上门揽收时间
	 *
	 * @param cwb
	 * @param shangmenlanshoutime
	 */
	public void updateShangmenlanshoutime(String cwb, String shangmenlanshoutime) {
		String sql = "update express_ops_delivery_state set shangmenlanshoutime=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, shangmenlanshoutime, cwb);
	}

	/**
	 * 按站点查询反馈的合计订单数量、应收金额、应退金额
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select count(1) as num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) as receivablefee,"
				+ "SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) as paybackfee " + " from express_ops_delivery_state where  "
				+ " deliverystate >0  and deliverybranchid=? and auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0 ", new DeliveryStateStatisticsMapper(), branchid, startCredate,
				endCredate);
	}

	public List<String> getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select DISTINCT cwb from express_ops_delivery_state  where  "
				+ " deliverystate >0  and deliverybranchid=? and auditingtime>=?  and auditingtime<=?  and state=1 and gcaid>0 ", new OrderCwbMapper(), branchid, startCredate, endCredate);
	}

	/**
	 * 按站点查询未反馈的合计订单数量、应收金额、应退金额
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, String startCredate, String endCredate, long flowordertype) {
		return this.jdbcTemplate.query("select count(1) as num,SUM(CASE WHEN ds.isout=0 THEN ds.businessfee ELSE 0 END) as receivablefee,"
				+ "SUM(CASE WHEN ds.isout=1 THEN ds.businessfee ELSE 0 END) as paybackfee "
				+ "FROM express_ops_delivery_state AS ds FORCE INDEX(ds_deliverybranchid_idx) LEFT JOIN express_ops_cwb_detail AS de ON ds.cwb=de.cwb "
				+ " WHERE  ds.deliverystate <= 0 AND ds.deliverybranchid=?  AND ds.state=1  AND de.state=1 AND de.flowordertype =?   ", new DeliveryStateStatisticsMapper(), branchid, flowordertype);
	}

	public List<String> getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, String startCredate, String endCredate, long flowordertype) {
		return this.jdbcTemplate.query(
				"SELECT DISTINCT de.cwb as cwb FROM express_ops_delivery_state AS ds FORCE INDEX(ds_deliverybranchid_idx) LEFT JOIN express_ops_cwb_detail AS de ON ds.cwb=de.cwb "
						+ " WHERE  ds.deliverystate <= 0 AND ds.deliverybranchid=?  AND ds.state=1  AND de.state=1 AND de.flowordertype =? ", new OrderCwbMapper(), branchid, flowordertype);
	}

	/**
	 * 按站点查询反馈，未归班的合计订单数量、应收金额、应退金额
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, String startCredate, String endCredate) {
		return this.jdbcTemplate.query(
				"select count(1) as num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) as receivablefee,SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) as paybackfee from express_ops_delivery_state where  "
						+ " deliverystate > 0 and gcaid <= 0 and deliverybranchid=?  and state=1  ", new DeliveryStateStatisticsMapper(), branchid);
	}

	public List<String> getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select DISTINCT cwb as cwb from express_ops_delivery_state where  " + " deliverystate > 0 and gcaid <= 0 and deliverybranchid=?  and state=1  ",
				new OrderCwbMapper(), branchid);
	}

	/**
	 * 未归班的统计
	 *
	 * @param branchid
	 * @param startCredate
	 * @param endCredate
	 * @return
	 */
	public List<JSONObject> getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredate(long branchid) {
		return this.jdbcTemplate.query("select count(1) as num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) as receivablefee,SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) as paybackfee"
				+ " from express_ops_delivery_state where  " + " gcaid <= 0 and deliverybranchid=?  and state=1  ", new DeliveryStateStatisticsMapper(), branchid);
	}

	public long getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_delivery_state where  " + " gcaid <= 0 and deliverybranchid=?  and state=1  ", branchid);
	}

	public List<String> getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid) {
		return this.jdbcTemplate.query("select DISTINCT cwb as cwb from express_ops_delivery_state where  " + " gcaid <= 0 and deliverybranchid=?  and state=1  ", new OrderCwbMapper(), branchid);
	}

	/**
	 * 按站点查询款项分布单数、现金、pos、支票
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getFunsByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, long deliverystate, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select count(1) as num," + "SUM(cash) as cash," + "SUM(pos) as pos," + "SUM(checkfee) as checkfee " + " from express_ops_delivery_state  where  "
				+ " deliverybranchid=? and deliverystate = ? and gcaid > 0 and auditingtime>=?  and auditingtime<=? and state=1 ", new DeliveryStateStatisticsFunsMapper(), branchid, deliverystate,
				startCredate, endCredate);
	}

	public List<String> getFunsByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, long deliverystate, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select DISTINCT cwb " + " from express_ops_delivery_state  where  "
				+ " deliverybranchid=? and deliverystate = ? and gcaid > 0 and auditingtime>=?  and auditingtime<=?   and state=1 ", new OrderCwbMapper(), branchid, deliverystate, startCredate,
				endCredate);
	}

	/**
	 * 按站点查询上门退成功的单数和应退金额
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getFunsShangmentuiByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, long deliverystate, String startCredate, String endCredate) {
		String sql = "select count(1) as num,SUM(CASE WHEN isout=0 THEN businessfee ELSE 0 END) as receivablefee," + "SUM(CASE WHEN isout=1 THEN businessfee ELSE 0 END) as paybackfee "
				+ " from express_ops_delivery_state where  " + " deliverystate = ? and deliverybranchid=?  and gcaid > 0 and auditingtime>=?  and auditingtime<=?  and state=1 ";
		return this.jdbcTemplate.query(sql, new DeliveryStateStatisticsMapper(), deliverystate, branchid, startCredate, endCredate);
	}

	public List<String> getFunsShangmentuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, long deliverystate, String startCredate, String endCredate) {
		String sql = "select DISTINCT cwb from express_ops_delivery_state  where  " + " deliverystate = ? and deliverybranchid=?  and gcaid > 0 and auditingtime>=?  and auditingtime<=?  and state=1 ";
		return this.jdbcTemplate.query(sql, new OrderCwbMapper(), deliverystate, branchid, startCredate, endCredate);
	}

	/**
	 * 按站点查询已上缴的单数、现金、pos、支票
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getPayUpByDeliverybranchidAndDeliverystateAndCredateToJson(long branchid, String startCredate, String endCredate) {
		// 应退金额在cash里有一份钱，所有要减两次
		return this.jdbcTemplate.query("select count(1) as num,SUM(ds.cash+ds.checkfee+ds.otherfee-ds.returnedfee) as cash,SUM(ds.pos) as pos,SUM(ds.checkfee) as checkfee "
				+ " from express_ops_delivery_state as ds LEFT JOIN  express_ops_goto_class_auditing as gc ON ds.gcaid=gc.id "
				+ " where gc.payupid<>0 and ds.deliverybranchid = ?  and ds.gcaid > 0 and gc.auditingtime >= ? " + " and gc.auditingtime <= ? and ds.deliverystate IN(1,2,3,5,8) and ds.state=1 ",
				new DeliveryStateStatisticsFunsMapper(), branchid, startCredate, endCredate);
	}

	public List<String> getPayUpByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(long branchid, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select DISTINCT cwb  " + " from express_ops_delivery_state where  " + " payupid<>0 and deliverybranchid=?  and gcaid > 0 and auditingtime >= ?  "
				+ " and auditingtime <= ? and deliverystate IN(1,2,3,5,8)  and state=1 ", new OrderCwbMapper(), branchid, startCredate, endCredate);
	}

	// 库存的统计
	public List<JSONObject> getCountbyKucun(long branchid) {
		return this.jdbcTemplate.query(" select count(1) as num,SUM(receivablefee) as receivablefee," + "SUM(paybackfee) as paybackfee FROM express_ops_cwb_detail WHERE "
				+ " currentbranchid=? and state = 1 ", new DeliveryStateStatisticsMapper(), branchid);
	}

	// ======站点日报统计使用======end===

	public DeliveryState getDeliveryByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb =?  and  state=1 limit 0,1";
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb);
		} catch (DataAccessException e) {
			return new DeliveryState();
		}
	}

	public DeliveryState getDeliveryByCwbLock(String cwb) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb =?  and  state=1 limit 0,1 for update";
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}

	// ============货款结算 和退货款结算 begin==========

	/**
	 * 按供货商、反馈状态、反馈日期 查询订单数量、应收金额、应退金额
	 *
	 * @param customerid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getListByCustomeridAndDeliverystateAndCredate(long customerid, long deliverystate, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select count(1) as num,SUM(CASE WHEN ds.isout=0 THEN ds.businessfee ELSE 0 END) as receivablefee,"
				+ "SUM(CASE WHEN ds.isout=1 THEN ds.businessfee ELSE 0 END) as paybackfee from express_ops_delivery_state as ds LEFT JOIN "
				+ "express_ops_cwb_detail as de on ds.cwb = de.cwb  where  " + " de.customerid=? and ds.deliverystate=? and ds.createtime>=?  and ds.createtime<=?   and de.state = 1 and ds.state=1 ",
				new DeliveryStateStatisticsMapper(), customerid, deliverystate, startCredate, endCredate);
	}

	/**
	 * 按供货商、反馈状态、反馈日期 查询订单数量、应收金额、应退金额
	 *
	 * @param customerid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<String> getCwbByCustomeridAndDeliverystateAndCredate(long customerid, String deliverystates, String startCredate, String endCredate) {
		return this.jdbcTemplate.query("select ds.cwb as cwb from express_ops_delivery_state as ds LEFT JOIN " + "express_ops_cwb_detail as de on ds.cwb = de.cwb  where  "
				+ " de.customerid=? and ds.deliverystate in(" + deliverystates + ") and ds.createtime>=?  and ds.createtime<=? and gcaid>0  and de.state=1 and ds.state=1 order by de.customerid ",
				new CwbStringMapper(), customerid, startCredate, endCredate);
	}

	public List<JSONObject> getCwbByCustomeridAndDeliverystateAndCredateAll(String deliverystates, String startCredate, String endCredate) {
		String sql = "select de.customerid as customerid ,count(1) as num,SUM(receivedfee) as receivablefee," + "SUM(returnedfee) as paybackfee from express_ops_delivery_state as ds LEFT JOIN "
				+ "express_ops_cwb_detail as de on ds.cwb = de.cwb  where  " + "  ds.deliverystate in (" + deliverystates
				+ ") and ds.createtime>=?  and ds.createtime<=?  and ds.state=1  and de.state=1 and gcaid>0 group by de.customerid order by de.customerid ";
		return this.jdbcTemplate.query(sql, new CustomerTongjiMapper(), startCredate, endCredate);
	}

	/**
	 * 按供货商、反馈状态、反馈日期 查询订单数量、应收金额、应退金额
	 *
	 * @param customerid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<JSONObject> getListByCustomeridAndFloworderTypeowAndCredate(long flowordertype, String startCredate, String endCredate, long customerid) {

		if (customerid > 0) {
			String sql = "select customerid,count(1) as num,SUM(receivablefee) as receivablefee," + "SUM(paybackfee) as paybackfee from express_ops_cwb_detail  where  "
					+ "  flowordertype >= ? and emaildate >=?  and emaildate <=? and customerid=? and state=1 group by customerid ";
			return this.jdbcTemplate.query(sql, new CustomerTongjiMapper(), flowordertype, startCredate, endCredate, customerid);
		} else {
			String sql = "select customerid,count(1) as num,SUM(receivablefee) as receivablefee," + "SUM(paybackfee) as paybackfee from express_ops_cwb_detail  where  "
					+ "  flowordertype >= ? and emaildate >=?  and emaildate <=? and state=1 group by customerid ";
			return this.jdbcTemplate.query(sql, new CustomerTongjiMapper(), flowordertype, startCredate, endCredate);
		}

	}

	public List<String> getCwbListByCustomeridAndFloworderTypeowAndCredateExp(long customerid, String startCredate, String endCredate, long page) {
		if (customerid > 0) {
			String sql = "select cwb from express_ops_cwb_detail  where  " + " customerid =? and emaildate >=?  and emaildate <=?  and state=1 ";
			if (page > -1) {
				sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new CwbStringMapper(), customerid, startCredate, endCredate);
		} else {
			String sql = "select cwb from express_ops_cwb_detail  where  " + "  emaildate >=?  and emaildate <=?  and state=1 ";
			if (page > -1) {
				sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
			}
			return this.jdbcTemplate.query(sql, new CwbStringMapper(), startCredate, endCredate);
		}
	}

	// ============货款结算 和退货款结算 end==========

	/**
	 * 更新反馈时间
	 *
	 * @param cwb
	 * @param deliverytime
	 */
	public void updateDeliverytime(String cwb, String deliverytime) {
		String sql = "update express_ops_delivery_state set deliverytime=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, deliverytime, cwb);
	}

	/**
	 * 更新审核时间
	 *
	 * @param cwb
	 * @param auditingtime
	 */
	public void updateAuditingtime(String cwb, String auditingtime) {
		String sql = "update express_ops_delivery_state set auditingtime=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, auditingtime, cwb);
	}

	public List<DeliveryState> getDeliveryByIds(String ids) {
		try {
			String sql = "select * from express_ops_delivery_state where id in(" + ids + ") and state=1 and payupid=0 ";
			return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<DeliveryState> getDeliveryByGcaid(long gcaid) {
		try {
			String sql = "select * from express_ops_delivery_state where gcaid = ? and state=1 ";
			return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper(), gcaid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 * 根据小件员id和既定的反馈ids 更新归班gcaid、上交款payupid、归班时间
	 *
	 * @param ids
	 * @param deliveid
	 * @param payupid
	 * @param gcaid
	 * @param auditingtime
	 */
	public void updateDeliveryByIds(String ids, long deliveid, long payupid, long gcaid, String auditingtime) {
		String sql = "update express_ops_delivery_state set payupid=?,gcaid=?,auditingtime=? where id in(" + ids + ") and deliveryid=? and state=1 ";
		this.jdbcTemplate.update(sql, payupid, gcaid, auditingtime, deliveid);
	}

	/**
	 * 在既定反馈ids的范围里按小件员获取 cash和pos统计的分组
	 *
	 * @param ids
	 * @return
	 */
	public List<JSONObject> getDeliveryByIdsAndDeliveryId(String ids) {
		try {// subAmount, subAmountPos
			String sql = "select deliveryid, sum(cash+otherfee+checkfee) as subAmount,sum(pos) as subAmountPos from express_ops_delivery_state where id in(" + ids
					+ ") and state=1 and payupid=0 group by deliveryid";
			return this.jdbcTemplate.query(sql, new DeliverByDeliveryId());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateStateByIds(long issendcustomer, String pushtime, String ids) {
		String sql = "update express_ops_delivery_state set issendcustomer=?,pushtime=? where id in(" + ids + ") ";
		this.jdbcTemplate.update(sql, issendcustomer, pushtime);
	}

	/**
	 * 更新反馈状态
	 *
	 * @param cwb
	 * @param state
	 */
	public void updateDelState(String cwb, String sign_time, String sign_man, int deliverystate, long userid) {
		String sql = "update express_ops_delivery_state set deliverystate=? ,deliverytime=?,sign_time=?,sign_man=?,userid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, deliverystate, sign_time, sign_time, sign_man, userid, cwb);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getDeliveryStateByCredateAndFlowordertype(String begindate, String enddate, long isauditTime, long isaudit, String[] operationOrderResultTypes, String[] dispatchbranchids,
			long deliverid, int isTuotou, String customeridStr, int firstlevelid) {

		String sql = "select cwb from express_ops_delivery_state where ";
		if (isauditTime == 0) {
			sql = sql.replace("express_ops_delivery_state", "express_ops_delivery_state FORCE INDEX(ds_deliverytime_idx)");
			sql += " deliverytime >= '" + begindate + "' ";
			sql += " and deliverytime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("express_ops_delivery_state", "express_ops_delivery_state FORCE INDEX(ds_auditingtime_idx)");
			sql += " auditingtime >= '" + begindate + "' ";
			sql += " and auditingtime <= '" + enddate + "' ";
		}

		if ((dispatchbranchids.length > 0) || (operationOrderResultTypes.length > 0) || (deliverid > 0) || (isaudit >= 0)) {
			StringBuffer deliverystatesql = new StringBuffer();
			if (isTuotou > 0) {
				deliverystatesql.append(" and state=1 ");
			}
			if (dispatchbranchids.length > 0) {
				deliverystatesql.append(" and deliverybranchid in (");
				if (dispatchbranchids.length > 0) {
					for (String dispatchbranchid : dispatchbranchids) {
						deliverystatesql.append(dispatchbranchid + ",");
					}
				}
				deliverystatesql.append("-1)");
			}
			if (customeridStr.length() > 0) {
				deliverystatesql.append(" and customerid in(" + customeridStr + ")");
			}

			if (operationOrderResultTypes.length > 0) {
				deliverystatesql.append(" and deliverystate in (");
				for (String deliverystate : operationOrderResultTypes) {
					deliverystatesql.append(deliverystate + ",");
				}
				deliverystatesql = new StringBuffer(deliverystatesql.substring(0, deliverystatesql.length() - 1) + ")");
			}
			if (deliverid > 0) {
				deliverystatesql.append(" and deliveryid=" + deliverid);
			}

			if (isaudit == 0) {
				deliverystatesql.append(" and gcaid<1");
			} else if (isaudit == 1) {
				deliverystatesql.append(" and gcaid>0");
			}

			if (firstlevelid > 0) {
				deliverystatesql.append(" and firstlevelid= " + firstlevelid);
			}
			sql += deliverystatesql.toString();
		}
		
		this.logger.info("DeliveryStateDAO.getDeliveryStateByCredateAndFlowordertype sql={}", sql);
		
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<JSONObject> getDeliveryByDayGroupByDeliverybranchid(String day, String secondDay) {
		try {
			String sql = "SELECT deliverybranchid,SUM(cash+otherfee+checkfee-returnedfee) AS amount,SUM(pos) AS pos FROM `express_ops_delivery_state` WHERE deliverytime>? AND deliverytime<=? AND auditingtime=''  AND state=1  GROUP BY deliverybranchid";
			return this.jdbcTemplate.query(sql, new DeliverByDeliverybranchid(), day, secondDay);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void saveDeliveryStateForB2C(String pushtime, long pushstate, String pushremarks, String cwb, long deliverystate) {
		String sql = "update express_ops_delivery_state set pushtime=?,pushstate=?,pushremarks=? where cwb=? and deliverystate=? and pushtime=''";
		this.jdbcTemplate.update(sql, pushtime, pushstate, pushremarks, cwb, deliverystate);
	}

	// /////////////////////财务结算改版4663版本方法////////////////

	public DeliveryState getDeliveryByCwbAndDeliverystate(String cwb) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb =? and deliverystate in (1,2,3,5,8) and state=1 ";
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb);
		} catch (Exception ee) {
			return null;
		}
	}

	// ==============================修改订单使用的方法 start
	// ==================================

	/**
	 * 重置审核状态 修改反馈表字段 cash、pos、otherfee、checkfee、receivedfee、returnedfee
	 * 各项反馈时使用的金额字段 在sql中自动清零； deliverytime 反馈时间 在sql中自动清空； gcaid 归班id
	 * 在sql中自动变为0 auditingtime 归班时间 在sql中自动清空； receivedfeeuser 收款人 在sql中自动置为0；
	 * sign_typeid 签收类型 在sql中自动置为未签收 0 sign_man 签收人 签收人在sql中自动清空 sign_time 签收时间
	 * 在sql中自动清空 issendcustomer 是否已推送供货商 状态在sql中自动变更为 0 为推送 将 pushtime 推送成功时间
	 * 在sql中自动置为"",sign_img自动清空
	 *
	 * @param id
	 *            反馈表id
	 * @param deliverystate
	 *            反馈为0 未反馈；
	 */
	public void updateForChongZhiShenHe(long id, DeliveryStateEnum deliverystate, BigDecimal infactfare) {
		this.jdbcTemplate.update("update express_ops_delivery_state " + "set cash=0,pos=0,otherfee=0,checkfee=0,receivedfee=0,returnedfee=0"
				+ ",deliverytime='',gcaid=0,auditingtime='',receivedfeeuser=0" + ",sign_typeid=0,sign_man=null,sign_time=null,issendcustomer=0,pushtime='',deliverystate=?,infactfare=?,sign_img=''"
				+ " where id=?", deliverystate.getValue(), infactfare, id);

	}

	/**
	 * 修改金额 修改反馈表字段
	 *
	 * @param id
	 * @param receivedfee
	 *            实收金额
	 * @param returnedfee
	 *            实退金额
	 * @param cash
	 * @param pos
	 * @param checkfee
	 * @param otherfee
	 * @param isOut
	 *            应处理金额的1应退0应收
	 */
	public void updateXiuGaiJinE(long id, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, BigDecimal cash, BigDecimal pos, BigDecimal checkfee, BigDecimal otherfee, Long isOut) {
		this.jdbcTemplate.update("update express_ops_delivery_state set receivedfee=?,returnedfee=?" + ",businessfee=?,isout=?,cash=?,pos=?,otherfee=?,checkfee=? where id=?", receivedfee,
				returnedfee, businessfee, isOut, cash, pos, otherfee, checkfee, id);

	}

	/**
	 * 修改支付方式 修改反馈表字段
	 *
	 * @param id
	 * @param cash
	 * @param pos
	 * @param checkfee
	 * @param otherfee
	 */
	public void updateXiuGaiZhiFuFangShi(long id, BigDecimal cash, BigDecimal pos, BigDecimal checkfee, BigDecimal otherfee) {
		this.jdbcTemplate.update("update express_ops_delivery_state set cash=?,pos=?,otherfee=?,checkfee=? where id=?", cash, pos, otherfee, checkfee, id);
	}

	/**
	 * 修改订单类型 修改反馈表字段
	 *
	 * @param id
	 * @param newcwbordertypeid
	 *            新的订单类型
	 * @param nowDeliveryState
	 *            随订单类型改变而改变的配送结果
	 */
	public void updateXiuGaiDingDanLeiXing(long id, int newcwbordertypeid, DeliveryStateEnum nowDeliveryState) {
		this.jdbcTemplate.update("update express_ops_delivery_state set cwbordertypeid=?,deliverystate=? where id=?", newcwbordertypeid, nowDeliveryState.getValue(), id);

	}

	// ==============================修改订单使用的方法 End
	// ==================================

	/**
	 * 根据订单号修改操作人
	 *
	 * @param userid
	 * @param cwb
	 *
	 */
	public void updateOperatorIdByCwb(long userid, String cwb) {
		String sql = "update express_ops_delivery_state set userid=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, userid, cwb);

	}

	/**
	 * g根据订单号和站点Id判断是否重复。 查询最后一条信息，并且能够确保只查询到一条limit 0,1
	 *
	 * @param cwb
	 * @return
	 */
	public DeliveryState getActiveDeliveryStateByCwbAndBranchid(String cwb, long branchid) {
		try {
			String sql = "select * from express_ops_delivery_state where cwb=? and deliverybranchid=? and state=1 order by id desc limit 0,1 ";
			return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb, branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 小件员领货查询 deliverystate表
	 *
	 * @param page
	 * @param branchid
	 * @param deliveryid
	 * @param start
	 * @param end
	 * @param deliverystate
	 * @return list
	 */
	public List<DeliveryState> getCwbAndDeliveryByToPagedeliverystate(long page, long branchid, long deliveryid, String start, String end, long deliverystate) {
		String sql = "SELECT * " + "FROM express_ops_delivery_state  where 1=1";
		if ((start.length() > 0) && (end.length() > 0)) {
			sql += " and createtime>'" + start + "' and createtime<'" + end + "'";
		}
		if (deliverystate >= 0) {
			sql += " and deliverystate=" + deliverystate;
		}
		if (deliveryid > 0) {
			sql += " and  deliveryid=" + deliveryid;
		}
		if (branchid > 0) {
			sql += " and  deliverybranchid=" + branchid;
		}
		sql += " and state=1 limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + "," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}

	/**
	 * 小件员领货查询 count,分页
	 *
	 * @param branchid
	 * @param deliverystate
	 * @param start
	 * @param end
	 * @param deliveryid
	 * @return
	 */
	public long getCountBydeliverystate(long branchid, long deliverystate, String start, String end, long deliveryid) {
		String sql = "SELECT count(1) " + "FROM express_ops_delivery_state  where 1=1";
		if ((start.length() > 0) && (end.length() > 0)) {
			sql += " and createtime>'" + start + "' and createtime<'" + end + "'";
		}
		if (deliverystate >= 0) {
			sql += " and deliverystate=" + deliverystate;
		}
		if (deliveryid > 0) {
			sql += " and  deliveryid=" + deliveryid;
		}
		if (branchid > 0) {
			sql += " and  deliverybranchid=" + branchid;
		}
		sql += " and state=1 ";
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 修改收件人--上门退
	 *
	 * @param consigneename
	 * @param cwb
	 */
	public void updateDeliveryStateConsigneenameByCwb(String consigneename, String cwb) {
		this.jdbcTemplate.update("update express_ops_delivery_state set sign_man=? where cwb=?", consigneename, cwb);
	}

	public List<DeliveryState> getDeliveryByDeliver(long deliverybranchid, long deliverystate, long page) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_delivery_state where deliverybranchid=? and deliverystate=? and state=1 ");
		// if(!"".equals(cwb)){
		// sql.append(" and cwb not in("+cwb+")");
		// }
		sql.append("order by id desc");
		sql.append(" limit ?,? ");
		return this.jdbcTemplate.query(sql.toString(), new DeliveryStateRowMapper(), deliverybranchid, deliverystate, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 小件员订单的列表
	 *
	 * @return
	 */
	public List<JSONObject> getDeliverByDeliveryStateNoZeroUserid(long branchId, long userid) {
		return this.jdbcTemplate.query("SELECT u.realname,ds.deliveryid,COUNT(1) AS num FROM express_ops_delivery_state ds "
				+ "LEFT JOIN express_set_user u ON ds.deliveryid=u.userid WHERE ds.deliverybranchid=? AND u.userid=? AND ds.gcaid<=0 AND ds.state=1 GROUP BY ds.deliveryid",
				new DeliverByDeliveryStateNoZero(), branchId, userid);
	}

	public long getCountYiLingHuo(long branchid, long deliverystate, long deliveryid) {
		String sql = "SELECT count(1) FROM express_ops_delivery_state WHERE deliverybranchid=? and deliverystate=? and state=1 ";
		if (deliveryid > 0) {
			sql += " and deliveryid=" + deliveryid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, deliverystate);
	}

	/**
	 * 小件员的派件量
	 *
	 * @return
	 */
	public List<JSONObject> getDeliveryStasticsByUserId(long branchId, String starttime, String endtime) {
		String sql = "SELECT u.realname,ds.deliveryid,COUNT(1) AS num,sum(case when deliverystate in(1,2,3) then 1 else 0 end) as success,"
				+ "sum(case when deliverystate in(4) then 1 else 0  end) as jushou," + "sum(case when deliverystate in(5) then 1 else 0  end) as bufenjushou,"
				+ "sum(case when deliverystate in(6) then 1 else 0  end) as zhiliu " + "sum(case when deliverystate in(8) then 1 else 0  end) as diushi " + " FROM express_ops_delivery_state ds "
				+ "LEFT JOIN express_set_user u ON ds.deliveryid=u.userid WHERE ds.deliverybranchid=?  and createtime between ? and ? AND ds.state=1 GROUP BY ds.deliveryid";

		return this.jdbcTemplate.query(sql, new DeliverByDeliveryStateWeixin(), branchId, starttime, endtime);

	}

	private final class DeliverByDeliveryStateWeixin implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("deliveryid", rs.getString("deliveryid"));
			reJson.put("realname", rs.getString("realname"));
			reJson.put("num", rs.getString("num"));
			reJson.put("success", rs.getString("success"));
			reJson.put("jushou", rs.getString("jushou"));
			reJson.put("bufenjushou", rs.getString("bufenjushou"));
			reJson.put("zhiliu", rs.getString("zhiliu"));

			return reJson;
		}
	}

	/**
	 * 根据订单号修改操作人
	 *
	 * @param userid
	 * @param cwb
	 *
	 */
	public void updateDeliveryByCwb(long userid, String cwb) {
		String sql = "update express_ops_delivery_state set deliveryid=?,userid=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, userid, userid, cwb);

	}

	/**
	 * 根据订单号修改小件员
	 *
	 * @param userid
	 * @param cwb
	 *
	 */
	public void updateDeliveryidByCwb(long deliveryid, long branchid, String cwb) {
		String sql = "update express_ops_delivery_state set deliveryid=?,userid=?,deliverybranchid=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, deliveryid, deliveryid, branchid, cwb);

	}

	public DeliveryState getDeliverSignTime(String cwb) {
		String sql = "select d.* from express_ops_cwb_detail AS c  LEFT OUTER JOIN express_ops_delivery_state as d ON c.cwb=d.cwb  where c.state=1 and d.state =1 and c.cwb=?";
		return this.jdbcTemplate.queryForObject(sql, new DeliveryStateRowMapper(), cwb);
	}

	public List<DeliveryState> getDeliverystates(String cwbs) {
		String sql = "select * from express_ops_delivery_state where deliverystate=4 and cwb in(?)";

		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper(), cwbs);
	}

	public Map<String, Object> getDeliverystatesOfSalaryCounts(String deliveryid, String starttime, String endtime) {
		String sql = "select deliverystate,count(1) as counts from express_ops_delivery_state where deliveryid=? and deliverystate in(1,2,3) and gcaid>0 and deliverytime>=? and deliverytime<=? GROUP BY deliverystate";
		return this.jdbcTemplate.queryForMap(sql, new DeliverySalaryCountsMapper(), deliveryid, starttime, endtime);
	}

	private final class DeliverySalaryCountsMapper implements RowMapper<Map<Integer, Integer>> {
		@Override
		public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			map.put(rs.getInt("deliverystate"), rs.getInt("counts"));
			return map;
		}
	}

	public List<DeliveryState> findcwbByCwbsAndDateAndtypelike(String cwbs, String startdate, String enddate) {
		String sql = "select * from express_ops_delivery_state where cwb like '%"+cwbs+"%' and state=1  and deliverytime>='"+startdate+"' and deliverytime<='"+enddate +"'";
		List<DeliveryState> cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		return cwblist;
	}
	
	public List<DeliveryState> findcwbByCwbsAndDateAndtype(String cwbs, String startdate, String enddate) {
		String sql = "select * from express_ops_delivery_state where cwb in(" + cwbs + ") and state=1 and deliverytime>='"+startdate+"' and deliverytime<='"+enddate+"'";
		List<DeliveryState> cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		return cwblist;
	}

	public List<DeliveryState> findcwbByCwbsAndDateAndtypeByPage(String cwbs, String startdate, String enddate,int start,int number) {
		String sql = "select * from express_ops_delivery_state where cwb in(" + cwbs + ") and state=1 and deliverytime>='"+startdate+"' and deliverytime<='"+enddate+"' limit "+start+","+number;
		List<DeliveryState> cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		return cwblist;
	}
	
	public List<DeliveryState> findcwbByCwbsAndDateAndtypeShenHeByPage(String cwbs, String startdate, String enddate,int start,int number) {
		String sql = "select * from express_ops_delivery_state where cwb in(" + cwbs + ") and state=1 and auditingtime>='"+startdate+"' and auditingtime<='"+enddate+"' limit "+start+","+number;
		List<DeliveryState> cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		return cwblist;
	}

	public List<DeliveryState> findcwbByCwbsAndDateAndtypeShenHe(String cwbs, String startdate, String enddate) {
		String sql = "select * from express_ops_delivery_state where cwb in(" + cwbs + ") and state=1 and auditingtime>='"+startdate+"' and auditingtime<='"+enddate+"'";
		List<DeliveryState> cwblist;
		try {
			cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return cwblist;
	}

	/**
	 * 配送员派费账单 生成时所指定的订单(反馈日期)
	 */
	public List<DeliveryState> queryOrderByDeliveryid(Integer site, Integer orderType, Integer diliverymanid, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM express_ops_delivery_state  where 1=1");
		if (site != null) {
			sql.append(" and deliverybranchid = '" + site + "'");
		}
		if ((orderType != null) && (orderType != 0)) {
			sql.append(" and cwbordertypeid= '" + orderType + "'");
		}
		if (diliverymanid != null) {
			sql.append(" and deliveryid='" + diliverymanid + "'");
		}
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			sql.append(" and deliverytime >= '" + startDate + " 00:00:00'");
			sql.append(" and deliverytime <= '" + endDate + " 59:59:59'");
		}
		sql.append(" and gcaid > 0");
		sql.append(" and whethergeneratedeliverymanbill = 0");
		sql.append(" and deliverystate in ('1','2','3');");
		return this.jdbcTemplate.query(sql.toString(), new DeliveryStateRowMapper());
	}

	/**
	 * 配送员派费账单 生成时所指定的订单(发货日期)
	 */
	public List<DeliveryState> queryOrderByFaHuo(Integer site, Integer orderType, Integer diliverymanid, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM express_ops_delivery_state d,express_ops_cwb_detail c where d.cwb = c.cwb");
		if (site != null) {
			sql.append(" and d.deliverybranchid = '" + site + "'");
		}
		if ((orderType != null) && (orderType != 0)) {
			sql.append(" and d.cwbordertypeid= '" + orderType + "'");
		}
		if (diliverymanid != null) {
			sql.append(" and d.deliveryid='" + diliverymanid + "'");
		}
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			sql.append(" and c.emaildate >= '" + startDate + " 00:00:00'");
			sql.append(" and c.emaildate <= '" + endDate + " 59:59:59'");
		}
		sql.append(" and d.gcaid >0");
		sql.append(" and d.whethergeneratedeliverymanbill = 0");
		sql.append(" and d.deliverystate  in ('1','2','3');");
		return this.jdbcTemplate.query(sql.toString(), new DeliveryStateRowMapper());
	}

	/**
	 * 配送员派费账单 生成时所指定的订单(入库日期)
	 */
	public List<DeliveryState> queryOrderByRuKu(Integer site, Integer orderType, Integer diliverymanid, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM express_ops_delivery_state d,express_ops_order_intowarhouse i where d.cwb = i.cwb");
		if (site != null) {
			sql.append(" and d.deliverybranchid = '" + site + "'");
		}
		if ((orderType != null) && (orderType != 0)) {
			sql.append(" and d.cwbordertypeid= '" + orderType + "'");
		}
		if (diliverymanid != null) {
			sql.append(" and d.deliveryid='" + diliverymanid + "'");
		}
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			sql.append(" and i.credate >= '" + startDate + " 00:00:00'");
			sql.append(" and i.credate <= '" + endDate + " 59:59:59'");
		}
		sql.append(" and d.gcaid >0");
		sql.append(" and d.whethergeneratedeliverymanbill = 0");
		sql.append(" and d.deliverystate in ('1','2','3');");
		return this.jdbcTemplate.query(sql.toString(), new DeliveryStateRowMapper());
	}

	/**
	 * 将已经生成过配送员账单的订单标识字段改为1
	 */
	public void setWhetherGenerateDeliveryManBill(String cwbs) {
		String sql = "update express_ops_delivery_state set whethergeneratedeliverymanbill = 1 where cwb in (" + cwbs + ")";
		this.jdbcTemplate.update(sql);
	}
	
	/**
	 * 将生成过配送员账单的订单移除以后更改标识字段改为0
	 */
	public void setWhetherGenerateDeliveryBill(String cwbs) {
		String sql = "update express_ops_delivery_state set whethergeneratedeliverymanbill = 0 where cwb in (" + cwbs + ")";
		this.jdbcTemplate.update(sql);
	}
	
	//配送成功，上门退成功，上门换成功总量（某个小件员某个时间段内）
	public long getDeliverysCountBytime(String starttime,String endtime,long userid){
		String sql = "select count(1) from express_ops_delivery_state where deliveryid=? and auditingtime>='"+starttime+" 00:00:00' and auditingtime<='"+endtime+" 23:59:59' and deliverystate in(1,2,3) and  state=1";
		return this.jdbcTemplate.queryForLong(sql,userid);
	}
	//配送成功，上门退成功，上门换成功总量（某个小件员某个时间段内）
	public List<DeliveryState> getDeliverysBytime(String starttime,String endtime,long userid){
		String sql = "select * from express_ops_delivery_state where deliveryid=? and auditingtime>= '"+starttime+" 00:00:00' and auditingtime<='"+endtime+" 23:59:59' and deliverystate in(1,2,3) and state=1";
		return this.jdbcTemplate.query(sql,new DeliveryStateRowMapper(),userid);
	}
	public List<DeliveryState> findcwbByCwbsAndDateAndtypeShenHelike(
			String cwbs, String startdate, String enddate) {
		String sql = "select * from express_ops_delivery_state where cwb like '%"+cwbs+"%' and state=1 and auditingtime>='"+startdate+"' and auditingtime<='"+enddate+"'";
		List<DeliveryState> cwblist = this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
		return cwblist;
	}

	public List<Smtcount> getShenheCount(long userid, String starttime,
			String endtime) {
		try{
			String sql = "select customerid,count(1) as pscount  from express_ops_delivery_state where deliveryid=? and auditingtime>='"+starttime+" 00:00:00' and auditingtime<='"+endtime+" 23:59:59' and deliverystate in(1,2,3) and state=1  group by customerid";
			return this.jdbcTemplate.query(sql,new OrderCwbByCustomerIdMapper(),userid);
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	public List<Smtcount> getLinghuocwbs(long userid, String starttime,
			String endtime) {
		try{
			String sql = "select customerid,count(1) as pscount from express_ops_delivery_state where deliveryid=? and createtime>='"+starttime+" 00:00:00' and createtime<='"+endtime+" 23:59:59' and state=1 group by customerid";
			return this.jdbcTemplate.query(sql,new OrderCwbByCustomerIdMapper(),userid);
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	
	//配送成功，上门退成功，上门换成功总量（某个小件员某个时间段内）
	public List<DeliveryState> getDeliveryssBytime(String starttime,String endtime,long userid){
		String sql = "select * from express_ops_delivery_state where deliveryid=? and auditingtime>='"+starttime+" 00:00:00' and auditingtime<='"+endtime+" 23:59:59' and deliverystate in(1,2,3) and state=1";
		return this.jdbcTemplate.query(sql,new DeliveryStateRowMapper(),userid);
	}

	public List<DeliveryState> getLinghuoDeliveryStates(String starttime,
			String endtime, long userid) {
		try{
			String sql = "select * from express_ops_delivery_state where deliveryid=? and createtime>='"+starttime+" 00:00:00' and createtime<='"+endtime+" 23:59:59' and state=1";
			return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper(),userid);
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	
	public List<DeliveryState> getCwbOrderByShenHeDate(long customerid,String start,String end,String type){
		String sql="SELECT * FROM express_ops_delivery_state WHERE deliverystate IN(1,2,3,4) AND `auditingtime` >='"+start+"' AND auditingtime <='"+end+"' AND customerid="+customerid+" AND state=1";
		if(Integer.valueOf(type)>0){
			sql+=" AND cwbordertypeid =1";
		}
		return this.jdbcTemplate.query(sql,  new DeliveryStateRowMapper());
	}
	
	public List<DeliveryState> getCwbOrderByFanKuiDate(long customerid,String start,String end,String type){
		String sql="SELECT * FROM express_ops_delivery_state WHERE deliverystate IN(1,2,3,4) AND `deliverytime` >='"+start+"' AND deliverytime <='"+end+"' AND customerid="+customerid+" AND state=1";
		if(Integer.valueOf(type)>0){
			sql+=" AND `cwbordertypeid` =1";
		}
		return this.jdbcTemplate.query(sql,  new DeliveryStateRowMapper());
	}
	public List<DeliveryState> getCwbOrderByShenHeDate(long customerid,String start,String end,String type,int spage,int pageSize){
		String sql="SELECT * FROM express_ops_delivery_state WHERE deliverystate IN(1,2,3,4) AND `auditingtime` >='"+start+"' AND auditingtime <='"+end+"' AND customerid="+customerid+" AND state=1";
		if(Integer.valueOf(type)>0){
			sql+=" AND `cwbordertypeid` =1";
		}
		sql+=" limit "+spage+","+pageSize;
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}
	
	public List<DeliveryState> getCwbOrderByFanKuiDate(long customerid,String start,String end,String type,int spage,int pageSize){
		String sql="SELECT * FROM express_ops_delivery_state WHERE deliverystate IN(1,2,3,4) AND `deliverytime` >='"+start+"' AND deliverytime <='"+end+"' AND customerid="+customerid+" AND state=1";
		if(Integer.valueOf(type)>0){
			sql+=" AND `cwbordertypeid` =1";
		}
		sql+=" limit "+spage+","+pageSize;
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}
	
	/**
	 * 更新反馈站点id字段
	 * @author leo01.liao
	 * @param cwb 订单号
	 * @param deliverybranchid 反馈站点id
	 */
	public void updateDeliverybranchid(String cwb, long deliverybranchid){
		String sql = "update express_ops_delivery_state set deliverybranchid=? where cwb='" + cwb + "' and state=1";
		this.jdbcTemplate.update(sql, deliverybranchid);
	}
	
	/**
	 * 更新deliverystate的值
	 * @param cwb 订单号
	 * @param deliverystate 配送状态
	 * @author neo01.huang
	 */
	public void updateDeliveryStateValue(String cwb, int deliverystate){
		String sql = "update express_ops_delivery_state set deliverystate=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, deliverystate, cwb);
	}
	/**
	 * 
	 * 根据运单号集合，找最后一条反馈信息
	 * @author 刘武强
	 * @date:2016年7月22日 下午5:52:14 
	 * @params:@param cwb
	 * @params:@return
	 */
	public List<DeliveryState> getLastDeliveryStateByCwbs(String cwbs) {
		String sql = "select * from express_ops_delivery_state where  state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new DeliveryStateRowMapper());
	}
	
	/**
	 * 查询交款单
	 * @author chunlei05.li
	 * @date 2016年8月29日 下午12:18:20
	 * @param deliveryId
	 * @param auditingtimeStart
	 * @param auditingtimeEnd
	 * @param paymentType
	 * @return
	 */
	public List<DeliveryPayment> getDeliveryPaymentList(long deliveryId, String auditingtimeStart,
			String auditingtimeEnd, int paymentType) {
		StringBuilder sql = new StringBuilder("SELECT ds.*,"
				+ " cd.receivablefee cwbreceivablefee,"
				+ " cd.paybackfee cwbpaybackfee"
				+ " FROM"
				+ " express_ops_delivery_state ds,"
				+ " express_ops_cwb_detail cd,"
				+ " express_ops_goto_class_auditing ca"
				+ " WHERE ds.gcaid = ca.id"
				+ " AND ds.cwb = cd.cwb"
				+ " AND ds.sign_typeid = 1"
				+ " AND ds.deliveryid = " + deliveryId);
		
		//审核时间
		if (StringUtils.isNotBlank(auditingtimeStart)) {
			sql.append(" AND ca.auditingtime >= '").append(auditingtimeStart).append("'");
		}
		if (StringUtils.isNotBlank(auditingtimeEnd)) {
			sql.append(" AND ca.auditingtime <= '").append(auditingtimeEnd).append("'");
		}
		// 支付方式
		// 根据产品确认，武汉不存在一个订单只存在一种支付类型的订单
		// 若存在一个订单存在多种支付类型，则统计表报会有误差
		if (paymentType == DeliveryPaymentPatternEnum.CASH.getPayno()) {
			// 默认支付方式为现金
			sql.append(" AND ds.pos = 0 AND ds.checkfee = 0 AND ds.codpos = 0 AND ds.otherfee = 0");
		} else if (paymentType == DeliveryPaymentPatternEnum.POS.getPayno()) {
			sql.append(" AND ds.pos > 0");
		} else if (paymentType == DeliveryPaymentPatternEnum.CHECKFEE.getPayno()) {
			sql.append(" AND ds.checkfee > 0");
		} else if (paymentType == DeliveryPaymentPatternEnum.CODPOS.getPayno()) {
			sql.append(" AND ds.codpos > 0");
		} else if (paymentType == DeliveryPaymentPatternEnum.OTHERFEE.getPayno()) {
			sql.append(" AND ds.otherfee > 0");
		}
		List<DeliveryPayment> deliveryPaymentList =  this.jdbcTemplate.query(sql.toString(), new DeliveryPaymentRowMapper());
		if (deliveryPaymentList == null) {
			deliveryPaymentList = new ArrayList<DeliveryPayment>();
		}
		return deliveryPaymentList;
	}
	
	/**
	 * 根据小件员、客户、订单类型、支付方式查询交款单
	 * @author chunlei05.li
	 * @date 2016年8月29日 下午5:07:59
	 * @param deliveryId
	 * @param customerIds
	 * @param cwbOrderTypeIds
	 * @param deliveryPaymentPatternIds
	 * @param auditingtimeStart
	 * @param auditingtimeEnd
	 * @return
	 */
	public List<DeliveryPayment> getDeliveryPaymentList(long deliveryId, long[] customerIds, int[] cwbOrderTypeIds,
			int[] deliveryPaymentPatternIds, String auditingtimeStart, String auditingtimeEnd) {
		StringBuilder sql = new StringBuilder("SELECT ds.*,"
				+ " cd.receivablefee cwbreceivablefee,"
				+ " cd.paybackfee cwbpaybackfee"
				+ " FROM"
				+ " express_ops_delivery_state ds,"
				+ " express_ops_cwb_detail cd,"
				+ " express_ops_goto_class_auditing ca"
				+ " WHERE ds.gcaid = ca.id"
				+ " AND ds.cwb = cd.cwb"
				+ " AND ds.sign_typeid = 1"
				+ " AND ds.deliveryid = " + deliveryId
				+ " AND ds.customerid IN (" + StringUtil.toDbInStr(customerIds) + ")"
				+ " AND ds.cwbordertypeid IN (" + StringUtil.toDbInStr(cwbOrderTypeIds) + ")");
		// 审核时间
		if (StringUtils.isNotBlank(auditingtimeStart)) {
			sql.append(" AND ca.auditingtime >= '").append(auditingtimeStart).append("'");
		}
		if (StringUtils.isNotBlank(auditingtimeEnd)) {
			sql.append(" AND ca.auditingtime <= '").append(auditingtimeEnd).append("'");
		}
		// 支付方式
		// 根据产品确认，武汉不存在一个订单只存在一种支付类型的订单
		// 若存在一个订单存在多种支付类型，则统计表报会有误差
		StringBuilder paymentTypeSql = new StringBuilder();
		for (long paymentType : deliveryPaymentPatternIds) {
			if (paymentType == DeliveryPaymentPatternEnum.CASH.getPayno()) {
				// 默认支付方式为现金
				paymentTypeSql.append(" OR (ds.pos = 0 AND ds.checkfee = 0 AND ds.codpos = 0 AND ds.otherfee = 0)");
			} else if (paymentType == DeliveryPaymentPatternEnum.POS.getPayno()) {
				paymentTypeSql.append(" OR ds.pos > 0");
			} else if (paymentType == DeliveryPaymentPatternEnum.CHECKFEE.getPayno()) {
				paymentTypeSql.append(" OR ds.checkfee > 0");
			} else if (paymentType == DeliveryPaymentPatternEnum.CODPOS.getPayno()) {
				paymentTypeSql.append(" OR ds.codpos > 0");
			} else if (paymentType == DeliveryPaymentPatternEnum.OTHERFEE.getPayno()) {
				paymentTypeSql.append(" OR ds.otherfee > 0");
			}
		}
		if (paymentTypeSql.length() > 0) {
			sql.append(" AND (").append(paymentTypeSql.substring(4)).append(")");
		} else { // 如果匹配不到传入的支付方式，则查询结果为空
			return new ArrayList<DeliveryPayment>();
		}
		List<DeliveryPayment> deliveryPaymentList =  this.jdbcTemplate.query(sql.toString(), new DeliveryPaymentRowMapper());
		if (deliveryPaymentList == null) {
			deliveryPaymentList = new ArrayList<DeliveryPayment>();
		}
		return deliveryPaymentList;
	}
	
	/**
	 * 根据订单查询最近的一条归班审核的记录  add by bruce shangguan 20160831
	 * @param cwb
	 * @param deliveryState
	 * @return
	 */
	public DeliveryState findDeliveryStateLastAuditingTime(String cwb , int deliveryState){
		try{
			StringBuffer sql = new StringBuffer("SELECT * FROM express_ops_delivery_state ") ;
			sql.append(" WHERE cwb = ? ") ;
			sql.append(" AND deliverystate = ? ") ;
			sql.append(" and state = 1 ") ;
			sql.append(" ORDER BY auditingtime DESC LIMIT 0,1 ") ;
			DeliveryState ob = this.jdbcTemplate.queryForObject(sql.toString(),new DeliveryStateRowMapper() , cwb , deliveryState);
			return ob;
		}catch(Exception ee){
			ee.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据订单号pjd签收人签名图片地址
	 *
	 * @param cwb,imgUrl
	 * @return
	 */
	public int saveSignImgByCwb(String cwb,String imgUrl) {
		String sql = "update express_ops_delivery_state set sign_img=? where cwb=? and state=1";
		return this.jdbcTemplate.update(sql,imgUrl,cwb);		
	}
}
