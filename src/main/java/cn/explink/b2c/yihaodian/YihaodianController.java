package cn.explink.b2c.yihaodian;

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
@RequestMapping("/yihaodian")
public class YihaodianController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	YihaodianService yihaodianService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	YihaodianInsertCwbDetailTimmer timmer;

	public static Map<String, Integer> threadMap;
	static { // 静态初始化 以下变量,用于判断线程是否在执行
		threadMap = new HashMap<String, Integer>();
		threadMap.put("yihaodian_hander", 0);
	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("yihaodianObject", yihaodianService.getYihaodian(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/yihaodian";
	}

	@RequestMapping("/saveYihaodian/{id}")
	public @ResponseBody String YihaodianSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				yihaodianService.edit(request, key);
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
		yihaodianService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/")
	public @ResponseBody String RequestYiHaoDian() {
		if (threadMap.get("yihaodian_hander") == 1) {
			return "本地定时器没有执行完毕，跳出循环";
		}
		threadMap.put("yihaodian_hander", 1);

		try {
			for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
				if (enums.getMethod().contains("yihaodian")) {
					yihaodianService.YiHaoDianInterfaceInvoke(enums.getKey());
				}
			}
		} catch (Exception e) {

		}

		threadMap.put("yihaodian_hander", 0);
		return "下载一号店数据成功";
	}

	@RequestMapping("/init")
	public @ResponseBody String init() {
		threadMap.put("yihaodian_hander", 0);
		return "手动初始化一号店成功";
	}

	@RequestMapping("/timmer")
	public @ResponseBody String RequestYiHaoDian_timmer() {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("yihaodian")) {
				timmer.selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		return "";
	}

}
