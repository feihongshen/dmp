package cn.explink.b2c.auto.order.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.explink.b2c.auto.order.dao.ExpressOrderDao;
import cn.explink.b2c.auto.order.domain.ExpressDetailTemp;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.service.addressmatch.AddressMatchExpressService;
import cn.explink.service.express.TpsInterfaceExecutor;

import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;

/**
 * 快递单处理
 * @author jian.xie
 *
 */
@Service
public class ExpressOrderService {
	
	private Logger logger = LoggerFactory.getLogger(ExpressOrderService.class);
	
	@Autowired
	@Qualifier("inter.expressOrderDao")
	ExpressOrderDao expressOrderDao;
	
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	
	@Autowired
	AddressMatchExpressService addressMatchExpressService;
	
	@Autowired
	BranchDAO branchDAO;
	
	@Autowired
	UserDAO userDAO;
	
	/**
	 * 根据 tpsTranId查询临时表，
	 * @param tpsTranId
	 * @return 不存在返回null
	 */
	public ExpressDetailTemp  getCwbOrderExpresstemp(String tpsTranId){
		return expressOrderDao.getCwbOrderExpresstemp(tpsTranId);
	}
	
	/**
	 * 新增
	 */
	public void insertExpressDetailTemp(ExpressDetailTemp expressDetailTemp){
		expressOrderDao.insertExpressDetailTemp(expressDetailTemp);
	}
	
	/**
	 * 更新特定字段
	 */
	public void updateExpressDetailTemp(ExpressDetailTemp expressDetailTemp){
		expressOrderDao.updateExpressDetailTemp(expressDetailTemp);
	}
	
	/**
	 * 更改成已转业务
	 */
	public void updateExpressDetailTempForOver(String tpsTransId){
		expressOrderDao.updateExpressDetailTempForOver(tpsTransId);
	}
	
	/**
	 * 转业务 
	 */
	public void expressOrderTransfer(){
		List<ExpressDetailTemp> expressDetailTempList = expressOrderDao.getExpressDetailTempListNotOver(1);
		if (CollectionUtils.isEmpty(expressDetailTempList)) {
			logger.info("无快递订单需要转业务");
			return;
		}

		// 分组操作数据
		int k = 1;
		int batch = 50;
		while (true) {
			int fromIndex = (k - 1) * batch;
			if (fromIndex >= expressDetailTempList.size()) {
				break;
			}
			int toIdx = k * batch;
			if (k * batch > expressDetailTempList.size()) {
				toIdx = expressDetailTempList.size();
			}
			List<ExpressDetailTemp> subList = expressDetailTempList.subList(fromIndex, toIdx);
			importSubList(subList);
			k++;
		}
		
	}
	
	public void importSubList(List<ExpressDetailTemp> expressDetailTempList) {
		for (ExpressDetailTemp expressDetailTemp : expressDetailTempList) {
			try {
				insertSigleCwbOrder(expressDetailTemp);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("定时器临时表插入或修改方法执行异常!transOrderNo=" + expressDetailTemp.getTransportNo(), e);
			}
		}
	}
	
	/**
	 * 单一插入操作
	 * @param pjwlExpress
	 * @param preOrder
	 */
	@Transactional
	private void insertSigleCwbOrder(ExpressDetailTemp expressDetailTemp) {
		CwbOrder cwbOrder = expressOrderDao.getCwbOrderRecordByReserveOrderNo(expressDetailTemp.getTransportNo());
		if (cwbOrder != null) {
			logger.warn("查询临时表-检测到有重复数据,已过滤!订单号：{}", cwbOrder.getCwb());
		} else {
			//同步匹配站点
			Branch branch = null;
			if(StringUtils.isNotEmpty(expressDetailTemp.getCneeAddr())){
				branch = this.matchDeliveryBranch(expressDetailTemp.getTransportNo(),expressDetailTemp.getCneeAddr());
			}
			// 
			List<Branch> listBranch  = branchDAO.getBranchByTpsBranchcode(expressDetailTemp.getAcceptDept());
			Branch acceptBranch = null;
			if(!CollectionUtils.isEmpty(listBranch)){
				acceptBranch = listBranch.get(0);				
			}
			// 小件员
			User user = userDAO.getUserByUsername(expressDetailTemp.getAcceptOperator());
			
			//插入主表
			expressOrderDao.insertCwbOrder(expressDetailTemp, branch, acceptBranch, user);
			logger.info("定时器临时表插入detail表成功!cwb={}", expressDetailTemp.getTransportNo());
		}
		//更新记录
		expressOrderDao.updateExpressDetailTempForOver(expressDetailTemp.getTpsTransId());
	}
	
	/**
	 * 匹配站点
	 * @param cneeAddr 收件人地址
	 * @return
	 */
	private Branch matchDeliveryBranch(String cwb,String cneeAddr) {
		Branch branch = null;
		ExtralInfo4Address info = new ExtralInfo4Address(cwb,1L,cneeAddr);
		//匹配站点
		branch = addressMatchExpressService.matchAddress4SinfferTransData(info);
		if (null!=branch) {
			executeTpsFeedBackInterface(branch,cwb);
		}
		return branch;
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
