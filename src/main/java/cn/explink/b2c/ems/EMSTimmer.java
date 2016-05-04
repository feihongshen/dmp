package cn.explink.b2c.ems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EMSTraceDataEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;

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
	 * EMS会定时器，查询临时表，模拟dmp相关操作
	 */
	public void imitateDmpOpt(){
		for (B2cEnum enums : B2cEnum.values()) { 
			if (enums.getMethod().equals("EMS")) {
				int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启mes[" + enums.getKey() + "]对接！");
					continue;
				}
				selectTempAndImitateDmpOpt(enums.getKey());
			}
		}
	}
	
	public void selectTempAndImitateDmpOpt(int b2ckey){
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
		
		//非集包模式的订单转业务
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
					logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", ee);
				}
			}
		}
		
		return "OK";
	}
	
	@Transactional
	public void handleTranscwbFlowList(EMS ems,List<EMSFlowEntity> eMSFlowEntityList) {

		for (EMSFlowEntity eMSFlowEntity : eMSFlowEntityList) {
			String remark = "";
			try {
				eMSService.handleTranscwbFlowResult(ems,eMSFlowEntity);
			} catch (Exception e) {
				remark = e.getMessage(); 
				eMSDAO.changeEmsTraceDataState(eMSFlowEntity.getId(),EMSTraceDataEnum.chulishibai.getValue(),remark);
				logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", e);
			}
		}
	}
}
