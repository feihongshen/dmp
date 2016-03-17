
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
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.VMSEventArgs;

/**
 * 消费下发订单数据接口
 * <p>
 * 类详细描述
 * </p>
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
	TPSOrderHandle tPSOrderHandle;
	@Autowired
	CustomerDAO customerDAO;
	
    protected Logger logger = LoggerFactory.getLogger(TPSOrderAutomateMQCallback.class);
	
    @SuppressWarnings({ "rawtypes", "deprecation", "unused" })
	@Override
    public void onSuccess(Object sender, VMSEventArgs e) {
    	int vipshop_key = B2cEnum.VipShop_TPSAutomate.getKey();
    	VipShop vipshop = this.getVipShop(vipshop_key);
    	String msg = "";
    	List<TPSOrder> errorOrderList = null;
    	List<AutoMQExceptionDto> errorList = null;
    	AutoMQExceptionDto error=null;
    	long msgid=0;
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		try {
			msg = new String(e.getPayload(), "utf-8");
			this.logger.info("TPS自动化订单下发接口报文：" + msg);
			if (isOpenFlag == 1) {
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
	        	
	        	Customer customer=customerDAO.getCustomerById(Long.valueOf(vipshop.getCustomerids()));
	        	for(TPSOrder order : list){
	        		//System.out.println(order.getCustOrderNo());
	        		String[] cwbStr = null;
	        		int cwbTransLen = 0;
	        		if(!order.getBoxNo().isEmpty()){
	        			cwbStr = order.getBoxNo().split(",");
	        			cwbTransLen = cwbStr.length;
	        		}
	        		try{
	        			if(order.getCustOrderNo().isEmpty()){
	    					this.logger.info("接口数据异常，订单号为空");
	    					throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"接口数据异常，订单号为空");
	    				}
	        			if(cwbTransLen!=0 && cwbTransLen!=order.getTotalPack()){
	        				this.logger.info("订单发货数量与箱数不一致！");
	    					throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"订单发货数量与箱数不一致！");
	        			}
	        			tPSOrderHandle.handleOrderData(order,vipshop,vipshop_key,msg,customer.getMpsswitch());
	        		}catch(Exception ex){
	        			error=new AutoMQExceptionDto();
	        			if(msgid==0){
	        				msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.dingdanxiafa.getValue());
	        			}
	        	        long detailId=this.autoExceptionService.createAutoExceptionDetail(order.getCustOrderNo(),order.getBoxNo(), "下发订单数据转业务异常",AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
	        	        error.setBusiness_id(order.getCustOrderNo());
	        	        error.setException_info(ex.getMessage());
	        	        error.setMessage(msg);
	        	        error.setRefid(detailId);
	        	       
	        	        if(errorOrderList==null){
	        				errorOrderList=new ArrayList<TPSOrder>();
	        			}
	        	        if(errorList==null){
	        	        	errorList=new ArrayList<AutoMQExceptionDto>();
	        			}
	        	        errorOrderList.add(order);
	        	        errorList.add(error);
	        		}
	        	}
		    }else{
		    	this.logger.info("未开启TPS自动化订单下发接口[" + vipshop_key + "]对接！");
		    }
        } catch (Throwable ex) {
        	this.logger.error("消费TPS自动化订单下发数据时解析异常!");
        	if(msgid==0){
        		msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.dingdanxiafa.getValue());
        	}
        	long detailId=this.autoExceptionService.createAutoExceptionDetail("报文异常","", "TPS自动化订单下发数据异常",AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0,"");
        	AutoMQExceptionDto mqe=new AutoMQExceptionDto();
			mqe.setBusiness_id("");
			mqe.setException_info(ex.getMessage());
			mqe.setMessage(msg);
			mqe.setRefid(detailId);
			if(errorList==null){
				errorList=new ArrayList<AutoMQExceptionDto>();
        	}
			ex.printStackTrace();
			errorList.add(mqe);
        } finally {
        	if(errorList!=null){
        		this.logger.error("TPS自动化订单下发接口异常报文：" + msg);
        		for(AutoMQExceptionDto err:errorList){
              	    String sendXml = StringXMLSend(vipshop,err,msg);
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
		sub.append("<message><![CDATA["+falure+"]]></message>");
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
