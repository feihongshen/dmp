package cn.explink.service.express.tps.adaptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;

import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageDtModel;
import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageModel;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;
import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;
import com.vip.top.mainid.bizservice.MainIdService;
import com.vip.top.mainid.bizservice.MainIdServiceHelper;
import com.vip.top.mainid.bizservice.SeqBatchModel;

@Component("expressDeliveryOrderService")
public class PjDeliveryOrderServiceAdaptor implements ExpressDeliveryOrderService4TPS {

	private static final Logger LOGGER = LoggerFactory.getLogger(PjDeliveryOrderServiceAdaptor.class);

	private MainIdService mainIdService;

	@Autowired
	private TpsInterfaceExecutor tpsInterfaceExecutor;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbDAO cwbDAO;

	public MainIdService getMainIdService(){
		if(mainIdService == null){
			try{
				this.mainIdService = new MainIdServiceHelper.MainIdServiceClient();
				InvocationContext.Factory.getInstance().setTimeout(20000);
			}catch(Exception e){
				LOGGER.error("获取MainIdService异常："+e.getMessage());
				throw new RuntimeException("获取MainIdService异常："+e.getMessage());
			}
		}
		return mainIdService;
	}
	
	/*
	@PostConstruct
	public void init() {
		getMainIdService() = new MainIdServiceHelper.MainIdServiceClient();
		InvocationContext.Factory.getInstance().setTimeout(20000);
	}
	*/

