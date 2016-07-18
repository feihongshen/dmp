package cn.explink.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.WeightAgainService;

@RequestMapping("/weightAgainManager")
@Controller
public class WeightAgainManagerController {
	
	@Autowired
	private WeightAgainService weightAgainService ;
	
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	
	@Autowired
	private CwbOrderService cwbOrderService;
	
	
	/**
	 * index 页面
	 */
	@RequestMapping(params = "index")
	public String index(Model model){
		//****************add*********************
		// add by bruce shangguan 20160712 获取电子秤称重时长
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("weightTime") ;
		String weightTime = "10" ; // 电子秤称重时长默认为10秒
		if(systemInstall != null && !StringUtils.isEmpty(systemInstall.getValue()) && systemInstall.getValue().trim().matches("^[1-9][0-9]*$")){
			weightTime = systemInstall.getValue() ;
		}
		model.addAttribute("weightTime", weightTime);
		// end 20160712
		//************end**************************
		return "weightAgain";
	}
	
	
	/**
	 * 补录重量
	 */
	@RequestMapping(params = "updateOrderWeight")
	@ResponseBody
	public Map<String,Object> updateOrderWeight(@RequestParam(value = "orderNumber", required = true) String orderNumber,
			@RequestParam(value = "carrealweight", required = true , defaultValue = "0") BigDecimal carrealweight,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,Object> result = new HashMap<String,Object>();
		CwbOrder cwbOrder = this.weightAgainService.findCwbOrder(orderNumber) ;
		boolean isScanOrderFlag  = true ;
		if(cwbOrder == null){
			//  可能输入的是运单号
			String cwbOrderNumber = this.cwbOrderService.translateCwb(orderNumber);
			cwbOrder = this.weightAgainService.findCwbOrder(cwbOrderNumber) ;
			isScanOrderFlag = false ;
			if(cwbOrder == null){
				result.put("errorMsg", ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI.getText()) ;
				return result ;
			}
		}
		// 获取订单对应的客户信息
		Customer customer = this.weightAgainService.findCustomer(cwbOrder.getCustomerid()) ;
		if(customer == null){
			result.put("errorMsg", ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI.getText()) ;
			return result ;
		}
		long isypdjusetranscwb = customer.getIsypdjusetranscwb() ;
		// 如果客户开启一票多件扫描订单模式，并且订单是有运单的，若当前扫描的是订单号，就提示要扫描运单号
		if (isypdjusetranscwb == 1 && ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1) || (cwbOrder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()))){
			String tranScwbs = cwbOrder.getTranscwb() ;
			if(!StringUtils.isEmpty(tranScwbs) && tranScwbs.trim().indexOf(orderNumber) == -1){
				result.put("errorMsg", ExceptionCwbErrorTypeEnum.Qing_SAO_MIAO_YUN_DAN_HAO.getText()) ;
				return result ;
			}
		}
		if(isScanOrderFlag){
			this.weightAgainService.updateOrderWeight(cwbOrder.getCwb(), carrealweight);
		}else{
			this.weightAgainService.updateDeliveryNumberWeight(cwbOrder.getCwb(), orderNumber, carrealweight);
		}
		return result ;
	}
	
}
