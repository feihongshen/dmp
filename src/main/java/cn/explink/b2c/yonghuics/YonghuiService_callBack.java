package cn.explink.b2c.yonghuics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.HttpClienCommon;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.b2c.yonghuics.json.OrderCallbackDto;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 下载数据成功后回传
 * 
 * @author Administrator
 *
 */
@Service
public class YonghuiService_callBack extends YonghuiService {
	private Logger logger = LoggerFactory.getLogger(YonghuiService_callBack.class);

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	/**
	 * 回传下载成功记录
	 */
	public void ExportCallBackByYiHaoDian(int key, int loopcount) {
		Yonghui yh = getYonghui(key);
		try {
			List<CwbOrderDTO> datalist = dataImportDAO_B2c.getCwbOrderByCustomerIdAndPageCount(Long.parseLong(yh.getCustomerids()), yh.getCallBackCount());
			if (datalist == null || datalist.size() == 0) {
				return;
			}
			OrderCallbackDto condto = new OrderCallbackDto();
			condto.setUserCode(yh.getUserCode());
			String nowtime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
			condto.setRequestTime(nowtime);
			condto.setSign(MD5Util.md5(yh.getUserCode() + nowtime + yh.getPrivate_key()));
			String multiTranscwbs = getTranscwbs(datalist);
			condto.setCwbs(multiTranscwbs.replaceAll("'", ""));

			String jsoncontent = JacksonMapper.getInstance().writeValueAsString(condto);

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("content", jsoncontent);

			// String
			// responseJson=RestHttpServiceHanlder.sendHttptoServer(paramsMap,yh.getCallback_URL());
			logger.info("订单获取成功回调-请求信息={}", jsoncontent);

			String responseJson = HttpClienCommon.post(paramsMap, null, yh.getCallback_URL(), 5000, 5000, "utf-8");

			logger.info("订单获取成功回调-当前永辉超市返回={}", responseJson);

			ReturnDto returnDto = JacksonMapper.getInstance().readValue(responseJson, ReturnDto.class);

			if (!returnDto.getErrCode().equals(YonghuiExpEmum.Success.getErrCode())) {
				logger.info("回调[永辉超市]的订单信息导出成功的接口-返回异常:errCode={},errMsg={},loopcount=" + loopcount, returnDto.getErrCode(), returnDto.getErrMsg());
				return;
			} else {
				String opscwbids = "";
				for (CwbOrderDTO cwbOrderDTO : datalist) {
					opscwbids += cwbOrderDTO.getOpscwbid() + ",";
				}
				opscwbids = opscwbids.length() > 1 ? opscwbids.substring(0, opscwbids.length() - 1) : "0";
				dataImportDAO_B2c.updateIsB2cSuccessFlagByIds(opscwbids);

				logger.info("成功的回调[永辉超市]的订单信息导出成功的接口,当前回调运单号组={},loopcount=" + loopcount, multiTranscwbs);
			}

			if (datalist != null) {
				ExportCallBackByYiHaoDian(key, loopcount + 1);
			}

		} catch (Exception e) {
			logger.error("error info while request yihaodian export cwb Successfully CallBackReturn  interface!" + e, e);
			e.printStackTrace();
		}
	}

	private String getTranscwbs(List<CwbOrderDTO> datalist) {
		if (datalist != null && datalist.size() > 0) {
			StringBuffer sub = new StringBuffer();
			for (CwbOrderDTO cwbOrder : datalist) {
				sub.append("'" + cwbOrder.getTranscwb() + "',");
			}
			return sub.substring(0, sub.length() - 1);
		}
		return null;
	}

}
