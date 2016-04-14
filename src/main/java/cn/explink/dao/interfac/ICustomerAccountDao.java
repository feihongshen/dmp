package cn.explink.dao.interfac;

import cn.explink.domain.CustomerAccountPO;

public interface ICustomerAccountDao {

	/**
	 * 获取客户信息
	 * @param customerId
	 * @return
	 */
	public CustomerAccountPO findCustomer(String customerId) ;
}
