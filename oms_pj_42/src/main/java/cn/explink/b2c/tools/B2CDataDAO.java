package cn.explink.b2c.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Component
public class B2CDataDAO {
	@Autowired
	GetDmpDAO getDmpDAO;
	private Logger logger = LoggerFactory.getLogger(B2CDataDAO.class);

	private final class ComMapper implements RowMapper<B2CData> {
		@Override
		public B2CData mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2CData b2CData = new B2CData();
			b2CData.setB2cid(rs.getLong("b2cid"));
			b2CData.setCustomerid(rs.getLong("customerid"));
			b2CData.setFlowordertype(rs.getLong("flowordertype"));
			b2CData.setSend_b2c_flag(rs.getLong("send_b2c_flag"));
			b2CData.setCwb(rs.getString("cwb"));
			b2CData.setJsoncontent(rs.getString("jsoncontent"));
			b2CData.setPosttime(rs.getString("posttime"));
			b2CData.setShipcwb(rs.getString("shipcwb"));
			b2CData.setRemark(rs.getString("remark"));
			b2CData.setTimeout(rs.getLong("timeout"));
			b2CData.setTimeoutdate(rs.getString("timeoutdate"));
			b2CData.setDelId(rs.getLong("delId"));
			b2CData.setSend_payamount_flag(rs.getLong("send_payamount_flag"));
			return b2CData;
		}
	}

	private final class ComGroupMapper implements RowMapper<B2CData> {
		@Override
		public B2CData mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2CData b2CData = new B2CData();
			b2CData.setCustomerid(rs.getLong("customerid"));
			b2CData.setCwb(rs.getString("cwb"));
			return b2CData;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<B2CData> getB2cMonitorDataListByCwb(String cwb, long page) {
		String sql = "SELECT * FROM  express_send_b2c_data where cwb in (" + cwb + ") " + " ORDER BY b2cid ASC ,posttime ASC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	public Long getB2cMonitorDataListByCwbCount(String cwb) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where cwb in (" + cwb + ") ";

		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<B2CData> selectB2cMonitorDataList(String customerid, long page) {
		String sql = "SELECT * FROM  express_send_b2c_data where send_b2c_flag in(0,2)  ";
		sql += this.whereSqlByB2cMonitorData(customerid);
		if (page > 0) {
			sql += " ORDER BY cwb ASC ,posttime ASC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " ORDER BY cwb ASC ,posttime ASC ";
		}

		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	public String selectB2cMonitorDataListSql(String cwb, long customerid, long flowordertypeid, String starttime, String endtime) {
		String sql = "SELECT * FROM  express_send_b2c_data where  1=1  ";
		sql = this.creContent(cwb, customerid, flowordertypeid, starttime, endtime, sql);
		sql += " ORDER BY cwb ASC ,posttime ASC ";

		return sql;
	}

	public List<String> selectB2cMonitorDataList(String cwb, String customerid, long flowordertype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime) {
		String sql = "SELECT * FROM  express_send_b2c_data where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(customerid);
		sql += " ORDER BY cwb ASC ,posttime ASC ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<B2CData> selectB2cMonitorDataListGroup(String cwb, String customerid, long flowordertype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime) {
		String sql = "SELECT customerid,GROUP_CONCAT(DISTINCT cwb) as cwb FROM  express_send_b2c_data where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(customerid);
		sql += " group by customerid ";
		return this.jdbcTemplate.query(sql, new ComGroupMapper());
	}

	public long selectB2cMonitorDataCount(String customerid) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where  send_b2c_flag in(0,2)  ";
		sql += this.whereSqlByB2cMonitorData(customerid);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String whereSqlByB2cMonitorData(String customerid) {
		String sql = "";
		if (!"0".equals(customerid)) {
			sql += " and customerid='" + customerid + "' ";
		}
		return sql;
	}

	public void saveB2CData(final B2CData b2CData) {
		this.jdbcTemplate.update("insert into express_send_b2c_data(customerid,flowordertype," + "send_b2c_flag,cwb,jsoncontent,posttime) " + "values(?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, b2CData.getCustomerid());
				ps.setLong(2, b2CData.getFlowordertype());
				ps.setLong(3, b2CData.getSend_b2c_flag());
				ps.setString(4, b2CData.getCwb());
				ps.setString(5, b2CData.getJsoncontent());
				ps.setString(6, b2CData.getPosttime());
			}
		});
	}

	/**
	 * 能满足所有的对接插入数据的入库
	 *
	 * @param b2CData
	 */
	public void saveB2CData(final B2CData b2CData, String multi_shipcwb) {// tmall不为空，其他为空
		if (multi_shipcwb == null) {
			this.jdbcTemplate.update(
					"insert into express_send_b2c_data(customerid,flowordertype," + "send_b2c_flag,cwb,jsoncontent,posttime,timeout,timeoutdate,delId) " + "values(?,?,?,?,?,?,?,?,?)",
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							// TODO Auto-generated method stub
							ps.setLong(1, b2CData.getCustomerid());
							ps.setLong(2, b2CData.getFlowordertype());
							ps.setLong(3, b2CData.getSend_b2c_flag());
							ps.setString(4, b2CData.getCwb());
							ps.setString(5, b2CData.getJsoncontent());
							ps.setString(6, b2CData.getPosttime());
							ps.setLong(7, b2CData.getTimeout());
							ps.setString(8, b2CData.getTimeoutdate());
							ps.setLong(9, b2CData.getDelId());
						}
					});
		} else {
			if ((multi_shipcwb != null) && (multi_shipcwb.split(",").length > 0)) {
				for (final String shipcwb : multi_shipcwb.split(",")) {
					this.jdbcTemplate.update("insert into express_send_b2c_data(customerid,flowordertype," + "send_b2c_flag,cwb,jsoncontent,posttime,shipcwb,timeout,timeoutdate,delId) "
							+ "values(?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							// TODO Auto-generated method stub
							ps.setLong(1, b2CData.getCustomerid());
							ps.setLong(2, b2CData.getFlowordertype());
							ps.setLong(3, b2CData.getSend_b2c_flag());
							ps.setString(4, b2CData.getCwb());
							ps.setString(5, b2CData.getJsoncontent());
							ps.setString(6, b2CData.getPosttime());
							ps.setString(7, shipcwb);
							ps.setLong(8, b2CData.getTimeout());
							ps.setString(9, b2CData.getTimeoutdate());
							ps.setLong(10, b2CData.getDelId());
						}
					});
				}
			}
		}

	}

	public long check(String cwb, long flowtype) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where cwb='" + cwb + "' and flowordertype=" + flowtype + " ";
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 查询重发流程JMS的时候
	 *
	 * @param cwb
	 * @param flowtype
	 * @return
	 */
	public long checkIsRepeatDataFlag(String cwb, long flowtype, String posttime) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where cwb='" + cwb + "' and flowordertype=" + flowtype;
		if ((posttime != null) && !posttime.equals("")) {
			sql += " and posttime='" + posttime + "'";
		}

		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 查询是否有重复36状态且未推送的
	 *
	 * @param cwb
	 * @param flowtype
	 * @return
	 */
	public B2CData getB2cDataByKeys(String cwb, long flowtype, long send_b2c_flag) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag=" + send_b2c_flag + "  and cwb='" + cwb + "' and flowordertype=" + flowtype;
		sql += "  order by posttime,flowordertype LIMIT 0,1";

		try {
			return this.jdbcTemplate.queryForObject(sql, new ComMapper());
		} catch (DataAccessException e) {
			return null;
		}

	}

	// 修改推送失败的单子为 未推送
	public void updateSendFailedDataList(String customerids) {
		String updatesql = " update express_send_b2c_data set  posttime='" + DateTimeUtil.getNowTime()
				+ "',send_b2c_flag=0,select_b2c_flag=0  where send_b2c_flag=2 and select_b2c_flag=1 and customerid in (" + customerids + ")  ";
		try {
			this.jdbcTemplate.update(updatesql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 20121031 查询当前状态是否被推送过
	 *
	 * @return
	 */
	public boolean getCwbByB2cSendCount(String customerids, String cwb, long flowordertype) {
		String sql = "select count(1) from express_send_b2c_data where customerid in (" + customerids + ") and cwb='" + cwb + "' ";
		if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			sql += " and flowordertype in(" + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + " ) and send_b2c_flag in (1,2) ";
		} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			sql += " and flowordertype=" + FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "  and send_b2c_flag in (1,2)  ";
		}
		return this.jdbcTemplate.queryForInt(sql) > 0;
	}

	/**
	 * 根据条件查询出来数据 反馈给b2c
	 *
	 * @return
	 */
	public List<B2CData> getDataListByFlowStatus(long flowordertype, String customerids, long maxCount) {
		SystemInstall useAudit = this.getDmpDAO.getSystemInstallByName("useAudit");
		if ((useAudit != null) && "no".equals(useAudit.getValue())) {// 不需要归班
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			return this.getDataListByB2cSendByNotAudit(flowordertype, customerids, nowTime, maxCount);
		} else {
			String sql = "select * from express_send_b2c_data where send_b2c_flag=0  and  customerid in (" + customerids + ") ";
			if (flowordertype > 0) {
				sql += " and flowordertype=" + flowordertype;
			}
			sql += "  order by posttime,flowordertype LIMIT 0," + maxCount;

			this.logger.info("b2c-sql:" + sql);
			try {
				return this.jdbcTemplate.query(sql, new ComMapper());
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				this.logger.error("sql查询异常!", e);
				return null;
			}
		}

	}

	/**
	 * 根据条件查询出来数据 反馈给b2c
	 *
	 * @return
	 */
	public List<B2CData> getDataListByFlowStatusAndPosttime(long flowordertype, String customerids, long maxCount, String posttime, int type) {
		SystemInstall useAudit = this.getDmpDAO.getSystemInstallByName("useAudit");
		if ((useAudit != null) && "no".equals(useAudit.getValue())) {// 不需要归班
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			return this.getDataListByB2cSendByNotAudit(flowordertype, customerids, nowTime, maxCount);
		} else {
			String sql = "select * from express_send_b2c_data where send_b2c_flag=" + type + "  and  customerid in (" + customerids + ") and posttime<='" + posttime + "' ";
			if (flowordertype > 0) {
				sql += " and flowordertype=" + flowordertype;
			}
			sql += "  order by posttime LIMIT 0," + maxCount;

			this.logger.info("b2c-sql:" + sql);
			try {
				return this.jdbcTemplate.query(sql, new ComMapper());
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				this.logger.error("sql查询异常!", e);
				return null;
			}
		}

	}

	/**
	 * 查询所有未推送数据 反馈给b2c 不是分状态
	 *
	 * @return
	 */
	public List<B2CData> getDataListByFlowStatus(String customerids, long maxCount) {
		SystemInstall useAudit = this.getDmpDAO.getSystemInstallByName("useAudit");
		if ((useAudit != null) && "no".equals(useAudit.getValue())) {// 不需要归班
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			return this.getDataListByB2cSendByNotAudit(0, customerids, nowTime, maxCount);
		} else {
			String sql = "select * from express_send_b2c_data where send_b2c_flag=0  and  customerid in (" + customerids + ") ";

			sql += "  order by cwb,posttime,flowordertype LIMIT 0," + maxCount;

			this.logger.info("b2c-sql:" + sql);
			try {
				return this.jdbcTemplate.query(sql, new ComMapper());
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				this.logger.error("sql查询异常!", e);
				return null;
			}
		}

	}

	public List<B2CData> getDataListByFlowByIsTuotou(String customerids, long maxCount, int istuotou) {
		SystemInstall useAudit = this.getDmpDAO.getSystemInstallByName("useAudit");
		if ((useAudit != null) && "no".equals(useAudit.getValue())) {// 不需要归班
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			return this.getDataListByB2cSendByNotAudit(0, customerids, nowTime, maxCount);
		} else {

			String sql = "select * from express_send_b2c_data where send_b2c_flag=0  and  customerid in (" + customerids + ") ";
			if (istuotou == 0) {
				sql += " AND jsoncontent NOT LIKE '%\"status\":\"D1\"%'";
			} else {
				sql += " AND flowordertype=36 and jsoncontent LIKE '%\"status\":\"D1\"%'";
			}
			sql += "  order by cwb,posttime,flowordertype LIMIT 0," + maxCount;

			this.logger.info("b2c-sql:" + sql);
			try {
				return this.jdbcTemplate.query(sql, new ComMapper());
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				this.logger.error("sql查询异常!", e);
				return null;
			}
		}

	}

	/**
	 * 根据cwbs查询需要推送的订单
	 *
	 * @return
	 */
	public List<B2CData> getDataListByCwbs(String cwbs, long send_b2c_flag) {

		String sql = "select * from express_send_b2c_data where cwb in (" + cwbs + ") ";
		if (send_b2c_flag < 3) {

			sql += " and send_b2c_flag =" + send_b2c_flag;
		} else {
			sql += " and send_b2c_flag in(0,2)";
		}
		sql += "  order by posttime,flowordertype ";
		this.logger.info("b2c-sql:" + sql);
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("sql查询异常!" + e);
			return null;
		}

	}

	public List<B2CData> getDataListByCwb(String cwbs, long send_b2c_flag) {

		String sql = "select * from express_send_b2c_data where cwb in (" + cwbs + ") and send_b2c_flag =" + send_b2c_flag;
		sql += "  order by posttime ";
		this.logger.info("b2c-sql:" + sql);
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("sql查询异常!" + e);
			return null;
		}

	}

	public List<B2CData> getDataListByCwbAndType(String cwbs, String type) {
		String sql = "select * from express_send_b2c_data where cwb in (" + cwbs + ") and jsoncontent like '%\"statusCode\":\"" + type + "\"%'";
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("sql查询异常!" + e);
			return null;
		}

	}

	/**
	 * 不需要归班的查询推送的数据
	 *
	 * @param flowordertype
	 * @param customerids
	 * @param maxCount
	 * @return
	 */
	private List<B2CData> getDataListByB2cSendByNotAudit(long flowordertype, String customerids, String timeoutdate, long maxCount) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag=0  and  customerid in (" + customerids + ")";
		if (flowordertype > 0) {
			sql += " and flowordertype=" + flowordertype;
		}
		sql += " and (timeoutdate <='" + timeoutdate + "' or timeout=0) order by posttime,flowordertype LIMIT 0," + maxCount;
		this.logger.info("not audit b2c-sql:" + sql);
		List<B2CData> list = null;
		try {
			list = this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询所有未推送给供货商的订单列表
	 *
	 * @param send_b2c_flag
	 *            0 未推送 1 推送成功 2 推送失败
	 * @param flowordertype
	 *            订单的状态
	 * @param customerIds
	 *            供货商id： 125,126
	 * @param page
	 *            一次查询几条
	 * @return
	 */
	public List<B2CData> getDataListByCustomerIds(int send_b2c_flag, int flowordertype, String customerIds, int page) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag=" + send_b2c_flag + "  and  customerid in (" + customerIds + ") and flowordertype=" + flowordertype
				+ " order by posttime LIMIT 0," + page;
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/**
	 * 推送后供货商返回结果修改表
	 *
	 * @param send_b2c_flag
	 *            修改的状态
	 * @param b2cids
	 *            修改的主键串
	 * @param cwbs
	 *            修改的订单号
	 * @param type
	 *            0 全部成功或者全部失败，1 部分失败修改成功的订单，2 部分失败修改失败的订单
	 * @return
	 */
	public long updateDataByCwb(int send_b2c_flag, String b2cids, String cwbs, int type, String remark) {
		String sql = "update express_send_b2c_data set send_b2c_flag=" + send_b2c_flag + ",remark='" + remark + "' " + " where b2cid in(" + b2cids + ")  ";
		if (type == 1) {
			sql += " and cwb not in (" + cwbs + ") ";
		} else if (type == 2) {
			sql += " and cwb in (" + cwbs + ") ";
		}
		return this.jdbcTemplate.update(sql);
	}

	/**
	 * 返回结果
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateB2cIdSQLResponseStatus(long b2cids, int send_b2c_flag, String remark) throws Exception {
		String sql = " update express_send_b2c_data set  send_b2c_flag=?,remark=?,select_b2c_flag=select_b2c_flag+1 where  b2cid=? ";
		this.jdbcTemplate.update(sql, send_b2c_flag, remark, b2cids);
	}

	/**
	 * 返回结果 根据订单号修改状态 前提是只有唯一一条确定的订单号
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateSendB2cFlagByCwb(String cwb, int send_b2c_flag, String remark) throws Exception {
		String sql = " update express_send_b2c_data set  send_b2c_flag=?,remark=? where  cwb=?  and send_b2c_flag=0  ";
		this.jdbcTemplate.update(sql, send_b2c_flag, remark, cwb);
	}

	/**
	 * 根据 多个 b2cIds修改信息 修改为成功的
	 *
	 * @param paralist
	 * @param status
	 */
	public void updateMultiB2cIdSQLResponseStatus_AllSuccess(String b2cids) {

		String sql = " update express_send_b2c_data set  send_b2c_flag=1 where  b2cid in (" + b2cids + ") and send_b2c_flag=0 ";
		try {
			this.jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateTimebyId(long b2cid, String state) {

		String sql = " update express_send_b2c_data set  send_b2c_flag=?,remark='' where  b2cid =? ";
		try {
			this.jdbcTemplate.update(sql, state, b2cid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据 多个 b2cIds修改信息 修改支付通知推送成功的
	 *
	 * @param paralist
	 * @param status
	 */
	public void updatePayAmountFlagByMultiB2cId(String b2cids, int send_payamount_flag, String remark) {

		String sql = " update express_send_b2c_data set  send_payamount_flag=" + send_payamount_flag + ",remark='" + remark + "' where  b2cid in (" + b2cids + ") and send_payamount_flag=0 ";
		try {
			this.jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据 多个 b2cIds修改信息 修改为失败的
	 *
	 * @param paralist
	 * @param status
	 */
	public void updateMultiB2cIdSQLResponseStatus_AllFaild(String b2cids) {

		String sql = " update express_send_b2c_data set  send_b2c_flag=2 where  b2cid in (" + b2cids + ") ";
		try {
			this.jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 返回结果
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateSQLResponseStatus(String b2cid, int flowordertype, int send_b2c_flag) throws Exception {
		String sql = " update express_send_b2c_data set  send_b2c_flag=" + send_b2c_flag + " where  b2cid=" + b2cid;
		this.jdbcTemplate.update(sql);
	}

	public void updateSQLResponseStatus_oneTable(List<Object[]> paralist) {

		String sql = " update express_send_b2c_data set send_b2c_flag=? where send_b2c_flag=0 and cwb=? and flowordertype=? ";

		int[] updateBatch = null;
		try {
			updateBatch = this.jdbcTemplate.batchUpdate(sql, paralist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 检查本状态状态是否推送成功
	 *
	 * @param cwb
	 * @param flowtypes
	 *            包括 1和4
	 * @return
	 */
	public long checkPreStatusSendFlag(String cwb, String flowtypes) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where cwb='" + cwb + "' and flowordertype in (" + flowtypes + ") and send_b2c_flag in (1,2) ";
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 删除某段时间以前成功推送的数据,减少查询压力
	 *
	 * @param day
	 */
	public void DeleteSendB2cDataForSuccess(int day) {
		String beforeTime = DateTimeUtil.getDateBefore(day);
		String sql = "DELETE FROM express_send_b2c_data WHERE send_b2c_flag=1 AND posttime<'" + beforeTime + "' ";
		try {
			this.jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			this.logger.error("删除SendB2cData表" + day + "天前的数据异常！SQL：" + sql);
		}
	}

	/**
	 * 根据订单号查询 最后一个状态flowordertype
	 *
	 * @param cwb
	 * @param flowtype
	 * @return
	 */
	public long getNearFlowOrdertypeByCwb(String cwb) {
		try {
			String sql = "SELECT flowordertype FROM  express_send_b2c_data where cwb='" + cwb + "' ORDER BY flowordertype DESC LIMIT 0,1 ";
			return this.jdbcTemplate.queryForLong(sql);
		} catch (DataAccessException e) {
			this.logger.error("查询b2cData表为空，返回flowordertype=0,当前订单号:" + cwb);
			return 0;
		}

	}

	/**
	 * 查询出此表的信息来监控此段程序是OMS
	 *
	 * @return
	 */
	public List<B2CData> getDataListByB2cSendDMP(long maxCount) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag>0 and send_dmp_flag=0 order by posttime LIMIT 0," + maxCount;
		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	/**
	 * 修改为已推送给dmp成功
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateSendDMPSuccessFlag(long b2cids, int send_dmp_flag) throws Exception {
		String sql = " update express_send_b2c_data set  send_dmp_flag=" + send_dmp_flag + " where  b2cid=" + b2cids;
		this.jdbcTemplate.update(sql);

	}

	/**
	 * 返回结果
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateSQLResponseStatus(long b2cids, int send_b2c_flag) throws Exception {
		String sql = " update express_send_b2c_data set  send_b2c_flag=" + send_b2c_flag + " where  b2cid=" + b2cids;
		this.jdbcTemplate.update(sql);
	}

	/**
	 * 按订单和状态查询list
	 *
	 * @param cwb
	 * @param flowtype
	 * @return
	 */
	public List<B2CData> getB2CDataList(String cwb, long flowtype) {
		String sql = "SELECT * FROM  express_send_b2c_data where cwb='" + cwb + "' and flowordertype=" + flowtype + " and select_b2c_flag<>1 ";
		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	/**
	 * 更新方法
	 *
	 * @param b2CData
	 */
	public void updateB2CData(final B2CData b2CData) {
		this.jdbcTemplate.update("update express_send_b2c_data set customerid=?,flowordertype=?,send_b2c_flag=?," + "cwb=?,jsoncontent=?,delId=? where b2cid = ? ", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, b2CData.getCustomerid());
				ps.setLong(2, b2CData.getFlowordertype());
				ps.setLong(3, b2CData.getSend_b2c_flag());
				ps.setString(4, b2CData.getCwb());
				ps.setString(5, b2CData.getJsoncontent());
				ps.setLong(6, b2CData.getDelId());
				ps.setLong(7, b2CData.getB2cid());
			}
		});
	}

	/**
	 * 按ids查询list
	 *
	 * @param ids
	 * @return
	 */
	public List<B2CData> getB2CDataListByIds(String ids) {
		String sql = "SELECT * FROM  express_send_b2c_data where b2cid in(" + ids + ")  and send_b2c_flag=1 and delId>0 ";
		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	/**
	 * 根据条件查询出来数据 查询反馈失败的订单数据
	 *
	 * @param flowordertype
	 *            状态
	 * @param customerids
	 *            供货商
	 * @param maxCount
	 *            每次查询大小
	 * @param sendNums
	 *            反复推送次数
	 * @return
	 */
	public List<B2CData> getDataListFailedByFlowStatus(long flowordertype, String customerids, long maxCount, int sendNums, String remarklike) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag=2  and  customerid in (" + customerids + ") and flowordertype=" + flowordertype + " and select_b2c_flag<" + sendNums;
		if (remarklike != null) {
			sql += " and remark like '%" + remarklike + "%' ";
		}
		sql += " order by posttime,flowordertype LIMIT 0," + maxCount;
		this.logger.info("b2c-sql:" + sql);
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("sql查询异常!" + e);
			return null;
		}
	}

	public List<B2CData> getDataForBenlai(String customerid, String maxCount) {
		String sql = "select * from express_send_b2c_data where send_b2c_flag=0 and customerid=" + customerid + " order by posttime LIMIT 0," + maxCount;
		this.logger.info("b2c-【本来】sql:" + sql);
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("sql查询异常!" + e);
			return null;
		}
	}

	public List<B2CData> getCwB(String cwb) {
		String sql = "select * from express_send_b2c_data where cwb='" + cwb + "' and send_b2c_flag=0 order by posttime,flowordertype";
		return this.jdbcTemplate.query(sql, new ComMapper());

	}

	public void UpdateBenlaiSql(String cwb, String remark, String flag, String b2cid) {
		String sql = "update express_send_b2c_data set  send_b2c_flag='" + flag + "' ,remark='" + remark + "' where cwb='" + cwb + "' and b2cid in(" + b2cid + ")";
		this.jdbcTemplate.update(sql);

	}

	/**
	 * 修改 根据关键词 重置状态
	 *
	 * @param b2cids
	 * @param respMap
	 */
	public void updateKeyWordByVipShop(String customerids, String time, String keyword) throws Exception {
		String sql = " update express_send_b2c_data set  send_b2c_flag=0   WHERE  customerid=? AND posttime>? AND send_b2c_flag=2 AND select_b2c_flag<100  ";
		if ((keyword != null) && !keyword.isEmpty()) {
			sql += " AND remark='" + keyword + "'";
		}
		this.jdbcTemplate.update(sql, customerids, time);
	}

	/**
	 * 按供货商修改失败的成为未推送状态
	 *
	 * @param customerid
	 */
	public void updateFlagByCustomerid(long customerid) {
		String sql = " update express_send_b2c_data set  send_b2c_flag=0   WHERE  customerid=?  AND send_b2c_flag=2 ";
		this.jdbcTemplate.update(sql, customerid);
	}

	/**
	 * 按供货商修改失败的成为未推送状态
	 *
	 * @param customerid
	 */
	public void updateFlagAndRemarkByCustomerid(long customerid) {
		String sql = " update express_send_b2c_data set  send_b2c_flag=3,remark='标记为屏蔽推送'   WHERE  customerid=?  AND send_b2c_flag=2 ";
		this.jdbcTemplate.update(sql, customerid);
	}

	/**
	 * 按供货商修改失败的成为未推送状态
	 *
	 * @param customerid
	 */
	public void updateFlagAndRemarkByCwb(long b2cid, int state, String remark) {
		String sql = " update express_send_b2c_data set  send_b2c_flag=?,remark=?   WHERE  b2cid=?  ";
		this.jdbcTemplate.update(sql, state, remark, b2cid);
	}

	/**
	 * 根据条件查询出来数据 Cod付款的信息
	 *
	 * @return
	 */
	public List<B2CData> getDataListByCodPayAmount(String customerids, long maxCount) {

		String sql = "select * from express_send_b2c_data where send_payamount_flag=0 and flowordertype=36  and  customerid in (" + customerids + ") ";

		sql += "  order by posttime,flowordertype LIMIT 0," + maxCount;

		this.logger.info("b2c-sql:" + sql);
		try {
			return this.jdbcTemplate.query(sql, new ComMapper());
		} catch (DataAccessException e) {

			this.logger.error("sql查询异常!", e);
		}
		return null;

	}

	/**
	 * 重置订单未 未推送 根据订单号
	 *
	 * @param cwbs
	 */
	public void updateSendB2cFlag(String cwbs) {
		this.jdbcTemplate.update("update express_send_b2c_data set  send_b2c_flag=0  where  cwb in (" + cwbs + ")");
	}

	public void updateSendB2cByIds(String ids) {
		this.jdbcTemplate.update("update express_send_b2c_data set  send_b2c_flag=0  where  b2cid in (" + ids + ")");
	}

	/**
	 * 重置订单未 未推送 根据订单号
	 *
	 * @param cwbs
	 */
	public void updateSendB2cFlagAndRV(long id) {
		this.jdbcTemplate.update("UPDATE express_send_b2c_data SET jsoncontent =REPLACE( jsoncontent ,'\"statusCode\":\"RV\"','\"statusCode\":\"CLRV\"'),send_b2c_flag=0 WHERE b2cid=" + id);
	}

	public long getDataCountBySendB2cFlagAndPosttime() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String sql = "select count(1) from express_send_b2c_data where send_b2c_flag in(0,2) and posttime >= ?";

		return this.jdbcTemplate.queryForLong(sql, today);
	}

	public List<B2CData> getB2cMonitorDataList(String cwb, long customerid, long flowordertypeid, String starttime, String endtime, long page) {
		String sql = "SELECT * FROM  express_send_b2c_data where 1=1 ";
		sql = this.creContent(cwb, customerid, flowordertypeid, starttime, endtime, sql);
		sql += " ORDER BY b2cid ASC ,posttime ASC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new ComMapper());
	}

	public Long getB2cMonitorDataListCount(String cwb, long customerid, long flowordertypeid, String starttime, String endtime) {
		String sql = "SELECT count(1) FROM  express_send_b2c_data where 1=1 ";
		sql = this.creContent(cwb, customerid, flowordertypeid, starttime, endtime, sql);

		return this.jdbcTemplate.queryForLong(sql);
	}

	public String creContent(String cwb, long customerid, long flowordertypeid, String starttime, String endtime, String sql) {
		if (cwb.length() > 0) {
			sql += " and cwb in(" + cwb + ")";
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (flowordertypeid > 0) {
			sql += " and flowordertype=" + flowordertypeid;
		}
		if ((starttime.length() > 0) && (endtime.length() > 0)) {
			sql += " and posttime>='" + starttime + "' and  posttime<='" + endtime + "'";
		}
		return sql;
	}
}
