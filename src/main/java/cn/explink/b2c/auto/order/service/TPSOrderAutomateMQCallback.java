
package cn.explink.b2c.auto.order.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.mq.AutoExceptionSender;
import cn.explink.b2c.auto.order.mq.AutoMQExceptionDto;
import cn.explink.b2c.auto.order.mq.ConsumerTemplate;
import cn.explink.b2c.auto.order.vo.TPSOrder;
import cn.explink.b2c.auto.order.vo.TPSOrderDetails;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.domain.User;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

import com.vip.platform.middleware.vms.IPublisher;
import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.Message;
import com.vip.platform.middleware.vms.VMSClient;
import com.vip.platform.middleware.vms.VMSEventArgs;
/*import com.vip.tps.common.constant.ExceptionCodeConstant.MqExceptionCode;
import com.vip.tps.common.constant.MqConstant;
import com.vip.tps.common.excelimport.exception.ServiceException;
import com.vip.tps.inf.common.XmlBeanTranslator;
import com.vip.tps.inf.mqexception.vo.InfCarrierOrderStatusCallbackVO;
import com.vip.tps.inf.mqexception.vo.InfCarrierOrderStatusTransportTrackVo;
import com.vip.tps.inf.tms.InfTmsToTpsTransportTrackService;
import com.vip.tps.inf.tms.vo.InfTmsToTpsTransportTrackVo;
import com.vip.tps.mq.entity.MqException;
import com.vip.tps.mq.service.MqExceptionService;
import com.vip.tps.order.entity.OmOrderTransport;
import com.vip.tps.order.service.OrderTransportService;*/

/**
 * 消费下发承运商订单状态接口表的物流状态信息
 * <p>
 * 类详细描述
 * </p>
 * @author vince.zhou
 * @since 1.0
 */
public class TPSOrderAutomateMQCallback implements IVMSCallback {
	
	@Autowired
	JointService jointService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	TPSGetOrderDataService tPSGetOrderDataService;
	@Autowired
	AutoExceptionService autoExceptionService;
	@Autowired
	@Qualifier("tpsOrderAutomateConsumer")
	private ConsumerTemplate consumerTemplate;
	@Autowired
	AutoExceptionSender autoExceptionSender;
	@Autowired
	TPSOrderAutomateHandler tPSOrderAutomateHandler;
	
    protected Logger logger = LoggerFactory.getLogger(TPSOrderAutomateMQCallback.class);
	
