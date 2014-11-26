package cn.explink.b2c.chinamobile;

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

@Controller
@RequestMapping("/chinamobile")
public class ChinamobileController {
	private Logger logger = LoggerFactory.getLogger(ChinamobileController.class);
	@Autowired
	ChinamobileService chinamobileService;

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
		model.addAttribute("chinamobileObject", chinamobileService.getChinaMobile(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/" + editJsp;

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			chinamobileService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		chinamobileService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/test")
	public @ResponseBody String receiver(HttpServletRequest request) {
		chinamobileService.downloadOrdersToFTPServer();
		chinamobileService.AnalyzCSVFileAndSaveB2cTemp();
		chinamobileService.MoveTxtToDownload_BakFile();
		return "手动下载移动数据完毕";
	}

}
