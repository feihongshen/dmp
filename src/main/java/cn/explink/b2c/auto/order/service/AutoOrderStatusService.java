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
	
	private final static String ORDER_STATUS_TMP_SAVE_SQL="insert into express_auto_order_status_tmp (cwb,transportno,operatetype,msg,createtime,status) values(?,?,?,?,?,?)";
	//private final static String ORDER_STATUS_TMP_UPDATE_SQL="update express_auto_order_status_tmp set status=? where cwb=? and operatetype=? and status=1";
	private final static String ORDER_STATUS_TMP_DELETE_SQL="delete from express_auto_order_status_tmp where cwb=? and transportno=? and operatetype=?";

	private final static String ORDER_STATUS_TMP_QUERY_SQL="SELECT t.* FROM (SELECT * FROM express_auto_order_status_tmp order by createtime LIMIT ?,?) t"
			+" WHERE EXISTS(SELECT 1 FROM express_ops_cwb_detail c WHERE c.cwb=t.cwb)";
	private final static String ORDER_STATUS_TMP_TIMEOUT_QUERY_SQL=
			"SELECT t.* FROM (SELECT * FROM express_auto_order_status_tmp WHERE createtime<DATE_SUB(NOW(),INTERVAL 12 HOUR) LIMIT 0,?) t" 
			+" WHERE NOT EXISTS(SELECT 1 FROM express_ops_cwb_detail c WHERE c.cwb=t.cwb)";
	
	private final static String ORDER_STATUS_TMP_QUERY_COUNT_SQL="SELECT COUNT(createtime) FROM express_auto_order_status_tmp";
	
	private static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	@Transactional
	public void updateAutoOrder(String cwb,String boxno,BigDecimal cargovolume,BigDecimal cargoweight,String packagecode,long deliverybranchid,boolean isJidan){
		if(isJidan){
			//this.getTransCwbDetailDAO() ？
			String updateWVSql="update express_ops_transcwb_detail set cargovolume=?,carrealweight=? where cwb=? and transcwb=?";
			
			//String queryWVSql="select id from express_ops_transcwb_detail where cwb=? and transcwb=?";//state?
			//String insertWVSql="insert express_ops_transcwb_detail () values()";
			/*List<Map<String, Object>> list=jdbcTemplate.queryForList(queryWVSql,cwb,boxno);
			if(list!=null&&list.size()>0){
				String id=list.get(0).get("id").toString();
				jdbcTemplate.update(updateWVSql,cargovolume,cargoweight,id);
			}else{
				//insert
			}*/
			int cnt=jdbcTemplate.update(updateWVSql,cargovolume,cargoweight,cwb,boxno);
			if(cnt==0){
				//logger.error("do not find the transcwb detail. cwb={},transcwb={}",cwb,boxno);
				throw new TranscwbNoFoundException("DMP do not find the transfer order detail data. cwb="+cwb+",boxno="+boxno);
			}
			/*else{
				String sumSql="select cwb,sum(cargovolume) as totalvolume,sum(carrealweight) as totalweight from express_ops_transcwb_detail where cwb=?";
				Map<String,Object> wvMap=jdbcTemplate.queryForMap(sumSql,cwb);
				totalCargovolume=(BigDecimal) wvMap.get("totalvolume");
				totalCargoweight=(BigDecimal) wvMap.get("carrealweight");
			}*/
			
			//重复出入库异常与什么有关？（扫箱号标志，集单标志？）
			//validateCwbChongFu()
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
			vo.setTransportno(rs.getString("transportno"));
			vo.setOperatetype(rs.getString("operatetype"));
			vo.setMsg(rs.getString("msg"));
			return vo;
		}
		
	}
}
