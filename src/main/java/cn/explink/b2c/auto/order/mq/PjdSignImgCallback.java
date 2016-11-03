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
import cn.explink.b2c.auto.order.service.PjdSignImgService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tps.PjdSignimgCfg;
import cn.explink.b2c.tps.PjdSignimgCfgService;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONObject;


public class PjdSignImgCallback implements IVMSCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JointService jointService;
	@Autowired
	private PjdSignimgCfgService pjdSignimgCfgService;
	@Autowired
	private PjdSignImgService pjdSignImgService;
	@Autowired
	private AutoExceptionSender autoExceptionSender;
	@Autowired
	private AutoExceptionService autoExceptionService;
	@Autowired
	@Qualifier("pjdSignImgConsumer")
	private ConsumerTemplate consumerTemplate;

	private final static String MSG_ENCODE="utf-8";
	@Override
	public void onSuccess(Object sender, VMSEventArgs e) {
	       this.logger.debug("开始消费PJD签收图片信息.");

	        String msg = "";
	        List<AutoMQExceptionDto> errorList=null;

	        int isOpenFlag =0; 
	        String custOrderNo=null;
	        String transportNo=null;
	        try {
	        	int openState = this.jointService.getStateForJoint(B2cEnum.PJD_SignImg.getKey());
	        	if(openState==1){
	        		PjdSignimgCfg  pjdSignimgCfg = this.pjdSignimgCfgService.getPjdSignimgCfg(B2cEnum.PJD_SignImg.getKey());
		            if(pjdSignimgCfg!=null){
		            	isOpenFlag=pjdSignimgCfg.getOpenFlag();
		            }
	        	}
	        	
	        	if(isOpenFlag==1){
		        	msg = new String(e.getPayload(), MSG_ENCODE);
		            this.logger.info("PJD签收图片报文消息:" + msg);
		            
		            //解析json
		            JSONObject jsonObj = JSONObject.fromObject(msg);  
		    		custOrderNo=jsonObj.getString("custOrderNo");
		    		//String orgCode=jsonObj.getString("orgCode");
		    		transportNo=jsonObj.getString("transportNo");
		    		String imageUrl=jsonObj.getString("imageUrl");
		    		int num=this.pjdSignImgService.saveSignImg(custOrderNo, imageUrl);
		    		if(num<1){
		    			throw new RuntimeException("保存PJD签收图片url时没找到反馈记录");
		    		}
	        	}
	        } catch (Throwable ex) {
	        	logger.error("消费PJD签收图片信息 onSuccess error,msg:"+msg,ex);
	        	
	        	String errinfo="DMP消费PJD签收图片MQ消息时出错."+ex.getMessage();
	        	long detailId=0;
	        	
	        	try{
		        	long msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.pjdsignimg.getValue());
		        	detailId=this.autoExceptionService.createAutoExceptionDetail(custOrderNo==null?"":custOrderNo,transportNo==null?"":transportNo,errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
	        	} catch (Exception ee) {
	        		logger.error("PJD签收图片createAutoException error",ee);
	        	}
	        	
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id(custOrderNo==null?"":custOrderNo);
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
		        	logger.error("DMP反馈异常到PJD时出错，sendErrorResponse error",et);
		        }
	            // 确认消费
	            ISubscriber subscriber = (ISubscriber) sender;
	            subscriber.commit();
	            logger.info("PJD sign img subscriber commit ok.");//
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
		        	logger.error("反馈异常到PJD时出错,errorMsg:"+errorMsg,et);

		        	long refid=err.getRefid();//
		        	try{
			        	long msgid=this.autoExceptionService.createAutoExceptionMsg(errorMsg, AutoInterfaceEnum.pjdsignimg_exception.getValue());
			        	this.autoExceptionService.createAutoExceptionDetail(err.getBusiness_id(),"","DMP反馈异常到PJD时出错."+et.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, refid,"");
					} catch (Exception ee) {
		        		logger.error("PJD createAutoException error",ee);
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
		mqe.setSystem_name("DMP");//
		mqe.setMessage("<![CDATA["+mqe.getMessage()+"]]>");//250 length?
		String routingKeyStr[] = consumerTemplate.getQueueName().split("_");
		String routingKey = "*";
		if(routingKeyStr.length>1){
			routingKey = routingKeyStr[routingKeyStr.length-1];
		}
		mqe.setRouting_key(routingKey);//
		
		String msg=XmlUtil.toXml(AutoMQExceptionDto.class, mqe); 
		msg=StringEscapeUtils.unescapeXml(msg);
		
		logger.debug("反馈PJD的报文 xml:"+msg);
		
		return msg;
	}
	
	@Override
	public void onFailure(Object sender, VMSEventArgs e, Throwable cause) {
		String forRecover = "";
        this.logger.error("消费PJD签收图片信息出错：" , cause);
        try {
            forRecover = new String(e.getPayload(), MSG_ENCODE);
            this.logger.error("the Payload msg is:"+forRecover);
        } catch (Throwable e1) {
            logger.error("消费PJD签收图片信息，onFailure:" , e1);
        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
		
	}


}
