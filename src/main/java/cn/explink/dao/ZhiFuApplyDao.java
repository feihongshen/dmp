package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.util.StringUtil;

@Component
public class ZhiFuApplyDao {
	private Logger logger = LoggerFactory.getLogger(ZhiFuApplyDao.class);
	private final  class ZhiFuApplyMapper implements RowMapper<ZhiFuApplyView> {
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
			zfav.setUserid(rs.getInt("userid"));
			zfav.setFeeremark(rs.getString("feeremark"));
			return zfav;
		}
	}

	private final class ApplyMapper implements RowMapper<JSONObject> {
		
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("applyid", rs.getInt(rs.getInt("applyid")));
			obj.put("cwb",rs.getString("cwb") );
			obj.put("customerid",rs.getInt("customerid") );
			obj.put("cwbordertypeid",rs.getInt("cwbordertypeid") );
			obj.put("applycwbordertypeid",rs.getInt("applycwbordertypeid"));
			obj.put("flowordertype",rs.getInt("flowordertype") );
			obj.put("branchid",rs.getInt("branchid") );
			obj.put("paywayid",rs.getInt("paywayid") );
			obj.put("applypaywayid",rs.getInt("applypaywayid") );
			obj.put("newpaywayid",rs.getString("newpaywayid") );
			obj.put("receivablefee",rs.getBigDecimal("receivablefee") );
			obj.put("applyreceivablefee",rs.getBigDecimal("applyreceivablefee") );
			obj.put("applyway", rs.getInt("applyway"));
			obj.put("applystate", rs.getInt("applystate"));
			obj.put("applyresult", rs.getInt("applyresult"));
			obj.put("userid", rs.getInt("userid"));
			obj.put("feeremark", rs.getString("feeremark"));
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
		return	this.jdbcTemplate.update("insert into express_ops_zhifu_apply (cwb,customerid,cwbordertypeid,applycwbordertypeid,flowordertype,branchid,paywayid,applypaywayid,receivablefee,applyreceivablefee,applyway,applystate,applyresult,userid,feeremark) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				zav.getCwb(),
				zav.getCustomerid(),
				zav.getCwbordertypeid(),
				zav.getApplycwbordertypeid(),
				zav.getFlowordertype(),
				zav.getBranchid(),
				zav.getPaywayid(),
				zav.getApplypaywayid(),
				zav.getReceivablefee(),
				zav.getApplyreceivablefee(),
				zav.getApplyway(),
				zav.getApplystate(),
				zav.getApplyresult(),
				zav.getUserid(),
				zav.getFeeremark());
	}
	
	public List<ZhiFuApplyView> getAllZFAVBycwbs(){
		String sql = "select * from express_ops_zhifu_apply";
		return jdbcTemplate.query(sql,new ZhiFuApplyMapper());
	}
	
	//查询申请（未审核）支付信息数据
	public List<ZhiFuApplyView> getZFAVBycwbs(String cwbs){
		String sql = "select * from express_ops_zhifu_apply where cwb in(?) and applystate=1";
		return jdbcTemplate.query(sql,new ZhiFuApplyMapper(),cwbs);
	}
	//查询已审核支付信息数据
	public List<ZhiFuApplyView> getZFAVBycwbss(String cwbs){
		String sql = "select * from express_ops_zhifu_apply where cwb in(?) and applystate=2";
		return jdbcTemplate.query(sql,new ZhiFuApplyMapper(),cwbs);
	}

	//在 审核~确认 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbs(int cwbtypeid, int applytype,int userid, int shenhestate,
			int shenheresult) {
		String sql = "select * from express_ops_zhifu_apply where applyid>=0 ";
		StringBuffer sb = new StringBuffer("");
		if(cwbtypeid>0){
			sb.append("and cwbordertypeid="+cwbtypeid);
		}
		if(applytype>0){
			if(applytype==1){
				sb.append("and applyreceivablefee<>0.00 ");
			}else if(applytype==2){
				sb.append("and applypaywayid<>0 ");
			}else if(applytype==3){
				sb.append("and applycwbordertypeid=<>0 ");
			}
		}
		if(userid>0){
			sb.append("and userid="+userid);
		}
		if(shenhestate>0){
			sb.append("and applystate="+shenhestate);
		}
		if(shenheresult>0){
			sb.append("and applyresult="+shenheresult);
		}
		
		return jdbcTemplate.query(sql+=sb, new ZhiFuApplyMapper());
	}
	
	//在 审核~确认 页面通过条件查询
	public List<ZhiFuApplyView> getapplycwbss(int cwbtypeid, int applytype,int userid,int applystate,int shenheresult) {
		String sql = "select * from express_ops_zhifu_apply where applystate=2 ";
		StringBuffer sb = new StringBuffer("");
		if(cwbtypeid>0){
			sb.append("and cwbordertypeid="+cwbtypeid);
		}
		if(applytype>0){
			if(applytype==1){
				sb.append("and applyreceivablefee<>0.00 ");
			}else if(applytype==2){
				sb.append("and applypaywayid<>0 ");
			}else if(applytype==3){
				sb.append("and applycwbordertypeid=<>0 ");
			}
		}
		if(userid>0){
			sb.append("and userid="+userid);
		}
		/*if(shenhestate>0){
			sb.append("and applystate=? ");
		}*/
		if(shenheresult>0){
			sb.append("and applyresult="+shenheresult);
		}
		
		return jdbcTemplate.query(sql+=sb, new ZhiFuApplyMapper());
	}

	
	
}
