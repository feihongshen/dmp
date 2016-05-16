package cn.explink.controller.express2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pjbest.deliveryorder.enumeration.OrderStatusEnum;
import com.pjbest.deliveryorder.service.OmReserveOrderModel;
import com.vip.osp.core.exception.OspException;

import cn.explink.controller.express.ExpressCommonController;
import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.core.utils.JsonUtil;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express2.VO.ReserveOrderLogVo;
import cn.explink.domain.express2.VO.ReserveOrderPageVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.service.express2.ReserveOrderService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Tools;
import net.sf.json.JSONObject;

/**
 * 预约单 Controller
 * @date 2016年5月13日 上午11:04:29
 */
@Controller
@RequestMapping("/express2/reserveOrder")
public class ReserveOrderController extends ExpressCommonController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private ReserveOrderService reserveOrderService;
	
	/**
	 * 快递预约单查询
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/query")
	public String query(Model model) {
		// 查找本省的所有城市
		List<AdressVO> cities = this.reserveOrderService.getCities();
		model.addAttribute("cityList", cities);

		// 查找本省的所有站点
		List<Branch> branches = this.reserveOrderService.getBranches();
		model.addAttribute("branchList", branches);
		
		model.addAttribute("orderStatusList", OrderStatusEnum.values());
		return "express2/reserveOrder/query";
	}
	
	/**
	 * 快递预约单处理
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handle")
	public String handle(Model model) {

        //查找本省的所有城市
        List<AdressVO> cities = this.reserveOrderService.getCities();
        model.addAttribute("cityList",cities);

        //查找本省的所有站点
        List<Branch> branches = this.reserveOrderService.getBranches();
        model.addAttribute("branchList",branches);
        
		// 预约单状态
		model.addAttribute("orderStatusList", OrderStatusEnum.values());

        String provinceName = this.reserveOrderService.getAdressInfoByBranchid().getProvinceName();
        model.addAttribute("provinceName", provinceName);

        return "express2/reserveOrder/handle";
	}
	
	/**
	 * 快递预约单处理(站点)
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handleWarehouse")
	public String warehouseHandle() {
		return "express2/reserveOrder/warehouseHandle";
	}
	
	/**
	 * 查询列表
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @date 2016年5月13日 下午5:39:59
	 */
	@ResponseBody
	@RequestMapping("/queryList/{queryType}")
	public void queryList(HttpServletResponse response, @PathVariable("queryType") String queryType,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows,
			String reserveOrderNo, String appointTimeStart, String appointTimeEnd, String cnorProv, String cnorCity,
			String cnorMobile, String acceptOrg, String courier, String reserveOrderStatusList)
			throws JsonGenerationException, JsonMappingException, IOException {
		// 填充数据
		OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
		if(StringUtils.isNotBlank(reserveOrderNo)) {
			omReserveOrderModel.setReserveOrderNo(reserveOrderNo);
		}
		if(StringUtils.isNotBlank(appointTimeStart)) {
			omReserveOrderModel.setAppointTimeStart(appointTimeStart);
		}
		if(StringUtils.isNotBlank(appointTimeEnd)) {
			omReserveOrderModel.setAppointTimeEnd(appointTimeEnd);
		}
		if(StringUtils.isNotBlank(cnorProv)) {
			omReserveOrderModel.setCnorProv(cnorProv);
		}
		if(StringUtils.isNotBlank(cnorCity)) {
			omReserveOrderModel.setCnorCity(cnorCity);
		}
		if(StringUtils.isNotBlank(cnorMobile)) {
			omReserveOrderModel.setCnorMobile(cnorMobile);
		}
		if(StringUtils.isNotBlank(acceptOrg)) {
			omReserveOrderModel.setAcceptOrg(acceptOrg);
		}
		if(StringUtils.isNotBlank(courier)) {
			omReserveOrderModel.setCourier(courier);
		}
		if(StringUtils.isNotBlank(reserveOrderStatusList)) {
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusList);
		}
		ReserveOrderPageVo reserveOrderPageVo = this.reserveOrderService.getReserveOrderPage(omReserveOrderModel, page, rows);
		DataGridReturn dg = new DataGridReturn();
		dg.setRows(reserveOrderPageVo.getReserveOrderVoList());
		dg.setTotal(reserveOrderPageVo.getTotalRecord());
		Tools.outData2Page(Tools.obj2json(dg), response);
	}
	
	/**
	 * 导出到Excel
	 * @throws Exception 
	 * @date 2016年5月13日 下午5:39:59
	 */
	@ResponseBody
	@RequestMapping("/exportExcel/{queryType}")
	public void exportExcel(HttpServletResponse response, @PathVariable("queryType") String queryType,
			String reserveOrderNo, String appointTimeStart, String appointTimeEnd, String cnorProv, String cnorCity,
			String cnorMobile, String acceptOrg, String courier, String reserveOrderStatusList) throws Exception {
		// 填充数据
		OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
		if(StringUtils.isNotBlank(reserveOrderNo)) {
			omReserveOrderModel.setReserveOrderNo(reserveOrderNo);
		}
		if(StringUtils.isNotBlank(appointTimeStart)) {
			omReserveOrderModel.setAppointTimeStart(appointTimeStart);
		}
		if(StringUtils.isNotBlank(appointTimeEnd)) {
			omReserveOrderModel.setAppointTimeEnd(appointTimeEnd);
		}
		if(StringUtils.isNotBlank(cnorProv)) {
			omReserveOrderModel.setCnorProv(cnorProv);
		}
		if(StringUtils.isNotBlank(cnorCity)) {
			omReserveOrderModel.setCnorCity(cnorCity);
		}
		if(StringUtils.isNotBlank(cnorMobile)) {
			omReserveOrderModel.setCnorMobile(cnorMobile);
		}
		if(StringUtils.isNotBlank(acceptOrg)) {
			omReserveOrderModel.setAcceptOrg(acceptOrg);
		}
		if(StringUtils.isNotBlank(courier)) {
			omReserveOrderModel.setCourier(courier);
		}
		if(StringUtils.isNotBlank(reserveOrderStatusList)) {
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusList);
		}
		final List<ReserveOrderVo> reserveOrderList = this.reserveOrderService.getTotalReserveOrders(omReserveOrderModel);
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "快递预约单_" + df.format(new Date()) + ".xlsx"; // 文件名
		final String[] cloumnName = {"预约单号", "下单时间", "寄件人", "手机", "固话", "寄件地址", "预约上门时间", "预约单状态", "原因", "运单号", "站点", "快递员", "备注"};
		ExcelUtils excelUtil = new ExcelUtils() {

			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				Font font = sheet.getWorkbook().createFont();
				font.setFontName("宋体");
				font.setFontHeightInPoints((short) 10);
				style.setFont(font);
				//设置列的默认值
				for(int i = 0; i < cloumnName.length; i++) {
					sheet.setColumnWidth(i, 4000);
				}
				for (int i = 0; i < reserveOrderList.size(); i++) {
					ReserveOrderVo vo = reserveOrderList.get(i);
					Row row = sheet.createRow(i + 1);
					short colIndex = 0;
					
					Cell cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getReserveOrderNo());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getAppointTimeStr());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorName());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorMobile());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorTel());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorAddr());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRequireTimeStr());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getReserveOrderStatusName());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getReason());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getTransportNo());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getAcceptOrgName());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorName());
					
					cell = row.createCell(colIndex++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getCnorRemark());
				}
			}
		};
		excelUtil.excel(response, cloumnName, sheetName, fileName);
	}
	
    @RequestMapping("/getCountyByCity")
    @ResponseBody
    public JSONObject getCountyByCity(int cityId) {
        JSONObject obj = new JSONObject();
        List<AdressVO> countyList = this.reserveOrderService.getRegionsByCity(cityId);
        obj.put("countyList", countyList);
        return obj;
    }

    @RequestMapping("/getCourierByBranch")
    @ResponseBody
    public JSONObject getCourierByBranch(int branchId) {
        JSONObject obj = new JSONObject();
        List<User> courierList = this.reserveOrderService.getCourierByBranch(branchId);
        obj.put("courierList", courierList);
        return obj;
    }
	
	/**
	 * 查询预约单日志
	 * @param reserveOrderNo 预约单号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryReserveOrderLog")
	public String queryReserveOrderLog(String reserveOrderNo) {
		reserveOrderNo = StringUtils.trimToEmpty(reserveOrderNo);
		
		DataGridReturn dg = new DataGridReturn();
		dg.setRows(Collections.emptyList());
		dg.setTotal(0);
		
		if (reserveOrderNo.length() == 0) {
			logger.warn("queryReserveOrderLog->请输入预约单号");
			return JsonUtil.translateToJson(dg);
		}
		
		List<ReserveOrderLogVo> reserveOrderLogVoList = Collections.emptyList();
		try {
			//查询预约单日志
			reserveOrderLogVoList = reserveOrderService.queryReserveOrderLog(reserveOrderNo);
			if (reserveOrderLogVoList == null) {
				reserveOrderLogVoList = Collections.emptyList();
			}
			dg.setRows(reserveOrderLogVoList);
			dg.setTotal(reserveOrderLogVoList.size());
		} catch (OspException e) {
			logger.error(e.getMessage(), e);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			
		}
		//System.out.println("reserveOrderLogVoList:" + JsonUtil.translateToJson(reserveOrderLogVoList));
		return JsonUtil.translateToJson(dg);
	}

    @RequestMapping("/closeReserveOrder")
    @ResponseBody
    public JSONObject closeReserveOrder(@RequestParam(value = "reserveOrderNos", required = true) String reserveOrderNos,
                                  @RequestParam(value = "closeReason", required = true) String closeReason,
                                  HttpServletRequest request, HttpServletResponse response
    ) {

        JSONObject obj = new JSONObject();
        try {
            reserveOrderService.closeReserveOrder(reserveOrderNos.split(","), closeReason);
        } catch (OspException e) {
            obj.put("errorMsg", e.getReturnMessage());
        }
        return obj;
    }
    @RequestMapping("/returnToCentral")
    @ResponseBody
    public JSONObject returnToCentral(@RequestParam(value = "reserveOrderNos", required = true) String reserveOrderNos,
                                  @RequestParam(value = "returnReason", required = true) String returnReason,
                                  HttpServletRequest request, HttpServletResponse response
    ) {

        JSONObject obj = new JSONObject();
        try {
            reserveOrderService.returnToCentral(reserveOrderNos.split(","), returnReason);
        } catch (OspException e) {
            obj.put("errorMsg", e.getReturnMessage());
        }
        return obj;
    }
    @RequestMapping("/distributeBranch")
    @ResponseBody
    public JSONObject distributeBranch(@RequestParam(value = "reserveOrderNos", required = true) String reserveOrderNos,
                                       @RequestParam(value = "distributeBranch", required = true) int distributeBranch,
                                       @RequestParam(value = "distributeCourier", required = true) int distributeCourier,
                                       HttpServletRequest request, HttpServletResponse response
    ) {

        JSONObject obj = new JSONObject();

        String selectedTpsbranchCode = null;
        List<Branch> branches = reserveOrderService.getBranches();
        for (int i = 0; i < branches.size(); i++) {
            Branch branch = branches.get(i);
            if (branch.getBranchid() == distributeBranch){
                selectedTpsbranchCode = branch.getTpsbranchcode();
                break;
            }
        }

        if(selectedTpsbranchCode == null){
            obj.put("errorMsg", "站点TPSBranchCode不存在");
            return obj;
        }

        String selectedCourierName = null;

        List<User> courierList = this.reserveOrderService.getCourierByBranch(distributeBranch);
        for (int i = 0; i < courierList.size(); i++) {
            User courier = courierList.get(i);
            if (courier.getUserid() == distributeCourier){
                selectedCourierName = courier.getUsername();
            }
        }

        if(selectedCourierName == null){
            obj.put("errorMsg", "快递员不存在");
            return obj;
        }

        try {
            reserveOrderService.distributeBranch(reserveOrderNos.split(","), selectedTpsbranchCode, distributeCourier, selectedCourierName);
        } catch (OspException e) {
            obj.put("errorMsg", e.getReturnMessage());
        }
        return obj;
    }
}
