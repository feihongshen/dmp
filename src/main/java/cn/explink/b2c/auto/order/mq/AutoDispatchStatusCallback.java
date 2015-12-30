package cn.explink.b2c.auto.order.mq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.IVMSCallback;
import com.vip.platform.middleware.vms.VMSEventArgs;

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
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.XmlUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class AutoDispatchStatusCallback implements IVMSCallback{
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
	
	public static final String OPERATE_TYPE_IN="1";//1是入库，3是出库
	public static final String OPERATE_TYPE_OUT="3";//1是入库，3是出库
	
	@Override
	public void onSuccess(Object sender, VMSEventArgs e) {
	       this.logger.debug("开始消费分拣状态信息.");

	        String msg = "";
	        List<AutoMQExceptionDto> errorList=null;
	        User user=null;
	        try {
	            msg = new String(e.getPayload(), "utf-8");
	            this.logger.debug("消费消费分拣状态信息接收到报文：" + msg);
	            System.out.println(msg);//?????
	            
	    		user=this.getSessionUser();
	            
	            //解析json
	            List<AutoPickStatusVo> voList= parseJson(msg);
	            //处理数据
	            List<AutoMQExceptionDto> dataErrorList=handleData(voList,user,msg);
	            //反馈错误
	            if(dataErrorList!=null){
	            	if(errorList==null){
	            		errorList=new ArrayList<AutoMQExceptionDto>();
	            	}
	            	errorList.addAll(dataErrorList);
	            	
	            }
	        } catch (Throwable ex) {
	        	logger.error("消费分拣状态信息 onSuccess error",ex);
	        	ex.printStackTrace();/////////////////
	        	
	        	long msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.fenjianzhuangtai.getValue());
	        	long detailId=this.autoExceptionService.createAutoExceptionDetail("","", ex.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0);
	        
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
	        	try{
	        		sendErrorResponse(msg,errorList,user);
		        } catch (Throwable et) {
		        	et.printStackTrace();//??????????
		        	logger.error("反馈异常到TPS时有出错，sendErrorResponse error",et);
		        }
	            // 确认消费
	            ISubscriber subscriber = (ISubscriber) sender;
	            subscriber.commit();
	            logger.debug("subscriber commit ok.");//?????????
	            System.out.println("consumed ok.");///////////////////????????
	        }
		
	}
	
	private void sendErrorResponse(String msg,List<AutoMQExceptionDto> errorList,User user){
		if(errorList!=null){
			for(AutoMQExceptionDto err:errorList){
				String errorMsg="";
				try{
					errorMsg=encodeErrorMsg(err);
					autoExceptionSender.send(errorMsg);
				} catch (Throwable et) {
		        	et.printStackTrace();///??????
		        	logger.error("反馈异常到TPS时有出错，send exception to TPS error",et);

		        	long refid=err.getRefid();//??????????????????????
		        	long msgid=this.autoExceptionService.createAutoExceptionMsg(errorMsg, AutoInterfaceEnum.fankui_fanjian.getValue());
		        	this.autoExceptionService.createAutoExceptionDetail(err.getBusiness_id(),"",et.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid, refid);
		        }
			}
		}
		
	}
	
	private String encodeErrorMsg(AutoMQExceptionDto mqe) throws Exception{
		mqe.setCreate_time("2015-12-28");//
		mqe.setExchange_name(consumerTemplate.getExchangeName());//
		mqe.setQueue_name(consumerTemplate.getQueueName());//
		mqe.setRemark("");
		mqe.setRouting_key("*");//
		mqe.setSystem_name("DMP");//
		mqe.setMessage("![CDATA["+mqe.getMessage()+"]]");//
		
		String msg=XmlUtil.toXml(AutoMQExceptionDto.class, mqe); 
		
		logger.debug("反馈TPS的报文 xml:"+msg);
		System.out.println("反馈TPS的报文 xml:"+msg);//?????????????
		return msg;
	}
	private List<AutoPickStatusVo> parseJson(String json){ 
		 JSONArray jsonarray = JSONArray.fromObject(json);  
	     List<AutoPickStatusVo> dataList = (List<AutoPickStatusVo>)JSONArray.toCollection(jsonarray,AutoPickStatusVo.class);  
		return dataList;
	}
	
	private List<AutoMQExceptionDto> handleData(List<AutoPickStatusVo> voList,User user,String msg){
		List<AutoMQExceptionDto> errorList=null;
		long msgid=0;

		for(AutoPickStatusVo vo:voList){
			try {
				if(OPERATE_TYPE_IN.equals(vo.getOperate_type())){
					autoInWarehouseService.autoInWarehouse(vo,user);
					this.autoExceptionService.fixException(vo.getOrder_sn(), vo.getTransport_no());
				}else if(OPERATE_TYPE_OUT.equals(vo.getOperate_type())){
					autoOutWarehouseService.autOutWarehouse(vo,user);
					this.autoExceptionService.fixException(vo.getOrder_sn(), vo.getTransport_no());
				}else{
					throw new RuntimeException("分拣状态报文中未明的操作类型:"+vo.getOperate_type());
				}
			} catch (Exception e) {
				e.printStackTrace();//??????
				logger.error("处理分拣状态出错，handleData error:",e);
				//String err=vo.getOrder_sn()+","+e.getMessage();
	
				long flowordertye=FlowOrderTypeEnum.RuKu.getValue();
				if(e instanceof CwbException){
					flowordertye=((CwbException)e).getFlowordertye();
				}else{
					if(OPERATE_TYPE_IN.equals(vo.getOperate_type())){
						flowordertye=FlowOrderTypeEnum.RuKu.getValue();
					}else if(OPERATE_TYPE_OUT.equals(vo.getOperate_type())){
						flowordertye=FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
					}
				}
				
				if(msgid==0){
					msgid=this.autoExceptionService.createAutoExceptionMsg(msg, AutoInterfaceEnum.fenjianzhuangtai.getValue());
				}
				long detailId=this.autoExceptionService.createAutoExceptionDetail(vo.getOrder_sn(),"",e.getMessage(),AutoExceptionStatusEnum.xinjian.getValue(),msgid,0);

				if(errorList==null){
					errorList=new ArrayList<AutoMQExceptionDto>();
				}
				AutoMQExceptionDto mqe=new AutoMQExceptionDto();
				mqe.setBusiness_id(vo.getOrder_sn());
				mqe.setException_info(e.getMessage());
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
            forRecover = new String(e.getPayload(), "utf-8");
            this.logger.error("the Payload msg is:"+forRecover);
        } catch (Throwable e1) {
            this.logger.error("消费分拨状态信息，onFailure：" , e1);
            e1.printStackTrace();;//??????????

        } finally {
            // 确认消费
            ISubscriber subscriber = (ISubscriber) sender;
            subscriber.commit();
        }
		
	}

	private User getSessionUser() {
		User user=new User();
		user.setUserid(1);//admin ???
		//user.setBranchid(199);
		user.setBranchid(getPickBranch());//joint ???199
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
