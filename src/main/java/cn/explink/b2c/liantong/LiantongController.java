package cn.explink.b2c.liantong;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/liantong")
public class LiantongController {
	private Logger logger = LoggerFactory.getLogger(LiantongController.class);
	@Autowired
	LiantongService liantongService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	LiantongInsertCwbDetailTimmer liantongInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("liantongObject", liantongService.getLianTong(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/liantong";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				liantongService.edit(request, key);
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
		liantongService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/page")
	public String testpage() {

		return "b2cdj/liantong/liantongtest";

	}

	/**
	 * 接收 联通商城推送数据
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/received")
	public @ResponseBody String receivedLiantongOrders(HttpServletRequest request) throws IOException {

		String JsonContent = request.getParameter("JsonContent");
		String RequestTime = request.getParameter("RequestTime");
		String sign = request.getParameter("sign");

		logger.info("联通EOP-数据同步请求参数，JsonContent={},RequestTime={},sign={}", new Object[] { JsonContent, RequestTime, sign });

		if (JsonContent == null || JsonContent.isEmpty() || JsonContent == null || JsonContent.isEmpty() || sign == null || sign.isEmpty()) {
			return "请求参数错误";
		}

		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Liantong.getKey());
		if (isOpenFlag == 0) {
			return "未开启联通数据同步接口，请联系易普联科";
		}

		String repsonse = liantongService.invokeDealWithCwbOrderImport(JsonContent, RequestTime, sign);
		logger.info("当前返回联通：" + repsonse);

		request.setAttribute("values", repsonse);

		return repsonse;
	}

	@RequestMapping("/timmer")
	public @ResponseBody String excuteTimmer() {
		liantongInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "SUCCESS";
	}

}
