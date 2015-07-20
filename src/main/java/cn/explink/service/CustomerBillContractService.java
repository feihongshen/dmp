package cn.explink.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.VO.CustomerBillContractFindConditionVO;
import cn.explink.domain.VO.CustomerBillContractVO;
import cn.explink.domain.VO.SerachCustomerBillContractVO;
import cn.explink.util.DateTimeUtil;

@Service
public class CustomerBillContractService {
	
	@Autowired
	private CwbDAO cwbdao;
	@Autowired
	private ExportwarhousesummaryDAO edao;
	@Autowired
	private CustomerBillContractDao customerbillCcontractdao;
	
	/**
	 * 
	 * @param cwborderlist
	 * @return 转换集合to字符串
	 */
	
	public String listToString(List<CwbOrder> cwborderlist){
		StringBuilder sb = new StringBuilder();
		String cwbs="";	
			for(CwbOrder str:cwborderlist){
				sb=sb.append("'"+str.getCwb()+"',");
			}
			cwbs=sb.substring(0, sb.length()-1);	
		return cwbs;	
	}
	
	public String DeliveryStatelistToString(List<DeliveryState> cwborderlist){
		StringBuilder sb = new StringBuilder();
		String cwbs="";	
			for(DeliveryState str:cwborderlist){
				sb=sb.append("'"+str.getCwb()+"',");
			}
			cwbs=sb.substring(0, sb.length()-1);	
		return cwbs;	
	}

