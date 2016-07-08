package cn.explink.b2c.zhemeng.track;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
/**
 * 哲盟_轨迹 接口对接
 * @author yurong.liang 2016-0527
 */
@Controller
@RequestMapping("/zhemengTrack")
public class ZhemengTrackController {
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ZhemengTrackService zhemengService;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("zhemengObject", zhemengService.getZhenMeng(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/zhemengTrack";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				zhemengService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		zhemengService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
