package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.YpdjHandleRecord;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class YpdjHandleRecordDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class YpdjHandleRecordMapper implements RowMapper<YpdjHandleRecord> {

		@Override
		public YpdjHandleRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			YpdjHandleRecord ypdjHandleRecord = new YpdjHandleRecord();
			ypdjHandleRecord.setId(rs.getLong("id"));
			ypdjHandleRecord.setFlowordertype(rs.getLong("flowordertype"));
			ypdjHandleRecord.setCustomerid(rs.getLong("customerid"));
			ypdjHandleRecord.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			ypdjHandleRecord.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			ypdjHandleRecord.setBranchid(rs.getLong("branchid"));
			ypdjHandleRecord.setNextbranchid(rs.getLong("nextbranchid"));
			return ypdjHandleRecord;
		}
	}

	private final class YpdjHandleRecordAndCwbOrderMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getLong("id"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("cwb", StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			obj.put("transcwb", StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			obj.put("branchid", rs.getLong("branchid"));
			obj.put("nextbranchid", rs.getLong("nextbranchid"));
			obj.put("emaildate", rs.getString("emaildate"));
			obj.put("consigneename", rs.getString("consigneename") == null ? "" : rs.getString("consigneename"));
			obj.put("consigneeaddress", rs.getString("consigneeaddress"));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			return obj;
		}
	}

	private final class YpdjHandleRecordAndDaohuoCwbOrderMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getLong("id"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("cwb", StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			obj.put("transcwb", StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			obj.put("branchid", rs.getLong("branchid"));
			obj.put("nextbranchid", rs.getLong("nextbranchid"));
			obj.put("emaildate", rs.getString("emaildate"));
			obj.put("consigneename", rs.getString("consigneename"));
			obj.put("consigneeaddress", rs.getString("consigneeaddress"));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("packagecode", rs.getString("packagecode"));
			return obj;
		}
	}

	public void delYpdjHandleRecordByCwb(String cwb) {
		String sql = "delete from ops_ypdjhandlerecord where cwb=?";
		this.jdbcTemplate.update(sql, cwb);
	}

	public long creYpdjHandleRecord(final YpdjHandleRecord ypdjHandleRecord) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_ypdjhandlerecord (cwb,customerid,flowordertype,transcwb,branchid,nextbranchid) " + "values(?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, ypdjHandleRecord.getCwb());
				ps.setLong(2, ypdjHandleRecord.getCustomerid());
				ps.setLong(3, ypdjHandleRecord.getFlowordertype());
				ps.setString(4, ypdjHandleRecord.getTranscwb());
				ps.setLong(5, ypdjHandleRecord.getBranchid());
				ps.setLong(6, ypdjHandleRecord.getNextbranchid());
				return ps;
			}
		}, key);
		ypdjHandleRecord.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void delYpdjHandleRecord(String cwb, String transcwb, long flowordertype, long customerid, long branchid, long nextbranchid) {
		String sql = "delete from ops_ypdjhandlerecord where cwb=? and flowordertype=? and transcwb=? and customerid=? and branchid=? and nextbranchid=?";
		this.jdbcTemplate.update(sql, cwb, flowordertype, transcwb, customerid, branchid, nextbranchid);
	}
	
	/**
	 * 清除非本站的缺件记录
	 * @param cwb 订单号
	 * @param branchidNotEq 站点id(not equal)
	 * @author neo01.huang 2016-6-13
	 */
	public void delYpdjHandleRecord(String cwb, long branchidNotEq) {
		String sql = "delete from ops_ypdjhandlerecord where cwb=? and branchid!=?";
		this.jdbcTemplate.update(sql, cwb, branchidNotEq);
	}

	// 得到入库缺货件数的统计
	public long getRukuQuejianbyBranchid(long branchid, long customerid, long emaildateid) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND yp.nextbranchid=0  and cd.state=1 AND yp.flowordertype =? ";
		if (customerid > 0) {
			sql += " and yp.customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and cd.emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.RuKu.getValue());
	}

	// 得到中转站入库缺货件数的统计
	public long getZhongZhuanZhanRukuQuejianbyBranchid(long branchid, long customerid) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND yp.nextbranchid=0 AND cd.state=1 AND yp.flowordertype =? ";
		if (customerid > 0) {
			sql += " and yp.customerid =" + customerid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());
	}

	// 得到入库缺货件数的list列表
	public List<JSONObject> getRukuQuejianbyBranchidList(long branchid, long customerid, long page, long emaildateid) {
		String sql = "SELECT yp.*,cd.emaildate,cd.consigneename,cd.consigneeaddress,cd.receivablefee FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND yp.nextbranchid=0  and cd.state=1 AND yp.flowordertype =? ";
		if (customerid > 0) {
			sql += " and yp.customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and cd.emaildateid =" + emaildateid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new YpdjHandleRecordAndCwbOrderMapper(), branchid, FlowOrderTypeEnum.RuKu.getValue(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	// 得到入库缺货件数的list列表
	public List<JSONObject> getZhongZhuanZhanRukuQuejianbyBranchidList(long branchid, long customerid, long page) {
		String sql = "SELECT yp.*,cd.emaildate,cd.consigneename,cd.consigneeaddress,cd.receivablefee FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND yp.nextbranchid=0 AND cd.state=1 AND yp.flowordertype =? ";
		if (customerid > 0) {
			sql += " and yp.customerid =" + customerid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate
				.query(sql, new YpdjHandleRecordAndCwbOrderMapper(), branchid, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public long getDaoHuoQuejianCount(long branchid) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb  "
				+ " WHERE  yp.branchid=?   AND cd.state=1 AND yp.flowordertype in(" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
				.getValue() + ")";
		return this.jdbcTemplate.queryForLong(sql, branchid);
	}

	public List<JSONObject> getDaoHuoQuejianList(long branchid, long page) {
		String sql = "SELECT yp.*,cd.emaildate,cd.consigneename,cd.consigneeaddress,cd.receivablefee,cd.packagecode FROM `ops_ypdjhandlerecord` yp " + "LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=?  and cd.state=1 AND yp.flowordertype in(" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
				.getValue() + ")";
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new YpdjHandleRecordAndDaohuoCwbOrderMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	// 得到出库缺货件数的统计
	public long getChukuQuejianbyBranchid(long branchid, long nextbranchid) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=?  and cd.state=1 AND yp.flowordertype =? ";
		if (nextbranchid > 0) {
			sql += " and yp.nextbranchid =" + nextbranchid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
	}

	// 得到中转站出库缺货件数的统计
	public long getZhongZhuanChukuQuejianbyBranchid(long branchid, long nextbranchid) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=?  AND yp.flowordertype =? ";
		if (nextbranchid > 0) {
			sql += " and yp.nextbranchid =" + nextbranchid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
	}

	// 得到出库缺货件数的统计
	public long getChukuQuejianbyBranchid(long branchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND cd.state=1 AND yp.flowordertype =? ";
		if (nextbranchid > 0) {
			sql += " and yp.nextbranchid =" + nextbranchid;
		}
		return this.jdbcTemplate.queryForLong(sql, branchid, flowordertype);
	}

	// 得到出库缺货件数的list列表
	public List<JSONObject> getChukuQuejianbyBranchidList(long branchid, long nextbranchid, long page, long flowordertype) {
		String sql = "SELECT yp.*,cd.emaildate,cd.consigneename,cd.consigneeaddress,cd.receivablefee FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=?  and cd.state=1 AND yp.flowordertype =?   ";
		if (nextbranchid > 0) {
			sql += " and yp.nextbranchid =" + nextbranchid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new YpdjHandleRecordAndCwbOrderMapper(), branchid, flowordertype, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 一票多件 导出 sql
	 *
	 * @param branchid
	 * @param customerid
	 * @return
	 */
	public List<String> getSQLExportforypdj(long branchid, long customerid, long emaildateid) {
		String sql = "SELECT yp.cwb FROM `ops_ypdjhandlerecord` AS yp LEFT JOIN `express_ops_cwb_detail` AS cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=" + branchid + " AND yp.nextbranchid=0   AND cd.state=1 AND yp.flowordertype =" + FlowOrderTypeEnum.RuKu.getValue();
		if (customerid > 0) {
			sql += " and cd.customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and cd.emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getSQLExportforypdjZhongzhuan(long branchid, long customerid, long emaildateid) {
		String sql = "SELECT yp.cwb FROM `ops_ypdjhandlerecord` AS yp LEFT JOIN `express_ops_cwb_detail` AS cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=" + branchid + " AND yp.nextbranchid=0   AND cd.state=1 AND (yp.flowordertype =" + FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() +")";
		if (customerid > 0) {
			sql += " and cd.customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and cd.emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getDaoHuoSQLExportforypdj(long branchid) {
		String sql = "SELECT cwb FROM `ops_ypdjhandlerecord` WHERE branchid=" + branchid + " AND flowordertype in( " + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao
				.getValue() + ")";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 出库 一票多件 导出 sql
	 *
	 * @param branchid
	 * @param branchid2
	 * @return
	 */
	public List<String> getSQLExportforchukuypdj(long branchid, long nextbranchid, int flowordertypeid) {
		String sql = "SELECT yp.cwb FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail` cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid = " + branchid + "   AND cd.state=1 AND yp.flowordertype = " + flowordertypeid;
		if (nextbranchid > 0) {
			sql += " AND yp.nextbranchid = " + nextbranchid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<JSONObject> getRukuQuejianbyBranchidList(long branchid, long customerid, String orderby, long emaildateid, long asc) {
		String sql = "SELECT yp.*,cd.emaildate,cd.consigneename,cd.consigneeaddress,cd.receivablefee FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE  yp.branchid=? AND yp.nextbranchid=0 AND cd.state=1 AND yp.flowordertype =? ";
		if (customerid > 0) {
			sql += " and yp.customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and cd.emaildateid =" + emaildateid;
		}
		sql += " ORDER BY  cd." + orderby;
		if ((asc % 2) == 0) {
			sql += " ASC";
		} else {
			sql += " DESC";
		}
		sql += " limit " + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new YpdjHandleRecordAndCwbOrderMapper(), branchid);
	}

	public void delypdjflowordertype(String cwb) {
		String sql = "delete from ops_ypdjhandlerecord where cwb='" + cwb + "'";
		this.jdbcTemplate.update(sql);
	}

	public long getScannedNumber(String cwb, long branchid, long flowordertype) {
		String sql = "SELECT COUNT(1) FROM `ops_ypdjhandlerecord` yp LEFT JOIN `express_ops_cwb_detail`cd ON yp.cwb=cd.cwb "
				+ " WHERE yp.cwb=? and  yp.branchid=?  and cd.state=1 AND yp.flowordertype =? ";

		return this.jdbcTemplate.queryForLong(sql, cwb, branchid, flowordertype);
	}
	
	/**
	 * 删除一票多件订单的处理记录
	 * @author leo01.liao
	 * @param listCwb 订单号列表
	 */
	public void delYpdjHandleRecordByCwbs(List<String> listCwb) {
		if(listCwb == null || listCwb.isEmpty()){
			return;
		}
		
		String strIn = "";
		for(String cwb : listCwb){
			strIn += "'" + cwb + "',";
		}
		
		if(strIn.length() > 0){
			strIn = strIn.substring(0, strIn.length()-1);
		}
		
		String sql = "delete from ops_ypdjhandlerecord where cwb in (" + strIn + ")";
		
		this.jdbcTemplate.update(sql);
	}
}
