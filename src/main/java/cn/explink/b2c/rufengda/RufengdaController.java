package cn.explink.b2c.rufengda;

import java.util.HashMap;
import java.util.Map;

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
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.RedisMap;
import cn.explink.util.impl.RedisMapImpl;

@Controller
@RequestMapping("/rufengda")
public class RufengdaController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	RufengdaService rufengdaService;
	@Autowired
	BranchDAO branchDAO;
	public static RedisMap<String, Integer> threadMap;
	static { // 静态初始化 以下变量,用于判断线程是否在执行
		threadMap = new RedisMapImpl<String, Integer>("RufengdaController");
		threadMap.put("rufengda_hander", 0);
	}
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("rufengdaObject", rufengdaService.getRufengda(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/rufengda";
	}

	@RequestMapping("/saveRufengda/{id}")
	public @ResponseBody String rufengdaSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				rufengdaService.edit(request, key);
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
		rufengdaService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/excute")
	public @ResponseBody String getOrder() {
		if (threadMap.get("rufengda_hander") == 1) {
			return "本地定时器没有执行完毕，跳出循环";
		}
		threadMap.put("rufengda_hander", 1);
		rufengdaService.RufengdaInterfaceInvoke();
		threadMap.put("rufengda_hander", 0);
		return "手动执行了获取如风达订单的列表";

	}

	@RequestMapping("/init")
	public @ResponseBody String init() {
		threadMap.put("rufengda_hander", 0);
		return "手动初始化如风达成功";
	}

	@RequestMapping("/getOrder")
	public @ResponseBody String getOrderinterface() {

		rufengdaService.RufengdaInterfaceGetOrdersInvoke(); // 获取订单
		return "excute getOrders Success!";
	}

	@RequestMapping("/syn_user")
	public @ResponseBody String syn_user() {
		rufengdaService.RufengdaInterfaceSynUserInfoInvoke();
		return "excute Syn_user Success!";

	}

	@RequestMapping("/successOrder")
	public @ResponseBody String successOrder() {
		rufengdaService.RufengdaInterfaceSuccessOrderInvoke();
		return "excute successOrder Success!";
	}

	@RequestMapping("/reason")
	public void reason() {
		rufengdaService.Reason();

	}

}
