package cn.explink.service.addressmatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AddressCustomerStationDao;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressCustomerStationVO;

@Service
public class AddressCustomerStationService {

	@Autowired
	AddressCustomerStationDao addressCustomerStationDao;
	@Autowired
	BranchDAO branchDAO;

	// 获取全部记录
	public List<AddressCustomerStationVO> getAllCustomerStations() {
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getAllCustomerStations();
		return list;
	}

	// 查询所有的记录数
	public int getAllCount() {
		return this.addressCustomerStationDao.getAllCount();
	}

	// 查询客户id不重复的记录数
	public int getAllCountByCustomerId() {
		return this.addressCustomerStationDao.getAllCountByCustomerId();
	}

	// 获取页面所需要的map记录(一个客户匹配多站的时候会用到)
	public HashMap<String, List<AddressCustomerStationVO>> getCustomerStations() {
		// @SuppressWarnings("unchecked")
		HashMap<String, List<AddressCustomerStationVO>> map = new HashMap<String, List<AddressCustomerStationVO>>();
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getCustomerStationsPage();
		for (AddressCustomerStationVO addressCustomerStationVO : list) {
			map.put("" + addressCustomerStationVO.getCustomerName(), this.addressCustomerStationDao.getCustomerStationByCustomerId(addressCustomerStationVO.getCustomerid()));
		}
		return map;
	}

	// 获取页面所需要的list(一个客户匹配一个站点用到)带分页
	public List<AddressCustomerStationVO> getCustomerStationsArea(Long page, String customerid, String station) {
		List<AddressCustomerStationVO> list = this.addressCustomerStationDao.getAllCustomerStationsByPage(page, customerid, station);
		List<Branch> listBranchs = this.branchDAO.getAllBranches();
		for (AddressCustomerStationVO addressCustomerStationVO : list) {
			for (Branch branch : listBranchs) {
				if (addressCustomerStationVO.getBranchid() == branch.getBranchid()) {
					String area = branch.getBranchprovince().length()==0?"":(branch.getBranchprovince() + "-") + (branch.getBranchcity().length()==0?"":(branch.getBranchcity() + "-")) + (branch.getBrancharea().length()==0?"":branch.getBrancharea() + "-") +branch.getBranchstreet();
					if(area.endsWith("-")){
						area = area.substring(0,area.length()-1);
					}
					addressCustomerStationVO.setArea(area);
				}
			}
		}
		return list;
	}

	// 根据branchId获取站点区域
	public String getAreaByBranchId(Long branchid) {
		Branch branch = this.branchDAO.getBranchByBranchid(branchid);
		String area = branch.getBranchprovince().length()==0?"":(branch.getBranchprovince() + "-") + (branch.getBranchcity().length()==0?"":(branch.getBranchcity() + "-")) + (branch.getBrancharea().length()==0?"":branch.getBrancharea() + "-") +branch.getBranchstreet();
		if(area.endsWith("-")){
			area = area.substring(0,area.length()-1);
		}
		return area;
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
	public void updateByCustomerId(Long customerid, String stationName, User user) {
		// 根据客户id删掉之前的数据
		this.addressCustomerStationDao.delByCustomerId(customerid);
		// 根据所选择的客户的站点，将所有站点都添加到该客户下
		this.create(customerid.toString(), stationName, user);
	}
	
	//根据id修改站点信息
	public void updateById(Long id, String stationName, User user){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		this.addressCustomerStationDao.updateById(id,stationName,user.getUserid(),user.getUsername(),df.format(new Date()));
	}

	// 根据id删除记录
	public void delById(Long id) {
		this.addressCustomerStationDao.delById(id);
	}
}
