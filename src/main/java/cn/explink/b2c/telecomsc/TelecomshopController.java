package cn.explink.b2c.telecomsc;

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
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/telecom")
public class TelecomshopController {
	private Logger logger = LoggerFactory.getLogger(TelecomshopController.class);
	@Autowired
	TelecomshopService telecomshopService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TelecomInsertCwbDetailTimmer telecomInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("telecomObject", telecomshopService.getTelecomShop(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/telecomshop";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			telecomshopService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		telecomshopService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 电信商城订单导入接口 入口
	 */
	@RequestMapping("/")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Telecomshop.getKey());
			if (isOpenFlag == 0) {
				return "未开启0电信商城0查询接口";
			}
			Telecomshop telecom = telecomshopService.getTelecomShop(B2cEnum.Telecomshop.getKey());

			TelecomParms telecomParms = telecomshopService.loadingTelecomParms(request); // 加载参数到这里

			return telecomshopService.receivedCwbOrderExport(telecom, telecomParms);
		} catch (Exception e) {
			logger.error("0电信商城0处理业务逻辑异常！", e);
			return "请求信息异常:" + e.getMessage();
		}
	}

	/**
	 * 电信商城订单导入接口 入口
	 */
	@RequestMapping("/test")
	public @ResponseBody String requestCwbSearch_test(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Telecomshop.getKey());
			if (isOpenFlag == 0) {
				return "未开启0电信商城0查询接口";
			}
			Telecomshop telecom = telecomshopService.getTelecomShop(B2cEnum.Telecomshop.getKey());

			TelecomParms telecomParms = telecomshopService.loadingTelecomParms_test(request); // 加载参数到这里

			return telecomshopService.receivedCwbOrderExport(telecom, telecomParms);
		} catch (Exception e) {
			logger.error("0电信商城0处理业务逻辑异常！", e);
			return "请求信息异常:" + e.getMessage();
		}
	}

	@RequestMapping("/timmer")
	public @ResponseBody String ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		telecomInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Telecomshop.getKey());
		return "excute Telecomshop timmer Success!";
	}

}
