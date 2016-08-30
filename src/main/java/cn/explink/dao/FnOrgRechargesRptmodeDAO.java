package cn.explink.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.FnOrgRecharges;
import cn.explink.enumutil.OrgPayInTypeEnum;
import cn.explink.enumutil.OrgRechargeSourceEnum;
import cn.explink.enumutil.OrgRechargesHandleStatusEnum;
import cn.explink.util.DateTimeUtil;


@Component
public class FnOrgRechargesRptmodeDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public boolean isAutoReceivedPOSCOD(String cwb){
		int count = this.jdbcTemplate.queryForInt("select count(1) from fn_org_recharges_rptmode where cwb =? and recharge_source="
				+ OrgRechargeSourceEnum.PosCodAuto.getValue(), cwb); 
		return count>0;
	}
	
	

	public List <FnOrgRecharges>  batchInsertOrgRecharges(List <FnOrgRecharges> entities){
		final String sql="INSERT INTO fn_org_recharges_rptmode (recharge_no,org_id,paymethod,amount,surplus,"
				+ "remark,create_time,import_time,creator,bi_id,handle_status,picker,picker_id,payin_type,recharge_source,cwb,vpal_record_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List <FnOrgRecharges> list=new ArrayList<FnOrgRecharges>();
		for(final FnOrgRecharges vo:entities){
			KeyHolder keyHolder = new GeneratedKeyHolder();
				this.jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {					
					PreparedStatement ps = connection.prepareStatement(sql.toString(), new String[] { "id" });						
					ps.setString(1, vo.getRechargeNo());
					ps.setLong(2, vo.getOrgId()); 
					ps.setInt(3, vo.getPaymethod());
					ps.setBigDecimal(4,  vo.getAmount());
					ps.setBigDecimal(5,  vo.getSurplus());
					ps.setString(6,  vo.getRemark());
					ps.setString(7, DateTimeUtil.formatDate(vo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					ps.setString(8, DateTimeUtil.formatDate(vo.getImportTime(), "yyyy-MM-dd HH:mm:ss"));
					ps.setString(9, vo.getCreator());
					ps.setLong(10, vo.getBiId());
					ps.setInt(11, vo.getHandleStatus() == null ? OrgRechargesHandleStatusEnum.JustHandled.getValue() : vo.getHandleStatus());
					ps.setString(12, vo.getPicker() == null ? "" : vo.getPicker());
					ps.setLong(13, vo.getPickerId() == null ? 0l : vo.getPickerId());
					ps.setInt(14, vo.getPayinType() == null ? OrgPayInTypeEnum.StationPay.getValue() : vo.getPayinType());
					ps.setInt(15, vo.getRechargeSource() == null ? 0 : vo.getRechargeSource());
					ps.setString(16, vo.getCwb() == null ? "" : vo.getCwb());
					ps.setLong(17, vo.getVpalRecordId() == null ? 0l : vo.getVpalRecordId());
					return ps;
			}

		}, keyHolder);
			vo.setId(keyHolder.getKey().longValue());	
			list.add(vo);
		}
		return list;		
	}

	
}
