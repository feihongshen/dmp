package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.KufangBranchMapDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.KufangBranchMap;


@Service
public class KufangBranchMappingService {
	@Autowired
	KufangBranchMapDAO kufangBranchMapDAO;
	@Autowired
	BranchDAO branchDAO;
	
	@Transactional
	public void create(long fromBranchId,List<Long> toBranchIdList,boolean isEdit) {
		if(!isEdit){
			long existCnt=kufangBranchMapDAO.getKufangBranchMapCount(fromBranchId, 0);
			if(existCnt>0){
				throw new RuntimeException("此分拣库的相关配置已经存在，请到修改页面进行配置");
			}
		}
		
		kufangBranchMapDAO.deleteKufangBranchMap(fromBranchId);
		String branchName=null;
		for(Long toBranchId:toBranchIdList){
			long cnt=kufangBranchMapDAO.getKufangBranchMapCount(0,toBranchId);
			if(cnt>0){
				Branch branch=branchDAO.getBranchById(toBranchId);
				if(branch!=null){
					if(branchName==null){
						branchName=branch.getBranchname();
					}else{
						branchName=branchName+","+branch.getBranchname();
					}
				}
				
			}
		}
		if(branchName!=null){
			throw new RuntimeException("不允许为站点重复分配分拣库，站点:"+branchName);
		}else{
			for(Long toBranchId:toBranchIdList){
				kufangBranchMapDAO.creKufangBranchMap(fromBranchId,toBranchId);
			}
		}
		
	}
	
	@Transactional
	public List<KufangBranchMap> edit(long fromBranchId){
		List<KufangBranchMap> list=kufangBranchMapDAO.getKufangBranchMapByWheresql(fromBranchId,0);
		return list;
	}
	
	@Transactional
	public List<KufangBranchMap> list(long fromBranchId,long toBranchId){
		List<KufangBranchMap> list=kufangBranchMapDAO.getKufangBranchMapByWheresql(fromBranchId,toBranchId);
		return list;
	}
	
	@Transactional
	public void delete(long fromBranchId){
		kufangBranchMapDAO.deleteKufangBranchMap(fromBranchId);
	}
	
	@Transactional
	public long getNextBranch(long deliveryBranchId){
		List<KufangBranchMap> list=kufangBranchMapDAO.getKufangBranchMapByToBranch(deliveryBranchId);
		long result=0;
		if(list!=null&&list.size()>0){
			result=list.get(0).getFromBranchId();
		}
		
		return result;
	}
}
