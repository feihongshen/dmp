package cn.explink.b2c.yihaodian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.yihaodian.xmldto.OrderExportCartonDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportConditionDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportDetailDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportHeaderDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportResultDto;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 下载订单数据
 *
 * @author Administrator
 *
 */
@Service
public class Yihaodian_DownloadCwb extends YihaodianService {
	private Logger logger = LoggerFactory.getLogger(Yihaodian_DownloadCwb.class);
	@Autowired
	RestTemplateClient restTemplate;

	/**
	 * 下载一号店订单信息
	 */
	public long DownLoadCwbDetailByYiHaoDian(int yhd_key, int loopcount,String url,int urlFlag,String userCode) {
		Yihaodian yihaodian = this.getYihaodian(yhd_key);
		try {
			OrderExportResultDto exportDto = this.BuildOrderExportCondition(yihaodian, url,userCode);
			if ((exportDto != null) && !exportDto.getErrCode().equals(YihaodianExpEmum.Success.getErrCode())) {
				this.logger.info("Invoke export cwb detail interface business exception!errCode={},errMsg={},yhd_key=" + yhd_key + ",loopcount=" + loopcount, exportDto.getErrCode(),
						exportDto.getErrMsg());
				return 0;
			}
			List<Map<String, String>> cwbOrderList = this.parseCwbArrByOrderDto(exportDto, yihaodian, urlFlag); // 返回一个封装好的List
			if (cwbOrderList == null) {
				this.logger.warn("请求一号店没有下载到订单数据!errCode={},errMsg={},yhd_key=" + yhd_key + ",loopcount=" + loopcount, exportDto.getErrCode(), exportDto.getErrMsg());
				return 0;
			}
			try {
				for (Map<String, String> data : cwbOrderList) {
					long customerid = Long.valueOf(data.get("customerid"));
					List<Map<String, String>> onelist = new ArrayList<Map<String, String>>();
					onelist.add(data);

					long warehouseid = yihaodian.getWarehouseid(); // 订单导入的库房Id
					this.dataImportInterface.Analizy_DataDealByB2c(customerid, B2cEnum.Yihaodian.getMethod(), onelist, warehouseid, true);
					this.logger.info("[一号店]下载订单信息调用数据导入接口-插入数据库成功!loopcount=" + loopcount);
				}
				return 1;
			} catch (Exception e) {
				this.logger.error("[一号店]调用数据导入接口异常!,订单List信息:" + cwbOrderList + "exptMessage=:" + e);
				e.printStackTrace();
				return 0;
			}
		} catch (Exception e) {
			this.logger.error("error info by request yihaodian download cwb detail interface!,loopcount=" + loopcount, e);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 构建一个对接请求并返回DTO
	 *
	 * @param yihaodian
	 * @return
	 */
	private OrderExportResultDto BuildOrderExportCondition(Yihaodian yihaodian,String url,String userCode) {
		OrderExportConditionDto condto = new OrderExportConditionDto();
		condto.setUserCode(userCode);
		condto.setPageSize(yihaodian.getExportCwb_pageSize());
		String nowtime = DateTimeUtil.getNowTime();
		condto.setRequestTime(nowtime);

		condto.setSign(MD5Util.md5(userCode + nowtime + yihaodian.getPrivate_key()));
		OrderExportResultDto exportDto = this.restTemplate.exportOrder(url, condto);
		return exportDto;
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(OrderExportResultDto exportDto, Yihaodian yhd, int urlFlag) {
		List<Map<String, String>> cwbList = null;
		List<OrderExportHeaderDto> orderHeaderList = exportDto.getOrderHeaderList();
		if ((orderHeaderList != null) && (orderHeaderList.size() > 0)) {
			cwbList = new ArrayList<Map<String, String>>();
			for (OrderExportHeaderDto order : orderHeaderList) {
				Map<String, String> cwbMap = new HashMap<String, String>();
				cwbMap.put("shipcwb", order.getOrderCode());
				cwbMap.put("cwb", order.getShipmentCode());
				cwbMap.put("consignoraddress", "1".equals(order.getOrderType()) ? order.getFromLocationName() : order.getToLocationName());
				cwbMap.put("receivablefee", order.getToCollectAmount() + "");
				cwbMap.put("paybackfee", order.getCollectedAmount() + "");
				cwbMap.put("caramount", order.getDeliveryProductAmount() + "");
				cwbMap.put("cargorealweight", order.getWeight() + "");
				cwbMap.put("sendcarnum", order.getCartonQuantity() + "");
				cwbMap.put("consigneename", order.getConsignee() + "");
				cwbMap.put("consigneemobile", order.getConsigneeTelephone());
				cwbMap.put("consigneephone", order.getConsigneeTelephone());
				cwbMap.put("consigneeaddress", order.getConsigneeAddress());
				cwbMap.put("consigneepostcode", order.getConsigneeZipcode());
				cwbMap.put("customercommand", (this.deliveryType(order.getDeliveryMode()) + "," + order.getExpectedDeliveryInfo()));
				cwbMap.put("cwbordertypeid", this.getCwbOrderTypeByYiHaoDian(order.getOrderType()));
				cwbMap.put("remark1", order.getGroupNo() == null ? "" : order.getGroupNo());
				// 订单明细集合
				cwbMap = this.parseCwbArrByOrderDetail(order, cwbMap);
				// 订单的包箱集合
				cwbMap = this.parseCwbArrByOrderCarton(order, cwbMap);

				// multi_shipcwb 存入 一票多箱 多个箱号逗号隔开，存入主表时插入 transcwb列 20130606
				String fromcompany = order.getFromCompany();
				if ((fromcompany != null) && fromcompany.contains("1")) {
					cwbMap.put("customerid", yhd.getCustomerids()); // 公司来源
				} else {
					if (yhd.getIsopenywaddressflag() == 1) { // 开启药网，不允许老地址下载到药网数据
						if (urlFlag == 0) { // 0表示老地址
							continue;
						}
					}
					cwbMap.put("customerid", yhd.getYwcustomerid()); // 公司来源
				}
				cwbMap.put("transcwb", cwbMap.get("multi_shipcwb"));

				cwbList.add(cwbMap);

				// logger.info("替换后的consigneeaddress={},consigneename={}",order.getConsigneeAddress().replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]",
				// "").replaceAll("\\?",
				// ""),order.getConsignee().replaceAll("\\?", ""));

			}
		}
		return cwbList;
	}

	private Map<String, String> parseCwbArrByOrderCarton(OrderExportHeaderDto order, Map<String, String> cwbMap) {
		if ((order.getCartonList() != null) && (order.getCartonList().size() > 0)) {

			String cartonNums = ""; // 箱号
			for (OrderExportCartonDto carton : order.getCartonList()) {
				cartonNums += carton.getCode() + ",";
			}
			cwbMap.put("remark3", "");
			cwbMap.put("remark4", "");
			cwbMap.put("remark5", "");
			cwbMap.put("multi_shipcwb", cartonNums.substring(0, cartonNums.length() - 1));
		}
		return cwbMap;
	}

	private Map<String, String> parseCwbArrByOrderDetail(OrderExportHeaderDto order, Map<String, String> cwbMap) {
		// 订单明细集合
		if ((order.getOrderDetailList() != null) && (order.getOrderDetailList().size() > 0)) {
			String goodsName = ""; // 商品名称
			String goodsDesc = ""; // 商品描述
			String cwbremark = ""; // 退货备注
			int i = 0;
			for (OrderExportDetailDto cwbdetail : order.getOrderDetailList()) {
				goodsName += cwbdetail.getGoodsName() + ",";
				goodsDesc += "第<" + (++i) + ">箱：" + (cwbdetail.getGoodsDesc() + ",数量:" + cwbdetail.getQuantity() + ",单位：" + cwbdetail.getUom() + ",货值:" + cwbdetail.getAmount() + ";");
				cwbremark += "第<" + (++i) + ">箱：" + cwbdetail.getReturnReasonDesc() + ";";
			}
			cwbMap.put("sendcarname", goodsName.substring(0, goodsName.length() - 1));
			// cwbMap.put("remark1",goodsDesc.length()>100?goodsDesc.substring(0,100):goodsDesc);
			cwbMap.put("cwbremark", "");
		}
		return cwbMap;
	}

	private String getCwbOrderTypeByYiHaoDian(String orderType) {
		if (orderType.equals(YiHaoDianOrderTypeEnum.PeiSong.getCode() + "")) {
			return CwbOrderTypeIdEnum.Peisong.getValue() + "";
		}
		if (orderType.equals(YiHaoDianOrderTypeEnum.HuanHuoPeiSong.getCode() + "")) {
			return CwbOrderTypeIdEnum.Peisong.getValue() + "";
		}
		if (orderType.equals(YiHaoDianOrderTypeEnum.HuanhuoQuJian.getCode() + "")) {
			return CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "";
		}
		if (orderType.equals(YiHaoDianOrderTypeEnum.TuiHuoQuJian.getCode() + "")) {
			return CwbOrderTypeIdEnum.Shangmentui.getValue() + "";
		}

		return CwbOrderTypeIdEnum.Peisong.getValue() + "";
	}

	/**
	 * 配送方式
	 *
	 * @return
	 */
	private String deliveryType(String deliveryMode) {
		if (deliveryMode.equals("10001")) {
			return "普通快递";
		}
		if (deliveryMode.equals("10002")) {
			return "指定收货日期";
		}
		if (deliveryMode.equals("10003")) {
			return "一日三送";
		}
		if (deliveryMode.equals("10004")) {
			return "半日达";
		}
		return "";
	}

}
