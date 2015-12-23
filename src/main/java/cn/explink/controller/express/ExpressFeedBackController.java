package cn.explink.controller.express;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.controller.DeliveryController;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressFeedBackDTO;
import cn.explink.domain.VO.express.ExpressFeedBackParamsVO;
import cn.explink.domain.VO.express.ExpressFeedBackView;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.enumutil.express.ExpressFeedBackExportFlagEnum;
import cn.explink.service.ExportService;
import cn.explink.service.express.ExpressFeedBackService;
import cn.explink.util.ExcelUtils;

/**
 * 揽件反馈
 * 
 * @author jiangyu 2015年7月30日
 *
 */
@Controller
@RequestMapping("/expressFeedback")
public class ExpressFeedBackController extends ExpressCommonController {
	private Logger logger = LoggerFactory.getLogger(DeliveryController.class);

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	PreOrderDao preOrderDao;

	@Autowired
	UserDAO userDAO;

	@Autowired
	HttpSession session;
	
	@Autowired
	ExportService exportService;
	
	@Autowired
	ExpressFeedBackService epressFeedBackService;

	@RequestMapping("/expressFeedbackIndex")
	public String expressFeedBackIndex(Model model, @RequestParam(value = "deliveryId", defaultValue = "0", required = false) long deliveryId) {
		User user = this.getSessionUser();
		Map<String, Object> params2PageView = epressFeedBackService.orgnizeInfo2PageView(user,deliveryId);
		model.addAttribute("deliverList", params2PageView.get("deliverList"));
		model.addAttribute("deliveryInCountMap", params2PageView.get("deliveryInCountMap"));
		model.addAttribute("useAudit", params2PageView.get("useAudit"));
		model.addAttribute("feedBackDTO", params2PageView.get("feedBackDTO"));
		model.addAttribute("deliveryxiaojianyuan", params2PageView.get("deliveryxiaojianyuan"));
		model.addAttribute("isGuiBanUseZanBuChuLi", params2PageView.get("isGuiBanUseZanBuChuLi"));
		model.addAttribute("summaryMap", params2PageView.get("summaryMap"));
		model.addAttribute("userroleid", user.getRoleid());
		model.addAttribute("isReAllocateExpress", params2PageView.get("isReAllocateExpress"));

		return "express/feedback/expressFeedBackView";
	}
	
	
	/**
	 * 反馈操作的统一入口
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getnowDeliveryState/{preOrderNo}")
	public String getnowDeliveryState(Model model, @PathVariable("preOrderNo") String preOrderNo) {
		Map<String, Object> feedBackInfo = epressFeedBackService.getFeedBackInfo(preOrderNo);
		
		model.addAttribute("feedBackView", feedBackInfo.get("feedBackView"));
		model.addAttribute("preOrder", feedBackInfo.get("preOrder"));
		model.addAttribute("pickFailedReason", feedBackInfo.get("pickFailedReason"));
		model.addAttribute("areaWrongReason", feedBackInfo.get("areaWrongReason"));
		model.addAttribute("pickWrongReason", feedBackInfo.get("pickWrongReason"));
		model.addAttribute("pickDelayReason", feedBackInfo.get("pickDelayReason"));
		this.session.setAttribute("executeState", Long.valueOf(((ExpressPreOrder)feedBackInfo.get("preOrder")).getExcuteState()));
		
		return "express/feedback/expressEditDeliveryState";
	}
	
	
	/**
	 * 修改反馈结果
	 * @param model
	 * @param params
	 * @return
	 */
	@RequestMapping("/editFeedBackState")
	@ResponseBody
	public String editDeliveryState(Model model,ExpressFeedBackParamsVO params) {
		this.logger.info("web-editDeliveryState-进入单票反馈,cwb={}", params.getPreOrderNoEdit());
		User user = this.getSessionUser();
		try {
			this.epressFeedBackService.executeFeedBackOperate(params,user);
		} catch (Exception ce) {
			return "{\"errorCode\":1,\"error\":\"" + ce.getMessage() + "\"}";
		}
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	/**
	 * 导出功能
	 * @param request
	 * @param response
	 * @param deliveryId 小件员
	 * @param exportFlag 导出的标识【未反馈|已反馈】
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/expressExportOpe")
	public void exportNotFeedBack(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "deliveryId", defaultValue = "0", required = false) long deliveryId,
			@RequestParam(value = "exportFlag", defaultValue = "feedBack", required = true) String exportFlag) {
		User user = this.getSessionUser();
		String[] cloumnName1 = new String[6]; // 导出的列名
		String[] cloumnName2 = new String[6]; // 导出的英文列名
		//组织导出的数据列
		this.exportService.setExpressFeedBackFileds(cloumnName1, cloumnName2);
		
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		
		String sheetName = "预订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "ExpressPreOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			
			Map<String, Object> map = epressFeedBackService.queryFeedBackDTOData(deliveryId,user);
			ExpressFeedBackDTO feedBackDTO  = (ExpressFeedBackDTO) map.get("feedBackDTO");
			List<ExpressFeedBackView> queryvViews = new ArrayList<ExpressFeedBackView>();
			
			if(ExpressFeedBackExportFlagEnum.UnFeedBack.getValue().equals(exportFlag)){
				//未反馈
				queryvViews = feedBackDTO.getUnFeedBackList();
				
			}else if(ExpressFeedBackExportFlagEnum.AlreadyFeedBack.getValue().equals(exportFlag)){
				//已反馈
				List<ExpressFeedBackView>  datas =(List<ExpressFeedBackView>) map.get("preOrderAndFeedBackState");
				datas.removeAll(feedBackDTO.getUnFeedBackList());
				queryvViews = datas;
			}
			//执行导出的逻辑
			final List<ExpressFeedBackView> views = queryvViews;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = ExpressFeedBackController.this.exportService.setExpressUnFeedBackObject(cloumnName3, views, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 校验系统中的运单号是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkTransNoIsRepeat")
	@ResponseBody
	public ExpressOpeAjaxResult checkTransNoIsRepeat(HttpServletRequest request) {
		String transNo = request.getParameter("transNo")==null?"":request.getParameter("transNo");
		ExpressOpeAjaxResult res = epressFeedBackService.checkTransNoIsRepeat(transNo);
		return res;
	}

}
