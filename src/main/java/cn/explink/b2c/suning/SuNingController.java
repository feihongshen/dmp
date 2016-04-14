package cn.explink.b2c.suning;

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
@RequestMapping("/suning")
@Controller
public class SuNingController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired 
	JointService jointService;
	@Autowired
	SuNingService suningService;
	@Autowired
	SuNingInsertCwbDetailTimmer suNingInsertCwbDetailTimmer;
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("suning", this.suningService.getSuNing(key));
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist",this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		logger.info("====进入苏宁对接配置====");
		return "/b2cdj/suning/suning";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		this.suningService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String suNingSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			try{
				this.suningService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	/**
	 * 【苏宁易购】下发数据
	 * @param request
	 * @return 返回信息,【苏宁易购】接收
	 */
	@RequestMapping("/suningdms")
	public @ResponseBody  String dms(HttpServletRequest request){
		String params=request.getParameter("request");
		logger.info("苏宁易购请求参数：{}",params);
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.SuNing.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启【苏宁易购】对接！");
			return "未开启苏宁易购对接！";
		}
		try{
			return this.suningService.RequestOrdersToTMS(params);
		}catch(Exception e){
			this.logger.error("【苏宁易购】处理异常,原因:{}",e);
		}
		return "系统异常！抓紧时间联系对方技术修改喽...";
	}
	
	//查出【苏宁易购】中未插入主表的订单插入主表
	@RequestMapping("/timmer")
	public  @ResponseBody String executeTimmer() {
		this.suNingInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "执行了【苏宁易购】的临时表插入主表";
	}
	
}
