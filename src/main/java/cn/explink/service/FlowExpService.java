package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.FlowExpDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.FlowExp;
import cn.explink.domain.orderflow.OrderFlow;

@Service
public class FlowExpService {

	@Autowired
	private FlowExpDao flowExpDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	private Logger logger = LoggerFactory.getLogger(FlowExpService.class);
	public void sendFlowExp() {
		for (int i = 0; i < 10; i++) {
			List<FlowExp> flowExpList =  flowExpDao.getFlowExpList();
			if(flowExpList == null || flowExpList.size() ==0 ){
				logger.info("resending flow no data! ");
				break;
			}
			
			
			for (FlowExp  f: flowExpList) {
				if (f.getCwb() != null ) {
					if (f.getCwb().trim().length() == 0) {
						continue;
					}
					logger.info("resending flow for {} ", f.getCwb());
					List<OrderFlow> orderflows = orderFlowDAO.getOrderFlowListByCwb(f.getCwb().trim());
					for (OrderFlow orderFlow : orderflows) {
						logger.info("resending flow for {} with state {} ", f.getCwb(), orderFlow.getFloworderdetail());
						cwbOrderService.resend(orderFlow);
					}
				}
			}
			
			for (FlowExp  f: flowExpList) {
				if (f.getCwb() != null ) {
					if (f.getCwb().trim().length() == 0) {
						continue;
					}
					flowExpDao.deteleFlowExp(f.getCwb());
					logger.info("resending flow for {} ", f.getCwb());
					OrderFlow orderflow = orderFlowDAO.getOrderCurrentFlowByCwb(f.getCwb().trim());
					logger.info("resending flow for {} with state {} ", f.getCwb(), orderflow.getFloworderdetail());
					cwbOrderService.resend(orderflow);
				}
			}
		}
	}

	

}
