package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.VO.CustomerBillContractFindConditionVO;
import cn.explink.domain.VO.CustomerBillContractVO;
import cn.explink.domain.VO.SerachCustomerBillContractVO;
import cn.explink.enumutil.BillStateEnum;
import cn.explink.enumutil.CwbDateEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CustomerBillContractService;
import cn.explink.service.PaiFeiRuleService;

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
	
	@Autowired
	private CustomerDAO customerdao;
	
	@Autowired
	private PaiFeiRuleService  paifeiruleservice;
	
	@RequestMapping("/CustomerBillContractList")
	public String CustomerBillContractlist(){	
		
		return "CustomerBillContract/CustomerBillContractList";
	}

	@RequestMapping("/AllBill")
	@ResponseBody
	public Map<String,Object> CustomerBillContractlist(			
			@RequestParam(value="page",defaultValue="0",required=true) String page,
			@RequestParam(value="rows",defaultValue="0",required=true) String rows,
			HttpServletRequest req
			) throws Exception{	
				//接受从前台接受的json对象
				String jsonStr=req.getParameter("jsoninfo");
				List<CustomerBillContract> cbclist=null;
				long dataCount=0; //数据数量默认为0
				if(jsonStr!=null&&!jsonStr.equals("")){  
				JSONObject jsonStu = JSONObject.fromObject(jsonStr);
				  CustomerBillContractFindConditionVO cccv=(CustomerBillContractFindConditionVO) JSONObject.toBean(jsonStu, CustomerBillContractFindConditionVO.class);
				  //根据传过来的查询条件查找符合条件的数据
				  Map<String,Long> cbcomap=customerbillcontractservice.getCustomerBillContractCountObject(jsonStr);  
					int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);   
			        //每页显示条数   
			        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);   
			        //每页的开始记录  第一页为1  第二页为number +1    
			        int start = (intPage-1)*number; 
					cbclist=customerbillcontractservice.getCustomerBillContractBySelect(cccv.getBillBatches(),cbcomap.get("billState"),cccv.getCrestartdate(),cccv.getCreenddate(),cccv.getVerificationstratdate(),cccv.getVerificationenddate(),cbcomap.get("customerId"),cbcomap.get("cwbOrderType"),cccv.getCondition(),cccv.getSequence(),start,number);
					dataCount=customerbillcontractservice.findCustomerBillContractCount(cccv.getBillBatches(),cbcomap.get("billState"),cccv.getCrestartdate(),cccv.getCreenddate(),cccv.getVerificationstratdate(),cccv.getVerificationenddate(),cbcomap.get("customerId"),cbcomap.get("cwbOrderType"));				
				}
					
			Map<String,Object> map = new HashMap<String, Object>();
				map.put("total",dataCount);
				map.put("rows",cbclist);
			return map;
	}
	
	@RequestMapping("/addBill")
	@ResponseBody
	public String add(
					@RequestParam(value="customerId") long customerid,
					@RequestParam(value="dateState") long dateState,
					@RequestParam(value="startdate") String startdate,
					@RequestParam(value="enddate") String enddate,
					@RequestParam(value="cwbOrderType") String cwbOrderType,
					@RequestParam(value="remark") String remark

				){		
			//查找所有该客户下的订单信息
				List<CwbOrder> cwborderlist=cwbdao.findCwbByCustomerid(customerid);
				//通过客户id查找该客户对象
				Customer customer=customerdao.getCustomerById(customerid);
				String cwbs=customerbillcontractservice.listToString(cwborderlist);			
				long cwbtype=-1; //默认订单类型-1不查询
			if(cwbOrderType!=null&&!cwbOrderType.equals("")){
				cwbtype=Integer.valueOf(cwbOrderType);
			}
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
			
				String dateRange=startdate+"至"+enddate;   //日期范围
				String BillBatches=customerbillcontractservice.getBillBatches(); //自动生成批次号
				long initbillState= BillStateEnum.WeiShenHe.getValue();  //默认未审核
				BigDecimal deliveryMoney=new BigDecimal("0");   //提货费
				BigDecimal distributionMoney=new BigDecimal("0"); //配送费
				BigDecimal transferMoney=new BigDecimal("0");	//中转费
				BigDecimal refuseMoney=new BigDecimal("0");  //拒收派费
			
				StringBuilder sb = new StringBuilder();
				/*List<SerachCustomerBillContractVO> svlist=new ArrayList<SerachCustomerBillContractVO>();*/
				long a=0;
				for(CwbOrder str:col){
					a+=1;
					
					distributionMoney=distributionMoney.add(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb()));
					deliveryMoney=deliveryMoney.add(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb()));
					transferMoney=transferMoney.add(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb()));
					//还差一个拒收派费暂时没有方法接口
					sb.append(str.getCwb()+",");
					
					//--------------------------
					
					SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();
					sv.setCwb(str.getCwb());
					sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbordertypeid()));
					sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbstate()).getText());
					sv.setDeliveryMoney(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb()));
					sv.setDistributionMoney(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb()));
					sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(str.getCwb()).getPaywayid())); //强转支付方式
					sv.setTransferMoney(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb()));
					sv.setRefuseMoney(new BigDecimal("0"));
					sv.setTotalCharge(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb())
							.add(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb())
							.add(paifeiruleservice.getPFRulefee(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb())))
							.add(new BigDecimal("0"))
							);
					sv.setBillBatches(BillBatches);
					
				
					customerbillcontractdao.addBillVo(sv);
				}
				System.out.println(a);
				String cwbsOfOneBill=sb.substring(0,sb.length()-1);
				BigDecimal totalCharge=new BigDecimal("0"); //派费总计	
				totalCharge=deliveryMoney.add(distributionMoney).add(transferMoney).add(refuseMoney);
				
				
				customerbillcontractservice.addBill(BillBatches,initbillState,customerid,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark,cwbtype,dateState,cwbsOfOneBill);
				/*CustomerBillContract customerBillContract= customerbillcontractdao.datebillBatche(BillBatches);
				CustomerBillContractVO cbcv = new CustomerBillContractVO();
				cbcv.setBillBatches(customerBillContract.getBillBatches());
				cbcv.setBillState(BillStateEnum.getTextByValue(customerBillContract.getBillState()));
				cbcv.setDateRange(customerBillContract.getDateRange());
				cbcv.setTotalCharge(customerBillContract.getTotalCharge());
				cbcv.setCorrespondingCwbNum(customerBillContract.getCorrespondingCwbNum());
				cbcv.setCustomername(customerdao.getCustomerName(customerBillContract.getCustomerId()));
				cbcv.setDeliveryMoney(customerBillContract.getDeliveryMoney());
				cbcv.setDistributionMoney(customerBillContract.getDistributionMoney());
				cbcv.setTransferMoney(customerBillContract.getTransferMoney());
				cbcv.setRemark(customerBillContract.getRemark());*/

					//使用VO累接受所查出的参数以json传到前台

				
		return "{\"success\":0,\"successdata\":\"添加成功\"}";  
		
	}
	
		@RequestMapping("/removeBill")
		@ResponseBody
		public String removeBill(@RequestParam("id") long id){
			customerbillcontractservice.removebill(id);
			customerbillcontractdao.removeSerachCustomerBillContractVOBybillbatches(customerbillcontractdao.findbill(id).getBillBatches());
			return "{\"success\":0,\"successdata\":\"删除成功\"}";
		}
		
		@RequestMapping("/removeBillInfoofEdit")
		@ResponseBody
		public String removeBill(HttpServletRequest req){
		String cwb=req.getParameter("cwb");	
		String billBatchs=req.getParameter("billBatches");
		CustomerBillContract c=customerbillcontractdao.datebillBatche(billBatchs); 
		SerachCustomerBillContractVO c1=customerbillcontractdao.findSerachCustomerBillContractVOByBillBatches(cwb);
		String cwbs=c.getCwbs();
		String cwbss[]=cwbs.split(",");
		StringBuilder sb = new StringBuilder();
		for(String cc:cwbss){
			if(cc.equals(cwb)){
				cc="";
			}   
			sb.append(cc+",");
		}
		String newcwb=sb.substring(0,sb.length()-1);
		
		long newcorrespondingCwbNum=c.getCorrespondingCwbNum()-1;		
		BigDecimal newDeliveryMoney=c.getDeliveryMoney().subtract(c1.getDeliveryMoney());
		BigDecimal newdistributionMoney=c.getDistributionMoney().subtract(c1.getDistributionMoney());
		BigDecimal newrefuseMoney=c.getRefuseMoney().subtract(c1.getRefuseMoney());
		BigDecimal newtransferMoney=c.getTransferMoney().subtract(c1.getTransferMoney());
		BigDecimal newtotalCharge=c.getTotalCharge().subtract(c1.getDeliveryMoney().add(c1.getDistributionMoney()).add(c1.getRefuseMoney()).add(c1.getTransferMoney()));
		customerbillcontractdao.updateCustomerBillContract(newcwb,billBatchs, newcorrespondingCwbNum, newDeliveryMoney, newdistributionMoney, newrefuseMoney, newtransferMoney, newtotalCharge);
		customerbillcontractdao.removeSerachCustomerBillContractVOByCwb(cwb);
		return "{\"success\":0,\"successdata\":\"删除成功\"}";
		}
		
		
		
		
		
		
		@RequestMapping("/editBill")
		@ResponseBody
		public String EditBill(CustomerBillContractVO cbv){
			customerbillcontractservice.EditBill(cbv);			
			return "{\"success\":0,\"successdata\":\"修改成功\"}";		
		}
		
		@RequestMapping("/editBillValue")
		@ResponseBody
		public CustomerBillContract EditBill(@RequestParam("id") long id){
			CustomerBillContract cbc=customerbillcontractservice.findCustomerBillContractById(id);			
			return cbc;		
		}
		
		@RequestMapping("/editAboutContent")	
		@ResponseBody
		public Map<String,Object> editAboutContent(@RequestParam(value="billBatches",required=true) String billBatches,
				@RequestParam(value="page",defaultValue="0",required=true) String page,
				@RequestParam(value="rows",defaultValue="0",required=true) String rows
				
				){
			int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);   
	        //每页显示条数   
	        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);   
	        //每页的开始记录  第一页为1  第二页为number +1    
	        int start = (intPage-1)*number; 
	        
	        List<SerachCustomerBillContractVO> svlist=  customerbillcontractdao.findSerachCustomerBillContractVOByBillBatches(billBatches,start,number);
	        long count=customerbillcontractdao.findSerachCustomerBillContractVOByBillBatchesCount(billBatches);
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("total",count);
				map.put("rows",svlist);
			
			return map;
		}
		
		@RequestMapping("/newAddofEditList")
		@ResponseBody
		public Map<String,Object> newAddofEditList(
				@RequestParam(value="cwb",required=true,defaultValue="") String cwb,
				@RequestParam(value="page",defaultValue="0",required=true) String page,
				@RequestParam(value="rows",defaultValue="0",required=true) String rows
				
				){
			int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);   
	        //每页显示条数   
	        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);   
	        //每页的开始记录  第一页为1  第二页为number +1    
	        int start = (intPage-1)*number; 
	        List<CwbOrder> col=null;
	        long correspondingCwbNum=0;
	      /*  JSONObject jsonCwb = JSONObject.fromObject(cwb);
	        String strCwb=jsonCwb.get(cwb).toString();
*/
	     if(cwb!=null&&!cwb.equals("")){
	        	col=cwbdao.getAllCwbOrderByCwbPage(cwb,start,number);
	        	correspondingCwbNum=cwbdao.getAllCwbOrderByCwbPageCount(cwb);
	        }else{
	        	col=cwbdao.getAllCwbOrder(start,number);
	        	correspondingCwbNum=cwbdao.getAllCwbOrderCount();
	        }

			List<SerachCustomerBillContractVO> svlist=new ArrayList<SerachCustomerBillContractVO>();
				for(CwbOrder str:col){
					SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();
					sv.setCwb(str.getCwb());
					sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbordertypeid()));
					sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbstate()).getText());
					sv.setDeliveryMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb()));
					sv.setDistributionMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb()));
					sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(str.getCwb()).getPaywayid())); //强转支付方式
					sv.setTransferMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb()));
					sv.setRefuseMoney(new BigDecimal("0"));
					sv.setTotalCharge(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb())
							.add(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb())
							.add(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb())))
							.add(new BigDecimal("0"))
							);
					
					svlist.add(sv);
				}
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("total",correspondingCwbNum);
				map.put("rows",svlist);
			return map;			
		}
		
		@RequestMapping("/findCustomerBillContractVOByBillBatches")
		@ResponseBody
		public CustomerBillContract findCustomerBillContractVOByBillBatches(
				@RequestParam
				(value="billBatches",defaultValue="",required=true) String billBatches){
			System.out.println(billBatches);
			CustomerBillContract customerBillContract= customerbillcontractdao.datebillBatche(billBatches);
			
			/*CustomerBillContractVO cbcv = new CustomerBillContractVO();
			cbcv.setBillBatches(customerBillContract.getBillBatches());
			cbcv.setBillState(BillStateEnum.getTextByValue(customerBillContract.getBillState()));
			cbcv.setDateRange(customerBillContract.getDateRange());
			cbcv.setTotalCharge(customerBillContract.getTotalCharge());
			cbcv.setCorrespondingCwbNum(customerBillContract.getCorrespondingCwbNum());
			cbcv.setCustomername(customerdao.getCustomerName(customerBillContract.getCustomerId()));
			cbcv.setDeliveryMoney(customerBillContract.getDeliveryMoney());
			cbcv.setDistributionMoney(customerBillContract.getDistributionMoney());
			cbcv.setTransferMoney(customerBillContract.getTransferMoney());
			cbcv.setRemark(customerBillContract.getRemark());*/
			
			return customerBillContract;
		}

}
