package cn.explink.b2c.yonghuics;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/yonghuics")
public class YonghuiController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	YonghuiService yonghuiService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	YonghuiInsertCwbDetailTimmer timmer;

	public static Map<String, Integer> threadMap;
	static { // 静态初始化 以下变量,用于判断线程是否在执行
		threadMap = new HashMap<String, Integer>();
		threadMap.put("yonghui_hander", 0);
	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("yonghuiObject", yonghuiService.getYonghui(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/yonghui";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String YihaodianSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			yonghuiService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		yonghuiService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/")
	public @ResponseBody String RequestYiHaoDian() {
		if (threadMap.get("yonghui_hander") == 1) {
			return "本地定时器没有执行完毕，跳出循环";
		}
		threadMap.put("yonghui_hander", 1);
		try {
			yonghuiService.yonghuiInterfaceInvoke(B2cEnum.YongHuics.getKey());
		} catch (Exception e) {

		} finally {
			threadMap.put("yonghui_hander", 0);
		}

		return "下载永辉超市数据成功";
	}

	@RequestMapping("/init")
	public @ResponseBody String init() {
		threadMap.put("yonghui_hander", 0);
		return "手动初始化永辉超市成功";
	}

	@RequestMapping("/timmer")
	public @ResponseBody String RequestYiHaoDian_timmer() {
		timmer.selectTempAndInsertToCwbDetail(B2cEnum.YongHuics.getKey());
		return "SUCCESS";
	}

}
