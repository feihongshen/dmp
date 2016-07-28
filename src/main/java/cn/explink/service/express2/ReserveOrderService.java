package cn.explink.service.express2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.explink.util.ResourceBundleUtil;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjbest.deliveryorder.bizservice.PjReserveOrderRequest;
import com.pjbest.deliveryorder.bizservice.PjReserveOrderResponse;
import com.pjbest.deliveryorder.bizservice.PjReserveOrderService;
import com.pjbest.deliveryorder.bizservice.PjReserveOrderServiceHelper;
import com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum;
import com.pjbest.deliveryorder.service.OmReserveOrderModel;
import com.pjbest.deliveryorder.service.PjReserveOrderPageModel;
import com.pjbest.deliveryorder.service.PjReserveOrderQueryModel;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;
import com.pjbest.deliveryorder.service.ReserveOrderLogModel;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

import cn.explink.core.utils.JsonUtil;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.dao.express.TownDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.express2.VO.ReserveOrderEditVo;
import cn.explink.domain.express2.VO.ReserveOrderLogVo;
import cn.explink.domain.express2.VO.ReserveOrderPageVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.express2.ReserveOrderDmpStatusEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.service.express.ExpressCommonService;

/**
 * 预约单Service
 * @date 2016年5月13日 下午6:11:56
 */
