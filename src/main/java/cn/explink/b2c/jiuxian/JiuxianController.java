package cn.explink.b2c.jiuxian;

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

import cn.explink.b2c.benlaishenghuo.BenLaiShengHuo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/Jiuxian_wang")
public class JiuxianController {
	@Autowired
	JiuxianService jiuxianService;
	@Autowired
	JiuxianInsertCwbDetailTimmer cwbDetailTimmer;
	@Autowired
	BranchDAO branchDAO;
	private Logger logger = LoggerFactory.getLogger(BenLaiShengHuo.class);

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("JiuxianObject", jiuxianService.getJiuxianShenghuo(B2cEnum.Jiuxian.getKey()));
		model.addAttribute("joint_num", key);
		return "/b2cdj/Jiuxian";
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		jiuxianService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				jiuxianService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存
	}

}
