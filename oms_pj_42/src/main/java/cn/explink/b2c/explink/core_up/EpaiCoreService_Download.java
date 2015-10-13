package cn.explink.b2c.explink.core_up;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.code_down.EpaiExpEmum;
import cn.explink.b2c.explink.xmldto.CoreMarchal;
import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.explink.xmldto.OrderExportConditionDto;
import cn.explink.b2c.explink.xmldto.OrderExportResultDto;
import cn.explink.b2c.explink.xmldto.OrderListDto;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.MD5.MD5Util;

/**
 * 系统之间的对接（上游）
 * 
 * @author Administrator
 *
 */
@Service
public class EpaiCoreService_Download {
	private Logger logger = LoggerFactory.getLogger(EpaiCoreService_Download.class);

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;
	@Autowired
	GetDmpDAO getDmpDAO;

	private Branch getBranch(long branchid) {
		return getDmpDAO.getNowBranch(branchid);
	}

	/**
	 * 接收请求订单导出接口
	 * 
	 * @throws JAXBException
	 * @throws Exception
	 */
	public String requestOrdersExport(OrderExportConditionDto orderExportConditionDto) throws JAXBException {
		OrderExportResultDto orderExportResultDto = new OrderExportResultDto();

		try {

			String userCode = orderExportConditionDto.getUserCode();
			String requestTime = orderExportConditionDto.getRequestTime();
			String sign = orderExportConditionDto.getSign();
			int pageSize = orderExportConditionDto.getPageSize();

			try {
				validatorRequestParams(userCode, requestTime, sign, pageSize, orderExportResultDto); // 校验基本参数,签名等信息
			} catch (RuntimeException e) {
				logger.error("验证下游请求业务逻辑异常", e);

				return CoreMarchal.Marchal_setOrders(orderExportResultDto);// 通过jaxb状态对象对XML传输
																			// 回传至下游订单详细信息
			}

			List<WarehouseToCommen> datalist = WarehouseCommenDAO.getCommenCwbListByCommonAndCount("'" + userCode + "'", pageSize);
			if (datalist == null || datalist.size() == 0) { // 无数据List返回null
															// 但是返回成功
				orderExportResultDto.setErrCode(EpaiExpEmum.Success.getErrCode());
				orderExportResultDto.setErrMsg(EpaiExpEmum.Success.getErrMsg());
				return CoreMarchal.Marchal_setOrders(orderExportResultDto);// 通过jaxb状态对象对XML传输
																			// 回传至下游订单详细信息
			}

			String requestCwbs = getRequestDMPCwbArrs(datalist); // 封装为-上游oms请求dmp的参数.

			logger.info("请求-上游OMS请求DMP参数cwbs={}", requestCwbs);

			String responseJson = getDmpDAO.getDMPOrdersByCwbs(requestCwbs); // 根据订单号批量
																				// 请求dmp，返回订单集合

			logger.info("返回-上游DMP返回OrderListDto={}", responseJson);

			if (responseJson == null) {
				orderExportResultDto.setErrCode(EpaiExpEmum.Success.getErrCode());
				orderExportResultDto.setErrMsg(EpaiExpEmum.Success.getErrMsg());
				logger.info("请求-上游OMS请求DMP发生未知异常，返回空,json={}", requestCwbs);
				return CoreMarchal.Marchal_setOrders(orderExportResultDto);// 通过jaxb状态对象对XML传输
																			// 回传至下游订单详细信息
			}

			List<OrderDto> respOrders = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<List<OrderDto>>() {
			}); // json格式转化为泛型集合
			addOtherParms(datalist, respOrders); // 追加其他参数
			addOtherParms(datalist, respOrders);

			orderExportResultDto.setErrCode(EpaiExpEmum.Success.getErrCode());
			orderExportResultDto.setErrMsg(EpaiExpEmum.Success.getErrMsg());
			OrderListDto orderListdto = new OrderListDto();
			orderListdto.setOrderDtoList(respOrders);
			orderExportResultDto.setOrderListDto(orderListdto);

			return CoreMarchal.Marchal_setOrders(orderExportResultDto);// 通过jaxb状态对象对XML传输
																		// 回传至下游订单详细信息

		} catch (Exception e) {

			orderExportResultDto.setErrCode(EpaiExpEmum.XiTongYiChang.getErrCode());
			orderExportResultDto.setErrMsg(EpaiExpEmum.XiTongYiChang.getErrMsg());
			logger.error("上游处理下载订单程序未知异常", e);
			return CoreMarchal.Marchal_setOrders(orderExportResultDto);// 通过jaxb状态对象对XML传输
																		// 回传至下游订单详细信息

		}

	}

	public static void main(String[] args) throws JAXBException {
		OrderExportResultDto orderExportResultDto = new OrderExportResultDto();
		orderExportResultDto.setErrCode("00");
		orderExportResultDto.setErrMsg("成功");

		OrderListDto orderListDto = new OrderListDto();
		List<OrderDto> olist = new ArrayList<OrderDto>();
		for (int i = 0; i < 3; i++) {

			OrderDto orderDto = new OrderDto();
			orderDto.setCwb("111111" + i);
			olist.add(orderDto);

		}
		orderListDto.setOrderDtoList(olist);
		orderExportResultDto.setOrderListDto(orderListDto);

		String xml = CoreMarchal.Marchal_setOrders(orderExportResultDto);
		System.out.println(xml);

	}

	/**
	 * 追加两个属性，出库站点，时间
	 * 
	 * @param datalist
	 * @param respOrders
	 */
	private void addOtherParms(List<WarehouseToCommen> datalist, List<OrderDto> respOrders) {
		if (respOrders != null && respOrders.size() > 0) {
			for (WarehouseToCommen toCommon : datalist) {
				for (OrderDto orderDto : respOrders) {
					if (orderDto.getCwb().equals(toCommon.getCwb())) {
						orderDto.setSendtime(toCommon.getCredate()); // 发货时间
						orderDto.setOuttobranch(getBranch(toCommon.getStartbranchid()).getBranchname()); // 出库承运商站点
					}
				}
			}
		}
	}

	private String getRequestDMPCwbArrs(List<WarehouseToCommen> datalist) {
		JSONArray jsonArr = new JSONArray();
		for (WarehouseToCommen common : datalist) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("cwb", common.getCwb());
			jsonArr.add(jsonobj);
		}
		String requestCwbs = jsonArr.toString();
		return requestCwbs;
	}

	private void validatorRequestParams(String userCode, String requestTime, String sign, int pageSize, OrderExportResultDto orderExportResultDto) throws RuntimeException {
		if (userCode == null || userCode.isEmpty()) {

			orderExportResultDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			orderExportResultDto.setErrMsg("请求参数userCode不能为空");
			throw new RuntimeException("请求参数userCode不能为空");
		}
		if (requestTime == null || requestTime.isEmpty()) {
			orderExportResultDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			orderExportResultDto.setErrMsg("请求参数requestTime不能为空");
			throw new RuntimeException("请求参数requestTime不能为空");
		}
		if (sign == null || sign.isEmpty()) {
			orderExportResultDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			orderExportResultDto.setErrMsg("请求参数sign不能为空");
			throw new RuntimeException("请求参数sign不能为空");
		}
		if (pageSize == 0) {
			orderExportResultDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			orderExportResultDto.setErrMsg("请求参数pageSize不能为零");
			throw new RuntimeException("请求参数pageSize不能为零");
		}

		Common common = getDMPCommon(userCode);
		if (common == null) {
			orderExportResultDto.setErrCode(EpaiExpEmum.YongHuBuCunZai.getErrCode());
			orderExportResultDto.setErrMsg("用户" + userCode + "不存在");
			throw new RuntimeException("用户" + userCode + "不存在");
		}
		if (common.getPageSize() < pageSize) {
			orderExportResultDto.setErrCode(EpaiExpEmum.YeWuYiChang.getErrCode());
			orderExportResultDto.setErrMsg("请求参数pageSize不能超过" + common.getPageSize() + "条");
			throw new RuntimeException("请求参数pageSize不能超过" + common.getPageSize() + "条");
		}
		String localsign = MD5Util.md5(userCode + requestTime + common.getPrivate_key());
		if (!localsign.equalsIgnoreCase(sign)) {
			orderExportResultDto.setErrCode(EpaiExpEmum.QianMingCuoWu.getErrCode());
			orderExportResultDto.setErrMsg("签名验证异常");
			throw new RuntimeException("签名验证异常");
		}
	}

	private Common getDMPCommon(String userCode) {
		for (Common common : this.getAllCommons()) {
			if (common.getCommonnumber().equals(userCode)) {
				return common;
			}
		}
		return null;
	}

	/**
	 * 获取dmp承运商设置表
	 * 
	 * @return
	 */
	public List<Common> getAllCommons() {
		return getDmpdao.getAllCommons();
	}

	/**
	 * 回传订单下载接口
	 * 
	 * @return
	 */
	public static String responseXML() {

		return null;
	}

}
