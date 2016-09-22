package cn.explink.b2c.ems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.b2c.emsSmallPakage.EmsSmallPackageViewVo;
import cn.explink.util.StringUtil;

@Component
public class EMSDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class EMSFlowMapper implements RowMapper<EMSFlowEntity> {
		@Override
		public EMSFlowEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			EMSFlowEntity en = new EMSFlowEntity();
			en.setId(rs.getLong("id"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setEmailnum(rs.getString("email_num"));
			en.setFlowContent(rs.getString("flow_content"));
			en.setState(rs.getInt("state"));
			en.setEmsFlowordertype(rs.getLong("emsFlowordertype"));
			en.setEmsAction(rs.getString("emsAction"));
			en.setProperdelivery(rs.getString("properdelivery"));
			en.setNotproperdelivery(rs.getString("notproperdelivery"));
			en.setHandleCount(rs.getInt("handleCount"));
			return en; 
		}
	}
	
	private final class SendToEMSOrderMapper implements RowMapper<SendToEMSOrder> {
		@Override
		public SendToEMSOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			SendToEMSOrder order = new SendToEMSOrder();
			order.setCwb(rs.getString("cwb"));
			order.setTranscwb(rs.getString("transcwb"));
			order.setCredate(rs.getString("credate"));
			order.setAddTranscwbFlag(rs.getString("addTranscwbFlag"));
			order.setData(rs.getString("data"));
			order.setGetMailnumFlag(rs.getString("getMailnumFlag"));
			return order; 
		}
	}
	
	private final class EMSFlowObjInitialMapper implements RowMapper<EMSFlowObjInitial> {
		@Override
		public EMSFlowObjInitial mapRow(ResultSet rs, int rowNum) throws SQLException {
			EMSFlowObjInitial obj = new EMSFlowObjInitial();
			obj.setId(rs.getLong("id"));
			obj.setEmailnum(rs.getString("email_num"));
			obj.setFlowContent(rs.getString("flow_content"));
			obj.setCredate(rs.getString("credate"));
			obj.setState(rs.getInt("state"));
			obj.setHandleCount(rs.getInt("handleCount"));
			obj.setAction(rs.getString("action"));
			return obj; 
		}
	}
	
	/**
	 * 订单运单对照表mapper 2016-07-25
	 */
	private final class EMSTranscwbMapper implements RowMapper<SendToEMSOrder> {
		@Override
		public SendToEMSOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			SendToEMSOrder obj = new SendToEMSOrder();
			obj.setCwb(rs.getString("cwb"));
			obj.setTranscwb(rs.getString("transcwb"));
			obj.setEmail_num(rs.getString("email_num"));
			return obj; 
		}
	}
	
	/**
	 * 查询绑定关系展示信息mapper 2016-07-25
	 */
	private final class EMSCwbInfoViewMapper implements RowMapper<EmsSmallPackageViewVo> {
		@Override
		public EmsSmallPackageViewVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmsSmallPackageViewVo obj = new EmsSmallPackageViewVo();
			obj.setCwb(rs.getString("cwb"));
			obj.setTranscwb(rs.getString("transcwb"));
			obj.setEmail_num(rs.getString("email_num"));
			obj.setBingTime(rs.getString("bing_time"));
			obj.setDeliveryBranchName(rs.getString("branchname"));
			obj.setConsigneename(rs.getString("consigneename"));
			obj.setConsigneeaddress(rs.getString("consigneeaddress"));
			obj.setRealweight(rs.getDouble("realweight"));
			return obj; 
		}
	}
	
	
    //保存ems运单轨迹报文信息  edit by zhouhuan 2016-07-21
	public void saveEMSFlowInfo(final String transcwb, final String mailnum,
			final String listexpressmail,final String action,final long emsFlowordertype,
			final String properdelivery,final String notproperdelivery,final String credate,final long orderDirection) {
		this.jdbcTemplate
		.update("insert into express_ems_flow_info(transcwb,email_num,flow_content,emsAction,emsFlowordertype,"
				+ "properdelivery,notproperdelivery,credate,order_direction) values(?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, transcwb);
				ps.setString(2, mailnum);
				ps.setString(3, listexpressmail);
				ps.setString(4, action);
				ps.setLong(5, emsFlowordertype);
				ps.setString(6, properdelivery);
				ps.setString(7, notproperdelivery);
				ps.setString(8, credate);
				ps.setLong(9, orderDirection);
			}
		});
	}

	//根据ems运单号获取dmp运单号
	public String getTranscwbByEmailNo(String mailnum) {
		String sql = "select transcwb from express_ems_dmp_transcwb where email_num=? limit 0,1";
		List<Map<String, Object>> mailnumObj = this.jdbcTemplate.queryForList(sql,mailnum);
		String transcwb = "";
		if ((mailnumObj != null) && (mailnumObj.size() > 0)) {
			Map<String, Object> map = mailnumObj.get(0);
			transcwb =  map.get("transcwb")+"";
		}
		return transcwb;
	}

	//根据获取需要转业务的ems轨迹信息
	public List<EMSFlowEntity> getEMSFlowEntityList() {
		String sql = "select * from express_ems_flow_info where state in(0,2) and handleCount<10 and order_direction=0 order by credate limit 0,2000";
		List<EMSFlowEntity> emsFlowEntityList = this.jdbcTemplate.query(sql, new EMSFlowMapper());
		return emsFlowEntityList;
	}

	//根据ems轨迹模拟dmp操作的处理结果，修改处理状态
	public void changeEmsTraceDataState(long id,int state,String remark) {
		String sql = "update express_ems_flow_info set state="+state+",remark='"+remark+"',handleCount=handleCount+1 where id=?";
		this.jdbcTemplate.update(sql,id);
	}

	//保存ems运单与dmp运单对照关系
	public void saveEMSEmailnoAndDMPTranscwb(final String cwb,final String transcwb,final String billno,final String bingTime) {
		this.jdbcTemplate.update("insert into express_ems_dmp_transcwb(cwb,transcwb,email_num,bing_time) values(?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwb);
				ps.setString(2, transcwb);
				ps.setString(3, billno);
				ps.setString(4, bingTime);
			}
		});
	}

	//根据运单号获取EMS接口表中订单号
	public String getCwbByAddTranscwb(String transcwb) {
		String sql = "select cwb from express_ems_order_b2ctemp where transcwb=? and addTranscwbFlag=1 limit 0,1";
		List<Map<String, Object>> cwbObj = this.jdbcTemplate.queryForList(sql,transcwb);
		String cwb = "";
		if ((cwbObj != null) && (cwbObj.size() > 0)) {
			Map<String, Object> map = cwbObj.get(0);
			cwb =  map.get("cwb")+"";
		}
		return cwb;
	}
	
	//根据运单号获取EMS接口表中订单信息
	public List<SendToEMSOrder> getOrderInfoByTranscwb(String transcwb) {
		String sql = "select * from express_ems_order_b2ctemp where cwb=? ";
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper(),transcwb);
		return list;
	}

	//保存要发送给ems的订单信息
	public void saveOrderInfo(final String cwb,final String transcwb,final String credate,
			final int addTranscwbFlag,final String data,final long orderDirection) {
		this.jdbcTemplate
		.update("insert into express_ems_order_b2ctemp(cwb,transcwb,credate,getMailnumFlag,addTranscwbFlag,data,order_direction) values(?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwb);
				ps.setString(2, transcwb);
				ps.setString(3, credate);
				ps.setInt(4, 0);
				ps.setInt(5, addTranscwbFlag);
				ps.setString(6, data);
				ps.setLong(7, orderDirection);
			}
		});
		
	}

	//获取需要发送给ems的订单信息
	public List<SendToEMSOrder> getSendToEMSOrderList() {
		String sql = "select * from express_ems_order_b2ctemp where "
				+ "state in(0,2) and resendCount<6 limit 0,2000 ";
		//测试
		/*String sql = "select * from express_ems_order_b2ctemp where "
				+ "state=0 limit 0,2 ";*/
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper());
		return list;
	}

	public void updateOrderTemp(String dataID, String dataError,int state) {
		String sql = "update express_ems_order_b2ctemp set state="+state+",remark='"+dataError+"',resendCount=resendCount+1 where transcwb=?";
		this.jdbcTemplate.update(sql,dataID);
	}

	//根据运单号获取发送给EMS的订单信息
	public List<SendToEMSOrder> getSendOrderByTranscwb(String dataID) {
		String sql = "select * from express_ems_order_b2ctemp where transcwb='"+dataID+"'";
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper());
		return list;
	}

	//根据运单号修改订单临时表中是否已获取运单的状态
	public List<SendToEMSOrder> updateGetTranscwbStateByTranscwb(String dataID) {
		String sql = "update express_ems_order_b2ctemp set getMailnumFlag=1 where transcwb="+dataID;
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper());
		return list;
	}
	
	
	//获取没有获取EMS运单号,且dmp订单信息已经推送给ems的数据
	public List<Map<String, Object>> getTranscwbs() {
		String sql = "select transcwb from express_ems_order_b2ctemp where getMailnumFlag=0 and state=1 order by id asc limit 0,1000";
		//测试
		//String sql = "select transcwb from express_ems_order_b2ctemp where getMailnumFlag=0 order by id asc limit 0,1";
		List<Map<String, Object>> transcwbs = this.jdbcTemplate.queryForList(sql);
		return transcwbs;
	}
	
	//获取没有获取EMS运单号的ems数据
	public List<EMSFlowEntity> getFlowByCondition(String transcwb,String mailNum,long emsFlowordertype, String action, String notproperdelivery) {
		String sql = "select * from express_ems_flow_info where transcwb='"+transcwb+"' and email_num='"+mailNum+"'" 
				+ " and emsFlowordertype="+emsFlowordertype+" and emsAction='"+action+"'";
		if("20".equals(action)){
			sql += " and notproperdelivery='" + notproperdelivery + "' ";
		} 
		List<EMSFlowEntity> entity = this.jdbcTemplate.query(sql,new EMSFlowMapper());
		return entity;
	}
	
	//根据transcwb获取运单号对照关系
	public long getListByTranscwb(String transcwb) {
		String sql = "select count(1) from express_ems_dmp_transcwb where transcwb='"+transcwb + "'";
		return this.jdbcTemplate.queryForLong(sql);
	}

	public void saveEMSFlowInfoToTemp(final String mailnum,final String listexpressmail,
			final String credate,final String action) {
		this.jdbcTemplate
		.update("insert into express_ems_flow_info_temp(email_num,flow_content,credate,action) values(?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, mailnum);
				ps.setString(2, listexpressmail);
				ps.setString(3, credate);
				ps.setString(4, action);
			}
		});		
	}
	
	//获取需要初步处理的ems轨迹信息
	public List<EMSFlowObjInitial> getFlowInfoList() {
		String sql = "select * from express_ems_flow_info_temp where "
				+ "state in(0,2) and handleCount<6 limit 0,2000 ";
		//测试
		/*String sql = "select * from express_ems_flow_info_temp where "
				+ "state in(0,2) and handleCount<6 limit 0,1 ";*/
		List<EMSFlowObjInitial> list = this.jdbcTemplate.query(sql, new EMSFlowObjInitialMapper());
		return list;
	}

	//修改轨迹临时表状态
	public void changeEmsFlowState(long id, int state, String remark) {
		String sql = "update express_ems_flow_info_temp set state="+state+",remark='"+remark+"', "
				+ "handleCount=handleCount+1 where id=?";
		this.jdbcTemplate.update(sql,id);
	}

	/*
	 * add by zhouhuan
	 * param transcwb:dmp运单号，emailno：邮政运单号
	 * remark:根据dmp运单号和邮政运单号获取订单信息
	 */
	public List<SendToEMSOrder> getSendOrderByNo(String transcwb, String emailno) {
		String sql = "SELECT * FROM express_ems_order_b2ctemp a JOIN express_ems_dmp_transcwb b ON a.transcwb=b.transcwb where transcwb='"+transcwb+"'";
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper());
		return list;
	}
	
	/**
	 * 根据订单查询订单邮政运单关系集合   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<SendToEMSOrder> getTransListByCwb(String cwb) {
		//注意:这里排序是必须的
		String sql  = "select * from express_ems_dmp_transcwb where cwb = ? order by bing_time";
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new EMSTranscwbMapper(), cwb);
		return list;
	}
	
	/**
	 * 根据订单查询订单邮政运单关系集合   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<SendToEMSOrder> getTransListByTransCwb(String transcwb) {
		String sql  = "select * from express_ems_dmp_transcwb where transcwb = ?";
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new EMSTranscwbMapper(), transcwb);
		return list;
	}
	
	public int getCountEMSViewListByTransCwb(String cwbType,String cwb, String starttime,String endtime,String status) {
		String sql  = "select count(1) from express_ems_dmp_transcwb ems left join express_ops_cwb_detail cwb on ems.cwb = cwb.cwb"
                      +" left join express_set_branch branch on cwb.deliverybranchid = branch.branchid "
                      +" where 1=1 and cwb.state = 1";
		sql += getWhereSql(cwbType, cwb, starttime, endtime, status);
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	private String getWhereSql (String cwbType,String cwb, String starttime,String endtime,String status) {
		String whereSql = ""; 
		if (!StringUtil.isEmpty(cwb)) {
       	  StringBuilder sb = new StringBuilder();
       	  for (String cwbNo : cwb.split(",")) {
       		  if (!"".equals(cwbNo.trim())) {
       			  if (sb.length() > 0) {
       				  sb.append(",'").append(cwbNo).append("'");
       			  } else {
       				  sb.append("'").append(cwbNo).append("'");
       			  }
       		  }
       	  }
       	  if (sb.length() > 0) {
       		  if ("0".equals(cwbType)) {
       			whereSql += " and ems.cwb in ("+sb.toString()+")";
           	  } else if ("1".equals(cwbType)) {
           		whereSql += " and ems.transcwb in ("+sb.toString()+")";
           	  } else if ("2".equals(cwbType)) {
           		whereSql += " and ems.email_num in ("+sb.toString()+")";
           	  }
       	  }
         }
         if (!StringUtil.isEmpty(starttime)) {
        	 whereSql += " and ems.bing_time > '"+starttime+"'";
         }
         if (!StringUtil.isEmpty(endtime)) {
        	 whereSql += " and ems.bing_time <= '"+endtime+"'";
         }
         return whereSql;
	}
	
	/**
	 * 根据订单查询订单邮政运单关系集合   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<EmsSmallPackageViewVo> getEMSViewListByTransCwb(int page,int pageSize,String cwbType,String cwb, String starttime,String endtime,String status) {
		String sql  = "select ems.*,branch.branchname,cwb.consigneename,cwb.consigneeaddress,cwb.realweight" 
                      +" from express_ems_dmp_transcwb ems left join express_ops_cwb_detail cwb on ems.cwb = cwb.cwb"
                      +" left join express_set_branch branch on cwb.deliverybranchid = branch.branchid"
                      +" where 1=1 and cwb.state = 1 ";
                      sql += getWhereSql(cwbType, cwb, starttime, endtime, status);
                      sql += " order by ems.bing_time desc limit ?,?";
		List<EmsSmallPackageViewVo> list = this.jdbcTemplate.query(sql, new EMSCwbInfoViewMapper(), ((page - 1) * pageSize), pageSize);
		return list;
	}
	
	/**
	 * 根据订单查询订单邮政运单关系集合   add by vic.liang@pjbest.com 2016-07-26
	 * @param cwb
	 * @return
	 */
	public List<EmsSmallPackageViewVo> getEMSViewListByTransCwb(String cwbType,String cwb, String starttime,String endtime,String status) {
		String sql  = "select ems.*,branch.branchname,cwb.consigneename,cwb.consigneeaddress,cwb.realweight" 
                      +" from express_ems_dmp_transcwb ems left join express_ops_cwb_detail cwb on ems.cwb = cwb.cwb"
                      +" left join express_set_branch branch on cwb.deliverybranchid = branch.branchid "
                      +" where 1=1 and cwb.state = 1";
                      sql += getWhereSql(cwbType, cwb, starttime, endtime, status);
                      sql += " order by ems.bing_time desc ";
		List<EmsSmallPackageViewVo> list = this.jdbcTemplate.query(sql, new EMSCwbInfoViewMapper());
		return list;
	}
	
	/**
	 * 更新订单/运单号绑定邮政运单号   add by vic.liang@pjbest.com 2016-07-25
	 * @param emscwb
	 * @param emscwbOld
	 * @return
	 */
	public int updateEmscwb (String transcwb, String scanems) {
		String sql = "update express_ems_dmp_transcwb set email_num = ? where transcwb = ?";
		return this.jdbcTemplate.update(sql,scanems,transcwb);
	}
}
