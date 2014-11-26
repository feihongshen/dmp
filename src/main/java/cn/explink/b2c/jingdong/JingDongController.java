package cn.explink.b2c.jingdong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;

@Controller
@RequestMapping("/jingdong")
public class JingDongController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	JingDongService jingDongService;
	@Autowired
	BranchDAO branchDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("jingdongObject", jingDongService.getJingDong(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/jingdong";
	}

	@RequestMapping("/saveJingDong/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			jingDongService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		jingDongService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 提供口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.JingDong.getKey());
			if (isOpenFlag == 0) {
				return "未开启[京东]查询接口";
			}
			JingDong jingdong = jingDongService.getJingDong(B2cEnum.JingDong.getKey());

			String billcode = request.getParameter("billcode");

			return jingDongService.requestCwbSearchInterface(billcode, jingdong);
		} catch (Exception e) {
			logger.error("[京东]处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