	public List<CwbOrder> findAllByCwbDate(String cwbs,String startdate,String enddate) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDate(cwbs,startdate,enddate);
		return col;
	}
	
	public List<CwbOrder> findAllByCwbDate(String cwbs,String startdate,String enddate,int start,int number) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDatePage(cwbs,startdate,enddate,start,number);
		return col;
	}

	public List<CwbOrder> findAllByCwbDateType(String cwbs, String startdate,
			String enddate, String cwbOrderType) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtype(cwbs,startdate,enddate,cwbOrderType);
		return col;
	}

	public List<CwbOrder> findcwbByCwbsAndDateAndtypeedao(String cwbs,
			String string, String string2, String cwbOrderType) {		
		return edao.findcwbByCwbsAndDateAndtype(cwbs, string, string2, cwbOrderType);
	}
	
	public List<CwbOrder> findcwbByCwbsAndDateAndtypeedao(String cwbs,
			String string, String string2, String cwbOrderType,int start,int pageSize) {		
		return edao.findcwbByCwbsAndDateAndtypePage(cwbs, string, string2, cwbOrderType,start,pageSize);
	}
	
	/**
	 * 
	 * @return 自动生成流水号
	 */
	
	public String getBillBatches(){  
/*		long initNum=00000;*/
		 Random random = new Random();  		  
	     int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
		String initbillBatches="B"+DateTimeUtil.getCurrentDate()+rannum;	
/*		String initBillBatches=initbillBatches+initNum;	//B2015070200001
		List<CustomerBillContract> list=customerbillCcontractdao.datebillBatche();//取出当前从数据库中查到的最大流水号
		String NowLiuShuiNum="B"+DateTimeUtil.getCurrentDate()+initNum;
		String BillBatche=null;
		if(list.size()>0){
			BillBatche=list.get(0).getBillBatches();
		}
		String BillBatche=list.get(0).getBillBatches();
		String dateinitBillBatches=initBillBatches.substring(1, 8);  //默认初始化的流水号
		String datebillBatche=BillBatche.substring(1,8); //从数据库去除的流水号数字日期
		if(BillBatche!=null){
		if(Integer.valueOf(datebillBatche)<Integer.valueOf(dateinitBillBatches)){
			NowLiuShuiNum="B"+DateTimeUtil.getCurrentDate()+initNum; //新的一天的流水号
		}else if(Integer.valueOf(datebillBatche)==Integer.valueOf(dateinitBillBatches)){
			NowLiuShuiNum=BillBatche;  //原有的流水号
			}
		} 
	
		long billbatche=Integer.valueOf(NowLiuShuiNum.substring(9,13))+1;
		String billOneDaoEight=NowLiuShuiNum.substring(0,8);
		String BillBatches=billOneDaoEight+billbatche;*/
		return initbillBatches;
	}

	public List<CustomerBillContract> getCustomerBillContractBySelect(
			String billBatches, long billState, String crestartdate,
			String creenddate, String verificationstratdate,
			String verificationenddate, long customerId, long cwbOrderType,
			String condition, String sequence, int start, int number) {
		
		List<CustomerBillContract> cbclist=customerbillCcontractdao.findCustomerBillContract(billBatches, billState, crestartdate, creenddate, verificationstratdate, verificationenddate, customerId, cwbOrderType, condition, sequence,start,number);
		
		return cbclist;
	}

	public void removebill(long id) {
		customerbillCcontractdao.removebill(id);
		
	}
	public void removeofEdit(long id) {
		customerbillCcontractdao.removebill(id);
		
	}

	public void addBill(String billBatches, long initbillState, long customerid,
			String dateRange, long correspondingCwbNum, BigDecimal deliveryMoney,
			BigDecimal distributionMoney, BigDecimal transferMoney, BigDecimal refuseMoney,
			BigDecimal totalCharge, String remark,long cwbtype, long dateState,String cwbs) {
		String createBillDate=DateTimeUtil.getNowDate();
		customerbillCcontractdao.addBill(billBatches,initbillState,customerid,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark,createBillDate,cwbtype,dateState,cwbs);
		
	}

	public void EditBill(CustomerBillContractVO cbv) {
		// TODO Auto-generated method stub
		customerbillCcontractdao.EditBill(cbv);
	}

	public List<CustomerBillContract> getAllcustomerbillcontract() {
		List<CustomerBillContract> cbclist=customerbillCcontractdao.dateAllbillBatche();
		return cbclist;
	}

	public CustomerBillContract findCustomerBillContractById(long id) {
		// TODO Auto-generated method stub
		CustomerBillContract cbc=customerbillCcontractdao.findCustomerBillContractById(id);
		return cbc;
	}

	public long findCustomerBillContractCount(String billBatches,
			long billState, String crestartdate, String creenddate,
			String verificationstratdate, String verificationenddate,
			long customerId, long cwbOrderType) {
			long dataCount=customerbillCcontractdao.findCustomerBillContractCount(billBatches, billState, crestartdate, creenddate, verificationstratdate, verificationenddate, customerId, cwbOrderType);
		return dataCount;
	}
	
	public Map<String,Long> getCustomerBillContractCountObject(String jsonStr){
			JSONObject jsonStu = JSONObject.fromObject(jsonStr);
		  CustomerBillContractFindConditionVO cccv=(CustomerBillContractFindConditionVO) JSONObject.toBean(jsonStu, CustomerBillContractFindConditionVO.class);
		  long billState=-1;
		  if(!cccv.getBillState().equals("")){
			  billState=Long.valueOf(cccv.getBillState());
		  }
		  long customerId=-1;
		  if(!cccv.getCustomerId().equals("")){
			  customerId=Long.valueOf(cccv.getCustomerId());
		  }
		  long cwbOrderType=-1;
		  if(!cccv.getCwbOrderType().equals("")){
			  cwbOrderType=Long.valueOf(cccv.getCwbOrderType());
		  }
		  
		  Map<String,Long> map = new HashMap<String, Long>();
		  map.put("billState", billState);
		  map.put("customerId", customerId);
		  map.put("cwbOrderType", cwbOrderType);
		  return map;
	}

	public CustomerBillContract datebillBatche(long id) {
		CustomerBillContract cbc=customerbillCcontractdao.datebillBatcheById(id);
		return cbc;
	}

	public List<CwbOrder> findAllByCwbDateTypePage(String cwbs, String startdate,
			String enddate, String cwbOrderType, int start, int number) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtypePage(cwbs,startdate,enddate,cwbOrderType,start,number);
		return col;
		
	}

}
