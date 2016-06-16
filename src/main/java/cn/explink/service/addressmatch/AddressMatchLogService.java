package cn.explink.service.addressmatch;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.core.utils.JsonUtil;
import cn.explink.dao.AddressMatchLogDAO;
import cn.explink.domain.AddressMatchLog;
import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.AddressMappingResultEnum;
import cn.explink.domain.addressvo.AddressMatchLogMatchStatusEnum;
import cn.explink.domain.addressvo.OrderAddressMappingResult;
import cn.explink.domain.addressvo.ResultCodeEnum;

/**
 * 地址匹配日志表Service
 * @author neo01.huang
 * 2016-4-12
 */
@Service
public class AddressMatchLogService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AddressMatchLogDAO addressMatchLogDAO;
	
	@PostConstruct
	public void init() {
		//初始化匹配地址日志收集器
		AddressMatchLogCollector.init(addressMatchLogDAO);
	}
	
	/**
	 * 生产地址匹配日志
	 * @param itemno 序号
	 * @param address 输入地址
	 * @param orderId orderId，UUID+序号
	 * @param addressreturn 地址匹配结果
	 */
	public void produceAddressMatchLog(String itemno, String address, String orderId, AddressMappingResult addressreturn) {
		
		try {
			logger.info("produceAddressMatchLog->itemno:{}, address:{}, orderId:{}, addressreturn:{}",
					itemno, orderId, address, JsonUtil.translateToJson(addressreturn));
			if (addressreturn == null) {
				logger.info("produceAddressMatchLog->addressreturn is null--skiped! itemno:{}, orderId:{}, address:{}",
						itemno, orderId, address);
				return;
			}
			
			//不成功
			if (addressreturn.getResultCode() == null || ResultCodeEnum.success.getCode() != addressreturn.getResultCode().getCode()) {
				logger.info("produceAddressMatchLog->addressreturn not success--skiped! itemno:{}, orderId:{}, address:{}",
						itemno, orderId, address);
				return ;
			}
			
			AddressMatchLog addressMatchLog = new AddressMatchLog();
			addressMatchLog.setId(null); //id
			addressMatchLog.setItemno(itemno); //序号
			addressMatchLog.setAddress(address); //输入地址 
			
			//匹配状态
			String matchStatus = AddressMatchLogMatchStatusEnum.EXCEPTION_RESULT.key();
			
			if (addressreturn.getResultMap() == null || addressreturn.getResultMap().size() == 0) {
				logger.info("produceAddressMatchLog->addressreturn.getResultMap is null or empty--skiped! itemno:{}, orderId:{}, address:{}",
						itemno, orderId, address);
				return;
			}
			
			OrderAddressMappingResult orderAddressMappingResult = addressreturn.getResultMap().get(orderId);
			if (orderAddressMappingResult == null) {
				logger.info("produceAddressMatchLog->orderAddressMappingResult is null--skiped! itemno:{}, orderId:{}, address:{}",
						itemno, orderId, address);
				return;
			}
			
			//singleResult ==> 成功
			if (AddressMappingResultEnum.singleResult.equals(orderAddressMappingResult.getResult())) {
				matchStatus = AddressMatchLogMatchStatusEnum.SUCCESS.key();
				
			//zeroResult
			} else if (AddressMappingResultEnum.zeroResult.equals(orderAddressMappingResult.getResult())) {
				
				//addressList为空 ==> 未维护关键字
				if (orderAddressMappingResult.getAddressList() == null ||
						orderAddressMappingResult.getAddressList().size() == 0) {
					matchStatus = AddressMatchLogMatchStatusEnum.NOT_MAINTENANCE_KEYWORD.getKey();
					
				} else { //addressList不为空 ==> 关键字无对应站点
					matchStatus = AddressMatchLogMatchStatusEnum.KEYWORD_NO_STATION.key();
					
				}
			
			//multipleResult
			} else if (AddressMappingResultEnum.multipleResult.equals(orderAddressMappingResult.getResult())) {
				matchStatus = AddressMatchLogMatchStatusEnum.MULTIPLE_RESULT.getKey();
				
			} else { //exceptionResult
				matchStatus = AddressMatchLogMatchStatusEnum.EXCEPTION_RESULT.getKey();
				
			}
			
			addressMatchLog.setMatchStatus(matchStatus); //匹配状态
			
			
			List<String> matchMsgList = new ArrayList<String>();
			if (StringUtils.isNotEmpty(addressreturn.getMessage())) {
				matchMsgList.add(addressreturn.getMessage());
			}
			if (StringUtils.isNotEmpty(orderAddressMappingResult.getMessage())) {
				matchMsgList.add(orderAddressMappingResult.getMessage());
			}
			StringBuffer matchMsg = new StringBuffer();
			for (int i = 0, len = matchMsgList.size(); i < len; i++) {
				String msg = matchMsgList.get(i);
				if (msg == null || msg.length() == 0) {
					continue;
				}
				
				if (i == 0) {
					matchMsg.append(msg);
					
				} else {
					matchMsg.append("__").append(msg);
					
				}
			}
			addressMatchLog.setMatchMsg(matchMsg.toString()); //失败原因
			
			addressMatchLog.setCreateTime(new Timestamp(System.currentTimeMillis())); //创建时间
			addressMatchLog.setUpdateTime(null); //修改时间
			
			//logger.info("addressMatchLog==>" + JsonUtil.translateToJson(addressMatchLog));
			
			//生产 地址匹配日志
			AddressMatchLogCollector.produce(addressMatchLog, addressMatchLogDAO);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
