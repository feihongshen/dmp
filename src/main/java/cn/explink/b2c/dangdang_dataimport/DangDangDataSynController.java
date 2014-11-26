package cn.explink.b2c.dangdang_dataimport;

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
@RequestMapping("/dangdangdatasyn")
public class DangDangDataSynController {
	private Logger logger = LoggerFactory.getLogger(DangDangDataSynController.class);
	@Autowired
	DangDangDataSynService dangDangDataSynService;
	@Autowired
	DangDangSynInsertCwbDetailTimmer dangDangSynInsertCwbDetailTimmer;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("dangdangdatasynObject", dangDangDataSynService.getDangDang(key));
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		return "b2cdj/" + editJsp;

	}

	@RequestMapping("/saveDangdang/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			dangDangDataSynService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		dangDangDataSynService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 当当数据导入接口 请求接口 2013-06-22
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByDangDang(HttpServletRequest request, HttpServletResponse response) {

		try {

			DangDangDataSyn dangdangsyn = dangDangDataSynService.getDangDang(B2cEnum.DangDang_daoru.getKey());
			String charcode = dangdangsyn.getCharcode();
			// request.setCharacterEncoding(charcode);
			response.setContentType("text/xml;charset=" + charcode);
			response.setCharacterEncoding(charcode);

			String version = request.getParameter("version");// 当当 版本
			String action = request.getParameter("action");// 接收的接口 put / del
			String express_id = request.getParameter("express_id");// 快递公司id
			String request_time = request.getParameter("request_time");// 请求时间
			String token = request.getParameter("token"); // 验证码
			String order_list = request.getParameter("order_list"); // JSON 字符串

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.DangDang_daoru.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启当当订单导入接口");
				return dangDangDataSynService.respJSONInfo("103");
			}

			logger.info("当当数据导入接口请求:version=" + version + ",action=" + action + ",express_id=" + express_id + ",request_time=" + request_time + ",token=" + token + ",order_list=" + order_list);

			return dangDangDataSynService.AnalizyDangDangRequestInfo(dangdangsyn, version, action, express_id, request_time, token, order_list);

		} catch (Exception e) {
			logger.error("当当请求数据导入接口发生未知异常", e);
			return dangDangDataSynService.respJSONInfo("103");
		}

	}

	@RequestMapping("/test")
	public @ResponseBody String selectTempAndInsertToCwbDetail1() {
		dangDangSynInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DangDang_daoru.getKey());
		return "success";
	}

}
