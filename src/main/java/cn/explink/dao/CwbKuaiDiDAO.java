package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.CwbKuaiDi;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.Page;
import cn.explink.util.SecurityUtil;
import cn.explink.util.StringUtil;

@Component
public class CwbKuaiDiDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CwbKuaiDiMapper implements RowMapper<CwbKuaiDi> {

		@Override
		public CwbKuaiDi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbKuaiDi cwbKuaiDi = new CwbKuaiDi();
			cwbKuaiDi.setId(rs.getLong("id"));
			cwbKuaiDi.setCwb(rs.getString("cwb"));
			cwbKuaiDi.setAllfee(rs.getBigDecimal("allfee"));
			cwbKuaiDi.setConsigneeareacode(StringUtil.nullConvertToEmptyString(rs.getString("consigneeareacode")));
			cwbKuaiDi.setEdittime(StringUtil.nullConvertToEmptyString(rs.getString("edittime")));
			cwbKuaiDi.setEdituserid(rs.getLong("edituserid"));
			cwbKuaiDi.setFuturefee(rs.getBigDecimal("futurefee"));
			cwbKuaiDi.setLanshoubranchid(rs.getLong("lanshoubranchid"));
			cwbKuaiDi.setLanshouuserid(rs.getLong("lanshouuserid"));
			cwbKuaiDi.setNowfee(rs.getBigDecimal("nowfee"));
			cwbKuaiDi.setOtherfee(rs.getBigDecimal("otherfee"));
			cwbKuaiDi.setPackagefee(rs.getBigDecimal("packagefee"));
			cwbKuaiDi.setPackingfee(rs.getBigDecimal("packingfee"));
			cwbKuaiDi.setPaisongbranchid(rs.getLong("paisongbranchid"));
			cwbKuaiDi.setPaisongtime(StringUtil.nullConvertToEmptyString(rs.getString("paisongtime")));
			cwbKuaiDi.setPaisonguserid(rs.getLong("paisonguserid"));
			cwbKuaiDi.setPaytype(rs.getLong("paytype"));
			cwbKuaiDi.setLanshoutime(StringUtil.nullConvertToEmptyString(rs.getString("lanshoutime")));
			cwbKuaiDi.setSafefee(rs.getBigDecimal("safefee"));
			cwbKuaiDi.setSendconsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneeaddress")));
			cwbKuaiDi.setSendconsigneeareacode(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneeareacode")));
			cwbKuaiDi.setSendconsigneecity(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneecity")));
			cwbKuaiDi.setSendconsigneecompany(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneecompany")));
			cwbKuaiDi.setSendconsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneemobile")));
			cwbKuaiDi.setSendconsigneename(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneename")));
			cwbKuaiDi.setSendconsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneephone")));
			cwbKuaiDi.setSendconsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneepostcode")));
			cwbKuaiDi.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			cwbKuaiDi.setInsuredrate(rs.getBigDecimal("insuredrate"));
			cwbKuaiDi.setRealweight(rs.getBigDecimal("realweight"));
			cwbKuaiDi.setSignman(StringUtil.nullConvertToEmptyString(rs.getString("signman")));
			cwbKuaiDi.setSignstate(rs.getLong("signstate"));
			cwbKuaiDi.setSigntime(StringUtil.nullConvertToEmptyString(rs.getString("signtime")));
			cwbKuaiDi.setTransitfee(rs.getBigDecimal("transitfee"));
			cwbKuaiDi.setWeightfee(rs.getBigDecimal("weightfee"));
			cwbKuaiDi.setZhongzhuanbranchid(rs.getLong("zhongzhuanbranchid"));
			cwbKuaiDi.setShoujianrencompany(rs.getString("shoujianrencompany"));
			cwbKuaiDi.setFlowordertype(rs.getLong("flowordertype"));
			cwbKuaiDi.setCustomercode(rs.getString("customercode"));
			return cwbKuaiDi;
		}
	}

	private final class ExpressMapper implements RowMapper<CwbKuaiDi> {
		@Override
		public CwbKuaiDi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbKuaiDi cwbKuaiDi = new CwbKuaiDi();
			cwbKuaiDi.setCwb(rs.getString("cwb"));
			cwbKuaiDi.setAllfee(rs.getBigDecimal("allfee"));
			cwbKuaiDi.setLanshoubranchid(rs.getLong("lanshoubranchid"));
			cwbKuaiDi.setLanshouuserid(rs.getLong("lanshouuserid"));
			cwbKuaiDi.setPaytype(rs.getLong("paytype"));
			cwbKuaiDi.setLanshoutime(StringUtil.nullConvertToEmptyString(rs.getString("lanshoutime")));
			cwbKuaiDi.setSendconsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneeaddress")));
			cwbKuaiDi.setSendconsigneecompany(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneecompany")));
			cwbKuaiDi.setSendconsigneemobile(SecurityUtil.getInstance().decrypt(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneemobile"))));
			cwbKuaiDi.setSendconsigneename(StringUtil.nullConvertToEmptyString(rs.getString("sendconsigneename")));
			cwbKuaiDi.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			cwbKuaiDi.setRealweight(rs.getBigDecimal("realweight"));
			cwbKuaiDi.setTransitfee(rs.getBigDecimal("transitfee"));
			cwbKuaiDi.setPackagefee(rs.getBigDecimal("packagefee"));
			cwbKuaiDi.setInsuredrate(rs.getBigDecimal("insuredfee"));
			cwbKuaiDi.setShoujianrencompany(rs.getString("shoujianrencompany"));
			cwbKuaiDi.setFlowordertype(rs.getLong("flowordertype"));
			cwbKuaiDi.setCustomercode(rs.getString("customercode"));
			cwbKuaiDi.setPaisongbranchid(rs.getLong("deliverybrach"));
			cwbKuaiDi.setReceivablefee(rs.getBigDecimal("receivablefee"));
			return cwbKuaiDi;
		}
	}

	public CwbKuaiDi getCwbByCwbLock(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from ops_cwbkuaidi_detail where cwb=? and state=1 for update", new CwbKuaiDiMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void saveCwbKuaiDiPaiSongByCwb(CwbKuaiDi cwbKuaiDi) {
		String sql = "update ops_cwbkuaidi_detail set paisongbranchid=?,paisonguserid=?,paisongtime=?  where cwb=?";
		this.jdbcTemplate.update(sql, cwbKuaiDi.getPaisongbranchid(), cwbKuaiDi.getPaisonguserid(), cwbKuaiDi.getPaisongtime(), cwbKuaiDi.getCwb());
	}

	public void saveCwbKuaiDiZhongZhuanByCwb(long zhongzhuanbranchid, String cwb) {
		String sql = "update ops_cwbkuaidi_detail set zhongzhuanbranchid=?  where cwb=?";
		this.jdbcTemplate.update(sql, zhongzhuanbranchid, cwb);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<CwbKuaiDi> getCwbKuaiDiListPage(long page, long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		String sql = "SELECT * from ops_cwbkuaidi_detail where ";
		if (timeType == 1) {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_LanshouTime_Idx)");
			sql += " lanshoutime >= '" + begindate + "' and lanshoutime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_PaiSongtime_Idx)");
			sql += " paisongtime >= '" + begindate + "'  and paisongtime <= '" + enddate + "' ";
		}
		sql = this.getCwbKuaiDiByPageWhereSql(sql, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbKuaiDiMapper());
	}

	// 快递订单查询
	@DataSource(DatabaseType.REPLICA)
	public List<CwbKuaiDi> getExpressOrderListPage(long page, long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		String sql = this.getQueryExpressSql(timeType, begindate, enddate, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ExpressMapper());
	}

	/**
	 * 快递单导出-查询所有
	 *
	 * @param timeType
	 * @param begindate
	 * @param enddate
	 * @param lanshoubranchids
	 * @param lanshouuserid
	 * @param paisongbranchids
	 * @param paisonguserid
	 * @return
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<CwbKuaiDi> getExpressOrderListNoPage(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		String sql = this.getQueryExpressSql(timeType, begindate, enddate, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		return this.jdbcTemplate.query(sql, new ExpressMapper());
	}

	@DataSource(DatabaseType.REPLICA)
	public List<CwbKuaiDi> getCwbKuaiDiListNoPage(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		String sql = "SELECT * from ops_cwbkuaidi_detail where ";
		if (timeType == 1) {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_LanshouTime_Idx)");
			sql += " lanshoutime >= '" + begindate + "'  and lanshoutime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_PaiSongtime_Idx)");
			sql += " paisongtime >= '" + begindate + "'  and paisongtime <= '" + enddate + "' ";
		}
		sql = this.getCwbKuaiDiByPageWhereSql(sql, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		return this.jdbcTemplate.query(sql, new CwbKuaiDiMapper());
	}

	@DataSource(DatabaseType.REPLICA)
	public long getCwbKuaiDiListCount(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		String sql = "SELECT count(1) from ops_cwbkuaidi_detail where ";
		if (timeType == 1) {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_LanshouTime_Idx)");
			sql += " lanshoutime >= '" + begindate + "'  and lanshoutime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_cwbkuaidi_detail", "ops_cwbkuaidi_detail FORCE INDEX(KD_PaiSongtime_Idx)");
			sql += " paisongtime >= '" + begindate + "'  and paisongtime <= '" + enddate + "' ";
		}
		sql = this.getCwbKuaiDiByPageWhereSql(sql, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getCwbKuaiDiByPageWhereSql(String sql, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		if ((lanshoubranchids.length() > 0) || (lanshouuserid > 0) || (paisongbranchids.length() > 0) || (paisonguserid > 0)) {
			StringBuffer w = new StringBuffer();
			if (lanshoubranchids.length() > 0) {
				w.append(" and lanshoubranchid in(" + lanshoubranchids + ")");
			}
			if (lanshouuserid > 0) {
				w.append(" and lanshouuserid = " + lanshouuserid);
			}
			if (paisongbranchids.length() > 0) {
				w.append(" and paisongbranchid in(" + paisongbranchids + ")");
			}
			if (paisonguserid > 0) {
				w.append(" and paisonguserid=" + paisonguserid);
			}
			sql += w.toString();
		}
		return sql;
	}

	// 4.2快递使用
	private String getExpressOrderByPageWhereSql(String sql, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		if ((lanshoubranchids.length() > 0) || (lanshouuserid > 0) || (paisongbranchids.length() > 0) || (paisonguserid > 0)) {
			StringBuffer w = new StringBuffer();
			if (lanshoubranchids.length() > 0) {
				w.append(" and cd.instationid in(" + lanshoubranchids + ")");
			}
			if (lanshouuserid > 0) {
				w.append(" and cd.collectorid = " + lanshouuserid);
			}
			if (paisongbranchids.length() > 0) {
				w.append(" and cd.deliverybranchid in(" + paisongbranchids + ")");
			}
			if (paisonguserid > 0) {
				w.append(" and ds.deliveryid=" + paisonguserid);
			}

			sql += w.toString();
		}
		//sql = sql + " and cd.state=1 and ds.state=1 and  cd.cwbordertypeid = " + CwbOrderTypeIdEnum.Express.getValue();
		sql = sql + " and cd.state=1 and  cd.cwbordertypeid = " + CwbOrderTypeIdEnum.Express.getValue();
		return sql;
	}

	// 4.2快递使用
	@DataSource(DatabaseType.REPLICA)
	public long getExpressOrderListCount(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		/**
		String sql =  this.getQueryExpressSql(timeType, begindate, enddate, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		List<CwbKuaiDi> list = this.jdbcTemplate.query(sql, new ExpressMapper());
		return list.size();
		**/
		
		String sql = this.getCountForQueryExpressSql(timeType, begindate, enddate, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 根据订单号 查询 多个
	 *
	 * @param string
	 * @return
	 */
	public List<CwbKuaiDi> getCwbKuaiDiByCwbs(String cwbs) {
		String sql = " select * from ops_cwbkuaidi_detail where cwb in(" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new CwbKuaiDiMapper());
	}

	/**
	 * 根据订单号 查询
	 *
	 * @param cwb
	 * @return
	 */
	public CwbKuaiDi getCwbKuaiDiByCwb(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from ops_cwbkuaidi_detail where cwb=? limit 0,1", new CwbKuaiDiMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	/**
	 * 更新
	 *
	 * @param kd
	 */
	public void updateKuDi(final CwbKuaiDi kd) {
		String sql = "update ops_cwbkuaidi_detail set sendconsigneename=?,sendconsigneecompany=?," + "sendconsigneeareacode=?,sendconsigneecity=?,sendconsigneeaddress=?,"
				+ "sendconsigneemobile=?,sendconsigneephone=?,sendconsigneepostcode=?," + "paytype=?,consigneeareacode=?,packingfee=?,insuredrate=?,safefee=?,transitfee=?,"
				+ "nowfee=?,futurefee=?,weightfee=?,realweight=?,packagefee=?,otherfee=?," + "allfee=?,remark=?,signstate=?,signman=?,signtime=?,edituserid=?,edittime=?,"
				+ "zhongzhuanbranchid=?,shoujianrencompany=? where cwb=?";

		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, kd.getSendconsigneename());
				ps.setString(2, kd.getSendconsigneecompany());
				ps.setString(3, kd.getSendconsigneeareacode());
				ps.setString(4, kd.getSendconsigneecity());
				ps.setString(5, kd.getSendconsigneeaddress());
				ps.setString(6, kd.getSendconsigneemobile());
				ps.setString(7, kd.getSendconsigneephone());
				ps.setString(8, kd.getSendconsigneepostcode());
				ps.setLong(9, kd.getPaytype());
				ps.setString(10, kd.getConsigneeareacode());
				ps.setBigDecimal(11, kd.getPackingfee());
				ps.setBigDecimal(12, kd.getInsuredrate());
				ps.setBigDecimal(13, kd.getSafefee());
				ps.setBigDecimal(14, kd.getTransitfee());
				ps.setBigDecimal(15, kd.getNowfee());
				ps.setBigDecimal(16, kd.getFuturefee());
				ps.setBigDecimal(17, kd.getWeightfee());
				ps.setBigDecimal(18, kd.getRealweight());
				ps.setBigDecimal(19, kd.getPackagefee());
				ps.setBigDecimal(20, kd.getOtherfee());
				ps.setBigDecimal(21, kd.getAllfee());
				ps.setString(22, kd.getRemark());
				ps.setLong(23, kd.getSignstate());
				ps.setString(24, kd.getSignman());
				ps.setString(25, kd.getSigntime());
				ps.setLong(26, kd.getEdituserid());
				ps.setString(27, kd.getEdittime());
				ps.setLong(28, kd.getZhongzhuanbranchid());
				ps.setString(29, kd.getShoujianrencompany());
				ps.setString(30, kd.getCwb());
			}
		});

	}

