package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbDateEnum;
import cn.explink.enumutil.CwbOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CustomerBillContractService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/CustomerBillContract")
public class CustomerBillContractController {
	
	@Autowired
	private CwbDAO cwbdao;
	
	@Autowired
	private CustomerBillContractService customerbillcontractservice;
	
	@Autowired
	private CustomerBillContractDao customerbillcontractdao;
	
	@Autowired
	private ExportwarhousesummaryDAO edao;
	
	@RequestMapping("/CustomerBillContractList")
	public String list(){
			
		
		
		return "CustomerBillContract/CustomerBillContractList";
	}
	
	@RequestMapping("/addBill")
	public String add(@RequestParam(value="customerId") long id,
			@RequestParam(value="dateState") long dateState,
			@RequestParam(value="startdate") String startdate,
			@RequestParam(value="enddate") String enddate,
			@RequestParam(value="cwbOrderType") String cwbOrderType,
			@RequestParam(value="remark") String remark
			){
		
			String billBatches="B"+DateTimeUtil.getCurrentDate()+(Math.random()*10);		
			List<CwbOrder> cwborderlist=cwbdao.findCwbByCustomerid(id);
			String cwbs=customerbillcontractservice.listToString(cwborderlist);
			long cwbtype=Integer.valueOf(cwbOrderType);
			List<CwbOrder> col=null;
			long cwbCount=0;
			if(dateState==CwbDateEnum.ShenHeRiQi.getValue()){
				
			}else if(dateState==CwbDateEnum.FaHuoRiQi.getValue()){
				if(cwbtype>0){
					col=customerbillcontractservice.findAllByCwbDateType(cwbs,startdate+" 00:00:00",enddate+" 00:00:00",cwbOrderType);
					cwbCount=cwbdao.findcwbByCwbsAndDateAndtypeCount(cwbs, startdate+" 00:00:00", enddate+" 00:00:00", cwbOrderType);
				}else{
					col=customerbillcontractservice.findAllByCwbDate(cwbs,startdate+" 00:00:00",enddate+" 00:00:00");
					cwbCount=cwbdao.findcwbByCwbsAndDateCount(cwbs, startdate+" 00:00:00", enddate+" 00:00:00");
				}								
			}else if(dateState==CwbDateEnum.RUKuRiQi.getValue()){
				col=customerbillcontractservice.findcwbByCwbsAndDateAndtypeedao(cwbs,startdate+" 00:00:00",enddate+" 00:00:00",cwbOrderType);
				cwbCount=edao.findcwbByCwbsAndDateAndtypeCount(cwbs,startdate+" 00:00:00",enddate+" 00:00:00",cwbOrderType);
			}else if(dateState==CwbDateEnum.FanKuiRiQi.getValue()){
				
			}
		
		
		
		
		
		
		return "";
	}
	

}
