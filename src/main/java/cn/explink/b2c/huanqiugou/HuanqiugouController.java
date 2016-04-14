package cn.explink.b2c.huanqiugou;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/huanqiugou")
public class HuanqiugouController {
	private Logger logger =LoggerFactory.getLogger(HuanqiugouController.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	HuanqiugouService haunqiugouService;
	@Autowired
	BranchDAO branchDAO; 
	@Autowired
	HuanqiugouInsertCwbDetailTimmer smileInsertCwbDetailTimmer;
	
	@RequestMapping("/show/{id}")
	public String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("huanqiugouObject", haunqiugouService.getHuanqiugou(key));
		model.addAttribute("warehouselist",branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/huanqiugou";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String smileSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			try{
				haunqiugouService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		haunqiugouService.update(key, state); 
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	/**
	 * 订单推送
	 * @param request
	 * @return
	 */
	@RequestMapping("/putExpressOrder")
	public @ResponseBody String haveOrders(HttpServletRequest request) {
		try {
			String method=request.getParameter("method");
			String expressid=request.getParameter("expressid");
			String sign=request.getParameter("sign");
			String timestamp=request.getParameter("timestamp");
			String data=request.getParameter("data");
			
			logger.info("环球购物请求信息method={},expressid={},sign={},timestamp={},data={}",new Object[]{method,expressid,sign,data});
			
			if(method==null||expressid==null||sign==null||timestamp==null||data==null){
				return haunqiugouService.buildResponse("S007", "参数错误", "0");
			}
			
			return haunqiugouService.dealwithHxgdmsOrders(method,expressid,sign,timestamp,data);
			
		} catch (Exception e) {
			logger.error("未知异常",e);
			return haunqiugouService.buildResponse("S007", "未知异常", "0");
		}
	}

	
	
	
	/**
	 *   临时表
	 * @param request
	 * @return
	 */
	@RequestMapping("/timmer")
	public  @ResponseBody String timmer() {
		smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "执行了好像购DMS速递的临时表";
	}
	
}
