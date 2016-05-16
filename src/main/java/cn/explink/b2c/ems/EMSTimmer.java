package cn.explink.b2c.ems;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.enumutil.EMSTraceDataEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.Tools;

@Service
public class EMSTimmer {
	@Autowired
	JointService jointService;
	@Autowired
	EMSService eMSService;
	@Autowired
	EMSDAO eMSDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	
	private Logger logger = LoggerFactory.getLogger(EMSTimmer.class);

	/**
	 * EMS定时器，查询临时表，模拟dmp相关操作
	 */
	public void imitateDmpOpt(){
		for (B2cEnum enums : B2cEnum.values()) { 
			if (enums.getMethod().equals("EMS")) {
				int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启mes[" + enums.getKey() + "]对接！");
					continue;
				}
				selectTempAndImitateDmpOpt();
			}
		}
	}
	
	public void selectTempAndImitateDmpOpt(){
		try {
			EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
			for(int i=0;i<15;i++){
				//String result = dealWithOders(vipshop, lefengCustomerid);
				
				String result = dealWithOrders(ems);				
				if(result==null){
					break;
				}
			}
		} catch (Exception e) {
			logger.error("0EMS0定时器查询临时表，模拟dmp相关操作执行异常!异常原因:", e);
		}
	}
	
	/**
	 * 执行转业务处理
	 */
	private String dealWithOrders(EMS ems) {
		
		//获取未转业务的记录
		List<EMSFlowEntity> eMSFlowEntityList = eMSDAO.getEMSFlowEntityList();
		if(eMSFlowEntityList == null || eMSFlowEntityList.isEmpty()){
			return null;
		}
		
		int countNoMps = eMSFlowEntityList.size();
		if(countNoMps > 0){
			int k = 1;
			int batch = 50;
			while (true) {
				try{
					int fromIndex = (k - 1) * batch;
					if (fromIndex >= countNoMps) {
						break;
					}
					
					int toIdx = k * batch;
					if (k * batch > countNoMps) {
						toIdx = countNoMps;
					}
										
					List<EMSFlowEntity> subList = eMSFlowEntityList.subList(fromIndex, toIdx);
					this.handleTranscwbFlowList(ems,subList);
					
					k++;
				}catch(Exception ee){
					ee.printStackTrace();
					logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", ee);
				}
			}
		}
		
		return "OK";
	}
	
	@Transactional
	public void handleTranscwbFlowList(EMS ems,List<EMSFlowEntity> eMSFlowEntityList) {

		for (EMSFlowEntity eMSFlowEntity : eMSFlowEntityList) {
			int state=EMSTraceDataEnum.weichuli.getValue();
			String remark = "";
			try {
				state=eMSService.handleTranscwbFlowResult(ems,eMSFlowEntity);
			} catch (Exception e) {
				e.printStackTrace();
				remark = e.getMessage(); 
				state=EMSTraceDataEnum.chulishibai.getValue();
				logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", e);
			}finally{
				if(state!=0){
					eMSDAO.changeEmsTraceDataState(eMSFlowEntity.getId(),state,remark);
				}
			}
		}
	}
	
	public void SendOrderOps(){
		try {
			EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
			for(int i=0;i<15;i++){
				String result = sendOrderToEMS(ems);				
				if(result==null){
					break;
				}
			}
		} catch (Exception e) {
			logger.error("0EMS0定时器查询临时表，模拟dmp相关操作执行异常!异常原因:", e);
		}
	}

	//推送订单信息给EMS
	public String sendOrderToEMS(EMS ems) {
		//获取需要推送给EMS的数据
		List<SendToEMSOrder> sendToEMSOrderList = eMSDAO.getSendToEMSOrderList();
		if(sendToEMSOrderList == null || sendToEMSOrderList.isEmpty()){
			return null;
		}
		
		int countNoMps = sendToEMSOrderList.size();
		if(countNoMps > 0){
			int k = 1;
			int batch = 50;
			while (true) {
				try{
					int fromIndex = (k - 1) * batch;
					if (fromIndex >= countNoMps) {
						break;
					}
					
					int toIdx = k * batch;
					if (k * batch > countNoMps) {
						toIdx = countNoMps;
					}
										
					List<SendToEMSOrder> subList = sendToEMSOrderList.subList(fromIndex, toIdx);
					eMSService.handleSendOrderToEMS(ems,subList);
					k++;
				}catch(Exception ee){
					logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", ee);
				}
			}
		}
		
		return "OK";
	}

	//获取EMS运单号
	/*public void getEmsMailNoTask() {
		//获取需要推送给EMS的数据
		List<Map<String, Object>> transcwbs = eMSDAO.getTranscwbs();
		int countNoMps = transcwbs.size();
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		int emsTranscwbCount = ems.getEmsTranscwbCount();
		int count = 0;
		StringBuffer nos = new StringBuffer();
		if(countNoMps > 0){
			for(int i=0;i<countNoMps;i++){
				nos.append(transcwbs.get(i).get("transcwb"));
				count ++;
				if(emsTranscwbCount!=0 && count%emsTranscwbCount==0){
					try {
						eMSService.getEMSTranscwb(nos.toString(),ems);
						nos.delete(0, nos.length());
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
			}
		}
	}*/
	
	public void getEmsMailNoTask() {
		//获取需要推送给EMS的数据
		List<Map<String, Object>> transcwbs = eMSDAO.getTranscwbs();
		int countNoMps = transcwbs.size();
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		if(countNoMps > 0){
			for(int i=0;i<countNoMps;i++){
				try {
					eMSService.getEMSTranscwb(transcwbs.get(i).get("transcwb")+"",ems);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
}
