package cn.explink.b2c.explink.core_down;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.xmldto.EpaiAPIMarchal;
import cn.explink.b2c.explink.xmldto.EpaiAPIUnmarchal;
import cn.explink.b2c.explink.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.explink.xmldto.ReturnDto;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 系统之间的对接（下游）
 * 
 * @author Administrator 订单，
 */
@Service
public class EpaiApiService_ExportCallBack extends EpaiApiService {
	private Logger logger = LoggerFactory.getLogger(EpaiApiService_ExportCallBack.class);

	/**
	 * 订单下载成功回传通知
	 */
	public void exportCallBack_controllers() {
		List<EpaiApi> epailist = epaiApiDAO.getEpaiApiList();
		if (epailist == null || epailist.size() == 0) {
			logger.warn("订单下载回传通知-当前没有配置系统对接设置！");
			return;
		}
		for (EpaiApi epai : epailist) {
			if (epai.getIsopenflag() == 1) {
				exportCallBack(epai);
			} else {
				logger.warn("订单获取部分-当前m没有开启对接！" + epai.getUserCode());
			}

		}

	}

	/**
	 * 回传下载成功记录
	 */
	public void exportCallBack(EpaiApi epai) {

		try {

			List<CwbOrderDTO> datalist = dataImportDAO_B2c.getCwbOrderByCustomerIdAndPageCount(epai.getCustomerid(), epai.getPageSize());
			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有确认可回传上游的数据userCode={}", epai.getUserCode());
				return;
			}
			OrderExportCallbackDto condto = new OrderExportCallbackDto();
			condto.setUserCode(epai.getUserCode());
			String nowtime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
			condto.setRequestTime(nowtime);
			condto.setSign(MD5Util.md5(epai.getUserCode() + nowtime + epai.getPrivate_key()));
			String multiCwbs = getCwbArrStr(datalist);
			condto.setCwbs(multiCwbs);

			String content = EpaiAPIMarchal.Marchal_ExportCallBack(condto); // 通过jaxb状态对象对XML传输
			logger.info("回调0易派0的订单信息请求content={}", content);

			String responseContent = "";

			if (epai.getIspostflag() == 0) { // 使用数据流获取
				responseContent = RestTemplateHanlder.sendHttptoServer(content, epai.getCallBack_url());
			} else {
				Map<String, String> params = new HashMap<String, String>();
				params.put("content", content);
				responseContent = RestTemplateHanlder.sendHttptoServer(params, epai.getCallBack_url());
			}

			this.logger.info("回调0易派0的订单信息请求content={}", content);

			ReturnDto returnDto = EpaiAPIUnmarchal.Unmarchal_ExportCallBack(responseContent); // xml转化为bean对象

			if (!returnDto.getErrCode().equals(EpaiExpEmum.Success.getErrCode())) {
				logger.info("回调0易派0的订单信息导出成功的接口-返回异常:errCode={},errMsg={}", returnDto.getErrCode(), returnDto.getErrMsg());
				return;
			}

			String opscwbids = "";
			for (CwbOrderDTO order : datalist) {
				opscwbids += order.getOpscwbid() + ",";
			}

			dataImportDAO_B2c.updateIsB2cSuccessFlagByIds(opscwbids.length() > 0 ? opscwbids.substring(0, opscwbids.length() - 1) : "-1");

			logger.info("成功的回调0易派0的订单信息导出成功的接口,当前回调运单号组={}", multiCwbs);

			if (datalist != null && datalist.size() > 0) {
				exportCallBack(epai);
			}

		} catch (Exception e) {
			logger.error("下载完成回调0易派0发生未知异常", e);
			return;
		}
	}

	private String getCwbArrStr(List<CwbOrderDTO> datalist) {
		if (datalist != null && datalist.size() > 0) {
			StringBuffer sub = new StringBuffer();
			for (CwbOrderDTO cwbOrder : datalist) {
				sub.append(cwbOrder.getCwb() + ",");
			}
			return sub.substring(0, sub.length() - 1);
		}
		return null;
	}

}
