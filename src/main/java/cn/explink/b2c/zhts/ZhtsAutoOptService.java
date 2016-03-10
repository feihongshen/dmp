package cn.explink.b2c.zhts;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderTrackDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.SystemInstallService;
import cn.explink.service.WhareHouseToCommonService;

@Service
public class ZhtsAutoOptService {
	private static Logger logger = LoggerFactory.getLogger(ZhtsAutoOptService.class);

	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	OrderTrackDAO orderTrackDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	WhareHouseToCommonService whareHouseToCommonService;
	@Autowired
	CommonDAO commonDAO;
	
	private static int LOOPCOUNT=20; //最大循环次数
	
	/**
	 * 自动入库 出库
	 * @return
	 */
	public void autoOperatorInputOut(){
		
		String  inandOutputFlag = systemInstallService.getParameter("zhonghao_input_output"); //1/自动、0/手动
		if(inandOutputFlag==null||!"1".equals(inandOutputFlag)){
			logger.info("未开启中浩途胜自动入库出库功能");
			return ;
		}
		
		List<Customer> customerList= customerDAO.getAllCustomers();
		if(customerList==null||customerList.size()==0){
			return ;
		}
				
		User operatorUser = this.getOperatorUser();
		
		for(Customer customer:customerList){
			autoOperatorWareHouse(operatorUser, customer,ZhtsAutoFlowEnum.RuKu.getValue()); //自动入库
			autoOperatorWareHouse(operatorUser, customer,ZhtsAutoFlowEnum.ChuKu.getValue()); //自动入库
		}

		
	}
	
	
	
	/**
	 * 自动操作承运商确认出库
	 */
	public void autoOperatorCarrierConfirm(){
		
		String  carrierConfirmFlag = systemInstallService.getParameter("zhonghao_confirmoutput"); //1/自动、0/手动
		if(carrierConfirmFlag==null||!"1".equals(carrierConfirmFlag)){
			logger.info("未开启中浩途胜自动承运商确认出库功能");
			return ;
		}
		
		List<Common> commonList = commonDAO.getAllCommons();
		if(commonList==null||commonList.size()==0){
			return ;
		}
		String commencodes = getAllCommonCode(commonList);
		
		 whareHouseToCommonService.auditCommen(commencodes, 0, 0,500);
		
	}



	private String getAllCommonCode(List<Common> commonList) {
		String commencodes="";
		for(Common common:commonList){
			commencodes+="'"+common.getCommonnumber()+"',";
		}
		if(commencodes.contains(",")){
			commencodes=commencodes.substring(0,commencodes.length()-1);
		}
		return commencodes;
	}
	
	
	
	
	
	
	

	private void autoOperatorWareHouse(User operatorUser, Customer customer,long flowordertype) {
		
		for(int i=0;i<LOOPCOUNT;i++){
			List<CwbOrder> orderList = cwbDAO.getCwbOrderListByStatusAndCustomerId(flowordertype,customer.getCustomerid(),200);
			if(orderList == null || orderList.size() == 0){
				return;
			}
			executorManager(orderList, customer, operatorUser, flowordertype);
		}
		
	}
	
	private User getOperatorUser() {
		String  branchid = systemInstallService.getParameter("zhonghao_warehousebranchid"); //导入库房机构ID
		long wareHouseBranchid=(branchid==null||branchid.isEmpty())? branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()).get(0).getBranchid():Long.valueOf(branchid);
		
		List<User> userlist = this.userDAO.getAllUserbybranchid(wareHouseBranchid); //
		if(userlist==null||userlist.size()==0){
			return userDAO.getUserByUserid(1); //默认管理员
		}
		
