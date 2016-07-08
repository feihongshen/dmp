package cn.explink.b2c.tps;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;


@Controller
@RequestMapping("/tpsAutoFlowCfg")
public class TpsAutoCfgController {
	@Autowired
	private TpsAutoCfgService tpsAutoCfgService;
	@Autowired
	private BranchDAO branchDAO;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		TpsAutoCfg  tpsAutoCfg = this.tpsAutoCfgService.getTpsAutoCfg(key);
		List<Branch> branchList=this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());

		model.addAttribute("tpsAutoCfg", tpsAutoCfg);
		model.addAttribute("warehouselist", branchList);
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/tpsAutoCfg";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter("autoOpenFlag"))){
			return "{\"errorCode\":1,\"error\":\"自动化分拣接口开关必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("batchnoOpenFlag"))){
			return "{\"errorCode\":1,\"error\":\"交接单号接口开关必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("warehouseId"))){
			return "{\"errorCode\":1,\"error\":\"分拣库房id必填\"}";
		}	
		
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.tpsAutoCfgService.edit(request, key);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.tpsAutoCfgService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
