//package cn.explink.b2c.yihaodian.xmldto;
/////**
//// * NewHeight.com Inc.
//// * Copyright (c) 2008-2010 All Rights Reserved.
//// */
////package cn.explink.b2c.vipshop.dto;
////
////import net.sf.json.JSONObject;
////
////import org.apache.commons.logging.Log;
////import org.apache.commons.logging.LogFactory;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////
////
/////**
//// * 
//// * <pre>
//// * 承运商3PL系统对接
//// * </pre>
//// * 
//// * @since 2012-08
//// * @author songzhonghua
//// * @version $Id: TplOrderRestService, v 0.1 2012-8-7 15:28:53 songzhonghua Exp $ 
//// */
////@Service
////@Path("/tplOrder")
////public class TplOrderRestService extends BaseRestService {
////	private static final Log log = LogFactory.getLog(TplOrderRestService.class);
////	@Autowired
////	private TplOrderImpService tplOrderImpService;
////	@Autowired
////	private TplOrderResultImpService tplOrderResultImpService;
////	/**
////	 * 配送日志反馈到TMS（配送单、取件单）
////	 * @param orderPlan OrderPlanDto
////	 * @return
////	 */
////	@POST
////	@Path("/deliveryLog/add")
////	@Produces("application/xml")
////	public IFReturnDto updateDeliveryLog(OrderDeliveryLogDto orderDeliveryLogDto) {
////		log.info("REST /tplOrder/deliveryLog/add PARAM:" + JSONObject.fromObject(orderDeliveryLogDto).toString());
////		IFReturnDto result = new IFReturnDto();
////		try {
////			result = tplOrderImpService.updateDeliveryLog(orderDeliveryLogDto);
////		} catch (Exception e) {
////			result = exceptionHandlerTpl(e);
////		}
////		
////		log.info("REST /tplOrder/deliveryLog/add RESULT:" + JSONObject.fromObject(result).toString());
////		return result;
////	}
////	/**
////	 * 
////	 * <pre>
////	 * 订单信息获取（配送单、取件单）
////	 * </pre>
////	 *
////	 * @param conditionDto
////	 * @return
////	 */
//	@POST
//	@Path("/export")
//	@Produces("application/xml")
//	public OrderExportResultDto exportOrder(OrderExportConditionDto conditionDto){
//		log.info("REST /tplOrder/export PARAM:" + JSONObject.fromObject(conditionDto).toString());
//		OrderExportResultDto result = new OrderExportResultDto();
//		IFReturnDto ifReturnDto = new IFReturnDto();
//		try {
//			result = tplOrderImpService.exportOrder(conditionDto);
//		} catch (Exception e) {
//			ifReturnDto = exceptionHandlerTpl(e);
//			result.setErrCode(ifReturnDto.getErrCode());
//			result.setErrMsg(ifReturnDto.getErrMsg());
//		}
//		log.info("REST /tplOrder/export RESULT:" + JSONObject.fromObject(result).toString());
//		return result;
//	}
////	
////	/**
////	 * 
////	 * <pre>
////	 * 订单信息获取（配送单、取件单）
////	 * </pre>
////	 *
////	 * @param conditionDto
////	 * @return
////	 */
////	@GET
////	@Path("/exportByGet")
////	@Produces("application/xml")
////	public OrderExportResultDto exportByGet(OrderExportConditionDto conditionDto) {
////		log.info("REST /tplOrder/exportByGet PARAM:" + JSONObject.fromObject(conditionDto).toString());
////		OrderExportResultDto result = new OrderExportResultDto();
////		IFReturnDto ifReturnDto = new IFReturnDto();
////		try {
////			result = tplOrderImpService.exportOrder(conditionDto);
////		} catch (Exception e) {
////			ifReturnDto = exceptionHandlerTpl(e);
////			result.setErrCode(ifReturnDto.getErrCode());
////			result.setErrMsg(ifReturnDto.getErrMsg());
////		}
////		log.info("REST /tplOrder/exportByGet RESULT:" + JSONObject.fromObject(result).toString());
////		return result;
////	}
////	/**
////	 * 
////	 * <pre>
////	 * 订单信息导出成功回调（配送单、取件单）
////	 * </pre>
////	 *
////	 * @param orderExportCallbackDto
////	 * @return
////	 */
////	@POST
////	@Path("/exportCallback")
////	@Produces("application/xml")
////	public IFReturnDto exportCallback(OrderExportCallbackDto orderExportCallbackDto){
////		log.info("REST /tplOrder/exportCallback PARAM:" + JSONObject.fromObject(orderExportCallbackDto).toString());
////		IFReturnDto result = new IFReturnDto();
////		try{
////			result = tplOrderImpService.exportCallback(orderExportCallbackDto);
////		} catch (Exception e) {
////			result = exceptionHandlerTpl(e);
////		}
////		log.info("REST /tplOrder/exportCallback RESULT:" + JSONObject.fromObject(result).toString());
////		return result;
////	}
////	
////	/**
////	 * 配送结果反馈到TMS（配送单）
////	 * @param orderDeliveryResultDto OrderDeliveryResultDto
////	 * @return
////	 */
////	@POST
////	@Path("/deliveryResult/add")
////	@Produces("application/xml")
////	public IFReturnDto addDeliveryResult(OrderDeliveryResultDto orderDeliveryResultDto) {
////		log.info("REST /tplOrder/deliveryResult/add PARAM:" + JSONObject.fromObject(orderDeliveryResultDto).toString());
////		IFReturnDto result = new IFReturnDto();
////		try {
////			result = tplOrderResultImpService.add(orderDeliveryResultDto);
////		} catch (Exception e) {
////			result = exceptionHandlerTpl(e);
////		}
////		
////		log.info("REST /tplOrder/deliveryResult/add RESULT:" + JSONObject.fromObject(result).toString());
////		return result;
////	}
////	
////	/**
////	 * 付款信息修改反馈到TMS（配送单）
////	 * @param orderPlan OrderPlanDto
////	 * @return
////	 */
////	@POST
////	@Path("/deliveryResult/update")
////	@Produces("application/xml")
////	public IFReturnDto updateDeliveryResult(OrderDeliveryResultDto orderDeliveryResultDto) {
////		log.info("REST /tplOrder/deliveryResult/update PARAM:" + JSONObject.fromObject(orderDeliveryResultDto).toString());
////		IFReturnDto result = new IFReturnDto();
////		try {
////			result = tplOrderResultImpService.update(orderDeliveryResultDto);
////		} catch (Exception e) {
////			result = exceptionHandlerTpl(e);
////		}
////		
////		log.info("REST /tplOrder/deliveryResult/update RESULT:" + JSONObject.fromObject(result).toString());
////		return result;
////	}
////
// //}
