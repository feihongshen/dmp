package cn.explink.b2c.explink.core_down;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.EpaiAPIMarchal;
import cn.explink.b2c.explink.xmldto.EpaiAPIUnmarchal;
import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.explink.xmldto.OrderExportConditionDto;
import cn.explink.b2c.explink.xmldto.OrderExportResultDto;
import cn.explink.b2c.explink.xmldto.OrderListDto;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 系统之间的对接（下游）
 * 
 * @author Administrator
 *
 */
@Service
public class EpaiApiService_Download extends EpaiApiService {
	private Logger logger = LoggerFactory.getLogger(EpaiApiService_Download.class);

	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
/*
	*//**
	 * 订单下载入口
	 *//*
	public long downLoadOrders_controllers() {
		String customerids = "";
		String remark = "";
		long calcCount = 0;
		int check = 0;
		List<EpaiApi> epailist = epaiApiDAO.getEpaiApiList();
		if (epailist == null || epailist.size() == 0) {
			logger.warn("订单获取部分-当前没有配置系统对接设置！");
			return -1;
		}

		for (EpaiApi epai : epailist) {
			if (epai.getIsopenflag() == 1) {
				calcCount += downLoadingOrders(epai);
				customerids += epai.getCustomerid() + ",";
				check++;
			} else {
				logger.warn("订单获取部分-当前m没有开启对接！" + epai.getUserCode());
			}

		}
		if (check == 0) {
			return -1;
		}
		remark = !remark.isEmpty() && calcCount == 0 ? "未下载到数据" : remark;
		if (calcCount > 0) {
			remark = "下载完成";
		}
		//b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, DateTimeUtil.getNowTime(), calcCount, remark);
		return calcCount;

	}*/

	/**
	 * 订单数据下载接口（主动、定时）
	 */
	public long downLoadingOrders(EpaiApi epai) {

		try {

			OrderExportResultDto exportDto = buildOrderExportCondition(epai);
			if (!exportDto.getErrCode().equals(EpaiExpEmum.Success.getErrCode())) {
				logger.info("系统对接(下游)-请求获取订单接口发生异常!errCode={},errMsg={},userCode=" + epai.getUserCode(), exportDto.getErrCode(), exportDto.getErrMsg());
				return 0;
			}
			List<Map<String, String>> cwbOrderList = parseCwbArrByOrderDto(exportDto, epai); // 返回一个封装好的List
			if (cwbOrderList == null || cwbOrderList.size() == 0) {
				logger.warn("系统对接(下游)-请求0上游系统0没有下载到订单数据!errCode={},errMsg={},userCode=" + epai.getUserCode(), exportDto.getErrCode(), exportDto.getErrMsg());
				return 0;
			}

			dataImportService_B2c.Analizy_DataDealByB2c(epai.getCustomerid(), "epai", cwbOrderList, epai.getWarehouseid(), true);

			logger.info("0系统下游对接0下载订单信息调用数据导入接口-插入数据库成功!");
			return cwbOrderList.size();

		} catch (Exception e) {
			logger.error("系统对接(下游)-请求获取订单接口发生未知异常", e);
			return 0;
		}
	}

