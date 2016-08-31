import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.explink.b2c.benlaishenghuo.BenLaiShengHuoService;
import cn.explink.b2c.ems.EMS;
import cn.explink.b2c.ems.EMSDAO;
import cn.explink.b2c.ems.EMSService;
import cn.explink.b2c.ems.EMSTimmer;
import cn.explink.b2c.ems.EMSTranscwb;
import cn.explink.b2c.ems.EMSUnmarchal;
import cn.explink.b2c.ems.EmsAndDmpTranscwb;
import cn.explink.b2c.ems.FlowFromJMSToEMSOrderService;
import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import daoTest.BaseTest;

public class EMSTest extends BaseTest{
	@Autowired
	FlowFromJMSToEMSOrderService flowFromJMSToEMSOrderService;
	@Autowired
	EMSDAO eMSDAO;
	@Autowired
	EMSService eMSService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	BenLaiShengHuoService benLaiShengHuoService;
	@Autowired
	EMSTimmer eMSTimmer;
	
	//抓取ems运单号单元测试
	@Test
	public void testOrderList(){
		String transcwb = "huan0520003-3";
		String response_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response> "
				+ "<result>1</result><errorDesc></errorDesc><errorCode></errorCode>"
				+ "<qryData><bigAccountDataId>huan0520003-3</bigAccountDataId><billno>ems0520003-3</billno>"
				+ "<backBillno></backBillno></qryData></response>";
		try {
			
			if (response_XML.contains("<result>0</result>")) {
				String errorDesc = response_XML.substring(response_XML.indexOf("<errorDesc>") + 11, response_XML.indexOf("</errorDesc>"));
				String errorCode = response_XML.substring(response_XML.indexOf("<errorCode>") + 11, response_XML.indexOf("</errorCode>"));
				return;
			}
	
			// 3.成功了,解析xml
			EMSTranscwb eMSTranscwb = EMSUnmarchal.Unmarchal(response_XML);
			EmsAndDmpTranscwb emsAndDmpTranscwb = eMSTranscwb.getQryData();
			
			if (emsAndDmpTranscwb == null) {
				logger.info("请求[EMS]没有获取到EMS对应运单号!");
				return;
			} 
			
			transcwb = transcwb.trim();
	    	String cwb = cwbOrderService.translateCwb(transcwb);
	    	
	    	String bingTime = DateTimeUtil.getNowTime();
	    	//解析并将获取的运单号信息存储到dmp与ems运单对照关系表
			eMSDAO.saveEMSEmailnoAndDMPTranscwb(cwb,transcwb,emsAndDmpTranscwb.getBillno(),bingTime);
			
			//更新订单临时表"获取运单状态字段"值
			List<SendToEMSOrder> orderList = eMSDAO.getSendOrderByTranscwb(transcwb);
			if(orderList.size()==0){
				throw new CwbException("","EMS订单下发临时表中没有对应运单记录!运单号为：["+transcwb+"]"); 
			}else{
				eMSDAO.updateGetTranscwbStateByTranscwb(transcwb);
			}
			logger.info("请求[EMS]获取到对应运单号成功!");
			return;
	
		} catch (Exception e) {
			logger.error("处理请求[EMS]运单号异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
		}
	}
	
	//运单轨迹定时器单元测试
	@Test
	public void testSelectTempAndImitateDmpOpt(){
		eMSTimmer.selectTempAndImitateDmpOpt();
	}
	
	//测试推送订单给ems
	@Test
	public void testSenderOrder(){
		EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());
		List<SendToEMSOrder> subList = null;
		//eMSService.handleSendOrderToEMS(ems,subList);
		eMSTimmer.sendOrderToEMS();
	}
	
	//获取ems运单号测试
	@Test
	public void testGetEmsMail(){
		eMSTimmer.getEmsMailNoTask();
	}
	
	public static void main(String[] args){
		
		String s = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48cmVzcG9uc2U"
				+ "+PHJlc3VsdD4xPC9yZXN1bHQ"
				+ "+PGVycm9yRGVzYz7ml6DplJnor6/kv6Hmga88L2Vycm9yRGVzYz48ZXJyb3JDb2RlPkUwMDA8L2Vy"
				+ "cm9yQ29kZT48L3Jlc3BvbnNlPg==";
		try {
			String str = new String(new BASE64Decoder().decodeBuffer(s));
			System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
