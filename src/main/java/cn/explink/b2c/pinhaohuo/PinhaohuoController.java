package cn.explink.b2c.pinhaohuo;

import java.io.BufferedInputStream;
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
@RequestMapping("/pinhaohuo")
public class PinhaohuoController {
	private Logger logger =LoggerFactory.getLogger(PinhaohuoController.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	PinhaohuoService hxgdmsService;
	@Autowired
	BranchDAO branchDAO; 
	@Autowired
	PinhaohuoInsertCwbDetailTimmer smileInsertCwbDetailTimmer;
	
	@RequestMapping("/show/{id}")
	public String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("pinhaohuoObject", hxgdmsService.getPinhaohuo(key));
		model.addAttribute("warehouselist",branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/pinhaohuo";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String smileSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			hxgdmsService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		hxgdmsService.update(key, state); 
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	/**
	 * 好享购DMS，订单推送
	 * @param request customerid 默认传0
	 * @return
	 */
	@RequestMapping("/order/{customerid}")
	public @ResponseBody String haveOrders(HttpServletRequest request,@PathVariable("customerid") long customerid) {
		try {
			
			String jsonstr=request.getParameter("content");
			
			logger.info("拼好货请求信息jsonstr={},customerid={}",jsonstr,customerid);
			
			if(jsonstr==null||jsonstr.isEmpty()){
				return hxgdmsService.buildResponseJSON("fail","11111","参数不完整");
			}
			
			int smileKey=B2cEnum.PinHaoHuo.getKey();
			return hxgdmsService.dealwithHxgdmsOrders(jsonstr,smileKey,customerid);
			
		} catch (Exception e) {
			logger.error("未知异常",e);
			return hxgdmsService.buildResponseJSON("fail","11111","未知异常"+e.getMessage());
		}
	}

	
	@RequestMapping("/addressmatch")
	public @ResponseBody String addressmatch(HttpServletRequest request) {
		try {
			
			
			String jsonstr=request.getParameter("content");
			if(jsonstr==null){
				InputStream input = new BufferedInputStream(request.getInputStream());
				jsonstr = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			}
			
			logger.info("拼好货请求匹配信息jsonstr={}",jsonstr);
			
			if(jsonstr==null||jsonstr.isEmpty()){
				return hxgdmsService.buildResponseJSON("fail","11111","参数不完整");
			}
			return hxgdmsService.addressmatchService(jsonstr);
			
		} catch (Exception e) {
			logger.error("未知异常",e);
			return hxgdmsService.buildResponseJSON("fail","11111","未知异常"+e.getMessage());
		}
	}
	
	/**
	 *  拼好货，订单推送  测试
	 * @param request
	 * @return
	 */
	@RequestMapping("/order_test")
	public  String haveOrders_test(HttpServletRequest request) {
		return "b2cdj/smile_test";
	}
	
	/**
	 *   临时表
	 * @param request
	 * @return
	 */
	@RequestMapping("/timmer")
	public  @ResponseBody String timmer() {
		smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "执行了拼好货速递的临时表";
	}
	
}
