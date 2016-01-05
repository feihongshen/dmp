package cn.explink.b2c.auto.order.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.mq.AutoMQExceptionDto;
import cn.explink.b2c.auto.order.vo.TPSOrder;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.AutoExceptionStatusEnum;
import cn.explink.enumutil.AutoInterfaceEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

public class TPSOrderAutomateHandler {
	@Autowired
	TPSGetOrderDataService tPSGetOrderDataService;
	@Autowired
	AutoExceptionService autoExceptionService;
	
	 protected Logger logger = LoggerFactory.getLogger(TPSOrderAutomateHandler.class);
	
	 //处理业务逻辑
    @Transactional
	public AutoMQExceptionDto handleOrderData(List<TPSOrder> errorOrderList,
    		TPSOrder order,VipShop vipshop,int vipshop_key,String msg){
    	AutoMQExceptionDto error=null;
    	long msgid=0;
    	//flagOrder = order.getCustOrderNo();
    	try{
    		if(order.getBusinessType()==20 || order.getBusinessType()==40){
    			tPSGetOrderDataService.extractedOXODataImport(order,vipshop);
    		}else{
    			//普通接口数据导入
    			if(null!=order){
    				if(order.getBusinessType()!=60 && order.getAddTime()==null){
    					this.logger.info("没有出仓时间");
    					throw new CwbException(order.getCustOrderNo(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),"没有出仓时间");
    				}
    				//返回的报文订单信息解析
    				CwbOrderDTO cwbOrder = tPSGetOrderDataService.parseXmlDetailInfo(vipshop,order);
    				if (cwbOrder != null) {
    					//是否开启托运单模式，生成多个批次 0 不开启
        				if (vipshop.getIsTuoYunDanFlag() == 0) {
        					//普通单在没有开启托运单模式下，数据插入临时表
        					tPSGetOrderDataService.extractedDataImport(vipshop_key, vipshop, cwbOrder);
        				} else {
        					//普通单在开启托运单模式下，数据插入临时表
        					tPSGetOrderDataService.extractedDataImportByEmaildate(vipshop_key, vipshop, cwbOrder);
        				}
    				}
    				
    			}
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
			logger.error("handleData error:",ex);
			if(errorOrderList==null){
				errorOrderList=new ArrayList<TPSOrder>();
			}
			if(error==null){
				error=new AutoMQExceptionDto();
			}
			if(msgid==0){
				msgid=this.autoExceptionService.createAutoExceptionMsg(msg,AutoInterfaceEnum.dingdanxiafa.getValue());
			}
	        long detailId=this.autoExceptionService.createAutoExceptionDetail(order.getCustOrderNo(),order.getBoxNo(), "下发订单数据转业务异常",AutoExceptionStatusEnum.xinjian.getValue(),msgid, 0);
	        error.setBusiness_id(order.getCustOrderNo());
	        error.setException_info(ex.getMessage());
	        error.setMessage(msg);
	        error.setRefid(detailId);
	        errorOrderList.add(order);
    	}
    	return error;
    }
}
