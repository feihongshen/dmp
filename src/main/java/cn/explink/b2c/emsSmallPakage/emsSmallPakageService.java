package cn.explink.b2c.emsSmallPakage;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.ems.EMSDAO;
import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.domain.CwbOrder;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

public class emsSmallPakageService {
	private Logger logger = LoggerFactory.getLogger(emsSmallPakageService.class);
	@Autowired
	EMSDAO eMSDAO;
	/*
	 * @author zhouhuan
	 * @param order:订单信息，transcwb:dmp运单号,emailno:邮政运单号
	 * @remark:保存需要发送给邮政小包的订单信息
	 * @add time 2016-07-20
	 */
	public void  saveOrderForEmsSmallPakage(CwbOrder order,String transcwb,String emailno)throws Exception{
	
		try {
			if(order==null){
				this.logger.info("dmp订单号不存在，订单号为：{}",order.getCwb());
				return;
			}
			//校验订单是否重复插入
			int resaveFlag = validateOrderResave(order.getCwb());
			
			if(resaveFlag==1){
				this.logger.info("邮政小包订单下发接口临时表中已存在该订单对应的数据：订单号为：{}",order.getCwb());
				return;
			}
			
			this.logger.info("保存EMS订单消息开始：" + System.currentTimeMillis());
			int addTranscwbFlag = 0; 
			//处理订单信息，将dmp订单信息解析成发送给邮政小包的数据格式
			String data = getStringToEMS(order,transcwb,emailno);
			if(StringUtil.isEmpty(data)){
				return;
			}
			List<SendToEMSOrder> oldOrder = eMSDAO.getSendOrderByNo(transcwb,emailno);
			if(oldOrder.size()!=0){
				this.logger.info("发送给邮政小包的对应数据在接口临时表中已存在，运单号为：{}",transcwb);
				return;
			}
			String credate = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			//每个运单号对应临时表中一条记录
			eMSDAO.saveOrderInfo(order.getCwb(),transcwb,credate,addTranscwbFlag,data,0l);
			
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			throw e1;
		}
		this.logger.info("保存邮政小包订单消息结束：" + System.currentTimeMillis());
	}
	
	/*
	 * @author zhouhuan
	 * @param cwb:订单号
	 * @remark:校验订单是否重复插入
	 * @add time 2016-07-20
	 */
	public int validateOrderResave(String cwb){
		int flag = 0;
		List<SendToEMSOrder> list = eMSDAO.getOrderInfoByTranscwb(cwb);
		if(list.size()!=0){
			flag=1;
		}
		return flag;
	}
	
	/*
	 * @author zhouhuan
	 * @param cwb:订单号
	 * @remark:将dmp订单信息解析为发送的xml格式
	 * @add time 2016-07-20
	 */
	//
	public static String getStringToEMS(CwbOrder order,String transcwb,String mailno) throws JAXBException {
		UserInfo sender = new UserInfo();
		UserInfo receiver = new UserInfo();
		RequestOrder orderInfo = new RequestOrder();
		@SuppressWarnings("rawtypes")
		List<Item> items = new ArrayList();
		Item item = new Item();
		item.setItemName("办公用品");
		item.setNumber(1);
		item.setItemValue(0);
		items.add(item);
		sender.setName("徐得谱");
		sender.setPostCode("430000");
		sender.setPhone("027-82668066");
		receiver.setName(order.getConsigneename());
		receiver.setPostCode(order.getConsigneepostcode());
		receiver.setMobile(order.getConsigneemobile());
		receiver.setPhone(order.getConsigneephone());
		receiver.setProv(order.getCwbprovince());
		receiver.setCity(order.getCwbcity());
		receiver.setAddress(order.getConsigneeaddress());
		orderInfo.setEcCompanyId("PINJUN");
		orderInfo.setLogisticProviderID("2016051839597");
		orderInfo.setCustomerId(order.getCustomerid()+"");
		orderInfo.setTxLogisticID(order.getTranscwb());
		orderInfo.setTradeNo("259");
		orderInfo.setMailNo(mailno);
		orderInfo.setTotalServiceFee(0l);
		orderInfo.setCodSplitFee(0l);
		orderInfo.setBuyServiceFee(0l);
		orderInfo.setOrderType(1);
		orderInfo.setServiceType(0);
		orderInfo.setSender(sender);
		orderInfo.setReceiver(receiver);
		if(order.getAnnouncedvalue()!=null && order.getAnnouncedvalue()!=BigDecimal.ZERO){
			orderInfo.setGoodsValue(new Double(order.getAnnouncedvalue().doubleValue()*100).longValue());
		}else{
			orderInfo.setGoodsValue(0l);
		}
		orderInfo.setItems(items);
		orderInfo.setSpecial(0);
	    StringWriter sw = new StringWriter();
        JAXBContext jAXBContext = JAXBContext.newInstance(orderInfo.getClass());
        Marshaller marshaller = jAXBContext.createMarshaller();
        marshaller.marshal(orderInfo, sw);
		return sw.toString();
	}
	
	public static void main(String[] args){
		CwbOrder order = new CwbOrder();
		order.setAnnouncedvalue(new BigDecimal(12.1));
		order.setConsigneename("xiaoh");
		try {
			getStringToEMS(order,"12","45");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
