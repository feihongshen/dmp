package cn.explink.b2c.auto.order.service;

import java.util.ArrayList;
import java.util.List;

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
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.domain.User;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.AutoCommonStatusEnum;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	
	public static final String OPERATE_TYPE_IN="1";//1是入库，3是出库
	public static final String OPERATE_TYPE_OUT="3";//1是入库，3是出库
	
	private User user=null;

	public void process() {
	       this.logger.info("auto dispatch job process start...");

	        List<AutoMQExceptionDto> errorList=null;

	        int isOpenFlag =0; 
	        try {
	        	isOpenFlag=this.jointService.getStateForJoint(B2cEnum.VipShop_TPSAutomate.getKey());
	           
	        	if(isOpenFlag==1){
	        		/*
	        		 if(autoExceptionSender.getChannel()==null||autoExceptionSender.getChannel().length()==0){
	        			this.logger.warn("MQ autoExceptionSender is not initilzed yet.auto dispatch job process is stopped!!!");
	        			return;
	        		}
	        		*/
	        		
		    		if(user==null){
		    			user=this.getSessionUser();
		    		}
		    		this.logger.info("auto dispatch task start...");
		            
		            //加载临时表数据
		            List<AutoPickStatusMsgVo> voList= this.retrieveOrderStatusMsg(3000);//????
		            //处理数据
		            List<AutoMQExceptionDto> dataErrorList=handleData(voList,user);
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
		        	this.autoExceptionService.createAutoExceptionDetail("","", errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0);
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
	        
	        if(isOpenFlag==1){
	        	//处理那些超过1小时没订单的分拣状态记录
	        	handleTimeoutData();
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
			        	this.autoExceptionService.createAutoExceptionDetail(err.getBusiness_id(),"",errinfo+et.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, refid);
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

	
	private List<AutoMQExceptionDto> handleData(List<AutoPickStatusMsgVo> msgVoList,User user){
		List<AutoMQExceptionDto> errorList=null;
		if(msgVoList==null||msgVoList.size()<1){
			return errorList;
		}

		for(AutoPickStatusMsgVo msgVo:msgVoList){
			AutoPickStatusVo vo=msgVo.getAutoPickStatusVo();
			try {
				if(OPERATE_TYPE_IN.equals(vo.getOperate_type())){
					autoInWarehouseService.autoInWarehouse(vo,user);
				}else if(OPERATE_TYPE_OUT.equals(vo.getOperate_type())){
					autoOutWarehouseService.autOutWarehouse(vo,user);
				}else{
					throw new RuntimeException("分拣状态报文中未明的操作类型:"+vo.getOperate_type());
				}
				autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.success.getValue(),vo.getOrder_sn(),vo.getOperate_type());
			} catch (Exception e) {
				logger.error("处理分拣状态出错，handleData error,cwb:"+vo.getOrder_sn(),e);
				
				String errinfo="DMP分拣状态数据转业务时出错."+e.getMessage();
				long detailId=0;
				try{
					
					long msgid=this.autoExceptionService.createAutoExceptionMsg(msgVo.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
					detailId=this.autoExceptionService.createAutoExceptionDetail(vo.getOrder_sn(),"",errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0);
					
					autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(),vo.getOrder_sn(),vo.getOperate_type());
				} catch (Exception ee) {
	        		logger.error("createAutoException error",ee);
	        	}
				
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
		return errorList;
	}
	
	private void handleTimeoutData(){
		List<AutoOrderStatusTmpVo> timeOutVoList= autoOrderStatusService.retrieveTimeoutOrderStatusMsg(50);//?????
		if(timeOutVoList==null||timeOutVoList.size()<1){
			return;
		}
		List<AutoMQExceptionDto> errorList=new ArrayList<AutoMQExceptionDto>();
		String errinfo="DMP处理分拣状态数据时一直没等到此订单数据.";
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
	
	private List<AutoPickStatusMsgVo> retrieveOrderStatusMsg(int size){
		 List<AutoOrderStatusTmpVo> rowList= autoOrderStatusService.retrieveOrderStatusMsg(size);
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
	    		String errinfo="DMP分拣状态数据转业务时出错."+e.getMessage();
	    		
	    		autoOrderStatusService.completedOrderStatusMsg(AutoCommonStatusEnum.fail.getValue(), row.getCwb(), row.getOperatetype());
   		
				long msgid=this.autoExceptionService.createAutoExceptionMsg(row.getMsg(), AutoInterfaceEnum.fenjianzhuangtai.getValue());
				this.autoExceptionService.createAutoExceptionDetail(row.getCwb(),"",errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0);
		
	    	}
		}
		return dataList;
	}

	private User getSessionUser() {
		User user=new User();
		user.setUserid(1);//admin
		user.setBranchid(getPickBranch());//joint 199
		user.setRealname("admin");//
		user.setIsImposedOutWarehouse(1);//
		return user;
	}
	
	private long getPickBranch() {
		JointEntity jointEntity=jointService.getObjectMethod(B2cEnum.VipShop_TPSAutomate.getKey());
		if(jointEntity==null){
			throw new RuntimeException("找不到自动化分拣库相关配置");
		}
		String objectMethod = jointEntity.getJoint_property();
		JSONObject jsonObj = JSONObject.fromObject(objectMethod);
		VipShop vipshop = (VipShop)JSONObject.toBean(jsonObj, VipShop.class);
		
		if(vipshop.getWarehouseid()==0){
			throw new RuntimeException("请先配置自动化分拣库id");
		}
		
		return vipshop.getWarehouseid();//?
	}
}
