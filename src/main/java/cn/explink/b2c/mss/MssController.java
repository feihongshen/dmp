package cn.explink.b2c.mss;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.jiuye.JiuYeInsertCwbDetailTimmer;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.B2cUtil;

@Controller
@RequestMapping("/mss")
public class MssController {
	@Autowired
	JiuYeInsertCwbDetailTimmer jiuyeCwbService;
	@Autowired
	MssService mssService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	B2cUtil b2cUtil;
	@Autowired
	MSSInsertCwbDetailTimmer cwbDetailTimmer;
	// 日志
	private Logger logger = LoggerFactory.getLogger(MssController.class);

	/**
	 * 显示otms对接页面
	 *
	 * @param key
	 * @param model
	 * @return
	 */
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("mss", this.b2cUtil.getViewBean(key, Mss.class));
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		this.logger.info("进行otms对接配置----");
		return "/b2cdj/mss";
	}

	@RequestMapping("/del/{state}/{id}")
	public String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.mssService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String jiuyeSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			this.mssService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	/**
	 * otms订单下载
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/dms")
	public @ResponseBody String dms(HttpServletRequest request,@RequestBody String params) throws IOException {

		Mss mss = this.b2cUtil.getViewBean(B2cEnum.MSS.getKey(), Mss.class);
		this.logger.info("otms请求参数：{}", params);
		if (!StringUtils.hasText(params)) {
			this.logger.warn("otms参数为空,params={}",params);
			return this.mssService.responseJson("4002", "请求参数错误", mss,"");
		}

		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.MSS.getKey());
		if (isOpenFlag == 0) {
			this.logger.info("未开启[otms]对接！");
			return this.mssService.responseJson("4007", "开发者信息异常", mss,"");
		}
		try {
			return this.mssService.RequestOrdersToTMS(params,mss);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.warn("otms未知异常" + e);
			return this.mssService.responseJson("6001", "未知异常", mss,"");
		}

	}
}
