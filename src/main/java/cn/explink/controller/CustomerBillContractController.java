package cn.explink.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.ImportBillExcel;
import cn.explink.domain.VO.BillMoneyDuiBiChaYiVO;
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
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.PaiFeiRuleService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/CustomerBillContract")
public class CustomerBillContractController {
	
	@Autowired
	private PaiFeiRuleDAO pfra;
	
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
	
	@Autowired
	private Excel2007Extractor ex;
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
				int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);   
		        //每页显示条数   
		        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);   
		        //每页的开始记录  第一页为1  第二页为number +1    
		        int start = (intPage-1)*number; 
				//接受从前台接受的json对象
				String jsonStr=req.getParameter("jsoninfo");
				List<CustomerBillContract> cbclist=null;
				long dataCount=0; //数据数量默认为0
				if(jsonStr!=null&&!jsonStr.equals("")){  
				JSONObject jsonStu = JSONObject.fromObject(jsonStr);
				  CustomerBillContractFindConditionVO cccv=(CustomerBillContractFindConditionVO) JSONObject.toBean(jsonStu, CustomerBillContractFindConditionVO.class);
				  //根据传过来的查询条件查找符合条件的数据
				  Map<String,Long> cbcomap=customerbillcontractservice.getCustomerBillContractCountObject(jsonStr);  
					String crestartdate=null;
					String creenddate=null;
					if(cccv.getCrestartdate().equals("")){
						crestartdate=customerbillcontractservice.getMonthFirstDay(); //如果查询创建时间日期条件为空，默认为当月第一天
					}else{
						crestartdate=cccv.getCrestartdate();
					}
					if(cccv.getCreenddate().equals("")){
						creenddate=DateTimeUtil.getNowDate(); //如果查询创建时间日期条件为空，默认当前系统时间
					}else{
						creenddate=cccv.getCreenddate();
					}
					cbclist=customerbillcontractservice.getCustomerBillContractBySelect(cccv.getBillBatches(),cbcomap.get("billState"),crestartdate,creenddate,cccv.getVerificationstratdate(),cccv.getVerificationenddate(),cbcomap.get("customerId"),cbcomap.get("cwbOrderType"),cccv.getCondition().equals("") ? "bill_batches":cccv.getCondition(),cccv.getSequence().equals("") ? "ASC" :cccv.getSequence(),start,number);
					dataCount=customerbillcontractservice.findCustomerBillContractCount(cccv.getBillBatches(),cbcomap.get("billState"),crestartdate,creenddate,cccv.getVerificationstratdate(),cccv.getVerificationenddate(),cbcomap.get("customerId"),cbcomap.get("cwbOrderType"));				
				}else{
					cbclist=customerbillcontractdao.dateAllbillBatche(start,number);
					dataCount=customerbillcontractdao.dateAllbillCount();
				}
					
			Map<String,Object> map = new HashMap<String, Object>();
				map.put("total",dataCount);
				map.put("rows",cbclist);
			return map;
	}
	
	@RequestMapping("/addBill")
	@ResponseBody
	public CustomerBillContract add(
					@RequestParam(value="crecustomerId") long customerid,
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
				if(cwbs==null){
					return null;
				}
				
				List<CwbOrder> col=null;
				long correspondingCwbNum=0;
		
			if(dateState==CwbDateEnum.ShenHeRiQi.getValue()){
				
				List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtypeShenHe(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
				String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
				if(dcwbs!=null){
						if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
							col=cwbdao.getCwbByCwbsAndType(dcwbs,cwbOrderType);
							correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
						}else{
							col=cwbdao.getCwbByCwbs(dcwbs);
							correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
						}	
					}else{
						col=null;
					}
			
				}else if(dateState==CwbDateEnum.FaHuoRiQi.getValue()){
					if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
						col=customerbillcontractservice.findAllByCwbDateType(cwbs,startdate+" 00:00:00",enddate+" 23:59:59",cwbOrderType);
						correspondingCwbNum=cwbdao.findcwbByCwbsAndDateAndtypeCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59", cwbOrderType);
					}else{
						col=customerbillcontractservice.findAllByCwbDate(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
						correspondingCwbNum=cwbdao.findcwbByCwbsAndDateCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59");
					}								
				}else if(dateState==CwbDateEnum.RUKuRiQi.getValue()){
					col=customerbillcontractservice.findcwbByCwbsAndDateAndtypeedao(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType);		
					correspondingCwbNum=edao.findcwbByCwbsAndDateAndtypeCount(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType);
				}else if(dateState==CwbDateEnum.FanKuiRiQi.getValue()){
					List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtype(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
					String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
					if(dcwbs!=null){
							if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
								col=cwbdao.getCwbByCwbsAndType(dcwbs,cwbOrderType);
								correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
							}else{
								col=cwbdao.getCwbByCwbs(dcwbs);
								correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
							}	
					}else{
						col=null;
					}	
			}	
		
				String dateRange=startdate+"至"+enddate;   //日期范围
				String BillBatches=customerbillcontractservice.getBillBatches(); //自动生成批次号
				long initbillState= BillStateEnum.WeiShenHe.getValue();  //默认未审核
				BigDecimal deliveryMoney=BigDecimal.ZERO;   //提货费
				BigDecimal distributionMoney=BigDecimal.ZERO; //配送费
				BigDecimal transferMoney=BigDecimal.ZERO;	//中转费
				BigDecimal refuseMoney=BigDecimal.ZERO;  //拒收派费
				BigDecimal totalCharge=BigDecimal.ZERO; //派费总计	
				
				

			Map<String,BigDecimal>	map=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong, col);
			Map<String,BigDecimal>	map1=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, col);
			Map<String,BigDecimal>	map2=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, col);
			if(col!=null){
				StringBuilder sb = new StringBuilder();
				/*List<SerachCustomerBillContractVO> svlist=new ArrayList<SerachCustomerBillContractVO>();*/
				for(CwbOrder str:col){					
					distributionMoney=distributionMoney.add(map.get(str.getCwb())==null?BigDecimal.ZERO:map.get(str.getCwb()));
					deliveryMoney=deliveryMoney.add(map1.get(str.getCwb())==null?BigDecimal.ZERO:map1.get(str.getCwb()));
					transferMoney=transferMoney.add(map2.get(str.getCwb())==null?BigDecimal.ZERO:map2.get(str.getCwb()));
					refuseMoney=refuseMoney.add(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? BigDecimal.ZERO:pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee());
					sb.append(str.getCwb()+",");
					
					//--------------------------
					
					SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();
					sv.setCwb(str.getCwb());
					sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbordertypeid()));
					sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbstate()).getText());
					sv.setDeliveryMoney(map1.get(str.getCwb())==null?BigDecimal.ZERO:map1.get(str.getCwb()));
					sv.setDistributionMoney(map.get(str.getCwb())==null?BigDecimal.ZERO:map.get(str.getCwb()));
					sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(str.getCwb()).getPaywayid())); //强转支付方式
					sv.setTransferMoney(map2.get(str.getCwb())==null?BigDecimal.ZERO:map2.get(str.getCwb()));
					sv.setRefuseMoney(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee());
					sv.setTotalCharge(map.get(str.getCwb())==null?BigDecimal.ZERO:map.get(str.getCwb())
							.add(map1.get(str.getCwb())==null?BigDecimal.ZERO:map1.get(str.getCwb()))
							.add(map2.get(str.getCwb())==null?BigDecimal.ZERO:map2.get(str.getCwb()))
							.add(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? BigDecimal.ZERO:pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee())
							);
					sv.setBillBatches(BillBatches);
					
				
					customerbillcontractdao.addBillVo(sv);  //生成的账单关联的所有订单
				}
				String cwbsOfOneBill=null;
				if(sb.length()>0){
				cwbsOfOneBill=sb.substring(0,sb.length()-1);
				}
				totalCharge=deliveryMoney.add(distributionMoney).add(transferMoney).add(refuseMoney).add(refuseMoney);
				customerbillcontractservice.addBill(BillBatches,initbillState,customerid,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark,cwbOrderType,dateState,cwbsOfOneBill);

				
				CustomerBillContract c=customerbillcontractdao.datebillBatche(BillBatches);
				
				if(col!=null&&correspondingCwbNum!=0){
					return c;  
				}
			}
				return null;
	
	}
	
		@RequestMapping("/removeBill")
		@ResponseBody
		public String removeBill(@RequestParam("id") long id){
			
			customerbillcontractdao.removeSerachCustomerBillContractVOBybillbatches(customerbillcontractdao.findbill(id).getBillBatches());
			customerbillcontractservice.removebill(id);
			return "{\"success\":0,\"successdata\":\"删除成功\"}";
		}
		
		@RequestMapping("/removeBillInfoofEdit")
		@ResponseBody
		public String removeBill(HttpServletRequest req){
		String cwb=req.getParameter("cwb");	
		String billBatchs=req.getParameter("billBatches");
		SerachCustomerBillContractVO cs=customerbillcontractdao.queryCustomerbillcontractvo(cwb,billBatchs);
		if(cs!=null){
		CustomerBillContract c=customerbillcontractdao.datebillBatche(billBatchs); 
		SerachCustomerBillContractVO c1=customerbillcontractdao.findSerachCustomerBillContractVOBycwb(cwb);
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
			BigDecimal newDeliveryMoney=c.    getDeliveryMoney().subtract(c1.getDeliveryMoney());
			BigDecimal newdistributionMoney=c.getDistributionMoney().subtract(c1.getDistributionMoney());
			BigDecimal newrefuseMoney=c.getRefuseMoney().subtract(c1.getRefuseMoney());
			BigDecimal newtransferMoney=c.getTransferMoney().subtract(c1.getTransferMoney());
			BigDecimal newtotalCharge=c.getTotalCharge().subtract(c1.getDeliveryMoney().add(c1.getDistributionMoney()).add(c1.getRefuseMoney()).add(c1.getTransferMoney()));
			customerbillcontractdao.updateCustomerBillContract(newcwb,billBatchs, newcorrespondingCwbNum, newDeliveryMoney, newdistributionMoney, newrefuseMoney, newtransferMoney, newtotalCharge);
			customerbillcontractdao.removeSerachCustomerBillContractVOByCwb(cwb);
			return "{\"success\":0,\"successdata\":\"删除成功\"}";
		}
		return "{\"success\":1,\"successdata\":\"该订单不存在\"}";
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
				@RequestParam(value="rows",defaultValue="0",required=true) String rows,
				HttpServletRequest req
				){
			int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);   
	        //每页显示条数   
	        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);   
	        //每页的开始记录  第一页为1  第二页为number +1    
	        int start = (intPage-1)*number; 
	      
	        String billBatches=req.getParameter("billBatches");
	        //根据账单批次查找账单
	    	CustomerBillContract c=customerbillcontractdao.datebillBatche(billBatches);
	    	long customerid=c.getCustomerId();  //过去账单所属客户
	    	Customer customer=customerdao.getCustomerById(customerid);
	    	String rangeDate=c.getDateRange().trim(); //查找该账单所属的订单的日期范围
	    	long dateState=c.getDateState();//"2015-03-18至2015-07-13"
	    	String startdate=rangeDate.substring(0,10).trim();
	    	String enddate=rangeDate.substring(11,21).trim();
	    	String cwbOrderType=String.valueOf(c.getCwbOrderType()); //该账单所属的订单类型
	    	//查找所有该客户下的订单信息
			List<CwbOrder> cwborderlist=cwbdao.findCwbByCustomerid(customerid);
			List<CwbOrder> col=null;
			long correspondingCwbNum=0;
			String cwbs=null;	
			 
			 if(cwb!=null&&!cwb.equals("")){
				  cwbs=cwb;
					
					if(dateState==CwbDateEnum.ShenHeRiQi.getValue()){
						
						List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtypeShenHelike(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
						String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
						if(dcwbs!=null){
							if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
								col=cwbdao.getCwbByCwbsAndTypeByPage(dcwbs,cwbOrderType,start,number);
								correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
							}else{
								col=cwbdao.getCwbByCwbsByPage(dcwbs,start,number);
								correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
							}	
						}
						}else if(dateState==CwbDateEnum.FaHuoRiQi.getValue()){
							if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
								col=customerbillcontractservice.findAllByCwbDateTypeLike(cwbs,startdate+" 00:00:00",enddate+" 23:59:59",cwbOrderType,start,number);
								correspondingCwbNum=cwbdao.findcwbByCwbsAndDateAndtypeEditCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59", cwbOrderType);
							}else{
								col=customerbillcontractservice.findAllByCwbDatelike(cwbs,startdate+" 00:00:00",enddate+" 23:59:59",start,number);
								correspondingCwbNum=cwbdao.findcwbByCwbsAndDateEditCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59");
							}								
						}else if(dateState==CwbDateEnum.RUKuRiQi.getValue()){
							col=customerbillcontractservice.findcwbByCwbsAndDateAndtypeedaolike(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType,start,number);
							correspondingCwbNum=edao.findcwbByCwbsAndDateAndtypeLikeCount(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType);
						}else if(dateState==CwbDateEnum.FanKuiRiQi.getValue()){
							List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtypelike(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
							String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
							if(dcwbs!=null){
							if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
								col=cwbdao.getCwbByCwbsAndTypeByPage(dcwbs,cwbOrderType,start,number);
								correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
							}else{
								col=cwbdao.getCwbByCwbsByPage(dcwbs,start,number);
								correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
							}		
						}	
					}	
					
		        }else{
		        	cwbs=customerbillcontractservice.listToString(cwborderlist);	
		        	
		    		if(dateState==CwbDateEnum.ShenHeRiQi.getValue()){
		    			
		    			List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtypeShenHe(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
		    			String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
		    			if(dcwbs!=null){
			    			if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
			    				col=cwbdao.getCwbByCwbsAndTypeByPage(dcwbs,cwbOrderType,start,number);
			    				correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
			    			}else{
			    				col=cwbdao.getCwbByCwbsByPage(dcwbs,start,number);
			    				correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
			    			}	
		    			}
		    			}else if(dateState==CwbDateEnum.FaHuoRiQi.getValue()){
		    				if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
		    					col=customerbillcontractservice.findAllByCwbDateType(cwbs,startdate+" 00:00:00",enddate+" 23:59:59",cwbOrderType,start,number);
		    					correspondingCwbNum=cwbdao.findcwbByCwbsAndDateAndtypeCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59", cwbOrderType);
		    				}else{
		    					col=customerbillcontractservice.findAllByCwbDate(cwbs,startdate+" 00:00:00",enddate+" 23:59:59",start,number);
		    					correspondingCwbNum=cwbdao.findcwbByCwbsAndDateCount(cwbs, startdate+" 00:00:00", enddate+" 23:59:59");
		    				}								
		    			}else if(dateState==CwbDateEnum.RUKuRiQi.getValue()){
		    				col=customerbillcontractservice.findcwbByCwbsAndDateAndtypeedao(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType,start,number);
		    				correspondingCwbNum=edao.findcwbByCwbsAndDateAndtypeCount(cwbs,startdate+" 00:00",enddate+" 23:59",cwbOrderType);
		    			}else if(dateState==CwbDateEnum.FanKuiRiQi.getValue()){
		    				List<DeliveryState> lds=deliverystatedao.findcwbByCwbsAndDateAndtype(cwbs,startdate+" 00:00:00",enddate+" 23:59:59");
		    				String dcwbs=customerbillcontractservice.DeliveryStatelistToString(lds);
		    				if(dcwbs!=null){
			    				if(cwbOrderType!=null&&!cwbOrderType.equals("")&&Long.valueOf(cwbOrderType)>0){
			    					col=cwbdao.getCwbByCwbsAndTypeByPage(dcwbs,cwbOrderType,start,number);
			    					correspondingCwbNum=cwbdao.getCwbByCwbsAndTypeCount(dcwbs,cwbOrderType);
			    				}else{
			    					col=cwbdao.getCwbByCwbsByPage(dcwbs,start,number);
			    					correspondingCwbNum=cwbdao.getCwbByCwbsCount(dcwbs);
			    				}	
		    				}	
		    		}	
		    		
		        }
		
		
			 	Map<String,BigDecimal>	map=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong, col);
				Map<String,BigDecimal>	map1=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, col);
				Map<String,BigDecimal>	map2=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, col);
			List<SerachCustomerBillContractVO> svlist=new ArrayList<SerachCustomerBillContractVO>();
				for(CwbOrder str:col){
					SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();	
					sv.setCwb(str.getCwb());
					sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbordertypeid()));
					sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbstate()).getText());
					sv.setDeliveryMoney(map1.get(str.getCwb())==null?BigDecimal.ZERO:map1.get(str.getCwb()));
					sv.setDistributionMoney(map.get(str.getCwb())==null?BigDecimal.ZERO:map.get(str.getCwb()));
					sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(str.getCwb()).getPaywayid())); //强转支付方式
					sv.setTransferMoney(map2.get(str.getCwb())==null?BigDecimal.ZERO:map2.get(str.getCwb()));
					sv.setRefuseMoney(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee());
					sv.setTotalCharge(map.get(str.getCwb())==null?BigDecimal.ZERO:map.get(str.getCwb())
							.add(map1.get(str.getCwb())==null?BigDecimal.ZERO:map1.get(str.getCwb()))
							.add(map2.get(str.getCwb())==null?BigDecimal.ZERO:map2.get(str.getCwb()))
							.add(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? BigDecimal.ZERO:pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee())
							);
						
					svlist.add(sv);
				}
				Map<String,Object> mapx = new HashMap<String, Object>();
				mapx.put("total",correspondingCwbNum);
				mapx.put("rows",svlist);
			return mapx;			
		}
		
		@RequestMapping("/findCustomerBillContractVOByBillBatches")
		@ResponseBody
		public CustomerBillContractVO findCustomerBillContractVOByBillBatches(
				@RequestParam(value="billBatches",defaultValue="",required=true) String billBatches){

			CustomerBillContract customerBillContract= customerbillcontractdao.datebillBatche(billBatches);
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
				cbcv.setRemark(customerBillContract.getRemark());

			return cbcv;
		}
		

		@RequestMapping("/changeBillState")
		@ResponseBody
		public String changeBillState(HttpServletRequest req){
			String billState=req.getParameter("billState");	
			String billBatchs=req.getParameter("billBatches");
			
			customerbillcontractdao.changeBillState(Integer.valueOf(billState),billBatchs);
			if(Integer.valueOf(billState)==BillStateEnum.YiHeXiao.getValue()){
				customerbillcontractdao.removeImportExcelByBatches(billBatchs);	
			}
			
			return "{\"success\":0,\"successdata\":\"修改成功\"}";		
		}
		
		@RequestMapping("/getupdateExcel")
		@ResponseBody
		public String getupdateExcel(@RequestParam("uploadExcel") CommonsMultipartFile uploadExcel,  
	            HttpServletRequest request, HttpServletResponse response)throws Exception{
					String billBatches=request.getParameter("billBatches");
			InputStream in = null;
					
				in = uploadExcel.getInputStream();
				/*POIFSFileSystem pfs = new POIFSFileSystem(in);*/
				List<ImportBillExcel> objlist=customerbillcontractservice.getUploadExcel(in);
/*				BigDecimal b = new BigDecimal("0");*/
				if(objlist==null){
					return "{\"success\":2,\"successdata\":\"上传文本格式错误\"}";
				}
					StringBuilder sb = new StringBuilder();
					String cwbs="";	
					if(objlist.size()>0){
						for(ImportBillExcel str:objlist){
							sb=sb.append("'"+str.getCwb()+"',");
						}
						cwbs=sb.substring(0, sb.length()-1);	
					}
					List<ImportBillExcel> lc=customerbillcontractdao.queryImportBillExcel(cwbs,billBatches);
					if(lc!=null&&lc.size()>0){						
						return "{\"success\":1,\"successdata\":\"已存在相关上传文件\"}";
					}
				
				for(ImportBillExcel o:objlist){
					customerbillcontractdao.addBillloadExcel(o, billBatches);
				}
				return "{\"success\":0,\"successdata\":\"上传成功\"}";		
					
		}
		
		@RequestMapping("/addCwbInEdit")
		@ResponseBody
		public String addCwbInEdit(HttpServletRequest req){
			String cwb=req.getParameter("cwb");	
			String billBatchs=req.getParameter("billBatches");
			CustomerBillContract c=customerbillcontractdao.datebillBatche(billBatchs); 
			CwbOrder str=cwbdao.getOneCwbOrderByCwb(cwb);  
			Customer customer=customerdao.getCustomerById(str.getCustomerid());
			SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();
				sv.setCwb(str.getCwb());
				sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbordertypeid()));
				sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(str.getCwb()).getCwbstate()).getText());
				sv.setDeliveryMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb()));
				sv.setDistributionMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb()));
				sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(str.getCwb()).getPaywayid())); //强转支付方式
				sv.setTransferMoney(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb()));
				sv.setRefuseMoney(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee());
				sv.setTotalCharge(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Tihuo, str.getCwb())
						.add(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Paisong, str.getCwb())
						.add(paifeiruleservice.getPFRulefee(customerdao.getCustomerById(str.getCustomerid()).getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, str.getCwb())))
						.add(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee())
						);
				sv.setBillBatches(billBatchs);
			String cwbs=c.getCwbs();
			String newcwb=null;
			Boolean bl=false;
				if(!cwbs.contains(cwb)){					
					newcwb=cwbs+","+cwb;
					bl=true;
				} 				
			
			if(bl==true){
				long newcorrespondingCwbNum=c.getCorrespondingCwbNum()+1;		
				BigDecimal newDeliveryMoney=c.getDeliveryMoney().add(sv.getDeliveryMoney());
				BigDecimal newdistributionMoney=c.getDistributionMoney().add(sv.getDistributionMoney());
				BigDecimal newrefuseMoney=c.getRefuseMoney().add(sv.getRefuseMoney());
				BigDecimal newtransferMoney=c.getTransferMoney().add(sv.getTransferMoney());
				BigDecimal newtotalCharge=c.getTotalCharge().add(sv.getDeliveryMoney().add(sv.getDistributionMoney()).add(sv.getRefuseMoney()).add(sv.getTransferMoney()));
				customerbillcontractdao.updateCustomerBillContract(newcwb,billBatchs, newcorrespondingCwbNum, newDeliveryMoney, newdistributionMoney, newrefuseMoney, newtransferMoney, newtotalCharge);
				customerbillcontractdao.addBillVo(sv);
			}else{
				return "{\"success\":1,\"successdata\":\"该订单已存在\"}";
			}
		return "{\"success\":0,\"successdata\":\"添加成功\"}";		
		}
		
		
	@RequestMapping("/billMoneyWithImportExcelChaYiDuiBi")	
	@ResponseBody
	public BillMoneyDuiBiChaYiVO billMoneyWithImportExcelChaYiDuiBi(HttpServletRequest req){
			String batches=req.getParameter("billBatches");
			CustomerBillContract cc=customerbillcontractdao.findCustomerBillContractByBillBatches(batches);		
			List<ImportBillExcel> lbe=customerbillcontractdao.findImportBillExcelByBatches(batches);
			BillMoneyDuiBiChaYiVO b = new BillMoneyDuiBiChaYiVO();
		if(lbe.size()>0){
			
			long systemDateCount=cc.getCorrespondingCwbNum();
			long importDateCount=lbe.size();
			long chaYi=systemDateCount-importDateCount;
			BigDecimal systemAllMoney=cc.getTotalCharge();
			BigDecimal importAllMoney = new BigDecimal("0");
			
			for(ImportBillExcel l:lbe){
				//求出导入的数据总和
				importAllMoney=importAllMoney.add(l.getJijiaMoney()).add(l.getXuzhongMoney()).add(l.getFandanMoney()).add(l.getFanchengMoney()).add(l.getDaishoukuanshouxuMoney()).add(l.getPosShouxuMoney()).add(l.getBaojiaMoney()).add(l.getBaozhuangMoney()).add(l.getGanxianbutieMoney());
			}
				b.setImportDateCount(importDateCount);
				b.setSystemDateCount(systemDateCount);
				b.setChaYiCount(chaYi);
				b.setSystemDateMoney(systemAllMoney);
				b.setImportDateMoney(importAllMoney);
				b.setChaYiMoney(systemAllMoney.subtract(importAllMoney));
			
			String cwbs[]=cc.getCwbs().toString().split(",");
			StringBuilder sb = new StringBuilder();
			String cwbss="";
			for(String s:cwbs){
				SerachCustomerBillContractVO c=customerbillcontractdao.findSerachCustomerBillContractVOBycwb(s);
				
				for(ImportBillExcel l:lbe){   //订单相同，总额却不同的，对比不上的不比对
					if(s.equals(l.getCwb())){
						if(!c.getTotalCharge().setScale(2,BigDecimal.ROUND_HALF_DOWN).equals(l.getJijiaMoney().add(l.getXuzhongMoney()).add(l.getFandanMoney()).add(l.getFanchengMoney()).add(l.getDaishoukuanshouxuMoney()).add(l.getPosShouxuMoney()).add(l.getBaojiaMoney()).add(l.getBaozhuangMoney()).add(l.getGanxianbutieMoney()).setScale(2,BigDecimal.ROUND_HALF_DOWN))){
							sb.append(l.getCwb()+",");
						}
					}
				}
			}
			if(sb.length()>0){
				cwbss=sb.substring(0,sb.length()-1).trim().toString(); //订单相同，总额却不同的cwbs
			}
			if(cwbss.length()>0){
				String cwbsss[]=cwbss.split(",");
				b.setDuibiCwbMoneyChaYi(cwbsss.length);
			}else{
				b.setDuibiCwbMoneyChaYi(0);
				}
			}
		
		return b;
	}	
		

	@RequestMapping("/CountChaYi")
	@ResponseBody
	public List<SerachCustomerBillContractVO> CountChaYi(HttpServletRequest req){
		String batches=req.getParameter("billBatches");
		CustomerBillContract cc=customerbillcontractdao.findCustomerBillContractByBillBatches(batches);		
		Customer customer=customerdao.getCustomerById(cc.getCustomerId());
		List<ImportBillExcel> lbe=customerbillcontractdao.findImportBillExcelByBatches(batches);
		StringBuilder sb = new StringBuilder();   //导入的多
		StringBuilder sb1 = new StringBuilder();  //系统的多
		String ccwbs="";
		String cwbs[]=cc.getCwbs().split(",");
		List<CwbOrder> c = new ArrayList<CwbOrder>();
		for(String s:cwbs){
			CwbOrder cb = new CwbOrder();
				cb.setCwb(s);
				c.add(cb);
		}
		String cwbs1=customerbillcontractservice.listImportBillExcelToString(lbe);
		if(cwbs!=null&&!cwbs.equals("")){
			if(cc.getCorrespondingCwbNum()<lbe.size()){  
				for(ImportBillExcel i:lbe){
						if(!cc.getCwbs().contains(i.getCwb())){
							sb.append(i.getCwb()+",");
							
						}
				}
			
			}else{
				for(CwbOrder cwborder:c){
					if(!cwbs1.contains(cwborder.getCwb())){
						sb1.append(cwborder.getCwb()+",");
					}
				}	
			}
		}
		System.out.println(sb.length());
		if(sb!=null&&!sb.equals("")&&sb.length()>0){
			ccwbs="导入多余订单"+":"+sb.substring(0,sb.length()-1).toString();
		}else if(sb1!=null&&!sb1.equals("")&&sb1.length()>0){
			ccwbs="DMP多余订单"+":"+sb1.substring(0,sb1.length()-1).toString();
		}
		String ccwbs1[]=ccwbs.trim().split(":");
		String zhongLei="";
		if(ccwbs1[0].equals("导入多余订单")){
			zhongLei="导入多余订单";
		}else if(ccwbs1[0].equals("DMP多余订单")){
			zhongLei="DMP多余订单";
		}
		String cwbgetChYi[]=ccwbs1[1].trim().split(",");
		List<CwbOrder> co = new ArrayList<CwbOrder>();
		for(String s:cwbgetChYi){
			CwbOrder e = new CwbOrder();
			e.setCwb(s);
			co.add(e);
		}
		
		
		List<SerachCustomerBillContractVO> ls= new ArrayList<SerachCustomerBillContractVO>();

		Map<String,BigDecimal>	map=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Paisong,co);
		Map<String,BigDecimal>	map1=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Tihuo, co);
		Map<String,BigDecimal>	map2=paifeiruleservice.getPFRulefeeOfBatch(customer.getPfruleid(), PaiFeiRuleTabEnum.Zhongzhuan, co);
		for(String s:cwbgetChYi){			
				SerachCustomerBillContractVO sv=new SerachCustomerBillContractVO();
				sv.setCwb(s);
				sv.setCwbOrderType(CwbOrderTypeIdEnum.getTextByValue(cwbdao.getOneCwbOrderByCwb(s).getCwbordertypeid()));
				sv.setCwbstate(CwbStateEnum.getByValue(cwbdao.getOneCwbOrderByCwb(s).getCwbstate()).getText());
				sv.setDeliveryMoney(map1.get(s)==null?BigDecimal.ZERO:map1.get(s));
				sv.setDistributionMoney(map.get(s)==null?BigDecimal.ZERO:map.get(s));
				sv.setPaywayid(PaytypeEnum.getTextByValue((int)cwbdao.getOneCwbOrderByCwb(s).getPaywayid())); //强转支付方式
				sv.setTransferMoney(map2.get(s)==null?BigDecimal.ZERO:map2.get(s));
				sv.setRefuseMoney(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee());
				sv.setTotalCharge(map.get(s)==null?BigDecimal.ZERO:map.get(s)
						.add(map1.get(s)==null?BigDecimal.ZERO:map1.get(s))
						.add(map2.get(s)==null?BigDecimal.ZERO:map2.get(s))
						.add(pfra.getPaiFeiRuleById(customer.getPfruleid())==null ? new BigDecimal("0"):pfra.getPaiFeiRuleById(customer.getPfruleid()).getJushouPFfee())
						);
			
				ls.add(sv);
		}
		return ls;
	}
	
	@RequestMapping("/totalMoneyDuiBi")
	@ResponseBody
	public List<SerachCustomerBillContractVO> totalMoneyDuiBi(HttpServletRequest req){
		String batches=req.getParameter("billBatches");
			CustomerBillContract cc=customerbillcontractdao.findCustomerBillContractByBillBatches(batches);		
				List<ImportBillExcel> lbe=customerbillcontractdao.findImportBillExcelByBatches(batches);	
					String cwbs[]=cc.getCwbs().toString().split(",");
						StringBuilder sb = new StringBuilder();
							String cwbss="";
		/*BigDecimal allmoney=new BigDecimal("0");*/
		for(String s:cwbs){
			SerachCustomerBillContractVO c=customerbillcontractdao.findSerachCustomerBillContractVOBycwb(s);
			
			for(ImportBillExcel l:lbe){   //订单相同，总额却不同的，对比不上的不比对
					if(s.equals(l.getCwb())){
						int a=c.getTotalCharge().compareTo(l.getJijiaMoney().add(l.getXuzhongMoney()).add(l.getFandanMoney()).add(l.getFanchengMoney()).add(l.getDaishoukuanshouxuMoney()).add(l.getPosShouxuMoney()).add(l.getBaojiaMoney()).add(l.getBaozhuangMoney()).add(l.getGanxianbutieMoney()));
						    if(a==-1||a==1){
						    	sb.append(l.getCwb()+",");
						    }
					}
			}
		}
		List<SerachCustomerBillContractVO> ls= new ArrayList<SerachCustomerBillContractVO>();
			if(sb.length()>0){
					cwbss=sb.substring(0,sb.length()-1).trim().toString(); //订单相同，总额却不同的cwbs
					
						String cwbsss[]=cwbss.split(",");
		
		
						for(String str:cwbsss){
			
							SerachCustomerBillContractVO c=customerbillcontractdao.findSerachCustomerBillContractVOBycwb(str);
							
							ImportBillExcel l=customerbillcontractdao.findImportBillExcelBycwbAndBatches(str,batches);
							
							c.setImporttotalCharge(l.getJijiaMoney().add(l.getXuzhongMoney()).add(l.getFandanMoney()).add(l.getFanchengMoney()).add(l.getDaishoukuanshouxuMoney()).add(l.getPosShouxuMoney()).add(l.getBaojiaMoney()).add(l.getBaozhuangMoney()).add(l.getGanxianbutieMoney()));
							ls.add(c);
			
						}
		
		}
		
		return ls;
	}
	
	@RequestMapping("/panDuanDateShiFouChongDie")
	@ResponseBody
	public String panDuanDateShiFouChongDie(HttpServletRequest req){
		String customerid=req.getParameter("crecustomerId");		
		String creend=req.getParameter("enddate");
		String crestart=req.getParameter("startdate");
		String dateState=req.getParameter("dateState");

		//根据客户id和账单状态找出符合条件的账单
		List<CustomerBillContract> l=customerbillcontractdao.findbillByCustomerid(Long.valueOf(customerid),Long.valueOf(dateState));
		String dateType=CwbDateEnum.getTextByValue(Integer.valueOf(dateState)).toString();
		String dateChongFu="";
		StringBuffer sb = new StringBuffer();
		if(l.size()>0){
		for(CustomerBillContract c:l){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  	
			String startdate=c.getDateRange().substring(0,10).trim(); //分别从账单中提取日期范围
	    	String enddate=c.getDateRange().substring(11,21).trim();
			try {
				Date d = df.parse(startdate);
				
				Date d1 = df.parse(enddate);
				
				Date d2 = df.parse(crestart);
				
				Date d3 = df.parse(creend);
				
				
				if((d2.getTime()>=d.getTime()&&d2.getTime()<=d1.getTime())||(d3.getTime()>=d.getTime()&&d3.getTime()<=d1.getTime())){
					sb.append(c.getDateRange()+",");   //通过比较是否在账单范围内判断账单创建是否重复
				}
				
			} catch (ParseException e) {   
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}	
		if(sb.length()>0){
				dateChongFu=sb.substring(0,sb.length()-1).toString();
		}
		
		if(dateChongFu==null||dateChongFu.equals("")){  //如果不重复返回1直接创建
			
			return "{\"success\":1}";
		}
		
			return "{\"success\":0,\"successdata\":\""+dateChongFu+"\",\"successdateType\":\""+dateType+"\"}";

	}
	 
	
	
	


	@RequestMapping("/findImportBillExcelByCwb")
	@ResponseBody
	public ImportBillExcel findImportBillExcelByCwb(HttpServletRequest req){
		String cwb = req.getParameter("cwb");	
		//根据订单号查出相对应的导入数据并且返回
		ImportBillExcel ibelist=customerbillcontractdao.findImportBillExcelBycwb(cwb);	
		return ibelist;	
	}
	  
	
	/*
	 * 根据前台传过来的订单和批次，找出相对应的账单，并将导入的总和赋值给派费合计
	 */   
	@RequestMapping("/changeImportToSystemMoney")
	@ResponseBody
	public String changeImportToSystemMoney(HttpServletRequest req){
		
		String cwb = req.getParameter("cwb");
		String billBatches = req.getParameter("billBatches");
		String importtotalCharge = req.getParameter("importtotalCharge");
		BigDecimal bd = new BigDecimal(importtotalCharge);
		customerbillcontractdao.changeImportToDmpMoney(cwb, billBatches, bd);
		
		return "{\"success\":0,\"successdata\":\"修改成功\"}";
	}
	

	@RequestMapping("/changeImportToSystemAllMoney")
	@ResponseBody
	public String changeImportToSystemAllMoney(HttpServletRequest req){
		String billBatches = req.getParameter("billBatches");
		//根据批次号查出批次为当前批次的导入数据
		List<ImportBillExcel> lm=customerbillcontractdao.findImportBillExcelByBatches(billBatches);
		//根据批次号查出批次为当前批次的订单
		List<SerachCustomerBillContractVO> ls=customerbillcontractdao.findSerachCustomerBillContractVOByBatches(billBatches);
		for(ImportBillExcel l:lm){
			for(SerachCustomerBillContractVO s:ls){
				if(l.getCwb().equals(s.getCwb())){    //如果订单号相同，将订单号相同的导入的派费合计赋值给相对应订单好的订单中的派费合计
					customerbillcontractdao.changeImportToDmpMoney(l.getCwb(),billBatches, l.getJijiaMoney().add(l.getXuzhongMoney()).add(l.getFandanMoney()).add(l.getFanchengMoney()).add(l.getDaishoukuanshouxuMoney()).add(l.getPosShouxuMoney()).add(l.getBaojiaMoney()).add(l.getBaozhuangMoney()).add(l.getGanxianbutieMoney()));
				}
			}
		}
		

		return "{\"success\":0,\"successdata\":\"修改成功\"}";
	}

}
