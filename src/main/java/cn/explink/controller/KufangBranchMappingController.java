package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;

import cn.explink.domain.Branch;

import cn.explink.domain.KufangBranchMap;

import cn.explink.service.KufangBranchMappingService;


@Controller
@RequestMapping("/kufangBranchMap")
public class KufangBranchMappingController {

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	KufangBranchMappingService kufangBranchMappingService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		return "kufangbranch/add";
	}

	
	@RequestMapping("/edit/{fromBranchId}")
	public String edit(Model model, @PathVariable("fromBranchId") long fromBranchId) {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		 List<KufangBranchMap> kfList=kufangBranchMappingService.edit(fromBranchId);
		 Map<Long,String> kfMap=new HashMap<Long,String>();
		 for(KufangBranchMap kf:kfList){
			 kfMap.put(kf.getToBranchId(), null);
		 }
		model.addAttribute("kfMap", kfMap);
		model.addAttribute("fromBranchId", new Long(fromBranchId));
		return "kufangbranch/add";
	}
	
	@RequestMapping("/create")
	public @ResponseBody String create(@RequestParam(value = "fromBranchId", required = false, defaultValue = "0") long fromBranchId,
			@RequestParam(value = "toBranchId", required = false, defaultValue = "") String[] toBranchId
			,@RequestParam(value = "edit", required = false, defaultValue = "") String edit) throws Exception {
		List<Long> toBranchIdList=new ArrayList<Long>();
		
		try {
			for( String tempToBranchId : toBranchId){
				toBranchIdList.add(Long.parseLong(tempToBranchId));
			}
			boolean isEdit="1".equals(edit)?true:false;
			kufangBranchMappingService.create(fromBranchId, toBranchIdList,isEdit);
		} catch (Exception e) {
			String err="创建二级分拣库站点映射出错";
			logger.error(err,e);
			return "{\"errorCode\":1,\"error\":\"操作失败, "+e.getMessage()+"\"}"; 
		}
		
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "fromBranchId", required = false, defaultValue = "0") long fromBranchId,
			@RequestParam(value = "toBranchId", required = false, defaultValue = "0") long toBranchId) {
		List<Branch> branchList=branchDAO.getAllBranches();
		Map<Long,String> branchMap=new HashMap<Long,String>();
		for(Branch branch:branchList){
			branchMap.put(branch.getBranchid(), branch.getBranchname());
		}
		
		List<KufangBranchMap> kfList=kufangBranchMappingService.list(fromBranchId,toBranchId);
		Map<Long,String[]> kfNameMap=new HashMap<Long,String[]>();
		for(KufangBranchMap kf:kfList){
			String[] row=kfNameMap.get(kf.getFromBranchId());
			if(row==null){
				String fromName=branchMap.get(kf.getFromBranchId());
				String toName=branchMap.get(kf.getToBranchId());
				row=new String[2];
				row[0]=fromName;
				row[1]=toName;
				kfNameMap.put(kf.getFromBranchId(), row);
			}else{
				String toName=branchMap.get(kf.getToBranchId());
				row[1]=row[1]+" | "+toName;
			}
		}
		
		
		model.addAttribute("kfNameMap", kfNameMap);
		model.addAttribute("branchlist", branchList);
		return "kufangbranch/list";
	}

	@RequestMapping("/del/{fromBranchId}")
	public @ResponseBody String delete(@PathVariable("fromBranchId") long fromBranchId) throws Exception {
		try {
			kufangBranchMappingService.delete(fromBranchId);
		} catch (Exception e) {
			String err="删除二级分拣库站点映射出错";
			logger.error(err,e);
			return "{\"errorCode\":1,\"error\":\"操作失败, "+e.getMessage()+"\"}"; 
		}
		
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}




}