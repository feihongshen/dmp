package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.express.ExpressCwb4TakeGoodsQuery;
import cn.explink.domain.express.ExpressOutStationInfo;
import cn.explink.util.Tools;

@Component
public class ExpressOutStationInfoDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressOutStationInfoRowMapper implements RowMapper<ExpressOutStationInfo> {
		@Override
		public ExpressOutStationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressOutStationInfo expressOutStationInfo = new ExpressOutStationInfo();
			expressOutStationInfo.setId(rs.getInt("id"));
			expressOutStationInfo.setCwbId(rs.getInt("cwbid"));
			expressOutStationInfo.setCwb(rs.getString("cwb"));
			expressOutStationInfo.setOutstationBranchid(rs.getInt("outstation_branchid"));
			expressOutStationInfo.setOutstationBranchName(rs.getString("outstation_branchname"));
			String outtime = rs.getTimestamp("outstationtime").toString();
			expressOutStationInfo.setOutstationTime(outtime.substring(0,outtime.length()-2));
			expressOutStationInfo.setHanderId(rs.getInt("handerid"));
			expressOutStationInfo.setHanderName(rs.getString("handername"));
			return expressOutStationInfo;
		}
	}

	//查询所有
	public List<ExpressOutStationInfo> getAllOutStationInfo(){
		List<ExpressOutStationInfo> list = new ArrayList<ExpressOutStationInfo>();
		String sql = "select * from express_ops_outstationinfo";
		list = this.jdbcTemplate.query(sql, new ExpressOutStationInfoRowMapper());
		return list;
	}
	
	//创建记录
	public void creatOutStationInfo(long cwbid,String cwb,long outstationBranchid,String outstationBranchName,String outstationtime,long handerid,String handerName,long payMethod,long collectorid){
		String sql = "INSERT INTO `express_ops_outstationinfo` (cwbid,cwb,outstation_branchid,outstation_branchname,outstationtime,handerid,handername,collectorid) VALUES ("
				+ cwbid + ",'" + cwb + "'," + outstationBranchid + ",'" + outstationBranchName + "','" + outstationtime + "'," + handerid + ",'" + handerName  + "'," +collectorid
				+ ")";
		this.jdbcTemplate.execute(sql);
	}
	
	//按揽件查询条件查询出站信息表
	public Map<Long,ExpressOutStationInfo> getOutStationInfo(ExpressCwb4TakeGoodsQuery expressCwb4TakeGoodsQuery,long outStationId,String userIds){
//		List<ExpressOutStationInfo>
		Map<Long,ExpressOutStationInfo> map = new HashMap<Long, ExpressOutStationInfo>();
		String sql = "select * from express_ops_outstationinfo where 1=1 ";
		sql += conditions4CwbExpressTakeGoodsQuery(expressCwb4TakeGoodsQuery,outStationId,userIds);
		List<ExpressOutStationInfo> list = this.jdbcTemplate.query(sql,new ExpressOutStationInfoRowMapper());
		for(int a = 0; a < list.size(); a++){
			map.put((long)list.get(a).getId(), list.get(a));
		}
		return map;
	}
	
	
	/**
	 * 获取查询订单【快递--揽件查询】的sql
	 *
	 * @author 王志宇
	 * @param cwb4TakeGoodsQuery
	 * @return
	 */
	private String conditions4CwbExpressTakeGoodsQuery(ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery,long nextBranchListr,String userIds) {
//		List<Branch> nextBranchList = expressOutStationService.getNextBranchList(user.getBranchid(),user);
		StringBuffer sql = new StringBuffer();
		// 站点
		sql.append(" and outstation_branchid = " + nextBranchListr);
		// 小件员id
		if (!Tools.isEmpty(cwb4TakeGoodsQuery.getCollectorid())) {
			sql.append(" and collectorid=" + cwb4TakeGoodsQuery.getCollectorid() + "");
		}else{
			sql.append(" and collectorid in (" + userIds + ")");
		}
		// 付款方式
		/*if (!Tools.isEmpty(cwb4TakeGoodsQuery.getPayWay())) {
			sql.append(" and paymethod=" + cwb4TakeGoodsQuery.getPayWay() + "");
		}*/
		// 开始时间
		if (!Tools.isEmpty(cwb4TakeGoodsQuery.getStartTime())) {
			sql.append(" and (outstationtime > '" + cwb4TakeGoodsQuery.getStartTime() + "'");
			sql.append(" or outstationtime is null)");
		}
		// 结束时间
		if (!Tools.isEmpty(cwb4TakeGoodsQuery.getEndTime())) {
			sql.append(" and (outstationtime < '" + cwb4TakeGoodsQuery.getEndTime() + "'");
			sql.append(" or outstationtime is null)");
		}
		return sql.toString();
	}

}
