package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/SettlementContract")
@Controller
public class SettlementContractController {
	
	@RequestMapping("/SettlementContractList")
	public String SettlementContractList(){	
		
		
		return "SettlementContract/SettlementContractList";
	}
	
	@RequestMapping("/CriteriaQuery")
	public String CriteriaQuery(){	
		
		
		return "SettlementContract/CriteriaQuery";
	}
	
	@RequestMapping("/AddSettlementContract")
	public String AddSettlementContract(){	
		
		
		return "SettlementContract/AddSettlementContract";
	}

}
