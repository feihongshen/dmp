package cn.explink.b2c.gztlfeedback;

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
@RequestMapping("/gztlfeedback")
public class GztlFeedbackController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	GztlFeedbackService gztlFeedbackService;
	@Autowired
	BranchDAO branchDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("gztlFeedbackObject", this.gztlFeedbackService.getGztlFeedback(key));
		model.addAttribute("warehouselist", this.branchDAO.getAllBranches());
		model.addAttribute("joint_num", key);
		return "b2cdj/guangzhoutonglufeedback";
	}

	@RequestMapping("/saveGztlFeedback/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			this.gztlFeedbackService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.gztlFeedbackService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
