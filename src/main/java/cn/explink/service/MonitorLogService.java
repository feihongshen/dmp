package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderView;
import cn.explink.controller.MonitorKucunDTO;
import cn.explink.controller.MonitorKucunSim;
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
	public  List<MonitorLogSim> getMonitorLogByBranchidWithZhanDianZaiZhanZiJin(String branchids,String customerids,String wheresql) {
		return monitorDAO.getMonitorLogByBranchidWithZhandianzaiZhanZiJin(branchids,customerids,wheresql) ;
	}
	public  List<MonitorKucunSim> getMonitorKucunByBranchid(String branchids,String wheresql) {
		
		return monitorKucunDAO.getMonitorLogByBranchid(branchids,wheresql);
	}
	
	public  List<CwbOrderView> getMonitorLogByType(String branchids,String branchids1,String branchids2,String customerid,String type,long page) {
		
		
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
		
		if("tuihuoyichuzhan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(40) AND startbranchid NOT IN("+branchids+") ", page);
		}
		if("zhongzhuanyichuzhan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(6,14) AND startbranchid NOT IN("+branchids+") ", page);
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
/*		if("zhandianzaizhanzijin".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByTypeAndNotIn("7,8,9,35,36", branchids,customerid, page);
			String cwbs ="";
			if (cwbList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs = "'--'";
			}
			clist = cwbDAO.getCwbOrderByCwbs(cwbs);
		
		}*/
		if("zhongzhuankuyichuweidaozhan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=6 and `startbranchid` IN("+branchids1+") or flowordertype=14", page);
		}
		if("tuihuokutuihuozaitouweidaozhan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=6 and startbranchid IN("+branchids2+") ", page);
		}
		if("tuikehuweishoukuan".equals(type)){
			clist = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " fncustomerbillverifyflag=0 and flowordertype=34", page);
		}
		
		if("all".equals(type)){
			
			cwbList =   monitorDAO.getMonitorLogByTypeAndNotIn("7,8,9,35,36", branchids,customerid, -9);
			String cwbs ="";
			if (cwbList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs = "'--'";
			}
			
	/*		String wheresql = " (flowordertype IN(1,2,6,12,14,15,27) " +
					" OR (flowordertype = 4 AND currentbranchid IN("+branchids+" ) ) " +
					" OR (flowordertype IN(14,40)  AND  startbranchid NOT IN("+branchids+"))" +
					" OR cwb IN("+cwbs+")" +
					" OR (flowordertype=6 and `startbranchid` IN("+branchids1+"))" +
					" OR (flowordertype=6 and startbranchid IN("+branchids2+")) " +
					" OR (fncustomerbillverifyflag=0 and flowordertype=34)" +
					") ";*/
			
			String wheresql = " (flowordertype IN(1,2,6,12,15,27) " +
					" OR (flowordertype = 4 AND currentbranchid IN("+branchids+" ) ) " +
					" OR (flowordertype IN(14,40)  AND  startbranchid NOT IN("+branchids+"))" +
					" OR ((flowordertype=6 and `startbranchid` IN("+branchids1+")) or flowordertype=14) " +
					" OR cwb IN("+cwbs+")" +
					" OR (flowordertype=6 and startbranchid IN("+branchids2+") )" +
					" OR (fncustomerbillverifyflag=0 and flowordertype=34)" +
					") ";
			
			clist =   cwbDAO.getMonitorLogByBranchid(branchids, customerid+"",wheresql, page);
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
	public  long getMonitorLogByTypeCount(String branchids,String branchids1,String branchids2,String customerid,String type) {
		
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
		
		if("tuihuoyichuzhan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(40) AND startbranchid NOT IN("+branchids+") ");
		}
		if("zhongzhuanyichuzhan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype IN(6,14) AND startbranchid NOT IN("+branchids+") ");
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
		
	/*	if("zhandianzaizhanzijin".equals(type)){
			count = cwbDAO.getMonitorLogByBranchidWithZhandianzaizhanzijinOrAll(branchids, customerid+"", " flowordertype IN(7,8,9,35,36) ");
		}*/
		if("zhongzhuankuyichuweidaozhan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", "( flowordertype=6 and `startbranchid` IN("+branchids1+")) or flowordertype=14");
		}
		if("tuihuokutuihuozaitouweidaozhan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " flowordertype=6 and startbranchid IN("+branchids2+") ");
		}
		if("tuikehuweishoukuan".equals(type)){
			count = cwbDAO.getMonitorLogByBranchid(branchids, customerid+"", " fncustomerbillverifyflag=0 and flowordertype=34");
		}
		
		if("all".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeAndNotInCount("7,8,9,35,36", branchids,customerid);
			String wheresql = " (flowordertype IN(1,2,6,12,15,27) " +
					" OR (flowordertype = 4 AND currentbranchid IN("+branchids+" ) ) " +
					" OR (flowordertype IN(14,40)  AND  startbranchid NOT IN("+branchids+"))" +
					" OR ((flowordertype=6 and `startbranchid` IN("+branchids1+")) or flowordertype=14) " +
					" OR (flowordertype=6 and startbranchid IN("+branchids2+") )" +
					" OR (fncustomerbillverifyflag=0 and flowordertype=34)" +
					") ";
			
			long count2 =   cwbDAO.getMonitorLogByBranchidWithZhandianzaizhanzijinOrAll(branchids, customerid+"", wheresql);
			count = count + count2;
		}
		
		
		return count;
	}
	public  List<CwbOrderView> getMonitorKucunByType(String branchids,String branchid,String type,long page) {
		
		
		List<String>  cwbList = new ArrayList<String>();
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		
		if("kucun".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorKucunByType("1", branchid, page,branchids);
			String cwbs ="";
			if (cwbList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs = "'--'";
			}
			 clist = cwbDAO.getCwbOrderByCwbs(cwbs);
		}
		
		if("yichukuzaitu".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorLogByType(" op.flowordertype in(6,14,40,27) ", branchid, page,branchids);
			String cwbs ="";
			if (cwbList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs = "'--'";
			}
			 clist = cwbDAO.getCwbOrderByCwbs(cwbs);
		}
		
		
		
		 if("weiruku".equals(type)){
			 clist = cwbDAO.getMonitorLogByType(" flowordertype in(1,2) ", branchid, page, branchids);
		 }
		 
		 if("all".equals(type)){
			cwbList =   monitorKucunDAO.getMonitorKucunByTypeNoPage("1", branchid,branchids);
			String cwbs1 ="";
			if (cwbList.size() > 0) {
				cwbs1 = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs1 = "'--'";
			}
			cwbList =   monitorKucunDAO.getMonitorLogByTypeNoPage( " op.flowordertype in(6,14,40,27) ", branchid,branchids);
			String cwbs2 ="";
			if (cwbList.size() > 0) {
				cwbs2 = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
			} else {
				cwbs2 = "'--'";
			}
			
			clist = cwbDAO.getMonitorLogByTypeAll(" cwb in("+cwbs1+")" +
			 		" or cwb in("+cwbs2+")", branchid, page, branchids);
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
	public  long getMonitorKucunByTypeCount(String branchids,String branchid,String type) {
		
		long count = 0;
		if("kucun".equals(type)){
			count =   monitorKucunDAO.getMonitorKucunByTypeCount("1", branchid,branchids);
		}
		if("yichukuzaitu".equals(type)){
			count =   monitorKucunDAO.getMonitorLogByTypeCount("6,14,40,27", branchid,branchids);
		}
		if("weiruku".equals(type)){
			count =   cwbDAO.getMonitorLogByType(" flowordertype in(1,2) ", branchid,  branchids);
		}
		
		
		if("all".equals(type)){
			count =   monitorKucunDAO.getMonitorKucunByTypeCount("1", branchid,branchids);
			count +=   monitorKucunDAO.getMonitorLogByTypeCount("6,14,40,27", branchid,branchids);
			count +=   cwbDAO.getMonitorLogByType(" flowordertype in(1,2) ", branchid,  branchids);
		}
		
		return count;
	}

}
