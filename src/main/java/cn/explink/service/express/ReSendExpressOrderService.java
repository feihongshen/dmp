package cn.explink.service.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.express.ExpressTpsInterfaceExcepRecordDAO;
import cn.explink.domain.VO.express.ReSendExpressOrderVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.util.Page;
import cn.explink.util.Tools;
import cn.explink.util.express.ExpressTpsInterfaceException;

import com.alibaba.fastjson.JSONObject;
import com.pjbest.deliveryorder.bizservice.PjDeliverOrder4DMPRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPResponse;
import com.vip.osp.core.context.InvocationContext;

@Transactional
@Service
public class ReSendExpressOrderService extends ExpressCommonService {
	@Autowired
	private ExpressTpsInterfaceExcepRecordDAO expressTpsInterfaceExcepRecordDAO;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	@Autowired
	ExpressTpsInterfaceService ExpressTpsInterfaceService;

	/**
	 *
	 * @Title: getTpsInterfaceInfo
	 * @description 获接口表中创建运单接口的数据
	 * @author 刘武强
	 * @date  2015年11月11日上午11:16:23
	 * @param  @param beginTime
	 * @param  @param endTime
	 * @param  @param transNo
	 * @param  @param opeFlag
	 * @param  @param page
	 * @param  @param pageNumber
	 * @param  @return
	 * @return  Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getTpsInterfaceInfo(String beginTime, String endTime, String transNo, String opeFlag, long page, int pageNumber) {
		return this.expressTpsInterfaceExcepRecordDAO.getTpsInterfaceInfo(beginTime, endTime, transNo, opeFlag, page, Page.ONE_PAGE_NUMBER);
	}

	/**
	 *
	 * @Title: reSendTps
	 * @description 创建运单tps接口的重发逻辑
	 * @author 刘武强
	 * @date  2015年11月11日下午4:31:37
	 * @param  @param ids
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	public boolean reSendTps(String[] ids, List<String> success, List<String> failure) {
		boolean flag;
		String idStr = StringUtils.join(ids, ",");
		List<ReSendExpressOrderVO> list = this.expressTpsInterfaceExcepRecordDAO.getTpsInterfaceInfoByids(idStr);
		List<ReSendExpressOrderVO> resultVO = new ArrayList<ReSendExpressOrderVO>();

		try {
			//调用tps重发方法
			for (ReSendExpressOrderVO vo : list) {
				this.sendTps(vo, resultVO, success, failure);
			}
			this.expressTpsInterfaceExcepRecordDAO.updateTpsInterfaceExcepRecordBatch(resultVO);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 *
	 * @Title: sendTps
	 * @description 调用tps里运单创建接口
	 * @author 刘武强
	 * @date  2015年11月11日下午5:16:54
	 * @param  @param vo
	 * @param  @param resultVO
	 * @return  void
	 * @throws
	 */

	public void sendTps(ReSendExpressOrderVO vo, List<ReSendExpressOrderVO> resultVO, List<String> success, List<String> failure) {
		List<PjDeliverOrder4DMPRequest> requestlist = new ArrayList<PjDeliverOrder4DMPRequest>();
		ExpressOperationInfo params = new ExpressOperationInfo(ExpressOperationEnum.CreateTransNO);
		params = (ExpressOperationInfo) Tools.json2Object(vo.getMethodParams(), ExpressOperationInfo.class, false);
		requestlist.add(params.getRequestlist().get(0));
		List<PjDeliveryOrder4DMPResponse> result = new ArrayList<PjDeliveryOrder4DMPResponse>();
		try {
			result = this.ExpressTpsInterfaceService.createTransNo4Dmp(requestlist);
			vo.setErrMsg("");
			vo.setOpeFlag(YesOrNoStateEnum.Yes.getValue());
			resultVO.add(vo);
			success.add(vo.getTransNo());
			this.logger.info("调用TPS接口正常，查询预约单轨迹请求Tps后的结果为：{}", result);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setErrMsg(e.getMessage());
			vo.setOpeFlag(YesOrNoStateEnum.No.getValue());
			failure.add(vo.getTransNo());
			resultVO.add(vo);
		}

	}

