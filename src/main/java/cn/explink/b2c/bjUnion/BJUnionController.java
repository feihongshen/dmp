package cn.explink.b2c.bjUnion;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;

@Controller
@RequestMapping("/bjunion")
public class BJUnionController {
	@Autowired
	BJUnionService bjunionService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired 
	JointService jointService;
	private Logger logger =LoggerFactory.getLogger(BJUnionController.class);
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("bjunion", this.bjunionService.getBJUnion(key));
		model.addAttribute("joint_num", key);
		logger.info("下一步进行北京银联(浙江)配置——————");
		return "/b2cdj/bjunion/bjunion";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		this.bjunionService.update(key, state);
		//保存成功
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String bjunionSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			try{
				this.bjunionService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	//北京银联(浙江)请求数据入口
	@RequestMapping("/requestIN")
	public @ResponseBody String requestIN(HttpServletRequest request){
		String context = request.getParameter("context").trim();
		this.logger.info("北京银联(浙江)请求参数:"+context);
		return this.bjunionService.dealWithRequest(context);
	}
	
	
}
