package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
/**
 * TPS运单状态对接配置  Controller
 * @author yurong.liang 2015/12/19
 */
@Controller
@RequestMapping("/tpsCarrierOrderStatus")
public class TPSCarrierOrderStatusController {
	@Autowired
	private JiontDAO jiontDAO;
	@Autowired
	private JointService jointService;
	@Autowired
	private TPSCarrierOrderStatusService tpsCarrierOrderStatusService;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("tpsCarrierOrderStatus", this.tpsCarrierOrderStatusService.getTPSCarrierOrderStatus(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/tpsCarrierOrderStatus";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String tmallSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {

			this.tpsCarrierOrderStatusService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.tpsCarrierOrderStatusService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
