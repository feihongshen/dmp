package cn.explink.b2c.explink.core_up;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.explink.core.CoreService;
import cn.explink.b2c.explink.xmldto.CoreUnmarchal;
import cn.explink.b2c.explink.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.explink.xmldto.OrderExportConditionDto;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.util.Dom4jParseUtil;

/**
 * 上游
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/epaicore")
public class EpaiCoreController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EpaiCoreService_Download epaiCoreService_Download;
	@Autowired
	EpaiCoreService_Callback epaiCoreService_Callback;
	@Autowired
	EpaiCoreService_Receiver epaiCoreService_Receiver;
	@Autowired
	CoreService coreService;

	/**
	 * 初始化
	 * 
	 * @return
	 */
	@RequestMapping("/initCommonList")
	public @ResponseBody String initCommonList() {

		epaiCoreService_Receiver.initCommonList();
		coreService.initCommonList();
		logger.info("dmp承运商设置表发生改变，重新加载成功");

		return "SUCCESS";

	}

	/**
	 * 订单信息导出接口
	 * 
	 * @return
	 */
	@RequestMapping("/export")
	public @ResponseBody String export(HttpServletRequest request) {

		try {
			String requestXML = request.getParameter("content");
			if (requestXML == null) {
				InputStream input = new BufferedInputStream(request.getInputStream());
				requestXML = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			}

			logger.info("订单获取-下游请求信息={}", requestXML);

			OrderExportConditionDto orderExportConditionDto = CoreUnmarchal.Unmarchal_req_getOrder(requestXML);

			String response = epaiCoreService_Download.requestOrdersExport(orderExportConditionDto);

			logger.info("订单获取-返回下游信息={}", response);
			return response;
		} catch (Exception e) {
			logger.error("处理订单导出接口发生未知异常", e);
			return "系统异常" + e.getMessage();
		}

	}

	/**
	 * 订单下载成功回调接收
	 * 
	 * @return
	 */
	@RequestMapping("/exportCallBack")
	public @ResponseBody String exportCallBack(HttpServletRequest request) {

		try {

			String requestXML = request.getParameter("content");
			if (requestXML == null) {
				InputStream input = new BufferedInputStream(request.getInputStream());
				requestXML = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			}

			logger.info("订单导出成功回调-下游请求信息={}", requestXML);

			OrderExportCallbackDto orderExportCallbackDto = CoreUnmarchal.Unmarchal_rep_ExportCallBack(requestXML);

			return epaiCoreService_Callback.requestOrdersExportCallBack(orderExportCallbackDto);

		} catch (Exception e) {
			logger.error("处理订单导出成功回调接口发生未知异常", e);
			return "系统严重异常" + e.getMessage();
		}

	}

	/**
	 * 状态反馈
	 * 
	 * @return
	 */
	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request) {

		try {

			String requestXML = request.getParameter("content");
			if (requestXML == null) {
				InputStream input = new BufferedInputStream(request.getInputStream());
				requestXML = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			}

			logger.info("订单状态反馈-下游请求信息={}", requestXML);

			OrderFlowDto orderFlowDto = CoreUnmarchal.Unmarchal_rep_feedback(requestXML);

			return epaiCoreService_Receiver.cwbOrdersFeedback_Receiver(orderFlowDto);

		} catch (Exception e) {
			logger.error("处理订单状态反馈接口发生未知异常", e);
			return "系统严重异常" + e.getMessage();
		}

	}

}
