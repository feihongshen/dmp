package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.service.CwbRouteService;

@Service
public class AutoOrderStatusService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private BranchDAO branchDAO;
	
	@Autowired
	private CwbRouteService cwbRouteService;
	
	@Transactional
	public void updateAutoOrder(String cwb,BigDecimal cargovolume,BigDecimal cargoweight,String packagecode,long deliverybranchid){
		Map<String,Object> paramMap=new HashMap<String,Object>();
		String sql="";
		if(cargovolume!=null&&cargovolume.floatValue()>0){//??????????
			sql=sql+"cargovolume=:cargovolume,";
			paramMap.put("cargovolume", cargovolume);
		}
		
		if(cargoweight!=null&&cargoweight.floatValue()>0){
			sql=sql+"carrealweight=:carrealweight,";
			paramMap.put("carrealweight", cargoweight);
		}
		
		if(packagecode!=null&&packagecode.length()>0){ 
			sql=sql+"packagecode=:packagecode,";
			paramMap.put("packagecode", packagecode);
		}
		
		if(deliverybranchid>0){
			sql=sql+"deliverybranchid=:deliverybranchid,";
			paramMap.put("deliverybranchid", deliverybranchid);
		}

		
		if(sql.length()<1){
			return;
		}else{
			sql=sql.substring(0, sql.length()-1);
		}
		sql="update express_ops_cwb_detail set "+sql+" where cwb=:cwb and state=1";
		paramMap.put("cwb", cwb);
		
		this.namedParameterJdbcTemplate.update(sql, paramMap); 
		
	    //this.logger.debug("自动化出库时更新体积重量等订单信息", cwb);
	}
	
	@Transactional
	public long getDeliveryBranchId(String deliveryBranchCode){
		if(deliveryBranchCode==null||deliveryBranchCode.length()<1){
			return 0;
		}
		List<Branch> branchList=branchDAO.getBranchByBranchcode(deliveryBranchCode);
		if(branchList==null||branchList.size()<1){
			return 0;
		}
		long deliveryBranchId=branchList.get(0).getBranchid();
		return deliveryBranchId;
	}
	
	@Transactional
	public long getNextBranch(long currentBranchId,String deliveryBranchCode){
		long deliveryBranchId=getDeliveryBranchId(deliveryBranchCode);
		if(deliveryBranchId<1){
			return 0;
		}
		
		this.cwbRouteService.reload();//?????
		long nextbranchid = this.cwbRouteService.getNextBranch(currentBranchId, deliveryBranchId);
		
		return nextbranchid;
	}
	
}
