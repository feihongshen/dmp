package cn.explink.b2c.huitongtx;

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

@Controller
@RequestMapping("/httx")
public class HuitongtxController {
	private Logger logger = LoggerFactory.getLogger(HuitongtxController.class);
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	HuitongtxInsertCwbDetailTimmer huitongtxInsertCwbDetailTimmer;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/test")
	public String test() {

		return "b2cdj/huitongtx/huitongtx_test";

	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("huitongtxObject", huitongtxService.getHuitongtx(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/huitongtx";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String Save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			huitongtxService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		huitongtxService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * 汇通天下 请求接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByTmall(HttpServletRequest request, HttpServletResponse response) {

		try {

			String method = request.getParameter("method");
			String timestamp = request.getParameter("timestamp");
			String app_key = request.getParameter("app_key");
			String sign = request.getParameter("sign");
			String data = request.getParameter("data");

			String baseParams = "app_key=" + app_key + ",sign=" + sign + ",sign=" + sign + ",timestamp=" + timestamp + ",method=" + method;
			logger.info("httx请求参数：{},data={}", baseParams, data);

			Huitongtx httx = huitongtxService.getHuitongtx(B2cEnum.Huitongtx.getKey());

			int isOpenFlag = jointService.getStateForJoint(httx.getB2c_enum());
			if (isOpenFlag == 0) {
				return huitongtxService.responseMessage("1", "未开启httx对接,type=" + httx.getB2c_enum(), null);
			}

			if (app_key == null || app_key.isEmpty() || sign == null || sign.isEmpty() || timestamp == null || timestamp.isEmpty() || method == null || method.isEmpty() || data == null
					|| data.isEmpty()) {
				return huitongtxService.responseMessage("3", "基本参数不完整" + baseParams, null);
			}

			return huitongtxService.requestMethod(httx, app_key, sign, timestamp, method, data);

		} catch (Exception e) {
			logger.error("汇通天下接收订单数据发生未知异常！", e);
			return huitongtxService.responseMessage("2", "未知异常" + e.getMessage(), null);
		}
	}

	/**
	 * 请求接口 手动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		huitongtxInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		logger.info("执行了tmall查询临时表的定时器!");
		return "SUCCESS";
	}

}
