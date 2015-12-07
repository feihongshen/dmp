package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.DeliverServerParamVO;
import cn.explink.domain.VO.DeliverServerPullVO;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DeliverService;
import cn.explink.util.DigestsEncoder;
import cn.explink.util.Dom4jParseUtil;
import cn.explink.util.JsonUtil;
import cn.explink.util.StringUtil;


@Controller
@RequestMapping("/deliverServer")
public class DeliverServerController {
	
	private Logger logger =LoggerFactory.getLogger(DeliverServerController.class);
	
	@Autowired
	private DeliverService deliverService;
	@Autowired
	private CwbOrderService cwborderService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private ExceptionCwbDAO exceptionCwbDAO;

	@RequestMapping("/show/{id}")
	public String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("deliverServerParamVO", deliverService.getDeliverServerParamVOForSet(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/deliverServer";
	
	}
	
	@RequestMapping("/saveParamVO/{id}")
	public @ResponseBody  String tmallSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			deliverService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		deliverService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	/**
	 * 派送结果 反馈
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @throws Exception 
	 */
	@RequestMapping("/result")
	@ResponseBody
	public String deliverServerResult(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException{
		String cwb ="";
		Map<String,String> resultMap = new HashMap<String,String>();
		DeliverServerPullVO pullVO = this.buildPullVO(request);
		if( null != pullVO){
				
	        cwb = pullVO.getMail_num();
	        User user = userDAO.getUserByUsername(pullVO.getUnid());
	        DeliverServerParamVO paramVO = deliverService.getDeliverServerParamVO(PosEnum.DeliverServerAPP.getKey());
			//签名校验
			if(this.validateSign(pullVO, paramVO)){
				this.buildResultMap("error", "51002", "签名错误", resultMap);
			}else{
				//订单业务校验
				String scancwb = cwb;
				cwb = cwborderService.translateCwb(cwb);
				CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
				if( null != co ){
					try {
						Map<String, Object> parameters = deliverService.buildBusinessParam(user,co,pullVO,paramVO);
						cwborderService.deliverStatePod(user, cwb, scancwb, parameters);
						this.buildResultMap("ok", null, null, resultMap);
					} catch (CwbException ce) {
						CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
						exceptionCwbDAO.createExceptionCwb(cwb, ce.getFlowordertye(),ce.getMessage(), user.getBranchid(),user.getUserid(), cwbOrder == null?0:cwbOrder.getCustomerid(), 0, 0, 0, "");
						this.buildResult(pullVO,paramVO,ce,resultMap);
						logger.error("派送服务App-派送结果反馈操作异常！订单号：" + cwb + ";异常：" + ce.getMessage());
					}
				}else{
					buildResultMap("error", "51011", "订单号不存在", resultMap);
				}
			}
		}else{
			this.buildResultMap("error", "51000", "参数格式错误", resultMap);
			logger.error("派送服务App-派送结果反馈操作报文获取异常！");
		}
		String responseJson =  JsonUtil.translateToJson(resultMap);
		logger.info("派送服务App-返回:{},cwb={}",responseJson,cwb);
		return responseJson;
	}

	/**
	 * 构建派送结果反馈Map
	 * @param pullVO
	 * @param paramVO
	 * @param ce
	 */
	private Map<String,String> buildResult(DeliverServerPullVO pullVO,DeliverServerParamVO paramVO, CwbException ce,Map<String,String> resultMap) {
		
		int index = ce.getError().getValue();
		if( ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI.getValue() == index){
			buildResultMap("error", "51011", "订单号不存在", resultMap);
		}else if( ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan.getValue() == index){
			buildResultMap("error", "51022", "派送员唯一编号不存在", resultMap);
		}else{
			buildResultMap("error", "10001", "系统异常", resultMap);
		}
		return resultMap;
	}

	/**
	 * 派送结果反馈-签名校验
	 * @param pullVO
	 * @param paramVO
	 * @return
	 */
	private boolean validateSign(DeliverServerPullVO pullVO,DeliverServerParamVO paramVO) {
		boolean flag = false;
		String[] signStrs = pullVO.buildSignStr();
		String signLocal1 = DigestsEncoder.encode("SHA1", signStrs[0] + "&" + paramVO.getToken());
		String signLocal2 = DigestsEncoder.encode("SHA1", signStrs[1] + "&" + paramVO.getToken());
		this.logger.info(signStrs[0] + ":" + signStrs[1] + ",秘钥：" + signLocal1 + ":" + signLocal2 );
		if (!signLocal1.equals(pullVO.getSign()) && !signLocal2.equals(pullVO.getSign())) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 反馈结果封装
	 * @param result
	 * @param code
	 * @param msg
	 * @param resultMap
	 * @return
	 */
	private Map<String,String> buildResultMap(String result,String code,String msg,Map<String,String> resultMap){
		resultMap.put("result", result);
		if( !StringUtil.isEmpty(code)){
			resultMap.put("code", code);
		}
		if( !StringUtil.isEmpty(msg)){
			resultMap.put("msg", msg);
		}
		return resultMap;
	}
	
	/**
	 * 构建派送结果反馈VO
	 * @param request
	 * @return
	 */
	private DeliverServerPullVO buildPullVO(HttpServletRequest request){
        DeliverServerPullVO pullVO = null;
		try {
			InputStream input = new BufferedInputStream(request.getInputStream());
			String str = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串 
	        this.logger.info("棒棒糖派送服务App-派送结果反馈请求报文：" + str);
	        pullVO = JsonUtil.readValue(str, DeliverServerPullVO.class);
		} catch (IOException e) {
			this.logger.error("棒棒糖派送服务App-派送结果反馈请求流文件异常！异常：" + e);
		}  
		return pullVO;
	}
	
	
	@RequestMapping("/posnotify/{notifyCwb}")
	public @ResponseBody  void posnotify(Model model,@PathVariable("notifyCwb") String  notifyCwb ){
		 deliverService.posFeedbackNotifyApp(notifyCwb);
	}
	
	
}
