package cn.explink.b2c.auto.order.handle;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.domain.ExpressDetailTemp;
import cn.explink.b2c.auto.order.mq.TpsOrderMQCallback;
import cn.explink.b2c.auto.order.service.ExpressOrderService;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendDetailVO;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

import com.pjbest.deliveryorder.wcs.constants.WcsContants.CmdType;

/**
 * 快递订单处理
 * @author jian.xie
 *
 */
@Component("expressOrderHandler")
public class ExpressOrderHandler implements IOrderHandler {
	
	protected Logger logger = LoggerFactory.getLogger(TpsOrderMQCallback.class);

	@Autowired
	ExpressOrderService expressOrderService;
	
	@Autowired
	SystemInstallDAO systemInstallDAO;
	
	@Override
	@Transactional
	public void dealWith(InfDmpOrderSendVO orderSend) {		
		// 判断接口是否开启	
		if(!systemInstallDAO.isBoolenInstall("openExpressMQInter")){
			logger.info("ExpressOrderHandler 接口未开启");
			return;
		}		
		
		// 针对快递类型的判断
		if(!verify(orderSend)){
			return ;
		}
		
		ExpressDetailTemp expressDetailTemp = expressOrderService.getCwbOrderExpresstemp(orderSend.getMessageId());		
		ExpressDetailTemp expressDetailTemp_New = getExprssDetailTemp(orderSend);
		//新增
		if(CmdType.NEW.equals(orderSend.getCmdType())){
			if(expressDetailTemp != null){
				// 已存在，不能新增
				logger.info("快递已存在，单号{}", orderSend.getTransportNo());
				return;
			}
			expressOrderService.insertExpressDetailTemp(expressDetailTemp_New);
			// 更新明细
		}else if (CmdType.EDIT.equals(orderSend.getCmdType())){// 更新
			if(expressDetailTemp == null){
				// 不存在，不能更新
				logger.info("快递不存在, 单号{}", orderSend.getTransportNo());
				return ;				
			} 
			expressOrderService.updateExpressDetailTemp(expressDetailTemp_New);
		}		
	}
	
	/**
	 * 针对快递类型的效验
	 * @param orderSend
	 * @return
	 */
	private boolean verify(InfDmpOrderSendVO orderSend) {
		
		
		return true;
	}

	/**
	 * 把InfDmpOrderSendVO转成ExprssDetailTemp方便后续操作
	 */
	private ExpressDetailTemp getExprssDetailTemp(InfDmpOrderSendVO orderSend){
		ExpressDetailTemp expressDetailTemp = new ExpressDetailTemp();
		expressDetailTemp.setTransportNo(orderSend.getTransportNo());
		if(StringUtils.isEmpty(orderSend.getCustOrderNo())){
			expressDetailTemp.setCustOrderNo(orderSend.getTransportNo());
		}else{
			expressDetailTemp.setCustOrderNo(orderSend.getCustOrderNo());
		}
		expressDetailTemp.setCustCode(orderSend.getCustCode());
		expressDetailTemp.setAcceptDept(orderSend.getOrgCode());// 站点code
		expressDetailTemp.setAcceptOperator(orderSend.getExpress().getAcceptOperator());// 揽件员
		
		expressDetailTemp.setCnorProv(orderSend.getCnorProv());
		expressDetailTemp.setCnorCity(orderSend.getCnorCity());
		expressDetailTemp.setCnorRegion(orderSend.getCnorRegion());
		expressDetailTemp.setCnorTown(orderSend.getCnorTown());
		expressDetailTemp.setCnorAddr(orderSend.getCnorAddr());
		expressDetailTemp.setCnorName(orderSend.getCnorContacts());// 寄件人
		expressDetailTemp.setCnorMobile(orderSend.getCnorMobile());
		expressDetailTemp.setCnorTel(orderSend.getCnorTel());
		
		expressDetailTemp.setCneeProv(orderSend.getCneeProv());
		expressDetailTemp.setCneeCity(orderSend.getCneeCity());
		expressDetailTemp.setCneeRegion(orderSend.getCneeRegion());
		expressDetailTemp.setCneeTown(orderSend.getCneeTown());
		expressDetailTemp.setCneeAddr(orderSend.getCneeAddr());
		expressDetailTemp.setCneeName(orderSend.getCneeContacts());// 收货人
		expressDetailTemp.setCneeMobile(orderSend.getCneeMobile());
		expressDetailTemp.setCneeTel(orderSend.getCneeTel());
		
		expressDetailTemp.setCneePeriod(orderSend.getExpress().getCneePeriod());// 送货时间类型
		expressDetailTemp.setCneeCertificate(orderSend.getExpress().getCneeCertificate());
		expressDetailTemp.setCneeNo(orderSend.getExpress().getCneeCorpNo());
		expressDetailTemp.setIsCod(orderSend.getIsCod() == true ? 1 : 0);
		expressDetailTemp.setCodAmount(new BigDecimal(orderSend.getCodAmount()));
		expressDetailTemp.setTotalBox(orderSend.getTotalPack());// 合计箱数
		expressDetailTemp.setTotalWeight(new BigDecimal(orderSend.getOriginalWeight())); // 重量
		expressDetailTemp.setCalculateWeight(new BigDecimal(orderSend.getExpress().getCalculateWeight()));
		expressDetailTemp.setTotalVolume(new BigDecimal(orderSend.getOriginalVolume()));
		expressDetailTemp.setAssuranceValue(new BigDecimal(orderSend.getValuationValue()));// 保价金额
		expressDetailTemp.setAssuranceFee(new BigDecimal(orderSend.getExpress().getValuationFee()));// 保费
		expressDetailTemp.setPayType(orderSend.getPayType());
		expressDetailTemp.setPayment(orderSend.getPayment());
		expressDetailTemp.setCnorCorpNo(orderSend.getCustCode());// 寄件单位编码
		expressDetailTemp.setCnorCorpName(orderSend.getCustomerName());// 寄件单位名称
		expressDetailTemp.setFreight(new BigDecimal(orderSend.getFreight()));
		expressDetailTemp.setAccountId(orderSend.getExpress().getAccountId());
		expressDetailTemp.setPackingFee(new BigDecimal(orderSend.getExpress().getPackingFee()));
		expressDetailTemp.setExpressImage(orderSend.getExpress().getExpressImage());
//		expressDetailTemp.setCneeCorpName(orderSend.getCn);// 收货公司名称 
		expressDetailTemp.setExpressProductType(orderSend.getExpress().getProductType());//快递产品类型
		//运费合计   运费+保费+包装费
		double carriage = orderSend.getFreight() + orderSend.getExpress().getValuationFee() + orderSend.getExpress().getPackingFee();
		expressDetailTemp.setCarriage(new BigDecimal(carriage));
		// 运单明细对象
		// 商品明细
		List<InfDmpOrderSendDetailVO> details = orderSend.getDetails();
		InfDmpOrderSendDetailVO detailVO = null;
		for(int i = 0, size = details.size(); i < size; i++){
			detailVO = details.get(0);
			expressDetailTemp.setSizeSn(detailVO.getGoodsCode());
			expressDetailTemp.setCount(detailVO.getGoodsNum());
			expressDetailTemp.setUnit(detailVO.getGoodsSize());
			expressDetailTemp.setPrice(new BigDecimal(detailVO.getPrice()));
			expressDetailTemp.setCustPackNo(detailVO.getCustPackNo());
			break;// 特殊处理，只要第一条记录
		}
		
		return expressDetailTemp;
	}
	
}
