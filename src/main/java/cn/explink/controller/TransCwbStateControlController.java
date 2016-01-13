package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.TransCwbStateControlDAO;
import cn.explink.domain.TransCwbStateControl;
import cn.explink.enumutil.TransCwbStateEnum;


@Controller
@RequestMapping(value="/transCwbStateControl")
public class TransCwbStateControlController {
	
@Autowired
private TransCwbStateControlDAO transcwbstateControlDAO;





@RequestMapping(value="/list")
public String gettransCwbStateControlList(Model model){
	Map<Integer, List<TransCwbStateControl>> Mlist=new HashMap<Integer, List<TransCwbStateControl>>();
	for(TransCwbStateEnum em:TransCwbStateEnum.values()){
		Mlist.put(em.getValue(), this.transcwbstateControlDAO.getTransStateControlByWhere(em.getValue()));
	}
	model.addAttribute("Mlist", Mlist);
	
	return "/transCwbState/list";
	
}

@RequestMapping(value="/edit/{transcwbstate}")
public String getEdittransCwbStateControl(Model model,@PathVariable(value="transcwbstate")int transcwbstate){
	model.addAttribute("transcwbstate", transcwbstate);
	model.addAttribute("transcwbstateList", this.transcwbstateControlDAO.getTransStateControlByWhere(transcwbstate));
	return "/transCwbState/edit";
	
}
@RequestMapping("/save/{transcwbstate}")
public  @ResponseBody String saveTransCwbStateControl(@PathVariable(value="transcwbstate")int transcwbstate,@RequestParam(value = "toflowtype", required = false, defaultValue = "") String[] toflowtype){
	
	this.transcwbstateControlDAO.deleteTranStateControl(transcwbstate);
	
		for (String flowtype : toflowtype) {
			this.transcwbstateControlDAO.createTransStateControl(transcwbstate, Integer.parseInt(flowtype));
		}
	
	
	return "{\"errorCode\":0,\"error\":\"保存成功\"}";
}
/*@RequestMapping("/save/{transcwbstate}")
public @ResponseBody String save(@PathVariable("transcwbstate") int transcwbstate, @RequestParam(value = "tostate", required = false, defaultValue = "") String[] tostates) {

	this.transcwbstateControlDAO.deleteTranStateControl(transcwbstate);
		for (String tostate : tostates) {
			this.transcwbstateControlDAO.createTransStateControl(transcwbstate, Integer.parseInt(tostate));
		}
	
	return "{\"errorCode\":0,\"error\":\"保存成功\"}";
}*/


}
