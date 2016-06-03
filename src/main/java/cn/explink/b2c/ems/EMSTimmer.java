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
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		selectTempAndImitateDmpOpt();
	}
	
	public void selectTempAndImitateDmpOpt(){
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		for(int i=0;i<15;i++){
			String result = dealWithOrders(ems);				
			if(result==null){
				break;
			}
		}
	}
	
	/**
	 * 执行转业务处理
	 */
	public String dealWithOrders(EMS ems) {
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
				eMSDAO.changeEmsTraceDataState(eMSFlowEntity.getId(),state,remark);
			}
		}
	}
	
	//推送订单信息给ems定时器
	/*public void sendOrderOps(){
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
	}*/

	//推送订单信息给EMS
	public void sendOrderToEMS() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		//获取需要推送给EMS的数据
		List<SendToEMSOrder> sendToEMSOrderList = eMSDAO.getSendToEMSOrderList();
		if(sendToEMSOrderList == null || sendToEMSOrderList.isEmpty()){
			return;
		}
		
		int countNoMps = sendToEMSOrderList.size();
		if(countNoMps > 0){
			int k = 1;
			int batch = 50;
			//测试
			/*int batch = 1;*/
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
		return;
	}

	
	//获取EMS运单号
	public void getEmsMailNoTask() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		//获取需要推送给EMS的数据
		List<Map<String, Object>> transcwbs = eMSDAO.getTranscwbs();
		int countNoMps = transcwbs.size();
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		for(int i=0;i<countNoMps;i++){
			try {
				eMSService.getEMSTranscwb(transcwbs.get(i).get("transcwb")+"",ems);
			} catch (Exception e) {
				logger.info("获取EMS运单号异常，dmp运单号："+transcwbs.get(i).get("transcwb"));
			} 
		}
	}

	//初步处理EMS轨迹信息
	public void saveFlowInfo() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启EMS接口对接！");
			return;
		}
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		//获取需要处理的EMS的轨迹信息
		List<EMSFlowObjInitial> eMSFlowObjInitial = eMSDAO.getFlowInfoList();
		if(eMSFlowObjInitial == null || eMSFlowObjInitial.isEmpty()){
			return;
		}
		
		int countNoMps = eMSFlowObjInitial.size();
		if(countNoMps > 0){
			int k = 1;
			int batch = 50;
			//测试
			/*int batch = 1;*/
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
										
					List<EMSFlowObjInitial> subList = eMSFlowObjInitial.subList(fromIndex, toIdx);
					eMSService.initialHandleEMSFlow(ems,subList);
					k++;
				}catch(Exception ee){
					logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", ee);
				}
			}
		}
		return;
	}
}
