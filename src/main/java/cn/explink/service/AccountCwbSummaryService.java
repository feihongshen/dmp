package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AccountCwbSummaryDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.AccountCwbSummary;
import cn.explink.domain.Branch;
import cn.explink.domain.User;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Service
public class AccountCwbSummaryService {
	private Logger logger = LoggerFactory.getLogger(AccountCwbSummaryService.class);
	@Autowired
	AccountCwbSummaryDAO accountCwbSummaryDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	BranchDAO branchDAO;

	/**
	 * 获得汇总List
	 * 
	 * @param branchList
	 * @param userList
	 * @param page
	 * @param checkoutstate
	 * @param branchids
	 * @return
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<AccountCwbSummary> getAccountCwbSummaryListByBranchAndUser(List<Branch> branchList, List<User> userList, long page, long checkoutstate, String branchids, long accounttype,
			String starttime, String endtime) {
		try {
			List<AccountCwbSummary> detailList = accountCwbSummaryDAO.getAccountCwbSummaryList(page, checkoutstate, branchids, accounttype, starttime, endtime);
			// 处理列表页关联的站点名称
			List<AccountCwbSummary> list = new ArrayList<AccountCwbSummary>();
			if (!detailList.isEmpty()) {
				for (AccountCwbSummary o : detailList) {
					o.setBranchname(dataStatisticsService.getQueryBranchName(branchList, o.getBranchid()));
					long userid = 0;
					if (checkoutstate == 0) {
						userid = o.getSaveuserid();
					} else {
						userid = o.getUserid();
					}
					o.setUsername(dataStatisticsService.getQueryUserName(userList, userid));
					list.add(o);
				}
			}
			return list;
		} catch (Exception e) {
			logger.info("分页查找结算列表异常：", e);
			return null;
		}
	}
}
