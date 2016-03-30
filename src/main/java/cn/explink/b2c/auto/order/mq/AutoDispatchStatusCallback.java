package cn.explink.b2c.auto.order.mq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.VMSEventArgs;

import cn.explink.b2c.auto.order.service.AutoExceptionService;
import cn.explink.b2c.auto.order.service.AutoOrderStatusService;
import cn.explink.b2c.auto.order.service.AutoPickStatusVo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONArray;


public class AutoDispatchStatusCallback implements IVMSCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

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

	private final static String MSG_ENCODE="utf-8";
	@Override
	public void onSuccess(Object sender, VMSEventArgs e) {
	       this.logger.debug("开始消费分拣状态信息.");

	        String msg = "";
	        List<AutoMQExceptionDto> errorList=null;

	        int isOpenFlag =0; 
	        try {
	        	isOpenFlag=this.jointService.getStateForJoint(B2cEnum.VipShop_TPSAutomate.getKey());
	           
	        	if(isOpenFlag==1){
		        	msg = new String(e.getPayload(), MSG_ENCODE);
		            this.logger.info("分拣状态信息报文 auto dispatch msg:" + msg);
		            
		            //解析json
		            List<AutoPickStatusVo> voList= parseJson(msg);
		            //保存数据到临时表
		            List<AutoMQExceptionDto> dataErrorList=persistData(voList,msg);
		            //反馈错误
		            if(dataErrorList!=null){
		            	if(errorList==null){
		            		errorList=new ArrayList<AutoMQExceptionDto>();
		            	}
		            	errorList.addAll(dataErrorList);
		            	
		            }
	        	}
	        } catch (Throwable ex) {
	        	logger.error("消费分拣状态信息 onSuccess error,msg:"+msg,ex);
	        	
	        	String errinfo="DMP消费分拣状态MQ消息时出错."+ex.getMessage();
	        	long detailId=0;
	        	
	        	try{
		        	long msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.fenjianzhuangtai.getValue());
		        	detailId=this.autoExceptionService.createAutoExceptionDetail("","",errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
	        	} catch (Exception ee) {
	        		logger.error("createAutoException error",ee);
	        	}
	        	
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id("");
				mqe.setException_info(errinfo);
				mqe.setMessage(msg);
				mqe.setRefid(detailId);
				if(errorList==null){
            		errorList=new ArrayList<AutoMQExceptionDto>();
            	}
	        	errorList.add(mqe);
	        } finally {
	        	try{
	        		sendErrorResponse(msg,errorList);
		        } catch (Throwable et) {
		        	logger.error("DMP反馈异常到TPS时出错，sendErrorResponse error",et);
		        }
	            // 确认消费
	            ISubscriber subscriber = (ISubscriber) sender;
	            subscriber.commit();
	            logger.info("dispatch subscriber commit ok.");//
	        }
		
	}
	
	private void sendErrorResponse(String msg,List<AutoMQExceptionDto> errorList){
		if(errorList!=null){
			for(AutoMQExceptionDto err:errorList){
				String errorMsg="";
				try{
					errorMsg=encodeErrorMsg(err);
					autoExceptionSender.send(errorMsg);
				} catch (Throwable et) {
		        	logger.error("反馈异常到TPS时出错，send exception to TPS error,errorMsg:"+errorMsg,et);

		        	long refid=err.getRefid();//
		        	try{
			        	long msgid=this.autoExceptionService.createAutoExceptionMsg(errorMsg, AutoInterfaceEnum.fankui_fanjian.getValue());
			        	this.autoExceptionService.createAutoExceptionDetail(err.getBusiness_id(),"","DMP反馈异常到TPS时出错."+et.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, refid,"");
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
	private List<AutoPickStatusVo> parseJson(String json){ 
		 JSONArray jsonarray = JSONArray.fromObject(json);  
	     List<AutoPickStatusVo> dataList = (List<AutoPickStatusVo>)JSONArray.toCollection(jsonarray,AutoPickStatusVo.class);  
		return dataList;
	}
	
	private List<AutoMQExceptionDto> persistData(List<AutoPickStatusVo> voList,String msg){
		List<AutoMQExceptionDto> errorList=null;
		if(voList==null||voList.size()<1){
			return errorList;
		}
		
		long msgid=0;
		String json=null;
		if(voList.size()==1){
			json=msg;
		}

		for(AutoPickStatusVo vo:voList){
			try {
				autoOrderStatusService.saveOrderStatusMsg(vo, json);
			} catch (Exception e) {
				logger.error("处理分拣状态出错，handleData error,cwb:"+vo.getOrder_sn(),e);
	
				String errinfo="DMP保存分拣状态数据到临时表时出错."+e.getMessage();
				long detailId=0;
				try{
					if(msgid==0){
						msgid=this.autoExceptionService.createAutoExceptionMsg(msg, AutoInterfaceEnum.fenjianzhuangtai.getValue());
					}
					detailId=this.autoExceptionService.createAutoExceptionDetail(vo.getOrder_sn(),"",errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid,0,"");
				} catch (Exception ee) {
	        		logger.error("createAutoException error",ee);
	        	}
				
				if(errorList==null){
					errorList=new ArrayList<AutoMQExceptionDto>();
				}
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id(vo.getOrder_sn());
				mqe.setException_info(errinfo);
				mqe.setMessage(msg);
				mqe.setRefid(detailId);
				errorList.add(mqe);
			}
		}
		return errorList;
	}
	
	@Override
	public void onFailure(Object sender, VMSEventArgs e, Throwable cause) {
		String forRecover = "";
        this.logger.error("消费分拨状态信息出错：" , cause);
        try {
            forRecover = new String(e.getPayload(), MSG_ENCODE);
            this.logger.error("the Payload msg is:"+forRecover);
        } catch (Throwable e1) {
            logger.error("消费分拨状态信息，onFailure:" , e1);
        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
		
	}


}