		return userlist.get(0);
	}


	
	public void executorManager(List<CwbOrder> cwbOrderList,Customer customer,User operatorUser,long flowordertype) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		int threadCounts=10;
		int totalSize=cwbOrderList.size();
			
		int len=totalSize/threadCounts;//平均分割List   
        if(len==0){   
           threadCounts=totalSize;//采用一个线程处理List中的一个元素   
           len=totalSize/(threadCounts==0?1:threadCounts);//重新平均分割List   
        }   
        CyclicBarrier  barrier=new CyclicBarrier(threadCounts+1);   
        for(int i=0;i<threadCounts;i++){     //创建线程任务   
       	 
       	List<CwbOrder> sublist = null;
       	 
        if(i==threadCounts-1){ //最后一个线程承担剩下的所有元素的计算   
       	   sublist = cwbOrderList.subList(i*len,totalSize);
        }else{   
           sublist = cwbOrderList.subList(i*len, len*(i+1)>totalSize?totalSize:len*(i+1));
        }   
           
           executor.execute(new SubExcuteWorkTask(sublist,customer,cwbOrderService,operatorUser,flowordertype,barrier));   
        }
        
        try {   
            barrier.await();//关键，使该线程在障栅处等待，直到所有的线程都到达障栅处   
        } catch (Exception e) {   
       	 	logger.error("多线程执行异常:"+Thread.currentThread().getName(),e);
        }  
        executor.shutdown();   
				
	}

}


class SubExcuteWorkTask implements Runnable{
	private List<CwbOrder> cwbOrderList;
	private CyclicBarrier barrier;
	private Customer customer;
	private long flowordertype;
	private CwbOrderService cwbOrderService;
	private User operatorUser;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public SubExcuteWorkTask(List<CwbOrder> cwbOrderList,Customer customer,CwbOrderService cwbOrderService,User operatorUser,long flowordertype,CyclicBarrier  barrier){
		this.cwbOrderList=cwbOrderList;
		this.barrier=barrier;
		this.customer=customer;
		this.flowordertype=flowordertype;
		this.cwbOrderService=cwbOrderService;
		this.operatorUser=operatorUser;
	}
	
	
	@Override
	public void run() {
		try {
			
			if(flowordertype==ZhtsAutoFlowEnum.RuKu.getValue()){
				excutorIntoWareHouse();
			}
			
			if(flowordertype==ZhtsAutoFlowEnum.ChuKu.getValue()){
				excutorOutWareHouse();
			}
			
			barrier.await();
		} catch (Exception e) {
			logger.error("执行自动调用入库出库方法异常",e);
		} 
		
	}

	
	/**
	 * 执行入库操作
	 */
	private void excutorIntoWareHouse() {
		for(CwbOrder cwbOrder:cwbOrderList){
			if (customer.getIsypdjusetranscwb() == 1) { // 使用一票多件模式
				String transcwbs=cwbOrder.getTranscwb();
				if(transcwbs==null||transcwbs.isEmpty()){
					transcwbs=cwbOrder.getCwb();
				}
				String[] split = transcwbs.split(cwbOrderService.getSplitstring(transcwbs));
				for (String transcwb : split) {
					cwbOrderService.intoWarehous(operatorUser, cwbOrder.getCwb(), transcwb, customer.getCustomerid(), 0, 0, "系统自动操作", "", false);
				}

			} else {
				cwbOrderService.intoWarehous(operatorUser, cwbOrder.getCwb(), cwbOrder.getCwb(), customer.getCustomerid(), 0, 0, "系统自动操作", "", false);
			}
		}
	}
	
	/**
	 * 执行出库操作
	 */
	private void excutorOutWareHouse() {
		
		
		for(CwbOrder cwbOrder:cwbOrderList){
			
			long matchBranchId=cwbOrder.getDeliverybranchid();
			
			
			if (customer.getIsypdjusetranscwb() == 1) { // 使用一票多件模式
				String transcwbs=cwbOrder.getTranscwb();
				if(transcwbs==null||transcwbs.isEmpty()){
					transcwbs=cwbOrder.getCwb();
				}
				String[] split = transcwbs.split(cwbOrderService.getSplitstring(transcwbs));
				for (String transcwb : split) {
					cwbOrderService.outWarehous(operatorUser,  cwbOrder.getCwb(), transcwb, 0, 0, matchBranchId,0, true, "系统自动操作", "", 0, false, false);
				}

			} else {
				cwbOrderService.outWarehous(operatorUser,  cwbOrder.getCwb(), cwbOrder.getCwb(), 0, 0, matchBranchId,0, true, "系统自动操作", "", 0, false, false);
			}
		}
	}
	 
}
