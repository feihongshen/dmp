package cn.explink.b2c.hxgdms;

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
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/hxgdms")
public class HxgdmsController {
	private Logger logger = LoggerFactory.getLogger(HxgdmsController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	HxgdmsService hxgdmsService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	HxgdmsInsertCwbDetailTimmer smileInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("hxgdmsObject", hxgdmsService.getHxgDms(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/hxgdms";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				hxgdmsService.edit(request, key);
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
		hxgdmsService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 好享购DMS，订单推送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/order")
	public @ResponseBody String haveOrders(HttpServletRequest request) {
		try {
			String Request = request.getParameter("Request");
			String Signed = request.getParameter("Signed");
			String Action = request.getParameter("Action");
			String Dcode = request.getParameter("Dcode");

			logger.info("好像购DMS请求信息Request={},MD5={},Action={},Dcode={}", new Object[] { Request, Signed, Action, Dcode });

			if (Request == null || Signed == null || Action == null || Dcode == null) {
				return hxgdmsService.buildResponseJSON("False", "参数不完整");
			}

			return hxgdmsService.dealwithHxgdmsOrders(Request, Signed, Action, Dcode);

		} catch (Exception e) {
			logger.error("未知异常", e);
			return hxgdmsService.buildResponseJSON("False", "未知异常" + e.getMessage());
		}
	}

	/**
	 * 好像购DMS，订单推送 测试
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
		return "执行了好像购DMS速递的临时表";
	}

}
