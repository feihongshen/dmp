package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderView;
import cn.explink.controller.MonitorLogDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MonitorDAO;
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
	private CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	
	public  List<MonitorLogDTO> getMonitorLogByBranchid(String branchids,String customerids) {
		
		return monitorDAO.getMonitorLogByBranchid(branchids,customerids);
	}
	
	public  List<CwbOrderView> getMonitorLogByType(String branchids,long customerid,String type,long page) {
		
		
		List<String>  cwbList = new ArrayList<String>();
		
		if("weidaohuo".equals(type)){
			 cwbList =   monitorDAO.getMonitorLogByType("1", customerid, page);
		}
		
		if("tihuo".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("2", customerid, page);
		}
		
		if("ruku".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("4", branchids,customerid, page);
		}
		if("chuku".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("6", branchids,customerid, page);
		}
		if("daozhan".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("7,8,9,35,36", branchids,customerid, page);
		}
		if("zaizhanziji".equals(type)){
			cwbList =   new ArrayList<String>();
		}
		if("yichuzhan".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("6,14,40", branchids,customerid, page);
		}
		if("Zhongzhanruku".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("12",customerid, page);
		}
		if("tuihuoruku".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("15", customerid, page);
		}
		if("tuigonghuoshang".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("27",customerid, page);
		}
		if("tuikehuweishoukuan".equals(type)){
			cwbList =   new ArrayList<String>();
		}
		if("all".equals(type)){
			cwbList =   monitorDAO.getMonitorLogByType("1,2,4,6,7,8,9,12,14,15,27,35,36,40",customerid, page);
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
	public  long getMonitorLogByTypeCount(String branchids,long customerid,String type) {
		
		long count = 0;
		if("weidaohuo".equals(type)){
			 count =   monitorDAO.getMonitorLogByTypeCount("1", customerid);
		}
		if("tihuo".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("2", customerid);
		}
		if("ruku".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("4",branchids, customerid);
		}
		if("chuku".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("6", branchids,customerid);
		}
		if("daozhan".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("7,8,9,35,36", branchids,customerid);
		}
		if("zaizhanziji".equals(type)){
			count =   0;
		}
		if("yichuzhan".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("6,14,40", branchids,customerid);
		}
		if("Zhongzhanruku".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("12",customerid);
		}
		if("tuihuoruku".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("15", customerid);
		}
		if("tuigonghuoshang".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("27",customerid);
		}
		if("tuikehuweishoukuan".equals(type)){
			count =   0;
		}
		if("all".equals(type)){
			count =   monitorDAO.getMonitorLogByTypeCount("1,2,4,6,7,8,9,12,14,15,27,35,36,40",customerid);
		}
		
		
		return count;
	}

}
