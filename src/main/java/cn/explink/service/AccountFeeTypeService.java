package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AccountFeeTypeDAO;
import cn.explink.domain.AccountFeeType;

@Service
public class AccountFeeTypeService {
	@Autowired
	AccountFeeTypeDAO accountFeeTypeDAO;

	private Logger logger = LoggerFactory.getLogger(AccountFeeTypeService.class);

	/**
	 * 分页查找款项列表
	 * 
	 * @param page
	 * @param feetype
	 * @return List<AccountFeeType>
	 */
	public List<AccountFeeType> getAccountFeeTypeList(long page, long feetype) {
		try {
			List<AccountFeeType> list = accountFeeTypeDAO.getAccountFeeTypeList(page, feetype);
			return list;
		} catch (Exception e) {
			logger.info("分页查找款项列表异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 款项列表合计条数
	 * 
	 * @param feetype
	 * @return long
	 */
	public long getAccountFeeTypeCount(long feetype) {
		try {
			return accountFeeTypeDAO.getAccountFeeTypeCount(feetype);
		} catch (Exception e) {
			logger.info("款项列表合计条数异常：" + e);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 款项列表按款项名称查找
	 * 
	 * @param feetypename
	 * @return List<AccountFeeType>
	 */
	public List<AccountFeeType> getAccountFeeTypeByName(String feetypename) {
		try {
			return accountFeeTypeDAO.getAccountFeeTypeByName(feetypename);
		} catch (Exception e) {
			logger.info("款项列表按款项名称查找异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 款项保存
	 * 
	 * @param AccountFeeType
	 * @return boolean
	 */
	public boolean createAccountFeeType(AccountFeeType accountFeeType) {
		try {
			accountFeeTypeDAO.createAccountFeeType(accountFeeType);
			return true;
		} catch (Exception e) {
			logger.info("款项保存异常：" + e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 款项列表按ID查找
	 * 
	 * @param feetypeid
	 * @return AccountFeeType
	 */
	public AccountFeeType getAccountFeeTypeById(long feetypeid) {
		try {
			return accountFeeTypeDAO.getAccountFeeTypeById(feetypeid);
		} catch (Exception e) {
			logger.info("款项列表按款项ID查找异常：" + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 款项修改
	 * 
	 * @param feetypeid
	 * @param feetype
	 * @param feetypename
	 * @return boolean
	 */
	public boolean saveAccountFeeType(long feetypeid, long feetype, String feetypename) {
		try {
			accountFeeTypeDAO.saveAccountFeeType(feetypeid, feetype, feetypename);
			return true;
		} catch (Exception e) {
			logger.info("款项修改异常：" + e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 款项启用停用
	 * 
	 * @param feetypeid
	 * @return boolean
	 */
	public boolean getDelAccountFeeType(long feetypeid) {
		try {
			accountFeeTypeDAO.getDelAccountFeeType(feetypeid);
			return true;
		} catch (Exception e) {
			logger.info("款项启用停用异常：" + e);
			e.printStackTrace();
			return false;
		}
	}

}
