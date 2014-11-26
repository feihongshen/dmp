package cn.explink.b2c.efast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.efast.orderdetail_json.OrderShipping;
import cn.explink.b2c.efast.orderdetail_json.Orderdetail;
import cn.explink.b2c.efast.orderdetail_json.OrderdetailShell;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;

/**
 * 中兴云购，ERP系统接口service 获取订单 详情 根据单号获取
 * 
 * @author Administrator
 *
 */
@Service
public class EfastService_getOrderDetailList extends EfastService {

	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取订单详情
	 */
	public void getOrderDetailList() {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("efastERP")) {
				int efastKey = enums.getKey();
				int isOpenFlag = jointService.getStateForJoint(efastKey);
				if (isOpenFlag == 0) {
					logger.info("未开启[中兴云购ERPO]对接！efastKey={}", efastKey);
					continue;
				}

				getOrderDetailMethod(efastKey);

			}
		}

	}

	private void getOrderDetailMethod(int efastKey) {

		Efast efast = getEfast(efastKey);
		List<ErpCwbtemp> datalist = efastDAO.getOrderSnList(efast.getErpEnum());

		if (datalist == null || datalist.size() == 0) {
			logger.info("当前没有待处理中兴ERP的订单列表");
			return;
		}

		for (ErpCwbtemp erp : datalist) {
			String successtime = DateTimeUtil.getNowTime();
			try {

				Map<String, String> params = buildRequestParams(erp, efast.getApp_key(), efast.getApp_secret(), efast.getApp_nick());

				String response = RestHttpServiceHanlder.sendHttptoServer(params, efast.getApp_url());

				logger.info("获取ERP订单详细信息-当前返回订单信息={}", response);

				String keywords = "\"code\":99";
				if (response.contains(keywords)) {
					logger.info("请求中兴云购订单不存在,cwb={}", erp.getOrder_sn());
					efastDAO.updateErpCwbtempById(erp.getErpid(), "3|订单不存在");
					continue;
				}

				OrderdetailShell orderdetailShell = JacksonMapper.getInstance().readValue(response, OrderdetailShell.class);

				Orderdetail orderdetail = orderdetailShell.getResp_data();

				List<Map<String, String>> orderlist = parseCwbArrByOrderDto(orderdetail);

				if (orderlist == null || orderlist.size() == 0) {
					logger.warn("中兴云购ERP-请求没有封装参数");
					continue;
				}

				long warehouseid = efast.getWarehouseid(); // 订单导入的库房Id
				dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(efast.getCustomerid()), B2cEnum.EfastERP.getMethod(), orderlist, warehouseid, true);
				logger.info("中兴云购ERP-订单详情下载成功,order_sn={}", erp.getOrder_sn());

			} catch (Exception e) {
				logger.error("中兴云购ERP-订单详情下载未知异常,order_sn=" + erp.getOrder_sn(), e);

			}

			efastDAO.updateErpCwbtempById(erp.getErpid(), successtime);
		}

	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(Orderdetail order) {
		List<Map<String, String>> cwbList = null;

		if (order != null) {
			cwbList = new ArrayList<Map<String, String>>();
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getOrder_sn());
			if (cwbOrder != null) {
				logger.warn("获取0中兴云购ERP0订单中含有重复数据cwb={}", order.getOrder_sn());
				return null;
			}

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("transcwb", order.getOrder_sn()); // 运单号存 订单号
			cwbMap.put("cwb", order.getInvoice_no()); // 订单号，快递单号
			cwbMap.put("consigneename", order.getConsignee());
			cwbMap.put("consigneeaddress", order.getAddress());
			cwbMap.put("consigneepostcode", order.getZipcode());
			cwbMap.put("consigneephone", order.getTel());
			cwbMap.put("consigneemobile", order.getMobile());
			// 配送方式代码 如 顺丰 cwbMap.put("shipping_name",order.getShipping_name());
			// 支付方式代码 如 支付宝，块钱等 cwbMap.put("_name",order.getPay_name());

			cwbMap.put("cwbordertypeid", "1");

			String otherinfo = "订单状态:" + order.getOrder_status() + ",发货状态:" + order.getShipping_status() + ",付款状态:" + (order.getPay_status() == 0 ? "未付款" : "已付款");

			String shippnames = "";
			String goods_barcode = "";
			double goods_price = 0;
			String goods_sn = "";

			List<OrderShipping> ordershippList = order.getOrders();
			if (ordershippList != null && ordershippList.size() > 0) {
				for (OrderShipping orderShipping : ordershippList) {
					shippnames += orderShipping.getGoods_name() + ",";
					goods_barcode += orderShipping.getGoods_barcode() + ",";
					goods_price = goods_price + orderShipping.getGoods_price();
					goods_sn += orderShipping.getGoods_sn() + ",";
				}
			}

			cwbMap.put("sendcarname", shippnames); // 发货货物名称
			cwbMap.put("caramount", order.getOrder_amount());
			cwbMap.put("receivablefee", order.getPay_status() == 0 ? order.getOrder_amount() : "0");
			cwbMap.put("remark1", "交易单号:" + (order.getDeal_code().length() > 100 ? order.getDeal_code().substring(0, 90) : order.getDeal_code())); // 交易单号
			cwbMap.put("remark2", "商品SKU:" + (goods_barcode.length() > 90 ? goods_barcode.substring(0, 90) : goods_barcode)); // 商品SKU
			cwbMap.put("remark3", otherinfo.length() > 100 ? otherinfo.substring(0, 90) : otherinfo);

			cwbList.add(cwbMap);

		}
		return cwbList;
	}

	private Map<String, String> buildRequestParams(ErpCwbtemp erp, String app_key, String app_secret, String app_nick) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", app_key); // 请求唯一标识
		params.put("app_secret", app_secret); // 请求密钥
		params.put("app_nick", app_nick); // 请求接口标识

		String feilds = "order_sn,deal_code,order_status,shipping_status,pay_status,consignee,address,zipcode,tel,mobile,shipping_name,"
				+ "pay_name,invoice_no,orders.goods_sn,orders.goods_name,orders.goods_number,orders.goods_price,orders.goods_barcode,shipping_fee,order_amount";

		params.put("app_act", "efast.trade.detail.get"); // 请求参数名
		params.put("oid", erp.getOrder_sn()); // 系统编号
		params.put("feilds", feilds); // 查询的字段
		params.put("type", "1"); // 系统订单号
		return params;
	}

}
