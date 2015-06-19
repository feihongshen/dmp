package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.core.pager.Pager;
import cn.explink.core.pager.PropertyFilter;
import cn.explink.dao.CsPushSmsDao;

@Service
public class CsPushSmsService {

	@Autowired 
	private CsPushSmsDao csPushSmsDao;
	
	/**
	 * 页面数据查询
	 * @param pager
	 * @param filters
	 */
	public void queryData(Pager pager, List<PropertyFilter> filters) {
		csPushSmsDao.queryData(pager,filters);
	}
	
	public CsPushSmsDao getCsPushSmsDao(){
		return csPushSmsDao;
	}
}
