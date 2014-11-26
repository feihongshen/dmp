package cn.explink.b2c.homegou;

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
@RequestMapping("/homegou")
public class HomegouController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	HomegouService homegouService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	HomegouService_getOrder homegouService_getOrder;
	@Autowired
	HomegouInsertCwbDetailTimmer homegouInsertCwbDetailTimmer;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("homegouObject", homegouService.getHomeGou(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/homegou";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			homegouService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		homegouService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 提供手动导入接口 和插入主表
	 */
	@RequestMapping("/test")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {

		homegouService_getOrder.downloadOrdersToHomeGouFTPServer();
		homegouService_getOrder.AnalyzTxtFileAndSaveB2cTemp();
		homegouInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.HomeGou.getKey());
		homegouService_getOrder.MoveTxtToDownload_BakFile();

		return "excute HomeGou orders download Success!";
	}

}
