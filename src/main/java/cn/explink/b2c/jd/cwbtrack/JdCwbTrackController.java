package cn.explink.b2c.jd.cwbtrack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cassandra.thrift.Cassandra.AsyncProcessor.system_add_column_family;
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
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/jdCwbTrack")
public class JdCwbTrackController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	JdCwbTrackService jdCwbTrackService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static volatile int currentConnCount=0;
	private static int limiMaxConnCount=10000;//最大请求并发数
	
	/**
	 * 显示接口配置界面
	 */
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("jdCwbTrackConfig", jdCwbTrackService.getJdCwbTrackConfig(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/jdcwbtrack";
	}
	
	/**
	 * 保存配置信息
	 */
	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			jdCwbTrackService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		jdCwbTrackService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	/**
	 * 提供口查询-获取订单跟踪信息
	 */
	@RequestMapping("/track")
	public @ResponseBody String getCwbTrack(HttpServletRequest request, HttpServletResponse response) {
		currentConnCount++;
		if(currentConnCount>limiMaxConnCount){
			return "系统繁忙，请稍后再试";
		}
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String customerId = request.getParameter("customerId");
			String billcode = request.getParameter("billcode");
			String sign = request.getParameter("sign");// 签名
			String requestTime = request.getParameter("requestTime");// 请求时间
			
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.JingDong_cwbTrack.getKey());
			if (isOpenFlag == 0) {
				return "未开启[京东_订单跟踪]查询接口";
			}
			
			this.logger.info("请求参数:billcode={},customerId={},sign={}}",
					new Object[] { billcode, customerId, sign });
			
			if (customerId==null||"".equals(customerId)) {
				this.logger.info("[京东_订单跟踪]接口-客户ID不能为空");
				return "请求客户ID为空";
			}
			
			if (requestTime==null||"".equals(requestTime)) {
				this.logger.info("[京东_订单跟踪]接口-请求时间不能为空");
				return "请求时间为空";
			}
			
			//设置签名过期检验
			if(System.currentTimeMillis()-Long.parseLong(requestTime)>300*1000){
				this.logger.info("请求过期,系统当前时间={},请求时间={}",new Object[] { System.currentTimeMillis(),Long.parseLong(requestTime) });
				return "请求过期";
			}
			
			if (sign==null||"".equals(sign)) {
				this.logger.info("[京东_订单跟踪]接口-签名不能为空");
				return "请求签名为空";
			}
			
			JdCwbTrackConfig config = jdCwbTrackService.getJdCwbTrackConfig(B2cEnum.JingDong_cwbTrack.getKey());
			if (!customerId.equals(config.getCustomerId()+"")) {
				this.logger.info("[京东_订单跟踪]接口-客户ID不能为空");
				return "请求客户ID错误";
			}
			String salt="6a6502cebf8e049bae17928355b757dd41a6f76f";
			// 校验加密是否符合约定
			String MD5Sign = MD5Util.md5(customerId + config.getPrivateKey() + requestTime + salt);
			if (!sign.equalsIgnoreCase(MD5Sign)) {
				this.logger.info(customerId+"MD5验证失败sign={},MD5Sign={}",sign,MD5Sign);
				return "签名错误";
			}
			
			String responseXML=jdCwbTrackService.requestCwbSearchInterface(billcode, config);
			currentConnCount--;
			return responseXML;
		} catch (Exception e) {
			logger.error("[京东_订单跟踪]处理业务逻辑异常！", e);
			currentConnCount--;
			return "[京东_订单跟踪]处理业务逻辑异常！";
		}
		
	}
	
	/**
	 * 测试用-获得签名
	 */
	@RequestMapping("/sign")
	public @ResponseBody String getSign(HttpServletRequest request, HttpServletResponse response) {
		String customerId = request.getParameter("customerId");
		String privateKey = request.getParameter("privateKey");
		//String requestTime = request.getParameter("requestTime");// 请求时间
		long requestTime=System.currentTimeMillis();
		String salt="6a6502cebf8e049bae17928355b757dd41a6f76f";
		String MD5Sign = MD5Util.md5(customerId + privateKey + requestTime+ salt);
		return "当前时间："+requestTime+" 签名: "+MD5Sign;
	}
	
}
