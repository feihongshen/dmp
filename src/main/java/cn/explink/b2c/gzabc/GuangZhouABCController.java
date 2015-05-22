package cn.explink.b2c.gzabc;

import java.net.URLDecoder;

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
@RequestMapping("/gzabc")
public class GuangZhouABCController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	GuangZhouABCService gzabcService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GuangZhouABCInsertCwbDetailTimmer guangZhouABCInsertCwbDetailTimmer;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("gzabcObject", this.gzabcService.getGuangZhougABC(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/gzabc";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			this.gzabcService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.gzabcService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 订单导入接口
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByGuangZhouABC(HttpServletRequest request, HttpServletResponse response) {
		try {

			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.GuangZhouABC.getKey());
			if (isOpenFlag == 0) {
				return "未开启0广州ABC0查询接口";
			}
			GuangZhouABC gzabc = this.gzabcService.getGuangZhougABC(B2cEnum.GuangZhouABC.getKey());

			String sShippedCode = request.getParameter("sShippedCode"); // 标识
			String logicdata = request.getParameter("logicdata"); // xml格式列
			String checkdata = request.getParameter("checkdata"); // 加密后数据
			try {
				sShippedCode = URLDecoder.decode(sShippedCode, "UTF-8");
				logicdata = URLDecoder.decode(logicdata, "UTF-8");
				checkdata = URLDecoder.decode(checkdata, "UTF-8");
			} catch (Exception e) {
				this.logger.error("解码异常sShippedCode=" + sShippedCode + ",logicdata=" + logicdata + ",checkdata=" + checkdata, e);

			}

			this.logger.info("广州ABC请求参数:sShippedCode={},logicdata={},checkdata=" + checkdata, sShippedCode, logicdata);

			return this.gzabcService.orderDetailExportInterface(sShippedCode, logicdata, checkdata, gzabc);
		} catch (Exception e) {
			this.logger.error("0广州ABC0处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

	@RequestMapping("/gzabc_timmer")
	public @ResponseBody void ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		this.guangZhouABCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.GuangZhouABC.getKey());
		this.logger.info("执行了广州ABC查询临时表的定时器!");
	}

}
