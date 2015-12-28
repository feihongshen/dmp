package cn.explink.b2c.feiniuwang;

import java.io.UnsupportedEncodingException;
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

import cn.explink.b2c.feiniuwang.CwbRequest;
import cn.explink.b2c.feiniuwang.FeiNiuRequest;
import cn.explink.b2c.feiniuwang.FeiNiuWang;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;

@Controller
@RequestMapping("/feiniuwang")
public class FNWController {
	
	@Autowired
	FNWService fnwService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	FNWInsertCwbDetailTimmer fnwInsert;
	private Logger logger =LoggerFactory.getLogger(FNWController.class);
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		FeiNiuWang feiNiuWang=fnwService.getFeiniuwang(B2cEnum.Feiniuwang.getKey());
		model.addAttribute("feiniuwang",feiNiuWang==null?new FeiNiuWang(): feiNiuWang);
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist",branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		logger.info("进行飞牛网(http)对接配置——————");
		return "/b2cdj/feiniuwang/feiniuwang";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		fnwService.update(key, state);
		//保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	@RequestMapping("/save/{id}")
	public @ResponseBody  String feiniuSave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			
			fnwService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	//飞牛网(http)推送订单信息
	@RequestMapping("/dms")
	public @ResponseBody String dms(HttpServletRequest request) {
		String logistics_interface = request.getParameter("logistics_interface");
		try {
			logger.info("解码前的请求参数:{}",logistics_interface);
			logistics_interface = URLDecoder.decode(logistics_interface, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("编码转换异常,异常原因:{}",e1);
		}
		String data_digest = request.getParameter("data_digest");
		String logistic_proider_id = request.getParameter("logistic_proider_id");
		String msg_type = request.getParameter("msg_type");
		logger.info("飞牛网（http）请求：logistics_interface={},data_digest={},logistic_proider_id={},msg_type={}",new Object[]{logistics_interface,data_digest,logistic_proider_id,msg_type});
		String  data_digests = data_digest==null?"":data_digest;
		String logistics_proider_id = logistic_proider_id==null?"":logistic_proider_id;
		String msg_types = msg_type==null?"":msg_type;
		FeiNiuRequest feiniureq = new FeiNiuRequest();
		CwbRequest cwbRequest = null;
		try {
			cwbRequest = JacksonMapper.getInstance().readValue(logistics_interface, CwbRequest.class);
			feiniureq.setLogistics_interface(cwbRequest);
			feiniureq.setData_digest(data_digests);
			feiniureq.setLogistic_proider_id(logistics_proider_id);
			feiniureq.setMsg_type(msg_types);
		} catch (Exception e) {
			logger.error("订单json数据转化异常:",e);
		}
		if(cwbRequest==null){
			return "订单请求为空！";
			
		}
		fnwService.requestOrdersToTMS(feiniureq,logistics_interface);
		logger.info("执行飞牛网(http)推送订单！");
		String respjson = fnwService.respJson(feiniureq,"true","");
		return respjson;
	}
	
	//执行临时表直接插入主表
	@RequestMapping("/timmer")
	public  @ResponseBody String executeTimmer() {
		fnwInsert.selectTempAndInsertToCwbDetail();
		return "执行了飞牛网(http)的临时表";
	}
}
