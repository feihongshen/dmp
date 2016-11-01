package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.explink.b2c.auto.order.mq.AutoExceptionSender;
import cn.explink.b2c.auto.order.mq.AutoMQExceptionDto;
import cn.explink.b2c.auto.order.mq.ConsumerTemplate;
import cn.explink.b2c.auto.order.service.AutoExceptionService;
import cn.explink.b2c.auto.order.service.AutoInWarehouseService;
import cn.explink.b2c.auto.order.service.AutoOutWarehouseService;
import cn.explink.b2c.auto.order.service.AutoPickStatusVo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tps.TpsCwbFlowService;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.enumutil.AutoCommonStatusEnum;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONArray;

@Service
public class AutoDispatchStatusService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AutoOutWarehouseService autoOutWarehouseService;
	@Autowired
	private AutoInWarehouseService autoInWarehouseService;
	@Autowired
	private AutoExceptionSender autoExceptionSender;
	@Autowired
	private AutoExceptionService autoExceptionService;
	@Autowired
	@Qualifier("autoDispatchStatusConsumer")
	private ConsumerTemplate consumerTemplate;
	@Autowired
	private JointService jointService;
	@Autowired
	private AutoOrderStatusService autoOrderStatusService;
	@Autowired
	private TpsCwbFlowService tpsCwbFlowService;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private AutoUserService autoUserService;
	
	public static final String OPERATE_TYPE_IN="1";//1是入库，3是出库
	public static final String OPERATE_TYPE_OUT="3";//1是入库，3是出库
	
	private User user=null;
	private String today="";

	public void process() {
	       this.logger.info("auto dispatch job process start...");
	       int todoLen=3000;//每次查询条数
	       int maxtry=10;//最多查询次数
	       int waitNum=0;//处理不成功的条数
	       boolean dbhavedata=true;//表末尾
	       int isOpenFlag =0;
	       int offset=0;
	       
	       do{
	    	maxtry=maxtry-1;
	        List<AutoMQExceptionDto> errorList=null;

	        try {
	        	isOpenFlag=this.autoUserService.getAutoFlag();
	        	if(isOpenFlag!=1){
	        		this.logger.info("自动化分拣状态处理未开启.");
	        		return;
	        	}
	           
	        	if(isOpenFlag==1){
	        		/*
	        		 if(autoExceptionSender.getChannel()==null||autoExceptionSender.getChannel().length()==0){
	        			this.logger.warn("MQ autoExceptionSender is not initilzed yet.auto dispatch job process is stopped!!!");
	        			return;
	        		}
	        		*/
	        		
		    		if(user==null){
		    			user=this.autoUserService.getSessionUser();
		    		}else{
		    			user.setBranchid(this.autoUserService.getPickBranch());//缓存刷新时也刷新
		    		}
		    		this.logger.info("auto dispatch task start...");
		    		
		    		offset=offset+waitNum;
		            //加载临时表数据
		            List<AutoPickStatusMsgVo> voList= this.retrieveOrderStatusMsg(offset,todoLen);
		            if(voList==null||voList.size()==0||voList.size()<todoLen){
		            	dbhavedata=false;
		            }
		            //处理数据
		            Map<String,Object> resultMap=handleData(voList,user);
		            List<AutoMQExceptionDto> dataErrorList=(List<AutoMQExceptionDto>) resultMap.get("errorList");
		            waitNum=Integer.parseInt(resultMap.get("waitNum").toString());
		            //反馈错误
		            if(dataErrorList!=null){
		            	if(errorList==null){
		            		errorList=new ArrayList<AutoMQExceptionDto>();
		            	}
		            	errorList.addAll(dataErrorList);
		            	
		            }
	        	}

	        } catch (Throwable ex) {
	        	logger.error("auto dispatch job process error",ex);
	        	String errinfo="DMP分拣状态数据临时表转业务时出错."+ex.getMessage();
	        	try{
		        	long msgid=this.autoExceptionService.createAutoExceptionMsg("",AutoInterfaceEnum.fenjianzhuangtai.getValue());
		        	this.autoExceptionService.createAutoExceptionDetail("","", errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
	        	} catch (Exception ee) {
	        		logger.error("createAutoException error",ee);
	        	}
	        	
				
	        } finally {
	        	try{
	        		sendErrorResponse(errorList);
		        } catch (Throwable et) {
		        	logger.error("反馈异常到TPS时有出错，sendErrorResponse error",et);
		        }
	            
	        }

	       }while(waitNum>0&&dbhavedata&&maxtry>0);
	       
	       if(isOpenFlag==1){
	        	//处理那些超过7天没订单的分拣状态记录
	    	   housekeepData();
	      }
		
	}
	
	private void sendErrorResponse(List<AutoMQExceptionDto> errorList){
		if(errorList!=null){
			for(AutoMQExceptionDto err:errorList){
				String errorMsg="";
				try{
					errorMsg=encodeErrorMsg(err);
					autoExceptionSender.send(errorMsg);
				} catch (Throwable et) {
					String errinfo="DMP反馈异常到TPS时出错.channelName:"+autoExceptionSender.getChannel()+","+"channelKey:"+autoExceptionSender.getChannelKey()+".";
		        	logger.error(errinfo+",TPS errorMsg:"+errorMsg,et);

		        	long refid=err.getRefid();//
		        	try{
			        	long msgid=this.autoExceptionService.createAutoExceptionMsg(errorMsg, AutoInterfaceEnum.fankui_fanjian.getValue());
			        	this.autoExceptionService.createAutoExceptionDetail(err.getBusiness_id(),"",errinfo+et.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, refid,"");
					} catch (Exception ee) {
		        		logger.error("createAutoException error",ee);
		        	}
				}
			}
		}
		
	}
	
	private String encodeErrorMsg(AutoMQExceptionDto mqe) throws Exception{
		mqe.setCreate_time(DateTimeUtil.getNowTime());//
		mqe.setExchange_name(consumerTemplate.getExchangeName());//
		mqe.setQueue_name(consumerTemplate.getQueueName());//
		mqe.setRemark("");
		mqe.setRouting_key("*");//
		mqe.setSystem_name("DMP");//
		mqe.setMessage("<![CDATA["+mqe.getMessage()+"]]>");//250 length?
		
		String msg=XmlUtil.toXml(AutoMQExceptionDto.class, mqe); 
		msg=StringEscapeUtils.unescapeXml(msg);
		
		logger.debug("反馈TPS的报文 xml:"+msg);
		
		return msg;
	}

	
	private Map<String,Object> handleData(List<AutoPickStatusMsgVo> msgVoList,User user){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		int waitNum=0;
		long cnt=0;
		if(msgVoList!=null){
			cnt=msgVoList.size();
		}
		logger.info("auto dispatch retrieve msg with cwb size:"+cnt);
		
		List<AutoMQExceptionDto> errorList=null;
		if(msgVoList==null||msgVoList.size()<1){
			resultMap.put("errorList", errorList);
			resultMap.put("waitNum", waitNum);
			return resultMap;
		}

		boolean needHebao=this.autoUserService.getHebaoFlag()==1?false:true;
		AutoPickStatusVo currentVo=null;
		for(AutoPickStatusMsgVo msgVo:msgVoList){
			AutoPickStatusVo vo=msgVo.getAutoPickStatusVo();
			currentVo=vo;
			try {
				if(OPERATE_TYPE_IN.equals(vo.getOperate_type())){
					autoInWarehouseService.autoInWarehouse(vo,user);
					logger.info("模拟入库成功,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no());
				}else if(OPERATE_TYPE_OUT.equals(vo.getOperate_type())){
					autoOutWarehouseService.autOutWarehouse(vo,user,needHebao);
					logger.info("模拟出库成功,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no());
				}else{
					throw new RuntimeException("分拣状态报文中未明的操作类型:"+vo.getOperate_type());
				}
				autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.success.getValue(),vo.getOrder_sn(),vo.getOperate_type(),vo.getBox_no());
			} catch (Exception e) {
				String errinfo="DMP分拣状态数据转业务时出错."+e.getMessage();
				long detailId=0;
				boolean feedbackTps=true;//需要反馈tps
				boolean needDbLog=true;//需要写到异常表
				boolean isWait=false;//需要保留消息在临时表以等待订单数据
				boolean fullLog=true;//是否打印完整异常堆栈
				boolean emaildateLog=false;
				
				try {
					//出仓时间是无论入库正常或异常都要保存;AutoWaitException是等待订单或运单导入
					if (OPERATE_TYPE_IN.equals(vo.getOperate_type())&& !(e instanceof AutoWaitException)) {
						CwbOrder co = this.cwbDAO.getCwbByCwb(vo.getOrder_sn());
						BigDecimal weight=null;
						BigDecimal volume=null;
						if(currentVo!=null){
							weight=currentVo.getOriginal_weight()==null||currentVo.getOriginal_weight().trim().length()<1?null:new BigDecimal(currentVo.getOriginal_weight());
							volume=currentVo.getOriginal_volume()==null||currentVo.getOriginal_volume().trim().length()<1?null:new BigDecimal(currentVo.getOriginal_volume());
						}
						this.tpsCwbFlowService.save(co, vo.getBox_no(), FlowOrderTypeEnum.RuKu,user.getBranchid(), vo.getOperate_time(),false,weight,volume);
						logger.info("模拟入库异常时保存了出仓时间,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no());
					} 
				} catch (Exception e2) {
					emaildateLog=true;
					feedbackTps=false;
					logger.error("模拟入库保存出仓时间时出错,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no()+"operatetype:"+vo.getOperate_type(),e2);
					errinfo=errinfo+".模拟入库保存出仓时间时出错,"+e2.getMessage();
				}
				
				try{
					if(e instanceof CwbException){
						CwbException cwbe=(CwbException) e;
						if (cwbe.getError()!=null&&cwbe.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
							feedbackTps=false;
							needDbLog=false;
							fullLog=false;
						}else if (cwbe.getError()!=null&&cwbe.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU.getValue()) {
							feedbackTps=false;
							needDbLog=false;
							fullLog=false;
						}else if (cwbe.getError()!=null&&cwbe.getError().getValue() == ExceptionCwbErrorTypeEnum.OUTWAREHOUSE_MPS_NOT_ALL_ARRIVED.getValue()) {
							//此处集单模式下出库时发生;因为按包出库原因，自动化要求必须不能是库房集单，所以不会有此异常
							isWait=false;
							feedbackTps=false;
							needDbLog=false;
							fullLog=false;
						}else if (cwbe.getError()!=null&&cwbe.getError().getValue() == ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR.getValue()) {
							//此处入库时已有一件出库了时发生;虽然出库时也会发生，但设置为强制出库以及按包出库，自动化下忽略validateYipiaoduojianState方法里的等待，所以不会有此异常
							feedbackTps=false;
							needDbLog=false;
							fullLog=false;
						}else if (cwbe.getError()!=null&&cwbe.getError().getValue() == ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR.getValue()) {
							//此处出库时发生
							isWait=false;
							feedbackTps=false;
							needDbLog=false;
							fullLog=false;
						}else if(cwbe.getError()==null){
							isWait=false;
							feedbackTps=false;
						}
					}
					
					if(emaildateLog){
						needDbLog=true;
					}
					
					if(isWait||e instanceof AutoWaitException){
						feedbackTps=false;
						waitNum=waitNum+1;
						List<Map<String,Object>> detailList=this.autoExceptionService.queryAutoExceptionDetail(vo.getOrder_sn(),vo.getBox_no(),vo.getOperate_type());
						if(detailList!=null&&detailList.size()>0){
							Map<String,Object> detailMap= detailList.get(0);
							long msgid=detailMap.get("msgid")==null?0:Long.parseLong(detailMap.get("msgid").toString());
							detailId=detailMap.get("id")==null?0:Long.parseLong(detailMap.get("id").toString());
							this.autoExceptionService.updateAutoExceptionDetail(detailId, AutoExceptionStatusEnum.xinjian.getValue(), errinfo,msgid,msgVo.getMsg());
						}else{
							long msgid=this.autoExceptionService.createAutoExceptionMsg(msgVo.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
							detailId=this.autoExceptionService.createAutoExceptionDetail(vo.getOrder_sn(),vo.getBox_no(),errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0,vo.getOperate_type());
						}
					}else{
						if(needDbLog){
							long msgid=this.autoExceptionService.createAutoExceptionMsg(msgVo.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
							detailId=this.autoExceptionService.createAutoExceptionDetail(vo.getOrder_sn(),vo.getBox_no(),errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0,vo.getOperate_type());
							
							autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(),vo.getOrder_sn(),vo.getOperate_type(),vo.getBox_no());
						}else{
							autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.success.getValue(),vo.getOrder_sn(),vo.getOperate_type(),vo.getBox_no());
						}
					}
				} catch (Exception ee) {
	        		logger.error("保存自动化异常时出错.",ee);
	        	}finally{
					if(fullLog){
						logger.error("处理分拣状态出错，handleData error,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no()+"operatetype:"+vo.getOperate_type(),e);
					}else{
						logger.error("处理分拣状态出错，handleData error,cwb:"+vo.getOrder_sn()+",transcwb:"+vo.getBox_no()+"operatetype:"+vo.getOperate_type()+",error:"+e.getMessage());
					}
	        	}
				
				
				
				if(feedbackTps){
					if(errorList==null){
						errorList=new ArrayList<AutoMQExceptionDto>();
					}
					AutoMQExceptionDto mqe=new AutoMQExceptionDto();
					mqe.setBusiness_id(vo.getOrder_sn());
					mqe.setException_info(errinfo);
					mqe.setMessage(msgVo.getMsg());
					mqe.setRefid(detailId);
					errorList.add(mqe);
				}
			}
		}
		
		resultMap.put("errorList", errorList);
		resultMap.put("waitNum", waitNum);
		logger.info("auto dispatch msg processed successfully size:"+(cnt-(errorList==null?0:errorList.size())));
		
		return resultMap;
	}
	
	private void housekeepData(){
		try{
			int hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour==2){//每天2点只运行一次
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String now=sdf.format(Calendar.getInstance().getTime());
				if(!now.equals(this.today)){
					logger.info("清理自动化分拣临时表");
					this.today=now;
					handleTimeoutData();
				}
			}
		} catch (Exception ex) {
			logger.error("清理自动化分拣临时表的timeout数据时出错.", ex);
		}
	}
	
	private void handleTimeoutData(){
		int timeoutHour=24*7;//集单等待7天
		List<AutoOrderStatusTmpVo> timeOutVoList= autoOrderStatusService.retrieveTimeoutOrderStatusMsg(timeoutHour,3000);//?????
		long cnt=0;
		if(timeOutVoList!=null){
			cnt=timeOutVoList.size();
		}
		logger.info("auto dispatch timeout msg num:"+cnt);
		
		if(timeOutVoList==null||timeOutVoList.size()<1){
			return;
		}
		List<AutoMQExceptionDto> errorList=new ArrayList<AutoMQExceptionDto>();
		String errinfo="DMP处理自动化分拣状态时等待订单相关数据超时.";
		try{
			for(AutoOrderStatusTmpVo vo:timeOutVoList){
				long detailid=autoExceptionService.createAutoDispatchTimeoutException(vo,errinfo);
				
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id(vo.getCwb());
				mqe.setException_info(errinfo);
				mqe.setMessage(vo.getMsg());
				mqe.setRefid(detailid);
				errorList.add(mqe);
				
			}
		} catch (Exception ee) {
	    	logger.error("handleTimeoutData error",ee);
		}
		    	
		sendErrorResponse(errorList);
	}
	
	private List<AutoPickStatusMsgVo> retrieveOrderStatusMsg(int offset,int size){
		 List<AutoOrderStatusTmpVo> rowList= autoOrderStatusService.retrieveOrderStatusMsg(offset,size);
		 if(rowList==null){
			 return null;
		 }
		List<AutoPickStatusMsgVo> dataList=new ArrayList<AutoPickStatusMsgVo>();
		for(AutoOrderStatusTmpVo row:rowList){
			try{
				JSONArray jsonarray = JSONArray.fromObject(row.getMsg());  
			    List<AutoPickStatusVo> voList = (List<AutoPickStatusVo>)JSONArray.toCollection(jsonarray,AutoPickStatusVo.class);  
			    AutoPickStatusMsgVo msgVo=new AutoPickStatusMsgVo();
			    msgVo.setAutoPickStatusVo(voList.get(0));
			    msgVo.setMsg(row.getMsg());
			    dataList.add(msgVo);
			} catch (Exception e) {
	    		logger.error("retrieveOrderStatusMsg json error,cwb="+row.getCwb()+",operatetype="+row.getOperatetype(),e);
	    		String errinfo="DMP分拣状态json数据转vo时出错."+e.getMessage();
   		
				long msgid=this.autoExceptionService.createAutoExceptionMsg(row.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
				this.autoExceptionService.createAutoExceptionDetail(row.getCwb(),row.getBoxno(),errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0,row.getOperatetype());
				
				autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(), row.getCwb(), row.getOperatetype(),row.getBoxno());
	    	}
		}
		return dataList;
	}


}
