package cn.explink.b2c.pjwl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.util.Tools;

import com.pjbest.deliveryorder.bizservice.PjDeliverOrder4DMPRequest;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrder4DMPServiceHelper;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;
/**
 * 获取运单数据
 * @author jiangyu 2015年9月9日
 *
 */
@Component
public class PjwlExpressGetTransNoDataService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ExpressCwbOrderDataImportDAO expressCwbOrderDataImportDAO;
	
	public int getNormalTransNoDatasByPjwlExpress(String carrierCode) {
		int resFlag = 0;
		PjDeliveryOrder4DMPService pjDeliveryOrder4DMPService = new PjDeliveryOrder4DMPServiceHelper.PjDeliveryOrder4DMPServiceClient();
		InvocationContext.Factory.getInstance().setTimeout(100000);
		List<PjDeliverOrder4DMPRequest> result = new ArrayList<PjDeliverOrder4DMPRequest>();
		List<String> transDataIds = new ArrayList<String>();
		try {
			result = pjDeliveryOrder4DMPService.getDeliveryOrder(carrierCode);
			if (result!=null&&result.size()>0) {
				//插入数据的处理
				transDataIds = this.extractedDataImport(result);
			}
			if(null==transDataIds){
				resFlag=0;
			}else{
				resFlag=transDataIds.size();
			}

		} catch (OspException e) {
			resFlag = 0;
			logger.info("DMP抓取TPS运单数据成功，解析抓取到的数据异常，异常原因：{}", e.getMessage());
		}
		
		//抓取运单反馈接口
		try {
			pjDeliveryOrder4DMPService.makeDeliveryOrderSuccessList(transDataIds);
		} catch (OspException e) {
			logger.info("DMP抓取TPS运单反馈接口异常，异常原因：{}", e.getMessage());
		}
		
		return resFlag;
	}

	/**
	 * 插入到临时表
	 * 
	 * @param orders
	 */
	@Transactional
	public List<String> extractedDataImport(List<PjDeliverOrder4DMPRequest> transOrderDatas) {
		List<String> transDataIds = new ArrayList<String>();
		for (PjDeliverOrder4DMPRequest transOrderPj : transOrderDatas) {
			// 如果临时表中已存在对应记录 则 continue
			if (expressCwbOrderDataImportDAO.getCwbOrderExpresstemp(transOrderPj.getId()) != null) {
				logger.warn("接收到PJWL_Express_TransData发来数据，transNo=" + transOrderPj.getTransportNo() + "。但express_ops_cwb_exprss_detail_temp表中已存在对应记录，系统将不做任何操作。");
				continue;
			}
			// 转换为订单临时记录
			ExpressCwbOrderDTO transOrderDto = new ExpressCwbOrderDTO();
			convertTransOrderTempVoToTransOrderDTO(transOrderDto, transOrderPj);
			// 记录插入到临时表中
			expressCwbOrderDataImportDAO.insertTransOrder_toTempTable(transOrderDto);
			transDataIds.add(transOrderPj.getId());
		}
		return transDataIds;
	}
	/**
	 * 数据转换为dto
	 * @param transOrderDto
	 * @param transOrderPj
	 */
	private void convertTransOrderTempVoToTransOrderDTO(ExpressCwbOrderDTO transOrderDto, PjDeliverOrder4DMPRequest transOrderPj) {
		/*
		try {
			BeanUtils.copyProperties(transOrderDto, transOrderPj);
		} catch (IllegalAccessException e1) {
			logger.info("从TPS抓取运单转换为DMP订单数据转换异常,异常原因为：{}", e1.getMessage());
		} catch (InvocationTargetException e) {
			logger.info("从TPS抓取运单转换为DMP订单数据转换异常,异常原因为：{}", e.getMessage());
		}
		*/
		try {
			Tools.copyBeanValueNull2Default(transOrderDto,transOrderPj);
		} catch (Exception e) {
			logger.info("从TPS抓取运单转换为DMP订单数据转换异常,异常原因为：{}", e.getMessage());
		}
	}

}
