package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderView;
import cn.explink.controller.MonitorKucunDTO;
import cn.explink.controller.MonitorLogDTO;
import cn.explink.controller.MonitorLogSim;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.dao.MonitorKucunDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;

@Service
public class MonitorLogService {

	private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

	@Autowired
	private MonitorDAO monitorDAO;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ReasonDao reasonDao;
	@Autowired
	private RemarkDAO remarkDAO;
	@Autowired
	private MonitorKucunDAO monitorKucunDAO;
	@Autowired
	private CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	
	public  List<MonitorLogSim> getMonitorLogByBranchid(String branchids,String customerids,String wheresql) {
		return monitorDAO.getMonitorLogByBranchid(branchids,customerids,wheresql) ;
	}
	public  List<MonitorKucunDTO> getMonitorKucunByBranchid(String branchids) {
		
		return monitorKucunDAO.getMonitorLogByBranchid(branchids);
	}
	
	public  List<CwbOrderView> getMonitorLogByType(String branchids,long customerid,String type,long page) {
		
		
		List<String>  cwbList = new ArrayList<String>();
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if("weidaohuo".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=1 ", page);
		}
		
		if("tihuo".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=2 ", page);
		}
		
		if("ruku".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype = 4 AND currentbranchid IN("+branchids+") ", page);
		}
		if("chuku".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype = 6 AND `startbranchid` IN("+branchids+") ", page);
		}
		if("daozhan".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByTypeAndNotIn("7,8,9,35,36", branchids,customerid, page);
			String cwbs ="";
			if (cwbList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs = "'--'";
			}
			clist = cwbDAO.getCwbOrderByCwbs(cwbs);
		}
		
		if("yichuzhan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(6,14,40) AND startbranchid NOT IN("+branchids+") ", page);
		}
		if("Zhongzhanruku".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=12 ", page);
		}
		if("tuihuoruku".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=15 ", page);
		}
		if("tuigonghuoshang".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=27 ", page);
		}
		
		if("all".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("1,2,4,6,7,8,9,12,14,15,27,35,36,40",customerid, page);
		}
		
		
		
		List<Customer> customerList = this.customerDAO.getAllCustomersNew();
		List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<Reason> reasonList = this.reasonDao.getAllReason();
		List<Remark> remarkList = this.remarkDAO.getAllRemark();

		// 赋值显示对象
		 List<CwbOrderView> cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, "2015-01-01 00:00:00", "2015-01-01 00:00:00", remarkList);

		return cwbOrderView;
	}
	public  long getMonitorLogByTypeCount(String branchids,long customerid,String type) {
		
		long count = 0;
		if("weidaohuo".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=1 ");
		}
		
		if("tihuo".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=2 ");
		}
		
		if("ruku".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype = 4 AND currentbranchid IN("+branchids+") ");
		}
		if("chuku".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype = 6 AND `startbranchid` IN("+branchids+") ");
		}
		if("daozhan".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeAndNotInCount("7,8,9,35,36", branchids,customerid);
			
		}
		
		if("yichuzhan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(6,14,40) AND startbranchid NOT IN("+branchids+") ");
		}
		if("Zhongzhanruku".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=12 ");
		}
		if("tuihuoruku".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=15 ");
		}
		if("tuigonghuoshang".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=27 ");
		}
		
		if("all".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("7,8,9,35,36",customerid);
		}
		
		
		return count;
	}
	public  List<CwbOrderView> getMonitorKucunByType(String branchids,long branchid,String type,long page) {
		
		
		List<String>  cwbList = new ArrayList<String>();
		
		if("kucun".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorKucunByType("1", branchid, page,branchids);
		}
		
		if("yichukuzaitu".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorLogByType("6,14,40,27", branchid, page,branchids);
		}
		
		if("weiruku".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorLogByType("1",branchid, page,branchids);
		}
		if("yituikehuweifankuan".equals(type)){
			cwbList =   new ArrayList<String>();
		}
		
		if("all".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorKucunByTypeAll("1",branchid, page,branchids);
		}
		
		String cwbs ="";
		if (cwbList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
		} else {
			cwbs = "'--'";
		}
		List<CwbOrder> clist = cwbDAO.getCwbOrderByCwbs(cwbs);
		
		List<Customer> customerList = this.customerDAO.getAllCustomersNew();
		List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<Reason> reasonList = this.reasonDao.getAllReason();
		List<Remark> remarkList = this.remarkDAO.getAllRemark();
		
		// 赋值显示对象
		List<CwbOrderView> cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, "2015-01-01 00:00:00", "2015-01-01 00:00:00", remarkList);
		
		return cwbOrderView;
	}
	public  long getMonitorKucunByTypeCount(String branchids,long branchid,String type) {
		
		long count = 0;
		if("kucun".equals(type)){
			count =   monitorKucunDAO.getMonitorKucunByTypeCount("1", branchid,branchids);
		}
		if("yichukuzaitu".equals(type)){
			count =   monitorKucunDAO.getMonitorLogByTypeCount("6,14,40,27", branchid,branchids);
		}
		if("weiruku".equals(type)){
			count =   monitorKucunDAO.getMonitorLogByTypeCount("1", branchid,branchids);
		}
		if("yituikehuweifankuan".equals(type)){
			count =  0;
		}
		
		if("all".equals(type)){
			count =   monitorKucunDAO.getMonitorKucunByTypeCountAll("1",branchid,branchids);
		}
		
		return count;
	}

}
