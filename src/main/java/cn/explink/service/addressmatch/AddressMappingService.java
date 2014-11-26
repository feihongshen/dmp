package cn.explink.service.addressmatch;

import java.util.List;

import javax.jws.WebService;

import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.OrderVo;

@WebService
public interface AddressMappingService {

	AddressMappingResult mappingAddress(ApplicationVo applicationVo, List<OrderVo> orderList);

}
