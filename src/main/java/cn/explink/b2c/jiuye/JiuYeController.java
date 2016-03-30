package cn.explink.b2c.jiuye;

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
import cn.explink.b2c.jiuye.jsondto.JiuYe_request;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.B2cUtil;

@Controller
@RequestMapping("/jiuye")
public class JiuYeController {
	
	@Autowired
	JiuYeInsertCwbDetailTimmer jiuyeCwbService;
	@Autowired
	JiuYeService jiuYeService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired 
	JointService jointService;
	@Autowired
	B2cUtil b2cUtil;
	
	private Logger logger =LoggerFactory.getLogger(JiuYeController.class);
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("jiuye", this.b2cUtil.getViewBean(key, JiuYe.class));
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist",branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		logger.info("进行九曳对接配置——————");
		return "/b2cdj/jiuye/jiuye";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		jiuYeService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String jiuyeSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			try{
				jiuYeService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	@RequestMapping("/dms")
	public @ResponseBody  String dms(HttpServletRequest request) throws IOException{
		String params=request.getParameter("Data");
		logger.info("九曳请求参数：{}",params);
		
		JiuYe_request jiuyeReq=JacksonMapper.getInstance().readValue(params, JiuYe_request.class);
		int key = B2cEnum.JiuYe1.getKey();
		int isOpenFlag = this.jointService.getStateForJoint(key);
		if (isOpenFlag == 0) {
			logger.info("未开启[九曳]对接！");
			return "未开启九曳对接！";
		}
		JiuYe jiuye = this.b2cUtil.getViewBean(key, JiuYe.class);
		if(!jiuyeReq.getDelveryCode().equals(jiuye.getDmsCode())){
			this.logger.info("单号:{},九曳传来编码:{},物流商配置编码:{}",new Object[]{jiuyeReq.getContent().getWorkCode(),jiuyeReq.getDelveryCode(),jiuye.getDmsCode()});
			return "运营商编码不统一";
		}
		return jiuYeService.RequestOrdersToTMS(jiuyeReq,jiuye);
	}
	
	//执行临时表直接插入主表
	@RequestMapping("/timmer")
	public  @ResponseBody String executeTimmer() {
		jiuyeCwbService.selectTempAndInsertToCwbDetail();
		return "执行了九曳的临时表";
	}
	
	//jsp页面上实施的页面本地测试
	@RequestMapping("/test")
	public  String test(HttpServletRequest request){
		return "/b2cdj/jiuye/test";
	}
	
	
}