	/**
	 * 构建一个对接请求并返回DTO
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	private OrderExportResultDto buildOrderExportCondition(EpaiApi epai) throws Exception {
		OrderExportConditionDto condto = new OrderExportConditionDto();

		condto.setUserCode(epai.getUserCode());
		condto.setPageSize(epai.getPageSize());
		String requesttime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		condto.setRequestTime(requesttime);

		condto.setSign(MD5Util.md5(epai.getUserCode() + requesttime + epai.getPrivate_key()));

		String content = EpaiAPIMarchal.Marchal_getOrder(condto); // 通过jaxb状态对象对XML传输

		logger.info("订单下载-请求content={},userCode={}", content, epai.getUserCode());

		String responseContent = "";
		if (epai.getIspostflag() == 0) { // 使用数据流获取
			responseContent = RestTemplateHanlder.sendHttptoServer(content, epai.getGetOrder_url());
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("content", content);
			responseContent = RestTemplateHanlder.sendHttptoServer(params, epai.getGetOrder_url());
		}

		logger.info("订单下载-返回responseContent={},userCode={}", responseContent, epai.getUserCode());
		OrderExportResultDto exportDto = EpaiAPIUnmarchal.Unmarchal_getOrder(responseContent); // xml转化为bean对象

		return exportDto;
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	public List<Map<String, String>> parseCwbArrByOrderDto(OrderExportResultDto exportDto, EpaiApi epai) {
		List<Map<String, String>> cwbList = null;
		OrderListDto orderListDto = exportDto.getOrderListDto();
		if (orderListDto == null || orderListDto.getOrderDtoList() == null || orderListDto.getOrderDtoList().size() == 0) {
			return null;
		}
		List<OrderDto> orderList = orderListDto.getOrderDtoList();
		if (orderList != null && orderList.size() > 0) {
			cwbList = new ArrayList<Map<String, String>>();
			for (OrderDto order : orderList) {
				try {
					Map<String, String> cwbMap = new HashMap<String, String>();
					CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getCwb());
					if (cwbOrder != null) {
						logger.warn("获取0系统上游0订单中含有重复数据cwb={}", order.getCwb());
						dataImportDAO_B2c.updateIsB2cSuccessFlagFalseByCwbs(order.getCwb());
						continue;
					}

					cwbMap.put("cwb", order.getCwb());
					cwbMap.put("transcwb", order.getTranscwb() == null || order.getTranscwb().isEmpty() ? order.getCwb() : order.getTranscwb());
					cwbMap.put("consigneename", order.getConsigneename() == null ? "" : order.getConsigneename());
					cwbMap.put("consigneeaddress", order.getConsigneeaddress() == null ? "" : order.getConsigneeaddress());
					cwbMap.put("consigneepostcode", order.getConsigneepostcode() == null ? "" : order.getConsigneepostcode());
					cwbMap.put("consigneephone", order.getConsigneephone() == null ? "" : order.getConsigneephone());
					cwbMap.put("consigneemobile", order.getConsigneemobile() == null ? "" : order.getConsigneemobile());
					cwbMap.put("sendcarname", order.getSendcargoname() == null ? "" : order.getSendcargoname());
					cwbMap.put("backcargoname", order.getBackcargoname() == null ? "" : order.getBackcargoname());
					cwbMap.put("receivablefee", String.valueOf(order.getReceivablefee() == null ? "0" : order.getReceivablefee()));
					cwbMap.put("paybackfee", String.valueOf(order.getPaybackfee() == null ? "0" : order.getPaybackfee()));
					cwbMap.put("cargorealweight", String.valueOf(order.getCargorealweight() == null ? "0" : order.getCargorealweight()));

					cwbMap.put("caramount", String.valueOf(order.getCargoamount() == null ? "0" : order.getCargoamount()));
					cwbMap.put("cargotype", order.getCargotype() == null ? "" : order.getCargotype());
					cwbMap.put("cargosize", order.getCargosize() == null ? "" : order.getCargosize());
					cwbMap.put("sendcarnum", String.valueOf(order.getSendcargonum()));
					cwbMap.put("backcargonum", String.valueOf(order.getBackcargonum()));
					cwbMap.put("cwbordertypeid", String.valueOf(order.getCwbordertypeid()));
					cwbMap.put("cwbdelivertypeid", String.valueOf(order.getCwbdelivertypeid()));
					cwbMap.put("emaildate", order.getSendtime());
					cwbMap.put("customercommand", order.getCustomercommand());
					cwbMap.put("cwbremark", order.getOuttobranch() == null ? "" : order.getOuttobranch());
					cwbMap.put("paywayid", String.valueOf(order.getPaywayid()));
					cwbMap.put("customerwarehouseid", String.valueOf(getOrCreateCustomerWarehouse(epai, order.getWarehousename())));
					cwbMap.put("remark1", order.getRemark1());
					cwbMap.put("remark2", order.getRemark2());
					cwbMap.put("remark3", order.getRemark3());
					cwbMap.put("remark4", order.getRemark4());
					cwbMap.put("remark5", order.getRemark5());
					
					cwbMap.put("cwbprovince", order.getCwbprovince());
					cwbMap.put("cwbcity", order.getCwbcity());
					cwbMap.put("cwbcounty", order.getCwbcounty());

					cwbList.add(cwbMap);
				} catch (Exception e) {
					logger.error("下游获取订单数据构建异常cwb=" + order.getCwb(), e);
				}
			}
		}
		return cwbList;
	}

	public long getOrCreateCustomerWarehouse(EpaiApi epai, String warehousename) {
		long customerwarehouseid = 0;
		if(warehousename==null||warehousename.isEmpty()){
			return 0;
		}
		CustomWareHouse custwarehouse = customWareHouseDAO.getCustomWareHouseByName(warehousename, String.valueOf(epai.getCustomerid()));
		if (custwarehouse == null) {
			customerwarehouseid = customWareHouseDAO.creCustomerGetId(epai.getCustomerid(), warehousename);
		} else {
			customerwarehouseid = custwarehouse.getWarehouseid();
		}
		return customerwarehouseid;
	}

}
