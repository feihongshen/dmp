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

import cn.explink.core.utils.StringUtils;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ZhiFuApplyDao {
	private Logger logger = LoggerFactory.getLogger(ZhiFuApplyDao.class);

	private final class ZhiFuApplyMapper implements RowMapper<ZhiFuApplyView> {
		@Override
		public ZhiFuApplyView mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZhiFuApplyView zfav = new ZhiFuApplyView();
			zfav.setApplyid(rs.getInt("applyid"));
			zfav.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			zfav.setCustomerid(rs.getInt("customerid"));
			zfav.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			zfav.setApplycwbordertypeid(rs.getInt("applycwbordertypeid"));
			zfav.setFlowordertype(rs.getInt("flowordertype"));
			zfav.setBranchid(rs.getInt("branchid"));
			zfav.setPaywayid(rs.getInt("paywayid"));
			zfav.setApplypaywayid(rs.getInt("applypaywayid"));
			zfav.setReceivablefee(rs.getBigDecimal("receivablefee"));
			zfav.setApplyreceivablefee(rs.getBigDecimal("applyreceivablefee"));
			zfav.setApplyway(rs.getInt("applyway"));
			zfav.setApplystate(rs.getInt("applystate"));
			zfav.setApplyresult(rs.getInt("applyresult"));
			zfav.setConfirmstate(rs.getInt("confirmstate"));
			zfav.setConfirmresult(rs.getInt("confirmresult"));
			zfav.setUserid(rs.getInt("userid"));
			zfav.setFeewaytyperemark(rs.getString("feewaytyperemark"));
			zfav.setShouldfare(rs.getBigDecimal("shouldfare"));
			zfav.setApplytime(StringUtil.nullConvertToEmptyString(rs.getString("applytime")));//申请时间
			zfav.setAuditname(StringUtil.nullConvertToEmptyString(rs.getString("auditname")));//审核人
			zfav.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));//审核时间
			zfav.setConfirmname(StringUtil.nullConvertToEmptyString(rs.getString("confirmname")));//确认人
			zfav.setConfirmtime(StringUtil.nullConvertToEmptyString(rs.getString("confirmtime")));//确认时间

			return zfav;
		}
	}

	private final class ApplyMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("applyid", rs.getInt(rs.getInt("applyid")));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("customerid", rs.getInt("customerid"));
			obj.put("cwbordertypeid", rs.getInt("cwbordertypeid"));
			obj.put("applycwbordertypeid", rs.getInt("applycwbordertypeid"));
			obj.put("flowordertype", rs.getInt("flowordertype"));
			obj.put("branchid", rs.getInt("branchid"));
			obj.put("paywayid", rs.getInt("paywayid"));
			obj.put("applypaywayid", rs.getInt("applypaywayid"));
			obj.put("newpaywayid", rs.getString("newpaywayid"));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("applyreceivablefee", rs.getBigDecimal("applyreceivablefee"));
			obj.put("applyway", rs.getInt("applyway"));
			obj.put("applystate", rs.getInt("applystate"));
			obj.put("applyresult", rs.getInt("applyresult"));
			obj.put("confirmstate", rs.getInt("confirmstate"));
			obj.put("confirmresult", rs.getInt("confirmresult"));
			obj.put("userid", rs.getInt("userid"));
			obj.put("feeremark", rs.getString("feewaytyperemark"));
			return obj;
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
	public long creZhiFuApplyView(final ZhiFuApplyView zav) {
		return this.jdbcTemplate
				.update("insert into express_ops_zhifu_apply (cwb,customerid,cwbordertypeid,applycwbordertypeid,flowordertype,branchid,paywayid,applypaywayid,receivablefee,applyreceivablefee,applyway,applystate,applyresult,userid,feewaytyperemark,applytime,shouldfare) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", zav
						.getCwb(), zav.getCustomerid(), zav.getCwbordertypeid(), zav.getApplycwbordertypeid(), zav.getFlowordertype(), zav.getBranchid(), zav.getPaywayid(), zav.getApplypaywayid(), zav
						.getReceivablefee(), zav.getApplyreceivablefee(), zav.getApplyway(), zav.getApplystate(), zav.getApplyresult(), zav.getUserid(), zav.getFeewaytyperemark(), zav.getApplytime(), zav
						.getShouldfare());

	}

	public List<ZhiFuApplyView> getAllZFAVBycwbs() {
		String sql = "select * from express_ops_zhifu_apply";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	//查询申请（未审核）支付信息数据
	public List<ZhiFuApplyView> getZFAVBycwbs(String cwbs) {
		String sql = "select * from express_ops_zhifu_apply where cwb in(?) and applystate=1";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper(), cwbs);
	}

	//查询已审核支付信息数据
	public List<ZhiFuApplyView> getZFAVBycwbss(String cwbs) {
		String sql = "select * from express_ops_zhifu_apply where cwb in(?) and applyresult=2 and confirmresult=0";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper(), cwbs);
	}

	//在 审核~确认 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbs(int cwbtypeid, int applytype, int userid, int shenhestate, int shenheresult) {
		String sql = "";
		StringBuffer sb = new StringBuffer("");
		if ((cwbtypeid == 0) && (applytype == 0) && (userid == 0) && (shenhestate == 0)) {
			sql = "select * from express_ops_zhifu_apply where applystate=1 ";
		} else {
			sql = "select * from express_ops_zhifu_apply where applyid>=0 ";
			if (cwbtypeid > 0) {
				sb.append("and cwbordertypeid=" + cwbtypeid);
			}
			if (applytype > 0) {
				if (applytype == 1) {
					sb.append("and applyreceivablefee<>0.00 ");
				} else if (applytype == 2) {
					sb.append("and applypaywayid<>0 ");
				} else if (applytype == 3) {
					sb.append("and applycwbordertypeid=<>0 ");
				}
			}
			if (userid > 0) {
				sb.append("and userid=" + userid);
			}
			if (shenhestate > 0) {
				sb.append("and applystate=" + shenhestate);
			}
			if (shenheresult > 0) {
				sb.append("and applyresult=" + shenheresult);
			}
		}

		return this.jdbcTemplate.query(sql += sb, new ZhiFuApplyMapper());
	}

	//在 审核~确认 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbss(int cwbtypeid, int applytype, int userid, int confirmstate, int confirmresult) {
		String sql = "";
		if ((cwbtypeid == 0) && (applytype == 0) && (userid == 0) && (confirmstate == 0) && (confirmresult == 0)) {
			sql = "select * from express_ops_zhifu_apply where applyresult=2 and confirmresult=0";
		} else {
			sql = "select * from express_ops_zhifu_apply where applyresult=2 ";
			StringBuffer sb = new StringBuffer("");
			if (cwbtypeid > 0) {
				sb.append("and cwbordertypeid=" + cwbtypeid);
			}
			if (applytype > 0) {
				if (applytype == 1) {
					sb.append("and applyreceivablefee<>0.00 ");
				} else if (applytype == 2) {
					sb.append("and applypaywayid<>0 ");
				} else if (applytype == 3) {
					sb.append("and applycwbordertypeid=<>0 ");
				}
			}
			if (userid > 0) {
				sb.append("and userid=" + userid);
			}
			if (confirmstate > 0) {
				sb.append("and confirmstate=" + confirmstate);
			}
			if (confirmresult > 0) {
				sb.append("and confirmresult=" + confirmresult);
			}
			sql = sql + sb;
		}

		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	public List<ZhiFuApplyView> getZAVByAppid(String applyids) {
		String sql = "select * from express_ops_zhifu_apply where applyid in(" + applyids + ")";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper(), applyids);
	}
	
	public ZhiFuApplyView findZFAByApplyId(String applyId){
		StringBuffer sql = new StringBuffer("select * from express_ops_zhifu_apply where applyid = ") ;
		sql.append(applyId) ;
		List<ZhiFuApplyView> list = this.jdbcTemplate.query(sql.toString() , new ZhiFuApplyMapper()) ;
		if(list == null || list.size() == 0){
			return null ;
		}
		return list.get(0) ;
	}

	//审核通过
	public void updateStatePassByCwb(int applyid, String auditname, String audittime) {
		String sql = "update express_ops_zhifu_apply set applystate=2, applyresult=2,auditname=?,audittime=? where applyid=? ";
		this.jdbcTemplate.update(sql, auditname, audittime, applyid);

	}

	//审核为不通过
	public void updateStateNopassByCwb(int applyid, String auditname, String audittime) {
		String sql = "update express_ops_zhifu_apply set applystate=2, applyresult=1,auditname=?,audittime=? where applyid=? ";
		this.jdbcTemplate.update(sql, auditname, audittime, applyid);
	}

	//确认通过
	public void updateStateConfirmPassByCwb(int applyid, String confirmname, String confirmtime) {
		String sql = "update express_ops_zhifu_apply set confirmstate=2,confirmresult=2,confirmname=?,confirmtime=? where applyid=? ";
		this.jdbcTemplate.update(sql, confirmname, confirmtime, applyid);
	}

	//确认不通过
	public void updateStateConfirmNopassByCwb(int applyid, String confirmname, String confirmtime) {
		String sql = "update express_ops_zhifu_apply set confirmstate=2,confirmresult=1,confirmname=?,confirmtime=? where applyid=? ";
		this.jdbcTemplate.update(sql, confirmname, confirmtime, applyid);

	}

	//通过applyid查到对应数据
	public ZhiFuApplyView getZhiFuViewByApplyid(String applyid) {
		String sql = "select * from express_ops_zhifu_apply where applyid=?";
		return this.jdbcTemplate.queryForObject(sql, new ZhiFuApplyMapper(), applyid);
	}

	//在 审核 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbsForpage(long page, String cwbs, int cwbtypeid, int applytype, int userid, int applystate, int applyresult) {
		String sql = "select * from express_ops_zhifu_apply where 1=1";
		StringBuffer sb = new StringBuffer("");
		if (!cwbs.equals("")) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		if (applystate > 0) {
			sb.append(" and applystate=" + applystate);
		}
		if (applyresult > 0) {
			sb.append(" and applyresult=" + applyresult);
		}
		sb.append(" order by applytime desc");
		sql += sb;
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	//在 审核 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbs(String cwbs, int cwbtypeid, int applytype, int userid, int applystate, int applyresult) {
		String sql = "select * from express_ops_zhifu_apply where 1=1";
		StringBuffer sb = new StringBuffer("");
		if (!cwbs.equals("")) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		if (applystate > 0) {
			sb.append(" and applystate=" + applystate);
		}
		if (applyresult > 0) {
			sb.append(" and applyresult=" + applyresult);
		}
		sql += sb;

		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	//查出申请总数
	public long getapplycwbsForCount(String cwbs, int cwbtypeid, int applytype, int userid, int shenhestate, int shenheresult) {
		String sql = "select count(1) from express_ops_zhifu_apply where 1=1";
		StringBuffer sb = new StringBuffer("");
		if (!cwbs.equals("")) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		if (shenhestate > 0) {
			sb.append(" and applystate=" + shenhestate);
		}
		if (shenheresult > 0) {
			sb.append(" and applyresult=" + shenheresult);
		}
		sql += sb;
		return this.jdbcTemplate.queryForLong(sql);
	}

	//在确认 页面通过条件查询
	public List<ZhiFuApplyView> getConfirmCwbsForpage(long page, String cwbs, int cwbtypeid, int applytype, int userid, int confirmstate, int confirmresult) {
		String sql = "select * from express_ops_zhifu_apply where applyresult=2 and confirmstate=" + confirmstate;
		StringBuffer sb = new StringBuffer("");
		if (!"".equals(cwbs)) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		/*if(confirmstate>0){
			sb.append(" and confirmstate="+confirmstate);
		}*/
		if (confirmresult > 0) {
			sb.append(" and confirmresult=" + confirmresult);
		}
		sb.append(" order by audittime desc");
		sql += sb;
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	//在确认 页面通过条件查询所有订单
	public List<ZhiFuApplyView> getConfirmCwbs(String cwbs, int cwbtypeid, int applytype, int userid, int confirmstate, int confirmresult) {
		String sql = "select * from express_ops_zhifu_apply where applyresult=2 and confirmstate=" + confirmstate;
		StringBuffer sb = new StringBuffer("");
		if (!"".equals(cwbs)) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		/*if(confirmstate>0){
			sb.append(" and confirmstate="+confirmstate);
		}*/
		if (confirmresult > 0) {
			sb.append(" and confirmresult=" + confirmresult);
		}
		sql += sb;
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}

	//在确认 页面通过条件查询
	public long getConfirmCwbsForCount(String cwbs, int cwbtypeid, int applytype, int userid, int confirmstate, int confirmresult) {
		String sql = "select count(1) from express_ops_zhifu_apply where applyresult=2 and confirmstate=" + confirmstate;
		StringBuffer sb = new StringBuffer("");
		if (!"".equals(cwbs)) {
			sb.append(" and cwb in(" + cwbs + ")");
		}
		if (cwbtypeid > 0) {
			sb.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (applytype > 0) {
			sb.append(" and applyway=" + applytype);
		}
		/*if(confirmstate>0){
			sb.append(" and confirmstate="+confirmstate);
		}*/
		if (confirmresult > 0) {
			sb.append(" and confirmresult=" + confirmresult);
		}
		sql += sb;
		return this.jdbcTemplate.queryForLong(sql);
	}

	public ZhiFuApplyView getCheckstate(int applyidint, int applystate) {
		try {
			String sql = "select * from express_ops_zhifu_apply where applyid=? and applystate=?";
			return this.jdbcTemplate.queryForObject(sql, new ZhiFuApplyMapper(), applyidint, applystate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ZhiFuApplyView getConfirmstate(int applyidint, int confirmstate) {
		try {
			String sql = "select * from express_ops_zhifu_apply where applyid=? and confirmstate=?";
			return this.jdbcTemplate.queryForObject(sql, new ZhiFuApplyMapper(), applyidint, confirmstate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//根据cwb查询申请表里有多少单申请数据
	public long getApplystateCount(String cwb, int applyway) {
		String sql = "select count(1) from express_ops_zhifu_apply where applystate=1 and cwb=? and applyway=?";
		return this.jdbcTemplate.queryForLong(sql, cwb, applyway);
	}
	
	//查询未审批或者审批通过未确认的申请数据
	public List<ZhiFuApplyView> getZhiFuApplyViewListByCwbs(String cwbs) {
		String sql = "select * from express_ops_zhifu_apply where cwb in ("+cwbs+")";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}
	
	//查询审批通过并且确认通过的申请数据
	public List<ZhiFuApplyView> getCheckConfirmZFAVByCwbs(String cwbs) {
		String sql  = "select * from express_ops_zhifu_apply where applystate = 2 and applyresult = 2 and confirmstate = 2 and confirmresult = 2 and cwb in ("+cwbs+")";
		return this.jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}
	
	/**
	 * 查询未审批的申请数据
	 */
	public List<ZhiFuApplyView> getNotAudiByCwbs(String cwbs){
		if(StringUtils.isEmpty(cwbs)){
			return new ArrayList<ZhiFuApplyView>();
		}
		String[] cwbArr = cwbs.split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0, len = cwbArr.length; i < len; i++){
			if(i != 0){
				sb.append(",");
			}
			sb.append("'");
			sb.append(cwbArr[i]);
			sb.append("'");
		}
		String sql = "select * from express_ops_zhifu_apply where applystate=1 and  cwb in (" + sb.toString() + ")";
		return jdbcTemplate.query(sql, new ZhiFuApplyMapper());
	}
}
