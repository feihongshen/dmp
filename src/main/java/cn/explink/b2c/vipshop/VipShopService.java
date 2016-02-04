package cn.explink.b2c.vipshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.vipshop.oxo.VipShopOXOGetCwbDataService;
import cn.explink.b2c.vipshop.oxo.VipShopOXOInsertCwbDetailTimmer;
import cn.explink.dao.CustomerDAO;

@Service
public class VipShopService {

	@Autowired
	VipShopGetCwbDataService vipShopGetCwbDataService;
	@Autowired
	VipShopOXOGetCwbDataService vipShopOXOGetCwbDataService;
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	@Autowired
	VipShopOXOInsertCwbDetailTimmer vipshopOXOInsertCwbDetailTimmer;
	@Autowired
	JointService jointService;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	CustomerDAO custoemrDAO;
	private Logger logger = LoggerFactory.getLogger(VipShopService.class);

	/**
	 * 执行唯品会定时器的公用方法
	 * 
	 * @return
	 */
	public void excuteVipshopDownLoadTask() {

		try {

			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("vipshop")) {
					int isOpenFlag = jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 0) {
						logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
						continue;
					}

					try {
						excuteGetOrdersByVipshop(enums);
					} catch (Exception e) {
						logger.error("唯品会对接循环处理异常",e);
					}

				}
			}

		} catch (Exception e) {
			logger.error("vipshop下载遇未知异常", e);
		}

		// b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",")>0?customerids.substring(0,customerids.length()-1):"",cretime,count,remark);

	}
	
	/**
	 * 执行唯品会OXO模式销售订单定时器
	 * 
	 * @return
	 */
	public void excuteVipshopOxoDownLoadTask() {

		try {

			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().equals(B2cEnum.VipShop_OXO.getMethod())) {
					int isOpenFlag = jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 0) {
						logger.info("未开启VipShop_OXO[" + enums.getKey() + "]对接！");
						break;
					}

					excuteGetOrdersByVipshopOXO(enums);
					break;
				}
			}

		} catch (Exception e) {
			logger.error("VipShop_OXO下载遇未知异常", e);
		}

	}


	private void excuteGetOrdersByVipshop(B2cEnum enums) {
		int loopcount = 20;
		for (int i = 0; i < loopcount; i++) {
			long resultflag = vipShopGetCwbDataService.getOrdersByVipShop(enums.getKey());
		//	vipshopInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
			if (resultflag == -1) { // -1标识下载不到或者异常，跳出循环 ,1标识仍然有数据未下载完毕
				return;
			}
			logger.info("当前下载唯品会次数={}", i);

		}

	}
	
	public void excuteGetOrdersByVipshopSigle() {
		
		try {

			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("vipshop")) {
					int isOpenFlag = jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 0) {
						logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
						continue;
					}

					try {
						 vipShopGetCwbDataService.getOrdersByVipShop(enums.getKey());
					} catch (Exception e) {
						logger.error("唯品会对接循环处理异常",e);
					}

				}
			}

		} catch (Exception e) {
			logger.error("vipshop下载遇未知异常", e);
		}
		
		
	}
	
	private void excuteGetOrdersByVipshopOXO(B2cEnum enums) {
		int loopcount = 20;
		for (int i = 0; i < loopcount; i++) {
			long resultflag = vipShopOXOGetCwbDataService.getOrdersByVipShopOXO(enums.getKey());
			vipshopOXOInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
			if (resultflag == -1) { // -1标识下载不到或者异常，跳出循环 ,1标识仍然有数据未下载完毕
				return;
			}
			logger.info("当前下载唯品会OXO次数={}", i);

		}

	}

}
