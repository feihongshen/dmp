package cn.explink.service.express;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.VO.express.UserInfo;
import cn.explink.service.express.impl.ExpressBillCommonServiceImpl;

@Service
public class ExpressAcrossReceFreightCheckService extends ExpressBillCommonServiceImpl {
	private Logger logger = LoggerFactory.getLogger(ExpressAcrossReceFreightCheckService.class);

	@Autowired
	ExpressFreightBillSelectDAO selectDao;
	@Autowired
	ProvinceDAO provinceDAO;
	@Autowired
	BranchDAO branchDAO;

	/**
	 * 应付省份数据
	 * 
	 * @return
	 */
	public List<SelectReturnVO> initPayAbleProvinceSelect(Long currentBranchId) {
		// 通过机构获取机构的省份
		Branch branch = branchDAO.getBranchByBranchid(currentBranchId);
		return selectDao.getProvinceData(branch.getBranchprovinceid());
	}

	/**
	 * 获取当前登录人的信息
	 * 
	 * @param user
	 * @return
	 */
	public UserInfo initUserInfo(User user) {
		UserInfo userInfo = null;
		// 通过机构获取机构的省份
		Branch branch = branchDAO.getBranchByBranchid(user.getBranchid());
		try {
			SelectReturnVO selectVo = selectDao.getCurrentUserInfo(branch.getBranchprovinceid());
			if (selectVo != null) {
				userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), selectVo.getHiddenValue(), selectVo.getDisplayValue(), user.getUserid(), user.getRealname());
			} else {
				userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), 0L, "", user.getUserid(), user.getRealname());
			}
		} catch (Exception e) {
			logger.info("查询用户所在站点的省份信息异常：" + e.getMessage());
			userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), 0L, "", user.getUserid(), user.getRealname());
		}
		// 查询当前用户站点所在的
		return userInfo;
	}
	/**
	 * 获取所有的省份信息
	 * @return
	 */
	public Map<Long, String> initProvince(){
		Map<Long, String> map = new HashMap<Long, String>();
		List<AdressVO> provinces = provinceDAO.getAllProvince();
		if (provinces!=null&&provinces.size()>0) {
			for (AdressVO adressVO : provinces) {
				map.put(Long.valueOf(adressVO.getId()), adressVO.getName());
			}
		}
		return map;
	}

}
