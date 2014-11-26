package cn.explink.b2c.zhongliang;

import java.util.List;

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
import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/zhongliang")
public class ZhongliangController {

	private Logger logger = LoggerFactory.getLogger(ZhongliangController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ZhongliangService zhongliangService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ZhongliangInsertCwbDetailTimmer timmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("zhongliang", zhongliangService.getZhongliang(key) == null ? new Zhongliang() : zhongliangService.getZhongliang(key));
		List<Branch> warehouselist = branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		model.addAttribute("warehouselist", warehouselist);
		model.addAttribute("joint_num", key);
		return "jointmanage/zhongliang/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			zhongliangService.edit(request, key);
			logger.info("修改中粮B2c信息");
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		zhongliangService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/waitOrder")
	public @ResponseBody String waitOrder() {
		return zhongliangService.waitOrder();
	}

	@RequestMapping("/cancelOrder")
	public @ResponseBody String cancelOrder() {

		return zhongliangService.CancelOrders();
	}

	@RequestMapping("/timmer")
	public @ResponseBody String timmer() {
		timmer.selectTempAndInsertToCwbDetail();
		return "手动执行了中粮的插入正式表定时器";
	}
}