@Service
public class ReserveOrderService extends ExpressCommonService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int OSP_INVOKE_TIMEOUT = 60000;
	
	/**TPS接口请求方*/
	public static final String TPS_REQUESTER = "DMP";

    public enum PJReserverOrderOperationCode {
        YiShenHe(38, "已审核"), RenWuTueHui(35, "任务退回"), RenWuQueRen(34, "任务确认"), GuanBi(33, "关闭"),
        FanKuiJiLiu(32, "反馈滞留"), LanJianShiBai(31, "揽件失败"), LanJianShiBaiTuiHui(30, "揽件超区(退回)", "揽件超区"),
        ZhanDianChaoQu(29, "站点超区"), ShengGongSiChaoQu(28, "省公司超区"), LanJianChengGong(27, "揽件成功"),
        YiLanJianFenPei(26, "已揽件分配"), YiFenPeiZhanDian(25, "已分配站点"), YiFenPeiShengGongSi(24, "已分配省公司");

        private int value;
        private String text;
        private String displayText = "";

        PJReserverOrderOperationCode(int value, String text, String displayText) {
            this.value = value;
            this.text = text;
            this.displayText = displayText;
        }

        PJReserverOrderOperationCode(int value, String text) {
            this.value = value;
            this.text = text;
            this.displayText = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public String getDisplayText() {
            return displayText;
        }
    }

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDao;
	@Autowired
    CountyDAO countyDAO;
    @Autowired
    CityDAO cityDAO;
    @Autowired
    ProvinceDAO provinceDAO;
    @Autowired
    SystemInstallDAO systemInstallDAO;
    @Autowired
    TownDAO townDAO;

    private static final String MAX_ROW_SIZE_DB_IN_DB  = "Express2ROExcelRows";
	
	/**
	 * 获取预约单列表
	 * @date 2016年5月13日 下午6:13:22
	 * @return
	 */
	public ReserveOrderPageVo getReserveOrderPage(OmReserveOrderModel omReserveOrderModel, int page, int rows) {
        logger.info("调用TPS查询接口开始");

        // 记录入库数据
//		try {
//			logger.info("omReserveOrderModel:{}", JsonUtil.translateToJson(omReserveOrderModel));
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}

        InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT);
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		PjReserveOrderPageModel pjReserveOrderPageModel = null;
		try {
			PjReserveOrderQueryModel pjReserveOrderQueryModel = new PjReserveOrderQueryModel();
			pjReserveOrderQueryModel.setPageNo(page);
			pjReserveOrderQueryModel.setPageSize(rows);
			pjReserveOrderQueryModel.setCondition(omReserveOrderModel);
            logger.info("查询报文: " + toInfoString(omReserveOrderModel));
            pjReserveOrderPageModel = pjReserveOrderService.getReserveOrders(pjReserveOrderQueryModel);
			// 返回数据记录
			logger.info("查询成功: 总条数：{}", pjReserveOrderPageModel.getReserveOrders().size());
            logger.debug("查询结果: " + toInfoString(pjReserveOrderPageModel));
		} catch (OspException e) {
            logger.info("查询报错： ");
			logger.error(e.getMessage(), e);
		}
		List<OmReserveOrderModel> poList = pjReserveOrderPageModel.getReserveOrders();
		List<ReserveOrderVo> voList = new ArrayList<ReserveOrderVo>(poList.size());
		// po转vo
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (OmReserveOrderModel po : poList) {
			ReserveOrderVo vo = new ReserveOrderVo();
			vo.setOmReserveOrderId(po.getOmReserveOrderId());
			vo.setReserveOrderNo(po.getReserveOrderNo());
			Long appointTimeMs = po.getAppointTime();
			if (appointTimeMs != null) {
				Date appointTime = new Date(appointTimeMs);
				vo.setAppointTime(appointTime);
				vo.setAppointTimeStr(sdf.format(appointTime));
			}
			vo.setCnorName(po.getCnorName());
			vo.setCnorMobile(po.getCnorMobile());
			vo.setCnorTel(po.getCnorTel());
			vo.setCnorAddr(po.getCnorAddr());
			vo.setCnorDetailAddr(po.getCnorProvName() + po.getCnorCityName() + po.getCnorRegionName() + po.getCnorAddr());
			Long requireTimeMs = po.getRequireTime();
			if (requireTimeMs != null) {
				Date requireTime = new Date(requireTimeMs);
				vo.setRequireTime(requireTime);
				vo.setRequireTimeStr(sdf.format(requireTime));
			}
			vo.setReserveOrderStatus(po.getReserveOrderStatus());
			// 转换成本地状态名称
			Integer reserveOrderStatus = po.getReserveOrderStatus() == null ? null : po.getReserveOrderStatus().intValue();
			vo.setReserveOrderStatusName(ReserveOrderDmpStatusEnum.getNameByIndex(reserveOrderStatus));
			vo.setReason(po.getReason());
			vo.setTransportNo(po.getTransportNo());
//			vo.setAcceptOrg(po.getAcceptOrg());
//			vo.setAcceptOrgName(po.getAcceptOrgName());
            List<Branch> acceptBranches = branchDAO.getBranchByTpsBranchcode(po.getCarrierSiteCode());
            if (acceptBranches != null && acceptBranches.size() > 0) {
                vo.setAcceptOrgName(acceptBranches.get(0).getBranchname());
            }
            vo.setCnorRemark(po.getCnorRemark());
            vo.setCourier(po.getCourier());
            vo.setCustName(po.getCustName());
            User user = this.userDao.getUserByUsername(po.getCourier());
            if(user != null) {
            	vo.setCourierName(user.getRealname());
            }
            vo.setRecordVersion(po.getRecordVersion());
			vo.setCnorProvName(po.getCnorProvName());
            vo.setCnorCityName(po.getCnorCityName());
            vo.setCnorRegionName(po.getCnorRegionName());
            vo.setCarrierSiteCode(po.getCarrierSiteCode());
//            vo.setRemark(po.getRemark());
            vo.setCnorRemark(po.getCnorRemark());
            voList.add(vo);
		}
		// 封装分页信息
		ReserveOrderPageVo reserveOrderPageVo = new ReserveOrderPageVo();
		reserveOrderPageVo.setTotalRecord(pjReserveOrderPageModel.getTotalRecord());
		reserveOrderPageVo.setReserveOrderVoList(voList);

        logger.info("调用TPS查询接口结束");

		return reserveOrderPageVo;
	}
	
	/**
	 * 获取全部的预约单
	 * @date 2016年5月16日 下午5:28:52
	 * @param omReserveOrderModel
	 * @return
	 */
	public List<ReserveOrderVo> getTotalReserveOrders(OmReserveOrderModel omReserveOrderModel) {

        List<ReserveOrderVo> reserveOrderList = new ArrayList<ReserveOrderVo>();
		final int DEFAULT_MAX_ROW_SIZE = 10000; //默认最大导出数量
		final int ROW_SIZE = 1000; //分批次查询数量

        int maxRowSizeDefined = DEFAULT_MAX_ROW_SIZE;
        // 从系统加载最大导出量，如果系统没有配置的话，就是用默认值
        SystemInstall systemInstall = systemInstallDAO.getSystemInstallByName(MAX_ROW_SIZE_DB_IN_DB);
        if (systemInstall != null && NumberUtils.isNumber(systemInstall.getValue())) {
            maxRowSizeDefined = Integer.parseInt(systemInstall.getValue());
        }
		//第一次查询，如果批次查询量不得大于最大导出量
        int fistRowSize = ROW_SIZE > maxRowSizeDefined ? maxRowSizeDefined : ROW_SIZE;
		ReserveOrderPageVo reserveOrderPageVo = this.getReserveOrderPage(omReserveOrderModel, 1, fistRowSize);
		reserveOrderList.addAll(reserveOrderPageVo.getReserveOrderVoList());
		int maxRowSize = reserveOrderPageVo.getTotalRecord();
		if(maxRowSize > maxRowSizeDefined) {
			maxRowSize = maxRowSizeDefined;
		}
		//继续查询
		int pageSize = (int) Math.ceil((double) maxRowSize / (double) ROW_SIZE);
		for(int page = 2; page <= pageSize; page++) {
			int rowSize = ROW_SIZE;
			// 最后一页，则查询剩余的记录即可
			if(page == pageSize) {
				rowSize = maxRowSize - (page - 1) * ROW_SIZE;
			}
			reserveOrderPageVo = this.getReserveOrderPage(omReserveOrderModel, page, rowSize);
			reserveOrderList.addAll(reserveOrderPageVo.getReserveOrderVoList());
		}
        return reserveOrderList;
	}
	
	/**
	 * 通过城市获取区
	 * @param cityId
	 * @return
	 */
    public List<AdressVO> getRegionsByCity(Integer cityId) {
        return this.countyDAO.getCountyOfCity(cityId);
    }

    /**
     * 获取当前省的所有城市
     * @return
     */
    public List<AdressVO> getCities() {
//
//        List<AdressVO> provincelist = this.provinceDAO.getProvince();
//        Long provinceId = this.getProvinceId();
//        if (provinceId == 0) {
//            return this.cityDAO.getCityOfProvince(provincelist.size() > 0 ? provincelist.get(0).getCode() : null);
//        } else {
//            return this.cityDAO.getCityOfProvince(provinceId.intValue());
//        }
        String provinceCode = ResourceBundleUtil.provinceCode;
        List<AdressVO> cities;
        if (provinceCode != null) {
            cities = cityDAO.getCityOfProvince(provinceCode);
            if (cities != null){
                return cities;
            }
        }
        return new ArrayList<AdressVO>();
    }

    /**
     * 获取所有站点
     * @return
     */
    public List<Branch> getBranches() {
        //数据库以省为单位，所以查找的是省的站点
       return branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
    }

    /**
     * 通过站点获取快递员
     * @param branchId
     * @return
     */
    public List<User>  getCourierByBranch(int branchId) {
        return userDao.getUserByRoleAndBranchid(2, branchId);
    }
    
	/**
	 * 查询预约单日志
	 * @param reserveOrderNo 预约单号
	 * @return 返回预约单日志list
	 * @throws OspException 
	 */
	public List<ReserveOrderLogVo> queryReserveOrderLog(String reserveOrderNo) throws OspException {
		
		InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT); 
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		List<ReserveOrderLogModel> reserveOrderLogModelList = pjReserveOrderService.getReserveOrderLogs(reserveOrderNo); 
		if (reserveOrderLogModelList == null || reserveOrderLogModelList.size() == 0) {
			return Collections.emptyList();
		}
		//System.out.println("reserveOrderLogModelList:" + JsonUtil.translateToJson(reserveOrderLogModelList));
		List<ReserveOrderLogVo> logVoList = new ArrayList<ReserveOrderLogVo>();
		for (ReserveOrderLogModel reserveOrderLogModel : reserveOrderLogModelList) {
			ReserveOrderLogVo logVo = new ReserveOrderLogVo();
			try {
				PropertyUtils.copyProperties(logVo, reserveOrderLogModel);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			//转换
			logVo.convert();
			logVoList.add(logVo);
		}
		return logVoList;
	}


    /**
     * 关闭预约单
     * @param omReserveOrderModels
     * @throws OspException
     */
    public void closeReserveOrder(List<OmReserveOrderModel> omReserveOrderModels, List<String> errMsg) {

        User user = this.getSessionUser();
        Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid()) ;
        if (branch == null){
            errMsg.add("操作用户没有机构信息");
            return;
        }
        String operateOrg = branch.getTpsbranchcode();
        String operator = user.getUsername();
        for (OmReserveOrderModel omReserveOrderModel : omReserveOrderModels) {
            PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest = new PjSaleOrderFeedbackRequest();
            pjSaleOrderFeedbackRequest.setReserveOrderNo(omReserveOrderModel.getReserveOrderNo());
            pjSaleOrderFeedbackRequest.setRecordVersion(omReserveOrderModel.getRecordVersion());
//            pjSaleOrderFeedbackRequest.setReason(omReserveOrderModel.getReason());
            //TPS去掉了omReserveOrderModel的Remark字段，用cnorRemark字段代替
//            pjSaleOrderFeedbackRequest.setRemark(omReserveOrderModel.getRemark());
            pjSaleOrderFeedbackRequest.setRemark(omReserveOrderModel.getCnorRemark());
            pjSaleOrderFeedbackRequest.setOperateType(PJReserverOrderOperationCode.GuanBi.getValue());
            pjSaleOrderFeedbackRequest.setOperateOrg(operateOrg);
            pjSaleOrderFeedbackRequest.setOperater(operator);
            Date now = new Date();
            pjSaleOrderFeedbackRequest.setOperateTime(now.getTime());

            try {
                feedbackReserveOrder(pjSaleOrderFeedbackRequest);
            }catch (OspException ospException){
                errMsg.add(ospException.getReturnMessage());
            }
        }

    }


    /**
     * 返回操作
     * @param omReserveOrderModels
     * @param returnType
     * @throws OspException
     */
    public void returnToCentral(List<OmReserveOrderModel> omReserveOrderModels, int returnType, List<String> errMsg) {

        User user = this.getSessionUser();
        Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid()) ;
        if (branch == null){
            errMsg.add("操作用户没有机构信息");
            return;
        }
        String operateOrg = branch.getTpsbranchcode();
        String operator = user.getUsername();
        for (OmReserveOrderModel omReserveOrderModel  : omReserveOrderModels) {
            PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest = new PjSaleOrderFeedbackRequest();
            pjSaleOrderFeedbackRequest.setReserveOrderNo(omReserveOrderModel.getReserveOrderNo());
            pjSaleOrderFeedbackRequest.setRecordVersion(omReserveOrderModel.getRecordVersion());
//            pjSaleOrderFeedbackRequest.setReason(omReserveOrderModel.getReason());
            pjSaleOrderFeedbackRequest.setRemark(omReserveOrderModel.getCnorRemark());
            pjSaleOrderFeedbackRequest.setOperateType(returnType);
            pjSaleOrderFeedbackRequest.setOperateOrg(operateOrg);
            pjSaleOrderFeedbackRequest.setOperater(operator);
            Date now = new Date();
            pjSaleOrderFeedbackRequest.setOperateTime(now.getTime());

            try {
                feedbackReserveOrder(pjSaleOrderFeedbackRequest);
            }catch (OspException ospException){
                errMsg.add(ospException.getReturnMessage());
            }
        }
    }


    /**
     * 分配快递员
     * @param omReserveOrderModels
     * @throws OspException
     */
    public void distributeBranch(List<OmReserveOrderModel> omReserveOrderModels, List<String> errMsg){

        User user = this.getSessionUser();
        Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid()) ;
        if (branch == null){
            errMsg.add("操作用户没有机构信息");
            return;
        }
        String operateOrg = branch.getTpsbranchcode();
        String operator = user.getUsername();
        for (OmReserveOrderModel omReserveOrderModel : omReserveOrderModels) {
            PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest = new PjSaleOrderFeedbackRequest();
            pjSaleOrderFeedbackRequest.setReserveOrderNo(omReserveOrderModel.getReserveOrderNo());
            pjSaleOrderFeedbackRequest.setRecordVersion(omReserveOrderModel.getRecordVersion());
            pjSaleOrderFeedbackRequest.setAcceptOrg(omReserveOrderModel.getAcceptOrg());
            pjSaleOrderFeedbackRequest.setCourier(omReserveOrderModel.getCourier());
            pjSaleOrderFeedbackRequest.setCourierName(omReserveOrderModel.getCourierName());
            pjSaleOrderFeedbackRequest.setOperateType(omReserveOrderModel.getReserveOrderStatus().intValue());
            pjSaleOrderFeedbackRequest.setOperateOrg(operateOrg);
            pjSaleOrderFeedbackRequest.setOperater(operator);
            Date now = new Date();
            pjSaleOrderFeedbackRequest.setOperateTime(now.getTime());

            try {
                feedbackReserveOrder(pjSaleOrderFeedbackRequest);
            }catch (OspException ospException){
                errMsg.add(ospException.getReturnMessage());
            }
        }

    }



    public void feedback(List<OmReserveOrderModel> omReserveOrderModels, int operateType, List<String> errMsg) {

        User user = this.getSessionUser();
        Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid()) ;
        if (branch == null){
            errMsg.add("操作用户没有机构信息");
            return;
        }
        String operateOrg = branch.getTpsbranchcode();
        String operator = user.getUsername();
        for (OmReserveOrderModel omReserveOrderModel : omReserveOrderModels) {
            PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest = new PjSaleOrderFeedbackRequest();
            pjSaleOrderFeedbackRequest.setReserveOrderNo(omReserveOrderModel.getReserveOrderNo());
            pjSaleOrderFeedbackRequest.setRecordVersion(omReserveOrderModel.getRecordVersion());
            pjSaleOrderFeedbackRequest.setOperateType(operateType);
            pjSaleOrderFeedbackRequest.setOperateOrg(operateOrg);
            pjSaleOrderFeedbackRequest.setReason(omReserveOrderModel.getReason());
            pjSaleOrderFeedbackRequest.setRemark(omReserveOrderModel.getCnorRemark());
            pjSaleOrderFeedbackRequest.setOperater(operator);
            Date now = new Date();
            pjSaleOrderFeedbackRequest.setOperateTime(now.getTime());

            try {
                feedbackReserveOrder(pjSaleOrderFeedbackRequest);
            }catch (OspException ospException){
                errMsg.add(ospException.getReturnMessage());
            }
        }

    }


    /**
     * 反馈服务
     * @param pjSaleOrderFeedbackRequest
     * @throws OspException
     */
    private void feedbackReserveOrder(PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest) throws OspException{
        logger.info("调用TPS反馈接口开始");

        InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT);
        PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();

        try {
            logger.info("反馈报文: " + toInfoString(pjSaleOrderFeedbackRequest));

            PjReserveOrderResponse reserveOrderResponse = pjReserveOrderService.feedbackReserveOrder(pjSaleOrderFeedbackRequest);
            // 返回数据记录
            logger.info("反馈结果: code 1 success, 0 fail - {}", reserveOrderResponse.getResultCode());
            logger.info("反馈结果: message - {}", reserveOrderResponse.getResultMsg());

            if ("0".equals(reserveOrderResponse.getResultCode())){
                throw new OspException("",reserveOrderResponse.getResultMsg());
            }

        } catch (OspException e) {
            logger.info("反馈异常");
            logger.error(e.getMessage(), e);
            throw e;
        }

        logger.info("调用TPS反馈接口结束");

    }

    private String toInfoString(Object object) {
        return  JsonUtil.translateToJson(object);
    }
    
    public OmReserveOrderModel getReserveOrderAddress(Integer cnorCity, Integer cnorRegion) {
    	OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
    	if(cnorCity == null) {
    		return omReserveOrderModel;
    	}
    	AdressVO city = this.cityDAO.getProvinceById(cnorCity);
    	omReserveOrderModel.setCnorCity(city.getCode());
    	omReserveOrderModel.setCnorCityName(city.getName());
    	
    	AdressVO prov = this.provinceDAO.getProvinceByCode(city.getParentCode());
    	if(prov != null) {
    		omReserveOrderModel.setCnorProv(prov.getCode());
        	omReserveOrderModel.setCnorProvName(prov.getName());
    	}
    	if(cnorRegion != null) {
    		AdressVO region = this.countyDAO.getCountyById(cnorRegion);
        	omReserveOrderModel.setCnorRegion(region.getCode());
        	omReserveOrderModel.setCnorRegionName(region.getName());
    	}
    	return omReserveOrderModel;
    }
	
	/**
     * 修改预约单
     * @param vo 预约单编辑vo
     * @throws OspException 
     */
    public void editReserveOrder(ReserveOrderEditVo vo, User user) throws OspException {
        logger.info("调用TPS修改接口开始");

        final String logPrefix = "editReserveOrder";
    	InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT); 
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		//通过预约单号获取预约单
		OmReserveOrderModel omReserveOrderModel = pjReserveOrderService.getReserveOrderWithDetailByNo(vo.getReserveOrderNo());
		if (omReserveOrderModel == null) {
			throw new ExplinkException("预约单不存在，预约单号：" + vo.getReserveOrderNo());
		}
