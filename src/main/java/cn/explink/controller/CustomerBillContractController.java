package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.BillStateEnum;
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
	
	@Autowired
	private DeliveryStateDAO deliverystatedao;
	
	
	@RequestMapping("/CustomerBillContractList")
	public String CustomerBillContractlist(@RequestParam(value="billBatches",defaultValue="",required=false) String billBatches,
			@RequestParam(value="billState",defaultValue="-1",required=false) long billState,
			@RequestParam(value="crestartdate",defaultValue="",required=false) String crestartdate,
			@RequestParam(value="creenddate",defaultValue="",required=false) String creenddate,
			@RequestParam(value="verificationstratdate",defaultValue="",required=false) String verificationstratdate,
			@RequestParam(value="verificationenddate",defaultValue="",required=false) String verificationenddate,
			@RequestParam(value="customerId",defaultValue="-1",required=false) long customerId,
			@RequestParam(value="cwbOrderType",defaultValue="-1",required=false) long cwbOrderType,
			@RequestParam(value="condition",defaultValue="billBatches",required=false) String condition,
			@RequestParam(value="sequence",defaultValue="asc",required=false) String sequence,
			Model model
			){	
			List<CustomerBillContract> cbclist=customerbillcontractservice.getCustomerBillContractBySelect(billBatches,billState,crestartdate,creenddate,verificationstratdate,verificationenddate,customerId,cwbOrderType,condition,sequence);
			model.addAttribute("customerbillcontract", cbclist);
	
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
			List<CwbOrder> cwborderlist=cwbdao.findCwbByCustomerid(id);
			String cwbs=customerbillcontractservice.listToString(cwborderlist);
			long cwbtype=Integer.valueOf(cwbOrderType);
			List<CwbOrder> col=null;
			long correspondingCwbNum=0;
			if(dateState==CwbDateEnum.ShenHeRiQi.getValue()){
				
				List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtypeShenHe(cwbs,startdate+" 00:00:00",enddate+" 00:00:00");
				String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
				
				if(cwbtype>0){
					col=cwbdao.getCwbByCwbsAndType(dcwbs,cwbOrderType);
					correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
				}else{
					col=cwbdao.getCwbByCwbs(dcwbs);
					correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
				}	
			
				}else if(dateState==CwbDateEnum.FaHuoRiQi.getValue()){
					if(cwbtype>0){
						col=customerbillcontractservice.findAllByCwbDateType(cwbs,startdate+" 00:00:00",enddate+" 00:00:00",cwbOrderType);
						correspondingCwbNum=cwbdao.findcwbByCwbsAndDateAndtypeCount(cwbs, startdate+" 00:00:00", enddate+" 00:00:00", cwbOrderType);
					}else{
						col=customerbillcontractservice.findAllByCwbDate(cwbs,startdate+" 00:00:00",enddate+" 00:00:00");
						correspondingCwbNum=cwbdao.findcwbByCwbsAndDateCount(cwbs, startdate+" 00:00:00", enddate+" 00:00:00");
					}								
				}else if(dateState==CwbDateEnum.RUKuRiQi.getValue()){
				col=customerbillcontractservice.findcwbByCwbsAndDateAndtypeedao(cwbs,startdate+" 00:00",enddate+" 00:00",cwbOrderType);
				correspondingCwbNum=edao.findcwbByCwbsAndDateAndtypeCount(cwbs,startdate+" 00:00",enddate+" 00:00",cwbOrderType);
				}else if(dateState==CwbDateEnum.FanKuiRiQi.getValue()){
				List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtype(cwbs,startdate+" 00:00:00",enddate+" 00:00:00");
				String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
				
					if(cwbtype>0){
						col=cwbdao.getCwbByCwbsAndType(dcwbs,cwbOrderType);
						correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
					}else{
						col=cwbdao.getCwbByCwbs(dcwbs);
						correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
					}				
			}		
			String dateRange=startdate+"の"+enddate;   //日期范围
			String BillBatches=customerbillcontractservice.getBillBatches(); //自动生成批次号
			long initbillState= BillStateEnum.WeiShenHe.getValue();  //默认未审核
			long deliveryMoney=0;
			long distributionMoney=0;
			long transferMoney=0;
			long refuseMoney=0;
			long totalCharge=deliveryMoney+distributionMoney+transferMoney+refuseMoney;
		customerbillcontractservice.addBill(BillBatches,initbillState,id,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark);
	
		return "{\"success\":0,\"successdata\":\"添加成功\"}";   
		
	}
	
		@RequestMapping("/removeBill")
		public String removeBill(@RequestParam("id") long id){
			customerbillcontractservice.removebill(id);
			return "{\"success\":0,\"successdata\":\"删除成功\"}";
		}
		

}
