/**
 *
 */
package cn.explink.service.mps;

import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;

/**
 * @author songkaojun 2016年1月19日
 */
@Component
public class MPSCommonService extends AbstractMPSService {

	private static final String UPDATE_MPS_STATE = "[MPS通用服务]";

	public boolean isNewMPSOrder(String transCwb) {
		CwbOrder cwbOrder = this.getMPSCwbOrder(transCwb, MPSCommonService.UPDATE_MPS_STATE);
		return cwbOrder == null ? false : true;
	}

}