    @SuppressWarnings({ "rawtypes", "deprecation", "unused" })
	@Override
    public void onSuccess(Object sender, VMSEventArgs e) {
    	int vipshop_key = B2cEnum.VipShop_TPSAutomate.getKey();
    	VipShop vipshop = this.getVipShop(vipshop_key);
    	String flagOrder = "";
    	String msg = "";
    	List<TPSOrder> errorOrderList = null;
    	List<AutoMQExceptionDto> errorList = null;
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		try {
			if (isOpenFlag == 1) {
				this.logger.info("未开启TPS自动化[" + vipshop_key + "]对接！");
				this.logger.info("消费下发承运商订单状态接口表的物流状态信息:");
	        	msg = new String(e.getPayload(), "utf-8");
	        	JsonConfig config=new JsonConfig(); 
	        	Map<String, Class<?>> clazz = new HashMap<String,Class<?>>();
				clazz.put("details", TPSOrderDetails.class);
	        	 //忽略属性  
	            config.setExcludes(new String[]{"createdDtmLoc","iDValue","isDeleted","logged",
	            		"principalGroupCode","rowStatus","updatedByUser",
	            		"updatedDtmLoc","updatedOffice","updatedTimeZone","dirty"});  
	        	JSONArray jsonArray = JSONArray.fromObject(msg,config);
	        	//List<TPSOrder> list = (List<TPSOrder>)JSONArray.toCollection(jsonArray,TPSOrder.class);  
	        	List<TPSOrder> list=new ArrayList<TPSOrder>();
	        	for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
					JSONObject jsonObject = (JSONObject) iterator.next();
					
					TPSOrder tpsOrder = (TPSOrder) JSONObject.toBean(jsonObject,TPSOrder.class,clazz);
					list.add(tpsOrder);
				}
	        	if ((list == null || list.size()==0)) {
	    			this.logger.info("请求TPS自动化订单信息-获取订单信息失败!");
	    			return;
	    		}
	        	
	        	for(TPSOrder order : list){
	        		AutoMQExceptionDto error = tPSOrderAutomateHandler.handleOrderData(errorOrderList,order,vipshop,vipshop_key,msg);
	        		if(error!=null){
	        			if(errorList==null){
		        			errorList = new ArrayList<AutoMQExceptionDto>();
		        		}
	        			errorList.add(error);
	        		}
	        	}
		    }
        } catch (Throwable ex) {
        	this.logger.error("消费下发订单时解析异常!");
        	long msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.dingdanxiafa.getValue());
	        long detailId=this.autoExceptionService.createAutoExceptionDetail("报文异常","", "下发订单数据异常",AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0);
        	AutoMQExceptionDto mqe=new AutoMQExceptionDto();
			mqe.setBusiness_id("");
			mqe.setException_info(ex.getMessage());
			mqe.setMessage(msg);
			mqe.setRefid(detailId);
			if(errorList==null){
				errorList=new ArrayList<AutoMQExceptionDto>();
        	}
			errorList.add(mqe);
        } finally {
        	if(errorList!=null){
        		for(AutoMQExceptionDto err:errorList){
              	    String sendXml = StringXMLSend(vipshop,err,msg);
              	  /*VMSClient client = VMSClient.getDefault();
              	    Message falure = Message.from(sendXml);
                    falure.addRoutingKey("*");
                    falure.qos().durable(true); // 非持久化的消息在宕机后消息会丢失。对于订单/运单类消息，必须设置为持久化。
                   
                    // msg.qos().priority(0); // 数字大的表示优先级高。 在同一个topic中，优先级高的消息先于优先级低的消息被消费。可选设置。
                    // 推送到MQ
                    try{
        	              client.options().setConfirmable(true).setWaitingTimeout(2000).setFailFastEnabled(false);
        	              IPublisher publisher = client.publish("channel.rabbitmq.tps.exception", falure);// 推送，第一个参数channelName，第二个参数报文内容
        	              this.logger.info("错误订单消息推送成功");
                    }catch(Exception e1){
                  	  e1.printStackTrace();
                  	  this.logger.error("下发订单信息异常信息保存失败!");
                    }*/ 
                   autoExceptionSender.send(sendXml); 
        		}
        	}
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
    }
    
   
    
    //异常报文返回数据拼接
    private String StringXMLSend(VipShop vipshop,AutoMQExceptionDto error,String falure) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	String date = String.valueOf(df.format(new Date()));
		StringBuffer sub = new StringBuffer();
		sub.append("<root>");
		sub.append("<system_name>ZDH_Order</system_name>");
		sub.append("<queue_name>"+consumerTemplate.getQueueName()+"</queue_name>");
		sub.append("<exchange_name>"+consumerTemplate.getExchangeName()+"</exchange_name>");
		sub.append("<routing_key>*</routing_key>");
		sub.append("<exception_info>"+error.getException_info()+"</exception_info>");
		sub.append("<business_id></business_id>");
		sub.append("<create_time>"+date+"</create_time>");
		sub.append("<remark></remark>");
		sub.append("<message>![CDATA["+falure+"]]</message>");
		sub.append("</root>");
		return sub.toString();
	}
    
    @Override
    public void onFailure(Object sender, VMSEventArgs e, Throwable cause) {
        String forRecover = "";
        this.logger.info("消费下发承运商订单状态接口表的物流状态信息，异常信息为：" + cause.toString());
        try {
            forRecover = new String(e.getPayload(), "utf-8");
        } catch (Throwable e1) {
            this.logger.error("消费下发承运商订单状态接口表的物流状态信息，onFailure：" + e1.toString());
            autoExceptionSender.send(forRecover); 
        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
    }
    
    //获取唯品会接口配置对象
    public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}
    
    public static void main(String[] args) {
    }
    
    private User getSessionUser() {
		User user=new User();
		user.setUserid(1);
		VipShop vipShop = this.getVipShop(B2cEnum.VipShop_TPSAutomate.getKey());
		user.setBranchid(vipShop.getWarehouseid());
		user.setRealname("admin");
		user.setIsImposedOutWarehouse(1);
		return user;
	}
    
}
