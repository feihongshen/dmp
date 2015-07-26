package cn.explink.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerBillContractDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.ImportBillExcel;
import cn.explink.domain.VO.CustomerBillContractFindConditionVO;
import cn.explink.domain.VO.CustomerBillContractVO;
import cn.explink.exception.BadExcelException;
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
		if(cwborderlist.size()>0){
			for(CwbOrder str:cwborderlist){
				sb=sb.append("'"+str.getCwb()+"',");
			}
			cwbs=sb.substring(0, sb.length()-1);	
		}
		return cwbs;	
	}
	
	public String listImportBillExcelToString(List<ImportBillExcel> cwborderlist){
		StringBuilder sb = new StringBuilder();
		String cwbs="";	
		if(cwborderlist.size()>0){
			for(ImportBillExcel str:cwborderlist){
				sb=sb.append(str.getCwb()+",");
			}
		
			cwbs=sb.substring(0, sb.length()-1);	
		}
		return cwbs;	
	}
	
	public String DeliveryStatelistToString(List<DeliveryState> cwborderlist){
		StringBuilder sb = new StringBuilder();
		String cwbs="";	
		if(cwborderlist.size()>0){
			for(DeliveryState str:cwborderlist){
				sb=sb.append("'"+str.getCwb()+"',");
			
			}
			cwbs=sb.substring(0, sb.length()-1);	
		}
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
	public List<CwbOrder> findAllByCwbDatelike(String cwbs,String startdate,String enddate,int start,int number) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDatePageLike(cwbs,startdate,enddate,start,number);
		return col;
	}

	public List<CwbOrder> findAllByCwbDateType(String cwbs, String startdate,
			String enddate, String cwbOrderType,int start,int pagesize) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtypePage(cwbs,startdate,enddate,cwbOrderType,start,pagesize);
		return col;
	}
	public List<CwbOrder> findAllByCwbDateTypeLike(String cwbs, String startdate,
			String enddate, String cwbOrderType,int start,int pagesize) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtypePageLike(cwbs,startdate,enddate,cwbOrderType,start,pagesize);
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
	
	public List<CwbOrder> findcwbByCwbsAndDateAndtypeedaolike(String cwbs,
			String string, String string2, String cwbOrderType,int start,int pageSize) {		
		return edao.findcwbByCwbsAndDateAndtypePageLike(cwbs, string, string2, cwbOrderType,start,pageSize);
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
			BigDecimal totalCharge, String remark,String cwbtype, long dateState,String cwbs) {
		String createBillDate=DateTimeUtil.getNowDate();
		long cwbType=-1;
		if(cwbtype!=null&&!cwbtype.equals("")){
			cwbType=Long.valueOf(cwbtype);
		}
		customerbillCcontractdao.addBill(billBatches,initbillState,customerid,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark,createBillDate,cwbType,dateState,cwbs);
		
	}

	public void EditBill(CustomerBillContractVO cbv) {
		// TODO Auto-generated method stub
		customerbillCcontractdao.EditBill(cbv);
	}

	/*public List<CustomerBillContract> getAllcustomerbillcontract() {
		List<CustomerBillContract> cbclist=customerbillCcontractdao.dateAllbillBatche();
		return cbclist;
	}*/

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
	

	public List<ImportBillExcel> getUploadExcel(InputStream fis) {
		List<ImportBillExcel> rows = new ArrayList<ImportBillExcel>();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw new BadExcelException();
			}
			
			HSSFSheet sheet = xwb.getSheetAt(0);
			for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
				HSSFRow row = sheet.getRow(rowNum); // 从第二行开始，取出每一行
				ImportBillExcel ib = new ImportBillExcel();
				ib.setCwb(formatCell(row.getCell(0)));
				ib.setJijiaMoney(new BigDecimal(formatCell(row.getCell(1))));
				ib.setXuzhongMoney(new BigDecimal(formatCell(row.getCell(2))));
				ib.setFandanMoney(new BigDecimal(formatCell(row.getCell(3))));
				ib.setFanchengMoney(new BigDecimal(formatCell(row.getCell(4))));
				ib.setDaishoukuanshouxuMoney(new BigDecimal(formatCell(row.getCell(5))));
				ib.setPosShouxuMoney(new BigDecimal(formatCell(row.getCell(6))));
				ib.setBaojiaMoney(new BigDecimal(formatCell(row.getCell(7))));
				ib.setBaozhuangMoney(new BigDecimal(formatCell(row.getCell(8))));
				ib.setGanxianbutieMoney(new BigDecimal(formatCell(row.getCell(9))));
				
				rows.add(ib);
			}
		
		} catch (Exception e) {
			e.printStackTrace();

		}
		return rows;
	}
	
	
	public static String formatCell(HSSFCell cell){//判断cell类型
		if(cell==null){
			return "";
		}else{
			if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
				return String.valueOf(cell.getBooleanCellValue());
			}else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
				return String.valueOf(cell.getNumericCellValue());
			}else{
				return String.valueOf(cell.getStringCellValue());
			}
			
		}
	
	}
	
	
	public String getMonthFirstDay(){
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");         
		   Calendar c = Calendar.getInstance();    
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        String first = format.format(c.getTime());
		      return first;
	}
	public String getMonthLastDay(){
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 Calendar ca = Calendar.getInstance();    
	        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
	        String last = format.format(ca.getTime());
	        return last;
	}
	
}

