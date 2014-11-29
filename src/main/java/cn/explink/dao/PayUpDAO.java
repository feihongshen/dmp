package cn.explink.dao;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.controller.PayUpDTO;
import cn.explink.domain.NewForExportJson;
import cn.explink.domain.PayUp;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class PayUpDAO {
	private final class PayUpRowMapper implements RowMapper<PayUp> {
		@Override
		public PayUp mapRow(ResultSet rs, int rowNum) throws SQLException {
			PayUp payup = new PayUp();
			payup.setId(rs.getLong("id"));
			payup.setCredatetime(rs.getString("credatetime"));
			payup.setUpaccountnumber(rs.getString("upbranchid"));
			payup.setUpuserrealname(rs.getString("upuserrealname"));
			payup.setUpbranchid(rs.getLong("upbranchid"));
			payup.setToaccountnumber(rs.getString("toaccountnumber"));
			payup.setTouserrealname(rs.getString("touserrealname"));
			payup.setAmount(rs.getBigDecimal("amount"));
			payup.setAmountPos(rs.getBigDecimal("amountpos"));
			payup.setUpstate(rs.getInt("upstate"));
			payup.setBranchid(rs.getLong("branchid"));
			payup.setUserid(rs.getLong("userid"));
			payup.setRemark(rs.getString("remark"));
			payup.setType(rs.getInt("type"));
			payup.setWay(rs.getInt("way"));
			payup.setAuditingremark(rs.getString("auditingremark"));
			payup.setAuditingtime(rs.getString("auditingtime"));
			payup.setAuditinguser(rs.getString("auditinguser"));
			payup.setAuditid(rs.getLong("auditid"));
			payup.setUpdateTime(rs.getString("updateTime"));
			return payup;
		}
	}

	private final class PayUpAuditRowMapper implements RowMapper<PayUpDTO> {
		@Override
		public PayUpDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PayUpDTO payup = new PayUpDTO();
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("ids").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setIds(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setIds(new String(rs.getString("ids")));
				e.printStackTrace();
			}
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("auditids").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setAuditids(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setAuditids(rs.getString("auditids"));
				e.printStackTrace();
			}
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("remark").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setRemark(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setRemark(rs.getString("remark"));
				e.printStackTrace();
			}
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("types").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setTypes(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setTypes(rs.getString("types"));
				e.printStackTrace();
			}
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("ways").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setWays(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setWays(rs.getString("ways"));
				e.printStackTrace();
			}
			try {
				ByteArrayInputStream msgContent = (ByteArrayInputStream) rs.getBlob("upuserrealname").getBinaryStream();
				byte[] byte_data = new byte[msgContent.available()];
				msgContent.read(byte_data, 0, byte_data.length);
				payup.setUpuserrealname(new String(byte_data));
				msgContent.close();
			} catch (Exception e) {
				payup.setUpuserrealname(rs.getString("upuserrealname"));
				e.printStackTrace();
			}
			payup.setCredatetime(rs.getString("credatetime"));
			payup.setAmount(rs.getBigDecimal("amount"));
			payup.setAmountPos(rs.getBigDecimal("amountpos"));
			payup.setBranchid(rs.getLong("branchid"));
			payup.setAuditingremark(rs.getString("auditingremark"));
			payup.setAuditingtime(rs.getString("auditingtime"));
			payup.setAuditinguser(rs.getString("auditinguser"));
			return payup;
		}
	}

	private final class PayUpForMapRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject json = new JSONObject();
			json.put("countCwb", rs.getLong("countCwb"));
			json.put("sumCash", rs.getDouble("sumCash"));
			json.put("sumPos", rs.getDouble("sumPos"));
			json.put("sumCheckfee", rs.getDouble("sumCheckfee"));
			json.put("sumOrderfee", rs.getDouble("sumOrderfee"));
			json.put("sumReturnfee", rs.getDouble("sumReturnfee"));
			return json;
		}
	}

	private final class PayUpForDetailMapRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject json = new JSONObject();
			json.put("id", rs.getLong("id"));
			json.put("cwb", rs.getString("cwb"));
			json.put("deliveryid", rs.getLong("deliveryid"));
			json.put("receivedfee", rs.getBigDecimal("receivedfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("receivedfee"));
			json.put("returnedfee", rs.getBigDecimal("returnedfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("returnedfee"));
			json.put("businessfee", rs.getBigDecimal("businessfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("businessfee"));
			json.put("deliverystate", rs.getLong("deliverystate"));
			json.put("cash", rs.getBigDecimal("cash") == null ? BigDecimal.ZERO : rs.getBigDecimal("cash"));
			json.put("pos", rs.getBigDecimal("pos") == null ? BigDecimal.ZERO : rs.getBigDecimal("pos"));
			json.put("codpos", rs.getBigDecimal("codpos") == null ? BigDecimal.ZERO : rs.getBigDecimal("codpos"));
			json.put("posremark", StringUtil.nullConvertToEmptyString(rs.getString("posremark")));
			json.put("mobilepodtime", rs.getString("mobilepodtime"));
			json.put("checkfee", rs.getBigDecimal("checkfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("checkfee"));
			json.put("checkremark", StringUtil.nullConvertToEmptyString(rs.getString("checkremark")));
			json.put("receivedfeeuser", rs.getLong("receivedfeeuser"));
			json.put("createtime", StringUtil.nullConvertToEmptyString(rs.getString("createtime")));
			json.put("otherfee", rs.getBigDecimal("otherfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("otherfee"));
			json.put("podremarkid", rs.getObject("podremarkid") == null ? 0L : rs.getLong("podremarkid"));
			json.put("deliverstateremark", StringUtil.nullConvertToEmptyString(rs.getString("deliverstateremark")));
			json.put("isout", rs.getLong("isout"));
			json.put("pos_feedback_flag", rs.getLong("pos_feedback_flag"));
			json.put("userid", rs.getLong("userid"));
			json.put("gcaid", rs.getLong("gcaid"));
			json.put("sign_typeid", rs.getInt("sign_typeid"));
			json.put("sign_man", rs.getString("sign_man"));
			json.put("sign_time", rs.getString("sign_time"));
			json.put("deliverybranchid", rs.getLong("deliverybranchid"));

			json.put("credatetime", rs.getString("credatetime"));
			json.put("auditingtime", rs.getString("auditingtime"));
			return json;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 产生交款记录
	 * 
	 * @param branchid
	 * @param receivedfeeuser
	 * @param subAmount
	 * @param okTime
	 */
	public long crePayUp(final PayUp pu) {

		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_pay_up (upaccountnumber,upuserrealname,upbranchid,toaccountnumber,"
						+ "touserrealname,amount,amountpos,upstate,branchid,userid,remark,type,way) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,? )", new String[] { "id" });
				ps.setString(1, pu.getUpaccountnumber());
				ps.setString(2, pu.getUpuserrealname());
				ps.setLong(3, pu.getUpbranchid());
				ps.setString(4, pu.getToaccountnumber());
				ps.setString(5, pu.getTouserrealname());
				ps.setBigDecimal(6, pu.getAmount());
				ps.setBigDecimal(7, pu.getAmountPos());
				ps.setInt(8, 0);
				ps.setLong(9, pu.getBranchid());
				ps.setLong(10, pu.getUserid());
				ps.setString(11, pu.getRemark());
				ps.setInt(12, pu.getType());
				ps.setInt(13, pu.getWay());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 返回交款列表
	 * 
	 * @param page
	 * @param upstate
	 * @param branchid
	 * @param branchid
	 * @param credatetime
	 * @param upbranchid
	 */
	public List<PayUp> getPayUpByCredatetimeAndUpstateAndBranchid(int upstate, String credatetime, String credatetime1, long branchid, long upbranchid) {
		String sql = "select * from express_ops_pay_up where upstate=?";
		if (credatetime.length() > 0) {
			sql += " and credatetime>='" + credatetime + "'";
		}
		if (credatetime1.length() > 0) {
			sql += " and credatetime<='" + credatetime1 + "'";
		}
		if (branchid > 0) {
			sql += " and branchid=" + branchid;
		}
		if (upbranchid > 0) {
			sql += " and upbranchid=" + upbranchid;
		}
		sql += " order by credatetime asc  ";
		return this.jdbcTemplate.query(sql, new PayUpRowMapper(), upstate);
	}

	public List<PayUpDTO> getPayUpByCredatetimeAndUpstateAndBranchidAudit(int upstate, String credatetime, String credatetime1, long branchid, long upbranchid) {
		String sql = "SELECT GROUP_CONCAT(id) as ids,GROUP_CONCAT(auditid) as auditids, branchid,DATE(credatetime) as credatetime, SUM(`amount`) AS amount,SUM(`amountpos`)AS amountpos, auditingremark,"
				+ "GROUP_CONCAT(remark) as remark,GROUP_CONCAT(type) as types,GROUP_CONCAT(way) as ways,"
				+ "GROUP_CONCAT(`upuserrealname`) as upuserrealname,auditingtime,auditinguser  FROM `express_ops_pay_up` where upstate=?";
		if (credatetime.length() > 0) {
			sql += " and credatetime>='" + credatetime + "'";
		}
		if (credatetime1.length() > 0) {
			sql += " and credatetime<='" + credatetime1 + "'";
		}
		if (branchid > 0) {
			sql += " and branchid=" + branchid;
		}
		if (upbranchid > 0) {
			sql += " and upbranchid=" + upbranchid;
		}
		sql += " GROUP BY branchid, DATE(credatetime) ORDER BY id ASC ";
		return this.jdbcTemplate.query(sql, new PayUpAuditRowMapper(), upstate);
	}

	public List<JSONObject> getAllPayUpByCredatetimeAndUpstateAndBranchid(long page, int upstate, String credatetime, String credatetime1, long branchid, long upbranchid, long type) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.upstate=? and ds.state =1  and ds.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ","
				+ DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}

		if (credatetime.length() > 0) {
			sql += " and pu.credatetime>='" + credatetime + "'";
		}
		if (credatetime1.length() > 0) {
			sql += " and pu.credatetime<='" + credatetime1 + "'";
		}
		if (branchid > 0) {
			sql += " and pu.branchid=" + branchid;
		}
		if (upbranchid > 0) {
			sql += " and pu.upbranchid=" + upbranchid;
		}
		sql += " order by pu.credatetime asc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper(), upstate);
	}

	public long getAllPayUpByCredatetimeAndUpstateAndBranchidCount(int upstate, String credatetime, String credatetime1, long branchid, long upbranchid, long type) {
		String sql = "SELECT count(1) " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.upstate=? and ds.state =1 and ds.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ","
				+ DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}

		if (credatetime.length() > 0) {
			sql += " and pu.credatetime>='" + credatetime + "'";
		}
		if (credatetime1.length() > 0) {
			sql += " and pu.credatetime<='" + credatetime1 + "'";
		}
		if (branchid > 0) {
			sql += " and pu.branchid=" + branchid;
		}
		if (upbranchid > 0) {
			sql += " and pu.upbranchid=" + upbranchid;
		}
		sql += " order by pu.credatetime asc ";

		return this.jdbcTemplate.queryForLong(sql, upstate);
	}

	/**
	 * 返回交款结果总数
	 * 
	 * @param page
	 * @param upstate
	 * @param branchid
	 * @param branchid
	 * @param credatetime
	 * @param upbranchid
	 */
	/*
	 * public long getPayUpByCredatetimeAndUpstateAndBranchidCount( int upstate,
	 * String credatetime,String credatetime1, long branchid,long upbranchid){
	 * 
	 * String
	 * sql="select count(1) from express_ops_pay_up where upstate="+upstate;
	 * if(credatetime.length()>0){ sql += " and credatetime>'"+credatetime+"'";
	 * }if(credatetime1.length()>0){ sql +=
	 * " and credatetime<'"+credatetime1+"'"; }if(branchid>0){ sql +=
	 * " and branchid="+branchid; }if(upbranchid>0){ sql +=
	 * " and upbranchid="+upbranchid; } sql += " order by credatetime asc ";
	 * return jdbcTemplate.queryForLong(sql); }
	 */
	/**
	 * 获得对应交款记录的统计明细
	 * 
	 * @param way
	 * @param type
	 * @param branchid
	 * @param branchid
	 * @param credatetime
	 */
	public List<JSONObject> getPayUpByIdForCount(long payupid) {
		String sql = "SELECT pu.id,COUNT(ds.cwb) as countCwb,SUM(ds.cash) as sumCash,SUM(ds.pos+ds.codpos) as sumPos,SUM(ds.checkfee) as sumCheckfee,SUM(ds.otherfee) as sumOrderfee ,SUM(ds.returnedfee) as sumReturnfee "
				+ "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id=? "
				+ " and ds.state=1 and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue()
				+ ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				+ ","
				+ DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ") GROUP BY pu.id";
		return this.jdbcTemplate.query(sql, new PayUpForMapRowMapper(), payupid);
	}

	public List<JSONObject> getPayUpByIdForCountAudit(String payupids) {
		String sql = "SELECT pu.id,COUNT(ds.cwb) as countCwb,SUM(ds.cash+ds.otherfee+ds.checkfee-ds.returnedfee) as sumCash,SUM(ds.pos+ds.codpos) as sumPos,SUM(ds.checkfee) as sumCheckfee,SUM(ds.otherfee) as sumOrderfee ,SUM(ds.returnedfee) as sumReturnfee "
				+ "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id in("
				+ payupids
				+ ") "
				+ "  and ds.state=1  and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue()
				+ ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				+ ","
				+ DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ") ";
		return this.jdbcTemplate.query(sql, new PayUpForMapRowMapper());
	}

	/**
	 * 获得对应交款记录的统计订单明细
	 * 
	 * @param way
	 * @param type
	 * @param branchid
	 * @param branchid
	 * @param credatetime
	 */
	public List<JSONObject> getPayUpByIdForDetail(long payupid, long type, long page) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id=?  and ds.state=1  and ds.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ","
				+ DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += " order by ds.cwb limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper(), payupid);
	}

	public List<JSONObject> getPayUpByIdForDetailAudit(String payupids, long type, long page) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id in(" + payupids + ")  and ds.state=1 and  ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += " order by ds.cwb limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper());
	}

	public List<JSONObject> getPayUpByIdForDetailNopage(long payupid, long type) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id=? AND  ds.id>0 " + " and ds.state=1  and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += " order by ds.cwb";

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper(), payupid);
	}

	public List<JSONObject> getPayUpByIdForDetailAuditNopage(String payupids, long type) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id in(" + payupids + ") AND ds.id>0 " + " and ds.state=1  and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += " order by ds.cwb";

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper());
	}

	public long getPayUpByIdForDetailCount(long payupid, long type) {
		String sql = "SELECT count(1) " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id=?  and ds.state=1 and ds.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ","
				+ DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += "  order by ds.cwb ";

		return this.jdbcTemplate.queryForLong(sql, payupid);
	}

	public long getPayUpByIdForDetailAuditCount(String payupids, long type) {
		String sql = "SELECT count(1) " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id in(" + payupids + ")  and ds.state=1 and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}
		sql += "  order by ds.cwb ";

		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 返回交款列表
	 * 
	 * @param way
	 * @param type
	 * @param branchid
	 * @param branchid
	 * @param credatetime
	 */
	public List<PayUp> getPayUpByBranchid(long upstate, int type, int way, String credatetime, String credatetime1, long branchid) {
		String sql = "select * from express_ops_pay_up ";
		sql = this.getPayUpByPageWhereSql(sql, upstate, type, way, credatetime, credatetime1, branchid);
		sql += " order by credatetime desc ";
		return this.jdbcTemplate.query(sql, new PayUpRowMapper());
	}

	private String getPayUpByPageWhereSql(String sql, long upstate, int type, int way, String credatetime, String credatetime1, long branchid) {
		sql += " where   credatetime>'" + credatetime + "' AND credatetime<'" + credatetime1 + " 23:59:59" + "' ";
		if (branchid > -1) {
			sql += " and branchid='" + branchid + "' ";
		}
		if (upstate > -1) {
			sql += " and upstate='" + upstate + "' ";
		}
		if (type > 0) {
			sql += " and type='" + type + "' ";
		}
		if (way > 0) {
			sql += " and way='" + way + "' ";
		}
		return sql;
	}

	/**
	 * 根据id返回交款记录
	 */
	public PayUp getPayUpById(long id) {
		String sql = "select * from express_ops_pay_up where id=? ";
		return this.jdbcTemplate.queryForObject(sql, new PayUpRowMapper(), id);
	}

	/**
	 * 根据id返回交款记录 并锁行
	 */
	public PayUp getPayUpByIdLock(long id) {
		String sql = "select * from express_ops_pay_up where id=? for update ";
		return this.jdbcTemplate.queryForObject(sql, new PayUpRowMapper(), id);
	}

	/**
	 * 更新已审核的交款信息的状态
	 */
	public void savePayUpByBack(long id, String backRemark) {
		String sql = "update express_ops_pay_up set upstate=1,remark=CONCAT(remark,'<br/>回复：',?) where id=? ";
		this.jdbcTemplate.update(sql, backRemark, id);
	}

	public void savePayUpForAudting(long id, String auditinguser, String auditingremark, String auditingtime) {
		String sql = "update express_ops_pay_up set upstate=?,auditinguser=?,auditingremark=?,auditingtime=? where id=? ";
		this.jdbcTemplate.update(sql, 1, auditinguser, auditingremark, auditingtime, id);
	}

	public void savePayUpForAudtingAudit(String payupids, String auditinguser, String auditingremark, String auditingtime, long auditid) {
		String sql = "update express_ops_pay_up set upstate=?,auditinguser=?,auditingremark=?,auditingtime=?,auditid=? where id in(" + payupids + ") ";
		this.jdbcTemplate.update(sql, 1, auditinguser, auditingremark, auditingtime, auditid);
	}

	public List<JSONObject> getAllPayUpByCredatetimeAndUpstateAndBranchidNoPage(int upstate, String credatetime, String credatetime1, long branchid, long upbranchid, long type) {
		String sql = "SELECT pu.credatetime,ds.*,gca.auditingtime " + "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.upstate=" + upstate + " AND ds.id>0 " + " and ds.state=1  and ds.deliverystate in("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		if (type > 0) {
			if (type == 1) {// 现金
				sql += " and ds.cash>0";
			} else if (type == 2) { // pos
				sql += " and (ds.pos>0 or ds.codpos>0)";
			} else if (type == 3) {// 其他
				sql += " and ds.otherfee>0";
			} else if (type == 4) { // 支票
				sql += " and ds.checkfee>0";
			}
		}

		if (credatetime.length() > 0) {
			sql += " and pu.credatetime>='" + credatetime + "'";
		}
		if (credatetime1.length() > 0) {
			sql += " and pu.credatetime<='" + credatetime1 + "'";
		}
		if (branchid > 0) {
			sql += " and pu.branchid=" + branchid;
		}
		if (upbranchid > 0) {
			sql += " and pu.upbranchid=" + upbranchid;
		}
		sql += " order by pu.credatetime asc ";

		return this.jdbcTemplate.query(sql, new PayUpForDetailMapRowMapper());
	}

	private final class PayupByBranchid implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("branchid", rs.getString("branchid"));
			reJson.put("amount", rs.getString("amount"));
			reJson.put("pos", rs.getString("pos"));
			return reJson;
		}
	}

	public List<JSONObject> getPayupByDayGroupByBranchid(String day, String secondDay) {
		try {
			String sql = "SELECT branchid,SUM(`amount`) AS amount,SUM(`amountpos`) AS pos FROM `express_ops_pay_up` WHERE upstate=0 AND credatetime>? AND credatetime<=? GROUP BY branchid";
			return this.jdbcTemplate.query(sql, new PayupByBranchid(), day, secondDay);
		} catch (DataAccessException e) {
			return null;
		}
	}

	// ==============================修改订单使用的方法 start
	// ==================================
	/**
	 * 重置审核状态 修改站点交款表字段
	 * 
	 * @param id
	 *            站点交款记录id
	 * @param amount
	 *            站点交款非POS金额 减去重置审核状态订单的非POS金额 的值
	 * @param amount_pos
	 *            站点交款POS金额 减去重置审核状态订单的POS金额 的值
	 * @param updateTime
	 *            修改订单时间
	 */
	public void updateForChongZhiShenHe(long id, BigDecimal amount, BigDecimal amount_pos, String updateTime) {
		this.jdbcTemplate.update("update  express_ops_pay_up set " + "amount=?,amountpos=?,updatetime=? where id = ? ", amount, amount_pos, updateTime, id);

	}

	// ==============================修改订单使用的方法 End
	// ==================================

	// ==============================站点导出之普通模板模式==================================

	/*
	 * 站点导出准备lcx
	 */

	public List<PayUp> getAllForExportbyid(String id) {
		String sql = "select * from express_ops_pay_up where id in (" + id + ")";
		return this.jdbcTemplate.query(sql, new PayUpRowMapper());
	}

	// 新加为导出
	private final class PayUpExportRowMapper implements RowMapper<NewForExportJson> {
		@Override
		public NewForExportJson mapRow(ResultSet rs, int rowNum) throws SQLException {
			NewForExportJson payup = new NewForExportJson();
			payup.setId(rs.getLong("id"));
			payup.setCountCwb(rs.getString("countCwb"));
			payup.setSumCash(rs.getString("sumCash"));
			payup.setSumPos(rs.getString("sumPos"));
			payup.setSumCheckfee(rs.getString("sumCheckfee"));
			payup.setSumOrderfee(rs.getString("sumOrderfee"));
			payup.setSumReturnfee(rs.getString("sumReturnfee"));
			return payup;
		}
	}

	// 新加为导出
	public NewForExportJson getPayUpByIdForExport(String payupids) {
		String sql = "SELECT pu.id as id,COUNT(ds.cwb) as countCwb,SUM(ds.cash+ds.otherfee+ds.checkfee-ds.returnedfee) as sumCash,SUM(ds.pos) as sumPos,SUM(ds.checkfee) as sumCheckfee,SUM(ds.otherfee) as sumOrderfee ,SUM(ds.returnedfee) as sumReturnfee "
				+ "FROM `express_ops_pay_up` pu LEFT JOIN `express_ops_goto_class_auditing` gca ON gca.payupid=pu.id	"
				+ "LEFT JOIN `express_ops_delivery_state` ds ON ds.gcaid=gca.id WHERE pu.id in("
				+ payupids + ") ";
		sql += " and ds.state =1  and ds.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";

		return this.jdbcTemplate.queryForObject(sql, new PayUpExportRowMapper());
	}

	// 新加方法，求出站点结算list
	public List<String> getpayupAllForexport(long upstate, String credatetime, String credatetime1, long branchid) {
		String sql = "select id from express_ops_pay_up where credatetime>=? and credatetime<=?";

		StringBuffer sb = new StringBuffer();
		if (upstate >= 0) {
			sb.append(" and upstate=" + upstate);
		}
		if (branchid > 0) {
			sb.append(" and branchid=" + branchid);
		}
		sql += sb.toString();
		return this.jdbcTemplate.queryForList(sql, String.class, credatetime, credatetime1);
	}

}
