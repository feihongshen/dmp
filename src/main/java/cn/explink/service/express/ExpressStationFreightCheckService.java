package cn.explink.service.express;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.service.express.impl.ExpressBillCommonServiceImpl;

@Service
public class ExpressStationFreightCheckService extends ExpressBillCommonServiceImpl {
	@Autowired
	ExpressFreightBillSelectDAO selectDao;
	
	/**
	 * 查询站点信息
	 * @return
	 */
	public List<SelectReturnVO> getStationSelectInfo() {
		return selectDao.getBranchData();
	}
	/**
	 * 获取所有站点信息
	 * @return
	 */
	public Map<Long, String> getBranchName(){
		 return getMapInfo(selectDao.getBranchData());
	}
}
