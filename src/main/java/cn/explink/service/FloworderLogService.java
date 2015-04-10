package cn.explink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.OrderFlowLogDAO;
import cn.explink.util.DateTimeUtil;

@Service
public class FloworderLogService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderFlowLogDAO orderFlowLogDAO;
	
	public void deteFlow(){
		
		String endTime = orderFlowLogDAO.getEndTime();
		String fiveTime = DateTimeUtil.getNeedDate(endTime,-5) +" 00:00:00";
		long maxId = orderFlowLogDAO.getEndFlowId(fiveTime);
		long minId = orderFlowLogDAO.getStartFlowId(fiveTime);
		if(maxId <= minId+10000){
			orderFlowLogDAO.deleteFlowLogByid(minId, maxId);
		}else{
			for(long i =minId;i < maxId ;i++ ){
				if(maxId <= i+10000){
					orderFlowLogDAO.deleteFlowLogByid(i, maxId);
				}else{
					orderFlowLogDAO.deleteFlowLogByid(i, i+10000);
				}
				i = (i-1)+10000; 
			}
			
		}
	}

}
