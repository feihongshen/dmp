package cn.explink.service.addressmatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AddressCustomerStationDao;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressCustomerStationVO;

@Service
public class AddressCustomerStationService {

	@Autowired
	AddressCustomerStationDao addressCustomerStationDao;

	// 获取全部记录
	public List<AddressCustomerStationVO> getAllCustomerStations() {
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getAllCustomerStations();
		return list;
	}

	// 查询客户id不重复的记录数
	public int getAllCountByCustomerId() {
		return this.addressCustomerStationDao.getAllCountByCustomerId();
	}

	// 获取页面所需要的map记录
	public HashMap<String, List<AddressCustomerStationVO>> getCustomerStations() {
		// @SuppressWarnings("unchecked")
		HashMap<String, List<AddressCustomerStationVO>> map = new HashMap<String, List<AddressCustomerStationVO>>();
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getCustomerStationsPage();
		for (AddressCustomerStationVO addressCustomerStationVO : list) {
			map.put("" + addressCustomerStationVO.getCustomerName(), this.addressCustomerStationDao.getCustomerStationByCustomerId(addressCustomerStationVO.getCustomerid()));
		}
		return map;
	}

	// 根据id获取一条记录
	public AddressCustomerStationVO getCustomerStationByid(Long id) {
		return this.addressCustomerStationDao.getCustomerStationByid(id);
	}

	// 根据客户id获取一条记录
	public List<AddressCustomerStationVO> getCustomerStationByCustomerid(Long customerid) {
		return this.addressCustomerStationDao.getCustomerStationByCustomerid(customerid);
	}

	// 根据customerid创建多条记录
	public void create(String customerName, String stationName, User user) {
		String[] stationNames = stationName.split(",");
		int length = stationNames.length;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		for (int a = 0; a < length; a++) {

			this.addressCustomerStationDao.create(customerName, stationNames[a], user.getUserid(), user.getUsername(), df.format(new Date()));
		}
	}

	// 根据客户id修改站点信息
	public void update(Long customerid) {
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getCustomerStationByCustomerid(customerid);
	}

	// 根据id删除记录
	public void delById(Long id) {
		this.addressCustomerStationDao.delById(id);
	}
}
