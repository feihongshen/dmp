package cn.explink.controller.express2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pjbest.deliveryorder.service.OmReserveOrderModel;
import com.vip.osp.core.exception.OspException;
import com.vip.tps.base.service.SbCodeDefModel;
import com.vip.tps.base.service.SbCodeTypeService;
import com.vip.tps.base.service.SbCodeTypeServiceHelper;

import cn.explink.controller.ExplinkResponse;
import cn.explink.controller.express.ExpressCommonController;
import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.core.utils.JsonUtil;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express2.VO.ReserveOrderEditVo;
import cn.explink.domain.express2.VO.ReserveOrderLogVo;
import cn.explink.domain.express2.VO.ReserveOrderPageVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.enumutil.ReserveOrderQueryTypeEnum;
import cn.explink.enumutil.express2.ReserveOrderStatusClassifyEnum;
import cn.explink.enumutil.express2.ReserveOrderDmpStatusEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.service.BranchService;
import cn.explink.service.UserService;
import cn.explink.service.express2.ReserveOrderService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.ResourceBundleUtil;
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
	
	@Resource
	private BranchService branchService;

	@Resource
    private UserService userService;
	
    private static final String RESERVE_EXCEPTION_REASON = "RESERVE_EXCEPTION_REASON";
    private static final String RESERVE_RETENTION_REASON = "RESERVE_RETENTION_REASON";
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
		
		// 预约单状态
        ReserveOrderDmpStatusEnum[] reserveOrderStatusList;
        if(this.isWarehouseMaster() || this.isCourier()) {
        	reserveOrderStatusList = ReserveOrderStatusClassifyEnum.QUERY_BY_WAREHOUSE_MASTER.toArray();
        } else {
        	reserveOrderStatusList = ReserveOrderStatusClassifyEnum.QUERY_BY_CUSTOM_SERVICE.toArray();
        }
        model.addAttribute("reserveOrderStatusList", reserveOrderStatusList);
		
		// 是否是站长
		model.addAttribute("isWarehouseMaster", this.isWarehouseMaster());
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
        ReserveOrderDmpStatusEnum[] reserveOrderStatusList =  ReserveOrderStatusClassifyEnum.HANDLE_BY_CUSTOM_SERVICE.toArray();
        
        model.addAttribute("reserveOrderStatusList", reserveOrderStatusList);

		if (!CollectionUtils.isEmpty(cities)) {
			String provinceName = reserveOrderService.getCurProvince(cities.get(0).getId()).getName();
	        model.addAttribute("provinceName", provinceName);
		}

        return "express2/reserveOrder/handle";
	}
	
	/**
	 * 快递预约单处理(站点)
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handleWarehouse")
	public String handleWarehouse(Model model) {
        List<User> courierList = this.reserveOrderService.getCourierByBranch(Long.valueOf(this.getSessionUser().getBranchid()).intValue());
        model.addAttribute("courierList",courierList);
		List<ReserveOrderService.PJReserverOrderOperationCode> feedbackOptCodes = new ArrayList<ReserveOrderService.PJReserverOrderOperationCode>();

        feedbackOptCodes.add(ReserveOrderService.PJReserverOrderOperationCode.LanJianShiBaiTuiHui);
        feedbackOptCodes.add(ReserveOrderService.PJReserverOrderOperationCode.FanKuiJiLiu);
        feedbackOptCodes.add(ReserveOrderService.PJReserverOrderOperationCode.LanJianShiBai);

        model.addAttribute("feedbackOptCodes",feedbackOptCodes);
        
        model.addAttribute("reserveOrderStatusList", ReserveOrderStatusClassifyEnum.WAREHOUSE_HANDLE.toArray());

        SbCodeTypeService sbCodeTypeService = new SbCodeTypeServiceHelper.SbCodeTypeServiceClient();

        try {
            model.addAttribute("reverseExceptionReason", sbCodeTypeService.findCodeDefList(RESERVE_EXCEPTION_REASON));
            model.addAttribute("reverseRetentionReason", sbCodeTypeService.findCodeDefList(RESERVE_RETENTION_REASON));
        } catch (OspException e) {
            logger.error(e.getMessage(), e);
        }

        // 查找本省的所有城市
 		List<AdressVO> cities = this.reserveOrderService.getCities();
 		model.addAttribute("cityList", cities);
        
        if (!CollectionUtils.isEmpty(cities)) {
			String provinceName = reserveOrderService.getCurProvince(cities.get(0).getId()).getName();
	        model.addAttribute("provinceName", provinceName);
		}
        
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
			String reserveOrderNo, String appointTimeStart, String appointTimeEnd, Integer cnorCity, Integer cnorRegion,
			String cnorMobile, String acceptOrg, Long courier, String reserveOrderStatusList)
			throws JsonGenerationException, JsonMappingException, IOException {
		// 填充数据
		OmReserveOrderModel omReserveOrderModel = this.reserveOrderService.getReserveOrderAddress(cnorCity, cnorRegion);
		if(StringUtils.isNotBlank(reserveOrderNo)) {
			omReserveOrderModel.setReserveOrderNo(StringUtils.strip(reserveOrderNo));
		}
		if(StringUtils.isNotBlank(appointTimeStart)) {
			omReserveOrderModel.setAppointTimeStart(appointTimeStart);
		}
		if(StringUtils.isNotBlank(appointTimeEnd)) {
			omReserveOrderModel.setAppointTimeEnd(appointTimeEnd);
		}
		if(StringUtils.isNotBlank(cnorMobile)) {
			omReserveOrderModel.setCnorMobile(StringUtils.strip(cnorMobile));
		}
		if(StringUtils.isNotBlank(acceptOrg)) {
//			omReserveOrderModel.setAcceptOrg(acceptOrg);
            //需求更改，tps期望拿到carrierSiteCode
			omReserveOrderModel.setCarrierSiteCode(acceptOrg);
		}
		if(courier != null) {
			User user = this.userService.getUserByUserid(courier);
			omReserveOrderModel.setCourier(user.getUsername());
		}
		if(StringUtils.isNotBlank(reserveOrderStatusList)) {
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusList);
		} else {
			String reserveOrderStatusStr = null;
			if(StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.QUERY.getValue())) {
				if(this.isWarehouseMaster()) {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.QUERY_BY_WAREHOUSE_MASTER.toString();
				} else {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.QUERY_BY_CUSTOM_SERVICE.toString();
				}
			} else if(StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.HANDLE.getValue())) {
//				if(this.isWarehouseMaster()) {
//					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.HANDLE_BY_WAREHOUSE_MASTER.toString();
//				} else {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.HANDLE_BY_CUSTOM_SERVICE.toString();
//				}
			} else {
				reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.WAREHOUSE_HANDLE.toString();
			}
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusStr);
		}
		//默认省编号
		String carrierCode = ResourceBundleUtil.expressCarrierCode;
        omReserveOrderModel.setCarrierCode(carrierCode);
		boolean isQuery = true;
        if (this.isWarehouseMaster() || this.isCourier()) {
            //站长或小件员只能看到本站点的
			Branch branch = this.branchService.getBranchByBranchid(this.getSessionUser().getBranchid());
//            omReserveOrderModel.setAcceptOrg(branch.getTpsbranchcode());
            omReserveOrderModel.setCarrierSiteCode(branch.getTpsbranchcode());
        } else if(this.isCustomService() || this.isAdmin()) {
//            if (StringUtils.isNotBlank(acceptOrg)) {
//                omReserveOrderModel.setCarrierSiteCode(acceptOrg);
//            }
        } else {
			isQuery = false;
		}
		ReserveOrderPageVo reserveOrderPageVo;
		if(isQuery) {
			reserveOrderPageVo = this.reserveOrderService.getReserveOrderPage(omReserveOrderModel, page, rows);
		} else {
			reserveOrderPageVo = new ReserveOrderPageVo();
		}

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
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows,
			String reserveOrderNo, String appointTimeStart, String appointTimeEnd, Integer cnorCity, Integer cnorRegion,
			String cnorMobile, String acceptOrg, Long courier, String reserveOrderStatusList) throws Exception {
		// 填充数据
		OmReserveOrderModel omReserveOrderModel = this.reserveOrderService.getReserveOrderAddress(cnorCity, cnorRegion);
		if(StringUtils.isNotBlank(reserveOrderNo)) {
			omReserveOrderModel.setReserveOrderNo(reserveOrderNo);
		}
		if(StringUtils.isNotBlank(appointTimeStart)) {
			omReserveOrderModel.setAppointTimeStart(appointTimeStart);
		}
		if(StringUtils.isNotBlank(appointTimeEnd)) {
			omReserveOrderModel.setAppointTimeEnd(appointTimeEnd);
		}
		if(StringUtils.isNotBlank(cnorMobile)) {
			omReserveOrderModel.setCnorMobile(cnorMobile);
		}
        if(StringUtils.isNotBlank(acceptOrg)) {
//			omReserveOrderModel.setAcceptOrg(acceptOrg);
            //需求更改，tps期望拿到carrierSiteCode
            omReserveOrderModel.setCarrierSiteCode(acceptOrg);
        }
		if(courier != null) {
			User user = this.userService.getUserByUserid(courier);
			omReserveOrderModel.setCourier(user.getUsername());
		}
		if(StringUtils.isNotBlank(reserveOrderStatusList)) {
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusList);
		} else {
			String reserveOrderStatusStr = null;
			if(StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.QUERY.getValue())) {
				if(this.isWarehouseMaster()) {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.QUERY_BY_WAREHOUSE_MASTER.toString();
				} else {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.QUERY_BY_CUSTOM_SERVICE.toString();
				}
			} else if(StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.HANDLE.getValue())) {
//				if(this.isWarehouseMaster()) {
//					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.HANDLE_BY_WAREHOUSE_MASTER.toString();
//				} else {
					reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.HANDLE_BY_CUSTOM_SERVICE.toString();
//				}
			} else {
				reserveOrderStatusStr = ReserveOrderStatusClassifyEnum.WAREHOUSE_HANDLE.toString();
			}
			omReserveOrderModel.setReserveOrderStatusList(reserveOrderStatusStr);
		}
		//默认省编号
        String carrierCode = ResourceBundleUtil.expressCarrierCode;
        omReserveOrderModel.setCarrierCode(carrierCode);
		boolean isQuery = true;
		if (this.isWarehouseMaster() || this.isCourier()) {
			//站长只能看到本站点的
			Branch branch = this.branchService.getBranchByBranchid(this.getSessionUser().getBranchid());
//			omReserveOrderModel.setAcceptOrg(branch.getTpsbranchcode());
            omReserveOrderModel.setCarrierSiteCode(branch.getTpsbranchcode());
        } else if (this.isCustomService() || this.isAdmin()) {
//            if (StringUtils.isNotBlank(acceptOrg)) {
//                omReserveOrderModel.setAcceptOrg(acceptOrg);
//                Branch branch = this.branchService.getBranchByBranchid(Integer.parseInt(acceptOrg));
//                omReserveOrderModel.setAcceptOrg(branch.getTpsbranchcode());
//                omReserveOrderModel.setCarrierSiteCode(acceptOrg);
//            }
        } else {
            isQuery = false;
		}
		final List<ReserveOrderVo> reserveOrderList;
		if(isQuery) {
			reserveOrderList = this.reserveOrderService.getTotalReserveOrders(omReserveOrderModel);
		} else {
			reserveOrderList = new ArrayList<ReserveOrderVo>();
		}
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

    /**
     * 根据城市返回区
     * @param cityId
     * @return
     */
    @RequestMapping("/getCountyByCity")
    @ResponseBody
    public JSONObject getCountyByCity(int cityId) {
        JSONObject obj = new JSONObject();
        List<AdressVO> countyList = this.reserveOrderService.getRegionsByCity(cityId);
        obj.put("countyList", countyList);
        return obj;
    }

    /**
     * 根据站点查找派送员
     * @param branchId
     * @return jsonObject
     */
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
	 * @return jsonObject
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

    /**
     * 关闭预约单
     * @param reserveOrderVos
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/closeReserveOrder")
    @ResponseBody
    public JSONObject closeReserveOrder(@RequestBody ReserveOrderVo[] reserveOrderVos,
                                  HttpServletRequest request, HttpServletResponse response
    ) {
        final String logPrefix = "closeReserveOrder->";

        JSONObject obj = new JSONObject();
        List<OmReserveOrderModel> omReserveOrderModels = null;
        if (reserveOrderVos.length > 0) {

            omReserveOrderModels = new ArrayList<OmReserveOrderModel>();
            for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
                logger.info("{} reserveOrder: {}", logPrefix, JsonUtil.translateToJson(reserveOrderVo));

                OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
                omReserveOrderModel.setReserveOrderNo(reserveOrderVo.getReserveOrderNo());
                omReserveOrderModel.setRecordVersion(reserveOrderVo.getRecordVersion());
//                omReserveOrderModel.setReason(reserveOrderVo.getReason());
                //关闭原因写到备注
                omReserveOrderModel.setRemark(reserveOrderVo.getReason());
                omReserveOrderModels.add(omReserveOrderModel);
            }
            List<String> errMsg = new ArrayList<String>();
            reserveOrderService.closeReserveOrder(omReserveOrderModels, errMsg);
            buildErrorMsg(obj, errMsg);
        }
        return obj;
    }


    /**退回操作
     * @param reserveOrderVos
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/returnToCentral")
    @ResponseBody
    public JSONObject returnToCentral(@RequestBody ReserveOrderVo[] reserveOrderVos,
                                  HttpServletRequest request, HttpServletResponse response
    ) {
        final String logPrefix = "returnToCentral->";

        JSONObject obj = new JSONObject();
        List<OmReserveOrderModel> omReserveOrderModels = null;
        List<String> errMsg = new ArrayList<String>();

        if (!validateReturnToCentral(reserveOrderVos, errMsg)) {
            buildErrorMsg(obj, errMsg);
            return obj;
        }

        int operateType = reserveOrderVos[0].getOperateType();
        logger.info("{} operateType: {}", logPrefix, operateType);

        omReserveOrderModels = new ArrayList<OmReserveOrderModel>();
        for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
            logger.info("{} reserveOrder: {}", logPrefix, JsonUtil.translateToJson(reserveOrderVo));

            OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
            omReserveOrderModel.setReserveOrderNo(reserveOrderVo.getReserveOrderNo());
            omReserveOrderModel.setRecordVersion(reserveOrderVo.getRecordVersion());
//                omReserveOrderModel.setReason(reserveOrderVo.getReason());
            //关闭原因写到备注
            omReserveOrderModel.setRemark(reserveOrderVo.getReason());
            omReserveOrderModels.add(omReserveOrderModel);
        }

        reserveOrderService.returnToCentral(omReserveOrderModels, operateType, errMsg);
        buildErrorMsg(obj, errMsg);
        return obj;
    }

    private boolean validateReturnToCentral(ReserveOrderVo[] reserveOrderVos, List<String> errMsg) {
        boolean isPass = true;

        if (reserveOrderVos == null || reserveOrderVos.length < 1) {
            errMsg.add("请选择至少一条预约单！");
            isPass = false;
        } else {
            for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
                if (ReserveOrderService.PJReserverOrderOperationCode.ShengGongSiChaoQu.getValue() == reserveOrderVo.getOperateType()
                        && ReserveOrderStatusEnum.HadAllocationStation.getIndex().byteValue() == reserveOrderVo.getReserveOrderStatus()) {
                    errMsg.add("{"+reserveOrderVo.getReserveOrderNo()+"} 已分配站点,不能退回总部!");
                    isPass = false;
                }
            }
        }
        return isPass;
    }

    /**
     * 分配快递员
     * @param reserveOrderVos
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/distributeBranch/{queryType}")
    @ResponseBody
	public JSONObject distributeBranch(@RequestBody ReserveOrderVo[] reserveOrderVos,
			@PathVariable("queryType") String queryType, HttpServletRequest request, HttpServletResponse response) {
        final String logPrefix = "distributeBranch->";

        JSONObject obj = new JSONObject();
        List<OmReserveOrderModel> omReserveOrderModels = null;

        List<String> errMsg = new ArrayList<String>();
        if (!validateDistributeBranch(reserveOrderVos, queryType, errMsg)) {
            buildErrorMsg(obj, errMsg);
            return obj;
        }

        String selectedBranch = reserveOrderVos[0].getAcceptOrg();
        String selectedCourier = reserveOrderVos[0].getCourier();

        String selectedTpsbranchCode = null;
        //选择站点默认值是登录人员自己的站点
        Long distributeBranch = this.getSessionUser().getBranchid();

        String selectedCourierName = null;

        String courierLoginName = null;

        if (StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.HANDLE.getValue())) {

            if (StringUtils.isNotBlank(selectedBranch) && NumberUtils.isNumber(selectedBranch)) {
                logger.info("{} 省点分配站点和快递员", logPrefix);
                distributeBranch = Long.parseLong(selectedBranch);
                //找TPS CODE
                List<Branch> branches = reserveOrderService.getBranches();
                for (int i = 0; i < branches.size(); i++) {
                    Branch branch = branches.get(i);
                    if (branch.getBranchid() == distributeBranch) {
                        selectedTpsbranchCode = branch.getTpsbranchcode();
                        logger.info("{} selectedTpsbranchCode: {}", logPrefix, selectedTpsbranchCode);
                        break;
                    }
                }
                if (selectedTpsbranchCode == null) {
                    obj.put("errorMsg", "站点TPSBranchCode不存在");
                    logger.error("{} 站点TPSBranchCode不存在", logPrefix);
                    return obj;
                }
            } else {
                obj.put("errorMsg", "请选择站点！");
                logger.error("{} 没有选择站点", logPrefix);
                return obj;
            }
        } else if (StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.WAREHOUSE_HANDLE.getValue())) {
            if (StringUtils.isNotBlank(selectedCourier) && NumberUtils.isNumber(selectedCourier)) {
                Long distributeCourier = Long.parseLong(selectedCourier);
                //找快递员名字
                List<User> courierList = this.reserveOrderService.getCourierByBranch(distributeBranch.intValue());
                for (int i = 0; i < courierList.size(); i++) {
                    User courier = courierList.get(i);
                    if (courier.getUserid() == distributeCourier) {
                        selectedCourierName = courier.getRealname();
                        courierLoginName = courier.getUsername();
                        logger.info("{} selectedCourierName: {}", logPrefix, selectedCourierName);
                    }
                }
                if (selectedCourierName == null) {
                    obj.put("errorMsg", "快递员不存在");
                    logger.error("{} 快递员不存在", logPrefix);
                    return obj;
                }
            } else {
                obj.put("errorMsg", "请选择小件员！");
                logger.error("{} 没有选择小件员", logPrefix);
                return obj;
            }
        }

        omReserveOrderModels = new ArrayList<OmReserveOrderModel>();
        for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
            logger.info("{} reserveOrder: {}", logPrefix, JsonUtil.translateToJson(reserveOrderVo));
            OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
            omReserveOrderModel.setReserveOrderNo(reserveOrderVo.getReserveOrderNo());
            omReserveOrderModel.setRecordVersion(reserveOrderVo.getRecordVersion());
            if (StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.HANDLE.getValue())) {
                omReserveOrderModel.setAcceptOrg(selectedTpsbranchCode);
                omReserveOrderModel.setReserveOrderStatus(Integer.valueOf(ReserveOrderService.PJReserverOrderOperationCode.YiFenPeiZhanDian.getValue()).byteValue());
            } else if (StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.WAREHOUSE_HANDLE.getValue())) {
                omReserveOrderModel.setCourier(courierLoginName);
                omReserveOrderModel.setCourierName(selectedCourierName);
                omReserveOrderModel.setReserveOrderStatus(Integer.valueOf(ReserveOrderService.PJReserverOrderOperationCode.YiLanJianFenPei.getValue()).byteValue());
            }
            omReserveOrderModels.add(omReserveOrderModel);
        }

        reserveOrderService.distributeBranch(omReserveOrderModels, errMsg);
        buildErrorMsg(obj, errMsg);
        return obj;
    }

    private boolean validateDistributeBranch(ReserveOrderVo[] reserveOrderVos, String queryType, List<String> errMsg) {
        boolean isPass = true;

        if (reserveOrderVos == null || reserveOrderVos.length < 1) {
            errMsg.add("请选择至少一条预约单！");
            isPass = false;
        } else {
            if (StringUtils.equals(queryType, ReserveOrderQueryTypeEnum.HANDLE.getValue())) {
                for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
                    if (!(
                            ReserveOrderStatusEnum.HadAllocationPro.getIndex().byteValue() == reserveOrderVo.getReserveOrderStatus()
                                    ||  ReserveOrderStatusEnum.HadAllocationStation.getIndex().byteValue() == reserveOrderVo.getReserveOrderStatus()
                                    ||  ReserveOrderStatusEnum.HaveStationOutZone.getIndex().byteValue() == reserveOrderVo.getReserveOrderStatus()
                        )){
                        errMsg.add("{"+reserveOrderVo.getReserveOrderNo()+"} 只允许对状态为：已分配省公司、已分配站点、站点超区的预约单进行分配站点操作！");
                        isPass = false;
                    }
                }
            }
        }
        return isPass;
    }


    /**
     * 反馈
     * @param reserveOrderVos
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/feedback")
    @ResponseBody
    public JSONObject feedback(@RequestBody ReserveOrderVo[] reserveOrderVos, HttpServletRequest request, HttpServletResponse response) {
        final String logPrefix = "feedback->";

        JSONObject obj = new JSONObject();
        List<OmReserveOrderModel> omReserveOrderModels = null;
        List<String> errMsg = new ArrayList<String>();

//        if (!validateFeedback(reserveOrderVos, errMsg)) {
//            buildErrorMsg(obj, errMsg);
//            return obj;
//        }

        String reason4Feedback = reserveOrderVos[0].getReason();
        logger.info("{} reason4Feedback {}", logPrefix, reason4Feedback);

        int operateType = reserveOrderVos[0].getOperateType();
        logger.info("{} operateType {}", logPrefix, operateType);

        SbCodeTypeService sbCodeTypeService = new SbCodeTypeServiceHelper.SbCodeTypeServiceClient();

        String displayValue = null;
        try {
            List<SbCodeDefModel> reasons = null ;
            if (ReserveOrderService.PJReserverOrderOperationCode.LanJianShiBai.getValue() == operateType) {
                reasons = sbCodeTypeService.findCodeDefList(RESERVE_EXCEPTION_REASON);
            } else if (ReserveOrderService.PJReserverOrderOperationCode.FanKuiJiLiu.getValue() == operateType) {
                reasons = sbCodeTypeService.findCodeDefList(RESERVE_RETENTION_REASON);
            } else if (ReserveOrderService.PJReserverOrderOperationCode.LanJianShiBaiTuiHui.getValue() == operateType) {
            	reasons = new ArrayList<SbCodeDefModel>();
            } else {
                throw new OspException("没有找到原因","没有找到原因");
            }
            for (int i = 0; i < reasons.size(); i++) {
                SbCodeDefModel reason = reasons.get(i);
                if (reason.getCodeValue().equals(reason4Feedback)) {
                    displayValue = reason.getDisplayValue();
                    logger.info("{} displayValue {}", logPrefix, displayValue);
                }
            }
        } catch (OspException e) {
            obj.put("errorMsg", e.getReturnMessage());
            return obj;
        }
        omReserveOrderModels = new ArrayList<OmReserveOrderModel>();
        for (ReserveOrderVo reserveOrderVo : reserveOrderVos) {
            logger.info("{} reserveOrder: {}", logPrefix, JsonUtil.translateToJson(reserveOrderVo));
            OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
            omReserveOrderModel.setReserveOrderNo(reserveOrderVo.getReserveOrderNo());
            omReserveOrderModel.setRecordVersion(reserveOrderVo.getRecordVersion());
            omReserveOrderModel.setReason(displayValue);
            omReserveOrderModel.setRemark(reserveOrderVo.getCnorRemark());
            omReserveOrderModels.add(omReserveOrderModel);
        }

        reserveOrderService.feedback(omReserveOrderModels, operateType, errMsg);
        buildErrorMsg(obj, errMsg);
        return obj;
    }

    /**
     * 修改预约单
     * @param vo 预约单修改vo
     * @return
     */
    @ResponseBody
    @RequestMapping("/editReserveOrder")
    public String editReserveOrder(ReserveOrderEditVo vo) {
    	final String logPrefix = "editReserveOrder->";
    	
    	ExplinkResponse explinkResponse = new ExplinkResponse();
    	
    	logger.info("{}vo:{}", logPrefix, JsonUtil.translateToJson(vo));
    	try {
    		vo.validate();
    	} catch (IllegalStateException e) {
    		explinkResponse.setStatuscode(Boolean.FALSE.toString());
    		explinkResponse.setErrorinfo(e.getMessage());
    		logger.info("{}explinkResponse:{}", logPrefix, JsonUtil.translateToJson(explinkResponse));
    		return JsonUtil.translateToJson(explinkResponse);
    	}
    	logger.info("{}vo->after validate:{}", logPrefix, JsonUtil.translateToJson(vo));
    	
    	try {
    		//修改预约单
    		reserveOrderService.editReserveOrder(vo, super.getSessionUser());
    		
    	} catch (ExplinkException e) {
    		logger.info(e.getMessage());
    		explinkResponse.setStatuscode(Boolean.FALSE.toString());
    		explinkResponse.setErrorinfo(e.getMessage());
    		
    	} catch (OspException e) {
    		logger.error(e.getMessage(), e);
    		explinkResponse.setStatuscode(Boolean.FALSE.toString());
    		explinkResponse.setErrorinfo("TPS系统错误");
    		
		} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		explinkResponse.setStatuscode(Boolean.FALSE.toString());
    		explinkResponse.setErrorinfo("系统错误");
    	}
    	
    	return JsonUtil.translateToJson(explinkResponse);
    }

    private void buildErrorMsg(JSONObject obj, List<String> errMsg) {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < errMsg.size(); i++) {
            String status = errMsg.get(i);
            msg.append(status);
            if (i < errMsg.size()) {
                msg.append("<br/>");
            }
        }
        obj.put("errorMsg", msg.toString());
    }
}
