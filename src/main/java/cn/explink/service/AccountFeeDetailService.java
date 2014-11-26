package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AccountFeeDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.AccountFeeDetail;

@Service
public class AccountFeeDetailService {
	@Autowired
	AccountFeeDetailDAO accountFeeDetailDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	DataStatisticsService dataStatisticsService;

	private Logger logger = LoggerFactory.getLogger(AccountFeeDetailService.class);

	/**
	 * 分页查找款项明细列表
	 * 
	 * @param page
	 * @param branchid
	 * @param detailname
	 * @return List<AccountFeeDetail>
	 */
	public List<AccountFeeDetail> getAccountFeeDetailList(long page, long feetypeid, String branchname, String detailname) {
		try {

			List<AccountFeeDetail> detailList = accountFeeDetailDAO.getAccountFeeDetailList(page, feetypeid, branchname, detailname);
			// //处理列表页关联的站点名称
			// List<AccountFeeDetail> list=new ArrayList<AccountFeeDetail>();
			// if(!detailList.isEmpty()){
			// //站点List
			// List<Branch>
			// branchList=branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
			// for (AccountFeeDetail o : detailList) {
			// o.setBranchname(dataStatisticsService.getQueryBranchName(branchList,o.getBranchid()));
			// list.add(o);
			// }
			// }
			// return list;
			return detailList;

		} catch (Exception e) {
			logger.info("分页查找款项明细列表异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 款项明细列表合计条数
	 * 
	 * @param branchid
	 * @param detailname
	 * @return long
	 */
	public long getAccountFeeDetailCount(long feetypeid, String branchname, String detailname) {
		try {
			return accountFeeDetailDAO.getAccountFeeDetailCount(feetypeid, branchname, detailname);
		} catch (Exception e) {
			logger.info("款项明细列表合计条数异常：" + e);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 款项明细保存
	 * 
	 * @param AccountFeeType
	 * @return boolean
	 */
	public boolean createAccountFeeDetail(AccountFeeDetail o) {
		try {
			accountFeeDetailDAO.createAccountFeeDetail(o);
			return true;
		} catch (Exception e) {
			logger.info("款项明细保存异常：" + e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 款项明细按ID查找
	 * 
	 * @param feetypeid
	 * @return AccountFeeType
	 */
	public AccountFeeDetail getAccountFeeDetailById(long feedetailid) {
		try {
			AccountFeeDetail accountFeeDetail = accountFeeDetailDAO.getAccountFeeDetailById(feedetailid);
			/*
			 * //站点List List<Branch>
			 * branchList=branchDAO.getBranchBySiteType(BranchEnum
			 * .ZhanDian.getValue());
			 * accountFeeDetail.setBranchname(dataStatisticsService
			 * .getQueryBranchName(branchList,accountFeeDetail.getBranchid()));
			 */
			return accountFeeDetail;
		} catch (Exception e) {
			logger.info("款项明细按款项明细ID查找异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 款项明细修改
	 * 
	 * @param feetypeid
	 * @param feetype
	 * @param feetypename
	 * @return boolean
	 */
	public boolean saveAccountFeeType(AccountFeeDetail o) {
		try {
			accountFeeDetailDAO.saveAccountFeeDetail(o);
			return true;
		} catch (Exception e) {
			logger.info("款项明细修改异常：" + e);
			e.printStackTrace();
			return false;
		}
	}
}
