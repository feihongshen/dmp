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
			return en; 
		}
	}
	
	private final class SendToEMSOrderMapper implements RowMapper<SendToEMSOrder> {
		@Override
		public SendToEMSOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			SendToEMSOrder order = new SendToEMSOrder();
			order.setTranscwb(rs.getString("cwb"));
			order.setTranscwb(rs.getString("transcwb"));
			order.setCredate(rs.getString("credate"));
			order.setAddTranscwbFlag(rs.getString("addTranscwbFlag"));
			order.setData(rs.getString("data"));
			order.setGetMailnumFlag(rs.getString("getMailnumFlag"));
			return order; 
		}
	}
	
    //保存ems运单轨迹报文信息
	public void saveEMSFlowInfo(final String transcwb, final String mailnum,
			final String listexpressmail,final String action,final long emsFlowordertype,
			final String properdelivery,final String notproperdelivery,final String credate) {
		this.jdbcTemplate
		.update("insert into express_ems_flow_info(transcwb,email_num,flow_content,emsAction,emsFlowordertype,"
				+ "properdelivery,notproperdelivery,credate) values(?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
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

	//根据ems运单号获取dmp运单号
	public List<EMSFlowEntity> getEMSFlowEntityList() {
		String sql = "select * from express_ems_flow_info where state=0 order by credate limit 0,2000";
		List<EMSFlowEntity> emsFlowEntityList = this.jdbcTemplate.query(sql, new EMSFlowMapper());
		return emsFlowEntityList;
	}

	//根据ems轨迹模拟dmp操作的处理结果，修改处理状态
	public void changeEmsTraceDataState(long id,int state,String remark) {
		String sql = "update express_ems_flow_info set state="+state+",remark='"+remark+"' where id=?";
		this.jdbcTemplate.update(sql,id);
	}

	//保存ems运单与dmp运单对照关系
	public void saveEMSEmailnoAndDMPTranscwb(final String cwb,final String transcwb,final String billno) {
		this.jdbcTemplate
		.update("insert into express_ems_dmp_transcwb(cwb,transcwb,email_num) values(?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwb);
				ps.setString(2, transcwb);
				ps.setString(3, billno);
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
			final int addTranscwbFlag,final String data) {
		this.jdbcTemplate
		.update("insert into express_ems_order_b2ctemp(cwb,transcwb,credate,getMailnumFlag,addTranscwbFlag,data) values(?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwb);
				ps.setString(2, transcwb);
				ps.setString(3, credate);
				ps.setInt(4, 0);
				ps.setInt(5, addTranscwbFlag);
				ps.setString(6, data);
			}
		});
		
	}

	//获取需要发送给ems的订单信息
	public List<SendToEMSOrder> getSendToEMSOrderList() {
		String sql = "select * from express_ems_order_b2ctemp where "
				+ "state=0 limit 0,2000 ";
		//测试
		/*String sql = "select * from express_ems_order_b2ctemp where "
				+ "state=0 limit 0,2 ";*/
		List<SendToEMSOrder> list = this.jdbcTemplate.query(sql, new SendToEMSOrderMapper());
		return list;
	}

	public void updateOrderTemp(String dataID, String dataError,int state) {
		String sql = "update express_ems_order_b2ctemp set state="+state+",remark='"+dataError+"' where transcwb=?";
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
		/*String sql = "select transcwb from express_ems_order_b2ctemp where getMailnumFlag=0 and state=1 order by id asc limit 0,500";*/
		String sql = "select transcwb from express_ems_order_b2ctemp where getMailnumFlag=0 order by id asc limit 0,500";
		List<Map<String, Object>> transcwbs = this.jdbcTemplate.queryForList(sql);
		return transcwbs;
	}
	
	//获取没有获取EMS运单号的ems数据
	public List<EMSFlowEntity> getFlowByCondition(String transcwb,String mailNum,long emsFlowordertype, String action) {
		String sql = "select * from express_ems_flow_info where transcwb='"+transcwb+"' and email_num='"+mailNum+"'" 
				+ " and emsFlowordertype="+emsFlowordertype+" and emsAction='"+action+"'";
		List<EMSFlowEntity> entity = this.jdbcTemplate.query(sql,new EMSFlowMapper());
		return entity;
	}
	
	//根据transcwb获取运单号对照关系
	public long getListByTranscwb(String transcwb) {
		String sql = "select count(1) from express_ems_dmp_transcwb where transcwb="+transcwb;
		return this.jdbcTemplate.queryForLong(sql);
	}
}
