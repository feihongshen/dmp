package cn.explink.b2c.yixun;

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
 * 易迅网接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/yixun")
public class YiXunController {
	private Logger logger = LoggerFactory.getLogger(YiXunController.class);
	@Autowired
	YiXunService yixunService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	YiXunInsertCwbDetailTimmer yiXunInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("yixunObject", yixunService.getYiXun(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/yixun";

	}

	@RequestMapping("/saveYiXun/{id}")
	public @ResponseBody String tmallSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				yixunService.edit(request, key);
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
		yixunService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/test")
	public String test(Model model) {
		// 保存
		return "b2cdj/testPingtai";

	}

	/**
	 * 易迅请求接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/interface")
	public @ResponseBody String requestByTmall(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "";
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			YiXun yixun = yixunService.getYiXun(B2cEnum.YiXun.getKey());
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.YiXun.getKey());
			if (isOpenFlag == 0) {
				returnStr = "未开启[易迅网]接口";
			} else {
				String orderdetail = request.getParameter("orderdetail");
				returnStr = yixunService.requestMethod(yixun, orderdetail);
			}
			logger.info("[易迅网] 推送返回{}", returnStr);
			return returnStr;
		} catch (Exception e) {
			logger.error("[易迅网]处理业务逻辑异常！", e);
			return yixunService.response_XML("0", "未知异常" + e.getMessage());
		}
	}

	@RequestMapping("/insert")
	public @ResponseBody String insert(HttpServletRequest request, HttpServletResponse response) {
		long starttime = System.currentTimeMillis();
		long endtime = System.currentTimeMillis();
		yiXunInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		logger.info("执行了临时表-获取[易迅网]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		return "{\"error\":0 }";
	}

}
