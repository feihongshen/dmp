package cn.explink.b2c.yonghuics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.HttpClienCommon;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.yonghuics.json.OrderDownloadReq;
import cn.explink.b2c.yonghuics.json.OrderDownloadRet;
import cn.explink.b2c.yonghuics.json.OrderDto;
import cn.explink.b2c.yonghuics.json.OrderListDto;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 下载订单数据
 * 
 * @author Administrator
 *
 */
@Service
public class YonghuiService_download extends YonghuiService {

	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	private Logger logger = LoggerFactory.getLogger(YonghuiService_download.class);

	/**
	 * 下载永辉超市订单信息
	 */
	public void DownLoadCwbDetailByYiHaoDian(int yhd_key, int loopcount) {
		Yonghui yh = getYonghui(yhd_key);
		try {
			OrderDownloadRet exportDto = BuildOrderExportCondition(yh);
			if (exportDto != null && !exportDto.getErrCode().equals(YonghuiExpEmum.Success.getErrCode())) {
				logger.info("调用永辉超市订单下载接口异常!errCode={},errMsg={},key=" + yhd_key + ",loopcount=" + loopcount, exportDto.getErrCode(), exportDto.getErrMsg());
				return;
			}
			List<Map<String, String>> cwbOrderList = parseCwbArrByOrderDto(exportDto); // 返回一个封装好的List
			if (cwbOrderList == null) {
				logger.warn("请求永辉超市没有下载到订单数据!errCode={},errMsg={},yhd_key=" + yhd_key + ",loopcount=" + loopcount, exportDto.getErrCode(), exportDto.getErrMsg());
				return;
			}
			try {
				long warehouseid = yh.getWarehouseid(); // 订单导入的库房Id
				dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(yh.getCustomerids()), B2cEnum.YongHuics.getMethod(), cwbOrderList, warehouseid, true);
				logger.info("[永辉超市]下载订单信息调用数据导入接口-插入数据库成功!loopcount=" + loopcount);

			} catch (Exception e) {
				logger.error("[永辉超市]调用数据导入接口异常!,订单List信息:" + cwbOrderList + "exptMessage=:" + e);
				e.printStackTrace();
				return;
			}

		} catch (Exception e) {
			logger.error("error info by request yonghuics download cwb detail interface!,loopcount=" + loopcount, e);
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 构建一个对接请求并返回DTO
	 * 
	 * @param yh
	 * @return
	 * @throws Exception
	 */
	private OrderDownloadRet BuildOrderExportCondition(Yonghui yh) throws Exception {
		OrderDownloadReq condto = new OrderDownloadReq();
		condto.setUserCode(yh.getUserCode());
		condto.setPageSize(yh.getExportCwb_pageSize());
		String nowtime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		condto.setRequestTime(nowtime);
		condto.setSign(MD5Util.md5(yh.getUserCode() + nowtime + yh.getPrivate_key()));

		String jsoncontent = JacksonMapper.getInstance().writeValueAsString(condto);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("content", jsoncontent);
		// String
		// responseJson=RestHttpServiceHanlder.sendHttptoServer(paramsMap,yh.getDownload_URL());
		String responseJson = HttpClienCommon.post(paramsMap, null, yh.getDownload_URL(), 5000, 5000, "utf-8");

		logger.info("订单数据下载-当前永辉超市返回={}", responseJson);
		JSONObject jsonobject = JSONObject.fromObject(responseJson);

		OrderDownloadRet orderDownloadRet = new OrderDownloadRet();
		String errCode = jsonobject.getString("errCode");
		String errMsg = jsonobject.getString("errMsg");
		orderDownloadRet.setErrCode(errCode);
		orderDownloadRet.setErrMsg(errMsg);

		String orderlist = jsonobject.getString("orderList");

		OrderListDto orderListDto = new OrderListDto();
		List<OrderDto> orderlists = JacksonMapper.getInstance().readValue(orderlist, new TypeReference<List<OrderDto>>() {
		});
		orderListDto.setOrderDtoList(orderlists);

		orderDownloadRet.setOrderListDto(orderListDto);
		// OrderDownloadRet
		// exportDto=JacksonMapper.getInstance().readValue(responseJson,OrderDownloadRet.class);
		// OrderDownloadRet
		// exportDto=JacksonMapper.getInstance().readValue(responseJson,new
		// TypeReference<OrderDownloadRet>() {});

		return orderDownloadRet;
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(OrderDownloadRet exportDto) {
		List<Map<String, String>> cwbList = null;
		OrderListDto orderListDto = exportDto.getOrderListDto();
		if (orderListDto == null) {
			return null;
		}
		List<OrderDto> orderlist = orderListDto.getOrderDtoList();
		if (orderlist != null && orderlist.size() > 0) {
			cwbList = new ArrayList<Map<String, String>>();
			for (OrderDto order : orderlist) {
				Map<String, String> cwbMap = new HashMap<String, String>();

				cwbMap.put("cwb", order.getSheetid());
				cwbMap.put("transcwb", order.getBagno());
				cwbMap.put("consigneename", order.getRecName());
				cwbMap.put("consigneeaddress", order.getOrder_address());
				cwbMap.put("consigneemobile", order.getRecphone());
				cwbMap.put("cargorealweight", order.getWeight());
				cwbMap.put("receivablefee", "0");
				cwbMap.put("caramount", order.getAllamt() + "");
				cwbMap.put("sendcarnum", "1");
				cwbMap.put("customercommand", order.getLogistics_name());
				cwbMap.put("cwbordertypeid", "1");
				cwbMap.put("cwbremark", order.getNote());
				cwbMap.put("remark1", order.getOrdertype());

				cwbMap.put("multi_shipcwb", order.getBagno()); // 标识oms存储多次推送
				cwbList.add(cwbMap);

			}
		}
		return cwbList;
	}

}
