package cn.explink.b2c.pjwl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.Tools;

@Service
public class PjwlExpressService {
	@Autowired
	PjwlExpressGetPreOrderDataService pjwlExpressGetPreOrderDataService;
	@Autowired
	PjwlExpressInsertPreOrderDetailTimmer pjwlExpressInsertPreOrderDetailTimmer;
	
	@Autowired
	PjwlExpressGetTransNoDataService pjwlExpressGetTransNoDataService;
	@Autowired
	PjwlExpressInsertCwbOrderDetailTimmer pjwlExpressInsertCwbOrderDetailTimmer;
	
	@Autowired
	AddressTransferCollectorHelper addressTransferCollectorHelper;
	
	private Logger logger = LoggerFactory.getLogger(PjwlExpressService.class);
	
	/**
	 * 执行品竣物流预订单抓取定时器
	 * 
	 * @return
	 */
	public void excutePjwlExpressDownLoadTask() {

		try {
			excuteGetPreOrdersByPjwlExpress();
		} catch (Exception e) {
			logger.error("PJWL_Express下载遇未知异常", e);
		}
	}
	/**
	 * 获取预订单数据
	 * @param enums
	 */
	private void excuteGetPreOrdersByPjwlExpress() {
		//查询出所有的数据存放在map中
		Map<String, Map<String, String>> addressInfo = addressTransferCollectorHelper.getAddressCollectorMap();
		
		int loopcount = 20;
		//承运商编码
		String carrierCode = ResourceBundleUtil.expressCarrierCode;
		if (Tools.isEmpty(carrierCode)) {
			//carrierCode="";
			return;//如果没有配置承运商编码，则不进行获取
		}
		for (int i = 0; i < loopcount; i++) {
			int resultflag = pjwlExpressGetPreOrderDataService.getNormalPreOrdersByPjwlExpress(carrierCode);//carrierCode
			//将临时表中的记录数据插入到Dmp的预订单表中
			pjwlExpressInsertPreOrderDetailTimmer.selectTempAndInsertToPreOrderDetail(addressInfo);
			if (resultflag==0) { // 0标识下载不到或者异常，跳出循环 ,>0标识仍然有数据未下载完毕
				return;
			}
			logger.info("当前下载PJWL次数={}", i);

		}
	}
	/**
	 * 执行获取运单数据的定时器
	 */
	public void excutePjwlExpressTransNoSinfferTask() {
		try {
			excuteGetTransNoDatasByPjwlExpress();
		} catch (Exception e) {
			logger.error("PJWL_Express下载遇未知异常", e);
		}
		
	}
	/**
	 * 获取运单数据
	 */
	private void excuteGetTransNoDatasByPjwlExpress() {
		int loopcount = 10;
		//承运商编码
		String carrierCode = ResourceBundleUtil.expressCarrierCode;
		if (Tools.isEmpty(carrierCode)) {
			//carrierCode="";
			return;//如果没有配置承运商编码，则不进行获取
		}
		for (int i = 0; i < loopcount; i++) {
			int resultflag = pjwlExpressGetTransNoDataService.getNormalTransNoDatasByPjwlExpress(carrierCode);//carrierCode
			//将临时表中的记录数据插入到Dmp的订单表中
			pjwlExpressInsertCwbOrderDetailTimmer.selectTempAndInsertToCwbOrderDetail();
			if (resultflag==0) { // -1标识下载不到或者异常，跳出循环 ,1标识仍然有数据未下载完毕
				return;
			}
			logger.info("当前下载PJWL_TransData次数={}", i);
		}
	}

}