	/**
	 * 快递单操作重推TPS
	 * @author leo01.liao
	 * @return
	 */
	public void resendToTps(){
		try{
			int excuteCount = 20;
			int maxCount    = 2000;
			List<ReSendExpressOrderVO> listExpressOrderVO = this.expressTpsInterfaceExcepRecordDAO.getTpsInterfaceInfoForResend(excuteCount, maxCount);
			if(listExpressOrderVO == null || listExpressOrderVO.isEmpty()){
				this.logger.info("快递单操作重推TPS:没有符合条件的需要重推的数据！");
				return;
			}
			
			for(ReSendExpressOrderVO expressOrderVO : listExpressOrderVO){
				try{
					this.logger.info("快递单操作重推TPS:trans_no={},id={},operationType={},methodParams={}", 
							         expressOrderVO.getTransNo(), expressOrderVO.getId(), expressOrderVO.getOperationType(), expressOrderVO.getMethodParams());
					ExpressOperationInfo operationInfo = (ExpressOperationInfo) Tools.json2Object(expressOrderVO.getMethodParams(), ExpressOperationInfo.class, false);
					operationInfo.setOperationType(expressOrderVO.getOperationType());
					
					this.logger.info("快递单操作重推TPS:请求参数requestlist={}", JSONObject.toJSONString(operationInfo));
					
					Map<String, Object> mapResult = this.doResendToTps(operationInfo);
					
					this.logger.info("快递单操作重推TPS:返回结果mapResult={}", JSONObject.toJSONString(mapResult));
					
					this.expressTpsInterfaceExcepRecordDAO.updateTpsInterfaceForResend(expressOrderVO.getId(), "", YesOrNoStateEnum.Yes.getValue());
				}catch(Exception ee){
					this.logger.error("快递单(trans_no="+expressOrderVO.getTransNo()+",id="+expressOrderVO.getId()+")操作重推TPS异常", ee);
					this.expressTpsInterfaceExcepRecordDAO.updateTpsInterfaceForResend(expressOrderVO.getId(), ee.getMessage(), YesOrNoStateEnum.Error.getValue());
				}				
			}
		}catch(Exception ex){
			this.logger.error("快递单操作重推TPS异常", ex);
		}
	}
	
	/**
	 * 调用TPS的OSP服务接口：根据不同的操作环节选择适合的接口
	 * @author leo01.liao
	 * @param  operationInfo
	 * @return Map<String, Object>
	 * @throws ExpressTpsInterfaceException
	 */
	private Map<String, Object> doResendToTps(ExpressOperationInfo operationInfo) throws ExpressTpsInterfaceException {
		//存放返回结果
		Map<String, Object> mapResult = new ConcurrentHashMap<String, Object>();
		mapResult.clear();
		
		if(operationInfo == null){
			return mapResult;
		}

		//根据不同的业务场景调用不同的服务
		InvocationContext.Factory.getInstance().setTimeout(10000);
		if (ExpressOperationEnum.PreOrderFeedBack.getValue().equals(operationInfo.getOperationType())) {
			//揽件反馈接口
			Boolean result = this.ExpressTpsInterfaceService.preOrderFeedBackProcess(operationInfo.getPreOrderfeedBack());
			mapResult.put(ExpressOperationEnum.PreOrderFeedBack.getUniqueCode(), result);
			this.logger.info("(快递单操作重推TPS)调用TPS接口正常，反馈操作的结果回传到Tps后的结果为：" + result);
		} else if (ExpressOperationEnum.CreateTransNO.getValue().equals(operationInfo.getOperationType())) {
			//创建运单接口
			List<PjDeliveryOrder4DMPResponse> result = this.ExpressTpsInterfaceService.createTransNo4Dmp(operationInfo.getRequestlist());
			mapResult.put(ExpressOperationEnum.CreateTransNO.getUniqueCode(), result);
			this.logger.info("(快递单操作重推TPS)调用TPS接口正常，创建运单接口请求Tps后的结果为：{}", result);
		} else if (ExpressOperationEnum.TransNOFeedBack.getValue().equals(operationInfo.getOperationType())) {
			//运单状态反馈接口
			Boolean resultFlag = this.ExpressTpsInterfaceService.transNoFeedBackProcess(operationInfo.getTransNoFeedBack());
			mapResult.put(ExpressOperationEnum.TransNOFeedBack.getUniqueCode(), resultFlag);
			this.logger.info("(快递单操作重推TPS)调用TPS接口正常，运单状态反馈接口请求Tps后的结果为：{}", resultFlag);
		}else if (ExpressOperationEnum.PackOpereate.getValue().equals(operationInfo.getOperationType())) {
			//上传打包信息接口
			Boolean resultFlag = this.ExpressTpsInterfaceService.handlePackingInfo(operationInfo.getPackModel());
			mapResult.put(ExpressOperationEnum.PackOpereate.getUniqueCode(), resultFlag);
			this.logger.info("(快递单操作重推TPS)调用TPS接口正常，上传打包信息接口请求Tps后的结果为：{}", resultFlag);
		} else if (ExpressOperationEnum.UnPackOperate.getValue().equals(operationInfo.getOperationType())) {
			//上传拆包信息接口
			Boolean resultFlag = this.ExpressTpsInterfaceService.handleUnPackingInfo(operationInfo.getUnPackRequest());
			mapResult.put(ExpressOperationEnum.UnPackOperate.getUniqueCode(), resultFlag);
			this.logger.info("(快递单操作重推TPS)调用TPS接口正常，上传拆包信息接口请求Tps后的结果为：{}", resultFlag);
		}else{
			this.logger.info("(快递单操作重推TPS)其他操作不重推TPS：operationType={}", operationInfo.getOperationType());
		}

		return mapResult;
	}
	
}
