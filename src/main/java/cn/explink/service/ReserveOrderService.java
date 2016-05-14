package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pjbest.deliveryorder.bizservice.PjReserveOrderService;
import com.pjbest.deliveryorder.bizservice.PjReserveOrderServiceHelper;
import com.pjbest.deliveryorder.service.OmReserveOrderModel;
import com.pjbest.deliveryorder.service.PjReserveOrderPageModel;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

import cn.explink.domain.VO.ReserveOrderPageVo;
import cn.explink.domain.VO.ReserveOrderVo;
import cn.explink.util.JsonUtil;

/**
 * 预约单Service
 * @date 2016年5月13日 下午6:11:56
 */
@Service
public class ReserveOrderService {
	
private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int OSP_INVOKE_TIMEOUT = 60000;
	
	/**
	 * 获取预约单列表
	 * @date 2016年5月13日 下午6:13:22
	 * @return
	 */
	public ReserveOrderPageVo getReserveOrderPage(int page, int rows) {
		OmReserveOrderModel omReserveOrderModel = new OmReserveOrderModel();
		
		// 记录入库数据
		try {
			logger.info("omReserveOrderModel:{}", JsonUtil.translateToJson(omReserveOrderModel));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT);
		PjReserveOrderService pjReserveOrderService = new PjReserveOrderServiceHelper.PjReserveOrderServiceClient();
		PjReserveOrderPageModel pjReserveOrderPageModel = null;
		try {
			pjReserveOrderPageModel = pjReserveOrderService.getReserveOrders(omReserveOrderModel, page - 1, rows);
			// 返回数据记录
			logger.info("pjReserveOrderPageModel:{}", pjReserveOrderPageModel.getReserveOrders().size());
		} catch (OspException e) {
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
			Long requireTimeMs = po.getRequireTime();
			if (requireTimeMs != null) {
				Date requireTime = new Date(requireTimeMs);
				vo.setRequireTime(requireTime);
				vo.setRequireTimeStr(sdf.format(requireTime));
			}
			vo.setReserveOrderStatus(po.getReserveOrderStatus());
			vo.setReserveOrderStatusName(po.getReserveOrderStatusName());
			vo.setReason(po.getReason());
			vo.setTransportNo(po.getTransportNo());
			vo.setAcceptOrg(po.getAcceptOrg());
			vo.setAcceptOrgName(po.getAcceptOrgName());
			vo.setCnorRemark(po.getCnorRemark());
			voList.add(vo);
		}
		// 封装分页信息
		ReserveOrderPageVo reserveOrderPageVo = new ReserveOrderPageVo();
		reserveOrderPageVo.setTotalRecord(pjReserveOrderPageModel.getTotalRecord());
		reserveOrderPageVo.setReserveOrderVoList(voList);
		return reserveOrderPageVo;
	}
}
