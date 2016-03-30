package cn.explink.b2c.lefeng;

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
@RequestMapping("/lefeng")
public class LefengController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	LefengService lefengService;
	@Autowired
	BranchDAO branchDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("lefengObject", lefengService.getLefengT(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/lefeng";
	}

	@RequestMapping("/saveLefeng/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				lefengService.edit(request, key);
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
		lefengService.update(key, state);
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
			LefengT lefeng = lefengService.getLefengT(B2cEnum.LefengWang.getKey());

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.LefengWang.getKey());
			if (isOpenFlag == 0) {
				return "未开启[乐蜂网]查询接口";
			}
			String code = request.getParameter("code");
			String cwb = request.getParameter("num");
			String key = request.getParameter("key");

			return lefengService.requestMethod(lefeng, cwb, code, key);
		} catch (Exception e) {
			logger.error("[乐蜂网]处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

	/**
	 * 提供口查询 乐蜂webservice服务端转发用到 根据订单查询跟踪信息
	 */
	@RequestMapping("/getDmpOrderFlow/{cwb}")
	public @ResponseBody String getDmpOrderFlow(@PathVariable("cwb") String cwb) {

		return lefengService.getCwbOrderFlowByLefeng(cwb);

	}

}
