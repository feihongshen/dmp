package cn.explink.b2c.efast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.efast.orders_json.OrderInfo;
import cn.explink.b2c.efast.orders_json.OrderTotalResult;
import cn.explink.b2c.efast.orders_json.OrderTotalShell;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;

/**
 * 中兴云购，ERP系统接口service 获取订单列表
 * 
 * @author Administrator
 *
 */
@Service
public class EfastService_getOrderList extends EfastService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取订单列表
	 */
	public void getOrderList() {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("efastERP")) {
				int efastKey = enums.getKey();
				int isOpenFlag = jointService.getStateForJoint(efastKey);
				if (isOpenFlag == 0) {
					logger.info("未开启[中兴云购ERPO]对接！efastKey={}", efastKey);
					continue;
				}

				getOrderListMethod(efastKey);

			}
		}

	}

	private void getOrderListMethod(int efastKey) {

		Efast efast = getEfast(efastKey);
		long request_size = 20; // 请求每页数量
		int hours = efast.getBeforhours() == 0 ? 1 : efast.getBeforhours(); // 提前时间段
		int loopcount = efast.getLoopcount() == 0 ? 100 : efast.getLoopcount(); // 循环请求次数

		for (int i = 1; i <= loopcount; i++) {

			try {

				Map<String, String> params = buildRequestParms(i, request_size, hours, efast.getApp_key(), efast.getApp_secret(), efast.getApp_nick());

				String response = RestHttpServiceHanlder.sendHttptoServer(params, efast.getApp_url());

				logger.info("获取ERP订单列表-当前返回订单列表信息={},efastKey={}", response, efastKey);

				OrderTotalShell ordershell = JacksonMapper.getInstance().readValue(response, OrderTotalShell.class);
				OrderTotalResult orderresult = ordershell.getResp_data();
				long page_size = orderresult.getPage_size();
				long page_no = orderresult.getPage_no();
				long total_result = orderresult.getTotal_results();

				logger.info("获取ERP订单列表-当前下载次数={},total_result={},page_no={},page_size={},efastKey=" + efastKey, new Object[] { i, total_result, page_no, page_size });

				List<OrderInfo> orderlist = orderresult.getList();
				if (orderlist == null || orderlist.size() == 0) {
					logger.info("获取ERP订单列表-当前下载次数={},当前没有待下载的数据,return..efastKey={}", i, efastKey);
					return;
				}

				insertErpCwbs(orderlist, page_size, page_no, total_result, efast.getShipping_code(), efast.getErpEnum());
				logger.info("获取ERP订单列表-插入数据库成功,total_result={},当前处理次数={},efastKey=" + efastKey, total_result, i);

			} catch (Exception e) {
				logger.error("获取ERP订单列表-发生未知异常,当前页数=" + i + ",efastKey=" + efastKey, e);
			}

		}
	}

	/**
	 * 构建请求订单列表的参数
	 * 
	 * @param i
	 * @param request_size
	 * @param hours
	 * @return
	 */
	private Map<String, String> buildRequestParms(int i, long request_size, int hours, String app_key, String app_secret, String app_nick) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", app_key); // 请求唯一标识
		params.put("app_secret", app_secret); // 请求密钥
		params.put("app_nick", app_nick); // 请求接口标识

		params.put("app_act", "efast.trade.list.get"); // 请求参数名
		params.put("shipping_status", "1"); // 默认为1
		params.put("start_time", DateTimeUtil.getDateBeforeHours(hours, "yyyy-MM-dd HH:mm:ss"));
		params.put("end_time", DateTimeUtil.getNowTime());
		params.put("page_no", String.valueOf(i));
		params.put("page_size", String.valueOf(request_size));
		return params;
	}

	/**
	 * 每20条插入一次
	 * 
	 * @param orderresult
	 * @param page_size
	 * @param page_no
	 * @param total_result
	 */
	@Transactional
	private void insertErpCwbs(List<OrderInfo> orderlist, long page_size, long page_no, long total_result, String shipping_code, String erpEnum) {
		for (OrderInfo order : orderlist) {
			boolean ifexsistsflag = efastDAO.isExistsOrder_sn(order.getOrder_sn());
			if (ifexsistsflag) {
				logger.info("获取数据已存在,order_sn={}", order.getOrder_sn());
				continue;
			}
			if (!shipping_code.equals(order.getShipping_name())) {
				logger.info("该订单号={}不属于中兴云购的数据,shipping_name={}", order.getOrder_sn(), order.getShipping_name());
				continue;
			}

			efastDAO.insertErpCwb(order, total_result, page_no, page_size, erpEnum);

			logger.info("ERP-插入订单临时列表={}成功", order.getOrder_sn());
		}
	}

	public static void transJson_test(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		OrderTotalResult orderresult = new OrderTotalResult();
		orderresult.setTotal_results(100);
		orderresult.setPage_no(1);
		orderresult.setPage_size(20);

		List<OrderInfo> list = new ArrayList<OrderInfo>();
		for (int i = 0; i < 3; i++) {
			OrderInfo order = new OrderInfo();
			order.setOrder_sn("11111111" + i);
			order.setDeal_code("1111111" + i);
			order.setAdd_time("2013-" + i);
			list.add(order);
		}
		orderresult.setList(list);

		String jsoncontent = JacksonMapper.getInstance().writeValueAsString(orderresult);
		System.out.println(jsoncontent);
	}

}
