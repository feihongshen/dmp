package cn.explink.service.addressmatch;

import javax.jws.WebService;

import cn.explink.domain.addressvo.AddressQueryResult;
import cn.explink.domain.addressvo.ApplicationVo;

@WebService
public interface AddressQueryService {

	AddressQueryResult getAddress(ApplicationVo applicationVo, Long addressId, Long deliveryStationId);

}
