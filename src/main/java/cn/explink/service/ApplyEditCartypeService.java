package cn.explink.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.explink.core.utils.StringUtils;
import cn.explink.dao.ApplyEditCartypeDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.ApplyEditCartypeResultView;
import cn.explink.domain.ApplyEditCartypeVO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.deliveryFee.DfBillFee;
import cn.explink.enumutil.ApplyEditCartypeReviewStatusEnum;
import cn.explink.enumutil.CarTypeEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.DfFeeService.DeliveryFeeChargerType;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.express.Page;


/**
 * 审核货物类型变更的service
 * @author zhili01.liang
 *
 */
@Service
public class ApplyEditCartypeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	ApplyEditCartypeDAO applyEditCartypeDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	BranchService branchService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DfFeeService dfFeeService;
	
	/**
	 * 根据订单或运单号发起货物类型修改申请，暂时只支持普件进行申请
	 * @param cwbs
	 * @param applyUserid
	 * @param applyCartype
	 * @return Map,内含2个key: succList, failList
	 */
	public Map<String,List<ApplyEditCartypeResultView>> applyByCwds(String cwbs,User applyUser,String applyCartype,String remark){
		//成功列表
		List<ApplyEditCartypeResultView> succList = new ArrayList<ApplyEditCartypeResultView>();
		//失败列表
		List<ApplyEditCartypeResultView> failList = new ArrayList<ApplyEditCartypeResultView>();
		Map<String,List<ApplyEditCartypeResultView>> returnMap = new HashMap<String,List<ApplyEditCartypeResultView>>();
		returnMap.put("succList", succList);
		returnMap.put("failList", failList);
		if(StringUtils.isEmpty(cwbs)){
			return returnMap;
		}
		//存放订单号，记录处理结果，主要用于检查运单是否重复
		Map<String,String> allCwbResult = new HashMap<String,String>();
		String[] cwbstrs = cwbs.split(",");
		for(String cwb:cwbstrs){
			
			cwb = cwb.trim();
			//订单号为空，去掉
			if(StringUtils.isEmpty(cwb)){
				continue;
			}
			CwbOrder cwbOrder = cwbOrderService.getCwbByCwb(cwb);
			
			//单个申请的操作结果
			ApplyEditCartypeResultView opsResult = validateApplyInfo(cwb,cwbOrder,allCwbResult);
			//成功，则进行数据保存操作
			if(opsResult.isValid()){
				try{
					createApply(cwbOrder,cwbOrder.getTranscwb(),applyUser,applyCartype,remark);
				}catch(Exception e){
					opsResult.setValid(false);
					opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_EXCEPTION);
					logger.error("AEC01:创建货物类型修改申请出错，订单号："+cwbOrder.getCwb()+" 异常："+e);
				}
			}
			if(opsResult.isValid()){
				succList.add(opsResult);
			}else{
				failList.add(opsResult);
			}
		}
		return returnMap;
	}
	
	/**
	 * 创建货物类型变更申请记录
	 * @param cwbOrder
	 * @param transcwb
	 * @param applyUser 申请用户
	 * @param applyCartype 
	 */
	public void createApply(CwbOrder cwbOrder,String transcwb,User applyUser,String applyCartype,String remark){
		ApplyEditCartypeVO vo = new ApplyEditCartypeVO();
		vo.setCwb(cwbOrder.getCwb());
		vo.setTranscwb(cwbOrder.getTranscwb());
		vo.setDoType(cwbOrder.getCwbordertypeid());
		vo.setApplyUserid(applyUser.getUserid());
		vo.setCustomerid(cwbOrder.getCustomerid());
		vo.setApplyUsername(applyUser.getRealname());
		vo.setOriginalCartype(cwbOrder.getCartype());
		vo.setApplyCartype(applyCartype);
		vo.setApplyTime(DateTimeUtil.getNowTime());
		vo.setCarsize(cwbOrder.getCarsize());
		vo.setCarrealweight(cwbOrder.getCarrealweight());
		vo.setApplyBranchid(applyUser.getBranchid());
		vo.setRemark(remark);
		Branch branch = branchService.getBranchByBranchid(applyUser.getBranchid());
		if(branch!=null){
			vo.setApplyBranchname(branch.getBranchname());
		}
		Customer customer = customerDAO.getCustomerById(cwbOrder.getCustomerid());
		if(customer!=null){
			vo.setCustomername(customer.getCustomername());
		}
		applyEditCartypeDAO.createApplyEditCartype(vo);
			
		//applyEditCartypeMapper.insert(vo);
	}
	
	/**
	 * 检查申请记录的合法性
	 * @param cwb 订单号
	 * @param allCwb 目前所有订单号
	 * @return
	 */
	private ApplyEditCartypeResultView validateApplyInfo(String cwbOrTrancwb,CwbOrder cwbOrder,Map<String,String> allCwbReslut){
		ApplyEditCartypeResultView opsResult = new ApplyEditCartypeResultView();
		opsResult.setCwb(cwbOrTrancwb);
		opsResult.setValid(false);
		//订单不存在
		if(cwbOrder==null ||cwbOrder.getState() == CwbStateEnum.WUShuju.getValue()){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CWB_INVALID);
			return opsResult;
		}
		String cwb = cwbOrder.getCwb();
		//重复订单处理逻辑
		if(isExistCwb(cwb,allCwbReslut)){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CWB_REPEAT);
			return opsResult;
		}
		//订单的货物类型不为普件处理逻辑
		if(cwbOrder.getCartype().equals(CarTypeEnum.big.getValue())){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CAR_TYPE_BIG);
			return opsResult;
		}
		//重复提交申请逻辑
		List<ApplyEditCartypeVO> applyHistory = applyEditCartypeDAO.queryApplyEditCartype(cwb, 0, 0, 1);
		if(applyHistory!=null && applyHistory.size()>0){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_REPEAT);
			return opsResult;
		}
		//判断订单流程是否在反馈前
		if(isHitGuiBanShenHeLogic(cwbOrder)){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CWB_YIGUIBANSHENHE);
			return opsResult;
		}
		//已计费出账
		if(isBillCalculted(cwbOrder.getCwb())){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_BILL_CALCULATED);
			return opsResult;
		}
		//验证通过
		opsResult.setValid(true);
		return opsResult;
	}
	
	/**
	 * 判断订单流程是否在反馈前
	 * @author zhili01.liang  
	 * @param cwbOrder
	 * @return
	 */
	private boolean isHitGuiBanShenHeLogic(CwbOrder cwbOrder) {
		
		if(cwbOrder.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue() 
				&& (cwbOrder.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue() ||
						cwbOrder.getDeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue() ||
						cwbOrder.getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue() ||
						cwbOrder.getDeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()
						)){
			
			return true;
		}
		return false;

	}
	
	
	/**
	 * 检查allCwb是都包含cwb
	 * @param cwb
	 * @param allCwb
	 * @return
	 */
	private boolean isExistCwb(String cwb,Map<String,String> allCwbReslut){
		if(allCwbReslut==null){
			return false;
		}
		if(allCwbReslut.containsKey(cwb)){
			return true;
		}else{
			allCwbReslut.put(cwb, cwb);
			return false;
		}
	}
	
	public Page<ApplyEditCartypeVO> queryApplyEditCartype(String cwbs,String branchid,String applyUserid,
															Boolean isReview,Integer reviewStatus,
															String startApplyTime,String endApplyTime,
															int page, int pageSize){
		
		int begin = (page - 1) * pageSize;
		return applyEditCartypeDAO.queryApplyEditCartypeByPage(cwbs, branchid, applyUserid, isReview, reviewStatus, startApplyTime, endApplyTime, begin, pageSize);
	}
	
	public List<ApplyEditCartypeVO> queryApplyEditCartype(String cwbs,String branchid,String applyUserid,
															Boolean isReview,Integer reviewStatus,
															String startApplyTime,String endApplyTime){

		return applyEditCartypeDAO.queryApplyEditCartypeByList(cwbs, branchid, applyUserid, isReview, reviewStatus, startApplyTime, endApplyTime);
	}
	
	/**
	 * 审核成功
	 * @param cwbs
	 * @param applyUserid
	 * @param applyCartype
	 * @return Map,内含2个key: succList, failList
	 */
	public Map<String,List<ApplyEditCartypeResultView>> reviewPass(Long[] applyIds,User reviewUser){
		//成功列表
		List<ApplyEditCartypeResultView> succList = new ArrayList<ApplyEditCartypeResultView>();
		//失败列表
		List<ApplyEditCartypeResultView> failList = new ArrayList<ApplyEditCartypeResultView>();
		Map<String,List<ApplyEditCartypeResultView>> returnMap = new HashMap<String,List<ApplyEditCartypeResultView>>();
		returnMap.put("succList", succList);
		returnMap.put("failList", failList);
		if(applyIds==null || applyIds.length<1){
			return returnMap;
		}
		for(Long applyId : applyIds){
			ApplyEditCartypeVO applyEditCartype = applyEditCartypeDAO.getApplyEditCartypeVOById(applyId);
			//ApplyEditCartypeVO applyEditCartype = applyEditCartypeMapper.selectByPrimaryKey(applyId);
			CwbOrder cwbOrder = cwbOrderService.getCwbByCwb(applyEditCartype.getCwb());
			ApplyEditCartypeResultView reviewResult = validateReviewPass(cwbOrder, applyEditCartype);
			//成功，则进行数据保存操作
			if(reviewResult.isValid()){
				try{
					reviewPassProcess(applyEditCartype,cwbOrder,reviewUser);
				}catch(Exception e){
					logger.error("AEC02", "审核出错，审核记录信息："+applyEditCartype.toString()+" 异常："+e);
					reviewResult.setValid(false);
					reviewResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_EXCEPTION);
				}
				
			}
			if(reviewResult.isValid()){
				succList.add(reviewResult);
			}else{
				failList.add(reviewResult);
			}
			
		}
		return returnMap;

	}
	
	

	/**
	 * 检查申请记录的合法性
	 * @param cwb 订单号
	 * @param cwbOrder 订单实体
	 * @return
	 */
	private ApplyEditCartypeResultView validateReviewPass(CwbOrder cwbOrder,ApplyEditCartypeVO applyEditCartype){
		ApplyEditCartypeResultView opsResult = new ApplyEditCartypeResultView();
		opsResult.setCwb(applyEditCartype.getCwb());
		opsResult.setValid(false);
		//订单不存或失效处理逻辑
		if(cwbOrder==null || cwbOrder.getState() == CwbStateEnum.WUShuju.getValue()){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CWB_INVALID);
			return opsResult;
		}
		//订单的货物类型为大件处理逻辑
		if(cwbOrder.getCartype().equals(CarTypeEnum.big.getValue())){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_CAR_TYPE_BIG);
			return opsResult;
		}
		//记录已被审核
		if(applyEditCartype.getReviewStatus() > 0){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_REVIEWED);
			return opsResult;
		}
		//已计费出账
		if(isBillCalculted(cwbOrder.getCwb())){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_BILL_CALCULATED);
			return opsResult;
		}
		
		opsResult.setValid(true);
		return opsResult;
	}
	
	/**
	 * 审核同国所进行的DB操作
	 * @param applyEditCartype
	 * @param cwbOrder
	 */
	@Transactional
	private void reviewPassProcess(ApplyEditCartypeVO applyEditCartype, CwbOrder cwbOrder,User reviewUser){
		//更新申请记录，标记为审核不通过
		updateReviewStatus(applyEditCartype,reviewUser,ApplyEditCartypeReviewStatusEnum.pass.getValue());
		//更新订单的货物类型
		cwbOrderService.updateCwbCartype(cwbOrder.getCwb(),applyEditCartype.getApplyCartype());
		//添加订单操作记录
		String comment = String.format("类型从%s改为%s", applyEditCartype.getOriginalCartype(),applyEditCartype.getApplyCartype());
		cwbOrderService.createFloworder(reviewUser, reviewUser.getBranchid(), cwbOrder, 
										FlowOrderTypeEnum.XiuGaiHuoWuLeiXingTongGuo, comment, System.currentTimeMillis(),cwbOrder.getCwb(),false);
		
		//待处理：财务报表逻辑
		
	}
	
	
	/**
	 * 审核不通过
	 * @param cwbs
	 * @param applyUserid
	 * @param applyCartype
	 * @return Map,内含2个key: succList, failList
	 */
	public Map<String,List<ApplyEditCartypeResultView>> reviewDenied(Long[] applyIds,User reviewUser){
		//成功列表
		List<ApplyEditCartypeResultView> succList = new ArrayList<ApplyEditCartypeResultView>();
		//失败列表
		List<ApplyEditCartypeResultView> failList = new ArrayList<ApplyEditCartypeResultView>();
		Map<String,List<ApplyEditCartypeResultView>> returnMap = new HashMap<String,List<ApplyEditCartypeResultView>>();
		returnMap.put("succList", succList);
		returnMap.put("failList", failList);
		if(applyIds==null || applyIds.length<1){
			return returnMap;
		}
		for(Long applyId : applyIds){
			ApplyEditCartypeVO applyEditCartype = applyEditCartypeDAO.getApplyEditCartypeVOById(applyId);
			CwbOrder cwbOrder = cwbOrderService.getCwbByCwb(applyEditCartype.getCwb());
			ApplyEditCartypeResultView reviewResult = validateReviewDenied(applyEditCartype);
			//成功，则进行数据保存操作
			if(reviewResult.isValid()){
				try{
					reviewDeniedProcess(applyEditCartype,cwbOrder,reviewUser);
				}catch(Exception e){
					logger.error("AEC02", "审核出错，审核记录信息："+applyEditCartype.toString()+" 异常："+e);
					reviewResult.setValid(false);
					reviewResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_EXCEPTION);
				}
				
			}
			if(reviewResult.isValid()){
				succList.add(reviewResult);
			}else{
				failList.add(reviewResult);
			}
			
		}
		return returnMap;

	}
	
	

	/**
	 * 检查申请记录的合法性
	 * @param cwb 订单号
	 * @param cwbOrder 订单实体
	 * @return
	 */
	private ApplyEditCartypeResultView validateReviewDenied(ApplyEditCartypeVO applyEditCartype){
		ApplyEditCartypeResultView opsResult = new ApplyEditCartypeResultView();
		opsResult.setCwb(applyEditCartype.getCwb());
		opsResult.setValid(false);
		//已审核
		if(applyEditCartype.getReviewStatus() > 0){
			opsResult.setRemark(ApplyEditCartypeResultView.REMARK_APPLY_REVIEWED);
			return opsResult;
		}
		
		opsResult.setValid(true);
		return opsResult;
	}
	
	/**
	 * 审核同国所进行的DB操作
	 * @param applyEditCartype
	 * @param cwbOrder
	 */
	@Transactional
	private void reviewDeniedProcess(ApplyEditCartypeVO applyEditCartype, CwbOrder cwbOrder,User reviewUser){
		//更新申请记录，标记为审核不通过
		updateReviewStatus(applyEditCartype,reviewUser,ApplyEditCartypeReviewStatusEnum.denied.getValue());
		//待处理：写操作记录
		if(cwbOrder!=null && cwbOrder.getState() != CwbStateEnum.WUShuju.getValue()){
			//添加订单操作记录
			String comment = String.format("类型从%s改为%s", applyEditCartype.getOriginalCartype(),applyEditCartype.getApplyCartype());
			cwbOrderService.createFloworder(reviewUser, reviewUser.getBranchid(), cwbOrder, 
											FlowOrderTypeEnum.XiuGaiHuoWuLeiXingBuTongGuo, comment, System.currentTimeMillis(),cwbOrder.getCwb(),false);

		}
		
	}
	
	/**
	 * 设置审核状态
	 * @param applyEditCartype
	 * @param reviewUser
	 * @param reviewStatus
	 */
	private void updateReviewStatus(ApplyEditCartypeVO applyEditCartype,User reviewUser,int reviewStatus){
		applyEditCartype.setReviewUserid(reviewUser.getUserid());
		applyEditCartype.setReviewUsername(reviewUser.getRealname());
		applyEditCartype.setReviewTime(DateTimeUtil.getNowTime());
		applyEditCartype.setReviewStatus(reviewStatus);
		applyEditCartypeDAO.updateReview(applyEditCartype);
	}
	
	/**
	 * 订单是否已计费
	 * @author zhili01.liang  
	 * @param cwb
	 * @return
	 */
	private boolean isBillCalculted(String cwb){
		//小件员账单已计费
		List<DfBillFee> staffList = dfFeeService.findByCwbAndCalculted(cwb, 1, DeliveryFeeChargerType.STAFF.getValue(), 0, 1);
		if(staffList!=null && staffList.size()>0){
			return true;
		}
		//站点账单已计费
		List<DfBillFee> orgList = dfFeeService.findByCwbAndCalculted(cwb, 1, DeliveryFeeChargerType.ORG.getValue(), 0, 1);
		if(orgList!=null && orgList.size()>0){
			return true;
		}
		return false;
	}
	
	
	
}