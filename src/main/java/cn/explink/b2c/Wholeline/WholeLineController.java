package cn.explink.b2c.Wholeline;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/wholeLine")
public class WholeLineController {
	@Autowired
	WholeLineService wholeLineService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("happyObject", wholeLineService.getWholeline(B2cEnum.wholeLine.getKey()));
		model.addAttribute("joint_num", key);
		return "/b2cdj/wholeLine";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			wholeLineService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

}
