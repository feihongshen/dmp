package cn.explink.b2c.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.weisuda.WeisudaService;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.pos.PosEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.pos.etong.EtongService;
import cn.explink.pos.mobileapp_dcb.MobiledcbService;
import cn.explink.pos.yalian.YalianApp;
import cn.explink.pos.yalian.YalianService;

/**
 * 手机App 构建发送类 version=物流E通（2.0平移过来）
 * 
 * @author Administrator
 * @user 恒宇运通
 */
@Service
public class MobileAppService {
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	EtongService etongService;
	@Autowired
	YalianService yalianService;
	@Autowired
	MobiledcbService mobiledcbService;
	@Autowired
	WeisudaService weisudaService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 构建派送信息发送至手机App
	 */
	public void buildDeliveryingToMobileApps(CwbOrderWithDeliveryState cwbOrderWithDeliveryState, DmpOrderFlow orderFlow) {

		logger.info("执行了CODApp接口,cwb={},flowordertype={}", orderFlow.getCwb(), orderFlow.getFlowordertype());

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {

			if (b2ctools.isB2cOpen(PosEnum.Weisuda.getKey())) {

				logger.info("进入唯速达数据对接cwb={}", orderFlow.getCwb());
				weisudaService.updateOrders(cwbOrderWithDeliveryState, orderFlow);

			}
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {

			logger.info("执行了CODApp接口-领货执行,cwb={}", orderFlow.getCwb());

			if (b2ctools.isB2cOpen(PosEnum.MobileEtong.getKey())) {
				etongService.BuildDeliveryingServiceSendEtong(cwbOrderWithDeliveryState);
			}

			if (b2ctools.isB2cOpen(PosEnum.YalianApp.getKey())) {
				long code = cwbOrderWithDeliveryState.getCwbOrder().getCustomerid();
				String key = String.valueOf(code);
				YalianApp app = yalianService.getYalianAppSettingMethod(PosEnum.YalianApp.getKey());
				if (app.getCode_dianxin().equals((key)) || app.getCode_yidong().equals((key)) || app.getCode_liantong().equals(key)) {
					yalianService.BuildYalianAppMethod(cwbOrderWithDeliveryState, orderFlow);
				}
			}

			// 新疆大晨报App
			if (b2ctools.isB2cOpen(PosEnum.MobileApp_dcb.getKey())) {
				mobiledcbService.BuildDeliveryingServiceSend_dcb(cwbOrderWithDeliveryState, orderFlow);
			}

			// 唯速达
			if (b2ctools.isB2cOpen(PosEnum.Weisuda.getKey())) {
				logger.info("进入唯速达数据对接cwb={}", orderFlow.getCwb());
				weisudaService.insertWeisuda(cwbOrderWithDeliveryState, orderFlow);

			} else {
				logger.info("对接没有开启,cwb={}", orderFlow.getCwb());
			}
		}

	}
}
