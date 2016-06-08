package cn.explink.b2c.tps;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.explink.core.utils.StringUtils;

/**
 * 外单推送DO Controller
 * @author gordon.zhou
 *
 */
@Controller
@RequestMapping("/tpsCwbFlow")
public class TpsCwbFlowCfgController {
	@Autowired
	private TpsCwbFlowCfgService tpsCwbFlowService;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		TpsCwbFlowCfg  tpsCwbFlowCfg = this.tpsCwbFlowService.getTpsCwbFlowCfg(key);

		model.addAttribute("tpsCwbFlowCfg", tpsCwbFlowCfg);
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/tpsCwbFlowCfg";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter("openFlag"))){
			return "{\"errorCode\":1,\"error\":\"接口开关必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("customerids"))){
			return "{\"errorCode\":1,\"error\":\"唯品会客户id必填\"}";
		}	
		
		if(StringUtils.isBlank(request.getParameter("maxTryTime"))){
			return "{\"errorCode\":1,\"error\":\"最大尝试次数必填\"}";
		}
	
		if(StringUtils.isBlank(request.getParameter("maxDataSize"))){
			return "{\"errorCode\":1,\"error\":\"最大处理行数必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("housekeepDay"))){
			return "{\"errorCode\":1,\"error\":\"临时表数据保留天数必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.tpsCwbFlowService.edit(request, key);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.tpsCwbFlowService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
