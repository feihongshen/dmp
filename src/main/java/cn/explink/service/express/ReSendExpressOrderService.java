package cn.explink.service.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderResponse;

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
		List<PjDeliveryOrderRequest> requestlist = new ArrayList<PjDeliveryOrderRequest>();
		ExpressOperationInfo params = new ExpressOperationInfo(ExpressOperationEnum.CreateTransNO);
		params = (ExpressOperationInfo) Tools.json2Object(vo.getMethodParams(), ExpressOperationInfo.class, false);
		requestlist.add(params.getRequestlist().get(0));
		List<PjDeliveryOrderResponse> result = new ArrayList<PjDeliveryOrderResponse>();
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

}
