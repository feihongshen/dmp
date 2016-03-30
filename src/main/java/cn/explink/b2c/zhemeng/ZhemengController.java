package cn.explink.b2c.zhemeng;

import java.net.URLDecoder;
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

@Controller
@RequestMapping("/zhemeng")
public class ZhemengController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ZhemengService zhemengService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ZhemengInsertCwbDetailTimmer zhemengInsertCwbDetailTimmer;


	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("zhemengObject", zhemengService.getZhenMeng(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/zhemeng";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				zhemengService.edit(request, key);
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
		zhemengService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	
	
	/**
	 * 3.2 订单创建接口
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/createorder")
	public @ResponseBody String createOrder(HttpServletRequest request) throws Exception {
		String service = request.getParameter("service");
		String content = request.getParameter("content");
		String sign = request.getParameter("sign");
		
		content = URLDecoder.decode(content, "UTF-8");
		
		logger.info("哲盟订单明细请求:service={},content={},sign={}",new Object[]{service,content,sign});
		
		if(service==null||content==null||sign==null){
			return "参数不可为空";
		}
		
		String responseJson = zhemengService.createOrder(service, content, sign);
		
		logger.info("哲盟订单明细返回:{}",responseJson);
		
		return responseJson;
	}
	
	
	/**
	 * 接收请求地址匹配
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/match")
	public @ResponseBody String checkAddress(HttpServletRequest request) throws Exception {

		String service = request.getParameter("service");
		String content = request.getParameter("content");
		String sign = request.getParameter("sign");
		
		content = URLDecoder.decode(content, "UTF-8");
		
		logger.info("哲盟站点匹配请求:service={},content={},sign={}",new Object[]{service,content,sign});
		String repsonseXML = zhemengService.checkAddress(service, content, sign);

		return repsonseXML;
	}

	
	

	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request) throws Exception {
	
		zhemengInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.ZheMeng.getKey());
		return "success";
	}

}
