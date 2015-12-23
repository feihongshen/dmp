package cn.explink.b2c.pjwl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.service.addressmatch.AddressMatchExpressService;
import cn.explink.service.express.TpsInterfaceExecutor;
/**
 * 获取运单数据
 * @author jiangyu 2015年9月9日
 *
 */
@Service
public class PjwlExpressInsertCwbOrderDetailTimmer {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ExpressCwbOrderDataImportDAO expressCwbOrderDataImportDAO;
	
	@Autowired
	AddressMatchExpressService addressMatchExpressService;
	
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;

	public void selectTempAndInsertToCwbOrderDetail() {
		List<ExpressCwbOrderDTO> transOrderList = expressCwbOrderDataImportDAO.getTransOrderTempByKeys();
		if (transOrderList == null || transOrderList.size() == 0) {
			return;
		}

		// 分组操作数据
		int k = 1;
		int batch = 50;
		while (true) {
			int fromIndex = (k - 1) * batch;
			if (fromIndex >= transOrderList.size()) {
				break;
			}
			int toIdx = k * batch;
			if (k * batch > transOrderList.size()) {
				toIdx = transOrderList.size();
			}
			List<ExpressCwbOrderDTO> subList = transOrderList.subList(fromIndex, toIdx);
			importSubList(subList);
			k++;
		}
	}
	
	
	@Transactional
	public void importSubList(List<ExpressCwbOrderDTO> transOrderList) {
		for (ExpressCwbOrderDTO transOrder : transOrderList) {
			try {
				insertSigleCwbOrder(transOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!transOrderNo=" + transOrder.getTransportNo(), e);
			}
		}
	}

	/**
	 * 单一插入操作
	 * @param pjwlExpress
	 * @param preOrder
	 */
	private void insertSigleCwbOrder(ExpressCwbOrderDTO transOrder) {
		CwbOrder cwbOrder = expressCwbOrderDataImportDAO.getCwbOrderRecordByReserveOrderNo(transOrder.getTransportNo());
		if (cwbOrder != null) {
			logger.warn("查询临时表-检测到有重复数据,已过滤!订单号：{}", cwbOrder.getCwb());
		} else {
			//同步匹配站点
			Long deliverBranchId = this.matchDeliveryBranch(transOrder.getTransportNo(),transOrder.getCneeAddr());
			//插入主表
			expressCwbOrderDataImportDAO.insertCwbOrder(transOrder,deliverBranchId);
			logger.info("定时器临时表插入detail表成功!cwb={}", transOrder.getTransportNo());
		}
		//更新记录
		expressCwbOrderDataImportDAO.updateTransDataTempRecord(transOrder.getId());
	}

	/**
	 * 匹配站点
	 * @param cneeAddr 收件人地址
	 * @return
	 */
	private Long matchDeliveryBranch(String cwb,String cneeAddr) {
		Long branchId = 0L;
		ExtralInfo4Address info = new ExtralInfo4Address(cwb,1L,cneeAddr);
		//匹配站点
		Branch branch = addressMatchExpressService.matchAddress4SinfferTransData(info);
		if (null!=branch) {
			branchId = branch.getBranchid();
			executeTpsFeedBackInterface(branch,cwb);
		}
		return branchId;
	}

	/**
	 * 调用tps接口
	 * @param branch
	 * @param preOrderNo
	 */
	private void executeTpsFeedBackInterface(Branch branch, String preOrderNo) {
		PjSaleOrderFeedbackRequest request = new PjSaleOrderFeedbackRequest();
		request.setOperateOrg(branch.getTpsbranchcode());//机构编码
		request.setReserveOrderNo(preOrderNo);//预订单编号
		request.setOperater("");//操作人
		request.setOperateTime(new Date().getTime());//操作时间
		//2.将反馈状态通知到TPS
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PreOrderFeedBack);
		//设置预订单号
		paramObj.setReserveOrderNo(request.getReserveOrderNo());
		paramObj.setPreOrderfeedBack(request);
		//发送JMS消息
		tpsInterfaceExecutor.executTpsInterface(paramObj);
	}
	
	

}
