package cn.explink.b2c.rufengda;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.util.WebServiceHandler;

/**
 * 如风达 获取订单信息后通知 如风达
 * 
 * @author Administrator
 *
 */
@Service
public class RufengdaService_SuccessOrders extends RufengdaService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/***
	 * 获取订单状态更新
	 * ==================================================================
	 * ================
	 * 
	 */

	public void SuccessOrders(int rfd_key, int loopcount) {

		Rufengda rfd = getRufengda(rfd_key);

		int isOpenFlag = jointService.getStateForJoint(rfd_key);
		if (isOpenFlag == 0) {
			logger.warn("未开启[如风达][通知成功获取订单]接口");
			return;
		}
		try {

			List<CwbOrderDTO> datalist = dataImportDAO_B2c.getCwbOrderByCustomerIdAndPageCount(Integer.valueOf(rfd.getCustomerid()), rfd.getMaxCount());

			if (datalist == null || datalist.size() == 0) {
				logger.error("当前没有要推送[如风达]的[通知成功获取订单]信息loopcount={}", loopcount);
				return;
			}
			String orderNoList = getCwbArrStr(datalist);

			Object[] parms = { rfd.getLcId(), orderNoList.replaceAll("'", "") };
			Object returnValue = WebServiceHandler.invokeWs(rfd.getWs_url(), "SuccessOrders", parms);
			if (returnValue.toString().contains("true")) {

				for (String cwb : orderNoList.replaceAll("'", "").split(",")) {
					dataImportDAO_B2c.updateIsB2cSuccessFlagByCwbs(cwb);
				}

				logger.info("[通知成功获取订单]更新成功loopcount={},successOrders={}", loopcount, orderNoList);
			} else {
				logger.info("[通知成功获取订单]更新失败loopcount={},失败订单={}", loopcount, orderNoList);
			}

		} catch (Exception e) {
			logger.error("获取[如风达]订单出现未知异常", e);
			e.printStackTrace();
		}

	}

	private String getCwbArrStr(List<CwbOrderDTO> datalist) {
		if (datalist != null && datalist.size() > 0) {
			StringBuffer sub = new StringBuffer();
			for (CwbOrderDTO order : datalist) {
				sub.append("'" + order.getCwb() + "',");
			}
			return sub.substring(0, sub.length() - 1);
		}
		return null;
	}

}
