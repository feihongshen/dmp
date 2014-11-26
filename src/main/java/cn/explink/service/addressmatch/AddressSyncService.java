package cn.explink.service.addressmatch;

import java.util.List;

import javax.jws.WebService;

import cn.explink.domain.addressvo.AddressSyncServiceResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DelivererRuleVo;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.VendorVo;

@WebService
public interface AddressSyncService {

	AddressSyncServiceResult createDeliveryStation(ApplicationVo applicationVo, DeliveryStationVo deliveryStationVo);

	AddressSyncServiceResult updateDeliveryStation(ApplicationVo applicationVo, DeliveryStationVo deliveryStationVo);

	AddressSyncServiceResult deleteDeliveryStation(ApplicationVo applicationVo, DeliveryStationVo deliveryStationVo);

	AddressSyncServiceResult createVendor(ApplicationVo applicationVo, VendorVo vendorVo);

	AddressSyncServiceResult updateVendor(ApplicationVo applicationVo, VendorVo vendorVo);

	AddressSyncServiceResult deleteVendor(ApplicationVo applicationVo, VendorVo vendorVo);

	AddressSyncServiceResult createDeliverer(ApplicationVo applicationVo, DelivererVo deliverer);

	AddressSyncServiceResult updateDeliverer(ApplicationVo applicationVo, DelivererVo deliverer);

	AddressSyncServiceResult deleteDeliverer(ApplicationVo applicationVo, DelivererVo deliverer);

	/**
	 * 创建配送员规则
	 * 
	 * @param applicationVo
	 * @param delivererRuleVoList
	 * @return
	 */
	AddressSyncServiceResult createDelivererRule(ApplicationVo applicationVo, List<DelivererRuleVo> delivererRuleVoList);

	/**
	 * 删除配送员规则
	 * 
	 * @param applicationVo
	 * @param delivererRuleVoList
	 * @return
	 */
	AddressSyncServiceResult deleteDelivererRule(ApplicationVo applicationVo, Long ruleId);

}
