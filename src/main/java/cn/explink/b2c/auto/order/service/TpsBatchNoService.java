package cn.explink.b2c.auto.order.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;

@Service
public class TpsBatchNoService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CwbDAO cwbDAO;
	
	@Transactional
	public void save(String cwb,TpsBatchNoDoBoxInfo boxInfo){
		if(cwb==null||cwb.length()<1||boxInfo==null||boxInfo.getTmsBatchNo()==null||boxInfo.getTmsBatchNo().length()<1){
			throw new RuntimeException("报文里订单号或交接单号数据为空");
		}
		
		//mq消息并发处理时要同步
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		
		if(co==null){
			String err="DMP里没找到此订单数据,订单号="+cwb;
			logger.error(err);
			throw new RuntimeException(err);
		}
		
		//交接单号
		String batchNo=boxInfo.getTmsBatchNo();
		if(batchNo!=null){
			batchNo=batchNo.trim();
		}
		
		if(batchNo!=null&&batchNo.length()>0){
			if(co.getRemark1()==null||co.getRemark1().length()<1){
				co.setRemark1(batchNo);
			}else{
				String orderBatchNos[]=co.getRemark1().split(",");
				boolean exist=false;
				for(String no:orderBatchNos){
					if(batchNo.equals(no)){
						exist=true;
						break;//test
					}
				}
				if(!exist){
					co.setRemark1(co.getRemark1()+","+batchNo);
				}
			}
		}
		
		//托运单号
		String attemperNo=boxInfo.getTmsAttemperNo();
		if(attemperNo!=null){
			attemperNo=attemperNo.trim();
		}

		if(attemperNo!=null&&attemperNo.length()>0){
			if(co.getRemark3()==null||co.getRemark3().length()<1){
				co.setRemark3(attemperNo);
			}else{
				String orderAttemperNos[]=co.getRemark3().split(",");
				boolean exist=false;
				for(String no:orderAttemperNos){
					if(attemperNo.equals(no)){
						exist=true;
						break;//test
					}
				}
				if(!exist){
					co.setRemark3(co.getRemark3()+","+attemperNo);
				}
			}
		}
		
		//托运单生成时间
		String attemperTime=boxInfo.getTmsAttemperTime();
		if(attemperTime!=null){
			attemperTime=attemperTime.trim();
		}
		
		if(attemperTime!=null&&attemperTime.length()>0){
			if(co.getRemark4()==null||co.getRemark4().length()<1){
				co.setRemark4(attemperTime);
			}else{
				String orderAttemperTimes[]=co.getRemark4().split(",");
				boolean exist=false;
				for(String time:orderAttemperTimes){
					if(attemperTime.equals(time)){
						exist=true;
						break;//test
					}
				}
				if(!exist){
					co.setRemark4(co.getRemark4()+","+attemperTime);
				}
			}
		}
		
		this.cwbDAO.updateBatchNo(cwb, co.getRemark1(), co.getRemark3(), co.getRemark4());
	}
	
}
