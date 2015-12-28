package cn.explink.b2c.caifutong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/caifutong")
@Controller
public class CaiFuTongController {
	@Autowired
	CaiFuTongService caiFuTongService;

	/**
	 *
	 * @Title: jointShow
	 * @description 自动核销-通联，公司的信息配置功能-初始化
	 * @author 刘武强
	 * @date  2015年12月12日上午9:35:44
	 * @param  @param key
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("caiFuTong", this.caiFuTongService.getCaiFuTong(key));
		model.addAttribute("joint_num", key);
		return "/caifutong/caifutong";
	}

	/**
	 * @Title: updateState
	 * @description TODO
	 * @author 刘武强
	 * @date  2015年12月12日上午9:36:53
	 * @param  @param model
	 * @param  @param key
	 * @param  @param state
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.caiFuTongService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 *
	 * @Title: TonglianSave
	 * @description 自动核销-通联，公司的信息配置功能-保存方法
	 * @author 刘武强
	 * @date  2015年12月12日上午9:37:23
	 * @param  @param model
	 * @param  @param key
	 * @param  @param request
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/save/{id}")
	@ResponseBody
	public String TonglianSave(Model model, @RequestParam(value = "caifutongca", required = false) MultipartFile caifutongca, @RequestParam(value = "caifutongcer", required = false) MultipartFile caifutongcer, @PathVariable("id") int key, String type, HttpServletRequest request) {
		this.caiFuTongService.edit(request, key, caifutongca, caifutongcer);
		return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
	}
}
