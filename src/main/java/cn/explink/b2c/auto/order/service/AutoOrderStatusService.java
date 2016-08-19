package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cn.explink.service.KufangBranchMappingService;
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
	
	@Autowired
	private AutoExceptionService autoExceptionService;
	
	@Autowired
	KufangBranchMappingService kufangBranchMappingService;
	
	private final static String ORDER_STATUS_TMP_SAVE_SQL="insert into express_auto_order_status_tmp (cwb,transportno,operatetype,msg,createtime,status) values(?,?,?,?,?,?)";
	//private final static String ORDER_STATUS_TMP_UPDATE_SQL="update express_auto_order_status_tmp set status=? where cwb=? and operatetype=? and status=1";
	private final static String ORDER_STATUS_TMP_DELETE_SQL="delete from express_auto_order_status_tmp where cwb=? and transportno=? and operatetype=?";

	private final static String ORDER_STATUS_TMP_QUERY_SQL="SELECT * FROM express_auto_order_status_tmp order by createtime LIMIT ?,?";
	private final static String ORDER_STATUS_TMP_TIMEOUT_QUERY_SQL=
			"SELECT t.* FROM (SELECT * FROM express_auto_order_status_tmp WHERE createtime<DATE_SUB(NOW(),INTERVAL ? HOUR) LIMIT 0,?) t" 
			+" WHERE NOT EXISTS(SELECT 1 FROM express_ops_cwb_detail c WHERE c.cwb=t.cwb)";
		
	private final static String TRANS_CWB_DETAIL_UPDATE_SQL="update express_ops_transcwb_detail set cargovolume=?,carrealweight=? where cwb=? and transcwb=?";;
	private static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	@Transactional
	public void updateAutoOrder(String cwb,String boxno,BigDecimal cargovolume,BigDecimal cargoweight,long deliverybranchid,boolean isJidan){
		if(isJidan){
			int cnt=jdbcTemplate.update(TRANS_CWB_DETAIL_UPDATE_SQL,cargovolume,cargoweight,cwb,boxno);
			if(cnt==0){
				//此处应该不会发生，因为前面已经验证只有等全部入库才能做出库
				throw new AutoWaitException("集单模式下出库更新重量体积时有运单还没入库,箱号="+boxno);
			}
		}
		
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		String sql="";
		if(cargovolume!=null&&cargovolume.floatValue()>0){
			sql=sql+"cargovolume=cargovolume+:cargovolume,";//when init value is null???
			paramMap.put("cargovolume", cargovolume);
		}
		
		if(cargoweight!=null&&cargoweight.floatValue()>0){
			sql=sql+"carrealweight=carrealweight+:carrealweight,";
			paramMap.put("carrealweight", cargoweight);
		}
		
		/*if(packagecode!=null&&packagecode.length()>0){ 
			sql=sql+"packagecode=:packagecode,";
			paramMap.put("packagecode", packagecode);
		}*/
		
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
		
	}
	
	@Transactional
	public long getDeliveryBranchId(String deliveryBranchCode){
		if(deliveryBranchCode==null||deliveryBranchCode.length()<1){
			return 0;
		}
		List<Branch> branchList=branchDAO.getBranchByTpsBranchcode(deliveryBranchCode);
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
		//检查是否要到二级分拣库
		long nextbranchid = kufangBranchMappingService.getNextBranch(deliveryBranchId);
		if(nextbranchid==0){
			//常规的路由
			this.cwbRouteService.reload();//?????
			nextbranchid = this.cwbRouteService.getNextBranch(currentBranchId, deliveryBranchId);
		}
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

		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
		Date operateTime=null;
		try {
			 operateTime=sdf.parse(vo.getOperate_time());
		} catch (Exception e) {
			throw new RuntimeException("parse date error. operate_time="+vo.getOperate_time());
		}
		this.jdbcTemplate.update(ORDER_STATUS_TMP_SAVE_SQL, vo.getOrder_sn(),vo.getBox_no(),vo.getOperate_type(),json,operateTime,AutoCommonStatusEnum.create.getValue());
		
		
	}
	
	private void trimData(AutoPickStatusVo vo){
		vo.setOrder_sn(vo.getOrder_sn()==null?null:vo.getOrder_sn().trim());
		vo.setOperate_type(vo.getOperate_type()==null?null:vo.getOperate_type().trim());
	}
	
	@Transactional
	public void completedOrderStatusMsg(int status,String cwb,String operatetype,String transportno){
		//this.jdbcTemplate.update(ORDER_STATUS_TMP_UPDATE_SQL, status,cwb,operatetype);
		this.jdbcTemplate.update(ORDER_STATUS_TMP_DELETE_SQL,cwb,transportno,operatetype);
		if(status==AutoCommonStatusEnum.success.getValue()){
			List<Map<String,Object>> detailList=this.autoExceptionService.queryAutoExceptionDetail(cwb,transportno,operatetype);
			if(detailList!=null&&detailList.size()>0){
				Map<String,Object> detailMap= detailList.get(0);
				long msgid=detailMap.get("msgid")==null?0:Long.parseLong(detailMap.get("msgid").toString());
				long detailId=detailMap.get("id")==null?0:Long.parseLong(detailMap.get("id").toString());
				this.autoExceptionService.deleteAutoExceptionDetail(detailId, msgid);
			}
		}
	}
	
	@Transactional
	public List<AutoOrderStatusTmpVo> retrieveOrderStatusMsg(int offset,int size){
		List<AutoOrderStatusTmpVo> resultList=jdbcTemplate.query(ORDER_STATUS_TMP_QUERY_SQL, new AutoOrderStatusTmpVoMapper(), offset,size);
		
		return resultList;
	}
	
	@Transactional
	public List<AutoOrderStatusTmpVo> retrieveTimeoutOrderStatusMsg(int timeoutHour,int size){
		List<AutoOrderStatusTmpVo> rowList=jdbcTemplate.query(ORDER_STATUS_TMP_TIMEOUT_QUERY_SQL, new AutoOrderStatusTmpVoMapper(),timeoutHour, size);

		return rowList;
	}
	
	private final class AutoOrderStatusTmpVoMapper implements RowMapper<AutoOrderStatusTmpVo> {
		@Override
		public AutoOrderStatusTmpVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			AutoOrderStatusTmpVo vo=new AutoOrderStatusTmpVo();
			vo.setCwb(rs.getString("cwb"));
			vo.setBoxno(rs.getString("transportno"));
			vo.setOperatetype(rs.getString("operatetype"));
			vo.setMsg(rs.getString("msg"));
			return vo;
		}
		
	}
}