	@Override
	public void assignDeliver(List<String> preOrderNoList, String branchCode, String operator) {
		List<PjSaleOrderFeedbackRequest> requestList = this.constructAssignDeliver(preOrderNoList, branchCode, operator);
		for (PjSaleOrderFeedbackRequest request : requestList) {
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PreOrderFeedBack);
			paramObj.setPreOrderfeedBack(request);
			// 发送JMS消息
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}
	}

	@Override
	public void superzone(List<String> preOrderNoList, String branchCode, String operator) {
		List<PjSaleOrderFeedbackRequest> requestList = this.constructSuperzoneRequest(preOrderNoList, branchCode, operator);
		for (PjSaleOrderFeedbackRequest request : requestList) {
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PreOrderFeedBack);
			paramObj.setPreOrderfeedBack(request);
			// 发送JMS消息
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}

	}

	private List<PjSaleOrderFeedbackRequest> constructSuperzoneRequest(List<String> preOrderNoList, String branchCode, String operator) {
		List<PjSaleOrderFeedbackRequest> requestList = new ArrayList<PjSaleOrderFeedbackRequest>();

		for (String preOrderNo : preOrderNoList) {
			PjSaleOrderFeedbackRequest request = new PjSaleOrderFeedbackRequest();
			request.setOperateType(FeedbackOperateTypeEnum.SiteSurpass.getValue());
			request.setOperateOrg(branchCode);
			request.setOperater(operator);
			request.setOperateTime(new Date().getTime());
			request.setReserveOrderNo(preOrderNo);

			requestList.add(request);
		}
		return requestList;
	}

	private List<PjSaleOrderFeedbackRequest> constructAssignDeliver(List<String> preOrderNoList, String branchCode, String operator) {
		List<PjSaleOrderFeedbackRequest> requestList = new ArrayList<PjSaleOrderFeedbackRequest>();

		for (String preOrderNo : preOrderNoList) {
			PjSaleOrderFeedbackRequest request = new PjSaleOrderFeedbackRequest();
			request.setOperateType(FeedbackOperateTypeEnum.DistributionCourier.getValue());
			request.setOperateOrg(branchCode);
			request.setOperater(operator);
			request.setOperateTime(new Date().getTime());
			request.setReserveOrderNo(preOrderNo);

			requestList.add(request);
		}
		return requestList;
	}

	@Override
	public String getPackageNo() {
		String seqNo = "";
		String seqRuleNo = "PACKAGE_NO";
		Map<String, String> contextVars = new HashMap<String, String>();
		try {
			seqNo = getMainIdService().getNextSeq(seqRuleNo, contextVars);
		} catch (OspException e) {
			PjDeliveryOrderServiceAdaptor.LOGGER.error(e.getMessage());
		}
		if (StringUtils.isEmpty(seqNo)) {
			PjDeliveryOrderServiceAdaptor.LOGGER.info("没有请求到包号！时间：" + new Date());
		}
		return seqNo;
	}

	@Override
	public List<String> getBatchPackageNo(int count) {
		String seqNo = "";
		String seqRuleNo = "PACKAGE_NO";
		Map<String, String> contextVars = new HashMap<String, String>();
		List<String> packageList = new ArrayList<String>();
		try {

			@SuppressWarnings("unused")
			SeqBatchModel seqNoa = getMainIdService().getNextBatchSeq(seqRuleNo, contextVars, count);
			String firstCode = seqNoa.getStartSeq();
			Long firstNum = Long.parseLong(firstCode.substring(1, firstCode.length()));
			//手动拼接包号
			for (int i = 0; i < count; i++) {

				packageList.add("P" + (firstNum + i));
			}
		} catch (OspException e) {
			PjDeliveryOrderServiceAdaptor.LOGGER.error(e.getMessage());
		}
		if (packageList.size() == 0) {
			PjDeliveryOrderServiceAdaptor.LOGGER.info("没有请求到包号！时间：" + new Date());
		}
		return packageList;
	}

	@Override
	public void upLoadPackageInfo(String packageNo, List<String> waybillNoList, String branchCode, String packageMan) {
		//Added by leoliao at 2016-08-09 增加日志输出
		LOGGER.info("upLoadPackageInfo:packageNo={}, tpsbranchcode={}, packageMan={}", packageNo, branchCode, packageMan);
		//Added end
		
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PackOpereate);
		PjDcPackageModel pjPackage = this.constructUploadPackageRequestParam(packageNo, waybillNoList, branchCode, packageMan);
		paramObj.setPackModel(pjPackage);
		// 发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		//List<CwbOrder> list = this.cwbDAO.getCwbOrderByCwbList(waybillNoList); //Comment by leoliao
		//增加快递单的判断，只有快递单才需要此操作--刘武强10.19
		//王海说调用合包接口后就不需要在调用状态反馈了--刘武强（10.23）
		/*for (CwbOrder temp : list) {
			if (temp.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
				this.executeTpsTransInterface4TransFeedBack(temp.getCwb(), packageMan, branchCode);
			}
		}*/
	}

	/**
	 * 合包调用tps运单反馈接口
	 * @param orders
	 */
	public void executeTpsTransInterface4TransFeedBack(String transNo, String packageMan, String branchCode) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		transNoFeedBack.setTransportNo(transNo);
		transNoFeedBack.setOperateOrg(this.branchDAO.getBranchByBranchcode(branchCode).size() > 0 ? this.branchDAO.getBranchByBranchcode(branchCode).get(0).getTpsbranchcode() : "");
		//add by wangzy 新增加下一站机构的参数
		transNoFeedBack.setOperater(packageMan);
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.PackingScan.getValue());//对应接口文档【枚举对应】
		transNoFeedBack.setReason("");
		/*//拼接描述
		JoinMessageVO contextVar = new JoinMessageVO();
		contextVar.setOperationType(TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
		contextVar.setStation(this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());//站点名称
		contextVar.setOperator(packageMan);
		contextVar.connectMessage();
		transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());*/
		paramObj.setTransNoFeedBack(transNoFeedBack);
		//发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);
	}

	private PjDcPackageModel constructUploadPackageRequestParam(String packageNo, List<String> waybillNoList, String branchCode, String packageMan) {
		PjDcPackageModel pjPackage = new PjDcPackageModel();
		// 包号
		pjPackage.setPackageNo(packageNo);
		// 打包站点
		pjPackage.setPackRdc(branchCode);
		// 封包时间
		pjPackage.setPackTime(new Date().getTime());
		// 打包人(名称)
		pjPackage.setPackMan(packageMan);
		// 总单数
		pjPackage.setTotalOrder(waybillNoList.size());
		// 总重量,没有传0
		pjPackage.setTotalWeight(0d);
		// 总体积,没有传0
		pjPackage.setTotalVolume(0d);

		List<PjDcPackageDtModel> pjPackageDtList = new ArrayList<PjDcPackageDtModel>();
		for (String waybillNo : waybillNoList) {
			PjDcPackageDtModel packageDt = new PjDcPackageDtModel();
			// 运单号
			packageDt.setTransportNo(waybillNo);
			pjPackageDtList.add(packageDt);
		}

		pjPackage.setDtList(pjPackageDtList);
		return pjPackage;
	}

	/**
	 *
	 * @Title: getSessionUser
	 * @description 获取登录用户
	 * @author 刘武强
	 * @date  2015年10月19日上午10:18:46
	 * @param  @return
	 * @return  User
	 * @throws
	 */
	public User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
}
