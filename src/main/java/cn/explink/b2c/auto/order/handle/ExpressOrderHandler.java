package cn.explink.b2c.auto.order.handle;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.explink.b2c.auto.order.domain.ExpressDetailTemp;
import cn.explink.b2c.auto.order.exception.ExpressOrderException;
import cn.explink.b2c.auto.order.mq.TpsOrderMQCallback;
import cn.explink.b2c.auto.order.service.ExpressOrderService;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendDetailVO;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

import com.pjbest.deliveryorder.wcs.constants.WcsContants.CmdType;

/**
 * 快递订单处理
 * @author jian.xie
 *
 */
@Service("expressOrderHandler")
public class ExpressOrderHandler implements IOrderHandler {
	
	protected Logger logger = LoggerFactory.getLogger(TpsOrderMQCallback.class);

	@Autowired
	ExpressOrderService expressOrderService;
	
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Override
	@Transactional
	public void dealWith(InfDmpOrderSendVO orderSend,VipShop vipshop) {		
		/************************add start******************/
		//add by 周欢     根据承运商编码和客户id筛选订单    2016-07-15
		JointEntity jointEntityByShipper = this.jiontDAO.getDetialJointEntityByShipperNoForUse("\""+orderSend.getCustCode()+"\"",vipshop.getCustomerids());
		if(jointEntityByShipper == null){
			this.logger.info("tps订单下发接口，承运商对应的配置与接口设置客户id不符,承运商号：{},客户id:{}", orderSend.getCustCode(),vipshop.getCustomerids());
			throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"TPS接口未开启接收配送单开关");
		}
		/************************add end******************/
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
				logger.info("快递已存在，单号{},tps_trans_id{}", orderSend.getTransportNo(), orderSend.getMessageId());
				return;
			}
			expressOrderService.insertExpressDetailTemp(expressDetailTemp_New);
			// 更新明细
		}else if (CmdType.EDIT.equals(orderSend.getCmdType())){// 更新
			if(expressDetailTemp == null){
				// 不存在，不能更新
				logger.info("快递不存在, 单号{},tps_trans_id{}", orderSend.getTransportNo(), orderSend.getMessageId());
				return ;				
			} 
			expressOrderService.updateExpressDetailTemp(expressDetailTemp_New);
		} else {
			logger.info("不存在对应的cmdtype:{},订单号：{}", orderSend.getCmdType(), orderSend.getTransportNo());
		}
				
	}
	
	/**
	 * 针对快递类型的效验
	 * @param orderSend
	 * @return
	 */
	private boolean verify(InfDmpOrderSendVO orderSend) {
		if(StringUtils.isEmpty(orderSend.getTransportNo())){
			throw new ExpressOrderException("运单号为空");
		}
		return true;
	}

	/**
	 * 把InfDmpOrderSendVO转成ExprssDetailTemp方便后续操作
	 */
	private ExpressDetailTemp getExprssDetailTemp(InfDmpOrderSendVO orderSend){
		ExpressDetailTemp expressDetailTemp = new ExpressDetailTemp();
		expressDetailTemp.setTpsTransId(orderSend.getMessageId());
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
		expressDetailTemp.setIsCod(orderSend.getIsCod() ? 1 : 0);
		expressDetailTemp.setCodAmount(new BigDecimal(orderSend.getCodAmount()));
		expressDetailTemp.setTotalBox(orderSend.getTotalPack());// 合计箱数
		expressDetailTemp.setTotalWeight(new BigDecimal(orderSend.getOriginalWeight())); // 重量
		expressDetailTemp.setCalculateWeight(new BigDecimal(orderSend.getExpress().getCalculateWeight()));// 计费重量
		expressDetailTemp.setTotalVolume(new BigDecimal(orderSend.getOriginalVolume())); // 总体积
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
		expressDetailTemp.setCneeCorpName(orderSend.getExpress().getCneeCorpName());// 收货公司名称 
		expressDetailTemp.setExpressProductType(orderSend.getExpress().getProductType());//快递产品类型
		//运费合计   运费+保费+包装费
		double carriage = orderSend.getFreight() + orderSend.getExpress().getValuationFee() + orderSend.getExpress().getPackingFee();
		expressDetailTemp.setCarriage(new BigDecimal(carriage));
		expressDetailTemp.setIsAcceptProv(orderSend.getExpress().getIsAcceptProv() ? 1 : 0 );
		// 运单明细对象
		// 商品明细
		List<InfDmpOrderSendDetailVO> details = orderSend.getDetails();
		InfDmpOrderSendDetailVO detailVO = null;
		if(!CollectionUtils.isEmpty(details)){
			detailVO = details.get(0);// 特殊处理，只要第一条记录
			expressDetailTemp.setSizeSn(detailVO.getGoodsCode());
			expressDetailTemp.setCargoName(detailVO.getGoodsName());
			expressDetailTemp.setCount(detailVO.getGoodsNum());
			expressDetailTemp.setUnit(detailVO.getGoodsSize());
			expressDetailTemp.setPrice(new BigDecimal(detailVO.getPrice()));
			expressDetailTemp.setCustPackNo(detailVO.getCustPackNo());
		}else{
			expressDetailTemp.setSizeSn("");
			expressDetailTemp.setCargoName("");
			expressDetailTemp.setCount(0);
			expressDetailTemp.setUnit("");
			expressDetailTemp.setPrice(new BigDecimal(0.00));
			expressDetailTemp.setCustPackNo("");
		}
		// 快递属性
		expressDetailTemp.setCargoLength(new BigDecimal(orderSend.getExpress().getLength()));
		expressDetailTemp.setCargoWidth(new BigDecimal(orderSend.getExpress().getWidth()));
		expressDetailTemp.setCargoHeight(new BigDecimal(orderSend.getExpress().getHeight()));
		expressDetailTemp.setWeight(new BigDecimal(orderSend.getOriginalWeight()));
		expressDetailTemp.setVolume(new BigDecimal(orderSend.getOriginalVolume()));
		
		expressDetailTemp.setReturnCredit(new BigDecimal(orderSend.getReturnCredit()));
		expressDetailTemp.setTotalNum(details.size());
		expressDetailTemp.setOrderSource(orderSend.getOrderSource());
		return expressDetailTemp;
	}
	
}
