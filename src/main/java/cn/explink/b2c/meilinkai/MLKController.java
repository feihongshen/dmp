package cn.explink.b2c.meilinkai;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.jiuye.JiuYeController;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
/**
 * 【玫琳凯】页面基础配置处理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/meilinkai")
public class MLKController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired 
	JointService jointService;
	@Autowired
	MLKService mlkService;
	
	private Logger logger =LoggerFactory.getLogger(JiuYeController.class);
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("meilinkai", this.mlkService.getMLK(key));
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist",this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		logger.info("======进入【玫琳凯】基础设置页面=====");
		return "/b2cdj/meilinkai/meilinkai";
	}
	/**
	 * 
	 * @param model
	 * @param key
	 * @param state（开关标识0：关闭     1：开启）
	 * @return
	 */
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		this.mlkService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	/**
	 * 【玫琳凯】页面基础设置（与更新）
	 * @param model
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping("/save/{id}")
	public @ResponseBody  String mlkSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			try{
				this.mlkService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}

		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	/**
	 * 【玫琳凯】手动执行请求webservice服务
	 */
	@RequestMapping("/requestTOmlk")
	public void requestTOmlk(){
		try{
			this.mlkService.getOrderData();
		}catch(Exception e){
			this.logger.error("请求调用【玫琳凯】webservice异常,原因:{}",e);
		}
	}
	
	
}
