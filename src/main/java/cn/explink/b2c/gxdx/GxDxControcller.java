/**
 * 
 */
package cn.explink.b2c.gxdx;

import java.net.URLDecoder;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.gxdx.xmldto.RequestDto;
import cn.explink.b2c.gxdx.xmldto.util.XmlToBean;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.MD5.MD5Util;

/**
 * @ClassName: GxdxControcll
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月9日下午3:59:41
 */
@Controller
@RequestMapping("/gxdxAddress")
public class GxDxControcller {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	GxDxService gxDxService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GxDxInsertCwbDetailTimmer gxDxInsertCwbDetailTimmer;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		GxDx gxDx = gxDxService.getGxDx(B2cEnum.GuangXinDianXin.getKey());
		model.addAttribute("gxdx", gxDx == null ? new GxDx() : gxDx);
		model.addAttribute("joint_num", key);
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		logger.info("广信电信对接配置——————");
		return "/b2cdj/gxdx/gxdx";
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		gxDxService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			// 保存(页面配置信息)
			this.gxDxService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}
	
	
	/**
	 * 
	 */
	@RequestMapping("/sendCwbDetail")
	public @ResponseBody String requestByGxDx(HttpServletRequest request, HttpServletResponse response) {

			String logicdata = request.getParameter("logicdata"); // 
			String checkdata = request.getParameter("checkdata"); // 加密后数据
			this.logger.info("广信电信请求参数:logicdata={},checkdata={}}",new Object[]{logicdata,checkdata});
			
			GxDx gxdx = this.gxDxService.getGxDx(B2cEnum.GuangXinDianXin.getKey());
			
			try {
				logicdata = URLDecoder.decode(logicdata, "UTF-8");//实际xml信息

				if (logicdata == null || logicdata.equals("")) {

					logger.info("0广信电信0解析xml之后的数据集为空logicdata={}", logicdata);
					return gxDxService.responseXml("","","False","解析之后的logicdata为空");
				}
				//校验加密是否符合约定
				String sign = MD5Util.md5(logicdata+gxdx.getPrivate_key());
				if(!checkdata.equalsIgnoreCase(sign)){
					this.logger.info("广信电信MD5验证失败");
					return gxDxService.responseXml("","","False","MD5验证失败");
				}
				RequestDto red = (RequestDto)XmlToBean.toBeanRequest(logicdata);
				//验证物流公司编号是否一致
				if(!gxdx.getLogisticProviderID().equals(red.getLogisticProviderID())){
					this.logger.info("0广信电信0请求的物流公司编码不一致："+red.getLogisticProviderID()+"-------"+gxdx.getLogisticProviderID());
					return gxDxService.responseXml(red.getWaybillNo(), gxdx.getLogisticProviderID(), "False", "物流公司编号不一致");
				}  
				/*
				 * 处理xml中的信息
				 */
				return this.gxDxService.orderDetailExportInterface(red, gxdx);
			} catch (Exception e) {
				this.logger.error("广信电信处理异常，原因:", e);
				return gxDxService.responseXml("", gxdx.getLogisticProviderID(), "False", "未知异常");
			}

			
			
	}
	
	
	
	
	@RequestMapping("/test")
	public void test1() throws Exception{
		/*String xmlStrResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				  + "<UpdateInfo>"	
				  + "<LogisticProviderID>HLM</LogisticProviderID>"	
				  + "<OrderStates>"	
				  + "<OrderState>"	
				  + "<WaybillNo>7700000022</WaybillNo>"	
				  + "<State>SC04</State>"	
				  + "<StateTime></StateTime>"	
				  + "<OperName>李一鸣</OperName>"	
				  + "<OperateTime>2010-08-17 10:14:17</OperateTime>"	
				  + "<OperatorUnit></OperatorUnit>"	
				  + "<DeliveryMan>洛可可</DeliveryMan>"	
				  + "<DeliveryMobile></DeliveryMobile>"	
				  + "<Reason></Reason>"	
				  + "<Remark></Remark>"	
				  + "</OrderState>"	
				  + "</OrderStates>"	
				  + "</UpdateInfo>"	;*/
		String StrResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				  + "<RequestWorkOrder>"	
				  + "<ClientCode>Sp001</ClientCode>"
				  + "<LogisticProviderID>HLM</LogisticProviderID>"	
				  + "<Holiday>1</Holiday>"	
				  + "<ReplCost>20.2</ReplCost>"	
				  + "<TrustPerson>王三</TrustPerson>"	
				  + "<TrustUnit>好乐买</TrustUnit>"	
				  + "<TrustZipCode>001</TrustZipCode>"	
				  + "<TrustCity>北京市北京市朝阳区</TrustCity>"	
				  + "<TrustAddress>三里屯SOHO大厦3D15层</TrustAddress>"	
				  + "<TrustMobile>13436709024</TrustMobile>"	
				  + "<TrustTel>010-3650092</TrustTel>"	
				  + "<GetPerson>李三</GetPerson>"	
				  + "<GetUnit>XXx公司</GetUnit>"	
				  + "<GetZipCode>010</GetZipCode>"	
				  + "<GetCity>北京市昌平区</GetCity>"
				  + "<GetAddress>立汤路北方明珠</GetAddress>"
				  + "<GetTel>010-3456789</GetTel>"
				  + "<GetMobile>13436700238</GetMobile>"
				  + "<GoodsValue>30.45</GoodsValue>"
				  + "<WorkType>T0</WorkType>"
				  + "<GoodsInfo>"
				  + "<Good>"
				  + "<GoodsName>XXX商品</GoodsName>"
				  + "<GoodsValue>20.34 </GoodsValue>"
				  + "<GoodsBarCode>00987</GoodsBarCode>"
				  + "<ListType>0</ListType>"
				  + "</Good>"
				  + "<Good>"
				  + "<GoodsName>XXX商品</GoodsName>"
				  + "<GoodsValue>20.34 </GoodsValue>"
				  + "<GoodsBarCode>00987</GoodsBarCode>"
				  + "<ListType>0</ListType>"
				  + "</Good>"
				  + "</GoodsInfo>"
				  + "<GoodsNum>2</GoodsNum>"
				  + "<GoodsHav>2</GoodsHav>"
				  + "</RequestWorkOrder>"	;
		/*
		GoodsState respd = (GoodsState)XmlToBean.toBean(xmlStrResponse);
		String response = BeanToXml.toXml2(respd);
		String str = this.xmlToBean.toBean(xmlStrRequest).toString();
		System.out.println(respd);
		System.out.println(response);*/
		GxDx gxdx = gxDxService.getGxDx(B2cEnum.GuangXinDianXin.getKey());
		RequestDto red = (RequestDto)XmlToBean.toBeanRequest(StrResponse);
		
		
		 this.gxDxService.orderDetailExportInterface(red,gxdx);
		
		System.out.println(red);
		//保存(页面配置信息)
	}
	////执行插入主表
	@RequestMapping("/excute")
	public @ResponseBody String excute(){
		this.gxDxInsertCwbDetailTimmer.execute(B2cEnum.GuangXinDianXin.getKey());
		this.logger.info("执行广信电信插入主表");
		return "执行广信电信插入主表";
	}
}
