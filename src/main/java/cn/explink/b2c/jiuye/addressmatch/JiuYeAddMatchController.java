package cn.explink.b2c.jiuye.addressmatch;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.jiuye.JiuYe;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;

@Controller
@RequestMapping("/jiuyeaddressmatch")
public class JiuYeAddMatchController {
	private Logger logger = LoggerFactory.getLogger(JiuYeAddMatchController.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	JiuYeAddMatchService jiuYeAddMatchService;
	@Autowired
	BranchDAO branchDAO; 
	@RequestMapping("/show/{id}")
	public String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("jiuyeAddMatch", jiuYeAddMatchService.getJiuYe(key));
		model.addAttribute("warehouselist",branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/jiuye/jiuyeAddmatch";
	}
	@RequestMapping("/saveJiuye/{id}")
	public @ResponseBody  String YihaodianSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			jiuYeAddMatchService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		jiuYeAddMatchService.update(key, state); 
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	/**
	 * 接收九曳请求地址匹配
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/match")
	public @ResponseBody String JiuYeRequest(HttpServletRequest request) throws IOException{
		
		String requestjson=request.getParameter("Data");
		if(requestjson==null||requestjson.isEmpty()){
			return "请求参数Data不能为空";
		}
		JiuYeRequest jyreq=JacksonMapper.getInstance().readValue(requestjson, JiuYeRequest.class);
		
		JiuYeAddressMatch jiuyadd = getJiuYeDeliveryCode(jyreq.getDelveryCode());
		if(jiuyadd == null){
			return "运营商编码设置错误,请注意核实!";
		}
		logger.info("九曳站点匹配-请求json={}",requestjson);
		
		int openState=jiuYeAddMatchService.getStateForJiuYe(jiuyadd.getB2cenum());
		if(openState==0){
			return "未开启九曳站点匹配接口，请联系易普联科";
		}
		
		return jiuYeAddMatchService.invokeJiuYeAddressmatch(jyreq,jiuyadd);
	}
	private JiuYeAddressMatch getJiuYeDeliveryCode(String deliveryCode) {
		JiuYeAddressMatch jiuye = jiuYeAddMatchService.getJiuYe(B2cEnum.JiuYeAddressMatch1.getKey());
		if (jiuye != null && jiuye.getDmsCode().equals(deliveryCode)) {
			return jiuye;
		}
		return null;
	}
	
}
