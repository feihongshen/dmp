package cn.explink.b2c.wenxuan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.homegobj.xmldto.Header;
import cn.explink.b2c.homegobj.xmldto.OrderRequest;
import cn.explink.b2c.homegobj.xmldto.Page;
import cn.explink.b2c.homegobj.xmldto.RequestBody;
import cn.explink.b2c.wenxuan.jdto.Orderlist;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 文轩网 获取订单 详情
 * 
 * @author Administrator
 *
 */
@Service
public class WenxuanService_getOrder extends WenxuanService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//
	// paramMap.put("startTime",starttime);
	// paramMap.put("endTime",DateTimeUtil.getNowDate());
	// paramMap.put("page",i+"");
	// paramMap.put("size",hg.getPagesize()+"");
	//
	//
	// paramMap.put("appkey",hg.getApikey());
	// paramMap.put("timestamp",String.valueOf(timestamp));
	// paramMap.put("appsign",appsign);

	// String
	// linkParams="?appkey="+hg.getApikey()+"&timestamp="+timestamp+"&appsign="+appsign+"&startTime="+starttime+"&endTime="+DateTimeUtil.getNowDate()+"&page="+i+"&size="+hg.getPagesize();
	//
	// String requestURL = hg.getDownloadUrl()+linkParams;
	//
	// String responseXML = RestHttpServiceHanlder.sendHttptoServer(null,"GET",
	// requestURL);

	// Map<String,String> paramMap=new HashMap<String, String>();

	/**
	 * 执行订单下载 根据时间下载
	 */
	public void excutorGetOrdersByTimes() {
		// try {
		//
		// int isOpenFlag =
		// jointService.getStateForJoint(B2cEnum.Wenxuan.getKey());
		//
		// if (isOpenFlag == 0) {
		// logger.info("未开启文轩网对接！");
		// return;
		// }
		// Wenxuan hg = this.getWenxuan(B2cEnum.Wenxuan.getKey());
		//
		// String starttime =
		// DateTimeUtil.getDateBeforeHours(hg.getBeforhours(), "yyyy-MM-dd");
		//
		// for (int i = 1; i <= hg.getLoopcount(); i++) { // 通常页数设置在30页，每页200
		// // 条数据 即可 ,数据量大了可调整
		// ApiParameters parameters = new ApiParameters();
		// parameters.add("startTime", starttime);
		// parameters.add("endTime", DateTimeUtil.getNowDate());
		// parameters.add("page", i+"");
		// parameters.add("size", hg.getPagesize()+"");
		// WinXuanRequest request = new WinXuanRequest();
		// request.setApiUrl(hg.getDownloadUrl());
		// request.setAppKey(hg.getApikey());
		// request.setAppSecret(hg.getApiSecret());
		// request.setHttpMethod(ApiHttpMethod.GET);
		//
		// WinXuanClient client = new DefaultWinXuanClient();
		// WinXuanResponse response = client.execute(request, parameters);
		//
		//
		// String resultJson = response.getResult();
		//
		// WinxuanOrders
		// winxuanOrders=JacksonMapper.getInstance().readValue(resultJson,
		// WinxuanOrders.class);
		//
		//
		// logger.info("文轩网返回-xml={}",resultJson);
		//
		//
		// List<Map<String, String>> orderlist =
		// parseCwbArrByOrderDto(winxuanOrders.getOrderlist(), hg);
		//
		// if (orderlist == null || orderlist.size() == 0) {
		// logger.warn("文轩网-请求没有封装参数，订单号可能为空");
		// continue;
		// }
		//
		// long warehouseid = hg.getWarehouseid(); // 订单导入的库房Id
		// dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(hg.getCustomerid()),B2cEnum.Wenxuan.getMethod(),
		// orderlist, warehouseid,true);
		//
		// logger.info("文轩网导入订单临时表成功，当前第{}页", i);
		//
		// }
		//
		// } catch (Exception e) {
		// logger.error("获取文轩网出现异常",e);
		// }
	}

	private String getSign(Wenxuan hg, Map<String, String> paramMap, long timestamp) {
		String signstr1 = WenxuanCore.createLinkString(paramMap); // 拼接为参数1=值1&参数2=值2
																	// 升序
		String method = "GET";
		String signstr2 = hg.getApiSecret() + method + hg.getDownloadUrl().substring(hg.getDownloadUrl().indexOf(".com/") + 4) + signstr1 + timestamp;
		String appsign = MD5Util.md5(signstr2);
		return appsign;
	}

	// Map<String,String> paramMap=new HashMap<String, String>();
	// long timestamp=System.currentTimeMillis();
	// String appsign = getSign(hg, paramMap, timestamp);
	//
	// paramMap.put("appkey",hg.getApikey());
	// paramMap.put("timestamp",String.valueOf(timestamp));
	// paramMap.put("appsign",appsign);
	// paramMap.put("waybillId",waybill);
	//
	// String
	// linkParams="?appkey="+hg.getApikey()+"&timestamp="+timestamp+"&appsign="+appsign+"&waybillId="+waybill;
	//
	// String requestURL = hg.getDownloadUrl()+linkParams;
	//
	// String responseXML = RestHttpServiceHanlder.sendHttptoServer(null,"GET",
	// requestURL);

	/**
	 * 执行订单下载 根据运单号下载数据
	 */
	public String excutorGetOrdersByWayBill(String waybill) {

		try {
			//
			// int isOpenFlag =
			// jointService.getStateForJoint(B2cEnum.Wenxuan.getKey());
			//
			// if (isOpenFlag == 0) {
			// logger.info("未开启文轩网对接！");
			// throw new RuntimeException("请求文轩网运单号下载-未开启接口");
			//
			// }
			// Wenxuan hg = this.getWenxuan(B2cEnum.Wenxuan.getKey());
			//
			//
			//
			//
			//
			// ApiParameters parameters = new ApiParameters();
			// parameters.add("waybillId",waybill);
			// WinXuanRequest request = new WinXuanRequest();
			// request.setApiUrl(hg.getWabillUrl());
			// request.setAppKey(hg.getApikey());
			// request.setAppSecret(hg.getApiSecret());
			// request.setHttpMethod(ApiHttpMethod.GET);
			//
			// WinXuanClient client = new DefaultWinXuanClient();
			// WinXuanResponse response = client.execute(request, parameters);
			//
			//
			// String resultJson = response.getResult();
			//
			//
			//
			// logger.info("文轩网返回-xml={}",resultJson);
			//
			//
			//
			// WinxuanOrders
			// winxuanOrders=JacksonMapper.getInstance().readValue(resultJson,
			// WinxuanOrders.class);
			//
			//
			//
			// List<Map<String, String>> orderlist =
			// parseCwbArrByOrderDto(winxuanOrders.getOrderlist(), hg);
			//
			// if (orderlist == null || orderlist.size() == 0) {
			// logger.warn("文轩网-请求没有封装参数，订单号可能为空");
			// throw new RuntimeException("请求文轩网运单号下载-未下载到数据");
			// }
			//
			// long warehouseid = hg.getWarehouseid(); // 订单导入的库房Id
			// dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(hg.getCustomerid()),B2cEnum.Wenxuan.getMethod(),
			// orderlist, warehouseid,true);
			//
			// logger.info("文轩网导入订单临时表成功,单号={}",waybill);
			//
			return "运单" + waybill + "数据下载成功";

		} catch (Exception e) {
			logger.error("获取文轩网出现异常", e);
			throw new RuntimeException("请求文轩网运单号下载异常" + e.getMessage());
		}
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(List<Orderlist> orderDtoList, Wenxuan hg) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		for (Orderlist orderDto : orderDtoList) {
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(orderDto.getDeliveryCode());
			if (cwbOrder != null) {
				logger.warn("获取文轩网订单中含有重复数据cwb={}", cwbOrder.getCwb());
				continue;
			}

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", orderDto.getDeliveryCode()); // cwb
			cwbMap.put("consigneename", orderDto.getCustomerName());
			cwbMap.put("consigneepostcode", orderDto.getZipCode());
			cwbMap.put("consigneemobile", orderDto.getMobile());
			cwbMap.put("cargorealweight", orderDto.getWeight()); // 订单重量(KG)
			cwbMap.put("consigneeaddress", orderDto.getAddress());
			cwbMap.put("receivablefee", orderDto.getRequidPayMoney()); // COD代收款
			cwbMap.put("cwbordertypeid", "1");
			cwbMap.put("sendcarnum", orderDto.getPackageCount()); // 发货货物名称

			cwbList.add(cwbMap);
		}

		return cwbList;
	}

	private OrderRequest buildOrderRequest(Wenxuan hg, String starttime, int i) {
		OrderRequest orderRequest = new OrderRequest();

		Header requestHeader = new Header();
		requestHeader.setApp_id(hg.getApikey());
		requestHeader.setCharset("01");
		requestHeader.setData_type("01");
		requestHeader.setFunction_id("");
		requestHeader.setRequest_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		requestHeader.setSign_type("02");

		requestHeader.setVersion("1.0");
		orderRequest.setRequestHeader(requestHeader);

		RequestBody requestBody = new RequestBody();
		requestBody.setStart_time(starttime);
		requestBody.setEnd_time(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		Page requestPage = new Page();
		requestPage.setPage_no(i);
		requestPage.setPage_size(10);
		requestBody.setRequestPage(requestPage);

		orderRequest.setRequestBody(requestBody);
		return orderRequest;
	}

	private String buildOrderRequest(Wenxuan hg, String starttime, int i, String sign) {
		return null;
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

	public static void main(String[] args) {

		String starttime = DateTimeUtil.getDateBeforeHours(24, "yyyy-MM-dd");
		System.out.println(starttime);
	}
}
