package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;

@Service
public class BranchRouteService {

	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private BranchRouteDAO branchRouteDAO;
	
	public List<Branch> getBranchIds(String branchids) {
		List<BranchRoute> list = this.branchRouteDAO.getNextBranchByfromBranchIdsAndType(branchids, 2);
		StringBuilder sb = new StringBuilder();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if(i>0){
					sb.append(",");
				}
				sb.append(list.get(i).getToBranchId());
			}
		}
		   List<Branch> list2 = this.branchDAO.getBranchByBranchidsNoType(sb.toString());
		   if(list2 == null){
			   return new ArrayList<Branch>();
		   }else{
			   return list2;
		   }
	}
	
}
