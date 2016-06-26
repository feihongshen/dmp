package cn.explink.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.enumutil.OrgRechargeSourceEnum;


@Component
public class FnOrgRechargesRptmodeDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public boolean isAutoReceivedPOSCOD(String cwb){
		int count = this.jdbcTemplate.queryForInt("select count(1) from fn_org_recharges_rptmode where cwb =? and recharge_source="
				+ OrgRechargeSourceEnum.PosCodAuto.getValue(), cwb); 
		return count>0;
	}

	
}