	/**
	 * 快速补录
	 *
	 * @param cwb
	 * @param signman
	 * @param signstate
	 * @param signtime
	 * @param remark
	 * @param userid
	 * @param nowTime
	 */
	public void updateKuDiQuick(String cwb, String signman, long signstate, String signtime, String remark, long userid, String nowTime) {
		String sql = " update ops_cwbkuaidi_detail set remark=?,signstate=?,signman=?,signtime=?,edituserid=?," + "edittime=? where cwb=?";
		this.jdbcTemplate.update(sql, remark, signstate, signman, signtime, userid, nowTime, cwb);
	}

	private String getQueryExpressSql(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		//将客户编号的取数逻辑改为从客户表里面取，而不是从主表里面----刘武强20160627
		//将配送站点取数逻辑改为从主表去，而不是从归班反馈表取---梁宇荣 20160730
		String sql = "SELECT DISTINCT cd.cwb as cwb, " + " cd.collectorid as lanshouuserid, " + " cd.instationid as lanshoubranchid, " + " cd.credate as lanshoutime, "
				+ " cd.consigneename as sendconsigneename, " + " cd.consigneemobile as sendconsigneemobile, " + " cd.consigneeaddress as sendconsigneeaddress, " + " cd.shouldfare as transitfee, "
				+ " cd.totalfee as allfee, " + " cd.flowordertype as flowordertype, " + " cd.cwbremark as remark, " + " cd.paymethod as paytype, " + " ci.customercode as customercode, "
				+ " cd.customerid as sendconsigneecompany, " + " cd.reccustomerid as shoujianrencompany, " + " cd.carrealweight as realweight,cd.packagefee as packagefee,cd.insuredfee as insuredfee, "
				+ " cd.deliverybranchid as deliverybrach, " + " cd.receivablefee as receivablefee"
				+ " from express_ops_cwb_detail as cd ";
		sql = sql + " left join express_ops_delivery_state as ds on (cd.cwb =ds.cwb and ds.state=1 ) ";
		sql = sql + " left join express_set_customer_info as ci on cd.customerid =ci.customerid ";

		//将反馈表和主表都加上state=1的条件，防止重复的计算同一个快递单---刘武强20160628	
		if (timeType == 1) {
			sql += " where cd.credate >= '" + begindate + "' and cd.credate <= '" + enddate + "' ";
		} else if (timeType == 2) {
			sql += " where ds.deliverytime >= '" + begindate + "'  and ds.deliverytime <= '" + enddate + "' ";
		} else {
			sql += " where 1=1 ";
		}
		sql = this.getExpressOrderByPageWhereSql(sql, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		
		logger.info("CwbKuaiDiDAO.getQueryExpressSql:{}", sql);
		
		return sql;
	}

	/**
	 * 获取符合条件的记录总数
	 * @author leo01.liao
	 * @param timeType
	 * @param begindate
	 * @param enddate
	 * @param lanshoubranchids
	 * @param lanshouuserid
	 * @param paisongbranchids
	 * @param paisonguserid
	 * @return
	 */
	private String getCountForQueryExpressSql(long timeType, String begindate, String enddate, String lanshoubranchids, long lanshouuserid, String paisongbranchids, long paisonguserid) {
		//将客户编号的取数逻辑改为从客户表里面取，而不是从主表里面----刘武强20160627
		String sql = "SELECT count(DISTINCT cd.cwb) from express_ops_cwb_detail as cd ";
		sql = sql + " left join express_ops_delivery_state as ds on (cd.cwb =ds.cwb and ds.state=1) ";
		sql = sql + " left join express_set_customer_info as ci on cd.customerid =ci.customerid ";

		//将反馈表和主表都加上state=1的条件，防止重复的计算同一个快递单---刘武强20160628	
		if (timeType == 1) {
			sql += " where cd.credate >= '" + begindate + "' and cd.credate <= '" + enddate + "' ";
		} else if (timeType == 2) {
			sql += " where ds.deliverytime >= '" + begindate + "'  and ds.deliverytime <= '" + enddate + "' ";
		} else {
			sql += " where 1 = 1 ";
		}
		
		sql = this.getExpressOrderByPageWhereSql(sql, lanshoubranchids, lanshouuserid, paisongbranchids, paisonguserid);
		
		logger.info("CwbKuaiDiDAO.getCountForQueryExpressSql:{}", sql);
		
		return sql;
	}
}
