package cn.explink.b2c.gztl;

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
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/gztl")
public class GztlController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	GztlService gztlService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GuangZhouTongLuInsertCwbDetailTimmer guangZhouTongLuInsertCwbDetailTimmer;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("guangzhoutongluObject", this.gztlService.getGztl(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));

		model.addAttribute("joint_num", key);
		return "b2cdj/guangzhoutonglu";
	}

	@RequestMapping("/saveGuangzhoutonglu/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			this.gztlService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.gztlService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByGuangZhouABC(HttpServletRequest request, HttpServletResponse response) {
		String xml = null;
		try {

			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.GuangZhouABC.getKey());
			if (isOpenFlag == 0) {
				return "未开启0广州ABC0查询接口";
			}
			Gztl gztl = this.gztlService.getGztl(B2cEnum.Guangzhoutonglu.getKey());

			xml = request.getParameter("XML");
			String MD5 = request.getParameter("MD5");

			this.logger.info("广州通路请求参数xml={},MD5={}", xml, MD5);

			String localSignString = MD5Util.md5(xml + gztl.getPrivate_key());
			if (!MD5.equalsIgnoreCase(localSignString)) {
				this.logger.info("签名验证失败,xml={},MD5={}", xml, MD5);
				return "签名验证失败";
			}

			return this.gztlService.orderDetailExportInterface(xml, gztl);
		} catch (Exception e) {
			this.logger.error("0广州ABC0处理业务逻辑异常！" + xml, e);
			return "处理业务逻辑异常";
		}
	}

	@RequestMapping("/gztl_timmer")
	public @ResponseBody void ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		this.guangZhouTongLuInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Guangzhoutonglu.getKey());
		this.logger.info("执行了广州通路查询临时表的定时器!");
	}
}
