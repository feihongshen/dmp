package cn.explink.b2c.tools.power;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jointpower")
public class JointPowerController {
	@Autowired
	JointPowerDAO jiontDAO;

	@RequestMapping("/")
	public String jointManagerByPowerEnterPage(Model model) {
		model.addAttribute("b2cenumlist", jiontDAO.getJointPowerList(1));
		return "jointmanage/b2cjointPower";
	}

	@RequestMapping("/save")
	public @ResponseBody String save(Model model, HttpServletRequest request) {
		String b2cpowers[] = request.getParameterValues("b2cpower");
		if (b2cpowers != null && b2cpowers.length > 0) {
			jiontDAO.delete(1);
			for (String joint_num : b2cpowers) {
				jiontDAO.update(1, joint_num);
			}
		}
		model.addAttribute("b2cenumlist", jiontDAO.getJointPowerList(1));
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

}
