package cn.explink.b2c.auto.order.service;


import java.util.HashSet;
import java.util.Set;

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
				String remark1=makeValue(batchNo,co.getRemark1());
				co.setRemark1(remark1);
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
				String remark3=makeValue(attemperNo,co.getRemark3());
				co.setRemark3(remark3);
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
				String remark4=makeValue(attemperTime,co.getRemark4());
				co.setRemark4(remark4);
			}
		}
		
		this.cwbDAO.updateBatchNo(cwb, co.getRemark1(), co.getRemark3(), co.getRemark4());
	}
	
	private String makeValue(String data,String dbValue){
		String dot=",";
		String result="";
		String orderBatchNos[]=null;
		if(dbValue!=null){
			orderBatchNos=dbValue.split(dot);
		}
		Set<String> set=new HashSet<String>();
		if(orderBatchNos!=null&&orderBatchNos.length>0){
			for(String no:orderBatchNos){
				if(no==null||no.trim().length()<1){
					continue;
				}
				set.add(no.trim());
			}
		}
		if(data!=null){
			data=data.trim();
			if(data.length()>0){
				set.add(data);
			}
		}

		if(set.size()>0){
			for(String v:set){
				if(result.length()<1){
					result=v;
				}else{
					result=result+dot+v;
				}
			}
		}
		
		return result;
	}
	
}