//		logger.info("{}omReserveOrderModel:{}", logPrefix, JsonUtil.translateToJson(omReserveOrderModel));
		
		if (ReserveOrderStatusEnum.HadReceiveSuccess.getIndex().intValue() == omReserveOrderModel.getReserveOrderStatus().intValue() ||
				ReserveOrderStatusEnum.HaveReciveFailure.getIndex().intValue() == omReserveOrderModel.getReserveOrderStatus().intValue() ||
				ReserveOrderStatusEnum.HadClosed.getIndex().intValue() == omReserveOrderModel.getReserveOrderStatus().intValue() ) {
			throw new ExplinkException("状态为揽件成功、揽件失败、已关闭的预约单不允许修改，预约单号：" + vo.getReserveOrderNo());
		}
		
		
		PjReserveOrderRequest pjReserveOrderRequest = new PjReserveOrderRequest();
		pjReserveOrderRequest.setReserveOrderNo(vo.getReserveOrderNo()); //预约单号
		pjReserveOrderRequest.setCnorName(vo.getCnorName4edit()); //寄件人姓名
		
		//获取当前所在省
		AdressVO prov = getCurProvince(vo.getCity4editInt());
		pjReserveOrderRequest.setCnorProv(prov.getName()); //省份编码，传名称
		
		pjReserveOrderRequest.setCnorCity(vo.getCityName4edit()); //城市编码，传名称
		pjReserveOrderRequest.setCnorRegion(vo.getCountyName4edit()); //区域编码，传名称
		pjReserveOrderRequest.setCnorAddr(vo.getCnorAddr4edit()); //寄件地址
		pjReserveOrderRequest.setRequireTime(vo.getRequireTimeLong()); //预约上门时间
		pjReserveOrderRequest.setRequester(TPS_REQUESTER); //请求方
		pjReserveOrderRequest.setOperater(user.getUsername()); //操作人名称
        pjReserveOrderRequest.setRecordVersion(vo.getRecordVersion());

        //原值回传
        pjReserveOrderRequest.setWeight(omReserveOrderModel.getWeight());
        pjReserveOrderRequest.setAppointTime(omReserveOrderModel.getAppointTime());
        pjReserveOrderRequest.setCnorMobile(omReserveOrderModel.getCnorMobile());
        pjReserveOrderRequest.setCnorTel(omReserveOrderModel.getCnorTel());
