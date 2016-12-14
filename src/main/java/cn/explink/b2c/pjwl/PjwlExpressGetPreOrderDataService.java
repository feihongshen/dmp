package cn.explink.b2c.pjwl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.activemq.broker.view.DotFileInterceptorSupport;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.enumutil.express.ExpressDoServerStatusEnum;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.reserve.service.InfReserveOrderSModel;
import com.pjbest.deliveryorder.reserve.service.PjSaleOrderQueryRequest;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;

/**
 * 品竣物流获取预订单数据
 * 
 * @author jiangyu 2015年8月26日
 *
 */
@Service
public class PjwlExpressGetPreOrderDataService {

	@Autowired
	PreOrderDataImportDAO_Express preOrderDataImportDAO_Express;

	private Logger logger = LoggerFactory.getLogger(PjwlExpressGetPreOrderDataService.class);


	/**
	 * 获取预订单数据
	 * 
	 * @param key
	 * @return
	 */
	public int getNormalPreOrdersByPjwlExpress(String carrierCode) {

		int resFlag = 0;
		
		/*List<InfReserveOrderSModel> list_temp = new ArrayList<InfReserveOrderSModel>();
		for (int i = 0; i < 500; i++) {
			InfReserveOrderSModel model = new InfReserveOrderSModel();
			model.setReserveOrderNo("pre" + (System.currentTimeMillis()) + i);
			model.setCarrierCode("PJWL");
			model.setCustCode("PJWL" + i);
			model.setCnorMobile("15001288615");
			model.setCnorTel("76325489");
			model.setCnorAddr("北京市朝阳区双井街道");
			model.setCustName("jjyy");
			model.setReserveOrderStatus(10);
			list_temp.add(model);
		}
		extractedDataImport(list_temp);
		 */
		

		List<InfReserveOrderSModel> normalList = snifferExpressDataList(carrierCode, ExpressDoServerStatusEnum.MatchCarrier.getValue());// 换成枚举
		List<InfReserveOrderSModel> closedList = snifferExpressDataList(carrierCode, ExpressDoServerStatusEnum.ReserveClose.getValue());
		try {
			// 将list合并
			if (normalList != null) {
				normalList.addAll(closedList);
			} else {
				normalList = closedList;
			}
			// 提取品竣过来的数据插入到临时表
			extractedDataImport(normalList);
			if(null==normalList){
				resFlag=0;
			}else{
				resFlag=normalList.size();
			}
			
		} catch (Exception e) {
			logger.info("数据抓取成功，解析抓取到的数据异常，异常原因：{}", e.getMessage());
			resFlag = 0;
		}
		// DMP调用DO服务反馈抓取成功的预约单 需注释
		/*List<String> preOrderNos = new ArrayList<String>();
		if (normalList != null) {
			for (InfReserveOrderSModel record : normalList) {
				preOrderNos.add(record.getReserveOrderNo());
			}
			PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
			try {
				InvocationContext.Factory.getInstance().setTimeout(10000);
				pjDeliveryOrderService.makeSaleOrderSuccessList(preOrderNos);
			} catch (OspException e) {
				logger.info("DMP数据抓取成功将成功的抓取预订号返回TPS DO 操作异常，异常原因：{}", e.getMessage());
				
			}
		}*/

		
		return resFlag;
	}

	private List<InfReserveOrderSModel> snifferExpressDataList(String carrierCode, Integer reserveOrderStatus) {
		// 调用品竣快递接口 begin
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		List<InfReserveOrderSModel> list = new ArrayList<InfReserveOrderSModel>();
		try {
			InvocationContext.Factory.getInstance().setTimeout(10000);
			PjSaleOrderQueryRequest psqr = new PjSaleOrderQueryRequest();
			psqr.setCarrierCode(carrierCode);
			psqr.setReserveOrderStatus(reserveOrderStatus);
			// 获取品竣的预订单数据
			list = pjDeliveryOrderService.findSaleOrderList(psqr);
		} catch (Exception e) {
			logger.info("抓取预订单数据接口异常，异常原因:{}", e.getMessage());
		}

		return list;
	}

	/**
	 * 插入到临时表
	 * 
	 * @param orders
	 */
	@Transactional
	public void extractedDataImport(List<InfReserveOrderSModel> preOrderList) {
		for (InfReserveOrderSModel preOrderPj : preOrderList) {
			// 如果临时表中已存在对应记录 则 continue
			ExpressPreOrderDTO dto = preOrderDataImportDAO_Express.getPreOrderExpresstemp(preOrderPj.getReserveOrderNo());
			if (dto != null&&dto.getReserveOrderStatus().intValue()==preOrderPj.getReserveOrderStatus().intValue()) {//插入临时表的时候要判断状态
				logger.warn("接收到PJWL_Express发来数据，preOrderNo=" + preOrderPj.getReserveOrderNo() + "。但express_ops_preorder_temp表中已存在对应记录，系统将不做任何操作。");
				continue;
			}
			// 转换为预订单临时记录
			ExpressPreOrderDTO preOrderDto = new ExpressPreOrderDTO();
			convertPreOrderTempVoToPreOrderDTO(preOrderDto, preOrderPj);
			// 记录插入到临时表中
			preOrderDataImportDAO_Express.insertPreOrder_toTempTable(preOrderDto);
		}
	}

	/**
	 * 转换pj过来的预订单数据为dmp的preOrderDTO
	 * 
	 * @param preOrderDto
	 * @param preOrderPj
	 */
	private void convertPreOrderTempVoToPreOrderDTO(ExpressPreOrderDTO preOrderDto, InfReserveOrderSModel preOrderPj) {
		try {
			BeanUtils.copyProperties(preOrderDto, preOrderPj);
		} catch (IllegalAccessException e1) {
			logger.info("从TPS抓取预订单转换为DMP预订单数据转换异常,异常原因为：{}", e1.getMessage());
		} catch (InvocationTargetException e) {
			logger.info("从TPS抓取预订单转换为DMP预订单数据转换异常,异常原因为：{}", e.getMessage());
		}

	}
}
