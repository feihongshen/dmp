package cn.explink.b2c.yihaodian;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.yihaodian.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 下载数据成功后回传
 * 
 * @author Administrator
 *
 */
@Service
public class Yihaodian_ExportCallBack extends YihaodianService {
	private Logger logger = LoggerFactory.getLogger(Yihaodian_ExportCallBack.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	/**
	 * 回传下载成功记录
	 */
	public void ExportCallBackByYiHaoDian(int yhd_key, int loopcount,String url,String customerid) {
		Yihaodian yihaodian = getYihaodian(yhd_key);
		try {
			
			List<CwbOrderDTO> datalist = dataImportDAO_B2c.getCwbOrderByCustomerIdsAndPageCount(customerid, yihaodian.getCallBackCount());
			if (datalist == null || datalist.size() == 0) {
				return;
			}
			OrderExportCallbackDto condto = new OrderExportCallbackDto();
			condto.setUserCode(yihaodian.getUserCode());
			String nowtime = DateTimeUtil.getNowTime();
			condto.setRequestTime(nowtime);
			condto.setSign(MD5Util.md5(yihaodian.getUserCode() + nowtime + yihaodian.getPrivate_key()));
			String multiCwbs = getCwbArrStr(datalist);
			condto.setShipmentCode(multiCwbs.replaceAll("'", ""));

			ReturnDto returnDto = restTemplate.exportCallBack(url, condto); // 返回dto
			if (!returnDto.getErrCode().equals(YihaodianExpEmum.Success.getErrCode())) {
				logger.info("回调[一号店]的订单信息导出成功的接口-返回异常:errCode={},errMsg={},loopcount=" + loopcount, returnDto.getErrCode(), returnDto.getErrMsg());
				return;
			} else {
				for (String cwb : condto.getShipmentCode().split(",")) {
					dataImportDAO_B2c.updateIsB2cSuccessFlagByCwbs(cwb);
				}

				logger.info("成功的回调[一号店]的订单信息导出成功的接口,当前回调运单号组={},loopcount=" + loopcount, multiCwbs);
			}

			if (datalist != null) {
				ExportCallBackByYiHaoDian(yhd_key, loopcount + 1,url,customerid);
			}

		} catch (Exception e) {
			logger.error("error info while request yihaodian export cwb Successfully CallBackReturn  interface!" + e, e);
			e.printStackTrace();
		}
	}

	private String getCwbArrStr(List<CwbOrderDTO> datalist) {
		if (datalist != null && datalist.size() > 0) {
			StringBuffer sub = new StringBuffer();
			for (CwbOrderDTO cwbOrder : datalist) {
				sub.append("'" + cwbOrder.getCwb() + "',");
			}
			return sub.substring(0, sub.length() - 1);
		}
		return null;
	}

}
