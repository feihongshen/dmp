package cn.explink.b2c.letv;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;

@Controller
@RequestMapping("/letv")
public class LetvController {
	private Logger logger = LoggerFactory.getLogger(LetvController.class);
	@Autowired
	LetvService letvService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("letvObject", letvService.getYeMaiJiu(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/" + editJsp;

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				letvService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		letvService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/receiver")
	public @ResponseBody String receiver(HttpServletRequest request) {
		String content = request.getParameter("content");
		logger.info("接收参数content={}", content);

		return content;
	}

}
