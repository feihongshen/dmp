package cn.explink.b2c.smile;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.maisike.Maisike;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/smile")
public class SmileController {
	private Logger logger = LoggerFactory.getLogger(SmileController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	SmileService smileService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SmileInsertCwbDetailTimmer smileInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("smileObject", smileService.getSmile(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/smile";
	}

	@RequestMapping("/saveSmile/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			smileService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		smileService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 广州思迈，订单推送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/order")
	public @ResponseBody String haveOrders(HttpServletRequest request) {
		try {
			String Request = request.getParameter("Request");
			String MD5 = request.getParameter("MD5");
			String Action = request.getParameter("Action");

			logger.info("广州思迈请求信息Request={},MD5={},Action=" + Action, Request, MD5);

			if (Request == null || MD5 == null || Action == null) {
				return smileService.buildResponseXML("False", "参数不完整");
			}

			return smileService.requestSiMaiMakeOrders(Request, MD5, Action);

		} catch (Exception e) {
			logger.error("未知异常", e);
			return smileService.buildResponseXML("False", "未知异常" + e.getMessage());
		}
	}

	/**
	 * 广州思迈，订单推送 测试
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/order_test")
	public String haveOrders_test(HttpServletRequest request) {
		return "b2cdj/smile_test";
	}

	/**
	 * 临时表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String timmer() {
		smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "执行了广州思迈速递的临时表";
	}

}
