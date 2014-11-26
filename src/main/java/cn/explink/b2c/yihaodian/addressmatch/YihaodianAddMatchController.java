package cn.explink.b2c.yihaodian.addressmatch;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/yhdAddressmatch")
public class YihaodianAddMatchController {
	private Logger logger = LoggerFactory.getLogger(YihaodianAddMatchController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	YihaodianAddMatchService yihaodianAddMatchService;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("yihaodianObject", yihaodianAddMatchService.getYihaodian(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/yihaodianAddmatch";
	}

	@RequestMapping("/saveYihaodian/{id}")
	public @ResponseBody String YihaodianSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			yihaodianAddMatchService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		yihaodianAddMatchService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 接收一号店请求地址匹配
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/match")
	public @ResponseBody String RequestYiHaoDian(HttpServletRequest request) throws IOException {

		String requestXML = request.getParameter("depotParse");
		if (requestXML == null) {
			InputStream input = new BufferedInputStream(request.getInputStream());
			requestXML = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		}
		if (requestXML == null || requestXML.isEmpty()) {
			return "请求参数depotParse不能为空";
		}

		logger.info("一号店站点匹配-请求XML={}", requestXML);

		int openState = yihaodianAddMatchService.getStateForYihaodian(B2cEnum.yhdAddmatch.getKey());
		if (openState == 0) {
			return "未开启一号店站点匹配接口，请联系易普联科";
		}

		String repsonseXML = yihaodianAddMatchService.invokeYihaodianAddressmatch(requestXML);

		return repsonseXML;
	}

}
