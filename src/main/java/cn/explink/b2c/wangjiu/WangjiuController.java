package cn.explink.b2c.wangjiu;

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
@RequestMapping("/wangjiu")
public class WangjiuController {
	private Logger logger = LoggerFactory.getLogger(WangjiuController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	WangjiuService wangjiuService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	WangjiuInsertCwbDetailTimmer wangjiuInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("wangjiuObject", wangjiuService.getWangjiu(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/wangjiu";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				wangjiuService.edit(request, key);
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
		wangjiuService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 网酒网，订单推送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/order")
	public @ResponseBody String haveOrders(HttpServletRequest request) {
		try {
			String logistics_interface = request.getParameter("logistics_interface"); // xml字符串

			String data_digest = request.getParameter("data_digest"); // 签名字符串

			logger.info("网酒网请求信息logistics_interface={},data_digest={}", logistics_interface, data_digest);

			if (logistics_interface == null || data_digest == null) {
				return wangjiuService.buildResponseXML("false", "参数不完整");
			}

			return wangjiuService.processRequestOrders(logistics_interface, data_digest);

		} catch (Exception e) {
			logger.error("未知异常", e);
			return wangjiuService.buildResponseXML("false", "未知异常" + e.getMessage());
		}
	}

	/**
	 * 网酒网，订单推送 测试
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
		wangjiuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "执行了网酒网速递的临时表";
	}

}
