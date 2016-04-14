package cn.explink.b2c.sfxhm;

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
import cn.explink.enumutil.BranchEnum;

/**
 * 家有购物北京ERP接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sfxhm")
public class SfxhmController {
	private Logger logger = LoggerFactory.getLogger(SfxhmController.class);
	@Autowired
	SfxhmService sfxhmService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	SfxhmInsertCwbDetailTimmer sfxhmInsertCwbDetailTimmer;
	@Autowired
	SfxhmService_getOrder sfxhmService_getOrder;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("sfxhmObject", sfxhmService.getSfxhm(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/sfxhm";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				sfxhmService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		sfxhmService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/download")
	public @ResponseBody String test(Model model) {
		long starttime = System.currentTimeMillis();

		sfxhmService_getOrder.downloadOrdersforRemoteProc();

		long endtime = System.currentTimeMillis();

		return "SUCCESS,耗时：" + (endtime - starttime) / 1000;
	}

	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {
		long starttime = System.currentTimeMillis();

		sfxhmInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		long endtime = System.currentTimeMillis();
		logger.info("执行了临时表-获取[小红帽-顺丰]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		return "小红帽-顺丰执行临时表成功";
	}

}
