package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.AutoCommonStatusEnum;
import cn.explink.service.CwbRouteService;
import net.sf.json.JSONArray;


@Service
public class AutoOrderStatusService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BranchDAO branchDAO;
	
	@Autowired
	private CwbRouteService cwbRouteService;
	
	private final static String ORDER_STATUS_TMP_SAVE_SQL="insert into express_auto_order_status_tmp (cwb,operatetype,msg,createtime,status) values(?,?,?,CURRENT_TIMESTAMP,?)";
	//private final static String ORDER_STATUS_TMP_UPDATE_SQL="update express_auto_order_status_tmp set status=? where cwb=? and operatetype=? and status=1";
	private final static String ORDER_STATUS_TMP_DELETE_SQL="delete from express_auto_order_status_tmp where cwb=? and operatetype=?";

	private final static String ORDER_STATUS_TMP_QUERY_SQL="SELECT t.* FROM (SELECT * FROM express_auto_order_status_tmp order by createtime LIMIT ?,?) t"
			+" WHERE EXISTS(SELECT 1 FROM express_ops_cwb_detail c WHERE c.cwb=t.cwb)";
	private final static String ORDER_STATUS_TMP_TIMEOUT_QUERY_SQL=
			"SELECT t.* FROM (SELECT * FROM express_auto_order_status_tmp WHERE createtime<DATE_SUB(NOW(),INTERVAL 12 HOUR) LIMIT 0,?) t" 
			+" WHERE NOT EXISTS(SELECT 1 FROM express_ops_cwb_detail c WHERE c.cwb=t.cwb)";
	
	private final static String ORDER_STATUS_TMP_QUERY_COUNT_SQL="SELECT COUNT(createtime) FROM express_auto_order_status_tmp";
	
	@Transactional
	public void updateAutoOrder(String cwb,BigDecimal cargovolume,BigDecimal cargoweight,String packagecode,long deliverybranchid){
		Map<String,Object> paramMap=new HashMap<String,Object>();
		String sql="";
		if(cargovolume!=null&&cargovolume.floatValue()>0){
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
	
	@Transactional
	public void saveOrderStatusMsg(AutoPickStatusVo vo,String msg){
		//cwb can not be null ??????????
		trimData(vo);
		if(vo.getOrder_sn()==null||vo.getOrder_sn().length()==0){
			throw new RuntimeException("分拣状态报文中订单号不能为空");
		}
		
		if(vo.getOperate_type()==null||vo.getOperate_type().length()==0){
			throw new RuntimeException("分拣状态报文中操作类型不能为空");
		}
		
		String json=null;
		if(msg!=null&&msg.length()>0){
			json=msg;
		}else{		
			JSONArray jsonarray = JSONArray.fromObject(vo);  
			json=jsonarray.toString();
		}

		this.jdbcTemplate.update(ORDER_STATUS_TMP_SAVE_SQL, vo.getOrder_sn(),vo.getOperate_type(),json,AutoCommonStatusEnum.create.getValue());
		
		
	}
	
	private void trimData(AutoPickStatusVo vo){
		vo.setOrder_sn(vo.getOrder_sn()==null?null:vo.getOrder_sn().trim());
		vo.setOperate_type(vo.getOperate_type()==null?null:vo.getOperate_type().trim());
	}
	
	@Transactional
	public void completedOrderStatusMsg(int status,String cwb,String operatetype){
		//this.jdbcTemplate.update(ORDER_STATUS_TMP_UPDATE_SQL, status,cwb,operatetype);
		this.jdbcTemplate.update(ORDER_STATUS_TMP_DELETE_SQL,cwb,operatetype);
	}
	
	@Transactional
	public List<AutoOrderStatusTmpVo> retrieveOrderStatusMsg(int size){
		List<AutoOrderStatusTmpVo> resultList=new ArrayList<AutoOrderStatusTmpVo>();
		List<AutoOrderStatusTmpVo> rowList=null;
		int offset=0;
		int len=size;
		int total=0;
		boolean again=true;
		boolean isCount=true;
		do{
			rowList=jdbcTemplate.query(ORDER_STATUS_TMP_QUERY_SQL, new AutoOrderStatusTmpVoMapper(), offset,len);
			if(isCount&&(rowList==null||rowList.size()<size)){
				total=jdbcTemplate.queryForInt(ORDER_STATUS_TMP_QUERY_COUNT_SQL);
				isCount=false;
			}
			
			if(rowList!=null&&rowList.size()>0){
				resultList.addAll(rowList);
			}
			
			//翻页
			offset=offset+len;
			len=size-resultList.size();
			
			if(resultList.size()>=size||total<=size||offset>=total||len<1){
				again=false;
			}
			
		}while(again);
		
		return resultList;
	}
	
	@Transactional
	public List<AutoOrderStatusTmpVo> retrieveTimeoutOrderStatusMsg(int size){
		List<AutoOrderStatusTmpVo> rowList=jdbcTemplate.query(ORDER_STATUS_TMP_TIMEOUT_QUERY_SQL, new AutoOrderStatusTmpVoMapper(), size);

		return rowList;
	}
	
	private final class AutoOrderStatusTmpVoMapper implements RowMapper<AutoOrderStatusTmpVo> {
		@Override
		public AutoOrderStatusTmpVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			AutoOrderStatusTmpVo vo=new AutoOrderStatusTmpVo();
			vo.setCwb(rs.getString("cwb"));
			vo.setOperatetype(rs.getString("operatetype"));
			vo.setMsg(rs.getString("msg"));
			return vo;
		}
		
	}
}
