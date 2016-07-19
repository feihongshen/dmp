package cn.explink.b2c.auto.order.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.VMSEventArgs;

import cn.explink.b2c.auto.order.mq.AutoExceptionSender;
import cn.explink.b2c.auto.order.mq.AutoMQExceptionDto;
import cn.explink.b2c.auto.order.mq.ConsumerTemplate;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONObject;

/**
 * TPS交接单号、托运单号下发给DMP
 */
@Component("tpsBatchNoCallback")
public class TpsBatchNoCallback implements IVMSCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AutoExceptionSender autoExceptionSender;
	@Autowired
	private AutoExceptionService autoExceptionService;
	@Autowired
	@Qualifier("tpsBatchNoConsumer")
	private ConsumerTemplate consumerTemplate;
	@Autowired
	private TpsBatchNoService tsBatchNoService;
	@Autowired
	private AutoUserService autoUserService;

	private final static String MSG_ENCODE="utf-8";
	
	@Override
	public void onSuccess(Object sender, VMSEventArgs e) {
	        String msg = "";
	        List<AutoMQExceptionDto> errorList=null;

	        int isOpenFlag =0; 
        	String cwb="";
        	String transcwb="";
	        try {
	        	isOpenFlag=this.autoUserService.getBatchnoOpenFlag();

	        	if(isOpenFlag==1){
		        	msg = new String(e.getPayload(), MSG_ENCODE);
		            this.logger.info("交接单号信息报文:" + msg);
		           
		            TpsBatchNoVo vo= parseJson(msg);
		            if(vo!=null){
		            	if(vo.getHeader()!=null){
		            		cwb=vo.getHeader().getCustOrderNo();
		            	}
		            	if(vo.getBoxInfo()!=null){
		            		transcwb=vo.getBoxInfo().getBoxNo();
		            	}
		            	
		            	tsBatchNoService.save(cwb, vo.getBoxInfo());
				        this.logger.info("交接单号保存成功.cwb=" + cwb+",transcwb="+transcwb); 
		            }else{
		            	throw new RuntimeException("解析交接单号报文数据出错");
		            }
		           
	        	}
	        } catch (Exception ex) {
	        	logger.error("消费交接单号信息出错,msg:"+msg,ex);
	        	String errinfo="DMP消费交接单号MQ消息时出错."+ex.getMessage();
	        	long detailId=0;
	        	
	        	try{
		        	long msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.tpsbatchno.getValue());
		        	detailId=this.autoExceptionService.createAutoExceptionDetail(cwb,transcwb,errinfo,AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
	        	} catch (Exception ee) {
	        		logger.error("交接单号createAutoException error",ee);
	        	}
	        	
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id(cwb);
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
		        	logger.error("DMP反馈交接单号异常到TPS时出错.",et);
		        }
	            // 确认消费
	            ISubscriber subscriber = (ISubscriber) sender;
	            subscriber.commit();
	            logger.info("tpsbatchno subscriber commit ok.");//
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
		        	logger.error("反馈异常到TPS时出错,errorMsg:"+errorMsg,et);

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
		mqe.setRouting_key(ResourceBundleUtil.expressCarrierCode);//
		mqe.setSystem_name("DMP");//
		mqe.setMessage("<![CDATA["+mqe.getMessage()+"]]>");//250 length?
		
		String msg=XmlUtil.toXml(AutoMQExceptionDto.class, mqe); 
		msg=StringEscapeUtils.unescapeXml(msg);
		
		logger.info("异常反馈TPS的报文 xml:"+msg);
		
		return msg;
	}
	public TpsBatchNoVo parseJson(String json){ 
		JSONObject jsonObj = JSONObject.fromObject(json);
		TpsBatchNoHeader header = (TpsBatchNoHeader)JSONObject.toBean(jsonObj, TpsBatchNoHeader.class);
		TpsBatchNoDoBoxInfo boxInfo=null;
		TpsBatchNoVo vo=null;
		if(header!=null){
			//因为此处dataBody可能数据结构不一样
			if(header.getDataType()==101||header.getDataType()==102){//101:更新箱托运单 102:更新箱批次号
				String dataBody=header.getDataBody();
				if(dataBody!=null){
					dataBody=dataBody.trim();
				}
				if(dataBody!=null&&dataBody.length()>0){
					jsonObj = JSONObject.fromObject(dataBody);
					boxInfo = (TpsBatchNoDoBoxInfo)JSONObject.toBean(jsonObj, TpsBatchNoDoBoxInfo.class);
					if(boxInfo!=null){
						vo=new TpsBatchNoVo();
						vo.setBoxInfo(boxInfo);
						vo.setHeader(header);
					}
				}
			}
		}
		
		return vo;
	}
	
	@Override
	public void onFailure(Object sender, VMSEventArgs e, Throwable cause) {
		String forRecover = "";
        this.logger.error("消费交接单号信息出错:" , cause);
        try {
            forRecover = new String(e.getPayload(), MSG_ENCODE);
            this.logger.error("交接单号 Payload msg is:"+forRecover);
        } catch (Throwable e1) {
            logger.error("消费交接单号信息，onFailure:" , e1);
        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
	}

}
