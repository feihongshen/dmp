package cn.explink.b2c.homegobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.homegobj.xmldto.Good;
import cn.explink.b2c.homegobj.xmldto.Header;
import cn.explink.b2c.homegobj.xmldto.OrderDto;
import cn.explink.b2c.homegobj.xmldto.OrderRequest;
import cn.explink.b2c.homegobj.xmldto.OrderResponse;
import cn.explink.b2c.homegobj.xmldto.Page;
import cn.explink.b2c.homegobj.xmldto.RequestBody;
import cn.explink.b2c.homegobj.xmldto.ResponseBody;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 北京家有购物 获取订单 详情
 * 
 * @author Administrator
 *
 */
@Service
public class HomegobjService_getOrderDetailList extends HomegobjService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 执行订单下载
	 */
	public void excutorGetOrders() {
		try {

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.HomegoBJ.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启家有购物POS对接！");
				return;
			}
			Homegobj hg = this.getHomegobj(B2cEnum.HomegoBJ.getKey());

			String starttime = DateTimeUtil.getDateBeforeHours(hg.getBeforhours(), "yyyyMMddHHmmss");

			for (int i = 1; i <= hg.getLoopcount(); i++) { // 通常页数设置在30页，每页200
															// 条数据 即可 ,数据量大了可调整

				String xmlSignStr = buildOrderRequest(hg, starttime, i, null); // 构建sign字符串

				logger.info("待签名字符串:{}", xmlSignStr);

				String signed = MD5Util.md5(xmlSignStr + hg.getPrivate_key());

				String requestXML = buildOrderRequest(hg, starttime, i, signed); // 构建sign字符串

				logger.info("请求家有购物北京-xml={}", requestXML);

				String responseXML = RestHttpServiceHanlder.sendHttptoServer(requestXML, hg.getGetOrder_url());

				logger.info("家有购物北京返回-xml={}", responseXML);

				OrderResponse orderResponse = HomegoUnmarchal.Unmarchal(responseXML); // 反序列化为bean

				String resp_code = orderResponse.getHeader().getResp_code();
				String resp_msg = orderResponse.getHeader().getResp_msg();
				if (!"00".equals(resp_code)) {
					logger.info("家有购物返回校验异常,resp_code={},resp_msg={}", resp_code, resp_msg);
					return;
				}

				ResponseBody responseBody = orderResponse.getResponseBody();

				List<OrderDto> orderDtoList = responseBody.getOrders().getOrderDto();
				if (orderDtoList == null || orderDtoList.size() == 0) {
					logger.info("未获取到家有购物订单明细,当前页数{}", i);
					continue;
				}

				List<Map<String, String>> orderlist = parseCwbArrByOrderDto(orderDtoList, hg);

				if (orderlist == null || orderlist.size() == 0) {
					logger.warn("家有购物-请求没有封装参数，订单号可能为空");
					continue;
				}

				long warehouseid = hg.getWarehouseid(); // 订单导入的库房Id
				dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(hg.getCustomerid()), B2cEnum.HomegoBJ.getMethod(), orderlist, warehouseid, true);

				logger.info("家有购物北京导入订单临时表成功，当前第{}页", i);

			}

		} catch (Exception e) {
			logger.error("获取家有购物北京出现异常", e);
		}
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(List<OrderDto> orderDtoList, Homegobj hg) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		for (OrderDto orderDto : orderDtoList) {
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(orderDto.getInvc_id());
			if (cwbOrder != null) {
				logger.warn("获取家有购物订单中含有重复数据cwb={}", cwbOrder.getCwb());
				continue;
			}

			String orderType = orderDto.getOrd_type();
			int cwbordertypeid = 1;
			if ("00".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			} else if ("10".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
			} else {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			}
			double caramount = 0;
			String sendcarname = "";
			String remark1 = ""; // 出库号
			List<Good> goods = orderDto.getGoods().getGood();
			for (Good good : goods) {
				caramount += good.getGood_prc();
				sendcarname += good.getGood_nm() + ",";
				remark1 += good.getOutgo_no() + ",";
			}
			remark1 = !remark1.isEmpty() ? remark1.substring(0, remark1.length() - 1) : remark1;

			String detailaddress = orderDto.getReceiver().getProv() + orderDto.getReceiver().getCity() + orderDto.getReceiver().getDistrict() + orderDto.getReceiver().getAddress();

			String send_start_time = orderDto.getSend_start_tim();
			if (send_start_time != null && !send_start_time.isEmpty()) {
				send_start_time = "建议派送时间：" + send_start_time;
			}
			String send_end_time = orderDto.getSend_end_time();
			if (send_end_time != null && !send_end_time.isEmpty()) {
				send_end_time = "建议派送完成时间：" + send_end_time;
			}
			String size = orderDto.getLength() + "x" + orderDto.getWidth() + "x" + orderDto.getHeight();
			if (orderDto.getLength() == null || orderDto.getHeight() == null || orderDto.getWeight() == null) {
				size = "";
			}

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", orderDto.getInvc_id()); // cwb
			cwbMap.put("transcwb", orderDto.getOrd_id()); // transcwb
			cwbMap.put("consigneename", orderDto.getReceiver().getName());
			cwbMap.put("consigneepostcode", orderDto.getReceiver().getPostcode() == null ? "" : orderDto.getReceiver().getPostcode());
			cwbMap.put("consigneephone", orderDto.getReceiver().getPhone());
			cwbMap.put("consigneemobile", orderDto.getReceiver().getMobile());
			cwbMap.put("cargorealweight", orderDto.getWeight()); // 订单重量(KG)
			cwbMap.put("cwbprovince", orderDto.getReceiver().getProv());// 省
			cwbMap.put("cwbcity", orderDto.getReceiver().getCity());// 市
			cwbMap.put("consigneeaddress", detailaddress);
			cwbMap.put("caramount", String.valueOf(caramount));
			cwbMap.put("receivablefee", orderDto.getCod_amt()); // COD代收款
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			cwbMap.put("cargosize", size); // 尺寸
			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			cwbMap.put("remark1", remark1); // 出库号
			cwbMap.put("customercommand", orderDto.getRemark() == null ? "" : orderDto.getRemark() + "," + send_start_time == null ? "" : send_start_time + "," + send_end_time == null ? ""
					: send_end_time); // 备注信息

			cwbList.add(cwbMap);
		}

		return cwbList;
	}

	private OrderRequest buildOrderRequest(Homegobj hg, String starttime, int i) {
		OrderRequest orderRequest = new OrderRequest();

		Header requestHeader = new Header();
		requestHeader.setApp_id(hg.getExpress_id());
		requestHeader.setCharset("01");
		requestHeader.setData_type("01");
		requestHeader.setFunction_id(FunctionEnum.JIAYOU0001.getKey());
		requestHeader.setRequest_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		requestHeader.setSign_type("02");

		requestHeader.setVersion("1.0");
		orderRequest.setRequestHeader(requestHeader);

		RequestBody requestBody = new RequestBody();
		requestBody.setStart_time(starttime);
		requestBody.setEnd_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		Page requestPage = new Page();
		requestPage.setPage_no(i);
		requestPage.setPage_size(hg.getPageSize());
		requestBody.setRequestPage(requestPage);

		orderRequest.setRequestBody(requestBody);
		return orderRequest;
	}

	private String buildOrderRequest(Homegobj hg, String starttime, int i, String sign) {
		String xml = "";
		if (sign != null) {
			xml += "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		}
		xml += "<Jiayou>";
		xml += "<header>" + "<function_id>JIAYOU0001</function_id>" + "<version>1.0</version>" + "<app_id>" + hg.getExpress_id() + "</app_id>" + "<charset>01</charset>" + "<sign_type>02</sign_type>";
		if (sign != null) {
			xml += "<signed>" + sign.toUpperCase() + "</signed>";
		}
		xml += "<data_type>01</data_type>" + "<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>" + "</header>" + "<body>" + "<start_time>" + starttime + "</start_time>"
				+ "<end_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</end_time>" + "<page>" + "<page_no>" + i + "</page_no>" + "<page_size>" + hg.getPageSize() + "</page_size>" + "</page>"
				+ "</body>" + "</Jiayou>";

		return xml;
	}

	// private OrderRequest buildOrderRequest(Homegobj hg, String starttime, int
	// i,String signed) {
	// OrderRequest orderRequest = new OrderRequest();
	//
	// Header requestHeader = new Header();
	// requestHeader.setApp_id(hg.getExpress_id());
	// requestHeader.setCharset("01");
	// requestHeader.setData_type("01");
	// requestHeader.setFunction_id(FunctionEnum.JIAYOU0001.getKey());
	// requestHeader.setRequest_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
	// requestHeader.setSign_type("02");
	//
	// requestHeader.setSigned(signed);
	// requestHeader.setVersion("1.0");
	// orderRequest.setRequestHeader(requestHeader);
	//
	// RequestBody requestBody = new RequestBody();
	// requestBody.setStart_time(starttime);
	// requestBody.setEnd_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
	// Page requestPage = new Page();
	// requestPage.setPage_no(i);
	// requestPage.setPage_size(hg.getPageSize());
	// requestBody.setRequestPage(requestPage);
	//
	// orderRequest.setRequestBody(requestBody);
	// return orderRequest;
	// }

}
