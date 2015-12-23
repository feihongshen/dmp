package cn.explink.service.express;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.service.express.impl.ExpressBillCommonServiceImpl;

/**
 * 客户运费结算
 * @author jiangyu 2015年8月11日
 *
 */
@Service
public class ExpressCustomerFreightCheckService extends ExpressBillCommonServiceImpl{
	@Autowired
	ExpressFreightBillSelectDAO selectDao;
	
	/**
	 * 获取客户信息
	 * @return
	 */
	public List<SelectReturnVO> initCutomerThroughCwb() {
		return selectDao.getCustomerSelectInfo();
	}
	
	/**
	 * 获取所有账单的缓存信息
	 * @return
	 */
	public Map<Long, String> getCustomerName(){
		return getMapInfo(selectDao.getCustomerSelectInfo());
	}
	
}
