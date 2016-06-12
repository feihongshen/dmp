package cn.explink.b2c.shenzhoushuma;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;

/**
 * 神州数码 Controler
 * @author yurong.liang 2016-04-29
 */
@Controller
@RequestMapping("/shenzhoushuma")
public class ShenZhouShuMaControler {
	@Autowired
	private JiontDAO jiontDAO;
	@Autowired
	private JointService jointService;
	@Autowired
	private ShenZhouShuMaService shenZhouShuMaService;
	
	/**
	 * 显示接口配置界面
	 */
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("shenZhouShuMa", shenZhouShuMaService.getShenZhouShuMa(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/shenzhoushuma";
	}
	
	/**
	 * 保存配置信息
	 */
	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			shenZhouShuMaService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		shenZhouShuMaService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
}
