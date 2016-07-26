package cn.explink.service.express;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressInfoDetailVO;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;

/**
 *
 * @description Service基类，继承后可获得公用方法
 * @author  刘武强
 * @data   2015年8月5日
 */
public class ExpressCommonService {
	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private ProvinceDAO provinceDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountyDAO countyDAO;
	/**
	 *显示日志的对象
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	/**
	 *
	 * @Title: getSessionUser
	 * @description 获取登录人员的branchid
	 * @author 刘武强
	 * @date  2015年8月7日上午11:45:15
	 * @param  @return
	 * @return  User
	 * @throws
	 */
	public User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		
		//Modified by leoliao at 2016-07-26 如果session用户的branchid为0则重新获取用户branchid
		User currUser = userDetail.getUser();
		userService.convertSessionUserBranchId(currUser);
		
		return currUser;
		//return userDetail.getUser();
		//Modified end
	}

	/**
	 *
	 * @Title: getUserByUserid
	 * @description 通过userid获取user信息
	 * @author 刘武强
	 * @date  2015年8月10日下午9:54:24
	 * @param  @param userid
	 * @param  @return
	 * @return  User
	 * @throws
	 */
	public User getUserByUserid(Long userid) {
		return this.userDAO.getUserByUserid(userid);
	}

	/**
	 *
	 * @Title: getAdressInfoByBranchid
	 * @description 根据登陆的id获取其具体的省市信息(没有区/县、街道信息)
	 * @author 刘武强
	 * @date  2015年8月17日下午8:01:02
	 * @param  @return
	 * @return  AdressInfoDetailVO
	 * @throws
	 */
	public AdressInfoDetailVO getAdressInfoByBranchid() {
		AdressInfoDetailVO adressInfoDetailVO = new AdressInfoDetailVO();
		User user = this.getSessionUser();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		adressInfoDetailVO.setProvinceId(branch.getBranchprovinceid());
		adressInfoDetailVO.setProvinceName(branch.getBranchprovince());
		adressInfoDetailVO.setCityId(branch.getBranchcityid());
		adressInfoDetailVO.setCityName(branch.getBranchcity());
		//adressInfoDetailVO.setCountyId(branch);
		adressInfoDetailVO.setCountyName(branch.getBrancharea());

		return adressInfoDetailVO;
	}

	/**
	 *
	 * @Title: getProvinceId
	 * @description 获取站点的省（先对比省的id，如果没对比上，在对比省的name，如果还没有则返回0）
	 * @author 刘武强
	 * @date  2015年8月17日下午8:13:35
	 * @param  @return
	 * @return  long
	 * @throws
	 */
	public long getProvinceId() {
		AdressInfoDetailVO adressInfoDetailVO = this.getAdressInfoByBranchid();
		List<AdressVO> Adresslist = this.provinceDAO.getAllProvince();
		//对比省的id
		Long provinceId = adressInfoDetailVO.getProvinceId();
		if (provinceId != null) {
			for (AdressVO temp : Adresslist) {
				if (provinceId == temp.getId()) {
					return provinceId;
				}
			}
		}
		//对比省的name
		String provinceName = adressInfoDetailVO.getProvinceName();
		for (AdressVO temp : Adresslist) {
			if ((provinceName != null) && provinceName.equals(temp.getName())) {
				return temp.getId();
			}
		}
		return 0;
	}

	/**
	 *
	 * @Title: getCityId
	 * @description 获取站点所在市id
	 * @author 刘武强
	 * @date  2015年10月15日下午3:06:23
	 * @param  @return
	 * @return  long
	 * @throws
	 */
	public long getCityId() {
		AdressInfoDetailVO adressInfoDetailVO = this.getAdressInfoByBranchid();
		//对比省的id
		Long provinceId = this.getProvinceId();
		if (provinceId == 0) {
			return 0;
		}
		Long cityId = adressInfoDetailVO.getCityId();
		List<AdressVO> Adresslist = this.cityDAO.getCityOfProvince(provinceId.intValue());
		if (cityId != null) {
			for (AdressVO temp : Adresslist) {
				if (cityId == temp.getId()) {
					return cityId;
				}
			}
		}
		//对比市的name
		String cityName = adressInfoDetailVO.getCityName();
		for (AdressVO temp : Adresslist) {
			if ((cityName != null) && cityName.equals(temp.getName())) {
				return temp.getId();
			}
		}
		return 0;
	}

	/**
	 *
	 * @Title: getCountyId
	 * @description 获取站点所在区/县的id
	 * @author 刘武强
	 * @date  2015年10月15日下午3:07:54
	 * @param  @return
	 * @return  long
	 * @throws
	 */
	public long getCountyId() {
		AdressInfoDetailVO adressInfoDetailVO = this.getAdressInfoByBranchid();
		Long cityId = this.getCityId();
		if (cityId == 0) {
			return 0;
		}
		Long countyId = adressInfoDetailVO.getCountyId();
		List<AdressVO> Adresslist = this.countyDAO.getCountyOfCity(cityId.intValue());
		if (countyId != null) {
			for (AdressVO temp : Adresslist) {
				if (countyId == temp.getId()) {
					return countyId;
				}
			}
		}
		//对比name
		String countyName = adressInfoDetailVO.getCountyName();
		for (AdressVO temp : Adresslist) {
			if ((countyName != null) && countyName.equals(temp.getName())) {
				return temp.getId();
			}
		}
		return 0;
	}

}
