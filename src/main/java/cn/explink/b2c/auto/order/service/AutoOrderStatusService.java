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
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.AutoCommonStatusEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
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
	
	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	
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
	/**
	* @Title: getIntoWarePermitFlag 
	* @Description: tps自动化入库前校验订单是否处于数据导入状态
	* 				一票一件：主表的状态处于订单导入
	* 				一票多件：运单号处于数据导入状态（运单表没有数据，或者floworderetype为数据导入）
	* @param @param cwbOrder
	* @param @param scancwb
	* @param @return    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年9月28日 下午5:02:42 
	* @author 刘武强
	 */
	public void getIntoWarePermitFlag(CwbOrder cwbOrder, String scancwb){
		if(cwbOrder == null){//如果传进来的订单信息为空，则抛出异常
			throw new AutoWaitException("自动化入库时没找到此订单");
		}
		if(scancwb == null){//如果传进来的运单号为空，则抛出异常
			throw new AutoWaitException("自动化入库时运单号为空");
		}
		if(cwbOrder.getSendcarnum() == 1 && cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()){//如果是一票一件，订单主表的状态不是数据导入，也跑出去异常
			throw new AutoWaitException("一票一件，订单不处于数据导入状态，不允许自动化入库");
		}
		if(cwbOrder.getSendcarnum() > 1){//如果是一票多件,并且运单号存在
			if(cwbOrder.getCwb() != null && scancwb.trim().equals(cwbOrder.getCwb().trim())){
				this.logger.info("自动化入库，一票多件，运单号等于订单号");
				// TODO 如果一票多件，运单号等于订单号，要如何去校验，目前没有这种情况，所以不考虑
			}
			//查询运单的轨迹
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cwb", cwbOrder.getCwb());
			paramMap.put("scancwb", scancwb);
			paramMap.put("isnow", 1);
			//当前操作记录List
			List<TranscwbOrderFlow> currentOptList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, "t.credate DESC");
			//当前轨迹条数为0，则是导入数据;倘若有轨迹，那也只能是处于数据导入状态
			if(currentOptList.size() == 0 || (currentOptList.size() == 1 && currentOptList.get(0).getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())){
				return;
			}else{//如果运单不满足要求，则不允许自动化入库
				throw new AutoWaitException("一票多件，运单不处于数据导入状态，不允许自动化入库");
			}
		}
	} 
	
	/**
	* @Title: getOutWarePermitFlag 
	* @Description: tps自动化出库前校验订单是否处于数据导入状态
	* 				一票一件：主表的状态处于订单导入或者入库
	* 				一票多件：运单号处于数据导入状态或者入库（运单表没有数据，或者floworderetype为数据导入或入库）
	* @param @param cwbOrder    设定文件 
	* @param @param scancwb
	* @return void    返回类型 
	* @throws 
	* @date 2016年9月28日 下午5:41:53 
	* @author 刘武强
	 */
	public void getOutWarePermitFlag(CwbOrder cwbOrder, String scancwb){
		if(cwbOrder == null){//如果传进来的订单信息为空，则抛出异常
			throw new AutoWaitException("自动化出库时没找到此订单");
		}
		if(scancwb == null){//如果传进来的运单号为空，则抛出异常
			throw new AutoWaitException("自动化出库时运单号为空");
		}
		//如果是一票一件，订单主表的状态不是数据导入，也不是入库，也跑出去异常
		if(cwbOrder.getSendcarnum() == 1 && cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue() && cwbOrder.getFlowordertype() != FlowOrderTypeEnum.RuKu.getValue()){
			throw new AutoWaitException("一票一件，订单不处于数据导入状态，不允许自动化出库");
		}
		if(cwbOrder.getSendcarnum() > 1){//如果是一票多件,并且运单号存在
			if(cwbOrder.getCwb() != null && scancwb.trim().equals(cwbOrder.getCwb().trim())){
				this.logger.info("自动化出库，一票多件，运单号等于订单号");
				// TODO 如果一票多件，运单号等于订单号，要如何去校验，目前没有这种情况，所以不考虑
			}
			//查询运单的轨迹
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cwb", cwbOrder.getCwb());
			paramMap.put("scancwb", scancwb);
			paramMap.put("isnow", 1);
			//当前操作记录List
			List<TranscwbOrderFlow> currentOptList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, "t.credate DESC");
			//当前轨迹条数为0，则是导入数据;倘若有轨迹，那也只能是处于数据导入状态或者入库状态
			if(currentOptList.size() == 0 || (currentOptList.size() == 1 && (currentOptList.get(0).getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue() || cwbOrder.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()))){
				return;
			}else{//如果运单不满足要求，则不允许自动化出库
				throw new AutoWaitException("一票多件，运单不处于数据导入状态，不允许自动出库");
			}
		}
	}
	
	
}
