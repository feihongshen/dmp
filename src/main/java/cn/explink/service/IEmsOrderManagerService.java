package cn.explink.service;

import java.util.List;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.domain.Customer;
import cn.explink.domain.QueryCondition;

public interface IEmsOrderManagerService {
	
	public List<Customer> queryAllCustomers() ;
	
	/**
	 * 获得查询符合条件的列表
	 */
	public DataGridReturn queryEmsOrderList(QueryCondition qc) ;

}
