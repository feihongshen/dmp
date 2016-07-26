/**
 *
 */
package cn.explink.service.mps.release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.exception.CwbException;

/**
 *
 * 退供应商对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
@Component("returnToCustomerReleaseService")
public final class ReturnToCustomerReleaseService extends AbstractMPSReleaseService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	
	@Override
	public void validateReleaseCondition(String transCwb) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
		CwbException exception = new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.OUTWAREHOUSE_MPS_NOT_ALL_ARRIVED);
		this.validateMPS(transCwb, cwbOrder, exception, AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE);
	}
	
	@Override
	protected void validateMPS(String transCwb, CwbOrder cwbOrder, CwbException exception, Set<Integer> beforeStateSet) {
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		if (MPSAllArrivedFlagEnum.NO.getValue() == mpsallarrivedflag) {
			TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
			if(transCwbDetail!=null&&transCwbDetail.getTranscwboptstate()==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){
				return;
			}
			throw exception;
		} else {
			List<TransCwbDetail> siblingTransCwbDetailList = this.getSiblingTransCwbDetailList(transCwb, cwbOrder.getCwb());
			for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
				if (beforeStateSet.contains(siblingTransCwbDetail.getTranscwboptstate())) {
					throw exception;
				}
			}
		}
	}
	
	/**
	 * 校验退供货商出库是否到齐
	 * @param cwb 订单号
	 * @param currentUserBranchid 当前用户所在站点id
	 * @param scancwb 扫描的单号
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang，2016-7-12
	 */
	public void validateBackToCustomerAllArrived(String cwb, long currentUserBranchid, String scancwb) {
		final String logPrefix = "校验退供货商出库是否到齐->";
		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder == null) {
			logger.info("{}cwbOrder为空，跳过", logPrefix);
			return ;
		}
		
		int ismpsflag = cwbOrder.getIsmpsflag(); //集单标识
		String transcwb = StringUtils.trim(cwbOrder.getTranscwb()); //运单号
		long sendcarnum = cwbOrder.getSendcarnum(); //发货数量
		long flowordertype = cwbOrder.getFlowordertype(); //操作类型
		logger.info("{}currentUserBranchid:{}, ismpsflag:{}, transcwb:{}, sendcarnum:{}, flowordertype:{}\n"
				+ "scancwb:{}", 
				logPrefix, currentUserBranchid, ismpsflag, transcwb, sendcarnum, flowordertype,
				scancwb);
		
		SystemInstall ypdjpathtongParam  = systemInstallDAO.getSystemInstallByName("ypdjpathtong");
		//一票多件操作流程件数限制:一票多件全部流程不对件数限制:(2),
		//缺件扫描下站，站点能扫描入库，但不能领货,但不能操作退货甲方:(1),
		//正常每个环节都会对件数验证限制:(0)
		if (ypdjpathtongParam != null && "2".equals(ypdjpathtongParam.getValue())) {
			logger.info("{}{}为非集单，系统参数ypdjpathtong为2，跳过", logPrefix, cwb);
			return ;
		}
		
		if (StringUtils.isEmpty(transcwb)) {
			logger.info("{}{}运单号为空，无需校验，跳过", logPrefix, cwb);
			return;
		}
		
		String[] transcwbs = transcwb.split(",");
		if (transcwbs == null || transcwbs.length != sendcarnum) {
			logger.info("{}{}发货数量与运单号数量不一致，无需校验，跳过", logPrefix, cwb);
			return;
		}
		
		/* ***************modify begin*********************/
		//commented by neo01.huang，2016-7-26，放开对express_ops_transcwb的校验，express_ops_transcwb的记录条数有可能跟sendcarnum不一致
		/*
		List<TranscwbView> transcwbViewList = transCwbDao.getTransCwbByCwb(cwb);
		if (CollectionUtils.isEmpty(transcwbViewList)) {
			logger.info("{}{}订单号没有绑定运单号，无需校验，跳过", logPrefix, cwb);
			return;
		}
		logger.info("{}transcwbViewList.size:{}", logPrefix, transcwbViewList.size());
		if (transcwbViewList.size() != sendcarnum) {
			logger.info("{}{}运单号数量与发货数量不一致，无需校验，跳过", logPrefix, cwb);
			return;
		}
		*/
		/* ***************modify end*********************/
		
		if (flowordertype != FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			//一件都还没退供货商出库校验
			validateNoOneBackToCustomer(cwbOrder, currentUserBranchid);
			
		} else {
			//第一件已经退供货商出库校验
			validateFirstDoneBackToCustomer(cwbOrder, currentUserBranchid, scancwb);
			
		}
		
		
	}
	
	/**
	 * 一件都还没退供货商出库校验
	 * @param cwbOrder 订单
	 * @param currentUserBranchid 当前用户所在站点id
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang，2016-7-13
	 */
	protected void validateNoOneBackToCustomer(CwbOrder cwbOrder, long currentUserBranchid) {
		final String logPrefix = "一件都还没退供货商出库校验->";
		
		String cwb = cwbOrder.getCwb(); //订单号
		long sendcarnum = cwbOrder.getSendcarnum(); //发货数量
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cwb", cwb);
		paramMap.put("flowordertype", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());
		paramMap.put("branchid", currentUserBranchid);
		paramMap.put("isnow", 1);
		
		/* SELECT * FROM express_ops_transcwb_orderflow t
			WHERE t.cwb = cwb
				AND t.flowordertype = 15(退货站入库)
				AND t.branchid = currentUserBranchid
				AND t.isnow = 1;
		 */
		//退货站入库List
		List<TranscwbOrderFlow> backIntoWareHouseFlowList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, null);
		backIntoWareHouseFlowList = backIntoWareHouseFlowList == null ? new ArrayList<TranscwbOrderFlow>() : backIntoWareHouseFlowList;
		logger.info("{}退货站入库List.size:{}", logPrefix, backIntoWareHouseFlowList.size());
		
		paramMap.put("flowordertype", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());
		/* SELECT * FROM express_ops_transcwb_orderflow t
			WHERE t.cwb = cwb
				AND t.flowordertype = 28(供货商拒收返库)
				AND t.branchid = currentUserBranchid
				AND t.isnow = 1;
		*/
		//供货商拒收返库List
		List<TranscwbOrderFlow> customerRefuseFlowList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, null);
		customerRefuseFlowList = customerRefuseFlowList == null ? new ArrayList<TranscwbOrderFlow>() : customerRefuseFlowList;
		logger.info("{}供货商拒收返库List.size:{}", logPrefix, customerRefuseFlowList.size());
		
		if (backIntoWareHouseFlowList.size() != sendcarnum && customerRefuseFlowList.size() != sendcarnum) {
			logger.info("{}{}退货站入库List.size不等于sendcarnum AND 供货商拒收返库List.size不等于sendcarnum", logPrefix, cwb);
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.YPDJSTATE_CONTROL_ERROR, 
					FlowOrderTypeEnum.TuiHuoZhanRuKu.getText(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getText());
		}
		
		logger.info("{}通过", logPrefix);
	}
	
	/**
	 * 第一件已经退供货商出库校验
	 * @param cwbOrder 订单
	 * @param currentUserBranchid 当前用户所在站点id
	 * @param scancwb 扫描的单号
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang，2016-7-13
	 */
	protected void validateFirstDoneBackToCustomer(CwbOrder cwbOrder, long currentUserBranchid, String scancwb) {
		final String logPrefix = "第一件已经退供货商出库校验->";
		
		String cwb = cwbOrder.getCwb(); //订单号
		
		if (StringUtils.equals(cwb, scancwb)) {
			logger.info("{}扫描的是订单号，无需校验", logPrefix);
			return;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("flowordertypeIn", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());
        paramMap.put("isnow", 1);
        paramMap.put("scancwb", scancwb);
        
        /*
		 * SELECT * FROM express_ops_transcwb_orderflow t
			WHERE 1 = 1
				AND t.flowordertype IN (15(退货站入库),28(供货商拒收返库)
				AND t.isnow = 1
				AND t.scancwb = scancwb
		 */
        //退货站入库List
        List<TranscwbOrderFlow> backIntoWarehouseList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, null);
		if (CollectionUtils.isEmpty(backIntoWarehouseList)) {
			logger.info("{}{}退货站入库List为空", logPrefix, cwb);
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.DIFF_TUI_HUO_KU_ZUO_BACK_TO_CUSTOMER);
		}
		
		//当前运单退货站入库branchId
		long backIntoWarehouseBranchId = backIntoWarehouseList.get(0).getBranchid();
		logger.info("{}{}，backIntoWarehouseBranchId:{}, currentUserBranchid:{}", logPrefix, cwb,
				backIntoWarehouseBranchId, currentUserBranchid);
		if (currentUserBranchid != backIntoWarehouseBranchId) {
			logger.info("{}{}当前运单退货站入库branchId不等于userBranchId", logPrefix, cwb);
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.DIFF_TUI_HUO_KU_ZUO_BACK_TO_CUSTOMER);
		}
        
		logger.info("{}通过", logPrefix);
	}

}
