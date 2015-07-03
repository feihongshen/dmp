package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
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

	public List<CwbOrder> findAllByCwbDateType(String cwbs, String startdate,
			String enddate, String cwbOrderType) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtype(cwbs,startdate,enddate,cwbOrderType);
		return col;
	}

	public List<CwbOrder> findcwbByCwbsAndDateAndtypeedao(String cwbs,
			String string, String string2, String cwbOrderType) {		
		return edao.findcwbByCwbsAndDateAndtype(cwbs, string, string2, cwbOrderType);
	}
	
	/**
	 * 
	 * @return 自动生成流水号
	 */
	
	public String getBillBatches(){  
		String NowLiuShuiNum="";
		String initbillBatches="B"+DateTimeUtil.getCurrentDate();
		long initNum=00000;
		String initBillBatches=initbillBatches+initNum;	//B2015070200001
		String billBatche=customerbillCcontractdao.datebillBatche();//取出当前从数据库中查到的最大流水号
		String dateinitBillBatches=initBillBatches.substring(1, 8);
		String datebillBatche=billBatche.substring(1,8);
		if(Integer.valueOf(datebillBatche)<Integer.valueOf(dateinitBillBatches)){
			NowLiuShuiNum="B"+DateTimeUtil.getCurrentDate()+initNum; //新的一天的流水号
		}else if(Integer.valueOf(datebillBatche)==Integer.valueOf(dateinitBillBatches)){
			NowLiuShuiNum=billBatche;  //原有的流水号
		}
		long billbatche=Integer.valueOf(NowLiuShuiNum.substring(9,13))+1;
		String billOneDaoEight=NowLiuShuiNum.substring(0,8);
		String BillBatches=billOneDaoEight+billbatche;
		return BillBatches;
	}

	public List<CustomerBillContract> getCustomerBillContractBySelect(
			String billBatches, long billState, String crestartdate,
			String creenddate, String verificationstratdate,
			String verificationenddate, long customerId, long cwbOrderType,
			String condition, String sequence) {
		
		List<CustomerBillContract> cbclist=customerbillCcontractdao.findCustomerBillContract(billBatches, billState, crestartdate, creenddate, verificationstratdate, verificationenddate, customerId, cwbOrderType, condition, sequence);
		
		return cbclist;
	}

	public void removebill(long id) {
		customerbillCcontractdao.removebill(id);
		
	}

	public void addBill(String billBatches, long initbillState, long id,
			String dateRange, long correspondingCwbNum, long deliveryMoney,
			long distributionMoney, long transferMoney, long refuseMoney,
			long totalCharge, String remark) {
		customerbillCcontractdao.addBill(billBatches,initbillState,id,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark);
		
	}


}
