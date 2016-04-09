package cn.explink.b2c.zjfeiyuan.controller;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.zjfeiyuan.requestdto.RequestData;
import cn.explink.b2c.zjfeiyuan.responsedto.ResponseData;
import cn.explink.b2c.zjfeiyuan.service.RequestFYService;
import cn.explink.b2c.zjfeiyuan.util.BeanToXml;
import cn.explink.b2c.zjfeiyuan.util.XmlToBean;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.addressmatch.AddressMatchService;
@Controller
@RequestMapping("/fyAddress")
public class RequestFYController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired 
	RequestFYService requestFYService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AddressMatchService addressMatchService;
	/*@Autowired
	BeanToXml beanToXml;
	@Autowired
	XmlToBean xmlToBean;*/
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	@RequestMapping("/show/{id}")
	public  String  jointShow(@PathVariable("id") int key ,Model model){
		model.addAttribute("feiyuan", this.requestFYService.getFeiyuan(key));
		model.addAttribute("joint_num", key);
		logger.info("浙江飞远对接配置——————");
		return "/zjfeiyuan/feiyuan";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody  String updateState(Model model,@PathVariable("id") int key ,@PathVariable("state") int state ){
		//保存(停用或者启用)
		this.requestFYService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody  String fySave(Model model,@PathVariable("id") int key ,HttpServletRequest request){
		if(request.getParameter("password")!= null && "explink".equals(request.getParameter("password"))){
			//保存(页面配置信息)
			this.requestFYService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	@Test
	public void test() throws Exception{
		String xmlStrRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<request>"
				  + "<head>"
				  + "<usercode>explink</usercode>"
				  + "<batchno>2013072312341034</batchno>"
				  + "<key>2341asd9fasdfadf23</key>"
				  + "</head>"
				  + "<items>" 
				  + 	"<item>" 
				  +      "<itemno>1></itemno>"
				  +      "<province><![CDATA[广东省]]></province>"
				  +      "<city><![CDATA[广州市]]></city>"
				  +      "<area><![CDATA[天河区]]></area>"
				  +		 "<town></town>"
				  +		 "<address><![CDATA[广东省广州市天河区天河东路118号]]></address>"
				  +		 "<yworder>1234567889</yworder>"
				  + 	"</item>" 
				  + 	"<item>" 
				  +      "<itemno>2></itemno>"
				  +      "<province><![CDATA[广东省]]></province>"
				  +      "<city><![CDATA[广州市]]></city>"
				  +      "<area><![CDATA[天河区]]></area>"
				  +		 "<town></town>"
				  +		 "<address><![CDATA[广东省广州市天河区天河东路119号]]></address>"
				  +		 "<yworder>1234567899</yworder>"
				  + 	"</item>" 
				  + "</items>" 
			    + "</request>";
		String xmlStrResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				  + "<response>"	
				  + "<head>"	
				  + "<msg></msg>"
				  + "</head>"	
				  + "<items>"	
				  + "<item>"	
				  + "<itemno>1></itemno>"	
				  + "<netid>101</netid>"	
				  + "<netpoint><![CDATA[天河分站]]></netpoint>"	
				  + "<yworder>1234567889</yworder>"	
				  + "<siteno>30057400</siteno>"	
				  + "<sitename><![CDATA[天河配送站]]></sitename>"	
				  + "<remark></remark>"	
				  + "</item>"	
				  + "<item>"	
				  + "<itemno>2></itemno>"	
				  + "<netid>101</netid>"	
				  + "<netpoint><![CDATA[天河分站]]></netpoint>"	
				  + "<yworder>1234567899</yworder>"	
				  + "<siteno>30057400</siteno>"	
				  + "<sitename><![CDATA[天河配送站]]></sitename>"	
				  + "<remark></remark>"	
				  + "</item>"	
				  + "</items>"	
				  + "</response>"	;
		ResponseData respd = (ResponseData)XmlToBean.toBean(xmlStrResponse);
		String response = BeanToXml.toXml2(respd);
		/*String str = this.xmlToBean.toBean(xmlStrRequest).toString();*/
		RequestData rd = (RequestData)XmlToBean.toBean2(xmlStrRequest);
		String request = BeanToXml.toXml(rd);
		//保存(页面配置信息)
		//return "{\"errorCode\":0,\"error\":\"浙江飞远地址库匹配成功\"}";
	}
	@RequestMapping("/doit")
	public String  tojsp(HttpServletRequest request){
		return "/test/tojsp";
	}
	
	@RequestMapping("/totest")
	public @ResponseBody String doaddressmatch(){
		User user =  this.getSessionUser();
		String str = "";
		try{
			this.addressMatchService.doMatchAddress(user.getUserid(), "zff15001");
		}catch(Exception e){
			str = e.toString();
			this.logger.error("异常原因:{}",e);
			
		}
		if(!"".equals(str)){
			return "请求地址库异常！-----请在日志中查看异常原因！";
		}else{
			return "请求飞远地址库成功";
		}
	}
	
}
