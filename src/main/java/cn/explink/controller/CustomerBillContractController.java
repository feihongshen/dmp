package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/CustomerBillContract")
public class CustomerBillContractController {
	@RequestMapping("/CustomerBillContractList")
	public String list(){
			
		
		
		return "CustomerBillContract/CustomerBillContractList";
	}
	
	@RequestMapping("/addBill")
	public String add(){
		String billBatches="B"+DateTimeUtil.getCurrentDate()+(Math.random()*10);
		
		return "";
	}
	

}
