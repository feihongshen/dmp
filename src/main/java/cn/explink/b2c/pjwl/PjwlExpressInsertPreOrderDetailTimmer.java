package cn.explink.b2c.pjwl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.enumutil.express.ExpressDoServerStatusEnum;
import cn.explink.service.express.PreOrderService;

@Service
public class PjwlExpressInsertPreOrderDetailTimmer {

	@Autowired
	PjwlExpressGetPreOrderDataService pjwlExpressGetPreOrderDataService;
	@Autowired
	PreOrderDataImportDAO_Express preOrderDataImportDAO_Express;
	@Autowired
	PreOrderDao preOrderDao;

	@Autowired
	PreOrderService preOrderService;

	private Logger logger = LoggerFactory.getLogger(PjwlExpressInsertPreOrderDetailTimmer.class);

	/**
	 * 选择临时表中的记录插入到预订单表中
	 * @param addressInfo 
	 * 
	 * @param key
	 */
	public void selectTempAndInsertToPreOrderDetail(Map<String, Map<String, String>> addressInfo) {
		List<ExpressPreOrderDTO> preOrderList = preOrderDataImportDAO_Express.getPreOrderTempByKeys();
		if (preOrderList == null || preOrderList.size() == 0) {
			return;
		}
		// 分组操作数据
		int k = 1;
		int batch = 50;
		while (true) {
			int fromIndex = (k - 1) * batch;
			if (fromIndex >= preOrderList.size()) {
				break;
			}
			int toIdx = k * batch;
			if (k * batch > preOrderList.size()) {
				toIdx = preOrderList.size();
			}
			List<ExpressPreOrderDTO> subList = preOrderList.subList(fromIndex, toIdx);
			importSubList(subList,addressInfo);
			k++;
		}
	}

	@Transactional
	public void importSubList(List<ExpressPreOrderDTO> preOrderList, Map<String, Map<String, String>> addressInfo) {
		for (ExpressPreOrderDTO preOrder : preOrderList) {
			try {
				if (ExpressDoServerStatusEnum.MatchCarrier.getValue().equals(preOrder.getReserveOrderStatus())) {
					insertSiglePreOrder(preOrder,addressInfo);
				}else if (ExpressDoServerStatusEnum.ReserveClose.getValue().equals(preOrder.getReserveOrderStatus())) {
					updateSingleOrder(preOrder);
				}
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!preOrderNo=" + preOrder.getReserveOrderNo(), e);
			}
			preOrderDataImportDAO_Express.update_preOrderDetailTempByPreOrderNo(preOrder.getReserveOrderNo());
		}
	}

	/**
	 * 单一的更新操作
	 * @param preOrder
	 */
	private void updateSingleOrder(ExpressPreOrderDTO preOrderDto) {
		ExpressPreOrder preOrder = preOrderDao.getPreOrderRecordByReserveOrderNo(preOrderDto.getReserveOrderNo());
		if (preOrder != null) {
			preOrderService.updatePreOrderState(preOrderDto);
			logger.info("定时器临时表中抓取的关闭的数据更新detail表记录为关闭状态操作成功!preOrderNo={}", preOrderDto.getReserveOrderNo());
		} else {
			logger.warn("detail表中查询没有此预订单的数据,或者DMP已经将预订单置为关闭状态,已过滤!预订单号：{}", preOrderDto.getReserveOrderNo());
		}
	}

	/**
	 * 单一插入操作
	 * @param addressInfo 
	 * @param pjwlExpress
	 * @param preOrder
	 */
	private void insertSiglePreOrder(ExpressPreOrderDTO preOrderDto, Map<String, Map<String, String>> addressInfo) {
		ExpressPreOrder preOrder = preOrderDao.getPreOrderRecordByReserveOrderNo(preOrderDto.getReserveOrderNo());
		if (preOrder != null) {
//			preOrderService.updateAllInfo(preOrderDto,addressInfo);
			preOrderService.deleteAndInsert(preOrderDto,addressInfo);
			logger.warn("查询临时表-检测到有重复数据,已覆盖所有信息!预订单号：{}", preOrderDto.getReserveOrderNo());
		} else {
			preOrderService.insertPreOrder(preOrderDto,addressInfo);
			logger.info("定时器临时表插入detail表成功!preOrderNo={}", preOrderDto.getReserveOrderNo());
		}
	}

}
