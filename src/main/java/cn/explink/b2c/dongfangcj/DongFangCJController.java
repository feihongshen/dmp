package cn.explink.b2c.dongfangcj;

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
@RequestMapping("/dongfangcj")
public class DongFangCJController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	DongFangCJService dongFangCJService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DongFangCJService_getOrder dongFangCJService_getOrder;
	@Autowired
	DongFangCJInsertCwbDetailTimmer dongFangCJInsertCwbDetailTimmer;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("dongfangcjObject", dongFangCJService.getDongFangCJ(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/dongfangcj";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				dongFangCJService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		dongFangCJService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 提供手动导入接口 和插入主表
	 */
	@RequestMapping("/test")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {

		dongFangCJService_getOrder.downloadOrdersToDongFangCJFTPServer();
		dongFangCJService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
		dongFangCJInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.DongFangCJ.getKey());
		dongFangCJService_getOrder.MoveTxtToDownload_BakFile();

		return "excute dongfangCJ orders download Success!";
	}

	/**
	 * 提供口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String searchInterface(HttpServletRequest request, HttpServletResponse response) {
		try {
			// response.setContentType("text/xml;charset=GBK");
			// response.setCharacterEncoding("GB2312");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.DongFangCJ.getKey());
			if (isOpenFlag == 0) {
				return "未开启0东方CJ0查询接口";
			}
			DongFangCJ cj = dongFangCJService.getDongFangCJ(B2cEnum.DongFangCJ.getKey());

			String wb_no = request.getParameter("wb_no");
			String sign = request.getParameter("sign");

			return dongFangCJService.requestCwbSearchInterface(wb_no, sign, cj);
		} catch (Exception e) {
			logger.error("[京东]处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
