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
 * pjd签收图片Controller
 * @author jianrong.gao
 *
 */
@Controller
@RequestMapping("/pjdsignimgcfg")
public class PjdSignimgCfgController {
	@Autowired
	private PjdSignimgCfgService pjdSignimgCfgService;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		PjdSignimgCfg  pjdSignimgCfg = this.pjdSignimgCfgService.getPjdSignimgCfg(key);

		model.addAttribute("pjdSignimgCfg", pjdSignimgCfg);
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/pjdSignimgCfg";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter("openFlag"))){
			return "{\"errorCode\":1,\"error\":\"接口开关必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.pjdSignimgCfgService.edit(request, key);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.pjdSignimgCfgService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