//		pjReserveOrderRequest.setRecordVersion(omReserveOrderModel.getRecordVersion());

		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		if (branch == null) {
			throw new ExplinkException("当前用户所属机构不存在");
		}
		String operateOrg = branch.getTpsbranchcode();
		pjReserveOrderRequest.setOperateOrg(operateOrg); //操作机构
		
		logger.info("修改报文: {}", JsonUtil.translateToJson(pjReserveOrderRequest));
		
		//修改预约单
		PjReserveOrderResponse pjReserveOrderResponse = pjReserveOrderService.updateReserveOrder(pjReserveOrderRequest);
		if (pjReserveOrderResponse == null) {
			throw new ExplinkException("TPS的response对象为空，预约单号：" + vo.getReserveOrderNo());
		}
        logger.info("修改结果: code 1 success, 0 fail - {}", pjReserveOrderResponse.getResultCode());
        logger.info("修改结果: message - {}", pjReserveOrderResponse.getResultMsg());

		if ("0".equals(pjReserveOrderResponse.getResultCode())) {
			throw new ExplinkException(pjReserveOrderResponse.getResultMsg());
		}

        logger.info("调用TPS修改接口结束");

    }
    
    /**
     * 获取当前所在省
     * @param cityId 城市id
     * @return
     */
    public AdressVO getCurProvince(int cityId) {
    	AdressVO city = this.cityDAO.getProvinceById(cityId);
		if (city == null) {
			throw new ExplinkException("市不存在，市的id为：" + cityId);
		}
		AdressVO prov = this.provinceDAO.getProvinceByCode(city.getParentCode());
		if (prov == null) {
			throw new ExplinkException("省不存在，省的编号为：" + city.getParentCode());
		}
		return prov;
    }
    
    /**
     * 反馈预约单揽件成功给tps
     * @param branch 
     * @param omReserveOrderModels
     * @param returnType
     * @return 
     * @throws OspException
     */
    public void returnReserveOrderStateToTps(EmbracedOrderVO embracedOrderVO, Branch branch) {
        PjSaleOrderFeedbackRequest pjSaleOrderFeedbackRequest = new PjSaleOrderFeedbackRequest();
        pjSaleOrderFeedbackRequest.setReserveOrderNo(embracedOrderVO.getReserveOrderNo());
        pjSaleOrderFeedbackRequest.setRecordVersion(embracedOrderVO.getRecordVersion());
//            pjSaleOrderFeedbackRequest.setReason(omReserveOrderModel.getReason());
        pjSaleOrderFeedbackRequest.setOperateType(27);
        pjSaleOrderFeedbackRequest.setOperater(embracedOrderVO.getDelivermanName());
        pjSaleOrderFeedbackRequest.setOperateOrg(branch.getTpsbranchcode());
        pjSaleOrderFeedbackRequest.setTransportNo(embracedOrderVO.getOrderNo());
        pjSaleOrderFeedbackRequest.setAcceptOrg(branch.getTpsbranchcode());
        Date now = new Date();
        pjSaleOrderFeedbackRequest.setOperateTime(now.getTime());

        try {
            feedbackReserveOrder(pjSaleOrderFeedbackRequest);
        }catch (OspException e){
        	logger.error(e.getMessage(), e);
        }
    }

	public ReserveOrderPageVo getReserveOrder(
			OmReserveOrderModel omReserveOrderModel) {
		Map<String, String> orderField = new HashMap();
		orderField.put("appoint_Time", "desc");
		ReserveOrderPageVo reserveOrderPageVo = this.getReserveOrderList(omReserveOrderModel,orderField,1, 3);
		List<ReserveOrderVo> reserveOrderList = reserveOrderPageVo.getReserveOrderVoList();
		for(int i=0;i<reserveOrderList.size();i++){
			int provinceId = 0;
			int cityId = 0;
			int countyId = 0;
			int townId = 0;
			AdressVO provinceVo =  provinceDAO.getProviceByName(reserveOrderList.get(i).getCnorProvName());
			AdressVO cityVo = null;
			AdressVO contyVo = null;
			AdressVO townVo = null;
			if(provinceVo!=null&&provinceVo.getId()!=0){
				provinceId = provinceVo.getId();
			}
			if(provinceVo!=null&&provinceId!=0){
				cityVo = cityDAO.getCityByNameAndProvice(reserveOrderList.get(i).getCnorCityName(),provinceVo);
				cityId=cityVo.getId();
			}
			if(cityVo!=null&&cityId!=0){
				contyVo = countyDAO.getCountyByNameAndCity(reserveOrderList.get(i).getCnorRegionName(),cityVo);
				countyId=contyVo.getId();
			}
			if(cityVo!=null&&countyId!=0){
				townVo = townDAO.getTownByNameAndCounty(reserveOrderList.get(i).getCnorRegionName(),cityVo);
				townId=cityVo.getId();
			}
			reserveOrderList.get(i).setCnorProvCode(provinceId+"");
			reserveOrderList.get(i).setCnorCityCode(cityId+"");
			reserveOrderList.get(i).setCnorRegionCode(countyId+"");
			reserveOrderList.get(i).setCnorTownCode(townId+"");
		}
		return reserveOrderPageVo;
	}
	
	/**
	 * 获取预约单列表
	 * @date 2016年7月13日 下午2:13:22
	 * @author zhouhuan
	 * @param orderField 
	 * @return
	 */
	public ReserveOrderPageVo getReserveOrderList(OmReserveOrderModel omReserveOrderModel, Map<String, String> orderField, int page, int rows) {
        logger.info("调用TPS查询接口开始");

        // 记录入库数据
//		try {
//			logger.info("omReserveOrderModel:{}", JsonUtil.translateToJson(omReserveOrderModel));
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}

        InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT);
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		PjReserveOrderPageModel pjReserveOrderPageModel = null;
		try {
			PjReserveOrderQueryModel pjReserveOrderQueryModel = new PjReserveOrderQueryModel();
			pjReserveOrderQueryModel.setPageNo(page);
			pjReserveOrderQueryModel.setPageSize(rows);
			pjReserveOrderQueryModel.setCondition(omReserveOrderModel);
			pjReserveOrderQueryModel.setOrderByProps(orderField);
            logger.info("查询报文: " + toInfoString(omReserveOrderModel));
            pjReserveOrderPageModel = pjReserveOrderService.getReserveOrders(pjReserveOrderQueryModel);
			// 返回数据记录
			logger.info("查询成功: 总条数：{}", pjReserveOrderPageModel.getReserveOrders().size());
            logger.debug("查询结果: " + toInfoString(pjReserveOrderPageModel));
		} catch (OspException e) {
            logger.info("查询报错： ");
			logger.error(e.getMessage(), e);
		}
		List<OmReserveOrderModel> poList = pjReserveOrderPageModel.getReserveOrders();
		List<ReserveOrderVo> voList = new ArrayList<ReserveOrderVo>(poList.size());
		// po转vo
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (OmReserveOrderModel po : poList) {
			ReserveOrderVo vo = new ReserveOrderVo();
			vo.setOmReserveOrderId(po.getOmReserveOrderId());
			vo.setReserveOrderNo(po.getReserveOrderNo());
			Long appointTimeMs = po.getAppointTime();
			if (appointTimeMs != null) {
				Date appointTime = new Date(appointTimeMs);
				vo.setAppointTime(appointTime);
				vo.setAppointTimeStr(sdf.format(appointTime));
			}
			vo.setCnorName(po.getCnorName());
			vo.setCnorMobile(po.getCnorMobile());
			vo.setCnorTel(po.getCnorTel());
			vo.setCnorAddr(po.getCnorAddr());
			vo.setCnorDetailAddr(po.getCnorProvName() + po.getCnorCityName() + po.getCnorRegionName() + po.getCnorAddr());
			Long requireTimeMs = po.getRequireTime();
			if (requireTimeMs != null) {
				Date requireTime = new Date(requireTimeMs);
				vo.setRequireTime(requireTime);
				vo.setRequireTimeStr(sdf.format(requireTime));
			}
			vo.setReserveOrderStatus(po.getReserveOrderStatus());
			// 转换成本地状态名称
			Integer reserveOrderStatus = po.getReserveOrderStatus() == null ? null : po.getReserveOrderStatus().intValue();
			vo.setReserveOrderStatusName(ReserveOrderDmpStatusEnum.getNameByIndex(reserveOrderStatus));
			vo.setReason(po.getReason());
			vo.setTransportNo(po.getTransportNo());
//			vo.setAcceptOrg(po.getAcceptOrg());
//			vo.setAcceptOrgName(po.getAcceptOrgName());
            List<Branch> acceptBranches = branchDAO.getBranchByTpsBranchcode(po.getCarrierSiteCode());
            if (acceptBranches != null && acceptBranches.size() > 0) {
                vo.setAcceptOrgName(acceptBranches.get(0).getBranchname());
            }
            vo.setCnorRemark(po.getCnorRemark());
            vo.setCourier(po.getCourier());
            vo.setCustName(po.getCustName());
            User user = this.userDao.getUserByUsername(po.getCourier());
            if(user != null) {
            	vo.setCourierName(user.getRealname());
            }
            vo.setRecordVersion(po.getRecordVersion());
			vo.setCnorProvName(po.getCnorProvName());
            vo.setCnorCityName(po.getCnorCityName());
            vo.setCnorRegionName(po.getCnorRegionName());
            vo.setCarrierSiteCode(po.getCarrierSiteCode());
//            vo.setRemark(po.getRemark());
            vo.setCnorRemark(po.getCnorRemark());
            
            vo.setCnorProvCode(po.getCnorProv());
            vo.setCnorCityCode(po.getCnorCity());
            vo.setCnorRegionCode(po.getCnorRegion());
            voList.add(vo);
		}
		// 封装分页信息
		ReserveOrderPageVo reserveOrderPageVo = new ReserveOrderPageVo();
		reserveOrderPageVo.setTotalRecord(pjReserveOrderPageModel.getTotalRecord());
		reserveOrderPageVo.setReserveOrderVoList(voList);

        logger.info("调用TPS查询接口结束");

		return reserveOrderPageVo;
	}
}
